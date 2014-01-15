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
	private boolean touchSenseSwitch = false;
	private ScaleGestureDetector scaleGestureDetector;//api 10, only pinch function
	private MyScaleGestureDetector myScaleGestureDetector;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView1);
        scaleGestureDetector = new ScaleGestureDetector(this, new simpleOnScaleGestureListener());
        button = (Button)findViewById(R.id.button1);
        myScaleGestureDetector = new MyScaleGestureDetector(this, new OnScaleGestureListener() {
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
        			return false;
        		}
		}, new Handler());
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(touchSenseSwitch == true){
					
		            if(myScaleGestureDetector.getDoubleTapMode() == true)
		    			myScaleGestureDetector.setDoubleTapMode(false);
		    		else 
		    			myScaleGestureDetector.setDoubleTapMode(true);
		            Log.e("mode", ""+myScaleGestureDetector.getDoubleTapMode());
		        }
			}
		});
        //Log.e("scale", "" + myScaleGestureDetector.getScaleFactor());
        Log.e("CREATE", "create");
    }
	
	 @SuppressLint("NewApi") @Override
	   public boolean onTouchEvent(MotionEvent event) {
	     // TODO Auto-generated method stub
		 /*if(doubleSwitch == true){
			 myScaleGestureDetector.onTouchEvent(event);
		 }
		 else*/ if(pinchSwitch == true){
			 scaleGestureDetector.onTouchEvent(event);
		 }
		 else if(touchSenseSwitch == true){
			
			 myScaleGestureDetector.onTouchEvent(event);
			 //myScaleGestureDetector.setQuickScaleEnabled(false);
		 }
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
	
	
    public class MyScaleGestureDetector extends ScaleGestureDetector{
    	private final Context mContext;
        private final OnScaleGestureListener mListener;

        private float mFocusX;
        private float mFocusY;

        private boolean mQuickScaleEnabled;

        private float mCurrSpan;
        private float mPrevSpan;
        private float mInitialSpan;
        private float mCurrSpanX;
        private float mCurrSpanY;
        private float mPrevSpanX;
        private float mPrevSpanY;
        private long mCurrTime;
        private long mPrevTime;
        private boolean mInProgress;
        private int mSpanSlop;
        private int mMinSpan;

        // Bounds for recently seen values
        private float mTouchUpper;
        private float mTouchLower;
        private float mTouchHistoryLastAccepted;
        private int mTouchHistoryDirection;
        private long mTouchHistoryLastAcceptedTime;
        private int mTouchMinMajor;
        private MotionEvent mDoubleTapEvent;
        private int mDoubleTapMode = DOUBLE_TAP_MODE_NONE;
        private final Handler mHandler;

        private static final long TOUCH_STABILIZE_TIME = 128; // ms
        private static final int DOUBLE_TAP_MODE_NONE = 0;
        private static final int DOUBLE_TAP_MODE_IN_PROGRESS = 1;
        private static final float SCALE_FACTOR = .5f;
        
        
        private boolean mModeSwitch = false;
        private int mFlag = 0;

        /**
         * Consistency verifier for debugging purposes.
         */
        /*private final InputEventConsistencyVerifier mInputEventConsistencyVerifier =
                InputEventConsistencyVerifier.isInstrumentationEnabled() ?
                        new InputEventConsistencyVerifier(this, 0) : null;*/
        private GestureDetector mGestureDetector;

        private boolean mEventBeforeOrAboveStartingGestureEvent;
		public MyScaleGestureDetector(Context context, OnScaleGestureListener listener, Handler handler) {
			super(context, listener);
			mContext = context;
			mListener = listener;
			mHandler = handler;
			mSpanSlop = ViewConfiguration.get(context).getScaledTouchSlop() * 2;
			setQuickScaleEnabled(true);
			//Log.e("create", "My Scale");
		}
		@Override
		public boolean onTouchEvent(MotionEvent event){
			Log.e("PROGRESS", "1"+isInProgress()); 
			final int action = event.getActionMasked();
			if(mQuickScaleEnabled){
				//Log.e("ENABLE", "quick scale enabled");
				mGestureDetector.onTouchEvent(event);
				
			}
			final boolean streamComplete = action == MotionEvent.ACTION_UP ||
	                action == MotionEvent.ACTION_CANCEL;
			if (action == MotionEvent.ACTION_DOWN || streamComplete) {
	            // Reset any scale in progress with the listener.
	            // If it's an ACTION_DOWN we're beginning a new event stream.
	            // This means the app probably didn't give us all the events. Shame on it.
				
	            if (mInProgress) {
	                mListener.onScaleEnd(this);
	                mInProgress = false;
	                mInitialSpan = 0;
	                mDoubleTapMode = DOUBLE_TAP_MODE_NONE;
	            }
	            if(mModeSwitch == true){
					mDoubleTapMode = DOUBLE_TAP_MODE_IN_PROGRESS;
					mInProgress = true;
					mFlag = 1;
				}
	            if (streamComplete) {
	                //clearTouchHistory();
	                return true;
	            }
	        }
			final boolean configChanged = action == MotionEvent.ACTION_DOWN ||
	                action == MotionEvent.ACTION_POINTER_UP ||
	                action == MotionEvent.ACTION_POINTER_DOWN;


	        final boolean pointerUp = action == MotionEvent.ACTION_POINTER_UP;
	        final int skipIndex = pointerUp ? event.getActionIndex() : -1;

	        // Determine focal point
	        float sumX = 0, sumY = 0;
	        final int count = event.getPointerCount();
	        final int div = pointerUp ? count - 1 : count;
	        final float focusX;
	        final float focusY;
	       
	        if (mDoubleTapMode == DOUBLE_TAP_MODE_IN_PROGRESS) {
	            // In double tap mode, the focal pt is always where the double tap
	            // gesture started
	        	Log.e("MODE", "Double Tap");
	        	//if(mFlag > 1){
	        		focusX = mDoubleTapEvent.getX();
	        		focusY = mDoubleTapEvent.getY();
	        		if (event.getY() < focusY) {
	        			mEventBeforeOrAboveStartingGestureEvent = true;
	        		} else {
	        			mEventBeforeOrAboveStartingGestureEvent = false;
	        		}
	        	//}else{
//	        		focusX = 0;
//		            focusY = 0;
//	        	}
	            
	        } else {
	            for (int i = 0; i < count; i++) {
	                if (skipIndex == i) continue;
	                sumX += event.getX(i);
	                sumY += event.getY(i);
	            }

	            focusX = sumX / div;
	            focusY = sumY / div;
	        }
	        
	        
	        // Determine average deviation from focal point
	        float devSumX = 0, devSumY = 0;
	        for (int i = 0; i < count; i++) {
	            if (skipIndex == i) continue;

	            // Convert the resulting diameter into a radius.
	            final float touchSize = mTouchHistoryLastAccepted / 2;
	            devSumX += Math.abs(event.getX(i) - focusX) + touchSize;
	            devSumY += Math.abs(event.getY(i) - focusY) + touchSize;
	        }
	        final float devX = devSumX / div;
	        final float devY = devSumY / div;

	        // Span is the average distance between touch points through the focal point;
	        // i.e. the diameter of the circle with a radius of the average deviation from
	        // the focal point.
	        final float spanX = devX * 2;
	        final float spanY = devY * 2;
	        final float span;
	        if (inDoubleTapMode()) {
	            span = spanY;
	        } else {
	            span = FloatMath.sqrt(spanX * spanX + spanY * spanY);
	        }

	        // Dispatch begin/end events as needed.
	        // If the configuration changes, notify the app to reset its current state by beginning
	        // a fresh scale event stream.
	        final boolean wasInProgress = mInProgress;
	        mFocusX = focusX;
	        mFocusY = focusY;
	        if (!inDoubleTapMode() && mInProgress && (span < mMinSpan || configChanged)) {
	            mListener.onScaleEnd(this);
	            mInProgress = false;
	            mInitialSpan = span;
	            mDoubleTapMode = DOUBLE_TAP_MODE_NONE;
	        }
	        if (configChanged) {
	            mPrevSpanX = mCurrSpanX = spanX;
	            mPrevSpanY = mCurrSpanY = spanY;
	            mInitialSpan = mPrevSpan = mCurrSpan = span;
	        }
	        if(mModeSwitch == true && mFlag % 2 == 1){
	        	mInProgress = false;
	        	mFlag++;
	        }
	        final int minSpan = inDoubleTapMode() ? mSpanSlop : mMinSpan;
	        if (!mInProgress && span >=  minSpan &&
	                (wasInProgress || Math.abs(span - mInitialSpan) > mSpanSlop)) {
	            mPrevSpanX = mCurrSpanX = spanX;
	            mPrevSpanY = mCurrSpanY = spanY;
	            mPrevSpan = mCurrSpan = span;
	            mPrevTime = mCurrTime;
	            mInProgress = mListener.onScaleBegin(this);
	        }
	        //if(mModeSwitch == true)mInProgress = true;
	        // Handle motion; focal point and span/scale factor are changing.
	        if (action == MotionEvent.ACTION_MOVE) {
	            mCurrSpanX = spanX;
	            mCurrSpanY = spanY;
	            mCurrSpan = span;

	            boolean updatePrev = true;

	            if (mInProgress) {
	                updatePrev = mListener.onScale(this);
	                //Log.e("UPDATE", ""+updatePrev);
	            }
	            
	            if (updatePrev) {
	            	//Log.e("UPDATE", "dddddddddd");
	                mPrevSpanX = mCurrSpanX;
	                mPrevSpanY = mCurrSpanY;
	                mPrevSpan = mCurrSpan;
	                mPrevTime = mCurrTime;
	            }
	        }
	        Log.e("BUG", ""+mInProgress+", "+mInitialSpan+", "+mDoubleTapMode+",, "+mCurrSpan+"!! "+mPrevSpan);
	        textView.setText(String.valueOf(this.getScaleFactor()));
	        button.setText(String.valueOf(myScaleGestureDetector.getFocusX())+", "+String.valueOf(myScaleGestureDetector.getFocusY()));
	        return true;
			
		}
		private boolean inDoubleTapMode() {
	        return mDoubleTapMode == DOUBLE_TAP_MODE_IN_PROGRESS;
	    }
		@SuppressLint("Override") public void setQuickScaleEnabled(boolean scales) {
		        mQuickScaleEnabled = scales;
		        if (mQuickScaleEnabled && mGestureDetector == null) {
		        		GestureDetector.SimpleOnGestureListener gestureListener =
		        				new GestureDetector.SimpleOnGestureListener() {
		                        	@Override
		                        	public boolean onDoubleTap(MotionEvent e) {
		                        		// Double tap: start watching for a swipe
		                        		mDoubleTapEvent = e;
		                        		mDoubleTapMode = DOUBLE_TAP_MODE_IN_PROGRESS;
		                        		return true;
		                        	}
		                        	/*@Override
		                        	public boolean onDown(MotionEvent e){
		                        		mDoubleTapEvent = e;
		                        		mDoubleTapMode = DOUBLE_TAP_MODE_IN_PROGRESS;
		                        		return true;
		                        	}*/
		                    	};
		                mGestureDetector = new GestureDetector(mContext, gestureListener, mHandler);
		                Log.e("Create", "gestureListener");
		        }/*else{
		        	GestureDetector.SimpleOnGestureListener gestureListener =
	        				new GestureDetector.SimpleOnGestureListener() {
	                        	@Override
	                        	public boolean onDown(MotionEvent e){
	                        		mDoubleTapEvent = e;
	                        		mDoubleTapMode = DOUBLE_TAP_MODE_IN_PROGRESS;
	                        		return true;
	                        	}
	                    	};
	                mGestureDetector = new GestureDetector(mContext, gestureListener, mHandler);
	                Log.e("Create", "gestureListener else");
		        }*/
		        
		 }
		@SuppressLint("Override") public boolean isQuickScaleEnabled() {
	        return mQuickScaleEnabled;
	    }

	    /**
	     * Returns {@code true} if a scale gesture is in progress.
	     */
	    public boolean isInProgress() {
	        return mInProgress;
	    }

	    /**
	     * Get the X coordinate of the current gesture's focal point.
	     * If a gesture is in progress, the focal point is between
	     * each of the pointers forming the gesture.
	     *
	     * If {@link #isInProgress()} would return false, the result of this
	     * function is undefined.
	     *
	     * @return X coordinate of the focal point in pixels.
	     */
	    public float getFocusX() {
	        return mFocusX;
	    }

	    /**
	     * Get the Y coordinate of the current gesture's focal point.
	     * If a gesture is in progress, the focal point is between
	     * each of the pointers forming the gesture.
	     *
	     * If {@link #isInProgress()} would return false, the result of this
	     * function is undefined.
	     *
	     * @return Y coordinate of the focal point in pixels.
	     */
	    public float getFocusY() {
	        return mFocusY;
	    }

	    /**
	     * Return the average distance between each of the pointers forming the
	     * gesture in progress through the focal point.
	     *
	     * @return Distance between pointers in pixels.
	     */
	    public float getCurrentSpan() {
	        return mCurrSpan;
	    }

	    /**
	     * Return the average X distance between each of the pointers forming the
	     * gesture in progress through the focal point.
	     *
	     * @return Distance between pointers in pixels.
	     */
	    @SuppressLint("Override") public float getCurrentSpanX() {
	        return mCurrSpanX;
	    }

	    /**
	     * Return the average Y distance between each of the pointers forming the
	     * gesture in progress through the focal point.
	     *
	     * @return Distance between pointers in pixels.
	     */
	    @SuppressLint("Override") public float getCurrentSpanY() {
	        return mCurrSpanY;
	    }

	    /**
	     * Return the previous average distance between each of the pointers forming the
	     * gesture in progress through the focal point.
	     *
	     * @return Previous distance between pointers in pixels.
	     */
	    public float getPreviousSpan() {
	        return mPrevSpan;
	    }

	    /**
	     * Return the previous average X distance between each of the pointers forming the
	     * gesture in progress through the focal point.
	     *
	     * @return Previous distance between pointers in pixels.
	     */
	    @SuppressLint("Override") public float getPreviousSpanX() {
	        return mPrevSpanX;
	    }

	    /**
	     * Return the previous average Y distance between each of the pointers forming the
	     * gesture in progress through the focal point.
	     *
	     * @return Previous distance between pointers in pixels.
	     */
	    @SuppressLint("Override") public float getPreviousSpanY() {
	        return mPrevSpanY;
	    }

	    /**
	     * Return the scaling factor from the previous scale event to the current
	     * event. This value is defined as
	     * ({@link #getCurrentSpan()} / {@link #getPreviousSpan()}).
	     *
	     * @return The current scaling factor.
	     */
	    public float getScaleFactor() {
	        if (inDoubleTapMode()) {
	            // Drag is moving up; the further away from the gesture
	            // start, the smaller the span should be, the closer,
	            // the larger the span, and therefore the larger the scale
	            final boolean scaleUp =
	                    (mEventBeforeOrAboveStartingGestureEvent && (mCurrSpan < mPrevSpan)) ||
	                    (!mEventBeforeOrAboveStartingGestureEvent && (mCurrSpan > mPrevSpan));
	            final float spanDiff = (Math.abs(1 - (mCurrSpan / mPrevSpan)) * SCALE_FACTOR);
	            return mPrevSpan <= 0 ? 1 : scaleUp ? (1 + spanDiff) : (1 - spanDiff);
	        }
	        return mPrevSpan > 0 ? mCurrSpan / mPrevSpan : 1;
	    }

	    /**
	     * Return the time difference in milliseconds between the previous
	     * accepted scaling event and the current scaling event.
	     *
	     * @return Time difference since the last scaling event in milliseconds.
	     */
	    public long getTimeDelta() {
	        return mCurrTime - mPrevTime;
	    }

	    /**
	     * Return the event time of the current event being processed.
	     *
	     * @return Current event time in milliseconds.
	     */
	    public long getEventTime() {
	        return mCurrTime;
	    }
	    public void setDoubleTapMode(boolean set){
	    	mModeSwitch = set;
	    }
	    public boolean getDoubleTapMode(){
	    	return mModeSwitch;
	    }
    	
    }
   
   
}
