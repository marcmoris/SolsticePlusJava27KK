package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Tools_Credits Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Tools_Credits extends X_P_Tools_Credits
{
	/**
	 * 	Get Tools_Credits
	 *	@param ctx context
	 * 	@param P_Tools_Credits_ID id
	 *	@return Tools_Credits
	 */
	public static P_Tools_Credits get (Properties ctx, int P_Tools_Credits_ID, String trxName)
	{
		Integer key = new Integer (P_Tools_Credits_ID);
		P_Tools_Credits Tools_Credits = (P_Tools_Credits)s_cache.get(key);
		if (Tools_Credits != null)
			return Tools_Credits;
		Tools_Credits = new P_Tools_Credits (ctx, P_Tools_Credits_ID, trxName);
		s_cache.put (key, Tools_Credits);
		return Tools_Credits;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Tools_Credits>	s_cache = new CCache<Integer,P_Tools_Credits>("P_Tools_Credits", 50);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Tools_Credits_ID id
	 */
	public P_Tools_Credits (Properties ctx, int P_Tools_Credits_ID, String trxName)
	{
		super (ctx, P_Tools_Credits_ID, trxName);
		if (P_Tools_Credits_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Tools_Credits

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Tools_Credits (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Tools_Credits (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Tools_Credits_ID"), trxName);
	}	//	P_Tools_Credits


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Tools_Credits[ID=")
			.append(getP_Tools_Credits_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Tools_Credits
