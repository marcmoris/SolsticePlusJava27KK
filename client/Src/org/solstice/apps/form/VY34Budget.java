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
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Properties;
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
import org.compiere.apps.form.VPayPrint;
import org.compiere.grid.ed.VCheckBox;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MPaySelection;
import org.compiere.model.MPeriod;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.plaf.CompierePLAF;
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

public class VY34Budget extends CPanel 
implements FormPanel, ActionListener, TableModelListener, ASyncProcess
{

	/**
	 *  VCreditsTotal Constructor
	 */
	public VY34Budget()
	{
	}   //  VY34Budget

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		s_log.log(Level.INFO, "VY34Budget.init");
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
			s_log.log(Level.SEVERE, "VY34Budget.init" + e);
		}
	}	//	init

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (VY34Budget.class);
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

	private CLabel labelStatus = new CLabel();
	private VComboBox fieldStatus = new VComboBox();

	private CLabel labelWorkPlace = new CLabel();
	private VComboBox fieldWorkPlace = new VComboBox();

	private CLabel labelJobType = new CLabel();
	private VComboBox fieldJobType = new VComboBox();

	
	private CLabel labelSelectionPeriod = new CLabel();
	private VComboBox fieldSelectionPeriod = new VComboBox();

	private CLabel labelSelectionPeriodFrom = new CLabel();
	private VComboBox fieldSelectionPeriodFrom = new VComboBox();

	private CLabel labelSelectionPeriodTo = new CLabel();
	private VComboBox fieldSelectionPeriodTo = new VComboBox();

	private CLabel labelErrorMsg = new CLabel();

	private VCheckBox checkIsActive = new VCheckBox();
	
	private CLabel labelHiredDateTo = new CLabel();
	private CLabel labelHiredDateFrom = new CLabel();
	private VDate fieldHiredDateTo = new VDate();
	private VDate fieldHiredDateFrom = new VDate();


	/** Dispose active                                  */
	private boolean         m_disposing = false;
	
	private int m_Period_ID = 0;


	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
	    this.fillPeriods();
		fillCombobox();
	    this.labelErrorMsg.setText("(*) Indique que le calcul est fait sur l'interval de période");
	    this.labelErrorMsg.setVisible(true);
