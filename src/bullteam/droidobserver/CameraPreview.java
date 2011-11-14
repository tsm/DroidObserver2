package bullteam.droidobserver;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private Context context;

	public CameraPreview(Context context, Camera camera) {
		super(context);
		this.context = context;
		
		mCamera = camera;

		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void startCapture(Camera camera) {
		Log.d("CAMERA", "start capture!");
		try {
			camera.setPreviewDisplay(mHolder);
			camera.startPreview();
		} catch (IOException exception) {
			Log.e("TAG", "IOException caused by setPreviewDisplay()", exception);
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the
		// preview.
		Log.d("CAMERA", "Surface created!");
		try {
			if (mCamera != null) {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			}
		} catch (IOException e) {
			Log.d("TAG", "Error setting camera preview: " + e.getMessage());
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("CAMERA", "Surface destroyed!");
		if (mCamera != null) {
			try {
				Log.d("CAMERA", "Zatrzymuje podgl¹d!");
				mCamera.stopPreview();
			} catch (Exception e) {
				// ignore: tried to stop a non-existent preview
			}
			mCamera.release(); // release the camera for other applications
			mCamera = null;
			Log.d("CAMERA", "Zwolniono kamerê");
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.
		Log.d("CAMERA", "Surface changed!");
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			Log.d("CAMERA", "surfaceChanged 1");
			return;
		}

		// stop preview before making changes
		try {
			Log.d("CAMERA", "surfaceChanged 2");
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// make any resize, rotate or reformatting changes here

		// start preview with new settings
		try {
			Log.d("CAMERA", "surfaceChanged 3");
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();

		} catch (Exception e) {
			Log.d("CAMERA", "B³¹d podczas tworzenia podgl¹du kamery: " + e.getMessage());
		}
	}
}
