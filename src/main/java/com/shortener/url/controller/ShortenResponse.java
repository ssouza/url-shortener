package com.shortener.url.controller;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ShortenResponse implements Serializable {

    private final String newUrl;
    private final LocalDateTime expiresAt;

    public ShortenResponse(String newUrl, LocalDateTime expiresAt) {
        this.newUrl = newUrl;
        this.expiresAt = expiresAt;
    }

    public String getNewUrl() {
        return newUrl;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

}
