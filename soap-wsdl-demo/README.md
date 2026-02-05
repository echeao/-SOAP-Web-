# WSDL/SOAP Java 学习实践项目

> 一个完整、可运行的 SOAP Web Service 示例项目，用于学习 WSDL 和 SOAP 协议。

## 🎯 项目目标

通过实践理解：
- WSDL（Web Services Description Language）描述语言
- SOAP（Simple Object Access Protocol）通信协议
- JAX-WS（Java API for XML Web Services）编程模型
- JAXB（Java Architecture for XML Binding）对象映射

## 📁 项目结构

```
soap-wsdl-demo/
├── soap-server/          # SOAP 服务端（端口 8089）
├── soap-client/          # SOAP 客户端（端口 8080）
├── IFXML/                # XML 响应保存目录
├── docs/                 # 学习文档
└── pom.xml              # 父 POM
```

## 🚀 快速开始

### 前提条件

- JDK 11 或更高版本
- Maven 3.6+

### 步骤 1：编译项目

```powershell
cd soap-wsdl-demo

# 编译 Server
cd soap-server
mvn clean package -DskipTests

# 编译 Client（需要新开终端）
cd ../soap-client
mvn clean package -DskipTests
```

### 步骤 2：启动 Server

```powershell
cd soap-server
java -jar target/soap-server-1.0.0.jar
```

成功启动后会看到：
```
【启动成功】SOAP Server 已启动！
服务地址: http://localhost:8089/ws/user
WSDL 地址: http://localhost:8089/ws/user?wsdl
```

### 步骤 3：验证 WSDL

在浏览器中打开：http://localhost:8089/ws/user?wsdl

应看到 XML 格式的服务描述文档。

### 步骤 4：启动 Client

```powershell
# 新开一个终端（保持 Server 运行）
cd soap-client
mvn spring-boot:run
```

### 步骤 5：测试 SOAP 调用

浏览器访问或使用 PowerShell：

```powershell
# 获取用户信息
Invoke-RestMethod "http://localhost:8080/api/test/call?name=张三&age=25"

# 创建用户
Invoke-RestMethod "http://localhost:8080/api/test/create?name=李四&age=30"

# 健康检查
Invoke-RestMethod "http://localhost:8080/api/test/health"
```

### 步骤 6：查看保存的 XML

```powershell
ls IFXML/
```

## 📖 API 接口

| 接口 | 说明 | 示例 |
|------|------|------|
| `/api/test/call` | 获取用户信息 | `?name=张三&age=25` |
| `/api/test/create` | 创建用户 | `?name=李四&age=30` |
| `/api/test/health` | 健康检查 | 无参数 |

## 📚 学习资料

- [WSDL 学习指南](docs/WSDL_Guide.md)
- [常见问题解答](docs/Troubleshooting.md)

## 🔧 技术栈

| 模块 | 技术 | 版本 |
|------|------|------|
| Server | Java + JAX-WS | 11 |
| Client | Spring Boot | 2.7.x |
| 构建 | Maven | 3.6+ |

## 📝 许可证

本项目仅供学习使用。
