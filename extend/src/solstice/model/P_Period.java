package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.compiere.model.MRole;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Period Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Period extends X_P_Period
{
	/**
	 * 	Get Period
	 *	@param ctx context
	 * 	@param P_Period_ID id
	 *	@return Period
	 */
	public static P_Period get (Properties ctx, int P_Period_ID, String trxName)
	{
		Integer key = new Integer (P_Period_ID);
		P_Period Period = (P_Period)s_cache.get(key);
		if (Period != null)
			return Period;
		Period = new P_Period (ctx, P_Period_ID, trxName);
		s_cache.put (key, Period);
		return Period;
	}	//	get

	/**	Cache					*/
	static private CCache<Integer,P_Period> s_cache = new CCache<Integer,P_Period>("P_Period", 30, 60);

	/*
	 *  Return the first Open Period ! 
	 *  TODO add a Calendar Parameter
	 */
	public static P_Period getOpenPeriod ( Properties ctx, String trxName )
	{
		String sql = "Select top 1 P_Period_ID FROM P_Period WHERE PeriodStatus =  '" + P_Period.PERIODSTATUS_Open + "'";
		
//		sql = MRole.getDefault().addAccessSQL (sql, "P_Period", true, false);	// fully qualidfied - RO 

        if ( Env.getContextAsInt(Env.getCtx(), "#P_Payment_Group_ID") > 0 )
	    {
    		P_Payment_Group PaymentGroup = P_Payment_Group.get(Env.getCtx(), Env.getContextAsInt(Env.getCtx(), "#P_Payment_Group_ID"), trxName);
    		sql += " AND P_Frequency_ID = "  + PaymentGroup.getP_Frequency_ID();
    		Env.setContext(Env.getCtx(), "P_Frequency_ID", PaymentGroup.getP_Frequency_ID());
	    }
		
		PreparedStatement pstmt = null;

		int Period_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				Period_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Period - getOpenPeriod - " + e);
		}
			
		return P_Period.get( ctx, Period_ID , trxName);
		
	}

	/*
	 *  Return the first Open Period ! 
	 *  TODO add a Calendar Parameter
	 */
	public static P_Period getOpenPeriod ( Properties ctx, int P_Payment_Group_ID, String trxName )
	{
		String sql = "Select top 1 P_Period_ID From P_Period Where PeriodStatus =  '" + P_Period.PERIODSTATUS_Open + "'";

        if ( P_Payment_Group_ID > 0 )
	    {
    		P_Payment_Group PaymentGroup = P_Payment_Group.get(Env.getCtx(), P_Payment_Group_ID, trxName);
    		sql += " AND P_Frequency_ID = "  + PaymentGroup.getP_Frequency_ID();
    		Env.setContext(Env.getCtx(), "P_Frequency_ID", PaymentGroup.getP_Frequency_ID());
	    }
		
		PreparedStatement pstmt = null;

		int Period_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				Period_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Period - getOpenPeriod - " + e);
		}
			
		return P_Period.get( ctx, Period_ID , trxName);
		
	}

	
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Period.class);

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Period (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Period_ID id
	 */
	public P_Period (Properties ctx, int P_Period_ID, String trxName)
	{
		super (ctx, P_Period_ID, trxName);
		if (P_Period_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Period

	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Period (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Period_ID"), trxName);
	}	//	P_Period

	public boolean createPeriod( Properties ctx, int PeriodNo, int YearID, int FrequencyID, Timestamp StartDate, Timestamp EndDate, Timestamp PayDate, String trxName)  
	{	
		int Year = -1;
		boolean exist = false;
		int CPeriodID = -1;
		P_Period Period;
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
						
		// *** Find Year
		sql = "SELECT Year FROM P_YEAR WHERE P_YEAR_ID = " + YearID;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			if (rs.next())
				Year = rs.getInt("Year");
			else
			{
				rs.close();
				pstmt.close();
				return false;
			}
			
			rs.close();
			pstmt.close();
						
			// *** Check if exist
			sql = "SELECT * FROM P_Period " + 
							" WHERE P_Year_ID = " + YearID +  
							" AND PeriodNo = " + PeriodNo + 
							" AND P_Frequency_ID = " + FrequencyID;
			
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
	
			exist = rs.next();
		  	
			if ( exist )
			{
					
				// Update
				Period = get(ctx, rs.getInt("P_Period_ID"), trxName);
				Period.setName(Year + "-" + (PeriodNo < 10 ? "0" + PeriodNo : ""+PeriodNo));
				Period.setStartDate(StartDate);
				Period.setEndDate(EndDate);
				Period.setPayDate(PayDate);
				
				Period.save();	

			}	
			else
			{
				// Insert
				Period = new P_Period( ctx, -1, trxName );
				Period.setP_Year_ID(YearID);
				Period.setPeriodNo(PeriodNo);
				Period.setP_Frequency_ID(FrequencyID);
				Period.setPeriodStatus("N");
				Period.setName(Year + "-" + (PeriodNo < 10 ? "0" + PeriodNo : ""+PeriodNo));
				Period.setStartDate(StartDate);
				Period.setEndDate(EndDate);
				Period.setPayDate(PayDate);
				Period.save();	
			}

			rs.close();
			pstmt.close();
		  	pstmt = null;
		
			sql = "Update P_Period set p_period.c_period_id1 = C_Period.c_period_id "
			    + " FROM C_Period "
			    + " WHERE Paydate BETWEEN C_Period.Startdate AND C_Period.Enddate " 
			    + " AND P_Period.P_Period_ID = " + Period.getP_Period_ID()
			    + " AND C_Period.AD_Org_ID in ( P_Period.AD_Org_ID, 0 ) "
			    ;
			
			DB.executeUpdate(sql, null);
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE,"P_Period - createPeriod - " + sql, e);
		}
	
		return true;
	}



	//
	// Return le nombre de jours ouvrable dans la période.
	//
	public BigDecimal getNbrWorkDay(  )
	{
		BigDecimal nbrWorkDay = Env.ZERO;

		int nday = TimeUtil.getDaysBetween( this.getStartDate(), this.getEndDate() ) + 1;

		Timestamp date = this.getStartDate();
		GregorianCalendar cal = new GregorianCalendar();
		for (int i = 0; i < nday ; i++)  // nbrDays
		{
			date = TimeUtil.addDays( this.getStartDate(), i );
			// 	on exclue les samedi dimande
			cal.setTimeInMillis( date.getTime());
			if ( cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY )
			{
				nbrWorkDay = nbrWorkDay.add ( Env.ONE );
			}
		}
		return nbrWorkDay;
	}

	//
	// Return le nombre de jours ouvrable entre 2 date.
	//
	public BigDecimal getNbrWorkDay( Timestamp StartDate, Timestamp EndDate )
	{
		BigDecimal nbrWorkDay = Env.ZERO;

		int nday = TimeUtil.getDaysBetween( StartDate, EndDate ) + 1;

		Timestamp date = StartDate;
		GregorianCalendar cal = new GregorianCalendar();
		for (int i = 0; i < nday ; i++)  // nbrDays
		{
			date = TimeUtil.addDays( StartDate, i );
			// 	on exclue les samedi dimande
			cal.setTimeInMillis( date.getTime());
			if ( cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY )
			{
				nbrWorkDay = nbrWorkDay.add ( Env.ONE );
			}
		}
		return nbrWorkDay;
	}

	public static P_Period get ( Properties ctx, Timestamp PeriodDate, String trxName )
	{
		String sql = "Select top 1 P_Period_ID From P_Period Where " + DB.TO_DATE(PeriodDate) + " Between StartDate and EndDate and IsAdjustmentPeriod = 'N' ";

		PreparedStatement pstmt = null;

		int Period_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				Period_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Period - get - " + e);
		}
			
		return P_Period.get( ctx, Period_ID , trxName);
		
	}


	/*
	 *  Return the first Open Period ! 
	 *  TODO add a Calendar Parameter
	 */
	public static P_Period getOpenPeriodWithPaymentGroup ( Properties ctx, int P_Payment_Group_ID, String trxName )
	{
		P_Payment_Group PaymentGroup = P_Payment_Group.get(Env.getCtx(), P_Payment_Group_ID, trxName);
		String sql = "Select top 1 P_Period.P_Period_ID From P_Period, P_Year "
					+ " Where P_Year.P_Year_ID = P_Period.P_Year_ID" 
            		+ "	AND P_Period.PeriodStatus =  '" + P_Period.PERIODSTATUS_Open + "'"
//					+ " AND P_Year.P_Calendar_ID = "  + PaymentGroup.getP_Calendar_ID()
					;
		
		PreparedStatement pstmt = null;

		int Period_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				Period_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Period - getOpenPeriod - " + e);
		}
			
		return P_Period.get( ctx, Period_ID , trxName);
		
	}

	/*
	 *  Return the first Open Period ! 
	 *  TODO add a Calendar Parameter
	 */
	public static P_Period getOpenPeriodWithCalendar ( Properties ctx, int P_Calendar_ID, String trxName )
	{
		String sql = "Select top 1 P_Period_ID From P_Period, P_Year "
			+ " Where P_Year.P_Year_ID = P_Period.P_Year_ID" 
    		+ "	AND P_Period.PeriodStatus =  '" + P_Period.PERIODSTATUS_Open + "'"
			+ " AND P_Year.P_Calendar_ID = "  + P_Calendar_ID;
		
		PreparedStatement pstmt = null;

		int Period_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				Period_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Period - getOpenPeriod - " + e);
		}
			
		return P_Period.get( ctx, Period_ID , trxName);
		
	}

	public static P_Period get ( Properties ctx, int P_Calendar_ID, Timestamp PeriodDate, String trxName )
	{
		String sql = "Select top 1 P_Period_ID From P_Period Where P_Calendar_ID = " + P_Calendar_ID + " and " + DB.TO_DATE(PeriodDate) + " Between StartDate and EndDate and IsAdjustmentPeriod = 'N'"
		;

		PreparedStatement pstmt = null;

		int Period_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				Period_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Period - get - " + e);
		}
			
		return P_Period.get( ctx, Period_ID , trxName);
		
	}

	
	public static P_Period getFirstPeriodYear( Properties ctx, int P_Year_ID,  String trxName )
	{
		String sql = "Select top 1 P_Period_ID From P_Period Where P_Year_ID = " + P_Year_ID + " and PeriodNo = 1 ";
		;

		PreparedStatement pstmt = null;

		int Period_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				Period_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("P_Period - get - " + e);
		}
			
		return P_Period.get( ctx, Period_ID , trxName);
	}


	public String toString()
	{
	StringBuffer sb = new StringBuffer ("P_Period[").append( this.getName() ).append("]");
	return sb.toString();
	}

}	//	P_Period

