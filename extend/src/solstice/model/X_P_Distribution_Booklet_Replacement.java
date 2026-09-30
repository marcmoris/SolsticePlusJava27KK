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
/** Generated Model for P_Distribution_Booklet_Replacement
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_P_Distribution_Booklet_Replacement.java,v 1.1 2008/11/24 18:36:38 marmor01 Exp $ */
public class X_P_Distribution_Booklet_Replacement extends PO
{
/** Standard Constructor
@param ctx context
@param P_Distribution_Booklet_Replacement_ID id
@param trxName transaction
*/
public X_P_Distribution_Booklet_Replacement (Properties ctx, int P_Distribution_Booklet_Replacement_ID, String trxName)
{
super (ctx, P_Distribution_Booklet_Replacement_ID, trxName);
/** if (P_Distribution_Booklet_Replacement_ID == 0)
{
setP_Employee_ID (0);
setReplacementType (null);
setStartDate (new Timestamp(System.currentTimeMillis()));
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Distribution_Booklet_Replacement (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100905 */
public static final int Table_ID=2100905;

/** TableName=P_Distribution_Booklet_Replacement */
public static final String Table_Name="P_Distribution_Booklet_Replacement";

protected static KeyNamePair Model = new KeyNamePair(2100905,"P_Distribution_Booklet_Replacement");

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
StringBuffer sb = new StringBuffer ("X_P_Distribution_Booklet_Replacement[").append(get_ID()).append("]");
return sb.toString();
}
/** Set End Date.
@param EndDate Last effective date (inclusive) */
public void setEndDate (Timestamp EndDate)
{
set_Value ("EndDate", EndDate);
}
/** Get End Date.
@return Last effective date (inclusive) */
public Timestamp getEndDate() 
{
return (Timestamp)get_Value("EndDate");
}
/** Set Booklet.
@param P_Distribution_Booklet_ID Booklet */
public void setP_Distribution_Booklet_ID (int P_Distribution_Booklet_ID)
{
if (P_Distribution_Booklet_ID <= 0) set_Value ("P_Distribution_Booklet_ID", null);
 else 
set_Value ("P_Distribution_Booklet_ID", new Integer(P_Distribution_Booklet_ID));
}
/** Get Booklet.
@return Booklet */
public int getP_Distribution_Booklet_ID() 
{
Integer ii = (Integer)get_Value("P_Distribution_Booklet_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_DISTRIBUTION_BOOKLET_REPLACEMENT_ID.
@param P_DISTRIBUTION_BOOKLET_REPLACEMENT_ID P_DISTRIBUTION_BOOKLET_REPLACEMENT_ID */
public void setP_DISTRIBUTION_BOOKLET_REPLACEMENT_ID (int P_DISTRIBUTION_BOOKLET_REPLACEMENT_ID)
{
if (P_DISTRIBUTION_BOOKLET_REPLACEMENT_ID <= 0) set_ValueNoCheck ("P_DISTRIBUTION_BOOKLET_REPLACEMENT_ID", null);
 else 
set_ValueNoCheck ("P_DISTRIBUTION_BOOKLET_REPLACEMENT_ID", new Integer(P_DISTRIBUTION_BOOKLET_REPLACEMENT_ID));
}
/** Get P_DISTRIBUTION_BOOKLET_REPLACEMENT_ID.
@return P_DISTRIBUTION_BOOKLET_REPLACEMENT_ID */
public int getP_DISTRIBUTION_BOOKLET_REPLACEMENT_ID() 
{
Integer ii = (Integer)get_Value("P_DISTRIBUTION_BOOKLET_REPLACEMENT_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
set_Value ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee .
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** ReplacementType AD_Reference_ID=2100478 */
public static final int REPLACEMENTTYPE_AD_Reference_ID=2100478;
/** Gestionnaire = G */
public static final String REPLACEMENTTYPE_Gestionnaire = "G";
/** Secrétaire = S */
public static final String REPLACEMENTTYPE_Secrétaire = "S";
/** Coordonnateur = C */
public static final String REPLACEMENTTYPE_Coordonnateur = "C";
/** Signataire = N */
public static final String REPLACEMENTTYPE_Signataire = "N";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isReplacementTypeValid (String test)
{
return test.equals("G") || test.equals("S") || test.equals("C") || test.equals("N");
}
/** Set Type.
@param ReplacementType Type */
public void setReplacementType (String ReplacementType)
{
if (ReplacementType == null) throw new IllegalArgumentException ("ReplacementType is mandatory");
if (!isReplacementTypeValid(ReplacementType))
throw new IllegalArgumentException ("ReplacementType Invalid value - " + ReplacementType + " - Reference_ID=2100478 - G - S - C - N");
if (ReplacementType.length() > 1)
{
log.warning("Length > 1 - truncated");
ReplacementType = ReplacementType.substring(0,0);
}
set_Value ("ReplacementType", ReplacementType);
}
/** Get Type.
@return Type */
public String getReplacementType() 
{
return (String)get_Value("ReplacementType");
}
/** Set Start Date.
@param StartDate First effective day (inclusive) */
public void setStartDate (Timestamp StartDate)
{
if (StartDate == null) throw new IllegalArgumentException ("StartDate is mandatory.");
set_Value ("StartDate", StartDate);
}
/** Get Start Date.
@return First effective day (inclusive) */
public Timestamp getStartDate() 
{
return (Timestamp)get_Value("StartDate");
}
}
