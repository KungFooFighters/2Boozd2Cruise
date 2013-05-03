//This was modified from the Tiltview program tutorial, but heavily modified.
//Many BMPs were added, the paint objects modified, and text added
//In addition all objects were aligned based on screen dimensions rather than pixel sizes.


package kungfoofighters.boozcruise;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;


public class Circle_generation extends View {
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
Bitmap gBall, drunk1,drunk2,drunk3,drunk4;

//construct new ball object
public Circle_generation(Context context, float x, float y, double r) {
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
    mPaint.setTextSize(22);
   mPaint.setTextAlign(Align.CENTER);


}
	
//called by invalidate()	
@Override
protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    

    
    //The following changes the circle colors based on the radius size
    if (mR <= 15)
    {
    canvas.drawCircle(mX, mY, (float) mR, mPaint);}
    else if (15< mR && mR <= 30)
    {
        canvas.drawCircle(mX, mY, (float) mR, yellowpaint);}
    else
    { 
        canvas.drawCircle(mX, mY, (float) mR, redpaint);}
    
    //If time is less than 0 put instructions, and after 18 sec put reset, and always display the time.
    if (Time<0)
    {canvas.drawText("Get Ready! Starting in: " , getWidth()/2, getHeight()/2+200, greenpaint);
    canvas.drawText( (int)Math.abs(Time) + " seconds", getWidth()/2, getHeight()/2+250, greenpaint);}
    else if(ResetTime>18000) 
    {canvas.drawText("Tap the Screen anywhere to reset the game", getWidth()/2, getHeight()-10, mPaint);}
    else{
    canvas.drawText("Time:" + (int)  Time + "s", getWidth()/2, getHeight()-50, mPaint);}
    
    canvas.drawText("Score:" + (double) Math.round(Score*10000)/10000, getWidth()/2, 20, mPaint);
    
    //Draw instructions and endgame strings 
    canvas.drawText(instruction, getWidth()/2, 150, mPaint);
    canvas.drawText(endgame, getWidth()/2, 150, mPaint);
   
    //Keep the score in a global int. 
    SharedData.shared2 = (int) Score;
    //Once the reset time is larger than 15 second, display the score
    //if (ResetTime>15000)
    //canvas.drawText("Final Score:" + (int) NumScore, getWidth()/2, 50, mPaint);
    //canvas.drawText("Total Score:" + (int) (SharedData.shared1 + SharedData.shared2), getWidth()/2, 100, mPaint);
} 
}

