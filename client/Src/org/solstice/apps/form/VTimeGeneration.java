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

import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.*;
import org.compiere.apps.*;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.*;
import org.compiere.model.*;
import org.compiere.plaf.*;
import org.compiere.process.*;
import org.compiere.swing.*;
import org.compiere.util.*;
import org.compiere.model.MPInstancePara;

import solstice.model.P_Payment_Group;
import solstice.model.P_Period;
import solstice.model.P_Time_Sheet;
//import org.compiere.apps.StatusBar;

/**
 *  Create Manual Payments From (AP) Invoices or (AR) Credit Memos.
 *  Allows user to select Invoices for payment.
 *  When Processed, PaySelection is created
 *  and optionally posted/generated and printed
 *
 *  @author Jorg Janke
 *  @version $Id: VTimeGeneration.java,v 1.2 2007/07/18 21:09:07 marmor01 Exp $
 */
public class VTimeGeneration extends CPanel
	implements FormPanel, ActionListener, TableModelListener, ASyncProcess
{
	/** @todo withholding */
	
	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		log.info("");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			jbInit();
			dynInit();
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
//			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
			loadTableInfo();

		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	init

	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;

	/** SQL for Query           */
	private String          m_sql;
	/** Number of selected rows */
	private int             m_noSelected = 0;
	/** Client ID               */
	private int             m_AD_Client_ID = 0;
	/**/
	private boolean         m_isLocked = false;
	/** Payment Selection		*/
//	private MPaySelection	m_ps = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VTimeGeneration.class);

	//
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();

    private CTextField m_txtPeriodStart = new CTextField();


	private CLabel labelInfo = new CLabel();
    public CLabel labelClient = new CLabel();
    public CLabel labelOrg = new CLabel();
    public CLabel labelPaymentGroup = new CLabel();
    public CLabel labelCalendar = new CLabel();
    public CLabel labelPeriod = new CLabel();
    public CLabel labelRework = new CLabel();
    public VLookup fieldClient ;
    public VLookup fieldOrg ;
	public VLookup fieldPaymentGroup;
	public VComboBox fieldCalendar;
    public VComboBox fieldPayPeriod = new VComboBox();
	private JButton bRefresh = ConfirmPanel.createRefreshButton(true);

	private JLabel dataStatus = new JLabel();
	private JScrollPane dataPane = new JScrollPane();
	private MiniTable miniTable = new MiniTable();
	private CPanel commandPanel = new CPanel();
	private JButton bViewErrors = new JButton("Rapport des erreurs...");
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	private JButton bGenerate = ConfirmPanel.createProcessButton(true);
	private FlowLayout commandLayout = new FlowLayout();
	private GridBagLayout parameterLayout = new GridBagLayout();

    private CLabel lblPeriodStart = new CLabel(Msg.translate(Env.getCtx(),"PeriodDate" )); // Msg.getMsg(Env.getCtx(),"StartDate" )

	private VCheckBox onlyLocked = new VCheckBox();
	private VCheckBox Rework = new VCheckBox();

//	public StatusBar statusBar = new StatusBar();
//	public StatusBar getStatusBar() { return statusBar; }


	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		CompiereColor.setBackground(this);
		//
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);
		//
		
		labelClient.setText(Msg.translate(Env.getCtx(), "AD_Client_ID"));
		labelOrg.setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		labelCalendar.setText(Msg.translate(Env.getCtx(), "P_Calendar_ID"));
		labelPaymentGroup.setText(Msg.translate(Env.getCtx(), "P_Payment_Group_ID"));
		labelPeriod.setText(Msg.translate(Env.getCtx(), "P_Period_ID"));

		MLookup ClientL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 527, DisplayType.Table);
		fieldClient = new VLookup ("AD_Client_ID", true, false, true, ClientL);
		fieldClient.setBounds(206, 20, 210, 19);

		MLookup OrganisationL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 528, DisplayType.Table);
		fieldOrg = new VLookup ("AD_Org_ID", false, false, true, OrganisationL);
		fieldOrg.setBounds(206, 20, 210, 19);

		MLookup PaymentGroupL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, getAD_Column_ID("P_Payment_Group_ID"), DisplayType.Table);
		fieldPaymentGroup = new VLookup ("P_Payment_Group_ID", false, false, true, PaymentGroupL);
		fieldPaymentGroup.setBounds(206, 20, 210, 19);

		fieldCalendar = new VComboBox();
		fillCalendarComboBox();
		fillComboBox();


		fieldCalendar.setBounds(206, 20, 210, 19);
		
