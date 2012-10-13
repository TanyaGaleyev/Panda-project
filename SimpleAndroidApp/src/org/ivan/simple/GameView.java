package org.ivan.simple;

import org.ivan.simple.hero.Hero;
import org.ivan.simple.level.LevelCell;
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
	
	private Hero hero;
	
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
	
	private GameManager gameLoopThread;
	
	private SurfaceHolder holder;
	
	public LevelView level;
	
	public UserControlType pressedControl = UserControlType.IDLE;
	private boolean pressed = false;
	private float startPressedY;
	private float endPressedY;
	
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
		
		hero = new Hero();
		
		background = BitmapFactory.decodeResource(getResources(), R.drawable.background_1);
		
		GRID_STEP = hero.getSprite().getWidth() % 4 == 0 ? hero.getSprite().getWidth() : (hero.getSprite().getWidth() / 4  + 1) * 4;
		TOP_BOUND = GRID_STEP;
		BOTTOM_BOUND = getHeight() - GRID_STEP;
		BOTTOM_BOUND -= BOTTOM_BOUND % GRID_STEP;
		LEFT_BOUND = GRID_STEP;
		RIGHT_BOUND = getWidth() - GRID_STEP;
		RIGHT_BOUND -= RIGHT_BOUND % GRID_STEP;
		JUMP_SPEED = GRID_STEP;
		ANIMATION_JUMP_SPEED = JUMP_SPEED / 8;
		
		heroX = LEFT_BOUND;
		heroY = BOTTOM_BOUND;
		
		level = new LevelView(1);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		xSpeed = level.model.xSpeed * ANIMATION_JUMP_SPEED;
		ySpeed = level.model.ySpeed * ANIMATION_JUMP_SPEED;
		
		heroX += xSpeed;
		heroY += ySpeed;
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(background, 0, 0, null);
		level.onDraw(canvas);
		hero.onDraw(canvas, heroX, heroY);
//		drawGrid(canvas);
		drawFPS(canvas);
		
	}
	
	public void drawFPS(Canvas canvas) {
		Paint paint = new Paint(); 
		paint.setColor(Color.BLACK); 
		paint.setStyle(Paint.Style.FILL); 
		paint.setTextSize(25); 
		paint.setColor(Color.BLUE);
		canvas.drawText("FPS: " + GameManager.getFPS(), 5, 25, paint);
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
//		if(event.getAction() == MotionEvent.ACTION_DOWN ||
//				event.getAction() == MotionEvent.ACTION_MOVE) {
//			pressedControl = getMoveType(event); 
//			level.model.controlType = pressedControl;
//			return true;
//    	}
//		if(event.getAction() == MotionEvent.ACTION_UP) {
//			pressedControl = UserControlType.IDLE;
//			return true;
//		}
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startPressedY = event.getY();
			if(event.getX() > getWidth() / 2) {
				pressedControl = UserControlType.RIGHT;
				level.model.controlType = pressedControl;
			} else {
				pressedControl = UserControlType.LEFT;
				level.model.controlType = pressedControl;
			}
			return true;
		case MotionEvent.ACTION_MOVE:
			if(event.getX() > getWidth() / 2) {
				pressedControl = UserControlType.RIGHT;
				level.model.controlType = pressedControl;
			} else {
				pressedControl = UserControlType.LEFT;
				level.model.controlType = pressedControl;
			}
			return true;
//		case MotionEvent.ACTION_POINTER_DOWN:
//			if(event.getX() > getWidth() / 2) {
//				pressedControl = UserControlType.RIGHT;
//				level.model.controlType = pressedControl;
//			} else {
//				pressedControl = UserControlType.LEFT;
//				level.model.controlType = pressedControl;
//			}
//			return true;
		case MotionEvent.ACTION_POINTER_UP:
			return true;
		case MotionEvent.ACTION_UP:
			if(event.getY() - startPressedY > 5) {
				level.model.controlType = UserControlType.DOWN;
			} else if(event.getY() - startPressedY < -5) {
				level.model.controlType = UserControlType.UP;
			}
			pressedControl = UserControlType.IDLE;
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	public UserControlType getMoveType(MotionEvent event) {
		float dX = heroX - event.getX(); // positive dx move left
		float dY = heroY - event.getY(); // positive dy move up
		// use for get control max by absolute value dx, dy
		// max(|dx|, |dy|)
		if(Math.abs(dY) > Math.abs(dX)) {
			// up or down
			if(dY < 0) {
				return UserControlType.DOWN;
			} else {
				return UserControlType.UP;
			}
		} else {
			// left or right
			if(dX < 0) {
				return UserControlType.RIGHT;
			} else {
				return UserControlType.LEFT;
			}
		}
	}
	
	public boolean readyForUpdate() {
		// change behavior only if hero is on grid point
		return (heroX % GRID_STEP == 0) && (heroY % GRID_STEP == 0) && hero.isInControlState();
	}
	
	public void updateGame() {
		if(level.model.controlType == UserControlType.IDLE) {
			level.model.controlType = pressedControl;
		}
		LevelCell prevCell = level.model.getHeroCell();
		level.model.updateGame();
		prevCell.updateCell(level.model.getMotionType());
		hero.changeSet(level.model.getMotionType());
	}

}
