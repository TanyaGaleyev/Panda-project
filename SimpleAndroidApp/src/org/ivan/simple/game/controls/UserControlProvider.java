package org.ivan.simple.game.controls;

import android.view.MotionEvent;

import org.ivan.simple.UserControlType;

/**
 * Created by ivan on 28.05.2014.
 */
public interface UserControlProvider {
    UserControl getUserControl();
    boolean obtainControl(MotionEvent event);
}
