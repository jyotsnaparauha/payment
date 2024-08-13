package com.vst.app;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentCallbackController {

    @PostMapping("/paymentCallback")
    public JSONObject paymentCallback(@RequestBody JSONObject callbackData) {
        try {
            String orderId = callbackData.getString("ORDERID");
            String txnId = callbackData.getString("TXNID");
            String txnAmount = callbackData.getString("TXNAMOUNT");
            String paymentStatus = callbackData.getString("STATUS");

            
            boolean isPaymentSuccessful = paymentStatus.equalsIgnoreCase("TXN_SUCCESS");
           
            JSONObject response = new JSONObject();
            response.put("status", "success");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("status", "error");
            return errorResponse;
        }
    }
}
