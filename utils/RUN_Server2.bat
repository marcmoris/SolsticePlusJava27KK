@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Compiere Server Start - %COMPIERE_HOME% (%COMPIERE_APPS_TYPE%)

@Rem $Id: RUN_Server2.bat,v 1.2 2007/07/19 15:29:45 marmor01 Exp $

@Rem  To use your own Encryption class (implementing org.compiere.util.SecureInterface),
@Rem  you need to set it here (and in the client start script) - example:
@Rem  SET SECURE=-DCOMPIERE_SECURE=org.compiere.util.Secure
@SET SECURE=


@Echo Apps Server start of %COMPIERE_APPS_TYPE%
@GOTO END

:END
@Sleep 60
@Exit

