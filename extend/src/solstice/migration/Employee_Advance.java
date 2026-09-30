package solstice.migration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.Compiere;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Employee_Advance;
import solstice.model.P_Employee_Advance_Summary;
import solstice.model.P_Employee_Advance_Detail;

public class Employee_Advance {


	/** Transaction			*/
	private String m_trxName = null; // Trx.createTrxName();

	Properties ctx = Env.getCtx();

	public Employee_Advance () 
	{
		Env.setContext( ctx, "#AD_Client_ID", 11);
		Env.setContext( ctx, "#AD_Org_ID", 11);

		int count = p_employee_advance();
		System.out.println("created = " + count);
		 
	}

	/**************************************************************************
	 * 	Migration des données pour la SIQ
	 */
	public static void main (String[] args)
	{
		System.out.println("Migration   $Revision: 1.1 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);

		new Employee_Advance();

		
	}	//	main

		

	private int p_employee_advance()
	{
		int count = 0;
		
		String sql = "SELECT P_Payment.P_Employee_ID, P_GAIN.P_ADVANCE_ID "
                   + "  FROM P_PAYMENT_GAIN, P_GAIN, P_PAYMENT "
                   + "    WHERE P_PAYMENT_GAIN.P_GAIN_ID = P_GAIN.P_GAIN_ID "
                   + "    AND   P_PAYMENT_GAIN.P_PAYMENT_ID = P_PAYMENT.P_PAYMENT_ID "
                   + "    AND P_GAIN.P_ADVANCE_ID is not null "
                   + "     group by P_Payment.P_Employee_ID, P_GAIN.P_ADVANCE_ID";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				P_Employee_Advance tab = new P_Employee_Advance( ctx, -1, m_trxName);
				tab.setP_Employee_ID( rs.getInt("P_Employee_ID"));
				tab.setP_Advance_ID( rs.getInt("P_Advance_ID") );
               	tab.setIsActive( true);
				tab.save();

				count++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("Migration p_employee_advance- " + e);
		}

		count = count + p_employee_advance_summary( );
		count = count + p_employee_advance_detail(  );
		count = count + p_employee_advance_refund( );
		return count;
		
	}

	private int p_employee_advance_summary()
	{
		int count = 0;
		
		String sql = "SELECT P_Payment.P_Employee_ID, P_GAIN.P_ADVANCE_ID, isnull(P_GAIN.P_SpecialRate_ID,0) P_SpecialRate_ID, P_Employee_Advance_ID "
                   + " FROM P_PAYMENT_GAIN, P_GAIN, P_PAYMENT, P_Employee_Advance "
                   + " WHERE P_PAYMENT_GAIN.P_GAIN_ID = P_GAIN.P_GAIN_ID "
                   + " AND   P_PAYMENT_GAIN.P_PAYMENT_ID = P_PAYMENT.P_PAYMENT_ID "
                   + " AND   P_Employee_Advance.P_Employee_ID = P_Payment.P_Employee_ID "
                   + " AND   P_Employee_Advance.P_Advance_ID = P_GAIN.P_ADVANCE_ID "
                   + " AND P_GAIN.P_ADVANCE_ID is not null "
                   + " group by P_Payment.P_Employee_ID, P_GAIN.P_ADVANCE_ID, P_GAIN.P_SpecialRate_ID, P_Employee_Advance_ID"
;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				P_Employee_Advance_Summary tab = new P_Employee_Advance_Summary( ctx, -1, m_trxName);
				tab.setP_Employee_ID( rs.getInt("P_Employee_ID"));
				tab.setP_Advance_ID( rs.getInt("P_Advance_ID") );
				if ( rs.getInt("P_SpecialRate_ID") != 0 )
					tab.setP_SpecialRate_ID(rs.getInt("P_SpecialRate_ID"));
				tab.setP_Employee_Advance_ID( rs.getInt("P_Employee_Advance_ID"));
               	tab.setIsActive( true);
				tab.save();

				count++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("Migration p_employee_advance_summary- " + e);
		}

