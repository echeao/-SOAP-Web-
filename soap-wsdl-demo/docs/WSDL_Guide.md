# WSDL 学习指南

## 什么是 WSDL？

**WSDL**（Web Services Description Language）是一种基于 XML 的语言，用于描述 Web 服务的接口。

简单来说，WSDL 就像一份"说明书"，告诉客户端：
- 服务提供哪些功能（操作）
- 每个功能需要什么参数
- 每个功能返回什么结果
- 服务的访问地址是什么

## WSDL 文档结构

```xml
<definitions>
    ├── <types>        <!-- 数据类型定义 -->
    ├── <message>      <!-- 消息格式定义 -->
    ├── <portType>     <!-- 操作集合（类似接口） -->
    ├── <binding>      <!-- 传输协议绑定 -->
    └── <service>      <!-- 服务地址 -->
</definitions>
```

### 1. types（类型）

定义消息中使用的数据类型：

```xml
<types>
    <xsd:schema>
        <xsd:complexType name="userResponse">
            <xsd:sequence>
                <xsd:element name="status" type="xsd:string"/>
                <xsd:element name="code" type="xsd:int"/>
            </xsd:sequence>
        </xsd:complexType>
    </xsd:schema>
</types>
```

**类比**：类似 Java 中的 class 定义。

### 2. message（消息）

定义请求和响应的消息格式：

```xml
<message name="getUserInfoRequest">
    <part name="parameters" element="tns:getUserInfo"/>
</message>
```

**类比**：类似方法的参数列表。

### 3. portType（端口类型）

定义服务提供的操作：

```xml
<portType name="UserService">
    <operation name="getUserInfo">
        <input message="tns:getUserInfoRequest"/>
        <output message="tns:getUserInfoResponse"/>
    </operation>
</portType>
```

**类比**：类似 Java 中的 interface。

### 4. binding（绑定）

定义消息格式和传输协议：

```xml
<binding name="UserServiceBinding" type="tns:UserService">
    <soap:binding style="document" 
                  transport="http://schemas.xmlsoap.org/soap/http"/>
</binding>
```

### 5. service（服务）

定义服务的具体访问地址：

```xml
<service name="UserService">
    <port name="UserServicePort" binding="tns:UserServiceBinding">
        <soap:address location="http://localhost:8089/ws/user"/>
    </port>
</service>
```

## 命名空间（Namespace）

命名空间用于避免名称冲突，类似 Java 的 package：

```xml
targetNamespace="http://example.com/soap/user"
```

**重要**：客户端和服务端的 namespace 必须一致！

## SOAP 消息格式

SOAP 消息由三部分组成：

```xml
<soap:Envelope>
    <soap:Header>
        <!-- 可选：元数据 -->
    </soap:Header>
    <soap:Body>
        <!-- 必须：实际内容 -->
    </soap:Body>
</soap:Envelope>
```

## 调用流程

```
1. 客户端获取 WSDL          GET ?wsdl
2. 解析 WSDL，了解接口      
3. 构造 SOAP 请求消息       
4. 发送 HTTP POST          POST /ws/user
5. 服务端处理请求           
6. 返回 SOAP 响应           
7. 客户端解析响应           
```

## 与 Java 类的对应关系

| WSDL 概念 | Java 概念 |
|-----------|-----------|
| types/complexType | class |
| portType | interface |
| operation | method |
| message | 参数/返回值 |
| service | 服务实例 |

## 查看本项目的 WSDL

启动 Server 后访问：

```
http://localhost:8089/ws/user?wsdl
```

你会看到完整的 WSDL 文档，包含上述所有元素。
