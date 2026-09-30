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

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;


/**
 *  Compiere ComboBox UI.
 *  The ComboBox is opaque - with opaque arrow button and textfield background
 *
 *  @author     Jorg Janke
 *  @version    $Id: CompiereComboBoxUI.java,v 1.1 2007/07/18 14:40:53 marmor01 Exp $
 */
public class CompiereComboBoxUI extends MetalComboBoxUI
{
	/**
	 *  Create UI
	 *  @param c
	 *  @return new instance of CompiereComboBoxUI
	 */
	public static ComponentUI createUI(JComponent c)
	{
		return new CompiereComboBoxUI();
	}   //  CreateUI

	/*************************************************************************/

	static int s_no = 0;
	/**
	 *  Install UI - Set ComboBox opaque.
	 *  Bug in Metal: arrowButton gets Mouse Events, so add the JComboBox
	 *  MouseListeners to the arrowButton
	 *  @see org.compiere.swing.CComboBox#addMouseListener(MouseListener)
	 *  @param c componrnt
	 */
	public void installUI (JComponent c)
	{
		MouseListener[] ml = c.getMouseListeners();
		super.installUI(c);
		c.setOpaque(false);
		//
		for (int i = 0; i < ml.length; i++)
		{
		//	System.out.println("adding " + c.getClass().getName());
			arrowButton.addMouseListener(ml[i]);
		}

	}   //  installUI

	/*************************************************************************/

	/**
	 *  Create opaque button
	 *  @return opaque button
	 */
	protected JButton createArrowButton()
	{
		JButton button = super.createArrowButton();
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		return button;
	}   //  createArrowButton

	public JButton getArrowButton()
	{
		return arrowButton;
	}

	/**
	 *  Set Icon  of arrow button
	 *  @param defaultIcon
	 */
	public void setIcon(Icon defaultIcon)
	{
		((MetalComboBoxButton)arrowButton).setComboIcon(defaultIcon);
	}   //  setIcon

	/*************************************************************************/

	/**
	 *  Create Popup
	 *  @return CompiereComboPopup
	 */
	protected ComboPopup createPopup()
	{
		CompiereComboPopup newPopup = new CompiereComboPopup( comboBox );
		newPopup.getAccessibleContext().setAccessibleParent(comboBox);
		return newPopup;
	}   //  createPopup
	
	/* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicComboBoxUI#getPreferredSize(javax.swing.JComponent)
	 */
	public Dimension getPreferredSize(JComponent c)
	{
		Dimension size = super.getPreferredSize(c);
		
		if (c instanceof JComboBox)
		{
			JComboBox comboBox = (JComboBox)c;
			JComponent edComp = (JComponent)comboBox.getEditor().getEditorComponent();
			Dimension edSize = edComp.getPreferredSize();
			Insets insets = edComp.getInsets();
			size.height = edSize.height - insets.bottom - insets.top;
		}
		
		return size;
	}

}   //  CompiereComboBoxUI
