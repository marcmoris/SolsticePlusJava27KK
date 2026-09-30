package org.solstice.apps.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;

import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.process.ProcessInfo;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.ASyncProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import java.sql.Date;

import solstice.model.P_Period;
import solstice.process.PgiUtil;

public class CHL_ControleOperation extends CPanel 
implements FormPanel, ActionListener, TableModelListener, ASyncProcess
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 *  VCreditsTotal Constructor
	 */
	public CHL_ControleOperation()
	{
	}   //  CHL_ControleOperation

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		s_log.log(Level.INFO, "CHL_ControleOperation.init");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			MLookup empL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 2111113, DisplayType.Search);
			fieldEmployee = new VLookup ("P_Employee_ID", false, false, true, empL);
//			fPEmployee.addVetoableChangeListener(this);
			fieldEmployee.setBounds(206, 20, 210, 19);

			
			jbInit();
			dynInit();
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

//			this.miniTable.getTableHeader().addMouseListener(new ColumnChecker(this.miniTable));


		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE, "CHL_ControleOperation.init" + e);
		}
	}	//	init

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (CHL_ControleOperation.class);
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;

	/** SQL for Query           */
	private String          m_sql;
	/** Number of selected rows */
	private int             m_noSelected = 0;
	/**/
	private boolean         m_isLocked = false;

	//
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private CLabel labelEmployee = new CLabel();
	private VLookup fieldEmployee;
	

//	private VComboBox fieldEmployee = new VComboBox();
	private JLabel dataStatus = new JLabel();
	private JScrollPane dataPane = new JScrollPane();
	private MiniTable miniTable = new MiniTable();
	private CPanel commandPanel = new CPanel();
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	private FlowLayout commandLayout = new FlowLayout();
	private JButton bRefresh = ConfirmPanel.createRefreshButton(true);

	private JButton bExport = ConfirmPanel.createExportButton(true);
//	private DialogButton bExport = new DialogButton ("export", Msg.translate(Env.getLanguage(Env.getCtx()), "ExportExcel"), Env.getImageIcon("Export24.gif"), 0);

	private CLabel labelGroupPayment = new CLabel();
	private VComboBox fieldGroupPayment = new VComboBox();
	private CLabel labelActivity = new CLabel();
	private VComboBox fieldActivity = new VComboBox();
	private CLabel labelOrg = new CLabel();
	private VComboBox fieldOrg = new VComboBox();
	private CLabel labelDept = new CLabel();
	private VComboBox fieldDept = new VComboBox();
	private CLabel labelSelectionPeriod = new CLabel();
	private CLabel labelSelectionPeriodTo = new CLabel();
	private VComboBox fieldSelectionPeriod = new VComboBox();
	private VComboBox fieldSelectionPeriodTo = new VComboBox();
	
	private CLabel labelErrorMsg = new CLabel();

	/** Dispose active                                  */
	private boolean         m_disposing = false;
	
	private int m_Period_ID = 0;
	private int m_PeriodTo_ID = 0;


	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
	    this.fillPeriods();
		fillCombobox();
	    this.labelErrorMsg.setText(Msg.translate(Env.getCtx(), "CreditsTotalErr"));
	    this.labelErrorMsg.setVisible(false);
	    this.labelErrorMsg.setForeground(Color.RED);
	    
		CompiereColor.setBackground(this);
		//
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);
		//
		labelEmployee.setText(Msg.translate(Env.getCtx(), "P_Employee_ID"));
		
		labelGroupPayment.setText(Msg.translate(Env.getCtx(), "P_Payment_Group_ID"));
		labelActivity.setText(Msg.translate(Env.getCtx(), "C_activity_ID"));
		labelOrg.setText(Msg.translate(Env.getCtx(), "ad_org_id"));
		labelDept.setText(Msg.translate(Env.getCtx(), "P_Department_ID"));

		labelSelectionPeriod.setText(Msg.translate(Env.getCtx(), "P_Period_ID"));
		labelSelectionPeriodTo.setText(Msg.translate(Env.getCtx(), "P_Period_End_ID"));
		

