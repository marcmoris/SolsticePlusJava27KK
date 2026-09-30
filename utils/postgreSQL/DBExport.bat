@Echo	Compiere Database Export 	$Revision: 1.2 $

@Rem $Id: DBExport.bat,v 1.2 2007/07/19 21:48:29 marmor01 Exp $

@Echo Saving database %1@%COMPIERE_DB_NAME% to %COMPIERE_HOME%\data\ExpDat.dmp

@if (%COMPIERE_HOME%) == () goto environment
@if (%COMPIERE_DB_NAME%) == () goto environment
@Rem Must have parameter: userAccount
@if (%1) == () goto usage

@exp %1@%COMPIERE_DB_NAME% FILE=%COMPIERE_HOME%\data\ExpDat.dmp Log=%COMPIERE_HOME%\data\ExpDat.log CONSISTENT=Y OWNER=Compiere 

@cd %COMPIERE_HOME%\Data
@jar cvfM ExpDat.jar ExpDat.dmp

@goto end

:environment
@Echo Please make sure that the enviroment variables are set correctly:
@Echo		COMPIERE_HOME	e.g. D:\Compiere2
@Echo		COMPIERE_DB_NAME 	e.g. compiere.compiere.org

:usage
@echo Usage:		%0 <userAccount>
@echo Examples:	%0 compiere/compiere

:end
