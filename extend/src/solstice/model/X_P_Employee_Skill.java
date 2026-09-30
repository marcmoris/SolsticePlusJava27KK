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
/** Generated Model for P_Employee_Skill
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.4 2008/03/12 20:02:49 marmor01 Exp $ */
public class X_P_Employee_Skill extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Skill_ID id
@param trxName transaction
*/
public X_P_Employee_Skill (Properties ctx, int P_Employee_Skill_ID, String trxName)
{
super (ctx, P_Employee_Skill_ID, trxName);
/** if (P_Employee_Skill_ID == 0)
{
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Skill (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100892 */
public static final int Table_ID=2100892;

/** TableName=P_Employee_Skill */
public static final String Table_Name="P_Employee_Skill";

protected static KeyNamePair Model = new KeyNamePair(2100892,"P_Employee_Skill");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Skill[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Number of Year.
@param NumberOfYear Number of Year */
public void setNumberOfYear (int NumberOfYear)
{
set_Value ("NumberOfYear", new Integer(NumberOfYear));
}
/** Get Number of Year.
@return Number of Year */
public int getNumberOfYear() 
{
Integer ii = (Integer)get_Value("NumberOfYear");
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
/** Set P_Employee_Skill_ID.
@param P_Employee_Skill_ID P_Employee_Skill_ID */
public void setP_Employee_Skill_ID (int P_Employee_Skill_ID)
{
if (P_Employee_Skill_ID <= 0) set_ValueNoCheck ("P_Employee_Skill_ID", null);
 else 
set_ValueNoCheck ("P_Employee_Skill_ID", new Integer(P_Employee_Skill_ID));
}
/** Get P_Employee_Skill_ID.
@return P_Employee_Skill_ID */
public int getP_Employee_Skill_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Skill_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Skill.
@param P_Skill_ID Skill */
public void setP_Skill_ID (int P_Skill_ID)
{
if (P_Skill_ID <= 0) set_Value ("P_Skill_ID", null);
 else 
set_Value ("P_Skill_ID", new Integer(P_Skill_ID));
}
/** Get Skill.
@return Skill */
public int getP_Skill_ID() 
{
Integer ii = (Integer)get_Value("P_Skill_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
