package org.ivan.simple.game.hero;

import java.io.IOException;

import org.ivan.simple.ImageProvider;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	protected final Bitmap bmp;
	
	protected final int BMP_ROWS;

	protected final int BMP_COLS;
	
	protected int currentFrame = 0;
	
	protected int currentSet = 0;
	
	protected final int singleWidth;
	
	protected final int singleHeight;
	
	private boolean animating = false;
	
	private boolean playOnce = false;
	
	private boolean switchSet = false;
	
	private int delay = 0;
	
	private Sprite(Bitmap bmp, int rows, int cols) {
		this.bmp = bmp;
		BMP_ROWS = rows;
		BMP_COLS = cols;
		singleWidth = bmp.getWidth() / BMP_COLS;
        singleHeight = bmp.getHeight() / BMP_ROWS;
	}
	
	private Sprite(Bitmap bmp, int rows, int cols, int set) {
		this(bmp, rows, cols);
		changeSet(set);
	}
	
	public Sprite(String bmpid, int rows, int cols) {
		this(ImageProvider.getBitmap(bmpid, rows, cols), rows, cols);
	}
	
	public Sprite(String bmpid, int rows, int cols, int set) {
		this(bmpid, rows, cols);
		changeSet(set);
	}

	/**
	 * Draw current frame by specified center coordinates
	 * @param canvas
	 * @param x x coordinate of to draw sprite
	 * @param y y coordinate of to draw sprite
	 * @param update should we go to next frame after draw?
	 */
	public void onDraw(Canvas canvas, int x, int y, boolean update) {
		int srcX = currentFrame * singleWidth;
        int srcY = currentSet * singleHeight;
        int cornerX = x - singleWidth / 2;
        int cornerY = y - singleHeight / 2;
        Rect src = new Rect(srcX, srcY, srcX + singleWidth, srcY + singleHeight);
        Rect dst = new Rect(cornerX, cornerY, cornerX + singleWidth, cornerY + singleHeight);
        canvas.drawBitmap(bmp, src, dst, null);
        if(update) {
        	update();
        }
	}
	
	protected void update() {
		if(delay > 0) {
			delay--;
			animating = delay == 0 ? true : animating;
		}
        if(animating) {
        	currentFrame = (currentFrame + 1) % BMP_COLS;
        	if(currentFrame == 0) {
            	if(switchSet) {
            		currentSet = (currentSet + 1) % BMP_ROWS;
            	}
            	if(playOnce) {
            		animating = false;
            	}
            }
        }
	}
	
	public void goToFrame(int fr){
		if(fr >= BMP_COLS) return;
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
	
	public int getSet() {
		return currentSet;
	}
	
	public boolean changeSet(int i) {
		if(i < 0 || i >= BMP_ROWS) return false;
		currentFrame = 0;
		currentSet = i;
		return true;
	}
	
	public void playOnce() {
		playOnce(0, false);
	}
	
	public void playOnce(boolean switchSet) {
		playOnce(0, switchSet);
	}
	
	public void playOnce(int delay) {
		playOnce(delay, false);
	}
	
	public void playOnce(int delay, boolean switchSet) {
		this.switchSet = switchSet;
		if(delay < 0) delay = 0;
		this.animating = delay > 0 ? false : true; 
		this.playOnce = true;
		this.delay = delay;
	}
	
	protected void setPlayOnce(boolean playOnce) {
		this.playOnce = playOnce;
	}
	
	public boolean isAnimatingOrDelayed() {
		return animating || delay != 0;
	}
	
}
