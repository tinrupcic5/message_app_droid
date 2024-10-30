package hr.vsite.messageapp.model.response;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hr.vsite.messageapp.utils.Variables;

public class User implements Serializable {


    private Integer id;
    private String email;
    private String username;
    private Set<Role> roles;
    private UserInfo userInfo;


    public User(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
