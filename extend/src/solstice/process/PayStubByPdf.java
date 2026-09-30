package solstice.process;


import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.FileDataSource;

import org.compiere.Compiere;
import org.compiere.db.CConnection;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.DB;

import com.crystaldecisions.sdk.occa.report.application.OpenReportOptions;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.application.DatabaseController;
import com.crystaldecisions.sdk.occa.report.application.SubreportController;
import com.crystaldecisions.sdk.occa.report.data.*;
import com.crystaldecisions.sdk.occa.report.exportoptions.ExportOptions;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.*;
import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;


import solstice.model.P_Department;
import solstice.model.P_Employee;
import solstice.model.P_Notice_Bank_Deposit;
import solstice.model.P_Payment;
import solstice.model.P_Period;
import solstice.model.P_Statement_Earning;

import solstice.model.P_Year;
import solstice.utils.PgiUtil;

import org.compiere.model.MActivity;
import org.compiere.model.MCountry;
import org.compiere.model.MLocation;
import org.compiere.model.MOrg;
import org.compiere.model.MProcess;
import org.compiere.model.MPInstance;
import org.compiere.model.MRegion;
import org.compiere.model.MUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class PayStubByPdf extends SvrProcess {

	
	
	private String trxName = null;
	private int Org_ID=0;
	
	private int P_Period_ID=0;
	private int P_Distribution_ID=0;
	private int P_Distribution_Booklet_ID=0;
	private int P_Payment_Group_ID=0;
	private int P_Employee_ID=0;
	private boolean Rework=false;
	private int m_inserted = 0;

	
//	private String ExportFormat = "PDF";
	private String ExportFormat = "MSWord";
	private String reportName;
	private MProcess process;
	private String P_Langue = "FR";
    private ReportClientDocument reportClientDoc = null;

	private final String TABLE_NAME_QUALIFIER = "solstice.dbo.";
//	private final String DB_PORT = ",50000";

	//Obtenir de compiere les informations de connection des rapports
    private String databaseName = CConnection.get().getDbName(); 
    private String databaseServer = CConnection.get().getDbHost(); // + DB_PORT;
    private String userName = CConnection.get().getDbUid(); 
    private String password = CConnection.get().getDbPwd();

    private String reportNameFullPath;

	protected void prepare()
    {
		MPInstance instance = new MPInstance( getCtx(),this.getAD_PInstance_ID(), null);
    	int process_id = instance.getAD_Process_ID();
    	process = new MProcess(getCtx(),process_id, null);

//    	reportName = process.getValue() + ".rpt";

    	reportName = "lstAvisDepotBancaire_PDF.rpt";
    	
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		
		//Read the parameters
		for(int i=0; i < para.length; i++){
			paramName = para[i].getParameterName();
			if (paramName.equals("P_Period_ID")){
				this.P_Period_ID = para[i].getParameterAsInt();
			}
			else if (paramName.equals("AD_Org_ID"))
			{
				Org_ID = para[i].getParameterAsInt();
			}
			else if (paramName.equals("P_Distribution_ID")){
				this.P_Distribution_ID = para[i].getParameterAsInt();
			}
			else if (paramName.equals("P_Distribution_Booklet_ID")){
				this.P_Distribution_Booklet_ID = para[i].getParameterAsInt();
			}
			else if (paramName.equals("P_Employee_ID")){
				this.P_Employee_ID = para[i].getParameterAsInt();
			}
			else if (paramName.equals("Rework")){
				this.Rework = "Y".equals( para[i].getParameter());
			}			
			else if (paramName.equals("P_Payment_Group_ID")){
				this.P_Payment_Group_ID = para[i].getParameterAsInt();
			}

		}

    	String reportFilePath =  PgiUtil.getSolsticeParameter(Env.getCtx(), "CrystalReportShareDirectory01" );

        if ( ! reportFilePath.endsWith("\\"))
        	reportFilePath = reportFilePath + "\\";
        
        reportNameFullPath = reportFilePath + reportName ; //+ ".rpt";
       	
		File file = new File(reportNameFullPath);

		//Path 2
		//search report file custom for the client on the second path
		if (! file.exists() )
		{
	       reportFilePath = PgiUtil.getSolsticeParameter(Env.getCtx(), "CrystalReportShareDirectory02");
	       if ( ! reportFilePath.endsWith("\\"))
	        	reportFilePath = reportFilePath + "\\";

	       reportNameFullPath = reportFilePath + reportName ; //+ ".rpt";
	        
			file = new File(reportNameFullPath);
			// Path 3
			//search report file custom for the client on the path 3
			if (! file.exists() )
			{
		       reportFilePath = PgiUtil.getSolsticeParameter(Env.getCtx(), "CrystalReportShareDirectory03");
		        if ( ! reportFilePath.endsWith("\\"))
		        	reportFilePath = reportFilePath + "\\";

		        reportNameFullPath = reportFilePath + reportName; // + ".rpt";
	        }
        }
    }
	
//	private 
	
	// private P_Period Period;
/*
	protected String doIt() throws Exception
    {
        try
        {
		String EXPORT_LOC =  "C:\\Solstice\\PayStub\\2016-04\\"; // PgiUtil.getSolsticeParameter(getCtx(), "PayStubPDFPath");

		SendFileSFTP( EXPORT_LOC,  "D95AE66B4AC0F6105B0B8ECDB9892D44_2C0E_2016-04.zip");
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
        return "";
    }
  */ 
	private String EXPORT_LOC ;
	private String scriptFileName;
	private File scriptFile;
	
	protected String doIt() throws Exception
	{
		
    	StringBuffer xmlStr = new StringBuffer( "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");

    	xmlStr = new StringBuffer( "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");

    	P_Period Period = P_Period.get( getCtx(), P_Period_ID, trxName);
      	
    	String sql = "Select * from P_Statement_Earning Where grossEarningsPeriod <> 0 AND P_Period_ID = " + P_Period_ID
//    			   + " AND P_Employee_ID IN ( select P_Employee_ID from P_Employee where PrintStatementEarning = 'N' ) "
    			;
    	

		if ( this.Org_ID != 0 )
			sql +=" AND AD_Org_ID = " + Org_ID;
	    
		if ( this.P_Distribution_Booklet_ID !=0)
			sql +=" AND P_Employee_id in ( select P_Employee_ID from P_Employee where P_DISTRIBUTION_BOOKLET_ID = " + this.P_Distribution_Booklet_ID + " )";
		if ( this.P_Distribution_ID !=0)
			sql +=" AND P_DISTRIBUTION_ID=" + this.P_Distribution_ID;
		if ( this.P_Employee_ID !=0)
			sql +=" AND P_EMPLOYEE_ID="+ this.P_Employee_ID;
		if ( this.P_Payment_Group_ID !=0)
			sql +=" AND P_Payment_Group_ID="+ this.P_Payment_Group_ID;

	
		//Seulement les employés actif et avec un utilisateur.
//		sql +=" AND P_Employee_id in ( select P_Employee_ID from P_Employee where Isactive = 'Y' and UserCode is not null )";

    	
    	PreparedStatement stmt = DB.prepareStatement(sql, null);
        try
        {
        	P_Payment Payment = null;
        	
			EXPORT_LOC =  PgiUtil.getSolsticeParameter(getCtx(), "CassiniwebPDFPath");
		
			EXPORT_LOC = EXPORT_LOC + Period.getName() + File.separator;
			File dir = new File(EXPORT_LOC);
			if (!dir.exists())
				dir.mkdir();
			dir = new File(EXPORT_LOC);
			if (!dir.exists())
			{
				System.out.println("Cannot create directory " + EXPORT_LOC);
				System.exit(1);
			}

			scriptFileName = EXPORT_LOC + Period.getName() + ".txt";
			scriptFile = new File( scriptFileName );
			// if file doesnt exists, then create it
	        if (!scriptFile.exists()) {
	        	scriptFile.createNewFile();
	        }
	        
	        FileWriter fw = new FileWriter(scriptFile.getAbsoluteFile(), false);
	        BufferedWriter bw = new BufferedWriter(fw);
	        // write in file
	    	String ftpConnection = "open sftp://" + getUserName() + ":" + getPassword() + "@" + getIP_Address();
//	        bw.write( "open sftp://canadianhelicopters:caHe2798!gT67sR3@canadianhelicopters.cassiniweb.com:1122" + "\r\n");
	        bw.write( ftpConnection + "\r\n");
	        bw.close();

			
			xmlStr = xmlStr.append( "<titan>");
			xmlStr = xmlStr.append( getCompanyBranchDeptInfo() ); 
					

	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
					P_Employee Employee = P_Employee.get( getCtx(), rs.getInt("P_Employee_ID"), null);
					
					MUser User = null;
					if (  Employee.getAD_User_ID() != 0 )
						User = new MUser( getCtx(), Employee.getAD_User_ID(), null);
					
					MLocation Location = MLocation.get( getCtx(), Employee.getC_Location_ID(), null);
					MRegion Region = new MRegion( getCtx(), Location.getC_Region_ID(), null);
					MCountry Country = new MCountry( getCtx(), Location.getC_Country_ID(), null);
					
					MActivity Activity = new MActivity( getCtx(), Employee.getC_Activity_ID(), null);
					P_Department Department = P_Department.get( getCtx(), Employee.getP_Department_ID(), null);
					MOrg Org = new MOrg( getCtx(), Employee.getAD_Org_ID(), null);
					
					xmlStr = xmlStr.append( "<employee code=\"" + Employee.getValue() + "\">");
					addElement( xmlStr, "employee_id", rs.getString("P_Employee_ID") );
					addElement( xmlStr, "employee_code" , Employee.getValue() ) ;
					if ( Employee.isActive() )
						addElement( xmlStr, "employee_is_active" , "1" ) ;
					else
						addElement( xmlStr, "employee_is_active" , "0" ) ;
					
					
					addElement( xmlStr, "employee_first_name" , Employee.getFirstName() );
					addElement( xmlStr, "employee_last_name"  , Employee.getSurname() );
					addElement( xmlStr, "employee_sex" ,  Employee.getGender() );
					if ( Employee.getP_Language_ID() == 127) {
						addElement( xmlStr, "employee_language" , "1" );
					}
					else {
						addElement( xmlStr, "employee_language" , "2" );
					}	
					addElement( xmlStr, "employee_prefix" ,  null );
					addElement( xmlStr, "company_id" , String.valueOf(Employee.getAD_Org_ID()) );
					addElement( xmlStr, "company_code" , Org.getValue() );
					addElement( xmlStr, "branch_id" , String.valueOf(Employee.getC_Activity_ID()) );
					addElement( xmlStr, "branch_code" , Org.getValue() + '-' + Activity.getValue() );
					if ( Employee.getP_Department_ID() == 1000000 )
					{
						String dept_id = String.valueOf( Org.getAD_Org_ID() + ( 23 * 1000 ) );
						addElement( xmlStr, "dept_id" ,  dept_id );
						
					}
					else {
						String dept_id = String.valueOf( Org.getAD_Org_ID() + ( Employee.getP_Department_ID() * 1000 ) );
						addElement( xmlStr, "dept_id" ,  dept_id );
					}

//					addElement( xmlStr, "dept_id" ,  String.valueOf(Employee.getP_Department_ID()) );
					addElement( xmlStr, "dept_code" , Org.getValue() + '-' + Department.getValue()   );
					
					if ( Employee.getValue().trim().equals( "024910") ||
							Employee.getValue().trim().equals( "002626") ||
							Employee.getValue().trim().equals( "002624") 
					   )
					{
						addElement( xmlStr, "employee_is_admin" , "1" ) ;
						addElement( xmlStr, "employee_email" ,  Employee.getEMail() );
					}
					
					else {
						addElement( xmlStr, "employee_is_admin" , "0" ) ;
						addElement( xmlStr, "employee_email" ,  Employee.getEMail() );
					}
					
					addElement( xmlStr, "company_name" ,  Org.getValue() + " " + Org.getName() );
					addElement( xmlStr, "last3_nas" ,  Employee.getSin().substring(8) );
//					addElement( xmlStr, "unique_guid",  Employee.getUnique_GUID() );
					
					if ( Employee.getPersonalEmail() != null )
						addElement( xmlStr, "employee_personalemail" , Employee.getPersonalEmail() );
					else
						addElement( xmlStr, "employee_personalemail" , Employee.getEMail() );
					
	        		addElement( xmlStr, "username" , Employee.getValue()  );

/*	        		if ( User != null)
	        		{
		        		addElement("username" , Employee.getUserCode() );
	        		}
	        		else
	        		{
		        		addElement("username" , "" );
	        		}
	*/        			

//	        		addElement("PayStub" , rs.getString( "P_Statement_Earning_ID") );

//	        		addElement("attached_by" , rs.getString( "P_Statement_Earning_ID") );
//	        		addElement("attached_by_name" , rs.getString( "P_Statement_Earning_ID") );
	        		
	    	        // Create an instance of SimpleDateFormat used for formatting 
	    			// the string representation of date (month/day/year)
	    			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	    			// Using DateFormat format method we can create a string 
	    			// representation of a date with the defined format.
	    			String startDate = df.format( Period.getStartDate());
	    			String endDate = df.format( Period.getEndDate());
	        		
					xmlStr = xmlStr.append( "</employee>");
				
				}
//	        }

			xmlStr = xmlStr.append( "</titan>");

	        rs.close();
	        stmt.close();

	        // Create an instance of SimpleDateFormat used for formatting 
			// the string representation of date (month/day/year)
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

			// Get the date today using Calendar object.
			Date today = Calendar.getInstance().getTime();        
			// Using DateFormat format method we can create a string 
			// representation of a date with the defined format.
			String reportDate = df.format(today);
			
			String xmlFileName =  "D95AE66B4AC0F6105B0B8ECDB9892D44_Employee" + "_" + Period.getName() + ".xml" ;
    		File xmlFile = new File( EXPORT_LOC + xmlFileName );
    		FileOutputStream out = new FileOutputStream(xmlFile, false);
    		PrintStream p = new PrintStream(out); 
    		p.println(xmlStr);
    		p.close();
    		
    		String ftpFileName = "D95AE66B4AC0F6105B0B8ECDB9892D44_Employee" +  "_" + Period.getName() + ".zip";
    		String employeeFileName = EXPORT_LOC + "D95AE66B4AC0F6105B0B8ECDB9892D44_Employee" +  "_" + Period.getName() + ".zip";
    		try {
    			BufferedInputStream origin = null;
    			FileOutputStream dest = new FileOutputStream( employeeFileName );
    			CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
    			ZipOutputStream outZip = new ZipOutputStream(new BufferedOutputStream(checksum));
    			
    			outZip.setMethod(ZipOutputStream.DEFLATED);
    			outZip.setLevel(Deflater.BEST_COMPRESSION);
    			         
    			byte data[] = new byte[BUFFER];
    	        // get a list of files from current directory
  	        
   	            System.out.println("Adding: "+xmlFileName);
   	            FileInputStream fi = new FileInputStream( EXPORT_LOC + xmlFileName );
   	            origin = new BufferedInputStream(fi, BUFFER);
   	            ZipEntry entry = new ZipEntry( xmlFileName );
   	            outZip.putNextEntry(entry);
   	            int count;
   	            while((count = origin.read(data, 0, BUFFER)) != -1) {
   	            	outZip.write(data, 0, count);
   	            }
   	            origin.close();
   	            outZip.close();

   	            
    		} catch(Exception e) {
    			
    			log.log(Level.SEVERE, "Error send Cassiniweb :", e);
    	       // e.printStackTrace();
    	    }

	        FileWriter fw2 = new FileWriter(scriptFile.getAbsoluteFile(), true);
	        BufferedWriter bw2 = new BufferedWriter(fw2);
	        // write in file
	        bw2.write( "put " + employeeFileName  + "\r\n" );
	        bw2.close();


    		//
    		// Generate PDF file
    		//

     		generatePdf();

        }
        catch (Exception e)
        {
        	log.log(Level.SEVERE, "Error send Cassiniweb :", e);
            // e.printStackTrace(System.out);
        }
   	

        return "";

    }
    
    private void addElement( StringBuffer xmlStr, String Element, String value)
    {
    	//PgiUtil.convertHTMLString( 
    	if ( value != null)
    		xmlStr = xmlStr.append(  new StringBuffer( "<" + Element + ">" + value  + "</" + Element + ">" ));
    	else
    		xmlStr = xmlStr.append(  new StringBuffer( "<" + Element + ">" + "</" + Element + ">" ));
    }
    
    
    
    private String	SendReport( Properties ctx, String fileName,  P_Statement_Earning Statement_Earning ) throws Exception
    {

    	if ( reportClientDoc == null )
    	{
        	reportClientDoc = new ReportClientDocument();
        	
            reportClientDoc.setReportAppServer(ReportClientDocument.inprocConnectionString);
            reportClientDoc.open (reportNameFullPath, OpenReportOptions._openAsReadOnly);
//        	reportClientDoc.open(ReportName, 0);

            ConnectionInfo connectionInfo = createConnectionInfo();
           	setReportConnectionInfo(reportClientDoc, connectionInfo);

    		
    	}

    	ParameterFieldController paramFieldController = reportClientDoc.getDataDefController().getParameterFieldController();
    	
    	
    	//SINGLE VALUE DISCRETE PARAMETERS.
    	
    	P_Period Period = P_Period.get( ctx, Statement_Earning.getP_Period_ID(), trxName);

    	Object [] ClientID = { String.valueOf( Statement_Earning.getAD_Client_ID() ) };
    	Object [] OrgID = { String.valueOf( Statement_Earning.getAD_Org_ID() ) };
    	Object [] EmployeeID = { String.valueOf( Statement_Earning.getP_Employee_ID() ) };
    	Object [] FrequencyID = { String.valueOf( Period.getP_Frequency_ID() ) };
    	Object [] PeriodID = { String.valueOf( Statement_Earning.getP_Period_ID() ) };
    	Object [] DepartmentID = { String.valueOf( 0 ) };
    	Object [] ActivityID = { String.valueOf( 0 ) };
    	Object [] DistributionBookletID = { String.valueOf( 0 ) };
    	Object [] TriAvis = { "1" };
    	
    	String s_note = "";
    	P_Notice_Bank_Deposit note = P_Notice_Bank_Deposit.get(ctx, Statement_Earning.getP_Period_ID(), Statement_Earning.getAD_Org_ID(), Statement_Earning.getP_Department_ID(), Statement_Earning.getC_Activity_ID(), Statement_Earning.getP_Employee_ID(), trxName);
    	if ( note != null )
    		s_note = note.getNotice();
      	
    	Object [] Note = { s_note };
    	
       	paramFieldController.setCurrentValues("", "AD_Client_ID", ClientID );
       	paramFieldController.setCurrentValues("", "AD_Org_ID", OrgID );
       	paramFieldController.setCurrentValues("", "P_Employee_ID", EmployeeID );
       	paramFieldController.setCurrentValues("", "P_Frequency_ID", FrequencyID );
       	paramFieldController.setCurrentValues("", "P_Period_ID", PeriodID );
       	paramFieldController.setCurrentValues("", "P_Department_ID", DepartmentID );
       	paramFieldController.setCurrentValues("", "C_Activity_ID", ActivityID );
       	paramFieldController.setCurrentValues("", "P_Distribution_Booklet_ID", DistributionBookletID );
       	paramFieldController.setCurrentValues("", "Triavis", TriAvis );
       	paramFieldController.setCurrentValues("", "Note", Note );
    		

    	ExportOptions exportOptions = new ExportOptions();
    	if ( ExportFormat.equals("MSExcel"))
    	{
    		exportOptions.setExportFormatType(ReportExportFormat.MSExcel);
    	}
    	else if ( ExportFormat.equals("MSWord"))
    	{
    		exportOptions.setExportFormatType(ReportExportFormat.MSWord);
    	}
    	else if ( ExportFormat.equals("text"))
    	{
    		exportOptions.setExportFormatType(ReportExportFormat.text);
    	}
    	else
    	{
    		exportOptions.setExportFormatType(ReportExportFormat.PDF);
    	}

    	String EXPORT_OUTPUT = fileName;

    	//Export report and obtain an input stream that can be written to disk.
    	//See the Java Reporting Component Developer's Guide for more information on the supported export format enumerations
    	//possible with the JRC.

//    	reportClientDoc.saveAs("MM.rpt", "D:\\PGI\\reports", 1);
    	
    	ReportExportFormat i_ReportExportFormat;
    	if ( ExportFormat.equals("MSExcel"))
    	{
    		i_ReportExportFormat = ReportExportFormat.MSExcel;
    	}
    	else if ( ExportFormat.equals("MSWord"))
    	{
    		i_ReportExportFormat = ReportExportFormat.MSWord;
    	}
    	else
    	{
    		i_ReportExportFormat = ReportExportFormat.PDF;
    	}
//    	ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream)reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
    	ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream)reportClientDoc.getPrintOutputController().export(i_ReportExportFormat);
    			
    	//Release report.
 //   	reportClientDoc.close();
    	
    	//Write file to disk...
    	writeToFileSystem(byteArrayInputStream, EXPORT_OUTPUT);
    	
    	File file2 = new File(EXPORT_OUTPUT);
    	FileDataSource fDataSrc = new FileDataSource(file2);
    	
        return "";
    }

    /*
	* Utility method that demonstrates how to write an input stream to the server's local file system.  
	*/
	private void writeToFileSystem(ByteArrayInputStream byteArrayInputStream, String exportFile) throws Exception {
	
		//Use the Java I/O libraries to write the exported content to the file system.
		byte byteArray[] = new byte[byteArrayInputStream.available()];

		//Create a new file that will contain the exported result.
		File file = new File(exportFile);
		FileOutputStream fileOutputStream = new FileOutputStream(file);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(byteArrayInputStream.available());
		int x = byteArrayInputStream.read(byteArray, 0, byteArrayInputStream.available());

		byteArrayOutputStream.write(byteArray, 0, x);
		byteArrayOutputStream.writeTo(fileOutputStream);

		//Close streams.
		byteArrayInputStream.close();
		byteArrayOutputStream.close();
		fileOutputStream.close();
		
	}


    private void setReportConnectionInfo(ReportClientDocument report, ConnectionInfo connectionInfo) throws ReportSDKException
    {
		DatabaseController dbController = report.getDatabaseController();
		SubreportController srController = report.getSubreportController();
		
		Database db = (Database)dbController.getDatabase();

		//Spécifier la nouvelle connection pour les tables du rapport principal
		setDatabaseLogon ();
//		setTablesConnectionInfo(report, db, connectionInfo, null);
		
		Strings subreportNames = (Strings)srController.querySubreportNames();
		int i = 0;
		boolean endReached = false;
		String subReportName;
		while (!endReached)
		{
			try
			{
				subReportName = subreportNames.getString(i);
				db = (Database)srController.getSubreportDatabase(subReportName);
				//Spécifier la nouvelle connection pour les tables du sous-rapport
				setDatabaseLogon ();
				// setTablesConnectionInfo(report, db, connectionInfo, subReportName);
				
			}
			catch (IndexOutOfBoundsException exc)
			{
				endReached = true;
			}
			i++;
		}

    }

	
    /**
     * Set the database logon associated with the report document.
     * @throws ReportSDKException if there is a problem setting the database logon. 
     */
    private void setDatabaseLogon () throws ReportSDKException
    {
        // TODO Set up database logon here to have the report log onto the
    	// data sources defined in the report automatically, without prompting the
    	// user.  For more information about this feature, refer to the documentation.
    	// For example:
    	//  CRJavaHelper.logonDataSource (reportClientDocument, "username", "password");
    	// will log onto the data sources defined in the report with username "username"
    	// and password "password".

        // jdbc:sqlserver://PGI0133\SQLEXPRESS:1142;databaseName=solstice
        DB.getDatabase();
       
/*        
        String connectionURL = "jdbc:sqlserver://PGI0133\\SQLEXPRESS:1142;databaseName=solstice";
        String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String jndiName = "";
*/        

//		MSystem system = MSystem.get(Env.getCtx());
//		DB.getDatabase().getDriver().getClass()
		CConnection cc = CConnection.get();
//        String connectionURL = "jdbc:jtds:sqlserver://Solstice:1433/solstice;instance=SQLEXPRESS" ; //system.getDBAddress(true);
        String driverName = "net.sourceforge.jtds.jdbc.Driver";
//		String connectionURL = "jdbc:sqlserver://ntap2:50000/solstice" ; //system.getDBAddress(true);
//        String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";

        String connectionURL = cc.getConnectionURL();
		try
        {
			driverName = DB.getDatabase().getDriver().getClass().getName();
        }
        catch (Exception e)
        {
			log.log (Level.SEVERE, "setDatabaseLogon", e);
        }

        String jndiName = "NTAP2";

        
//        DB_SqlServer[jdbc:jtds:sqlserver://Solstice:1433/solstice]
        	
		String userName = CConnection.get().getDbUid(); 
		String password = CConnection.get().getDbPwd();
        
        CRJavaHelper.changeDataSource(reportClientDoc, userName, password, connectionURL, driverName, jndiName);

 
 //   	CRJavaHelper.logonDataSource (reportClientDoc, userName, password);
    }


    private ConnectionInfo createConnectionInfo()
    {
	
		//Spécifier le nouvel object de connection
		ConnectionInfo connectionInfo = new ConnectionInfo();
		connectionInfo.setKind(ConnectionInfoKind.SQL);
		connectionInfo.setUserName(userName);
		connectionInfo.setPassword(password);
		PropertyBag attributes = new PropertyBag();
		PropertyBag logonProperties = new PropertyBag();
		
		attributes.putBooleanValue(PropertyBagHelper.CONNINFO_CRQE_SQLDB, true);
		attributes.putStringValue(PropertyBagHelper.CONNINFO_DATABASE_DLL, "crdb_ado.dll");
		attributes.putStringValue(PropertyBagHelper.CONNINFO_SERVER_NAME, databaseServer);
		attributes.putStringValue(PropertyBagHelper.CONNINFO_DATABASE_NAME, databaseName);
		attributes.putStringValue(PropertyBagHelper.CONNINFO_SERVER_TYPE, "OLE DB (ADO)");
		
		attributes.put(PropertyBagHelper.CONNINFO_CRQE_LOGONPROPERTIES, logonProperties);

		connectionInfo.setAttributes(attributes);
		return connectionInfo;
    }
    

	public void AddDateParameters()
	{
		int ClientID = this.getAD_Client_ID(); 
        int OrgID = Env.getAD_Org_ID(Env.getCtx()); 
        int UserID = this.getAD_User_ID();
        
        try
		{        
        	String SQLInsert = "INSERT INTO AD_PINSTANCE_PARA (AD_PINSTANCE_ID, SEQNO, PARAMETERNAME, P_STRING, P_STRING_TO, P_NUMBER, P_NUMBER_TO, P_DATE, P_DATE_TO, INFO, INFO_TO, AD_CLIENT_ID, AD_ORG_ID, CREATED, CREATEDBY, UPDATED, UPDATEDBY, ISACTIVE) " +
	        "SELECT " + this.getAD_PInstance_ID() + ", " +
	        "(SELECT ISNULL(MAX(SEQNO) + 1,0) FROM AD_PINSTANCE_PARA WHERE AD_PINSTANCE_ID = " + this.getAD_PInstance_ID() + "), " +
	        "'AD_PInstance_ID',NULL, NULL," + this.getAD_PInstance_ID() + ", NULL,NULL, NULL, NULL, NULL, " + ClientID + ", " + OrgID + ", GETDATE(), " + UserID + ", GETDATE(), " + UserID + ", 'Y'";
        		
			PreparedStatement pstmp = null;
			
			pstmp = DB.prepareStatement(SQLInsert, null);
			pstmp.execute();
		}
		catch (Exception e)
		{
			System.err.println("ERROR  [AddParameters] " + e);
		}
	}


	private String getIP_Address() 
	{
		return  PgiUtil.getSolsticeParameter(Env.getCtx(), "CassiniwebFtpServer");
//		return  "canadianhelicopters.cassiniweb.com";
    }

	private String getUserName() 
	{
		return  PgiUtil.getSolsticeParameter(Env.getCtx(), "CassiniwebFtpUser");
//		return "canadianhelicopters";
    }

	private String getPassword() 
	{
		return  PgiUtil.getSolsticeParameter(Env.getCtx(), "CassiniwebFtpPwd");
//		return "caHe2798!gT67sR3";
    }

