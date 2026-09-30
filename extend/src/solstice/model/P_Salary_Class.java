package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

/**
 *  Salary_Class Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Salary_Class extends X_P_Salary_Class
{
	/**
	 * 	Get Salary_Class
	 *	@param ctx context
	 * 	@param P_Salary_Class_ID id
	 *	@return Salary_Class
	 */
	public static P_Salary_Class get (Properties ctx, int P_Salary_Class_ID, String trxName)
	{
		Integer key = new Integer (P_Salary_Class_ID);
		P_Salary_Class Salary_Class = (P_Salary_Class)s_cache.get(key);
		if (Salary_Class != null)
			return Salary_Class;
		Salary_Class = new P_Salary_Class (ctx, P_Salary_Class_ID, trxName);
		s_cache.put (key, Salary_Class);
		return Salary_Class;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Salary_Class>	s_cache = new CCache<Integer,P_Salary_Class>("P_Salary_Class", 20);
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Salary_Class.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Salary_Class_ID id
	 */
	public P_Salary_Class (Properties ctx, int P_Salary_Class_ID, String trxName)
	{
		super (ctx, P_Salary_Class_ID, trxName);
		if (P_Salary_Class_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Salary_Class

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Salary_Class (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Salary_Class (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Salary_Class_ID"), trxName);
	}	//	P_Salary_Class

 
}	//	P_Salary_Class
