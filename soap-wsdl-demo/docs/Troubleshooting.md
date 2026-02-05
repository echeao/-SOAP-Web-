# 常见问题解答

## 🔴 错误 1：无法连接到服务

**错误信息**：
```
Connection refused: connect
```

**原因**：SOAP Server 未启动或端口不正确。

**解决方案**：
1. 确保 Server 已启动：`java -jar soap-server-1.0.0.jar`
2. 检查端口是否被占用：`netstat -ano | findstr :8089`
3. 检查配置文件中的 URL

---

## 🔴 错误 2：Namespace 不匹配

**错误信息**：
```
Unexpected element ... expected ...
```

**原因**：客户端和服务端的 targetNamespace 不一致。

**解决方案**：
1. 检查 Server 的 `@WebService` 注解中的 `targetNamespace`
2. 检查 WSDL 文件中的 `targetNamespace`
3. 重新生成客户端代码：`mvn clean generate-sources`

---

## 🔴 错误 3：端口被占用

**错误信息**：
```
Address already in use: bind
```

**原因**：8089 或 8080 端口已被其他程序占用。

**解决方案**：

```powershell
# 查找占用端口的进程
netstat -ano | findstr :8089

# 结束进程（替换 <PID> 为实际的进程 ID）
taskkill /PID <PID> /F
```

或者修改配置使用其他端口。

---

## 🔴 错误 4：wsimport 生成失败

**错误信息**：
```
Failed to read WSDL
```

**原因**：生成客户端代码时无法访问 WSDL。

**解决方案**：
1. 本项目使用预置 WSDL 文件，无需在线访问
2. 确保 `src/main/resources/wsdl/user-service.wsdl` 存在
3. 执行 `mvn clean generate-sources -X` 查看详细日志

---

## 🔴 错误 5：ClassNotFoundException

**错误信息**：
```
ClassNotFoundException: com.example.soapclient.generated.UserService
```

**原因**：wsimport 生成的代码未添加到构建路径。

**解决方案**：
1. 执行 `mvn clean compile`
2. 在 IDE 中刷新项目
3. 检查 `target/generated-sources/wsimport` 目录

---

## 🔴 错误 6：XML 文件保存失败

**错误信息**：
```
IOException: Access denied
```

**原因**：没有写入 IFXML 目录的权限。

**解决方案**：
1. 手动创建 IFXML 目录
2. 检查目录权限
3. 修改 `application.properties` 中的输出路径

---

## 🟡 常见调试技巧

### 1. 开启 SOAP 消息日志

在 `application.properties` 中添加：
```properties
logging.level.com.sun.xml.ws=DEBUG
```

### 2. 使用 SoapUI 测试

1. 下载 [SoapUI](https://www.soapui.org/)
2. 创建 SOAP 项目，输入 WSDL 地址
3. 自动生成测试请求

### 3. 查看原始 SOAP 消息

可以使用 Wireshark 或 Fiddler 抓包查看实际发送的 SOAP 消息。

---

## 📚 相关链接

- [Oracle JAX-WS 教程](https://docs.oracle.com/javaee/7/tutorial/jaxws.htm)
- [SOAP 协议规范](https://www.w3.org/TR/soap12/)
- [WSDL 规范](https://www.w3.org/TR/wsdl20/)
