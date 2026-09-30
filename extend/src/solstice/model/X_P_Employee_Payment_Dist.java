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
/** Generated Model for P_Employee_Payment_Dist
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Payment_Dist extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Payment_Dist_ID id
@param trxName transaction
*/
public X_P_Employee_Payment_Dist (Properties ctx, int P_Employee_Payment_Dist_ID, String trxName)
{
super (ctx, P_Employee_Payment_Dist_ID, trxName);
/** if (P_Employee_Payment_Dist_ID == 0)
{
setIsResidual (false);	// N
setP_Employee_ID (0);
setP_Employee_Payment_Dist_ID (0);
setP_Financial_Institution_ID (0);
setPriority (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Payment_Dist (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000154 */
public static final int Table_ID=2000154;

/** TableName=P_Employee_Payment_Dist */
public static final String Table_Name="P_Employee_Payment_Dist";

protected static KeyNamePair Model = new KeyNamePair(2000154,"P_Employee_Payment_Dist");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Payment_Dist[").append(get_ID()).append("]");
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
if (Folio != null && Folio.length() > 60)
{
log.warning("Length > 60 - truncated");
Folio = Folio.substring(0,59);
}
set_Value ("Folio", Folio);
}
/** Get Folio.
@return Folio */
public String getFolio() 
{
return (String)get_Value("Folio");
}
/** Set Residual.
@param IsResidual Residual */
public void setIsResidual (boolean IsResidual)
{
set_Value ("IsResidual", new Boolean(IsResidual));
}
/** Get Residual.
@return Residual */
public boolean isResidual() 
{
Object oo = get_Value("IsResidual");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
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
/** Set Employee Payment Distribution.
@param P_Employee_Payment_Dist_ID Employee Payment Distribution */
public void setP_Employee_Payment_Dist_ID (int P_Employee_Payment_Dist_ID)
{
if (P_Employee_Payment_Dist_ID < 1) throw new IllegalArgumentException ("P_Employee_Payment_Dist_ID is mandatory.");
set_Value ("P_Employee_Payment_Dist_ID", new Integer(P_Employee_Payment_Dist_ID));
}
/** Get Employee Payment Distribution.
@return Employee Payment Distribution */
public int getP_Employee_Payment_Dist_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Payment_Dist_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Financial_Institution_ID AD_Reference_ID=2000050 */
public static final int P_FINANCIAL_INSTITUTION_ID_AD_Reference_ID=2000050;
/** Set Financial Institution.
@param P_Financial_Institution_ID Financial Institution */
public void setP_Financial_Institution_ID (int P_Financial_Institution_ID)
{
if (P_Financial_Institution_ID < 1) throw new IllegalArgumentException ("P_Financial_Institution_ID is mandatory.");
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
/** Set Priority.
@param Priority Indicates if this request is of a high, medium or low priority. */
public void setPriority (int Priority)
{
set_Value ("Priority", new Integer(Priority));
}
/** Get Priority.
@return Indicates if this request is of a high, medium or low priority. */
public int getPriority() 
{
Integer ii = (Integer)get_Value("Priority");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Rate.
@param Rate Rate or Tax or Exchange */
public void setRate (BigDecimal Rate)
{
set_Value ("Rate", Rate);
}
/** Get Rate.
@return Rate or Tax or Exchange */
public BigDecimal getRate() 
{
BigDecimal bd = (BigDecimal)get_Value("Rate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Transit.
@param Transit Transit */
public void setTransit (String Transit)
{
if (Transit != null && Transit.length() > 60)
{
log.warning("Length > 60 - truncated");
Transit = Transit.substring(0,59);
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
