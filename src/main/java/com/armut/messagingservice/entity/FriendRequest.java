package com.armut.messagingservice.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class FriendRequest extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;

    private Long senderId;

    private Long receiverId;

    @Column(nullable = false)
    @ColumnDefault("'PENDING'")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public FriendRequestStatus getStatus() {
        return FriendRequestStatus.valueOf(status);
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status.name();
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", lastUpdatedAt=" + lastUpdatedAt +
                '}';
    }

    public enum FriendRequestStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
