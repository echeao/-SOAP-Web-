package com.example.soapclient.util;

import com.example.soapclient.generated.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * XML 文件工具类 - 保存 SOAP 响应到文件
 * 
 * 【教学说明】
 * 这个工具类展示了如何将 SOAP 响应保存为 XML 文件。
 * 
 * 【关于 wsimport 生成的类】
 * wsimport 生成的类通常没有 @XmlRootElement 注解，
 * 因此使用 JAXB Marshaller 直接序列化会失败。
 * 这里采用手动构建 XML 的方式，更直观且可控。
 * 
 * 【文件命名规则】
 * {操作名}_{时间戳}.xml
 * 例如：getUserInfo_20260205_201500.xml
 * 
 * @author 教学示例
 * @since 1.0.0
 */
@Component
public class XmlFileUtil {
    
    /**
     * XML 输出目录（从配置文件读取）
     */
    @Value("${xml.output.directory:../IFXML}")
    private String outputDirectory;
    
    /**
     * 日期时间格式化器
     */
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    /**
     * 保存 SOAP 响应到 XML 文件
     * 
     * @param response     响应对象
     * @param operationName 操作名称（用于文件命名）
     * @return 保存的文件路径
     */
    public String saveResponseToFile(UserResponse response, String operationName) {
        if (response == null) {
            System.out.println("【XmlFileUtil】响应为空，跳过保存");
            return null;
        }
        
        try {
            // 步骤 1: 确保输出目录存在
            Path outputPath = ensureDirectoryExists();
            
            // 步骤 2: 生成文件名
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String filename = String.format("%s_%s.xml", operationName, timestamp);
            Path filePath = outputPath.resolve(filename);
            
            // 步骤 3: 手动构建 XML 内容并保存
            saveToXmlManually(response, filePath.toFile());
            
            System.out.println("【XmlFileUtil】响应已保存到: " + filePath.toAbsolutePath());
            return filePath.toAbsolutePath().toString();
            
        } catch (Exception e) {
            System.err.println("【XmlFileUtil】保存文件失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 确保输出目录存在
     * 
     * @return 输出目录的 Path 对象
     * @throws IOException 创建目录失败时抛出
     */
    private Path ensureDirectoryExists() throws IOException {
        Path path = Paths.get(outputDirectory);
        
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            System.out.println("【XmlFileUtil】创建目录: " + path.toAbsolutePath());
        }
        
        return path;
    }
    
    /**
     * 手动构建 XML 并保存到文件
     * 
     * 【教学说明】
     * 由于 wsimport 生成的类没有 @XmlRootElement 注解，
     * 使用 JAXB 直接序列化会失败。
     * 这里使用手动构建 XML 的方式，更直观且可控。
     * 
     * @param response 响应对象
     * @param file     输出文件
     * @throws IOException 文件写入失败时抛出
     */
    private void saveToXmlManually(UserResponse response, File file) throws IOException {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<userResponse>\n");
        xml.append("    <status>").append(nullSafe(response.getStatus())).append("</status>\n");
        xml.append("    <code>").append(response.getCode()).append("</code>\n");
        xml.append("    <message>").append(nullSafe(response.getMessage())).append("</message>\n");
        
        if (response.getData() != null) {
            xml.append("    <data>\n");
            xml.append("        <userId>").append(response.getData().getUserId()).append("</userId>\n");
            xml.append("        <name>").append(nullSafe(response.getData().getName())).append("</name>\n");
            xml.append("        <age>").append(response.getData().getAge()).append("</age>\n");
            xml.append("        <greeting>").append(nullSafe(response.getData().getGreeting())).append("</greeting>\n");
            xml.append("        <timestamp>").append(nullSafe(response.getData().getTimestamp())).append("</timestamp>\n");
            xml.append("    </data>\n");
        }
        
        xml.append("</userResponse>\n");
        
        Files.write(file.toPath(), xml.toString().getBytes("UTF-8"));
    }
    
    /**
     * 空值安全处理
     */
    private String nullSafe(String value) {
        return value != null ? escapeXml(value) : "";
    }
    
    /**
     * 转义 XML 特殊字符
     */
    private String escapeXml(String value) {
        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;");
    }
}
