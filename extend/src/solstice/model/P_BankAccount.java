/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/
package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  BankAccount Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_BankAccount extends X_P_BankAccount
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get BankAccount
	 *	@param ctx context
	 * 	@param P_BankAccount_ID id
	 *	@return BankAccount
	 */
	public static P_BankAccount get (Properties ctx, int P_BankAccount_ID, String trxName)
	{
		Integer key = new Integer (P_BankAccount_ID);
		P_BankAccount BankAccount = (P_BankAccount)s_cache.get(key);
		if (BankAccount != null)
			return BankAccount;
		BankAccount = new P_BankAccount (ctx, P_BankAccount_ID, trxName);
		s_cache.put (key, BankAccount);
		return BankAccount;
	}	//	get
 
	
	/**	Cache						*/
	private static CCache<Integer,P_BankAccount>	s_cache = new CCache<Integer,P_BankAccount>("P_BankAccount", 20);
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_BankAccount_ID id
	 */
	public P_BankAccount (Properties ctx, int P_BankAccount_ID, String trxName)
	{
		super (ctx, P_BankAccount_ID, trxName);
		if (P_BankAccount_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_BankAccount

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_BankAccount (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_BankAccount (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_BankAccount_ID"), trxName);
	}	//	P_BankAccount



	public static P_BankAccount getByOrgID (Properties ctx, int AD_Org_ID, int P_Bank_ID, String trxName)
	{
		int BankAccountID = 0;

		String sql = "Select P_BankAccount_ID From P_BankAccount" 
				   + " Inner join P_Bank on P_BankAccount.P_Bank_ID = P_Bank.P_Bank_ID and P_Bank.isActive = 'Y' "
				   + " Where P_Bank.P_Bank_ID = " + P_Bank_ID  + "  order By P_BankAccount.AD_Org_ID Desc ";

//AND P_BankAccount.AD_Org_ID in ( ?, 0)
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
//			pstmt.setInt( 1, AD_Org_ID );
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next() )
			    BankAccountID = rs.getInt( "P_BankAccount_ID");

		}
		catch (Exception e)
		{
			System.out.println ("* Error * P_BankAccount.getByOrgID - " + sql + " - " + e);

		}
		
		if (BankAccountID == 0 )
		    return null;
		
		return P_BankAccount.get( ctx, BankAccountID, trxName );
	    
	}	//	get

	public static P_BankAccount getByClientID (Properties ctx, int AD_Client_ID, String trxName)
	{
		int BankAccountID = 0;

		String sql = "Select P_BankAccount_ID From P_BankAccount Where AD_Client_ID = ?";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt( 1, AD_Client_ID );
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next() )
			    BankAccountID = rs.getInt( "P_BankAccount_ID");

		}
		catch (Exception e)
		{
			System.out.println ("* Error * P_BankAccount.getByClientID - " + sql + " - " + e);

		}
		
		if (BankAccountID == 0 )
		    return null;
		
		return P_BankAccount.get( ctx, BankAccountID, trxName );
	    
	}	//	get


}	//	P_BankAccount
