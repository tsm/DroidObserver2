package bullteam.droidobserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class CameraActivity extends Activity {

	private Camera cam = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Sprawdza czy istnieja kamery
		boolean isCamera = checkCameraHardware(getBaseContext());
		if (isCamera != true)
			return;
		// Otwiera kamere
		cam = getCameraInstance();
		// Robi zdjêcie
		cam.takePicture(null, null, myPhoto);
		// Zwalnia kamerê
		releaseCamera(cam);
		// Jeœli wszystko wykonalo sie poprawnie zwraca true
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
					Environment.getExternalStorageDirectory()+ "/droidobserver/",
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
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
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
