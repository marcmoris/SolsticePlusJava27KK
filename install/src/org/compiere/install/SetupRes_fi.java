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
 *	Setup Resources for Finnish language
 *
 * 	@author 	Petteri Soininen (petteri.soininen@netorek.fi)
 * 	@version 	$Id: SetupRes_fi.java,v 1.1 2007/07/18 14:41:04 marmor01 Exp $
 */
public class SetupRes_fi extends ListResourceBundle
{
	/**	
    * Translation Info
    */
	static final Object[][] contents = new String[][]{
	{ "CompiereServerSetup", 	"Compiere-palvelimen Asetukset" },
	{ "Ok", 					"Hyväksy" },
	{ "File", 					"Tiedosto" },
	{ "Exit", 					"Poistu" },
	{ "Help", 					"Help" },
	{ "PleaseCheck", 			"Ole hyvä ja valitse" },
	{ "UnableToConnect", 		"Yhteydenotto Compieren Web-Help:in ei onnistu" },
	//
	{ "CompiereHomeInfo", 		"Compiere Home on pääkansio" },
	{ "CompiereHome", 			"Compiere Home" },
	{ "WebPortInfo", 			"Web (HTML) Portti" },
	{ "WebPort", 				"Web Portti" },
	{ "AppsServerInfo", 		"Sovelluspalvelimen Nimi" },
	{ "AppsServer", 			"Sovelluspalvelin" },
	{ "DatabaseTypeInfo", 		"Tietokantatyyppi" },
	{ "DatabaseType", 			"Tietokantatyyppi" },
	{ "DatabaseNameInfo", 		"Tietokannan Nimi" },
	{ "DatabaseName", 			"Tietokannan Nimi (SID)" },
	{ "DatabasePortInfo", 		"Tietokannan kuuntelijaportti" },
	{ "DatabasePort", 			"Tietokantaportti" },
	{ "DatabaseUserInfo", 		"Tietokannan Compiere-käyttäjätunnus" },
	{ "DatabaseUser", 			"Tietokannan käyttäjätunnus" },
	{ "DatabasePasswordInfo", 	"Tietokannan Compiere-salasana" },
	{ "DatabasePassword", 		"Tietokannan salasana" },
	{ "TNSNameInfo", 			"TNS tai Globaali Tietokannan Nimi" },
	{ "TNSName", 				"TNS Nimi" },
	{ "SystemPasswordInfo", 	"Järjestelmäsalasana" },
	{ "SystemPassword", 		"Järjestelmäsalasana" },
	{ "MailServerInfo", 		"Sähköpostipalvelin" },
	{ "MailServer", 			"Sähköpostipalvelin" },
	{ "AdminEMailInfo", 		"Compiere-ylläpitäjän Sähköposti" },
	{ "AdminEMail", 			"Ylläpitäjän Sähköposti" },
	{ "DatabaseServerInfo", 	"Tietokantapalvelimen Nimi" },
	{ "DatabaseServer", 		"Tietokantapalvelin" },
	{ "JavaHomeInfo", 			"Java-kotihakemisto" },
	{ "JavaHome", 				"Java-koti" },
	{ "JNPPortInfo", 			"Sovelluspalvelimen JNP-portti" },
	{ "JNPPort", 				"JNP-portti" },
	{ "MailUserInfo", 			"Compiere-sähköpostikäyttäjä" },
	{ "MailUser", 				"Sähköpostikäyttäjä" },
	{ "MailPasswordInfo", 		"Compiere-sähköpostisalasana" },
	{ "MailPassword", 			"Sähköpostisalasana" },
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
	{ "TestInfo", 				"Testaa Asetukset" },
	{ "Test", 					"Testaa" },
	{ "SaveInfo", 				"Tallenna Asetukset" },
	{ "Save", 					"Tallenna" },
	{ "HelpInfo", 				"Hae Apua" },
	//
	{ "ServerError", 			"Palvelimen Asetusvirhe" },
	{ "ErrorJavaHome", 			"Java-kotivirhe" },
	{ "ErrorCompiereHome", 		"Compiere-kotivirhe" },
	{ "ErrorAppsServer", 		"Sovelluspalvelinvirhe (älä käytä paikallisverkkoasemaa)" },
	{ "ErrorWebPort", 			"Web-porttivirhe" },
	{ "ErrorJNPPort", 			"JNP-porttivirhe" },
	{ "ErrorDatabaseServer", 	"Tietokantapalvelinvirhe (älä käytä paikallisverkkoasemaa)" },
	{ "ErrorDatabasePort", 		"Tietokantaporttivirhe" },
	{ "ErrorJDBC", 				"JDBC-yhteysvirhe" },
	{ "ErrorTNS", 				"TNS-yhteysvirhe" },
	{ "ErrorMailServer", 		"Sähköpostipalvelinvirhe (älä käytä paikallisverkkoasemaa)" },
	{ "ErrorMail", 				"Sähköpostivirhe" },
	{ "ErrorSave", 				"Tiedostontallennusvirhe" },

	{ "EnvironmentSaved", 		"Ympäristö tallennettu/Palvelin täytyy käynnistää uudelleen." },

	{ "RMIoverHTTP", 			"Tunneloi objektit HTTP kautta" },
	{ "RMIoverHTTPInfo", 		"RMI HTTP:n yli mahdollistaa palomuurien läpäisyn" },
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

}	//	SerupRes
