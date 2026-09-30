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
/** Generated Model for P_Employee_Advance_Detail
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Advance_Detail extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Advance_Detail_ID id
@param trxName transaction
*/
public X_P_Employee_Advance_Detail (Properties ctx, int P_Employee_Advance_Detail_ID, String trxName)
{
super (ctx, P_Employee_Advance_Detail_ID, trxName);
/** if (P_Employee_Advance_Detail_ID == 0)
{
setAdvanceDate (new Timestamp(System.currentTimeMillis()));
setP_Employee_Advance_Detail_ID (0);
setP_Employee_Advance_ID (0);
setP_Period_ID (0);
setP_Year_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Advance_Detail (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000145 */
public static final int Table_ID=2000145;

/** TableName=P_Employee_Advance_Detail */
public static final String Table_Name="P_Employee_Advance_Detail";

protected static KeyNamePair Model = new KeyNamePair(2000145,"P_Employee_Advance_Detail");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Advance_Detail[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Amount.
@param AdvanceAmount Amount */
public void setAdvanceAmount (BigDecimal AdvanceAmount)
{
set_Value ("AdvanceAmount", AdvanceAmount);
}
/** Get Amount.
@return Amount */
public BigDecimal getAdvanceAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("AdvanceAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Date.
@param AdvanceDate Date */
public void setAdvanceDate (Timestamp AdvanceDate)
{
if (AdvanceDate == null) throw new IllegalArgumentException ("AdvanceDate is mandatory.");
set_Value ("AdvanceDate", AdvanceDate);
}
/** Get Date.
@return Date */
public Timestamp getAdvanceDate() 
{
return (Timestamp)get_Value("AdvanceDate");
}
/** Set Check No.
@param CheckNo Check Number */
public void setCheckNo (String CheckNo)
{
if (CheckNo != null && CheckNo.length() > 30)
{
log.warning("Length > 30 - truncated");
CheckNo = CheckNo.substring(0,29);
}
set_Value ("CheckNo", CheckNo);
}
/** Get Check No.
@return Check Number */
public String getCheckNo() 
{
return (String)get_Value("CheckNo");
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
/** Set Refund.
@param IsRefund Refund */
public void setIsRefund (boolean IsRefund)
{
set_Value ("IsRefund", new Boolean(IsRefund));
}
/** Get Refund.
@return Refund */
public boolean isRefund() 
{
Object oo = get_Value("IsRefund");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}

/** P_AccidentReport_ID AD_Reference_ID=2100373 */
public static final int P_ACCIDENTREPORT_ID_AD_Reference_ID=2100373;
/** Set Report of Accident.
@param P_AccidentReport_ID CSST's Report of Accident */
public void setP_AccidentReport_ID (int P_AccidentReport_ID)
{
if (P_AccidentReport_ID <= 0) set_Value ("P_AccidentReport_ID", null);
 else 
set_Value ("P_AccidentReport_ID", new Integer(P_AccidentReport_ID));
}
/** Get Report of Accident.
@return CSST's Report of Accident */
public int getP_AccidentReport_ID() 
{
Integer ii = (Integer)get_Value("P_AccidentReport_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Advance.
@param P_Advance_ID Advance */
public void setP_Advance_ID (int P_Advance_ID)
{
if (P_Advance_ID <= 0) set_Value ("P_Advance_ID", null);
 else 
set_Value ("P_Advance_ID", new Integer(P_Advance_ID));
}
/** Get Advance.
@return Advance */
public int getP_Advance_ID() 
{
Integer ii = (Integer)get_Value("P_Advance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Employee_Advance_Detail_ID.
@param P_Employee_Advance_Detail_ID P_Employee_Advance_Detail_ID */
public void setP_Employee_Advance_Detail_ID (int P_Employee_Advance_Detail_ID)
{
if (P_Employee_Advance_Detail_ID < 1) throw new IllegalArgumentException ("P_Employee_Advance_Detail_ID is mandatory.");
set_Value ("P_Employee_Advance_Detail_ID", new Integer(P_Employee_Advance_Detail_ID));
}
/** Get P_Employee_Advance_Detail_ID.
@return P_Employee_Advance_Detail_ID */
public int getP_Employee_Advance_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Advance_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Advance.
@param P_Employee_Advance_ID Advance */
public void setP_Employee_Advance_ID (int P_Employee_Advance_ID)
{
if (P_Employee_Advance_ID < 1) throw new IllegalArgumentException ("P_Employee_Advance_ID is mandatory.");
set_Value ("P_Employee_Advance_ID", new Integer(P_Employee_Advance_ID));
}
/** Get Advance.
@return Advance */
public int getP_Employee_Advance_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Advance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Employee_Advance_Summary_ID.
@param P_Employee_Advance_Summary_ID P_Employee_Advance_Summary_ID */
public void setP_Employee_Advance_Summary_ID (int P_Employee_Advance_Summary_ID)
{
if (P_Employee_Advance_Summary_ID <= 0) set_Value ("P_Employee_Advance_Summary_ID", null);
 else 
set_Value ("P_Employee_Advance_Summary_ID", new Integer(P_Employee_Advance_Summary_ID));
}
/** Get P_Employee_Advance_Summary_ID.
@return P_Employee_Advance_Summary_ID */
public int getP_Employee_Advance_Summary_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Advance_Summary_ID");
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

/** P_Payment_ID AD_Reference_ID=2000059 */
public static final int P_PAYMENT_ID_AD_Reference_ID=2000059;
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
if (P_Period_ID < 1) throw new IllegalArgumentException ("P_Period_ID is mandatory.");
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
/** Set Special Rate.
@param P_SpecialRate_ID Special Rate */
public void setP_SpecialRate_ID (int P_SpecialRate_ID)
{
if (P_SpecialRate_ID <= 0) set_Value ("P_SpecialRate_ID", null);
 else 
set_Value ("P_SpecialRate_ID", new Integer(P_SpecialRate_ID));
}
/** Get Special Rate.
@return Special Rate */
public int getP_SpecialRate_ID() 
{
Integer ii = (Integer)get_Value("P_SpecialRate_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Year_ID AD_Reference_ID=2000012 */
public static final int P_YEAR_ID_AD_Reference_ID=2000012;
/** Set Year.
@param P_Year_ID Calendar Year */
public void setP_Year_ID (int P_Year_ID)
{
if (P_Year_ID < 1) throw new IllegalArgumentException ("P_Year_ID is mandatory.");
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

/** Transmitter AD_Reference_ID=2100456 */
public static final int TRANSMITTER_AD_Reference_ID=2100456;
/** C.S.S.T = C.S.S.T */
public static final String TRANSMITTER_CSST = "C.S.S.T";
/** Employee = Employee */
public static final String TRANSMITTER_Employee = "Employee";
/** Others = Others */
public static final String TRANSMITTER_Others = "Others";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isTransmitterValid (String test)
{
return test == null || test.equals("C.S.S.T") || test.equals("Employee") || test.equals("Others");
}
/** Set Transmitter.
@param Transmitter Transmitter */
public void setTransmitter (String Transmitter)
{
if (!isTransmitterValid(Transmitter))
throw new IllegalArgumentException ("Transmitter Invalid value - " + Transmitter + " - Reference_ID=2100456 - C.S.S.T - Employee - Others");
if (Transmitter != null && Transmitter.length() > 30)
{
log.warning("Length > 30 - truncated");
Transmitter = Transmitter.substring(0,29);
}
set_Value ("Transmitter", Transmitter);
}
/** Get Transmitter.
@return Transmitter */
public String getTransmitter() 
{
return (String)get_Value("Transmitter");
}
}
