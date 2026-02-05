package com.example.soapserver.service;

import com.example.soapserver.model.UserResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * 用户服务接口 - SOAP Web Service 定义
 * 
 * 【教学说明】
 * 这是 SOAP Web Service 的核心接口定义。
 * 通过 JAX-WS 注解，这个普通的 Java 接口会被暴露为 SOAP 服务。
 * 
 * 关键注解说明：
 * - @WebService: 将接口标记为 Web Service
 * - @SOAPBinding: 指定 SOAP 绑定风格（Document/Literal 是推荐的现代风格）
 * - @WebMethod: 将方法暴露为 SOAP 操作
 * - @WebParam: 定义方法参数在 SOAP 消息中的名称
 * - @WebResult: 定义返回值在 SOAP 消息中的名称
 * 
 * 【WSDL 生成】
 * JAX-WS 运行时会根据这些注解自动生成 WSDL 文档。
 * WSDL 描述了服务的操作、参数和地址，是客户端调用的依据。
 * 
 * @author 教学示例
 * @since 1.0.0
 */
@WebService(name = "UserService", // WSDL 中的 portType 名称
        targetNamespace = "http://example.com/soap/user" // XML 命名空间，用于唯一标识服务
)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, // 文档风格（推荐）
        use = SOAPBinding.Use.LITERAL, // 字面量编码（推荐）
        parameterStyle = SOAPBinding.ParameterStyle.WRAPPED // 参数包装方式
)
public interface UserService {

    /**
     * 获取用户信息
     * 
     * 【业务说明】
     * 根据用户名和年龄查询用户信息。
     * 这是主要的业务方法，返回包含问候语的用户响应。
     * 
     * 【SOAP 映射】
     * 请求消息：
     * <getUserInfo xmlns="http://example.com/soap/user">
     * <name>张三</name>
     * <age>25</age>
     * </getUserInfo>
     * 
     * 响应消息：
     * <getUserInfoResponse xmlns="http://example.com/soap/user">
     * <result>
     * <status>SUCCESS</status>
     * ...
     * </result>
     * </getUserInfoResponse>
     * 
     * @param name 用户姓名（必填）
     * @param age  用户年龄（必填，0-150）
     * @return 用户响应对象，包含状态、消息和用户数据
     */
    @WebMethod(operationName = "getUserInfo")
    @WebResult(name = "result")
    UserResponse getUserInfo(
            @WebParam(name = "name") String name,
            @WebParam(name = "age") int age);

    /**
     * 创建用户
     * 
     * 【业务说明】
     * 创建一个新用户。在本教学示例中，只返回模拟结果。
     * 
     * @param name 用户姓名
     * @param age  用户年龄
     * @return 创建结果响应
     */
    @WebMethod(operationName = "createUser")
    @WebResult(name = "result")
    UserResponse createUser(
            @WebParam(name = "name") String name,
            @WebParam(name = "age") int age);

    /**
     * 健康检查
     * 
     * 【业务说明】
     * 检查服务是否正常运行。
     * 用于监控和运维，不需要任何参数。
     * 
     * @return 服务状态响应
     */
    @WebMethod(operationName = "healthCheck")
    @WebResult(name = "result")
    UserResponse healthCheck();
}
