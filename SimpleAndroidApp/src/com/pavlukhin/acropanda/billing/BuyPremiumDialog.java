package com.pavlukhin.acropanda.billing;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pavlukhin.acropanda.PandaBaseActivity;
import com.pavlukhin.acropanda.R;
import com.pavlukhin.acropanda.utils.DialogsCalculator;
import com.pavlukhin.acropanda.utils.UIUtils;

/**
 * Created by ivan on 08.07.2014.
 */
public class BuyPremiumDialog extends Dialog {
    private Button buy;
    private Button cancel;
    private TextView message;
    private IBillingManager billing;
    private PandaBaseActivity pContext;
    private int requestCode;
    private TextView title;
    private DialogsCalculator dc;

    public BuyPremiumDialog(PandaBaseActivity pContext, IBillingManager billing, int requestCode) {
        super(pContext);
        this.billing = billing;
        this.pContext = pContext;
        this.requestCode = requestCode;
        dc = new DialogsCalculator(pContext);
        findViews();
        initLayout();
        initListeners();
    }

    private void findViews() {
        setContentView(R.layout.buy_premium);
        buy = (Button) findViewById(R.id.buy);
        cancel = (Button) findViewById(R.id.cancel);
        title = (TextView) findViewById(android.R.id.title);
        message = (TextView) findViewById(R.id.message);
    }

    private void initLayout() {
        setTitle(getContext().getResources().getString(R.string.buy_premium_title));
        title.setGravity(Gravity.CENTER);
        title.setTextColor(DialogsCalculator.TITLE_COLOR);
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, dc.titleTextSize());
//        prepareTitleIcon();
        getWindow().setBackgroundDrawableResource(R.drawable.settings_border);
        buy.setHeight((int) dc.btnHeight());
        buy.setTextSize(TypedValue.COMPLEX_UNIT_PX, dc.btnTextSize());
        cancel.setHeight((int) dc.btnHeight());
        cancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, dc.btnTextSize());
        message.setTextSize(TypedValue.COMPLEX_UNIT_PX, dc.btnTextSize());
        UIUtils.setDefaultFont(pContext.app().getFontProvider().bold(), title);
        UIUtils.setDefaultFont(pContext.app().getFontProvider().regular(), findViewById(android.R.id.content));
    }

    private void prepareTitleIcon() {
        Drawable icon = pContext.getResources().getDrawable(R.drawable.chest_open_small);
        float scale = dc.titleTextSize() * 1.5f / icon.getIntrinsicHeight();
        icon.setBounds(0, 0, (int) (icon.getIntrinsicWidth() * scale), (int) (icon.getIntrinsicHeight() * scale));
        title.setCompoundDrawables(icon, null, null, null);
    }

    private void initListeners() {
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyPremium();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void buyPremium() {
        dismiss();
        billing.buyPremium(pContext, requestCode);
    }
}
