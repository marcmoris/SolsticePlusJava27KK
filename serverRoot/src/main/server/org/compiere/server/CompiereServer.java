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
import org.compiere.ldap.*;
import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.wf.*;
//import org.solstice.server.PayProcessor;

//import solstice.model.MPayProcessor;

import org.solstice.server.PayProcessorV2;
import solstice.model.MPayProcessorV2;

/**
 *	Compiere Server Base
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereServer.java,v 1.1 2007/07/18 14:44:06 marmor01 Exp $
 */
public abstract class CompiereServer extends Thread
{
	/**
	 * 	Create New Server Thead
	 *	@param model model
	 *	@return server tread or null
	 */
	public static CompiereServer create (CompiereProcessor model)
	{
		if (model instanceof MRequestProcessor)
			return new RequestProcessor ((MRequestProcessor)model);
		if (model instanceof MWorkflowProcessor)
			return new WorkflowProcessor ((MWorkflowProcessor)model);
		if (model instanceof MAcctProcessor)
			return new AcctProcessor ((MAcctProcessor)model);
		
		//+ PROGESTION Solstice
//		if (model instanceof MPayProcessor)
//			return new PayProcessor ((MPayProcessor)model);
		//- PROGESTION Solstice
		
		//+ PROGESTION Solstice
		if (model instanceof MPayProcessorV2)
			return new PayProcessorV2 ((MPayProcessorV2)model);
		//- PROGESTION Solstice
		
		
		if (model instanceof MAlertProcessor)
			return new AlertProcessor ((MAlertProcessor)model);
		if (model instanceof MScheduler)
			return new Scheduler ((MScheduler)model);
		if (model instanceof MLdapProcessor)
			return new LdapProcessor((MLdapProcessor)model);
		//
		throw new IllegalArgumentException("Unknown Processor");
	}	//	 create

	
	/**************************************************************************
	 * 	Server Base Class
	 * 	@param model model
	 *	@param initialNap delay time running in sec
	 */
	protected CompiereServer (CompiereProcessor model, int initialNap)
	{
		super (CompiereServerGroup.get(), null, model.getName(), 0);
		p_model = model;
		m_ctx = new Properties(model.getCtx());
		if (p_system == null)
			p_system = MSystem.get(m_ctx);
		p_client = MClient.get(m_ctx);
		Env.setContext(m_ctx, "#AD_Client_ID", p_client.getAD_Client_ID());
		m_initialNap = initialNap;
	//	log.info(model.getName() + " - " + getThreadGroup());
	}	//	ServerBase

	/**	The Processor Model						*/
	protected					CompiereProcessor 	p_model;
	/** Initial nap is seconds		*/
	private int					m_initialNap = 0;
	
	/**	Miliseconds to sleep - 10 Min default	*/
	private long				m_sleepMS = 600000;
	/** Sleeping					*/
	private volatile boolean	m_sleeping = false;
	/** Server start time					*/
	private long				m_start = 0;
	/** Number of Work executions	*/
	protected int 				p_runCount = 0;
	/** Tine start of work				*/
	protected long				p_startWork = 0;
	/** Number MS of last Run		*/
	private long 				m_runLastMS = 0;
	/** Number of MS total			*/
	private long 				m_runTotalMS = 0;
	/** When to run next			*/
	private long 				m_nextWork = 0;
	
	/**	Logger						*/
	protected CLogger	log = CLogger.getCLogger(getClass());
	/**	Context						*/
	private Properties	m_ctx = null;
	/** System						*/
	protected static MSystem p_system = null;		
	/** Client						*/
	protected MClient	p_client = null;		
	
	/**
	 * 	Get Server Context
	 *	@return context
	 */
	public Properties getCtx()
	{
		return m_ctx;
	}	//	getCtx
	
	/**
	 * @return Returns the sleepMS.
	 */
	public long getSleepMS ()
	{
		return m_sleepMS;
	}	//	getSleepMS
	
	
	/**
	 * 	Sleep for set time
	 *	@return true if not interrupted
	 */
	public boolean sleep()
	{
		if (isInterrupted())
		{
			log.info (getName() + ": interrupted");
			return false;
		}
		if (m_sleepMS < 10)
			return true;
		//
		log.fine(getName() + ": sleeping " + TimeUtil.formatElapsed(m_sleepMS));
		m_sleeping = true;
		try
		{
			sleep (m_sleepMS);
		}
		catch (InterruptedException e)
		{
			log.info (getName() + ": interrupted");
			m_sleeping = false;
			return false;
		}
		m_sleeping = false;
		return true;
	}	//	sleep

	/**
	 * 	Run Now
	 */
	public void runNow()
	{
		log.info(getName());
		p_startWork = System.currentTimeMillis();
		doWork();
		long now = System.currentTimeMillis();
		//	---------------
		
		p_runCount++;
		m_runLastMS = now - p_startWork;
		m_runTotalMS += m_runLastMS;
		//
		p_model.setDateLastRun(new Timestamp(now));
		p_model.save();
		//
		log.fine(getName() + ": " + getStatistics());
	}	//	runNow
	
