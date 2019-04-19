package com.android.israr.myfirebasechatapp;

import java.util.List;

public class Conversation {
    private String conversationUsername;
    private String conversationName;
    private List<ChatMessage> messages;

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public String getConversationUsername() {
        return conversationUsername;
    }

    public void setConversationUsername(String conversationUsername) {
        this.conversationUsername = conversationUsername;
    }


}
