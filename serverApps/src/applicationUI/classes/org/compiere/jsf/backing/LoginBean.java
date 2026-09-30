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

package org.compiere.jsf.backing;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

import org.compiere.Compiere;
import org.compiere.db.CConnection;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Login;

public class LoginBean implements Serializable
{
	private static final long serialVersionUID = 6668443190949296296L;
	private LoginInformation loginInformation = new LoginInformation();
	private boolean showTab;
	private int selectedIndex;
	
	public static class LoginInformation {
		private String server;
		private String userId;
		private String password;
		private String selectedLanguage;
		private HashMap<String,String> languageMap = new HashMap<String, String>();
		private String copyright = Compiere.COPYRIGHT;
		private String release = Compiere.DATE_VERSION;
		private String version = Compiere.MAIN_VERSION;
		private String selectedRole = "";
		private String selectedRoleName="";
		private HashMap<String,String> roleMap = new HashMap<String,String>();
		private String selectedClient = "";
		private String selectedClientName = "";
		private HashMap<String,String> clientMap = new HashMap<String,String>();
		private String selectedOrg = "";
		private String selectedOrgName = "";
		private HashMap<String,String> orgMap = new HashMap<String,String>();
		private String selectedWarehouse = "";
		private String selectedWarehouseName = "";
		private HashMap<String,String> warehouseMap = new HashMap<String,String>();
		private String date = Calendar.getInstance().getTime().toString();
		private String selectedPrinter;
		private HashMap<String,String> printerMap = new HashMap<String,String>();

