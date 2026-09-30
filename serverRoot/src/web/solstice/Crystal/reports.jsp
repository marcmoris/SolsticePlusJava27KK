<%@page import="java.io.IOException"%>
<%@page import="org.compiere.util.DB"%>
<%@page import="org.compiere.db.CConnection"%>

<%@page import="org.compiere.model.MRole"%>

<%@page import="java.sql.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.ParseException"%>

<%@page import="javax.servlet.ServletException"%>
<%@page import="javax.servlet.http.HttpServlet"%>
<%@page import="javax.servlet.http.HttpServletRequest"%>
<%@page import="javax.servlet.http.HttpServletResponse"%>
<%@page import="java.math.BigDecimal"%>

<%@page import="com.crystaldecisions.report.web.viewer.ReportExportControl"%>
<%@page import="com.crystaldecisions.sdk.occa.report.application.ReportClientDocument"%>
<%@page import="com.crystaldecisions.sdk.occa.report.application.DatabaseController"%>
<%@page import="com.crystaldecisions.sdk.occa.report.application.SubreportController"%>
<%@page import="com.crystaldecisions.sdk.occa.report.data.*"%>
<%@page import="com.crystaldecisions.sdk.occa.report.exportoptions.ExportOptions"%>
<%@page import="com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat"%>
<%@page import="com.crystaldecisions.sdk.occa.report.lib.*"%>

<%@page import="solstice.utils.PgiUtil"%>
<%@page import="org.compiere.util.Env"%>



<%! private int pinstanceId; %>
<%! private String reportPath; %>
<%! private String reportServer; %>
<%! private String canShowParam; %>
<%! private String showParam; %>
<%! private String needParam; %>
<%! private String trxName; %>
<%!	private String ad_RoleID; %>

<%!	private String paramReportName; %>

