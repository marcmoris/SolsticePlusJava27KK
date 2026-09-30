@Title Build Print
@Rem   $Header: /cvsrepo/SolsticeV7/print/RUN_build.bat,v 1.1 2007/07/18 14:59:05 marmor01 Exp $

@CALL ..\utils_dev\myDevEnv.bat

@IF %COMPIERE_ENV%==N GOTO NOBUILD
@IF NOT %COMPIERE_ENV%==Y GOTO NOBUILD

@echo Cleanup ...
@"%JAVA_HOME%\bin\java" -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.Main clean

@echo Building ...
@"%JAVA_HOME%\bin\java" -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.Main printDistribute

@Echo Done ...
@sleep 60
@exit

:NOBUILD
@Echo Check myDevEnv.bat (copy from myDevEnvTemplate.bat)
@Pause