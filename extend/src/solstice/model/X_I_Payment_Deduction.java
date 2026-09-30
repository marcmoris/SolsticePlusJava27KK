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
/** Generated Model for I_Payment_Deduction
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_I_Payment_Deduction extends PO
{
/** Standard Constructor
@param ctx context
@param I_Payment_Deduction_ID id
@param trxName transaction
*/
public X_I_Payment_Deduction (Properties ctx, int I_Payment_Deduction_ID, String trxName)
{
super (ctx, I_Payment_Deduction_ID, trxName);
/** if (I_Payment_Deduction_ID == 0)
{
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_I_Payment_Deduction (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201174 */
public static final int Table_ID=3201174;

/** TableName=I_Payment_Deduction */
public static final String Table_Name="I_Payment_Deduction";

protected static KeyNamePair Model = new KeyNamePair(3201174,"I_Payment_Deduction");

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
StringBuffer sb = new StringBuffer ("X_I_Payment_Deduction[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Accumulation .
@param AccumulationAmount Accumulation  */
public void setAccumulationAmount (BigDecimal AccumulationAmount)
{
set_Value ("AccumulationAmount", AccumulationAmount);
}
/** Get Accumulation .
@return Accumulation  */
public BigDecimal getAccumulationAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("AccumulationAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employee Part.
@param Employee_Part Employee Part */
public void setEmployee_Part (BigDecimal Employee_Part)
{
set_Value ("Employee_Part", Employee_Part);
}
/** Get Employee Part.
@return Employee Part */
public BigDecimal getEmployee_Part() 
{
BigDecimal bd = (BigDecimal)get_Value("Employee_Part");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employer Part.
@param Employer_Part Employer Part */
public void setEmployer_Part (BigDecimal Employer_Part)
{
set_Value ("Employer_Part", Employer_Part);
}
/** Get Employer Part.
@return Employer Part */
public BigDecimal getEmployer_Part() 
{
BigDecimal bd = (BigDecimal)get_Value("Employer_Part");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours Eligible.
@param Hours_Eligible Hours Eligible */
public void setHours_Eligible (BigDecimal Hours_Eligible)
{
set_Value ("Hours_Eligible", Hours_Eligible);
}
/** Get Hours Eligible.
@return Hours Eligible */
public BigDecimal getHours_Eligible() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours_Eligible");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Import Error Message.
@param I_ErrorMsg Messages generated from import process */
public void setI_ErrorMsg (String I_ErrorMsg)
{
if (I_ErrorMsg != null && I_ErrorMsg.length() > 2000)
{
log.warning("Length > 2000 - truncated");
I_ErrorMsg = I_ErrorMsg.substring(0,1999);
}
set_Value ("I_ErrorMsg", I_ErrorMsg);
}
/** Get Import Error Message.
@return Messages generated from import process */
public String getI_ErrorMsg() 
{
return (String)get_Value("I_ErrorMsg");
}
/** Set Imported.
@param I_IsImported Has this import been processed */
public void setI_IsImported (boolean I_IsImported)
{
set_Value ("I_IsImported", new Boolean(I_IsImported));
}
/** Get Imported.
@return Has this import been processed */
public boolean isI_IsImported() 
{
Object oo = get_Value("I_IsImported");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set I_PAYMENT_DEDUCTION_ID.
@param I_PAYMENT_DEDUCTION_ID I_PAYMENT_DEDUCTION_ID */
public void setI_PAYMENT_DEDUCTION_ID (int I_PAYMENT_DEDUCTION_ID)
{
if (I_PAYMENT_DEDUCTION_ID <= 0) set_ValueNoCheck ("I_PAYMENT_DEDUCTION_ID", null);
 else 
set_ValueNoCheck ("I_PAYMENT_DEDUCTION_ID", new Integer(I_PAYMENT_DEDUCTION_ID));
}
/** Get I_PAYMENT_DEDUCTION_ID.
@return I_PAYMENT_DEDUCTION_ID */
public int getI_PAYMENT_DEDUCTION_ID() 
{
Integer ii = (Integer)get_Value("I_PAYMENT_DEDUCTION_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set I_PAYMENT_PAY_ID.
@param I_PAYMENT_PAY_ID I_PAYMENT_PAY_ID */
public void setI_PAYMENT_PAY_ID (int I_PAYMENT_PAY_ID)
{
if (I_PAYMENT_PAY_ID <= 0) set_Value ("I_PAYMENT_PAY_ID", null);
 else 
set_Value ("I_PAYMENT_PAY_ID", new Integer(I_PAYMENT_PAY_ID));
}
/** Get I_PAYMENT_PAY_ID.
@return I_PAYMENT_PAY_ID */
public int getI_PAYMENT_PAY_ID() 
{
Integer ii = (Integer)get_Value("I_PAYMENT_PAY_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Deduction.
@param P_Deduction_ID   */
public void setP_Deduction_ID (int P_Deduction_ID)
{
if (P_Deduction_ID <= 0) set_Value ("P_Deduction_ID", null);
 else 
set_Value ("P_Deduction_ID", new Integer(P_Deduction_ID));
}
/** Get Deduction.
@return   */
public int getP_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_ID");
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
/** Set Payment.
@param P_Payment_ID Payment */
public void setP_Payment_ID (int P_Payment_ID)
{
if (P_Payment_ID <= 0) set_Value ("P_Payment_ID", null);
 else 
set_Value ("P_Payment_ID", new Integer(P_Payment_ID));
}
/** Get Payment.
@return Payment */
public int getP_Payment_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Period.
@param P_Period_ID Period of the Calendar   */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID <= 0) set_Value ("P_Period_ID", null);
 else 
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
/** Set Year.
@param P_Year_ID Calendar Year */
public void setP_Year_ID (int P_Year_ID)
{
if (P_Year_ID <= 0) set_Value ("P_Year_ID", null);
 else 
set_Value ("P_Year_ID", new Integer(P_Year_ID));
}
/** Get Year.
@return Calendar Year */
public int getP_Year_ID() 
{
Integer ii = (Integer)get_Value("P_Year_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set PAY_CHD_HRE_ADMIS_1.
@param PAY_CHD_HRE_ADMIS_1 PAY_CHD_HRE_ADMIS_1 */
public void setPAY_CHD_HRE_ADMIS_1 (BigDecimal PAY_CHD_HRE_ADMIS_1)
{
set_Value ("PAY_CHD_HRE_ADMIS_1", PAY_CHD_HRE_ADMIS_1);
}
/** Get PAY_CHD_HRE_ADMIS_1.
@return PAY_CHD_HRE_ADMIS_1 */
public BigDecimal getPAY_CHD_HRE_ADMIS_1() 
{
BigDecimal bd = (BigDecimal)get_Value("PAY_CHD_HRE_ADMIS_1");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PAY_CHD_HRE_ADMIS_2.
@param PAY_CHD_HRE_ADMIS_2 PAY_CHD_HRE_ADMIS_2 */
public void setPAY_CHD_HRE_ADMIS_2 (BigDecimal PAY_CHD_HRE_ADMIS_2)
{
set_Value ("PAY_CHD_HRE_ADMIS_2", PAY_CHD_HRE_ADMIS_2);
}
/** Get PAY_CHD_HRE_ADMIS_2.
@return PAY_CHD_HRE_ADMIS_2 */
public BigDecimal getPAY_CHD_HRE_ADMIS_2() 
{
BigDecimal bd = (BigDecimal)get_Value("PAY_CHD_HRE_ADMIS_2");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PAY_CHD_MNT.
@param PAY_CHD_MNT PAY_CHD_MNT */
public void setPAY_CHD_MNT (BigDecimal PAY_CHD_MNT)
{
set_Value ("PAY_CHD_MNT", PAY_CHD_MNT);
}
/** Get PAY_CHD_MNT.
@return PAY_CHD_MNT */
public BigDecimal getPAY_CHD_MNT() 
{
BigDecimal bd = (BigDecimal)get_Value("PAY_CHD_MNT");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PAY_CHD_MNT_ADMIS_1.
@param PAY_CHD_MNT_ADMIS_1 PAY_CHD_MNT_ADMIS_1 */
public void setPAY_CHD_MNT_ADMIS_1 (BigDecimal PAY_CHD_MNT_ADMIS_1)
{
set_Value ("PAY_CHD_MNT_ADMIS_1", PAY_CHD_MNT_ADMIS_1);
}
/** Get PAY_CHD_MNT_ADMIS_1.
@return PAY_CHD_MNT_ADMIS_1 */
public BigDecimal getPAY_CHD_MNT_ADMIS_1() 
{
BigDecimal bd = (BigDecimal)get_Value("PAY_CHD_MNT_ADMIS_1");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PAY_CHD_MNT_ADMIS_2.
@param PAY_CHD_MNT_ADMIS_2 PAY_CHD_MNT_ADMIS_2 */
public void setPAY_CHD_MNT_ADMIS_2 (BigDecimal PAY_CHD_MNT_ADMIS_2)
{
set_Value ("PAY_CHD_MNT_ADMIS_2", PAY_CHD_MNT_ADMIS_2);
}
/** Get PAY_CHD_MNT_ADMIS_2.
@return PAY_CHD_MNT_ADMIS_2 */
public BigDecimal getPAY_CHD_MNT_ADMIS_2() 
{
BigDecimal bd = (BigDecimal)get_Value("PAY_CHD_MNT_ADMIS_2");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PAY_CHD_MNT_CONT.
@param PAY_CHD_MNT_CONT PAY_CHD_MNT_CONT */
public void setPAY_CHD_MNT_CONT (BigDecimal PAY_CHD_MNT_CONT)
{
set_Value ("PAY_CHD_MNT_CONT", PAY_CHD_MNT_CONT);
}
/** Get PAY_CHD_MNT_CONT.
@return PAY_CHD_MNT_CONT */
public BigDecimal getPAY_CHD_MNT_CONT() 
{
BigDecimal bd = (BigDecimal)get_Value("PAY_CHD_MNT_CONT");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PAY_CHD_MNT_NON_PREL.
@param PAY_CHD_MNT_NON_PREL PAY_CHD_MNT_NON_PREL */
public void setPAY_CHD_MNT_NON_PREL (BigDecimal PAY_CHD_MNT_NON_PREL)
{
set_Value ("PAY_CHD_MNT_NON_PREL", PAY_CHD_MNT_NON_PREL);
}
/** Get PAY_CHD_MNT_NON_PREL.
@return PAY_CHD_MNT_NON_PREL */
public BigDecimal getPAY_CHD_MNT_NON_PREL() 
{
BigDecimal bd = (BigDecimal)get_Value("PAY_CHD_MNT_NON_PREL");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PAY_CHD_MNT_RECUPERE.
@param PAY_CHD_MNT_RECUPERE PAY_CHD_MNT_RECUPERE */
public void setPAY_CHD_MNT_RECUPERE (BigDecimal PAY_CHD_MNT_RECUPERE)
{
set_Value ("PAY_CHD_MNT_RECUPERE", PAY_CHD_MNT_RECUPERE);
}
/** Get PAY_CHD_MNT_RECUPERE.
@return PAY_CHD_MNT_RECUPERE */
public BigDecimal getPAY_CHD_MNT_RECUPERE() 
{
BigDecimal bd = (BigDecimal)get_Value("PAY_CHD_MNT_RECUPERE");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PAY_CHQ_NO.
@param PAY_CHQ_NO PAY_CHQ_NO */
public void setPAY_CHQ_NO (String PAY_CHQ_NO)
{
if (PAY_CHQ_NO != null && PAY_CHQ_NO.length() > 10)
{
log.warning("Length > 10 - truncated");
PAY_CHQ_NO = PAY_CHQ_NO.substring(0,9);
}
set_Value ("PAY_CHQ_NO", PAY_CHQ_NO);
}
/** Get PAY_CHQ_NO.
@return PAY_CHQ_NO */
public String getPAY_CHQ_NO() 
{
return (String)get_Value("PAY_CHQ_NO");
}
/** Set PAY_DED_CODE.
@param PAY_DED_CODE PAY_DED_CODE */
public void setPAY_DED_CODE (String PAY_DED_CODE)
{
if (PAY_DED_CODE != null && PAY_DED_CODE.length() > 4)
{
log.warning("Length > 4 - truncated");
PAY_DED_CODE = PAY_DED_CODE.substring(0,3);
}
set_Value ("PAY_DED_CODE", PAY_DED_CODE);
}
/** Get PAY_DED_CODE.
@return PAY_DED_CODE */
public String getPAY_DED_CODE() 
{
return (String)get_Value("PAY_DED_CODE");
}
/** Set Processed.
@param Processed The document has been processed */
public void setProcessed (boolean Processed)
{
set_Value ("Processed", new Boolean(Processed));
}
/** Get Processed.
@return The document has been processed */
public boolean isProcessed() 
{
Object oo = get_Value("Processed");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Process Now.
@param Processing Process Now */
public void setProcessing (boolean Processing)
{
set_Value ("Processing", new Boolean(Processing));
}
/** Get Process Now.
@return Process Now */
public boolean isProcessing() 
{
Object oo = get_Value("Processing");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set PROJET.
@param PROJET PROJET */
public void setPROJET (String PROJET)
{
if (PROJET != null && PROJET.length() > 4)
{
log.warning("Length > 4 - truncated");
PROJET = PROJET.substring(0,3);
}
set_Value ("PROJET", PROJET);
}
/** Get PROJET.
@return PROJET */
public String getPROJET() 
{
return (String)get_Value("PROJET");
}
/** Set Retrieval.
@param RetrievalAmount Retrieval */
public void setRetrievalAmount (BigDecimal RetrievalAmount)
{
set_Value ("RetrievalAmount", RetrievalAmount);
}
/** Get Retrieval.
@return Retrieval */
public BigDecimal getRetrievalAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("RetrievalAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Salary Eligible.
@param Salary_Eligible Salary Eligible */
public void setSalary_Eligible (BigDecimal Salary_Eligible)
{
set_Value ("Salary_Eligible", Salary_Eligible);
}
/** Get Salary Eligible.
@return Salary Eligible */
public BigDecimal getSalary_Eligible() 
{
BigDecimal bd = (BigDecimal)get_Value("Salary_Eligible");
if (bd == null) return Env.ZERO;
return bd;
}
}
