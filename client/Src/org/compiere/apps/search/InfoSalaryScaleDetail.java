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
import java.math.*;
import java.sql.*;

import org.compiere.apps.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.swing.*;
import org.compiere.util.*;


import java.util.logging.*;

import solstice.custom.DictionaryEntry;
import solstice.process.PgiUtil;
import solstice.model.P_Salary_Scale;


/**
 *	Search Salary Scale  and return selection
 *
 *  @author     Progestion Informatique
 *  @version    $Id: InfoSalaryScaleDetail.java,v 1.4 2007/08/25 19:46:20 marmor01 Exp $
 */
public final class InfoSalaryScaleDetail extends Info implements ActionListener
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
	public InfoSalaryScaleDetail(Frame frame, boolean modal, int WindowNo, int P_Salary_Scale_ID, int P_Occupation_Group_ID,
		String value, boolean multiSelection, String whereClause)
	{
		super (frame, modal, WindowNo, "p", "P_Salary_Scale_Detail_ID", multiSelection, whereClause);
		setTitle(Msg.getMsg(Env.getCtx(), "InfoSalaryScaleDetail"));
		//
		
		statInit();
		initInfo (value, WindowNo, P_Salary_Scale_ID, P_Occupation_Group_ID );
//		m_C_BPartner_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "C_BPartner_ID");

		System.out.println(" InfoSalaryScaleDetail " + P_Salary_Scale_ID );
		executeQuery();

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
	}	//	InfoSalaryScale

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (InfoSalaryScaleDetail.class);

    private static Language language = Env.getLanguage(Env.getCtx());

	private static final String s_From =
		"P_Salary_Scale_Detail p, P_Salary_Scale pr "
		+ " LEFT OUTER JOIN P_Occupation_Group og ON (pr.P_Occupation_Group_ID=og.P_Occupation_Group_ID)"
		+ " LEFT OUTER JOIN P_Collective_Labour_Agr cl ON (cl.P_Collective_Labour_Agr_ID=pr.P_Collective_Labour_Agr_ID)"
		+ " LEFT OUTER JOIN P_Job_Title jt ON (pr.P_Job_Title_ID=jt.P_Job_Title_ID)"
		+ " LEFT OUTER JOIN P_Salary_Class sc ON (pr.P_Salary_Class_ID=sc.P_Salary_Class_ID)";

	private static final String s_FromTrl =
		"P_Salary_Scale_Detail p, P_Salary_Scale pr "
		+ " LEFT OUTER JOIN P_Occupation_Group og ON (pr.P_Occupation_Group_ID=og.P_Occupation_Group_ID)"
		+ " LEFT OUTER JOIN P_Occupation_Group_Trl  ON (P_Occupation_Group_Trl.P_Occupation_Group_ID=og.P_Occupation_Group_ID and P_Occupation_Group_Trl.AD_Language = '" + language.getLocale() + "')"
		+ " LEFT OUTER JOIN P_Collective_Labour_Agr cl ON (cl.P_Collective_Labour_Agr_ID=pr.P_Collective_Labour_Agr_ID)"
		+ " LEFT OUTER JOIN P_Collective_Labour_Agr_Trl ON (P_Collective_Labour_Agr_Trl.P_Collective_Labour_Agr_ID=pr.P_Collective_Labour_Agr_ID and P_Collective_Labour_Agr_Trl.AD_Language = '" + language.getLocale()+ "')"
		+ " LEFT OUTER JOIN P_Job_Title jt ON (pr.P_Job_Title_ID=jt.P_Job_Title_ID)"
		+ " LEFT OUTER JOIN P_Job_Title_Trl ON (P_Job_Title_Trl.P_Job_Title_ID=jt.P_Job_Title_ID and P_Job_Title_Trl.AD_Language = '" + language.getLocale()+ "')"
		+ " LEFT OUTER JOIN P_Salary_Class sc ON (pr.P_Salary_Class_ID=sc.P_Salary_Class_ID)"
		+ " LEFT OUTER JOIN P_Salary_Class_Trl  ON (pr.P_Salary_Class_ID=P_Salary_Class_Trl.P_Salary_Class_ID and P_Salary_Class_Trl.AD_Language = '" + language.getLocale()+ "')";

	/**  Array of Column Info    */
	private static final Info_Column[] s_salary_scaleLayout = {
		new Info_Column(" ", "p.P_Salary_Scale_Detail_ID", IDColumn.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Value"), "og.Value", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Occupation_Group_ID"), "og.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Job_Title_ID"), "jt.Value + '-' + jt.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "ScaleDate"), "pr.ScaleDate", Timestamp.class, true, true, null),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Salary_Class_ID"), "sc.Value + '-' + sc.Name",  String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Step"), "p.Step",  String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Hourly_Rate"), "Hourly_Rate", BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Annual_Salary"), "Annual_Salary", BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Salary_Limit_Min"), "Salary_Limit_Min", BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Salary_Limit_Max"), "Salary_Limit_Max", BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Remuneration_Method"), new PgiUtil.ReferencedQueryField("P_Remuneration_Method", "pr", "Remuneration_Method").getSQLColumn(), String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Collective_Labour_Agr_ID"), "cl.Value + '-' + cl.Name", String.class)