		return count;
		
	}

	
	private int p_employee_advance_detail()
	{
		int count = 0;
		
		String sql = "SELECT P_Payment.P_YEAR_ID, P_Payment.P_PERIOD_ID, P_Payment.P_Payment_ID, P_Payment.P_Employee_ID, P_GAIN.P_ADVANCE_ID, isnull(P_GAIN.P_SpecialRate_ID,0) P_SpecialRate_ID, P_Employee_Advance_ID, P_Employee_Advance_Summary_ID, [day], sum( amountcalc ) amountcalc " 
		           + " FROM P_PAYMENT_GAIN, P_GAIN, P_PAYMENT, P_Employee_Advance_Summary "
		           + " 	WHERE P_PAYMENT_GAIN.P_GAIN_ID = P_GAIN.P_GAIN_ID "
		           + "  AND   P_PAYMENT_GAIN.P_PAYMENT_ID = P_PAYMENT.P_PAYMENT_ID "
		           + "	AND   P_Employee_Advance_Summary.P_Employee_ID = P_Payment.P_Employee_ID "
		           + "	AND   P_Employee_Advance_Summary.P_Advance_ID = P_GAIN.P_ADVANCE_ID "
		           + "	AND   isnull(P_Employee_Advance_Summary.P_SpecialRate_ID, 0) = isnull( P_GAIN.P_SpecialRate_ID, 0) "
		           + "	AND P_GAIN.P_ADVANCE_ID is not null "
		           + "	group by P_Payment.P_YEAR_ID, P_Payment.P_PERIOD_ID, P_Payment.P_Payment_ID, P_Payment.P_Employee_ID, P_GAIN.P_ADVANCE_ID, isnull(P_GAIN.P_SpecialRate_ID,0), P_Employee_Advance_ID, P_Employee_Advance_Summary_ID, [day] "
        ;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				P_Employee_Advance_Detail tab = new P_Employee_Advance_Detail( ctx, -1, m_trxName);
				tab.setP_Employee_ID( rs.getInt("P_Employee_ID"));
				tab.setP_Advance_ID( rs.getInt("P_Advance_ID") );
				if ( rs.getInt("P_SpecialRate_ID") != 0 )
					tab.setP_SpecialRate_ID(rs.getInt("P_SpecialRate_ID"));
				tab.setP_Employee_Advance_ID( rs.getInt("P_Employee_Advance_ID"));
				tab.setP_Employee_Advance_Summary_ID( rs.getInt("P_Employee_Advance_Summary_ID"));
				tab.setAdvanceDate( rs.getTimestamp("Day"));
				tab.setAdvanceAmount( rs.getBigDecimal("amountcalc"));
				tab.setP_Payment_ID( rs.getInt("P_Payment_ID"));
				tab.setP_Period_ID( rs.getInt("P_Period_ID"));
				tab.setP_Year_ID( rs.getInt("P_Year_ID"));
               	tab.setIsActive( true);
				tab.setIsRefund( false);
				tab.save();

				count++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("Migration p_employee_advance detail- " + e);
		}

		return count;
		
	}


	private int p_employee_advance_refund()
	{
		int count = 0;
		
		String sql = "SELECT P_Payment.P_YEAR_ID, P_Payment.P_PERIOD_ID, P_Payment.P_Payment_ID, P_Payment.P_Employee_ID, P_DEDUCTION_PARAM.P_ADVANCE_ID, 0 P_SpecialRate_ID, P_Employee_Advance_ID, P_Employee_Advance_Summary_ID, P_Period.enddate [day], 0 - sum( employee_part ) amount "
                   + " FROM P_PAYMENT_DEDUCTION, P_DEDUCTION, P_PAYMENT, P_Employee_Advance_Summary, P_Period, P_Deduction_Param "
                   + " WHERE P_PAYMENT_DEDUCTION.P_DEDUCTION_ID = P_DEDUCTION.P_DEDUCTION_ID "
                   + " AND   P_PAYMENT_DEDUCTION.P_PAYMENT_ID = P_PAYMENT.P_PAYMENT_ID "
                   + " AND   P_Employee_Advance_Summary.P_Employee_ID = P_Payment.P_Employee_ID "
                   + " AND   P_Employee_Advance_Summary.P_Advance_ID = P_DEDUCTION_Param.P_ADVANCE_ID "
                   + " AND   isnull(P_Employee_Advance_Summary.P_SpecialRate_ID, 0) = 0 "
                   + " AND P_DEDUCTION_Param.P_ADVANCE_ID is not null "
                   + " AND P_Payment.P_Period_ID = P_Period.P_Period_ID "
                   + " AND P_Deduction_Param.P_Deduction_ID = P_Deduction.P_Deduction_ID "
                   + " AND P_Deduction_Param.Effectin = ( select max( a.EffectIN ) from P_Deduction_Param a where a.p_deduction_id = P_Deduction_Param.P_Deduction_ID ) "
                   + " group by P_Payment.P_YEAR_ID, P_Payment.P_PERIOD_ID, P_Payment.P_Payment_ID, P_Payment.P_Employee_ID, P_DEDUCTION_PARAM.P_ADVANCE_ID, P_Employee_Advance_ID, P_Employee_Advance_Summary_ID, P_Period.enddate "
        ;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				P_Employee_Advance_Detail tab = new P_Employee_Advance_Detail( ctx, -1, m_trxName);
				tab.setP_Employee_ID( rs.getInt("P_Employee_ID"));
				tab.setP_Advance_ID( rs.getInt("P_Advance_ID") );
				if ( rs.getInt("P_SpecialRate_ID") != 0 )
					tab.setP_SpecialRate_ID(rs.getInt("P_SpecialRate_ID"));
				tab.setP_Employee_Advance_ID( rs.getInt("P_Employee_Advance_ID"));
				tab.setP_Employee_Advance_Summary_ID( rs.getInt("P_Employee_Advance_Summary_ID"));
				tab.setAdvanceDate( rs.getTimestamp("Day"));
				tab.setAdvanceAmount( rs.getBigDecimal("Amount"));
				tab.setP_Payment_ID( rs.getInt("P_Payment_ID"));
				tab.setP_Period_ID( rs.getInt("P_Period_ID"));
				tab.setP_Year_ID( rs.getInt("P_Year_ID"));
				tab.setIsRefund( true);
               	tab.setIsActive( true);
				tab.save();

				count++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("Migration p_employee_advance detail- " + e);
		}

		return count;
		
	}

}


