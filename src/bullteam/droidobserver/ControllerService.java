package bullteam.droidobserver;

import java.text.DateFormat;
import java.util.Date;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class ControllerService extends Service {
	@SuppressWarnings("unused")
	private Thread thr;
	private Handler handler;
	private long update_time = 1000 * 30 * 1;
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private BroadcastReceiver smsReceiver;
	public Location currentBestLocation;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("ControllerService", "Start");
		final IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION);

		this.smsReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent != null && intent.getAction() != null
						&& ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
					Object[] pduArray = (Object[]) intent.getExtras().get(
							"pdus");
					SmsMessage[] messages = new SmsMessage[pduArray.length];
					for (int i = 0; i < pduArray.length; i++) {
						messages[i] = SmsMessage
								.createFromPdu((byte[]) pduArray[i]);
					}
					parseSms(messages[0].getOriginatingAddress(),
							messages[0].getMessageBody());
				}

			}
		};

		this.registerReceiver(this.smsReceiver, filter);
	}

	public void onDestroy() {
		super.onDestroy();

		this.unregisterReceiver(this.smsReceiver);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Log.d("ControllerService", "Sprawdzam POST");
			}

		};
		thr = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(update_time);
						handler.sendEmptyMessage(0);

					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}
		});
		// thr.start(); na razie nie potrzebujemy cyklicznego nas�uchiwacza
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void parseSms(String sender, String msg) {
		Log.d("ControllerSevice", sender + ": " + msg);
		if (msg.equals("GetGPS")) {
			//wysylanie smsa z lokalizacja
			GetLocationService.sendSMS=true;
			if(!GetLocationService.started){
				startService(new Intent(ControllerService.this,
						GetLocationService.class));
			  }
//			SharedPreferences prefs = getSharedPreferences(
//			"bullteam.droidobserver_preferences", 0);
//			LocationManager locMgr = (LocationManager) this
//					.getSystemService(Context.LOCATION_SERVICE);
//			currentBestLocation=null;
//			LocationListener locListener = new LocationListener() {
//
//				public void onLocationChanged(Location location) {
//					if (location != null) {						
//							currentBestLocation = location;
//							Toast.makeText(
//									getBaseContext(),
//									"New currentBest: szerok�� ["
//											+ location.getLatitude()
//											+ "] d�ugo�� ["
//											+ location.getLongitude() + "]",
//									Toast.LENGTH_SHORT).show();
//						
//					}
//				}
//
//				public void onProviderDisabled(String provider) {
//				}
//
//				public void onProviderEnabled(String provider) {
//				}
//
//				public void onStatusChanged(String provider, int status, Bundle extras) {
//				}
//			};
//
//			locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//					update_time, 0, locListener);
//			String currentDateTimeString = DateFormat.getDateInstance().format(
//					new Date());
//			while (currentBestLocation == null){} // calkowicie bledne posuniecie!
//			String message=currentDateTimeString+" Obecne polozenie pacjenta: szerokosc "+currentBestLocation.getLatitude()+" dlugosc "+currentBestLocation.getLongitude();
//			Toast.makeText(getBaseContext(), message,Toast.LENGTH_SHORT).show();
//			Log.d("ControllerService", "Wysylam SMS: "+message);
//			String telephoneNumber = prefs.getString(
//			this.getResources().getString(R.string.telelphoneNumberOption),"");
//			SmsManager smsMgr = SmsManager.getDefault();
//			smsMgr.sendTextMessage(telephoneNumber,null,message,null,null);
			
		} else if (msg.equals("StartGPS")) {
		  if(!GetLocationService.started){
			startService(new Intent(ControllerService.this,
					GetLocationService.class));
			GetLocationService.started=true;
		  }
		} else if (msg.equals("StopGPS")) {
			stopService(new Intent(ControllerService.this,
					GetLocationService.class));
			GetLocationService.started=false;
		} else if (msg.equals("TakePhoto")) {
			startActivity(new Intent(ControllerService.this,
					SendFileActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}
	}
}
