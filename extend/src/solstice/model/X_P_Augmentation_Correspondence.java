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
/** Generated Model for P_Augmentation_Correspondence
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Augmentation_Correspondence extends PO
{
/** Standard Constructor
@param ctx context
@param P_Augmentation_Correspondence_ID id
@param trxName transaction
*/
public X_P_Augmentation_Correspondence (Properties ctx, int P_Augmentation_Correspondence_ID, String trxName)
{
super (ctx, P_Augmentation_Correspondence_ID, trxName);
/** if (P_Augmentation_Correspondence_ID == 0)
{
setP_Augmentation_Correspondence_ID (0);
setP_Augmentation_ID (0);
setP_Job_Title_ID (0);
setP_Job_Title_New_ID (0);
setP_Salary_Class_ID (0);
setP_Salary_Class_New_ID (0);
setP_Salary_Scale_Detail_ID (0);
setP_Salary_Scale_Detail_New_ID (0);
setStartDate (new Timestamp(System.currentTimeMillis()));
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Augmentation_Correspondence (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100788 */
public static final int Table_ID=2100788;

/** TableName=P_Augmentation_Correspondence */
public static final String Table_Name="P_Augmentation_Correspondence";

protected static KeyNamePair Model = new KeyNamePair(2100788,"P_Augmentation_Correspondence");

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
StringBuffer sb = new StringBuffer ("X_P_Augmentation_Correspondence[").append(get_ID()).append("]");
return sb.toString();
}
/** Set % Augmentation.
@param AugPrcCad % Augmentation */
public void setAugPrcCad (BigDecimal AugPrcCad)
{
set_Value ("AugPrcCad", AugPrcCad);
}
/** Get % Augmentation.
@return % Augmentation */
public BigDecimal getAugPrcCad() 
{
BigDecimal bd = (BigDecimal)get_Value("AugPrcCad");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set End Date.
@param EndDate Last effective date (inclusive) */
public void setEndDate (Timestamp EndDate)
{
set_Value ("EndDate", EndDate);
}
/** Get End Date.
@return Last effective date (inclusive) */
public Timestamp getEndDate() 
{
return (Timestamp)get_Value("EndDate");
}
/** Set Is with %.
@param IsWithPct Is with % */
public void setIsWithPct (boolean IsWithPct)
{
set_Value ("IsWithPct", new Boolean(IsWithPct));
}
/** Get Is with %.
@return Is with % */
public boolean isWithPct() 
{
Object oo = get_Value("IsWithPct");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set P_Augmentation_Correspondence_ID.
@param P_Augmentation_Correspondence_ID P_Augmentation_Correspondence_ID */
public void setP_Augmentation_Correspondence_ID (int P_Augmentation_Correspondence_ID)
{
if (P_Augmentation_Correspondence_ID < 1) throw new IllegalArgumentException ("P_Augmentation_Correspondence_ID is mandatory.");
set_ValueNoCheck ("P_Augmentation_Correspondence_ID", new Integer(P_Augmentation_Correspondence_ID));
}
/** Get P_Augmentation_Correspondence_ID.
@return P_Augmentation_Correspondence_ID */
public int getP_Augmentation_Correspondence_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_Correspondence_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Retroactivity.
@param P_Augmentation_ID Retroactivity */
public void setP_Augmentation_ID (int P_Augmentation_ID)
{
if (P_Augmentation_ID < 1) throw new IllegalArgumentException ("P_Augmentation_ID is mandatory.");
set_Value ("P_Augmentation_ID", new Integer(P_Augmentation_ID));
}
/** Get Retroactivity.
@return Retroactivity */
public int getP_Augmentation_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Job_Title_ID AD_Reference_ID=2000025 */
public static final int P_JOB_TITLE_ID_AD_Reference_ID=2000025;
/** Set Job Title.
@param P_Job_Title_ID   */
public void setP_Job_Title_ID (int P_Job_Title_ID)
{
if (P_Job_Title_ID < 1) throw new IllegalArgumentException ("P_Job_Title_ID is mandatory.");
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

/** P_Job_Title_New_ID AD_Reference_ID=2000025 */
public static final int P_JOB_TITLE_NEW_ID_AD_Reference_ID=2000025;
/** Set New job title.
@param P_Job_Title_New_ID New job title */
public void setP_Job_Title_New_ID (int P_Job_Title_New_ID)
{
if (P_Job_Title_New_ID < 1) throw new IllegalArgumentException ("P_Job_Title_New_ID is mandatory.");
set_Value ("P_Job_Title_New_ID", new Integer(P_Job_Title_New_ID));
}
/** Get New job title.
@return New job title */
public int getP_Job_Title_New_ID() 
{
Integer ii = (Integer)get_Value("P_Job_Title_New_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Salary_Class_ID AD_Reference_ID=2100354 */
public static final int P_SALARY_CLASS_ID_AD_Reference_ID=2100354;
/** Set Salary Class.
@param P_Salary_Class_ID Salary Class */
public void setP_Salary_Class_ID (int P_Salary_Class_ID)
{
if (P_Salary_Class_ID < 1) throw new IllegalArgumentException ("P_Salary_Class_ID is mandatory.");
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

/** P_Salary_Class_New_ID AD_Reference_ID=2100354 */
public static final int P_SALARY_CLASS_NEW_ID_AD_Reference_ID=2100354;
/** Set New salary class.
@param P_Salary_Class_New_ID New salary class */
public void setP_Salary_Class_New_ID (int P_Salary_Class_New_ID)
{
if (P_Salary_Class_New_ID < 1) throw new IllegalArgumentException ("P_Salary_Class_New_ID is mandatory.");
set_Value ("P_Salary_Class_New_ID", new Integer(P_Salary_Class_New_ID));
}
/** Get New salary class.
@return New salary class */
public int getP_Salary_Class_New_ID() 
{
Integer ii = (Integer)get_Value("P_Salary_Class_New_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Salary_Scale_Detail_ID AD_Reference_ID=2000015 */
public static final int P_SALARY_SCALE_DETAIL_ID_AD_Reference_ID=2000015;
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

/** P_Salary_Scale_Detail_New_ID AD_Reference_ID=2000015 */
public static final int P_SALARY_SCALE_DETAIL_NEW_ID_AD_Reference_ID=2000015;
/** Set New salary scale detail.
@param P_Salary_Scale_Detail_New_ID New salary scale detail */
public void setP_Salary_Scale_Detail_New_ID (int P_Salary_Scale_Detail_New_ID)
{
if (P_Salary_Scale_Detail_New_ID < 1) throw new IllegalArgumentException ("P_Salary_Scale_Detail_New_ID is mandatory.");
set_Value ("P_Salary_Scale_Detail_New_ID", new Integer(P_Salary_Scale_Detail_New_ID));
}
/** Get New salary scale detail.
@return New salary scale detail */
public int getP_Salary_Scale_Detail_New_ID() 
{
Integer ii = (Integer)get_Value("P_Salary_Scale_Detail_New_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Salary_Scale_ID AD_Reference_ID=2100434 */
public static final int P_SALARY_SCALE_ID_AD_Reference_ID=2100434;
/** Set Salary Scale.
@param P_Salary_Scale_ID   */
public void setP_Salary_Scale_ID (int P_Salary_Scale_ID)
{
if (P_Salary_Scale_ID <= 0) set_Value ("P_Salary_Scale_ID", null);
 else 
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

/** P_Salary_Scale_New_ID AD_Reference_ID=2100434 */
public static final int P_SALARY_SCALE_NEW_ID_AD_Reference_ID=2100434;
/** Set New salary scale.
@param P_Salary_Scale_New_ID New salary scale */
public void setP_Salary_Scale_New_ID (int P_Salary_Scale_New_ID)
{
if (P_Salary_Scale_New_ID <= 0) set_Value ("P_Salary_Scale_New_ID", null);
 else 
set_Value ("P_Salary_Scale_New_ID", new Integer(P_Salary_Scale_New_ID));
}
/** Get New salary scale.
@return New salary scale */
public int getP_Salary_Scale_New_ID() 
{
Integer ii = (Integer)get_Value("P_Salary_Scale_New_ID");
if (ii == null) return 0;
return ii.intValue();
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
}
