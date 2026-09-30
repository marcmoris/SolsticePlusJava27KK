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
package solstice.migration;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Properties;

import org.compiere.Compiere;
import org.compiere.util.Env;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;


import solstice.model.P_Payment;
import solstice.model.P_Payment_Deduction;


public class MAJ_RRQ {

	Properties ctx = Env.getCtx();
	/**	Logger							*/
	protected CLogger			log = CLogger.getCLogger (getClass());

	/** Transaction			*/
	private String trxName = null;

	/**	Big Decimal 0	 */
	static final public java.math.BigDecimal ZERO = new java.math.BigDecimal(0.0);

	
	
	/**
	 * 	Generate PO Class
	 * 	@param AD_Table_ID table id
	 * 	@param directory directory with \ or / at the end.
	 * 	@param packageName package name
	 * @throws SQLException
	 */
	public MAJ_RRQ () 
	{
		int count = 0;
		String sql = "SELECT * FROM P_PAYMENT "
                   + " Where not exists ( select 1 from P_PAYMENT_DEDUCTION where P_PAYMENT_DEDUCTION.P_PAYMENT_ID = P_PAYMENT.P_PAYMENT_ID "
                   + " and P_PAYMENT_DEDUCTION.P_DEDUCTION_ID IN ( 1002712 , 1002570) " 
                   + " ) and P_YEAR_ID = 1000098 "
                   + " and 48300 != (select sum(salary_eligible) from p_payment_deduction where p_payment_deduction.p_employee_id = p_payment.p_employee_id  " 
                   + " and P_PAYMENT_DEDUCTION.P_DEDUCTION_ID IN ( 1002712 , 1002570) and p_payment_deduction.P_YEAR_ID = 1000098  ) "
                  ;

		try
		{

			PreparedStatement pstmt = DB.prepareStatement(sql, trxName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				P_Payment Payment = P_Payment.get(getCtx(), rs.getInt("P_Payment_ID"), null);
				P_Payment_Deduction PaymentDeduction = new P_Payment_Deduction( getCtx(), -1 , null);
				PaymentDeduction.setAD_Org_ID( Payment.getAD_Org_ID());
				if ( Payment.getTaxation_Region_ID() == 166)
					PaymentDeduction.setP_Deduction_ID( 1002712 );
				else
					PaymentDeduction.setP_Deduction_ID( 1002570 );
				PaymentDeduction.setP_Employee_ID(Payment.getP_Employee_ID());
				PaymentDeduction.setP_Payment_ID(Payment.getP_Payment_ID());
				PaymentDeduction.setEmployee_Part( Env.ZERO);
				PaymentDeduction.setEmployer_Part( Env.ZERO);
				PaymentDeduction.setRetrievalAmount( Env.ZERO );
				PaymentDeduction.setAccumulationAmount( Env.ZERO );
				PaymentDeduction.setP_Period_ID(Payment.getP_Period_ID());
				PaymentDeduction.setP_Year_ID(Payment.getP_Year_ID());

				if ( Payment.getTaxation_Region_ID() == 166)
					PaymentDeduction.setSalary_Eligible( get_salaire_eligible( Payment.getP_Payment_ID(), 102) );
				else
					PaymentDeduction.setSalary_Eligible( get_salaire_eligible( Payment.getP_Payment_ID(), 1000007) );
				
				PaymentDeduction.setOrigine("IMP");
				if ( PaymentDeduction.getSalary_Eligible().compareTo( Env.ZERO) != 0)
				{
					PaymentDeduction.save();
					count++;
				}
			}
			rs.close();
			pstmt.close();
			System.out.println("Generated = " + count);

		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
		}
	}
	
	//
	// Read Cumulative information about a deduction for this Year..
	// 
	private BigDecimal get_salaire_eligible( int P_Payment_ID, int P_Column_Adm_ID) throws Exception
	{
		BigDecimal cumSalaryAdmissible =   ZERO;
		String query = null;
		query = "Select [dbo].[fn_getsalaire_eligible_01] ( " + P_Payment_ID + ", " + P_Column_Adm_ID + " )"
			;

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			cumSalaryAdmissible =   rs.getBigDecimal(1);
		}
		else
		{
			cumSalaryAdmissible =   ZERO;
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
		
		return cumSalaryAdmissible;
			
	}	


    private Properties getCtx()
    {
    	return Env.getCtx();
    }

	/**************************************************************************
	 * 	MAJ Donnée RRQ
	 */
	public static void main (String[] args)
	{
		System.out.println("MAJ_RRQ   $Revision: 1.4 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		
		int count = 0;
		new MAJ_RRQ();
		count++;
		System.out.println("Generated = " + count);

	}	//	main
	
}
