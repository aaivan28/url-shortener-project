package com.github.aaivan28.url.shortener.admin.application.exception;

public class UrlNotFoundException extends RuntimeException {

    public UrlNotFoundException() {
        super("URL not found");
    }
}
