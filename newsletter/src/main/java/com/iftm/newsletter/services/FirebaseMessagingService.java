package com.iftm.newsletter.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.iftm.newsletter.models.firebase.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    @Value("${firebase.recipient-token}")
    private String recipientToken;

    public void sendNotification(NotificationMessage notificationMessage) {
        var notification = Notification
                .builder()
                .setTitle(notificationMessage.getTitle())
                .setBody(notificationMessage.getBody())
                .setImage(notificationMessage.getImage())
                .build();

        var message = Message
                .builder()
                .setToken(recipientToken)
                .setNotification(notification)
                .putAllData(notificationMessage.getData())
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException ex) {
            ex.printStackTrace();
        }
    }
}
