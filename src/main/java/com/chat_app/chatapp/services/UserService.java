package com.chat_app.chatapp.services;


import com.chat_app.chatapp.models.User;
import com.chat_app.chatapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        userRepository.createUser(user);
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.getUserById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void updateUser(Long userId, String newUsername, String newEmail) {
        userRepository.updateUser(userId, newUsername, newEmail);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Cannot add yourself as a friend.");
        }
        userRepository.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        userRepository.removeFriend(userId, friendId);
    }

    public List<User> getFriendsWithNames(Long userId) {
        return userRepository.getFriendsWithNames(userId);
    }
}
