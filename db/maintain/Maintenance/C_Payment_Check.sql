/*************************************************************************
 * The contents of this file are subject to the Compiere License.  You may
 * obtain a copy of the License at    http://www.compiere.org/license.html 
 * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either 
 * express or implied. See the License for details. Code: Compiere ERP+CRM
 * Copyright (C) 1999-2004 Jorg Janke, ComPiere, Inc. All Rights Reserved.
 *************************************************************************
 * $Id: C_Payment_Check.sql,v 1.1 2007/07/18 15:21:33 marmor01 Exp $
 ***
 * Title:	Check Payments
 * Description:
 *		Check Is Allocated Flag for Payments (no transfers)
 ************************************************************************/

/** Test
SELECT DocumentNo, PayAmt, C_Payment_Allocated(C_Payment_ID, C_Currency_ID) 
FROM C_Payment
WHERE IsAllocated <> DECODE(C_Payment_Allocated(C_Payment_ID, C_Currency_ID), PayAmt, 'Y', 'N')
  AND TenderType<>'X';
**/
  
UPDATE C_Payment
  SET IsAllocated = DECODE(C_Payment_Allocated(C_Payment_ID, C_Currency_ID), PayAmt, 'Y', 'N')
WHERE IsAllocated <> DECODE(C_Payment_Allocated(C_Payment_ID, C_Currency_ID), PayAmt, 'Y', 'N')
  AND TenderType<>'X';
  
COMMIT;
