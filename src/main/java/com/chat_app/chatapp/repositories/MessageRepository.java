package com.chat_app.chatapp.repositories;


import com.chat_app.chatapp.models.Message;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageRepository {

    private final JdbcTemplate jdbcTemplate;

    public MessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Message> messageRowMapper = (rs, rowNum) -> {
        Message message = new Message();
        message.setId(rs.getLong("id"));
        message.setSenderId(rs.getObject("sender_id") != null ? rs.getLong("sender_id") : null);
        message.setRecipientId(rs.getObject("recipient_id") != null ? rs.getLong("recipient_id") : null);
        message.setChannelId(rs.getObject("channel_id") != null ? rs.getLong("channel_id") : null);
        message.setContent(rs.getString("content"));
        message.setDeleted(rs.getBoolean("is_deleted"));
        message.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        message.setSenderNickname(rs.getString("sender_nickname") != null ? rs.getString("sender_nickname") : "Unknown");
        return message;
    };

    public List<Message> getMessagesBetweenUsersWithSenderName(Long senderId, Long recipientId) {
        String sql = "SELECT m.id, m.sender_id, m.recipient_id, m.channel_id, m.content, m.timestamp, m.is_deleted, " +
                "u.username AS sender_nickname " +
                "FROM messages m " +
                "INNER JOIN users u ON m.sender_id = u.id " +
                "WHERE ((m.sender_id = ? AND m.recipient_id = ?) OR (m.sender_id = ? AND m.recipient_id = ?)) " +
                "AND m.is_deleted = FALSE " +
                "ORDER BY m.timestamp";

        return jdbcTemplate.query(sql, messageRowMapper, senderId, recipientId, recipientId, senderId);
    }

    public void sendMessage(Message message) {
        String sql = "INSERT INTO messages (sender_id, recipient_id, channel_id, content, is_deleted, timestamp) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, message.getSenderId(), message.getRecipientId(), message.getChannelId(),
                message.getContent(), message.isDeleted(), message.getTimestamp());
    }

    private final RowMapper<Message> channelMessageRowMapper = (rs, rowNum) -> {
        Message message = new Message();
        message.setId(rs.getLong("id"));
        message.setSenderId(rs.getLong("sender_id"));
        message.setChannelId(rs.getLong("channel_id"));
        message.setContent(rs.getString("content"));
        message.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        message.setSenderNickname(rs.getString("sender_nickname"));
        return message;
    };

    public List<Message> getAllMessagesInChannel(Long channelId) {
        String sql = "SELECT m.id, m.sender_id, m.channel_id, m.content, m.timestamp, " +
                "COALESCE(cu.nickname, u.username) AS sender_nickname " +
                "FROM messages m " +
                "LEFT JOIN users u ON m.sender_id = u.id " +
                "LEFT JOIN channel_users cu ON m.sender_id = cu.user_id AND m.channel_id = cu.channel_id " +
                "WHERE m.channel_id = ? AND m.is_deleted = FALSE " +
                "ORDER BY m.timestamp";

        return jdbcTemplate.query(sql, channelMessageRowMapper, channelId);
    }

    public void softDeleteMessage(Long messageId, Long senderId) {
        String sql = "UPDATE messages SET is_deleted = TRUE WHERE id = ? AND sender_id = ?";
        jdbcTemplate.update(sql, messageId, senderId);
    }
}
