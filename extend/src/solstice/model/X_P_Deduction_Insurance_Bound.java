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
/** Generated Model for P_Deduction_Insurance_Bound
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Deduction_Insurance_Bound extends PO
{
/** Standard Constructor
@param ctx context
@param P_Deduction_Insurance_Bound_ID id
@param trxName transaction
*/
public X_P_Deduction_Insurance_Bound (Properties ctx, int P_Deduction_Insurance_Bound_ID, String trxName)
{
super (ctx, P_Deduction_Insurance_Bound_ID, trxName);
/** if (P_Deduction_Insurance_Bound_ID == 0)
{
setAgeLimitMax (0);
setAgeLimitMin (0);
setP_Deduction_Insurance_Bound_ID (0);
setP_Deduction_Insurance_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Deduction_Insurance_Bound (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000069 */
public static final int Table_ID=2000069;

/** TableName=P_Deduction_Insurance_Bound */
public static final String Table_Name="P_Deduction_Insurance_Bound";

protected static KeyNamePair Model = new KeyNamePair(2000069,"P_Deduction_Insurance_Bound");

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
StringBuffer sb = new StringBuffer ("X_P_Deduction_Insurance_Bound[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Age Limit Max.
@param AgeLimitMax Age Limit Max */
public void setAgeLimitMax (int AgeLimitMax)
{
set_Value ("AgeLimitMax", new Integer(AgeLimitMax));
}
/** Get Age Limit Max.
@return Age Limit Max */
public int getAgeLimitMax() 
{
Integer ii = (Integer)get_Value("AgeLimitMax");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Age Limit Min.
@param AgeLimitMin Age Limit Min */
public void setAgeLimitMin (int AgeLimitMin)
{
set_Value ("AgeLimitMin", new Integer(AgeLimitMin));
}
/** Get Age Limit Min.
@return Age Limit Min */
public int getAgeLimitMin() 
{
Integer ii = (Integer)get_Value("AgeLimitMin");
if (ii == null) return 0;
return ii.intValue();
}
/** Set No Smoker Femal.
@param NoSmokerFemal No Smoker Femal */
public void setNoSmokerFemal (BigDecimal NoSmokerFemal)
{
set_Value ("NoSmokerFemal", NoSmokerFemal);
}
/** Get No Smoker Femal.
@return No Smoker Femal */
public BigDecimal getNoSmokerFemal() 
{
BigDecimal bd = (BigDecimal)get_Value("NoSmokerFemal");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set No Smoker Male.
@param NoSmokerMale No Smoker Male */
public void setNoSmokerMale (BigDecimal NoSmokerMale)
{
set_Value ("NoSmokerMale", NoSmokerMale);
}
/** Get No Smoker Male.
@return No Smoker Male */
public BigDecimal getNoSmokerMale() 
{
BigDecimal bd = (BigDecimal)get_Value("NoSmokerMale");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Age Bound .
@param P_Deduction_Insurance_Bound_ID Age Bound  */
public void setP_Deduction_Insurance_Bound_ID (int P_Deduction_Insurance_Bound_ID)
{
if (P_Deduction_Insurance_Bound_ID < 1) throw new IllegalArgumentException ("P_Deduction_Insurance_Bound_ID is mandatory.");
set_Value ("P_Deduction_Insurance_Bound_ID", new Integer(P_Deduction_Insurance_Bound_ID));
}
/** Get Age Bound .
@return Age Bound  */
public int getP_Deduction_Insurance_Bound_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_Insurance_Bound_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Insurance.
@param P_Deduction_Insurance_ID Insurance */
public void setP_Deduction_Insurance_ID (int P_Deduction_Insurance_ID)
{
if (P_Deduction_Insurance_ID < 1) throw new IllegalArgumentException ("P_Deduction_Insurance_ID is mandatory.");
set_Value ("P_Deduction_Insurance_ID", new Integer(P_Deduction_Insurance_ID));
}
/** Get Insurance.
@return Insurance */
public int getP_Deduction_Insurance_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_Insurance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Smoker Femal.
@param SmokerFemal Smoker Femal */
public void setSmokerFemal (BigDecimal SmokerFemal)
{
set_Value ("SmokerFemal", SmokerFemal);
}
/** Get Smoker Femal.
@return Smoker Femal */
public BigDecimal getSmokerFemal() 
{
BigDecimal bd = (BigDecimal)get_Value("SmokerFemal");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Smoker Male.
@param SmokerMale Smoker Male */
public void setSmokerMale (BigDecimal SmokerMale)
{
set_Value ("SmokerMale", SmokerMale);
}
/** Get Smoker Male.
@return Smoker Male */
public BigDecimal getSmokerMale() 
{
BigDecimal bd = (BigDecimal)get_Value("SmokerMale");
if (bd == null) return Env.ZERO;
return bd;
}
}
