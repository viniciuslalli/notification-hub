package com.kamal.notification.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kamal.notification.NotificationMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendDirectionRequest {
    private NotificationMessage notification;
    private DataMessage data;
    private List<String> tokens;
}
