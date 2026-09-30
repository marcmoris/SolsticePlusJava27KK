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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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


public class CHL_Employee extends CPanel 
implements FormPanel, ActionListener, TableModelListener, ASyncProcess
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 *  CHL_Employee Constructor
	 */
	public CHL_Employee()
	{
	}   //  CHL_Employee

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		s_log.log(Level.INFO, "init");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			
			jbInit();
			dynInit();
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

//			this.miniTable.getTableHeader().addMouseListener(new ColumnChecker(this.miniTable));


		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE, "init" + e);
		}
	}	//	init

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (CHL_Employee.class);
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

	private CLabel labelRegion = new CLabel();
	private VComboBox fieldRegion = new VComboBox();

	private CLabel labelLongTermLeave = new CLabel();
	private VComboBox fieldLongTermLeave = new VComboBox();


	/** Dispose active                                  */
	private boolean         m_disposing = false;
	
	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		fillCombobox();
	    
		CompiereColor.setBackground(this);
		//
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);
		//
		labelRegion.setText(Msg.translate(Env.getCtx(), "C_Region_ID"));
		labelLongTermLeave.setText(Msg.translate(Env.getCtx(), "P_LongTermLeave_ID"));
		
		labelGroupPayment.setText(Msg.translate(Env.getCtx(), "P_Payment_Group_ID"));
		
		labelActivity.setText(Msg.translate(Env.getCtx(), "C_activity_ID"));
		
		labelOrg.setText(Msg.translate(Env.getCtx(), "ad_org_id"));
		labelDept.setText(Msg.translate(Env.getCtx(), "P_Department_ID"));

		

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

		parameterPanel.add(labelDept,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldDept,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelActivity,   new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldActivity,    new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(labelRegion,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldRegion,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelLongTermLeave,   new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldLongTermLeave,    new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		parameterPanel.add(this.bExport,    new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(bRefresh,    new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));


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
		//Fisrt name, last name, assurance number, adresse, ville, province, country, postal code, statut d'activité, date de statut d'activité. 
		Properties ctx = Env.getCtx();
		m_sql = miniTable.prepareTable(new ColumnInfo[] {
			//  0..4
			new ColumnInfo(" ", "P_Employee_ID", IDColumn.class, false, false, null),
			new ColumnInfo(Msg.translate(ctx, "Firstname"), "Firstname", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Surname"), "Surname", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Insurancenumber"), "Insurancenumber", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Sin"), "Sin", String.class, true, false, null),

			new ColumnInfo(Msg.translate(ctx, "Address1"), "Address1", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Address2"), "Address2", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Address3"), "Address3", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Address4"), "Address4", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "City"), "C_Location.City", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Postal"), "Postal", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "C_Region_ID"), "C_Region.Name", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "C_Country_ID"), "C_Country.Name", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "P_LongTermLeave_ID"), "P_LongTermLeave.Name", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "LongTermLeaveDate"), "LongTermLeaveDate", Date.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "P_Job_Type_ID"), "P_Job_Type.Name", String.class, true, false, null)
			
		},
			//	FROM
			" P_Employee P_Employee "  +
            " Inner Join C_Location C_Location  ON C_Location.C_Location_ID = P_Employee.C_Location_ID " +
            " Left outer Join P_Job_Type P_Job_Type ON P_Job_Type.P_Job_Type_ID = P_Employee.P_Job_Type_ID " +
            " Left outer Join C_Region C_Region ON C_Location.C_Region_ID = C_Region.C_Region_ID " +
            " Left outer Join C_Country C_Country ON C_Location.C_Country_ID = C_Country.C_Country_ID " +
            " Left outer Join P_LongTermLeave P_LongTermLeave ON P_Employee.P_LongTermLeave_ID = P_LongTermLeave.P_LongTermLeave_ID "
            ,
			//	WHERE
			" 1 = 1 and P_Job_Type.ISOCCASIONAL= 'N'"  ,
         	//	additional where & order in loadTableInfo() 
			true, "P_Employee");
		
		//
		miniTable.getModel().addTableModelListener(this);    	

	}   //  dynInit

	
	private ColumnInfo[] getColumn()
	{
		Properties ctx = Env.getCtx();
		return new ColumnInfo[] 
		{
				new ColumnInfo(" ", "P_Employee_ID", IDColumn.class, false, false, null),
				new ColumnInfo(Msg.translate(ctx, "Firstname"), "Firstname", String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx, "Surname"), "Surname", String.class, true, false, null),
				new ColumnInfo(Msg.translate(ctx, "Insurancenumber"), "Insurancenumber", String.class),
				new ColumnInfo(Msg.translate(ctx, "Sin"), "Sin", String.class, true, false, null),

				new ColumnInfo(Msg.translate(ctx, "Address1"), "Address1", String.class),
				new ColumnInfo(Msg.translate(ctx, "Address2"), "Address2", String.class),
				new ColumnInfo(Msg.translate(ctx, "Address3"), "Address3", String.class),
				new ColumnInfo(Msg.translate(ctx, "Address4"), "Address4", String.class),
				new ColumnInfo(Msg.translate(ctx, "City"), "C_Location.City", String.class),
				new ColumnInfo(Msg.translate(ctx, "Postal"), "Postal", String.class),
				new ColumnInfo(Msg.translate(ctx, "C_Region_ID"), "C_Region.Description", String.class),
				new ColumnInfo(Msg.translate(ctx, "C_Country_ID"), "C_Country.Name", String.class),
				new ColumnInfo(Msg.translate(ctx, "P_LongTermLeave_ID"), "P_LongTermLeave.Name", String.class),
				new ColumnInfo(Msg.translate(ctx, "LongTermLeaveDate"), "LongTermLeaveDate", Date.class),
				new ColumnInfo(Msg.translate(ctx, "P_Job_Type_ID"), "P_Job_Type.Name", String.class)
				
		};	
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

	    String sqlRegion = "select C_Region_ID, Description as Name from C_Region where IsActive = 'Y' and C_Country_ID = 109 ";
	    sqlRegion = sqlRegion + " And C_Region." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
		+ " ORDER BY C_Region.Description";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlRegion, null);
	        ResultSet rs = stmt.executeQuery();
	        this.fieldRegion.addItem(new KeyNamePair(0,""));
	        while(rs.next())
	        {
	        	this.fieldRegion.addItem(new KeyNamePair(rs.getInt("C_Region_ID"), rs.getString("Name")));
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        s_log.log(Level.SEVERE, "fillCombobox()", e);
	    }	

	    String sqlLongTermLeave = "select P_LongTermLeave_ID,Value + ' - ' + Name as Name from P_LongTermLeave where IsActive = 'Y'";
	    sqlLongTermLeave = sqlLongTermLeave + " And P_LongTermLeave." + MRole.getDefault().getClientWhere(false)	// fully qualidfied - RO 
		+ " ORDER BY P_LongTermLeave.Value + ' - ' + P_LongTermLeave.Name";
		try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sqlLongTermLeave, null);
	        ResultSet rs = stmt.executeQuery();
	        this.fieldLongTermLeave.addItem(new KeyNamePair(0,""));
	        while(rs.next())
	        {
	        	this.fieldLongTermLeave.addItem(new KeyNamePair(rs.getInt("P_LongTermLeave_ID"), rs.getString("Name")));
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
		
	
		int C_Region_ID = Integer.parseInt( fieldRegion.getValue().toString() );
		
		if (C_Region_ID != 0)
			sql += " AND C_Location.C_Region_ID = " + C_Region_ID;

		int	P_LongTermLeave_ID = Integer.parseInt( fieldLongTermLeave.getValue().toString() );
		
		if ( fieldLongTermLeave.getSelectedIndex() > 0)
			sql += " AND P_Employee.P_LongTermLeave_ID = " + P_LongTermLeave_ID;

		// ---------------------------------

		int Org_id = Integer.parseInt(fieldOrg.getValue().toString());
		if (Org_id != 0)
		{
			sql += " and P_Employee.AD_Org_id =  " + Org_id;
		}
		
		int Payment_Group_id = Integer.parseInt(fieldGroupPayment.getValue().toString());	
		if (Payment_Group_id != 0)
		{
			sql += " and P_Employee.P_Payment_Group_id = " + Payment_Group_id;
		}


		int Department_id = Integer.parseInt(fieldDept.getValue().toString());	
		if (Department_id != 0)
		{
			sql += " and P_Employee.P_Department_id = " + Department_id;
		}

		int Activity_id = Integer.parseInt(fieldActivity.getValue().toString());	
		if (Activity_id != 0)
		{
			sql += " and P_Employee.C_Activity_id = " + Activity_id;
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
			        fileWriter.write(" <Worksheet ss:Name=\"" + Msg.translate(Env.getCtx(), "P_Employee_ID") + "\">\n");
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
			                		fileWriter.write("<Cell><Data ss:Type=\"String\">" + rs.getString(i) + "</Data></Cell>\n");
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.BigDecimal") )
			                		fileWriter.write("<Cell><Data ss:Type=\"Number\">" + rs.getString(i) + "</Data></Cell>\n");
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.math.Double") )
			                		fileWriter.write("<Cell><Data ss:Type=\"Number\">" + rs.getString(i) + "</Data></Cell>\n");
			                	else if ( rsMetaData.getColumnClassName(i).equals("java.sql.Timestamp") )
			                		fileWriter.write("<Cell><Data ss:Type=\"String\">" + ((rs.getTimestamp(i) != null)? rs.getTimestamp(i).toString().substring(0,10) : "") + "</Data></Cell>\n");
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
		s_log.log(Level.INFO, "executeASync");
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
