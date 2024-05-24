package com.kamal.notification;

import com.kamal.notification.model.PushNotification;
import com.kamal.notification.model.RegistrationRequest;
import com.windowsazure.messaging.CollectionResult;
import com.windowsazure.messaging.NotificationOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class NotificationController {

    @Autowired
    private AzureNotificationHubManager hub;

//    @RequestMapping(value = "/apple",method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
//    public String sendNotificationToApple() {
////        NotificationOutcome notificationOutcome = hub.pushMessageToApple();
////        return MessageFormat.format("Pushed notification Apple-Notification ID: {0}, Tracking ID: {1}", notificationOutcome.getNotificationId(), notificationOutcome.getTrackingId());
//    }

//    @RequestMapping(value = "/droid",method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
//    public String sendNotificationToAndroid() {
//        NotificationOutcome notificationOutcome = hub.pushMessageToAndroid();
//        return MessageFormat.format("Pushed notification Droid-Notification ID: {0}, Tracking ID: {1}", notificationOutcome.getNotificationId(), notificationOutcome.getTrackingId());
//    }


//    @PostMapping(value = "/sendToDeviceList", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<String> sendNotificationToMultipleDevices(@RequestBody List<String> deviceTokens) {
//        try {
//            NotificationOutcome notificationOutcome = hub.pushMessageToMultipleDevicesGCM(deviceTokens);
//            String responseMessage = MessageFormat.format("Pushed notification to list - Notification ID: {0}, Tracking ID: {1}",
//                    notificationOutcome.getNotificationId(),
//                    notificationOutcome.getTrackingId());
//
//            return ResponseEntity.ok(responseMessage);
//        } catch (Exception e) {
//            String errorMessage = MessageFormat.format("Error sending notification: {0}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
//        }
//    }

    @PostMapping(value = "/pushSendDirectMessage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity pushSendDirectMessageToAndroidList(@RequestBody PushNotification request) {
        try {
            NotificationOutcome outcome = hub.pushNotification(request);
            return ResponseEntity.ok(outcome);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao enviar notificações: " + e.getMessage());
        }
    }

    @PostMapping(value = "/pushSendDirectMessageTemplate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity pushSendDirectMessageToAndroidListTemplate(@RequestBody PushNotification request) {
        try {
            NotificationOutcome outcome = hub.pushNotificationTemplate(request);
            return ResponseEntity.ok(outcome);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao enviar notificações: " + e.getMessage());
        }
    }

//    @PostMapping(value = "/sendToMultipleDevicesGCM", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<String> pushMessageToDeviceGCM(@RequestBody List<String> deviceTokens) {
//        try {
//            NotificationOutcome outcome = hub.pushMessageToMultipleDevicesGCM(deviceTokens);
//            return ResponseEntity.ok("Notificações enviadas com sucesso. IDs: " + outcome.getNotificationId() + " Tracking IDs: " + outcome.getTrackingId());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao enviar notificações: " + e.getMessage());
//        }
//    }
//
//    @PostMapping(value = "/sendToGCM", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<String> sendToMultipleDevicesGCM(@RequestBody NotificationRequest notificationRequest) {
//        try {
//            NotificationOutcome outcome = hub.pushMessageToDeviceGCM(notificationRequest);
//            return ResponseEntity.ok("Notificações enviadas com sucesso. IDs: " + outcome.getNotificationId() + " Tracking IDs: " + outcome.getTrackingId());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao enviar notificações: " + e.getMessage());
//        }
//    }
//
//    @PostMapping(value = "/verifyRegistrationId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity verifyRegistrationId(@RequestBody RegistrationRequest request) {
//
//            Registration registration = hub.verifyRegistrationId(request.getRegistrationId());
//
//            if (registration == null){
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("RegistrationId not found: " + request.getRegistrationId());
//            } else {
//                return ResponseEntity.ok(registration);
//            }
//    }
//
    @RequestMapping(value = "/getRegistrations",method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getRegistrations() {

        CollectionResult registration = hub.getRegistrations();

        if (registration == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registrations not found! " );
        } else {
            return ResponseEntity.ok(registration);
        }
    }
//
//    @DeleteMapping(value = "/deleteRegistrationId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity deleteRegistrationId(@RequestBody RegistrationRequest request) {
//
//            Boolean deletetedRegistration = hub.deleteRegistrationId(request);
//
//            return deletetedRegistration ? ResponseEntity.ok("RegistrationId: " + request.getRegistrationId() + " deleted with success!")
//                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Atempting of delete process failed");
//    }
//
//    @PatchMapping(value = "/updateRegistrationId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity updateRegistrationId(@RequestBody UpdatedRequest request) {
//
//        Registration updateRegistration = hub.updateRegistrationId(request);
//
//        if (updateRegistration == null) {
//            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Atempting of updated process failed");
//        }
//        return ResponseEntity.ok("Updated RegistrationId from: " + request.getRegistrationIdOld() + "to: "
//                + request.getRegistrationIdNew() +" updated with success!");
//    }
}

