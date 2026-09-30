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

import solstice.custom.DictionaryEntry;
import solstice.process.PgiUtil;


/**
 *	Search Salary Scale  and return selection
 *
 *  @author     Progestion Informatique
 *  @version    $Id: InfoSalaryScale.java,v 1.4 2007/08/25 19:46:20 marmor01 Exp $
 */
public final class InfoSalaryScale extends Info implements ActionListener
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
	public InfoSalaryScale(Frame frame, boolean modal, int WindowNo, int P_Salary_Scale_ID, int P_Occupation_Group_ID,
		String value, boolean multiSelection, String whereClause)
	{
		super (frame, modal, WindowNo, "p", "P_Salary_Scale_ID", multiSelection, whereClause);
		setTitle(Msg.getMsg(Env.getCtx(), "InfoSalaryScale"));
		//
		
		statInit();
		initInfo (value, WindowNo, P_Salary_Scale_ID, P_Occupation_Group_ID );
		
		executeQuery();
		
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
	}	//	InfoSalaryScale

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (InfoSalaryScale.class);

    private static Language language = Env.getLanguage(Env.getCtx());

	private static final String s_From =
		"P_Salary_Scale p "
		+ " LEFT OUTER JOIN P_Occupation_Group og ON (p.P_Occupation_Group_ID=og.P_Occupation_Group_ID)"
		+ " LEFT OUTER JOIN P_Collective_Labour_Agr cl ON (cl.P_Collective_Labour_Agr_ID=p.P_Collective_Labour_Agr_ID)"
		+ " LEFT OUTER JOIN P_Post pt ON (p.P_Post_ID=pt.P_Post_ID)"
		+ " LEFT OUTER JOIN P_Job_Title jt ON (p.P_Job_Title_ID=jt.P_Job_Title_ID)"
		+ " LEFT OUTER JOIN P_Salary_Class sc ON (p.P_Salary_Class_ID=sc.P_Salary_Class_ID)";

	private static final String s_FromTrl =
		"P_Salary_Scale p "
		+ " LEFT OUTER JOIN P_Occupation_Group og ON (p.P_Occupation_Group_ID=og.P_Occupation_Group_ID)"
		+ " LEFT OUTER JOIN P_Collective_Labour_Agr cl ON (cl.P_Collective_Labour_Agr_ID=p.P_Collective_Labour_Agr_ID)"
		+ " LEFT OUTER JOIN P_Post pt ON (p.P_Post_ID=pt.P_Post_ID)"
		+ " LEFT OUTER JOIN P_Job_Title jt ON (p.P_Job_Title_ID=jt.P_Job_Title_ID)"
		+ " LEFT OUTER JOIN P_Salary_Class sc ON (p.P_Salary_Class_ID=sc.P_Salary_Class_ID)"
		+ " LEFT OUTER JOIN P_Occupation_Group_Trl  ON (P_Occupation_Group_Trl.P_Occupation_Group_ID=og.P_Occupation_Group_ID and P_Occupation_Group_Trl.AD_Language = '" + language.getLocale() + "')"
		+ " LEFT OUTER JOIN P_Collective_Labour_Agr_Trl ON (P_Collective_Labour_Agr_Trl.P_Collective_Labour_Agr_ID=p.P_Collective_Labour_Agr_ID and P_Collective_Labour_Agr_Trl.AD_Language = '" + language.getLocale()+ "')"
		+ " LEFT OUTER JOIN P_Job_Title_Trl ON (P_Job_Title_Trl.P_Job_Title_ID=jt.P_Job_Title_ID and P_Job_Title_Trl.AD_Language = '" + language.getLocale()+ "')"
		+ " LEFT OUTER JOIN P_Post_Trl ON (P_Post_Trl.P_Post_ID=pt.P_Post_ID and P_Post_Trl.AD_Language = '" + language.getLocale()+ "')"
		+ " LEFT OUTER JOIN P_Salary_Class_Trl  ON (p.P_Salary_Class_ID=P_Salary_Class_Trl.P_Salary_Class_ID and P_Salary_Class_Trl.AD_Language = '" + language.getLocale()+ "')";

	/**  Array of Column Info    */
	private static final Info_Column[] s_salary_scaleLayout = {
		new Info_Column(" ", "p.P_Salary_Scale_ID", IDColumn.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Value"), "og.Value", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Occupation_Group_ID"), "og.Name", String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Post_ID"), "pt.Value + '-' + pt.Name" , String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Job_Title_ID"), "jt.Value + '-' + jt.Name" , String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "ScaleDate"), "p.ScaleDate", Timestamp.class, true, true, null),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Salary_Class_ID"), "sc.Value + '-' + " + "sc.Name",  String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "Remuneration_Method"), new PgiUtil.ReferencedQueryField("P_Remuneration_Method", "p", "Remuneration_Method").getSQLColumn(), String.class),
		new Info_Column(Msg.translate(Env.getCtx(), "P_Collective_Labour_Agr_ID"),  "cl.Value + '-' +cl.Name", String.class)
	};

	private static final Info_Column[] s_salary_scaleLayoutTrl = {
			new Info_Column(" ", "p.P_Salary_Scale_ID", IDColumn.class),
			new Info_Column(Msg.translate(Env.getCtx(), "Value"), "og.Value", String.class),
			new Info_Column(Msg.translate(Env.getCtx(), "P_Occupation_Group_ID"), "P_Occupation_Group_Trl.Name", String.class),
			new Info_Column(Msg.translate(Env.getCtx(), "P_Post_ID"), "pt.Value + '-' + P_Post_Trl.Name" , String.class),
			new Info_Column(Msg.translate(Env.getCtx(), "P_Job_Title_ID"), "jt.Value + '-' + P_Job_Title_Trl.Name" , String.class),
			new Info_Column(Msg.translate(Env.getCtx(), "ScaleDate"), "p.ScaleDate", Timestamp.class, true, true, null),
			new Info_Column(Msg.translate(Env.getCtx(), "P_Salary_Class_ID"), "sc.Value + '-' + " + "P_Salary_Class_Trl.Name",  String.class),
			new Info_Column(Msg.translate(Env.getCtx(), "Remuneration_Method"), new PgiUtil.ReferencedQueryField("P_Remuneration_Method", "p", "Remuneration_Method").getSQLColumn(), String.class),
			new Info_Column(Msg.translate(Env.getCtx(), "P_Collective_Labour_Agr_ID"),  "cl.Value + '-' +P_Collective_Labour_Agr_Trl.Name", String.class)
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
	    	return s_salary_scaleLayoutTrl;
	}	//	getInfoColumns


	//
	private CLabel labelValue = new CLabel();
	private CTextField fieldValue = new CTextField(10);
