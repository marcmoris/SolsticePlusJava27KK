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
/** Generated Model for P_Gain_Hst
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Gain_Hst extends PO
{
/** Standard Constructor
@param ctx context
@param P_Gain_Hst_ID id
@param trxName transaction
*/
public X_P_Gain_Hst (Properties ctx, int P_Gain_Hst_ID, String trxName)
{
super (ctx, P_Gain_Hst_ID, trxName);
/** if (P_Gain_Hst_ID == 0)
{
setAccessible (false);
setBonus_Amount (Env.ZERO);
setEffectIn (new Timestamp(System.currentTimeMillis()));
setFixed_Amount (Env.ZERO);
setIsSummary (false);
setName (null);
setP_Gain_Family_ID (0);
setP_Gain_Hst_ID (0);
setP_Gain_ID (0);
setP_UOM_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Gain_Hst (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000152 */
public static final int Table_ID=2000152;

/** TableName=P_Gain_Hst */
public static final String Table_Name="P_Gain_Hst";

protected static KeyNamePair Model = new KeyNamePair(2000152,"P_Gain_Hst");

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
StringBuffer sb = new StringBuffer ("X_P_Gain_Hst[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Accessible.
@param Accessible Accessible */
public void setAccessible (boolean Accessible)
{
set_Value ("Accessible", new Boolean(Accessible));
}
/** Get Accessible.
@return Accessible */
public boolean isAccessible() 
{
Object oo = get_Value("Accessible");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Bonus Amount.
@param Bonus_Amount Bonus Amount */
public void setBonus_Amount (BigDecimal Bonus_Amount)
{
if (Bonus_Amount == null) throw new IllegalArgumentException ("Bonus_Amount is mandatory.");
set_Value ("Bonus_Amount", Bonus_Amount);
}
/** Get Bonus Amount.
@return Bonus Amount */
public BigDecimal getBonus_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("Bonus_Amount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Color.
@param Color Color */
public void setColor (String Color)
{
if (Color != null && Color.length() > 22)
{
log.warning("Length > 22 - truncated");
Color = Color.substring(0,21);
}
set_Value ("Color", Color);
}
/** Get Color.
@return Color */
public String getColor() 
{
return (String)get_Value("Color");
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
/** Set Effect In.
@param EffectIn Date when information in effect  */
public void setEffectIn (Timestamp EffectIn)
{
if (EffectIn == null) throw new IllegalArgumentException ("EffectIn is mandatory.");
set_Value ("EffectIn", EffectIn);
}
/** Get Effect In.
@return Date when information in effect  */
public Timestamp getEffectIn() 
{
return (Timestamp)get_Value("EffectIn");
}
/** Set Fixed Amount.
@param Fixed_Amount   */
public void setFixed_Amount (BigDecimal Fixed_Amount)
{
if (Fixed_Amount == null) throw new IllegalArgumentException ("Fixed_Amount is mandatory.");
set_Value ("Fixed_Amount", Fixed_Amount);
}
/** Get Fixed Amount.
@return   */
public BigDecimal getFixed_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("Fixed_Amount");
if (bd == null) return Env.ZERO;
return bd;
}

/** GainUnit AD_Reference_ID=2100377 */
public static final int GAINUNIT_AD_Reference_ID=2100377;
/** Quantity = Q */
public static final String GAINUNIT_Quantity = "Q";
/** Amount = A */
public static final String GAINUNIT_Amount = "A";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isGainUnitValid (String test)
{
return test == null || test.equals("Q") || test.equals("A");
}
/** Set Unit.
@param GainUnit Unit */
public void setGainUnit (String GainUnit)
{
if (!isGainUnitValid(GainUnit))
throw new IllegalArgumentException ("GainUnit Invalid value - " + GainUnit + " - Reference_ID=2100377 - Q - A");
if (GainUnit != null && GainUnit.length() > 1)
{
log.warning("Length > 1 - truncated");
GainUnit = GainUnit.substring(0,0);
}
set_Value ("GainUnit", GainUnit);
}
/** Get Unit.
@return Unit */
public String getGainUnit() 
{
return (String)get_Value("GainUnit");
}
/** Set Absence.
@param IsAbsence Absence */
public void setIsAbsence (boolean IsAbsence)
{
set_Value ("IsAbsence", new Boolean(IsAbsence));
}
/** Get Absence.
@return Absence */
public boolean isAbsence() 
{
Object oo = get_Value("IsAbsence");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Productive .
@param IsProductive this gain egal a productive ti */
public void setIsProductive (boolean IsProductive)
{
set_Value ("IsProductive", new Boolean(IsProductive));
}
/** Get Productive .
@return this gain egal a productive ti */
public boolean isProductive() 
{
Object oo = get_Value("IsProductive");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Summary Level.
@param IsSummary This is a summary entity */
public void setIsSummary (boolean IsSummary)
{
set_Value ("IsSummary", new Boolean(IsSummary));
}
/** Get Summary Level.
@return This is a summary entity */
public boolean isSummary() 
{
Object oo = get_Value("IsSummary");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 120)
{
log.warning("Length > 120 - truncated");
Name = Name.substring(0,119);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Set Advance.
@param P_Advance_ID Advance */
public void setP_Advance_ID (int P_Advance_ID)
{
if (P_Advance_ID <= 0) set_Value ("P_Advance_ID", null);
 else 
set_Value ("P_Advance_ID", new Integer(P_Advance_ID));
}
/** Get Advance.
@return Advance */
public int getP_Advance_ID() 
{
Integer ii = (Integer)get_Value("P_Advance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Admissibility Column.
@param P_Column_Adm_ID Admissibility Column */
public void setP_Column_Adm_ID (int P_Column_Adm_ID)
{
if (P_Column_Adm_ID <= 0) set_Value ("P_Column_Adm_ID", null);
 else 
set_Value ("P_Column_Adm_ID", new Integer(P_Column_Adm_ID));
}
/** Get Admissibility Column.
@return Admissibility Column */
public int getP_Column_Adm_ID() 
{
Integer ii = (Integer)get_Value("P_Column_Adm_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Credits.
@param P_Credits_ID Credits */
public void setP_Credits_ID (int P_Credits_ID)
{
if (P_Credits_ID <= 0) set_Value ("P_Credits_ID", null);
 else 
set_Value ("P_Credits_ID", new Integer(P_Credits_ID));
}
/** Get Credits.
@return Credits */
public int getP_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_Category_ID AD_Reference_ID=2100396 */
public static final int P_GAIN_CATEGORY_ID_AD_Reference_ID=2100396;
/** Set Gain Category.
@param P_Gain_Category_ID Gain Category */
public void setP_Gain_Category_ID (int P_Gain_Category_ID)
{
if (P_Gain_Category_ID <= 0) set_Value ("P_Gain_Category_ID", null);
 else 
set_Value ("P_Gain_Category_ID", new Integer(P_Gain_Category_ID));
}
/** Get Gain Category.
@return Gain Category */
public int getP_Gain_Category_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Category_ID");
if (ii == null) return 0;
return ii.intValue();
}
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

/** P_Gain_Group_ID AD_Reference_ID=2100378 */
public static final int P_GAIN_GROUP_ID_AD_Reference_ID=2100378;
/** Set Gain Group.
@param P_Gain_Group_ID Gain Group */
public void setP_Gain_Group_ID (int P_Gain_Group_ID)
{
if (P_Gain_Group_ID <= 0) set_Value ("P_Gain_Group_ID", null);
 else 
set_Value ("P_Gain_Group_ID", new Integer(P_Gain_Group_ID));
}
/** Get Gain Group.
@return Gain Group */
public int getP_Gain_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Gain_Hst_ID.
@param P_Gain_Hst_ID P_Gain_Hst_ID */
public void setP_Gain_Hst_ID (int P_Gain_Hst_ID)
{
if (P_Gain_Hst_ID < 1) throw new IllegalArgumentException ("P_Gain_Hst_ID is mandatory.");
set_Value ("P_Gain_Hst_ID", new Integer(P_Gain_Hst_ID));
}
/** Get P_Gain_Hst_ID.
@return P_Gain_Hst_ID */
public int getP_Gain_Hst_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Hst_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set Method.
@param P_Method_Gain_ID Method */
public void setP_Method_Gain_ID (int P_Method_Gain_ID)
{
if (P_Method_Gain_ID <= 0) set_Value ("P_Method_Gain_ID", null);
 else 
set_Value ("P_Method_Gain_ID", new Integer(P_Method_Gain_ID));
}
/** Get Method.
@return Method */
public int getP_Method_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Method_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_UOM_ID AD_Reference_ID=2100342 */
public static final int P_UOM_ID_AD_Reference_ID=2100342;
/** Set Unit.
@param P_UOM_ID Unit of Measure */
public void setP_UOM_ID (int P_UOM_ID)
{
if (P_UOM_ID < 1) throw new IllegalArgumentException ("P_UOM_ID is mandatory.");
set_Value ("P_UOM_ID", new Integer(P_UOM_ID));
}
/** Get Unit.
@return Unit of Measure */
public int getP_UOM_ID() 
{
Integer ii = (Integer)get_Value("P_UOM_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Type Rate.
@param Type_Rate   */
public void setType_Rate (String Type_Rate)
{
if (Type_Rate != null && Type_Rate.length() > 1)
{
log.warning("Length > 1 - truncated");
Type_Rate = Type_Rate.substring(0,0);
}
set_Value ("Type_Rate", Type_Rate);
}
/** Get Type Rate.
@return   */
public String getType_Rate() 
{
return (String)get_Value("Type_Rate");
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 60)
{
log.warning("Length > 60 - truncated");
Value = Value.substring(0,59);
}
set_Value ("Value", Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
/** Set Visible.
@param Visible Visible */
public void setVisible (boolean Visible)
{
set_Value ("Visible", new Boolean(Visible));
}
/** Get Visible.
@return Visible */
public boolean isVisible() 
{
Object oo = get_Value("Visible");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
}
