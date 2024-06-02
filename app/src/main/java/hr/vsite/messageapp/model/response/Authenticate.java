package hr.vsite.messageapp.model.response;

import androidx.room.ColumnInfo;

import java.io.Serializable;
import java.util.List;

public class Authenticate implements Serializable {

    @ColumnInfo(name="id")
    private Long id;
    @ColumnInfo(name="refreshToken")
    private String refreshToken;
    @ColumnInfo(name="accessToken")
    private String accessToken;
    @ColumnInfo(name="username")
    private String username;
    @ColumnInfo(name="email")
    private String email;
    @ColumnInfo(name="roles")
    private List<String> roles;
    @ColumnInfo(name="tokenType")
    private String tokenType;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
