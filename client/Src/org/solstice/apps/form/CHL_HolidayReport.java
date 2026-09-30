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

import solstice.process.PgiUtil;

/**
 *  Consultation des soldes de banques
 *
 *  @author Marc Morissette
 *  @version $Id: CHL_HolidayReport.java,v 1.2 2008/05/28 11:23:53 marmor01 Exp $
 */
public class CHL_HolidayReport extends CPanel
	implements FormPanel, ActionListener, TableModelListener, ASyncProcess
{
//	private DialogButton bExport = new DialogButton ("export", Msg.translate(Env.getLanguage(Env.getCtx()), "ExportExcel"), Env.getImageIcon("Export24.gif"), 0);
	private JButton bExport = ConfirmPanel.createExportButton(true);

	/**
	 *  HolidayReport Constructor
	 */
	public CHL_HolidayReport()
	{
	}   //  HolidayReport

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		s_log.log(Level.INFO, "HolidayReport.init");
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
			s_log.log(Level.SEVERE, "HolidayReport.init" + e);
		}
	}	//	init

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (CHL_HolidayReport.class);
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
	private CLabel labelCredits = new CLabel();
	private VComboBox fieldCredits = new VComboBox();
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

	private CLabel labelBooklet = new CLabel();
	private VComboBox fieldBooklet = new VComboBox();

	//private CLabel labelMovementDateFrom = new CLabel();
	//private VDate fieldMovementDateFrom = new VDate();
	
	private CLabel labelGroupPayment = new CLabel();
	private VComboBox fieldGroupPayment = new VComboBox();
	private CLabel labelActivity = new CLabel();
	private VComboBox fieldActivity = new VComboBox();
	private CLabel labelOrg = new CLabel();
	private VComboBox fieldOrg = new VComboBox();
	private CLabel labelDept = new CLabel();
	private VComboBox fieldDept = new VComboBox();
	
	private CLabel labelErrorMsg = new CLabel();

	/** Dispose active                                  */
	private boolean         m_disposing = false;

	final int[] cPosSIQ = {0,8,9};
	final int[] cPosHelico = {0,10,11};

	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		fillCredits();
		fillCombobox();
	    this.labelErrorMsg.setText(Msg.translate(Env.getCtx(), "CreditsTotalErr"));
	    this.labelErrorMsg.setVisible(false);
	    this.labelErrorMsg.setForeground(Color.RED);
	    
		CompiereColor.setBackground(this);
		//
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);
		//
		labelCredits.setText(Msg.translate(Env.getCtx(), "P_Credits_ID"));
		fieldCredits.addActionListener(this);
		labelEmployee.setText(Msg.translate(Env.getCtx(), "P_Employee_ID"));
