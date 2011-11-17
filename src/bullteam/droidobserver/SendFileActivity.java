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
			// tv.setText("Trwa wysy�anie zdj�cia...");
		} catch (Exception e) {
			// tv.setText("Wyst�pi� b��d" + e.getMessage());
		}
		// setContentView(tv);
	}

	/**
	 * Robi zdj�cie (automatycznie lub manualnie)
	 */
	public void captureImage() {
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
				finish();
			} else {
				Log.d(tag, "Nieznany B��d: plik pusty?");
			}
		} else {
			startActivity(new Intent(this, DialogActivity.class).putExtra(
					"text", "Wyst�pi� b��d podczas robienia zdj�cia!"));
			Log.d(tag, "Wyst�pi� b��d w aktywno�ci!");
		}
	}

}