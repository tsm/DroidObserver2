package bullteam.droidobserver;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PatientPreferenceActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.patientoptions);
	}
	
	protected void onStop(){
		super.onStop();
		startActivity(new Intent(this,WizardActivity.class));
	}
}