/*		
		labelBankAccount.setText(Msg.translate(Env.getCtx(), "C_BankAccount_ID"));
		fieldBankAccount.addActionListener(this);
		labelBPartner.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		fieldBPartner.addActionListener(this);
		labelPayDate.setText(Msg.translate(Env.getCtx(), "PayDate"));
		labelPaymentRule.setText(Msg.translate(Env.getCtx(), "PaymentRule"));
		fieldPaymentRule.addActionListener(this);
*/		
		
		onlyLocked.setText("Approuvé par le gestionnaire" );
		Rework.setText( Msg.translate(Env.getCtx(), "Rework") );

		//
		labelInfo.setText( "Liste des gestionnaires qui n'ont pas complétés leurs feuilles de temps.");
//		dataStatus.setText("Liste des gestionnaires qui n'ont pas complétés leurs feuilles de temps.");
		
		//
		bGenerate.addActionListener(this);
		bCancel.addActionListener(this);
		//
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		
		parameterPanel.add(labelClient,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldClient,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelOrg,  new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldOrg,   new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelPaymentGroup,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldPaymentGroup,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(onlyLocked,  new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(Rework,  new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelCalendar,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldCalendar,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelPeriod,  new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldPayPeriod,   new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add( lblPeriodStart, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add( m_txtPeriodStart, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

/*		parameterPanel.add(labelInfo,  new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
*/
		parameterPanel.add(bRefresh,    new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));


		mainPanel.add(dataStatus, BorderLayout.SOUTH);
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(miniTable, null);
		//
		

		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
		bViewErrors.setIcon(Env.getImageIcon("Report16.gif"));
		bViewErrors.setToolTipText("Afficher les messages et erreurs de la table P_Time_Sheet_Error");
		bViewErrors.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent e)
			{
				Object value = fieldPayPeriod.getValue();
				int periodId = 0;
				if (value != null && value.toString().length() > 0)
				{
					try { periodId = Integer.parseInt(value.toString()); } catch (Exception ignored) {}
				}
				VTimeSheetErrorReportDialog.showReport(Env.getCtx(), periodId, 0, 0);
			}
		});
		commandPanel.add(bViewErrors, null);
		commandPanel.add(bCancel, null);
		commandPanel.add(bGenerate, null);

		fieldPaymentGroup.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
            	fillCalendarComboBox();
            }
        });


		fieldCalendar.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
            	fillComboBox();
            }
        });

		fieldPayPeriod.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
            	loadTableInfo();


            }
        });

		onlyLocked.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
            	loadTableInfo();
            }
        });


		Rework.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
            	loadTableInfo();
            }
        });

		fieldOrg.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
            	loadTableInfo();
            }
        });

		fieldClient.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
            	loadTableInfo();
            }
        });

	}   //  jbInit

	/**
	 *  Dynamic Init.
	 *  - Load Bank Info
	 *  - Load BPartner
	 *  - Init Table
	 */
	private void dynInit()
	{
		Properties ctx = Env.getCtx();


		m_sql = miniTable.prepareTable(new ColumnInfo[] {
			//  0..4
			new ColumnInfo(" ", "i.SUPERVISOR_ID", IDColumn.class, false, false, null),
			new ColumnInfo(Msg.translate(ctx, "Name"), "i.Nom", String.class, true, true, null),
			new ColumnInfo(Msg.translate(ctx, "email"), "i.Email", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "P_Period_ID"), "i.Name", String.class)
			},
			//	FROM
			"VTimeGenerationValidation i ",
			//	WHERE
			" i.AD_Client_ID=?"
			+ " AND i.P_Period_ID=?"
			+ " AND i.Locked=?",	//	additional where & order in loadTableInfo() 
			true, "i");
		//

		miniTable.getTableHeader().addMouseListener(new ColumnChecker(this.miniTable));

		miniTable.getModel().addTableModelListener(this);
		//
		//
		m_AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
	}   //  dynInit


	/**
	 *  Query and create TableInfo
	 */
	private void loadTableInfo()
	{
		log.config("");
		//  not yet initialized
		if (m_sql == null)
			return;

		String sql = m_sql;


		//  Get Open Invoices
		try
		{
			int index = 1;

			Object value = fieldClient.getValue();
			if (value != null && value.toString().length() > 0)
			{
				m_AD_Client_ID = Integer.parseInt( value.toString() );
			}

			value = fieldPayPeriod.getValue();
			
			int P_Period_ID = 0;
			if (value != null && value.toString().length() > 0)
			{
				 P_Period_ID = Integer.parseInt( value.toString() );
			}

	        if ( fieldOrg.getDisplay() != null && fieldOrg.getValue() != null )
	        {
	        	int AD_Org_ID = Integer.parseInt( fieldOrg.getValue().toString() );
	        	sql = sql + " AND AD_Org_ID = " + AD_Org_ID ;
			}

	        if ( fieldPaymentGroup.getDisplay() != null && fieldPaymentGroup.getValue() != null )
	        {
	        	int P_Payment_Group_ID = Integer.parseInt( fieldPaymentGroup.getValue().toString() );
	        	sql = sql + " AND P_Payment_Group_ID = " + P_Payment_Group_ID ;
			}

			if ( Rework.isSelected() == false && onlyLocked.isSelected() == true )
			{
				sql = sql + " AND EXISTS ( select 1 from VTimeGenerationEmployee  WHERE VTimeGenerationEmployee.SUPERVISOR_ID = i.SUPERVISOR_ID "
				          + " AND NOT EXISTS( SELECT 1 FROM P_TIME_SHEET WHERE P_TIME_SHEET.P_EMPLOYEE_ID = VTimeGenerationEmployee.P_EMPLOYEE_ID "  
				          + "                  AND P_TIME_SHEET.SHEETTYPE = 'Regular'  "
				          + "                  AND P_TIME_SHEET.P_PERIOD_ID = i.P_Period_ID  ) ) "
 	           ;
				
/*				sql = sql + " AND NOT EXISTS ( select 1 from VTimeGenerationEmployee "
				+ " inner join  P_TIME_SHEET on P_TIME_SHEET.P_EMPLOYEE_ID = VTimeGenerationEmployee.P_EMPLOYEE_ID "
				+ " AND P_TIME_SHEET.SHEETTYPE = 'Regular' "
				+ " AND P_TIME_SHEET.P_PERIOD_ID = i.P_Period_ID "
				+ " AND VTimeGenerationEmployee.SUPERVISOR_ID = i.SUPERVISOR_ID" +
                + ") ";
*/                
			}

			log.info("Debug SQL : " + sql );

			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(index++, m_AD_Client_ID);		//	Client
			pstmt.setInt(index++, P_Period_ID);

			if ( onlyLocked.isSelected() == true )
				pstmt.setInt(index++, 1);
			if ( onlyLocked.isSelected() == false )
				pstmt.setInt(index++, 0);
			
			ResultSet rs = pstmt.executeQuery();  
			miniTable.loadTable(rs);
			rs.close();
			pstmt.close();
			
			
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		try
		{
			int P_Period_ID = 0;

			Object value = fieldPayPeriod.getValue();
			if (value != null && value.toString().length() > 0)
			{
				 P_Period_ID = Integer.parseInt( value.toString() );
			}

			P_Period Period = P_Period.get( Env.getCtx(), P_Period_ID, null);
			sql = "select cycle_status From AT_Cycle where AT_CYCLE.BEGINNING_DATE = " + DB.TO_DATE( Period.getStartDate());
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();  
			if ( rs.next() )
			{
				if ( rs.getInt( 1) == 1 )
					dataStatus.setText("Le cycle de temps est fermé");
//					statusBar.setInfo( "La cycle de temps est fermé");
				else
					dataStatus.setText("Le cycle de temps n'est pas encore fermé");
//					statusBar.setInfo( "La cycle de temps n'est pas encore fermé");
			}
			rs.close();
			pstmt.close();

//			int rows = miniTable.getRowCount();
//			statusBar.setStatusLine(" Information sur la recherche : "  );
//	        getStatusBar().setStatusDB( miniTable.getSelectedRow() + m_noSelected + "/" + rows, null);

		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		
		calculateSelection();
		
		Object value = fieldPayPeriod.getValue();
		
		int P_Period_ID = 0;
		if (value != null && value.toString().length() > 0)
		{
			 P_Period_ID = Integer.parseInt( value.toString() );
			 P_Period period = P_Period.get( Env.getCtx(), P_Period_ID, null );
             m_txtPeriodStart.setText( period.getStartDate().toString().substring(0,10) 
                        + Msg.translate(Env.getCtx(),"TimeSheetTo" )//" au " 
                        + period.getEndDate().toString().substring(0,10) );
		}

	}   //  loadTableInfo

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	
	/**************************************************************************
	 *  ActionListener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{

		//  Generate PaySelection
		if (e.getSource() == bGenerate)
		{
			generateTimeSheetSelect();
			dispose();
		}

		else if (e.getSource() == bCancel)
			dispose();

		//  Update Open Invoices
		else if ( e.getSource() == bRefresh)
			loadTableInfo();

	}   //  actionPerformed

	/**
	 *  Table Model Listener
	 *  @param e event
	 */
	public void tableChanged(TableModelEvent e)
	{
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

		int rows = miniTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);
			if (id.isSelected())
			{
				m_noSelected++;
			}
		}

		//  Information
		StringBuffer info = new StringBuffer();
		info.append(m_noSelected).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected"));
//		dataStatus.setText(info.toString());
		//
		if ( onlyLocked.isSelected() == true )
			bGenerate.setEnabled(m_noSelected != 0);
		else
			bGenerate.setEnabled( false );
	}   //  calculateSelection

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
		log.config("-");
	}   //  executeASync

	

	/**
	 * On remplit les combobox de périodes en leur affectant la période ouverte comme valeur par défaut
	 */

	private void fillCalendarComboBox()
	{
        // Calendar
        String sql = "SELECT P_Calendar.P_Calendar_ID, P_Calendar.NAME, isDefault "
            + " From P_Calendar " ;
        
        if ( fieldPaymentGroup.getDisplay() != null && fieldPaymentGroup.getValue() != null )
        {
        	int P_Payment_Group_ID = Integer.parseInt( fieldPaymentGroup.getValue().toString() );
        	P_Payment_Group PaymentGroup = P_Payment_Group.get(Env.getCtx(), P_Payment_Group_ID, null);
        	sql += " where P_Calendar_ID =" + PaymentGroup.getP_Calendar_ID();
        }
        sql += " ORDER BY P_Calendar.NAME";
        try
        {
        	if ( fieldCalendar != null && fieldCalendar.getItemCount() != 0)
        	{
        		fieldCalendar.setSelectedIndex(0);
            	fieldCalendar.removeAllItems();
        	}
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE,"loadComboBox", e);
        }
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        fieldCalendar.setName("P_Calendar_ID");
//	        fieldCalendar.addItem(new KeyNamePair(0, ""));

	        int i = 0;
	        while(rs.next())
	        {
	        	fieldCalendar.addItem(new KeyNamePair(rs.getInt("P_Calendar_ID"), rs.getString("Name")));
	        	if ( rs.getString("IsDefault").equals("Y"))
	        		fieldCalendar.setSelectedIndex(i);
	        	i = i + 1;
	        }
	        