//		new Info_Column(Msg.translate(Env.getCtx(), "P_Collective_Labour_Agr_ID"), new PgiUtil.QueryField("P_Collective_Labour_Agr", "cl", "Value + '-' + cl.Name").getSQLColumn(), String.class)
	};

	private static final Info_Column[] s_salary_scaleLayout_Trl = {
		new Info_Column(" ", "p.P_Salary_Scale_Detail_ID", IDColumn.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Value"), "og.Value", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Occupation_Group_ID"), "P_Occupation_Group_Trl.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Job_Title_ID"), "jt.Value + '-' + P_Job_Title_Trl.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "ScaleDate"), "pr.ScaleDate", Timestamp.class, true, true, null),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Salary_Class_ID"), "sc.Value + '-' + P_Salary_Class_Trl.Name",  String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Step"), "p.Step",  String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Hourly_Rate"), "Hourly_Rate", BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Annual_Salary"), "Annual_Salary", BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Salary_Limit_Min"), "Salary_Limit_Min", BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Salary_Limit_Max"), "Salary_Limit_Max", BigDecimal.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Remuneration_Method"), new PgiUtil.ReferencedQueryField("P_Remuneration_Method", "pr", "Remuneration_Method").getSQLColumn(), String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Collective_Labour_Agr_ID"), "cl.Value + '-' + P_Collective_Labour_Agr_Trl.Name", String.class)
//		new Info_Column(Msg.translate(Env.getCtx(), "P_Collective_Labour_Agr_ID"), new PgiUtil.QueryField("P_Collective_Labour_Agr", "cl", "Value + '-' + cl.Name").getSQLColumn(), String.class)
	};


	/**
	 *	Get Layout
	 *	@return array of Column_Info
	 */
	protected Info_Column[] getInfoColumns()
	{
	    if(language.isBaseLanguage())
	    	return s_salary_scaleLayout;
	    else
	    	return s_salary_scaleLayout_Trl;
	}	//	getInfoColumns

	
	//
	private CLabel labelValue = new CLabel();
	private CTextField fieldValue = new CTextField(10);
//	private CLabel labelName = new CLabel();
//	private VComboBox fieldName = new VComboBox();
	private CLabel labelScaleDate = new CLabel();
	private VDate fieldScaleDate = new VDate();
	private CLabel labelSalaryClass = new CLabel();
//	private CTextField fieldSalaryClass = new CTextField(10);
	private VComboBox pickClass = new VComboBox ();
	private CLabel labelCollective_Labour = new CLabel();
	private VComboBox pickCollective_Labour = new VComboBox();
	private CLabel labelRemuneration_Method = new CLabel();
	private VComboBox  fieldRemuneration_Method = new VComboBox();;
	private CLabel labelStep = new CLabel();
	private CTextField fieldStep = new CTextField(10);
	
	private CLabel labelOccupationGroup = new CLabel();
	private VComboBox pickOccupationGroup = new VComboBox();
	private CLabel labelJobTitle = new CLabel();
	private VComboBox pickPost = new VComboBox();
	

	/**	Search Button				*/
//	private CButton		m_InfoPAttributeButton = new CButton(Env.getImageIcon("PAttribute16.gif"));
	/** Instance Button				*/
//	private CButton		m_PAttributeButton = null;
//	private int			m_M_AttributeSetInstance_ID = -1;

