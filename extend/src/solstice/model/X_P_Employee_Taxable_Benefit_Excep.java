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
/** Generated Model for P_Employee_Taxable_Benefit_Excep
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Taxable_Benefit_Excep extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Taxable_Benefit_Excep_ID id
@param trxName transaction
*/
public X_P_Employee_Taxable_Benefit_Excep (Properties ctx, int P_Employee_Taxable_Benefit_Excep_ID, String trxName)
{
super (ctx, P_Employee_Taxable_Benefit_Excep_ID, trxName);
/** if (P_Employee_Taxable_Benefit_Excep_ID == 0)
{
setP_Period_ID (0);	// @P_Period_ID@
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Taxable_Benefit_Excep (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100880 */
public static final int Table_ID=2100880;

/** TableName=P_Employee_Taxable_Benefit_Excep */
public static final String Table_Name="P_Employee_Taxable_Benefit_Excep";

protected static KeyNamePair Model = new KeyNamePair(2100880,"P_Employee_Taxable_Benefit_Excep");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Taxable_Benefit_Excep[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Amount.
@param ExceptionAmount Amount */
public void setExceptionAmount (BigDecimal ExceptionAmount)
{
set_Value ("ExceptionAmount", ExceptionAmount);
}
/** Get Amount.
@return Amount */
public BigDecimal getExceptionAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("ExceptionAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Amount Additional.
@param ExceptionAmountAdd Amount Additional */
public void setExceptionAmountAdd (BigDecimal ExceptionAmountAdd)
{
set_Value ("ExceptionAmountAdd", ExceptionAmountAdd);
}
/** Get Amount Additional.
@return Amount Additional */
public BigDecimal getExceptionAmountAdd() 
{
BigDecimal bd = (BigDecimal)get_Value("ExceptionAmountAdd");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set P_Employee_Taxable_Benefit_Excep_ID.
@param P_Employee_Taxable_Benefit_Excep_ID P_Employee_Taxable_Benefit_Excep_ID */
public void setP_Employee_Taxable_Benefit_Excep_ID (int P_Employee_Taxable_Benefit_Excep_ID)
{
if (P_Employee_Taxable_Benefit_Excep_ID <= 0) set_ValueNoCheck ("P_Employee_Taxable_Benefit_Excep_ID", null);
 else 
set_ValueNoCheck ("P_Employee_Taxable_Benefit_Excep_ID", new Integer(P_Employee_Taxable_Benefit_Excep_ID));
}
/** Get P_Employee_Taxable_Benefit_Excep_ID.
@return P_Employee_Taxable_Benefit_Excep_ID */
public int getP_Employee_Taxable_Benefit_Excep_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Taxable_Benefit_Excep_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Taxable Benefit.
@param P_Employee_Taxable_Benefit_ID Taxable Benefit */
public void setP_Employee_Taxable_Benefit_ID (int P_Employee_Taxable_Benefit_ID)
{
if (P_Employee_Taxable_Benefit_ID <= 0) set_Value ("P_Employee_Taxable_Benefit_ID", null);
 else 
set_Value ("P_Employee_Taxable_Benefit_ID", new Integer(P_Employee_Taxable_Benefit_ID));
}
/** Get Taxable Benefit.
@return Taxable Benefit */
public int getP_Employee_Taxable_Benefit_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Taxable_Benefit_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Period_ID AD_Reference_ID=2000079 */
public static final int P_PERIOD_ID_AD_Reference_ID=2000079;
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
}
