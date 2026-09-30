package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Deduction_Insurance_Bound Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Deduction_Insurance_Bound extends X_P_Deduction_Insurance_Bound
{
	/**
	 * 	Get Deduction_Insurance_Bound
	 *	@param ctx context
	 * 	@param P_Deduction_Insurance_Bound_ID id
	 *	@return Deduction_Insurance_Bound
	 */
	public static P_Deduction_Insurance_Bound get (Properties ctx, int P_Deduction_Insurance_Bound_ID, String trxName)
	{
		Integer key = new Integer (P_Deduction_Insurance_Bound_ID);
		P_Deduction_Insurance_Bound Deduction_Insurance_Bound = (P_Deduction_Insurance_Bound)s_cache.get(key);
		if (Deduction_Insurance_Bound != null)
			return Deduction_Insurance_Bound;
		Deduction_Insurance_Bound = new P_Deduction_Insurance_Bound (ctx, P_Deduction_Insurance_Bound_ID, trxName);
		s_cache.put (key, Deduction_Insurance_Bound);
		return Deduction_Insurance_Bound;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Deduction_Insurance_Bound>	s_cache = new CCache<Integer,P_Deduction_Insurance_Bound>("P_Deduction_Insurance_Bound", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Deduction_Insurance_Bound_ID id
	 */
	public P_Deduction_Insurance_Bound (Properties ctx, int P_Deduction_Insurance_Bound_ID, String trxName)
	{
		super (ctx, P_Deduction_Insurance_Bound_ID, trxName);
		if (P_Deduction_Insurance_Bound_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Deduction_Insurance_Bound

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Deduction_Insurance_Bound (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Deduction_Insurance_Bound (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Deduction_Insurance_Bound_ID"), trxName);
	}	//	P_Deduction_Insurance_Bound



}	//	P_Deduction_Insurance_Bound
