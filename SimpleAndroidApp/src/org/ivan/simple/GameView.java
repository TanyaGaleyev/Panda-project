package org.ivan.simple;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
	private Bitmap hero;
	
	private static final int JUMP_SPEED = -20;
	private int speed = 0;
	
	private static final int TOP_BOUND = 10;
	private static final int BOTTOM_BOUND = 500;
	private int heroX = 10;
	private int heroY = BOTTOM_BOUND;
	
	private GameManager gameLoopThread;
	
	private SurfaceHolder holder;
	
	public GameView(Context context) {
		super(context);
		init();
	}
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private final void init() {
		gameLoopThread = new GameManager(this);
		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {
			
			public void surfaceDestroyed(SurfaceHolder holder) {
				boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                   try {
                         gameLoopThread.join();
                         retry = false;
                   } catch (InterruptedException e) {
                   }
                }
                
			}
			
			public void surfaceCreated(SurfaceHolder holder) {
				gameLoopThread.setRunning(true);
				gameLoopThread.start();
			}
			
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO Auto-generated method stub
				
			}
		});
		hero = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// fly up
		if(speed < 0) {
			if(heroY + JUMP_SPEED < TOP_BOUND) {
				speed = -JUMP_SPEED;
			}
		// fly down
		} else if(speed > 0) {
			if(heroY + JUMP_SPEED > BOTTOM_BOUND) {
				speed = 0;
			}
		}
		heroY += speed;
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(hero, heroX, heroY, null);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			if(speed == 0) {
				speed = JUMP_SPEED;
			} else if(speed < 0 ) {
				speed = -JUMP_SPEED;
			}
			return true;
    	}
		return super.onTouchEvent(event);
	}

}
