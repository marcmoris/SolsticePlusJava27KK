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
public class P_Bonus extends X_P_Bonus
{
	/**
	 * 	Get Bonus
	 *	@param ctx context
	 * 	@param P_Bonus_ID id
	 *	@return Bonus
	 */
	public static P_Bonus get (Properties ctx, int P_Bonus_ID, String trxName)
	{
		Integer key = new Integer (P_Bonus_ID);
		P_Bonus Bonus = (P_Bonus)s_cache.get(key);
		if (Bonus != null)
			return Bonus;
		Bonus = new P_Bonus (ctx, P_Bonus_ID, trxName);
		s_cache.put (key, Bonus);
		return Bonus;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Bonus>	s_cache = new CCache<Integer,P_Bonus>("P_Bonus", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Bonus_ID id
	 */
	public P_Bonus (Properties ctx, int P_Bonus_ID, String trxName)
	{
		super (ctx, P_Bonus_ID, trxName);
		if (P_Bonus_ID == 0)
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
	public P_Bonus (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Bonus (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Bonus_ID"), trxName);
	}	//	P_Bonus


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Bonus[ID=")
			.append(this.getP_Bonus_ID())
//			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Bonus
