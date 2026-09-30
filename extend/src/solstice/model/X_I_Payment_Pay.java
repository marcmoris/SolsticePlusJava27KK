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
/** Generated Model for I_Payment_Pay
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_I_Payment_Pay extends PO
{
/** Standard Constructor
@param ctx context
@param I_Payment_Pay_ID id
@param trxName transaction
*/
public X_I_Payment_Pay (Properties ctx, int I_Payment_Pay_ID, String trxName)
{
super (ctx, I_Payment_Pay_ID, trxName);
/** if (I_Payment_Pay_ID == 0)
{
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_I_Payment_Pay (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201172 */
public static final int Table_ID=3201172;

/** TableName=I_Payment_Pay */
public static final String Table_Name="I_Payment_Pay";

protected static KeyNamePair Model = new KeyNamePair(3201172,"I_Payment_Pay");

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
StringBuffer sb = new StringBuffer ("X_I_Payment_Pay[").append(get_ID()).append("]");
return sb.toString();
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
/** Set I_PAYMENT_PAY_ID.
@param I_PAYMENT_PAY_ID I_PAYMENT_PAY_ID */
public void setI_PAYMENT_PAY_ID (int I_PAYMENT_PAY_ID)
{
if (I_PAYMENT_PAY_ID <= 0) set_ValueNoCheck ("I_PAYMENT_PAY_ID", null);
 else 
set_ValueNoCheck ("I_PAYMENT_PAY_ID", new Integer(I_PAYMENT_PAY_ID));
}
/** Get I_PAYMENT_PAY_ID.
@return I_PAYMENT_PAY_ID */
public int getI_PAYMENT_PAY_ID() 
{
Integer ii = (Integer)get_Value("I_PAYMENT_PAY_ID");
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
/** Set PAY_CHQ_ANNULATION.
@param PAY_CHQ_ANNULATION PAY_CHQ_ANNULATION */
public void setPAY_CHQ_ANNULATION (boolean PAY_CHQ_ANNULATION)
{
set_Value ("PAY_CHQ_ANNULATION", new Boolean(PAY_CHQ_ANNULATION));
}
/** Get PAY_CHQ_ANNULATION.
@return PAY_CHQ_ANNULATION */
public boolean isPAY_CHQ_ANNULATION() 
{
Object oo = get_Value("PAY_CHQ_ANNULATION");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set PAY_CHQ_DATE.
@param PAY_CHQ_DATE PAY_CHQ_DATE */
public void setPAY_CHQ_DATE (int PAY_CHQ_DATE)
{
set_Value ("PAY_CHQ_DATE", new Integer(PAY_CHQ_DATE));
}
/** Get PAY_CHQ_DATE.
@return PAY_CHQ_DATE */
public int getPAY_CHQ_DATE() 
{
Integer ii = (Integer)get_Value("PAY_CHQ_DATE");
if (ii == null) return 0;
return ii.intValue();
}
/** Set PAY_CHQ_DATE_CONC.
@param PAY_CHQ_DATE_CONC PAY_CHQ_DATE_CONC */
public void setPAY_CHQ_DATE_CONC (int PAY_CHQ_DATE_CONC)
{
set_Value ("PAY_CHQ_DATE_CONC", new Integer(PAY_CHQ_DATE_CONC));
}
/** Get PAY_CHQ_DATE_CONC.
@return PAY_CHQ_DATE_CONC */
public int getPAY_CHQ_DATE_CONC() 
{
Integer ii = (Integer)get_Value("PAY_CHQ_DATE_CONC");
if (ii == null) return 0;
return ii.intValue();
}
/** Set PAY_CHQ_FILLER.
@param PAY_CHQ_FILLER PAY_CHQ_FILLER */
public void setPAY_CHQ_FILLER (String PAY_CHQ_FILLER)
{
if (PAY_CHQ_FILLER != null && PAY_CHQ_FILLER.length() > 7)
{
log.warning("Length > 7 - truncated");
PAY_CHQ_FILLER = PAY_CHQ_FILLER.substring(0,6);
}
set_Value ("PAY_CHQ_FILLER", PAY_CHQ_FILLER);
}
/** Get PAY_CHQ_FILLER.
@return PAY_CHQ_FILLER */
public String getPAY_CHQ_FILLER() 
{
return (String)get_Value("PAY_CHQ_FILLER");
}
/** Set PAY_CHQ_GENRE.
@param PAY_CHQ_GENRE PAY_CHQ_GENRE */
public void setPAY_CHQ_GENRE (String PAY_CHQ_GENRE)
{
if (PAY_CHQ_GENRE != null && PAY_CHQ_GENRE.length() > 1)
{
log.warning("Length > 1 - truncated");
PAY_CHQ_GENRE = PAY_CHQ_GENRE.substring(0,0);
}
set_Value ("PAY_CHQ_GENRE", PAY_CHQ_GENRE);
}
/** Get PAY_CHQ_GENRE.
@return PAY_CHQ_GENRE */
public String getPAY_CHQ_GENRE() 
{
return (String)get_Value("PAY_CHQ_GENRE");
}
/** Set PAY_CHQ_HRE_1.
@param PAY_CHQ_HRE_1 PAY_CHQ_HRE_1 */
public void setPAY_CHQ_HRE_1 (BigDecimal PAY_CHQ_HRE_1)
{
set_Value ("PAY_CHQ_HRE_1", PAY_CHQ_HRE_1);
}
/** Get PAY_CHQ_HRE_1.
@return PAY_CHQ_HRE_1 */
public BigDecimal getPAY_CHQ_HRE_1() 
{
BigDecimal bd = (BigDecimal)get_Value("PAY_CHQ_HRE_1");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PAY_CHQ_HRE_2.
@param PAY_CHQ_HRE_2 PAY_CHQ_HRE_2 */
public void setPAY_CHQ_HRE_2 (BigDecimal PAY_CHQ_HRE_2)
{
set_Value ("PAY_CHQ_HRE_2", PAY_CHQ_HRE_2);
}
/** Get PAY_CHQ_HRE_2.
@return PAY_CHQ_HRE_2 */
public BigDecimal getPAY_CHQ_HRE_2() 
{
BigDecimal bd = (BigDecimal)get_Value("PAY_CHQ_HRE_2");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PAY_CHQ_IMPRIME.
@param PAY_CHQ_IMPRIME PAY_CHQ_IMPRIME */
public void setPAY_CHQ_IMPRIME (boolean PAY_CHQ_IMPRIME)
{
set_Value ("PAY_CHQ_IMPRIME", new Boolean(PAY_CHQ_IMPRIME));
}
/** Get PAY_CHQ_IMPRIME.
@return PAY_CHQ_IMPRIME */
public boolean isPAY_CHQ_IMPRIME() 
{
Object oo = get_Value("PAY_CHQ_IMPRIME");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set PAY_CHQ_MNT_1.
@param PAY_CHQ_MNT_1 PAY_CHQ_MNT_1 */
public void setPAY_CHQ_MNT_1 (BigDecimal PAY_CHQ_MNT_1)
{
set_Value ("PAY_CHQ_MNT_1", PAY_CHQ_MNT_1);
}
/** Get PAY_CHQ_MNT_1.
@return PAY_CHQ_MNT_1 */
public BigDecimal getPAY_CHQ_MNT_1() 
{
BigDecimal bd = (BigDecimal)get_Value("PAY_CHQ_MNT_1");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PAY_CHQ_MNT_2.
@param PAY_CHQ_MNT_2 PAY_CHQ_MNT_2 */
public void setPAY_CHQ_MNT_2 (BigDecimal PAY_CHQ_MNT_2)
{
set_Value ("PAY_CHQ_MNT_2", PAY_CHQ_MNT_2);
}
/** Get PAY_CHQ_MNT_2.
@return PAY_CHQ_MNT_2 */
public BigDecimal getPAY_CHQ_MNT_2() 
{
BigDecimal bd = (BigDecimal)get_Value("PAY_CHQ_MNT_2");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PAY_CHQ_MNT_NET.
@param PAY_CHQ_MNT_NET PAY_CHQ_MNT_NET */
public void setPAY_CHQ_MNT_NET (BigDecimal PAY_CHQ_MNT_NET)
{
set_Value ("PAY_CHQ_MNT_NET", PAY_CHQ_MNT_NET);
}
/** Get PAY_CHQ_MNT_NET.
@return PAY_CHQ_MNT_NET */
public BigDecimal getPAY_CHQ_MNT_NET() 
{
BigDecimal bd = (BigDecimal)get_Value("PAY_CHQ_MNT_NET");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PAY_CHQ_NB_SEM.
@param PAY_CHQ_NB_SEM PAY_CHQ_NB_SEM */
public void setPAY_CHQ_NB_SEM (int PAY_CHQ_NB_SEM)
{
set_Value ("PAY_CHQ_NB_SEM", new Integer(PAY_CHQ_NB_SEM));
}
/** Get PAY_CHQ_NB_SEM.
@return PAY_CHQ_NB_SEM */
public int getPAY_CHQ_NB_SEM() 
{
Integer ii = (Integer)get_Value("PAY_CHQ_NB_SEM");
if (ii == null) return 0;
return ii.intValue();
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
/** Set PAY_CHQ_NO_CHQ_ANNULE.
@param PAY_CHQ_NO_CHQ_ANNULE PAY_CHQ_NO_CHQ_ANNULE */
public void setPAY_CHQ_NO_CHQ_ANNULE (String PAY_CHQ_NO_CHQ_ANNULE)
{
if (PAY_CHQ_NO_CHQ_ANNULE != null && PAY_CHQ_NO_CHQ_ANNULE.length() > 10)
{
log.warning("Length > 10 - truncated");
PAY_CHQ_NO_CHQ_ANNULE = PAY_CHQ_NO_CHQ_ANNULE.substring(0,9);
}
set_Value ("PAY_CHQ_NO_CHQ_ANNULE", PAY_CHQ_NO_CHQ_ANNULE);
}
/** Get PAY_CHQ_NO_CHQ_ANNULE.
@return PAY_CHQ_NO_CHQ_ANNULE */
public String getPAY_CHQ_NO_CHQ_ANNULE() 
{
return (String)get_Value("PAY_CHQ_NO_CHQ_ANNULE");
}
/** Set PAY_CHQ_PER_TRAITE.
@param PAY_CHQ_PER_TRAITE PAY_CHQ_PER_TRAITE */
public void setPAY_CHQ_PER_TRAITE (String PAY_CHQ_PER_TRAITE)
{
if (PAY_CHQ_PER_TRAITE != null && PAY_CHQ_PER_TRAITE.length() > 6)
{
log.warning("Length > 6 - truncated");
PAY_CHQ_PER_TRAITE = PAY_CHQ_PER_TRAITE.substring(0,5);
}
set_Value ("PAY_CHQ_PER_TRAITE", PAY_CHQ_PER_TRAITE);
}
/** Get PAY_CHQ_PER_TRAITE.
@return PAY_CHQ_PER_TRAITE */
public String getPAY_CHQ_PER_TRAITE() 
{
return (String)get_Value("PAY_CHQ_PER_TRAITE");
}
/** Set PAY_CHQ_PROVISION_VAC.
@param PAY_CHQ_PROVISION_VAC PAY_CHQ_PROVISION_VAC */
public void setPAY_CHQ_PROVISION_VAC (BigDecimal PAY_CHQ_PROVISION_VAC)
{
set_Value ("PAY_CHQ_PROVISION_VAC", PAY_CHQ_PROVISION_VAC);
}
/** Get PAY_CHQ_PROVISION_VAC.
@return PAY_CHQ_PROVISION_VAC */
public BigDecimal getPAY_CHQ_PROVISION_VAC() 
{
BigDecimal bd = (BigDecimal)get_Value("PAY_CHQ_PROVISION_VAC");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PAY_CHQ_REMUN_ASS.
@param PAY_CHQ_REMUN_ASS PAY_CHQ_REMUN_ASS */
public void setPAY_CHQ_REMUN_ASS (BigDecimal PAY_CHQ_REMUN_ASS)
{
set_Value ("PAY_CHQ_REMUN_ASS", PAY_CHQ_REMUN_ASS);
}
/** Get PAY_CHQ_REMUN_ASS.
@return PAY_CHQ_REMUN_ASS */
public BigDecimal getPAY_CHQ_REMUN_ASS() 
{
BigDecimal bd = (BigDecimal)get_Value("PAY_CHQ_REMUN_ASS");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PAY_CHQ_TYPE.
@param PAY_CHQ_TYPE PAY_CHQ_TYPE */
public void setPAY_CHQ_TYPE (String PAY_CHQ_TYPE)
{
if (PAY_CHQ_TYPE != null && PAY_CHQ_TYPE.length() > 2)
{
log.warning("Length > 2 - truncated");
PAY_CHQ_TYPE = PAY_CHQ_TYPE.substring(0,1);
}
set_Value ("PAY_CHQ_TYPE", PAY_CHQ_TYPE);
}
/** Get PAY_CHQ_TYPE.
@return PAY_CHQ_TYPE */
public String getPAY_CHQ_TYPE() 
{
return (String)get_Value("PAY_CHQ_TYPE");
}
/** Set PAY_EMP_NO.
@param PAY_EMP_NO PAY_EMP_NO */
public void setPAY_EMP_NO (String PAY_EMP_NO)
{
if (PAY_EMP_NO != null && PAY_EMP_NO.length() > 6)
{
log.warning("Length > 6 - truncated");
PAY_EMP_NO = PAY_EMP_NO.substring(0,5);
}
set_Value ("PAY_EMP_NO", PAY_EMP_NO);
}
/** Get PAY_EMP_NO.
@return PAY_EMP_NO */
public String getPAY_EMP_NO() 
{
return (String)get_Value("PAY_EMP_NO");
}
/** Set PAY_PER_NO.
@param PAY_PER_NO PAY_PER_NO */
public void setPAY_PER_NO (String PAY_PER_NO)
{
if (PAY_PER_NO != null && PAY_PER_NO.length() > 6)
{
log.warning("Length > 6 - truncated");
PAY_PER_NO = PAY_PER_NO.substring(0,5);
}
set_Value ("PAY_PER_NO", PAY_PER_NO);
}
/** Get PAY_PER_NO.
@return PAY_PER_NO */
public String getPAY_PER_NO() 
{
return (String)get_Value("PAY_PER_NO");
}
/** Set PAY_UNA_NO.
@param PAY_UNA_NO PAY_UNA_NO */
public void setPAY_UNA_NO (String PAY_UNA_NO)
{
if (PAY_UNA_NO != null && PAY_UNA_NO.length() > 7)
{
log.warning("Length > 7 - truncated");
PAY_UNA_NO = PAY_UNA_NO.substring(0,6);
}
set_Value ("PAY_UNA_NO", PAY_UNA_NO);
}
/** Get PAY_UNA_NO.
@return PAY_UNA_NO */
public String getPAY_UNA_NO() 
{
return (String)get_Value("PAY_UNA_NO");
}
/** Set PAY_UNP_CODE.
@param PAY_UNP_CODE PAY_UNP_CODE */
public void setPAY_UNP_CODE (String PAY_UNP_CODE)
{
if (PAY_UNP_CODE != null && PAY_UNP_CODE.length() > 4)
{
log.warning("Length > 4 - truncated");
PAY_UNP_CODE = PAY_UNP_CODE.substring(0,3);
}
set_Value ("PAY_UNP_CODE", PAY_UNP_CODE);
}
/** Get PAY_UNP_CODE.
@return PAY_UNP_CODE */
public String getPAY_UNP_CODE() 
{
return (String)get_Value("PAY_UNP_CODE");
}
/** Set Payment date.
@param PayDate Date Payment made */
public void setPayDate (Timestamp PayDate)
{
set_Value ("PayDate", PayDate);
}
/** Get Payment date.
@return Date Payment made */
public Timestamp getPayDate() 
{
return (Timestamp)get_Value("PayDate");
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
}
