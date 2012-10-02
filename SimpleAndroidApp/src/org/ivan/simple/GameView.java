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
	
	//private MoveEvent moveEvent;
	private UserControlType controlType = UserControlType.IDLE;
	private MotionType motionType = MotionType.STAY;
	private MotionType nextMotionType;
	
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
		
		level = new LevelView(1);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// change behavior only if hero is on grid point
		if(heroX % GRID_STEP == 0  && heroY % GRID_STEP == 0) {
			switch (motionType) {
			case STAY:
				switch (controlType) {
				case UP:
					if(motionAvaible(MotionType.JUMP)) {
						motionType = MotionType.JUMP;
					} else {
						motionType = MotionType.STAY;
					}
					break;
				case LEFT:
					if(motionAvaible(MotionType.STEP_LEFT)) {
						motionType = MotionType.STEP_LEFT;
					} else {
						motionType = MotionType.STAY;
					}
					break;
				case RIGHT:
					if(motionAvaible(MotionType.STEP_RIGHT)) {
						motionType = MotionType.STEP_RIGHT;
					} else {
						motionType = MotionType.STAY;
					}
					break;
				default:
					motionType = MotionType.STAY;
					break;
				}
				break;
			case JUMP:
				switch (controlType) {
				case DOWN:
					motionType = MotionType.FALL;
					break;
				default:
					if(motionAvaible(MotionType.JUMP)) {
						motionType = MotionType.JUMP;
					} else {
						motionType = MotionType.FALL;
					}
					break;
				}
				break;
			case FALL:
				switch (controlType) {
				default:
					if(motionAvaible(MotionType.FALL)) {
						motionType = MotionType.FALL;
					} else {
						motionType = MotionType.STAY;
					}
					break;
				}
				break;
			case STEP_RIGHT:
			case STEP_LEFT:
				motionType = MotionType.STAY;
				break;
			default:
				break;
			}
			controlType = UserControlType.IDLE;
			
			switch (motionType) {
			case JUMP:
				xSpeed = 0;
				ySpeed = -ANIMATION_JUMP_SPEED;
				break;
			case FALL:
				xSpeed = 0;
				ySpeed = ANIMATION_JUMP_SPEED;
				break;
			case STEP_LEFT:
				xSpeed = -ANIMATION_JUMP_SPEED;
				ySpeed = 0;
				break;
			case STEP_RIGHT:
				xSpeed = ANIMATION_JUMP_SPEED;
				ySpeed = 0;
				break;
			default:
				xSpeed = 0;
				ySpeed = 0;
				break;
			}
//			xSpeed = 0;
//			// fly up
//			if(ySpeed < 0) {
//				if(moveEvent != null && moveEvent.type == UserControlType.LEFT) {
//					ySpeed = ANIMATION_JUMP_SPEED;
//					if(heroX - JUMP_SPEED >= LEFT_BOUND) {
//						xSpeed = -ANIMATION_JUMP_SPEED;
//					}
//				} else if(moveEvent != null && moveEvent.type == UserControlType.RIGHT) {
//					ySpeed = ANIMATION_JUMP_SPEED;
//					if(heroX + JUMP_SPEED <= RIGHT_BOUND) {
//						xSpeed = ANIMATION_JUMP_SPEED;
//					}
//				} else if(heroY - JUMP_SPEED < TOP_BOUND) {
//					ySpeed = ANIMATION_JUMP_SPEED;
//				}
//			// fly down
//			} else if(ySpeed > 0) {
//				if(heroY + JUMP_SPEED > BOTTOM_BOUND) {
//					ySpeed = 0;
//				}
//			}
//			moveEvent = null;
		}
		heroY += ySpeed;
		heroX += xSpeed;
		canvas.drawColor(Color.WHITE);
		//canvas.drawBitmap(background, BACKGROUND_LEFT, BACKGROUND_TOP, null);
		level.onDraw(canvas);
		hero.onDraw(canvas, heroX - hero.getWidth() / 2, heroY - hero.getHeight() / 2);
		drawGrid(canvas);
	}
	
	private boolean motionAvaible(MotionType mt) {
		switch (mt) {
		case JUMP:
			if(heroY - JUMP_SPEED < TOP_BOUND) return false;
			return true;
		case STEP_LEFT:
		case JUMP_LEFT:
			if(heroX - JUMP_SPEED < LEFT_BOUND) return false;
			return true;
		case STEP_RIGHT:
		case JUMP_RIGHT:
			if(heroX + JUMP_SPEED > RIGHT_BOUND) return false;
			return true;
		case FALL:
			if(heroY +JUMP_SPEED > BOTTOM_BOUND) return false;
			return true;
		default:
			return false;
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
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			controlType = getMoveType(event);
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
