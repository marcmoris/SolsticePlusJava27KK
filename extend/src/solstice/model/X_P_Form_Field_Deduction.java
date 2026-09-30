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
/** Generated Model for P_Form_Field_Deduction
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Form_Field_Deduction extends PO
{
/** Standard Constructor
@param ctx context
@param P_Form_Field_Deduction_ID id
@param trxName transaction
*/
public X_P_Form_Field_Deduction (Properties ctx, int P_Form_Field_Deduction_ID, String trxName)
{
super (ctx, P_Form_Field_Deduction_ID, trxName);
/** if (P_Form_Field_Deduction_ID == 0)
{
setP_Deduction_ID (0);
setP_Form_Field_Deduction_ID (0);
setP_Form_Field_ID (0);	// @P_Form_Field_ID@
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Form_Field_Deduction (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100672 */
public static final int Table_ID=2100672;

/** TableName=P_Form_Field_Deduction */
public static final String Table_Name="P_Form_Field_Deduction";

protected static KeyNamePair Model = new KeyNamePair(2100672,"P_Form_Field_Deduction");

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
StringBuffer sb = new StringBuffer ("X_P_Form_Field_Deduction[").append(get_ID()).append("]");
return sb.toString();
}

/** DEDUCTIONTYPE AD_Reference_ID=2100446 */
public static final int DEDUCTIONTYPE_AD_Reference_ID=2100446;
/** Montant  employé + employeur = 4 */
public static final String DEDUCTIONTYPE_MontantEmployéPlusEmployeur = "4";
/** Salary eligible = 1 */
public static final String DEDUCTIONTYPE_SalaryEligible = "1";
/** Montant employé = 2 */
public static final String DEDUCTIONTYPE_MontantEmployé = "2";
/** Montant employeur = 3 */
public static final String DEDUCTIONTYPE_MontantEmployeur = "3";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isDEDUCTIONTYPEValid (String test)
{
return test == null || test.equals("4") || test.equals("1") || test.equals("2") || test.equals("3");
}
/** Set DEDUCTIONTYPE.
@param DEDUCTIONTYPE DEDUCTIONTYPE */
public void setDEDUCTIONTYPE (String DEDUCTIONTYPE)
{
if (!isDEDUCTIONTYPEValid(DEDUCTIONTYPE))
throw new IllegalArgumentException ("DEDUCTIONTYPE Invalid value - " + DEDUCTIONTYPE + " - Reference_ID=2100446 - 4 - 1 - 2 - 3");
if (DEDUCTIONTYPE != null && DEDUCTIONTYPE.length() > 1)
{
log.warning("Length > 1 - truncated");
DEDUCTIONTYPE = DEDUCTIONTYPE.substring(0,0);
}
set_Value ("DEDUCTIONTYPE", DEDUCTIONTYPE);
}
/** Get DEDUCTIONTYPE.
@return DEDUCTIONTYPE */
public String getDEDUCTIONTYPE() 
{
return (String)get_Value("DEDUCTIONTYPE");
}

/** P_Deduction_ID AD_Reference_ID=2000039 */
public static final int P_DEDUCTION_ID_AD_Reference_ID=2000039;
/** Set Deduction.
@param P_Deduction_ID   */
public void setP_Deduction_ID (int P_Deduction_ID)
{
if (P_Deduction_ID < 1) throw new IllegalArgumentException ("P_Deduction_ID is mandatory.");
set_ValueNoCheck ("P_Deduction_ID", new Integer(P_Deduction_ID));
}
/** Get Deduction.
@return   */
public int getP_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Form_Field_Deduction_ID.
@param P_Form_Field_Deduction_ID P_Form_Field_Deduction_ID */
public void setP_Form_Field_Deduction_ID (int P_Form_Field_Deduction_ID)
{
if (P_Form_Field_Deduction_ID < 1) throw new IllegalArgumentException ("P_Form_Field_Deduction_ID is mandatory.");
set_ValueNoCheck ("P_Form_Field_Deduction_ID", new Integer(P_Form_Field_Deduction_ID));
}
/** Get P_Form_Field_Deduction_ID.
@return P_Form_Field_Deduction_ID */
public int getP_Form_Field_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Form_Field_Deduction_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Field.
@param P_Form_Field_ID Field */
public void setP_Form_Field_ID (int P_Form_Field_ID)
{
if (P_Form_Field_ID < 1) throw new IllegalArgumentException ("P_Form_Field_ID is mandatory.");
set_ValueNoCheck ("P_Form_Field_ID", new Integer(P_Form_Field_ID));
}
/** Get Field.
@return Field */
public int getP_Form_Field_ID() 
{
Integer ii = (Integer)get_Value("P_Form_Field_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
