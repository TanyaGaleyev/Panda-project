package org.ivan.simple.settings;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.ivan.simple.PandaBaseActivity;

/**
 * Created by ivan on 21.11.13.
 */
public class SettingsComponents {
    public static final String EFFECTS = "effects";
    public static final String MUSIC = "music";
    private final PandaBaseActivity context;

    private CheckBox effects;
    private CheckBox music;
    private SettingsModel model;

    public SettingsComponents(PandaBaseActivity context, SettingsModel model) {
        this.model = model;
        this.context = context;
        Typeface regular = context.app().getFontProvider().regular();
        effects = new CheckBox(context);
        effects.setText(EFFECTS);
        effects.setTypeface(regular);
        effects.setChecked(model.isEffectsEnabled());
        music = new CheckBox(context);
        music.setText(MUSIC);
        music.setTypeface(regular);
        music.setChecked(model.isMusicEnabled());

        initListeners();
    }

    private void initListeners() {
        effects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                model.setEffectsEnabled(b);
                context.app().setSound(b);
            }
        });
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                model.setMusicEnabled(b);
            }
        });
    }

    public CheckBox getEffects() {
        return effects;
    }

    public CheckBox getMusic() {
        return music;
    }

    public SettingsModel getModel() {
        return model;
    }
}
