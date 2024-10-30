package hr.vsite.messageapp.model.transferModel;

import java.io.Serializable;

import hr.vsite.messageapp.model.response.ChatDto;
import hr.vsite.messageapp.model.response.UserChatDto;
import hr.vsite.messageapp.model.response.UserDto;

/**
 * klasa za prijenos podataka
 */
public class TransferModel implements Serializable {
    private String username;
    private UserDto userDto;
    private ChatDto chatDto;
    private UserChatDto userChatDto;

    private Integer id;

    public TransferModel(Integer id) {
        this.id = id;
    }

    public TransferModel(ChatDto chatDto,UserDto userDto) {
        this.chatDto = chatDto;
        this.userDto = userDto;
    }

    public TransferModel(UserDto userDto) {
        this.userDto = userDto;
    }

    public TransferModel(UserChatDto userChatDto, UserDto userDto) {
        this.userChatDto = userChatDto;
        this.userDto = userDto;
    }

    public UserChatDto getUserChatDto() {
        return userChatDto;
    }

    public void setUserChatDto(UserChatDto userChatDto) {
        this.userChatDto = userChatDto;
    }

    public ChatDto getChatDto() {
        return chatDto;
    }

    public void setChatDto(ChatDto chatDto) {
        this.chatDto = chatDto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
