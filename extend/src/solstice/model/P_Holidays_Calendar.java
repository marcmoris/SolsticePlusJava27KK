package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import solstice.model.P_Holidays_Year;

/**
 *  Holidays_Calendar Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Holidays_Calendar extends X_P_Holidays_Calendar
{
	/**
	 * 	Get Holidays_Calendar
	 *	@param ctx context
	 * 	@param P_Holidays_Calendar_ID id
	 *	@return Holidays_Calendar
	 */
	public static P_Holidays_Calendar get (Properties ctx, int P_Holidays_Calendar_ID, String trxName)
	{
		Integer key = new Integer (P_Holidays_Calendar_ID);
		P_Holidays_Calendar Holidays_Calendar = (P_Holidays_Calendar)s_cache.get(key);
		if (Holidays_Calendar != null)
			return Holidays_Calendar;
		Holidays_Calendar = new P_Holidays_Calendar (ctx, P_Holidays_Calendar_ID, trxName);
		s_cache.put (key, Holidays_Calendar);
		return Holidays_Calendar;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Holidays_Calendar>	s_cache = new CCache<Integer,P_Holidays_Calendar>("P_Holidays_Calendar", 2);

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Holidays_Calendar_ID id
	 */
	public P_Holidays_Calendar (Properties ctx, int P_Holidays_Calendar_ID, String trxName)
	{
		super (ctx, P_Holidays_Calendar_ID, trxName);
		if (P_Holidays_Calendar_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Holidays_Calendar

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Holidays_Calendar (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Holidays_Calendar (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Holidays_Calendar_ID"), trxName);
	}	//	P_Holidays_Calendar

	/**
	 * Procédure de copie. Lorsqu'on copie un calendrier,
	 */
	public void afterCopy(int originalId)
	{
	    // On récupère d'abord les journée de congé de l'année d'origine
	    String sql
	    = "select P_Holidays_Year_ID, Year"
	        + " from P_Holidays_Year"
	        + " where P_Holidays_Calendar_ID = " + originalId;
	    
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, this.get_TrxName());
	        ResultSet rs = stmt.executeQuery();
	        
	        while(rs.next())
	        {
	        	P_Holidays_Year Holidays_Year = new P_Holidays_Year( Env.getCtx(), -1, this.get_TrxName());
	        	Holidays_Year.setYear(rs.getString("Year"));
	        	Holidays_Year.setP_Holidays_Calendar_ID(this.getP_Holidays_Calendar_ID());
	        	Holidays_Year.save();
	        	
	        	Holidays_Year.afterCopy(rs.getInt("P_Holidays_Year_ID"));
	        	
	        }
	        
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        log.log(Level.SEVERE, "afterCopy", e);
	    }
	}


}	//	P_Holidays_Calendar
