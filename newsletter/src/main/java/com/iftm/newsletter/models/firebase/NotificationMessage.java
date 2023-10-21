package com.iftm.newsletter.models.firebase;

import java.io.Serializable;
import java.util.Map;

public class NotificationMessage implements Serializable {

    private String title;
    private String body;
    private String image;
    private Map<String, String> data;

    public NotificationMessage() {
    }

    public NotificationMessage(String title, String body, String image, Map<String, String> data) {
        this.title = title;
        this.body = body;
        this.image = image;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

}
