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
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFileChooser;
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

public class CHL_Insurance extends CPanel 
implements FormPanel, ActionListener, TableModelListener, ASyncProcess
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 *  VCreditsTotal Constructor
	 */
	public CHL_Insurance()
	{
	}   //  CHL_Insurrance

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		s_log.log(Level.INFO, "CHL_Insurance.init");
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
			s_log.log(Level.SEVERE, "CHL_Insurance.init" + e);
		}
	}	//	init

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (CHL_Insurance.class);
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
	private VComboBox fieldActivity = new VComboBox();
	private CLabel labelOrg = new CLabel();
	private VComboBox fieldOrg = new VComboBox();
	private VComboBox fieldDept = new VComboBox();
	private CLabel labelSelectionYear = new CLabel();
	private VComboBox fieldSelectionYear = new VComboBox();
	
	private CLabel labelErrorMsg = new CLabel();

	/** Dispose active                                  */
	private boolean         m_disposing = false;
	
	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
	    this.fillYear();
		fillCombobox();
	    
		CompiereColor.setBackground(this);
		//
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);
		//
		labelEmployee.setText(Msg.translate(Env.getCtx(), "P_Employee_ID"));
		
		labelGroupPayment.setText(Msg.translate(Env.getCtx(), "P_Payment_Group_ID"));
		//labelActivity.setText(Msg.translate(Env.getCtx(), "C_activity_ID"));
		labelOrg.setText(Msg.translate(Env.getCtx(), "ad_org_id"));
		//labelDept.setText(Msg.translate(Env.getCtx(), "P_Department_ID"));

		labelSelectionYear.setText(Msg.translate(Env.getCtx(), "P_Year_ID"));
		

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


		parameterPanel.add(labelSelectionYear,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldSelectionYear,  new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelEmployee,   new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldEmployee,    new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
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
			new ColumnInfo(Msg.translate(ctx, "AD_Org_ID"), "Ad_Org_Name", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "p_LongTermLeave_id"), "LongTermLeave_Name", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Surname"), "Surname", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Firstname"), "Firstname", String.class, true, false, null),
//			new ColumnInfo("SIN", "SIN", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Insurancenumber"), "Insurancenumber", Integer.class),
			new ColumnInfo(Msg.translate(ctx, "DateHired"), "DateHired", Date.class),
			new ColumnInfo(Msg.translate(ctx, "DateLayoff"), "DateLayoff", Date.class),
			new ColumnInfo("Jours Trav.", "Nb_Day_Works", Integer.class),
//			new ColumnInfo(Msg.translate(ctx, "p_profile_id"), "Profile", String.class, true, false, null),
            new ColumnInfo("Célib.","DayWorkToC", Integer.class),
            new ColumnInfo("Famil.","DayWorkToF", Integer.class),
            new ColumnInfo("Total Avantage imposable","YE_Earn_59", BigDecimal.class),
            new ColumnInfo("Total Célib.","YE_Earn_59_Single", BigDecimal.class),
            new ColumnInfo("Total Famil.","YE_Earn_59_NotSingle", BigDecimal.class),
            new ColumnInfo("Montant Célib.","YE_Earn_59_SingleMax", BigDecimal.class),
            new ColumnInfo("Montant Famil.","YE_Earn_59_NotSingleMax", BigDecimal.class),
            new ColumnInfo("Période Célib.","Period_Single", String.class),
            new ColumnInfo("Période Famil.","Period_NotSingle", String.class),
            new ColumnInfo("Total Base Célib.","YE_Earn_59BI", BigDecimal.class),
            new ColumnInfo("Total Base Famile","YE_Earn_59EI", BigDecimal.class),
            new ColumnInfo("Total Enrichie Célib.","YE_Earn_59BF", BigDecimal.class),
            new ColumnInfo("Total Enrichie Famile","YE_Earn_59EF", BigDecimal.class),
		},
			//	FROM
			" CHL_Insurance_Rpt ",
			//	WHERE
			" 1 = 1 "  ,
         	//	additional where & order in loadTableInfo() 
			true, "CHL_Insurance_Rpt");
		
		//
		miniTable.getModel().addTableModelListener(this);    	

	}   //  dynInit

	private ColumnInfo[] getColumn()
	{
		return new ColumnInfo[] 
		{
				new ColumnInfo(" ", "P_Employee_ID", IDColumn.class, false, false, null),
				new ColumnInfo(Msg.translate(Env.getCtx(), "AD_Org_ID"), "Ad_Org_Name", String.class, true, false, null),
				new ColumnInfo(Msg.translate(Env.getCtx(), "p_LongTermLeave_id"), "LongTermLeave_Name", String.class, true, false, null),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Surname"), "Surname", String.class, true, false, null),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Firstname"), "Firstname", String.class, true, false, null),
//				new ColumnInfo("SIN", "SIN", String.class, true, false, null),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Insurancenumber"), "Insurancenumber", Integer.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "DateHired"), "DateHired", Date.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "DateLayoff"), "DateLayoff", Date.class),
				new ColumnInfo("Jours Trav.", "Nb_Day_Works", Integer.class),
