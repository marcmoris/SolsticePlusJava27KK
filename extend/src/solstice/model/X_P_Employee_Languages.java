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
/** Generated Model for P_Employee_Languages
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.4 2008/03/12 20:02:49 marmor01 Exp $ */
public class X_P_Employee_Languages extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Languages_ID id
@param trxName transaction
*/
public X_P_Employee_Languages (Properties ctx, int P_Employee_Languages_ID, String trxName)
{
super (ctx, P_Employee_Languages_ID, trxName);
/** if (P_Employee_Languages_ID == 0)
{
setCompetency (null);
setFluency (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Languages (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100894 */
public static final int Table_ID=2100894;

/** TableName=P_Employee_Languages */
public static final String Table_Name="P_Employee_Languages";

protected static KeyNamePair Model = new KeyNamePair(2100894,"P_Employee_Languages");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Languages[").append(get_ID()).append("]");
return sb.toString();
}

/** Competency AD_Reference_ID=2100474 */
public static final int COMPETENCY_AD_Reference_ID=2100474;
/** Poor = Poor */
public static final String COMPETENCY_Poor = "Poor";
/** Basic = Basic */
public static final String COMPETENCY_Basic = "Basic";
/** Good = Good */
public static final String COMPETENCY_Good = "Good";
/** Mother Tongue = Mother Tongue */
public static final String COMPETENCY_MotherTongue = "Mother Tongue";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isCompetencyValid (String test)
{
return test.equals("Poor") || test.equals("Basic") || test.equals("Good") || test.equals("Mother Tongue");
}
/** Set Competency.
@param Competency Competency */
public void setCompetency (String Competency)
{
if (Competency == null) throw new IllegalArgumentException ("Competency is mandatory");
if (!isCompetencyValid(Competency))
throw new IllegalArgumentException ("Competency Invalid value - " + Competency + " - Reference_ID=2100474 - Poor - Basic - Good - Mother Tongue");
if (Competency.length() > 30)
{
log.warning("Length > 30 - truncated");
Competency = Competency.substring(0,29);
}
set_Value ("Competency", Competency);
}
/** Get Competency.
@return Competency */
public String getCompetency() 
{
return (String)get_Value("Competency");
}

/** Fluency AD_Reference_ID=2100473 */
public static final int FLUENCY_AD_Reference_ID=2100473;
/** Writing = Writing */
public static final String FLUENCY_Writing = "Writing";
/** Speaking = Speaking */
public static final String FLUENCY_Speaking = "Speaking";
/** Reading = Reading */
public static final String FLUENCY_Reading = "Reading";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isFluencyValid (String test)
{
return test.equals("Writing") || test.equals("Speaking") || test.equals("Reading");
}
/** Set Fluency.
@param Fluency Fluency */
public void setFluency (String Fluency)
{
if (Fluency == null) throw new IllegalArgumentException ("Fluency is mandatory");
if (!isFluencyValid(Fluency))
throw new IllegalArgumentException ("Fluency Invalid value - " + Fluency + " - Reference_ID=2100473 - Writing - Speaking - Reading");
if (Fluency.length() > 30)
{
log.warning("Length > 30 - truncated");
Fluency = Fluency.substring(0,29);
}
set_Value ("Fluency", Fluency);
}
/** Get Fluency.
@return Fluency */
public String getFluency() 
{
return (String)get_Value("Fluency");
}
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID <= 0) set_Value ("P_Employee_ID", null);
 else 
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
/** Set P_Employee_Languages_ID.
@param P_Employee_Languages_ID P_Employee_Languages_ID */
public void setP_Employee_Languages_ID (int P_Employee_Languages_ID)
{
if (P_Employee_Languages_ID <= 0) set_ValueNoCheck ("P_Employee_Languages_ID", null);
 else 
set_ValueNoCheck ("P_Employee_Languages_ID", new Integer(P_Employee_Languages_ID));
}
/** Get P_Employee_Languages_ID.
@return P_Employee_Languages_ID */
public int getP_Employee_Languages_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Languages_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Language.
@param P_Language_ID Language */
public void setP_Language_ID (int P_Language_ID)
{
if (P_Language_ID <= 0) set_Value ("P_Language_ID", null);
 else 
set_Value ("P_Language_ID", new Integer(P_Language_ID));
}
/** Get Language.
@return Language */
public int getP_Language_ID() 
{
Integer ii = (Integer)get_Value("P_Language_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
