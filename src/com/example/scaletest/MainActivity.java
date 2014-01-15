package com.example.scaletest;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	//private ScaleGestureDetector scaleGestureDetector;
	private TextView textView;
	private Button button;
	private boolean doubleSwitch = false;
	private boolean pinchSwitch = false;
	private boolean touchSenseSwitch = true;
	private ScaleGestureDetector scaleGestureDetector;//api 10, only pinch function
	private MyScaleGestureDetector myScaleGestureDetector;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView1);
        scaleGestureDetector = new ScaleGestureDetector(this, new simpleOnScaleGestureListener());
        //button = (Button)findViewById(R.id.button1);
        myScaleGestureDetector = new MyScaleGestureDetector(this, new OnScaleGestureListener() {
        		@Override
        		public void onScaleEnd(ScaleGestureDetector detector) {
        		}
			
        		@Override
        		public boolean onScaleBegin(ScaleGestureDetector detector) {
        			return true;
        		}
			
        		@Override
        		public boolean onScale(ScaleGestureDetector detector) {
        			return false;
        		}
		}, new Handler());
    }
	
	 @SuppressLint("NewApi") @Override
	   public boolean onTouchEvent(MotionEvent event) {
	     // TODO Auto-generated method stub
		 if(doubleSwitch == true){
			 myScaleGestureDetector.setDoubleTapMode(false);
			 myScaleGestureDetector.onTouchEvent(event);
		 }
		 else if(pinchSwitch == true){
			 scaleGestureDetector.onTouchEvent(event);
		 }
		 else if(touchSenseSwitch == true){
			 myScaleGestureDetector.setDoubleTapMode(true);
			 myScaleGestureDetector.onTouchEvent(event);
		 }
		 textView.setText(String.valueOf(myScaleGestureDetector.getScaleFactor()));
	     return true;
	 }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public class simpleOnScaleGestureListener extends SimpleOnScaleGestureListener{
    	@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
			//Log.e("Scale", "End");
		}
	
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
			//Log.e("Scale", "Begin");
			return true;
		}
	
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
		//L	\og.e("Scale", "Scale");
			textView.setText(String.valueOf(scaleGestureDetector.getScaleFactor()));
			return false;
		}
    
    }
   
}
