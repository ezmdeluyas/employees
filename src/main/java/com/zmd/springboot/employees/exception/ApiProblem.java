package com.zmd.springboot.employees.exception;

import java.net.URI;

public enum ApiProblem {

    VALIDATION_ERROR(
            "Validation Failed",
            "One or more request values are invalid",
            URI.create("https://api.zmd.com/problems/validation-error")
    ),
    TYPE_MISMATCH(
            "Parameter Type Mismatch",
            "One or more request values are invalid",
            URI.create("https://api.zmd.com/problems/type-mismatch")
    ),
    MALFORMED_BODY(
            "Malformed Request Body",
            "Request body is invalid or unreadable",
            URI.create("https://api.zmd.com/problems/malformed-body")
    ),
    NOT_FOUND(
            "Resource Not Found",
            "The requested resource does not exist",
            URI.create("https://api.zmd.com/problems/not-found")
    );

    private final String title;
    private final String detail;
    private final URI type;

    ApiProblem(String title, String detail, URI type) {
        this.title = title;
        this.detail = detail;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public URI getType() {
        return type;
    }
}