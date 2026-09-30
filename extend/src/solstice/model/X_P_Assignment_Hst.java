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
/** Generated Model for P_Assignment_Hst
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Assignment_Hst extends PO
{
/** Standard Constructor
@param ctx context
@param P_Assignment_Hst_ID id
@param trxName transaction
*/
public X_P_Assignment_Hst (Properties ctx, int P_Assignment_Hst_ID, String trxName)
{
super (ctx, P_Assignment_Hst_ID, trxName);
/** if (P_Assignment_Hst_ID == 0)
{
setAssignmentType (null);
setEffectIn (new Timestamp(System.currentTimeMillis()));
setP_Assignment_Hst_ID (0);
setP_Assignment_ID (0);
setP_Employee_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Assignment_Hst (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100824 */
public static final int Table_ID=2100824;

/** TableName=P_Assignment_Hst */
public static final String Table_Name="P_Assignment_Hst";

protected static KeyNamePair Model = new KeyNamePair(2100824,"P_Assignment_Hst");

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
StringBuffer sb = new StringBuffer ("X_P_Assignment_Hst[").append(get_ID()).append("]");
return sb.toString();
}

/** AssignmentType AD_Reference_ID=2000067 */
public static final int ASSIGNMENTTYPE_AD_Reference_ID=2000067;
/** Base = B */
public static final String ASSIGNMENTTYPE_Base = "B";
/** Primary = P */
public static final String ASSIGNMENTTYPE_Primary = "P";
/** Other = O */
public static final String ASSIGNMENTTYPE_Other = "O";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isAssignmentTypeValid (String test)
{
return test.equals("B") || test.equals("P") || test.equals("O");
}
/** Set Assignment Type.
@param AssignmentType Assignment Type */
public void setAssignmentType (String AssignmentType)
{
if (AssignmentType == null) throw new IllegalArgumentException ("AssignmentType is mandatory");
if (!isAssignmentTypeValid(AssignmentType))
throw new IllegalArgumentException ("AssignmentType Invalid value - " + AssignmentType + " - Reference_ID=2000067 - B - P - O");
if (AssignmentType.length() > 1)
{
log.warning("Length > 1 - truncated");
AssignmentType = AssignmentType.substring(0,0);
}
set_Value ("AssignmentType", AssignmentType);
}
/** Get Assignment Type.
@return Assignment Type */
public String getAssignmentType() 
{
return (String)get_Value("AssignmentType");
}
/** Set Effect In.
@param EffectIn Date when information in effect  */
public void setEffectIn (Timestamp EffectIn)
{
if (EffectIn == null) throw new IllegalArgumentException ("EffectIn is mandatory.");
set_Value ("EffectIn", EffectIn);
}
/** Get Effect In.
@return Date when information in effect  */
public Timestamp getEffectIn() 
{
return (Timestamp)get_Value("EffectIn");
}
/** Set P_Assignment_Hst_ID.
@param P_Assignment_Hst_ID P_Assignment_Hst_ID */
public void setP_Assignment_Hst_ID (int P_Assignment_Hst_ID)
{
if (P_Assignment_Hst_ID < 1) throw new IllegalArgumentException ("P_Assignment_Hst_ID is mandatory.");
set_ValueNoCheck ("P_Assignment_Hst_ID", new Integer(P_Assignment_Hst_ID));
}
/** Get P_Assignment_Hst_ID.
@return P_Assignment_Hst_ID */
public int getP_Assignment_Hst_ID() 
{
Integer ii = (Integer)get_Value("P_Assignment_Hst_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Assignment.
@param P_Assignment_ID Assignment */
public void setP_Assignment_ID (int P_Assignment_ID)
{
if (P_Assignment_ID < 1) throw new IllegalArgumentException ("P_Assignment_ID is mandatory.");
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
/** Set Assignment State.
@param P_Assignment_State_ID Assignment State */
public void setP_Assignment_State_ID (int P_Assignment_State_ID)
{
if (P_Assignment_State_ID <= 0) set_Value ("P_Assignment_State_ID", null);
 else 
set_Value ("P_Assignment_State_ID", new Integer(P_Assignment_State_ID));
}
/** Get Assignment State.
@return Assignment State */
public int getP_Assignment_State_ID() 
{
Integer ii = (Integer)get_Value("P_Assignment_State_ID");
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
}
