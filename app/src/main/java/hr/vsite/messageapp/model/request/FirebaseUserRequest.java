package hr.vsite.messageapp.model.request;

public class FirebaseUserRequest {

    private String firebaseToken;
    private UserRequest userRequest;

    public FirebaseUserRequest(String firebase_token, UserRequest userRequest) {
        this.firebaseToken = firebase_token;
        this.userRequest = userRequest;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public UserRequest getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(UserRequest userRequest) {
        this.userRequest = userRequest;
    }
}