		/**
		 * @return the selectedClientName
		 */
		public String getSelectedClientName() {
			return selectedClientName;
		}
		/**
		 * @param selectedClientName the selectedClientName to set
		 */
		public void setSelectedClientName(String selectedClientName) {
			this.selectedClientName = selectedClientName;
		}
		/**
		 * @return the selectedOrgName
		 */
		public String getSelectedOrgName() {
			return selectedOrgName;
		}
		/**
		 * @param selectedOrgName the selectedOrgName to set
		 */
		public void setSelectedOrgName(String selectedOrgName) {
			this.selectedOrgName = selectedOrgName;
		}
		/**
		 * @return the selectedRoleName
		 */
		public String getSelectedRoleName() {
			return selectedRoleName;
		}
		/**
		 * @param selectedRoleName the selectedRoleName to set
		 */
		public void setSelectedRoleName(String selectedRoleName) {
			this.selectedRoleName = selectedRoleName;
		}
		/**
		 * @return the selectedWarehouseName
		 */
		public String getSelectedWarehouseName() {
			return selectedWarehouseName;
		}
		/**
		 * @param selectedWarehouseName the selectedWarehouseName to set
		 */
		public void setSelectedWarehouseName(String selectedWarehouseName) {
			this.selectedWarehouseName = selectedWarehouseName;
		}
		/**
		 * @return the release
		 */
		public String getRelease() {
			return release;
		}
		/**
		 * @param release the release to set
		 */
		public void setRelease(String release) {
			this.release = release;
		}
		/**
		 * @return the version
		 */
		public String getVersion() {
			return version;
		}
		/**
		 * @param version the version to set
		 */
		public void setVersion(String version) {
			this.version = version;
		}	
		/**
		 * @return the clientMap
		 */
		public HashMap<String, String> getClientMap() {
			return clientMap;
		}
		/**
		 * @param clientMap the clientMap to set
		 */
		public void setClientMap(HashMap<String, String> clientMap) {
			this.clientMap = clientMap;
		}
		/**
		 * @return the date
		 */
		public String getDate() {
			return date;
		}
		/**
		 * @param date the date to set
		 */
		public void setDate(String date) {
			this.date = date;
		}
		/**
		 * @return the orgMap
		 */
		public HashMap<String, String> getOrgMap() {
			return orgMap;
		}
		/**
		 * @param orgMap the orgMap to set
		 */
		public void setOrgMap(HashMap<String, String> orgMap) {
			this.orgMap = orgMap;
		}
		/**
		 * @return the printerMap
		 */
		public HashMap<String, String> getPrinterMap() {
			return printerMap;
		}
		/**
		 * @param printerMap the printerMap to set
		 */
		public void setPrinterMap(HashMap<String, String> printerMap) {
			this.printerMap = printerMap;
		}
		/**
		 * @return the roleMap
		 */
		public HashMap<String, String> getRoleMap() {
			return roleMap;
		}
		/**
		 * @param roleMap the roleMap to set
		 */
		public void setRoleMap(HashMap<String, String> roleMap) {
			this.roleMap = roleMap;
		}
		/**
		 * @return the selectedClient
		 */
		public String getSelectedClient() {
			return selectedClient;
		}
		/**
		 * @param selectedClient the selectedClient to set
		 */
		public void setSelectedClient(String selectedClient) {
			this.selectedClient = selectedClient;
		}
		/**
		 * @return the selectedOrg
		 */
		public String getSelectedOrg() {
			return selectedOrg;
		}
		/**
		 * @param selectedOrg the selectedOrg to set
		 */
		public void setSelectedOrg(String selectedOrg) {
			this.selectedOrg = selectedOrg;
		}
		/**
		 * @return the selectedPrinter
		 */
		public String getSelectedPrinter() {
			return selectedPrinter;
		}
		/**
		 * @param selectedPrinter the selectedPrinter to set
		 */
		public void setSelectedPrinter(String selectedPrinter) {
			this.selectedPrinter = selectedPrinter;
		}
		/**
		 * @return the selectedRole
		 */
		public String getSelectedRole() {
			return selectedRole;
		}
		/**
		 * @param selectedRole the selectedRole to set
		 */
		public void setSelectedRole(String selectedRole) {
			this.selectedRole = selectedRole;
		}
		/**
		 * @return the selectedWarehouse
		 */
		public String getSelectedWarehouse() {
			return selectedWarehouse;
		}
		/**
		 * @param selectedWarehouse the selectedWarehouse to set
		 */
		public void setSelectedWarehouse(String selectedWarehouse) {
			this.selectedWarehouse = selectedWarehouse;
		}
		/**
		 * @return the warehouseMap
		 */
		public HashMap<String, String> getWarehouseMap() {
			return warehouseMap;
		}
		/**
		 * @param warehouseMap the warehouseMap to set
		 */
		public void setWarehouseMap(HashMap<String, String> warehouseMap) {
			this.warehouseMap = warehouseMap;
		}
		/**
		 * @return the copyright
		 */
		public String getCopyright() {
			return copyright;
		}
		/**
		 * @param copyright the copyright to set
		 */
		public void setCopyright(String copyright) {
			this.copyright = copyright;
		}
		/**
		 * @return the languageMap
		 */
		public HashMap<String, String> getLanguageMap() 
		{
			if (languageMap.isEmpty())
			{
				tempFillMap();
			}
			return languageMap;
		}
		/**
		 * @param languageMap the languageMap to set
		 */
		public void setLanguageMap(HashMap<String, String> languageMap) 
		{
			this.languageMap = languageMap;
		}
		/**
		 * @return the password
		 */
		public String getPassword() 
		{
			return password;
		}
		/**
		 * @param password the password to set
		 */
		public void setPassword(String password) 
		{
			Ini.setProperty("ApplicationPassword", getPassword());
			this.password = password;
		}
		/**
		 * @return the selectedLanguage
		 */
		public String getSelectedLanguage() 
		{
			return selectedLanguage;
		}
		/**
		 * @param selectedLanguage the selectedLanguage to set
		 */
		public void setSelectedLanguage(String selectedLanguage) 
		{
			this.selectedLanguage = selectedLanguage;
		}
		/**
		 * @return the server
		 */
		public String getServer() 
		{
			return server;
		}
		/**
		 * @param server the server to set
		 */
		public void setServer(String server) 
		{
			this.server = server;
		}
		/**
		 * @return the userId
		 */
		public String getUserId() 
		{
			return userId;
		}
		/**
		 * @param userId the userId to set
		 */
		public void setUserId(String userId) 
		{
			Ini.setProperty("ApplicationUserID", getUserId());
			this.userId = userId;
		}
		
		/**
		 * Creates a temp HashMap to fill selectOneListBox
		 */
		public void tempFillMap()
		{
			HashMap<String,String> tempMap = new HashMap<String,String>();
			tempMap.put("English", "English");
			setLanguageMap(tempMap);
		}
	}
	
	/**
	 * @return the loginInformation
	 */
	public LoginInformation getLoginInformation() {
		return loginInformation;
	}
	/**
	 * @param loginInformation the loginInformation to set
	 */
	public void setLoginInformation(LoginInformation loginInformation) {
		this.loginInformation = loginInformation;
	}
	/**
	 * @return the selectedIndex
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}
	/**
	 * @param selectedIndex the selectedIndex to set
	 */
	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	/**
	 * @return the showTab
	 */
	public boolean getShowTab() {
		return showTab;
	}
	/**
	 * @param showTab the showTab to set
	 */
	public void setShowTab(boolean showTab) {
		this.showTab = showTab;
	}
	/**	Logging	*/
	private static CLogger log = null;
	
