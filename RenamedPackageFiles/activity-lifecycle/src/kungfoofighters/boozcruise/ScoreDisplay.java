//This is just a "holder" class. It does not really do anything. Instead, it just passes
//the info along to the BallView3, which takes care of the printing
//This class was created to receive input at the end after the player opts to play all three games at once

//It heavily relies on the link to BallView3 and acts as the final display

package kungfoofighters.boozcruise;

import android.app.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.lifecycle.util.StatusTracker;
import com.example.android.lifecycle.util.Utils;


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
public class ScoreDisplay extends Activity {

	public static int counter = 0;
	ScoreDisplayCanvas mBallView3 = null;
	Handler RedrawHandler = new Handler(); //so redraw occurs in main thread
	Timer mTmr = null;
	TimerTask mTsk = null;
	int mScrWidth, mScrHeight;
    android.graphics.PointF mBallPos, mBallSpd;
	double Sumx;
	double Sumy;
	long time1 = System.currentTimeMillis();
	long time2 = System.currentTimeMillis();
	double Score = 0;
	double Distance;
	String instruction, endgame;
	double radius = 2;
	double SenseX, SenseY;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title bar
        getWindow().setFlags(0xFFFFFFFF,
        		LayoutParams.FLAG_FULLSCREEN|LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
        //create pointer to main screen
        final FrameLayout mainView = (android.widget.FrameLayout) findViewById(R.id.main_view);

        //get screen dimensions
        Display display = getWindowManager().getDefaultDisplay();  
        mScrWidth = display.getWidth(); 
        mScrHeight = display.getHeight();
    	mBallPos = new android.graphics.PointF();
    	mBallSpd = new android.graphics.PointF();
        
        //create variables for ball position and speed
        mBallPos.x = 44+(int)(Math.random()*(mScrWidth-44)); 
        mBallPos.y = 44+(int)(Math.random()*(mScrHeight-44)); 
       	
        //mBallSpd.x = 0;
       // mBallSpd.y = 0; 
        
       
        
        
        //create initial ball
        mBallView3 = new ScoreDisplayCanvas(this,mBallPos.x,mBallPos.y,radius);
                
        mainView.addView(mBallView3); //add ball to main screen
        mBallView3.invalidate(); //call onDraw in BallView
        		
        //listener for accelerometer, use anonymous class for simplicity
        /*((SensorManager)getSystemService(Context.SENSOR_SERVICE)).registerListener(
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
        	.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_NORMAL);*/
        		
        //listener for touch event 
      
    } //OnCreate
    
    //listener for menu button on phone
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         //only one menu item
        menu.add("Return to Main Menu");
        return super.onCreateOptionsMenu(menu);
    }
    
    //listener for menu item clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle item selection    
    	if (item.getTitle() == "Exit") //user clicked Exit
    		{super.onDestroy();
    		System.runFinalizersOnExit(true);
    		android.os.Process.killProcess(android.os.Process.myPid());  //remove app from memory 
    	    //will call onPause//wait for threads to exit before clearing app
    		}
    	
    	if (item.getTitle() == "Return to Main Menu")
    		 	ScoreDisplay.this.finish();
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
				/*mBallPos.x += 3*mBallSpd.x;
				mBallPos.y += 3*mBallSpd.y;*/
				
				RedrawHandler.post(new Runnable() {
				    public void run() {	
					   mBallView3.invalidate();
					 
				    
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
    }*/
    
    //listener for config change. 
    //This is called when user tilts phone enough to trigger landscape view
    //we want our app to stay in portrait view, so bypass event 
    @Override 
    public void onConfigurationChanged(Configuration newConfig)
	{
       super.onConfigurationChanged(newConfig);
	}

}