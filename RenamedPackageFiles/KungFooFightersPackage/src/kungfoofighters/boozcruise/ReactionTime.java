//This is the reaction time test. The premise is that random circles are generated and with time
//they get larger and larger and change color in the process. The user is asked to tap the balls
//as fast as they can. Score is given based on how large the circles are. The point "clicked" on 
//by the user is obtained through Android Hardware and checked if this point is within the 
//radius of the circles generated.


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
public class ReactionTime extends Activity {

	public static int counter = 0;
	Circle_generation mBallView2 = null;
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
        
        //create variables for ball position and speed. The ball position is randomly generated.
    	//The x and y values ranged from 44 from the sides so the viewer can tap the circles.
        mBallPos.x = 44+(int)(Math.random()*(mScrWidth-44)); 
        mBallPos.y = 44+(int)(Math.random()*(mScrHeight-44)); 
       	
        //mBallSpd.x = 0;
       // mBallSpd.y = 0; 
        
       
        
        
        //create initial ball
        mBallView2 = new Circle_generation(this,mBallPos.x,mBallPos.y,radius);
                
        mainView.addView(mBallView2); //add ball to main screen
        mBallView2.invalidate(); //call onDraw in BallView
        		
        
        
        
        //listener for touch event 
        mainView.setOnTouchListener(new android.view.View.OnTouchListener() {
	        public boolean onTouch(android.view.View v, android.view.MotionEvent e) {
	        	//set ball position based on screen touch
	        	
	        	//During the gameplay mode, check if where the user clicked on was within the circle radius
	        	if ((System.currentTimeMillis()-time2) > 5000 && (System.currentTimeMillis()-time2) < 15000 )
	        	{//Get the sensor positions
	        	SenseX = e.getX();
	        	SenseY = e.getY();
	        	//If the location tapped was close to the thing generated:
	        	//Add Score based on the radius
	        		if (Math.abs(SenseX - mBallPos.x) < Math.abs(radius) && Math.abs(SenseY - mBallPos.y) < Math.abs(radius))
	        		{//Add a new position
	        			mBallPos.x = 44+(int)(Math.random()*(mScrWidth-44)); 
	                mBallPos.y = 44+(int)(Math.random()*(mScrHeight-44)); 
	                	if (radius<=15)
	                		Score = Score + 15;
	                	if (radius>15 && radius<=30 )
	                		Score = Score + 8;
	                	if (radius>30)
	                		Score = Score +4;
	                	//Set the radius back to a small value
	                radius = 1.1;
	        			        			
	        		}
	        		        	
	        	}
	        	
	        	//Once the value is larger than 18 seconds, the user acan click anywhere to restart the Reaction test
	        	if ((System.currentTimeMillis()-time2) > 18000)
	        	{Score = 0; radius = 1.1;
	        	time2= System.currentTimeMillis();time1 = System.currentTimeMillis(); }
    			//timer event will redraw ball
	        	
	        	
	        	
	        	return true;
	        }}); 
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
    		 	ReactionTime.this.finish();
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
			    
				//Instructions before game starts for 5 seconds, then nothing
				if ((System.currentTimeMillis()-time2) < 5000)
				{instruction = "Tap the Balls as they appear! Be quick!";
				mBallView2.instruction = instruction;
				}
				else {mBallView2.instruction = " ";}
				
				//If the radius is larger than 1; ie a dot already exists keep increasing the size.
				if (5000<(System.currentTimeMillis()-time2) && (System.currentTimeMillis()-time2)< 15000)
				{if (radius >1 )
				{radius = radius +.6;
				}}
				
				
				//This will pause the score. If the user does not click on any circle just give 2 points for effort.
				//Freeze the score keeping after this time
				if ((System.currentTimeMillis()-time2) > 15000)
				{	
				if (counter == 0)
					{Score = 2+Score;	
					}
					counter++;
				}
					
					
				//reset to center after 15 sec				
			    if ( (System.currentTimeMillis()-time1) > 15000) {mBallPos.x=mScrWidth/2;}
			    if ( (System.currentTimeMillis()-time1) > 15000) mBallPos.y=mScrHeight/2;
			    //if ( (System.currentTimeMillis()-time1) > 15000) time1 = System.currentTimeMillis();
			    
			    //end game message
			    if ((System.currentTimeMillis()-time2) > 15000)
				{endgame = "The Reaction Test has ended!";
				mBallView2.endgame = endgame;
				//PUT BUTTON HERE THAT GOES TO MAIN MENU
				}
				else {mBallView2.endgame = " ";}
				
			    
				//update ball class instance
				mBallView2.mX = mBallPos.x;
				mBallView2.mY = mBallPos.y;
				mBallView2.mR = (float) radius;
				mBallView2.Sumx = (float) Sumx;
				mBallView2.Sumy = (float) Sumy;
				mBallView2.Score = (float) Score;
				
				mBallView2.Distance = (float) Distance;
				if ( (System.currentTimeMillis()-time1) < 15000) {
				mBallView2.Time = (System.currentTimeMillis()-time1)/1000 - 5;}
				else
				{mBallView2.Time = 0;}
				//redraw ball. Must run in background thread to prevent thread lock.
				mBallView2.ResetTime = (System.currentTimeMillis()-time2);   
			
				
				RedrawHandler.post(new Runnable() {
				    public void run() {	
					   mBallView2.invalidate();
					 
				    
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