package com.armut.messagingservice.controller;

import com.armut.messagingservice.entity.BlockedUser;
import com.armut.messagingservice.entity.Errors;
import com.armut.messagingservice.entity.FriendList;
import com.armut.messagingservice.entity.FriendRequest;
import com.armut.messagingservice.entity.User;
import com.armut.messagingservice.exception.NoUserFoundException;
import com.armut.messagingservice.request.BlockedUserRequest;
import com.armut.messagingservice.request.FriendInvitationReplyRequest;
import com.armut.messagingservice.request.FriendInvitationRequest;
import com.armut.messagingservice.security.CustomUserDetailsService;
import com.armut.messagingservice.service.BlockedUserService;
import com.armut.messagingservice.service.ErrorsService;
import com.armut.messagingservice.service.FriendListService;
import com.armut.messagingservice.service.FriendRequestService;
import com.armut.messagingservice.service.OperatorService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    @Autowired
    private FriendListService friendListService;

    @Autowired
    private BlockedUserService blockedUserService;

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    ErrorsService errorsService;

    @ApiOperation("Get all friends of the current user")
    @GetMapping
    public ResponseEntity<List<FriendList>> getAllFriendsOfCurrentUser() throws NoUserFoundException {
        User currentUser = customUserDetailsService.getCurrentUser();
        List<FriendList> friendLists = friendListService.getAllByUserId(currentUser.getId());
        return new ResponseEntity<>(friendLists, HttpStatus.OK);
    }

    // Friend Request

    @ApiOperation("Get all friend requests sent to the current user")
    @GetMapping("/friend-requests")
    public ResponseEntity<List<FriendRequest>> getAllFriendRequestOfCurrentUser() throws NoUserFoundException {
        User currentUser = customUserDetailsService.getCurrentUser();
        List<FriendRequest> friendRequests = friendRequestService.getAllByReceiverId(currentUser.getId());
        return new ResponseEntity<>(friendRequests, HttpStatus.OK);
    }

    @ApiOperation("Send friend request to a user from the current user")
    @PostMapping("/friend-requests")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendInvitationRequest request) throws NoUserFoundException {
        User currentUser = customUserDetailsService.getCurrentUser();
        try {
            FriendRequest friendRequest = operatorService.sendFriendRequest(request.toFriendRequest(currentUser.getId()));
            return new ResponseEntity<>(friendRequest, HttpStatus.OK);
        } catch (NoUserFoundException e) {
            errorsService.add(Errors.newError(request.getReceiverId(), null, e.getMessage()));
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @ApiOperation("Reply a friend request by accepting or rejecting")
    @PatchMapping("/friend-requests")
    public ResponseEntity<?> replyFriendRequest(@RequestParam FriendRequest.FriendRequestStatus status, @RequestBody FriendInvitationReplyRequest request) {
        try {
            if (status.equals(FriendRequest.FriendRequestStatus.APPROVED)) {
                User currentUser = customUserDetailsService.getCurrentUser();
                if (operatorService.addFriend(request.toFriendRequest(currentUser.getId(), status)))
                    return new ResponseEntity<>(HttpStatus.OK);
                else
                    return null;
            } else if (status.equals(FriendRequest.FriendRequestStatus.REJECTED)) {
                User currentUser = customUserDetailsService.getCurrentUser();
                if (operatorService.rejectFriendRequest(request.toFriendRequest(currentUser.getId(), status)))
                    return new ResponseEntity<>(HttpStatus.OK);
                else
                    return null;
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(NoUserFoundException e) {
            errorsService.add(Errors.newError(request.getSenderId(), null, e.getMessage()));
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    // Block User

    @ApiOperation("Get all blocked users of the current user")
    @GetMapping("/blocked-users")
    public ResponseEntity<List<BlockedUser>> getAllBlockedUsersOfCurrentUser() throws NoUserFoundException {
        User currentUser = customUserDetailsService.getCurrentUser();
        List<BlockedUser> blockedUsers = blockedUserService.getAllByUserId(currentUser.getId());
        return new ResponseEntity<>(blockedUsers, HttpStatus.OK);
    }

    @ApiOperation("Block a user for the current user")
    @PostMapping("/blocked-users")
    public ResponseEntity<?> blockUser(@RequestBody BlockedUserRequest request) throws NoUserFoundException {
        User currentUser = customUserDetailsService.getCurrentUser();
        try {
            BlockedUser blockedUsers = operatorService.blockUser(currentUser.getId(), request.getUserId());
            return new ResponseEntity<>(blockedUsers, HttpStatus.OK);
        } catch (NoUserFoundException e) {
            errorsService.add(Errors.newError(request.getUserId(), null, e.getMessage()));
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @ApiOperation("Remove block of current user from a user")
    @PostMapping("/blocked-users/remove")
    public ResponseEntity<?> removeBlockUser(@RequestBody BlockedUserRequest request) throws NoUserFoundException {
        User currentUser = customUserDetailsService.getCurrentUser();
        blockedUserService.deleteByUserIdAndAndBlockedUserId(currentUser.getId(), request.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
