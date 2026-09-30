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
/** Generated Model for P_Absence_Detail
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Absence_Detail extends PO
{
/** Standard Constructor
@param ctx context
@param P_Absence_Detail_ID id
@param trxName transaction
*/
public X_P_Absence_Detail (Properties ctx, int P_Absence_Detail_ID, String trxName)
{
super (ctx, P_Absence_Detail_ID, trxName);
/** if (P_Absence_Detail_ID == 0)
{
setAbsenceDate (new Timestamp(System.currentTimeMillis()));
setP_Absence_Detail_ID (0);
setP_Absence_ID (0);
setSource (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Absence_Detail (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100760 */
public static final int Table_ID=2100760;

/** TableName=P_Absence_Detail */
public static final String Table_Name="P_Absence_Detail";

protected static KeyNamePair Model = new KeyNamePair(2100760,"P_Absence_Detail");

protected BigDecimal accessLevel = new BigDecimal(4);
/** AccessLevel
@return 4 - System 
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
StringBuffer sb = new StringBuffer ("X_P_Absence_Detail[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Date de l'absence.
@param AbsenceDate Date de l'absence */
public void setAbsenceDate (Timestamp AbsenceDate)
{
if (AbsenceDate == null) throw new IllegalArgumentException ("AbsenceDate is mandatory.");
set_Value ("AbsenceDate", AbsenceDate);
}
/** Get Date de l'absence.
@return Date de l'absence */
public Timestamp getAbsenceDate() 
{
return (Timestamp)get_Value("AbsenceDate");
}
/** Set Begining_Hour.
@param Begining_Hour Begining_Hour */
public void setBegining_Hour (String Begining_Hour)
{
if (Begining_Hour != null && Begining_Hour.length() > 5)
{
log.warning("Length > 5 - truncated");
Begining_Hour = Begining_Hour.substring(0,4);
}
set_Value ("Begining_Hour", Begining_Hour);
}
/** Get Begining_Hour.
@return Begining_Hour */
public String getBegining_Hour() 
{
return (String)get_Value("Begining_Hour");
}
/** Set Duration.
@param Duration Normal Duration in Duration Unit */
public void setDuration (BigDecimal Duration)
{
set_Value ("Duration", Duration);
}
/** Get Duration.
@return Normal Duration in Duration Unit */
public BigDecimal getDuration() 
{
BigDecimal bd = (BigDecimal)get_Value("Duration");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Ending_Hour.
@param Ending_Hour Ending_Hour */
public void setEnding_Hour (String Ending_Hour)
{
if (Ending_Hour != null && Ending_Hour.length() > 5)
{
log.warning("Length > 5 - truncated");
Ending_Hour = Ending_Hour.substring(0,4);
}
set_Value ("Ending_Hour", Ending_Hour);
}
/** Get Ending_Hour.
@return Ending_Hour */
public String getEnding_Hour() 
{
return (String)get_Value("Ending_Hour");
}
/** Set P_Absence_Detail_ID.
@param P_Absence_Detail_ID P_Absence_Detail_ID */
public void setP_Absence_Detail_ID (int P_Absence_Detail_ID)
{
if (P_Absence_Detail_ID < 1) throw new IllegalArgumentException ("P_Absence_Detail_ID is mandatory.");
set_ValueNoCheck ("P_Absence_Detail_ID", new Integer(P_Absence_Detail_ID));
}
/** Get P_Absence_Detail_ID.
@return P_Absence_Detail_ID */
public int getP_Absence_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Absence_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Absence.
@param P_Absence_ID Absence */
public void setP_Absence_ID (int P_Absence_ID)
{
if (P_Absence_ID < 1) throw new IllegalArgumentException ("P_Absence_ID is mandatory.");
set_Value ("P_Absence_ID", new Integer(P_Absence_ID));
}
/** Get Absence.
@return Absence */
public int getP_Absence_ID() 
{
Integer ii = (Integer)get_Value("P_Absence_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Period.
@param P_Period_ID Period of the Calendar   */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID <= 0) set_Value ("P_Period_ID", null);
 else 
set_Value ("P_Period_ID", new Integer(P_Period_ID));
}
/** Get Period.
@return Period of the Calendar   */
public int getP_Period_ID() 
{
Integer ii = (Integer)get_Value("P_Period_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set Source.
@param Source Source */
public void setSource (String Source)
{
if (Source == null) throw new IllegalArgumentException ("Source is mandatory.");
if (Source.length() > 1)
{
log.warning("Length > 1 - truncated");
Source = Source.substring(0,0);
}
set_Value ("Source", Source);
}
/** Get Source.
@return Source */
public String getSource() 
{
return (String)get_Value("Source");
}
/** Set Source_ID.
@param Source_ID Source_ID */
public void setSource_ID (int Source_ID)
{
if (Source_ID <= 0) set_Value ("Source_ID", null);
 else 
set_Value ("Source_ID", new Integer(Source_ID));
}
/** Get Source_ID.
@return Source_ID */
public int getSource_ID() 
{
Integer ii = (Integer)get_Value("Source_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
