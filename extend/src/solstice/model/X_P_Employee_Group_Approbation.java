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
/** Generated Model for P_Employee_Group_Approbation
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Group_Approbation extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Group_Approbation_ID id
@param trxName transaction
*/
public X_P_Employee_Group_Approbation (Properties ctx, int P_Employee_Group_Approbation_ID, String trxName)
{
super (ctx, P_Employee_Group_Approbation_ID, trxName);
/** if (P_Employee_Group_Approbation_ID == 0)
{
setP_Approbation_Level (null);
setP_Employee_Group_Approbation_ID (0);
setP_Employee_Relation_ID (0);
setP_Occupation_Group_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Group_Approbation (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100742 */
public static final int Table_ID=2100742;

/** TableName=P_Employee_Group_Approbation */
public static final String Table_Name="P_Employee_Group_Approbation";

protected static KeyNamePair Model = new KeyNamePair(2100742,"P_Employee_Group_Approbation");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Group_Approbation[").append(get_ID()).append("]");
return sb.toString();
}

/** P_Approbation_Level AD_Reference_ID=2100394 */
public static final int P_APPROBATION_LEVEL_AD_Reference_ID=2100394;
/** Complet = C */
public static final String P_APPROBATION_LEVEL_Complet = "C";
/** Partiel = P */
public static final String P_APPROBATION_LEVEL_Partiel = "P";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isP_Approbation_LevelValid (String test)
{
return test.equals("C") || test.equals("P");
}
/** Set Approbation Level.
@param P_Approbation_Level Approbation Level */
public void setP_Approbation_Level (String P_Approbation_Level)
{
if (P_Approbation_Level == null) throw new IllegalArgumentException ("P_Approbation_Level is mandatory");
if (!isP_Approbation_LevelValid(P_Approbation_Level))
throw new IllegalArgumentException ("P_Approbation_Level Invalid value - " + P_Approbation_Level + " - Reference_ID=2100394 - C - P");
if (P_Approbation_Level.length() > 1)
{
log.warning("Length > 1 - truncated");
P_Approbation_Level = P_Approbation_Level.substring(0,0);
}
set_Value ("P_Approbation_Level", P_Approbation_Level);
}
/** Get Approbation Level.
@return Approbation Level */
public String getP_Approbation_Level() 
{
return (String)get_Value("P_Approbation_Level");
}
/** Set P_Employee_Group_Approbation_ID.
@param P_Employee_Group_Approbation_ID P_Employee_Group_Approbation_ID */
public void setP_Employee_Group_Approbation_ID (int P_Employee_Group_Approbation_ID)
{
if (P_Employee_Group_Approbation_ID < 1) throw new IllegalArgumentException ("P_Employee_Group_Approbation_ID is mandatory.");
set_ValueNoCheck ("P_Employee_Group_Approbation_ID", new Integer(P_Employee_Group_Approbation_ID));
}
/** Get P_Employee_Group_Approbation_ID.
@return P_Employee_Group_Approbation_ID */
public int getP_Employee_Group_Approbation_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Group_Approbation_ID");
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

/** P_Occupation_Group_ID AD_Reference_ID=2100363 */
public static final int P_OCCUPATION_GROUP_ID_AD_Reference_ID=2100363;
/** Set Occupation Group.
@param P_Occupation_Group_ID   */
public void setP_Occupation_Group_ID (int P_Occupation_Group_ID)
{
if (P_Occupation_Group_ID < 1) throw new IllegalArgumentException ("P_Occupation_Group_ID is mandatory.");
set_Value ("P_Occupation_Group_ID", new Integer(P_Occupation_Group_ID));
}
/** Get Occupation Group.
@return   */
public int getP_Occupation_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Occupation_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
