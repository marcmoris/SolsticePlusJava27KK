package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Bonus Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_WorkRegion extends X_P_WorkRegion
{
	/**
	 * 	Get WorkRegion
	 *	@param ctx context
	 * 	@param P_WorkRegion_ID id
	 *	@return WorkRegion
	 */
	public static P_WorkRegion get (Properties ctx, int P_WorkRegion_ID, String trxName)
	{
		Integer key = new Integer (P_WorkRegion_ID);
		P_WorkRegion WorkRegion = (P_WorkRegion)s_cache.get(key);
		if (WorkRegion != null)
			return WorkRegion;
		WorkRegion = new P_WorkRegion (ctx, P_WorkRegion_ID, trxName);
		s_cache.put (key, WorkRegion);
		return WorkRegion;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_WorkRegion>	s_cache = new CCache<Integer,P_WorkRegion>("P_WorkRegion", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_WorkRegion_ID id
	 */
	public P_WorkRegion (Properties ctx, int P_WorkRegion_ID, String trxName)
	{
		super (ctx, P_WorkRegion_ID, trxName);
		if (P_WorkRegion_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_WorkRegion

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_WorkRegion (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_WorkRegion (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_WorkRegion_ID"), trxName);
	}	//	P_WorkRegion


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_WorkRegion[ID=")
			.append(this.getP_WorkRegion_ID())
//			.append(",Value=").append(getValue())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_WorkRegion
