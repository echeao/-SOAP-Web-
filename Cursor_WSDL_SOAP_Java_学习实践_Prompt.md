## WSDL（SOAP）通信学习与实践（Java）

> 目标：生成一个**完整、可运行、适合新手学习的 WSDL / SOAP Java 双端示例项目**。

---

## 一、项目总体要求

你是一名**资深 Java 架构师与教学型工程师**。  
请为我生成一个**完整、可运行、适合初学者学习的 WSDL（SOAP）通信示例项目**，用于理解 **Java 中 SOAP / WSDL 的发送与接收机制**。

---

## 二、系统架构

本项目由两个独立模块组成：

- **接收端（SOAP Server）**
- **发送端（SOAP Client）**

目标流程：

> 发送端向接收端发送 **姓名（name）与年龄（age）**等等内容，具有可扩展性  
> 接收端处理后返回 **XML 格式响应**

---

## 三、接收端（Server）要求

### 1. 技术栈

- 纯 Java 应用
- 使用 **JAX-WS（javax.jws / javax.xml.ws）**
- 不使用 Spring / Spring Boot
- Maven 构建
- Java 8+

### 2. 运行要求

- 服务端口：**8089**
- 使用 `Endpoint.publish` 启动
- 自动生成并暴露 WSDL

### 3. 功能要求

- 提供 SOAP Web Service
- 接收参数：
  - `name`（String）
  - `age`（int）
- 返回 XML 响应，例如：

```xml
<userResponse>
  <message>Tom，30岁，信息接收成功</message>
</userResponse>
```

### 4. 代码要求

- 使用 `@WebService`、`@WebMethod`
- 请求 / 响应使用 POJO（JAXB）
- 提供：
  - Service 接口
  - 实现类
  - 启动类（main 方法）
- 明确说明：
  - WSDL 地址（如 `http://localhost:8089/ws/user?wsdl`）
  - namespace 的定义与作用

---

## 四、发送端（Client）要求

### 1. 技术栈

- Spring Boot
- Maven
- Java 8+

### 2. 运行要求

- 应用端口：**8080**
- 作为 SOAP 客户端
- 不对外提供 SOAP 服务

### 3. 功能要求

- 根据接收端 WSDL 生成客户端代码
- 构造并发送：
  - name
  - age
- 接收 SOAP 响应并输出到控制台

### 4. 实现方式要求

- 使用 `wsimport`（或等效方式）生成：
  - Service
  - Port
  - Request / Response 类
- 在 Spring Boot 中：
  - 使用 `CommandLineRunner` 或简单 REST 接口触发调用
- 不手写 XML，使用 JAXB 自动序列化

---

## 五、项目结构（必须）

```text
soap-wsdl-demo/
├── soap-server/
│   ├── pom.xml
│   └── src/main/java/...
│
└── soap-client/
    ├── pom.xml
    └── src/main/java/...
```

每个模块需：

- 独立 Maven 构建
- 可直接运行
- 包结构清晰、偏教学

---

## 六、教学说明（必须输出）

生成代码的同时，请输出文字说明，包括：

1. WSDL 是什么，在本项目中的作用
2. 一次 SOAP 请求 / 响应的完整流程
3. Server 与 Client 的职责边界
4. XML 与 Java 对象如何映射（JAXB）
5. namespace、element、complexType 的通俗解释
6. 新手常见 3～5 个错误（如 namespace 不一致）

---

## 七、约束与风格

- 所有代码必须 **可编译、可运行**
- 不省略关键步骤
- 代码风格偏教学，注释清晰
- 不引入无关框架
- 使用 **document / literal** 风格

---

## 八、最终目标

完成后应能够：

1. 启动接收端（8089）
2. 访问并查看 WSDL
3. 启动发送端（8080）
4. 成功完成一次 SOAP 调用
5. 理解完整的 WSDL / SOAP 调用链路
