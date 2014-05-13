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
        canvas.drawColor(bgColor);
        for(SubTexture sub : subTextures) {
            Bitmap bmp = sub.textureImage.bmp;
            float rPx = sub.textureImage.pivotX / bmp.getWidth();
            float rPy = sub.textureImage.pivotY / bmp.getHeight();
            for (SubTexture.InstanceMutation mutation : sub.settings) {
                Paint p = new Paint();
                p.setAlpha((int) (255 * mutation.alpha));
                p.setAntiAlias(true);
                Rect dstRect = getDstRect(mutation, canvas);
                Point dstPivot = getDstPivot(rPx, rPy, dstRect);
                canvas.save();
                canvas.rotate(mutation.rotate, dstPivot.x, dstPivot.y);
                canvas.drawBitmap(bmp, null, dstRect, p);
                canvas.restore();
            }
        }
    }

    private Rect getDstRect(SubTexture.InstanceMutation mutation, Canvas canvas) {
        float left = mutation.left * canvas.getWidth();
        float top = mutation.top * canvas.getHeight();
        float right = left + mutation.width * canvas.getWidth();
        float bottom = top + mutation.height * canvas.getWidth();
        return new Rect((int) left, (int) top, (int) right, (int) bottom);
    }

    private Point getDstPivot(float relativePx, float relativePy, Rect dstRect) {
        int x = dstRect.left + (int) (relativePx * dstRect.width());
        int y = dstRect.top + (int) (relativePy * dstRect.height());
        return new Point(x, y);
    }
}