	/**
	 * Constructor
	 */
	public LoginBean() {
		setShowTab(false);
		setSelectedIndex(0);
		Compiere.startup(true);
		Ini.loadProperties(true);
		loginInformation.setServer(CConnection.get().toString());
		loginInformation.setUserId(Ini.getProperty(Ini.P_UID));
		loginInformation.setPassword(Ini.getProperty(Ini.P_PWD));
	}
	
	/**
	 * calls setDefault method to update drop down boxes
	 */
	public String refreshDropDownMenus() 
	{
		setDropDownMenus();
		return "";
	}
	
	/** 
	 * Enter fields from the "connection" tab, render additional tabs, and continue
	 */
	public String connectionOkClicked()
	{
		// Initial role
		loginInformation.setSelectedRole(Ini.getProperty(Ini.P_ROLE));
		setShowTab(true);
		setDropDownMenus();
		setSelectedIndex(1);
		//  Set Defaults
		loginInformation.getPrinterMap().put(Ini.getProperty(Ini.P_PRINTER), Ini.getProperty(Ini.P_PRINTER));

		//	Establish connection
		DB.setDBTarget(CConnection.get());
		if (!DB.isConnected())
		{
			setSelectedIndex(0);
			return "";
		}
		
		//	Reference check
		Ini.setProperty(Ini.P_COMPIERESYS, "Reference".equalsIgnoreCase(CConnection.get().getDbUid()));

		
	
		return "";
	}
	
	/**
	 * Enter fields from the "defaults" tab and attempt to log in
	 */
	public String defaultOkClicked()
	{
		//	attempt to log user in
		if(savePropertiesAndLogIn())
		{
			return "menu";
		}
		// if login fails, return to login page, "defaults" tab
		return "";
	}
	
	/**
	 * set up defaults.jsp drop down menus for Role, Client, Organization, Warehouse
	 */
	public void setDropDownMenus() {
		Login login = new Login(Env.getCtx());
		// get Roles
		KeyNamePair[] roleKNP = login.getRoles(loginInformation.getUserId(), loginInformation.getPassword());
		KeyNamePair currRole = null;
		KeyNamePair currClient = null;
		KeyNamePair currOrg = null;
		
		// empty HashMaps to fill in new values
		loginInformation.getRoleMap().clear();
		loginInformation.getClientMap().clear();
		loginInformation.getOrgMap().clear();
		loginInformation.getWarehouseMap().clear();
		
		// Check for selected role
		for (int i = 0; i < roleKNP.length; i++)
		{
			if (loginInformation.getSelectedRole().equals(String.valueOf(roleKNP[i].getKey()))) 
			{
				currRole = new KeyNamePair(roleKNP[i].getKey(),roleKNP[i].getName());
				loginInformation.setSelectedRoleName(roleKNP[i].getName());
			} 
		}
		if (currRole == null)
		{
			currRole = new KeyNamePair(roleKNP[0].getKey(),roleKNP[0].getName());
			loginInformation.setSelectedRoleName(roleKNP[0].getName());
		}
		
		// Iterate through roleKNP to put values into loginInformation.roleMap
		for (int i = 0; i < roleKNP.length; i++)
		{
			loginInformation.getRoleMap().put(roleKNP[i].getName(), String.valueOf(roleKNP[i].getKey()));
		}
		
		if (roleKNP != null && roleKNP.length > 0)
		{
			KeyNamePair[] clientKNP = login.getClients(currRole);
			
			// check for selected client
			for (int i = 0; i < clientKNP.length; i++)
			{
				if (loginInformation.getSelectedClient() != null)
				{
					if (loginInformation.getSelectedClient().equals(String.valueOf(clientKNP[i].getKey())))
					{
						currClient = new KeyNamePair(clientKNP[i].getKey(), clientKNP[i].getName());
						loginInformation.setSelectedClientName(clientKNP[i].getName());
					}
				}
			}
			if (currClient == null)
			{
				currClient = new KeyNamePair(clientKNP[0].getKey(), clientKNP[0].getName());
				loginInformation.setSelectedClientName(clientKNP[0].getName());
			}
			
			// Iterate through clientKNP to put values into loginInformation.clientMap
			for (int i = 0; i < clientKNP.length; i++)
			{
				loginInformation.getClientMap().put(clientKNP[i].getName(), String.valueOf(clientKNP[i].getKey()));
			}
			
			if (clientKNP != null && clientKNP.length > 0)
			{
				KeyNamePair[] orgKNP = login.getOrgs(currClient);
				
				// check for selected org
				for (int i = 0; i < orgKNP.length; i++)
				{
					if (loginInformation.getSelectedOrg() != null)
					{
						if (loginInformation.getSelectedOrg().equals(String.valueOf(orgKNP[i].getKey())))
						{
							currOrg = new KeyNamePair(orgKNP[i].getKey(), orgKNP[i].getName());
							loginInformation.setSelectedOrgName(orgKNP[i].getName());
						}
					}
				}
				if (currOrg == null)
				{
					currOrg = new KeyNamePair(orgKNP[0].getKey(), orgKNP[0].getName());
					loginInformation.setSelectedOrgName(orgKNP[0].getName());
				}
				
				// Iterate through orgKNP to put values into loginInformation.orgMap
				for (int i = 0; i < orgKNP.length; i++)
				{
					loginInformation.getOrgMap().put(orgKNP[i].getName(), String.valueOf(orgKNP[i].getKey()));
				}
				
				if (orgKNP != null && orgKNP.length > 0)
				{
					KeyNamePair[] warehouseKNP = login.getWarehouses(currOrg);
					// check to make sure warehouse isn't null.  Is so set to empty string.
					if (loginInformation.getSelectedWarehouse() == null)
					{
						loginInformation.setSelectedWarehouse("");
					}
					if (warehouseKNP != null) {
						for (int i = 0; i < warehouseKNP.length; i++)
						{
							if (loginInformation.getSelectedWarehouse().equals(String.valueOf(warehouseKNP[i].getKey())))
							{
								loginInformation.setSelectedWarehouseName(warehouseKNP[i].getName());
							}
							else
							{
								loginInformation.setSelectedWarehouseName(warehouseKNP[0].getName());
							}
							loginInformation.getWarehouseMap().put(warehouseKNP[i].getName(), String.valueOf(warehouseKNP[i].getKey()));
						}
					}
				}
			}
		}
	}

