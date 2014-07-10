package com.aquamorph.frcdrive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Throttle extends View implements OnTouchListener {

	interface OnChangeListenerThrottle {
		public boolean onChange(byte axis);
	}

	private byte axis = 0;
	private double virtualAxis = 128.0;
	private float actualAxis;
	private Paint nub = new Paint();
	private Paint slide = new Paint();
	private float width;
	private float height;
	private OnChangeListenerThrottle onChange;

	@SuppressLint("ClickableViewAccessibility")
	public Throttle(Context context, AttributeSet attrs) {
		super(context, attrs);
		nub.setARGB(150, 0, 0, 0);
		slide.setARGB(100, 150, 150, 150);
		nub.setStyle(Style.FILL);
		slide.setStyle(Style.FILL);
		setOnTouchListener(this);
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawRect(width / 4, 0, width - width / 4, height, slide);
		canvas.drawRect(0, actualAxis, width, actualAxis + width / 4, nub);
	}

	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
	}

	@SuppressLint("ClickableViewAccessibility")
	public void setOnChangeListener(OnChangeListenerThrottle listener) {
		onChange = listener;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View arg0, MotionEvent event) {

		actualAxis = event.getY();
		if (actualAxis < 0)
			actualAxis = 0;
		if (actualAxis > height - (width / 4))
			actualAxis = height - (width / 4);
		invalidate();
		// Convert joystick range
		virtualAxis = (256 / (height - (width / 4)))
				* (actualAxis - ((height - (width / 4)) / 2)) - .1;
		// Convert to byte for packets
		axis = (byte) (virtualAxis);
		onChange.onChange(axis);
		return true;
	}

	public byte getaxis() {
		return axis;
	}

}
