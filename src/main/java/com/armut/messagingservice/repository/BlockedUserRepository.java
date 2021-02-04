package com.armut.messagingservice.repository;

import com.armut.messagingservice.entity.BlockedUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlockedUserRepository extends CrudRepository<BlockedUser, Long> {
    BlockedUser findByUserIdAndBlockedUserId(Long userId, Long blockedUserId);

    List<BlockedUser> findAllByUserId(Long id);

    void deleteAllByUserId(Long id);

    void deleteByUserIdAndAndBlockedUserId(Long userId, Long blockedUserId);
}
