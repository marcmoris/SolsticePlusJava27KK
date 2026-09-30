@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Compiere Server Stop - %COMPIERE_HOME%

@Rem $Id: RUN_Server2Stop.bat,v 1.2 2007/07/19 15:29:42 marmor01 Exp $

@Echo Apps Server stop of %COMPIERE_APPS_TYPE%
@GOTO END

:END
@sleep 30
@Exit
