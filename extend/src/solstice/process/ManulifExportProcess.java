package solstice.process;
import java.io.*;

import jxl.*;

import java.util.*;
import java.util.logging.Level;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import jxl.write.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.Compiere;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import solstice.model.P_Employee;
import solstice.model.P_Period;
import solstice.utils.PgiUtil;
import jxl.format.Colour;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ManulifExportProcess extends SvrProcess
{
	int P_Period_ID;
    P_Period Period;
    String Path;
    String filename;
    
	protected void prepare() 
	{
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		//Read the parameters
		for(int i=0; i < para.length; i++)
		{ 
			paramName = para[i].getParameterName();
			if (paramName.equals("P_Period_ID"))
			{
				this.P_Period_ID = para[i].getParameterAsInt();
			}
		}
		Period = P_Period.get(getCtx(), P_Period_ID, null);
	}

	/*
		1	Organizational Customer Number
		2	Member Number
		3	Last Name
		4	First Name
		5	Middle Name
		6	Address Line 1
		7	Address Line 2
		8	Address Line 3
		9	City
		10	Province/State
		11	Postal Code
		12	Country
		13	Language
		14	Gender
		15	Social Insurance Number
		16	Birth Date
		17	Hire Date
		18	Employment Status
		19	Status Effective Date
		20	Employee Class	
		21	Province of Employment
		22	Email address
		23  Payment Group
	*/

    public void CreateHeader(String group, WritableSheet s , StringBuffer sb, String trxName) throws WriteException
    {
	    /* Format the Font */
	    WritableFont wf = new WritableFont(WritableFont.ARIAL, 
	      10, WritableFont.BOLD);
	    WritableCellFormat cf = new WritableCellFormat(wf);
	    cf.setWrap(true);
	    Colour c = Colour.GREY_25_PERCENT;
	    cf.setBackground(c);
	    cf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

	    WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
	  	WritableCellFormat cf2 = new WritableCellFormat(wf2);
	  	cf2.setWrap(true);

		for ( int l=1; l <= 23 ; l++)
		{
			s.setColumnView(l, 30);
		}
			

	    int i = 0;
	    /* Creates Label and writes date to one cell of sheet*/
	    Label l = new Label(i,0,"Organizational Customer Number",cf);
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Member Number",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Last Name",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "First Name",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Middle Name",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Address Line 1",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Address Line 2",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Address Line 3",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "City",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Province/State",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Postal Code",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Country",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Language",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Gender",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Social Insurance Number",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Birth Date",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Hire Date",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Employment Status",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Status Effective Date",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Employee Class",cf); 	
	    s.addCell(l);
	    i = i+1;
//        l = new Label(i,0, "Province of Employment",cf); 
//	    s.addCell(l);
//	    i = i+1;
        l = new Label(i,0, "Email address",cf); 
	    s.addCell(l);
	    i = i+1;
        l = new Label(i,0, "Groupe de paiement",cf); 
	    s.addCell(l);
	    i = i+1;

	    
	    
        sb.append( "Organizational Customer Number" + "\t");
        sb.append( "Member Number" + "\t");
        sb.append( "Last Name" + "\t");
        sb.append( "First Name" + "\t");
        sb.append( "Middle Name" + "\t");
        sb.append( "Address Line 1" + "\t");
        sb.append( "Address Line 2" + "\t");
        sb.append( "Address Line 3" + "\t");
        sb.append( "City" + "\t");
        sb.append( "Province/State" + "\t");
        sb.append( "Postal Code" + "\t");
        sb.append( "Country" + "\t");
        sb.append( "Language" + "\t");
        sb.append( "Gender" + "\t");
        sb.append( "Social Insurance Number" + "\t");
        sb.append( "Birth Date" + "\t");
        sb.append( "Hire Date" + "\t");
        sb.append( "Employment Status" + "\t");
        sb.append( "Status Effective Date" + "\t");
        sb.append( "Employee Class" + "\t");	
//        sb.append( "Province of Employment" + "\t");
        sb.append( "Email address\t" );
        sb.append( "Groupe de paiement" );
      	sb.append("\r\n");
		String sql = " select 103421700,                                                                                       "
				+ "        P_Employee.insuranceNumber as VALUE ,                                                                               "
				+ "        P_Employee.SURNAME,                                                                             "
				+ "        P_Employee.FIRSTNAME,                                                                           "
				+ "        '' as MiddleName,                                                                               "
				+ "        ISNULL( C_LOCATION.ADDRESS1, ''),                                                                            "
				+ "        ISNULL( C_LOCATION.ADDRESS2, ''),                                                                            "
				+ "        ISNULL( C_LOCATION.ADDRESS3, ''),                                                                            "
				+ "        C_LOCATION.CITY,                                                                                "
				+ "        C_REGION.NAME,                                                                                  "
				+ "        C_LOCATION.POSTAL,                                                                              "
				+ "        C_COUNTRY.Name,                                                                                 "
				+ "        upper(substring( P_LANGUAGE.VALUE,1,1)),                                                        "
				+ "        P_Employee.GENDER,                                                                              "
				+ "        replace( P_Employee.SIN, '-',''),                                                               "
				+ "        convert( varchar(10), P_Employee.BIRTHDATE , 112),                                                                           "
				+ "        convert( varchar(10), P_Employee.DATEHIRED , 112),                                                                           "
				+ "        case when P_LONGTERMLEAVE.Value = '09' then '03' else '01' end,                                                                          "
				+ "        convert( varchar(10), isnull( P_Employee.LongTermLeaveDate, P_Employee.DATEHIRED ) , 112),                                   "
				+ " 	   Case when CustomFieldYesNo01 = 'Y' then '17' when P_Occupation_Group.Value = '28' then '28' else '' end,                                        "
//				+ " 	   Case when AD_ORG.VALUE = '2C0E' and P_Workplace.Value = '881' then '17' else '' end,                                        "
//				+ " 	   TAXATION_REGION.NAME,                                                                         "
				+ " 	   isnull( P_Employee.EMAIL, '' ) Email,                                                         "
				+ "        P_Payment_Group.Value as Payment_Group, "
                + "        P_Employee.P_Employee_ID"
				+ " FROM P_EMPLOYEE                                                                                        "
				+ " Left outer join C_LOCATION on C_LOCATION.C_LOCATION_ID = P_EMPLOYEE.C_LOCATION_ID                      "
				+ " Left outer join P_LONGTERMLEAVE on P_LONGTERMLEAVE.P_LONGTERMLEAVE_ID = P_EMPLOYEE.P_LongTermLeave_ID  "
				+ " Left outer join C_REGION on C_REGION.C_REGION_ID = C_LOCATION.C_REGION_ID                              "
				+ " Left outer join C_REGION TAXATION_REGION on TAXATION_REGION.C_REGION_ID = P_EMPLOYEE.TAXATION_REGION_ID"
				+ " Inner join AD_ORG on AD_ORG.AD_ORG_ID = P_EMPLOYEE.AD_ORG_ID                                      "
				+ " Left outer join C_COUNTRY on C_COUNTRY.C_COUNTRY_ID = C_LOCATION.C_COUNTRY_ID                          "
				+ " Left outer join P_LANGUAGE on P_LANGUAGE.P_LANGUAGE_ID = P_EMPLOYEE.P_LANGUAGE_ID     "
				+ " Left outer join P_Workplace ON P_Workplace.P_Workplace_ID = P_Employee.P_Workplace_ID "
				//+ 2011.10.07 ne pas sélectionner les employés occationnel
				+ " inner join P_Job_Type ON P_Job_Type.P_Job_Type_ID = P_Employee.P_Job_Type_ID AND P_Job_Type.IsOccasional = 'N' "
				+ " Left outer join P_Payment_Group On P_Payment_Group.P_Payment_Group_ID = P_Employee.P_Payment_Group_ID"
				+ " Left outer join P_Occupation_Group ON P_Occupation_Group.P_Occupation_Group_ID = P_Employee.P_Occupation_Group_ID "
//				2012.05.04
				+ " Where ( P_Employee.isActive = 'Y' or ( P_Employee.isActive = 'N' and CustomFieldYesNo02 = 'N' )" 
			
			//			"P_Employee.DateLayoff between " + DB.TO_DATE(Period.getStartDate()) + " and " + DB.TO_DATE( Period.getEndDate())
				
//			+ " Where ( P_Employee.isActive = 'Y' or P_Employee.DateLayoff between " + DB.TO_DATE(Period.getStartDate()) + " and " + DB.TO_DATE( Period.getEndDate())
	
				+ " ) AND P_Employee.P_Workplace_ID not in ( 1001338 ) " // Board of Director 880				
//				+ " and AD_ORG.VALUE = '2C0E'  " 
				;
//				+ " WHERE EXISTS( SELECT 1 FROM P_Payment, P_Payment_Deduction "
//				+ "              where P_Payment.P_Payment_ID = P_Payment_Deduction.P_Payment_ID and P_Payment.P_Employee_ID = P_Employee.P_Employee_ID " 
//				+ "                and P_Payment.P_Period_Id = " + Period.getP_Period_ID() 
//				+ "                and P_Deduction_ID in ( SELECT P_DEDUCTION_ID FROM P_DEDUCTION WHERE P_Deduction.VALUE in ( '02','59', '50','56', '64', '68', '40', '42' ))) " 
				;
				
	if ( group.equals("CHL")) {
		sql = sql + " AND P_Employee.P_Payment_Group_ID = 1000012 ";
	}
	else {
		sql = sql + " AND P_Employee.P_Payment_Group_ID = 1000015 ";
	}
	PreparedStatement pstmt = null;
	try
	{
		pstmt = DB.prepareStatement(sql, trxName);
		ResultSet rs = pstmt.executeQuery();
		int j = 0; 
		P_Employee  Employee;
		while (rs.next())
		{
			Employee = P_Employee.get( Env.getCtx(), rs.getInt( "P_Employee_ID"), trxName);
			if ( Employee.isActive() == false )
			{
				//Indique que l'employé inactif est transféré à Manulift
				Employee.setCustomFieldYesNo02( true );
				Employee.save();
			}
			j = j + 1;
			for ( i=1; i <= 22 ; i++)
			{
			    l = new Label(i-1,j,rs.getString(i),cf2);
			    s.addCell(l);

//			    if (i == 20 )
//			    	log.log (Level.INFO, "Test");
			    
			    if (i != 22 )
			    	sb.append( rs.getString(i) + "\t");    // 1 organizational Customer Number
			    else
			    	sb.append( rs.getString(i) );    // 1 organizational Customer Number

			}

			/*			
	        sb.append( rs.getString(1) + "\t");    // 1 organizational Customer Number                 
	        sb.append( rs.getString(2) + "\t" ) ;  // 2	Member Number          
	        sb.append( rs.getString(3) + "\t" ) ;  // 3	Last Name              
	        sb.append( rs.getString(4) + "\t" ) ;  // 4	First Name             
	        sb.append( rs.getString(5) + "\t" ) ;  // 5	Middle Name            
	        sb.append( rs.getString(6) + "\t" ) ;  // 6	Address Line 1         
	        sb.append( rs.getString(7) + "\t" ) ;  // 7	Address Line 2         
	        sb.append( rs.getString(8) + "\t" ) ;  // 8	Address Line 3         
	        sb.append( rs.getString(9) + "\t" ) ;  // 9	City                   
	        sb.append( rs.getString(10) + "\t" ) ;  // 10	Province/State         
	        sb.append( rs.getString(11) + "\t" ) ;  // 11	Postal Code            
	        sb.append( rs.getString(12) + "\t" ) ;  // 12	Country                
	        sb.append( rs.getString(13) + "\t" ) ;  // 13	Language               
	        sb.append( rs.getString(14) + "\t" ) ;  // 14	Gender                 
	        sb.append( rs.getString(15) + "\t" ) ;  // 15	Social Insurance Number
	        sb.append( rs.getString(16).substring(0,10) + "\t" ) ;  // 16	Birth Date             
	        sb.append( rs.getString(17).substring(0,10) + "\t" ) ;  // 17	Hire Date              
	        sb.append( rs.getString(18) + "\t" ) ;  // 18	Employment Status      
	        sb.append( rs.getString(19).substring(0,10) + "\t" ) ;  // 19	Status Effective Date  
	        sb.append( rs.getString(20) + "\t" ) ;  // 20	Employee Class	       
	        sb.append( rs.getString(21) + "\t" ) ;  // 21	Province of Employment 
	        sb.append( rs.getString(22) + "\t" ) ;  // 22	Email address
*/	                  
	      	sb.append("\r\n");
		}
		rs.close();
		pstmt.close();
		pstmt = null;

	    l = new Label(0,j+1,"103421700",cf2);
	    s.addCell(l);
	    l = new Label(1,j+1,"Totals",cf2);
	    s.addCell(l);
	    l = new Label(2,j+1, String.valueOf(j),cf2);
	    s.addCell(l);

  		sb.append( "103421700" + "\t" + "Totals"  + "\t" + j );
      	sb.append("\r\n");

	}
	catch (Exception e)
	{
			System.err.println("ManulifExportProcess - " + e);
	}
    }

    public void CreateHeader( StringBuffer sb, String trxName) throws WriteException
    {
	    
        sb.append( "Organizational Customer Number" + "\t");
        sb.append( "Member Number" + "\t");
        sb.append( "Last Name" + "\t");
        sb.append( "First Name" + "\t");
        sb.append( "Middle Name" + "\t");
        sb.append( "Address Line 1" + "\t");
        sb.append( "Address Line 2" + "\t");
        sb.append( "Address Line 3" + "\t");
        sb.append( "City" + "\t");
        sb.append( "Province/State" + "\t");
        sb.append( "Postal Code" + "\t");
        sb.append( "Country" + "\t");
        sb.append( "Language" + "\t");
        sb.append( "Gender" + "\t");
        sb.append( "Social Insurance Number" + "\t");
        sb.append( "Birth Date" + "\t");
        sb.append( "Hire Date" + "\t");
        sb.append( "Employment Status" + "\t");
        sb.append( "Status Effective Date" + "\t");
        sb.append( "Employee Class" + "\t");	
//        sb.append( "Province of Employment" + "\t");
        sb.append( "Email address\t" );
        sb.append( "Groupe de paiement" );
      	sb.append("\r\n");
		String sql = " select 103421700,                                                                                       "
				+ "        P_Employee.insuranceNumber as VALUE ,                                                                               "
				+ "        P_Employee.SURNAME,                                                                             "
				+ "        P_Employee.FIRSTNAME,                                                                           "
				+ "        '' as MiddleName,                                                                               "
				+ "        ISNULL( C_LOCATION.ADDRESS1, ''),                                                                            "
				+ "        ISNULL( C_LOCATION.ADDRESS2, ''),                                                                            "
				+ "        ISNULL( C_LOCATION.ADDRESS3, ''),                                                                            "
				+ "        C_LOCATION.CITY,                                                                                "
				+ "        C_REGION.NAME,                                                                                  "
				+ "        C_LOCATION.POSTAL,                                                                              "
				+ "        C_COUNTRY.Name,                                                                                 "
				+ "        upper(substring( P_LANGUAGE.VALUE,1,1)),                                                        "
				+ "        P_Employee.GENDER,                                                                              "
				+ "        replace( P_Employee.SIN, '-',''),                                                               "
				+ "        convert( varchar(10), P_Employee.BIRTHDATE , 112),                                                                           "
				+ "        convert( varchar(10), P_Employee.DATEHIRED , 112),                                                                           "
				+ "        case when P_LONGTERMLEAVE.Value = '09' then '03' else '01' end,                                                                          "
				+ "        convert( varchar(10), isnull( P_Employee.LongTermLeaveDate, P_Employee.DATEHIRED ) , 112),                                   "
				+ " 	   Case when CustomFieldYesNo01 = 'Y' then '17' when P_Occupation_Group.Value = '28' then '28' else '' end,                                        "
//				+ " 	   Case when CustomFieldYesNo01 = 'Y' then '17' else '' end,                                        "
//				+ " 	   Case when AD_ORG.VALUE = '2C0E' and P_Workplace.Value = '881' then '17' else '' end,                                        "
//				+ " 	   TAXATION_REGION.NAME,                                                                         "
				+ " 	   isnull( P_Employee.EMAIL, '' ) Email,                                                         "
				+ "        P_Payment_Group.Value as Payment_Group, "
                + "        P_Employee.P_Employee_ID"
				+ " FROM P_EMPLOYEE                                                                                        "
				+ " Left outer join C_LOCATION on C_LOCATION.C_LOCATION_ID = P_EMPLOYEE.C_LOCATION_ID                      "
				+ " Left outer join P_LONGTERMLEAVE on P_LONGTERMLEAVE.P_LONGTERMLEAVE_ID = P_EMPLOYEE.P_LongTermLeave_ID  "
				+ " Left outer join C_REGION on C_REGION.C_REGION_ID = C_LOCATION.C_REGION_ID                              "
				+ " Left outer join C_REGION TAXATION_REGION on TAXATION_REGION.C_REGION_ID = P_EMPLOYEE.TAXATION_REGION_ID"
				+ " Inner join AD_ORG on AD_ORG.AD_ORG_ID = P_EMPLOYEE.AD_ORG_ID                                      "
				+ " Left outer join C_COUNTRY on C_COUNTRY.C_COUNTRY_ID = C_LOCATION.C_COUNTRY_ID                          "
				+ " Left outer join P_LANGUAGE on P_LANGUAGE.P_LANGUAGE_ID = P_EMPLOYEE.P_LANGUAGE_ID     "
				+ " Left outer join P_Workplace ON P_Workplace.P_Workplace_ID = P_Employee.P_Workplace_ID "
				//+ 2011.10.07 ne pas sélectionner les employés occationnel
				+ " inner join P_Job_Type ON P_Job_Type.P_Job_Type_ID = P_Employee.P_Job_Type_ID AND P_Job_Type.IsOccasional = 'N' "
				+ " Left outer join P_Payment_Group On P_Payment_Group.P_Payment_Group_ID = P_Employee.P_Payment_Group_ID"
				+ " Left outer join P_Occupation_Group ON P_Occupation_Group.P_Occupation_Group_ID = P_Employee.P_Occupation_Group_ID "
//				2012.05.04
				+ " Where ( P_Employee.isActive = 'Y' or ( P_Employee.isActive = 'N' and CustomFieldYesNo02 = 'N' )" 
			
			//			"P_Employee.DateLayoff between " + DB.TO_DATE(Period.getStartDate()) + " and " + DB.TO_DATE( Period.getEndDate())
				
//			+ " Where ( P_Employee.isActive = 'Y' or P_Employee.DateLayoff between " + DB.TO_DATE(Period.getStartDate()) + " and " + DB.TO_DATE( Period.getEndDate())
	
				+ " ) AND P_Employee.P_Workplace_ID not in ( 1001338 ) " // Board of Director 880				
//				+ " and AD_ORG.VALUE = '2C0E'  " 
				;
//				+ " WHERE EXISTS( SELECT 1 FROM P_Payment, P_Payment_Deduction "
//				+ "              where P_Payment.P_Payment_ID = P_Payment_Deduction.P_Payment_ID and P_Payment.P_Employee_ID = P_Employee.P_Employee_ID " 
//				+ "                and P_Payment.P_Period_Id = " + Period.getP_Period_ID() 
//				+ "                and P_Deduction_ID in ( SELECT P_DEDUCTION_ID FROM P_DEDUCTION WHERE P_Deduction.VALUE in ( '02','59', '50','56', '64', '68', '40', '42' ))) " 
				;
				
	PreparedStatement pstmt = null;
	try
	{
		pstmt = DB.prepareStatement(sql, trxName);
		ResultSet rs = pstmt.executeQuery();
		int j = 0; 
		P_Employee  Employee;
		while (rs.next())
		{
			Employee = P_Employee.get( Env.getCtx(), rs.getInt( "P_Employee_ID"), trxName);
			if ( Employee.isActive() == false )
			{
				//Indique que l'employé inactif est transféré à Manulift
				Employee.setCustomFieldYesNo02( true );
				Employee.save();
			}
			j = j + 1;
			for ( int i=1; i <= 22 ; i++)
			{
			    
			    if (i != 22 )
			    	sb.append( rs.getString(i) + "\t");    // 1 organizational Customer Number
			    else
			    	sb.append( rs.getString(i) );    // 1 organizational Customer Number

			}

	      	sb.append("\r\n");
		}
		rs.close();
		pstmt.close();
		pstmt = null;

  		sb.append( "103421700" + "\t" + "Totals"  + "\t" + j );
      	sb.append("\r\n");

	}
	catch (Exception e)
	{
			System.err.println("ManulifExportProcess - " + e);
	}
    }
	
	private void CreateDemographicFile( WritableSheet s2, String group )
	{
	    try
	    {
 
	    	StringBuffer CreateFile = new StringBuffer();
	        String trxName = null; //	Trx.createTrxName();
	    	CreateHeader( group, s2, CreateFile, trxName);
//	    	WriteToFile( group, CreateFile, trxName);

	    	//2017-08-09 global file
			if ( group.equals("CHL")) {
				CreateFile =  new StringBuffer();
				CreateHeader( CreateFile, trxName);
				WriteToFile( CreateFile, trxName);
				
			}
	    }
     	catch (Exception e)
	    {
			System.err.println("ManulifExportProcess - " + e);
	    }
		
	}

	private void CreateDemographicFile( )
	{
	    try
	    {
 
	    	StringBuffer CreateFile = new StringBuffer();
	        String trxName = null; //	Trx.createTrxName();
	    	CreateHeader( CreateFile, trxName);
	    	WriteToFile( CreateFile, trxName);

	    }
     	catch (Exception e)
	    {
			System.err.println("ManulifExportProcess - " + e);
	    }
		
	}

	
	static DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	   public void WriteToFile( String group, StringBuffer sb, String trxName)
	    {
		   

	    	Path = PgiUtil.getSolsticeParameter(getCtx(), "TransfertPathManulif");
	    	if ( ! Path.endsWith("\\") )
	    		Path = Path + "\\";

	    	Date date = new Date();
			String currentDate = dateFormat.format( date );
			String fileNameOut = "103421700.10001080.1.0." + group + currentDate + ".DEMOFTP.TXT" ;

	    	filename =  Path + fileNameOut;

//	    	filename =  Path + "ManulifDemographicFile.txt";
	  	    File aFile = null;
	  	    aFile = new File( filename ); 
	        FileOutputStream out; // declare a file output object
	        PrintStream p; // declare a print stream object
	        try
	        {
	              out = new FileOutputStream(aFile);
	              // Connect print stream to the output stream
	              p = new PrintStream( out );

	              p.println (sb.toString());
	              System.out.println(sb.toString());
	              p.close();
	        }
	        catch (Exception e)
	        {
	        	log.log (Level.SEVERE, "Error writing to file :", e);
	        }
	   }
	 
	   public void WriteToFile( StringBuffer sb, String trxName)
	    {
		   

	    	Path = PgiUtil.getSolsticeParameter(getCtx(), "TransfertPathManulif");
	    	if ( ! Path.endsWith("\\") )
	    		Path = Path + "\\";

	    	Date date = new Date();
			String currentDate = dateFormat.format( date );
			String fileNameOut = "103421700.10001080.1.0." + currentDate + ".DEMOFTP.TXT" ;

	    	filename =  Path + fileNameOut;

//	    	filename =  Path + "ManulifDemographicFile.txt";
	  	    File aFile = null;
	  	    aFile = new File( filename ); 
	        FileOutputStream out; // declare a file output object
	        PrintStream p; // declare a print stream object
	        try
	        {
	              out = new FileOutputStream(aFile);
	              // Connect print stream to the output stream
	              p = new PrintStream( out );

	              p.println (sb.toString());
	              System.out.println(sb.toString());
	              p.close();
	        }
	        catch (Exception e)
	        {
	        	log.log (Level.SEVERE, "Error writing to file :", e);
	        }
	   }

	private void CreateExcelFile( String group )
	{
		int exported = 0;
	    try
	    {
     	  String Path = PgiUtil.getSolsticeParameter(getCtx(), "TransfertPathManulif");
	    	if ( ! Path.endsWith("\\") )
	    		Path = Path + "\\";

     	  filename =  Path + "Manulif " + group + " Contribution " + Period.getName() + ".xls";
	  	  System.out.println("filename = " + filename );
	      WorkbookSettings ws = new WorkbookSettings();
	      ws.setLocale(new Locale("en", "EN"));
	      WritableWorkbook workbook = 
	      Workbook.createWorkbook(new File(filename), ws);
	      WritableSheet s = workbook.createSheet("RSP Contribution Details", 1);
	      WritableSheet s1 = workbook.createSheet("RPP Contribution Details", 2);
//	      WritableSheet s2 = workbook.createSheet("ESPP", 3);
//	      WritableSheet s3 = workbook.createSheet("ESPP RRSP", 4);
	      WritableSheet s4 = workbook.createSheet("Demographic File", 5);
	      // set la largeur des colonnes
	      s.setColumnView(0, 30);
	      s.setColumnView(1, 20);
	      s.setColumnView(2, 20);
	      s.setColumnView(3, 20);
	      s.setColumnView(4, 20);
	      s1.setColumnView(0, 30);
	      s1.setColumnView(1, 20);
	      s1.setColumnView(2, 20);
	      s1.setColumnView(3, 20);
	      s1.setColumnView(4, 20);
/*	      
	      s2.setColumnView(0, 30);
	      s2.setColumnView(1, 20);
	      s2.setColumnView(2, 20);
	      s2.setColumnView(3, 20);
	      s2.setColumnView(4, 20);
	      s3.setColumnView(0, 30);
	      s3.setColumnView(1, 20);
	      s3.setColumnView(2, 20);
	      s3.setColumnView(3, 20);
	      s3.setColumnView(4, 20);
*/
	      exported = writeDataSheet(s, "RSP", group);
	      exported += writeDataSheet(s1, "RPP", group);
/*	      exported = writeDataSheet(s2, "ESPP");
	      exported = writeDataSheet(s3, "RRSP");
*/	      
	  	  CreateDemographicFile( s4, group );

	      workbook.write();
	      workbook.close();      

	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	    catch (WriteException e)
	    {
	      e.printStackTrace();
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	}


	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception 
	{
		System.out.println("ManulifExportProcess   $Revision: 1.0 $");
		System.out.println("----------------------------------");
		
  	    
  	    CreateExcelFile( "CHL");
  	    CreateExcelFile( "ACASTA");
		

	    return " Le processus à créer les fichiers dans le répertoire : " + Path;
	}
	
	  private int writeDataSheet(WritableSheet s, String type, String group) 
	    throws WriteException
	  {
	    /* Format the Font */
	    WritableFont wf = new WritableFont(WritableFont.ARIAL, 
	      10, WritableFont.BOLD);
	    WritableCellFormat cf = new WritableCellFormat(wf);
	    cf.setWrap(true);
	    Colour c = Colour.GREY_25_PERCENT;
	    cf.setBackground(c);
	    cf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

	    /* Creates Label and writes date to one cell of sheet*/
	    Label l = new Label(0,0,"Plan Name",cf);
	    s.addCell(l);
	    l = new Label(0,1,"Division",cf);
	    s.addCell(l);
	    l = new Label(0,2,"Policy Number",cf);
	    s.addCell(l);
	    l = new Label(0,3,"Total Amount Being Contributed",cf);
	    s.addCell(l);
	    l = new Label(0,4,"Contribution Period Ending Date (YYYYMMMDD)",cf);
	    s.addCell(l);
	    l = new Label(0,5,"Payment by PreAuthorized Debit",cf);
	    s.addCell(l);
	    l = new Label(0,6,"Payment by Wire Transfer",cf);
	    s.addCell(l);
	    l = new Label(0,7,"Payment by Cheque",cf);
	    s.addCell(l);
	    l = new Label(0,8,"Payment by Variance",cf);
	    s.addCell(l);
	    l = new Label(0,9,"Payment by Forfeiture",cf);
	    s.addCell(l);
	    l = new Label(0,10,"Member Number",cf);
	    s.addCell(l);
	    l = new Label(1,10,"Member Last Name",cf);
	    s.addCell(l);
	    l = new Label(2,10,"Member First Name",cf);
	    s.addCell(l);
	    if ( type.equals("RPP"))
	    	l = new Label(3,10,"Employee Contributions",cf);
	    else
	    	l = new Label(3,10,"Employee Voluntary",cf);
	    s.addCell(l);
	    l = new Label(4,10,"Company Contributions",cf);
	    s.addCell(l);

	    WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
	  	WritableCellFormat cf2 = new WritableCellFormat(wf2);
	  	cf2.setWrap(true);
		if ( group.equals("CHL")) {
			l = new Label(1,0,"Canadian Helicopters Limited",cf2);
		}
		else {
			l = new Label(1,0,"Acasta HeliFlight Inc.",cf2);
		}
	    s.addCell(l);
	    if ( type.equals("RPP"))
	    	l = new Label(1,2,"10001080",cf2);
	    else
	    	l = new Label(1,2,"20003080",cf2);
	    s.addCell(l);

	    WritableCellFormat i1 = new WritableCellFormat(NumberFormats.FLOAT);
	    
	    String PeriodDate = getStrDate( Period.getEndDate() )
	    ;
	    
	    l = new Label(1,4, PeriodDate , cf2);
	    s.addCell(l);

	    /*
	     * RPP inclus le code 02 portion EE et portion ER et le code 59 portion EE et ER. 
	     * RSP inclus le code 50 portion EE, code 56 portion EE, code 54 portion EE et ER et le code 68 portion EE.
	     */
  	    int i = 10;
		String sql = "select p_employee.insuranceNumber as VALUE, P_Employee.Surname, P_Employee.FIRSTNAME, SUM( P_Payment_Deduction.employee_part) employee_part, SUM( P_Payment_Deduction.employer_part) employer_part "
                   + "From P_EMPLOYEE "
                   + "inner join P_PAYMENT on P_Employee.P_EMPLOYEE_ID = P_PAYMENT.P_EMPLOYEE_ID "
                   + "inner join P_PAYMENT_DEDUCTION on P_PAYMENT.P_PAYMENT_ID = P_PAYMENT_DEDUCTION.P_PAYMENT_ID "
                   + "inner join P_Deduction on P_Deduction.P_DEDUCTION_ID = P_PAYMENT_DEDUCTION.P_Deduction_ID ";

		if ( type.equals("RPP"))
	    	sql += "WHERE P_Deduction.VALUE in ( '02','59') ";
	    else  if ( type.equals("RSP"))
	    	sql += "WHERE P_Deduction.VALUE in ( '50','56', '64', '68', '76') ";
	    else  if ( type.equals("ESPP"))
	    	sql += "WHERE P_Deduction.VALUE in ( '40') ";
	    else  if ( type.equals("RRSP"))
	    	sql += "WHERE P_Deduction.VALUE in ( '42') ";
	    sql += " AND P_Payment.P_Period_ID = " + Period.getP_Period_ID();
	    
		if ( group.equals("CHL")) {
			sql = sql + " AND P_Employee.P_Payment_Group_ID = 1000012";
		}
		else {
			sql = sql + " AND P_Employee.P_Payment_Group_ID = 1000015";
		}
	    
		sql += "GROUP BY p_employee.insuranceNumber, P_Employee.Surname, P_Employee.FIRSTNAME "
                   ;
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			Double Total = new Double( 0 );
			while (rs.next())
			{
			    i = i + 1;
			    l = new Label(0,i,rs.getString("VALUE"),cf2);
			    s.addCell(l);
			    l = new Label(1,i,rs.getString("Surname"),cf2);
			    s.addCell(l);
			    l = new Label(2,i,rs.getString("FIRSTNAME"),cf2);
			    s.addCell(l);
			    Number nI2 = new Number(3,i, rs.getDouble("employee_part") ,i1);
			    s.addCell(nI2);

			    Number nI3 = new Number(4,i, rs.getDouble("employer_part") ,i1);
			    s.addCell(nI3);
				Total = Total + rs.getDouble("employee_part")  + rs.getDouble("employer_part") ;

			}
			
//			2012.05.16
			Number nI4 = new Number(1,3, Total ,i1);
		    s.addCell(nI4);

			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
				System.err.println("ManulifExportProcess - " + e);
		}

		StringBuffer buf = new StringBuffer();
		buf.append("SUM(D12:D" + (i+1) +") +" + "SUM(E12:E" + (i+1) +")");
// Si le excel doit être en Francais il faut utilisé le formule SOMME		
	    Formula f = new Formula(1,3, buf.toString(), i1 );
//2012.05.14	    s.addCell(f);
//	  la formule ce calcul uniquement lorsqu'on ouvre le fichier excel, pour controurné ce problème on calcul maintenant le total	    
	    
//    	l = new Label(1,4,"=SUM(D12:E" + i + ")",cf2);
//	    s.addCell(l);

		return i;
	  }

	  public static String getStrDate(Timestamp day) 
	  {
		  Calendar c = TimeUtil.getCalendar(day);
		  int m = c.get(Calendar.MONTH) ;
		  String m2 = "";
		  switch ( m )
		  {
		  	case Calendar.JANUARY   : m2 = "JAN";
		  	break;
		  	case Calendar.FEBRUARY  : m2 = "FEB";
		  	break;
		  	case Calendar.MARCH     : m2 = "MAR";
		  	break;
		  	case Calendar.APRIL     : m2 = "APR";
		  	break;
		  	case Calendar.MAY       : m2 = "MAY";
		  	break;
		  	case Calendar.JUNE      : m2 = "JUN";
		  	break;
		  	case Calendar.JULY      : m2 = "JUL";
		  	break;
		  	case Calendar.AUGUST    : m2 = "AUG";
		  	break;
		  	case Calendar.SEPTEMBER : m2 = "SEP";
		  	break;
		  	case Calendar.OCTOBER   : m2 = "OCT";
		  	break;
		  	case Calendar.NOVEMBER  : m2 = "NOV";
		  	break;
		  	case Calendar.DECEMBER  : m2 = "DEC";
		  	break;
		  
		  }
		  
		  int d = c.get(Calendar.DAY_OF_MONTH);
		  String mm = Integer.toString(m);
		  String dd = Integer.toString(d);
//		  return "" + c.get(Calendar.YEAR) + (m < 10 ? "0" + mm : mm) +
//		      (d < 10 ? "0" + dd : dd);
		  return "" + c.get(Calendar.YEAR) + m2 +
	      (d < 10 ? "0" + dd : dd);
		}

		Properties ctx = Env.getCtx();

	  public ManulifExportProcess()
	  {
/*			Env.setContext( ctx, "#AD_Client_ID", 1000004);
			Env.setContext( ctx, "#AD_Org_ID", 1000001);

//		    P_Period_ID = 1002032;
			Period = P_Period.get( Env.getCtx(), P_Period_ID, null);
//			Period = P_Period.getOpenPeriod(Env.getCtx(), null);
		    try
		    {
			    doIt();
		    }
			catch (Exception e)
			{
				System.err.println("ManulifExportProcess - " + e);
			}
*/	 
	  }
		/**************************************************************************
		 * 	Manulif export Process
		 */
		public static void main (String[] args)
		{
			//
			Compiere.startup(true);
			int count = 0;
			new ManulifExportProcess();
			count++;
			System.out.println("End" );

		}

}
