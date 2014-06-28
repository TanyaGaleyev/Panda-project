package com.pavlukhin.acropanda.settings;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pavlukhin.acropanda.PandaBaseActivity;
import com.pavlukhin.acropanda.R;

/**
 * Created by Ivan on 22.11.13.
 */
public class SettingsInGamePanel extends SettingsPanel {

    public static final int PADDING_DP = 4;
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
        buttonsPanel.setGravity(Gravity.CENTER);
        int p = context.app().convertDpToPixels(PADDING_DP);
        exit = addLabeledView(context.prepare(R.drawable.back), "Exit");
        exit.setPadding(p, p, p, p);
        replay = addLabeledView(context.prepare(R.drawable.replay), "Replay");
        replay.setPadding(p, p, p, p);
        resume = addLabeledView(context.prepare(R.drawable.resume), "Resume");
        resume.setPadding(p, p, p, p);

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

    private View addLabeledView(View view, String text) {
        LinearLayout labeledView = new LinearLayout(getContext());
        labeledView.setOrientation(HORIZONTAL);
        labeledView.setGravity(Gravity.CENTER);
        labeledView.addView(view);
        labeledView.addView(createLabel(text));
        buttonsPanel.addView(labeledView);
        return labeledView;
    }

    private TextView createLabel(String text) {
        TextView label = new TextView(getContext());
        label.setText(text);
        label.setTypeface(context.app().getFontProvider().regular());
        return label;
    }

}
