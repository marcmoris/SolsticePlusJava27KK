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
/** Generated Model for P_Employee_Credits
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Credits extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Credits_ID id
@param trxName transaction
*/
public X_P_Employee_Credits (Properties ctx, int P_Employee_Credits_ID, String trxName)
{
super (ctx, P_Employee_Credits_ID, trxName);
/** if (P_Employee_Credits_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));	// @SQL=SELECT StartDate AS DefaultValue FROM P_Period WHERE PeriodStatus='O' 
setP_Credits_ID (0);
setP_Employee_Credits_ID (0);
setP_Employee_ID (0);
setP_Method_Credits_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Credits (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000080 */
public static final int Table_ID=2000080;

/** TableName=P_Employee_Credits */
public static final String Table_Name="P_Employee_Credits";

protected static KeyNamePair Model = new KeyNamePair(2000080,"P_Employee_Credits");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Credits[").append(get_ID()).append("]");
return sb.toString();
}

/** DisplayUOM AD_Reference_ID=2100452 */
public static final int DISPLAYUOM_AD_Reference_ID=2100452;
/** Month = Mo */
public static final String DISPLAYUOM_Month = "Mo";
/** Amounts = A */
public static final String DISPLAYUOM_Amounts = "A";
/** Days = J */
public static final String DISPLAYUOM_Days = "J";
/** Year / Days = A/J */
public static final String DISPLAYUOM_YearDays = "A/J";
/** Hours = H */
public static final String DISPLAYUOM_Hours = "H";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isDisplayUOMValid (String test)
{
return test == null || test.equals("Mo") || test.equals("A") || test.equals("J") || test.equals("A/J") || test.equals("H");
}
/** Set Display UOM.
@param DisplayUOM Display UOM */
public void setDisplayUOM (String DisplayUOM)
{
if (!isDisplayUOMValid(DisplayUOM))
throw new IllegalArgumentException ("DisplayUOM Invalid value - " + DisplayUOM + " - Reference_ID=2100452 - Mo - A - J - A/J - H");
throw new IllegalArgumentException ("DisplayUOM is virtual column");
}
/** Get Display UOM.
@return Display UOM */
public String getDisplayUOM() 
{
return (String)get_Value("DisplayUOM");
}
/** Set Effect In.
@param EffectIn Date when information in effect  */
public void setEffectIn (Timestamp EffectIn)
{
if (EffectIn == null) throw new IllegalArgumentException ("EffectIn is mandatory.");
set_Value ("EffectIn", EffectIn);
}
/** Get Effect In.
@return Date when information in effect  */
public Timestamp getEffectIn() 
{
return (Timestamp)get_Value("EffectIn");
}
/** Set To.
@param EffectTo To */
public void setEffectTo (Timestamp EffectTo)
{
set_Value ("EffectTo", EffectTo);
}
/** Get To.
@return To */
public Timestamp getEffectTo() 
{
return (Timestamp)get_Value("EffectTo");
}

/** P_Credits_ID AD_Reference_ID=2100318 */
public static final int P_CREDITS_ID_AD_Reference_ID=2100318;
/** Set Credits.
@param P_Credits_ID Credits */
public void setP_Credits_ID (int P_Credits_ID)
{
if (P_Credits_ID < 1) throw new IllegalArgumentException ("P_Credits_ID is mandatory.");
set_ValueNoCheck ("P_Credits_ID", new Integer(P_Credits_ID));
}
/** Get Credits.
@return Credits */
public int getP_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Credits.
@param P_Employee_Credits_ID Credits */
public void setP_Employee_Credits_ID (int P_Employee_Credits_ID)
{
if (P_Employee_Credits_ID < 1) throw new IllegalArgumentException ("P_Employee_Credits_ID is mandatory.");
set_Value ("P_Employee_Credits_ID", new Integer(P_Employee_Credits_ID));
}
/** Get Credits.
@return Credits */
public int getP_Employee_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Credits_ID");
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
set_ValueNoCheck ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee .
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Method_Credits_ID AD_Reference_ID=2100392 */
public static final int P_METHOD_CREDITS_ID_AD_Reference_ID=2100392;
/** Set Method.
@param P_Method_Credits_ID Method */
public void setP_Method_Credits_ID (int P_Method_Credits_ID)
{
if (P_Method_Credits_ID < 1) throw new IllegalArgumentException ("P_Method_Credits_ID is mandatory.");
set_Value ("P_Method_Credits_ID", new Integer(P_Method_Credits_ID));
}
/** Get Method.
@return Method */
public int getP_Method_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Method_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Total.
@param TotalCredits Total */
public void setTotalCredits (BigDecimal TotalCredits)
{
throw new IllegalArgumentException ("TotalCredits is virtual column");
}
/** Get Total.
@return Total */
public BigDecimal getTotalCredits() 
{
BigDecimal bd = (BigDecimal)get_Value("TotalCredits");
if (bd == null) return Env.ZERO;
return bd;
}
}
