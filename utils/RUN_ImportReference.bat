@Rem $Id: RUN_ImportReference.bat,v 1.2 2007/07/19 15:29:42 marmor01 Exp $

@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Import Reference - %COMPIERE_HOME% (%COMPIERE_DB_NAME%)


@echo Re-Create Reference User and import %COMPIERE_HOME%\data - (%COMPIERE_DB_NAME%)
@dir %COMPIERE_HOME%\data\*.*
@echo == The import will show warnings. This is OK ==
@pause

@Rem Parameter: <systemAccount> <CompiereID> <CompierePwd>
@call %COMPIERE_DB_PATH%\ImportCompiere %COMPIERE_DB_SYSTEM% reference reference

@pause
