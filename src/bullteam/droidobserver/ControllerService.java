package bullteam.droidobserver;

import org.apache.http.client.methods.HttpPost;

import android.app.NotificationManager;
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

public class ControllerService extends Service {
	private Thread thr;
	private Handler handler;	
	private long update_time = 1000 * 30 * 1;
	
	@Override
    public void onCreate() {
		super.onCreate();		
		Log.d("ControllerService","Start");
		
	}   
    
    public void onDestroy(){    	
    	super.onDestroy();
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d("ContrellerService","Sprawdzam e-maile");
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
		                e.printStackTrace();
		           } 
	
	            }

            }
        });
        thr.start();
    }
    
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
