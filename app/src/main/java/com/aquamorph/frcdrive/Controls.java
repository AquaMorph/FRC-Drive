package com.aquamorph.frcdrive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.aquamorph.frcdrive.Joystick;
import com.aquamorph.frcdrive.Joystick.OnChangeListener;
import com.aquamorph.frcdrive.Throttle;
import com.aquamorph.frcdrive.Throttle.OnChangeListenerThrottle;

public class Controls {
	public boolean enabled = false;
	public boolean auto = false;
	public byte joy1X = 0;
	public byte joy1Y = 0;
	public byte joy2X = 0;
	public byte joy2Y = 0;
	public byte throttleAxis1 = 0;
	public byte throttleAxis2 = 0;
	public boolean[] Joy1Bttns = new boolean[12];
	public boolean[] Joy2Bttns = new boolean[12];

	private Joystick joystick1;
	private Joystick joystick2;
	private Throttle throttle1;
	private Throttle throttle2;
	private ToggleButton enableBttn;
	private RadioButton enableAuto;
	private ViewGroup Joy1Buttons;
	private ViewGroup Joy2Buttons;


	@SuppressLint("ClickableViewAccessibility")
	public Controls(Activity activity) {

		// Initialize UI components.
		joystick1 = (Joystick) activity.findViewById(R.id.joystick1);
		joystick2 = (Joystick) activity.findViewById(R.id.joystick2);
		throttle1 = (Throttle) activity.findViewById(R.id.throttle1);
		throttle2 = (Throttle) activity.findViewById(R.id.throttle2);
		enableBttn = (ToggleButton) activity.findViewById(R.id.enable_button);
		enableAuto = (RadioButton) activity.findViewById(R.id.run_autonomous);

		// Set event listeners
		joystick1.setOnChangeListener(joyListener1);
		joystick2.setOnChangeListener(joyListener2);
		throttle1.setOnChangeListener(throttleListener1);
		throttle2.setOnChangeListener(throttleListener2);
		enableBttn.setOnCheckedChangeListener(enableListener);
		enableAuto.setOnCheckedChangeListener(autoListener);

		enableBttn.setChecked(enabled);

		// Don't need tele listener because auto listener takes off auto
		// mode and puts in tele.

		// Joystick one buttons
		Joy1Buttons = (ViewGroup) activity.findViewById(R.id.Joy1Buttons);
		for (int i = 0; i < Joy1Buttons.getChildCount(); i++) {
			final int x = i;
			// Add listeners to the buttons.
			final Button bttn = (Button) Joy1Buttons.getChildAt(i);

			bttn.setOnTouchListener(new OnTouchListener() {

				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					Log.d("Buttons", "Button hit: " + x);
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							Joy1Bttns[x] = true;
							break;
						case MotionEvent.ACTION_UP:
							Joy1Bttns[x] = false;
							break;
					}
					return false;
				}
			});
		}

		// Joystick two buttons
		Joy2Buttons = (ViewGroup) activity.findViewById(R.id.Joy2Buttons);
		for (int i = 0; i < Joy2Buttons.getChildCount(); i++) {
			final int x = i;
			// Add listeners to the buttons.
			final Button bttn = (Button) Joy2Buttons.getChildAt(i);

			bttn.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View view, MotionEvent event) {
					Log.d("Buttons", "Button hit: " + x);
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							Joy2Bttns[x] = true;
							break;
						case MotionEvent.ACTION_UP:
							Joy2Bttns[x] = false;
							break;
					}
					return false;
				}
			});
		}
	}

	// Joystick 1
	OnChangeListener joyListener1 = new OnChangeListener() {

		@Override
		public boolean onChange(byte xAxis, byte yAxis) {
			joy1X = xAxis;
			joy1Y = yAxis;
			return false;
		}
	};

	// Joystick 2
	OnChangeListener joyListener2 = new OnChangeListener() {

		@Override
		public boolean onChange(byte xAxis, byte yAxis) {
			joy2X = xAxis;
			joy2Y = yAxis;
			return false;
		}
	};

	//Throttle 1
	OnChangeListenerThrottle throttleListener1 = new OnChangeListenerThrottle() {

		@Override
		public boolean onChange(byte axis) {
			throttleAxis1 = axis;
			return false;
		}

	};

	//Throttle 2
	OnChangeListenerThrottle throttleListener2 = new OnChangeListenerThrottle() {

		@Override
		public boolean onChange(byte axis) {
			throttleAxis2 = axis;
			return false;
		}

	};

	// Enable button
	OnCheckedChangeListener enableListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton button, boolean value) {
			if (value) {
				enabled = true;
			} else {
				enabled = false;
			}
		}
	};

	// Autonimous radio
	OnCheckedChangeListener autoListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton button, boolean value) {
			// Disable robot when changing modes.
			enabled = false;
			enableBttn.setChecked(false);
			if (value)
				auto = true;
			else
				auto = false;
		}

	};

}
