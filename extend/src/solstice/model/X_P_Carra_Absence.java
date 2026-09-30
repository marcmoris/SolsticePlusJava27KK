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
/** Generated Model for P_Carra_Absence
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_P_Carra_Absence.java,v 1.2 2008/11/13 19:32:27 marmor01 Exp $ */
public class X_P_Carra_Absence extends PO
{
/** Standard Constructor
@param ctx context
@param P_Carra_Absence_ID id
@param trxName transaction
*/
public X_P_Carra_Absence (Properties ctx, int P_Carra_Absence_ID, String trxName)
{
super (ctx, P_Carra_Absence_ID, trxName);
/** if (P_Carra_Absence_ID == 0)
{
setAbsenceCode (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Carra_Absence (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100902 */
public static final int Table_ID=2100902;

/** TableName=P_Carra_Absence */
public static final String Table_Name="P_Carra_Absence";

protected static KeyNamePair Model = new KeyNamePair(2100902,"P_Carra_Absence");

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
StringBuffer sb = new StringBuffer ("X_P_Carra_Absence[").append(get_ID()).append("]");
return sb.toString();
}

/** AbsenceCode AD_Reference_ID=2100473 */
public static final int ABSENCECODE_AD_Reference_ID=2100473;
/** Absence avec salaire exonéré = A1 */
public static final String ABSENCECODE_AbsenceAvecSalaireExonéré = "A1";
/** Congé de maternité = B1 */
public static final String ABSENCECODE_CongéDeMaternité = "B1";
/** Congé sabbatique à traitement différé période de congé = C1 */
public static final String ABSENCECODE_CongéSabbatiqueÀTraitementDifféréPériodeDeCongé = "C1";
/** Congé sabbatique à traitement différé période de travail = C2 */
public static final String ABSENCECODE_CongéSabbatiqueÀTraitementDifféréPériodeDeTravail = "C2";
/** Mise en disponibilité = C3 */
public static final String ABSENCECODE_MiseEnDisponibilité = "C3";
/** Préretraite = C4 */
public static final String ABSENCECODE_Préretraite = "C4";
/** Départ progressif = D1 */
public static final String ABSENCECODE_DépartProgressif = "D1";
/** Absence sans salaire à temps plein = D2 */
public static final String ABSENCECODE_AbsenceSansSalaireÀTempsPlein = "D2";
/** Absence pour maladie ou invalidité = D3 */
public static final String ABSENCECODE_AbsencePourMaladieOuInvalidité = "D3";
/** Absence pour obligations familiales ou parentales = D4 */
public static final String ABSENCECODE_AbsencePourObligationsFamilialesOuParentales = "D4";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isAbsenceCodeValid (String test)
{
return test.equals("A1") || test.equals("B1") || test.equals("C1") || test.equals("C2") || test.equals("C3") || test.equals("C4") || test.equals("D1") || test.equals("D2") || test.equals("D3") || test.equals("D4");
}
/** Set AbsenceCode.
@param AbsenceCode AbsenceCode */
public void setAbsenceCode (String AbsenceCode)
{
if (AbsenceCode == null) throw new IllegalArgumentException ("AbsenceCode is mandatory");
if (!isAbsenceCodeValid(AbsenceCode))
throw new IllegalArgumentException ("AbsenceCode Invalid value - " + AbsenceCode + " - Reference_ID=2100473 - A1 - B1 - C1 - C2 - C3 - C4 - D1 - D2 - D3 - D4");
if (AbsenceCode.length() > 2)
{
log.warning("Length > 2 - truncated");
AbsenceCode = AbsenceCode.substring(0,1);
}
set_Value ("AbsenceCode", AbsenceCode);
}
/** Get AbsenceCode.
@return AbsenceCode */
public String getAbsenceCode() 
{
return (String)get_Value("AbsenceCode");
}
/** Set AbsenceRetroAmt.
@param AbsenceRetroAmt AbsenceRetroAmt */
public void setAbsenceRetroAmt (BigDecimal AbsenceRetroAmt)
{
set_Value ("AbsenceRetroAmt", AbsenceRetroAmt);
}
/** Get AbsenceRetroAmt.
@return AbsenceRetroAmt */
public BigDecimal getAbsenceRetroAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("AbsenceRetroAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set AbsenceSalary.
@param AbsenceSalary AbsenceSalary */
public void setAbsenceSalary (BigDecimal AbsenceSalary)
{
set_Value ("AbsenceSalary", AbsenceSalary);
}
/** Get AbsenceSalary.
@return AbsenceSalary */
public BigDecimal getAbsenceSalary() 
{
BigDecimal bd = (BigDecimal)get_Value("AbsenceSalary");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set NbrDays.
@param NbrDays NbrDays */
public void setNbrDays (BigDecimal NbrDays)
{
set_Value ("NbrDays", NbrDays);
}
/** Get NbrDays.
@return NbrDays */
public BigDecimal getNbrDays() 
{
BigDecimal bd = (BigDecimal)get_Value("NbrDays");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set P_Carra_Absence_ID.
@param P_Carra_Absence_ID P_Carra_Absence_ID */
public void setP_Carra_Absence_ID (int P_Carra_Absence_ID)
{
if (P_Carra_Absence_ID <= 0) set_ValueNoCheck ("P_Carra_Absence_ID", null);
 else 
set_ValueNoCheck ("P_Carra_Absence_ID", new Integer(P_Carra_Absence_ID));
}
/** Get P_Carra_Absence_ID.
@return P_Carra_Absence_ID */
public int getP_Carra_Absence_ID() 
{
Integer ii = (Integer)get_Value("P_Carra_Absence_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Carra_ID.
@param P_Carra_ID P_Carra_ID */
public void setP_Carra_ID (int P_Carra_ID)
{
if (P_Carra_ID <= 0) set_Value ("P_Carra_ID", null);
 else 
set_Value ("P_Carra_ID", new Integer(P_Carra_ID));
}
/** Get P_Carra_ID.
@return P_Carra_ID */
public int getP_Carra_ID() 
{
Integer ii = (Integer)get_Value("P_Carra_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
