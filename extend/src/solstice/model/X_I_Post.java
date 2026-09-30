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
/** Generated Model for I_Post
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_I_Post.java,v 1.1 2007/07/18 14:43:29 marmor01 Exp $ */
public class X_I_Post extends PO
{
/** Standard Constructor
@param ctx context
@param I_Post_ID id
@param trxName transaction
*/
public X_I_Post (Properties ctx, int I_Post_ID, String trxName)
{
super (ctx, I_Post_ID, trxName);
/** if (I_Post_ID == 0)
{
setI_IsImported (false);
setI_Post_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_I_Post (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100846 */
public static final int Table_ID=2100846;

/** TableName=I_Post */
public static final String Table_Name="I_Post";

protected static KeyNamePair Model = new KeyNamePair(2100846,"I_Post");

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
StringBuffer sb = new StringBuffer ("X_I_Post[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Import Error Message.
@param I_ErrorMsg Messages generated from import process */
public void setI_ErrorMsg (String I_ErrorMsg)
{
if (I_ErrorMsg != null && I_ErrorMsg.length() > 2000)
{
log.warning("Length > 2000 - truncated");
I_ErrorMsg = I_ErrorMsg.substring(0,1999);
}
set_Value ("I_ErrorMsg", I_ErrorMsg);
}
/** Get Import Error Message.
@return Messages generated from import process */
public String getI_ErrorMsg() 
{
return (String)get_Value("I_ErrorMsg");
}
/** Set Imported.
@param I_IsImported Has this import been processed */
public void setI_IsImported (boolean I_IsImported)
{
set_Value ("I_IsImported", new Boolean(I_IsImported));
}
/** Get Imported.
@return Has this import been processed */
public boolean isI_IsImported() 
{
Object oo = get_Value("I_IsImported");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set I_Post_ID.
@param I_Post_ID I_Post_ID */
public void setI_Post_ID (int I_Post_ID)
{
if (I_Post_ID < 1) throw new IllegalArgumentException ("I_Post_ID is mandatory.");
set_ValueNoCheck ("I_Post_ID", new Integer(I_Post_ID));
}
/** Get I_Post_ID.
@return I_Post_ID */
public int getI_Post_ID() 
{
Integer ii = (Integer)get_Value("I_Post_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set name_en.
@param name_en name_en */
public void setname_en (String name_en)
{
if (name_en != null && name_en.length() > 60)
{
log.warning("Length > 60 - truncated");
name_en = name_en.substring(0,59);
}
set_Value ("name_en", name_en);
}
/** Get name_en.
@return name_en */
public String getname_en() 
{
return (String)get_Value("name_en");
}
/** Set name_fr.
@param name_fr name_fr */
public void setname_fr (String name_fr)
{
if (name_fr != null && name_fr.length() > 60)
{
log.warning("Length > 60 - truncated");
name_fr = name_fr.substring(0,59);
}
set_Value ("name_fr", name_fr);
}
/** Get name_fr.
@return name_fr */
public String getname_fr() 
{
return (String)get_Value("name_fr");
}
/** Set Processed.
@param Processed The document has been processed */
public void setProcessed (boolean Processed)
{
set_Value ("Processed", new Boolean(Processed));
}
/** Get Processed.
@return The document has been processed */
public boolean isProcessed() 
{
Object oo = get_Value("Processed");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Process Now.
@param Processing Process Now */
public void setProcessing (boolean Processing)
{
set_Value ("Processing", new Boolean(Processing));
}
/** Get Process Now.
@return Process Now */
public boolean isProcessing() 
{
Object oo = get_Value("Processing");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value != null && Value.length() > 30)
{
log.warning("Length > 30 - truncated");
Value = Value.substring(0,29);
}
set_Value ("Value", Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
}
