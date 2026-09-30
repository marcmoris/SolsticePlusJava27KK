/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.
 * This program is free software; you can redistribute it and/or modify it
 * under the terms version 2 of the GNU General Public License as published
 * by the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 * You may reach us at: ComPiere, Inc. - http://www.compiere.org/license.html
 * 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA or info@compiere.org 
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;


/**
 *	Table Index Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MTableIndex.java,v 1.1 2007/07/18 14:39:43 marmor01 Exp $
 */
public class MTableIndex extends X_AD_TableIndex
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Active Indexes for Table
	 *	@param table table
	 *	@return array of index
	 */
	public static MTableIndex[] get (MTable table)
	{
		ArrayList<MTableIndex> list = new ArrayList<MTableIndex>();
		String sql = "SELECT * FROM AD_TableIndex WHERE AD_Table_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, table.get_TrxName());
			pstmt.setInt (1, table.getAD_Table_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MTableIndex (table.getCtx(), rs, table.get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		
		MTableIndex[] retValue = new MTableIndex[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	get
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MTableIndex.class);
	
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_TableIndex_ID id
	 *	@param trxName trx
	 */
	public MTableIndex (Properties ctx, int AD_TableIndex_ID, String trxName)
	{
		super (ctx, AD_TableIndex_ID, trxName);
		if (AD_TableIndex_ID == 0)
		{
		//	setAD_Table_ID (0);
		//	setName (null);
			setEntityType (ENTITYTYPE_UserMaintained);
			setIsUnique (false);
		}
	}	//	MTableIndex
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MTableIndex (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
		m_ddl = createDDL();
	}	//	MTableIndex
	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param Name name
	 */
	public MTableIndex (MTable parent, String Name)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg (parent);
		setAD_Table_ID(parent.getAD_Table_ID());
		setEntityType (parent.getEntityType());
		setName(Name);
	}	//	MTableIndex
	
	
	/**	The Lines						*/
	private MIndexColumn[] m_columns = null;
	/** Index Create DDL	*/
	private String		m_ddl = null;

	/**
	 * 	Get Index Columns
	 *	@param reload reload data
	 *	@return array of lines
	 */
	public MIndexColumn[] getColumns(boolean reload)
	{
		if (m_columns != null && !reload)
			return m_columns;
		ArrayList<MIndexColumn> list = new ArrayList<MIndexColumn>();
		String sql = "SELECT * FROM AD_IndexColumn WHERE AD_TableIndex_ID=?"
			+ " ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getAD_TableIndex_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MIndexColumn (getCtx(), rs, get_TrxName ()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		//
		m_columns = new MIndexColumn[list.size ()];
		list.toArray (m_columns);
		return m_columns;
	}	//	getLines

	/**
	 * 	Get Table Name
	 *	@return table name
	 */
	public String getTableName()
	{
		int AD_Table_ID = getAD_Table_ID();
		return MTable.getTableName (getCtx(), AD_Table_ID);
	}	//	getTableName
	
	/**
	 * 	Get SQL DDL
	 *	@return ddl
	 */
	private String createDDL()
	{
		StringBuffer sql = new StringBuffer("CREATE ");
		if (isUnique())
			sql.append (" UNIQUE ");
		sql.append(" INDEX ").append (getName())
			.append(" ON ").append(getTableName())
			.append(" (");
		//
		getColumns(false);
		for (int i = 0; i < m_columns.length; i++)
		{
			MIndexColumn ic = m_columns[i];
			if (i > 0)
				sql.append(",");
			sql.append (ic.getColumnName());
		}
		
		sql.append(")");
		return sql.toString();
	}	//	createDDL
	
	/**
	 * 	Get SQL Index cerate DDL
	 *	@return sql ddl
	 */
	public String getDDL()
	{
		if (m_ddl == null)
			m_ddl = createDDL();
		return m_ddl;
	}	//	getDDL
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MTableIndex[");
		sb.append (get_ID()).append ("-")
			.append (getName ())
			.append (",AD_Table_ID=").append (getAD_Table_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MTableIndex
