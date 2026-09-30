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
/** Generated Model for P_Credits_Alert
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Credits_Alert extends PO
{
/** Standard Constructor
@param ctx context
@param P_Credits_Alert_ID id
@param trxName transaction
*/
public X_P_Credits_Alert (Properties ctx, int P_Credits_Alert_ID, String trxName)
{
super (ctx, P_Credits_Alert_ID, trxName);
/** if (P_Credits_Alert_ID == 0)
{
setAlert_Condition (null);	// 1
setAlert_Limit (Env.ZERO);	// 0
setP_Credits_Alert_ID (0);
setP_Credits_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Credits_Alert (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000058 */
public static final int Table_ID=2000058;

/** TableName=P_Credits_Alert */
public static final String Table_Name="P_Credits_Alert";

protected static KeyNamePair Model = new KeyNamePair(2000058,"P_Credits_Alert");

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
StringBuffer sb = new StringBuffer ("X_P_Credits_Alert[").append(get_ID()).append("]");
return sb.toString();
}

/** Alert_Condition AD_Reference_ID=2000047 */
public static final int ALERT_CONDITION_AD_Reference_ID=2000047;
/** Le solde est égal à = 4 */
public static final String ALERT_CONDITION_LeSoldeEstÉgalÀ = "4";
/** Le solde est plus grand que = 5 */
public static final String ALERT_CONDITION_LeSoldeEstPlusGrandQue = "5";
/** Le solde est plus petit que = 1 */
public static final String ALERT_CONDITION_LeSoldeEstPlusPetitQue = "1";
/** Le solde est plus grand ou égal = 6 */
public static final String ALERT_CONDITION_LeSoldeEstPlusGrandOuÉgal = "6";
/** Le solde est plus petit ou égal à = 2 */
public static final String ALERT_CONDITION_LeSoldeEstPlusPetitOuÉgalÀ = "2";
/** Le solde est différent de = 3 */
public static final String ALERT_CONDITION_LeSoldeEstDifférentDe = "3";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isAlert_ConditionValid (String test)
{
return test.equals("4") || test.equals("5") || test.equals("1") || test.equals("6") || test.equals("2") || test.equals("3");
}
/** Set Condition.
@param Alert_Condition Condition */
public void setAlert_Condition (String Alert_Condition)
{
if (Alert_Condition == null) throw new IllegalArgumentException ("Alert_Condition is mandatory");
if (!isAlert_ConditionValid(Alert_Condition))
throw new IllegalArgumentException ("Alert_Condition Invalid value - " + Alert_Condition + " - Reference_ID=2000047 - 4 - 5 - 1 - 6 - 2 - 3");
if (Alert_Condition.length() > 4)
{
log.warning("Length > 4 - truncated");
Alert_Condition = Alert_Condition.substring(0,3);
}
set_Value ("Alert_Condition", Alert_Condition);
}
/** Get Condition.
@return Condition */
public String getAlert_Condition() 
{
return (String)get_Value("Alert_Condition");
}
/** Set Limit.
@param Alert_Limit Limit */
public void setAlert_Limit (BigDecimal Alert_Limit)
{
if (Alert_Limit == null) throw new IllegalArgumentException ("Alert_Limit is mandatory.");
set_Value ("Alert_Limit", Alert_Limit);
}
/** Get Limit.
@return Limit */
public BigDecimal getAlert_Limit() 
{
BigDecimal bd = (BigDecimal)get_Value("Alert_Limit");
if (bd == null) return Env.ZERO;
return bd;
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getAlert_Limit()));
}
/** Set Message.
@param Alert_Message Message */
public void setAlert_Message (String Alert_Message)
{
if (Alert_Message != null && Alert_Message.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Alert_Message = Alert_Message.substring(0,1999);
}
set_Value ("Alert_Message", Alert_Message);
}
/** Get Message.
@return Message */
public String getAlert_Message() 
{
return (String)get_Value("Alert_Message");
}

/** Alert_Option AD_Reference_ID=2100339 */
public static final int ALERT_OPTION_AD_Reference_ID=2100339;
/** if the Job Type ist = 1 */
public static final String ALERT_OPTION_IfTheJobTypeIst = "1";
/** if the Job Title ist = 2 */
public static final String ALERT_OPTION_IfTheJobTitleIst = "2";
/** if Occupation Group ist = 3 */
public static final String ALERT_OPTION_IfOccupationGroupIst = "3";
/** if the Payment Group ist = 4 */
public static final String ALERT_OPTION_IfThePaymentGroupIst = "4";
/** if the Workplace ist = 5 */
public static final String ALERT_OPTION_IfTheWorkplaceIst = "5";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isAlert_OptionValid (String test)
{
return test == null || test.equals("1") || test.equals("2") || test.equals("3") || test.equals("4") || test.equals("5");
}
/** Set Option.
@param Alert_Option Option */
public void setAlert_Option (String Alert_Option)
{
if (!isAlert_OptionValid(Alert_Option))
throw new IllegalArgumentException ("Alert_Option Invalid value - " + Alert_Option + " - Reference_ID=2100339 - 1 - 2 - 3 - 4 - 5");
if (Alert_Option != null && Alert_Option.length() > 2)
{
log.warning("Length > 2 - truncated");
Alert_Option = Alert_Option.substring(0,1);
}
set_Value ("Alert_Option", Alert_Option);
}
/** Get Option.
@return Option */
public String getAlert_Option() 
{
return (String)get_Value("Alert_Option");
}

