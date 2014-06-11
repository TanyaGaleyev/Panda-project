package org.ivan.simple.game.controls;

import android.view.MotionEvent;

import org.ivan.simple.UserControlType;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ivan on 28.05.2014.
 */
public class TwoHandControlProvider implements UserControlProvider {

    public static final int USE_TAP_DELAY = 100;

    public static interface FloatProvider {
        float get();
    }
    private final FloatProvider lrBound;
    private UserControlType pressedControl = UserControlType.IDLE;
    private UserControlType obtainedControl = UserControlType.IDLE;
    private UserControlType delayedControl = UserControlType.IDLE;
    private TimerTask useDelayedControl;
    private float[] startPressedY = new float[2];
    private int slideSenderID;

    public TwoHandControlProvider(FloatProvider lrBound) {
        this.lrBound = lrBound;
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
                receiveTap(event.getX());
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                if(event.getPointerCount() > 2) return true;
                startPressedY[pointerId] = event.getY(actionIndex);
                receiveTap(event.getX(actionIndex));
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                if(event.getPointerCount() > 2) return true;
                int anotherActionIndex = actionIndex == 0 ? 1 : 0;
                if((pressedControl != UserControlType.UP &&
                        pressedControl != UserControlType.DOWN) ||
                        slideSenderID == pointerId) {
                    if(event.getX(anotherActionIndex) > lrBound.get()) {
                        if(useDelayedControl != null) useDelayedControl.cancel();
                        pressedControl = UserControlType.RIGHT;
                    } else {
                        if(useDelayedControl != null) useDelayedControl.cancel();
                        pressedControl = UserControlType.LEFT;
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
                if(useDelayedControl != null &&
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
                    if(event.getY(ai) - startPressedY[pointerId] > 20) {
                        if(useDelayedControl != null) useDelayedControl.cancel();
                        pressedControl = UserControlType.DOWN;
                        obtainedControl = pressedControl;
                        startPressedY[pointerId] = event.getY(ai);
                        slideSenderID = pointerId;
                        break;
                    } else if(event.getY(ai) - startPressedY[pointerId] < -20) {
                        if(useDelayedControl != null) useDelayedControl.cancel();
                        pressedControl = UserControlType.UP;
                        obtainedControl = pressedControl;
                        startPressedY[pointerId] = event.getY(ai);
                        slideSenderID = pointerId;
                        break;
                    }
                }
                return true;
        }
        return false;
    }

    private void receiveTap(float tapX) {
        if(useDelayedControl != null) useDelayedControl.cancel();
        if(tapX > lrBound.get()) {
            delayedControl = UserControlType.RIGHT;
        } else {
            delayedControl = UserControlType.LEFT;
        }
        new Timer().schedule(useDelayedControl = new UseDelayedControl(), USE_TAP_DELAY);
    }

    private class UseDelayedControl extends TimerTask {

        @Override
        public void run() {
            obtainedControl = pressedControl = delayedControl;
        }
    }
}
