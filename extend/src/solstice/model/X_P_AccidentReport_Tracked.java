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
/** Generated Model for P_AccidentReport_Tracked
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_AccidentReport_Tracked extends PO
{
/** Standard Constructor
@param ctx context
@param P_AccidentReport_Tracked_ID id
@param trxName transaction
*/
public X_P_AccidentReport_Tracked (Properties ctx, int P_AccidentReport_Tracked_ID, String trxName)
{
super (ctx, P_AccidentReport_Tracked_ID, trxName);
/** if (P_AccidentReport_Tracked_ID == 0)
{
setP_AccidentReport_ID (0);
setP_AccidentReport_Tracked_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_AccidentReport_Tracked (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100805 */
public static final int Table_ID=2100805;

/** TableName=P_AccidentReport_Tracked */
public static final String Table_Name="P_AccidentReport_Tracked";

protected static KeyNamePair Model = new KeyNamePair(2100805,"P_AccidentReport_Tracked");

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
StringBuffer sb = new StringBuffer ("X_P_AccidentReport_Tracked[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Contestation.
@param Contestation Description of a constation on a CSST's report of accident */
public void setContestation (String Contestation)
{
if (Contestation != null && Contestation.length() > 100)
{
log.warning("Length > 100 - truncated");
Contestation = Contestation.substring(0,99);
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
/** Set Report of Accident.
@param P_AccidentReport_ID CSST's Report of Accident */
public void setP_AccidentReport_ID (int P_AccidentReport_ID)
{
if (P_AccidentReport_ID < 1) throw new IllegalArgumentException ("P_AccidentReport_ID is mandatory.");
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
/** Set P_AccidentReport_Tracked_ID.
@param P_AccidentReport_Tracked_ID P_AccidentReport_Tracked_ID */
public void setP_AccidentReport_Tracked_ID (int P_AccidentReport_Tracked_ID)
{
if (P_AccidentReport_Tracked_ID < 1) throw new IllegalArgumentException ("P_AccidentReport_Tracked_ID is mandatory.");
set_ValueNoCheck ("P_AccidentReport_Tracked_ID", new Integer(P_AccidentReport_Tracked_ID));
}
/** Get P_AccidentReport_Tracked_ID.
@return P_AccidentReport_Tracked_ID */
public int getP_AccidentReport_Tracked_ID() 
{
Integer ii = (Integer)get_Value("P_AccidentReport_Tracked_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_RecordStatus_ID AD_Reference_ID=2100358 */
public static final int P_RECORDSTATUS_ID_AD_Reference_ID=2100358;
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
/** Set Remark.
@param Remark Remark */
public void setRemark (String Remark)
{
if (Remark != null && Remark.length() > 100)
{
log.warning("Length > 100 - truncated");
Remark = Remark.substring(0,99);
}
set_Value ("Remark", Remark);
}
/** Get Remark.
@return Remark */
public String getRemark() 
{
return (String)get_Value("Remark");
}

/** WhoContest AD_Reference_ID=2100366 */
public static final int WHOCONTEST_AD_Reference_ID=2100366;
/** Employee = EE */
public static final String WHOCONTEST_Employee = "EE";
/** Employer = ER */
public static final String WHOCONTEST_Employer = "ER";
/** Succession = SU */
public static final String WHOCONTEST_Succession = "SU";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isWhoContestValid (String test)
{
return test == null || test.equals("EE") || test.equals("ER") || test.equals("SU");
}
/** Set Who Contests.
@param WhoContest The kind of person who contests a report of CSST */
public void setWhoContest (String WhoContest)
{
if (!isWhoContestValid(WhoContest))
throw new IllegalArgumentException ("WhoContest Invalid value - " + WhoContest + " - Reference_ID=2100366 - EE - ER - SU");
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
