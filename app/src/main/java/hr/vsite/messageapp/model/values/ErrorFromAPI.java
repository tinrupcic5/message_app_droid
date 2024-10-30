package hr.vsite.messageapp.model.values;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ErrorFromAPI implements Serializable {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("message")
    @Expose
    private String message;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
