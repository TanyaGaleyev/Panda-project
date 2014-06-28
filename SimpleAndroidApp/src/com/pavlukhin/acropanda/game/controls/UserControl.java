package com.pavlukhin.acropanda.game.controls;

import com.pavlukhin.acropanda.UserControlType;

/**
 * Created by ivan on 05.06.2014.
 */
public interface UserControl {
    UserControlType getControlType();
    UserControlType getRememberControlType();
}
