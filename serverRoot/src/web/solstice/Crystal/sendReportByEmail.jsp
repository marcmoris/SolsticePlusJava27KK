<%
/* 
 * Applies to versions:	XI Release 2
 * Date Created: December 2005
 * Description: This sample demonstrates how to export a report to a stream (server-side exporting).  The sample
 * 				then demonstrates how to use the Java I/O libraries to write the exported result to the local 
 * 				file system or the browser.
 * 				NOTE: If the report is based on a secured database the database login credentials
 * 				will need to be set before calling the export method below.  Also, if the report
 * 				has parameters, then values will need to be set for the report before export method can
 * 				be called or an error will be thrown.  See the ViewReportParameters or ViewReportLogon samples 
 * 				for samples on how to set parameters and login credentials through the JRC SDK.   
 * Author: CW.
 */
%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@page import="java.io.IOException"%>
<%@page import="org.compiere.util.DB"%>
<%@page import="org.compiere.db.CConnection"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.math.BigDecimal"%>

<%//Crystal Java Reporting Component (JRC) imports.%>

<%@page import="com.crystaldecisions.report.web.viewer.ReportExportControl"%>
<%@page import="com.crystaldecisions.sdk.occa.report.application.ReportClientDocument"%>
<%@page import="com.crystaldecisions.sdk.occa.report.application.DatabaseController"%>
<%@page import="com.crystaldecisions.sdk.occa.report.application.SubreportController"%>
<%@page import="com.crystaldecisions.sdk.occa.report.data.*"%>
<%@page import="com.crystaldecisions.sdk.occa.report.exportoptions.ExportOptions"%>
<%@page import="com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat"%>
<%@page import="com.crystaldecisions.sdk.occa.report.lib.*"%>

<%@page import="com.crystaldecisions.sdk.occa.report.exportoptions.*" %>

<%//@page import="com.crystaldecisions.reports.sdk.ParameterFieldController" %>


<%//Java imports. %>
<%@page import="java.io.*" %>

<%
//Reports can be opened from the relative location specified in the CRConfig.xml, or the report location
//tag can be removed to open the reports as Java resources or using an absolute path (absolute path not recommended
//for Web applications).
final String REPORT_NAME = "LstLivretDeTemps.rpt";
 
final String EXPORT_FILE = "myExportedReport.pdf";
final String EXPORT_LOC = "E:\\PGI\\";
%>

<%! private int pinstanceId; %>
<%! private String reportPath; %>
<%! private String reportServer; %>
<%! private String canShowParam; %>
<%! private String showParam; %>
<%! private String needParam; %>

<%

try {

	
	//Open report.
	ReportClientDocument reportClientDoc = new ReportClientDocument();
    reportClientDoc.setReportAppServer("ntap2");
	reportClientDoc.open(REPORT_NAME, 0);

//	String userName = "fpgi99"; 
//	String password = "Pgi027";
//	reportClientDoc.getDatabaseController().logon(userName, password);

    ConnectionInfo connectionInfo = createConnectionInfo();
   	setReportConnectionInfo(reportClientDoc, connectionInfo);

	ReportExportControl exportControl = new ReportExportControl();
	exportControl.setReportSource(reportClientDoc.getReportSource());
	exportControl.setExportAsAttachment(false);

	ExportOptions exportOptions = new ExportOptions();
	exportOptions.setExportFormatType(ReportExportFormat.PDF);
	exportControl.setExportOptions(exportOptions);
	exportControl.setParameterFields(this.getParameterFields(reportClientDoc.getDataDefinition().getParameterFields()));
	exportControl.setEnableParameterPrompt(true);
	exportControl.refresh();

	String EXPORT_OUTPUT = EXPORT_LOC + EXPORT_FILE;
	out.println("Exporting to " + EXPORT_OUTPUT);

	exportControl.processHttpRequest(request, response, getServletConfig().getServletContext(), EXPORT_OUTPUT);
		
//	exportControl.dispose();


/*
	ParameterFieldController paramFieldController = reportClientDoc.getDataDefController().getParameterFieldController();
			
	//SINGLE VALUE DISCRETE PARAMETERS.
	paramFieldController.setCurrentValue("", "P_Period_ID", new Double(1001754));
	paramFieldController.setCurrentValue("", "P_Distribution_Booklet_ID", new Double(0));
	paramFieldController.setCurrentValue("", "FormulaireVide", new String("N"));
*/
/*	paramFieldController.setCurrentValue("", "StringParam", new String("Hello"));
	paramFieldController.setCurrentValue("", "BooleanParam", new Boolean(true));
	paramFieldController.setCurrentValue("", "P_Period_ID", new Double(1001754));
	paramFieldController.setCurrentValue("", "NumberParam", new Integer(123));
*/


	//NOTE: If parameters or database login credentials are required, they need to be set before.
	//calling the export() method of the PrintOutputController.
			
	//Export report and obtain an input stream that can be written to disk.
	//See the Java Reporting Component Developer's Guide for more information on the supported export format enumerations
	//possible with the JRC.

	ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream)reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
			
	//Release report.
	reportClientDoc.close();
	
	//These utility methods below demonstrate how to use the Java I/O libraries to write the input stream content
	//directly to the browser, or the server's file system.  Note: We are now working with APIs completely outside of
	//Crystal at this point:  						
