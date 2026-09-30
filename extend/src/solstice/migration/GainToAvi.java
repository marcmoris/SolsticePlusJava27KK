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

import solstice.model.*;
import org.compiere.*;
/**
 * @author marmor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GainToAvi {

	Properties ctx = Env.getCtx();
	
	/** Transaction			*/
	private String m_trxName = null; //Trx.createTrxName();

	/**
	 * 	Generate PO Class
	 * 	@param AD_Table_ID table id
	 * 	@param directory directory with \ or / at the end.
	 * 	@param packageName package name
	 */
/*	private GainToAvi()
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);
		int count = 0;
		
		//'69','70'
		String sql = "select p_payment_gain_id from p_payment_gain where p_gain_id in ( select p_gain_id from p_gain where value in ( 71) ) Order By P_Period_ID";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			int i = 0;
			int P_Taxable_Benefit_ID = 0;
			int P_Deduction_ID = 0;
			int P_Deduction_Param_ID = 0;
			
			while (rs.next())
			{


				P_Payment_Gain PaymentGain = new P_Payment_Gain( ctx, rs.getInt("P_Payment_Gain_ID"), null);
				P_Payment Payment = new P_Payment( ctx, PaymentGain.getP_Payment_ID(), null);
				P_Payment_Taxable_Benefit Avi = new P_Payment_Taxable_Benefit( ctx, -1, null);

				if ( PaymentGain.getP_Gain_ID() == 1003596)
				{
					P_Taxable_Benefit_ID = 1000020;
					P_Deduction_ID=1002625;
					P_Deduction_Param_ID = 1002695;
				}
				else
				{
					P_Taxable_Benefit_ID = 1000021;
					P_Deduction_ID=1002631;
					P_Deduction_Param_ID = 1002701;
				}

				P_Period Period = P_Period.get( ctx, PaymentGain.getP_Period_ID(), null);
				P_Employee_Taxable_Benefit Employee_Taxable_Benefit = P_Employee_Taxable_Benefit.get(ctx, Payment.getP_Employee_ID(), P_Taxable_Benefit_ID, null);
					
				Avi.setIsActive(true);
				Avi.setOrigine("FTP");
				Avi.setP_Employee_ID(Payment.getP_Employee_ID());
				Avi.setP_Payment_ID(Payment.getP_Payment_ID());
				Avi.setP_Employee_Taxable_Benefit_ID(Employee_Taxable_Benefit.getP_Employee_Taxable_Benefit_ID() );
				Avi.setP_Period_ID(PaymentGain.getP_Period_ID());
				Avi.setP_Taxable_Benefit_ID(P_Taxable_Benefit_ID);
				Avi.setTaxable_Benefit_Amount(PaymentGain.getAmountCalc());
				Avi.setP_Year_ID(PaymentGain.getP_Year_ID());
				Avi.save();
				
				
				P_Employee_Deduction EmployeeDeduction = P_Employee_Deduction.get(ctx, Payment.getP_Employee_ID(), P_Deduction_ID, Period.getEndDate(), null);
				if ( EmployeeDeduction.getP_Employee_Deduction_ID() <= 0 )
				{
					EmployeeDeduction = new P_Employee_Deduction( Env.getCtx(), -1, null);
					EmployeeDeduction.setP_Deduction_ID( P_Deduction_ID);
					EmployeeDeduction.setIsActive(true);
					EmployeeDeduction.setP_Employee_ID(Payment.getP_Employee_ID());
					EmployeeDeduction.setEffectIn(Period.getStartDate());
					EmployeeDeduction.save();
				}
				
				P_Payment_Deduction Rce = new P_Payment_Deduction( ctx, -1, null );
				Rce.setEmployee_Part(Env.ZERO);
				Rce.setEmployer_Part(PaymentGain.getAmountCalc());
				Rce.setAccumulationAmount(Env.ZERO);
				Rce.setHours_Eligible(Env.ZERO);
				Rce.setIsActive(true);
				Rce.setIsExonerated(false);
				Rce.setOrigine("FTP");
				Rce.setP_Deduction_ID(P_Deduction_ID);
				Rce.setP_Deduction_Param_ID(P_Deduction_Param_ID);
				Rce.setP_Employee_Deduction_ID( EmployeeDeduction.getP_Employee_Deduction_ID());
				Rce.setP_Employee_ID(Payment.getP_Employee_ID());
				Rce.setP_Payment_ID(Payment.getP_Payment_ID());
				Rce.setP_Period_ID(PaymentGain.getP_Period_ID());
				Rce.setP_Year_ID(PaymentGain.getP_Year_ID());
				Rce.setPensionFundCharges(Env.ZERO);
				Rce.setRetrievalAmount(Env.ZERO);
				Rce.setSalary_Eligible(Env.ZERO);
				Rce.save();
				
//				PaymentGain.delete(true);
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

*/
	private GainToAvi()
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);
		int count = 0;
		
		//'69','70'
		String sql = "select p_employee.value, p_employee.name, p_period.name, t.value, t.name, p_payment.p_payment_id, t.p_taxable_benefit_id, employer_part "
                   + " from p_payment_deduction, p_deduction, p_payment, p_employee, p_Period, p_taxable_benefit as t "
                   + " where p_payment_deduction.p_deduction_id = p_deduction.p_deduction_id "
                   + " and p_payment_deduction.p_payment_id = p_payment.p_payment_id "
                   + " and not exists( select 1 from p_payment_taxable_benefit where p_payment.p_payment_id = p_payment_taxable_benefit.p_payment_id " 
                   + " and p_payment_taxable_benefit.p_taxable_benefit_id = p_deduction.p_taxable_benefit_id "
                   + " ) and employer_part != 0 "
                   + " and p_deduction.p_taxable_benefit_id != 0 "
                   + " and p_payment.p_employee_id = p_employee.p_employee_id "
                   + " and p_payment.p_period_id = p_period.p_period_id "
                   + " and p_deduction.p_taxable_benefit_id = t.p_taxable_benefit_id " 
                   + " and t.value != '58' "
                   + " order by p_period.name";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			int i = 0;
			int P_Taxable_Benefit_ID = 0;
			
			while (rs.next())
			{
				P_Taxable_Benefit_ID = rs.getInt( "P_Taxable_Benefit_ID");
				P_Payment Payment = new P_Payment( ctx, rs.getInt( "P_Payment_ID" ), null);
				P_Payment_Taxable_Benefit Avi = new P_Payment_Taxable_Benefit( ctx, -1, null);

				P_Period Period = P_Period.get( ctx, Payment.getP_Period_ID(), null);
				P_Employee_Taxable_Benefit Employee_Taxable_Benefit = P_Employee_Taxable_Benefit.get(ctx, Payment.getP_Employee_ID(), P_Taxable_Benefit_ID, null);
					
				Avi.setIsActive(true);
				Avi.setOrigine("FTP");
				Avi.setP_Employee_ID(Payment.getP_Employee_ID());
				Avi.setP_Payment_ID(Payment.getP_Payment_ID());
				Avi.setP_Employee_Taxable_Benefit_ID(Employee_Taxable_Benefit.getP_Employee_Taxable_Benefit_ID() );
				Avi.setP_Period_ID(Payment.getP_Period_ID());
				Avi.setP_Taxable_Benefit_ID(P_Taxable_Benefit_ID);
				Avi.setTaxable_Benefit_Amount( rs.getBigDecimal("employer_part"));
				Avi.setP_Year_ID(Period.getP_Year_ID());
				Avi.save();
				
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

	
	/**
	 * 	String representation
	 * 	@return string representation
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("Copy_GainToAvi[")
			.append("]");
		return sb.toString();
	}	//	toString



	/**************************************************************************
	 * 	Copy_Time_Sheet 
	 */
	public static void main (String[] args)
	{
		System.out.println("Copy_GainToAvi   $Revision: 1.1 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		int count = 0;
		new GainToAvi();
		count++;
		System.out.println("Generated = " + count);

	}	//	main

}
