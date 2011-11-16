package bullteam.droidobserver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WizardActivity extends Activity{
	    
	    private SharedPreferences prefs;
	    private String serverAddress;
	    private String login;
	    private String pass;
	    private String telephoneNumber;
	    private String update_time;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			Log.d(this.getLocalClassName(),"start");
			super.onCreate(savedInstanceState);
			prefs=getSharedPreferences("bullteam.droidobserver_preferences",0);
			serverAddress = prefs.getString(this.getResources().getString(R.string.serverAddressOption), "");
			login = prefs.getString(this.getResources().getString(R.string.loginOption), "");
			pass = prefs.getString(this.getResources().getString(R.string.passOption), "");
			telephoneNumber = prefs.getString(this.getResources().getString(R.string.telelphoneNumberOption), "");
			update_time= prefs.getString(this.getResources().getString(R.string.updateTimeOption),"");
			if(update_time.length()==0) prefs.edit().putString(this.getResources().getString(R.string.updateTimeOption), "30").commit();
			if(serverAddress.length()!=0){
				if(!serverAddress.startsWith("http://")){
					serverAddress = "http://"+serverAddress;
					prefs.edit().putString(this.getResources().getString(R.string.serverAddressOption), serverAddress).commit();
				}
				if(!serverAddress.endsWith("/")){
					serverAddress = serverAddress+"/";
					prefs.edit().putString(this.getResources().getString(R.string.serverAddressOption), serverAddress).commit();
				}
			}
			
			if((serverAddress.length()==0)||(login.length()==0)||(pass.length()==0)||(telephoneNumber.length()==0)){
				setContentView(R.layout.wizard); 
				refreshView();
			}
			else finish();
		}
		
		private void refreshView(){
			serverAddress = prefs.getString(this.getResources().getString(R.string.serverAddressOption), "");
			login = prefs.getString(this.getResources().getString(R.string.loginOption), "");
			pass = prefs.getString(this.getResources().getString(R.string.passOption), "");
			telephoneNumber = prefs.getString(this.getResources().getString(R.string.telelphoneNumberOption), "");
			if(serverAddress.equalsIgnoreCase(this.getResources().getString(R.string.default_server_address))){
		          Button but =(Button)this.findViewById(R.id.button_default);
		          but.setVisibility(View.GONE);
				}
			String wrong="";
			if(serverAddress.length()==0) wrong = wrong+this.getResources().getString(R.string.error_server_address_empty)+"\n";
			if(login.length()==0) wrong = wrong+this.getResources().getString(R.string.error_login_empty)+"\n";
			if(pass.length()==0) wrong = wrong+this.getResources().getString(R.string.error_pass_empty)+"\n";
			if(telephoneNumber.length()==0) wrong = wrong+this.getResources().getString(R.string.error_telephoneNumber_empty)+"\n";
			TextView tv= (TextView) this.findViewById(R.id.tv_wrong_preferences);
			if(wrong.equalsIgnoreCase("")) finish();
			tv.setText(wrong);			
		}
		
		public void onResume(){
			super.onResume();
			refreshView();
		}
		
		public void onPause(){
			super.onPause();
			finish();
		}
		
		public void setDefault(View target) { 
			SharedPreferences prefs=getSharedPreferences("bullteam.droidobserver_preferences",0);
			prefs.edit().putString(this.getResources().getString(R.string.serverAddressOption), this.getResources().getString(R.string.default_server_address)).commit();
			Button but =(Button)this.findViewById(R.id.button_default);
	          but.setVisibility(View.GONE);
	        refreshView();
		}
		public void openPreferences(View target) { 
			startActivity(new Intent(this,PatientPreferenceActivity.class));
			finish(); // DEL?
		}
}
