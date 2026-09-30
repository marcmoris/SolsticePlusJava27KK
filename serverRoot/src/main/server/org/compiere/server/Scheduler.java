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
package org.compiere.server;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;


/**
 *	Scheduler
 *	
 *  @author Jorg Janke
 *  @version $Id: Scheduler.java,v 1.1 2007/07/18 14:44:06 marmor01 Exp $
 */
public class Scheduler extends CompiereServer
{
	/**
	 * 	Scheduler
	 *	@param model model
	 */
	public Scheduler (MScheduler model)
	{
		super (model, 240);		//	nap
		m_model = model;
	//	m_client = MClient.get(model.getCtx(), model.getAD_Client_ID());
	}	//	Scheduler

	/**	The Concrete Model			*/
	private MScheduler			m_model = null;
	/**	Last Summary				*/
	private StringBuffer 		m_summary = new StringBuffer();
	/** Transaction					*/
	private Trx					m_trx = null;

	/**
	 * 	Work
	 */
	protected void doWork ()
	{
		m_summary = new StringBuffer(m_model.toString())
			.append(" - ");
		MProcess process = m_model.getProcess();
		//
		try
		{
			//	Explicitly set Environment
			Properties ctx = m_model.getCtx(); 
			Env.setContext (ctx, "#AD_Client_ID", m_model.getAD_Client_ID());
			Env.setContext (ctx, "AD_Client_ID", m_model.getAD_Client_ID());
			Env.setContext (ctx, "#AD_Org_ID", m_model.getAD_Org_ID());
			Env.setContext (ctx, "AD_Org_ID", m_model.getAD_Org_ID());
			Env.setContext (ctx, "#AD_User_ID", m_model.getUpdatedBy());
			Env.setContext (ctx, "AD_User_ID", m_model.getUpdatedBy());
			Env.setContext (ctx, "#SalesRep_ID", m_model.getUpdatedBy());
			//
			m_trx = Trx.get(Trx.createTrxName("Scheduler"), true);
			m_summary.append(m_model.execute(m_trx));
			m_trx.commit();
		}
		catch (Exception e)
		{
			if (m_trx != null)
				m_trx.rollback();
			log.log(Level.WARNING, process.toString(), e);
			m_summary.append(e.toString());
		}
		if (m_trx != null)
			m_trx.close();
		//
		int no = m_model.deleteLog();
		m_summary.append("Logs deleted=").append(no);
		//
		MSchedulerLog pLog = new MSchedulerLog(m_model, m_summary.toString());
		pLog.setReference("#" + String.valueOf(p_runCount) 
			+ " - " + TimeUtil.formatElapsed(new Timestamp(p_startWork)));
		pLog.save();
	}	//	doWork


	
	/**
	 * 	Get Server Info
	 *	@return info
	 */
	public String getServerInfo()
	{
		return "#" + p_runCount + " - Last=" + m_summary.toString();
	}	//	getServerInfo

}	//	Scheduler
