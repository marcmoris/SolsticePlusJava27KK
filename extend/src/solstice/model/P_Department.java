package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Department Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Department extends X_P_Department
{
	/**
	 * 	Get Department
	 *	@param ctx context
	 * 	@param P_Department_ID id
	 *	@return Department
	 */
	public static P_Department get (Properties ctx, int P_Department_ID, String trxName)
	{
		Integer key = new Integer (P_Department_ID);
		P_Department Department = (P_Department)s_cache.get(key);
		if (Department != null)
			return Department;
		Department = new P_Department (ctx, P_Department_ID, trxName);
		s_cache.put (key, Department);
		return Department;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Department>	s_cache = new CCache<Integer,P_Department>("P_Department", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Department_ID id
	 */
	public P_Department (Properties ctx, int P_Department_ID, String trxName)
	{
		super (ctx, P_Department_ID, trxName);
		if (P_Department_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Department

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Department (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Department (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Department_ID"), trxName);
	}	//	P_Department


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Department[ID=")
			.append(this.getP_Department_ID())
			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Department
