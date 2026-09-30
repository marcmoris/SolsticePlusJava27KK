package solstice.process;


import java.io.*;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;


import org.compiere.db.CConnection;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.DB;
import org.compiere.util.EMail;

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

import solstice.utils.PgiUtil;

import org.compiere.model.MActivity;
import org.compiere.model.MClient;
import org.compiere.model.MOrg;
import org.compiere.model.MProcess;
import org.compiere.model.MPInstance;

import org.compiere.model.MUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;



public class PayStubByPdfEmail extends SvrProcess {

	
	
	private String trxName = null;
	private int Org_ID=0;
	
	private int P_Period_ID=0;
	private int P_Distribution_ID=0;
	private int P_Distribution_Booklet_ID=0;
	private int P_Payment_Group_ID=0;
	private int P_Employee_ID=0;
	private boolean Rework=false;
	private int m_inserted = 0;

	private MClient m_client = MClient.get(Env.getCtx());

	
	private String ExportFormat = "PDF";
//	private String ExportFormat = "MSWord";
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
	
	private String EXPORT_LOC ;
	private String scriptFileName;
	private File scriptFile;
	
	protected String doIt() throws Exception
	{
    	int i = 0;

		
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
			sql +=" AND P_Employee_id in ( select P_Employee_ID from P_Employee where P_Payment_Group_ID = " + this.P_Payment_Group_ID + " )";

	
		//Seulement les employés actif et avec un utilisateur.
//		sql +=" AND P_Employee_id in ( select P_Employee_ID from P_Employee where Isactive = 'Y' and UserCode is not null )";

		sql +=" AND P_Employee_id in ( select P_Employee_ID from P_Employee where PersonalEmail is not null and PersonalEmail <> '' )";
    	
//		sql += " AND P_Payment_Id in ( select P_Payment_Id from p_Payment where timesheetstatus = 'E' )";
		
    	PreparedStatement stmt = DB.prepareStatement(sql, null);
        try
        {
        	
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {
	        	

		        	
		        // Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date (month/day/year)
				DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	
				// Get the date today using Calendar object.
				Date today = Calendar.getInstance().getTime();        
	
	
	    		//
	    		// Generate PDF file
	    		//
	
				P_Statement_Earning Statement_Earning = new P_Statement_Earning( Env.getCtx(), rs, null);
				generatePdf( Statement_Earning );
				
				i = i + 1;

	        }
        }
        catch (Exception e)
        {
        	log.log(Level.SEVERE, "Error send PayStubByPDF :", e);
            // e.printStackTrace(System.out);
        }
 	

        return "@inserted@ " + i;

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
//    	Object [] DepartmentID = { String.valueOf( 0 ) };
//    	Object [] ActivityID = { String.valueOf( 0 ) };
    	Object [] DistributionBookletID = { String.valueOf( P_Distribution_Booklet_ID ) };
    	Object [] DistributionID = { String.valueOf( P_Distribution_ID ) };
    	Object [] Statement_Earning_ID = { String.valueOf( Statement_Earning.getP_Statement_Earning_ID() ) };
    	
    	
    	
//    	Object [] TriAvis = { "1" };
//    	Object [] Note = { " " };
    	Object [] IsCodeForAbsentEmployees = { "N" };
    	
    	String s_note = "";
/*    	P_Notice_Bank_Deposit note = P_Notice_Bank_Deposit.get(ctx, Statement_Earning.getP_Period_ID(), Statement_Earning.getAD_Org_ID(), Statement_Earning.getP_Department_ID(), Statement_Earning.getC_Activity_ID(), Statement_Earning.getP_Employee_ID(), trxName);
    	if ( note != null )
    		s_note = note.getNotice();
    	if ( s_note == null) s_note =  "";
 */     	
//    	Object [] Note = { s_note };
    	
    	
       	paramFieldController.setCurrentValues("", "AD_Client_ID", ClientID );
       	paramFieldController.setCurrentValues("", "AD_Org_ID", OrgID );
       	paramFieldController.setCurrentValues("", "P_Employee_ID", EmployeeID );
       	paramFieldController.setCurrentValues("", "P_Frequency_ID", FrequencyID );
       	paramFieldController.setCurrentValues("", "P_Period_ID", PeriodID );
//       	paramFieldController.setCurrentValues("", "P_Department_ID", DepartmentID );
//       	paramFieldController.setCurrentValues("", "C_Activity_ID", ActivityID );
       	paramFieldController.setCurrentValues("", "P_Distribution_Booklet_ID", DistributionBookletID );
       	paramFieldController.setCurrentValues("", "P_Distribution_ID", DistributionID );
       	//2023-08-18 Un relever a la fois.
       	paramFieldController.setCurrentValues("", "P_Statement_Earning_ID", Statement_Earning_ID );
       	paramFieldController.setCurrentValues("", "IsCodeForAbsentEmployees", IsCodeForAbsentEmployees );
//       	paramFieldController.setCurrentValues("", "Triavis", TriAvis );
//       	paramFieldController.setCurrentValues("", "Note", Note );
    		

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

        DB.getDatabase();
       
		CConnection cc = CConnection.get();
        String driverName = "net.sourceforge.jtds.jdbc.Driver";

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

        
        	
		String userName = CConnection.get().getDbUid(); 
		String password = CConnection.get().getDbPwd();
        
        CRJavaHelper.changeDataSource(reportClientDoc, userName, password, connectionURL, driverName, jndiName);

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


	static final int BUFFER = 2048;
	
	private void generatePdf( P_Statement_Earning Statement_Earning ) throws Exception{

				P_Employee Employee = P_Employee.get( getCtx(), Statement_Earning.getP_Employee_ID(), null);
				
				if ( Employee.getPersonalEmail() == null || Employee.getPersonalEmail() == "" )
					return;
				
				
				P_Period Period = P_Period.get(Env.getCtx(), this.P_Period_ID , null );

				String sfile = Period.getName() + "-" + Employee.getValue() + "-" + Statement_Earning.getPrePrintedNo();

				EXPORT_LOC = "C:\\Solstice\\Transferts\\Paystub" + File.separator  + Period.getName().trim() + File.separator;
				
				File file = new File(EXPORT_LOC);
 		        // true if the directory was created, false otherwise
		        if (file.mkdirs()) {
		            System.out.println("Directory is created!");
		        } 
//		        else {
//		            System.out.println("Failed to create directory!");
//		        }
	            
				String fileName = EXPORT_LOC + sfile + ".pdf";
				String fileNamePdf = EXPORT_LOC + sfile + ".pdf";
			
				//Appel crytal report pour créer un PDF
				SendReport( getCtx(), fileName, Statement_Earning );

				// 2022-04-22
	            // Envoie du courriel
				String title;
				String message;
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        String startDate = df.format( Period.getStartDate());
		        String endDate = df.format( Period.getEndDate() );
				
				if ( Employee.getP_Language_ID() == 155  )  // French
				{
					title = "Avis de dépôt";
				    message = "Voici votre nouveau bulletin de paie couvrant la p&eacute;riode du " + startDate + " au " + endDate;
				}
				else
				{
					title = "Paystub";
				    message = "Here is your new payslip covering the period from " + startDate + " to " + endDate;
				}
/*				
				EMail email = m_client.createEMail( getFrom() , Employee.getPersonalEmail() , title, message);

				File filePdf = new File( fileNamePdf  ); 
				email.addAttachment( filePdf );

				String status = email.send();
				
				if (email.isSentOK())
				{
					log.info( "MessageSent");
				}
				else
				{
					log.info( "MessageNotSent " + status);
				}
*/				
				log.info( "Envoyer a : " + Employee.getPersonalEmail().trim() );
				this.send( Employee.getPersonalEmail().trim(), title, message,  fileNamePdf);
	    	}
	
	/**
	 *  Get Sender
	 */
	public MUser getFrom()
	{
		MUser User = MUser.get( Env.getCtx());
		return User;
	}	//	getFrom


	public void send2( String MailTo, String MailSubject, String MailBody, String AttachmentPath ) throws IOException
	{
		String scriptPath = "C:\\Solstice\\Tools\\sendmail.ps1 '" + MailTo + "' '" + MailSubject + "' '" + MailBody + "' '" + AttachmentPath + "'" ;
		System.out.println( scriptPath );
	}

	public void send( String MailTo, String MailSubject, String MailBody, String AttachmentPath ) throws IOException
	{
		// C:\\Solstice\\Tools\\ dans le path
//		String scriptPath = "sendmail.ps1 -MailTo '" + MailTo + "' -MailSubject '" + MailSubject +"' -MailBody '" + MailBody + "' -AttachmentPath '" + AttachmentPath + "'";
//		String scriptPath = "'C:\\Solstice\\Tools\\sendmail.ps1' '" + MailTo + "' '" + MailSubject + "' '" + MailBody + "' '" + AttachmentPath + "'" ;
		String scriptPath = "C:\\Solstice\\Tools\\sendmail.ps1";
		java.util.List<String> command = new java.util.ArrayList<>();
        command.add("powershell.exe");
        command.add("-NoProfile");
        command.add("-ExecutionPolicy");
        command.add("Bypass");
        command.add("-File " );
        command.add(scriptPath);
        command.add(MailTo);
        command.add(MailSubject);
        command.add(MailBody);
        command.add(AttachmentPath);
 
	    try {
	            ProcessBuilder pb = new ProcessBuilder(command);
	            // Fusionne stderr dans stdout pour tout lire d'un seul flux
	            pb.redirectErrorStream(true);
	 
	            System.out.println("=== Lancement du script PowerShell : " + scriptPath + " ===");
	            Process process = pb.start();
	 
	            // Lecture en temps réel de la sortie du script.
	            // On utilise le charset par défaut du système (souvent CP850/CP1252 sous Windows FR)
	            // pour bien afficher les accents.
	            try (BufferedReader reader = new BufferedReader(
	                    new InputStreamReader(process.getInputStream(), Charset.defaultCharset()))) {
	                String line;
	                while ((line = reader.readLine()) != null) {
	                    System.out.println(line);
	                }
	            }
	 
	            int exitCode = process.waitFor();
	            System.out.println("=== Script terminé avec le code de sortie : " + exitCode + " ===");
//	            System.exit(exitCode);
	 
	        } catch (Exception e) {
	            System.err.println("Erreur lors de l'exécution du script PowerShell : " + e.getMessage());
	            e.printStackTrace();
//	            System.exit(2);
	        }		
/*		ProcessBuilder pb = new ProcessBuilder("powershell.exe", "-ExecutionPolicy", "Bypass", "-File", scriptPath);
		Process process = pb.start();

		// Lecture de la sortie (standard et erreur)
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			log.info( line );
//		    System.out.println(line);
		}
		*/
	}
	
	
}

