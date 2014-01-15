package com.example.scaletest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class MyTextView extends TextView implements OnTouchListener, OnScaleGestureListener {
	
	ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector(getContext(),this);
	
	public MyTextView(Context context, AttributeSet attrs){
		super(context, attrs);
		Log.e("SET", "build text view");
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		Log.e("OnScale", "Scale~~~~~");
		return false;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		Log.e("Begin", "Scale Begin!!!!!");
		return false;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		Log.e("END", "OnScaleEnd.........");
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.e("Touch", "TOUCH");
		if(mScaleGestureDetector.onTouchEvent(event)) return true;
		return super.onTouchEvent(event);
	}
	
}
