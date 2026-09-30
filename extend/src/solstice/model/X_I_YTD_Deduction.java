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
/** Generated Model for I_YTD_Deduction
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.1 2007/07/18 14:43:46 marmor01 Exp $ */
public class X_I_YTD_Deduction extends PO
{
/** Standard Constructor
@param ctx context
@param I_YTD_Deduction_ID id
@param trxName transaction
*/
public X_I_YTD_Deduction (Properties ctx, int I_YTD_Deduction_ID, String trxName)
{
super (ctx, I_YTD_Deduction_ID, trxName);
/** if (I_YTD_Deduction_ID == 0)
{
setI_YTD_Deduction_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_I_YTD_Deduction (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100854 */
public static final int Table_ID=2100854;

/** TableName=I_YTD_Deduction */
public static final String Table_Name="I_YTD_Deduction";

protected static KeyNamePair Model = new KeyNamePair(2100854,"I_YTD_Deduction");

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
StringBuffer sb = new StringBuffer ("X_I_YTD_Deduction[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Crew Cost.
@param ACTIVITY Crew Cost */
public void setACTIVITY (String ACTIVITY)
{
if (ACTIVITY != null && ACTIVITY.length() > 10)
{
log.warning("Length > 10 - truncated");
ACTIVITY = ACTIVITY.substring(0,9);
}
set_Value ("ACTIVITY", ACTIVITY);
}
/** Get Crew Cost.
@return Crew Cost */
public String getACTIVITY() 
{
return (String)get_Value("ACTIVITY");
}
/** Set Activity.
@param C_Activity_ID Business Activity */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
 else 
set_Value ("C_Activity_ID", new Integer(C_Activity_ID));
}
/** Get Activity.
@return Business Activity */
public int getC_Activity_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set CompanyEI.
@param CompanyEI CompanyEI */
public void setCompanyEI (BigDecimal CompanyEI)
{
set_Value ("CompanyEI", CompanyEI);
}
/** Get CompanyEI.
@return CompanyEI */
public BigDecimal getCompanyEI() 
{
BigDecimal bd = (BigDecimal)get_Value("CompanyEI");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn01.
@param Dedn01 Dedn01 */
public void setDedn01 (BigDecimal Dedn01)
{
set_Value ("Dedn01", Dedn01);
}
/** Get Dedn01.
@return Dedn01 */
public BigDecimal getDedn01() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn01");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn02.
@param Dedn02 Dedn02 */
public void setDedn02 (BigDecimal Dedn02)
{
set_Value ("Dedn02", Dedn02);
}
/** Get Dedn02.
@return Dedn02 */
public BigDecimal getDedn02() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn02");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn03.
@param Dedn03 Dedn03 */
public void setDedn03 (BigDecimal Dedn03)
{
set_Value ("Dedn03", Dedn03);
}
/** Get Dedn03.
@return Dedn03 */
public BigDecimal getDedn03() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn03");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn04.
@param Dedn04 Dedn04 */
public void setDedn04 (BigDecimal Dedn04)
{
set_Value ("Dedn04", Dedn04);
}
/** Get Dedn04.
@return Dedn04 */
public BigDecimal getDedn04() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn04");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn05.
@param Dedn05 Dedn05 */
public void setDedn05 (BigDecimal Dedn05)
{
set_Value ("Dedn05", Dedn05);
}
/** Get Dedn05.
@return Dedn05 */
public BigDecimal getDedn05() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn05");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn06.
@param Dedn06 Dedn06 */
public void setDedn06 (BigDecimal Dedn06)
{
set_Value ("Dedn06", Dedn06);
}
/** Get Dedn06.
@return Dedn06 */
public BigDecimal getDedn06() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn06");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn07.
@param Dedn07 Dedn07 */
public void setDedn07 (BigDecimal Dedn07)
{
set_Value ("Dedn07", Dedn07);
}
/** Get Dedn07.
@return Dedn07 */
public BigDecimal getDedn07() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn07");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn08.
@param Dedn08 Dedn08 */
public void setDedn08 (BigDecimal Dedn08)
{
set_Value ("Dedn08", Dedn08);
}
/** Get Dedn08.
@return Dedn08 */
public BigDecimal getDedn08() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn08");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn09.
@param Dedn09 Dedn09 */
public void setDedn09 (BigDecimal Dedn09)
{
set_Value ("Dedn09", Dedn09);
}
/** Get Dedn09.
@return Dedn09 */
public BigDecimal getDedn09() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn09");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn10.
@param Dedn10 Dedn10 */
public void setDedn10 (BigDecimal Dedn10)
{
set_Value ("Dedn10", Dedn10);
}
/** Get Dedn10.
@return Dedn10 */
public BigDecimal getDedn10() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn10");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn11.
@param Dedn11 Dedn11 */
public void setDedn11 (BigDecimal Dedn11)
{
set_Value ("Dedn11", Dedn11);
}
/** Get Dedn11.
@return Dedn11 */
public BigDecimal getDedn11() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn11");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn12.
@param Dedn12 Dedn12 */
public void setDedn12 (BigDecimal Dedn12)
{
set_Value ("Dedn12", Dedn12);
}
/** Get Dedn12.
@return Dedn12 */
public BigDecimal getDedn12() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn12");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn13.
@param Dedn13 Dedn13 */
public void setDedn13 (BigDecimal Dedn13)
{
set_Value ("Dedn13", Dedn13);
}
/** Get Dedn13.
@return Dedn13 */
public BigDecimal getDedn13() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn13");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn14.
@param Dedn14 Dedn14 */
public void setDedn14 (BigDecimal Dedn14)
{
set_Value ("Dedn14", Dedn14);
}
/** Get Dedn14.
@return Dedn14 */
public BigDecimal getDedn14() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn14");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn15.
@param Dedn15 Dedn15 */
public void setDedn15 (BigDecimal Dedn15)
{
set_Value ("Dedn15", Dedn15);
}
/** Get Dedn15.
@return Dedn15 */
public BigDecimal getDedn15() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn15");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn16.
@param Dedn16 Dedn16 */
public void setDedn16 (BigDecimal Dedn16)
{
set_Value ("Dedn16", Dedn16);
}
/** Get Dedn16.
@return Dedn16 */
public BigDecimal getDedn16() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn16");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn17.
@param Dedn17 Dedn17 */
public void setDedn17 (BigDecimal Dedn17)
{
set_Value ("Dedn17", Dedn17);
}
/** Get Dedn17.
@return Dedn17 */
public BigDecimal getDedn17() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn17");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn18.
@param Dedn18 Dedn18 */
public void setDedn18 (BigDecimal Dedn18)
{
set_Value ("Dedn18", Dedn18);
}
/** Get Dedn18.
@return Dedn18 */
public BigDecimal getDedn18() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn18");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn19.
@param Dedn19 Dedn19 */
public void setDedn19 (BigDecimal Dedn19)
{
set_Value ("Dedn19", Dedn19);
}
/** Get Dedn19.
@return Dedn19 */
public BigDecimal getDedn19() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn19");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn20.
@param Dedn20 Dedn20 */
public void setDedn20 (BigDecimal Dedn20)
{
set_Value ("Dedn20", Dedn20);
}
/** Get Dedn20.
@return Dedn20 */
public BigDecimal getDedn20() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn20");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn21.
@param Dedn21 Dedn21 */
public void setDedn21 (BigDecimal Dedn21)
{
set_Value ("Dedn21", Dedn21);
}
/** Get Dedn21.
@return Dedn21 */
public BigDecimal getDedn21() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn21");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn22.
@param Dedn22 Dedn22 */
public void setDedn22 (BigDecimal Dedn22)
{
set_Value ("Dedn22", Dedn22);
}
/** Get Dedn22.
@return Dedn22 */
public BigDecimal getDedn22() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn22");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn23.
@param Dedn23 Dedn23 */
public void setDedn23 (BigDecimal Dedn23)
{
set_Value ("Dedn23", Dedn23);
}
/** Get Dedn23.
@return Dedn23 */
public BigDecimal getDedn23() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn23");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn24.
@param Dedn24 Dedn24 */
public void setDedn24 (BigDecimal Dedn24)
{
set_Value ("Dedn24", Dedn24);
}
/** Get Dedn24.
@return Dedn24 */
public BigDecimal getDedn24() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn24");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn25.
@param Dedn25 Dedn25 */
public void setDedn25 (BigDecimal Dedn25)
{
set_Value ("Dedn25", Dedn25);
}
/** Get Dedn25.
@return Dedn25 */
public BigDecimal getDedn25() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn25");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn26.
@param Dedn26 Dedn26 */
public void setDedn26 (BigDecimal Dedn26)
{
set_Value ("Dedn26", Dedn26);
}
/** Get Dedn26.
@return Dedn26 */
public BigDecimal getDedn26() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn26");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn27.
@param Dedn27 Dedn27 */
public void setDedn27 (BigDecimal Dedn27)
{
set_Value ("Dedn27", Dedn27);
}
/** Get Dedn27.
@return Dedn27 */
public BigDecimal getDedn27() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn27");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn28.
@param Dedn28 Dedn28 */
public void setDedn28 (BigDecimal Dedn28)
{
set_Value ("Dedn28", Dedn28);
}
/** Get Dedn28.
@return Dedn28 */
public BigDecimal getDedn28() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn28");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn29.
@param Dedn29 Dedn29 */
public void setDedn29 (BigDecimal Dedn29)
{
set_Value ("Dedn29", Dedn29);
}
/** Get Dedn29.
@return Dedn29 */
public BigDecimal getDedn29() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn29");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn30.
@param Dedn30 Dedn30 */
public void setDedn30 (BigDecimal Dedn30)
{
set_Value ("Dedn30", Dedn30);
}
/** Get Dedn30.
@return Dedn30 */
public BigDecimal getDedn30() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn30");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn31.
@param Dedn31 Dedn31 */
public void setDedn31 (BigDecimal Dedn31)
{
set_Value ("Dedn31", Dedn31);
}
/** Get Dedn31.
@return Dedn31 */
public BigDecimal getDedn31() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn31");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn32.
@param Dedn32 Dedn32 */
public void setDedn32 (BigDecimal Dedn32)
{
set_Value ("Dedn32", Dedn32);
}
/** Get Dedn32.
@return Dedn32 */
public BigDecimal getDedn32() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn32");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn33.
@param Dedn33 Dedn33 */
public void setDedn33 (BigDecimal Dedn33)
{
set_Value ("Dedn33", Dedn33);
}
/** Get Dedn33.
@return Dedn33 */
public BigDecimal getDedn33() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn33");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn34.
@param Dedn34 Dedn34 */
public void setDedn34 (BigDecimal Dedn34)
{
set_Value ("Dedn34", Dedn34);
}
/** Get Dedn34.
@return Dedn34 */
public BigDecimal getDedn34() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn34");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn35.
@param Dedn35 Dedn35 */
public void setDedn35 (BigDecimal Dedn35)
{
set_Value ("Dedn35", Dedn35);
}
/** Get Dedn35.
@return Dedn35 */
public BigDecimal getDedn35() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn35");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn36.
@param Dedn36 Dedn36 */
public void setDedn36 (BigDecimal Dedn36)
{
set_Value ("Dedn36", Dedn36);
}
/** Get Dedn36.
@return Dedn36 */
public BigDecimal getDedn36() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn36");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn37.
@param Dedn37 Dedn37 */
public void setDedn37 (BigDecimal Dedn37)
{
set_Value ("Dedn37", Dedn37);
}
/** Get Dedn37.
@return Dedn37 */
public BigDecimal getDedn37() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn37");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn38.
@param Dedn38 Dedn38 */
public void setDedn38 (BigDecimal Dedn38)
{
set_Value ("Dedn38", Dedn38);
}
/** Get Dedn38.
@return Dedn38 */
public BigDecimal getDedn38() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn38");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn39.
@param Dedn39 Dedn39 */
public void setDedn39 (BigDecimal Dedn39)
{
set_Value ("Dedn39", Dedn39);
}
/** Get Dedn39.
@return Dedn39 */
public BigDecimal getDedn39() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn39");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn40.
@param Dedn40 Dedn40 */
public void setDedn40 (BigDecimal Dedn40)
{
set_Value ("Dedn40", Dedn40);
}
/** Get Dedn40.
@return Dedn40 */
public BigDecimal getDedn40() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn40");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn41.
@param Dedn41 Dedn41 */
public void setDedn41 (BigDecimal Dedn41)
{
set_Value ("Dedn41", Dedn41);
}
/** Get Dedn41.
@return Dedn41 */
public BigDecimal getDedn41() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn41");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn42.
@param Dedn42 Dedn42 */
public void setDedn42 (BigDecimal Dedn42)
{
set_Value ("Dedn42", Dedn42);
}
/** Get Dedn42.
@return Dedn42 */
public BigDecimal getDedn42() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn42");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn43.
@param Dedn43 Dedn43 */
public void setDedn43 (BigDecimal Dedn43)
{
set_Value ("Dedn43", Dedn43);
}
/** Get Dedn43.
@return Dedn43 */
public BigDecimal getDedn43() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn43");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn44.
@param Dedn44 Dedn44 */
public void setDedn44 (BigDecimal Dedn44)
{
set_Value ("Dedn44", Dedn44);
}
/** Get Dedn44.
@return Dedn44 */
public BigDecimal getDedn44() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn44");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn45.
@param Dedn45 Dedn45 */
public void setDedn45 (BigDecimal Dedn45)
{
set_Value ("Dedn45", Dedn45);
}
/** Get Dedn45.
@return Dedn45 */
public BigDecimal getDedn45() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn45");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn46.
@param Dedn46 Dedn46 */
public void setDedn46 (BigDecimal Dedn46)
{
set_Value ("Dedn46", Dedn46);
}
/** Get Dedn46.
@return Dedn46 */
public BigDecimal getDedn46() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn46");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn47.
@param Dedn47 Dedn47 */
public void setDedn47 (BigDecimal Dedn47)
{
set_Value ("Dedn47", Dedn47);
}
/** Get Dedn47.
@return Dedn47 */
public BigDecimal getDedn47() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn47");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn48.
@param Dedn48 Dedn48 */
public void setDedn48 (BigDecimal Dedn48)
{
set_Value ("Dedn48", Dedn48);
}
/** Get Dedn48.
@return Dedn48 */
public BigDecimal getDedn48() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn48");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn49.
@param Dedn49 Dedn49 */
public void setDedn49 (BigDecimal Dedn49)
{
set_Value ("Dedn49", Dedn49);
}
/** Get Dedn49.
@return Dedn49 */
public BigDecimal getDedn49() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn49");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn50.
@param Dedn50 Dedn50 */
public void setDedn50 (BigDecimal Dedn50)
{
set_Value ("Dedn50", Dedn50);
}
/** Get Dedn50.
@return Dedn50 */
public BigDecimal getDedn50() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn50");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn51.
@param Dedn51 Dedn51 */
public void setDedn51 (BigDecimal Dedn51)
{
set_Value ("Dedn51", Dedn51);
}
/** Get Dedn51.
@return Dedn51 */
public BigDecimal getDedn51() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn51");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn52.
@param Dedn52 Dedn52 */
public void setDedn52 (BigDecimal Dedn52)
{
set_Value ("Dedn52", Dedn52);
}
/** Get Dedn52.
@return Dedn52 */
public BigDecimal getDedn52() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn52");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn53.
@param Dedn53 Dedn53 */
public void setDedn53 (BigDecimal Dedn53)
{
set_Value ("Dedn53", Dedn53);
}
/** Get Dedn53.
@return Dedn53 */
public BigDecimal getDedn53() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn53");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn54.
@param Dedn54 Dedn54 */
public void setDedn54 (BigDecimal Dedn54)
{
set_Value ("Dedn54", Dedn54);
}
/** Get Dedn54.
@return Dedn54 */
public BigDecimal getDedn54() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn54");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn55.
@param Dedn55 Dedn55 */
public void setDedn55 (BigDecimal Dedn55)
{
set_Value ("Dedn55", Dedn55);
}
/** Get Dedn55.
@return Dedn55 */
public BigDecimal getDedn55() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn55");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn56.
@param Dedn56 Dedn56 */
public void setDedn56 (BigDecimal Dedn56)
{
set_Value ("Dedn56", Dedn56);
}
/** Get Dedn56.
@return Dedn56 */
public BigDecimal getDedn56() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn56");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn57.
@param Dedn57 Dedn57 */
public void setDedn57 (BigDecimal Dedn57)
{
set_Value ("Dedn57", Dedn57);
}
/** Get Dedn57.
@return Dedn57 */
public BigDecimal getDedn57() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn57");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn58.
@param Dedn58 Dedn58 */
public void setDedn58 (BigDecimal Dedn58)
{
set_Value ("Dedn58", Dedn58);
}
/** Get Dedn58.
@return Dedn58 */
public BigDecimal getDedn58() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn58");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn59.
@param Dedn59 Dedn59 */
public void setDedn59 (BigDecimal Dedn59)
{
set_Value ("Dedn59", Dedn59);
}
/** Get Dedn59.
@return Dedn59 */
public BigDecimal getDedn59() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn59");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn60.
@param Dedn60 Dedn60 */
public void setDedn60 (BigDecimal Dedn60)
{
set_Value ("Dedn60", Dedn60);
}
/** Get Dedn60.
@return Dedn60 */
public BigDecimal getDedn60() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn60");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn61.
@param Dedn61 Dedn61 */
public void setDedn61 (BigDecimal Dedn61)
{
set_Value ("Dedn61", Dedn61);
}
/** Get Dedn61.
@return Dedn61 */
public BigDecimal getDedn61() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn61");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn62.
@param Dedn62 Dedn62 */
public void setDedn62 (BigDecimal Dedn62)
{
set_Value ("Dedn62", Dedn62);
}
/** Get Dedn62.
@return Dedn62 */
public BigDecimal getDedn62() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn62");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn63.
@param Dedn63 Dedn63 */
public void setDedn63 (BigDecimal Dedn63)
{
set_Value ("Dedn63", Dedn63);
}
/** Get Dedn63.
@return Dedn63 */
public BigDecimal getDedn63() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn63");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn64.
@param Dedn64 Dedn64 */
public void setDedn64 (BigDecimal Dedn64)
{
set_Value ("Dedn64", Dedn64);
}
/** Get Dedn64.
@return Dedn64 */
public BigDecimal getDedn64() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn64");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn65.
@param Dedn65 Dedn65 */
public void setDedn65 (BigDecimal Dedn65)
{
set_Value ("Dedn65", Dedn65);
}
/** Get Dedn65.
@return Dedn65 */
public BigDecimal getDedn65() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn65");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn66.
@param Dedn66 Dedn66 */
public void setDedn66 (BigDecimal Dedn66)
{
set_Value ("Dedn66", Dedn66);
}
/** Get Dedn66.
@return Dedn66 */
public BigDecimal getDedn66() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn66");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn67.
@param Dedn67 Dedn67 */
public void setDedn67 (BigDecimal Dedn67)
{
set_Value ("Dedn67", Dedn67);
}
/** Get Dedn67.
@return Dedn67 */
public BigDecimal getDedn67() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn67");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn68.
@param Dedn68 Dedn68 */
public void setDedn68 (BigDecimal Dedn68)
{
set_Value ("Dedn68", Dedn68);
}
/** Get Dedn68.
@return Dedn68 */
public BigDecimal getDedn68() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn68");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn69.
@param Dedn69 Dedn69 */
public void setDedn69 (BigDecimal Dedn69)
{
set_Value ("Dedn69", Dedn69);
}
/** Get Dedn69.
@return Dedn69 */
public BigDecimal getDedn69() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn69");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn70.
@param Dedn70 Dedn70 */
public void setDedn70 (BigDecimal Dedn70)
{
set_Value ("Dedn70", Dedn70);
}
/** Get Dedn70.
@return Dedn70 */
public BigDecimal getDedn70() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn70");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn71.
@param Dedn71 Dedn71 */
public void setDedn71 (BigDecimal Dedn71)
{
set_Value ("Dedn71", Dedn71);
}
/** Get Dedn71.
@return Dedn71 */
public BigDecimal getDedn71() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn71");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn72.
@param Dedn72 Dedn72 */
public void setDedn72 (BigDecimal Dedn72)
{
set_Value ("Dedn72", Dedn72);
}
/** Get Dedn72.
@return Dedn72 */
public BigDecimal getDedn72() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn72");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn73.
@param Dedn73 Dedn73 */
public void setDedn73 (BigDecimal Dedn73)
{
set_Value ("Dedn73", Dedn73);
}
/** Get Dedn73.
@return Dedn73 */
public BigDecimal getDedn73() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn73");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn74.
@param Dedn74 Dedn74 */
public void setDedn74 (BigDecimal Dedn74)
{
set_Value ("Dedn74", Dedn74);
}
/** Get Dedn74.
@return Dedn74 */
public BigDecimal getDedn74() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn74");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn75.
@param Dedn75 Dedn75 */
public void setDedn75 (BigDecimal Dedn75)
{
set_Value ("Dedn75", Dedn75);
}
/** Get Dedn75.
@return Dedn75 */
public BigDecimal getDedn75() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn75");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn76.
@param Dedn76 Dedn76 */
public void setDedn76 (BigDecimal Dedn76)
{
set_Value ("Dedn76", Dedn76);
}
/** Get Dedn76.
@return Dedn76 */
public BigDecimal getDedn76() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn76");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn77.
@param Dedn77 Dedn77 */
public void setDedn77 (BigDecimal Dedn77)
{
set_Value ("Dedn77", Dedn77);
}
/** Get Dedn77.
@return Dedn77 */
public BigDecimal getDedn77() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn77");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn78.
@param Dedn78 Dedn78 */
public void setDedn78 (BigDecimal Dedn78)
{
set_Value ("Dedn78", Dedn78);
}
/** Get Dedn78.
@return Dedn78 */
public BigDecimal getDedn78() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn78");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn79.
@param Dedn79 Dedn79 */
public void setDedn79 (BigDecimal Dedn79)
{
set_Value ("Dedn79", Dedn79);
}
/** Get Dedn79.
@return Dedn79 */
public BigDecimal getDedn79() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn79");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn80.
@param Dedn80 Dedn80 */
public void setDedn80 (BigDecimal Dedn80)
{
set_Value ("Dedn80", Dedn80);
}
/** Get Dedn80.
@return Dedn80 */
public BigDecimal getDedn80() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn80");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn81.
@param Dedn81 Dedn81 */
public void setDedn81 (BigDecimal Dedn81)
{
set_Value ("Dedn81", Dedn81);
}
/** Get Dedn81.
@return Dedn81 */
public BigDecimal getDedn81() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn81");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn82.
@param Dedn82 Dedn82 */
public void setDedn82 (BigDecimal Dedn82)
{
set_Value ("Dedn82", Dedn82);
}
/** Get Dedn82.
@return Dedn82 */
public BigDecimal getDedn82() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn82");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn83.
@param Dedn83 Dedn83 */
public void setDedn83 (BigDecimal Dedn83)
{
set_Value ("Dedn83", Dedn83);
}
/** Get Dedn83.
@return Dedn83 */
public BigDecimal getDedn83() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn83");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn84.
@param Dedn84 Dedn84 */
public void setDedn84 (BigDecimal Dedn84)
{
set_Value ("Dedn84", Dedn84);
}
/** Get Dedn84.
@return Dedn84 */
public BigDecimal getDedn84() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn84");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn85.
@param Dedn85 Dedn85 */
public void setDedn85 (BigDecimal Dedn85)
{
set_Value ("Dedn85", Dedn85);
}
/** Get Dedn85.
@return Dedn85 */
public BigDecimal getDedn85() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn85");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn86.
@param Dedn86 Dedn86 */
public void setDedn86 (BigDecimal Dedn86)
{
set_Value ("Dedn86", Dedn86);
}
/** Get Dedn86.
@return Dedn86 */
public BigDecimal getDedn86() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn86");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn87.
@param Dedn87 Dedn87 */
public void setDedn87 (BigDecimal Dedn87)
{
set_Value ("Dedn87", Dedn87);
}
/** Get Dedn87.
@return Dedn87 */
public BigDecimal getDedn87() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn87");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn88.
@param Dedn88 Dedn88 */
public void setDedn88 (BigDecimal Dedn88)
{
set_Value ("Dedn88", Dedn88);
}
/** Get Dedn88.
@return Dedn88 */
public BigDecimal getDedn88() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn88");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn89.
@param Dedn89 Dedn89 */
public void setDedn89 (BigDecimal Dedn89)
{
set_Value ("Dedn89", Dedn89);
}
/** Get Dedn89.
@return Dedn89 */
public BigDecimal getDedn89() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn89");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn90.
@param Dedn90 Dedn90 */
public void setDedn90 (BigDecimal Dedn90)
{
set_Value ("Dedn90", Dedn90);
}
/** Get Dedn90.
@return Dedn90 */
public BigDecimal getDedn90() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn90");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn91.
@param Dedn91 Dedn91 */
public void setDedn91 (BigDecimal Dedn91)
{
set_Value ("Dedn91", Dedn91);
}
/** Get Dedn91.
@return Dedn91 */
public BigDecimal getDedn91() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn91");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn92.
@param Dedn92 Dedn92 */
public void setDedn92 (BigDecimal Dedn92)
{
set_Value ("Dedn92", Dedn92);
}
/** Get Dedn92.
@return Dedn92 */
public BigDecimal getDedn92() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn92");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn93.
@param Dedn93 Dedn93 */
public void setDedn93 (BigDecimal Dedn93)
{
set_Value ("Dedn93", Dedn93);
}
/** Get Dedn93.
@return Dedn93 */
public BigDecimal getDedn93() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn93");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn94.
@param Dedn94 Dedn94 */
public void setDedn94 (BigDecimal Dedn94)
{
set_Value ("Dedn94", Dedn94);
}
/** Get Dedn94.
@return Dedn94 */
public BigDecimal getDedn94() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn94");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn95.
@param Dedn95 Dedn95 */
public void setDedn95 (BigDecimal Dedn95)
{
set_Value ("Dedn95", Dedn95);
}
/** Get Dedn95.
@return Dedn95 */
public BigDecimal getDedn95() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn95");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Dedn96.
@param Dedn96 Dedn96 */
public void setDedn96 (BigDecimal Dedn96)
{
set_Value ("Dedn96", Dedn96);
}
/** Get Dedn96.
@return Dedn96 */
public BigDecimal getDedn96() 
{
BigDecimal bd = (BigDecimal)get_Value("Dedn96");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Department.
@param Department Department */
public void setDepartment (String Department)
{
if (Department != null && Department.length() > 10)
{
log.warning("Length > 10 - truncated");
Department = Department.substring(0,9);
}
set_Value ("Department", Department);
}
/** Get Department.
@return Department */
public String getDepartment() 
{
return (String)get_Value("Department");
}
/** Set Employee.
@param Employee Employee */
public void setEmployee (String Employee)
{
if (Employee != null && Employee.length() > 10)
{
log.warning("Length > 10 - truncated");
Employee = Employee.substring(0,9);
}
set_Value ("Employee", Employee);
}
/** Get Employee.
@return Employee */
public String getEmployee() 
{
return (String)get_Value("Employee");
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
/** Set I_YTD_Deduction_ID.
@param I_YTD_Deduction_ID I_YTD_Deduction_ID */
public void setI_YTD_Deduction_ID (int I_YTD_Deduction_ID)
{
if (I_YTD_Deduction_ID < 1) throw new IllegalArgumentException ("I_YTD_Deduction_ID is mandatory.");
set_ValueNoCheck ("I_YTD_Deduction_ID", new Integer(I_YTD_Deduction_ID));
}
/** Get I_YTD_Deduction_ID.
@return I_YTD_Deduction_ID */
public int getI_YTD_Deduction_ID() 
{
Integer ii = (Integer)get_Value("I_YTD_Deduction_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set OntarioEmployerHealthTax.
@param OntarioEmployerHealthTax OntarioEmployerHealthTax */
public void setOntarioEmployerHealthTax (BigDecimal OntarioEmployerHealthTax)
{
set_Value ("OntarioEmployerHealthTax", OntarioEmployerHealthTax);
}
/** Get OntarioEmployerHealthTax.
@return OntarioEmployerHealthTax */
public BigDecimal getOntarioEmployerHealthTax() 
{
BigDecimal bd = (BigDecimal)get_Value("OntarioEmployerHealthTax");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Org.
@param Org Org */
public void setOrg (String Org)
{
if (Org != null && Org.length() > 10)
{
log.warning("Length > 10 - truncated");
Org = Org.substring(0,9);
}
set_Value ("Org", Org);
}
/** Get Org.
@return Org */
public String getOrg() 
{
return (String)get_Value("Org");
}
/** Set Organization.
@param Org_ID Organizational entity within client */
public void setOrg_ID (int Org_ID)
{
if (Org_ID <= 0) set_Value ("Org_ID", null);
 else 
set_Value ("Org_ID", new Integer(Org_ID));
}
/** Get Organization.
@return Organizational entity within client */
public int getOrg_ID() 
{
Integer ii = (Integer)get_Value("Org_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Department.
@param P_Department_ID Department */
public void setP_Department_ID (int P_Department_ID)
{
if (P_Department_ID <= 0) set_Value ("P_Department_ID", null);
 else 
set_Value ("P_Department_ID", new Integer(P_Department_ID));
}
/** Get Department.
@return Department */
public int getP_Department_ID() 
{
Integer ii = (Integer)get_Value("P_Department_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set PeriodEndDate.
@param PeriodEndDate PeriodEndDate */
public void setPeriodEndDate (Timestamp PeriodEndDate)
{
set_Value ("PeriodEndDate", PeriodEndDate);
}
/** Get PeriodEndDate.
@return PeriodEndDate */
public Timestamp getPeriodEndDate() 
{
return (Timestamp)get_Value("PeriodEndDate");
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
/** Set QuebecHealthServiceFund.
@param QuebecHealthServiceFund QuebecHealthServiceFund */
public void setQuebecHealthServiceFund (BigDecimal QuebecHealthServiceFund)
{
set_Value ("QuebecHealthServiceFund", QuebecHealthServiceFund);
}
/** Get QuebecHealthServiceFund.
@return QuebecHealthServiceFund */
public BigDecimal getQuebecHealthServiceFund() 
{
BigDecimal bd = (BigDecimal)get_Value("QuebecHealthServiceFund");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Sin.
@param Sin Social Insurance Number  */
public void setSin (String Sin)
{
if (Sin != null && Sin.length() > 10)
{
log.warning("Length > 10 - truncated");
Sin = Sin.substring(0,9);
}
set_Value ("Sin", Sin);
}
/** Get Sin.
@return Social Insurance Number  */
public String getSin() 
{
return (String)get_Value("Sin");
}
}
