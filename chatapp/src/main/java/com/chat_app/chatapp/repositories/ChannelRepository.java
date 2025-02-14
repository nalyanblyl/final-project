package com.chat_app.chatapp.repositories;


import com.chat_app.chatapp.models.Channel;
import com.chat_app.chatapp.models.ChannelParticipant;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ChannelRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChannelRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Channel> channelRowMapper = (rs, rowNum) ->
            new Channel(rs.getLong("id"), rs.getString("name"), rs.getLong("owner_id"));

    public void createChannel(Channel channel) {
        String insertChannelSql = "INSERT INTO channels (name, owner_id, is_deleted) VALUES (?, ?, FALSE)";
        jdbcTemplate.update(insertChannelSql, channel.getName(), channel.getOwnerId());

        Long channelId = jdbcTemplate.queryForObject("SELECT id FROM channels ORDER BY id DESC LIMIT 1", Long.class);

        String getUsernameSql = "SELECT username FROM users WHERE id = ?";
        String ownerUsername = jdbcTemplate.queryForObject(getUsernameSql, new Object[]{channel.getOwnerId()}, String.class);


        String insertOwnerSql = "INSERT INTO channel_users (channel_id, user_id, role, nickname) VALUES (?, ?, 'OWNER', ?)";
        jdbcTemplate.update(insertOwnerSql, channelId, channel.getOwnerId(), ownerUsername);
    }

    public Optional<Channel> getChannelById(Long id) {
        return jdbcTemplate.query("SELECT * FROM channels WHERE id = ? AND is_deleted = FALSE",
                channelRowMapper, id).stream().findFirst();
    }

    public void addUserToChannel(Long channelId, Long userId, String role, String nickname) {
        String getUserNameSql = "SELECT username FROM users WHERE id = ?";
        String defaultNickname = jdbcTemplate.queryForObject(getUserNameSql, new Object[]{userId}, String.class);

        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = defaultNickname;
        }

        String insertUserSql = "INSERT INTO channel_users (channel_id, user_id, role, nickname) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertUserSql, channelId, userId, role, nickname);
    }

    public void updateChannelName(Long channelId, String newName) {
        String sql = "UPDATE channels SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, newName, channelId);
    }

    public void softDeleteChannel(Long channelId) {
        String sql = "UPDATE channels SET is_deleted = TRUE WHERE id = ?";
        jdbcTemplate.update(sql, channelId);
    }

    public boolean isUserOwnerOfChannel(Long channelId, Long userId) {
        String sql = "SELECT COUNT(*) FROM channels WHERE id = ? AND owner_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{channelId, userId}, Integer.class);
        return count != null && count > 0;
    }

    public String getUserRoleInChannel(Long channelId, Long userId) {
        String sql = "SELECT role FROM channel_users WHERE channel_id = ? AND user_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{channelId, userId}, String.class);
    }

    public void updateUserNickname(Long channelId, Long userId, String newNickname) {
        String sql = "UPDATE channel_users SET nickname = ? WHERE channel_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, newNickname, channelId, userId);
    }
    public void updateUserRole(Long channelId, Long userId, String newRole) {
        String sql = "UPDATE channel_users SET role = ? WHERE channel_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, newRole, channelId, userId);
    }

    public List<Channel> getChannelsForUser(Long userId) {
        String sql = "SELECT c.id, c.name, c.owner_id, c.is_deleted " +
                "FROM channel_users cu " +
                "INNER JOIN channels c ON cu.channel_id = c.id " +
                "WHERE cu.user_id = ? AND c.is_deleted = FALSE";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new Channel(rs.getLong("id"), rs.getString("name"), rs.getLong("owner_id")),
                userId
        );
    }
    public List<ChannelParticipant> getChannelParticipants(Long channelId) {
        String sql = "SELECT user_id AS userId, nickname, role FROM channel_users WHERE channel_id = ?";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ChannelParticipant.class), channelId);
    }
    public void removeUserFromChannel(Long channelId, Long userId) {
        String sql = "DELETE FROM channel_users WHERE channel_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, channelId, userId);
    }
}
