package com.aquamorph.frcdrive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.ToggleButton;

import com.aquamorph.frcdrive.ui.Joystick;
import com.aquamorph.frcdrive.ui.Joystick.OnChangeListener;
import com.aquamorph.frcdrive.ui.Throttle;
import com.aquamorph.frcdrive.ui.Throttle.OnChangeListenerThrottle;

class Controls {
	public boolean auto = false;
	boolean enabled = false;
	byte joy1X = 0;
	byte joy1Y = 0;
	byte joy2X = 0;
	byte joy2Y = 0;
	byte throttleAxis1 = 0;
	byte throttleAxis2 = 0;
	boolean[] Joy1Bttns = new boolean[12];
	boolean[] Joy2Bttns = new boolean[12];
	private String TAG = "Controls";
	private Joystick joystick1;
	private Joystick joystick2;
	private Throttle throttle1;
	private Throttle throttle2;
	private ToggleButton enableBttn;
	private RadioButton enableAuto;
	private Button[] Joy1Buttons = new Button[12];
	private Button[] Joy2Buttons = new Button[12];

	@SuppressLint("ClickableViewAccessibility")
	Controls(Activity activity) {

		// Initialize UI components.
		joystick1 = (Joystick) activity.findViewById(R.id.joystick1);
		joystick2 = (Joystick) activity.findViewById(R.id.joystick2);
		throttle1 = (Throttle) activity.findViewById(R.id.throttle1);
		throttle2 = (Throttle) activity.findViewById(R.id.throttle2);
		enableBttn = (ToggleButton) activity.findViewById(R.id.enable_button);
		enableAuto = (RadioButton) activity.findViewById(R.id.run_autonomous);

		// Set event listeners
		OnChangeListener joyListener1 = new OnChangeListener() {

			@Override
			public boolean onChange(byte xAxis, byte yAxis) {
				joy1X = xAxis;
				joy1Y = yAxis;
				return false;
			}
		};
		joystick1.setOnChangeListener(joyListener1);
		OnChangeListener joyListener2 = new OnChangeListener() {

			@Override
			public boolean onChange(byte xAxis, byte yAxis) {
				joy2X = xAxis;
				joy2Y = yAxis;
				return false;
			}
		};
		joystick2.setOnChangeListener(joyListener2);
		OnChangeListenerThrottle throttleListener1 = new OnChangeListenerThrottle() {

			@Override
			public boolean onChange(byte axis) {
				throttleAxis1 = axis;
				return false;
			}

		};
		throttle1.setOnChangeListener(throttleListener1);
		OnChangeListenerThrottle throttleListener2 = new OnChangeListenerThrottle() {

			@Override
			public boolean onChange(byte axis) {
				throttleAxis2 = axis;
				return false;
			}

		};
		throttle2.setOnChangeListener(throttleListener2);
		OnCheckedChangeListener enableListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton button, boolean value) {
				enabled = value;
			}
		};
		enableBttn.setOnCheckedChangeListener(enableListener);
		OnCheckedChangeListener autoListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton button, boolean value) {
				// Disable robot when changing modes.
				enabled = false;
				enableBttn.setChecked(false);
				auto = value;
			}

		};
		enableAuto.setOnCheckedChangeListener(autoListener);

		enableBttn.setChecked(enabled);

		// Don't need tele listener because auto listener takes off auto
		// mode and puts in tele.

		// Joystick one buttons
		Joy1Buttons[0] = (Button) activity.findViewById(R.id.leftButtons).findViewById(R.id.button1);
		Joy1Buttons[1] = (Button) activity.findViewById(R.id.leftButtons).findViewById(R.id.button2);
		Joy1Buttons[2] = (Button) activity.findViewById(R.id.leftButtons).findViewById(R.id.button3);
		Joy1Buttons[3] = (Button) activity.findViewById(R.id.leftButtons).findViewById(R.id.button4);
		Joy1Buttons[4] = (Button) activity.findViewById(R.id.leftButtons).findViewById(R.id.button5);
		Joy1Buttons[5] = (Button) activity.findViewById(R.id.leftButtons).findViewById(R.id.button6);
		Joy1Buttons[6] = (Button) activity.findViewById(R.id.leftButtons).findViewById(R.id.button7);
		Joy1Buttons[7] = (Button) activity.findViewById(R.id.leftButtons).findViewById(R.id.button8);
		Joy1Buttons[8] = (Button) activity.findViewById(R.id.leftButtons).findViewById(R.id.button9);
		Joy1Buttons[9] = (Button) activity.findViewById(R.id.leftButtons).findViewById(R.id.button10);
		Joy1Buttons[10] = (Button) activity.findViewById(R.id.leftButtons).findViewById(R.id.button11);
		Joy1Buttons[11] = (Button) activity.findViewById(R.id.leftButtons).findViewById(R.id.button12);
		for (int i = 0; i < Joy1Buttons.length; i++) {
			final int x = i;
			// Add listeners to the buttons.
			final Button bttn = Joy1Buttons[i];

			bttn.setOnTouchListener(new OnTouchListener() {

				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					Log.d(TAG, "Joystick 1 button " + (x + 1) + " hit");
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
		Joy2Buttons[0] = (Button) activity.findViewById(R.id.rightButtons).findViewById(R.id.button1);
		Joy2Buttons[1] = (Button) activity.findViewById(R.id.rightButtons).findViewById(R.id.button2);
		Joy2Buttons[2] = (Button) activity.findViewById(R.id.rightButtons).findViewById(R.id.button3);
		Joy2Buttons[3] = (Button) activity.findViewById(R.id.rightButtons).findViewById(R.id.button4);
		Joy2Buttons[4] = (Button) activity.findViewById(R.id.rightButtons).findViewById(R.id.button5);
		Joy2Buttons[5] = (Button) activity.findViewById(R.id.rightButtons).findViewById(R.id.button6);
		Joy2Buttons[6] = (Button) activity.findViewById(R.id.rightButtons).findViewById(R.id.button7);
		Joy2Buttons[7] = (Button) activity.findViewById(R.id.rightButtons).findViewById(R.id.button8);
		Joy2Buttons[8] = (Button) activity.findViewById(R.id.rightButtons).findViewById(R.id.button9);
		Joy2Buttons[9] = (Button) activity.findViewById(R.id.rightButtons).findViewById(R.id.button10);
		Joy2Buttons[10] = (Button) activity.findViewById(R.id.rightButtons).findViewById(R.id.button11);
		Joy2Buttons[11] = (Button) activity.findViewById(R.id.rightButtons).findViewById(R.id.button12);
		for (int i = 0; i < Joy2Buttons.length; i++) {
			final int x = i;
			// Add listeners to the buttons.
			final Button bttn = Joy2Buttons[i];

			bttn.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View view, MotionEvent event) {
					Log.d(TAG, "Joystick 2 button " + (x + 1) + " hit");
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

}
