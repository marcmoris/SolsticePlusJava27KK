package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  NonBusinessDay Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_NonBusinessDay extends X_P_NonBusinessDay
{
	/**
	 * 	Get NonBusinessDay
	 *	@param ctx context
	 * 	@param P_NonBusinessDay_ID id
	 *	@return NonBusinessDay
	 */
	public static P_NonBusinessDay get (Properties ctx, int P_NonBusinessDay_ID, String trxName)
	{
		Integer key = new Integer (P_NonBusinessDay_ID);
		P_NonBusinessDay NonBusinessDay = (P_NonBusinessDay)s_cache.get(key);
		if (NonBusinessDay != null)
			return NonBusinessDay;
		NonBusinessDay = new P_NonBusinessDay (ctx, P_NonBusinessDay_ID, trxName);
		s_cache.put (key, NonBusinessDay);
		return NonBusinessDay;
	}	//	get

	/**	Cache						*/
	
	private static CCache<Integer,P_NonBusinessDay>	s_cache = new CCache<Integer,P_NonBusinessDay>("P_NonBusinessDay", 20);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_NonBusinessDay_ID id
	 */
	public P_NonBusinessDay (Properties ctx, int P_NonBusinessDay_ID, String trxName)
	{
		super (ctx, P_NonBusinessDay_ID, trxName);
		if (P_NonBusinessDay_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_NonBusinessDay

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_NonBusinessDay (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_NonBusinessDay (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_NonBusinessDay_ID"), trxName);
	}	//	P_NonBusinessDay

	
	public static P_NonBusinessDay get( Properties ctx, int P_Holidays_Calendar_ID, Timestamp date, String trxName)
    {
		int P_NonBusinessDay_ID = 0;
		
    	String sql = " Select P_NonBusinessDay_ID From P_NonBusinessDay, P_Holidays_Year " 
    		       + " Where P_Holidays_Year.P_Holidays_Calendar_ID = " + P_Holidays_Calendar_ID
    		       + " And P_NonBusinessDay.P_Holidays_Year_ID =  P_Holidays_Year.P_Holidays_Year_ID "
    		       + " And P_NonBusinessDay.NonBusinessDate = " + DB.TO_DATE(date)
    		       ;
	    try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				P_NonBusinessDay_ID = rs.getInt("P_NonBusinessDay_ID");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read getNonBusinessDay sql " + sql + "Exception :" + e);
		}

		if (P_NonBusinessDay_ID == 0 )
			return null;
    	return P_NonBusinessDay.get( Env.getCtx(), P_NonBusinessDay_ID, trxName);
    }

	

}	//	P_NonBusinessDay
