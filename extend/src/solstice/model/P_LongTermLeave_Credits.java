package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  LongTermLeave_Credits Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_LongTermLeave_Credits extends X_P_LongTermLeave_Credits
{
	/**
	 * 	Get LongTermLeave_Credits
	 *	@param ctx context
	 * 	@param P_LongTermLeave_Credits_ID id
	 *	@return LongTermLeave_Credits
	 */
	public static P_LongTermLeave_Credits get (Properties ctx, int P_LongTermLeave_Credits_ID, String trxName)
	{
		Integer key = new Integer (P_LongTermLeave_Credits_ID);
		P_LongTermLeave_Credits LongTermLeave_Credits = (P_LongTermLeave_Credits)s_cache.get(key);
		if (LongTermLeave_Credits != null)
			return LongTermLeave_Credits;
		LongTermLeave_Credits = new P_LongTermLeave_Credits (ctx, P_LongTermLeave_Credits_ID, trxName);
		s_cache.put (key, LongTermLeave_Credits);
		return LongTermLeave_Credits;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_LongTermLeave_Credits>	s_cache = new CCache<Integer,P_LongTermLeave_Credits>("P_LongTermLeave_Credits", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_LongTermLeave_Credits_ID id
	 */
	public P_LongTermLeave_Credits (Properties ctx, int P_LongTermLeave_Credits_ID, String trxName)
	{
		super (ctx, P_LongTermLeave_Credits_ID, trxName);
		if (P_LongTermLeave_Credits_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_LongTermLeave_Credits

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_LongTermLeave_Credits (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_LongTermLeave_Credits (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_LongTermLeave_Credits_ID"), trxName);
	}	//	P_LongTermLeave_Credits



}	//	P_LongTermLeave_Credits
