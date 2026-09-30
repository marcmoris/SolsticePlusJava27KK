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
/** Generated Model for P_Period
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Period extends PO
{
/** Standard Constructor
@param ctx context
@param P_Period_ID id
@param trxName transaction
*/
public X_P_Period (Properties ctx, int P_Period_ID, String trxName)
{
super (ctx, P_Period_ID, trxName);
/** if (P_Period_ID == 0)
{
setEndDate (new Timestamp(System.currentTimeMillis()));
setIsDefault (false);	// N
setName (null);
setP_Frequency_ID (0);
setP_Period_ID (0);
setP_Year_ID (0);
setPayDate (new Timestamp(System.currentTimeMillis()));
setPeriodNo (0);
setPeriodStatus (null);	// N
setStartDate (new Timestamp(System.currentTimeMillis()));
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Period (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000020 */
public static final int Table_ID=2000020;

/** TableName=P_Period */
public static final String Table_Name="P_Period";

protected static KeyNamePair Model = new KeyNamePair(2000020,"P_Period");

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
StringBuffer sb = new StringBuffer ("X_P_Period[").append(get_ID()).append("]");
return sb.toString();
}

/** C_Period_ID1 AD_Reference_ID=1000015 */
public static final int C_PERIOD_ID1_AD_Reference_ID=1000015;
/** Set Period.
@param C_Period_ID1 Period of the Calendar */
public void setC_Period_ID1 (int C_Period_ID1)
{
set_Value ("C_Period_ID1", new Integer(C_Period_ID1));
}
/** Get Period.
@return Period of the Calendar */
public int getC_Period_ID1() 
{
Integer ii = (Integer)get_Value("C_Period_ID1");
if (ii == null) return 0;
return ii.intValue();
}

