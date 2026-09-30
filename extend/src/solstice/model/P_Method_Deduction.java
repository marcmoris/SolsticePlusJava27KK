package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Method_Deduction Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Method_Deduction extends X_P_Method_Deduction
{
	/**
	 * 	Get Method_Deduction
	 *	@param ctx context
	 * 	@param P_Method_Deduction_ID id
	 *	@return Method_Deduction
	 */
	public static P_Method_Deduction get (Properties ctx, int P_Method_Deduction_ID, String trxName)
	{
		Integer key = new Integer (P_Method_Deduction_ID);
		P_Method_Deduction Method_Deduction = (P_Method_Deduction)s_cache.get(key);
		if (Method_Deduction != null)
			return Method_Deduction;
		Method_Deduction = new P_Method_Deduction (ctx, P_Method_Deduction_ID, trxName);
		s_cache.put (key, Method_Deduction);
		return Method_Deduction;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Method_Deduction>	s_cache = new CCache<Integer,P_Method_Deduction>("P_Method_Deduction", 20);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Method_Deduction_ID id
	 */
	public P_Method_Deduction (Properties ctx, int P_Method_Deduction_ID, String trxName)
	{
		super (ctx, P_Method_Deduction_ID, trxName);
		if (P_Method_Deduction_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Method_Deduction

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Method_Deduction (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	
	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Method_Deduction (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Method_Deduction_ID"),trxName);
	}	//	P_Method_Deduction



}	//	P_Method_Deduction
