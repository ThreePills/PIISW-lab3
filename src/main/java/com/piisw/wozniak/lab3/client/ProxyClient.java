package com.piisw.wozniak.lab3.client;

import com.piisw.wozniak.lab3.utils.ProxyClientUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class ProxyClient {

        private RestTemplate restTemplate;
        private ProxyClientUtils proxyClientUtils;

        public ProxyClient(String localUri) {
                this.restTemplate = new RestTemplate();
                this.proxyClientUtils = new ProxyClientUtils(localUri);
        }

        public ResponseEntity<String> executeGet(Integer port, String requestUri,
                                                 MultiValueMap<String, String> requestHeaders) {
                ResponseEntity<String> response = restTemplate
                        .exchange(proxyClientUtils.generateLocalUrlWithRequestUri(port, requestUri), HttpMethod.GET,
                                  proxyClientUtils.createEntity(requestHeaders, port), String.class);
                return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders())
                                     .body(response.getBody());
        }

        public ResponseEntity<String> executePost(Integer port, String requestURI,
                                                  MultiValueMap<String, String> requestHeaders, String requestBody) {
                ResponseEntity<String> responseEntity = restTemplate
                        .exchange(proxyClientUtils.generateLocalUrlWithRequestUri(port, requestURI), HttpMethod.POST,
                                  proxyClientUtils.createEntity(requestBody, requestHeaders, port), String.class);
                return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders())
                                     .body(responseEntity.getBody());
        }

        public ResponseEntity<String> executePut(Integer port, String requestURI,
                                                 MultiValueMap<String, String> requestHeaders, String requestBody) {
                ResponseEntity<String> responseEntity = restTemplate
                        .exchange(proxyClientUtils.generateLocalUrlWithRequestUri(port, requestURI), HttpMethod.PUT,
                                  proxyClientUtils.createEntity(requestBody, requestHeaders, port), String.class);
                return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders())
                                     .body(responseEntity.getBody());
        }


}
