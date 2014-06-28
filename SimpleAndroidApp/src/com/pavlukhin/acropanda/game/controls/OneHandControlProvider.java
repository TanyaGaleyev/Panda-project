package com.pavlukhin.acropanda.game.controls;

import android.view.MotionEvent;

import com.pavlukhin.acropanda.UserControlType;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ivan on 28.05.2014.
 */
public class OneHandControlProvider implements UserControlProvider {

    private final PivotPoint pivot;
    private UserControlType delayedControl = UserControlType.IDLE;
    private UserControlType pressedControl = UserControlType.IDLE;
    private UserControlType obtainedControl = UserControlType.IDLE;
    private TimerTask useDelayedControl;
    private float[] startPressedY = new float[2];
    private float[] startPressedX = new float[2];

    public OneHandControlProvider(PivotPoint pivot) {
        this.pivot = pivot;
    }

    @Override
    public UserControl getUserControl() {
        UserControl controlType;
        if(obtainedControl == UserControlType.IDLE) {
            controlType = new PressedControl(pressedControl);
        } else {
            controlType = new ObtainedControl(obtainedControl);
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
//			if(event.getX() > hero.heroX) {
//				delayedControl = UserControlType.RIGHT;
//			} else {
//				delayedControl = UserControlType.LEFT;
//			}
                delayedControl = CoordBasedControl.get(
                        event.getX(), event.getY(), pivot.getX(), pivot.getY());
                useDelayedControl = new TimerTask() {
                    @Override
                    public void run() {
                        pressedControl = delayedControl;
                        obtainedControl = delayedControl;
                    }
                };
                new Timer().schedule(useDelayedControl, 100);
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
			/*if(pressedControl != UserControlType.IDLE) {
				model.setControlType(pressedControl);
			} else */if(useDelayedControl != null &&
                    useDelayedControl.cancel() &&
                    obtainedControl == UserControlType.IDLE) {
                obtainedControl = delayedControl;
            }
                pressedControl = UserControlType.IDLE;
                return true;
            case MotionEvent.ACTION_MOVE:
                if(event.getPointerCount() > 2) return true;
                for(int ai = 0; ai < event.getPointerCount(); ai++) {
                    pointerId = event.getPointerId(ai);
                    if(pointerId > 1) continue;
                    float ex = event.getX(ai);
                    float ey = event.getY(ai);
                    if(event.getX(ai) - startPressedX[pointerId] > 20) {
                        receiveSlideControl(UserControlType.RIGHT, pointerId, ex, ey);
                        break;
                    } else if(event.getX(ai) - startPressedX[pointerId] < -20) {
                        receiveSlideControl(UserControlType.LEFT, pointerId, ex, ey);
                        break;
                    } else if(event.getY(ai) - startPressedY[pointerId] > 20) {
                        receiveSlideControl(UserControlType.DOWN, pointerId, ex, ey);
                        break;
                    } else if(event.getY(ai) - startPressedY[pointerId] < -20) {
                        receiveSlideControl(UserControlType.UP, pointerId, ex, ey);
                        break;
                    }
                }
                return true;
        }
        return false;
    }

    private void receiveSlideControl(UserControlType control, int pointerId, float x, float y) {
        if(useDelayedControl != null) {
            useDelayedControl.cancel();
        }
        pressedControl = control;
        obtainedControl = control;
        startPressedY[pointerId] = y;
        startPressedX[pointerId] = x;
    }
}
