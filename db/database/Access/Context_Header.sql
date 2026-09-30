CREATE OR REPLACE PACKAGE Compiere_Context
/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: Compiere ERP+CRM
 * Copyright (C) 1999-2001 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: Context_Header.sql,v 1.1 2007/07/18 15:21:43 marmor01 Exp $
 ***
 * Title:	 Context Header
 * Description:
 ************************************************************************/
AS
	PROCEDURE Login (UserName VARCHAR2, UserPwd VARCHAR2, UserRole VARCHAR2, UserLang VARCHAR2 DEFAULT 'USAENG');
	--
	PROCEDURE SetEnv (Name VARCHAR2, NewValue VARCHAR);
	--
	FUNCTION GetPredicate (ObjectSchema VARCHAR2, ObjectName VARCHAR2) RETURN VARCHAR2; 
	--
END Compiere_Context;
/
