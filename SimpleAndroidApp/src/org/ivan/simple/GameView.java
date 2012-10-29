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
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
	
	public static int GRID_STEP;
	
	private static int JUMP_SPEED;
	private static int ANIMATION_JUMP_SPEED;
	
	private static int LEFT_BOUND;
	private static int RIGHT_BOUND;
	private static int TOP_BOUND;
	private static int BOTTOM_BOUND;
	
	private GameManager gameLoopThread;
	
	private SurfaceHolder holder;
	
	private Hero hero;
	private LevelView level;
	
	private GameControl control;
	
	private Bitmap background;
	
	private LevelCell prevCell;
	
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
				initSurface();
				gameLoopThread.setRunning(true);
				gameLoopThread.start();
			}
			
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				
			}
		});

	}
	
	private void initSurface() {
		
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
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(background, 0, 0, null);
		level.onDraw(canvas);
		if(level.model.isLost()) {
			moveLose();
		} else if(level.model.isComplete()) {
			hero.playWinAnimation();
		} else {
			int xSpeed = hero.getRealMotion().getXSpeed() * ANIMATION_JUMP_SPEED;
			int ySpeed = hero.getRealMotion().getYSpeed() * ANIMATION_JUMP_SPEED;
			
			hero.heroX += xSpeed;
			hero.heroY += ySpeed;
		}
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
	
	public void moveLose() {
		if((-GRID_STEP < hero.heroX && hero.heroX < getWidth() + GRID_STEP) || (-GRID_STEP < hero.heroY && hero.heroY < getHeight() + GRID_STEP)) {
			hero.playLoseAnimation();
			double rand = Math.random();
			if(rand < 0.33) {
				hero.heroX += JUMP_SPEED;
			} else if(rand < 0.66) {
				hero.heroX -= JUMP_SPEED;
			}
			rand = Math.random();
			if(rand < 0.33) {
				hero.heroY += JUMP_SPEED;
			} else if(rand < 0.66) {
				hero.heroY -= JUMP_SPEED;
			}
		}
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
	
	public boolean readyForUpdate() {
		// if the level is complete or lost the game should be not updatable on this level 
		if(level.model.isLost()) return false;
		if(level.model.isComplete()) return false;
		
		boolean inControlState = hero.isInControlState();
		
		boolean inFinishingState = hero.isFinishing();
		if(inFinishingState) {
			// when motion at last switches
			if(hero.tryToEndFinishMotion()) {
				prevCell.updateCell(level.model.getMotionType());
			}
		}

		boolean inStartingState = hero.isStarting();
		if(inStartingState) {
			hero.tryToEndStartMotion();
		}
		/*
		 * Hero is in control state usually when motion animation has ended
		 * If hero animation is in starting state game model should not be updated
		 * (after starting animation main animation will be played)
		 * If hero animation is in finishing state game model should not be updated
		 * (after finishing animation next motion animation will begin)   
		 */
		boolean stateReady = inControlState && !inStartingState && !inFinishingState;
		// change behavior only if hero is in ready for update state AND is on grid point
		return stateReady && (hero.heroX % GRID_STEP == 0) && (hero.heroY % GRID_STEP == 0);
	}
	
	public void updateGame() {
		// Used to remember pressed control (action down performed and no other actions after)
		if(level.model.getControlType() == UserControlType.IDLE) {
			level.model.setControlType(control.pressedControl);
		}
		// Store cell before update in purpose to play cell animation (like floor movement while jump) 
		prevCell = level.model.getHeroCell();
		// calculate new motion depending on current motion, hero cell and user control
		level.model.updateGame();
		// switch hero animation
		hero.changeMotion(level.model.getMotionType());
		// play cell reaction to new motion
		if(!hero.isFinishing()) {
			prevCell.updateCell(level.model.getMotionType());
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(control.oneHandControl(event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}

}
