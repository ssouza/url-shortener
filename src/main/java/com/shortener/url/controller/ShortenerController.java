package com.shortener.url.controller;

import com.shortener.url.common.Url;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.shortener.url.service.UrlService;
import java.time.LocalDateTime;
import javax.validation.Valid;
import static org.springframework.util.StringUtils.startsWithIgnoreCase;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class ShortenerController {

    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "https://";

    @Autowired
    private UrlService urlService;

    @GetMapping(value = {"/shorten/{shortUrl}"})
    public ResponseEntity<Url> find(@PathVariable String shortUrl) throws URISyntaxException {
        Url url = urlService.findByShortUrl(shortUrl);

        if (url == null) {
            return ResponseEntity.notFound().build();
        }
        if (!url.getExpiresAt().isAfter(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }

        String originalUrl = url.getOriginalUrl();
        if (!startsWithIgnoreCase(originalUrl, HTTP_PREFIX) && !startsWithIgnoreCase(originalUrl, HTTPS_PREFIX)) {
            originalUrl = HTTP_PREFIX.concat(originalUrl);
        }

        URI uri = new URI(originalUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(httpHeaders).build();
    }

    @PostMapping(path = "/shorten", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<ShortenResponse> create(@RequestBody @Valid ShortenRequest shortenRequest) {
        Url url = urlService.save(shortenRequest.getUrl());
        String urlFullPath = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/" + url.getShortUrl()).build().toUriString();
        ShortenResponse shortUrl = new ShortenResponse(urlFullPath, url.getExpiresAt());
        return new ResponseEntity(shortUrl, HttpStatus.CREATED);
    }

}
