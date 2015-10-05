package com.aquamorph.frcdrive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnKeyListener;

public class PhysicalJoystick {

    public byte joyPhy1X = 0;
    public byte joyPhy1Y = 0;
    public byte joyPhy2X = 0;
    public byte joyPhy2Y = 0;
    public boolean[] Joy3Bttns = new boolean[12];
    private View view;

    @SuppressLint("NewApi")
    public PhysicalJoystick(Activity activity) {
        view = (View) activity.findViewById(R.id.controls);
        view.setOnGenericMotionListener(phyJoystickListener);
        view.setOnKeyListener(phyButtonListener);
    }

    // Listener for physical gamepad or controller without buttons.
    OnGenericMotionListener phyJoystickListener = new OnGenericMotionListener() {

        @SuppressLint("InlinedApi")
        @Override
        public boolean onGenericMotion(View view, MotionEvent event) {
            if (event.getSource() == InputDevice.SOURCE_JOYSTICK
                    && event.getAction() == MotionEvent.ACTION_MOVE) {

                // Joystick 1 on the controller
                joyPhy1X = (byte) (127 * event.getAxisValue(MotionEvent.AXIS_X));
                joyPhy1Y = (byte) (127 * event.getAxisValue(MotionEvent.AXIS_Y));

                // Joystick 2
                joyPhy2X = (byte) (127 * event.getAxisValue(MotionEvent.AXIS_Z));
                joyPhy2Y = (byte) (127 * event
                        .getAxisValue(MotionEvent.AXIS_RZ));
            }
            return true;
        }

    };

    // Physical button listener.
    OnKeyListener phyButtonListener = new OnKeyListener() {

        @Override
        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BUTTON_1:
                        Joy3Bttns[0] = true;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_2:
                        Joy3Bttns[1] = true;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_3:
                        Joy3Bttns[2] = true;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_4:
                        Joy3Bttns[3] = true;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_5:
                        Joy3Bttns[4] = true;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_6:
                        Joy3Bttns[5] = true;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_7:
                        Joy3Bttns[6] = true;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_8:
                        Joy3Bttns[7] = true;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_9:
                        Joy3Bttns[8] = true;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_10:
                        Joy3Bttns[9] = true;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_11:
                        Joy3Bttns[10] = true;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_12:
                        Joy3Bttns[11] = true;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BUTTON_1:
                        Joy3Bttns[0] = false;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_2:
                        Joy3Bttns[1] = false;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_3:
                        Joy3Bttns[2] = false;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_4:
                        Joy3Bttns[3] = false;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_5:
                        Joy3Bttns[4] = false;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_6:
                        Joy3Bttns[5] = false;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_7:
                        Joy3Bttns[6] = false;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_8:
                        Joy3Bttns[7] = false;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_9:
                        Joy3Bttns[8] = false;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_10:
                        Joy3Bttns[9] = false;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_11:
                        Joy3Bttns[10] = false;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_12:
                        Joy3Bttns[11] = false;
                }
            }
            return true;
        }
    };

}
