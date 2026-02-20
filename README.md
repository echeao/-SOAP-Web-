# SOAP Web Service 学习实践项目 (Spring Boot Enhanced)

这是一个用于学习 Java SOAP (JAX-WS / CXF) 通信的完整示例项目。
包含基于 Spring Boot 的 **服务端 (soap-server)** 和 **客户端 (soap-client)**，并配备了 **实时交互 Web UI**。

## 一、项目结构

```text
soap-wsdl-demo/
├── soap-server/          # [Spring Boot] SOAP 服务端 + WebSocket Dashboard
│   ├── src/main/java/com/example/soap/server/
│   │   ├── config/       # CXF (SOAP) 与 WebSocket 配置
│   │   ├── controller/   # REST API (控制面板)
│   │   ├── service/      # 业务逻辑 (UserService, StateService)
│   │   └── ServerApp.java
│   └── src/main/resources/static/index.html  # 服务端实时监控大屏
│
└── soap-client/          # [Spring Boot] SOAP 客户端 + 用户交互 UI
    ├── src/main/java/com/example/soap/client/
    │   ├── generated/    # 根据 WSDL 自动生成的代码
    │   └── service/      # SOAP 调用封装
    └── src/main/resources/static/index.html  # 客户端发送页面
```

## 二、如何运行

### 1. 启动服务端 (Server)
服务端监听端口: `8089`

```bash
cd soap-server
mvn clean spring-boot:run
```
- **SOAP Endpoint**: `http://localhost:8089/services/user?wsdl`
- **Dashboard UI**: [http://localhost:8089](http://localhost:8089)

### 2. 启动客户端 (Client)
客户端监听端口: `8080`

```bash
cd soap-client
mvn clean spring-boot:run
```
- **Client UI**: [http://localhost:8080](http://localhost:8080)

---

## 三、实时交互演示流程

1. **打开两个浏览器窗口**：
   - 窗口 A (Server): [http://localhost:8089](http://localhost:8089) - 你会看到 "Real-time Request Log"。
   - 窗口 B (Client): [http://localhost:8080](http://localhost:8080) - 发送消息的表单。

2. **发送请求**：
   - 在 Client UI 输入姓名 "Alice" 和年龄 "25"，点击发送。
   - **Server UI**: 立即收到 WebSocket 推送，显示 `[Time] Received: Name=Alice, Age=25`。
   - **Client UI**: 收到响应 `Alice，25岁，信息接收成功 (Server: Spring Boot)`。

3. **动态修改响应 (服务端控制)**：
   - 在 Server UI 左侧 "Response Template" 输入框中，修改为：
     `VIP用户 %s (等级: %d) 已受理!`
   - 点击 "Update Template"。
   - 回到 Client UI，再次发送 "Bob", "99"。
   - **Client UI**: 显示新响应 `VIP用户 Bob (等级: 99) 已受理!`。

---

## 四、技术实现细节

### 1. 服务端架构 (Spring Boot + CXF)
- 使用 `cxf-spring-boot-starter-jaxws` 发布 SOAP 服务。
- 使用 `spring-boot-starter-websocket` 实现实时日志推送。
- `ServerStateService` 维护内存中的“响应模板”和“日志历史”，模拟数据库状态。
- `UserServiceImpl` 在处理 SOAP 请求时，同时触发 WebSocket 广播。

### 2. 客户端架构
- 使用 `jaxws-maven-plugin` (wsimport) 在编译期根据 `src/main/resources/user.wsdl` 生成 Java 代理类。
- 前端使用 Fetch API 调用 Spring Boot REST 接口，后者转发调用 SOAP 服务。

### 3. 关键知识点
- **WSDL**: 服务契约。注意 Server 迁移到 Spring Boot/CXF 后，WSDL URL 变为 `/services/user?wsdl`，且生成的 XML 结构可能与原生 JAX-WS RI 略有不同（Client 端已更新适配）。
- **WebSocket (Stomp)**: 用于服务端向浏览器主动推送消息，实现 "Real-time Dashboard"。
- **CXF Configuration**: 在 `CxfConfig.java` 中通过 `EndpointImpl` 发布服务。

## 五、常见问题
1. **端口占用**: 确保 8089 和 8080 未被占用。
2. **WSDL 不匹配**: 如果你修改了 Server 代码（如改了方法名），需要重新导出 WSDL 到 Client 的 resources 目录，并重新运行 `mvn generate-sources`。

---
**Happy Coding!**
