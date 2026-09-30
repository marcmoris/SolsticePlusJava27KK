package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Employee_Gestionnaire Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Employee_Gestionnaire extends X_P_Employee_Gestionnaire
{
	/**
	 * 	Get Employee_Gestionnaire
	 *	@param ctx context
	 * 	@param P_Employee_Gestionnaire_ID id
	 *	@return Employee_Gestionnaire
	 */
	public static P_Employee_Gestionnaire get (Properties ctx, int P_Employee_Gestionnaire_ID, String trxName)
	{
		Integer key = new Integer (P_Employee_Gestionnaire_ID);
		P_Employee_Gestionnaire Employee_Gestionnaire = (P_Employee_Gestionnaire)s_cache.get(key);
		if (Employee_Gestionnaire != null)
			return Employee_Gestionnaire;
		Employee_Gestionnaire = new P_Employee_Gestionnaire (ctx, P_Employee_Gestionnaire_ID, trxName);
		s_cache.put (key, Employee_Gestionnaire);
		return Employee_Gestionnaire;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Gestionnaire>	s_cache = new CCache<Integer,P_Employee_Gestionnaire>("P_Employee_Gestionnaire", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Gestionnaire_ID id
	 */
	public P_Employee_Gestionnaire (Properties ctx, int P_Employee_Gestionnaire_ID, String trxName)
	{
		super (ctx, P_Employee_Gestionnaire_ID, trxName);
		if (P_Employee_Gestionnaire_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Employee_Gestionnaire

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Gestionnaire (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Gestionnaire (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Gestionnaire_ID"), trxName);
	}	//	P_Employee_Gestionnaire


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Employee_Gestionnaire[ID=")
			.append(this.getP_Employee_Gestionnaire_ID())
			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Employee_Gestionnaire
