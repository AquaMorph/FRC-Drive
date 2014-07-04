package com.aquamorph.frcdrive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Switch;

public class MainActivity extends Activity {
	UIManager uiManager;
	PacketSender sender;
	Switch videoSwitch;

	// Menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.wifi:
			startActivity(new Intent(
					android.provider.Settings.ACTION_WIFI_SETTINGS));
			return true;
		case R.id.tutorial:
			openTutorial();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void openSettings() {
		Intent intent = new Intent(this, Preference.class);
		startActivity(intent);
	}

	public void openTutorial() {
		Intent intent = new Intent(this, Tutorial.class);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.controls);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiManager = new UIManager(this);
		sender = new PacketSender(this, uiManager);
		sender.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		sender.interrupt();
		sender = null;
	}
	
}

