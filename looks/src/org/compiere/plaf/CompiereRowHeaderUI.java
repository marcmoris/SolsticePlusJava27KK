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
package org.compiere.plaf;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/**
 *  Table Header UI
 *  3D effect
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereRowHeaderUI.java,v 1.1 2007/07/18 14:40:54 marmor01 Exp $
 */
public class CompiereRowHeaderUI extends BasicListUI
{
	/**
	 *  Static Create UI
	 *  @param c Component
	 *  @return Compiere TableHeader UI
	 */
	public static ComponentUI createUI(JComponent c)
	{
		return new CompiereRowHeaderUI();
	}   //  createUI


	/**
	 *  Install UI - set Opaque
	 *  @param c
	 */
	public void installUI (JComponent c)
	{
		super.installUI(c);
        c.setOpaque(false);
        
		for (MouseMotionListener listener : c.getMouseMotionListeners()) 
			c.removeMouseMotionListener(listener);
	}   //  installUI

}   //  CompiereTableHeader