//	writeToBrowser(byteArrayInputStream, response, "application/pdf", EXPORT_FILE);
	
	//Write file to disk...
	String EXPORT_OUTPUT = EXPORT_LOC + EXPORT_FILE;
	out.println("Exporting to " + EXPORT_OUTPUT);
	writeToFileSystem(byteArrayInputStream, EXPORT_OUTPUT);

		
}
catch(ReportSDKException ex) {	
	out.println(ex);
}
catch(Exception ex) {
	out.println(ex);			
}
%>

<%!
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
%>	

<%!
   /*
	* Utility method that demonstrates how to write an input stream to the server's local file system.  
	*/
	private void writeToBrowser(ByteArrayInputStream byteArrayInputStream, HttpServletResponse response, String mimetype, String exportFile) throws Exception {
	
		//Create a byte[] the same size as the exported ByteArrayInputStream.
		byte[] buffer = new byte[byteArrayInputStream.available()];
		int bytesRead = 0;
		
		//Set response headers to indicate mime type and inline file.
		response.reset();
		response.setHeader("Content-disposition", "inline;filename=" + exportFile);
		response.setContentType(mimetype);
		
		//Stream the byte array to the client.
		while((bytesRead = byteArrayInputStream.read(buffer)) != -1) {
			response.getOutputStream().write(buffer, 0, bytesRead);	
		}
		
		//Flush and close the output stream.
		response.getOutputStream().flush();
		response.getOutputStream().close();
		
	}
%>	


<%!
    private ParameterFieldValue createValue(String str, String strTo, 
            Number number, Number numberTo, Date date, Date dateTo)
    {

        ParameterFieldValue value = null;
        if(str != null)
        {
System.out.println("str " +str);
            if(strTo != null)
            {
                value = new ParameterFieldRangeValue();
                ((ParameterFieldRangeValue)value).setBeginValue(str);
                ((ParameterFieldRangeValue)value).setEndValue(strTo);
            }
            else
            {
                value = new ParameterFieldDiscreteValue();
                ((ParameterFieldDiscreteValue)value).setValue(str);
            }
        }

        if(number != null)
        {
System.out.println("number " + number);

            //DecimalFormat format = new DecimalFormat("#0");
            if(numberTo != null)
            {
                value = new ParameterFieldRangeValue();
                ((ParameterFieldRangeValue)value).setBeginValue(new Integer((int)number.doubleValue()));
                ((ParameterFieldRangeValue)value).setEndValue(new Integer((int)numberTo.doubleValue()));
            }
            else
            {
                value = new ParameterFieldDiscreteValue ();
                ((ParameterFieldDiscreteValue)value).setValue(new Integer((int)number.doubleValue()));
            }
        }

        if(date != null)
        {
            if(dateTo != null)
            {
                value = new ParameterFieldRangeValue();
                ((ParameterFieldRangeValue)value).setBeginValue(date);
                ((ParameterFieldRangeValue)value).setEndValue(dateTo);
            }
            else
            {
                value = new ParameterFieldDiscreteValue ();
                ((ParameterFieldDiscreteValue)value).setValue(date);
            }
        }
        return value;
    }
%>

