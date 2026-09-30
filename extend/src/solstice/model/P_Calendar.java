package solstice.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Calendar Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Calendar extends X_P_Calendar
{
	/**
	 * 	Get Calendar
	 *	@param ctx context
	 * 	@param P_Calendar_ID id
	 *	@return Calendar
	 */
	public static P_Calendar get (Properties ctx, int P_Calendar_ID, String trxName)
	{
		Integer key = new Integer (P_Calendar_ID);
		P_Calendar Calendar = (P_Calendar)s_Calendar.get(key);
		if (Calendar != null)
			return Calendar;
		Calendar = new P_Calendar (ctx, P_Calendar_ID, trxName);
		s_Calendar.put (key, Calendar);
		return Calendar;
	}	//	get

	/**	Calendar Cache				*/
	private static CCache<Integer,P_Calendar> s_Calendar  = null;

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Calendar_ID id
	 */
	public P_Calendar (Properties ctx, int P_Calendar_ID, String trxName)
	{
		super (ctx, P_Calendar_ID, trxName);
		if (P_Calendar_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Calendar

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Calendar (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Calendar (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Calendar_ID"), trxName);
	}	//	P_Calendar


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("P_Calendar[ID=")
			.append(this.getP_Calendar_ID())
			.append(",Name=").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	P_Calendar
