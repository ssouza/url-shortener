package com.shortener.url.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortener.url.common.Url;
import com.shortener.url.service.UrlService;
import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ShortenerController.class)
public class ShortenerControllerTest {

    @MockBean
    private UrlService urlService;
    @Autowired
    private MockMvc mvc;

    @Test
    public void testFindByShortUrlWhenNotFound() throws Exception {
        when(urlService.findByShortUrl("XpTo")).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.get("/shorten/XpTO"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByShortUrlWhenExpired() throws Exception {
        Url url = createUrlEntity();
        url.setExpiresAt(LocalDateTime.now().minusMinutes(5));

        when(urlService.findByShortUrl(url.getShortUrl())).thenReturn(url);

        mvc.perform(MockMvcRequestBuilders.get("/shorten/sHorTuRL"))
                .andExpect(status().isGone());
    }

    @Test
    public void testFindByShortUrl() throws Exception {
        Url url = createUrlEntity();

        when(urlService.findByShortUrl(url.getShortUrl())).thenReturn(url);

        mvc.perform(MockMvcRequestBuilders.get("/shorten/sHorTuRL"))
                .andExpect(status().isSeeOther());
    }

    @Test
    public void testFindByShortUrlWhenContainsHttpUrl() throws Exception {
        Url url = createUrlEntity();
        url.setOriginalUrl("http://www.google.com");

        when(urlService.findByShortUrl(url.getShortUrl())).thenReturn(url);

        mvc.perform(MockMvcRequestBuilders.get("/shorten/sHorTuRL"))
                .andExpect(status().isSeeOther());
    }

    @Test
    public void testFindByShortUrlWhenContainsHttpsUrl() throws Exception {
        Url url = createUrlEntity();
        url.setOriginalUrl("HTTPS://www.google.com");

        when(urlService.findByShortUrl(url.getShortUrl())).thenReturn(url);

        mvc.perform(MockMvcRequestBuilders.get("/shorten/sHorTuRL"))
                .andExpect(status().isSeeOther());
    }

    @Test
    public void testCreateShortUrlWhenThereIsNoContent() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateShortUrl() throws Exception {
        Url url = createUrlEntity();
        when(urlService.save(url.getOriginalUrl())).thenReturn(url);

        mvc.perform(MockMvcRequestBuilders.post("/shorten")
                .content(shortenRequestAsJsonString(url.getOriginalUrl()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        ).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.newUrl").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.newUrl").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiresAt").isNotEmpty());
    }

    private static Url createUrlEntity() {
        Url url = new Url("www.google.com", LocalDateTime.now().plusMinutes(30));
        url.setShortUrl("sHorTuRL");
        return url;
    }

    private static String shortenRequestAsJsonString(String originalUrl) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(new ShortenRequest(originalUrl));
    }

}
