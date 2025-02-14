package com.chat_app.chatapp.models;

public class ChannelUser {
    private Long channelId;
    private Long userId;
    private String role;
    private String nickname;

    public ChannelUser() {}

    public ChannelUser(Long channelId, Long userId, String role, String nickname) {
        this.channelId = channelId;
        this.userId = userId;
        this.role = role;
        this.nickname = nickname;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}