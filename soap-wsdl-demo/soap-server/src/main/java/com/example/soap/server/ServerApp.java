package com.example.soap.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApp {
    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class, args);
        System.out.println("SOAP Server (Spring Boot) started on http://localhost:8089");
        System.out.println("SOAP Endpoint: http://localhost:8089/services/user?wsdl");
    }
}
