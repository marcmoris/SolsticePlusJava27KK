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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
import solstice.utils.PgiUtil;

public class RemiseReerCollectifCSN extends CPanel 
implements FormPanel, ActionListener, TableModelListener, ASyncProcess, ListSelectionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 *  RemiseReerCollectifCSN Constructor
	 */
	public RemiseReerCollectifCSN()
	{
	}   //  RemiseReerCollectifCSN

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		s_log.log(Level.INFO, "RemiseReerCollectifCSN.init");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
//			MLookup empL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 2111113, DisplayType.Search);
//			fieldEmployee = new VLookup ("P_Employee_ID", false, false, true, empL);
//			fieldEmployee.setBounds(206, 20, 210, 19);

			
			jbInit();
			dynInit();
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

			miniTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			miniTable.setColumnSelectionAllowed(false);
			miniTable.setRowSelectionAllowed(true);

			this.miniTable.getTableHeader().addMouseListener(new ColumnChecker(this.miniTable));
			
//			miniTable.getModel().addTableModelListener(this);
			
			miniTable.getSelectionModel().addListSelectionListener(this);



		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE, "RemiseReerCollectifCSN.init" + e);
		}
	}	//	init

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (RemiseReerCollectifCSN.class);
	/** Format                  */
	public static DecimalFormat   m_format = DisplayType.getNumberFormat(DisplayType.Amount);
	
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
//	private CLabel labelEmployee = new CLabel();
//	private VLookup fieldEmployee;
	

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
//	private VComboBox fieldActivity = new VComboBox();
//	private CLabel labelOrg = new CLabel();
//	private VComboBox fieldOrg = new VComboBox();
//	private VComboBox fieldDept = new VComboBox();
//	private CLabel labelSelectionYear = new CLabel();
//	private VComboBox fieldSelectionYear = new VComboBox();
	
	private CLabel labelPeriodFrom = new CLabel();
	private VComboBox fieldPeriodFrom = new VComboBox();
	private CLabel labelPeriodTo = new CLabel();
	private VComboBox fieldPeriodTo = new VComboBox();
	
	
	private CLabel labelErrorMsg = new CLabel();

	/** Dispose active                                  */
	private boolean         m_disposing = false;
	
	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
//	    this.fillYear();
		fillCombobox();
	    
		CompiereColor.setBackground(this);
		//
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);
		//
//		labelEmployee.setText(Msg.translate(Env.getCtx(), "P_Employee_ID"));
		
		labelGroupPayment.setText(Msg.translate(Env.getCtx(), "P_Payment_Group_ID"));
		//labelActivity.setText(Msg.translate(Env.getCtx(), "C_activity_ID"));
//		labelOrg.setText(Msg.translate(Env.getCtx(), "ad_org_id"));
		//labelDept.setText(Msg.translate(Env.getCtx(), "P_Department_ID"));

//		labelSelectionYear.setText(Msg.translate(Env.getCtx(), "P_Year_ID"));
		
		labelPeriodFrom.setText(Msg.translate(Env.getCtx(), "SelectionDateFrom"));
		labelPeriodTo.setText(Msg.translate(Env.getCtx(), "SelectionDateTo"));
		

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
		
		
		parameterPanel.add(labelPeriodFrom,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldPeriodFrom,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelPeriodTo,  new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldPeriodTo,   new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		
//		parameterPanel.add(labelOrg,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
//		parameterPanel.add(fieldOrg,    new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


//		parameterPanel.add(labelSelectionYear,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
//		parameterPanel.add(fieldSelectionYear,  new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

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

	/**
	 *  Dynamic Init.
	 *  - Load Bank Info
	 *  - Load Employee
	 *  - Init Table
	 */
	private void dynInit()
	{
		Properties ctx = Env.getCtx();
//Msg.translate(Env.getCtx(), "P_Payment_Group_ID")
		

		
		m_sql = miniTable.prepareTable(new ColumnInfo[] {
			//  0..4
			new ColumnInfo(" ", "P_Employee_ID", IDColumn.class, false, false, null),
			new ColumnInfo(Msg.translate(ctx, "NO_GROUPE"), "NO_GROUPE", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "VALUE"), "VALUE", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "SurName"), "SurName", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "FirstName"), "FirstName", String.class, true, false, null),
			new ColumnInfo("PAT Reg", "PAT_Reg", BigDecimal.class, true, false, null),
			new ColumnInfo("VOL Reg", "VOL_Reg", BigDecimal.class, true, false, null),
		},
			//	FROM
			"  SP_RemiseReerCollectifCSN( 1000004 , ? , ?, ?)",
			//	WHERE
			" 1 = 1 ORDER BY SurName, FirstName "  ,
         	//	additional where & order in loadTableInfo() 
			true, "SP_RemiseReerCollectifCSN");
		
		//
		miniTable.getModel().addTableModelListener(this);    	

	}   //  dynInit

	
	public ColumnInfo[] getColumn() {
		Properties ctx = Env.getCtx();
        	return new ColumnInfo[] {
        			new ColumnInfo(" ", "P_Employee_ID", IDColumn.class, false, false, null),
        			new ColumnInfo(Msg.translate(ctx, "NO_GROUPE"), "NO_GROUPE", String.class, true, false, null),
        			new ColumnInfo(Msg.translate(ctx, "VALUE"), "VALUE", String.class, true, false, null),
        			new ColumnInfo(Msg.translate(ctx, "SurName"), "SurName", String.class, true, false, null),
        			new ColumnInfo(Msg.translate(ctx, "FirstName"), "FirstName", String.class, true, false, null),
        			new ColumnInfo(Msg.translate(ctx, "Employee_Part"), "PAT_Reg", BigDecimal.class, true, false, null),
        			new ColumnInfo(Msg.translate(ctx, "Employer_Part"), "VOL_Reg", BigDecimal.class, true, false, null)
        	};
	}

	
	/**
	 * On remplit les combobox d'années en leur affectant l'année de la période ouverte comme valeur par défaut
	 */
