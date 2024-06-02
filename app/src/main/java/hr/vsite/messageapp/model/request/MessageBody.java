package hr.vsite.messageapp.model.request;

import java.io.Serializable;

public class MessageBody implements Serializable {


    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
