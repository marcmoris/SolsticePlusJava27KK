/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/

package org.solstice.apps.form;


import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.*;
import java.text.*;
import java.util.*;

import org.compiere.util.*;
import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.apps.form.VPayPrint;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.plaf.*;
import org.compiere.process.*;

import java.util.logging.*;

import solstice.model.P_Period;
import solstice.model.P_Frequency;
import solstice.model.P_Tax;
import solstice.utils.PgiUtil;

public class VDeductionGouv extends CPanel
	implements FormPanel, ActionListener, TableModelListener, ASyncProcess
{
//	private DialogButton bExport = new DialogButton ("export", Msg.translate(Env.getLanguage(Env.getCtx()), "ExportExcel"), Env.getImageIcon("Export24.gif"), 0);
	private JButton bExport = ConfirmPanel.createExportButton(true);
	/** @todo withholding */

	/**
	 *  VDeductionTotal Constructor
	 */
	public VDeductionGouv()
	{
	}   //  VDeductionGouv

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			MLookup empL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 2111113, DisplayType.Search);
			fieldEmployee = new VLookup ("P_Employee_ID", false, false, true, empL);
			fieldEmployee.setBounds(206, 20, 210, 19);

/*			MLookup dedL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 2213720, DisplayType.Search);
			this.fieldDeduction = new VLookup ("P_Deduction_ID", false, false, true, dedL);
			this.fieldDeduction.setBounds(206, 20, 210, 19);
*/			
			jbInit();
			dynInit();
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

			this.miniTable.getTableHeader().addMouseListener(new ColumnChecker(this.miniTable));

		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE,"VDeductionGouv.init", e);
		}
	}	//	init

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (VDeductionGouv.class);

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;

	/** Format                  */
	private DecimalFormat   m_format = DisplayType.getNumberFormat(DisplayType.Amount);
	/** SQL for Query           */
	private String          m_sql;
	/** Number of selected rows */
	private int             m_noSelected = 0;
	/**/
	private boolean         m_isLocked = false;
	/** Payment Selection		*/
	private MPaySelection	m_ps = null;

	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();
	private CLabel labelDeduction = new CLabel();
	private VComboBox fieldDeduction = new VComboBox();;
	private GridBagLayout parameterLayout = new GridBagLayout();
	private CLabel labelEmployee = new CLabel();
	private VLookup fieldEmployee;
	
	private VCheckBox checkIsActive = new VCheckBox();
	
//	private VComboBox fieldEmployee = new VComboBox();
	private JLabel dataStatus = new JLabel();
	private JScrollPane dataPane = new JScrollPane();
	private MiniTable miniTable = new MiniTable();
	private CPanel commandPanel = new CPanel();
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	private FlowLayout commandLayout = new FlowLayout();
	private JButton bRefresh = ConfirmPanel.createRefreshButton(true);
	private CLabel labelSelectionDateFrom = new CLabel();
	private VComboBox fieldSelectionDateFrom = new VComboBox();
	private CLabel labelSelectionDateTo = new CLabel();
	private VComboBox fieldSelectionDateTo = new VComboBox();
	private CLabel labelGroupPayment = new CLabel();
	private VComboBox fieldGroupPayment = new VComboBox();
	private CLabel labelActivity = new CLabel();
	private VComboBox fieldActivity = new VComboBox();
	private CLabel labelOrg = new CLabel();
	private VComboBox fieldOrg = new VComboBox();
	private CLabel labelDept = new CLabel();
	private VComboBox fieldDept = new VComboBox();

	private CLabel labelDistribution = new CLabel();
	private VComboBox fieldDistribution = new VComboBox();
	public VLookup fieldCalendar;
    public CLabel labelCalendar = new CLabel();

	private int m_curWindowNo;
	
	/** Dispose active                                  */
	private boolean         m_disposing = false;

	final int[] cPosSIQ = {9,10,11,12};
	final int[] cPosHelico = {13,14,15,16};

	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		MLookup calendarL = MLookupFactory.get (Env.getCtx(), m_curWindowNo, 0, 2111015, DisplayType.Table);
		fieldCalendar = new VLookup ("P_Calendar_ID", true, false, true, calendarL);
		fieldCalendar.setBounds(206, 20, 210, 19);
		
	    this.fillPeriods();
	    this.fillCombobox();
		CompiereColor.setBackground(this);
		//
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);
		//
		labelDeduction.setText(Msg.translate(Env.getCtx(), "P_Deduction_ID"));
		labelEmployee.setText(Msg.translate(Env.getCtx(), "P_Employee_ID"));
