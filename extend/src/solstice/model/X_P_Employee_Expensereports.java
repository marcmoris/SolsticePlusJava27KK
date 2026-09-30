/* Product: Solstice+ Payroll & Human Resources Management                    *
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
/** Generated Model for P_Employee_Expensereports
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Expensereports extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Expensereports_ID id
@param trxName transaction
*/
public X_P_Employee_Expensereports (Properties ctx, int P_Employee_Expensereports_ID, String trxName)
{
super (ctx, P_Employee_Expensereports_ID, trxName);
/** if (P_Employee_Expensereports_ID == 0)
{
setP_Employee_Expensereports_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Expensereports (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201226 */
public static final int Table_ID=3201226;

/** TableName=P_Employee_Expensereports */
public static final String Table_Name="P_Employee_Expensereports";

protected static KeyNamePair Model = new KeyNamePair(3201226,"P_Employee_Expensereports");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Expensereports[").append(get_ID()).append("]");
return sb.toString();
}
/** Set accounting_app_name.
@param Accounting_App_Name accounting_app_name */
public void setAccounting_App_Name (String Accounting_App_Name)
{
if (Accounting_App_Name != null && Accounting_App_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Accounting_App_Name = Accounting_App_Name.substring(0,199);
}
set_Value ("Accounting_App_Name", Accounting_App_Name);
}
/** Get accounting_app_name.
@return accounting_app_name */
public String getAccounting_App_Name() 
{
return (String)get_Value("Accounting_App_Name");
}
/** Set accounting_reference_id.
@param Accounting_Reference_ID accounting_reference_id */
public void setAccounting_Reference_ID (String Accounting_Reference_ID)
{
if (Accounting_Reference_ID != null && Accounting_Reference_ID.length() > 200)
{
log.warning("Length > 200 - truncated");
Accounting_Reference_ID = Accounting_Reference_ID.substring(0,199);
}
set_Value ("Accounting_Reference_ID", Accounting_Reference_ID);
}
/** Get accounting_reference_id.
@return accounting_reference_id */
public String getAccounting_Reference_ID() 
{
return (String)get_Value("Accounting_Reference_ID");
}
/** Set accounting_sync_time.
@param Accounting_Sync_Time accounting_sync_time */
public void setAccounting_Sync_Time (String Accounting_Sync_Time)
{
if (Accounting_Sync_Time != null && Accounting_Sync_Time.length() > 200)
{
log.warning("Length > 200 - truncated");
Accounting_Sync_Time = Accounting_Sync_Time.substring(0,199);
}
set_Value ("Accounting_Sync_Time", Accounting_Sync_Time);
}
/** Get accounting_sync_time.
@return accounting_sync_time */
public String getAccounting_Sync_Time() 
{
return (String)get_Value("Accounting_Sync_Time");
}
/** Set amount_policy_violation_count.
@param Amount_Policy_Violation_Count amount_policy_violation_count */
public void setAmount_Policy_Violation_Count (int Amount_Policy_Violation_Count)
{
set_Value ("Amount_Policy_Violation_Count", new Integer(Amount_Policy_Violation_Count));
}
/** Get amount_policy_violation_count.
@return amount_policy_violation_count */
public int getAmount_Policy_Violation_Count() 
{
Integer ii = (Integer)get_Value("Amount_Policy_Violation_Count");
if (ii == null) return 0;
return ii.intValue();
}
/** Set amount_to_be_reimbursed.
@param Amount_To_Be_Reimbursed amount_to_be_reimbursed */
public void setAmount_To_Be_Reimbursed (BigDecimal Amount_To_Be_Reimbursed)
{
set_Value ("Amount_To_Be_Reimbursed", Amount_To_Be_Reimbursed);
}
/** Get amount_to_be_reimbursed.
@return amount_to_be_reimbursed */
public BigDecimal getAmount_To_Be_Reimbursed() 
{
BigDecimal bd = (BigDecimal)get_Value("Amount_To_Be_Reimbursed");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set approved_date.
@param Approved_Date approved_date */
public void setApproved_Date (Timestamp Approved_Date)
{
set_Value ("Approved_Date", Approved_Date);
}
/** Get approved_date.
@return approved_date */
public Timestamp getApproved_Date() 
{
return (Timestamp)get_Value("Approved_Date");
}
/** Set approver_email.
@param Approver_Email approver_email */
public void setApprover_Email (String Approver_Email)
{
if (Approver_Email != null && Approver_Email.length() > 200)
{
log.warning("Length > 200 - truncated");
Approver_Email = Approver_Email.substring(0,199);
}
set_Value ("Approver_Email", Approver_Email);
}
/** Get approver_email.
@return approver_email */
public String getApprover_Email() 
{
return (String)get_Value("Approver_Email");
}
/** Set approver_employee_no.
@param Approver_Employee_No approver_employee_no */
public void setApprover_Employee_No (String Approver_Employee_No)
{
if (Approver_Employee_No != null && Approver_Employee_No.length() > 200)
{
log.warning("Length > 200 - truncated");
Approver_Employee_No = Approver_Employee_No.substring(0,199);
}
set_Value ("Approver_Employee_No", Approver_Employee_No);
}
/** Get approver_employee_no.
@return approver_employee_no */
public String getApprover_Employee_No() 
{
return (String)get_Value("Approver_Employee_No");
}
/** Set approver_id.
@param Approver_ID approver_id */
public void setApprover_ID (String Approver_ID)
{
if (Approver_ID != null && Approver_ID.length() > 200)
{
log.warning("Length > 200 - truncated");
Approver_ID = Approver_ID.substring(0,199);
}
set_Value ("Approver_ID", Approver_ID);
}
/** Get approver_id.
@return approver_id */
public String getApprover_ID() 
{
return (String)get_Value("Approver_ID");
}
/** Set approver_name.
@param Approver_Name approver_name */
public void setApprover_Name (String Approver_Name)
{
if (Approver_Name != null && Approver_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Approver_Name = Approver_Name.substring(0,199);
}
set_Value ("Approver_Name", Approver_Name);
}
/** Get approver_name.
@return approver_name */
public String getApprover_Name() 
{
return (String)get_Value("Approver_Name");
}
/** Set approver_photo_url.
@param Approver_Photo_Url approver_photo_url */
public void setApprover_Photo_Url (String Approver_Photo_Url)
{
if (Approver_Photo_Url != null && Approver_Photo_Url.length() > 200)
{
log.warning("Length > 200 - truncated");
Approver_Photo_Url = Approver_Photo_Url.substring(0,199);
}
set_Value ("Approver_Photo_Url", Approver_Photo_Url);
}
/** Get approver_photo_url.
@return approver_photo_url */
public String getApprover_Photo_Url() 
{
return (String)get_Value("Approver_Photo_Url");
}
/** Set approver_zuid.
@param Approver_Zuid approver_zuid */
public void setApprover_Zuid (String Approver_Zuid)
{
if (Approver_Zuid != null && Approver_Zuid.length() > 200)
{
log.warning("Length > 200 - truncated");
Approver_Zuid = Approver_Zuid.substring(0,199);
}
set_Value ("Approver_Zuid", Approver_Zuid);
}
/** Get approver_zuid.
@return approver_zuid */
public String getApprover_Zuid() 
{
return (String)get_Value("Approver_Zuid");
}
/** Set audit_status.
@param Audit_Status audit_status */
public void setAudit_Status (String Audit_Status)
{
if (Audit_Status != null && Audit_Status.length() > 200)
{
log.warning("Length > 200 - truncated");
Audit_Status = Audit_Status.substring(0,199);
}
set_Value ("Audit_Status", Audit_Status);
}
/** Get audit_status.
@return audit_status */
public String getAudit_Status() 
{
return (String)get_Value("Audit_Status");
}
/** Set can_push_to_accounting_app.
@param Can_Push_To_Accounting_App can_push_to_accounting_app */
public void setCan_Push_To_Accounting_App (String Can_Push_To_Accounting_App)
{
if (Can_Push_To_Accounting_App != null && Can_Push_To_Accounting_App.length() > 200)
{
log.warning("Length > 200 - truncated");
Can_Push_To_Accounting_App = Can_Push_To_Accounting_App.substring(0,199);
}
set_Value ("Can_Push_To_Accounting_App", Can_Push_To_Accounting_App);
}
/** Get can_push_to_accounting_app.
@return can_push_to_accounting_app */
public String getCan_Push_To_Accounting_App() 
{
return (String)get_Value("Can_Push_To_Accounting_App");
}
/** Set claim_type_id.
@param Claim_Type_ID claim_type_id */
public void setClaim_Type_ID (String Claim_Type_ID)
{
if (Claim_Type_ID != null && Claim_Type_ID.length() > 200)
{
log.warning("Length > 200 - truncated");
Claim_Type_ID = Claim_Type_ID.substring(0,199);
}
set_Value ("Claim_Type_ID", Claim_Type_ID);
}
/** Get claim_type_id.
@return claim_type_id */
public String getClaim_Type_ID() 
{
return (String)get_Value("Claim_Type_ID");
}
/** Set claim_type_name.
@param Claim_Type_Name claim_type_name */
public void setClaim_Type_Name (String Claim_Type_Name)
{
if (Claim_Type_Name != null && Claim_Type_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Claim_Type_Name = Claim_Type_Name.substring(0,199);
}
set_Value ("Claim_Type_Name", Claim_Type_Name);
}
/** Get claim_type_name.
@return claim_type_name */
public String getClaim_Type_Name() 
{
return (String)get_Value("Claim_Type_Name");
}
/** Set comments_count.
@param Comments_Count comments_count */
public void setComments_Count (int Comments_Count)
{
set_Value ("Comments_Count", new Integer(Comments_Count));
}
/** Get comments_count.
@return comments_count */
public int getComments_Count() 
{
Integer ii = (Integer)get_Value("Comments_Count");
if (ii == null) return 0;
return ii.intValue();
}
/** Set creator_employee_no.
@param Creator_Employee_No creator_employee_no */
public void setCreator_Employee_No (String Creator_Employee_No)
{
if (Creator_Employee_No != null && Creator_Employee_No.length() > 200)
{
log.warning("Length > 200 - truncated");
Creator_Employee_No = Creator_Employee_No.substring(0,199);
}
set_Value ("Creator_Employee_No", Creator_Employee_No);
}
/** Get creator_employee_no.
@return creator_employee_no */
public String getCreator_Employee_No() 
{
return (String)get_Value("Creator_Employee_No");
}
/** Set creator_photo_url.
@param Creator_Photo_Url creator_photo_url */
public void setCreator_Photo_Url (String Creator_Photo_Url)
{
if (Creator_Photo_Url != null && Creator_Photo_Url.length() > 200)
{
log.warning("Length > 200 - truncated");
Creator_Photo_Url = Creator_Photo_Url.substring(0,199);
}
set_Value ("Creator_Photo_Url", Creator_Photo_Url);
}
/** Get creator_photo_url.
@return creator_photo_url */
public String getCreator_Photo_Url() 
{
return (String)get_Value("Creator_Photo_Url");
}
/** Set currency_code.
@param Currency_Code currency_code */
public void setCurrency_Code (String Currency_Code)
{
if (Currency_Code != null && Currency_Code.length() > 3)
{
log.warning("Length > 3 - truncated");
Currency_Code = Currency_Code.substring(0,2);
}
set_Value ("Currency_Code", Currency_Code);
}
/** Get currency_code.
@return currency_code */
public String getCurrency_Code() 
{
return (String)get_Value("Currency_Code");
}
/** Set currency_id.
@param Currency_Id currency_id */
public void setCurrency_Id (String Currency_Id)
{
if (Currency_Id != null && Currency_Id.length() > 200)
{
log.warning("Length > 200 - truncated");
Currency_Id = Currency_Id.substring(0,199);
}
set_Value ("Currency_Id", Currency_Id);
}
/** Get currency_id.
@return currency_id */
public String getCurrency_Id() 
{
return (String)get_Value("Currency_Id");
}
/** Set custom_policy_violation_count.
@param Custom_Policy_Violation_Count custom_policy_violation_count */
public void setCustom_Policy_Violation_Count (int Custom_Policy_Violation_Count)
{
set_Value ("Custom_Policy_Violation_Count", new Integer(Custom_Policy_Violation_Count));
}
/** Get custom_policy_violation_count.
@return custom_policy_violation_count */
public int getCustom_Policy_Violation_Count() 
{
Integer ii = (Integer)get_Value("Custom_Policy_Violation_Count");
if (ii == null) return 0;
return ii.intValue();
}
/** Set customer_id.
@param Customer_Id customer_id */
public void setCustomer_Id (String Customer_Id)
{
if (Customer_Id != null && Customer_Id.length() > 200)
{
log.warning("Length > 200 - truncated");
Customer_Id = Customer_Id.substring(0,199);
}
set_Value ("Customer_Id", Customer_Id);
}
/** Get customer_id.
@return customer_id */
public String getCustomer_Id() 
{
return (String)get_Value("Customer_Id");
}
/** Set customer_name.
@param Customer_Name customer_name */
public void setCustomer_Name (String Customer_Name)
{
if (Customer_Name != null && Customer_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Customer_Name = Customer_Name.substring(0,199);
}
set_Value ("Customer_Name", Customer_Name);
}
/** Get customer_name.
@return customer_name */
public String getCustomer_Name() 
{
return (String)get_Value("Customer_Name");
}
/** Set department_id.
@param Department_Id department_id */
public void setDepartment_Id (String Department_Id)
{
if (Department_Id != null && Department_Id.length() > 200)
{
log.warning("Length > 200 - truncated");
Department_Id = Department_Id.substring(0,199);
}
set_Value ("Department_Id", Department_Id);
}
/** Get department_id.
@return department_id */
public String getDepartment_Id() 
{
return (String)get_Value("Department_Id");
}
/** Set department_name.
@param Department_Name department_name */
public void setDepartment_Name (String Department_Name)
{
if (Department_Name != null && Department_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Department_Name = Department_Name.substring(0,199);
}
set_Value ("Department_Name", Department_Name);
}
/** Get department_name.
@return department_name */
public String getDepartment_Name() 
{
return (String)get_Value("Department_Name");
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
/** Set description_policy_violation_count.
@param Description_Policy_Violation_Count description_policy_violation_count */
public void setDescription_Policy_Violation_Count (int Description_Policy_Violation_Count)
{
set_Value ("Description_Policy_Violation_Count", new Integer(Description_Policy_Violation_Count));
}
/** Get description_policy_violation_count.
@return description_policy_violation_count */
public int getDescription_Policy_Violation_Count() 
{
Integer ii = (Integer)get_Value("Description_Policy_Violation_Count");
if (ii == null) return 0;
return ii.intValue();
}
/** Set due_date.
@param Due_Date due_date */
public void setDue_Date (Timestamp Due_Date)
{
set_Value ("Due_Date", Due_Date);
}
/** Get due_date.
@return due_date */
public Timestamp getDue_Date() 
{
return (Timestamp)get_Value("Due_Date");
}
/** Set due_days.
@param Due_Days due_days */
public void setDue_Days (String Due_Days)
{
if (Due_Days != null && Due_Days.length() > 200)
{
log.warning("Length > 200 - truncated");
Due_Days = Due_Days.substring(0,199);
}
set_Value ("Due_Days", Due_Days);
}
/** Get due_days.
@return due_days */
public String getDue_Days() 
{
return (String)get_Value("Due_Days");
}
/** Set employee_number.
@param Employee_Number employee_number */
public void setEmployee_Number (String Employee_Number)
{
if (Employee_Number != null && Employee_Number.length() > 200)
{
log.warning("Length > 200 - truncated");
Employee_Number = Employee_Number.substring(0,199);
}
set_Value ("Employee_Number", Employee_Number);
}
/** Get employee_number.
@return employee_number */
public String getEmployee_Number() 
{
return (String)get_Value("Employee_Number");
}
/** Set End date.
@param End_Date End date */
public void setEnd_Date (Timestamp End_Date)
{
set_Value ("End_Date", End_Date);
}
/** Get End date.
@return End date */
public Timestamp getEnd_Date() 
{
return (Timestamp)get_Value("End_Date");
}
/** Set expense_count.
@param Expense_Count expense_count */
public void setExpense_Count (String Expense_Count)
{
if (Expense_Count != null && Expense_Count.length() > 200)
{
log.warning("Length > 200 - truncated");
Expense_Count = Expense_Count.substring(0,199);
}
set_Value ("Expense_Count", Expense_Count);
}
/** Get expense_count.
@return expense_count */
public String getExpense_Count() 
{
return (String)get_Value("Expense_Count");
}
/** Set is_ach_reimbursement.
@param Is_Ach_Reimbursement is_ach_reimbursement */
public void setIs_Ach_Reimbursement (boolean Is_Ach_Reimbursement)
{
set_Value ("Is_Ach_Reimbursement", new Boolean(Is_Ach_Reimbursement));
}
/** Get is_ach_reimbursement.
@return is_ach_reimbursement */
public boolean is_Ach_Reimbursement() 
{
Object oo = get_Value("Is_Ach_Reimbursement");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set is_archived.
@param Is_Archived is_archived */
public void setIs_Archived (boolean Is_Archived)
{
set_Value ("Is_Archived", new Boolean(Is_Archived));
}
/** Get is_archived.
@return is_archived */
public boolean is_Archived() 
{
Object oo = get_Value("Is_Archived");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set is_international_trip.
@param Is_International_Trip is_international_trip */
public void setIs_International_Trip (String Is_International_Trip)
{
if (Is_International_Trip != null && Is_International_Trip.length() > 200)
{
log.warning("Length > 200 - truncated");
Is_International_Trip = Is_International_Trip.substring(0,199);
}
set_Value ("Is_International_Trip", Is_International_Trip);
}
/** Get is_international_trip.
@return is_international_trip */
public String getIs_International_Trip() 
{
return (String)get_Value("Is_International_Trip");
}
/** Set is_shared_report.
@param Is_Shared_Report is_shared_report */
public void setIs_Shared_Report (boolean Is_Shared_Report)
{
set_Value ("Is_Shared_Report", new Boolean(Is_Shared_Report));
}
/** Get is_shared_report.
@return is_shared_report */
public boolean is_Shared_Report() 
{
Object oo = get_Value("Is_Shared_Report");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set last_modified_date.
@param Last_Modified_Date last_modified_date */
public void setLast_Modified_Date (Timestamp Last_Modified_Date)
{
set_Value ("Last_Modified_Date", Last_Modified_Date);
}
/** Get last_modified_date.
@return last_modified_date */
public Timestamp getLast_Modified_Date() 
{
return (Timestamp)get_Value("Last_Modified_Date");
}
/** Set last_modified_time.
@param Last_Modified_Time last_modified_time */
public void setLast_Modified_Time (Timestamp Last_Modified_Time)
{
set_Value ("Last_Modified_Time", Last_Modified_Time);
}
/** Get last_modified_time.
@return last_modified_time */
public Timestamp getLast_Modified_Time() 
{
return (Timestamp)get_Value("Last_Modified_Time");
}
/** Set last_submitted_date.
@param Last_Submitted_Date last_submitted_date */
public void setLast_Submitted_Date (Timestamp Last_Submitted_Date)
{
set_Value ("Last_Submitted_Date", Last_Submitted_Date);
}
/** Get last_submitted_date.
@return last_submitted_date */
public Timestamp getLast_Submitted_Date() 
{
return (Timestamp)get_Value("Last_Submitted_Date");
}
/** Set non_reimbursable_total.
@param string non_reimbursable_total */
public void setNon_Reimbursable_Total (BigDecimal amt)
{
set_Value ("Non_Reimbursable_Total", amt);
}
/** Get non_reimbursable_total.
@return non_reimbursable_total */
public BigDecimal getNon_Reimbursable_Total() 
{
BigDecimal bd = (BigDecimal)get_Value("Non_Reimbursable_Total");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set P_Employee_Expensereports_ID.
@param P_Employee_Expensereports_ID P_Employee_Expensereports_ID */
public void setP_Employee_Expensereports_ID (int P_Employee_Expensereports_ID)
{
if (P_Employee_Expensereports_ID < 1) throw new IllegalArgumentException ("P_Employee_Expensereports_ID is mandatory.");
set_ValueNoCheck ("P_Employee_Expensereports_ID", new Integer(P_Employee_Expensereports_ID));
}
/** Get P_Employee_Expensereports_ID.
@return P_Employee_Expensereports_ID */
public int getP_Employee_Expensereports_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Expensereports_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID <= 0) set_Value ("P_Employee_ID", null);
 else 
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
/** Set payrun_name.
@param Payrun_Name payrun_name */
public void setPayrun_Name (String Payrun_Name)
{
if (Payrun_Name != null && Payrun_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Payrun_Name = Payrun_Name.substring(0,199);
}
set_Value ("Payrun_Name", Payrun_Name);
}
/** Get payrun_name.
@return payrun_name */
public String getPayrun_Name() 
{
return (String)get_Value("Payrun_Name");
}
/** Set policy_display_name.
@param Policy_Display_Name policy_display_name */
public void setPolicy_Display_Name (String Policy_Display_Name)
{
if (Policy_Display_Name != null && Policy_Display_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Policy_Display_Name = Policy_Display_Name.substring(0,199);
}
set_Value ("Policy_Display_Name", Policy_Display_Name);
}
/** Get policy_display_name.
@return policy_display_name */
public String getPolicy_Display_Name() 
{
return (String)get_Value("Policy_Display_Name");
}
/** Set policy_id.
@param Policy_ID policy_id */
public void setPolicy_ID (String Policy_ID)
{
if (Policy_ID != null && Policy_ID.length() > 200)
{
log.warning("Length > 200 - truncated");
Policy_ID = Policy_ID.substring(0,199);
}
set_Value ("Policy_ID", Policy_ID);
}
/** Get policy_id.
@return policy_id */
public String getPolicy_ID() 
{
return (String)get_Value("Policy_ID");
}
/** Set policy_name.
@param Policy_Name policy_name */
public void setPolicy_Name (String Policy_Name)
{
if (Policy_Name != null && Policy_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Policy_Name = Policy_Name.substring(0,199);
}
set_Value ("Policy_Name", Policy_Name);
}
/** Get policy_name.
@return policy_name */
public String getPolicy_Name() 
{
return (String)get_Value("Policy_Name");
}
/** Set policy_violated.
@param Policy_Violated policy_violated */
public void setPolicy_Violated (String Policy_Violated)
{
if (Policy_Violated != null && Policy_Violated.length() > 1)
{
log.warning("Length > 1 - truncated");
Policy_Violated = Policy_Violated.substring(0,0);
}
set_Value ("Policy_Violated", Policy_Violated);
}
/** Get policy_violated.
@return policy_violated */
public String getPolicy_Violated() 
{
return (String)get_Value("Policy_Violated");
}
/** Set project_id.
@param Project_ID project_id */
public void setProject_ID (String Project_ID)
{
if (Project_ID != null && Project_ID.length() > 200)
{
log.warning("Length > 200 - truncated");
Project_ID = Project_ID.substring(0,199);
}
set_Value ("Project_ID", Project_ID);
}
/** Get project_id.
@return project_id */
public String getProject_ID() 
{
return (String)get_Value("Project_ID");
}
/** Set project_name.
@param Project_Name project_name */
public void setProject_Name (String Project_Name)
{
if (Project_Name != null && Project_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Project_Name = Project_Name.substring(0,199);
}
set_Value ("Project_Name", Project_Name);
}
/** Get project_name.
@return project_name */
public String getProject_Name() 
{
return (String)get_Value("Project_Name");
}
/** Set rcy_currency_code.
@param Rcy_Currency_Code rcy_currency_code */
public void setRcy_Currency_Code (String Rcy_Currency_Code)
{
if (Rcy_Currency_Code != null && Rcy_Currency_Code.length() > 3)
{
log.warning("Length > 3 - truncated");
Rcy_Currency_Code = Rcy_Currency_Code.substring(0,2);
}
set_Value ("Rcy_Currency_Code", Rcy_Currency_Code);
}
/** Get rcy_currency_code.
@return rcy_currency_code */
public String getRcy_Currency_Code() 
{
return (String)get_Value("Rcy_Currency_Code");
}
/** Set rcy_currency_id.
@param Rcy_Currency_ID rcy_currency_id */
public void setRcy_Currency_ID (String Rcy_Currency_ID)
{
if (Rcy_Currency_ID != null && Rcy_Currency_ID.length() > 200)
{
log.warning("Length > 200 - truncated");
Rcy_Currency_ID = Rcy_Currency_ID.substring(0,199);
}
set_Value ("Rcy_Currency_ID", Rcy_Currency_ID);
}
/** Get rcy_currency_id.
@return rcy_currency_id */
public String getRcy_Currency_ID() 
{
return (String)get_Value("Rcy_Currency_ID");
}
/** Set rcy_currency_symbol.
@param Rcy_Currency_Symbol rcy_currency_symbol */
public void setRcy_Currency_Symbol (String Rcy_Currency_Symbol)
{
if (Rcy_Currency_Symbol != null && Rcy_Currency_Symbol.length() > 3)
{
log.warning("Length > 3 - truncated");
Rcy_Currency_Symbol = Rcy_Currency_Symbol.substring(0,2);
}
set_Value ("Rcy_Currency_Symbol", Rcy_Currency_Symbol);
}
/** Get rcy_currency_symbol.
@return rcy_currency_symbol */
public String getRcy_Currency_Symbol() 
{
return (String)get_Value("Rcy_Currency_Symbol");
}
/** Set rcy_exchange_rate.
@param Rcy_Exchange_Rate rcy_exchange_rate */
public void setRcy_Exchange_Rate (BigDecimal Rcy_Exchange_Rate)
{
set_Value ("Rcy_Exchange_Rate", Rcy_Exchange_Rate);
}
/** Get rcy_exchange_rate.
@return rcy_exchange_rate */
public BigDecimal getRcy_Exchange_Rate() 
{
BigDecimal bd = (BigDecimal)get_Value("Rcy_Exchange_Rate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set rcy_non_reimbursable_total.
@param Rcy_Non_Reimbursable_Total rcy_non_reimbursable_total */
public void setRcy_Non_Reimbursable_Total (BigDecimal Rcy_Non_Reimbursable_Total)
{
set_Value ("Rcy_Non_Reimbursable_Total", Rcy_Non_Reimbursable_Total);
}
/** Get rcy_non_reimbursable_total.
@return rcy_non_reimbursable_total */
public BigDecimal getRcy_Non_Reimbursable_Total() 
{
BigDecimal bd = (BigDecimal)get_Value("Rcy_Non_Reimbursable_Total");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set rcy_price_precision.
@param Rcy_Price_Precision rcy_price_precision */
public void setRcy_Price_Precision (String Rcy_Price_Precision)
{
if (Rcy_Price_Precision != null && Rcy_Price_Precision.length() > 200)
{
log.warning("Length > 200 - truncated");
Rcy_Price_Precision = Rcy_Price_Precision.substring(0,199);
}
set_Value ("Rcy_Price_Precision", Rcy_Price_Precision);
}
/** Get rcy_price_precision.
@return rcy_price_precision */
public String getRcy_Price_Precision() 
{
return (String)get_Value("Rcy_Price_Precision");
}
/** Set rcy_reimbursable_total.
@param Rcy_Reimbursable_Total rcy_reimbursable_total */
public void setRcy_Reimbursable_Total (BigDecimal Rcy_Reimbursable_Total)
{
set_Value ("Rcy_Reimbursable_Total", Rcy_Reimbursable_Total);
}
/** Get rcy_reimbursable_total.
@return rcy_reimbursable_total */
public BigDecimal getRcy_Reimbursable_Total() 
{
BigDecimal bd = (BigDecimal)get_Value("Rcy_Reimbursable_Total");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set rcy_total.
@param Rcy_Total rcy_total */
public void setRcy_Total (BigDecimal Rcy_Total)
{
set_Value ("Rcy_Total", Rcy_Total);
}
/** Get rcy_total.
@return rcy_total */
public BigDecimal getRcy_Total() 
{
BigDecimal bd = (BigDecimal)get_Value("Rcy_Total");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set receipt_policy_violation_count.
@param Receipt_Policy_Violation_Count receipt_policy_violation_count */
public void setReceipt_Policy_Violation_Count (int Receipt_Policy_Violation_Count)
{
set_Value ("Receipt_Policy_Violation_Count", new Integer(Receipt_Policy_Violation_Count));
}
/** Get receipt_policy_violation_count.
@return receipt_policy_violation_count */
public int getReceipt_Policy_Violation_Count() 
{
Integer ii = (Integer)get_Value("Receipt_Policy_Violation_Count");
if (ii == null) return 0;
return ii.intValue();
}
/** Set reimbursable_total.
@param Reimbursable_Total reimbursable_total */
public void setReimbursable_Total (BigDecimal Reimbursable_Total)
{
set_Value ("Reimbursable_Total", Reimbursable_Total);
}
/** Get reimbursable_total.
@return reimbursable_total */
public BigDecimal getReimbursable_Total() 
{
BigDecimal bd = (BigDecimal)get_Value("Reimbursable_Total");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set reimbursement_date.
@param Reimbursement_Date reimbursement_date */
public void setReimbursement_Date (Timestamp Reimbursement_Date)
{
set_Value ("Reimbursement_Date", Reimbursement_Date);
}
/** Get reimbursement_date.
@return reimbursement_date */
public Timestamp getReimbursement_Date() 
{
return (Timestamp)get_Value("Reimbursement_Date");
}
/** Set report id.
@param report_id report id */
public void setReport_Id (String report_id)
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
/** Set report_name.
@param Report_Name report_name */
public void setReport_Name (String Report_Name)
{
if (Report_Name != null && Report_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Report_Name = Report_Name.substring(0,199);
}
set_Value ("Report_Name", Report_Name);
}
/** Get report_name.
@return report_name */
public String getReport_Name() 
{
return (String)get_Value("Report_Name");
}
/** Set report_number.
@param Report_Number report_number */
public void setReport_Number (String Report_Number)
{
if (Report_Number != null && Report_Number.length() > 200)
{
log.warning("Length > 200 - truncated");
Report_Number = Report_Number.substring(0,199);
}
set_Value ("Report_Number", Report_Number);
}
/** Get report_number.
@return report_number */
public String getReport_Number() 
{
return (String)get_Value("Report_Number");
}
/** Set Start date.
@param Start_Date Start date */
public void setStart_Date (Timestamp Start_Date)
{
set_Value ("Start_Date", Start_Date);
}
/** Get Start date.
@return Start date */
public Timestamp getStart_Date() 
{
return (Timestamp)get_Value("Start_Date");
}
/** Set Status.
@param Status Status */
public void setStatus (String Status)
{
if (Status != null && Status.length() > 200)
{
log.warning("Length > 200 - truncated");
Status = Status.substring(0,199);
}
set_Value ("Status", Status);
}
/** Get Status.
@return Status */
public String getStatus() 
{
return (String)get_Value("Status");
}
/** Set sub_status.
@param Sub_Status sub_status */
public void setSub_Status (String Sub_Status)
{
if (Sub_Status != null && Sub_Status.length() > 200)
{
log.warning("Length > 200 - truncated");
Sub_Status = Sub_Status.substring(0,199);
}
set_Value ("Sub_Status", Sub_Status);
}
/** Get sub_status.
@return sub_status */
public String getSub_Status() 
{
return (String)get_Value("Sub_Status");
}
/** Set submitted_by.
@param Submitted_By submitted_by */
public void setSubmitted_By (String Submitted_By)
{
if (Submitted_By != null && Submitted_By.length() > 200)
{
log.warning("Length > 200 - truncated");
Submitted_By = Submitted_By.substring(0,199);
}
set_Value ("Submitted_By", Submitted_By);
}
/** Get submitted_by.
@return submitted_by */
public String getSubmitted_By() 
{
return (String)get_Value("Submitted_By");
}
/** Set submitted_date.
@param Submitted_Date submitted_date */
public void setSubmitted_Date (Timestamp Submitted_Date)
{
set_Value ("Submitted_Date", Submitted_Date);
}
/** Get submitted_date.
@return submitted_date */
public Timestamp getSubmitted_Date() 
{
return (Timestamp)get_Value("Submitted_Date");
}
/** Set submitted_to_email.
@param Submitted_To_Email submitted_to_email */
public void setSubmitted_To_Email (String Submitted_To_Email)
{
if (Submitted_To_Email != null && Submitted_To_Email.length() > 200)
{
log.warning("Length > 200 - truncated");
Submitted_To_Email = Submitted_To_Email.substring(0,199);
}
set_Value ("Submitted_To_Email", Submitted_To_Email);
}
/** Get submitted_to_email.
@return submitted_to_email */
public String getSubmitted_To_Email() 
{
return (String)get_Value("Submitted_To_Email");
}
/** Set submitted_to_employee_no.
@param Submitted_To_Employee_No submitted_to_employee_no */
public void setSubmitted_To_Employee_No (String Submitted_To_Employee_No)
{
if (Submitted_To_Employee_No != null && Submitted_To_Employee_No.length() > 200)
{
log.warning("Length > 200 - truncated");
Submitted_To_Employee_No = Submitted_To_Employee_No.substring(0,199);
}
set_Value ("Submitted_To_Employee_No", Submitted_To_Employee_No);
}
/** Get submitted_to_employee_no.
@return submitted_to_employee_no */
public String getSubmitted_To_Employee_No() 
{
return (String)get_Value("Submitted_To_Employee_No");
}
/** Set submitted_to_id.
@param Submitted_To_Id submitted_to_id */
public void setSubmitted_To_Id (String Submitted_To_Id)
{
if (Submitted_To_Id != null && Submitted_To_Id.length() > 200)
{
log.warning("Length > 200 - truncated");
Submitted_To_Id = Submitted_To_Id.substring(0,199);
}
set_Value ("Submitted_To_Id", Submitted_To_Id);
}
/** Get submitted_to_id.
@return submitted_to_id */
public String getSubmitted_To_Id() 
{
return (String)get_Value("Submitted_To_Id");
}
/** Set submitted_to_name.
@param Submitted_To_Name submitted_to_name */
public void setSubmitted_To_Name (String Submitted_To_Name)
{
if (Submitted_To_Name != null && Submitted_To_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Submitted_To_Name = Submitted_To_Name.substring(0,199);
}
set_Value ("Submitted_To_Name", Submitted_To_Name);
}
/** Get submitted_to_name.
@return submitted_to_name */
public String getSubmitted_To_Name() 
{
return (String)get_Value("Submitted_To_Name");
}
/** Set submitted_to_zuid.
@param Submitted_To_Zuid submitted_to_zuid */
public void setSubmitted_To_Zuid (String Submitted_To_Zuid)
{
if (Submitted_To_Zuid != null && Submitted_To_Zuid.length() > 200)
{
log.warning("Length > 200 - truncated");
Submitted_To_Zuid = Submitted_To_Zuid.substring(0,199);
}
set_Value ("Submitted_To_Zuid", Submitted_To_Zuid);
}
/** Get submitted_to_zuid.
@return submitted_to_zuid */
public String getSubmitted_To_Zuid() 
{
return (String)get_Value("Submitted_To_Zuid");
}
/** Set submitter_email.
@param Submitter_Email submitter_email */
public void setSubmitter_Email (String Submitter_Email)
{
if (Submitter_Email != null && Submitter_Email.length() > 200)
{
log.warning("Length > 200 - truncated");
Submitter_Email = Submitter_Email.substring(0,199);
}
set_Value ("Submitter_Email", Submitter_Email);
}
/** Get submitter_email.
@return submitter_email */
public String getSubmitter_Email() 
{
return (String)get_Value("Submitter_Email");
}
/** Set submitter_employee_no.
@param Submitter_Employee_No submitter_employee_no */
public void setSubmitter_Employee_No (String Submitter_Employee_No)
{
if (Submitter_Employee_No != null && Submitter_Employee_No.length() > 200)
{
log.warning("Length > 200 - truncated");
Submitter_Employee_No = Submitter_Employee_No.substring(0,199);
}
set_Value ("Submitter_Employee_No", Submitter_Employee_No);
}
/** Get submitter_employee_no.
@return submitter_employee_no */
public String getSubmitter_Employee_No() 
{
return (String)get_Value("Submitter_Employee_No");
}
/** Set submitter_name.
@param Submitter_Name submitter_name */
public void setSubmitter_Name (String Submitter_Name)
{
if (Submitter_Name != null && Submitter_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Submitter_Name = Submitter_Name.substring(0,199);
}
set_Value ("Submitter_Name", Submitter_Name);
}
/** Get submitter_name.
@return submitter_name */
public String getSubmitter_Name() 
{
return (String)get_Value("Submitter_Name");
}
/** Set submitter_photo_url.
@param Submitter_Photo_Url submitter_photo_url */
public void setSubmitter_Photo_Url (String Submitter_Photo_Url)
{
if (Submitter_Photo_Url != null && Submitter_Photo_Url.length() > 200)
{
log.warning("Length > 200 - truncated");
Submitter_Photo_Url = Submitter_Photo_Url.substring(0,199);
}
set_Value ("Submitter_Photo_Url", Submitter_Photo_Url);
}
/** Get submitter_photo_url.
@return submitter_photo_url */
public String getSubmitter_Photo_Url() 
{
return (String)get_Value("Submitter_Photo_Url");
}
/** Set submitter_zuid.
@param Submitter_Zuid submitter_zuid */
public void setSubmitter_Zuid (String Submitter_Zuid)
{
if (Submitter_Zuid != null && Submitter_Zuid.length() > 200)
{
log.warning("Length > 200 - truncated");
Submitter_Zuid = Submitter_Zuid.substring(0,199);
}
set_Value ("Submitter_Zuid", Submitter_Zuid);
}
/** Get submitter_zuid.
@return submitter_zuid */
public String getSubmitter_Zuid() 
{
return (String)get_Value("Submitter_Zuid");
}
/** Set total.
@param total total */
public void setTotal (BigDecimal total)
{
set_Value ("Total", total);
}
/** Get total.
@return total */
public BigDecimal getTotal() 
{
BigDecimal bd = (BigDecimal)get_Value("Total");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set total_policy_violation_count.
@param Total_Policy_Violation_Count total_policy_violation_count */
public void setTotal_Policy_Violation_Count (int Total_Policy_Violation_Count)
{
set_Value ("Total_Policy_Violation_Count", new Integer(Total_Policy_Violation_Count));
}
/** Get total_policy_violation_count.
@return total_policy_violation_count */
public int getTotal_Policy_Violation_Count() 
{
Integer ii = (Integer)get_Value("Total_Policy_Violation_Count");
if (ii == null) return 0;
return ii.intValue();
}
/** Set trip_budget_amount.
@param Trip_Budget_Amount trip_budget_amount */
public void setTrip_Budget_Amount (BigDecimal Trip_Budget_Amount)
{
set_Value ("Trip_Budget_Amount", Trip_Budget_Amount);
}
/** Get trip_budget_amount.
@return trip_budget_amount */
public BigDecimal getTrip_Budget_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("Trip_Budget_Amount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set trip_budget_amount_currency_code.
@param Trip_Budget_Amount_Currency_Code trip_budget_amount_currency_code */
public void setTrip_Budget_Amount_Currency_Code (String Trip_Budget_Amount_Currency_Code)
{
if (Trip_Budget_Amount_Currency_Code != null && Trip_Budget_Amount_Currency_Code.length() > 200)
{
log.warning("Length > 200 - truncated");
Trip_Budget_Amount_Currency_Code = Trip_Budget_Amount_Currency_Code.substring(0,199);
}
set_Value ("Trip_Budget_Amount_Currency_Code", Trip_Budget_Amount_Currency_Code);
}
/** Get trip_budget_amount_currency_code.
@return trip_budget_amount_currency_code */
public String getTrip_Budget_Amount_Currency_Code() 
{
return (String)get_Value("Trip_Budget_Amount_Currency_Code");
}
/** Set trip_budget_amount_currency_id.
@param Trip_Budget_Amount_Currency_ID trip_budget_amount_currency_id */
public void setTrip_Budget_Amount_Currency_ID (String Trip_Budget_Amount_Currency_ID)
{
if (Trip_Budget_Amount_Currency_ID != null && Trip_Budget_Amount_Currency_ID.length() > 200)
{
log.warning("Length > 200 - truncated");
Trip_Budget_Amount_Currency_ID = Trip_Budget_Amount_Currency_ID.substring(0,199);
}
set_Value ("Trip_Budget_Amount_Currency_ID", Trip_Budget_Amount_Currency_ID);
}
/** Get trip_budget_amount_currency_id.
@return trip_budget_amount_currency_id */
public String getTrip_Budget_Amount_Currency_ID() 
{
return (String)get_Value("Trip_Budget_Amount_Currency_ID");
}
/** Set trip_departure.
@param Trip_Departure trip_departure */
public void setTrip_Departure (String Trip_Departure)
{
if (Trip_Departure != null && Trip_Departure.length() > 200)
{
log.warning("Length > 200 - truncated");
Trip_Departure = Trip_Departure.substring(0,199);
}
set_Value ("Trip_Departure", Trip_Departure);
}
/** Get trip_departure.
@return trip_departure */
public String getTrip_Departure() 
{
return (String)get_Value("Trip_Departure");
}
/** Set trip_destination_city.
@param Trip_Destination_City trip_destination_city */
public void setTrip_Destination_City (String Trip_Destination_City)
{
if (Trip_Destination_City != null && Trip_Destination_City.length() > 200)
{
log.warning("Length > 200 - truncated");
Trip_Destination_City = Trip_Destination_City.substring(0,199);
}
set_Value ("Trip_Destination_City", Trip_Destination_City);
}
/** Get trip_destination_city.
@return trip_destination_city */
public String getTrip_Destination_City() 
{
return (String)get_Value("Trip_Destination_City");
}
/** Set trip_destination_country.
@param Trip_Destination_Country trip_destination_country */
public void setTrip_Destination_Country (String Trip_Destination_Country)
{
if (Trip_Destination_Country != null && Trip_Destination_Country.length() > 200)
{
log.warning("Length > 200 - truncated");
Trip_Destination_Country = Trip_Destination_Country.substring(0,199);
}
set_Value ("Trip_Destination_Country", Trip_Destination_Country);
}
/** Get trip_destination_country.
@return trip_destination_country */
public String getTrip_Destination_Country() 
{
return (String)get_Value("Trip_Destination_Country");
}
/** Set trip_end_date.
@param Trip_End_Date trip_end_date */
public void setTrip_End_Date (Timestamp Trip_End_Date)
{
set_Value ("Trip_End_Date", Trip_End_Date);
}
/** Get trip_end_date.
@return trip_end_date */
public Timestamp getTrip_End_Date() 
{
return (Timestamp)get_Value("Trip_End_Date");
}
/** Set trip_id.
@param Trip_Id trip_id */
public void setTrip_Id (String Trip_Id)
{
if (Trip_Id != null && Trip_Id.length() > 200)
{
log.warning("Length > 200 - truncated");
Trip_Id = Trip_Id.substring(0,199);
}
set_Value ("Trip_Id", Trip_Id);
}
/** Get trip_id.
@return trip_id */
public String getTrip_Id() 
{
return (String)get_Value("Trip_Id");
}
/** Set trip_name.
@param Trip_Name trip_name */
public void setTrip_Name (String Trip_Name)
{
if (Trip_Name != null && Trip_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Trip_Name = Trip_Name.substring(0,199);
}
set_Value ("Trip_Name", Trip_Name);
}
/** Get trip_name.
@return trip_name */
public String getTrip_Name() 
{
return (String)get_Value("Trip_Name");
}
/** Set trip_number.
@param Trip_Number trip_number */
public void setTrip_Number (String Trip_Number)
{
if (Trip_Number != null && Trip_Number.length() > 200)
{
log.warning("Length > 200 - truncated");
Trip_Number = Trip_Number.substring(0,199);
}
set_Value ("Trip_Number", Trip_Number);
}
/** Get trip_number.
@return trip_number */
public String getTrip_Number() 
{
return (String)get_Value("Trip_Number");
}
/** Set trip_start_date.
@param Trip_Start_Date trip_start_date */
public void setTrip_Start_Date (Timestamp Trip_Start_Date)
{
set_Value ("Trip_Start_Date", Trip_Start_Date);
}
/** Get trip_start_date.
@return trip_start_date */
public Timestamp getTrip_Start_Date() 
{
return (Timestamp)get_Value("Trip_Start_Date");
}
/** Set trip_status.
@param Trip_Status trip_status */
public void setTrip_Status (String Trip_Status)
{
if (Trip_Status != null && Trip_Status.length() > 200)
{
log.warning("Length > 200 - truncated");
Trip_Status = Trip_Status.substring(0,199);
}
set_Value ("Trip_Status", Trip_Status);
}
/** Get trip_status.
@return trip_status */
public String getTrip_Status() 
{
return (String)get_Value("Trip_Status");
}
/** Set uncategorized_expense_count.
@param Uncategorized_Expense_Count uncategorized_expense_count */
public void setUncategorized_Expense_Count (int Uncategorized_Expense_Count)
{
set_Value ("Uncategorized_Expense_Count", new Integer(Uncategorized_Expense_Count));
}
/** Get uncategorized_expense_count.
@return uncategorized_expense_count */
public int getUncategorized_Expense_Count() 
{
Integer ii = (Integer)get_Value("Uncategorized_Expense_Count");
if (ii == null) return 0;
return ii.intValue();
}
/** Set user_email.
@param User_Email user_email */
public void setUser_Email (String User_Email)
{
if (User_Email != null && User_Email.length() > 200)
{
log.warning("Length > 200 - truncated");
User_Email = User_Email.substring(0,199);
}
set_Value ("User_Email", User_Email);
}
/** Get user_email.
@return user_email */
public String getUser_Email() 
{
return (String)get_Value("User_Email");
}
/** Set user_id.
@param User_ID user_id */
public void setUser_ID (String User_ID)
{
if (User_ID != null && User_ID.length() > 200)
{
log.warning("Length > 200 - truncated");
User_ID = User_ID.substring(0,199);
}
set_Value ("User_ID", User_ID);
}
/** Get user_id.
@return user_id */
public String getUser_ID() 
{
return (String)get_Value("User_ID");
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
/** Set user_photo_url.
@param User_Photo_Url user_photo_url */
public void setUser_Photo_Url (String User_Photo_Url)
{
if (User_Photo_Url != null && User_Photo_Url.length() > 200)
{
log.warning("Length > 200 - truncated");
User_Photo_Url = User_Photo_Url.substring(0,199);
}
set_Value ("User_Photo_Url", User_Photo_Url);
}
/** Get user_photo_url.
@return user_photo_url */
public String getUser_Photo_Url() 
{
return (String)get_Value("User_Photo_Url");
}
/** Set zcrm_potential_id.
@param Zcrm_Potential_ID zcrm_potential_id */
public void setZcrm_Potential_ID (String Zcrm_Potential_ID)
{
if (Zcrm_Potential_ID != null && Zcrm_Potential_ID.length() > 200)
{
log.warning("Length > 200 - truncated");
Zcrm_Potential_ID = Zcrm_Potential_ID.substring(0,199);
}
set_Value ("Zcrm_Potential_ID", Zcrm_Potential_ID);
}
/** Get zcrm_potential_id.
@return zcrm_potential_id */
public String getZcrm_Potential_ID() 
{
return (String)get_Value("Zcrm_Potential_ID");
}

public void setCreated_By_Zuid (String Created_By_Zuid)
{
if (Created_By_Zuid != null && Created_By_Zuid.length() > 200)
{
log.warning("Length > 200 - truncated");
Created_By_Zuid = Created_By_Zuid.substring(0,199);
}
set_Value ("Created_By_Zuid", Created_By_Zuid);
}
/** Get Created_By_Zuid.
@return Created_By_Zuid */
public String getCreated_By_Zuid() 
{
return (String)get_Value("Created_By_Zuid");
}




public void setCreated_By_Id (String Created_By_Id)
{
if (Created_By_Id != null && Created_By_Id.length() > 200)
{
log.warning("Length > 200 - truncated");
Created_By_Id = Created_By_Id.substring(0,199);
}
set_Value ("Created_By_Id", Created_By_Id);
}
/** Get Created_By_Zuid.
@return Created_By_Zuid */
public String getCreated_By_Id() 
{
return (String)get_Value("Created_By_Id");
}



public void setCreated_By_Name (String Created_By_Name)
{
if (Created_By_Name != null && Created_By_Name.length() > 200)
{
log.warning("Length > 200 - truncated");
Created_By_Name = Created_By_Name.substring(0,199);
}
set_Value ("Created_By_Name", Created_By_Name);
}
/** Get Created_By_Name.
@return Created_By_Name */
public String getCreated_By_Name() 
{
return (String)get_Value("Created_By_Name");
}



/** Set Created_Date.
@param Created_Date Created_Date */
public void setCreated_Date (Timestamp Created_Date)
{
set_Value ("Created_Date", Created_Date);
}
/** Get Created_Date.
@return Created_Date */
public Timestamp getCreated_Date() 
{
return (Timestamp)get_Value("Created_Date");
}


/** Set Created_Time.
@param Created_Time Created_Time */
public void setCreated_Time (Timestamp Created_Time)
{
set_Value ("Created_Time", Created_Time);
}
/** Get Created_Time.
@return Created_Time */
public Timestamp getCreated_Time() 
{
return (Timestamp)get_Value("Created_Time");
}

/** Set P_Period_ID .
@param P_Period_ID P_Period_ID */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID <= 0) set_Value ("P_Period_ID", null);
 else 
set_Value ("P_Period_ID", new Integer(P_Period_ID));
}
/** Get P_Period_ID .
@return P_Period_ID */
public int getP_Period_ID() 
{
Integer ii = (Integer)get_Value("P_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** Set P_Payment_ID .
@param P_Payment_ID P_Payment_ID */
public void setP_Payment_ID (int P_Payment_ID)
{
if (P_Payment_ID <= 0) set_Value ("P_Payment_ID", null);
 else 
set_Value ("P_Payment_ID", new Integer(P_Payment_ID));
}
/** Get P_Payment_ID .
@return P_Payment_ID */
public int getP_Payment_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_ID");
if (ii == null) return 0;
return ii.intValue();
}

public void setExpensereportsType (String ExpensereportsType)
{
if (ExpensereportsType != null && ExpensereportsType.length() > 200)
{
log.warning("Length > 200 - truncated");
ExpensereportsType = ExpensereportsType.substring(0,199);
}
set_Value ("ExpensereportsType", ExpensereportsType);
}
/** Get ExpensereportsType.
@return ExpensereportsType */
public String getExpensereportsType() 
{
return (String)get_Value("ExpensereportsType");
}

/** Set Import Error Message.
@param I_ErrorMsg Messages generated from import process */
public void setI_ErrorMsg (String I_ErrorMsg)
{
if (I_ErrorMsg != null && I_ErrorMsg.length() > 2000)
{
log.warning("Length > 2000 - truncated");
I_ErrorMsg = I_ErrorMsg.substring(0,1999);
}
set_Value ("I_ErrorMsg", I_ErrorMsg);
}
/** Get Import Error Message.
@return Messages generated from import process */
public String getI_ErrorMsg() 
{
return (String)get_Value("I_ErrorMsg");
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


/** Set P_Time_Sheet_ID .
@param P_Time_Sheet_ID P_Time_Sheet_ID */
public void setP_Time_Sheet_ID (int P_Time_Sheet_ID)
{
if (P_Time_Sheet_ID <= 0) set_Value ("P_Time_Sheet_ID", null);
 else 
set_Value ("P_Time_Sheet_ID", new Integer(P_Time_Sheet_ID));
}
/** Get P_Time_Sheet_ID .
@return P_Time_Sheet_ID */
public int getP_Time_Sheet_ID() 
{
Integer ii = (Integer)get_Value("P_Time_Sheet_ID");
if (ii == null) return 0;
return ii.intValue();
}


}



