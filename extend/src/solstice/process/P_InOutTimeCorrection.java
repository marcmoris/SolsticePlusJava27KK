package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_ExpenseCorrection;
import solstice.model.P_Period;
import solstice.model.P_TimeCorrection;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Time_Sheet_Detail;

public class P_InOutTimeCorrection extends SvrProcess 
{

	int P_TimeCorrection_ID;
	
	// On récupère le ID de la session
	protected void prepare() 
	{
		P_TimeCorrection_ID = getRecord_ID();
	}

	protected String doIt() throws Exception 
	{
        String trxName = null; //Trx.createTrxName();

        P_TimeCorrection TimeCorrection = new P_TimeCorrection( getCtx(), P_TimeCorrection_ID, null); 

        if ( TimeCorrection.isProcessing() )
        	return "*Erreur* La correction a déjà été traiter";
        
        undo( P_TimeCorrection_ID );
        
        String sql = "SELECT P_Time_Sheet_Detail.*, P_Time_Sheet.P_Employee_ID From P_Time_Sheet INNER JOIN P_Time_Sheet_Detail ON P_Time_Sheet_Detail.P_Time_Sheet_ID = P_Time_Sheet.P_Time_Sheet_ID WHERE 1 = 1 ";
        
        if ( TimeCorrection.getP_Employee_ID() != 0)
        	sql = sql + " AND P_Employee_ID = " + TimeCorrection.getP_Employee_ID();
        
        if ( TimeCorrection.getP_Period_Start_ID() != 0)
        	sql = sql + " AND P_Time_Sheet_Detail.P_Period_ID >= " + TimeCorrection.getP_Period_Start_ID();

        if ( TimeCorrection.getP_Period_End_ID() != 0)
        	sql = sql + " AND P_Time_Sheet_Detail.P_Period_ID <= " + TimeCorrection.getP_Period_End_ID();

        if ( TimeCorrection.getStartDate() != null)
        	sql = sql + " AND Day >= " + TimeCorrection.getStartDate();

        if ( TimeCorrection.getEndDate() != null)
        	sql = sql + " AND Day <= " + TimeCorrection.getEndDate();

        if ( TimeCorrection.getP_Assignment_Src_ID() != 0)
        	sql = sql + " AND P_Assignment_ID <= " + TimeCorrection.getP_Assignment_Src_ID();

        if ( TimeCorrection.getP_Gain_Src_ID() != 0)
        	sql = sql + " AND P_Gain_ID <= " + TimeCorrection.getP_Gain_Src_ID();

        sql = sql + " ORDER BY P_Time_Sheet.P_Employee_ID";
        
	    try
	    {
		    PreparedStatement s = DB.prepareStatement(sql, null);
		    ResultSet rs = s.executeQuery();
		    while(rs.next())
		    {
		    	P_Period Period = P_Period.getOpenPeriod(Env.getCtx(), trxName);
		    	int P_Period_ID   = Period.getP_Period_ID();
		    	int P_Employee_ID = rs.getInt( "P_Employee_ID");
		    	P_Time_Sheet Sheet = P_Time_Sheet.get(Env.getCtx(), P_Period_ID, P_Employee_ID, P_Time_Sheet.SHEETTYPE_Regular , trxName);
		    	
		    	if ( Sheet.getTimeSheetStatus().equals( P_Time_Sheet.TIMESHEETSTATUS_Initial))
		    	{
		    		P_Time_Sheet_Detail detail = new P_Time_Sheet_Detail( Env.getCtx(), -1, null);	
		    		detail.setP_Time_Sheet_ID(Sheet.getP_Time_Sheet_ID());
		    		detail.setP_Assignment_ID( rs.getInt("P_Assignment_ID"));
//	                timeSheetDetail.setWeekIndex(weekNumber);
		    		detail.setHourly_Rate( rs.getBigDecimal("Hourly_Rate"));
	                detail.setP_Gain_ID( rs.getInt( "P_Gain_ID"));

	                detail.setDay( rs.getTimestamp("Day"));
	                detail.setIsActive(true);
	                if ( TimeCorrection.getDayQty() != null && TimeCorrection.getDayQty().compareTo(Env.ZERO) != 0)
	                	detail.setDayQty( TimeCorrection.getDayQty().multiply( new BigDecimal(-1)) );
	                else
	                	detail.setDayQty( rs.getBigDecimal("DayQty").multiply( new BigDecimal(-1)) );
	                detail.setOriginTime( rs.getString("OriginTime"));
	                detail.setP_Period_ID( rs.getInt( "P_Period_ID") );
	                detail.setP_Schedule_ID( rs.getInt( "P_Schedule_ID") );
	                detail.setStartDate(rs.getTimestamp("StartDate"));
	                detail.setEndDate( rs.getTimestamp("EndDate"));

	                detail.save();

		    		P_Time_Sheet_Detail detail2 = new P_Time_Sheet_Detail( Env.getCtx(), -1, null);	
		    		detail2.setP_Time_Sheet_ID(Sheet.getP_Time_Sheet_ID());
		    		if ( TimeCorrection.getP_Assignment_Dst_ID() != 0)
		    			detail2.setP_Assignment_ID( TimeCorrection.getP_Assignment_Dst_ID() );
		    		else
		    			detail2.setP_Assignment_ID( rs.getInt("P_Assignment_ID"));
//	                timeSheetDetail.setWeekIndex(weekNumber);
		    		detail2.setHourly_Rate( rs.getBigDecimal("Hourly_Rate"));

	                detail2.setDay( rs.getTimestamp("Day"));
	                detail2.setIsActive(true);
	                
	                if ( TimeCorrection.getDayQty() != null && TimeCorrection.getDayQty().compareTo(Env.ZERO) != 0)
	                	detail2.setDayQty( TimeCorrection.getDayQty() );
	                else
	                	detail2.setDayQty( rs.getBigDecimal("DayQty") );

	                detail2.setOriginTime( rs.getString("OriginTime"));
	                if ( TimeCorrection.getP_Gain_Dst_ID() != 0)
	                	detail2.setP_Gain_ID(  TimeCorrection.getP_Gain_Dst_ID());
	                else
	                	detail2.setP_Gain_ID( rs.getInt( "P_Gain_ID"));
	                detail2.setP_Period_ID( rs.getInt( "P_Period_ID") );
	                detail2.setP_Schedule_ID( rs.getInt( "P_Schedule_ID") );
	                detail2.setStartDate(rs.getTimestamp("StartDate"));
	                detail2.setEndDate( rs.getTimestamp("EndDate"));

	                detail2.save();

		    	}
		    	
		    	
		    	
		    }
	    }
	    catch (Exception e)
	    {
	        e.getMessage();
	    }

	    return "";
	}
	
	public void undo( int P_ExpenseCorrection_ID )
	{
;		String SqlDel = "Delete from P_Payment_Gain_Distribution where P_ExpenseCorrection_ID = " + P_ExpenseCorrection_ID ;;
;		DB.executeUpdate(SqlDel, null);

;		SqlDel = "Delete from P_Payment_EntryLine where P_ExpenseCorrection_ID = " + P_ExpenseCorrection_ID ;
;		DB.executeUpdate(SqlDel, null);
	}


		
	
}
