package org.ivan.simple.bitmaputils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.List;

/**
 * Created by ivan on 05.05.2014.
 */
public class TextureAtlasBackground implements PandaBackground {
    private final int bgColor;
    private final List<SubTexture> subTextures;

    public TextureAtlasBackground(int bgColor, List<SubTexture> subTextures) {
        this.bgColor = bgColor;
        this.subTextures = subTextures;
    }

    @Override
    public void draw(Canvas canvas) {
        int cw = canvas.getWidth();
        int ch = canvas.getHeight();
        Paint p = new Paint();
        canvas.drawColor(bgColor);
        for(SubTexture sub : subTextures) {
            Bitmap bmp = sub.textureImage.bmp;
            float rPx = sub.textureImage.pivotX / bmp.getWidth();
            float rPy = sub.textureImage.pivotY / bmp.getHeight();
            for (SubTexture.InstanceMutation mutation : sub.settings) {
                p.setAlpha((int) (255 * mutation.alpha));
                p.setAntiAlias(true);
                Rect dstRect = getDstRect(mutation, cw, ch);
                Point dstPivot = getDstPivot(rPx, rPy, dstRect);
                canvas.save();
                canvas.rotate(mutation.rotate, dstPivot.x, dstPivot.y);
                canvas.drawBitmap(bmp, null, dstRect, p);
                canvas.restore();
            }
        }
    }

    private Rect getDstRect(SubTexture.InstanceMutation mutation, int cw, int ch) {
        float left = mutation.left * cw;
        float top = mutation.top * ch;
        float right = left + mutation.width * cw;
        float bottom = top + mutation.height * cw;
        return new Rect((int) left, (int) top, (int) right, (int) bottom);
    }

    private Point getDstPivot(float relativePx, float relativePy, Rect dstRect) {
        int x = dstRect.left + (int) (relativePx * dstRect.width());
        int y = dstRect.top + (int) (relativePy * dstRect.height());
        return new Point(x, y);
    }
}
