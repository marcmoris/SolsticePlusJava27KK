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
/** Generated Model for P_ER_Competence
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_ER_Competence extends PO
{
/** Standard Constructor
@param ctx context
@param P_ER_Competence_ID id
@param trxName transaction
*/
public X_P_ER_Competence (Properties ctx, int P_ER_Competence_ID, String trxName)
{
super (ctx, P_ER_Competence_ID, trxName);
/** if (P_ER_Competence_ID == 0)
{
setP_ER_Competence_ID (0);
setPNumber (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_ER_Competence (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100723 */
public static final int Table_ID=2100723;

/** TableName=P_ER_Competence */
public static final String Table_Name="P_ER_Competence";

protected static KeyNamePair Model = new KeyNamePair(2100723,"P_ER_Competence");

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
StringBuffer sb = new StringBuffer ("X_P_ER_Competence[").append(get_ID()).append("]");
return sb.toString();
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
/** Set Competence.
@param P_ER_Competence_ID Competence */
public void setP_ER_Competence_ID (int P_ER_Competence_ID)
{
if (P_ER_Competence_ID < 1) throw new IllegalArgumentException ("P_ER_Competence_ID is mandatory.");
set_ValueNoCheck ("P_ER_Competence_ID", new Integer(P_ER_Competence_ID));
}
/** Get Competence.
@return Competence */
public int getP_ER_Competence_ID() 
{
Integer ii = (Integer)get_Value("P_ER_Competence_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Work performance evaluation.
@param P_ER_Evaluation_ID Work performance evaluation */
public void setP_ER_Evaluation_ID (int P_ER_Evaluation_ID)
{
if (P_ER_Evaluation_ID <= 0) set_Value ("P_ER_Evaluation_ID", null);
 else 
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
/** Set Quotation.
@param Quotation Quotation */
public void setQuotation (String Quotation)
{
if (Quotation != null && Quotation.length() > 3)
{
log.warning("Length > 3 - truncated");
Quotation = Quotation.substring(0,2);
}
set_Value ("Quotation", Quotation);
}
/** Get Quotation.
@return Quotation */
public String getQuotation() 
{
return (String)get_Value("Quotation");
}
}
