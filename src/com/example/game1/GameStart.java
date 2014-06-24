//Version 0.1
package com.example.game1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Toast;

public class GameStart extends Activity implements OnTouchListener {

	DrawStuff d; //Used for drawing
	double frac;
	float xloc,yloc, rad,cwidth,cheight; //First Circle and app size
	float xloc2,yloc2,rad2; // Second Circle
	boolean blnClick = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    
	    
	    Display display = getWindowManager().getDefaultDisplay();
	    Point size = new Point();
	    display.getSize(size);
	    cwidth = size.x;
	    cheight = size.y;
		frac = 0.02; //Size of the circle ~Rough percentage of the screen filled
	    
		rad = (float) Math.sqrt(0.318*cwidth*cheight*frac); //A = pi*r^2   1/pi ~ 0.318
		rad2= rad;
		xloc =  rad;
		yloc = cheight-1.8f*rad;
		
		xloc2 = (cwidth-2*rad2)*((float) Math.random()) + rad2;
		yloc2 = (cheight-2.8f*rad2)*((float) Math.random()) + rad2;
		d = new DrawStuff(this);
		d.setOnTouchListener(this);

		setContentView(d);
		
	
	}
	@Override
	protected void onPause(){
		super.onPause();
		d.pause();
		blnClick = false;
	}
	protected void onResume(){
		super.onResume();
		d.resume();
		String text2 = "Game Paused!"; //"You click at x = " + event.getX() + " and y = " + event.getY();
        Toast.makeText(d.getContext(), text2, Toast.LENGTH_LONG).show();
	}

@Override
	public boolean onTouch(View d, MotionEvent me)
	{
		try 
		{
			Thread.sleep(50);
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		switch(me.getAction())
		{
				case MotionEvent.ACTION_DOWN:
					if (Math.pow(me.getX() - xloc,2) + Math.pow(me.getY() - yloc,2) < Math.pow(rad,2)){
						blnClick = true;
						
					}		
					else
					{
						String text1 = "Click the green circle to play!"; //"You click at x = " + event.getX() + " and y = " + event.getY();
						Toast.makeText(d.getContext(), text1, Toast.LENGTH_LONG).show();
					}
					break;
				case MotionEvent.ACTION_UP:
			        blnClick = false;
					break;
				case MotionEvent.ACTION_MOVE:
					if(blnClick){
						xloc= me.getX();
						yloc= me.getY();
						
						if (Math.pow(me.getX() - xloc2,2) + Math.pow(me.getY() - yloc2,2) < Math.pow(rad+rad2,2)){
							xloc2 = (cwidth-2*rad2)*((float) Math.random()) + rad2;
							yloc2 = (cheight-2.8f*rad2)*((float) Math.random()) + rad2;
						}
					
					}
					break;
		}
		
	return true;
	}

	public class DrawStuff extends SurfaceView implements Runnable {
	
	
	 Thread t = null;
	 SurfaceHolder holder ;
	 boolean isItOk = false;
	 
	 Paint blue = new Paint();
	 Paint red = new Paint();
	 Paint green = new Paint();
	 Paint black = new Paint();
	 Paint white = new Paint();
	 
	public DrawStuff(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		holder = getHolder();
		blue.setColor(Color.BLUE);
		red.setColor(Color.RED);
		blue.setStyle(Paint.Style.FILL);
		red.setStyle(Paint.Style.FILL);
		green.setColor(Color.GREEN);
		green.setStyle(Paint.Style.FILL);
		black.setColor(Color.BLACK);
		black.setStyle(Paint.Style.FILL);
		white.setColor(Color.WHITE);
		white.setStyle(Paint.Style.FILL);
		white.setTextSize(20);
		white.setTextAlign(Align.CENTER);
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isItOk){
			if(!holder.getSurface().isValid()){
				continue;
			}
			Canvas c = holder.lockCanvas();
			c.drawARGB(255, 60, 70, 230);
			c.drawCircle(xloc2, yloc2, rad2, red);
			if (!blnClick){
				c.drawRect(0,0,cwidth,cheight,black); //Pause Screen
				//c.drawCircle(xloc,yloc, rad,green);  //Uncomment to only see hitbox on pause
				c.drawText("Game Paused. ",cwidth/2,cheight/2,white);
			}
			c.drawCircle(xloc,yloc, rad,green);
			holder.unlockCanvasAndPost(c);
		}
	}
	
	public void resume(){
			isItOk = true;
			t = new Thread(this);
			t.start();
		
	}
	
	public void pause(){
		isItOk = false;
		
		while(true){
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		t = null;
	}
	
		
	}

}




