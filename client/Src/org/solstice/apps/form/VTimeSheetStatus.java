/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2001 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
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
 *  @version $Id: VTimeSheetStatus.java,v 1.2 2007/07/19 15:09:35 marmor01 Exp $
 */
public class VTimeSheetStatus extends CPanel
	implements FormPanel, ActionListener, TableModelListener, ASyncProcess
{

	/**
	 *  VTimeSheetStatus Constructor
	 */
	public VTimeSheetStatus()
	{
	}   //  VTimeSheetStatus

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		s_log.log(Level.INFO, "VTimeSheetStatus.init");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			MLookup periodL = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 2111061, DisplayType.Search);
//			fieldPeriod = new VLookup ("P_Period_ID", false, false, true, periodL);

			fieldPeriod.setBounds(206, 20, 210, 19);

			
			jbInit();
			dynInit();
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

//			this.miniTable.getTableHeader().addMouseListener(new ColumnChecker(this.miniTable));

		}
		catch(Exception e)
		{
			s_log.log(Level.SEVERE, "VTimeSheetStatus.init" + e);
		}
	}	//	init

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (VTimeSheetStatus.class);
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;

	/** Format                  */
	private DecimalFormat   m_format = DisplayType.getNumberFormat(DisplayType.Amount);
	/** Bank Balance            */
	private BigDecimal      m_bankBalance = new BigDecimal(0.0);
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
	private CLabel labelStatus = new CLabel();
	private VComboBox fieldStatus = new VComboBox();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private CLabel labelPeriod = new CLabel();
//	private VLookup fieldPeriod;
	private VComboBox fieldPeriod = new VComboBox();


	private JLabel dataStatus = new JLabel();
	private JScrollPane dataPane = new JScrollPane();
	private MiniTable miniTable = new MiniTable();
	private CPanel commandPanel = new CPanel();
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	private FlowLayout commandLayout = new FlowLayout();
	private JButton bRefresh = ConfirmPanel.createRefreshButton(true);
	private CLabel labelErrorMsg = new CLabel();

//	private DialogButton bExport = new DialogButton ("export", Msg.translate(Env.getLanguage(Env.getCtx()), "ExportExcel"), Env.getImageIcon("Export24.gif"), 0);
	private JButton bExport = ConfirmPanel.createExportButton(true);

	/** Dispose active                                  */
	private boolean         m_disposing = false;


	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
	    this.fillPeriods();

	    this.labelErrorMsg.setText(Msg.translate(Env.getCtx(), "TimeSheetStatusMandatory"));
	    this.labelErrorMsg.setVisible(false);
	    this.labelErrorMsg.setForeground(Color.RED);
	    
		CompiereColor.setBackground(this);
		//
		mainPanel.setLayout(mainLayout);
		parameterPanel.setLayout(parameterLayout);
		//
		labelPeriod.setText(Msg.translate(Env.getCtx(), "P_Period_ID"));
		fieldStatus.addActionListener(this);
		labelStatus.setText(Msg.translate(Env.getCtx(), "StatusPreceding"));
