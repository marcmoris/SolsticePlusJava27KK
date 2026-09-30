package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  AccidentReport Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_AccidentReport extends X_P_AccidentReport
{
	/**
	 * 	Get AccidentReport
	 *	@param ctx context
	 * 	@param P_AccidentReport_ID id
	 *	@return AccidentReport
	 */
	public static P_AccidentReport get (Properties ctx, int P_AccidentReport_ID)
	{
		Integer key = new Integer (P_AccidentReport_ID);
		P_AccidentReport AccidentReport = (P_AccidentReport)s_cache.get(key);
		if (AccidentReport != null)
			return AccidentReport;
		AccidentReport = new P_AccidentReport (ctx, P_AccidentReport_ID, null);
		s_cache.put (key, AccidentReport);
		return AccidentReport;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_AccidentReport>	s_cache = new CCache<Integer,P_AccidentReport>("P_AccidentReport", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_AccidentReport_ID id
	 */
	public P_AccidentReport (Properties ctx, int P_AccidentReport_ID, String trxName)
	{
		super (ctx, P_AccidentReport_ID, trxName);
		if (P_AccidentReport_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_AccidentReport

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_AccidentReport (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_AccidentReport (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_AccidentReport_ID"), trxName);
	}	//	P_AccidentReport



}	//	P_AccidentReport
