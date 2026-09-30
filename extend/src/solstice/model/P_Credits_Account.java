package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.process.Ledger_Entry;

/**
 *  Credits_Account Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Credits_Account extends X_P_Credits_Account
{
	/**
	 * 	Get Credits_Account
	 *	@param ctx context
	 * 	@param P_Credits_Account_ID id
	 *	@return Credits_Account
	 */
	public static P_Credits_Account get (Properties ctx, int P_Credits_Account_ID, String trxName)
	{
		Integer key = new Integer (P_Credits_Account_ID);
		P_Credits_Account Credits_Account = (P_Credits_Account)s_cache.get(key);
		if (Credits_Account != null)
			return Credits_Account;
		Credits_Account = new P_Credits_Account (ctx, P_Credits_Account_ID,  trxName);
		s_cache.put (key, Credits_Account);
		return Credits_Account;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Credits_Account>	s_cache = new CCache<Integer,P_Credits_Account>("P_Credits_Account", 20);

	/**	Static Logger				*/
	private static CLogger		log = CLogger.getCLogger (P_Credits_Account.class);

	private static CCache<String,P_Credits_Account>	s_cache2 = new CCache<String,P_Credits_Account>("P_Credits_Account", 20);
	

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Credits_Account_ID id
	 */
	public P_Credits_Account (Properties ctx, int P_Credits_Account_ID, String trxName)
	{
		super (ctx, P_Credits_Account_ID, trxName);
		if (P_Credits_Account_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Credits_Account

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Credits_Account (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Credits_Account (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Credits_Account_ID"), trxName);
	}	//	P_Credits_Account


	public static P_Credits_Account getWithOccupationGroup(Properties ctx, 
			int P_Credits_ID, 
			int P_Occupation_Group_ID,
			int P_Job_Type_ID,
			int Job_Title_ID,
			String trxName)
	{
		int CreditsAccountID = 0;

		String sql = " SELECT P_Credits_Account_ID FROM P_Credits_Account " +
			      	 " WHERE P_Credits_ID = ? "//1-Credits_ID 
					+ " AND isnull(P_Occupation_Group_ID, ?) = ? "//2-3-Occupation_Group_ID
					+ " AND isnull(P_Job_Type_ID, ?) = ? "//4-5-Job_Type_ID
 				    + " AND ( P_Job_Title_ID = " + Job_Title_ID + " OR P_Job_Title_ID is null )"
					+ " ORDER BY P_Occupation_Group_ID, P_Job_Type_ID";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt( 1, P_Credits_ID );
			pstmt.setInt( 2, P_Occupation_Group_ID );
			pstmt.setInt( 3, P_Occupation_Group_ID );
			pstmt.setInt( 4, P_Job_Type_ID );
			pstmt.setInt( 5, P_Job_Type_ID );
			
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next() )
			    CreditsAccountID = rs.getInt( "P_Credits_Account_ID");

		}
		catch (Exception e)
		{
			System.out.println ("* Error * P_Credits_Account_ID.getWithOccupationGroup - " + sql + " - " + e);
		}
		
		if ( CreditsAccountID == 0)
		    return null;
		
		return P_Credits_Account.get( ctx, CreditsAccountID , trxName);
	    
	}

	public static P_Credits_Account getWithSalesRegion (Properties ctx, int AD_Client_ID, int AD_Org_ID, int C_SalesRegion_ID, int P_Credits_ID, String trxName )
	{

		String Skey = P_Credits_ID + "." + AD_Client_ID + "." + AD_Org_ID + "." + C_SalesRegion_ID;
		P_Credits_Account CreditsAccount = (P_Credits_Account)s_cache2.get(Skey);
		if (CreditsAccount != null)
			return CreditsAccount;

		int CreditsAccountID = 0;

		String query = " SELECT P_Credits_Account_ID FROM P_Credits_Account " +
			      	 " WHERE P_Credits_ID = ? "//1-Credits_ID 
					+ " AND AD_Org_ID = ? "//2-AD_Org_ID
 				    + " AND  C_SalesRegion_ID = ? "
					;
		
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (query, null);
			pstmt.setInt( 1, P_Credits_ID );
			pstmt.setInt( 2, AD_Org_ID );
			pstmt.setInt( 3, C_SalesRegion_ID );
			
			ResultSet rs = pstmt.executeQuery ();

			
			if (rs.next ())
			{
				CreditsAccountID = rs.getInt(1);
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Get P_Credits_Account Error  ", e);
		}

		
		if ( CreditsAccountID == 0 )
			return null;
		
		P_Credits_Account Credits_Account = P_Credits_Account.get( ctx, CreditsAccountID , trxName);
		s_cache2.put (Skey, Credits_Account);
	
		return Credits_Account;
	}	//	get

	
	
	public static P_Credits_Account getWithActivity(Properties ctx, 
			int P_Credits_ID, 
			int AD_Org_ID,
			int C_Activity_ID,
			String trxName)
	{
		int CreditsAccountID = 0;

		String sql = " SELECT P_Credits_Account_ID FROM P_Credits_Account " +
			      	 " WHERE P_Credits_ID = ? "//1-Credits_ID 
					+ " AND AD_Org_ID = ? "//2-AD_Org_ID
 				    + " AND ( C_Activity_Src_ID = ?  OR C_Activity_Src_ID is null )"
					+ " ORDER BY C_Activity_ID";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt( 1, P_Credits_ID );
			pstmt.setInt( 2, AD_Org_ID );
			pstmt.setInt( 3, C_Activity_ID );
			
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next() )
			    CreditsAccountID = rs.getInt( "P_Credits_Account_ID");

		}
		catch (Exception e)
		{
			System.out.println ("* Error * P_Credits_Account_ID.getWithActivity - " + sql + " - " + e);
            log.log (Level.SEVERE,"P_Credits_Account_ID.getWithActivity " + sql, e);

		}
		
		if ( CreditsAccountID == 0)
		    return null;
		
		return P_Credits_Account.get( ctx, CreditsAccountID , trxName);
	    
	}

	
	public static P_Credits_Account get (Properties ctx, int P_Credits_ID, int AD_Client_ID, int AD_Org_ID, int C_Activity_Src_ID, String trxName )
	{


		int P_Credits_Account_ID = 0;
		String sql = "Select P_Credits_Account_ID "
			         + " FROM P_Credits_Account "
					 + " WHERE AD_Client_ID  = " + AD_Client_ID
					 + "   AND AD_Org_ID     = " + AD_Org_ID
					 + "   AND P_Credits_ID     = " + P_Credits_ID
					 + "   AND C_Activity_Src_ID = " + C_Activity_Src_ID
					 ; 

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			
			if (rs.next ())
			{
				P_Credits_Account_ID = rs.getInt(1);
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println ("* Error * P_Credits_Account_ID.get - " + sql + " - " + e);
		}

		if ( P_Credits_Account_ID == 0 )
			return null;
		
		return P_Credits_Account.get( ctx, P_Credits_Account_ID , trxName);
	}	//	get

	
}	//	P_Credits_Account
