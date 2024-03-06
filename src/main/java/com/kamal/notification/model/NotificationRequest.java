package com.kamal.notification.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kamal.notification.NotificationMessage;
import com.kamal.notification.model.DataMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String pnsToken;
    private String clientTag;
    private String platform;
    private NotificationMessage notification;
    private DataMessage data;

}
