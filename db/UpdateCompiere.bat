@Title Update local Compiere DB
@Rem	$Id: UpdateCompiere.bat,v 1.1 2007/07/18 15:21:49 marmor01 Exp $

@dir database\DatabaseBuild.sql

@Echo	requires manual entry of exit

sqlplus compiere/compiere @database\DatabaseBuild.sql

sqlplus compiere/compiere @maintain\Maintenance\DBA_Recompile_Run.sql