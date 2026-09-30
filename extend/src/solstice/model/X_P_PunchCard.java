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
/** Generated Model for P_PunchCard
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_PunchCard extends PO
{
/** Standard Constructor
@param ctx context
@param P_PunchCard_ID id
@param trxName transaction
*/
public X_P_PunchCard (Properties ctx, int P_PunchCard_ID, String trxName)
{
super (ctx, P_PunchCard_ID, trxName);
/** if (P_PunchCard_ID == 0)
{
setDatePunchCard (new Timestamp(System.currentTimeMillis()));
setP_Absence_Statut_ID (0);
setP_Employee_ID (0);
setP_PunchCard_ID (0);
setP_PunchCard_Reason_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_PunchCard (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100829 */
public static final int Table_ID=2100829;

/** TableName=P_PunchCard */
public static final String Table_Name="P_PunchCard";

protected static KeyNamePair Model = new KeyNamePair(2100829,"P_PunchCard");

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
StringBuffer sb = new StringBuffer ("X_P_PunchCard[").append(get_ID()).append("]");
return sb.toString();
}
/** Set AfternoonEnd.
@param AfternoonEnd AfternoonEnd */
public void setAfternoonEnd (String AfternoonEnd)
{
if (AfternoonEnd != null && AfternoonEnd.length() > 5)
{
log.warning("Length > 5 - truncated");
AfternoonEnd = AfternoonEnd.substring(0,4);
}
set_Value ("AfternoonEnd", AfternoonEnd);
}
/** Get AfternoonEnd.
@return AfternoonEnd */
public String getAfternoonEnd() 
{
return (String)get_Value("AfternoonEnd");
}
/** Set AfternoonStart.
@param AfternoonStart AfternoonStart */
public void setAfternoonStart (String AfternoonStart)
{
if (AfternoonStart != null && AfternoonStart.length() > 5)
{
log.warning("Length > 5 - truncated");
AfternoonStart = AfternoonStart.substring(0,4);
}
set_Value ("AfternoonStart", AfternoonStart);
}
/** Get AfternoonStart.
@return AfternoonStart */
public String getAfternoonStart() 
{
return (String)get_Value("AfternoonStart");
}
/** Set DatePunchCard.
@param DatePunchCard DatePunchCard */
public void setDatePunchCard (Timestamp DatePunchCard)
{
if (DatePunchCard == null) throw new IllegalArgumentException ("DatePunchCard is mandatory.");
set_Value ("DatePunchCard", DatePunchCard);
}
/** Get DatePunchCard.
@return DatePunchCard */
public Timestamp getDatePunchCard() 
{
return (Timestamp)get_Value("DatePunchCard");
}
/** Set MorningEnd.
@param MorningEnd MorningEnd */
public void setMorningEnd (String MorningEnd)
{
if (MorningEnd != null && MorningEnd.length() > 5)
{
log.warning("Length > 5 - truncated");
MorningEnd = MorningEnd.substring(0,4);
}
set_Value ("MorningEnd", MorningEnd);
}
/** Get MorningEnd.
@return MorningEnd */
public String getMorningEnd() 
{
return (String)get_Value("MorningEnd");
}
/** Set MorningStart.
@param MorningStart MorningStart */
public void setMorningStart (String MorningStart)
{
if (MorningStart != null && MorningStart.length() > 5)
{
log.warning("Length > 5 - truncated");
MorningStart = MorningStart.substring(0,4);
}
set_Value ("MorningStart", MorningStart);
}
/** Get MorningStart.
@return MorningStart */
public String getMorningStart() 
{
return (String)get_Value("MorningStart");
}
/** Set P_Absence_Statut_ID.
@param P_Absence_Statut_ID P_Absence_Statut_ID */
public void setP_Absence_Statut_ID (int P_Absence_Statut_ID)
{
if (P_Absence_Statut_ID < 1) throw new IllegalArgumentException ("P_Absence_Statut_ID is mandatory.");
set_Value ("P_Absence_Statut_ID", new Integer(P_Absence_Statut_ID));
}
/** Get P_Absence_Statut_ID.
@return P_Absence_Statut_ID */
public int getP_Absence_Statut_ID() 
{
Integer ii = (Integer)get_Value("P_Absence_Statut_ID");
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
/** Set P_PunchCard_ID.
@param P_PunchCard_ID P_PunchCard_ID */
public void setP_PunchCard_ID (int P_PunchCard_ID)
{
if (P_PunchCard_ID < 1) throw new IllegalArgumentException ("P_PunchCard_ID is mandatory.");
set_ValueNoCheck ("P_PunchCard_ID", new Integer(P_PunchCard_ID));
}
/** Get P_PunchCard_ID.
@return P_PunchCard_ID */
public int getP_PunchCard_ID() 
{
Integer ii = (Integer)get_Value("P_PunchCard_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_PunchCard_Reason_ID.
@param P_PunchCard_Reason_ID P_PunchCard_Reason_ID */
public void setP_PunchCard_Reason_ID (int P_PunchCard_Reason_ID)
{
if (P_PunchCard_Reason_ID < 1) throw new IllegalArgumentException ("P_PunchCard_Reason_ID is mandatory.");
set_Value ("P_PunchCard_Reason_ID", new Integer(P_PunchCard_Reason_ID));
}
/** Get P_PunchCard_Reason_ID.
@return P_PunchCard_Reason_ID */
public int getP_PunchCard_Reason_ID() 
{
Integer ii = (Integer)get_Value("P_PunchCard_Reason_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Reason_Other.
@param Reason_Other Reason_Other */
public void setReason_Other (String Reason_Other)
{
if (Reason_Other != null && Reason_Other.length() > 255)
{
log.warning("Length > 255 - truncated");
Reason_Other = Reason_Other.substring(0,254);
}
set_Value ("Reason_Other", Reason_Other);
}
/** Get Reason_Other.
@return Reason_Other */
public String getReason_Other() 
{
return (String)get_Value("Reason_Other");
}
}
