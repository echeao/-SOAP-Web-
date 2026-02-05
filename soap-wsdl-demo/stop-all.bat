@echo off
chcp 65001 >nul
title WSDL/SOAP - 停止所有服务

echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║        WSDL/SOAP 学习实践项目 - 停止服务                   ║
echo ╚════════════════════════════════════════════════════════════╝
echo.

:: 结束占用 8089 端口的进程 (SOAP Server)
echo [1/2] 停止 SOAP Server (端口 8089)...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8089 ^| findstr LISTENING') do (
    taskkill /PID %%a /F >nul 2>&1
    echo      已停止进程 PID: %%a
)

:: 结束占用 8080 端口的进程 (SOAP Client)
echo [2/2] 停止 SOAP Client (端口 8080)...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do (
    taskkill /PID %%a /F >nul 2>&1
    echo      已停止进程 PID: %%a
)

echo.
echo 所有服务已停止！
echo.
pause
