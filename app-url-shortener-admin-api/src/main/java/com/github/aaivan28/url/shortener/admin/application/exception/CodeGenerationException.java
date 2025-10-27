package com.github.aaivan28.url.shortener.admin.application.exception;

public class CodeGenerationException extends RuntimeException {
    public CodeGenerationException(final int attempts) {
        super("Failed to generate unique code after " + attempts + " attempts.");
    }
}
