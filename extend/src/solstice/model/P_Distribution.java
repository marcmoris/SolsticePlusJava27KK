package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Distribution Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Distribution extends X_P_Distribution
{
	/**
	 * 	Get Distribution
	 *	@param ctx context
	 * 	@param P_Distribution_ID id
	 *	@return Distribution
	 */
	public static P_Distribution get (Properties ctx, int P_Distribution_ID, String trxName)
	{
		Integer key = new Integer (P_Distribution_ID);
		P_Distribution Distribution = (P_Distribution)s_cache.get(key);
		if (Distribution != null)
			return Distribution;
		Distribution = new P_Distribution (ctx, P_Distribution_ID, trxName);
		s_cache.put (key, Distribution);
		return Distribution;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Distribution>	s_cache = new CCache<Integer,P_Distribution>("P_Distribution", 20);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Distribution_ID id
	 */
	public P_Distribution (Properties ctx, int P_Distribution_ID, String trxName)
	{
		super (ctx, P_Distribution_ID, trxName);
		if (P_Distribution_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Distribution

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Distribution (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Distribution (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Distribution_ID"), trxName);
	}	//	P_Distribution



}	//	P_Distribution
