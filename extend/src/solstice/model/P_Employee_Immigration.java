package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Employee_Immigration Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_Immigration extends X_P_Employee_Immigration
{
	/**
	 * 	Get Employee_Immigration
	 *	@param ctx context
	 * 	@param P_Employee_Immigration_ID id
	 *	@return Employee_Immigration
	 */
	public static P_Employee_Immigration get (Properties ctx, int P_Employee_Immigration_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Immigration_ID);
		P_Employee_Immigration Employee_Immigration = (P_Employee_Immigration)s_cache.get(key);
		if (Employee_Immigration != null)
			return Employee_Immigration;
		Employee_Immigration = new P_Employee_Immigration (ctx, P_Employee_Immigration_ID, trxName);
		s_cache.put (key, Employee_Immigration);
		return Employee_Immigration;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Immigration>	s_cache = new CCache<Integer,P_Employee_Immigration>("P_Employee_Immigration", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Immigration_ID id
	 */
	public P_Employee_Immigration (Properties ctx, int P_Employee_Immigration_ID, String trxName)
	{
		super (ctx, P_Employee_Immigration_ID, trxName);
		if (P_Employee_Immigration_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Immigration

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Immigration (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Immigration (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Immigration_ID"), trxName);
	}	//	P_Employee_Immigration

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
		StringBuffer sb = new StringBuffer ("P_Employee_Immigration[ID=")
			.append(this.getP_Employee_Immigration_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Employee_Immigration
