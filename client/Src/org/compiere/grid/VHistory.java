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
package org.compiere.grid;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *	Queries how many days back history is displayed as current
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: VHistory.java,v 1.2 2007/07/18 21:09:26 marmor01 Exp $
 */
public class VHistory extends CDialog
	implements ActionListener
{
	/**
	 *	Constructor
	 *  @param parent parent frame
	 *  @param buttonLocation lower left corner of the button
	 *  @param AD_Tab_ID tab
	 */
	public VHistory (Frame parent, Point buttonLocation, int AD_Tab_ID)
	{
		//	How long back in History?
		super (parent, Msg.getMsg(Env.getCtx(), "VHistory", true), true);
		m_AD_Tab_ID = AD_Tab_ID;
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "VHistory", e);
		}
		this.pack();
		buttonLocation.x -= (int)getPreferredSize().getWidth()/2;
		this.setLocation(buttonLocation);
		this.setVisible(true);
	}	//	VOnlyCurrentDays

	private CPanel mainPanel = new CPanel();
	private CButton bShowAll = new CButton();
	private CButton bShowMonth = new CButton();
	private CButton bShowWeek = new CButton();
	private CButton bShowDay = new CButton();
	private CButton bShowYear = new CButton();
	private CCheckBox cbCreated = new CCheckBox(Msg.getMsg(Env.getCtx(), "Created"), true);
	private CComboBox comboQueries = new CComboBox();

	
	/** The Tab					*/
	private int		m_AD_Tab_ID = 0;
	/**	Days (0=all)			*/
	private int 	m_days = 0;
	/**	Created Flag			*/
	private boolean	m_created = true;
	/** Query Where Clause		*/
	private String	m_sqlWhere = null;
	/**	Margin					*/
	private static Insets	s_margin = new Insets (2, 3, 2, 3);
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VHistory.class);

	/**
	 * 	Static Initializer
	 * 	@throws Exception
	 */
	private void jbInit() throws Exception
	{
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		bShowAll.setText(Msg.getMsg(Env.getCtx(), "All"));
		bShowAll.addActionListener(this);
		bShowAll.setMargin(s_margin);
		bShowYear.setText(Msg.getMsg(Env.getCtx(), "Year"));
		bShowYear.addActionListener(this);
		bShowYear.setMargin(s_margin);
		bShowMonth.setText(Msg.getMsg(Env.getCtx(), "Month"));
		bShowMonth.addActionListener(this);
		bShowMonth.setMargin(s_margin);
		bShowWeek.setText(Msg.getMsg(Env.getCtx(), "Week"));
		bShowWeek.addActionListener(this);
		bShowWeek.setMargin(s_margin);
		bShowDay.setText(Msg.getMsg(Env.getCtx(), "Day"));
		bShowDay.addActionListener(this);
		bShowDay.setMargin(s_margin);
		bShowDay.setDefaultCapable(true);
		//
		cbCreated.setMargin (s_margin);
		cbCreated.setToolTipText ("tool tip text");
		//
		mainPanel.add(bShowDay, null);
		mainPanel.add(bShowWeek, null);
		mainPanel.add(bShowMonth, null);
		mainPanel.add(bShowYear, null);
		mainPanel.add(bShowAll, null);
		mainPanel.add(cbCreated, null);
		if (fillQuery())
			mainPanel.add(comboQueries, null);
		//
		mainPanel.setToolTipText(Msg.getMsg(Env.getCtx(), "VHistory", false));
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		this.getRootPane().setDefaultButton(bShowDay);
	}	//	jbInit

	/**
	 * 	Fill Query
	 *	@return true if queries exist
	 */
	private boolean fillQuery()
	{
		MUserQuery[] queries = MUserQuery.get (Env.getCtx(), m_AD_Tab_ID);
		if (queries.length == 0)
			return false;
		//
		Vector<ValueNamePair> vector = new Vector<ValueNamePair>();
		ValueNamePair pp = new ValueNamePair("", Msg.getMsg (Env.getCtx(), "SelectQuery"));
		vector.add(pp);
		for (int i = 0; i < queries.length; i++)
		{
			MUserQuery query = queries[i];
			pp = new ValueNamePair(query.getCode(), query.getName());
			vector.add(pp);
		}
		ComboBoxModel model = new DefaultComboBoxModel(vector);
		comboQueries.setModel(model);
		comboQueries.setToolTipText (Msg.getMsg(Env.getCtx(),"QueryName"));
		comboQueries.addActionListener(this);
		return true;
	}	//	fillQuery
	
	
	/**
	 * 	Action Listener
	 * 	@param e evant
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bShowDay)
			m_days = 1;
		else if (e.getSource() == bShowWeek)
			m_days = 7;
		else if (e.getSource() == bShowMonth)
			m_days = 31;
		else if (e.getSource() == bShowYear)
			m_days = 356;
		else
			m_days = 0;		//	all
		m_created = cbCreated.isSelected();
		Object xx = comboQueries.getSelectedItem();
		if (xx instanceof ValueNamePair)
		{
			ValueNamePair pp = (ValueNamePair)xx;
			m_sqlWhere = pp.getValue();
		}
		//
		dispose();
	}	//	actionPerformed

	/**
	 * 	Get selected number of days
	 * 	@return days or -1 for all
	 */
	public int getCurrentDays()
	{
		return m_days;
	}	//	getCurrentDays

	/**
	 * 	Get created or uupdated
	 * 	@return true if created
	 */
	public boolean getIsCreated()
	{
		return m_created;
	}	//	getIsCreated

	/**
	 * 	Get sqlWhere
	 * 	@return "" or where clause
	 */
	public String getSqlWhere()
	{
		return m_sqlWhere;
	}	//	getSqlWhere

	
}	//	VOnlyCurrentDays
