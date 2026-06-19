@echo off
chcp 65001 >nul
if not exist out mkdir out
javac -encoding UTF-8 -d out src\*.java
if errorlevel 1 (
  echo.
  echo Hubo errores de compilacion.
  pause
  exit /b 1
)
java -cp out Main
pause
