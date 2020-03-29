package com.piisw.wozniak.lab3;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class Lab3ControllersTest {

    @Value("${destination.get}")
    private String urlGet;
    @Value("${destination.put}")
    private String urlPut;
    @Value("${destination.post}")
    private String urlPost;

    private int portPost = 9090;
    private int portPut = 9080;
    private int portGet = 9070;

    private static final String ERROR_500_MSG = "Cannot establish connection";

    private WireMockServer wireMockGet = new WireMockServer(portGet);
    private WireMockServer wireMockPost = new WireMockServer(portPost);
    private WireMockServer wireMockPut = new WireMockServer(portPut);


    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void setup() {
        wireMockGet.start();
        wireMockPost.start();
        wireMockPut.start();

    }

    @AfterEach
    void tearDown() {
        wireMockGet.stop();
        wireMockPost.stop();
        wireMockPut.stop();
    }

    @Test
    public void testPost() {
        String controllerUri = "/rest/post/";
        wireMockPost.stubFor(post(controllerUri).willReturn(aResponse().withStatus(200).withBody("aaa")));
        ResponseEntity responseEntity = this.testRestTemplate.exchange(createUrlWithUri(controllerUri), HttpMethod.POST, new HttpEntity<>("aaa"), String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("aaa", responseEntity.getBody().toString());
    }

    @Test
    public void testPostInternalError() {
        String controllerUri = "/rest/post/";
        wireMockPost.stop();
        ResponseEntity responseEntity = this.testRestTemplate.exchange(createUrlWithUri(controllerUri), HttpMethod.POST, new HttpEntity<>("asd"), String.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(ERROR_500_MSG, responseEntity.getBody().toString());

    }

    @Test
    public void testPostNotFound() {
        String controllerUri = "/rest/post/";
        ResponseEntity responseEntity = this.testRestTemplate.exchange(createUrlWithUri(controllerUri), HttpMethod.POST, new HttpEntity<>("aaa"), String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testPut() {
        String controllerUri = "/rest/put/";
        wireMockPut.stubFor(put(urlPathMatching(controllerUri)).willReturn(aResponse().withStatus(200).withBody("aaa")));
        ResponseEntity responseEntity = this.testRestTemplate.exchange(createUrlWithUri(controllerUri), HttpMethod.PUT, new HttpEntity<>("aaa"), String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("aaa", responseEntity.getBody().toString());
    }

    @Test
    public void testPutInternalError() {
        String controllerUri = "/rest/put/";
        wireMockPut.stop();
        ResponseEntity responseEntity = this.testRestTemplate.exchange(createUrlWithUri(controllerUri), HttpMethod.PUT, new HttpEntity<>("aaa"), String.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(ERROR_500_MSG, responseEntity.getBody().toString());
    }

    @Test
    public void testPutNotFound() {
        String controllerUri = "/rest/put/";
        ResponseEntity responseEntity = this.testRestTemplate.exchange(createUrlWithUri(controllerUri), HttpMethod.PUT, new HttpEntity<>("aaa"), String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGet() {
        String controllerUri = "/rest/get/";
        wireMockGet.stubFor(get(urlEqualTo(controllerUri)).willReturn(aResponse().withStatus(HttpStatus.OK.value()).withBody("aaa")));
        ResponseEntity responseEntity = this.testRestTemplate.exchange(createUrlWithUri(controllerUri), HttpMethod.GET, HttpEntity.EMPTY, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("aaa", responseEntity.getBody().toString());
    }

    @Test
    public void testGetInternalError() {
        String controllerUri = "/rest/get/";
        wireMockGet.stop();
        ResponseEntity responseEntity = this.testRestTemplate.exchange(createUrlWithUri(controllerUri), HttpMethod.GET, HttpEntity.EMPTY, String.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(ERROR_500_MSG, responseEntity.getBody().toString());
    }

    @Test
    public void testGetNotFound() {
        String controllerUri = "/rest/get/";
        ResponseEntity responseEntity = this.testRestTemplate.exchange(createUrlWithUri(controllerUri), HttpMethod.GET, HttpEntity.EMPTY, String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    private String createUrlWithUri(String uri) {
        return "http://localhost:8080" + uri;
    }

}
