package org.ivan.simple;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class LevelChooseView extends SurfaceView {
	
	private static final int GRID_STEP = 128;
	private int levelX = 0;
	private int levelY = 0;
	private int[][] levels = {{1,1},{1,1}};
	
	private SurfaceHolder holder;
	
	private Bitmap border;

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
			}
			
			public void surfaceCreated(SurfaceHolder holder) {
				border = BitmapFactory.decodeResource(getResources(), R.drawable.border);
				new Redrawer().start();
			}
			
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
			}
		});

	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		for(int i = 0; i < levels.length; i++) {
			for(int j = 0; j < levels[i].length; j++) {
				canvas.drawBitmap(border, getScreenX(j), getScreenY(i), null);
			}
		}
	}
	
	private int getScreenX(int col) {
		return GRID_STEP + col * GRID_STEP;
	}
	
	private int getScreenY(int row) {
		return GRID_STEP + row * GRID_STEP;
	}
	
	private class Redrawer extends Thread {
		@Override
		public void run() {
			while(true) {
				Canvas c = getHolder().lockCanvas();
				onDraw(c);
				getHolder().unlockCanvasAndPost(c);
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
