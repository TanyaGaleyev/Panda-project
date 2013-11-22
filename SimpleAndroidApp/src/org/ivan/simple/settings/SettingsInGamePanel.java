package org.ivan.simple.settings;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ivan.simple.PandaBaseActivity;
import org.ivan.simple.R;

/**
 * Created by Ivan on 22.11.13.
 */
public class SettingsInGamePanel extends SettingsPanel {

    private final View exit;
    private final View replay;
    private final View resume;
    private final PandaBaseActivity context;
    private final LinearLayout buttonsPanel;

    public SettingsInGamePanel(PandaBaseActivity context, SettingsModel model) {
        super(context, model);
        this.context = context;
        buttonsPanel = new LinearLayout(context);
        buttonsPanel.setOrientation(HORIZONTAL);
        buttonsPanel.setGravity(Gravity.CENTER_HORIZONTAL);
        exit = context.prepare(R.drawable.back);
        addLabeledView(exit, "Exit");
        replay = context.prepare(R.drawable.replay);
        addLabeledView(replay, "Replay");
        resume = context.prepare(R.drawable.resume);
        addLabeledView(resume, "Resume");

        addView(buttonsPanel);
    }

    public void setExitOnClickListener(OnClickListener listener) {
        exit.setOnClickListener(listener);
    }

    public void setReplayOnClickListener(OnClickListener listener) {
        replay.setOnClickListener(listener);
    }

    public void setResumeOnClickListener(OnClickListener listener) {
        resume.setOnClickListener(listener);
    }

    private void addLabeledView(View view, String text) {
        buttonsPanel.addView(view);
        buttonsPanel.addView(createLabel(text));
    }

    private TextView createLabel(String text) {
        TextView label = new TextView(getContext());
        label.setText(text);
        label.setTypeface(context.app().getFontProvider().regular());
        return label;
    }

}
