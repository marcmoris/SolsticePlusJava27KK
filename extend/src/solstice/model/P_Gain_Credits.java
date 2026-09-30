package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Gain_Credits Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Gain_Credits extends X_P_Gain_Credits
{
	/**
	 * 	Get Gain_Credits
	 *	@param ctx context
	 * 	@param P_Gain_Credits_ID id
	 *	@return Gain_Credits
	 */
	public static P_Gain_Credits get (Properties ctx, int P_Gain_Credits_ID, String trxName)
	{
		Integer key = new Integer (P_Gain_Credits_ID);
		P_Gain_Credits Gain_Credits = (P_Gain_Credits)s_cache.get(key);
		if (Gain_Credits != null)
			return Gain_Credits;
		Gain_Credits = new P_Gain_Credits (ctx, P_Gain_Credits_ID, trxName);
		s_cache.put (key, Gain_Credits);
		return Gain_Credits;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Gain_Credits>	s_cache = new CCache<Integer,P_Gain_Credits>("P_Gain_Credits", 20);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Gain_Credits_ID id
	 */
	public P_Gain_Credits (Properties ctx, int P_Gain_Credits_ID, String trxName)
	{
		super (ctx, P_Gain_Credits_ID, trxName);
		if (P_Gain_Credits_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Gain_Credits

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Gain_Credits (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	
	
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Gain_Credits (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Gain_Credits_ID"), trxName);
	}	//	P_Gain_Credits



}	//	P_Gain_Credits
