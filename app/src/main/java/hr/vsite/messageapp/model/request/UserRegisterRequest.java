package hr.vsite.messageapp.model.request;

import java.io.Serializable;

public class UserRegisterRequest implements Serializable {
    private String userName;
    private String userEmail;
    private String password;
    private String phoneNumber;
    private FirebaseUserRequest firebaseUserRequest;


    public UserRegisterRequest(String userName, String userEmail, String password, String phoneNumber, FirebaseUserRequest firebaseUserRequest) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.firebaseUserRequest = firebaseUserRequest;
    }

    public FirebaseUserRequest getFirebaseUserRequest() {
        return firebaseUserRequest;
    }

    public void setFirebaseUserRequest(FirebaseUserRequest firebaseUserRequest) {
        this.firebaseUserRequest = firebaseUserRequest;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}