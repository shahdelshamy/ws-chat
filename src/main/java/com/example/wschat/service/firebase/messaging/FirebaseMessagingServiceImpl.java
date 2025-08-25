package com.example.wschat.service.firebase.messaging;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {

    @Override
    public void sendNotification(String token, String title, String body) {

        try {

            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Successfully sent message: " + response);

        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
