package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;
  
/**
 *  ExpenseCorrection Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_ExpenseCorrection extends X_P_ExpenseCorrection
{
	/**
	 * 	Get ExpenseCorrection
	 *	@param ctx context
	 * 	@param P_ExpenseCorrection_ID id
	 *	@return ExpenseCorrection
	 */
	public static P_ExpenseCorrection get (Properties ctx, int P_ExpenseCorrection_ID, String trxName)
	{
		Integer key = new Integer (P_ExpenseCorrection_ID);
		P_ExpenseCorrection ExpenseCorrection = (P_ExpenseCorrection)s_cache.get(key);
		if (ExpenseCorrection != null)
			return ExpenseCorrection;
		ExpenseCorrection = new P_ExpenseCorrection (ctx, P_ExpenseCorrection_ID, trxName);
		s_cache.put (key, ExpenseCorrection);
		return ExpenseCorrection;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_ExpenseCorrection>	s_cache = new CCache<Integer,P_ExpenseCorrection>("P_ExpenseCorrection", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_ExpenseCorrection_ID id
	 */
	public P_ExpenseCorrection (Properties ctx, int P_ExpenseCorrection_ID, String trxName)
	{
		super (ctx, P_ExpenseCorrection_ID, trxName);
		if (P_ExpenseCorrection_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_ExpenseCorrection

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_ExpenseCorrection (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_ExpenseCorrection (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_ExpenseCorrection_ID"), trxName);
	}	//	P_ExpenseCorrection


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_ExpenseCorrection[ID=")
			.append(this.getP_ExpenseCorrection_ID())
			.append(",Value=").append(getValue())
			.append(",Description=").append(getDescription())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_ExpenseCorrection
