package com.example.soap.server.config;

import com.example.soap.server.service.UserService;
import com.example.soap.server.service.UserServiceImpl;
import javax.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfConfig {

    @Autowired
    private Bus bus;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, userServiceImpl);
        // This publishes the service at /ws/user (relative to CXF servlet path, usually /services)
        // By default CXF starter maps to /services/*
        // So full URL: http://localhost:8089/services/user
        endpoint.publish("/user");
        return endpoint;
    }
}