//		fieldEmployee.addActionListener(this);
		bRefresh.addActionListener(this);
		this.bExport.addActionListener(this);
		//
		dataStatus.setText(" ");
		//
		bCancel.addActionListener(this);
		//
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		parameterPanel.add(labelPeriod,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldPeriod,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(labelStatus,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(fieldStatus,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(bRefresh,    new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//parameterPanel.add(labelMovementDateFrom,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
		//	,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		//parameterPanel.add(fieldMovementDateFrom,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
		//	,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(this.labelErrorMsg,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(this.bExport,    new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
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

		//  Sheet status
		String sql = 
			"SELECT ref.value, trl.name "                       //  1
			+ " FROM ad_ref_list ref, ad_ref_list_trl trl "
			+ " where ref.ad_reference_id=2000078 "
			+ " and trl.ad_ref_list_ID=ref.ad_ref_list_ID "
			+ "ORDER BY ref.Description";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();

			StatusInfo si = new StatusInfo ("", "");
			fieldStatus.addItem(si);

			while (rs.next())
			{
				si = new StatusInfo (rs.getString(1), rs.getString(2));
				fieldStatus.addItem(si);
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "Erreur ", e);
		}
		if (fieldStatus.getItemCount() <= 0)
			;
		else
//ici left outer join
		m_sql = miniTable.prepareTable(new ColumnInfo[] {
			//  0..6
			new ColumnInfo(Msg.translate(ctx, "SheetNumber"), "ts.Value", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "SheetStatus"), "CASE ts.TimeSheetStatus WHEN 'I' THEN 'Initiale' WHEN 'F' THEN 'En erreur' WHEN 'E' THEN 'Émise' WHEN 'V' THEN 'Validée' WHEN 'C' THEN 'Calculée' WHEN 'T' THEN 'Transférée' END ", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "SheetType"), "trl.Name", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "Name"), "emp.Value + ' - ' + emp.Name", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "P_Job_Type_ID"), "type.Value", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "P_Job_Title_ID"), "title.value + ' - ' + title.name", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "P_Occupation_Group_ID"), "grp.Name", String.class, true, false, null),
			new ColumnInfo(Msg.translate(ctx, "DistributionCode"), "db.Value +' '+ db.Name", String.class, true, false, null),
			},
			//	FROM
			" P_Time_Sheet ts "
			+ " , P_Distribution_Booklet db "
			+ " , P_Employee emp "
			+ " , P_Job_Title title "
			+ " , P_Job_Type type "
			+ " , P_Occupation_Group grp "
			+ " , AD_Ref_List ref "
			+ " , AD_Ref_List_Trl trl ",
			//	WHERE
			" ts.P_Distribution_Booklet_ID=db.P_Distribution_Booklet_ID "
			+ " AND ref.AD_Reference_ID=2100376 "//Sheet Type
			+ " AND ts.P_Employee_ID=emp.P_Employee_ID "
			+ " AND ts.P_Job_Title_ID=title.P_Job_Title_ID "
			+ " AND ts.P_Job_Type_ID=type.P_Job_Type_ID "
			+ " AND ts.P_Occupation_Group_ID=grp.P_Occupation_Group_ID "
			+ " AND ts.SheetType = ref.name "
			+ " AND ref.AD_Ref_List_ID = trl.AD_Ref_List_ID "
			+ " AND ts.P_Period_ID = ? "
			//+ " AND TimeSheetStatus in (?) "
			, true, "ts");
		
		//
		miniTable.getModel().addTableModelListener(this);
		//
		//
		m_AD_Client_ID = Integer.parseInt(Env.getContext(Env.getCtx(), "#AD_Client_ID"));
	}   //  dynInit


	/**
	 *  Query and create TableInfo
	 */
	private void loadTableInfo()
	{
		s_log.log(Level.INFO, "VTimeSheetStatus.loadTableInfo");

		
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
			s_log.log (Level.SEVERE, "VTimeSheetStatus.loadTableInfo:" + e);
		}
		//calculateSelection();
	}   //  loadTableInfo
	
	private PreparedStatement prepareStatement() throws Exception
	{
		//  not yet initialized
		if (m_sql == null)
			return null;

		String sql = m_sql;
		//  Parameters
		int P_Period_ID = 0;

		Object value = fieldPeriod.getValue();
		if (value != null && value.toString().length() > 0)
		{
			P_Period_ID = Integer.parseInt( value.toString() );
		}

		StatusInfo si = (StatusInfo)fieldStatus.getSelectedItem();
		String strStatus = si.getValue();

		// STATUS DE LA FEUILLE DE TEMPS
		// #ordre - Value - Name
		// 1 - I - Initiale
		// 2 - A - Approuvée
		// 3 - V - Validée
		// 4 - F - en Erreur
		// 5 - C - Calculée
		// 6 - E - Émise
		// 7 - Z - Annulée
		// 8 - T - Transférée
		
		String strStatusToHave = "''";
		
		if(strStatus.equals("T") == true)
			strStatusToHave = " 'I', 'A', 'V', 'F', 'C', 'E', 'Z' ";
		else if(strStatus.equals("Z") == true)
			strStatusToHave = " 'I', 'A', 'V', 'F', 'C', 'E' ";
		else if(strStatus.equals("E") == true)
			strStatusToHave = " 'I', 'A', 'V', 'F', 'C' ";
		else if(strStatus.equals("C") == true)
			strStatusToHave = " 'I', 'A', 'V', 'F' ";
		else if(strStatus.equals("F") == true)
			strStatusToHave = " 'I', 'A', 'V' ";
		else if(strStatus.equals("V") == true)
			strStatusToHave = " 'I', 'A' ";
		else if(strStatus.equals("A") == true)
			strStatusToHave = " 'I' ";
		

		sql += " AND TimeSheetStatus IN ("+strStatusToHave+") ";
		System.out.println(sql);
		int index = 1;
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		//pstmt.setInt(index++, m_AD_Client_ID);
//		if (onlyDue.isSelected())

		if (P_Period_ID != 0)
			pstmt.setInt(index++, P_Period_ID);
		System.out.println(P_Period_ID);
		//if (strStatusToHave.length() >= 0)
		//	pstmt.setString(index++, strStatusToHave);
		//System.out.println(strStatusToHave);
		
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
			}
			catch (IOException ex)
			{
			    s_log.log(Level.SEVERE, "exportToXml", ex);
			}
			setBusy( false );
	    }
	    else
	    {
			int P_Period_ID = 0;
			String strStatus = "";
	
			Object value = fieldPeriod.getValue();
			if (value != null && value.toString().length() > 0)
			{
				P_Period_ID = Integer.parseInt( value.toString() );
			}
	
			StatusInfo si = (StatusInfo)fieldStatus.getSelectedItem();
			if ( si != null )
				strStatus = si.getValue();
	
			
			// On doit vérifier si l'utilisateur a saisi au moins un paramètre entre la
			// banque et l'employé
			if (P_Period_ID != 0 && strStatus.length() > 0)
			{
			    this.labelErrorMsg.setVisible(false);
			    
				if (e.getSource() == bCancel)
					dispose();
	
				//  Update Open Invoices
				else if (e.getSource() == fieldPeriod || e.getSource() == fieldStatus || e.getSource() == bRefresh)
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
			        fileWriter.write(" <Worksheet ss:Name=\"Feuille\">\n");
			        fileWriter.write("  <Table ss:ExpandedColumnCount=\"" + rsMetaData.getColumnCount() + "\">\n");

/*		            fileWriter.write("<Row>\n");
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
			            String buffer = "";
			            fileWriter.write("<Row>\n");
			            for(int i = 1; i <= rsMetaData.getColumnCount(); i++)
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

		//if (e.getColumn() == 0)
			//calculateSelection();
	}   //  valueChanged

	/**
	 *  Calculate selected rows.
	 *  - add up selected rows
	 */
	public void calculateSelection()
	{
		

		m_noSelected = 0;
		BigDecimal creditsAmt = new BigDecimal(0.0);

		int rows = miniTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getModel().getValueAt(i, 0);
			if (id.isSelected())
			{
				BigDecimal amt = (BigDecimal)miniTable.getModel().getValueAt(i, 6);
				creditsAmt = creditsAmt.add(amt);
				m_noSelected++;
			}
		}

		//  Information
		BigDecimal remaining = m_bankBalance.subtract(creditsAmt);
		StringBuffer info = new StringBuffer();
		info.append(m_noSelected).append(" ").append(Msg.getMsg(Env.getCtx(), "Selected")).append(" - ");
		info.append(m_format.format(creditsAmt));  //.append(", ");
//		info.append(Msg.getMsg(Env.getCtx(), "Remaining")).append(" ").append(m_format.format(remaining));
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
		if (!ADialog.ask(m_WindowNo, this, "VTimeSheetStatusPrint?", "(" + pi.getSummary() + ")"))
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
		s_log.log(Level.INFO, "VTimeSheetStatus.executeASync");
	}   //  executeASync

	/*************************************************************************/

	/**
	 *  Bank Account Info
	 */
	public class StatusInfo
	{
		public StatusInfo (String newValue, String newName)
		{
			Value = newValue;
			Name = newName;
		}
		private String Value;
		private String Name;
		public String toString()
		{
			return Name;
		}
		public String getValue()
		{return Value;}
		public String getName()
		{return Name;}
	}   //  VTimeSheetStatusInfo

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
	            this.fieldPeriod.addItem(new KeyNamePair(rs.getInt("P_Period_ID"), rs.getString("Name")));
	            if(rs.getString("PeriodStatus").toUpperCase().equals("O"))
	            {
	                this.fieldPeriod.setSelectedIndex(index);
	            }
	            index++;
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
	        s_log.log(Level.SEVERE, "fillPeriods()", e);
	    }
	}

	
}   //  VTimeSheetStatusTotal
