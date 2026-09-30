/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2001 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.apps.search;

import java.awt.Frame;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.compiere.apps.AEnv;
import org.compiere.apps.ALayout;
import org.compiere.apps.ALayoutConstraint;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.Info_Column;
import org.compiere.model.MQuery;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.CLabel;
import org.compiere.swing.CTextField;
import org.compiere.util.*;

/**
 *	Search Business Partner and return selection
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: InfoDeduction.java,v 1.3 2007/10/10 15:38:16 marmor01 Exp $
 */
public final class InfoDeduction extends Info
{
	/**
	 *	Standard Constructor
	 *  @param frame frame
	 *  @param modal modal
	 *  @param WindowNo WindowNo
	 *  @param  value   Query value Name or Value if contains numbers
	 *  @param isSOTrx  if false, query vendors only
	 *  @param multiSelection multiple selection
	 *  @param whereClause where clause
	 */
	public InfoDeduction(Frame frame, boolean modal, int WindowNo, String value, boolean isSOTrx, boolean multiSelection, String whereClause)
	{
		super (frame, modal, WindowNo, "P_Deduction", "P_Deduction_ID", multiSelection, whereClause);
		setTitle(Msg.getMsg(Env.getCtx(), "InfoDeduction"));
		m_isSOTrx = isSOTrx;
		//
		statInit();
		initInfo (value, whereClause);
		//
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
	}	//	InfoDeduction

	/** SalesOrder Trx          */
	private boolean 		m_isSOTrx = false;


	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (InfoDeduction.class);

	/** From Clause             */
	private static String s_FROM = "P_Deduction left outer join P_Deduction_TRL on P_Deduction_TRL.P_Deduction_ID = P_Deduction.P_Deduction_ID ";
	
    Language language = Env.getLanguage(Env.getCtx());

	/**  Array of Column Info    */
	private static Info_Column[] s_Layout = {
		new Info_Column(" ", "P_Deduction.P_Deduction_ID", IDColumn.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Value"), "P_Deduction.Value", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Name"), "P_Deduction.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Description"), "P_Deduction.Description", String.class)
	};

	/**  Array of Column Info    */
	private static Info_Column[] s_LayoutTrl = {
		new Info_Column(" ", "P_Deduction.P_Deduction_ID", IDColumn.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Value"), "P_Deduction.Value", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Name"), "P_Deduction_Trl.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Description"), "P_Deduction_Trl.Description", String.class)
	};

	
	/**
	 *	Get Layout
	 *	@return array of Column_Info
	 */
	protected Info_Column[] getInfoColumns()
	{
	    if (language.isBaseLanguage() )
			return s_Layout;
	    else
			return s_LayoutTrl;
	}	//	getInfoColumns

	//
	private CLabel labelValue = new CLabel();
	private CTextField fieldValue = new CTextField(10);
	private CLabel labelName = new CLabel();
	private CTextField fieldName = new CTextField(10);

	/**
	 *	Static Setup - add fields to parameterPanel
	 */
	private void statInit()
	{
		labelValue.setText(Msg.getMsg(Env.getCtx(), "Value"));
		fieldValue.setBackground(CompierePLAF.getInfoBackground());
		fieldValue.addActionListener(this);

		labelName.setText(Msg.getMsg(Env.getCtx(), "Name"));
		fieldName.setBackground(CompierePLAF.getInfoBackground());
		fieldName.addActionListener(this);

		//
		parameterPanel.setLayout(new ALayout());
		parameterPanel.add(labelValue, new ALayoutConstraint(0,0));
		parameterPanel.add(fieldValue, null);
		parameterPanel.add(labelName, null);
		parameterPanel.add(fieldName, null);
	}	//	statInit

	/**
	 *	Dynamic Init
	 *  @param value value
	 *  @param whereClause where clause
	 */
	private void initInfo(String value, String whereClause)
	{
		//	Create Grid
		StringBuffer where = new StringBuffer();
		where.append("P_Deduction.IsActive='Y'");
		if (whereClause != null && whereClause.length() > 0)
			where.append(" AND ").append(whereClause);
		//
		prepareTable(s_FROM ,
			where.toString(),
			"P_Deduction.Value");

		//  Set Value
		if (value == null)
			value = "%";
		if (!value.endsWith("%"))
			value += "%";
		
		fieldValue.setText(value);
		
	}	//	initInfo

	/*************************************************************************/

	/**
	 *	Construct SQL Where Clause and define parameters.
	 *  (setParameters needs to set parameters)
	 *  Includes first AND
	 *  @return WHERE clause
	 */
	String getSQLWhere()
	{
		ArrayList<String> list = new ArrayList<String>();
		//	=> Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
			list.add ("UPPER(P_Deduction.Value) LIKE ?");
		//	=> Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
			list.add ("UPPER(P_Deduction.Name) LIKE ?");

		StringBuffer sql = new StringBuffer();
		int size = list.size();
		//	Just one
		if (size == 1)
			sql.append(" AND ").append(list.get(0));
		return sql.toString();
	}	//	getSQLWhere

	/**
	 *  Set Parameters for Query.
	 *  (as defined in getSQLWhere)
	 *  @param pstmt pstmt
	 *  @throws SQLException
	 */

	void setParameters(PreparedStatement pstmt, boolean forCount) throws SQLException
	{
		int index = 1;
		//	=> Value
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

	/*************************************************************************/

	/**
	 *  Save Selection Details
	 *  Get Location/Partner Info
	 */
	public void saveSelectionDetail()
	{
		int row = p_table.getSelectedRow();
		if (row == -1)
			return;

		//  publish for Callout to read
		Integer ID = getSelectedRowKey();
		Env.setContext(Env.getCtx(), Env.WINDOW_INFO, Env.TAB_INFO, "P_Deduction_ID", ID == null ? "0" : ID.toString());
	}   //  saveSelectionDetail


	/*************************************************************************/


	/**
	 *	Zoom
	 */
	void zoom()
	{
		Integer P_Deduction_ID = getSelectedRowKey();
		if (P_Deduction_ID == null)
			return;
		MQuery query = new MQuery("P_Deduction");
		query.addRestriction("P_Deduction_ID", MQuery.EQUAL, P_Deduction_ID);
		zoom (123, query);
	}	//	zoom

	/**
	 *	Has Zoom
	 *  @return true
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
	 *  @return false
	 */
	boolean hasCustomize()
	{
		return false;	//	for now
	}	//	hasCustomize

}	//	InfoDeduction
