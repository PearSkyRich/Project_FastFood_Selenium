@echo off
set DB_USERNAME=root
set DB_PASSWORD=Ltp16082005#
:: Chuyển đến thư mục chứa file .bat
cd /d "%~dp0"

echo ==============================
echo Starting Backend...
echo ==============================

start "Backend" cmd /k "cd backend && mvn spring-boot:run"

echo Waiting for backend to start...
timeout /t 10 > nul

echo ==============================
echo Starting Frontend...
echo ==============================

start "Frontend" cmd /k "cd frontend && npm run dev"

echo Waiting for frontend to start...
timeout /t 10 > nul

echo ==============================
echo Running Selenium Tests...
echo ==============================

cd automation
call mvn clean test -Dtest=LoginTests

pause