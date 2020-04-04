package com.piisw.wozniak.lab3.server;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class WiremockServers {


        private Integer getPort;
        private Integer putPort;
        private Integer postPort;

        private static final String GET_PATH = "/rest/get/";
        private static final String PUT_PATH = "/rest/put/";
        private static final String POST_PATH = "/rest/post/";
        private static final String LOCALHOST = "localhost";

        @Autowired
        public WiremockServers(@Value ("${port.get}") Integer getPort, @Value ("${port.put}") Integer putPort,
                               @Value ("${port.post}") Integer postPort) {
                this.getPort = getPort;
                this.putPort = putPort;
                this.postPort = postPort;
        }

        public void runServices() {
                runGetServer();
                runPostServer();
                runPutServer();
        }

        private void runGetServer() {
                WireMockServer wireMockServer = new WireMockServer(getPort);
                wireMockServer.start();
                configureFor(LOCALHOST, getPort);
                stubFor(get(urlPathMatching(GET_PATH))
                                .willReturn(aResponse().withStatus(HttpStatus.OK.value()).withBody("GET response")));
        }

        private void runPostServer() {
                WireMockServer wireMockServer = new WireMockServer(postPort);
                wireMockServer.start();
                configureFor(LOCALHOST, postPort);
                stubFor(post(urlPathMatching(POST_PATH))
                                .willReturn(aResponse().withStatus(HttpStatus.OK.value()).withBody("POST response")));
        }

        private void runPutServer() {
                WireMockServer wireMockServer = new WireMockServer(putPort);
                wireMockServer.start();
                configureFor(LOCALHOST, putPort);
                stubFor(put(urlPathMatching(PUT_PATH))
                                .willReturn(aResponse().withStatus(HttpStatus.OK.value()).withBody("PUT response")));
        }
}
