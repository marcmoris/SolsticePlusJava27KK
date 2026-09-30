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
/** Generated Model for P_AccidentReport_HST
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_AccidentReport_HST extends PO
{
/** Standard Constructor
@param ctx context
@param P_AccidentReport_HST_ID id
@param trxName transaction
*/
public X_P_AccidentReport_HST (Properties ctx, int P_AccidentReport_HST_ID, String trxName)
{
super (ctx, P_AccidentReport_HST_ID, trxName);
/** if (P_AccidentReport_HST_ID == 0)
{
setP_ACCIDENTREPORT_HST_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_AccidentReport_HST (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100797 */
public static final int Table_ID=2100797;

/** TableName=P_AccidentReport_HST */
public static final String Table_Name="P_AccidentReport_HST";

protected static KeyNamePair Model = new KeyNamePair(2100797,"P_AccidentReport_HST");

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
StringBuffer sb = new StringBuffer ("X_P_AccidentReport_HST[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Date of Accident.
@param AccidentDate Date of Accident */
public void setAccidentDate (Timestamp AccidentDate)
{
set_Value ("AccidentDate", AccidentDate);
}
/** Get Date of Accident.
@return Date of Accident */
public Timestamp getAccidentDate() 
{
return (Timestamp)get_Value("AccidentDate");
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
/** Set Contestation.
@param Contestation Description of a constation on a CSST's report of accident */
public void setContestation (String Contestation)
{
if (Contestation != null && Contestation.length() > 4000)
{
log.warning("Length > 4000 - truncated");
Contestation = Contestation.substring(0,3999);
}
set_Value ("Contestation", Contestation);
}
/** Get Contestation.
@return Description of a constation on a CSST's report of accident */
public String getContestation() 
{
return (String)get_Value("Contestation");
}
/** Set Date of Contestation.
@param ContestationDate Date of Contestation */
public void setContestationDate (Timestamp ContestationDate)
{
set_Value ("ContestationDate", ContestationDate);
}
/** Get Date of Contestation.
@return Date of Contestation */
public Timestamp getContestationDate() 
{
return (Timestamp)get_Value("ContestationDate");
}
/** Set Contract.
@param ContractNumber The number of the contract */
public void setContractNumber (String ContractNumber)
{
if (ContractNumber != null && ContractNumber.length() > 50)
{
log.warning("Length > 50 - truncated");
ContractNumber = ContractNumber.substring(0,49);
}
set_Value ("ContractNumber", ContractNumber);
}
/** Get Contract.
@return The number of the contract */
public String getContractNumber() 
{
return (String)get_Value("ContractNumber");
}
/** Set CSST's Record.
@param CSSTRecord CSST's Record */
public void setCSSTRecord (String CSSTRecord)
{
if (CSSTRecord != null && CSSTRecord.length() > 10)
{
log.warning("Length > 10 - truncated");
CSSTRecord = CSSTRecord.substring(0,9);
}
set_Value ("CSSTRecord", CSSTRecord);
}
/** Get CSST's Record.
@return CSST's Record */
public String getCSSTRecord() 
{
return (String)get_Value("CSSTRecord");
}
/** Set Decision date.
@param DecisionDate Decision date */
public void setDecisionDate (Timestamp DecisionDate)
{
set_Value ("DecisionDate", DecisionDate);
}
/** Get Decision date.
@return Decision date */
public Timestamp getDecisionDate() 
{
return (Timestamp)get_Value("DecisionDate");
}
/** Set FirstDayLost.
@param FirstDayLost FirstDayLost */
public void setFirstDayLost (Timestamp FirstDayLost)
{
set_Value ("FirstDayLost", FirstDayLost);
}
/** Get FirstDayLost.
@return FirstDayLost */
public Timestamp getFirstDayLost() 
{
return (Timestamp)get_Value("FirstDayLost");
}
/** Set Manager.
@param Manager Manager */
public void setManager (String Manager)
{
if (Manager != null && Manager.length() > 50)
{
log.warning("Length > 50 - truncated");
Manager = Manager.substring(0,49);
}
set_Value ("Manager", Manager);
}
/** Get Manager.
@return Manager */
public String getManager() 
{
return (String)get_Value("Manager");
}
/** Set P_ACCIDENTREPORT_HST_ID.
@param P_ACCIDENTREPORT_HST_ID P_ACCIDENTREPORT_HST_ID */
public void setP_ACCIDENTREPORT_HST_ID (int P_ACCIDENTREPORT_HST_ID)
{
if (P_ACCIDENTREPORT_HST_ID < 1) throw new IllegalArgumentException ("P_ACCIDENTREPORT_HST_ID is mandatory.");
set_ValueNoCheck ("P_ACCIDENTREPORT_HST_ID", new Integer(P_ACCIDENTREPORT_HST_ID));
}
/** Get P_ACCIDENTREPORT_HST_ID.
@return P_ACCIDENTREPORT_HST_ID */
public int getP_ACCIDENTREPORT_HST_ID() 
{
Integer ii = (Integer)get_Value("P_ACCIDENTREPORT_HST_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Accident Type.
@param P_AccidentType_ID Accident Type */
public void setP_AccidentType_ID (int P_AccidentType_ID)
{
if (P_AccidentType_ID <= 0) set_Value ("P_AccidentType_ID", null);
 else 
set_Value ("P_AccidentType_ID", new Integer(P_AccidentType_ID));
}
/** Get Accident Type.
@return Accident Type */
public int getP_AccidentType_ID() 
{
Integer ii = (Integer)get_Value("P_AccidentType_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set Part Of Body Injured.
@param P_PartOfBodyInjured_ID A list of de parts of body that can be injured */
public void setP_PartOfBodyInjured_ID (int P_PartOfBodyInjured_ID)
{
if (P_PartOfBodyInjured_ID <= 0) set_Value ("P_PartOfBodyInjured_ID", null);
 else 
set_Value ("P_PartOfBodyInjured_ID", new Integer(P_PartOfBodyInjured_ID));
}
/** Get Part Of Body Injured.
@return A list of de parts of body that can be injured */
public int getP_PartOfBodyInjured_ID() 
{
Integer ii = (Integer)get_Value("P_PartOfBodyInjured_ID");
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
/** Set Record Status.
@param P_RecordStatus_ID CSST's Record Status */
public void setP_RecordStatus_ID (int P_RecordStatus_ID)
{
if (P_RecordStatus_ID <= 0) set_Value ("P_RecordStatus_ID", null);
 else 
set_Value ("P_RecordStatus_ID", new Integer(P_RecordStatus_ID));
}
/** Get Record Status.
@return CSST's Record Status */
public int getP_RecordStatus_ID() 
{
Integer ii = (Integer)get_Value("P_RecordStatus_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Wound.
@param P_Wound_ID The list of possible wounds */
public void setP_Wound_ID (int P_Wound_ID)
{
if (P_Wound_ID <= 0) set_Value ("P_Wound_ID", null);
 else 
set_Value ("P_Wound_ID", new Integer(P_Wound_ID));
}
/** Get Wound.
@return The list of possible wounds */
public int getP_Wound_ID() 
{
Integer ii = (Integer)get_Value("P_Wound_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Remark.
@param Remark Remark */
public void setRemark (String Remark)
{
if (Remark != null && Remark.length() > 4000)
{
log.warning("Length > 4000 - truncated");
Remark = Remark.substring(0,3999);
}
set_Value ("Remark", Remark);
}
/** Get Remark.
@return Remark */
public String getRemark() 
{
return (String)get_Value("Remark");
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 10)
{
log.warning("Length > 10 - truncated");
Value = Value.substring(0,9);
}
set_Value ("Value", Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
/** Set Who Contests.
@param WhoContest The kind of person who contests a report of CSST */
public void setWhoContest (String WhoContest)
{
if (WhoContest != null && WhoContest.length() > 20)
{
log.warning("Length > 20 - truncated");
WhoContest = WhoContest.substring(0,19);
}
set_Value ("WhoContest", WhoContest);
}
/** Get Who Contests.
@return The kind of person who contests a report of CSST */
public String getWhoContest() 
{
return (String)get_Value("WhoContest");
}
}
