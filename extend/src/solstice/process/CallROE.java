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
package solstice.process;

import org.compiere.util.Env;
import org.compiere.process.*;

import solstice.utils.PgiUtil;

/**
 * @author marmor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CallROE extends SvrProcess {

	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
	}	//	prepare

	
	/**
	 * 	Read each time sheet 
	 *	@return 
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
        String srvUrl = PgiUtil.getSolsticeParameter(Env.getCtx(), "WebAppURL");
	    Env.startBrowser(srvUrl + "login.jsp?user=" + Env.getContext(Env.getCtx(), "#AD_User_Name") + "&redirection=%2Fsolstice%2FreleveEmploi%2Findex.jsp");

	    return null;
	}	
}
