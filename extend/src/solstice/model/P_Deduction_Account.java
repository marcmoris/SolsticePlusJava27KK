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
 *  Deduction_Account Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Deduction_Account extends X_P_Deduction_Account
{
	/**
	 * 	Get Deduction_Account
	 *	@param ctx context
	 * 	@param P_Deduction_Account_ID id
	 *	@return Deduction_Account
	 */
	public static P_Deduction_Account get (Properties ctx, int P_Deduction_Account_ID, String trxName)
	{
		Integer key = new Integer (P_Deduction_Account_ID);
		P_Deduction_Account Deduction_Account = (P_Deduction_Account)s_cache.get(key);
		if (Deduction_Account != null)
			return Deduction_Account;
		Deduction_Account = new P_Deduction_Account (ctx, P_Deduction_Account_ID, trxName);
		s_cache.put (key, Deduction_Account);
		return Deduction_Account;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Deduction_Account>	s_cache = new CCache<Integer,P_Deduction_Account>("P_Deduction_Account", 20);
	private static CCache<String,P_Deduction_Account>	s_cache2 = new CCache<String,P_Deduction_Account>("P_Deduction_Account", 20);
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Deduction_Account.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Deduction_Account_ID id
	 */
	public P_Deduction_Account (Properties ctx, int P_Deduction_Account_ID, String trxName)
	{
		super (ctx, P_Deduction_Account_ID, trxName);
		if (P_Deduction_Account_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Deduction_Account

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Deduction_Account (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Deduction_Account (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Deduction_Account_ID"), trxName);
	}	//	P_Deduction_Account

	public static P_Deduction_Account get (Properties ctx, int P_Deduction_ID, int P_Job_Title_ID, int P_Job_Type_ID,  String trxName )
	{
		
		String Skey = P_Deduction_ID + "." + P_Job_Title_ID + "." + P_Job_Type_ID;
		P_Deduction_Account DeductionAccount = (P_Deduction_Account)s_cache2.get(Skey);
		if (DeductionAccount != null)
			return DeductionAccount;

		
		int P_Deduction_Account_ID = 0;
		
		String sql = "Select P_Deduction_Account_ID From P_Deduction_Account "
			       + " Where P_Deduction_ID =" + P_Deduction_ID
		           + "  AND P_Job_Title_ID = " + P_Job_Title_ID 
		           + "  AND P_Job_Type_ID  = " + P_Job_Type_ID
		;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				P_Deduction_Account_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Deduction_Account - Get - " + sql, e);
		}
		
		if ( P_Deduction_Account_ID == 0 )
			return null;
		
		P_Deduction_Account DedAccount = P_Deduction_Account.get( ctx, P_Deduction_Account_ID, trxName); 
		s_cache2.put (Skey, DedAccount);

		return DedAccount;
	}

	public static P_Deduction_Account getWithSaleRegion (Properties ctx, int AD_Client_ID, int AD_Org_ID, int C_SalesRegion_ID, int P_Deduction_ID,  String trxName )
	{
		
		String Skey = P_Deduction_ID + "." + AD_Org_ID + "." + C_SalesRegion_ID + "." + AD_Client_ID;
		P_Deduction_Account DeductionAccount = (P_Deduction_Account)s_cache2.get(Skey);
		if (DeductionAccount != null)
			return DeductionAccount;

		
		int P_Deduction_Account_ID = 0;
		
		String sql = "Select P_Deduction_Account_ID From P_Deduction_Account "
			       + " Where P_Deduction_ID =" + P_Deduction_ID
			       + "  AND  AD_Client_ID = " + AD_Client_ID
			       + "  AND AD_Org_ID = " + AD_Org_ID
			       + "  AND C_SalesRegion_ID = " + C_SalesRegion_ID
		;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				P_Deduction_Account_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Deduction_Account - Get - " + sql, e);
		}
		
		if ( P_Deduction_Account_ID == 0 )
			return null;
		
		P_Deduction_Account DedAccount = P_Deduction_Account.get( ctx, P_Deduction_Account_ID, trxName); 
		s_cache2.put (Skey, DedAccount);

		return DedAccount;
	}



	public static P_Deduction_Account get (Properties ctx, int AD_Client_ID, int AD_Org_ID, int C_Activity_Src_ID, int P_Deduction_ID,  String trxName )
	{
		
		String Skey = P_Deduction_ID + "." + AD_Org_ID + "." + C_Activity_Src_ID + "." + AD_Client_ID;
		P_Deduction_Account DeductionAccount = (P_Deduction_Account)s_cache2.get(Skey);
		if (DeductionAccount != null)
			return DeductionAccount;

		
		int P_Deduction_Account_ID = 0;
		
		String sql = "Select P_Deduction_Account_ID From P_Deduction_Account "
			       + " Where P_Deduction_ID =" + P_Deduction_ID
			       + "  AND  AD_Client_ID = " + AD_Client_ID
			       + "  AND AD_Org_ID = " + AD_Org_ID
			       + "  AND C_Activity_Src_ID = " + C_Activity_Src_ID
		;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				P_Deduction_Account_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Deduction_Account - Get - " + sql, e);
		}
		
		if ( P_Deduction_Account_ID == 0 )
			return null;
		
		P_Deduction_Account DedAccount = P_Deduction_Account.get( ctx, P_Deduction_Account_ID, trxName); 
		s_cache2.put (Skey, DedAccount);

		return DedAccount;
	}


	
}	//	P_Deduction_Account
