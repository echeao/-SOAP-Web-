@echo off
chcp 65001 >nul
title WSDL/SOAP 学习实践项目 - 启动器

echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║        WSDL/SOAP 学习实践项目 - 一键启动器                 ║
echo ╚════════════════════════════════════════════════════════════╝
echo.

:: 设置项目根目录
set PROJECT_DIR=%~dp0

:: 检查 Server JAR 是否存在
if not exist "%PROJECT_DIR%soap-server\target\soap-server-1.0.0.jar" (
    echo [警告] Server JAR 不存在，正在编译...
    cd /d "%PROJECT_DIR%soap-server"
    call mvn clean package -DskipTests -q
    if errorlevel 1 (
        echo [错误] Server 编译失败！
        pause
        exit /b 1
    )
    echo [成功] Server 编译完成
)

:: 启动 SOAP Server（新窗口）
echo [1/3] 启动 SOAP Server...
start "SOAP Server (8089)" cmd /k "cd /d "%PROJECT_DIR%soap-server" && java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/soap-server-1.0.0.jar"

:: 等待 Server 启动
echo      等待 Server 启动 (5秒)...
timeout /t 5 /nobreak >nul

:: 启动 SOAP Client（新窗口）
echo [2/3] 启动 SOAP Client...
start "SOAP Client (8080)" cmd /k "chcp 65001 >nul && cd /d "%PROJECT_DIR%soap-client" && mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8""

:: 等待 Client 启动
echo      等待 Client 启动 (10秒)...
timeout /t 10 /nobreak >nul

:: 打开浏览器
echo [3/3] 打开测试页面...
start http://localhost:8080/

echo.
echo ════════════════════════════════════════════════════════════
echo   启动完成！
echo.
echo   Server: http://localhost:8089/ws/user
echo   Client: http://localhost:8080/
echo   WSDL:   http://localhost:8089/ws/user?wsdl
echo.
echo   要停止服务，请关闭两个 CMD 窗口
echo ════════════════════════════════════════════════════════════
echo.
pause
