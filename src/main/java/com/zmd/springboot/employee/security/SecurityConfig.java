package com.zmd.springboot.employee.security;

import com.zmd.springboot.employee.config.ApiPathProperties;
import com.zmd.springboot.employee.config.SecurityCorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;

import static com.zmd.springboot.employee.config.ApiPaths.*;

@Configuration
public class SecurityConfig {

    private final String apiBasePath;

    public SecurityConfig(ApiPathProperties props) {
        this.apiBasePath = props.getBasePath();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select user_id, password, enabled from system_users where user_id=?"
        );
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select user_id, role from roles where user_id=?"
        );

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, Environment env) {

        boolean isDev = Arrays.asList(env.getActiveProfiles()).contains("dev");

        String employeesBase = apiBasePath + EMPLOYEES;
        String employeesAll = apiBasePath + EMPLOYEES_ALL;

        http.authorizeHttpRequests(configurer -> {

            // Allow CORS preflight (OPTIONS) without authentication
            configurer.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

            // Swagger / OpenAPI
            if (isDev) {
                configurer.requestMatchers(swaggerWhitelist()).permitAll();
            } else {
                configurer.requestMatchers(swaggerWhitelist()).denyAll();
            }

            // H2 console
            if (isDev) {
                configurer.requestMatchers(H2_CONSOLE).permitAll();
            } else {
                configurer.requestMatchers(H2_CONSOLE).denyAll();
            }

            // API rules
            configurer
                    .requestMatchers(HttpMethod.GET, employeesBase).hasRole("EMPLOYEE")
                    .requestMatchers(HttpMethod.GET, employeesAll).hasRole("EMPLOYEE")
                    .requestMatchers(HttpMethod.POST, employeesBase).hasRole("MANAGER")
                    .requestMatchers(HttpMethod.PUT, employeesAll).hasRole("MANAGER")
                    .requestMatchers(HttpMethod.DELETE, employeesAll).hasRole("ADMIN")
                    .anyRequest().authenticated();
        });

        http.httpBasic(Customizer.withDefaults());
        http.cors(Customizer.withDefaults());

        // REST hardening: don’t create sessions for API calls
        http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Step 3: CSRF strategy
        // - Ignore CSRF for stateless API endpoints (Basic Auth via Authorization header)
        // - Ignore for dev tools endpoints (H2 + Swagger UI/Try-it-out)
        http.csrf(csrf -> csrf.ignoringRequestMatchers(
                employeesBase,
                employeesAll,
                H2_CONSOLE
        ).ignoringRequestMatchers(swaggerWhitelist()));

        // Frames: allow only in dev (H2 console uses frames)
        if (isDev) {
            http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        } else {
            http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::deny));
        }

        http.exceptionHandling(eh -> eh.authenticationEntryPoint(authenticationEntryPoint()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(SecurityCorsProperties props) {
        CorsConfiguration cfg = new CorsConfiguration();

        cfg.setAllowedOrigins(props.getAllowedOrigins());
        cfg.setAllowedMethods(props.getAllowedMethods());
        cfg.setAllowedHeaders(props.getAllowedHeaders());
        cfg.setExposedHeaders(props.getExposedHeaders());
        cfg.setAllowCredentials(props.isAllowCredentials());
        cfg.setMaxAge(props.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setHeader("WWW-Authenticate", "");
            response.getWriter().write("{\"message\":\"Unauthorized\"}");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
