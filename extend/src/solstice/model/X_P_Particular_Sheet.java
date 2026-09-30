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
/** Generated Model for P_Particular_Sheet
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Particular_Sheet extends PO
{
/** Standard Constructor
@param ctx context
@param P_Particular_Sheet_ID id
@param trxName transaction
*/
public X_P_Particular_Sheet (Properties ctx, int P_Particular_Sheet_ID, String trxName)
{
super (ctx, P_Particular_Sheet_ID, trxName);
/** if (P_Particular_Sheet_ID == 0)
{
setEmployee (null);	// @P_Employee_ID@
setGain (null);
setP_Particular_Sheet_ID (0);
setP_Schedule_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Particular_Sheet (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100729 */
public static final int Table_ID=2100729;

/** TableName=P_Particular_Sheet */
public static final String Table_Name="P_Particular_Sheet";

protected static KeyNamePair Model = new KeyNamePair(2100729,"P_Particular_Sheet");

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
StringBuffer sb = new StringBuffer ("X_P_Particular_Sheet[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Assignment.
@param Assignment Assignment */
public void setAssignment (String Assignment)
{
if (Assignment != null && Assignment.length() > 95)
{
log.warning("Length > 95 - truncated");
Assignment = Assignment.substring(0,94);
}
set_Value ("Assignment", Assignment);
}
/** Get Assignment.
@return Assignment */
public String getAssignment() 
{
return (String)get_Value("Assignment");
}
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
/** Set Day.
@param Day Day */
public void setDay (Timestamp Day)
{
set_Value ("Day", Day);
}
/** Get Day.
@return Day */
public Timestamp getDay() 
{
return (Timestamp)get_Value("Day");
}
/** Set Day.
@param Day_Character Day */
public void setDay_Character (String Day_Character)
{
if (Day_Character != null && Day_Character.length() > 10)
{
log.warning("Length > 10 - truncated");
Day_Character = Day_Character.substring(0,9);
}
set_Value ("Day_Character", Day_Character);
}
/** Get Day.
@return Day */
public String getDay_Character() 
{
return (String)get_Value("Day_Character");
}
/** Set Quantity.
@param DayQty Quantity */
public void setDayQty (BigDecimal DayQty)
{
set_Value ("DayQty", DayQty);
}
/** Get Quantity.
@return Quantity */
public BigDecimal getDayQty() 
{
BigDecimal bd = (BigDecimal)get_Value("DayQty");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employee.
@param Employee Employee */
public void setEmployee (String Employee)
{
if (Employee == null) throw new IllegalArgumentException ("Employee is mandatory.");
if (Employee.length() > 65)
{
log.warning("Length > 65 - truncated");
Employee = Employee.substring(0,64);
}
set_Value ("Employee", Employee);
}
/** Get Employee.
@return Employee */
public String getEmployee() 
{
return (String)get_Value("Employee");
}
/** Set Gain.
@param Gain Gain */
public void setGain (String Gain)
{
if (Gain == null) throw new IllegalArgumentException ("Gain is mandatory.");
if (Gain.length() > 95)
{
log.warning("Length > 95 - truncated");
Gain = Gain.substring(0,94);
}
set_Value ("Gain", Gain);
}
/** Get Gain.
@return Gain */
public String getGain() 
{
return (String)get_Value("Gain");
}
/** Set Hourly Rate.
@param Hourly_Rate   */
public void setHourly_Rate (BigDecimal Hourly_Rate)
{
set_Value ("Hourly_Rate", Hourly_Rate);
}
/** Get Hourly Rate.
@return   */
public BigDecimal getHourly_Rate() 
{
BigDecimal bd = (BigDecimal)get_Value("Hourly_Rate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set origine.
@param Origine origine */
public void setOrigine (String Origine)
{
if (Origine != null && Origine.length() > 30)
{
log.warning("Length > 30 - truncated");
Origine = Origine.substring(0,29);
}
set_Value ("Origine", Origine);
}
/** Get origine.
@return origine */
public String getOrigine() 
{
return (String)get_Value("Origine");
}
/** Set P_Absence_Detail_ID.
@param P_Absence_Detail_ID P_Absence_Detail_ID */
public void setP_Absence_Detail_ID (int P_Absence_Detail_ID)
{
if (P_Absence_Detail_ID <= 0) set_Value ("P_Absence_Detail_ID", null);
 else 
set_Value ("P_Absence_Detail_ID", new Integer(P_Absence_Detail_ID));
}
/** Get P_Absence_Detail_ID.
@return P_Absence_Detail_ID */
public int getP_Absence_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Absence_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Assignment_ID AD_Reference_ID=2000057 */
public static final int P_ASSIGNMENT_ID_AD_Reference_ID=2000057;
/** Set Assignment.
@param P_Assignment_ID Assignment */
public void setP_Assignment_ID (int P_Assignment_ID)
{
if (P_Assignment_ID <= 0) set_Value ("P_Assignment_ID", null);
 else 
set_Value ("P_Assignment_ID", new Integer(P_Assignment_ID));
}
/** Get Assignment.
@return Assignment */
public int getP_Assignment_ID() 
{
Integer ii = (Integer)get_Value("P_Assignment_ID");
if (ii == null) return 0;
return ii.intValue();
}
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

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
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

/** P_Gain_ID AD_Reference_ID=2100317 */
public static final int P_GAIN_ID_AD_Reference_ID=2100317;
/** Set Gain.
@param P_Gain_ID Gain */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID <= 0) set_Value ("P_Gain_ID", null);
 else 
set_Value ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Gain.
@return Gain */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Particular Sheet.
@param P_Particular_Sheet_ID Particular Sheet */
public void setP_Particular_Sheet_ID (int P_Particular_Sheet_ID)
{
if (P_Particular_Sheet_ID < 1) throw new IllegalArgumentException ("P_Particular_Sheet_ID is mandatory.");
set_ValueNoCheck ("P_Particular_Sheet_ID", new Integer(P_Particular_Sheet_ID));
}
/** Get Particular Sheet.
@return Particular Sheet */
public int getP_Particular_Sheet_ID() 
{
Integer ii = (Integer)get_Value("P_Particular_Sheet_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Period_ID AD_Reference_ID=2000009 */
public static final int P_PERIOD_ID_AD_Reference_ID=2000009;
/** Set Period.
@param P_Period_ID Period of the Calendar  */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID <= 0) set_Value ("P_Period_ID", null);
 else 
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

/** P_Schedule_ID AD_Reference_ID=2000064 */
public static final int P_SCHEDULE_ID_AD_Reference_ID=2000064;
/** Set Schedule.
@param P_Schedule_ID Répartition des heures de travail à l'intérieur d'une période donnée.  */
public void setP_Schedule_ID (int P_Schedule_ID)
{
if (P_Schedule_ID < 1) throw new IllegalArgumentException ("P_Schedule_ID is mandatory.");
set_Value ("P_Schedule_ID", new Integer(P_Schedule_ID));
}
/** Get Schedule.
@return Répartition des heures de travail à l'intérieur d'une période donnée.  */
public int getP_Schedule_ID() 
{
Integer ii = (Integer)get_Value("P_Schedule_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Period.
@param Period Period */
public void setPeriod (String Period)
{
if (Period != null && Period.length() > 60)
{
log.warning("Length > 60 - truncated");
Period = Period.substring(0,59);
}
set_Value ("Period", Period);
}
/** Get Period.
@return Period */
public String getPeriod() 
{
return (String)get_Value("Period");
}
}
