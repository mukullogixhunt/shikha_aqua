package com.logixhunt.shikhaaqua.api.response.phonepe;

public class PhonePeResponse {

    public boolean success;
    public String code;
    public String message;
    public Data data;


    public class Data{
        public String merchantId;
        public String merchantTransactionId;
        public String transactionId;
        public int amount;
        public String state;
        public String responseCode;
        public PaymentInstrument paymentInstrument;
    }

    public class PaymentInstrument{
        public Object vpa;
        public String maskedAccountNumber;
        public String ifsc;
        public String utr;
        public Object upiTransactionId;
    }

}
