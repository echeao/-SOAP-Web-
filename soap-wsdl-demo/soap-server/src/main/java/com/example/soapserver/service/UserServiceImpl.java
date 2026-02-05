package com.example.soapserver.service;

import com.example.soapserver.model.UserData;
import com.example.soapserver.model.UserResponse;

import javax.jws.WebService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用户服务实现类 - SOAP Web Service 实现
 * 
 * 【教学说明】
 * 这是 UserService 接口的具体实现。
 * 
 * 关键注解说明：
 * - @WebService 的 endpointInterface 属性指向接口类
 * - serviceName 定义 WSDL 中的服务名称
 * - portName 定义 WSDL 中的端口名称
 * 
 * 【业务逻辑】
 * 在实际项目中，这里会调用数据库或其他服务。
 * 本示例使用模拟数据，专注于展示 SOAP 通信机制。
 * 
 * @author 教学示例
 * @since 1.0.0
 */
@WebService(endpointInterface = "com.example.soapserver.service.UserService", // 指向接口
        serviceName = "UserServiceImplService", // WSDL 中的 service 名称
        portName = "UserServiceImplPort", // WSDL 中的 port 名称
        targetNamespace = "http://example.com/soap/user" // 保持与接口一致的命名空间
)
public class UserServiceImpl implements UserService {

    /**
     * 用户 ID 生成器
     * 使用 AtomicLong 保证线程安全的自增
     */
    private final AtomicLong userIdGenerator = new AtomicLong(1000);

    /**
     * 日期时间格式化器
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * 获取用户信息
     * 
     * 【处理流程】
     * 1. 验证输入参数
     * 2. 生成用户 ID
     * 3. 构造问候语
     * 4. 封装响应对象
     * 
     * @param name 用户姓名
     * @param age  用户年龄
     * @return 用户响应对象
     */
    @Override
    public UserResponse getUserInfo(String name, int age) {
        System.out.println("【Server】收到 getUserInfo 请求: name=" + name + ", age=" + age);

        // 参数验证
        if (name == null || name.trim().isEmpty()) {
            return createErrorResponse("参数错误", "姓名不能为空");
        }
        if (age < 0 || age > 150) {
            return createErrorResponse("参数错误", "年龄必须在 0-150 之间");
        }

        // 生成用户数据
        UserData userData = new UserData();
        userData.setUserId(userIdGenerator.incrementAndGet());
        userData.setName(name);
        userData.setAge(age);
        userData.setGreeting(String.format("你好，%s！你今年%d岁。", name, age));
        userData.setTimestamp(LocalDateTime.now().format(FORMATTER));

        // 构造成功响应
        UserResponse response = new UserResponse();
        response.setStatus("SUCCESS");
        response.setCode(200);
        response.setMessage("操作成功");
        response.setData(userData);

        System.out.println("【Server】响应: userId=" + userData.getUserId());
        return response;
    }

    /**
     * 创建用户
     * 
     * 【教学说明】
     * 演示另一个 SOAP 操作。
     * 实际项目中会将用户数据保存到数据库。
     * 
     * @param name 用户姓名
     * @param age  用户年龄
     * @return 创建结果响应
     */
    @Override
    public UserResponse createUser(String name, int age) {
        System.out.println("【Server】收到 createUser 请求: name=" + name + ", age=" + age);

        // 参数验证
        if (name == null || name.trim().isEmpty()) {
            return createErrorResponse("参数错误", "姓名不能为空");
        }

        // 模拟创建用户
        UserData userData = new UserData();
        userData.setUserId(userIdGenerator.incrementAndGet());
        userData.setName(name);
        userData.setAge(age);
        userData.setGreeting("欢迎新用户 " + name + "！");
        userData.setTimestamp(LocalDateTime.now().format(FORMATTER));

        // 构造成功响应
        UserResponse response = new UserResponse();
        response.setStatus("SUCCESS");
        response.setCode(201); // HTTP 201 Created
        response.setMessage("用户创建成功");
        response.setData(userData);

        System.out.println("【Server】用户创建成功: userId=" + userData.getUserId());
        return response;
    }

    /**
     * 健康检查
     * 
     * 【教学说明】
     * 一个无参数的简单操作，用于检查服务是否正常运行。
     * 在生产环境中，可以检查数据库连接、外部服务等。
     * 
     * @return 服务状态响应
     */
    @Override
    public UserResponse healthCheck() {
        System.out.println("【Server】收到 healthCheck 请求");

        UserResponse response = new UserResponse();
        response.setStatus("SUCCESS");
        response.setCode(200);
        response.setMessage("服务运行正常");

        // 返回服务信息
        UserData serviceInfo = new UserData();
        serviceInfo.setUserId(0L);
        serviceInfo.setName("UserService");
        serviceInfo.setAge(0);
        serviceInfo.setGreeting("SOAP Server is running!");
        serviceInfo.setTimestamp(LocalDateTime.now().format(FORMATTER));
        response.setData(serviceInfo);

        return response;
    }

    /**
     * 创建错误响应
     * 
     * 【教学说明】
     * 封装错误响应的通用方法。
     * 在 SOAP 中，除了使用 SOAPFault，也可以返回自定义的错误响应。
     * 
     * @param status  错误状态
     * @param message 错误消息
     * @return 错误响应对象
     */
    private UserResponse createErrorResponse(String status, String message) {
        UserResponse response = new UserResponse();
        response.setStatus(status);
        response.setCode(400); // HTTP 400 Bad Request
        response.setMessage(message);
        response.setData(null);
        return response;
    }
}
