package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Deduction_Insurance Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Deduction_Insurance extends X_P_Deduction_Insurance
{
	/**
	 * 	Get Deduction_Insurance
	 *	@param ctx context
	 * 	@param P_Deduction_Insurance_ID id
	 *	@return Deduction_Insurance
	 */
	public static P_Deduction_Insurance get (Properties ctx, int P_Deduction_Insurance_ID, String trxName)
	{
		Integer key = new Integer (P_Deduction_Insurance_ID);
		P_Deduction_Insurance Deduction_Insurance = (P_Deduction_Insurance)s_cache.get(key);
		if (Deduction_Insurance != null)
			return Deduction_Insurance;
		Deduction_Insurance = new P_Deduction_Insurance (ctx, P_Deduction_Insurance_ID, trxName);
		s_cache.put (key, Deduction_Insurance);
		return Deduction_Insurance;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Deduction_Insurance>	s_cache = new CCache<Integer,P_Deduction_Insurance>("P_Deduction_Insurance", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Deduction_Insurance_ID id
	 */
	public P_Deduction_Insurance (Properties ctx, int P_Deduction_Insurance_ID, String trxName)
	{
		super (ctx, P_Deduction_Insurance_ID, trxName);
		if (P_Deduction_Insurance_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Deduction_Insurance

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Deduction_Insurance (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Deduction_Insurance (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Deduction_Insurance_ID"), trxName);
	}	//	P_Deduction_Insurance



}	//	P_Deduction_Insurance
