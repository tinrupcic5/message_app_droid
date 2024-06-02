package hr.vsite.messageapp.model.response;


import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class ChatDto implements Serializable {
    private int chatId;
    private String chatName;
    private String createdAt;
    private UserDto userCreator;

    private List<MessageDto> messageDto;

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public UserDto getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(UserDto userCreator) {
        this.userCreator = userCreator;
    }

    public List<MessageDto> getMessageDto() {
        return messageDto;
    }

    public void setMessageDto(List<MessageDto> messageDto) {
        this.messageDto = messageDto;
    }
}