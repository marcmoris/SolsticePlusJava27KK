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

import java.awt.event.*;
import java.sql.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;


/**
 *	Request Button Action.
 *	Popup Menu
 *	
 *  @author Jorg Janke
 *  @version $Id: ARequest.java,v 1.2 2007/07/18 21:09:24 marmor01 Exp $
 */
public class ARequest implements ActionListener
{
	/**
	 * 	Constructor
	 *	@param invoker invoker button
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 *	@param C_BPartner_ID optional bp
	 */
	public ARequest (JComponent invoker, int AD_Table_ID, int Record_ID,
		int C_BPartner_ID)
	{
		super ();
		log.config("AD_Table_ID=" + AD_Table_ID + ", Record_ID=" + Record_ID);
		m_AD_Table_ID = AD_Table_ID;
		m_Record_ID = Record_ID;
		m_C_BPartner_ID = C_BPartner_ID;
		getRequests(invoker);
		
		// register enter for when popup is visible
		InputMap imap = m_popup.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), m_enterAction);
		m_popup.getActionMap().put(m_enterAction, m_enterAction);
	}	//	ARequest
	
	/**	The Table						*/
	private int			m_AD_Table_ID;
	/** The Record						*/
	private int			m_Record_ID;
	/** BPartner						*/
	private int			m_C_BPartner_ID;
	
	/**	The Popup						*/
	private JPopupMenu 	m_popup = new JPopupMenu("RequestMenu");

	/** Action for enter key. */
	Action 	m_enterAction = new AbstractAction()
	{
		public void actionPerformed(ActionEvent ae)
		{
			ARequest.this.actionPerformed(ae);
		}
	};
	CMenuItem 	m_new = null;
	CMenuItem 	m_active = null;
	CMenuItem 	m_all = null;
	/** Where Clause					*/
	StringBuffer 		m_where = null;
	
	/**	Logger	*/
	private static CLogger	log	= CLogger.getCLogger (ARequest.class);
	
	/**
	 * 	Display Request Options - New/Existing.
	 * 	@param invoker button
	 */
	private void getRequests (JComponent invoker)
	{
		m_new = new CMenuItem(Msg.getMsg(Env.getCtx(), "RequestNew"));
		m_new.setIcon(Env.getImageIcon("New16.gif"));
		m_popup.add(m_new);
		m_new.addActionListener(this);
		m_new.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				if (m_new.isArmed())
					m_enterAction.putValue(Action.ACTION_COMMAND_KEY, m_new.getText());
			}
		});
		//
		int activeCount = 0;
		int inactiveCount = 0;
		m_where = new StringBuffer();
		m_where.append("(AD_Table_ID=").append(m_AD_Table_ID)
			.append(" AND Record_ID=").append(m_Record_ID)
			.append(")");
		//
		if (m_AD_Table_ID == MUser.Table_ID)
			m_where.append(" OR AD_User_ID=").append(m_Record_ID)
				.append(" OR SalesRep_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MBPartner.Table_ID)
			m_where.append(" OR C_BPartner_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MOrder.Table_ID)
			m_where.append(" OR C_Order_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MInvoice.Table_ID)
			m_where.append(" OR C_Invoice_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MPayment.Table_ID)
			m_where.append(" OR C_Payment_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MProduct.Table_ID)
			m_where.append(" OR M_Product_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MProject.Table_ID)
			m_where.append(" OR C_Project_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MCampaign.Table_ID)
			m_where.append(" OR C_Campaign_ID=").append(m_Record_ID);
		else if (m_AD_Table_ID == MAsset.Table_ID)
			m_where.append(" OR A_Asset_ID=").append(m_Record_ID);
		//
		String sql = "SELECT Processed, COUNT(*) "
			+ "FROM R_Request WHERE " + m_where 
			+ " GROUP BY Processed "
			+ "ORDER BY Processed DESC";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				if ("Y".equals(rs.getString(1)))
					inactiveCount = rs.getInt(2);
				else
					activeCount += rs.getInt(2);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}

		//
		if (activeCount > 0)
		{
			m_active = new CMenuItem(Msg.getMsg(Env.getCtx(), "RequestActive") 
				+ " (" + activeCount + ")");
			m_popup.add(m_active);
			m_active.addActionListener(this);
			m_active.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent e)
				{
					if (m_active.isArmed())
						m_enterAction.putValue(Action.ACTION_COMMAND_KEY, m_active.getText());
				}
			});
		}
		if (inactiveCount > 0)
		{
			m_all = new CMenuItem(Msg.getMsg(Env.getCtx(), "RequestAll") 
				+ " (" + (activeCount + inactiveCount) + ")");
			m_popup.add(m_all);
			m_all.addActionListener(this);
			m_all.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent e)
				{
					if (m_all.isArmed())
						m_enterAction.putValue(Action.ACTION_COMMAND_KEY, m_all.getText());
				}
			});
		}
		//
		if (invoker.isShowing())
			m_popup.show(invoker, 0, invoker.getHeight());	//	below button
	}	//	getZoomTargets
	
	/**
	 * 	Listner
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		MQuery query = null;
		String actionCommand = e.getActionCommand();
		if (m_active != null && m_active.getText().equals(actionCommand))
		{
			query = new MQuery("");
			String where = "(" + m_where + ") AND Processed='N'";
			query.addRestriction(where);
		}
		else if (m_all != null && m_all.getText().equals(actionCommand))
		{
			query = new MQuery("");
			query.addRestriction(m_where.toString());
		}
		//
		int AD_Window_ID = 232;		//	232=all - 201=my
		AWindow frame = new AWindow();
		if (!frame.initWindow(AD_Window_ID, query))
			return;
		
		//	New - set Table/Record
		if (m_new.getText().equals(actionCommand))
		{
			GridTab tab = frame.getAPanel().getCurrentTab();
			tab.dataNew (false);
			tab.setValue("AD_Table_ID", new Integer(m_AD_Table_ID));
			tab.setValue("Record_ID", new Integer(m_Record_ID));
			//
			if (m_C_BPartner_ID != 0)
				tab.setValue("C_BPartner_ID", new Integer(m_C_BPartner_ID));
			//
			if (m_AD_Table_ID == MBPartner.Table_ID)
				tab.setValue("C_BPartner_ID", new Integer(m_Record_ID));
			else if (m_AD_Table_ID == MUser.Table_ID)
				tab.setValue("AD_User_ID", new Integer(m_Record_ID));
			//
			else if (m_AD_Table_ID == MProject.Table_ID)
				tab.setValue("C_Project_ID", new Integer(m_Record_ID));
			else if (m_AD_Table_ID == MAsset.Table_ID)
				tab.setValue("A_Asset_ID", new Integer(m_Record_ID));
			//
			else if (m_AD_Table_ID == MOrder.Table_ID)
				tab.setValue("C_Order_ID", new Integer(m_Record_ID));
			else if (m_AD_Table_ID == MInvoice.Table_ID)
				tab.setValue("C_Invoice_ID", new Integer(m_Record_ID));
			//
			else if (m_AD_Table_ID == MProduct.Table_ID)
				tab.setValue("M_Product_ID", new Integer(m_Record_ID));
			else if (m_AD_Table_ID == MPayment.Table_ID)
				tab.setValue("C_Payment_ID", new Integer(m_Record_ID));
			//
			else if (m_AD_Table_ID == MInOut.Table_ID)
				tab.setValue("M_InOut_ID", new Integer(m_Record_ID));
			else if (m_AD_Table_ID == MRMA.Table_ID)
				tab.setValue("M_RMA_ID", new Integer(m_Record_ID));
			//
			else if (m_AD_Table_ID == MCampaign.Table_ID)
				tab.setValue("C_Campaign_ID", new Integer(m_Record_ID));
		}
		AEnv.showCenterScreen(frame);
		frame = null;
	}	//	actionPerformed
	
}	//	ARequest
