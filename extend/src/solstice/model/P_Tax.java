package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Tax Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Tax extends X_P_Tax
{
	/**
	 * 	Get Tax
	 *	@param ctx context
	 * 	@param P_Tax_ID id
	 *	@return Tax
	 */
	public static P_Tax get (Properties ctx, int P_Tax_ID, String trxName)
	{
		Integer key = new Integer (P_Tax_ID);
		P_Tax Tax = (P_Tax)s_cache.get(key);
		if (Tax != null)
			return Tax;
		Tax = new P_Tax (ctx, P_Tax_ID, trxName);
		s_cache.put (key, Tax);
		return Tax;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Tax>	s_cache = new CCache<Integer,P_Tax>("P_Tax", 2);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Tax_ID id
	 */
	public P_Tax (Properties ctx, int P_Tax_ID, String trxName)
	{
		super (ctx, P_Tax_ID, trxName);
		if (P_Tax_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Tax

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Tax (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Tax (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Tax_ID"),trxName);
	}	//	P_Tax

	public static int getCurrentTax( Timestamp EffectIn)
	{
		int taxID = 0;

		String query = "Select P_Tax_ID FROM P_Tax "
			+ "WHERE P_Tax.EffectIn = ( Select max( EffectIn ) FROM P_Tax WHERE EffectIn <= " + DB.TO_DATE( EffectIn ) +  ")";
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				taxID = rs.getInt( 1 );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Tax.getCurrentTax - " + e);
		}

		return taxID;
		
	}

	public static int getCurrentTax()
	{
		int taxID = 0;

		String query = "Select P_Tax_ID FROM P_Tax "
			+ "WHERE P_Tax.EffectIn = ( Select max( EffectIn ) FROM P_Tax )";
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				taxID = rs.getInt( 1 );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Tax.getCurrentTax - " + e);
		}

		return taxID;
		
	}

	public static BigDecimal getTD1_info( int P_Tax_ID, String param)
	{
		String sql = "select Amount from P_Tax_Federal_Td1 " 
			       + " Where P_Tax_ID = " + P_Tax_ID 
			       + " And Value = '" + param + "'";

		BigDecimal result = null;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getBigDecimal("Amount");
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Tax.getTD1_info - " + e);
		}
	
		return result;
	}

	public static BigDecimal getTD1Provincial_info( int P_Tax_ID, int P_Tax_Provincial_ID, String param)
	{
		String sql = "select P_Tax_Provincial_TD1.Amount from P_Tax_Provincial_TD1, P_Tax_Provincial "
			       + " where P_Tax_Provincial.P_Tax_ID = " + P_Tax_ID
			       + " And P_Tax_Provincial.P_Tax_Provincial_ID = P_Tax_Provincial_TD1.P_Tax_Provincial_ID "
			       + " And P_Tax_Provincial.C_Region_ID = " + P_Tax_Provincial_ID 
			       + " And P_Tax_Provincial_TD1.Value = '" + param + "'";

		BigDecimal result = null;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getBigDecimal("Amount");
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Tax.getTP1015_info - " + e);
		}
	
		return result;
	}

}	//	P_Tax
