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
/** Generated Model for P_Notice_Bank_Deposit
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Notice_Bank_Deposit extends PO
{
/** Standard Constructor
@param ctx context
@param P_Notice_Bank_Deposit_ID id
@param trxName transaction
*/
public X_P_Notice_Bank_Deposit (Properties ctx, int P_Notice_Bank_Deposit_ID, String trxName)
{
super (ctx, P_Notice_Bank_Deposit_ID, trxName);
/** if (P_Notice_Bank_Deposit_ID == 0)
{
setNOTICE (null);
setP_Notice_Bank_Deposit_ID (0);
setP_Period_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Notice_Bank_Deposit (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100867 */
public static final int Table_ID=2100867;

/** TableName=P_Notice_Bank_Deposit */
public static final String Table_Name="P_Notice_Bank_Deposit";

protected static KeyNamePair Model = new KeyNamePair(2100867,"P_Notice_Bank_Deposit");

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
StringBuffer sb = new StringBuffer ("X_P_Notice_Bank_Deposit[").append(get_ID()).append("]");
return sb.toString();
}

/** C_Activity_ID AD_Reference_ID=142 */
public static final int C_ACTIVITY_ID_AD_Reference_ID=142;
/** Set Crew cost.
@param C_Activity_ID Crew cost */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
 else 
set_Value ("C_Activity_ID", new Integer(C_Activity_ID));
}
/** Get Crew cost.
@return Crew cost */
public int getC_Activity_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Notice.
@param NOTICE Notice */
public void setNotice (String Notice)
{
if (Notice == null) throw new IllegalArgumentException ("Notice is mandatory.");
if (Notice.length() > 500)
{
log.warning("Length > 500 - truncated");
Notice = Notice.substring(0,499);
}
set_Value ("Notice", Notice);
}
/** Get Notice.
@return Notice */
public String getNotice() 
{
return (String)get_Value("Notice");
}

/** P_Department_ID AD_Reference_ID=2100460 */
public static final int P_DEPARTMENT_ID_AD_Reference_ID=2100460;
/** Set Department.
@param P_Department_ID Department */
public void setP_Department_ID (int P_Department_ID)
{
if (P_Department_ID <= 0) set_Value ("P_Department_ID", null);
 else 
set_Value ("P_Department_ID", new Integer(P_Department_ID));
}
/** Get Department.
@return Department */
public int getP_Department_ID() 
{
Integer ii = (Integer)get_Value("P_Department_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID <= 0) set_Value ("P_Employee_ID", null);
 else 
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
/** Set P_Notice_Bank_Deposit_ID.
@param P_Notice_Bank_Deposit_ID P_Notice_Bank_Deposit_ID */
public void setP_Notice_Bank_Deposit_ID (int P_Notice_Bank_Deposit_ID)
{
if (P_Notice_Bank_Deposit_ID < 1) throw new IllegalArgumentException ("P_Notice_Bank_Deposit_ID is mandatory.");
set_ValueNoCheck ("P_Notice_Bank_Deposit_ID", new Integer(P_Notice_Bank_Deposit_ID));
}
/** Get P_Notice_Bank_Deposit_ID.
@return P_Notice_Bank_Deposit_ID */
public int getP_Notice_Bank_Deposit_ID() 
{
Integer ii = (Integer)get_Value("P_Notice_Bank_Deposit_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Period.
@param P_Period_ID Period of the Calendar   */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID < 1) throw new IllegalArgumentException ("P_Period_ID is mandatory.");
set_Value ("P_Period_ID", new Integer(P_Period_ID));
}
/** Get Period.
@return Period of the Calendar   */
public int getP_Period_ID() 
{
Integer ii = (Integer)get_Value("P_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 30)
{
log.warning("Length > 30 - truncated");
Value = Value.substring(0,29);
}
set_Value ("Value", Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
}
