/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is             Compiere  ERP & CRM Smart Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Gain_Account Model
 *
 *  @author Marc Morissette
 *  @version $Id: P_Gain_Account.java,v 1.3 2007/09/04 13:01:35 marmor01 Exp $
 */
public class P_Gain_Account extends X_P_Gain_Account
{
	/**
	 * 	Get Gain_Account
	 *	@param ctx context
	 * 	@param P_Gain_Account_ID id
	 *	@return Gain_Account
	 */
	public static P_Gain_Account get (Properties ctx, int P_Gain_Account_ID, String trxName)
	{
		Integer key = new Integer (P_Gain_Account_ID);
		P_Gain_Account Gain_Account = (P_Gain_Account)s_cache.get(key);
		if (Gain_Account != null)
			return Gain_Account;
		Gain_Account = new P_Gain_Account (ctx, P_Gain_Account_ID, trxName);
		s_cache.put (key, Gain_Account);
		return Gain_Account;
	}	//	get

	
	public static P_Gain_Account get (Properties ctx, int AD_Client_ID, int AD_Org_ID, int Gain_ID, int Occupation_Group_ID, int Job_Type_ID, int Job_Title_ID, String trxName )
	{
	
		String Skey = Gain_ID + "." + Occupation_Group_ID + "." + Job_Title_ID + "." + Job_Type_ID;
		P_Gain_Account GainAccount = (P_Gain_Account)s_cache2.get(Skey);
		if (GainAccount != null)
			return GainAccount;


		int P_Gain_Account_ID = 0;
		String query = "Select P_Gain_Account_ID "
			         + " FROM P_Gain_Account "
					 + " WHERE AD_Client_ID = " + AD_Client_ID
					 + "   AND P_Gain_ID = " + Gain_ID
					 + "   AND P_Occupation_Group_ID = " +  Occupation_Group_ID 
					 + "   AND ( P_Job_Type_ID  = " + Job_Type_ID  + " OR P_Job_Type_ID  is null )"
					 + "   AND ( P_Job_Title_ID = " + Job_Title_ID + " OR P_Job_Title_ID is null )"
					 + "   ORDER BY P_Gain_ID, P_Occupation_Group_ID, P_Job_Type_ID DESC, P_Job_Title_ID DESC "
					 ; 

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (query, null);
			
			ResultSet rs = pstmt.executeQuery ();
			
			if (rs.next ())
			{
				P_Gain_Account_ID = rs.getInt(1);
			}
			else
			{
				P_Gain_Account_ID = -1;
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "Get P_Gain_Account Error ", e);
		}

		P_Gain_Account Gain_Account = P_Gain_Account.get( ctx, P_Gain_Account_ID , trxName);
		s_cache2.put (Skey, Gain_Account);
	
