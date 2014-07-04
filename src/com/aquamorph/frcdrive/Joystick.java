package com.aquamorph.frcdrive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Joystick extends View implements OnTouchListener{
	
	interface OnChangeListener{
		public boolean onChange(byte xAxis, byte yAxis);
	}
	
	private byte xAxis = 0;
	private byte yAxis = 0;
	private float virtualX = 0;
	private float virtualY = 0;
	private float actualX;
	private float actualY;
	private Paint inner = new Paint();
	private Paint outer = new Paint();
	private int outerRadius;
	private int innerRadius;

	private OnChangeListener onChange;

	@SuppressLint("ClickableViewAccessibility")
	public Joystick(Context context, AttributeSet attrs) {
		super(context,attrs);
		inner.setARGB(150, 0, 0, 0);
		outer.setARGB(100, 150, 150, 150);
		inner.setStyle(Style.FILL);
		outer.setStyle(Style.FILL);
		setOnTouchListener(this);
	}
	
	@Override
	public void onDraw(Canvas canvas){
		canvas.drawCircle(outerRadius, outerRadius, outerRadius, outer);
		canvas.drawCircle(actualX, actualY, innerRadius, inner);
	}
	
	public byte getXAxis(){
		return xAxis;
	}
	public byte getYAxis(){
		return yAxis;
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		outerRadius = w/2;
		innerRadius = w/4;
		actualX = outerRadius;
		actualY = outerRadius;
	}
	@SuppressLint("ClickableViewAccessibility")
	public void setOnChangeListener(OnChangeListener listener){
		onChange = listener;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		
		actualX = event.getX();
		actualY = event.getY();
		virtualX = actualX - outerRadius;
		virtualY = outerRadius-actualY;
		int radius = outerRadius-innerRadius;
		
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:	//Fall through ACTION_MOVE case
		case MotionEvent.ACTION_MOVE:
			if(Math.sqrt(Math.pow(virtualX, 2)+Math.pow(virtualY, 2))
					>= radius){
				double angle= Math.atan(virtualY/virtualX);
				if(virtualY > 0 && virtualX > 0){
					virtualX = (float) (Math.cos(angle)*radius);
					virtualY = (float) (Math.sin(angle)*radius);
				}else if(virtualY > 0 && virtualX < 0){
					virtualX = (float) -Math.abs(Math.cos(angle)*radius);
					virtualY = (float) Math.abs(Math.sin(angle)*radius);
				}else if(virtualY < 0 && virtualX < 0){
					virtualX = (float) -Math.abs(Math.cos(angle)*radius);
					virtualY = (float) -Math.abs(Math.sin(angle)*radius);
				}else if(virtualY < 0 && virtualX > 0){
					virtualX = (float) (Math.cos(angle)*radius);
					virtualY = (float) -Math.abs(Math.sin(angle)*radius);
				}else{
					virtualX = 0;
					virtualY = 0;
				}
				actualX = virtualX + outerRadius;
				actualY = outerRadius-virtualY ;
				Log.d("Joystick 3", "X: " + actualX);
				Log.d("Joystick 3", "Y: " + actualY);
			}
			break;
			
		case MotionEvent.ACTION_UP:
			virtualX = 0;
			virtualY = 0;
			actualX = virtualX + outerRadius;
			actualY = outerRadius-virtualY ;
			break;
		
		}
		invalidate();
		xAxis = (byte) (virtualX);
		yAxis = (byte) (virtualY);
		onChange.onChange(xAxis, yAxis);
		return true;
	}

	}
