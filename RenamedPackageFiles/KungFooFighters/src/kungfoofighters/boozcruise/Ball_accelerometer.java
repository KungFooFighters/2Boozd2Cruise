//This was modified from the Tiltview program tutorial, but heavily modified.

//Many BMPs were added, the paint objects modified, and text added
//In addition all objects were aligned based on screen dimensions rather than pixel sizes.

package kungfoofighters.boozcruise;


import kungfoofighters.boozcruise.R;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;

//Extend view class to draw a canvas on the screen
public class Ball_accelerometer extends View {
	public float Sumx, Sumy, Time,Score, Distance, ResetTime;
	public float mX;
    public float mY;
    
    public String instruction, endgame;
    private Paint fpaint = new Paint();
    private Paint rpaint = new Paint();
    private final int mR;
    private Paint greenpaint = new Paint();
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap gBall, drunk1,drunk2,drunk3,drunk4;
   
    //construct new ball object
    public Ball_accelerometer(Context context, float x, float y, int r) {
        super(context);
        //color hex is [transparency][red][green][blue]
        //Create various paint schemes with different colors
        mPaint.setColor(0xFF00FF00); //not transparent. color is green
        fpaint.setColor(0xFF00FF00);
        rpaint.setColor(0xFFFF0000);
        //We want the circle to be transparent and not filled
        fpaint.setStyle(Paint.Style.STROKE);
        rpaint.setStyle(Paint.Style.STROKE);
        this.mX = x;
        this.mY = y;
        this.mR = r; //radius
        
        //Import the bitmaps in from the drawable folder
        gBall = BitmapFactory.decodeResource(getResources(), R.drawable.greenball);
        drunk1 = BitmapFactory.decodeResource(getResources(), R.drawable.drunk1);
        drunk2 = BitmapFactory.decodeResource(getResources(), R.drawable.drunk2);
        drunk3 =BitmapFactory.decodeResource(getResources(), R.drawable.drunk3);
        drunk4 = BitmapFactory.decodeResource(getResources(), R.drawable.drunk4);
        
        //Change the text size and align the text to the center
        mPaint.setTextSize(22);
        mPaint.setTextAlign(Align.CENTER);
        greenpaint.setColor(0xFF00FF00); 
        greenpaint.setTextSize(35);
        greenpaint.setTextAlign(Align.CENTER);
    }
    	
    //called by invalidate()	
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Create variables to position the BMP at the center later on.
    int drunk1x = (getWidth() - drunk1.getWidth())/2;
    int drunk1y = (getHeight() - drunk1.getHeight())/2; 
    int drunk4x = (getWidth() - drunk4.getWidth())/2;
    int drunk4y = (getHeight() - drunk4.getHeight())/2;    
    int gballheight =  gBall.getHeight()/2;
    int gballwidth = gBall.getWidth()/2;
    
    //Create bitmap images that warn the player if he is too far from the center
        if (Distance>100 && Distance<250)
    	{canvas.drawBitmap(drunk4,drunk4x, drunk4y,null);}
    if (Distance>250)
    	{canvas.drawBitmap(drunk1, drunk1x, drunk1y, null);}
        canvas.drawCircle(getWidth()/2, getHeight()/2, (float) (gballwidth*1), fpaint);
        canvas.drawCircle(getWidth()/2, getHeight()/2, (float) (gballwidth*1.3), rpaint);


        //Draw text on the screen for the score at the top, with the ball at the center
        canvas.drawText("Score:" + (int) Score, getWidth()/2, 20, mPaint);
        canvas.drawBitmap(gBall, mX-gballwidth, mY -gballheight, null);
        
        
        //Instructions are based on the time, before the game starts display instructions with a countdown.
        //After 15 seconds, state the game has ended and that the player can reset it.
        if (Time<0)
        {canvas.drawText("Get Ready! Starting in: " , getWidth()/2, getHeight()/2+200, greenpaint);
        canvas.drawText( (int)Math.abs(Time) + " seconds", getWidth()/2, getHeight()/2+250, greenpaint);}
        else if(ResetTime>15000) 
        {canvas.drawText("Tap the Screen anywhere to reset the game", getWidth()/2, getHeight()-25, mPaint);}
        else{
        canvas.drawText("Time:" + Time + "s", getWidth()/2, getHeight(), mPaint);}
        
        
        canvas.drawText(instruction, getWidth()/2, 150, greenpaint);
        canvas.drawText(endgame, getWidth()/2, 150, mPaint);
       
    } 
}
