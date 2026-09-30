@Title Build dbPort
@Rem   $Header: /cvsrepo/SolsticeV7/dbPort/RUN_build.bat,v 1.1 2007/07/18 15:22:38 marmor01 Exp $

@CALL ..\utils_dev\myDevEnv.bat
@IF NOT %COMPIERE_ENV%==Y GOTO NOBUILD

@echo Cleanup ...
@"%JAVA_HOME%\bin\java" -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.Main clean

@echo Building ...
@"%JAVA_HOME%\bin\java" -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.Main dist

@Echo Done ...
@sleep 60
@Pause
@exit

:NOBUILD
@Echo Check myDevEnv.bat (copy from myDevEnvTemplate.bat)
@Pause
