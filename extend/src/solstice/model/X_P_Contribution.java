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
/** Generated Model for P_Contribution
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Contribution extends PO
{
/** Standard Constructor
@param ctx context
@param P_Contribution_ID id
@param trxName transaction
*/
public X_P_Contribution (Properties ctx, int P_Contribution_ID, String trxName)
{
super (ctx, P_Contribution_ID, trxName);
/** if (P_Contribution_ID == 0)
{
setP_Contribution_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Contribution (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100779 */
public static final int Table_ID=2100779;

/** TableName=P_Contribution */
public static final String Table_Name="P_Contribution";

protected static KeyNamePair Model = new KeyNamePair(2100779,"P_Contribution");

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
StringBuffer sb = new StringBuffer ("X_P_Contribution[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Account Element.
@param C_ElementValue_ID Account Element */
public void setC_ElementValue_ID (int C_ElementValue_ID)
{
if (C_ElementValue_ID <= 0) set_Value ("C_ElementValue_ID", null);
 else 
set_Value ("C_ElementValue_ID", new Integer(C_ElementValue_ID));
}
/** Get Account Element.
@return Account Element */
public int getC_ElementValue_ID() 
{
Integer ii = (Integer)get_Value("C_ElementValue_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ISASSURANCE.
@param ISASSURANCE ISASSURANCE */
public void setISASSURANCE (boolean ISASSURANCE)
{
set_Value ("ISASSURANCE", new Boolean(ISASSURANCE));
}
/** Get ISASSURANCE.
@return ISASSURANCE */
public boolean isASSURANCE() 
{
Object oo = get_Value("ISASSURANCE");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Federal.
@param IsFederal Federal */
public void setIsFederal (boolean IsFederal)
{
set_Value ("IsFederal", new Boolean(IsFederal));
}
/** Get Federal.
@return Federal */
public boolean isFederal() 
{
Object oo = get_Value("IsFederal");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsGouvernement.
@param IsGouvernement IsGouvernement */
public void setIsGouvernement (boolean IsGouvernement)
{
set_Value ("IsGouvernement", new Boolean(IsGouvernement));
}
/** Get IsGouvernement.
@return IsGouvernement */
public boolean isGouvernement() 
{
Object oo = get_Value("IsGouvernement");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set ISOTHER.
@param ISOTHER ISOTHER */
public void setISOTHER (boolean ISOTHER)
{
set_Value ("ISOTHER", new Boolean(ISOTHER));
}
/** Get ISOTHER.
@return ISOTHER */
public boolean isOTHER() 
{
Object oo = get_Value("ISOTHER");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set WCB.
@param IsWCB WCB */
public void setIsWCB (boolean IsWCB)
{
set_Value ("IsWCB", new Boolean(IsWCB));
}
/** Get WCB.
@return WCB */
public boolean isWCB() 
{
Object oo = get_Value("IsWCB");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Contribution Parameter.
@param P_Contribution_ID Contribution Parameter */
public void setP_Contribution_ID (int P_Contribution_ID)
{
if (P_Contribution_ID < 1) throw new IllegalArgumentException ("P_Contribution_ID is mandatory.");
set_ValueNoCheck ("P_Contribution_ID", new Integer(P_Contribution_ID));
}
/** Get Contribution Parameter.
@return Contribution Parameter */
public int getP_Contribution_ID() 
{
Integer ii = (Integer)get_Value("P_Contribution_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
