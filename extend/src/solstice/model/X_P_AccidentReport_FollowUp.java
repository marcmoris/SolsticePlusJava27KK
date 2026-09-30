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
/** Generated Model for P_AccidentReport_FollowUp
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_AccidentReport_FollowUp extends PO
{
/** Standard Constructor
@param ctx context
@param P_AccidentReport_FollowUp_ID id
@param trxName transaction
*/
public X_P_AccidentReport_FollowUp (Properties ctx, int P_AccidentReport_FollowUp_ID, String trxName)
{
super (ctx, P_AccidentReport_FollowUp_ID, trxName);
/** if (P_AccidentReport_FollowUp_ID == 0)
{
setP_AccidentReport_FollowUp_ID (0);
setP_AccidentReport_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_AccidentReport_FollowUp (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100796 */
public static final int Table_ID=2100796;

/** TableName=P_AccidentReport_FollowUp */
public static final String Table_Name="P_AccidentReport_FollowUp";

protected static KeyNamePair Model = new KeyNamePair(2100796,"P_AccidentReport_FollowUp");

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
StringBuffer sb = new StringBuffer ("X_P_AccidentReport_FollowUp[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Commentary.
@param Commentary Commentary */
public void setCommentary (String Commentary)
{
if (Commentary != null && Commentary.length() > 4000)
{
log.warning("Length > 4000 - truncated");
Commentary = Commentary.substring(0,3999);
}
set_Value ("Commentary", Commentary);
}
/** Get Commentary.
@return Commentary */
public String getCommentary() 
{
return (String)get_Value("Commentary");
}
/** Set End Date.
@param EndDate Last effective date (inclusive) */
public void setEndDate (Timestamp EndDate)
{
set_Value ("EndDate", EndDate);
}
/** Get End Date.
@return Last effective date (inclusive) */
public Timestamp getEndDate() 
{
return (Timestamp)get_Value("EndDate");
}
/** Set P_AccidentReport_FollowUp_ID.
@param P_AccidentReport_FollowUp_ID P_AccidentReport_FollowUp_ID */
public void setP_AccidentReport_FollowUp_ID (int P_AccidentReport_FollowUp_ID)
{
if (P_AccidentReport_FollowUp_ID < 1) throw new IllegalArgumentException ("P_AccidentReport_FollowUp_ID is mandatory.");
set_ValueNoCheck ("P_AccidentReport_FollowUp_ID", new Integer(P_AccidentReport_FollowUp_ID));
}
/** Get P_AccidentReport_FollowUp_ID.
@return P_AccidentReport_FollowUp_ID */
public int getP_AccidentReport_FollowUp_ID() 
{
Integer ii = (Integer)get_Value("P_AccidentReport_FollowUp_ID");
if (ii == null) return 0;
return ii.intValue();
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

/** P_Assignment_ID AD_Reference_ID=2000057 */
public static final int P_ASSIGNMENT_ID_AD_Reference_ID=2000057;
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
/** Set Followup type.
@param P_RAAType_ID Followup type */
public void setP_RAAType_ID (int P_RAAType_ID)
{
if (P_RAAType_ID <= 0) set_Value ("P_RAAType_ID", null);
 else 
set_Value ("P_RAAType_ID", new Integer(P_RAAType_ID));
}
/** Get Followup type.
@return Followup type */
public int getP_RAAType_ID() 
{
Integer ii = (Integer)get_Value("P_RAAType_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Start Date.
@param StartDate First effective day (inclusive) */
public void setStartDate (Timestamp StartDate)
{
set_Value ("StartDate", StartDate);
}
/** Get Start Date.
@return First effective day (inclusive) */
public Timestamp getStartDate() 
{
return (Timestamp)get_Value("StartDate");
}
}
