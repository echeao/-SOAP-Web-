package com.example.soapclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SOAP Client 启动类
 * 
 * 【教学说明】
 * 这是 Spring Boot 应用的入口类。
 * 
 * @SpringBootApplication 是一个组合注解，包含：
 * - @Configuration: 标记为配置类
 * - @EnableAutoConfiguration: 启用自动配置
 * - @ComponentScan: 扫描当前包及子包的组件
 * 
 * 【客户端职责】
 * 1. 提供 REST API 供用户触发 SOAP 调用
 * 2. 调用 SOAP Server 的 Web Service
 * 3. 将响应保存到 IFXML 目录
 * 
 * @author 教学示例
 * @since 1.0.0
 */
@SpringBootApplication
public class SoapClientApplication {
    
    /**
     * 主方法 - 启动 Spring Boot 应用
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           WSDL/SOAP Java 学习实践项目 - Client              ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        SpringApplication.run(SoapClientApplication.class, args);
        
        System.out.println();
        System.out.println("【启动成功】SOAP Client 已启动！");
        System.out.println();
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ 测试接口:                                                    │");
        System.out.println("│   GET http://localhost:8080/api/test/call?name=张三&age=25  │");
        System.out.println("│   GET http://localhost:8080/api/test/health                 │");
        System.out.println("│   GET http://localhost:8080/api/test/create?name=李四&age=30│");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        System.out.println();
    }
}
