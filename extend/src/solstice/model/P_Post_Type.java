package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Post_Type Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Post_Type extends X_P_Post_Type
{
	/**
	 * 	Get Post_Type
	 *	@param ctx context
	 * 	@param P_Post_Type_ID id
	 *	@return Post_Type
	 */
	public static P_Post_Type get (Properties ctx, int P_Post_Type_ID, String trxName)
	{
		Integer key = new Integer (P_Post_Type_ID);
		P_Post_Type Post_Type = (P_Post_Type)s_cache.get(key);
		if (Post_Type != null)
			return Post_Type;
		Post_Type = new P_Post_Type (ctx, P_Post_Type_ID, trxName);
		s_cache.put (key, Post_Type);
		return Post_Type;
	}	//	get

	/**	Cache						*/

	private static CCache<Integer,P_Post_Type>	s_cache = new CCache<Integer,P_Post_Type>("P_Post_Type", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Post_Type_ID id
	 */
	public P_Post_Type (Properties ctx, int P_Post_Type_ID, String trxName)
	{
		super (ctx, P_Post_Type_ID, trxName);
		if (P_Post_Type_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Post_Type

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Post_Type (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Post_Type (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Post_Type_ID"), trxName);
	}	//	P_Post_Type



}	//	P_Post_Type