//	private String		m_pAttributeWhere = null;
//	private int			m_C_BPartner_ID = 0;

	/**
	 *	Static Setup - add fields to parameterPanel
	 */
	private void statInit()
	{
		labelValue.setText(Msg.translate(Env.getCtx(), "Value"));
		fieldValue.setBackground(CompierePLAF.getInfoBackground());
		fieldValue.addActionListener(this);

		labelOccupationGroup.setText(Msg.translate(Env.getCtx(), "P_Occupation_Group_ID"));
		pickOccupationGroup.setBackground(CompierePLAF.getInfoBackground());
		
		labelJobTitle.setText(Msg.translate(Env.getCtx(), "P_Job_Title_ID"));
		pickPost.setBackground(CompierePLAF.getInfoBackground());

//		labelName.setText(Msg.getMsg(Env.getCtx(), "Name"));
//		fieldName.setBackground(CompierePLAF.getInfoBackground());
//		fieldName.addActionListener(this);

		labelScaleDate.setText(Msg.translate(Env.getCtx(), "ScaleDate"));
		fieldScaleDate.setBackground(CompierePLAF.getInfoBackground());
		fieldScaleDate.addActionListener(this);

		labelSalaryClass.setText(Msg.translate(Env.getCtx(), "P_Salary_Class_ID"));
//		fieldSalaryClass.setBackground(CompierePLAF.getInfoBackground());
//		fieldSalaryClass.addActionListener(this);
		this.pickClass.setBackground( CompierePLAF.getInfoBackground() );
		
		labelRemuneration_Method.setText(Msg.translate(Env.getCtx(), "Remuneration_Method"));
		fieldRemuneration_Method.setBackground(CompierePLAF.getInfoBackground());
		
		labelCollective_Labour.setText(Msg.translate(Env.getCtx(), "P_Collective_Labour_Agr_ID"));
		pickCollective_Labour.setBackground(CompierePLAF.getInfoBackground());

		labelStep.setText(Msg.translate(Env.getCtx(), "Step"));
		fieldStep.setBackground(CompierePLAF.getInfoBackground());
		fieldStep.addActionListener(this);

		
//		m_InfoPAttributeButton.setMargin(new Insets(2,2,2,2));
//		m_InfoPAttributeButton.setToolTipText(Msg.getMsg(Env.getCtx(), "InfoPAttribute"));
//		m_InfoPAttributeButton.addActionListener(this);

		//	Line 1
		parameterPanel.setLayout(new ALayout());
		parameterPanel.add(labelValue, new ALayoutConstraint(0,0));
		parameterPanel.add(fieldValue, null);
		parameterPanel.add(labelScaleDate, null);
		parameterPanel.add(fieldScaleDate, null);
		parameterPanel.add(labelRemuneration_Method, null);
		parameterPanel.add(fieldRemuneration_Method, null);
		//	Line 2
		parameterPanel.add(labelOccupationGroup, new ALayoutConstraint(1,0));
		parameterPanel.add(pickOccupationGroup, null);
//		parameterPanel.add(labelName, new ALayoutConstraint(1,0));
//		parameterPanel.add(fieldName, null);
		parameterPanel.add(labelSalaryClass, null);
		parameterPanel.add(this.pickClass, null);
//		parameterPanel.add(fieldSalaryClass, null);
		parameterPanel.add(labelCollective_Labour, null);
		parameterPanel.add(pickCollective_Labour, null);
//		parameterPanel.add(labelStep, null);
//		parameterPanel.add(fieldStep, null);
		// Line 3
		parameterPanel.add(labelJobTitle, new ALayoutConstraint(2,0));
		parameterPanel.add(pickPost, null);
		
		
	}	//	statInit

	/**
	 *	Dynamic Init
	 *
	 * @param value value
	 * @param M_Warehouse_ID warehouse
	 * @param M_PriceList_ID price list
	 */
	private void initInfo ( String value, int WindowNo, int P_Salary_Scale_ID, int P_Occupation_Group_ID)
	{
		//	Set P_Salary_Scale_ID
		if (P_Salary_Scale_ID == 0)
			P_Salary_Scale_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Salary_Scale_ID");

		if (P_Occupation_Group_ID == 0)
			P_Occupation_Group_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Occupation_Group_ID");
		
		int salaryClassId = Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Salary_Class_ID");
		int collectiveLabourId = 0 ; // Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Collective_Labour_Agr_ID");
		//2023-08-15 pas de jobtitle pour krispy
		int jobTitleId = 0;
				//Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Job_Title_ID");
		String remuneration_methode = Env.getContext(Env.getCtx(), WindowNo, "Remuneration_Method");

		fillPicks( P_Salary_Scale_ID, P_Occupation_Group_ID, salaryClassId, collectiveLabourId, jobTitleId, remuneration_methode );
		
		System.out.println(" InfoSalaryScaleDetail : occup " + Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Occupation_Group_ID"));

		//	Create Grid
		StringBuffer where = new StringBuffer();
		where.append("p.IsActive='Y'");
		where.append( " AND p.P_Salary_Scale_ID=pr.P_Salary_Scale_ID AND pr.IsActive='Y'" );
		
		if (P_Salary_Scale_ID != 0)
			where.append(" AND p.P_Salary_Scale_ID=" + P_Salary_Scale_ID );
//		if (P_Occupation_Group_ID != 0)
//			where.append(" AND pr.P_Occupation_Group_ID=" + P_Occupation_Group_ID );
		//  dynamic Where Clause
//		if (p_whereClause != null && p_whereClause.length() > 0)
//			where.append(" AND ")   //  replace fully qalified name with alias
//				.append(Util.replace(p_whereClause, "M_Product.", "p."));
		//
	    if(language.isBaseLanguage())
	    	prepareTable(s_From,
	    			where.toString(),
				"og.Name, pr.ScaleDate, pr.P_Salary_Class_ID, pr.Remuneration_Method, cl.Name, p.Step");
	    else
	    	prepareTable(s_FromTrl,
	    			where.toString(),
				"P_Occupation_Group_Trl.Name, pr.ScaleDate, pr.P_Salary_Class_ID, pr.Remuneration_Method, P_Collective_Labour_Agr_Trl.Name, p.Step");
	    	
		
		//  Set Value
		if (value == null)
			value = "%";
		if (!value.endsWith("%"))
			value += "%";
		
		fieldValue.setText(value);

		// Défini les actions
		pickOccupationGroup.setActionCommand("OccupationGroupChange");
		pickOccupationGroup.addActionListener(this);
		pickCollective_Labour.addActionListener(this);
		fieldRemuneration_Method.addActionListener(this);
	}	//	initInfo

	

	/**
	 *	Fill Picks with values
	 *
	 * @param M_PriceList_ID price list
	 */
	private void fillPicks ( int P_Salary_Scale_ID, int P_Occupation_Group_ID, int salaryClassId, 
								int collectiveLabourId, int jobTitleId, String remuneration_methode )
	{
		InfoSalaryScale.fillRemunerationMethod( this.fieldRemuneration_Method,  remuneration_methode );
		InfoSalaryScale.getComboBoxSalaryClass( this.pickClass, language, salaryClassId );
	    
	    P_Salary_Scale SalaryScale = P_Salary_Scale.get(Env.getCtx(), P_Salary_Scale_ID, null);
	    fieldScaleDate.setValue( SalaryScale.getScaleDate());
	    
	    InfoSalaryScale.getComboBoxOccupationGroup( this.pickOccupationGroup, language, P_Occupation_Group_ID );
		
		InfoSalaryScale.getComboBoxCollectiveLabour( this.pickCollective_Labour, language, collectiveLabourId);
		
		// fill job title
		InfoSalaryScale.getComboBoxPost( this.pickPost, language, P_Occupation_Group_ID,  jobTitleId);
		
	}	//	fillPicks

	/**
	 *	Set P_Salary_Scale
	 *
	 * 	@param P_Salary_Scale_ID
	 */
	
	
	/**************************************************************************
	 *	Construct SQL Where Clause and define parameters
	 *  (setParameters needs to set parameters)
	 *  Includes first AND
	 *  @return SQL WHERE clause
	 */
	String getSQLWhere()
	{
		StringBuffer where = new StringBuffer();

		int P_Occupation_Group_ID = 0;
		KeyNamePair pl = (KeyNamePair)pickOccupationGroup.getSelectedItem();
		if (pl != null)
			P_Occupation_Group_ID = pl.getKey();
		if (P_Occupation_Group_ID != 0)
			where.append(" AND pr.P_Occupation_Group_ID=?");
		
		int P_Job_Title_ID = 0;
		KeyNamePair p2 = (KeyNamePair)pickPost.getSelectedItem();
		if (p2 != null)
			P_Job_Title_ID = p2.getKey();
		if (P_Job_Title_ID != 0)
//			where.append(" AND pr.P_Job_Title_ID=?");
			where.append(" AND ( pr.P_Job_Title_ID=?  OR exists( select 1 from P_Salary_Scale_Job_Title e Where e.P_Job_Title_ID=? and e.P_Salary_Scale_ID = pr.P_Salary_Scale_ID ))");

		//	Optional 
		int P_Collective_Labour_Agr_ID = 0;
		KeyNamePair pl3 = (KeyNamePair)pickCollective_Labour.getSelectedItem();
		if (pl3 != null)
			P_Collective_Labour_Agr_ID = pl3.getKey();
		if (P_Collective_Labour_Agr_ID != 0)
			where.append(" AND pr.P_Collective_Labour_Agr_ID=?");

		
		//  => Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
			where.append(" AND UPPER(og.Value) LIKE ?");

		//  => Name
//		String name = fieldName.getText().toUpperCase();
//		if (!(name.equals("") || name.equals("%")))
//			where.append(" AND UPPER(p.Name) LIKE ?");

		//  => EffectIn
		if(this.fieldScaleDate.getTimestamp() != null)
			where.append(" AND ScaleDate LIKE ?");

		//  => Class
		KeyNamePair k = (KeyNamePair)this.pickClass.getSelectedItem();
		if(k != null && k.getKey() > 0)
		{
		    where.append( " and pr.P_Salary_Class_ID = ?" );
		}
		
		if(this.fieldRemuneration_Method.getSelectedIndex() > 0)
		{
		    where.append(" and pr.Remuneration_Method = ?");
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

		//  => Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
		{
			if (!value.endsWith("%"))
				value += "%";
			pstmt.setString(index++, value);
		}

		//  => Occupation_Group
		int P_Occupation_Group_ID = 0;
		KeyNamePair pl = (KeyNamePair)pickOccupationGroup.getSelectedItem();
		if (pl != null)
			P_Occupation_Group_ID = pl.getKey();
		if (P_Occupation_Group_ID != 0)
		{
			pstmt.setInt(index++, P_Occupation_Group_ID);
		}
		
		//	=> Job_Title
		int P_Job_Title_ID = 0;
		KeyNamePair p2 = (KeyNamePair)pickPost.getSelectedItem();
		if (p2 != null)
			P_Job_Title_ID = p2.getKey();
		if (P_Job_Title_ID != 0)
		{
			pstmt.setInt(index++, P_Job_Title_ID);
			pstmt.setInt(index++, P_Job_Title_ID);
		}

		//  => P_Collective_Labour_Agr
		int P_Collective_Labour_Agr_ID = 0;
		KeyNamePair pl3 = (KeyNamePair)pickCollective_Labour.getSelectedItem();
		if (pl3 != null)
			P_Collective_Labour_Agr_ID = pl3.getKey();
		if (P_Collective_Labour_Agr_ID != 0)
		{
			pstmt.setInt(index++, P_Collective_Labour_Agr_ID);
		}
		
		//  => EffectIn
		if(this.fieldScaleDate.getTimestamp() != null)
			pstmt.setTimestamp(index++, this.fieldScaleDate.getTimestamp());

		//  => Class
		KeyNamePair k = (KeyNamePair)this.pickClass.getSelectedItem();
		if(k != null && k.getKey() > 0)
		{
			pstmt.setString(index++, String.valueOf(k.getKey()) );
		}

		//  => Remuneration_Method
		if(this.fieldRemuneration_Method.getSelectedIndex() > 0)
		{
			pstmt.setString(index++, (String)((DictionaryEntry)this.fieldRemuneration_Method.getSelectedItem()).getKey());
		}
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
			int P_Occupation_Group_ID = -1;
			KeyNamePair kn = (KeyNamePair) pickOccupationGroup.getItemAt(pickOccupationGroup.getSelectedIndex());
			P_Occupation_Group_ID = Integer.parseInt(kn.getID());
			InfoSalaryScale.getComboBoxPost( this.pickPost, language, P_Occupation_Group_ID,  0);
		}
		
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
		Integer P_Salary_Scale_ID = getSelectedRowKey();
		if (P_Salary_Scale_ID == null)
			return;
		MQuery query = new MQuery("P_Salary_Scale");
		query.addRestriction("P_Salary_Scale_ID", MQuery.EQUAL, P_Salary_Scale_ID);
		zoom (2000020, query);		//	HARDCODED
		

	}	//	zoom

	/**
	 *	Has Zoom
	 *  @return (has zoom)
	 */
	boolean hasZoom()
	{
		return false;
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
		Integer ID = getSelectedRowKey();
		Env.setContext(Env.getCtx(), Env.WINDOW_INFO, Env.TAB_INFO, "P_Salary_Scale_Detail_ID", ID == null ? "0" : ID.toString());
	}	//	saveSelectionDetail

	/**
	 *  Get Product Layout
	 *
	 * @return array of Column_Info
	 */
	private Info_Column[] getSalaryScaleLayout()
	{
		return s_salary_scaleLayout;
	}   //  getProductLayout


	
}	//	InfoSalaryScaleDetail

	

	