package com.drewapps.ai.billing;

import java.util.HashMap;
import java.util.List;

public interface BillingManagerListener {

    void onUpdatePurchase(String currentSku, String orderId);
    void onUpdatePrice(List<HashMap<String, String>> details);

    void onError(String error);
}
