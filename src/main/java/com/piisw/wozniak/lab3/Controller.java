package com.piisw.wozniak.lab3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("rest")
public class Controller {

    @Value("${destination.get}")
    String getUrl;

    @Value("${destination.post}")
    String postUrl;

    @Value("${destination.put}")
    String putUrl;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping (value = "get")
    public ResponseEntity get(){
        return restTemplate.exchange(getUrl+"/rest/get/",HttpMethod.GET, HttpEntity.EMPTY,String.class);

    }

    @PostMapping (value = "post")
    public ResponseEntity<String> postSomething(@RequestBody String string){
        return restTemplate.exchange(postUrl+"/rest/post/",HttpMethod.POST, new HttpEntity<>(string),String.class);
    }

    @PutMapping(value = "put")
    public ResponseEntity<String> putSomething(@RequestBody String string){
        return restTemplate.exchange(putUrl+"/rest/put/", HttpMethod.PUT,new HttpEntity<>(string), String.class);
    }


}
