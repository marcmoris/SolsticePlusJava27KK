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
/** Generated Model for P_Employee_Date_Hist
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Date_Hist extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Date_Hist_ID id
@param trxName transaction
*/
public X_P_Employee_Date_Hist (Properties ctx, int P_Employee_Date_Hist_ID, String trxName)
{
super (ctx, P_Employee_Date_Hist_ID, trxName);
/** if (P_Employee_Date_Hist_ID == 0)
{
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Date_Hist (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100881 */
public static final int Table_ID=2100881;

/** TableName=P_Employee_Date_Hist */
public static final String Table_Name="P_Employee_Date_Hist";

protected static KeyNamePair Model = new KeyNamePair(2100881,"P_Employee_Date_Hist");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Date_Hist[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Comment.
@param Comment Comment */
public void setComment (String Comment)
{
if (Comment != null && Comment.length() > 255)
{
log.warning("Length > 255 - truncated");
Comment = Comment.substring(0,254);
}
set_Value ("Comment", Comment);
}
/** Get Comment.
@return Comment */
public String getComment() 
{
return (String)get_Value("Comment");
}
/** Set Date.
@param Date_Hist Date */
public void setDate_Hist (Timestamp Date_Hist)
{
set_Value ("Date_Hist", Date_Hist);
}
/** Get Date.
@return Date */
public Timestamp getDate_Hist() 
{
return (Timestamp)get_Value("Date_Hist");
}
/** Set P_Employee_Date_Hist_ID.
@param P_Employee_Date_Hist_ID P_Employee_Date_Hist_ID */
public void setP_Employee_Date_Hist_ID (int P_Employee_Date_Hist_ID)
{
if (P_Employee_Date_Hist_ID <= 0) set_ValueNoCheck ("P_Employee_Date_Hist_ID", null);
 else 
set_ValueNoCheck ("P_Employee_Date_Hist_ID", new Integer(P_Employee_Date_Hist_ID));
}
/** Get P_Employee_Date_Hist_ID.
@return P_Employee_Date_Hist_ID */
public int getP_Employee_Date_Hist_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Date_Hist_ID");
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

/** Type_Date_Hist AD_Reference_ID=2100470 */
public static final int TYPE_DATE_HIST_AD_Reference_ID=2100470;
/** Layoff = DateLayoff */
public static final String TYPE_DATE_HIST_Layoff = "DateLayoff";
/** Hired  = DateHired */
public static final String TYPE_DATE_HIST_Hired = "DateHired";
/** DateHired2 = DateHired2 */
public static final String TYPE_DATE_HIST_DateHired2 = "DateHired2";
/** Date Seniority = DateSeniority */
public static final String TYPE_DATE_HIST_DateSeniority = "DateSeniority";
/** Date Probationary = DateProbationary */
public static final String TYPE_DATE_HIST_DateProbationary = "DateProbationary";
/** Date Rehired = DateRehired */
public static final String TYPE_DATE_HIST_DateRehired = "DateRehired";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isType_Date_HistValid (String test)
{
return test == null || test.equals("DateLayoff") || test.equals("DateHired") || test.equals("DateHired2") || test.equals("DateSeniority") || test.equals("DateProbationary") || test.equals("DateRehired");
}
/** Set Date type.
@param Type_Date_Hist Date type */
public void setType_Date_Hist (String Type_Date_Hist)
{
if (!isType_Date_HistValid(Type_Date_Hist))
throw new IllegalArgumentException ("Type_Date_Hist Invalid value - " + Type_Date_Hist + " - Reference_ID=2100470 - DateLayoff - DateHired - DateHired2 - DateSeniority - DateProbationary - DateRehired");
if (Type_Date_Hist != null && Type_Date_Hist.length() > 30)
{
log.warning("Length > 30 - truncated");
Type_Date_Hist = Type_Date_Hist.substring(0,29);
}
set_Value ("Type_Date_Hist", Type_Date_Hist);
}
/** Get Date type.
@return Date type */
public String getType_Date_Hist() 
{
return (String)get_Value("Type_Date_Hist");
}
}
