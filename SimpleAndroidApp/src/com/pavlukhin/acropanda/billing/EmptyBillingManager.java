package com.pavlukhin.acropanda.billing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ivan on 24.07.2014.
 */
public class EmptyBillingManager implements IBillingManager {

    @Override
    public void init(Context context) {}

    @Override
    public void dispose() {}

    @Override
    public boolean checkPremium() {
        return false;
    }

    @Override
    public void buyPremium(Activity caller, int requestCode, BuyPremiumCaller listener) {}

    @Override
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }
}
