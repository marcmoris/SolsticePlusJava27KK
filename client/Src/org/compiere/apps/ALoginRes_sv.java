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
package org.compiere.apps;

import java.util.*;

/**
 *  Swedish Base Resource Bundle Translation
 *
 * 	@author 	Thomas Dilts
 * 	@version 	$Id: ALoginRes_sv.java,v 1.2 2007/07/18 21:09:24 marmor01 Exp $
 */
public final class ALoginRes_sv extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Anslutning" },
	{ "Defaults",           "Standardinställningar" },
	{ "Login",              "Compiere inloggning" },
	{ "File",               "Fil" },
	{ "Exit",               "Avsluta" },
	{ "Help",               "Hjälp" },
	{ "About",              "Om" },
	{ "Host",               "Värddatorn" },
	{ "Database",           "Databas" },
	{ "User",               "Användarnamn" },
	{ "EnterUser",          "Ange program användarnamn" },
	{ "Password",           "Lösenord" },
	{ "EnterPassword",      "Ange program lösenord" },
	{ "Language",           "Språk" },
	{ "SelectLanguage",     "Välj ditt språk" },
	{ "Role",               "Roll" },
	{ "Client",             "Klient" },
	{ "Organization",       "Organisation" },
	{ "Date",               "Datum" },
	{ "Warehouse",          "Lager" },
	{ "Printer",            "Skrivare" },
	{ "Connected",          "Anslutad" },
	{ "NotConnected",       "Ej ansluten" },
	{ "DatabaseNotFound",   "Hittade inte databasen" },
	{ "UserPwdError",       "User does not match password" },
	{ "RoleNotFound",       "Hittade inte rollen" },
	{ "Authorized",         "Auktoriserad" },
	{ "Ok",                 "Ok" },
	{ "Cancel",             "Avbryt" },
	{ "VersionConflict",    "Versionskonflikt:" },
	{ "VersionInfo",        "Server <> Klient" },
	{ "PleaseUpgrade",      "Kör updateringsprogram" }
	};

	/**
	 *  Get Contents
	 *  @return context
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}   //  ALoginRes
