package com.github.aaivan28.url.shortener.admin.application.exception;

public class CodeGenerationException extends RuntimeException {
    public CodeGenerationException() {
        super("Error generating distinct code: Retry limit exceeded");
    }
}
