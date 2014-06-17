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
import org.ivan.simple.utils.BiMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private BiMap<Integer, ControlsType> positionToControlsTypeMap =
        new BiMap<Integer, ControlsType>();
    private ControlsSequence controlsSequence = new ControlsSequence(
            new ControlsName(ControlsType.ONE_FINGER, ONE_FINGER_CONTROL),
            new ControlsName(ControlsType.TWO_FINGERS, TWO_FINGERS_CONTROL)
    );
    private SettingsModel model;
    private Typeface regular;

    private static class ControlsSequence {
        List<ControlsName> sequence;

        ControlsSequence(ControlsName... controlsNames) {
            sequence = Arrays.asList(controlsNames);
        }

        List<ControlsType> types() {
            ArrayList<ControlsType> ret = new ArrayList<ControlsType>();
            for(ControlsName cn : sequence)
                ret.add(cn.type);
            return ret;
        }

        List<String> names() {
            ArrayList<String> ret = new ArrayList<String>();
            for(ControlsName cn : sequence)
                ret.add(cn.name);
            return ret;
        }
    }

    private static class ControlsName {
        ControlsType type;
        String name;

        ControlsName(ControlsType type, String name) {
            this.type = type;
            this.name = name;
        }
    }

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
        // TODO mapping between position, name and type of controls are inconsistent, need to rework
        FontArrayAdapter controlsAdapter = new FontArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                regular,
                Color.BLACK,
                controlsSequence.names());
        controlsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectControl.setAdapter(controlsAdapter);
        selectControl.setSelection(positionToControlsTypeMap.getReverse(model.getControlsType()));

        initListeners();
    }

    private void initControls() {
        List<ControlsType> types = controlsSequence.types();
        for (int i = 0; i < types.size(); i++)
            positionToControlsTypeMap.put(i, types.get(i));
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
        setText(component, caption, regular, Color.BLACK);
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
