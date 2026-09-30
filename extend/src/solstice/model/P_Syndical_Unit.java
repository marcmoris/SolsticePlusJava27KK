package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Syndical_Unit Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Syndical_Unit extends X_P_Syndical_Unit
{
	/**
	 * 	Get Syndical_Unit
	 *	@param ctx context
	 * 	@param P_Syndical_Unit_ID id
	 *	@return Syndical_Unit
	 */
	public static P_Syndical_Unit get (Properties ctx, int P_Syndical_Unit_ID, String trxName)
	{
		Integer key = new Integer (P_Syndical_Unit_ID);
		P_Syndical_Unit Syndical_Unit = (P_Syndical_Unit)s_cache.get(key);
		if (Syndical_Unit != null)
			return Syndical_Unit;
		Syndical_Unit = new P_Syndical_Unit (ctx, P_Syndical_Unit_ID, trxName);
		s_cache.put (key, Syndical_Unit);
		return Syndical_Unit;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Syndical_Unit>	s_cache = new CCache<Integer,P_Syndical_Unit>("P_Syndical_Unit", 2);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Syndical_Unit_ID id
	 */
	public P_Syndical_Unit (Properties ctx, int P_Syndical_Unit_ID, String trxName)
	{
		super (ctx, P_Syndical_Unit_ID, trxName);
		if (P_Syndical_Unit_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Syndical_Unit

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Syndical_Unit (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Syndical_Unit (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Syndical_Unit_ID"), trxName);
	}	//	P_Syndical_Unit



}	//	P_Syndical_Unit
