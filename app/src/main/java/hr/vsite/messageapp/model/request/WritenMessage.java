package hr.vsite.messageapp.model.request;

import java.io.Serializable;

public class WritenMessage implements Serializable {


    private String message;
    private Long idFrom;
    private Long idTo;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(Long idFrom) {
        this.idFrom = idFrom;
    }

    public Long getIdTo() {
        return idTo;
    }

    public void setIdTo(Long idTo) {
        this.idTo = idTo;
    }
}
