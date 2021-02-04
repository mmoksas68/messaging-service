package com.armut.messagingservice.service;

import com.armut.messagingservice.entity.FriendRequest;
import com.armut.messagingservice.repository.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendRequestService {
    @Autowired
    private FriendRequestRepository friendRequestRepository;

    public List<FriendRequest> getAllByReceiverId(Long id) {
        List<FriendRequest> friendRequests = friendRequestRepository.findAllByReceiverId(id);
        return friendRequests;
    }

    public FriendRequest add(FriendRequest friendRequest) {
        FriendRequest addedFriendRequest = friendRequestRepository.save(friendRequest);
        return addedFriendRequest;
    }

    public Boolean checkFriendRequest(Long receiverId, Long senderId) {
        FriendRequest friendRequest = friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId);
        return friendRequest != null;
    }

    public Boolean checkPendingFriendRequest(Long receiverId, Long senderId) {
        FriendRequest friendRequest =
                friendRequestRepository.findBySenderIdAndReceiverIdAndStatus(senderId,
                                                                            receiverId,
                                                                            FriendRequest.FriendRequestStatus.PENDING.name());
        return friendRequest != null;
    }

    public FriendRequest updateStatus(FriendRequest friendRequest) {
        FriendRequest request = friendRequestRepository.findBySenderIdAndReceiverId(friendRequest.getSenderId(), friendRequest.getReceiverId());
        if (request != null && request.getStatus().equals(FriendRequest.FriendRequestStatus.PENDING)) {
            request.setStatus(friendRequest.getStatus());
            FriendRequest updatedFriendRequest = friendRequestRepository.save(request);
            return updatedFriendRequest;
        }
        return null;
    }

    public void deleteAllBySenderId(Long senderId) {
        friendRequestRepository.deleteAllBySenderId(senderId);
    }
}
