package com.pavlukhin.acropanda.game.controls;

import android.view.MotionEvent;

import com.pavlukhin.acropanda.UserControlType;

/**
 * Created by ivan on 28.05.2014.
 */
public class SimpleControlProvider implements UserControlProvider {
    private UserControlType pressedControl = UserControlType.IDLE;
    private UserControlType obtainedControl = UserControlType.IDLE;
    private final PivotPoint pivot;

    public SimpleControlProvider(PivotPoint pivot) {
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
        if(event.getAction() == MotionEvent.ACTION_DOWN ||
                event.getAction() == MotionEvent.ACTION_MOVE) {
            pressedControl = CoordBasedControl.get(
                    event.getX(), event.getY(), pivot.getX(), pivot.getY());
            obtainedControl = pressedControl;
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            pressedControl = UserControlType.IDLE;
            return true;
        }
        return false;
    }
}
