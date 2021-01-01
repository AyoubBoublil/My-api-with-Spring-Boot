package org.project.smarttrack.repository;

import org.project.smarttrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByIsEnabled(Integer isEnabled);
    Optional<User> findByIdAndIsEnabled(Long userId, Integer isEnabled);
    Optional<User> findByEmailAndIsEnabled(String email, Integer isEnabled);
    Optional<User> findByActivateAccountTokenAndIsEnabled(String activatedAccountToken, Integer isEnabled);
    boolean existsByEmailAndIsEnabled(String email, Integer isEnabled);
}
