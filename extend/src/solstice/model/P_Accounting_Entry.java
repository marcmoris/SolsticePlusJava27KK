package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Collective_Labour_Agr Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Accounting_Entry extends X_P_Accounting_Entry
{
	/**
	 * 	Get Collective_Labour_Agr
	 *	@param ctx context
	 * 	@param P_Accounting_Entry_ID id
	 *	@return Collective_Labour_Agr
	 */
	public static P_Accounting_Entry get (Properties ctx, int P_Accounting_Entry_ID, String trxName)
	{
		Integer key = new Integer (P_Accounting_Entry_ID);
		P_Accounting_Entry Accounting_Entry = (P_Accounting_Entry)s_cache.get(key);
		if (Accounting_Entry != null)
			return Accounting_Entry;
		Accounting_Entry = new P_Accounting_Entry (ctx, P_Accounting_Entry_ID, trxName);
		s_cache.put (key, Accounting_Entry);
		return Accounting_Entry;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Accounting_Entry>	s_cache = new CCache<Integer,P_Accounting_Entry>("P_Accounting_Entry", 20);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Accounting_Entry_ID id
	 */
	public P_Accounting_Entry (Properties ctx, int P_Accounting_Entry_ID, String trxName)
	{
		super (ctx, P_Accounting_Entry_ID, trxName);
		if (P_Accounting_Entry_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Accounting_Entry

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Accounting_Entry (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Accounting_Entry (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Accounting_Entry_ID"), trxName);
	}	//	P_Accounting_Entry



}	//	P_Accounting_Entry
