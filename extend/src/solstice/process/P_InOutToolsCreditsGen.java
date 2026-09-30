/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/
package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.math.BigDecimal;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.TimeUtil;


import solstice.model.*;



public class P_InOutToolsCreditsGen extends SvrProcess
{
	private String m_trxName; 
	public int DisplayDetail = 0;
	private int m_inserted;
	private int Record_ID;
	private int Employee_ID = 0;
	private boolean GenParicularSheet = true;
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_InOutToolsCredits.class);

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		Record_ID = this.getRecord_ID();
		
		m_trxName = this.get_TrxName();

		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("GenParicularSheet"))
				GenParicularSheet  = "Y".equals(para[i].getParameter());
			
			if (name.equals("P_Employee_ID"))
				Employee_ID = para[i].getParameterAsInt();
		}
        
	}	//	prepare

	 
	/**
	 * 	Read each time sheet 
	 *	@return 
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		P_Tools_Credits ToolsCredits = new P_Tools_Credits( getCtx(), Record_ID, null );
		String sql = " Select *, (TotalCreditsSrc - isnull(QuantityTransfer,0)) QtyPay From P_Tools_Credits_Result, P_Employee  "
			+ " WHERE P_Tools_Credits_ID = " +  ToolsCredits.getP_Tools_Credits_ID()
			+ " AND P_Tools_Credits_Result.IsActive = 'Y' "
			+ " AND ( TotalCreditsSrc - isnull(QuantityTransfer,0)) <> 0"
			+ " AND P_Tools_Credits_Result.P_Employee_ID = P_employee.P_Employee_ID"
			+ " AND IsTimeSheetTransferred = 'N' "
			;
		
		if ( Employee_ID != 0)
			sql = sql + " AND P_Tools_Credits_Result.P_Employee_ID = " + Employee_ID;
		
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		try
		{
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				P_Tools_Credits_Result result = new P_Tools_Credits_Result( getCtx(), rs.getInt( "P_Tools_Credits_Result_ID"), m_trxName);

				if ( GenParicularSheet )
				{
					P_Particular_Sheet Result = new P_Particular_Sheet( getCtx(), -1, null );
					Result.setP_Employee_ID( rs.getInt("P_Employee_ID"));
					Result.setP_Gain_ID( ToolsCredits.getP_Gain_ID());
					Result.setP_Period_ID( getPeriodID( ToolsCredits.getTransfertDate()) );
					Result.setDay(ToolsCredits.getTransfertDate() );
					Result.setDayQty( rs.getBigDecimal("QtyPay").setScale(6, BigDecimal.ROUND_HALF_UP ));
					
					P_Credits credits = P_Credits.get( getCtx(), ToolsCredits.getCredits_Dst_ID(), null);
					//2024-05-06 conversion en heures les jours.
					if ( credits.getP_UOM_ID() == 102 )
					{
						P_Employee Employee = P_Employee.get(getCtx(), rs.getInt("P_Employee_ID"), null);
						P_Assignment_Param assignment_Param = Employee.getAssignment_Param(ToolsCredits.getTransfertDate(), null);
						Result.setDayQty( rs.getBigDecimal("QtyPay").setScale(6, BigDecimal.ROUND_HALF_UP ).multiply( assignment_Param.getDay_Hours() ) );
					}
						
					
					Result.setP_Schedule_ID( 100000 );
					Result.setIsActive(true);
					Result.setC_Activity_ID(rs.getInt("C_Activity_ID"));
					Result.setP_Department_ID(rs.getInt("P_Department_ID"));
					Result.save();
					
				}
				else
					
				{
					Timestamp PeriodDate = ToolsCredits.getTransfertDate();
					P_Period Period = P_Period.get( getCtx(),  getPeriodID( ToolsCredits.getTransfertDate()), m_trxName);
					P_Period Period_Payment = P_Period.getOpenPeriod(getCtx(), m_trxName);

					P_Employee Employee = P_Employee.get( getCtx(), rs.getInt("P_Employee_ID"), m_trxName );
					
					s_log.log(Level.INFO,"Employee - " + Employee.getValue() );
					
					P_Assignment_Param AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), PeriodDate, m_trxName);
					P_Schedule Schedule = P_Schedule.get( getCtx(), AssignmentParam.getP_Schedule_ID(), m_trxName );
					P_Gain Gain = P_Gain.get( getCtx(), ToolsCredits.getP_Gain_ID(), m_trxName );

					
					P_Time_Sheet  timeSheet = P_Time_Sheet.get( getCtx(), Period_Payment.getP_Period_ID(), Employee.getP_Employee_ID(), P_Time_Sheet.SHEETTYPE_Regular ,  m_trxName);
					if ( timeSheet == null)
						s_log.log(Level.SEVERE,"Employee sans feuille de temps - " + Employee.getValue() );
//						break;
					
					if ( ! timeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Initial ))
					{
						s_log.log(Level.SEVERE,"Employee feuille de temps non initial - " + Employee.getValue() );
						break;
						
					}
					
// Mettre un message ici

					if ( timeSheet != null)
					{
						P_Time_Sheet_Detail timeSheetDetail = new P_Time_Sheet_Detail( getCtx(), -1, m_trxName);
						
		                timeSheetDetail.setP_Time_Sheet_ID(timeSheet.getP_Time_Sheet_ID());

//		                if ( Activity != null )
//		        			timeSheetDetail.setC_Activity_ID( Activity.getC_Activity_ID());
		        		
//		        		if ( SelfFinancedLeave != null)
//		        			timeSheetDetail.setSalaryPercentage( SelfFinancedLeave.getSalaryPercentage() );

//						int weekNumber = getWeek(new BigDecimal( TimeUtil.getDaysBetween(Period.getStartDate(), PeriodDate) +1 ) .divide(new BigDecimal(7), 0, BigDecimal.ROUND_UP).intValue());

		                timeSheetDetail.setP_Assignment_ID(AssignmentParam.getP_Assignment_ID());
//		                timeSheetDetail.setWeekIndex(weekNumber);
		                timeSheetDetail.setHourly_Rate(AssignmentParam.getHourlyRate( Gain, ToolsCredits.getTransfertDate() ));

		                timeSheetDetail.setDay(ToolsCredits.getTransfertDate());
		                timeSheetDetail.setIsActive(true);
		                timeSheetDetail.setDayQty(rs.getBigDecimal("QtyPay").setScale(6, BigDecimal.ROUND_HALF_UP ));
		                timeSheetDetail.setOriginTime( "PAR");
		                timeSheetDetail.setP_Gain_ID(ToolsCredits.getP_Gain_ID());
		                timeSheetDetail.setP_Period_ID(getPeriodID( ToolsCredits.getTransfertDate()));
		                timeSheetDetail.setP_Schedule_ID( Schedule.getP_Schedule_ID() );
		                timeSheetDetail.setStartDate(ToolsCredits.getTransfertDate());
		                timeSheetDetail.setEndDate(ToolsCredits.getTransfertDate());

		                timeSheetDetail.save();
						
					}



	                result.setIsTimeSheetTransferred( true);
	                result.save();
					
					m_inserted++;
						
					}
						

					
			}
	  		rs.close ();
	  		pstmt.close ();
	  		pstmt = null;

		}
		catch(Exception e)
		{
		  	s_log.log(Level.SEVERE,"P_InOutToolsCreditsGen- " + sql, e);
		}

		return  "Enregistrements créés " + m_inserted;
	}
 
	private int getWeek(int valeur)
	{
		if (DisplayDetail == 0)
		{
			return valeur;
		}
		else
		{
			return 1;
		}
	}
	   // 
    // retourne la période en fonction de la date 
    private int getPeriodID(  Timestamp date )
    {
        int Period_ID = 0;
        String sql = "select P_Period_ID from P_Period where " + DB.TO_DATE( date ) + " between StartDate and EndDate" ;
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
            	Period_ID = rs.getInt("P_Period_ID");
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "getPeriodID", e);
        }
        
        return Period_ID;
    }

}
