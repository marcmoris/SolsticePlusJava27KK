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
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;

import java.util.logging.*;

/**
 *	Search Employee and return selection
 *
 *  @author     Progestion Informatique
 *  @version    $Id: InfoEmployee.java,v 1.3 2007/10/24 12:21:37 marmor01 Exp $
 */
public final class InfoEmployee extends Info implements ActionListener
{
	/**
	 *	Standard Constructor

	 * @param frame frame
	 * @param modal modal
	 * @param WindowNo window no
	 * @param value    Query Value or Name if enclosed in @
	 * @param multiSelection multiple selections
	 * @param whereClause where clause
	 */
	public InfoEmployee(Frame frame, boolean modal, int WindowNo,
		String value, boolean multiSelection, String whereClause)
	{
		super (frame, modal, WindowNo, "P_Employee", "P_Employee_ID", multiSelection, whereClause);
		setTitle(Msg.getMsg(Env.getCtx(), "InfoEmployee"));
		//
		m_P_Employee_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Employee_ID");
		m_P_Occupation_Group_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Occupation_Group_ID");
		
		statInit();
		initInfo (value, m_P_Occupation_Group_ID);
		

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
	}	//	InfoEmployee

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (InfoEmployee.class);

	private static final String s_EmployeeFrom = "P_Employee "
	  + " LEFT OUTER JOIN P_Occupation_Group ON (P_Employee.P_Occupation_Group_ID=P_Occupation_Group.P_Occupation_Group_ID)"
	  + " LEFT OUTER JOIN P_Payment_Group ON (P_Employee.P_Payment_Group_ID=P_Payment_Group.P_Payment_Group_ID)"
	  + " LEFT OUTER JOIN P_Job_Title ON (P_Employee.P_Job_Title_ID=P_Job_Title.P_Job_Title_ID)";
	
