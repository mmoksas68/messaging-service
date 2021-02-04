package com.armut.messagingservice.service;

import com.armut.messagingservice.entity.Action;
import com.armut.messagingservice.entity.BlockedUser;
import com.armut.messagingservice.entity.FriendList;
import com.armut.messagingservice.entity.FriendRequest;
import com.armut.messagingservice.entity.Message;
import com.armut.messagingservice.entity.User;
import com.armut.messagingservice.exception.NoUserFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperatorService {
    @Autowired
    BlockedUserService blockedUserService;

    @Autowired
    FriendListService friendListService;

    @Autowired
    FriendRequestService friendRequestService;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    ActionService actionService;

    public User addUser(User newUser) {
        User user = null;
        try {
            user = userService.getByUsername(newUser.getUsername());
            actionService.add(Action.newAction(user, Action.ActionType.ADD_USER, Action.ActionStatus.FAILED, "A user with this username already exists."));
            return null;
        } catch (NoUserFoundException e) {
            user = userService.add(newUser);
            actionService.add(Action.newAction(user, Action.ActionType.ADD_USER, Action.ActionStatus.SUCCESSFUL, null));
            return user;
        }
    }

    public FriendRequest sendFriendRequest(FriendRequest friendRequest) throws NoUserFoundException {
        User user = userService.get(friendRequest.getSenderId());
        if (blockedUserService.isBlocked(friendRequest.getReceiverId(), friendRequest.getSenderId())) {
            actionService.add(Action.newAction(user, Action.ActionType.SEND_FRIEND_REQUEST, Action.ActionStatus.FAILED, "Sender is blocked by the receiver."));
            return null;
        }
        if (friendRequestService.checkFriendRequest(friendRequest.getReceiverId(), friendRequest.getSenderId())) {
            actionService.add(Action.newAction(user, Action.ActionType.SEND_FRIEND_REQUEST, Action.ActionStatus.FAILED, "Sender sent friend request before to the receiver."));
            return null;
        }
        actionService.add(Action.newAction(user, Action.ActionType.SEND_FRIEND_REQUEST, Action.ActionStatus.SUCCESSFUL, null));
        return friendRequestService.add(friendRequest);
    }

    public Message sendMessage(Message message) throws NoUserFoundException {
        User user = userService.get(message.getSenderId());
        if (blockedUserService.isBlocked(message.getReceiverId(), message.getSenderId())) {
            actionService.add(Action.newAction(user, Action.ActionType.SEND_MESSAGE, Action.ActionStatus.FAILED, "Sender is blocked by the receiver."));
            return null;
        }
        if (!friendListService.isFriend(message.getReceiverId(), message.getSenderId())) {
            actionService.add(Action.newAction(user, Action.ActionType.SEND_MESSAGE, Action.ActionStatus.FAILED, "Receiver and sender are not friends."));
            return null;
        }
        actionService.add(Action.newAction(user, Action.ActionType.SEND_MESSAGE, Action.ActionStatus.SUCCESSFUL, null));
        return messageService.add(message);

    }

    public boolean addFriend(FriendRequest friendRequest) throws NoUserFoundException {
        User user = userService.get(friendRequest.getReceiverId());
        if (friendListService.isFriend(friendRequest.getReceiverId(), friendRequest.getSenderId())) {
            actionService.add(Action.newAction(user, Action.ActionType.ACCEPT_FRIEND_REQUEST, Action.ActionStatus.FAILED, "Users are already friends."));
            return false;
        }
        if (!friendRequestService.checkPendingFriendRequest(friendRequest.getReceiverId(), friendRequest.getSenderId())) {
            actionService.add(Action.newAction(user, Action.ActionType.ACCEPT_FRIEND_REQUEST, Action.ActionStatus.FAILED, "There is no pending friend request."));
            return false;
        }
        User user1 = userService.get(friendRequest.getReceiverId());
        User user2 = userService.get(friendRequest.getSenderId());
        friendListService.add(new FriendList(user1, user2));
        friendListService.add(new FriendList(user2, user1));
        friendRequestService.updateStatus(friendRequest);
        actionService.add(Action.newAction(user, Action.ActionType.ACCEPT_FRIEND_REQUEST, Action.ActionStatus.SUCCESSFUL, null));
        return true;
    }

    public boolean rejectFriendRequest(FriendRequest friendRequest) throws NoUserFoundException {
        User user = userService.get(friendRequest.getReceiverId());
        if (friendRequestService.checkPendingFriendRequest(friendRequest.getReceiverId(), friendRequest.getSenderId())) {
            actionService.add(Action.newAction(user, Action.ActionType.REJECT_FRIEND_REQUEST, Action.ActionStatus.FAILED, "There is no pending friend request."));
            return false;
        }
        friendRequestService.updateStatus(friendRequest);
        actionService.add(Action.newAction(user, Action.ActionType.REJECT_FRIEND_REQUEST, Action.ActionStatus.SUCCESSFUL, null));
        return true;
    }

    public BlockedUser blockUser(Long userId, Long blockedUserId) throws NoUserFoundException {
        User user = userService.get(userId);
        if (friendListService.isFriend(userId, blockedUserId)) {
            friendListService.deleteFriendship(userId, blockedUserId);
        }
        User blockedUser = userService.get(blockedUserId);
        actionService.add(Action.newAction(user, Action.ActionType.BLOCK_USER, Action.ActionStatus.SUCCESSFUL, null));
        return blockedUserService.add(new BlockedUser(userId, blockedUser));
    }

    public void deleteUser(User user) {
        blockedUserService.deleteAllByUserId(user.getId());
        friendListService.deleteAllByUserIdOrAndFriendId(user.getId());
        friendRequestService.deleteAllBySenderId(user.getId());
        messageService.deleteAllByReceiverId(user.getId());
        userService.delete(user);
    }
}
