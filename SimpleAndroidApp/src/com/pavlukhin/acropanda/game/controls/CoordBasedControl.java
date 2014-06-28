package com.pavlukhin.acropanda.game.controls;

import com.pavlukhin.acropanda.UserControlType;

/**
 * Created by ivan on 28.05.2014.
 */
public class CoordBasedControl {
    public static UserControlType get(float clickX, float clickY, int pivotX, int pivotY) {
        float dX = pivotX - clickX; // positive dx move left
        float dY = pivotY - clickY; // positive dy move up
        // use for get control max by absolute value dx, dy
        // max(|dx|, |dy|)
        if(Math.abs(dY) > Math.abs(dX)) {
            // up or down
            if(dY < 0) {
                return UserControlType.DOWN;
            } else {
                return UserControlType.UP;
            }
        } else {
            // left or right
            if(dX < 0) {
                return UserControlType.RIGHT;
            } else {
                return UserControlType.LEFT;
            }
        }
    }
}
