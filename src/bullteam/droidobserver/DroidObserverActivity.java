package bullteam.droidobserver;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
<<<<<<< HEAD
import android.content.SharedPreferences;
=======
>>>>>>> fa5b27dd1a5db41425af4040b9f94a3ad2fe625e
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DroidObserverActivity extends Activity {
	private TextView tv=null;
	
    /** Called when the activity is first created. */
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       /* TextView tv = new TextView(this);
	        tv.setText("Lubie placki z serem");
	        setContentView(tv);*/
	        LocationManager locMgr= (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
			LocationListener locListener = new LocationListener()
			{
				
				public void onLocationChanged(Location location)
				{
					if(location!=null)
					{
						Toast.makeText(getBaseContext(), "Nowa lokalizacja: szerokœæ [" + location.getLatitude()
								+"] d³ugoœæ [" +location.getLongitude()+"]",Toast.LENGTH_SHORT).show();
					}
				}
				
				public void onProviderDisabled(String provider){
					
				}
				public void onProviderEnabled(String provider){
					
				}
				public void onStatusChanged(String provider,int status, Bundle extras){
					
				}
			};
	        locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locListener);
	        setContentView(R.layout.main);  
	        
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
		
<<<<<<< HEAD
	    private void setOptionText(){
	    	SharedPreferences prefs=getSharedPreferences("bullteam.droidobserver_preferences",0);
	    	//String option = prefs.getString(
	    	//		this.getResources().getString(R.string.selected_patient_sort_option),
	    	//		this.getResources().getString(R.string.patient_sort_option_default_value));
	    	//String[] optionText=
	    	//		this.getResources().getStringArray(R.array.patient_sort_options);
	    	//tv.setText("wartosc opacji wynosi "+option+" ("+optionText[Integer.parseInt(option)]+")");
	    	
	    }
		public void getGPSLocation(View target){
			TextView tv = new TextView(this);
	        tv.setText("Lubie placki z serem i miodem");
	        setContentView(tv);
			
		}
}
=======
		public void getGPSLocation(View target) {
		Uri uri = Uri.parse("gps://location");
		//startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),REQ_CODE);
		startActivity(new Intent(this,GetLocationActivity.class));
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
//			Toast.makeText(getBaseContext(), "pobrano dane", Toast.LENGTH_LONG).show();
//        }
//	}
}
>>>>>>> fa5b27dd1a5db41425af4040b9f94a3ad2fe625e
