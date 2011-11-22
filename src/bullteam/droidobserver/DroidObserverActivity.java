package bullteam.droidobserver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class DroidObserverActivity extends Activity {

	private final static String tag = "DroidObserverActivity";
	private static ConnectivityManager cm;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(tag, "Uruchamianie aplikacji");
		cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		setContentView(R.layout.main);
		startActivity(new Intent(this, WizardActivity.class));
		startService(new Intent(DroidObserverActivity.this,
				ControllerService.class));
		startService(new Intent(DroidObserverActivity.this,
				SendDataService.class));
	}

	@Override
	protected void onDestroy() {
		stopService(new Intent(DroidObserverActivity.this,
				GetLocationService.class));
		stopService(new Intent(DroidObserverActivity.this,
				ControllerService.class));
		stopService(new Intent(DroidObserverActivity.this,
				SendDataService.class));
		super.onDestroy();
		System.runFinalizersOnExit(true);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_prefs) {
			Intent intent = new Intent().setClass(this,
					bullteam.droidobserver.PatientPreferenceActivity.class);
			this.startActivityForResult(intent, 0);
		} else if (item.getItemId() == R.id.menu_quit) {
			Log.d(tag, "Killuje - DroidObserver");
			onDestroy();
		} else if (item.getItemId() == R.id.menu_takephoto) {
			startActivity(new Intent(this, SendFileActivity.class));
		} else if (item.getItemId()== R.id.menu_getGPS){
			startActivity(new Intent(this, WizardActivity.class));
			startService(new Intent(DroidObserverActivity.this, GetLocationService.class));
		} else if (item.getItemId()== R.id.menu_stopGPS){
			stopService(new Intent(DroidObserverActivity.this, GetLocationService.class));
		}
		return true;
	}

	@Override
	public void onActivityResult(int reqCode, int resCode, Intent data) {
		super.onActivityResult(reqCode, resCode, data);
	}

	public void bindGPSLocation(View target) {
		startActivity(new Intent(this, WizardActivity.class));
		Log.d(tag, "bindGPS!");
		startService(new Intent(DroidObserverActivity.this,
				GetLocationService.class));
	}

	public void unbindGPSLocation(View target) {
		Log.d(tag, "unbindGPS!");
		stopService(new Intent(DroidObserverActivity.this,
				GetLocationService.class));
	}

	public void emergencyCall(View target) {
		SharedPreferences prefs = getSharedPreferences(
				"bullteam.droidobserver_preferences", 0);
		String telephoneNumber = prefs.getString(
				this.getResources().getString(R.string.telelphoneNumberOption),
				"");

		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + telephoneNumber));
		Log.d(tag, "Telefon alarmowy do: " + telephoneNumber);
		startActivity(intent);
	}

	public static boolean isconnected(String tag) {
		if (cm != null) {
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isAvailable()
					&& netInfo.isConnected()) {
				Log.d(tag,
						"Aktywne po³¹czenie internetowe poprzez: "
								+ netInfo.getTypeName());
				return true;
			} else {
				Log.d(tag, "Brak po³¹czenia z internetem");
				return false;
			}
		} else {
			Log.d(tag,
					"Nie mo¿na uzyskaæ dostêpu do serwisu po³¹czeñ internetowych");
		}
		return false;
	}
}