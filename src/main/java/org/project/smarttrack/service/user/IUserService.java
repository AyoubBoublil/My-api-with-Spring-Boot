package org.project.smarttrack.service.user;

import org.project.smarttrack.dto.UserDto;
import org.project.smarttrack.entity.User;
import org.project.smarttrack.service.exception.EmailAlreadyExistException;
import org.project.smarttrack.service.exception.NoMatchPasswordException;
import org.project.smarttrack.service.exception.UserNotFoundException;

import java.util.List;

public interface IUserService {

    List<User> getAllUsers();

    User getUserById(Long id) throws UserNotFoundException;

    User saveUser(UserDto userDto) throws NoMatchPasswordException, EmailAlreadyExistException;

    User updateUser(UserDto userDto) throws UserNotFoundException;

    boolean deleteUser(Long userId) throws UserNotFoundException;

    boolean activateAccount(String activateAccountToken) throws UserNotFoundException;
}
