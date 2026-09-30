
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


import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.Sides;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.compiere.Compiere;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.SwingWorker;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VCheckBox;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.MSort;
import org.compiere.util.Msg;
import org.compiere.util.DB;

import solstice.model.P_Distribution;
import solstice.model.P_Employee;
import solstice.utils.PgiUtil;

import org.compiere.plaf.CompiereColor;

import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfFusion extends CPanel
implements FormPanel, ActionListener 
{
	
	private int P_Year_ID = 0;

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
			frame.getContentPane().add(northPanel, BorderLayout.NORTH);
			frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
			frame.getContentPane().add(confirmPanel, BorderLayout.SOUTH);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "init", e);
		}
	}	//	init

	/**	Window No			*/
	private int         		m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 			m_frame;

	private ArrayList<String>	m_data = new ArrayList<String>();
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(EndYearPrintAvantaxPdf.class);
	//
//	private static final String s_none = "----";	//	no format indicator
	//
	private CPanel northPanel = new CPanel();
	private JButton bFile = new JButton();
	private CPanel centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout();
	private JScrollPane rawDataPane = new JScrollPane();
	private JTextArea rawData = new JTextArea();
	private JScrollPane previewPane = new JScrollPane();
	private CPanel previewPanel = new CPanel();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true);
	private JLabel info = new JLabel();
	private JLabel dirInfo = new JLabel();

	private JLabel labelFormat = new JLabel();
	private GridBagLayout previewLayout = new GridBagLayout();
	private JLabel record = new JLabel();

	public VCheckBox isRectoVerso = new VCheckBox();

    public PrinterJob job = PrinterJob.getPrinterJob();
    public PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();

	
	private File[] file = null;

	/**
	 *	Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		CompiereColor.setBackground(this);
//		bFile.setText(Msg.getMsg(Env.getCtx(), "FileImportFile"));
		bFile.setText("Imprimer les formulaires");
		bFile.setToolTipText("Imprimer les formulaires");
		bFile.addActionListener(this);
		info.setText("   ");
		
		isRectoVerso.setSelected( true );
		isRectoVerso.setText( "Recto Verso");
		isRectoVerso.setName("isRectoVerso");

//		labelFormat.setText(Msg.translate(Env.getCtx(), "AD_ImpFormat_ID"));
		//

		//
		northPanel.setBorder(BorderFactory.createEtchedBorder());
		northPanel.add(bFile, null);
		northPanel.add(info, null);
		northPanel.add(isRectoVerso, null);
		northPanel.add(labelFormat, null);
		northPanel.add(record, null);
		//
		centerPanel.setLayout(centerLayout);
		rawData.setFont(new java.awt.Font("Monospaced", 0, 10));
		rawData.setColumns(80);
		rawData.setRows(5);
		rawDataPane.getViewport().add(rawData, null);
		centerPanel.add(rawDataPane, BorderLayout.NORTH);
		centerPanel.add(previewPane, BorderLayout.CENTER);
		//
		previewPanel.setLayout(previewLayout);
		previewPane.getViewport().add(previewPanel, null);
		previewPane.setPreferredSize(new Dimension(700,80));
		//
		confirmPanel.addActionListener(this);
	}	//	jbInit

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	/**
	 *	Dynamic Init
	 */
	private void dynInit()
	{
		//	Load Formats
		//
		confirmPanel.getOKButton().setEnabled(false);
	}	//	dynInit

	String directory = "";
	
	/**************************************************************************
	 *	Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{

		String path = PgiUtil.getSolsticeParameter(Env.getCtx(), "AvantaxPath");
		
    	path = "C:\\Solstice\\Transferts\\T4_R1\\PDF Output\\";

		
		if ( path.endsWith( File.separator ) == false )
			path = path + File.separator;

		directory = path ; //+ "Export" + File.separator;

		if (e.getSource() == bFile)
		{
//	    	String startDir = Ini.getCompiereHome() + File.separator + "data";
//			startDir = "\\\\192.168.3.194\\humanRess$\\Payroll\\Avantax eForms 2018\\PDF Output\\";
			
			
			
			JFileChooser chooser = new JFileChooser(directory);
			chooser.setMultiSelectionEnabled(false);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setDialogTitle("Sélectionner les répertoires contenant les fichiers pdf");

			chooser.setMultiSelectionEnabled(false);
//			chooser.setDialogTitle(Msg.getMsg(Env.getCtx(), "FileImportFileInfo"));
			if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
				return ;

			String dirName = chooser.getSelectedFile().getAbsolutePath();
//			bFile.setText(dirName);

			info.setText( dirName );

			directory = chooser.getSelectedFile().getAbsolutePath();

	    	if ( ! directory.endsWith( File.separator ))
	    		directory = directory + File.separator;

			//			cmd_loadFile();
			invalidate();
			m_frame.pack();
		}

		
		else if (e.getActionCommand().equals(ConfirmPanel.A_OK))
		{
			m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			confirmPanel.setEnabled(false);
			m_frame.setBusy(true);

			
			//
			SwingWorker worker = new SwingWorker()
			{
				public Object construct()
			    {
			    	cmd_process();
					return Boolean.TRUE;
				}
			};
			worker.start();
			
			//  when you need the result:
			//	x = worker.get();   //  this blocks the UI !!
		}
		else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
		{
			dispose();
		}

		confirmPanel.getOKButton().setEnabled(true);

	}	//	actionPerformed

	/**************************************************************************
	 *	Process File
	 */
	void cmd_process()
	{
		try 
		{
			this.printPdf();
		}
		catch (Exception ex) 
		{
			log.log(Level.SEVERE, "Error :", ex);
		}
		dispose();
	}

	
	public PdfFusion(  )
	{
	}
	public PdfFusion ( boolean Standalone )
	{
		try 
		{
			this.printPdf();
//			this.addInstructionPdf();
		}
		catch (Exception e) 
		{
			log.log(Level.SEVERE, "Error :", e);
		}
	}
	
	  class InfoFile{
		  String fileName;
		  String name;
		  public InfoFile( String fileName, String name)
		  {
			  this.fileName = fileName;
			  this.name = name;
		  }

		  public String getFileName(){
		      return this.fileName;
		  }
		  public String getName(){
		      return this.name;
		  }

	  }

	  ArrayList<InfoFile> empArray = new ArrayList<InfoFile>();

	  
		private InfoFile EmpArraygetWithID( String name )
		{
	        for(int i = 0; i < empArray.size() ; i++)
	        {
	        	if ( empArray.get(i).getName() == name )
	        		return empArray.get(i);
	        }
			
	        return null;
		}

		
	// ATTachement
    public String printPdf() throws Exception
    {

        attr.add( Sides.DUPLEX );

        if (job.printDialog())
        {
        	
        }
        //m.m
        directory = "\\\\10.10.10.4\\Transferts\\Paystub\\imp\\";
     
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        
        String dest = directory + "A_Imprimer" + ".pdf" ; //args[0];
 //       File destFile = new File( dest );
    	
    	File folder = new File(directory);
    	File[] listOfFiles = folder.listFiles();
    	
    	Arrays.sort(listOfFiles);
    	
    	for (File file : listOfFiles) {
    	    if (file.isFile()) {
    	    	if (file.getName().startsWith("2024")  ) {

//    	    		fileArray[ i ] = file.getName().substring(5, file.getName().indexOf(".") );
    	    		
		            pdfMerger.addSource( file );
		            
    	    	}
		            
   	    	}
   	    }
        File destFile = new File( dest );
        
        pdfMerger.setDestinationFileName(destFile.getAbsolutePath());
        try {
            pdfMerger.mergeDocuments( null );
        } catch (Exception e) {
        }



        return "";
    }

    
    public static void main (String[] args)
	{
		System.out.println("EndYearPrintAvantaxPdf");
		System.out.println("----------------------------------");
		//
		int count = 0;
		try
		{
			Compiere.startup(true);
			new PdfFusion( true );
			
			count++;
		}
		catch (Exception e) {
			log.log(Level.SEVERE, "*ERROR EndYearImportAvantaxPdf Fail", e);
		}

		System.out.println("EndYearImportAvantaxPdf = " + count);

	}	//	main

 }
