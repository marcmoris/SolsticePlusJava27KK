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
/** Generated Model for P_BankAccount
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_BankAccount extends PO
{
/** Standard Constructor
@param ctx context
@param P_BankAccount_ID id
@param trxName transaction
*/
public X_P_BankAccount (Properties ctx, int P_BankAccount_ID, String trxName)
{
super (ctx, P_BankAccount_ID, trxName);
/** if (P_BankAccount_ID == 0)
{
setAccountNo (null);
setBranchNumber (null);
setC_Currency_ID (0);
setIsDefault (false);
setP_Bank_ID (0);
setP_BankAccount_ID (0);
setP_Financial_Institution_ID (0);
setTransfertNumber (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_BankAccount (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000042 */
public static final int Table_ID=2000042;

/** TableName=P_BankAccount */
public static final String Table_Name="P_BankAccount";

protected static KeyNamePair Model = new KeyNamePair(2000042,"P_BankAccount");

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
StringBuffer sb = new StringBuffer ("X_P_BankAccount[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Account No.
@param AccountNo Account Number */
public void setAccountNo (String AccountNo)
{
if (AccountNo == null) throw new IllegalArgumentException ("AccountNo is mandatory.");
if (AccountNo.length() > 40)
{
log.warning("Length > 40 - truncated");
AccountNo = AccountNo.substring(0,39);
}
set_Value ("AccountNo", AccountNo);
}
/** Get Account No.
@return Account Number */
public String getAccountNo() 
{
return (String)get_Value("AccountNo");
}
/** Set Branch Number.
@param BranchNumber Branch Number */
public void setBranchNumber (String BranchNumber)
{
if (BranchNumber == null) throw new IllegalArgumentException ("BranchNumber is mandatory.");
if (BranchNumber.length() > 15)
{
log.warning("Length > 15 - truncated");
BranchNumber = BranchNumber.substring(0,14);
}
set_Value ("BranchNumber", BranchNumber);
}
/** Get Branch Number.
@return Branch Number */
public String getBranchNumber() 
{
return (String)get_Value("BranchNumber");
}
/** Set Currency.
@param C_Currency_ID The Currency for this record */
public void setC_Currency_ID (int C_Currency_ID)
{
if (C_Currency_ID < 1) throw new IllegalArgumentException ("C_Currency_ID is mandatory.");
set_Value ("C_Currency_ID", new Integer(C_Currency_ID));
}
/** Get Currency.
@return The Currency for this record */
public int getC_Currency_ID() 
{
Integer ii = (Integer)get_Value("C_Currency_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Default.
@param IsDefault Default value */
public void setIsDefault (boolean IsDefault)
{
set_Value ("IsDefault", new Boolean(IsDefault));
}
/** Get Default.
@return Default value */
public boolean isDefault() 
{
Object oo = get_Value("IsDefault");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Bank.
@param P_Bank_ID   */
public void setP_Bank_ID (int P_Bank_ID)
{
if (P_Bank_ID < 1) throw new IllegalArgumentException ("P_Bank_ID is mandatory.");
set_ValueNoCheck ("P_Bank_ID", new Integer(P_Bank_ID));
}
/** Get Bank.
@return   */
public int getP_Bank_ID() 
{
Integer ii = (Integer)get_Value("P_Bank_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getP_Bank_ID()));
}
/** Set Bank Account.
@param P_BankAccount_ID Account at the Bank  */
public void setP_BankAccount_ID (int P_BankAccount_ID)
{
if (P_BankAccount_ID < 1) throw new IllegalArgumentException ("P_BankAccount_ID is mandatory.");
set_ValueNoCheck ("P_BankAccount_ID", new Integer(P_BankAccount_ID));
}
/** Get Bank Account.
@return Account at the Bank  */
public int getP_BankAccount_ID() 
{
Integer ii = (Integer)get_Value("P_BankAccount_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Current_Acct AD_Reference_ID=132 */
public static final int P_CURRENT_ACCT_AD_Reference_ID=132;
/** Set Current Account.
@param P_Current_Acct Current Account  */
public void setP_Current_Acct (int P_Current_Acct)
{
set_Value ("P_Current_Acct", new Integer(P_Current_Acct));
}
/** Get Current Account.
@return Current Account  */
public int getP_Current_Acct() 
{
Integer ii = (Integer)get_Value("P_Current_Acct");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Current_Acct_360 AD_Reference_ID=132 */
public static final int P_CURRENT_ACCT_360_AD_Reference_ID=132;
/** Set Current Account 360.
@param P_Current_Acct_360 Current Account 360 */
public void setP_Current_Acct_360 (int P_Current_Acct_360)
{
set_Value ("P_Current_Acct_360", new Integer(P_Current_Acct_360));
}
/** Get Current Account 360.
@return Current Account 360 */
public int getP_Current_Acct_360() 
{
Integer ii = (Integer)get_Value("P_Current_Acct_360");
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
/** Set Header Protocol.
@param ProtocolRsn Header protocol from file tran */
public void setProtocolRsn (String ProtocolRsn)
{
if (ProtocolRsn != null && ProtocolRsn.length() > 30)
{
log.warning("Length > 30 - truncated");
ProtocolRsn = ProtocolRsn.substring(0,29);
}
set_Value ("ProtocolRsn", ProtocolRsn);
}
/** Get Header Protocol.
@return Header protocol from file tran */
public String getProtocolRsn() 
{
return (String)get_Value("ProtocolRsn");
}
/** Set Number Transfert.
@param TransfertNumber Number Transfert of bank */
public void setTransfertNumber (String TransfertNumber)
{
if (TransfertNumber == null) throw new IllegalArgumentException ("TransfertNumber is mandatory.");
if (TransfertNumber.length() > 22)
{
log.warning("Length > 22 - truncated");
TransfertNumber = TransfertNumber.substring(0,21);
}
set_Value ("TransfertNumber", TransfertNumber);
}
/** Get Number Transfert.
@return Number Transfert of bank */
public String getTransfertNumber() 
{
return (String)get_Value("TransfertNumber");
}
}
