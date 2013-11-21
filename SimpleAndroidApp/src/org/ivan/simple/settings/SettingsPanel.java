package org.ivan.simple.settings;

import android.content.Context;
import android.widget.LinearLayout;

import org.ivan.simple.PandaBaseActivity;

/**
 * Created by ivan on 21.11.13.
 */
public class SettingsPanel extends LinearLayout {
    public SettingsPanel(PandaBaseActivity context, SettingsModel model) {
        super(context);
        setOrientation(VERTICAL);
        SettingsComponents components = new SettingsComponents(context, model);
        addView(components.getEffects());
        addView(components.getMusic());
    }
}
