package com.zmd.springboot.employee.exception;

public record ValidationError(String field, String message) {}