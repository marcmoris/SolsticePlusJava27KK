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
/** Generated Model for P_Credits_Account
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Credits_Account extends PO
{
/** Standard Constructor
@param ctx context
@param P_Credits_Account_ID id
@param trxName transaction
*/
public X_P_Credits_Account (Properties ctx, int P_Credits_Account_ID, String trxName)
{
super (ctx, P_Credits_Account_ID, trxName);
/** if (P_Credits_Account_ID == 0)
{
setP_Credits_Account_ID (0);
setP_Credits_Acct (0);
setP_Credits_ID (0);
setP_Occupation_Group_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Credits_Account (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100663 */
public static final int Table_ID=2100663;

/** TableName=P_Credits_Account */
public static final String Table_Name="P_Credits_Account";

protected static KeyNamePair Model = new KeyNamePair(2100663,"P_Credits_Account");

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
StringBuffer sb = new StringBuffer ("X_P_Credits_Account[").append(get_ID()).append("]");
return sb.toString();
}

/** C_Activity_ID AD_Reference_ID=142 */
public static final int C_ACTIVITY_ID_AD_Reference_ID=142;
/** Set Crew cost.
@param C_Activity_ID Crew cost */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
 else 
set_Value ("C_Activity_ID", new Integer(C_Activity_ID));
}
/** Get Crew cost.
@return Crew cost */
public int getC_Activity_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_Activity_Src_ID AD_Reference_ID=142 */
public static final int C_ACTIVITY_SRC_ID_AD_Reference_ID=142;
/** Set Crew cost.
@param C_Activity_Src_ID Crew cost */
public void setC_Activity_Src_ID (int C_Activity_Src_ID)
{
if (C_Activity_Src_ID <= 0) set_Value ("C_Activity_Src_ID", null);
 else 
set_Value ("C_Activity_Src_ID", new Integer(C_Activity_Src_ID));
}
/** Get Crew cost.
@return Crew cost */
public int getC_Activity_Src_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_Src_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Base.
@param C_SalesRegion_ID Base */
public void setC_SalesRegion_ID (int C_SalesRegion_ID)
{
if (C_SalesRegion_ID <= 0) set_Value ("C_SalesRegion_ID", null);
 else 
set_Value ("C_SalesRegion_ID", new Integer(C_SalesRegion_ID));
}
/** Get Base.
@return Base */
public int getC_SalesRegion_ID() 
{
Integer ii = (Integer)get_Value("C_SalesRegion_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Credits_Account_ID.
@param P_Credits_Account_ID P_Credits_Account_ID */
public void setP_Credits_Account_ID (int P_Credits_Account_ID)
{
if (P_Credits_Account_ID < 1) throw new IllegalArgumentException ("P_Credits_Account_ID is mandatory.");
set_Value ("P_Credits_Account_ID", new Integer(P_Credits_Account_ID));
}
/** Get P_Credits_Account_ID.
@return P_Credits_Account_ID */
public int getP_Credits_Account_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Account_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Credits_Acct AD_Reference_ID=132 */
public static final int P_CREDITS_ACCT_AD_Reference_ID=132;
/** Set Account.
@param P_Credits_Acct Account */
public void setP_Credits_Acct (int P_Credits_Acct)
{
set_Value ("P_Credits_Acct", new Integer(P_Credits_Acct));
}
/** Get Account.
@return Account */
public int getP_Credits_Acct() 
{
Integer ii = (Integer)get_Value("P_Credits_Acct");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Credits_ID AD_Reference_ID=2100318 */
public static final int P_CREDITS_ID_AD_Reference_ID=2100318;
/** Set Credits.
@param P_Credits_ID Credits */
public void setP_Credits_ID (int P_Credits_ID)
{
if (P_Credits_ID < 1) throw new IllegalArgumentException ("P_Credits_ID is mandatory.");
set_ValueNoCheck ("P_Credits_ID", new Integer(P_Credits_ID));
}
/** Get Credits.
@return Credits */
public int getP_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Job_Title_ID AD_Reference_ID=2000025 */
public static final int P_JOB_TITLE_ID_AD_Reference_ID=2000025;
/** Set Job Title.
@param P_Job_Title_ID   */
public void setP_Job_Title_ID (int P_Job_Title_ID)
{
if (P_Job_Title_ID <= 0) set_Value ("P_Job_Title_ID", null);
 else 
set_Value ("P_Job_Title_ID", new Integer(P_Job_Title_ID));
}
/** Get Job Title.
@return   */
public int getP_Job_Title_ID() 
{
Integer ii = (Integer)get_Value("P_Job_Title_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Job_Type_ID AD_Reference_ID=2000054 */
public static final int P_JOB_TYPE_ID_AD_Reference_ID=2000054;
/** Set Job Type.
@param P_Job_Type_ID Job Type */
public void setP_Job_Type_ID (int P_Job_Type_ID)
{
if (P_Job_Type_ID <= 0) set_Value ("P_Job_Type_ID", null);
 else 
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

/** P_Occupation_Group_ID AD_Reference_ID=2000014 */
public static final int P_OCCUPATION_GROUP_ID_AD_Reference_ID=2000014;
/** Set Occupation Group.
@param P_Occupation_Group_ID   */
public void setP_Occupation_Group_ID (int P_Occupation_Group_ID)
{
set_Value ("P_Occupation_Group_ID", new Integer(P_Occupation_Group_ID));
}
/** Get Occupation Group.
@return   */
public int getP_Occupation_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Occupation_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
