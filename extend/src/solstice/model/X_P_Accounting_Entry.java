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
/** Generated Model for P_Accounting_Entry
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Accounting_Entry extends PO
{
/** Standard Constructor
@param ctx context
@param P_Accounting_Entry_ID id
@param trxName transaction
*/
public X_P_Accounting_Entry (Properties ctx, int P_Accounting_Entry_ID, String trxName)
{
super (ctx, P_Accounting_Entry_ID, trxName);
/** if (P_Accounting_Entry_ID == 0)
{
setDateDoc (new Timestamp(System.currentTimeMillis()));
setDocumentNo (null);
setGl_Journalbatch_ID1 (0);
setGl_Journalbatch_ID2 (0);
setP_Accounting_Entry_ID (0);
setP_Period_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Accounting_Entry (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000147 */
public static final int Table_ID=2000147;

/** TableName=P_Accounting_Entry */
public static final String Table_Name="P_Accounting_Entry";

protected static KeyNamePair Model = new KeyNamePair(2000147,"P_Accounting_Entry");

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
StringBuffer sb = new StringBuffer ("X_P_Accounting_Entry[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Accounted Credit.
@param AmtAcctCr Accounted Credit Amount */
public void setAmtAcctCr (BigDecimal AmtAcctCr)
{
set_ValueNoCheck ("AmtAcctCr", AmtAcctCr);
}
/** Get Accounted Credit.
@return Accounted Credit Amount */
public BigDecimal getAmtAcctCr() 
{
BigDecimal bd = (BigDecimal)get_Value("AmtAcctCr");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Accounted Debit.
@param AmtAcctDr Accounted Debit Amount */
public void setAmtAcctDr (BigDecimal AmtAcctDr)
{
set_ValueNoCheck ("AmtAcctDr", AmtAcctDr);
}
/** Get Accounted Debit.
@return Accounted Debit Amount */
public BigDecimal getAmtAcctDr() 
{
BigDecimal bd = (BigDecimal)get_Value("AmtAcctDr");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Document Date.
@param DateDoc Date of the Document */
public void setDateDoc (Timestamp DateDoc)
{
if (DateDoc == null) throw new IllegalArgumentException ("DateDoc is mandatory.");
set_Value ("DateDoc", DateDoc);
}
/** Get Document Date.
@return Date of the Document */
public Timestamp getDateDoc() 
{
return (Timestamp)get_Value("DateDoc");
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 30)
{
log.warning("Length > 30 - truncated");
Description = Description.substring(0,29);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set Document No.
@param DocumentNo Document sequence number of the document */
public void setDocumentNo (String DocumentNo)
{
if (DocumentNo == null) throw new IllegalArgumentException ("DocumentNo is mandatory.");
if (DocumentNo.length() > 30)
{
log.warning("Length > 30 - truncated");
DocumentNo = DocumentNo.substring(0,29);
}
set_Value ("DocumentNo", DocumentNo);
}
/** Get Document No.
@return Document sequence number of the document */
public String getDocumentNo() 
{
return (String)get_Value("DocumentNo");
}
/** Set Entry 1.
@param Gl_Journalbatch_ID1 Entry 1 */
public void setGl_Journalbatch_ID1 (int Gl_Journalbatch_ID1)
{
set_Value ("Gl_Journalbatch_ID1", new Integer(Gl_Journalbatch_ID1));
}
/** Get Entry 1.
@return Entry 1 */
public int getGl_Journalbatch_ID1() 
{
Integer ii = (Integer)get_Value("Gl_Journalbatch_ID1");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Entry 2.
@param Gl_Journalbatch_ID2 Entry 2 */
public void setGl_Journalbatch_ID2 (int Gl_Journalbatch_ID2)
{
set_Value ("Gl_Journalbatch_ID2", new Integer(Gl_Journalbatch_ID2));
}
/** Get Entry 2.
@return Entry 2 */
public int getGl_Journalbatch_ID2() 
{
Integer ii = (Integer)get_Value("Gl_Journalbatch_ID2");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Document No.
@param P_Accounting_Entry_ID Document No */
public void setP_Accounting_Entry_ID (int P_Accounting_Entry_ID)
{
if (P_Accounting_Entry_ID < 1) throw new IllegalArgumentException ("P_Accounting_Entry_ID is mandatory.");
set_Value ("P_Accounting_Entry_ID", new Integer(P_Accounting_Entry_ID));
}
/** Get Document No.
@return Document No */
public int getP_Accounting_Entry_ID() 
{
Integer ii = (Integer)get_Value("P_Accounting_Entry_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Payment Group.
@param P_Payment_Group_ID Payment Group */
public void setP_Payment_Group_ID (int P_Payment_Group_ID)
{
if (P_Payment_Group_ID <= 0) set_Value ("P_Payment_Group_ID", null);
 else 
set_Value ("P_Payment_Group_ID", new Integer(P_Payment_Group_ID));
}
/** Get Payment Group.
@return Payment Group */
public int getP_Payment_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Period_ID AD_Reference_ID=2000009 */
public static final int P_PERIOD_ID_AD_Reference_ID=2000009;
/** Set Period.
@param P_Period_ID Period of the Calendar   */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID < 1) throw new IllegalArgumentException ("P_Period_ID is mandatory.");
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
/** Set RegularWriting.
@param RegularWriting RegularWriting */
public void setRegularWriting (boolean RegularWriting)
{
set_Value ("RegularWriting", new Boolean(RegularWriting));
}
/** Get RegularWriting.
@return RegularWriting */
public boolean isRegularWriting() 
{
Object oo = get_Value("RegularWriting");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
}