//		fieldEmployee.addActionListener(this);

		checkIsActive.setBackground(CompierePLAF.getInfoBackground());
		checkIsActive.setSelected(true);
		checkIsActive.setText(Msg.translate(Env.getCtx(), "IsActive"));

		labelGroupPayment.setText(Msg.translate(Env.getCtx(), "P_Payment_Group_ID"));
		labelActivity.setText(Msg.translate(Env.getCtx(), "C_activity_ID"));
		labelOrg.setText(Msg.translate(Env.getCtx(), "ad_org_id"));
		labelDept.setText(Msg.translate(Env.getCtx(), "P_Department_ID"));

		labelBooklet.setText(Msg.translate(Env.getCtx(), "P_Distribution_Booklet_ID"));

		bRefresh.addActionListener(this);
		this.bExport.addActionListener(this);
		//labelMovementDateFrom.setText(Msg.translate(Env.getCtx(), "MovementDateFrom"));
		labelYear.setText(Msg.translate(Env.getCtx(), "P_Year_ID"));


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
		
		parameterPanel.add(labelCredits,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldCredits,  new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelBooklet,  new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldBooklet,  new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		
		parameterPanel.add(labelEmployee,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldEmployee,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(checkIsActive,    new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		//parameterPanel.add(labelMovementDateFrom,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//parameterPanel.add(fieldMovementDateFrom,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelYear        ,  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldYear        ,   new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
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
		fieldBooklet.addActionListener(this);

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

		if (fieldCredits.getItemCount() == 0)
			ADialog.error(m_WindowNo, this, "HolidayReportNoBank");
		else
			fieldCredits.setSelectedIndex(0);

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
			" CHL_HolidayReport_04 ",
			//	WHERE
			" Exists ( select 1 from P_Employee_Credits where P_Employee_Credits.P_Employee_ID = CHL_HolidayReport_04.P_Employee_ID and P_Employee_Credits.P_Credits_ID = ? and P_Employee_Credits.isActive = 'Y' and P_Employee_Credits.EffectTO is null)  ",
         	//	additional where & order in loadTableInfo() 
			true, "CHL_HolidayReport_04");
		
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
				new ColumnInfo(Msg.translate(ctx, "AD_Org_ID")      , "Organisation", String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx, "C_Activity_ID")  , "Activity"    , String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx, "P_Department_ID"), "Department"  , String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx, "P_Employee_ID")  , "value"       , String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx, "Sin")            , "Sin"         , String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx, "DateHired")      , "DateHired"   , Timestamp.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx, "FirstName")      , "FirstName"   , String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx, "Surname")        , "Surname"     , String.class, true, false, null),
				new ColumnInfo("Seniority"      , "Seniority"   , Double.class),
				new ColumnInfo("Vac %"          , "pctvac"      , Double.class),
				new ColumnInfo("*"          , "case when isVacationFixeRate = 'Y' then '*' else '' end"      , String.class),
				new ColumnInfo("Vac Jours"      , "NbrDayVac"   , Double.class),
				new ColumnInfo("Total Fix"      , "totalFixe"   , Double.class),
				new ColumnInfo("Daily rate"     , "DailyRate"   , Double.class),
				new ColumnInfo("Paid vacation " , "round(DailyRate * NbrDayVac ,2) AS PaidVacation "   , Double.class),
				new ColumnInfo("Salary (01)", "Salary01"   , Double.class),
				new ColumnInfo("Salary (02)", "Salary02"   , Double.class),
				new ColumnInfo("Vac earnings"   , "Salary01 + Salary02"   , Double.class),
				new ColumnInfo("$ topped at 6%" , "topped"   , Double.class),
				new ColumnInfo("$ Earned no max", "EarnedMax "   , Double.class),
				new ColumnInfo("Vac adjustment" , "Case when VacAdjustment < 0 then 0 else VacAdjustment end "   , Double.class),
				new ColumnInfo("Vac Adj no max ", "Case when VacAdjustmentMax < 0 then 0 else VacAdjustmentMax end "   , Double.class)
		};
	}
	
	private void fillCredits()
	{
		//  Bank Account Info
		String sql = "SELECT P_Credits_ID,"                       //  1
			+ "Value + ' ' + Name AS Name "             //  2
			+ " FROM P_Credits Where Value in ( '3200', '3300' ) "
			+ " ORDER BY 2";
			;
		
//	    sql = sql + " And P_Credits." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 

		System.out.println( "HolidayReport Sql" + sql.toString());
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			CreditsInfo bi = new CreditsInfo (0, "");
//			fieldCredits.addItem(bi);

			int i = 0;
			while (rs.next())
			{
				boolean transfers = false;
				bi = new CreditsInfo (rs.getInt(1), rs.getString(2));
				fieldCredits.addItem(bi);
//				if ( rs.getString("Value").equals("3200"))
//					fieldCredits.setSelectedIndex(i);
				i++;
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, e.toString());
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

	    String sqlYear = "select P_Year_ID, Year  from P_Year ORDER BY Year Desc ";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlYear, null);
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	        	this.fieldYear.addItem(new KeyNamePair(rs.getInt("P_Year_ID"), rs.getString("Year")));
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
	        s_log.log(Level.SEVERE, "fillCombobox()", e);
	    }	

	    
	    String sqlBooklet = "select P_Distribution_Booklet_ID, Value + ' ' + Name as name   from P_Distribution_Booklet ORDER BY Value";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlBooklet, null);
	        ResultSet rs = stmt.executeQuery();
	        this.fieldBooklet.addItem(new KeyNamePair(0,""));
	        while(rs.next())
	        {
	        	this.fieldBooklet.addItem(new KeyNamePair(rs.getInt("P_Distribution_Booklet_ID"), rs.getString("Name")));
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
	 *  Load Bank Info - Load Info from Bank Account and valid Documents (PaymentRule)
	 */
	private void loadCreditsInfo()
	{
		CreditsInfo bi = (CreditsInfo)fieldCredits.getSelectedItem();
		if (bi == null)
			return;
	}   //  loadCreditsInfo

	/**
	 *  Query and create TableInfo
	 */
	private void loadTableInfo()
	{
		s_log.log(Level.INFO, "HolidayReport.loadTableInfo");

		
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
			s_log.log (Level.SEVERE, "HolidayReport.loadTableInfo:" , e);
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

		sql += " AND P_Year_ID = " + Integer.parseInt(fieldYear.getValue().toString());	

		int P_Employee_ID = 0;
		int P_Credits_ID = 0;

		Object value = fieldEmployee.getValue();
		if (value != null && value.toString().length() > 0)
		{
			P_Employee_ID = Integer.parseInt( value.toString() );
		}

		CreditsInfo bi = (CreditsInfo)fieldCredits.getSelectedItem();
		P_Credits_ID = bi.P_Credits_ID;
		
//		if (P_Credits_ID != 0)
//			sql += " AND P_Credits.P_Credits_ID = ?";
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

		
		int Distribution_Booklet_ID = Integer.parseInt(fieldBooklet.getValue().toString());	
		if (Distribution_Booklet_ID != 0)
		{
			sql += " and P_Distribution_Booklet_ID = ? ";
		}

		int Payment_Group_id = Integer.parseInt(fieldGroupPayment.getValue().toString());	
		if (Payment_Group_id != 0)
		{
			sql += " and P_Payment_Group_id = ? ";
		}
		
		sql += " ORDER BY 2,5";

		int index = 1;
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
//		pstmt.setInt(index++, m_AD_Client_ID);
//		if (onlyDue.isSelected())

		//pstmt.setTimestamp(index++, MovementDateFrom);
//		pstmt.setTimestamp(index++, MovementDateTo);
		
//		if (P_Credits_ID != 0)
		pstmt.setInt(index++, P_Credits_ID);
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

		if (Distribution_Booklet_ID != 0)
		{
			pstmt.setInt(index++, Distribution_Booklet_ID);
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
		if ( fieldYear != null && fieldYear.getDisplay() != null )
		{
			int Year = Integer.parseInt(fieldYear.getDisplay().toString());	
			labelDateSalary01.setText("Variable au (01)" + TimeUtil.getDay(Year -1, 05, 01).toString().substring(0, 10) 
				 + " au " + TimeUtil.getDay(Year -1, 12, 31).toString().substring(0, 10)
				 + " (02) " + TimeUtil.getDay(Year, 01, 01).toString().substring(0, 10)
				 + " au " + TimeUtil.getDay(Year   , 04, 30).toString().substring(0, 10));
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
	
			CreditsInfo bi = (CreditsInfo)fieldCredits.getSelectedItem();
			P_Credits_ID = bi.P_Credits_ID;
	
			
			// On doit vérifier si l'utilisateur a saisi au moins un paramètre entre la
			// banque et l'employé
			if (P_Credits_ID != 0 || P_Employee_ID != 0)
			{
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
			else
			{
			    this.labelErrorMsg.setVisible(true);
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
			                		if (rs.getString(i) != null)
			                		{
			                			fileWriter.write("<Cell><Data ss:Type=\"String\">" + rs.getString(i) + "</Data></Cell>\n");
			                		}
			                		else
			                		{
			                			fileWriter.write("<Cell><Data ss:Type=\"String\">" + "" + "</Data></Cell>\n");
			                		}
			                		
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.BigDecimal") )
			                		fileWriter.write("<Cell><Data ss:Type=\"Number\">" + rs.getString(i) + "</Data></Cell>\n");
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.Double") )
			                		fileWriter.write("<Cell><Data ss:Type=\"Number\">" + rs.getString(i) + "</Data></Cell>\n");
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.sql.Timestamp") )
			                		fileWriter.write("<Cell><Data ss:Type=\"String\">" + rs.getTimestamp(i).toString().substring(0,10) + "</Data></Cell>\n");
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
		BigDecimal creditsAmt = new BigDecimal(0.0);
		int  nbrYear;
		int  nbrDay;
		String unit = "";
		int rows = miniTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, lPos[0]);
			if (id.isSelected())
			{
				
				Double dAmt = ((Double)miniTable.getModel().getValueAt(i,lPos[1]));
				BigDecimal amt = new BigDecimal( dAmt.toString() );
				if ( unit == null )
					unit = (String)miniTable.getModel().getValueAt(i, lPos[3]);

				if ( unit.equals("A/J") )
				{
					nbrYear = amt.intValue();
				    nbrDay =  (amt.subtract( new BigDecimal( nbrYear) )).multiply(new BigDecimal(1000)).intValue() + (nbrYear * 365);
					creditsAmt = creditsAmt.add(  new BigDecimal( nbrDay ));
				}
				else
					creditsAmt = creditsAmt.add(amt);
					
				m_noSelected++;
			}
		}

		if ( unit != null && unit.equals("A/J") )
		{
			String sql = "Select dbo.FN_Credits_Sld_Display( " + creditsAmt.toString() + ",'" + unit.trim() +"' ) amt"; 
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next())
				{
					creditsAmt = rs.getBigDecimal("amt");
				}
				rs.close();
				pstmt.close();
			}
			catch (Exception e)
			{
				s_log.log(Level.SEVERE, e.toString());
			}
		}
		
		//  Information
		StringBuffer info = new StringBuffer();
		info.append(m_noSelected).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected")).append(" - ");
		info.append(m_format.format(creditsAmt));  
		info.append( " " + unit );  //.append(", ");
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
		if (!ADialog.ask(m_WindowNo, this, "HolidayReportPrint?", "(" + pi.getSummary() + ")"))
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
		s_log.log(Level.INFO, "HolidayReport.executeASync");
	}   //  executeASync

	/*************************************************************************/

	/**
	 *  Bank Account Info
	 */
	public class CreditsInfo
	{
		public CreditsInfo (int newP_Credits_ID, String newName)
		{
			P_Credits_ID = newP_Credits_ID;
			Name = newName;
		}
		int P_Credits_ID;
		String Name;
		public String toString()
		{
			return Name;
		}
	}   //  CreditsInfo

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

}   //  HolidayReport
