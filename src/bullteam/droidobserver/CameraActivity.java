package bullteam.droidobserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bullteam.droidobserver.R.id;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CameraActivity extends Activity {

	private Camera cam = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("CAMERA", "CameraActivity start");
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		setContentView(R.layout.camera_layout);

		// Sprawdza czy istnieja kamery
		boolean isCamera = checkCameraHardware(getBaseContext());
		Log.d("CAMERA", "isCamera=" + isCamera);
		if (!isCamera) {
			setResult(RESULT_CANCELED, intent);
			finish();
		}

		// Otwiera kamere
		cam = getCameraInstance();
		if (cam == null) {
			Log.d("CAMERA", "Kamera = null");
			releaseCamera(cam);
			setResult(RESULT_CANCELED, intent);
			finish();
		}
		Log.d("CAMERA", "Kamera: " + cam.toString());

		// Robi preview
		CameraPreview preview = new CameraPreview(this, cam);
		// FrameLayout fl = ((FrameLayout) findViewById(id.camera_preview));
		preview.startCapture(cam);
		// fl.addView(preview);
		Log.d("CAMERA", "Dodano Layout");

		// // Robi zdjêcie
		cam.takePicture(null, null, myPhoto);
		Log.d("CAMERA", "Zrobiono zdjêcie");

		// Jeœli wszystko wykonalo sie poprawnie zwraca true
		setResult(RESULT_OK, intent);
		Log.d("CAMERA", "ALL OK - CameraActivity stop");
		finish();
	}

	private PictureCallback myPhoto = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d("CAMERA", "onPictureTaken start");
			File pictureFile = SendFileActivity.getOutputMediaFile();
			if (pictureFile == null) {
				Log.d("CAMERA", "Wyst¹pi³ b³¹d podczas tworzenia pliku!");
				return;
			}

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
			} catch (FileNotFoundException e) {
				Log.d("CAMERA", "Nie znaleziono pliku: " + e.getMessage());
			} catch (IOException e) {
				Log.d("CAMERA", "B³¹d IO: " + e.getMessage());
			}
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("CAMERA", "onPause");
		releaseCamera(cam);
	}

	/**
	 * Funkcja sprawdza czy w urz¹dzeniu istnieje kamera
	 * 
	 * @param context
	 * @return true jeœli istnieje, false jeœli nie istnieje
	 */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// urz¹dzenie posiada kamerê
			return true;
		} else {
			// brak kamery w urz¹dzeniu
			return false;
		}
	}

	/**
	 * Zwraca instancjê kamery
	 * 
	 * @return
	 */
	public Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			Log.d("CAMERA", "Wyst¹pi³ b³¹d podczas otwarcia kamery!");
		}
		return c; // returns null if camera is unavailable
	}

	/**
	 * Zwalnia kamerê do u¿ytku innych aplikacji
	 * 
	 * @param mCamera
	 *            kamera do zwolnienia
	 */
	private void releaseCamera(Camera mCamera) {
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
			Log.d("CAMERA", "Kamera zwolniona");
		}
	}

}
