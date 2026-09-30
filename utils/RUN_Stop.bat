@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Stop Compiere  - %COMPIERE_HOME% (%COMPIERE_DB_NAME%)

@Rem $Id: RUN_Stop.bat,v 1.2 2007/07/19 15:29:46 marmor01 Exp $

@CALL %COMPIERE_HOME%\utils\RUN_Server2Stop.bat

@CALL %COMPIERE_DB_PATH%\Stop.bat

