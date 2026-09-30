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
/** Generated Model for P_Payment_Group
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.1 2007/07/18 14:43:46 marmor01 Exp $ */
public class X_P_Payment_Group extends PO
{
/** Standard Constructor
@param ctx context
@param P_Payment_Group_ID id
@param trxName transaction
*/
public X_P_Payment_Group (Properties ctx, int P_Payment_Group_ID, String trxName)
{
super (ctx, P_Payment_Group_ID, trxName);
/** if (P_Payment_Group_ID == 0)
{
setName (null);
setP_Bank_ID (0);
setP_Frequency_ID (0);
setP_Payment_Group_ID (0);
setTimeSheetModel (null);	// 0
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Payment_Group (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000034 */
public static final int Table_ID=2000034;

/** TableName=P_Payment_Group */
public static final String Table_Name="P_Payment_Group";

protected static KeyNamePair Model = new KeyNamePair(2000034,"P_Payment_Group");

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
StringBuffer sb = new StringBuffer ("X_P_Payment_Group[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Annual Increase .
@param Annual_Increase Annual Increase  */
public void setAnnual_Increase (BigDecimal Annual_Increase)
{
set_Value ("Annual_Increase", Annual_Increase);
}
/** Get Annual Increase .
@return Annual Increase  */
public BigDecimal getAnnual_Increase() 
{
BigDecimal bd = (BigDecimal)get_Value("Annual_Increase");
if (bd == null) return Env.ZERO;
return bd;
}

/** C_Activity_ID AD_Reference_ID=142 */
public static final int C_ACTIVITY_ID_AD_Reference_ID=142;
/** Set Function.
@param C_Activity_ID Function */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
 else 
set_Value ("C_Activity_ID", new Integer(C_Activity_ID));
}
/** Get Function.
@return Function */
public int getC_Activity_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_SalesRegion_ID AD_Reference_ID=144 */
public static final int C_SALESREGION_ID_AD_Reference_ID=144;
/** Set Base.
@param C_SalesRegion_ID Base */
public void setC_SalesRegion_ID (int C_SalesRegion_ID)
{
if (C_SalesRegion_ID <= 0) set_Value ("C_SalesRegion_ID", null);
 else 
set_Value ("C_SalesRegion_ID", new Integer(C_SalesRegion_ID));
}
/** Get Base.
@return Base */
public int getC_SalesRegion_ID() 
{
Integer ii = (Integer)get_Value("C_SalesRegion_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Description = Description.substring(0,1999);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 240)
{
log.warning("Length > 240 - truncated");
Name = Name.substring(0,239);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Set Bank.
@param P_Bank_ID   */
public void setP_Bank_ID (int P_Bank_ID)
{
if (P_Bank_ID < 1) throw new IllegalArgumentException ("P_Bank_ID is mandatory.");
set_Value ("P_Bank_ID", new Integer(P_Bank_ID));
}
/** Get Bank.
@return   */
public int getP_Bank_ID() 
{
Integer ii = (Integer)get_Value("P_Bank_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Calendar.
@param P_Calendar_ID Payroll Calendar Name  */
public void setP_Calendar_ID (int P_Calendar_ID)
{
if (P_Calendar_ID <= 0) set_Value ("P_Calendar_ID", null);
 else 
set_Value ("P_Calendar_ID", new Integer(P_Calendar_ID));
}
/** Get Calendar.
@return Payroll Calendar Name  */
public int getP_Calendar_ID() 
{
Integer ii = (Integer)get_Value("P_Calendar_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Frequency_ID AD_Reference_ID=2100457 */
public static final int P_FREQUENCY_ID_AD_Reference_ID=2100457;
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

/** TimeSheetModel AD_Reference_ID=2100459 */
public static final int TIMESHEETMODEL_AD_Reference_ID=2100459;
/** Detail = 0 */
public static final String TIMESHEETMODEL_Detail = "0";
/** Summary = 1 */
public static final String TIMESHEETMODEL_Summary = "1";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isTimeSheetModelValid (String test)
{
return test.equals("0") || test.equals("1");
}
/** Set TimeSheet Model.
@param TimeSheetModel Display the timesheet in mode summary or detail */
public void setTimeSheetModel (String TimeSheetModel)
{
if (TimeSheetModel == null) throw new IllegalArgumentException ("TimeSheetModel is mandatory");
if (!isTimeSheetModelValid(TimeSheetModel))
throw new IllegalArgumentException ("TimeSheetModel Invalid value - " + TimeSheetModel + " - Reference_ID=2100459 - 0 - 1");
if (TimeSheetModel.length() > 1)
{
log.warning("Length > 1 - truncated");
TimeSheetModel = TimeSheetModel.substring(0,0);
}
set_Value ("TimeSheetModel", TimeSheetModel);
}
/** Get TimeSheet Model.
@return Display the timesheet in mode summary or detail */
public String getTimeSheetModel() 
{
return (String)get_Value("TimeSheetModel");
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value != null && Value.length() > 30)
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
