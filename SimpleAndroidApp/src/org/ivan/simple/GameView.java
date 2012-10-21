package org.ivan.simple;

import java.util.Timer;
import java.util.TimerTask;

import org.ivan.simple.hero.Hero;
import org.ivan.simple.level.LevelCell;
import org.ivan.simple.level.LevelView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

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
	LevelCell prevCell;
	
	private GameManager gameLoopThread;
	
	private SurfaceHolder holder;
	
	public LevelView level;
	
	public UserControlType pressedControl = UserControlType.IDLE;
	public UserControlType delayedControl = UserControlType.IDLE;
	private TimerTask useDelayedControl;
	
	private float[] startPressedY = new float[2];
	private float[] startPressedX = new float[2];
	private int slideSenderID;
	
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
		prevCell = level.model.getHeroCell();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		xSpeed = hero.getRealMotion().getXSpeed() * ANIMATION_JUMP_SPEED;
		ySpeed = hero.getRealMotion().getYSpeed() * ANIMATION_JUMP_SPEED;
		
		heroX += xSpeed;
		heroY += ySpeed;
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(background, 0, 0, null);
		level.onDraw(canvas);
		hero.onDraw(canvas, heroX, heroY);
//		drawGrid(canvas);
		drawFPS(canvas);
		if(level.isComplete()) {
			drawWin(canvas);
		}
	}
	
	public void drawFPS(Canvas canvas) {
		Paint paint = new Paint(); 
		paint.setStyle(Paint.Style.FILL); 
		paint.setTextSize(25); 
		paint.setColor(Color.BLUE);
		canvas.drawText("FPS: " + GameManager.getFPS(), 5, 25, paint);
	}
	
	public void drawWin(Canvas canvas) {
		String complete = "COMPLETE";
		Paint paint = new Paint(); 		
		paint.setTextSize(80);
		Rect textRect = new Rect();
		paint.getTextBounds(complete, 0, complete.length(), textRect);
		canvas.rotate(-35, getWidth() / 2, getHeight() / 2);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.RED);
		canvas.drawText(complete, getWidth() / 2 - textRect.exactCenterX(), getHeight() / 2 - textRect.exactCenterY(), paint);
		canvas.restore();
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
		return oneHandControl(event);
	}
	
	private boolean oneHandControl(MotionEvent event) {
		int actionMask = event.getActionMasked();
		int actionIndex = event.getActionIndex();
		int pointerId = event.getPointerId(actionIndex);
		switch(actionMask) {
		case MotionEvent.ACTION_DOWN:
			startPressedY[0] = event.getY();
			startPressedX[0] = event.getX();
			if(event.getX() > heroX) {
				delayedControl = UserControlType.RIGHT;
			} else {
				delayedControl = UserControlType.LEFT;
			}
			useDelayedControl = new TimerTask() {				
				@Override
				public void run() {
					pressedControl = delayedControl;
					level.model.controlType = delayedControl;
				}
			};
			new Timer().schedule(useDelayedControl, 100);
			return true;
		case MotionEvent.ACTION_POINTER_DOWN:
			if(event.getPointerCount() > 2) return true;
			startPressedY[pointerId] = event.getY(actionIndex);
			startPressedX[pointerId] = event.getX(actionIndex);
			return true;
		case MotionEvent.ACTION_POINTER_UP:
			if(event.getPointerCount() > 2) return true;
			return true;
		case MotionEvent.ACTION_UP:
			useDelayedControl.cancel();
			if(level.model.controlType == UserControlType.IDLE) {
				level.model.controlType = delayedControl;
			}
			pressedControl = UserControlType.IDLE;
			return true;
		case MotionEvent.ACTION_MOVE:
			if(event.getPointerCount() > 2) return true;
			for(int ai = 0; ai < event.getPointerCount(); ai++) {
				pointerId = event.getPointerId(ai); 
				float x = event.getX(ai);
				float y = event.getY(ai);
				if(event.getX(ai) - startPressedX[pointerId] > 20) {
					receiveSlideControl(UserControlType.RIGHT, pointerId, x, y);
					break;
				} else if(event.getX(ai) - startPressedX[pointerId] < -20) {
					receiveSlideControl(UserControlType.LEFT, pointerId, x, y);
					break;
				} else if(event.getY(ai) - startPressedY[pointerId] > 20) {
					receiveSlideControl(UserControlType.DOWN, pointerId, x, y);
					break;
				} else if(event.getY(ai) - startPressedY[pointerId] < -20) {
					receiveSlideControl(UserControlType.UP, pointerId, x, y);
					break;
				}
			}
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	private void receiveSlideControl(UserControlType control, int pointerId, float x, float y) {
		useDelayedControl.cancel();
		pressedControl = control;
		level.model.controlType = control;
		startPressedY[pointerId] = y;
		startPressedX[pointerId] = x;
		slideSenderID = pointerId;
	}
	
	private boolean simpleControl(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN ||
			event.getAction() == MotionEvent.ACTION_MOVE) {
			pressedControl = getMoveType(event); 
			level.model.controlType = pressedControl;
			return true;
		}
		if(event.getAction() == MotionEvent.ACTION_UP) {
			pressedControl = UserControlType.IDLE;
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	private boolean twoHandedControl(MotionEvent event) {
		int actionMask = event.getActionMasked();
		int actionIndex = event.getActionIndex();
		int pointerId = event.getPointerId(actionIndex);
		switch(actionMask) {
		case MotionEvent.ACTION_DOWN:
			startPressedY[0] = event.getY();
			if(event.getX() > getWidth() / 2) {
				pressedControl = UserControlType.RIGHT;
				level.model.controlType = pressedControl;
			} else {
				pressedControl = UserControlType.LEFT;
				level.model.controlType = pressedControl;
			}
			return true;
		case MotionEvent.ACTION_POINTER_DOWN:
			if(event.getPointerCount() > 2) return true;
			startPressedY[pointerId] = event.getY(actionIndex);
			if(event.getX(actionIndex) > getWidth() / 2) {
				pressedControl = UserControlType.RIGHT;
				level.model.controlType = pressedControl;
			} else {
				pressedControl = UserControlType.LEFT;
				level.model.controlType = pressedControl;
			}
			return true;
		case MotionEvent.ACTION_POINTER_UP:
			if(event.getPointerCount() > 2) return true;
			int anotherPointer = event.getActionIndex() == 0 ? 1 : 0;
			if((pressedControl != UserControlType.UP &&
					pressedControl !=UserControlType.DOWN) ||
					slideSenderID == pointerId) {
				if(event.getX(anotherPointer) > getWidth() / 2) {
					pressedControl = UserControlType.RIGHT;
				} else {
					pressedControl = UserControlType.LEFT;
				}
			}
			return true;
		case MotionEvent.ACTION_UP:
			pressedControl = UserControlType.IDLE;
			return true;
		case MotionEvent.ACTION_MOVE:
			if(event.getPointerCount() > 2) return true;
			for(int ai = 0; ai < event.getPointerCount(); ai++) {
				pointerId = event.getPointerId(ai); 
				if(event.getY(ai) - startPressedY[pointerId] > 20) {
					pressedControl = UserControlType.DOWN;
					level.model.controlType = pressedControl;
					startPressedY[pointerId] = event.getY(ai);
					slideSenderID = pointerId;
					break;
				} else if(event.getY(ai) - startPressedY[pointerId] < -20) {
					pressedControl = UserControlType.UP;
					level.model.controlType = pressedControl;
					startPressedY[pointerId] = event.getY(ai);
					slideSenderID = pointerId;
					break;
				}
			}
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
		boolean controlState = hero.isInControlState();
		boolean readyOnFinishing;
		if(hero.isFinishing()) {
			readyOnFinishing = !hero.tryToEndFinishMotion();
			// when motion at last switches
			if(!readyOnFinishing) {
				prevCell.updateCell(level.model.getMotionType());
			}
		} else {
			readyOnFinishing = true;
		}
		boolean readyOnStarting;
		if(hero.isStarting()) {
			readyOnStarting = !hero.tryToEndStartMotion();
		} else {
			readyOnStarting = true;
		}
		boolean stateReady = controlState && readyOnFinishing && readyOnStarting;
		// change behavior only if hero is on grid point
		return stateReady && (heroX % GRID_STEP == 0) && (heroY % GRID_STEP == 0);
	}
	
	public void updateGame() {
		if(level.model.controlType == UserControlType.IDLE) {
			level.model.controlType = pressedControl;
		}
		prevCell = level.model.getHeroCell();
		level.model.updateGame();
		hero.changeMotion(level.model.getMotionType());
		if(!hero.isFinishing()) {
			prevCell.updateCell(level.model.getMotionType());
		}
	}

}
