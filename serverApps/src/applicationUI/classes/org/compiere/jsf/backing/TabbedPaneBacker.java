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

package org.compiere.jsf.backing;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class TabbedPaneBacker implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5886820711075555082L;
	private boolean _tab1Visible = true;
	private boolean _tab2Visible = true;
	private boolean _tab3Visible = true;
	
	public String getMenu() 
	{
		return "mainMenu.jsf";		        
	}
	
    public boolean isTab1Visible()
    {
        return _tab1Visible;
    }

    public void setTab1Visible(boolean tab1Visible)
    {
        _tab1Visible = tab1Visible;
    }

    public boolean isTab2Visible()
    {
        return _tab2Visible;
    }

    public void setTab2Visible(boolean tab2Visible)
    {
        _tab2Visible = tab2Visible;
    }

    public boolean isTab3Visible()
    {
        return _tab3Visible;
    }

    public void setTab3Visible(boolean tab3Visible)
    {
        _tab3Visible = tab3Visible;
    }
    
    public String logout()
    {
    	HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    	if (session != null)
    	{
    		session.invalidate();
    		return "home";
    	}
    	return "";
    }
}




