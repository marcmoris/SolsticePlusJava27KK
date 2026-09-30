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


/**
 *	Table Index Column Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MIndexColumn.java,v 1.1 2007/07/18 14:39:54 marmor01 Exp $
 */
public class MIndexColumn extends X_AD_IndexColumn
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_IndexColumn_ID id
	 *	@param trxName transaction
	 */
	public MIndexColumn(Properties ctx, int AD_IndexColumn_ID, String trxName)
	{
		super (ctx, AD_IndexColumn_ID, trxName);
	}	//	MIndexColumn

	/**
	 * 	Load Contsructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MIndexColumn(Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MIndexColumn

	/**
	 * 	Parent Contsructor
	 *	@param parent parent
	 *	@param column column
	 *	@param seqNo seq no
	 */
	public MIndexColumn(MTableIndex parent, MColumn column, int seqNo)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg (parent);
		setAD_TableIndex_ID (parent.getAD_TableIndex_ID());
		setAD_Column_ID (column.getAD_Column_ID());
		setSeqNo(seqNo);
	}	//	MIndexColumn

	/**
	 * 	Get Column Name
	 *	@return column name
	 */
	public String getColumnName()
	{
		int AD_Column_ID = getAD_Column_ID();
		return MColumn.getColumnName (getCtx(), AD_Column_ID);
	}	//	getColumnName
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MIndexColumn[");
		sb.append (get_ID()).append ("-").append (getAD_Column_ID()).append ("]");
		return sb.toString ();
	}	//	toString
	
}	//	MIndexColumn
