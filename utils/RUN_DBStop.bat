@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Stop DataBase Service  - %COMPIERE_HOME% (%COMPIERE_DB_NAME%)

@Rem $Id: RUN_DBStop.bat,v 1.2 2007/07/19 15:29:46 marmor01 Exp $

@CALL %COMPIERE_DB_PATH%\Stop.bat
@Echo Done stopping database %COMPIERE_HOME% (%COMPIERE_DB_NAME%)

@sleep 60
