package solstice.model;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

public class P_Statement_Earning_Notice extends X_P_Statement_Earning_Notice {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static P_Statement_Earning_Notice get (Properties ctx, int P_Statement_Earning_Notice_ID, String trxName)
	{
		Integer key = new Integer (P_Statement_Earning_Notice_ID);
		P_Statement_Earning_Notice Skill = (P_Statement_Earning_Notice)s_cache.get(key);
		if (Skill != null)
			return Skill;
		Skill = new P_Statement_Earning_Notice (ctx, P_Statement_Earning_Notice_ID, trxName);
		s_cache.put (key, Skill);
		return Skill;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Statement_Earning_Notice>	s_cache = new CCache<Integer,P_Statement_Earning_Notice>("P_Statement_Earning_Notice", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Statement_Earning_Notice_ID id
	 */
	public P_Statement_Earning_Notice (Properties ctx, int P_Statement_Earning_Notice_ID, String trxName)
	{
		super (ctx, P_Statement_Earning_Notice_ID, trxName);
		if (P_Statement_Earning_Notice_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Statement_Earning_Notice

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Statement_Earning_Notice (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Statement_Earning_Notice (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Statement_Earning_Notice_ID"), trxName);
	}	//	P_Statement_Earning_Notice


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Statement_Earning_Notice[ID=")
			.append(this.getP_Statement_Earning_Notice_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Statement_Earning_Notice
