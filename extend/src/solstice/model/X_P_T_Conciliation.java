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
/** Generated Model for P_T_Conciliation
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_T_Conciliation extends PO
{
/** Standard Constructor
@param ctx context
@param P_T_Conciliation_ID id
@param trxName transaction
*/
public X_P_T_Conciliation (Properties ctx, int P_T_Conciliation_ID, String trxName)
{
super (ctx, P_T_Conciliation_ID, trxName);
/** if (P_T_Conciliation_ID == 0)
{
setAD_PInstance_ID (0);
setP_Employee_ID (0);
setP_Frequency_ID (0);
setP_Payment_Group_ID (0);
setP_Payment_ID (0);
setP_Period_ID (0);
setP_T_Conciliation_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_T_Conciliation (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100765 */
public static final int Table_ID=2100765;

/** TableName=P_T_Conciliation */
public static final String Table_Name="P_T_Conciliation";

protected static KeyNamePair Model = new KeyNamePair(2100765,"P_T_Conciliation");

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
StringBuffer sb = new StringBuffer ("X_P_T_Conciliation[").append(get_ID()).append("]");
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
/** Set Net Pay.
@param NetPay Net Pay */
public void setNetPay (BigDecimal NetPay)
{
set_Value ("NetPay", NetPay);
}
/** Get Net Pay.
@return Net Pay */
public BigDecimal getNetPay() 
{
BigDecimal bd = (BigDecimal)get_Value("NetPay");
if (bd == null) return Env.ZERO;
return bd;
}
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
/** Set Frequency.
@param P_Frequency_ID Frequency */
public void setP_Frequency_ID (int P_Frequency_ID)
{
if (P_Frequency_ID < 1) throw new IllegalArgumentException ("P_Frequency_ID is mandatory.");
set_Value ("P_Frequency_ID", new Integer(P_Frequency_ID));
}
/** Get Frequency.
@return Frequency */
public int getP_Frequency_ID() 
{
Integer ii = (Integer)get_Value("P_Frequency_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Payment Group.
@param P_Payment_Group_ID Payment Group */
public void setP_Payment_Group_ID (int P_Payment_Group_ID)
{
if (P_Payment_Group_ID < 1) throw new IllegalArgumentException ("P_Payment_Group_ID is mandatory.");
set_Value ("P_Payment_Group_ID", new Integer(P_Payment_Group_ID));
}
/** Get Payment Group.
@return Payment Group */
public int getP_Payment_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}
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
/** Set Period.
@param P_Period_ID Period of the Calendar  */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID < 1) throw new IllegalArgumentException ("P_Period_ID is mandatory.");
set_Value ("P_Period_ID", new Integer(P_Period_ID));
}
/** Get Period.
@return Period of the Calendar  */
public int getP_Period_ID() 
{
Integer ii = (Integer)get_Value("P_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_T_Conciliation_ID.
@param P_T_Conciliation_ID P_T_Conciliation_ID */
public void setP_T_Conciliation_ID (int P_T_Conciliation_ID)
{
if (P_T_Conciliation_ID < 1) throw new IllegalArgumentException ("P_T_Conciliation_ID is mandatory.");
set_ValueNoCheck ("P_T_Conciliation_ID", new Integer(P_T_Conciliation_ID));
}
/** Get P_T_Conciliation_ID.
@return P_T_Conciliation_ID */
public int getP_T_Conciliation_ID() 
{
Integer ii = (Integer)get_Value("P_T_Conciliation_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Payment date.
@param PayDate Date Payment made */
public void setPayDate (Timestamp PayDate)
{
set_Value ("PayDate", PayDate);
}
/** Get Payment date.
@return Date Payment made */
public Timestamp getPayDate() 
{
return (Timestamp)get_Value("PayDate");
}
/** Set Preprinted no.
@param PrePrintedNo Preprinted no */
public void setPrePrintedNo (String PrePrintedNo)
{
if (PrePrintedNo != null && PrePrintedNo.length() > 60)
{
log.warning("Length > 60 - truncated");
PrePrintedNo = PrePrintedNo.substring(0,59);
}
set_Value ("PrePrintedNo", PrePrintedNo);
}
/** Get Preprinted no.
@return Preprinted no */
public String getPrePrintedNo() 
{
return (String)get_Value("PrePrintedNo");
}
}
