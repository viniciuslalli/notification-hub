package com.kamal.notification;

import com.windowsazure.messaging.Notification;


public class FcmNotification extends Notification {

    public static Notification createFcmNotification(String body) {
        Notification n = new Notification();
        String fcmPayload = String.format("{ \"message\": { \"notification\": { \"body\" : \"%s\" } } }", body);
        n.setBody(fcmPayload);
//        n.setContentType(ContentType.APPLICATION_JSON);
        n.getHeaders().put("ServiceBusNotification-Format", "fcmv1");
        return n;
    }
}
