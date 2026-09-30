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
import java.text.SimpleDateFormat;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

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


import solstice.model.P_Period;
import solstice.process.PgiUtil;

public class CHL_YTD extends CPanel 
implements FormPanel, ActionListener, TableModelListener, ASyncProcess
{

	/**
	 *  OEMS Rapport d'analyses 
	 */
	public CHL_YTD()
	{
	}   //  CHL_YTD

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		s_log.log(Level.INFO, "CHL_YTD.init");
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
			s_log.log(Level.SEVERE, "CHL_YTD.init" + e);
		}
	}	//	init

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (CHL_YTD.class);
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
	private VComboBox fieldSelectionPeriod = new VComboBox();
	
	private CLabel labelSelectionPeriod2 = new CLabel();
	private VComboBox fieldSelectionPeriod2 = new VComboBox();
	
	private CLabel labelErrorMsg = new CLabel();

	/** Dispose active                                  */
	private boolean         m_disposing = false;
	
	private int m_Period_ID = 0;
	private int m_Period2_ID = 0;


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
		labelSelectionPeriod2.setText(Msg.translate(Env.getCtx(), "P_Period_ID"));
		

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

		parameterPanel.add(labelSelectionPeriod2,  new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldSelectionPeriod2,  new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

//		parameterPanel.add(labelEmployee,   new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
//		parameterPanel.add(fieldEmployee,    new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(this.bExport,    new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(bRefresh,    new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

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

	
	private ColumnInfo[] getColumnInfo()
	{
		return new ColumnInfo[] {
				//  0..4
				new ColumnInfo(" ", "CHL_YTD_SUM_01.P_Employee_ID", IDColumn.class, false, false, null),
				new ColumnInfo("Division", "max(AD_Org.Value) + '-' + max( AD_Org.Name )", String.class, true, false, null),
				new ColumnInfo("Department", "max(P_Department.Value) + '-' + max( P_Department.Name )", String.class, true, false, null),
				new ColumnInfo("Base", "max(P_WorkPlace.Value) + '-' + max( P_WorkPlace.Name )", String.class, true, false, null),
				new ColumnInfo("Fonction", "max(C_Activity.Value) + '-' + max( C_Activity.Name )", String.class, true, false, null),
				new ColumnInfo(" ", "1 AS SORT_ID", IDColumn.class, true, false, null), 
				new ColumnInfo("Emp Nom", "max(P_Employee.Name)", String.class, true, false, null),
				new ColumnInfo("Emp No", "max(P_Employee.Value)", String.class, true, false, null),
				new ColumnInfo("Heures réguliéres", "SUM( Total_QTY ) - SUM( Gain_02_QTY + Gain_03_QTY + Gain_53_QTY + Gain_99_QTY  ) ", BigDecimal.class, true, false, null), 
				new ColumnInfo("Heures en surtemps (O/T hrs) ", "SUM( Gain_02_QTY + Gain_03_QTY + Gain_53_QTY + Gain_99_QTY    )", BigDecimal.class, true, false, null), 
				new ColumnInfo("Total", "null", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("01" ), "SUM( Gain_01 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("04" ), "SUM( Gain_04 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("05" ), "SUM( Gain_05 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("07" ), "SUM( Gain_07 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("08" ), "SUM( Gain_08 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("09" ), "SUM( Gain_09 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("10" ), "SUM( Gain_10 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("11" ), "SUM( Gain_11 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("12" ), "SUM( Gain_12 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("13" ), "SUM( Gain_13 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("14" ), "SUM( Gain_14 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("15" ), "SUM( Gain_15 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("16" ), "SUM( Gain_16 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("17" ), "SUM( Gain_17 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("18" ), "SUM( Gain_18 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("19" ), "SUM( Gain_19 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("20" ), "SUM( Gain_20 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("21" ), "SUM( Gain_21 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("22" ), "SUM( Gain_22 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("23" ), "SUM( Gain_23 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("24" ), "SUM( Gain_24 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("25" ), "SUM( Gain_25 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("26" ), "SUM( Gain_26 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("27" ), "SUM( Gain_27 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("28" ), "SUM( Gain_28 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("29" ), "SUM( Gain_29 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("30" ), "SUM( Gain_30 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("31" ), "SUM( Gain_31 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("32" ), "SUM( Gain_32 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("33" ), "SUM( Gain_33 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("34" ), "SUM( Gain_34 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("35" ), "SUM( Gain_35 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("36" ), "SUM( Gain_36 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("37" ), "SUM( Gain_37 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("39" ), "SUM( Gain_39 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("40" ), "SUM( Gain_40 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("41" ), "SUM( Gain_41 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("42" ), "SUM( Gain_42 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("43" ), "SUM( Gain_43 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("44" ), "SUM( Gain_44 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("45" ), "SUM( Gain_45 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("46" ), "SUM( Gain_46 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("47" ), "SUM( Gain_47 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("48" ), "SUM( Gain_48 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("49" ), "SUM( Gain_49 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("50" ), "SUM( Gain_50 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("54" ), "SUM( Gain_54 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("64" ), "SUM( Gain_64 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("65" ), "SUM( Gain_65 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("66" ), "SUM( Gain_66 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("73" ), "SUM( Gain_73 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("74" ), "SUM( Gain_74 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("75" ), "SUM( Gain_75 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("76" ), "SUM( Gain_76 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("80" ), "SUM( Gain_80 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("81" ), "SUM( Gain_81 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("83" ), "SUM( Gain_83 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("84" ), "SUM( Gain_84 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("85" ), "SUM( Gain_85 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("86" ), "SUM( Gain_86 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("87" ), "SUM( Gain_87 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("88" ), "SUM( Gain_88 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("91" ), "SUM( Gain_91 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("92" ), "SUM( Gain_92 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("92A"), "SUM( Gain_92A)", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("93" ), "SUM( Gain_93 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("94" ), "SUM( Gain_94 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("95" ), "SUM( Gain_95 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("96" ), "SUM( Gain_96 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("97" ), "SUM( Gain_97 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("98" ), "SUM( Gain_98 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("06" ), "SUM( Gain_06 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("79" ), "SUM( Gain_79 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("82" ), "SUM( Gain_82 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("90" ), "SUM( Gain_90 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("02" ), "SUM( Gain_02 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("03" ), "SUM( Gain_03 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("53" ), "SUM( Gain_53 )", BigDecimal.class, true, false, null), 
				new ColumnInfo(getDescriptionGain("99" ), "SUM( Gain_99 )", BigDecimal.class, true, false, null), 
				
				//				new ColumnInfo(getDescriptionGain("I02"), "SUM( Gain_I02)", BigDecimal.class, true, false, null) , 
//				new ColumnInfo(getDescriptionGain("I03"), "SUM( Gain_I03)", BigDecimal.class, true, false, null) , 
//				new ColumnInfo(getDescriptionGain("I04"), "SUM( Gain_I04)", BigDecimal.class, true, false, null) , 
				new ColumnInfo(getDescriptionGain("I05"), "SUM( Gain_I05)", BigDecimal.class, true, false, null) , 
//				new ColumnInfo(getDescriptionGain("I06"), "SUM( Gain_I06)", BigDecimal.class, true, false, null) , 
//				new ColumnInfo(getDescriptionGain("I07"), "SUM( Gain_I07)", BigDecimal.class, true, false, null) , 
//				new ColumnInfo(getDescriptionDeduction("01"       ), "sum( Deduction_01              )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("01"       ), "sum( Deduction_01_yeur         )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("01a"      ), "sum( Deduction_01a             )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("01a"      ), "sum( Deduction_01a_yeur        )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("02"       ), "sum( Deduction_02              )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("02"       ), "sum( Deduction_02_yeur         )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("03"       ), "sum( Deduction_03              )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("03"       ), "sum( Deduction_03_yeur         )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("04"       ), "sum( Deduction_04              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("05"       ), "sum( Deduction_05              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("06"       ), "sum( Deduction_06              )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("06"       ), "sum( Deduction_06_yeur         )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("07"       ), "sum( Deduction_07              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("09"       ), "sum( Deduction_09              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("10"       ), "sum( Deduction_10              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("11"       ), "sum( Deduction_11              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("14"       ), "sum( Deduction_14              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("15"       ), "sum( Deduction_15              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("16"       ), "sum( Deduction_16              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("17"       ), "sum( Deduction_17              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("18"       ), "sum( Deduction_18              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("19"       ), "sum( Deduction_19              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("21"       ), "sum( Deduction_21              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("22"       ), "sum( Deduction_22              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("27"       ), "sum( Deduction_27              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("28"       ), "sum( Deduction_28              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("30"       ), "sum( Deduction_30              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("31"       ), "sum( Deduction_31              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("32"       ), "sum( Deduction_32              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("34"       ), "sum( Deduction_34              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("36"       ), "sum( Deduction_36              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("37"       ), "sum( Deduction_37              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("38"       ), "sum( Deduction_38              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("39"       ), "sum( Deduction_39              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("40"       ), "sum( Deduction_40              )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("40"       ), "sum( Deduction_40_yeur         )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("42"       ), "sum( Deduction_42              )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("42"       ), "sum( Deduction_42_yeur         )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("46"       ), "sum( Deduction_46              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("48"       ), "sum( Deduction_48              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("49"       ), "sum( Deduction_49              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("50"       ), "sum( Deduction_50              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("51"       ), "sum( Deduction_51              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("53"       ), "sum( Deduction_53              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("54"       ), "sum( Deduction_54              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("55"       ), "sum( Deduction_55              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("56"       ), "sum( Deduction_56              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("57"       ), "sum( Deduction_57              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("58"       ), "sum( Deduction_58              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("60"       ), "sum( Deduction_60              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("62"       ), "sum( Deduction_62              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("63"       ), "sum( Deduction_63              )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("64"       ), "sum( Deduction_64_yeur         )" , Double.class),
//2009.10.06				
				new ColumnInfo(getDescriptionDeduction("63"       ), "sum( Deduction_63_yeur         )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("67"       ), "sum( Deduction_67              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("68"       ), "sum( Deduction_68              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("69"       ), "sum( Deduction_69              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("70"       ), "sum( Deduction_70              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("72"       ), "sum( Deduction_72              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("75"       ), "sum( Deduction_75              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("80"       ), "sum( Deduction_80              )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("A01"      ), "sum( Deduction_A01             )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("A01"      ), "sum( Deduction_A01_yeur        )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("A02"      ), "sum( Deduction_A02             )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("A02"      ), "sum( Deduction_A02_yeur        )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("A05"      ), "sum( Deduction_A05             )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("A05"      ), "sum( Deduction_A05_yeur        )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("A06"      ), "sum( Deduction_A06             )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("A06"      ), "sum( Deduction_A06_yeur        )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("A07"      ), "sum( Deduction_A07             )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("A07"      ), "sum( Deduction_A07_yeur        )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("A08"      ), "sum( Deduction_A08             )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("A08"      ), "sum( Deduction_A08_yeur        )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("A09"      ), "sum( Deduction_A09             )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("A09"      ), "sum( Deduction_A09_yeur        )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("CSST1"    ), "sum( Deduction_CSST1_yeur      )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("CSST2"    ), "sum( Deduction_CSST2_yeur      )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("EHT"      ), "sum( Deduction_EHT_yeur        )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("Exchequer"), "sum( Deduction_Exchequer_yeur  )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("FSS"      ), "sum( Deduction_FSS_yeur        )" , Double.class),
//				new ColumnInfo(getDescriptionDeduction("H-AB"     ), "sum( [Deduction_H-AB_yeur]       )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("H-BC"     ), "sum( [Deduction_H-BC_yeur]       )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("WCB-AL"   ), "sum( [Deduction_WCB-AL_yeur]     )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("WCB-BC"   ), "sum( [Deduction_WCB-BC_yeur]     )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("WCB-MB"   ), "sum( [Deduction_WCB-MB_yeur]     )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("WCB-NS"   ), "sum( [Deduction_WCB-NS_yeur]     )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("WCB-NU"   ), "sum( [Deduction_WCB-NU_yeur]     )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("WCB-NWT"  ), "sum( [Deduction_WCB-NWT_yeur]    )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("WCB-SK"   ), "sum( [Deduction_WCB-SK_yeur]     )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("WHSCC-NB" ), "sum( [Deduction_WHSCC-NB_yeur]   )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("WHSCC-NF" ), "sum( [Deduction_WHSCC-NF_yeur]   )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("WSIB-ON"  ), "sum( [Deduction_WSIB-ON_yeur]    )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("08"       ), "sum( Deduction_08              )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("12"       ), "sum( Deduction_12              )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("13"       ), "sum( Deduction_13              )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("25"       ), "sum( Deduction_25              )" , Double.class),
				new ColumnInfo(getDescriptionDeduction("44"       ), "sum( Deduction_44              )" , Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("37"), "sum( Taxable_Benefit_37 ) AS Taxable_Benefit_37", Double.class),
//				new ColumnInfo(getDescriptionTaxable_Benefit("51"), "sum( Taxable_Benefit_51 ) AS Taxable_Benefit_51", Double.class),
//				new ColumnInfo(getDescriptionTaxable_Benefit("52"), "sum( Taxable_Benefit_52 ) AS Taxable_Benefit_52", Double.class),
//				new ColumnInfo(getDescriptionTaxable_Benefit("57"), "sum( Taxable_Benefit_57 ) AS Taxable_Benefit_57", Double.class),
//				new ColumnInfo(getDescriptionTaxable_Benefit("58"), "sum( Taxable_Benefit_58 ) AS Taxable_Benefit_58", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("59"), "sum( Taxable_Benefit_59 ) AS Taxable_Benefit_59", Double.class),
//				new ColumnInfo(getDescriptionTaxable_Benefit("61"), "sum( Taxable_Benefit_61 ) AS Taxable_Benefit_61", Double.class),
//				new ColumnInfo(getDescriptionTaxable_Benefit("62"), "sum( Taxable_Benefit_62 ) AS Taxable_Benefit_62", Double.class),
//				new ColumnInfo(getDescriptionTaxable_Benefit("63"), "sum( Taxable_Benefit_63 ) AS Taxable_Benefit_63", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("67"), "sum( Taxable_Benefit_67 ) AS Taxable_Benefit_67", Double.class)
//				new ColumnInfo(getDescriptionTaxable_Benefit("68"), "sum( Taxable_Benefit_68 ) AS Taxable_Benefit_68", Double.class),
//				new ColumnInfo(getDescriptionTaxable_Benefit("69"), "sum( Taxable_Benefit_69 ) AS Taxable_Benefit_69", Double.class),
//				new ColumnInfo(getDescriptionTaxable_Benefit("70"), "sum( Taxable_Benefit_70 ) AS Taxable_Benefit_70", Double.class),
//				new ColumnInfo(getDescriptionTaxable_Benefit("71"), "sum( Taxable_Benefit_71 ) AS Taxable_Benefit_71", Double.class),
//				new ColumnInfo(getDescriptionTaxable_Benefit("71a"), "sum( Taxable_Benefit_71a ) AS Taxable_Benefit_71a", Double.class),
//				new ColumnInfo(getDescriptionTaxable_Benefit("75"), "sum( Taxable_Benefit_75 ) AS Taxable_Benefit_75", Double.class)
			};
	}
	
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
		 */

		m_sql = miniTable.prepareTable( getColumnInfo(),
			//	FROM
			" CHL_YTD_SUM_01 CHL_YTD_SUM_01 " +
			" INNER JOIN P_EMPLOYEE ON CHL_YTD_SUM_01.P_Employee_ID=P_EMPLOYEE.P_EMPLOYEE_ID" +
			" INNER JOIN P_WORKPLACE ON CHL_YTD_SUM_01.P_Workplace_ID=P_WORKPLACE.P_WORKPLACE_ID" +
			" INNER JOIN AD_ORG ON CHL_YTD_SUM_01.AD_Org_ID=AD_ORG.AD_ORG_ID" +
			" INNER JOIN C_ACTIVITY ON CHL_YTD_SUM_01.C_Activity_ID=C_ACTIVITY.C_ACTIVITY_ID" +
			" INNER JOIN P_DEPARTMENT ON CHL_YTD_SUM_01.P_Department_ID=P_DEPARTMENT.P_DEPARTMENT_ID "				
				,
			//	WHERE
			" 1=1 "  ,
         	//	additional where & order in loadTableInfo() 
			true, "CHL_YTD_SUM_01");
		
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
	            this.fieldSelectionPeriod2.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name")));
	            if(rs.getString("PeriodStatus").toUpperCase().equals("O"))
	            {
	                this.fieldSelectionPeriod.setSelectedIndex(index);
	                this.fieldSelectionPeriod2.setSelectedIndex(index);
	                m_Period_ID = rs.getInt("P_Period_ID");
	                m_Period2_ID = rs.getInt("P_Period_ID");
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
	    String sqlOrg = "select ad_org_id,Name  from ad_org where IsActive = 'Y' AND AD_ORG_id = 1000005 ";
	    sqlOrg = sqlOrg + " And ad_org." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
		+ " ORDER BY Name";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlOrg, null);
	        ResultSet rs = stmt.executeQuery();
	        int index = 0;
	        while(rs.next())
	        {
	        	this.fieldOrg.addItem(new KeyNamePair(rs.getInt("ad_org_id"), rs.getString("Name")));
	        	if ( rs.getInt("AD_Org_ID") == 1000005)
	        		this.fieldOrg.setSelectedIndex(index);
	        	index++;
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
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, "loadTableInfo:" + e);
		}
		calculateSelection();

	}   //  loadTableInfo
	
	private PreparedStatement prepareStatement() throws Exception
	{
		//  not yet initialized
		if (m_sql == null)
			return null;

		String sql = m_sql;
		//  Parameters

		//

		int P_Employee_ID = 0;

		Object value = fieldEmployee.getValue();
		if (value != null && value.toString().length() > 0)
		{
			P_Employee_ID = Integer.parseInt( value.toString() );
		}

		Integer period = new Integer(((KeyNamePair)fieldSelectionPeriod.getSelectedItem()).getKey());
		Integer period2 = new Integer(((KeyNamePair)fieldSelectionPeriod2.getSelectedItem()).getKey());

		if (period != null && period.toString().length() > 0)
		{
			m_Period_ID = Integer.parseInt( period.toString() );
			
		}

		if (period2 != null && period2.toString().length() > 0)
		{
			m_Period2_ID = Integer.parseInt( period2.toString() );
		}

		sql += " AND CHL_YTD_SUM_01.P_Period_ID Between " + m_Period_ID + " and "  + m_Period2_ID;
		sql += " and P_WorkPlace.Value != '753' ";
		

		
		if (P_Employee_ID != 0)
			sql += " AND CHL_YTD_SUM_01.P_Employee_ID = " + P_Employee_ID;


		int Org_id = Integer.parseInt(fieldOrg.getValue().toString());
		if (Org_id != 0)
		{
			sql += " and ( CHL_YTD_SUM_01.AD_Org_id = 1000005  )";
		}
		
		int Activity_id = Integer.parseInt(fieldActivity.getValue().toString());	
		if (Activity_id != 0)
		{
			sql += " and CHL_YTD_SUM_01.C_Activity_id =  " + Activity_id;
		}
		
		int Department_id = Integer.parseInt(fieldDept.getValue().toString());	
		if (Department_id != 0)
		{
			sql += " and CHL_YTD_SUM_01.P_Department_id = " + Department_id;
		}

		int Payment_Group_id = Integer.parseInt(fieldGroupPayment.getValue().toString());	
		if (Payment_Group_id != 0)
		{
			sql += " and CHL_YTD_SUM_01.P_Payment_Group_id = " + Payment_Group_id;
		}
		
		
		sql += " Group BY CHL_YTD_SUM_01.AD_CLient_ID, CHL_YTD_SUM_01.AD_ORG_ID, CHL_YTD_SUM_01.P_Workplace_ID, CHL_YTD_SUM_01.P_Employee_ID, AD_Org.Value, P_Workplace.Value, P_Employee.Value";
		
		sql += " Union All ";
	
		//2009.05.19 enlever le gain 66 du total
		// add wcb column
		// deduction 64 - la part employeur
		// order des columns, overtime and vacation at the end.
		sql += " SELECT 999998, " +
				"max(AD_Org.Value) + '-' + max( AD_Org.Name ), " +
				"'', " +
				"max(P_WorkPlace.Value) + '-' + max( P_WorkPlace.Name ), " +
				"'', " +
				"2," +
				"'Total :', " +
				"'', " +
				"SUM( Total_QTY ) - SUM( Gain_02_QTY + Gain_03_QTY + Gain_53_QTY + Gain_99_QTY  ), " +
				"SUM( Gain_02_QTY + Gain_03_QTY + Gain_53_QTY + Gain_99_QTY  ), " +
				"( ISNULL( SUM( Gain_01 ),0) + ISNULL( SUM( Gain_02 ),0) + ISNULL( SUM( Gain_03 ),0) + ISNULL( SUM( Gain_04 ),0) + ISNULL( SUM( Gain_05 ),0) + ISNULL( SUM( Gain_06 ),0) +ISNULL( SUM( Gain_07 ),0) +ISNULL( SUM( Gain_08 ),0) +ISNULL( SUM( Gain_09 ),0) +ISNULL( SUM( Gain_10 ),0) +ISNULL( SUM( Gain_11 ),0) +ISNULL( SUM( Gain_12 ),0) +ISNULL( SUM( Gain_13 ),0) +ISNULL( SUM( Gain_14 ),0) +ISNULL( SUM( Gain_15 ),0) +ISNULL( SUM( Gain_16 ),0) +ISNULL( SUM( Gain_17 ),0) +ISNULL( SUM( Gain_18 ),0) +ISNULL( SUM( Gain_19 ),0) +ISNULL( SUM( Gain_20 ),0) +ISNULL( SUM( Gain_21 ),0) +ISNULL( SUM( Gain_22 ),0) +ISNULL( SUM( Gain_23 ),0) +ISNULL( SUM( Gain_24 ),0) +ISNULL( SUM( Gain_25 ),0) +ISNULL( SUM( Gain_26 ),0) +ISNULL( SUM( Gain_27 ),0) +ISNULL( SUM( Gain_28 ),0) +ISNULL( SUM( Gain_29 ),0) +ISNULL( SUM( Gain_30 ),0) +ISNULL( SUM( Gain_31 ),0) +ISNULL( SUM( Gain_32 ),0) +ISNULL( SUM( Gain_33 ),0) +ISNULL( SUM( Gain_34 ),0) +ISNULL( SUM( Gain_35 ),0) +ISNULL( SUM( Gain_36 ),0) +ISNULL( SUM( Gain_37 ),0) +ISNULL( SUM( Gain_39 ),0) +ISNULL( SUM( Gain_40 ),0) +ISNULL( SUM( Gain_41 ),0) +ISNULL( SUM( Gain_42 ),0) +ISNULL( SUM( Gain_43 ),0) +ISNULL( SUM( Gain_44 ),0) +ISNULL( SUM( Gain_45 ),0) +ISNULL( SUM( Gain_46 ),0) +ISNULL( SUM( Gain_47 ),0) +ISNULL( SUM( Gain_48 ),0) +ISNULL( SUM( Gain_49 ),0) +ISNULL( SUM( Gain_50 ),0) +ISNULL( SUM( Gain_53 ),0) +ISNULL( SUM( Gain_54 ),0) +ISNULL( SUM( Gain_64 ),0) +ISNULL( SUM( Gain_65 ),0) +ISNULL( SUM( Gain_73 ),0) +ISNULL( SUM( Gain_74 ),0) +ISNULL( SUM( Gain_75 ),0) +ISNULL( SUM( Gain_76 ),0) +ISNULL( SUM( Gain_79 ),0) +ISNULL( SUM( Gain_80 ),0) +ISNULL( SUM( Gain_81 ),0) +ISNULL( SUM( Gain_82 ),0) +ISNULL( SUM( Gain_83 ),0) +ISNULL( SUM( Gain_84 ),0) +ISNULL( SUM( Gain_85 ),0) +ISNULL( SUM( Gain_86 ),0) +ISNULL( SUM( Gain_87 ),0) +ISNULL( SUM( Gain_88 ),0) +ISNULL( SUM( Gain_90 ),0) +ISNULL( SUM( Gain_91 ),0) +ISNULL( SUM( Gain_92 ),0) +ISNULL( SUM( Gain_92A),0) +ISNULL( SUM( Gain_93 ),0) +ISNULL( SUM( Gain_94 ),0) +ISNULL( SUM( Gain_95 ),0) +ISNULL( SUM( Gain_96 ),0) +ISNULL( SUM( Gain_97 ),0) +ISNULL( SUM( Gain_98 ),0) +ISNULL( SUM( Gain_99 ),0) +ISNULL( SUM( Gain_I05),0) +ISNULL( SUM( Deduction_01_yeur         ),0) +ISNULL( SUM( Deduction_01a_yeur        ),0) +ISNULL( SUM( Deduction_02_yeur         ),0) +ISNULL( SUM( Deduction_03_yeur         ),0) +ISNULL( SUM( Deduction_06_yeur         ),0) +ISNULL( SUM( Deduction_40_yeur         ),0) +ISNULL( SUM( Deduction_42_yeur         ),0)  +ISNULL( SUM( Deduction_64_yeur              ),0)+ISNULL( SUM( Deduction_63_yeur              ),0) +ISNULL( SUM( Deduction_A01_yeur        ),0) +ISNULL( SUM( Deduction_A05_yeur        ),0) +ISNULL( SUM( Deduction_CSST1_yeur      ),0) +ISNULL( SUM( Deduction_EHT_yeur        ),0)  + ISNULL(sum( [Deduction_H-BC_yeur]     ),0) +ISNULL( SUM( Deduction_FSS_yeur        ),0) +ISNULL( SUM( [Deduction_WSIB-ON_yeur]    ),0) + ISNULL(sum( [Deduction_WCB-AL_yeur]     ),0) + ISNULL(sum( [Deduction_WCB-BC_yeur]     ),0) + ISNULL(sum( [Deduction_WCB-MB_yeur]     ),0) + ISNULL(sum( [Deduction_WCB-NS_yeur]     ),0) + ISNULL(sum( [Deduction_WCB-NU_yeur]     ),0) + ISNULL(sum( [Deduction_WCB-NWT_yeur]),0) + ISNULL(sum( [Deduction_WCB-SK_yeur]     ),0) + ISNULL(sum( [Deduction_WHSCC-NB_yeur]   ),0) + ISNULL(sum( [Deduction_WHSCC-NF_yeur]   ),0) ), " +  // +ISNULL( SUM( Taxable_Benefit_37 ),0) +ISNULL( SUM( Taxable_Benefit_59 ),0) ) +ISNULL( SUM( Deduction_08              ),0) +ISNULL( SUM( Deduction_12              ),0) +ISNULL( SUM( Deduction_13              ),0) +ISNULL( SUM( Deduction_25              ),0) +ISNULL( SUM( Deduction_44              ),0)	
				"SUM( Gain_01 ), "+
				"SUM( Gain_04 ), "+
				"SUM( Gain_05 ), "+
				"SUM( Gain_07 ), "+
				"SUM( Gain_08 ), "+
				"SUM( Gain_09 ), "+
				"SUM( Gain_10 ), "+
				"SUM( Gain_11 ), "+
				"SUM( Gain_12 ), "+
				"SUM( Gain_13 ), "+
				"SUM( Gain_14 ), "+
				"SUM( Gain_15 ), "+
				"SUM( Gain_16 ), "+
				"SUM( Gain_17 ), "+
				"SUM( Gain_18 ), "+
				"SUM( Gain_19 ), "+
				"SUM( Gain_20 ), "+
				"SUM( Gain_21 ), "+
				"SUM( Gain_22 ), "+
				"SUM( Gain_23 ), "+
				"SUM( Gain_24 ), "+
				"SUM( Gain_25 ), "+
				"SUM( Gain_26 ), "+
				"SUM( Gain_27 ), "+
				"SUM( Gain_28 ), "+
				"SUM( Gain_29 ), "+
				"SUM( Gain_30 ), "+
				"SUM( Gain_31 ), "+
				"SUM( Gain_32 ), "+
				"SUM( Gain_33 ), "+
				"SUM( Gain_34 ), "+
				"SUM( Gain_35 ), "+
				"SUM( Gain_36 ), "+
				"SUM( Gain_37 ), "+
				"SUM( Gain_39 ), "+
				"SUM( Gain_40 ), "+
				"SUM( Gain_41 ), "+
				"SUM( Gain_42 ), "+
				"SUM( Gain_43 ), "+
				"SUM( Gain_44 ), "+
				"SUM( Gain_45 ), "+
				"SUM( Gain_46 ), "+
				"SUM( Gain_47 ), "+
				"SUM( Gain_48 ), "+
				"SUM( Gain_49 ), "+
				"SUM( Gain_50 ), "+
				"SUM( Gain_54 ), "+
				"SUM( Gain_64 ), "+
				"SUM( Gain_65 ), "+
				"SUM( Gain_66 ), "+
				"SUM( Gain_73 ), "+
				"SUM( Gain_74 ), "+
				"SUM( Gain_75 ), "+
				"SUM( Gain_76 ), "+
				"SUM( Gain_80 ), "+
				"SUM( Gain_81 ), "+
				"SUM( Gain_83 ), "+
				"SUM( Gain_84 ), "+
				"SUM( Gain_85 ), "+
				"SUM( Gain_86 ), "+
				"SUM( Gain_87 ), "+
				"SUM( Gain_88 ), "+
				"SUM( Gain_91 ), "+
				"SUM( Gain_92 ), "+
				"SUM( Gain_92A), "+
				"SUM( Gain_93 ), "+
				"SUM( Gain_94 ), "+
				"SUM( Gain_95 ), "+
				"SUM( Gain_96 ), "+
				"SUM( Gain_97 ), "+
				"SUM( Gain_98 ), "+
				"SUM( Gain_06 ), "+
				"SUM( Gain_79 ), "+
				"SUM( Gain_82 ), "+
				"SUM( Gain_90 ), "+
				"SUM( Gain_02 ), "+
				"SUM( Gain_03 ), "+
				"SUM( Gain_53 ), "+
				"SUM( Gain_99 ), "+
//				"SUM( Gain_I02), "+
//				"SUM( Gain_I03), "+
//				"SUM( Gain_I04), "+
				"SUM( Gain_I05), "+
//				"SUM( Gain_I06), "+
//				"SUM( Gain_I07), "+

//				"sum( Deduction_01              ), "+
				"sum( Deduction_01_yeur         ), "+
//				"sum( Deduction_01a             ), "+
				"sum( Deduction_01a_yeur        ), "+
//				"sum( Deduction_02              ), "+
				"sum( Deduction_02_yeur         ), "+
//				"sum( Deduction_03              ), "+
				"sum( Deduction_03_yeur         ), "+
//				"sum( Deduction_04              ), "+
//				"sum( Deduction_05              ), "+
//				"sum( Deduction_06              ), "+
				"sum( Deduction_06_yeur         ), "+
//				"sum( Deduction_07              ), "+
//				"sum( Deduction_09              ), "+
//				"sum( Deduction_10              ), "+
//				"sum( Deduction_11              ), "+
//				"sum( Deduction_14              ), "+
//				"sum( Deduction_15              ), "+
//				"sum( Deduction_16              ), "+
//				"sum( Deduction_17              ), "+
//				"sum( Deduction_18              ), "+
//				"sum( Deduction_19              ), "+
//				"sum( Deduction_21              ), "+
//				"sum( Deduction_22              ), "+
//				"sum( Deduction_27              ), "+
//				"sum( Deduction_28              ), "+
//				"sum( Deduction_30              ), "+
//				"sum( Deduction_31              ), "+
//				"sum( Deduction_32              ), "+
//				"sum( Deduction_34              ), "+
//				"sum( Deduction_36              ), "+
//				"sum( Deduction_37              ), "+
//				"sum( Deduction_38              ), "+
//				"sum( Deduction_39              ), "+
//				"sum( Deduction_40              ), "+
				"sum( Deduction_40_yeur         ), "+
//				"sum( Deduction_42              ), "+
				"sum( Deduction_42_yeur         ), "+
//				"sum( Deduction_46              ), "+
//				"sum( Deduction_48              ), "+
//				"sum( Deduction_49              ), "+
//				"sum( Deduction_50              ), "+
//				"sum( Deduction_51              ), "+
//				"sum( Deduction_53              ), "+
//				"sum( Deduction_54              ), "+
//				"sum( Deduction_55              ), "+
//				"sum( Deduction_56              ), "+
//				"sum( Deduction_57              ), "+
//				"sum( Deduction_58              ), "+
//				"sum( Deduction_60              ), "+
//				"sum( Deduction_62              ), "+
//				"sum( Deduction_63              ), "+
				"sum( Deduction_64_yeur         ), "+
				"sum( Deduction_63_yeur         ), "+
//				"sum( Deduction_67              ), "+
//				"sum( Deduction_68              ), "+
//				"sum( Deduction_69              ), "+
//				"sum( Deduction_70              ), "+
//				"sum( Deduction_72              ), "+
//				"sum( Deduction_75              ), "+
//				"sum( Deduction_80              ), "+
//				"sum( Deduction_A01             ), "+
				"sum( Deduction_A01_yeur        ), "+
//				"sum( Deduction_A02             ), "+
//				"sum( Deduction_A02_yeur        ), "+
//				"sum( Deduction_A05             ), "+
				"sum( Deduction_A05_yeur        ), "+
//				"sum( Deduction_A06             ), "+
//				"sum( Deduction_A06_yeur        ), "+
//				"sum( Deduction_A07             ), "+
//				"sum( Deduction_A07_yeur        ), "+
//				"sum( Deduction_A08             ), "+
//				"sum( Deduction_A08_yeur        ), "+
//				"sum( Deduction_A09             ), "+
//				"sum( Deduction_A09_yeur        ), "+
				"sum( Deduction_CSST1_yeur      ), "+
//				"sum( Deduction_CSST2_yeur      ), "+
				"sum( Deduction_EHT_yeur        ), "+
//				"sum( Deduction_Exchequer_yeur  ), "+
				"sum( Deduction_FSS_yeur        ), "+
//				"sum( [Deduction_H-AB_yeur]       ), "+
				"sum( [Deduction_H-BC_yeur]       ), "+
				"sum( [Deduction_WCB-AL_yeur]     ), "+
				"sum( [Deduction_WCB-BC_yeur]     ), "+
				"sum( [Deduction_WCB-MB_yeur]     ), "+
				"sum( [Deduction_WCB-NS_yeur]     ), "+
				"sum( [Deduction_WCB-NU_yeur]     ), "+
				"sum( [Deduction_WCB-NWT_yeur]    ), "+
				"sum( [Deduction_WCB-SK_yeur]     ), "+
				"sum( [Deduction_WHSCC-NB_yeur]   ), "+
				"sum( [Deduction_WHSCC-NF_yeur]   ), "+
				"sum( [Deduction_WSIB-ON_yeur]    ), "+
				"sum( Deduction_08              ), "+
				"sum( Deduction_12              ), "+
				"sum( Deduction_13              ), "+
				"sum( Deduction_25              ), "+
				"sum( Deduction_44              ), "+
				"sum( Taxable_Benefit_37 ) AS Taxable_Benefit_37, "+
//				"sum( Taxable_Benefit_51 ) AS Taxable_Benefit_51, "+
//				"sum( Taxable_Benefit_52 ) AS Taxable_Benefit_52, "+
//				"sum( Taxable_Benefit_57 ) AS Taxable_Benefit_57, "+
//				"sum( Taxable_Benefit_58 ) AS Taxable_Benefit_58, "+
				"sum( Taxable_Benefit_59 ) AS Taxable_Benefit_59, "+
//				"sum( Taxable_Benefit_61 ) AS Taxable_Benefit_61, "+
//				"sum( Taxable_Benefit_62 ) AS Taxable_Benefit_62, "+
//				"sum( Taxable_Benefit_63 ) AS Taxable_Benefit_63, "+
				"sum( Taxable_Benefit_67 ) AS Taxable_Benefit_67 "+
//				"sum( Taxable_Benefit_68 ) AS Taxable_Benefit_68, "+
//				"sum( Taxable_Benefit_69 ) AS Taxable_Benefit_69, "+
//				"sum( Taxable_Benefit_70 ) AS Taxable_Benefit_70, "+
//				"sum( Taxable_Benefit_71 ) AS Taxable_Benefit_71, "+
//				"sum( Taxable_Benefit_71a ) AS Taxable_Benefit_71a, "+
//				"sum( Taxable_Benefit_75 ) AS Taxable_Benefit_75 " + 
				" FROM  CHL_YTD_SUM_01 CHL_YTD_SUM_01  INNER JOIN P_EMPLOYEE ON CHL_YTD_SUM_01.P_Employee_ID=P_EMPLOYEE.P_EMPLOYEE_ID INNER JOIN P_WORKPLACE ON CHL_YTD_SUM_01.P_Workplace_ID=P_WORKPLACE.P_WORKPLACE_ID INNER JOIN AD_ORG ON CHL_YTD_SUM_01.AD_Org_ID=AD_ORG.AD_ORG_ID INNER JOIN C_ACTIVITY ON CHL_YTD_SUM_01.C_Activity_ID=C_ACTIVITY.C_ACTIVITY_ID INNER JOIN P_DEPARTMENT ON CHL_YTD_SUM_01.P_Department_ID=P_DEPARTMENT.P_DEPARTMENT_ID  " +
			   "  WHERE  1=1  AND CHL_YTD_SUM_01.AD_Client_ID IN(1000004,0) AND CHL_YTD_SUM_01.AD_Org_ID IN(1000001,1000003,1000004,1000006,0,1000002,1000005,1000007) and ( CHL_YTD_SUM_01.AD_Org_id = 1000005  ) ";

		sql += " AND CHL_YTD_SUM_01.P_Period_ID Between " + m_Period_ID + " and "  + m_Period2_ID;
		sql += " and P_WorkPlace.Value != '753' ";

		if (Activity_id != 0)
		{
			sql += " and CHL_YTD_SUM_01.C_Activity_id =  " + Activity_id;
		}
		
		if (Department_id != 0)
		{
			sql += " and CHL_YTD_SUM_01.P_Department_id = " + Department_id;
		}

		if (Payment_Group_id != 0)
		{
			sql += " and CHL_YTD_SUM_01.P_Payment_Group_id = " + Payment_Group_id;
		}

		sql += "  Group BY CHL_YTD_SUM_01.AD_CLient_ID, CHL_YTD_SUM_01.AD_ORG_ID, CHL_YTD_SUM_01.P_Workplace_ID, AD_Org.Value, P_Workplace.Value " ;

		sql += " Union All ";
		
		sql += " SELECT 999999, " +
				"max(AD_Org.Value) + '-' + max( AD_Org.Name ), " +
				"'-', " +
				"'Total :', " +
				"'', " +
				"'3'," +
				"'Total :', " +
				"'', " +
				"SUM( Total_QTY ) - SUM( Gain_02_QTY + Gain_03_QTY + Gain_53_QTY + Gain_99_QTY  ), " +
				"SUM( Gain_02_QTY + Gain_03_QTY + Gain_53_QTY + Gain_99_QTY  ), " +
				"( ISNULL( SUM( Gain_01 ),0) + ISNULL( SUM( Gain_02 ),0) + ISNULL( SUM( Gain_03 ),0) + ISNULL( SUM( Gain_04 ),0) + ISNULL( SUM( Gain_05 ),0) + ISNULL( SUM( Gain_06 ),0) +ISNULL( SUM( Gain_07 ),0) +ISNULL( SUM( Gain_08 ),0) +ISNULL( SUM( Gain_09 ),0) +ISNULL( SUM( Gain_10 ),0) +ISNULL( SUM( Gain_11 ),0) +ISNULL( SUM( Gain_12 ),0) +ISNULL( SUM( Gain_13 ),0) +ISNULL( SUM( Gain_14 ),0) +ISNULL( SUM( Gain_15 ),0) +ISNULL( SUM( Gain_16 ),0) +ISNULL( SUM( Gain_17 ),0) +ISNULL( SUM( Gain_18 ),0) +ISNULL( SUM( Gain_19 ),0) +ISNULL( SUM( Gain_20 ),0) +ISNULL( SUM( Gain_21 ),0) +ISNULL( SUM( Gain_22 ),0) +ISNULL( SUM( Gain_23 ),0) +ISNULL( SUM( Gain_24 ),0) +ISNULL( SUM( Gain_25 ),0) +ISNULL( SUM( Gain_26 ),0) +ISNULL( SUM( Gain_27 ),0) +ISNULL( SUM( Gain_28 ),0) +ISNULL( SUM( Gain_29 ),0) +ISNULL( SUM( Gain_30 ),0) +ISNULL( SUM( Gain_31 ),0) +ISNULL( SUM( Gain_32 ),0) +ISNULL( SUM( Gain_33 ),0) +ISNULL( SUM( Gain_34 ),0) +ISNULL( SUM( Gain_35 ),0) +ISNULL( SUM( Gain_36 ),0) +ISNULL( SUM( Gain_37 ),0) +ISNULL( SUM( Gain_39 ),0) +ISNULL( SUM( Gain_40 ),0) +ISNULL( SUM( Gain_41 ),0) +ISNULL( SUM( Gain_42 ),0) +ISNULL( SUM( Gain_43 ),0) +ISNULL( SUM( Gain_44 ),0) +ISNULL( SUM( Gain_45 ),0) +ISNULL( SUM( Gain_46 ),0) +ISNULL( SUM( Gain_47 ),0) +ISNULL( SUM( Gain_48 ),0) +ISNULL( SUM( Gain_49 ),0) +ISNULL( SUM( Gain_50 ),0) +ISNULL( SUM( Gain_53 ),0) +ISNULL( SUM( Gain_54 ),0) +ISNULL( SUM( Gain_64 ),0) +ISNULL( SUM( Gain_65 ),0) +ISNULL( SUM( Gain_73 ),0) +ISNULL( SUM( Gain_74 ),0) +ISNULL( SUM( Gain_75 ),0) +ISNULL( SUM( Gain_76 ),0) +ISNULL( SUM( Gain_79 ),0) +ISNULL( SUM( Gain_80 ),0) +ISNULL( SUM( Gain_81 ),0) +ISNULL( SUM( Gain_82 ),0) +ISNULL( SUM( Gain_83 ),0) +ISNULL( SUM( Gain_84 ),0) +ISNULL( SUM( Gain_85 ),0) +ISNULL( SUM( Gain_86 ),0) +ISNULL( SUM( Gain_87 ),0) +ISNULL( SUM( Gain_88 ),0) +ISNULL( SUM( Gain_90 ),0) +ISNULL( SUM( Gain_91 ),0) +ISNULL( SUM( Gain_92 ),0) +ISNULL( SUM( Gain_92A),0) +ISNULL( SUM( Gain_93 ),0) +ISNULL( SUM( Gain_94 ),0) +ISNULL( SUM( Gain_95 ),0) +ISNULL( SUM( Gain_96 ),0) +ISNULL( SUM( Gain_97 ),0) +ISNULL( SUM( Gain_98 ),0) +ISNULL( SUM( Gain_99 ),0) +ISNULL( SUM( Gain_I05),0) +ISNULL( SUM( Deduction_01_yeur         ),0) +ISNULL( SUM( Deduction_01a_yeur        ),0) +ISNULL( SUM( Deduction_02_yeur         ),0) +ISNULL( SUM( Deduction_03_yeur         ),0) +ISNULL( SUM( Deduction_06_yeur         ),0)  +ISNULL( SUM( Deduction_40_yeur         ),0) +ISNULL( SUM( Deduction_42_yeur         ),0) +ISNULL( SUM( Deduction_64_yeur              ),0) +ISNULL( SUM( Deduction_63_yeur              ),0) +ISNULL( SUM( Deduction_A01_yeur        ),0) +ISNULL( SUM( Deduction_A05_yeur        ),0) +ISNULL( SUM( Deduction_CSST1_yeur      ),0) +ISNULL( SUM( Deduction_EHT_yeur        ),0) + ISNULL(sum( [Deduction_H-BC_yeur]     ),0) +ISNULL( SUM( Deduction_FSS_yeur        ),0) +ISNULL( SUM( [Deduction_WSIB-ON_yeur]    ),0) + ISNULL(sum( [Deduction_WCB-AL_yeur]     ),0) + ISNULL(sum( [Deduction_WCB-BC_yeur]     ),0) + ISNULL(sum( [Deduction_WCB-MB_yeur]     ),0) + ISNULL(sum( [Deduction_WCB-NS_yeur]     ),0) + ISNULL(sum( [Deduction_WCB-NU_yeur]     ),0) + ISNULL(sum( [Deduction_WCB-NWT_yeur]),0) + ISNULL(sum( [Deduction_WCB-SK_yeur]     ),0) + ISNULL(sum( [Deduction_WHSCC-NB_yeur]   ),0) + ISNULL(sum( [Deduction_WHSCC-NF_yeur]   ),0) ) , "+  //  +ISNULL( SUM( Taxable_Benefit_37 ),0) +ISNULL( SUM( Taxable_Benefit_59 ),0) )  +ISNULL( SUM( Deduction_44              ),0) +ISNULL( SUM( Deduction_08              ),0) +ISNULL( SUM( Deduction_12              ),0) +ISNULL( SUM( Deduction_13              ),0) +ISNULL( SUM( Deduction_25              ),0)  
			    "SUM( Gain_01 ), "+
				"SUM( Gain_04 ), "+
				"SUM( Gain_05 ), "+
				"SUM( Gain_07 ), "+
				"SUM( Gain_08 ), "+
				"SUM( Gain_09 ), "+
				"SUM( Gain_10 ), "+
				"SUM( Gain_11 ), "+
				"SUM( Gain_12 ), "+
				"SUM( Gain_13 ), "+
				"SUM( Gain_14 ), "+
				"SUM( Gain_15 ), "+
				"SUM( Gain_16 ), "+
				"SUM( Gain_17 ), "+
				"SUM( Gain_18 ), "+
				"SUM( Gain_19 ), "+
				"SUM( Gain_20 ), "+
				"SUM( Gain_21 ), "+
				"SUM( Gain_22 ), "+
				"SUM( Gain_23 ), "+
				"SUM( Gain_24 ), "+
				"SUM( Gain_25 ), "+
				"SUM( Gain_26 ), "+
				"SUM( Gain_27 ), "+
				"SUM( Gain_28 ), "+
				"SUM( Gain_29 ), "+
				"SUM( Gain_30 ), "+
				"SUM( Gain_31 ), "+
				"SUM( Gain_32 ), "+
				"SUM( Gain_33 ), "+
				"SUM( Gain_34 ), "+
				"SUM( Gain_35 ), "+
				"SUM( Gain_36 ), "+
				"SUM( Gain_37 ), "+
				"SUM( Gain_39 ), "+
				"SUM( Gain_40 ), "+
				"SUM( Gain_41 ), "+
				"SUM( Gain_42 ), "+
				"SUM( Gain_43 ), "+
				"SUM( Gain_44 ), "+
				"SUM( Gain_45 ), "+
				"SUM( Gain_46 ), "+
				"SUM( Gain_47 ), "+
				"SUM( Gain_48 ), "+
				"SUM( Gain_49 ), "+
				"SUM( Gain_50 ), "+
				"SUM( Gain_54 ), "+
				"SUM( Gain_64 ), "+
				"SUM( Gain_65 ), "+
				"SUM( Gain_66 ), "+
				"SUM( Gain_73 ), "+
				"SUM( Gain_74 ), "+
				"SUM( Gain_75 ), "+
				"SUM( Gain_76 ), "+
				"SUM( Gain_80 ), "+
				"SUM( Gain_81 ), "+
				"SUM( Gain_83 ), "+
				"SUM( Gain_84 ), "+
				"SUM( Gain_85 ), "+
				"SUM( Gain_86 ), "+
				"SUM( Gain_87 ), "+
				"SUM( Gain_88 ), "+
				"SUM( Gain_91 ), "+
				"SUM( Gain_92 ), "+
				"SUM( Gain_92A), "+
				"SUM( Gain_93 ), "+
				"SUM( Gain_94 ), "+
				"SUM( Gain_95 ), "+
				"SUM( Gain_96 ), "+
				"SUM( Gain_97 ), "+
				"SUM( Gain_98 ), "+
				"SUM( Gain_06 ), "+
				"SUM( Gain_79 ), "+
				"SUM( Gain_82 ), "+
				"SUM( Gain_90 ), "+
				"SUM( Gain_02 ), "+
				"SUM( Gain_03 ), "+
				"SUM( Gain_53 ), "+
				"SUM( Gain_99 ), "+
				
//				"SUM( Gain_I02), "+
//				"SUM( Gain_I03), "+
//				"SUM( Gain_I04), "+
				"SUM( Gain_I05), "+
//				"SUM( Gain_I06), "+
//				"SUM( Gain_I07), "+

//				"sum( Deduction_01              ), "+
				"sum( Deduction_01_yeur         ), "+
//				"sum( Deduction_01a             ), "+
				"sum( Deduction_01a_yeur        ), "+
//				"sum( Deduction_02              ), "+
				"sum( Deduction_02_yeur         ), "+
//				"sum( Deduction_03              ), "+
				"sum( Deduction_03_yeur         ), "+
//				"sum( Deduction_04              ), "+
//				"sum( Deduction_05              ), "+
//				"sum( Deduction_06              ), "+
				"sum( Deduction_06_yeur         ), "+
//				"sum( Deduction_07              ), "+
//				"sum( Deduction_09              ), "+
//				"sum( Deduction_10              ), "+
//				"sum( Deduction_11              ), "+
//				"sum( Deduction_14              ), "+
//				"sum( Deduction_15              ), "+
//				"sum( Deduction_16              ), "+
//				"sum( Deduction_17              ), "+
//				"sum( Deduction_18              ), "+
//				"sum( Deduction_19              ), "+
//				"sum( Deduction_21              ), "+
//				"sum( Deduction_22              ), "+
//				"sum( Deduction_27              ), "+
//				"sum( Deduction_28              ), "+
//				"sum( Deduction_30              ), "+
//				"sum( Deduction_31              ), "+
//				"sum( Deduction_32              ), "+
//				"sum( Deduction_34              ), "+
//				"sum( Deduction_36              ), "+
//				"sum( Deduction_37              ), "+
//				"sum( Deduction_38              ), "+
//				"sum( Deduction_39              ), "+
//				"sum( Deduction_40              ), "+
				"sum( Deduction_40_yeur         ), "+
//				"sum( Deduction_42              ), "+
				"sum( Deduction_42_yeur         ), "+
//				"sum( Deduction_46              ), "+
//				"sum( Deduction_48              ), "+
//				"sum( Deduction_49              ), "+
//				"sum( Deduction_50              ), "+
//				"sum( Deduction_51              ), "+
//				"sum( Deduction_53              ), "+
//				"sum( Deduction_54              ), "+
//				"sum( Deduction_55              ), "+
//				"sum( Deduction_56              ), "+
//				"sum( Deduction_57              ), "+
//				"sum( Deduction_58              ), "+
//				"sum( Deduction_60              ), "+
//				"sum( Deduction_62              ), "+
//				"sum( Deduction_63              ), "+
				"sum( Deduction_64_yeur              ), "+
				"sum( Deduction_63_yeur              ), "+
//				"sum( Deduction_67              ), "+
//				"sum( Deduction_68              ), "+
//				"sum( Deduction_69              ), "+
//				"sum( Deduction_70              ), "+
//				"sum( Deduction_72              ), "+
//				"sum( Deduction_75              ), "+
//				"sum( Deduction_80              ), "+
//				"sum( Deduction_A01             ), "+
				"sum( Deduction_A01_yeur        ), "+
//				"sum( Deduction_A02             ), "+
//				"sum( Deduction_A02_yeur        ), "+
//				"sum( Deduction_A05             ), "+
				"sum( Deduction_A05_yeur        ), "+
//				"sum( Deduction_A06             ), "+
//				"sum( Deduction_A06_yeur        ), "+
//				"sum( Deduction_A07             ), "+
//				"sum( Deduction_A07_yeur        ), "+
//				"sum( Deduction_A08             ), "+
//				"sum( Deduction_A08_yeur        ), "+
//				"sum( Deduction_A09             ), "+
//				"sum( Deduction_A09_yeur        ), "+
				"sum( Deduction_CSST1_yeur      ), "+
//				"sum( Deduction_CSST2_yeur      ), "+
				"sum( Deduction_EHT_yeur        ), "+
//				"sum( Deduction_Exchequer_yeur  ), "+
				"sum( Deduction_FSS_yeur        ), "+
//				"sum( [Deduction_H-AB_yeur]       ), "+
				"sum( [Deduction_H-BC_yeur]       ), "+
				"sum( [Deduction_WCB-AL_yeur]     ), "+
				"sum( [Deduction_WCB-BC_yeur]     ), "+
				"sum( [Deduction_WCB-MB_yeur]     ), "+
				"sum( [Deduction_WCB-NS_yeur]     ), "+
				"sum( [Deduction_WCB-NU_yeur]     ), "+
				"sum( [Deduction_WCB-NWT_yeur]    ), "+
				"sum( [Deduction_WCB-SK_yeur]     ), "+
				"sum( [Deduction_WHSCC-NB_yeur]   ), "+
				"sum( [Deduction_WHSCC-NF_yeur]   ), "+
				"sum( [Deduction_WSIB-ON_yeur]    ), "+
				"sum( Deduction_08              ), "+
				"sum( Deduction_12              ), "+
				"sum( Deduction_13              ), "+
				"sum( Deduction_25              ), "+
				"sum( Deduction_44              ), "+
				"sum( Taxable_Benefit_37 ) AS Taxable_Benefit_37, "+
//				"sum( Taxable_Benefit_51 ) AS Taxable_Benefit_51, "+
//				"sum( Taxable_Benefit_52 ) AS Taxable_Benefit_52, "+
//				"sum( Taxable_Benefit_57 ) AS Taxable_Benefit_57, "+
//				"sum( Taxable_Benefit_58 ) AS Taxable_Benefit_58, "+
				"sum( Taxable_Benefit_59 ) AS Taxable_Benefit_59, "+
//				"sum( Taxable_Benefit_61 ) AS Taxable_Benefit_61, "+
//				"sum( Taxable_Benefit_62 ) AS Taxable_Benefit_62, "+
//				"sum( Taxable_Benefit_63 ) AS Taxable_Benefit_63, "+
				"sum( Taxable_Benefit_67 ) AS Taxable_Benefit_67 "+
//				"sum( Taxable_Benefit_68 ) AS Taxable_Benefit_68, "+
//				"sum( Taxable_Benefit_69 ) AS Taxable_Benefit_69, "+
//				"sum( Taxable_Benefit_70 ) AS Taxable_Benefit_70, "+
//				"sum( Taxable_Benefit_71 ) AS Taxable_Benefit_71, "+
//				"sum( Taxable_Benefit_71a ) AS Taxable_Benefit_71a, "+
//				"sum( Taxable_Benefit_75 ) AS Taxable_Benefit_75 " + 

				" FROM  CHL_YTD_SUM_01 CHL_YTD_SUM_01  INNER JOIN P_EMPLOYEE ON CHL_YTD_SUM_01.P_Employee_ID=P_EMPLOYEE.P_EMPLOYEE_ID INNER JOIN P_WORKPLACE ON CHL_YTD_SUM_01.P_Workplace_ID=P_WORKPLACE.P_WORKPLACE_ID INNER JOIN AD_ORG ON CHL_YTD_SUM_01.AD_Org_ID=AD_ORG.AD_ORG_ID INNER JOIN C_ACTIVITY ON CHL_YTD_SUM_01.C_Activity_ID=C_ACTIVITY.C_ACTIVITY_ID INNER JOIN P_DEPARTMENT ON CHL_YTD_SUM_01.P_Department_ID=P_DEPARTMENT.P_DEPARTMENT_ID  " +
				"WHERE  1=1  AND CHL_YTD_SUM_01.AD_Client_ID IN(1000004,0) AND CHL_YTD_SUM_01.AD_Org_ID IN(1000001,1000003,1000004,1000006,0,1000002,1000005,1000007) and ( CHL_YTD_SUM_01.AD_Org_id = 1000005   ) "; 

		sql += " AND CHL_YTD_SUM_01.P_Period_ID Between " + m_Period_ID + " and "  + m_Period2_ID;
		sql += " and P_WorkPlace.Value != '753' ";

		if (Activity_id != 0)
		{
			sql += " and CHL_YTD_SUM_01.C_Activity_id =  " + Activity_id;
		}
		
		if (Department_id != 0)
		{
			sql += " and CHL_YTD_SUM_01.P_Department_id = " + Department_id;
		}

		if (Payment_Group_id != 0)
		{
			sql += " and CHL_YTD_SUM_01.P_Payment_Group_id = " + Payment_Group_id;
		}

		sql += " Group BY CHL_YTD_SUM_01.AD_CLient_ID, CHL_YTD_SUM_01.AD_ORG_ID, AD_Org.Value " ;

/*		
		sql += " Union All ";
		
		sql += " SELECT CHL_YTD_SUM_01.P_Employee_ID, " +
				"'-[' + max(AD_Org.Value) + '-' + max( AD_Org.Name ) + ']', " +
				"max(P_Department.Value) + '-' + max( P_Department.Name ), " +
				"max(P_WorkPlace.Value) + '-' + max( P_WorkPlace.Name ), " +
				"max(C_Activity.Value) + '-' + max( C_Activity.Name ), " +
				"0," +
				"'-[' + max(P_Employee.Name) + ']', " +
				"max(P_Employee.Value), " +
				"null , " +
				"SUM( Gain_01 ), "+
				"SUM( Gain_02 ), "+
				"SUM( Gain_03 ), "+
				"SUM( Gain_04 ), "+
				"SUM( Gain_05 ), "+
				"SUM( Gain_06 ), "+
				"SUM( Gain_07 ), "+
				"SUM( Gain_08 ), "+
				"SUM( Gain_09 ), "+
				"SUM( Gain_10 ), "+
				"SUM( Gain_11 ), "+
				"SUM( Gain_12 ), "+
				"SUM( Gain_13 ), "+
				"SUM( Gain_14 ), "+
				"SUM( Gain_15 ), "+
				"SUM( Gain_16 ), "+
				"SUM( Gain_17 ), "+
				"SUM( Gain_18 ), "+
				"SUM( Gain_19 ), "+
				"SUM( Gain_20 ), "+
				"SUM( Gain_21 ), "+
				"SUM( Gain_22 ), "+
				"SUM( Gain_23 ), "+
				"SUM( Gain_24 ), "+
				"SUM( Gain_25 ), "+
				"SUM( Gain_26 ), "+
				"SUM( Gain_27 ), "+
				"SUM( Gain_28 ), "+
				"SUM( Gain_29 ), "+
				"SUM( Gain_30 ), "+
				"SUM( Gain_31 ), "+
				"SUM( Gain_32 ), "+
				"SUM( Gain_33 ), "+
				"SUM( Gain_34 ), "+
				"SUM( Gain_35 ), "+
				"SUM( Gain_36 ), "+
				"SUM( Gain_37 ), "+
				"SUM( Gain_39 ), "+
				"SUM( Gain_40 ), "+
				"SUM( Gain_41 ), "+
				"SUM( Gain_42 ), "+
				"SUM( Gain_43 ), "+
				"SUM( Gain_44 ), "+
				"SUM( Gain_45 ), "+
				"SUM( Gain_46 ), "+
				"SUM( Gain_47 ), "+
				"SUM( Gain_48 ), "+
				"SUM( Gain_49 ), "+
				"SUM( Gain_50 ), "+
				"SUM( Gain_53 ), "+
				"SUM( Gain_54 ), "+
				"SUM( Gain_64 ), "+
				"SUM( Gain_65 ), "+
				"SUM( Gain_66 ), "+
				"SUM( Gain_73 ), "+
				"SUM( Gain_74 ), "+
				"SUM( Gain_75 ), "+
				"SUM( Gain_76 ), "+
				"SUM( Gain_79 ), "+
				"SUM( Gain_80 ), "+
				"SUM( Gain_81 ), "+
				"SUM( Gain_82 ), "+
				"SUM( Gain_83 ), "+
				"SUM( Gain_84 ), "+
				"SUM( Gain_85 ), "+
				"SUM( Gain_86 ), "+
				"SUM( Gain_87 ), "+
				"SUM( Gain_88 ), "+
				"SUM( Gain_90 ), "+
				"SUM( Gain_91 ), "+
				"SUM( Gain_92 ), "+
				"SUM( Gain_92A), "+
				"SUM( Gain_93 ), "+
				"SUM( Gain_94 ), "+
				"SUM( Gain_95 ), "+
				"SUM( Gain_96 ), "+
				"SUM( Gain_97 ), "+
				"SUM( Gain_98 ), "+
				"SUM( Gain_99 ), "+
//				"SUM( Gain_I02), "+
//				"SUM( Gain_I03), "+
//				"SUM( Gain_I04), "+
				"SUM( Gain_I05), "+
//				"SUM( Gain_I06), "+
//				"SUM( Gain_I07), "+

//				"sum( Deduction_01              ), "+
				"sum( Deduction_01_yeur         ), "+
//				"sum( Deduction_01a             ), "+
				"sum( Deduction_01a_yeur        ), "+
//				"sum( Deduction_02              ), "+
				"sum( Deduction_02_yeur         ), "+
//				"sum( Deduction_03              ), "+
				"sum( Deduction_03_yeur         ), "+
//				"sum( Deduction_04              ), "+
//				"sum( Deduction_05              ), "+
//				"sum( Deduction_06              ), "+
				"sum( Deduction_06_yeur         ), "+
//				"sum( Deduction_07              ), "+
				"sum( Deduction_08              ), "+
//				"sum( Deduction_09              ), "+
//				"sum( Deduction_10              ), "+
//				"sum( Deduction_11              ), "+
				"sum( Deduction_12              ), "+
				"sum( Deduction_13              ), "+
//				"sum( Deduction_14              ), "+
//				"sum( Deduction_15              ), "+
//				"sum( Deduction_16              ), "+
//				"sum( Deduction_17              ), "+
//				"sum( Deduction_18              ), "+
//				"sum( Deduction_19              ), "+
//				"sum( Deduction_21              ), "+
//				"sum( Deduction_22              ), "+
				"sum( Deduction_25              ), "+
//				"sum( Deduction_27              ), "+
//				"sum( Deduction_28              ), "+
//				"sum( Deduction_30              ), "+
//				"sum( Deduction_31              ), "+
//				"sum( Deduction_32              ), "+
//				"sum( Deduction_34              ), "+
//				"sum( Deduction_36              ), "+
//				"sum( Deduction_37              ), "+
//				"sum( Deduction_38              ), "+
//				"sum( Deduction_39              ), "+
//				"sum( Deduction_40              ), "+
				"sum( Deduction_40_yeur         ), "+
//				"sum( Deduction_42              ), "+
				"sum( Deduction_42_yeur         ), "+
				"sum( Deduction_44              ), "+
//				"sum( Deduction_46              ), "+
//				"sum( Deduction_48              ), "+
//				"sum( Deduction_49              ), "+
//				"sum( Deduction_50              ), "+
//				"sum( Deduction_51              ), "+
//				"sum( Deduction_53              ), "+
//				"sum( Deduction_54              ), "+
//				"sum( Deduction_55              ), "+
//				"sum( Deduction_56              ), "+
//				"sum( Deduction_57              ), "+
//				"sum( Deduction_58              ), "+
//				"sum( Deduction_60              ), "+
//				"sum( Deduction_62              ), "+
//				"sum( Deduction_63              ), "+
				"sum( Deduction_64_yeur              ), "+
				"sum( Deduction_63_yeur              ), "+
//				"sum( Deduction_67              ), "+
//				"sum( Deduction_68              ), "+
//				"sum( Deduction_69              ), "+
//				"sum( Deduction_70              ), "+
//				"sum( Deduction_72              ), "+
//				"sum( Deduction_75              ), "+
//				"sum( Deduction_80              ), "+
//				"sum( Deduction_A01             ), "+
				"sum( Deduction_A01_yeur        ), "+
//				"sum( Deduction_A02             ), "+
//				"sum( Deduction_A02_yeur        ), "+
//				"sum( Deduction_A05             ), "+
				"sum( Deduction_A05_yeur        ), "+
//				"sum( Deduction_A06             ), "+
//				"sum( Deduction_A06_yeur        ), "+
//				"sum( Deduction_A07             ), "+
//				"sum( Deduction_A07_yeur        ), "+
//				"sum( Deduction_A08             ), "+
//				"sum( Deduction_A08_yeur        ), "+
//				"sum( Deduction_A09             ), "+
//				"sum( Deduction_A09_yeur        ), "+
				"sum( Deduction_CSST1_yeur      ), "+
//				"sum( Deduction_CSST2_yeur      ), "+
				"sum( Deduction_EHT_yeur        ), "+
//				"sum( Deduction_Exchequer_yeur  ), "+
				"sum( Deduction_FSS_yeur        ), "+
//				"sum( [Deduction_H-AB_yeur]       ), "+
				"sum( [Deduction_H-BC_yeur]       ), "+
				"sum( [Deduction_WCB-AL_yeur]     ), "+
				"sum( [Deduction_WCB-BC_yeur]     ), "+
				"sum( [Deduction_WCB-MB_yeur]     ), "+
				"sum( [Deduction_WCB-NS_yeur]     ), "+
				"sum( [Deduction_WCB-NU_yeur]     ), "+
				"sum( [Deduction_WCB-NWT_yeur]    ), "+
				"sum( [Deduction_WCB-SK_yeur]     ), "+
				"sum( [Deduction_WHSCC-NB_yeur]   ), "+
				"sum( [Deduction_WHSCC-NF_yeur]   ), "+
				"sum( [Deduction_WSIB-ON_yeur]    ), "+
				"sum( Taxable_Benefit_37 ) AS Taxable_Benefit_37, "+
//				"sum( Taxable_Benefit_51 ) AS Taxable_Benefit_51, "+
//				"sum( Taxable_Benefit_52 ) AS Taxable_Benefit_52, "+
//				"sum( Taxable_Benefit_57 ) AS Taxable_Benefit_57, "+
//				"sum( Taxable_Benefit_58 ) AS Taxable_Benefit_58, "+
				"sum( Taxable_Benefit_59 ) AS Taxable_Benefit_59, "+
//				"sum( Taxable_Benefit_61 ) AS Taxable_Benefit_61, "+
//				"sum( Taxable_Benefit_62 ) AS Taxable_Benefit_62, "+
//				"sum( Taxable_Benefit_63 ) AS Taxable_Benefit_63, "+
				"sum( Taxable_Benefit_67 ) AS Taxable_Benefit_67 "+
//				"sum( Taxable_Benefit_68 ) AS Taxable_Benefit_68, "+
//				"sum( Taxable_Benefit_69 ) AS Taxable_Benefit_69, "+
//				"sum( Taxable_Benefit_70 ) AS Taxable_Benefit_70, "+
//				"sum( Taxable_Benefit_71 ) AS Taxable_Benefit_71, "+
//				"sum( Taxable_Benefit_71a ) AS Taxable_Benefit_71a, "+
//				"sum( Taxable_Benefit_75 ) AS Taxable_Benefit_75 " + 
				" FROM  CHL_YTD_SUM_01 CHL_YTD_SUM_01  INNER JOIN P_EMPLOYEE ON CHL_YTD_SUM_01.P_Employee_ID=P_EMPLOYEE.P_EMPLOYEE_ID INNER JOIN P_WORKPLACE ON CHL_YTD_SUM_01.P_Workplace_ID=P_WORKPLACE.P_WORKPLACE_ID INNER JOIN AD_ORG ON CHL_YTD_SUM_01.AD_Org_ID=AD_ORG.AD_ORG_ID INNER JOIN C_ACTIVITY ON CHL_YTD_SUM_01.C_Activity_ID=C_ACTIVITY.C_ACTIVITY_ID INNER JOIN P_DEPARTMENT ON CHL_YTD_SUM_01.P_Department_ID=P_DEPARTMENT.P_DEPARTMENT_ID  " +
			   "  WHERE  1=1  AND CHL_YTD_SUM_01.AD_Client_ID IN(1000004,0) AND CHL_YTD_SUM_01.AD_Org_ID IN(1000001,1000003,1000004,1000006,0,1000002,1000005,1000007) and ( P_Employee.Value in ('035758','070847', '026891', '043851', '070472','070641','036632','000712') ) ";

		sql += " AND CHL_YTD_SUM_01.P_Period_ID Between " + m_Period_ID + " and "  + m_Period2_ID;

		sql += "  Group BY CHL_YTD_SUM_01.AD_CLient_ID, CHL_YTD_SUM_01.AD_ORG_ID, CHL_YTD_SUM_01.P_Workplace_ID, CHL_YTD_SUM_01.P_Employee_ID, AD_Org.Value, P_Workplace.Value, P_Employee.Value " ;
*/		
		
		sql += " ORDER BY 2,4, 6  ";

		
		PreparedStatement pstmt = DB.prepareStatement(sql, null);

		s_log.info( sql );
		
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
			Integer period2 = new Integer(((KeyNamePair)fieldSelectionPeriod2.getSelectedItem()).getKey());

			m_Period_ID = Integer.parseInt( period.toString() );
			m_Period2_ID = Integer.parseInt( period2.toString() );
	
			
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
			        fileWriter.write(" <Worksheet ss:Name=\"" + Msg.translate(Env.getCtx(), "P_Credits_ID") + "\">\n");
			        fileWriter.write("  <Table ss:ExpandedColumnCount=\"" + rsMetaData.getColumnCount() + "\">\n");

			        // generation des entêtes de colonnes
			        fileWriter.write("<Row>\n");
			        ColumnInfo[] colInfo = this.getColumnInfo();
			        for(int i = 1; i <= rsMetaData.getColumnCount() ; i++)
			        {
			        	String colName = colInfo[i-1].getColHeader();
			        	if(!colName.endsWith("_ID") && !colName.equals(" "))
			        		fileWriter.write("<Cell><Data ss:Type=\"String\">" + colName + "</Data></Cell>\n");
			        }
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

			        String buffer = "";
			        ColumnInfo[] colInfo = this.getColumnInfo();
			        for(int i = 1; i <= rsMetaData.getColumnCount() ; i++)
			        {
			        	String colName = colInfo[i-1].getColHeader();
			        	if(!colName.endsWith("_ID") && !colName.equals(" "))
			        		buffer += "\"" + colName + "\"," ;
			        }

		            fileWriter.write(buffer + "\n");

			        SimpleDateFormat f = new SimpleDateFormat ("MM/dd/yyyy");

			        while(rs.next())
			        {
			        	buffer = "";
			            // On crée chaque ligne dans le fichier selon le format d'un .csv
			            for(int i = 1; i <= rsMetaData.getColumnCount() ; i++)
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
//	                	buffer += "\"" + f.format(rs.getTimestamp(rsMetaData.getColumnCount()))  + "\"";

//			            buffer += "\"" + rs.getString(rsMetaData.getColumnCount()) + "\"";
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
//			if (id.isSelected())
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

	private String getDescriptionGain( String value )
	{
		String sql = "Select value , name From P_Gain Where Value = '" + value + "'";
		String gain = null;
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	        	gain = rs.getString(1) + " " + rs.getString(2);
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
	        s_log.log(Level.SEVERE, "GetDescriptionGain", e);
	    }
		return gain;
	}

	private String getDescriptionDeduction( String value )
	{
		String sql = "Select value , name From P_Deduction Where Value = '" + value + "'";
		String deduction = null;
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	        	deduction = rs.getString(1) + " " + rs.getString(2);
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
	        s_log.log(Level.SEVERE, "GetDescriptionDeduction", e);
	    }
		return deduction;
	}

	private String getDescriptionTaxable_Benefit( String value )
	{
		String sql = "Select value , name From P_Taxable_Benefit Where Value = '" + value + "'";
		String Taxable_Benefit = null;
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	        	Taxable_Benefit = rs.getString(1) + " " + rs.getString(2);
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
	        s_log.log(Level.SEVERE, "GetDescriptionTaxable_Benefit", e);
	    }
		return Taxable_Benefit;
	}

}
