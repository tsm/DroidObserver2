package bullteam.droidobserver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class DroidObserverActivity extends Activity {
	private static final String TAG = "DroidObserverActivity";

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "uruchamianie aplikacji");
		setContentView(R.layout.main);
		startActivity(new Intent(this, WizardActivity.class));
	}

	@Override
	protected void onDestroy() {
		stopService(new Intent(DroidObserverActivity.this,GetLocationService.class));
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
			Log.d("DroidObserver", "Killuje - DroidObserver");
			onDestroy();
		}
		return true;
	}

	@Override
	public void onActivityResult(int reqCode, int resCode, Intent data) {
		super.onActivityResult(reqCode, resCode, data);
	}

	public void getPhoto(View target) {
		startActivity(new Intent(this, SendFileActivity.class));
	}

	public void bindGPSLocation(View target) {
		startActivity(new Intent(this, WizardActivity.class));
		startService(new Intent(DroidObserverActivity.this,
				GetLocationService.class));
	}

	public void unbindGPSLocation(View target) {
		stopService(new Intent(DroidObserverActivity.this,
				GetLocationService.class));
	}
	public void bindEmail(View target) {
		startActivity(new Intent(this, WizardActivity.class));
		startService(new Intent(DroidObserverActivity.this,
				EmailService.class));
	}

	public void unbindEmail(View target) {
		stopService(new Intent(DroidObserverActivity.this,
				EmailService.class));
	}

}