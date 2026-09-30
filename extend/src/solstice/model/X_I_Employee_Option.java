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
/** Generated Model for I_Employee_Option
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_I_Employee_Option.java,v 1.1 2007/07/31 17:50:09 marmor01 Exp $ */
public class X_I_Employee_Option extends PO
{
/** Standard Constructor
@param ctx context
@param I_Employee_Option_ID id
@param trxName transaction
*/
public X_I_Employee_Option (Properties ctx, int I_Employee_Option_ID, String trxName)
{
super (ctx, I_Employee_Option_ID, trxName);
/** if (I_Employee_Option_ID == 0)
{
setI_EMPLOYEE_OPTION_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_I_Employee_Option (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100848 */
public static final int Table_ID=2100848;

/** TableName=I_Employee_Option */
public static final String Table_Name="I_Employee_Option";

protected static KeyNamePair Model = new KeyNamePair(2100848,"I_Employee_Option");

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
StringBuffer sb = new StringBuffer ("X_I_Employee_Option[").append(get_ID()).append("]");
return sb.toString();
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
/** Set Delete old/existing records.
@param DeleteOld Otherwise records will be added */
public void setDeleteOld (String DeleteOld)
{
if (DeleteOld != null && DeleteOld.length() > 1)
{
log.warning("Length > 1 - truncated");
DeleteOld = DeleteOld.substring(0,0);
}
set_Value ("DeleteOld", DeleteOld);
}
/** Get Delete old/existing records.
@return Otherwise records will be added */
public String getDeleteOld() 
{
return (String)get_Value("DeleteOld");
}
/** Set First name.
@param FirstName First name */
public void setFirstName (String FirstName)
{
if (FirstName != null && FirstName.length() > 30)
{
log.warning("Length > 30 - truncated");
FirstName = FirstName.substring(0,29);
}
set_Value ("FirstName", FirstName);
}
/** Get First name.
@return First name */
public String getFirstName() 
{
return (String)get_Value("FirstName");
}
/** Set Gain01.
@param Gain01 Gain01 */
public void setGain01 (BigDecimal Gain01)
{
set_Value ("Gain01", Gain01);
}
/** Get Gain01.
@return Gain01 */
public BigDecimal getGain01() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain01");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain02.
@param Gain02 Gain02 */
public void setGain02 (BigDecimal Gain02)
{
set_Value ("Gain02", Gain02);
}
/** Get Gain02.
@return Gain02 */
public BigDecimal getGain02() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain02");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain03.
@param Gain03 Gain03 */
public void setGain03 (BigDecimal Gain03)
{
set_Value ("Gain03", Gain03);
}
/** Get Gain03.
@return Gain03 */
public BigDecimal getGain03() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain03");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain04.
@param Gain04 Gain04 */
public void setGain04 (BigDecimal Gain04)
{
set_Value ("Gain04", Gain04);
}
/** Get Gain04.
@return Gain04 */
public BigDecimal getGain04() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain04");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain05.
@param Gain05 Gain05 */
public void setGain05 (BigDecimal Gain05)
{
set_Value ("Gain05", Gain05);
}
/** Get Gain05.
@return Gain05 */
public BigDecimal getGain05() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain05");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain06.
@param Gain06 Gain06 */
public void setGain06 (BigDecimal Gain06)
{
set_Value ("Gain06", Gain06);
}
/** Get Gain06.
@return Gain06 */
public BigDecimal getGain06() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain06");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain07.
@param Gain07 Gain07 */
public void setGain07 (BigDecimal Gain07)
{
set_Value ("Gain07", Gain07);
}
/** Get Gain07.
@return Gain07 */
public BigDecimal getGain07() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain07");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain08.
@param Gain08 Gain08 */
public void setGain08 (BigDecimal Gain08)
{
set_Value ("Gain08", Gain08);
}
/** Get Gain08.
@return Gain08 */
public BigDecimal getGain08() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain08");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain09.
@param Gain09 Gain09 */
public void setGain09 (BigDecimal Gain09)
{
set_Value ("Gain09", Gain09);
}
/** Get Gain09.
@return Gain09 */
public BigDecimal getGain09() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain09");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain10.
@param Gain10 Gain10 */
public void setGain10 (BigDecimal Gain10)
{
set_Value ("Gain10", Gain10);
}
/** Get Gain10.
@return Gain10 */
public BigDecimal getGain10() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain10");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain11.
@param Gain11 Gain11 */
public void setGain11 (BigDecimal Gain11)
{
set_Value ("Gain11", Gain11);
}
/** Get Gain11.
@return Gain11 */
public BigDecimal getGain11() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain11");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain12.
@param Gain12 Gain12 */
public void setGain12 (BigDecimal Gain12)
{
set_Value ("Gain12", Gain12);
}
/** Get Gain12.
@return Gain12 */
public BigDecimal getGain12() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain12");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain13.
@param Gain13 Gain13 */
public void setGain13 (BigDecimal Gain13)
{
set_Value ("Gain13", Gain13);
}
/** Get Gain13.
@return Gain13 */
public BigDecimal getGain13() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain13");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain14.
@param Gain14 Gain14 */
public void setGain14 (BigDecimal Gain14)
{
set_Value ("Gain14", Gain14);
}
/** Get Gain14.
@return Gain14 */
public BigDecimal getGain14() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain14");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain15.
@param Gain15 Gain15 */
public void setGain15 (BigDecimal Gain15)
{
set_Value ("Gain15", Gain15);
}
/** Get Gain15.
@return Gain15 */
public BigDecimal getGain15() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain15");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain16.
@param Gain16 Gain16 */
public void setGain16 (BigDecimal Gain16)
{
set_Value ("Gain16", Gain16);
}
/** Get Gain16.
@return Gain16 */
public BigDecimal getGain16() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain16");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain17.
@param Gain17 Gain17 */
public void setGain17 (BigDecimal Gain17)
{
set_Value ("Gain17", Gain17);
}
/** Get Gain17.
@return Gain17 */
public BigDecimal getGain17() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain17");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain18.
@param Gain18 Gain18 */
public void setGain18 (BigDecimal Gain18)
{
set_Value ("Gain18", Gain18);
}
/** Get Gain18.
@return Gain18 */
public BigDecimal getGain18() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain18");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain19.
@param Gain19 Gain19 */
public void setGain19 (BigDecimal Gain19)
{
set_Value ("Gain19", Gain19);
}
/** Get Gain19.
@return Gain19 */
public BigDecimal getGain19() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain19");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain20.
@param Gain20 Gain20 */
public void setGain20 (BigDecimal Gain20)
{
set_Value ("Gain20", Gain20);
}
/** Get Gain20.
@return Gain20 */
public BigDecimal getGain20() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain20");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain21.
@param Gain21 Gain21 */
public void setGain21 (BigDecimal Gain21)
{
set_Value ("Gain21", Gain21);
}
/** Get Gain21.
@return Gain21 */
public BigDecimal getGain21() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain21");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain22.
@param Gain22 Gain22 */
public void setGain22 (BigDecimal Gain22)
{
set_Value ("Gain22", Gain22);
}
/** Get Gain22.
@return Gain22 */
public BigDecimal getGain22() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain22");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain23.
@param Gain23 Gain23 */
public void setGain23 (BigDecimal Gain23)
{
set_Value ("Gain23", Gain23);
}
/** Get Gain23.
@return Gain23 */
public BigDecimal getGain23() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain23");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain24.
@param Gain24 Gain24 */
public void setGain24 (BigDecimal Gain24)
{
set_Value ("Gain24", Gain24);
}
/** Get Gain24.
@return Gain24 */
public BigDecimal getGain24() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain24");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain25.
@param Gain25 Gain25 */
public void setGain25 (BigDecimal Gain25)
{
set_Value ("Gain25", Gain25);
}
/** Get Gain25.
@return Gain25 */
public BigDecimal getGain25() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain25");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain26.
@param Gain26 Gain26 */
public void setGain26 (BigDecimal Gain26)
{
set_Value ("Gain26", Gain26);
}
/** Get Gain26.
@return Gain26 */
public BigDecimal getGain26() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain26");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain27.
@param Gain27 Gain27 */
public void setGain27 (BigDecimal Gain27)
{
set_Value ("Gain27", Gain27);
}
/** Get Gain27.
@return Gain27 */
public BigDecimal getGain27() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain27");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain28.
@param Gain28 Gain28 */
public void setGain28 (BigDecimal Gain28)
{
set_Value ("Gain28", Gain28);
}
/** Get Gain28.
@return Gain28 */
public BigDecimal getGain28() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain28");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain29.
@param Gain29 Gain29 */
public void setGain29 (BigDecimal Gain29)
{
set_Value ("Gain29", Gain29);
}
/** Get Gain29.
@return Gain29 */
public BigDecimal getGain29() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain29");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain30.
@param Gain30 Gain30 */
public void setGain30 (BigDecimal Gain30)
{
set_Value ("Gain30", Gain30);
}
/** Get Gain30.
@return Gain30 */
public BigDecimal getGain30() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain30");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain31.
@param Gain31 Gain31 */
public void setGain31 (BigDecimal Gain31)
{
set_Value ("Gain31", Gain31);
}
/** Get Gain31.
@return Gain31 */
public BigDecimal getGain31() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain31");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain32.
@param Gain32 Gain32 */
public void setGain32 (BigDecimal Gain32)
{
set_Value ("Gain32", Gain32);
}
/** Get Gain32.
@return Gain32 */
public BigDecimal getGain32() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain32");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain33.
@param Gain33 Gain33 */
public void setGain33 (BigDecimal Gain33)
{
set_Value ("Gain33", Gain33);
}
/** Get Gain33.
@return Gain33 */
public BigDecimal getGain33() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain33");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain34.
@param Gain34 Gain34 */
public void setGain34 (BigDecimal Gain34)
{
set_Value ("Gain34", Gain34);
}
/** Get Gain34.
@return Gain34 */
public BigDecimal getGain34() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain34");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain35.
@param Gain35 Gain35 */
public void setGain35 (BigDecimal Gain35)
{
set_Value ("Gain35", Gain35);
}
/** Get Gain35.
@return Gain35 */
public BigDecimal getGain35() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain35");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain36.
@param Gain36 Gain36 */
public void setGain36 (BigDecimal Gain36)
{
set_Value ("Gain36", Gain36);
}
/** Get Gain36.
@return Gain36 */
public BigDecimal getGain36() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain36");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain37.
@param Gain37 Gain37 */
public void setGain37 (BigDecimal Gain37)
{
set_Value ("Gain37", Gain37);
}
/** Get Gain37.
@return Gain37 */
public BigDecimal getGain37() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain37");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain38.
@param Gain38 Gain38 */
public void setGain38 (BigDecimal Gain38)
{
set_Value ("Gain38", Gain38);
}
/** Get Gain38.
@return Gain38 */
public BigDecimal getGain38() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain38");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain39.
@param Gain39 Gain39 */
public void setGain39 (BigDecimal Gain39)
{
set_Value ("Gain39", Gain39);
}
/** Get Gain39.
@return Gain39 */
public BigDecimal getGain39() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain39");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain40.
@param Gain40 Gain40 */
public void setGain40 (BigDecimal Gain40)
{
set_Value ("Gain40", Gain40);
}
/** Get Gain40.
@return Gain40 */
public BigDecimal getGain40() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain40");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain41.
@param Gain41 Gain41 */
public void setGain41 (BigDecimal Gain41)
{
set_Value ("Gain41", Gain41);
}
/** Get Gain41.
@return Gain41 */
public BigDecimal getGain41() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain41");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain42.
@param Gain42 Gain42 */
public void setGain42 (BigDecimal Gain42)
{
set_Value ("Gain42", Gain42);
}
/** Get Gain42.
@return Gain42 */
public BigDecimal getGain42() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain42");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain43.
@param Gain43 Gain43 */
public void setGain43 (BigDecimal Gain43)
{
set_Value ("Gain43", Gain43);
}
/** Get Gain43.
@return Gain43 */
public BigDecimal getGain43() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain43");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain44.
@param Gain44 Gain44 */
public void setGain44 (BigDecimal Gain44)
{
set_Value ("Gain44", Gain44);
}
/** Get Gain44.
@return Gain44 */
public BigDecimal getGain44() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain44");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain45.
@param Gain45 Gain45 */
public void setGain45 (BigDecimal Gain45)
{
set_Value ("Gain45", Gain45);
}
/** Get Gain45.
@return Gain45 */
public BigDecimal getGain45() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain45");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain46.
@param Gain46 Gain46 */
public void setGain46 (BigDecimal Gain46)
{
set_Value ("Gain46", Gain46);
}
/** Get Gain46.
@return Gain46 */
public BigDecimal getGain46() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain46");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain47.
@param Gain47 Gain47 */
public void setGain47 (BigDecimal Gain47)
{
set_Value ("Gain47", Gain47);
}
/** Get Gain47.
@return Gain47 */
public BigDecimal getGain47() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain47");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain48.
@param Gain48 Gain48 */
public void setGain48 (BigDecimal Gain48)
{
set_Value ("Gain48", Gain48);
}
/** Get Gain48.
@return Gain48 */
public BigDecimal getGain48() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain48");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain49.
@param Gain49 Gain49 */
public void setGain49 (BigDecimal Gain49)
{
set_Value ("Gain49", Gain49);
}
/** Get Gain49.
@return Gain49 */
public BigDecimal getGain49() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain49");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain50.
@param Gain50 Gain50 */
public void setGain50 (BigDecimal Gain50)
{
set_Value ("Gain50", Gain50);
}
/** Get Gain50.
@return Gain50 */
public BigDecimal getGain50() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain50");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain51.
@param Gain51 Gain51 */
public void setGain51 (BigDecimal Gain51)
{
set_Value ("Gain51", Gain51);
}
/** Get Gain51.
@return Gain51 */
public BigDecimal getGain51() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain51");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain52.
@param Gain52 Gain52 */
public void setGain52 (BigDecimal Gain52)
{
set_Value ("Gain52", Gain52);
}
/** Get Gain52.
@return Gain52 */
public BigDecimal getGain52() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain52");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain53.
@param Gain53 Gain53 */
public void setGain53 (BigDecimal Gain53)
{
set_Value ("Gain53", Gain53);
}
/** Get Gain53.
@return Gain53 */
public BigDecimal getGain53() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain53");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain54.
@param Gain54 Gain54 */
public void setGain54 (BigDecimal Gain54)
{
set_Value ("Gain54", Gain54);
}
/** Get Gain54.
@return Gain54 */
public BigDecimal getGain54() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain54");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain55.
@param Gain55 Gain55 */
public void setGain55 (BigDecimal Gain55)
{
set_Value ("Gain55", Gain55);
}
/** Get Gain55.
@return Gain55 */
public BigDecimal getGain55() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain55");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain56.
@param Gain56 Gain56 */
public void setGain56 (BigDecimal Gain56)
{
set_Value ("Gain56", Gain56);
}
/** Get Gain56.
@return Gain56 */
public BigDecimal getGain56() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain56");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain57.
@param Gain57 Gain57 */
public void setGain57 (BigDecimal Gain57)
{
set_Value ("Gain57", Gain57);
}
/** Get Gain57.
@return Gain57 */
public BigDecimal getGain57() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain57");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain58.
@param Gain58 Gain58 */
public void setGain58 (BigDecimal Gain58)
{
set_Value ("Gain58", Gain58);
}
/** Get Gain58.
@return Gain58 */
public BigDecimal getGain58() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain58");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain59.
@param Gain59 Gain59 */
public void setGain59 (BigDecimal Gain59)
{
set_Value ("Gain59", Gain59);
}
/** Get Gain59.
@return Gain59 */
public BigDecimal getGain59() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain59");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain60.
@param Gain60 Gain60 */
public void setGain60 (BigDecimal Gain60)
{
set_Value ("Gain60", Gain60);
}
/** Get Gain60.
@return Gain60 */
public BigDecimal getGain60() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain60");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain61.
@param Gain61 Gain61 */
public void setGain61 (BigDecimal Gain61)
{
set_Value ("Gain61", Gain61);
}
/** Get Gain61.
@return Gain61 */
public BigDecimal getGain61() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain61");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain62.
@param Gain62 Gain62 */
public void setGain62 (BigDecimal Gain62)
{
set_Value ("Gain62", Gain62);
}
/** Get Gain62.
@return Gain62 */
public BigDecimal getGain62() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain62");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain63.
@param Gain63 Gain63 */
public void setGain63 (BigDecimal Gain63)
{
set_Value ("Gain63", Gain63);
}
/** Get Gain63.
@return Gain63 */
public BigDecimal getGain63() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain63");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain64.
@param Gain64 Gain64 */
public void setGain64 (BigDecimal Gain64)
{
set_Value ("Gain64", Gain64);
}
/** Get Gain64.
@return Gain64 */
public BigDecimal getGain64() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain64");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain65.
@param Gain65 Gain65 */
public void setGain65 (BigDecimal Gain65)
{
set_Value ("Gain65", Gain65);
}
/** Get Gain65.
@return Gain65 */
public BigDecimal getGain65() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain65");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain66.
@param Gain66 Gain66 */
public void setGain66 (BigDecimal Gain66)
{
set_Value ("Gain66", Gain66);
}
/** Get Gain66.
@return Gain66 */
public BigDecimal getGain66() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain66");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain67.
@param Gain67 Gain67 */
public void setGain67 (BigDecimal Gain67)
{
set_Value ("Gain67", Gain67);
}
/** Get Gain67.
@return Gain67 */
public BigDecimal getGain67() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain67");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain68.
@param Gain68 Gain68 */
public void setGain68 (BigDecimal Gain68)
{
set_Value ("Gain68", Gain68);
}
/** Get Gain68.
@return Gain68 */
public BigDecimal getGain68() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain68");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain69.
@param Gain69 Gain69 */
public void setGain69 (BigDecimal Gain69)
{
set_Value ("Gain69", Gain69);
}
/** Get Gain69.
@return Gain69 */
public BigDecimal getGain69() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain69");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain70.
@param Gain70 Gain70 */
public void setGain70 (BigDecimal Gain70)
{
set_Value ("Gain70", Gain70);
}
/** Get Gain70.
@return Gain70 */
public BigDecimal getGain70() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain70");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain71.
@param Gain71 Gain71 */
public void setGain71 (BigDecimal Gain71)
{
set_Value ("Gain71", Gain71);
}
/** Get Gain71.
@return Gain71 */
public BigDecimal getGain71() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain71");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain72.
@param Gain72 Gain72 */
public void setGain72 (BigDecimal Gain72)
{
set_Value ("Gain72", Gain72);
}
/** Get Gain72.
@return Gain72 */
public BigDecimal getGain72() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain72");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain73.
@param Gain73 Gain73 */
public void setGain73 (BigDecimal Gain73)
{
set_Value ("Gain73", Gain73);
}
/** Get Gain73.
@return Gain73 */
public BigDecimal getGain73() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain73");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain74.
@param Gain74 Gain74 */
public void setGain74 (BigDecimal Gain74)
{
set_Value ("Gain74", Gain74);
}
/** Get Gain74.
@return Gain74 */
public BigDecimal getGain74() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain74");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain75.
@param Gain75 Gain75 */
public void setGain75 (BigDecimal Gain75)
{
set_Value ("Gain75", Gain75);
}
/** Get Gain75.
@return Gain75 */
public BigDecimal getGain75() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain75");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain76.
@param Gain76 Gain76 */
public void setGain76 (BigDecimal Gain76)
{
set_Value ("Gain76", Gain76);
}
/** Get Gain76.
@return Gain76 */
public BigDecimal getGain76() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain76");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain77.
@param Gain77 Gain77 */
public void setGain77 (BigDecimal Gain77)
{
set_Value ("Gain77", Gain77);
}
/** Get Gain77.
@return Gain77 */
public BigDecimal getGain77() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain77");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain78.
@param Gain78 Gain78 */
public void setGain78 (BigDecimal Gain78)
{
set_Value ("Gain78", Gain78);
}
/** Get Gain78.
@return Gain78 */
public BigDecimal getGain78() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain78");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain79.
@param Gain79 Gain79 */
public void setGain79 (BigDecimal Gain79)
{
set_Value ("Gain79", Gain79);
}
/** Get Gain79.
@return Gain79 */
public BigDecimal getGain79() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain79");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain80.
@param Gain80 Gain80 */
public void setGain80 (BigDecimal Gain80)
{
set_Value ("Gain80", Gain80);
}
/** Get Gain80.
@return Gain80 */
public BigDecimal getGain80() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain80");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain81.
@param Gain81 Gain81 */
public void setGain81 (BigDecimal Gain81)
{
set_Value ("Gain81", Gain81);
}
/** Get Gain81.
@return Gain81 */
public BigDecimal getGain81() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain81");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain82.
@param Gain82 Gain82 */
public void setGain82 (BigDecimal Gain82)
{
set_Value ("Gain82", Gain82);
}
/** Get Gain82.
@return Gain82 */
public BigDecimal getGain82() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain82");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain83.
@param Gain83 Gain83 */
public void setGain83 (BigDecimal Gain83)
{
set_Value ("Gain83", Gain83);
}
/** Get Gain83.
@return Gain83 */
public BigDecimal getGain83() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain83");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain84.
@param Gain84 Gain84 */
public void setGain84 (BigDecimal Gain84)
{
set_Value ("Gain84", Gain84);
}
/** Get Gain84.
@return Gain84 */
public BigDecimal getGain84() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain84");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain85.
@param Gain85 Gain85 */
public void setGain85 (BigDecimal Gain85)
{
set_Value ("Gain85", Gain85);
}
/** Get Gain85.
@return Gain85 */
public BigDecimal getGain85() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain85");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain86.
@param Gain86 Gain86 */
public void setGain86 (BigDecimal Gain86)
{
set_Value ("Gain86", Gain86);
}
/** Get Gain86.
@return Gain86 */
public BigDecimal getGain86() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain86");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain87.
@param Gain87 Gain87 */
public void setGain87 (BigDecimal Gain87)
{
set_Value ("Gain87", Gain87);
}
/** Get Gain87.
@return Gain87 */
public BigDecimal getGain87() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain87");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain88.
@param Gain88 Gain88 */
public void setGain88 (BigDecimal Gain88)
{
set_Value ("Gain88", Gain88);
}
/** Get Gain88.
@return Gain88 */
public BigDecimal getGain88() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain88");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain89.
@param Gain89 Gain89 */
public void setGain89 (BigDecimal Gain89)
{
set_Value ("Gain89", Gain89);
}
/** Get Gain89.
@return Gain89 */
public BigDecimal getGain89() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain89");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain90.
@param Gain90 Gain90 */
public void setGain90 (BigDecimal Gain90)
{
set_Value ("Gain90", Gain90);
}
/** Get Gain90.
@return Gain90 */
public BigDecimal getGain90() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain90");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain91.
@param Gain91 Gain91 */
public void setGain91 (BigDecimal Gain91)
{
set_Value ("Gain91", Gain91);
}
/** Get Gain91.
@return Gain91 */
public BigDecimal getGain91() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain91");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain92.
@param Gain92 Gain92 */
public void setGain92 (BigDecimal Gain92)
{
set_Value ("Gain92", Gain92);
}
/** Get Gain92.
@return Gain92 */
public BigDecimal getGain92() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain92");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain93.
@param Gain93 Gain93 */
public void setGain93 (BigDecimal Gain93)
{
set_Value ("Gain93", Gain93);
}
/** Get Gain93.
@return Gain93 */
public BigDecimal getGain93() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain93");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain94.
@param Gain94 Gain94 */
public void setGain94 (BigDecimal Gain94)
{
set_Value ("Gain94", Gain94);
}
/** Get Gain94.
@return Gain94 */
public BigDecimal getGain94() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain94");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain95.
@param Gain95 Gain95 */
public void setGain95 (BigDecimal Gain95)
{
set_Value ("Gain95", Gain95);
}
/** Get Gain95.
@return Gain95 */
public BigDecimal getGain95() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain95");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gain96.
@param Gain96 Gain96 */
public void setGain96 (BigDecimal Gain96)
{
set_Value ("Gain96", Gain96);
}
/** Get Gain96.
@return Gain96 */
public BigDecimal getGain96() 
{
BigDecimal bd = (BigDecimal)get_Value("Gain96");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set I_EMPLOYEE_OPTION_ID.
@param I_EMPLOYEE_OPTION_ID I_EMPLOYEE_OPTION_ID */
public void setI_EMPLOYEE_OPTION_ID (int I_EMPLOYEE_OPTION_ID)
{
if (I_EMPLOYEE_OPTION_ID < 1) throw new IllegalArgumentException ("I_EMPLOYEE_OPTION_ID is mandatory.");
set_ValueNoCheck ("I_EMPLOYEE_OPTION_ID", new Integer(I_EMPLOYEE_OPTION_ID));
}
/** Get I_EMPLOYEE_OPTION_ID.
@return I_EMPLOYEE_OPTION_ID */
public int getI_EMPLOYEE_OPTION_ID() 
{
Integer ii = (Integer)get_Value("I_EMPLOYEE_OPTION_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set langue.
@param langue langue */
public void setlangue (String langue)
{
if (langue != null && langue.length() > 1)
{
log.warning("Length > 1 - truncated");
langue = langue.substring(0,0);
}
set_Value ("langue", langue);
}
/** Get langue.
@return langue */
public String getlangue() 
{
return (String)get_Value("langue");
}
/** Set LastName.
@param LastName LastName */
public void setLastName (String LastName)
{
if (LastName != null && LastName.length() > 30)
{
log.warning("Length > 30 - truncated");
LastName = LastName.substring(0,29);
}
set_Value ("LastName", LastName);
}
/** Get LastName.
@return LastName */
public String getLastName() 
{
return (String)get_Value("LastName");
}
/** Set MaxDedn07.
@param MaxDedn07 MaxDedn07 */
public void setMaxDedn07 (BigDecimal MaxDedn07)
{
set_Value ("MaxDedn07", MaxDedn07);
}
/** Get MaxDedn07.
@return MaxDedn07 */
public BigDecimal getMaxDedn07() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn07");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MaxDedn08.
@param MaxDedn08 MaxDedn08 */
public void setMaxDedn08 (BigDecimal MaxDedn08)
{
set_Value ("MaxDedn08", MaxDedn08);
}
/** Get MaxDedn08.
@return MaxDedn08 */
public BigDecimal getMaxDedn08() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn08");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MaxDedn09.
@param MaxDedn09 MaxDedn09 */
public void setMaxDedn09 (BigDecimal MaxDedn09)
{
set_Value ("MaxDedn09", MaxDedn09);
}
/** Get MaxDedn09.
@return MaxDedn09 */
public BigDecimal getMaxDedn09() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn09");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MaxDedn10.
@param MaxDedn10 MaxDedn10 */
public void setMaxDedn10 (BigDecimal MaxDedn10)
{
set_Value ("MaxDedn10", MaxDedn10);
}
/** Get MaxDedn10.
@return MaxDedn10 */
public BigDecimal getMaxDedn10() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn10");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MaxDedn11.
@param MaxDedn11 MaxDedn11 */
public void setMaxDedn11 (BigDecimal MaxDedn11)
{
set_Value ("MaxDedn11", MaxDedn11);
}
/** Get MaxDedn11.
@return MaxDedn11 */
public BigDecimal getMaxDedn11() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn11");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MaxDedn22.
@param MaxDedn22 MaxDedn22 */
public void setMaxDedn22 (BigDecimal MaxDedn22)
{
set_Value ("MaxDedn22", MaxDedn22);
}
/** Get MaxDedn22.
@return MaxDedn22 */
public BigDecimal getMaxDedn22() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn22");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MaxDedn27.
@param MaxDedn27 MaxDedn27 */
public void setMaxDedn27 (BigDecimal MaxDedn27)
{
set_Value ("MaxDedn27", MaxDedn27);
}
/** Get MaxDedn27.
@return MaxDedn27 */
public BigDecimal getMaxDedn27() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn27");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MaxDedn34.
@param MaxDedn34 MaxDedn34 */
public void setMaxDedn34 (BigDecimal MaxDedn34)
{
set_Value ("MaxDedn34", MaxDedn34);
}
/** Get MaxDedn34.
@return MaxDedn34 */
public BigDecimal getMaxDedn34() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn34");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MaxDedn49.
@param MaxDedn49 MaxDedn49 */
public void setMaxDedn49 (BigDecimal MaxDedn49)
{
set_Value ("MaxDedn49", MaxDedn49);
}
/** Get MaxDedn49.
@return MaxDedn49 */
public BigDecimal getMaxDedn49() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn49");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MaxDedn54.
@param MaxDedn54 MaxDedn54 */
public void setMaxDedn54 (BigDecimal MaxDedn54)
{
set_Value ("MaxDedn54", MaxDedn54);
}
/** Get MaxDedn54.
@return MaxDedn54 */
public BigDecimal getMaxDedn54() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn54");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MaxDedn55.
@param MaxDedn55 MaxDedn55 */
public void setMaxDedn55 (BigDecimal MaxDedn55)
{
set_Value ("MaxDedn55", MaxDedn55);
}
/** Get MaxDedn55.
@return MaxDedn55 */
public BigDecimal getMaxDedn55() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn55");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MaxDedn57.
@param MaxDedn57 MaxDedn57 */
public void setMaxDedn57 (BigDecimal MaxDedn57)
{
set_Value ("MaxDedn57", MaxDedn57);
}
/** Get MaxDedn57.
@return MaxDedn57 */
public BigDecimal getMaxDedn57() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn57");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MaxDedn62.
@param MaxDedn62 MaxDedn62 */
public void setMaxDedn62 (BigDecimal MaxDedn62)
{
set_Value ("MaxDedn62", MaxDedn62);
}
/** Get MaxDedn62.
@return MaxDedn62 */
public BigDecimal getMaxDedn62() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn62");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MaxDedn66.
@param MaxDedn66 MaxDedn66 */
public void setMaxDedn66 (BigDecimal MaxDedn66)
{
set_Value ("MaxDedn66", MaxDedn66);
}
/** Get MaxDedn66.
@return MaxDedn66 */
public BigDecimal getMaxDedn66() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn66");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MaxDedn69.
@param MaxDedn69 MaxDedn69 */
public void setMaxDedn69 (BigDecimal MaxDedn69)
{
set_Value ("MaxDedn69", MaxDedn69);
}
/** Get MaxDedn69.
@return MaxDedn69 */
public BigDecimal getMaxDedn69() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn69");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set MaxDedn80.
@param MaxDedn80 MaxDedn80 */
public void setMaxDedn80 (BigDecimal MaxDedn80)
{
set_Value ("MaxDedn80", MaxDedn80);
}
/** Get MaxDedn80.
@return MaxDedn80 */
public BigDecimal getMaxDedn80() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxDedn80");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set Post.
@param P_Post_ID Post */
public void setP_Post_ID (int P_Post_ID)
{
if (P_Post_ID <= 0) set_Value ("P_Post_ID", null);
 else 
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
/** Set Post.
@param Post Post */
public void setPost (String Post)
{
if (Post != null && Post.length() > 60)
{
log.warning("Length > 60 - truncated");
Post = Post.substring(0,59);
}
set_Value ("Post", Post);
}
/** Get Post.
@return Post */
public String getPost() 
{
return (String)get_Value("Post");
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
