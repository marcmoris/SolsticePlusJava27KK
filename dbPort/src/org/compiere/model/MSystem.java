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
package org.compiere.model;

import java.lang.management.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.db.*;
import org.compiere.util.*;

/**
 * 	System Record (just one)
 *
 *  @author Jorg Janke
 *  @version $Id: MSystem.java,v 1.1 2007/07/18 14:42:15 marmor01 Exp $
 */
public class MSystem extends X_AD_System
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Load System Record
	 *	@param ctx context
	 *	@return System
	 */
	public static MSystem get (Properties ctx)
	{
		if (s_system != null)
			return s_system;
		//
		String sql = "SELECT * FROM AD_System ORDER BY AD_System_ID";	//	0 first
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				s_system = new MSystem (ctx, rs, null);
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			String info = "No System - " + DB.getDatabaseInfo() + " - " + ex.getLocalizedMessage();
			System.err.println(info);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		if (s_system == null)
			return null;
		//
		if (!Ini.isClient() && s_system.setInfo())
			s_system.save();
		
		boolean SyncroniseWithPandora = getSolsticeParameter(ctx, "SyncroniseWithPandora").equals("True");
		boolean SyncroniseWithAtlas = getSolsticeParameter(ctx, "SyncroniseWithAtlas").equals("True");
		
		
		//+ Solstice Progestion syncronise with Pandora 2012.04.03
		if ( SyncroniseWithPandora ||  SyncroniseWithAtlas )
		{
			//SIQ
			if ( SyncroniseWithAtlas )
			{
				try
				{
					// Vérifie si la base de donnée de l'horodateur est online.
					sql = "SELECT * FROM openquery( HORODATEUR, 'select state from master.sys.databases where name = ''sat0100'' and state = 0' )";
					pstmt = DB.prepareStatement(sql, null);
					ResultSet rs = pstmt.executeQuery();
					rs.next();
					rs.close();
					pstmt.close();
					pstmt = null;
				}
				catch (SQLException e)
				{
					DB.executeUpdate("UPDATE P_Solstice_Parameters set Parameter = 'True' where value = 'SyncroniseWithAtlasFail'", null);
					s_log.log(Level.SEVERE,"Pandora Linked Server not found", e);
					return s_system;
				}			
	        }
			
					
			//2012-11-21. exécution une fois par jours. 	
			Timestamp ssm = s_system.getUpdated();
			if ( s_system.getUpdated().compareTo( new Timestamp( TimeUtil.getToday().getTimeInMillis()) ) < 0 )
			{
				//+ Solstice Progestion syncronise with Pandora
				if ( SyncroniseWithAtlas )
				{
					sql = "EXEC [DBO].[Solstice_Atlas_Syncro]";
					DB.executeUpdate(sql, null);
				}
			}	
			//2012-11-21. exécution a chaque login
			//2013.08.26 SM Enlever le Else, pour faire vraiement un update à chaque login, remettre également en vigueur l'update de la date updated
			//sinon la condition du à chaque jour marche pas.
			//else if ( SyncroniseWithPandora )
			if ( SyncroniseWithPandora )
			{
				sql = "EXEC [DBO].[Solstice_Pandora_Syncro]";
				DB.executeUpdate(sql, null);
			}
				
				s_system.setUpdatedBy(0);
				s_system.save();
//			}

			boolean SyncroniseWithPandoraFail = getSolsticeParameter(ctx, "SyncroniseWithPandoraFail").equals("True");
			boolean SyncroniseWithAtlasFail = getSolsticeParameter(ctx, "SyncroniseWithAtlasFail").equals("True");

			if (  SyncroniseWithPandoraFail && SyncroniseWithPandora )
			{
				//+ Solstice Progestion syncronise with Pandora
				sql = "EXEC [dbo].[PandoraUpdateDataWhenSyncroniseFail]";
				System.out.println( sql);
				DB.executeUpdate(sql, null);
			}
			
			if ( SyncroniseWithAtlasFail && SyncroniseWithAtlas )
			{
				//+ Solstice Progestion syncronise with Pandora
				sql = "EXEC [dbo].[AtlasUpdateDataWhenSyncroniseFail]";
				System.out.println( sql);
				DB.executeUpdate(sql, null);
			}
		}
		//- Solstice Progestion syncronise with Pandora ou Atlas

		return s_system;
	}	//	get

	
	//+ Solstice Progestion 2012.04.04
	public static String getSolsticeParameter (Properties ctx, String ParameterName )
	{
		String Parameter = null;
	    String sql = "select top 1 Parameter from P_Solstice_Parameters Where Value = '" + ParameterName + "'";
        PreparedStatement stmt = DB.prepareStatement(sql, null);
		try
		{
		    ResultSet rs = stmt.executeQuery();
		    if(rs.next())
		    {
		    	// Env.getContext(Env.getCtx(), "#AD_User_Name")
		    	Parameter = rs.getString(1);
		    }
		    rs.close();
		    stmt.close();
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"PgiUtil", e);
			return null;
		}
		
	    return Parameter;
	}
	//- Solstice Progestion 2012.04.04
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (MSystem.class);
	/** System - cached					*/
	private static MSystem		s_system = null;
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param ignored id - if < 0 not loaded
	 *	@param mtrxName transaction
	 */
	public MSystem (Properties ctx, int ignored, String mtrxName)
	{
		super(ctx, 0, mtrxName);
		String trxName = null;
		if (ignored >= 0)
			load(trxName);	//	load ID=0
		if (s_system == null)
			s_system = this;
	}	//	MSystem

	/**
	 * 	Load Constructor
	 * 	@param ctx context
	 * 	@param rs result set
	 * 	@param trxName transaction
	 */
	public MSystem (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
		if (s_system == null)
			s_system = this;
	}	//	MSystem

	/**
	 * 	Constructor
	 */
	public MSystem ()
	{
		this (new Properties(), 0, null);
	}	//	MSystem

	/**
	 * 	Is LDAP Authentification defined
	 *	@return true if ldap defined
	 */
	public boolean isLDAP()
	{
		String host = getLDAPHost();
		if (host == null || host.length() == 0)
			return false;
		String domain = getLDAPDomain();
		return domain != null 
			&& domain.length() > 0;
	}	//	isLDAP	
	
	/**
	 * 	LDAP Authentification. Assumes that LDAP is defined.
	 *	@param userName user name
	 *	@param password password
	 *	@return true if ldap authenticated
	 */
	public boolean isLDAP (String userName, String password)
	{
		return LDAP.validate(getLDAPHost(), getLDAPDomain(), userName, password);
	}	//	isLDAP

	/**
	 * 	Get DB Address
	 *	@return address
	 */
	public String getDBAddress (boolean actual)
	{
		String s = super.getDBAddress ();
		if (actual || s == null || s.length() == 0)
		{
			CConnection cc = CConnection.get(); 
			s = cc.getConnectionURL() + "#" + cc.getDbUid();
			s = s.toLowerCase();
		}
		return s;
	}	//	getDBAddress
	
	/**
	 * 	Get Statistics Info
	 * 	@param recalc recalculate
	 *	@return statistics
	 */
	public String getStatisticsInfo (boolean recalc)
	{
		String s = super.getStatisticsInfo ();
		if (s == null || recalc)
		{
			String count = DB.TO_CHAR("COUNT(*)", DisplayType.Number, Env.getAD_Language(Env.getCtx())); 
			String sql = "SELECT 'C'||(SELECT " + count + " FROM AD_Client)"
				+ " ||'U'||(SELECT " + count + " FROM AD_User)"
				+ " ||'B'||(SELECT " + count + " FROM C_BPartner)"
				+ " ||'P'||(SELECT " + count + " FROM M_Product)"
				+ " ||'I'||(SELECT " + count + " FROM C_Invoice)"
				+ " ||'L'||(SELECT " + count + " FROM C_InvoiceLine)"
				+ " ||'M'||(SELECT " + count + " FROM M_Transaction)"
				+ " ||'c'||(SELECT " + count + " FROM AD_Column WHERE EntityType NOT IN ('C','D'))"
				+ " ||'t'||(SELECT " + count + " FROM AD_Table WHERE EntityType NOT IN ('C','D'))"
				+ " ||'f'||(SELECT " + count + " FROM AD_Field WHERE EntityType NOT IN ('C','D'))"
				+ " FROM AD_System"; 
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				if (rs.next ())
					s = rs.getString(1);
				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log (Level.SEVERE, sql, e);
			}
			try
			{
				if (pstmt != null)
					pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				pstmt = null;
			}
		}
		return s;
	}	//	getStatisticsInfo
	
	/**
	 * 	Get Profile Info
	 * 	@param recalc recalculate
	 *	@return profile
	 */
	public String getProfileInfo (boolean recalc)
	{
		String s = super.getProfileInfo ();
		if (s == null || recalc)
		{
			String sql = "SELECT Value FROM AD_Client "
				+ "WHERE IsActive='Y' ORDER BY AD_Client_ID DESC";
			PreparedStatement pstmt = null;
			StringBuffer sb = new StringBuffer();
			try
			{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				while (rs.next ())
					sb.append(rs.getString(1)).append('|');
				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log (Level.SEVERE, sql, e);
			}
			try
			{
				if (pstmt != null)
					pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				pstmt = null;
			}
			s = sb.toString();
		}
		return s;
	}	//	getProfileInfo
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true/false
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Mandatory Values
		if (get_Value("IsAutoErrorReport") == null)
			setIsAutoErrorReport (true);
		//
		boolean userChange = Ini.isClient() &&
			(is_ValueChanged("Name")
			|| is_ValueChanged("UserName")
			|| is_ValueChanged("Password")
			|| is_ValueChanged("LDAPHost")
			|| is_ValueChanged("LDAPDomain")
			|| is_ValueChanged("CustomPrefix")
			);
		if (userChange)
		{
			String name = getName();
			if (name.equals("?") || name.length() < 2)
			{
				log.saveError("Error", "Define a unique System name (e.g. Company name) not " + name);
				return false;
			}
			if (getUserName().equals("?") || getUserName().length() < 2)
			{
				log.saveError("Error", "Use the same EMail address as in the Compiere Web Store");
				return false;
			}
			if (getPassword().equals("?") || getPassword().length() < 2)
			{
				log.saveError("Error", "Use the same Password as in the Compiere Web Store");
				return false;
			}
		}
		if (getSupportLevel() == null)
			setSupportLevel(SUPPORTLEVEL_Unsupported);
		//
		setInfo();
		return true;
	}	//	beforeSave
	
	/**
	 * 	Save Record (ID=0)
	 * 	@return true if saved
	 */
	public boolean save()
	{
		if (!beforeSave(false))
			return false;
		return saveUpdate();
	}	//	save

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		return "MSystem[" + getName()
			+ ",User=" + getUserName()
			+ ",ReleaseNo=" + getReleaseNo()
			+ "]";
	}	//	toString

	
	/**************************************************************************
	 * 	Check valididity
	 *	@return true if valid
	 */
	public boolean isValid()
	{
		if (getName() == null || getName().length() < 2)
		{
			log.log(Level.WARNING, "Name not valid: " + getName());
			return false;
		}
		if (getPassword() == null || getPassword().length() < 2)
		{
			log.log(Level.WARNING, "Password not valid: " + getPassword());
			return false;
		}
		if (getInfo() == null || getInfo().length() < 2)
		{
			log.log(Level.WARNING, "Need to run Migration once");
			return false;
		}
		return true;
	}	//	isValid

	/**
	 * 	Is there a PDF License
	 *	@return true if there is a PDF License
	 */
	public boolean isPDFLicense()
	{
		String key = getSummary();
		return key != null && key.length() > 25;
	}	//	isPDFLicense
	
	/**
	 * 	Get SupportLevel
	 *	@return Support Level
	 */
	public String getSupportLevel()
	{
		String sl = null;
		if (get_ColumnIndex("SupportLevel") != -1)
			sl = super.getSupportLevel();
		if (sl == null)
			return SUPPORTLEVEL_Unsupported;
		return sl;
	}	//	getSupportLevel
	
	/**
	 * 	Get Record_ID
	 *	@return record ID
	 */
	public int getRecord_ID()
	{
		if (get_ColumnIndex("Record_ID") == -1)
			return -1;
		return super.getRecord_ID ();
	}	//	getRecord_ID
	
	/**
	 * 	Get SupportUnits
	 *	@return SupportUnits
	 */
	public int getSupportUnits()
	{
		if (get_ColumnIndex("SupportUnits") == -1)
			return 0;
		return super.getSupportUnits ();
	}	//	getSupportUnits
	
	/**
	 * 	Get System Status
	 *	@return	system status
	 */
	public String getSystemStatus()
	{
		String ss = null;
		if (get_ColumnIndex("SystemStatus") != -1)
			ss = super.getSystemStatus();
		if (ss == null)
			ss = SYSTEMSTATUS_Evaluation;
		return ss;
	}	//	getSystemStatus
	
	
	/**************************************************************************
	 * 	Set/Derive Info if more then a day old
	 * 	@return true if set
	 */
	public boolean setInfo()
	{
	//	log.severe("setInfo");
		if (!TimeUtil.getDay(getUpdated()).before(TimeUtil.getDay(null)))
			return false;	
		try
		{
			setDBInfo();
			setInternalUsers();
			if (isAllowStatistics())
			{
				setStatisticsInfo(getStatisticsInfo(true));
				setProfileInfo(getProfileInfo(true));
			}
		}
		catch (Exception e)
		{
			setSupportUnits(9999);
			setInfo(e.getLocalizedMessage());
			log.log(Level.SEVERE, "", e);
		}
		return true;
	}	//	setInfo
	
	/**
	 * 	Set Internal User Count
	 */
	private void setInternalUsers()
	{
		String sql = "SELECT COUNT(DISTINCT (u.AD_User_ID)) AS iu "
			+ "FROM AD_User u"
			+ " INNER JOIN AD_User_Roles ur ON (u.AD_User_ID=ur.AD_User_ID) "
			+ "WHERE u.AD_Client_ID<>11"			//	no Demo
			+ " AND u.AD_User_ID NOT IN (0,100)";	//	no System/SuperUser
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				int internalUsers = rs.getInt (1);
				setSupportUnits(internalUsers);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
	}	//	setInternalUsers

	/**
	 * 	Set DB Info
	 */
	private void setDBInfo()
	{
		if (!DB.isRemoteObjects())
		{
			String dbAddress = getDBAddress(true);
			setDBAddress(dbAddress);
		}
		//
		if (!Ini.isClient())
		{
			int noProcessors = Runtime.getRuntime().availableProcessors();
			setNoProcessors(noProcessors);
		}
		//
		try
		{
			DatabaseMetaData md = DB.getConnectionRO().getMetaData();
			String db1 = md.getDatabaseProductName();
			String db2 = md.getDatabaseProductVersion();
			if (db2.startsWith(db1))
				db1 = db2;
			else
				db1 += "-" + db2;
			if (db1.length() > 60)
			{
				db1 = Util.replace (db1, "Database ", "");
				db1 = Util.replace (db1, "Version ", "");
				db1 = Util.replace (db1, "Edition ", "");
				db1 = Util.replace (db1, "Release ", "");
			}
			db1 = Util.removeCRLF(db1);
			setDBInstance(db1);
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "MetaData", e);
		}
	}	//	setDBInfo
		
	
	/**
	 * 	Print info
	 */
	public void info()
	{
		if (!CLogMgt.isLevelFine())
			return;
		//	OS
	//	OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
	//	log.fine(os.getName() + " " + os.getVersion() + " " + os.getArch() 
	//		+ " Processors=" + os.getAvailableProcessors());
		//	Runtime
		RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
		log.fine(rt.getName() + " (" + rt.getVmVersion() + ") Up=" + TimeUtil.formatElapsed(rt.getUptime()));
		//	Memory
		if (CLogMgt.isLevelFiner())
		{
			List<MemoryPoolMXBean> list = ManagementFactory.getMemoryPoolMXBeans();
			Iterator<MemoryPoolMXBean> it = list.iterator();
			while (it.hasNext())
			{
				MemoryPoolMXBean pool = (MemoryPoolMXBean)it.next();
				log.finer(pool.getName() + " " + pool.getType() 
					+ ": " + new CMemoryUsage(pool.getUsage()));
			}
		}
		else
		{
			MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
			log.fine("VM: " + new CMemoryUsage(memory.getNonHeapMemoryUsage()));
			log.fine("Heap: " + new CMemoryUsage(memory.getHeapMemoryUsage()));
		}
		//	Thread
		ThreadMXBean th = ManagementFactory.getThreadMXBean();
		log.fine("Threads=" + th.getThreadCount()
			+ ", Peak=" + th.getPeakThreadCount()
			+ ", Demons=" + th.getDaemonThreadCount()
			+ ", Total=" + th.getTotalStartedThreadCount()
		);
	}	//	info
	
	
	/**
	 * 	Test
	 *	@param args
	 */
	public static void main (String[] args)
	{
		new MSystem();
	}	//	main
	
}	//	MSystem
