/*
 * Created on 2005-09-05
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
public class P_Method_Gain extends X_P_Method_Gain
{
	/**
	 * 	Get Method_Gain
	 *	@param ctx context
	 * 	@param P_Method_Gain_ID id
	 *	@return Method_Gain
	 */
	public static P_Method_Gain get (Properties ctx, int P_Method_Gain_ID, String trxName)
	{
		Integer key = new Integer (P_Method_Gain_ID);
		P_Method_Gain Method_Gain = (P_Method_Gain)s_cache.get(key);
		if (Method_Gain != null)
			return Method_Gain;
		Method_Gain = new P_Method_Gain (ctx, P_Method_Gain_ID, trxName);
		s_cache.put (key, Method_Gain);
		return Method_Gain;
	}	//	get


	/**	Cache						*/
	
	private static CCache<Integer,P_Method_Gain>	s_cache = new CCache<Integer,P_Method_Gain>("P_Method_Gain", 20);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Method_Gain_ID id
	 */
	public P_Method_Gain (Properties ctx, int P_Method_Gain_ID, String trxName)
	{
		super (ctx, P_Method_Gain_ID, trxName);
		if (P_Method_Gain_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Method_Gain

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Method_Gain (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Method_Gain (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Method_Gain_ID"), trxName);
	}	//	P_Method_Gain

}
