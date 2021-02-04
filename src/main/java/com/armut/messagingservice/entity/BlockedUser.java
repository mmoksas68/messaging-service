package com.armut.messagingservice.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class BlockedUser extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private Long blockedUserId;

    private String blockedUserUsername;

    public BlockedUser() {
    }

    public BlockedUser(Long userId, User userToBlock) {
        this.userId = userId;
        this.blockedUserId = userToBlock.getId();
        this.blockedUserUsername = userToBlock.getUsername();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBlockedUserId() {
        return blockedUserId;
    }

    public void setBlockedUserId(Long blockedUserId) {
        this.blockedUserId = blockedUserId;
    }

    public String getBlockedUserUsername() {
        return blockedUserUsername;
    }

    public void setBlockedUserUsername(String blockedUserUsername) {
        this.blockedUserUsername = blockedUserUsername;
    }

    @Override
    public String toString() {
        return "BlockedUser{" +
                "id=" + id +
                ", userId=" + userId +
                ", blockedUserId=" + blockedUserId +
                ", blockedUserUsername='" + blockedUserUsername + '\'' +
                ", createdAt=" + createdAt +
                ", lastUpdatedAt=" + lastUpdatedAt +
                '}';
    }
}