	/**
	 * save selected properties to Compiere.Properties
	 */
	private boolean savePropertiesAndLogIn()
	{
		Ini.setProperty(Ini.P_UID, loginInformation.getUserId());
        Ini.setProperty(Ini.P_PWD, loginInformation.getPassword());
        Ini.setProperty(Ini.P_ROLE, loginInformation.getSelectedRoleName());
        Ini.setProperty(Ini.P_CLIENT, loginInformation.getSelectedClientName());
        Ini.setProperty(Ini.P_ORG, loginInformation.getSelectedOrgName());
        Ini.setProperty(Ini.P_LANGUAGE, loginInformation.getSelectedLanguage());
        if (loginInformation.getSelectedWarehouseName().equals(""))
        {
        	Ini.setProperty(Ini.P_WAREHOUSE, null);
        }
        else
        {
        	Ini.setProperty(Ini.P_WAREHOUSE, loginInformation.getSelectedWarehouseName());
        }
        Ini.saveProperties(true);
        Properties compiereProperties = Env.getCtx();
        Compiere.startupEnvironment(true);
        Login login = new Login(compiereProperties);
        
        // create KeyNamePair for org and warehouse to send to methods
        KeyNamePair org = new KeyNamePair(Integer.parseInt(loginInformation.getSelectedOrg()), loginInformation.getSelectedOrgName());
        KeyNamePair warehouse = null;
        if (loginInformation.getSelectedWarehouse() != null)
        {
        	warehouse = new KeyNamePair(Integer.parseInt(loginInformation.getSelectedWarehouse()), loginInformation.getSelectedWarehouseName());
        }
         
        // validate login and check errorMsg
        String errorMsg = login.validateLogin(org);
        if (errorMsg != null && errorMsg.length() > 0)
        {
        	log.warning("Login Validation Failed!");
        	return false;
        }
        
        // load Preferences
        String msg = login.loadPreferences(org, warehouse, new Timestamp(System.currentTimeMillis()), null);
        if (msg.length() > 0)
        {
        	log.info(msg);
        }
        
//        if (!login.batchLogin())
//        {
//            return false;
//        }
        
        return true;
	}
}