	/**  Array of Column Info    */
	private static final Info_Column[] s_EmployeeLayout = {
		new Info_Column(" ", "P_Employee.P_Employee_ID", IDColumn.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Value"), "P_Employee.Value", String.class),
//		new Info_Column(Msg.translate(Env.getCtx(), "Name"), "P_Employee.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "SurName"), "P_Employee.SurName", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "FirstName"), "P_Employee.FirstName", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Sin"), "P_Employee.Sin", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Phone"), "P_Employee.Phone", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Gender"), "case P_Employee.Gender when 'M' then 'Homme' else 'Femme' end ", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Payment_Group_ID"), "P_Payment_Group.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Occupation_Group_ID"), "P_Occupation_Group.Value + ' - ' + P_Occupation_Group.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Job_Title_ID"), "P_Job_Title.Value + ' - ' + P_Job_Title.Name", String.class)
	};

	/**
	 *	Get Layout
	 *	@return array of Column_Info
	 */
	protected Info_Column[] getInfoColumns()
	{
		return s_EmployeeLayout;
	}	//	getInfoColumns

	
	private static int INDEX_NAME = 3;
	private static int INDEX_PATTRIBUTE = s_EmployeeLayout.length - 1;	//	last item

	//
	private CLabel labelValue = new CLabel();
	private CTextField fieldValue = new CTextField(10);
	private CLabel labelName = new CLabel();
	private CTextField fieldName = new CTextField(10);
	private CLabel labelFirstName = new CLabel();
	private CTextField fieldFirstName = new CTextField(10);
	private CLabel labelSin = new CLabel();
	private CTextField fieldSin = new CTextField(10);
//	private CLabel labelPhone = new CLabel();
//	private CTextField fieldPhone = new CTextField(10);
	private CLabel labelGender = new CLabel();
	//private CTextField fieldGender = new CTextField(10);
	private VComboBox pickGender = new VComboBox();
	private CLabel labelOccupationGroup = new CLabel();
	private VComboBox pickOccupationGroup = new VComboBox();
	private CLabel labelJobTitle = new CLabel();
	private VComboBox pickJobTitle = new VComboBox();
	private CLabel labelPaymentGroup = new CLabel();
	private VComboBox pickPaymentGroup = new VComboBox();
//	private VCheckBox fIsActive = new VCheckBox ("IsActive", false, false, true, Msg.translate(Env.getCtx(), "IsActive"), "", false);
	//ajout du check box actif
	private CLabel labelIsActive = new CLabel();
	private VCheckBox checkIsActive = new VCheckBox();


	private int			m_P_Employee_ID = 0;
	private int			m_P_Occupation_Group_ID = 0;

	/**
	 *	Static Setup - add fields to parameterPanel
	 */
	private void statInit()
	{
		labelValue.setText(Msg.getMsg(Env.getCtx(), "Value"));
		fieldValue.setBackground(CompierePLAF.getInfoBackground());
		fieldValue.addActionListener(this);
/*		
		labelName.setText(Msg.getMsg(Env.getCtx(), "Name"));
		fieldName.setBackground(CompierePLAF.getInfoBackground());
		fieldName.addActionListener(this);
*/
		labelName.setText(Msg.getMsg(Env.getCtx(), "SurName"));
		fieldName.setBackground(CompierePLAF.getInfoBackground());
		fieldName.addActionListener(this);

		labelFirstName.setText(Msg.getMsg(Env.getCtx(), "FirstName"));
		fieldFirstName.setBackground(CompierePLAF.getInfoBackground());
		fieldFirstName.addActionListener(this);

		labelSin.setText(Msg.translate(Env.getCtx(), "Sin"));
		fieldSin.setBackground(CompierePLAF.getInfoBackground());
		fieldSin.addActionListener(this);

//		labelPhone.setText(Msg.translate(Env.getCtx(), "Phone"));
//		fieldPhone.setBackground(CompierePLAF.getInfoBackground());
//		fieldPhone.addActionListener(this);

		labelGender.setText(Msg.translate(Env.getCtx(), "Gender"));
		pickGender.setBackground(CompierePLAF.getInfoBackground());
		//pickGender.addActionListener(this);
		
		labelOccupationGroup.setText(Msg.translate(Env.getCtx(), "P_Occupation_Group_ID"));
		pickOccupationGroup.setBackground(CompierePLAF.getInfoBackground());
		
		labelJobTitle.setText(Msg.translate(Env.getCtx(), "P_Job_Title_ID"));
		pickJobTitle.setBackground(CompierePLAF.getInfoBackground());

		labelPaymentGroup.setText(Msg.translate(Env.getCtx(), "P_Payment_Group_ID"));
		pickPaymentGroup.setBackground(CompierePLAF.getInfoBackground());

		labelIsActive.setText(Msg.translate(Env.getCtx(), "IsActive"));
		checkIsActive.setBackground(CompierePLAF.getInfoBackground());

		checkIsActive.setSelected(true);
		
//		fIsActive.setSelected(!"N".equals(Env.getContext(Env.getCtx(), p_WindowNo, "IsSOTrx")));
//		fIsActive.addActionListener(this);


		//	Line 1
		parameterPanel.setLayout(new ALayout());
		parameterPanel.add(labelValue, new ALayoutConstraint(0,0));
		parameterPanel.add(fieldValue, null);

//		parameterPanel.add(labelPhone, null);
//		parameterPanel.add(fieldPhone, null);

		parameterPanel.add(labelPaymentGroup, null);
		parameterPanel.add(pickPaymentGroup, null);
		
		parameterPanel.add(labelSin, null);
		parameterPanel.add(fieldSin, null);

		//		parameterPanel.add(fIsActive, new ALayoutConstraint(0,5));

		//	Line 2
		parameterPanel.add(labelName, new ALayoutConstraint(1,0));
		parameterPanel.add(fieldName, null);
		parameterPanel.add(labelFirstName, null);
		parameterPanel.add(fieldFirstName, null);
		parameterPanel.add(labelOccupationGroup, null);
		parameterPanel.add(pickOccupationGroup, null);
		
		//  Line 3
		parameterPanel.add(labelJobTitle, new ALayoutConstraint(2,0));
		parameterPanel.add(pickJobTitle, null);
		parameterPanel.add(labelIsActive, null);
		parameterPanel.add(checkIsActive, null);
		parameterPanel.add(labelGender, null);
		parameterPanel.add(pickGender, null);
				
	}	//	statInit

	/**
	 *	Dynamic Init
	 *
	 * @param value value
	 */
	private void initInfo (String value, int OccupationGroupID)
	{
		//	Pick init
		fillPicks(OccupationGroupID);
		//	Set Value or Name
		if (value.startsWith("@") && value.endsWith("@"))
			fieldName.setText(value.substring(1,value.length()-1));
		else
		{
			if ( value.contains("1") || value.contains("2") || value.contains("3") || value.contains("4") || value.contains("5") || value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9") || value.contains("0")  )
				fieldValue.setText(value);
			else
				fieldName.setText(value);
		}

				//	Create Grid
		StringBuffer where = new StringBuffer();

		
		if ( Env.getContextAsInt(Env.getCtx(), "#P_Payment_Group_ID") > 0 )
			where.append("P_Employee.P_Payment_Group_ID = " + Env.getContextAsInt(Env.getCtx(), "#P_Payment_Group_ID") );
		else
			where.append(" 1 = 1 ");


		//  dynamic Where Clause
		where.append(  p_whereClause );

		
		prepareTable( s_EmployeeFrom.toString(),
			where.toString(),
			"P_Employee.Value ");

		//
		pickOccupationGroup.setActionCommand("OccupationGroupChange");
		pickOccupationGroup.addActionListener(this);
		pickPaymentGroup.addActionListener(this);
		pickGender.addActionListener(this);
		pickJobTitle.addActionListener(this);
	}	//	initInfo

	/**
	 *	Fill Picks with values
	 *
	 * @param M_PriceList_ID price list
	 */
	private void fillPicks (int OccupationGroup)
	{
	    pickGender.addItem(new KeyNamePair(0, ""));
	    pickGender.addItem(new KeyNamePair(1, "Homme"));
	    pickGender.addItem(new KeyNamePair(2, "Femme"));
	    
		//	Occupation Group
		String sql = "SELECT P_Occupation_Group.P_Occupation_Group_ID,"
			+ " Value + ' - ' + P_Occupation_Group.Name AS ValueName "
			+ "FROM P_Occupation_Group "
			+ "WHERE P_Occupation_Group.IsActive='Y' ";
		//	Add Access & Order
		sql = MRole.getDefault().addAccessSQL (sql, "P_Occupation_Group", true, false)	// fully qualidfied - RO 
			+ " ORDER BY Value + ' - ' + P_Occupation_Group.Name";
		try
		{
			pickOccupationGroup.addItem(new KeyNamePair (0, ""));
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				KeyNamePair kn = new KeyNamePair (rs.getInt(1), rs.getString(2));
				pickOccupationGroup.addItem(kn);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE,"InfoEmployee.fillPicks", e);
			setStatusLine(e.getLocalizedMessage(), true);
		}

		//	Payment Group
		sql = "SELECT P_Payment_Group.P_Payment_Group_ID,"
			+ " P_Payment_Group.Name AS ValueName "
			+ "FROM P_Payment_Group "
			+ "WHERE P_Payment_Group.IsActive='Y' ";
		//	Add Access & Order
		sql = MRole.getDefault().addAccessSQL (sql, "P_Payment_Group", true, false)	// fully qualidfied - RO 
			+ " ORDER BY P_Payment_Group.Name";
		try
		{
			pickPaymentGroup.addItem(new KeyNamePair (0, ""));
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				KeyNamePair kn2 = new KeyNamePair (rs.getInt(1), rs.getString(2));
				pickPaymentGroup.addItem(kn2);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE,"InfoEmployee.fillPicks", e);
			setStatusLine(e.getLocalizedMessage(), true);
		}

		//	Job title
		fillJobTitle(0);
	}	//	fillPicks