/*

	private boolean SendFileSFTP( String path, String fileName) throws Exception
	{
		
			
		boolean success = true;
		
		String hostname = getIP_Address() ;
		String username = getUserName();
		String password = getPassword();
		int port = 1122;
		
		String cmd = null;
		
		 // Create an SshConnector instance
		SshConnector con = SshConnector.createInstance();

		// Lets do some host key verification

//		con.getContext().setHostKeyVerification( new ConsoleKnownHostsKeyVerification());
		con.getContext().setPreferredPublicKey(	Ssh2Context.PUBLIC_KEY_SSHDSS);

		// Connect to the host
		SocketTransport t = new SocketTransport(hostname, port);
		t.setTcpNoDelay(true);

		SshClient ssh = con.connect(t, username, true);

		Ssh2Client ssh2 = (Ssh2Client) ssh;
		// Authenticate the user using password authentication
		PasswordAuthentication pwd = new PasswordAuthentication();

		do {
			System.out.print("Password: ");
			pwd.setPassword( password );
		} while (ssh2.authenticate(pwd) != SshAuthentication.COMPLETE
				&& ssh.isConnected());

		 // Start a session and do basic IO
		if (ssh.isAuthenticated()) {

			SftpClient sftp = new SftpClient(ssh2);
			
			 // Perform some text mode operations
			sftp.setTransferMode(SftpClient.MODE_BINARY);
			
			sftp.put( path + '/' + fileName );
		
			sftp.quit();
		}

		ssh.disconnect();
		return success;
		
	}
	*/
	
	private StringBuffer getCompanyBranchDeptInfo(){
		StringBuffer xmlStr = new StringBuffer( "");
	
    	String sql = "Select P_Employee.AD_Org_ID,  max(AD_Org.Value) Company_code "
			   		+ "	, max( AD_Org.Name ) as Company_Desc1 "
 			   		+ " , max( AD_Org.Description ) as Company_Desc2 "
					+ "From P_Employee  "
					+ "Inner Join AD_Org ON AD_Org.AD_Org_ID = P_Employee.AD_Org_ID "
					+ "Where P_Employee.P_Employee_ID IN ( Select P_Employee_ID From P_Statement_Earning Where grossEarningsPeriod <> 0 AND P_Period_ID = " + P_Period_ID + ") "
					+ "Group BY P_Employee.AD_Org_ID "
					+ "ORDER BY P_Employee.AD_Org_ID" ;
		
		
    	PreparedStatement stmt = DB.prepareStatement(sql, null);
        try
        {
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	        	xmlStr = xmlStr.append( "<company code=\"" + rs.getString("Company_Code") + "\">");

				addElement( xmlStr, "company_id" , rs.getString("AD_Org_ID") );
		        addElement( xmlStr, "company_code" , rs.getString("Company_Code" ) ) ;
				addElement( xmlStr, "company_name" , rs.getString("Company_Desc1" ) ) ;
				addElement( xmlStr, "company_desc1" , rs.getString("Company_Desc1" ) ) ;
				addElement( xmlStr, "company_desc2" , rs.getString("Company_Desc2" ) ) ;
				addElement( xmlStr, "contact_name" , "Roxanne Blanchfield" ) ;
				addElement( xmlStr, "contact_email" , "rblanchfield@canadianhelicopters.com" ) ;
				
				xmlStr = xmlStr.append( getBranchDeptInfo( rs.getInt("AD_Org_ID") ) );
		        xmlStr = xmlStr.append( "</company>");
	        }


	        rs.close();
	        stmt.close();
        }
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getCompanyBranchDeptInfo", e);
		}
        return xmlStr;
	}

	private StringBuffer getBranchDeptInfo( int AD_Org_ID ){
		StringBuffer xmlStr = new StringBuffer( "");
	
    	String sql = "Select P_Employee.C_Activity_ID as Branch_ID "
					+ ", max(AD_Org.Value + '-' + C_Activity.Value ) as branch_code "
					+ ", max(C_Activity_TRL.Name )  as branch_desc1 "
					+ ", max(C_Activity.Name )  as branch_desc2 "
					+ "From P_Employee  "
					+ "Inner Join AD_Org ON AD_Org.AD_Org_ID = P_Employee.AD_Org_ID "
					+ "Inner Join C_Activity ON C_Activity.C_Activity_ID = P_Employee.C_Activity_ID "
					+ "Inner Join C_Activity_TRL ON C_Activity_Trl.C_Activity_ID = P_Employee.C_Activity_ID  AND AD_Language = 'fr_CA'"
					+ "Where P_Employee.P_Employee_ID IN ( Select P_Employee_ID From P_Statement_Earning Where grossEarningsPeriod <> 0 AND  P_Period_ID = " + P_Period_ID + ") "
					+ "AND P_Employee.AD_Org_ID = " + AD_Org_ID 
					+ "Group BY P_Employee.C_Activity_ID "
					+ "ORDER BY P_Employee.C_Activity_ID" ;
		
		
    	PreparedStatement stmt = DB.prepareStatement(sql, null);
        try
        {
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
				xmlStr = xmlStr.append("<branch code=\"" + rs.getString("branch_code") + "\">" ) ;
				addElement( xmlStr, "branch_id" , rs.getString( "branch_id")) ;
				addElement( xmlStr, "branch_code" , rs.getString( "branch_code") ) ;
				addElement( xmlStr, "branch_desc1" , rs.getString( "branch_desc1") ) ;
				addElement( xmlStr, "branch_desc2" , rs.getString( "branch_desc2") ) ;

				xmlStr = xmlStr.append( getDeptInfo( AD_Org_ID, rs.getInt("Branch_ID") ) );

				xmlStr = xmlStr.append( "</branch>");

	        }

	        rs.close();
	        stmt.close();
        }
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getBranchDeptInfo", e);
		}
        return xmlStr;
	}

	private StringBuffer getDeptInfo( int AD_Org_ID,  int C_Activity_ID ){
		StringBuffer xmlStr = new StringBuffer( "");
	
    	String sql = "Select AD_Org.AD_Org_ID + ( CASE WHEN P_Employee.P_Department_ID = 1000000 THEN 23 else P_Employee.P_Department_ID END * 1000 ) as Dept_ID "
					+ ", max(AD_Org.Value + '-' + P_Department.Value ) as dept_code "
					+ ", max(P_Department_Trl.Name ) as dept_desc1 "
					+ ", max(P_Department.Name ) as dept_desc2 "
					+ "From P_Employee  "
					+ "Inner Join AD_Org ON AD_Org.AD_Org_ID = P_Employee.AD_Org_ID "
					+ "Inner Join P_Department ON P_Department.P_Department_ID = P_Employee.P_Department_ID "
					+ "Inner Join P_Department_Trl ON P_Department_Trl.P_Department_ID = P_Department.P_Department_ID AND AD_Language = 'fr_CA'"
					+ "Where P_Employee.P_Employee_ID IN ( Select P_Employee_ID From P_Statement_Earning Where grossEarningsPeriod <> 0 AND P_Period_ID = " + P_Period_ID + ") "
					+ "AND P_Employee.AD_Org_ID = " + AD_Org_ID 
					+ "AND P_Employee.C_Activity_ID = " + C_Activity_ID
					+ "Group BY AD_Org.AD_Org_ID, P_Employee.P_Department_ID "
					+ "ORDER BY AD_Org.AD_Org_ID, P_Employee.P_Department_ID" ;
		
		
    	PreparedStatement stmt = DB.prepareStatement(sql, null);
        try
        {
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
				xmlStr = xmlStr.append("<department code=\"" + rs.getString("dept_code") + "\">" ) ;
				addElement( xmlStr, "dept_id" , rs.getString( "dept_id")) ;
				addElement( xmlStr, "dept_code" , rs.getString( "dept_code") ) ;
				
				addElement( xmlStr, "dept_desc1" , rs.getString( "dept_desc1") ) ;
				addElement( xmlStr, "dept_desc2" , rs.getString( "dept_desc2") ) ;

				xmlStr = xmlStr.append( "</department>");

	        }

	        rs.close();
	        stmt.close();
        }
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getDeptInfo", e);
		}
        return xmlStr;
	}

	static final int BUFFER = 2048;
	
	private void generatePdf(){

		P_Period Period = P_Period.get( getCtx(), P_Period_ID, trxName);
    	P_Year Year = P_Year.get( getCtx(),  Period.getP_Year_ID(),  trxName);

	
    	String sql = "Select P_Employee.AD_Org_ID,  max(AD_Org.Value) Company_code "
 			   		+ "  , max((AD_Org.Value ) + ' ' + AD_Org.Description ) as Company_Desc1 "
 			   		+ "	, max((AD_Org.Value ) + ' ' + AD_Org.Name ) as Company_Desc2 "
					+ "From P_Employee  "
					+ "Inner Join P_Statement_Earning ON P_Statement_Earning.P_Employee_ID = P_Employee.P_Employee_ID AND grossEarningsPeriod <> 0 AND P_Period_ID = " + P_Period_ID 
					+ "Inner Join AD_Org ON AD_Org.AD_Org_ID = P_Statement_Earning.AD_Org_ID "
					+ "Where P_Statement_Earning.PREPRINTEDNO like 'D%' ";  //1=1 2017-04-27

		if ( this.Org_ID != 0 )
			sql +=" AND P_Statement_Earning.AD_Org_ID = " + Org_ID;

		if ( this.P_Distribution_Booklet_ID !=0)
			sql +=" AND P_Employee.P_Employee_id in ( select P_Employee_ID from P_Employee where P_DISTRIBUTION_BOOKLET_ID = " + this.P_Distribution_Booklet_ID + " )";
		if ( this.P_Distribution_ID !=0)
			sql +=" AND P_Employee.P_DISTRIBUTION_ID=" + this.P_Distribution_ID;
		if ( this.P_Employee_ID !=0)
			sql +=" AND P_Employee.P_EMPLOYEE_ID="+ this.P_Employee_ID;
		if ( this.P_Payment_Group_ID !=0)
			sql +=" AND P_Payment_Group_ID="+ this.P_Payment_Group_ID;


		
		sql += "Group BY P_Employee.AD_Org_ID "
					+ "ORDER BY P_Employee.AD_Org_ID" ;
		
		
    	PreparedStatement stmt = DB.prepareStatement(sql, null);
        try
        {

			ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	    		StringBuffer xmlStr = new StringBuffer( "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
				xmlStr = xmlStr.append( "<titan>");
	        	xmlStr = xmlStr.append( "<company code=\"" + rs.getString("Company_Code") + "\">");

				addElement( xmlStr, "company_id" , rs.getString("AD_Org_ID") );
		        addElement( xmlStr, "company_code" , rs.getString("Company_Code" ) ) ;
				addElement( xmlStr, "company_name" , rs.getString("Company_Desc1" ) ) ;
				addElement( xmlStr, "company_desc1" , rs.getString("Company_Desc1" ) ) ;
				addElement( xmlStr, "company_desc2" , rs.getString("Company_Desc2" ) ) ;
				addElement( xmlStr, "contact_name" , "Roxanne Blanchfield" ) ;
				addElement( xmlStr, "contact_email" , "rblanchfield@canadianhelicopters.com" ) ;
				
				xmlStr = xmlStr.append( getBranchDeptInfo( rs.getInt("AD_Org_ID") ) );
		        xmlStr = xmlStr.append( "</company>");
		        xmlStr = xmlStr.append(  generatePdfDetail( rs.getInt( "AD_Org_ID")) );

				xmlStr = xmlStr.append( "</titan>");

		        // Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

				// Get the date today using Calendar object.
				Date today = Calendar.getInstance().getTime();        
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				String reportDate = df.format(today);
				
				String xmlFileName =  "D95AE66B4AC0F6105B0B8ECDB9892D44" +  "_" + rs.getString("Company_Code").trim() + "_" + Period.getName() + ".xml" ;
				File xmlFile = new File( EXPORT_LOC + xmlFileName );
				try
			    {
					FileOutputStream out = new FileOutputStream(xmlFile, false);
					PrintStream p = new PrintStream(out); 
					p.println(xmlStr);
					p.close();
			    }
			    catch (Exception e)
			    {
			            e.printStackTrace(System.out);
			    }
				
				String ftpFileName = "D95AE66B4AC0F6105B0B8ECDB9892D44" +  "_" + rs.getString("Company_Code").trim() + "_" + Period.getName() + ".zip" ;
				String zipFileName = EXPORT_LOC + "D95AE66B4AC0F6105B0B8ECDB9892D44" +  "_" + rs.getString("Company_Code").trim() + "_" + Period.getName() + ".zip" ;
				try {
					BufferedInputStream origin = null;
					FileOutputStream dest = new FileOutputStream( zipFileName );
					CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
					ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
					
					out.setMethod(ZipOutputStream.DEFLATED);
					out.setLevel(Deflater.BEST_COMPRESSION);
					         
					byte data[] = new byte[BUFFER];
			        // get a list of files from current directory
					
			        File f = new File( EXPORT_LOC  );
			        String files[] = f.list();
			        // Création des PDF.
			        for (int i=0; i<files.length; i++) {
			            if(files[i].endsWith(".doc")) {
				            System.out.println("OfficeToPDf: "+files[i]);
				            String path = PgiUtil.getSolsticeParameter(getCtx(), "CassiniwebPDFPath");
				            String cmd = path +  "OfficeToPdf.exe ";
				            String mm = "cmd /c " + cmd + EXPORT_LOC +  files[i] + " " + EXPORT_LOC + files[i].replace(".doc", ".pdf");
				            
			            	Process p = Runtime.getRuntime().exec("cmd /c " + cmd + EXPORT_LOC +  files[i] + " " + EXPORT_LOC + files[i].replace(".doc", ".pdf"));
//			            	Process p = Runtime.getRuntime().exec("cmd /c C:\\Solstice\\PayStub\\OfficeToPdf.exe " + EXPORT_LOC +  files[i] + " " + EXPORT_LOC + files[i].replace(".doc", ".pdf"));
			            	
			            	System.out.println("Waiting for batch file ...");
			                p.waitFor();
			                System.out.println("Batch file done.");

			                //Delete les DOC.
				            File file = new File( EXPORT_LOC + files[i] );
				            if (file.exists())
								file.delete();
								
			            }
			        }
			        
			        //Zip pdf.
			        String  pdfFiles[] = f.list();
			        
			        // Add Pdf file to zip file.
			        for (int i=0; i<pdfFiles.length; i++) {
			            if(pdfFiles[i].endsWith(".pdf")) {
				            System.out.println("Adding: "+pdfFiles[i]);
			                //it is a .pdf file!
				            FileInputStream fi = new FileInputStream( EXPORT_LOC + pdfFiles[i]);
				            origin = new BufferedInputStream(fi, BUFFER);
				            ZipEntry entry = new ZipEntry(pdfFiles[i]);
				            out.putNextEntry(entry);
				            int count;
				            while((count = origin.read(data, 0, BUFFER)) != -1) {
				               out.write(data, 0, count);
				            }
				            origin.close();

				            //Delete les PDF.
				            File file = new File( EXPORT_LOC + pdfFiles[i] );
				            if (file.exists())
								file.delete();
			            }
			         }

			         // ADD Xml file to zip file
		  	         System.out.println("Adding: "+xmlFileName);
		   	         FileInputStream fi = new FileInputStream( EXPORT_LOC + xmlFileName );
		   	         origin = new BufferedInputStream(fi, BUFFER);
		   	         ZipEntry entry = new ZipEntry( xmlFileName );
		   	         out.putNextEntry(entry);
		   	         int count;
		   	         while((count = origin.read(data, 0, BUFFER)) != -1) {
		   	        	out.write(data, 0, count);
		   	         }
		   	         origin.close();
			        
			        out.close();


//			        SendFileSFTP( EXPORT_LOC,  ftpFileName );
			        
			        FileWriter fw = new FileWriter(scriptFile.getAbsoluteFile(), true);
			        BufferedWriter bw = new BufferedWriter(fw);
			        // write in file
			        bw.write( "put " + zipFileName + "\r\n" );
			        bw.close();


				} catch(Exception e) {
			         e.printStackTrace();
			    }

	        }


	        rs.close();
	        stmt.close();
        }
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getCompanyBranchDeptInfo", e);
		}



	}

	private StringBuffer generatePdfDetail( int AD_Org_ID ) {

		
    	StringBuffer xmlStr = new StringBuffer( "");

    	P_Period Period = P_Period.get( getCtx(), P_Period_ID, trxName);
    	P_Year Year = P_Year.get( getCtx(),  Period.getP_Year_ID(),  trxName);

    	String sql = "Select P_Statement_Earning.*, PaymentTypeDoc, ROW_NUMBER() OVER (  PARTITION BY P_Statement_Earning.P_Employee_ID order by P_Statement_Earning.P_EMPLOYEE_ID ) as Sequence "
    			   + " FROM P_Statement_Earning "
    			   + " inner join P_Payment on P_Payment.P_Payment_ID = P_Statement_Earning.P_Payment_ID "
    			   + " inner join P_Employee on P_Employee.P_Employee_ID = P_Statement_Earning.P_Employee_ID "
    			   + " Where grossEarningsPeriod <> 0 AND P_Statement_Earning.P_Period_ID = " + P_Period_ID;

		sql +=" AND P_Employee.AD_Org_ID = " + AD_Org_ID;
	    
		if ( this.P_Distribution_Booklet_ID !=0)
			sql +=" AND P_Statement_Earning.P_Employee_id in ( select P_Employee_ID from P_Employee where P_DISTRIBUTION_BOOKLET_ID = " + this.P_Distribution_Booklet_ID + " )";
		if ( this.P_Distribution_ID !=0)
			sql +=" AND P_Statement_Earning.P_DISTRIBUTION_ID=" + this.P_Distribution_ID;
		if ( this.P_Employee_ID !=0)
			sql +=" AND P_Statement_Earning.P_EMPLOYEE_ID="+ this.P_Employee_ID;
		if ( this.P_Payment_Group_ID !=0)
			sql +=" AND P_Statement_Earning.P_Payment_Group_ID="+ this.P_Payment_Group_ID;

		//Seulement les employés actif et avec un utilisateur.
//		sql +=" AND P_Employee_id in ( select P_Employee_ID from P_Employee where Isactive = 'Y' and UserCode is not null )";

 	
 	PreparedStatement stmt = DB.prepareStatement(sql, null);
     try
     {
     	P_Payment Payment = null;
     	


			xmlStr = xmlStr.append( "<perioddata code=\"" + Period.getName().replace("-", "") +"\">");
			
			addElement( xmlStr, "code", Period.getName().replace("-", "")  );
			addElement( xmlStr, "year_no", String.valueOf( Year.getYear())  );
			addElement( xmlStr, "period_no", String.valueOf( Period.getPeriodNo() ) );

			DateFormat dfperiod = new SimpleDateFormat("yyyy-MM-dd");

			addElement( xmlStr, "from", dfperiod.format(  Period.getStartDate() ) );
			addElement( xmlStr, "to",  dfperiod.format( Period.getEndDate() ) );

			
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
				xmlStr = xmlStr.append( "<talon>");
				P_Employee Employee = P_Employee.get( getCtx(), rs.getInt("P_Employee_ID"), null);
				MActivity Activity = new MActivity( getCtx(), Employee.getC_Activity_ID(), null);
				P_Department Department = P_Department.get( getCtx(), Employee.getP_Department_ID(), null);
				MOrg Org = new MOrg( getCtx(), Employee.getAD_Org_ID(), null);
				
				String sfile = rs.getString( "P_Statement_Earning_ID");

				addElement( xmlStr, "filename", sfile + ".pdf" );

	        	P_Statement_Earning Statement_Earning = new P_Statement_Earning( getCtx(), rs, trxName);
	        	
	        	Payment = new P_Payment( getCtx(), rs.getInt( "P_Payment_ID"), trxName); 

				addElement( xmlStr, "check_no", Payment.getValue() );
/*				
				if ( rs.getString( "PaymentTypeDoc").equals("Regular") )
					addElement( xmlStr, "sequence", "1" );
				else
					addElement( xmlStr, "sequence", "2" );
*/				
				addElement( xmlStr, "sequence", rs.getString( "Sequence") );
				

	        	
				String fileName = EXPORT_LOC + sfile + ".pdf";

				if ( ExportFormat.equals("MSWord") ) {
					fileName = EXPORT_LOC + sfile + ".doc";
				}
				
				log.log(Level.INFO,"fileName - " + fileName );
				
				File file = new File( fileName  ); 
/*
				MAttachment attachment = null;
									
				if ( Payment.isPdfAttachment()  )
				{
					log.log(Level.INFO,"Attachment Exists  "  );
					Payment.deletePdfAttachment(".pdf");
				}

				{
*/				
//M.M
					SendReport( getCtx(), fileName, Statement_Earning );

					addElement( xmlStr, "employee_id", rs.getString("P_Employee_ID") );
					addElement( xmlStr, "employee_code" , Employee.getValue() ) ;
					addElement( xmlStr, "employee_first_name" , Employee.getFirstName() );
					addElement( xmlStr, "employee_last_name"  , Employee.getSurname() );
					addElement( xmlStr, "company_id" , String.valueOf(Employee.getAD_Org_ID()) );
					addElement( xmlStr, "company_code" , Org.getValue() );
					addElement( xmlStr, "branch_id" , String.valueOf(Employee.getC_Activity_ID()) );
					addElement( xmlStr, "branch_code" , Org.getValue() + '-' + Activity.getValue() );
					if ( Employee.getP_Department_ID() == 1000000 )
					{
						String dept_id = String.valueOf( Org.getAD_Org_ID() + ( 23 * 1000 ) );
						addElement( xmlStr, "dept_id" ,  dept_id );
						
					}
					else {
						String dept_id = String.valueOf( Org.getAD_Org_ID() + ( Employee.getP_Department_ID() * 1000 ) );
						addElement( xmlStr, "dept_id" ,  dept_id );
					}
					addElement( xmlStr, "dept_code" , Org.getValue() + '-' + Department.getValue()   );
					
/*					if ( Employee.getValue().trim().equals( "001073") ||
							Employee.getValue().trim().equals( "001042") ||
							Employee.getValue().trim().equals( "024910") ||
							Employee.getValue().trim().equals( "070915") ||
							Employee.getValue().trim().equals( "002205") ||
							Employee.getValue().trim().equals( "002569") ||
							Employee.getValue().trim().equals( "002570")
					   )
*/					   
						addElement( xmlStr, "employee_email" ,  Employee.getEMail() );
/*					else
						addElement( xmlStr, "employee_email" , null );
*/						
//					addElement( xmlStr, "employee_email" ,  Employee.getEMail() );
					if ( Employee.getP_Language_ID() == 127) {
						addElement( xmlStr, "employee_language" , "1" );
					}
					else {
						addElement( xmlStr, "employee_language" , "2" );
					}	
						
					addElement( xmlStr, "employee_sex" ,  Employee.getGender() );
					addElement( xmlStr, "employee_prefix" ,  null );
					
					addElement( xmlStr, "company_name" ,  Org.getValue() + " " + Org.getName() );
					addElement( xmlStr, "last3_nas" ,  Employee.getSin().substring(8) );
//					addElement( xmlStr, "unique_guid",  Employee.getUnique_GUID() );
					
	        		addElement( xmlStr, "employee_personalemail" , Employee.getPersonalEmail() );

	        		addElement( xmlStr, "username" , Employee.getValue()  );
					
					/*
					log.log(Level.INFO,"Create Attachment - " + fileName );
					attachment = Payment.createAttachment();
					attachment.addEntry( file);
					attachment.save();
//ICI CRÉER UN ZIP					
					SendFileFTP( EXPORT_LOC,  sfile + ".pdf"  );
//					SendToWebSite( EXPORT_LOC,  sfile + ".pdf"  );
*/
	    			xmlStr = xmlStr.append( "</talon>");

	        }
	        rs.close();
	        stmt.close();

			xmlStr = xmlStr.append( "</perioddata>");

    		
//			SendFileFTP( EXPORT_LOC,  xmlFileName  );

	//	    Env.startBrowser("www.solsticeplus.com/hrm/upload/loadPayStub.php?file=" + xmlFileName );

//2019-07-31 Fermeture du rapport a chaque compagnie.			
	     	reportClientDoc.close();
	     	reportClientDoc = null;

        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }

     
     	return xmlStr;

	}
	
	
}

