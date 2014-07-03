package com.pavlukhin.acropanda.settings;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.pavlukhin.acropanda.PandaBaseActivity;
import com.pavlukhin.acropanda.R;

/**
 * Created by ivan on 21.11.13.
 */
public class SettingsPanel extends LinearLayout {

    public static final String CONTROLS_CAPTION = "Controls:";

    public SettingsPanel(PandaBaseActivity context, SettingsModel model) {
        super(context);
//        setBackgroundResource(R.drawable.settings_border);
        setOrientation(VERTICAL);
        SettingsComponents components = new SettingsComponents(context, model);
        addView(components.getEffects());
        addView(components.getMusic());
        TextView chooseControlCaption = new TextView(context);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int m = (int) (context.app().displayHeight * 0.02f);
        lp.setMargins(m, m, m, m);
        chooseControlCaption.setLayoutParams(lp);
        components.setTextDefault(chooseControlCaption, CONTROLS_CAPTION);
        addView(chooseControlCaption);
        addView(components.getSelectControl());
    }
}
