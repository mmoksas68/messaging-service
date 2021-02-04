package com.armut.messagingservice.request;

import com.armut.messagingservice.entity.FriendRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class FriendInvitationRequest {
    @NotNull
    @Positive
    private Long receiverId;

    public FriendRequest toFriendRequest(Long senderId) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setReceiverId(receiverId);
        friendRequest.setSenderId(senderId);
        friendRequest.setStatus(FriendRequest.FriendRequestStatus.PENDING);
        return friendRequest;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public String toString() {
        return "FriendInvitationRequest{" +
                "receiverId=" + receiverId +
                '}';
    }
}
