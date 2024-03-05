package com.kamal.notification;

import com.windowsazure.messaging.NotificationOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

@RequestMapping("/api")
@RestController
public class NotificationController {

    @Autowired
    private AzureNotificationHubManager hub;

    @RequestMapping(value = "/apple",method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String sendNotificationToApple() {
        NotificationOutcome notificationOutcome = hub.pushMessageToApple();
        return MessageFormat.format("Pushed notification Apple-Notification ID: {0}, Tracking ID: {1}", notificationOutcome.getNotificationId(), notificationOutcome.getTrackingId());
    }

//    @RequestMapping(value = "/droid",method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
//    public String sendNotificationToAndroid() {
//        NotificationOutcome notificationOutcome = hub.pushMessageToAndroid();
//        return MessageFormat.format("Pushed notification Droid-Notification ID: {0}, Tracking ID: {1}", notificationOutcome.getNotificationId(), notificationOutcome.getTrackingId());
//    }


    @RequestMapping(value = "/droid",method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String sendNotificationToAndroid() {
        NotificationOutcome notificationOutcome = hub.pushMessageToAndroid();
        return MessageFormat.format("Pushed notification Droid-Notification ID: {0}, Tracking ID: {1}", notificationOutcome.getNotificationId(), notificationOutcome.getTrackingId());
    }

    @PostMapping(value = "/sendToDeviceList", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendNotificationToMultipleDevices(@RequestBody List<String> deviceTokens) {
        try {
            NotificationOutcome notificationOutcome = hub.pushMessageToMultipleDevicesGCM(deviceTokens);
            String responseMessage = MessageFormat.format("Pushed notification to list - Notification ID: {0}, Tracking ID: {1}",
                    notificationOutcome.getNotificationId(),
                    notificationOutcome.getTrackingId());

            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            String errorMessage = MessageFormat.format("Error sending notification: {0}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }


    @PostMapping(value = "/sendToMultipleDevicesGCM", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendToMultipleDevicesGCM(@RequestBody List<String> deviceTokens) {
        try {
            NotificationOutcome outcome = hub.pushMessageToMultipleDevicesGCM(deviceTokens);
            return ResponseEntity.ok("Notificações enviadas com sucesso. IDs: " + outcome.getNotificationId() + " Tracking IDs: " + outcome.getTrackingId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao enviar notificações: " + e.getMessage());
        }
    }

}

