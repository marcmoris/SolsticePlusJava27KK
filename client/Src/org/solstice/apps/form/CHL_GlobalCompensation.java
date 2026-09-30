
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
import java.util.logging.Level;

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

import solstice.model.P_Period;
import solstice.process.PgiUtil;

/**
 *  Consultation des soldes de banques
 *
 *  @author Marc Morissette
 *  @version $Id: CHL_GlobalCompensation.java,v 1.2 2008/10/08 17:32:21 marmor01 Exp $
 */
public class CHL_GlobalCompensation extends CPanel
	implements FormPanel, ActionListener, TableModelListener, ASyncProcess
{
//	private DialogButton bExport = new DialogButton ("export", Msg.translate(Env.getLanguage(Env.getCtx()), "ExportExcel"), Env.getImageIcon("Export24.gif"), 0);
	private JButton bExport = ConfirmPanel.createExportButton(true);

	/**
	 *  RecognitionAllowance Constructor
	 */
	public CHL_GlobalCompensation()
	{
	}   //  RecognitionAllowance

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		s_log.log(Level.INFO, "RecognitionAllowance.init");
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

			this.miniTable.getTableHeader().addMouseListener(new ColumnChecker(this.miniTable));

		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE, "RecognitionAllowance.init" + e);
		}
	}	//	init

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (CHL_GlobalCompensation.class);
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;

	/** Format                  */
//	private DecimalFormat   m_format = DisplayType.getNumberFormat(DisplayType.Amount); 
	private DecimalFormat   m_format = DisplayType.getNumberFormat(DisplayType.Quantity); 
	/** SQL for Query           */
	private String          m_sql;
	/** Number of selected rows */
	private int             m_noSelected = 0;
	/** Client ID               */
	private int             m_AD_Client_ID = 0;
	/**/
	private boolean         m_isLocked = false;
	/** Payment Selection		*/
	private MPaySelection	m_ps = null;

	//
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();
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
	private CLabel labelYear = new CLabel();
	private VComboBox fieldYear = new VComboBox();
	private CLabel labelDateSalary01 = new CLabel();

	
	private CLabel labelGroupPayment = new CLabel();
	private VComboBox fieldGroupPayment = new VComboBox();
	private CLabel labelActivity = new CLabel();
	private VComboBox fieldActivity = new VComboBox();
	private CLabel labelOrg = new CLabel();
	private VComboBox fieldOrg = new VComboBox();
	private CLabel labelDept = new CLabel();
	private VComboBox fieldDept = new VComboBox();

	private CLabel labelPeriodFrom = new CLabel();
	private VComboBox fieldPeriodFrom = new VComboBox();
	private CLabel labelPeriodTo = new CLabel();
	private VComboBox fieldPeriodTo = new VComboBox();

	private CLabel labelErrorMsg = new CLabel();

	/** Dispose active                                  */
	private boolean         m_disposing = false;

	final int[] cPos = {0,10,11};

	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
	    this.fillPeriods();

		fillCombobox();
	    this.labelErrorMsg.setText("");
	    this.labelErrorMsg.setVisible(false);
	    this.labelErrorMsg.setForeground(Color.RED);
	    
		CompiereColor.setBackground(this);
		//
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);
		//
		labelEmployee.setText(Msg.translate(Env.getCtx(), "P_Employee_ID"));
