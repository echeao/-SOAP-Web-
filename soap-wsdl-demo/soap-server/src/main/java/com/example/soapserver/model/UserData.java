package com.example.soapserver.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 用户数据模型 - 嵌套在 UserResponse 中
 * 
 * 【教学说明】
 * 这是一个嵌套的数据模型，作为 UserResponse 的 data 字段。
 * 
 * 在 SOAP/XML 中，复杂类型（complexType）可以包含其他复杂类型，
 * 形成嵌套结构。JAXB 会自动处理这种嵌套关系。
 * 
 * 注意：嵌套类型不需要 @XmlRootElement，
 * 因为它不会作为 XML 文档的根元素出现。
 * 
 * 【生成的 XML 示例】（作为 UserResponse 的一部分）
 * <data>
 * <userId>1001</userId>
 * <name>张三</name>
 * <age>25</age>
 * <greeting>你好，张三！你今年25岁。</greeting>
 * <timestamp>2026-02-05T20:15:00</timestamp>
 * </data>
 * 
 * @author 教学示例
 * @since 1.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserData", propOrder = {
        "userId",
        "name",
        "age",
        "greeting",
        "timestamp"
})
public class UserData {

    /**
     * 用户ID
     * 系统自动生成的唯一标识
     */
    @XmlElement(name = "userId", required = true)
    private Long userId;

    /**
     * 用户姓名
     */
    @XmlElement(name = "name", required = true)
    private String name;

    /**
     * 用户年龄
     */
    @XmlElement(name = "age", required = true)
    private int age;

    /**
     * 问候语
     * 由服务端根据用户信息生成
     */
    @XmlElement(name = "greeting")
    private String greeting;

    /**
     * 时间戳
     * 记录响应生成的时间
     */
    @XmlElement(name = "timestamp")
    private String timestamp;

    // ==================== 构造方法 ====================

    /**
     * 默认构造方法
     * JAXB 需要无参构造方法
     */
    public UserData() {
    }

    /**
     * 全参构造方法
     */
    public UserData(Long userId, String name, int age, String greeting, String timestamp) {
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.greeting = greeting;
        this.timestamp = timestamp;
    }

    // ==================== Getter 和 Setter ====================

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // ==================== toString ====================

    @Override
    public String toString() {
        return "UserData{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", greeting='" + greeting + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
