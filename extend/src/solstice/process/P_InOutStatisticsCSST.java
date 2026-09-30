/*
 * Created on 2005-11-08
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_T_AccidentReport;

/**
 * @author alenav01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutStatisticsCSST {

	 private String _ClassErrorMessage ="Error Generating Statistics";
	 private int _CountAccidents=0;
	 
	/**
	 * 
	 */
	
	public P_InOutStatisticsCSST() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void LaucnhRapport(int iADInstanceID,int iPeriodStartID,int iPeriodEndID,String iSort)
	{
		if (iSort.equals("1")) 
			LaunchSortByJobTitle(iADInstanceID,iPeriodStartID,iPeriodEndID);
		else if (iSort.equals("2"))
			LaunchSortByActivity(iADInstanceID,iPeriodStartID,iPeriodEndID);
		else if	(iSort.equals("3"))
			LaunchSortByWorkPlace(iADInstanceID,iPeriodStartID,iPeriodEndID);		
	}
	
	/*LaunchSortByJobTitle*/
	
	private void LaunchSortByJobTitle(int iADInstanceID,int iPeriodStartID,int iPeriodEndID)
	{
		_CountAccidents=CountAccidents(iPeriodStartID,iPeriodEndID);
		ResultSet rsAccidents=GetAccidentsByJobTitle(iPeriodStartID,iPeriodEndID);
		BigDecimal NHTQuantity=new BigDecimal(0);
		BigDecimal NHATQuantity=new BigDecimal(0);
		BigDecimal NHPQuantity=new BigDecimal(0);
		BigDecimal NHTGLBQuantity=new BigDecimal(0);
		
		try
		{
			if (rsAccidents.isBeforeFirst())
			{
				while (rsAccidents.next())
				{
					ResultSet rsCumulHours=GetCumulHoursByJobTitle(iPeriodStartID,iPeriodEndID,rsAccidents.getInt("P_JOB_TITLE_ID"));
					
					if (rsCumulHours.isBeforeFirst())
					{
						while (rsCumulHours.next())
						try
						{
							if (rsCumulHours.getString("VALUE").toUpperCase().compareTo("NHT")==0)
								NHTQuantity=rsCumulHours.getBigDecimal("SUMQUANTITY");
							else if (rsCumulHours.getString("VALUE").toUpperCase().compareTo("NHAT")==0)
								NHATQuantity=rsCumulHours.getBigDecimal("SUMQUANTITY");
							else if (rsCumulHours.getString("VALUE").toUpperCase().compareTo("NHP")==0)
								NHPQuantity=rsCumulHours.getBigDecimal("SUMQUANTITY");
						}
						catch (Exception e)
						{
							System.err.println(_ClassErrorMessage + " [LaunchSortByJobTitle] " + e); 
						}			        
					}
					NHTGLBQuantity = GetNHTGLB(iPeriodStartID,iPeriodEndID);
					
					InsertIntoTempTable(iADInstanceID,rsAccidents.getInt("P_JOB_TITLE_ID"),
										rsAccidents.getString("VALUE"),
										rsAccidents.getString("NAME"),
										FrequenceRelative(NHTQuantity),
										IndiceGravite(NHATQuantity,NHPQuantity),
										TauxGravite(NHTQuantity,NHATQuantity,NHPQuantity),
										TauxGraviteGlobal(NHATQuantity,NHPQuantity,NHTGLBQuantity));
				}
			}	
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [LaunchSortByJobTitle] " + e); 
		}
		
	}
	
	private ResultSet GetAccidentsByJobTitle(int iPeriodStartID,int iPeriodEndID)
	{
		String StringSQL="SELECT " + 
							"J.P_JOB_TITLE_ID," +
							"J.VALUE," +
							"J.NAME" +
	                     " FROM P_ACCIDENTREPORT A " +
							"INNER JOIN P_JOB_TITLE J ON A.P_JOB_TITLE_ID = J.P_JOB_TITLE_ID " +
	                    " WHERE " +
							"ACCIDENTDATE>=(SELECT STARTDATE FROM P_PERIOD WHERE P_PERIOD_ID=" + iPeriodStartID + ") " +
							"AND ACCIDENTDATE<=(SELECT ENDDATE FROM P_PERIOD WHERE P_PERIOD_ID=" + iPeriodEndID + ") " +
 	                    " GROUP BY " +
							"J.P_JOB_TITLE_ID," +
							"J.VALUE," +
							"J.NAME";
		
		ResultSet rsAccidentReport=null;
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt= DB.prepareStatement(StringSQL, null);
			rsAccidentReport= pstmt.executeQuery();		
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [GetAccidentsByJobTitle] " + e); 
		}
		
		return rsAccidentReport;
	}
	
	private ResultSet GetCumulHoursByJobTitle(int iPeriodStartID,int iPeriodEndID,int iJobTitleID)
	{
		String StringSQL="SELECT " +
							"GI.VALUE, " +
							"SUM(PG.QUANTITY) SUMQUANTITY" + 
	                     " FROM " + 
						 	"P_PAYMENT_GAIN PG " +
								"INNER JOIN P_GAIN_GAININFO GGI ON GGI.P_GAIN_ID = PG.P_GAIN_ID " +
								"INNER JOIN P_GAININFO GI ON GI.P_GAININFO_ID = GGI.P_GAININFO_ID " +
	                    " WHERE " +
						    " P_JOB_TITLE_ID= " + iJobTitleID +
							" AND GI.VALUE IN ('NHT','NHAT','NHP')"+
							" AND GGI.TO_CONSIDER='Y' " +
							" AND DAY>=(SELECT STARTDATE FROM P_PERIOD WHERE P_PERIOD_ID=" + iPeriodStartID + ")"+
							" AND DAY<=(SELECT ENDDATE FROM P_PERIOD WHERE P_PERIOD_ID="+ iPeriodEndID + ")" +
						" GROUP BY " +
							"GI.VALUE";
		
		ResultSet rsCumul=null;
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt= DB.prepareStatement(StringSQL, null);
			rsCumul= pstmt.executeQuery();		
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [GetCumulHoursByJobTitle] " + e); 
		}
		
		return rsCumul;
	}
	
	/*LaunchSortByWorkPlace*/
	
	private void LaunchSortByWorkPlace(int iADInstanceID,int iPeriodStartID,int iPeriodEndID)
	{
		_CountAccidents=CountAccidents(iPeriodStartID,iPeriodEndID);
		ResultSet rsAccidents=GetAccidentsByWorkPlace(iPeriodStartID,iPeriodEndID);
		BigDecimal NHTQuantity=new BigDecimal(0);
		BigDecimal NHATQuantity=new BigDecimal(0);
		BigDecimal NHPQuantity=new BigDecimal(0);
		BigDecimal NHTGLBQuantity=new BigDecimal(0);
		
		try
		{
			if (rsAccidents.isBeforeFirst())
			{
				while (rsAccidents.next())
				{
					ResultSet rsCumulHours=GetCumulHoursByWorkPlace(iPeriodStartID,iPeriodEndID,rsAccidents.getInt("P_WORKPLACE_ID"));
					
					if (rsCumulHours.isBeforeFirst())
					{
						while (rsCumulHours.next())
						try
						{
							if (rsCumulHours.getString("VALUE").toUpperCase().compareTo("NHT")==0)
								NHTQuantity=rsCumulHours.getBigDecimal("SUMQUANTITY");
							else if (rsCumulHours.getString("VALUE").toUpperCase().compareTo("NHAT")==0)
								NHATQuantity=rsCumulHours.getBigDecimal("SUMQUANTITY");
							else if (rsCumulHours.getString("VALUE").toUpperCase().compareTo("NHP")==0)
								NHPQuantity=rsCumulHours.getBigDecimal("SUMQUANTITY");
						}
						catch (Exception e)
						{
							System.err.println(_ClassErrorMessage + " [LaunchSortByWorkPlace] " + e); 
						}			        
					}
					NHTGLBQuantity = GetNHTGLB(iPeriodStartID,iPeriodEndID);
					
					InsertIntoTempTable(iADInstanceID,rsAccidents.getInt("P_WORKPLACE_ID"),
							rsAccidents.getString("VALUE"),
							rsAccidents.getString("NAME"),
										FrequenceRelative(NHTQuantity),
										IndiceGravite(NHATQuantity,NHPQuantity),
										TauxGravite(NHTQuantity,NHATQuantity,NHPQuantity),
										TauxGraviteGlobal(NHATQuantity,NHPQuantity,NHTGLBQuantity));
				}
			}	
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [LaunchSortByWorkPlace] " + e); 
		}
	}
	
	private ResultSet GetAccidentsByWorkPlace(int iPeriodStartId,int iPeriodEndID)
	{
		String StringSQL="SELECT " +
							"W.P_WORKPLACE_ID," + 
							"W.VALUE," +
							"W.NAME" +
	                    " FROM P_ACCIDENTREPORT A " +
							"INNER JOIN P_POST P ON A.P_POST_ID = P.P_POST_ID " +
							"INNER JOIN P_WORKPLACE W ON P.P_WORKPLACE_ID = W.P_WORKPLACE_ID " +
						" WHERE " +
							"ACCIDENTDATE>=(SELECT STARTDATE FROM P_PERIOD WHERE P_PERIOD_ID=" + iPeriodStartId + ") " +
							"AND ACCIDENTDATE<=(SELECT ENDDATE FROM P_PERIOD WHERE P_PERIOD_ID=" + iPeriodEndID + ") " +
 	                    " GROUP BY " +
							"W.P_WORKPLACE_ID," +
							"W.VALUE," +
							"W.NAME";
		
		ResultSet rsAccidentReport=null;
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt= DB.prepareStatement(StringSQL, null);
			rsAccidentReport= pstmt.executeQuery();		
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [GetAccidentsByWorkPlace] " + e); 
		}
		
		return rsAccidentReport;
	}
	
	private ResultSet GetCumulHoursByWorkPlace(int iPeriodStartId,int iPeriodEndID,int iWorkPlaceID)
	{
		String StringSQL="SELECT " +
							"GI.VALUE, " +
							"SUM(PG.QUANTITY) SUMQUANTITY" + 
	                     " FROM " + 
						 	"P_PAYMENT_GAIN PG " +
								"INNER JOIN P_GAIN_GAININFO GGI ON GGI.P_GAIN_ID = PG.P_GAIN_ID " +
								"INNER JOIN P_GAININFO GI ON GI.P_GAININFO_ID = GGI.P_GAININFO_ID " +
								"INNER JOIN P_POST P ON PG.P_POST_ID = P.P_POST_ID " +
	                    " WHERE " +
						    " P.P_WORKPLACE_ID= " + iWorkPlaceID +
							" AND GI.VALUE IN ('NHT','NHAT','NHP')" +
							" AND GGI.TO_CONSIDER='Y' " +
							" AND DAY>=(SELECT STARTDATE FROM P_PERIOD WHERE P_PERIOD_ID=" + iPeriodStartId + ")"+
							" AND DAY<=(SELECT ENDDATE FROM P_PERIOD WHERE P_PERIOD_ID="+ iPeriodEndID + ")" +
						" GROUP BY " +
							"GI.VALUE";
			
		ResultSet rsCumul=null;
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt= DB.prepareStatement(StringSQL, null);
			rsCumul= pstmt.executeQuery();		
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [GetCumulHoursByWorkPlace] " + e); 
		}
		
		return rsCumul;
	}

	/*LaunchSortByActivity*/
	
	private void LaunchSortByActivity(int iADInstanceID,int iPeriodStartID,int iPeriodEndID)
	{
		_CountAccidents=CountAccidents(iPeriodStartID,iPeriodEndID);
		ResultSet rsAccidents=GetAccidentsByActivity(iPeriodStartID,iPeriodEndID);
		BigDecimal NHTQuantity=new BigDecimal(0);
		BigDecimal NHATQuantity=new BigDecimal(0);
		BigDecimal NHPQuantity=new BigDecimal(0);
		BigDecimal NHTGLBQuantity=new BigDecimal(0);
		
		try
		{
			if (rsAccidents.isBeforeFirst())
			{
				while (rsAccidents.next())
				{
					ResultSet rsCumulHours=GetCumulHoursByActivity(iPeriodStartID,iPeriodEndID,rsAccidents.getInt("C_ACTIVITY_ID"));
					
					if (rsCumulHours.isBeforeFirst())
					{
						while (rsCumulHours.next())
						try
						{
							if (rsCumulHours.getString("VALUE").toUpperCase().compareTo("NHT")==0)
								NHTQuantity=rsCumulHours.getBigDecimal("SUMQUANTITY");
							else if (rsCumulHours.getString("VALUE").toUpperCase().compareTo("NHAT")==0)
								NHATQuantity=rsCumulHours.getBigDecimal("SUMQUANTITY");
							else if (rsCumulHours.getString("VALUE").toUpperCase().compareTo("NHP")==0)
								NHPQuantity=rsCumulHours.getBigDecimal("SUMQUANTITY");
							else if (rsCumulHours.getString("VALUE").toUpperCase().compareTo("NHTGLB")==0)
								NHTGLBQuantity=rsCumulHours.getBigDecimal("SUMQUANTITY");
						}
						catch (Exception e)
						{
							System.err.println(_ClassErrorMessage + " [LaunchSortByActivity] " + e); 
						}			        
					}
					
					NHTGLBQuantity = GetNHTGLB(iPeriodStartID,iPeriodEndID);
					
					InsertIntoTempTable(iADInstanceID,
										rsAccidents.getInt("C_ACTIVITY_ID"),
										rsAccidents.getString("VALUE"),
										rsAccidents.getString("NAME"),
										FrequenceRelative(NHTQuantity),
										IndiceGravite(NHATQuantity,NHPQuantity),
										TauxGravite(NHTQuantity,NHATQuantity,NHPQuantity),TauxGraviteGlobal(NHATQuantity,NHPQuantity,NHTGLBQuantity));
					
				}
			}	
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [LaunchSortByActivity] " + e); 
		}
	}
	
	private ResultSet GetAccidentsByActivity(int iPeriodStartID,int iPeriodEndID)
	{
		String StringSQL="SELECT " +
							"AC.C_ACTIVITY_ID," + 
							"AC.VALUE," +
							"AC.NAME" +
	                    " FROM P_ACCIDENTREPORT A " +
							"INNER JOIN C_ACTIVITY AC ON AC.C_ACTIVITY_ID = A.C_ACTIVITY_ID " +
						" WHERE " +
							"ACCIDENTDATE>=(SELECT STARTDATE FROM P_PERIOD WHERE P_PERIOD_ID=" + iPeriodStartID + ") " +
							"AND ACCIDENTDATE<=(SELECT ENDDATE FROM P_PERIOD WHERE P_PERIOD_ID=" + iPeriodEndID + ") " +
 	                    " GROUP BY " +
							"AC.C_ACTIVITY_ID," +
							"AC.VALUE," +
							"AC.NAME";
		
		ResultSet rsAccidentReport=null;
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt= DB.prepareStatement(StringSQL, null);
			rsAccidentReport= pstmt.executeQuery();		
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [GetAccidentsByActivity] " + e); 
		}
		
		return rsAccidentReport;
	}

	private ResultSet GetCumulHoursByActivity(int iPeriodStartID,int iPeriodEndID,int iActivityID)
	{
		String StringSQL="SELECT " +
							"GI.VALUE, " +
							"SUM(PG.QUANTITY) SUMQUANTITY" + 
	                     " FROM " + 
						 	"P_PAYMENT_GAIN PG " +
								"INNER JOIN P_GAIN_GAININFO GGI ON GGI.P_GAIN_ID = PG.P_GAIN_ID " +
								"INNER JOIN P_GAININFO GI ON GI.P_GAININFO_ID = GGI.P_GAININFO_ID " +
								"INNER JOIN (SELECT DISTINCT P_PAYMENT_GAIN_ID,C_ACTIVITY_ID FROM P_PAYMENT_GAIN_DISTRIBUTION) PGD ON PGD.P_PAYMENT_GAIN_ID = PG.P_PAYMENT_GAIN_ID " +
	                    " WHERE " +
						    " PGD.C_ACTIVITY_ID = " + iActivityID +
							" AND GI.VALUE IN ('NHT','NHAT','NHP')" +
							" AND GGI.TO_CONSIDER='Y' " +
							" AND DAY>=(SELECT STARTDATE FROM P_PERIOD WHERE P_PERIOD_ID=" + iPeriodStartID + ")"+
							" AND DAY<=(SELECT ENDDATE FROM P_PERIOD WHERE P_PERIOD_ID="+ iPeriodEndID + ")" +
						" GROUP BY " +
							"GI.VALUE";
			
		ResultSet rsCumul=null;
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt= DB.prepareStatement(StringSQL, null);
			rsCumul= pstmt.executeQuery();		
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [GetCumulHoursByActivity] " + e); 
		}
		
		return rsCumul;
	}
		
	/*Tools to All Sort Types*/
	
	private BigDecimal GetNHTGLB(int iPeriodStartID,int iPeriodEndID)
	{
		String StringSQL="SELECT " +
							" ISNULL(SUM(PG.QUANTITY),0) AS SUMQUANTITY" + 
	                     " FROM " + 
						 	"P_PAYMENT_GAIN PG " +
								"INNER JOIN P_GAIN_GAININFO GGI ON GGI.P_GAIN_ID = PG.P_GAIN_ID " +
								"INNER JOIN P_GAININFO GI ON GI.P_GAININFO_ID = GGI.P_GAININFO_ID " +
	                    " WHERE " +
							" GI.VALUE = 'NHTGLB' " +
							" AND GGI.TO_CONSIDER='Y' " +
							" AND DAY>=(SELECT STARTDATE FROM P_PERIOD WHERE P_PERIOD_ID=" + iPeriodStartID + ")"+
							" AND DAY<=(SELECT ENDDATE FROM P_PERIOD WHERE P_PERIOD_ID="+ iPeriodEndID + ")";
			
		ResultSet rsCumul=null;
		PreparedStatement pstmt = null;
		BigDecimal lNHTGLB = new BigDecimal(0);
		
		try
		{
			pstmt= DB.prepareStatement(StringSQL, null);
			rsCumul= pstmt.executeQuery();	
			
			if (rsCumul.isBeforeFirst())
			{
				while (rsCumul.next())
				{
					lNHTGLB = rsCumul.getBigDecimal("SUMQUANTITY");
				}		        
			}
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [GetNHTGLB] " + e); 
		}
				
		return lNHTGLB;
	}
	
	private int CountAccidents(int iPeriodStartId,int iPeriodEndID)
	{
		String StringSQL="SELECT " + 
							" COUNT(*) COUNT " +
	                     "FROM P_ACCIDENTREPORT " +
	                    "WHERE " +
							"ACCIDENTDATE>=(SELECT STARTDATE FROM P_PERIOD WHERE P_PERIOD_ID=" + iPeriodStartId + ")" +
							"AND ACCIDENTDATE<=(SELECT ENDDATE FROM P_PERIOD WHERE P_PERIOD_ID=" + iPeriodEndID + ")";
		
		ResultSet rsAccidentReport=null;
		PreparedStatement pstmt = null;
		int Count=0;
		
		try
		{
			pstmt= DB.prepareStatement(StringSQL, null);
			rsAccidentReport= pstmt.executeQuery();		
			
			if (rsAccidentReport.isBeforeFirst())
			{
				rsAccidentReport.next();
				Count=rsAccidentReport.getInt("COUNT");
			}
		}
		catch (Exception e)
		{
			System.err.println(_ClassErrorMessage + " [GetAccidentsByJobTitle] " + e); 
		}
		
		return Count;
	}
	
	private void InsertIntoTempTable(int iADPInstanceID,int iID,String iValue,String iDescription,BigDecimal iAmount1,BigDecimal iAmount2, 
									BigDecimal iAmount3,BigDecimal iAmount4)
	{
		P_T_AccidentReport AccidentReport=new P_T_AccidentReport(Env.getCtx(), -1, null);
		AccidentReport.setAD_PInstance_ID(iADPInstanceID);
		AccidentReport.setP_AccidentReport_ID(iID);
		AccidentReport.setValue(iValue);
		AccidentReport.setDescription(iDescription);
		AccidentReport.setAmount1(iAmount1);
		AccidentReport.setAmount2(iAmount2);
		AccidentReport.setAmount3(iAmount3);
		AccidentReport.setAmount4(iAmount4);
		AccidentReport.save();
	}
	
	private BigDecimal FrequenceRelative(BigDecimal NHT)
	{
		try
		{
			return (new BigDecimal(_CountAccidents).multiply(new BigDecimal(1000000)).divide(NHT,5,BigDecimal.ROUND_HALF_UP));
		}
		catch (ArithmeticException e)
		{
			return new BigDecimal(0);
		}
		catch (Exception e)
		{
			return new BigDecimal(-1);
		}
	}
	
	private BigDecimal IndiceGravite(BigDecimal NHAT,BigDecimal NHP)
	{
		try
		{
			return ((NHP.add(NHAT)).divide(new BigDecimal(_CountAccidents),5,BigDecimal.ROUND_HALF_UP));
		}
		catch (ArithmeticException e)
		{
			return new BigDecimal(0);
		}
		catch (Exception e)
		{
			return new BigDecimal(-1);
		}

	}
	
	private BigDecimal TauxGravite(BigDecimal NHT,BigDecimal NHAT,BigDecimal NHP)
	{
		try
		{
			return  ((NHP.add(NHAT)).multiply(new BigDecimal(1000000)).divide(NHT,5,BigDecimal.ROUND_HALF_UP));
		}
		catch (ArithmeticException e)
		{
			return new BigDecimal(0);
		}
		catch (Exception e)
		{
			return new BigDecimal(-1);
		}
	}	
	
	private BigDecimal TauxGraviteGlobal(BigDecimal NHAT,BigDecimal NHP,BigDecimal NHTGLB)
	{
		try
		{
			return ((NHP.add(NHAT)).multiply(new BigDecimal(1000000)).divide(NHTGLB,5,BigDecimal.ROUND_HALF_UP));
		}
		catch (ArithmeticException e)
		{
			return new BigDecimal(0);
		}
		catch (Exception e)
		{
			return new BigDecimal(-1);
		}
	}

}
