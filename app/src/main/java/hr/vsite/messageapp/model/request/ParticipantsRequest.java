package hr.vsite.messageapp.model.request;

public class ParticipantsRequest {
    ChatRequest chat;
    UserRequest user;

    public ParticipantsRequest(ChatRequest chat, UserRequest user) {
        this.chat = chat;
        this.user = user;
    }

    public ChatRequest getChat() {
        return chat;
    }

    public void setChat(ChatRequest chat) {
        this.chat = chat;
    }

    public UserRequest getUser() {
        return user;
    }

    public void setUser(UserRequest user) {
        this.user = user;
    }
}
