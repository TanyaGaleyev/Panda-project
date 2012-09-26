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
	private static int GRID_STEP;
	
	private Bitmap background;
	
	private static int BACKGROUND_LEFT;
	private static int BACKGROUND_TOP;
	
	private Bitmap hero;
	
	private static int JUMP_SPEED;
	private static int ANIMATION_JUMP_SPEED;
	private int ySpeed = 0;
	private int xSpeed = 0;
	
	private static int LEFT_BOUND;
	private static int RIGHT_BOUND;
	private static int TOP_BOUND;
	private static int BOTTOM_BOUND;
	
	private int heroX;
	private int heroY;
	
	private MoveEvent moveEvent;
	
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
				initImages();
				gameLoopThread.setRunning(true);
				gameLoopThread.start();
			}
			
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO Auto-generated method stub
				
			}
		});

	}
	
	private void initImages() {
		hero = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		Bitmap backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.level);
		int bLeft = 0;
		int bWidth = (backgroundImage.getWidth() > getWidth()) ? getWidth() : backgroundImage.getWidth();
		int bTop = (backgroundImage.getHeight() > getHeight()) ? backgroundImage.getHeight() - getHeight() : 0;
		int bHeight = (backgroundImage.getHeight() > getHeight()) ? getHeight() : backgroundImage.getHeight();
		background = Bitmap.createBitmap(backgroundImage, bLeft, bTop, bWidth, bHeight);
		
		GRID_STEP = hero.getWidth();
		TOP_BOUND = hero.getHeight();
		BOTTOM_BOUND = background.getHeight() - hero.getHeight();
		BOTTOM_BOUND -= BOTTOM_BOUND % GRID_STEP;
		LEFT_BOUND = hero.getWidth();
		RIGHT_BOUND = background.getWidth() - hero.getWidth();
		RIGHT_BOUND -= RIGHT_BOUND % GRID_STEP;
		JUMP_SPEED = GRID_STEP;
		ANIMATION_JUMP_SPEED = JUMP_SPEED / 4;
		
		heroX = LEFT_BOUND;
		heroY = BOTTOM_BOUND;
		
		BACKGROUND_LEFT = 0;
		BACKGROUND_TOP = 0;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// change behavior only if hero is on grid point
		if(heroX % GRID_STEP == 0  && heroY % GRID_STEP == 0) {
			xSpeed = 0;
			// fly up
			if(ySpeed < 0) {
				if(moveEvent != null && moveEvent.type == UserControlType.LEFT) {
					ySpeed = ANIMATION_JUMP_SPEED;
					if(heroX - JUMP_SPEED >= LEFT_BOUND) {
						xSpeed = -ANIMATION_JUMP_SPEED;
					}
				} else if(moveEvent != null && moveEvent.type == UserControlType.RIGHT) {
					ySpeed = ANIMATION_JUMP_SPEED;
					if(heroX + JUMP_SPEED <= RIGHT_BOUND) {
						xSpeed = ANIMATION_JUMP_SPEED;
					}
				} else if(heroY - JUMP_SPEED < TOP_BOUND) {
					ySpeed = ANIMATION_JUMP_SPEED;
				}
			// fly down
			} else if(ySpeed > 0) {
				if(heroY + JUMP_SPEED > BOTTOM_BOUND) {
					ySpeed = 0;
				}
			}
			moveEvent = null;
		}
		if(xSpeed == 0) {
			heroY += ySpeed;
		}
		heroX += xSpeed;
		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(background, BACKGROUND_LEFT, BACKGROUND_TOP, null);
		canvas.drawBitmap(hero, heroX - hero.getWidth() / 2, heroY - hero.getHeight() / 2, null);		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			UserControlType moveType = getMoveType(event);
			switch(moveType) {
				case UP:
					if(ySpeed == 0) {
						ySpeed = -ANIMATION_JUMP_SPEED;
					}
					break;
				case DOWN:
					if(ySpeed < 0) {
						ySpeed = ANIMATION_JUMP_SPEED;
					}
					break;
				case LEFT:
					moveEvent = new MoveEvent(UserControlType.LEFT, 1);
					break;
				case RIGHT:
					moveEvent = new MoveEvent(UserControlType.RIGHT, 1);
					break;
			}
			return true;
    	}
		return super.onTouchEvent(event);
	}
	
	public UserControlType getMoveType(MotionEvent event) {
		if((event.getX() - heroX) > (GRID_STEP / 2)) {
			return UserControlType.RIGHT;
		}
		if((heroX - event.getX()) > (GRID_STEP / 2)) {
			return UserControlType.LEFT;
		}
		if((event.getY() - heroY) > (GRID_STEP / 2)) {
			return UserControlType.DOWN;
		}
		if((heroY - event.getY()) > (GRID_STEP / 2)) {
			return UserControlType.UP;
		}
		return UserControlType.IDLE;
	}

}
