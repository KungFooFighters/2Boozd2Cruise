//This class has the objects that are drawn on the canvas based on the scores
//It is the final display of how the player did, containing the scores and images associated
//with the scores. 

package kungfoofighters.boozcruise;

import kungfoofighters.boozcruise.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;


public class ScoreDisplayCanvas extends View {
public float Sumx, Sumy, Time,Score, Distance, ResetTime,NumScore;
public float mX;
public float mY;
public String instruction, endgame;
private Paint fpaint = new Paint();
private Paint rpaint = new Paint();
private Paint redpaint = new Paint();
private Paint yellowpaint = new Paint();
private Paint greenpaint = new Paint();
public float mR;
private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
Bitmap steercontrol, steerloss, crash, straight,braindead,brainalive;

//construct new ball object
public ScoreDisplayCanvas(Context context, float x, float y, double r) {
    super(context);
    //color hex is [transparency][red][green][blue]
    mPaint.setColor(0xFF00FF00); //not transparent. color is green
    fpaint.setColor(0xFF00FF00);
    rpaint.setColor(0xFFFF0000);
    redpaint.setColor(0xFFFF0000);
    yellowpaint.setColor(0xFFFFFF00);
    greenpaint.setColor(0xFF00FF00); 

    greenpaint.setTextSize(35);
    greenpaint.setTextAlign(Align.CENTER);//not transparent. color is green
    
    fpaint.setStyle(Paint.Style.STROKE);
    rpaint.setStyle(Paint.Style.STROKE);
    this.mX = x;
    this.mY = y;
    this.mR = (float) r; //radius
    mPaint.setTextSize(26);
   mPaint.setTextAlign(Align.CENTER);

   //Create bitmaps from the drawable folder related for each test
   steercontrol = BitmapFactory.decodeResource(getResources(), R.drawable.steercontrol);
   steerloss = BitmapFactory.decodeResource(getResources(), R.drawable.steerloss);
   crash = BitmapFactory.decodeResource(getResources(), R.drawable.crash);
   straight = BitmapFactory.decodeResource(getResources(), R.drawable.straight);
   braindead = BitmapFactory.decodeResource(getResources(), R.drawable.braindead);
   brainalive = BitmapFactory.decodeResource(getResources(), R.drawable.brainalive);
   
   
}
	
//called by invalidate()	
@Override
protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    
    //Variables for centering the images
    int steer1x = (getWidth() - steercontrol.getWidth())/2;
    int steer2x = (getWidth() - steerloss.getWidth())/2;
    int straightx = (getWidth() - straight.getWidth())/2;
    int crashx = (getWidth() - crash.getWidth())/2;
    int braindeadx = (getWidth() - braindead.getWidth())/2;
    int brainalivex = (getWidth() - brainalive.getWidth())/2;
    
    //Balance test score followed by which image and message to display based on the static variables 
    //in the Shared Data object
    canvas.drawText("Balance Test Score :" + (int) (SharedData.shared1 ), getWidth()/2, getHeight()/10, mPaint);
   
    if (SharedData.shared1 >75)
    {canvas.drawText("You can steer the wheel properly." , getWidth()/2, getHeight()/10+30, mPaint);
    canvas.drawBitmap(steercontrol, steer1x, getHeight()/10+70, null);}
    
    else
    	{canvas.drawText("Keep your hands" , getWidth()/2, getHeight()/10+30, mPaint);
    	canvas.drawText(" off the steering wheel..." , getWidth()/2,getHeight()/10+53, mPaint);
    	 canvas.drawBitmap(steerloss, steer2x, getHeight()/10+70, null);}
    
    //Reaction test score and images
    canvas.drawText("Reaction Test Score :" + (int) (SharedData.shared2 ), getWidth()/2, getHeight()/10+230, mPaint);
    if (SharedData.shared2 >60)
    { canvas.drawText("You can avoid any immediate dangers" , getWidth()/2, getHeight()/10+253, mPaint);
    canvas.drawBitmap(straight, straightx, getHeight()/10+270, null); }
        else
        	{canvas.drawText("Hope the roads are empty... Don't drive!", getWidth()/2, getHeight()/10+250, mPaint);
        	canvas.drawBitmap(crash, crashx, getHeight()/10+270, null);}
        
    //Logic test score and associated images
    canvas.drawText("Logic Test Score :" + (int) (SharedData.shared3 ), getWidth()/2, 2*getHeight()/3, mPaint);
    if (SharedData.shared3 >60)
    { canvas.drawText("Your brain is fit to drive!" , getWidth()/2, 2*getHeight()/3+30, mPaint);
    canvas.drawBitmap(brainalive, brainalivex, 2*getHeight()/3+50, null); }
        else
        	{canvas.drawText("Your brain just took ", getWidth()/2, 2*getHeight()/3+30, mPaint);
        	canvas.drawText("a beating from those drinks... ", getWidth()/2, 2*getHeight()/3+55, mPaint);
        	       	canvas.drawBitmap(braindead, braindeadx, 2*getHeight()/3+80, null);}
} 




}

