package solstice.migration;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.Compiere;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.*;



public class Maj_03 {

	/**************************************************************************
	 * 	Migration des données pour la SIQ
	 */
	public static void main (String[] args)
	{
		System.out.println("Migration   $Revision: 1.1 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);

	//	new Maj_03();

		
	}	//	main
	
	private static CLogger		log = CLogger.getCLogger (Maj_Deduction.class);

	/** Transaction			*/
	private String trxName = null; // Trx.createTrxName();

	Properties ctx = Env.getCtx();

	public Maj_03 () 
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);

		int count = maj();
		System.out.println("created = " + count);
		 
	}

	private P_Employee_Deduction EmployeeDeduction = null;
	private P_Deduction_Param Deduction_Param = null;
	private P_Period Period = null;

	private int maj(  )
	{
		int count = 0;
		
		String sql = "select p_payment.p_payment_id,  [dbo].[fn_getsalaire_eligible03]( p_payment.p_payment_id ) newsal"
                   +  " from p_payment"
                   +  " inner join p_employee on p_employee.p_employee_id = p_payment.p_employee_id"
                   +  " left outer join p_payment_deduction on p_payment_deduction.p_payment_id = p_payment.p_payment_id" 
                   +  "             and p_deduction_id =  1002557"
                   +  "  where p_payment.p_period_id in ( select p_period_id from p_period where name = '2009-23')"
                   +  " and p_payment.paymenttypedoc != 'Adjustement'"
                   +  " and isnull( p_payment_deduction.salary_eligible, 0) = 0"
                   + " and p_payment_deduction.employee_part is null ";
        
		P_Payment_Deduction PaymentDeduction;
		P_Deduction Deduction;
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
	
				P_Payment Payment = P_Payment.get( Env.getCtx(), rs.getInt("P_Payment_ID"),  trxName);
		        int DeductionID = 1002557;
				Period = new P_Period( Env.getCtx(), Payment.getP_Period_ID(),  trxName);
				Deduction = new P_Deduction( Env.getCtx(), DeductionID, trxName);

				EmployeeDeduction = P_Employee_Deduction.get(Env.getCtx(), Payment.getP_Employee_ID(), Deduction.getP_Deduction_ID(), Period.getStartDate(), trxName);

				Deduction_Param = P_Deduction_Param.get(ctx, Deduction.getP_Deduction_ID(), Period.getStartDate(), trxName);

				PaymentDeduction = new P_Payment_Deduction ( Env.getCtx (), -1, trxName);
				
				PaymentDeduction.setAD_Org_ID(Payment.getAD_Org_ID());
				PaymentDeduction.setP_Employee_ID( Payment.getP_Employee_ID());
				PaymentDeduction.setP_Deduction_ID( Deduction_Param.getP_Deduction_ID() );
				PaymentDeduction.setP_Year_ID(  Period.getP_Year_ID () );
				PaymentDeduction.setP_Period_ID( Period.getP_Period_ID () );
				PaymentDeduction.setP_Payment_ID( Payment.getP_Payment_ID ());
				

				PaymentDeduction.setSalary_Eligible( rs.getBigDecimal("newsal") );
				PaymentDeduction.setEmployee_Part( Env.ZERO );
				PaymentDeduction.setEmployer_Part( Env.ZERO );

				log.info("Calcul -   " + Period.getName() + " " + Deduction.getValue() + " " + PaymentDeduction.getSalary_Eligible() + " " + PaymentDeduction.getEmployer_Part());

				PaymentDeduction.setHours_Eligible( Env.ZERO );
				PaymentDeduction.setIsExonerated( false );
				PaymentDeduction.setOrigine("MAJ");
				PaymentDeduction.setP_Deduction_Param_ID( Deduction_Param.getP_Deduction_Param_ID());
				PaymentDeduction.setP_Employee_Deduction_ID( EmployeeDeduction.getP_Employee_Deduction_ID());
				
				PaymentDeduction.save( );
				
				count ++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"calcul - " + sql, e);
		}

		return count;
		
	}


}
