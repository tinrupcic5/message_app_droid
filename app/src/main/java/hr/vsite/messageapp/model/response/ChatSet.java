package hr.vsite.messageapp.model.response;

import java.io.Serializable;
import java.util.Set;

public class ChatSet implements Serializable {


    private Set<ChatDto> chats;

    public Set<ChatDto> getChats() {
        return chats;
    }

    public void setChats(Set<ChatDto> chats) {
        this.chats = chats;
    }
}
