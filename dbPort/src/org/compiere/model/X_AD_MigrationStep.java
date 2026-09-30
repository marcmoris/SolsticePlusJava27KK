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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for AD_MigrationStep
 *  @author Jorg Janke (generated) 
 *  @version Release 2.6.1 - $Id: X_AD_MigrationStep.java,v 1.1 2007/07/18 15:00:39 marmor01 Exp $ */
public class X_AD_MigrationStep extends PO
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Standard Constructor
@param ctx context
@param AD_MigrationStep_ID id
@param trxName transaction
*/
public X_AD_MigrationStep (Properties ctx, int AD_MigrationStep_ID, String trxName)
{
super (ctx, AD_MigrationStep_ID, trxName);
/** if (AD_MigrationStep_ID == 0)
{
setAD_MigrationStep_ID (0);
setAD_Version_ID (0);
setName (null);
setSeqNo (0);
setTimingType (null);	// B
setType (null);	// S
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_AD_MigrationStep (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=922 */
public static final int Table_ID=922;

/** TableName=AD_MigrationStep */
public static final String Table_Name="AD_MigrationStep";

protected static KeyNamePair Model = new KeyNamePair(922,"AD_MigrationStep");

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
StringBuffer sb = new StringBuffer ("X_AD_MigrationStep[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Migration Step.
@param AD_MigrationStep_ID Migration Step */
public void setAD_MigrationStep_ID (int AD_MigrationStep_ID)
{
if (AD_MigrationStep_ID < 1) throw new IllegalArgumentException ("AD_MigrationStep_ID is mandatory.");
set_ValueNoCheck ("AD_MigrationStep_ID", new Integer(AD_MigrationStep_ID));
}
/** Get Migration Step.
@return Migration Step */
public int getAD_MigrationStep_ID() 
{
Integer ii = (Integer)get_Value("AD_MigrationStep_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Entity Version.
@param AD_Version_ID Entity Version */
public void setAD_Version_ID (int AD_Version_ID)
{
if (AD_Version_ID < 1) throw new IllegalArgumentException ("AD_Version_ID is mandatory.");
set_ValueNoCheck ("AD_Version_ID", new Integer(AD_Version_ID));
}
/** Get Entity Version.
@return Entity Version */
public int getAD_Version_ID() 
{
Integer ii = (Integer)get_Value("AD_Version_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Classname.
@param Classname Java Classname */
public void setClassname (String Classname)
{
if (Classname != null && Classname.length() > 60)
{
log.warning("Length > 60 - truncated");
Classname = Classname.substring(0,59);
}
set_Value ("Classname", Classname);
}
/** Get Classname.
@return Java Classname */
public String getClassname() 
{
return (String)get_Value("Classname");
}
/** Set Code.
@param Code Code to execute or to validate */
public void setCode (String Code)
{
if (Code != null && Code.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Code = Code.substring(0,1999);
}
set_Value ("Code", Code);
}
/** Get Code.
@return Code to execute or to validate */
public String getCode() 
{
return (String)get_Value("Code");
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,254);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set Comment/Help.
@param Help Comment or Hint */
public void setHelp (String Help)
{
if (Help != null && Help.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Help = Help.substring(0,1999);
}
set_Value ("Help", Help);
}
/** Get Comment/Help.
@return Comment or Hint */
public String getHelp() 
{
return (String)get_Value("Help");
}
/** Set Name.
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 60)
{
log.warning("Length > 60 - truncated");
Name = Name.substring(0,59);
}
set_Value ("Name", Name);
}
/** Get Name.
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Set Sequence.
@param SeqNo Method of ordering records;
 lowest number comes first */
public void setSeqNo (int SeqNo)
{
set_Value ("SeqNo", new Integer(SeqNo));
}
/** Get Sequence.
@return Method of ordering records;
 lowest number comes first */
public int getSeqNo() 
{
Integer ii = (Integer)get_Value("SeqNo");
if (ii == null) return 0;
return ii.intValue();
}

/** TimingType AD_Reference_ID=416 */
public static final int TIMINGTYPE_AD_Reference_ID=416;
/** Before Structure = 1 */
public static final String TIMINGTYPE_BeforeStructure = "1";
/** Before Data = 3 */
public static final String TIMINGTYPE_BeforeData = "3";
/** After Data = 4 */
public static final String TIMINGTYPE_AfterData = "4";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isTimingTypeValid (String test)
{
return test.equals("1") || test.equals("3") || test.equals("4");
}
/** Set Timing Type.
@param TimingType Migration Timing Type */
public void setTimingType (String TimingType)
{
if (TimingType == null) throw new IllegalArgumentException ("TimingType is mandatory");
if (!isTimingTypeValid(TimingType))
throw new IllegalArgumentException ("TimingType Invalid value - " + TimingType + " - Reference_ID=416 - 1 - 3 - 4");
if (TimingType.length() > 1)
{
log.warning("Length > 1 - truncated");
TimingType = TimingType.substring(0,0);
}
set_Value ("TimingType", TimingType);
}
/** Get Timing Type.
@return Migration Timing Type */
public String getTimingType() 
{
return (String)get_Value("TimingType");
}

/** Type AD_Reference_ID=101 */
public static final int TYPE_AD_Reference_ID=101;
/** Java Script = E */
public static final String TYPE_JavaScript = "E";
/** Java Language = J */
public static final String TYPE_JavaLanguage = "J";
/** SQL = S */
public static final String TYPE_SQL = "S";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isTypeValid (String test)
{
return test.equals("E") || test.equals("J") || test.equals("S");
}
/** Set Type.
@param Type Type of Validation (SQL, Java Script, Java Language) */
public void setType (String Type)
{
if (Type == null) throw new IllegalArgumentException ("Type is mandatory");
if (!isTypeValid(Type))
throw new IllegalArgumentException ("Type Invalid value - " + Type + " - Reference_ID=101 - E - J - S");
if (Type.length() > 1)
{
log.warning("Length > 1 - truncated");
Type = Type.substring(0,0);
}
set_Value ("Type", Type);
}
/** Get Type.
@return Type of Validation (SQL, Java Script, Java Language) */
public String getType() 
{
return (String)get_Value("Type");
}
}
