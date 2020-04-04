package com.piisw.wozniak.lab3;

import com.piisw.wozniak.lab3.server.WiremockServers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Lab3Application {

        public static void main(String[] args) {
                SpringApplication.run(Lab3Application.class, args);
                WiremockServers wiremockServers = new WiremockServers(9070, 9080, 9090);
                wiremockServers.runServices();
        }
}