//	private CLabel labelName = new CLabel();
//	private VComboBox fieldName = new VComboBox();
	private CLabel labelScaleDate = new CLabel();
	private VDate fieldScaleDate = new VDate();
	private CLabel labelClass = new CLabel();
	//private CTextField fieldClass = new CTextField(10);
	private VComboBox pickClass = new VComboBox(); //---------------- pour aller avec le P_SALARY_CLASS_ID
	private CLabel labelCollective_Labour = new CLabel();
	private VComboBox pickCollective_Labour = new VComboBox();
	private CLabel labelRemuneration_Method = new CLabel();
	private VComboBox  fieldRemuneration_Method = new VComboBox();;
	private CLabel labelStep = new CLabel();
	private CTextField fieldStep = new CTextField(10);
	
	private CLabel labelOccupationGroup = new CLabel();
	private VComboBox pickOccupationGroup = new VComboBox();
	private CLabel labelJobTitle = new CLabel();
	private VComboBox pickJobTitle = new VComboBox();
	private VComboBox pickPost = new VComboBox();
	private CLabel labelPost = new CLabel();
	//ajout du check box actif
	private CLabel labelIsActive = new CLabel();
	private VCheckBox checkIsActive = new VCheckBox();


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
		
//		labelJobTitle.setText(Msg.translate(Env.getCtx(), "P_Job_Title_ID"));
//		pickJobTitle.setBackground(CompierePLAF.getInfoBackground());

		labelPost.setText(Msg.translate(Env.getCtx(), "P_Post_ID"));
		pickPost.setBackground(CompierePLAF.getInfoBackground());

		
		labelIsActive.setText(Msg.translate(Env.getCtx(), "IsActive"));
		checkIsActive.setBackground(CompierePLAF.getInfoBackground());
		
		checkIsActive.setSelected(true);

