package com.hoangsong.zumechat.models;

/**
 * Created by HOAI AN on 23/02/2017.
 */

public class BroadcastSignalRInfo {
    private int type;
    private int action;
    private ChatInfo chat;

    public BroadcastSignalRInfo(int type, int action, ChatInfo chat) {
        this.type = type;
        this.action = action;
        this.chat = chat;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public ChatInfo getChat() {
        return chat;
    }

    public void setChat(ChatInfo chat) {
        this.chat = chat;
    }
}
