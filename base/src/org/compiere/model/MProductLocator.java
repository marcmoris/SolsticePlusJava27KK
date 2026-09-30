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
 *	Product Locator Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MProductLocator.java,v 1.1 2007/07/18 14:39:44 marmor01 Exp $
 */
public class MProductLocator extends X_M_ProductLocator
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get array of active Locators for Product and warehouse ordered by priority
	 *	@param product product
	 *	@param M_Warehouse_ID wh
	 *	@return product locators
	 */
	public static MLocator[] getLocators (MProduct product, int M_Warehouse_ID)
	{
		ArrayList<MLocator> list = new ArrayList<MLocator>();
		String sql = "SELECT * FROM M_Locator l "
			+ "WHERE l.IsActive='Y'"
			+ " AND (M_Locator_ID IN (SELECT M_Locator_ID FROM M_Product WHERE M_Product_ID=?)"
			+ " OR M_Locator_ID IN (SELECT M_Locator_ID FROM M_ProductLocator WHERE M_Product_ID=? AND IsActive='Y'))"
			+ " AND M_Warehouse_ID=? "
			+ "ORDER BY PriorityNo DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, product.get_TrxName());
			pstmt.setInt (1, product.getM_Product_ID());
			pstmt.setInt (2, product.getM_Product_ID());
			pstmt.setInt (3, M_Warehouse_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MLocator (product.getCtx(), rs, product.get_TrxName()));
			}
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
		MLocator[] retValue = new MLocator[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getLocator
	
	/**
	 * 	Get First M_Locator_ID for product and Warehouse ordered by priority of Locator
	 *	@param product product
	 *	@param M_Warehouse_ID wh
	 *	@return locator or 0 if none
	 */
	public static int getFirstM_Locator_ID (MProduct product, int M_Warehouse_ID)
	{
		if (product == null || M_Warehouse_ID == 0)
			return 0;
		//
		int M_Locator_ID = 0;
		String sql = "SELECT M_Locator_ID FROM M_Locator l "
			+ "WHERE l.IsActive='Y'"
			+ " AND (M_Locator_ID IN (SELECT M_Locator_ID FROM M_Product WHERE M_Product_ID=?)"
			+ " OR M_Locator_ID IN (SELECT M_Locator_ID FROM M_ProductLocator WHERE M_Product_ID=? AND IsActive='Y'))"
			+ " AND M_Warehouse_ID=? "
			+ "ORDER BY PriorityNo DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, product.get_TrxName());
			pstmt.setInt (1, product.getM_Product_ID());
			pstmt.setInt (2, product.getM_Product_ID());
			pstmt.setInt (3, M_Warehouse_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				M_Locator_ID = rs.getInt(1);
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
		
		return M_Locator_ID;
	}	//	getFirstM_Locator_ID
	
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MProductLocator.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_ProductLocator_ID id
	 *	@param trxName trx
	 */
	public MProductLocator(Properties ctx, int M_ProductLocator_ID,
		String trxName)
	{
		super (ctx, M_ProductLocator_ID, trxName);
	}	//	MProductLocator

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MProductLocator(Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MProductLocator
	
}	//	MProductLocator
