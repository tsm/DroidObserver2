package bullteam.droidobserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
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

		controlInflater = LayoutInflater.from(getBaseContext());
		View viewControl = controlInflater
				.inflate(R.layout.cameracontrol, null);
		LayoutParams layoutParamsControl = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.addContentView(viewControl, layoutParamsControl);
		Log.d(getLocalClassName(), "onCreate end");
	}

	private PictureCallback myPictureCallback_JPG = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			File pictureFile = SendFileActivity.getOutputMediaFile();
			try {
				if (pictureFile == null) {
					Log.d(getLocalClassName(),
							"Wyst�pi� b��d podczas tworzenia pliku!");
					return;
				}

				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
				setResult(RESULT_OK, getIntent());
			} catch (FileNotFoundException e) {
				Log.d(getLocalClassName(),
						"Nie znaleziono pliku: " + e.getMessage());
				return;
			} catch (IOException e) {
				Log.d(tag, "B��d IO: " + e.getMessage());
				return;
			} finally {
				surfaceDestroyed(surfaceHolder);
				finish();
			}
		}
	};

	private AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {

		public void onAutoFocus(boolean success, Camera camera) {
			try {
				camera.takePicture(null, null, myPictureCallback_JPG);
			} catch (Exception e) {
				Log.d(getLocalClassName(),
						"B��d w onAutoFocus: " + e.getMessage());
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
				camera.autoFocus(myAutoFocusCallback);
			} catch (Exception e) {
				Log.d(getLocalClassName(),
						"B��d w surfacechanged: " + e.getMessage());
				surfaceDestroyed(holder);
				finish();
			}
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera = Camera.open();
			if (camera != null) {
				Camera.Parameters params = camera.getParameters();
				params.setJpegQuality(70);
				camera.setParameters(params);
			}
		} catch (Exception e) {
			Log.d(getLocalClassName(),
					"B��d w surfaceCreated: " + e.getMessage());
			surfaceDestroyed(holder);
			finish();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (camera != null) {
			camera.stopPreview();
			camera.release();
			camera = null;
		} else {
			Log.d(getLocalClassName(), "Powierzchnia juz usuni�ta!");
		}
	}
}