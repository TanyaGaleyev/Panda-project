package com.pavlukhin.acropanda.billing;

import android.content.Context;

import com.pavlukhin.acropanda.billing.util.IabException;
import com.pavlukhin.acropanda.billing.util.IabHelper;
import com.pavlukhin.acropanda.billing.util.IabResult;
import com.pavlukhin.acropanda.billing.util.Inventory;

/**
 * Created by ivan on 08.07.2014.
 */
public class BillingManager {

    public static final String PREMIUM_SKU = "premium_upgrade";
    private IabHelper billingHelper;

    private String publicKey() {
        return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr4l/hhOJeiJcLQtVSd/a+jjHs/z0hN1cEbiG3byjJMxSpnlcUxv6P5Pm54W8zemqsTdsX0e4g3/x0V/wiENHkosQLgBGLtLQBHzLYbLxfGgkGDmaq3C68IyzQuV8zGr+vPD/GR4Rk6HCyK1k5EDWznm4AeJia55BNSA+iQc0tVrSSXWJ2AD+FgvEPnRZKGdH94uMyKRNGKIMRX83eFw8T2r8vCqEp5t03i9ecVJxP3AKtAGjMU4y3VLA/yjCV3x0RtVelO9TIsARe601/sIlQQrrQoVGnS6BKRgovyr4T4AH5Sro5n2yw7DcWbXyByNjnj7U8bq/wIrxPeXZUlvjNwIDAQAB";
    }

    public void init(Context context) {
        billingHelper = new IabHelper(context, publicKey());
        billingHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    System.err.println("bill error");
                    // Oh noes, there was a problem.
                } else {
                    System.out.println("bill ok");
                    // Hooray, IAB is fully set up!
                }
            }
        });
    }

    public void dispose() {
        if(billingHelper != null) billingHelper.dispose();
    }

    public boolean checkPremium() {
        boolean ret = false;
        try {
            Inventory inventory = billingHelper.queryInventory(false, null);
            ret = inventory.hasPurchase(PREMIUM_SKU);
        } catch (IabException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void buyPremium() {

    }
}
