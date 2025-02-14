package com.chat_app.chatapp.services;


import com.chat_app.chatapp.models.Message;
import com.chat_app.chatapp.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void sendMessageToUser(Long senderId, Long recipientId, String content) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setRecipientId(recipientId);
        message.setContent(content);
        messageRepository.sendMessage(message);
    }

    public void sendMessageToChannel(Long senderId, Long channelId, String content) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setChannelId(channelId);
        message.setContent(content);
        messageRepository.sendMessage(message);
    }

    public List<Message> getMessagesBetweenUsers(Long senderId, Long recipientId) {
        return messageRepository.getMessagesBetweenUsersWithSenderName(senderId, recipientId);
    }

    public List<Message> getAllMessagesInChannel(Long channelId) {
        return messageRepository.getAllMessagesInChannel(channelId);
    }

    public void softDeleteMessage(Long messageId, Long senderId) {
        messageRepository.softDeleteMessage(messageId, senderId);
    }
}