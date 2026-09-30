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
/** Generated Model for P_Employee_Distribution_Booklet
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Distribution_Booklet extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Distribution_Booklet_ID id
@param trxName transaction
*/
public X_P_Employee_Distribution_Booklet (Properties ctx, int P_Employee_Distribution_Booklet_ID, String trxName)
{
super (ctx, P_Employee_Distribution_Booklet_ID, trxName);
/** if (P_Employee_Distribution_Booklet_ID == 0)
{
setIsLocked (false);
setP_Distribution_Booklet_ID (0);
setP_Employee_Distribution_Booklet_ID (0);
setP_Employee_ID (0);
setStart_Date (new Timestamp(System.currentTimeMillis()));
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Distribution_Booklet (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100823 */
public static final int Table_ID=2100823;

/** TableName=P_Employee_Distribution_Booklet */
public static final String Table_Name="P_Employee_Distribution_Booklet";

protected static KeyNamePair Model = new KeyNamePair(2100823,"P_Employee_Distribution_Booklet");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Distribution_Booklet[").append(get_ID()).append("]");
return sb.toString();
}
/** Set End date.
@param End_Date End date */
public void setEnd_Date (Timestamp End_Date)
{
set_Value ("End_Date", End_Date);
}
/** Get End date.
@return End date */
public Timestamp getEnd_Date() 
{
return (Timestamp)get_Value("End_Date");
}
/** Set Locked booklet.
@param IsLocked Locked booklet */
public void setIsLocked (boolean IsLocked)
{
set_Value ("IsLocked", new Boolean(IsLocked));
}
/** Get Locked booklet.
@return Locked booklet */
public boolean isLocked() 
{
Object oo = get_Value("IsLocked");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}

/** P_Distribution_Booklet_ID AD_Reference_ID=2100388 */
public static final int P_DISTRIBUTION_BOOKLET_ID_AD_Reference_ID=2100388;
/** Set Booklet.
@param P_Distribution_Booklet_ID Booklet */
public void setP_Distribution_Booklet_ID (int P_Distribution_Booklet_ID)
{
if (P_Distribution_Booklet_ID < 1) throw new IllegalArgumentException ("P_Distribution_Booklet_ID is mandatory.");
set_Value ("P_Distribution_Booklet_ID", new Integer(P_Distribution_Booklet_ID));
}
/** Get Booklet.
@return Booklet */
public int getP_Distribution_Booklet_ID() 
{
Integer ii = (Integer)get_Value("P_Distribution_Booklet_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_EMPLOYEE_DISTRIBUTION_BOOKLET_ID.
@param P_Employee_Distribution_Booklet_ID P_EMPLOYEE_DISTRIBUTION_BOOKLET_ID */
public void setP_Employee_Distribution_Booklet_ID (int P_Employee_Distribution_Booklet_ID)
{
if (P_Employee_Distribution_Booklet_ID < 1) throw new IllegalArgumentException ("P_Employee_Distribution_Booklet_ID is mandatory.");
set_ValueNoCheck ("P_Employee_Distribution_Booklet_ID", new Integer(P_Employee_Distribution_Booklet_ID));
}
/** Get P_EMPLOYEE_DISTRIBUTION_BOOKLET_ID.
@return P_EMPLOYEE_DISTRIBUTION_BOOKLET_ID */
public int getP_Employee_Distribution_Booklet_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Distribution_Booklet_ID");
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
/** Set Start date.
@param Start_Date Start date */
public void setStart_Date (Timestamp Start_Date)
{
if (Start_Date == null) throw new IllegalArgumentException ("Start_Date is mandatory.");
set_Value ("Start_Date", Start_Date);
}
/** Get Start date.
@return Start date */
public Timestamp getStart_Date() 
{
return (Timestamp)get_Value("Start_Date");
}
}
