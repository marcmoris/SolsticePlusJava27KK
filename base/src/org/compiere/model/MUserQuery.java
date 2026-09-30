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
 *	User Query Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MUserQuery.java,v 1.1 2007/07/18 14:39:24 marmor01 Exp $
 */
public class MUserQuery extends X_AD_UserQuery
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get all active queries of client for Tab
	 *	@param ctx context
	 *	@param AD_Tab_ID tab
	 *	@return array of queries
	 */
	public static MUserQuery[] get (Properties ctx, int AD_Tab_ID)
	{
		String sql = "SELECT * FROM AD_UserQuery "
			+ "WHERE AD_Client_ID=? AND AD_Tab_ID=? AND IsActive='Y' "
			+ "ORDER BY Name";
		int AD_Client_ID = Env.getAD_Client_ID (ctx);
		ArrayList<MUserQuery> list = new ArrayList<MUserQuery>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, AD_Client_ID);
			pstmt.setInt (2, AD_Tab_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next ())
				list.add(new MUserQuery (ctx, rs, null));
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
		MUserQuery[] retValue = new MUserQuery[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	get
	
	/**
	 * 	Get Specific Tab Query
	 *	@param ctx context
	 *	@param AD_Tab_ID tab
	 *	@param name name
	 *	@return query or null
	 */
	public static MUserQuery get (Properties ctx, int AD_Tab_ID, String name)
	{
		String sql = "SELECT * FROM AD_UserQuery "
			+ "WHERE AD_Client_ID=? AND AD_Tab_ID=? AND UPPER(Name) LIKE ? AND IsActive='Y' "
			+ "ORDER BY Name";
		int AD_Client_ID = Env.getAD_Client_ID (ctx);
		if (name == null)
			name = "%";
		MUserQuery retValue = null;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, AD_Client_ID);
			pstmt.setInt (2, AD_Tab_ID);
			pstmt.setString (3, name.toUpperCase());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MUserQuery (ctx, rs, null);
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
		return retValue;
	}	//	get

	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MUserQuery.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_UserQuery_ID id
	 *	@param trxName trx
	 */
	public MUserQuery(Properties ctx, int AD_UserQuery_ID, String trxName)
	{
		super (ctx, AD_UserQuery_ID, trxName);
	}	//	MUserQuery

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MUserQuery(Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MUserQuery

	//+ PROGESTION Solstice
	
	/**	The Lines						*/
	private MUserQueryLine[] m_lines = null;

	/**
	 * 	Get Lines
	 *	@param reload reload data
	 *	@return array of lines
	 */
	public MUserQueryLine[] getLines(boolean reload)
	{
		if (m_lines != null && !reload)
			return m_lines;
		ArrayList<MUserQueryLine> list = new ArrayList<MUserQueryLine>();
		String sql = "SELECT * FROM AD_UserQueryLine WHERE AD_UserQuery_ID=? ORDER BY SeqNo";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getAD_UserQuery_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MUserQueryLine(getCtx(), rs, get_TrxName()));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		//
		m_lines = new MUserQueryLine[list.size()];
		list.toArray(m_lines);
		return m_lines;
	}	//	getLines
	
	/**
	 * 	Delete all Lines
	 *	@return true if deleted
	 */
	public boolean deleteLines()
	{
		String sql = "DELETE FROM AD_UserQueryLine WHERE AD_UserQuery_ID=?";
		int no = DB.executeUpdate(sql, getAD_UserQuery_ID(), null);	//	out of trx
		log.info("#" + no);
		m_lines = null;
		return no >= 0;
	}	//	deleteLines

	/**
	 * 	Get all active queries of client for Tab
	 *	@param ctx context
	 *	@param AD_Tab_ID tab
	 *	@return array of queries
	 */
	public static MUserQuery[] get (Properties ctx, int AD_Tab_ID, int AD_Table_ID)
	{
		String sql = "SELECT * FROM AD_UserQuery "
			+ "WHERE AD_Client_ID=? AND IsActive='Y'"
			+ " AND (AD_Tab_ID=? OR AD_Table_ID=?) "
			+ "ORDER BY Name";
		int AD_Client_ID = Env.getAD_Client_ID( ctx );
		ArrayList<MUserQuery> list = new ArrayList<MUserQuery>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, AD_Client_ID);
			pstmt.setInt (2, AD_Tab_ID);
			pstmt.setInt (3, AD_Table_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next ())
				list.add(new MUserQuery (ctx, rs, null));
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
		MUserQuery[] retValue = new MUserQuery[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	get

	public String toString()
	{
		StringBuffer sb = new StringBuffer ( this.getName());
		return sb.toString();
	}

	//- Progestion Solstice
}	//	MUserQuery
