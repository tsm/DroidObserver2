package bullteam.droidobserver;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class DroidObserverActivity extends Activity {

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(getLocalClassName(), "uruchamianie aplikacji");
		setContentView(R.layout.main);
		startActivity(new Intent(this, WizardActivity.class));
		startService(new Intent(DroidObserverActivity.this, ControllerService.class));
	}

	@Override
	protected void onDestroy() {
		stopService(new Intent(DroidObserverActivity.this,
				GetLocationService.class));
		stopService(new Intent(DroidObserverActivity.this,
				ControllerService.class));
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
			Log.d(getLocalClassName(), "Killuje - DroidObserver");
			onDestroy();
		} else if (item.getItemId() == R.id.menu_takephoto) {
			if (isconnected()) {
				startActivity(new Intent(this, SendFileActivity.class));
			} else {
				// TODO nie dzia³a toast w menu???
				// Toast.makeText(getBaseContext(),
				// "Brak po³¹czenia z internetem", Toast.LENGTH_LONG);
			}
		}
		return true;
	}

	@Override
	public void onActivityResult(int reqCode, int resCode, Intent data) {
		super.onActivityResult(reqCode, resCode, data);
	}

	public void bindGPSLocation(View target) {
		startActivity(new Intent(this, WizardActivity.class));
		Log.d(getLocalClassName(), "bindGPS!");
		startService(new Intent(DroidObserverActivity.this,
				GetLocationService.class));
	}

	public void unbindGPSLocation(View target) {
		Log.d(getLocalClassName(), "unbindGPS!");
		stopService(new Intent(DroidObserverActivity.this,
				GetLocationService.class));
	}

	public void emergencyCall(View target) {
		Log.d(getLocalClassName(), "Emergency Call!");
	}

	public boolean isconnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		if (cm != null) {
			Log.d(getLocalClassName(), "Sprawdzanie dostêpu do sieci...");
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isAvailable()
					&& netInfo.isConnected()) {
				Log.d(getLocalClassName(),
						"Aktywne po³¹czenie internetowe poprzez: "
								+ netInfo.getTypeName());
				return true;
			} else {
				Log.d(getLocalClassName(), "Brak po³¹czenia z internetem");
				return false;
			}
		} else {
			Log.d(getLocalClassName(),
					"Nie mo¿na uzyskaæ dostêpu do serwisu po³¹czeñ internetowych");
		}
		return false;
	}
}