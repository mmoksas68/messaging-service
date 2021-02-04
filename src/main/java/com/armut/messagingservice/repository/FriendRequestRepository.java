package com.armut.messagingservice.repository;

import com.armut.messagingservice.entity.FriendRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {
    FriendRequest findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    FriendRequest findBySenderIdAndReceiverIdAndStatus(Long senderId, Long receiverId, String status);

    List<FriendRequest> findAllByReceiverId(Long id);

    void deleteAllBySenderId(Long id);

    void deleteBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
