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
package solstice.process;


import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;

import javax.activation.FileDataSource;

import org.compiere.Compiere;
import org.compiere.db.CConnection;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.DB;

import solstice.model.P_Period;


import solstice.model.P_Employee;
import solstice.model.P_Form_Employee;
import solstice.model.P_Form;
import solstice.model.P_Year;
import solstice.utils.PgiUtil;

import org.compiere.model.MAttachment;
import org.compiere.model.MOrg;
import org.compiere.model.MPInstancePara;
import org.compiere.model.MProcess;
import org.compiere.model.MPInstance;
import org.compiere.model.MSystem;




public class EndYearImportAvantaxPdf extends SvrProcess 
{
	
	private int P_Year_ID = 0;
	private int P_Form_ID = 0;
	private int P_Employee_ID = 0;
	private int P_Employer_ID = 0;
	private MProcess process;
	private String ExportFormat = "PDF";

	private static CLogger		log = CLogger.getCLogger (EndYearImportAvantaxPdf.class);

	
	public EndYearImportAvantaxPdf(  )
	{
	}
	public EndYearImportAvantaxPdf ( boolean Standalone )
	{
		try 
		{
			this.P_Year_ID = 1100133; // 1000126 = 2020
			this.P_Form_ID = 1000100 ; // 1000102;  R1 = 1000100 T4 = 1000102
			this.P_Employer_ID = 0; //1000046;
			this.doIt();
		}
		catch (Exception e) 
		{
			log.log(Level.SEVERE, "Error :", e);
		}
	}
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
			else if (name.equals("P_Form_ID"))
			{
				P_Form_ID = para[i].getParameterAsInt();
			}
			else if (name.equals("P_Employee_ID"))
			{
				P_Employee_ID = para[i].getParameterAsInt();
			}
			else if (name.equals("P_Employer_ID"))
			{
				P_Employer_ID = para[i].getParameterAsInt();
			}
			
		}
		MPInstance instance = new MPInstance(getCtx(),this.getAD_PInstance_ID(), null);
    	int process_id = instance.getAD_Process_ID();
    	process = new MProcess(getCtx(),process_id, null);
    	
    }	//	prepare
    
    protected String doIt() throws Exception
    {
    	
    	String path = PgiUtil.getSolsticeParameter(Env.getCtx(), "AvantaxPath");
    	
    	path = "C:\\Solstice\\Transferts\\T4_R1\\2025\\";
    	
		if ( path.endsWith( File.separator ) == false )
			path = path + File.separator;


    	File folder = new File(path);
    	File[] listOfFiles = folder.listFiles();
//    	String[][] empArray = new String[ listOfFiles.length ][]; 

    	String[] fileArray = new String[ listOfFiles.length ];
    	
    	Arrays.sort(listOfFiles);

		
    	for (File file : listOfFiles) {
    	
    	    if (file.isFile()) {
    	    	String value =  "";
    	    	if (file.getName().startsWith("T4") ) {
    	    		value =  file.getName().substring(5, file.getName().indexOf(".") );
    	    	}
    	    	if (file.getName().startsWith("TA") ) {
    	    		value =  file.getName().substring(5, file.getName().indexOf(".") );
    	    	}
    	    
    	    	if (file.getName().startsWith("R1") ) {
    	    		value =  file.getName().substring(5, file.getName().indexOf(".") );
    	    	}
    	    	 
    	    	
    	    	if (file.getName().startsWith("R1") || file.getName().startsWith("T4") || file.getName().startsWith("TA") ) {
    	    		
	        		P_Employee Employee = this.getWithValue(Env.getCtx(), value , null);
	        		MOrg Org = new MOrg(Env.getCtx(), Employee.getAD_Org_ID(), null);
	        		
	        		if ( Employee.isActive() == false )  {
		        		String fileName = path + "Print" + Org.getValue() + File.separator + file.getName();
		        		
		        		File fileToPrint = new File( fileName );
	        			// Move file dans le répertoire d'impression.
//	        			Files.copy(file, fileToPrint) ;
	        		}
	        		else {
		        		String fileName = path + "Actif" + Org.getValue() + File.separator + file.getName();
		        		
		        		File fileToPrint = new File( fileName );
	//        			Files.copy(file, fileToPrint) ;
	        			
	        		}
    	    	}
    	    }

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

	
    
	public static void main (String[] args)
	{
		System.out.println("EndYearImportAvantaxPdf");
		System.out.println("----------------------------------");
		//
		int count = 0;
		try
		{
			Compiere.startup(true);
		
			new EndYearImportAvantaxPdf( true );
			count++;
		}
		catch (Exception e) {
			log.log(Level.SEVERE, "*ERROR EndYearImportAvantaxPdf Fail", e);
		}

		System.out.println("EndYearImportAvantaxPdf = " + count);

	}	//	main

 }
