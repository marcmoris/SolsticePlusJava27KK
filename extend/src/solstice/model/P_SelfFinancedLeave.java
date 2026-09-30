package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  SelfFinancedLeave Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_SelfFinancedLeave extends X_P_SelfFinancedLeave
{
	/**
	 * 	Get SelfFinancedLeave
	 *	@param ctx context
	 * 	@param P_SelfFinancedLeave_ID id
	 *	@return SelfFinancedLeave
	 */
	public static P_SelfFinancedLeave get (Properties ctx, int P_SelfFinancedLeave_ID, String trxName)
	{
		Integer key = new Integer (P_SelfFinancedLeave_ID);
		P_SelfFinancedLeave SelfFinancedLeave = (P_SelfFinancedLeave)s_cache.get(key);
		if (SelfFinancedLeave != null)
			return SelfFinancedLeave;
		SelfFinancedLeave = new P_SelfFinancedLeave (ctx, P_SelfFinancedLeave_ID, trxName);
		s_cache.put (key, SelfFinancedLeave);
		return SelfFinancedLeave;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_SelfFinancedLeave>	s_cache = new CCache<Integer,P_SelfFinancedLeave>("P_SelfFinancedLeave", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_SelfFinancedLeave_ID id
	 */
	public P_SelfFinancedLeave (Properties ctx, int P_SelfFinancedLeave_ID, String trxName)
	{
		super (ctx, P_SelfFinancedLeave_ID, trxName);
		if (P_SelfFinancedLeave_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_SelfFinancedLeave

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_SelfFinancedLeave (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_SelfFinancedLeave (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_SelfFinancedLeave_ID"), trxName);
	}	//	P_SelfFinancedLeave



}	//	P_SelfFinancedLeave
