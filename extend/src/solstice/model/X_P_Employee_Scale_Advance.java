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
/** Generated Model for P_Employee_Scale_Advance
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Scale_Advance extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Scale_Advance_ID id
@param trxName transaction
*/
public X_P_Employee_Scale_Advance (Properties ctx, int P_Employee_Scale_Advance_ID, String trxName)
{
super (ctx, P_Employee_Scale_Advance_ID, trxName);
/** if (P_Employee_Scale_Advance_ID == 0)
{
setCurrentRate (Env.ZERO);
setExpectedRate (Env.ZERO);
setIsReady (false);
setNextScale_ID (0);
setP_Assignment_ID (0);
setP_Employee_ID (0);
setP_Employee_Scale_Advance_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Scale_Advance (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100775 */
public static final int Table_ID=2100775;

/** TableName=P_Employee_Scale_Advance */
public static final String Table_Name="P_Employee_Scale_Advance";

protected static KeyNamePair Model = new KeyNamePair(2100775,"P_Employee_Scale_Advance");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Scale_Advance[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Current rate.
@param CurrentRate Current rate */
public void setCurrentRate (BigDecimal CurrentRate)
{
if (CurrentRate == null) throw new IllegalArgumentException ("CurrentRate is mandatory.");
set_Value ("CurrentRate", CurrentRate);
}
/** Get Current rate.
@return Current rate */
public BigDecimal getCurrentRate() 
{
BigDecimal bd = (BigDecimal)get_Value("CurrentRate");
if (bd == null) return Env.ZERO;
return bd;
}

/** CurrentScale_ID AD_Reference_ID=2000015 */
public static final int CURRENTSCALE_ID_AD_Reference_ID=2000015;
/** Set Échelon actuel.
@param CurrentScale_ID Échelon actuel */
public void setCurrentScale_ID (int CurrentScale_ID)
{
if (CurrentScale_ID <= 0) set_Value ("CurrentScale_ID", null);
 else 
set_Value ("CurrentScale_ID", new Integer(CurrentScale_ID));
}
/** Get Échelon actuel.
@return Échelon actuel */
public int getCurrentScale_ID() 
{
Integer ii = (Integer)get_Value("CurrentScale_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Effect In.
@param EffectIn Date when information in effect  */
public void setEffectIn (Timestamp EffectIn)
{
set_Value ("EffectIn", EffectIn);
}
/** Get Effect In.
@return Date when information in effect  */
public Timestamp getEffectIn() 
{
return (Timestamp)get_Value("EffectIn");
}
/** Set Expected rate.
@param ExpectedRate Expected rate */
public void setExpectedRate (BigDecimal ExpectedRate)
{
if (ExpectedRate == null) throw new IllegalArgumentException ("ExpectedRate is mandatory.");
set_Value ("ExpectedRate", ExpectedRate);
}
/** Get Expected rate.
@return Expected rate */
public BigDecimal getExpectedRate() 
{
BigDecimal bd = (BigDecimal)get_Value("ExpectedRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Is Ready.
@param IsReady Is Ready */
public void setIsReady (boolean IsReady)
{
set_Value ("IsReady", new Boolean(IsReady));
}
/** Get Is Ready.
@return Is Ready */
public boolean isReady() 
{
Object oo = get_Value("IsReady");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Last Advance Date.
@param LastAdvanceDate Last Advance Date */
public void setLastAdvanceDate (Timestamp LastAdvanceDate)
{
set_Value ("LastAdvanceDate", LastAdvanceDate);
}
/** Get Last Advance Date.
@return Last Advance Date */
public Timestamp getLastAdvanceDate() 
{
return (Timestamp)get_Value("LastAdvanceDate");
}
/** Set Next Advance Date.
@param NextAdvanceDate Next Advance Date */
public void setNextAdvanceDate (Timestamp NextAdvanceDate)
{
set_Value ("NextAdvanceDate", NextAdvanceDate);
}
/** Get Next Advance Date.
@return Next Advance Date */
public Timestamp getNextAdvanceDate() 
{
return (Timestamp)get_Value("NextAdvanceDate");
}

/** NextScale_ID AD_Reference_ID=2000015 */
public static final int NEXTSCALE_ID_AD_Reference_ID=2000015;
/** Set Next scale.
@param NextScale_ID Next scale */
public void setNextScale_ID (int NextScale_ID)
{
if (NextScale_ID < 1) throw new IllegalArgumentException ("NextScale_ID is mandatory.");
set_Value ("NextScale_ID", new Integer(NextScale_ID));
}
/** Get Next scale.
@return Next scale */
public int getNextScale_ID() 
{
Integer ii = (Integer)get_Value("NextScale_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Assignment_ID AD_Reference_ID=2000057 */
public static final int P_ASSIGNMENT_ID_AD_Reference_ID=2000057;
/** Set Assignment.
@param P_Assignment_ID Assignment */
public void setP_Assignment_ID (int P_Assignment_ID)
{
if (P_Assignment_ID < 1) throw new IllegalArgumentException ("P_Assignment_ID is mandatory.");
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

/** P_Collective_Labour_Agr_ID AD_Reference_ID=2000006 */
public static final int P_COLLECTIVE_LABOUR_AGR_ID_AD_Reference_ID=2000006;
/** Set Collective Labour Agreement.
@param P_Collective_Labour_Agr_ID Collective Labour Agreement */
public void setP_Collective_Labour_Agr_ID (int P_Collective_Labour_Agr_ID)
{
if (P_Collective_Labour_Agr_ID <= 0) set_Value ("P_Collective_Labour_Agr_ID", null);
 else 
set_Value ("P_Collective_Labour_Agr_ID", new Integer(P_Collective_Labour_Agr_ID));
}
/** Get Collective Labour Agreement.
@return Collective Labour Agreement */
public int getP_Collective_Labour_Agr_ID() 
{
Integer ii = (Integer)get_Value("P_Collective_Labour_Agr_ID");
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
/** Set P_Employee_Scale_Advance_ID.
@param P_Employee_Scale_Advance_ID P_Employee_Scale_Advance_ID */
public void setP_Employee_Scale_Advance_ID (int P_Employee_Scale_Advance_ID)
{
if (P_Employee_Scale_Advance_ID < 1) throw new IllegalArgumentException ("P_Employee_Scale_Advance_ID is mandatory.");
set_ValueNoCheck ("P_Employee_Scale_Advance_ID", new Integer(P_Employee_Scale_Advance_ID));
}
/** Get P_Employee_Scale_Advance_ID.
@return P_Employee_Scale_Advance_ID */
public int getP_Employee_Scale_Advance_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Scale_Advance_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Job_Title_ID AD_Reference_ID=2000025 */
public static final int P_JOB_TITLE_ID_AD_Reference_ID=2000025;
/** Set Job Title.
@param P_Job_Title_ID   */
public void setP_Job_Title_ID (int P_Job_Title_ID)
{
if (P_Job_Title_ID <= 0) set_Value ("P_Job_Title_ID", null);
 else 
set_Value ("P_Job_Title_ID", new Integer(P_Job_Title_ID));
}
/** Get Job Title.
@return   */
public int getP_Job_Title_ID() 
{
Integer ii = (Integer)get_Value("P_Job_Title_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Job_Type_ID AD_Reference_ID=2000054 */
public static final int P_JOB_TYPE_ID_AD_Reference_ID=2000054;
/** Set Job Type.
@param P_Job_Type_ID Job Type */
public void setP_Job_Type_ID (int P_Job_Type_ID)
{
if (P_Job_Type_ID <= 0) set_Value ("P_Job_Type_ID", null);
 else 
set_Value ("P_Job_Type_ID", new Integer(P_Job_Type_ID));
}
/** Get Job Type.
@return Job Type */
public int getP_Job_Type_ID() 
{
Integer ii = (Integer)get_Value("P_Job_Type_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Occupation Group.
@param P_Occupation_Group_ID   */
public void setP_Occupation_Group_ID (int P_Occupation_Group_ID)
{
if (P_Occupation_Group_ID <= 0) set_Value ("P_Occupation_Group_ID", null);
 else 
set_Value ("P_Occupation_Group_ID", new Integer(P_Occupation_Group_ID));
}
/** Get Occupation Group.
@return   */
public int getP_Occupation_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Occupation_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Salary_Class_ID AD_Reference_ID=2100354 */
public static final int P_SALARY_CLASS_ID_AD_Reference_ID=2100354;
/** Set Salary Class.
@param P_Salary_Class_ID Salary Class */
public void setP_Salary_Class_ID (int P_Salary_Class_ID)
{
if (P_Salary_Class_ID <= 0) set_Value ("P_Salary_Class_ID", null);
 else 
set_Value ("P_Salary_Class_ID", new Integer(P_Salary_Class_ID));
}
/** Get Salary Class.
@return Salary Class */
public int getP_Salary_Class_ID() 
{
Integer ii = (Integer)get_Value("P_Salary_Class_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
