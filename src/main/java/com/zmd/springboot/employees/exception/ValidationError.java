package com.zmd.springboot.employees.exception;

public record ValidationError(String field, String message) {}