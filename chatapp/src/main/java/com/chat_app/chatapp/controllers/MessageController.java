package com.chat_app.chatapp.controllers;


import com.chat_app.chatapp.models.Message;
import com.chat_app.chatapp.services.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/user")
    public ResponseEntity<String> sendMessageToUser(@RequestBody Message message) {
        messageService.sendMessageToUser(message.getSenderId(), message.getRecipientId(), message.getContent());
        return ResponseEntity.ok("Message sent to user successfully.");
    }

    @PostMapping("/channel")
    public ResponseEntity<String> sendMessageToChannel(@RequestBody Message message) {
        messageService.sendMessageToChannel(message.getSenderId(), message.getChannelId(), message.getContent());
        return ResponseEntity.ok("Message sent to channel successfully.");
    }

    @GetMapping("/private")
    public ResponseEntity<List<Message>> getMessagesBetweenUsers(
            @RequestParam("senderId") Long senderId,
            @RequestParam("recipientId") Long recipientId) {

        List<Message> messages = messageService.getMessagesBetweenUsers(senderId, recipientId);
        return ResponseEntity.ok(messages);
    }
    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<Message>> getAllMessagesInChannel(@PathVariable Long channelId) {
        List<Message> messages = messageService.getAllMessagesInChannel(channelId);
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{messageId}/delete")
    public ResponseEntity<String> deleteMessage(@PathVariable Long messageId, @RequestParam Long senderId) {
        messageService.softDeleteMessage(messageId, senderId);
        return ResponseEntity.ok("Message deleted successfully.");
    }
}
