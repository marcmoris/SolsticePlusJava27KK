package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Schedule Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Schedule extends X_P_Schedule
{
	/**
	 * 	Get Schedule
	 *	@param ctx context
	 * 	@param P_Schedule_ID id
	 *	@return Schedule
	 */
	public static P_Schedule get (Properties ctx, int P_Schedule_ID, String trxName)
	{
		Integer key = new Integer (P_Schedule_ID);
		P_Schedule Schedule = (P_Schedule)s_cache.get(key);
		if (Schedule != null)
			return Schedule;
		Schedule = new P_Schedule (ctx, P_Schedule_ID, trxName);
		s_cache.put (key, Schedule);
		return Schedule;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Schedule>	s_cache = new CCache<Integer,P_Schedule>("P_Schedule", 5);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Schedule_ID id
	 */
	public P_Schedule (Properties ctx, int P_Schedule_ID, String trxName)
	{
		super (ctx, P_Schedule_ID, trxName);
		if (P_Schedule_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Schedule

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Schedule (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Schedule (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Schedule_ID"), trxName);
	}	//	P_Schedule


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Schedule[ID=")
			.append(getP_Schedule_ID())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Schedule
