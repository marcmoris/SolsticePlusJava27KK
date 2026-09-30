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

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import org.compiere.apps.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

import java.util.logging.*;

/**
 *	Search Job Title  and return selection
 *
 *  @author     Progestion Informatique
 *  @version    $Id: InfoJobTitle.java,v 1.3 2007/07/25 21:56:22 marmor01 Exp $
 */
public final class InfoJobTitle extends Info implements ActionListener
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
	public InfoJobTitle(Frame frame, boolean modal, int WindowNo,
		String value, boolean multiSelection, String whereClause)
	{
		super (frame, modal, WindowNo, "jt", "P_Job_Title_ID", multiSelection, whereClause);
		setTitle(Msg.getMsg(Env.getCtx(), "InfoJobTitle"));
		
		if(Env.getContext(Env.getCtx(), "$Finding") != null
		        && Env.getContextAsInt(Env.getCtx(), "$Finding") == 0)
		{
		    OccupationGroupID = Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Occupation_Group_ID");
		}

		statInit();
		initInfo (value, WindowNo);
//		m_C_BPartner_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "C_BPartner_ID");

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
	}	//	InfoJobTitle

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (MPeriod.class);

	private static Language language = Env.getLanguage(Env.getCtx());

	private static final String s_From =
		"P_Job_Title jt ";
		//+ " LEFT OUTER JOIN P_Occupation_Group og ON (jt.P_Occupation_Group_ID=og.P_Occupation_Group_ID)";
		
	/**  Array of Grid Column Info    */
	private static final Info_Column[] s_job_titleLayout = {
		new Info_Column(" ", "jt.P_Job_Title_ID", IDColumn.class),
		new Info_Column("Value", "jt.Value", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Name"), "jt.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Description"), "jt.Description", String.class)
		//new Info_Column(Msg.translate(Env.getCtx(), "P_Occupation_Group_ID"), new PgiUtil.QueryField("P_Occupation_Group", "og", "Name").getSQLColumn(), String.class)
	};
	
	/**
	 *	Get Layout
	 *	@return array of Column_Info
	 */
	protected Info_Column[] getInfoColumns()
	{
		return s_job_titleLayout;
	}	//	getInfoColumns
	
	private static int INDEX_NAME = 3;
	private static int INDEX_PATTRIBUTE = s_job_titleLayout.length - 1;	//	last item
	
	private int OccupationGroupID = 0;

	//
	private CLabel labelValue = new CLabel();
	private CTextField fieldValue = new CTextField(12);
	private CLabel labelName = new CLabel();
	private CTextField fieldName = new CTextField(12);
	private CLabel labelOccupationGroup = new CLabel();
	//private VComboBox pickOccupationGroup = new VComboBox();
	private CLabel labelOccupationGroupData = new CLabel();
	
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

		labelOccupationGroup.setText(Msg.translate(Env.getCtx(), "P_Occupation_Group_ID"));
		//pickOccupationGroup.setBackground(CompierePLAF.getInfoBackground());
			
		//	Line 1
		parameterPanel.setLayout(new ALayout());
		parameterPanel.add(labelValue, new ALayoutConstraint(0,0));
		parameterPanel.add(fieldValue, null);
		parameterPanel.add(labelName, null);
		parameterPanel.add(fieldName, null);
		if(this.OccupationGroupID > 0)
		{
			parameterPanel.add(labelOccupationGroup, null);
			parameterPanel.add(labelOccupationGroupData, null);		
		}
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
		//	Pick init
		//fillOccupationGroupPick(OccupationGroupID );
	    
	    if(this.OccupationGroupID > 0)
	    {
			String sql;
			
		    if (language.isBaseLanguage() )
 		    	sql = "SELECT Name FROM P_Occupation_Group WHERE P_Occupation_Group_ID = " + OccupationGroupID;
		    else
 		    	sql = "SELECT P_Occupation_Group_Trl.Name FROM P_Occupation_Group, P_Occupation_Group_TRL"
 		    		+ " WHERE P_Occupation_Group_ID = " + OccupationGroupID
		            + " P_Occupation_Group.P_Occupation_Group_ID = P_Occupation_Group_Trl.P_Occupation_Group_ID "
	    	        + " AND P_Occupation_Group_Trl.AD_Language = '" + language.getLocale() + "'";
	    
		    	
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				
				if (rs.next())
					labelOccupationGroupData.setText(rs.getString("Name"));
				else
					labelOccupationGroupData.setText(Msg.getMsg(Env.getCtx(), "SelectOccupationGroup"));
				
				rs.close();
				pstmt.close();
			}
			catch (Exception e)
			{
				s_log.log(Level.SEVERE,"InfoJobTitle.initInfo", e);
				setStatusLine(e.getLocalizedMessage(), true);
			}
			
			System.out.println(" InfoJobTitle : occup " + Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Occupation_Group_ID"));
	    }
	    
		//	Create Grid
		prepareTable(s_From,
					"jt.IsActive='Y'",
					"jt.Value, jt.Name");
		
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
			where.append(" AND UPPER(jt.Value) LIKE ?");

		//  => Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
			where.append(" AND UPPER(jt.Name) LIKE ?");
		
		//    => Occupation Group
		/*int P_Occupation_Group_ID = 0;
		KeyNamePair pl = (KeyNamePair)pickOccupationGroup.getSelectedItem();
		if (pl != null)
			P_Occupation_Group_ID = pl.getKey();
		if (P_Occupation_Group_ID != 0)
			where.append(" AND og.P_Occupation_Group_ID=?");
		*/
		
		if(this.OccupationGroupID > 0)
		    where.append(" AND P_Occupation_Group_ID = " + this.OccupationGroupID);
	

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
		
		//  => Occupation_Group
		/*int P_Occupation_Group_ID = 0;
		KeyNamePair pl = (KeyNamePair)pickOccupationGroup.getSelectedItem();
		if (pl != null)
			P_Occupation_Group_ID = pl.getKey();
		if (P_Occupation_Group_ID != 0)
		{
			pstmt.setInt(index++, P_Occupation_Group_ID);
		}
		*/
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
		Integer P_Job_Title_ID = getSelectedRowKey();
		if (P_Job_Title_ID == null)
			return;
		MQuery query = new MQuery("P_Job_Title");
		query.addRestriction("P_Job_Title_ID", MQuery.EQUAL, P_Job_Title_ID);
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
		Env.setContext(Env.getCtx(), Env.WINDOW_INFO, Env.TAB_INFO, "P_Job_Title_ID", ID == null ? "0" : ID.toString());
		
	}	//	saveSelectionDetail

	
}	//	InfoJobTitle

	
	