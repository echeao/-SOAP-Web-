package com.example.soap.server.controller;

import com.example.soap.server.service.ServerStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/server")
public class ServerApiController {

    @Autowired
    private ServerStateService serverStateService;

    @GetMapping("/template")
    public String getTemplate() {
        return serverStateService.getResponseTemplate();
    }

    @PostMapping("/template")
    public void updateTemplate(@RequestBody Map<String, String> payload) {
        if (payload.containsKey("template")) {
            serverStateService.setResponseTemplate(payload.get("template"));
        }
    }
}
