
//ActivityB represents the Balance Ball test, which key features include the
 
//Accelerometer and keep tracking of the position of the ball with time.
//The basic premise is that you tilt the phone to keep the ball at the center
//When the ball is close to the center, you lose less points but as the ball is
//further away, more points are lost per second.

//This program was modified from a very simple program called Tiltball, which had a green dot
//that moved around. We added many new features and text.


package kungfoofighters.boozcruise;

import android.app.Activity;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.lifecycle.util.StatusTracker;
import com.example.android.lifecycle.util.Utils;

import java.util.Date;

import java.util.Timer;


import java.util.TimerTask;
import kungfoofighters.boozcruise.R;
import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.SensorEventListener;


/**
 * Example Activity to demonstrate the lifecycle callback methods.
 */

public class BalanceBall extends Activity {

	Ball_accelerometer mBallView = null;
	Handler RedrawHandler = new Handler(); //so redraw occurs in main thread
	Timer mTmr = null;
	TimerTask mTsk = null;
	int mScrWidth, mScrHeight;
    android.graphics.PointF mBallPos, mBallSpd;
	double Sumx;
	double Sumy;
	long time1 = System.currentTimeMillis();
	long time2 = System.currentTimeMillis();
	double Score, Distance;
	String instruction, endgame;
	
	 
//Creates the initial settings/screen for Balance Ball	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title bar
        getWindow().setFlags(0xFFFFFFFF,
        		LayoutParams.FLAG_FULLSCREEN|LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        //create pointer to main screen
        final FrameLayout mainView = (android.widget.FrameLayout) findViewById(R.id.main_view);

        //get screen dimensions
        Display display = getWindowManager().getDefaultDisplay();  
        mScrWidth = display.getWidth(); 
        mScrHeight = display.getHeight();
    	mBallPos = new android.graphics.PointF();
    	mBallSpd = new android.graphics.PointF();
        
        //create variables for ball position and speed
        mBallPos.x = mScrWidth/2; 
        mBallPos.y = mScrHeight/2; 
        mBallSpd.x = 0;
        mBallSpd.y = 0; 
        
       
        
        
        //create initial ball
        mBallView = new Ball_accelerometer(this,mBallPos.x,mBallPos.y,25);
                
        mainView.addView(mBallView); //add ball to main screen
        mBallView.invalidate(); //call onDraw in BallView
        		
        //listener for accelerometer, use anonymous class for simplicity
        ((SensorManager)getSystemService(Context.SENSOR_SERVICE)).registerListener(
    		new SensorEventListener() {    
    			@Override  
    			public void onSensorChanged(SensorEvent event) {  
    			    //set ball speed based on phone tilt (ignore Z axis)
    				mBallSpd.x = -event.values[0];
    				mBallSpd.y = event.values[1];
    				//timer event will redraw ball
    			}
        		@Override  
        		public void onAccuracyChanged(Sensor sensor, int accuracy) {} //ignore this event
        	},
        	((SensorManager)getSystemService(Context.SENSOR_SERVICE))
        	.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_NORMAL);
        		
        //listener for touch event 
        mainView.setOnTouchListener(new android.view.View.OnTouchListener() {
	        public boolean onTouch(android.view.View v, android.view.MotionEvent e) {
	        	
	        	//This acts as a reset; When the android is tapped after 15 seconds, the
	        	//system can be reset with intial values and resetting the time values
	        	
	        	if ((System.currentTimeMillis()-time2) > 15000)
	        	{Score = 100; Sumx =0; Sumy = 0;
	        	time2= System.currentTimeMillis();time1 = System.currentTimeMillis(); }
    			//timer event will redraw ball
	        		        	    	
	        	return true;
	        }}); 
    } 
    
    //OnCreate
    
