package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  LongTermLeave_Gain Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_LongTermLeave_Gain extends X_P_LongTermLeave_Gain
{
	/**
	 * 	Get LongTermLeave_Gain
	 *	@param ctx context
	 * 	@param P_LongTermLeave_Gain_ID id
	 *	@return LongTermLeave_Gain
	 */
	public static P_LongTermLeave_Gain get (Properties ctx, int P_LongTermLeave_Gain_ID, String trxName)
	{
		Integer key = new Integer (P_LongTermLeave_Gain_ID);
		P_LongTermLeave_Gain LongTermLeave_Gain = (P_LongTermLeave_Gain)s_cache.get(key);
		if (LongTermLeave_Gain != null)
			return LongTermLeave_Gain;
		LongTermLeave_Gain = new P_LongTermLeave_Gain (ctx, P_LongTermLeave_Gain_ID, trxName);
		s_cache.put (key, LongTermLeave_Gain);
		return LongTermLeave_Gain;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_LongTermLeave_Gain>	s_cache = new CCache<Integer,P_LongTermLeave_Gain>("P_LongTermLeave_Gain", 20);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_LongTermLeave_Gain_ID id
	 */
	public P_LongTermLeave_Gain (Properties ctx, int P_LongTermLeave_Gain_ID, String trxName)
	{
		super (ctx, P_LongTermLeave_Gain_ID, trxName);
		if (P_LongTermLeave_Gain_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_LongTermLeave_Gain

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_LongTermLeave_Gain (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_LongTermLeave_Gain (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_LongTermLeave_Gain_ID"), trxName);
	}	//	P_LongTermLeave_Gain



}	//	P_LongTermLeave_Gain
