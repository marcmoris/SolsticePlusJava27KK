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
/** Generated Model for P_Method_Gain
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_P_Method_Gain.java,v 1.3 2008/04/23 19:50:34 jeaham01 Exp $ */
public class X_P_Method_Gain extends PO
{
/** Standard Constructor
@param ctx context
@param P_Method_Gain_ID id
@param trxName transaction
*/
public X_P_Method_Gain (Properties ctx, int P_Method_Gain_ID, String trxName)
{
super (ctx, P_Method_Gain_ID, trxName);
/** if (P_Method_Gain_ID == 0)
{
setIsBonus_Amount_Allow (true);	// Y
setIsFactor_Allow (true);	// Y
setIsFixed_Amount_Allow (true);	// Y
setIsProrate_Allow (true);	// Y
setIsType_Advance_Allow (true);	// Y
setIsType_Rate_Allow (true);	// Y
setIsUnit_Measure_Allow (true);	// Y
setName (null);
setP_Gain_Family_ID (0);
setP_Method_Gain_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Method_Gain (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000007 */
public static final int Table_ID=2000007;

/** TableName=P_Method_Gain */
public static final String Table_Name="P_Method_Gain";

protected static KeyNamePair Model = new KeyNamePair(2000007,"P_Method_Gain");

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
StringBuffer sb = new StringBuffer ("X_P_Method_Gain[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,254);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set Bonus Amount Allow.
@param IsBonus_Amount_Allow   */
public void setIsBonus_Amount_Allow (boolean IsBonus_Amount_Allow)
{
set_Value ("IsBonus_Amount_Allow", new Boolean(IsBonus_Amount_Allow));
}
/** Get Bonus Amount Allow.
@return   */
public boolean isBonus_Amount_Allow() 
{
Object oo = get_Value("IsBonus_Amount_Allow");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsColumnAdm_Allow.
@param IsColumnAdm_Allow   */
public void setIsColumnAdm_Allow (boolean IsColumnAdm_Allow)
{
set_Value ("IsColumnAdm_Allow", new Boolean(IsColumnAdm_Allow));
}
/** Get IsColumnAdm_Allow.
@return   */
public boolean isColumnAdm_Allow() 
{
Object oo = get_Value("IsColumnAdm_Allow");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Default.
@param IsDefault Default value */
public void setIsDefault (boolean IsDefault)
{
set_Value ("IsDefault", new Boolean(IsDefault));
}
/** Get Default.
@return Default value */
public boolean isDefault() 
{
Object oo = get_Value("IsDefault");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsEditHourlyRate.
@param IsEditHourlyRate IsEditHourlyRate */
public void setIsEditHourlyRate (boolean IsEditHourlyRate)
{
set_Value ("IsEditHourlyRate", new Boolean(IsEditHourlyRate));
}
/** Get IsEditHourlyRate.
@return IsEditHourlyRate */
public boolean isEditHourlyRate() 
{
Object oo = get_Value("IsEditHourlyRate");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsEditQuantity.
@param IsEditQuantity IsEditQuantity */
public void setIsEditQuantity (boolean IsEditQuantity)
{
set_Value ("IsEditQuantity", new Boolean(IsEditQuantity));
}
/** Get IsEditQuantity.
@return IsEditQuantity */
public boolean isEditQuantity() 
{
Object oo = get_Value("IsEditQuantity");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Factor Allow.
@param IsFactor_Allow   */
public void setIsFactor_Allow (boolean IsFactor_Allow)
{
set_Value ("IsFactor_Allow", new Boolean(IsFactor_Allow));
}
/** Get Factor Allow.
@return   */
public boolean isFactor_Allow() 
{
Object oo = get_Value("IsFactor_Allow");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Fixed Amount Allow.
@param IsFixed_Amount_Allow   */
public void setIsFixed_Amount_Allow (boolean IsFixed_Amount_Allow)
{
set_Value ("IsFixed_Amount_Allow", new Boolean(IsFixed_Amount_Allow));
}
/** Get Fixed Amount Allow.
@return   */
public boolean isFixed_Amount_Allow() 
{
Object oo = get_Value("IsFixed_Amount_Allow");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Prorate Allow.
@param IsProrate_Allow   */
public void setIsProrate_Allow (boolean IsProrate_Allow)
{
set_Value ("IsProrate_Allow", new Boolean(IsProrate_Allow));
}
/** Get Prorate Allow.
@return   */
public boolean isProrate_Allow() 
{
Object oo = get_Value("IsProrate_Allow");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Type Advance Allow.
@param IsType_Advance_Allow   */
public void setIsType_Advance_Allow (boolean IsType_Advance_Allow)
{
set_Value ("IsType_Advance_Allow", new Boolean(IsType_Advance_Allow));
}
/** Get Type Advance Allow.
@return   */
public boolean isType_Advance_Allow() 
{
Object oo = get_Value("IsType_Advance_Allow");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Type Rate Allow.
@param IsType_Rate_Allow   */
public void setIsType_Rate_Allow (boolean IsType_Rate_Allow)
{
set_Value ("IsType_Rate_Allow", new Boolean(IsType_Rate_Allow));
}
/** Get Type Rate Allow.
@return   */
public boolean isType_Rate_Allow() 
{
Object oo = get_Value("IsType_Rate_Allow");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Unit Measure Allow.
@param IsUnit_Measure_Allow   */
public void setIsUnit_Measure_Allow (boolean IsUnit_Measure_Allow)
{
set_Value ("IsUnit_Measure_Allow", new Boolean(IsUnit_Measure_Allow));
}
/** Get Unit Measure Allow.
@return   */
public boolean isUnit_Measure_Allow() 
{
Object oo = get_Value("IsUnit_Measure_Allow");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}

/** Management_Type AD_Reference_ID=2000002 */
public static final int MANAGEMENT_TYPE_AD_Reference_ID=2000002;
/** Amount  Only = 1 */
public static final String MANAGEMENT_TYPE_AmountOnly = "1";
/** Quantity and Amount Calculated = 2 */
public static final String MANAGEMENT_TYPE_QuantityAndAmountCalculated = "2";
/** Quantity entry only = 3 */
public static final String MANAGEMENT_TYPE_QuantityEntryOnly = "3";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isManagement_TypeValid (String test)
{
return test == null || test.equals("1") || test.equals("2") || test.equals("3");
}
/** Set Management Type.
@param Management_Type   */
public void setManagement_Type (String Management_Type)
{
if (!isManagement_TypeValid(Management_Type))
throw new IllegalArgumentException ("Management_Type Invalid value - " + Management_Type + " - Reference_ID=2000002 - 1 - 2 - 3");
if (Management_Type != null && Management_Type.length() > 1)
{
log.warning("Length > 1 - truncated");
Management_Type = Management_Type.substring(0,0);
}
set_Value ("Management_Type", Management_Type);
}
/** Get Management Type.
@return   */
public String getManagement_Type() 
{
return (String)get_Value("Management_Type");
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 240)
{
log.warning("Length > 240 - truncated");
Name = Name.substring(0,239);
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

/** P_Gain_Family_ID AD_Reference_ID=2000021 */
public static final int P_GAIN_FAMILY_ID_AD_Reference_ID=2000021;
/** Set Gain Family.
@param P_Gain_Family_ID Gain Family  */
public void setP_Gain_Family_ID (int P_Gain_Family_ID)
{
if (P_Gain_Family_ID < 1) throw new IllegalArgumentException ("P_Gain_Family_ID is mandatory.");
set_Value ("P_Gain_Family_ID", new Integer(P_Gain_Family_ID));
}
/** Get Gain Family.
@return Gain Family  */
public int getP_Gain_Family_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Family_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Method.
@param P_Method_Gain_ID Method */
public void setP_Method_Gain_ID (int P_Method_Gain_ID)
{
if (P_Method_Gain_ID < 1) throw new IllegalArgumentException ("P_Method_Gain_ID is mandatory.");
set_ValueNoCheck ("P_Method_Gain_ID", new Integer(P_Method_Gain_ID));
}
/** Get Method.
@return Method */
public int getP_Method_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Method_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
