package bullteam.droidobserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

/**
 * Serwis który dzia³a w tle: - szuka czy s¹ zdjêcia w folderze programu - jeœli
 * znajdzie takowe to wysy³a je na serwer - usuwa wys³ane zdjêcia
 * 
 * @author Krzychu
 * 
 */
public class SendDataService extends Service {
	private final static String tag = "SendDataService";
	private NotificationManager notificationMgr;
	private SendData sd = null;

	/**
	 * Metoda wywo³ywana automatycznie gdy serwis rozpoczyna dzia³anie
	 */
	@Override
	public void onCreate() {
		Log.d(tag, "start");
		super.onCreate();

		notificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		if (notificationMgr == null) {
			stopSelf();
		} else {
			sd = new SendData();
			Thread thr = new Thread(sd);
			thr.start();
		}
	}

	/**
	 * Metoda wywo³ywana automatycznie gdy serwis koñczy dzia³anie
	 */
	@Override
	public void onDestroy() {
		Log.d(tag, "stop");
		if (sd != null) {
			sd.deactivate();
		}
		super.onDestroy();
	}

	/**
	 * Funkcja do bindowania serwisu do innych aplikacji (tutaj uniemozliwia
	 * innym aplikacjom utuchomienie tego serwisu)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * Klasa odpowiedzialna za w¹tek który sprawdza i wysy³a zdjêcia
	 * 
	 * @author Krzychu
	 * 
	 */
	class SendData implements Runnable {
		boolean isActive = true;
		int delay = 60; // czestotliwosc wysy³ania zdjec w sek

		public void deactivate() {
			isActive = false;
		}

		public void run() {
			while (isActive) {
				try {
					// Jeœli istnieje po³¹czenie
					if (DroidObserverActivity.isconnected(tag)) {
						// Wysy³anie plików
						File[] filelist = getPhotos();
						if (filelist != null) {
							for (int i = filelist.length - 1; i >= 0; i--) {
								sendFile(filelist[i]);
							}
						} else {
							Log.d(tag, "Folder zdjêæ jest pusty");
						}
					}
					Thread.sleep(1000 * delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Funkcja sprawdza czy w folderze programu s¹ nowe zdjêcia
	 * 
	 * @return lista zdjêæ
	 */
	public File[] getPhotos() {
		File[] filelist = null;
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory() + "", "DroidObserver");
		if (mediaStorageDir.exists()) {
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					return (filename.endsWith(".jpg"));
				}
			};
			filelist = mediaStorageDir.listFiles(filter);
			if (filelist.length == 0) {
				filelist = null;
			}
		}
		return filelist;
	}

	/**
	 * Wysy³a zrobione zdjêcie na serwer
	 * 
	 * @param fileToSend
	 *            plik do wys³ania
	 */
	@SuppressWarnings("unused")
	public void sendFile(File fileToSend) {
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
			FileInputStream fileInputStream = new FileInputStream(fileToSend);

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
							+ fileToSend.getName() + "\"" + lineEnd);
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
					connection.getInputStream()), 8 * 1024);
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
			Log.d(tag, fileToSend.getName() + " -> status: " + textResult);
			// Usuwanie wys³anego pliku
			fileToSend.delete();
		} catch (Exception ex) {
			Log.d(tag, "B³¹d :" + ex.getMessage());
		}
	}
}