//		fieldEmployee.addActionListener(this);
		
		checkIsActive.setBackground(CompierePLAF.getInfoBackground());
		checkIsActive.setSelected(false);
		checkIsActive.setText(Msg.translate(Env.getCtx(), "IsActive"));

		
		bRefresh.addActionListener(this);
		this.bExport.addActionListener(this);
		
		labelGroupPayment.setText(Msg.translate(Env.getCtx(), "P_Payment_Group_ID"));
		labelActivity.setText(Msg.translate(Env.getCtx(), "C_activity_ID"));
		labelOrg.setText(Msg.translate(Env.getCtx(), "ad_org_id"));
		labelDept.setText(Msg.translate(Env.getCtx(), "P_Department_ID"));
		labelDistribution.setText(Msg.translate(Env.getCtx(), "P_Distribution_Booklet_ID"));
		
		labelCalendar.setText(Msg.translate(Env.getCtx(), "P_Calendar_ID"));

		
		labelSelectionDateFrom.setText(Msg.translate(Env.getCtx(), "SelectionDateFrom"));
		labelSelectionDateTo.setText(Msg.translate(Env.getCtx(), "SelectionDateTo"));
		//
		dataStatus.setText(" ");
		//
		bCancel.addActionListener(this);
		//
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		
		parameterPanel.add(labelGroupPayment,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldGroupPayment,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelOrg,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldOrg,    new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		
		if (Env.getAD_Client_ID(Env.getCtx()) == 11)

		{
			parameterPanel.add(labelDeduction,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(fieldDeduction,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(labelEmployee,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(fieldEmployee,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(checkIsActive,    new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(bRefresh,    new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(labelSelectionDateFrom,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(fieldSelectionDateFrom,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(labelSelectionDateTo,  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(fieldSelectionDateTo,   new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(this.bExport,    new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		}
		else
		{
			parameterPanel.add(labelActivity,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(fieldActivity,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(labelDept,       new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(fieldDept,       new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			
			parameterPanel.add(labelDeduction,      new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(fieldDeduction,      new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(labelDistribution,   new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(fieldDistribution,   new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			
			parameterPanel.add(labelEmployee,    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(fieldEmployee,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(checkIsActive,    new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(bRefresh,         new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(labelSelectionDateFrom,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(fieldSelectionDateFrom,   new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(labelSelectionDateTo,     new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(fieldSelectionDateTo,     new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(this.bExport,    new GridBagConstraints(4, 5, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			
		}
		mainPanel.add(dataStatus, BorderLayout.SOUTH);
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(miniTable, null);
		//
		
		mainPanel.setPreferredSize(new Dimension(1024, 750));

		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
		commandPanel.add(bCancel, null);
	}   //  jbInit
	
	/**
	 * On remplit les combobox de périodes en leur affectant la période ouverte comme valeur par défaut
	 */
	private void fillPeriods()
	{
        int P_Calendar_ID =  1000011 ; // Integer.parseInt( fieldCalendar.getValue().toString() );

        if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "Client").toUpperCase().equals("CHL") )
        	P_Calendar_ID = 1000008;

        P_Period period = P_Period.getOpenPeriodWithCalendar(Env.getCtx(), P_Calendar_ID, null);
        
		String sql = "select P_Period.P_Period_ID, P_Period.SynonymName AS Name, P_Period.PeriodStatus, P_Period.P_Year_ID, P_Period.periodno "
			   + " From P_Period " 
		       + " Inner Join P_Year ON P_Year.P_Year_ID = P_Period.P_Year_ID "
			   + " WHERE P_Period.IsActive = 'Y' " 
			   + " AND P_Year.P_Calendar_ID = "  + P_Calendar_ID  
     	   + " ORDER BY P_Period.Name DESC";
		
		
	    try
	    {
	        int index = 0;
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	            this.fieldSelectionDateFrom.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name")));
	            this.fieldSelectionDateTo.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name")));
	            if(rs.getString("PeriodStatus").toUpperCase().equals("O"))
	            {
//	                this.fieldSelectionDateFrom.setSelectedIndex(index);
	                this.fieldSelectionDateTo.setSelectedIndex(index);
	            }

		        if ( rs.getInt("P_Year_ID" ) == period.getP_Year_ID() && rs.getInt("periodno" ) == 1 )  
		                this.fieldSelectionDateFrom.setSelectedIndex(index );

	            index++;
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "VDeductionGouv.fillPeriods()", e);
	    }
	}
	
	private void fillCombobox()
	{
	    String sqlOrg = "select ad_org_id,Name  from ad_org where IsActive = 'Y'";
	    sqlOrg = sqlOrg + " And ad_org." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
		+ " ORDER BY Name";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlOrg, null);
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	        	this.fieldOrg.addItem(new KeyNamePair(rs.getInt("ad_org_id"), rs.getString("Name")));
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "VDeductionGouv.fillCombobox()", e);
	    }
	    String sqlDept = "select P_Department_ID,Value + ' - ' + Name as Name from p_department where IsActive = 'Y'";
	    sqlDept = sqlDept + " And p_department." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
		+ " ORDER BY p_department.Value + ' - ' + p_department.Name";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlDept, null);
	        ResultSet rs = stmt.executeQuery();
	        this.fieldDept.addItem(new KeyNamePair(0,""));
	        while(rs.next())
	        {
	        	this.fieldDept.addItem(new KeyNamePair(rs.getInt("P_Department_ID"), rs.getString("Name")));
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "VDeductionGouv.fillCombobox()", e);
	    }	
	    String sqlActivity = "select C_Activity_ID,Value + ' - ' + Name as Name from C_Activity where IsActive = 'Y'";
	    sqlActivity = sqlActivity + " And C_Activity." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
		+ " ORDER BY C_Activity.Value + ' - ' + C_Activity.Name";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlActivity, null);
	        ResultSet rs = stmt.executeQuery();
	        this.fieldActivity.addItem(new KeyNamePair(0,""));
	        while(rs.next())
	        {
	        	this.fieldActivity.addItem(new KeyNamePair(rs.getInt("C_Activity_ID"), rs.getString("Name")));
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "VDeductionGouv.fillCombobox()", e);
	    }	
	    String sqlGroupPayment = "select p_payment_group_ID,name from p_payment_group where IsActive = 'Y'";
	    sqlGroupPayment = sqlGroupPayment + " And p_payment_group." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
		+ " ORDER BY Name";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlGroupPayment, null);
	        ResultSet rs = stmt.executeQuery();
	        this.fieldGroupPayment.addItem(new KeyNamePair(0,""));
	        while(rs.next())
	        {
	        	this.fieldGroupPayment.addItem(new KeyNamePair(rs.getInt("p_payment_group_ID"), rs.getString("Name")));
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "VDeductionGouv.fillCombobox()", e);
	    }		    

	    
	    String sqlDistribution_Booklet = "select P_Distribution_Booklet_ID, Value + ' - ' + name as name from P_Distribution_Booklet where IsActive = 'Y'";
	    sqlDistribution_Booklet = sqlDistribution_Booklet + " And P_Distribution_Booklet." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
		+ " ORDER BY Value ";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlDistribution_Booklet, null);
	        ResultSet rs = stmt.executeQuery();
	        this.fieldDistribution.addItem(new KeyNamePair(0,""));
	        while(rs.next())
	        {
	        	this.fieldDistribution.addItem(new KeyNamePair(rs.getInt("p_Distribution_Booklet_ID"), rs.getString("Name")));
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "VDeductionGouv.fillCombobox()", e);
	    }		    

	    String sqlDeduction = "select P_Deduction_ID,Value + ' - ' + Name as Name from P_Deduction where IsActive = 'Y'";
	    sqlDeduction = sqlDeduction + " And P_Deduction." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
	    							+ " AND P_Deduction.P_Deduction_ID IN ( Select P_Deduction_ID FROM P_Deduction_Param, P_Method_Deduction WHERE P_Deduction_Param.P_Method_Deduction_ID = P_Method_Deduction.P_Method_Deduction_ID AND P_Method_Deduction.VALUE IN ('002', '003','004','031') ) "
	    							+ " ORDER BY P_Deduction.Value + ' - ' + P_Deduction.Name";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlDeduction, null);
	        ResultSet rs = stmt.executeQuery();
	        this.fieldDeduction.addItem(new KeyNamePair(0,""));
	        while(rs.next())
	        {
	        	this.fieldDeduction.addItem(new KeyNamePair(rs.getInt("P_Deduction_ID"), rs.getString("Name")));
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "VDeductionGouv.fillCombobox()", e);
	    }	

	
	}

	/**
	 *  Dynamic Init.
	 *  - Load Bank Info
	 *  - Load Employee
	 *  - Init Table
	 */
	private void dynInit()
	{

		Language language = Env.getLanguage(Env.getCtx());

		/**  prepare MiniTable
		 *
		 */

		m_sql = miniTable.prepareTable(getColumn(),
				
			//	FROM
			"P_Employee " 
			+ " inner join P_Payment on P_Employee.P_Employee_ID = P_Payment.P_Employee_ID "
			+ " left outer join C_Region on C_Region.C_Region_ID = P_Payment.Taxation_Region_ID "
			+ " inner join P_Employer on P_Employer.P_Employer_ID = P_Payment.P_Employer_ID "
			+ " inner join P_Payment_Deduction on P_Payment.P_Payment_ID = P_Payment_Deduction.P_Payment_ID "
			+ " inner join P_Deduction on P_Deduction.P_Deduction_ID = P_Payment_Deduction.P_Deduction_ID "
			+ " inner join P_Period on P_Payment.P_Period_ID = P_Period.P_Period_ID "
//			+ " inner join AD_Org on P_Employee.AD_Org_ID = AD_Org.AD_Org_ID "
			+ " inner join P_Distribution_Booklet on P_Employee.P_Distribution_Booklet_ID = P_Distribution_Booklet.P_Distribution_Booklet_ID "
			+ " left outer join C_Activity on P_Payment.C_Activity_ID = C_Activity.C_Activity_ID "
			+ " left outer join P_Deduction_Trl  on (P_Deduction_Trl.P_Deduction_ID=P_Deduction.P_Deduction_ID and P_Deduction_Trl.AD_Language = '" + language.getLocale() + "')"
			+ " left outer join C_Activity_Trl  on (C_Activity_Trl.C_Activity_ID=C_Activity.C_Activity_ID and C_Activity_Trl.AD_Language = '" + language.getLocale() + "')"
			+ " left join P_Department on P_Payment.P_Department_ID = P_Department.P_Department_ID "
			+ " inner join P_Payment_Group on P_Payment.P_Payment_Group_ID = P_Payment_Group.P_Payment_Group_ID "
			+ " inner join P_Period PeriodFrom on PeriodFrom.P_period_ID = ? "
			+ " inner join P_Period PeriodTo On PeriodTo.P_Period_ID = ? "
				,
				
			//	WHERE
			"P_Period.StartDate >= (SELECT StartDate FROM P_Period where P_Period_ID = ?)"
			+ " and P_Period.EndDate <= (SELECT EndDate FROM P_Period where P_Period_ID = ?) ",
         	//	additional where & order in loadTableInfo() 
			true, "P_Employee");
			;
			
//			"P_Employee employee inner join (P_Deduction deduction inner join (P_Payment_Deduction paymentDeduction inner join P_Period period"
//			+ " on paymentDeduction.P_Period_ID = period.P_Period_ID)"
//			+ " on deduction.P_Deduction_ID = paymentDeduction.P_Deduction_ID)"
//			+ " on employee.P_Employee_ID = paymentDeduction.P_Employee_ID",
		//
		miniTable.getModel().addTableModelListener(this);
		//
		fieldSelectionDateFrom.setMandatory(true);
		fieldSelectionDateTo.setMandatory(true);
		//
	}   //  dynInit

	private int taxID;
	
	private ColumnInfo[] getColumn()
	{
		Properties ctx = Env.getCtx();

		Integer selectionDateTo = new Integer(((KeyNamePair)fieldSelectionDateTo.getSelectedItem()).getKey());
		P_Period Period = P_Period.get( Env.getCtx(), selectionDateTo.intValue(), null );
		P_Frequency Frequency = P_Frequency.get( Env.getCtx(), Period.getP_Frequency_ID(), null); 
		
		taxID = P_Tax.getCurrentTax( Period.getStartDate() );
		if ( taxID == 0)
			taxID = P_Tax.getCurrentTax( );
//		load_tax_parameter( Period.getStartDate() );
		
		int numberOfPeriod = new BigDecimal( Frequency.getNumberOfPeriod() ).intValue();

		
		
		return new ColumnInfo[] 
	    {
			//  0..4
			new ColumnInfo(" ", "P_Employee.P_Employee_ID", IDColumn.class, false, false, null),
//			new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "AD_Org.name", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "P_Distribution_Booklet_ID"), "max(P_Distribution_Booklet.value) + '-' + max(P_Distribution_Booklet.name)" , String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "P_Payment_Group_ID"), "P_Payment_Group.name", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "P_Employee_ID"), "P_Employee.value", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Name"), "P_Employee.Name", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Age"), "dbo.getAge( birthdate, getdate() )", Integer.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "IsActive"), "P_Employee.IsActive", Boolean.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "P_Employer_ID"), "P_Employer.value", String.class, true, false, null),

			new ColumnInfo(" ", "P_Deduction.P_Deduction_ID", IDColumn.class, false, false, null),
			new ColumnInfo(Msg.translate(ctx, "P_Deduction_ID"), "P_Deduction.Value", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Name"), "isNull(P_Deduction_Trl.Name,P_Deduction.Name)", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Region"), "max( C_Region.Description )", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Salary_Eligible"), "isnull(sum(P_Payment_Deduction.Salary_Eligible), 0) as total_Salary_Eligible", BigDecimal.class),
			new ColumnInfo("Salaire Admissible Calculé"  , " [dbo].[fn_getsalaire_eligible_gouv]( P_Employee.P_Employee_ID, P_Deduction.P_Deduction_ID, P_Payment.P_Employer_ID, P_Period.P_Year_ID,  max(PeriodFrom.P_Period_ID) , max(PeriodTo.P_Period_ID) , P_Payment.Taxation_Region_ID ) as SalAdmCal",  BigDecimal.class),
			new ColumnInfo(Msg.translate(ctx, "Hours_Eligible"), "isnull(sum(P_Payment_Deduction.Hours_Eligible), 0) as total_Hours_Eligible", BigDecimal.class),
			new ColumnInfo(Msg.translate(ctx, "Employee_Part"), "isnull(sum(P_Payment_Deduction.Employee_Part), 0) as total_Emp", BigDecimal.class),
			new ColumnInfo(Msg.translate(ctx, "Employer_Part"), "isnull(sum(P_Payment_Deduction.Employer_Part), 0) as total_Emr", BigDecimal.class),
			new ColumnInfo(Msg.translate(ctx, "AccumulationAmount"), "isnull(sum(P_Payment_Deduction.AccumulationAmount), 0) as total_Acc", BigDecimal.class),
			new ColumnInfo(Msg.translate(ctx, "RetrievalAmount"), "isnull(sum(P_Payment_Deduction.RetrievalAmount), 0) as total_Ret", BigDecimal.class),
			new ColumnInfo(Msg.translate(ctx, "Count"), "dbo.fn_GetNbrPeriod( P_Employee.P_Employee_ID, P_Deduction.P_Deduction_ID, P_Payment.P_Employer_ID, P_Period.P_Year_ID, max(PeriodFrom.P_Period_ID) , max(PeriodTo.P_Period_ID), P_Payment.Taxation_Region_ID ) as nbrPeriod", Integer.class),
			new ColumnInfo(Msg.translate(ctx, "TotalExemption"), " dbo.fn_GetTotalDeductionCal( P_Deduction.P_Deduction_ID, sum(P_Payment_Deduction.Salary_Eligible), sum(P_Payment_Deduction.Employee_Part), P_Employee.P_Employee_ID, P_Payment.P_Employer_ID , " + numberOfPeriod + ", dbo.fn_GetNbrPeriod( P_Employee.P_Employee_ID, P_Deduction.P_Deduction_ID, P_Payment.P_Employer_ID, P_Period.P_Year_ID,  max(PeriodFrom.P_Period_ID) , max(PeriodTo.P_Period_ID), P_Payment.Taxation_Region_ID ), " + taxID + ",1, P_Payment.Taxation_Region_ID) as TotalExemption",  BigDecimal.class),
			new ColumnInfo(Msg.translate(ctx, "AmountCalc")    , " dbo.fn_GetTotalDeductionCal( P_Deduction.P_Deduction_ID, sum(P_Payment_Deduction.Salary_Eligible), sum(P_Payment_Deduction.Employee_Part), P_Employee.P_Employee_ID, P_Payment.P_Employer_ID , " + numberOfPeriod + ", dbo.fn_GetNbrPeriod( P_Employee.P_Employee_ID, P_Deduction.P_Deduction_ID, P_Payment.P_Employer_ID, P_Period.P_Year_ID,  max(PeriodFrom.P_Period_ID) , max(PeriodTo.P_Period_ID), P_Payment.Taxation_Region_ID ), " + taxID + ",2, P_Payment.Taxation_Region_ID) as AmountCalc",  BigDecimal.class),
			new ColumnInfo(Msg.translate(ctx, "Difference")    , " dbo.fn_GetTotalDeductionCal( P_Deduction.P_Deduction_ID, sum(P_Payment_Deduction.Salary_Eligible), sum(P_Payment_Deduction.Employee_Part), P_Employee.P_Employee_ID, P_Payment.P_Employer_ID , " + numberOfPeriod + ", dbo.fn_GetNbrPeriod( P_Employee.P_Employee_ID, P_Deduction.P_Deduction_ID, P_Payment.P_Employer_ID, P_Period.P_Year_ID,  max(PeriodFrom.P_Period_ID) , max(PeriodTo.P_Period_ID), P_Payment.Taxation_Region_ID ), " + taxID + ",3, P_Payment.Taxation_Region_ID) as diff",  BigDecimal.class),
			new ColumnInfo("Montant Calculé Employer"    , " dbo.fn_GetTotalDeductionCal( P_Deduction.P_Deduction_ID, sum(P_Payment_Deduction.Salary_Eligible), sum(P_Payment_Deduction.Employer_Part), P_Employee.P_Employee_ID, P_Payment.P_Employer_ID , " + numberOfPeriod + ", dbo.fn_GetNbrPeriod( P_Employee.P_Employee_ID, P_Deduction.P_Deduction_ID, P_Payment.P_Employer_ID, P_Period.P_Year_ID,  max(PeriodFrom.P_Period_ID) , max(PeriodTo.P_Period_ID), P_Payment.Taxation_Region_ID ), " + taxID + ",4, P_Payment.Taxation_Region_ID) as AmountCalcYer",  BigDecimal.class),
			new ColumnInfo("Différence Employer"         , " dbo.fn_GetTotalDeductionCal( P_Deduction.P_Deduction_ID, sum(P_Payment_Deduction.Salary_Eligible), sum(P_Payment_Deduction.Employer_Part), P_Employee.P_Employee_ID, P_Payment.P_Employer_ID , " + numberOfPeriod + ", dbo.fn_GetNbrPeriod( P_Employee.P_Employee_ID, P_Deduction.P_Deduction_ID, P_Payment.P_Employer_ID, P_Period.P_Year_ID,  max(PeriodFrom.P_Period_ID) , max(PeriodTo.P_Period_ID), P_Payment.Taxation_Region_ID ), " + taxID + ",5, P_Payment.Taxation_Region_ID) as diffYer",  BigDecimal.class)
						
	    };
	}
		          		
	
	/**
	 *  Query and create TableInfo
	 */
	private void loadTableInfo()
	{

		
		//  Get 
		try
		{
		    PreparedStatement pstmt = this.prepareStatement();
			//
			ResultSet rs = pstmt.executeQuery();
			miniTable.loadTable(rs);
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"VDeductionGouv.loadTableInfo", e);
		}
		calculateSelection();
	}   //  loadTableInfo
	
	private PreparedStatement prepareStatement() throws Exception
	{
		//  not yet initialized
		if (m_sql == null)
			return null;

		String sql =  m_sql;
		//  Parameters
		Integer selectionDateFrom = new Integer(((KeyNamePair)fieldSelectionDateFrom.getSelectedItem()).getKey());
		Integer selectionDateTo = new Integer(((KeyNamePair)fieldSelectionDateTo.getSelectedItem()).getKey());
		miniTable.setColorCompare(selectionDateFrom);
		miniTable.setColorCompare(selectionDateTo);

		//
//		if (onlyDue.isSelected())
//			sql += " AND i.DateInvoiced+p.NetDays <= ?";
		//

		int P_Employee_ID = 0;
		int P_Deduction_ID = 0;

		Object value = fieldEmployee.getValue();
		if (value != null && value.toString().length() > 0)
		{
			P_Employee_ID = Integer.parseInt( value.toString() );
		}

		if(this.fieldDeduction.getValue() != null
		        && (P_Deduction_ID = ((Number)this.fieldDeduction.getValue()).intValue()) > 0)
		{
			sql += " AND P_Deduction.P_Deduction_ID=?";
		}

		sql += " AND P_Deduction.P_Deduction_ID IN ( Select P_Deduction_ID FROM P_Deduction_Param, P_Method_Deduction WHERE P_Deduction_Param.P_Method_Deduction_ID = P_Method_Deduction.P_Method_Deduction_ID AND P_Method_Deduction.VALUE IN ('002', '003','004','031') ) ";

		
		if(checkIsActive.isSelected() == true)
			sql += " AND P_Employee.IsActive = 'Y'";

		if (P_Employee_ID != 0)
			sql += " AND P_Employee.P_Employee_ID=?";
		
		int Org_id = Integer.parseInt(fieldOrg.getValue().toString());
		if (Org_id != 0)
		{
			sql += " and P_Payment.ad_Org_id = ? ";
		}
		
		int Activity_id = Integer.parseInt(fieldActivity.getValue().toString());	
		if (Activity_id != 0)
		{
			sql += " and P_Payment.C_Activity_id = ? ";
		}
		
		int Department_id = Integer.parseInt(fieldDept.getValue().toString());	
		if (Department_id != 0)
		{
			sql += " and P_Payment.P_Department_id = ? ";
		}

		int Payment_Group_id = Integer.parseInt(fieldGroupPayment.getValue().toString());	
		if (Payment_Group_id != 0)
		{
			sql += " and P_Payment.P_Payment_Group_id = ? ";
		}

		int Distribution_id = Integer.parseInt(fieldDistribution.getValue().toString());	
		if (Distribution_id != 0)
		{
			sql += " and P_Employee.P_Distribution_Booklet_id = ? ";
		}

		
		sql += " group by P_Payment.Taxation_Region_ID, P_Employee.P_Employee_ID, P_Employee.Value, P_Employee.Name, P_Deduction.P_Deduction_ID, P_Deduction.Value, P_Deduction.Name,P_Deduction_Trl.name, P_Payment_Group.name, P_Payment.P_Employer_ID, P_Employer.Value, P_Employee.IsActive, P_Employee.birthdate, P_Period.P_Year_ID ";
		sql += " ORDER BY 2,5";

		int index = 1;
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
//		pstmt.setInt(index++, m_AD_Client_ID);
//		if (onlyDue.isSelected())

		pstmt.setInt(index++, selectionDateFrom.intValue());
		pstmt.setInt(index++, selectionDateTo.intValue());

		pstmt.setInt(index++, selectionDateFrom.intValue());
		pstmt.setInt(index++, selectionDateTo.intValue());
		if (P_Deduction_ID != 0)
			pstmt.setInt(index++, P_Deduction_ID);
		if (P_Employee_ID != 0)
			pstmt.setInt(index++, P_Employee_ID);
		
		if (Org_id != 0)
		{
			pstmt.setInt(index++, Org_id);
		}
			
		if (Activity_id != 0)
		{
			pstmt.setInt(index++, Activity_id);
		}
		
		if (Department_id != 0)
		{
			pstmt.setInt(index++, Department_id);
		}

		if (Payment_Group_id != 0)
		{
			pstmt.setInt(index++, Payment_Group_id);
		}

		if (Distribution_id != 0)
		{
			pstmt.setInt(index++, Distribution_id);
		}

		s_log.info("sql : " + sql);
		
		return pstmt;
	}

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		m_disposing = true;

		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	/*************************************************************************/

	/**
	 *  ActionListener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
	    if(e.getSource() == this.bExport)
	    {
			setBusy( true );
			try
			{
			    exportToXml();
			}
			catch (IOException ex)
			{
			    s_log.log(Level.SEVERE, "exportToCSV", ex);
			}
			setBusy( false );
	    }
	    else
	    {
			//  Update Bank Info
	//		if (e.getSource() == fieldDeduction)
	//			loadDeductionInfo();
	
			if (e.getSource() == bCancel)
				dispose();
	
			//  Update Open Invoices
			else if (e.getSource() == fieldDeduction || e.getSource() == fieldEmployee || e.getSource() == bRefresh)
			{
			    setBusy( true );
				loadTableInfo();
				setBusy( false );
			}
	    }
	}   //  actionPerformed

	
	/**
	 * Cette méthode sert à exporter le contenue du grid dans un fichier XML Excel
	 */

	private void exportToXml() throws IOException
	{
	    // On doit d'abord créer le fichier *.csv. Pour ce faire, on utilise d'abord
	    // un dialogue de sauvegarde pour obtenir le nom du fichier
        String fileName = null;
		JFileChooser dialog = new JFileChooser();
		if(dialog.showSaveDialog(new Frame()) == JFileChooser.APPROVE_OPTION)
		{
		    // L'utilisateur a choisi un nom de ficher, on doit vérifier si celui-ci
		    // est valide et se termine bien par .csv
		    File file = dialog.getSelectedFile();
		    if(file.getPath().length() > 0)
		    {
		        FileWriter fileWriter;
		        if(file.getPath().length() > 4 && file.getPath().endsWith(".xml"))
		        {
		        	fileName = file.getPath();
		            fileWriter = new FileWriter(file);
		        }
		        else
		        {
		        	fileName = file.getPath() + ".xml";
		            fileWriter = new FileWriter(file.getPath() + ".xml");
		        }

		        
			    try
			    {
			        // On charge maintenant la liste des informations du grid
			        // en réexécutant la requête
			        PreparedStatement stmt = this.prepareStatement();
			        ResultSet rs = stmt.executeQuery();
			        ResultSetMetaData rsMetaData = rs.getMetaData();

			        fileWriter.write("<?xml version=\"1.0\"?>\n");
			        fileWriter.write("<?mso-application progid=\"Excel.Sheet\"?>\n");
			        fileWriter.write("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"\n");
			        fileWriter.write("xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n");
			        fileWriter.write(" xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\n");
			        fileWriter.write(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"\n");
			        fileWriter.write(" xmlns:html=\"http://www.w3.org/TR/REC-html40\">\n");
			        fileWriter.write(" <DocumentProperties xmlns=\"urn:schemas-microsoft-com:office:office\">\n");
			        fileWriter.write(" </DocumentProperties>\n");
			        fileWriter.write(" <OfficeDocumentSettings xmlns=\"urn:schemas-microsoft-com:office:office\">\n");
			        fileWriter.write("  <RelyOnVML/>\n");
			        fileWriter.write(" </OfficeDocumentSettings>\n");
			        fileWriter.write(" <Worksheet ss:Name=\"" + PgiUtil.convertHTMLString(Msg.translate(Env.getCtx(), "P_Deduction_ID")) + "\">\n");
			        fileWriter.write("  <Table ss:ExpandedColumnCount=\"" + rsMetaData.getColumnCount() + "\">\n");

		            fileWriter.write("<Row>\n");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Livret de temps</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Groupe de paiement</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">EmployÃ©</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Nom</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Age</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Actif</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Employer</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">DÃ©duction</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Nom</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">RÃ©gion</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Salaire admissible</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Salaire admissible Calcule</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Heures admissibles</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Part employÃ©</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Part employeur</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">ArrÃ©rage</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">RÃ©cupÃ©ration</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Nombre</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Total Exemption</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Montant calculÃ©</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">DiffÃ©rence</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">Montant calculÃ© Employeur</Data></Cell>");
		            fileWriter.write("<Cell><Data ss:Type=\"String\">DiffÃ©rence Employeur</Data></Cell>");
		            fileWriter.write("</Row>\n");
		            

		            while(rs.next())
			        {
			            // On crée chaque ligne dans le fichier selon le format d'un .csv
			            fileWriter.write("<Row>\n");
			            for(int i = 1; i <= rsMetaData.getColumnCount() ; i++)
			            {
			                // On ne doit pas inclure les colonnes ID
			                if(!rsMetaData.getColumnName(i).endsWith("_ID"))
			                {
			                	if ( rsMetaData.getColumnClassName(i).equals( "java.lang.String") )
			                		if (rs.getString(i) != null)
			                		{
			                			fileWriter.write("<Cell><Data ss:Type=\"String\">" + PgiUtil.convertHTMLString(rs.getString(i)) + "</Data></Cell>\n");
			                		}
			                		else
			                		{
			                			fileWriter.write("<Cell><Data ss:Type=\"String\">" + PgiUtil.convertHTMLString("") + "</Data></Cell>\n");
			                		}
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.BigDecimal") )
			                		fileWriter.write("<Cell><Data ss:Type=\"Number\">" + rs.getString(i) + "</Data></Cell>\n");
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.Double") )
			                		fileWriter.write("<Cell><Data ss:Type=\"Number\">" + rs.getString(i) + "</Data></Cell>\n");
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.sql.Timestamp") )
			                		fileWriter.write("<Cell><Data ss:Type=\"String\">" + rs.getTimestamp(i).toString().substring(0,9) + "</Data></Cell>\n");
			                	else
			                		fileWriter.write("<Cell><Data ss:Type=\"String\">" + rs.getString(i) + "</Data></Cell>\n");
			                }
//			                    buffer += "\"" + rs.getString(i).replaceAll("\"", "\"\"") + "\";";
			            }
//			            buffer += "\"" + rs.getString(rsMetaData.getColumnCount()) + "\"";

				            fileWriter.write("</Row>\n");

//			            fileWriter.write(buffer + "\n");
			        }
			        
			        rs.close();
			        stmt.close();

			        fileWriter.write("  </Table>\n");
			        fileWriter.write(" </Worksheet>\n");
			        fileWriter.write("</Workbook>\n");

			    }
			    catch (Exception e)
			    {
			        s_log.log(Level.SEVERE, "exportToXML", e);
			    }
			    
			    fileWriter.close();

			    org.solstice.util.ExcelExportUtil.postExportSuccess(new File(fileName), this);

		    }
		}
		
	}

	
	/**
	 * Cette méthode sert à exporter le contenue du grid dans un fichier
	 * séparé par des virgules (*.csv) qui peut être lu dans excel
	 */
	private void exportToCSV() throws IOException
	{
	    // On doit d'abord créer le fichier *.csv. Pour ce faire, on utilise d'abord
	    // un dialogue de sauvegarde pour obtenir le nom du fichier
		JFileChooser dialog = new JFileChooser();
		if(dialog.showSaveDialog(new Frame()) == JFileChooser.APPROVE_OPTION)
		{
		    // L'utilisateur a choisi un nom de ficher, on doit vérifier si celui-ci
		    // est valide et se termine bien par .csv
		    File file = dialog.getSelectedFile();
		    if(file.getPath().length() > 0)
		    {
		        FileWriter fileWriter;
		        if(file.getPath().length() > 4 && file.getPath().endsWith(".csv"))
		        {
		            fileWriter = new FileWriter(file);
		        }
		        else
		        {
		            fileWriter = new FileWriter(file.getPath() + ".csv");
		        }

			    try
			    {
			        // On charge maintenant la liste des informations du grid
			        // en réexécutant la requête
			        PreparedStatement stmt = this.prepareStatement();
			        ResultSet rs = stmt.executeQuery();
			        ResultSetMetaData rsMetaData = rs.getMetaData();
			        
			        while(rs.next())
			        {
			            // On crée chaque ligne dans le fichier selon le format d'un .csv
			            String buffer = "";
			            for(int i = 1; i <= rsMetaData.getColumnCount() - 1; i++)
			            {
			                // On ne doit pas inclure les colonnes ID
			                if(!rsMetaData.getColumnName(i).endsWith("_ID"))
			                    buffer += "\"" + rs.getString(i).replaceAll("\"", "\"\"") + "\";";
			            }
			            buffer += "\"" + rs.getString(rsMetaData.getColumnCount()) + "\"";
			            fileWriter.write(buffer + "\n");
			        }
			        
			        rs.close();
			        stmt.close();
			    }
			    catch (Exception e)
			    {
			        s_log.log(Level.SEVERE, "exportToCSV", e);
			    }
			    
			    fileWriter.close();
		    }
		}
	}
	/**
	 *  Table Model Listener
	 *  @param e event
	 */
	public void tableChanged(TableModelEvent e)
	{
		if (m_disposing || isUILocked())
			return;

		if (e.getColumn() == 0)
			calculateSelection();
	}   //  valueChanged

	/**
	 *  Calculate selected rows.
	 *  - add up selected rows
	 */
	public void calculateSelection()
	{
		m_noSelected = 0;
		int[] lPos;
		if (Env.getAD_Client_ID(Env.getCtx()) == 11)
		{
			lPos = cPosSIQ;	
		}
		else
		{
			lPos = cPosHelico;
		}
		BigDecimal DeductionAmt1 = new BigDecimal(0.0);
		BigDecimal DeductionAmt2 = new BigDecimal(0.0);
		BigDecimal DeductionAmt3 = new BigDecimal(0.0);
		BigDecimal DeductionAmt4 = new BigDecimal(0.0);

		int rows = miniTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);
			if (id.isSelected())
			{
				BigDecimal amt1 = (BigDecimal)miniTable.getModel().getValueAt(i, lPos[0]);
				BigDecimal amt2 = (BigDecimal)miniTable.getModel().getValueAt(i, lPos[1]);
				BigDecimal amt3 = (BigDecimal)miniTable.getModel().getValueAt(i, lPos[2]);
				BigDecimal amt4 = (BigDecimal)miniTable.getModel().getValueAt(i, lPos[3]);
				DeductionAmt1 = DeductionAmt1.add(amt1);
				DeductionAmt2 = DeductionAmt2.add(amt2);
				DeductionAmt3 = DeductionAmt3.add(amt3);
				DeductionAmt4 = DeductionAmt4.add(amt4);
				m_noSelected++;
			}
		}

		//  Information
		StringBuffer info = new StringBuffer();
		info.append(m_noSelected).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected")).append(" - ");
		info.append(" Salaires admissibles : ");  //.append(", ");
		info.append(m_format.format(DeductionAmt1));  //.append(", ");
		info.append(" Heures admissibles : ");  //.append(", ");
		info.append(m_format.format(DeductionAmt2));  //.append(", ");
		info.append(" Part employé : ");  //.append(", ");
		info.append(m_format.format(DeductionAmt3));  //.append(", ");
		info.append(" Part employeur : ");  //.append(", ");
		info.append(m_format.format(DeductionAmt4));  //.append(", ");
		dataStatus.setText(info.toString());
		//
	}   //  calculateSelection

	/**
	 *  Generate PaySelection
	 */
	
	/**
	 *  Lock User Interface
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
	public void lockUI (ProcessInfo pi)
	{
		this.setEnabled(false);
		m_isLocked = true;
	}   //  lockUI

	/**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi process info
	 */
	public void unlockUI (ProcessInfo pi)
	{
	//	this.setEnabled(true);
	//	m_isLocked = false;
		//  Ask to Print it
		if (!ADialog.ask(m_WindowNo, this, "VDeductionGouvPrint?", "(" + pi.getSummary() + ")"))
			return;

		//  Start PayPrint
		int AD_Form_ID = 106;	//	Payment Print/Export
		FormFrame ff = new FormFrame();
		ff.openForm (AD_Form_ID);
		//	Set Parameter
		if (m_ps != null)
		{
			VPayPrint pp = (VPayPrint)ff.getFormPanel();
			pp.setPaySelection(m_ps.getC_PaySelection_ID());
		}
		//
		ff.pack();
		this.setVisible(false);
		AEnv.showCenterScreen(ff);
		this.dispose();
	}   //  unlockUI

	/**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
	public boolean isUILocked()
	{
		return m_isLocked;
	}   //  isLoacked

	/**
	 *  Method to be executed async.
	 *  Called from the ASyncProcess worker
	 *  @param pi process info
	 */
	public void executeASync (ProcessInfo pi)
	{
	}   //  executeASync

	/*************************************************************************/

	/**
	 *  Bank Account Info
	 */
	public class DeductionInfo
	{
		public DeductionInfo (int newP_Deduction_ID, String newName)
		{
			P_Deduction_ID = newP_Deduction_ID;
			Name = newName;
		}
		int P_Deduction_ID;
		String Name;
		public String toString()
		{
			return Name;
		}
	}   //  DeductionInfo

	/**
	 *	Indicate Busy
	 *  @param busy busy
	 */
	private void setBusy (boolean busy)
	{
		m_isLocked = busy;
		//
		if (busy)
		{
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
		else
		{
			this.setCursor(Cursor.getDefaultCursor());
			m_frame.setCursor(Cursor.getDefaultCursor());
		}
	}	//	set Busy

}
