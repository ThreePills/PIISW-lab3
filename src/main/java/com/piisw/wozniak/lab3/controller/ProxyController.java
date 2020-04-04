package com.piisw.wozniak.lab3.controller;

import javax.servlet.http.HttpServletRequest;

import com.piisw.wozniak.lab3.client.ProxyClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping ("rest")
public class ProxyController {

        private Integer getPort;
        private Integer postPort;
        private Integer putPort;
        private ProxyClient proxyClient;

        public ProxyController(@Value ("${port.get}") Integer getPort, @Value ("${port.post}") Integer postPort,
                               @Value ("${port.put}") Integer putPort, @Value ("${local.uri}") String localUrl) {
                proxyClient = new ProxyClient(localUrl);
                this.getPort = getPort;
                this.postPort = postPort;
                this.putPort = putPort;
        }

        @GetMapping (value = "get")
        @ResponseBody
        public ResponseEntity<String> getSomethig(HttpServletRequest httpServletRequest,
                                          @RequestHeader MultiValueMap<String, String> requestHeaders) {

                return proxyClient.executeGet(getPort, httpServletRequest.getRequestURI(), requestHeaders);
        }

        @PostMapping (value = "post")
        @ResponseBody
        public ResponseEntity<String> postSomething(HttpServletRequest httpServletRequest,
                                                    @RequestHeader MultiValueMap<String, String> requestHeaders,
                                                    @RequestBody String requestBody) {

                return proxyClient.executePost(postPort, httpServletRequest.getRequestURI(), requestHeaders, requestBody);
        }


        @PutMapping (value = "put")
        public ResponseEntity<String> putSomething(HttpServletRequest httpServletRequest,
                                                   @RequestHeader MultiValueMap<String, String> requestHeaders,
                                                   @RequestBody String requestBody) {

                return proxyClient.executePut(putPort, httpServletRequest.getRequestURI(), requestHeaders, requestBody);
        }


}
