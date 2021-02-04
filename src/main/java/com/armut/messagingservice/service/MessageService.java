package com.armut.messagingservice.service;

import com.armut.messagingservice.entity.BaseEntity;
import com.armut.messagingservice.entity.Message;
import com.armut.messagingservice.exception.NoMessageFoundException;
import com.armut.messagingservice.exception.NoUserFoundException;
import com.armut.messagingservice.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message get(Long id) throws NoMessageFoundException {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isPresent())
            return message.get();
        else {
            throw  new NoMessageFoundException();
        }
    }

    public List<Message> getAllById(Long id) {
        List<Message> messages = messageRepository.findAllByReceiverId(id);
        return messages;
    }

    public List<Message> getDialog(Long userId1, Long userId2) {
        List<Message> messages = messageRepository.findAllBySenderIdAndReceiverId(userId2, userId1);
        List<Message> messages2 = messageRepository.findAllBySenderIdAndReceiverId(userId1, userId2);
        messages.addAll(messages2);
        Collections.sort(messages, Comparator.comparing(BaseEntity::getCreatedAt));
        return messages;
    }

    public Message add(Message message) {
        Message addedMessage = messageRepository.save(message);
        return message;
    }

    public Message update(Message message) {
        Message updatedMessage = messageRepository.save(message);
        return updatedMessage;
    }

    public void delete(Message message) {
        messageRepository.delete(message);
    }

    public void deleteAllByReceiverId(Long receiverId) {
        messageRepository.deleteAllByReceiverId(receiverId);
    }
}