/** Alert_Severity_Level AD_Reference_ID=2100348 */
public static final int ALERT_SEVERITY_LEVEL_AD_Reference_ID=2100348;
/** Error = E */
public static final String ALERT_SEVERITY_LEVEL_Error = "E";
/** Information = I */
public static final String ALERT_SEVERITY_LEVEL_Information = "I";
/** Warning = W */
public static final String ALERT_SEVERITY_LEVEL_Warning = "W";
/** Chain = C */
public static final String ALERT_SEVERITY_LEVEL_Chain = "C";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isAlert_Severity_LevelValid (String test)
{
return test == null || test.equals("E") || test.equals("I") || test.equals("W") || test.equals("C");
}
/** Set Severity Level.
@param Alert_Severity_Level Severity Level */
public void setAlert_Severity_Level (String Alert_Severity_Level)
{
if (!isAlert_Severity_LevelValid(Alert_Severity_Level))
throw new IllegalArgumentException ("Alert_Severity_Level Invalid value - " + Alert_Severity_Level + " - Reference_ID=2100348 - E - I - W - C");
if (Alert_Severity_Level != null && Alert_Severity_Level.length() > 1)
{
log.warning("Length > 1 - truncated");
Alert_Severity_Level = Alert_Severity_Level.substring(0,0);
}
set_Value ("Alert_Severity_Level", Alert_Severity_Level);
}
/** Get Severity Level.
@return Severity Level */
public String getAlert_Severity_Level() 
{
return (String)get_Value("Alert_Severity_Level");
}
/** Set Alert.
@param P_Credits_Alert_ID Alert */
public void setP_Credits_Alert_ID (int P_Credits_Alert_ID)
{
if (P_Credits_Alert_ID < 1) throw new IllegalArgumentException ("P_Credits_Alert_ID is mandatory.");
set_Value ("P_Credits_Alert_ID", new Integer(P_Credits_Alert_ID));
}
/** Get Alert.
@return Alert */
public int getP_Credits_Alert_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Alert_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Credits.
@param P_Credits_ID Credits */
public void setP_Credits_ID (int P_Credits_ID)
{
if (P_Credits_ID < 1) throw new IllegalArgumentException ("P_Credits_ID is mandatory.");
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

/** P_Credits_Join_ID AD_Reference_ID=2100318 */
public static final int P_CREDITS_JOIN_ID_AD_Reference_ID=2100318;
/** Set Credit.
@param P_Credits_Join_ID Credit */
public void setP_Credits_Join_ID (int P_Credits_Join_ID)
{
if (P_Credits_Join_ID <= 0) set_Value ("P_Credits_Join_ID", null);
 else 
set_Value ("P_Credits_Join_ID", new Integer(P_Credits_Join_ID));
}
/** Get Credit.
@return Credit */
public int getP_Credits_Join_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Join_ID");
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
/** Set Method.
@param P_Method_Credits_ID Method */
public void setP_Method_Credits_ID (int P_Method_Credits_ID)
{
if (P_Method_Credits_ID <= 0) set_Value ("P_Method_Credits_ID", null);
 else 
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

/** P_Occupation_Group_ID AD_Reference_ID=2000014 */
public static final int P_OCCUPATION_GROUP_ID_AD_Reference_ID=2000014;
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

/** P_Payment_Group_ID AD_Reference_ID=2000056 */
public static final int P_PAYMENT_GROUP_ID_AD_Reference_ID=2000056;
/** Set Payment Group.
@param P_Payment_Group_ID Payment Group */
public void setP_Payment_Group_ID (int P_Payment_Group_ID)
{
if (P_Payment_Group_ID <= 0) set_Value ("P_Payment_Group_ID", null);
 else 
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

/** P_Workplace_ID AD_Reference_ID=2000005 */
public static final int P_WORKPLACE_ID_AD_Reference_ID=2000005;
/** Set Workplace .
@param P_Workplace_ID Workplace  */
public void setP_Workplace_ID (int P_Workplace_ID)
{
if (P_Workplace_ID <= 0) set_Value ("P_Workplace_ID", null);
 else 
set_Value ("P_Workplace_ID", new Integer(P_Workplace_ID));
}
/** Get Workplace .
@return Workplace  */
public int getP_Workplace_ID() 
{
Integer ii = (Integer)get_Value("P_Workplace_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
