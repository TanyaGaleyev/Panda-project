package org.ivan.simple.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;

import org.ivan.simple.ImageProvider;
import org.ivan.simple.PandaApplication;

/**
 * Created by Ivan on 21.09.13.
 *
 */
public class ServiceButtons {
    private Point topLeftCorner;
    private Bitmap pause;
    private Bitmap restart;
    private Bitmap back;

    public ServiceButtons(Point topLeftCorner) {
        this.topLeftCorner = topLeftCorner;
        pause = imageProvider().getBitmapOrigSizeNoCache("menu/pause.png");
        restart = imageProvider().getBitmapOrigSizeNoCache("menu/restart.png");
        back = imageProvider().getBitmapOrigSizeNoCache("menu/back.png");
    }

    private ImageProvider imageProvider() {
        return PandaApplication.getPandaApplication().getImageProvider();
    }

    public ServiceButtons(int x, int y) {
        this(new Point(x, y));
    }

    public void draw(Canvas canvas) {
        drawSingleBitmap(pause, 0, 0, canvas);
        drawSingleBitmap(restart, 0, 40, canvas);
        drawSingleBitmap(back, 0, 80, canvas);
    }

    private void drawSingleBitmap(Bitmap bitmap, int x, int y, Canvas canvas) {
        int x1 = x + topLeftCorner.x;
        int y1 = y + topLeftCorner.y;
        canvas.drawBitmap(bitmap, x1, y1, null);
    }

    public enum Controls {
        PAUSE_RESUME,
        RESTART,
        BACK,
        NONE
    }

    public Controls getServiceAction(MotionEvent event) {
        if(event.getX() >= 0 && event.getX() < 40) {
            float y = event.getY();
            if(y >= 50 && y < 80) {
                return Controls.PAUSE_RESUME;
            } else if(y >= 90 && y < 120) {
                return Controls.RESTART;
            } else if(y >= 130 && y < 160) {
                return Controls.BACK;
            }
        }
        return Controls.NONE;
    }
}
