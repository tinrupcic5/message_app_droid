package hr.vsite.messageapp.model.request;

import java.io.Serializable;

public class ChatRequest implements Serializable {

    private Integer chatId;
    private String chatName;
    private UserRequest userCreator;

    public ChatRequest(Integer chatId, String chatName, UserRequest userCreator) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.userCreator = userCreator;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public UserRequest getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(UserRequest userCreator) {
        this.userCreator = userCreator;
    }
}
