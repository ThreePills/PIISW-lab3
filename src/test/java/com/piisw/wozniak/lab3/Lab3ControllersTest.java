package com.piisw.wozniak.lab3;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

@RunWith (SpringRunner.class)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext (classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration (initializers = ConfigFileApplicationContextInitializer.class)
class Lab3ControllersTest {


        private static final String ERROR_500_MSG = "Cannot establish connection";

        private WireMockServer wireMockGet = new WireMockServer(9070);
        private WireMockServer wireMockPost = new WireMockServer(9090);
        private WireMockServer wireMockPut = new WireMockServer(9080);

        @LocalServerPort
        private int localPort;

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
                //given
                String controllerUri = "/rest/post/";
                wireMockPost.stubFor(post(controllerUri).willReturn(aResponse().withStatus(200).withBody("aaa")));

                //when
                ResponseEntity responseEntity = this.testRestTemplate
                        .exchange(createUrlWithUri(controllerUri), HttpMethod.POST, new HttpEntity<>("aaa"),
                                  String.class);

                //then
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                assertEquals("aaa", responseEntity.getBody().toString());
        }

        @Test
        public void testPostInternalError() {
                //given
                String controllerUri = "/rest/post/";
                wireMockPost.stop();

                //when
                ResponseEntity responseEntity = this.testRestTemplate
                        .exchange(createUrlWithUri(controllerUri), HttpMethod.POST, new HttpEntity<>("asd"),
                                  String.class);

                //then
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
                assertEquals(ERROR_500_MSG, responseEntity.getBody().toString());

        }

        @Test
        public void testPostNotFound() {
                //given
                String controllerUri = "/rest/post/";

                //when
                ResponseEntity responseEntity = this.testRestTemplate
                        .exchange(createUrlWithUri(controllerUri), HttpMethod.POST, new HttpEntity<>("aaa"),
                                  String.class);

                //then
                assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @Test
        public void testPut() {
                //given
                String controllerUri = "/rest/put/";
                wireMockPut.stubFor(
                        put(urlPathMatching(controllerUri)).willReturn(aResponse().withStatus(200).withBody("aaa")));

                //when
                ResponseEntity responseEntity = this.testRestTemplate
                        .exchange(createUrlWithUri(controllerUri), HttpMethod.PUT, new HttpEntity<>("aaa"),
                                  String.class);

                //then
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                assertEquals("aaa", responseEntity.getBody().toString());
        }

        @Test
        public void testPutInternalError() {
                //given
                String controllerUri = "/rest/put/";
                wireMockPut.stop();

                //when
                ResponseEntity responseEntity = this.testRestTemplate
                        .exchange(createUrlWithUri(controllerUri), HttpMethod.PUT, new HttpEntity<>("aaa"),
                                  String.class);

                //then
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
                assertEquals(ERROR_500_MSG, responseEntity.getBody().toString());
        }

        @Test
        public void testPutNotFound() {
                //given
                String controllerUri = "/rest/p/";
                ResponseEntity responseEntity = this.testRestTemplate
                        .exchange(createUrlWithUri(controllerUri), HttpMethod.PUT, new HttpEntity<>("aaa"),
                                  String.class);

                //then
                assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @Test
        public void testGet() {
                //given
                String controllerUri = "/rest/get/";
                wireMockGet.stubFor(get(urlMatching(controllerUri)).willReturn(
                        aResponse().withStatus(HttpStatus.OK.value()).withHeader("Content-Type", "application/json")
                                   .withBody("aaa")));

                //when
                ResponseEntity responseEntity = this.testRestTemplate
                        .exchange(createUrlWithUri(controllerUri), HttpMethod.GET, new HttpEntity<>("aaa"),
                                  String.class);

                //then
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                assertEquals("aaa", responseEntity.getBody().toString());
        }

        @Test
        public void testGetInternalError() {
                //given
                String controllerUri = "/rest/get/";
                wireMockGet.stop();

                //when
                ResponseEntity responseEntity = this.testRestTemplate
                        .exchange(createUrlWithUri(controllerUri), HttpMethod.GET, HttpEntity.EMPTY, String.class);

                //then
                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
                assertEquals(ERROR_500_MSG, responseEntity.getBody().toString());
        }

        @Test
        public void testGetNotFound() {
                //given
                String controllerUri = "/rest/get/";

                //when
                ResponseEntity responseEntity = this.testRestTemplate
                        .exchange(createUrlWithUri(controllerUri), HttpMethod.GET, HttpEntity.EMPTY, String.class);

                //then
                assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        private String createUrlWithUri(String uri) {
                return "http://localhost:" + localPort + uri;
        }

}
