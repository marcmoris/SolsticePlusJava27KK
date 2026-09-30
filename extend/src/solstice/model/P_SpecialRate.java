/*
 * Created on 2005-08-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 * @author vivdun01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_SpecialRate extends X_P_SpecialRate
{
	/**
	 * 	Get SpecialRate
	 *	@param ctx context
	 * 	@param P_SpecialRate_ID id
	 *	@return SpecialRate
	 */
	public static P_SpecialRate get (Properties ctx, int P_SpecialRate_ID)
	{
		Integer key = new Integer (P_SpecialRate_ID);
		P_SpecialRate SpecialRate = (P_SpecialRate)s_cache.get(key);
		if (SpecialRate != null)
			return SpecialRate;
		SpecialRate = new P_SpecialRate (ctx, P_SpecialRate_ID, null);
		s_cache.put (key, SpecialRate);
		return SpecialRate;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_SpecialRate>	s_cache = new CCache<Integer,P_SpecialRate>("P_SpecialRate", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_SpecialRate_ID id
	 */
	public P_SpecialRate (Properties ctx, int P_SpecialRate_ID, String trxName)
	{
		super (ctx, P_SpecialRate_ID, trxName);
		if (P_SpecialRate_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_SpecialRate

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_SpecialRate (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_SpecialRate (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_SpecialRate_ID"), trxName);
	}	//	P_SpecialRate

}
