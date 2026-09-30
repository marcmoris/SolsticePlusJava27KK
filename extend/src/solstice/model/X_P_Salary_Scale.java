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
/** Generated Model for P_Salary_Scale
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Salary_Scale extends PO
{
/** Standard Constructor
@param ctx context
@param P_Salary_Scale_ID id
@param trxName transaction
*/
public X_P_Salary_Scale (Properties ctx, int P_Salary_Scale_ID, String trxName)
{
super (ctx, P_Salary_Scale_ID, trxName);
/** if (P_Salary_Scale_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));	// @#Date@
setP_Collective_Labour_Agr_ID (0);
setP_Occupation_Group_ID (0);
setP_Salary_Scale_ID (0);
setRemuneration_Method (null);	// annual
setScale_Type (null);
setScaleDate (new Timestamp(System.currentTimeMillis()));	// @#Date@
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Salary_Scale (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000028 */
public static final int Table_ID=2000028;

/** TableName=P_Salary_Scale */
public static final String Table_Name="P_Salary_Scale";

protected static KeyNamePair Model = new KeyNamePair(2000028,"P_Salary_Scale");

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
StringBuffer sb = new StringBuffer ("X_P_Salary_Scale[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Copy From.
@param CopyFrom Copy From Record */
public void setCopyFrom (String CopyFrom)
{
if (CopyFrom != null && CopyFrom.length() > 2)
{
log.warning("Length > 2 - truncated");
CopyFrom = CopyFrom.substring(0,1);
}
set_Value ("CopyFrom", CopyFrom);
}
/** Get Copy From.
@return Copy From Record */
public String getCopyFrom() 
{
return (String)get_Value("CopyFrom");
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
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name != null && Name.length() > 255)
{
log.warning("Length > 255 - truncated");
Name = Name.substring(0,254);
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

/** P_Occupation_Group_ID AD_Reference_ID=2000014 */
public static final int P_OCCUPATION_GROUP_ID_AD_Reference_ID=2000014;
/** Set Occupation Group.
@param P_Occupation_Group_ID   */
public void setP_Occupation_Group_ID (int P_Occupation_Group_ID)
{
if (P_Occupation_Group_ID < 1) throw new IllegalArgumentException ("P_Occupation_Group_ID is mandatory.");
set_ValueNoCheck ("P_Occupation_Group_ID", new Integer(P_Occupation_Group_ID));
}
/** Get Occupation Group.
@return   */
public int getP_Occupation_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Occupation_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Post_ID AD_Reference_ID=2000004 */
public static final int P_POST_ID_AD_Reference_ID=2000004;
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
/** Set Salary Scale.
@param P_Salary_Scale_ID   */
public void setP_Salary_Scale_ID (int P_Salary_Scale_ID)
{
if (P_Salary_Scale_ID < 1) throw new IllegalArgumentException ("P_Salary_Scale_ID is mandatory.");
set_ValueNoCheck ("P_Salary_Scale_ID", new Integer(P_Salary_Scale_ID));
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
/** hourly = hourly */
public static final String REMUNERATION_METHOD_Hourly = "hourly";
/** annual = annual */
public static final String REMUNERATION_METHOD_Annual = "annual";
/** daily = daily */
public static final String REMUNERATION_METHOD_Daily = "daily";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isRemuneration_MethodValid (String test)
{
return test.equals("hourly") || test.equals("annual") || test.equals("daily");
}
/** Set Remuneration Method.
@param Remuneration_Method   */
public void setRemuneration_Method (String Remuneration_Method)
{
if (Remuneration_Method == null) throw new IllegalArgumentException ("Remuneration_Method is mandatory");
if (!isRemuneration_MethodValid(Remuneration_Method))
throw new IllegalArgumentException ("Remuneration_Method Invalid value - " + Remuneration_Method + " - Reference_ID=2000016 - hourly - annual - daily");
if (Remuneration_Method.length() > 30)
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

/** Scale_Type AD_Reference_ID=2100355 */
public static final int SCALE_TYPE_AD_Reference_ID=2100355;
/** Regular = R */
public static final String SCALE_TYPE_Regular = "R";
/** According to limits = B */
public static final String SCALE_TYPE_AccordingToLimits = "B";
/** According to limits/credits = C */
public static final String SCALE_TYPE_AccordingToLimitsCredits = "C";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isScale_TypeValid (String test)
{
return test.equals("R") || test.equals("B") || test.equals("C");
}
/** Set Scale Type.
@param Scale_Type Scale Type */
public void setScale_Type (String Scale_Type)
{
if (Scale_Type == null) throw new IllegalArgumentException ("Scale_Type is mandatory");
if (!isScale_TypeValid(Scale_Type))
throw new IllegalArgumentException ("Scale_Type Invalid value - " + Scale_Type + " - Reference_ID=2100355 - R - B - C");
if (Scale_Type.length() > 1)
{
log.warning("Length > 1 - truncated");
Scale_Type = Scale_Type.substring(0,0);
}
set_Value ("Scale_Type", Scale_Type);
}
/** Get Scale Type.
@return Scale Type */
public String getScale_Type() 
{
return (String)get_Value("Scale_Type");
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
/** Set ScaleDateStr.
@param ScaleDateStr ScaleDateStr */
public void setScaleDateStr (String ScaleDateStr)
{
if (ScaleDateStr != null && ScaleDateStr.length() > 50)
{
log.warning("Length > 50 - truncated");
ScaleDateStr = ScaleDateStr.substring(0,49);
}
set_ValueNoCheck ("ScaleDateStr", ScaleDateStr);
}
/** Get ScaleDateStr.
@return ScaleDateStr */
public String getScaleDateStr() 
{
return (String)get_Value("ScaleDateStr");
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
