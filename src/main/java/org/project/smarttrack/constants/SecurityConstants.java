package org.project.smarttrack.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class SecurityConstants {
    private String jwtSecret;
    private long expirationTime;
    private String tokenType;
    private String tokenPrefix;
    private String tokenHeader;
    private String signInUrl;
    private String manageUsers;

    /*public static final String SECRET = "admin@SmartTrack.com";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 Days
    public static final String TOKEN_TYPE = "JWT"; // Token type is JWT
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_IN_URL = "/login/**";
    public static final String MANAGE_USERS= "/admin/**";*/
}
