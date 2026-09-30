/*
 * Created on 2005-09-05
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.compiere.apps.search;

import java.awt.Frame;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.compiere.apps.AEnv;
import org.compiere.apps.ALayout;
import org.compiere.apps.ALayoutConstraint;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.Info_Column;
import org.compiere.model.MPeriod;
import org.compiere.model.MQuery;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.CLabel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 *	Search Credits and return selection
 *
 *  @author     Progestion Informatique
 *  @version    $Id: InfoCredits.java,v 1.2 2007/07/18 21:09:11 marmor01 Exp $
 */
public final class InfoCredits extends Info implements ActionListener
{
	/**
	 *	Standard Constructor

	 * @param frame frame
	 * @param modal modal
	 * @param WindowNo window no
	 * @param M_Warehouse_ID warehouse
	 * @param M_PriceList_ID price list
	 * @param value    Query Value or Name if enclosed in @
	 * @param multiSelection multiple selections
	 * @param whereClause where clause
	 */
	public InfoCredits(Frame frame, boolean modal, int WindowNo,
		String value, boolean multiSelection, String whereClause)
	{
		super (frame, modal, WindowNo, "c", "P_Credits_ID", multiSelection, whereClause);
		setTitle(Msg.getMsg(Env.getCtx(), "InfoCredits"));
		
		statInit();
		initInfo (value, WindowNo);

		int no = p_table.getRowCount();
		setStatusLine(Integer.toString(no) + " " + Msg.getMsg(Env.getCtx(), "SearchRows_EnterQuery"), false);
		setStatusDB(Integer.toString(no));
		//	AutoQuery
		if (value != null && value.length() > 0)
			executeQuery();
		p_loadedOK = true;
		//	Focus
		fieldValue.requestFocus();

		AEnv.positionCenterWindow(frame, this);
	}	//	InfoCredits

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (MPeriod.class);

	private static final String s_productFrom =
		"P_Credits c INNER JOIN P_Credits_TRL ON P_Credits_TRL.P_Credits_ID = c.P_Credits_ID ";
		
	/**  Array of Grid Column Info    */
	private static final Info_Column[] c_creditsLayout = {
		new Info_Column(" ", "c.P_Credits_ID", IDColumn.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Value"), "c.Value", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Name"), "P_Credits_TRL.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Description"), "P_Credits_TRL.Description", String.class)
	};
	
	/**
	 *	Get Layout
	 *	@return array of Column_Info
	 */
	protected Info_Column[] getInfoColumns()
	{
		return c_creditsLayout;
	}	//	getInfoColumns

	private static int INDEX_NAME = 3;
	private static int INDEX_PATTRIBUTE = c_creditsLayout.length - 1;	//	last item
	
	//
	private CLabel labelValue = new CLabel();
	private CTextField fieldValue = new CTextField(12);
	private CLabel labelName = new CLabel();
	private CTextField fieldName = new CTextField(12);
	
	/**
	 *	Static Setup - add fields to parameterPanel
	 */
	private void statInit()
	{
		labelValue.setText(Msg.translate(Env.getCtx(), "Value"));
		fieldValue.setBackground(CompierePLAF.getInfoBackground());
		fieldValue.addActionListener(this);
		
		labelName.setText(Msg.translate(Env.getCtx(), "Name"));
		fieldName.setBackground(CompierePLAF.getInfoBackground());
		fieldName.addActionListener(this);

		//	Line 1
		parameterPanel.setLayout(new ALayout());
		parameterPanel.add(labelValue, new ALayoutConstraint(0,0));
		parameterPanel.add(fieldValue, null);
		parameterPanel.add(labelName, null);
		parameterPanel.add(fieldName, null);
	
	}	//	statInit

	/**
	 *	Dynamic Init
	 *
	 * @param value value
	 * @param M_Warehouse_ID warehouse
	 * @param M_PriceList_ID price list
	 */
	private void initInfo (String value, int WindowNo)
	{
		//	Create Grid
		prepareTable(s_productFrom,
					"c.IsActive='Y'",
					"c.Value, c.Name");
		
		// Set Value
		if (value == null)
			value = "%";
		if (!value.endsWith("%"))
			value += "%";
		
		fieldValue.setText(value);

	}	//	initInfo

		/**************************************************************************
	 *	Construct SQL Where Clause and define parameters
	 *  (setParameters needs to set parameters)
	 *  Includes first AND
	 *  @return SQL WHERE clause
	 */
	String getSQLWhere()
	{
		StringBuffer where = new StringBuffer();

		//	  => Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
			where.append(" AND UPPER(c.Value) LIKE ?");

		//  => Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
			where.append(" AND UPPER(c.Name) LIKE ?");
				
		return where.toString();
	}	//	getSQLWhere

	/**
	 *  Set Parameters for Query
	 *  (as defined in getSQLWhere)
	 *
	 * @param pstmt pstmt
	 * @throws SQLException
	 * @see org.compiere.apps.search.Info#setParameters(java.sql.PreparedStatement)
	 */
	void setParameters(PreparedStatement pstmt, boolean forCount) throws SQLException
	{
		int index = 1;

		//  => Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
		{
			if (!value.endsWith("%"))
				value += "%";
			pstmt.setString(index++, value);
		}

		//	=> Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
		{
			if (!name.endsWith("%"))
				name += "%";
			pstmt.setString(index++, name);
		}
		
	}   //  setParameters

	/**
	 *	Has History
	 *
	 * @return true (has history)
	 */
	boolean hasHistory()
	{
		return true;
	}	//	hasHistory

	/**
	 *	Zoom
	 */
	void zoom()
	{
		Integer P_Credits_ID = getSelectedRowKey();
		if (P_Credits_ID == null)
			return;
		MQuery query = new MQuery("P_Credits");
		query.addRestriction("P_Credits_ID", MQuery.EQUAL, P_Credits_ID);
		zoom (123, query);

	}	//	zoom

	/**
	 *	Has Zoom
	 *  @return (has zoom)
	 */
	boolean hasZoom()
	{
		return true;
	}	//	hasZoom

	/**
	 *	Customize
	 */
	void customize()
	{
	}	//	customize

	/**
	 *	Has Customize
	 *  @return false (no customize)
	 */
	boolean hasCustomize()
	{
		return false;	//	for now
	}	//	hasCustomize

	/**
	 *	Save Selection Settings 
	 */
	void saveSelectionDetail()
	{
		int row = p_table.getSelectedRow();
		if (row == -1)
			return;

		//  publish for Callout to read
		Integer ID = getSelectedRowKey();
		Env.setContext(Env.getCtx(), Env.WINDOW_INFO, Env.TAB_INFO, "P_Credits_ID", ID == null ? "0" : ID.toString());
		
	}	//	saveSelectionDetail

	/**
	 *  Get Product Layout
	 *
	 * @return array of Column_Info
	 */
	private Info_Column[] getCreditsLayout()
	{
		//  Euro 13
		if (Env.getContext(Env.getCtx(), "#Client_Value").equals("FRIE"))
		{
			// Info search field
			final Info_Column[] frieLayout = {
				new Info_Column(" ", "c.P_Credits_ID", IDColumn.class),
				new Info_Column(Msg.translate(Env.getCtx(), "Value"), "c.Value", String.class),
				new Info_Column(Msg.translate(Env.getCtx(), "Name"), "c.Name", String.class)
			};
			INDEX_NAME = 2;
			INDEX_PATTRIBUTE = c_creditsLayout.length - 1;	//	last item
			return frieLayout; 
		}
		return c_creditsLayout;
	}   //  getCreditsLayout
	
}	//	InfoCredits
