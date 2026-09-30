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
/** Generated Model for P_ER_Evaluation
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_ER_Evaluation extends PO
{
/** Standard Constructor
@param ctx context
@param P_ER_Evaluation_ID id
@param trxName transaction
*/
public X_P_ER_Evaluation (Properties ctx, int P_ER_Evaluation_ID, String trxName)
{
super (ctx, P_ER_Evaluation_ID, trxName);
/** if (P_ER_Evaluation_ID == 0)
{
setEvaluationDate (new Timestamp(System.currentTimeMillis()));
setEvaluationType (null);
setIsProbation (false);
setP_Employee_Gest_ID (0);
setP_Employee_ID (0);
setP_ER_Evaluation_ID (0);
setP_ER_Form_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_ER_Evaluation (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100721 */
public static final int Table_ID=2100721;

/** TableName=P_ER_Evaluation */
public static final String Table_Name="P_ER_Evaluation";

protected static KeyNamePair Model = new KeyNamePair(2100721,"P_ER_Evaluation");

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
StringBuffer sb = new StringBuffer ("X_P_ER_Evaluation[").append(get_ID()).append("]");
return sb.toString();
}
/** Set BossBossEvaluationSignoff.
@param BossBossEvaluationSignoff BossBossEvaluationSignoff */
public void setBossBossEvaluationSignoff (Timestamp BossBossEvaluationSignoff)
{
set_Value ("BossBossEvaluationSignoff", BossBossEvaluationSignoff);
}
/** Get BossBossEvaluationSignoff.
@return BossBossEvaluationSignoff */
public Timestamp getBossBossEvaluationSignoff() 
{
return (Timestamp)get_Value("BossBossEvaluationSignoff");
}
/** Set BossEvaluationSignoff.
@param BossEvaluationSignoff BossEvaluationSignoff */
public void setBossEvaluationSignoff (Timestamp BossEvaluationSignoff)
{
set_Value ("BossEvaluationSignoff", BossEvaluationSignoff);
}
/** Get BossEvaluationSignoff.
@return BossEvaluationSignoff */
public Timestamp getBossEvaluationSignoff() 
{
return (Timestamp)get_Value("BossEvaluationSignoff");
}
/** Set BossExpectencySignoff.
@param BossExpectencySignoff BossExpectencySignoff */
public void setBossExpectencySignoff (Timestamp BossExpectencySignoff)
{
set_Value ("BossExpectencySignoff", BossExpectencySignoff);
}
/** Get BossExpectencySignoff.
@return BossExpectencySignoff */
public Timestamp getBossExpectencySignoff() 
{
return (Timestamp)get_Value("BossExpectencySignoff");
}
/** Set Closed.
@param Closed Closed */
public void setClosed (boolean Closed)
{
set_Value ("Closed", new Boolean(Closed));
}
/** Get Closed.
@return Closed */
public boolean isClosed() 
{
Object oo = get_Value("Closed");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Cr.
@param CR Cr */
public void setCR (String CR)
{
if (CR != null && CR.length() > 4)
{
log.warning("Length > 4 - truncated");
CR = CR.substring(0,3);
}
set_Value ("CR", CR);
}
/** Get Cr.
@return Cr */
public String getCR() 
{
return (String)get_Value("CR");
}
/** Set Employee Commentary.
@param EmployeeCommentary Employee Commentary */
public void setEmployeeCommentary (String EmployeeCommentary)
{
if (EmployeeCommentary != null && EmployeeCommentary.length() > 1024)
{
log.warning("Length > 1024 - truncated");
EmployeeCommentary = EmployeeCommentary.substring(0,1023);
}
set_Value ("EmployeeCommentary", EmployeeCommentary);
}
/** Get Employee Commentary.
@return Employee Commentary */
public String getEmployeeCommentary() 
{
return (String)get_Value("EmployeeCommentary");
}
/** Set EmployeeEvaluationSignoff.
@param EmployeeEvaluationSignoff EmployeeEvaluationSignoff */
public void setEmployeeEvaluationSignoff (Timestamp EmployeeEvaluationSignoff)
{
set_Value ("EmployeeEvaluationSignoff", EmployeeEvaluationSignoff);
}
/** Get EmployeeEvaluationSignoff.
@return EmployeeEvaluationSignoff */
public Timestamp getEmployeeEvaluationSignoff() 
{
return (Timestamp)get_Value("EmployeeEvaluationSignoff");
}
/** Set EmployeeExpectencySignoff.
@param EmployeeExpectencySignoff EmployeeExpectencySignoff */
public void setEmployeeExpectencySignoff (Timestamp EmployeeExpectencySignoff)
{
set_Value ("EmployeeExpectencySignoff", EmployeeExpectencySignoff);
}
/** Get EmployeeExpectencySignoff.
@return EmployeeExpectencySignoff */
public Timestamp getEmployeeExpectencySignoff() 
{
return (Timestamp)get_Value("EmployeeExpectencySignoff");
}
/** Set Ending Reference Period Date.
@param EndingRefPeriodDate Ending Reference Period Date */
public void setEndingRefPeriodDate (Timestamp EndingRefPeriodDate)
{
set_Value ("EndingRefPeriodDate", EndingRefPeriodDate);
}
/** Get Ending Reference Period Date.
@return Ending Reference Period Date */
public Timestamp getEndingRefPeriodDate() 
{
return (Timestamp)get_Value("EndingRefPeriodDate");
}
/** Set Evaluation Date.
@param EvaluationDate Evaluation Date */
public void setEvaluationDate (Timestamp EvaluationDate)
{
if (EvaluationDate == null) throw new IllegalArgumentException ("EvaluationDate is mandatory.");
set_Value ("EvaluationDate", EvaluationDate);
}
/** Get Evaluation Date.
@return Evaluation Date */
public Timestamp getEvaluationDate() 
{
return (Timestamp)get_Value("EvaluationDate");
}
/** Set Evaluation Type.
@param EvaluationType Evaluation Type */
public void setEvaluationType (String EvaluationType)
{
if (EvaluationType == null) throw new IllegalArgumentException ("EvaluationType is mandatory.");
if (EvaluationType.length() > 3)
{
log.warning("Length > 3 - truncated");
EvaluationType = EvaluationType.substring(0,2);
}
set_Value ("EvaluationType", EvaluationType);
}
/** Get Evaluation Type.
@return Evaluation Type */
public String getEvaluationType() 
{
return (String)get_Value("EvaluationType");
}
/** Set Global Quotation.
@param GlobalQuotation Global Quotation */
public void setGlobalQuotation (String GlobalQuotation)
{
if (GlobalQuotation != null && GlobalQuotation.length() > 3)
{
log.warning("Length > 3 - truncated");
GlobalQuotation = GlobalQuotation.substring(0,2);
}
set_Value ("GlobalQuotation", GlobalQuotation);
}
/** Get Global Quotation.
@return Global Quotation */
public String getGlobalQuotation() 
{
return (String)get_Value("GlobalQuotation");
}
/** Set Global Quotation Reason.
@param GlobalQuotationReason Global Quotation Reason */
public void setGlobalQuotationReason (String GlobalQuotationReason)
{
if (GlobalQuotationReason != null && GlobalQuotationReason.length() > 1024)
{
log.warning("Length > 1024 - truncated");
GlobalQuotationReason = GlobalQuotationReason.substring(0,1023);
}
set_Value ("GlobalQuotationReason", GlobalQuotationReason);
}
/** Get Global Quotation Reason.
@return Global Quotation Reason */
public String getGlobalQuotationReason() 
{
return (String)get_Value("GlobalQuotationReason");
}
/** Set IsExpectationCompleted.
@param IsExpectationCompleted IsExpectationCompleted */
public void setIsExpectationCompleted (boolean IsExpectationCompleted)
{
set_Value ("IsExpectationCompleted", new Boolean(IsExpectationCompleted));
}
/** Get IsExpectationCompleted.
@return IsExpectationCompleted */
public boolean isExpectationCompleted() 
{
Object oo = get_Value("IsExpectationCompleted");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsProbation.
@param IsProbation IsProbation */
public void setIsProbation (boolean IsProbation)
{
set_Value ("IsProbation", new Boolean(IsProbation));
}
/** Get IsProbation.
@return IsProbation */
public boolean isProbation() 
{
Object oo = get_Value("IsProbation");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Other employee's realisation.
@param OtherEmployeeRealisation Other employee's realisation */
public void setOtherEmployeeRealisation (String OtherEmployeeRealisation)
{
if (OtherEmployeeRealisation != null && OtherEmployeeRealisation.length() > 1024)
{
log.warning("Length > 1024 - truncated");
OtherEmployeeRealisation = OtherEmployeeRealisation.substring(0,1023);
}
set_Value ("OtherEmployeeRealisation", OtherEmployeeRealisation);
}
/** Get Other employee's realisation.
@return Other employee's realisation */
public String getOtherEmployeeRealisation() 
{
return (String)get_Value("OtherEmployeeRealisation");
}

/** P_Employee_Gest_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_GEST_ID_AD_Reference_ID=2000010;
/** Set Manager.
@param P_Employee_Gest_ID Manager */
public void setP_Employee_Gest_ID (int P_Employee_Gest_ID)
{
if (P_Employee_Gest_ID < 1) throw new IllegalArgumentException ("P_Employee_Gest_ID is mandatory.");
set_Value ("P_Employee_Gest_ID", new Integer(P_Employee_Gest_ID));
}
/** Get Manager.
@return Manager */
public int getP_Employee_Gest_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Gest_ID");
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
/** Set Work performance evaluation.
@param P_ER_Evaluation_ID Work performance evaluation */
public void setP_ER_Evaluation_ID (int P_ER_Evaluation_ID)
{
if (P_ER_Evaluation_ID < 1) throw new IllegalArgumentException ("P_ER_Evaluation_ID is mandatory.");
set_ValueNoCheck ("P_ER_Evaluation_ID", new Integer(P_ER_Evaluation_ID));
}
/** Get Work performance evaluation.
@return Work performance evaluation */
public int getP_ER_Evaluation_ID() 
{
Integer ii = (Integer)get_Value("P_ER_Evaluation_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Work performance evaluation form.
@param P_ER_Form_ID Work performance evaluation form */
public void setP_ER_Form_ID (int P_ER_Form_ID)
{
if (P_ER_Form_ID < 1) throw new IllegalArgumentException ("P_ER_Form_ID is mandatory.");
set_Value ("P_ER_Form_ID", new Integer(P_ER_Form_ID));
}
/** Get Work performance evaluation form.
@return Work performance evaluation form */
public int getP_ER_Form_ID() 
{
Integer ii = (Integer)get_Value("P_ER_Form_ID");
if (ii == null) return 0;
return ii.intValue();
}
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
/** Set Recommendation.
@param Recommendation Recommendation */
public void setRecommendation (String Recommendation)
{
if (Recommendation != null && Recommendation.length() > 1)
{
log.warning("Length > 1 - truncated");
Recommendation = Recommendation.substring(0,0);
}
set_Value ("Recommendation", Recommendation);
}
/** Get Recommendation.
@return Recommendation */
public String getRecommendation() 
{
return (String)get_Value("Recommendation");
}
/** Set Starting Reference Period Date.
@param StartingRefPeriodDate Starting Reference Period Date */
public void setStartingRefPeriodDate (Timestamp StartingRefPeriodDate)
{
set_Value ("StartingRefPeriodDate", StartingRefPeriodDate);
}
/** Get Starting Reference Period Date.
@return Starting Reference Period Date */
public Timestamp getStartingRefPeriodDate() 
{
return (Timestamp)get_Value("StartingRefPeriodDate");
}
/** Set Starting Work Date.
@param StartingWorkDate Starting Work Date */
public void setStartingWorkDate (Timestamp StartingWorkDate)
{
set_Value ("StartingWorkDate", StartingWorkDate);
}
/** Get Starting Work Date.
@return Starting Work Date */
public Timestamp getStartingWorkDate() 
{
return (Timestamp)get_Value("StartingWorkDate");
}
/** Set Una.
@param UNA Una */
public void setUNA (String UNA)
{
if (UNA != null && UNA.length() > 60)
{
log.warning("Length > 60 - truncated");
UNA = UNA.substring(0,59);
}
set_Value ("UNA", UNA);
}
/** Get Una.
@return Una */
public String getUNA() 
{
return (String)get_Value("UNA");
}
/** Set Year.
@param Year Calendar Year */
public void setYear (int Year)
{
set_Value ("Year", new Integer(Year));
}
/** Get Year.
@return Calendar Year */
public int getYear() 
{
Integer ii = (Integer)get_Value("Year");
if (ii == null) return 0;
return ii.intValue();
}
}
