/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                     *
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
 * ProGestion Informatique, 5300 Bld des Galerie, Suite 210, Quebec, G2K 2A2 Canada      *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ****************************************************************************/
package org.solstice.apps.form;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileFilter;
import java.math.*;
import java.text.*;
import java.util.*;
import java.util.Calendar;

import org.compiere.util.*;
import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.plaf.*;
import org.compiere.process.*;
import java.util.logging.*;

import solstice.model.*;
import solstice.process.PgiUtil;

import java.util.Enumeration;
import javax.swing.table.*;

/**
 *  Analyse de la dépense
 *
 *  @author Marc Morissette
 *  @version $Id: VExpenseReport.java,v 1.5 2008/10/29 15:00:15 marmor01 Exp $
 */

public class VExpenseReport extends CPanel
	implements FormPanel, ActionListener, TableModelListener, ASyncProcess
{
//	private DialogButton bExport = new DialogButton ("export", Msg.translate(Env.getLanguage(Env.getCtx()), "ExportExcel"), Env.getImageIcon("Export24.gif"), 0);
	private JButton bExport = ConfirmPanel.createExportButton(true);

	private String trxName = null;

	/**
	 *  VExpenseReport Constructor
	 */
	public VExpenseReport()
	{
	}   //  VGeneralLedgerTotal

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

			MLookup elementValueL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 1125, DisplayType.Search);
			this.fieldElementValue = new VLookup ("C_ElementValue_ID", false, false, true, elementValueL);
			this.fieldElementValue.setBounds(206, 20, 210, 19);

			this.fieldBElementValue = new VLookup ("C_ElementValue_ID", false, false, true, elementValueL);
			this.fieldBElementValue.setBounds(206, 20, 210, 19);

			jbInit();
			dynInit();
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

			this.miniTable.getTableHeader().addMouseListener(new ColumnChecker(this.miniTable));

		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE,"VExpenseReport.init", e);
		}
	}	//	init

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (VExpenseReport.class);

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
	/** Client ID               */
	private int             m_AD_Client_ID = 0;
	/**/
	private boolean         m_isLocked = false;
	
	//
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();
	
	private CLabel labelElementValue = new CLabel();
	private CLabel labelElementValueTxt = new CLabel();
	private CLabel labelElementValueTxt2 = new CLabel();
	private CLabel labelElementValueTxt3 = new CLabel();
	private CLabel labelElementValueTxt4 = new CLabel();
	private VLookup fieldElementValue;
	private CTextField fieldElementValueTxt1 = new CTextField("%");
	private CTextField fieldElementValueTxt2 = new CTextField("%");
	private CTextField fieldElementValueTxt3 = new CTextField("%");
	private CTextField fieldElementValueTxt4 = new CTextField("%");

	private CLabel labelBElementValue = new CLabel();
	private CLabel labelBElementValueTxt = new CLabel();
	private CLabel labelBElementValueTxt2 = new CLabel();
	private CLabel labelBElementValueTxt3 = new CLabel();
	private CLabel labelBElementValueTxt4 = new CLabel();
	private VLookup fieldBElementValue;
	private CTextField fieldBElementValueTxt1 = new CTextField("%");
	private CTextField fieldBElementValueTxt2 = new CTextField("%");
	private CTextField fieldBElementValueTxt3 = new CTextField("%");
	private CTextField fieldBElementValueTxt4 = new CTextField("%");
	
	private CLabel labelExcel = new CLabel();
	private CLabel labelExcelTab = new CLabel();
	
	private VComboBox ExcelFile = new VComboBox();
	private VComboBox ExcelTab = new VComboBox();
	private JButton bLinkedServerAdd = ConfirmPanel.createNewButton(true);

	private VComboBox ReportType = new VComboBox();

	private GridBagLayout parameterLayout = new GridBagLayout();
	private CLabel labelEmployee = new CLabel();
	private VLookup fieldEmployee;
	private VCheckBox checkIsActive = new VCheckBox();
	private VCheckBox checkAssignBaseEmp = new VCheckBox();
	
//	private VComboBox fieldEmployee = new VComboBox();
	private JLabel dataStatus = new JLabel();
	private JScrollPane dataPane = new JScrollPane();
	private MiniTable miniTable = new MiniTable();
	private CPanel commandPanel = new CPanel();
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	private FlowLayout commandLayout = new FlowLayout();
	private JButton bRefresh = ConfirmPanel.createRefreshButton(true);

	private CLabel labelSelectionDateFrom = new CLabel();
	private VComboBox fieldPeriodCouFrom = new VComboBox();
	private CLabel labelSelectionDateTo = new CLabel();
	private VComboBox fieldPeriodCouTo = new VComboBox();

	private CLabel labelPeriodAntFrom = new CLabel();
	private VComboBox fieldPeriodAntFrom = new VComboBox();
	private CLabel labelPeriodAntTo = new CLabel();
	private VComboBox fieldPeriodAntTo = new VComboBox();

	
	private CLabel labelGroupPayment = new CLabel();
	private VComboBox fieldGroupPayment = new VComboBox();
	private CLabel labelActivity = new CLabel();
	private VComboBox fieldActivity = new VComboBox();
	private CLabel labelOrg = new CLabel();
	private VComboBox fieldOrg = new VComboBox();
	private CLabel labelDept = new CLabel();
	private VComboBox fieldDept = new VComboBox();

	private CLabel labelsalesRegionFrom = new CLabel();
	private VComboBox fieldsalesRegionFrom = new VComboBox();

	private CLabel labelsalesRegion = new CLabel();
	private VComboBox fieldsalesRegion = new VComboBox();

	/** Dispose active                                  */
	private boolean         m_disposing = false;
	


	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{

    	this.ReportType.addItem(new KeyNamePair(1, "Sommaire"));
    	this.ReportType.addItem(new KeyNamePair(2, "Detailler"));
    	
		this.fillPeriods();
	    this.fillCombobox();
	    this.fillComboboxServers("");
		CompiereColor.setBackground(this);
		//
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);
		//

		labelsalesRegionFrom.setText(Msg.translate(Env.getCtx(), "C_salesRegion_ID") + " de : ");
		labelsalesRegion.setText( " à " );

		labelElementValue.setText(Msg.translate(Env.getCtx(), "Expense") + " " + Msg.translate(Env.getCtx(), "C_ElementValue_ID"));
		labelElementValueTxt.setText(Msg.translate(Env.getCtx(), "C_ElementValue_ID") + " 01") ;
		labelElementValueTxt2.setText(Msg.translate(Env.getCtx(), "C_ElementValue_ID") + " 02");
		labelElementValueTxt3.setText(Msg.translate(Env.getCtx(), "C_ElementValue_ID") + " 03");
		labelElementValueTxt4.setText(Msg.translate(Env.getCtx(), "C_ElementValue_ID") + " 04");

		labelBElementValue.setText(Msg.translate(Env.getCtx(), "Budget") + " " + Msg.translate(Env.getCtx(), "C_ElementValue_ID"));
		labelBElementValueTxt.setText(Msg.translate(Env.getCtx(), "C_ElementValue_ID") + " 01") ;
		labelBElementValueTxt2.setText(Msg.translate(Env.getCtx(), "C_ElementValue_ID") + " 02");
		labelBElementValueTxt3.setText(Msg.translate(Env.getCtx(), "C_ElementValue_ID") + " 03");
		labelBElementValueTxt4.setText(Msg.translate(Env.getCtx(), "C_ElementValue_ID") + " 04");

		labelEmployee.setText(Msg.translate(Env.getCtx(), "P_Employee_ID"));

		labelExcel.setText("Excel");
		labelExcelTab.setText( "Tab" );

		checkAssignBaseEmp.setBackground(CompierePLAF.getInfoBackground());
		checkAssignBaseEmp.setSelected(false);
		checkAssignBaseEmp.setText("Base assig.");

		checkIsActive.setBackground(CompierePLAF.getInfoBackground());
		checkIsActive.setSelected(false);
		checkIsActive.setText(Msg.translate(Env.getCtx(), "IsActive"));