//		labelName.setText(Msg.getMsg(Env.getCtx(), "Name"));
//		fieldName.setBackground(CompierePLAF.getInfoBackground());
//		fieldName.addActionListener(this);

		labelScaleDate.setText(Msg.translate(Env.getCtx(), "ScaleDate"));
		fieldScaleDate.setBackground(CompierePLAF.getInfoBackground());
		fieldScaleDate.addActionListener(this);

		labelClass.setText(Msg.translate(Env.getCtx(), "P_Salary_Class_ID"));
//		fieldClass.setBackground(CompierePLAF.getInfoBackground());
//		fieldClass.addActionListener(this);
		
		labelRemuneration_Method.setText(Msg.translate(Env.getCtx(), "Remuneration_Method"));
		fieldRemuneration_Method.setBackground(CompierePLAF.getInfoBackground());
		
		labelCollective_Labour.setText(Msg.translate(Env.getCtx(), "P_Collective_Labour_Agr_ID"));

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
		parameterPanel.add(labelClass, null);
//		parameterPanel.add(fieldClass, null);
		parameterPanel.add(this.pickClass, null);
		parameterPanel.add(labelCollective_Labour, null);
		parameterPanel.add(pickCollective_Labour, null);
//		parameterPanel.add(labelStep, null);
//		parameterPanel.add(fieldStep, null);
		// Line 3
