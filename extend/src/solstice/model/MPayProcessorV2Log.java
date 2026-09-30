/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/
package solstice.model;


import java.sql.*;
import java.util.*;

import org.compiere.model.CompiereProcessorLog;


/**
 *	Accounting Processor Log
 *	
 *  @author Jorg Janke
 *  @version $Id: MPayProcessorV2Log.java,v 1.1 2007/07/18 14:39:34 marmor01 Exp $
 */
public class MPayProcessorV2Log extends X_P_PayProcessorV2Log
	implements CompiereProcessorLog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param P_PayProcessorLog_ID id
	 *	@param trxName transaction
	 */
	public MPayProcessorV2Log (Properties ctx, int P_PayProcessorV2Log_ID, String trxName)
	{
		super (ctx, P_PayProcessorV2Log_ID, trxName);
	}	//	MPayProcessorV2Log

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MPayProcessorV2Log (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPayProcessorV2Log

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param summary summary
	 */
	public MPayProcessorV2Log (MPayProcessorV2 parent, String summary)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setP_PayProcessorV2_ID(parent.getP_PayProcessorV2_ID());
		setSummary(summary);
	}	//	MPayProcessorV2Log


}	//	MPayProcessorV2Log
