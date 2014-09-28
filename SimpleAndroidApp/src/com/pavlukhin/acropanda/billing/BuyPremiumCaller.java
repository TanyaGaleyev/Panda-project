package com.pavlukhin.acropanda.billing;

import com.pavlukhin.acropanda.billing.util.Purchase;

/**
 * Created by ivan on 28.09.2014.
 */
public interface BuyPremiumCaller {
    void onPremiumBought(Purchase p);
}
