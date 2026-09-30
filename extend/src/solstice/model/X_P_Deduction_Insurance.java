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
/** Generated Model for P_Deduction_Insurance
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Deduction_Insurance extends PO
{
/** Standard Constructor
@param ctx context
@param P_Deduction_Insurance_ID id
@param trxName transaction
*/
public X_P_Deduction_Insurance (Properties ctx, int P_Deduction_Insurance_ID, String trxName)
{
super (ctx, P_Deduction_Insurance_ID, trxName);
/** if (P_Deduction_Insurance_ID == 0)
{
setInsuranceParticipant (null);
setInsuranceTypeRate (null);
setP_Deduction_Insurance_ID (0);
setP_Deduction_Param_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Deduction_Insurance (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000067 */
public static final int Table_ID=2000067;

/** TableName=P_Deduction_Insurance */
public static final String Table_Name="P_Deduction_Insurance";

protected static KeyNamePair Model = new KeyNamePair(2000067,"P_Deduction_Insurance");

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
StringBuffer sb = new StringBuffer ("X_P_Deduction_Insurance[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Layer.
@param InsuranceLayer Layer */
public void setInsuranceLayer (int InsuranceLayer)
{
set_Value ("InsuranceLayer", new Integer(InsuranceLayer));
}
/** Get Layer.
@return Layer */
public int getInsuranceLayer() 
{
Integer ii = (Integer)get_Value("InsuranceLayer");
if (ii == null) return 0;
return ii.intValue();
}

/** InsuranceParticipant AD_Reference_ID=2000040 */
public static final int INSURANCEPARTICIPANT_AD_Reference_ID=2000040;
/** Familly = 4 */
public static final String INSURANCEPARTICIPANT_Familly = "4";
/** Salaried = 1 */
public static final String INSURANCEPARTICIPANT_Salaried = "1";
/** Conjoint = 2 */
public static final String INSURANCEPARTICIPANT_Conjoint = "2";
/** Child = 3 */
public static final String INSURANCEPARTICIPANT_Child = "3";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isInsuranceParticipantValid (String test)
{
return test.equals("4") || test.equals("1") || test.equals("2") || test.equals("3");
}
/** Set Participant.
@param InsuranceParticipant Participant */
public void setInsuranceParticipant (String InsuranceParticipant)
{
if (InsuranceParticipant == null) throw new IllegalArgumentException ("InsuranceParticipant is mandatory");
if (!isInsuranceParticipantValid(InsuranceParticipant))
throw new IllegalArgumentException ("InsuranceParticipant Invalid value - " + InsuranceParticipant + " - Reference_ID=2000040 - 4 - 1 - 2 - 3");
if (InsuranceParticipant.length() > 1)
{
log.warning("Length > 1 - truncated");
InsuranceParticipant = InsuranceParticipant.substring(0,0);
}
set_Value ("InsuranceParticipant", InsuranceParticipant);
}
/** Get Participant.
@return Participant */
public String getInsuranceParticipant() 
{
return (String)get_Value("InsuranceParticipant");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getInsuranceParticipant()));
}

/** InsuranceTypeRate AD_Reference_ID=2000041 */
public static final int INSURANCETYPERATE_AD_Reference_ID=2000041;
/** Employee = 1 */
public static final String INSURANCETYPERATE_Employee = "1";
/** Employer = 2 */
public static final String INSURANCETYPERATE_Employer = "2";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isInsuranceTypeRateValid (String test)
{
return test.equals("1") || test.equals("2");
}
/** Set Type Rate.
@param InsuranceTypeRate Type Rate */
public void setInsuranceTypeRate (String InsuranceTypeRate)
{
if (InsuranceTypeRate == null) throw new IllegalArgumentException ("InsuranceTypeRate is mandatory");
if (!isInsuranceTypeRateValid(InsuranceTypeRate))
throw new IllegalArgumentException ("InsuranceTypeRate Invalid value - " + InsuranceTypeRate + " - Reference_ID=2000041 - 1 - 2");
if (InsuranceTypeRate.length() > 1)
{
log.warning("Length > 1 - truncated");
InsuranceTypeRate = InsuranceTypeRate.substring(0,0);
}
set_Value ("InsuranceTypeRate", InsuranceTypeRate);
}
/** Get Type Rate.
@return Type Rate */
public String getInsuranceTypeRate() 
{
return (String)get_Value("InsuranceTypeRate");
}
/** Set Insurance.
@param P_Deduction_Insurance_ID Insurance */
public void setP_Deduction_Insurance_ID (int P_Deduction_Insurance_ID)
{
if (P_Deduction_Insurance_ID < 1) throw new IllegalArgumentException ("P_Deduction_Insurance_ID is mandatory.");
set_Value ("P_Deduction_Insurance_ID", new Integer(P_Deduction_Insurance_ID));
}
/** Get Insurance.
@return Insurance */
public int getP_Deduction_Insurance_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_Insurance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Deduction.
@param P_Deduction_Param_ID Deduction */
public void setP_Deduction_Param_ID (int P_Deduction_Param_ID)
{
if (P_Deduction_Param_ID < 1) throw new IllegalArgumentException ("P_Deduction_Param_ID is mandatory.");
set_Value ("P_Deduction_Param_ID", new Integer(P_Deduction_Param_ID));
}
/** Get Deduction.
@return Deduction */
public int getP_Deduction_Param_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_Param_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
