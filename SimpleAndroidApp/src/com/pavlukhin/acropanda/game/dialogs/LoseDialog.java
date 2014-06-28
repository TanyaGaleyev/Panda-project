package com.pavlukhin.acropanda.game.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.pavlukhin.acropanda.PandaBaseActivity;
import com.pavlukhin.acropanda.R;

/**
 * Created by ivan on 17.06.2014.
 */
public class LoseDialog extends Dialog {

    public static final String TITLE = "Oops...";

//    OneShotAction increaseButtonsClickArea = new OneShotAction() {
//        @Override
//        protected void doAction() {
//            increaseClickArea(findViewById(R.id.replay));
//            increaseClickArea(findViewById(R.id.back));
//        }
//    };

    public LoseDialog(PandaBaseActivity context) {
        super(context);
        init(context);
    }

    private void init(PandaBaseActivity context) {
        setTitle(TITLE);
        setContentView(R.layout.lose_dialog);
        getWindow().setBackgroundDrawableResource(R.drawable.settings_border);
        styleText(android.R.id.title, context.app().getFontProvider().bold(), Color.DKGRAY);
        context.prepare(findViewById(R.id.replay));
        styleText(R.id.replay_caption, context.app().getFontProvider().regular(), Color.DKGRAY);
        context.prepare(findViewById(R.id.back));
        styleText(R.id.back_caption, context.app().getFontProvider().regular(), Color.DKGRAY);
    }

    public void setReplayOnClickListener(View.OnClickListener l) {
        findViewById(R.id.replay_container).setOnClickListener(l);
    }

    public void setBackOnClickListener(View.OnClickListener l) {
        findViewById(R.id.back_container).setOnClickListener(l);
    }

    private void styleText(int id, Typeface font, int color) {
        TextView textView = (TextView) findViewById(id);
        textView.setTypeface(font);
        textView.setTextColor(color);
    }

//    @Override
//    public void show() {
//        super.show();
//        increaseButtonsClickArea.act();
//    }

//        private void increaseClickArea(final View viewToIncreaseArea) {
//        if(!(viewToIncreaseArea.getParent() instanceof View)) return;
//        final View parent = (View) viewToIncreaseArea.getParent();
//        parent.post(new Runnable() {
//            @Override
//            public void run() {
//                Rect increasedArea = new Rect();
//                viewToIncreaseArea.getHitRect(increasedArea);
//                int dw = 4 * increasedArea.width(), dh = 4 * increasedArea.height();
//                increasedArea.top -= dh;
//                increasedArea.bottom += dh;
//                increasedArea.left -= dw;
//                increasedArea.right += dw;
//                TouchDelegate expandedArea = new TouchDelegate(increasedArea, viewToIncreaseArea);
//                parent.setTouchDelegate(expandedArea);
//            }
//        });
//    }
}