<%!
	private Fields getParameterFields(Fields old)
	{
		Fields fields = new Fields();
		try 
		{	
			for(int i = 0; i < old.size()  ; i++)
			{
				String parameterName = ((IParameterField)old.elementAt(i)).getName();
								
	System.out.println("parameterName " + parameterName);

				if(parameterName.trim().equals("AD_PInstance_ID") || parameterName.trim().equals("AfficherParametre"))
				{
					System.out.println(" ");
				}

				else
				{
   				String sql
   				= "select P_String, P_String_To, P_Number, P_Number_To, P_Date, P_Date_To"
   					 + " from AD_PInstance_Para"
   					 + " where AD_PInstance_ID = " + this.pinstanceId
   					 + " and ParameterName = '" + parameterName + "'"
   					 + " order by seqNo";
   	

   				ParameterField field = new ParameterField();
   				field.setName(parameterName);
   				field.setReportName("");
   				Values values = new Values();
   	
   				PreparedStatement stmt = DB.prepareStatement(sql);
   				ResultSet rs = stmt.executeQuery();
   				
   				boolean sendNull = true;
   				
   				// Tant qu'il y a des valeurs
   				while(rs.next())
   				{

   					 // On crée une valeur et on l'ajoute à la liste des valeurs pour ce
   					 // paramètre
   					ParameterFieldValue value = this.createValue(
   								rs.getString("P_String"),
   								rs.getString("P_String_To"),
   								rs.getBigDecimal("P_Number"),
   								rs.getBigDecimal("P_Number_To"),
   								rs.getTimestamp("P_Date"),
   								rs.getTimestamp("P_Date_To"));
   					 

   					 if(value != null)
   					 {
      					 values.add(value);
      					 sendNull = false;
   					 }
   				}
   				
   				if(sendNull)
   				{
      				String typeString = ((IParameterField)old.elementAt(i)).getType().toString();
	System.out.println("typeString " + typeString);

      				if("xsd:decimal".equals(typeString))

      				{

         				// On renvoit 0 comme paramètre null
         				ParameterFieldDiscreteValue value = new ParameterFieldDiscreteValue();
         				value.setValue(new Integer(0));
         				values.add(value);
      				}
      				else if("xsd:string".equals(typeString))
      				{
         				// On renvoit "" comme paramètre null
         				ParameterFieldDiscreteValue value = new ParameterFieldDiscreteValue();
         				value.setValue("");
         				values.add(value);
      				}
      				else if("xsd:date".equals(typeString))
      				{
         				// On renvoit 1900-01-01 comme paramètre null
         				SimpleDateFormat format = 
            				new SimpleDateFormat("yyyy MM dd HH:mm:ss");
            			Date nullDate = format.parse("1900 01 01 00:00:00");
         				
         				ParameterFieldDiscreteValue value = new ParameterFieldDiscreteValue();
         				value.setValue(nullDate);
         				values.add(value);
      				}

   				}
   				
   				// On associe la/les valeurs au paramètre courrant
   				field.setCurrentValues(values);
   				fields.add(field);
				}
			} 
/*			
			if(needParam.trim().equals("Y") && showParam.equals("N"))
			{
				ParameterField field1 = new ParameterField();
				field1.setName("AD_PInstance_ID");
				field1.setReportName("");
				Values values1 = new Values();
				
				ParameterFieldValue value1 = this.createValue(
								null,
								null,
								new BigDecimal(this.pinstanceId),
								null,
								null,
								null);
				values1.add(value1);
				field1.setCurrentValues(values1);
				fields.add(field1);
			}
			else if(showParam.equals("Y"))
			{
				ParameterField field1 = new ParameterField();
				field1.setName("AD_PInstance_ID");
				field1.setReportName("");
				Values values1 = new Values();
				
				ParameterFieldValue value1 = this.createValue(
								null,
								null,
								new BigDecimal(this.pinstanceId),
								null,
								null,
								null);
				values1.add(value1);
				field1.setCurrentValues(values1);
				fields.add(field1);

				ParameterField field2 = new ParameterField();
				field2.setName("AfficherParametre");
				field2.setReportName("");
				Values values2 = new Values();
				
				ParameterFieldValue value2 = this.createValue(
								"Y",
								null,
								null,
								null,
								null,
								null);
				values2.add(value2);
				field2.setCurrentValues(values2);
				fields.add(field2);
				
			}
*/
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
		return fields;
	}

%>
<%!
    private void setReportConnectionInfo(ReportClientDocument report, ConnectionInfo connectionInfo) throws ReportSDKException
    {
		DatabaseController dbController = report.getDatabaseController();
		SubreportController srController = report.getSubreportController();
		
		Database db = (Database)dbController.getDatabase();

		//Spécifier la nouvelle connection pour les tables du rapport principal
		setTablesConnectionInfo(report, db, connectionInfo, null);
		
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
				setTablesConnectionInfo(report, db, connectionInfo, subReportName);
				
			}
			catch (IndexOutOfBoundsException exc)
			{
				endReached = true;
			}
			i++;
		}

    }
