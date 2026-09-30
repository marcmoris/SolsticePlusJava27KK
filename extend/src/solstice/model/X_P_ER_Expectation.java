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
/** Generated Model for P_ER_Expectation
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_ER_Expectation extends PO
{
/** Standard Constructor
@param ctx context
@param P_ER_Expectation_ID id
@param trxName transaction
*/
public X_P_ER_Expectation (Properties ctx, int P_ER_Expectation_ID, String trxName)
{
super (ctx, P_ER_Expectation_ID, trxName);
/** if (P_ER_Expectation_ID == 0)
{
setP_ER_Evaluation_ID (0);
setP_ER_Expectation_ID (0);
setPNumber (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_ER_Expectation (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100724 */
public static final int Table_ID=2100724;

/** TableName=P_ER_Expectation */
public static final String Table_Name="P_ER_Expectation";

protected static KeyNamePair Model = new KeyNamePair(2100724,"P_ER_Expectation");

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
StringBuffer sb = new StringBuffer ("X_P_ER_Expectation[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Achievement assessment.
@param AchievementAssessment Achievement assessment */
public void setAchievementAssessment (String AchievementAssessment)
{
if (AchievementAssessment != null && AchievementAssessment.length() > 1024)
{
log.warning("Length > 1024 - truncated");
AchievementAssessment = AchievementAssessment.substring(0,1023);
}
set_Value ("AchievementAssessment", AchievementAssessment);
}
/** Get Achievement assessment.
@return Achievement assessment */
public String getAchievementAssessment() 
{
return (String)get_Value("AchievementAssessment");
}
/** Set Appreciation Element.
@param AppreciationElement Appreciation Element */
public void setAppreciationElement (String AppreciationElement)
{
if (AppreciationElement != null && AppreciationElement.length() > 1024)
{
log.warning("Length > 1024 - truncated");
AppreciationElement = AppreciationElement.substring(0,1023);
}
set_Value ("AppreciationElement", AppreciationElement);
}
/** Get Appreciation Element.
@return Appreciation Element */
public String getAppreciationElement() 
{
return (String)get_Value("AppreciationElement");
}
/** Set Commentary.
@param Commentary Commentary */
public void setCommentary (String Commentary)
{
if (Commentary != null && Commentary.length() > 1024)
{
log.warning("Length > 1024 - truncated");
Commentary = Commentary.substring(0,1023);
}
set_Value ("Commentary", Commentary);
}
/** Get Commentary.
@return Commentary */
public String getCommentary() 
{
return (String)get_Value("Commentary");
}
/** Set Expectation quotation.
@param ExpectationQuotation Expectation quotation */
public void setExpectationQuotation (String ExpectationQuotation)
{
if (ExpectationQuotation != null && ExpectationQuotation.length() > 3)
{
log.warning("Length > 3 - truncated");
ExpectationQuotation = ExpectationQuotation.substring(0,2);
}
set_Value ("ExpectationQuotation", ExpectationQuotation);
}
/** Get Expectation quotation.
@return Expectation quotation */
public String getExpectationQuotation() 
{
return (String)get_Value("ExpectationQuotation");
}
/** Set Expected result.
@param Expectedresult Expected result */
public void setExpectedresult (String Expectedresult)
{
if (Expectedresult != null && Expectedresult.length() > 1024)
{
log.warning("Length > 1024 - truncated");
Expectedresult = Expectedresult.substring(0,1023);
}
set_Value ("Expectedresult", Expectedresult);
}
/** Get Expected result.
@return Expected result */
public String getExpectedresult() 
{
return (String)get_Value("Expectedresult");
}
/** Set Work performance evaluation.
@param P_ER_Evaluation_ID Work performance evaluation */
public void setP_ER_Evaluation_ID (int P_ER_Evaluation_ID)
{
if (P_ER_Evaluation_ID < 1) throw new IllegalArgumentException ("P_ER_Evaluation_ID is mandatory.");
set_Value ("P_ER_Evaluation_ID", new Integer(P_ER_Evaluation_ID));
}
/** Get Work performance evaluation.
@return Work performance evaluation */
public int getP_ER_Evaluation_ID() 
{
Integer ii = (Integer)get_Value("P_ER_Evaluation_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_ER_Expectation_ID.
@param P_ER_Expectation_ID P_ER_Expectation_ID */
public void setP_ER_Expectation_ID (int P_ER_Expectation_ID)
{
if (P_ER_Expectation_ID < 1) throw new IllegalArgumentException ("P_ER_Expectation_ID is mandatory.");
set_ValueNoCheck ("P_ER_Expectation_ID", new Integer(P_ER_Expectation_ID));
}
/** Get P_ER_Expectation_ID.
@return P_ER_Expectation_ID */
public int getP_ER_Expectation_ID() 
{
Integer ii = (Integer)get_Value("P_ER_Expectation_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set PNumber.
@param PNumber PNumber */
public void setPNumber (int PNumber)
{
set_Value ("PNumber", new Integer(PNumber));
}
/** Get PNumber.
@return PNumber */
public int getPNumber() 
{
Integer ii = (Integer)get_Value("PNumber");
if (ii == null) return 0;
return ii.intValue();
}
}
