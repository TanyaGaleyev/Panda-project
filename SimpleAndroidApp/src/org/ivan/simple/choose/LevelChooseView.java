package org.ivan.simple.choose;

import java.io.IOException;
import java.util.StringTokenizer;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.PandaApplication;
import org.ivan.simple.UserControlType;
import org.ivan.simple.bitmaputils.ColorBackground;
import org.ivan.simple.bitmaputils.PandaBackground;
import org.ivan.simple.bitmaputils.TextureAtlasParser;
import org.ivan.simple.game.GameActivity;
import org.ivan.simple.game.scores.Scores;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class LevelChooseView extends SurfaceView {

	private static final int LEVELS_ROWS = 3;
    private static final int LEVELS_COLS = 4;
    private int GRID_STEP;
	private int LEFT_BOUND;
	private int TOP_BOUND;
    private int markerSpeed() {
        return GRID_STEP / 8;
    }

    // selected level coordinates in array
	private int levelX = 0;
    private int levelY = 0;
    /**
	 * Matrix with levels IDs
	 */
	private int[][][] levels;
    private int[][] finishedLevels;
	/**
	 * Border of levels to select
	 */
	private Bitmap border;


	/**
	 * Marks complete levels
	 */
//	private Bitmap cross;

	// Backgroung image of LevelChooseView
	private String backgroundId;
    private Bitmap background;
//    private PandaBackground bgr;

    // Scores bitmaps
    private Bitmap highscore;

    private Bitmap mediumscore;
    private Bitmap lowscore;
	private Paint textPaint;
    private Paint backgroundPaint;
    private Paint gridPaint;

	/**
	 * Marker is a panda image moving from one level to another (choosing level)
	 */
	private Bitmap marker;
	// coordinates of marker center on screen
	private int markerX = -1;
	private int markerY = -1;

	// Thread redrawing view
	private Redrawer redrawer;

	private boolean chooseReady = true;

	// buffer choose level action
	private UserControlType performingAction = UserControlType.IDLE;
	private UserControlType chooseAcion = UserControlType.IDLE;
    private LevelChooseActivity context;

    public LevelChooseView(Context context) {
		super(context);
		init(context);
	}

	public LevelChooseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LevelChooseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private boolean initialized = false;
    private void init(Context context) {
        this.context = (LevelChooseActivity) context;
		textPaint = new Paint();
		textPaint.setColor(Color.DKGRAY);
		textPaint.setTextSize(36);
		textPaint.setTypeface(PandaApplication.getPandaApplication().getFontProvider().regular());
        textPaint.setAntiAlias(true);
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        gridPaint = new Paint();
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(2);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{5, 2}, 0));
		getHolder().addCallback(new SurfaceHolder.Callback() {

			public void surfaceDestroyed(SurfaceHolder holder) {
				boolean retry = true;
				redrawer.running = false;
				while (retry) {
                   try {
                         redrawer.join();
                         retry = false;
                   } catch (InterruptedException e) {

                   }
                }
				System.out.println("Choose view destroyed!");
			}

			public void surfaceCreated(SurfaceHolder holder) {
                if(!initialized) {
                    initSurface();
                    initialized = true;
                }

				redrawer = new Redrawer();
				redrawer.start();
			}

			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
			}
		});

	}

    private void initGrid(int width, int height) {
        int xStep = width / LEVELS_COLS;
        int yStep = height / LEVELS_ROWS;
        if(xStep < yStep) {
            GRID_STEP = xStep - xStep % 8;
            LEFT_BOUND = 0;
            TOP_BOUND = (height - GRID_STEP * LEVELS_ROWS) / 2;
        } else {
            GRID_STEP = yStep - yStep % 8;
            TOP_BOUND = 0;
            LEFT_BOUND = (width - GRID_STEP * LEVELS_COLS) / 2;
        }
        if(markerX < 0) {
            markerX = getScreenX(0);
            markerY = getScreenY(0);
        }
    }

    private void initSurface() {
        initGrid(getWidth(), getHeight());
        border = imageProvider().getBitmapNoCache("menu/border.png");
//        cross = imageProvider().getBitmapNoCache("menu/cross.png");
//        try {
//            bgr = new TextureAtlasParser().createTextureAtlasBackground(context, backgroundId);
//        } catch (IOException e) {
//            e.printStackTrace();
//            bgr = new ColorBackground();
//        } catch (XmlPullParserException e) {
//            bgr = new ColorBackground();
//        }
        background = imageProvider().getBackground(backgroundId, getWidth(), getHeight());
        marker = imageProvider().getBitmapNoCache("menu/single_panda.png");
        highscore = imageProvider().getBitmapNoCache("menu/high_score.png");
        mediumscore = imageProvider().getBitmapNoCache("menu/medium_score.png");
        lowscore = imageProvider().getBitmapNoCache("menu/low_score.png");
    }

    private ImageProvider imageProvider() {
        return context.app().getImageProvider();
    }

    @Override
	protected void onDraw(Canvas canvas) {
        if(context.isLoading()) {
            drawLoading(canvas);
        } else {
            drawChoose(canvas);
        }
    }

    private void drawChoose(Canvas canvas) {
        moveMarker(performingAction);
        canvas.drawColor(Color.rgb(218, 228, 115));
//        bgr.draw(canvas);
        canvas.drawBitmap(background, 0, 0, backgroundPaint);
        drawGrid(canvas);
        drawLevelsIcons(canvas);
        drawOnCenterCoordinates(marker, markerX, markerY, canvas);
    }

    private void moveMarker(UserControlType moveAction) {
        // move marker
        switch(moveAction) {
            case UP:
                markerY -= markerSpeed();
                break;
            case DOWN:
                markerY += markerSpeed();
                break;
            case LEFT:
                markerX -= markerSpeed();
                break;
            case RIGHT:
                markerX += markerSpeed();
                break;
            default:
                break;
        }
    }

    private void moveMarker(int row, int col) {
        markerX = getScreenX(col);
        markerY = getScreenY(row);
    }

    private void drawGrid(Canvas canvas) {
        int gap = 5;
        for(int i = 0; i < LEVELS_COLS; i++) {
            int colX = getScreenX(i);
            for(int j = 0; j < LEVELS_ROWS - 1; j++) {
                canvas.drawLine(
                        colX,
                        getScreenY(j) + border.getWidth() / 2 - gap,
                        colX,
                        getScreenY(j + 1) - border.getWidth() / 2 + gap,
                        gridPaint
                );
            }
        }
        for(int j = 0; j < LEVELS_ROWS; j++) {
            int rowY = getScreenY(j);
            for(int i = 0; i < LEVELS_COLS - 1; i++) {
                canvas.drawLine(
                        getScreenX(i) + border.getWidth() / 2 - gap,
                        rowY,
                        getScreenX(i + 1) - border.getWidth() / 2 + gap,
                        rowY,
                        gridPaint
                );
            }
        }
    }

    private void drawLevelsIcons(Canvas canvas) {
        int k = 1;
        for(int i = 0; i < levels.length; i++) {
            for(int j = 0; j < levels[i].length; j++) {
                int x = getScreenX(j);
                int y = getScreenY(i);
                drawOnCenterCoordinates(border, x, y, canvas);
//                drawTextOnCenter("" + levels[i][j][0], x, y, canvas);
                drawTextOnCenter("" + k++, x, y, canvas);
                drawScore(i, j, canvas);
            }
        }
    }

    private void drawTextOnCenter(String text, int x, int y, Canvas canvas) {
        Rect textRect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textRect);
        canvas.drawText(text, x - textRect.centerX(), y - textRect.centerY(), textPaint);
    }

    private void drawScore(int i, int j, Canvas canvas) {
//        drawOnCenterCoordinates(cross, x + border.getWidth() / 4, y + border.getHeight() / 4, canvas);
        Bitmap scoresImg = getScoreAward(i, j);
        if(scoresImg != null) {
            drawOnCenterCoordinates(
                    scoresImg,
                    getScreenX(j) + border.getWidth() / 2 - 10,
                    getScreenY(i) + border.getHeight() / 2,
                    canvas);
        }
    }

    private void drawLoading(Canvas canvas) {
        canvas.drawColor(Color.rgb(75, 64, 50));
        PandaApplication.getPandaApplication().getLoading().onDraw(
                canvas, getWidth() / 2, getHeight() / 2, true);
    }

    private void drawOnCenterCoordinates(Bitmap bitmap, int x, int y, Canvas canvas) {
		canvas.drawBitmap(bitmap, x - bitmap.getWidth() / 2, y - bitmap.getHeight() / 2, null);
	}
	
	private Bitmap getScoreAward(int i, int j) {
		int score = finishedLevels[i][j];
		// TODO add uniq score gradations for each level
        if(score == Scores.LOW_SCORE) {
			return lowscore;
		} else if (score == Scores.MEDIUM_SCORE) {
			return mediumscore;
		} else if (score == Scores.HIGH_SCORE) {
			return highscore;
		} else {
		    return null;
		    // А Танюшка Ванюшка!!!
        }
	}
	
	private int getScreenX(int col) {
		return LEFT_BOUND + col * GRID_STEP + GRID_STEP / 2;
	}

    private int getGridCol(int x) {
        return (x - LEFT_BOUND) / GRID_STEP;
    }
	
	private int getScreenY(int row) {
		return TOP_BOUND + row * GRID_STEP + GRID_STEP / 2;
	}

    private int getGridRow(int y) {
        return (y - TOP_BOUND) / GRID_STEP;
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
        // ignore other actions
        if(event.getAction() != MotionEvent.ACTION_DOWN) return super.onTouchEvent(event);
		// get level Id depending on by click on screen selection
		final int levId = getLevelId(event);
		// if level selected (levId != 0) start next GameActivity with specified level
		if(levId != 0) {
            context.setLoading(true);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    startLevel(levId);
                    return null;
                }
            }.execute();
		} else {
            chooseAcion = scanMoveDiection(event);
        }
		return true;
	}

    private UserControlType scanMoveDiection(MotionEvent event) {
        // Get choice direction
        UserControlType tempAction = getMoveType(event);
        switch(tempAction) {
            case UP:
                if(levelY <= 0 || levelX >= levels[levelY - 1].length) tempAction = UserControlType.IDLE;
                else levelY -= 1;
                break;
            case DOWN:
                if(levelY >= levels.length - 1 || levelX >= levels[levelY + 1].length) tempAction = UserControlType.IDLE;
                else levelY += 1;
                break;
            case LEFT:
                if(levelX <= 0) tempAction = UserControlType.IDLE;
                else levelX -= 1;
                break;
            case RIGHT:
                if(levelX >= levels[levelY].length - 1) tempAction = UserControlType.IDLE;
                else levelX += 1;
                break;
            default:
                break;
        }
        return tempAction;
    }

    void startLevel(int levId) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(LevelChooseActivity.LEVEL_ID, levId);
        context.startActivityForResult(intent, LevelChooseActivity.FINISHED_LEVEL_ID);
    }

    private static final long FAST_CLICK_MILLS = 200L;
    private long lastClickTime = 0L;
    public synchronized int getLevelId(MotionEvent event) {
        // TODO strictly we need check that fast click chosen level was not changed
        long gap = event.getEventTime() - lastClickTime;
        if(gap < FAST_CLICK_MILLS) {
            levelY = getGridRow((int) event.getY());
            levelX = getGridCol((int) event.getX());
            if(inGridBounds(levelX, levelY)) {
                moveMarker(levelY, levelX);
                return levels[levelY][levelX][0];
            }
        }
        lastClickTime = event.getEventTime();
        // if marker is moving choosing are not allowed
		if(!chooseReady) return 0;
		// switch to moving state
		chooseReady = false;
		// Click on markered cell means level choise
		if(markerX - GRID_STEP / 2 < event.getX() &&
				event.getX() < markerX + GRID_STEP / 2 &&
				markerY - GRID_STEP / 2 < event.getY() && 
				event.getY() < markerY + GRID_STEP / 2) {
			return levels[levelY][levelX][0];
		}
		return 0;
	}

    private boolean inGridBounds(int levelX, int levelY) {
        return levelY >= 0 && levelY < levels.length && levelX >=0 && levelX < levels[levelY].length;
    }

    protected UserControlType getMoveType(MotionEvent event) {
		float dX = markerX - event.getX(); // positive dx move left
		float dY = markerY - event.getY(); // positive dy move up
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
	
	private boolean isOnGridCenter(int x, int y) {
        return (x - GRID_STEP / 2 - LEFT_BOUND) % GRID_STEP == 0 &&
                (y - GRID_STEP / 2 - TOP_BOUND) % GRID_STEP == 0;
    }
	
	public int completeCurrentLevel(int score) {
		int oldScore = finishedLevels[levelY][levelX];
        if(Scores.better(score, oldScore)) {
            finishedLevels[levelY][levelX] = score;
        }
		return oldScore;
	}
	
	protected String getFinishedLevels() {
		String finishedArray = "";
		for(int i = 0; i < levels.length; i++) {
			for(int j = 0; j < levels[i].length; j++) {
				finishedArray += finishedLevels[i][j];
				finishedArray += ",";
			}
		}
		return finishedArray;
	}
	
	protected boolean allLevelsFinished() {
		for(int i = 0; i < levels.length; i++) {
			for(int j = 0; j < levels[i].length; j++) {
				if(finishedLevels[i][j] == 0) return false;
			}
		}
		return true;
	}
	
	protected void setChooseScreenProperties(int id, String finishedArray) {
		setLevelsId(id);
		setFinishedLevels(finishedArray);
		backgroundId = getBackgroundId(id);
	}
	
	private void setFinishedLevels(String finishedArray) {
		StringTokenizer st = new StringTokenizer(finishedArray, ",");
		for(int i = 0; i < levels.length; i++) {
			for(int j = 0; j < levels[i].length; j++) {
				if(!st.hasMoreTokens()) break;
				String strScore = st.nextToken();
				if(strScore.length() == 0) continue;
				finishedLevels[i][j] = Integer.parseInt(strScore);
			}
		}
	}
	
	private void setLevelsId(int id) {
        try {
            levels = PandaApplication.getPandaApplication().getLevelParser().readLevelsPackInfo(id);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        finishedLevels = new int[levels.length][];
		for(int i = 0; i < levels.length; i++) {
			finishedLevels[i] = new int[levels[i].length];
		}
	}

    private String getBackgroundId(int levelsid) {
        switch(levelsid) {
            case 1: return "background/background_c_1.jpg";
            case 2: return "background/background_c_2.jpg";
            case 3: return "background/background_c_3.jpg";
            default:return "background/background_c_1.jpg";
        }
    }

    public void releaseResources() {
        if(background != null) background.recycle();
    }
	
	private class Redrawer extends Thread {
		boolean running = true;
		@Override
		public void run() {
			while(running) {
				if(isOnGridCenter(markerX, markerY)) {
					chooseReady = true;
					performingAction = chooseAcion;
					chooseAcion = UserControlType.IDLE;
				}
				Canvas c = null;
				try {
					c = getHolder().lockCanvas();
					if(c != null) {
						synchronized (getHolder()) {
							onDraw(c);
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					if(c != null) {
						getHolder().unlockCanvasAndPost(c);
					}
				}
				try {
					sleep(40);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
