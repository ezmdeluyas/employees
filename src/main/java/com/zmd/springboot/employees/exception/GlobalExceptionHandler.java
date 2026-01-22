package com.zmd.springboot.employees.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        List<ValidationError> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> new ValidationError(
                        extractParamName(v.getPropertyPath().toString()),
                        v.getMessage()
                ))
                .toList();

        return buildProblemDetail(
                request,
                HttpStatus.BAD_REQUEST,
                ApiProblem.VALIDATION_ERROR,
                errors
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleBodyValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<ValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ValidationError(
                        err.getField(),
                        err.getDefaultMessage()
                ))
                .toList();

        return buildProblemDetail(
                request,
                HttpStatus.BAD_REQUEST,
                ApiProblem.VALIDATION_ERROR,
                errors
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        String expectedType = Optional.ofNullable(ex.getRequiredType())
                .map(Class::getSimpleName)
                .orElse("Unknown");

        List<ValidationError> errors = List.of(
                new ValidationError(
                        ex.getName(),
                        "must be of type " + expectedType
                )
        );

        return buildProblemDetail(
                request,
                HttpStatus.BAD_REQUEST,
                ApiProblem.TYPE_MISMATCH,
                errors
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleMalformedBody(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        return buildProblemDetail(
                request,
                HttpStatus.BAD_REQUEST,
                ApiProblem.MALFORMED_BODY,
                null
        );
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ProblemDetail handleBookNotFound(
            EmployeeNotFoundException ex,
            HttpServletRequest request
    ) {
        return buildProblemDetail(
                request,
                HttpStatus.NOT_FOUND,
                ApiProblem.NOT_FOUND,
                null
        );
    }

    private ProblemDetail buildProblemDetail(
            HttpServletRequest request,
            HttpStatus status,
            ApiProblem problem,
            List<ValidationError> errors
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(problem.getTitle());
        problemDetail.setDetail(problem.getDetail());
        problemDetail.setType(problem.getType());
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        if (errors != null && !errors.isEmpty()) {
            problemDetail.setProperty("errors", errors);
        }

        return problemDetail;
    }

    private String extractParamName(String path) {
        int lastDot = path.lastIndexOf('.');
        return lastDot != -1 ? path.substring(lastDot + 1) : path;
    }
}
