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
/** Generated Model for P_Salary_Scale_Detail
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Salary_Scale_Detail extends PO
{
/** Standard Constructor
@param ctx context
@param P_Salary_Scale_Detail_ID id
@param trxName transaction
*/
public X_P_Salary_Scale_Detail (Properties ctx, int P_Salary_Scale_Detail_ID, String trxName)
{
super (ctx, P_Salary_Scale_Detail_ID, trxName);
/** if (P_Salary_Scale_Detail_ID == 0)
{
setP_Salary_Scale_Detail_ID (0);
setP_Salary_Scale_ID (0);
setStep (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Salary_Scale_Detail (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000029 */
public static final int Table_ID=2000029;

/** TableName=P_Salary_Scale_Detail */
public static final String Table_Name="P_Salary_Scale_Detail";

protected static KeyNamePair Model = new KeyNamePair(2000029,"P_Salary_Scale_Detail");

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
StringBuffer sb = new StringBuffer ("X_P_Salary_Scale_Detail[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Annual Salary.
@param Annual_Salary   */
public void setAnnual_Salary (BigDecimal Annual_Salary)
{
set_Value ("Annual_Salary", Annual_Salary);
}
/** Get Annual Salary.
@return   */
public BigDecimal getAnnual_Salary() 
{
BigDecimal bd = (BigDecimal)get_Value("Annual_Salary");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set Limit Max.
@param Limit_Max Maximum  */
public void setLimit_Max (BigDecimal Limit_Max)
{
set_Value ("Limit_Max", Limit_Max);
}
/** Get Limit Max.
@return Maximum  */
public BigDecimal getLimit_Max() 
{
BigDecimal bd = (BigDecimal)get_Value("Limit_Max");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Limit Min.
@param Limit_Min Minimum  */
public void setLimit_Min (BigDecimal Limit_Min)
{
set_Value ("Limit_Min", Limit_Min);
}
/** Get Limit Min.
@return Minimum  */
public BigDecimal getLimit_Min() 
{
BigDecimal bd = (BigDecimal)get_Value("Limit_Min");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Collective Labour Agreement.
@param P_Collective_Labour_Agr_ID Collective Labour Agreement */
public void setP_Collective_Labour_Agr_ID (String P_Collective_Labour_Agr_ID)
{
throw new IllegalArgumentException ("P_Collective_Labour_Agr_ID is virtual column");
}
/** Get Collective Labour Agreement.
@return Collective Labour Agreement */
public String getP_Collective_Labour_Agr_ID() 
{
return (String)get_Value("P_Collective_Labour_Agr_ID");
}

/** P_Job_Title_ID AD_Reference_ID=2000025 */
public static final int P_JOB_TITLE_ID_AD_Reference_ID=2000025;
/** Set Job Title.
@param P_Job_Title_ID   */
public void setP_Job_Title_ID (int P_Job_Title_ID)
{
throw new IllegalArgumentException ("P_Job_Title_ID is virtual column");
}
/** Get Job Title.
@return   */
public int getP_Job_Title_ID() 
{
Integer ii = (Integer)get_Value("P_Job_Title_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Salary_Class_ID AD_Reference_ID=2100354 */
public static final int P_SALARY_CLASS_ID_AD_Reference_ID=2100354;
/** Set Salary Class.
@param P_Salary_Class_ID Salary Class */
public void setP_Salary_Class_ID (int P_Salary_Class_ID)
{
throw new IllegalArgumentException ("P_Salary_Class_ID is virtual column");
}
/** Get Salary Class.
@return Salary Class */
public int getP_Salary_Class_ID() 
{
Integer ii = (Integer)get_Value("P_Salary_Class_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Step.
@param P_Salary_Scale_Detail_ID   */
public void setP_Salary_Scale_Detail_ID (int P_Salary_Scale_Detail_ID)
{
if (P_Salary_Scale_Detail_ID < 1) throw new IllegalArgumentException ("P_Salary_Scale_Detail_ID is mandatory.");
set_Value ("P_Salary_Scale_Detail_ID", new Integer(P_Salary_Scale_Detail_ID));
}
/** Get Step.
@return   */
public int getP_Salary_Scale_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Salary_Scale_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Salary Scale.
@param P_Salary_Scale_ID   */
public void setP_Salary_Scale_ID (int P_Salary_Scale_ID)
{
if (P_Salary_Scale_ID < 1) throw new IllegalArgumentException ("P_Salary_Scale_ID is mandatory.");
set_Value ("P_Salary_Scale_ID", new Integer(P_Salary_Scale_ID));
}
/** Get Salary Scale.
@return   */
public int getP_Salary_Scale_ID() 
{
Integer ii = (Integer)get_Value("P_Salary_Scale_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Salary Limit Max.
@param Salary_Limit_Max Salary Limit Max */
public void setSalary_Limit_Max (BigDecimal Salary_Limit_Max)
{
set_Value ("Salary_Limit_Max", Salary_Limit_Max);
}
/** Get Salary Limit Max.
@return Salary Limit Max */
public BigDecimal getSalary_Limit_Max() 
{
BigDecimal bd = (BigDecimal)get_Value("Salary_Limit_Max");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Salary Limit Min.
@param Salary_Limit_Min Salary Limit Min */
public void setSalary_Limit_Min (BigDecimal Salary_Limit_Min)
{
set_Value ("Salary_Limit_Min", Salary_Limit_Min);
}
/** Get Salary Limit Min.
@return Salary Limit Min */
public BigDecimal getSalary_Limit_Min() 
{
BigDecimal bd = (BigDecimal)get_Value("Salary_Limit_Min");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Mid.
@param Salary_Mid Mid */
public void setSalary_Mid (BigDecimal Salary_Mid)
{
throw new IllegalArgumentException ("Salary_Mid is virtual column");
}
/** Get Mid.
@return Mid */
public BigDecimal getSalary_Mid() 
{
BigDecimal bd = (BigDecimal)get_Value("Salary_Mid");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Step.
@param Step   */
public void setStep (String Step)
{
if (Step == null) throw new IllegalArgumentException ("Step is mandatory.");
if (Step.length() > 30)
{
log.warning("Length > 30 - truncated");
Step = Step.substring(0,29);
}
set_Value ("Step", Step);
}
/** Get Step.
@return   */
public String getStep() 
{
return (String)get_Value("Step");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getStep());
}
}
