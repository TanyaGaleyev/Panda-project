package org.ivan.simple.hero;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	private final Bitmap bmp;
	
	private final int BMP_ROWS;

	private final int BMP_COLS;
	
	protected int currentFrame = 0;
	
	protected int currentSet = 0;
	
	private final int singleWidth;
	
	private final int singleHeight;
	
	protected boolean animating = false;
	
	public boolean playOnce = false;
	
	public Sprite(Bitmap bmp, int rows, int cols) {
		this.bmp = bmp;
		BMP_ROWS = rows;
		BMP_COLS = cols;
		singleWidth = bmp.getWidth() / BMP_COLS;
        singleHeight = bmp.getHeight() / BMP_ROWS;
	}

	public void onDraw(Canvas canvas, int x, int y) {
		int srcX = currentFrame * singleWidth;
        int srcY = currentSet * singleHeight;
        Rect src = new Rect(srcX, srcY, srcX + singleWidth, srcY + singleHeight);
        Rect dst = new Rect(x, y, x + singleWidth, y + singleHeight);
        canvas.drawBitmap(bmp, src, dst, null);
        if(animating) {
        	currentFrame = (currentFrame + 1) % BMP_COLS;
        }
        if(currentFrame == 0 && playOnce) {
        	animating = false;
        }
	}
	public void gotoAndStop(int fr){	
		currentFrame=fr;
	}
	public int getWidth() {
		return singleWidth;
	}
	
	public int getHeight() {
		return singleHeight;
	}
	
	public void setAnimating(boolean animating) {
		this.animating = animating;
	}
	
	public int getFrame() {
		return currentFrame;
	}
	
	public boolean changeSet(int i ) {
		if(i < 0 || i >= BMP_ROWS) return false;
		currentFrame = 0;
		currentSet = i;
		return true;
	}
}
