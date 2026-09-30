@echo off
title Solstice+ Timesheet API Server
echo =======================================================
echo   Demarrage du Microservice Solstice+ Timesheet
echo   Port: 3001
echo =======================================================
cd /d "%~dp0"
node src\server.js
pause
