package com.kamal.notification;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.windowsazure.messaging.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
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

    public NotificationOutcome pushMessageToDeviceGCM(NotificationRequest notificationRequest) {
        NotificationOutcome notificationOutcome = new NotificationOutcome("", "");
        try {
            if(!existeDevice(notificationRequest.getPnsToken())){
                registerDevice(notificationRequest);
            }


            //  mensagem de notificação aqui
//            String notificationPayload = "{\"notification\":{\"title\":\"Título da Notificação\",\"body\":\"Teste\"},\"data\":{\"chave1\":\"valor1\",\"chave2\":\"valor2\"}}";
            String notificationPayload = "{\"notification\":{\"title\":\"Título da Notificação\",\"body\":\"Teste\"},\"data\":{\"chave1\":\"valor1\",\"chave2\":\"valor2\"}}";
            NotificationMessage notificationMessage = new NotificationMessage();

            notificationMessage.setTitle(notificationRequest.getNotification().getTitle());
            notificationMessage.setBody(notificationRequest.getNotification().getBody());

            DataMessage dataMessage = new DataMessage();
            dataMessage.setChave1(notificationRequest.getData().getChave1());
            dataMessage.setChave2(notificationRequest.getData().getChave2());

            MessagePayload messagePayload = new MessagePayload();
            messagePayload.setNotification(notificationMessage);
            messagePayload.setData(dataMessage);


            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(messagePayload);


            Notification notification = Notification.createFcmNotification(message);


            // Envie a notificação para o token atual
            notificationOutcome = notificationHub.sendDirectNotification(notification, notificationRequest.getPnsToken());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Azure Push Ne = {NotificationHubsException@7818} \"com.windowsazure.messaging.NotificationHubsException: Error: HTTP/1.1 403 Forbidden body: This operation requires one of the following permissions: [manage, send].\\nCurrent request has the following permissions: [listen].\\nPlease check both Namespace Network ACLs and used SharedAccessSignature.\"... Viewotification android devices failed.", e);
        }
        return notificationOutcome;
    }

    private Boolean existeDevice(String pnsToken) throws NotificationHubsException {
        boolean result = false;
        try {
            Optional<BaseInstallation> existeInstalation = Optional.ofNullable(notificationHub.getInstallation(pnsToken));
            result = true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Dispositivo não existe");
        }
        return result;
    }


    public void registerDevice(NotificationRequest notificationRequest) {
        Registration registration;

        switch (notificationRequest.getPlatform()) {
            case "G":
                registration = new GcmRegistration(notificationRequest.getPnsToken());
                break;
            case "A":
                registration = new AppleRegistration(notificationRequest.getPnsToken());
                break;
            default:
                throw new IllegalArgumentException("Plataforma não suportada: " + notificationRequest.getPlatform());
        }
        try {
            registration.getTags().add(notificationRequest.getClientTag()); // Adiciona a tag do identificador único do cliente
            Registration registrationResult = notificationHub.createRegistration(registration);
            System.out.println("registrationId: " + registrationResult.getRegistrationId());
        } catch (NotificationHubsException e) {
            e.printStackTrace(); // Trata a exceção apropriadamente
        }
    }

    public Registration verifyRegistrationId(String registrationId){
        Registration registration = null;
        try {
             registration = notificationHub.getRegistration(registrationId);
        } catch (NotificationHubsException e) {
            logger.log(Level.SEVERE, "RegistrationId not found");
        }
        return registration;
    }

    public CollectionResult getRegistrations(){
        CollectionResult registration = null;
        try {
            registration = notificationHub.getRegistrations();
            List<Registration> registrations = registration.getRegistrations();

            logger.log(Level.INFO, "Total de registros: " + registrations.size());
        } catch (NotificationHubsException e) {
            logger.log(Level.SEVERE, "RegistrationId not found");
        }
        return registration;
    }

    public Boolean deleteRegistrationId(RegistrationRequest request){
        boolean result = false;
        Registration registrationDelete = verifyRegistrationId(request.getRegistrationId());
        try {
            notificationHub.deleteRegistration(registrationDelete);
            result = true;
        } catch (NotificationHubsException e) {
            logger.log(Level.SEVERE, "RegistrationId delete failed");
        }
        return result;
    }

    //Anzai
//    val registrationToUpdate:Registration = hub.getRegistration(registrationId)
//    registrationToUpdate.tags.add(newTag)
//    val registrationResult = hub.updateRegistration(registrationToUpdate)
//    print("registrationId: ${registrationResult.registrationId}")

    public Registration updateRegistrationId(UpdatedRequest request) {

        Registration registrationUpdated = verifyRegistrationId(request.getRegistrationIdOld());

        if (registrationUpdated != null) {
            try {
                registrationUpdated.setTags(request.getClientTag());
                registrationUpdated.setRegistrationId(request.getRegistrationIdNew());
                registrationUpdated = notificationHub.updateRegistration(registrationUpdated);

                // TO DO verificar outras possibilidades
            } catch (NotificationHubsException e) {
                logger.log(Level.SEVERE, "RegistrationId updated failed");
            }
        }
        return registrationUpdated;
    }
}
