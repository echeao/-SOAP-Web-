package com.example.soap.server.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ServerStateService {

    private String responseTemplate = "%s，%d岁，信息接收成功 (Server: Spring Boot)";
    private final List<String> requestLog = new CopyOnWriteArrayList<>();

    public String getResponseTemplate() {
        return responseTemplate;
    }

    public void setResponseTemplate(String responseTemplate) {
        this.responseTemplate = responseTemplate;
    }

    public void logRequest(String log) {
        requestLog.add(0, log); // Add to top
        if (requestLog.size() > 50) {
            requestLog.remove(requestLog.size() - 1);
        }
    }

    public List<String> getRequestLog() {
        return new ArrayList<>(requestLog);
    }
}
