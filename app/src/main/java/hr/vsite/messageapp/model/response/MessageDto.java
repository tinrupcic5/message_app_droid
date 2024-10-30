package hr.vsite.messageapp.model.response;


import java.io.Serializable;

public class MessageDto implements Serializable {
    private Long messageId;

    private String messageText;
    private String createdAt;

    private boolean isRead;
    private ChatDto chatDto;

    private UserDto userSender;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public ChatDto getChatDto() {
        return chatDto;
    }

    public void setChatDto(ChatDto chatDto) {
        this.chatDto = chatDto;
    }

    public UserDto getUserSender() {
        return userSender;
    }

    public void setUserSender(UserDto userSender) {
        this.userSender = userSender;
    }
}
