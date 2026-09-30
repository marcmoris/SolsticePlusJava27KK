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

/**
 * 	Info Window Column Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MInfoColumn.java,v 1.1 2007/07/18 14:39:49 marmor01 Exp $
 */
public class MInfoColumn extends X_AD_InfoColumn
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Stanfard Constructor
	 *	@param ctx context
	 *	@param AD_InfoColumn_ID id
	 *	@param trxName transaction
	 */
	public MInfoColumn (Properties ctx, int AD_InfoColumn_ID, String trxName)
	{
		super (ctx, AD_InfoColumn_ID, trxName);
	}	//	MInfoColumn

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MInfoColumn (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MInfoColumn
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave(boolean newRecord)
	{
		//	Sync Terminology
		if ((newRecord || is_ValueChanged ("AD_Element_ID")) 
			&& getAD_Element_ID() != 0 
			&& isCentrallyMaintained())
		{
			M_Element element = new M_Element (getCtx(), getAD_Element_ID (), null);
			setName (element.getName ());
			setDescription (element.getDescription ());
			setHelp (element.getHelp());
		}
		return true;
	}	//	beforeSave

	
}	//	MInfoColumn
