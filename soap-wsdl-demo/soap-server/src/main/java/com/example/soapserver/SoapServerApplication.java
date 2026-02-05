package com.example.soapserver;

import com.example.soapserver.service.UserServiceImpl;

import javax.xml.ws.Endpoint;

/**
 * SOAP Server 启动类
 * 
 * 【教学说明】
 * 这是 SOAP 服务端的入口类，负责发布 Web Service。
 * 
 * 核心方法说明：
 * - Endpoint.publish(address, implementor)
 * 将 Web Service 实现类发布到指定地址。
 * JAX-WS 运行时会：
 * 1. 启动内置的 HTTP 服务器
 * 2. 根据 @WebService 注解自动生成 WSDL
 * 3. 处理进入的 SOAP 请求并调用实现类方法
 * 
 * 【WSDL 生成机制】
 * 当服务启动后，JAX-WS 会自动生成 WSDL 描述文档。
 * 访问 http://localhost:8089/ws/user?wsdl 即可获取。
 * 
 * 【注意事项】
 * 1. 地址格式必须是 http://host:port/path
 * 2. 端口不能被其他程序占用
 * 3. 服务实现类必须有 @WebService 注解
 * 
 * @author 教学示例
 * @since 1.0.0
 */
public class SoapServerApplication {

    /**
     * 服务发布地址
     * 格式：http://主机:端口/路径
     */
    private static final String SERVICE_URL = "http://localhost:8089/ws/user";

    /**
     * 主方法 - 启动 SOAP 服务
     * 
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           WSDL/SOAP Java 学习实践项目 - Server              ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();

        try {
            // 创建服务实现类实例
            UserServiceImpl implementor = new UserServiceImpl();

            // 发布 Web Service
            // Endpoint.publish() 是 JAX-WS 提供的便捷方法
            // 它会启动一个轻量级的 HTTP 服务器来托管 Web Service
            Endpoint endpoint = Endpoint.publish(SERVICE_URL, implementor);

            // 输出启动信息
            System.out.println("【启动成功】SOAP Server 已启动！");
            System.out.println();
            System.out.println("┌─────────────────────────────────────────────────────────┐");
            System.out.println("│ 服务地址: " + SERVICE_URL);
            System.out.println("│ WSDL 地址: " + SERVICE_URL + "?wsdl");
            System.out.println("└─────────────────────────────────────────────────────────┘");
            System.out.println();
            System.out.println("【提示】在浏览器中打开 WSDL 地址查看服务描述文档");
            System.out.println("【提示】按 Ctrl+C 停止服务");
            System.out.println();

            // 添加关闭钩子，优雅停机
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println();
                System.out.println("【关闭中】正在停止 SOAP Server...");
                endpoint.stop();
                System.out.println("【已关闭】SOAP Server 已停止");
            }));

            // 保持主线程运行（否则程序会立即退出）
            // 在生产环境中，通常会有更复杂的生命周期管理
            Thread.currentThread().join();

        } catch (Exception e) {
            System.err.println("【错误】启动 SOAP Server 失败: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
