@Title Compiere Clean
@Rem $Header: /cvsrepo/SolsticeV7/utils_dev/RUN_clean.bat,v 1.2 2007/07/19 21:46:15 marmor01 Exp $

@CALL myDevEnv.bat
@IF NOT %COMPIERE_ENV%==Y GOTO NOBUILD

@echo Cleanup ...
@"%JAVA_HOME%\bin\java" -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.Main clean

@sleep 60
@exit
:NOBUILD
@Echo Check myDevEnv.bat (copy from myDevEnvTemplate.bat)
@Pause