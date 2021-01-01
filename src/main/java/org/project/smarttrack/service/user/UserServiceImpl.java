package org.project.smarttrack.service.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.project.smarttrack.dto.UserDto;
import org.project.smarttrack.entity.User;
import org.project.smarttrack.repository.RoleRepository;
import org.project.smarttrack.repository.UserRepository;
import org.project.smarttrack.service.exception.EmailAlreadyExistException;
import org.project.smarttrack.service.exception.NoMatchPasswordException;
import org.project.smarttrack.service.exception.UserNotFoundException;
import org.project.smarttrack.service.mailing.IMailingService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    public final IMailingService mailingService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, IMailingService mailingService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailingService = mailingService;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllByIsEnabled(1);
    }

    @Override
    public User getUserById(Long id) throws UserNotFoundException {
        return userRepository.findByIdAndIsEnabled(id, 1).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User saveUser(UserDto userDto) throws NoMatchPasswordException, EmailAlreadyExistException {
        if (userRepository.existsByEmailAndIsEnabled(userDto.getEmail(), 1))
            throw new EmailAlreadyExistException("Email already exist !");
        if (!userDto.getPassword().equalsIgnoreCase(userDto.getConfirmPassword()))
            throw new NoMatchPasswordException("No match password and confirm password !");

        String activateAccountToken = RandomStringUtils.random(12, true, true);
        log.info("activateAccountToken : " + activateAccountToken);
        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .roles(roleRepository.findAll().stream().filter(role -> role.getName().equalsIgnoreCase("USER")).collect(Collectors.toList()))
                .isActivatedAccount(false)
                .activateAccountToken(activateAccountToken)
                .build();

        user = userRepository.save(user);

        // Send email to this user for confirm and validate account
        String text = "Please click in this link to activate your account :  " + "http://localhost:8080/admin/users/confirm-account/" + activateAccountToken;
        mailingService.sendActivateAccountEmail(user.getEmail(), "Confirmation account", text);

        return user;
    }

    @Override
    public User updateUser(UserDto userDto) throws UserNotFoundException {
        User userToUpdate = userRepository.findByIdAndIsEnabled(userDto.getId(), 1).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!userToUpdate.getFirstName().equalsIgnoreCase(userDto.getFirstName()))
            userToUpdate.setFirstName(userDto.getFirstName());
        if (!userToUpdate.getLastName().equalsIgnoreCase(userDto.getLastName()))
            userToUpdate.setLastName(userDto.getLastName());
        if (!userToUpdate.getEmail().equalsIgnoreCase(userDto.getEmail())) userToUpdate.setEmail(userDto.getEmail());
        userToUpdate = userRepository.save(userToUpdate);
        return userToUpdate;
    }

    @Override
    public boolean deleteUser(Long userId) throws UserNotFoundException {
        User userToDelete = userRepository.findByIdAndIsEnabled(userId, 1).orElseThrow(() -> new UserNotFoundException("User not found"));
        userToDelete.setIsEnabled(0);
        userRepository.save(userToDelete);
        return true;
    }

    @Override
    public boolean activateAccount(String activateAccountToken) throws UserNotFoundException {
        log.info("activateAccountToken " + activateAccountToken);
        User userToActivateAccount = userRepository.findByActivateAccountTokenAndIsEnabled(activateAccountToken, 1).orElseThrow(() -> new UserNotFoundException("Your account is already activated !"));
        userToActivateAccount.setIsActivatedAccount(true);
        userToActivateAccount.setActivateAccountToken(null);
        userRepository.save(userToActivateAccount);
        return true;
    }
}
