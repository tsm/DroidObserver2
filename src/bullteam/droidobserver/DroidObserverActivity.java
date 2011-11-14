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
	private static final String TAG = "DroidObserverActivity"; //DEL ?
	
    /** Called when the activity is first created. */
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       /* TextView tv = new TextView(this);
	        tv.setText("Lubie placki z serem");
	        setContentView(tv);*/
	        
	        setContentView(R.layout.main);  
	        
	        Log.d(TAG, "uruchamianie aplikacji"); //DEL ?
	        
	        //preferencje i menu:
	        tv=(TextView)findViewById(R.id.text1);
	        setOptionText();
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
	    	setOptionText();	    	
	    }
		
	    private void setOptionText(){
	    	SharedPreferences prefs=getSharedPreferences("bullteam.droidobserver_preferences",0);
	    	//String option = prefs.getString(
	    	//		this.getResources().getString(R.string.selected_patient_sort_option),
	    	//		this.getResources().getString(R.string.patient_sort_option_default_value));
	    	//String[] optionText=
	    	//		this.getResources().getStringArray(R.array.patient_sort_options);
	    	//tv.setText("wartosc opacji wynosi "+option+" ("+optionText[Integer.parseInt(option)]+")");
	    	
	    }
//		public void getGPSLocation(View target){
//			TextView tv = new TextView(this);
//	        tv.setText("Lubie placki z serem i miodem");
//	        setContentView(tv);
//			
//		}
	    
	    public void getPhoto(View target) {
			startActivity(new Intent(this,SendFileActivity.class));
		}
	    
	    public void bindGPSLocation(View target) {
			//Uri uri = Uri.parse("gps://location");
	    	startService(new Intent(DroidObserverActivity.this,GetLocationService.class));
		}
	    
	    public void unbindGPSLocation(View target) {
			//Uri uri = Uri.parse("gps://location");
	    	stopService(new Intent(DroidObserverActivity.this,GetLocationService.class));
		}
	    	
}

//  
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
//			Toast.makeText(getBaseContext(), "pobrano dane", Toast.LENGTH_LONG).show();
//        }
//	}
