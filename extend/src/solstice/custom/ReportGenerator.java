///**
// * 
// */
//package solstice.custom;
//
//import java.io.ByteArrayInputStream;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Hashtable;
//
//import org.compiere.db.CConnection;
//
//import com.crystaldecisions.sdk.occa.report.application.DatabaseController;
//import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
//import com.crystaldecisions.sdk.occa.report.application.SubreportController;
//import com.crystaldecisions.sdk.occa.report.data.ConnectionInfo;
//import com.crystaldecisions.sdk.occa.report.data.ConnectionInfoKind;
//import com.crystaldecisions.sdk.occa.report.data.Database;
//import com.crystaldecisions.sdk.occa.report.data.Fields;
//import com.crystaldecisions.sdk.occa.report.data.IDataDefinition;
//import com.crystaldecisions.sdk.occa.report.data.IParameterField;
//import com.crystaldecisions.sdk.occa.report.data.ParameterField;
//import com.crystaldecisions.sdk.occa.report.data.ParameterFieldDiscreteValue;
//import com.crystaldecisions.sdk.occa.report.data.ParameterFieldRangeValue;
//import com.crystaldecisions.sdk.occa.report.data.Table;
//import com.crystaldecisions.sdk.occa.report.data.Tables;
//import com.crystaldecisions.sdk.occa.report.data.Values;
//import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
//import com.crystaldecisions.sdk.occa.report.lib.PropertyBag;
//import com.crystaldecisions.sdk.occa.report.lib.PropertyBagHelper;
//import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
//import com.crystaldecisions.sdk.occa.report.lib.ReportSDKExceptionBase;
//import com.crystaldecisions.sdk.occa.report.lib.Strings;
//
///**
// * @author frafor01
// * Cette classe sert à générer un rapport à l'aide de crystal report
// */
//public class ReportGenerator
//{
//	
//	private String reportPath = null;
//	private String reportServer = null;
//	private Hashtable<String, IParameterField> parameterFields = new Hashtable<String, IParameterField>();
//	
//	public void setReportPath(String reportPath)
//	{
//		this.reportPath = reportPath;
//	}
//	
//	public void setReportServer(String reportServer)
//	{
//		this.reportServer = reportServer;
//	}
//
//	/**
//	 * Constructeur
//	 */
//	public ReportGenerator()
//	{
//	}
//	
//	/**
//	 * Cette méthode permet d'ajouter un paramètre entier
//	 * @param name Le nom du paramètre
//	 * @param value La valeur non null
//	 * @throws NullArgumentException interdit aux paramètres nulls
//	 */
//	public void addParameter(String name, int value) throws NullArgumentException
//	{
//		if(name == null)
//			throw new NullArgumentException("name");
//		
//		ParameterField field = new ParameterField();
//		field.setName(name);
//		field.setReportName("");
//		Values values = new Values();
//		
//		ParameterFieldDiscreteValue val = new ParameterFieldDiscreteValue();
//        val.setValue(new Integer(value));
//        values.add(val);
//		
//		field.setCurrentValues(values);
//		this.parameterFields.put(name, field);
//	}
//	
//	/**
//	 * Cette méthode permet d'ajouter un interval de paramètres entiers
//	 * @param name Le nom du paramètre
//	 * @param beginValue La valeur non null du début de l'interval
//	 * @param endValue La valeur non null de la fin de l'interval
//	 * @throws NullArgumentException interdit aux paramètres nulls
//	 */
//	public void addParameter(String name, int beginValue, int endValue) throws NullArgumentException
//	{
//		if(name == null)
//			throw new NullArgumentException("name");
//		
//		ParameterField field = new ParameterField();
//		field.setName(name);
//		field.setReportName("");
//		Values values = new Values();
//		
//		ParameterFieldRangeValue val = new ParameterFieldRangeValue();
//        val.setBeginValue(new Integer(beginValue));
//        val.setEndValue(new Integer(endValue));
//        values.add(val);
//		
//		field.setCurrentValues(values);
//		this.parameterFields.put(name, field);
//	}
//	
//	/**
//	 * Cette méthode permet d'ajouter un paramètre string
//	 * @param name Le nom du paramètre
//	 * @param value La valeur non null
//	 * @throws NullArgumentException interdit aux paramètres nulls
//	 */
//	public void addParameter(String name, String value) throws NullArgumentException
//	{
//		if(name == null)
//			throw new NullArgumentException("name");
//		
//		if(name == null)
//			throw new NullArgumentException("value");
//		
//		ParameterField field = new ParameterField();
//		field.setName(name);
//		field.setReportName("");
//		Values values = new Values();
//		
//		ParameterFieldDiscreteValue val = new ParameterFieldDiscreteValue();
//        val.setValue(value);
//        values.add(val);
//		
//		field.setCurrentValues(values);
//		this.parameterFields.put(name, field);
//	}
//	
//	/**
//	 * Cette méthode permet d'ajouter un interval de paramètres strings
//	 * @param name Le nom du paramètre
//	 * @param beginValue La valeur non null du début de l'interval
//	 * @param endValue La valeur non null de la fin de l'interval
//	 * @throws NullArgumentException interdit aux paramètres nulls
//	 */
//	public void addParameter(String name, String beginValue, String endValue) throws NullArgumentException
//	{
//		if(name == null)
//			throw new NullArgumentException("name");
//		
//		if(name == null)
//			throw new NullArgumentException("startValue");
//		
//		if(name == null)
//			throw new NullArgumentException("endValue");
//		
//		ParameterField field = new ParameterField();
//		field.setName(name);
//		field.setReportName("");
//		Values values = new Values();
//		
//		ParameterFieldRangeValue val = new ParameterFieldRangeValue();
//        val.setBeginValue(beginValue);
//        val.setEndValue(endValue);
//        values.add(val);
//		
//		field.setCurrentValues(values);
//		this.parameterFields.put(name, field);
//	}
//	
//	/**
//	 * Cette méthode permet d'ajouter un paramètre date
//	 * @param name Le nom du paramètre
//	 * @param value La valeur non null
//	 * @throws NullArgumentException interdit aux paramètres nulls
//	 */
//	public void addParameter(String name, Date value) throws NullArgumentException
//	{
//		if(name == null)
//			throw new NullArgumentException("name");
//		
//		ParameterField field = new ParameterField();
//		field.setName(name);
//		field.setReportName("");
//		Values values = new Values();
//		
//		ParameterFieldDiscreteValue val = new ParameterFieldDiscreteValue();
//        val.setValue(value);
//        values.add(val);
//		
//		field.setCurrentValues(values);
//		this.parameterFields.put(name, field);
//	}
//	
//	/**
//	 * Cette méthode permet d'ajouter un interval de paramètres dates
//	 * @param name Le nom du paramètre
//	 * @param beginValue La valeur non null du début de l'interval
//	 * @param endValue La valeur non null de la fin de l'interval
//	 * @throws NullArgumentException interdit aux paramètres nulls
//	 */
//	public void addParameter(String name, Date startValue, Date endValue) throws NullArgumentException
//	{
//		if(name == null)
//			throw new NullArgumentException("name");
//		
//		ParameterField field = new ParameterField();
//		field.setName(name);
//		field.setReportName("");
//		Values values = new Values();
//		
//		ParameterFieldRangeValue val = new ParameterFieldRangeValue();
//        val.setBeginValue(startValue);
//        val.setEndValue(endValue);
//        values.add(val);
//		
//		field.setCurrentValues(values);
//		this.parameterFields.put(name, field);
//	}
//	
//	public void RemoveParameter(String name) throws NullArgumentException
//	{
//		if(name == null)
//			throw new NullArgumentException("name");
//		
//		if(this.parameterFields.containsKey(name))
//			this.parameterFields.remove(name);
//	}
//	
//	/**
//	 * Cette méthode sert à générer le rapport
//	 * @return Un stream en mémoire contenant le rapport en PDF
//	 * @throws ReportSDKExceptionBase Exception lancée par Crystal
//	 * @throws Exception Le nom du rapport et le nom du serveur sont obligatoires
//	 */
//	public ByteArrayInputStream genererRapport() throws ReportSDKExceptionBase, Exception
//	{
//		if(this.reportPath == null
//				|| this.reportServer == null)
//		{
//			throw new Exception("Le nom du rapport et le nom du serveur sont obligatoires");
//		}
//		
//		ReportClientDocument rptDoc = new ReportClientDocument();
//	        
//        rptDoc.setReportAppServer(this.reportServer);
//        rptDoc.open(this.reportPath, 1);
//        IDataDefinition dataDefinition = rptDoc.getDataDefinition();
//        dataDefinition.setParameterFields(
//        		this.getParameterFields(dataDefinition.getParameterFields()));
//
//		//Recréer la connection en ADO
//		ConnectionInfo connectionInfo = createConnectionInfo();
//		this.setReportConnectionInfo(rptDoc, connectionInfo);
//		
//		return (ByteArrayInputStream)rptDoc.getPrintOutputController().export(ReportExportFormat.PDF);
//	}
//	
//	/**
//	 * Cette méthode sert à créer l'objet de connexion à la base de données
//	 * @return Nouvelle instance de ConnectionInfo
//	 */
//    private ConnectionInfo createConnectionInfo()
//    {
//		//Obtenir de compiere les informations de connection des rapports
//		String databaseName = CConnection.get().getDbName(); 
//		String databaseServer = CConnection.get().getDbHost();
//		String userName = CConnection.get().getDbUid(); 
//		String password = CConnection.get().getDbPwd();
//	
//		//Spécifier le nouvel object de connection
//		ConnectionInfo connectionInfo = new ConnectionInfo();
//		connectionInfo.setKind(ConnectionInfoKind.SQL);
//		connectionInfo.setUserName(userName);
//		connectionInfo.setPassword(password);
//		PropertyBag attributes = new PropertyBag();
//		PropertyBag logonProperties = new PropertyBag();
//		
//		attributes.putBooleanValue(PropertyBagHelper.CONNINFO_CRQE_SQLDB, true);
//		attributes.putStringValue(PropertyBagHelper.CONNINFO_DATABASE_DLL, "crdb_ado.dll");
//		attributes.putStringValue(PropertyBagHelper.CONNINFO_SERVER_NAME, databaseServer);
//		attributes.putStringValue(PropertyBagHelper.CONNINFO_DATABASE_NAME, databaseName);
//		attributes.putStringValue(PropertyBagHelper.CONNINFO_SERVER_TYPE, "OLE DB (ADO)");
//		
//		attributes.put(PropertyBagHelper.CONNINFO_CRQE_LOGONPROPERTIES, logonProperties);
//
//		connectionInfo.setAttributes(attributes);
//		return connectionInfo;
//    }
//
//    /**
//     * Cette méthode sert à affecter la connexion au rapport et à tous ses sous-rapports
//     */
//    private void setReportConnectionInfo(ReportClientDocument report, ConnectionInfo connectionInfo) throws ReportSDKException
//    {
//		DatabaseController dbController = report.getDatabaseController();
//		SubreportController srController = report.getSubreportController();
//		
//		Database db = (Database)dbController.getDatabase();
//
//		//Spécifier la nouvelle connection pour les tables du rapport principal
//		setTablesConnectionInfo(report, db, connectionInfo, null);
//		
//		Strings subreportNames = (Strings)srController.querySubreportNames();
//		int i = 0;
//		boolean endReached = false;
//		String subReportName;
//		while (!endReached)
//		{
//			try
//			{
//				subReportName = subreportNames.getString(i);
//				db = (Database)srController.getSubreportDatabase(subReportName);
//				//Spécifier la nouvelle connection pour les tables du sous-rapport
//				setTablesConnectionInfo(report, db, connectionInfo, subReportName);
//				
//			}
//			catch (IndexOutOfBoundsException exc)
//			{
//				endReached = true;
//			}
//			i++;
//		}
//
//    }
//
//    /**
//     * Cette méthode sert à affecter la connexion à chacune des tables du rapport passé en paramètre
//     */
//    private void setTablesConnectionInfo(ReportClientDocument report, Database db, ConnectionInfo connectionInfo, String subReportName) throws ReportSDKException
//    {
//		Table originalTable;
//		Table clonedTable;
//		Tables tables = db.getTables();
//		int i = 0;
//		boolean endReached = false;
//		//Cette boucle est spéciale pcq il n'y a pas de moyen d'obtenir la longeur de Tables, ainsi j'utilise le catch de
//		//l'exception IndexOutOfBoundsException pour faire ma sentinelle
//		while (!endReached)
//		{
//			try
//			{
//				originalTable = (Table)tables.getTable(i);
//				clonedTable = (Table)originalTable.clone(true);
//				clonedTable.setConnectionInfo(connectionInfo);
//				//Si le nom du sous-rapport est passé à la fonction, ça veut dire que la base de donnée utilisé fait partie d'un
//				//sous rapport.  Ensuite on spécifie le nouvel emplacement de la table (la connection)
//				if (subReportName == null)
//				{
//					report.getDatabaseController().setTableLocation(originalTable, clonedTable);
//				}
//				else
//				{
//					report.getSubreportController().setTableLocation(subReportName, originalTable, clonedTable);
//				}
//			}
//			catch (IndexOutOfBoundsException exc)
//			{
//				endReached = true;
//			}
//			i++;
//		}
//    }
//
//    /**
//     * Cette méthode sert à obtenir la liste complète des paramètres selon une liste d'origine
//     * @param old la liste d'origine
//     * @return la nouvelle liste des paramètres
//     */
//	private Fields getParameterFields(Fields old) throws ParseException
//	{
//		Fields fields = new Fields();
//
//		for(int i = 0; i < old.size(); i++)
//		{
//			String parameterName = ((IParameterField)old.elementAt(i)).getName();
//				
//			if(this.parameterFields.containsKey(parameterName))
//			{
//  				fields.add(this.parameterFields.get(parameterName));
//			}
//			else
//			{
//   				ParameterField field = new ParameterField();
//   				field.setName(parameterName);
//   				field.setReportName("");
//				Values values = new Values();
//  				String typeString = ((IParameterField)old.elementAt(i)).getType().toString();
//  				if("xsd:decimal".equals(typeString))
//  				{
//     				// On renvoit 0 comme paramètre null
//     				ParameterFieldDiscreteValue value = new ParameterFieldDiscreteValue();
//     				value.setValue(new Integer(0));
//     				values.add(value);
//  				}
//  				else if("xsd:string".equals(typeString))
//  				{
//     				// On renvoit "" comme paramètre null
//     				ParameterFieldDiscreteValue value = new ParameterFieldDiscreteValue();
//     				value.setValue("");
//     				values.add(value);
//  				}
//  				else if("xsd:date".equals(typeString))
//  				{
//     				// On renvoit 1900-01-01 comme paramètre null
//     				SimpleDateFormat format = 
//        				new SimpleDateFormat("yyyy MM dd HH:mm:ss");
//        			Date nullDate = format.parse("1900 01 01 00:00:00");
//     				
//     				ParameterFieldDiscreteValue value = new ParameterFieldDiscreteValue();
//     				value.setValue(nullDate);
//     				values.add(value);
//  				}
//  				field.setCurrentValues(values);
//  				fields.add(field);
//			}
//			
//		} 
//		
//		return fields;
//	}
//}
