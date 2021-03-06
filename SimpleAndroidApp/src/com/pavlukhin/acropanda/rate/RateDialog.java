package com.pavlukhin.acropanda.rate;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pavlukhin.acropanda.PandaBaseActivity;
import com.pavlukhin.acropanda.R;
import com.pavlukhin.acropanda.utils.DialogsCalculator;
import com.pavlukhin.acropanda.utils.UIUtils;

/**
 * Created by ivan on 17.07.2014.
 */
public class RateDialog extends Dialog {
    public static final float ICON_PERCENT = 1.00f;
    private Button rate;
    private Button later;
    private Button cancel;
    private TextView title;
    private final PandaBaseActivity pContext;
    private DialogsCalculator dc;

    public RateDialog(PandaBaseActivity context) {
        super(context);
        pContext = context;
        dc = new DialogsCalculator(pContext);
        findViews();
        initLayout();
        initListeners();
    }

    private void findViews() {
        setContentView(R.layout.rate_this_app);
        rate = (Button) findViewById(R.id.rate_btn);
        later = (Button) findViewById(R.id.rate_later_btn);
        cancel = (Button) findViewById(R.id.rate_cancel_btn);
        title = (TextView) findViewById(android.R.id.title);
    }

    private void initLayout() {
        setTitle(getContext().getResources().getString(R.string.rate));
        title.setGravity(Gravity.CENTER);
        title.setTextColor(DialogsCalculator.TITLE_COLOR);
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, dc.titleTextSize());
        getWindow().setBackgroundDrawableResource(R.drawable.settings_border);
        getWindow().setLayout(
                (int) (dc.btnWidth()),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                prepareButton(rate, R.drawable.rate);
                prepareButton(later, R.drawable.remind);
                prepareButton(cancel, R.drawable.no_thanks);
            }
        });
        UIUtils.setDefaultFont(
                pContext.app().getFontProvider().regular(),
                findViewById(android.R.id.content));
        UIUtils.setDefaultFont(
                pContext.app().getFontProvider().bold(),
                findViewById(android.R.id.title));
    }

    private void prepareButton(Button btn, int resId) {
        prepareButton(btn, getContext().getResources().getDrawable(resId));
    }

    private void prepareButton(Button btn, String imagePath) {
        Drawable icon = new BitmapDrawable(
                pContext.app().getImageProvider().getBitmapAutoResizeNoCache(imagePath));
        prepareButton(btn, icon);
    }

    private void prepareButton(Button btn, Drawable icon) {
        btn.setHeight((int) dc.btnHeight());
        btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, dc.btnTextSize());
        setLeftIcon(btn, icon);
    }

    private void setLeftIcon(Button btn, Drawable icon) {
        int h =  (int) dc.btnHeight();
        float iconHeight = h * ICON_PERCENT;
        int margin = (int) ((h - iconHeight) / 2);
        float scale = iconHeight / icon.getIntrinsicHeight();
        icon.setBounds(
                margin,
                0,
                margin + (int) (scale * icon.getIntrinsicWidth()),
                (int) (scale * icon.getIntrinsicHeight()));
        btn.setCompoundDrawables(icon, null, null, null);
//        btn.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
    }

    private void initListeners() {
        rate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AppRater.rate(getContext());
                AppRater.dontShowAgain(getContext());
                dismiss();
            }
        });
        later.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AppRater.dontShowAgain(getContext());
                dismiss();
            }
        });
    }
}
