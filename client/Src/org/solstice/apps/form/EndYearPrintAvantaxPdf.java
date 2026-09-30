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

public class EndYearPrintAvantaxPdf extends CPanel
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
//	String directory = "\\\\192.168.3.194\\humanRess$\\Payroll\\Avantax eForms 2018\\PDF Output\\";
	
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

	
	public EndYearPrintAvantaxPdf(  )
	{
	}
	public EndYearPrintAvantaxPdf ( boolean Standalone )
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
/*
	protected void prepare()
    {
    	
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("P_Year_ID"))
			{
				P_Year_ID = para[i].getParameterAsInt();
			}
			
		}
		MPInstance instance = new MPInstance(getCtx(),this.getAD_PInstance_ID(), null);
    	int process_id = instance.getAD_Process_ID();
    	process = new MProcess(getCtx(),process_id, null);
    	
    }	//	prepare

*/
	
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

    public String addInstructionPdf() throws Exception
    {
        attr.add( Sides.DUPLEX );

        if (job.printDialog())
        {
        	
        }
        //m.m
        directory = "C:\\Solstice\\Transferts\\T4_R1\\PDF Output 2\\";


    	File folder = new File(directory);
    	File[] listOfFiles = folder.listFiles();
//    	String[][] empArray = new String[ listOfFiles.length ][]; 


    	
    	Arrays.sort(listOfFiles);
    	
    	for (File file : listOfFiles) {
    	    if (file.isFile()) {
    	    	
    	    	if (file.getName().startsWith("T4") || file.getName().startsWith("TA") ) {
    	            String t4verso = directory + "SlipT4 Instructions.pdf" ; //args[0];

    	            File t4VersoFile = new File(t4verso);
    	         
    	            PDFMergerUtility pdfMerger = new PDFMergerUtility();

   		            if ( file.exists()  ) {
   			            pdfMerger.addSource(file);
   			            pdfMerger.addSource(t4VersoFile);

   	   		            String dest = "";
   	    	            dest = directory + "\\"+ file.getName(); //.replace("T4", "WEB//T4")  ; 
   	    	            File destFile = new File( dest );

   	    	            pdfMerger.setDestinationFileName(destFile.getAbsolutePath());
   	    	            try {
   	    	                pdfMerger.mergeDocuments( null );
   	    	            } catch (Exception e) {
   	    	            }


   		            }
    	            
        	    }

    	    }
    	}
        
        return "";
    }

		
	// ATTachement
    public String printPdf() throws Exception
    {

        attr.add( Sides.DUPLEX );

        if (job.printDialog())
        {
        	
        }
        //m.m
        directory = "C:\\Solstice\\Transferts\\T4_R1\\2025\\PrintYY\\";
     
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        
        String dest = directory + "A_Imprimer" + ".pdf" ; //args[0];
        File destFile = new File( dest );
    	
    	File folder = new File(directory);
    	File[] listOfFiles = folder.listFiles();
//    	String[][] empArray = new String[ listOfFiles.length ][]; 

    	String[] fileArray = new String[ listOfFiles.length ];


    	
    	int i = 0;
    	
    	Arrays.sort(listOfFiles);
//NOTE 2020 1 Employee avec juste 1 R1
    	
    	for (File file : listOfFiles) {
    	    if (file.isFile()) {
    	    	
    	    	if (file.getName().startsWith("T4") || file.getName().startsWith("TA") ) {
    	    		fileArray[ i ] = file.getName().substring(5, file.getName().indexOf(".")  );
    	    		empArray.add( new InfoFile( file.getName(), file.getName().substring(5, file.getName().indexOf(".")  )  ) ) ;
    	    		
    	    	}
/*
    	    	if (file.getName().startsWith("R1") )
    	    		fileArray[ i ] = file.getName().substring(6, 15 );
    	    		
    	    		empArray.add( new InfoFile( file.getName(), file.getName().substring(6, 15 )  ) ) ;
*/    	    		
        		i++;		
    	    }
    	}


/*    	
//       	P_Year Year = P_Year.get( Env.getCtx(), this.P_Year_ID, null);

   		String sql = "Select DISTINCT P_Employee_ID, Value, Name, SIN FROM P_Employee "
   				+ " WHERE P_Employee_ID in ( SELECT P_Employee_ID FROM P_Form_Employee WHERE P_Year_ID = " + P_Year_ID + " )  ORDER BY P_Employee.Value "; 
   		PreparedStatement stmt = DB.prepareStatement(sql, null);
*/
    	
//    	empArray.sort( null );
        try
        {
/*
        	ResultSet rs = stmt.executeQuery();
	        while(rs.next())
*/	       
        	
        int nform = 0;
        
        String tmp = "";
        		
        for(int j = 0; j < empArray.size() ; j++)
    	{
        	
        		if ( ! empArray.get(j).getName().equals( tmp ) )
        			nform = 0;
        		else 
        			nform = 1;
        		
//        	    P_Employee Employee = this.getWithSin(Env.getCtx(), empArray.get(j).getName() , null);
//        	    P_Distribution Distribution = P_Distribution.get( Env.getCtx(), Employee.getP_Distribution_ID(), null);
//        	    if ( Distribution.isCodeForAbsentEmployees() == true || Employee.isEndOfYearFormWebConfirmation() == false )
//        	    if ( Employee.isPrintEndOfYearForm() == true)
        	    {

            		String fileName = empArray.get(j).getFileName();
//    	        	P_Employee Employee = P_Employee.get( Env.getCtx(), rs.getInt("P_Employee_ID"), null);
    	        	
//    	        	String t4TP = directory + "T4".trim() +"TP" + Employee.getSin().replace("-", "") + ".pdf";
//    	        	String t4TR = directory + "T4".trim() +"TR" + Employee.getSin().replace("-", "") + ".pdf";

    	        	String t4 = directory + empArray.get(j).getFileName();

    	            String t4verso = directory + "SlipT4 Instructions.pdf" ; //args[0];
//    	            String t4verso = directory + "SlipT4a Instructions.pdf" ; //args[0];
//    	        	String r1 = directory + "R1".trim() + Employee.getSin().replace("-", "") + ".pdf";

    	        	String r1 = directory + "R1R1-" + empArray.get(j).getName() + ".pdf";

    	        	System.out.println( empArray.get(j).getName() );
    	        	
    	        	String r1verso = directory + "SlipR1 Instructions.pdf" ; //args[0];


    	            File t4File = new File(t4);
//    	            File t4FileTr = new File(t4TR);
    	            File t4VersoFile = new File(t4verso);

    	            File r1File = new File(r1);
    	            
/*    	            if ( ! r1File.exists()  ) {
    	            	r1 = directory + "R1" + empArray.get(j).getName() + "--0002.pdf";
    		            r1File = new File(r1);
    	            }
*/    	            
    	            
    	            File r1VersoFile = new File(r1verso);
    	            
    	          //add instruction a chaque T4     	            
//    	            PDFMergerUtility pdfMerger = new PDFMergerUtility();
    	            
    	            // isRectoVerso.isSelected()
    	            Boolean isRectoVerso = true;
    	            if ( isRectoVerso ) {
    		            if ( t4File.exists()  ) {
    			            pdfMerger.addSource(t4File);
    			            pdfMerger.addSource(t4VersoFile);
    		            }
    		            
    		            if ( r1File.exists() && nform == 0  ) {
    			            pdfMerger.addSource(r1File);
    			            pdfMerger.addSource(r1VersoFile);
    		            }
    		            
    	            } else {
    		            if ( t4File.exists()  ) {
    			            pdfMerger.addSource(t4File);
    		            }
    		            
    		            if ( r1File.exists()  ) {
    			            pdfMerger.addSource(r1File);
    			            pdfMerger.addSource(r1VersoFile);
    		            }

    	            }
    	            
    	            tmp = empArray.get(j).getName();
        	    }


	        }

	        pdfMerger.setDestinationFileName(destFile.getAbsolutePath());
	        try {
	            pdfMerger.mergeDocuments( null );
	        } catch (Exception e) {
	        }


        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }

        return "";
    }

	public P_Employee getWithSin( Properties ctx, String Sin, String trxName  ) 
	{
		String sql = null;
		sql = "Select P_Employee.P_Employee_ID from P_Employee WHERE replace( sin, '-', '' ) = " + Sin;
		//
		
		P_Employee Employee = null;
		
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				Employee = P_Employee.get( ctx, rs.getInt( "P_Employee_ID"), trxName);
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"P_Employee - " + sql, e);
		}

		return Employee;
	}

	public P_Employee getWithValue( Properties ctx, String Value, String trxName  ) 
	{
		String sql = null;
		sql = "Select P_Employee.P_Employee_ID from P_Employee WHERE Value = " + DB.TO_STRING( Value );
		//
		
		P_Employee Employee = null;
		
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				Employee = P_Employee.get( ctx, rs.getInt( "P_Employee_ID"), trxName);
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"P_Employee - " + sql, e);
		}

		return Employee;
	}


    /**
     * Prints the document at its actual size. This is the recommended way to print.
     */
    private void print(PDDocument document) throws IOException, PrinterException
    {
//        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));
        job.print();
    }

    /**
     * Prints using custom PrintRequestAttribute values.
     */
    private void printWithAttributes(PDDocument document)
            throws IOException, PrinterException
    {
//        PrinterJob job = PrinterJob.getPrinterJob();    	
        job.setPageable(new PDFPageable(document));

//        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
//        attr.add( Sides.DUPLEX );
//        attr.add(new PageRanges(1, 1)); // pages 1 to 1

        job.print(attr);
    }

    /**
     * Prints with a print preview dialog.
     */
    private void printWithDialog(PDDocument document) throws IOException, PrinterException
    {
//        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

        if (job.printDialog())
        {
            job.print();
        }
    }

    /**
     * Prints with a print preview dialog and custom PrintRequestAttribute values.
     */
    private void printWithDialogAndAttributes(PDDocument document)
            throws IOException, PrinterException
    {
//        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

//        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        attr.add(new PageRanges(1, 1)); // pages 1 to 1

        if (job.printDialog(attr))
        {
            job.print(attr);
        }
    }
    
    /**
     * Prints using a custom page size and custom margins.
     */
    private static void printWithPaper(PDDocument document)
            throws IOException, PrinterException
    {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

        // define custom paper
        Paper paper = new Paper();
        paper.setSize(306, 396); // 1/72 inch
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight()); // no margins

        // custom page format
        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);
        
        // override the page format
        Book book = new Book();
        // append all pages
        book.append(new PDFPrintable(document), pageFormat, document.getNumberOfPages());
        job.setPageable(book);
        
        job.print();
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
			new EndYearPrintAvantaxPdf( true );
			
			count++;
		}
		catch (Exception e) {
			log.log(Level.SEVERE, "*ERROR EndYearImportAvantaxPdf Fail", e);
		}

		System.out.println("EndYearImportAvantaxPdf = " + count);

	}	//	main

 }
