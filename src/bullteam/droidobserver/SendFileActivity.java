package bullteam.droidobserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SendFileActivity extends Activity {
	private static File plik = null;
	private static String tag = "SendFileActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		try {
			captureImage();
			tv.setText("Trwa wysy�anie zdj�cia...");
		} catch (Exception e) {
			tv.setText("Wyst�pi� b��d" + e.getMessage());
		}
		setContentView(tv);
	}

	/**
	 * Robi zdj�cie (automatycznie lub manualnie)
	 */
	public void captureImage() {
		Log.d(tag, "Rozpoczynam aktywno��");
		startActivityForResult(new Intent(this, CameraActivity.class), 0);
	}

	/**
	 * Tworzy plik (ewentualnie te� katalog) do kt�rego zapisane zostanie
	 * zdj�cie
	 * 
	 * @return utworzony plik
	 */
	public static File getOutputMediaFile() {
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory() + "", "DroidObserver");
		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			Log.d(tag, "Katalog nie istnieje");
			if (!mediaStorageDir.mkdirs()) {
				Log.d(tag, "Nie uda�o si� stworzy� katalogu dla zdj��");
				return null;
			}
			Log.d(tag, "Katalog zosta� utworzony");
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");
		Log.d(tag, "Stworzono plik:" + mediaFile.getName());
		plik = mediaFile;
		return mediaFile;
	}

	/**
	 * Funkcja wywo�ywana po wykonaniu intencji robienia zdj�cia
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
			if (plik != null) {
				Toast.makeText(this, "Zdj�cie wykonano pomy�lnie!",
						Toast.LENGTH_LONG).show();

				Log.d(tag, "Zdj�cie zosta�o zrobione!");
				sendFile();
			} else {
				Log.d(tag, "Nieznany B��d: plik pusty?");
			}
		} else {
			Toast.makeText(this, "Wystapil blad w aktywnosci",
					Toast.LENGTH_LONG).show();
			Log.d(tag, "Wyst�pi� b��d w aktywno�ci!");
		}
	}

	/**
	 * Wysy�a zrobione zdj�cie na serwer
	 */
	@SuppressWarnings("unused")
	public void sendFile() {
		Log.d(tag, "Rozpoczeto funkcj� wysy�ania pliku");
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		DataInputStream inputStream = null;

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		try {
			FileInputStream fileInputStream = new FileInputStream(plik);

			SharedPreferences prefs = getSharedPreferences(
					"bullteam.droidobserver_preferences", 0);
			String serverAddress = prefs.getString(this.getResources()
					.getString(R.string.serverAddressOption), "");
			String login = prefs.getString("login_option", "");
			String pass = prefs.getString("pass_option", "");
			serverAddress += "sendfile.php";

			URL url = new URL(serverAddress + "?login=" + login + "&pass="
					+ pass);
			connection = (HttpURLConnection) url.openConnection();
			Log.d(tag, "Ustanowiono po��czenie z " + serverAddress);

			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream
					.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
							+ plik.getName() + "\"" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);

			// Responses from the server (code and message)
			int serverResponseCode = connection.getResponseCode();
			String serverResponseMessage = connection.getResponseMessage();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			String textResult = sb.toString();

			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
			Log.d(tag, textResult);
			Toast.makeText(getBaseContext(), "Obraz zosta� pomy�lnie wys�any",
					Toast.LENGTH_SHORT);
		} catch (Exception ex) {
			Log.d(tag, "B��d :" + ex.getMessage());
		}
		finish();
	}
}