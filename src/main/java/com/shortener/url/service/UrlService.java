package com.shortener.url.service;

import com.shortener.url.common.Url;
import com.shortener.url.repository.UrlRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    @Value("${expiration.timeInMinutes}")
    private Long expirationTimeInMinutes;

    @Autowired
    private UrlRepository urlRepository;

    public Url findByShortUrl(String shortUrl) {
        Optional<Url> optional = urlRepository.findByShortUrl(shortUrl);
        return optional.orElse(null);
    }

    public Url save(String originalUrl) {
        Url url = new Url(originalUrl, LocalDateTime.now().plusMinutes(expirationTimeInMinutes));
        url = urlRepository.save(url);
        url.setShortUrl(generateUniqueShortUrl());
        return urlRepository.save(url);
    }

    private String generateUniqueShortUrl() {
        String shortUrl;
        Optional<Url> optional;
        do {
            shortUrl = RandomStringUtils.randomAlphanumeric(15);
            optional = urlRepository.findByShortUrlAndNotExpired(shortUrl);
        } while (optional.orElse(null) != null);
        return shortUrl;
    }

}
