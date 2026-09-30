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
/** Generated Model for P_Augmentation_Employee
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Augmentation_Employee extends PO
{
/** Standard Constructor
@param ctx context
@param P_Augmentation_Employee_ID id
@param trxName transaction
*/
public X_P_Augmentation_Employee (Properties ctx, int P_Augmentation_Employee_ID, String trxName)
{
super (ctx, P_Augmentation_Employee_ID, trxName);
/** if (P_Augmentation_Employee_ID == 0)
{
setP_Augmentation_Employee_ID (0);
setP_Augmentation_ID (0);
setP_Employee_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Augmentation_Employee (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100789 */
public static final int Table_ID=2100789;

/** TableName=P_Augmentation_Employee */
public static final String Table_Name="P_Augmentation_Employee";

protected static KeyNamePair Model = new KeyNamePair(2100789,"P_Augmentation_Employee");

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
StringBuffer sb = new StringBuffer ("X_P_Augmentation_Employee[").append(get_ID()).append("]");
return sb.toString();
}
/** Set % Augmentation.
@param AugPrcCad % Augmentation */
public void setAugPrcCad (BigDecimal AugPrcCad)
{
set_Value ("AugPrcCad", AugPrcCad);
}
/** Get % Augmentation.
@return % Augmentation */
public BigDecimal getAugPrcCad() 
{
BigDecimal bd = (BigDecimal)get_Value("AugPrcCad");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set P_Augmentation_Employee_ID.
@param P_Augmentation_Employee_ID P_Augmentation_Employee_ID */
public void setP_Augmentation_Employee_ID (int P_Augmentation_Employee_ID)
{
if (P_Augmentation_Employee_ID < 1) throw new IllegalArgumentException ("P_Augmentation_Employee_ID is mandatory.");
set_ValueNoCheck ("P_Augmentation_Employee_ID", new Integer(P_Augmentation_Employee_ID));
}
/** Get P_Augmentation_Employee_ID.
@return P_Augmentation_Employee_ID */
public int getP_Augmentation_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Retroactivity.
@param P_Augmentation_ID Retroactivity */
public void setP_Augmentation_ID (int P_Augmentation_ID)
{
if (P_Augmentation_ID < 1) throw new IllegalArgumentException ("P_Augmentation_ID is mandatory.");
set_Value ("P_Augmentation_ID", new Integer(P_Augmentation_ID));
}
/** Get Retroactivity.
@return Retroactivity */
public int getP_Augmentation_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_ID");
if (ii == null) return 0;
return ii.intValue();
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
}