/*	
	private void fillYear()
	{
        P_Period period = P_Period.getOpenPeriod(Env.getCtx(), null);
        int defaultValue = Integer.parseInt(period.get_Value("P_Year_ID").toString());
        
		String sql = "SELECT P_Year_ID, Year from P_Year order by year Desc ";
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	            this.fieldSelectionYear.addItem(new KeyNamePair(rs.getInt("P_Year_ID"), rs.getString("Year")));
	        }
	        this.fieldSelectionYear.setSelectedItem(defaultValue);//setSelectedIndex(index);
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
	        s_log.log(Level.SEVERE, "RemiseReerCollectifCSN.fillYear()", e);
	    }
	}
*/	
	
	private void fillCombobox()
	{
	    String sqlGroupPayment = "select p_payment_group_ID,name from p_payment_group where IsActive = 'Y'";
	    sqlGroupPayment = sqlGroupPayment + " And p_payment_group." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
		+ " ORDER BY Name Desc";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlGroupPayment, null);
	        ResultSet rs = stmt.executeQuery();
//	        this.fieldGroupPayment.addItem(new KeyNamePair(0,""));
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
		calculateSelection( -1, -1);

//    	TableColumnModel tcm = miniTable.getColumnModel();
//    	tcm.getColumn(7).setPreferredWidth(15);

	}   //  loadTableInfo
	
	private PreparedStatement prepareStatement() throws Exception
	{
		//  not yet initialized
		if (m_sql == null)
			return null;

		String sql = m_sql;
		//  Parameters
		
//		sql += " And P_Year_ID = " + new Integer(((KeyNamePair)fieldSelectionYear.getSelectedItem()).getKey());
	
//		int P_Employee_ID = 0;

/*		Object value = fieldEmployee.getValue();
		if (value != null && value.toString().length() > 0)
		{
			P_Employee_ID = Integer.parseInt( value.toString() );
		}		
		
		if (P_Employee_ID != 0)
			sql += " AND P_Employee_ID = " + P_Employee_ID;


		int Org_id = Integer.parseInt(fieldOrg.getValue().toString());
		if (Org_id != 0)
		{
			sql += " and ad_Org_id =  " + Org_id;
		}
	*/	
/*		
		int Payment_Group_id = Integer.parseInt(fieldGroupPayment.getValue().toString());	
		if (Payment_Group_id != 0)
		{
			sql += " and P_Payment_Group_id = " + Payment_Group_id;
		}
*/
		
		int Payment_Group_id = Integer.parseInt(fieldGroupPayment.getValue().toString());	
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


		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		pstmt.setInt(1, Payment_Group_id);
		pstmt.setInt(2, PeriodFrom);
		pstmt.setInt(3, PeriodTo);
		
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
	    		
		    setBusy( true );
			loadTableInfo();
			setBusy( false );
	    }
	}   //  actionPerformed

	
	/**
	 * Cette méthode sert à exporter le contenue du grid dans un fichier XML Excel
	 */

	
	/**
	 *  Table Model Listener
	 *  @param e event
	 */
	public void tableChanged(TableModelEvent e)
	{
		if (m_disposing || isUILocked())
			return;

		if (e.getColumn() == 0)
			calculateSelection( -1, -1);
	}   //  valueChanged

	/**
	 *  Calculate selected rows.
	 *  - add up selected rows
	 */
	public void calculateSelection(int firstRow, int lastRow)
	{
		
		ColumnInfo[] colInfo = this.getColumn();

		m_noSelected = 0;
		BigDecimal Amt1 = new BigDecimal(0.0);
		BigDecimal Amt2 = new BigDecimal(0.0);

		int rows = miniTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);
			if (id.isSelected())
			{
				m_noSelected++;
				for (int j = 0; j <= colInfo.length - 1; j++) {
					String colName = colInfo[j].getColSQL();
					if (colInfo[j].getColClass().equals(BigDecimal.class)) {
						BigDecimal amt = Env.ZERO;
						if (colName.equals("PAT_Reg")) {
							amt = (BigDecimal) miniTable.getModel().getValueAt( i, j);
							Amt1 = Amt1.add(amt);
						}
						if (colName.equals("VOL_Reg")) {
							amt = (BigDecimal) miniTable.getModel().getValueAt(	i, j);
							Amt2 = Amt2.add(amt);
						}
					}
				}

			}
		}

		//  Information
		StringBuffer info = new StringBuffer();
