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
	
	private static int JUMP_SPEED;
	private int speed = 0;
	private int xSpeed = 0;
	
	private static int GRID_STEP;
	
	private static int TOP_BOUND;
	private static int BOTTOM_BOUND;
	private static int LEFT_BOUND;
	private static int RIGHT_BOUND;
	private int heroX = LEFT_BOUND;
	private int heroY = BOTTOM_BOUND;
	
	private MoveEvent moveEvent;
	
	private GameManager gameLoopThread;
	
	private SurfaceHolder holder;
	
	private Bitmap background; 
	
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
		TOP_BOUND = hero.getHeight();
		BOTTOM_BOUND = 670;
		LEFT_BOUND = hero.getWidth();
		RIGHT_BOUND = 500;
		JUMP_SPEED = hero.getWidth();
		GRID_STEP = hero.getWidth();
		
		heroX = LEFT_BOUND;
		heroY = BOTTOM_BOUND;
		
		background = BitmapFactory.decodeResource(getResources(), R.drawable.level);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		xSpeed = 0;
		// fly up
		if(speed < 0) {
			if(moveEvent != null && moveEvent.type == MoveType.LEFT) {
				speed = JUMP_SPEED;
				if(heroX - JUMP_SPEED >= LEFT_BOUND) {
					xSpeed = - JUMP_SPEED;
				}
			} else if(moveEvent != null && moveEvent.type == MoveType.RIGHT) {
				speed = JUMP_SPEED;
				if(heroX + JUMP_SPEED <= RIGHT_BOUND) {
					xSpeed = JUMP_SPEED;
				}
			} else if(heroY - JUMP_SPEED < TOP_BOUND) {
				speed = JUMP_SPEED;
			}
		// fly down
		} else if(speed > 0) {
			if(heroY + JUMP_SPEED > BOTTOM_BOUND) {
				speed = 0;
			}
		}
		moveEvent = null;
		if(xSpeed == 0) {
			heroY += speed;
		}
		heroX += xSpeed;
		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(background, 0, 0, null);
		canvas.drawBitmap(hero, heroX - hero.getWidth() / 2, heroY - hero.getHeight() / 2, null);		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			MoveType moveType = getMoveType(event);
			switch(moveType) {
				case UP:
					if(speed == 0) {
						speed = -JUMP_SPEED;
					}
					break;
				case DOWN:
					if(speed < 0) {
						speed = JUMP_SPEED;
					}
					break;
				case LEFT:
					moveEvent = new MoveEvent(MoveType.LEFT, 1);
					break;
				case RIGHT:
					moveEvent = new MoveEvent(MoveType.RIGHT, 1);
					break;
			}
			return true;
    	}
		return super.onTouchEvent(event);
	}
	
	public MoveType getMoveType(MotionEvent event) {
		if((event.getX() - heroX) > (GRID_STEP / 2)) {
			return MoveType.RIGHT;
		}
		if((heroX - event.getX()) > (GRID_STEP / 2)) {
			return MoveType.LEFT;
		}
		if((event.getY() - heroY) > (GRID_STEP / 2)) {
			return MoveType.DOWN;
		}
		if((heroY - event.getY()) > (GRID_STEP / 2)) {
			return MoveType.UP;
		}
		return MoveType.IDLE;
	}

}
