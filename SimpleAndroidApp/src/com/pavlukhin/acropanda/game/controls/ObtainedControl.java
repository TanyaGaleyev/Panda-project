package com.pavlukhin.acropanda.game.controls;

import com.pavlukhin.acropanda.UserControlType;

/**
 * Created by ivan on 05.06.2014.
 */
public class ObtainedControl implements UserControl {
    private final UserControlType controlType;

    public ObtainedControl(UserControlType controlType) {
        this.controlType = controlType;
    }

    @Override
    public UserControlType getControlType() {
        return controlType;
    }

    @Override
    public UserControlType getRememberControlType() {
        return controlType;
    }
}
