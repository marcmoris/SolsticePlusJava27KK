package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Bank Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Bank extends X_P_Bank
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 777405923749825376L;


	/**
	 * 	Get Bank
	 *	@param ctx context
	 * 	@param P_Bank_ID id
	 *	@return Bank
	 */
	public static P_Bank get (Properties ctx, int P_Bank_ID, String trxName)
	{
		Integer key = new Integer (P_Bank_ID);
		P_Bank Bank = (P_Bank)s_cache.get(key);
		if (Bank != null)
			return Bank;
		Bank = new P_Bank (ctx, P_Bank_ID, trxName);
		s_cache.put (key, Bank);
		return Bank;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Bank>	s_cache = new CCache<Integer,P_Bank>("P_Bank", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Bank_ID id
	 */
	public P_Bank (Properties ctx, int P_Bank_ID, String trxName)
	{
		super (ctx, P_Bank_ID, trxName);
		if (P_Bank_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Bank

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Bank (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Bank (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Bank_ID"), trxName);
	}	//	P_Bank



}	//	P_Bank
