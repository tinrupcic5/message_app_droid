package hr.vsite.messageapp.model.request;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class LogOutRequest implements Serializable {

    @NotNull
    private Integer userId;

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LogOutRequest(@NotNull Integer userId) {
        this.userId = userId;
    }
}
