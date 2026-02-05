package com.example.soapserver.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 用户响应模型 - SOAP 响应数据封装
 * 
 * 【教学说明】
 * 这个类展示了如何使用 JAXB 注解将 Java 对象映射为 XML。
 * 
 * JAXB（Java Architecture for XML Binding）注解说明：
 * 
 * - @XmlRootElement: 指定 XML 根元素名称
 * 生成的 XML: <userResponse>...</userResponse>
 * 
 * - @XmlAccessorType: 指定 JAXB 如何访问类成员
 * FIELD: 直接访问字段（推荐）
 * PROPERTY: 通过 getter/setter 方法访问
 * 
 * - @XmlType: 定义 XML 类型的属性顺序
 * propOrder: 指定元素在 XML 中的出现顺序
 * 
 * - @XmlElement: 指定字段对应的 XML 元素名称
 * name: 元素名称（默认使用字段名）
 * required: 是否必填
 * 
 * 【生成的 XML 示例】
 * <userResponse>
 * <status>SUCCESS</status>
 * <code>200</code>
 * <message>操作成功</message>
 * <data>
 * <userId>1001</userId>
 * ...
 * </data>
 * </userResponse>
 * 
 * @author 教学示例
 * @since 1.0.0
 */
@XmlRootElement(name = "userResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "status", "code", "message", "data" })
public class UserResponse {

    /**
     * 响应状态
     * 可能的值：SUCCESS, ERROR, WARNING
     */
    @XmlElement(name = "status", required = true)
    private String status;

    /**
     * 响应代码
     * 类似 HTTP 状态码：200=成功, 400=客户端错误, 500=服务端错误
     */
    @XmlElement(name = "code", required = true)
    private int code;

    /**
     * 响应消息
     * 人类可读的描述信息
     */
    @XmlElement(name = "message", required = true)
    private String message;

    /**
     * 响应数据
     * 包含具体的用户信息，可能为 null（如错误响应时）
     */
    @XmlElement(name = "data", required = false)
    private UserData data;

    // ==================== 构造方法 ====================

    /**
     * 默认构造方法
     * JAXB 需要无参构造方法来创建对象
     */
    public UserResponse() {
    }

    /**
     * 全参构造方法
     */
    public UserResponse(String status, int code, String message, UserData data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ==================== Getter 和 Setter ====================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    // ==================== toString ====================

    @Override
    public String toString() {
        return "UserResponse{" +
                "status='" + status + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
