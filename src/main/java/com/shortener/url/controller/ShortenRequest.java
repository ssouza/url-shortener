package com.shortener.url.controller;

import java.io.Serializable;
import javax.validation.constraints.Pattern;

public class ShortenRequest implements Serializable {

    @Pattern(message = "A URL é inválida ou não foi informada", regexp = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$")
    private String url;

    public ShortenRequest() {
    }

    public String getUrl() {
        return url;
    }

    public ShortenRequest(String url) {
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
