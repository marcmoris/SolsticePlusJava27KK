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
/** Generated Model for P_SelfFinancedLeave
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_SelfFinancedLeave extends PO
{
/** Standard Constructor
@param ctx context
@param P_SelfFinancedLeave_ID id
@param trxName transaction
*/
public X_P_SelfFinancedLeave (Properties ctx, int P_SelfFinancedLeave_ID, String trxName)
{
super (ctx, P_SelfFinancedLeave_ID, trxName);
/** if (P_SelfFinancedLeave_ID == 0)
{
setName (null);
setP_SelfFinancedLeave_ID (0);
setReductionPercentage (Env.ZERO);
setSalaryPercentage (Env.ZERO);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_SelfFinancedLeave (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100694 */
public static final int Table_ID=2100694;

/** TableName=P_SelfFinancedLeave */
public static final String Table_Name="P_SelfFinancedLeave";

protected static KeyNamePair Model = new KeyNamePair(2100694,"P_SelfFinancedLeave");

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
StringBuffer sb = new StringBuffer ("X_P_SelfFinancedLeave[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 60)
{
log.warning("Length > 60 - truncated");
Name = Name.substring(0,59);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Set Self Financed Leave Plan.
@param P_SelfFinancedLeave_ID Self Financed Leave Plan */
public void setP_SelfFinancedLeave_ID (int P_SelfFinancedLeave_ID)
{
if (P_SelfFinancedLeave_ID < 1) throw new IllegalArgumentException ("P_SelfFinancedLeave_ID is mandatory.");
set_ValueNoCheck ("P_SelfFinancedLeave_ID", new Integer(P_SelfFinancedLeave_ID));
}
/** Get Self Financed Leave Plan.
@return Self Financed Leave Plan */
public int getP_SelfFinancedLeave_ID() 
{
Integer ii = (Integer)get_Value("P_SelfFinancedLeave_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Reduction Percentage.
@param ReductionPercentage Reduction Percentage */
public void setReductionPercentage (BigDecimal ReductionPercentage)
{
if (ReductionPercentage == null) throw new IllegalArgumentException ("ReductionPercentage is mandatory.");
set_Value ("ReductionPercentage", ReductionPercentage);
}
/** Get Reduction Percentage.
@return Reduction Percentage */
public BigDecimal getReductionPercentage() 
{
BigDecimal bd = (BigDecimal)get_Value("ReductionPercentage");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Salary Percentage.
@param SalaryPercentage Salary Percentage */
public void setSalaryPercentage (BigDecimal SalaryPercentage)
{
if (SalaryPercentage == null) throw new IllegalArgumentException ("SalaryPercentage is mandatory.");
set_Value ("SalaryPercentage", SalaryPercentage);
}
/** Get Salary Percentage.
@return Salary Percentage */
public BigDecimal getSalaryPercentage() 
{
BigDecimal bd = (BigDecimal)get_Value("SalaryPercentage");
if (bd == null) return Env.ZERO;
return bd;
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
