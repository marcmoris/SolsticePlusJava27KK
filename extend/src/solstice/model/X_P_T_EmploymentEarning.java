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
/** Generated Model for P_T_EmploymentEarning
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_T_EmploymentEarning extends PO
{
/** Standard Constructor
@param ctx context
@param P_T_EmploymentEarning_ID id
@param trxName transaction
*/
public X_P_T_EmploymentEarning (Properties ctx, int P_T_EmploymentEarning_ID, String trxName)
{
super (ctx, P_T_EmploymentEarning_ID, trxName);
/** if (P_T_EmploymentEarning_ID == 0)
{
setP_T_EMPLOYMENTEARNING_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_T_EmploymentEarning (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100818 */
public static final int Table_ID=2100818;

/** TableName=P_T_EmploymentEarning */
public static final String Table_Name="P_T_EmploymentEarning";

protected static KeyNamePair Model = new KeyNamePair(2100818,"P_T_EmploymentEarning");

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
StringBuffer sb = new StringBuffer ("X_P_T_EmploymentEarning[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Process Instance.
@param AD_PInstance_ID Instance of the process */
public void setAD_PInstance_ID (int AD_PInstance_ID)
{
if (AD_PInstance_ID <= 0) set_Value ("AD_PInstance_ID", null);
 else 
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
/** Set Amount1.
@param Amount1 Amount1 */
public void setAmount1 (BigDecimal Amount1)
{
set_Value ("Amount1", Amount1);
}
/** Get Amount1.
@return Amount1 */
public BigDecimal getAmount1() 
{
BigDecimal bd = (BigDecimal)get_Value("Amount1");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Amount2.
@param Amount2 Amount2 */
public void setAmount2 (BigDecimal Amount2)
{
set_Value ("Amount2", Amount2);
}
/** Get Amount2.
@return Amount2 */
public BigDecimal getAmount2() 
{
BigDecimal bd = (BigDecimal)get_Value("Amount2");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Amount3.
@param Amount3 Amount3 */
public void setAmount3 (BigDecimal Amount3)
{
set_Value ("Amount3", Amount3);
}
/** Get Amount3.
@return Amount3 */
public BigDecimal getAmount3() 
{
BigDecimal bd = (BigDecimal)get_Value("Amount3");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Amount4.
@param Amount4 Amount4 */
public void setAmount4 (BigDecimal Amount4)
{
set_Value ("Amount4", Amount4);
}
/** Get Amount4.
@return Amount4 */
public BigDecimal getAmount4() 
{
BigDecimal bd = (BigDecimal)get_Value("Amount4");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Amount5.
@param Amount5 Amount5 */
public void setAmount5 (BigDecimal Amount5)
{
set_Value ("Amount5", Amount5);
}
/** Get Amount5.
@return Amount5 */
public BigDecimal getAmount5() 
{
BigDecimal bd = (BigDecimal)get_Value("Amount5");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Establishment.
@param Establishment Establishment */
public void setEstablishment (String Establishment)
{
if (Establishment != null && Establishment.length() > 30)
{
log.warning("Length > 30 - truncated");
Establishment = Establishment.substring(0,29);
}
set_Value ("Establishment", Establishment);
}
/** Get Establishment.
@return Establishment */
public String getEstablishment() 
{
return (String)get_Value("Establishment");
}
/** Set INSURABLEWAGE.
@param INSURABLEWAGE INSURABLEWAGE */
public void setINSURABLEWAGE (BigDecimal INSURABLEWAGE)
{
set_Value ("INSURABLEWAGE", INSURABLEWAGE);
}
/** Get INSURABLEWAGE.
@return INSURABLEWAGE */
public BigDecimal getINSURABLEWAGE() 
{
BigDecimal bd = (BigDecimal)get_Value("INSURABLEWAGE");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set INSURABLEWAGECUMUL.
@param INSURABLEWAGECUMUL INSURABLEWAGECUMUL */
public void setINSURABLEWAGECUMUL (BigDecimal INSURABLEWAGECUMUL)
{
set_Value ("INSURABLEWAGECUMUL", INSURABLEWAGECUMUL);
}
/** Get INSURABLEWAGECUMUL.
@return INSURABLEWAGECUMUL */
public BigDecimal getINSURABLEWAGECUMUL() 
{
BigDecimal bd = (BigDecimal)get_Value("INSURABLEWAGECUMUL");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set INSURABLEWAGECUMUL2.
@param INSURABLEWAGECUMUL2 INSURABLEWAGECUMUL2 */
public void setINSURABLEWAGECUMUL2 (BigDecimal INSURABLEWAGECUMUL2)
{
set_Value ("INSURABLEWAGECUMUL2", INSURABLEWAGECUMUL2);
}
/** Get INSURABLEWAGECUMUL2.
@return INSURABLEWAGECUMUL2 */
public BigDecimal getINSURABLEWAGECUMUL2() 
{
BigDecimal bd = (BigDecimal)get_Value("INSURABLEWAGECUMUL2");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MAXIMUMEARNING.
@param MAXIMUMEARNING MAXIMUMEARNING */
public void setMAXIMUMEARNING (BigDecimal MAXIMUMEARNING)
{
set_Value ("MAXIMUMEARNING", MAXIMUMEARNING);
}
/** Get MAXIMUMEARNING.
@return MAXIMUMEARNING */
public BigDecimal getMAXIMUMEARNING() 
{
BigDecimal bd = (BigDecimal)get_Value("MAXIMUMEARNING");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set P_T_EMPLOYMENTEARNING_ID.
@param P_T_EMPLOYMENTEARNING_ID P_T_EMPLOYMENTEARNING_ID */
public void setP_T_EMPLOYMENTEARNING_ID (int P_T_EMPLOYMENTEARNING_ID)
{
if (P_T_EMPLOYMENTEARNING_ID < 1) throw new IllegalArgumentException ("P_T_EMPLOYMENTEARNING_ID is mandatory.");
set_ValueNoCheck ("P_T_EMPLOYMENTEARNING_ID", new Integer(P_T_EMPLOYMENTEARNING_ID));
}
/** Get P_T_EMPLOYMENTEARNING_ID.
@return P_T_EMPLOYMENTEARNING_ID */
public int getP_T_EMPLOYMENTEARNING_ID() 
{
Integer ii = (Integer)get_Value("P_T_EMPLOYMENTEARNING_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set QUANTITY1.
@param QUANTITY1 QUANTITY1 */
public void setQUANTITY1 (BigDecimal QUANTITY1)
{
set_Value ("QUANTITY1", QUANTITY1);
}
/** Get QUANTITY1.
@return QUANTITY1 */
public BigDecimal getQUANTITY1() 
{
BigDecimal bd = (BigDecimal)get_Value("QUANTITY1");
if (bd == null) return Env.ZERO;
return bd;
}
}
