package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Employee_Advance_Detail Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_Advance_Detail extends X_P_Employee_Advance_Detail
{
	/**
	 * 	Get Employee_Advance_Detail
	 *	@param ctx context
	 * 	@param P_Employee_Advance_Detail_ID id
	 *	@return Employee_Advance_Detail
	 */
	public static P_Employee_Advance_Detail get (Properties ctx, int P_Employee_Advance_Detail_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Advance_Detail_ID);
		P_Employee_Advance_Detail Employee_Advance_Detail = (P_Employee_Advance_Detail)s_cache.get(key);
		if (Employee_Advance_Detail != null)
			return Employee_Advance_Detail;
		Employee_Advance_Detail = new P_Employee_Advance_Detail (ctx, P_Employee_Advance_Detail_ID, trxName);
		s_cache.put (key, Employee_Advance_Detail);
		return Employee_Advance_Detail;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Advance_Detail>	s_cache = new CCache<Integer,P_Employee_Advance_Detail>("P_Employee_Advance_Detail", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Advance_Detail_ID id
	 */
	public P_Employee_Advance_Detail (Properties ctx, int P_Employee_Advance_Detail_ID, String trxName)
	{
		super (ctx, P_Employee_Advance_Detail_ID, trxName);
		if (P_Employee_Advance_Detail_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Advance_Detail

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Advance_Detail (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Advance_Detail (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Advance_Detail_ID"), trxName);
	}	//	P_Employee_Advance_Detail



}	//	P_Employee_Advance_Detail
