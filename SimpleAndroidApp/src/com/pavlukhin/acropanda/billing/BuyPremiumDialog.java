package com.pavlukhin.acropanda.billing;

import android.app.Dialog;
import android.view.View;

import com.pavlukhin.acropanda.PandaBaseActivity;
import com.pavlukhin.acropanda.R;
import com.pavlukhin.acropanda.utils.UIUtils;

/**
 * Created by ivan on 08.07.2014.
 */
public class BuyPremiumDialog extends Dialog {
    private View buy;
    private View cancel;
    private IBillingManager billing;
    private PandaBaseActivity context;
    private int requestCode;

    public BuyPremiumDialog(PandaBaseActivity context, IBillingManager billing, int requestCode) {
        super(context);
        this.billing = billing;
        this.context = context;
        this.requestCode = requestCode;
        setContentView(R.layout.buy_premium);
        findViews();
        initLayout();
        initListeners();
    }

    private void findViews() {
        buy = findViewById(R.id.buy);
        cancel = findViewById(R.id.cancel);
    }

    private void initLayout() {
        setTitle(getContext().getResources().getString(R.string.buy_premium_title));
        UIUtils.setDefaultFont(context.app().getFontProvider().bold(), findViewById(android.R.id.title));
        UIUtils.setDefaultFont(context.app().getFontProvider().regular(), findViewById(android.R.id.content));
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
        billing.buyPremium(context, requestCode);
    }
}
