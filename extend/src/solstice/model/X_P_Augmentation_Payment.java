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
/** Generated Model for P_Augmentation_Payment
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Augmentation_Payment extends PO
{
/** Standard Constructor
@param ctx context
@param P_Augmentation_Payment_ID id
@param trxName transaction
*/
public X_P_Augmentation_Payment (Properties ctx, int P_Augmentation_Payment_ID, String trxName)
{
super (ctx, P_Augmentation_Payment_ID, trxName);
/** if (P_Augmentation_Payment_ID == 0)
{
setP_Augmentation_ID (0);
setP_Augmentation_Payment_ID (0);
setP_Payment_Gain_ID (0);
setP_Payment_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Augmentation_Payment (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100795 */
public static final int Table_ID=2100795;

/** TableName=P_Augmentation_Payment */
public static final String Table_Name="P_Augmentation_Payment";

protected static KeyNamePair Model = new KeyNamePair(2100795,"P_Augmentation_Payment");

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
StringBuffer sb = new StringBuffer ("X_P_Augmentation_Payment[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Amount calculed.
@param AmountCalc Amount calculed */
public void setAmountCalc (BigDecimal AmountCalc)
{
throw new IllegalArgumentException ("AmountCalc is virtual column");
}
/** Get Amount calculed.
@return Amount calculed */
public BigDecimal getAmountCalc() 
{
BigDecimal bd = (BigDecimal)get_Value("AmountCalc");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Modified amount.
@param AmountModified Modified amount */
public void setAmountModified (BigDecimal AmountModified)
{
set_Value ("AmountModified", AmountModified);
}
/** Get Modified amount.
@return Modified amount */
public BigDecimal getAmountModified() 
{
BigDecimal bd = (BigDecimal)get_Value("AmountModified");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set End Date.
@param EndDate Last effective date (inclusive) */
public void setEndDate (Timestamp EndDate)
{
throw new IllegalArgumentException ("EndDate is virtual column");
}
/** Get End Date.
@return Last effective date (inclusive) */
public Timestamp getEndDate() 
{
return (Timestamp)get_Value("EndDate");
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

/** P_Assignment_ID AD_Reference_ID=2000057 */
public static final int P_ASSIGNMENT_ID_AD_Reference_ID=2000057;
/** Set Assignment.
@param P_Assignment_ID Assignment */
public void setP_Assignment_ID (String P_Assignment_ID)
{
throw new IllegalArgumentException ("P_Assignment_ID is virtual column");
}
/** Get Assignment.
@return Assignment */
public String getP_Assignment_ID() 
{
return (String)get_Value("P_Assignment_ID");
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
/** Set P_Augmentation_Payment_ID.
@param P_Augmentation_Payment_ID P_Augmentation_Payment_ID */
public void setP_Augmentation_Payment_ID (int P_Augmentation_Payment_ID)
{
if (P_Augmentation_Payment_ID < 1) throw new IllegalArgumentException ("P_Augmentation_Payment_ID is mandatory.");
set_ValueNoCheck ("P_Augmentation_Payment_ID", new Integer(P_Augmentation_Payment_ID));
}
/** Get P_Augmentation_Payment_ID.
@return P_Augmentation_Payment_ID */
public int getP_Augmentation_Payment_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_Payment_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Augmentation_Result_ID.
@param P_Augmentation_Result_ID P_Augmentation_Result_ID */
public void setP_Augmentation_Result_ID (int P_Augmentation_Result_ID)
{
if (P_Augmentation_Result_ID <= 0) set_Value ("P_Augmentation_Result_ID", null);
 else 
set_Value ("P_Augmentation_Result_ID", new Integer(P_Augmentation_Result_ID));
}
/** Get P_Augmentation_Result_ID.
@return P_Augmentation_Result_ID */
public int getP_Augmentation_Result_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_Result_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_ID AD_Reference_ID=2100317 */
public static final int P_GAIN_ID_AD_Reference_ID=2100317;
/** Set Gain.
@param P_Gain_ID Gain */
public void setP_Gain_ID (String P_Gain_ID)
{
throw new IllegalArgumentException ("P_Gain_ID is virtual column");
}
/** Get Gain.
@return Gain */
public String getP_Gain_ID() 
{
return (String)get_Value("P_Gain_ID");
}

/** P_Job_Title_ID AD_Reference_ID=2000025 */
public static final int P_JOB_TITLE_ID_AD_Reference_ID=2000025;
/** Set Job Title.
@param P_Job_Title_ID   */
public void setP_Job_Title_ID (String P_Job_Title_ID)
{
throw new IllegalArgumentException ("P_Job_Title_ID is virtual column");
}
/** Get Job Title.
@return   */
public String getP_Job_Title_ID() 
{
return (String)get_Value("P_Job_Title_ID");
}
/** Set P_Payment_Gain_ID.
@param P_Payment_Gain_ID P_Payment_Gain_ID */
public void setP_Payment_Gain_ID (int P_Payment_Gain_ID)
{
if (P_Payment_Gain_ID < 1) throw new IllegalArgumentException ("P_Payment_Gain_ID is mandatory.");
set_Value ("P_Payment_Gain_ID", new Integer(P_Payment_Gain_ID));
}
/** Get P_Payment_Gain_ID.
@return P_Payment_Gain_ID */
public int getP_Payment_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Payment.
@param P_Payment_ID Payment */
public void setP_Payment_ID (int P_Payment_ID)
{
if (P_Payment_ID < 1) throw new IllegalArgumentException ("P_Payment_ID is mandatory.");
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

/** P_Period_ID AD_Reference_ID=2000009 */
public static final int P_PERIOD_ID_AD_Reference_ID=2000009;
/** Set Period.
@param P_Period_ID Period of the Calendar   */
public void setP_Period_ID (String P_Period_ID)
{
throw new IllegalArgumentException ("P_Period_ID is virtual column");
}
/** Get Period.
@return Period of the Calendar   */
public String getP_Period_ID() 
{
return (String)get_Value("P_Period_ID");
}
/** Set Salary Class.
@param P_Salary_Class_ID Salary Class */
public void setP_Salary_Class_ID (String P_Salary_Class_ID)
{
throw new IllegalArgumentException ("P_Salary_Class_ID is virtual column");
}
/** Get Salary Class.
@return Salary Class */
public String getP_Salary_Class_ID() 
{
return (String)get_Value("P_Salary_Class_ID");
}
/** Set Step.
@param P_Salary_Scale_Detail_ID   */
public void setP_Salary_Scale_Detail_ID (String P_Salary_Scale_Detail_ID)
{
throw new IllegalArgumentException ("P_Salary_Scale_Detail_ID is virtual column");
}
/** Get Step.
@return   */
public String getP_Salary_Scale_Detail_ID() 
{
return (String)get_Value("P_Salary_Scale_Detail_ID");
}
/** Set Time Sheet No..
@param P_Time_Sheet_ID   */
public void setP_Time_Sheet_ID (String P_Time_Sheet_ID)
{
throw new IllegalArgumentException ("P_Time_Sheet_ID is virtual column");
}
/** Get Time Sheet No..
@return   */
public String getP_Time_Sheet_ID() 
{
return (String)get_Value("P_Time_Sheet_ID");
}
/** Set Hourly Rate (payment).
@param PaymentHourly_Rate Hourly Rate (payment) */
public void setPaymentHourly_Rate (String PaymentHourly_Rate)
{
throw new IllegalArgumentException ("PaymentHourly_Rate is virtual column");
}
/** Get Hourly Rate (payment).
@return Hourly Rate (payment) */
public String getPaymentHourly_Rate() 
{
return (String)get_Value("PaymentHourly_Rate");
}
/** Set Percentage.
@param Percentage Percent of the entire amount */
public void setPercentage (BigDecimal Percentage)
{
set_Value ("Percentage", Percentage);
}
/** Get Percentage.
@return Percent of the entire amount */
public BigDecimal getPercentage() 
{
BigDecimal bd = (BigDecimal)get_Value("Percentage");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Quantity calculed.
@param QuantityCalc Quantity calculed */
public void setQuantityCalc (BigDecimal QuantityCalc)
{
throw new IllegalArgumentException ("QuantityCalc is virtual column");
}
/** Get Quantity calculed.
@return Quantity calculed */
public BigDecimal getQuantityCalc() 
{
BigDecimal bd = (BigDecimal)get_Value("QuantityCalc");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Retroactivity Amount.
@param RetroAmount Retroactivity Amount */
public void setRetroAmount (BigDecimal RetroAmount)
{
set_Value ("RetroAmount", RetroAmount);
}
/** Get Retroactivity Amount.
@return Retroactivity Amount */
public BigDecimal getRetroAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("RetroAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Start Date.
@param StartDate First effective day (inclusive) */
public void setStartDate (Timestamp StartDate)
{
throw new IllegalArgumentException ("StartDate is virtual column");
}
/** Get Start Date.
@return First effective day (inclusive) */
public Timestamp getStartDate() 
{
return (Timestamp)get_Value("StartDate");
}
/** Set Registered EMail.
@param UserName Email of the responsible for the System */
public void setUserName (String UserName)
{
if (UserName != null && UserName.length() > 60)
{
log.warning("Length > 60 - truncated");
UserName = UserName.substring(0,59);
}
set_Value ("UserName", UserName);
}
/** Get Registered EMail.
@return Email of the responsible for the System */
public String getUserName() 
{
return (String)get_Value("UserName");
}
}
