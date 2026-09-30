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
package org.compiere.web;

import java.io.*;
import java.util.logging.*;
import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.http.*;
import org.compiere.model.*;
import org.compiere.util.*;

import solstice.utils.PgiUtil;


/**
 * 	Compiere Monitor Filter.
 * 	Application Server independent check of username/password
 * 	
 *  @author Jorg Janke
 *  @version $Id: CompiereMonitorFilter.java,v 1.1 2007/07/18 14:44:07 marmor01 Exp $
 */
public class CompiereMonitorFilter implements Filter
{
	/**
	 * 	CompiereMonitorFilter
	 */
	public CompiereMonitorFilter ()
	{
		super ();
		m_authorization = new Long (System.currentTimeMillis());
	}	//	CompiereMonitorFilter

	/**	Logger			*/
	protected CLogger	log = CLogger.getCLogger(getClass());

	/**	Authorization ID				*/
	private static final String		AUTHORIZATION = "CompiereAuthorization";
	/** Authorization Marker			*/
	private Long					m_authorization = null;
	
	/**
	 * 	Init
	 *	@param config configuration
	 *	@throws ServletException
	 */
	public void init (FilterConfig config)
		throws ServletException
	{
		log.info ("");
	}	//	Init

	/**
	 * 	Filter
	 *	@param request request
	 *	@param response response
	 *	@param chain chain
	 *	@throws IOException
	 *	@throws ServletException
	 */
	public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException
	{
//+ Progestion Solstice
		boolean error = false;
		String webAppUrl = PgiUtil.getSolsticeParameter(Env.getCtx(), "WEBAPPURL");

		String errorPage = webAppUrl + "error.html";
		
//	String errorPage = "/error.html";
//- Progestion Solstice
		boolean pass = false;
		try
		{
			if (!(request instanceof HttpServletRequest && response instanceof HttpServletResponse))
			{
				request.getRequestDispatcher(errorPage).forward(request, response);
				return;
			}
			HttpServletRequest req = (HttpServletRequest)request;
			HttpServletResponse resp = (HttpServletResponse)response;
			//	Previously checked
			HttpSession session = req.getSession(true);
			Long compare = (Long)session.getAttribute(AUTHORIZATION);
			if (compare != null && compare.compareTo(m_authorization) == 0)
			{
				pass = true;
			}
			else if (checkAuthorization (req.getHeader("Authorization")))
			{
				session.setAttribute(AUTHORIZATION, m_authorization);
				pass = true;
			}
			//	--------------------------------------------
			if (pass)
			{
				chain.doFilter(request, response);
			}
			else
			{
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				resp.setHeader("WWW-Authenticate", "BASIC realm=\"Compiere Server\"");
			}
			return;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "filter", e);
		}
		request.getRequestDispatcher(errorPage).forward(request, response);
	}	//	doFilter

	/**
	 * 	Check Authorization
	 *	@param authorization authorization
	 *	@return true if authenticated
	 */
	private boolean checkAuthorization (String authorization)
	{
		if (authorization == null)
			return false;
		try
		{
			String userInfo = authorization.substring(6).trim();
			byte[] decoded = java.util.Base64.getDecoder().decode(userInfo);
			String namePassword = new String (decoded);
		//	log.fine("checkAuthorization - Name:Password=" + namePassword);
			int index = namePassword.indexOf(":");
			String name = namePassword.substring(0, index);
			String password = namePassword.substring(index+1);
			MUser user = MUser.get(Env.getCtx(), name, password);
			if (user == null)
			{
				log.warning ("User not found: '" + name + "/" + password + "'");
				return false;
			}
			if (!user.isAdministrator())
			{
				log.warning ("Not a Sys Admin = " + name);
				return false;
			}
			log.info ("Name=" + name);
			return true;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "check", e);
		}
		return false;
	}	//	check
	
	/**
	 * 	Destroy
	 */
	public void destroy ()
	{
		log.info ("");
	}	//	destroy

}	//	CompiereMonitorFilter
