package com.example.soap.client.config;

import com.example.soap.client.generated.UserService;
import com.example.soap.client.generated.UserService_Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
public class SoapConfig {

    @Bean
    public UserService userService() {
        // Load WSDL from classpath
        URL wsdlUrl = getClass().getResource("/user.wsdl");
        if (wsdlUrl == null) {
            // Fallback (though unlikely if built correctly)
            try {
                wsdlUrl = new URL("http://localhost:8089/services/user?wsdl");
            } catch (Exception e) {
                throw new RuntimeException("Cannot locate WSDL", e);
            }
        }

        UserService_Service service = new UserService_Service(wsdlUrl);
        return service.getUserServicePort();
    }
}
