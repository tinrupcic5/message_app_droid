package hr.vsite.messageapp.model.request;

import java.io.Serializable;

public class MessageRequest implements Serializable {
   private String messageText;
    private ChatRequest chat;
    private UserRequest userSender;

    public MessageRequest(String messageText, ChatRequest chat, UserRequest userSender) {
        this.messageText = messageText;
        this.chat = chat;
        this.userSender = userSender;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public ChatRequest getChat() {
        return chat;
    }

    public void setChat(ChatRequest chat) {
        this.chat = chat;
    }

    public UserRequest getUserSender() {
        return userSender;
    }

    public void setUserSender(UserRequest userSender) {
        this.userSender = userSender;
    }
}
