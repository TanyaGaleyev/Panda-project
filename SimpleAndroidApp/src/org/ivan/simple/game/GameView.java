package org.ivan.simple.game;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.PandaApplication;
import org.ivan.simple.UserControlType;
import org.ivan.simple.game.hero.Hero;
import org.ivan.simple.game.level.LevelCell;
import org.ivan.simple.game.level.LevelModel;
import org.ivan.simple.game.level.LevelView;
import org.ivan.simple.game.monster.Monster;
import org.ivan.simple.game.monster.MonsterFactory;
import org.ivan.simple.game.motion.MotionType;
import org.ivan.simple.game.tutorial.GuideAnimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class GameView extends SurfaceView {
	
	private static int GRID_STEP;
	
	private static int JUMP_SPEED;
	private static int ANIMATION_JUMP_SPEED;
	
	private static int LEFT_BOUND;
	private static int RIGHT_BOUND;
	private static int TOP_BOUND;
	private static int BOTTOM_BOUND;

	private Hero hero;
	private Monster monster;
	private LevelView level;
    GuideAnimation guideAnimation = new GuideAnimation();
	
	private GameControl control = new GameControl(this);
	
	private String backgroundId;
	private Bitmap background;

    private Paint backgroundPaint;

	private LevelCell prevCell;
	
	private int levId = 0;
	
	protected boolean finished = false;
    private boolean monsterLose = false;
//    ServiceButtons serviceButtons;

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

	public GameControl getControl() {
		return control;
	}
	
	private final void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
		getHolder().addCallback(new SurfaceHolder.Callback() {
			
			public void surfaceDestroyed(SurfaceHolder holder) {
				System.out.println("surfaceDestroyed");
				// turn motion to initial stage (stage == 0)
				//level.model.getMotion().startMotion();
                control.stopManager();
                //ImageProvider.removeFromCatch(backgroundId);
//                background.recycle();
//                background = null;
			}
			
			public void surfaceCreated(SurfaceHolder holder) {
				System.out.println("surfaceCreated");
				initSurface();
				if(control.getGameLoopThread() == null) {
					control.startManager();
				}
				control.getGameLoopThread().doDraw(false);
			}
			
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				System.out.println("surfaceChanged");
				control.getGameLoopThread().doDraw(false);
			}
		});

	}
	
	protected void initSurface() {
//		GRID_STEP = ImageProvider.loadBitmapSize(R.drawable.single_panda).outWidth;
//		System.out.println(getHeight());
//		if(getHeight() < 432) {
//			GRID_STEP = 48;
//		} else if(getHeight() < 528) {
//			GRID_STEP = 72;
//		} else if(getHeight() < 672) {
//			GRID_STEP = 88;
//		} else {
//			GRID_STEP = 112;
//		}
//		GRID_STEP = GRID_STEP % 8 == 0 ? GRID_STEP : GRID_STEP + 8 - GRID_STEP % 8;
//		ImageProvider.setGridStep(GRID_STEP);
        background = imageProvider().getBitmapNoCache(backgroundId);
//                Bitmap.createScaledBitmap(
//                imageProvider().getBitmapNoCache(backgroundId),
//                getWidth(),
//                getHeight(),
//                false);

//        int gridWidth = getWidth() - 100;
//        int gridHeight = getHeight();
//        GRID_STEP = imageProvider().setScaleParameters(gridWidth, gridHeight);
        GRID_STEP = imageProvider().getGridStep();

//        serviceButtons = new ServiceButtons(10, 50);
        //GRID_STEP = 112;//88dp,48dp
//		System.out.println("GRID_STEP = " + GRID_STEP);

        LevelModel model = new LevelModel(levId);
		TOP_BOUND = (getHeight() - GRID_STEP * model.getRows()) / 2 + GRID_STEP / 2;
		// TODO check this bound carefully!
		LEFT_BOUND = (getWidth() - GRID_STEP * model.getCols()) / 2 + GRID_STEP / 2;
		JUMP_SPEED = GRID_STEP;
		ANIMATION_JUMP_SPEED = JUMP_SPEED / 8;

        BOTTOM_BOUND = TOP_BOUND + model.getRows() * GRID_STEP;
        RIGHT_BOUND = LEFT_BOUND + model.getCols() * GRID_STEP;

        level = new LevelView(model, GRID_STEP, LEFT_BOUND, TOP_BOUND);

		prevCell = level.model.getHeroCell();
//		prevMotion = level.model.getMotion();
		
		hero = new Hero(level.model.hero);
		monster = MonsterFactory.createMonster(level.model.monster);
		
		hero.x = LEFT_BOUND + level.model.hero.getX() * GRID_STEP;
		hero.y = TOP_BOUND + level.model.hero.getY() * GRID_STEP;
		
		if(level.model.monster != null) {
			monster.xCoordinate = LEFT_BOUND + level.model.monster.getCol() * GRID_STEP;
			monster.yCoordinate = TOP_BOUND + level.model.monster.getRow() * GRID_STEP;
		}

        guideAnimation = new GuideAnimation();
	}

    private ImageProvider imageProvider() {
        return PandaApplication.getPandaApplication().getImageProvider();
    }

    /**
	 * Draw hero, level and etc.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		onDraw(canvas, false);
	}
	
	protected void onDraw(Canvas canvas, boolean update) {
		canvas.drawColor(0xffC6E10E);
		canvas.drawBitmap(background, 0, 0, backgroundPaint);
//		serviceButtons.draw(canvas);
		level.onDraw(canvas, update);
		hero.onDraw(canvas, update);
		monster.onDraw(canvas, update);
        guideAnimation.onDraw(canvas, update);
//		level.drawGrid(canvas);
		drawFPS(canvas);
		drawScore(canvas);
        drawMemoryUsage(canvas);
		if(level.model.isLost()) {
			drawLose(canvas);
		} else if(isReadyToPlayWinAnimation()) {
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
	
	private void drawScore(Canvas canvas) {
		Paint paint = new Paint(); 
		paint.setStyle(Paint.Style.FILL); 
		paint.setTextSize(25);
		paint.setColor(Color.MAGENTA);
		canvas.drawText("Score: " + getScore(), 100, 25, paint);
	}

    private void drawMemoryUsage(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(25);
        paint.setColor(Color.BLACK);
        canvas.drawText(
                String.format(
                        "used: %d KiB free: %d KiB",
                        Runtime.getRuntime().totalMemory() / 1024,
                        Runtime.getRuntime().freeMemory() / 1024
                ), 300, 25, paint
        );
    }
	
	private void drawWin(Canvas canvas) {
        drawStamp(canvas, "COMPLETE", Color.RED);
	}

    private void drawLose(Canvas canvas) {
		drawStamp(canvas, "GAME OVER", Color.BLACK);
	}

    private void drawStamp(Canvas canvas, String complete, int color) {
        Paint paint = new Paint();
        paint.setTextSize(80);
        Rect textRect = new Rect();
        paint.getTextBounds(complete, 0, complete.length(), textRect);
        canvas.rotate(-35, getWidth() / 2, getHeight() / 2);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawText(complete, getWidth() / 2 - textRect.exactCenterX(), getHeight() / 2 - textRect.exactCenterY(), paint);
        canvas.restore();
    }

	/**
	 * Checks if game is ready to switch hero animation and/or motion
	 * @return
	 */
	protected boolean readyForUpdate() {
		// if the level is complete or lost the game should be not updatable on this level 
		if(level.model.isLost()) return false;
		if(isReadyToPlayWinAnimation()) return false;
		
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
		return stateReady;
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
		UserControlType controlType = control.getUserControl();
        // Store cell before update in purpose to play cell animation (like floor movement while jump)
		prevCell = level.model.getHeroCell();
		// calculate new motion depending on current motion, hero cell and user control
		level.model.updateGame(controlType);
		// switch hero animation
		hero.finishPrevMotion(prevCell);
		// play cell reaction to new motion
		if(!hero.isFinishing()) {
			hero.switchToCurrentMotion();
			prevCell.updateCell(hero.model.currentMotion, hero.model.finishingMotion);
		}
		
		control.playSound(level.model);
	}

    /**
	 * Switch to next animation after pre/post- animation finished
	 * @return true if pre or post animation ended, otherwise - false 
	 */
	private boolean continueModel() {
		if(hero.isFinishing()) {
			// when motion at last switches we need to play cell animation
			if(hero.isFinishingMotionEnded(/*level.model.getPrevMotion()*/)) {
				hero.switchToCurrentMotion();
				prevCell.updateCell(hero.model.currentMotion, hero.model.finishingMotion);
			}
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
		} else if(isReadyToPlayWinAnimation()) {
			finished = !hero.playWinAnimation();
		} else {
            regularMove();
		}
	}

    private void regularMove() {
        if(hero.getRealMotion().getType() == MotionType.TP_LEFT ||
                hero.getRealMotion().getType() == MotionType.TP_RIGHT ||
                hero.getRealMotion().getType() == MotionType.TP) {
            hero.x = LEFT_BOUND + level.model.hero.getX() * GRID_STEP;
            hero.y = TOP_BOUND + level.model.hero.getY() * GRID_STEP;
        }
        int xSpeed = hero.getRealMotion().getXSpeed() * ANIMATION_JUMP_SPEED;
        int ySpeed = hero.getRealMotion().getYSpeed() * ANIMATION_JUMP_SPEED;

        hero.x += xSpeed;
        hero.y += ySpeed;
    }

    /**
	 * Random rotating movement if hero was spiked
	 * @return
	 */
	private boolean moveLose() {
        if(level.model.outOfBounds() && !monsterLose) {
            regularMove();
            return !outOfAnimationBounds();
        } else {
            return hero.playLoseAnimation();
        }
	}

    private boolean outOfAnimationBounds() {
        return hero.x <= LEFT_BOUND - GRID_STEP || hero.x >= RIGHT_BOUND + GRID_STEP ||
               hero.y <= TOP_BOUND - GRID_STEP || hero.y >= BOTTOM_BOUND + GRID_STEP;
    }

    private void moveLoseRandom() {
        double rand = Math.random();
        if(rand < 0.33) {
            hero.x += JUMP_SPEED;
        } else if(rand < 0.66) {
            hero.x -= JUMP_SPEED;
        }
        rand = Math.random();
        if(rand < 0.33) {
            hero.y += JUMP_SPEED;
        } else if(rand < 0.66) {
            hero.y -= JUMP_SPEED;
        }
    }
	
	public boolean isComplete() {
		return level.model.isComplete();
	}

    private boolean isReadyToPlayWinAnimation() {
        return isComplete() && !hero.isFinishing();
    }
	
	public int getScore() {
		return level.model.getScore();
	}
	
	protected void setLevId(int levId) {
		this.levId = levId;
		this.backgroundId = getBackgroundId(levId);
	}

	private String getBackgroundId(int levId) {
		switch(levId) {
		case 1: return "background/background_l_1.jpg";
		case 2: return "background/background_l_2.jpg";
		case 3: return "background/background_l_3.jpg";
		case 4: return "background/background_l_4.jpg";
		default:return "background/background_l_4.jpg";
		}
	}
	
	protected int getLevId() {
		return levId;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		if(control.processServiceButton(event)) {
//			return true;
//		}
		if(control.scanControl(event, hero.x, hero.y)) {
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	public void updateMonster() {
		if(isGridCoordinates(monster.xCoordinate, monster.yCoordinate)) {
			level.model.nextDirection();
		}
		monster.moveInCurrentDirection(ANIMATION_JUMP_SPEED);
	}
	
	private boolean isGridCoordinates(int xCoordinate, int yCoordinate) {
		return (xCoordinate - LEFT_BOUND) % GRID_STEP == 0 && 
			   (yCoordinate - TOP_BOUND) % GRID_STEP == 0;
	}
	
	protected void checkMonsterColision() {
        if(monster == null) return;
        Rect heroRect = new Rect(hero.x, hero.y, hero.x + GRID_STEP, hero.y + GRID_STEP);
        Rect monsterRect = new Rect(
                monster.xCoordinate, monster.yCoordinate,
                monster.xCoordinate + GRID_STEP, monster.yCoordinate + GRID_STEP);
        if(heroRect.intersect(monsterRect)) {
            level.model.setLost(true);
            monsterLose = true;
        }
	}

    public void updatePositions() {
        updateHeroScreenPosition();
        updateMonster();
        checkMonsterColision();
    }

}
