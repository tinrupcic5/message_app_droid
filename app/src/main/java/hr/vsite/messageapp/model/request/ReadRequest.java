package hr.vsite.messageapp.model.request;

import java.io.Serializable;

public  class ReadRequest implements Serializable {

    private boolean isRead;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
