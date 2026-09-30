/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: Compiere ERP+CRM
 * Copyright (C) 1999-2003 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: Start.sql,v 1.1 2007/07/18 15:21:55 marmor01 Exp $
 ***
 * Title:	Start Database
 * Description:	
 *	Start the script via 
 *	sqlplus "system/$COMPIERE_DB_SYSTEM AS SYSDBA" @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/Start.sql
 ************************************************************************/
set pause off
set echo on
startup
exit
