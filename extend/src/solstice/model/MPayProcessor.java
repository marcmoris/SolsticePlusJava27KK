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
import java.util.logging.*;

import org.compiere.model.CompiereProcessor;
import org.compiere.model.CompiereProcessorLog;
import org.compiere.model.MClient;
import org.compiere.util.*;


/**
 *	Accounting Processor Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MPayProcessor.java,v 1.1 2007/07/18 14:39:55 marmor01 Exp $
 */
public class MPayProcessor extends X_P_PayProcessor
	implements CompiereProcessor
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Active
	 *	@param ctx context
	 *	@return active processors
	 */
	public static MPayProcessor[] getActive (Properties ctx)
	{
		ArrayList<MPayProcessor> list = new ArrayList<MPayProcessor>();
		String sql = "SELECT * FROM P_PayProcessor WHERE IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MPayProcessor (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "getActive", e);
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
		MPayProcessor[] retValue = new MPayProcessor[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getActive

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MPayProcessor.class);

	
	/**
	 * 	Standard Construvtor
	 *	@param ctx context
	 *	@param P_PayProcessor_ID id
	 *	@param trxName transaction
	 */
	public MPayProcessor (Properties ctx, int P_PayProcessor_ID, String trxName)
	{
		super (ctx, P_PayProcessor_ID, trxName);
		if (P_PayProcessor_ID == 0)
		{
		//	setName (null);
		//	setSupervisor_ID (0);
			setFrequencyType (FREQUENCYTYPE_Minute);
			setFrequency (1);
			setKeepLogDays (7);	// 7
		}	
	}	//	MPayProcessor

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MPayProcessor (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPayProcessor

	/**
	 * 	Parent Constructor
	 *	@param client parent
	 *	@param Supervisor_ID admin
	 */
	public MPayProcessor (MClient client, int Supervisor_ID)
	{
		this (client.getCtx(), 1000003, client.get_TrxName());
		setClientOrg(client);
		setName (client.getName() + " - " 
			+ Msg.translate(getCtx(), "P_PayProcessor_ID"));
		setSupervisor_ID (Supervisor_ID);
	}	//	MPayProcessor
	
	
	
	/**
	 * 	Get Server ID
	 *	@return id
	 */
	public String getServerID ()
	{
		return "PayProcessor" + get_ID();
	}	//	getServerID

	/**
	 * 	Get Date Next Run
	 *	@param requery requery
	 *	@return date next run
	 */
	public Timestamp getDateNextRun (boolean requery)
	{
		if (requery)
			load(get_TrxName());
		return getDateNextRun();
	}	//	getDateNextRun

	/**
	 * 	Get Logs
	 *	@return logs
	 */
	public CompiereProcessorLog[] getLogs ()
	{
		ArrayList<MPayProcessorLog> list = new ArrayList<MPayProcessorLog>();
		String sql = "SELECT * "
			+ "FROM P_PayProcessorLog "
			+ "WHERE P_PayProcessor_ID=? " 
			+ "ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getP_PayProcessor_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MPayProcessorLog (getCtx(), rs, get_TrxName()));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		MPayProcessorLog[] retValue = new MPayProcessorLog[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getLogs

	/**
	 * 	Delete old Request Log
	 *	@return number of records
	 */
	public int deleteLog()
	{
		if (getKeepLogDays() < 1)
			return 0;
		String sql = "DELETE FROM P_PayProcessorLog "
			+ "WHERE P_PayProcessor_ID=" + getP_PayProcessor_ID() 
			//jz + " AND (Created+" + getKeepLogDays() + ") < SysDate";
			+ " AND addDays(Created," + getKeepLogDays() + ") < SysDate";
		int no = DB.executeUpdate(sql, get_TrxName());
		return 0;
	}	//	deleteLog

}	//	MPayProcessor
