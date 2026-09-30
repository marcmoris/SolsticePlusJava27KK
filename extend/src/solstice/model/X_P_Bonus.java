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
/** Generated Model for P_Bonus
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Bonus extends PO
{
/** Standard Constructor
@param ctx context
@param P_Bonus_ID id
@param trxName transaction
*/
public X_P_Bonus (Properties ctx, int P_Bonus_ID, String trxName)
{
super (ctx, P_Bonus_ID, trxName);
/** if (P_Bonus_ID == 0)
{
setIsLocalization (false);	// N
setIsPost (false);	// N
setIsSchedule (false);	// N
setIsWeekly (false);	// N
setName (null);
setP_Bonus_ID (0);
setP_Gain_ID (0);
setP_Post_ID (0);
setP_Schedule_ID (0);
setP_Workplace_ID (0);
setStartDate (new Timestamp(System.currentTimeMillis()));
setWeekly (true);	// Y
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Bonus (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000110 */
public static final int Table_ID=2000110;

/** TableName=P_Bonus */
public static final String Table_Name="P_Bonus";

protected static KeyNamePair Model = new KeyNamePair(2000110,"P_Bonus");

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
StringBuffer sb = new StringBuffer ("X_P_Bonus[").append(get_ID()).append("]");
return sb.toString();
}
/** Set End Date.
@param EndDate Last effective date (inclusive) */
public void setEndDate (Timestamp EndDate)
{
set_Value ("EndDate", EndDate);
}
/** Get End Date.
@return Last effective date (inclusive) */
public Timestamp getEndDate() 
{
return (Timestamp)get_Value("EndDate");
}
/** Set Friday.
@param Friday Friday */
public void setFriday (boolean Friday)
{
set_Value ("Friday", new Boolean(Friday));
}
/** Get Friday.
@return Friday */
public boolean isFriday() 
{
Object oo = get_Value("Friday");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set L'employé travaille à une localisation en particulier.
@param IsLocalization L'employé travaille à une localisation en particulier */
public void setIsLocalization (boolean IsLocalization)
{
set_Value ("IsLocalization", new Boolean(IsLocalization));
}
/** Get L'employé travaille à une localisation en particulier.
@return L'employé travaille à une localisation en particulier */
public boolean isLocalization() 
{
Object oo = get_Value("IsLocalization");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set L'employé travaille sur un poste particulier.
@param IsPost L'employé travaille sur un poste particulier */
public void setIsPost (boolean IsPost)
{
set_Value ("IsPost", new Boolean(IsPost));
}
/** Get L'employé travaille sur un poste particulier.
@return L'employé travaille sur un poste particulier */
public boolean isPost() 
{
Object oo = get_Value("IsPost");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set L'employé travaille sur un horaire particulier.
@param IsSchedule L'employé travaille sur un horaire particulier */
public void setIsSchedule (boolean IsSchedule)
{
set_Value ("IsSchedule", new Boolean(IsSchedule));
}
/** Get L'employé travaille sur un horaire particulier.
@return L'employé travaille sur un horaire particulier */
public boolean isSchedule() 
{
Object oo = get_Value("IsSchedule");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set L'employé a un solde de banque.
@param IsTotalCredits L'employé a un solde de banque */
public void setIsTotalCredits (boolean IsTotalCredits)
{
set_Value ("IsTotalCredits", new Boolean(IsTotalCredits));
}
/** Get L'employé a un solde de banque.
@return L'employé a un solde de banque */
public boolean isTotalCredits() 
{
Object oo = get_Value("IsTotalCredits");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set L'employé travaille certain jours de la semaine.
@param IsWeekly L'employé travaille certain jours de la semaine */
public void setIsWeekly (boolean IsWeekly)
{
set_Value ("IsWeekly", new Boolean(IsWeekly));
}

/** Set Monday.
@param Monday Monday */
public void setMonday (boolean Monday)
{
set_Value ("Monday", new Boolean(Monday));
}
/** Get Monday.
@return Monday */
public boolean isMonday() 
{
Object oo = get_Value("Monday");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 120)
{
log.warning("Length > 120 - truncated");
Name = Name.substring(0,119);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Set Bonus.
@param P_Bonus_ID Bonus */
public void setP_Bonus_ID (int P_Bonus_ID)
{
if (P_Bonus_ID < 1) throw new IllegalArgumentException ("P_Bonus_ID is mandatory.");
set_Value ("P_Bonus_ID", new Integer(P_Bonus_ID));
}
/** Get Bonus.
@return Bonus */
public int getP_Bonus_ID() 
{
Integer ii = (Integer)get_Value("P_Bonus_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Credits_ID AD_Reference_ID=2100318 */
public static final int P_CREDITS_ID_AD_Reference_ID=2100318;
/** Set Credits.
@param P_Credits_ID Credits */
public void setP_Credits_ID (int P_Credits_ID)
{
if (P_Credits_ID <= 0) set_Value ("P_Credits_ID", null);
 else 
set_Value ("P_Credits_ID", new Integer(P_Credits_ID));
}
/** Get Credits.
@return Credits */
public int getP_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_ID AD_Reference_ID=2100317 */
public static final int P_GAIN_ID_AD_Reference_ID=2100317;
/** Set Gain.
@param P_Gain_ID Gain */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID < 1) throw new IllegalArgumentException ("P_Gain_ID is mandatory.");
set_Value ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Gain.
@return Gain */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
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
if (P_Schedule_ID < 1) throw new IllegalArgumentException ("P_Schedule_ID is mandatory.");
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

/** P_Workplace_ID AD_Reference_ID=2000005 */
public static final int P_WORKPLACE_ID_AD_Reference_ID=2000005;
/** Set Workplace .
@param P_Workplace_ID Workplace  */
public void setP_Workplace_ID (int P_Workplace_ID)
{
if (P_Workplace_ID < 1) throw new IllegalArgumentException ("P_Workplace_ID is mandatory.");
set_Value ("P_Workplace_ID", new Integer(P_Workplace_ID));
}
/** Get Workplace .
@return Workplace  */
public int getP_Workplace_ID() 
{
Integer ii = (Integer)get_Value("P_Workplace_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Saturday.
@param Saturday Saturday */
public void setSaturday (boolean Saturday)
{
set_Value ("Saturday", new Boolean(Saturday));
}
/** Get Saturday.
@return Saturday */
public boolean isSaturday() 
{
Object oo = get_Value("Saturday");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Start Date.
@param StartDate First effective day (inclusive) */
public void setStartDate (Timestamp StartDate)
{
if (StartDate == null) throw new IllegalArgumentException ("StartDate is mandatory.");
set_Value ("StartDate", StartDate);
}
/** Get Start Date.
@return First effective day (inclusive) */
public Timestamp getStartDate() 
{
return (Timestamp)get_Value("StartDate");
}
/** Set Sunday.
@param Sunday Sunday */
public void setSunday (boolean Sunday)
{
set_Value ("Sunday", new Boolean(Sunday));
}
/** Get Sunday.
@return Sunday */
public boolean isSunday() 
{
Object oo = get_Value("Sunday");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Thursday.
@param Thursday Thursday */
public void setThursday (boolean Thursday)
{
set_Value ("Thursday", new Boolean(Thursday));
}
/** Get Thursday.
@return Thursday */
public boolean isThursday() 
{
Object oo = get_Value("Thursday");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Solde Maximum.
@param TotalCreditsMax Solde Maximum */
public void setTotalCreditsMax (BigDecimal TotalCreditsMax)
{
set_Value ("TotalCreditsMax", TotalCreditsMax);
}
/** Get Solde Maximum.
@return Solde Maximum */
public BigDecimal getTotalCreditsMax() 
{
BigDecimal bd = (BigDecimal)get_Value("TotalCreditsMax");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Solde Minimum.
@param TotalCreditsMin Solde Minimum */
public void setTotalCreditsMin (BigDecimal TotalCreditsMin)
{
set_Value ("TotalCreditsMin", TotalCreditsMin);
}
/** Get Solde Minimum.
@return Solde Minimum */
public BigDecimal getTotalCreditsMin() 
{
BigDecimal bd = (BigDecimal)get_Value("TotalCreditsMin");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Tuesday.
@param Tuesday Tuesday */
public void setTuesday (boolean Tuesday)
{
set_Value ("Tuesday", new Boolean(Tuesday));
}
/** Get Tuesday.
@return Tuesday */
public boolean isTuesday() 
{
Object oo = get_Value("Tuesday");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Wednesday.
@param Wednesday Wednesday */
public void setWednesday (boolean Wednesday)
{
set_Value ("Wednesday", new Boolean(Wednesday));
}
/** Get Wednesday.
@return Wednesday */
public boolean isWednesday() 
{
Object oo = get_Value("Wednesday");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Weekly.
@param Weekly Number of Weekly   */
public void setWeekly (boolean Weekly)
{
set_Value ("Weekly", new Boolean(Weekly));
}
/** Get Weekly.
@return Number of Weekly   */
public boolean isWeekly() 
{
Object oo = get_Value("Weekly");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
}
