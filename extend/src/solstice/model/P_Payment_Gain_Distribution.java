package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/** 
 *  Payment_Gain_Distribution Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Payment_Gain_Distribution extends X_P_Payment_Gain_Distribution
{
	/**
	 * 	Get Payment_Gain_Distribution
	 *	@param ctx context
	 * 	@param P_Payment_Gain_Distribution_ID id
	 *	@return Payment_Gain_Distribution
	 */
	public static P_Payment_Gain_Distribution get (Properties ctx, int P_Payment_Gain_Distribution_ID, String trxName)
	{
		Integer key = new Integer (P_Payment_Gain_Distribution_ID);
		P_Payment_Gain_Distribution Payment_Gain_Distribution = (P_Payment_Gain_Distribution)s_cache.get(key);
		if (Payment_Gain_Distribution != null)
			return Payment_Gain_Distribution;
		Payment_Gain_Distribution = new P_Payment_Gain_Distribution (ctx, P_Payment_Gain_Distribution_ID, trxName);
		s_cache.put (key, Payment_Gain_Distribution);
		return Payment_Gain_Distribution;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Payment_Gain_Distribution>	s_cache = new CCache<Integer,P_Payment_Gain_Distribution>("P_Payment_Gain_Distribution", 20);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Payment_Gain_Distribution_ID id
	 */
	public P_Payment_Gain_Distribution (Properties ctx, int P_Payment_Gain_Distribution_ID, String trxName)
	{
		super (ctx, P_Payment_Gain_Distribution_ID, trxName);
		if (P_Payment_Gain_Distribution_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Payment_Gain_Distribution

	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Payment_Gain_Distribution (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Payment_Gain_Distribution (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Payment_Gain_Distribution_ID"), trxName);
	}	//	P_Payment_Gain_Distribution



}	//	P_Payment_Gain_Distribution
