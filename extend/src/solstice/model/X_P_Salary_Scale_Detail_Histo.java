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
/** Generated Model for P_Salary_Scale_Detail_Histo
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Salary_Scale_Detail_Histo extends PO
{
/** Standard Constructor
@param ctx context
@param P_Salary_Scale_Detail_Histo_ID id
@param trxName transaction
*/
public X_P_Salary_Scale_Detail_Histo (Properties ctx, int P_Salary_Scale_Detail_Histo_ID, String trxName)
{
super (ctx, P_Salary_Scale_Detail_Histo_ID, trxName);
/** if (P_Salary_Scale_Detail_Histo_ID == 0)
{
setP_Salary_Scale_Detail_ID (0);
setP_Salary_Scale_Histo_ID (0);
setStep (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Salary_Scale_Detail_Histo (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000053 */
public static final int Table_ID=2000053;

/** TableName=P_Salary_Scale_Detail_Histo */
public static final String Table_Name="P_Salary_Scale_Detail_Histo";

protected static KeyNamePair Model = new KeyNamePair(2000053,"P_Salary_Scale_Detail_Histo");

protected BigDecimal accessLevel = new BigDecimal(7);
/** AccessLevel
@return 7 - System - Client - Org 
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
StringBuffer sb = new StringBuffer ("X_P_Salary_Scale_Detail_Histo[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Annual Salary.
@param Annual_Salary   */
public void setAnnual_Salary (BigDecimal Annual_Salary)
{
set_Value ("Annual_Salary", Annual_Salary);
}
/** Get Annual Salary.
@return   */
public BigDecimal getAnnual_Salary() 
{
BigDecimal bd = (BigDecimal)get_Value("Annual_Salary");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hourly Rate.
@param Hourly_Rate   */
public void setHourly_Rate (BigDecimal Hourly_Rate)
{
set_Value ("Hourly_Rate", Hourly_Rate);
}
/** Get Hourly Rate.
@return   */
public BigDecimal getHourly_Rate() 
{
BigDecimal bd = (BigDecimal)get_Value("Hourly_Rate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Limit Max.
@param Limit_Max Maximum  */
public void setLimit_Max (int Limit_Max)
{
set_Value ("Limit_Max", new Integer(Limit_Max));
}
/** Get Limit Max.
@return Maximum  */
public int getLimit_Max() 
{
Integer ii = (Integer)get_Value("Limit_Max");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Limit Min.
@param Limit_Min Minimum  */
public void setLimit_Min (int Limit_Min)
{
set_Value ("Limit_Min", new Integer(Limit_Min));
}
/** Get Limit Min.
@return Minimum  */
public int getLimit_Min() 
{
Integer ii = (Integer)get_Value("Limit_Min");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Step.
@param P_Salary_Scale_Detail_ID   */
public void setP_Salary_Scale_Detail_ID (int P_Salary_Scale_Detail_ID)
{
if (P_Salary_Scale_Detail_ID < 1) throw new IllegalArgumentException ("P_Salary_Scale_Detail_ID is mandatory.");
set_Value ("P_Salary_Scale_Detail_ID", new Integer(P_Salary_Scale_Detail_ID));
}
/** Get Step.
@return   */
public int getP_Salary_Scale_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Salary_Scale_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Salary_Scale_Histo_ID.
@param P_Salary_Scale_Histo_ID P_Salary_Scale_Histo_ID */
public void setP_Salary_Scale_Histo_ID (int P_Salary_Scale_Histo_ID)
{
if (P_Salary_Scale_Histo_ID < 1) throw new IllegalArgumentException ("P_Salary_Scale_Histo_ID is mandatory.");
set_Value ("P_Salary_Scale_Histo_ID", new Integer(P_Salary_Scale_Histo_ID));
}
/** Get P_Salary_Scale_Histo_ID.
@return P_Salary_Scale_Histo_ID */
public int getP_Salary_Scale_Histo_ID() 
{
Integer ii = (Integer)get_Value("P_Salary_Scale_Histo_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Step.
@param Step   */
public void setStep (String Step)
{
if (Step == null) throw new IllegalArgumentException ("Step is mandatory.");
if (Step.length() > 30)
{
log.warning("Length > 30 - truncated");
Step = Step.substring(0,29);
}
set_Value ("Step", Step);
}
/** Get Step.
@return   */
public String getStep() 
{
return (String)get_Value("Step");
}
}
