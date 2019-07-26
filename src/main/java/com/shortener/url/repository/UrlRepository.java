package com.shortener.url.repository;

import com.shortener.url.common.Url;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByShortUrl(String shortUrl);

    @Query("FROM Url WHERE shortUrl = :shortUrl AND expiresAt > current_timestamp()")
    Optional<Url> findByShortUrlAndNotExpired(@Param("shortUrl") String shortUrl);

}
