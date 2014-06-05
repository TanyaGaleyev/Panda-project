package org.ivan.simple.game.controls;

import org.ivan.simple.UserControlType;

/**
 * Created by ivan on 05.06.2014.
 */
public interface UserControl {
    UserControlType getControlType();
    UserControlType getRememberControlType();
}
