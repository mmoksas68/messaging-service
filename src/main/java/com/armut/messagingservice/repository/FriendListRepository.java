package com.armut.messagingservice.repository;

import com.armut.messagingservice.entity.FriendList;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendListRepository extends CrudRepository<FriendList, Long> {
    FriendList findByUserIdAndFriendId(Long userId, Long friendId);

    List<FriendList> findAllByUserId(Long id);

    void deleteAllByUserIdOrAndFriendId(Long userId, Long friendId);

    void deleteByUserIdAndFriendId(Long userId, Long friendId);
}
