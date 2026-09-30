@if (%COMPIERE_HOME%) == () (CALL ..\myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Create Oracle SQLJ - %COMPIERE_HOME% (%COMPIERE_DB_NAME%)
@Rem	
@Rem	Author + Copyright 1999-2005 Jorg Janke
@Rem	$Id: RUN_sqlj.bat,v 1.2 2007/07/19 15:28:22 marmor01 Exp $
@Rem

call create %COMPIERE_DB_USER%/%COMPIERE_DB_PASSWORD%

@pause