//				new ColumnInfo(Msg.translate(ctx, "p_profile_id"), "Profile", String.class, true, false, null),
	            new ColumnInfo("Célib.","DayWorkToC", Integer.class),
	            new ColumnInfo("Famil.","DayWorkToF", Integer.class),
	            new ColumnInfo("Total Avantage imposable","YE_Earn_59", BigDecimal.class),
	            new ColumnInfo("Total Célib.","YE_Earn_59_Single", BigDecimal.class),
	            new ColumnInfo("Total Famil.","YE_Earn_59_NotSingle", BigDecimal.class),
	            new ColumnInfo("Montant Célib.","YE_Earn_59_SingleMax", BigDecimal.class),
	            new ColumnInfo("Montant Famil.","YE_Earn_59_NotSingleMax", BigDecimal.class),
	            new ColumnInfo("Période Célib.","Period_Single", String.class),
	            new ColumnInfo("Période Famil.","Period_NotSingle", String.class),
	            new ColumnInfo("Total Base Célib.","YE_Earn_59BI", BigDecimal.class),
	            new ColumnInfo("Total Base Famile","YE_Earn_59BF", BigDecimal.class),
	            new ColumnInfo("Total Enrichie Célib.","YE_Earn_59EI", BigDecimal.class),
	            new ColumnInfo("Total Enrichie Famile","YE_Earn_59EF", BigDecimal.class),
/*								
			new ColumnInfo(" ", "P_Employee_ID", IDColumn.class, false, false, null),
			new ColumnInfo("Division", "Ad_Org_Name", String.class, true, false, null),
			new ColumnInfo("Status d'emploi", "LongTermLeave_Name", String.class, true, false, null),
			new ColumnInfo("Nom", "Surname", String.class, true, false, null),
			new ColumnInfo("Prénom", "Firstname", String.class, true, false, null),
			new ColumnInfo("SIN", "SIN", String.class, true, false, null),
			new ColumnInfo("Quickcard", "Insurancenumber", Integer.class),
			new ColumnInfo("Embauche", "DateHired", Date.class),
			new ColumnInfo("Terminaison", "DateLayoff", Date.class),
			new ColumnInfo("Jours Trav.", "Nb_Day_Works", Integer.class),
//			new ColumnInfo("Régime", "Profile", String.class, true, false, null),
	        new ColumnInfo("Célib.","DayWorkToC", Integer.class),
	        new ColumnInfo("Famil.","DayWorkToF", Integer.class),
	        new ColumnInfo("YE EARN 59","YE_Earn_59", BigDecimal.class),
            new ColumnInfo("Total Célib.","YE_Earn_59_Single", BigDecimal.class),
            new ColumnInfo("Total Famil.","YE_Earn_59_NotSingle", BigDecimal.class),
            new ColumnInfo("Montant Célib.","YE_Earn_59_SingleMax", BigDecimal.class),
            new ColumnInfo("Montant Famil.","YE_Earn_59_NotSingleMax", BigDecimal.class),
            new ColumnInfo("Période Célib.","Period_Single", String.class),
            new ColumnInfo("Période Famil.","Period_NotSingle", String.class)
*/            
		};	
	}
	
	/**
	 * On remplit les combobox d'années en leur affectant l'année de la période ouverte comme valeur par défaut
	 */
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
	        s_log.log(Level.SEVERE, "VGainDetail.fillYear()", e);
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
		
		sql += " And P_Year_ID = " + new Integer(((KeyNamePair)fieldSelectionYear.getSelectedItem()).getKey());
	
		int P_Employee_ID = 0;

		Object value = fieldEmployee.getValue();
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
		
		int Payment_Group_id = Integer.parseInt(fieldGroupPayment.getValue().toString());	
		if (Payment_Group_id != 0)
		{
			sql += " and P_Payment_Group_id = " + Payment_Group_id;
		}


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
			    //exportToCSV();
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
			
			        //			      -------------PGI Solstice 07/04/2008-----------------
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
			                		fileWriter.write("<Cell><Data ss:Type=\"String\">" + nvl( rs.getString(i) ) + "</Data></Cell>\n");
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.BigDecimal") )
			                		fileWriter.write("<Cell><Data ss:Type=\"Number\">" + nvl( rs.getString(i) ) + "</Data></Cell>\n");
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.Double") )
			                		fileWriter.write("<Cell><Data ss:Type=\"Number\">" + nvl( rs.getString(i) ) + "</Data></Cell>\n");
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.sql.Timestamp") )
			                		fileWriter.write("<Cell><Data ss:Type=\"String\">" + ((rs.getTimestamp(i) != null)? rs.getTimestamp(i).toString().substring(0,10) : "") + "</Data></Cell>\n");
			                	else
			                		fileWriter.write("<Cell><Data ss:Type=\"String\">" + nvl( rs.getString(i) ) + "</Data></Cell>\n");
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
	
	private BigDecimal nvl ( BigDecimal bd )
	{
		if ( bd == null )
			return new BigDecimal(0);
		else
			return bd;
	}

	private String nvl ( String str )
	{
		if ( str == null )
			return "";
		else
			return str;
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
