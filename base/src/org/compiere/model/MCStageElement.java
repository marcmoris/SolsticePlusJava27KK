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
 * CStage Element
 * 
 * @author Yves Sandfort
 * @version $Id: MCStageElement.java,v 1.1 2007/07/18 14:39:20 marmor01 Exp $
 */
public class MCStageElement extends X_CM_CStage_Element
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/***************************************************************************
     * Standard Constructor
     * 
     * @param ctx  context
     * @param CM_CStage_Element_ID id
     * @param trxName transaction
     */
	public MCStageElement (Properties ctx, int CM_CStage_Element_ID, String trxName)
	{
		super (ctx, CM_CStage_Element_ID, trxName);
		if (CM_CStage_Element_ID == 0)
		{
			setIsValid(false);
		}
	}	// MCStageElement

	/**
     * Load Constructor
     * 
     * @param ctx context
     * @param rs result set
     * @param trxName transaction
     */
	public MCStageElement (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	} 	// MCStageElement
	
	/** Logger								*/
	private static CLogger		s_log = CLogger.getCLogger (MCStageElement.class);
	
	/**
	 * 	get By ElementName
	 *  @param ctx 
	 *	@param CM_CStage_ID
	 *	@param elementName
	 *  @param trxName 
	 *	@return Element by Name
	 */
	public static MCStageElement getByName (Properties ctx, int CM_CStage_ID, String elementName, String trxName)
	{
		String sql = "SELECT * FROM CM_CStage_Element WHERE CM_CStage_ID=? AND Name LIKE ?";
		MCStageElement thisElement = null;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, CM_CStage_ID);
			pstmt.setString (2, elementName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				thisElement = new MCStageElement(ctx, rs, trxName);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "getByName", e);
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
		return thisElement;
	}	//	getByName
	
	/**	m_parent					*/
	private MCStage m_parent	= null;
	
	/**
	 * 	getParent MCStage Object
	 *	@return Container Stage
	 */
	public MCStage getParent() 
	{
		if (m_parent!=null)
			return m_parent;
		m_parent = new MCStage(getCtx(), getCM_CStage_ID (), get_TrxName ());
		return m_parent;
	}
	
	/**
	 * 	After Save.
	 *	@param newRecord insert
	 *	@param success save success
	 *	@return true if saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		if (!getParent().isModified ()) 
		{
			getParent().setIsModified (true);
			getParent().save ();
		}
		return success;
	}	//	afterSave


}	//	MCStageElement