//		fieldEmployee.addActionListener(this);
		bRefresh.addActionListener(this);
		this.bExport.addActionListener(this);
		this.bLinkedServerAdd.addActionListener(this);
		ExcelFile.addActionListener(this);
	
		labelGroupPayment.setText(Msg.translate(Env.getCtx(), "P_Payment_Group_ID"));
		labelActivity.setText(Msg.translate(Env.getCtx(), "C_activity_ID"));
		labelOrg.setText(Msg.translate(Env.getCtx(), "ad_org_id"));
		labelDept.setText(Msg.translate(Env.getCtx(), "P_Department_ID"));

		labelSelectionDateFrom.setText("(Courante) " + Msg.translate(Env.getCtx(), "SelectionDateFrom"));
		labelSelectionDateTo.setText(Msg.translate(Env.getCtx(), "SelectionDateTo"));

		labelPeriodAntFrom.setText("(Antérieur) " + Msg.translate(Env.getCtx(), "SelectionDateFrom"));
		labelPeriodAntTo.setText(Msg.translate(Env.getCtx(), "SelectionDateTo"));


		//
		dataStatus.setText(" ");
		//
		bCancel.addActionListener(this);
		ReportType.addActionListener(this);
		//
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		parameterPanel.add(labelGroupPayment,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldGroupPayment,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelOrg,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldOrg,    new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelEmployee,   new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldEmployee,    new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkIsActive,    new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelsalesRegionFrom,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldsalesRegionFrom,  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelsalesRegion,  new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldsalesRegion,  new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(checkAssignBaseEmp,    new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


		parameterPanel.add(labelElementValue,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldElementValue,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelElementValueTxt,  new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldElementValueTxt1,   new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelElementValueTxt2,  new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldElementValueTxt2,   new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		
		parameterPanel.add(labelElementValueTxt3,  new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldElementValueTxt3,   new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelElementValueTxt4,  new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldElementValueTxt4,   new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(bRefresh,    new GridBagConstraints(6, 4, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(bExport,    new GridBagConstraints(6, 5, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(bLinkedServerAdd,    new GridBagConstraints(6, 6, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelDept,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldDept,    new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelActivity,  new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldActivity,   new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		
		parameterPanel.add(labelSelectionDateFrom,  new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldPeriodCouFrom,   new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelSelectionDateTo,  new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldPeriodCouTo,   new GridBagConstraints(1, 6, 2, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		
		parameterPanel.add(labelPeriodAntFrom,  new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldPeriodAntFrom,  new GridBagConstraints(4, 5, 2, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelPeriodAntTo,  new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldPeriodAntTo,   new GridBagConstraints(4, 6, 2, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));



		parameterPanel.add(this.ReportType,   new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelExcel,  new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(this.ExcelFile,   new GridBagConstraints(3, 7, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(this.labelExcelTab, new GridBagConstraints(4, 7, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(this.ExcelTab,   new GridBagConstraints(5, 7, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

/*		JPanel line = new JPanel();
		line.setLayout(new BoxLayout( line, BoxLayout.LINE_AXIS));
		line.setBorder( BorderFactory.createEmptyBorder(0,7,2,50));
		parameterPanel.add( line);
	*/	
		parameterPanel.add(labelBElementValue,  new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldBElementValue,   new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelBElementValueTxt,  new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldBElementValueTxt1,   new GridBagConstraints(3, 8, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelBElementValueTxt2,  new GridBagConstraints(4, 8, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldBElementValueTxt2,   new GridBagConstraints(5, 8, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelBElementValueTxt3,  new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldBElementValueTxt3,   new GridBagConstraints(3, 9, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelBElementValueTxt4,  new GridBagConstraints(4, 9, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldBElementValueTxt4,   new GridBagConstraints(5, 9, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

			
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
        P_Period period = P_Period.getOpenPeriod(Env.getCtx(), null);
        
		String sql = "select P_Period_ID, Name, StartDate, Enddate, PeriodStatus from P_Period WHERE IsActive = 'Y' " +
					 " AND P_Frequency_ID = "  + period.getP_Frequency_ID() + 
            		 "ORDER BY Name DESC";
	    try
	    {
	        int index = 0;
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	            this.fieldPeriodCouFrom.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name") + " (" + rs.getString("StartDate").substring(0, 10) + " - " + rs.getString("EndDate").substring(0, 10) + ")" ));
	            this.fieldPeriodCouTo.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name") + " (" + rs.getString("StartDate").substring(0, 10) + " - " + rs.getString("EndDate").substring(0, 10) + ")"));

	            this.fieldPeriodAntFrom.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name") + " (" + rs.getString("StartDate").substring(0, 10) + " - " + rs.getString("EndDate").substring(0, 10) + ")"));
	            this.fieldPeriodAntTo.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name") + " (" + rs.getString("StartDate").substring(0, 10) + " - " + rs.getString("EndDate").substring(0, 10) + ")"));

	            if(rs.getString("PeriodStatus").toUpperCase().equals("O"))
	            {
	                this.fieldPeriodCouFrom.setSelectedIndex(index);
	                this.fieldPeriodCouTo.setSelectedIndex(index);

	                this.fieldPeriodAntFrom.setSelectedIndex(index);
	                this.fieldPeriodAntTo.setSelectedIndex(index);

	            }
	            index++;
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "VExpenseReport.fillPeriods()", e);
	    }
	}

	private void fillComboboxServers( String srvdefault )
	{	
		ExcelFile.removeAllItems();
		
		String sqlExcelFile = "SELECT srv.name FROM sys.servers srv WHERE srv.server_id != 0 and product like 'Excel%' "
		;
		
		// Sommaire
		if ( ((KeyNamePair)ReportType.getSelectedItem()).getKey() == 1  )
		{
			sqlExcelFile += " and srv.name Like 'BUDGET%' ";
		}
		else
		{
			sqlExcelFile += " and srv.name Like 'HEAD%' ";
		}

		sqlExcelFile += " ORDER BY Name";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlExcelFile, null);
	        ResultSet rs = stmt.executeQuery();
	        int index = 0;
	        this.ExcelFile.addItem("");
	        while(rs.next())
	        {
	        	this.ExcelFile.addItem(rs.getString("Name"));
	        	index++;
	        	if ( rs.getString("Name").equals(srvdefault))
	        		this.ExcelFile.setSelectedIndex( index);
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "fillCombobox()", e);
	    }
	    
	}

	String m_currentExcel = null;
	String m_currentTab = null;
	
	private void fillComboboxExcelTab( String srv, String currentTab )
	{	
		ExcelTab.removeAllItems();
		
        if ( srv == null)
        	return;
 
/*
        if ( srv.equals("HEADCOUNT"))
        {
        	this.ExcelTab.addItem("Headcount$");
       		this.ExcelTab.setSelectedIndex( 0);
       		return;
        }

        if ( srv.equals("BUDGET"))
        {
        	this.ExcelTab.addItem("Budget$");
       		this.ExcelTab.setSelectedIndex( 0);
       		return;
        }
*/
        
		String sqlExcelTab = "exec dbo.info_linkedserver '" + srv + "' "; 
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlExcelTab, null);
	        ResultSet rs = stmt.executeQuery();
	        int index = 0;
	        this.ExcelTab.addItem("");
	        while(rs.next())
	        {
	        	this.ExcelTab.addItem(rs.getString("Name"));
	        	index++;
	        	if ( rs.getString("Name").equals(currentTab))
	        		this.ExcelTab.setSelectedIndex( index);
	        }
	        rs.close();
	        stmt.close();
	        
	    }
	    catch (SQLException e)
	    {
			String msg = "*ERREUR* Impossible d'exécuté la requête, vérifier si le fichier excel n'est pas ouvert.";
			JOptionPane.showMessageDialog(null, msg + "\n\n" + e.toString(), "Erreur" ,JOptionPane.ERROR_MESSAGE);
	    	
	        s_log.log(Level.SEVERE, "fillCombobox()", e);
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
	        s_log.log(Level.SEVERE, "fillCombobox()", e);
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
	        s_log.log(Level.SEVERE, "fillCombobox()", e);
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
	        s_log.log(Level.SEVERE, "fillCombobox()", e);
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
	        s_log.log(Level.SEVERE, "fillCombobox()", e);
	    }		    
	    
	    
	    

	    String sqlSales = "select C_SalesRegion_id,Value + ' - ' + Name as Name  from C_salesRegion where IsActive = 'Y'";
	    sqlSales = sqlSales + " And C_salesRegion." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
		+ " ORDER BY Value";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlSales, null);
	        ResultSet rs = stmt.executeQuery();
	        this.fieldsalesRegionFrom.addItem(new KeyNamePair(0,""));
	        this.fieldsalesRegion.addItem(new KeyNamePair(0,""));
	        while(rs.next())
	        {
	        	this.fieldsalesRegionFrom.addItem(new KeyNamePair(rs.getInt("C_SalesRegion_id"), rs.getString("Name")));
	        	this.fieldsalesRegion.addItem(new KeyNamePair(rs.getInt("C_SalesRegion_id"), rs.getString("Name")));
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "fillCombobox()", e);
	    }

	    
	}

	 // Removes the specified column from the table and the associated
    // call data from the table model.
    private void removeColumnAndData(MiniTable  table, int vColIndex) 
    {
    	TableModel model = (TableModel)table.getModel();
        TableColumn col = table.getColumnModel().getColumn(vColIndex);
        int columnModelIndex = col.getModelIndex();
 //       Vector data = model.getDataVector();
//        Vector colIds = model.getColumnIdentifiers();
    
        // Remove the column from the table
        table.removeColumn(col);
    
        // Remove the column header from the table model
 //       colIds.removeElementAt(columnModelIndex);
    
        // Remove the column data
 /*       for (int r=0; r<data.size(); r++) {
            Vector row = (Vector)data.get(r);
            row.removeElementAt(columnModelIndex);
        }
*/        
 //       model.setDataVector(data, colIds);
    
        // Correct the model indices in the TableColumn objects
        // by decrementing those indices that follow the deleted column
        Enumeration e = table.getColumnModel().getColumns();
        for (; e.hasMoreElements(); ) {
            TableColumn c = (TableColumn)e.nextElement();
            if (c.getModelIndex() >= columnModelIndex) {
                c.setModelIndex(c.getModelIndex()-1);
            }
        }
 //       model.fireTableStructureChanged();
    }
    
    // This subclass adds a method to retrieve the columnIdentifiers
    // which is needed to implement the removal of
    // column data from the table model
    class MyDefaultTableModel extends DefaultTableModel {
        public Vector getColumnIdentifiers() {
            return columnIdentifiers;
        }
    }

    private void ResetMiniTable()
    {
		dataPane.getViewport().remove(miniTable);

    	miniTable = new MiniTable();
		dataPane.getViewport().add(miniTable, null);
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
		
		String sqlFrom = " P_Payment P_Payment " +
		"  inner join P_Payment_Gain P_Payment_Gain on P_Payment_Gain.P_Payment_ID = P_Payment.P_Payment_ID" +
		"  inner join P_Payment_Gain_Distribution P_Payment_Gain_Distribution on P_Payment_Gain_Distribution.P_Payment_Gain_ID = P_Payment_Gain.P_Payment_Gain_ID" +
		"  inner join Ad_Org Ad_Org on Ad_Org.Ad_Org_ID = P_Payment_Gain_Distribution.Org_ID" +
		"  inner join P_Year P_Year on P_Year.P_Year_ID = P_Payment.P_Year_ID" +
		"  left outer join C_Activity C_Activity on C_Activity.C_Activity_ID = P_Payment_Gain_Distribution.C_Activity_ID" +
		"  left outer join C_Activity_Trl C_Activity_Trl on C_Activity_Trl.C_Activity_ID = C_Activity.C_Activity_ID and C_Activity_Trl.AD_Language = '" + language.getLocale() + "'" +
		"  left outer join P_Department P_Department on P_Department.P_Department_ID = P_Payment.P_Department_ID" +
		"  left outer join C_SalesRegion C_salesRegion on C_salesRegion.C_SalesRegion_ID = P_Payment_Gain_Distribution.C_SalesRegion_ID" +
		"  inner join P_Employee P_Employee on P_Employee.P_Employee_ID = P_Payment.P_Employee_ID" +
		"  left outer join P_Assignment P_Assignment on P_Assignment.P_Assignment_ID = P_Payment_Gain.P_Assignment_ID" +
		"  left outer join P_Post P_Post on P_Assignment.P_Post_ID = P_Post.P_Post_ID" +
		"  inner join C_ElementValue C_ElementValue on C_ElementValue.C_ElementValue_ID = P_Payment_Gain_Distribution.Account_ID" + 
		"  left outer join C_ElementValue_Trl on C_ElementValue_Trl.C_ElementValue_ID = C_ElementValue.C_ElementValue_ID  and C_ElementValue_Trl.AD_Language = '" + language.getLocale() + "'" +
		"  left outer join P_LongTermLeave on P_LongTermLeave.P_LongTermLeave_ID = P_Employee.P_LongTermLeave_ID " +
		"  left outer join P_Job_Type on P_Job_Type.P_Job_Type_ID = P_Employee.P_Job_Type_ID " +
		"  left outer join P_Workplace P_WorkplaceEmp on P_Payment.P_Workplace_id = P_WorkPlaceEmp.p_workplace_id " +
		"  left outer join C_SalesRegion C_SalesRegionEmp on P_WorkplaceEmp.C_SalesRegion_ID = C_SalesRegionEmp.C_SalesRegion_ID "
		;

		if ( this.ExcelFile != null && this.ExcelTab != null && this.ExcelFile.getSelectedItem() != null && this.ExcelTab.getSelectedItem() != null && ((String)this.ExcelFile.getSelectedItem()).length() != 0 && ((String)this.ExcelTab.getSelectedItem()).length() != 0)
		{
			
			if ((((KeyNamePair)ReportType.getSelectedItem()).getKey()) == 1  )
			{
				sqlFrom += "left outer join [" + (String)ExcelFile.getSelectedItem() +"]...[" + (String)ExcelTab.getSelectedItem() + "] bug on C_ElementValue.value COLLATE SQL_Latin1_General_CP1_CI_AS = bug.[GLAccount] and C_SalesRegion.Value COLLATE SQL_Latin1_General_CP1_CI_AS = Bug.[Ass#Base] " ;
//				sqlFrom += "left outer join [BUDGET]...[Budget$] bug on C_ElementValue.value COLLATE SQL_Latin1_General_CP1_CI_AS = bug.[GLAccount] and C_SalesRegion.Value COLLATE SQL_Latin1_General_CP1_CI_AS = Bug.[Ass#Base] " ;
			}
			else
			{
				sqlFrom += "left outer join [" + (String)ExcelFile.getSelectedItem() + "]...[" + (String)ExcelTab.getSelectedItem() + "] head on head.F4 = cast ( P_Employee.value as int  ) and head.F18 =  cast( C_SalesRegion.Value as int )" ;
//				sqlFrom += "left outer join [HEADCOUNT]...[HEADCOUNT$] head on head.F4 = cast ( P_Employee.value as int  ) and head.F18 =  cast( C_SalesRegion.Value as int )" ;
			}
		}
			
		
//		if ( this.ExcelFile != null && this.ExcelTab != null && this.ExcelFile.getSelectedItem() != null && this.ExcelTab.getSelectedItem() != null && ((String)this.ExcelFile.getSelectedItem()).length() != 0 && ((String)this.ExcelTab.getSelectedItem()).length() != 0) 

//			sqlFrom += "left outer join [" + (String)this.ExcelFile.getSelectedItem() + "]...[" + (String)this.ExcelTab.getSelectedItem() + "] tab on P_Employee.value COLLATE SQL_Latin1_General_CP1_CI_AS = tab.[# Employee] " ;


		/**  prepare MiniTable
		 *
		 */
		m_sql = miniTable.prepareTable(getColumn(),

			//	FROM
				sqlFrom
			,
			
			//	WHERE
			"1=1"
			,
         	//	additional where & order in loadTableInfo() 
			true, "P_Payment");
			;
			
		//
		miniTable.getModel().addTableModelListener(this);
		//
		fieldPeriodCouFrom.setMandatory(true);
		fieldPeriodCouTo.setMandatory(true);

		fieldPeriodAntFrom.setMandatory(true);
		fieldPeriodAntTo.setMandatory(true);

		this.ExcelFile.setMandatory(true);
		this.ExcelTab.setMandatory(true);

		
		//
		m_AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
	}   //  dynInit

	String formula = "";

	private String getFormula01() 
	{
		String formulaRes = "";
		String formula1 = "";
		String formula2 = "";
		String formula3 = "";
		String formula4 = "";
		String formula5 = "";
		String formula6 = "";
		String formula7 = "";
		String formula8 = "";
		String formula9 = "";
		String formula10 = "";
		String formula11 = "";
		String formula12 = "";

		String sql = "SELECT Distinct P_Period_ID FROM P_Period Where P_Period_ID Between " + period_ID11 + " and " + period_ID12; 
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	        	P_Period Period1 = P_Period.get( Env.getCtx(), rs.getInt( "P_Period_ID"), trxName);
	        	if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.JANUARY )
	        		formula1 = "max(isnull(Bug.F7,0)) + ";
	        	if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.FEBRUARY )
	        		formula2 = "max(isnull(Bug.F8,0)) + ";
	        	if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.MARCH )
	        		formula3 = "max(isnull(Bug.F9,0)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.APRIL )
					formula4 = "max(isnull(Bug.F10,0)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.MAY )
					formula5 = "max(isnull(Bug.F11,0)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.JUNE )
					formula6 = "max(isnull(Bug.F12,0)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.JULY )
					formula7 = "max(isnull(Bug.F13,0)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.AUGUST )
					formula8 = "max(isnull(Bug.F14,0)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.SEPTEMBER )
					formula9 = "max(isnull(Bug.F15,0)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.OCTOBER )
					formula10 = "max(isnull(Bug.F16,0)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.NOVEMBER )
					formula11 = "max(isnull(Bug.F17,0)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.DECEMBER )
					formula12 = "max(isnull(Bug.F18,0)) + ";
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "VExpenseReport.getColumn", e);
	    }

		
		
	    formulaRes = formula1 + formula2 + formula3 + formula4 + formula5 + formula6 + formula7 + formula8 + formula9 + formula10 + formula11 + formula12 + "0" ;

	    return formulaRes;
	}

	private String getFormula02() 
	{
		// String formula = "";

		String formulaRes = "";
		String formula1 = "";
		String formula2 = "";
		String formula3 = "";
		String formula4 = "";
		String formula5 = "";
		String formula6 = "";
		String formula7 = "";
		String formula8 = "";
		String formula9 = "";
		String formula10 = "";
		String formula11 = "";
		String formula12 = "";

		String sql = "SELECT Distinct P_Period_ID FROM P_Period Where P_Period_ID Between " + period_ID11 + " and " + period_ID12; 
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	        	P_Period Period1 = P_Period.get( Env.getCtx(), rs.getInt( "P_Period_ID"), trxName);
	        	if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.JANUARY )
	        		formula1 = "max(isnull(Head.F82,0))  + max(isnull(Head.F111,0))  + ";
	        	if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.FEBRUARY )
	        		formula2 = "max(isnull(Head.F83,0))  + max(isnull(Head.F112,0))  + ";
	        	if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.MARCH )
	        		formula3 = "max(isnull(Head.F84,0))  + max(isnull(Head.F113,0))  + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.APRIL )
					formula4 = "max(isnull(Head.F85,0))  + max(isnull(Head.F114,0))  + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.MAY )
					formula5 = "max(isnull(Head.F86,0))  + max(isnull(Head.F115,0))  + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.JUNE )
					formula6 = "max(isnull(Head.F87,0))  + max(isnull(Head.F116,0))  +";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.JULY )
					formula7 = "max(isnull(Head.F88,0))  + max(isnull(Head.F117,0))  + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.AUGUST )
					formula8 = "max(isnull(Head.F89,0))  + max(isnull(Head.F118,0))  + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.SEPTEMBER )
					formula9 = "max(isnull(Head.F90,0))  + max(isnull(Head.F119,0))  + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.OCTOBER )
					formula10 = "max(isnull(Head.F91,0)) + max(isnull(Head.F120,0))  + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.NOVEMBER )
					formula11 = "max(isnull(Head.F92,0)) + max(isnull(Head.F121,0))  + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) -1 == GregorianCalendar.DECEMBER )
					formula12 = "max(isnull(Head.F93,0)) + max(isnull(Head.F122,0))  + ";
/*
 * 	        	if ( TimeUtil.getMonth( Period1.getStartDate()) == GregorianCalendar.JANUARY )
	        		formula1 = "max(Head.F82) + max(rtrim(Head.F111)) + ";
	        	if ( TimeUtil.getMonth( Period1.getStartDate()) == GregorianCalendar.FEBRUARY )
	        		formula2 = "max(Head.F83) + max(rtrim(Head.F112)) + ";
	        	if ( TimeUtil.getMonth( Period1.getStartDate()) == GregorianCalendar.MARCH )
	        		formula3 = "max(Head.F84) + max(rtrim(Head.F113)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) == GregorianCalendar.APRIL )
					formula4 = "max(Head.F85) + max(rtrim(Head.F114)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) == GregorianCalendar.MAY )
					formula5 = "max(Head.F86) + max(rtrim(Head.F115)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) == GregorianCalendar.JUNE )
					formula6 = "max(Head.F87) + max(rtrim(Head.F116)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) == GregorianCalendar.JULY )
					formula7 = "max(Head.F88) + max(rtrim(Head.F117)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) == GregorianCalendar.AUGUST )
					formula8 = "max(Head.F89) + max(rtrim(Head.F118)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) == GregorianCalendar.SEPTEMBER )
					formula9 = "max(Head.F90) + max(rtrim(Head.F119)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) == GregorianCalendar.OCTOBER )
					formula10 = "max(Head.F91) + max(rtrim(Head.F120)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) == GregorianCalendar.NOVEMBER )
					formula11 = "max(Head.F92) + max(rtrim(Head.F121)) + ";
				if ( TimeUtil.getMonth( Period1.getStartDate()) == GregorianCalendar.DECEMBER )
					formula12 = "max(Head.F93) + max(rtrim(Head.F122)) + ";

 */
	        
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "VExpenseReport.getColumn", e);
	    }

		
		
	    formulaRes = formula1 + formula2 + formula3 + formula4 + formula5 + formula6 + formula7 + formula8 + formula9 + formula10 + formula11 + formula12 + "0" ;

	    return formulaRes;
	}

	int[] lPos = {4,5,6,7,8,9,0,0};
	
	
	//Afficher les colonnes en fonctions de l'entreprise
	private ColumnInfo[] getColumn()
	{
/*
		ad_org.value as OrgValue , 
		ad_org.name  as OrgName , 
		c_activity.value  as ActivityValue ,
		c_activity_trl.name as ActivityName ,
		C_SalesRegion.value as salesRegionValue ,
		C_SalesRegion.name as salesRegionName,
		P_Department.value as DepartmentValue,
		P_Department.name as DepartmentName,
		P_Employee.value as EmployeeValue,
		P_Employee.Name as EmployeeName, 
		P_Post.Value as PositionValue,
		P_Post.Name as PositionName,
		C_ElementValue.Value as AccountValue,
		C_ElementValue.Name as AccountName,
		P_Payment_Gain_Distribution.Distribution_Type,
		sum( isnull( case when P_Year.Year = '2007' then P_Payment_Gain_Distribution.Quantity else 0 end, 0)) as Quantity1,
		sum( isnull( case when P_Year.Year = '2007' then P_Payment_Gain_Distribution.Amount else 0 end , 0)) as Amount1,
		sum( isnull( case when P_Year.Year = '2008' then P_Payment_Gain_Distribution.Quantity else 0 end, 0)) as Quantity2,
		sum( isnull( case when P_Year.Year = '2008' then P_Payment_Gain_Distribution.Amount else 0 end , 0)) as Amount2
*/

		setPeriodSelection();

		P_Period Period11 = P_Period.get(Env.getCtx(), period_ID11, trxName);
		P_Period Period21 = P_Period.get(Env.getCtx(), period_ID21, trxName);
		

		ColumnInfo[] Column;
		
		Properties ctx = Env.getCtx();
		// Sommaire
		if ( (((KeyNamePair)ReportType.getSelectedItem()).getKey()) == 1  )
		{
			if ( ((String)this.ExcelFile.getSelectedItem()).length() != 0 && ((String)this.ExcelTab.getSelectedItem()).length() != 0) 
			{
				formula = getFormula01();

				lPos[0] = 5;
				lPos[1] = 6;
				lPos[2] = 7;
				lPos[3] = 8;
				lPos[4] = 9;
				lPos[5] = 11;
				lPos[6] = 10;
				lPos[7] = 12;
				Column = new ColumnInfo[] 
						               {
								//  0..4
								new ColumnInfo(" ", "0", IDColumn.class, false, false, null),
								new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "AD_Org.name", String.class, true, false, null),
								
								new ColumnInfo(Msg.translate(ctx, "C_SalesRegion_ID"), "C_SalesRegion.value As salesRegion  ", String.class, true, false, null),
								new ColumnInfo(Msg.translate(ctx, "C_ElementValue_ID"), "C_ElementValue.Value", String.class, true, false, null),
								new ColumnInfo(Msg.translate(ctx, "Name"), "isNull(C_ElementValue_Trl.Name,C_ElementValue.Name)", String.class, true, false, null),

								new ColumnInfo(Msg.translate(ctx, "Quantity").trim() + " 01", "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + " then P_Payment_Gain_Distribution.Quantity else 0 end, 0)) as Quantity1", BigDecimal.class),
								new ColumnInfo(Msg.translate(ctx, "Quantity").trim() + " 02", "sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Quantity else 0 end, 0)) as Quantity2", BigDecimal.class),
								new ColumnInfo(Msg.translate(ctx, "Difference"),   "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + "  then P_Payment_Gain_Distribution.Quantity else 0 end , 0)) - sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Quantity else 0 end , 0))  as Difference"  , BigDecimal.class),
								new ColumnInfo(Msg.translate(ctx, "Amount").trim() + " 01",   "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0))  as Amount1"  , BigDecimal.class),
								new ColumnInfo(Msg.translate(ctx, "Amount").trim() + " 02",   "sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0))  as Amount2"  , BigDecimal.class),

								new ColumnInfo(Msg.translate(ctx, "Difference"),   "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0)) - sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0))  as Difference"  , BigDecimal.class),
								
								new ColumnInfo("Budget ",   formula  , BigDecimal.class),

								new ColumnInfo(Msg.translate(ctx, "Difference"),   "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0)) - " +  formula + "  as Difference"  , BigDecimal.class),

								new ColumnInfo(" ", "0", IDColumn.class, false, false, null)
								};
				
				
			}
			else
			{
				lPos[0] = 5;
				lPos[1] = 6;
				lPos[2] = 7;
				lPos[3] = 8;
				lPos[4] = 9;
				lPos[5] = 10;
				lPos[6] = 0;
				lPos[7] = 0;

				
				Column = new ColumnInfo[] 
						               {
								//  0..4
//						new ColumnInfo(" ", "C_ElementValue.C_ElementValue_ID", IDColumn.class, false, false, null),
						new ColumnInfo(" ", "0", IDColumn.class, false, false, null),
						new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "AD_Org.name", String.class, true, false, null),
						new ColumnInfo(Msg.translate(ctx, "C_SalesRegion_ID"), "C_SalesRegion.value As salesRegion  ", String.class, true, false, null),
						new ColumnInfo(Msg.translate(ctx, "C_ElementValue_ID"), "C_ElementValue.Value", String.class, true, false, null),
						new ColumnInfo(Msg.translate(ctx, "Name"), "isNull(C_ElementValue_Trl.Name,C_ElementValue.Name)", String.class, true, false, null),

						new ColumnInfo(Msg.translate(ctx, "Quantity") + " 01", "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + " then P_Payment_Gain_Distribution.Quantity else 0 end, 0)) as Quantity1", BigDecimal.class),
						new ColumnInfo(Msg.translate(ctx, "Quantity")+ " 02" , "sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Quantity else 0 end, 0)) as Quantity2", BigDecimal.class),
						new ColumnInfo(Msg.translate(ctx, "Difference"),   "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + "  then P_Payment_Gain_Distribution.Quantity else 0 end , 0)) - sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Quantity else 0 end , 0))  as Difference"  , BigDecimal.class),
						new ColumnInfo(Msg.translate(ctx, "Amount") + " 01",   "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0))  as Amount1"  , BigDecimal.class),
						new ColumnInfo(Msg.translate(ctx, "Amount")+ " 02",   "sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0))  as Amount2"  , BigDecimal.class),
						new ColumnInfo(Msg.translate(ctx, "Difference"),   "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0)) - sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0))  as Difference"  , BigDecimal.class),
//						new ColumnInfo(" ", "C_ElementValue.C_ElementValue_ID", IDColumn.class, false, false, null)
						new ColumnInfo(" ", "0", IDColumn.class, false, false, null)
								
								};
				
				
			}
			
		}
		else
		{
			if ( ((String)this.ExcelFile.getSelectedItem()).length() != 0 && ((String)this.ExcelTab.getSelectedItem()).length() != 0) 
			{
				formula = getFormula02();

				lPos[0] = 11;
				lPos[1] = 12;
				lPos[2] = 13;
				lPos[3] = 14;
				lPos[4] = 15;
				lPos[5] = 17;
				lPos[6] = 16;
				lPos[7] = 18;

				Column = new ColumnInfo[] 
						               {
								//  0..4
								new ColumnInfo(" ", "P_Payment.P_Employee_ID", IDColumn.class, false, false, null),
								new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "AD_Org.name", String.class, true, false, null),
								new ColumnInfo(Msg.translate(ctx, "C_Activity_ID"), "isNull(C_Activity.value + '-' + C_Activity_Trl.name,C_Activity.value + '-' + C_Activity.name)", String.class, true, false, null),

								new ColumnInfo(Msg.translate(ctx, "P_Department_ID"), "P_Department.value + '-' + P_Department.name" , String.class, true, false, null),
								new ColumnInfo(Msg.translate(ctx, "P_Employee_ID"), "P_Employee.value", String.class, true, false, null),
								new ColumnInfo(Msg.translate(ctx, "Name"), "P_Employee.Name", String.class, true, false, null),

								new ColumnInfo(Msg.translate(ctx, "P_LongTermLeave_ID"), "max(P_LongTermLeave.Name)", String.class, true, false, null),
								new ColumnInfo(Msg.translate(ctx, "P_Job_Type_ID"), "max(P_Job_Type.Name)", String.class, true, false, null),

								new ColumnInfo(Msg.translate(ctx, "C_SalesRegion_ID"), "C_SalesRegion.value As salesRegion  ", String.class, true, false, null),
								//new ColumnInfo(Msg.translate(ctx, "C_ElementValue_ID"), "' '", String.class, true, false, null),
								//new ColumnInfo(Msg.translate(ctx, "Name"), "' '", String.class, true, false, null),

								new ColumnInfo(Msg.translate(ctx, "C_ElementValue_ID"), "C_ElementValue.Value", String.class, true, false, null),
								new ColumnInfo(Msg.translate(ctx, "Name"), "isNull(C_ElementValue_Trl.Name,C_ElementValue.Name)", String.class, true, false, null),

								new ColumnInfo(Msg.translate(ctx, "Quantity") + " 01", "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + " then P_Payment_Gain_Distribution.Quantity else 0 end, 0)) as Quantity1", BigDecimal.class),
								new ColumnInfo(Msg.translate(ctx, "Quantity") + " 02", "sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Quantity else 0 end, 0)) as Quantity2", BigDecimal.class),
								new ColumnInfo(Msg.translate(ctx, "Difference"),   "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + "  then P_Payment_Gain_Distribution.Quantity else 0 end , 0)) - sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Quantity else 0 end , 0))  as Difference"  , BigDecimal.class),
								new ColumnInfo(Msg.translate(ctx, "Amount") + " 01",   "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0))  as Amount1"  , BigDecimal.class),
								new ColumnInfo(Msg.translate(ctx, "Amount") + " 02",   "sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0))  as Amount2"  , BigDecimal.class),
								new ColumnInfo(Msg.translate(ctx, "Difference"),   "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0)) - sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0))  as Difference"  , BigDecimal.class),
								new ColumnInfo("Budget",   formula  , BigDecimal.class),
								new ColumnInfo(Msg.translate(ctx, "Difference"),   "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0)) - " +  formula + "  as Difference"  , BigDecimal.class),
								new ColumnInfo("Step/Echelon 01",   "max([dbo].[fn_getEmployeeStep]( P_Payment.P_Employee_ID, " + DB.TO_DATE( Period11.getStartDate()) + " ))"  , String.class),
								new ColumnInfo("Step/Echelon 02",   "max([dbo].[fn_getEmployeeStep]( P_Payment.P_Employee_ID, " + DB.TO_DATE( Period21.getStartDate()) + " ))"  , String.class),

								new ColumnInfo(" ", "P_Payment.P_Employee_ID", IDColumn.class, false, false, null)
								};
				
				
			}
			else
			{

				lPos[0] = 11;
				lPos[1] = 12;
				lPos[2] = 13;
				lPos[3] = 14;
				lPos[4] = 15;
				lPos[5] = 16;
				lPos[6] = 0;
				lPos[7] = 0;

				Column = new ColumnInfo[] 
						               {
								//  0..4
								new ColumnInfo(" ", "P_Payment.P_Employee_ID", IDColumn.class, false, false, null),
								new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "AD_Org.name", String.class, true, false, null),
								new ColumnInfo(Msg.translate(ctx, "C_Activity_ID"), "isNull(C_Activity.value + '-' + C_Activity_Trl.name,C_Activity.value + '-' + C_Activity.name)", String.class, true, false, null),

								new ColumnInfo(Msg.translate(ctx, "P_Department_ID"), "P_Department.value + '-' + P_Department.name" , String.class, true, false, null),
								new ColumnInfo(Msg.translate(ctx, "P_Employee_ID"), "P_Employee.value", String.class, true, false, null),
								new ColumnInfo(Msg.translate(ctx, "Name"), "P_Employee.Name", String.class, true, false, null),

								new ColumnInfo(Msg.translate(ctx, "P_LongTermLeave_ID"), "max(P_LongTermLeave.Name)", String.class, true, false, null),
								new ColumnInfo(Msg.translate(ctx, "P_Job_Type_ID"), "max(P_Job_Type.Name)", String.class, true, false, null),

								new ColumnInfo(Msg.translate(ctx, "C_SalesRegion_ID"), "C_SalesRegion.value As salesRegion  ", String.class, true, false, null),
								//new ColumnInfo(Msg.translate(ctx, "C_ElementValue_ID"), "' '", String.class, true, false, null),
								//new ColumnInfo(Msg.translate(ctx, "Name"), "' '", String.class, true, false, null),
								new ColumnInfo(Msg.translate(ctx, "C_ElementValue_ID"), "C_ElementValue.Value", String.class, true, false, null),
								new ColumnInfo(Msg.translate(ctx, "Name"), "isNull(C_ElementValue_Trl.Name,C_ElementValue.Name)", String.class, true, false, null),

								new ColumnInfo(Msg.translate(ctx, "Quantity")+ " 01", "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + " then P_Payment_Gain_Distribution.Quantity else 0 end, 0)) as Quantity1", BigDecimal.class),
								new ColumnInfo(Msg.translate(ctx, "Quantity")+ " 02", "sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Quantity else 0 end, 0)) as Quantity2", BigDecimal.class),
								new ColumnInfo(Msg.translate(ctx, "Difference"),   "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + "  then P_Payment_Gain_Distribution.Quantity else 0 end , 0)) - sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Quantity else 0 end , 0))  as Difference"  , BigDecimal.class),
								new ColumnInfo(Msg.translate(ctx, "Amount")+ " 01",   "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0))  as Amount1"  , BigDecimal.class),
								new ColumnInfo(Msg.translate(ctx, "Amount")+ " 02",   "sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0))  as Amount2"  , BigDecimal.class),
								new ColumnInfo(Msg.translate(ctx, "Difference"),   "sum( isnull( case when P_Payment.P_Period_ID " + sql1 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0)) - sum( isnull( case when P_Payment.P_Period_ID " + sql2 + "  then P_Payment_Gain_Distribution.Amount else 0 end , 0))  as Difference"  , BigDecimal.class),

								new ColumnInfo("Step/Echelon 01",   "max([dbo].[fn_getEmployeeStep]( P_Payment.P_Employee_ID, " + DB.TO_DATE( Period11.getStartDate()) + " ))"  , String.class),
								new ColumnInfo("Step/Echelon 02",   "max([dbo].[fn_getEmployeeStep]( P_Payment.P_Employee_ID, " + DB.TO_DATE( Period21.getStartDate()) + " ))"  , String.class),

								new ColumnInfo(" ", "P_Payment.P_Employee_ID", IDColumn.class, false, false, null)
								};
				
				
			}
			
		}
		
		return Column;
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
		catch (SQLException e)
		{
			String msg = "*ERREUR* Impossible d'exécuté la requête, vérifier si le fichier excel n'est pas ouvert.";
			JOptionPane.showMessageDialog(null, msg + "\n\n" + e.toString(), "Erreur" ,JOptionPane.ERROR_MESSAGE);


			s_log.saveError("Error", "Impossible d'exécuté la requête, vérifier si le fichier excel n'est pas ouvert.");
			s_log.log(Level.SEVERE,"VExpenseReport.loadTableInfo", e);
		}
		calculateSelection();
	}   //  loadTableInfo
	
	private PreparedStatement prepareStatement() throws SQLException
	{
		//  not yet initialized
		if (m_sql == null)
			return null;

		String sql =  m_sql;
		//  Parameters
/*
		Integer selectionDateFrom = new Integer(((KeyNamePair)fieldPeriodCouFrom.getSelectedItem()).getKey());
		Integer selectionDateTo = new Integer(((KeyNamePair)fieldPeriodCouTo.getSelectedItem()).getKey());
		miniTable.setColorCompare(selectionDateFrom);
		miniTable.setColorCompare(selectionDateTo);

		
		Integer selectionDateFrom2 = new Integer(((KeyNamePair)fieldPeriodAntFrom.getSelectedItem()).getKey());
		Integer selectionDateTo2 = new Integer(((KeyNamePair)fieldPeriodAntTo.getSelectedItem()).getKey());
		miniTable.setColorCompare(selectionDateFrom2);
		miniTable.setColorCompare(selectionDateTo2);
*/
		//
//		if (onlyDue.isSelected())
//			sql += " AND i.DateInvoiced+p.NetDays <= ?";
		//

		int P_Employee_ID = 0;
		int C_ElementValue_ID = 0;

		Object value = fieldEmployee.getValue();
		if (value != null && value.toString().length() > 0)
		{
			P_Employee_ID = Integer.parseInt( value.toString() );
		}

		if(this.fieldElementValue.getValue() != null || this.fieldElementValueTxt1.getValue() != null || this.fieldElementValueTxt2.getValue() != null || this.fieldElementValueTxt3.getValue() != null || this.fieldElementValueTxt4.getValue() != null )
		{
			if ( this.fieldElementValue.getValue() != null )
			{
				C_ElementValue_ID = ((Number)this.fieldElementValue.getValue()).intValue();
				sql += " AND  C_ElementValue.C_ElementValue_ID = " + C_ElementValue_ID ; 
			}		
			else if ( this.fieldElementValueTxt1.getValue().toString().length() > 1
					  || this.fieldElementValueTxt2.getValue().toString().length() > 1
					  || this.fieldElementValueTxt3.getValue().toString().length() > 1
					  || this.fieldElementValueTxt4.getValue().toString().length() > 1
					)
			{	
				sql += " AND ( ";
				if (  this.fieldElementValueTxt1.getValue().toString().length() > 1)
					sql +=	"C_ElementValue.Value like '" + this.fieldElementValueTxt1.getValue() + "'";

				if (  this.fieldElementValueTxt2.getValue().toString().length() > 1)
					sql += " OR C_ElementValue.Value like '" + this.fieldElementValueTxt2.getValue() + "'" ;
				if (  this.fieldElementValueTxt3.getValue().toString().length() > 1)
					sql += " OR C_ElementValue.Value like '" + this.fieldElementValueTxt3.getValue() + "'" ;
				if (  this.fieldElementValueTxt4.getValue().toString().length() > 1)
					sql += " OR C_ElementValue.Value like '" + this.fieldElementValueTxt4.getValue() + "'"; 
				sql += " ) ";
			}
		}
	

		if (P_Employee_ID != 0)
			sql += " AND P_Employee.P_Employee_ID = " + P_Employee_ID;

		if(checkIsActive.isSelected() == true)
			sql += " AND  P_Employee.IsActive = 'Y'";

		int Org_id = Integer.parseInt(fieldOrg.getValue().toString());
		if (Org_id != 0)
		{
			sql += " and P_Payment_Gain_Distribution.ad_Org_id =  " + Org_id;
		}
		
		int Activity_id = Integer.parseInt(fieldActivity.getValue().toString());	
		if (Activity_id != 0)
		{
			sql += " and P_Payment_Gain_Distribution.C_Activity_id =  " + Activity_id;
		}
		
		int Department_id = Integer.parseInt(fieldDept.getValue().toString());	
		if (Department_id != 0)
		{
			sql += " and P_Payment.P_Department_id =  " + Department_id;
		}

		int Payment_Group_id = Integer.parseInt(fieldGroupPayment.getValue().toString());	
		if (Payment_Group_id != 0)
		{
			sql += " and P_Payment.P_Payment_Group_id = " + Payment_Group_id;
		}		
		int fromSalesRegion_id = Integer.parseInt(fieldsalesRegionFrom.getValue().toString());	
		int salesRegion_id = Integer.parseInt(fieldsalesRegion.getValue().toString());	

		if( ! checkAssignBaseEmp.isSelected() == true )
		{
			if ( fromSalesRegion_id != 0 && salesRegion_id != 0 )
			{
				MSalesRegion s1 = new MSalesRegion( Env.getCtx(), fromSalesRegion_id, trxName );  
				MSalesRegion s2 = new MSalesRegion( Env.getCtx(), salesRegion_id, trxName );  
				sql += " and C_SalesRegionEmp.Value Between '" + s1.getValue() + "' AND '" + s2.getValue() + "'";
			}
			else if (salesRegion_id != 0 )
			{
				sql += " and p_workplaceEmp.C_SalesRegion_id =  " + salesRegion_id;
			}
			else if (fromSalesRegion_id != 0 )
			{
				sql += " and p_workplaceEmp.C_SalesRegion_id =  " + fromSalesRegion_id;
			}
			
		}
		else
		{
			if ( fromSalesRegion_id != 0 && salesRegion_id != 0 )
			{
				MSalesRegion s1 = new MSalesRegion( Env.getCtx(), fromSalesRegion_id, trxName );  
				MSalesRegion s2 = new MSalesRegion( Env.getCtx(), salesRegion_id, trxName );  
				sql += " and C_SalesRegion.Value Between '" + s1.getValue() + "' AND '" + s2.getValue() + "'";
			}
			else if (salesRegion_id != 0 )
			{
				sql += " and P_Payment_Gain_Distribution.C_SalesRegion_id =  " + salesRegion_id;
			}
			else if (fromSalesRegion_id != 0 )
			{
				sql += " and P_Payment_Gain_Distribution.C_SalesRegion_id =  " + fromSalesRegion_id;
			}
			
		}

		
		
		sql += " and ( P_Payment.P_Period_ID " + sql1  ;
		sql += "     or  P_Payment.P_Period_ID " + sql2 + ")";  

		// Sommaire
		if ( (((KeyNamePair)ReportType.getSelectedItem()).getKey()) == 1  )
			sql += 
				" group by" +
				" P_Payment_Gain_Distribution.C_SalesRegion_ID," +
				" ad_org.value , " +
				" ad_org.name   ," + 
				" C_SalesRegion.value  ," +
				" C_SalesRegion.name ," +
				" C_ElementValue.C_ElementValue_ID ," +
				" C_ElementValue.Value ," +
				" C_ElementValue.Name ," +
				" C_ElementValue_Trl.Name " 
				;
		else
		{
			sql += 
				" group by" +
				" P_Payment_Gain_Distribution.AD_Org_ID," +
				" P_Payment.P_Employee_ID," +
				" P_Payment_Gain_Distribution.C_Activity_ID," +
				" P_Payment.P_Department_ID," +
				" P_Payment_Gain_Distribution.C_SalesRegion_ID," +
				" P_Assignment.P_Post_ID," +
				" P_Payment_Gain_Distribution.Account_ID," +
				" ad_org.value , " +
				" ad_org.name   ," + 
				" c_activity.value  ," +
				" c_activity.name  ," +
				" c_activity_trl.name  ," +
				" C_SalesRegion.value  ," +
				" C_SalesRegion.name ," +
				" P_Department.value ," +
				" P_Department.name ," +
				" P_Employee.value ," +
				" P_Employee.Name , " +
				" P_Post.Value ," +
				" P_Post.Name ," +
				" C_ElementValue.C_ElementValue_ID ," +
				" C_ElementValue.Value ," +
				" C_ElementValue.Name ," +
				" C_ElementValue_Trl.Name ," +
				" P_Payment_Gain_Distribution.Distribution_Type" 
				;
				
//				sql += " ORDER BY C_SalesRegion.value, C_ElementValue.Value, P_Employee.value";
		}
		
		if ( this.ExcelFile != null && this.ExcelTab != null 
				&& this.ExcelFile.getSelectedItem() != null 
				&& this.ExcelTab.getSelectedItem() != null 
				&& ((String)this.ExcelFile.getSelectedItem()).length() != 0 
				&& ((String)this.ExcelTab.getSelectedItem()).length() != 0)
		{
			if ((((KeyNamePair)ReportType.getSelectedItem()).getKey()) == 1  )
			{
				sql += " UNION SELECT ";
				sql += " 0, "; // C_ElementValue.C_ElementValue_ID
				sql += " substring( cast( Division as varchar ), 0, 5 ) Division , ";
				sql += " cast( Bug.[Ass#Base] as varchar ) , " ; // salesRegion
				sql += " cast( cast( bug.[GLAccount] as int ) as varchar ), " ;
				sql += " max( C_ElementValue.Name ) , " ; // ElementValue.Value + Name
				sql += "0,0,0,0,0,0,  " ; // Quantity 01, Quantity 02, diff, Amount 01, Amount 02, diff
				sql += formula ; // Budget
				sql += ", ( " + formula + " ) * -1, 0 " ; // difference
				
//				sql += " from [BUDGET]...[Budget$] bug ";
				sql += " from [" + (String)ExcelFile.getSelectedItem() +"]...[" + (String)ExcelTab.getSelectedItem() + "] bug ";
				sql += " left outer join C_ElementValue on value = cast( cast( bug.[GLAccount] as int ) as varchar ) ";
				sql += " where 1=1 ";	
//				sql += " where not exists( select 1 from ";
		
//				sql  += " P_Payment P_Payment " +
//					"  inner join P_Payment_Gain P_Payment_Gain on P_Payment_Gain.P_Payment_ID = P_Payment.P_Payment_ID" +
//					"  inner join P_Payment_Gain_Distribution P_Payment_Gain_Distribution on P_Payment_Gain_Distribution.P_Payment_Gain_ID = P_Payment_Gain.P_Payment_Gain_ID" +
//					"  left outer join C_SalesRegion C_SalesRegion on C_SalesRegion.C_SalesRegion_ID = P_Payment_Gain_Distribution.C_SalesRegion_ID" +
//					"  inner join C_ElementValue C_ElementValue on C_ElementValue.C_ElementValue_ID = P_Payment_Gain_Distribution.Account_ID" + 
//					"  where C_ElementValue.value COLLATE SQL_Latin1_General_CP1_CI_AS = cast ( bug.[GLAccount] as varchar ) " ;
					
//					sql += "  and C_SalesRegion.Value COLLATE SQL_Latin1_General_CP1_CI_AS = cast( Bug.[Ass#Base] as varchar ) " ;
					
//					sql += " ) "; 
					
					s_log.log( Level.INFO, sql);
// ici				cast( cast( bug.[GLAccount] as int ) as varchar )

				//if (P_Employee_ID != 0)
				//	sql += " AND P_Employee.P_Employee_ID = " + P_Employee_ID;

				if (Org_id != 0)
				{
					MOrg Org = new MOrg( Env.getCtx(), Org_id, trxName);
					sql += " and substring( cast( Division as varchar ), 0, 5 )  =  '" + Org.getValue() + "'";
				}


				if (this.fieldElementValue.getValue() != null || this.fieldElementValueTxt1.getValue().toString().length() > 1 || this.fieldElementValueTxt2.getValue().toString().length() > 1 || this.fieldElementValueTxt3.getValue().toString().length() > 1 || this.fieldElementValueTxt4.getValue().toString().length() > 1 )
				{
					sql += " AND ( ";

					if ( this.fieldElementValue.getValue() != null )
					{
						C_ElementValue_ID = ((Number)this.fieldElementValue.getValue()).intValue();
						sql += " C_ElementValue.C_ElementValue_ID = " + C_ElementValue_ID ; 
					}		
					else if ( this.fieldElementValueTxt1.getValue().toString().length() > 1
							  || this.fieldElementValueTxt2.getValue().toString().length() > 1
							  || this.fieldElementValueTxt3.getValue().toString().length() > 1
							  || this.fieldElementValueTxt4.getValue().toString().length() > 1
							)
					{	

					    if (  this.fieldElementValueTxt1.getValue().toString().length() > 1)
							sql +=	"C_ElementValue.Value like '" + this.fieldElementValueTxt1.getValue() + "'";

						if (  this.fieldElementValueTxt2.getValue().toString().length() > 1)
							sql += " OR C_ElementValue.Value like '" + this.fieldElementValueTxt2.getValue() + "'" ;
						if (  this.fieldElementValueTxt3.getValue().toString().length() > 1)
							sql += " OR C_ElementValue.Value like '" + this.fieldElementValueTxt3.getValue() + "'" ;
						if (  this.fieldElementValueTxt4.getValue().toString().length() > 1)
							sql += " OR C_ElementValue.Value like '" + this.fieldElementValueTxt4.getValue() + "'";
					}

					if (this.fieldBElementValue.getValue() != null || this.fieldBElementValueTxt1.getValue().toString().length() > 1 || this.fieldBElementValueTxt2.getValue().toString().length() > 1 || this.fieldBElementValueTxt3.getValue().toString().length() > 1 || this.fieldBElementValueTxt4.getValue().toString().length() > 1 )
					{
						sql += " OR ";
					}
				}

				
				if (this.fieldBElementValue.getValue() != null || this.fieldBElementValueTxt1.getValue().toString().length() > 1 || this.fieldBElementValueTxt2.getValue().toString().length() > 1 || this.fieldBElementValueTxt3.getValue().toString().length() > 1 || this.fieldBElementValueTxt4.getValue().toString().length() > 1 )
				{

					if ( ! ( this.fieldElementValue.getValue() != null || this.fieldElementValueTxt1.getValue().toString().length() > 1 || this.fieldElementValueTxt2.getValue().toString().length() > 1 || this.fieldElementValueTxt3.getValue().toString().length() > 1 || this.fieldElementValueTxt4.getValue().toString().length() > 1 ))
						sql += " AND ";
					
					if ( this.fieldBElementValue.getValue() != null )
					{
						C_ElementValue_ID = ((Number)this.fieldBElementValue.getValue()).intValue();
						sql += " C_ElementValue.C_ElementValue_ID = " + C_ElementValue_ID ; 
					}		
					else if ( this.fieldBElementValueTxt1.getValue().toString().length() > 1
							  || this.fieldBElementValueTxt2.getValue().toString().length() > 1
							  || this.fieldBElementValueTxt3.getValue().toString().length() > 1
							  || this.fieldBElementValueTxt4.getValue().toString().length() > 1
							)
					{	

					    if (  this.fieldBElementValueTxt1.getValue().toString().length() > 1)
							sql +=	" C_ElementValue.Value like '" + this.fieldBElementValueTxt1.getValue() + "'";

						if (  this.fieldBElementValueTxt2.getValue().toString().length() > 1)
							sql += " OR C_ElementValue.Value like '" + this.fieldBElementValueTxt2.getValue() + "'" ;
						if (  this.fieldBElementValueTxt3.getValue().toString().length() > 1)
							sql += " OR C_ElementValue.Value like '" + this.fieldBElementValueTxt3.getValue() + "'" ;
						if (  this.fieldBElementValueTxt4.getValue().toString().length() > 1)
							sql += " OR C_ElementValue.Value like '" + this.fieldBElementValueTxt4.getValue() + "'";
					}
				}
				
				if (this.fieldElementValue.getValue() != null || this.fieldElementValueTxt1.getValue().toString().length() > 1 || this.fieldElementValueTxt2.getValue().toString().length() > 1 || this.fieldElementValueTxt3.getValue().toString().length() > 1 || this.fieldElementValueTxt4.getValue().toString().length() > 1 )

				{
					sql += "  ) ";
				}
				

				//if (Activity_id != 0)
				//{
				//	sql += " and P_Payment.C_Activity_id =  " + Activity_id;
				//}
				
				//if (Department_id != 0)
				//{
				//	sql += " and P_Payment.P_Department_id =  " + Department_id;
				//}
/*
				if (salesRegion_id != 0)
				{
					MSalesRegion salesRegion = new MSalesRegion( Env.getCtx(), salesRegion_id, trxName);
					sql += " and cast( Bug.[Ass#Base] as varchar ) =  '" + salesRegion.getValue() + "'";
				}
*/
				if ( fromSalesRegion_id != 0 && salesRegion_id != 0 )
				{
					MSalesRegion s1 = new MSalesRegion( Env.getCtx(), fromSalesRegion_id, trxName );  
					MSalesRegion s2 = new MSalesRegion( Env.getCtx(), salesRegion_id, trxName );  
					sql += " and cast( Bug.[Ass#Base] as varchar ) Between '" + s1.getValue() + "' AND '" + s2.getValue() + "'";
				}
				else if (salesRegion_id != 0)
				{
					MSalesRegion salesRegion = new MSalesRegion( Env.getCtx(), salesRegion_id, trxName);
					sql += " and cast( Bug.[Ass#Base] as varchar ) =  '" + salesRegion.getValue() + "'";
				}
				else if (fromSalesRegion_id != 0)
				{
					MSalesRegion salesRegion = new MSalesRegion( Env.getCtx(), fromSalesRegion_id, trxName);
					sql += " and cast( Bug.[Ass#Base] as varchar ) =  '" + salesRegion.getValue() + "'";
				}

					
				sql +=	" group by bug.Division, Bug.[Ass#Base], bug.[GLAccount] " 
					;
			}
			else
			{
				String sqlSub = "Select distinct P_Payment.P_Employee_ID " + sql.substring( sql.indexOf(" FROM ") );
				s_log.log( Level.INFO, sqlSub);
				
				P_Period Period11 = P_Period.get(Env.getCtx(), period_ID11, trxName);
				P_Period Period21 = P_Period.get(Env.getCtx(), period_ID21, trxName);


				sql += " UNION ( SELECT ";
				sql += " P_Employee.P_Employee_ID, "; // C_ElementValue.C_ElementValue_ID
				sql += "max( AD_Org.Name ), "; // org.name
				sql += "substring( head.F12,2,4) COLLATE SQL_Latin1_General_CP1_CI_AS + '-' + max( C_Activity.Name ), "; // activity.name
				sql += "substring( head.F12,1,1) COLLATE SQL_Latin1_General_CP1_CI_AS + '-' + max( P_Department.Name ), "; // department
				sql += " dbo.lpad( cast( head.F4 as varchar), 6, '0' ) COLLATE SQL_Latin1_General_CP1_CI_AS, " ; // Employee.value
				sql += " MAX( [Canadian Helicopters Limited] ) COLLATE SQL_Latin1_General_CP1_CI_AS, " ; // Employee.Name
				sql += " max( P_LongTermLeave.Name ) ,  "; 
				sql += " max( P_Job_Type.Name ),  " ;
				sql += " dbo.lpad(cast( head.F18  as varchar ), 3, '0' ) COLLATE SQL_Latin1_General_CP1_CI_AS, " ; // salesRegion
				sql += " ' ', ' ', " ; // ElementValue.Value + Name
				
				sql += "0,0,0,0,0,0,  " ; // Quantity 01, Amount 01, Quantity 02, Amount 02, diff
				sql += formula ; // Budget
				sql += ", ( " + formula + " ) * -1,  max([dbo].[fn_getEmployeeStep]( P_Employee.P_Employee_ID, " + DB.TO_DATE( Period11.getStartDate()) + " ))" ;
				sql += ", max([dbo].[fn_getEmployeeStep]( P_Employee.P_Employee_ID, " + DB.TO_DATE( Period21.getStartDate()) + " )), P_Employee.P_Employee_ID " ; 


				
//  				sql += " from [HEADCOUNT]...[HEADCOUNT$] head " +
				sql += " from [" + (String)ExcelFile.getSelectedItem() + "]...[" + (String)ExcelTab.getSelectedItem() + "] head " +
				       " left outer join P_Employee P_Employee on P_Employee.value = dbo.lpad( cast( head.F4 as varchar), 6, '0' ) " +
				       " left outer join AD_Org AD_Org on P_Employee.AD_Org_ID = AD_Org.AD_Org_ID " +
					   " left outer join P_Department P_Department on P_Department.Value = substring( head.F12,1,1) COLLATE SQL_Latin1_General_CP1_CI_AS " +
					   " left outer join C_Activity C_Activity on C_Activity.Value = substring( head.F12,2,4) COLLATE SQL_Latin1_General_CP1_CI_AS " +
					   "  left outer join P_LongTermLeave on P_LongTermLeave.P_LongTermLeave_ID = P_Employee.P_LongTermLeave_ID " +
					   "  left outer join P_Job_Type on P_Job_Type.P_Job_Type_ID = P_Employee.P_Job_Type_ID " +

					   				   
					   " where 1=1 "
					   ;
//					  "where not exists( select 1 from ";
//				sql  += " P_Payment P_Payment " +
//					" inner join P_Employee P_Employee on P_Employee.P_Employee_ID = P_Payment.P_Employee_ID " +
//					"  inner join P_Payment_Gain P_Payment_Gain on P_Payment_Gain.P_Payment_ID = P_Payment.P_Payment_ID" +
//					"  inner join P_Payment_Gain_Distribution P_Payment_Gain_Distribution on P_Payment_Gain_Distribution.P_Payment_Gain_ID = P_Payment_Gain.P_Payment_Gain_ID" +
//					"  left outer join C_SalesRegion C_SalesRegion on C_SalesRegion.C_SalesRegion_ID = P_Payment_Gain_Distribution.C_SalesRegion_ID" +
//					"  inner join C_ElementValue C_ElementValue on C_ElementValue.C_ElementValue_ID = P_Payment_Gain_Distribution.Account_ID" + 
//				"  where dbo.lpad( cast( head.F4 as varchar), 6, '0' ) =  P_Employee.value  " +
//					" and dbo.lpad(cast( head.F18  as varchar ), 3, '0' ) =   C_SalesRegion.Value  " +
//					" ) " +
				sql +=	" and len( ltrim( cast( head.F2 as varchar))) <> 0 " ;
				
				if (P_Employee_ID != 0)
				{
					P_Employee Employee = P_Employee.get( Env.getCtx(), P_Employee_ID, trxName);
					sql += " AND dbo.lpad( cast( head.F4 as varchar), 6, '0' ) = '" + Employee.getValue() + "'";
					
				}

				if (Org_id != 0)
				{
					sql += " and AD_Org.AD_Org_ID =  " + Org_id;
				}
				
				if (Activity_id != 0)
				{
					MActivity Activity = new MActivity( Env.getCtx(), Activity_id, trxName);
					sql += " and substring( head.F12,2,4) =  '" + Activity.getValue() + "'";
				}
				
				if (Department_id != 0)
				{
					P_Department Department = P_Department.get( Env.getCtx(),Department_id, trxName);
					sql += " and substring( head.F12,1,1) =  '" + Department.getValue() +"'";
				}

				if ( fromSalesRegion_id != 0 && salesRegion_id != 0 )
				{
					MSalesRegion s1 = new MSalesRegion( Env.getCtx(), salesRegion_id, trxName );  
					MSalesRegion s2 = new MSalesRegion( Env.getCtx(), salesRegion_id, trxName );  
					sql += " and dbo.lpad(cast( head.F18  as varchar ), 3, '0' ) Between '" + s1.getValue() + "' AND '" + s2.getValue() + "'";
				}
				else if (salesRegion_id != 0)
				{
					MSalesRegion salesRegion = new MSalesRegion( Env.getCtx(), salesRegion_id, trxName);
					sql += " and dbo.lpad(cast( head.F18  as varchar ), 3, '0' ) =  '" + salesRegion.getValue() + "'";
				}
				else if (fromSalesRegion_id != 0)
				{
					MSalesRegion salesRegion = new MSalesRegion( Env.getCtx(), fromSalesRegion_id, trxName);
					sql += " and dbo.lpad(cast( head.F18  as varchar ), 3, '0' ) =  '" + salesRegion.getValue() + "'";
				}

				if ( this.fieldBElementValue.getValue() != null )
				{
					C_ElementValue_ID = ((Number)this.fieldBElementValue.getValue()).intValue();
					MElementValue ElementValue = new MElementValue( Env.getCtx(), C_ElementValue_ID, trxName );
					sql += " and head.F12 like '_" + ElementValue.getValue().substring(0,3) + "%'" ; 
				}		
				else if ( this.fieldBElementValueTxt1.getValue().toString().length() > 1
						  || this.fieldBElementValueTxt2.getValue().toString().length() > 1
						  || this.fieldBElementValueTxt3.getValue().toString().length() > 1
						  || this.fieldBElementValueTxt4.getValue().toString().length() > 1
						)
				{	

					sql +=	" and ( ";
				    if (  this.fieldBElementValueTxt1.getValue().toString().length() > 1)
						sql +=	" head.F12 like '_" + this.fieldBElementValueTxt1.getValue().toString().substring(0, 3) + "%'";

					if (  this.fieldBElementValueTxt2.getValue().toString().length() > 1)
						sql += " OR head.F12 like '_" + this.fieldBElementValueTxt2.getValue().toString().substring(0, 3) + "%'" ;
					if (  this.fieldBElementValueTxt3.getValue().toString().length() > 1)
						sql += " OR head.F12 like '_" + this.fieldBElementValueTxt3.getValue().toString().substring(0, 3) + "%'" ;
					if (  this.fieldBElementValueTxt4.getValue().toString().length() > 1)
						sql += " OR head.F12 like '_" + this.fieldBElementValueTxt4.getValue().toString().substring(0, 3) + "%'";

					sql +=	" ) ";
					
				}
				sql += " and P_Employee_ID not in ("  + sqlSub + ")" ;

/*				
				sql += " and not exists( select 1 from p_payment where P_Payment.P_Employee_ID = P_Employee.P_Employee_ID  ";
				
				if (Department_id != 0)
				{
					sql += " and P_Payment.P_Department_id =  " + Department_id;
				}

				if (Payment_Group_id != 0)
				{
					sql += " and P_Payment.P_Payment_Group_id = " + Payment_Group_id;
				}		
				
				sql += " and ( P_Payment.P_Period_ID " + sql1  ;
				sql += "     or  P_Payment.P_Period_ID " + sql2 + ")";  
		
				sql += ")";
*/
						
				sql += " group by head.F12, head.F4, head.F5, head.F18, P_Employee.P_Employee_ID )  " 
					;
			}
		}
	

		s_log.log( Level.INFO, sql);
		PreparedStatement pstmt = DB.prepareStatement(sql, null);

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

	int period_ID11 ;
	int period_ID12 ;

	int period_ID21 ;
	int period_ID22 ;

	String sql1  ;  
	String sql2  ;  

	private void setPeriodSelection()
	{
		if ( fieldPeriodCouFrom == null || fieldPeriodCouFrom.getSelectedItem() == null)
			return; 

		period_ID11 = new Integer(((KeyNamePair)fieldPeriodCouFrom.getSelectedItem()).getKey());
		period_ID12 = new Integer(((KeyNamePair)fieldPeriodCouTo.getSelectedItem()).getKey());

		period_ID21 = new Integer(((KeyNamePair)fieldPeriodAntFrom.getSelectedItem()).getKey());
		period_ID22 = new Integer(((KeyNamePair)fieldPeriodAntTo.getSelectedItem()).getKey());


		if ( period_ID11 != period_ID12 )
			sql1 = " between " + period_ID11 + " and " + period_ID12 ;
		else
			sql1 = " = " + period_ID11 ;

		if ( period_ID21 != period_ID22 )
			sql2 = " between " + period_ID21 + " and " + period_ID22 ;  
		else
			sql2 = " = " + period_ID21 ;
	}
	
	/**
	 *  ActionListener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		setPeriodSelection();

		if(e.getSource() == this.ReportType )
	    {
			this.fillComboboxServers("");
	    }

		
		if ( m_currentExcel != ((String)this.ExcelFile.getSelectedItem()))
		{
			m_currentExcel = ((String)this.ExcelFile.getSelectedItem())	;
			if ( this.ExcelFile != null && this.ExcelFile.getSelectedItem() != null && ((String)this.ExcelFile.getSelectedItem()).length() != 0)
			{
				m_currentTab = (String)this.ExcelTab.getSelectedItem();
				fillComboboxExcelTab( (String)this.ExcelFile.getSelectedItem() , m_currentTab);
			}
			else
			{
				m_currentTab = null;
				fillComboboxExcelTab( null , m_currentTab);
			}
		}
			

		
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

	    else if(e.getSource() == this.bLinkedServerAdd)
	    {
			setBusy( true );
			try
			{
				CreateLikedServer();
			}
			catch (Exception ex)
			{
			    s_log.log(Level.SEVERE, "CreateLikedServer", ex);
			}
			setBusy( false );
	    }

	    
	    
	    else
	    {
	
			if (e.getSource() == bCancel)
				dispose();
	
			else if ( e.getSource() == bRefresh)
			{

			    setBusy( true );

			    ResetMiniTable();
			    dynInit();
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

			        fileWriter.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n");
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
			        fileWriter.write(" <Worksheet ss:Name=\"" + PgiUtil.convertHTMLString(Msg.translate(Env.getCtx(), "C_ElementValue_ID")) + "\">\n");
			        fileWriter.write("  <Table ss:ExpandedColumnCount=\"" + rsMetaData.getColumnCount() + "\">\n");

			        // generation des entêtes de colonnes
			        fileWriter.write("<Row>\n");
			        ColumnInfo[] colInfo = this.getColumn();
			        for(int i = 2; i <= rsMetaData.getColumnCount() ; i++)
			        {
			        	String colName = colInfo[i-1].getColHeader();
			        	if(!colName.endsWith("_ID") && !colName.equals(" "))
			        		fileWriter.write("<Cell><Data ss:Type=\"String\">" + colName + "</Data></Cell>\n");
			        }
			        			        
			        fileWriter.write("</Row>\n");
		            while(rs.next())
			        {
			            // On crée chaque ligne dans le fichier selon le format d'un .csv
			            String buffer = "";
			            fileWriter.write("<Row>\n");
			            for(int i = 2; i <= rsMetaData.getColumnCount(); i++)
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
			                		fileWriter.write("<Cell><Data ss:Type=\"Number\">" + nvl(rs.getString(i)) + "</Data></Cell>\n");
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.Double") )
			                		fileWriter.write("<Cell><Data ss:Type=\"Number\">" + nvl(rs.getString(i)) + "</Data></Cell>\n");
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.sql.Timestamp") )
			                		fileWriter.write("<Cell><Data ss:Type=\"String\">" + rs.getTimestamp(i).toString().substring(0,9) + "</Data></Cell>\n");
			                	else
			                		fileWriter.write("<Cell><Data ss:Type=\"Number\">" + nvl(rs.getString(i)) + "</Data></Cell>\n");
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
			    catch (SQLException e)
			    {
			        s_log.log(Level.SEVERE, "exportToXML", e);
			    }
			    
			    fileWriter.close();

			    org.solstice.util.ExcelExportUtil.postExportSuccess(new File(fileName), this);

		    }
		}
		
	}
	
	
	private String nvl ( String bd )
	{
		if ( bd == null )
			return new String("0");
		else
			return bd;
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
			    catch (SQLException e)
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

//		calculateSelection();

	}   //  valueChanged

	/**
	 *  Calculate selected rows.
	 *  - add up selected rows
	 */
	public void calculateSelection()
	{
		BigDecimal Qty1 = new BigDecimal(0.0);
		BigDecimal Qty2 = new BigDecimal(0.0);
		BigDecimal Amt1 = new BigDecimal(0.0);
		BigDecimal Amt2 = new BigDecimal(0.0);
		BigDecimal Budget = new BigDecimal(0.0);
		BigDecimal dif0 = new BigDecimal(0.0);
		BigDecimal dif1 = new BigDecimal(0.0);
		BigDecimal dif2 = new BigDecimal(0.0);

		int rows = miniTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			Qty1 = Qty1.add((BigDecimal)miniTable.getModel().getValueAt(i, lPos[0]));
			Qty2 = Qty2.add((BigDecimal)miniTable.getModel().getValueAt(i, lPos[1]));
			dif0 = dif1.add((BigDecimal)miniTable.getModel().getValueAt(i, lPos[2]));
			Amt1 = Amt1.add((BigDecimal)miniTable.getModel().getValueAt(i, lPos[3]));
			Amt2 = Amt2.add((BigDecimal)miniTable.getModel().getValueAt(i, lPos[4]));
			if ( miniTable.getModel().getValueAt(i, lPos[5]) != null && lPos[5] != 0)
				Budget = Budget.add((BigDecimal)miniTable.getModel().getValueAt(i, lPos[5]));
			if ( miniTable.getModel().getValueAt(i, lPos[6]) != null && lPos[6] != 0)
				dif1 = dif1.add((BigDecimal)miniTable.getModel().getValueAt(i, lPos[6]));
			if ( miniTable.getModel().getValueAt(i, lPos[7]) != null && lPos[7] != 0)
				dif2 = dif2.add((BigDecimal)miniTable.getModel().getValueAt(i, lPos[7]));
//			else
//				Budget = Env.ZERO;
			m_noSelected++;
		}

		//  Information
		StringBuffer info = new StringBuffer();
		info.append(m_noSelected).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected")).append(" - ");
		info.append(" Quantité : ");  //.append(", ");
		info.append(m_format.format(Qty1));  //.append(", ");
		info.append(" Quantité : ");  //.append(", ");
		info.append(m_format.format(Qty2));  //.append(", ");
		info.append(" Diff : ");  //.append(", ");
		info.append(m_format.format(dif0));  //.append(", ");
		info.append(" Montant : ");  //.append(", ");
		info.append(m_format.format(Amt1));  //.append(", ");
		info.append(" Montant : ");  //.append(", ");
		info.append(m_format.format(Amt2));  //.append(", ");

		info.append(" Diff : ");  //.append(", ");
		info.append(m_format.format(dif1));  //.append(", ");

		info.append(" Budget : ");  //.append(", ");
		info.append(m_format.format(Budget));  //.append(", ");

		info.append(" Diff : ");  //.append(", ");
		info.append(m_format.format(dif2));  //.append(", ");

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
		if (!ADialog.ask(m_WindowNo, this, "VExpenseReport?", "(" + pi.getSummary() + ")"))
			return;
/*
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
		*/		
		this.setVisible(false);
//		AEnv.showCenterScreen(ff);
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


	private void CreateLikedServer(  )
	{
		// exemple ExcelFile \\\\shiraz\\Reports\\Budget.xls
		
		String Path;
		String ExcelFile;
		
		JFileChooser dialog = new JFileChooser();
		dialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
		dialog.setMultiSelectionEnabled(false);
		dialog.setDialogTitle("Selection fichier excel");
		dialog.addChoosableFileFilter(new ExtensionFileFilter("xls", "Fichier Excel"));


		if(dialog.showSaveDialog(new Frame()) == JFileChooser.APPROVE_OPTION)
		{
		    File file = dialog.getSelectedFile();
		    if ( ! file.canRead() )
		    	return;
		    
		    Path = file.getPath();
		    ExcelFile = file.getName();
		    Path = Path.replace( ExcelFile, "");
		}
		else 
		{
		   return;
		}
		
		ExcelFile = ExcelFile.replace(".xls", "");
		
		String Sql ;	

	 	Sql = "IF  EXISTS (SELECT srv.name FROM sys.servers srv WHERE srv.server_id != 0 AND srv.name = N'" + ExcelFile + "')EXEC master.dbo.sp_dropserver @server=N'" + ExcelFile + "', @droplogins='droplogins'";
		DB.executeUpdate(Sql, null);

		/****** Object:  LinkedServer [" + ExcelFile + "]*/    
	 	Sql = "EXEC master.dbo.sp_addlinkedserver @server = N'" + ExcelFile + "', @srvproduct=N'Excel 8.0', @provider=N'Microsoft.Jet.OLEDB.4.0', @datasrc=N'" + Path + ExcelFile + ".xls" + "', @provstr=N'Excel 8.0' ";
		int count = DB.executeUpdate(Sql, null);
		
		if ( count != -1)
		{
			/* For security reasons the linked server remote logins password is changed with ######## */
		 	Sql = "EXEC master.dbo.sp_addlinkedsrvlogin @rmtsrvname=N'" + ExcelFile + "',@useself=N'False',@locallogin=NULL,@rmtuser=NULL,@rmtpassword=NULL";
		 	DB.executeUpdate(Sql, null);
		 	Sql = "EXEC master.dbo.sp_serveroption @server=N'" + ExcelFile + "', @optname=N'collation compatible', @optvalue=N'false'";
		 	DB.executeUpdate(Sql, null);
		 	Sql = "EXEC master.dbo.sp_serveroption @server=N'" + ExcelFile + "', @optname=N'data access', @optvalue=N'true'";
		 	DB.executeUpdate(Sql, null);
		 	Sql = "EXEC master.dbo.sp_serveroption @server=N'" + ExcelFile + "', @optname=N'dist', @optvalue=N'false'";
		 	DB.executeUpdate(Sql, null);
		 	Sql = "EXEC master.dbo.sp_serveroption @server=N'" + ExcelFile + "', @optname=N'pub', @optvalue=N'false'";
		 	DB.executeUpdate(Sql, null);
		 	Sql = "EXEC master.dbo.sp_serveroption @server=N'" + ExcelFile + "', @optname=N'rpc', @optvalue=N'false'";
		 	DB.executeUpdate(Sql, null);
		 	Sql = "EXEC master.dbo.sp_serveroption @server=N'" + ExcelFile + "', @optname=N'rpc out', @optvalue=N'false'";
		 	DB.executeUpdate(Sql, null);
		 	Sql = "EXEC master.dbo.sp_serveroption @server=N'" + ExcelFile + "', @optname=N'sub', @optvalue=N'false'";
		 	DB.executeUpdate(Sql, null);
		 	Sql = "EXEC master.dbo.sp_serveroption @server=N'" + ExcelFile + "', @optname=N'connect timeout', @optvalue=N'0'";
		 	DB.executeUpdate(Sql, null);
		 	Sql = "EXEC master.dbo.sp_serveroption @server=N'" + ExcelFile + "', @optname=N'collation name', @optvalue=N'French_CI_AS'";
		 	DB.executeUpdate(Sql, null);
		 	Sql = "EXEC master.dbo.sp_serveroption @server=N'" + ExcelFile + "', @optname=N'lazy schema validation', @optvalue=N'false'";
		 	DB.executeUpdate(Sql, null);
		 	Sql = "EXEC master.dbo.sp_serveroption @server=N'" + ExcelFile + "', @optname=N'query timeout', @optvalue=N'0'";
		 	DB.executeUpdate(Sql, null);
		 	Sql = "EXEC master.dbo.sp_serveroption @server=N'" + ExcelFile + "', @optname=N'use remote collation', @optvalue=N'true'";
		 	DB.executeUpdate(Sql, null);
			
		}

		this.fillComboboxServers( ExcelFile );

	}
	
}   //  VExpenseReport



