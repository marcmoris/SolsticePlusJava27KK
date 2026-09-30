package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Bonus Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_Expensereports_Detail extends X_P_Employee_Expensereports_Detail
{
	/**
	 * 	Get Bonus
	 *	@param ctx context
	 * 	@param P_Employee_Expensereports_Detail_ID id
	 *	@return Bonus
	 */
	public static P_Employee_Expensereports_Detail get (Properties ctx, int P_Employee_Expensereports_Detail_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Expensereports_Detail_ID);
		P_Employee_Expensereports_Detail Bonus = (P_Employee_Expensereports_Detail)s_cache.get(key);
		if (Bonus != null)
			return Bonus;
		Bonus = new P_Employee_Expensereports_Detail (ctx, P_Employee_Expensereports_Detail_ID, trxName);
		s_cache.put (key, Bonus);
		return Bonus;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Expensereports_Detail>	s_cache = new CCache<Integer,P_Employee_Expensereports_Detail>("P_Employee_Expensereports_Detail", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Expensereports_Detail_ID id
	 */
	public P_Employee_Expensereports_Detail (Properties ctx, int P_Employee_Expensereports_Detail_ID, String trxName)
	{
		super (ctx, P_Employee_Expensereports_Detail_ID, trxName);
		if (P_Employee_Expensereports_Detail_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Expensereports_Detail

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Expensereports_Detail (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Expensereports_Detail (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Expensereports_Detail_ID"), trxName);
	}	//	P_Employee_Expensereports_Detail


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Employee_Expensereports_Detail[ID=")
			.append(this.getP_Employee_Expensereports_Detail_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Employee_Expensereports_Detail
