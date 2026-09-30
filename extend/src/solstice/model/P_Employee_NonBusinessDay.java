package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Employee_NonBusinessDay Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_NonBusinessDay extends X_P_Employee_NonBusinessDay
{
	/**
	 * 	Get Employee_NonBusinessDay
	 *	@param ctx context
	 * 	@param P_Employee_NonBusinessDay_ID id
	 *	@return Employee_NonBusinessDay
	 */
	public static P_Employee_NonBusinessDay get (Properties ctx, int P_Employee_NonBusinessDay_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_NonBusinessDay_ID);
		P_Employee_NonBusinessDay Employee_NonBusinessDay = (P_Employee_NonBusinessDay)s_cache.get(key);
		if (Employee_NonBusinessDay != null)
			return Employee_NonBusinessDay;
		Employee_NonBusinessDay = new P_Employee_NonBusinessDay (ctx, P_Employee_NonBusinessDay_ID, trxName);
		s_cache.put (key, Employee_NonBusinessDay);
		return Employee_NonBusinessDay;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_NonBusinessDay>	s_cache = new CCache<Integer,P_Employee_NonBusinessDay>("P_Employee_NonBusinessDay", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_NonBusinessDay_ID id
	 */
	public P_Employee_NonBusinessDay (Properties ctx, int P_Employee_NonBusinessDay_ID, String trxName)
	{
		super (ctx, P_Employee_NonBusinessDay_ID, trxName);
		if (P_Employee_NonBusinessDay_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_NonBusinessDay

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_NonBusinessDay (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_NonBusinessDay (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_NonBusinessDay_ID"), trxName);
	}	//	P_Employee_NonBusinessDay



}	//	P_Employee_NonBusinessDay
