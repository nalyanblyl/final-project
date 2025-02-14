package com.chat_app.chatapp.controllers;

import com.chat_app.chatapp.models.Channel;
import com.chat_app.chatapp.models.ChannelParticipant;
import com.chat_app.chatapp.services.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createChannel(@RequestBody Map<String, String> payload) {
        String channelName = payload.get("channelName");
        Long ownerId = Long.valueOf(payload.get("ownerId"));

        if (channelName == null || channelName.isEmpty()) {
            return ResponseEntity.badRequest().body("Channel name cannot be null or empty.");
        }

        channelService.createChannel(channelName, ownerId);
        return ResponseEntity.ok("Channel created successfully with owner.");
    }

    @PostMapping("/{channelId}/addUser")
    public ResponseEntity<String> addUserToChannel(@PathVariable Long channelId,
                                                   @RequestParam Long userId,
                                                   @RequestParam String role,
                                                   @RequestParam(required = false) String nickname) {
        channelService.addUserToChannel(channelId, userId, role, nickname);
        return ResponseEntity.ok("User added to channel successfully.");
    }

    @PutMapping("/{channelId}/updateUser")
    public ResponseEntity<String> updateUserNicknameAndRole(@PathVariable Long channelId,
                                                            @RequestParam Long requesterId,
                                                            @RequestParam Long userId,
                                                            @RequestParam String newNickname,
                                                            @RequestParam String newRole) {
        channelService.updateUserNicknameAndRole(requesterId, channelId, userId, newNickname, newRole);
        return ResponseEntity.ok("User nickname and role updated successfully.");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Channel>> getChannelsForUser(@PathVariable Long userId) {
        List<Channel> channels = channelService.getChannelsForUser(userId);
        if (channels.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(channels);
    }

    @GetMapping("/{channelId}/participants")
    public List<ChannelParticipant> getChannelParticipant(@PathVariable Long channelId) {
        return channelService.getChannelParticipants(channelId);
    }
    @PutMapping("/{channelId}/updateName")
    public ResponseEntity<String> updateChannelName(@PathVariable Long channelId,
                                                    @RequestParam Long requesterId,
                                                    @RequestParam String newName) {
        channelService.updateChannelName(requesterId, channelId, newName);
        return ResponseEntity.ok("Channel name updated successfully.");
    }

    @DeleteMapping("/{channelId}/delete")
    public ResponseEntity<String> softDeleteChannel(@PathVariable Long channelId,
                                                    @RequestParam Long requesterId) {
        channelService.softDeleteChannel(requesterId, channelId);
        return ResponseEntity.ok("Channel deleted successfully.");
    }
    @DeleteMapping("/{channelId}/removeUser")
    public ResponseEntity<String> removeUserFromChannel(@PathVariable Long channelId,
                                                        @RequestParam Long requesterId,
                                                        @RequestParam Long targetUserId) {
        channelService.removeUserFromChannel(requesterId, channelId, targetUserId);
        return ResponseEntity.ok("User removed from channel successfully.");
    }
    @GetMapping("/{channelId}/user/{userId}/role")
    public ResponseEntity<String> getUserRoleInChannel(@PathVariable Long channelId, @PathVariable Long userId) {
        String role = channelService.getUserRoleInChannel(channelId, userId);
        if (role != null) {
            return ResponseEntity.ok(role);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found for this user in the channel.");
        }
    }

}
