package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Employee_License Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_License extends X_P_Employee_License
{
	/**
	 * 	Get Employee_License
	 *	@param ctx context
	 * 	@param P_Employee_License_ID id
	 *	@return Employee_License
	 */
	public static P_Employee_License get (Properties ctx, int P_Employee_License_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_License_ID);
		P_Employee_License Employee_License = (P_Employee_License)s_cache.get(key);
		if (Employee_License != null)
			return Employee_License;
		Employee_License = new P_Employee_License (ctx, P_Employee_License_ID, trxName);
		s_cache.put (key, Employee_License);
		return Employee_License;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_License>	s_cache = new CCache<Integer,P_Employee_License>("P_Employee_License", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_License_ID id
	 */
	public P_Employee_License (Properties ctx, int P_Employee_License_ID, String trxName)
	{
		super (ctx, P_Employee_License_ID, trxName);
		if (P_Employee_License_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_License

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_License (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_License (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_License_ID"), trxName);
	}	//	P_Employee_License

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true or false
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		//+ 2011.07.05 + sécurité par compagnie.
 	    P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
		this.setAD_Org_ID( Employee.getAD_Org_ID());
		//- 2011.07.05 + sécurité par compagnie.

		return true;
	}	//	beforeSave

	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Employee_License[ID=")
			.append(this.getP_Employee_License_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Employee_License
