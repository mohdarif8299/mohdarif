package com.chat.sharechat;

public class ChatMessage {
    private String image, message;

    public String getImage() {
        return image;
    }

    public void setImage(String username) {
        this.image = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ChatMessage(String image, String message, boolean status) {
        this.image = image;
        this.message = message;
        this.status = status;
    }

    private boolean status;

    public ChatMessage() {
    }
}
