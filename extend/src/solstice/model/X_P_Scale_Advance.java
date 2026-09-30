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
/** Generated Model for P_Scale_Advance
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Scale_Advance extends PO
{
/** Standard Constructor
@param ctx context
@param P_Scale_Advance_ID id
@param trxName transaction
*/
public X_P_Scale_Advance (Properties ctx, int P_Scale_Advance_ID, String trxName)
{
super (ctx, P_Scale_Advance_ID, trxName);
/** if (P_Scale_Advance_ID == 0)
{
setP_Collective_Labour_Agr_ID (0);
setP_Scale_Advance_ID (0);
setRuleIndex (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Scale_Advance (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100773 */
public static final int Table_ID=2100773;

/** TableName=P_Scale_Advance */
public static final String Table_Name="P_Scale_Advance";

protected static KeyNamePair Model = new KeyNamePair(2100773,"P_Scale_Advance");

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
StringBuffer sb = new StringBuffer ("X_P_Scale_Advance[").append(get_ID()).append("]");
return sb.toString();
}

/** IntervalType AD_Reference_ID=2100409 */
public static final int INTERVALTYPE_AD_Reference_ID=2100409;
/** 1 fois au 4 mois = A */
public static final String INTERVALTYPE_1FoisAu4Mois = "A";
/** 1 fois au 2 ans = X */
public static final String INTERVALTYPE_1FoisAu2Ans = "X";
/** 1 fois par année = Y */
public static final String INTERVALTYPE_1FoisParAnnée = "Y";
/** 1 fois par mois = M */
public static final String INTERVALTYPE_1FoisParMois = "M";
/** Selon nombre de jours dans une banque = D */
public static final String INTERVALTYPE_SelonNombreDeJoursDansUneBanque = "D";
/** 1 fois par année ( Date début période ) = Z */
public static final String INTERVALTYPE_1FoisParAnnéeDateDébutPériode = "Z";
/** 2 fois par année ( Date fixe ) = E */
public static final String INTERVALTYPE_2FoisParAnnéeDateFixe = "E";
/** 2 fois par année ( Date début période ) = F */
public static final String INTERVALTYPE_2FoisParAnnéeDateDébutPériode = "F";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isIntervalTypeValid (String test)
{
return test == null || test.equals("A") || test.equals("X") || test.equals("Y") || test.equals("M") || test.equals("D") || test.equals("Z") || test.equals("E") || test.equals("F");
}
/** Set Interval type.
@param IntervalType Interval type */
public void setIntervalType (String IntervalType)
{
if (!isIntervalTypeValid(IntervalType))
throw new IllegalArgumentException ("IntervalType Invalid value - " + IntervalType + " - Reference_ID=2100409 - A - X - Y - M - D - Z - E - F");
if (IntervalType != null && IntervalType.length() > 1)
{
log.warning("Length > 1 - truncated");
IntervalType = IntervalType.substring(0,0);
}
set_Value ("IntervalType", IntervalType);
}
/** Get Interval type.
@return Interval type */
public String getIntervalType() 
{
return (String)get_Value("IntervalType");
}

/** P_Collective_Labour_Agr_ID AD_Reference_ID=2000006 */
public static final int P_COLLECTIVE_LABOUR_AGR_ID_AD_Reference_ID=2000006;
/** Set Collective Labour Agreement.
@param P_Collective_Labour_Agr_ID Collective Labour Agreement */
public void setP_Collective_Labour_Agr_ID (int P_Collective_Labour_Agr_ID)
{
if (P_Collective_Labour_Agr_ID < 1) throw new IllegalArgumentException ("P_Collective_Labour_Agr_ID is mandatory.");
set_Value ("P_Collective_Labour_Agr_ID", new Integer(P_Collective_Labour_Agr_ID));
}
/** Get Collective Labour Agreement.
@return Collective Labour Agreement */
public int getP_Collective_Labour_Agr_ID() 
{
Integer ii = (Integer)get_Value("P_Collective_Labour_Agr_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Credits_ID AD_Reference_ID=2100318 */
public static final int P_CREDITS_ID_AD_Reference_ID=2100318;
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
/** Set Job Type.
@param P_Job_Type_ID Job Type */
public void setP_Job_Type_ID (int P_Job_Type_ID)
{
if (P_Job_Type_ID <= 0) set_Value ("P_Job_Type_ID", null);
 else 
set_Value ("P_Job_Type_ID", new Integer(P_Job_Type_ID));
}
/** Get Job Type.
@return Job Type */
public int getP_Job_Type_ID() 
{
Integer ii = (Integer)get_Value("P_Job_Type_ID");
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
/** Set Advance Rule.
@param P_Scale_Advance_ID Advance Rule */
public void setP_Scale_Advance_ID (int P_Scale_Advance_ID)
{
if (P_Scale_Advance_ID < 1) throw new IllegalArgumentException ("P_Scale_Advance_ID is mandatory.");
set_ValueNoCheck ("P_Scale_Advance_ID", new Integer(P_Scale_Advance_ID));
}
/** Get Advance Rule.
@return Advance Rule */
public int getP_Scale_Advance_ID() 
{
Integer ii = (Integer)get_Value("P_Scale_Advance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Quantity.
@param Quantity Quantity */
public void setQuantity (int Quantity)
{
set_Value ("Quantity", new Integer(Quantity));
}
/** Get Quantity.
@return Quantity */
public int getQuantity() 
{
Integer ii = (Integer)get_Value("Quantity");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Rule Index.
@param RuleIndex Rule Index */
public void setRuleIndex (int RuleIndex)
{
set_Value ("RuleIndex", new Integer(RuleIndex));
}
/** Get Rule Index.
@return Rule Index */
public int getRuleIndex() 
{
Integer ii = (Integer)get_Value("RuleIndex");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Specific Date.
@param SpecificDate1 Specific Date */
public void setSpecificDate1 (String SpecificDate1)
{
if (SpecificDate1 != null && SpecificDate1.length() > 7)
{
log.warning("Length > 7 - truncated");
SpecificDate1 = SpecificDate1.substring(0,6);
}
set_Value ("SpecificDate1", SpecificDate1);
}
/** Get Specific Date.
@return Specific Date */
public String getSpecificDate1() 
{
return (String)get_Value("SpecificDate1");
}
/** Set Specific Date 2.
@param SpecificDate2 Specific Date 2 */
public void setSpecificDate2 (String SpecificDate2)
{
if (SpecificDate2 != null && SpecificDate2.length() > 7)
{
log.warning("Length > 7 - truncated");
SpecificDate2 = SpecificDate2.substring(0,6);
}
set_Value ("SpecificDate2", SpecificDate2);
}
/** Get Specific Date 2.
@return Specific Date 2 */
public String getSpecificDate2() 
{
return (String)get_Value("SpecificDate2");
}
/** Set Step From.
@param StepFrom Step From */
public void setStepFrom (String StepFrom)
{
if (StepFrom != null && StepFrom.length() > 30)
{
log.warning("Length > 30 - truncated");
StepFrom = StepFrom.substring(0,29);
}
set_Value ("StepFrom", StepFrom);
}
/** Get Step From.
@return Step From */
public String getStepFrom() 
{
return (String)get_Value("StepFrom");
}
/** Set To.
@param StepTo To */
public void setStepTo (String StepTo)
{
if (StepTo != null && StepTo.length() > 30)
{
log.warning("Length > 30 - truncated");
StepTo = StepTo.substring(0,29);
}
set_Value ("StepTo", StepTo);
}
/** Get To.
@return To */
public String getStepTo() 
{
return (String)get_Value("StepTo");
}
}
