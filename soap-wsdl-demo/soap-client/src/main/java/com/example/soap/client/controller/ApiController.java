package com.example.soap.client.controller;

import com.example.soap.client.service.SoapClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private SoapClientService soapClientService;

    @PostMapping("/check-user")
    public CheckUserResponse checkUser(@RequestBody CheckUserRequest request) {
        String result = soapClientService.checkUser(request.getName(), request.getAge());
        return new CheckUserResponse(result);
    }

    // DTOs
    public static class CheckUserRequest {
        private String name;
        private int age;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }

    public static class CheckUserResponse {
        private String message;

        public CheckUserResponse(String message) {
            this.message = message;
        }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
