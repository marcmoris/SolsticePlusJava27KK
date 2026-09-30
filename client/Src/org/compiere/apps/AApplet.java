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
package org.compiere.apps;

import java.awt.*;
import org.compiere.*;
import org.compiere.util.*;


/**
 *	Application Applet (legacy container, maintained for compatibility)
 *	
 *  @author Jorg Janke
 *  @version $Id: AApplet.java,v 1.2 2007/07/18 21:09:18 marmor01 Exp $
 */
public class AApplet extends Panel
{

	/**
	 * 	Compiere Application Applet
	 *	@throws java.awt.HeadlessException
	 */
	public AApplet () throws HeadlessException
	{
		super ();
	}	//	AApplet

	
	/**************************************************************************
	 * 	init
	 */
	public void init ()
	{
		TextArea ta = new TextArea(Compiere.getSummary());
		add (ta);
	}	//	init
	
	public void showStatus (String status)
	{
		// nop
	}

	/**
	 * 	start
	 */
	public void start ()
	{
		showStatus(Compiere.getSummary());
		//
		Splash splash = Splash.getSplash();
		Compiere.startup(true);	//	needs to be here for UI
		AMenu menu = new AMenu();
	}	//	start
	
	/**
	 * 	stop
	 */
	public void stop ()
	{
	}	//	stop
	
	/**
	 * 	destroy
	 */
	public void destroy ()
	{
		Env.exitEnv(0);
	}	//	destroy
	
}	//	AApplet
