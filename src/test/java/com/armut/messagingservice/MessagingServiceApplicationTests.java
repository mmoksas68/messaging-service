package com.armut.messagingservice;

import com.armut.messagingservice.entity.Action;
import com.armut.messagingservice.entity.BlockedUser;
import com.armut.messagingservice.entity.Errors;
import com.armut.messagingservice.entity.FriendRequest;
import com.armut.messagingservice.entity.Message;
import com.armut.messagingservice.entity.User;
import com.armut.messagingservice.exception.NoMessageFoundException;
import com.armut.messagingservice.exception.NoUserFoundException;
import com.armut.messagingservice.service.ActionService;
import com.armut.messagingservice.service.BlockedUserService;
import com.armut.messagingservice.service.ErrorsService;
import com.armut.messagingservice.service.FriendListService;
import com.armut.messagingservice.service.FriendRequestService;
import com.armut.messagingservice.service.MessageService;
import com.armut.messagingservice.service.OperatorService;
import com.armut.messagingservice.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class MessagingServiceApplicationTests {
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
	OperatorService operatorService;

	@Autowired
	ActionService actionService;

	@Autowired
	ErrorsService errorsService;

	@Test
	void testOperatorService() throws NoUserFoundException, NoMessageFoundException {
		// create users
		User user1 = new User();
		user1.setUsername("deneme1");
		user1.setEmail("deneme1@gmail.com");
		user1.setPassword("123456");

		User user2 = new User();
		user2.setUsername("deneme2");
		user2.setEmail("deneme2@gmail.com");
		user2.setPassword("123456");

		User user3 = new User();
		user3.setUsername("deneme3");
		user3.setEmail("deneme3@gmail.com");
		user3.setPassword("123456");

		User user4 = new User();
		user4.setUsername("deneme4");
		user4.setEmail("deneme4@gmail.com");
		user4.setPassword("123456");

		User addedUser1 = operatorService.addUser(user1);
		User addedUser2 = operatorService.addUser(user2);
		User addedUser3 = operatorService.addUser(user3);
		User addedUser4 = operatorService.addUser(user4);
		User addUser1Again = operatorService.addUser(user1);

		// test friend request

			// create friend requests
		FriendRequest friendRequest1 = new FriendRequest();
		friendRequest1.setStatus(FriendRequest.FriendRequestStatus.PENDING);
		friendRequest1.setSenderId(addedUser1.getId());
		friendRequest1.setReceiverId(addedUser2.getId());

		FriendRequest friendRequest2 = new FriendRequest();
		friendRequest2.setStatus(FriendRequest.FriendRequestStatus.PENDING);
		friendRequest2.setSenderId(addedUser1.getId());
		friendRequest2.setReceiverId(addedUser3.getId());

		FriendRequest friendRequest4 = new FriendRequest();
		friendRequest4.setStatus(FriendRequest.FriendRequestStatus.PENDING);
		friendRequest4.setSenderId(addedUser2.getId());
		friendRequest4.setReceiverId(addedUser4.getId());

		FriendRequest friendRequest5 = new FriendRequest();
		friendRequest5.setStatus(FriendRequest.FriendRequestStatus.PENDING);
		friendRequest5.setSenderId(addedUser3.getId());
		friendRequest5.setReceiverId(addedUser4.getId());

		operatorService.sendFriendRequest(friendRequest1);
		operatorService.sendFriendRequest(friendRequest2);
		operatorService.sendFriendRequest(friendRequest1);

		Assert.assertEquals(true, friendRequestService.checkPendingFriendRequest(addedUser2.getId(), addedUser1.getId()));
		Assert.assertEquals(true, friendRequestService.checkPendingFriendRequest(addedUser3.getId(), addedUser1.getId()));
		Assert.assertEquals(false, friendRequestService.checkPendingFriendRequest(addedUser2.getId(), addedUser3.getId()));

			// accept friend request
		FriendRequest acceptRequest = new FriendRequest();
		acceptRequest.setReceiverId(friendRequest1.getReceiverId());
		acceptRequest.setSenderId(friendRequest1.getSenderId());
		acceptRequest.setStatus(FriendRequest.FriendRequestStatus.APPROVED);
		operatorService.addFriend(friendRequest1);

		friendRequest5.setStatus(FriendRequest.FriendRequestStatus.APPROVED);
		acceptRequest.setReceiverId(friendRequest5.getReceiverId());
		acceptRequest.setSenderId(friendRequest5.getSenderId());
		acceptRequest.setStatus(FriendRequest.FriendRequestStatus.APPROVED);
		operatorService.addFriend(friendRequest5);

		Assert.assertEquals(true, friendListService.isFriend(addedUser1.getId(), addedUser2.getId()));
		Assert.assertEquals(false, friendListService.isFriend(addedUser3.getId(), addedUser4.getId()));

			// reject friend request
		friendRequest2.setStatus(FriendRequest.FriendRequestStatus.REJECTED);
		operatorService.rejectFriendRequest(friendRequest2);

		Assert.assertEquals(false, friendListService.isFriend(addedUser3.getId(), addedUser1.getId()));

		// test block user

		operatorService.blockUser(addedUser3.getId(), addedUser4.getId());

		Assert.assertEquals(true, blockedUserService.isBlocked(addedUser3.getId(), addedUser4.getId()));
		Assert.assertEquals(false, blockedUserService.isBlocked(addedUser4.getId(), addedUser3.getId()));

			// test sending friend request to a blocked user
		FriendRequest friendRequest3 = new FriendRequest();
		friendRequest3.setStatus(FriendRequest.FriendRequestStatus.PENDING);
		friendRequest3.setSenderId(addedUser4.getId());
		friendRequest3.setReceiverId(addedUser3.getId());

		operatorService.sendFriendRequest(friendRequest3);

		Assert.assertEquals(false, friendRequestService.checkFriendRequest(addedUser3.getId(), addedUser4.getId()));

		// test message

		Message message1 = new Message();
		message1.setReceiverId(addedUser2.getId());
		message1.setSenderId(addedUser1.getId());
		message1.setSenderUsername(addedUser1.getUsername());
		message1.setBody("deneme mesaj");

		Message message2 = new Message();
		message2.setReceiverId(addedUser1.getId());
		message2.setSenderId(addedUser2.getId());
		message2.setSenderUsername(addedUser2.getUsername());
		message2.setBody("deneme cevap");

		Message messageToUpdate = operatorService.sendMessage(message1);
		operatorService.sendMessage(message2);

		Assert.assertEquals(2, messageService.getDialog(addedUser1.getId(), addedUser2.getId()).size());

			// test sending message to non friend user
		Message message3 = new Message();
		message3.setReceiverId(addedUser4.getId());
		message3.setSenderId(addedUser1.getId());
		message3.setSenderUsername(addedUser1.getUsername());
		message3.setBody("deneme mesaj 2");

		operatorService.sendMessage(message3);

		Assert.assertEquals(0, messageService.getDialog(addedUser1.getId(), addedUser4.getId()).size());

			// test sending message to a blocked user
		Message message4 = new Message();
		message4.setReceiverId(addedUser3.getId());
		message4.setSenderId(addedUser4.getId());
		message4.setSenderUsername(addedUser4.getUsername());
		message4.setBody("deneme mesaj 3");

		operatorService.sendMessage(message4);

		Assert.assertEquals(0, messageService.getDialog(addedUser3.getId(), addedUser4.getId()).size());

			// test message update
		messageToUpdate.setBody("updated");
		messageService.update(messageToUpdate);
		Message updatedMessage = messageService.get(messageToUpdate.getId());

		Assert.assertEquals(updatedMessage.getBody(), messageToUpdate.getBody());

			// test message delete
		Assert.assertEquals(2,messageService.getAllById(addedUser2.getId()).size());
		messageService.delete(updatedMessage);
		Assert.assertEquals(1,messageService.getAllById(addedUser2.getId()).size());

		// test blocking a friend
		operatorService.blockUser(addedUser1.getId(), addedUser2.getId());

		Assert.assertEquals(true, blockedUserService.isBlocked(addedUser1.getId(), addedUser2.getId()));
		Assert.assertEquals(0, friendListService.getAllByUserId(addedUser1.getId()).size());
		Assert.assertEquals(1, friendListService.getAllByUserId(addedUser2.getId()).size());

		// test delete user

		User userToDelete = userService.get(addedUser1.getId());

		operatorService.deleteUser(userToDelete);

		Assert.assertEquals(3, userService.getAll().size());

		// test actions

		List<Action> allActions = actionService.getAll();
		List<Action> allActionsOfUser1 = actionService.getAllByUserId(addedUser1.getId());
		List<Action> allSendMessageActionsOfUser1 = actionService.getAllByUserIdAndActionType(addedUser1.getId(), Action.ActionType.SEND_MESSAGE);

		Assert.assertEquals(18, allActions.size());
		Assert.assertEquals(8, allActionsOfUser1.size());
		Assert.assertEquals(2, allSendMessageActionsOfUser1.size());
	}

	@Test
	void testUser() throws NoUserFoundException {
		// test add
		User user1 = new User();
		user1.setUsername("deneme1");
		user1.setEmail("deneme1@gmail.com");
		user1.setPassword("123456");

		User user2 = new User();
		user2.setUsername("deneme2");
		user2.setEmail("deneme2@gmail.com");
		user2.setPassword("123456");

		User addedUser1 = userService.add(user1);
		User addedUser2 = userService.add(user2);

		Assert.assertEquals(user1.getUsername(),userService.get(addedUser1.getId()).getUsername());
		Assert.assertEquals(user2.getUsername(),userService.get(addedUser2.getId()).getUsername());
		Assert.assertEquals(2, userService.getAll().size());

		// test update
		addedUser1.setEmail("changed@gmail.com");
		addedUser2.setUsername("changed2");

		User updatedUser1 = userService.update(addedUser1);
		User updatedUser2 = userService.update(addedUser2);

		Assert.assertEquals(updatedUser1.getEmail(), userService.get(addedUser1.getId()).getEmail());
		Assert.assertEquals(updatedUser2.getUsername(), userService.get(addedUser2.getId()).getUsername());

		// test delete
		userService.delete(updatedUser1);
		userService.delete(updatedUser2);

		Assert.assertEquals(0, userService.getAll().size());

		// test errors
		Errors errors = Errors.newError(addedUser1.getId(), addedUser1.getUsername(), "error");
		errorsService.add(errors);

		Assert.assertEquals(1, errorsService.getAll().size());
		Assert.assertEquals(1, errorsService.getAllByUserId(addedUser1.getId()).size());
	}

	@Test
	void testBlockedUser() {
		// test add block
		User user1 = new User();
		user1.setUsername("deneme1");
		user1.setEmail("deneme1@gmail.com");
		user1.setPassword("123456");

		User user2 = new User();
		user2.setUsername("deneme2");
		user2.setEmail("deneme2@gmail.com");
		user2.setPassword("123456");

		User addedUser1 = userService.add(user1);
		User addedUser2 = userService.add(user2);

		BlockedUser blockedUser = new BlockedUser();
		blockedUser.setUserId(addedUser1.getId());
		blockedUser.setBlockedUserId(addedUser2.getId());
		blockedUser.setBlockedUserUsername(addedUser2.getUsername());

		blockedUserService.add(blockedUser);

		Assert.assertEquals(true, blockedUserService.isBlocked(addedUser1.getId(), addedUser2.getId()));
		Assert.assertEquals(1, blockedUserService.getAllByUserId(addedUser1.getId()).size());

		// test delete
		BlockedUser blockedUser2 = new BlockedUser();
		blockedUser2.setUserId(addedUser2.getId());
		blockedUser2.setBlockedUserId(addedUser1.getId());
		blockedUser2.setBlockedUserUsername(addedUser1.getUsername());

		blockedUserService.add(blockedUser2);
		blockedUserService.deleteAllByUserId(addedUser1.getId());
		blockedUserService.deleteByUserIdAndAndBlockedUserId(addedUser2.getId(), addedUser1.getId());

		Assert.assertEquals(0, blockedUserService.getAllByUserId(addedUser1.getId()).size());
		Assert.assertEquals(0, blockedUserService.getAllByUserId(addedUser2.getId()).size());	
	}
}
