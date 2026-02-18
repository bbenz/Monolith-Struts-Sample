@echo off
REM Quick Start Script for AI Ski Trip Advisor (Windows)

echo ╔════════════════════════════════════════════╗
echo ║   AI SKI TRIP ADVISOR - QUICK START        ║
echo ╚════════════════════════════════════════════╝
echo.

echo Checking prerequisites...

REM Check Java
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Java is not installed. Please install Java 11 or higher.
    pause
    exit /b 1
)
echo ✓ Java found

REM Check Maven
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Maven is not installed. Please install Maven 3.6 or higher.
    pause
    exit /b 1
)
echo ✓ Maven found

echo.
echo Building the project...
call mvn clean package

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✓ Build successful!
    echo.
    echo Starting AI Ski Trip Advisor...
    echo.
    call mvn exec:java
) else (
    echo.
    echo ❌ Build failed. Please check the error messages above.
    echo.
    echo Common issues:
    echo 1. Update database.properties with your database credentials
    echo 2. Update OpenAI API key in database.properties
    echo 3. Ensure PostgreSQL database is running
    pause
    exit /b 1
)
