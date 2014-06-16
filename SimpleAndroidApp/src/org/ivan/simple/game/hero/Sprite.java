package org.ivan.simple.game.hero;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.PandaApplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	protected final Bitmap bmp;
	
	protected int bmpRows;

	protected int bmpCols;

	protected int currentFrame = 0;
	
	protected int currentSet = 0;
	
	protected final int singleWidth;
	
	protected final int singleHeight;
	
	private boolean animating = false;
	
	private boolean playOnce = false;
	
	private boolean switchSet = false;
	
	private int delay = 0;
	
	protected Sprite() {
        bmp = null;
        bmpRows = 0;
        bmpCols = 0;
        singleHeight = 0;
        singleWidth = 0;
    }

    protected Sprite(Bitmap bmp, int rows, int cols) {
		this.bmp = bmp;
		bmpRows = rows;
		bmpCols = cols;
		singleWidth = bmp.getWidth() / bmpCols;
        singleHeight = bmp.getHeight() / bmpRows;
	}

    protected Sprite(Bitmap bmp, int rows, int cols, int set) {
		this(bmp, rows, cols);
		changeSet(set);
	}

    public static Sprite createStrict(String bmpid, int rows, int cols) {
        return new Sprite(imageProvider().getBitmapStrictCache(bmpid, rows, cols), rows, cols);
    }

    public static Sprite createLru(String bmpid, int rows, int cols) {
        return new Sprite(imageProvider().getBitmapLruCache(bmpid, rows, cols), rows, cols);
    }

    protected static ImageProvider imageProvider() {
        return PandaApplication.getPandaApplication().getImageProvider();
    }

	/**
	 * Draw current frame by specified center coordinates
	 * @param canvas
	 * @param x x coordinate of to draw sprite
	 * @param y y coordinate of to draw sprite
	 * @param update should we go to next frame after draw?
	 */
	public void onDraw(Canvas canvas, int x, int y, boolean update) {
		int srcX = getXOffset();
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
			animating = delay == 0 || animating;
		}
        if(animating) {
        	currentFrame = (currentFrame + 1) % bmpCols;
        	if(currentFrame == 0) {
            	if(switchSet) {
            		currentSet = (currentSet + 1) % bmpRows;
            	}
            	if(playOnce) {
            		animating = false;
            	}
            }
        }
	}

    protected int getXOffset() {
        return currentFrame * singleWidth;
    }

	public void goToFrame(int fr){
		if(fr >= bmpCols) return;
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
		if(i < 0 || i >= bmpRows) return false;
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
		this.animating = !(delay > 0);
		this.playOnce = true;
		this.delay = delay;
	}
	
	protected void setPlayOnce(boolean playOnce) {
		this.playOnce = playOnce;
	}
	
	public boolean isAnimatingOrDelayed() {
		return animating || delay != 0;
	}

    public Sprite inverse() {
        return new Sprite(bmp, bmpRows, bmpCols, currentSet) {
            @Override
            protected int getXOffset() {
                return (bmpCols - 1 - currentFrame) * singleWidth;
            }
        };
    }

    public Sprite first() {
        return new Sprite(bmp, bmpRows, bmpCols, currentSet) {
            @Override
            protected int getXOffset() {
                return 0;
            }
        };
    }

    public Sprite last() {
        return new Sprite(bmp, bmpRows, bmpCols, currentSet) {
            @Override
            protected int getXOffset() {
                return (bmpCols - 1) * singleWidth;
            }
        };
    }

    public static enum Flag {
        INVERT,
        FIRST,
        LAST,
    }

}