		return Gain_Account;
	}	//	get


	//
	//
	//
	public static P_Gain_Account get (Properties ctx, int AD_Client_ID, int Gain_ID, int Occupation_Group_ID, int Job_Type_ID, int Job_Title_ID, String trxName )
	{
	
		String Skey = Gain_ID + "." + Occupation_Group_ID + "." + Job_Title_ID + "." + Job_Type_ID;
		P_Gain_Account GainAccount = (P_Gain_Account)s_cache2.get(Skey);
		if (GainAccount != null)
			return GainAccount;


		int P_Gain_Account_ID = 0;
		String query = "Select P_Gain_Account_ID "
			         + " FROM P_Gain_Account "
					 + " WHERE AD_Client_ID = " + AD_Client_ID
					 + "   AND P_Gain_ID = " + Gain_ID
					 + "   AND P_Occupation_Group_ID = " +  Occupation_Group_ID 
					 + "   AND ( P_Job_Type_ID  = " + Job_Type_ID  + " OR P_Job_Type_ID  is null )"
					 + "   AND ( P_Job_Title_ID = " + Job_Title_ID + " OR P_Job_Title_ID is null )"
					 + "   ORDER BY P_Gain_ID, P_Occupation_Group_ID, P_Job_Type_ID DESC, P_Job_Title_ID DESC "
					 ; 

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (query, null);
			
			ResultSet rs = pstmt.executeQuery ();
			
			if (rs.next ())
			{
				P_Gain_Account_ID = rs.getInt(1);
			}
			else
			{
				P_Gain_Account_ID = -1;
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "Get P_Gain_Account Error ", e);
		}

		P_Gain_Account Gain_Account = P_Gain_Account.get( ctx, P_Gain_Account_ID , trxName);
		s_cache2.put (Skey, Gain_Account);
	
		return Gain_Account;
	}	//	get

	
	public static P_Gain_Account getWithSalesRegion (Properties ctx, int AD_Client_ID, int AD_Org_ID, int C_SalesRegion_ID, int P_Gain_ID, String trxName )
	{

		String Skey = P_Gain_ID + "." + AD_Client_ID + "." + AD_Org_ID + "." + C_SalesRegion_ID;
		P_Gain_Account GainAccount = (P_Gain_Account)s_cache2.get(Skey);
		if (GainAccount != null)
			return GainAccount;

		int P_Gain_Account_ID = 0;
		String query = "Select P_Gain_Account_ID "
			         + " FROM P_Gain_Account "
					 + " WHERE AD_Client_ID  = " + AD_Client_ID
					 + "   AND AD_Org_ID     = " + AD_Org_ID
					 + "   AND P_Gain_ID     = " + P_Gain_ID
					 + "   AND C_SalesRegion_ID = " + C_SalesRegion_ID
					 ; 

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (query, null);
			ResultSet rs = pstmt.executeQuery ();
			
			if (rs.next ())
			{
				P_Gain_Account_ID = rs.getInt(1);
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "Get P_Gain_Account Error  ", e);
		}

		if ( P_Gain_Account_ID == 0 )
			return null;
		
		P_Gain_Account Gain_Account = P_Gain_Account.get( ctx, P_Gain_Account_ID , trxName);
		s_cache2.put (Skey, Gain_Account);
	
		return Gain_Account;
	}	//	get

	
	public static P_Gain_Account get (Properties ctx, int AD_Client_ID, int AD_Org_ID, int C_Activity_Src_ID, int P_Gain_ID, String trxName )
	{

		String Skey = P_Gain_ID + "." + AD_Client_ID + "." + AD_Org_ID + "." + C_Activity_Src_ID;
		P_Gain_Account GainAccount = (P_Gain_Account)s_cache2.get(Skey);
		if (GainAccount != null)
			return GainAccount;

		int P_Gain_Account_ID = 0;
		String query = "Select P_Gain_Account_ID "
			         + " FROM P_Gain_Account "
					 + " WHERE AD_Client_ID  = " + AD_Client_ID
					 + "   AND AD_Org_ID     = " + AD_Org_ID
					 + "   AND P_Gain_ID     = " + P_Gain_ID
					 + "   AND C_Activity_Src_ID = " + C_Activity_Src_ID
					 ; 

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (query, null);
			ResultSet rs = pstmt.executeQuery ();
			
			if (rs.next ())
			{
				P_Gain_Account_ID = rs.getInt(1);
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "Get P_Gain_Account Error  ", e);
		}

		if ( P_Gain_Account_ID == 0 )
			return null;
		
		P_Gain_Account Gain_Account = P_Gain_Account.get( ctx, P_Gain_Account_ID , trxName);
		s_cache2.put (Skey, Gain_Account);
	
		return Gain_Account;
	}	//	get


	
	/**	Cache						*/
	private static CCache<Integer,P_Gain_Account>	s_cache = new CCache<Integer,P_Gain_Account>("P_Gain_Account", 20);
	private static CCache<String,P_Gain_Account>	s_cache2 = new CCache<String,P_Gain_Account>("P_Gain_Account", 20);
	
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Payment.class);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Gain_Account_ID id
	 */
	public P_Gain_Account (Properties ctx, int P_Gain_Account_ID, String trxName)
	{
		super (ctx, P_Gain_Account_ID, trxName);
		if (P_Gain_Account_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Gain_Account

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Gain_Account (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Gain_Account (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Gain_Account_ID"), trxName);
	}	//	P_Gain_Account

	


}	//	P_Gain_Account
