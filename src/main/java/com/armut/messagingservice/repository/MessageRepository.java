package com.armut.messagingservice.repository;

import com.armut.messagingservice.entity.FriendRequest;
import com.armut.messagingservice.entity.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Message findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    List<Message> findAllByReceiverId(Long id);

    List<Message> findAllBySenderIdAndReceiverId(Long senderId, Long receiverId);

    void deleteAllByReceiverId(Long id);
}
