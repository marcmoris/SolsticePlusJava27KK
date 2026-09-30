package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 * @author vivdun01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Payment_Deduction_Insurance extends X_P_Payment_Deduction_Insurance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	/**
	 * 	Get Payment_Deduction_Insurance
	 *	@param ctx context
	 * 	@param P_Payment_Deduction_Insurance_ID id
	 *	@return Payment_Deduction_Insurance
	 */
	public static P_Payment_Deduction_Insurance get (Properties ctx, int P_Payment_Deduction_Insurance_ID, String trxName)
	{
		Integer key = new Integer (P_Payment_Deduction_Insurance_ID);
		P_Payment_Deduction_Insurance Payment_Deduction_Insurance = (P_Payment_Deduction_Insurance)s_cache.get(key);
		if (Payment_Deduction_Insurance != null)
			return Payment_Deduction_Insurance;
		Payment_Deduction_Insurance = new P_Payment_Deduction_Insurance (ctx, P_Payment_Deduction_Insurance_ID, trxName);
		s_cache.put (key, Payment_Deduction_Insurance);
		return Payment_Deduction_Insurance;
	}	//	get


	/**	Cache						*/
	
	private static CCache<Integer,P_Payment_Deduction_Insurance>	s_cache = new CCache<Integer,P_Payment_Deduction_Insurance>("P_Payment_Deduction_Insurance", 20);


	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Payment_Deduction_Insurance_ID id
	 */
	public P_Payment_Deduction_Insurance (Properties ctx, int P_Payment_Deduction_Insurance_ID, String trxName)
	{
		super (ctx, P_Payment_Deduction_Insurance_ID, trxName);
		if (P_Payment_Deduction_Insurance_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Payment_Deduction_Insurance

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Payment_Deduction_Insurance (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Payment_Deduction_Insurance (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Payment_Deduction_Insurance_ID"), trxName);
	}	//	P_Payment_Deduction_Insurance

}
