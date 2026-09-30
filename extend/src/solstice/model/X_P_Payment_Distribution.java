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
/** Generated Model for P_Payment_Distribution
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Payment_Distribution extends PO
{
/** Standard Constructor
@param ctx context
@param P_Payment_Distribution_ID id
@param trxName transaction
*/
public X_P_Payment_Distribution (Properties ctx, int P_Payment_Distribution_ID, String trxName)
{
super (ctx, P_Payment_Distribution_ID, trxName);
/** if (P_Payment_Distribution_ID == 0)
{
setP_Payment_Distribution_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Payment_Distribution (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000160 */
public static final int Table_ID=2000160;

/** TableName=P_Payment_Distribution */
public static final String Table_Name="P_Payment_Distribution";

protected static KeyNamePair Model = new KeyNamePair(2000160,"P_Payment_Distribution");

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
StringBuffer sb = new StringBuffer ("X_P_Payment_Distribution[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Amount.
@param Amount Amount in a defined currency */
public void setAmount (BigDecimal Amount)
{
set_Value ("Amount", Amount);
}
/** Get Amount.
@return Amount in a defined currency */
public BigDecimal getAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("Amount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Folio.
@param Folio Folio */
public void setFolio (String Folio)
{
if (Folio != null && Folio.length() > 120)
{
log.warning("Length > 120 - truncated");
Folio = Folio.substring(0,119);
}
set_Value ("Folio", Folio);
}
/** Get Folio.
@return Folio */
public String getFolio() 
{
return (String)get_Value("Folio");
}

/** P_Financial_Institution_ID AD_Reference_ID=2000050 */
public static final int P_FINANCIAL_INSTITUTION_ID_AD_Reference_ID=2000050;
/** Set Financial Institution.
@param P_Financial_Institution_ID Financial Institution */
public void setP_Financial_Institution_ID (int P_Financial_Institution_ID)
{
if (P_Financial_Institution_ID <= 0) set_Value ("P_Financial_Institution_ID", null);
 else 
set_Value ("P_Financial_Institution_ID", new Integer(P_Financial_Institution_ID));
}
/** Get Financial Institution.
@return Financial Institution */
public int getP_Financial_Institution_ID() 
{
Integer ii = (Integer)get_Value("P_Financial_Institution_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Payment_Distribution_ID.
@param P_Payment_Distribution_ID P_Payment_Distribution_ID */
public void setP_Payment_Distribution_ID (int P_Payment_Distribution_ID)
{
if (P_Payment_Distribution_ID < 1) throw new IllegalArgumentException ("P_Payment_Distribution_ID is mandatory.");
set_Value ("P_Payment_Distribution_ID", new Integer(P_Payment_Distribution_ID));
}
/** Get P_Payment_Distribution_ID.
@return P_Payment_Distribution_ID */
public int getP_Payment_Distribution_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Distribution_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Payment.
@param P_Payment_ID Payment */
public void setP_Payment_ID (int P_Payment_ID)
{
if (P_Payment_ID <= 0) set_Value ("P_Payment_ID", null);
 else 
set_Value ("P_Payment_ID", new Integer(P_Payment_ID));
}
/** Get Payment.
@return Payment */
public int getP_Payment_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Transit.
@param Transit Transit */
public void setTransit (String Transit)
{
if (Transit != null && Transit.length() > 120)
{
log.warning("Length > 120 - truncated");
Transit = Transit.substring(0,119);
}
set_Value ("Transit", Transit);
}
/** Get Transit.
@return Transit */
public String getTransit() 
{
return (String)get_Value("Transit");
}
}
