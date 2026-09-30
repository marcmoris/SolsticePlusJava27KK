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
import java.util.logging.*;
import org.compiere.util.*;

/**
 * 	Info Window Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MInfoWindow.java,v 1.1 2007/07/18 14:39:55 marmor01 Exp $
 */
public class MInfoWindow extends X_AD_InfoWindow
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_InfoWindow_ID id
	 *	@param trxName transaction
	 */
	public MInfoWindow (Properties ctx, int AD_InfoWindow_ID, String trxName)
	{
		super (ctx, AD_InfoWindow_ID, trxName);
	}	//	MInfoWindow

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MInfoWindow (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MInfoWindow
	
	
	/**	The Lines				*/
	private MInfoColumn[] 	m_lines = null;
	/** Table Name				*/
	private String			m_tableName = null;

	/**
	 * 	Get Lines
	 *	@param reload reload data
	 *	@return array of lines
	 */
	public MInfoColumn[] getLines(boolean reload)
	{
		if (m_lines != null && !reload)
			return m_lines;
		String sql = "SELECT * FROM AD_InfoColumn WHERE AD_InfoWindow_ID=? ORDER BY SeqNo";
		ArrayList<MInfoColumn> list = new ArrayList<MInfoColumn>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName ());
			pstmt.setInt (1, getAD_InfoWindow_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MInfoColumn (getCtx(), rs, get_TrxName ()));
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
		m_lines = new MInfoColumn[list.size ()];
		list.toArray (m_lines);
		return m_lines;
	}	//	getLines

	/**
	 * 	Get Table Name
	 *	@return table name
	 */
	protected String getTableName()
	{
		if (m_tableName == null)
		{
			MTable table = MTable.get (getCtx(), getAD_Table_ID());
			m_tableName = table.get_TableName();
		}
		return m_tableName;
	}	//	getTableName
	
	/**
	 * 	Get SQL for Role
	 *	@param role role
	 *	@return statement
	 */
	public String getSQL (MRole role)
	{
		if (m_lines == null)
			getLines(true);
		
		StringBuffer sql = new StringBuffer("SELECT ");
		for (int i = 0; i < m_lines.length; i++)
		{
			MInfoColumn col = m_lines[i];
			if (i > 0)
				sql.append(",");
			sql.append(col.getSelectClause());
		}
		sql.append(" FROM ").append(getFromClause());

		//	Access
		if (role == null)
			role = MRole.getDefault (getCtx(), false);
		String finalSQL = role.addAccessSQL (sql.toString(), 
			getTableName(), MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		log.finer(finalSQL);
		return finalSQL;
	}	//	getSQL
	
}	//	MInfoWindow
