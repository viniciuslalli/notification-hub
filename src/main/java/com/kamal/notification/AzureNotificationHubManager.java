package com.kamal.notification;


import com.windowsazure.messaging.*;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AzureNotificationHubManager {

    private static final Logger logger = Logger.getLogger(AzureNotificationHubManager.class.getCanonicalName());
    @Autowired
    private NotificationHub notificationHub;

    public NotificationOutcome pushMessageToApple() {
        NotificationOutcome notificationOutcome = new NotificationOutcome("", "");
        try {
            String message = "{\"aps\":{\"alert\":\"Notification Hub test notification\"}}";
            Notification iosNotification = Notification.createAppleNotification(message);
            notificationOutcome = notificationHub.sendNotification(iosNotification, "");
            logger.log(Level.INFO, MessageFormat.format("notification id: {0},track id {1},message: {2}", notificationOutcome.getNotificationId(), notificationOutcome.getTrackingId(), message));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Azure Push Notification iOS devices failed.", e);
        }


        return notificationOutcome;
    }

    public NotificationOutcome pushMessageToMultipleDevicesGCM(List<String> deviceTokens) {
        NotificationOutcome notificationOutcome = new NotificationOutcome("", "");
        try {

            CollectionResult registrations = notificationHub.getRegistrations();

            //  mensagem de notificação aqui
            String notificationPayload = "{\"notification\":{\"title\":\"Título da Notificação\",\"body\":\"Notificação de teste para o Sidney, Vinicius e Justo  Apenas\"},\"data\":{\"chave1\":\"valor1\",\"chave2\":\"valor2\"}}";

            Notification notification = Notification.createFcmNotification(notificationPayload);

            // Envie a notificação para o token atual
            notificationOutcome = notificationHub.sendDirectNotification(notification, deviceTokens);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Azure Push Ne = {NotificationHubsException@7818} \"com.windowsazure.messaging.NotificationHubsException: Error: HTTP/1.1 403 Forbidden body: This operation requires one of the following permissions: [manage, send].\\nCurrent request has the following permissions: [listen].\\nPlease check both Namespace Network ACLs and used SharedAccessSignature.\"... Viewotification android devices failed.", e);
        }
        return notificationOutcome;
    }

//    public void registerDevice(String deviceToken, String platform) {
//        try {
//
//
//
//
//            String installationId = UUID.randomUUID().toString();
//            Installation installation = new Installation(installationId);
//
//            installation.setPushChannel(deviceToken);
//
//            if (platform.equalsIgnoreCase("android")) {
//                installation.setPlatform(NotificationPlatform.Gcm);
//            } else if (platform.equalsIgnoreCase("ios")) {
//                installation.setPlatform(NotificationPlatform.Apns);
//            }
//
//
//            notificationHub.createOrUpdateInstallation(installation);
//            logger.log(Level.INFO, "Dispositivo registrado com sucesso: " + deviceToken);
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Falha ao registrar o dispositivo", e);
//        }
//    }





}
