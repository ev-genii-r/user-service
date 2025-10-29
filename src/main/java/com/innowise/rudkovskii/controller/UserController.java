package com.innowise.rudkovskii.controller;

import com.innowise.rudkovskii.dto.user.UserMapper;
import com.innowise.rudkovskii.dto.user.request.UserCreateRequest;
import com.innowise.rudkovskii.dto.user.request.UserUpdateRequest;
import com.innowise.rudkovskii.dto.user.response.UserResponse;
import com.innowise.rudkovskii.entity.User;
import com.innowise.rudkovskii.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Integer id){
        User user = userService.getById(id);
        UserResponse userResponse = userMapper.userToUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String email){
        User user = userService.getByEmail(email);
        UserResponse userResponse = userMapper.userToUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest){
        User user = userMapper.toUser(userCreateRequest);
        User createdUser = userService.createUser(user);
        UserResponse userResponse = userMapper.userToUserResponse(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Integer id,
                                                   @Valid @RequestBody UserUpdateRequest userUpdateRequest){
        User user = userMapper.toUser(userUpdateRequest);
        User createdUser = userService.updateUser(id, user);
        UserResponse userResponse = userMapper.userToUserResponse(createdUser);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
