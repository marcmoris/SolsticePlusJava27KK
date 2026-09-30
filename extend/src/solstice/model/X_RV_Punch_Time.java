/******************************************************************************
* Product: Solstice+ Payroll & Human Resources Management                    *
* Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
* This program is free software, you can redistribute it and/or modify it    *
* under the terms version 2 of the GNU General Public License as published   *
* by the Free Software Foundation. This program is distributed in the hope   *
* that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
* warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
* See the GNU General Public License for more details.                       *
* You should have received a copy of the GNU General Public License along    *
* with this program, if not, write to the Free Software Foundation, Inc.,    *
* 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
* For the text or an alternative of this public license, you may reach us    *
* ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
* or via info@progestion.net or http://www.progestion.net/license.html       *
******************************************************************************/
package solstice.model;

/** Generated Model - DO NOT CHANGE */
import org.compiere.model.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for RV_Punch_Time
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_RV_Punch_Time extends PO
{
/** Standard Constructor
@param ctx context
@param RV_Punch_Time_ID id
@param trxName transaction
*/
public X_RV_Punch_Time (Properties ctx, int RV_Punch_Time_ID, String trxName)
{
super (ctx, RV_Punch_Time_ID, trxName);
/** if (RV_Punch_Time_ID == 0)
{
setcodeFonction (null);
setDay (new Timestamp(System.currentTimeMillis()));
setDayQty (Env.ZERO);
setEndTime (null);
setName (null);
setnumEmp (null);
setP_Employee_ID (0);
setP_Gain_ID (0);
setP_Period_ID (0);
setStartTime (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_RV_Punch_Time (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201177 */
public static final int Table_ID=3201177;

/** TableName=RV_Punch_Time */
public static final String Table_Name="RV_Punch_Time";

protected static KeyNamePair Model = new KeyNamePair(3201177,"RV_Punch_Time");

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
StringBuffer sb = new StringBuffer ("X_RV_Punch_Time[").append(get_ID()).append("]");
return sb.toString();
}
/** Set codeFonction.
@param codeFonction codeFonction */
public void setcodeFonction (String codeFonction)
{
if (codeFonction == null) throw new IllegalArgumentException ("codeFonction is mandatory.");
if (codeFonction.length() > 50)
{
log.warning("Length > 50 - truncated");
codeFonction = codeFonction.substring(0,49);
}
set_Value ("codeFonction", codeFonction);
}
/** Get codeFonction.
@return codeFonction */
public String getcodeFonction() 
{
return (String)get_Value("codeFonction");
}
/** Set Day.
@param Day Day */
public void setDay (Timestamp Day)
{
if (Day == null) throw new IllegalArgumentException ("Day is mandatory.");
set_Value ("Day", Day);
}
/** Get Day.
@return Day */
public Timestamp getDay() 
{
return (Timestamp)get_Value("Day");
}
/** Set Quantity.
@param DayQty Quantity */
public void setDayQty (BigDecimal DayQty)
{
if (DayQty == null) throw new IllegalArgumentException ("DayQty is mandatory.");
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
/** Set End Time.
@param EndTime End of the time span */
public void setEndTime (String EndTime)
{
if (EndTime == null) throw new IllegalArgumentException ("EndTime is mandatory.");
if (EndTime.length() > 50)
{
log.warning("Length > 50 - truncated");
EndTime = EndTime.substring(0,49);
}
set_Value ("EndTime", EndTime);
}
/** Get End Time.
@return End of the time span */
public String getEndTime() 
{
return (String)get_Value("EndTime");
}
/** Set HourlyRate.
@param HourlyRate HourlyRate */
public void setHourlyRate (int HourlyRate)
{
set_Value ("HourlyRate", new Integer(HourlyRate));
}
/** Get HourlyRate.
@return HourlyRate */
public int getHourlyRate() 
{
Integer ii = (Integer)get_Value("HourlyRate");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 65)
{
log.warning("Length > 65 - truncated");
Name = Name.substring(0,64);
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
/** Set numEmp.
@param numEmp numEmp */
public void setnumEmp (String numEmp)
{
if (numEmp == null) throw new IllegalArgumentException ("numEmp is mandatory.");
if (numEmp.length() > 50)
{
log.warning("Length > 50 - truncated");
numEmp = numEmp.substring(0,49);
}
set_Value ("numEmp", numEmp);
}
/** Get numEmp.
@return numEmp */
public String getnumEmp() 
{
return (String)get_Value("numEmp");
}
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
/** Set Gain.
@param P_Gain_ID Gain */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID < 1) throw new IllegalArgumentException ("P_Gain_ID is mandatory.");
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
/** Set Post.
@param P_Post_ID Post */
public void setP_Post_ID (int P_Post_ID)
{
if (P_Post_ID <= 0) set_Value ("P_Post_ID", null);
 else 
set_Value ("P_Post_ID", new Integer(P_Post_ID));
}
/** Get Post.
@return Post */
public int getP_Post_ID() 
{
Integer ii = (Integer)get_Value("P_Post_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set PAY_EQT_NO.
@param PAY_EQT_NO PAY_EQT_NO */
public void setPAY_EQT_NO (String PAY_EQT_NO)
{
if (PAY_EQT_NO != null && PAY_EQT_NO.length() > 2)
{
log.warning("Length > 2 - truncated");
PAY_EQT_NO = PAY_EQT_NO.substring(0,1);
}
set_Value ("PAY_EQT_NO", PAY_EQT_NO);
}
/** Get PAY_EQT_NO.
@return PAY_EQT_NO */
public String getPAY_EQT_NO() 
{
return (String)get_Value("PAY_EQT_NO");
}
/** Set PostName.
@param PostName PostName */
public void setPostName (String PostName)
{
if (PostName != null && PostName.length() > 60)
{
log.warning("Length > 60 - truncated");
PostName = PostName.substring(0,59);
}
set_Value ("PostName", PostName);
}
/** Get PostName.
@return PostName */
public String getPostName() 
{
return (String)get_Value("PostName");
}
/** Set Start Time.
@param StartTime Time started */
public void setStartTime (String StartTime)
{
if (StartTime == null) throw new IllegalArgumentException ("StartTime is mandatory.");
if (StartTime.length() > 50)
{
log.warning("Length > 50 - truncated");
StartTime = StartTime.substring(0,49);
}
set_Value ("StartTime", StartTime);
}
/** Get Start Time.
@return Time started */
public String getStartTime() 
{
return (String)get_Value("StartTime");
}
}
