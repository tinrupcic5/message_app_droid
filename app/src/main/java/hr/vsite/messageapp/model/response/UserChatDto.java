package hr.vsite.messageapp.model.response;


import java.io.Serializable;

public class UserChatDto implements Serializable {
    private ChatDto chatDto;
    private UserDto userDto;

    public UserChatDto(ChatDto chatDto, UserDto userDto) {
        this.chatDto = chatDto;
        this.userDto = userDto;
    }

    public ChatDto getChatDto() {
        return chatDto;
    }

    public void setChatDto(ChatDto chatDto) {
        this.chatDto = chatDto;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}
