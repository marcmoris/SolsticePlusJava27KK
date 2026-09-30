package solstice.process;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;



import solstice.model.P_Payment;
import solstice.process.TimeValidation;
import solstice.model.P_Time_Sheet;

public class P_InOutCreateGenerateLedger extends SvrProcess 
{
	//
	// Generate GL Entry for each Paiement  
	//
	private int Org_ID = 0;
	private int Employee_ID = 0;
	private int Period_ID = 0;
	private String trxName = null; // Trx.createTrxName();
	Properties ctx = Env.getCtx();
	
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_Org_ID"))
				Org_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Employee_ID"))
				Employee_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Period_ID"))
				Period_ID = para[i].getParameterAsInt();
			else
				log.log (Level.SEVERE,"prepare - Unknown Parameter: " + name);
		}
	}
	
	protected String doIt () throws Exception
	{
		boolean retValue = true;
		String ret = "";

		int count = 0;

		String sql = "Select P_Payment_ID from P_Payment where P_Period_ID = " + Integer.toString(Period_ID);
		
		if (Employee_ID != 0)
			sql = sql + "AND P_Employee_ID = " + Integer.toString(Employee_ID);

		if (Org_ID != 0)
			sql = sql + "AND AD_Org_ID = " + Integer.toString(Org_ID);

		//tempo
//		sql = sql + " AND P_Payment.PaymentTypeDoc = 'Adjustement' and CreatedBy = 0";

		PreparedStatement pstmt = null;

		try 
		{

			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			P_Payment payment;
			P_Time_Sheet TimeSheet;
			TimeValidation timeValidation;
			
			while (rs.next())
			{
	            payment = new P_Payment( Env.getCtx(), rs.getInt("P_Payment_ID"), trxName);
	            TimeSheet = P_Time_Sheet.GetWithPaymentID(Env.getCtx(), payment.getP_Payment_ID(), trxName);
	            if ( TimeSheet != null)
	            {
	            	//2013.01.30
	            	//path pour corrigé lorsqu'un ajustement est en erreur a cause de l'écriture comptable.
	            	if ( TimeSheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Error))
	            		TimeSheet.setTimeSheetStatus( P_Time_Sheet.TIMESHEETSTATUS_Calculated);

	                timeValidation = new TimeValidation( TimeSheet, trxName);
	                
	                DB.executeUpdate("Delete from P_Payment_Gain_Distribution where P_Payment_Gain_ID in ( select P_Payment_Gain_ID From P_Payment_Gain where P_Payment_ID = " + payment.getP_Payment_ID() + ")", trxName);
	
					timeValidation.Create_PaymentGainDistribution(payment, 0);
	        		payment.createEntryLine( trxName );
	        		payment.save( trxName );
	
					count++;
	            }
			}
			rs.close();
			pstmt.close();
			pstmt = null;

		} catch (Exception e) {
			e.printStackTrace();
			retValue = false;
		}
		return retValue == true ? ret : "error";
		
		
	}
}
