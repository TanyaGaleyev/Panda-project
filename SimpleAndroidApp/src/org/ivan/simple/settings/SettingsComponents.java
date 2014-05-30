package org.ivan.simple.settings;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.ivan.simple.PandaBaseActivity;
import org.ivan.simple.game.controls.ControlsType;

import java.util.EnumMap;
import java.util.LinkedHashMap;

/**
 * Created by ivan on 21.11.13.
 */
public class SettingsComponents {
    public static final String EFFECTS = "effects";
    public static final String MUSIC = "music";
    public static final String SIMPLE_CONTROL = "Simple";
    public static final String ONE_FINGER_CONTROL = "Lazy";
    public static final String TWO_FINGERS_CONTROL = "PSP";
    private final PandaBaseActivity context;

    private CheckBox effects;
    private CheckBox music;
    private Spinner selectControl;
    private LinkedHashMap<Integer, ControlsType> positionToControlsTypeMap =
            new LinkedHashMap<Integer, ControlsType>();
    private EnumMap<ControlsType, Integer> controlsTypeToPositionMap =
            new EnumMap<ControlsType, Integer>(ControlsType.class);
    private SettingsModel model;
    private Typeface regular;

    public SettingsComponents(PandaBaseActivity context, SettingsModel model) {
        this.model = model;
        this.context = context;
        regular = context.app().getFontProvider().regular();
        setText(effects = new CheckBox(context), EFFECTS, regular, Color.BLACK);
        effects.setChecked(model.isEffectsEnabled());
        setText(music = new CheckBox(context), MUSIC, regular, Color.BLACK);
        music.setChecked(model.isMusicEnabled());
        selectControl = new Spinner(context);
        initControls();
        // TODO mapping between positon, name and type of controls are inconsistent, need to rework
        FontArrayAdapter controlsAdapter = new FontArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                regular,
                Color.BLACK,
                SIMPLE_CONTROL, ONE_FINGER_CONTROL, TWO_FINGERS_CONTROL);
        controlsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectControl.setAdapter(controlsAdapter);
        selectControl.setSelection(controlsTypeToPositionMap.get(model.getControlsType()));

        initListeners();
    }

    private void initControls() {
        putControlsEntry(ControlsType.SIMPLE, 0);
        putControlsEntry(ControlsType.ONE_FINGER, 1);
        putControlsEntry(ControlsType.TWO_FINGERS, 2);
    }

    private void putControlsEntry(ControlsType type, int position) {
        positionToControlsTypeMap.put(position, type);
        controlsTypeToPositionMap.put(type, position);
    }

    private void initListeners() {
        effects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                model.setEffectsEnabled(b);
            }
        });
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                model.setMusicEnabled(b);
            }
        });
        selectControl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model.setControlsType(positionToControlsTypeMap.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void setText(TextView component, String caption, Typeface font, int color) {
        component.setText(caption);
        component.setTypeface(font);
        component.setTextColor(color);
    }

    protected void setTextDefault(TextView component, String caption) {
        component.setText(caption);
        component.setTypeface(regular);
        component.setTextColor(Color.BLACK);
    }

    public CheckBox getEffects() {
        return effects;
    }

    public CheckBox getMusic() {
        return music;
    }

    public Spinner getSelectControl() {
        return selectControl;
    }

    public SettingsModel getModel() {
        return model;
    }
}
