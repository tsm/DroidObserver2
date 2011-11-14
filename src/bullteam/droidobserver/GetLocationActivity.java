package bullteam.droidobserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class GetLocationActivity extends Activity {
	
	HttpClient client = new DefaultHttpClient();
    HttpPost post;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SharedPreferences prefs=getSharedPreferences("bullteam.droidobserver_preferences",0);
		String serverAddress = prefs.getString(this.getResources().getString(R.string.serverAddressOption), "");
		post = new HttpPost(serverAddress+"sendgps.php");
		LocationManager locMgr= (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		LocationListener locListener = new LocationListener()
		{
			
			public void onLocationChanged(Location location)
			{
				if(location!=null)
				{					
					SharedPreferences prefs=getSharedPreferences("bullteam.droidobserver_preferences",0);
					
					BufferedReader in = null;
					
					String login = prefs.getString("login_option", ""); //TODO sprawdzic czy jest ustawione login i haslo
					String pass = prefs.getString("pass_option", "");
					String textResult = "";
					List<NameValuePair> pairs = new ArrayList<NameValuePair>();
					pairs.add(new BasicNameValuePair("login", login));
					pairs.add(new BasicNameValuePair("pass", pass));
					pairs.add(new BasicNameValuePair("latitude", Double.toString(location.getLatitude())));
					pairs.add(new BasicNameValuePair("longitude", Double.toString(location.getLongitude())));
					try {
						post.setEntity(new UrlEncodedFormEntity(pairs));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						HttpResponse response = client.execute(post); //TODO: czy zwraca OK?
						in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						StringBuffer sb =new StringBuffer("");
						String line = "";
						String NL = System.getProperty("line.separator");
						while ((line = in.readLine())!=null){
							sb.append(line + NL);
					    }
					    in.close();
					    textResult = sb.toString();
						
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Toast.makeText(getBaseContext(), "Nowa lokalizacja: szerokœæ [" + location.getLatitude()
							+"] d³ugoœæ [" +location.getLongitude()+"] "+textResult,Toast.LENGTH_SHORT).show();
				}
			}
			
			public void onProviderDisabled(String provider){
				
			}
			public void onProviderEnabled(String provider){
				
			}
			public void onStatusChanged(String provider,int status, Bundle extras){
				
			}
		};
        locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,120000,0,locListener);
		//TextView tv = new TextView(this);
		//tv.setText("dziala");
		//setContentView(tv);
        //finish();
	}
	
}
