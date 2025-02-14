package com.chat_app.chatapp.repositories;


import com.chat_app.chatapp.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("email")
    );

    public void createUser(User user) {
        jdbcTemplate.update("INSERT INTO users (username, email, is_deleted) VALUES (?, ?, ?)",
                user.getUsername(), user.getEmail(), false);
    }

    public Optional<User> getUserById(Long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ? AND is_deleted = FALSE",
                userRowMapper, id).stream().findFirst();
    }

    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users WHERE is_deleted = FALSE", userRowMapper);
    }

    public void updateUser(Long userId, String newUsername, String newEmail) {
        jdbcTemplate.update("UPDATE users SET username = ?, email = ? WHERE id = ?",
                newUsername, newEmail, userId);
    }

    public void deleteUser(Long userId) {
        jdbcTemplate.update("UPDATE users SET is_deleted = TRUE WHERE id = ?", userId);
    }

    public void addFriend(Long userId, Long friendId) {
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES (?, ?)", userId, friendId);
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES (?, ?)", friendId, userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? AND friend_id = ?", userId, friendId);
        jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? AND friend_id = ?", friendId, userId);
    }

    public List<User> getFriendsWithNames(Long userId) {
        String sql = "SELECT u.id, u.username, u.email " +
                "FROM friends f " +
                "INNER JOIN users u ON f.friend_id = u.id " +
                "WHERE f.user_id = ?";
        return jdbcTemplate.query(sql, userRowMapper, userId);
    }
}