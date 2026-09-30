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



public class Maj_Deduction {

	/**************************************************************************
	 * 	Migration des données pour la SIQ
	 */
	public static void main (String[] args)
	{
		System.out.println("Migration   $Revision: 1.1 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);

		new Maj_Deduction();

		
	}	//	main
	
	private static CLogger		log = CLogger.getCLogger (Maj_Deduction.class);

	/** Transaction			*/
	private String trxName = null; // Trx.createTrxName();

	Properties ctx = Env.getCtx();

    private BigDecimal salaryAdmissible ;

	public Maj_Deduction () 
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);

		int count = maj();
		System.out.println("created = " + count);
		 
	}

	private P_Employee_Deduction EmployeeDeduction = null;
	private P_Deduction_Param Deduction_Param = null;
	private P_Period Period = null;

	private int maj()
	{
		int count = 0;
		
		String sql = "Select distinct P_Period_ID, P_Employee_ID, Taxation_Region_ID, P_Payment_Group_ID, P_Payment_ID from P_Payment where p_period_id between 1001992 and 1001996  and taxation_region_id in (158)  "
        ;
		
		P_Payment Payment;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			int no = 0;
			int old_periodid = 0;
			while (rs.next())
			{
				Period = new P_Period( Env.getCtx(), rs.getInt("P_Period_ID"), null);
				
				if ( old_periodid != Period.getP_Period_ID() )
				{
					old_periodid = Period.getP_Period_ID();
					no = 0;
				}
				
				P_Employee Employee = P_Employee.get(Env.getCtx(), rs.getInt( "P_Employee_ID"), trxName);
				
				log.info("Calcul -   " + Employee.getValue() + " " + Employee.getName()  );

				Payment = new P_Payment( Env.getCtx(), rs.getInt("P_Payment_ID"), trxName);
				
				maj2( Payment);
				
				
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
	
	private	BigDecimal cumEmployerAmount = Env.ZERO;
	private	BigDecimal cumEmployeeAmount = Env.ZERO;
	private	BigDecimal cumSalaryAdmissible = Env.ZERO;
	private	BigDecimal cumHoursAdmissible  = Env.ZERO;
	
	private int maj2( P_Payment Payment ) throws Exception
	{
		int count = 0;
		
		P_Payment_Deduction PaymentDeduction;
		P_Deduction Deduction;
		
        int DeductionID = 0;
		      DeductionID = 1002733;

				
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
				
				PaymentDeduction.setEmployee_Part( Env.ZERO );

				BigDecimal salaryAdmissible = Env.ZERO;
				BigDecimal MaximumAllowableEarning = Deduction_Param.getMaximumAllowableEarnings();
				
		    	salaryAdmissible =  getSalaryAdmissible( Payment.getP_Payment_ID()) ;


				PaymentDeduction.setSalary_Eligible( salaryAdmissible.setScale(2, BigDecimal.ROUND_HALF_UP) );
				PaymentDeduction.setEmployer_Part( (PaymentDeduction.getSalary_Eligible().multiply( Deduction_Param.getEmployerRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_DOWN))).setScale(2, BigDecimal.ROUND_HALF_DOWN) );

				log.info("Calcul -   " + Period.getName() + " " + Deduction.getValue() + " " + PaymentDeduction.getSalary_Eligible() + " " + PaymentDeduction.getEmployer_Part());

				PaymentDeduction.setHours_Eligible( Env.ZERO );
				PaymentDeduction.setIsExonerated( false );
				PaymentDeduction.setOrigine("FTP");
				PaymentDeduction.setP_Deduction_Param_ID( Deduction_Param.getP_Deduction_Param_ID());
				PaymentDeduction.setP_Employee_Deduction_ID( EmployeeDeduction.getP_Employee_Deduction_ID());
				
				PaymentDeduction.save( );
				
				count ++;

		return count;
		
	}
	
	
	private BigDecimal getSalaryAdmissible(int payment_Id  ) throws Exception
	{
		BigDecimal amt = null;
		String query = null;
		query = "select dbo.fn_getsalaire_eligible_tmp( " + payment_Id + " )";
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			amt = rs.getBigDecimal( 1 );
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
		
		return amt;

	}

	//
	// Read Cumulative information about a deduction for this Year..
	// 
	private void get_deduction_cumulative( P_Payment Payment) throws Exception
	{
		String query = null;
		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible"
			+ " from P_Payment_Deduction, P_Payment"
			+ " where P_Payment_Deduction.P_Deduction_ID = " + EmployeeDeduction.getP_Deduction_ID ()
			+ " and P_Payment.P_Year_ID = " + this.Period.getP_Year_ID ()
			+ " and P_Payment_Deduction.P_Period_ID < " + this.Period.getP_Period_ID()
			+ " and P_Payment_Deduction.P_Employee_ID = " + Payment.getP_Employee_ID ()
			+ " and P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID "
			;

		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " Query " + query ) ;

		PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				this.cumEmployeeAmount =     rs.getBigDecimal("Employee_Part");
				this.cumEmployerAmount =     rs.getBigDecimal("Employer_Part");
				this.cumSalaryAdmissible =   rs.getBigDecimal("Salary_Eligible");
				this.cumHoursAdmissible =    rs.getBigDecimal("Hours_Eligible");
			}
			else
			{
				this.cumEmployeeAmount =     Env.ZERO;
				this.cumEmployerAmount =     Env.ZERO;
				this.cumSalaryAdmissible =   Env.ZERO;
				this.cumHoursAdmissible =    Env.ZERO;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			if ( this.cumEmployeeAmount == null )
				this.cumEmployeeAmount =     Env.ZERO;
			if ( this.cumEmployerAmount == null)
				this.cumEmployerAmount =     Env.ZERO;
			if ( this.cumSalaryAdmissible == null)
				this.cumSalaryAdmissible =   Env.ZERO;
			if (this.cumHoursAdmissible == null)
				this.cumHoursAdmissible =    Env.ZERO;
			
	}	


}