	/**************************************************************************
	 * 	Run async
	 */
	public void run ()
	{
		int AD_Schedule_ID = p_model.getAD_Schedule_ID();
		MSchedule schedule = null;
		if (AD_Schedule_ID != 0)
		{
			schedule = MSchedule.get (getCtx(), AD_Schedule_ID);
			if (!schedule.isOKtoRunOnIP())
			{
				log.warning (getName() + ": Stopped - IP Restriction " + schedule);
				return;		//	done
			}
		}
		
		try
		{
			log.fine(getName() + ": pre-nap - " + m_initialNap);
			sleep (m_initialNap * 1000);
		}
		catch (InterruptedException e)
		{
			log.log(Level.SEVERE, getName() + ": pre-nap interrupted", e);
			return;
		}
		
		m_start = System.currentTimeMillis();
		while (true)
		{
			if (m_nextWork == 0)
			{
				Timestamp dateNextRun = getDateNextRun(true);
				if (dateNextRun != null)
					m_nextWork = dateNextRun.getTime();
			}
			long now = System.currentTimeMillis();
			if (m_nextWork > now)
			{
				m_sleepMS = m_nextWork - now;
				if (!sleep ())
					break;
			}
			if (isInterrupted())
			{
				log.info (getName() + ": interrupted");
				break;
			}
			
			//	---------------
			p_startWork = System.currentTimeMillis();
			doWork();
			now = System.currentTimeMillis();
			//	---------------
			
			p_runCount++;
			m_runLastMS = now - p_startWork;
			m_runTotalMS += m_runLastMS;
			//
			m_sleepMS = calculateSleep(now);
			m_nextWork = now + m_sleepMS;
			//
			p_model.setDateLastRun(new Timestamp(now));
			p_model.setDateNextRun(new Timestamp(m_nextWork));
			p_model.save();
			//
			log.fine(getName() + ": " + getStatistics());
			if (!sleep())
				break;
		}
		m_start = 0;
	}	//	run

	/**
	 * 	Get Run Statistics
	 *	@return Statistic info
	 */
	public String getStatistics()
	{
		return "Run #" + p_runCount 
			+ " - Last=" + TimeUtil.formatElapsed(m_runLastMS)
			+ " - Total=" + TimeUtil.formatElapsed(m_runTotalMS)
			+ " - Next " + TimeUtil.formatElapsed(m_nextWork - System.currentTimeMillis());
	}	//	getStatistics
	
	/**
	 * 	Do the actual Work
	 */
	protected abstract void doWork();

	/**
	 * 	Get Server Info
	 *	@return info
	 */
	public abstract String getServerInfo();

	/**
	 * 	Get Unique ID
	 *	@return Unique ID
	 */
	public String getServerID()
	{
		return p_model.getServerID();
	}	//	getServerID

	/**
	 * 	Get the date Next run
	 * 	@param requery requery database
	 * 	@return date next run
	 */
	public Timestamp getDateNextRun (boolean requery)
	{
		return p_model.getDateNextRun(requery);
	}	//	getDateNextRun
	
	/**
	 * 	Get the date Last run
	 * 	@return date lext run
	 */
	public Timestamp getDateLastRun ()
	{
		return p_model.getDateLastRun();
	}	//	getDateLastRun

	/**
	 * 	Get Description
	 *	@return Description
	 */
	public String getDescription()
	{
		return p_model.getDescription();
	}	//	getDescription
	
	/**
	 * 	Get Model
	 *	@return Model
	 */
	public CompiereProcessor getModel()
	{
		return p_model;
	}	//	getModel
	
	/**
	 * 	Calculate Sleep ms
	 *	@return miliseconds
	 */
	private long calculateSleep (long now)
	{
		String frequencyType = p_model.getFrequencyType();
		int frequency = p_model.getFrequency();
		if (frequency < 1)
			frequency = 1;
		//
		long typeSec = 600;			//	10 minutes
		if (frequencyType == null)
			typeSec = 300;			//	5 minutes
		else if (X_R_RequestProcessor.FREQUENCYTYPE_Minute.equals(frequencyType))
			typeSec = 60;
		else if (X_R_RequestProcessor.FREQUENCYTYPE_Hour.equals(frequencyType))
			typeSec = 3600;
		else if (X_R_RequestProcessor.FREQUENCYTYPE_Day.equals(frequencyType))
			typeSec = 86400;
		//
		long sleep = typeSec * 1000 * frequency;		//	ms
		if (p_model.getAD_Schedule_ID() == 0)
			return sleep;
		
		//	Calculate Schedule
		MSchedule schedule = MSchedule.get(getCtx(), p_model.getAD_Schedule_ID());
		long next = schedule.getNextRunMS (now);
		long delta = next - now;
		if (delta < 0)
		{
			log.warning ("Negative Delta=" + delta + " - set to " + sleep);
			delta = sleep;
		}
		return delta;
	}	//	calculateSleep

	/**
	 * 	Is Sleeping
	 *	@return sleeping
	 */
	public boolean isSleeping()
	{
		return m_sleeping;
	}	//	isSleeping
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer (getName())
			.append (",Prio=").append(getPriority())
			.append (",").append (getThreadGroup())
			.append (",Alive=").append(isAlive())
			.append (",Sleeping=").append(m_sleeping)
			.append (",Last=").append(getDateLastRun());
		if (m_sleeping)
			sb.append (",Next=").append(getDateNextRun(false));
		return sb.toString ();
	}	//	toString

	/**
	 * 	Get Seconds Alive
	 *	@return seconds alive
	 */
	public int getSecondsAlive()
	{
		if (m_start == 0)
			return 0;
		long now = System.currentTimeMillis();
		long ms = (now-m_start) / 1000;
		return (int)ms;
	}	//	getSecondsAlive
	
	/**
	 * 	Get Start Time
	 *	@return start time
	 */
	public Timestamp getStartTime()
	{
		if (m_start == 0)
			return null;
		return new Timestamp (m_start);
	}	//	getStartTime
	
	/**
	 * 	Get Processor Logs
	 *	@return logs
	 */
	public CompiereProcessorLog[] getLogs()
	{
		return p_model.getLogs();
	}	//	getLogs

}	//	CompiereServer
