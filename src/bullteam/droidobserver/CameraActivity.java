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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CameraActivity extends Activity {

	private Camera cam = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Sprawdza czy istnieja kamery
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		// setContentView(R.layout.camera_layout);
		TextView tv = new TextView(this);

		boolean isCamera = checkCameraHardware(getBaseContext());
		Toast.makeText(getBaseContext(), "isCamera=" + isCamera,
				Toast.LENGTH_SHORT).show();
		tv.setText(tv.getText() + "\n" + "isCamera=" + isCamera);
		if (isCamera != true)
			return;
		// Otwiera kamere
		String camname = "null";
		cam = getCameraInstance();
		if (cam != null)
			camname = cam.toString();
		Toast.makeText(getBaseContext(), "Otwarto kamere: " + camname,
				Toast.LENGTH_SHORT).show();

		tv.setText(tv.getText() + "\n" + "otwarto kamere");
		// CameraPreview preview = new CameraPreview(this, cam);
		// FrameLayout fl = ((FrameLayout) findViewById(id.camera_preview));
		// fl.addView(preview);
		// Toast.makeText(this, "Dodano layout", Toast.LENGTH_LONG).show();
		//
		// // Robi zdjêcie
		// cam.takePicture(null, null, myPhoto);
		// Toast.makeText(this, "zrobiono zdjecie", Toast.LENGTH_LONG).show();
		//
		// // Zwalnia kamerê
		// releaseCamera(cam);
		// Toast.makeText(this, "zwolniono kamere", Toast.LENGTH_LONG).show();
		//
		// Jeœli wszystko wykonalo sie poprawnie zwraca true
		setContentView(tv);
		setResult(RESULT_OK, intent);
		finish();
	}

	private PictureCallback myPhoto = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			File pictureFile = getOutputMediaFile();
			if (pictureFile == null) {
				Log.d("TAG",
						"Error creating media file, check storage permissions!");
				return;
			}

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
			} catch (FileNotFoundException e) {
				Log.d("TAG", "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d("TAG", "Error accessing file: " + e.getMessage());
			}

		}

		private File getOutputMediaFile() {
			File mediaStorageDir = new File(
					Environment.getExternalStorageDirectory() + "",
					"DroidObserver");

			// Create the storage directory if it does not exist
			if (!mediaStorageDir.exists()) {
				if (!mediaStorageDir.mkdirs()) {
					Log.d("DroidObserver", "failed to create directory");
					return null;
				}
			}

			// Create a media file name
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(new Date());
			File mediaFile;
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");

			return mediaFile;

		}
	};

	@Override
	protected void onPause() {
		super.onPause();
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
			// this device has a camera
			return true;
		} else {
			// no camera on this device
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
			Toast.makeText(getBaseContext(), "Kamera = null:",
					Toast.LENGTH_SHORT).show();
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
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

}
