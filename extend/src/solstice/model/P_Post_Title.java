package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Post_Title Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Post_Title extends X_P_Post_Title
{
	/**
	 * 	Get Post_Title
	 *	@param ctx context
	 * 	@param P_Post_Title_ID id
	 *	@return Post_Title
	 */
	public static P_Post_Title get (Properties ctx, int P_Post_Title_ID, String trxName)
	{
		Integer key = new Integer (P_Post_Title_ID);
		P_Post_Title Post_Title = (P_Post_Title)s_cache.get(key);
		if (Post_Title != null)
			return Post_Title;
		Post_Title = new P_Post_Title (ctx, P_Post_Title_ID, trxName);
		s_cache.put (key, Post_Title);
		return Post_Title;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Post_Title>	s_cache = new CCache<Integer,P_Post_Title>("P_Post_Title", 20);
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Post_Title_ID id
	 */
	public P_Post_Title (Properties ctx, int P_Post_Title_ID, String trxName)
	{
		super (ctx, P_Post_Title_ID, trxName);
		if (P_Post_Title_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Post_Title

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Post_Title (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Post_Title (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Post_Title_ID"), trxName);
	}	//	P_Post_Title


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Post_Title[ID=")
			.append(this.getP_Post_Title_ID())
//			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Post_Title
