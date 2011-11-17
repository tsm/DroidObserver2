package bullteam.droidobserver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
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
		// TextView tv = new TextView(this);
		try {
			captureImage();
			// tv.setText("Trwa wysy³anie zdjêcia...");
		} catch (Exception e) {
			// tv.setText("Wyst¹pi³ b³¹d" + e.getMessage());
		}
		// setContentView(tv);
	}

	/**
	 * Robi zdjêcie (automatycznie lub manualnie)
	 */
	public void captureImage() {
		startActivityForResult(new Intent(this, CameraActivity.class), 0);
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
			Log.d(tag, "Katalog nie istnieje");
			if (!mediaStorageDir.mkdirs()) {
				Log.d(tag, "Nie uda³o siê stworzyæ katalogu dla zdjêæ");
				return null;
			}
			Log.d(tag, "Katalog zosta³ utworzony");
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
	 * Funkcja wywo³ywana po wykonaniu intencji robienia zdjêcia
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
			if (plik != null) {
				Toast.makeText(this, "Zdjêcie wykonano pomyœlnie!",
						Toast.LENGTH_LONG).show();

				Log.d(tag, "Zdjêcie zosta³o zrobione!");
				finish();
			} else {
				Log.d(tag, "Nieznany B³¹d: plik pusty?");
			}
		} else {
			startActivity(new Intent(this, DialogActivity.class).putExtra(
					"text", "Wyst¹pi³ b³¹d podczas robienia zdjêcia!"));
			Log.d(tag, "Wyst¹pi³ b³¹d w aktywnoœci!");
		}
	}

}