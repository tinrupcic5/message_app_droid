package hr.vsite.messageapp.model.request;

import java.util.Set;

public class ChatParticipantsRequest {
    ChatRequest chatRequest;
    Set<ParticipantsRequest> participantsRequest;

    public ChatParticipantsRequest(ChatRequest chatRequest, Set<ParticipantsRequest> participantsRequest) {
        this.chatRequest = chatRequest;
        this.participantsRequest = participantsRequest;
    }

    public ChatRequest getChatRequest() {
        return chatRequest;
    }

    public void setChatRequest(ChatRequest chatRequest) {
        this.chatRequest = chatRequest;
    }

    public Set<ParticipantsRequest> getParticipantsRequest() {
        return participantsRequest;
    }

    public void setParticipantsRequest(Set<ParticipantsRequest> participantsRequest) {
        this.participantsRequest = participantsRequest;
    }
}
