/*
 * Created on 2005-06-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.TimeUtil;
import solstice.model.P_Period;


/**
 * @author vivdun01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutYearPeriods extends SvrProcess
{
	int FrequencyID;
	Timestamp StartDate;
	Timestamp PayDate;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("P_Frequency_ID"))
				// Patch de cast car on ne sait pas pourquoi il retourne un bigdecimal
				FrequencyID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("StartDate"))
				StartDate = (Timestamp) para[i].getParameter();
			else if (name.equals("PayDate"))
				PayDate = (Timestamp) para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		// prepare
	}
	
	protected String doIt() throws Exception
	{
		int NbPeriod = -1;
		int PeriodNo = 1;
		int NbDay = -1;
		Timestamp EndDate = null;
		Calendar Cal = new GregorianCalendar();
				
		String trxName = null;
		String sql = null;
		ResultSet rs = null;
		
		// *** Find number of periods
		sql = "SELECT NumberOfPeriod " + 
	      				" FROM P_FREQUENCY " + 
						" WHERE P_Frequency_Id = " + FrequencyID +
						" AND  IsActive = 'Y'";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next())
				NbPeriod = rs.getInt("NumberOfPeriod");
			else
			{
				rs.close();
				pstmt.close();
				return "NumberOfPeriodNotFound";
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "P_InOutYearPeriods - doIt - " + sql, e);
		}
				
		// *** Find number of days
		NbDay = TimeUtil.getDaysBetween(StartDate, PayDate);
				
		// *** Find end date
		EndDate = getEndDate(StartDate, NbPeriod);
				
		// Loop to add missing periods
		while (PeriodNo <= NbPeriod)
		{
			P_Period Period = new P_Period(getCtx(), trxName);
			if (Period.createPeriod(getCtx(), PeriodNo, this.getRecord_ID(), FrequencyID,
						StartDate, EndDate, PayDate, trxName) == false)
				return "Periods Not Created";
			
			// Set next start date
			Cal.setTime(EndDate);
			Cal.add(Calendar.DAY_OF_YEAR, 1);
			StartDate = new Timestamp(Cal.getTime().getTime());
			
			EndDate = getEndDate(StartDate, NbPeriod);
			PayDate = getNextPayDate(PayDate, EndDate, NbPeriod, NbDay); 
			
			PeriodNo++;
		}
					
		return "processed successfully";
	}
	
	private Timestamp getEndDate(Timestamp StartingDate, int NbPeriod)
	{
		Calendar Cal = new GregorianCalendar();
		Cal.setTime(StartingDate);
				
		if (NbPeriod == 52 || NbPeriod == 53)
        {
			Cal.add(Calendar.DAY_OF_YEAR, 6);
			return new Timestamp(Cal.getTime().getTime());
        }
                
        if (NbPeriod == 26 || NbPeriod == 27)
        {
        	Cal.add(Calendar.DAY_OF_YEAR, 13);
        	return new Timestamp(Cal.getTime().getTime());
        }
        
        if (NbPeriod == 24)
        {
           	Timestamp day = StartingDate;
        	if ( TimeUtil.getDay_Of_Month(day) == 1)
        	{
        		day = TimeUtil.addDays( day, 14);
        		return day;
        	}
            else
            {
            	// Last day of the StartDate month
            	day = TimeUtil.getMonthLastDay(day);
            	return day;
            }
        }
        
        if (NbPeriod == 12)
        {
        	Cal.add(Calendar.MONTH, 1);
        	Cal.add(Calendar.DAY_OF_YEAR, -1);
        	return new Timestamp(Cal.getTime().getTime());
        } 
        
        return null;
    }
	
	private Timestamp getNextPayDate(Timestamp PaymentDate, Timestamp EndDate, int NbPeriod, int NbDay)
	{
		Calendar Cal = new GregorianCalendar();
		Cal.setTime(PaymentDate);
				
		if (NbPeriod == 52 || NbPeriod == 53)
		{
			Cal.add(Calendar.DAY_OF_YEAR, 7);
			return new Timestamp(Cal.getTime().getTime());
		}  

        if (NbPeriod == 26 || NbPeriod == 27)
        {
        	Cal.add(Calendar.DAY_OF_YEAR, 14);
        	return new Timestamp(Cal.getTime().getTime());
        }  

        if (NbPeriod == 24)
        {
        	return EndDate;
        }

        if (NbPeriod == 12 ) 
        {
        	Cal.add(Calendar.MONTH, 1);
        	return new Timestamp(Cal.getTime().getTime());
        }   
		        
		return null;
	}
	
}
