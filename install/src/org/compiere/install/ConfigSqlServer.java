/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.install;

import java.net.*;
import java.sql.*;
import org.compiere.db.*;
import org.compiere.startup.Environment;

/**
 *	Sybase Configuration
 *	
 *  @author Jorg Janke
 *  @version $Id: ConfigSqlServer.java,v 1.1 2007/07/18 14:41:03 marmor01 Exp $
 */
public class ConfigSqlServer extends Config
{

	/**
	 * 	ConfigSybase
	 *	@param data
	 */
	public ConfigSqlServer (ConfigurationData data)
	{
		super (data);
	}	//	ConfigSybase

	/** Discoverd TNS			*/
	private String[] 			p_discovered = null;
	/**	Sybase DB Info			*/
	private DB_SqlServer			p_db = new DB_SqlServer();
	
	/**
	 * 	Init
	 */
	public void init()
	{
		p_data.setDatabasePort(String.valueOf(DB_SqlServer.DEFAULT_PORT));

		//
		p_data.setDatabaseSystemPassword(true);
		// Database name
		p_data.setDatabaseName(true);
		// Database Discovered
		p_data.setDatabaseDiscovered(true);
		// Database name
		p_data.setDatabaseUser(true);

	}	//	init

	/**
	 * 	Discover Databases.
	 * 	To be overwritten by database configs
	 *	@param selected selected database
	 *	@return array of databases
	 */
	public String[] discoverDatabases(String selected)
	{
		if (p_discovered != null)
			return p_discovered;
		p_discovered = new String[]{};
		return p_discovered;
	}	//	discoveredDatabases
	
	
	/**************************************************************************
	 * 	Test
	 *	@return error message or null if OK
	 */
	public String test()
	{
		//	Database Server
		String server = p_data.getDatabaseServer();
		boolean pass = server != null && server.length() > 0
			&& server.toLowerCase().indexOf("localhost") == -1 
			&& !server.equals("127.0.0.1");
		String error = "Not correct: DB Server = " + server;
		InetAddress databaseServer = null;
		try
		{
			if (pass)
				databaseServer = InetAddress.getByName(server);
		}
		catch (Exception e)
		{
			error += " - " + e.getMessage();
			pass = false;
		}
		signalOK(getPanel().okDatabaseServer, "ErrorDatabaseServer", 
			pass, true, error); 
		log.info("OK: Database Server = " + databaseServer);
		setProperty(Environment.COMPIERE_DB_SERVER, databaseServer.getHostName());
		setProperty(Environment.COMPIERE_DB_TYPE, p_data.getDatabaseType());
		setProperty(Environment.COMPIERE_DB_PATH, Environment.DBTYPE_MSSQLServer);

		//	Database Port
		int databasePort = p_data.getDatabasePort();
		pass = p_data.testPort (databaseServer, databasePort, true);
		error = "DB Server Port = " + databasePort; 
		signalOK(getPanel().okDatabaseServer, "ErrorDatabasePort",
			pass, true, error);
		if (!pass)
			return error;
		log.info("OK: Database Port = " + databasePort);
		setProperty(Environment.COMPIERE_DB_PORT, String.valueOf(databasePort));


		//	JDBC Database Info
		String databaseName = p_data.getDatabaseName();	//	Service Name
		String systemPassword = p_data.getDatabaseSystemPassword();

		//	URL (derived)	jdbc:sybase:Tds:prod1:5000/prod1
		String urlSystem = p_db.getConnectionURL(databaseServer.getHostName(), databasePort, 
			p_db.getSystemDatabase(databaseName), p_db.getSystemUser());
		//Pu de check sur le System Pass
		//TODO Verifier l'impact de pu saisir le system pass
		//pass = testJDBC(urlSystem, p_db.getSystemUser(), systemPassword);
		pass = true;
		error = "Error connecting: " + urlSystem 
			+ " - " + p_db.getSystemUser() + "/" + systemPassword;
		signalOK(getPanel().okDatabaseSystem, "ErrorJDBC",
			pass, true, error);
		if (!pass)
			return error;
		log.info("OK: System Connection = " + urlSystem);
		setProperty(Environment.COMPIERE_DB_SYSTEM, systemPassword);


		//	Database User Info
		String databaseUser = p_data.getDatabaseUser();	//	UID
		String databasePassword = p_data.getDatabasePassword();	//	PWD
		pass = databasePassword != null && databasePassword.length() > 0;
		error = "No Database User Password entered";
		signalOK(getPanel().okDatabaseUser, "ErrorJDBC",
			pass, true, error); 
		if (!pass)
			return error;
		//
		String url= p_db.getConnectionURL(databaseServer.getHostName(), databasePort, 
			databaseName, databaseUser);
		//	Ignore result as it might not be imported
		pass = testJDBC(url, databaseUser, databasePassword);
		error = "Database imported? Cannot connect to User: " + databaseUser + "/" + databasePassword;
		signalOK(getPanel().okDatabaseUser, "ErrorJDBC",
			pass, false, error);
		if (pass)
			log.info("OK: Database User = " + databaseUser);
		else
			log.warning(error);
		setProperty(Environment.COMPIERE_DB_URL, url);
		setProperty(Environment.COMPIERE_DB_NAME, databaseName);
		setProperty(Environment.COMPIERE_DB_USER, databaseUser);
		setProperty(Environment.COMPIERE_DB_PASSWORD, databasePassword);

		/**
		//	TNS Name Info
		String sqlplus = "sqlplus system/" + systemPassword + "@" + databaseName
			+ " @utils/oracle/Test.sql";
		log.config(sqlplus);
		pass = testSQL(sqlplus);
		error = "Error connecting via: " + sqlplus;
		signalOK(getPanel().okDatabaseSQL, "ErrorTNS", 
			pass, true, error);
		if (pass)
			log.info("OK: Database SQL Connection");
		
		//	OCI Test
		url = "jdbc:oracle:oci8:@" + databaseName;
		pass = testJDBC(url, "system", systemPassword);
		if (pass)
			log.info("OK: Connection = " + url);
		else
			log.warning("Cannot connect via Net8: " + url);
		setProperty(ConfigurationData.COMPIERE_DB_TNS, databaseName);
		
		**/
		return null;
	}	//	test
	
	/**
	 * 	Test JDBC Connection to Server
	 * 	@param url connection string
	 *  @param uid user id
	 *  @param pwd password
	 * 	@return true if OK
	 */
	private boolean testJDBC (String url, String uid, String pwd)
	{
		try
		{
			Connection conn = p_db.getDriverConnection(url, uid, pwd);
		}
		catch (Exception e)
		{
			log.severe(e.toString());
			return false;
		}
		return true;
	}	//	testJDBC
	
}	//	ConfigSybase
