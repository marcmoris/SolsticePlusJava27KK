package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Carra_Sabbatical Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Carra_Sabbatical extends X_P_Carra_Sabbatical
{
	/**
	 * 	Get Carra_Sabbatical
	 *	@param ctx context
	 * 	@param P_Carra_Sabbatical_ID id
	 *	@return Carra_Sabbatical
	 */
	public static P_Carra_Sabbatical get (Properties ctx, int P_Carra_Sabbatical_ID, String trxName)
	{
		Integer key = new Integer (P_Carra_Sabbatical_ID);
		P_Carra_Sabbatical Carra_Sabbatical = (P_Carra_Sabbatical)s_cache.get(key);
		if (Carra_Sabbatical != null)
			return Carra_Sabbatical;
		Carra_Sabbatical = new P_Carra_Sabbatical (ctx, P_Carra_Sabbatical_ID, trxName);
		s_cache.put (key, Carra_Sabbatical);
		return Carra_Sabbatical;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Carra_Sabbatical>	s_cache = new CCache<Integer,P_Carra_Sabbatical>("P_Carra_Sabbatical", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Carra_Sabbatical_ID id
	 */
	public P_Carra_Sabbatical (Properties ctx, int P_Carra_Sabbatical_ID, String trxName)
	{
		super (ctx, P_Carra_Sabbatical_ID, trxName);
		if (P_Carra_Sabbatical_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Carra_Sabbatical

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Carra_Sabbatical (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Carra_Sabbatical (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Carra_Sabbatical_ID"), trxName);
	}	//	P_Carra_Sabbatical


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Carra_Sabbatical[ID=")
			.append(this.getP_Carra_Sabbatical_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Carra_Sabbatical
