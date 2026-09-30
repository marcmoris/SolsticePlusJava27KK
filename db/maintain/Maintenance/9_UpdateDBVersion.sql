/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for details. Code: Compiere ERP+CRM
 * Copyright (C) 1999-2001 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: 9_UpdateDBVersion.sql,v 1.1 2007/07/18 15:21:40 marmor01 Exp $
 ***
 * Title:	Update Database Version
 * Description:
 ************************************************************************/

UPDATE AD_System 
-----------------==========-----
  SET	ReleaseNo = '261',
        Version='2007-04-27',
-----------------==========----- 
        Created=Sysdate,
		Updated=SysDate;
--
COMMIT;
--
SELECT * FROM AD_System;

