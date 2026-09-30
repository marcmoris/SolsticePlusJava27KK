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
/** Generated Model for P_Tools_Credits
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Tools_Credits extends PO
{
/** Standard Constructor
@param ctx context
@param P_Tools_Credits_ID id
@param trxName transaction
*/
public X_P_Tools_Credits (Properties ctx, int P_Tools_Credits_ID, String trxName)
{
super (ctx, P_Tools_Credits_ID, trxName);
/** if (P_Tools_Credits_ID == 0)
{
setCredits_Dst_ID (0);
setCredits_Src_ID (0);
setP_Tools_Credits_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Tools_Credits (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100825 */
public static final int Table_ID=2100825;

/** TableName=P_Tools_Credits */
public static final String Table_Name="P_Tools_Credits";

protected static KeyNamePair Model = new KeyNamePair(2100825,"P_Tools_Credits");

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
StringBuffer sb = new StringBuffer ("X_P_Tools_Credits[").append(get_ID()).append("]");
return sb.toString();
}

/** Credits_Dst_ID AD_Reference_ID=2100318 */
public static final int CREDITS_DST_ID_AD_Reference_ID=2100318;
/** Set Banque destination.
@param Credits_Dst_ID Banque destination */
public void setCredits_Dst_ID (int Credits_Dst_ID)
{
if (Credits_Dst_ID < 1) throw new IllegalArgumentException ("Credits_Dst_ID is mandatory.");
set_Value ("Credits_Dst_ID", new Integer(Credits_Dst_ID));
}
/** Get Banque destination.
@return Banque destination */
public int getCredits_Dst_ID() 
{
Integer ii = (Integer)get_Value("Credits_Dst_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** Credits_Src_ID AD_Reference_ID=2100318 */
public static final int CREDITS_SRC_ID_AD_Reference_ID=2100318;
/** Set Banque source.
@param Credits_Src_ID Banque source */
public void setCredits_Src_ID (int Credits_Src_ID)
{
if (Credits_Src_ID < 1) throw new IllegalArgumentException ("Credits_Src_ID is mandatory.");
set_Value ("Credits_Src_ID", new Integer(Credits_Src_ID));
}
/** Get Banque source.
@return Banque source */
public int getCredits_Src_ID() 
{
Integer ii = (Integer)get_Value("Credits_Src_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_ID AD_Reference_ID=2100317 */
public static final int P_GAIN_ID_AD_Reference_ID=2100317;
/** Set Gain.
@param P_Gain_ID Gain */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID <= 0) set_Value ("P_Gain_ID", null);
 else 
set_Value ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Gain.
@return Gain */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Tools_Credits_ID.
@param P_Tools_Credits_ID P_Tools_Credits_ID */
public void setP_Tools_Credits_ID (int P_Tools_Credits_ID)
{
if (P_Tools_Credits_ID < 1) throw new IllegalArgumentException ("P_Tools_Credits_ID is mandatory.");
set_ValueNoCheck ("P_Tools_Credits_ID", new Integer(P_Tools_Credits_ID));
}
/** Get P_Tools_Credits_ID.
@return P_Tools_Credits_ID */
public int getP_Tools_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Tools_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set Date du transfert.
@param TransfertDate Date du transfert */
public void setTransfertDate (Timestamp TransfertDate)
{
set_Value ("TransfertDate", TransfertDate);
}
/** Get Date du transfert.
@return Date du transfert */
public Timestamp getTransfertDate() 
{
return (Timestamp)get_Value("TransfertDate");
}
/** Set TransfertToParticularSheet.
@param TransfertToParticularSheet TransfertToParticularSheet */
public void setTransfertToParticularSheet (String TransfertToParticularSheet)
{
if (TransfertToParticularSheet != null && TransfertToParticularSheet.length() > 1)
{
log.warning("Length > 1 - truncated");
TransfertToParticularSheet = TransfertToParticularSheet.substring(0,0);
}
set_Value ("TransfertToParticularSheet", TransfertToParticularSheet);
}
/** Get TransfertToParticularSheet.
@return TransfertToParticularSheet */
public String getTransfertToParticularSheet() 
{
return (String)get_Value("TransfertToParticularSheet");
}
}
