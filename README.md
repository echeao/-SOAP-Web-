# SOAP Web Service 学习实践项目

这是一个用于学习 Java SOAP (JAX-WS) 通信的完整示例项目。
包含纯 Java 实现的 **服务端 (soap-server)** 和基于 Spring Boot 的 **客户端 (soap-client)**。

## 一、项目结构

```text
soap-wsdl-demo/
├── soap-server/          # [纯 Java] SOAP 服务端
│   ├── src/main/java/com/example/soap/server/
│   │   ├── model/        # 请求/响应 POJO
│   │   ├── service/      # 接口与实现
│   │   └── ServerApp.java # 发布服务的启动类
│   └── pom.xml
│
└── soap-client/          # [Spring Boot] SOAP 客户端 & Web UI
    ├── src/main/java/com/example/soap/client/
    │   ├── config/       # 配置类 (WSDL加载)
    │   ├── controller/   # REST 接口 (供前端调用)
    │   ├── generated/    # JAX-WS 自动生成的代码
    │   └── service/      # 封装 SOAP 调用逻辑
    ├── src/main/resources/
    │   ├── user.wsdl     # 本地缓存的 WSDL
    │   ├── user.xsd      # 本地缓存的 XSD
    │   └── static/       # Web 前端页面
    └── pom.xml
```

## 二、如何运行

### 1. 启动服务端 (Server)
服务端监听端口: `8089`

```bash
cd soap-server
mvn clean compile exec:java -Dexec.mainClass="com.example.soap.server.ServerApp"
```

看到 `Publishing Service at: http://localhost:8089/ws/user` 即启动成功。

### 2. 启动客户端 (Client)
客户端监听端口: `8080`

```bash
cd soap-client
mvn clean spring-boot:run
```

### 3. 测试
打开浏览器访问: [http://localhost:8080](http://localhost:8080)
输入姓名和年龄，点击发送，即可看到服务端返回的 SOAP 响应。

---

## 三、教学说明 (核心知识点)

### 1. WSDL 是什么？
WSDL (Web Services Description Language) 是 SOAP 服务的 **"说明书"**。
在本项目中，WSDL 文件 (`user.wsdl`) 定义了：
- **Types**: 数据长什么样？(UserRequest, UserResponse)
- **Message**: 消息包含哪些部分？
- **PortType**: 有哪些方法可以调用？(getUserInfo)
- **Binding**: 使用什么协议传输？(HTTP + SOAP XML)
- **Service**: 服务地址在哪里？(http://localhost:8089/ws/user)

客户端正是通过解析这个 "说明书"，自动生成了 Java 代码 (`generated` 包下的类)。

### 2. 一次 SOAP 请求/响应的完整流程
当你点击网页上的 "Send" 按钮时：
1. **浏览器 (JS)** 发送 JSON 数据 (`{name:"Tom", age:20}`) 到 Spring Boot 后端 (`/api/check-user`)。
2. **Spring Boot (Client)** 接收请求，调用 `SoapClientService`。
3. **JAX-WS 代理** 将 Java 对象 (`UserRequest`) 转换 (Marshal) 为 **SOAP XML**：
   ```xml
   <S:Envelope xmlns:S="...">
     <S:Body>
       <ns2:getUserInfo xmlns:ns2="...">
         <userRequest>
           <name>Tom</name>
           <age>20</age>
         </userRequest>
       </ns2:getUserInfo>
     </S:Body>
   </S:Envelope>
   ```
4. **HTTP 传输** 将 XML 发送到服务端 `http://localhost:8089/ws/user`。
5. **服务端 (Server)** 接收 XML，解析 (Unmarshal) 回 Java 对象，执行 `UserServiceImpl` 逻辑。
6. **服务端** 返回结果对象，再次转换为 XML 响应。
7. **客户端** 收到 XML，转回 Java 对象，最终返回 JSON 给浏览器。

### 3. Server 与 Client 的职责边界
- **Server**: 定义服务契约 (Interface + POJO)，实现业务逻辑，发布 Endpoint。它**不关心**谁来调用它。
- **Client**: 依赖 WSDL (契约)，生成代理代码，发起调用。它**不关心**服务端的具体实现细节 (如是 Java 写的还是 .NET 写的)。

### 4. XML 与 Java 对象如何映射 (JAXB)
JAXB (Java Architecture for XML Binding) 是背后的功臣。
- `@XmlRootElement`: 告诉 JAXB 这个类对应 XML 的根节点。
- `@XmlElement`: 映射类的字段到 XML 的标签。
- `@XmlAccessorType`: 定义是按字段还是按 Getter/Setter 方法映射。

例如 `UserRequest` 类中的 `name` 字段，通过注解自动变成了 XML 中的 `<name>Tom</name>`。

### 5. 通俗解释 WSDL 关键术语
- **Namespace (命名空间)**: 就像 Java 的 `package`。为了防止不同服务都有叫 `User` 的对象而冲突。本项目中是 `http://service.server.soap.example.com/`。
- **Element (元素)**: XML 的标签，比如 `<name>` 或 `<age>`。
- **ComplexType (复杂类型)**: 也就是 "对象" 或 "结构体"。比如 `UserRequest` 是一个 ComplexType，因为它里面包含了 `name` 和 `age` 两个子元素。

### 6. 新手常见错误
1. **Namespace 不一致**: 服务端改了包名或 namespace，但客户端还在用旧的 WSDL 生成代码，会导致 `UnmarshalException` (无法解析 XML)。
2. **WSDL 地址无法访问**: 生成代码时通过 `mvn generate-sources` 需要访问 WSDL。如果服务端没开，或者防火墙挡住了，构建会失败。(本项目通过将 WSDL 保存到本地 `src/main/resources` 解决了这个问题)。
3. **JAXB 依赖缺失**: 在 Java 9+ (特别是 Java 11/17/21) 中，JDK 移除了 JAXB，必须在 `pom.xml` 中手动引入 `jaxb-api` 和 implementation，否则会报 `ClassNotFoundException`。
4. **端口冲突**: 确保 8080 和 8089 端口没被占用。
5. **参数为 null**: SOAP 对 `required` 校验比较严格，如果 XML 缺了必填字段，可能会报错。

---
**Happy Coding!**
