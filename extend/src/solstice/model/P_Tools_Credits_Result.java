package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Tools_Credits_Result Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Tools_Credits_Result extends X_P_Tools_Credits_Result
{
	/**
	 * 	Get Tools_Credits_Result
	 *	@param ctx context
	 * 	@param P_Tools_Credits_Result_ID id
	 *	@return Tools_Credits_Result
	 */
	public static P_Tools_Credits_Result get (Properties ctx, int P_Tools_Credits_Result_ID, String trxName)
	{
		Integer key = new Integer (P_Tools_Credits_Result_ID);
		P_Tools_Credits_Result Tools_Credits_Result = (P_Tools_Credits_Result)s_cache.get(key);
		if (Tools_Credits_Result != null)
			return Tools_Credits_Result;
		Tools_Credits_Result = new P_Tools_Credits_Result (ctx, P_Tools_Credits_Result_ID, trxName);
		s_cache.put (key, Tools_Credits_Result);
		return Tools_Credits_Result;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Tools_Credits_Result>	s_cache = new CCache<Integer,P_Tools_Credits_Result>("P_Tools_Credits_Result", 50);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Tools_Credits_Result_ID id
	 */
	public P_Tools_Credits_Result (Properties ctx, int P_Tools_Credits_Result_ID, String trxName)
	{
		super (ctx, P_Tools_Credits_Result_ID, trxName);
		if (P_Tools_Credits_Result_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Tools_Credits_Result

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Tools_Credits_Result (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Tools_Credits_Result (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Tools_Credits_Result_ID"), trxName);
	}	//	P_Tools_Credits_Result


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Tools_Credits_Result[ID=")
			.append(getP_Tools_Credits_Result_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Tools_Credits_Result
