/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software;
 you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program;
 if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package solstice.model;

/** Generated Model - DO NOT CHANGE */
import org.compiere.model.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for P_Payment_Transfert
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Payment_Transfert extends PO
{
/** Standard Constructor
@param ctx context
@param P_Payment_Transfert_ID id
@param trxName transaction
*/
public X_P_Payment_Transfert (Properties ctx, int P_Payment_Transfert_ID, String trxName)
{
super (ctx, P_Payment_Transfert_ID, trxName);
/** if (P_Payment_Transfert_ID == 0)
{
setAD_PInstance_ID (0);
setP_Employee_ID (0);
setP_Payment_ID (0);
setP_Payment_Transfert_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Payment_Transfert (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000140 */
public static final int Table_ID=2000140;

/** TableName=P_Payment_Transfert */
public static final String Table_Name="P_Payment_Transfert";

protected static KeyNamePair Model = new KeyNamePair(2000140,"P_Payment_Transfert");

protected BigDecimal accessLevel = new BigDecimal(3);
/** AccessLevel
@return 3 - Client - Org 
*/
protected int get_AccessLevel()
{
return accessLevel.intValue();
}
/** Load Meta Data
@param ctx context
@return PO Info
*/
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
/** Info
@return info
*/
public String toString()
{
StringBuffer sb = new StringBuffer ("X_P_Payment_Transfert[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Process Instance.
@param AD_PInstance_ID Instance of the process */
public void setAD_PInstance_ID (int AD_PInstance_ID)
{
if (AD_PInstance_ID < 1) throw new IllegalArgumentException ("AD_PInstance_ID is mandatory.");
set_Value ("AD_PInstance_ID", new Integer(AD_PInstance_ID));
}
/** Get Process Instance.
@return Instance of the process */
public int getAD_PInstance_ID() 
{
Integer ii = (Integer)get_Value("AD_PInstance_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
set_Value ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee .
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Payment_ID AD_Reference_ID=2000059 */
public static final int P_PAYMENT_ID_AD_Reference_ID=2000059;
/** Set Payment.
@param P_Payment_ID Payment */
public void setP_Payment_ID (int P_Payment_ID)
{
if (P_Payment_ID < 1) throw new IllegalArgumentException ("P_Payment_ID is mandatory.");
set_Value ("P_Payment_ID", new Integer(P_Payment_ID));
}
/** Get Payment.
@return Payment */
public int getP_Payment_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Payment_Transfert_ID.
@param P_Payment_Transfert_ID P_Payment_Transfert_ID */
public void setP_Payment_Transfert_ID (int P_Payment_Transfert_ID)
{
if (P_Payment_Transfert_ID < 1) throw new IllegalArgumentException ("P_Payment_Transfert_ID is mandatory.");
set_Value ("P_Payment_Transfert_ID", new Integer(P_Payment_Transfert_ID));
}
/** Get P_Payment_Transfert_ID.
@return P_Payment_Transfert_ID */
public int getP_Payment_Transfert_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Transfert_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
