/******************************************************************************
* Product: Solstice+ Payroll & Human Resources Management                    *
* Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
* This program is free software, you can redistribute it and/or modify it    *
* under the terms version 2 of the GNU General Public License as published   *
* by the Free Software Foundation. This program is distributed in the hope   *
* that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
* warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
* See the GNU General Public License for more details.                       *
* You should have received a copy of the GNU General Public License along    *
* with this program, if not, write to the Free Software Foundation, Inc.,    *
* 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
* For the text or an alternative of this public license, you may reach us    *
* ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
* or via info@progestion.net or http://www.progestion.net/license.html       *
******************************************************************************/
package solstice.model;

/** Generated Model - DO NOT CHANGE */
import org.compiere.model.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for P_Distribution
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Distribution extends PO
{
/** Standard Constructor
@param ctx context
@param P_Distribution_ID id
@param trxName transaction
*/
public X_P_Distribution (Properties ctx, int P_Distribution_ID, String trxName)
{
super (ctx, P_Distribution_ID, trxName);
/** if (P_Distribution_ID == 0)
{
setP_Distribution_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Distribution (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000023 */
public static final int Table_ID=2000023;

/** TableName=P_Distribution */
public static final String Table_Name="P_Distribution";

protected static KeyNamePair Model = new KeyNamePair(2000023,"P_Distribution");

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
StringBuffer sb = new StringBuffer ("X_P_Distribution[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Employé absent ou sans courriel.
@param IsCodeForAbsentEmployees Employé absent ou sans courriel */
public void setIsCodeForAbsentEmployees (boolean IsCodeForAbsentEmployees)
{
set_Value ("IsCodeForAbsentEmployees", new Boolean(IsCodeForAbsentEmployees));
}
/** Get Employé absent ou sans courriel.
@return Employé absent ou sans courriel */
public boolean isCodeForAbsentEmployees() 
{
Object oo = get_Value("IsCodeForAbsentEmployees");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Manager.
@param Manager Manager */
public void setManager (String Manager)
{
if (Manager != null && Manager.length() > 60)
{
log.warning("Length > 60 - truncated");
Manager = Manager.substring(0,59);
}
set_Value ("Manager", Manager);
}
/** Get Manager.
@return Manager */
public String getManager() 
{
return (String)get_Value("Manager");
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name != null && Name.length() > 255)
{
log.warning("Length > 255 - truncated");
Name = Name.substring(0,254);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Set Distribution.
@param P_Distribution_ID Code for the check Distributio */
public void setP_Distribution_ID (int P_Distribution_ID)
{
if (P_Distribution_ID < 1) throw new IllegalArgumentException ("P_Distribution_ID is mandatory.");
set_Value ("P_Distribution_ID", new Integer(P_Distribution_ID));
}
/** Get Distribution.
@return Code for the check Distributio */
public int getP_Distribution_ID() 
{
Integer ii = (Integer)get_Value("P_Distribution_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Schedule.
@param P_Schedule_ID Répartition des heures de travail à l'intérieur d'une période donnée.  */
public void setP_Schedule_ID (int P_Schedule_ID)
{
if (P_Schedule_ID <= 0) set_Value ("P_Schedule_ID", null);
 else 
set_Value ("P_Schedule_ID", new Integer(P_Schedule_ID));
}
/** Get Schedule.
@return Répartition des heures de travail à l'intérieur d'une période donnée.  */
public int getP_Schedule_ID() 
{
Integer ii = (Integer)get_Value("P_Schedule_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 30)
{
log.warning("Length > 30 - truncated");
Value = Value.substring(0,29);
}
set_Value ("Value", Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
}