//		info.append(m_noSelected).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected")).append(" - ");
		
		info.append(m_noSelected).append(" ")
		.append(Msg.getMsg(Env.getCtx(), "Selected")).append(" - ");
		info.append( "Montant 1").append(" : ");
		info.append(m_format.format(Amt1)).append(", ");
		info.append( "Montant 2").append(" : ");
		info.append(m_format.format(Amt2)); // .append(", ");
//		statusBar.setInfo(info.toString());
//		statusBar.setAutoscrolls(true);
		
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
	//		        ResultSetMetaData rsMetaData = rs.getMetaData();

		            String buffer = "T;NO_GROUPE  ;DT_DEBUT_PERIODE;DT_FIN_PERIODE;TOTAL_COTISATION;MNT_CT_NON_ALLOUE;MNT_CT_DROITS_NON_ACQUIS;MNT_DT_NON_ALLOUE;MNT_FRAIS_ADMIN;MNT_FRAIS_ACHAT;MNT_DEPOT           ";
		            fileWriter.write(buffer + "\n");
		            
		            
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

		            P_Period lPeriodFrom = P_Period.get( Env.getCtx(), PeriodFrom, null);
		            P_Period lPeriodTo = P_Period.get( Env.getCtx(), PeriodTo, null);
		            
		            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		            String reportDate1 = df.format(lPeriodFrom.getStartDate());
		            String reportDate2 = df.format(lPeriodTo.getEndDate());
		            
		            BigDecimal amt = Env.ZERO;
			        while(rs.next())
			        {
			        	amt = amt.add( rs.getBigDecimal( "PAT_Reg" )).add( rs.getBigDecimal( "VOL_Reg" ) );
			        }
			        rs.close();
			        stmt.close();

			        stmt = this.prepareStatement();
			        rs = stmt.executeQuery();
		            String mnt = amt.toString();
		            
		            String buffer2 = "T;G900344    ;" + reportDate1 + ";" + reportDate2 + ";"
		            		+ mnt + ";;;;;;"
		            		+ mnt
		            		
		            		; 
		            fileWriter.write(buffer2 + "\n");
		            
		            
		            String buffer3 = "D;NO_EMPLOYE ;NOM ;PRENOM ;REER:PAT Reg ;REER:VOL Reg ; ; ;;;";      
		            fileWriter.write(buffer3 + "\n");

			        while(rs.next())
			        {
		            	buffer = "D;" + rs.getString("Value") + ";" + 
		            			rs.getString("SURNAME") + ";" +
		            			rs.getString("FIRSTNAME") + ";" +
		            			rs.getString("PAT_Reg") + ";" +
		            			rs.getString("VOL_Reg") + ";" + ";;;;;"
		            			;
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

	/**************************************************************************
	 *  List Selection Listener (VTable) - row changed
	 *  @param e event
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		//	nothing or initiated by mouse (wait for "real" one)
		if (e.getValueIsAdjusting())
		{
			System.out.println("The mouse button has not yet been released");
			return;
		}
		//  no rows
		int first = -1;
		int last = -1;
		
	    if (e.getSource() == miniTable.getSelectionModel() && miniTable.getRowSelectionAllowed()) 
	    {
	    		first = e.getFirstIndex();
	    		last = e.getLastIndex();
		} 
	    else if (e.getSource() == miniTable.getColumnModel().getSelectionModel()
		        && miniTable.getColumnSelectionAllowed()) 
		{
		    	first = e.getFirstIndex();
		    	last = e.getLastIndex();
		}

		System.out.println("first " + first + " last " + last);

	    calculateSelection( first, last );
		
	}   //  valueChanged


	
}
