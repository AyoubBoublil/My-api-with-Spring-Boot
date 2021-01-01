package org.project.smarttrack.audit;

import lombok.extern.slf4j.Slf4j;
import org.project.smarttrack.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Auditor : " + ((User) authentication.getPrincipal()).getEmail());

        if(authentication == null || !authentication.isAuthenticated() ||
        authentication instanceof AnonymousAuthenticationToken)
            return Optional.of("ROOT");
        else
        return Optional.of(((User) authentication.getPrincipal()).getEmail());
    }
}
