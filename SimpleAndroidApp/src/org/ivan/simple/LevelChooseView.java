package org.ivan.simple;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class LevelChooseView extends SurfaceView {
	
	private static final int GRID_STEP = 128;
	private static final int LEFT_BOUND = GRID_STEP / 4;
	private static final int TOP_BOUND = GRID_STEP / 4;
	private static final int MARKER_SPEED = GRID_STEP / 8;
	private int levelX = 0;
	private int levelY = 0;
	private int[][] levels = {{1,1},{1,1}};
	
	private SurfaceHolder holder;
	
	private Bitmap border;
	
	private Bitmap background;
	
	private Bitmap marker;
	private int markerX = LEFT_BOUND + GRID_STEP / 2;
	private int markerY = TOP_BOUND + GRID_STEP / 2;
	
	private Redrawer redrawer;
	
	private boolean chooseReady = true;
	
	private UserControlType performingAction = UserControlType.IDLE;
	private UserControlType chooseAcion = UserControlType.IDLE;

	public LevelChooseView(Context context) {
		super(context);
		init();
	}
	
	public LevelChooseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public LevelChooseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private final void init() {
		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {
			
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
			}
			
			public void surfaceCreated(SurfaceHolder holder) {
				border = ImageProvider.getBitmap(R.drawable.border);
				background = ImageProvider.getBitmap(R.drawable.background_2);
				marker = ImageProvider.getBitmap(R.drawable.single_panda);
				redrawer = new Redrawer();
				redrawer.start();
			}
			
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
			}
		});

	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		switch(performingAction) {
		case UP:
			markerY -= MARKER_SPEED;
			break;
		case DOWN:
			markerY += MARKER_SPEED;
			break;
		case LEFT:
			markerX -= MARKER_SPEED;
			break;
		case RIGHT:
			markerX += MARKER_SPEED;
			break;
		default:
			break;
		}
		canvas.drawBitmap(background, 0, 0, null);
		for(int i = 0; i < levels.length; i++) {
			for(int j = 0; j < levels[i].length; j++) {
				canvas.drawBitmap(border, getScreenX(j), getScreenY(i), null);
			}
		}
		canvas.drawBitmap(marker, markerX - marker.getWidth() / 2, markerY - marker.getHeight() / 2, null);
	}
	
	private int getScreenX(int col) {
		return LEFT_BOUND + col * GRID_STEP + GRID_STEP / 2 - border.getWidth() / 2;
	}
	
	private int getScreenY(int row) {
		return TOP_BOUND + row * GRID_STEP + GRID_STEP / 2 - border.getHeight() / 2;
	}
	
	public synchronized int getLevelId(MotionEvent event) {
		if(event.getAction() != MotionEvent.ACTION_DOWN) return 0;
		if(!chooseReady) return 0;
		chooseReady = false;
		UserControlType tempAction = getMoveType(event);
		if(getScreenX(levelX) < event.getX() &&
				event.getX() < getScreenX(levelX + 1) &&
				getScreenY(levelY) < event.getY() && 
				event.getY() < getScreenY(levelY + 1)) {
			return 1 + levelY * levels.length + levelX;
		}
		switch(tempAction) {
		case UP:
			if(levelY == 0) tempAction = UserControlType.IDLE;
			else levelY -= 1;
			break;
		case DOWN:
			if(levelY == levels.length - 1) tempAction = UserControlType.IDLE; 
			else levelY += 1;
			break;
		case LEFT:
			if(levelX == 0) tempAction = UserControlType.IDLE;
			else levelX -= 1;
			break;
		case RIGHT:
			if(levelX == levels[0].length - 1) tempAction = UserControlType.IDLE;
			else levelX += 1;
			break;
		default:
			break;
		}
		chooseAcion = tempAction;
		return 0;
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
	
	private class Redrawer extends Thread {
		boolean running = true;
		@Override
		public void run() {
			while(running) {
				if((markerX - GRID_STEP / 2 - LEFT_BOUND) % GRID_STEP == 0 &&
						(markerY - GRID_STEP / 2 - TOP_BOUND) % GRID_STEP == 0) {
					chooseReady = true;
					performingAction = chooseAcion;
					chooseAcion = UserControlType.IDLE;
				}
				Canvas c = getHolder().lockCanvas();
				onDraw(c);
				getHolder().unlockCanvasAndPost(c);
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
