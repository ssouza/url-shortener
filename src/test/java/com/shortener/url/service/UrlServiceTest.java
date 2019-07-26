package com.shortener.url.service;

import com.shortener.url.common.Url;
import com.shortener.url.repository.UrlRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UrlService.class)
@TestPropertySource(locations = "classpath:test.properties")
public class UrlServiceTest {

    @TestConfiguration
    static class UrlServiceTestContextConfiguration {

        @Bean
        public UrlService urlService() {
            return new UrlService();
        }
    }

    @MockBean
    private UrlRepository urlRepository;
    @Autowired
    private UrlService urlService;

    @Test
    public void testSave() {
        Url url = new Url("www.Url.com", LocalDateTime.now());
        when(urlRepository.save(url)).thenReturn(url);
        when(urlRepository.findByShortUrlAndNotExpired(any(String.class))).thenReturn(Optional.of(url), Optional.empty());
        assertNotNull(urlService.save(url.getOriginalUrl()));
        verify(urlRepository, times(2)).findByShortUrlAndNotExpired(any(String.class));
    }

    @Test
    public void testFindByShortUrl() {
        Url url = new Url("www.Url.com", LocalDateTime.now());
        when(urlRepository.findFirstByShortUrlOrderByExpiresAtDesc("XpTo")).thenReturn(Optional.of(url));
        assertNotNull(urlService.findByShortUrl("XpTo"));
    }

    @Test
    public void testFindByShortUrlWhenNotFound() {
        when(urlRepository.findFirstByShortUrlOrderByExpiresAtDesc("XpTo")).thenReturn(Optional.empty());
        assertNull(urlService.findByShortUrl("XpTo"));
    }

}
