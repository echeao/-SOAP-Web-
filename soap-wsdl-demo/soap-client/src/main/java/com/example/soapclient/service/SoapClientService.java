package com.example.soapclient.service;

import com.example.soapclient.generated.*;
import com.example.soapclient.util.XmlFileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.ws.BindingProvider;
import java.net.URL;

/**
 * SOAP 客户端服务 - 封装 SOAP 调用逻辑
 * 
 * 【教学说明】
 * 这个类展示了如何使用 wsimport 生成的代码调用 SOAP 服务。
 * 
 * 调用 SOAP 服务的标准流程：
 * 1. 创建 Service 实例（由 wsimport 生成）
 * 2. 获取 Port（类似于获取远程接口的代理）
 * 3. 调用 Port 上的方法（就像调用本地方法一样）
 * 
 * 【生成的类说明】
 * - UserServiceImplService: 服务工厂类，用于创建 Port
 * - UserService: 服务接口，定义可调用的操作
 * - UserResponse: 响应对象，包含返回数据
 * 
 * @author 教学示例
 * @since 1.0.0
 */
@Service
public class SoapClientService {
    
    /**
     * SOAP 服务端地址（从配置文件读取）
     */
    @Value("${soap.server.url:http://localhost:8089/ws/user}")
    private String serverUrl;
    
    /**
     * XML 文件工具类
     */
    private final XmlFileUtil xmlFileUtil;
    
    /**
     * 构造方法 - 注入依赖
     */
    public SoapClientService(XmlFileUtil xmlFileUtil) {
        this.xmlFileUtil = xmlFileUtil;
    }
    
    /**
     * 调用 getUserInfo 操作
     * 
     * 【流程说明】
     * 1. 创建 Service 实例
     * 2. 获取 Port
     * 3. 调用远程方法
     * 4. 保存响应到文件
     * 5. 返回响应数据
     * 
     * @param name 用户姓名
     * @param age  用户年龄
     * @return 用户响应对象
     */
    public UserResponse getUserInfo(String name, int age) {
        System.out.println("【Client】调用 getUserInfo: name=" + name + ", age=" + age);
        
        try {
            // 步骤 1: 创建 Service 实例
            // UserServiceImplService 是 wsimport 生成的服务工厂类
            UserServiceImplService service = new UserServiceImplService();
            
            // 步骤 2: 获取 Port（服务端口/代理）
            // Port 是一个动态代理，调用其方法会发送 SOAP 请求
            UserService port = service.getUserServiceImplPort();
            
            // 配置服务端地址（允许覆盖默认地址）
            configureEndpoint(port);
            
            // 步骤 3: 调用远程方法
            // 这里的调用会被转换为 SOAP 请求发送到服务端
            UserResponse response = port.getUserInfo(name, age);
            
            // 步骤 4: 保存响应到 XML 文件
            if (response != null) {
                xmlFileUtil.saveResponseToFile(response, "getUserInfo");
            }
            
            System.out.println("【Client】收到响应: " + response);
            return response;
            
        } catch (Exception e) {
            System.err.println("【Client】调用失败: " + e.getMessage());
            throw new RuntimeException("调用 SOAP 服务失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 调用 createUser 操作
     * 
     * @param name 用户姓名
     * @param age  用户年龄
     * @return 创建结果响应
     */
    public UserResponse createUser(String name, int age) {
        System.out.println("【Client】调用 createUser: name=" + name + ", age=" + age);
        
        try {
            UserServiceImplService service = new UserServiceImplService();
            UserService port = service.getUserServiceImplPort();
            configureEndpoint(port);
            
            UserResponse response = port.createUser(name, age);
            
            if (response != null) {
                xmlFileUtil.saveResponseToFile(response, "createUser");
            }
            
            System.out.println("【Client】收到响应: " + response);
            return response;
            
        } catch (Exception e) {
            System.err.println("【Client】调用失败: " + e.getMessage());
            throw new RuntimeException("调用 SOAP 服务失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 调用 healthCheck 操作
     * 
     * @return 服务状态响应
     */
    public UserResponse healthCheck() {
        System.out.println("【Client】调用 healthCheck");
        
        try {
            UserServiceImplService service = new UserServiceImplService();
            UserService port = service.getUserServiceImplPort();
            configureEndpoint(port);
            
            UserResponse response = port.healthCheck();
            
            System.out.println("【Client】健康检查结果: " + response);
            return response;
            
        } catch (Exception e) {
            System.err.println("【Client】健康检查失败: " + e.getMessage());
            throw new RuntimeException("健康检查失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 配置服务端点地址
     * 
     * 【教学说明】
     * BindingProvider 接口允许我们在运行时配置 SOAP 调用的各种参数：
     * - ENDPOINT_ADDRESS_PROPERTY: 服务端地址
     * - USERNAME_PROPERTY / PASSWORD_PROPERTY: 认证信息
     * - 等等...
     * 
     * 这在以下场景很有用：
     * - 需要连接到不同环境（开发/测试/生产）
     * - WSDL 中的地址与实际地址不同
     * - 需要动态切换服务端
     * 
     * @param port 服务端口
     */
    private void configureEndpoint(UserService port) {
        BindingProvider bindingProvider = (BindingProvider) port;
        bindingProvider.getRequestContext().put(
            BindingProvider.ENDPOINT_ADDRESS_PROPERTY, 
            serverUrl
        );
        System.out.println("【Client】配置服务端地址: " + serverUrl);
    }
}
