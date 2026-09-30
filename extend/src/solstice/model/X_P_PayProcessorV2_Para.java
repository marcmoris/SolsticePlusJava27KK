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
/** Generated Model for P_PayProcessorV2_Para
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_PayProcessorV2_Para extends PO
{
/** Standard Constructor
@param ctx context
@param P_PayProcessorV2_Para_ID id
@param trxName transaction
*/
public X_P_PayProcessorV2_Para (Properties ctx, int P_PayProcessorV2_Para_ID, String trxName)
{
super (ctx, P_PayProcessorV2_Para_ID, trxName);
/** if (P_PayProcessorV2_Para_ID == 0)
{
setAD_PInstance_ID (0);
setP_PayProcessorV2_ID (0);
setP_PayProcessorV2_Para_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_PayProcessorV2_Para (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201176 */
public static final int Table_ID=3201176;

/** TableName=P_PayProcessorV2_Para */
public static final String Table_Name="P_PayProcessorV2_Para";

protected static KeyNamePair Model = new KeyNamePair(3201176,"P_PayProcessorV2_Para");

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
StringBuffer sb = new StringBuffer ("X_P_PayProcessorV2_Para[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Process Instance.
@param AD_PInstance_ID Instance of the process */
public void setAD_PInstance_ID (int AD_PInstance_ID)
{
if (AD_PInstance_ID < 1) throw new IllegalArgumentException ("AD_PInstance_ID is mandatory.");
set_Value ("AD_PInstance_ID", new Integer(AD_PInstance_ID));
}
/** Get Process Instance.
@return Instance of the process */
public int getAD_PInstance_ID() 
{
Integer ii = (Integer)get_Value("AD_PInstance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Process.
@param AD_Process_ID Process or Report */
public void setAD_Process_ID (int AD_Process_ID)
{
if (AD_Process_ID <= 0) set_Value ("AD_Process_ID", null);
 else 
set_Value ("AD_Process_ID", new Integer(AD_Process_ID));
}
/** Get Process.
@return Process or Report */
public int getAD_Process_ID() 
{
Integer ii = (Integer)get_Value("AD_Process_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_PayProcessorV2_ID.
@param P_PayProcessorV2_ID P_PayProcessorV2_ID */
public void setP_PayProcessorV2_ID (int P_PayProcessorV2_ID)
{
if (P_PayProcessorV2_ID < 1) throw new IllegalArgumentException ("P_PayProcessorV2_ID is mandatory.");
set_Value ("P_PayProcessorV2_ID", new Integer(P_PayProcessorV2_ID));
}
/** Get P_PayProcessorV2_ID.
@return P_PayProcessorV2_ID */
public int getP_PayProcessorV2_ID() 
{
Integer ii = (Integer)get_Value("P_PayProcessorV2_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_PayProcessorV2_Para_ID.
@param P_PayProcessorV2_Para_ID P_PayProcessorV2_Para_ID */
public void setP_PayProcessorV2_Para_ID (int P_PayProcessorV2_Para_ID)
{
if (P_PayProcessorV2_Para_ID < 1) throw new IllegalArgumentException ("P_PayProcessorV2_Para_ID is mandatory.");
set_ValueNoCheck ("P_PayProcessorV2_Para_ID", new Integer(P_PayProcessorV2_Para_ID));
}
/** Get P_PayProcessorV2_Para_ID.
@return P_PayProcessorV2_Para_ID */
public int getP_PayProcessorV2_Para_ID() 
{
Integer ii = (Integer)get_Value("P_PayProcessorV2_Para_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Title.
@param Title Name this entity is referred to as */
public void setTitle (String Title)
{
if (Title != null && Title.length() > 60)
{
log.warning("Length > 60 - truncated");
Title = Title.substring(0,59);
}
set_Value ("Title", Title);
}
/** Get Title.
@return Name this entity is referred to as */
public String getTitle() 
{
return (String)get_Value("Title");
}
}
