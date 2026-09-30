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
package org.compiere.grid.ed;

import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.print.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Printer Editor
 *	
 *  @author Jorg Janke
 *  @version $Id: VPrinter.java,v 1.2 2007/07/18 21:09:13 marmor01 Exp $
 */
public class VPrinter extends CComboBox
	implements VEditor
{

	/**
	 *	Detail Constructor
	 *  @param columnName column name
	 *  @param mandatory mandatory
	 *  @param isReadOnly read only
	 *  @param isUpdateable updateable
	 *  @param fieldLength field length
	 */
	public VPrinter (String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
		int fieldLength)
	{
		super();
		setName(columnName);
		m_columnName = columnName;
		m_fieldLength = fieldLength;
		//
		setMandatory(mandatory);
		if (mandatory)
			setModel(new DefaultComboBoxModel(CPrinter.getPrinterNames()));
		else
		{
			String[] printer = CPrinter.getPrinterNames();
			Vector<String> v = new Vector<String>();
			v.add("");
			for (int i = 0; i < printer.length; i++)
				v.add (printer[i]);
			setModel(new DefaultComboBoxModel(v));
		}

		//	Editable
		if (isReadOnly || !isUpdateable)
		{
			setEditable(false);
			setBackground(CompierePLAF.getFieldBackground_Inactive());
		}
		this.addActionListener(this);
		setForeground(CompierePLAF.getTextColor_Normal());
		setBackground(CompierePLAF.getFieldBackground_Normal());
	}	//	VString

	/** Grid Field				*/
	private GridField      		m_mField = null;
	/** Column Name				*/
	private String				m_columnName;
	/** Field Length			*/
	private int					m_fieldLength;
	/**	Setting Value			*/
	private volatile boolean	m_setting = false;
	
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (VPrinter.class);

	/**
	 *	Set Editor to value
	 *  @param value value
	 */
	public void setValue(Object value)
	{
		log.config("" + value);
		m_setting = true;
		super.setValue(value);
		m_setting = false;
	}	//	setValue

	/**
	 * 	Action Performed
	 *	@param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (m_setting)
			return;
		super.actionPerformed (e);
		Object value = getValue();
		log.config("" + value);
		try
		{
			fireVetoableChange (m_columnName, "_", value);
		}
		catch (PropertyVetoException e1)
		{
		}
	}	//	actionPerformed

	
	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		m_mField = null;
	}	//	dispose

	/**
	 *  Set Field/WindowNo 
	 *  @param mField field
	 */
	public void setField (GridField mField)
	{
		m_mField = mField;
	}   //  setField

	/**
	 *  Get Field
	 *  @return gridField
	 */
	public GridField getField()
	{
		return m_mField;
	}   //  getField

	/**
	 * 	Property Change
	 *	@param evt event
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(GridField.PROPERTY))
			setValue(evt.getNewValue());
	}	//	propertyChange
	
}	//	VPrinter
