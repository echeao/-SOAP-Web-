package com.example.soap.client.service;

import com.example.soap.client.generated.UserRequest;
import com.example.soap.client.generated.UserResponse;
import com.example.soap.client.generated.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SoapClientService {

    @Autowired
    private UserService userService;

    public String checkUser(String name, int age) {
        UserRequest request = new UserRequest();
        request.setName(name);
        request.setAge(age);

        System.out.println("Calling SOAP Service with Name: " + name + ", Age: " + age);
        UserResponse response = userService.getUserInfo(request);

        return response.getMessage();
    }
}