//	    this.labelErrorMsg.setForeground(Color.RED);
	    
		CompiereColor.setBackground(this);
		//
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);
		//
		labelEmployee.setText("Employee");
		
		checkIsActive.setBackground(CompierePLAF.getInfoBackground());
		checkIsActive.setSelected(true);
		checkIsActive.setText("Is Active");
		
		labelGroupPayment.setText("Payment Group");
		labelActivity.setText("Function");
		labelOrg.setText("Division");
		labelDept.setText("Department");

		labelWorkPlace.setText("Base");
		labelJobType.setText("Job Type" );

		labelStatus.setText("Status" );

		labelSelectionPeriod.setText("Period");
		labelSelectionPeriodFrom.setText("(Interval) Period Start");
		labelSelectionPeriodTo.setText("Period End");

		labelHiredDateTo.setText("Hired Date Start");
		labelHiredDateFrom.setText("Hired Date End");
		

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



		parameterPanel.add(labelActivity,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldActivity,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelDept,   new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldDept,    new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelWorkPlace,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldWorkPlace,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelJobType,   new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldJobType,    new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelStatus,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldStatus,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelSelectionPeriod,  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldSelectionPeriod,  new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelEmployee,   new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldEmployee,    new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(checkIsActive,    new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		
		parameterPanel.add(labelSelectionPeriodFrom,  new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldSelectionPeriodFrom,  new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelSelectionPeriodTo,  new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldSelectionPeriodTo,  new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelHiredDateFrom,  new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldHiredDateFrom,  new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelHiredDateTo,  new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldHiredDateTo,  new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		
		parameterPanel.add(this.bExport,    new GridBagConstraints(4, 6, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(bRefresh,    new GridBagConstraints(5, 6, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(this.labelErrorMsg,   new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

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
		Properties ctx = Env.getCtx();

		/**  prepare MiniTable
		 *
		 */

		m_sql = miniTable.prepareTable(new ColumnInfo[] {
			//  0..4
			new ColumnInfo(" ", "P_Employee_ID", IDColumn.class, false, false, null),
			new ColumnInfo("Division", "Division", String.class, true, false, null),
			new ColumnInfo("Department", "Department", String.class, true, false, null),
			new ColumnInfo("Function", "Crew_Cost", String.class, true, false, null),
			new ColumnInfo("Cost Center", "Cost_Center", String.class, true, false, null),
			new ColumnInfo("CE Emp no", "EmpNo", String.class, true, false, null),
			new ColumnInfo("Lastname", "LastName", String.class, true, false, null),
			new ColumnInfo("Firstname", "Firstname", String.class, true, false, null),
			new ColumnInfo("Job Type", "Job_Type", String.class, true, false, null),
			new ColumnInfo("Post", "Post", String.class, true, false, null),
			new ColumnInfo("Status", "LongTermLeave", String.class, true, false, null),
//			new ColumnInfo("Actif", "IsActive", Boolean.class, true, false, null),
			new ColumnInfo("Salary per Pay", "SalaryPerPay", BigDecimal.class),
			new ColumnInfo("Hours per Pay", "HoursPerPay", BigDecimal.class),
			new ColumnInfo("Hourly Rate", "Hourly_Rate", BigDecimal.class),
			
			new ColumnInfo("01 Regular (Hours)*", "Earn01", BigDecimal.class),
			new ColumnInfo("01 Regular (Amount)*", "Earn01Amt", BigDecimal.class),
			new ColumnInfo("02 Overtime (Hours)*", "Overtime_Earn02", BigDecimal.class),
			new ColumnInfo("02 Overtime (Amount)*", "Overtime_Earn02Amt", BigDecimal.class),
			new ColumnInfo("03 Overtime Reg ", "Overtime_Reg_Earn03", BigDecimal.class),
			new ColumnInfo("04 Contractual Daily (Days)*", "ContractDaily", BigDecimal.class),
			new ColumnInfo("04 Contract Daily (Amount)*", "ContractDailyAmt", BigDecimal.class),
			new ColumnInfo("05 Contractual Hourly (Hours)*", "hourly05", BigDecimal.class),
			new ColumnInfo("05 Contractual Hourly (Amount)*", "hourly05Amt", BigDecimal.class),
			new ColumnInfo("06 Contract Gen Holiday", "CE_Ctrct_Legal_Earn06", BigDecimal.class),
			new ColumnInfo("07 General holiday (Hours)*", "CE_Contract_4_Earn07", BigDecimal.class),
			new ColumnInfo("07 General holiday (Amount)*", "CE_Contract_4_Earn07Amt", BigDecimal.class),
			new ColumnInfo("08 Lead Premium", "EO_Leader_Premium_Earn08", BigDecimal.class),
			new ColumnInfo("09 Regular Adjust. amount", "CE_Adjst_Sal_Earn09", BigDecimal.class),
			new ColumnInfo("10 Base Mgr Allowance", "EO_Bmgr_All_10", BigDecimal.class),
			new ColumnInfo("11 Marketing Allowance", "EO_Mark_All_11", BigDecimal.class),
			new ColumnInfo("12 Miscelanious Allowance", "EO_Diff_All_12", BigDecimal.class),
			new ColumnInfo("13 Supervisor Ame", "EO_Sup_Eng_13", BigDecimal.class),
			new ColumnInfo("14 Guaranty Flight Pay", "EO_Gar_Fp_A_14", BigDecimal.class),
			new ColumnInfo("15 Vfr Ck Pilot", "EO_Vfr_Ck_P_15", BigDecimal.class),
			new ColumnInfo("16 Safety Allowance", "EO_Safety_Al_16", BigDecimal.class),
			new ColumnInfo("17 Remote Base", "EO_Remote_B_17", BigDecimal.class),
			new ColumnInfo("18 Travel Da", "EO_Trav_DA_18", BigDecimal.class),
			new ColumnInfo("19 Medical Travel", "EO_Medtrv_19", BigDecimal.class),
			new ColumnInfo("20 Hangar Supervisor (Hours)", "EO_Hang_Sup_20", BigDecimal.class),
			new ColumnInfo("20 Hangar Supervisor (Amount", "EO_Hang_Sup_20Amt", BigDecimal.class),
			new ColumnInfo("21 Recognition Premium", "EO_Recog_Al_21", BigDecimal.class),
			new ColumnInfo("22 Prefered Provider (Hours)", "CE_Pref_Provider_Earn22", BigDecimal.class),
			new ColumnInfo("23 Ifr Captain Allowance", "EO_IfrcAl_23", BigDecimal.class),
			new ColumnInfo("24 Mgr Paramedic", "CE_Mgr_Paramed_Earn24", BigDecimal.class),
			new ColumnInfo("25 First Officer Allowance", "EO_Copil_Al_25", BigDecimal.class),
			new ColumnInfo("26 Ifr Training", "EO_Ifr_Train_26", BigDecimal.class),
			new ColumnInfo("27 Ifr Ame Allowance", "EO_Ifr_EnA_27", BigDecimal.class),
			new ColumnInfo("28 Ehs Halifax Allowance", "CE_All_EHS_Earn28", BigDecimal.class),
			new ColumnInfo("29 Remote Base Pensionable", "EO_Remote_Base_Pen_29", BigDecimal.class),
			new ColumnInfo("30 Vacation (Hours)*", "CE_Adjst_Vac_Earn30", BigDecimal.class),
			new ColumnInfo("30 Vacation (Amount)*", "CE_Adjst_Vac_Earn30Amt", BigDecimal.class),
			new ColumnInfo("65 Recognition Pension", "CE_Earn65", BigDecimal.class)
		},
			//	FROM
			"Y34Budget",
			//	WHERE
			" P_Period_ID =  ? " + 
			" AND P_Period_From_ID =  ? " +  
			" AND P_Period_To_ID =  ? " ,  
         	//	additional where & order in loadTableInfo() 
			true, "Y34Budget");
		
		//
		miniTable.getModel().addTableModelListener(this);    	

		//
		m_AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
	}   //  dynInit


	/**
	 * On remplit les combobox de périodes en leur affectant la période ouverte comme valeur par défaut
	 */
	private void fillPeriods()
	{
        P_Period period = P_Period.getOpenPeriod(Env.getCtx(), null);
        
		String sql = "SELECT P_Period_ID, Name, PeriodStatus, P_Year_ID, PeriodNo FROM P_Period WHERE IsActive = 'Y' " +
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
	            this.fieldSelectionPeriodFrom.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name")));
	            this.fieldSelectionPeriodTo.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name")));
	            if(rs.getString("PeriodStatus").toUpperCase().equals("O"))
	            {
	                this.fieldSelectionPeriod.setSelectedIndex(index);
	                m_Period_ID = rs.getInt("P_Period_ID");
	            }
	            if ( rs.getInt( "P_Year_ID") < (period.getP_Period_ID())     )
	            {
	            	if ( rs.getInt("P_Period_ID") == period.getP_Period_ID() - 25 )
	            		this.fieldSelectionPeriodFrom.setSelectedIndex(index);
	            	if ( rs.getInt("P_Period_ID") == period.getP_Period_ID() - 1 )
	            		this.fieldSelectionPeriodTo.setSelectedIndex(index);
	            }
	            index++;
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
	        s_log.log(Level.SEVERE, "VGainDetail.fillPeriods()", e);
	    }
	}
	
	private void fillCombobox()
	{
	    String sqlOrg = "select ad_org_id,Name  from ad_org where IsActive = 'Y'";
	    sqlOrg = sqlOrg + " And ad_org." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
//               + " AND AD_Org.Value in ('20S7', '2152', '0') "
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
	    catch (Exception e)
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
	    catch (Exception e)
	    {
	        s_log.log(Level.SEVERE, "VGainDetail.fillCombobox()", e);
	    }	

	    String sqlStatus = "select P_LongTermLeave_ID,  Name from p_LongTermLeave where IsActive = 'Y'";
	    sqlStatus = sqlStatus + " And p_LongTermLeave." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
		+ " ORDER BY Name";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlStatus, null);
	        ResultSet rs = stmt.executeQuery();
	        this.fieldStatus.addItem(new KeyNamePair(0,""));
	        while(rs.next())
	        {
	        	this.fieldStatus.addItem(new KeyNamePair(rs.getInt("P_LongTermLeave_ID"), rs.getString("Name")));
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
	        s_log.log(Level.SEVERE, "VGainDetail.fillCombobox()", e);
	    }	

	    
	    String sqlJobType = "select P_Job_Type_ID, Value  from P_Job_Type where IsActive = 'Y'";
	    sqlJobType = sqlJobType + " And P_Job_Type." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
		+ " ORDER BY p_Job_Type.Value ";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlJobType, null);
	        ResultSet rs = stmt.executeQuery();
	        this.fieldJobType.addItem(new KeyNamePair(0,""));
	        while(rs.next())
	        {
	        	this.fieldJobType.addItem(new KeyNamePair(rs.getInt("P_Job_Type_ID"), rs.getString("Value")));
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
	        s_log.log(Level.SEVERE, "VGainDetail.fillCombobox()", e);
	    }	

	    String sqlWorkPlace = "select P_Workplace_ID,Value, P_Workplace.Name  from p_Workplace where IsActive = 'Y'";
	    sqlWorkPlace = sqlWorkPlace + " And p_WorkPlace." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
		+ " ORDER BY P_Workplace.value";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlWorkPlace, null);
	        ResultSet rs = stmt.executeQuery();
	        this.fieldWorkPlace.addItem(new KeyNamePair(0,""));
	        while(rs.next())
	        {
	        	this.fieldWorkPlace.addItem(new KeyNamePair(rs.getInt("P_Workplace_ID"), rs.getString("Value") + " - " + rs.getString("Name")));
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
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
	    catch (Exception e)
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
	    catch (Exception e)
	    {
	        s_log.log(Level.SEVERE, "fillCombobox()", e);
	    }		    
	}


	/**
	 *  Query and create TableInfo
	 */
	private void loadTableInfo()
	{
		
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
			s_log.log (Level.SEVERE, "loadTableInfo:" + e);
		}
		calculateSelection();
    	TableColumnModel tcm = miniTable.getColumnModel();
    	tcm.getColumn(7).setPreferredWidth(15);

	}   //  loadTableInfo
	
	private PreparedStatement prepareStatement() throws Exception
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
		Integer periodFrom = new Integer(((KeyNamePair)fieldSelectionPeriodFrom.getSelectedItem()).getKey());
		Integer periodTo   = new Integer(((KeyNamePair)fieldSelectionPeriodTo.getSelectedItem()).getKey());

		if (period != null && period.toString().length() > 0)
		{
			m_Period_ID = Integer.parseInt( period.toString() );
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

		int LongTermLeave_id = Integer.parseInt(fieldStatus.getValue().toString());	
		if (LongTermLeave_id != 0)
		{
			sql += " and P_LongTermLeave_id = ? ";
		}

		int WorkPlace_id = Integer.parseInt(fieldWorkPlace.getValue().toString());	
		if (WorkPlace_id != 0)
		{
			sql += " and P_WorkPlace_id = ? ";
		}

		int JobType_id = Integer.parseInt(fieldJobType.getValue().toString());	
		if (JobType_id  != 0)
		{
			sql += " and P_Job_Type_id = ? ";
		}

		int Payment_Group_id = Integer.parseInt(fieldGroupPayment.getValue().toString());	
		if (Payment_Group_id != 0)
		{
			sql += " and P_Payment_Group_id = ? ";
		}
		
		if(checkIsActive.isSelected() == true)
			sql += " AND IsActive = 'Y'";
		
		
		Timestamp HiredDateFrom = (Timestamp)fieldHiredDateFrom.getValue();
		Timestamp HiredDateTo = (Timestamp)fieldHiredDateTo.getValue();
		if ( HiredDateFrom != null && HiredDateTo == null ) 
			sql += " AND DateHired >= " + DB.TO_DATE(HiredDateFrom);

		if ( HiredDateFrom == null && HiredDateTo != null ) 
			sql += " AND DateHired <= " + DB.TO_DATE(HiredDateTo);
		
		if ( HiredDateFrom != null && HiredDateTo != null ) 
			sql += " AND DateHired Between " + DB.TO_DATE(HiredDateFrom) + " and " + DB.TO_DATE(HiredDateTo);;

		
		//sql += " group by employee.P_Employee_ID, employee.Value, employee.Name, credits.P_Credits_ID, credits.Value, credits.Name, creditsMovement.PMovementVariation, UOMSymbol, creditsMovement.PMovementType, creditsMovement.PMovementDate, Payment ";
		sql += " ORDER BY Division, Empno";

		int index = 1;
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
//		pstmt.setInt(index++, m_AD_Client_ID);
//		if (onlyDue.isSelected())

		pstmt.setInt(index++, m_Period_ID);
		pstmt.setInt(index++, Integer.parseInt( periodFrom.toString() ));
		pstmt.setInt(index++, Integer.parseInt( periodTo.toString() ));

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

		if (LongTermLeave_id != 0)
		{
			pstmt.setInt(index++, LongTermLeave_id);
		}
		
		

		if (WorkPlace_id  != 0)
		{
			pstmt.setInt(index++, WorkPlace_id );
		}

		if (JobType_id  != 0)
		{
			pstmt.setInt(index++, JobType_id );
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
		setBusy( true );
	    if(e.getSource() == this.bExport)
	    {
			try
			{
			    exportToXml();
//			    exportToCSV();
			}
			catch (IOException ex)
			{
			    s_log.log(Level.SEVERE, "exportToCSV", ex);
			}
	    }
	    else
	    {
	    	
			Integer period = new Integer(((KeyNamePair)fieldSelectionPeriod.getSelectedItem()).getKey());

			if (period != null && period.toString().length() > 0)
			{
				m_Period_ID = Integer.parseInt( period.toString() );
			}
	
			loadTableInfo();
	    }
		setBusy( false );

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

			        fileWriter.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\" ?>\n");
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
			        fileWriter.write(" <Worksheet ss:Name=\" Y34-Budget \">\n");
			        fileWriter.write("  <Table ss:ExpandedColumnCount=\"" + rsMetaData.getColumnCount() + "\">\n");
			      
		            fileWriter.write("<Row>\n");
		            
		    		Integer periodFrom = new Integer(((KeyNamePair)fieldSelectionPeriodFrom.getSelectedItem()).getKey());
		    		Integer periodTo   = new Integer(((KeyNamePair)fieldSelectionPeriodTo.getSelectedItem()).getKey());

		    		P_Period PeriodFrom = P_Period.get( Env.getCtx(), periodFrom.intValue(), null); 
		    		P_Period PeriodTo   = P_Period.get( Env.getCtx(), periodTo.intValue(), null); 
//		            fileWriter.write("<Row>\n");
 //               	fileWriter.write("<Cell><Data ss:Type=\"String\"> (*) Indique que le calcul est fait sur l'interval de periode " +  PeriodFrom.getName() + " - " + PeriodTo.getName() + "</Data></Cell>\n");
//		            fileWriter.write("</Row>\n");

		            for(int i = 1; i <= rsMetaData.getColumnCount() - 1; i++)
		            {
//		                if(!rsMetaData.getColumnName(i).endsWith("_ID"))
	                	fileWriter.write("<Cell><Data ss:Type=\"String\">" + miniTable.getColumnName(i) + "</Data></Cell>\n");
		            }
		            fileWriter.write("</Row>\n");
		            

		            while(rs.next())
			        {
			            // On crée chaque ligne dans le fichier selon le format d'un .csv
			            String buffer = "";
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

		            String buffer = "\"Compagnie\",\"Département\",\"Crew_Cost\",\"Cost Center\",\"Numéro employé\",\"Prénom\",\"Nom \",\"Statut emploi\",\"Salaire par paie\", \"Heure par paie\", \"Taux Horaire\", \"Overtime_Earn02\",\"Overtime_Reg_Earn03\",ContractDaily,hourly05,\"CE_Ctrct_Legal_Earn06\",\"CE_Contract_4_Earn07\",\"EO_Leader_Premium_Earn08\",\"CE_Adjst_Sal_Earn09\",\"EO_Bmgr_All_10\",\"EO_Mark_All_11\",\"EO_Diff_All_12\",\"EO_Sup_Eng_13\",\"EO_Gar_Fp_A_14\",\"EO_Vfr_Ck_P_15\",\"EO_Safety_Al_16\",\"EO_Remote_B_17\",\"EO_Trav_DA_18\",\"EO_Medtrv_19\",\"EO_Hang_Sup_20\",\"EO_Recog_Al_21\",\"CE_Pref_Provider_Earn22\",\"EO_IfrcAl_23\",\"CE_Mgr_Paramed_Earn24\",\"EO_Copil_Al_25\",\"EO_Ifr_Train_26\",\"EO_Ifr_EnA_27\",\"CE_All_EHS_Earn28\",\"EO_Remote_Base_Pen_29\",\"CE_Adjst_Vac_Earn30\",\"CE_Earn65\"";
		            fileWriter.write(buffer + "\n");

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
				                	buffer += "\"" + rs.getBigDecimal(i).setScale(2,BigDecimal.ROUND_HALF_UP).toString() + "\",";
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.Double") )
				                	buffer += "\"" + rs.getString(i) + "\",";
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.sql.Timestamp") )
				                	buffer += "\"" + rs.getTimestamp(i).toString().substring(0,9) + "\",";
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.sql.Date") )
				                	buffer += "\"" + rs.getTimestamp(i).toString().substring(0,9) + "\",";
			                	else
				                	buffer += "\"" + rs.getString(i).replaceAll("\"", "\"\"") + "\",";

			            }
                		buffer += "\"" + rs.getBigDecimal(rsMetaData.getColumnCount()).setScale(2,BigDecimal.ROUND_HALF_UP).toString() + "\"";
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
