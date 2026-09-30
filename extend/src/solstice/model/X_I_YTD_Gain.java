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
/** Generated Model for I_YTD_Gain
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.1 2007/07/18 14:43:46 marmor01 Exp $ */
public class X_I_YTD_Gain extends PO
{
/** Standard Constructor
@param ctx context
@param I_YTD_Gain_ID id
@param trxName transaction
*/
public X_I_YTD_Gain (Properties ctx, int I_YTD_Gain_ID, String trxName)
{
super (ctx, I_YTD_Gain_ID, trxName);
/** if (I_YTD_Gain_ID == 0)
{
setI_YTD_Gain_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_I_YTD_Gain (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100856 */
public static final int Table_ID=2100856;

/** TableName=I_YTD_Gain */
public static final String Table_Name="I_YTD_Gain";

protected static KeyNamePair Model = new KeyNamePair(2100856,"I_YTD_Gain");

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
StringBuffer sb = new StringBuffer ("X_I_YTD_Gain[").append(get_ID()).append("]");
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
/** Set Earn01.
@param Earn01 Earn01 */
public void setEarn01 (BigDecimal Earn01)
{
set_Value ("Earn01", Earn01);
}
/** Get Earn01.
@return Earn01 */
public BigDecimal getEarn01() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn01");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn02.
@param Earn02 Earn02 */
public void setEarn02 (BigDecimal Earn02)
{
set_Value ("Earn02", Earn02);
}
/** Get Earn02.
@return Earn02 */
public BigDecimal getEarn02() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn02");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn03.
@param Earn03 Earn03 */
public void setEarn03 (BigDecimal Earn03)
{
set_Value ("Earn03", Earn03);
}
/** Get Earn03.
@return Earn03 */
public BigDecimal getEarn03() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn03");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn04.
@param Earn04 Earn04 */
public void setEarn04 (BigDecimal Earn04)
{
set_Value ("Earn04", Earn04);
}
/** Get Earn04.
@return Earn04 */
public BigDecimal getEarn04() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn04");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn05.
@param Earn05 Earn05 */
public void setEarn05 (BigDecimal Earn05)
{
set_Value ("Earn05", Earn05);
}
/** Get Earn05.
@return Earn05 */
public BigDecimal getEarn05() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn05");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn06.
@param Earn06 Earn06 */
public void setEarn06 (BigDecimal Earn06)
{
set_Value ("Earn06", Earn06);
}
/** Get Earn06.
@return Earn06 */
public BigDecimal getEarn06() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn06");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn07.
@param Earn07 Earn07 */
public void setEarn07 (BigDecimal Earn07)
{
set_Value ("Earn07", Earn07);
}
/** Get Earn07.
@return Earn07 */
public BigDecimal getEarn07() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn07");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn08.
@param Earn08 Earn08 */
public void setEarn08 (BigDecimal Earn08)
{
set_Value ("Earn08", Earn08);
}
/** Get Earn08.
@return Earn08 */
public BigDecimal getEarn08() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn08");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn09.
@param Earn09 Earn09 */
public void setEarn09 (BigDecimal Earn09)
{
set_Value ("Earn09", Earn09);
}
/** Get Earn09.
@return Earn09 */
public BigDecimal getEarn09() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn09");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn10.
@param Earn10 Earn10 */
public void setEarn10 (BigDecimal Earn10)
{
set_Value ("Earn10", Earn10);
}
/** Get Earn10.
@return Earn10 */
public BigDecimal getEarn10() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn10");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn11.
@param Earn11 Earn11 */
public void setEarn11 (BigDecimal Earn11)
{
set_Value ("Earn11", Earn11);
}
/** Get Earn11.
@return Earn11 */
public BigDecimal getEarn11() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn11");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn12.
@param Earn12 Earn12 */
public void setEarn12 (BigDecimal Earn12)
{
set_Value ("Earn12", Earn12);
}
/** Get Earn12.
@return Earn12 */
public BigDecimal getEarn12() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn12");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn13.
@param Earn13 Earn13 */
public void setEarn13 (BigDecimal Earn13)
{
set_Value ("Earn13", Earn13);
}
/** Get Earn13.
@return Earn13 */
public BigDecimal getEarn13() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn13");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn14.
@param Earn14 Earn14 */
public void setEarn14 (BigDecimal Earn14)
{
set_Value ("Earn14", Earn14);
}
/** Get Earn14.
@return Earn14 */
public BigDecimal getEarn14() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn14");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn15.
@param Earn15 Earn15 */
public void setEarn15 (BigDecimal Earn15)
{
set_Value ("Earn15", Earn15);
}
/** Get Earn15.
@return Earn15 */
public BigDecimal getEarn15() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn15");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn16.
@param Earn16 Earn16 */
public void setEarn16 (BigDecimal Earn16)
{
set_Value ("Earn16", Earn16);
}
/** Get Earn16.
@return Earn16 */
public BigDecimal getEarn16() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn16");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn17.
@param Earn17 Earn17 */
public void setEarn17 (BigDecimal Earn17)
{
set_Value ("Earn17", Earn17);
}
/** Get Earn17.
@return Earn17 */
public BigDecimal getEarn17() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn17");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn18.
@param Earn18 Earn18 */
public void setEarn18 (BigDecimal Earn18)
{
set_Value ("Earn18", Earn18);
}
/** Get Earn18.
@return Earn18 */
public BigDecimal getEarn18() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn18");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn19.
@param Earn19 Earn19 */
public void setEarn19 (BigDecimal Earn19)
{
set_Value ("Earn19", Earn19);
}
/** Get Earn19.
@return Earn19 */
public BigDecimal getEarn19() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn19");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn20.
@param Earn20 Earn20 */
public void setEarn20 (BigDecimal Earn20)
{
set_Value ("Earn20", Earn20);
}
/** Get Earn20.
@return Earn20 */
public BigDecimal getEarn20() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn20");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn21.
@param Earn21 Earn21 */
public void setEarn21 (BigDecimal Earn21)
{
set_Value ("Earn21", Earn21);
}
/** Get Earn21.
@return Earn21 */
public BigDecimal getEarn21() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn21");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn22.
@param Earn22 Earn22 */
public void setEarn22 (BigDecimal Earn22)
{
set_Value ("Earn22", Earn22);
}
/** Get Earn22.
@return Earn22 */
public BigDecimal getEarn22() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn22");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn23.
@param Earn23 Earn23 */
public void setEarn23 (BigDecimal Earn23)
{
set_Value ("Earn23", Earn23);
}
/** Get Earn23.
@return Earn23 */
public BigDecimal getEarn23() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn23");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn24.
@param Earn24 Earn24 */
public void setEarn24 (BigDecimal Earn24)
{
set_Value ("Earn24", Earn24);
}
/** Get Earn24.
@return Earn24 */
public BigDecimal getEarn24() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn24");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn25.
@param Earn25 Earn25 */
public void setEarn25 (BigDecimal Earn25)
{
set_Value ("Earn25", Earn25);
}
/** Get Earn25.
@return Earn25 */
public BigDecimal getEarn25() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn25");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn26.
@param Earn26 Earn26 */
public void setEarn26 (BigDecimal Earn26)
{
set_Value ("Earn26", Earn26);
}
/** Get Earn26.
@return Earn26 */
public BigDecimal getEarn26() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn26");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn27.
@param Earn27 Earn27 */
public void setEarn27 (BigDecimal Earn27)
{
set_Value ("Earn27", Earn27);
}
/** Get Earn27.
@return Earn27 */
public BigDecimal getEarn27() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn27");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn28.
@param Earn28 Earn28 */
public void setEarn28 (BigDecimal Earn28)
{
set_Value ("Earn28", Earn28);
}
/** Get Earn28.
@return Earn28 */
public BigDecimal getEarn28() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn28");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn29.
@param Earn29 Earn29 */
public void setEarn29 (BigDecimal Earn29)
{
set_Value ("Earn29", Earn29);
}
/** Get Earn29.
@return Earn29 */
public BigDecimal getEarn29() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn29");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn30.
@param Earn30 Earn30 */
public void setEarn30 (BigDecimal Earn30)
{
set_Value ("Earn30", Earn30);
}
/** Get Earn30.
@return Earn30 */
public BigDecimal getEarn30() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn30");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn31.
@param Earn31 Earn31 */
public void setEarn31 (BigDecimal Earn31)
{
set_Value ("Earn31", Earn31);
}
/** Get Earn31.
@return Earn31 */
public BigDecimal getEarn31() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn31");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn32.
@param Earn32 Earn32 */
public void setEarn32 (BigDecimal Earn32)
{
set_Value ("Earn32", Earn32);
}
/** Get Earn32.
@return Earn32 */
public BigDecimal getEarn32() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn32");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn33.
@param Earn33 Earn33 */
public void setEarn33 (BigDecimal Earn33)
{
set_Value ("Earn33", Earn33);
}
/** Get Earn33.
@return Earn33 */
public BigDecimal getEarn33() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn33");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn34.
@param Earn34 Earn34 */
public void setEarn34 (BigDecimal Earn34)
{
set_Value ("Earn34", Earn34);
}
/** Get Earn34.
@return Earn34 */
public BigDecimal getEarn34() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn34");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn35.
@param Earn35 Earn35 */
public void setEarn35 (BigDecimal Earn35)
{
set_Value ("Earn35", Earn35);
}
/** Get Earn35.
@return Earn35 */
public BigDecimal getEarn35() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn35");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn36.
@param Earn36 Earn36 */
public void setEarn36 (BigDecimal Earn36)
{
set_Value ("Earn36", Earn36);
}
/** Get Earn36.
@return Earn36 */
public BigDecimal getEarn36() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn36");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn37.
@param Earn37 Earn37 */
public void setEarn37 (BigDecimal Earn37)
{
set_Value ("Earn37", Earn37);
}
/** Get Earn37.
@return Earn37 */
public BigDecimal getEarn37() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn37");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn38.
@param Earn38 Earn38 */
public void setEarn38 (BigDecimal Earn38)
{
set_Value ("Earn38", Earn38);
}
/** Get Earn38.
@return Earn38 */
public BigDecimal getEarn38() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn38");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn39.
@param Earn39 Earn39 */
public void setEarn39 (BigDecimal Earn39)
{
set_Value ("Earn39", Earn39);
}
/** Get Earn39.
@return Earn39 */
public BigDecimal getEarn39() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn39");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn40.
@param Earn40 Earn40 */
public void setEarn40 (BigDecimal Earn40)
{
set_Value ("Earn40", Earn40);
}
/** Get Earn40.
@return Earn40 */
public BigDecimal getEarn40() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn40");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn41.
@param Earn41 Earn41 */
public void setEarn41 (BigDecimal Earn41)
{
set_Value ("Earn41", Earn41);
}
/** Get Earn41.
@return Earn41 */
public BigDecimal getEarn41() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn41");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn42.
@param Earn42 Earn42 */
public void setEarn42 (BigDecimal Earn42)
{
set_Value ("Earn42", Earn42);
}
/** Get Earn42.
@return Earn42 */
public BigDecimal getEarn42() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn42");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn43.
@param Earn43 Earn43 */
public void setEarn43 (BigDecimal Earn43)
{
set_Value ("Earn43", Earn43);
}
/** Get Earn43.
@return Earn43 */
public BigDecimal getEarn43() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn43");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn44.
@param Earn44 Earn44 */
public void setEarn44 (BigDecimal Earn44)
{
set_Value ("Earn44", Earn44);
}
/** Get Earn44.
@return Earn44 */
public BigDecimal getEarn44() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn44");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn45.
@param Earn45 Earn45 */
public void setEarn45 (BigDecimal Earn45)
{
set_Value ("Earn45", Earn45);
}
/** Get Earn45.
@return Earn45 */
public BigDecimal getEarn45() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn45");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn46.
@param Earn46 Earn46 */
public void setEarn46 (BigDecimal Earn46)
{
set_Value ("Earn46", Earn46);
}
/** Get Earn46.
@return Earn46 */
public BigDecimal getEarn46() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn46");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn47.
@param Earn47 Earn47 */
public void setEarn47 (BigDecimal Earn47)
{
set_Value ("Earn47", Earn47);
}
/** Get Earn47.
@return Earn47 */
public BigDecimal getEarn47() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn47");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn48.
@param Earn48 Earn48 */
public void setEarn48 (BigDecimal Earn48)
{
set_Value ("Earn48", Earn48);
}
/** Get Earn48.
@return Earn48 */
public BigDecimal getEarn48() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn48");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn49.
@param Earn49 Earn49 */
public void setEarn49 (BigDecimal Earn49)
{
set_Value ("Earn49", Earn49);
}
/** Get Earn49.
@return Earn49 */
public BigDecimal getEarn49() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn49");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn50.
@param Earn50 Earn50 */
public void setEarn50 (BigDecimal Earn50)
{
set_Value ("Earn50", Earn50);
}
/** Get Earn50.
@return Earn50 */
public BigDecimal getEarn50() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn50");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn51.
@param Earn51 Earn51 */
public void setEarn51 (BigDecimal Earn51)
{
set_Value ("Earn51", Earn51);
}
/** Get Earn51.
@return Earn51 */
public BigDecimal getEarn51() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn51");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn52.
@param Earn52 Earn52 */
public void setEarn52 (BigDecimal Earn52)
{
set_Value ("Earn52", Earn52);
}
/** Get Earn52.
@return Earn52 */
public BigDecimal getEarn52() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn52");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn53.
@param Earn53 Earn53 */
public void setEarn53 (BigDecimal Earn53)
{
set_Value ("Earn53", Earn53);
}
/** Get Earn53.
@return Earn53 */
public BigDecimal getEarn53() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn53");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn54.
@param Earn54 Earn54 */
public void setEarn54 (BigDecimal Earn54)
{
set_Value ("Earn54", Earn54);
}
/** Get Earn54.
@return Earn54 */
public BigDecimal getEarn54() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn54");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn55.
@param Earn55 Earn55 */
public void setEarn55 (BigDecimal Earn55)
{
set_Value ("Earn55", Earn55);
}
/** Get Earn55.
@return Earn55 */
public BigDecimal getEarn55() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn55");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn56.
@param Earn56 Earn56 */
public void setEarn56 (BigDecimal Earn56)
{
set_Value ("Earn56", Earn56);
}
/** Get Earn56.
@return Earn56 */
public BigDecimal getEarn56() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn56");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn57.
@param Earn57 Earn57 */
public void setEarn57 (BigDecimal Earn57)
{
set_Value ("Earn57", Earn57);
}
/** Get Earn57.
@return Earn57 */
public BigDecimal getEarn57() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn57");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn58.
@param Earn58 Earn58 */
public void setEarn58 (BigDecimal Earn58)
{
set_Value ("Earn58", Earn58);
}
/** Get Earn58.
@return Earn58 */
public BigDecimal getEarn58() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn58");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn59.
@param Earn59 Earn59 */
public void setEarn59 (BigDecimal Earn59)
{
set_Value ("Earn59", Earn59);
}
/** Get Earn59.
@return Earn59 */
public BigDecimal getEarn59() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn59");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn60.
@param Earn60 Earn60 */
public void setEarn60 (BigDecimal Earn60)
{
set_Value ("Earn60", Earn60);
}
/** Get Earn60.
@return Earn60 */
public BigDecimal getEarn60() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn60");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn61.
@param Earn61 Earn61 */
public void setEarn61 (BigDecimal Earn61)
{
set_Value ("Earn61", Earn61);
}
/** Get Earn61.
@return Earn61 */
public BigDecimal getEarn61() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn61");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn62.
@param Earn62 Earn62 */
public void setEarn62 (BigDecimal Earn62)
{
set_Value ("Earn62", Earn62);
}
/** Get Earn62.
@return Earn62 */
public BigDecimal getEarn62() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn62");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn63.
@param Earn63 Earn63 */
public void setEarn63 (BigDecimal Earn63)
{
set_Value ("Earn63", Earn63);
}
/** Get Earn63.
@return Earn63 */
public BigDecimal getEarn63() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn63");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn64.
@param Earn64 Earn64 */
public void setEarn64 (BigDecimal Earn64)
{
set_Value ("Earn64", Earn64);
}
/** Get Earn64.
@return Earn64 */
public BigDecimal getEarn64() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn64");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn65.
@param Earn65 Earn65 */
public void setEarn65 (BigDecimal Earn65)
{
set_Value ("Earn65", Earn65);
}
/** Get Earn65.
@return Earn65 */
public BigDecimal getEarn65() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn65");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn66.
@param Earn66 Earn66 */
public void setEarn66 (BigDecimal Earn66)
{
set_Value ("Earn66", Earn66);
}
/** Get Earn66.
@return Earn66 */
public BigDecimal getEarn66() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn66");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn67.
@param Earn67 Earn67 */
public void setEarn67 (BigDecimal Earn67)
{
set_Value ("Earn67", Earn67);
}
/** Get Earn67.
@return Earn67 */
public BigDecimal getEarn67() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn67");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn68.
@param Earn68 Earn68 */
public void setEarn68 (BigDecimal Earn68)
{
set_Value ("Earn68", Earn68);
}
/** Get Earn68.
@return Earn68 */
public BigDecimal getEarn68() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn68");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn69.
@param Earn69 Earn69 */
public void setEarn69 (BigDecimal Earn69)
{
set_Value ("Earn69", Earn69);
}
/** Get Earn69.
@return Earn69 */
public BigDecimal getEarn69() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn69");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn70.
@param Earn70 Earn70 */
public void setEarn70 (BigDecimal Earn70)
{
set_Value ("Earn70", Earn70);
}
/** Get Earn70.
@return Earn70 */
public BigDecimal getEarn70() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn70");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn71.
@param Earn71 Earn71 */
public void setEarn71 (BigDecimal Earn71)
{
set_Value ("Earn71", Earn71);
}
/** Get Earn71.
@return Earn71 */
public BigDecimal getEarn71() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn71");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn72.
@param Earn72 Earn72 */
public void setEarn72 (BigDecimal Earn72)
{
set_Value ("Earn72", Earn72);
}
/** Get Earn72.
@return Earn72 */
public BigDecimal getEarn72() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn72");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn73.
@param Earn73 Earn73 */
public void setEarn73 (BigDecimal Earn73)
{
set_Value ("Earn73", Earn73);
}
/** Get Earn73.
@return Earn73 */
public BigDecimal getEarn73() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn73");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn74.
@param Earn74 Earn74 */
public void setEarn74 (BigDecimal Earn74)
{
set_Value ("Earn74", Earn74);
}
/** Get Earn74.
@return Earn74 */
public BigDecimal getEarn74() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn74");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn75.
@param Earn75 Earn75 */
public void setEarn75 (BigDecimal Earn75)
{
set_Value ("Earn75", Earn75);
}
/** Get Earn75.
@return Earn75 */
public BigDecimal getEarn75() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn75");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn76.
@param Earn76 Earn76 */
public void setEarn76 (BigDecimal Earn76)
{
set_Value ("Earn76", Earn76);
}
/** Get Earn76.
@return Earn76 */
public BigDecimal getEarn76() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn76");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn77.
@param Earn77 Earn77 */
public void setEarn77 (BigDecimal Earn77)
{
set_Value ("Earn77", Earn77);
}
/** Get Earn77.
@return Earn77 */
public BigDecimal getEarn77() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn77");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn78.
@param Earn78 Earn78 */
public void setEarn78 (BigDecimal Earn78)
{
set_Value ("Earn78", Earn78);
}
/** Get Earn78.
@return Earn78 */
public BigDecimal getEarn78() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn78");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn79.
@param Earn79 Earn79 */
public void setEarn79 (BigDecimal Earn79)
{
set_Value ("Earn79", Earn79);
}
/** Get Earn79.
@return Earn79 */
public BigDecimal getEarn79() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn79");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn80.
@param Earn80 Earn80 */
public void setEarn80 (BigDecimal Earn80)
{
set_Value ("Earn80", Earn80);
}
/** Get Earn80.
@return Earn80 */
public BigDecimal getEarn80() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn80");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn81.
@param Earn81 Earn81 */
public void setEarn81 (BigDecimal Earn81)
{
set_Value ("Earn81", Earn81);
}
/** Get Earn81.
@return Earn81 */
public BigDecimal getEarn81() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn81");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn82.
@param Earn82 Earn82 */
public void setEarn82 (BigDecimal Earn82)
{
set_Value ("Earn82", Earn82);
}
/** Get Earn82.
@return Earn82 */
public BigDecimal getEarn82() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn82");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn83.
@param Earn83 Earn83 */
public void setEarn83 (BigDecimal Earn83)
{
set_Value ("Earn83", Earn83);
}
/** Get Earn83.
@return Earn83 */
public BigDecimal getEarn83() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn83");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn84.
@param Earn84 Earn84 */
public void setEarn84 (BigDecimal Earn84)
{
set_Value ("Earn84", Earn84);
}
/** Get Earn84.
@return Earn84 */
public BigDecimal getEarn84() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn84");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn85.
@param Earn85 Earn85 */
public void setEarn85 (BigDecimal Earn85)
{
set_Value ("Earn85", Earn85);
}
/** Get Earn85.
@return Earn85 */
public BigDecimal getEarn85() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn85");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn86.
@param Earn86 Earn86 */
public void setEarn86 (BigDecimal Earn86)
{
set_Value ("Earn86", Earn86);
}
/** Get Earn86.
@return Earn86 */
public BigDecimal getEarn86() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn86");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn87.
@param Earn87 Earn87 */
public void setEarn87 (BigDecimal Earn87)
{
set_Value ("Earn87", Earn87);
}
/** Get Earn87.
@return Earn87 */
public BigDecimal getEarn87() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn87");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn88.
@param Earn88 Earn88 */
public void setEarn88 (BigDecimal Earn88)
{
set_Value ("Earn88", Earn88);
}
/** Get Earn88.
@return Earn88 */
public BigDecimal getEarn88() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn88");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn89.
@param Earn89 Earn89 */
public void setEarn89 (BigDecimal Earn89)
{
set_Value ("Earn89", Earn89);
}
/** Get Earn89.
@return Earn89 */
public BigDecimal getEarn89() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn89");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn90.
@param Earn90 Earn90 */
public void setEarn90 (BigDecimal Earn90)
{
set_Value ("Earn90", Earn90);
}
/** Get Earn90.
@return Earn90 */
public BigDecimal getEarn90() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn90");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn91.
@param Earn91 Earn91 */
public void setEarn91 (BigDecimal Earn91)
{
set_Value ("Earn91", Earn91);
}
/** Get Earn91.
@return Earn91 */
public BigDecimal getEarn91() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn91");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn92.
@param Earn92 Earn92 */
public void setEarn92 (BigDecimal Earn92)
{
set_Value ("Earn92", Earn92);
}
/** Get Earn92.
@return Earn92 */
public BigDecimal getEarn92() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn92");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn93.
@param Earn93 Earn93 */
public void setEarn93 (BigDecimal Earn93)
{
set_Value ("Earn93", Earn93);
}
/** Get Earn93.
@return Earn93 */
public BigDecimal getEarn93() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn93");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn94.
@param Earn94 Earn94 */
public void setEarn94 (BigDecimal Earn94)
{
set_Value ("Earn94", Earn94);
}
/** Get Earn94.
@return Earn94 */
public BigDecimal getEarn94() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn94");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn95.
@param Earn95 Earn95 */
public void setEarn95 (BigDecimal Earn95)
{
set_Value ("Earn95", Earn95);
}
/** Get Earn95.
@return Earn95 */
public BigDecimal getEarn95() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn95");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earn96.
@param Earn96 Earn96 */
public void setEarn96 (BigDecimal Earn96)
{
set_Value ("Earn96", Earn96);
}
/** Get Earn96.
@return Earn96 */
public BigDecimal getEarn96() 
{
BigDecimal bd = (BigDecimal)get_Value("Earn96");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set GrossPay.
@param GrossPay GrossPay */
public void setGrossPay (BigDecimal GrossPay)
{
set_Value ("GrossPay", GrossPay);
}
/** Get GrossPay.
@return GrossPay */
public BigDecimal getGrossPay() 
{
BigDecimal bd = (BigDecimal)get_Value("GrossPay");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours01.
@param Hours01 Hours01 */
public void setHours01 (BigDecimal Hours01)
{
set_Value ("Hours01", Hours01);
}
/** Get Hours01.
@return Hours01 */
public BigDecimal getHours01() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours01");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours02.
@param Hours02 Hours02 */
public void setHours02 (BigDecimal Hours02)
{
set_Value ("Hours02", Hours02);
}
/** Get Hours02.
@return Hours02 */
public BigDecimal getHours02() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours02");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours03.
@param Hours03 Hours03 */
public void setHours03 (BigDecimal Hours03)
{
set_Value ("Hours03", Hours03);
}
/** Get Hours03.
@return Hours03 */
public BigDecimal getHours03() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours03");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours04.
@param Hours04 Hours04 */
public void setHours04 (BigDecimal Hours04)
{
set_Value ("Hours04", Hours04);
}
/** Get Hours04.
@return Hours04 */
public BigDecimal getHours04() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours04");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours05.
@param Hours05 Hours05 */
public void setHours05 (BigDecimal Hours05)
{
set_Value ("Hours05", Hours05);
}
/** Get Hours05.
@return Hours05 */
public BigDecimal getHours05() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours05");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours06.
@param Hours06 Hours06 */
public void setHours06 (BigDecimal Hours06)
{
set_Value ("Hours06", Hours06);
}
/** Get Hours06.
@return Hours06 */
public BigDecimal getHours06() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours06");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours07.
@param Hours07 Hours07 */
public void setHours07 (BigDecimal Hours07)
{
set_Value ("Hours07", Hours07);
}
/** Get Hours07.
@return Hours07 */
public BigDecimal getHours07() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours07");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours08.
@param Hours08 Hours08 */
public void setHours08 (BigDecimal Hours08)
{
set_Value ("Hours08", Hours08);
}
/** Get Hours08.
@return Hours08 */
public BigDecimal getHours08() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours08");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours09.
@param Hours09 Hours09 */
public void setHours09 (BigDecimal Hours09)
{
set_Value ("Hours09", Hours09);
}
/** Get Hours09.
@return Hours09 */
public BigDecimal getHours09() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours09");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours10.
@param Hours10 Hours10 */
public void setHours10 (BigDecimal Hours10)
{
set_Value ("Hours10", Hours10);
}
/** Get Hours10.
@return Hours10 */
public BigDecimal getHours10() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours10");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours11.
@param Hours11 Hours11 */
public void setHours11 (BigDecimal Hours11)
{
set_Value ("Hours11", Hours11);
}
/** Get Hours11.
@return Hours11 */
public BigDecimal getHours11() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours11");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours12.
@param Hours12 Hours12 */
public void setHours12 (BigDecimal Hours12)
{
set_Value ("Hours12", Hours12);
}
/** Get Hours12.
@return Hours12 */
public BigDecimal getHours12() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours12");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours13.
@param Hours13 Hours13 */
public void setHours13 (BigDecimal Hours13)
{
set_Value ("Hours13", Hours13);
}
/** Get Hours13.
@return Hours13 */
public BigDecimal getHours13() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours13");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours14.
@param Hours14 Hours14 */
public void setHours14 (BigDecimal Hours14)
{
set_Value ("Hours14", Hours14);
}
/** Get Hours14.
@return Hours14 */
public BigDecimal getHours14() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours14");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours15.
@param Hours15 Hours15 */
public void setHours15 (BigDecimal Hours15)
{
set_Value ("Hours15", Hours15);
}
/** Get Hours15.
@return Hours15 */
public BigDecimal getHours15() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours15");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours16.
@param Hours16 Hours16 */
public void setHours16 (BigDecimal Hours16)
{
set_Value ("Hours16", Hours16);
}
/** Get Hours16.
@return Hours16 */
public BigDecimal getHours16() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours16");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours17.
@param Hours17 Hours17 */
public void setHours17 (BigDecimal Hours17)
{
set_Value ("Hours17", Hours17);
}
/** Get Hours17.
@return Hours17 */
public BigDecimal getHours17() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours17");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours18.
@param Hours18 Hours18 */
public void setHours18 (BigDecimal Hours18)
{
set_Value ("Hours18", Hours18);
}
/** Get Hours18.
@return Hours18 */
public BigDecimal getHours18() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours18");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours19.
@param Hours19 Hours19 */
public void setHours19 (BigDecimal Hours19)
{
set_Value ("Hours19", Hours19);
}
/** Get Hours19.
@return Hours19 */
public BigDecimal getHours19() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours19");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours20.
@param Hours20 Hours20 */
public void setHours20 (BigDecimal Hours20)
{
set_Value ("Hours20", Hours20);
}
/** Get Hours20.
@return Hours20 */
public BigDecimal getHours20() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours20");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours21.
@param Hours21 Hours21 */
public void setHours21 (BigDecimal Hours21)
{
set_Value ("Hours21", Hours21);
}
/** Get Hours21.
@return Hours21 */
public BigDecimal getHours21() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours21");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours22.
@param Hours22 Hours22 */
public void setHours22 (BigDecimal Hours22)
{
set_Value ("Hours22", Hours22);
}
/** Get Hours22.
@return Hours22 */
public BigDecimal getHours22() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours22");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours23.
@param Hours23 Hours23 */
public void setHours23 (BigDecimal Hours23)
{
set_Value ("Hours23", Hours23);
}
/** Get Hours23.
@return Hours23 */
public BigDecimal getHours23() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours23");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours24.
@param Hours24 Hours24 */
public void setHours24 (BigDecimal Hours24)
{
set_Value ("Hours24", Hours24);
}
/** Get Hours24.
@return Hours24 */
public BigDecimal getHours24() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours24");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours25.
@param Hours25 Hours25 */
public void setHours25 (BigDecimal Hours25)
{
set_Value ("Hours25", Hours25);
}
/** Get Hours25.
@return Hours25 */
public BigDecimal getHours25() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours25");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours26.
@param Hours26 Hours26 */
public void setHours26 (BigDecimal Hours26)
{
set_Value ("Hours26", Hours26);
}
/** Get Hours26.
@return Hours26 */
public BigDecimal getHours26() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours26");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours27.
@param Hours27 Hours27 */
public void setHours27 (BigDecimal Hours27)
{
set_Value ("Hours27", Hours27);
}
/** Get Hours27.
@return Hours27 */
public BigDecimal getHours27() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours27");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours28.
@param Hours28 Hours28 */
public void setHours28 (BigDecimal Hours28)
{
set_Value ("Hours28", Hours28);
}
/** Get Hours28.
@return Hours28 */
public BigDecimal getHours28() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours28");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours29.
@param Hours29 Hours29 */
public void setHours29 (BigDecimal Hours29)
{
set_Value ("Hours29", Hours29);
}
/** Get Hours29.
@return Hours29 */
public BigDecimal getHours29() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours29");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours30.
@param Hours30 Hours30 */
public void setHours30 (BigDecimal Hours30)
{
set_Value ("Hours30", Hours30);
}
/** Get Hours30.
@return Hours30 */
public BigDecimal getHours30() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours30");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours31.
@param Hours31 Hours31 */
public void setHours31 (BigDecimal Hours31)
{
set_Value ("Hours31", Hours31);
}
/** Get Hours31.
@return Hours31 */
public BigDecimal getHours31() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours31");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours32.
@param Hours32 Hours32 */
public void setHours32 (BigDecimal Hours32)
{
set_Value ("Hours32", Hours32);
}
/** Get Hours32.
@return Hours32 */
public BigDecimal getHours32() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours32");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours33.
@param Hours33 Hours33 */
public void setHours33 (BigDecimal Hours33)
{
set_Value ("Hours33", Hours33);
}
/** Get Hours33.
@return Hours33 */
public BigDecimal getHours33() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours33");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours34.
@param Hours34 Hours34 */
public void setHours34 (BigDecimal Hours34)
{
set_Value ("Hours34", Hours34);
}
/** Get Hours34.
@return Hours34 */
public BigDecimal getHours34() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours34");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours35.
@param Hours35 Hours35 */
public void setHours35 (BigDecimal Hours35)
{
set_Value ("Hours35", Hours35);
}
/** Get Hours35.
@return Hours35 */
public BigDecimal getHours35() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours35");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours36.
@param Hours36 Hours36 */
public void setHours36 (BigDecimal Hours36)
{
set_Value ("Hours36", Hours36);
}
/** Get Hours36.
@return Hours36 */
public BigDecimal getHours36() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours36");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours37.
@param Hours37 Hours37 */
public void setHours37 (BigDecimal Hours37)
{
set_Value ("Hours37", Hours37);
}
/** Get Hours37.
@return Hours37 */
public BigDecimal getHours37() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours37");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours38.
@param Hours38 Hours38 */
public void setHours38 (BigDecimal Hours38)
{
set_Value ("Hours38", Hours38);
}
/** Get Hours38.
@return Hours38 */
public BigDecimal getHours38() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours38");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours39.
@param Hours39 Hours39 */
public void setHours39 (BigDecimal Hours39)
{
set_Value ("Hours39", Hours39);
}
/** Get Hours39.
@return Hours39 */
public BigDecimal getHours39() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours39");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours40.
@param Hours40 Hours40 */
public void setHours40 (BigDecimal Hours40)
{
set_Value ("Hours40", Hours40);
}
/** Get Hours40.
@return Hours40 */
public BigDecimal getHours40() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours40");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours41.
@param Hours41 Hours41 */
public void setHours41 (BigDecimal Hours41)
{
set_Value ("Hours41", Hours41);
}
/** Get Hours41.
@return Hours41 */
public BigDecimal getHours41() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours41");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours42.
@param Hours42 Hours42 */
public void setHours42 (BigDecimal Hours42)
{
set_Value ("Hours42", Hours42);
}
/** Get Hours42.
@return Hours42 */
public BigDecimal getHours42() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours42");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours43.
@param Hours43 Hours43 */
public void setHours43 (BigDecimal Hours43)
{
set_Value ("Hours43", Hours43);
}
/** Get Hours43.
@return Hours43 */
public BigDecimal getHours43() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours43");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours44.
@param Hours44 Hours44 */
public void setHours44 (BigDecimal Hours44)
{
set_Value ("Hours44", Hours44);
}
/** Get Hours44.
@return Hours44 */
public BigDecimal getHours44() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours44");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours45.
@param Hours45 Hours45 */
public void setHours45 (BigDecimal Hours45)
{
set_Value ("Hours45", Hours45);
}
/** Get Hours45.
@return Hours45 */
public BigDecimal getHours45() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours45");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours46.
@param Hours46 Hours46 */
public void setHours46 (BigDecimal Hours46)
{
set_Value ("Hours46", Hours46);
}
/** Get Hours46.
@return Hours46 */
public BigDecimal getHours46() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours46");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours47.
@param Hours47 Hours47 */
public void setHours47 (BigDecimal Hours47)
{
set_Value ("Hours47", Hours47);
}
/** Get Hours47.
@return Hours47 */
public BigDecimal getHours47() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours47");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours48.
@param Hours48 Hours48 */
public void setHours48 (BigDecimal Hours48)
{
set_Value ("Hours48", Hours48);
}
/** Get Hours48.
@return Hours48 */
public BigDecimal getHours48() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours48");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours49.
@param Hours49 Hours49 */
public void setHours49 (BigDecimal Hours49)
{
set_Value ("Hours49", Hours49);
}
/** Get Hours49.
@return Hours49 */
public BigDecimal getHours49() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours49");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours50.
@param Hours50 Hours50 */
public void setHours50 (BigDecimal Hours50)
{
set_Value ("Hours50", Hours50);
}
/** Get Hours50.
@return Hours50 */
public BigDecimal getHours50() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours50");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours51.
@param Hours51 Hours51 */
public void setHours51 (BigDecimal Hours51)
{
set_Value ("Hours51", Hours51);
}
/** Get Hours51.
@return Hours51 */
public BigDecimal getHours51() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours51");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours52.
@param Hours52 Hours52 */
public void setHours52 (BigDecimal Hours52)
{
set_Value ("Hours52", Hours52);
}
/** Get Hours52.
@return Hours52 */
public BigDecimal getHours52() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours52");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours53.
@param Hours53 Hours53 */
public void setHours53 (BigDecimal Hours53)
{
set_Value ("Hours53", Hours53);
}
/** Get Hours53.
@return Hours53 */
public BigDecimal getHours53() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours53");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours54.
@param Hours54 Hours54 */
public void setHours54 (BigDecimal Hours54)
{
set_Value ("Hours54", Hours54);
}
/** Get Hours54.
@return Hours54 */
public BigDecimal getHours54() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours54");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours55.
@param Hours55 Hours55 */
public void setHours55 (BigDecimal Hours55)
{
set_Value ("Hours55", Hours55);
}
/** Get Hours55.
@return Hours55 */
public BigDecimal getHours55() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours55");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours56.
@param Hours56 Hours56 */
public void setHours56 (BigDecimal Hours56)
{
set_Value ("Hours56", Hours56);
}
/** Get Hours56.
@return Hours56 */
public BigDecimal getHours56() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours56");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours57.
@param Hours57 Hours57 */
public void setHours57 (BigDecimal Hours57)
{
set_Value ("Hours57", Hours57);
}
/** Get Hours57.
@return Hours57 */
public BigDecimal getHours57() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours57");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours58.
@param Hours58 Hours58 */
public void setHours58 (BigDecimal Hours58)
{
set_Value ("Hours58", Hours58);
}
/** Get Hours58.
@return Hours58 */
public BigDecimal getHours58() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours58");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours59.
@param Hours59 Hours59 */
public void setHours59 (BigDecimal Hours59)
{
set_Value ("Hours59", Hours59);
}
/** Get Hours59.
@return Hours59 */
public BigDecimal getHours59() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours59");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours60.
@param Hours60 Hours60 */
public void setHours60 (BigDecimal Hours60)
{
set_Value ("Hours60", Hours60);
}
/** Get Hours60.
@return Hours60 */
public BigDecimal getHours60() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours60");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours61.
@param Hours61 Hours61 */
public void setHours61 (BigDecimal Hours61)
{
set_Value ("Hours61", Hours61);
}
/** Get Hours61.
@return Hours61 */
public BigDecimal getHours61() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours61");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours62.
@param Hours62 Hours62 */
public void setHours62 (BigDecimal Hours62)
{
set_Value ("Hours62", Hours62);
}
/** Get Hours62.
@return Hours62 */
public BigDecimal getHours62() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours62");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours63.
@param Hours63 Hours63 */
public void setHours63 (BigDecimal Hours63)
{
set_Value ("Hours63", Hours63);
}
/** Get Hours63.
@return Hours63 */
public BigDecimal getHours63() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours63");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours64.
@param Hours64 Hours64 */
public void setHours64 (BigDecimal Hours64)
{
set_Value ("Hours64", Hours64);
}
/** Get Hours64.
@return Hours64 */
public BigDecimal getHours64() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours64");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours65.
@param Hours65 Hours65 */
public void setHours65 (BigDecimal Hours65)
{
set_Value ("Hours65", Hours65);
}
/** Get Hours65.
@return Hours65 */
public BigDecimal getHours65() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours65");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours66.
@param Hours66 Hours66 */
public void setHours66 (BigDecimal Hours66)
{
set_Value ("Hours66", Hours66);
}
/** Get Hours66.
@return Hours66 */
public BigDecimal getHours66() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours66");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours67.
@param Hours67 Hours67 */
public void setHours67 (BigDecimal Hours67)
{
set_Value ("Hours67", Hours67);
}
/** Get Hours67.
@return Hours67 */
public BigDecimal getHours67() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours67");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours68.
@param Hours68 Hours68 */
public void setHours68 (BigDecimal Hours68)
{
set_Value ("Hours68", Hours68);
}
/** Get Hours68.
@return Hours68 */
public BigDecimal getHours68() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours68");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours69.
@param Hours69 Hours69 */
public void setHours69 (BigDecimal Hours69)
{
set_Value ("Hours69", Hours69);
}
/** Get Hours69.
@return Hours69 */
public BigDecimal getHours69() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours69");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours70.
@param Hours70 Hours70 */
public void setHours70 (BigDecimal Hours70)
{
set_Value ("Hours70", Hours70);
}
/** Get Hours70.
@return Hours70 */
public BigDecimal getHours70() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours70");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours71.
@param Hours71 Hours71 */
public void setHours71 (BigDecimal Hours71)
{
set_Value ("Hours71", Hours71);
}
/** Get Hours71.
@return Hours71 */
public BigDecimal getHours71() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours71");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours72.
@param Hours72 Hours72 */
public void setHours72 (BigDecimal Hours72)
{
set_Value ("Hours72", Hours72);
}
/** Get Hours72.
@return Hours72 */
public BigDecimal getHours72() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours72");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours73.
@param Hours73 Hours73 */
public void setHours73 (BigDecimal Hours73)
{
set_Value ("Hours73", Hours73);
}
/** Get Hours73.
@return Hours73 */
public BigDecimal getHours73() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours73");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours74.
@param Hours74 Hours74 */
public void setHours74 (BigDecimal Hours74)
{
set_Value ("Hours74", Hours74);
}
/** Get Hours74.
@return Hours74 */
public BigDecimal getHours74() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours74");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours75.
@param Hours75 Hours75 */
public void setHours75 (BigDecimal Hours75)
{
set_Value ("Hours75", Hours75);
}
/** Get Hours75.
@return Hours75 */
public BigDecimal getHours75() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours75");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours76.
@param Hours76 Hours76 */
public void setHours76 (BigDecimal Hours76)
{
set_Value ("Hours76", Hours76);
}
/** Get Hours76.
@return Hours76 */
public BigDecimal getHours76() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours76");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours77.
@param Hours77 Hours77 */
public void setHours77 (BigDecimal Hours77)
{
set_Value ("Hours77", Hours77);
}
/** Get Hours77.
@return Hours77 */
public BigDecimal getHours77() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours77");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours78.
@param Hours78 Hours78 */
public void setHours78 (BigDecimal Hours78)
{
set_Value ("Hours78", Hours78);
}
/** Get Hours78.
@return Hours78 */
public BigDecimal getHours78() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours78");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours79.
@param Hours79 Hours79 */
public void setHours79 (BigDecimal Hours79)
{
set_Value ("Hours79", Hours79);
}
/** Get Hours79.
@return Hours79 */
public BigDecimal getHours79() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours79");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours80.
@param Hours80 Hours80 */
public void setHours80 (BigDecimal Hours80)
{
set_Value ("Hours80", Hours80);
}
/** Get Hours80.
@return Hours80 */
public BigDecimal getHours80() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours80");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours81.
@param Hours81 Hours81 */
public void setHours81 (BigDecimal Hours81)
{
set_Value ("Hours81", Hours81);
}
/** Get Hours81.
@return Hours81 */
public BigDecimal getHours81() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours81");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours82.
@param Hours82 Hours82 */
public void setHours82 (BigDecimal Hours82)
{
set_Value ("Hours82", Hours82);
}
/** Get Hours82.
@return Hours82 */
public BigDecimal getHours82() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours82");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours83.
@param Hours83 Hours83 */
public void setHours83 (BigDecimal Hours83)
{
set_Value ("Hours83", Hours83);
}
/** Get Hours83.
@return Hours83 */
public BigDecimal getHours83() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours83");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours84.
@param Hours84 Hours84 */
public void setHours84 (BigDecimal Hours84)
{
set_Value ("Hours84", Hours84);
}
/** Get Hours84.
@return Hours84 */
public BigDecimal getHours84() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours84");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours85.
@param Hours85 Hours85 */
public void setHours85 (BigDecimal Hours85)
{
set_Value ("Hours85", Hours85);
}
/** Get Hours85.
@return Hours85 */
public BigDecimal getHours85() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours85");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours86.
@param Hours86 Hours86 */
public void setHours86 (BigDecimal Hours86)
{
set_Value ("Hours86", Hours86);
}
/** Get Hours86.
@return Hours86 */
public BigDecimal getHours86() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours86");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours87.
@param Hours87 Hours87 */
public void setHours87 (BigDecimal Hours87)
{
set_Value ("Hours87", Hours87);
}
/** Get Hours87.
@return Hours87 */
public BigDecimal getHours87() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours87");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours88.
@param Hours88 Hours88 */
public void setHours88 (BigDecimal Hours88)
{
set_Value ("Hours88", Hours88);
}
/** Get Hours88.
@return Hours88 */
public BigDecimal getHours88() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours88");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours89.
@param Hours89 Hours89 */
public void setHours89 (BigDecimal Hours89)
{
set_Value ("Hours89", Hours89);
}
/** Get Hours89.
@return Hours89 */
public BigDecimal getHours89() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours89");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours90.
@param Hours90 Hours90 */
public void setHours90 (BigDecimal Hours90)
{
set_Value ("Hours90", Hours90);
}
/** Get Hours90.
@return Hours90 */
public BigDecimal getHours90() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours90");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours91.
@param Hours91 Hours91 */
public void setHours91 (BigDecimal Hours91)
{
set_Value ("Hours91", Hours91);
}
/** Get Hours91.
@return Hours91 */
public BigDecimal getHours91() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours91");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours92.
@param Hours92 Hours92 */
public void setHours92 (BigDecimal Hours92)
{
set_Value ("Hours92", Hours92);
}
/** Get Hours92.
@return Hours92 */
public BigDecimal getHours92() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours92");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours93.
@param Hours93 Hours93 */
public void setHours93 (BigDecimal Hours93)
{
set_Value ("Hours93", Hours93);
}
/** Get Hours93.
@return Hours93 */
public BigDecimal getHours93() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours93");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours94.
@param Hours94 Hours94 */
public void setHours94 (BigDecimal Hours94)
{
set_Value ("Hours94", Hours94);
}
/** Get Hours94.
@return Hours94 */
public BigDecimal getHours94() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours94");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours95.
@param Hours95 Hours95 */
public void setHours95 (BigDecimal Hours95)
{
set_Value ("Hours95", Hours95);
}
/** Get Hours95.
@return Hours95 */
public BigDecimal getHours95() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours95");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours96.
@param Hours96 Hours96 */
public void setHours96 (BigDecimal Hours96)
{
set_Value ("Hours96", Hours96);
}
/** Get Hours96.
@return Hours96 */
public BigDecimal getHours96() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours96");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set I_YTD_Gain_ID.
@param I_YTD_Gain_ID I_YTD_Gain_ID */
public void setI_YTD_Gain_ID (int I_YTD_Gain_ID)
{
if (I_YTD_Gain_ID < 1) throw new IllegalArgumentException ("I_YTD_Gain_ID is mandatory.");
set_ValueNoCheck ("I_YTD_Gain_ID", new Integer(I_YTD_Gain_ID));
}
/** Get I_YTD_Gain_ID.
@return I_YTD_Gain_ID */
public int getI_YTD_Gain_ID() 
{
Integer ii = (Integer)get_Value("I_YTD_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
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
