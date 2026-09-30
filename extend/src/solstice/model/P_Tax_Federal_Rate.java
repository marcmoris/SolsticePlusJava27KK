package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Tax_Federal_Rate Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Tax_Federal_Rate extends X_P_Tax_Federal_Rate
{
	/**
	 * 	Get Tax_Federal_Rate
	 *	@param ctx context
	 * 	@param P_Tax_Federal_Rate_ID id
	 *	@return Tax_Federal_Rate
	 */
	public static P_Tax_Federal_Rate get (Properties ctx, int P_Tax_Federal_Rate_ID, String trxName)
	{
		Integer key = new Integer (P_Tax_Federal_Rate_ID);
		P_Tax_Federal_Rate Tax_Federal_Rate = (P_Tax_Federal_Rate)s_cache.get(key);
		if (Tax_Federal_Rate != null)
			return Tax_Federal_Rate;
		Tax_Federal_Rate = new P_Tax_Federal_Rate (ctx, P_Tax_Federal_Rate_ID, trxName);
		s_cache.put (key, Tax_Federal_Rate);
		return Tax_Federal_Rate;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Tax_Federal_Rate>	s_cache = new CCache<Integer,P_Tax_Federal_Rate>("P_Tax_Federal_Rate", 15);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Tax_Federal_Rate_ID id
	 */
	public P_Tax_Federal_Rate (Properties ctx, int P_Tax_Federal_Rate_ID, String trxName)
	{
		super (ctx, P_Tax_Federal_Rate_ID, trxName);
		if (P_Tax_Federal_Rate_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Tax_Federal_Rate

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Tax_Federal_Rate (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Tax_Federal_Rate (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Tax_Federal_Rate_ID"),trxName);
	}	//	P_Tax_Federal_Rate



}	//	P_Tax_Federal_Rate
