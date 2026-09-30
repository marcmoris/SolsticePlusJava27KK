package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Scale_Advance Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Scale_Advance extends X_P_Scale_Advance
{
	/**
	 * 	Get Scale_Advance
	 *	@param ctx context
	 * 	@param P_Scale_Advance_ID id
	 *	@return Scale_Advance
	 */
	public static P_Scale_Advance get (Properties ctx, int P_Scale_Advance_ID, String trxName)
	{
		Integer key = new Integer (P_Scale_Advance_ID);
		P_Scale_Advance Scale_Advance = (P_Scale_Advance)s_cache.get(key);
		if (Scale_Advance != null)
			return Scale_Advance;
		Scale_Advance = new P_Scale_Advance (ctx, P_Scale_Advance_ID, trxName);
		s_cache.put (key, Scale_Advance);
		return Scale_Advance;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Scale_Advance>	s_cache = new CCache<Integer,P_Scale_Advance>("P_Scale_Advance", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Scale_Advance_ID id
	 */
	public P_Scale_Advance (Properties ctx, int P_Scale_Advance_ID, String trxName)
	{
		super (ctx, P_Scale_Advance_ID, trxName);
		if (P_Scale_Advance_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Scale_Advance

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Scale_Advance (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Scale_Advance (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Scale_Advance_ID"), trxName);
	}	//	P_Scale_Advance


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Scale_Advance[ID=")
			.append(getP_Scale_Advance_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Scale_Advance
