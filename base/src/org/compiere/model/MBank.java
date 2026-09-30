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

import java.sql.*;
import java.util.*;
import java.util.logging.Level;

import org.compiere.util.*;

/**
 * 	Bank Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MBank.java,v 1.1 2007/07/18 14:39:45 marmor01 Exp $
 */
public class MBank extends X_C_Bank
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get MBank from Cache
	 *	@param ctx context
	 *	@param C_Bank_ID id
	 *	@return MBank
	 */
	public static MBank get (Properties ctx, int C_Bank_ID)
	{
		Integer key = new Integer (C_Bank_ID);
		MBank retValue = (MBank)s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MBank (ctx, C_Bank_ID, null);
		if (retValue.get_ID() != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MBank> s_cache = 
		new CCache<Integer,MBank> ("C_Bank", 3);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Bank_ID bank
	 *	@param trxName trx
	 */
	public MBank (Properties ctx, int C_Bank_ID, String trxName)
	{
		super (ctx, C_Bank_ID, trxName);
	}	//	MBank
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger(MBPBankAccount.class);

	
	/**
	 * 	get Bank by RoutingNo
	 *	@param ctx
	 *	@param routingNo
	 *	@return Array of banks with this RoutingNo
	 */
	public static MBank[] getByRoutingNo (Properties ctx, String routingNo)
	{
		String sql = "SELECT * FROM C_Bank WHERE RoutingNo LIKE ? AND IsActive='Y'";
		ArrayList<MBank> list = new ArrayList<MBank>();
		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, routingNo);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) 
			{
				list.add(new MBank(ctx, rs, null));
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		} 
		catch (Exception e) 
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		try {
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		} catch (Exception e) {
			pstmt = null;
		}
		
		MBank[] retValue = new MBank[list.size()];
		list.toArray(retValue);
		return retValue;
	}

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MBank (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MBank

	/** Verification Class				*/
	private BankVerificationInterface	m_verify = null;
	/** Searched for verification class	*/
	private boolean 					m_verifySearched = false;
	/** Bank Location					*/
	private MLocation					m_loc = null;
	
	/**
	 * 	Get Verification Class
	 *	@return verification class
	 */
	protected BankVerificationInterface getVerificationClass()
	{
		if (m_verify == null && !m_verifySearched)
		{
			String className = getBankVerificationClass();
			if (className == null || className.length() == 0)
				className = MClientInfo.get (getCtx(), getAD_Client_ID()).getBankVerificationClass();
			if (className != null && className.length() > 0)
			{
				try
				{
					Class<?> clazz = Class.forName(className);
					m_verify = (BankVerificationInterface)clazz.newInstance();
				}
				catch (Exception e)
				{
					log.log (Level.SEVERE, className, e);
				}
			}
			m_verifySearched = true;
		}
		return m_verify;
	}	//	getVerificationClass
	
	/**
	 * 	Get C_Country_ID
	 *	@return C_Country_ID
	 */
	public int getC_Country_ID()
	{
		if (m_loc == null)
			m_loc = MLocation.get (getCtx(), getC_Location_ID(), null);
		if (m_loc == null)
			return 0;
		return m_loc.getC_Country_ID();
	}	//	getC_Country_ID
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MBank[");
		sb.append (get_ID ()).append ("-").append(getName ()).append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if valid
	 */
	protected boolean beforeSave(boolean newRecord)
	{
		BankVerificationInterface verify = getVerificationClass();
		if (verify != null)
		{
			String errorMsg = verify.verifyRoutingNo (getC_Country_ID(), getRoutingNo());
			if (errorMsg != null)
			{
				log.saveError("Error", "@Invalid@ @RoutingNo@ " + errorMsg);
				return false;
			}
			errorMsg = verify.verifySwiftCode (getSwiftCode());
			if (errorMsg != null)
			{
				log.saveError("Error", "@Invalid@ @SwiftCode@ " + errorMsg);
				return false;
			}
		}
		return true;
	}	//	beforeSave
	
}	//	MBank
