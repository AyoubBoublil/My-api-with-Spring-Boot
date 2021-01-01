package org.project.smarttrack.controller;

import lombok.extern.slf4j.Slf4j;
import org.project.smarttrack.dto.UserDto;
import org.project.smarttrack.entity.User;
import org.project.smarttrack.mapper.UserMapper;
import org.project.smarttrack.service.exception.EmailAlreadyExistException;
import org.project.smarttrack.service.exception.NoMatchPasswordException;
import org.project.smarttrack.service.exception.UserNotFoundException;
import org.project.smarttrack.service.user.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
public class UserManagementController {

    private final IUserService userService;
    private final UserMapper userMapper;

    public UserManagementController(IUserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/users/list")
    public ResponseEntity<List<UserDto>> getAllUsers(Authentication authentication) {
        User auth_user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.usersToUserDtos(userService.getAllUsers()));
    }

    @GetMapping("/users/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id, Authentication authentication) {
        User auth_user = (User) authentication.getPrincipal();
        try {
            return ResponseEntity.ok().body(userMapper.userToUserDto(userService.getUserById(id)));
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/users/register")
    public ResponseEntity<?> saveUser(@RequestBody UserDto userDto, Authentication authentication) {
        User auth_user = (User) authentication.getPrincipal();
        try {
            return ResponseEntity.ok().body(userMapper.userToUserDto(userService.saveUser(userDto)));
        } catch (NoMatchPasswordException | EmailAlreadyExistException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/users/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, Authentication authentication) {
        User auth_user = (User) authentication.getPrincipal();
        try {
            return ResponseEntity.ok().body(userMapper.userToUserDto(userService.updateUser(userDto)));
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/users/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, Authentication authentication) {
        User auth_user = (User) authentication.getPrincipal();
        try {
            return ResponseEntity.ok().body(userService.deleteUser(userId));
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/users/confirm-account/{activateAccountToken}")
    public ResponseEntity<?> getUserById(@PathVariable String activateAccountToken, Authentication authentication) {
        User auth_user = (User) authentication.getPrincipal();
        try {
            return ResponseEntity.ok().body(userService.activateAccount(activateAccountToken));
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
