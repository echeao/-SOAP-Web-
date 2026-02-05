package com.example.soap.server.service;

import com.example.soap.server.model.UserRequest;
import com.example.soap.server.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@WebService(
    endpointInterface = "com.example.soap.server.service.UserService",
    targetNamespace = "http://service.server.soap.example.com/",
    serviceName = "UserService",
    portName = "UserServicePort"
)
public class UserServiceImpl implements UserService {

    @Autowired
    private ServerStateService serverStateService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public UserResponse getUserInfo(UserRequest request) {
        String name = request != null ? request.getName() : "Unknown";
        int age = request != null ? request.getAge() : 0;

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String logMessage = String.format("[%s] Received: Name=%s, Age=%d", timestamp, name, age);

        // 1. Log internally
        serverStateService.logRequest(logMessage);
        System.out.println(logMessage);

        // 2. Notify Web UI (Server Dashboard) via WebSocket
        messagingTemplate.convertAndSend("/topic/logs", logMessage);

        // 3. Generate Response using dynamic template
        String message = String.format(serverStateService.getResponseTemplate(), name, age);

        return new UserResponse(message);
    }
}
