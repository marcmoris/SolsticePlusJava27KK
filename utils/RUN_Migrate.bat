@Rem $Id: RUN_Migrate.bat,v 1.2 2007/07/19 15:29:43 marmor01 Exp $

@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Compiere Version Migration - %COMPIERE_HOME% (%COMPIERE_DB_NAME%)

@Echo Version Migration is an optional service for a fee.
@Echo Please check http://www.compiere.org/migrate/


@echo -------------------------------------
@echo Start UI
@echo -------------------------------------
@"%COMPIERE_JAVA%" %COMPIERE_JAVA_OPTIONS% -cp %CLASSPATH% com.compiere.client.Support

@echo -------------------------------------
@if (%COMPIERE_DB_TYPE%) == (%COMPIERE_DB_PATH%) echo Create SQLJ 
@echo -------------------------------------
@if (%COMPIERE_DB_TYPE%) == (%COMPIERE_DB_PATH%) call %COMPIERE_HOME%\Utils\%COMPIERE_DB_PATH%\create %COMPIERE_DB_USER%/%COMPIERE_DB_PASSWORD%

@echo -------------------------------------
@echo Check System
@echo -------------------------------------
@sqlplus %COMPIERE_DB_USER%/%COMPIERE_DB_PASSWORD%@%COMPIERE_DB_NAME% @%COMPIERE_HOME%\Utils\%COMPIERE_DB_PATH%\AfterImport.sql


@pause


