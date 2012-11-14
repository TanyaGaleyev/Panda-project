package org.ivan.simple.game;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.R;
import org.ivan.simple.UserControlType;
import org.ivan.simple.game.hero.Hero;
import org.ivan.simple.game.level.LevelCell;
import org.ivan.simple.game.level.LevelView;

import android.content.Context;
import android.graphics.Bitmap;
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
	
	private Hero hero;
	private LevelView level;
	
	private GameControl control;
	
	private Bitmap background;
	
	private LevelCell prevCell;
	
	private int levId = 0;
	
	protected boolean finished = false;
	
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
		getHolder().addCallback(new SurfaceHolder.Callback() {
			
			public void surfaceDestroyed(SurfaceHolder holder) {
				// turn motion to initial stage (stage == 0)
				level.model.getMotionType().startMotion();
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
				gameLoopThread = new GameManager(GameView.this);
				gameLoopThread.setRunning(true);
				gameLoopThread.start();
			}
			
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				
			}
		});

	}
	
	private void initSurface() {
		background = ImageProvider.getBitmap(R.drawable.background_1);
		
		GRID_STEP = hero.getSprite().getWidth() % 4 == 0 ? hero.getSprite().getWidth() : (hero.getSprite().getWidth() / 4  + 1) * 4;
		TOP_BOUND = GRID_STEP;
		BOTTOM_BOUND = getHeight() - GRID_STEP;
		BOTTOM_BOUND -= BOTTOM_BOUND % GRID_STEP;
		LEFT_BOUND = GRID_STEP;
		RIGHT_BOUND = getWidth() - GRID_STEP;
		RIGHT_BOUND -= RIGHT_BOUND % GRID_STEP;
		JUMP_SPEED = GRID_STEP;
		ANIMATION_JUMP_SPEED = JUMP_SPEED / 8;
		
		hero.heroX = LEFT_BOUND + level.model.heroX * GRID_STEP;
		hero.heroY = TOP_BOUND + level.model.heroY * GRID_STEP;
	}
	
	/**
	 * Draw hero, level and etc.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
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
	
	private void drawFPS(Canvas canvas) {
		Paint paint = new Paint(); 
		paint.setStyle(Paint.Style.FILL); 
		paint.setTextSize(25); 
		paint.setColor(Color.BLUE);
		canvas.drawText("FPS: " + GameManager.getFPS(), 5, 25, paint);
	}
	
	private void drawWin(Canvas canvas) {
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
	
	private void drawLose(Canvas canvas) {
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
	
	private void drawGrid(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		for(int x = LEFT_BOUND - GRID_STEP / 2; x <= RIGHT_BOUND + GRID_STEP / 2; x += GRID_STEP) {
			canvas.drawLine(x, TOP_BOUND - GRID_STEP / 2, x, BOTTOM_BOUND + GRID_STEP / 2, paint);
		}
		for(int y = TOP_BOUND - GRID_STEP / 2; y <= BOTTOM_BOUND + GRID_STEP / 2; y += GRID_STEP) {
			canvas.drawLine(LEFT_BOUND - GRID_STEP / 2, y, RIGHT_BOUND + GRID_STEP / 2, y, paint);
		}
	}
	
	/**
	 * Checks if game is ready to switch hero animation and/or motion
	 * @return
	 */
	protected boolean readyForUpdate() {
		// if the level is complete or lost the game should be not updatable on this level 
		if(level.model.isLost()) return false;
		if(level.model.isComplete()) return false;
		
		boolean inControlState = hero.isInControlState();
		/*
		 * Hero is in control state usually when motion animation has ended
		 * If hero animation is in starting state game model should not be updated
		 * (after starting animation main animation will be played)
		 * If hero animation is in finishing state game model should not be updated
		 * (after finishing animation next motion animation will begin)   
		 */
		boolean stateReady = inControlState;
		// change behavior only if hero is in ready for update state AND is on grid point
		return stateReady && (hero.heroX % GRID_STEP == 0) && (hero.heroY % GRID_STEP == 0);
	}
	
	/**
	 * Switch hero animation and motion
	 */
	protected void updateGame() {
		// try to end pre/post motion if it exists
		boolean continued = continueModel();
		// get new motion type only if it was not obtained yet
		// (obtained yet means that pre- or post- motion was just ended)
		if(!continued) {
			updateModel();
		}
	}
	
	/**
	 * Use user control to obtain next motion type, move hero in model (to next level cell),
	 * switch hero motion animation and cell platforms reaction to this animation  
	 */
	private void updateModel() {
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
	
	/**
	 * Switch to next animation after pre/post- animation finished
	 * @return true if pre or post animation ended, otherwise - false 
	 */
	private boolean continueModel() {
		if(hero.isFinishing()) {
			// when motion at last switches we need to play cell animation
			if(hero.tryToEndFinishMotion()) {
				prevCell.updateCell(level.model.getMotionType());
			}
			return true;
		}

		if(hero.isStarting()) {
			hero.tryToEndStartMotion();
			return true;
		}
		return false;
	}
	
	/**
	 * Move hero sprite on the screen
	 */
	protected void updateHeroScreenPosition() {
		if(level.model.isLost()) {
			finished = !moveLose();
		} else if(level.model.isComplete()) {
			finished = !hero.playWinAnimation();
		} else {
			if(hero.getRealMotion() == MotionType.TP_LEFT || hero.getRealMotion() == MotionType.TP_RIGHT) {
				hero.heroX = LEFT_BOUND + level.model.heroX * GRID_STEP;
				hero.heroY = TOP_BOUND + level.model.heroY * GRID_STEP;
			}
			int xSpeed = hero.getRealMotion().getXSpeed() * ANIMATION_JUMP_SPEED;
			int ySpeed = hero.getRealMotion().getYSpeed() * ANIMATION_JUMP_SPEED;
			
			hero.heroX += xSpeed;
			hero.heroY += ySpeed;
		}
	}
	
	/**
	 * Random rotating movement if hero was spiked
	 * @return
	 */
	private boolean moveLose() {
		if((-GRID_STEP < hero.heroX && hero.heroX < getWidth() + GRID_STEP) && (-GRID_STEP < hero.heroY && hero.heroY < getHeight() + GRID_STEP)) {
			if(hero.getRealMotion() == MotionType.FALL) {
				hero.heroY += ANIMATION_JUMP_SPEED;
				return true;
			}
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
			return true;
		}
		return false;
	}
	
	public boolean isComplete() {
		return level.model.isComplete();
	}
	
	protected void initLevel(int levId) {
		hero = new Hero();
		this.levId = levId; 
		level = new LevelView(levId);
		control = new GameControl(level.model, hero);
		prevCell = level.model.getHeroCell();
	}
	
	protected int getLevId() {
		return levId;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(control.oneHandControl(event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}

}
