package bullteam.droidobserver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DroidObserverActivity extends Activity {
	private TextView tv=null;
	private static final String TAG = "DroidObserverActivity";
	
    /** Called when the activity is first created. */
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);   
	        
	        setContentView(R.layout.main);  
	        startActivity(new Intent(this,WizardActivity.class));
	        
	        Log.d(TAG, "uruchamianie aplikacji");
	        startActivity(new Intent(this,WizardActivity.class));
	    }
	 
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu){
	    	MenuInflater inflater=getMenuInflater();
	    	inflater.inflate(R.menu.mainmenu, menu);
	    	return true;
	    }
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item){
	    	if(item.getItemId()==R.id.menu_prefs){
	    		Intent intent = new Intent()
	    					.setClass(this, bullteam.droidobserver.PatientPreferenceActivity.class);
	    		this.startActivityForResult(intent, 0);
	    	}
	    	else if (item.getItemId()==R.id.menu_quit){
	    		finish();
	    	}
	    	return true;
	    }
	    
	    @Override
	    public void onActivityResult(int reqCode,int resCode,Intent data){
	    	super.onActivityResult(reqCode, resCode, data);  	
	    }
	    
	    public void getPhoto(View target) {
			startActivity(new Intent(this,SendFileActivity.class));
		}
	    
	    public void bindGPSLocation(View target) {
	    	startActivity(new Intent(this,WizardActivity.class));
	    	startService(new Intent(DroidObserverActivity.this,GetLocationService.class));
		}
	    
	    public void unbindGPSLocation(View target) {
	    	stopService(new Intent(DroidObserverActivity.this,GetLocationService.class));
		}
	    	
}