/*
 * Created on 2005-11-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.CLogger;

import solstice.model.P_T_AccidentReport;



/**
 * @author alenav01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutWorkAccidentsFollowUpReturn {

	private CLogger	log = CLogger.getCLogger (getClass());
	private String _ClassErrorMessage = "Error generating data for this report";
	/**
	 * 
	 */
	public P_InOutWorkAccidentsFollowUpReturn() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void StartCalculation(int iADPinstaceID)
	{
		int EmployeeID=0;
		int CountAccidents=0;
		int AccidentReportID=0;
		int Count=0;

		Timestamp AccidentDate = null;
		Timestamp NextAccidentDate = null;
		BigDecimal DailyHours=new BigDecimal(0);
		BigDecimal MissingHours=new BigDecimal(0);
		BigDecimal MissingDays=new BigDecimal(0);
		
		
		String SqlString = "SELECT P_EMPLOYEE_ID,COUNT(P_EMPLOYEE_ID) COUNTACCIDENTS FROM P_ACCIDENTREPORT GROUP BY P_EMPLOYEE_ID";
		
		DeleteOldRecords();
		
		try
		{
			PreparedStatement pstmp = DB.prepareStatement(SqlString, null);
            ResultSet rs = pstmp.executeQuery();
        	
            while ( rs.next() )
            {
            	
            	EmployeeID = rs.getInt("P_EMPLOYEE_ID");
            	CountAccidents = rs.getInt("COUNTACCIDENTS");

        		SqlString = "SELECT " +
				   			" AR.P_ACCIDENTREPORT_ID,AR.ACCIDENTDATE,ISNULL(RVA.DAY_HOURS,0) DAY_HOURS" +
							" FROM P_ACCIDENTREPORT AR " +
							" LEFT JOIN RV_P_ASSIGNMENT_PARAM_EFFECTIN RVA ON RVA.P_ASSIGNMENT_ID = AR.P_ASSIGNMENT_ID " +
							" WHERE " +
							" RVA.P_PERIOD_ID = (SELECT P_PERIOD_ID FROM P_PERIOD WHERE AR.ACCIDENTDATE BETWEEN STARTDATE AND ENDDATE)" +
							" AND P_EMPLOYEE_ID = " + EmployeeID;
        		       		
            	
        		pstmp = DB.prepareStatement(SqlString, null);
                ResultSet rs2 = pstmp.executeQuery();
                
                Count=0;
                
                while ( rs2.next() )
                {
                	if (CountAccidents==1)
                	{
                		AccidentReportID = rs2.getInt("P_ACCIDENTREPORT_ID");
                		AccidentDate = rs2.getTimestamp("ACCIDENTDATE");
                		DailyHours = rs2.getBigDecimal("DAY_HOURS");             	

                		MissingHours = CalculateMissingHours(EmployeeID,AccidentDate,null);
                		MissingDays =  MissingHours.divide(DailyHours,5,BigDecimal.ROUND_HALF_UP);
                		InsertIntoTempTable(iADPinstaceID,AccidentReportID,MissingHours,MissingDays);
                	}
                	else
                	{   
                		if (Count==0)
                		{
                			Count =+1;
    						AccidentReportID = rs2.getInt("P_ACCIDENTREPORT_ID");
                    		AccidentDate = rs2.getTimestamp("ACCIDENTDATE");
                    		DailyHours = rs2.getBigDecimal("DAY_HOURS");
                		}
                		else
                		{
                  			 NextAccidentDate = rs2.getTimestamp("ACCIDENTDATE");
                			
                    		MissingHours = CalculateMissingHours(EmployeeID,AccidentDate,NextAccidentDate);
                    		MissingDays =  MissingHours.divide(DailyHours,5,BigDecimal.ROUND_HALF_UP);
                    		InsertIntoTempTable(iADPinstaceID,AccidentReportID,MissingHours,MissingDays);
                    		
                    		AccidentDate = NextAccidentDate;
    						AccidentReportID = rs2.getInt("P_ACCIDENTREPORT_ID");
                    		DailyHours = rs2.getBigDecimal("DAY_HOURS");
                		}                			
                	}
                }
                
                if (CountAccidents!=1)
                {
               		MissingHours = CalculateMissingHours(EmployeeID,AccidentDate,null);
            		MissingDays =  MissingHours.divide(DailyHours,5,BigDecimal.ROUND_HALF_UP);
            		InsertIntoTempTable(iADPinstaceID,AccidentReportID,MissingHours,MissingDays);
                }
                
            }
            rs.close();
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, _ClassErrorMessage + " [StartCalculation] ", e);
		}
	}
		
	private void DeleteOldRecords()
	{       
        try
		{        
        	String StringSQL = "DELETE FROM P_T_ACCIDENTREPORT WHERE CREATED<=GETDATE()-1";
			PreparedStatement pstmp = null;
			
			pstmp = DB.prepareStatement(StringSQL, null);
			pstmp.execute();

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, _ClassErrorMessage + " [DeleteOldRecords] ", e);
		}
	}
	
	private BigDecimal CalculateMissingHours(int iEmployeeID,Timestamp AccidentDate,Timestamp NextAccidentDate)
	{
		BigDecimal MissingHours=new BigDecimal(0);
		
		String SqlString = "SELECT ISNULL(SUM(ISNULL(QUANTITYCALC,0)),0) QUANTITYCALC " +
						   " FROM P_PAYMENT_GAIN PG " +
						   " WHERE EXISTS (SELECT 1 " +
						   " FROM P_PAYMENT A, P_PERIOD B" +
						   " WHERE " +
						   " B.P_FREQUENCY_ID = A.P_FREQUENCY_ID " +
						   " AND B.P_PERIOD_ID = A.P_PERIOD_ID " +
						   " AND PG.P_EMPLOYEE_ID = " + iEmployeeID +
						   " AND A.P_PAYMENT_ID = PG.P_PAYMENT_ID " +
						   " AND B.ENDDATE >=" + DB.TO_DATE(AccidentDate);
						  
		if (NextAccidentDate!=null)
		{
			SqlString += " AND B.STARTDATE <= " + DB.TO_DATE(NextAccidentDate);
		}
		
		SqlString += ") AND EXISTS " +
						" (SELECT 1 " +
						" FROM P_GAIN_GAININFO C, " +
						" P_GAININFO D " +
						" WHERE PG.P_GAIN_ID = C.P_GAIN_ID " +
						" AND C.P_GAININFO_ID = D.P_GAININFO_ID " +
						" AND D.VALUE = 'NHAT'" +
						" AND C.TO_CONSIDER = 'Y')";
		
		try
		{
			PreparedStatement pstmp = DB.prepareStatement(SqlString, null);
            ResultSet rs = pstmp.executeQuery();
        	
            while ( rs.next() )
            {
            	MissingHours = rs.getBigDecimal("QUANTITYCALC");
            }
            rs.close();
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, _ClassErrorMessage + " [CalculateMissingHours] ", e);
		}
		return MissingHours;
		
	}
		
	private void InsertIntoTempTable(int iADPInstanceID,int iID,BigDecimal iAmount1,BigDecimal iAmount2)
	{
		P_T_AccidentReport AccidentReport=new P_T_AccidentReport(Env.getCtx(), -1, null);
		AccidentReport.setAD_PInstance_ID(iADPInstanceID);
		AccidentReport.setP_AccidentReport_ID(iID);
		AccidentReport.setAmount1(iAmount1);
		AccidentReport.setAmount2(iAmount2);
		AccidentReport.save();
	}
}
