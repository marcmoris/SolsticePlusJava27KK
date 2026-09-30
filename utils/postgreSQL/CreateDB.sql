/*************************************************************************
 * The contents of this file are subject to the compiere1 License.  You may
 * obtain a copy of the License at    http://www.compiere1.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: compiere1 ERP+CRM
 * Copyright (C) 1999-2007 Jorg Janke, compiere1, Inc. All Rights Reserved.
 *************************************************************************
 ****
 * Title:	Create EnterpriseDB database objects
 * Description:	
 *	Run as enterprisedb
 *      :compiereID serves both database name and primary Compiere userID
 ************************************************************************/
 
DROP DATABASE :compiereID;

DROP ROLE :compiereID CASCADE;

CREATE ROLE :compiereID LOGIN
 SUPERUSER INHERIT CREATEDB CREATEROLE;

ALTER USER :compiereID IDENTIFIED BY :compierePW;

--CREATE USER compiere PASSWORD 'compiere' IN GROUP compiere;
 

CREATE DATABASE :compiereID
 WITH OWNER = enterprisedb
 ENCODING = 'UNICODE'
 TABLESPACE = pg_default;

-- This isn't needed if import from pg_restore
--\CONNECT compiere 
--\c :compiereID;

--CREATE SCHEMA AUTHORIZATION compiere;
--CREATE SCHEMA :compiereID AUTHORIZATION :compiereID;

exit

