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
/** Generated Model for P_Employee_Relation
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Relation extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Relation_ID id
@param trxName transaction
*/
public X_P_Employee_Relation (Properties ctx, int P_Employee_Relation_ID, String trxName)
{
super (ctx, P_Employee_Relation_ID, trxName);
/** if (P_Employee_Relation_ID == 0)
{
setP_Employee_ID (0);
setP_Employee_ID_In_Relation (0);
setP_Employee_Relation_ID (0);
setRelation_Type (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Relation (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100740 */
public static final int Table_ID=2100740;

/** TableName=P_Employee_Relation */
public static final String Table_Name="P_Employee_Relation";

protected static KeyNamePair Model = new KeyNamePair(2100740,"P_Employee_Relation");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Relation[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
set_ValueNoCheck ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee .
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Employee_ID_In_Relation AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_IN_RELATION_AD_Reference_ID=2000010;
/** Set P_Employee_ID_In_Relation.
@param P_Employee_ID_In_Relation P_Employee_ID_In_Relation */
public void setP_Employee_ID_In_Relation (int P_Employee_ID_In_Relation)
{
set_Value ("P_Employee_ID_In_Relation", new Integer(P_Employee_ID_In_Relation));
}
/** Get P_Employee_ID_In_Relation.
@return P_Employee_ID_In_Relation */
public int getP_Employee_ID_In_Relation() 
{
Integer ii = (Integer)get_Value("P_Employee_ID_In_Relation");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Employee_Relation_ID.
@param P_Employee_Relation_ID P_Employee_Relation_ID */
public void setP_Employee_Relation_ID (int P_Employee_Relation_ID)
{
if (P_Employee_Relation_ID < 1) throw new IllegalArgumentException ("P_Employee_Relation_ID is mandatory.");
set_ValueNoCheck ("P_Employee_Relation_ID", new Integer(P_Employee_Relation_ID));
}
/** Get P_Employee_Relation_ID.
@return P_Employee_Relation_ID */
public int getP_Employee_Relation_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Relation_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** Relation_Type AD_Reference_ID=2100393 */
public static final int RELATION_TYPE_AD_Reference_ID=2100393;
/** Approbateur = A */
public static final String RELATION_TYPE_Approbateur = "A";
/** Délégué = D */
public static final String RELATION_TYPE_Délégué = "D";
/** Remplaçant = R */
public static final String RELATION_TYPE_Remplaçant = "R";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isRelation_TypeValid (String test)
{
return test.equals("A") || test.equals("D") || test.equals("R");
}
/** Set Relation_Type.
@param Relation_Type Relation_Type */
public void setRelation_Type (String Relation_Type)
{
if (Relation_Type == null) throw new IllegalArgumentException ("Relation_Type is mandatory");
if (!isRelation_TypeValid(Relation_Type))
throw new IllegalArgumentException ("Relation_Type Invalid value - " + Relation_Type + " - Reference_ID=2100393 - A - D - R");
if (Relation_Type.length() > 1)
{
log.warning("Length > 1 - truncated");
Relation_Type = Relation_Type.substring(0,0);
}
set_Value ("Relation_Type", Relation_Type);
}
/** Get Relation_Type.
@return Relation_Type */
public String getRelation_Type() 
{
return (String)get_Value("Relation_Type");
}
}
