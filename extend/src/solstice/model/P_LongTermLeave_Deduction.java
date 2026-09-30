package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  LongTermLeave_Deduction Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_LongTermLeave_Deduction extends X_P_LongTermLeave_Deduction
{
	/**
	 * 	Get LongTermLeave_Deduction
	 *	@param ctx context
	 * 	@param P_LongTermLeave_Deduction_ID id
	 *	@return LongTermLeave_Deduction
	 */
	public static P_LongTermLeave_Deduction get (Properties ctx, int P_LongTermLeave_Deduction_ID, String trxName)
	{
		Integer key = new Integer (P_LongTermLeave_Deduction_ID);
		P_LongTermLeave_Deduction LongTermLeave_Deduction = (P_LongTermLeave_Deduction)s_cache.get(key);
		if (LongTermLeave_Deduction != null)
			return LongTermLeave_Deduction;
		LongTermLeave_Deduction = new P_LongTermLeave_Deduction (ctx, P_LongTermLeave_Deduction_ID, trxName);
		s_cache.put (key, LongTermLeave_Deduction);
		return LongTermLeave_Deduction;
	}	//	get

	/**	Cache						*/
	
	private static CCache<Integer,P_LongTermLeave_Deduction>	s_cache = new CCache<Integer,P_LongTermLeave_Deduction>("P_LongTermLeave_Deduction", 20);

	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_LongTermLeave_Deduction_ID id
	 */
	public P_LongTermLeave_Deduction (Properties ctx, int P_LongTermLeave_Deduction_ID, String trxName)
	{
		super (ctx, P_LongTermLeave_Deduction_ID, trxName);
		if (P_LongTermLeave_Deduction_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_LongTermLeave_Deduction

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_LongTermLeave_Deduction (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_LongTermLeave_Deduction (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_LongTermLeave_Deduction_ID"), trxName);
	}	//	P_LongTermLeave_Deduction



}	//	P_LongTermLeave_Deduction
