package com.zmd.springboot.employee.config;

import java.util.List;

public final class ApiPaths {

    private ApiPaths() {
        throw new UnsupportedOperationException("Utility class");
    }

    // relative paths only
    public static final String EMPLOYEES = "/employees";
    public static final String EMPLOYEES_ALL = EMPLOYEES + "/**";

    // Sonar S1075 is noisy here: this is a route template, not an env-dependent URI
    @SuppressWarnings("java:S1075")
    public static final String ID_PATH = "/{employeeId}";

    public static final String H2_CONSOLE = "/h2-console/**";

    private static final List<String> SWAGGER_WHITELIST_INTERNAL = List.of(
            "/docs/**",
            "/swagger-ui/**",
            "/api-docs/**",
            "/swagger-ui.html"
    );

    public static String[] swaggerWhitelist() {
        return SWAGGER_WHITELIST_INTERNAL.toArray(String[]::new);
    }
}
