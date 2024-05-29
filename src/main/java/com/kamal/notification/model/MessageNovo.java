package com.kamal.notification.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Getter
public class MessageNovo {
    private String to;
    private AndroidNotification notification;
    private NewDataTemplate data;
}
