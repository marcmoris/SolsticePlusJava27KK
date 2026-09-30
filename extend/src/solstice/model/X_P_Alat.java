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
/** Generated Model for P_Alat
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Alat extends PO
{
/** Standard Constructor
@param ctx context
@param P_Alat_ID id
@param trxName transaction
*/
public X_P_Alat (Properties ctx, int P_Alat_ID, String trxName)
{
super (ctx, P_Alat_ID, trxName);
/** if (P_Alat_ID == 0)
{
setDaysWorked (Env.ZERO);
setMaximumAnnualLeave (0);
setP_Alat_ID (0);
setResultingAnnualLeave (Env.ZERO);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Alat (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100783 */
public static final int Table_ID=2100783;

/** TableName=P_Alat */
public static final String Table_Name="P_Alat";

protected static KeyNamePair Model = new KeyNamePair(2100783,"P_Alat");

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
StringBuffer sb = new StringBuffer ("X_P_Alat[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Days Worked.
@param DaysWorked Days Worked */
public void setDaysWorked (BigDecimal DaysWorked)
{
if (DaysWorked == null) throw new IllegalArgumentException ("DaysWorked is mandatory.");
set_Value ("DaysWorked", DaysWorked);
}
/** Get Days Worked.
@return Days Worked */
public BigDecimal getDaysWorked() 
{
BigDecimal bd = (BigDecimal)get_Value("DaysWorked");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Maximum Annual Leave.
@param MaximumAnnualLeave Maximum Annual Leave */
public void setMaximumAnnualLeave (int MaximumAnnualLeave)
{
set_Value ("MaximumAnnualLeave", new Integer(MaximumAnnualLeave));
}
/** Get Maximum Annual Leave.
@return Maximum Annual Leave */
public int getMaximumAnnualLeave() 
{
Integer ii = (Integer)get_Value("MaximumAnnualLeave");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Annual Leave - Accumulation Table.
@param P_Alat_ID Annual Leave - Accumulation Table */
public void setP_Alat_ID (int P_Alat_ID)
{
if (P_Alat_ID < 1) throw new IllegalArgumentException ("P_Alat_ID is mandatory.");
set_ValueNoCheck ("P_Alat_ID", new Integer(P_Alat_ID));
}
/** Get Annual Leave - Accumulation Table.
@return Annual Leave - Accumulation Table */
public int getP_Alat_ID() 
{
Integer ii = (Integer)get_Value("P_Alat_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Resulting Annual Leave.
@param ResultingAnnualLeave Resulting Annual Leave */
public void setResultingAnnualLeave (BigDecimal ResultingAnnualLeave)
{
if (ResultingAnnualLeave == null) throw new IllegalArgumentException ("ResultingAnnualLeave is mandatory.");
set_Value ("ResultingAnnualLeave", ResultingAnnualLeave);
}
/** Get Resulting Annual Leave.
@return Resulting Annual Leave */
public BigDecimal getResultingAnnualLeave() 
{
BigDecimal bd = (BigDecimal)get_Value("ResultingAnnualLeave");
if (bd == null) return Env.ZERO;
return bd;
}
}
