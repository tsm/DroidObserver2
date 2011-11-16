package bullteam.droidobserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {

	private Camera camera;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private LayoutInflater controlInflater = null;
	private static String tag = "Kamera";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.camerapreview);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		getWindow().setFormat(PixelFormat.UNKNOWN);
		surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		Log.d(getLocalClassName(), "zrobilem holdery");

		controlInflater = LayoutInflater.from(getBaseContext());
		View viewControl = controlInflater
				.inflate(R.layout.cameracontrol, null);
		LayoutParams layoutParamsControl = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.addContentView(viewControl, layoutParamsControl);
	}

	PictureCallback myPictureCallback_JPG = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			File pictureFile = SendFileActivity.getOutputMediaFile();
			try {
				if (pictureFile == null) {
					Log.d(getLocalClassName(), "Wyst¹pi³ b³¹d podczas tworzenia pliku!");
					return;
				}

				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
				setResult(RESULT_OK, getIntent());
			} catch (FileNotFoundException e) {
				Log.d(getLocalClassName(), "Nie znaleziono pliku: " + e.getMessage());
				return;
			} catch (IOException e) {
				Log.d(tag, "B³¹d IO: " + e.getMessage());
				return;
			} finally {
				surfaceDestroyed(surfaceHolder);
				finish();
			}
		}
	};

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (camera != null) {
			try {
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
				try {
					Thread.sleep(3500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				camera.takePicture(null, null, myPictureCallback_JPG);
				// previewing = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
		if (camera != null) {
			Camera.Parameters params = camera.getParameters();

			SharedPreferences prefs = getSharedPreferences(
					"bullteam.droidobserver_preferences", 0);
			String resolutionIndex = prefs.getString(this.getResources()
					.getString(R.string.resolutionOption), this.getResources()
					.getString(R.string.resolution_defaultValue));

			String[] options = this.getResources().getStringArray(
					R.array.resolution_entries);
			String resolution = options[Integer.parseInt(resolutionIndex)];

			Log.d(getLocalClassName(), "resolution=" + resolution);
			String w = resolution.substring(0, resolution.indexOf("x"));
			String h = resolution.substring(resolution.indexOf("x") + 1,
					resolution.length());
			params.setPictureSize(Integer.parseInt(w), Integer.parseInt(h));

			// List<Size> sizes = params.getSupportedPictureSizes();
			// // See which sizes the camera supports and choose one of those
			// for (Size s : sizes) {
			// Log.d(tag, s.height + "x" + s.width);
			// }
			// Size mSize = sizes.get(0);
			// params.setPictureSize(mSize.width, mSize.height);

			// Ustawienie kompresji i rozdzielczoœci aparatu
			params.setJpegQuality(70);
			camera.setParameters(params);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {

		if (camera != null) {
			camera.stopPreview();
			camera.release();
			camera = null;
			// previewing = false;
		} else {
			Log.d(getLocalClassName(), "Powierzchnia juz usuniêta!");
		}
	}
}