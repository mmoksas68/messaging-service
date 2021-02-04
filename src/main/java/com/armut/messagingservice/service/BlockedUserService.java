package com.armut.messagingservice.service;

import com.armut.messagingservice.entity.BlockedUser;
import com.armut.messagingservice.repository.BlockedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockedUserService {
    @Autowired
    private BlockedUserRepository blockedUserRepository;

    public List<BlockedUser> getAllByUserId(Long id) {
        List<BlockedUser> blockedUsers = blockedUserRepository.findAllByUserId(id);
        return blockedUsers;
    }

    public BlockedUser add(BlockedUser blockedUser) {
        BlockedUser addedBlockedUser = blockedUserRepository.save(blockedUser);
        return addedBlockedUser;
    }

    public Boolean isBlocked(Long userId, Long blockedUserId){
       BlockedUser blockedUser = blockedUserRepository.findByUserIdAndBlockedUserId(userId, blockedUserId);
       return blockedUser != null;
    }

    public void deleteByUserIdAndAndBlockedUserId(Long userId, Long blockedUserId) {
        blockedUserRepository.deleteByUserIdAndAndBlockedUserId(userId, blockedUserId);
    }

    public void deleteAllByUserId(Long userId) {
        blockedUserRepository.deleteAllByUserId(userId);
    }
}
