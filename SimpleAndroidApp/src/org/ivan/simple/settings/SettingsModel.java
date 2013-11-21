package org.ivan.simple.settings;

import java.util.HashMap;

/**
 * Created by ivan on 21.11.13.
 */
public class SettingsModel {
    private boolean effects = true;
    private boolean music = true;

    public SettingsModel() {
    }

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


}
