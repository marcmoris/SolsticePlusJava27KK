package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Bonus Model
 *
 *  @author Marc Morissette - SolsticePlus
 */
public class P_Employee_Expensereports extends X_P_Employee_Expensereports
{
	/**
	 * 	Get Bonus
	 *	@param ctx context
	 * 	@param P_Employee_Expensereports_ID id
	 *	@return Bonus
	 */
	public static P_Employee_Expensereports get (Properties ctx, int P_Employee_Expensereports_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Expensereports_ID);
		P_Employee_Expensereports Bonus = (P_Employee_Expensereports)s_cache.get(key);
		if (Bonus != null)
			return Bonus;
		Bonus = new P_Employee_Expensereports (ctx, P_Employee_Expensereports_ID, trxName);
		s_cache.put (key, Bonus);
		return Bonus;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Expensereports>	s_cache = new CCache<Integer,P_Employee_Expensereports>("P_Employee_Expensereports", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Expensereports_ID id
	 */
	public P_Employee_Expensereports (Properties ctx, int P_Employee_Expensereports_ID, String trxName)
	{
		super (ctx, P_Employee_Expensereports_ID, trxName);
		if (P_Employee_Expensereports_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Expensereports

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Expensereports (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Expensereports (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Expensereports_ID"), trxName);
	}	//	P_Employee_Expensereports


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Employee_Expensereports[ID=")
			.append(this.getP_Employee_Expensereports_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Employee_Expensereports