/** C_Period_ID2 AD_Reference_ID=1000015 */
public static final int C_PERIOD_ID2_AD_Reference_ID=1000015;
/** Set Period 2.
@param C_Period_ID2 Period of the Calendar */
public void setC_Period_ID2 (int C_Period_ID2)
{
set_Value ("C_Period_ID2", new Integer(C_Period_ID2));
}
/** Get Period 2.
@return Period of the Calendar */
public int getC_Period_ID2() 
{
Integer ii = (Integer)get_Value("C_Period_ID2");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Day Exceeding.
@param Day_Exceeding Number of Exceeding Day */
public void setDay_Exceeding (int Day_Exceeding)
{
set_Value ("Day_Exceeding", new Integer(Day_Exceeding));
}
/** Get Day Exceeding.
@return Number of Exceeding Day */
public int getDay_Exceeding() 
{
Integer ii = (Integer)get_Value("Day_Exceeding");
if (ii == null) return 0;
return ii.intValue();
}
/** Set End Date.
@param EndDate Last effective date (inclusive) */
public void setEndDate (Timestamp EndDate)
{
if (EndDate == null) throw new IllegalArgumentException ("EndDate is mandatory.");
set_Value ("EndDate", EndDate);
}
/** Get End Date.
@return Last effective date (inclusive) */
public Timestamp getEndDate() 
{
return (Timestamp)get_Value("EndDate");
}
/** Set Adjustment Period.
@param IsAdjustmentPeriod Adjustment Period */
public void setIsAdjustmentPeriod (boolean IsAdjustmentPeriod)
{
set_Value ("IsAdjustmentPeriod", new Boolean(IsAdjustmentPeriod));
}
/** Get Adjustment Period.
@return Adjustment Period */
public boolean isAdjustmentPeriod() 
{
Object oo = get_Value("IsAdjustmentPeriod");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Default.
@param IsDefault Default value */
public void setIsDefault (boolean IsDefault)
{
set_Value ("IsDefault", new Boolean(IsDefault));
}
/** Get Default.
@return Default value */
public boolean isDefault() 
{
Object oo = get_Value("IsDefault");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 120)
{
log.warning("Length > 120 - truncated");
Name = Name.substring(0,119);
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

/** P_Frequency_ID AD_Reference_ID=2000007 */
public static final int P_FREQUENCY_ID_AD_Reference_ID=2000007;
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
/** Set Period.
@param P_Period_ID Period of the Calendar   */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID < 1) throw new IllegalArgumentException ("P_Period_ID is mandatory.");
set_ValueNoCheck ("P_Period_ID", new Integer(P_Period_ID));
}
/** Get Period.
@return Period of the Calendar   */
public int getP_Period_ID() 
{
Integer ii = (Integer)get_Value("P_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Year_ID AD_Reference_ID=2000012 */
public static final int P_YEAR_ID_AD_Reference_ID=2000012;
/** Set Year.
@param P_Year_ID Calendar Year */
public void setP_Year_ID (int P_Year_ID)
{
if (P_Year_ID < 1) throw new IllegalArgumentException ("P_Year_ID is mandatory.");
set_ValueNoCheck ("P_Year_ID", new Integer(P_Year_ID));
}
/** Get Year.
@return Calendar Year */
public int getP_Year_ID() 
{
Integer ii = (Integer)get_Value("P_Year_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Payment date.
@param PayDate Date Payment made */
public void setPayDate (Timestamp PayDate)
{
if (PayDate == null) throw new IllegalArgumentException ("PayDate is mandatory.");
set_Value ("PayDate", PayDate);
}
/** Get Payment date.
@return Date Payment made */
public Timestamp getPayDate() 
{
return (Timestamp)get_Value("PayDate");
}
/** Set Period No.
@param PeriodNo Unique Period Number */
public void setPeriodNo (int PeriodNo)
{
set_Value ("PeriodNo", new Integer(PeriodNo));
}
/** Get Period No.
@return Unique Period Number */
public int getPeriodNo() 
{
Integer ii = (Integer)get_Value("PeriodNo");
if (ii == null) return 0;
return ii.intValue();
}

/** PeriodStatus AD_Reference_ID=2000008 */
public static final int PERIODSTATUS_AD_Reference_ID=2000008;
/** Open = O */
public static final String PERIODSTATUS_Open = "O";
/** Permanently closed = P */
public static final String PERIODSTATUS_PermanentlyClosed = "P";
/** Closed = C */
public static final String PERIODSTATUS_Closed = "C";
/** Never opened = N */
public static final String PERIODSTATUS_NeverOpened = "N";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPeriodStatusValid (String test)
{
return test.equals("O") || test.equals("P") || test.equals("C") || test.equals("N");
}
/** Set Period Status.
@param PeriodStatus Current state of this period */
public void setPeriodStatus (String PeriodStatus)
{
if (PeriodStatus == null) throw new IllegalArgumentException ("PeriodStatus is mandatory");
if (!isPeriodStatusValid(PeriodStatus))
throw new IllegalArgumentException ("PeriodStatus Invalid value - " + PeriodStatus + " - Reference_ID=2000008 - O - P - C - N");
if (PeriodStatus.length() > 1)
{
log.warning("Length > 1 - truncated");
PeriodStatus = PeriodStatus.substring(0,0);
}
set_Value ("PeriodStatus", PeriodStatus);
}
/** Get Period Status.
@return Current state of this period */
public String getPeriodStatus() 
{
return (String)get_Value("PeriodStatus");
}
/** Set Process Now.
@param Processing Process Now */
public void setProcessing (boolean Processing)
{
set_Value ("Processing", new Boolean(Processing));
}
/** Get Process Now.
@return Process Now */
public boolean isProcessing() 
{
Object oo = get_Value("Processing");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Start Date.
@param StartDate First effective day (inclusive) */
public void setStartDate (Timestamp StartDate)
{
if (StartDate == null) throw new IllegalArgumentException ("StartDate is mandatory.");
set_Value ("StartDate", StartDate);
}
/** Get Start Date.
@return First effective day (inclusive) */
public Timestamp getStartDate() 
{
return (Timestamp)get_Value("StartDate");
}

/** Set Calendar.
@param P_Calendar_ID Calendar */
public void setP_Calendar_ID (int P_Calendar_ID)
{
throw new IllegalArgumentException ("P_Calendar_ID is virtual column");
}
/** Get Calendar.
@return Calendar */
public int getP_Calendar_ID() 
{
Integer ii = (Integer)get_Value("P_Calendar_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** Set Synonym Name.
@param SynonymName The synonym for the name */
public void setSynonymName (String SynonymName)
{
throw new IllegalArgumentException ("SynonymName is virtual column");
}
/** Get Synonym Name.
@return The synonym for the name */
public String getSynonymName() 
{
return (String)get_Value("SynonymName");
}

}