//		fieldEmployee.addActionListener(this);
		bRefresh.addActionListener(this);
		this.bExport.addActionListener(this);
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


		parameterPanel.add(labelSelectionPeriod,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldSelectionPeriod,  new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelSelectionPeriodTo,  new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldSelectionPeriodTo,  new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelEmployee,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldEmployee,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(this.bExport,    new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(bRefresh,    new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(this.labelErrorMsg,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

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
	 *  Dynamic Init.
	 *  - Load Bank Info
	 *  - Load Employee
	 *  - Init Table
	 */
	private void dynInit()
	{

		/**  prepare MiniTable
		 *
			new ColumnInfo("CE Earn07", "CE_Earn07", BigDecimal.class),
			new ColumnInfo("CE Earn11", "CE_Earn11", BigDecimal.class),
			new ColumnInfo("CE Earn18", "CE_Earn18", BigDecimal.class),
			new ColumnInfo("CE Earn19", "CE_Earn19", BigDecimal.class),
			new ColumnInfo("CE Earn31", "CE_Earn31", BigDecimal.class),

			new ColumnInfo("CE Earn38", "CE_Earn38", BigDecimal.class),
			new ColumnInfo("CE Earn51", "CE_Earn51", BigDecimal.class),
			new ColumnInfo("CE Earn52", "CE_Earn52", BigDecimal.class),
			new ColumnInfo("CE Earn55", "CE_Earn55", BigDecimal.class),
			new ColumnInfo("CE Earn56", "CE_Earn56", BigDecimal.class),
			new ColumnInfo("CE Earn57", "CE_Earn57", BigDecimal.class),
			new ColumnInfo("CE Earn58", "CE_Earn58", BigDecimal.class),
			new ColumnInfo("CE Earn59", "CE_Earn59", BigDecimal.class),
			new ColumnInfo("CE Earn60", "CE_Earn60", BigDecimal.class),
			new ColumnInfo("CE Earn61", "CE_Earn61", BigDecimal.class),
			new ColumnInfo("CE Earn62", "CE_Earn62", BigDecimal.class),
			new ColumnInfo("CE Earn63", "CE_Earn63", BigDecimal.class),
			new ColumnInfo("CE Earn64", "CE_Earn64", BigDecimal.class),
			new ColumnInfo("CE Earn68", "CE_Earn68", BigDecimal.class),
			new ColumnInfo("CE Earn69", "CE_Earn69", BigDecimal.class),
			new ColumnInfo("CE Earn70", "CE_Earn70", BigDecimal.class),
			new ColumnInfo("CE Earn71", "CE_Earn71", BigDecimal.class),
			new ColumnInfo("CE Earn72", "CE_Earn72", BigDecimal.class),
			new ColumnInfo("CE Earn77", "CE_Earn77", BigDecimal.class),
			new ColumnInfo("CE Earn78", "CE_Earn78", BigDecimal.class),
			new ColumnInfo("CE Earn89", "CE_Earn89", BigDecimal.class),

		 *
		 */

		m_sql = miniTable.prepareTable(new ColumnInfo[] {
			//  0..4
			new ColumnInfo(" ", "P_Employee_ID", IDColumn.class, false, false, null),
			new ColumnInfo("Division", "Division", String.class, true, false, null),
			new ColumnInfo("Department", "Department", String.class, true, false, null),
			new ColumnInfo("Crew Cost", "CrewCost", String.class, true, false, null),
			new ColumnInfo("CE Emp no", "CE_Emp_no", String.class, true, false, null),
			new ColumnInfo("CE Earn01", "sum(CE_Earn01)", BigDecimal.class),
			new ColumnInfo("CE Earn04", "sum(CE_Earn04)", BigDecimal.class),
			new ColumnInfo("CE Earn05", "sum(CE_Earn05)", BigDecimal.class),
			new ColumnInfo("CE Earn06", "sum(CE_Earn06)", BigDecimal.class),
			new ColumnInfo("CE Earn79", "sum(CE_Earn79)", BigDecimal.class),
			new ColumnInfo("CE Earn08", "sum(CE_Earn08)", BigDecimal.class),
			new ColumnInfo("CE Earn09", "sum(CE_Earn09)", BigDecimal.class),
			new ColumnInfo("CE Earn10", "sum(CE_Earn10)", BigDecimal.class),
			new ColumnInfo("CE Earn11", "sum(CE_Earn11)", BigDecimal.class),
			new ColumnInfo("CE Earn15", "sum(CE_Earn15)", BigDecimal.class),
			new ColumnInfo("CE Earn16", "sum(CE_Earn16)", BigDecimal.class),
			new ColumnInfo("CE Earn21", "sum(CE_Earn21)", BigDecimal.class),
			new ColumnInfo("CE Earn22", "sum(CE_Earn22)", BigDecimal.class),
			new ColumnInfo("CE Earn23", "sum(CE_Earn23)", BigDecimal.class),
			new ColumnInfo("CE Earn24", "sum(CE_Earn24)", BigDecimal.class),
			new ColumnInfo("CE Earn25", "sum(CE_Earn25)", BigDecimal.class),
			new ColumnInfo("CE Earn26", "sum(CE_Earn26)", BigDecimal.class),
			new ColumnInfo("CE Earn28", "sum(CE_Earn28)", BigDecimal.class),
			new ColumnInfo("CE Earn32", "sum(CE_Earn32)", BigDecimal.class),
			new ColumnInfo("CE Earn33", "sum(CE_Earn33)", BigDecimal.class),
			new ColumnInfo("CE Earn34", "sum(CE_Earn34)", BigDecimal.class),
			new ColumnInfo("CE Earn35", "sum(CE_Earn35)", BigDecimal.class),
			new ColumnInfo("CE Earn36", "sum(CE_Earn36)", BigDecimal.class),
			new ColumnInfo("CE Earn65", "sum(CE_Earn65)", BigDecimal.class),
			new ColumnInfo("CE Earn30", "sum(CE_Earn30)", BigDecimal.class),
			new ColumnInfo("CE Earn31", "sum(CE_Earn31)", BigDecimal.class),
			new ColumnInfo("CE Earn82", "sum(CE_Earn82)", BigDecimal.class),
			new ColumnInfo("CE Earn83", "sum(CE_Earn83)", BigDecimal.class),
			new ColumnInfo("CE Earn84", "sum(CE_Earn84)", BigDecimal.class),
			new ColumnInfo("CE Earn85", "sum(CE_Earn85)", BigDecimal.class),
			new ColumnInfo("CE Earn86", "sum(CE_Earn86)", BigDecimal.class),
			new ColumnInfo("CE Earn87", "sum(CE_Earn87)", BigDecimal.class),
			new ColumnInfo("CE Earn88", "sum(CE_Earn88)", BigDecimal.class),
			new ColumnInfo("CE Earn91", "sum(CE_Earn91)", BigDecimal.class),
			new ColumnInfo("CE Earn92", "sum(CE_Earn92)", BigDecimal.class),
			new ColumnInfo("CE Earn93", "sum(CE_Earn93)", BigDecimal.class),
			new ColumnInfo("CE Earn94", "sum(CE_Earn94)", BigDecimal.class),
			new ColumnInfo("CE Earn96", "sum(CE_Earn96)", BigDecimal.class),
			new ColumnInfo("CE Earn97", "sum(CE_Earn97)", BigDecimal.class),
			new ColumnInfo("CE Earn98", "sum(CE_Earn98)", BigDecimal.class),
			new ColumnInfo("CE Earn99", "sum(CE_Earn99)", BigDecimal.class),
			new ColumnInfo("CE Earn14", "sum(CE_Earn14)", BigDecimal.class),
			new ColumnInfo("CE Earn39", "sum(CE_Earn39)", BigDecimal.class),
			new ColumnInfo("CE Earn40", "sum(CE_Earn40)", BigDecimal.class),
			new ColumnInfo("CE Earn41", "sum(CE_Earn41)", BigDecimal.class),
			new ColumnInfo("CE Earn42", "sum(CE_Earn42)", BigDecimal.class),
			new ColumnInfo("CE Earn43", "sum(CE_Earn43)", BigDecimal.class),
			new ColumnInfo("CE Earn44", "sum(CE_Earn44)", BigDecimal.class),
			new ColumnInfo("CE Earn45", "sum(CE_Earn45)", BigDecimal.class),
			new ColumnInfo("CE Earn46", "sum(CE_Earn46)", BigDecimal.class),
			new ColumnInfo("CE Earn47", "sum(CE_Earn47)", BigDecimal.class),
			new ColumnInfo("CE Earn48", "sum(CE_Earn48)", BigDecimal.class),
			new ColumnInfo("CE Earn49", "sum(CE_Earn49)", BigDecimal.class),
			new ColumnInfo("CE Earn50", "sum(CE_Earn50)", BigDecimal.class),
			new ColumnInfo("CE Earn73", "sum(CE_Earn73)", BigDecimal.class),
			new ColumnInfo("CE Earn74", "sum(CE_Earn74)", BigDecimal.class),
			new ColumnInfo("CE Earn75", "sum(CE_Earn75)", BigDecimal.class),
			new ColumnInfo("CE Earn76", "sum(CE_Earn76)", BigDecimal.class),
			new ColumnInfo("CE Earn54", "sum(CE_Earn54)", BigDecimal.class),
			new ColumnInfo("CE Earn80", "sum(CE_Earn80)", BigDecimal.class),
			new ColumnInfo("CE Earn12", "sum(CE_Earn12)", BigDecimal.class),
			new ColumnInfo("CE Earn13", "sum(CE_Earn13)", BigDecimal.class),
			new ColumnInfo("CE Earn20", "sum(CE_Earn20)", BigDecimal.class),
			new ColumnInfo("CE Earn27", "sum(CE_Earn27)", BigDecimal.class),
			new ColumnInfo("CE Earn17", "sum(CE_Earn17)", BigDecimal.class),
			new ColumnInfo("CE Earn29", "sum(CE_Earn29)", BigDecimal.class),
			new ColumnInfo("CE Earn90", "sum(CE_Earn90)", BigDecimal.class),
			new ColumnInfo("CE Earn81", "sum(CE_Earn81)", BigDecimal.class),
			new ColumnInfo("CE Earn02", "sum(CE_Earn02)", BigDecimal.class),
			new ColumnInfo("CE Earn53", "sum(CE_Earn53)", BigDecimal.class),
			new ColumnInfo("CE Earn03", "sum(CE_Earn03)", BigDecimal.class),
			new ColumnInfo("CE Earn95", "sum(CE_Earn95)", BigDecimal.class),
			new ColumnInfo("CE Earn66", "sum(CE_Earn66)", BigDecimal.class),
			new ColumnInfo("CE Earn67", "sum(CE_Earn67)", BigDecimal.class),

            new ColumnInfo("CD Dedn01","sum(CD_Dedn01)", BigDecimal.class),
            new ColumnInfo("CD Dedn03","sum(CD_Dedn03)", BigDecimal.class),
            new ColumnInfo("CD Dedn02","sum(CD_Dedn02)", BigDecimal.class),
            new ColumnInfo("CD Dedn53","sum(CD_Dedn53)", BigDecimal.class),
            new ColumnInfo("CD Company_EI","sum(CD_Company_EI)", BigDecimal.class),
            new ColumnInfo("CD Ontario Employer Health Tax","sum(CD_Ontario_Employer_Health_Tax)", BigDecimal.class),
            new ColumnInfo("CD Quebec Health Service Fund","sum(CD_Quebec_Health_Service_Fund)", BigDecimal.class),
			new ColumnInfo("CE Earn07", "sum(CE_Earn07)", BigDecimal.class),
			new ColumnInfo("CE Earn37", "sum(CE_Earn37)", BigDecimal.class),
            new ColumnInfo("Pay Period End Date", "max(EndDate)", Date.class)

		},
			//	FROM
			" CHL_Control_Operation ",
			//	WHERE
			" P_Period_ID between  ? and ?  " ,

			//	additional where & order in loadTableInfo() 
			true, "CHL_Control_Operation");
		
		//
		miniTable.getModel().addTableModelListener(this);    	

	}   //  dynInit


	/**
	 * On remplit les combobox de périodes en leur affectant la période ouverte comme valeur par défaut
	 */
	private void fillPeriods()
	{
        P_Period period = P_Period.getOpenPeriod(Env.getCtx(), null);
        
		String sql = "SELECT P_Period_ID, Name, PeriodStatus FROM P_Period WHERE IsActive = 'Y' " +
					 " AND P_Frequency_ID = "  + period.getP_Frequency_ID() + 
            		 "ORDER BY Name DESC";
	    try
	    {
	        int index = 0;
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	            this.fieldSelectionPeriod.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name")));
	            this.fieldSelectionPeriodTo.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name")));
	            if(rs.getString("PeriodStatus").toUpperCase().equals("O"))
	            {
	                this.fieldSelectionPeriod.setSelectedIndex(index);
	                this.fieldSelectionPeriodTo.setSelectedIndex(index);
	                m_Period_ID = rs.getInt("P_Period_ID");
	                m_PeriodTo_ID = rs.getInt("P_Period_ID");
	            }
	            index++;
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "VGainDetail.fillPeriods()", e);
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
	        s_log.log(Level.SEVERE, "VGainDetail.fillCombobox()", e);
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
	        s_log.log(Level.SEVERE, "VGainDetail.fillCombobox()", e);
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
	}


	/**
	 *  Query and create TableInfo
	 */
	private void loadTableInfo()
	{
		
		//  Get Open Invoices
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
			s_log.log (Level.SEVERE, "loadTableInfo:" + e);
		}
		calculateSelection();
    	TableColumnModel tcm = miniTable.getColumnModel();
    	tcm.getColumn(7).setPreferredWidth(15);

	}   //  loadTableInfo
	
	private PreparedStatement prepareStatement() throws SQLException
	{
		//  not yet initialized
		if (m_sql == null)
			return null;

		String sql = m_sql;
		//  Parameters

		//
//		if (onlyDue.isSelected())
//			sql += " AND i.DateInvoiced+p.NetDays <= ?";
		//

		int P_Employee_ID = 0;

		Object value = fieldEmployee.getValue();
		if (value != null && value.toString().length() > 0)
		{
			P_Employee_ID = Integer.parseInt( value.toString() );
		}

		Integer period = new Integer(((KeyNamePair)fieldSelectionPeriod.getSelectedItem()).getKey());
		Integer periodTo = new Integer(((KeyNamePair)fieldSelectionPeriodTo.getSelectedItem()).getKey());

		if (period != null && period.toString().length() > 0)
		{
			m_Period_ID = Integer.parseInt( period.toString() );
		}

		if (periodTo != null && periodTo.toString().length() > 0)
		{
			m_PeriodTo_ID = Integer.parseInt( periodTo.toString() );
		}
		
		if (P_Employee_ID != 0)
			sql += " AND P_Employee_ID = ?";


		int Org_id = Integer.parseInt(fieldOrg.getValue().toString());
		if (Org_id != 0)
		{
			sql += " and ad_Org_id = ? ";
		}
		
		int Activity_id = Integer.parseInt(fieldActivity.getValue().toString());	
		if (Activity_id != 0)
		{
			sql += " and C_Activity_id = ? ";
		}
		
		int Department_id = Integer.parseInt(fieldDept.getValue().toString());	
		if (Department_id != 0)
		{
			sql += " and P_Department_id = ? ";
		}

		int Payment_Group_id = Integer.parseInt(fieldGroupPayment.getValue().toString());	
		if (Payment_Group_id != 0)
		{
			sql += " and P_Payment_Group_id = ? ";
		}

		sql += " group by P_Employee_ID, Division, Department, CrewCost, CE_Emp_no";

		//sql += " group by employee.P_Employee_ID, employee.Value, employee.Name, credits.P_Credits_ID, credits.Value, credits.Name, creditsMovement.PMovementVariation, UOMSymbol, creditsMovement.PMovementType, creditsMovement.PMovementDate, Payment ";
		sql += " ORDER BY Division, CE_Emp_no";

		int index = 1;
		PreparedStatement pstmt = DB.prepareStatement(sql, null);

		pstmt.setInt(index++, m_Period_ID);
		pstmt.setInt(index++, m_PeriodTo_ID);

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
//			    exportToXml();
			    exportToCSV();
			}
			catch (IOException ex)
			{
			    s_log.log(Level.SEVERE, "exportToCSV", ex);
			}
			setBusy( false );
	    }
	    else
	    {
	    	
			Integer period = new Integer(((KeyNamePair)fieldSelectionPeriod.getSelectedItem()).getKey());
			Integer periodTo = new Integer(((KeyNamePair)fieldSelectionPeriodTo.getSelectedItem()).getKey());

			if (period != null && period.toString().length() > 0)
			{
				m_Period_ID = Integer.parseInt( period.toString() );
			}
	
			if (periodTo != null && periodTo.toString().length() > 0)
			{
				m_PeriodTo_ID = Integer.parseInt( periodTo.toString() );
			}
			
		    setBusy( true );
			loadTableInfo();
			setBusy( false );
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
			        fileWriter.write(" <Worksheet ss:Name=\"" + Msg.translate(Env.getCtx(), "P_Credits_ID") + "\">\n");
			        fileWriter.write("  <Table ss:ExpandedColumnCount=\"" + rsMetaData.getColumnCount() + "\">\n");
/*
		            fileWriter.write("<Row>\n");
		            for(int i = 1; i <= rsMetaData.getColumnCount() - 1; i++)
		            {
		                if(!rsMetaData.getColumnName(i).endsWith("_ID"))
		                	fileWriter.write("<Cell><Data ss:Type=\"String\">" + Msg.translate(Env.getCtx(), rsMetaData.getColumnName(i)) + "</Data></Cell>\n");
		            }
		            fileWriter.write("</Row>\n");
*/		            

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
			                		fileWriter.write("<Cell><Data ss:Type=\"String\">" + PgiUtil.convertHTMLString(rs.getString(i)) + "</Data></Cell>\n");
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
			    catch (SQLException e)
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

			        String buffer = "\"EE company no\",\"EE branch no\",\"EE dept no\",\"EE emp no\",\"CE Earn01\",\"CE Earn04\",\"CE Earn05\",\"CE Earn06\",\"CE Earn79\",\"CE Earn08\",\"CE Earn09\",\"CE Earn10\",\"CE Earn11\",\"CE Earn15\",\"CE Earn16\",\"CE Earn21\",\"CE Earn22\",\"CE Earn23\",\"CE Earn24\",\"CE Earn25\",\"CE Earn26\",\"CE Earn28\",\"CE Earn32\",\"CE Earn33\",\"CE Earn34\",\"CE Earn35\",\"CE Earn36\",\"CE Earn65\",\"CE Earn30\",\"CE Earn31\",\"CE Earn82\",\"CE Earn83\",\"CE Earn84\",\"CE Earn85\",\"CE Earn86\",\"CE Earn87\",\"CE Earn88\",\"CE Earn91\",\"CE Earn92\",\"CE Earn93\",\"CE Earn94\",\"CE Earn96\",\"CE Earn97\",\"CE Earn98\",\"CE Earn99\",\"CE Earn14\",\"CE Earn39\",\"CE Earn40\",\"CE Earn41\",\"CE Earn42\",\"CE Earn43\",\"CE Earn44\",\"CE Earn45\",\"CE Earn46\",\"CE Earn47\",\"CE Earn48\",\"CE Earn49\",\"CE Earn50\",\"CE Earn73\",\"CE Earn74\",\"CE Earn75\",\"CE Earn76\",\"CE Earn54\",\"CE Earn80\",\"CE Earn12\",\"CE Earn13\",\"CE Earn20\",\"CE Earn27\",\"CE Earn17\",\"CE Earn29\",\"CE Earn90\",\"CE Earn81\",\"CE Earn02\",\"CE Earn53\",\"CE Earn03\",\"CE Earn95\",\"CE Earn66\",\"CE Earn67\",\"CD Dedn01\",\"CD Dedn03\",\"CD Dedn02\",\"CD Dedn53\",\"CD Company EI\",\"CD Ontario Employer Health Tax\",\"CD Quebec Health Service Fund\",\"CE Earn07\",\"CE Earn37\",\"Pay Period End Date\",\"Pay Period #\"";
		            fileWriter.write(buffer + "\n");

			        SimpleDateFormat f = new SimpleDateFormat ("MM/dd/yyyy");

			        while(rs.next())
			        {
			        	buffer = "";
			            // On crée chaque ligne dans le fichier selon le format d'un .csv
			            for(int i = 1; i <= rsMetaData.getColumnCount() - 1; i++)
			            {
			                // On ne doit pas inclure les colonnes ID
			                if(!rsMetaData.getColumnName(i).endsWith("_ID"))
			                	if ( rsMetaData.getColumnClassName(i).equals( "java.lang.String") )
				                	buffer += "\"" + rs.getString(i).replaceAll("\"", "\"\"") + "\",";
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.BigDecimal") )
				                	buffer += "\"" + nvl(rs.getBigDecimal(i)).setScale(2).toString() + "\",";
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.Double") )
				                	buffer += "\"" + rs.getString(i) + "\",";
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.sql.Timestamp") )
				                	buffer += "\"" + f.format(rs.getTimestamp(i))  + "\",";
//				                	buffer += "\"" + rs.getTimestamp(i).toString().substring(0,9) + "\",";
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.sql.Date") )
				                	buffer += "\"" + f.format(rs.getTimestamp(i))  + "\",";
			                	else
				                	buffer += "\"" + rs.getString(i).replaceAll("\"", "\"\"") + "\",";

			            }
	                	buffer += "\"" + f.format(rs.getTimestamp(rsMetaData.getColumnCount()))  + "\"";

//			            buffer += "\"" + rs.getString(rsMetaData.getColumnCount()) + "\"";
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

	private BigDecimal nvl ( BigDecimal bd )
	{
		if ( bd == null )
			return new BigDecimal(0);
		else
			return bd;
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
		info.append(m_noSelected).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected")).append(" - ");
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
		this.setVisible(false);
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
		s_log.log(Level.INFO, "VCreditsTotal.executeASync");
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
}
