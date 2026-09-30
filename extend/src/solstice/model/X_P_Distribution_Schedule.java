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
/** Generated Model for P_Distribution_Schedule
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Distribution_Schedule extends PO
{
/** Standard Constructor
@param ctx context
@param P_Distribution_Schedule_ID id
@param trxName transaction
*/
public X_P_Distribution_Schedule (Properties ctx, int P_Distribution_Schedule_ID, String trxName)
{
super (ctx, P_Distribution_Schedule_ID, trxName);
/** if (P_Distribution_Schedule_ID == 0)
{
setP_Distribution_ID (0);
setP_DISTRIBUTION_SCHEDULE_ID (0);
setP_Post_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Distribution_Schedule (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201213 */
public static final int Table_ID=3201213;

/** TableName=P_Distribution_Schedule */
public static final String Table_Name="P_Distribution_Schedule";

protected static KeyNamePair Model = new KeyNamePair(3201213,"P_Distribution_Schedule");

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
StringBuffer sb = new StringBuffer ("X_P_Distribution_Schedule[").append(get_ID()).append("]");
return sb.toString();
}

/** P_Distribution_ID AD_Reference_ID=2000053 */
public static final int P_DISTRIBUTION_ID_AD_Reference_ID=2000053;
/** Set Distribution.
@param P_Distribution_ID Code for the check Distributio */
public void setP_Distribution_ID (int P_Distribution_ID)
{
if (P_Distribution_ID < 1) throw new IllegalArgumentException ("P_Distribution_ID is mandatory.");
set_Value ("P_Distribution_ID", new Integer(P_Distribution_ID));
}
/** Get Distribution.
@return Code for the check Distributio */
public int getP_Distribution_ID() 
{
Integer ii = (Integer)get_Value("P_Distribution_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_DISTRIBUTION_SCHEDULE_ID.
@param P_DISTRIBUTION_SCHEDULE_ID P_DISTRIBUTION_SCHEDULE_ID */
public void setP_DISTRIBUTION_SCHEDULE_ID (int P_DISTRIBUTION_SCHEDULE_ID)
{
if (P_DISTRIBUTION_SCHEDULE_ID < 1) throw new IllegalArgumentException ("P_DISTRIBUTION_SCHEDULE_ID is mandatory.");
set_ValueNoCheck ("P_DISTRIBUTION_SCHEDULE_ID", new Integer(P_DISTRIBUTION_SCHEDULE_ID));
}
/** Get P_DISTRIBUTION_SCHEDULE_ID.
@return P_DISTRIBUTION_SCHEDULE_ID */
public int getP_DISTRIBUTION_SCHEDULE_ID() 
{
Integer ii = (Integer)get_Value("P_DISTRIBUTION_SCHEDULE_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Post_ID AD_Reference_ID=2000004 */
public static final int P_POST_ID_AD_Reference_ID=2000004;
/** Set Post.
@param P_Post_ID Post */
public void setP_Post_ID (int P_Post_ID)
{
if (P_Post_ID < 1) throw new IllegalArgumentException ("P_Post_ID is mandatory.");
set_Value ("P_Post_ID", new Integer(P_Post_ID));
}
/** Get Post.
@return Post */
public int getP_Post_ID() 
{
Integer ii = (Integer)get_Value("P_Post_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Schedule_ID AD_Reference_ID=2000064 */
public static final int P_SCHEDULE_ID_AD_Reference_ID=2000064;
/** Set Schedule.
@param P_Schedule_ID Répartition des heures de travail à l'intérieur d'une période donnée.  */
public void setP_Schedule_ID (int P_Schedule_ID)
{
if (P_Schedule_ID <= 0) set_Value ("P_Schedule_ID", null);
 else 
set_Value ("P_Schedule_ID", new Integer(P_Schedule_ID));
}
/** Get Schedule.
@return Répartition des heures de travail à l'intérieur d'une période donnée.  */
public int getP_Schedule_ID() 
{
Integer ii = (Integer)get_Value("P_Schedule_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