	/**
	 * Fill the JobTitle combobox with the associated OccupationGroup
	 * @param OccupationGroupID
	 */
	private void fillJobTitle (int OccupationGroupID)
	{
		pickJobTitle.removeAllItems();
		
		String sql = null;
		if ( OccupationGroupID != 0 )
			sql = "SELECT P_Job_Title_ID, Value + ' - ' + Name "
			+ " FROM P_Job_Title "
			+ " WHERE P_Occupation_Group_ID = " + OccupationGroupID;
			//+ " AND IsActive='Y' ";
		else 
			sql = "SELECT P_Job_Title_ID, Value + ' - ' + Name "
				+ " FROM P_Job_Title "
				+ " WHERE IsActive='Y' ";
				;

		//	Add Access & Order
		sql = MRole.getDefault().addAccessSQL (sql, "P_Job_Title", true, false)	// fully qualidfied - RO 
			+ " ORDER BY Value + ' - ' + Name";
		try
		{
			pickJobTitle.addItem(new KeyNamePair (0, ""));
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				KeyNamePair kn = new KeyNamePair (rs.getInt(1), rs.getString(2));
				pickJobTitle.addItem(kn);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE,"InfoEmployee.fillJobTitle", e);
			setStatusLine(e.getLocalizedMessage(), true);
		}
	}
	
	/**************************************************************************
	 *	Construct SQL Where Clause and define parameters
	 *  (setParameters needs to set parameters)
	 *  Includes first AND
	 *  @return SQL WHERE clause
	 */
	String getSQLWhere()
	{
		StringBuffer where = new StringBuffer();

		//	Optional 
		int P_Occupation_Group_ID = 0;
		KeyNamePair pl = (KeyNamePair)pickOccupationGroup.getSelectedItem();
		if (pl != null)
			P_Occupation_Group_ID = pl.getKey();
		if (P_Occupation_Group_ID != 0)
			where.append(" AND P_Occupation_Group.P_Occupation_Group_ID=?");
		
		// => JobTitle
		int P_Job_Title_ID = 0;
		KeyNamePair p2 = (KeyNamePair)pickJobTitle.getSelectedItem();
		if (p2 != null)
			P_Job_Title_ID = p2.getKey();
		if (P_Job_Title_ID != 0)
			where.append(" AND P_Employee.P_Job_Title_ID=?");

		// => Payment Group
		int P_Payment_Group_ID = 0;
		KeyNamePair pm = (KeyNamePair)pickPaymentGroup.getSelectedItem();
		if (pm != null)
			P_Payment_Group_ID = pm.getKey();
		if (P_Payment_Group_ID != 0)
			where.append(" AND P_Payment_Group.P_Payment_Group_ID=?");

		//  => Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
			where.append(" AND UPPER(P_Employee.Value) LIKE ?");
/*
		//  => Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
			where.append(" AND UPPER(P_Employee.Name) LIKE ?");
*/		
		//  => SurName
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
			where.append(" AND UPPER(P_Employee.Name) LIKE ?");
/*		if (!(name.equals("") || name.equals("%")))
			where.append(" AND UPPER(P_Employee.SurName) LIKE ?");
*/
		//  => FirstName
		String firstName = fieldFirstName.getText().toUpperCase();
		if (!(firstName.equals("") || firstName.equals("%")))
			where.append(" AND UPPER(P_Employee.Name) LIKE ?");
/*		if (!(firstName.equals("") || firstName.equals("%")))
			where.append(" AND UPPER(P_Employee.FirstName) LIKE ?");
*/
		//  => SIN
		String sin = fieldSin.getText().toUpperCase();
		if (!(sin.equals("") || sin.equals("%")))
			where.append(" AND UPPER(P_Employee.Sin) LIKE ?");

		//  => Gender
		if (pickGender.getSelectedItem() != null && ((KeyNamePair)pickGender.getSelectedItem()).getKey() > 0)
			where.append(" AND UPPER(P_Employee.Gender) LIKE ?");

		//  => Phone
//		String phone = fieldPhone.getText().toUpperCase();
//		if (!(phone.equals("") || phone.equals("%")))
//			where.append(" AND UPPER(P_Employee.Phone) LIKE ?");

		//IsActive
		if(checkIsActive.isSelected() == true)
		{
			where.append(" AND P_Employee.IsActive = 'Y' ");
		}

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

		//  => Occupation_Group
		int P_Occupation_Group_ID = 0;
		KeyNamePair pl = (KeyNamePair)pickOccupationGroup.getSelectedItem();
		if (pl != null)
			P_Occupation_Group_ID = pl.getKey();
		if (P_Occupation_Group_ID != 0)
		{
			pstmt.setInt(index++, P_Occupation_Group_ID);
		}
		
		// => JobTitle
		int P_Job_Title_ID = 0;
		KeyNamePair p2 = (KeyNamePair)pickJobTitle.getSelectedItem();
		if (p2 != null)
			P_Job_Title_ID = p2.getKey();
		if (P_Job_Title_ID != 0)
		{
			pstmt.setInt(index++, P_Job_Title_ID);
		}

		//  => Payment_Group
		int P_Payment_Group_ID = 0;
		KeyNamePair pg = (KeyNamePair)pickPaymentGroup.getSelectedItem();
		if (pg != null)
			P_Payment_Group_ID = pg.getKey();
		if (P_Payment_Group_ID != 0)
		{
			pstmt.setInt(index++, P_Payment_Group_ID);
		}

		//  => Value
		String value = "%" + fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
		{
			if (!value.endsWith("%"))
				value += "%";
			pstmt.setString(index++, value);
		}
		
/*		//  => Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
		{
			if (!name.endsWith("%"))
				name += "%";
			pstmt.setString(index++, name);
		}		
*/
		//  => SurName
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
		{
			if (!name.endsWith("%"))
				name += "%";
			if (!name.startsWith("%"))
				name = "%" + name;
			pstmt.setString(index++, name);
		}

		//  => FirstName
		String firstName = fieldFirstName.getText().toUpperCase();
		if (!(firstName.equals("") || firstName.equals("%")))
		{
			if (!firstName.endsWith("%"))
			    firstName += "%";
			if (!firstName.startsWith("%"))
				firstName = "%" + firstName;
			
			pstmt.setString(index++, firstName);
		}

		//  => Sin
		String sin = fieldSin.getText().toUpperCase();
		if (!(sin.equals("") || sin.equals("%")))
		{
			if (!sin.endsWith("%"))
				sin += "%";
			pstmt.setString(index++, sin);
		}

		//  => Gender
		if (pickGender.getSelectedItem() != null && ((KeyNamePair)pickGender.getSelectedItem()).getKey() > 0)
		{
			String gender = ((KeyNamePair)pickGender.getSelectedItem()).getKey() == 1 ? "M" : "F";
			pstmt.setString(index++, gender);
		}

		//  => Phone
//		String phone = fieldPhone.getText().toUpperCase();
//		if (!(phone.equals("") || phone.equals("%")))
//		{
//			if (!phone.endsWith("%"))
//				phone += "%";
//			pstmt.setString(index++, phone);
//		}

	}   //  setParameters

	
	/**************************************************************************
	 *  Action Listner
	 *
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		if (e.getActionCommand().equals("OccupationGroupChange"))
		{
			int P_OccupationGroup_ID = -1;
			KeyNamePair kn = (KeyNamePair) pickOccupationGroup.getItemAt(pickOccupationGroup.getSelectedIndex());
			P_OccupationGroup_ID = Integer.parseInt(kn.getID());
			fillJobTitle(P_OccupationGroup_ID);
		}
		
		//
		super.actionPerformed(e);
	}   //  actionPerformed


	/**
	 *	Has History
	 *
	 * @return true (has history)
	 */
	boolean hasHistory()
	{
		return false;
	}	//	hasHistory

	/**
	 *	Zoom
	 */
	void zoom()
	{
		Integer P_Employee_ID = getSelectedRowKey();
		if (P_Employee_ID == null)
			return;
		MQuery query = new MQuery("P_Employee");
		query.addRestriction("P_Employee_ID", MQuery.EQUAL, P_Employee_ID);
		zoom (1000304, query);		//	HARDCODED
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
	 *	Save Selection Settings for PriceList
	 */
	void saveSelectionDetail()
	{
		//  publish for Callout to read
		Integer ID = getSelectedRowKey();
		Env.setContext(Env.getCtx(), Env.WINDOW_INFO, Env.TAB_INFO, "P_Employee_ID", ID == null ? "0" : ID.toString());
		//
	}	//	saveSelectionDetail


}	//	InfoEmployee
