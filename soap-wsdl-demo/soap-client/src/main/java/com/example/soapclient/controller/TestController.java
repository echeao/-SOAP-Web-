package com.example.soapclient.controller;

import com.example.soapclient.generated.UserResponse;
import com.example.soapclient.service.SoapClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器 - 提供 REST API 触发 SOAP 调用
 * 
 * 【教学说明】
 * 这个控制器提供简单的 REST 接口，方便通过浏览器或 cURL 测试 SOAP 调用。
 * 
 * 接口列表：
 * - GET /api/test/call    - 调用 getUserInfo
 * - GET /api/test/create  - 调用 createUser
 * - GET /api/test/health  - 调用 healthCheck
 * 
 * 【为什么使用 REST 包装 SOAP？】
 * 1. 方便测试：可以直接在浏览器地址栏输入 URL
 * 2. 易于理解：REST 接口更直观
 * 3. 调试方便：可以使用 Postman 等工具
 * 
 * @author 教学示例
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    /**
     * SOAP 客户端服务
     */
    private final SoapClientService soapClientService;
    
    /**
     * 构造方法 - 注入依赖
     */
    public TestController(SoapClientService soapClientService) {
        this.soapClientService = soapClientService;
    }
    
    /**
     * 调用 getUserInfo 操作
     * 
     * 使用方式：
     *   GET http://localhost:8080/api/test/call?name=张三&age=25
     * 
     * @param name 用户姓名
     * @param age  用户年龄
     * @return SOAP 响应（转换为 JSON）
     */
    @GetMapping("/call")
    public ResponseEntity<?> callGetUserInfo(
            @RequestParam(value = "name", defaultValue = "测试用户") String name,
            @RequestParam(value = "age", defaultValue = "25") int age) {
        
        try {
            UserResponse response = soapClientService.getUserInfo(name, age);
            return ResponseEntity.ok(convertToMap(response));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 调用 createUser 操作
     * 
     * 使用方式：
     *   GET http://localhost:8080/api/test/create?name=李四&age=30
     * 
     * @param name 用户姓名
     * @param age  用户年龄
     * @return SOAP 响应（转换为 JSON）
     */
    @GetMapping("/create")
    public ResponseEntity<?> callCreateUser(
            @RequestParam(value = "name", defaultValue = "新用户") String name,
            @RequestParam(value = "age", defaultValue = "20") int age) {
        
        try {
            UserResponse response = soapClientService.createUser(name, age);
            return ResponseEntity.ok(convertToMap(response));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 调用 healthCheck 操作
     * 
     * 使用方式：
     *   GET http://localhost:8080/api/test/health
     * 
     * @return 服务状态响应
     */
    @GetMapping("/health")
    public ResponseEntity<?> callHealthCheck() {
        try {
            UserResponse response = soapClientService.healthCheck();
            return ResponseEntity.ok(convertToMap(response));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * 首页 - 显示可用接口列表
     * 
     * 使用方式：
     *   GET http://localhost:8080/api/test
     */
    @GetMapping
    public ResponseEntity<?> index() {
        Map<String, Object> info = new HashMap<>();
        info.put("message", "SOAP Client 测试接口");
        info.put("endpoints", new String[]{
            "GET /api/test/call?name=张三&age=25  - 获取用户信息",
            "GET /api/test/create?name=李四&age=30 - 创建用户",
            "GET /api/test/health - 健康检查"
        });
        return ResponseEntity.ok(info);
    }
    
    /**
     * 将 UserResponse 转换为 Map（便于 JSON 序列化）
     * 
     * 【教学说明】
     * wsimport 生成的类可能没有标准的 JSON 序列化注解，
     * 手动转换为 Map 可以确保响应格式正确。
     */
    private Map<String, Object> convertToMap(UserResponse response) {
        if (response == null) {
            return null;
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("status", response.getStatus());
        result.put("code", response.getCode());
        result.put("message", response.getMessage());
        
        if (response.getData() != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("userId", response.getData().getUserId());
            data.put("name", response.getData().getName());
            data.put("age", response.getData().getAge());
            data.put("greeting", response.getData().getGreeting());
            data.put("timestamp", response.getData().getTimestamp());
            result.put("data", data);
        }
        
        return result;
    }
    
    /**
     * 创建错误响应
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", "ERROR");
        error.put("code", 500);
        error.put("message", message);
        error.put("hint", "请确保 SOAP Server 已启动在 http://localhost:8089");
        return error;
    }
}
