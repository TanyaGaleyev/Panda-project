package org.ivan.simple;

import org.ivan.simple.hero.Sprite;
import org.ivan.simple.level.LevelView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
	
	public static int GRID_STEP;
	
	private Sprite hero;
	
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
	
	private LevelView level;
	
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
//		hero = new Sprite(ImageProvider.getBitmap(R.drawable.ic_launcher),	1, 8);
		hero = new Sprite(ImageProvider.getBitmap(R.drawable.ic_launcher3),	1, 17);
		hero.setAnimating(true);
		
		GRID_STEP = hero.getWidth() % 4 == 0 ? hero.getWidth() : (hero.getWidth() / 4  + 1) * 4;
		TOP_BOUND = GRID_STEP;
		BOTTOM_BOUND = getHeight() - hero.getHeight();
		BOTTOM_BOUND -= BOTTOM_BOUND % GRID_STEP;
		LEFT_BOUND = GRID_STEP;
		RIGHT_BOUND = getWidth() - hero.getWidth();
		RIGHT_BOUND -= RIGHT_BOUND % GRID_STEP;
		JUMP_SPEED = GRID_STEP;
		ANIMATION_JUMP_SPEED = JUMP_SPEED / 4;
		
		heroX = LEFT_BOUND;
		heroY = BOTTOM_BOUND;
		
		level = new LevelView(5, 10);
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
		canvas.drawColor(Color.WHITE);
		//canvas.drawBitmap(background, BACKGROUND_LEFT, BACKGROUND_TOP, null);
		level.onDraw(canvas);
		hero.onDraw(canvas, heroX - hero.getWidth() / 2, heroY - hero.getHeight() / 2);
		drawGrid(canvas);
	}
	
	public void drawGrid(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		for(int x = LEFT_BOUND - GRID_STEP / 2; x <= RIGHT_BOUND + GRID_STEP / 2; x += GRID_STEP) {
			canvas.drawLine(x, TOP_BOUND - GRID_STEP / 2, x, BOTTOM_BOUND + GRID_STEP / 2, paint);
		}
		for(int y = TOP_BOUND - GRID_STEP / 2; y <= BOTTOM_BOUND + GRID_STEP / 2; y += GRID_STEP) {
			canvas.drawLine(LEFT_BOUND - GRID_STEP / 2, y, RIGHT_BOUND + GRID_STEP / 2, y, paint);
		}
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
