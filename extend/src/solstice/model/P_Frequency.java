package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Frequency Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Frequency extends X_P_Frequency
{
	/**
	 * 	Get Frequency
	 *	@param ctx context
	 * 	@param P_Frequency_ID id
	 *	@return Frequency
	 */
	public static P_Frequency get (Properties ctx, int P_Frequency_ID, String trxName)
	{
		Integer key = new Integer (P_Frequency_ID);
		P_Frequency Frequency = (P_Frequency)s_cache.get(key);
		if (Frequency != null)
			return Frequency;
		Frequency = new P_Frequency (ctx, P_Frequency_ID, trxName);
		s_cache.put (key, Frequency);
		return Frequency;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Frequency>	s_cache = new CCache<Integer,P_Frequency>("P_Frequency", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Frequency_ID id
	 */
	public P_Frequency (Properties ctx, int P_Frequency_ID, String trxName)
	{
		super (ctx, P_Frequency_ID, trxName);
		if (P_Frequency_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Frequency

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Frequency (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Frequency (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Frequency_ID"), trxName);
	}	//	P_Frequency



}	//	P_Frequency
