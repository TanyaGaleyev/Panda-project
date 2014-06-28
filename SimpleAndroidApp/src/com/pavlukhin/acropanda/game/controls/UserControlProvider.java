package com.pavlukhin.acropanda.game.controls;

import android.view.MotionEvent;

/**
 * Created by ivan on 28.05.2014.
 */
public interface UserControlProvider {
    UserControl getUserControl();
    boolean obtainControl(MotionEvent event);
}
