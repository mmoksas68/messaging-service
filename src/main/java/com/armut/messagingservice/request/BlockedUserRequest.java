package com.armut.messagingservice.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class BlockedUserRequest {
    @NotNull
    @Positive
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "BlockedUserRequest{" +
                "userId=" + userId +
                '}';
    }
}
