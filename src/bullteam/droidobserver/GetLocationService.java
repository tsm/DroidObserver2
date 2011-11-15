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
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class GetLocationService extends Service {
	private NotificationManager notificationMgr;
	private LocationManager locMgr;
	private LocationListener locListener;
	private Location currentBestLocation=null;
	private Thread thr;
	private Handler handler;
	
	private long update_time = 1000 * 30 * 1;
	
	HttpClient client = new DefaultHttpClient();
    HttpPost post;//= new HttpPost("http://student.agh.edu.pl/~tsm/droidobserver/sendgps.php");
    
    @Override
    public void onCreate() {
		super.onCreate();
		notificationMgr =(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		displayNotificationMessage("uruchamianie us³ugi wysy³aj¹cej sygna³ GPS");
		
		SharedPreferences prefs=getSharedPreferences("bullteam.droidobserver_preferences",0);
		String serverAddress = prefs.getString(this.getResources().getString(R.string.serverAddressOption), "");
		update_time = Long.getLong(prefs.getString(this.getResources().getString(R.string.updateTimeOption), "30"))*1000;
		post = new HttpPost(serverAddress+"sendgps.php");
		Log.d("serveradress",serverAddress+"sendgps.php");
		locMgr= (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		locListener = new LocationListener()
		{
			
			public void onLocationChanged(Location location)
			{
				if(location!=null)
				{					
					if(isBetterLocation(location, currentBestLocation))
			    	{
			    		currentBestLocation=location;
			    		Toast.makeText(getBaseContext(), "New currentBest: szerokœæ [" + location.getLatitude()+"] d³ugoœæ [" +location.getLongitude()+"]",Toast.LENGTH_SHORT).show();
			    	}
				}
			}
			
			public void onProviderDisabled(String provider){
			}
			public void onProviderEnabled(String provider){				
			}
			public void onStatusChanged(String provider,int status, Bundle extras){
				//if(status==LocationProvider.TEMPORARILY_UNAVAILABLE) sendGPS(currentBestLocation);
			}
		};
        locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,update_time,0,locListener);
        currentBestLocation = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}   
    
    public void onDestroy(){    	
    	displayNotificationMessage("zatrzymanie uslugi wysylanie lokalizacji GPS");
    	locMgr.removeUpdates(locListener);
    	if(thr!=null) thr.stop();
    	super.onDestroy();
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                if (currentBestLocation!=null) sendGPS(currentBestLocation);
            }

        };
        thr = new Thread(new Runnable(){
            public void run() {
	            while(true)
	            {
	               try {
		                Thread.sleep(update_time);
		                handler.sendEmptyMessage(0);
	
		           } catch (InterruptedException e) {
		                // TODO Auto-generated catch block
		                e.printStackTrace();
		           } 
	
	            }

            }
        });
        thr.start();
    }
    
    public IBinder onBind(Intent intent){
    	return null;
    }
    
    public void sendGPS(Location location){
    	  	SharedPreferences prefs=getSharedPreferences("bullteam.droidobserver_preferences",0);
			
			BufferedReader in = null;
			
			String login = prefs.getString("login_option", "");
			String pass = prefs.getString("pass_option", "");
			String textResult = "";
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("login", login));
			pairs.add(new BasicNameValuePair("pass", pass));
			pairs.add(new BasicNameValuePair("latitude", Double.toString(location.getLatitude())));
			pairs.add(new BasicNameValuePair("longitude", Double.toString(location.getLongitude())));
			Toast.makeText(getBaseContext(), "Nowa lokalizacja: szerokœæ [" + location.getLatitude()+"] d³ugoœæ [" +location.getLongitude()+"]",Toast.LENGTH_SHORT).show();
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
			Log.d("GetLocationService - response",textResult);
			//if (!textResult.equals("ok"))
				Toast.makeText(getBaseContext(),textResult,Toast.LENGTH_SHORT).show();
    }
    
    
    
    private void displayNotificationMessage(String message){
    	Notification notification = new Notification(R.drawable.ic_droidobserver,message,System.currentTimeMillis());
    	PendingIntent contentIntent = PendingIntent.getActivity(this,0,new Intent(this,DroidObserverActivity.class),0);
    	notification.setLatestEventInfo(this, "GetLocationService", message, contentIntent);
    	notificationMgr.notify(R.id.app_notification_id, notification);
    }
    
    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
   protected boolean isBetterLocation(Location location, Location currentBestLocation) {
       if (currentBestLocation == null) {
           // A new location is always better than no location
           return true;
       }

       // Check whether the new location fix is newer or older
       long timeDelta = location.getTime() - currentBestLocation.getTime();
       boolean isSignificantlyNewer = timeDelta > update_time/2;
       boolean isSignificantlyOlder = timeDelta < -update_time/2;
       boolean isNewer = timeDelta > 0;

       // If it's been more than two minutes since the current location, use the new location
       // because the user has likely moved
       if (isSignificantlyNewer) {
           return true;
       // If the new location is more than two minutes older, it must be worse
       } else if (isSignificantlyOlder) {
           return false;
       }

       // Check whether the new location fix is more or less accurate
       int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
       boolean isLessAccurate = accuracyDelta > 0;
       boolean isMoreAccurate = accuracyDelta < 0;
       boolean isSignificantlyLessAccurate = accuracyDelta > 200;

       // Check if the old and new location are from the same provider
       boolean isFromSameProvider = isSameProvider(location.getProvider(),
               currentBestLocation.getProvider());

       // Determine location quality using a combination of timeliness and accuracy
       if (isMoreAccurate) {
           return true;
       } else if (isNewer && !isLessAccurate) {
           return true;
       } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
           return true;
       }
       return false;
   }

   /** Checks whether two providers are the same */
   private boolean isSameProvider(String provider1, String provider2) { //TODO sprawdzanie po sieci GSM?
       if (provider1 == null) {
         return provider2 == null;
       }
       return provider1.equals(provider2);
   }
    
}
