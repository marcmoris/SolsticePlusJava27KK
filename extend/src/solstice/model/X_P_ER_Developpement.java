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
/** Generated Model for P_ER_Developpement
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_ER_Developpement extends PO
{
/** Standard Constructor
@param ctx context
@param P_ER_Developpement_ID id
@param trxName transaction
*/
public X_P_ER_Developpement (Properties ctx, int P_ER_Developpement_ID, String trxName)
{
super (ctx, P_ER_Developpement_ID, trxName);
/** if (P_ER_Developpement_ID == 0)
{
setP_ER_Developpement_ID (0);
setP_ER_Evaluation_ID (0);
setPNumber (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_ER_Developpement (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100725 */
public static final int Table_ID=2100725;

/** TableName=P_ER_Developpement */
public static final String Table_Name="P_ER_Developpement";

protected static KeyNamePair Model = new KeyNamePair(2100725,"P_ER_Developpement");

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
StringBuffer sb = new StringBuffer ("X_P_ER_Developpement[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Action.
@param Action Indicates the Action to be performed */
public void setAction (String Action)
{
if (Action != null && Action.length() > 255)
{
log.warning("Length > 255 - truncated");
Action = Action.substring(0,254);
}
set_Value ("Action", Action);
}
/** Get Action.
@return Indicates the Action to be performed */
public String getAction() 
{
return (String)get_Value("Action");
}
/** Set Due Date.
@param DueDate Date when the payment is due */
public void setDueDate (Timestamp DueDate)
{
set_Value ("DueDate", DueDate);
}
/** Get Due Date.
@return Date when the payment is due */
public Timestamp getDueDate() 
{
return (Timestamp)get_Value("DueDate");
}
/** Set Improvement.
@param Improvement Improvement */
public void setImprovement (String Improvement)
{
if (Improvement != null && Improvement.length() > 255)
{
log.warning("Length > 255 - truncated");
Improvement = Improvement.substring(0,254);
}
set_Value ("Improvement", Improvement);
}
/** Get Improvement.
@return Improvement */
public String getImprovement() 
{
return (String)get_Value("Improvement");
}
/** Set Developpement.
@param P_ER_Developpement_ID Developpement */
public void setP_ER_Developpement_ID (int P_ER_Developpement_ID)
{
if (P_ER_Developpement_ID < 1) throw new IllegalArgumentException ("P_ER_Developpement_ID is mandatory.");
set_ValueNoCheck ("P_ER_Developpement_ID", new Integer(P_ER_Developpement_ID));
}
/** Get Developpement.
@return Developpement */
public int getP_ER_Developpement_ID() 
{
Integer ii = (Integer)get_Value("P_ER_Developpement_ID");
if (ii == null) return 0;
return ii.intValue();
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
