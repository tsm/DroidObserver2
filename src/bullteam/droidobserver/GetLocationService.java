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
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class GetLocationService extends Service {
	private NotificationManager notificationMgr;
	
	HttpClient client = new DefaultHttpClient();
    HttpPost post;//= new HttpPost("http://student.agh.edu.pl/~tsm/droidobserver/sendgps.php");;
    @Override
	
    public void onCreate() {
		super.onCreate();
		notificationMgr =(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		displayNotificationMessage("uruchamianie us³ugi wysy³aj¹cej sygna³ GPS");
		
		SharedPreferences prefs=getSharedPreferences("bullteam.droidobserver_preferences",0);
		String serverAddress = prefs.getString(this.getResources().getString(R.string.serverAddressOption), "");
		post = new HttpPost(serverAddress+"sendgps.php");
		Log.d("serveradress",serverAddress+"sendgps.php");
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
						Log.d("error","unsupprotedEncoding");
						e.printStackTrace();
					}
					try {
						HttpResponse response = client.execute(post); 
						in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						StringBuffer sb =new StringBuffer("");
						String line = "";
						String NL = System.getProperty("line.separator");
						while ((line = in.readLine())!=null){
							sb.append(line + NL);
					    }
					    in.close();
					    textResult = sb.toString(); //TODO: czy zwraca OK?
						
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Toast.makeText(getBaseContext(), "Nowa lokalizacja: szerokœæ [" + location.getLatitude()
							+"] d³ugoœæ [" +location.getLongitude()+"] "+textResult,Toast.LENGTH_SHORT).show();
					Log.d("response",textResult);
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
    public void onDestroy(){
    	displayNotificationMessage("zatrzymanie uslugi wysylanie lokalizacji GPS");
    }
    public void onStart(Intent intent, int startId){
    	super.onStart(intent,startId);
    }
    public IBinder onBind(Intent intent){
    	return null;
    }
    
    private void displayNotificationMessage(String message){
    	Notification notification = new Notification(R.drawable.ic_droidobserver,message,System.currentTimeMillis());
    	PendingIntent contentIntent = PendingIntent.getActivity(this,0,new Intent(this,DroidObserverActivity.class),0);
    	notification.setLatestEventInfo(this, "GetLocationService", message, contentIntent);
    	notificationMgr.notify(R.id.app_notification_id, notification);
    }
    
}
