package bullteam.droidobserver;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PatientPreferenceActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.patientoptions);
	}
}
