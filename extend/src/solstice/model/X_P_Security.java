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
/** Generated Model for P_Security
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Security extends PO
{
/** Standard Constructor
@param ctx context
@param P_Security_ID id
@param trxName transaction
*/
public X_P_Security (Properties ctx, int P_Security_ID, String trxName)
{
super (ctx, P_Security_ID, trxName);
/** if (P_Security_ID == 0)
{
setName (null);
setP_Security_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Security (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100815 */
public static final int Table_ID=2100815;

/** TableName=P_Security */
public static final String Table_Name="P_Security";

protected static KeyNamePair Model = new KeyNamePair(2100815,"P_Security");

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
StringBuffer sb = new StringBuffer ("X_P_Security[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 1000)
{
log.warning("Length > 1000 - truncated");
Description = Description.substring(0,999);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}

/** Module AD_Reference_ID=2100453 */
public static final int MODULE_AD_Reference_ID=2100453;
/** Autorisation d'absence = ABS */
public static final String MODULE_AutorisationDAbsence = "ABS";
/** Avis de dépôt bancaire = ADB */
public static final String MODULE_AvisDeDépôtBancaire = "ADB";
/** Évaluation de rendement = ER */
public static final String MODULE_ÉvaluationDeRendement = "ER";
/** Livret de temps = LIVR */
public static final String MODULE_LivretDeTemps = "LIVR";
/** Relevé d'emploi = RE */
public static final String MODULE_RelevéDEmploi = "RE";
/** Permis horodateur = PM */
public static final String MODULE_PermisHorodateur = "PM";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isModuleValid (String test)
{
return test == null || test.equals("ABS") || test.equals("ADB") || test.equals("ER") || test.equals("LIVR") || test.equals("RE") || test.equals("PM");
}
/** Set Module.
@param Module Module */
public void setModule (String Module)
{
if (!isModuleValid(Module))
throw new IllegalArgumentException ("Module Invalid value - " + Module + " - Reference_ID=2100453 - ABS - ADB - ER - LIVR - RE - PM");
if (Module != null && Module.length() > 30)
{
log.warning("Length > 30 - truncated");
Module = Module.substring(0,29);
}
set_Value ("Module", Module);
}
/** Get Module.
@return Module */
public String getModule() 
{
return (String)get_Value("Module");
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 50)
{
log.warning("Length > 50 - truncated");
Name = Name.substring(0,49);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Set Security.
@param P_Security_ID Security */
public void setP_Security_ID (int P_Security_ID)
{
if (P_Security_ID < 1) throw new IllegalArgumentException ("P_Security_ID is mandatory.");
set_ValueNoCheck ("P_Security_ID", new Integer(P_Security_ID));
}
/** Get Security.
@return Security */
public int getP_Security_ID() 
{
Integer ii = (Integer)get_Value("P_Security_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