//		fieldEmployee.addActionListener(this);

		checkIsActive.setBackground(CompierePLAF.getInfoBackground());
		checkIsActive.setSelected(false);
		checkIsActive.setText(Msg.translate(Env.getCtx(), "IsActive"));

		labelGroupPayment.setText(Msg.translate(Env.getCtx(), "P_Payment_Group_ID"));
		labelActivity.setText(Msg.translate(Env.getCtx(), "C_activity_ID"));
		labelOrg.setText(Msg.translate(Env.getCtx(), "ad_org_id"));
		labelDept.setText(Msg.translate(Env.getCtx(), "P_Department_ID"));

		bRefresh.addActionListener(this);
		this.bExport.addActionListener(this);
		//labelMovementDateFrom.setText(Msg.translate(Env.getCtx(), "MovementDateFrom"));
		labelYear.setText(Msg.translate(Env.getCtx(), "P_Year_ID"));

		labelPeriodFrom.setText(Msg.translate(Env.getCtx(), "SelectionDateFrom"));
		labelPeriodTo.setText(Msg.translate(Env.getCtx(), "SelectionDateTo"));


		labelDateSalary01.setText("");
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
		
		parameterPanel.add(labelEmployee,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldEmployee,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkIsActive,    new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		//parameterPanel.add(labelMovementDateFrom,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//parameterPanel.add(fieldMovementDateFrom,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
//		parameterPanel.add(labelYear        ,  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
//		parameterPanel.add(fieldYear        ,   new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelPeriodFrom,  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldPeriodFrom,   new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelPeriodTo,  new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldPeriodTo,   new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(this.bExport,    new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(bRefresh,    new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelDateSalary01,  new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
//		parameterPanel.add(this.labelErrorMsg,   new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		mainPanel.add(dataStatus, BorderLayout.SOUTH);
		mainPanel.add(dataPane, BorderLayout.CENTER);
		dataPane.getViewport().add(miniTable, null);
		//
		
		mainPanel.setPreferredSize(new Dimension(1024, 750));

		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
		commandPanel.add(bCancel, null);

		fieldYear.addActionListener(this);

	}   //  jbInit

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
		 SELECT c.AD_Client_ID, c.AD_Org_ID, e.P_Employee_ID, e.value , e.Name, c.P_Credits_ID, c.Value, c.Name , dbo.P_Credits_Total_Range( m.P_Credits_ID, m.P_employee_ID, getdate()  ) AS total
		 FROM P_Credits c, P_Employee e, P_Employee_Credits m
		 WHERE c.P_Credits_ID = m.P_Credits_ID
		 AND e.P_Employee_ID = m.P_Employee_ID
		 */

		// TODO /365 pour les banques donc P_UOM_ID = 102

		
		/**  prepare MiniTable
		 *
		 */
		m_sql = miniTable.prepareTable(getColumn(),
			//	FROM
			" CHL_GlobalCompensation_01 ",
			//	WHERE
			" 1=1 ",
         	//	additional where & order in loadTableInfo() 
			true, "CHL_GlobalCompensation_01");
		
		//
		miniTable.getModel().addTableModelListener(this);
		//
		//fieldMovementDateFrom.setMandatory(true);
		//fieldMovementDateFrom.setValue(new Timestamp(System.currentTimeMillis()));
		//
		m_AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
	}   //  dynInit

	private ColumnInfo[] getColumn()
	{
		Properties ctx = Env.getCtx();
			return new ColumnInfo[] 
             {
				//  0..4
				new ColumnInfo(" ", "P_Employee_ID"      , IDColumn.class, false, false, null),
//				new ColumnInfo(Msg.translate(ctx, "AD_Org_ID")      , "Organisation", String.class, true, false, null),
//				new ColumnInfo(Msg.translate(ctx, "C_Activity_ID")  , "Activity"    , String.class, true, false, null),
//				new ColumnInfo(Msg.translate(ctx, "P_Department_ID"), "Department"  , String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx, "P_Employee_ID")  , "value"       , String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx, "Name")      , "Name"   , String.class, true, false, null),
				new ColumnInfo(getDescriptionGain("01"), "sum( Gain_01) AS Gain_01", Double.class),
				new ColumnInfo(getDescriptionGain("02"), "sum( Gain_02) AS Gain_02", Double.class),
				new ColumnInfo(getDescriptionGain("03"), "sum( Gain_03) AS Gain_03", Double.class),
				new ColumnInfo(getDescriptionGain("07"), "sum( Gain_07) AS Gain_07", Double.class),
				new ColumnInfo(getDescriptionGain("08"), "sum( Gain_08) AS Gain_08", Double.class),
				new ColumnInfo(getDescriptionGain("09"), "sum( Gain_09) AS Gain_09", Double.class),
				new ColumnInfo(getDescriptionGain("10"), "sum( Gain_10) AS Gain_10", Double.class),
				new ColumnInfo(getDescriptionGain("11"), "sum( Gain_11) AS Gain_11", Double.class),
				new ColumnInfo(getDescriptionGain("12"), "sum( Gain_12) AS Gain_12", Double.class),
				new ColumnInfo(getDescriptionGain("13"), "sum( Gain_13) AS Gain_13", Double.class),
				new ColumnInfo(getDescriptionGain("14"), "sum( Gain_14) AS Gain_14", Double.class),
				new ColumnInfo(getDescriptionGain("15"), "sum( Gain_15) AS Gain_15", Double.class),
				new ColumnInfo(getDescriptionGain("17"), "sum( Gain_17) AS Gain_17", Double.class),
				new ColumnInfo(getDescriptionGain("20"), "sum( Gain_20) AS Gain_20", Double.class),
				new ColumnInfo(getDescriptionGain("21"), "sum( Gain_21) AS Gain_21", Double.class),
				new ColumnInfo(getDescriptionGain("23"), "sum( Gain_23) AS Gain_23", Double.class),
				new ColumnInfo(getDescriptionGain("25"), "sum( Gain_25) AS Gain_25", Double.class),
				new ColumnInfo(getDescriptionGain("26"), "sum( Gain_26) AS Gain_26", Double.class),
				new ColumnInfo(getDescriptionGain("27"), "sum( Gain_27) AS Gain_27", Double.class),
				new ColumnInfo(getDescriptionGain("29"), "sum( Gain_29) AS Gain_29", Double.class),
				new ColumnInfo(getDescriptionGain("30"), "sum( Gain_30) AS Gain_30", Double.class),
				new ColumnInfo(getDescriptionGain("31"), "sum( Gain_31) AS Gain_31", Double.class),
				new ColumnInfo(getDescriptionGain("32"), "sum( Gain_32) AS Gain_32", Double.class),
				new ColumnInfo(getDescriptionGain("36"), "sum( Gain_36) AS Gain_36", Double.class),
				new ColumnInfo(getDescriptionGain("39"), "sum( Gain_39) AS Gain_39", Double.class),
				new ColumnInfo(getDescriptionGain("40"), "sum( Gain_40) AS Gain_40", Double.class),
				new ColumnInfo(getDescriptionGain("41"), "sum( Gain_41) AS Gain_41", Double.class),
				new ColumnInfo(getDescriptionGain("43"), "sum( Gain_43) AS Gain_43", Double.class),
				new ColumnInfo(getDescriptionGain("44"), "sum( Gain_44) AS Gain_44", Double.class),
				new ColumnInfo(getDescriptionGain("45"), "sum( Gain_45) AS Gain_45", Double.class),
				new ColumnInfo(getDescriptionGain("47"), "sum( Gain_47) AS Gain_47", Double.class),
				new ColumnInfo(getDescriptionGain("49"), "sum( Gain_49) AS Gain_49", Double.class),
				new ColumnInfo(getDescriptionGain("50"), "sum( Gain_50) AS Gain_50", Double.class),
				new ColumnInfo(getDescriptionGain("53"), "sum( Gain_53) AS Gain_53", Double.class),
				new ColumnInfo(getDescriptionGain("54"), "sum( Gain_54) AS Gain_54", Double.class),
				new ColumnInfo(getDescriptionGain("65"), "sum( Gain_65) AS Gain_65", Double.class),
				new ColumnInfo(getDescriptionGain("81"), "sum( Gain_81) AS Gain_81", Double.class),
				new ColumnInfo(getDescriptionGain("82"), "sum( Gain_82) AS Gain_82", Double.class),
				new ColumnInfo(getDescriptionGain("87"), "sum( Gain_87) AS Gain_87", Double.class),
				new ColumnInfo(getDescriptionGain("88"), "sum( Gain_88) AS Gain_88", Double.class),
				new ColumnInfo(getDescriptionGain("92"), "sum( Gain_92) AS Gain_92", Double.class),
				new ColumnInfo(getDescriptionGain("92A"),"sum( Gain_92A ) AS Gain_92A", Double.class),
				new ColumnInfo(getDescriptionGain("95"), "sum( Gain_95 ) AS Gain_95", Double.class),
				new ColumnInfo(getDescriptionDeduction("02"), "sum( Deduction_02 ) AS Deduction_02", Double.class),
				new ColumnInfo(getDescriptionDeduction("40"), "sum( Deduction_40 ) AS Deduction_40", Double.class),
				new ColumnInfo(getDescriptionDeduction("42"), "sum( Deduction_42 ) AS Deduction_42", Double.class),
				new ColumnInfo(getDescriptionDeduction("59"), "sum( Deduction_59 ) AS Deduction_59", Double.class),
				new ColumnInfo(getDescriptionDeduction("64"), "sum( Deduction_64 ) AS Deduction_64", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("37"), "sum( Taxable_Benefit_37 ) AS Taxable_Benefit_37", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("51"), "sum( Taxable_Benefit_51 ) AS Taxable_Benefit_51", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("52"), "sum( Taxable_Benefit_52 ) AS Taxable_Benefit_52", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("57"), "sum( Taxable_Benefit_57 ) AS Taxable_Benefit_57", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("58"), "sum( Taxable_Benefit_58 ) AS Taxable_Benefit_58", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("59"), "sum( Taxable_Benefit_59 ) AS Taxable_Benefit_59", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("61"), "sum( Taxable_Benefit_61 ) AS Taxable_Benefit_61", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("62"), "sum( Taxable_Benefit_62 ) AS Taxable_Benefit_62", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("63"), "sum( Taxable_Benefit_63 ) AS Taxable_Benefit_63", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("67"), "sum( Taxable_Benefit_67 ) AS Taxable_Benefit_67", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("68"), "sum( Taxable_Benefit_68 ) AS Taxable_Benefit_68", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("69"), "sum( Taxable_Benefit_69 ) AS Taxable_Benefit_69", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("70"), "sum( Taxable_Benefit_70 ) AS Taxable_Benefit_70", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("71"), "sum( Taxable_Benefit_71 ) AS Taxable_Benefit_71", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("71a"), "sum( Taxable_Benefit_71a ) AS Taxable_Benefit_71a", Double.class),
				new ColumnInfo(getDescriptionTaxable_Benefit("75"), "sum( Taxable_Benefit_75 ) AS Taxable_Benefit_75", Double.class)
		};
	}
	
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
	        s_log.log(Level.SEVERE, "VGainDetail.fillCombobox()", e);
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

	    String sqlYear = "select P_Year_ID, Year, IsDefault  from P_Year ORDER BY Year Desc ";
		try
	    {
	        int index = 0;
	        PreparedStatement stmt = DB.prepareStatement(sqlYear, null);
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	        	this.fieldYear.addItem(new KeyNamePair(rs.getInt("P_Year_ID"), rs.getString("Year")));
	            if(rs.getString("IsDefault").toUpperCase().equals("Y"))
	            {
	                this.fieldYear.setSelectedIndex(index);
	            }
	            index++;
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
	 * On remplit les combobox de périodes en leur affectant la période ouverte comme valeur par défaut
	 */
	private void fillPeriods()
	{
        P_Period period = P_Period.getOpenPeriod(Env.getCtx(), null);
        
		String sql = "select P_Period_ID, Name, PeriodStatus from P_Period WHERE IsActive = 'Y' " +
					 " AND P_Frequency_ID = "  + period.getP_Frequency_ID() + 
            		 "ORDER BY Name DESC";
	    try
	    {
	        int index = 0;
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	            this.fieldPeriodFrom.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name")));
	            this.fieldPeriodTo.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name")));
	            if(rs.getString("PeriodStatus").toUpperCase().equals("O"))
	            {
	                this.fieldPeriodFrom.setSelectedIndex(index);
	                this.fieldPeriodTo.setSelectedIndex(index);
	            }
	            index++;
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "VDeductionTotal.fillPeriods()", e);
	    }
	}

	
	/**
	 *  Query and create TableInfo
	 */
	private void loadTableInfo()
	{
		s_log.log(Level.INFO, "RecognitionAllowance.loadTableInfo");

		
		//  Get Open Invoices
		try
		{
			PreparedStatement pstmt = this.prepareStatement();
			ResultSet rs = pstmt.executeQuery();
			miniTable.loadTable(rs);
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, "RecognitionAllowance.loadTableInfo:" , e);
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
//		if (onlyDue.isSelected())
//			sql += " AND i.DateInvoiced+p.NetDays <= ?";
		//

/*		int P_Year_ID = 0;
		
		Object year = fieldYear.getValue();
		if (year != null && year.toString().length() > 0)
		{
			P_Year_ID = Integer.parseInt( year.toString() );
		}
		
		if (P_Year_ID != 0)
		{
			sql += " AND P_Year_ID = ? ";
		}
	*/		

		int P_Employee_ID = 0;
		int P_Credits_ID = 0;

		Object value = fieldEmployee.getValue();
		if (value != null && value.toString().length() > 0)
		{
			P_Employee_ID = Integer.parseInt( value.toString() );
		}

		if (P_Employee_ID != 0)
			sql += " AND P_Employee_ID = ?";

		if(checkIsActive.isSelected() == true)
			sql += " AND IsActive = 'Y'";
	
		int Org_id = Integer.parseInt(fieldOrg.getValue().toString());
		if (Org_id != 0)
		{
			sql += " and AD_Org_id = ? ";
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


		Integer PeriodFrom = new Integer(((KeyNamePair)fieldPeriodFrom.getSelectedItem()).getKey());
		Integer PeriodTo = new Integer(((KeyNamePair)fieldPeriodTo.getSelectedItem()).getKey());

		
		Object periodFrom = fieldPeriodFrom.getValue();
		if (periodFrom != null && periodFrom.toString().length() > 0)
		{
			PeriodFrom = Integer.parseInt( periodFrom.toString() );
		}

		Object periodTo = fieldPeriodTo.getValue();
		if (periodTo != null && periodTo.toString().length() > 0)
		{
			PeriodTo = Integer.parseInt( periodTo.toString() );
		}

		if ( PeriodFrom != 0 && PeriodTo != 0)
			sql += " AND P_Period_ID Between ? AND ? " ;
		else if ( PeriodFrom != 0 )
			sql += " AND P_Period_ID >= ? " ;
		else if ( PeriodTo != 0 )
			sql += " AND P_Period_ID <= ? " ;
		
		
//		sql += " Group By AD_Client_ID,  AD_Org_ID,  P_Year_ID, P_Employee_ID, IsActive, P_Department_ID, C_Activity_ID, P_Payment_Group_ID, Year, Organisation, Value, Name, Department, Activity ";
		sql += " Group By AD_Client_ID,   P_Year_ID, P_Employee_ID, IsActive,  P_Payment_Group_ID, Year,  Value, Name ";

		sql += " ORDER BY 2,5";

		
		int index = 1;
		PreparedStatement pstmt = DB.prepareStatement(sql, null);

/*
		if (P_Year_ID != 0)
		{
			pstmt.setInt(index++, P_Year_ID);
		}
*/
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

		if (PeriodFrom != 0)
		{
			pstmt.setInt(index++, PeriodFrom);
		}

		if (PeriodTo != 0)
		{
			pstmt.setInt(index++, PeriodTo);
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
		if ( fieldYear != null && fieldYear.getDisplay() != null )
		{
			int Year = Integer.parseInt(fieldYear.getDisplay().toString());	
			labelDateSalary01.setText("                              " );
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
	    else
	    {
			int P_Employee_ID = 0;
			int P_Credits_ID = 0;
	
			Object value = fieldEmployee.getValue();
			if (value != null && value.toString().length() > 0)
			{
				P_Employee_ID = Integer.parseInt( value.toString() );
			}
	
		    this.labelErrorMsg.setVisible(true);
			    
			if (e.getSource() == bCancel)
				dispose();
	
			//  Update Open Invoices
			else if ( e.getSource() == bRefresh)
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


//				      -------------PGI Solstice 07/04/2008-----------------
		            // generation des entêtes de colonnes
			        fileWriter.write("<Row>\n");
			       ColumnInfo[] colInfo = this.getColumn();
			        for(int i = 1; i <= rsMetaData.getColumnCount() ; i++)
			        {
			        	String colName = colInfo[i-1].getColHeader();
			        	if(!colName.endsWith("_ID") && !colName.equals(" "))
			        		fileWriter.write("<Cell><Data ss:Type=\"String\">" + colName + "</Data></Cell>\n");
			        }
			        
			        fileWriter.write("</Row>\n");
			       // -----------------------------------------------------
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
			                	{
			                		if (rs.getString(i) != null)
			                		{
			                			fileWriter.write("<Cell><Data ss:Type=\"String\">" + rs.getString(i) + "</Data></Cell>\n");
			                		}
			                		else
			                		{
			                			fileWriter.write("<Cell><Data ss:Type=\"String\">" + "" + "</Data></Cell>\n");
			                		}
			                	}	
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.BigDecimal") )
			                	{
			                		if (rs.getString(i) != null)
			                		{
			                			fileWriter.write("<Cell><Data ss:Type=\"Number\">" + rs.getString(i) + "</Data></Cell>\n");
			                		}
			                		else
			                		{
			                			fileWriter.write("<Cell><Data ss:Type=\"Number\">" + "0.00" + "</Data></Cell>\n");
			                		}
			                	}
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.Double") )
			                	{
			                		if (rs.getString(i) != null)
			                		{
			                			fileWriter.write("<Cell><Data ss:Type=\"Number\">" + rs.getString(i) + "</Data></Cell>\n");
			                		}
			                		else
			                		{
				                		fileWriter.write("<Cell><Data ss:Type=\"Number\">" + "0.00" + "</Data></Cell>\n");
			                		}
			                	}
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
		int  nbrYear;
		int  nbrDay;
		String unit = "";
		int rows = miniTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, cPos[0]);
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
	//	this.setEnabled(true);
	//	m_isLocked = false;
		//  Ask to Print it
		if (!ADialog.ask(m_WindowNo, this, "RecognitionAllowancePrint?", "(" + pi.getSummary() + ")"))
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
		s_log.log(Level.INFO, "RecognitionAllowance.executeASync");
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


}   //  RecognitionAllowance
