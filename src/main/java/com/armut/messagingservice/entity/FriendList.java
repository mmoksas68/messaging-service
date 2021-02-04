package com.armut.messagingservice.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class FriendList extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private Long friendId;

    private String friendUsername;

    public FriendList() {
    }

    public FriendList (User user1, User user2) {
        this.setUserId(user1.getId());
        this.setFriendId(user2.getId());
        this.setFriendUsername(user2.getUsername());
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

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    @Override
    public String toString() {
        return "FriendList{" +
                "id=" + id +
                ", userId=" + userId +
                ", friendId=" + friendId +
                ", friendUsername='" + friendUsername + '\'' +
                ", createdAt=" + createdAt +
                ", lastUpdatedAt=" + lastUpdatedAt +
                '}';
    }
}