<%!
    private ParameterFieldValue createValue(String str, String strTo, 
            Number number, Number numberTo, Date date, Date dateTo)
    {
        ParameterFieldValue value = null;
        if(str != null)
        {
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

private void getAutorization()
{
	MRole.getDefault().setAD_User_ID(100);
	String reponse =  MRole.getDefault().getOrgWhere(false);
	    	
	System.out.println("********** Inexistant *****" + reponse);
}

%>

<%!

	    private String getOrgId(int ADRoleID)
		{
			String Chaine = "";
			try 
			{
	    		
				String sql
   				= "select AD_ORG_ID from AD_Role_OrgAccess"
   					 + " where AD_Role_ID = " + ADRoleID
   					 + " and isActive = 'Y'";
				
				PreparedStatement stmt = DB.prepareStatement(sql, trxName);
   				ResultSet rs = stmt.executeQuery();
   				while(rs.next())
   				{
   					Chaine += rs.getBigDecimal("AD_ORG_ID").toString() + ":";
   				}
   			}
			catch (Exception e) 
			{
				System.out.println(e);
			}
   			return Chaine;		
		}
		
%>

<%!

private Fields getParameterFields(Fields old)
		{
			Fields fields = new Fields();
			try 
			{	
//				System.out.println("*******************" + this.pinstanceId);
				for(int i = 0; i < old.size(); i++)
				{
					String parameterName = ((IParameterField)old.elementAt(i)).getName();

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
	   	
	   				PreparedStatement stmt = DB.prepareStatement(sql, trxName);
	   				ResultSet rs = stmt.executeQuery();
	   				
	   				boolean sendNull = true;
	   				boolean existOrg = false;
	   				
	   				// Tant qu'il y a des valeurs
	   				while(rs.next())
	   				{
	   					 // On crée une valeur et on l'ajoute à la liste des valeurs pour ce
	   					 // paramètre
						existOrg = true;
						if (parameterName.trim().equals("AD_Org_ID"))
	   					{
							System.out.println(rs.getBigDecimal("P_Number"));
						}
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
	   				
	   				if ((parameterName.trim().equals("AD_Org_ID")) && (existOrg == false))
	   				{
	   					if (ad_RoleID != null)
	   					{
//	   						System.out.println("**********" + ad_RoleID);
	   						String rep = this.getOrgId(Integer.parseInt(ad_RoleID));
							String[] array = rep.split(":");
		
							for(int q = 0; q < array.length; q++)
							{
//								System.out.println("**********" + array[q]);
								ParameterFieldValue valuex = this.createValue(null,null,new BigDecimal(array[q]),null,null,null);
	   			    			values.add(valuex);
							}

	   					}
	   			    	sendNull = false;
	   				}
	   				
//	   				System.out.println("**********************" + i);
	   				if(sendNull)
	   				{
	      				String typeString = ((IParameterField)old.elementAt(i)).getType().toString();
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
			} 
			catch (Exception e) 
			{
				System.out.println(e);
			}
			return fields;
		}

%>

<%!

    private ReportClientDocument createReport() throws ReportSDKException, SQLException
    {
        // On récupère le nom du rapport selon le numéro de l'instance dans la table AD_Process
        String sql
        	= " select Value, CanShowParam, NeedParam, ShowParam "
            + " from AD_Process"
            + " where exists ("
            + "   select *"
            + "   from AD_PInstance"
            + "   where AD_PInstance_ID = " + String.valueOf(this.pinstanceId)
            + "   and AD_Process_ID = AD_Process.AD_Process_ID"
            + " )";

//	System.out.println("* DEBUG * CreateReport " + sql);

        String reportName = null;

        try
        {
//            System.out.println("Instanciation du ReportClientDocument");
   	        ReportClientDocument rptDoc = new ReportClientDocument();
   	        PreparedStatement stmt = DB.prepareStatement(sql, trxName);
   	        ResultSet rs = stmt.executeQuery();
   	        String rptPath = this.reportPath;
   	        if(rs.next())
   	        {
   	        	System.out.println("* DEBUG * Appel du rapport : " + paramReportName );
  	        
   	        	if ( paramReportName != null && paramReportName.length() != 0  )
   	        		reportName = paramReportName + ".rpt";
   	        	else
	   	            reportName = rs.getString(1) + ".rpt";
   	            rptPath += reportName;
   	            canShowParam = rs.getString(2);
   	            needParam = rs.getString(3);
   	            showParam = rs.getString(4);
   	            System.out.println("Lancement du rapport : " + rptPath);
   	        }
   	        
   	        rptDoc.setReportAppServer(this.reportServer);
   	        rptDoc.open(rptPath, 1);
   			
   			//Recréer la connection en ADO
   			ConnectionInfo connectionInfo = createConnectionInfo();
   			setReportConnectionInfo(rptDoc, connectionInfo);
   			
   			//Version temporaire, login direct avec les settings de la BD
   			//logonToReport(rptDoc);

			
	        return rptDoc;
        }
        catch (ReportSDKException e)
        {
            System.out.println(e);
            e.printStackTrace(System.out);
            throw(e);
        }
        catch (SQLException e)
        {
            System.out.println(e);
            e.printStackTrace(System.out);
            throw e;
        }
    }
%>

<%!
    private void initPath()
    {
        String sql 
        	= " select ReportPath, ReportServer"
            + " from P_System_Parameters";

//		System.out.println("* DEBUG * initPath " + sql);
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, trxName);
            ResultSet rs = stmt.executeQuery() ;
            if(rs.next())
            {
                this.reportPath = rs.getString("ReportPath");
                this.reportServer = rs.getString("ReportServer");
            }
        }
        catch (SQLException e)
        {
            this.reportPath = null;
            this.reportServer = null;
            System.out.println(e);
        }
        
        System.out.println("Initialisation terminée");
        System.out.println("-> reportPath = " + this.reportPath);
        System.out.println("-> reportServer = " + this.reportServer);
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
	final String TABLE_NAME_QUALIFIER = PgiUtil.getSolsticeParameter(Env.getCtx(), "DB_Table_Name_Qualifier"); //"demo.dbo.";
	final String DB_PORT = PgiUtil.getSolsticeParameter(Env.getCtx(), "DB_Port"); // ""; // ,50000
	
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
	
	if ( request != null && request.getParameter("ReportName") != null)
    	paramReportName = (request.getParameter("ReportName"));
	else
		paramReportName = null;
	
	System.out.println("* DEBUG * Appel du rapport : "  + request.getParameter("ReportName") );
	
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