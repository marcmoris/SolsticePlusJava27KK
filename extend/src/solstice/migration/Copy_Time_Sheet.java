/*
 * Created on 2005-08-09
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.migration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Time_Sheet;
import solstice.model.P_Time_Sheet_Detail;
import solstice.model.*;
import org.compiere.*;
/**
 * @author marmor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Copy_Time_Sheet {

	Properties ctx = Env.getCtx();
	
	/** Transaction			*/
	private String m_trxName = null; //Trx.createTrxName();

	/**
	 * 	Generate PO Class
	 * 	@param AD_Table_ID table id
	 * 	@param directory directory with \ or / at the end.
	 * 	@param packageName package name
	 */
	private Copy_Time_Sheet ()
	{
		Env.setContext( ctx, "#AD_Client_ID", 11);
		Env.setContext( ctx, "#AD_Org_ID", 11);
		int count = 0;
		
		P_Period Period = P_Period.getOpenPeriod( ctx, m_trxName);
		
		String sql = "select P_Time_Sheet_ID from P_Time_Sheet Where P_Period_ID = 1001709";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			int i = 0;
			while (rs.next())
			{
				    P_Time_Sheet old = P_Time_Sheet.get( ctx, rs.getInt("P_Time_Sheet_ID"), m_trxName);
				    P_Time_Sheet TimeSheet = new P_Time_Sheet( ctx, -1, m_trxName);
				    TimeSheet.setP_Employee_ID ( old.getP_Employee_ID() );
				    TimeSheet.setP_Frequency_ID( old.getP_Frequency_ID() ) ;
				    TimeSheet.setP_Job_Title_ID( old.getP_Job_Title_ID());
				    TimeSheet.setP_Occupation_Group_ID( old.getP_Occupation_Group_ID());
				    TimeSheet.setP_Payment_Group_ID( old.getP_Payment_Group_ID());
				    TimeSheet.setP_Period_ID( Period.getP_Period_ID());
				    TimeSheet.setP_Year_ID( Period.getP_Year_ID());
				    TimeSheet.setPaymentType( old.getPaymentType());
				    TimeSheet.setDescription( old.getDescription());
				    TimeSheet.setIsComplete( false );
				    TimeSheet.setIsError( false );
				    TimeSheet.setIsWarning( false);
				    
				    TimeSheet.setSheetType( P_Time_Sheet.SHEETTYPE_Regular);
				    TimeSheet.setTimeSheetStatus( P_Time_Sheet.TIMESHEETSTATUS_Initial);
				    TimeSheet.setValue( old.getValue().replaceAll("0500", "2005-15-") );
				    TimeSheet.setValue( TimeSheet.getValue().replaceAll("0501", "2005-15-") );
				    TimeSheet.setP_Job_Type_ID( old.getP_Job_Type_ID());
					TimeSheet.setP_Distribution_ID( old.getP_Distribution_ID());
				    TimeSheet.setP_Distribution_Booklet_ID( old.getP_Distribution_Booklet_ID());
				    TimeSheet.save();
	            	
				    create_TimeSheetDetail( old, TimeSheet);

					count++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("Copy_Time_Sheet - " + e);
		}

		System.out.println("Generated = " + count);


	}

	private void create_TimeSheetDetail( P_Time_Sheet oldTime, P_Time_Sheet TimeSheet)
	{
		String sql = "select P_Time_Sheet_Detail_ID from P_Time_Sheet_Detail Where P_Time_Sheet_ID = " + oldTime.getP_Time_Sheet_ID();

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			int i = 0;
			while (rs.next())
			{
				P_Time_Sheet_Detail old = P_Time_Sheet_Detail.get( ctx, rs.getInt("P_Time_Sheet_Weekly_ID") );
				P_Time_Sheet_Detail TimeSheetDetail = new P_Time_Sheet_Detail( ctx, -1, null );
				TimeSheetDetail.setC_Activity_ID( old.getC_Activity_ID());
				TimeSheetDetail.setP_Assignment_ID( old.getP_Assignment_ID());
				TimeSheetDetail.setP_Gain_ID(  old.getP_Gain_ID());
				TimeSheetDetail.setP_Period_ID( TimeSheet.getP_Period_ID());
				TimeSheetDetail.setP_Schedule_ID( old.getP_Schedule_ID());
				TimeSheetDetail.setP_Time_Sheet_ID( TimeSheet.getP_Time_Sheet_ID() );
				
				TimeSheetDetail.setDayQty( old.getDayQty()); 
				TimeSheetDetail.setWeekIndex( old.getWeekIndex() );
				TimeSheetDetail.setP_Employee_LongTermLeave_ID( old.getP_Employee_LongTermLeave_ID());
				TimeSheetDetail.setDay( old.getDay());
				TimeSheetDetail.setOriginTime( old.getOriginTime());
				TimeSheetDetail.setSalaryPercentage( old.getSalaryPercentage());
				TimeSheetDetail.save();
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("create_TimeSheetWeekly - " + e);
		}
		
	}
    

	
	/**
	 * 	String representation
	 * 	@return string representation
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("Copy_Time_Sheet[")
			.append("]");
		return sb.toString();
	}	//	toString



	/**************************************************************************
	 * 	Copy_Time_Sheet 
	 */
	public static void main (String[] args)
	{
		System.out.println("Copy_Time_Sheet   $Revision: 1.1 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		int count = 0;
		new Copy_Time_Sheet();
		count++;
		System.out.println("Generated = " + count);

	}	//	main

}
