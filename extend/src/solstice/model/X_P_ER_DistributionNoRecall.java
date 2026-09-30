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
/** Generated Model for P_ER_DistributionNoRecall
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_ER_DistributionNoRecall extends PO
{
/** Standard Constructor
@param ctx context
@param P_ER_DistributionNoRecall_ID id
@param trxName transaction
*/
public X_P_ER_DistributionNoRecall (Properties ctx, int P_ER_DistributionNoRecall_ID, String trxName)
{
super (ctx, P_ER_DistributionNoRecall_ID, trxName);
/** if (P_ER_DistributionNoRecall_ID == 0)
{
setP_Distribution_Booklet_ID (0);
setP_ER_DISTRIBUTIONNORECALL_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_ER_DistributionNoRecall (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100838 */
public static final int Table_ID=2100838;

/** TableName=P_ER_DistributionNoRecall */
public static final String Table_Name="P_ER_DistributionNoRecall";

protected static KeyNamePair Model = new KeyNamePair(2100838,"P_ER_DistributionNoRecall");

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
StringBuffer sb = new StringBuffer ("X_P_ER_DistributionNoRecall[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Booklet.
@param P_Distribution_Booklet_ID Booklet */
public void setP_Distribution_Booklet_ID (int P_Distribution_Booklet_ID)
{
if (P_Distribution_Booklet_ID < 1) throw new IllegalArgumentException ("P_Distribution_Booklet_ID is mandatory.");
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
/** Set P_ER_DISTRIBUTIONNORECALL_ID.
@param P_ER_DISTRIBUTIONNORECALL_ID P_ER_DISTRIBUTIONNORECALL_ID */
public void setP_ER_DISTRIBUTIONNORECALL_ID (int P_ER_DISTRIBUTIONNORECALL_ID)
{
if (P_ER_DISTRIBUTIONNORECALL_ID < 1) throw new IllegalArgumentException ("P_ER_DISTRIBUTIONNORECALL_ID is mandatory.");
set_ValueNoCheck ("P_ER_DISTRIBUTIONNORECALL_ID", new Integer(P_ER_DISTRIBUTIONNORECALL_ID));
}
/** Get P_ER_DISTRIBUTIONNORECALL_ID.
@return P_ER_DISTRIBUTIONNORECALL_ID */
public int getP_ER_DISTRIBUTIONNORECALL_ID() 
{
Integer ii = (Integer)get_Value("P_ER_DISTRIBUTIONNORECALL_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
