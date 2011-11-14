package bullteam.droidobserver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SendFileActivity extends Activity {
	String path = Environment.getExternalStorageDirectory() + "";
	String result = "result:";
	File plik = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		try {
			boolean isDone = captureImage();
			tv.setText(result + " " + isDone);
		} catch (Exception e) {
			tv.setText("Wyst¹pi³ b³¹d" + e.getMessage());
		}
		setContentView(tv);
	}

	public boolean captureImage() {
		Toast.makeText(getBaseContext(), "Zaczynam captureImage()",
				Toast.LENGTH_SHORT).show();
		// startActivityForResult(new Intent(this, CameraActivity.class), 0);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		plik = getOutputMediaFile();
		Uri fileUri = Uri.fromFile(plik); // create a file to save the image
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
		startActivityForResult(intent, 0);

		return true;
	}

	private File getOutputMediaFile() {
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
			Toast.makeText(this, "Image saved to:\n" + data.getData(),
					Toast.LENGTH_LONG).show();
			Log.d("CAMERA", "Zdjêcie zosta³o zrobione!");
			sendFile();
		} else {
			Toast.makeText(this, "Wystapil blad w aktywnosci",
					Toast.LENGTH_LONG).show();
			Log.d("CAMERA", "Wyst¹pi³ b³¹d w aktywnoœci!");
		}
	}

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
			SharedPreferences prefs = getSharedPreferences(
					"bullteam.droidobserver_preferences", 0);
			String serverAddress = prefs.getString(this.getResources()
					.getString(R.string.serverAddressOption), "");
			serverAddress += "sendfile.php";
			Log.d("CAMERA", "Adres serwera = " + serverAddress);

			InputStream is = new FileInputStream(plik);
			Log.d("CAMERA", "is = " + is.toString());
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(serverAddress);
			postRequest.setHeader("Content-Type", "multipart/form-data");
			
			byte[] data = IOUtils.toByteArray(is);

			InputStreamBody isb = new InputStreamBody(new ByteArrayInputStream(
					data), "uploadedfile");

			MultipartEntity multipartContent = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			multipartContent.addPart("uploadedfile", isb);
			multipartContent.addPart("login", new StringBody("noob"));
			multipartContent.addPart("pass", new StringBody("qwe321"));

			postRequest.setEntity(multipartContent);
			HttpResponse res = httpClient.execute(postRequest);

			BufferedReader in = new BufferedReader(new InputStreamReader(res
					.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			String textResult = sb.toString(); // TODO: czy zwraca OK?

			// res.getEntity().getContent().close();

			Log.d("CAMERA", textResult);
		} catch (Exception e) {
			Log.d("CAMERA", "B³¹d :" + e.getMessage());
		}
		// try {
		// FileInputStream fileInputStream = new FileInputStream(plik);
		//
		// SharedPreferences prefs = getSharedPreferences(
		// "bullteam.droidobserver_preferences", 0);
		// String serverAddress = prefs.getString(this.getResources()
		// .getString(R.string.serverAddressOption), "");
		//
		// Log.d("CAMERA", "Adres serwera = " + serverAddress);
		//
		// URL url = new URL(serverAddress + "sendfile.php");
		// connection = (HttpURLConnection) url.openConnection();
		// Log.d("CAMERA", "Ustanowiono poo³¹czenie!");
		//
		// // Allow Inputs & Outputs
		// connection.setDoInput(true);
		// connection.setDoOutput(true);
		// connection.setUseCaches(false);
		//
		// // Enable POST method
		// connection.setRequestMethod("POST");
		//
		// connection.setRequestProperty("Connection", "Keep-Alive");
		// connection.setRequestProperty("Content-Type",
		// "multipart/form-data;boundary=" + boundary);
		//
		// outputStream = new DataOutputStream(connection.getOutputStream());
		// outputStream.writeBytes(twoHyphens + boundary + lineEnd);
		// outputStream
		// .writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
		// + path + "\"" + lineEnd);
		// outputStream.writeBytes(lineEnd);
		//
		// bytesAvailable = fileInputStream.available();
		// bufferSize = Math.min(bytesAvailable, maxBufferSize);
		// buffer = new byte[bufferSize];
		//
		// // Read file
		// bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		//
		// while (bytesRead > 0) {
		// outputStream.write(buffer, 0, bufferSize);
		// bytesAvailable = fileInputStream.available();
		// bufferSize = Math.min(bytesAvailable, maxBufferSize);
		// bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		// }
		//
		// outputStream.writeBytes(lineEnd);
		// outputStream.writeBytes(twoHyphens + boundary + twoHyphens
		// + lineEnd);
		//
		// // Responses from the server (code and message)
		// int serverResponseCode = connection.getResponseCode();
		// String serverResponseMessage = connection.getResponseMessage();
		//
		// fileInputStream.close();
		// outputStream.flush();
		// outputStream.close();
		// Log.d("CAMERA", "Wysy³anie pliku udane!");
		// } catch (Exception ex) {
		// Log.d("CAMERA", "B³¹d :" + ex.getMessage());
		// }

	}
}