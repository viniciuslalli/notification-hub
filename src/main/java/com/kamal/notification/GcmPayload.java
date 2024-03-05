package com.kamal.notification;

import java.util.List;

public class GcmPayload {

    private List<String> deviceTokens;
    private String message;

    // getters e setters
    public List<String> getDeviceTokens() {
        return deviceTokens;
    }

    public void setDeviceTokens(List<String> deviceTokens) {
        this.deviceTokens = deviceTokens;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
