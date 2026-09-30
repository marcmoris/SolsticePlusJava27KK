/*
 * Created on 2005-08-25
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
public class P_Method_Credits extends X_P_Method_Credits 
{
	/**
	 * 	Get Method_Credits
	 *	@param ctx context
	 * 	@param P_Method_Credits_ID id
	 *	@return Method_Credits
	 */
	public static P_Method_Credits get (Properties ctx, int P_Method_Credits_ID, String trxName)
	{
		Integer key = new Integer (P_Method_Credits_ID);
		P_Method_Credits Method_Credits = (P_Method_Credits)s_cache.get(key);
		if (Method_Credits != null)
			return Method_Credits;
		Method_Credits = new P_Method_Credits (ctx, P_Method_Credits_ID, trxName);
		s_cache.put (key, Method_Credits);
		return Method_Credits;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Method_Credits>	s_cache = new CCache<Integer,P_Method_Credits>("P_Method_Credits", 20);
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Method_Credits_ID id
	 */
	public P_Method_Credits (Properties ctx, int P_Method_Credits_ID, String trxName)
	{
		super (ctx, P_Method_Credits_ID, trxName);
		if (P_Method_Credits_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employer

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Method_Credits (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Method_Credits (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Method_Credits_ID"), trxName);
	}	//	P_Method_Credits
}
