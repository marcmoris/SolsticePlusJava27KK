@echo off

REM $Id: Windows_Service_Install.bat,v 1.2 2003/11/01 20:55:15 comdivisionys Exp $

if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)

REM Windows service install script.

