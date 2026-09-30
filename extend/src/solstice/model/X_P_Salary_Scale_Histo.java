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
/** Generated Model for P_Salary_Scale_Histo
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Salary_Scale_Histo extends PO
{
/** Standard Constructor
@param ctx context
@param P_Salary_Scale_Histo_ID id
@param trxName transaction
*/
public X_P_Salary_Scale_Histo (Properties ctx, int P_Salary_Scale_Histo_ID, String trxName)
{
super (ctx, P_Salary_Scale_Histo_ID, trxName);
/** if (P_Salary_Scale_Histo_ID == 0)
{
setP_Collective_Labour_Agr_ID (0);
setP_Occupation_Group_ID (0);
setP_Salary_Scale_Histo_ID (0);
setP_Salary_Scale_ID (0);
setScaleDate (new Timestamp(System.currentTimeMillis()));
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Salary_Scale_Histo (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000052 */
public static final int Table_ID=2000052;

/** TableName=P_Salary_Scale_Histo */
public static final String Table_Name="P_Salary_Scale_Histo";

protected static KeyNamePair Model = new KeyNamePair(2000052,"P_Salary_Scale_Histo");

protected BigDecimal accessLevel = new BigDecimal(7);
/** AccessLevel
@return 7 - System - Client - Org 
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
StringBuffer sb = new StringBuffer ("X_P_Salary_Scale_Histo[").append(get_ID()).append("]");
return sb.toString();
}

/** P_Collective_Labour_Agr_ID AD_Reference_ID=2000006 */
public static final int P_COLLECTIVE_LABOUR_AGR_ID_AD_Reference_ID=2000006;
/** Set Collective Labour Agreement.
@param P_Collective_Labour_Agr_ID Collective Labour Agreement */
public void setP_Collective_Labour_Agr_ID (int P_Collective_Labour_Agr_ID)
{
if (P_Collective_Labour_Agr_ID < 1) throw new IllegalArgumentException ("P_Collective_Labour_Agr_ID is mandatory.");
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

/** P_Credits_ID AD_Reference_ID=2100318 */
public static final int P_CREDITS_ID_AD_Reference_ID=2100318;
/** Set Credits.
@param P_Credits_ID Credits */
public void setP_Credits_ID (int P_Credits_ID)
{
if (P_Credits_ID <= 0) set_Value ("P_Credits_ID", null);
 else 
set_Value ("P_Credits_ID", new Integer(P_Credits_ID));
}
/** Get Credits.
@return Credits */
public int getP_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Job_Title_ID AD_Reference_ID=2000027 */
public static final int P_JOB_TITLE_ID_AD_Reference_ID=2000027;
/** Job_Title_LV = 1000021 */
public static final String P_JOB_TITLE_ID_Job_Title_LV = "1000021";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isP_Job_Title_IDValid (String test)
{
return test == null || test.equals("1000021");
}
/** Set Job Title.
@param P_Job_Title_ID   */
public void setP_Job_Title_ID (String P_Job_Title_ID)
{
if (!isP_Job_Title_IDValid(P_Job_Title_ID))
throw new IllegalArgumentException ("P_Job_Title_ID Invalid value - " + P_Job_Title_ID + " - Reference_ID=2000027 - 1000021");
if (P_Job_Title_ID != null && P_Job_Title_ID.length() > 22)
{
log.warning("Length > 22 - truncated");
P_Job_Title_ID = P_Job_Title_ID.substring(0,21);
}
set_Value ("P_Job_Title_ID", P_Job_Title_ID);
}
/** Get Job Title.
@return   */
public String getP_Job_Title_ID() 
{
return (String)get_Value("P_Job_Title_ID");
}

/** P_Occupation_Group_ID AD_Reference_ID=2000014 */
public static final int P_OCCUPATION_GROUP_ID_AD_Reference_ID=2000014;
/** Set Occupation Group.
@param P_Occupation_Group_ID   */
public void setP_Occupation_Group_ID (int P_Occupation_Group_ID)
{
if (P_Occupation_Group_ID < 1) throw new IllegalArgumentException ("P_Occupation_Group_ID is mandatory.");
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
/** Set P_Salary_Scale_Histo_ID.
@param P_Salary_Scale_Histo_ID P_Salary_Scale_Histo_ID */
public void setP_Salary_Scale_Histo_ID (int P_Salary_Scale_Histo_ID)
{
if (P_Salary_Scale_Histo_ID < 1) throw new IllegalArgumentException ("P_Salary_Scale_Histo_ID is mandatory.");
set_Value ("P_Salary_Scale_Histo_ID", new Integer(P_Salary_Scale_Histo_ID));
}
/** Get P_Salary_Scale_Histo_ID.
@return P_Salary_Scale_Histo_ID */
public int getP_Salary_Scale_Histo_ID() 
{
Integer ii = (Integer)get_Value("P_Salary_Scale_Histo_ID");
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

/** Remuneration_Method AD_Reference_ID=2000016 */
public static final int REMUNERATION_METHOD_AD_Reference_ID=2000016;
/** daily = daily */
public static final String REMUNERATION_METHOD_Daily = "daily";
/** hourly = hourly */
public static final String REMUNERATION_METHOD_Hourly = "hourly";
/** annual = annual */
public static final String REMUNERATION_METHOD_Annual = "annual";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isRemuneration_MethodValid (String test)
{
return test == null || test.equals("daily") || test.equals("hourly") || test.equals("annual");
}
/** Set Remuneration Method.
@param Remuneration_Method   */
public void setRemuneration_Method (String Remuneration_Method)
{
if (!isRemuneration_MethodValid(Remuneration_Method))
throw new IllegalArgumentException ("Remuneration_Method Invalid value - " + Remuneration_Method + " - Reference_ID=2000016 - daily - hourly - annual");
if (Remuneration_Method != null && Remuneration_Method.length() > 30)
{
log.warning("Length > 30 - truncated");
Remuneration_Method = Remuneration_Method.substring(0,29);
}
set_Value ("Remuneration_Method", Remuneration_Method);
}
/** Get Remuneration Method.
@return   */
public String getRemuneration_Method() 
{
return (String)get_Value("Remuneration_Method");
}
/** Set Scale Date.
@param ScaleDate Scale Date */
public void setScaleDate (Timestamp ScaleDate)
{
if (ScaleDate == null) throw new IllegalArgumentException ("ScaleDate is mandatory.");
set_Value ("ScaleDate", ScaleDate);
}
/** Get Scale Date.
@return Scale Date */
public Timestamp getScaleDate() 
{
return (Timestamp)get_Value("ScaleDate");
}
}
