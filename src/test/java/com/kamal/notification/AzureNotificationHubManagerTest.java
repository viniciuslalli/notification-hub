//package com.kamal.notification;
//
//import com.kamal.notification.AzureNotificationHubManager;
//import com.kamal.notification.model.PushNotification;
//import com.kamal.notification.model.Destinatary;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import com.windowsazure.messaging.*;
//import java.util.Arrays;
//
//import static org.mockito.Mockito.*;
//
//class AzureNotificationHubManagerTest {
//
//    @InjectMocks
//    private AzureNotificationHubManager notificationHubManager;
//
//    @Mock
//    private NotificationHub notificationHub;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//
//    @Test
//    void testPushNotificationAndroid() throws NotificationHubsException {
//        PushNotification pushNotification = new PushNotification();
//        pushNotification.setMensagemPush("Teste Android");
//        Destinatary destinatary = new Destinatary();
//        destinatary.setHash("AndroidToken");
//        destinatary.setPlataforma("G");
//        pushNotification.setDestinatarios(Arrays.asList(destinatary));
//
//        notificationHubManager.pushNotification(pushNotification);
//
//        verify(notificationHub, times(1)).sendDirectNotification(any(Notification.class), eq(Arrays.asList("AndroidToken")));
//    }
//
//    @Test
//    void testPushNotificationiOS() throws NotificationHubsException {
//        PushNotification pushNotification = new PushNotification();
//        pushNotification.setMensagemPush("Teste iOS");
//        Destinatary destinatary = new Destinatary();
//        destinatary.setHash("iOSToken");
//        destinatary.setPlataforma("A");
//        pushNotification.setDestinatarios(Arrays.asList(destinatary));
//
//        notificationHubManager.pushNotification(pushNotification);
//
//        verify(notificationHub, times(1)).sendDirectNotification(any(Notification.class), eq(Arrays.asList("iOSToken")));
//    }
//
//    //
//
////    @Test
////    void testPushNotificationAndroid() throws NotificationHubsException {
////        // Mock data
////        MensagemPush pushNotification = new MensagemPush();
////        pushNotification.setMensagemPush("Teste Android");
////        DispositivosResponse destinatary = new DispositivosResponse();
////        destinatary.setHash("AndroidToken");
////        destinatary.setPlataforma("G");
////        pushNotification.setDestinatarios(Arrays.asList(destinatary));
////
////        // Perform the method call
////        pnotService.pushNotification(pushNotification);
////
////        // Verify that Notification.createGcmNotification and notificationHub.sendDirectNotification were called with expected arguments
////        verify(notificationHub, times(1)).sendDirectNotification(ArgumentMatchers.any(Notification.class), eq(Arrays.asList("AndroidToken")));
////    }
////
////    @Test
////    void testPushNotificationiOS() throws NotificationHubsException {
////        // Mock data
////        MensagemPush pushNotification = new MensagemPush();
////        pushNotification.setMensagemPush("Teste iOS");
////        DispositivosResponse destinatary = new DispositivosResponse();
////        destinatary.setHash("iOSToken");
////        destinatary.setPlataforma("A");
////        pushNotification.setDestinatarios(Arrays.asList(destinatary));
////
////        // Perform the method call
////        pnotService.pushNotification(pushNotification);
////
////        // Verify that Notification.createAppleNotification and notificationHub.sendDirectNotification were called with expected arguments
////        verify(notificationHub, times(1)).sendDirectNotification(ArgumentMatchers.any(Notification.class), eq(Arrays.asList("iOSToken")));
////    }
//
//}