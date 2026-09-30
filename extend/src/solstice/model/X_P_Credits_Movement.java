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
/** Generated Model for P_Credits_Movement
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Credits_Movement extends PO
{
/** Standard Constructor
@param ctx context
@param P_Credits_Movement_ID id
@param trxName transaction
*/
public X_P_Credits_Movement (Properties ctx, int P_Credits_Movement_ID, String trxName)
{
super (ctx, P_Credits_Movement_ID, trxName);
/** if (P_Credits_Movement_ID == 0)
{
setP_Credits_ID (0);
setP_Credits_Movement_ID (0);
setP_Method_Credits_ID (0);
setPMovementHour (Env.ZERO);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Credits_Movement (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000114 */
public static final int Table_ID=2000114;

/** TableName=P_Credits_Movement */
public static final String Table_Name="P_Credits_Movement";

protected static KeyNamePair Model = new KeyNamePair(2000114,"P_Credits_Movement");

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
StringBuffer sb = new StringBuffer ("X_P_Credits_Movement[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,254);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set Is attribution.
@param IsAttribution Is attribution */
public void setIsAttribution (boolean IsAttribution)
{
set_Value ("IsAttribution", new Boolean(IsAttribution));
}
/** Get Is attribution.
@return Is attribution */
public boolean isAttribution() 
{
Object oo = get_Value("IsAttribution");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}

/** P_Credits_ID AD_Reference_ID=2100318 */
public static final int P_CREDITS_ID_AD_Reference_ID=2100318;
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
/** Set P_Credits_Movement_ID.
@param P_Credits_Movement_ID P_Credits_Movement_ID */
public void setP_Credits_Movement_ID (int P_Credits_Movement_ID)
{
if (P_Credits_Movement_ID < 1) throw new IllegalArgumentException ("P_Credits_Movement_ID is mandatory.");
set_Value ("P_Credits_Movement_ID", new Integer(P_Credits_Movement_ID));
}
/** Get P_Credits_Movement_ID.
@return P_Credits_Movement_ID */
public int getP_Credits_Movement_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Movement_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Credits.
@param P_Employee_Credits_ID Credits */
public void setP_Employee_Credits_ID (int P_Employee_Credits_ID)
{
if (P_Employee_Credits_ID <= 0) set_Value ("P_Employee_Credits_ID", null);
 else 
set_Value ("P_Employee_Credits_ID", new Integer(P_Employee_Credits_ID));
}
/** Get Credits.
@return Credits */
public int getP_Employee_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
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
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getP_Employee_ID()));
}
/** Set Method.
@param P_Method_Credits_ID Method */
public void setP_Method_Credits_ID (int P_Method_Credits_ID)
{
if (P_Method_Credits_ID < 1) throw new IllegalArgumentException ("P_Method_Credits_ID is mandatory.");
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
/** Set Date.
@param PMovementDate Date */
public void setPMovementDate (Timestamp PMovementDate)
{
set_Value ("PMovementDate", PMovementDate);
}
/** Get Date.
@return Date */
public Timestamp getPMovementDate() 
{
return (Timestamp)get_Value("PMovementDate");
}
/** Set Number Hour.
@param PMovementHour Number Hour */
public void setPMovementHour (BigDecimal PMovementHour)
{
if (PMovementHour == null) throw new IllegalArgumentException ("PMovementHour is mandatory.");
set_Value ("PMovementHour", PMovementHour);
}
/** Get Number Hour.
@return Number Hour */
public BigDecimal getPMovementHour() 
{
BigDecimal bd = (BigDecimal)get_Value("PMovementHour");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Origin.
@param PMovementOrigin Origin */
public void setPMovementOrigin (String PMovementOrigin)
{
if (PMovementOrigin != null && PMovementOrigin.length() > 12)
{
log.warning("Length > 12 - truncated");
PMovementOrigin = PMovementOrigin.substring(0,11);
}
set_Value ("PMovementOrigin", PMovementOrigin);
}
/** Get Origin.
@return Origin */
public String getPMovementOrigin() 
{
return (String)get_Value("PMovementOrigin");
}
/** Set Type.
@param PMovementType Type */
public void setPMovementType (String PMovementType)
{
if (PMovementType != null && PMovementType.length() > 5)
{
log.warning("Length > 5 - truncated");
PMovementType = PMovementType.substring(0,4);
}
set_Value ("PMovementType", PMovementType);
}
/** Get Type.
@return Type */
public String getPMovementType() 
{
return (String)get_Value("PMovementType");
}
/** Set Variation.
@param PMovementVariation Variation */
public void setPMovementVariation (BigDecimal PMovementVariation)
{
set_Value ("PMovementVariation", PMovementVariation);
}
/** Get Variation.
@return Variation */
public BigDecimal getPMovementVariation() 
{
BigDecimal bd = (BigDecimal)get_Value("PMovementVariation");
if (bd == null) return Env.ZERO;
return bd;
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

/** Set Amt Reserve.
@param Amt_Reserve Amt Reserve */
public void setAmt_Reserve (BigDecimal Amt_Reserve)
{
set_Value ("Amt_Reserve", Amt_Reserve);
}
/** Get Amt Reserve.
@return Amt Reserve */
public BigDecimal getAmt_Reserve() 
{
BigDecimal bd = (BigDecimal)get_Value("Amt_Reserve");
if (bd == null) return Env.ZERO;
return bd;
}



}