//        	if ( fieldCalendar != null && fieldCalendar.getItemCount() != 0)
//        		fieldCalendar.setSelectedIndex(0);
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	    	log.log(Level.SEVERE, "loadComboBox", e);
	    }
	}

	
	private void fillComboBox()
	{

        String sql = null;


        if ( fieldCalendar.getValue() == null)
        	return;
        
        int P_Calendar_ID =  Integer.parseInt( fieldCalendar.getValue().toString() );

	    // Period
        
		sql = "select P_Period.P_Period_ID, P_Period.SynonymName AS Name, P_Period.PeriodStatus "
			   + " From P_Period " 
		       + " Inner Join P_Year ON P_Year.P_Year_ID = P_Period.P_Year_ID "
               + " inner join P_Frequency on P_Frequency.P_Frequency_ID = P_PERIOD.P_Frequency_ID " 
			   + " WHERE P_Period.IsActive = 'Y' " 
			   + " AND P_Year.P_Calendar_ID = "  + P_Calendar_ID  
     	   + " ORDER BY P_Period.Name DESC";
        try
        {
        	if ( fieldPayPeriod != null && fieldPayPeriod.getItemCount() != 0)
        		fieldPayPeriod.setSelectedIndex(0);
        	fieldPayPeriod.removeAllItems();

        }
        catch (Exception e)
        {
            log.log(Level.SEVERE,"loadComboBox", e);
        }
	    try
	    {

	        int index = 0;
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        fieldPayPeriod.setName("PeriodFrom");
	        
	        while(rs.next())
	        {
	            this.fieldPayPeriod.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name")));
	            if(rs.getString("PeriodStatus").toUpperCase().equals("O"))
	            {
	                this.fieldPayPeriod.setSelectedIndex(index);
	            }
	            index++;
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	    	log.log(Level.SEVERE, "loadComboBox", e);
	    }
	    
    }

    private int getAD_Column_ID( String ColumnName )
	{
		int id = 0;
		String sql = "select AD_COLUMN_ID from AD_COLUMN where columnNAME = '" +ColumnName+ "'   and AD_TABLE_ID = 2000092";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("CustomWindowFind getAD_Column_ID - " + e);
		}
		
		return id;
		
	}

    

	/**
	 *  Generate PaySelection
	 */
    
    boolean firstTime = true;
    
	private void generateTimeSheetSelect()
	{
		log.info("");
	//	String trxName Trx.createTrxName("PaySelect");
	//	Trx trx = Trx.get(trxName, true);	trx needs to be committed too
		String trxName = null;
		Trx trx = null;
		//
		miniTable.stopEditor(true); 
		if (miniTable.getRowCount() == 0)
			return;
		miniTable.setRowSelectionInterval(0,0);
		calculateSelection();
		if (m_noSelected == 0)
			return;

		//  Ask to Post it
//		if (!ADialog.ask(m_WindowNo, this, "VTimeGenerationGenerate?", "(" + m_ps.getName() + ")"))
//			return;
			
		//  Prepare Process 
		
		int rows = miniTable.getRowCount();

		ProcessInfo pi = null;
		MPInstancePara param = null;
		MProcess process = null;
		MPInstance pInstance = null;
		
		if ( firstTime )
		{
			pi = new ProcessInfo("P_TimeGeneration", 2000010); // HARDCODED
			process = MProcess.get(Env.getCtx(), pi.getAD_Process_ID());

			pInstance = new MPInstance(Env.getCtx(), 2000010, 0);
			if (!pInstance.save())
			{
				return;
			}
			pi.setAD_PInstance_ID(pInstance.getAD_PInstance_ID());

	        if (pi.getAD_PInstance_ID() == 0)
	        {
		       log.severe(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
		       return;
		    }

			log.info("pi.getAD_PInstance_ID() : " + pi.getAD_PInstance_ID() );

			pi.setAD_User_ID (Env.getAD_User_ID(Env.getCtx()));
			pi.setAD_Client_ID(Env.getAD_Client_ID(Env.getCtx()));
			


			//Save the Period Id
			Object value = fieldPayPeriod.getValue();
			
			int P_Period_ID = 0;
			if (value != null && value.toString().length() > 0)
			{
				 P_Period_ID = Integer.parseInt( value.toString() );
			}

	        if ( fieldOrg.getDisplay() != null && fieldOrg.getValue() != null )
	        {
	        	int AD_Org_ID = Integer.parseInt( fieldOrg.getValue().toString() );
				param = new MPInstancePara(Env.getCtx(),pi.getAD_PInstance_ID(),1);
				param.setParameterName("AD_Org_ID");
				param.setP_Number( AD_Org_ID );
				param.save();
			}

	        if ( fieldPaymentGroup.getDisplay() != null && fieldPaymentGroup.getValue() != null )
	        {
	        	int P_Payment_Group_ID = Integer.parseInt( fieldPaymentGroup.getValue().toString() );
				param = new MPInstancePara(Env.getCtx(),pi.getAD_PInstance_ID(),20);
				param.setParameterName("P_Payment_Group_ID");
				param.setP_Number( P_Payment_Group_ID );
				param.save();
			}

			param = new MPInstancePara(Env.getCtx(),pi.getAD_PInstance_ID(),30);
			param.setParameterName("P_Period_ID");
			param.setP_Number( P_Period_ID );
			param.save();

			param = new MPInstancePara(Env.getCtx(),pi.getAD_PInstance_ID(),60);
			param.setParameterName("Rework");
			param.setP_String("Y");
			param.save();


			
/*			P_Period Period = P_Period.get(Env.getCtx(), P_Period_ID, trxName);
			
			param = new MPInstancePara(Env.getCtx(),pi.getAD_PInstance_ID(),2);
			//param.setAD_PInstance_ID(pi.getAD_PInstance_ID());
			param.setParameterName("P_Frequency_ID");
//			param.setSeqNo(2);
			param.setP_Number( Period.getP_Frequency_ID() );
			param.save();
*/
			firstTime = false;
		}


		boolean isSelected = false;
		String supervisorList = "-1";
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);
			if (id.isSelected())
			{
				isSelected = true;
				supervisorList = supervisorList + ',' + id.getRecord_ID(); 
			}
		}

		if ( isSelected )
		{
	        String sql = new String("");
	    	
	        sql = "SELECT Distinct P_Employee_ID FROM VTimeGenerationEmployee WHERE SUPERVISOR_ID in ( " + supervisorList + " )";

			log.info("SQL : " + sql );

	        PreparedStatement pstmt = null;
	    	try
	    	{
	    		pstmt = DB.prepareStatement(sql, trxName);
	    		ResultSet rs = pstmt.executeQuery();
	    		while (rs.next())
	    		{
		    		//Save the employee Id
		    		param = new MPInstancePara(Env.getCtx(),pi.getAD_PInstance_ID(), rs.getInt("P_Employee_ID"));
		    		param.setParameterName("P_Employee_ID");
		    		param.setP_Number(rs.getInt("P_Employee_ID"));
		    		param.save();
					log.info("P_Employee_ID: " + rs.getInt("P_Employee_ID" ) );
	    		}
	    		rs.close();
	    		pstmt.close();
	    		pstmt = null;
	    	}
	    	catch (Exception e)
	    	{
	    			System.err.println("VTimeGeneration - " + e);
	    	}

	    	//	Execute Process
			ProcessCtl worker = new ProcessCtl(this, pi, trx);
			worker.start();     //  complete tasks in unlockUI
			
		}
				
	}   //  generateTimeSheet


}   //  VTimeGeneration
