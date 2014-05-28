package org.ivan.simple.game.controls;

import android.view.MotionEvent;

import org.ivan.simple.UserControlType;

/**
 * Created by ivan on 28.05.2014.
 */
public class TwoHandControlProvider implements UserControlProvider {
    public static interface FloatProvider {
        float get();
    }
    private final FloatProvider lrBound;
    private UserControlType pressedControl = UserControlType.IDLE;
    private UserControlType obtainedControl = UserControlType.IDLE;
    private float[] startPressedY = new float[2];
    private int slideSenderID;

    public TwoHandControlProvider(FloatProvider lrBound) {
        this.lrBound = lrBound;
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
                if(event.getX() > lrBound.get()) {
                    pressedControl = UserControlType.RIGHT;
                    obtainedControl = pressedControl;
                } else {
                    pressedControl = UserControlType.LEFT;
                    obtainedControl = pressedControl;
                }
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                if(event.getPointerCount() > 2) return true;
                startPressedY[pointerId] = event.getY(actionIndex);
                if(event.getX(actionIndex) > lrBound.get()) {
                    pressedControl = UserControlType.RIGHT;
                    obtainedControl = pressedControl;
                } else {
                    pressedControl = UserControlType.LEFT;
                    obtainedControl = pressedControl;
                }
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                if(event.getPointerCount() > 2) return true;
                int anotherPointer = event.getActionIndex() == 0 ? 1 : 0;
                if((pressedControl != UserControlType.UP &&
                        pressedControl !=UserControlType.DOWN) ||
                        slideSenderID == pointerId) {
                    if(event.getX(anotherPointer) > lrBound.get()) {
                        pressedControl = UserControlType.RIGHT;
                    } else {
                        pressedControl = UserControlType.LEFT;
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
                pressedControl = UserControlType.IDLE;
                return true;
            case MotionEvent.ACTION_MOVE:
                if(event.getPointerCount() > 2) return true;
                for(int ai = 0; ai < event.getPointerCount(); ai++) {
                    pointerId = event.getPointerId(ai);
                    if(event.getY(ai) - startPressedY[pointerId] > 20) {
                        pressedControl = UserControlType.DOWN;
                        obtainedControl = pressedControl;
                        startPressedY[pointerId] = event.getY(ai);
                        slideSenderID = pointerId;
                        break;
                    } else if(event.getY(ai) - startPressedY[pointerId] < -20) {
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
}
