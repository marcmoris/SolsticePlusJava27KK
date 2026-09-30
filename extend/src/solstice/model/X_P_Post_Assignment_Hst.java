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
/** Generated Model for P_Post_Assignment_Hst
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Post_Assignment_Hst extends PO
{
/** Standard Constructor
@param ctx context
@param P_Post_Assignment_Hst_ID id
@param trxName transaction
*/
public X_P_Post_Assignment_Hst (Properties ctx, int P_Post_Assignment_Hst_ID, String trxName)
{
super (ctx, P_Post_Assignment_Hst_ID, trxName);
/** if (P_Post_Assignment_Hst_ID == 0)
{
setP_Employee_ID (0);
setP_POST_ASSIGNMENT_HST_ID (0);
setP_Post_ID (0);
setYear (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Post_Assignment_Hst (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100803 */
public static final int Table_ID=2100803;

/** TableName=P_Post_Assignment_Hst */
public static final String Table_Name="P_Post_Assignment_Hst";

protected static KeyNamePair Model = new KeyNamePair(2100803,"P_Post_Assignment_Hst");

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
StringBuffer sb = new StringBuffer ("X_P_Post_Assignment_Hst[").append(get_ID()).append("]");
return sb.toString();
}
/** Set AssignmentDate.
@param AssignmentDate AssignmentDate */
public void setAssignmentDate (Timestamp AssignmentDate)
{
set_Value ("AssignmentDate", AssignmentDate);
}
/** Get AssignmentDate.
@return AssignmentDate */
public Timestamp getAssignmentDate() 
{
return (Timestamp)get_Value("AssignmentDate");
}
/** Set AssignmentLastDate.
@param AssignmentLastDate AssignmentLastDate */
public void setAssignmentLastDate (Timestamp AssignmentLastDate)
{
set_Value ("AssignmentLastDate", AssignmentLastDate);
}
/** Get AssignmentLastDate.
@return AssignmentLastDate */
public Timestamp getAssignmentLastDate() 
{
return (Timestamp)get_Value("AssignmentLastDate");
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
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
/** Set P_POST_ASSIGNMENT_HST_ID.
@param P_POST_ASSIGNMENT_HST_ID P_POST_ASSIGNMENT_HST_ID */
public void setP_POST_ASSIGNMENT_HST_ID (int P_POST_ASSIGNMENT_HST_ID)
{
if (P_POST_ASSIGNMENT_HST_ID < 1) throw new IllegalArgumentException ("P_POST_ASSIGNMENT_HST_ID is mandatory.");
set_ValueNoCheck ("P_POST_ASSIGNMENT_HST_ID", new Integer(P_POST_ASSIGNMENT_HST_ID));
}
/** Get P_POST_ASSIGNMENT_HST_ID.
@return P_POST_ASSIGNMENT_HST_ID */
public int getP_POST_ASSIGNMENT_HST_ID() 
{
Integer ii = (Integer)get_Value("P_POST_ASSIGNMENT_HST_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Post.
@param P_Post_ID Post */
public void setP_Post_ID (int P_Post_ID)
{
if (P_Post_ID < 1) throw new IllegalArgumentException ("P_Post_ID is mandatory.");
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

/** PostAssignmentType AD_Reference_ID=2100430 */
public static final int POSTASSIGNMENTTYPE_AD_Reference_ID=2100430;
/** Titular = T */
public static final String POSTASSIGNMENTTYPE_Titular = "T";
/** Other = A */
public static final String POSTASSIGNMENTTYPE_Other = "A";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPostAssignmentTypeValid (String test)
{
return test == null || test.equals("T") || test.equals("A");
}
/** Set PostAssignmentType.
@param PostAssignmentType PostAssignmentType */
public void setPostAssignmentType (String PostAssignmentType)
{
if (!isPostAssignmentTypeValid(PostAssignmentType))
throw new IllegalArgumentException ("PostAssignmentType Invalid value - " + PostAssignmentType + " - Reference_ID=2100430 - T - A");
if (PostAssignmentType != null && PostAssignmentType.length() > 1)
{
log.warning("Length > 1 - truncated");
PostAssignmentType = PostAssignmentType.substring(0,0);
}
set_Value ("PostAssignmentType", PostAssignmentType);
}
/** Get PostAssignmentType.
@return PostAssignmentType */
public String getPostAssignmentType() 
{
return (String)get_Value("PostAssignmentType");
}

/** TypeYear AD_Reference_ID=2100429 */
public static final int TYPEYEAR_AD_Reference_ID=2100429;
/** Civil = C */
public static final String TYPEYEAR_Civil = "C";
/** Fiscal = F */
public static final String TYPEYEAR_Fiscal = "F";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isTypeYearValid (String test)
{
return test == null || test.equals("C") || test.equals("F");
}
/** Set TypeYear.
@param TypeYear TypeYear */
public void setTypeYear (String TypeYear)
{
if (!isTypeYearValid(TypeYear))
throw new IllegalArgumentException ("TypeYear Invalid value - " + TypeYear + " - Reference_ID=2100429 - C - F");
if (TypeYear != null && TypeYear.length() > 1)
{
log.warning("Length > 1 - truncated");
TypeYear = TypeYear.substring(0,0);
}
set_Value ("TypeYear", TypeYear);
}
/** Get TypeYear.
@return TypeYear */
public String getTypeYear() 
{
return (String)get_Value("TypeYear");
}
/** Set Year.
@param Year Calendar Year */
public void setYear (String Year)
{
if (Year == null) throw new IllegalArgumentException ("Year is mandatory.");
if (Year.length() > 4)
{
log.warning("Length > 4 - truncated");
Year = Year.substring(0,3);
}
set_Value ("Year", Year);
}
/** Get Year.
@return Calendar Year */
public String getYear() 
{
return (String)get_Value("Year");
}
}
