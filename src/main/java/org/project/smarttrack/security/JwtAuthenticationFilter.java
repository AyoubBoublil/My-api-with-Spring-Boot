package org.project.smarttrack.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.project.smarttrack.constants.SecurityConstants;
import org.project.smarttrack.dto.LoginDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final SecurityConstants securityConstants;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SecurityConstants securityConstants) {
        this.authenticationManager = authenticationManager;
        this.securityConstants = securityConstants;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginDto credentials = new ObjectMapper().readValue(request.getInputStream(), LoginDto.class);
            log.info("Username : " + credentials.getUsername());
            log.info("Password : " + credentials.getPassword());
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String jwtToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, securityConstants.getJwtSecret())
                .setHeaderParam("typ", securityConstants.getTokenType())
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + securityConstants.getExpirationTime()))
                .claim("roles", roles)
                .compact();

        log.info("The jwt token after generation : " + jwtToken);

        String body = user.getUsername() + " " + jwtToken;

        // response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + jwtToken);
        response.getWriter().write(body);
    }
}
