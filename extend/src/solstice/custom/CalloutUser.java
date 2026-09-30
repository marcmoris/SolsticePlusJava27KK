/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2001 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package solstice.custom;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

import org.compiere.model.*;

/**
 *	User Callout Example.
 *
 *  @author Jorg Janke
 *  @version  $Id: CalloutUser.java,v 1.1 2007/07/18 14:43:57 marmor01 Exp $
 */
public class CalloutUser extends CalloutEngine
{
	/**
	 *	JustAnExample.
	 *	The string in the Callout field is: 
	 *  <code>com.compiere.custom.CalloutEngine.justAnExample</code> 
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @param oldValue The old value
	 *	@return error message or "" if OK
	 */
	public String justAnExample (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		log.info("JustAnExample");
		return "";
	}	//	JustAnExample

	
	
	
	
	
	
	
	
	
	
	

	/**************************************************************************
	 *	Frie Value - convert to standardized Name
	 *
	 * @param value Name
	 * @return Name
	 */
	public String Frie_Name (String value)
	{
		if (value == null || value.length() == 0)
			return "";
		//
		String retValue = value;
		String SQL = "SELECT FRIE_Name(?) FROM DUAL";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			pstmt.setString(1, value);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getString(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log (Level.SEVERE, "CalloutUser.Frie_Name", e);
		}
		return retValue;
	}	//	Frie_Name


	/**
	 *	Frie Value - convert Name to Value
	 *
	 *  @param value Name
	 *  @return Value of Name
	 */
	public String Frie_Value (String value)
	{
		if (value == null || value.length() == 0)
			return "";
		//
		String retValue = value;
		String SQL = "SELECT FRIE_Value(FRIE_Name(?)) FROM DUAL";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			pstmt.setString(1, value);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getString(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log (Level.SEVERE, "CalloutUser.Frie_Value", e);
		}
		return retValue;
	}	//	Frie_Value


	/**
	 *	Frie Status - convert to Status.
	 *
	 *  @param value value
	 *  @return Status
	 */
	public String Frie_Status (String value)
	{
		String retValue = "N";		//	default
		if (value != null && value.equals("A"))		//	Auslaufartikel
			retValue = "Y";			//
		return retValue;
	}	//	Frie_Status

}	//	CalloutUser