%>

<%!
	final String TABLE_NAME_QUALIFIER = "solstice.dbo.";
	final String DB_PORT = ",50000";
	
    private void setTablesConnectionInfo(ReportClientDocument report, Database db, ConnectionInfo connectionInfo, String subReportName) throws ReportSDKException
    {
		Table originalTable;
		Table clonedTable;
		Tables tables = db.getTables();
		int i = 0;
		boolean endReached = false;
		//Cette boucle est spéciale pcq il n'y a pas de moyen d'obtenir la longeur de Tables, ainsi j'utilise le catch de
		//l'exception IndexOutOfBoundsException pour faire ma sentinelle
		while (!endReached)
		{
			try
			{
				originalTable = (Table)tables.getTable(i);
				clonedTable = (Table)originalTable.clone(true);
				clonedTable.setConnectionInfo(connectionInfo);

				//Change properties that are different from the original datasource.
				clonedTable.setQualifiedName(TABLE_NAME_QUALIFIER + clonedTable.getName());

				//Si le nom du sous-rapport est passé à la fonction, ça veut dire que la base de donnée utilisé fait partie d'un
				//sous rapport.  Ensuite on spécifie le nouvel emplacement de la table (la connection)
				if (subReportName == null)
				{
					report.getDatabaseController().setTableLocation(originalTable, clonedTable);
				}
				else
				{
					report.getSubreportController().setTableLocation(subReportName, originalTable, clonedTable);
				}
			}
			catch (IndexOutOfBoundsException exc)
			{
				endReached = true;
			}
			i++;
		}
    }
%>

<%!
    private void logonToReport(ReportClientDocument report) throws ReportSDKException
    {
		String userName = CConnection.get().getDbUid(); 
		String password = CConnection.get().getDbPwd();
		report.getDatabaseController().logon(userName, password);
    }
%>

<%!
    private ConnectionInfo createConnectionInfo()
    {
		//Obtenir de compiere les informations de connection des rapports
		String databaseName = CConnection.get().getDbName(); 
		String databaseServer = CConnection.get().getDbHost() + DB_PORT;
		String userName = CConnection.get().getDbUid(); 
		String password = CConnection.get().getDbPwd();
	
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
%>

<%
//	System.out.println("* DEBUG * Debut page web");
	this.pinstanceId = Integer.parseInt(request.getParameter("AD_PInstance_ID"));
	this.initPath();
//	System.out.println("* DEBUG * this.pinstanceId" + request.getParameter("AD_PInstance_ID"));
	
	if ( request != null && request.getParameter("Ad_Role_ID") != null)
		ad_RoleID = request.getParameter("Ad_Role_ID").trim();
	
	String ExportFormat = "PDF"; 
	if ( request != null && request.getParameter("Format") != null)
		ExportFormat = request.getParameter("Format").trim();

	try
	{
		response.reset();
		response.setContentType("application/pdf");
		
		ReportClientDocument doc = createReport();
		ReportExportControl exportControl = new ReportExportControl();
		exportControl.setReportSource(doc.getReportSource());
		exportControl.setExportAsAttachment(false);
		
		ExportOptions exportOptions = new ExportOptions();
		if ( ExportFormat.equals("MSExcel"))
		{
			response.setContentType("application/vnd.ms-excel");
			exportOptions.setExportFormatType(ReportExportFormat.MSExcel);
		}
		else if ( ExportFormat.equals("MSWord"))
		{
			response.setContentType("application/vnd.ms-word");
			exportOptions.setExportFormatType(ReportExportFormat.MSWord);
		}
		else if ( ExportFormat.equals("text"))
		{
			response.setContentType("text/plain");
			exportOptions.setExportFormatType(ReportExportFormat.text);
		}
		else
		{
			response.setContentType("application/pdf");
			exportOptions.setExportFormatType(ReportExportFormat.PDF);
		}

		exportControl.setExportOptions(exportOptions);
		exportControl.setParameterFields(this.getParameterFields(doc.getDataDefinition().getParameterFields()));
		exportControl.setEnableParameterPrompt(false);
		exportControl.refresh();
		exportControl.processHttpRequest(request, response, getServletConfig().getServletContext(), null);
		
		exportControl.dispose();

	}
	catch (Exception e)
	{
		System.out.println(e);
	}
%>
