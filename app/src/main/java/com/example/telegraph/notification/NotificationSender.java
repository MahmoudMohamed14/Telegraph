package com.example.telegraph.notification;

import com.example.telegraph.model.Data;

public class NotificationSender {
    public Data data;
    public String to;

    public NotificationSender(Data data, String to) {
        this.data = data;
        this.to = to;
    }
}
