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
/** Generated Model for P_Employee_Expensereports_Detail
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Expensereports_Detail extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Expensereports_Detail_ID id
@param trxName transaction
*/
public X_P_Employee_Expensereports_Detail (Properties ctx, int P_Employee_Expensereports_Detail_ID, String trxName)
{
super (ctx, P_Employee_Expensereports_Detail_ID, trxName);
/** if (P_Employee_Expensereports_Detail_ID == 0)
{
setP_Employee_Expensereports_Detail_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Expensereports_Detail (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201227 */
public static final int Table_ID=3201227;

/** TableName=P_Employee_Expensereports_Detail */
public static final String Table_Name="P_Employee_Expensereports_Detail";

protected static KeyNamePair Model = new KeyNamePair(3201227,"P_Employee_Expensereports_Detail");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Expensereports_Detail[").append(get_ID()).append("]");
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
/** Set bcy_total.
@param Bcy_Total bcy_total */
public void setBcy_Total (BigDecimal Bcy_Total)
{
set_Value ("Bcy_Total", Bcy_Total);
}
/** Get bcy_total.
@return bcy_total */
public BigDecimal getBcy_Total() 
{
BigDecimal bd = (BigDecimal)get_Value("Bcy_Total");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set category_id.
@param Category_code category_id */
public void setCategory_code (String Category_code)
{
if (Category_code != null && Category_code.length() > 200)
{
log.warning("Length > 200 - truncated");
Category_code = Category_code.substring(0,199);
}
set_Value ("Category_code", Category_code);
}
/** Get category_id.
@return category_id */
public String getCategory_code() 
{
return (String)get_Value("Category_code");
}
/** Set category_name.
@param Category_Name category_name */
public void setCategory_Name (String Category_Name)
{
if (Category_Name != null && Category_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Category_Name = Category_Name.substring(0,199);
}
set_Value ("Category_Name", Category_Name);
}
/** Get category_name.
@return category_name */
public String getCategory_Name() 
{
return (String)get_Value("Category_Name");
}
/** Set Claimed_Bcy_Total.
@param Claimed_Bcy_Total Claimed_Bcy_Total */
public void setClaimed_Bcy_Total (BigDecimal Claimed_Bcy_Total)
{
set_Value ("Claimed_Bcy_Total", Claimed_Bcy_Total);
}
/** Get Claimed_Bcy_Total.
@return Claimed_Bcy_Total */
public BigDecimal getClaimed_Bcy_Total() 
{
BigDecimal bd = (BigDecimal)get_Value("Claimed_Bcy_Total");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set claimed_fcy_total.
@param Claimed_Fcy_Total claimed_fcy_total */
public void setClaimed_Fcy_Total (BigDecimal Claimed_Fcy_Total)
{
set_Value ("Claimed_Fcy_Total", Claimed_Fcy_Total);
}
/** Get claimed_fcy_total.
@return claimed_fcy_total */
public BigDecimal getClaimed_Fcy_Total() 
{
BigDecimal bd = (BigDecimal)get_Value("Claimed_Fcy_Total");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 200)
{
log.warning("Length > 200 - truncated");
Description = Description.substring(0,199);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set gl_code.
@param Gl_Code gl_code */
public void setGl_Code (String Gl_Code)
{
if (Gl_Code != null && Gl_Code.length() > 200)
{
log.warning("Length > 200 - truncated");
Gl_Code = Gl_Code.substring(0,199);
}
set_Value ("Gl_Code", Gl_Code);
}
/** Get gl_code.
@return gl_code */
public String getGl_Code() 
{
return (String)get_Value("Gl_Code");
}
/** Set item_date.
@param Item_Date item_date */
public void setItem_Date (Timestamp Item_Date)
{
set_Value ("Item_Date", Item_Date);
}
/** Get item_date.
@return item_date */
public Timestamp getItem_Date() 
{
return (Timestamp)get_Value("Item_Date");
}
/** Set item_order.
@param Item_Order item_order */
public void setItem_Order (int Item_Order)
{
set_Value ("Item_Order", new Integer(Item_Order));
}
/** Get item_order.
@return item_order */
public int getItem_Order() 
{
Integer ii = (Integer)get_Value("Item_Order");
if (ii == null) return 0;
return ii.intValue();
}
/** Set item_total.
@param Item_Total item_total */
public void setItem_Total (BigDecimal Item_Total)
{
set_Value ("Item_Total", Item_Total);
}
/** Get item_total.
@return item_total */
public BigDecimal getItem_Total() 
{
BigDecimal bd = (BigDecimal)get_Value("Item_Total");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set item_total_inclusive_of_tax.
@param Item_Total_Inclusive_Of_Tax item_total_inclusive_of_tax */
public void setItem_Total_Inclusive_Of_Tax (BigDecimal Item_Total_Inclusive_Of_Tax)
{
set_Value ("Item_Total_Inclusive_Of_Tax", Item_Total_Inclusive_Of_Tax);
}
/** Get item_total_inclusive_of_tax.
@return item_total_inclusive_of_tax */
public BigDecimal getItem_Total_Inclusive_Of_Tax() 
{
BigDecimal bd = (BigDecimal)get_Value("Item_Total_Inclusive_Of_Tax");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set line_item_id.
@param Line_Item_Code line_item_id */
public void setLine_Item_Code (String Line_Item_Code)
{
if (Line_Item_Code != null && Line_Item_Code.length() > 200)
{
log.warning("Length > 200 - truncated");
Line_Item_Code = Line_Item_Code.substring(0,199);
}
set_Value ("Line_Item_Code", Line_Item_Code);
}
/** Get line_item_id.
@return line_item_id */
public String getLine_Item_Code() 
{
return (String)get_Value("Line_Item_Code");
}
/** Set P_Employee_Expensereports_Detail_ID.
@param P_Employee_Expensereports_Detail_ID P_Employee_Expensereports_Detail_ID */
public void setP_Employee_Expensereports_Detail_ID (int P_Employee_Expensereports_Detail_ID)
{
if (P_Employee_Expensereports_Detail_ID < 1) throw new IllegalArgumentException ("P_Employee_Expensereports_Detail_ID is mandatory.");
set_ValueNoCheck ("P_Employee_Expensereports_Detail_ID", new Integer(P_Employee_Expensereports_Detail_ID));
}
/** Get P_Employee_Expensereports_Detail_ID.
@return P_Employee_Expensereports_Detail_ID */
public int getP_Employee_Expensereports_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Expensereports_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Employee_Expensereports_ID.
@param P_Employee_Expensereports_ID P_Employee_Expensereports_ID */
public void setP_Employee_Expensereports_ID (int P_Employee_Expensereports_ID)
{
if (P_Employee_Expensereports_ID <= 0) set_Value ("P_Employee_Expensereports_ID", null);
 else 
set_Value ("P_Employee_Expensereports_ID", new Integer(P_Employee_Expensereports_ID));
}
/** Get P_Employee_Expensereports_ID.
@return P_Employee_Expensereports_ID */
public int getP_Employee_Expensereports_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Expensereports_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set report id.
@param report_id report id */
public void setreport_id (String report_id)
{
if (report_id != null && report_id.length() > 200)
{
log.warning("Length > 200 - truncated");
report_id = report_id.substring(0,199);
}
set_Value ("report_id", report_id);
}
/** Get report id.
@return report id */
public String getreport_id() 
{
return (String)get_Value("report_id");
}
/** Set tax_amount.
@param Tax_Amount tax_amount */
public void setTax_Amount (BigDecimal Tax_Amount)
{
set_Value ("Tax_Amount", Tax_Amount);
}
/** Get tax_amount.
@return tax_amount */
public BigDecimal getTax_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("Tax_Amount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set tax_id.
@param Tax_Code tax_id */
public void setTax_Code (String Tax_Code)
{
if (Tax_Code != null && Tax_Code.length() > 200)
{
log.warning("Length > 200 - truncated");
Tax_Code = Tax_Code.substring(0,199);
}
set_Value ("Tax_Code", Tax_Code);
}
/** Get tax_id.
@return tax_id */
public String getTax_Code() 
{
return (String)get_Value("Tax_Code");
}
/** Set tax_name.
@param Tax_Name tax_name */
public void setTax_Name (String Tax_Name)
{
if (Tax_Name != null && Tax_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Tax_Name = Tax_Name.substring(0,199);
}
set_Value ("Tax_Name", Tax_Name);
}
/** Get tax_name.
@return tax_name */
public String getTax_Name() 
{
return (String)get_Value("Tax_Name");
}
/** Set tax_percentage.
@param Tax_Percentage tax_percentage */
public void setTax_Percentage (BigDecimal Tax_Percentage)
{
set_Value ("Tax_Percentage", Tax_Percentage);
}
/** Get tax_percentage.
@return tax_percentage */
public BigDecimal getTax_Percentage() 
{
BigDecimal bd = (BigDecimal)get_Value("Tax_Percentage");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set tax_type.
@param Tax_Type tax_type */
public void setTax_Type (String Tax_Type)
{
if (Tax_Type != null && Tax_Type.length() > 200)
{
log.warning("Length > 200 - truncated");
Tax_Type = Tax_Type.substring(0,199);
}
set_Value ("Tax_Type", Tax_Type);
}
/** Get tax_type.
@return tax_type */
public String getTax_Type() 
{
return (String)get_Value("Tax_Type");
}
/** Set user_id.
@param User_Code user_id */
public void setUser_Code (String User_Code)
{
if (User_Code != null && User_Code.length() > 200)
{
log.warning("Length > 200 - truncated");
User_Code = User_Code.substring(0,199);
}
set_Value ("User_Code", User_Code);
}
/** Get user_id.
@return user_id */
public String getUser_Code() 
{
return (String)get_Value("User_Code");
}
/** Set user_name.
@param User_Name user_name */
public void setUser_Name (String User_Name)
{
if (User_Name != null && User_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
User_Name = User_Name.substring(0,199);
}
set_Value ("User_Name", User_Name);
}
/** Get user_name.
@return user_name */
public String getUser_Name() 
{
return (String)get_Value("User_Name");
}

/** C_ElementValue_ID AD_Reference_ID=2200494 */
public static final int C_ELEMENTVALUE_ID_AD_Reference_ID=2200494;
/** Set Account Element.
@param C_ElementValue_ID Account Element */
public void setC_ElementValue_ID (int C_ElementValue_ID)
{
if (C_ElementValue_ID <= 0) set_Value ("C_ElementValue_ID", null);
 else 
set_Value ("C_ElementValue_ID", new Integer(C_ElementValue_ID));
}
/** Get Account Element.
@return Account Element */
public int getC_ElementValue_ID() 
{
Integer ii = (Integer)get_Value("C_ElementValue_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set TPS Amount.
@param tps_amt TPS Amount */
public void settps_amt (BigDecimal tps_amt)
{
set_Value ("tps_amt", tps_amt);
}
/** Get TPS Amount.
@return TPS Amount */
public BigDecimal gettps_amt() 
{
BigDecimal bd = (BigDecimal)get_Value("tps_amt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set TVH Amount.
@param tvh_amt TVH Amount */
public void settvh_amt (BigDecimal tvh_amt)
{
set_Value ("tvh_amt", tvh_amt);
}
/** Get TVH Amount.
@return TVH Amount */
public BigDecimal gettvh_amt() 
{
BigDecimal bd = (BigDecimal)get_Value("tvh_amt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set TVQ Amount.
@param tvq_amt TVQ Amount */
public void settvq_amt (BigDecimal tvq_amt)
{
set_Value ("tvq_amt", tvq_amt);
}
/** Get TVQ Amount.
@return TVQ Amount */
public BigDecimal gettvq_amt() 
{
BigDecimal bd = (BigDecimal)get_Value("tvq_amt");
if (bd == null) return Env.ZERO;
return bd;
}


/**
 * 	Set Is_reimbursable
 * 	@param Is_reimbursable
 */
public final void setIs_reimbursable (boolean reimbursable)
{
	set_Value("Is_reimbursable", new Boolean(reimbursable));
}	//	setActive

/**
 *	Is_reimbursable
 *  @return Is_reimbursable
 */
public final boolean isReimbursable()
{
	Boolean bb = (Boolean)get_Value("Is_reimbursable");
	if (bb != null)
		return bb.booleanValue();
	return false;
}	//	isActive



}
