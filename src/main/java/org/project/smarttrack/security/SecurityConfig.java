package org.project.smarttrack.security;

import org.project.smarttrack.constants.SecurityConstants;
import org.project.smarttrack.enums.UserRoles;
import org.project.smarttrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final SecurityConstants securityConstants;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(@Qualifier("customUserDetailsService") UserDetailsService userDetailsService, UserRepository userRepository, SecurityConstants securityConstants, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.securityConstants = securityConstants;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Our spring security based on jwt so we will manage the authentication by using UserDetailsService
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // FormLogin the login form provided by spring
        // Disable the synchronize token generated by spring security with http.csrf().disable();
        // Before to start working with JWT , should disable the authentication based on session and work in mode stateless --> // http.formLogin();
        http.csrf().disable();
        // this disables session creation on Spring Security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //.antMatchers(SecurityConstants.SIGN_IN_URL).permitAll()
                .antMatchers(securityConstants.getSignInUrl()).permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/public/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(securityConstants.getManageUsers()).hasAuthority(UserRoles.ROLE_ADMIN.toString())
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), securityConstants))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository, securityConstants));

    }

    /*@Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }*/

}
