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
/** Generated Model for P_Gain_Bound
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Gain_Bound extends PO
{
/** Standard Constructor
@param ctx context
@param P_Gain_Bound_ID id
@param trxName transaction
*/
public X_P_Gain_Bound (Properties ctx, int P_Gain_Bound_ID, String trxName)
{
super (ctx, P_Gain_Bound_ID, trxName);
/** if (P_Gain_Bound_ID == 0)
{
setLimitMax (Env.ZERO);
setLimitMin (Env.ZERO);
setMultiplyRate (Env.ZERO);
setP_Credits_ID (0);
setP_Gain_Bound_ID (0);
setP_Gain_ID (0);
setP_Job_Type_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Gain_Bound (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100769 */
public static final int Table_ID=2100769;

/** TableName=P_Gain_Bound */
public static final String Table_Name="P_Gain_Bound";

protected static KeyNamePair Model = new KeyNamePair(2100769,"P_Gain_Bound");

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
StringBuffer sb = new StringBuffer ("X_P_Gain_Bound[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Maximum.
@param LimitMax Maximum */
public void setLimitMax (BigDecimal LimitMax)
{
if (LimitMax == null) throw new IllegalArgumentException ("LimitMax is mandatory.");
set_Value ("LimitMax", LimitMax);
}
/** Get Maximum.
@return Maximum */
public BigDecimal getLimitMax() 
{
BigDecimal bd = (BigDecimal)get_Value("LimitMax");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Limit minimum.
@param LimitMin Limit minimum */
public void setLimitMin (BigDecimal LimitMin)
{
if (LimitMin == null) throw new IllegalArgumentException ("LimitMin is mandatory.");
set_Value ("LimitMin", LimitMin);
}
/** Get Limit minimum.
@return Limit minimum */
public BigDecimal getLimitMin() 
{
BigDecimal bd = (BigDecimal)get_Value("LimitMin");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Multiply Rate.
@param MultiplyRate Rate to multiple the source by to calculate the target. */
public void setMultiplyRate (BigDecimal MultiplyRate)
{
if (MultiplyRate == null) throw new IllegalArgumentException ("MultiplyRate is mandatory.");
set_Value ("MultiplyRate", MultiplyRate);
}
/** Get Multiply Rate.
@return Rate to multiple the source by to calculate the target. */
public BigDecimal getMultiplyRate() 
{
BigDecimal bd = (BigDecimal)get_Value("MultiplyRate");
if (bd == null) return Env.ZERO;
return bd;
}

/** P_Credits_ID AD_Reference_ID=2100318 */
public static final int P_CREDITS_ID_AD_Reference_ID=2100318;
/** Set Credits.
@param P_Credits_ID Credits */
public void setP_Credits_ID (int P_Credits_ID)
{
if (P_Credits_ID < 1) throw new IllegalArgumentException ("P_Credits_ID is mandatory.");
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
/** Set P_Gain_Bound_ID.
@param P_Gain_Bound_ID P_Gain_Bound_ID */
public void setP_Gain_Bound_ID (int P_Gain_Bound_ID)
{
if (P_Gain_Bound_ID < 1) throw new IllegalArgumentException ("P_Gain_Bound_ID is mandatory.");
set_ValueNoCheck ("P_Gain_Bound_ID", new Integer(P_Gain_Bound_ID));
}
/** Get P_Gain_Bound_ID.
@return P_Gain_Bound_ID */
public int getP_Gain_Bound_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Bound_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_ID AD_Reference_ID=2100317 */
public static final int P_GAIN_ID_AD_Reference_ID=2100317;
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

/** P_Job_Type_ID AD_Reference_ID=2000054 */
public static final int P_JOB_TYPE_ID_AD_Reference_ID=2000054;
/** Set Job Type.
@param P_Job_Type_ID Job Type */
public void setP_Job_Type_ID (int P_Job_Type_ID)
{
if (P_Job_Type_ID < 1) throw new IllegalArgumentException ("P_Job_Type_ID is mandatory.");
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
}
