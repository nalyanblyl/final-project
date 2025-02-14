package com.chat_app.chatapp.models;

public class ChannelParticipant {
    private Long userId;
    private String nickname;
    private String role;

    public ChannelParticipant() {}

    public ChannelParticipant(Long userId, String nickname, String role) {
        this.userId = userId;
        this.nickname = nickname;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
