@Rem $Id: RUN_ImportCompiere.bat,v 1.2 2007/07/19 15:29:44 marmor01 Exp $

@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Import Compiere - %COMPIERE_HOME% (%COMPIERE_DB_NAME%)

@echo Re-Create Compiere User and import %COMPIERE_HOME%\data - (%COMPIERE_DB_NAME%)
@dir %COMPIERE_HOME%\data\*.*
@echo == The import will show warnings. This is OK ==
@pause

@Rem Parameter: <systemPassword> <CompiereID> <CompierePwd>
@call %COMPIERE_DB_PATH%\ImportCompiere %COMPIERE_DB_SYSTEM% %COMPIERE_DB_USER% %COMPIERE_DB_PASSWORD%

@pause
