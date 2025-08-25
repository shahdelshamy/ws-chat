package com.example.wschat.service.firebase.messaging;

public interface FirebaseMessagingService {

    void sendNotification(String token, String title, String body);
}
