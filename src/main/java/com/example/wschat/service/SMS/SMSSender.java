package com.example.wschat.service.SMS;

public interface SMSSender {

    void sendSMS(String senderMessagePhoneNumber,String receiverPhoneNumber);
}
