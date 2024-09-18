package org.example.service;

import org.example.model.Message;
import org.example.repository.MessageRepository;

import java.time.LocalDateTime;

public class MessageService {
    private final MessageRepository messageRepository = new MessageRepository();

    public Message createMessage(String text) {
        Message message = new Message(null, LocalDateTime.now(), text);
        Long id = messageRepository.create(message.getTime(), message.getText());
        message.setId(id);
        return message;
    }

    public int getMessagesCount() {
        return messageRepository.getMessagesCount();
    }

}
