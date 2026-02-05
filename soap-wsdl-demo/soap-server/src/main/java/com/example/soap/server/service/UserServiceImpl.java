package com.example.soap.server.service;

import com.example.soap.server.model.UserRequest;
import com.example.soap.server.model.UserResponse;

import javax.jws.WebService;

@WebService(
    endpointInterface = "com.example.soap.server.service.UserService",
    targetNamespace = "http://service.server.soap.example.com/",
    serviceName = "UserService",
    portName = "UserServicePort"
)
public class UserServiceImpl implements UserService {

    @Override
    public UserResponse getUserInfo(UserRequest request) {
        String name = request != null ? request.getName() : "Unknown";
        int age = request != null ? request.getAge() : 0;

        String message = String.format("%s，%d岁，信息接收成功", name, age);
        System.out.println("Processing request: " + message);

        return new UserResponse(message);
    }
}
