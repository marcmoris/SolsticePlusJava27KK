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
package org.compiere.process;

import org.compiere.model.*;


/**
 *	Scheduler Run
 *	
 *  @author Jorg Janke
 *  @version $Id: SchedulerRun.java,v 1.1 2007/07/18 14:39:00 marmor01 Exp $
 */
public class SchedulerRun extends SvrProcess
{
	/** Scheduler		*/
	private int	p_AD_Scheduler_ID = 0;

	/**
	 * 	Prepare
	 */
	protected void prepare()
	{
		p_AD_Scheduler_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt()	throws Exception
	{
		log.info ("AD_Scheduler_ID=" + p_AD_Scheduler_ID);
		MScheduler scheduler = new MScheduler(getCtx(), p_AD_Scheduler_ID, get_TrxName());
		return scheduler.execute (get_Trx());
	}	//	doIt

}	//	SchedulerRun
