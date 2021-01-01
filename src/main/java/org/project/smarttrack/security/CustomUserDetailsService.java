package org.project.smarttrack.security;

import lombok.SneakyThrows;
import org.project.smarttrack.entity.User;
import org.project.smarttrack.repository.UserRepository;
import org.project.smarttrack.service.exception.AccountNotActivatedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndIsEnabled(username, 1).orElseThrow(() -> new UsernameNotFoundException("User not found with this credentials"));

        if (user.getIsActivatedAccount().equals(false))
            throw new AccountNotActivatedException("Your account is not activated , check your mail to confirm it !");
        return new CustomUserDetails(user);
    }
}
