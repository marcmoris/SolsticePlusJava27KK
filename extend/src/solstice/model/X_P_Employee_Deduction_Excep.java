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
/** Generated Model for P_Employee_Deduction_Excep
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Deduction_Excep extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Deduction_Excep_ID id
@param trxName transaction
*/
public X_P_Employee_Deduction_Excep (Properties ctx, int P_Employee_Deduction_Excep_ID, String trxName)
{
super (ctx, P_Employee_Deduction_Excep_ID, trxName);
/** if (P_Employee_Deduction_Excep_ID == 0)
{
setP_Deduction_ID (0);
setP_Employee_Deduction_Excep_ID (0);
setP_Employee_Deduction_ID (0);
setP_Employee_ID (0);
setP_Period_ID (0);	// @P_Period_ID@
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Deduction_Excep (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000165 */
public static final int Table_ID=2000165;

/** TableName=P_Employee_Deduction_Excep */
public static final String Table_Name="P_Employee_Deduction_Excep";

protected static KeyNamePair Model = new KeyNamePair(2000165,"P_Employee_Deduction_Excep");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Deduction_Excep[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Amount Employee.
@param ExceptionAmountEmployee Amount Employee */
public void setExceptionAmountEmployee (BigDecimal ExceptionAmountEmployee)
{
set_Value ("ExceptionAmountEmployee", ExceptionAmountEmployee);
}
/** Get Amount Employee.
@return Amount Employee */
public BigDecimal getExceptionAmountEmployee() 
{
BigDecimal bd = (BigDecimal)get_Value("ExceptionAmountEmployee");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Amount Employee Additional.
@param ExceptionAmountEmployeeAdd Amount Employee Additional */
public void setExceptionAmountEmployeeAdd (BigDecimal ExceptionAmountEmployeeAdd)
{
set_Value ("ExceptionAmountEmployeeAdd", ExceptionAmountEmployeeAdd);
}
/** Get Amount Employee Additional.
@return Amount Employee Additional */
public BigDecimal getExceptionAmountEmployeeAdd() 
{
BigDecimal bd = (BigDecimal)get_Value("ExceptionAmountEmployeeAdd");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Amount Employer.
@param ExceptionAmountEmployer Amount Employer */
public void setExceptionAmountEmployer (BigDecimal ExceptionAmountEmployer)
{
set_Value ("ExceptionAmountEmployer", ExceptionAmountEmployer);
}
/** Get Amount Employer.
@return Amount Employer */
public BigDecimal getExceptionAmountEmployer() 
{
BigDecimal bd = (BigDecimal)get_Value("ExceptionAmountEmployer");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Amount Employer Additional.
@param ExceptionAmountEmployerAdd Amount Employer Additional */
public void setExceptionAmountEmployerAdd (BigDecimal ExceptionAmountEmployerAdd)
{
set_Value ("ExceptionAmountEmployerAdd", ExceptionAmountEmployerAdd);
}
/** Get Amount Employer Additional.
@return Amount Employer Additional */
public BigDecimal getExceptionAmountEmployerAdd() 
{
BigDecimal bd = (BigDecimal)get_Value("ExceptionAmountEmployerAdd");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employer Rate.
@param ExceptionEmployerRate Employer Rate */
public void setExceptionEmployerRate (BigDecimal ExceptionEmployerRate)
{
set_Value ("ExceptionEmployerRate", ExceptionEmployerRate);
}
/** Get Employer Rate.
@return Employer Rate */
public BigDecimal getExceptionEmployerRate() 
{
BigDecimal bd = (BigDecimal)get_Value("ExceptionEmployerRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Salary Rate.
@param ExceptionSalaryRate Salary Rate */
public void setExceptionSalaryRate (BigDecimal ExceptionSalaryRate)
{
set_Value ("ExceptionSalaryRate", ExceptionSalaryRate);
}
/** Get Salary Rate.
@return Salary Rate */
public BigDecimal getExceptionSalaryRate() 
{
BigDecimal bd = (BigDecimal)get_Value("ExceptionSalaryRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Exonerated.
@param IsExonerated Exonerated */
public void setIsExonerated (boolean IsExonerated)
{
set_Value ("IsExonerated", new Boolean(IsExonerated));
}
/** Get Exonerated.
@return Exonerated */
public boolean isExonerated() 
{
Object oo = get_Value("IsExonerated");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}

/** P_Deduction_ID AD_Reference_ID=2100465 */
public static final int P_DEDUCTION_ID_AD_Reference_ID=2100465;
/** Set Deduction.
@param P_Deduction_ID   */
public void setP_Deduction_ID (int P_Deduction_ID)
{
if (P_Deduction_ID < 1) throw new IllegalArgumentException ("P_Deduction_ID is mandatory.");
set_Value ("P_Deduction_ID", new Integer(P_Deduction_ID));
}
/** Get Deduction.
@return   */
public int getP_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Employee_Deduction_Excep_ID.
@param P_Employee_Deduction_Excep_ID P_Employee_Deduction_Excep_ID */
public void setP_Employee_Deduction_Excep_ID (int P_Employee_Deduction_Excep_ID)
{
if (P_Employee_Deduction_Excep_ID < 1) throw new IllegalArgumentException ("P_Employee_Deduction_Excep_ID is mandatory.");
set_Value ("P_Employee_Deduction_Excep_ID", new Integer(P_Employee_Deduction_Excep_ID));
}
/** Get P_Employee_Deduction_Excep_ID.
@return P_Employee_Deduction_Excep_ID */
public int getP_Employee_Deduction_Excep_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Deduction_Excep_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Deduction.
@param P_Employee_Deduction_ID Deduction */
public void setP_Employee_Deduction_ID (int P_Employee_Deduction_ID)
{
if (P_Employee_Deduction_ID < 1) throw new IllegalArgumentException ("P_Employee_Deduction_ID is mandatory.");
set_Value ("P_Employee_Deduction_ID", new Integer(P_Employee_Deduction_ID));
}
/** Get Deduction.
@return Deduction */
public int getP_Employee_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Deduction_ID");
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

/** P_Period_ID AD_Reference_ID=2000079 */
public static final int P_PERIOD_ID_AD_Reference_ID=2000079;
/** Set Period.
@param P_Period_ID Period of the Calendar   */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID < 1) throw new IllegalArgumentException ("P_Period_ID is mandatory.");
set_Value ("P_Period_ID", new Integer(P_Period_ID));
}
/** Get Period.
@return Period of the Calendar   */
public int getP_Period_ID() 
{
Integer ii = (Integer)get_Value("P_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
