@echo off
CALL "%~dp0myDevEnv.bat"
if "%~1"=="" goto usage
cd /d "%~1"
"%JAVA_HOME%\bin\java" -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.Main %2 %3 %4 %5
goto end
:usage
echo Usage: build_subproject.bat ^<subproject_dir^> [ant targets...]
:end
