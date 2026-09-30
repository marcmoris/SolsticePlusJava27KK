package solstice.migration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.compiere.Compiere;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import solstice.model.P_Employee_Form;
import solstice.model.P_Form_Template;
import solstice.model.P_Payment;
import solstice.model.P_Region;
import solstice.model.P_Employee;
import solstice.process.TimeValidation;
import solstice.model.P_Time_Sheet;

public class GL_Remake {

	Properties ctx = Env.getCtx();

	/** Transaction			*/
	private String trxName = null; // Trx.createTrxName();

	
	/**
	 * 	Generate PO Class
	 * 	@param AD_Table_ID table id
	 * 	@param directory directory with \ or / at the end.
	 * 	@param packageName package name
	 * @throws SQLException
	 */
	public GL_Remake () 
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);

		int count = 0;

//Mario ICI...
		String sql = "Select P_Payment_ID from P_Payment where p_payment_id in ( select p_payment_id from p_time_sheet  where createdby = 0 and sheettype = 'Adjustement' and timesheetstatus = 'T' and value like '%A%' and p_employee_id in ( select p_employee_id from p_employee where value = '045550' )) ";
		
//		sql += " and P_Employee_ID in ( select p_employee_id from p_payment_gain where p_gain_id in ( select p_gain_id from p_gain where value = '98' ) ) "		;
		
		PreparedStatement pstmt = null;

		try 
		{

			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
	            P_Payment payment = new P_Payment( Env.getCtx(), rs.getInt("P_Payment_ID"), trxName);
	            P_Time_Sheet TimeSheet = P_Time_Sheet.GetWithPaymentID(Env.getCtx(), payment.getP_Payment_ID(), trxName);
	            if ( TimeSheet != null)
	            {
	                TimeValidation timeValidation = new TimeValidation( TimeSheet, trxName);
	                
	                DB.executeUpdate("Delete from P_Payment_Gain_Distribution where P_Payment_Gain_ID in ( select P_Payment_Gain_ID From P_Payment_Gain where P_Payment_ID = " + payment.getP_Payment_ID() + ")", trxName);

					timeValidation.Create_PaymentGainDistribution(payment, 0);
	        		payment.createEntryLine( trxName );
	        		payment.save( trxName );
	            	
	            }
				count++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Generated = " + count);


	}
		




	/**************************************************************************
	 * 	Migration des données pour la HLC création TD1 pour tout les employés
	 */
	public static void main (String[] args)
	{
		System.out.println("GL_Remake   $Revision: 1.1 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		
		int count = 0;
//Mario ICI...
// Elever ce commentaire pour exécute ce programme.		
		new GL_Remake();
		count++;
		System.out.println("Generated = " + count);

	}	//	main

	
}
