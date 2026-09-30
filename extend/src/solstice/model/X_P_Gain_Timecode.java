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
/** Generated Model for P_Gain_Timecode
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Gain_Timecode extends PO
{
/** Standard Constructor
@param ctx context
@param P_Gain_Timecode_ID id
@param trxName transaction
*/
public X_P_Gain_Timecode (Properties ctx, int P_Gain_Timecode_ID, String trxName)
{
super (ctx, P_Gain_Timecode_ID, trxName);
/** if (P_Gain_Timecode_ID == 0)
{
setP_Gain_ID (0);
setP_Gain_Timecode_ID (0);
setP_Payment_Group_ID (0);
setP_Timecode_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Gain_Timecode (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201179 */
public static final int Table_ID=3201179;

/** TableName=P_Gain_Timecode */
public static final String Table_Name="P_Gain_Timecode";

protected static KeyNamePair Model = new KeyNamePair(3201179,"P_Gain_Timecode");

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
StringBuffer sb = new StringBuffer ("X_P_Gain_Timecode[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Gain.
@param P_Gain_ID Gain */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID < 1) throw new IllegalArgumentException ("P_Gain_ID is mandatory.");
set_Value ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Gain.
@return Gain */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Gain_Timecode_ID.
@param P_Gain_Timecode_ID P_Gain_Timecode_ID */
public void setP_Gain_Timecode_ID (int P_Gain_Timecode_ID)
{
if (P_Gain_Timecode_ID < 1) throw new IllegalArgumentException ("P_Gain_Timecode_ID is mandatory.");
set_ValueNoCheck ("P_Gain_Timecode_ID", new Integer(P_Gain_Timecode_ID));
}
/** Get P_Gain_Timecode_ID.
@return P_Gain_Timecode_ID */
public int getP_Gain_Timecode_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Timecode_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Occupation_Group_ID AD_Reference_ID=2000014 */
public static final int P_OCCUPATION_GROUP_ID_AD_Reference_ID=2000014;
/** Set Occupation Group.
@param P_Occupation_Group_ID   */
public void setP_Occupation_Group_ID (int P_Occupation_Group_ID)
{
if (P_Occupation_Group_ID <= 0) set_Value ("P_Occupation_Group_ID", null);
 else 
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
/** Set Payment Group.
@param P_Payment_Group_ID Payment Group */
public void setP_Payment_Group_ID (int P_Payment_Group_ID)
{
if (P_Payment_Group_ID < 1) throw new IllegalArgumentException ("P_Payment_Group_ID is mandatory.");
set_Value ("P_Payment_Group_ID", new Integer(P_Payment_Group_ID));
}
/** Get Payment Group.
@return Payment Group */
public int getP_Payment_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Time code.
@param P_Timecode_ID Time code */
public void setP_Timecode_ID (int P_Timecode_ID)
{
if (P_Timecode_ID < 1) throw new IllegalArgumentException ("P_Timecode_ID is mandatory.");
set_Value ("P_Timecode_ID", new Integer(P_Timecode_ID));
}
/** Get Time code.
@return Time code */
public int getP_Timecode_ID() 
{
Integer ii = (Integer)get_Value("P_Timecode_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
