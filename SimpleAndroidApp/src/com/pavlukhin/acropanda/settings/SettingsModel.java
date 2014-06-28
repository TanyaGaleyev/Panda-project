package com.pavlukhin.acropanda.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.pavlukhin.acropanda.PandaApplication;
import com.pavlukhin.acropanda.game.controls.ControlChangeObserver;
import com.pavlukhin.acropanda.game.controls.ControlsModel;
import com.pavlukhin.acropanda.game.controls.ControlsType;

/**
 * Created by ivan on 21.11.13.
 */
public class SettingsModel {
    public static final String SETTINGS = "settings";
    public static final String EFFECTS = "effects";
    public static final String MUSIC = "music";
    public static final String CONTROLS = "controls";
    private final PandaApplication app;
    private boolean effects;
    private boolean music;
    private ControlsModel controlsModel = new ControlsModel();
    private SharedPreferences settingsPreferences;

    public SettingsModel(PandaApplication app) {
        this.app = app;
        settingsPreferences = app.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        setEffectsEnabled(settingsPreferences.getBoolean(EFFECTS, true));
        setMusicEnabled(settingsPreferences.getBoolean(MUSIC, true));
        int controlsId = settingsPreferences.getInt(CONTROLS, ControlsType.ONE_FINGER.ordinal());
        setControlsType(ControlsType.values()[controlsId]);
    }

    public void setEffectsEnabled(boolean enabled) {
        effects = enabled;
        app.setSound(effects);
        settingsPreferences.edit().putBoolean(EFFECTS, effects).commit();
    }

    public void setMusicEnabled(boolean enabled) {
        music = enabled;
        app.getMusicManger().setMusicEnabled(enabled);
        settingsPreferences.edit().putBoolean(MUSIC, music).commit();
    }

    public boolean isEffectsEnabled() {
        return effects;
    }

    public boolean isMusicEnabled() {
        return music;
    }

    public void setControlsType(ControlsType controlsType) {
        controlsModel.setControlsType(controlsType);
        settingsPreferences.edit().putInt(CONTROLS, controlsType.ordinal()).commit();
    }

    public ControlsType getControlsType() {
        return controlsModel.getControlsType();
    }

    public void registerControlChangeObserver(ControlChangeObserver obs) {
        controlsModel.registerObserver(obs);
    }

    public void unregisterControlChangeObserver(ControlChangeObserver obs) {
        controlsModel.unregisterObserver(obs);
    }
}
