package hr.vsite.messageapp.model.request;

import java.io.Serializable;

public class RefreshToken implements Serializable {

    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
