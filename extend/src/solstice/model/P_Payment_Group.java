package solstice.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Payment_Group Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Payment_Group extends X_P_Payment_Group
{
	/**
	 * 	Get Payment_Group
	 *	@param ctx context
	 * 	@param P_Payment_Group_ID id
	 *	@return Payment_Group
	 */
	public static P_Payment_Group get (Properties ctx, int P_Payment_Group_ID, String trxName)
	{
		Integer key = new Integer (P_Payment_Group_ID);
		P_Payment_Group Payment_Group = (P_Payment_Group)s_cache.get(key);
		if (Payment_Group != null)
			return Payment_Group;
		Payment_Group = new P_Payment_Group (ctx, P_Payment_Group_ID, trxName);
		s_cache.put (key, Payment_Group);
		return Payment_Group;
	}	//	get

	/**	Cache						*/
	
	private static CCache<Integer,P_Payment_Group>	s_cache = new CCache<Integer,P_Payment_Group>("P_Payment_Group", 2);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Payment_Group_ID id
	 */
	public P_Payment_Group (Properties ctx, int P_Payment_Group_ID, String trxName)
	{
		super (ctx, P_Payment_Group_ID, trxName);
		if (P_Payment_Group_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Payment_Group

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Payment_Group (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Payment_Group (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Payment_Group_ID"), trxName);
	}	//	P_Payment_Group


	public static BigDecimal getAnnual_Increase( Properties ctx, int P_Employee_ID, String trxName)
	{
		P_Employee Employee = P_Employee.get(ctx, P_Employee_ID, trxName);
		P_Payment_Group Payment_Group = P_Payment_Group.get( ctx, Employee.getP_Payment_Group_ID(), trxName);
		return Payment_Group.getAnnual_Increase();
		
	}

}	//	P_Payment_Group
