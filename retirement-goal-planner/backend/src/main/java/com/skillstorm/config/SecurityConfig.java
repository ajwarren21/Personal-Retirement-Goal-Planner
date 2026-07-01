package com.skillstorm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;

import com.skillstorm.services.AuthService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(req -> req
            .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/users").permitAll()
            .anyRequest().authenticated()
        );

        // http.authorizeHttpRequests(req -> req.anyRequest().authenticated());

        http.oauth2Login(Customizer.withDefaults());

        // http.logout(logout -> logout
        //     .logoutUrl("/auth/logout")
        //     .logoutSuccessHandler(oidcLogoutSuccessHandler())
        //     .invalidateHttpSession(true)
        //     .deleteCookies("JSESSIONID")
        // );
        
        http.logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }

    // @Bean
    // // public OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
    // //     OidcClientInitiatedLogoutSuccessHandler handler =
    // //         new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
    // //     handler.setPostLogoutRedirectUri("http://localhost:5500/login.html");
    // //     return handler;
    // // }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private AuthService authservice;

    public SecurityConfig(AuthService service) {
        this.authservice = service;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
            .userDetailsService(authservice)
            .passwordEncoder(passwordEncoder());
        return builder.build();
    }
}
