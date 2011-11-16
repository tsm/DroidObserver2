package bullteam.droidobserver;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class DialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Be sure to call the super class.
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_LEFT_ICON);

		setContentView(R.layout.dialog);

		// Pobieranie zewnêtrznej treœci
		String text = getIntent().getStringExtra("text");
		TextView tv = (TextView) this.findViewById(R.id.dialog);
		if (text != null) {
			tv.setText(text);
		} else {
			tv.setText("Wystapi³ b³¹d");
		}

		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				android.R.drawable.ic_dialog_alert);
		// getWindow().;
	}
}
