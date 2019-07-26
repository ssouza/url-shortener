package com.shortener.url.repository;

import com.shortener.url.common.Url;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UrlRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UrlRepository urlRepository;

    @Before
    public void setup() {
        Url url = new Url("www.google.com", LocalDateTime.now().plusDays(1));
        url.setShortUrl("GoooooGlee");
        testEntityManager.persist(url);

        url = new Url("www.globo.com", LocalDateTime.now().minusDays(1));
        url.setShortUrl("GLOboO1");
        testEntityManager.persist(url);
    }

    @Test
    public void testFindByShortUrlWhenNotFound() {
        Optional<Url> optional = urlRepository.findByShortUrl("XpTO");
        assertFalse(optional.isPresent());
    }

    @Test
    public void testFindByShortUrl() {
        Optional<Url> optional = urlRepository.findByShortUrl("GoooooGlee");
        assertTrue(optional.isPresent());
    }

    @Test
    public void findByShortUrlAndNotExpired() {
        Optional<Url> optional = urlRepository.findByShortUrlAndNotExpired("GoooooGlee");
        assertTrue(optional.isPresent());
    }

    @Test
    public void findByShortUrlAndExpired() {
        Optional<Url> optional = urlRepository.findByShortUrlAndNotExpired("GLOboO1");
        assertFalse(optional.isPresent());
    }

    @Test
    public void testSave() {
        Url url = new Url("www.hotmail.com", LocalDateTime.now().plusDays(1));
        url.setShortUrl("sHortUrL");
        Url urlSaved = urlRepository.save(url);
        assertNotNull(urlSaved);
        assertNotNull(urlSaved.getId());
    }

}
