package com.armut.messagingservice.service;

import com.armut.messagingservice.entity.FriendList;
import com.armut.messagingservice.repository.FriendListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendListService {
    @Autowired
    private FriendListRepository friendListRepository;

    public List<FriendList> getAllByUserId(Long id) {
        List<FriendList> blockedUsers = friendListRepository.findAllByUserId(id);
        return blockedUsers;
    }

    public FriendList add(FriendList friendList) {
        FriendList addedFriendList = friendListRepository.save(friendList);
        return addedFriendList;
    }

    public Boolean isFriend(Long userId, Long friendId) {
        FriendList friendList = friendListRepository.findByUserIdAndFriendId(userId, friendId);
        return friendList != null;
    }

    public void deleteAllByUserIdOrAndFriendId(Long userId) {
        friendListRepository.deleteAllByUserIdOrAndFriendId(userId, userId);
    }

    public void deleteFriendship(Long userId1, Long userId2) {
        friendListRepository.deleteByUserIdAndFriendId(userId1, userId2);
        friendListRepository.deleteByUserIdAndFriendId(userId1, userId2);
    }
}