//		parameterPanel.add(labelJobTitle, new ALayoutConstraint(2,0));
//		parameterPanel.add(pickJobTitle, null);

		parameterPanel.add(labelPost, new ALayoutConstraint(2,0));
		parameterPanel.add(pickPost, null);

		parameterPanel.add(labelIsActive, null);
		parameterPanel.add(checkIsActive, null);
		
	}	//	statInit

	/**
	 *	Dynamic Init
	 *
	 * @param value value
	 * @param M_Warehouse_ID warehouse
	 * @param M_PriceList_ID price list
	 */
	private void initInfo (String value, int WindowNo, int P_Salary_Scale_ID, int P_Occupation_Group_ID)
	{
		//	Pick init
		
		
		//	Set P_Salary_Scale_ID
		if (P_Salary_Scale_ID == 0)
			P_Salary_Scale_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Salary_Scale_ID");

		if (P_Occupation_Group_ID == 0)
			P_Occupation_Group_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Occupation_Group_ID");


		int PostId = Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Post_ID");
		int jobTitleId = Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Job_Title_ID");
        int salaryClassId = Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Salary_Class_ID");
        
        int collectiveLabourId = 0 ; //Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Collective_Labour_Agr_ID");
		
		String remuneration_methode = Env.getContext(Env.getCtx(), WindowNo, "Remuneration_Method");

		fillPicks( P_Salary_Scale_ID, P_Occupation_Group_ID, PostId, jobTitleId, salaryClassId, collectiveLabourId, remuneration_methode );
		
		System.out.println(" InfoSalaryScale : occup " + Env.getContextAsInt(Env.getCtx(), WindowNo, "P_Occupation_Group_ID"));

		//	Create Grid
		StringBuffer where = new StringBuffer();
		//where.append("p.IsActive='Y'");
		where.append(" 1 = 1 ");
		
	    if(language.isBaseLanguage())
	    	prepareTable(s_From,
	    			where.toString(),
				"og.Name, p.ScaleDate, p.P_Salary_Class_ID, p.Remuneration_Method, cl.Name");
	    else
	    	prepareTable(s_FromTrl,
	    			where.toString(),
				"og.Name, p.ScaleDate, p.P_Salary_Class_ID, p.Remuneration_Method, cl.Name");

		//
		pickOccupationGroup.setActionCommand("OccupationGroupChange");
		pickOccupationGroup.addActionListener(this);
		pickCollective_Labour.addActionListener(this);
		this.fieldRemuneration_Method.addActionListener(this);
	}	//	initInfo

	/**
	 *	Fill Picks with values
	 *
	 * @param M_PriceList_ID price list
	 */
	private void fillPicks ( int P_Salary_Scale_ID, int P_Occupation_Group_ID, int PostId, int jobTitleId, 
								int salaryClassId, int collectiveLabourId, String remuneration_methode )
	{
	    getComboBoxOccupationGroup( this.pickOccupationGroup, language, P_Occupation_Group_ID );
		
		getComboBoxCollectiveLabour( this.pickCollective_Labour, language, collectiveLabourId);
		
		// Job title
		getComboBoxPost( this.pickPost, language, P_Occupation_Group_ID,  PostId);
		getComboBoxJobTitle( this.pickJobTitle, language, P_Occupation_Group_ID,  jobTitleId);
		getComboBoxSalaryClass( this.pickClass, language, salaryClassId );
		fillRemunerationMethod( this.fieldRemuneration_Method, remuneration_methode );
		
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
			where.append(" AND p.P_Occupation_Group_ID=?");
		
		// => JobTitle
		int P_Job_Title_ID = 0;
		KeyNamePair p2 = (KeyNamePair)pickJobTitle.getSelectedItem();
		if (p2 != null)
			P_Job_Title_ID = p2.getKey();
		if (P_Job_Title_ID != 0)
			where.append(" AND ( p.P_Job_Title_ID=?  OR exists( select 1 from P_Salary_Scale_Job_Title e Where e.P_Job_Title_ID=? and e.P_Salary_Scale_ID = p.P_Salary_Scale_ID ))");

		// => Post
		int P_Post_ID = 0;
		KeyNamePair p5 = (KeyNamePair)pickPost.getSelectedItem();
		if (p2 != null)
			P_Post_ID = p5.getKey();
		if (P_Post_ID != 0)
			where.append(" AND ( p.P_Post_ID=?  )");

		
		//	Optional 
		int P_Collective_Labour_Agr_ID = 0;
		KeyNamePair pl3 = (KeyNamePair)pickCollective_Labour.getSelectedItem();
		if (pl3 != null)
			P_Collective_Labour_Agr_ID = pl3.getKey();
		if (P_Collective_Labour_Agr_ID != 0)
			where.append(" AND p.P_Collective_Labour_Agr_ID=?");

		
		//  => Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
			where.append(" AND UPPER(og.Value) LIKE ?");

		//  => EffectIn
		if(this.fieldScaleDate.getTimestamp() != null)
			where.append(" AND ScaleDate LIKE ?");

		KeyNamePair k = (KeyNamePair)this.pickClass.getSelectedItem();
		if(k != null && k.getKey() > 0)
		{
		    where.append( " and p.P_Salary_Class_ID = ?" );
		}
		
		if(this.fieldRemuneration_Method.getSelectedIndex() > 0)
		{
		    where.append(" and p.Remuneration_Method like ?");
		}

		//IsActive
		if(checkIsActive.isSelected() == true)
		{
			where.append(" AND p.IsActive = 'Y' ");
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
		
		//	=> JobTitle
		int P_Job_Title_ID = 0;
		KeyNamePair p2 = (KeyNamePair)pickJobTitle.getSelectedItem();
		if (p2 != null)
			P_Job_Title_ID = p2.getKey();
		if (P_Job_Title_ID != 0)
		{
			pstmt.setInt(index++, P_Job_Title_ID);
			pstmt.setInt(index++, P_Job_Title_ID);
		}

		//	=> Post
		int P_Post_ID = 0;
		KeyNamePair p5 = (KeyNamePair)pickPost.getSelectedItem();
		if (p5 != null)
			P_Post_ID = p5.getKey();
		if (P_Post_ID != 0)
		{
			pstmt.setInt(index++, P_Post_ID);
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
		
		//IsActive
		//if(checkIsActive.isSelected() == true)
		//{
		//	pstmt.setString(index++, "Y");
		//}

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
			getComboBoxJobTitle( this.pickJobTitle, language, P_Occupation_Group_ID,  0);

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
		Env.setContext(Env.getCtx(), Env.WINDOW_INFO, Env.TAB_INFO, "P_Salary_Scale_ID", ID == null ? "0" : ID.toString());
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
	
	public static void getComboBoxOccupationGroup( VComboBox pickOccupationGroup, Language language, int P_Occupation_Group_ID )
	{
		
		pickOccupationGroup.setBackground(CompierePLAF.getInfoBackground());

		String sql;
		
	    if (language.isBaseLanguage() )
	    {
		    sql = "SELECT P_Occupation_Group.P_Occupation_Group_ID, Value + ' - ' + Name as Name "
			+ " FROM P_Occupation_Group "
			+ " WHERE IsActive='Y' ";
		    
			sql = sql + " And P_Occupation_Group." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
			+ " ORDER BY P_Occupation_Group.Value + ' - ' + P_Occupation_Group.Name";

	    }
	    else
	    {
		    sql = "SELECT P_Occupation_Group.P_Occupation_Group_ID, P_Occupation_Group.Value + ' - ' + P_Occupation_Group_Trl.Name as Name "
				+ " FROM P_Occupation_Group, P_Occupation_Group_Trl "
				+ " WHERE P_Occupation_Group.IsActive='Y' And P_Occupation_Group.P_Occupation_Group_ID = P_Occupation_Group_Trl.P_Occupation_Group_ID "
		    	+ " AND P_Occupation_Group_Trl.AD_Language = '" + language.getLocale() + "'";
		    
			sql = sql + " And P_Occupation_Group." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
			+ " ORDER BY P_Occupation_Group.Value + ' - ' +P_Occupation_Group_Trl.Name";

 	
	    }
		
		try
		{
			pickOccupationGroup.addItem(new KeyNamePair (0, ""));
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			int index = 0;
			while (rs.next())
			{
				index ++;
				KeyNamePair kn = new KeyNamePair (rs.getInt(1), rs.getString(2));
				pickOccupationGroup.addItem(kn);
				if ( rs.getInt(1) == P_Occupation_Group_ID )
					pickOccupationGroup.setSelectedIndex( index );
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE,"InfoSalaryScale.fillPicks", e);
		}

//		return pickOccupationGroup;
	}
	
	   public static void getComboBoxSalaryClass( VComboBox pickClass, Language language, int P_Salary_Class_ID )
	    {
			pickClass.setBackground( CompierePLAF.getInfoBackground() );

			String sql;
			
		    if (language.isBaseLanguage() )
		    {
			    sql = "SELECT P_Salary_Class.P_Salary_Class_ID, Value + ' - ' + Name as Name "
				+ " FROM P_Salary_Class "
				+ " WHERE IsActive='Y' ";
			    
				sql = sql + " And P_Salary_Class." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
				+ " ORDER BY P_Salary_Class.Value + ' - ' + P_Salary_Class.Name";

		    }
		    else
		    {
			    sql = "SELECT P_Salary_Class.P_Salary_Class_ID, P_Salary_Class.Value + ' - ' + P_Salary_Class_Trl.Name as Name "
					+ " FROM P_Salary_Class, P_Salary_Class_Trl "
					+ " WHERE P_Salary_Class.IsActive='Y' And P_Salary_Class.P_Salary_Class_ID = P_Salary_Class_Trl.P_Salary_Class_ID "
			    	+ " AND P_Salary_Class_Trl.AD_Language = '" + language.getLocale() + "'";
			    
				sql = sql + " And P_Salary_Class." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
				+ " ORDER BY P_Salary_Class.Value + ' - ' +P_Salary_Class_Trl.Name";

	 	
		    }
		    
		    pickClass.addItem( new KeyNamePair ( 0, "" ) );
		    
		    try
		    {
		        int index = 0;
			    PreparedStatement stmt = DB.prepareStatement(sql, null);
		        ResultSet rs = stmt.executeQuery();
		        while(rs.next())
		        {
		            index++;
		            KeyNamePair k = new KeyNamePair(rs.getInt(1), rs.getString(2).trim() + " - " + rs.getString(3).trim());
		            pickClass.addItem(k);
		            if(rs.getInt(1) == P_Salary_Class_ID)
		                pickClass.setSelectedIndex(index);
		        }
		        rs.close();
		        stmt.close();
		    }
		    catch (SQLException ex)
		    {
		    	s_log.log(Level.SEVERE,"InfoSalaryScale.fillSalaryClass", ex);
		    }

//		    return pickClass;
	    }

		public static void getComboBoxPost( VComboBox pickPost, Language language, int P_Occupation_Group_ID, int P_Post_ID )
		{
			pickPost.setBackground(CompierePLAF.getInfoBackground());
			
			String sql;
			
		    if (language.isBaseLanguage() )
		    {
			    sql = "SELECT P_Post.P_Post_ID, Value + ' - ' + Name as Name "
				+ " FROM P_Post "
				+ " WHERE IsActive='Y' ";
			    
				sql = sql + " And P_Post." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
				+ " ORDER BY P_Post.Value + ' - ' + P_Post.Name";

		    }
		    else
		    {
			    sql = "SELECT P_Post.P_Post_ID, P_Post.Value + ' - ' + P_Post_Trl.Name as Name "
					+ " FROM P_Post, P_Post_Trl "
					+ " WHERE P_Post.IsActive='Y' And P_Post.P_Post_ID = P_Post_Trl.P_Post_ID "
			    	+ " AND P_Post_Trl.AD_Language = '" + language.getLocale() + "'";
			    
				sql = sql + " And P_Post." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
				+ " ORDER BY P_Post.Value + ' - ' +P_Post_Trl.Name";

	 	
		    }
			try
			{
			    int index = 0;
				pickPost.addItem(new KeyNamePair (0, ""));
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
				    index++;
					KeyNamePair kn = new KeyNamePair (rs.getInt(1), rs.getString(2));
					pickPost.addItem(kn);
					if(rs.getInt(1) == P_Post_ID)
					    pickPost.setSelectedIndex(index);
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				s_log.log(Level.SEVERE,"InfoSalaryScale.fillJobTitle", e);
			}

//			return pickJobTitle;
		}

	   
		public static void getComboBoxJobTitle( VComboBox pickJobTitle, Language language, int P_Occupation_Group_ID, int P_Job_Title_ID )
		{
			pickJobTitle.setBackground(CompierePLAF.getInfoBackground());
			
			String sql;
			
		    if (language.isBaseLanguage() )
		    {
			    sql = "SELECT P_Job_Title.P_Job_Title_ID, Value + ' - ' + Name as Name "
				+ " FROM P_Job_Title "
				+ " WHERE P_Occupation_Group_ID = " + P_Occupation_Group_ID
				+ " AND IsActive='Y' ";
			    
				sql = sql + " And P_Job_Title." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
				+ " ORDER BY P_Job_Title.Value + ' - ' + P_Job_Title.Name";

		    }
		    else
		    {
			    sql = "SELECT P_Job_Title.P_Job_Title_ID, P_Job_Title.Value + ' - ' + P_Job_Title_Trl.Name as Name "
					+ " FROM P_Job_Title, P_Job_Title_Trl "
					+ " WHERE P_Occupation_Group_ID = " + P_Occupation_Group_ID
					+ " AND P_Job_Title.IsActive='Y' And P_Job_Title.P_Job_Title_ID = P_Job_Title_Trl.P_Job_Title_ID "
			    	+ " AND P_Job_Title_Trl.AD_Language = '" + language.getLocale() + "'";
			    
				sql = sql + " And P_Job_Title." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
				+ " ORDER BY P_Job_Title.Value + ' - ' +P_Job_Title_Trl.Name";

	 	
		    }
			try
			{
			    int index = 0;
				pickJobTitle.addItem(new KeyNamePair (0, ""));
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
				    index++;
					KeyNamePair kn = new KeyNamePair (rs.getInt(1), rs.getString(2));
					pickJobTitle.addItem(kn);
//					if(rs.getInt(1) == P_Job_Title_ID)
//					    pickJobTitle.setSelectedIndex(index);
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				s_log.log(Level.SEVERE,"InfoSalaryScale.fillJobTitle", e);
			}

//			return pickJobTitle;
		}


		public static void getComboBoxCollectiveLabour( VComboBox pickCollective_Labour, Language language, int collectiveLabourId )
		{
			String sql;
			pickCollective_Labour.setBackground(CompierePLAF.getInfoBackground());
			
			//	Collective Labour
		    if (language.isBaseLanguage() )
		    {
		    	sql = "SELECT P_Collective_Labour_Agr.P_Collective_Labour_Agr_ID, Value + ' - ' +Name "
		    		+ "FROM P_Collective_Labour_Agr "
		    		+ "WHERE IsActive='Y'";

				sql = sql + " And P_Collective_Labour_Agr." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
				+ " ORDER BY Value + ' - ' +Name";

		    }
		    else
		    {
		    	sql = "SELECT P_Collective_Labour_Agr.P_Collective_Labour_Agr_ID, P_Collective_Labour_Agr.Value + ' - ' +P_Collective_Labour_Agr_Trl.Name "
		    		+ "FROM P_Collective_Labour_Agr, P_Collective_Labour_Agr_Trl "
		    		+ "WHERE P_Collective_Labour_Agr.IsActive='Y' AND P_Collective_Labour_Agr.P_Collective_Labour_Agr_ID = P_Collective_Labour_Agr_Trl.P_Collective_Labour_Agr_ID "
		    		+ " AND P_Collective_Labour_Agr_Trl.AD_Language = '" + language.getLocale() + "'"
		    		;

				sql = sql + " And P_Collective_Labour_Agr." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
				+ " ORDER BY P_Collective_Labour_Agr.Value + ' - ' +P_Collective_Labour_Agr_Trl.Name";

		    }
		    	
			//
			//	Add Access & Order
			try
			{
			    int index = 0;
				pickCollective_Labour.addItem(new KeyNamePair (0, ""));
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
				    index++;
					KeyNamePair kn = new KeyNamePair (rs.getInt(1), rs.getString(2));
					pickCollective_Labour.addItem(kn);
					if(rs.getInt(1) == collectiveLabourId)
					    pickCollective_Labour.setSelectedIndex(index);
				}
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				s_log.log(Level.SEVERE,"InfoSalaryScale.fillPicks", e);
			}
			
//			return pickCollective_Labour;

		}
		
		/**
		 * On remplit le comboBox des méthodes de rémunération selon la langue
		 * courrante.
		 */
		public static void fillRemunerationMethod(VComboBox fieldRemuneration_Method, String remuneration_methode)
		{
			
			fieldRemuneration_Method.setBackground(CompierePLAF.getInfoBackground());
		    String sql = null;
		    if(language.isBaseLanguage())
		    {
		        sql = "select AD_Ref_List.Value, AD_Ref_List.Name"
		            + " from AD_Reference inner join AD_Ref_List"
		            + " on AD_Reference.AD_Reference_ID = AD_Ref_List.AD_Reference_ID"
		            + " where AD_Reference.Name like 'P_Remuneration_Method'";
		    }
		    else
		    {
		        sql = "select AD_Ref_List.Value, AD_Ref_List_TRL.Name"
		            + " from AD_Reference inner join (AD_Ref_List inner join AD_Ref_List_TRL"
		            + " on AD_Ref_List.AD_Ref_List_ID = AD_Ref_List_TRL.AD_Ref_List_ID)"
		            + " on AD_Reference.AD_Reference_ID = AD_Ref_List.AD_Reference_ID"
		            + " where AD_Reference.Name like 'P_Remuneration_Method'"
		            + " and AD_Ref_List_TRL.AD_Language = '" + language.getAD_Language() + "'";
		    }
		    
		    try
		    {
		        PreparedStatement stmt = DB.prepareStatement(sql, null);
		        ResultSet rs = stmt.executeQuery();
		        int index = 0;
		        fieldRemuneration_Method.removeAllItems();
		        fieldRemuneration_Method.addItem(new DictionaryEntry("", ""));
		        while(rs.next())
		        {
		        	index++;
		            fieldRemuneration_Method.addItem(new DictionaryEntry(rs.getString(1), rs.getString(2)));
					if(rs.getString(1).compareTo(remuneration_methode) == 0)
					    fieldRemuneration_Method.setSelectedIndex(index);
		        }
		        
		        rs.close();
		        stmt.close();
		    }
		    catch(SQLException e)
		    {
		        s_log.log(Level.SEVERE, "fillRemunerationMethod", e);
		    }
//		    return fieldRemuneration_Method;
		}

		
}	//	InfoSalaryScale

	
	