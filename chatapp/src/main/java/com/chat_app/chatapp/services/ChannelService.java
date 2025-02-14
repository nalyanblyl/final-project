package com.chat_app.chatapp.services;


import com.chat_app.chatapp.models.Channel;
import com.chat_app.chatapp.models.ChannelParticipant;
import com.chat_app.chatapp.repositories.ChannelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;

    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public void createChannel(String channelName, Long ownerId) {
        if (channelName == null || channelName.isEmpty()) {
            throw new IllegalArgumentException("Channel name cannot be null or empty.");
        }

        Channel channel = new Channel();
        channel.setName(channelName);
        channel.setOwnerId(ownerId);
        channelRepository.createChannel(channel);
    }
    public List<Channel> getChannelsForUser(Long userId) {
        return channelRepository.getChannelsForUser(userId);
    }

    public void addUserToChannel(Long channelId, Long userId, String role, String nickname) {
        channelRepository.addUserToChannel(channelId, userId, role, nickname);
    }
    public void updateUserNicknameAndRole(Long requesterId, Long channelId, Long userId, String newNickname, String newRole) {
        String requesterRole = channelRepository.getUserRoleInChannel(channelId, requesterId);


        if (!requesterRole.equalsIgnoreCase("OWNER") && !requesterRole.equalsIgnoreCase("ADMIN")) {
            throw new IllegalArgumentException("Only channel owners and admins can update users.");
        }

        if (newNickname != null && !newNickname.isEmpty()) {
            channelRepository.updateUserNickname(channelId, userId, newNickname);
        }

        if (newRole != null && !newRole.isEmpty()) {
            channelRepository.updateUserRole(channelId, userId, newRole);
        }
    }

    public List<ChannelParticipant> getChannelParticipants(Long channelId) {
        return channelRepository.getChannelParticipants(channelId);
    }

    public void updateChannelName(Long requesterId, Long channelId, String newName) {

        if (channelRepository.isUserOwnerOfChannel(channelId, requesterId)) {
            channelRepository.updateChannelName(channelId, newName);
        } else {
            throw new IllegalArgumentException("Only the channel owner can update the channel name.");
        }
    }

    public void softDeleteChannel(Long requesterId, Long channelId) {

        if (channelRepository.isUserOwnerOfChannel(channelId, requesterId)) {
            channelRepository.softDeleteChannel(channelId);
        } else {
            throw new IllegalArgumentException("Only the channel owner can delete the channel.");
        }
    }
    public void removeUserFromChannel(Long requesterId, Long channelId, Long targetUserId) {

        String requesterRole = channelRepository.getUserRoleInChannel(channelId, requesterId);


        if (requesterRole.equalsIgnoreCase("OWNER") || requesterRole.equalsIgnoreCase("ADMIN")) {
            String targetUserRole = channelRepository.getUserRoleInChannel(channelId, targetUserId);


            if (targetUserRole.equalsIgnoreCase("OWNER")) {
                throw new IllegalArgumentException("Cannot remove the channel owner.");
            }

            channelRepository.removeUserFromChannel(channelId, targetUserId);
        } else {
            throw new IllegalArgumentException("Only channel owners and admins can remove users.");
        }
    }
    public String getUserRoleInChannel(Long channelId, Long userId) {
        return channelRepository.getUserRoleInChannel(channelId, userId);
    }
}