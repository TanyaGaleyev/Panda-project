package org.ivan.simple.game.controls;

import android.view.MotionEvent;

import org.ivan.simple.UserControlType;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ivan on 28.05.2014.
 */
public class SlideOnly implements UserControlProvider {

    public static final int SLIDE_MIN_STEP = 30;
    private final PivotPoint pivot;
    private UserControlType pressedControl = UserControlType.IDLE;
    private UserControlType obtainedControl = UserControlType.IDLE;
    private float[] startPressedY = new float[2];
    private float[] startPressedX = new float[2];

    public SlideOnly(PivotPoint pivot) {
        this.pivot = pivot;
    }

    @Override
    public UserControlType getUserControl() {
        UserControlType controlType;
        if(obtainedControl == UserControlType.IDLE) {
            controlType = pressedControl;
        } else {
            controlType = obtainedControl;
            obtainedControl = UserControlType.IDLE;
        }
        return controlType;
    }

    @Override
    public boolean obtainControl(MotionEvent event) {
        int actionMask = event.getActionMasked();
        int actionIndex = event.getActionIndex();
        int pointerId = event.getPointerId(actionIndex);
        switch(actionMask) {
            case MotionEvent.ACTION_DOWN:
                startPressedY[0] = event.getY();
                startPressedX[0] = event.getX();
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                if(event.getPointerCount() > 2) return true;
                startPressedY[pointerId] = event.getY(actionIndex);
                startPressedX[pointerId] = event.getX(actionIndex);
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                if(event.getPointerCount() > 2) return true;
                return true;
            case MotionEvent.ACTION_UP:
                pressedControl = UserControlType.IDLE;
                return true;
            case MotionEvent.ACTION_MOVE:
                if(event.getPointerCount() > 2) return true;
                for(int ai = 0; ai < event.getPointerCount(); ai++) {
                    pointerId = event.getPointerId(ai);
                    if(pointerId > 1) continue;
                    float ex = event.getX(ai);
                    float ey = event.getY(ai);
                    if(event.getX(ai) - startPressedX[pointerId] > SLIDE_MIN_STEP) {
                        receiveSlideControl(UserControlType.RIGHT, pointerId, ex, ey);
                        break;
                    } else if(event.getX(ai) - startPressedX[pointerId] < -SLIDE_MIN_STEP) {
                        receiveSlideControl(UserControlType.LEFT, pointerId, ex, ey);
                        break;
                    } else if(event.getY(ai) - startPressedY[pointerId] > SLIDE_MIN_STEP) {
                        receiveSlideControl(UserControlType.DOWN, pointerId, ex, ey);
                        break;
                    } else if(event.getY(ai) - startPressedY[pointerId] < -SLIDE_MIN_STEP) {
                        receiveSlideControl(UserControlType.UP, pointerId, ex, ey);
                        break;
                    }
                }
                return true;
        }
        return false;
    }

    private void receiveSlideControl(UserControlType control, int pointerId, float x, float y) {
        pressedControl = control;
        obtainedControl = control;
        startPressedY[pointerId] = y;
        startPressedX[pointerId] = x;
    }
}
