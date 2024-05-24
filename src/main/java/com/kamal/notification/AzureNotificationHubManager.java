package com.kamal.notification;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamal.notification.model.*;
import com.windowsazure.messaging.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AzureNotificationHubManager {

    private static final Logger logger = Logger.getLogger(AzureNotificationHubManager.class.getCanonicalName());

    private static final String ANDROID = "G";
    private static final String IOS = "A";
    @Autowired
    private NotificationHub notificationHub;

    public NotificationOutcome pushNotification(PushNotification dataTemplate) {

        NotificationOutcome notificationOutcome = new NotificationOutcome("", "");

        try {
            Notification notificationAndroid = null;
            Notification notificationIos = null;
            List<String> androidTokenList = null;
            List<String> iosTokenList = null;

            DataTemplate dataTemplateDTO = new DataTemplate(dataTemplate);

            List<Destinatary> androidList = dataTemplate.getDestinatarios().stream()
                    .filter(dispositivosResponse -> dispositivosResponse.getPlataforma().equals("G"))
                    .toList();

            List<Destinatary> iosList = dataTemplate.getDestinatarios().stream()
                    .filter(dispositivosResponse -> dispositivosResponse.getPlataforma().equals("A"))
                    .toList();

            if (!androidList.isEmpty()) {
                androidTokenList = androidList.stream().map(dispositivosResponse -> dispositivosResponse.getDispostivos().getHashDispositivo())
                        .collect(Collectors.toList());
                String message = buildMessageForPlatform("G", dataTemplateDTO);
                notificationAndroid = Notification.createFcmV1Notification(message);
                logger.info("Headers da Notificação: {}" + notificationAndroid.getHeaders());
                logger.info("Body da Notificação: {}" + notificationAndroid.getBody());

                notificationOutcome = notificationHub.sendDirectNotification(notificationAndroid, androidTokenList);
//                logger.info("Tracking ID Android Platform - {}", notificationOutcome.getTrackingId());
            }

            if (!iosList.isEmpty()) {
                iosTokenList = iosList.stream().map(dispositivosResponse -> dispositivosResponse.getDispostivos().getHashDispositivo())
                        .collect(Collectors.toList());
                String message = buildMessageForPlatform("A", dataTemplateDTO);
                notificationIos = Notification.createAppleNotification(message);
//                logger.info("Headers da Notificação: {}", notificationIos.getHeaders());
                logger.info("Body da Notificação: {}" + notificationIos.getBody().toString());

                notificationOutcome = notificationHub.sendDirectNotification(notificationIos, iosTokenList);
                logger.info("Tracking ID IoS Platform - {}" + notificationOutcome.getTrackingId());
            }

        } catch (Exception e) {
            logger.info(e.getMessage());
//            this.pNotLog.logBusinessError(dataTemplate, ReturnCode.ERRO_FATAL, e);
        }

        return notificationOutcome;
    }

    private String buildMessageForPlatform(String platform, DataTemplate dataTemplateDTO) throws JsonProcessingException {
        String messageBuild = "";
        ObjectMapper mapper = new ObjectMapper();
//        DataTemplateDTO dataTemplateDTO = new DataTemplateDTO(dataTemplate);

        if (platform.equals("A")) {

            Alert alertTemplate = new Alert(buildTitleMessage(dataTemplateDTO.getEvento()), dataTemplateDTO.getMensagemPush());
            IosTemplate iosTemplate = new IosTemplate(new Aps(alertTemplate), dataTemplateDTO);

            messageBuild = mapper.writeValueAsString(iosTemplate);
        } else if (platform.equals("G")) {

            AndroidNotification androidNotification = new AndroidNotification(buildTitleMessage(dataTemplateDTO.getEvento()), dataTemplateDTO.getMensagemPush());
            Message message = new Message(androidNotification, dataTemplateDTO);
            AndroidTemplate androidTemplate = new AndroidTemplate(message);
            messageBuild = mapper.writeValueAsString(androidTemplate);
        }

        return messageBuild;
    }

    public String buildTitleMessage(String evento) {
        String titleMessage = switch (evento) {
            case "pix-realizado-enviado" -> "Pix enviado";
            case "pix-realizado-recebido" -> "Pix recebido";
            case "nova-mensagem-chat" -> "Net Empresa";
            default -> "";
        };
        return titleMessage;
    }

//    public NotificationOutcome pushMessageToApple(SendDirectionRequest request) {
//        NotificationOutcome notificationOutcome = new NotificationOutcome("", "");
//        try {
//
////            String message = "{\"aps\":{\"alert\":\"Notification Hub test notification\"}}";
//            Notification iosNotification = buildNotification(request);
//
//            notificationOutcome = notificationHub.sendDirectNotification(iosNotification, request.getTokens());
//            logger.log(Level.INFO, MessageFormat.format("notification id: {0},track id {1},message: {2}", notificationOutcome.getNotificationId(), notificationOutcome.getTrackingId()));
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Azure Push Notification iOS devices failed.", e);
//        }
//
//        return notificationOutcome;
//    }

//    public NotificationOutcome pushNotification(PushNotification request) {
//        NotificationOutcome notificationOutcome = new NotificationOutcome("", "");
//
//        List<Destinatary> androidList = getRecipients(request, ANDROID);
//
//        List<Destinatary> iosList = getRecipients(request, IOS);
//
//        if (!androidList.isEmpty()) {
//            buildAndSendNotification(androidList, ANDROID, request);
//        }
//
//        if (!iosList.isEmpty()) {
//            buildAndSendNotification(iosList, IOS, request);
//        }
//
//        return notificationOutcome;
//    }

//    private void buildAndSendNotification(List<Destinatary> recipients, String platform, PushNotification pushNotification) {
//        recipients
//                .stream()
//                .forEach(recipient -> {
//                    List<String> hasheTokens = getHasheTokens(recipients);
//                    String message = buildMessageForPlatform(platform, pushNotification);
//                    Notification notification = buildNotification(message, platform);
//                    NotificationOutcome notificationOutcome = sendNotificationHub(notification, hasheTokens);
//
//                    String template = platform.equals(ANDROID) ? "ANDROID" : platform.equals(IOS) ? "APPLE" : "";
//
//                    logger.log(Level.INFO, "Message sent successfully");
//                    logger.log(Level.INFO, "Platform " + template + " - NotificationId :: "
//                            + notificationOutcome.getNotificationId()
//                            + "\n TrackingId :: " + notificationOutcome.getTrackingId());
//                });
//
//    }

//    private static Notification buildNotification(String message, String platform) {
//        return platform.equals(ANDROID)
//                ? Notification.createGcmNotification(message)
//                : platform.equals(IOS)
//                ? Notification.createAppleNotification(message)
//                : null;
//    }
//
//    private static List<Destinatary> getRecipients(PushNotification request, String platform) {
//        return request.getDestinatarios().stream().filter(destinatary -> destinatary.getDispostivos()
//                .getPlataforma().equals(platform)).collect(Collectors.toList());
//    }

//    private NotificationOutcome sendNotificationHub(Notification notification, List<String> tokenList) {
//        try {
//            return notificationHub.sendDirectNotification(notification, tokenList);
//        } catch (NotificationHubsException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    private static List<String> getHasheTokens(List<Destinatary> recipient) {
//        return recipient.stream().map(destinatary -> destinatary.getDispostivos().getHash())
//                .collect(Collectors.toList());
//    }

//    private String buildMessageForPlatform(String platform, PushNotification request) {
//        String messageBuild = "";
//
//        ObjectMapper mapper = new ObjectMapper();
//        DataTemplate data = new DataTemplate(request);
//
//        if (platform.equals(IOS)) {
//            // TO DO - remover hardcode
//            Alert alertTemplate = new Alert("Teste Notificação", "Exemplo de teste de notificação");
//            IosTemplate iosTemplate = new IosTemplate(new Aps(alertTemplate), data);
//
//            try {
//                messageBuild = mapper.writeValueAsString(iosTemplate);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        } else if (platform.equals(ANDROID)) {
//            // TO DO - remover hardcode
//            AndroidNotification androidNotification = new AndroidNotification("Teste Notificação", "Exemplo de teste de notificação");
//            AndroidTemplate androidTemplate = new AndroidTemplate(androidNotification, data);
//            try {
//                messageBuild = mapper.writeValueAsString(androidTemplate);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        return messageBuild;
//    }


//    private Notification buildNotification(String plataforma, String mensagemPush) throws JsonProcessingException {
//
//        MessagePayloadAndroid messagePayload = new MessagePayloadAndroid();
//        messagePayload.setData(request.getData());
//        messagePayload.setNotification(request.getNotification());
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String message = objectMapper.writeValueAsString(messagePayload);
//
//        Notification notification = null;
//
//        if(plataforma.equals("G")) {
//            notification = Notification.createFcmNotification(message);
//        } else if(plataforma.equals("A")) {
//            notification = Notification.createAppleNotification(message);
//        }
//        return notification;
//    }

    //    public NotificationOutcome pushMessageToMultipleDevicesGCM(List<String> deviceTokens) {
//        NotificationOutcome notificationOutcome = new NotificationOutcome("", "");
//        try {
//            //  mensagem de notificação aqui
//            String notificationPayload = "{\"notification\":{\"title\":\"Título da Notificação\",\"body\":\"Notificação de teste para o Sidney, Vinicius e Justo  Apenas\"},\"data\":{\"chave1\":\"valor1\",\"chave2\":\"valor2\"}}";
//
//            Notification notification = Notification.createFcmNotification(notificationPayload);
//
//            // Envie a notificação para o token atual
//            notificationOutcome = notificationHub.sendDirectNotification(notification, deviceTokens);
//
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Azure Push Ne = {NotificationHubsException@7818} \"com.windowsazure.messaging.NotificationHubsException: Error: HTTP/1.1 403 Forbidden body: This operation requires one of the following permissions: [manage, send].\\nCurrent request has the following permissions: [listen].\\nPlease check both Namespace Network ACLs and used SharedAccessSignature.\"... Viewotification android devices failed.", e);
//        }
//        return notificationOutcome;
//    }
//
//    public NotificationOutcome pushMessageToDeviceGCM(NotificationRequest notificationRequest) {
//        NotificationOutcome notificationOutcome = new NotificationOutcome("", "");
//        try {
//            if(!existeDevice(notificationRequest.getPnsToken())){
//                registerDevice(notificationRequest);
//            }
//
//            String notificationPayload = "{\"notification\":{\"title\":\"Título da Notificação\",\"body\":\"Teste\"},\"data\":{\"chave1\":\"valor1\",\"chave2\":\"valor2\"}}";
//            NotificationMessage notificationMessage = new NotificationMessage();
//
//            notificationMessage.setTitle(notificationRequest.getNotification().getTitle());
//            notificationMessage.setBody(notificationRequest.getNotification().getBody());
//
//            DataMessage dataMessage = new DataMessage();
//            dataMessage.setChave1(notificationRequest.getData().getChave1());
//            dataMessage.setChave2(notificationRequest.getData().getChave2());
//
//            MessagePayloadAndroid messagePayload = new MessagePayloadAndroid();
//            messagePayload.setNotification(notificationMessage);
//            messagePayload.setData(dataMessage);
//
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            String message = objectMapper.writeValueAsString(messagePayload);
//
//
//            Notification notification = Notification.createFcmNotification(message);
//
//
//            // Envie a notificação para o token atual
//            notificationOutcome = notificationHub.sendDirectNotification(notification, notificationRequest.getPnsToken());
//
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Azure Push Ne = {NotificationHubsException@7818} \"com.windowsazure.messaging.NotificationHubsException: Error: HTTP/1.1 403 Forbidden body: This operation requires one of the following permissions: [manage, send].\\nCurrent request has the following permissions: [listen].\\nPlease check both Namespace Network ACLs and used SharedAccessSignature.\"... Viewotification android devices failed.", e);
//        }
//        return notificationOutcome;
//    }
//
//    private Boolean existeDevice(String pnsToken) throws NotificationHubsException {
//        boolean result = false;
//        try {
//            Optional<BaseInstallation> existeInstalation = Optional.ofNullable(notificationHub.getInstallation(pnsToken));
//            result = true;
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Dispositivo não existe");
//        }
//        return result;
//    }
//
//    public void registerDevice(NotificationRequest notificationRequest) {
//        Registration registration;
//
//        switch (notificationRequest.getPlatform()) {
//            case "G":
//                registration = new GcmRegistration(notificationRequest.getPnsToken());
//                break;
//            case "A":
//                registration = new AppleRegistration(notificationRequest.getPnsToken());
//                break;
//            default:
//                throw new IllegalArgumentException("Plataforma não suportada: " + notificationRequest.getPlatform());
//        }
//        try {
//            registration.getTags().add(notificationRequest.getClientTag()); // Adiciona a tag do identificador único do cliente
//            Registration registrationResult = notificationHub.createRegistration(registration);
//            System.out.println("registrationId: " + registrationResult.getRegistrationId());
//        } catch (NotificationHubsException e) {
//            e.printStackTrace(); // Trata a exceção apropriadamente
//        }
//    }
//
    public Registration verifyRegistrationId(String registrationId) {
        Registration registration = null;
        try {
            registration = notificationHub.getRegistration(registrationId);
        } catch (NotificationHubsException e) {
            logger.log(Level.SEVERE, "RegistrationId not found");
        }
        return registration;
    }

    public CollectionResult getRegistrations() {
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

    //
    public Boolean deleteRegistrationId(RegistrationRequest request) {
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
//
//    public Registration updateRegistrationId(UpdatedRequest request) {
//
//        Registration registrationUpdated = verifyRegistrationId(request.getRegistrationIdOld());
//
//        if (registrationUpdated != null) {
//            try {
//                registrationUpdated.setTags(request.getClientTag());
//                registrationUpdated.setRegistrationId(request.getRegistrationIdNew());
//                registrationUpdated = notificationHub.updateRegistration(registrationUpdated);
//
//                // TO DO verificar outras setters
//            } catch (NotificationHubsException e) {
//                logger.log(Level.SEVERE, "RegistrationId updated failed");
//            }
//        }
//        return registrationUpdated;
//    }

}
