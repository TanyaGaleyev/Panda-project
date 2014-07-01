package com.pavlukhin.acropanda.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.pavlukhin.acropanda.ImageProvider;
import com.pavlukhin.acropanda.PandaApplication;
import com.pavlukhin.acropanda.UserControlType;
import com.pavlukhin.acropanda.bitmaputils.cache.Recycler;
import com.pavlukhin.acropanda.game.hero.Hero;
import com.pavlukhin.acropanda.game.level.LevelCell;
import com.pavlukhin.acropanda.game.level.LevelModel;
import com.pavlukhin.acropanda.game.level.LevelView;
import com.pavlukhin.acropanda.game.level.PlatformType;
import com.pavlukhin.acropanda.game.monster.Monster;
import com.pavlukhin.acropanda.game.monster.MonsterFactory;
import com.pavlukhin.acropanda.game.motion.MotionType;
import com.pavlukhin.acropanda.game.tutorial.GuideAnimation;

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
	protected LevelView level;
    GuideAnimation guideAnimation = new GuideAnimation();
	
	private GameControl control;
	
	protected Bitmap background;
//    private PandaBackground bgr;

    private Paint backgroundPaint;

	protected LevelCell prevCell;
    private GameActivity gameContext;
    private boolean initialized = false;

    public GameView(GameActivity context) {
		super(context);
        this.gameContext = context;
		init();
        control = new GameControl(this);
    }

	public GameControl getControl() {
		return control;
	}
	
	private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        getHolder().addCallback(new SurfaceHolder.Callback() {
			
			public void surfaceDestroyed(SurfaceHolder holder) {
				System.out.println("surfaceDestroyed");
                control.stopManager();
			}
			
			public void surfaceCreated(SurfaceHolder holder) {
				System.out.println("surfaceCreated");
                if(!initialized) {
                    initialized = true;
                    initBackground();
                    control.initGame();
                }
				control.getGameLoopThread().doDraw(false);
			}
			
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				System.out.println("surfaceChanged");
				control.getGameLoopThread().doDraw(false);
			}
		});

	}

    private void initBackground() {
//        try {
//            bgr = new TextureAtlasParser().createTextureAtlasBackground(gameContext, backgroundId);
//        } catch (IOException e) {
//            bgr = new ColorBackground();
//        } catch (XmlPullParserException e) {
//            bgr = new ColorBackground();
//        }
        Recycler recycler = imageProvider().getCacheRecycler();
        boolean retry = true;
        while (retry) {
            try {
                background = imageProvider().getBackground(
                        BackgroundFactory.getGameBackgroundPath(control.levId), getWidth(), getHeight());
                retry = false;
            } catch (OutOfMemoryError oom) {
                recycler.recycle();
                System.err.println("Retry GameView.initBackground()");
            }
        }
    }

	protected void initView(LevelModel model) {
        GRID_STEP = imageProvider().getGridStep();
//		System.out.println("GRID_STEP = " + GRID_STEP);;
		TOP_BOUND = (getHeight() - GRID_STEP * model.getRows()) / 2 + GRID_STEP / 2;
		// TODO check this bound carefully!
		LEFT_BOUND = (getWidth() - GRID_STEP * model.getCols()) / 2 + GRID_STEP / 2;
		JUMP_SPEED = GRID_STEP;
		ANIMATION_JUMP_SPEED = JUMP_SPEED / 8;

        BOTTOM_BOUND = TOP_BOUND + model.getRows() * GRID_STEP;
        RIGHT_BOUND = LEFT_BOUND + model.getCols() * GRID_STEP;

        level = new LevelView(model, GRID_STEP, LEFT_BOUND, TOP_BOUND);

		prevCell = level.model.getHeroCell();

		hero = new Hero(level.model.hero);
		monster = MonsterFactory.createMonster(level.model.monster, GRID_STEP);

		hero.x = LEFT_BOUND + level.model.hero.getX() * GRID_STEP;
		hero.y = TOP_BOUND + level.model.hero.getY() * GRID_STEP;

		if(level.model.monster != null) {
			monster.setXCoordinate(LEFT_BOUND + level.model.monster.getCol() * GRID_STEP);
			monster.setYCoordinate(TOP_BOUND + level.model.monster.getRow() * GRID_STEP);
		}

        guideAnimation = new GuideAnimation();
	}

    private ImageProvider imageProvider() {
        return getGameContext().app().getImageProvider();
    }

    /**
	 * Draw hero, level and etc.
	 */
	@Override
	public void draw(Canvas canvas) {
		draw(canvas, false);
	}
	
	public void draw(Canvas canvas, boolean update) {
		canvas.drawColor(0xFFB6D76E);
//        bgr.draw(canvas);
        canvas.drawBitmap(background, 0, 0, backgroundPaint);
//		level.onDraw(canvas, update);
        level.getBackLayer().draw(canvas, update);
		hero.onDraw(canvas, update);
        level.getFrontLayer().draw(canvas, update);
		monster.onDraw(canvas, update);
        guideAnimation.onDraw(canvas, update);
//		level.drawGrid(canvas);
		drawFPS(canvas);
		drawScore(canvas);
//        drawMemoryUsage(canvas);
        if(isReadyToPlayLoseAnimation()) {
//            drawLose(canvas);
        } else if(isReadyToPlayWinAnimation()) {
            drawWin(canvas);
            control.playWinSound();
        }
    }

    private void drawOnCenterCoordinates(Bitmap bitmap, int x, int y, Paint paint, Canvas canvas) {
        canvas.drawBitmap(bitmap, x - bitmap.getWidth() / 2, y - bitmap.getHeight() / 2, paint);
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
        canvas.rotate(-35, getWidth() / 2, getHeight() / 2);

        Paint textPaint = new Paint();
        textPaint.setColor(color);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(80);
        textPaint.setTypeface(PandaApplication.getPandaApplication().getFontProvider().bold());

        Paint borderPaint = new Paint();
        borderPaint.setColor(color);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(10);
        borderPaint.setPathEffect(new DashPathEffect(new float[]{30, 15}, 0));

        Rect textRect = new Rect();
        textPaint.getTextBounds(complete, 0, complete.length(), textRect);
        canvas.drawText(complete, getWidth() / 2 - textRect.exactCenterX(), getHeight() / 2 - textRect.exactCenterY(), textPaint);

        RectF border = new RectF(
                getWidth() / 2 - textRect.centerX() - 50,
                getHeight() / 2 - textRect.centerY() - 100,
                getWidth() / 2 + textRect.centerX() + 50,
                getHeight() / 2 + textRect.centerY() + 100);
        canvas.drawRoundRect(border, 10, 10, borderPaint);

        canvas.restore();
    }

	/**
	 * Checks if game is ready to switch hero animation and/or motion
	 */
	protected boolean readyForUpdate(UserControlType controlType) {
		// if the level is complete or lost the game should be not updateable on this level
		if(isReadyToPlayLoseAnimation()) return false;
		if(isReadyToPlayWinAnimation()) return false;
		
		boolean inControlState = hero.isInControlState();
        boolean interruptStayCase = checkInterruptStay(controlType);
		/*
		 * Hero is in control state usually when motion animation has ended
		 * If hero animation is in starting state game model should not be updated
		 * (after starting animation main animation will be played)
		 * If hero animation is in finishing state game model should not be updated
		 * (after finishing animation next motion animation will begin)   
		 */
		boolean stateReady = inControlState || interruptStayCase;
		// change behavior only if hero is in ready for update state AND is on grid point
		return stateReady;
	}

    private boolean checkInterruptStay(UserControlType controlType) {
        MotionType mt = hero.model.currentMotion.getType();
        PlatformType floorType = level.model.getHeroCell().getFloor().getType();
        return mt == MotionType.STAY &&
               (controlType == UserControlType.LEFT ||
                controlType == UserControlType.RIGHT ||
                controlType == UserControlType.UP && floorType != PlatformType.BRICK ||
                controlType == UserControlType.DOWN &&
                (floorType == PlatformType.ONE_WAY_DOWN || floorType == PlatformType.WAY_UP_DOWN)) ||
               mt == MotionType.TRY_JUMP_GLUE &&
               (controlType == UserControlType.LEFT || controlType == UserControlType.RIGHT) ||
               mt == MotionType.MAGNET && hero.model.currentMotion.getStage() < 2 &&
               controlType == UserControlType.DOWN ||
               mt == MotionType.CLOUD_IDLE;
    }

    /**
	 * Switch hero animation and motion
	 */
	protected void updateGame(UserControlType controlType) {
	    if(hero.isFinishing()) {
            // try to end pre/post motion if it exists
            continueModel();
        } else {
            // get new motion type only if it was not obtained yet
            // (obtained yet means that pre- or post- motion was just ended)
			updateModel(controlType);
            control.playSound();
		}
        if(!hero.isFinishing())
            prevCell.updateCell(hero.model.currentMotion, hero.model.finishingMotion);
	}
	
	/**
	 * Use user control to obtain next motion type, move hero in model (to next level cell),
	 * switch hero motion animation and cell platforms reaction to this animation  
	 */
	private void updateModel(UserControlType controlType) {
		// Used to remember pressed control (action down performed and no other actions after)
//		UserControlType controlType = control.getUserControl();
        // Store cell before update in purpose to play cell animation (like floor movement while jump)
		prevCell = level.model.getHeroCell();
		// calculate new motion depending on current motion, hero cell and user control
		level.model.updateGame(controlType);
		// switch hero animation
		hero.finishPrevMotion(prevCell, level.model.getHeroCell());
		// play cell reaction to new motion
		if(!hero.isFinishing()) {
			hero.switchToCurrentMotion();
		}
	}

    /**
	 * Switch to next animation after pre/post- animation finished
	 */
	private void continueModel() {
        // when motion at last switches we need to play cell animation
        if(hero.isFinishingMotionEnded()) {
            hero.switchToCurrentMotion();
        }
	}
	
	/**
	 * Move hero sprite on the screen
	 */
	protected void updateHeroScreenPosition() {
		if(isReadyToPlayLoseAnimation()) {
			control.finished = !moveLose();
		} else if(isReadyToPlayWinAnimation()) {
			control.finished = !hero.playWinAnimation();
		} else {
            regularMove();
		}
	}

    private void regularMove() {
        if(hero.getRealMotion().getType().isHorizontalTP() ||
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
	 */
	private boolean moveLose() {
        if(control.monsterLose) {
            return doMonsterLose();
        } else if(level.model.outOfBounds()) {
            return doFallLose();
        } else {
            return doSpikeLose();
        }
	}

    private boolean doSpikeLose() {
        if (control.loseDelay > 0) {
            control.loseDelay--;
            return true;
        } else {
            control.playDetonateSound();
            return hero.playDetonateAnimation();
        }
    }

    private boolean doFallLose() {
        regularMove();
        return !outOfAnimationBounds();
    }

    private boolean doMonsterLose() {
        control.playDetonateSound();
        return hero.playDetonateAnimation();
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

    private boolean isReadyToPlayLoseAnimation() {
        return level.model.isLost() && !hero.isFinishing();
    }
	
	public int getScore() {
		return level.model.getScore();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
        control.firstStartGame();
		if(control.scanControl(event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	public void updateMonster() {
		if(isGridCoordinates(monster.getXCoordinate(), monster.getYCoordinate())) {
			level.model.nextDirection();
		}
		monster.moveInCurrentDirection();
	}
	
	private boolean isGridCoordinates(int xCoordinate, int yCoordinate) {
		return (xCoordinate - LEFT_BOUND) % GRID_STEP == 0 && 
			   (yCoordinate - TOP_BOUND) % GRID_STEP == 0;
	}
	
	protected void checkMonsterCollision() {
        if(monster == null) return;
        int heroShrink = (int) (GRID_STEP * 0.10);
        int monsterShrink = heroShrink;
        Rect heroRect = shrinkRect(hero.x, hero.y, GRID_STEP, GRID_STEP, heroShrink);
        Rect monsterRect = shrinkRect(
                monster.getXCoordinate(), monster.getYCoordinate(), GRID_STEP, GRID_STEP, monsterShrink);
        if(heroRect.intersect(monsterRect)) {
            level.model.setLost(true);
            control.monsterLose = true;
        }
	}

    private Rect shrinkRect(int x, int y, int w, int h, int shrink) {
        return new Rect(
                x + shrink,
                y + shrink,
                x + w - 2 * shrink,
                y + h - 2 * shrink);
    }

    public void updatePositions() {
        updateHeroScreenPosition();
        updateMonster();
        checkMonsterCollision();
    }

    public GameActivity getGameContext() {
        return gameContext;
    }

    protected Hero getHero() {
        return hero;
    }
}
