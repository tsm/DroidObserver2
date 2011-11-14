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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SendFileActivity extends Activity {
	String result = "result:";
	File plik = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		try {
			captureImage();
			tv.setText("Trwa wysy³anie zdjêcia...");
		} catch (Exception e) {
			tv.setText("Wyst¹pi³ b³¹d" + e.getMessage());
		}
		setContentView(tv);
	}

	/**
	 * Robi zdjêcie (automatycznie lub manualnie)
	 */
	public void captureImage() {
		Log.d("CAMERA", "Rozpoczynam aktywnoœæ");
		startActivityForResult(new Intent(this, CameraActivity.class), 0);

		// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// plik = getOutputMediaFile();
		// Uri fileUri = Uri.fromFile(plik); // create a file to save the image
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image
		// file
		// startActivityForResult(intent, 0);
	}

	/**
	 * Tworzy plik (ewentualnie te¿ katalog) do którego zapisane zostanie
	 * zdjêcie
	 * 
	 * @return utworzony plik
	 */
	public static File getOutputMediaFile() {
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory() + "", "DroidObserver");
		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			Log.d("CAMERA", "Katalog nie istnieje");
			if (!mediaStorageDir.mkdirs()) {
				Log.d("CAMERA", "Nie uda³o siê stworzyæ katalogu dla zdjêæ");
				return null;
			}
			Log.d("CAMERA", "Katalog zosta³ utworzony");
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");
		Log.d("CAMERA", "Stworzono plik:" + mediaFile.getName());
		return mediaFile;
	}

	/**
	 * Funkcja wywo³ywana po wykonaniu intencji robienia zdjêcia
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
			if (plik != null) {
				Toast.makeText(this, "Image saved to:\n" + plik.getPath(),
						Toast.LENGTH_LONG).show();

				Log.d("CAMERA", "Zdjêcie zosta³o zrobione!");
				sendFile();
			} else {
				Log.d("CAMERA", "Nieznany B³¹d: plik pusty?");
			}

		} else {
			Toast.makeText(this, "Wystapil blad w aktywnosci",
					Toast.LENGTH_LONG).show();
			Log.d("CAMERA", "Wyst¹pi³ b³¹d w aktywnoœci!");
		}
	}

	/**
	 * Wysy³a zrobione zdjêcie na serwer
	 */
	@SuppressWarnings("unused")
	public void sendFile() {
		Log.d("CAMERA", "Rozpoczeto funkcjê wysy³ania pliku");
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
			Log.d("CAMERA", "Ustanowiono po³¹czenie z " + serverAddress);

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
			Log.d("CAMERA", textResult);
		} catch (Exception ex) {
			Log.d("CAMERA", "B³¹d :" + ex.getMessage());
		}
		finish();
	}
}