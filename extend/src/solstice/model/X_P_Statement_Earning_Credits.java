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
/** Generated Model for P_Statement_Earning_Credits
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Statement_Earning_Credits extends PO
{
/** Standard Constructor
@param ctx context
@param P_Statement_Earning_Credits_ID id
@param trxName transaction
*/
public X_P_Statement_Earning_Credits (Properties ctx, int P_Statement_Earning_Credits_ID, String trxName)
{
super (ctx, P_Statement_Earning_Credits_ID, trxName);
/** if (P_Statement_Earning_Credits_ID == 0)
{
setP_Statement_Earning_Credits_ID (0);
setP_Statement_Earning_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Statement_Earning_Credits (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100734 */
public static final int Table_ID=2100734;

/** TableName=P_Statement_Earning_Credits */
public static final String Table_Name="P_Statement_Earning_Credits";

protected static KeyNamePair Model = new KeyNamePair(2100734,"P_Statement_Earning_Credits");

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
StringBuffer sb = new StringBuffer ("X_P_Statement_Earning_Credits[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Balance.
@param Balance Balance */
public void setBalance (BigDecimal Balance)
{
set_Value ("Balance", Balance);
}
/** Get Balance.
@return Balance */
public BigDecimal getBalance() 
{
BigDecimal bd = (BigDecimal)get_Value("Balance");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Calcul Unit.
@param CalculUnit Calcul Unit */
public void setCalculUnit (String CalculUnit)
{
if (CalculUnit != null && CalculUnit.length() > 10)
{
log.warning("Length > 10 - truncated");
CalculUnit = CalculUnit.substring(0,9);
}
set_Value ("CalculUnit", CalculUnit);
}
/** Get Calcul Unit.
@return Calcul Unit */
public String getCalculUnit() 
{
return (String)get_Value("CalculUnit");
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
/** Set Credits ID.
@param P_Statement_Earning_Credits_ID Credits ID */
public void setP_Statement_Earning_Credits_ID (int P_Statement_Earning_Credits_ID)
{
if (P_Statement_Earning_Credits_ID < 1) throw new IllegalArgumentException ("P_Statement_Earning_Credits_ID is mandatory.");
set_ValueNoCheck ("P_Statement_Earning_Credits_ID", new Integer(P_Statement_Earning_Credits_ID));
}
/** Get Credits ID.
@return Credits ID */
public int getP_Statement_Earning_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Statement_Earning_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_statement_earning_id.
@param P_Statement_Earning_ID P_statement_earning_id */
public void setP_Statement_Earning_ID (int P_Statement_Earning_ID)
{
if (P_Statement_Earning_ID < 1) throw new IllegalArgumentException ("P_Statement_Earning_ID is mandatory.");
set_Value ("P_Statement_Earning_ID", new Integer(P_Statement_Earning_ID));
}
/** Get P_statement_earning_id.
@return P_statement_earning_id */
public int getP_Statement_Earning_ID() 
{
Integer ii = (Integer)get_Value("P_Statement_Earning_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Variation.
@param PMovementVariation Variation */
public void setPMovementVariation (BigDecimal PMovementVariation)
{
set_Value ("PMovementVariation", PMovementVariation);
}
/** Get Variation.
@return Variation */
public BigDecimal getPMovementVariation() 
{
BigDecimal bd = (BigDecimal)get_Value("PMovementVariation");
if (bd == null) return Env.ZERO;
return bd;
}

/** Set OpeningBalance.
@param OpeningBalance OpeningBalance */
public void setOpeningBalance (BigDecimal OpeningBalance)
{
set_Value ("OpeningBalance", OpeningBalance);
}
/** Get OpeningBalance.
@return OpeningBalance */
public BigDecimal getOpeningBalance() 
{
BigDecimal bd = (BigDecimal)get_Value("OpeningBalance");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set UseBalance.
@param UseBalance UseBalance */
public void setUseBalance (BigDecimal UseBalance)
{
set_Value ("UseBalance", UseBalance);
}
/** Get UseBalance.
@return UseBalance */
public BigDecimal getUseBalance() 
{
BigDecimal bd = (BigDecimal)get_Value("UseBalance");
if (bd == null) return Env.ZERO;
return bd;
}

/** Set AccumulatedBalance.
@param AccumulatedBalance AccumulatedBalance */
public void setAccumulatedBalance (BigDecimal AccumulatedBalance)
{
set_Value ("AccumulatedBalance", AccumulatedBalance);
}
/** Get AccumulatedBalance.
@return AccumulatedBalance */
public BigDecimal getAccumulatedBalance() 
{
BigDecimal bd = (BigDecimal)get_Value("AccumulatedBalance");
if (bd == null) return Env.ZERO;
return bd;
}


}
