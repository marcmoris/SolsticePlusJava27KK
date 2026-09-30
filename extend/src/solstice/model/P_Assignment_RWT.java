package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Assignment_RWT Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Assignment_RWT extends X_P_Assignment_RWT
{
	/**
	 * 	Get Assignment_RWT
	 *	@param ctx context
	 * 	@param P_Assignment_RWT_ID id
	 *	@return Assignment_RWT
	 */
	public static P_Assignment_RWT get (Properties ctx, int P_Assignment_RWT_ID, String trxName)
	{
		Integer key = new Integer (P_Assignment_RWT_ID);
		P_Assignment_RWT Assignment_RWT = (P_Assignment_RWT)s_cache.get(key);
		if (Assignment_RWT != null)
			return Assignment_RWT;
		Assignment_RWT = new P_Assignment_RWT (ctx, P_Assignment_RWT_ID, trxName);
		s_cache.put (key, Assignment_RWT);
		return Assignment_RWT;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Assignment_RWT>	s_cache = new CCache<Integer,P_Assignment_RWT>("P_Assignment_RWT", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Assignment_RWT_ID id
	 */
	public P_Assignment_RWT (Properties ctx, int P_Assignment_RWT_ID, String trxName)
	{
		super (ctx, P_Assignment_RWT_ID, trxName);
		if (P_Assignment_RWT_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Assignment_RWT

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Assignment_RWT (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Assignment_RWT (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Assignment_RWT_ID"), trxName);
	}	//	P_Assignment_RWT


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Assignment_RWT[ID=")
			.append(getP_Assignment_RWT_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Assignment_RWT
