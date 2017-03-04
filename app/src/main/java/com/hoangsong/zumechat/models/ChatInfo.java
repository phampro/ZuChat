package com.hoangsong.zumechat.models;

/**
 * Created by HOAI AN on 23/02/2017.
 */

public class ChatInfo {
    private String id;
    private String chat_message;
    private String username;
    private String photo_url;
    private String chat_type;
    private String created_on;
    private String sender_id;
    private int background = 0;
    private boolean showTime = true;

    public ChatInfo(String id, String chat_message, String username, String photo_url, String chat_type, String created_on, String sender_id) {
        this.id = id;
        this.chat_message = chat_message;
        this.username = username;
        this.photo_url = photo_url;
        this.chat_type = chat_type;
        this.created_on = created_on;
        this.sender_id = sender_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChat_message() {
        return chat_message;
    }

    public void setChat_message(String chat_message) {
        this.chat_message = chat_message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getChat_type() {
        return chat_type;
    }

    public void setChat_type(String chat_type) {
        this.chat_type = chat_type;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }
}
