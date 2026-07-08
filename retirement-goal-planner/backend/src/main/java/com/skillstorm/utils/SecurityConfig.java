package com.skillstorm.utils;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

            /**
             * you can specify which endpoints require authentication and which don't
             *      - can also specify if a user needs to have certain role(s) to access certain endpoints
             * 
             */
            .authorizeHttpRequests(auth -> 
                auth
                    // allow all traffic for new users to register
                    .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/auth/csrf").permitAll()

                    // user controller endpoints are only allowed for admins
                    .requestMatchers("/api/v1/users/**").hasRole("ADMIN")

                    // any user can retrieve movies or directors, but only admins can modify
                    .requestMatchers(HttpMethod.GET, "/api/v1/directors/**", "/api/v1/movies/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/v1/directors/**", "/api/v1/movies/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/directors/**", "/api/v1/movies/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/directors/**", "/api/v1/movies/**").hasRole("ADMIN")

                    // all other requests, the user just needs to be authenticated, no role requirments
                    .anyRequest().authenticated()
            )

            /**
             * Spring defaults to only checking for CSRF token on modifyinh requests (POST, PUT, PATCH, DELETE)
             * So, to get a token initially, you need to send a GET request of some kind
             */
            .csrf(csrf -> 
                csrf
                    // write the token into a readable XSRF-TOKEN cookie
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                    /**
                     * the exact value of the XSRF-TOKEN sent back initially is what is used
                     * to authenticate on subsequent requests
                     *      - disables the XOR masking (less secure this way)
                     */
                    .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            )

            /**
             * tells spring security we are doing Basic Auth
             *      - prevents spring from trying to use its default login form
             */
            .httpBasic(Customizer.withDefaults());
            
        return http.build();

    }

}
