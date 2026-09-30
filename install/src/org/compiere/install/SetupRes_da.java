/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.install;

import java.util.*;

/**
 *	Setup Resources
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: SetupRes_da.java,v 1.1 2007/07/18 14:41:04 marmor01 Exp $
 */
public class SetupRes_da extends ListResourceBundle
{
	/**	Translation Info	*/
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", "Compiere: Opsætning af server" },
	{ "Ok", 				"OK" },
	{ "File", 				"Fil" },
	{ "Exit", 				"Afslut" },
	{ "Help", 				"Hjælp" },
	{ "PleaseCheck", 		"Kontrollér" },
	{ "UnableToConnect",	"Kan ikke hente hjælp fra Compieres web-sted" },

	{ "CompiereHomeInfo", 	"Compiere Home er hovedmappen" },
	{ "CompiereHome", 		"Compiere Home" },
	{ "WebPortInfo", 		"Web (HTML)-port" },
	{ "WebPort", 			"Web-port" },
	{ "AppsServerInfo", 	"Programserverens navn" },
	{ "AppsServer", 		"Prog.-server" },
	{ "DatabaseTypeInfo", 	"Databasetype" },
	{ "DatabaseType", 		"Databasetype" },
	{ "DatabaseNameInfo", 	"Databasenavn " },
	{ "DatabaseName", 		"Databasenavn (SID)" },
	{ "DatabasePortInfo", 	"Database Listener Port" },
	{ "DatabasePort", 		"Databaseport" },
	{ "DatabaseUserInfo", 	"Database: Bruger-ID til Compiere" },
	{ "DatabaseUser", 		"Database: Bruger" },
	{ "DatabasePasswordInfo", "Database: Brugeradgangskode til Compiere" },
	{ "DatabasePassword", 	"Database: Adgangskode" },
	{ "TNSNameInfo", 		"TNS eller Global Database Name" },
	{ "TNSName", 			"TNS-navn" },
	{ "SystemPasswordInfo", "System: Brugeradgangskode" },
	{ "SystemPassword", 	"System-adgangskode" },
	{ "MailServerInfo", 	"Mail-server" },
	{ "MailServer", 		"Mail-server" },
	{ "AdminEMailInfo", 	"Compiere: Administrators e-mail" },
	{ "AdminEMail", 		"Admin. e-mail" },
	{ "DatabaseServerInfo", "Databaseservers navn" },
	{ "DatabaseServer", 	"Databaseserver" },
	{ "JavaHomeInfo", 		"Java Home-mappe" },
	{ "JavaHome", 			"Java Home" },
	{ "JNPPortInfo", 		"Programservers JNP-port" },
	{ "JNPPort", 			"JNP-port" },
	{ "MailUserInfo", 		"Compiere: Mail-bruger" },
	{ "MailUser", 			"Mail: Bruger" },
	{ "MailPasswordInfo", 	"Compiere: Brugeradgangskode til mail" },
	{ "MailPassword", 		"Adgangskode til mail" },
	{ "KeyStorePassword",		"Key Store Password" },
	{ "KeyStorePasswordInfo",	"Password for SSL Key Store" },
	//
	{ "JavaType",				"Java VM"},
	{ "JavaTypeInfo",			"Java VM Vendor"},
	{ "AppsType",				"Server Type"},
	{ "AppsTypeInfo",			"J2EE Application Server Type"},
	{ "DeployDir",				"Deployment"},
	{ "DeployDirInfo",			"J2EE Deployment Directory"},
	{ "ErrorDeployDir",			"Error Deployment Directory"},
	//
	{ "TestInfo", 			"Afprøv opsætning" },
	{ "Test", 				"Afprøv" },
	{ "SaveInfo", 			"Gem opsætning" },
	{ "Save", 				"Gem" },
	{ "HelpInfo", 			"Hjælp" },

	{ "ServerError", 		"Fejl: Serverops�tning" },
	{ "ErrorJavaHome", 		"Fejl: Java Home" },
	{ "ErrorCompiereHome", 	"Fejl: Compiere Home" },
	{ "ErrorAppsServer", 	"Fejl: Prog.-server (brug ikke localhost)" },
	{ "ErrorWebPort", 		"Fejl: Web-port" },
	{ "ErrorJNPPort", 		"Fejl: JNP-port" },
	{ "ErrorDatabaseServer", "Fejl: Databaseserver (brug ikke localhost)" },
	{ "ErrorDatabasePort", 	"Fejl: Databaseport" },
	{ "ErrorJDBC", 			"Fejl: JDBC-forbindelse" },
	{ "ErrorTNS", 			"Fejl: TNS-forbindelse" },
	{ "ErrorMailServer", 	"Fejl: Mailserver (brug ikke localhost)" },
	{ "ErrorMail", 			"Fejl: Mail" },
	{ "ErrorSave", 			"Fejl: Swing-fil" },

	{ "EnvironmentSaved",	"Miljøet er gemt\nGenstart serveren." },
	{ "RMIoverHTTP", 		"Tunnel Objects via HTTP" },
	{ "RMIoverHTTPInfo", 	"RMI over HTTP allows to go through firewalls" },
	{ "JNDIPort", 				"JNDI Port" },
	{ "ErrorWasClient", 		"Error WAS Client PATH" }
	};

	/**
	 * 	Get Contents
	 * 	@return contents
	 */
	public Object[][] getContents()
	{
		return contents;
	}	//	getContents

}	//	SetupRes_da

