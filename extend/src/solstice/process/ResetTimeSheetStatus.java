package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Payment;

//
// Procedure for urgence 
//   Reset the status of the timesheet to calculated after a deposit generation
//
//
public class ResetTimeSheetStatus extends SvrProcess
{
	
	private int AD_Pinstance_ID = 0;
//	private int time_Sheet_ID = 0;
	private int frequency_ID = 0;
	private int period_ID = 0;
	private int employee_ID = 0;
	private int payment_Group_ID = 0;
    private int distribution_Booklet_ID = 0;
	private int	m_updated = 0;

	protected void prepare() 
	{
		ProcessInfoParameter[] para = getParameter();
		String paramName = "";
		AD_Pinstance_ID = this.getAD_PInstance_ID();
		//Read the parameters
		for(int i=0; i < para.length; i++)
		{ 
			paramName = para[i].getParameterName();
			if (paramName.equals("P_Frequency_ID"))
			{
				frequency_ID = para[i].getParameterAsInt();
			}
			if( paramName.equals("P_Period_ID"))
			{
				period_ID = para[i].getParameterAsInt();
			}
			if (paramName.equals("P_Employee_ID"))
			{
				employee_ID = para[i].getParameterAsInt();
			}
			if (paramName.equals("P_Payment_Group_ID"))
			{
				payment_Group_ID = para[i].getParameterAsInt();
			}
			if ( paramName.equals("P_Distribution_Booklet_ID"))
			{
				distribution_Booklet_ID = para[i].getParameterAsInt();
			}
//			if (paramName.equals("P_Time_Sheet_ID"))
//			{
//				time_Sheet_ID = para[i].getParameterAsInt();
//			}


		}
	}
		

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	protected String doIt() throws Exception 
	{
   	   if (period_ID == 0)
			log.log (Level.WARNING,"doIt - Period not found");

		String sql = null;
		sql = "Select P_Time_Sheet_ID From P_Time_Sheet " 
            + " WHERE P_Time_Sheet.IsActive='Y' "
	        + "   AND SheetType in ( '" + P_Time_Sheet.SHEETTYPE_Regular + "', '" +  P_Time_Sheet.SHEETTYPE_Complementary + "', '" + "', '" +  P_Time_Sheet.SHEETTYPE_ExpenseAccount + "', '"  + "', '" +  P_Time_Sheet.SHEETTYPE_HolidayPayment + "', '"  +  P_Time_Sheet.SHEETTYPE_SeparationPay + "' ) "
            + "   AND P_Time_Sheet.TimesheetStatus = 'E' ";
		
		if (employee_ID > 0)
			sql += " AND P_Employee_ID=" + employee_ID;
		if (period_ID > 0)
			sql += " AND P_Period_ID=" + period_ID;
		if (payment_Group_ID > 0)
			sql += " AND P_Payment_Group_ID=" + payment_Group_ID;

		if (distribution_Booklet_ID > 0)
			sql += " AND P_Distribution_Booklet_ID=" + distribution_Booklet_ID;

	//	if (time_Sheet_ID > 0)
	//		sql += " AND P_Time_Sheet_ID=" + time_Sheet_ID;

		PreparedStatement pstmt = null;
		try 
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			m_updated = 0;
			while (rs.next ()) 
			{
				P_Time_Sheet TimeSheet = P_Time_Sheet.get( Env.getCtx(), rs.getInt("P_Time_Sheet_ID"), null);
				TimeSheet.setTimeSheetStatus( P_Time_Sheet.TIMESHEETSTATUS_Calculated);
				TimeSheet.save();
				
				P_Payment Payment = P_Payment.get( Env.getCtx(), TimeSheet.getP_Payment_ID(), null);
				Payment.setPrePrintedNo(null);
				Payment.save();
				
	            m_updated++;

		    }
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"ResetTimeSheet - " + sql, e);
			throw e;
		}
	   return "@Updated@ = " + m_updated;
	}

}
