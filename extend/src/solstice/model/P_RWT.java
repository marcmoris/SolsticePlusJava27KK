package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  RWT Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_RWT extends X_P_RWT
{
	/**
	 * 	Get RWT
	 *	@param ctx context
	 * 	@param P_RWT_ID id
	 *	@return RWT
	 */
	public static P_RWT get (Properties ctx, int P_RWT_ID, String trxName)
	{
		Integer key = new Integer (P_RWT_ID);
		P_RWT RWT = (P_RWT)s_cache.get(key);
		if (RWT != null)
			return RWT;
		RWT = new P_RWT (ctx, P_RWT_ID, trxName);
		s_cache.put (key, RWT);
		return RWT;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_RWT>	s_cache = new CCache<Integer,P_RWT>("P_RWT", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_RWT_ID id
	 */
	public P_RWT (Properties ctx, int P_RWT_ID, String trxName)
	{
		super (ctx, P_RWT_ID, trxName);
		if (P_RWT_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_RWT

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_RWT (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_RWT (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_RWT_ID"), trxName);
	}	//	P_RWT


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_RWT[ID=")
			.append(getP_RWT_ID())
			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_RWT
