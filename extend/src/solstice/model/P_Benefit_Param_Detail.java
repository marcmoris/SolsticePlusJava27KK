package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Bonus Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Benefit_Param_Detail extends X_P_Benefit_Param_Detail
{
	/**
	 * 	Get Bonus
	 *	@param ctx context
	 * 	@param P_Bonus_ID id
	 *	@return Bonus
	 */
	public static P_Benefit_Param_Detail get (Properties ctx, int P_Benefit_Param_Detail_ID, String trxName)
	{
		Integer key = new Integer (P_Benefit_Param_Detail_ID);
		P_Benefit_Param_Detail Bonus = (P_Benefit_Param_Detail)s_cache.get(key);
		if (Bonus != null)
			return Bonus;
		Bonus = new P_Benefit_Param_Detail (ctx, P_Benefit_Param_Detail_ID, trxName);
		s_cache.put (key, Bonus);
		return Bonus;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Benefit_Param_Detail>	s_cache = new CCache<Integer,P_Benefit_Param_Detail>("P_Bonus", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Bonus_ID id
	 */
	public P_Benefit_Param_Detail (Properties ctx, int P_Benefit_Param_Detail_ID, String trxName)
	{
		super (ctx, P_Benefit_Param_Detail_ID, trxName);
		if (P_Benefit_Param_Detail_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Bonus

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Benefit_Param_Detail (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Benefit_Param_Detail (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Benefit_Param_Detail_ID"), trxName);
	}	//	P_Bonus


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
/*	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Bonus[ID=")
			.append(this.getP_Bonus_ID())
//			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString*/

}	//	P_Bonus
