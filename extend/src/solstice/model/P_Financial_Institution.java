package solstice.model;

import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;
import java.sql.ResultSet;

/**
 *  Financial_Institution Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Financial_Institution extends X_P_Financial_Institution
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 8017495043249025176L;


	/**
	 * 	Get Financial_Institution
	 *	@param ctx context
	 * 	@param P_Financial_Institution_ID id
	 *	@return Financial_Institution
	 */
	public static P_Financial_Institution get (Properties ctx, int P_Financial_Institution_ID, String trxName)
	{
		Integer key = new Integer (P_Financial_Institution_ID);
		P_Financial_Institution Financial_Institution = (P_Financial_Institution)s_cache.get(key);
		if (Financial_Institution != null)
			return Financial_Institution;
		Financial_Institution = new P_Financial_Institution (ctx, P_Financial_Institution_ID, trxName);
		s_cache.put (key, Financial_Institution);
		return Financial_Institution;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Financial_Institution>	s_cache = new CCache<Integer,P_Financial_Institution>("P_Financial_Institution", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Financial_Institution_ID id
	 */
	public P_Financial_Institution (Properties ctx, int P_Financial_Institution_ID, String trxName)
	{
		super (ctx, P_Financial_Institution_ID, trxName);
		if (P_Financial_Institution_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Financial_Institution

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Financial_Institution (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Financial_Institution (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Financial_Institution_ID"), trxName);
	}	//	P_Financial_Institution



}	//	P_Financial_Institution
