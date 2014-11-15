package com.pavlukhin.acropanda.billing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ivan on 24.07.2014.
 */
public interface IBillingManager {
    void init(Context context);

    void dispose();

    boolean checkPremium();

    void buyPremium(Activity caller, int requestCode, BuyPremiumCaller listener);

    boolean handleActivityResult(int requestCode, int resultCode, Intent data);

    boolean isInitialized();
}
