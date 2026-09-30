<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="javax.servlet.jsp.PageContext" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="org.compiere.util.Env" %>
<%@ page import="org.compiere.util.TimeUtil" %>
<%@ page import="solstice.custom.IUserInfo" %>
<%@ page import="solstice.process.CRJavaHelper" %>
<%@ page import="solstice.web.PageWebSolstice" %>
<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>

<%@ page import="org.compiere.model.MPInstance" %>
<%@ page import="org.compiere.model.MProcess" %>
<%@ page import="org.compiere.model.MSystem" %>
<%@ page import="org.compiere.process.ProcessInfoParameter" %>

<%@ page import="com.crystaldecisions.sdk.occa.report.application.OpenReportOptions" %>
<%@ page import="com.crystaldecisions.sdk.occa.report.application.ReportClientDocument" %>
<%@ page import="com.crystaldecisions.sdk.occa.report.lib.ReportSDKException" %>
<%@ page import="com.crystaldecisions.sdk.occa.report.exportoptions.*" %>
<%@ page import="com.crystaldecisions.sdk.occa.report.application.PrintOutputController" %>

<%@ page import="java.io.*" %>
 
<%@ page extends="solstice.web.PageWebSolstice" %>

<%!	

	private ReportClientDocument reportClientDocument;

	private static ProcessInfoParameter[] processInfoParameter;

    /**
     * Load a report and show it in the viewer.
     * @return whether a report was successfully displayed.
     */
    private boolean showReport ( int AD_PInstance_ID ) throws Exception
    {
        try
        {
            loadReport ( AD_PInstance_ID );

            if (reportClientDocument != null) {
                System.out.println("* DEBUG * setDatabaseLogon  " );

            	setDatabaseLogon ();

                System.out.println("* DEBUG * setParameterFieldValues " );
				setParameterFieldValues ();
    			try 
    			{
                    System.out.println("* DEBUG * setReportSource " );
	                setReportSource ();
    			} 
    			catch (Exception e) 
    			{
    				System.out.println(e);
    			}
               
                return true;
            }
        }
        catch (ReportSDKException e)
        {
            String localizedMessage = e.getLocalizedMessage ();
            int errorCode = e.errorCode ();
            
            String title = "Problem showing report";
            String message = localizedMessage + "\nError code: " + errorCode;
        }
        return false;
    }

    private MPInstance instance;
    private MProcess process;
    
    private void loadReport ( int AD_PInstance_ID ) throws ReportSDKException
    {
        // String reportFilePath = "C:\\Solstice\\Sources\\crystalReports\\";
        
       // String reportFilePath = "\\\\solstice\\Rapport\\"; 

        String reportFilePath = PgiUtil.getSolsticeParameter(Env.getCtx(), "CrystalReportShareDirectory");
        
        System.out.println("* DEBUG * reportFilePath " + reportFilePath );

        instance = new MPInstance( Env.getCtx(), AD_PInstance_ID, null );
        process = new MProcess( Env.getCtx(), instance.getAD_Process_ID(), null);
        
        if ( ! reportFilePath.endsWith("\\"))
        	reportFilePath = reportFilePath + "\\";
        
        
        reportFilePath = reportFilePath + process.getValue() + ".rpt";
        
        System.out.println("* DEBUG * reportFilePath " + reportFilePath );
        
//        reportFilePath = reportFilePath + "LstFeuillesManquantesV2008.rpt";
//        reportFilePath = reportFilePath + "LstFeuillesManquantesVXI.rpt";
        
        

        // Create a new client document and use it to open the desired report.
        System.out.println("* DEBUG *  new ReportClientDocument " );

        reportClientDocument = new ReportClientDocument ();

        System.out.println("* DEBUG *  setReportAppServer " );

        reportClientDocument.setReportAppServer(ReportClientDocument.inprocConnectionString);

        System.out.println("* DEBUG * open report " );
        reportClientDocument.open (reportFilePath, OpenReportOptions._openAsReadOnly);
    	
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

		MSystem system = MSystem.get(Env.getCtx());
//		DB.getDatabase().getDriver().getClass()
        String connectionURL = "jdbc:jtds:sqlserver://Solstice:1433/solstice;instance=SQLEXPRESS" ; //system.getDBAddress(true);
        String driverName = "net.sourceforge.jtds.jdbc.Driver";
        String jndiName = "";

//        DB_SqlServer[jdbc:jtds:sqlserver://Solstice:1433/solstice]
        	
        CRJavaHelper.changeDataSource(reportClientDocument, "solstice", "solstice", connectionURL, driverName, jndiName);

    	CRJavaHelper.logonDataSource (reportClientDocument, "solstice", "solstice");
    }
    
    /**
     * Set values for parameter fields in the report document.
     * @throws ReportSDKException if there is a problem setting parameter field values. 
     */
    private void setParameterFieldValues () throws ReportSDKException
    {
    	// TODO Populate the parameter fields in the report document here, to
    	// view the report without prompting the user for parameter values.  For
    	// more information about this feature, refer to the documentation.
    	// For example:
        //  CRJavaHelper.addDiscreteParameterValue(reportClientDocument, "subreportName", 
    	//			"parameterName", newValue);
    	// will populate the discrete parameter "parameterName" under the subreport
    	// "subreportName" with a new value (newValue).  To set a parameter in the main
    	// report, use an empty string ("") instead of "subreportName".
    	
//		boolean existOrg = false;
    	if ( processInfoParameter == null)	return;
    	
		for (int i = 0; i < processInfoParameter.length; i++)
		{
			String parameterName = processInfoParameter[i].getParameterName();

			if (parameterName.trim().equals("AD_Org_ID"))
			{
				System.out.println( processInfoParameter[i].getParameterAsInt() );
			}

			if ( processInfoParameter[i].getParameter() != null )
				CRJavaHelper.addDiscreteParameterValue( reportClientDocument, "", parameterName, processInfoParameter[i].getParameter() );
		}
   	
		String sql
				= "select *"
					 + " from AD_Process_Para"
					 + " where AD_Process_ID = " + process.getAD_Process_ID()
					 + " and Not exists( select 1 from AD_PINSTANCE_PARA WHERE AD_PINSTANCE_ID = " + instance.getAD_PInstance_ID() + " and AD_PINSTANCE_PARA.Parametername = AD_Process_Para.ColumnName ) "
					 + " order by seqNo";
			try 
			{	

				PreparedStatement stmt = DB.prepareStatement(sql, null);
   				ResultSet rs = stmt.executeQuery();
   				while(rs.next())
   				{
   					CRJavaHelper.addDiscreteParameterValue( reportClientDocument, "", rs.getString("ColumnName"), "" );
				}
			} 
			catch (Exception e) 
			{
				System.out.println(e);
			}

			

    }
    
    /**
     * Bind the report document to the report viewer so that the report is displayed.
     */
    private void setReportSource () throws Exception
    {
	
/*		

		exportOptions.DiskFileName = "C:\dossier\nomdu.pdf";

*/		
//	    reportClientDocument.getReportSource ().export( exportOptions , RequestContext);
/*
	    ReportExportControl exportControl = new ReportExportControl();
	    exportControl.setReportSource(session.getAttribute("reportSource"));

	    //Some formats allow you to set certain options.

	    ExportOptions exportOptions = new ExportOptions();
	    exportControl.setExportOptions(exportOptions);
	    exportControl.setExportAsAttachment(true);
	    exportControl.setOwnForm(true);
	    exportControl.setOwnPage(true);

	    String selectedFormat = (String) session.getAttribute("exportFormat");
	    if(selectedFormat.equals("0")/ *Crystal Report* /){
	        exportOptions.setExportFormatType(ReportExportFormat.crystalReports);
	    }
	    else if(selectedFormat.equals("1")/ *Word* /){
	        exportOptions.setExportFormatType(ReportExportFormat.MSWord);
	    }
	    else if(selectedFormat.equals("2")/ *Excel* /){
	        exportOptions.setExportFormatType(ReportExportFormat.MSExcel);
	    }
	    else if(selectedFormat.equals("3")/ *Rich Text Format* /){
	        exportOptions.setExportFormatType(ReportExportFormat.RTF);
	    }
	    else if(selectedFormat.equals("5")/ *PDF* /){
	        exportOptions.setExportFormatType(ReportExportFormat.PDF);
	    }
	    else if(selectedFormat.equals("6")/ *Excel without formatting* /){
	        exportOptions.setExportFormatType(ReportExportFormat.recordToMSExcel);
	    }
	    else if(selectedFormat.equals("7")/ *Text* /){
	        exportOptions.setExportFormatType(ReportExportFormat.text);
	    }
	    else if(selectedFormat.equals("8")/ *CSV* /){
	        exportOptions.setExportFormatType(ReportExportFormat.characterSeparatedValues);
	    }
	    else 
	        exportOptions.setExportFormatType(ReportExportFormat.PDF);

	    exportControl.processHttpRequest(request, response, getServletContext(), null);
	    exportControl.dispose();
*/
	    PrintOutputController printOutputController = reportClientDocument.getPrintOutputController();

	    ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) printOutputController.export(ReportExportFormat.PDF);
	    
	    byte byteArray[] = new byte[byteArrayInputStream.available()];
	   
   		System.out.println("* DEBUG * create file " + "C:\\mm.pdf" );

	    File file = new File("C:\\Solstice\\mm.pdf");
	    FileOutputStream fileOutputStream = new FileOutputStream(file);
	   
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(byteArrayInputStream.available());
	    int x = byteArrayInputStream.read(byteArray, 0, byteArrayInputStream.available());
	   
	    byteArrayOutputStream.write(byteArray, 0, x);
	    byteArrayOutputStream.writeTo(fileOutputStream);
	   
	    byteArrayInputStream.close();
	    byteArrayOutputStream.close();
	    fileOutputStream.close();
	    
	    
    }
    
    public String GeneratePage( ) throws Exception 
	{

   		System.out.println("* ERROR * AD_PInstance_ID " + request.getParameter("AD_PInstance_ID") );
	
		String trxName = null;
		String liste = "";
		this.setPageId("EndOfYearIndex");
		this.setSecurityCheck( true );
		this.setTemplate("Confirmation.jtpl");
		if ( SecurityCheck() )
		{
	
			IUserInfo userInfo = PageWebSolstice.getUserInfo(pageContext);
	
	   		System.out.println("* ERROR * UserInfo " + userInfo.getUserId() );
	
			String action = request.getParameter("action");
	

			String AD_PInstance_ID = request.getParameter("AD_PInstance_ID");

			String error = request.getParameter("error");
			if ( error != null )
			{
				this.setTemplate("Error.jtpl");
				tpl.assign("ERRORMSG", "Erreur : le fichier demandé n'existe pas...");
				tpl.parse("main");
				return (tpl.out());
			
			}
				
			if(action != null)
			{
				System.out.println("* DEBUG * action " + action );
				System.out.println("* DEBUG * AD_PInstance_ID " + AD_PInstance_ID );
			
			
			}

			showReport( Integer.parseInt( AD_PInstance_ID ) );
		}
		return "";
	}
	

	
%>