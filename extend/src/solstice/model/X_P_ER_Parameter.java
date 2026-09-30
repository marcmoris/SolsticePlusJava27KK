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
/** Generated Model for P_ER_Parameter
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_ER_Parameter extends PO
{
/** Standard Constructor
@param ctx context
@param P_ER_Parameter_ID id
@param trxName transaction
*/
public X_P_ER_Parameter (Properties ctx, int P_ER_Parameter_ID, String trxName)
{
super (ctx, P_ER_Parameter_ID, trxName);
/** if (P_ER_Parameter_ID == 0)
{
setDayOfMonth (0);
setMinimumDuration (0);
setNewCol1 (null);
setNewCol2 (null);
setNewFooter (null);
setNewHeader (null);
setNoticeCol1 (null);
setNoticeCol2 (null);
setNoticeCol3 (null);
setNoticeCol4 (null);
setNoticeFooter (null);
setNoticeHeader (null);
setP_ER_Parameter_ID (0);
setRecallCol1 (null);
setRecallCol2 (null);
setRecallCol3 (null);
setRecallCol4 (null);
setRecallFooter (null);
setRecallHeader (null);
setTransfertCol1 (null);
setTransfertCol2 (null);
setTransfertCol3 (null);
setTransfertFooter (null);
setTransfertHeader (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_ER_Parameter (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100837 */
public static final int Table_ID=2100837;

/** TableName=P_ER_Parameter */
public static final String Table_Name="P_ER_Parameter";

protected static KeyNamePair Model = new KeyNamePair(2100837,"P_ER_Parameter");

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
StringBuffer sb = new StringBuffer ("X_P_ER_Parameter[").append(get_ID()).append("]");
return sb.toString();
}
/** Set DAYOFMONTH.
@param DayOfMonth DAYOFMONTH */
public void setDayOfMonth (int DayOfMonth)
{
set_Value ("DayOfMonth", new Integer(DayOfMonth));
}
/** Get DAYOFMONTH.
@return DAYOFMONTH */
public int getDayOfMonth() 
{
Integer ii = (Integer)get_Value("DayOfMonth");
if (ii == null) return 0;
return ii.intValue();
}
/** Set MINIMUMDURATION.
@param MinimumDuration MINIMUMDURATION */
public void setMinimumDuration (int MinimumDuration)
{
set_Value ("MinimumDuration", new Integer(MinimumDuration));
}
/** Get MINIMUMDURATION.
@return MINIMUMDURATION */
public int getMinimumDuration() 
{
Integer ii = (Integer)get_Value("MinimumDuration");
if (ii == null) return 0;
return ii.intValue();
}
/** Set NEWCOL1.
@param NewCol1 NEWCOL1 */
public void setNewCol1 (String NewCol1)
{
if (NewCol1 == null) throw new IllegalArgumentException ("NewCol1 is mandatory.");
if (NewCol1.length() > 50)
{
log.warning("Length > 50 - truncated");
NewCol1 = NewCol1.substring(0,49);
}
set_Value ("NewCol1", NewCol1);
}
/** Get NEWCOL1.
@return NEWCOL1 */
public String getNewCol1() 
{
return (String)get_Value("NewCol1");
}
/** Set NEWCOL2.
@param NewCol2 NEWCOL2 */
public void setNewCol2 (String NewCol2)
{
if (NewCol2 == null) throw new IllegalArgumentException ("NewCol2 is mandatory.");
if (NewCol2.length() > 50)
{
log.warning("Length > 50 - truncated");
NewCol2 = NewCol2.substring(0,49);
}
set_Value ("NewCol2", NewCol2);
}
/** Get NEWCOL2.
@return NEWCOL2 */
public String getNewCol2() 
{
return (String)get_Value("NewCol2");
}
/** Set NEWFOOTER.
@param NewFooter NEWFOOTER */
public void setNewFooter (String NewFooter)
{
if (NewFooter == null) throw new IllegalArgumentException ("NewFooter is mandatory.");
if (NewFooter.length() > 500)
{
log.warning("Length > 500 - truncated");
NewFooter = NewFooter.substring(0,499);
}
set_Value ("NewFooter", NewFooter);
}
/** Get NEWFOOTER.
@return NEWFOOTER */
public String getNewFooter() 
{
return (String)get_Value("NewFooter");
}
/** Set NEWHEADER.
@param NewHeader NEWHEADER */
public void setNewHeader (String NewHeader)
{
if (NewHeader == null) throw new IllegalArgumentException ("NewHeader is mandatory.");
if (NewHeader.length() > 500)
{
log.warning("Length > 500 - truncated");
NewHeader = NewHeader.substring(0,499);
}
set_Value ("NewHeader", NewHeader);
}
/** Get NEWHEADER.
@return NEWHEADER */
public String getNewHeader() 
{
return (String)get_Value("NewHeader");
}
/** Set NOTICECOL1.
@param NoticeCol1 NOTICECOL1 */
public void setNoticeCol1 (String NoticeCol1)
{
if (NoticeCol1 == null) throw new IllegalArgumentException ("NoticeCol1 is mandatory.");
if (NoticeCol1.length() > 50)
{
log.warning("Length > 50 - truncated");
NoticeCol1 = NoticeCol1.substring(0,49);
}
set_Value ("NoticeCol1", NoticeCol1);
}
/** Get NOTICECOL1.
@return NOTICECOL1 */
public String getNoticeCol1() 
{
return (String)get_Value("NoticeCol1");
}
/** Set NOTICECOL2.
@param NoticeCol2 NOTICECOL2 */
public void setNoticeCol2 (String NoticeCol2)
{
if (NoticeCol2 == null) throw new IllegalArgumentException ("NoticeCol2 is mandatory.");
if (NoticeCol2.length() > 50)
{
log.warning("Length > 50 - truncated");
NoticeCol2 = NoticeCol2.substring(0,49);
}
set_Value ("NoticeCol2", NoticeCol2);
}
/** Get NOTICECOL2.
@return NOTICECOL2 */
public String getNoticeCol2() 
{
return (String)get_Value("NoticeCol2");
}
/** Set NOTICECOL3.
@param NoticeCol3 NOTICECOL3 */
public void setNoticeCol3 (String NoticeCol3)
{
if (NoticeCol3 == null) throw new IllegalArgumentException ("NoticeCol3 is mandatory.");
if (NoticeCol3.length() > 50)
{
log.warning("Length > 50 - truncated");
NoticeCol3 = NoticeCol3.substring(0,49);
}
set_Value ("NoticeCol3", NoticeCol3);
}
/** Get NOTICECOL3.
@return NOTICECOL3 */
public String getNoticeCol3() 
{
return (String)get_Value("NoticeCol3");
}
/** Set NOTICECOL4.
@param NoticeCol4 NOTICECOL4 */
public void setNoticeCol4 (String NoticeCol4)
{
if (NoticeCol4 == null) throw new IllegalArgumentException ("NoticeCol4 is mandatory.");
if (NoticeCol4.length() > 50)
{
log.warning("Length > 50 - truncated");
NoticeCol4 = NoticeCol4.substring(0,49);
}
set_Value ("NoticeCol4", NoticeCol4);
}
/** Get NOTICECOL4.
@return NOTICECOL4 */
public String getNoticeCol4() 
{
return (String)get_Value("NoticeCol4");
}
/** Set NOTICEFOOTER.
@param NoticeFooter NOTICEFOOTER */
public void setNoticeFooter (String NoticeFooter)
{
if (NoticeFooter == null) throw new IllegalArgumentException ("NoticeFooter is mandatory.");
if (NoticeFooter.length() > 500)
{
log.warning("Length > 500 - truncated");
NoticeFooter = NoticeFooter.substring(0,499);
}
set_Value ("NoticeFooter", NoticeFooter);
}
/** Get NOTICEFOOTER.
@return NOTICEFOOTER */
public String getNoticeFooter() 
{
return (String)get_Value("NoticeFooter");
}
/** Set NOTICEHEADER.
@param NoticeHeader NOTICEHEADER */
public void setNoticeHeader (String NoticeHeader)
{
if (NoticeHeader == null) throw new IllegalArgumentException ("NoticeHeader is mandatory.");
if (NoticeHeader.length() > 500)
{
log.warning("Length > 500 - truncated");
NoticeHeader = NoticeHeader.substring(0,499);
}
set_Value ("NoticeHeader", NoticeHeader);
}
/** Get NOTICEHEADER.
@return NOTICEHEADER */
public String getNoticeHeader() 
{
return (String)get_Value("NoticeHeader");
}
/** Set Assignment State.
@param P_Assignment_State_ID Assignment State */
public void setP_Assignment_State_ID (int P_Assignment_State_ID)
{
if (P_Assignment_State_ID <= 0) set_Value ("P_Assignment_State_ID", null);
 else 
set_Value ("P_Assignment_State_ID", new Integer(P_Assignment_State_ID));
}
/** Get Assignment State.
@return Assignment State */
public int getP_Assignment_State_ID() 
{
Integer ii = (Integer)get_Value("P_Assignment_State_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_ER_PARAMETER_ID.
@param P_ER_Parameter_ID P_ER_PARAMETER_ID */
public void setP_ER_Parameter_ID (int P_ER_Parameter_ID)
{
if (P_ER_Parameter_ID < 1) throw new IllegalArgumentException ("P_ER_Parameter_ID is mandatory.");
set_ValueNoCheck ("P_ER_Parameter_ID", new Integer(P_ER_Parameter_ID));
}
/** Get P_ER_PARAMETER_ID.
@return P_ER_PARAMETER_ID */
public int getP_ER_Parameter_ID() 
{
Integer ii = (Integer)get_Value("P_ER_Parameter_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set RECALLCOL1.
@param RecallCol1 RECALLCOL1 */
public void setRecallCol1 (String RecallCol1)
{
if (RecallCol1 == null) throw new IllegalArgumentException ("RecallCol1 is mandatory.");
if (RecallCol1.length() > 50)
{
log.warning("Length > 50 - truncated");
RecallCol1 = RecallCol1.substring(0,49);
}
set_Value ("RecallCol1", RecallCol1);
}
/** Get RECALLCOL1.
@return RECALLCOL1 */
public String getRecallCol1() 
{
return (String)get_Value("RecallCol1");
}
/** Set RECALLCOL2.
@param RecallCol2 RECALLCOL2 */
public void setRecallCol2 (String RecallCol2)
{
if (RecallCol2 == null) throw new IllegalArgumentException ("RecallCol2 is mandatory.");
if (RecallCol2.length() > 50)
{
log.warning("Length > 50 - truncated");
RecallCol2 = RecallCol2.substring(0,49);
}
set_Value ("RecallCol2", RecallCol2);
}
/** Get RECALLCOL2.
@return RECALLCOL2 */
public String getRecallCol2() 
{
return (String)get_Value("RecallCol2");
}
/** Set RECALLCOL3.
@param RecallCol3 RECALLCOL3 */
public void setRecallCol3 (String RecallCol3)
{
if (RecallCol3 == null) throw new IllegalArgumentException ("RecallCol3 is mandatory.");
if (RecallCol3.length() > 50)
{
log.warning("Length > 50 - truncated");
RecallCol3 = RecallCol3.substring(0,49);
}
set_Value ("RecallCol3", RecallCol3);
}
/** Get RECALLCOL3.
@return RECALLCOL3 */
public String getRecallCol3() 
{
return (String)get_Value("RecallCol3");
}
/** Set RECALLCOL4.
@param RecallCol4 RECALLCOL4 */
public void setRecallCol4 (String RecallCol4)
{
if (RecallCol4 == null) throw new IllegalArgumentException ("RecallCol4 is mandatory.");
if (RecallCol4.length() > 50)
{
log.warning("Length > 50 - truncated");
RecallCol4 = RecallCol4.substring(0,49);
}
set_Value ("RecallCol4", RecallCol4);
}
/** Get RECALLCOL4.
@return RECALLCOL4 */
public String getRecallCol4() 
{
return (String)get_Value("RecallCol4");
}
/** Set RECALLFOOTER.
@param RecallFooter RECALLFOOTER */
public void setRecallFooter (String RecallFooter)
{
if (RecallFooter == null) throw new IllegalArgumentException ("RecallFooter is mandatory.");
if (RecallFooter.length() > 500)
{
log.warning("Length > 500 - truncated");
RecallFooter = RecallFooter.substring(0,499);
}
set_Value ("RecallFooter", RecallFooter);
}
/** Get RECALLFOOTER.
@return RECALLFOOTER */
public String getRecallFooter() 
{
return (String)get_Value("RecallFooter");
}
/** Set RECALLHEADER.
@param RecallHeader RECALLHEADER */
public void setRecallHeader (String RecallHeader)
{
if (RecallHeader == null) throw new IllegalArgumentException ("RecallHeader is mandatory.");
if (RecallHeader.length() > 500)
{
log.warning("Length > 500 - truncated");
RecallHeader = RecallHeader.substring(0,499);
}
set_Value ("RecallHeader", RecallHeader);
}
/** Get RECALLHEADER.
@return RECALLHEADER */
public String getRecallHeader() 
{
return (String)get_Value("RecallHeader");
}
/** Set TRANSFERTCOL1.
@param TransfertCol1 TRANSFERTCOL1 */
public void setTransfertCol1 (String TransfertCol1)
{
if (TransfertCol1 == null) throw new IllegalArgumentException ("TransfertCol1 is mandatory.");
if (TransfertCol1.length() > 50)
{
log.warning("Length > 50 - truncated");
TransfertCol1 = TransfertCol1.substring(0,49);
}
set_Value ("TransfertCol1", TransfertCol1);
}
/** Get TRANSFERTCOL1.
@return TRANSFERTCOL1 */
public String getTransfertCol1() 
{
return (String)get_Value("TransfertCol1");
}
/** Set TRANSFERTCOL2.
@param TransfertCol2 TRANSFERTCOL2 */
public void setTransfertCol2 (String TransfertCol2)
{
if (TransfertCol2 == null) throw new IllegalArgumentException ("TransfertCol2 is mandatory.");
if (TransfertCol2.length() > 50)
{
log.warning("Length > 50 - truncated");
TransfertCol2 = TransfertCol2.substring(0,49);
}
set_Value ("TransfertCol2", TransfertCol2);
}
/** Get TRANSFERTCOL2.
@return TRANSFERTCOL2 */
public String getTransfertCol2() 
{
return (String)get_Value("TransfertCol2");
}
/** Set TRANSFERTCOL3.
@param TransfertCol3 TRANSFERTCOL3 */
public void setTransfertCol3 (String TransfertCol3)
{
if (TransfertCol3 == null) throw new IllegalArgumentException ("TransfertCol3 is mandatory.");
if (TransfertCol3.length() > 50)
{
log.warning("Length > 50 - truncated");
TransfertCol3 = TransfertCol3.substring(0,49);
}
set_Value ("TransfertCol3", TransfertCol3);
}
/** Get TRANSFERTCOL3.
@return TRANSFERTCOL3 */
public String getTransfertCol3() 
{
return (String)get_Value("TransfertCol3");
}
/** Set TRANSFERTFOOTER.
@param TransfertFooter TRANSFERTFOOTER */
public void setTransfertFooter (String TransfertFooter)
{
if (TransfertFooter == null) throw new IllegalArgumentException ("TransfertFooter is mandatory.");
if (TransfertFooter.length() > 500)
{
log.warning("Length > 500 - truncated");
TransfertFooter = TransfertFooter.substring(0,499);
}
set_Value ("TransfertFooter", TransfertFooter);
}
/** Get TRANSFERTFOOTER.
@return TRANSFERTFOOTER */
public String getTransfertFooter() 
{
return (String)get_Value("TransfertFooter");
}
/** Set TRANSFERTHEADER.
@param TransfertHeader TRANSFERTHEADER */
public void setTransfertHeader (String TransfertHeader)
{
if (TransfertHeader == null) throw new IllegalArgumentException ("TransfertHeader is mandatory.");
if (TransfertHeader.length() > 500)
{
log.warning("Length > 500 - truncated");
TransfertHeader = TransfertHeader.substring(0,499);
}
set_Value ("TransfertHeader", TransfertHeader);
}
/** Get TRANSFERTHEADER.
@return TRANSFERTHEADER */
public String getTransfertHeader() 
{
return (String)get_Value("TransfertHeader");
}
}
