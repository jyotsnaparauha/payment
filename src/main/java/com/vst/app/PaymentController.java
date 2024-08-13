package com.vst.app;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paytm.pg.merchant.PaytmChecksum;

@RestController
public class PaymentController {

    @Value("${paytm.merchant.id}") 
    private String merchantId;

    @Value("${paytm.merchant.key}") 
    private String merchantKey;

    @PostMapping("/initiatePayment")
    public JSONObject initiatePayment(@RequestBody JSONObject requestBody) {
        try {
            
            String amount = requestBody.getString("amount");

            
            JSONObject transactionToken = generateTransactionToken(amount);

            return transactionToken;
        } catch (Exception e) {
            e.printStackTrace();
            
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Failed to initiate payment");
            return errorResponse;
        }
    }

   
    private JSONObject generateTransactionToken(String amount) throws Exception {
      
        JSONObject body = new JSONObject();
        body.put("requestType", "Payment");
        body.put("mid", merchantId);
        body.put("websiteName", "WEBSTAGING");
        body.put("orderId", "ORDERID_" + System.currentTimeMillis());
        body.put("callbackUrl", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=<order_id>");

        JSONObject txnAmount = new JSONObject();
        txnAmount.put("value", amount);
        txnAmount.put("currency", "INR");

        JSONObject userInfo = new JSONObject();
        userInfo.put("custId", "CUST_" + System.currentTimeMillis());

        body.put("txnAmount", txnAmount);
        body.put("userInfo", userInfo);

        String checksum = PaytmChecksum.generateSignature(body.toString(), merchantKey);

        JSONObject head = new JSONObject();
        head.put("signature", checksum);

        JSONObject paytmParams = new JSONObject();
        paytmParams.put("body", body);
        paytmParams.put("head", head);

        return paytmParams;
    }
}

