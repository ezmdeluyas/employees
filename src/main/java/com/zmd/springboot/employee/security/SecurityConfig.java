package com.zmd.springboot.employee.security;

import com.zmd.springboot.employee.config.ApiPathProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

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

        // Define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery("select user_id, password, enabled from system_users where user_id=?");

        // Define query to retrieve the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select user_id, role from roles where user_id=?");

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {

        String employeesBase = apiBasePath + EMPLOYEES;
        String employeesAll = apiBasePath + EMPLOYEES_ALL;

        http.authorizeHttpRequests(configurer -> configurer
                        .requestMatchers(HttpMethod.GET, H2_CONSOLE).permitAll()
                        .requestMatchers(HttpMethod.POST, H2_CONSOLE).permitAll()
                        .requestMatchers(swaggerWhitelist()).permitAll()
                        .requestMatchers(HttpMethod.GET, employeesBase).hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, employeesAll).hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, employeesBase).hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, employeesAll).hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, employeesAll).hasRole("ADMIN")
                );
        // Use HTTP Basic Authentication
        http.httpBasic(Customizer.withDefaults());

        http.csrf(AbstractHttpConfigurer::disable);

        http.exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authenticationEntryPoint()));

        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        // Send 401 unauthorized status without triggering a basic auth
        return (request, response, e) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setHeader("WWW-Authenticate", "");
            response.getWriter().write("{\"message\":\"Unauthorized\"}");
        };
    }

}
