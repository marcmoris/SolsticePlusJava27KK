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
/** Generated Model for P_Employment_Equity
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employment_Equity extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employment_Equity_ID id
@param trxName transaction
*/
public X_P_Employment_Equity (Properties ctx, int P_Employment_Equity_ID, String trxName)
{
super (ctx, P_Employment_Equity_ID, trxName);
/** if (P_Employment_Equity_ID == 0)
{
setAboriginal (false);
setDisability (false);
setGender (null);
setP_Employee_ID (0);
setP_Employment_Equity_ID (0);
setVisible_Minority (false);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employment_Equity (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3101170 */
public static final int Table_ID=3101170;

/** TableName=P_Employment_Equity */
public static final String Table_Name="P_Employment_Equity";

protected static KeyNamePair Model = new KeyNamePair(3101170,"P_Employment_Equity");

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
StringBuffer sb = new StringBuffer ("X_P_Employment_Equity[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Aboriginal.
@param Aboriginal Aboriginal */
public void setAboriginal (boolean Aboriginal)
{
set_Value ("Aboriginal", new Boolean(Aboriginal));
}
/** Get Aboriginal.
@return Aboriginal */
public boolean isAboriginal() 
{
Object oo = get_Value("Aboriginal");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Additional data accommodation.
@param Additionnal_Data Additional data accommodation */
public void setAdditionnal_Data (String Additionnal_Data)
{
if (Additionnal_Data != null && Additionnal_Data.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Additionnal_Data = Additionnal_Data.substring(0,1999);
}
set_Value ("Additionnal_Data", Additionnal_Data);
}
/** Get Additional data accommodation.
@return Additional data accommodation */
public String getAdditionnal_Data() 
{
return (String)get_Value("Additionnal_Data");
}
/** Set Comments.
@param Comments Comments or additional information */
public void setComments (String Comments)
{
if (Comments != null && Comments.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Comments = Comments.substring(0,1999);
}
set_Value ("Comments", Comments);
}
/** Get Comments.
@return Comments or additional information */
public String getComments() 
{
return (String)get_Value("Comments");
}
/** Set Disability.
@param Disability Disability */
public void setDisability (boolean Disability)
{
set_Value ("Disability", new Boolean(Disability));
}
/** Get Disability.
@return Disability */
public boolean isDisability() 
{
Object oo = get_Value("Disability");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Date.
@param EntryDate Date */
public void setEntryDate (Timestamp EntryDate)
{
set_Value ("EntryDate", EntryDate);
}
/** Get Date.
@return Date */
public Timestamp getEntryDate() 
{
return (Timestamp)get_Value("EntryDate");
}

/** Gender AD_Reference_ID=2000048 */
public static final int GENDER_AD_Reference_ID=2000048;
/** Man = M */
public static final String GENDER_Man = "M";
/** Woman = F */
public static final String GENDER_Woman = "F";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isGenderValid (String test)
{
return test.equals("M") || test.equals("F");
}
/** Set Gender.
@param Gender Gender  */
public void setGender (String Gender)
{
if (Gender == null) throw new IllegalArgumentException ("Gender is mandatory");
if (!isGenderValid(Gender))
throw new IllegalArgumentException ("Gender Invalid value - " + Gender + " - Reference_ID=2000048 - M - F");
if (Gender.length() > 1)
{
log.warning("Length > 1 - truncated");
Gender = Gender.substring(0,0);
}
set_Value ("Gender", Gender);
}
/** Get Gender.
@return Gender  */
public String getGender() 
{
return (String)get_Value("Gender");
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
/** Set P_Employment_Equity_ID.
@param P_Employment_Equity_ID P_Employment_Equity_ID */
public void setP_Employment_Equity_ID (int P_Employment_Equity_ID)
{
if (P_Employment_Equity_ID < 1) throw new IllegalArgumentException ("P_Employment_Equity_ID is mandatory.");
set_ValueNoCheck ("P_Employment_Equity_ID", new Integer(P_Employment_Equity_ID));
}
/** Get P_Employment_Equity_ID.
@return P_Employment_Equity_ID */
public int getP_Employment_Equity_ID() 
{
Integer ii = (Integer)get_Value("P_Employment_Equity_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Visible Minority .
@param Visible_Minority Visible Minority  */
public void setVisible_Minority (boolean Visible_Minority)
{
set_Value ("Visible_Minority", new Boolean(Visible_Minority));
}
/** Get Visible Minority .
@return Visible Minority  */
public boolean isVisible_Minority() 
{
Object oo = get_Value("Visible_Minority");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Voluntary participation activities.
@param Voluntary_Activities Voluntary participation activities */
public void setVoluntary_Activities (boolean Voluntary_Activities)
{
set_Value ("Voluntary_Activities", new Boolean(Voluntary_Activities));
}
/** Get Voluntary participation activities.
@return Voluntary participation activities */
public boolean isVoluntary_Activities() 
{
Object oo = get_Value("Voluntary_Activities");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Voluntary participation use self-id information.
@param Voluntary_Seft_ID Voluntary participation use self-id information */
public void setVoluntary_Seft_ID (boolean Voluntary_Seft_ID)
{
set_Value ("Voluntary_Seft_ID", new Boolean(Voluntary_Seft_ID));
}
/** Get Voluntary participation use self-id information.
@return Voluntary participation use self-id information */
public boolean isVoluntary_Seft_ID() 
{
Object oo = get_Value("Voluntary_Seft_ID");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
}
