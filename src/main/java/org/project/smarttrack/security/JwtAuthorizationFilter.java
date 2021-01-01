package org.project.smarttrack.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.project.smarttrack.constants.SecurityConstants;
import org.project.smarttrack.entity.User;
import org.project.smarttrack.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final SecurityConstants securityConstants;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, SecurityConstants securityConstants) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.securityConstants = securityConstants;
    }

    // Reads the JWT from the Authorization header, and then uses JWT to validate the token
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String jwtToken = request.getHeader(securityConstants.getTokenHeader());

        System.out.println("JwtToken : " + jwtToken);

        if (jwtToken == null || !jwtToken.startsWith(securityConstants.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = Jwts.parser().setSigningKey(securityConstants.getJwtSecret())
                .parseClaimsJws(jwtToken.replace(securityConstants.getTokenPrefix(), ""))
                .getBody();

        // Get username from token
        String username = claims.getSubject();

        // Get the roles of current user from token
        List<String> roles = (List<String>) claims.get("roles");

        // Add the roles in list of Collection<GrantedAuthority> authorities
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });

        if (username != null) {
            User user = userRepository.findByEmailAndIsEnabled(username, 1).orElseThrow(() -> new UsernameNotFoundException("Invalid username or password !"));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);

    }

}
