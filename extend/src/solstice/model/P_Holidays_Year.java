package solstice.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Holidays_Year Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Holidays_Year extends X_P_Holidays_Year
{
	/**
	 * 	Get Holidays_Year
	 *	@param ctx context
	 * 	@param P_Holidays_Year_ID id
	 *	@return Holidays_Year
	 */
	public static P_Holidays_Year get (Properties ctx, int P_Holidays_Year_ID, String trxName)
	{
		Integer key = new Integer (P_Holidays_Year_ID);
		P_Holidays_Year Holidays_Year = (P_Holidays_Year)s_cache.get(key);
		if (Holidays_Year != null)
			return Holidays_Year;
		Holidays_Year = new P_Holidays_Year (ctx, P_Holidays_Year_ID, trxName);
		s_cache.put (key, Holidays_Year);
		return Holidays_Year;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Holidays_Year>	s_cache = new CCache<Integer,P_Holidays_Year>("P_Holidays_Year", 2);
	

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Holidays_Year_ID id
	 */
	public P_Holidays_Year (Properties ctx, int P_Holidays_Year_ID, String trxName)
	{
		super (ctx, P_Holidays_Year_ID, trxName);
		if (P_Holidays_Year_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Holidays_Year

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Holidays_Year (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Holidays_Year (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Holidays_Year_ID"), trxName);
	}	//	P_Holidays_Year

	/**
	 * Procédure de copie. Lorsqu'on copie une année de calendrier,
	 * on doit copier les journées de congé de l'année d'origine
	 * en ajustant la date.
	 */
	public void afterCopy(int originalId)
	{
	    // On récupère d'abord les journée de congé de l'année d'origine
	    String sql
	    = "select IsActive, Name, NonBusinessDate, P_Gain_ID"
	        + " from P_NonBusinessDay"
	        + " where P_Holidays_Year_ID = " + originalId;
	    
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, this.get_TrxName());
	        ResultSet rs = stmt.executeQuery();
	        
	        while(rs.next())
	        {
	            // On met à jour l'année pour qu'elle corresponde à l'année courrante
	            Timestamp nonBusinessDate = ajustDate(rs.getTimestamp("NonBusinessDate"));
	            
	            // On crée les nouveaux enregistrement pour le calendrier courrant
	            P_NonBusinessDay nonBusinessDay = new P_NonBusinessDay(Env.getCtx(), -1, this.get_TrxName());
	            nonBusinessDay.setP_Holidays_Year_ID(this.getP_Holidays_Year_ID());
	            nonBusinessDay.setIsActive(rs.getString("IsActive").equals("Y"));
	            nonBusinessDay.setName(rs.getString("Name"));
	            nonBusinessDay.setNonBusinessDate(nonBusinessDate);
	            nonBusinessDay.setP_Gain_ID(rs.getInt("P_Gain_ID"));
	            nonBusinessDay.save();
	        }
	        
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        log.log(Level.SEVERE, "afterCopy", e);
	    }
	}
	
	/**
	 * Ajuste la date à l'année courrante
	 */
	private Timestamp ajustDate(Timestamp originalDate)
	{
	    Calendar calendar = GregorianCalendar.getInstance();
	    calendar.setTimeInMillis(originalDate.getTime());
	    calendar.set(Calendar.YEAR, Integer.parseInt(this.getYear()));
	    return new Timestamp(calendar.getTimeInMillis());
	}
	
}	//	P_Holidays_Year
