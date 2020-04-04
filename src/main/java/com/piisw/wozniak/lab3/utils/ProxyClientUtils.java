package com.piisw.wozniak.lab3.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

public class ProxyClientUtils {

        private String localUri;

        public ProxyClientUtils(String localUri) {
                this.localUri = localUri;
        }

        private String generateLocalUrlWithPort(Integer port) {
                StringBuilder builder = new StringBuilder();
                builder.append(localUri);
                if (port != null) {
                        builder.append(port);
                }
                return builder.toString();
        }

        public String generateLocalUrlWithRequestUri(Integer port, String requestUri) {
                StringBuilder builder = new StringBuilder();
                builder.append(generateLocalUrlWithPort(port));
                if (requestUri != null) {
                        builder.append(requestUri);
                }
                return builder.toString();
        }

        private HttpHeaders composeRequestHeader(MultiValueMap<String, String> headers, Integer port) {
                String url = generateLocalUrlWithPort(port);
                HttpHeaders requestHeaders = new HttpHeaders(headers);
                requestHeaders.set(HttpHeaders.HOST, url);
                requestHeaders.set(HttpHeaders.ACCEPT_ENCODING, null);
                return requestHeaders;
        }

        public HttpEntity<String> createEntity(MultiValueMap<String, String> headers, Integer port) {
                return new HttpEntity<>(composeRequestHeader(headers, port));
        }

        public HttpEntity<String> createEntity(String requestBody, MultiValueMap<String, String> headers,
                                               Integer port) {
                return new HttpEntity<>(requestBody, composeRequestHeader(headers, port));
        }
}
