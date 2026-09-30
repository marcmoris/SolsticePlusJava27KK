package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  LongTermLeave Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_LongTermLeave extends X_P_LongTermLeave
{
	/**
	 * 	Get LongTermLeave
	 *	@param ctx context
	 * 	@param P_LongTermLeave_ID id
	 *	@return LongTermLeave
	 */
	public static P_LongTermLeave get (Properties ctx, int P_LongTermLeave_ID, String trxName)
	{
		Integer key = new Integer (P_LongTermLeave_ID);
		P_LongTermLeave LongTermLeave = (P_LongTermLeave)s_cache.get(key);
		if (LongTermLeave != null)
			return LongTermLeave;
		LongTermLeave = new P_LongTermLeave (ctx, P_LongTermLeave_ID, trxName);
		s_cache.put (key, LongTermLeave);
		return LongTermLeave;
	}	//	get

	/**	Cache						*/
	
	private static CCache<Integer,P_LongTermLeave>	s_cache = new CCache<Integer,P_LongTermLeave>("P_LongTermLeave", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_LongTermLeave_ID id
	 */
	public P_LongTermLeave (Properties ctx, int P_LongTermLeave_ID, String trxName)
	{
		super (ctx, P_LongTermLeave_ID, trxName);
		if (P_LongTermLeave_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_LongTermLeave

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_LongTermLeave (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_LongTermLeave (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_LongTermLeave_ID"), trxName);
	}	//	P_LongTermLeave



}	//	P_LongTermLeave
