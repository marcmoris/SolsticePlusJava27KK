@Title Build Solstice Web Modules
@Rem $Header: /cvsrepo/SolsticeV7/solsticeWebModules/RUN_build.bat,v 1.2 2007/07/18 21:06:52 marmor01 Exp $

@CALL ..\utils_dev\myDevEnv.bat

@IF %COMPIERE_ENV%==N GOTO NOBUILD
@echo Cleanup ...
@%JAVA_HOME%\bin\java -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.launch.Launcher clean
@echo Building ...
@%JAVA_HOME%\bin\java -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.launch.Launcher

@pause
:NOBUILD