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
/** Generated Model for P_Employee_Roe
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.2 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Roe extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Roe_ID id
@param trxName transaction
*/
public X_P_Employee_Roe (Properties ctx, int P_Employee_Roe_ID, String trxName)
{
super (ctx, P_Employee_Roe_ID, trxName);
/** if (P_Employee_Roe_ID == 0)
{
setP_Employee_ID (0);
setP_Employee_Roe_ID (0);
setPayPeriodType (null);	// B
setTransfered (false);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Roe (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000141 */
public static final int Table_ID=2000141;

/** TableName=P_Employee_Roe */
public static final String Table_Name="P_Employee_Roe";

protected static KeyNamePair Model = new KeyNamePair(2000141,"P_Employee_Roe");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Roe[").append(get_ID()).append("]");
return sb.toString();
}
/** Set User/Contact.
@param AD_User_ID User within the system - Internal or Business Partner Contact */
public void setAD_User_ID (int AD_User_ID)
{
if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
 else 
set_Value ("AD_User_ID", new Integer(AD_User_ID));
}
/** Get User/Contact.
@return User within the system - Internal or Business Partner Contact */
public int getAD_User_ID() 
{
Integer ii = (Integer)get_Value("AD_User_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Canada Revenue Agency Business Number.
@param CanadaRevenueBusinessNumber Canada Revenue Agency Business Number */
public void setCanadaRevenueBusinessNumber (String CanadaRevenueBusinessNumber)
{
if (CanadaRevenueBusinessNumber != null && CanadaRevenueBusinessNumber.length() > 15)
{
log.warning("Length > 15 - truncated");
CanadaRevenueBusinessNumber = CanadaRevenueBusinessNumber.substring(0,14);
}
set_Value ("CanadaRevenueBusinessNumber", CanadaRevenueBusinessNumber);
}
/** Get Canada Revenue Agency Business Number.
@return Canada Revenue Agency Business Number */
public String getCanadaRevenueBusinessNumber() 
{
return (String)get_Value("CanadaRevenueBusinessNumber");
}
/** Set Comments Line 1.
@param CommentsLine01 Comments Line 1 */
public void setCommentsLine01 (String CommentsLine01)
{
if (CommentsLine01 != null && CommentsLine01.length() > 40)
{
log.warning("Length > 40 - truncated");
CommentsLine01 = CommentsLine01.substring(0,39);
}
set_Value ("CommentsLine01", CommentsLine01);
}
/** Get Comments Line 1.
@return Comments Line 1 */
public String getCommentsLine01() 
{
return (String)get_Value("CommentsLine01");
}
/** Set Comments Line 2.
@param CommentsLine02 Comments Line 2 */
public void setCommentsLine02 (String CommentsLine02)
{
if (CommentsLine02 != null && CommentsLine02.length() > 40)
{
log.warning("Length > 40 - truncated");
CommentsLine02 = CommentsLine02.substring(0,39);
}
set_Value ("CommentsLine02", CommentsLine02);
}
/** Get Comments Line 2.
@return Comments Line 2 */
public String getCommentsLine02() 
{
return (String)get_Value("CommentsLine02");
}
/** Set Comments Line 3.
@param CommentsLine03 Comments Line 3 */
public void setCommentsLine03 (String CommentsLine03)
{
if (CommentsLine03 != null && CommentsLine03.length() > 40)
{
log.warning("Length > 40 - truncated");
CommentsLine03 = CommentsLine03.substring(0,39);
}
set_Value ("CommentsLine03", CommentsLine03);
}
/** Get Comments Line 3.
@return Comments Line 3 */
public String getCommentsLine03() 
{
return (String)get_Value("CommentsLine03");
}
/** Set Comments Line 4.
@param CommentsLine04 Comments Line 4 */
public void setCommentsLine04 (String CommentsLine04)
{
if (CommentsLine04 != null && CommentsLine04.length() > 40)
{
log.warning("Length > 40 - truncated");
CommentsLine04 = CommentsLine04.substring(0,39);
}
set_Value ("CommentsLine04", CommentsLine04);
}
/** Get Comments Line 4.
@return Comments Line 4 */
public String getCommentsLine04() 
{
return (String)get_Value("CommentsLine04");
}
/** Set Earnings Period 01.
@param EarningsForPayPeriod01 Earnings Period 01 */
public void setEarningsForPayPeriod01 (BigDecimal EarningsForPayPeriod01)
{
set_Value ("EarningsForPayPeriod01", EarningsForPayPeriod01);
}
/** Get Earnings Period 01.
@return Earnings Period 01 */
public BigDecimal getEarningsForPayPeriod01() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod01");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 02.
@param EarningsForPayPeriod02 Earnings Period 02 */
public void setEarningsForPayPeriod02 (BigDecimal EarningsForPayPeriod02)
{
set_Value ("EarningsForPayPeriod02", EarningsForPayPeriod02);
}
/** Get Earnings Period 02.
@return Earnings Period 02 */
public BigDecimal getEarningsForPayPeriod02() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod02");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 03.
@param EarningsForPayPeriod03 Earnings Period 03 */
public void setEarningsForPayPeriod03 (BigDecimal EarningsForPayPeriod03)
{
set_Value ("EarningsForPayPeriod03", EarningsForPayPeriod03);
}
/** Get Earnings Period 03.
@return Earnings Period 03 */
public BigDecimal getEarningsForPayPeriod03() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod03");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 04.
@param EarningsForPayPeriod04 Earnings Period 04 */
public void setEarningsForPayPeriod04 (BigDecimal EarningsForPayPeriod04)
{
set_Value ("EarningsForPayPeriod04", EarningsForPayPeriod04);
}
/** Get Earnings Period 04.
@return Earnings Period 04 */
public BigDecimal getEarningsForPayPeriod04() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod04");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 05.
@param EarningsForPayPeriod05 Earnings Period 05 */
public void setEarningsForPayPeriod05 (BigDecimal EarningsForPayPeriod05)
{
set_Value ("EarningsForPayPeriod05", EarningsForPayPeriod05);
}
/** Get Earnings Period 05.
@return Earnings Period 05 */
public BigDecimal getEarningsForPayPeriod05() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod05");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 06.
@param EarningsForPayPeriod06 Earnings Period 06 */
public void setEarningsForPayPeriod06 (BigDecimal EarningsForPayPeriod06)
{
set_Value ("EarningsForPayPeriod06", EarningsForPayPeriod06);
}
/** Get Earnings Period 06.
@return Earnings Period 06 */
public BigDecimal getEarningsForPayPeriod06() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod06");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 07.
@param EarningsForPayPeriod07 Earnings Period 07 */
public void setEarningsForPayPeriod07 (BigDecimal EarningsForPayPeriod07)
{
set_Value ("EarningsForPayPeriod07", EarningsForPayPeriod07);
}
/** Get Earnings Period 07.
@return Earnings Period 07 */
public BigDecimal getEarningsForPayPeriod07() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod07");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 08.
@param EarningsForPayPeriod08 Earnings Period 08 */
public void setEarningsForPayPeriod08 (BigDecimal EarningsForPayPeriod08)
{
set_Value ("EarningsForPayPeriod08", EarningsForPayPeriod08);
}
/** Get Earnings Period 08.
@return Earnings Period 08 */
public BigDecimal getEarningsForPayPeriod08() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod08");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 09.
@param EarningsForPayPeriod09 Earnings Period 09 */
public void setEarningsForPayPeriod09 (BigDecimal EarningsForPayPeriod09)
{
set_Value ("EarningsForPayPeriod09", EarningsForPayPeriod09);
}
/** Get Earnings Period 09.
@return Earnings Period 09 */
public BigDecimal getEarningsForPayPeriod09() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod09");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 10.
@param EarningsForPayPeriod10 Earnings Period 10 */
public void setEarningsForPayPeriod10 (BigDecimal EarningsForPayPeriod10)
{
set_Value ("EarningsForPayPeriod10", EarningsForPayPeriod10);
}
/** Get Earnings Period 10.
@return Earnings Period 10 */
public BigDecimal getEarningsForPayPeriod10() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod10");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 11.
@param EarningsForPayPeriod11 Earnings Period 11 */
public void setEarningsForPayPeriod11 (BigDecimal EarningsForPayPeriod11)
{
set_Value ("EarningsForPayPeriod11", EarningsForPayPeriod11);
}
/** Get Earnings Period 11.
@return Earnings Period 11 */
public BigDecimal getEarningsForPayPeriod11() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod11");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 12.
@param EarningsForPayPeriod12 Earnings Period 12 */
public void setEarningsForPayPeriod12 (BigDecimal EarningsForPayPeriod12)
{
set_Value ("EarningsForPayPeriod12", EarningsForPayPeriod12);
}
/** Get Earnings Period 12.
@return Earnings Period 12 */
public BigDecimal getEarningsForPayPeriod12() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod12");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 13.
@param EarningsForPayPeriod13 Earnings Period 13 */
public void setEarningsForPayPeriod13 (BigDecimal EarningsForPayPeriod13)
{
set_Value ("EarningsForPayPeriod13", EarningsForPayPeriod13);
}
/** Get Earnings Period 13.
@return Earnings Period 13 */
public BigDecimal getEarningsForPayPeriod13() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod13");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 14.
@param EarningsForPayPeriod14 Earnings Period 14 */
public void setEarningsForPayPeriod14 (BigDecimal EarningsForPayPeriod14)
{
set_Value ("EarningsForPayPeriod14", EarningsForPayPeriod14);
}
/** Get Earnings Period 14.
@return Earnings Period 14 */
public BigDecimal getEarningsForPayPeriod14() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod14");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 15.
@param EarningsForPayPeriod15 Earnings Period 15 */
public void setEarningsForPayPeriod15 (BigDecimal EarningsForPayPeriod15)
{
set_Value ("EarningsForPayPeriod15", EarningsForPayPeriod15);
}
/** Get Earnings Period 15.
@return Earnings Period 15 */
public BigDecimal getEarningsForPayPeriod15() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod15");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 16.
@param EarningsForPayPeriod16 Earnings Period 16 */
public void setEarningsForPayPeriod16 (BigDecimal EarningsForPayPeriod16)
{
set_Value ("EarningsForPayPeriod16", EarningsForPayPeriod16);
}
/** Get Earnings Period 16.
@return Earnings Period 16 */
public BigDecimal getEarningsForPayPeriod16() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod16");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 17.
@param EarningsForPayPeriod17 Earnings Period 17 */
public void setEarningsForPayPeriod17 (BigDecimal EarningsForPayPeriod17)
{
set_Value ("EarningsForPayPeriod17", EarningsForPayPeriod17);
}
/** Get Earnings Period 17.
@return Earnings Period 17 */
public BigDecimal getEarningsForPayPeriod17() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod17");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 18.
@param EarningsForPayPeriod18 Earnings Period 18 */
public void setEarningsForPayPeriod18 (BigDecimal EarningsForPayPeriod18)
{
set_Value ("EarningsForPayPeriod18", EarningsForPayPeriod18);
}
/** Get Earnings Period 18.
@return Earnings Period 18 */
public BigDecimal getEarningsForPayPeriod18() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod18");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 19.
@param EarningsForPayPeriod19 Earnings Period 19 */
public void setEarningsForPayPeriod19 (BigDecimal EarningsForPayPeriod19)
{
set_Value ("EarningsForPayPeriod19", EarningsForPayPeriod19);
}
/** Get Earnings Period 19.
@return Earnings Period 19 */
public BigDecimal getEarningsForPayPeriod19() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod19");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 20.
@param EarningsForPayPeriod20 Earnings Period 20 */
public void setEarningsForPayPeriod20 (BigDecimal EarningsForPayPeriod20)
{
set_Value ("EarningsForPayPeriod20", EarningsForPayPeriod20);
}
/** Get Earnings Period 20.
@return Earnings Period 20 */
public BigDecimal getEarningsForPayPeriod20() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod20");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 21.
@param EarningsForPayPeriod21 Earnings Period 21 */
public void setEarningsForPayPeriod21 (BigDecimal EarningsForPayPeriod21)
{
set_Value ("EarningsForPayPeriod21", EarningsForPayPeriod21);
}
/** Get Earnings Period 21.
@return Earnings Period 21 */
public BigDecimal getEarningsForPayPeriod21() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod21");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 22.
@param EarningsForPayPeriod22 Earnings Period 22 */
public void setEarningsForPayPeriod22 (BigDecimal EarningsForPayPeriod22)
{
set_Value ("EarningsForPayPeriod22", EarningsForPayPeriod22);
}
/** Get Earnings Period 22.
@return Earnings Period 22 */
public BigDecimal getEarningsForPayPeriod22() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod22");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 23.
@param EarningsForPayPeriod23 Earnings Period 23 */
public void setEarningsForPayPeriod23 (BigDecimal EarningsForPayPeriod23)
{
set_Value ("EarningsForPayPeriod23", EarningsForPayPeriod23);
}
/** Get Earnings Period 23.
@return Earnings Period 23 */
public BigDecimal getEarningsForPayPeriod23() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod23");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 24.
@param EarningsForPayPeriod24 Earnings Period 24 */
public void setEarningsForPayPeriod24 (BigDecimal EarningsForPayPeriod24)
{
set_Value ("EarningsForPayPeriod24", EarningsForPayPeriod24);
}
/** Get Earnings Period 24.
@return Earnings Period 24 */
public BigDecimal getEarningsForPayPeriod24() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod24");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 25.
@param EarningsForPayPeriod25 Earnings Period 25 */
public void setEarningsForPayPeriod25 (BigDecimal EarningsForPayPeriod25)
{
set_Value ("EarningsForPayPeriod25", EarningsForPayPeriod25);
}
/** Get Earnings Period 25.
@return Earnings Period 25 */
public BigDecimal getEarningsForPayPeriod25() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod25");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 26.
@param EarningsForPayPeriod26 Earnings Period 26 */
public void setEarningsForPayPeriod26 (BigDecimal EarningsForPayPeriod26)
{
set_Value ("EarningsForPayPeriod26", EarningsForPayPeriod26);
}
/** Get Earnings Period 26.
@return Earnings Period 26 */
public BigDecimal getEarningsForPayPeriod26() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod26");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 27.
@param EarningsForPayPeriod27 Earnings Period 27 */
public void setEarningsForPayPeriod27 (BigDecimal EarningsForPayPeriod27)
{
set_Value ("EarningsForPayPeriod27", EarningsForPayPeriod27);
}
/** Get Earnings Period 27.
@return Earnings Period 27 */
public BigDecimal getEarningsForPayPeriod27() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod27");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 28.
@param EarningsForPayPeriod28 Earnings Period 28 */
public void setEarningsForPayPeriod28 (BigDecimal EarningsForPayPeriod28)
{
set_Value ("EarningsForPayPeriod28", EarningsForPayPeriod28);
}
/** Get Earnings Period 28.
@return Earnings Period 28 */
public BigDecimal getEarningsForPayPeriod28() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod28");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 29.
@param EarningsForPayPeriod29 Earnings Period 29 */
public void setEarningsForPayPeriod29 (BigDecimal EarningsForPayPeriod29)
{
set_Value ("EarningsForPayPeriod29", EarningsForPayPeriod29);
}
/** Get Earnings Period 29.
@return Earnings Period 29 */
public BigDecimal getEarningsForPayPeriod29() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod29");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 30.
@param EarningsForPayPeriod30 Earnings Period 30 */
public void setEarningsForPayPeriod30 (BigDecimal EarningsForPayPeriod30)
{
set_Value ("EarningsForPayPeriod30", EarningsForPayPeriod30);
}
/** Get Earnings Period 30.
@return Earnings Period 30 */
public BigDecimal getEarningsForPayPeriod30() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod30");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 31.
@param EarningsForPayPeriod31 Earnings Period 31 */
public void setEarningsForPayPeriod31 (BigDecimal EarningsForPayPeriod31)
{
set_Value ("EarningsForPayPeriod31", EarningsForPayPeriod31);
}
/** Get Earnings Period 31.
@return Earnings Period 31 */
public BigDecimal getEarningsForPayPeriod31() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod31");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 32.
@param EarningsForPayPeriod32 Earnings Period 32 */
public void setEarningsForPayPeriod32 (BigDecimal EarningsForPayPeriod32)
{
set_Value ("EarningsForPayPeriod32", EarningsForPayPeriod32);
}
/** Get Earnings Period 32.
@return Earnings Period 32 */
public BigDecimal getEarningsForPayPeriod32() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod32");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 33.
@param EarningsForPayPeriod33 Earnings Period 33 */
public void setEarningsForPayPeriod33 (BigDecimal EarningsForPayPeriod33)
{
set_Value ("EarningsForPayPeriod33", EarningsForPayPeriod33);
}
/** Get Earnings Period 33.
@return Earnings Period 33 */
public BigDecimal getEarningsForPayPeriod33() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod33");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 34.
@param EarningsForPayPeriod34 Earnings Period 34 */
public void setEarningsForPayPeriod34 (BigDecimal EarningsForPayPeriod34)
{
set_Value ("EarningsForPayPeriod34", EarningsForPayPeriod34);
}
/** Get Earnings Period 34.
@return Earnings Period 34 */
public BigDecimal getEarningsForPayPeriod34() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod34");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 35.
@param EarningsForPayPeriod35 Earnings Period 35 */
public void setEarningsForPayPeriod35 (BigDecimal EarningsForPayPeriod35)
{
set_Value ("EarningsForPayPeriod35", EarningsForPayPeriod35);
}
/** Get Earnings Period 35.
@return Earnings Period 35 */
public BigDecimal getEarningsForPayPeriod35() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod35");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 36.
@param EarningsForPayPeriod36 Earnings Period 36 */
public void setEarningsForPayPeriod36 (BigDecimal EarningsForPayPeriod36)
{
set_Value ("EarningsForPayPeriod36", EarningsForPayPeriod36);
}
/** Get Earnings Period 36.
@return Earnings Period 36 */
public BigDecimal getEarningsForPayPeriod36() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod36");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 37.
@param EarningsForPayPeriod37 Earnings Period 37 */
public void setEarningsForPayPeriod37 (BigDecimal EarningsForPayPeriod37)
{
set_Value ("EarningsForPayPeriod37", EarningsForPayPeriod37);
}
/** Get Earnings Period 37.
@return Earnings Period 37 */
public BigDecimal getEarningsForPayPeriod37() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod37");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 38.
@param EarningsForPayPeriod38 Earnings Period 38 */
public void setEarningsForPayPeriod38 (BigDecimal EarningsForPayPeriod38)
{
set_Value ("EarningsForPayPeriod38", EarningsForPayPeriod38);
}
/** Get Earnings Period 38.
@return Earnings Period 38 */
public BigDecimal getEarningsForPayPeriod38() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod38");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 39.
@param EarningsForPayPeriod39 Earnings Period 39 */
public void setEarningsForPayPeriod39 (BigDecimal EarningsForPayPeriod39)
{
set_Value ("EarningsForPayPeriod39", EarningsForPayPeriod39);
}
/** Get Earnings Period 39.
@return Earnings Period 39 */
public BigDecimal getEarningsForPayPeriod39() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod39");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 40.
@param EarningsForPayPeriod40 Earnings Period 40 */
public void setEarningsForPayPeriod40 (BigDecimal EarningsForPayPeriod40)
{
set_Value ("EarningsForPayPeriod40", EarningsForPayPeriod40);
}
/** Get Earnings Period 40.
@return Earnings Period 40 */
public BigDecimal getEarningsForPayPeriod40() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod40");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 41.
@param EarningsForPayPeriod41 Earnings Period 41 */
public void setEarningsForPayPeriod41 (BigDecimal EarningsForPayPeriod41)
{
set_Value ("EarningsForPayPeriod41", EarningsForPayPeriod41);
}
/** Get Earnings Period 41.
@return Earnings Period 41 */
public BigDecimal getEarningsForPayPeriod41() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod41");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 42.
@param EarningsForPayPeriod42 Earnings Period 42 */
public void setEarningsForPayPeriod42 (BigDecimal EarningsForPayPeriod42)
{
set_Value ("EarningsForPayPeriod42", EarningsForPayPeriod42);
}
/** Get Earnings Period 42.
@return Earnings Period 42 */
public BigDecimal getEarningsForPayPeriod42() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod42");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 43.
@param EarningsForPayPeriod43 Earnings Period 43 */
public void setEarningsForPayPeriod43 (BigDecimal EarningsForPayPeriod43)
{
set_Value ("EarningsForPayPeriod43", EarningsForPayPeriod43);
}
/** Get Earnings Period 43.
@return Earnings Period 43 */
public BigDecimal getEarningsForPayPeriod43() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod43");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 44.
@param EarningsForPayPeriod44 Earnings Period 44 */
public void setEarningsForPayPeriod44 (BigDecimal EarningsForPayPeriod44)
{
set_Value ("EarningsForPayPeriod44", EarningsForPayPeriod44);
}
/** Get Earnings Period 44.
@return Earnings Period 44 */
public BigDecimal getEarningsForPayPeriod44() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod44");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 45.
@param EarningsForPayPeriod45 Earnings Period 45 */
public void setEarningsForPayPeriod45 (BigDecimal EarningsForPayPeriod45)
{
set_Value ("EarningsForPayPeriod45", EarningsForPayPeriod45);
}
/** Get Earnings Period 45.
@return Earnings Period 45 */
public BigDecimal getEarningsForPayPeriod45() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod45");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 46.
@param EarningsForPayPeriod46 Earnings Period 46 */
public void setEarningsForPayPeriod46 (BigDecimal EarningsForPayPeriod46)
{
set_Value ("EarningsForPayPeriod46", EarningsForPayPeriod46);
}
/** Get Earnings Period 46.
@return Earnings Period 46 */
public BigDecimal getEarningsForPayPeriod46() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod46");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 47.
@param EarningsForPayPeriod47 Earnings Period 47 */
public void setEarningsForPayPeriod47 (BigDecimal EarningsForPayPeriod47)
{
set_Value ("EarningsForPayPeriod47", EarningsForPayPeriod47);
}
/** Get Earnings Period 47.
@return Earnings Period 47 */
public BigDecimal getEarningsForPayPeriod47() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod47");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 48.
@param EarningsForPayPeriod48 Earnings Period 48 */
public void setEarningsForPayPeriod48 (BigDecimal EarningsForPayPeriod48)
{
set_Value ("EarningsForPayPeriod48", EarningsForPayPeriod48);
}
/** Get Earnings Period 48.
@return Earnings Period 48 */
public BigDecimal getEarningsForPayPeriod48() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod48");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 49.
@param EarningsForPayPeriod49 Earnings Period 49 */
public void setEarningsForPayPeriod49 (BigDecimal EarningsForPayPeriod49)
{
set_Value ("EarningsForPayPeriod49", EarningsForPayPeriod49);
}
/** Get Earnings Period 49.
@return Earnings Period 49 */
public BigDecimal getEarningsForPayPeriod49() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod49");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 50.
@param EarningsForPayPeriod50 Earnings Period 50 */
public void setEarningsForPayPeriod50 (BigDecimal EarningsForPayPeriod50)
{
set_Value ("EarningsForPayPeriod50", EarningsForPayPeriod50);
}
/** Get Earnings Period 50.
@return Earnings Period 50 */
public BigDecimal getEarningsForPayPeriod50() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod50");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 51.
@param EarningsForPayPeriod51 Earnings Period 51 */
public void setEarningsForPayPeriod51 (BigDecimal EarningsForPayPeriod51)
{
set_Value ("EarningsForPayPeriod51", EarningsForPayPeriod51);
}
/** Get Earnings Period 51.
@return Earnings Period 51 */
public BigDecimal getEarningsForPayPeriod51() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod51");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 52.
@param EarningsForPayPeriod52 Earnings Period 52 */
public void setEarningsForPayPeriod52 (BigDecimal EarningsForPayPeriod52)
{
set_Value ("EarningsForPayPeriod52", EarningsForPayPeriod52);
}
/** Get Earnings Period 52.
@return Earnings Period 52 */
public BigDecimal getEarningsForPayPeriod52() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod52");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 53.
@param EarningsForPayPeriod53 Earnings Period 53 */
public void setEarningsForPayPeriod53 (BigDecimal EarningsForPayPeriod53)
{
set_Value ("EarningsForPayPeriod53", EarningsForPayPeriod53);
}
/** Get Earnings Period 53.
@return Earnings Period 53 */
public BigDecimal getEarningsForPayPeriod53() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod53");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earnings Period 54.
@param EarningsForPayPeriod54 Earnings Period 54 */
public void setEarningsForPayPeriod54 (BigDecimal EarningsForPayPeriod54)
{
set_Value ("EarningsForPayPeriod54", EarningsForPayPeriod54);
}
/** Get Earnings Period 54.
@return Earnings Period 54 */
public BigDecimal getEarningsForPayPeriod54() 
{
BigDecimal bd = (BigDecimal)get_Value("EarningsForPayPeriod54");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Occupation.
@param EmployeeOccupation Occupation */
public void setEmployeeOccupation (String EmployeeOccupation)
{
if (EmployeeOccupation != null && EmployeeOccupation.length() > 100)
{
log.warning("Length > 100 - truncated");
EmployeeOccupation = EmployeeOccupation.substring(0,99);
}
set_Value ("EmployeeOccupation", EmployeeOccupation);
}
/** Get Occupation.
@return Occupation */
public String getEmployeeOccupation() 
{
return (String)get_Value("EmployeeOccupation");
}
/** Set Expected Date of Recall.
@param ExpectedDateOfRecall Expected Date of Recall */
public void setExpectedDateOfRecall (Timestamp ExpectedDateOfRecall)
{
set_Value ("ExpectedDateOfRecall", ExpectedDateOfRecall);
}
/** Get Expected Date of Recall.
@return Expected Date of Recall */
public Timestamp getExpectedDateOfRecall() 
{
return (Timestamp)get_Value("ExpectedDateOfRecall");
}

/** ExpectedRecallCode AD_Reference_ID=2000069 */
public static final int EXPECTEDRECALLCODE_AD_Reference_ID=2000069;
/** Date of Recall = Y */
public static final String EXPECTEDRECALLCODE_DateOfRecall = "Y";
/** Not returning = N */
public static final String EXPECTEDRECALLCODE_NotReturning = "N";
/** Unknown = U */
public static final String EXPECTEDRECALLCODE_Unknown = "U";
/** Unspecified = S */
public static final String EXPECTEDRECALLCODE_Unspecified = "S";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isExpectedRecallCodeValid (String test)
{
return test == null || test.equals("Y") || test.equals("N") || test.equals("U") || test.equals("S");
}
/** Set Expected Recall Code.
@param ExpectedRecallCode Expected Recall Code */
public void setExpectedRecallCode (String ExpectedRecallCode)
{
if (!isExpectedRecallCodeValid(ExpectedRecallCode))
throw new IllegalArgumentException ("ExpectedRecallCode Invalid value - " + ExpectedRecallCode + " - Reference_ID=2000069 - Y - N - U - S");
if (ExpectedRecallCode != null && ExpectedRecallCode.length() > 1)
{
log.warning("Length > 1 - truncated");
ExpectedRecallCode = ExpectedRecallCode.substring(0,0);
}
set_Value ("ExpectedRecallCode", ExpectedRecallCode);
}
/** Get Expected Recall Code.
@return Expected Recall Code */
public String getExpectedRecallCode() 
{
return (String)get_Value("ExpectedRecallCode");
}
/** Set Final Pay - Period End Date.
@param FinalPayPeriodEndingDate Final Pay - Period End Date */
public void setFinalPayPeriodEndingDate (Timestamp FinalPayPeriodEndingDate)
{
set_Value ("FinalPayPeriodEndingDate", FinalPayPeriodEndingDate);
}
/** Get Final Pay - Period End Date.
@return Final Pay - Period End Date */
public Timestamp getFinalPayPeriodEndingDate() 
{
return (Timestamp)get_Value("FinalPayPeriodEndingDate");
}
/** Set First Day of Work.
@param FirstDayWorked First Day of Work */
public void setFirstDayWorked (Timestamp FirstDayWorked)
{
set_Value ("FirstDayWorked", FirstDayWorked);
}
/** Get First Day of Work.
@return First Day of Work */
public Timestamp getFirstDayWorked() 
{
return (Timestamp)get_Value("FirstDayWorked");
}
/** Set First Name.
@param FirstNameContactPerson First Name */
public void setFirstNameContactPerson (String FirstNameContactPerson)
{
if (FirstNameContactPerson != null && FirstNameContactPerson.length() > 30)
{
log.warning("Length > 30 - truncated");
FirstNameContactPerson = FirstNameContactPerson.substring(0,29);
}
set_Value ("FirstNameContactPerson", FirstNameContactPerson);
}
/** Get First Name.
@return First Name */
public String getFirstNameContactPerson() 
{
return (String)get_Value("FirstNameContactPerson");
}
/** Set isOvertimeIncluded.
@param isOvertimeIncluded isOvertimeIncluded */
public void setisOvertimeIncluded (boolean isOvertimeIncluded)
{
set_Value ("isOvertimeIncluded", new Boolean(isOvertimeIncluded));
}
/** Get isOvertimeIncluded.
@return isOvertimeIncluded */
public boolean isOvertimeIncluded() 
{
Object oo = get_Value("isOvertimeIncluded");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Last Day Paid.
@param LastDayForWhichPaid Last Day Paid */
public void setLastDayForWhichPaid (Timestamp LastDayForWhichPaid)
{
set_Value ("LastDayForWhichPaid", LastDayForWhichPaid);
}
/** Get Last Day Paid.
@return Last Day Paid */
public Timestamp getLastDayForWhichPaid() 
{
return (Timestamp)get_Value("LastDayForWhichPaid");
}
/** Set Surname.
@param LastNameContactPerson Surname */
public void setLastNameContactPerson (String LastNameContactPerson)
{
if (LastNameContactPerson != null && LastNameContactPerson.length() > 30)
{
log.warning("Length > 30 - truncated");
LastNameContactPerson = LastNameContactPerson.substring(0,29);
}
set_Value ("LastNameContactPerson", LastNameContactPerson);
}
/** Get Surname.
@return Surname */
public String getLastNameContactPerson() 
{
return (String)get_Value("LastNameContactPerson");
}
/** Set Other monies amount 1 .
@param OtherMoniesAmount01 Other monies amount 1  */
public void setOtherMoniesAmount01 (BigDecimal OtherMoniesAmount01)
{
set_Value ("OtherMoniesAmount01", OtherMoniesAmount01);
}
/** Get Other monies amount 1 .
@return Other monies amount 1  */
public BigDecimal getOtherMoniesAmount01() 
{
BigDecimal bd = (BigDecimal)get_Value("OtherMoniesAmount01");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Other monies amount 2.
@param OtherMoniesAmount02 Other monies amount 2 */
public void setOtherMoniesAmount02 (BigDecimal OtherMoniesAmount02)
{
set_Value ("OtherMoniesAmount02", OtherMoniesAmount02);
}
/** Get Other monies amount 2.
@return Other monies amount 2 */
public BigDecimal getOtherMoniesAmount02() 
{
BigDecimal bd = (BigDecimal)get_Value("OtherMoniesAmount02");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Other monies amount 3.
@param OtherMoniesAmount03 Other monies amount 3 */
public void setOtherMoniesAmount03 (BigDecimal OtherMoniesAmount03)
{
set_Value ("OtherMoniesAmount03", OtherMoniesAmount03);
}
/** Get Other monies amount 3.
@return Other monies amount 3 */
public BigDecimal getOtherMoniesAmount03() 
{
BigDecimal bd = (BigDecimal)get_Value("OtherMoniesAmount03");
if (bd == null) return Env.ZERO;
return bd;
}

/** OtherMoniesCode01 AD_Reference_ID=2000070 */
public static final int OTHERMONIESCODE01_AD_Reference_ID=2000070;
/** Anniversary Payout = A */
public static final String OTHERMONIESCODE01_AnniversaryPayout = "A";
/** Premium = B */
public static final String OTHERMONIESCODE01_Premium = "B";
/** Severance Pay = E */
public static final String OTHERMONIESCODE01_SeverancePay = "E";
/** Gratuities = G */
public static final String OTHERMONIESCODE01_Gratuities = "G";
/** Honorariums = H */
public static final String OTHERMONIESCODE01_Honorariums = "H";
/** Sick Leave Credits = I */
public static final String OTHERMONIESCODE01_SickLeaveCredits = "I";
/** Pension(s) = N */
public static final String OTHERMONIESCODE01_PensionS = "N";
/** Other = O */
public static final String OTHERMONIESCODE01_Other = "O";
/** Retirement Leave Credits = R */
public static final String OTHERMONIESCODE01_RetirementLeaveCredits = "R";
/** Settlement Pay = S */
public static final String OTHERMONIESCODE01_SettlementPay = "S";
/** Supplemental Unemployment Benefits = U */
public static final String OTHERMONIESCODE01_SupplementalUnemploymentBenefits = "U";
/** Vacation Pay = V */
public static final String OTHERMONIESCODE01_VacationPay = "V";
/** Pay in Lieu of Notice = Y */
public static final String OTHERMONIESCODE01_PayInLieuOfNotice = "Y";
/** Prime(jours fériés) = B05 */
public static final String OTHERMONIESCODE01_PrimeJoursFériés = "B05";
/** Prime(pour récompenser le rendement/la productivité) = B06 */
public static final String OTHERMONIESCODE01_PrimePourRécompenserLeRendementLaProductivité = "B06";
/** Prime (événements particuliers) = B07 */
public static final String OTHERMONIESCODE01_PrimeÉvénementsParticuliers = "B07";
/** Prime (engagement/fin de contrat/fin de saison) = B08 */
public static final String OTHERMONIESCODE01_PrimeEngagementFinDeContratFinDeSaison = "B08";
/** Prime (départ ou retraite) = B09 */
public static final String OTHERMONIESCODE01_PrimeDépartOuRetraite = "B09";
/** Prime(fermeture) = B10 */
public static final String OTHERMONIESCODE01_PrimeFermeture = "B10";
/** Prime (autre) = B11 */
public static final String OTHERMONIESCODE01_PrimeAutre = "B11";
/** Indemnité de départ = E00 */
public static final String OTHERMONIESCODE01_IndemnitéDeDépart = "E00";
/** Gratifications = G00 */
public static final String OTHERMONIESCODE01_Gratifications = "G00";
/** Honoraires = H00 */
public static final String OTHERMONIESCODE01_Honoraires = "H00";
/** Crédits de congé de maladie = I00 */
public static final String OTHERMONIESCODE01_CréditsDeCongéDeMaladie = "I00";
/** Ajustement rétroactif de paie = J00 */
public static final String OTHERMONIESCODE01_AjustementRétroactifDePaie = "J00";
/** Autre = O00 */
public static final String OTHERMONIESCODE01_Autre = "O00";
/** Participation aux bénéfices = Q00 */
public static final String OTHERMONIESCODE01_ParticipationAuxBénéfices = "Q00";
/** Indemnité de retraite/crédits de congé de retraite = R00 */
public static final String OTHERMONIESCODE01_IndemnitéDeRetraiteCréditsDeCongéDeRetraite = "R00";
/** Règlement d'un différend = S00 */
public static final String OTHERMONIESCODE01_RèglementDUnDifférend = "S00";
/** Heures supplémentaires accumulées payées = T00 */
public static final String OTHERMONIESCODE01_HeuresSupplémentairesAccumuléesPayées = "T00";
/** PSC - Maternité/parental/compassion/parents d'enfants m. = U12 */
public static final String OTHERMONIESCODE01_PSC_MaternitéParentalCompassionParentsDEnfantsM = "U12";
/** PSC Mise à pied = U13 */
public static final String OTHERMONIESCODE01_PSCMiseÀPied = "U13";
/** PSC Maladie = U14 */
public static final String OTHERMONIESCODE01_PSCMaladie = "U14";
/** PSC Formation = U15 */
public static final String OTHERMONIESCODE01_PSCFormation = "U15";
/** Indemnité de préavis = Y00 */
public static final String OTHERMONIESCODE01_IndemnitéDePréavis = "Y00";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isOtherMoniesCode01Valid (String test)
{
return test == null || test.equals("A") || test.equals("B") || test.equals("E") || test.equals("G") || test.equals("H") || test.equals("I") || test.equals("N") || test.equals("O") || test.equals("R") || test.equals("S") || test.equals("U") || test.equals("V") || test.equals("Y") || test.equals("B05") || test.equals("B06") || test.equals("B07") || test.equals("B08") || test.equals("B09") || test.equals("B10") || test.equals("B11") || test.equals("E00") || test.equals("G00") || test.equals("H00") || test.equals("I00") || test.equals("J00") || test.equals("O00") || test.equals("Q00") || test.equals("R00") || test.equals("S00") || test.equals("T00") || test.equals("U12") || test.equals("U13") || test.equals("U14") || test.equals("U15") || test.equals("Y00");
}
/** Set Other monies code 1.
@param OtherMoniesCode01 Other monies code 1 */
public void setOtherMoniesCode01 (String OtherMoniesCode01)
{
if (!isOtherMoniesCode01Valid(OtherMoniesCode01))
throw new IllegalArgumentException ("OtherMoniesCode01 Invalid value - " + OtherMoniesCode01 + " - Reference_ID=2000070 - A - B - E - G - H - I - N - O - R - S - U - V - Y - B05 - B06 - B07 - B08 - B09 - B10 - B11 - E00 - G00 - H00 - I00 - J00 - O00 - Q00 - R00 - S00 - T00 - U12 - U13 - U14 - U15 - Y00");
if (OtherMoniesCode01 != null && OtherMoniesCode01.length() > 3)
{
log.warning("Length > 3 - truncated");
OtherMoniesCode01 = OtherMoniesCode01.substring(0,2);
}
set_Value ("OtherMoniesCode01", OtherMoniesCode01);
}
/** Get Other monies code 1.
@return Other monies code 1 */
public String getOtherMoniesCode01() 
{
return (String)get_Value("OtherMoniesCode01");
}

/** OtherMoniesCode02 AD_Reference_ID=2000070 */
public static final int OTHERMONIESCODE02_AD_Reference_ID=2000070;
/** Anniversary Payout = A */
public static final String OTHERMONIESCODE02_AnniversaryPayout = "A";
/** Premium = B */
public static final String OTHERMONIESCODE02_Premium = "B";
/** Severance Pay = E */
public static final String OTHERMONIESCODE02_SeverancePay = "E";
/** Gratuities = G */
public static final String OTHERMONIESCODE02_Gratuities = "G";
/** Honorariums = H */
public static final String OTHERMONIESCODE02_Honorariums = "H";
/** Sick Leave Credits = I */
public static final String OTHERMONIESCODE02_SickLeaveCredits = "I";
/** Pension(s) = N */
public static final String OTHERMONIESCODE02_PensionS = "N";
/** Other = O */
public static final String OTHERMONIESCODE02_Other = "O";
/** Retirement Leave Credits = R */
public static final String OTHERMONIESCODE02_RetirementLeaveCredits = "R";
/** Settlement Pay = S */
public static final String OTHERMONIESCODE02_SettlementPay = "S";
/** Supplemental Unemployment Benefits = U */
public static final String OTHERMONIESCODE02_SupplementalUnemploymentBenefits = "U";
/** Vacation Pay = V */
public static final String OTHERMONIESCODE02_VacationPay = "V";
/** Pay in Lieu of Notice = Y */
public static final String OTHERMONIESCODE02_PayInLieuOfNotice = "Y";
/** Prime(jours fériés) = B05 */
public static final String OTHERMONIESCODE02_PrimeJoursFériés = "B05";
/** Prime(pour récompenser le rendement/la productivité) = B06 */
public static final String OTHERMONIESCODE02_PrimePourRécompenserLeRendementLaProductivité = "B06";
/** Prime (événements particuliers) = B07 */
public static final String OTHERMONIESCODE02_PrimeÉvénementsParticuliers = "B07";
/** Prime (engagement/fin de contrat/fin de saison) = B08 */
public static final String OTHERMONIESCODE02_PrimeEngagementFinDeContratFinDeSaison = "B08";
/** Prime (départ ou retraite) = B09 */
public static final String OTHERMONIESCODE02_PrimeDépartOuRetraite = "B09";
/** Prime(fermeture) = B10 */
public static final String OTHERMONIESCODE02_PrimeFermeture = "B10";
/** Prime (autre) = B11 */
public static final String OTHERMONIESCODE02_PrimeAutre = "B11";
/** Indemnité de départ = E00 */
public static final String OTHERMONIESCODE02_IndemnitéDeDépart = "E00";
/** Gratifications = G00 */
public static final String OTHERMONIESCODE02_Gratifications = "G00";
/** Honoraires = H00 */
public static final String OTHERMONIESCODE02_Honoraires = "H00";
/** Crédits de congé de maladie = I00 */
public static final String OTHERMONIESCODE02_CréditsDeCongéDeMaladie = "I00";
/** Ajustement rétroactif de paie = J00 */
public static final String OTHERMONIESCODE02_AjustementRétroactifDePaie = "J00";
/** Autre = O00 */
public static final String OTHERMONIESCODE02_Autre = "O00";
/** Participation aux bénéfices = Q00 */
public static final String OTHERMONIESCODE02_ParticipationAuxBénéfices = "Q00";
/** Indemnité de retraite/crédits de congé de retraite = R00 */
public static final String OTHERMONIESCODE02_IndemnitéDeRetraiteCréditsDeCongéDeRetraite = "R00";
/** Règlement d'un différend = S00 */
public static final String OTHERMONIESCODE02_RèglementDUnDifférend = "S00";
/** Heures supplémentaires accumulées payées = T00 */
public static final String OTHERMONIESCODE02_HeuresSupplémentairesAccumuléesPayées = "T00";
/** PSC - Maternité/parental/compassion/parents d'enfants m. = U12 */
public static final String OTHERMONIESCODE02_PSC_MaternitéParentalCompassionParentsDEnfantsM = "U12";
/** PSC Mise à pied = U13 */
public static final String OTHERMONIESCODE02_PSCMiseÀPied = "U13";
/** PSC Maladie = U14 */
public static final String OTHERMONIESCODE02_PSCMaladie = "U14";
/** PSC Formation = U15 */
public static final String OTHERMONIESCODE02_PSCFormation = "U15";
/** Indemnité de préavis = Y00 */
public static final String OTHERMONIESCODE02_IndemnitéDePréavis = "Y00";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isOtherMoniesCode02Valid (String test)
{
return test == null || test.equals("A") || test.equals("B") || test.equals("E") || test.equals("G") || test.equals("H") || test.equals("I") || test.equals("N") || test.equals("O") || test.equals("R") || test.equals("S") || test.equals("U") || test.equals("V") || test.equals("Y") || test.equals("B05") || test.equals("B06") || test.equals("B07") || test.equals("B08") || test.equals("B09") || test.equals("B10") || test.equals("B11") || test.equals("E00") || test.equals("G00") || test.equals("H00") || test.equals("I00") || test.equals("J00") || test.equals("O00") || test.equals("Q00") || test.equals("R00") || test.equals("S00") || test.equals("T00") || test.equals("U12") || test.equals("U13") || test.equals("U14") || test.equals("U15") || test.equals("Y00");
}
/** Set Other monies code 2.
@param OtherMoniesCode02 Other monies code 2 */
public void setOtherMoniesCode02 (String OtherMoniesCode02)
{
if (!isOtherMoniesCode02Valid(OtherMoniesCode02))
throw new IllegalArgumentException ("OtherMoniesCode02 Invalid value - " + OtherMoniesCode02 + " - Reference_ID=2000070 - A - B - E - G - H - I - N - O - R - S - U - V - Y - B05 - B06 - B07 - B08 - B09 - B10 - B11 - E00 - G00 - H00 - I00 - J00 - O00 - Q00 - R00 - S00 - T00 - U12 - U13 - U14 - U15 - Y00");
if (OtherMoniesCode02 != null && OtherMoniesCode02.length() > 3)
{
log.warning("Length > 3 - truncated");
OtherMoniesCode02 = OtherMoniesCode02.substring(0,2);
}
set_Value ("OtherMoniesCode02", OtherMoniesCode02);
}
/** Get Other monies code 2.
@return Other monies code 2 */
public String getOtherMoniesCode02() 
{
return (String)get_Value("OtherMoniesCode02");
}

/** OtherMoniesCode03 AD_Reference_ID=2000070 */
public static final int OTHERMONIESCODE03_AD_Reference_ID=2000070;
/** Anniversary Payout = A */
public static final String OTHERMONIESCODE03_AnniversaryPayout = "A";
/** Premium = B */
public static final String OTHERMONIESCODE03_Premium = "B";
/** Severance Pay = E */
public static final String OTHERMONIESCODE03_SeverancePay = "E";
/** Gratuities = G */
public static final String OTHERMONIESCODE03_Gratuities = "G";
/** Honorariums = H */
public static final String OTHERMONIESCODE03_Honorariums = "H";
/** Sick Leave Credits = I */
public static final String OTHERMONIESCODE03_SickLeaveCredits = "I";
/** Pension(s) = N */
public static final String OTHERMONIESCODE03_PensionS = "N";
/** Other = O */
public static final String OTHERMONIESCODE03_Other = "O";
/** Retirement Leave Credits = R */
public static final String OTHERMONIESCODE03_RetirementLeaveCredits = "R";
/** Settlement Pay = S */
public static final String OTHERMONIESCODE03_SettlementPay = "S";
/** Supplemental Unemployment Benefits = U */
public static final String OTHERMONIESCODE03_SupplementalUnemploymentBenefits = "U";
/** Vacation Pay = V */
public static final String OTHERMONIESCODE03_VacationPay = "V";
/** Pay in Lieu of Notice = Y */
public static final String OTHERMONIESCODE03_PayInLieuOfNotice = "Y";
/** Prime(jours fériés) = B05 */
public static final String OTHERMONIESCODE03_PrimeJoursFériés = "B05";
/** Prime(pour récompenser le rendement/la productivité) = B06 */
public static final String OTHERMONIESCODE03_PrimePourRécompenserLeRendementLaProductivité = "B06";
/** Prime (événements particuliers) = B07 */
public static final String OTHERMONIESCODE03_PrimeÉvénementsParticuliers = "B07";
/** Prime (engagement/fin de contrat/fin de saison) = B08 */
public static final String OTHERMONIESCODE03_PrimeEngagementFinDeContratFinDeSaison = "B08";
/** Prime (départ ou retraite) = B09 */
public static final String OTHERMONIESCODE03_PrimeDépartOuRetraite = "B09";
/** Prime(fermeture) = B10 */
public static final String OTHERMONIESCODE03_PrimeFermeture = "B10";
/** Prime (autre) = B11 */
public static final String OTHERMONIESCODE03_PrimeAutre = "B11";
/** Indemnité de départ = E00 */
public static final String OTHERMONIESCODE03_IndemnitéDeDépart = "E00";
/** Gratifications = G00 */
public static final String OTHERMONIESCODE03_Gratifications = "G00";
/** Honoraires = H00 */
public static final String OTHERMONIESCODE03_Honoraires = "H00";
/** Crédits de congé de maladie = I00 */
public static final String OTHERMONIESCODE03_CréditsDeCongéDeMaladie = "I00";
/** Ajustement rétroactif de paie = J00 */
public static final String OTHERMONIESCODE03_AjustementRétroactifDePaie = "J00";
/** Autre = O00 */
public static final String OTHERMONIESCODE03_Autre = "O00";
/** Participation aux bénéfices = Q00 */
public static final String OTHERMONIESCODE03_ParticipationAuxBénéfices = "Q00";
/** Indemnité de retraite/crédits de congé de retraite = R00 */
public static final String OTHERMONIESCODE03_IndemnitéDeRetraiteCréditsDeCongéDeRetraite = "R00";
/** Règlement d'un différend = S00 */
public static final String OTHERMONIESCODE03_RèglementDUnDifférend = "S00";
/** Heures supplémentaires accumulées payées = T00 */
public static final String OTHERMONIESCODE03_HeuresSupplémentairesAccumuléesPayées = "T00";
/** PSC - Maternité/parental/compassion/parents d'enfants m. = U12 */
public static final String OTHERMONIESCODE03_PSC_MaternitéParentalCompassionParentsDEnfantsM = "U12";
/** PSC Mise à pied = U13 */
public static final String OTHERMONIESCODE03_PSCMiseÀPied = "U13";
/** PSC Maladie = U14 */
public static final String OTHERMONIESCODE03_PSCMaladie = "U14";
/** PSC Formation = U15 */
public static final String OTHERMONIESCODE03_PSCFormation = "U15";
/** Indemnité de préavis = Y00 */
public static final String OTHERMONIESCODE03_IndemnitéDePréavis = "Y00";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isOtherMoniesCode03Valid (String test)
{
return test == null || test.equals("A") || test.equals("B") || test.equals("E") || test.equals("G") || test.equals("H") || test.equals("I") || test.equals("N") || test.equals("O") || test.equals("R") || test.equals("S") || test.equals("U") || test.equals("V") || test.equals("Y") || test.equals("B05") || test.equals("B06") || test.equals("B07") || test.equals("B08") || test.equals("B09") || test.equals("B10") || test.equals("B11") || test.equals("E00") || test.equals("G00") || test.equals("H00") || test.equals("I00") || test.equals("J00") || test.equals("O00") || test.equals("Q00") || test.equals("R00") || test.equals("S00") || test.equals("T00") || test.equals("U12") || test.equals("U13") || test.equals("U14") || test.equals("U15") || test.equals("Y00");
}
/** Set Other monies code 3.
@param OtherMoniesCode03 Other monies code 3 */
public void setOtherMoniesCode03 (String OtherMoniesCode03)
{
if (!isOtherMoniesCode03Valid(OtherMoniesCode03))
throw new IllegalArgumentException ("OtherMoniesCode03 Invalid value - " + OtherMoniesCode03 + " - Reference_ID=2000070 - A - B - E - G - H - I - N - O - R - S - U - V - Y - B05 - B06 - B07 - B08 - B09 - B10 - B11 - E00 - G00 - H00 - I00 - J00 - O00 - Q00 - R00 - S00 - T00 - U12 - U13 - U14 - U15 - Y00");
if (OtherMoniesCode03 != null && OtherMoniesCode03.length() > 3)
{
log.warning("Length > 3 - truncated");
OtherMoniesCode03 = OtherMoniesCode03.substring(0,2);
}
set_Value ("OtherMoniesCode03", OtherMoniesCode03);
}
/** Get Other monies code 3.
@return Other monies code 3 */
public String getOtherMoniesCode03() 
{
return (String)get_Value("OtherMoniesCode03");
}
/** Set OtherMoniesEndDate01.
@param OtherMoniesEndDate01 OtherMoniesEndDate01 */
public void setOtherMoniesEndDate01 (Timestamp OtherMoniesEndDate01)
{
set_Value ("OtherMoniesEndDate01", OtherMoniesEndDate01);
}
/** Get OtherMoniesEndDate01.
@return OtherMoniesEndDate01 */
public Timestamp getOtherMoniesEndDate01() 
{
return (Timestamp)get_Value("OtherMoniesEndDate01");
}
/** Set OtherMoniesEndDate02.
@param OtherMoniesEndDate02 OtherMoniesEndDate02 */
public void setOtherMoniesEndDate02 (Timestamp OtherMoniesEndDate02)
{
set_Value ("OtherMoniesEndDate02", OtherMoniesEndDate02);
}
/** Get OtherMoniesEndDate02.
@return OtherMoniesEndDate02 */
public Timestamp getOtherMoniesEndDate02() 
{
return (Timestamp)get_Value("OtherMoniesEndDate02");
}
/** Set OtherMoniesEndDate03.
@param OtherMoniesEndDate03 OtherMoniesEndDate03 */
public void setOtherMoniesEndDate03 (Timestamp OtherMoniesEndDate03)
{
set_Value ("OtherMoniesEndDate03", OtherMoniesEndDate03);
}
/** Get OtherMoniesEndDate03.
@return OtherMoniesEndDate03 */
public Timestamp getOtherMoniesEndDate03() 
{
return (Timestamp)get_Value("OtherMoniesEndDate03");
}
/** Set OtherMoniesStartDate01.
@param OtherMoniesStartDate01 OtherMoniesStartDate01 */
public void setOtherMoniesStartDate01 (Timestamp OtherMoniesStartDate01)
{
set_Value ("OtherMoniesStartDate01", OtherMoniesStartDate01);
}
/** Get OtherMoniesStartDate01.
@return OtherMoniesStartDate01 */
public Timestamp getOtherMoniesStartDate01() 
{
return (Timestamp)get_Value("OtherMoniesStartDate01");
}
/** Set OtherMoniesStartDate02.
@param OtherMoniesStartDate02 OtherMoniesStartDate02 */
public void setOtherMoniesStartDate02 (Timestamp OtherMoniesStartDate02)
{
set_Value ("OtherMoniesStartDate02", OtherMoniesStartDate02);
}
/** Get OtherMoniesStartDate02.
@return OtherMoniesStartDate02 */
public Timestamp getOtherMoniesStartDate02() 
{
return (Timestamp)get_Value("OtherMoniesStartDate02");
}
/** Set OtherMoniesStartDate03.
@param OtherMoniesStartDate03 OtherMoniesStartDate03 */
public void setOtherMoniesStartDate03 (Timestamp OtherMoniesStartDate03)
{
set_Value ("OtherMoniesStartDate03", OtherMoniesStartDate03);
}
/** Get OtherMoniesStartDate03.
@return OtherMoniesStartDate03 */
public Timestamp getOtherMoniesStartDate03() 
{
return (Timestamp)get_Value("OtherMoniesStartDate03");
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee.
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
set_Value ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee.
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Serial Number.
@param P_Employee_Roe_ID Serial Number */
public void setP_Employee_Roe_ID (int P_Employee_Roe_ID)
{
if (P_Employee_Roe_ID < 1) throw new IllegalArgumentException ("P_Employee_Roe_ID is mandatory.");
set_Value ("P_Employee_Roe_ID", new Integer(P_Employee_Roe_ID));
}
/** Get Serial Number.
@return Serial Number */
public int getP_Employee_Roe_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Roe_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Employer.
@param P_Employer_ID Employer */
public void setP_Employer_ID (int P_Employer_ID)
{
if (P_Employer_ID <= 0) set_Value ("P_Employer_ID", null);
 else 
set_Value ("P_Employer_ID", new Integer(P_Employer_ID));
}
/** Get Employer.
@return Employer */
public int getP_Employer_ID() 
{
Integer ii = (Integer)get_Value("P_Employer_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Frequency.
@param P_Frequency_ID Frequency */
public void setP_Frequency_ID (int P_Frequency_ID)
{
if (P_Frequency_ID <= 0) set_Value ("P_Frequency_ID", null);
 else 
set_Value ("P_Frequency_ID", new Integer(P_Frequency_ID));
}
/** Get Frequency.
@return Frequency */
public int getP_Frequency_ID() 
{
Integer ii = (Integer)get_Value("P_Frequency_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Job Title.
@param P_Job_Title_ID   */
public void setP_Job_Title_ID (int P_Job_Title_ID)
{
if (P_Job_Title_ID <= 0) set_Value ("P_Job_Title_ID", null);
 else 
set_Value ("P_Job_Title_ID", new Integer(P_Job_Title_ID));
}
/** Get Job Title.
@return   */
public int getP_Job_Title_ID() 
{
Integer ii = (Integer)get_Value("P_Job_Title_ID");
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

/** P_Period_ID AD_Reference_ID=2000009 */
public static final int P_PERIOD_ID_AD_Reference_ID=2000009;
/** Set Period.
@param P_Period_ID Period */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID <= 0) set_Value ("P_Period_ID", null);
 else 
set_Value ("P_Period_ID", new Integer(P_Period_ID));
}
/** Get Period.
@return Period */
public int getP_Period_ID() 
{
Integer ii = (Integer)get_Value("P_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Paid Sick Amount.
@param PaidSickAmount Paid Sick Amount */
public void setPaidSickAmount (BigDecimal PaidSickAmount)
{
set_Value ("PaidSickAmount", PaidSickAmount);
}
/** Get Paid Sick Amount.
@return Paid Sick Amount */
public BigDecimal getPaidSickAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("PaidSickAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set First Paid Sick Day.
@param PaidSickDate First Paid Sick Day */
public void setPaidSickDate (Timestamp PaidSickDate)
{
set_Value ("PaidSickDate", PaidSickDate);
}
/** Get First Paid Sick Day.
@return First Paid Sick Day */
public Timestamp getPaidSickDate() 
{
return (Timestamp)get_Value("PaidSickDate");
}

/** PaidSickPeriod AD_Reference_ID=2000071 */
public static final int PAIDSICKPERIOD_AD_Reference_ID=2000071;
/** Per Day = D */
public static final String PAIDSICKPERIOD_PerDay = "D";
/** Per Week = W */
public static final String PAIDSICKPERIOD_PerWeek = "W";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPaidSickPeriodValid (String test)
{
return test == null || test.equals("D") || test.equals("W");
}
/** Set Paid Sick Leave.
@param PaidSickPeriod Paid Sick Leave */
public void setPaidSickPeriod (String PaidSickPeriod)
{
if (!isPaidSickPeriodValid(PaidSickPeriod))
throw new IllegalArgumentException ("PaidSickPeriod Invalid value - " + PaidSickPeriod + " - Reference_ID=2000071 - D - W");
if (PaidSickPeriod != null && PaidSickPeriod.length() > 1)
{
log.warning("Length > 1 - truncated");
PaidSickPeriod = PaidSickPeriod.substring(0,0);
}
set_Value ("PaidSickPeriod", PaidSickPeriod);
}
/** Get Paid Sick Leave.
@return Paid Sick Leave */
public String getPaidSickPeriod() 
{
return (String)get_Value("PaidSickPeriod");
}

/** PayPeriodType AD_Reference_ID=2000068 */
public static final int PAYPERIODTYPE_AD_Reference_ID=2000068;
/** Bi-weekly = B */
public static final String PAYPERIODTYPE_Bi_Weekly = "B";
/** Monthly = M */
public static final String PAYPERIODTYPE_Monthly = "M";
/** Monthly non-standard = O */
public static final String PAYPERIODTYPE_MonthlyNon_Standard = "O";
/** Semi-Monthly = S */
public static final String PAYPERIODTYPE_Semi_Monthly = "S";
/** Semi-Monthly non-standard = E */
public static final String PAYPERIODTYPE_Semi_MonthlyNon_Standard = "E";
/** Thirteen Per Year = H */
public static final String PAYPERIODTYPE_ThirteenPerYear = "H";
/** Weekly = W */
public static final String PAYPERIODTYPE_Weekly = "W";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPayPeriodTypeValid (String test)
{
return test.equals("B") || test.equals("M") || test.equals("O") || test.equals("S") || test.equals("E") || test.equals("H") || test.equals("W");
}
/** Set Pay Period Type.
@param PayPeriodType Pay Period Type */
public void setPayPeriodType (String PayPeriodType)
{
if (PayPeriodType == null) throw new IllegalArgumentException ("PayPeriodType is mandatory");
if (!isPayPeriodTypeValid(PayPeriodType))
throw new IllegalArgumentException ("PayPeriodType Invalid value - " + PayPeriodType + " - Reference_ID=2000068 - B - M - O - S - E - H - W");
if (PayPeriodType.length() > 1)
{
log.warning("Length > 1 - truncated");
PayPeriodType = PayPeriodType.substring(0,0);
}
set_Value ("PayPeriodType", PayPeriodType);
}
/** Get Pay Period Type.
@return Pay Period Type */
public String getPayPeriodType() 
{
return (String)get_Value("PayPeriodType");
}
/** Set Phone.
@param Phone Identifies a telephone number */
public void setPhone (String Phone)
{
if (Phone != null && Phone.length() > 30)
{
log.warning("Length > 30 - truncated");
Phone = Phone.substring(0,29);
}
set_Value ("Phone", Phone);
}
/** Get Phone.
@return Identifies a telephone number */
public String getPhone() 
{
return (String)get_Value("Phone");
}
/** Set 2nd Phone.
@param Phone2 Identifies an alternate telephone number. */
public void setPhone2 (String Phone2)
{
if (Phone2 != null && Phone2.length() > 30)
{
log.warning("Length > 30 - truncated");
Phone2 = Phone2.substring(0,29);
}
set_Value ("Phone2", Phone2);
}
/** Get 2nd Phone.
@return Identifies an alternate telephone number. */
public String getPhone2() 
{
return (String)get_Value("Phone2");
}
/** Set PhoneAreaCode.
@param PhoneAreaCode PhoneAreaCode */
public void setPhoneAreaCode (String PhoneAreaCode)
{
if (PhoneAreaCode != null && PhoneAreaCode.length() > 3)
{
log.warning("Length > 3 - truncated");
PhoneAreaCode = PhoneAreaCode.substring(0,2);
}
set_Value ("PhoneAreaCode", PhoneAreaCode);
}
/** Get PhoneAreaCode.
@return PhoneAreaCode */
public String getPhoneAreaCode() 
{
return (String)get_Value("PhoneAreaCode");
}
/** Set PhoneAreaCode2.
@param PhoneAreaCode2 PhoneAreaCode2 */
public void setPhoneAreaCode2 (String PhoneAreaCode2)
{
if (PhoneAreaCode2 != null && PhoneAreaCode2.length() > 3)
{
log.warning("Length > 3 - truncated");
PhoneAreaCode2 = PhoneAreaCode2.substring(0,2);
}
set_Value ("PhoneAreaCode2", PhoneAreaCode2);
}
/** Get PhoneAreaCode2.
@return PhoneAreaCode2 */
public String getPhoneAreaCode2() 
{
return (String)get_Value("PhoneAreaCode2");
}
/** Set Extention.
@param PhoneExt Extention */
public void setPhoneExt (String PhoneExt)
{
if (PhoneExt != null && PhoneExt.length() > 30)
{
log.warning("Length > 30 - truncated");
PhoneExt = PhoneExt.substring(0,29);
}
set_Value ("PhoneExt", PhoneExt);
}
/** Get Extention.
@return Extention */
public String getPhoneExt() 
{
return (String)get_Value("PhoneExt");
}
/** Set PhoneExt2.
@param PhoneExt2 PhoneExt2 */
public void setPhoneExt2 (String PhoneExt2)
{
if (PhoneExt2 != null && PhoneExt2.length() > 30)
{
log.warning("Length > 30 - truncated");
PhoneExt2 = PhoneExt2.substring(0,29);
}
set_Value ("PhoneExt2", PhoneExt2);
}
/** Get PhoneExt2.
@return PhoneExt2 */
public String getPhoneExt2() 
{
return (String)get_Value("PhoneExt2");
}
/** Set PrintLanguageID.
@param PrintLanguageID PrintLanguageID */
public void setPrintLanguageID (int PrintLanguageID)
{
set_Value ("PrintLanguageID", new Integer(PrintLanguageID));
}
/** Get PrintLanguageID.
@return PrintLanguageID */
public int getPrintLanguageID() 
{
Integer ii = (Integer)get_Value("PrintLanguageID");
if (ii == null) return 0;
return ii.intValue();
}

/** ReasonForIssuingThisRoe AD_Reference_ID=2100332 */
public static final int REASONFORISSUINGTHISROE_AD_Reference_ID=2100332;
/** A - Shortage of Work = A */
public static final String REASONFORISSUINGTHISROE_A_ShortageOfWork = "A";
/** B - Strike or Lockout = B */
public static final String REASONFORISSUINGTHISROE_B_StrikeOrLockout = "B";
/** C - Return to School = C */
public static final String REASONFORISSUINGTHISROE_C_ReturnToSchool = "C";
/** D - Illness or Injury = D */
public static final String REASONFORISSUINGTHISROE_D_IllnessOrInjury = "D";
/** E - Quit = E */
public static final String REASONFORISSUINGTHISROE_E_Quit = "E";
/** F - Pregnancy = F */
public static final String REASONFORISSUINGTHISROE_F_Pregnancy = "F";
/** G - Retirement = G */
public static final String REASONFORISSUINGTHISROE_G_Retirement = "G";
/** H - Work Sharing = H */
public static final String REASONFORISSUINGTHISROE_H_WorkSharing = "H";
/** J - Apprentice Training = J */
public static final String REASONFORISSUINGTHISROE_J_ApprenticeTraining = "J";
/** K - Other = K */
public static final String REASONFORISSUINGTHISROE_K_Other = "K";
/** M - Dismissal = M */
public static final String REASONFORISSUINGTHISROE_M_Dismissal = "M";
/** N - Leave of Absence = N */
public static final String REASONFORISSUINGTHISROE_N_LeaveOfAbsence = "N";
/** P - Parental = P */
public static final String REASONFORISSUINGTHISROE_P_Parental = "P";
/** Administrative Dismissal = Q */
public static final String REASONFORISSUINGTHISROE_AdministrativeDismissal = "Q";
/** R - Deceased = R */
public static final String REASONFORISSUINGTHISROE_R_Deceased = "R";
/** Disability Revocation) = S */
public static final String REASONFORISSUINGTHISROE_DisabilityRevocation = "S";
/** Suspension (recall casual employee) = T */
public static final String REASONFORISSUINGTHISROE_SuspensionRecallCasualEmployee = "T";
/** Suspension (recall permanent employee) = U */
public static final String REASONFORISSUINGTHISROE_SuspensionRecallPermanentEmployee = "U";
/** Suspension (no recall) = V */
public static final String REASONFORISSUINGTHISROE_SuspensionNoRecall = "V";
/** Return to Public Service Work = W */
public static final String REASONFORISSUINGTHISROE_ReturnToPublicServiceWork = "W";
/** Z - Compassionate care = Z */
public static final String REASONFORISSUINGTHISROE_Z_CompassionateCare = "Z";
/** A00 Manque de travail / Fin de saison ou de contrat = A00 */
public static final String REASONFORISSUINGTHISROE_A00ManqueDeTravailFinDeSaisonOuDeContrat = "A00";
/** A01 Faillite de l'employeur ou mise sous séquestre  = A01 */
public static final String REASONFORISSUINGTHISROE_A01FailliteDeLEmployeurOuMiseSousSéquestre = "A01";
/** B00 Grève ou lockout = B00 */
public static final String REASONFORISSUINGTHISROE_B00GrèveOuLockout = "B00";
/** D00 Maladie ou blessure = D00 */
public static final String REASONFORISSUINGTHISROE_D00MaladieOuBlessure = "D00";
/** E00 Départ volontaire = E00 */
public static final String REASONFORISSUINGTHISROE_E00DépartVolontaire = "E00";
/** E02 Départ volontaire / Pour accompagner un(e) conjoint(e)  = E02 */
public static final String REASONFORISSUINGTHISROE_E02DépartVolontairePourAccompagnerUnEConjointE = "E02";
/** E03 Départ volontaire / Retour aux études = E03 */
public static final String REASONFORISSUINGTHISROE_E03DépartVolontaireRetourAuxÉtudes = "E03";
/** E04 Départ volontaire / Raisons médicales = E04 */
public static final String REASONFORISSUINGTHISROE_E04DépartVolontaireRaisonsMédicales = "E04";
/** E05 Départ volontaire / Retraite volontaire = E05 */
public static final String REASONFORISSUINGTHISROE_E05DépartVolontaireRetraiteVolontaire = "E05";
/** E06 Départ volontaire / Autre emploi  = E06 */
public static final String REASONFORISSUINGTHISROE_E06DépartVolontaireAutreEmploi = "E06";
/** E09 Départ volontaire / Déménagement de l'employeur = E09 */
public static final String REASONFORISSUINGTHISROE_E09DépartVolontaireDéménagementDeLEmployeur = "E09";
/** E10 Départ volontaire / Prendre soin d'une personne à charge = E10 */
public static final String REASONFORISSUINGTHISROE_E10DépartVolontairePrendreSoinDUnePersonneÀCharge = "E10";
/** E11 Départ volontaire / Pour devenir travailleur indépendant = E11 */
public static final String REASONFORISSUINGTHISROE_E11DépartVolontairePourDevenirTravailleurIndépendant = "E11";
/** F00 Maternité = F00 */
public static final String REASONFORISSUINGTHISROE_F00Maternité = "F00";
/** G00 Retraite obligatoire  = G00 */
public static final String REASONFORISSUINGTHISROE_G00RetraiteObligatoire = "G00";
/** G07 Retraite Approuvée programme de compression du personnel = G07 */
public static final String REASONFORISSUINGTHISROE_G07RetraiteApprouvéeProgrammeDeCompressionDuPersonnel = "G07";
/** H00 Travail partagé = H00 */
public static final String REASONFORISSUINGTHISROE_H00TravailPartagé = "H00";
/** J00 Formation en apprentissage  = J00 */
public static final String REASONFORISSUINGTHISROE_J00FormationEnApprentissage = "J00";
/** K00 Autre = K00 */
public static final String REASONFORISSUINGTHISROE_K00Autre = "K00";
/** K12 Autre / Changement de la fréquence de paie  = K12 */
public static final String REASONFORISSUINGTHISROE_K12AutreChangementDeLaFréquenceDePaie = "K12";
/** K13 Autre / Changement de propriétaire  = K13 */
public static final String REASONFORISSUINGTHISROE_K13AutreChangementDePropriétaire = "K13";
/** K14 Autre / À la demande de l'assurance-emploi  = K14 */
public static final String REASONFORISSUINGTHISROE_K14AutreÀLaDemandeDeLAssurance_Emploi = "K14";
/** K15 Autre / Forces canadiennes - Ordonnances = K15 */
public static final String REASONFORISSUINGTHISROE_K15AutreForcesCanadiennes_Ordonnances = "K15";
/** K16 Autre / À la demande de l'employé(e)  = K16 */
public static final String REASONFORISSUINGTHISROE_K16AutreÀLaDemandeDeLEmployéE = "K16";
/** K17 Autre / Changement de fournisseur de service  = K17 */
public static final String REASONFORISSUINGTHISROE_K17AutreChangementDeFournisseurDeService = "K17";
/** M00 Congédiement  = M00 */
public static final String REASONFORISSUINGTHISROE_M00Congédiement = "M00";
/** M08 Congédiement avant la fin de la période de probation = M08 */
public static final String REASONFORISSUINGTHISROE_M08CongédiementAvantLaFinDeLaPériodeDeProbation = "M08";
/** N00 Congé = N00 */
public static final String REASONFORISSUINGTHISROE_N00Congé = "N00";
/** P00 Parental  = P00 */
public static final String REASONFORISSUINGTHISROE_P00Parental = "P00";
/** Z00 Congé de compassion = Z00 */
public static final String REASONFORISSUINGTHISROE_Z00CongéDeCompassion = "Z00";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isReasonForIssuingThisRoeValid (String test)
{
return test == null || test.equals("A00") || test.equals("A01") || test.equals("B00") || test.equals("D00") || test.equals("E00") || test.equals("E02") || test.equals("E03") || test.equals("E04") || test.equals("E05") || test.equals("E06") || test.equals("E09") || test.equals("E10") || test.equals("E11") || test.equals("F00") || test.equals("G00") || test.equals("G07") || test.equals("H00") || test.equals("J00") || test.equals("K00") || test.equals("K12") || test.equals("K13") || test.equals("K14") || test.equals("K15") || test.equals("K16") || test.equals("K17") || test.equals("M00") || test.equals("M08") || test.equals("N00") || test.equals("P00") || test.equals("Z00");
}
/** Set Reason for issuing this ROE.
@param ReasonForIssuingThisRoe Reason for issuing this ROE */
public void setReasonForIssuingThisRoe (String ReasonForIssuingThisRoe)
{
if (!isReasonForIssuingThisRoeValid(ReasonForIssuingThisRoe))
throw new IllegalArgumentException ("ReasonForIssuingThisRoe Invalid value - " + ReasonForIssuingThisRoe + " - Reference_ID=2100332 - A00 - A01 - B00 - D00 - E00 - E02 - E03 - E04 - E05 - E06 - E09 - E10 - E11 - F00 - G00 - G07 - H00 - J00 - K00 - K12 - K13 - K14 - K15 - K16 - K17 - M00 - M08 - N00 - P00 - Z00");
if (ReasonForIssuingThisRoe != null && ReasonForIssuingThisRoe.length() > 3)
{
log.warning("Length > 3 - truncated");
ReasonForIssuingThisRoe = ReasonForIssuingThisRoe.substring(0,2);
}
set_Value ("ReasonForIssuingThisRoe", ReasonForIssuingThisRoe);
}
/** Get Reason for issuing this ROE.
@return Reason for issuing this ROE */
public String getReasonForIssuingThisRoe() 
{
return (String)get_Value("ReasonForIssuingThisRoe");
}
/** Set Serial Number.
@param SerialNumber Serial Number */
public void setSerialNumber (String SerialNumber)
{
if (SerialNumber != null && SerialNumber.length() > 30)
{
log.warning("Length > 30 - truncated");
SerialNumber = SerialNumber.substring(0,29);
}
set_Value ("SerialNumber", SerialNumber);
}
/** Get Serial Number.
@return Serial Number */
public String getSerialNumber() 
{
return (String)get_Value("SerialNumber");
}
/** Set Signatory.
@param Signataire Signatory */
public void setSignataire (String Signataire)
{
if (Signataire != null && Signataire.length() > 60)
{
log.warning("Length > 60 - truncated");
Signataire = Signataire.substring(0,59);
}
set_Value ("Signataire", Signataire);
}
/** Get Signatory.
@return Signatory */
public String getSignataire() 
{
return (String)get_Value("Signataire");
}
/** Set Signatory.
@param Signataire2 Signatory */
public void setSignataire2 (String Signataire2)
{
if (Signataire2 != null && Signataire2.length() > 60)
{
log.warning("Length > 60 - truncated");
Signataire2 = Signataire2.substring(0,59);
}
set_Value ("Signataire2", Signataire2);
}
/** Get Signatory.
@return Signatory */
public String getSignataire2() 
{
return (String)get_Value("Signataire2");
}
/** Set SpecialPaymentAmount01.
@param SpecialPaymentAmount01 SpecialPaymentAmount01 */
public void setSpecialPaymentAmount01 (BigDecimal SpecialPaymentAmount01)
{
set_Value ("SpecialPaymentAmount01", SpecialPaymentAmount01);
}
/** Get SpecialPaymentAmount01.
@return SpecialPaymentAmount01 */
public BigDecimal getSpecialPaymentAmount01() 
{
BigDecimal bd = (BigDecimal)get_Value("SpecialPaymentAmount01");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set SpecialPaymentAmount02.
@param SpecialPaymentAmount02 SpecialPaymentAmount02 */
public void setSpecialPaymentAmount02 (BigDecimal SpecialPaymentAmount02)
{
set_Value ("SpecialPaymentAmount02", SpecialPaymentAmount02);
}
/** Get SpecialPaymentAmount02.
@return SpecialPaymentAmount02 */
public BigDecimal getSpecialPaymentAmount02() 
{
BigDecimal bd = (BigDecimal)get_Value("SpecialPaymentAmount02");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set SpecialPaymentAmount03.
@param SpecialPaymentAmount03 SpecialPaymentAmount03 */
public void setSpecialPaymentAmount03 (BigDecimal SpecialPaymentAmount03)
{
set_Value ("SpecialPaymentAmount03", SpecialPaymentAmount03);
}
/** Get SpecialPaymentAmount03.
@return SpecialPaymentAmount03 */
public BigDecimal getSpecialPaymentAmount03() 
{
BigDecimal bd = (BigDecimal)get_Value("SpecialPaymentAmount03");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set SpecialPaymentAmount04.
@param SpecialPaymentAmount04 SpecialPaymentAmount04 */
public void setSpecialPaymentAmount04 (BigDecimal SpecialPaymentAmount04)
{
set_Value ("SpecialPaymentAmount04", SpecialPaymentAmount04);
}
/** Get SpecialPaymentAmount04.
@return SpecialPaymentAmount04 */
public BigDecimal getSpecialPaymentAmount04() 
{
BigDecimal bd = (BigDecimal)get_Value("SpecialPaymentAmount04");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set SpecialPaymentEndDate01.
@param SpecialPaymentEndDate01 SpecialPaymentEndDate01 */
public void setSpecialPaymentEndDate01 (Timestamp SpecialPaymentEndDate01)
{
set_Value ("SpecialPaymentEndDate01", SpecialPaymentEndDate01);
}
/** Get SpecialPaymentEndDate01.
@return SpecialPaymentEndDate01 */
public Timestamp getSpecialPaymentEndDate01() 
{
return (Timestamp)get_Value("SpecialPaymentEndDate01");
}
/** Set SpecialPaymentEndDate02.
@param SpecialPaymentEndDate02 SpecialPaymentEndDate02 */
public void setSpecialPaymentEndDate02 (Timestamp SpecialPaymentEndDate02)
{
set_Value ("SpecialPaymentEndDate02", SpecialPaymentEndDate02);
}
/** Get SpecialPaymentEndDate02.
@return SpecialPaymentEndDate02 */
public Timestamp getSpecialPaymentEndDate02() 
{
return (Timestamp)get_Value("SpecialPaymentEndDate02");
}
/** Set SpecialPaymentEndDate03.
@param SpecialPaymentEndDate03 SpecialPaymentEndDate03 */
public void setSpecialPaymentEndDate03 (Timestamp SpecialPaymentEndDate03)
{
set_Value ("SpecialPaymentEndDate03", SpecialPaymentEndDate03);
}
/** Get SpecialPaymentEndDate03.
@return SpecialPaymentEndDate03 */
public Timestamp getSpecialPaymentEndDate03() 
{
return (Timestamp)get_Value("SpecialPaymentEndDate03");
}
/** Set SpecialPaymentEndDate04.
@param SpecialPaymentEndDate04 SpecialPaymentEndDate04 */
public void setSpecialPaymentEndDate04 (Timestamp SpecialPaymentEndDate04)
{
set_Value ("SpecialPaymentEndDate04", SpecialPaymentEndDate04);
}
/** Get SpecialPaymentEndDate04.
@return SpecialPaymentEndDate04 */
public Timestamp getSpecialPaymentEndDate04() 
{
return (Timestamp)get_Value("SpecialPaymentEndDate04");
}
/** Set SpecialPaymentPeriod01.
@param SpecialPaymentPeriod01 SpecialPaymentPeriod01 */
public void setSpecialPaymentPeriod01 (String SpecialPaymentPeriod01)
{
if (SpecialPaymentPeriod01 != null && SpecialPaymentPeriod01.length() > 1)
{
log.warning("Length > 1 - truncated");
SpecialPaymentPeriod01 = SpecialPaymentPeriod01.substring(0,0);
}
set_Value ("SpecialPaymentPeriod01", SpecialPaymentPeriod01);
}
/** Get SpecialPaymentPeriod01.
@return SpecialPaymentPeriod01 */
public String getSpecialPaymentPeriod01() 
{
return (String)get_Value("SpecialPaymentPeriod01");
}
/** Set SpecialPaymentPeriod02.
@param SpecialPaymentPeriod02 SpecialPaymentPeriod02 */
public void setSpecialPaymentPeriod02 (String SpecialPaymentPeriod02)
{
if (SpecialPaymentPeriod02 != null && SpecialPaymentPeriod02.length() > 1)
{
log.warning("Length > 1 - truncated");
SpecialPaymentPeriod02 = SpecialPaymentPeriod02.substring(0,0);
}
set_Value ("SpecialPaymentPeriod02", SpecialPaymentPeriod02);
}
/** Get SpecialPaymentPeriod02.
@return SpecialPaymentPeriod02 */
public String getSpecialPaymentPeriod02() 
{
return (String)get_Value("SpecialPaymentPeriod02");
}
/** Set SpecialPaymentPeriod03.
@param SpecialPaymentPeriod03 SpecialPaymentPeriod03 */
public void setSpecialPaymentPeriod03 (String SpecialPaymentPeriod03)
{
if (SpecialPaymentPeriod03 != null && SpecialPaymentPeriod03.length() > 1)
{
log.warning("Length > 1 - truncated");
SpecialPaymentPeriod03 = SpecialPaymentPeriod03.substring(0,0);
}
set_Value ("SpecialPaymentPeriod03", SpecialPaymentPeriod03);
}
/** Get SpecialPaymentPeriod03.
@return SpecialPaymentPeriod03 */
public String getSpecialPaymentPeriod03() 
{
return (String)get_Value("SpecialPaymentPeriod03");
}
/** Set SpecialPaymentPeriod04.
@param SpecialPaymentPeriod04 SpecialPaymentPeriod04 */
public void setSpecialPaymentPeriod04 (String SpecialPaymentPeriod04)
{
if (SpecialPaymentPeriod04 != null && SpecialPaymentPeriod04.length() > 1)
{
log.warning("Length > 1 - truncated");
SpecialPaymentPeriod04 = SpecialPaymentPeriod04.substring(0,0);
}
set_Value ("SpecialPaymentPeriod04", SpecialPaymentPeriod04);
}
/** Get SpecialPaymentPeriod04.
@return SpecialPaymentPeriod04 */
public String getSpecialPaymentPeriod04() 
{
return (String)get_Value("SpecialPaymentPeriod04");
}
/** Set SpecialPaymentPeriodType01.
@param SpecialPaymentPeriodType01 SpecialPaymentPeriodType01 */
public void setSpecialPaymentPeriodType01 (String SpecialPaymentPeriodType01)
{
if (SpecialPaymentPeriodType01 != null && SpecialPaymentPeriodType01.length() > 1)
{
log.warning("Length > 1 - truncated");
SpecialPaymentPeriodType01 = SpecialPaymentPeriodType01.substring(0,0);
}
set_Value ("SpecialPaymentPeriodType01", SpecialPaymentPeriodType01);
}
/** Get SpecialPaymentPeriodType01.
@return SpecialPaymentPeriodType01 */
public String getSpecialPaymentPeriodType01() 
{
return (String)get_Value("SpecialPaymentPeriodType01");
}
/** Set SpecialPaymentPeriodType02.
@param SpecialPaymentPeriodType02 SpecialPaymentPeriodType02 */
public void setSpecialPaymentPeriodType02 (String SpecialPaymentPeriodType02)
{
if (SpecialPaymentPeriodType02 != null && SpecialPaymentPeriodType02.length() > 1)
{
log.warning("Length > 1 - truncated");
SpecialPaymentPeriodType02 = SpecialPaymentPeriodType02.substring(0,0);
}
set_Value ("SpecialPaymentPeriodType02", SpecialPaymentPeriodType02);
}
/** Get SpecialPaymentPeriodType02.
@return SpecialPaymentPeriodType02 */
public String getSpecialPaymentPeriodType02() 
{
return (String)get_Value("SpecialPaymentPeriodType02");
}
/** Set SpecialPaymentPeriodType03.
@param SpecialPaymentPeriodType03 SpecialPaymentPeriodType03 */
public void setSpecialPaymentPeriodType03 (String SpecialPaymentPeriodType03)
{
if (SpecialPaymentPeriodType03 != null && SpecialPaymentPeriodType03.length() > 1)
{
log.warning("Length > 1 - truncated");
SpecialPaymentPeriodType03 = SpecialPaymentPeriodType03.substring(0,0);
}
set_Value ("SpecialPaymentPeriodType03", SpecialPaymentPeriodType03);
}
/** Get SpecialPaymentPeriodType03.
@return SpecialPaymentPeriodType03 */
public String getSpecialPaymentPeriodType03() 
{
return (String)get_Value("SpecialPaymentPeriodType03");
}
/** Set SpecialPaymentPeriodType04.
@param SpecialPaymentPeriodType04 SpecialPaymentPeriodType04 */
public void setSpecialPaymentPeriodType04 (String SpecialPaymentPeriodType04)
{
if (SpecialPaymentPeriodType04 != null && SpecialPaymentPeriodType04.length() > 1)
{
log.warning("Length > 1 - truncated");
SpecialPaymentPeriodType04 = SpecialPaymentPeriodType04.substring(0,0);
}
set_Value ("SpecialPaymentPeriodType04", SpecialPaymentPeriodType04);
}
/** Get SpecialPaymentPeriodType04.
@return SpecialPaymentPeriodType04 */
public String getSpecialPaymentPeriodType04() 
{
return (String)get_Value("SpecialPaymentPeriodType04");
}
/** Set SpecialPaymentStartDate01.
@param SpecialPaymentStartDate01 SpecialPaymentStartDate01 */
public void setSpecialPaymentStartDate01 (Timestamp SpecialPaymentStartDate01)
{
set_Value ("SpecialPaymentStartDate01", SpecialPaymentStartDate01);
}
/** Get SpecialPaymentStartDate01.
@return SpecialPaymentStartDate01 */
public Timestamp getSpecialPaymentStartDate01() 
{
return (Timestamp)get_Value("SpecialPaymentStartDate01");
}
/** Set SpecialPaymentStartDate02.
@param SpecialPaymentStartDate02 SpecialPaymentStartDate02 */
public void setSpecialPaymentStartDate02 (Timestamp SpecialPaymentStartDate02)
{
set_Value ("SpecialPaymentStartDate02", SpecialPaymentStartDate02);
}
/** Get SpecialPaymentStartDate02.
@return SpecialPaymentStartDate02 */
public Timestamp getSpecialPaymentStartDate02() 
{
return (Timestamp)get_Value("SpecialPaymentStartDate02");
}
/** Set SpecialPaymentStartDate03.
@param SpecialPaymentStartDate03 SpecialPaymentStartDate03 */
public void setSpecialPaymentStartDate03 (Timestamp SpecialPaymentStartDate03)
{
set_Value ("SpecialPaymentStartDate03", SpecialPaymentStartDate03);
}
/** Get SpecialPaymentStartDate03.
@return SpecialPaymentStartDate03 */
public Timestamp getSpecialPaymentStartDate03() 
{
return (Timestamp)get_Value("SpecialPaymentStartDate03");
}
/** Set SpecialPaymentStartDate04.
@param SpecialPaymentStartDate04 SpecialPaymentStartDate04 */
public void setSpecialPaymentStartDate04 (Timestamp SpecialPaymentStartDate04)
{
set_Value ("SpecialPaymentStartDate04", SpecialPaymentStartDate04);
}
/** Get SpecialPaymentStartDate04.
@return SpecialPaymentStartDate04 */
public Timestamp getSpecialPaymentStartDate04() 
{
return (Timestamp)get_Value("SpecialPaymentStartDate04");
}
/** Set SpecialPaymentType01.
@param SpecialPaymentType01 SpecialPaymentType01 */
public void setSpecialPaymentType01 (String SpecialPaymentType01)
{
if (SpecialPaymentType01 != null && SpecialPaymentType01.length() > 3)
{
log.warning("Length > 3 - truncated");
SpecialPaymentType01 = SpecialPaymentType01.substring(0,2);
}
set_Value ("SpecialPaymentType01", SpecialPaymentType01);
}
/** Get SpecialPaymentType01.
@return SpecialPaymentType01 */
public String getSpecialPaymentType01() 
{
return (String)get_Value("SpecialPaymentType01");
}
/** Set SpecialPaymentType02.
@param SpecialPaymentType02 SpecialPaymentType02 */
public void setSpecialPaymentType02 (String SpecialPaymentType02)
{
if (SpecialPaymentType02 != null && SpecialPaymentType02.length() > 3)
{
log.warning("Length > 3 - truncated");
SpecialPaymentType02 = SpecialPaymentType02.substring(0,2);
}
set_Value ("SpecialPaymentType02", SpecialPaymentType02);
}
/** Get SpecialPaymentType02.
@return SpecialPaymentType02 */
public String getSpecialPaymentType02() 
{
return (String)get_Value("SpecialPaymentType02");
}
/** Set SpecialPaymentType03.
@param SpecialPaymentType03 SpecialPaymentType03 */
public void setSpecialPaymentType03 (String SpecialPaymentType03)
{
if (SpecialPaymentType03 != null && SpecialPaymentType03.length() > 3)
{
log.warning("Length > 3 - truncated");
SpecialPaymentType03 = SpecialPaymentType03.substring(0,2);
}
set_Value ("SpecialPaymentType03", SpecialPaymentType03);
}
/** Get SpecialPaymentType03.
@return SpecialPaymentType03 */
public String getSpecialPaymentType03() 
{
return (String)get_Value("SpecialPaymentType03");
}
/** Set SpecialPaymentType04.
@param SpecialPaymentType04 SpecialPaymentType04 */
public void setSpecialPaymentType04 (String SpecialPaymentType04)
{
if (SpecialPaymentType04 != null && SpecialPaymentType04.length() > 3)
{
log.warning("Length > 3 - truncated");
SpecialPaymentType04 = SpecialPaymentType04.substring(0,2);
}
set_Value ("SpecialPaymentType04", SpecialPaymentType04);
}
/** Get SpecialPaymentType04.
@return SpecialPaymentType04 */
public String getSpecialPaymentType04() 
{
return (String)get_Value("SpecialPaymentType04");
}
/** Set StatutoryHolidayAmount01.
@param StatutoryHolidayAmount01 StatutoryHolidayAmount01 */
public void setStatutoryHolidayAmount01 (BigDecimal StatutoryHolidayAmount01)
{
set_Value ("StatutoryHolidayAmount01", StatutoryHolidayAmount01);
}
/** Get StatutoryHolidayAmount01.
@return StatutoryHolidayAmount01 */
public BigDecimal getStatutoryHolidayAmount01() 
{
BigDecimal bd = (BigDecimal)get_Value("StatutoryHolidayAmount01");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set StatutoryHolidayAmount02.
@param StatutoryHolidayAmount02 StatutoryHolidayAmount02 */
public void setStatutoryHolidayAmount02 (BigDecimal StatutoryHolidayAmount02)
{
set_Value ("StatutoryHolidayAmount02", StatutoryHolidayAmount02);
}
/** Get StatutoryHolidayAmount02.
@return StatutoryHolidayAmount02 */
public BigDecimal getStatutoryHolidayAmount02() 
{
BigDecimal bd = (BigDecimal)get_Value("StatutoryHolidayAmount02");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set StatutoryHolidayAmount03.
@param StatutoryHolidayAmount03 StatutoryHolidayAmount03 */
public void setStatutoryHolidayAmount03 (BigDecimal StatutoryHolidayAmount03)
{
set_Value ("StatutoryHolidayAmount03", StatutoryHolidayAmount03);
}
/** Get StatutoryHolidayAmount03.
@return StatutoryHolidayAmount03 */
public BigDecimal getStatutoryHolidayAmount03() 
{
BigDecimal bd = (BigDecimal)get_Value("StatutoryHolidayAmount03");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set StatutoryHolidayAmount04.
@param StatutoryHolidayAmount04 StatutoryHolidayAmount04 */
public void setStatutoryHolidayAmount04 (BigDecimal StatutoryHolidayAmount04)
{
set_Value ("StatutoryHolidayAmount04", StatutoryHolidayAmount04);
}
/** Get StatutoryHolidayAmount04.
@return StatutoryHolidayAmount04 */
public BigDecimal getStatutoryHolidayAmount04() 
{
BigDecimal bd = (BigDecimal)get_Value("StatutoryHolidayAmount04");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set StatutoryHolidayAmount05.
@param StatutoryHolidayAmount05 StatutoryHolidayAmount05 */
public void setStatutoryHolidayAmount05 (BigDecimal StatutoryHolidayAmount05)
{
set_Value ("StatutoryHolidayAmount05", StatutoryHolidayAmount05);
}
/** Get StatutoryHolidayAmount05.
@return StatutoryHolidayAmount05 */
public BigDecimal getStatutoryHolidayAmount05() 
{
BigDecimal bd = (BigDecimal)get_Value("StatutoryHolidayAmount05");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set StatutoryHolidayAmount06.
@param StatutoryHolidayAmount06 StatutoryHolidayAmount06 */
public void setStatutoryHolidayAmount06 (BigDecimal StatutoryHolidayAmount06)
{
set_Value ("StatutoryHolidayAmount06", StatutoryHolidayAmount06);
}
/** Get StatutoryHolidayAmount06.
@return StatutoryHolidayAmount06 */
public BigDecimal getStatutoryHolidayAmount06() 
{
BigDecimal bd = (BigDecimal)get_Value("StatutoryHolidayAmount06");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set StatutoryHolidayAmount07.
@param StatutoryHolidayAmount07 StatutoryHolidayAmount07 */
public void setStatutoryHolidayAmount07 (BigDecimal StatutoryHolidayAmount07)
{
set_Value ("StatutoryHolidayAmount07", StatutoryHolidayAmount07);
}
/** Get StatutoryHolidayAmount07.
@return StatutoryHolidayAmount07 */
public BigDecimal getStatutoryHolidayAmount07() 
{
BigDecimal bd = (BigDecimal)get_Value("StatutoryHolidayAmount07");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set StatutoryHolidayAmount08.
@param StatutoryHolidayAmount08 StatutoryHolidayAmount08 */
public void setStatutoryHolidayAmount08 (BigDecimal StatutoryHolidayAmount08)
{
set_Value ("StatutoryHolidayAmount08", StatutoryHolidayAmount08);
}
/** Get StatutoryHolidayAmount08.
@return StatutoryHolidayAmount08 */
public BigDecimal getStatutoryHolidayAmount08() 
{
BigDecimal bd = (BigDecimal)get_Value("StatutoryHolidayAmount08");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set StatutoryHolidayAmount09.
@param StatutoryHolidayAmount09 StatutoryHolidayAmount09 */
public void setStatutoryHolidayAmount09 (BigDecimal StatutoryHolidayAmount09)
{
set_Value ("StatutoryHolidayAmount09", StatutoryHolidayAmount09);
}
/** Get StatutoryHolidayAmount09.
@return StatutoryHolidayAmount09 */
public BigDecimal getStatutoryHolidayAmount09() 
{
BigDecimal bd = (BigDecimal)get_Value("StatutoryHolidayAmount09");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set StatutoryHolidayAmount10.
@param StatutoryHolidayAmount10 StatutoryHolidayAmount10 */
public void setStatutoryHolidayAmount10 (BigDecimal StatutoryHolidayAmount10)
{
set_Value ("StatutoryHolidayAmount10", StatutoryHolidayAmount10);
}
/** Get StatutoryHolidayAmount10.
@return StatutoryHolidayAmount10 */
public BigDecimal getStatutoryHolidayAmount10() 
{
BigDecimal bd = (BigDecimal)get_Value("StatutoryHolidayAmount10");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Statutory Holiday Pay amount 1.
@param StatutoryHolidayPayAmount01 Statutory Holiday Pay amount 1 */
public void setStatutoryHolidayPayAmount01 (BigDecimal StatutoryHolidayPayAmount01)
{
set_Value ("StatutoryHolidayPayAmount01", StatutoryHolidayPayAmount01);
}
/** Get Statutory Holiday Pay amount 1.
@return Statutory Holiday Pay amount 1 */
public BigDecimal getStatutoryHolidayPayAmount01() 
{
BigDecimal bd = (BigDecimal)get_Value("StatutoryHolidayPayAmount01");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Statutory Holiday Pay amount 2.
@param StatutoryHolidayPayAmount02 Statutory Holiday Pay amount 2 */
public void setStatutoryHolidayPayAmount02 (BigDecimal StatutoryHolidayPayAmount02)
{
set_Value ("StatutoryHolidayPayAmount02", StatutoryHolidayPayAmount02);
}
/** Get Statutory Holiday Pay amount 2.
@return Statutory Holiday Pay amount 2 */
public BigDecimal getStatutoryHolidayPayAmount02() 
{
BigDecimal bd = (BigDecimal)get_Value("StatutoryHolidayPayAmount02");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Statutory Holiday Pay amount 3.
@param StatutoryHolidayPayAmount03 Statutory Holiday Pay amount 3 */
public void setStatutoryHolidayPayAmount03 (BigDecimal StatutoryHolidayPayAmount03)
{
set_Value ("StatutoryHolidayPayAmount03", StatutoryHolidayPayAmount03);
}
/** Get Statutory Holiday Pay amount 3.
@return Statutory Holiday Pay amount 3 */
public BigDecimal getStatutoryHolidayPayAmount03() 
{
BigDecimal bd = (BigDecimal)get_Value("StatutoryHolidayPayAmount03");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Statutory Holiday Pay date 1.
@param StatutoryHolidayPaydate01 Statutory Holiday Pay date 1 */
public void setStatutoryHolidayPaydate01 (Timestamp StatutoryHolidayPaydate01)
{
set_Value ("StatutoryHolidayPaydate01", StatutoryHolidayPaydate01);
}
/** Get Statutory Holiday Pay date 1.
@return Statutory Holiday Pay date 1 */
public Timestamp getStatutoryHolidayPaydate01() 
{
return (Timestamp)get_Value("StatutoryHolidayPaydate01");
}
/** Set Statutory Holiday Pay date 2.
@param StatutoryHolidayPaydate02 Statutory Holiday Pay date 2 */
public void setStatutoryHolidayPaydate02 (Timestamp StatutoryHolidayPaydate02)
{
set_Value ("StatutoryHolidayPaydate02", StatutoryHolidayPaydate02);
}
/** Get Statutory Holiday Pay date 2.
@return Statutory Holiday Pay date 2 */
public Timestamp getStatutoryHolidayPaydate02() 
{
return (Timestamp)get_Value("StatutoryHolidayPaydate02");
}
/** Set Statutory Holiday Pay date 3.
@param StatutoryHolidayPaydate03 Statutory Holiday Pay date 3 */
public void setStatutoryHolidayPaydate03 (Timestamp StatutoryHolidayPaydate03)
{
set_Value ("StatutoryHolidayPaydate03", StatutoryHolidayPaydate03);
}
/** Get Statutory Holiday Pay date 3.
@return Statutory Holiday Pay date 3 */
public Timestamp getStatutoryHolidayPaydate03() 
{
return (Timestamp)get_Value("StatutoryHolidayPaydate03");
}
/** Set StatutoryHolidayStartDate01.
@param StatutoryHolidayStartDate01 StatutoryHolidayStartDate01 */
public void setStatutoryHolidayStartDate01 (Timestamp StatutoryHolidayStartDate01)
{
set_Value ("StatutoryHolidayStartDate01", StatutoryHolidayStartDate01);
}
/** Get StatutoryHolidayStartDate01.
@return StatutoryHolidayStartDate01 */
public Timestamp getStatutoryHolidayStartDate01() 
{
return (Timestamp)get_Value("StatutoryHolidayStartDate01");
}
/** Set StatutoryHolidayStartDate02.
@param StatutoryHolidayStartDate02 StatutoryHolidayStartDate02 */
public void setStatutoryHolidayStartDate02 (Timestamp StatutoryHolidayStartDate02)
{
set_Value ("StatutoryHolidayStartDate02", StatutoryHolidayStartDate02);
}
/** Get StatutoryHolidayStartDate02.
@return StatutoryHolidayStartDate02 */
public Timestamp getStatutoryHolidayStartDate02() 
{
return (Timestamp)get_Value("StatutoryHolidayStartDate02");
}
/** Set StatutoryHolidayStartDate03.
@param StatutoryHolidayStartDate03 StatutoryHolidayStartDate03 */
public void setStatutoryHolidayStartDate03 (Timestamp StatutoryHolidayStartDate03)
{
set_Value ("StatutoryHolidayStartDate03", StatutoryHolidayStartDate03);
}
/** Get StatutoryHolidayStartDate03.
@return StatutoryHolidayStartDate03 */
public Timestamp getStatutoryHolidayStartDate03() 
{
return (Timestamp)get_Value("StatutoryHolidayStartDate03");
}
/** Set StatutoryHolidayStartDate04.
@param StatutoryHolidayStartDate04 StatutoryHolidayStartDate04 */
public void setStatutoryHolidayStartDate04 (Timestamp StatutoryHolidayStartDate04)
{
set_Value ("StatutoryHolidayStartDate04", StatutoryHolidayStartDate04);
}
/** Get StatutoryHolidayStartDate04.
@return StatutoryHolidayStartDate04 */
public Timestamp getStatutoryHolidayStartDate04() 
{
return (Timestamp)get_Value("StatutoryHolidayStartDate04");
}
/** Set StatutoryHolidayStartDate05.
@param StatutoryHolidayStartDate05 StatutoryHolidayStartDate05 */
public void setStatutoryHolidayStartDate05 (Timestamp StatutoryHolidayStartDate05)
{
set_Value ("StatutoryHolidayStartDate05", StatutoryHolidayStartDate05);
}
/** Get StatutoryHolidayStartDate05.
@return StatutoryHolidayStartDate05 */
public Timestamp getStatutoryHolidayStartDate05() 
{
return (Timestamp)get_Value("StatutoryHolidayStartDate05");
}
/** Set StatutoryHolidayStartDate06.
@param StatutoryHolidayStartDate06 StatutoryHolidayStartDate06 */
public void setStatutoryHolidayStartDate06 (Timestamp StatutoryHolidayStartDate06)
{
set_Value ("StatutoryHolidayStartDate06", StatutoryHolidayStartDate06);
}
/** Get StatutoryHolidayStartDate06.
@return StatutoryHolidayStartDate06 */
public Timestamp getStatutoryHolidayStartDate06() 
{
return (Timestamp)get_Value("StatutoryHolidayStartDate06");
}
/** Set StatutoryHolidayStartDate07.
@param StatutoryHolidayStartDate07 StatutoryHolidayStartDate07 */
public void setStatutoryHolidayStartDate07 (Timestamp StatutoryHolidayStartDate07)
{
set_Value ("StatutoryHolidayStartDate07", StatutoryHolidayStartDate07);
}
/** Get StatutoryHolidayStartDate07.
@return StatutoryHolidayStartDate07 */
public Timestamp getStatutoryHolidayStartDate07() 
{
return (Timestamp)get_Value("StatutoryHolidayStartDate07");
}
/** Set StatutoryHolidayStartDate08.
@param StatutoryHolidayStartDate08 StatutoryHolidayStartDate08 */
public void setStatutoryHolidayStartDate08 (Timestamp StatutoryHolidayStartDate08)
{
set_Value ("StatutoryHolidayStartDate08", StatutoryHolidayStartDate08);
}
/** Get StatutoryHolidayStartDate08.
@return StatutoryHolidayStartDate08 */
public Timestamp getStatutoryHolidayStartDate08() 
{
return (Timestamp)get_Value("StatutoryHolidayStartDate08");
}
/** Set StatutoryHolidayStartDate09.
@param StatutoryHolidayStartDate09 StatutoryHolidayStartDate09 */
public void setStatutoryHolidayStartDate09 (Timestamp StatutoryHolidayStartDate09)
{
set_Value ("StatutoryHolidayStartDate09", StatutoryHolidayStartDate09);
}
/** Get StatutoryHolidayStartDate09.
@return StatutoryHolidayStartDate09 */
public Timestamp getStatutoryHolidayStartDate09() 
{
return (Timestamp)get_Value("StatutoryHolidayStartDate09");
}
/** Set StatutoryHolidayStartDate10.
@param StatutoryHolidayStartDate10 StatutoryHolidayStartDate10 */
public void setStatutoryHolidayStartDate10 (Timestamp StatutoryHolidayStartDate10)
{
set_Value ("StatutoryHolidayStartDate10", StatutoryHolidayStartDate10);
}
/** Get StatutoryHolidayStartDate10.
@return StatutoryHolidayStartDate10 */
public Timestamp getStatutoryHolidayStartDate10() 
{
return (Timestamp)get_Value("StatutoryHolidayStartDate10");
}
/** Set Total Insurable Earnings.
@param TotalInsurableEarnings Total Insurable Earnings */
public void setTotalInsurableEarnings (BigDecimal TotalInsurableEarnings)
{
set_Value ("TotalInsurableEarnings", TotalInsurableEarnings);
}
/** Get Total Insurable Earnings.
@return Total Insurable Earnings */
public BigDecimal getTotalInsurableEarnings() 
{
BigDecimal bd = (BigDecimal)get_Value("TotalInsurableEarnings");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Total Insurable Hours.
@param TotalInsurableHours Total Insurable Hours */
public void setTotalInsurableHours (BigDecimal TotalInsurableHours)
{
set_Value ("TotalInsurableHours", TotalInsurableHours.setScale(2, BigDecimal.ROUND_HALF_UP)   );
}
/** Get Total Insurable Hours.
@return Total Insurable Hours */
public BigDecimal getTotalInsurableHours() 
{
BigDecimal bd = (BigDecimal)get_Value("TotalInsurableHours");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Transferred.
@param Transfered Transferred */
public void setTransfered (boolean Transfered)
{
set_Value ("Transfered", new Boolean(Transfered));
}
/** Get Transferred.
@return Transferred */
public boolean isTransfered() 
{
Object oo = get_Value("Transfered");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Vacation pay amount.
@param VacationPayAmount Vacation pay amount */
public void setVacationPayAmount (BigDecimal VacationPayAmount)
{
set_Value ("VacationPayAmount", VacationPayAmount);
}
/** Get Vacation pay amount.
@return Vacation pay amount */
public BigDecimal getVacationPayAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("VacationPayAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set VacationPayCode.
@param VacationPayCode VacationPayCode */
public void setVacationPayCode (String VacationPayCode)
{
if (VacationPayCode != null && VacationPayCode.length() > 2)
{
log.warning("Length > 2 - truncated");
VacationPayCode = VacationPayCode.substring(0,1);
}
set_Value ("VacationPayCode", VacationPayCode);
}
/** Get VacationPayCode.
@return VacationPayCode */
public String getVacationPayCode() 
{
return (String)get_Value("VacationPayCode");
}
/** Set VacationPayComments.
@param VacationPayComments VacationPayComments */
public void setVacationPayComments (String VacationPayComments)
{
if (VacationPayComments != null && VacationPayComments.length() > 60)
{
log.warning("Length > 60 - truncated");
VacationPayComments = VacationPayComments.substring(0,59);
}
set_Value ("VacationPayComments", VacationPayComments);
}
/** Get VacationPayComments.
@return VacationPayComments */
public String getVacationPayComments() 
{
return (String)get_Value("VacationPayComments");
}
/** Set VacationPayEndDate.
@param VacationPayEndDate VacationPayEndDate */
public void setVacationPayEndDate (Timestamp VacationPayEndDate)
{
set_Value ("VacationPayEndDate", VacationPayEndDate);
}
/** Get VacationPayEndDate.
@return VacationPayEndDate */
public Timestamp getVacationPayEndDate() 
{
return (Timestamp)get_Value("VacationPayEndDate");
}
/** Set VacationPayStartDate.
@param VacationPayStartDate VacationPayStartDate */
public void setVacationPayStartDate (Timestamp VacationPayStartDate)
{
set_Value ("VacationPayStartDate", VacationPayStartDate);
}
/** Get VacationPayStartDate.
@return VacationPayStartDate */
public Timestamp getVacationPayStartDate() 
{
return (Timestamp)get_Value("VacationPayStartDate");
}
}
