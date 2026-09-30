package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MRole;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  BankAccountDoc Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_BankAccountDoc extends X_P_BankAccountDoc
{
	/**
	 * 	Get BankAccountDoc
	 *	@param ctx context
	 * 	@param P_BankAccountDoc_ID id
	 *	@return BankAccountDoc
	 */
	public static P_BankAccountDoc get (Properties ctx, int P_BankAccountDoc_ID, String trxName)
	{
		Integer key = new Integer (P_BankAccountDoc_ID);
		P_BankAccountDoc BankAccountDoc = (P_BankAccountDoc)s_cache.get(key);
		if (BankAccountDoc != null)
			return BankAccountDoc;
		BankAccountDoc = new P_BankAccountDoc (ctx, P_BankAccountDoc_ID, trxName);
		s_cache.put (key, BankAccountDoc);
		return BankAccountDoc;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_BankAccountDoc>	s_cache = new CCache<Integer,P_BankAccountDoc>("P_BankAccountDoc", 20);
	private static CCache<String,P_BankAccountDoc>	s_cache2 = new CCache<String,P_BankAccountDoc>("P_BankAccountDoc", 20);
	/**	Logger							*/
	private static CLogger		log = CLogger.getCLogger (P_BankAccountDoc.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_BankAccountDoc_ID id
	 */
	public P_BankAccountDoc (Properties ctx, int P_BankAccountDoc_ID, String trxName)
	{
		super (ctx, P_BankAccountDoc_ID, trxName);
		if (P_BankAccountDoc_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_BankAccountDoc

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_BankAccountDoc (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_BankAccountDoc (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_BankAccountDoc_ID"), trxName);
	}	//	P_BankAccountDoc

	public static P_BankAccountDoc getWithPaymentRule(Properties ctx, String PaymentRule, String trxName)
	{
		int BankAccountDocID = -1;

		String sql = "Select P_BankAccountDoc_ID From P_BankAccountDoc, P_BankAccount Where PaymentRule = " + PaymentRule 
		           + " and P_BankAccount.P_BankAccount_ID = P_BankAccountDoc.P_BankAccount_ID"
		           + " and P_BankAccount.isDefault = 'Y'";

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				BankAccountDocID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
//			log.error ("P_BankAccountDoc - getWithPaymentRule - " + sql, e);
		}

		return P_BankAccountDoc.get( ctx, BankAccountDocID, trxName);
	}

	
	public static P_BankAccountDoc getWithBankAccount (Properties ctx, int P_BankAccount_ID, String PaymentRule, String trxName)
	{
		String key = String.valueOf( P_BankAccount_ID ) + PaymentRule;
		P_BankAccountDoc BankAccountDoc = (P_BankAccountDoc)s_cache2.get(key);
		if (BankAccountDoc != null)
			return BankAccountDoc;

		int BankAccountDocID=0;
		String sql="SELECT P_BANKACCOUNTDOC_ID,CURRENTNEXT FROM P_BANKACCOUNTDOC WHERE PAYMENTRULE='" + PaymentRule + "'";
		
		//2012.09.04
		sql += " AND P_BankAccount_ID = " + P_BankAccount_ID;
//		sql += " AND AD_ORG_ID IN ( " + AD_Org_ID + ", 0)";
		
		sql = MRole.getDefault().addAccessSQL( sql , "P_BANKACCOUNTDOC", true, false);

			
		ResultSet rsBankAccountDoc=null;
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt= DB.prepareStatement(sql, null);
			rsBankAccountDoc= pstmt.executeQuery();
			
			if (	rsBankAccountDoc.next() )
				BankAccountDocID=rsBankAccountDoc.getInt("P_BANKACCOUNTDOC_ID");
			rsBankAccountDoc.close();
			pstmt.close();
			
				
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "P_BankAccountDoc - getWithBankAccount - " + sql, e);
		}

		if ( BankAccountDocID == 0)
			return null;
		
		BankAccountDoc = P_BankAccountDoc.get( ctx, BankAccountDocID, trxName) ;
		s_cache2.put (key, BankAccountDoc);

		return BankAccountDoc;

	}

}	//	P_BankAccountDoc
