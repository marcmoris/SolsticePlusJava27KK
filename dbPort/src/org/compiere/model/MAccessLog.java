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
 *	Access Log Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAccessLog.java,v 1.1 2007/07/18 15:00:58 marmor01 Exp $
 */
public class MAccessLog extends X_AD_AccessLog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_AccessLog_ID id
	 *	@param trxName transaction
	 */
	public MAccessLog (Properties ctx, int AD_AccessLog_ID, String trxName)
	{
		super (ctx, AD_AccessLog_ID, trxName);
	}	//	MAccessLog

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MAccessLog (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAccessLog

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param email mail
	 *	@param Remote_Host host
	 *	@param Remote_Addr address
	 *	@param TextMsg text message
	 *	@param trxName transaction
	 */
	public MAccessLog (Properties ctx, String email, 
		String Remote_Host, String Remote_Addr, 
		String TextMsg, 
		String trxName)
	{
		this (new Properties(ctx), 0, trxName);
		setRemote_Addr(Remote_Addr);
		setRemote_Host(Remote_Host);
		setTextMsg(TextMsg);
		setCreatedBy(email);
	}	//	MAccessLog

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param email mail
	 *	@param AD_Table_ID table
	 *	@param AD_Column_ID column
	 *	@param Record_ID record
	 *	@param trxName transaction
	 */
	public MAccessLog (Properties ctx, String email,
		int AD_Table_ID, int AD_Column_ID, int Record_ID, 
		String trxName)
	{
		this (new Properties(ctx), 0, trxName);
		setAD_Table_ID(AD_Table_ID);
		setAD_Column_ID(AD_Column_ID);
		setRecord_ID(Record_ID);
		setCreatedBy(email);
	}	//	MAccessLog
	
	/**
	 * 	Set Created/Updated By
	 *	@param email mail address
	 */
	void setCreatedBy (String email)
	{
		if (email == null || email.length() == 0)
			return;
		int AD_User_ID = MUser.getAD_User_ID (email, getAD_Client_ID());
		set_ValueNoCheck ("CreatedBy", new Integer(AD_User_ID));
		setUpdatedBy (AD_User_ID);
		getCtx().setProperty ("#AD_User_ID", String.valueOf (AD_User_ID));
	}	//	setCreatedBy
	
}	//	MAccessLog
