package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Payment_Entryline Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Payment_Entryline extends X_P_Payment_Entryline
{
	/**
	 * 	Get Payment_Entryline
	 *	@param ctx context
	 * 	@param P_Payment_Entryline_ID id
	 *	@return Payment_Entryline
	 */
	public static P_Payment_Entryline get (Properties ctx, int P_Payment_Entryline_ID, String trxName)
	{
		Integer key = new Integer (P_Payment_Entryline_ID);
		P_Payment_Entryline Payment_Entryline = (P_Payment_Entryline)s_cache.get(key);
		if (Payment_Entryline != null)
			return Payment_Entryline;
		Payment_Entryline = new P_Payment_Entryline (ctx, P_Payment_Entryline_ID, trxName);
		s_cache.put (key, Payment_Entryline);
		return Payment_Entryline;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Payment_Entryline>	s_cache = new CCache<Integer,P_Payment_Entryline>("P_Payment_Entryline", 20);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Payment_Entryline_ID id
	 */
	public P_Payment_Entryline (Properties ctx, int P_Payment_Entryline_ID, String trxName)
	{
		super (ctx, P_Payment_Entryline_ID, trxName);
		if (P_Payment_Entryline_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Payment_Entryline

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Payment_Entryline (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Payment_Entryline (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Payment_Entryline_ID"), trxName);
	}	//	P_Payment_Entryline



}	//	P_Payment_Entryline
