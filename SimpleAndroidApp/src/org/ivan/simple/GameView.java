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
	
	LevelCell prevCell;
	
	private GameManager gameLoopThread;
	
	private SurfaceHolder holder;
	
	public LevelView level;
	private GameControl control;
	
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
		
		hero.heroX = LEFT_BOUND;
		hero.heroY = BOTTOM_BOUND;
		
		level = new LevelView(1);
		control = new GameControl(level.model, hero);
		prevCell = level.model.getHeroCell();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		xSpeed = hero.getRealMotion().getXSpeed() * ANIMATION_JUMP_SPEED;
		ySpeed = hero.getRealMotion().getYSpeed() * ANIMATION_JUMP_SPEED;
		
		hero.heroX += xSpeed;
		hero.heroY += ySpeed;
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(background, 0, 0, null);
		level.onDraw(canvas);
		hero.onDraw(canvas);
//		drawGrid(canvas);
		drawFPS(canvas);
		if(level.model.isLost()) {
			drawLose(canvas);
		} else if(level.model.isComplete()) {
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
	
	public void drawLose(Canvas canvas) {
		String complete = "GAME OVER";
		Paint paint = new Paint(); 		
		paint.setTextSize(80);
		Rect textRect = new Rect();
		paint.getTextBounds(complete, 0, complete.length(), textRect);
		canvas.rotate(-35, getWidth() / 2, getHeight() / 2);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLACK);
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
		if(control.oneHandControl(event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	public boolean readyForUpdate() {
		if(level.model.isLost()) return false;
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
		return stateReady && (hero.heroX % GRID_STEP == 0) && (hero.heroY % GRID_STEP == 0);
	}
	
	public void updateGame() {
		if(level.model.controlType == UserControlType.IDLE) {
			level.model.controlType = control.pressedControl;
		}
		prevCell = level.model.getHeroCell();
		level.model.updateGame();
		hero.changeMotion(level.model.getMotionType());
		if(!hero.isFinishing()) {
			prevCell.updateCell(level.model.getMotionType());
		}
	}

}
