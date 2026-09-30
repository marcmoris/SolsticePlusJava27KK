/******************************************************************************
* Product: Solstice+ Payroll & Human Resources Management                    *
* Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
* This program is free software, you can redistribute it and/or modify it    *
* under the terms version 2 of the GNU General Public License as published   *
* by the Free Software Foundation. This program is distributed in the hope   *
* that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
* warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
* See the GNU General Public License for more details.                       *
* You should have received a copy of the GNU General Public License along    *
* with this program, if not, write to the Free Software Foundation, Inc.,    *
* 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
* For the text or an alternative of this public license, you may reach us    *
* ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
* or via info@progestion.net or http://www.progestion.net/license.html       *
******************************************************************************/
package solstice.model;

/** Generated Model - DO NOT CHANGE */
import org.compiere.model.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for P_Tools_Credits_Result
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Tools_Credits_Result extends PO
{
/** Standard Constructor
@param ctx context
@param P_Tools_Credits_Result_ID id
@param trxName transaction
*/
public X_P_Tools_Credits_Result (Properties ctx, int P_Tools_Credits_Result_ID, String trxName)
{
super (ctx, P_Tools_Credits_Result_ID, trxName);
/** if (P_Tools_Credits_Result_ID == 0)
{
setCredits_Dst_ID (0);
setCredits_Src_ID (0);
setP_Employee_ID (0);
setP_Tools_Credits_ID (0);
setP_Tools_Credits_Result_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Tools_Credits_Result (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100826 */
public static final int Table_ID=2100826;

/** TableName=P_Tools_Credits_Result */
public static final String Table_Name="P_Tools_Credits_Result";

protected static KeyNamePair Model = new KeyNamePair(2100826,"P_Tools_Credits_Result");

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
StringBuffer sb = new StringBuffer ("X_P_Tools_Credits_Result[").append(get_ID()).append("]");
return sb.toString();
}

/** Credits_Dst_ID AD_Reference_ID=2100318 */
public static final int CREDITS_DST_ID_AD_Reference_ID=2100318;
/** Set Banque destination.
@param Credits_Dst_ID Banque destination */
public void setCredits_Dst_ID (int Credits_Dst_ID)
{
if (Credits_Dst_ID < 1) throw new IllegalArgumentException ("Credits_Dst_ID is mandatory.");
set_Value ("Credits_Dst_ID", new Integer(Credits_Dst_ID));
}
/** Get Banque destination.
@return Banque destination */
public int getCredits_Dst_ID() 
{
Integer ii = (Integer)get_Value("Credits_Dst_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** Credits_Src_ID AD_Reference_ID=2100318 */
public static final int CREDITS_SRC_ID_AD_Reference_ID=2100318;
/** Set Banque source.
@param Credits_Src_ID Banque source */
public void setCredits_Src_ID (int Credits_Src_ID)
{
if (Credits_Src_ID < 1) throw new IllegalArgumentException ("Credits_Src_ID is mandatory.");
set_Value ("Credits_Src_ID", new Integer(Credits_Src_ID));
}
/** Get Banque source.
@return Banque source */
public int getCredits_Src_ID() 
{
Integer ii = (Integer)get_Value("Credits_Src_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set isTimeSheetTransferred.
@param isTimeSheetTransferred isTimeSheetTransferred */
public void setIsTimeSheetTransferred (boolean isTimeSheetTransferred)
{
set_Value ("isTimeSheetTransferred", new Boolean(isTimeSheetTransferred));
}
/** Get isTimeSheetTransferred.
@return isTimeSheetTransferred */
public boolean isTimeSheetTransferred() 
{
Object oo = get_Value("isTimeSheetTransferred");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}

/** P_Distribution_Booklet_ID AD_Reference_ID=2100388 */
public static final int P_DISTRIBUTION_BOOKLET_ID_AD_Reference_ID=2100388;
/** Set Work team.
@param P_Distribution_Booklet_ID Work team */
public void setP_Distribution_Booklet_ID (int P_Distribution_Booklet_ID)
{
if (P_Distribution_Booklet_ID <= 0) set_Value ("P_Distribution_Booklet_ID", null);
 else 
set_Value ("P_Distribution_Booklet_ID", new Integer(P_Distribution_Booklet_ID));
}
/** Get Work team.
@return Work team */
public int getP_Distribution_Booklet_ID() 
{
Integer ii = (Integer)get_Value("P_Distribution_Booklet_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set P_Tools_Credits_ID.
@param P_Tools_Credits_ID P_Tools_Credits_ID */
public void setP_Tools_Credits_ID (int P_Tools_Credits_ID)
{
if (P_Tools_Credits_ID < 1) throw new IllegalArgumentException ("P_Tools_Credits_ID is mandatory.");
set_Value ("P_Tools_Credits_ID", new Integer(P_Tools_Credits_ID));
}
/** Get P_Tools_Credits_ID.
@return P_Tools_Credits_ID */
public int getP_Tools_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Tools_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Tools_Credits_Result_ID.
@param P_Tools_Credits_Result_ID P_Tools_Credits_Result_ID */
public void setP_Tools_Credits_Result_ID (int P_Tools_Credits_Result_ID)
{
if (P_Tools_Credits_Result_ID < 1) throw new IllegalArgumentException ("P_Tools_Credits_Result_ID is mandatory.");
set_ValueNoCheck ("P_Tools_Credits_Result_ID", new Integer(P_Tools_Credits_Result_ID));
}
/** Get P_Tools_Credits_Result_ID.
@return P_Tools_Credits_Result_ID */
public int getP_Tools_Credits_Result_ID() 
{
Integer ii = (Integer)get_Value("P_Tools_Credits_Result_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Quantity Pay.
@param QuantityPay Quantity Pay */
public void setQuantityPay (BigDecimal QuantityPay)
{
throw new IllegalArgumentException ("QuantityPay is virtual column");
}
/** Get Quantity Pay.
@return Quantity Pay */
public BigDecimal getQuantityPay() 
{
BigDecimal bd = (BigDecimal)get_Value("QuantityPay");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Quantity Transfer.
@param QuantityTransfer Quantity Transfer */
public void setQuantityTransfer (BigDecimal QuantityTransfer)
{
set_Value ("QuantityTransfer", QuantityTransfer);
}
/** Get Quantity Transfer.
@return Quantity Transfer */
public BigDecimal getQuantityTransfer() 
{
BigDecimal bd = (BigDecimal)get_Value("QuantityTransfer");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Total .
@param TotalCreditsDst Total  */
public void setTotalCreditsDst (BigDecimal TotalCreditsDst)
{
set_Value ("TotalCreditsDst", TotalCreditsDst);
}
/** Get Total .
@return Total  */
public BigDecimal getTotalCreditsDst() 
{
BigDecimal bd = (BigDecimal)get_Value("TotalCreditsDst");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Total.
@param TotalCreditsSrc Total */
public void setTotalCreditsSrc (BigDecimal TotalCreditsSrc)
{
set_Value ("TotalCreditsSrc", TotalCreditsSrc);
}
/** Get Total.
@return Total */
public BigDecimal getTotalCreditsSrc() 
{
BigDecimal bd = (BigDecimal)get_Value("TotalCreditsSrc");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Date du transfert.
@param TransfertDate Date du transfert */
public void setTransfertDate (Timestamp TransfertDate)
{
set_Value ("TransfertDate", TransfertDate);
}
/** Get Date du transfert.
@return Date du transfert */
public Timestamp getTransfertDate() 
{
return (Timestamp)get_Value("TransfertDate");
}
}
