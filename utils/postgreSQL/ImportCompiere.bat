@Echo	Compiere Database Import		$Revision: 1.2 $

@Rem $Id: ImportCompiere.bat,v 1.2 2007/07/19 21:48:30 marmor01 Exp $

@Echo	Importing Compiere EnterpriseDB DB from %COMPIERE_HOME%\data\compiereEDB.jar (%COMPIERE_DB_NAME%)

@if (%COMPIERE_HOME%) == () goto environment
@if (%COMPIERE_DB_NAME%) == () goto environment
@Rem Must have parameters enterprisedbPassword CompiereID CompierePwd
@if (%1) == () goto usage
@if (%2) == () goto usage
@if (%3) == () goto usage

@echo -------------------------------------
@echo Re-Create DB Objects. 
@echo Please make sure your EnterpriseDB bin directory is in PATH.
@echo -------------------------------------
@echo edb-psql -d mgmtsvr -U enterprisedb -r %1 -f %COMPIERE_HOME%\Utils\postgreSQL\CreateDB.sql -v compiereID=%2 -v compierePW=%3
@edb-psql -d mgmtsvr -U enterprisedb -r %1 -f %COMPIERE_HOME%\Utils\postgreSQL\CreateDB.sql -v compiereID=%2 -v compierePW=%3

@echo -------------------------------------
@echo Import compiereEDB.jar
@echo This might take more than ten minutes ...
@echo -------------------------------------
@SET PGPASSWORD=%3

@if "%2" == "reference" goto :refUser
@echo pg_restore -d %COMPIERE_DB_NAME% %COMPIERE_HOME%\data\edb.compiere.backup -U %2
@pg_restore -d %COMPIERE_DB_NAME% %COMPIERE_HOME%\data\edb.compiere.backup -U %2
@goto end

:refUser
@echo pg_restore -d %2 %COMPIERE_HOME%\data\edb.compiere.backup -U %2
@pg_restore -d %2 %COMPIERE_HOME%\data\edb.compiere.backup -U %2

@rem pg_restore -d reference %COMPIERE_HOME%\data\edb.compiere.backup -U enterprisedb
@echo edb-psql -d %2 -U %2 -r %3 -f %COMPIERE_HOME%\Utils\postgreSQL\AlterSchema.sql -v referenceID=%2 -v compiereID=compiere
@edb-psql -d %2 -U %2 -r %3 -f %COMPIERE_HOME%\Utils\postgreSQL\AlterSchema.sql -v referenceID=%2 -v compiereID=compiere

@goto end

:environment
@Echo Please make sure that the enviroment variables are set correctly:
@Echo		COMPIERE_HOME	e.g. D:\Compiere2
@Echo		COMPIERE_DB_NAME	e.g. compiere

:usage
@echo Usage:		%0 <enterprisedb_pw> <CompiereID> <CompierePW>
@echo Example:	%0 Compiere Compiere compiere

:end
@SET PGPASSWORD=
