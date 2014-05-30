package org.ivan.simple.settings;

import org.ivan.simple.game.controls.ControlChangeObserver;
import org.ivan.simple.game.controls.ControlsModel;
import org.ivan.simple.game.controls.ControlsType;

/**
 * Created by ivan on 21.11.13.
 */
public class SettingsModel {
    private boolean effects = true;
    private boolean music = true;
    private ControlsModel controlsModel = new ControlsModel();

    public SettingsModel() {}

    public void setEffectsEnabled(boolean enabled) {
        effects = enabled;
    }

    public void setMusicEnabled(boolean enabled) {
        music = enabled;
    }

    public boolean isEffectsEnabled() {
        return effects;
    }

    public boolean isMusicEnabled() {
        return music;
    }

    public ControlsType getControlsType() {
        return controlsModel.getControlsType();
    }

    public void setControlsType(ControlsType controlsType) {
        controlsModel.setControlsType(controlsType);
    }

    public void registerControlChangeObserver(ControlChangeObserver obs) {
        controlsModel.registerObserver(obs);
    }

    public void unregisterControlChangeObserver(ControlChangeObserver obs) {
        controlsModel.unregisterObserver(obs);
    }
}
