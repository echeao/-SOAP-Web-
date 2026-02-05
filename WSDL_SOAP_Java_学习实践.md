# WSDL（SOAP）通信学习与实践（Java）

> **目标**：生成一个**完整、可运行、适合新手学习的 WSDL / SOAP Java 双端示例项目**。

---

## 目录

1. [项目总体要求](#一项目总体要求)
2. [系统架构](#二系统架构)
3. [接收端（Server）要求](#三接收端server要求)
4. [发送端（Client）要求](#四发送端client要求)
5. [项目结构](#五项目结构必须)
6. [教学说明](#六教学说明必须输出)
7. [约束与风格](#七约束与风格)
8. [最终目标](#八最终目标)
9. [技术背景知识](#九技术背景知识)
10. [开发环境准备](#十开发环境准备)
11. [实现步骤指南](#十一实现步骤指南)
12. [测试验证方案](#十二测试验证方案)
13. [扩展任务（可选）](#十三扩展任务可选)

---

## 一、项目总体要求

你是一名**资深 Java 架构师与教学型工程师**。  
请为我生成一个**完整、可运行、适合初学者学习的 WSDL（SOAP）通信示例项目**，用于理解 **Java 中 SOAP / WSDL 的发送与接收机制**。

### 项目核心价值

- **学习目标明确**：通过实践理解 WSDL 描述语言和 SOAP 协议
- **代码完整可运行**：不省略任何关键步骤，开箱即用
- **注释丰富**：每个关键点都有详细的中文注释
- **循序渐进**：从简单到复杂，逐步深入

---

## 二、系统架构

本项目由两个独立模块组成：

| 模块 | 角色 | 端口 | 技术栈 |
|------|------|------|--------|
| **soap-server** | 接收端（SOAP Server） | 8089 | 纯 Java + JAX-WS |
| **soap-client** | 发送端（SOAP Client） | 8080 | Spring Boot + Maven |

### 系统交互流程图

```
┌─────────────────┐                              ┌─────────────────┐
│   SOAP Client   │                              │   SOAP Server   │
│   (发送端)       │                              │   (接收端)       │
│   Port: 8080    │                              │   Port: 8089    │
└────────┬────────┘                              └────────┬────────┘
         │                                                │
         │  1. 获取 WSDL 文件                              │
         │ ─────────────────────────────────────────────> │
         │    GET http://localhost:8089/ws/user?wsdl      │
         │                                                │
         │  2. 返回 WSDL 描述文档                          │
         │ <───────────────────────────────────────────── │
         │                                                │
         │  3. 根据 WSDL 构造 SOAP 请求                    │
         │    (包含 name 和 age 参数)                      │
         │                                                │
         │  4. 发送 SOAP 请求                              │
         │ ─────────────────────────────────────────────> │
         │    POST http://localhost:8089/ws/user          │
         │    Content-Type: text/xml; charset=utf-8       │
         │                                                │
         │  5. Server 处理请求                             │
         │    - 解析 SOAP Envelope                         │
         │    - 提取参数 (name, age)                       │
         │    - 执行业务逻辑                               │
         │    - 构造响应对象                               │
         │                                                │
         │  6. 返回 SOAP 响应                              │
         │ <───────────────────────────────────────────── │
         │    (XML 格式的用户信息响应)                     │
         │                                                │
         │  7. Client 解析响应                             │
         │    - 将 XML 转为 Java 对象                      │
         │    - 保存到本地 IFXML 目录                      │
         │                                                │
         ▼                                                ▼
```

### 目标流程

> 发送端向接收端发送 **姓名（name）与年龄（age）**  
> 接收端处理后返回 **XML 格式响应**

---

## 三、接收端（Server）要求

### 1. 技术栈

| 技术 | 版本要求 | 用途 |
|------|----------|------|
| Java | 8+ (推荐 Java 11) | 核心运行环境 |
| JAX-WS | 2.3.x | SOAP Web Service 实现 |
| JAXB | 2.3.x | XML 与 Java 对象映射 |
| Maven | 3.6+ | 项目构建与依赖管理 |

### 2. 运行要求

- **服务端口**：`8089`
- **WSDL 发布地址**：`http://localhost:8089/ws/user?wsdl`
- **服务端点地址**：`http://localhost:8089/ws/user`

### 3. 功能要求

#### 3.1 提供 SOAP Web Service

服务需要暴露以下操作：

| 操作名 | 功能描述 | 输入参数 | 返回值 |
|--------|----------|----------|--------|
| `getUserInfo` | 获取用户信息 | name: String, age: int | UserResponse 对象 |
| `createUser` | 创建用户 | name: String, age: int | 创建结果 |
| `healthCheck` | 健康检查 | 无 | 服务状态信息 |

#### 3.2 接收参数定义

```java
/**
 * 请求参数
 */
public class UserRequest {
    private String name;    // 用户姓名，必填，最大长度 50
    private int age;        // 用户年龄，必填，范围 0-150
    private String email;   // 用户邮箱，选填
}
```

#### 3.3 返回 XML 响应格式

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <getUserInfoResponse xmlns="http://example.com/soap/user">
      <result>
        <status>SUCCESS</status>
        <code>200</code>
        <message>操作成功</message>
        <data>
          <userId>1001</userId>
          <name>张三</name>
          <age>25</age>
          <greeting>你好，张三！你今年25岁。</greeting>
          <timestamp>2026-02-05T20:10:00</timestamp>
        </data>
      </result>
    </getUserInfoResponse>
  </soap:Body>
</soap:Envelope>
```

### 4. 代码要求

#### 4.1 必须提供的类

| 类名 | 类型 | 职责 |
|------|------|------|
| `UserService` | 接口 | 定义 Web Service 契约（使用 @WebService 注解） |
| `UserServiceImpl` | 实现类 | 实现业务逻辑 |
| `UserRequest` | DTO | 请求数据封装 |
| `UserResponse` | DTO | 响应数据封装 |
| `SoapServerApplication` | 启动类 | main 方法，发布 Web Service |

#### 4.2 注解使用说明

```java
// Service 接口必须使用的注解
@WebService(
    name = "UserService",                           // 服务名称
    targetNamespace = "http://example.com/soap/user" // 命名空间
)
public interface UserService {
    
    @WebMethod(operationName = "getUserInfo")       // 操作名
    @WebResult(name = "result")                      // 返回值名称
    UserResponse getUserInfo(
        @WebParam(name = "name") String name,        // 参数名
        @WebParam(name = "age") int age
    );
}
```

#### 4.3 关键概念说明

| 概念 | 说明 | 示例 |
|------|------|------|
| **WSDL 地址** | 服务描述文档的访问地址 | `http://localhost:8089/ws/user?wsdl` |
| **targetNamespace** | XML 命名空间，用于唯一标识服务 | `http://example.com/soap/user` |
| **serviceName** | WSDL 中的服务名称 | `UserServiceImplService` |
| **portName** | 服务端口名称 | `UserServiceImplPort` |

---

## 四、发送端（Client）要求

### 1. 技术栈

| 技术 | 版本要求 | 用途 |
|------|----------|------|
| Spring Boot | 2.7.x 或 3.x | 应用框架 |
| Maven | 3.6+ | 项目构建 |
| Java | 8+ (推荐 Java 11) | 运行环境 |
| jaxws-maven-plugin | 2.6 | 根据 WSDL 生成客户端代码 |

### 2. 运行要求

- **应用端口**：`8080`
- **角色**：SOAP 客户端
- **启动前提**：Server 端必须先启动

### 3. 功能要求

#### 3.1 根据 WSDL 生成客户端代码

使用 Maven 插件自动生成：

```xml
<plugin>
    <groupId>com.sun.xml.ws</groupId>
    <artifactId>jaxws-maven-plugin</artifactId>
    <version>2.3.5</version>
    <executions>
        <execution>
            <goals>
                <goal>wsimport</goal>
            </goals>
            <configuration>
                <wsdlUrls>
                    <wsdlUrl>http://localhost:8089/ws/user?wsdl</wsdlUrl>
                </wsdlUrls>
                <packageName>com.example.soapclient.generated</packageName>
                <sourceDestDir>${project.build.directory}/generated-sources/wsimport</sourceDestDir>
            </configuration>
        </execution>
    </executions>
</plugin>
```

#### 3.2 构造并发送请求

```java
// 示例：发送 SOAP 请求
public UserResponse callUserService(String name, int age) {
    // 1. 创建 Service 实例
    UserServiceImplService service = new UserServiceImplService();
    
    // 2. 获取 Port（服务端口）
    UserService port = service.getUserServiceImplPort();
    
    // 3. 调用远程方法
    UserResponse response = port.getUserInfo(name, age);
    
    return response;
}
```

#### 3.3 响应处理与文件保存

接收 SOAP 响应并输出到本地 XML 文件：

- **文件保存目录**：`IFXML/`（项目根目录下）
- **文件命名规则**：`response_{timestamp}.xml`
- **若目录不存在**：自动创建

```java
// 保存响应到文件
public void saveResponseToFile(UserResponse response) {
    Path ifxmlDir = Paths.get("IFXML");
    if (!Files.exists(ifxmlDir)) {
        Files.createDirectories(ifxmlDir);
    }
    
    String filename = String.format("response_%s.xml", 
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));
    
    // 使用 JAXB 序列化为 XML
    JAXBContext context = JAXBContext.newInstance(UserResponse.class);
    Marshaller marshaller = context.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.marshal(response, ifxmlDir.resolve(filename).toFile());
}
```

### 4. 实现方式要求

#### 4.1 必须生成的类

| 类名 | 来源 | 用途 |
|------|------|------|
| `UserService` | wsimport 生成 | Service 接口 |
| `UserServiceImplService` | wsimport 生成 | Service 工厂类 |
| `UserRequest` | wsimport 生成 | 请求对象 |
| `UserResponse` | wsimport 生成 | 响应对象 |
| `ObjectFactory` | wsimport 生成 | JAXB 对象工厂 |

#### 4.2 代码原则

- **不手写 XML**：使用 JAXB 自动序列化/反序列化
- **类型安全**：使用生成的 Java 类，而非字符串拼接
- **异常处理**：正确处理 SOAP Fault

---

## 五、项目结构（必须）

```text
soap-wsdl-demo/
├── pom.xml                          # 父 POM（聚合多模块）
├── README.md                        # 项目说明文档
│
├── soap-server/                     # 接收端模块
│   ├── pom.xml                      # Server 模块 POM
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/example/soapserver/
│           │       ├── SoapServerApplication.java    # 启动类
│           │       ├── service/
│           │       │   ├── UserService.java          # 服务接口
│           │       │   └── UserServiceImpl.java      # 服务实现
│           │       ├── model/
│           │       │   ├── UserRequest.java          # 请求模型
│           │       │   └── UserResponse.java         # 响应模型
│           │       └── config/
│           │           └── WebServiceConfig.java     # 配置类
│           └── resources/
│               └── application.properties            # 配置文件
│
├── soap-client/                     # 发送端模块
│   ├── pom.xml                      # Client 模块 POM
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/example/soapclient/
│           │       ├── SoapClientApplication.java    # Spring Boot 启动类
│           │       ├── service/
│           │       │   └── SoapClientService.java    # 客户端服务
│           │       ├── controller/
│           │       │   └── TestController.java       # 测试接口
│           │       ├── util/
│           │       │   └── XmlFileUtil.java          # XML 文件工具
│           │       └── generated/                    # wsimport 生成的代码
│           │           └── ...
│           └── resources/
│               └── application.properties            # 配置文件
│
├── IFXML/                           # XML 响应文件保存目录
│   └── .gitkeep
│
└── docs/                            # 文档目录
    ├── WSDL_Guide.md               # WSDL 学习指南
    ├── SOAP_Protocol.md            # SOAP 协议说明
    └── Troubleshooting.md          # 常见问题解答
```

### 模块要求

每个模块需要：

- ✅ 可直接运行（`mvn spring-boot:run` 或 `java -jar`）
- ✅ 包结构清晰、偏教学
- ✅ 含完整的 `pom.xml` 依赖配置
- ✅ 提供启动脚本（可选）

---

## 六、教学说明（必须输出）

生成代码的同时，请输出文字说明，包括：

### 1. WSDL 是什么，在本项目中的作用

```markdown
WSDL（Web Services Description Language）是一种基于 XML 的语言，用于描述 Web 服务。

🔹 作用：
- 定义服务提供哪些操作（方法）
- 描述每个操作的输入/输出参数格式
- 指定服务的访问地址（endpoint）
- 作为客户端与服务端之间的"契约"

🔹 在本项目中：
- Server 启动后自动生成 WSDL
- Client 根据 WSDL 自动生成调用代码
- 双方通过 WSDL 保证通信格式一致
```

### 2. 一次 SOAP 请求/响应的完整流程

```markdown
┌──────────────────────────────────────────────────────────────────┐
│                        SOAP 通信流程                              │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│  步骤 1：客户端获取 WSDL                                          │
│  ───────────────────────                                         │
│  GET http://localhost:8089/ws/user?wsdl                          │
│  → 服务端返回 XML 格式的服务描述文档                               │
│                                                                   │
│  步骤 2：客户端构造 SOAP 请求                                     │
│  ──────────────────────────                                      │
│  - 创建 SOAP Envelope                                            │
│  - 在 Body 中放入操作名和参数                                     │
│  - 设置 Content-Type: text/xml                                   │
│                                                                   │
│  步骤 3：发送 HTTP POST 请求                                      │
│  ────────────────────────                                        │
│  POST http://localhost:8089/ws/user                              │
│  Body: SOAP XML 消息                                             │
│                                                                   │
│  步骤 4：服务端处理请求                                           │
│  ────────────────────                                            │
│  - 解析 SOAP 消息                                                │
│  - 调用对应的 Java 方法                                          │
│  - 将结果封装为 SOAP 响应                                        │
│                                                                   │
│  步骤 5：客户端接收响应                                           │
│  ────────────────────                                            │
│  - 解析 SOAP 响应 XML                                            │
│  - 转换为 Java 对象                                              │
│  - 进行后续处理（保存文件等）                                     │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘
```

### 3. Server 与 Client 的职责边界

| 职责 | Server（接收端） | Client（发送端） |
|------|------------------|------------------|
| WSDL | 生成并发布 | 获取并解析 |
| 请求 | 接收并解析 | 构造并发送 |
| 业务逻辑 | 执行 | 调用 |
| 响应 | 生成并返回 | 接收并处理 |
| 数据验证 | 服务端验证 | 客户端预验证 |

### 4. XML 与 Java 对象如何映射（JAXB）

```java
// Java 类
@XmlRootElement(name = "userResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserResponse {
    @XmlElement(name = "userId")
    private Long userId;
    
    @XmlElement(name = "name")
    private String name;
    
    @XmlElement(name = "age")
    private Integer age;
}

// 对应的 XML
<userResponse>
    <userId>1001</userId>
    <name>张三</name>
    <age>25</age>
</userResponse>
```

### 5. namespace、element、complexType 的通俗解释

| 概念 | 通俗解释 | 类比 |
|------|----------|------|
| **namespace** | 命名空间，避免名称冲突 | 类似 Java 的 package |
| **element** | XML 中的标签，包含数据 | 类似 Java 的属性 |
| **complexType** | 复杂类型，包含多个 element | 类似 Java 的 Class |
| **simpleType** | 简单类型，如 string、int | 类似 Java 的基本类型 |

### 6. 新手常见错误 Top 5

| # | 错误 | 原因 | 解决方案 |
|---|------|------|----------|
| 1 | **Namespace 不一致** | 客户端与服务端的 namespace 不匹配 | 确保双方使用相同的 targetNamespace |
| 2 | **WSDL 地址错误** | 服务未启动或地址写错 | 先启动 Server，检查 URL 拼写 |
| 3 | **端口被占用** | 8089 或 8080 端口已被其他程序使用 | 使用 `netstat` 检查，关闭占用程序 |
| 4 | **生成代码过期** | WSDL 更新后未重新生成客户端代码 | 执行 `mvn clean generate-sources` |
| 5 | **SOAPAction 头错误** | HTTP 请求缺少或错误的 SOAPAction | 检查请求头设置 |

---

## 七、约束与风格

### 代码规范

- ✅ 所有代码必须 **可编译、可运行**
- ✅ 不省略关键步骤
- ✅ 代码风格偏教学，**注释清晰**
- ✅ 不引入无关框架
- ✅ 使用 **document / literal** 风格（而非 RPC/encoded）

### 注释要求

```java
/**
 * 用户服务接口
 * 
 * 【教学说明】
 * @WebService 注解将此接口标记为 SOAP Web Service
 * targetNamespace 定义服务的 XML 命名空间，用于生成 WSDL
 * 
 * @author 教学示例
 * @since 1.0.0
 */
@WebService(targetNamespace = "http://example.com/soap/user")
public interface UserService {
    
    /**
     * 获取用户信息
     * 
     * 【业务说明】
     * 根据用户名和年龄查询用户信息，并返回问候语
     * 
     * 【SOAP 映射】
     * - 操作名：getUserInfo
     * - 输入：name (string), age (int)
     * - 输出：UserResponse (complexType)
     * 
     * @param name 用户姓名
     * @param age  用户年龄
     * @return 用户响应对象
     */
    @WebMethod(operationName = "getUserInfo")
    UserResponse getUserInfo(
        @WebParam(name = "name") String name, 
        @WebParam(name = "age") int age
    );
}
```

### SOAP 风格说明

| 风格 | 本项目使用 | 说明 |
|------|------------|------|
| **Document/Literal** | ✅ 是 | 推荐，消息体是完整的 XML 文档 |
| RPC/Encoded | ❌ 否 | 老旧风格，已不推荐 |

---

## 八、最终目标

完成后应能够：

### 验收清单

- [ ] **步骤 1**：启动接收端（8089）
  ```bash
  cd soap-server
  mvn clean package -DskipTests
  java -jar target/soap-server-1.0.0.jar
  ```

- [ ] **步骤 2**：访问并查看 WSDL
  ```
  浏览器打开：http://localhost:8089/ws/user?wsdl
  应看到 XML 格式的 WSDL 文档
  ```

- [ ] **步骤 3**：启动发送端（8080）
  ```bash
  cd soap-client
  mvn clean package -DskipTests
  mvn spring-boot:run
  ```

- [ ] **步骤 4**：成功完成一次 SOAP 调用
  ```bash
  curl http://localhost:8080/api/test/call?name=张三&age=25
  ```

- [ ] **步骤 5**：检查 IFXML 目录下生成的响应文件

- [ ] **步骤 6**：理解完整的 WSDL / SOAP 调用链路

---

## 九、技术背景知识

### 9.1 SOAP 协议简介

**SOAP**（Simple Object Access Protocol）是一种基于 XML 的消息传递协议，用于在网络上交换结构化信息。

#### SOAP 消息结构

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- SOAP 消息的根元素 -->
<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    
    <!-- 可选：消息头，包含元数据 -->
    <soap:Header>
        <!-- 认证信息、事务 ID 等 -->
    </soap:Header>
    
    <!-- 必须：消息体，包含实际数据 -->
    <soap:Body>
        <!-- 请求或响应的具体内容 -->
        <getUserInfo xmlns="http://example.com/soap/user">
            <name>张三</name>
            <age>25</age>
        </getUserInfo>
    </soap:Body>
    
</soap:Envelope>
```

### 9.2 WSDL 文档结构

```xml
<definitions>
    <!-- 类型定义 -->
    <types>
        <xsd:schema>
            <!-- 定义复杂类型 -->
        </xsd:schema>
    </types>
    
    <!-- 消息定义 -->
    <message name="getUserInfoRequest">
        <part name="parameters" element="tns:getUserInfo"/>
    </message>
    
    <!-- 端口类型（操作集合） -->
    <portType name="UserService">
        <operation name="getUserInfo">
            <input message="tns:getUserInfoRequest"/>
            <output message="tns:getUserInfoResponse"/>
        </operation>
    </portType>
    
    <!-- 绑定（协议与格式） -->
    <binding name="UserServiceBinding" type="tns:UserService">
        <soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http"/>
    </binding>
    
    <!-- 服务定义 -->
    <service name="UserService">
        <port name="UserServicePort" binding="tns:UserServiceBinding">
            <soap:address location="http://localhost:8089/ws/user"/>
        </port>
    </service>
</definitions>
```

### 9.3 JAX-WS 核心注解

| 注解 | 作用域 | 用途 |
|------|--------|------|
| `@WebService` | 类/接口 | 标记为 Web Service |
| `@WebMethod` | 方法 | 标记为可调用的操作 |
| `@WebParam` | 参数 | 定义参数名称和模式 |
| `@WebResult` | 方法 | 定义返回值名称 |
| `@SOAPBinding` | 类/接口 | 指定 SOAP 绑定风格 |

---

## 十、开发环境准备

### 10.1 必需软件

| 软件 | 版本 | 下载地址 |
|------|------|----------|
| JDK | 8+ (推荐 11 或 17) | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) |
| Maven | 3.6+ | [Apache Maven](https://maven.apache.org/download.cgi) |
| IDE | IntelliJ IDEA 或 Eclipse | - |

### 10.2 环境变量配置

```bash
# Windows (PowerShell)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-11"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

# 验证
java -version
mvn -version
```

### 10.3 IDE 插件推荐

- **IntelliJ IDEA**：内置 Web Service 支持
- **Eclipse**：安装 Eclipse Web Developer Tools
- **VS Code**：安装 Java Extension Pack

---

## 十一、实现步骤指南

### 阶段 1：创建 Server 模块（预计 30 分钟）

1. 创建 Maven 项目
2. 添加 JAX-WS 依赖
3. 定义服务接口 `UserService`
4. 实现服务类 `UserServiceImpl`
5. 创建启动类发布服务
6. 测试 WSDL 是否可访问

### 阶段 2：创建 Client 模块（预计 30 分钟）

1. 创建 Spring Boot 项目
2. 配置 jaxws-maven-plugin
3. 生成客户端代码
4. 创建调用服务的 Service 类
5. 创建测试 Controller
6. 测试 SOAP 调用

### 阶段 3：完善功能（预计 20 分钟）

1. 实现响应保存到 IFXML 目录
2. 添加异常处理
3. 编写集成测试
4. 完善文档

---

## 十二、测试验证方案

### 12.1 单元测试

```java
@Test
public void testGetUserInfo() {
    UserService service = new UserServiceImplService().getUserServiceImplPort();
    UserResponse response = service.getUserInfo("测试用户", 30);
    
    assertNotNull(response);
    assertEquals("SUCCESS", response.getStatus());
    assertEquals("测试用户", response.getName());
}
```

### 12.2 使用 SoapUI 测试

1. 下载并安装 [SoapUI](https://www.soapui.org/)
2. 创建新 SOAP 项目
3. 输入 WSDL 地址：`http://localhost:8089/ws/user?wsdl`
4. 自动生成测试请求
5. 修改参数并发送

### 12.3 使用 cURL 测试

```bash
curl -X POST http://localhost:8089/ws/user \
  -H "Content-Type: text/xml; charset=utf-8" \
  -H "SOAPAction: \"\"" \
  -d '<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <getUserInfo xmlns="http://example.com/soap/user">
      <name>张三</name>
      <age>25</age>
    </getUserInfo>
  </soap:Body>
</soap:Envelope>'
```

---

## 十三、扩展任务（可选）

完成基础功能后，可以尝试以下扩展：

### 初级扩展

- [ ] 添加更多操作方法（如 `deleteUser`、`updateUser`）
- [ ] 实现输入参数验证
- [ ] 添加日志记录

### 中级扩展

- [ ] 实现 SOAP Header 中的身份认证
- [ ] 添加 SOAP Fault 错误处理
- [ ] 使用数据库存储用户数据

### 高级扩展

- [ ] 实现异步 SOAP 调用
- [ ] 添加 WS-Security 安全机制
- [ ] 部署到云服务器

---

## 附录：参考资料

1. [Oracle JAX-WS 教程](https://docs.oracle.com/javaee/7/tutorial/jaxws.htm)
2. [Apache CXF 官方文档](https://cxf.apache.org/docs/)
3. [SOAP 1.2 规范](https://www.w3.org/TR/soap12/)
4. [WSDL 2.0 规范](https://www.w3.org/TR/wsdl20/)

---

> 📝 **文档版本**：v1.0.0  
> 📅 **最后更新**：2026-02-05  
> 👨‍💻 **适用于**：Java 初学者 / SOAP Web Service 入门学习