    //listener for menu button on phone
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       //only one menu item
        menu.add("Return to Main Menu");
        return super.onCreateOptionsMenu(menu);
    }
    
    //listener for menu item clicked, and it will exit based on what is selected.
    //Only one option is displayed though...
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle item selection    
    	if (item.getTitle() == "Exit") //user clicked Exit
    		finish(); //will call onPause
    	if (item.getTitle() == "Return to Main Menu")
    		 	BalanceBall.this.finish();
   		return super.onOptionsItemSelected(item);    
    }
    
    //For state flow see http://developer.android.com/reference/android/app/Activity.html
    @Override
    public void onPause() //app moved to background, stop background threads
    {
    	mTmr.cancel(); //kill\release timer (our only background thread)
    	mTmr = null;
    	mTsk = null;
    	super.onPause();
    }
    
    @Override
    public void onResume() //app moved to foreground (also occurs at app startup)
    {
        //create timer to move ball to new position
        mTmr = new Timer(); 
        mTsk = new TimerTask() {
			public void run() {
				//if debugging with external device, 
				//  a cat log viewer will be needed on the device
				android.util.Log.d(
				    "TiltBall","Timer Hit - " + mBallPos.x + ":" + mBallPos.y);
			    //move ball based on current speed
				mBallPos.x += 3*mBallSpd.x;
				mBallPos.y += 3*mBallSpd.y;
				
				//This will display the initial instructions and give the player 5 seconds before
				//the game starts. It will also start the ball at the center of the screen
				if ((System.currentTimeMillis()-time2) < 5000)
				{instruction = "Balance the Ball at the center!";
				mBallView.instruction = instruction;
				mBallPos.x = mScrWidth/2;mBallPos.y = mScrHeight/2;
				}
				//After 5 seconds, the instructions are blank
				else {mBallView.instruction = " ";}
					
				//From near the beginning to the end, the Score is added and the Sumx and Sumy are cumulated
				//The values for the score was determined by looking at the max Sumx and Sumy and normalizing both 
				//of the dimensions
				if (100<(System.currentTimeMillis()-time2) && (System.currentTimeMillis()-time2)< 15000)
				{
				Sumx = Math.abs((mBallPos.x - mScrWidth/2)) + Sumx;
				Sumy =  Math.abs((mBallPos.y - mScrHeight/2)) + Sumy;
				Score = 100 - (Sumx/276404*50 + Sumy/170330*50);
				}
				Distance =Math.abs((mBallPos.x - mScrWidth/2)) + Math.abs((mBallPos.y - mScrHeight/2));
				
				
				
				//if ball goes off screen, keep it stuck at the border.
				if (mBallPos.x > mScrWidth) mBallPos.x=mScrWidth;
				if (mBallPos.y > mScrHeight) mBallPos.y=mScrHeight;
				if (mBallPos.x < 0) mBallPos.x=0;
				if (mBallPos.y < 0) mBallPos.y=0;
				
				//Reposition the ball at the center after 10 seconds of the game or 15 sec elapsed
			    if ( (System.currentTimeMillis()-time1) > 15000) {mBallPos.x=mScrWidth/2;}
			    if ( (System.currentTimeMillis()-time1) > 15000) mBallPos.y=mScrHeight/2;
			    //if ( (System.currentTimeMillis()-time1) > 15000) time1 = System.currentTimeMillis();
			    
			    //Print out that the game has ended
			    if ((System.currentTimeMillis()-time2) > 15000)
				{endgame = "The Balance Test has ended!";
				mBallView.endgame = endgame;
				}
				else {mBallView.endgame = " ";}
				
			    
				//update ball class instance. Update the values in Ballview object.
				mBallView.mX = mBallPos.x;
				mBallView.mY = mBallPos.y;
				mBallView.Sumx = (float) Sumx;
				mBallView.Sumy = (float) Sumy;
				mBallView.Score = (float) Score;
				mBallView.Distance = (float) Distance;
				
				//The time is converted to an integer and kept to display in Ballview
				if ( (System.currentTimeMillis()-time1) < 15000) {
				mBallView.Time = (System.currentTimeMillis()-time1)/1000 - 5;}
				else
				{mBallView.Time = 0;}
				//redraw ball. Must run in background thread to prevent thread lock.
				mBallView.ResetTime = (System.currentTimeMillis()-time2);   
				
				//The data is shared amongst all the classes with the global static int final fields
				SharedData.shared1 = (int) Score ;
				
				
				RedrawHandler.post(new Runnable() {
				    public void run() {	
					   mBallView.invalidate();
					 
				    
				    }});
			}}; // TimerTask

        mTmr.schedule(mTsk,10,10); //start timer
        super.onResume();
    } // onResume
    
   /* @Override
    public void onDestroy() //main thread stopped
    {
    	super.onDestroy();
    	System.runFinalizersOnExit(true); //wait for threads to exit before clearing app
    	android.os.Process.killProcess(android.os.Process.myPid());  //remove app from memory 
    }
    */
    //listener for config change. 
    //This is called when user tilts phone enough to trigger landscape view
    //we want our app to stay in portrait view, so bypass event 
    @Override 
    public void onConfigurationChanged(Configuration newConfig)
	{
       super.onConfigurationChanged(newConfig);
	}

}