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
/** Generated Model for P_Interorg_Acct
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Interorg_Acct extends PO
{
/** Standard Constructor
@param ctx context
@param P_Interorg_Acct_ID id
@param trxName transaction
*/
public X_P_Interorg_Acct (Properties ctx, int P_Interorg_Acct_ID, String trxName)
{
super (ctx, P_Interorg_Acct_ID, trxName);
/** if (P_Interorg_Acct_ID == 0)
{
setAD_OrgTo_ID (0);
setC_Activity_ID (0);
setP_INTERORG_ACCT_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Interorg_Acct (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201214 */
public static final int Table_ID=3201214;

/** TableName=P_Interorg_Acct */
public static final String Table_Name="P_Interorg_Acct";

protected static KeyNamePair Model = new KeyNamePair(3201214,"P_Interorg_Acct");

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
StringBuffer sb = new StringBuffer ("X_P_Interorg_Acct[").append(get_ID()).append("]");
return sb.toString();
}

/** AD_OrgTo_ID AD_Reference_ID=322 */
public static final int AD_ORGTO_ID_AD_Reference_ID=322;
/** Set Inter-Organization.
@param AD_OrgTo_ID Organization valid for intercompany documents */
public void setAD_OrgTo_ID (int AD_OrgTo_ID)
{
if (AD_OrgTo_ID < 1) throw new IllegalArgumentException ("AD_OrgTo_ID is mandatory.");
set_Value ("AD_OrgTo_ID", new Integer(AD_OrgTo_ID));
}
/** Get Inter-Organization.
@return Organization valid for intercompany documents */
public int getAD_OrgTo_ID() 
{
Integer ii = (Integer)get_Value("AD_OrgTo_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_Activity_ID AD_Reference_ID=142 */
public static final int C_ACTIVITY_ID_AD_Reference_ID=142;
/** Set Function.
@param C_Activity_ID Function */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID < 1) throw new IllegalArgumentException ("C_Activity_ID is mandatory.");
set_Value ("C_Activity_ID", new Integer(C_Activity_ID));
}
/** Get Function.
@return Function */
public int getC_Activity_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** IntercompanyDueFrom_Acct AD_Reference_ID=182 */
public static final int INTERCOMPANYDUEFROM_ACCT_AD_Reference_ID=182;
/** Set Intercompany Due From Acct.
@param IntercompanyDueFrom_Acct Intercompany Due From / Receivables Account */
public void setIntercompanyDueFrom_Acct (int IntercompanyDueFrom_Acct)
{
set_Value ("IntercompanyDueFrom_Acct", new Integer(IntercompanyDueFrom_Acct));
}
/** Get Intercompany Due From Acct.
@return Intercompany Due From / Receivables Account */
public int getIntercompanyDueFrom_Acct() 
{
Integer ii = (Integer)get_Value("IntercompanyDueFrom_Acct");
if (ii == null) return 0;
return ii.intValue();
}

/** IntercompanyDueTo_Acct AD_Reference_ID=182 */
public static final int INTERCOMPANYDUETO_ACCT_AD_Reference_ID=182;
/** Set Intercompany Due To Acct.
@param IntercompanyDueTo_Acct Intercompany Due To / Payable Account */
public void setIntercompanyDueTo_Acct (int IntercompanyDueTo_Acct)
{
set_Value ("IntercompanyDueTo_Acct", new Integer(IntercompanyDueTo_Acct));
}
/** Get Intercompany Due To Acct.
@return Intercompany Due To / Payable Account */
public int getIntercompanyDueTo_Acct() 
{
Integer ii = (Integer)get_Value("IntercompanyDueTo_Acct");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_INTERORG_ACCT_ID.
@param P_INTERORG_ACCT_ID P_INTERORG_ACCT_ID */
public void setP_INTERORG_ACCT_ID (int P_INTERORG_ACCT_ID)
{
if (P_INTERORG_ACCT_ID < 1) throw new IllegalArgumentException ("P_INTERORG_ACCT_ID is mandatory.");
set_ValueNoCheck ("P_INTERORG_ACCT_ID", new Integer(P_INTERORG_ACCT_ID));
}
/** Get P_INTERORG_ACCT_ID.
@return P_INTERORG_ACCT_ID */
public int getP_INTERORG_ACCT_ID() 
{
Integer ii = (Integer)get_Value("P_INTERORG_ACCT_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
