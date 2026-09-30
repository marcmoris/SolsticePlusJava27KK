/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;
import java.util.*;
import org.compiere.util.*;

/**
 * 	Table Referenece Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRefTable.java,v 1.1 2007/07/18 14:39:43 marmor01 Exp $
 */
public class MRefTable extends X_AD_Ref_Table
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get MRefTable from Cache
	 *	@param ctx context
	 *	@param AD_Reference_ID id
	 *	@return MRefTable
	 */
	public static MRefTable get(Properties ctx, int AD_Reference_ID)
	{
		Integer key = new Integer (AD_Reference_ID);
		MRefTable retValue = (MRefTable)s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MRefTable (ctx, AD_Reference_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer, MRefTable> s_cache = new CCache<Integer, MRefTable> 
		("AD_Ref_Table", 20);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Reference_ID id
	 *	@param trxName trx
	 */
	public MRefTable (Properties ctx, int AD_Reference_ID, String trxName)
	{
		super (ctx, AD_Reference_ID, trxName);
		if (AD_Reference_ID == 0)
		{
		//	setAD_Table_ID (0);
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setIsValueDisplayed (false);
		}
	}	//	MRefTable

	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MRefTable (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MRefTable

	/**
	 * 	Get Table Name
	 *	@return Table Name
	 */
	public String getTableName()
	{
		int AD_Table_ID = getAD_Table_ID();
		return MTable.getTableName (getCtx(), AD_Table_ID);
	}	//	getTableName
	
	/**
	 * 	Get Key ColumnName
	 *	@return Key Column Name
	 */
	public String getKeyColumnName()
	{
		int AD_Column_ID = getColumn_Key_ID();
		return MColumn.getColumnName (getCtx(), AD_Column_ID);
	}	//	getKeyColumnName

	/**
	 * 	Get Display ColumnName
	 *	@return Display Column Name
	 */
	public String getDisplayColumnName()
	{
		int AD_Column_ID = getColumn_Display_ID();
		return MColumn.getColumnName (getCtx(), AD_Column_ID);
	}	//	getDisplayColumnName

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MRefTable[");
		sb.append (getAD_Reference_ID()).append ("-")
			.append (getAD_Table_ID ()).append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MRefTable
