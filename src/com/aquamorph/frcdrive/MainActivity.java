package com.aquamorph.frcdrive;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

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
		GridView gridView = (GridView) findViewById(R.id.Joy1Buttons);
		Button Joy1Buttons1 = (Button) findViewById(R.id.Joy1Button1);
		Joy1Buttons1.setVisibility(View.GONE);
		Button Joy1Buttons2 = (Button) findViewById(R.id.Joy1Button8);
		Joy1Buttons2.setVisibility(View.GONE);
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

