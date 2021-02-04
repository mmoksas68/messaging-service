package com.armut.messagingservice.request;

import com.armut.messagingservice.entity.FriendRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class FriendInvitationReplyRequest {
    @NotNull
    @Positive
    private Long senderId;

    public FriendRequest toFriendRequest(Long receiverId, FriendRequest.FriendRequestStatus status) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setReceiverId(receiverId);
        friendRequest.setSenderId(senderId);
        friendRequest.setStatus(status);
        return friendRequest;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    @Override
    public String toString() {
        return "FriendInvitationReplyRequest{" +
                "receiverId=" + senderId +
                '}';
    }
}
