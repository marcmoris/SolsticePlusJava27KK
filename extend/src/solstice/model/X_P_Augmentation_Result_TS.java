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
/** Generated Model for P_Augmentation_Result_TS
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Augmentation_Result_TS extends PO
{
/** Standard Constructor
@param ctx context
@param P_Augmentation_Result_TS_ID id
@param trxName transaction
*/
public X_P_Augmentation_Result_TS (Properties ctx, int P_Augmentation_Result_TS_ID, String trxName)
{
super (ctx, P_Augmentation_Result_TS_ID, trxName);
/** if (P_Augmentation_Result_TS_ID == 0)
{
setP_Augmentation_Result_ID (0);
setP_Augmentation_Result_TS_ID (0);
setP_Time_Sheet_Detail_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Augmentation_Result_TS (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100802 */
public static final int Table_ID=2100802;

/** TableName=P_Augmentation_Result_TS */
public static final String Table_Name="P_Augmentation_Result_TS";

protected static KeyNamePair Model = new KeyNamePair(2100802,"P_Augmentation_Result_TS");

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
StringBuffer sb = new StringBuffer ("X_P_Augmentation_Result_TS[").append(get_ID()).append("]");
return sb.toString();
}
/** Set P_Augmentation_Result_ID.
@param P_Augmentation_Result_ID P_Augmentation_Result_ID */
public void setP_Augmentation_Result_ID (int P_Augmentation_Result_ID)
{
if (P_Augmentation_Result_ID < 1) throw new IllegalArgumentException ("P_Augmentation_Result_ID is mandatory.");
set_Value ("P_Augmentation_Result_ID", new Integer(P_Augmentation_Result_ID));
}
/** Get P_Augmentation_Result_ID.
@return P_Augmentation_Result_ID */
public int getP_Augmentation_Result_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_Result_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Augmentation_Result_TS_ID.
@param P_Augmentation_Result_TS_ID P_Augmentation_Result_TS_ID */
public void setP_Augmentation_Result_TS_ID (int P_Augmentation_Result_TS_ID)
{
if (P_Augmentation_Result_TS_ID < 1) throw new IllegalArgumentException ("P_Augmentation_Result_TS_ID is mandatory.");
set_ValueNoCheck ("P_Augmentation_Result_TS_ID", new Integer(P_Augmentation_Result_TS_ID));
}
/** Get P_Augmentation_Result_TS_ID.
@return P_Augmentation_Result_TS_ID */
public int getP_Augmentation_Result_TS_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_Result_TS_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Feuille Detail.
@param P_Time_Sheet_Detail_ID   */
public void setP_Time_Sheet_Detail_ID (int P_Time_Sheet_Detail_ID)
{
if (P_Time_Sheet_Detail_ID < 1) throw new IllegalArgumentException ("P_Time_Sheet_Detail_ID is mandatory.");
set_Value ("P_Time_Sheet_Detail_ID", new Integer(P_Time_Sheet_Detail_ID));
}
/** Get Feuille Detail.
@return   */
public int getP_Time_Sheet_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Time_Sheet_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Year_ID AD_Reference_ID=2000012 */
public static final int P_YEAR_ID_AD_Reference_ID=2000012;
/** Set Year.
@param P_Year_ID Calendar Year */
public void setP_Year_ID (int P_Year_ID)
{
if (P_Year_ID <= 0) set_Value ("P_Year_ID", null);
 else 
set_Value ("P_Year_ID", new Integer(P_Year_ID));
}
/** Get Year.
@return Calendar Year */
public int getP_Year_ID() 
{
Integer ii = (Integer)get_Value("P_Year_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
