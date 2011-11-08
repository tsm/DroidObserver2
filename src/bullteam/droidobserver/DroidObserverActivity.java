package bullteam.droidobserver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
	HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost("http://student.agh.edu.pl/~tsm/droidobserver/sendgps.php"); //TODO: adres serwera z ustawieñ
    
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
						List<NameValuePair> pairs = new ArrayList<NameValuePair>();
						 pairs.add(new BasicNameValuePair("patient_id", "1"));
						 pairs.add(new BasicNameValuePair("latitude", Double.toString(location.getLatitude())));
						 pairs.add(new BasicNameValuePair("longitude", Double.toString(location.getLongitude())));
						 try {
							post.setEntity(new UrlEncodedFormEntity(pairs));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 try {
							HttpResponse response = client.execute(post);
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
	    
	    public void getGPSLocation(View target) {
			//Uri uri = Uri.parse("gps://location");
			//startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),REQ_CODE);
			startActivity(new Intent(this,SendFileActivity.class));
		}
	    	
}

//  
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
//			Toast.makeText(getBaseContext(), "pobrano dane", Toast.LENGTH_LONG).show();
//        }
//	}
