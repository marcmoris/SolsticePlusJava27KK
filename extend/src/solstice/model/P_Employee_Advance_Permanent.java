package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Employee_Advance_Permanent Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_Advance_Permanent extends X_P_Employee_Advance_Permanent
{
	/**
	 * 	Get Employee_Advance_Permanent
	 *	@param ctx context
	 * 	@param P_Employee_Advance_Permanent_ID id
	 *	@return Employee_Advance_Permanent
	 */
	public static P_Employee_Advance_Permanent get (Properties ctx, int P_Employee_Advance_Permanent_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Advance_Permanent_ID);
		P_Employee_Advance_Permanent Employee_Advance_Permanent = (P_Employee_Advance_Permanent)s_cache.get(key);
		if (Employee_Advance_Permanent != null)
			return Employee_Advance_Permanent;
		Employee_Advance_Permanent = new P_Employee_Advance_Permanent (ctx, P_Employee_Advance_Permanent_ID, trxName);
		s_cache.put (key, Employee_Advance_Permanent);
		return Employee_Advance_Permanent;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Advance_Permanent>	s_cache = new CCache<Integer,P_Employee_Advance_Permanent>("P_Employee_Advance_Permanent", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Advance_Permanent_ID id
	 */
	public P_Employee_Advance_Permanent (Properties ctx, int P_Employee_Advance_Permanent_ID, String trxName)
	{
		super (ctx, P_Employee_Advance_Permanent_ID, trxName);
		if (P_Employee_Advance_Permanent_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Advance_Permanent

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Advance_Permanent (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Advance_Permanent (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Advance_Permanent_ID"), trxName);
	}	//	P_Employee_Advance_Permanent



}	//	P_Employee_Advance_Permanent
