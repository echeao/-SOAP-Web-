package com.example.soap.server;

import com.example.soap.server.service.UserServiceImpl;
import javax.xml.ws.Endpoint;

public class ServerApp {
    public static void main(String[] args) {
        String address = "http://localhost:8089/ws/user";
        System.out.println("Starting SOAP Server...");
        System.out.println("Publishing Service at: " + address);

        Endpoint.publish(address, new UserServiceImpl());

        System.out.println("SOAP Server started successfully!");
        System.out.println("WSDL available at: " + address + "?wsdl");
    }
}
