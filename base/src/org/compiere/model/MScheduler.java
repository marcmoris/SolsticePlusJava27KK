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

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.print.*;
import org.compiere.process.*;
import org.compiere.util.*;


/**
 *	Scheduler Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MScheduler.java,v 1.1 2007/07/18 14:39:40 marmor01 Exp $
 */
public class MScheduler extends X_AD_Scheduler
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
	public static MScheduler[] getActive (Properties ctx)
	{
		ArrayList<MScheduler> list = new ArrayList<MScheduler>();
		String sql = "SELECT * FROM AD_Scheduler WHERE IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MScheduler (ctx, rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
		MScheduler[] retValue = new MScheduler[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getActive

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MScheduler.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Scheduler_ID id
	 *	@param trxName transaction
	 */
	public MScheduler (Properties ctx, int AD_Scheduler_ID, String trxName)
	{
		super (ctx, AD_Scheduler_ID, trxName);
		if (AD_Scheduler_ID == 0)
		{
		//	setAD_Process_ID (0);
		//	setName (null);
			setFrequencyType (FREQUENCYTYPE_Day);
			setFrequency (1);
			//
			setKeepLogDays (7);
		//	setSupervisor_ID (0);
		}
	}	//	MScheduler

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MScheduler (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MScheduler

	/** Process to be executed		*/
	private MProcess m_process = null;
	/**	Scheduler Parameter			*/
	private MSchedulerPara[] m_parameter = null;
	/** Scheduler Recipients		*/
	private MSchedulerRecipient[]	m_recipients = null;
	
	/**
	 * 	Get Server ID
	 *	@return id
	 */
	public String getServerID ()
	{
		return "Scheduler" + get_ID();
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
		ArrayList<MSchedulerLog> list = new ArrayList<MSchedulerLog>();
		String sql = "SELECT * "
			+ "FROM AD_SchedulerLog "
			+ "WHERE AD_Scheduler_ID=? " 
			+ "ORDER BY Created DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getAD_Scheduler_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MSchedulerLog (getCtx(), rs, get_TrxName()));
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
		MSchedulerLog[] retValue = new MSchedulerLog[list.size ()];
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
		String sql = "DELETE FROM AD_SchedulerLog "
			+ "WHERE AD_Scheduler_ID=" + getAD_Scheduler_ID() 
			+ " AND (Created+" + getKeepLogDays() + ") < SysDate";
		int no = DB.executeUpdate(sql, get_TrxName());
		return no;
	}	//	deleteLog

	/**
	 * 	Get Process
	 *	@return process
	 */
	public MProcess getProcess()
	{
		if (m_process == null)
			m_process = new MProcess (getCtx(), getAD_Process_ID(), null);
		return m_process;
	}	//	getProcess
	
	/**
	 * 	Get Parameters
	 *	@param reload reload
	 *	@return parameter
	 */
	public MSchedulerPara[] getParameters (boolean reload)
	{
		if (!reload && m_parameter != null)
			return m_parameter;
		ArrayList<MSchedulerPara> list = new ArrayList<MSchedulerPara>();
		String sql = "SELECT * FROM AD_Scheduler_Para WHERE AD_Scheduler_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, getAD_Scheduler_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MSchedulerPara (getCtx(), rs, null));
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
		m_parameter = new MSchedulerPara[list.size()];
		list.toArray(m_parameter);
		return m_parameter;
	}	//	getParameter
	
	/**
	 * 	Get Recipients
	 *	@param reload reload
	 *	@return Recipients
	 */
	public MSchedulerRecipient[] getRecipients (boolean reload)
	{
		if (!reload && m_recipients != null)
			return m_recipients;
		ArrayList<MSchedulerRecipient> list = new ArrayList<MSchedulerRecipient>();
		String sql = "SELECT * FROM AD_SchedulerRecipient WHERE AD_Scheduler_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, getAD_Scheduler_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MSchedulerRecipient (getCtx(), rs, null));
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
		m_recipients = new MSchedulerRecipient[list.size()];
		list.toArray(m_recipients);
		return m_recipients;
	}	//	getRecipients
	
	/**
	 * 	Get Recipient AD_User_IDs
	 *	@return array of user IDs
	 */
	public Integer[] getRecipientAD_User_IDs()
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		MSchedulerRecipient[] recipients = getRecipients(false);
		for (int i = 0; i < recipients.length; i++)
		{
			MSchedulerRecipient recipient = recipients[i];
			if (!recipient.isActive())
				continue;
			if (recipient.getAD_User_ID() != 0)
			{
				Integer ii = new Integer(recipient.getAD_User_ID());
				if (!list.contains(ii))
					list.add(ii);
			}
			if (recipient.getAD_Role_ID() != 0)
			{
				MUserRoles[] urs = MUserRoles.getOfRole(getCtx(), recipient.getAD_Role_ID());
				for (int j = 0; j < urs.length; j++)
				{
					MUserRoles ur = urs[j];
					if (!ur.isActive())
						continue;
					Integer ii = new Integer(ur.getAD_User_ID());
					if (!list.contains(ii))
						list.add(ii);
				}
			}
		}
		//	Add Updater
		if (list.size() == 0)
		{
			Integer ii = new Integer(getUpdatedBy());
			list.add(ii);
		}
		//
		Integer[] recipientIDs = new Integer[list.size()];
		list.toArray(recipientIDs);
		return recipientIDs;
	}	//	getRecipientAD_User_IDs
	
		
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MScheduler[");
		sb.append (get_ID()).append ("-").append (getName())
			.append ("]");
		return sb.toString ();
	}	//	toString
		
	/**
	 * 	Run Scheduler
	 *	@param trx transaction
	 *	@return Summary
	 *	@throws Exception
	 */
	public String execute (Trx trx) throws Exception
	{
		getProcess();
		if (m_process.isReport())
			return runReport(trx);
		else
			return runProcess(trx);
	}	//	execute
	
	/**
	 * 	Run Report
	 *	@param trx trx
	 *	@return summary
	 *	@throws Exception
	 */
	private String runReport (Trx trx) throws Exception
	{
		log.info(m_process.toString());
		if (!m_process.isReport() || m_process.getAD_ReportView_ID() == 0)
			return "Not a Report AD_Process_ID=" + m_process.getAD_Process_ID()
				+ " - " + m_process.getName();
		//	Process
		int AD_Table_ID = 0;
		int Record_ID = 0;
		//
		MPInstance pInstance = new MPInstance(m_process, Record_ID);
		fillParameter(pInstance);
		//
		ProcessInfo pi = new ProcessInfo (m_process.getName(), m_process.getAD_Process_ID(),
			AD_Table_ID, Record_ID);
		pi.setAD_User_ID(getUpdatedBy());
		pi.setAD_Client_ID(getAD_Client_ID());
		pi.setAD_PInstance_ID(pInstance.getAD_PInstance_ID());
		if (!m_process.processIt(pi, trx) && pi.getClassName() != null) 
			return "Process failed: (" + pi.getClassName() + ") " + pi.getSummary();

		//	Report
		ReportEngine re = ReportEngine.get(getCtx(), pi);
		if (re == null)
			return "Cannot create Report AD_Process_ID=" + m_process.getAD_Process_ID()
				+ " - " + m_process.getName();
		File report = re.getPDF();
		//	Notice
		int AD_Message_ID = 884;		//	HARDCODED SchedulerResult
		Integer[] userIDs = getRecipientAD_User_IDs();
		for (int i = 0; i < userIDs.length; i++)
		{
			MNote note = new MNote(getCtx(), 
					AD_Message_ID, userIDs[i].intValue(), trx.getTrxName());
			note.setClientOrg(getAD_Client_ID(), getAD_Org_ID());
			note.setTextMsg(getName());
			note.setDescription(getDescription());
			note.setRecord(AD_Table_ID, Record_ID);
			note.save();
			//	Attachment
			MAttachment attachment = new MAttachment (getCtx(), 
					X_AD_Note.Table_ID, note.getAD_Note_ID(), trx.getTrxName());
			attachment.setClientOrg(getAD_Client_ID(), getAD_Org_ID());
			attachment.addEntry(report);
			attachment.setTextMsg(getName());
			attachment.save();
		}
		//
		return pi.getSummary();
	}	//	runReport
	
	/**
	 * 	Run Process
	 *	@param transaction
	 *	@return summary
	 *	@throws Exception
	 */
	private String runProcess (Trx trx) throws Exception
	{
		log.info(m_process.toString());
		//	Process (see also MWFActivity.performWork
		int AD_Table_ID = 0;
		int Record_ID = 0;
		//
		MPInstance pInstance = new MPInstance(m_process, Record_ID);
		fillParameter(pInstance);
		//
		ProcessInfo pi = new ProcessInfo (m_process.getName(), m_process.getAD_Process_ID(),
			AD_Table_ID, Record_ID);
		pi.setAD_User_ID(getUpdatedBy());
		pi.setAD_Client_ID(getAD_Client_ID());
		pi.setAD_PInstance_ID(pInstance.getAD_PInstance_ID());
		m_process.processIt(pi, trx);
		return pi.getSummary();
	}	//	runProcess
	
	/**
	 * 	Fill Parameter
	 *	@param pInstance process instance
	 */
	private void fillParameter(MPInstance pInstance)
	{
		MSchedulerPara[] sParams = getParameters (false);
		MPInstancePara[] iParams = pInstance.getParameters();
		for (int pi = 0; pi < iParams.length; pi++)
		{
			MPInstancePara iPara = iParams[pi];
			for (int np = 0; np < sParams.length; np++)
			{
				MSchedulerPara sPara = sParams[np];
				if (iPara.getParameterName().equals(sPara.getColumnName()))
				{
					String variable = sPara.getParameterDefault();
					log.fine(sPara.getColumnName() + " = " + variable);
					//	Value - Constant/Variable
					Object value = variable;
					if (variable == null
						|| (variable != null && variable.length() == 0))
						value = null;
					else if (variable.indexOf("@") != -1)	//	we may have a variable
					{
						//	Strip
						int index = variable.indexOf("@");
						String columnName = variable.substring(index+1);
						index = columnName.indexOf("@");
						if (index != -1)
						{
							columnName = columnName.substring(0, index);
							//	try Env
							String env = Env.getContext(getCtx(), columnName);
							if (env.length() == 0)
							{
								log.warning(sPara.getColumnName()
									+ " - not in environment =" + columnName 
									+ "(" + variable + ") - ignored");
								break;
							}
							else
								value = env;
						}
					}	//	@variable@
					
					//	No Value
					if (value == null)
					{
						log.fine(sPara.getColumnName() + " - empty");
						break;
					}
					
					//	Convert to Type
					try
					{
						if (DisplayType.isNumeric(sPara.getDisplayType()) 
							|| DisplayType.isID(sPara.getDisplayType()))
						{
							BigDecimal bd = null;
							if (value instanceof BigDecimal)
								bd = (BigDecimal)value;
							else if (value instanceof Integer)
								bd = new BigDecimal (((Integer)value).intValue());
							else
								bd = new BigDecimal (value.toString());
							iPara.setP_Number(bd);
							log.fine(sPara.getColumnName()
								+ " = " + variable + " (=" + bd + "=)");
						}
						else if (DisplayType.isDate(sPara.getDisplayType()))
						{
							Timestamp ts = null;
							if (value instanceof Timestamp)
								ts = (Timestamp)value;
							else
								ts = Timestamp.valueOf(value.toString());
							iPara.setP_Date(ts);
							log.fine(sPara.getColumnName()
								+ " = " + variable + " (=" + ts + "=)");
						}
						else
						{
							iPara.setP_String(value.toString());
							log.fine(sPara.getColumnName()
								+ " = " + variable
								+ " (=" + value + "=) " + value.getClass().getName());
						}
						if (!iPara.save())
							log.warning("Not Saved - " + sPara.getColumnName());
					}
					catch (Exception e)
					{
						log.warning(sPara.getColumnName()
							+ " = " + variable + " (" + value
							+ ") " + value.getClass().getName()
							+ " - " + e.getLocalizedMessage());
					}
					break;
				}	//	parameter match
			}	//	scheduler parameter loop
		}	//	instance parameter loop
	}	//	fillParameter
	
}	//	MScheduler
