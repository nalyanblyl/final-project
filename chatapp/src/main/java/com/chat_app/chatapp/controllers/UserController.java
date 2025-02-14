package com.chat_app.chatapp.controllers;


import com.chat_app.chatapp.models.User;
import com.chat_app.chatapp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        userService.createUser(user);
        return ResponseEntity.ok("User created successfully.");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId,
                                             String newUsername,
                                             String newEmail) {
        userService.updateUser(userId, newUsername, newEmail);
        return ResponseEntity.ok("User updated successfully.");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully.");
    }
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{userId}/addFriend/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.addFriend(userId, friendId);
        return ResponseEntity.ok("Friend added successfully.");
    }

    @DeleteMapping("/{userId}/removeFriend/{friendId}")
    public ResponseEntity<String> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.removeFriend(userId, friendId);
        return ResponseEntity.ok("Friend removed successfully.");
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<User>> getFriendsWithNames(@PathVariable Long userId) {
        List<User> friends = userService.getFriendsWithNames(userId);
        return ResponseEntity.ok(friends);
    }
}