package solstice.migration;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.Compiere;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Gain;
import solstice.model.P_Payment;
import solstice.model.P_Payment_Deduction;
import solstice.model.P_Period;
import solstice.model.P_Time_Sheet;

public class MAJ_Payment_Deduction {

	Properties ctx = Env.getCtx();
	/**	Logger							*/
	protected CLogger			log = CLogger.getCLogger (getClass());

	/** Transaction			*/
	private String trxName = null;

	/**	Big Decimal 0	 */
	static final public java.math.BigDecimal ZERO = new java.math.BigDecimal(0.0);

	/** Transaction			*/
	private String m_trxName = null; // Trx.createTrxName();

	int P_Period25_ID = 1002041;
	
	public int create_payment()
	{
		int count = 0;
		
		String sql = "select AD_ORG_ID, P_Employee_ID, P_Employer_ID, Taxation_Region_ID,  max(P_Payment_ID) P_Payment_ID, max(P_Time_Sheet_ID) P_Time_Sheet_ID From P_Time_Sheet WHERE P_Period_ID in ( 1002037, 1002038, 1002039, 1002040 ) and exists ( select 1 from P_Payment_Deduction where P_Payment_Deduction.P_payment_id = P_Time_Sheet.P_payment_ID and P_Deduction_ID in ( select p_deduction_id from p_deduction where value in ( '50','64','68','56' )) ) group by AD_ORG_ID, P_Employee_ID, P_Employer_ID, Taxation_Region_ID ";  
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				P_Time_Sheet TimeSheet = P_Time_Sheet.copyFrom(getCtx(), rs.getInt( "P_Time_Sheet_ID"), P_Period25_ID, m_trxName );
				P_Payment Payment = P_Payment.copyFrom(getCtx(), rs.getInt( "P_Payment_ID"), TimeSheet, m_trxName );
				
				create_payment_deduction( Payment );
				
				count++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"Migration P_Payment- " , e);
		}

		return count;

	}

	public void create_payment_deduction( P_Payment Payment )
	{
	
		String sql = "select * from P_Payment_Deduction "
				   + " Inner join P_Payment on P_Payment.P_Payment_ID = P_Payment_Deduction.P_Payment_ID "	
                   + " where  P_Payment.P_Period_ID in ( 1002037, 1002038, 1002039, 1002040 ) "
                   + " and p_deduction_id in ( select p_deduction_id from p_deduction where value in ( '50','64','68','56' ) ) "
                   + " and P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID()
		 		   + " and P_Payment.AD_ORG_ID  = " + Payment.getAD_Org_ID()
		 		   + " and P_Payment.P_Employer_ID = " + Payment.getP_Employer_ID()
		 		   + " and P_Payment.Taxation_Region_ID = " + Payment.getTaxation_Region_ID()
                   ;
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				P_Payment_Deduction tab = new P_Payment_Deduction( ctx, -1, m_trxName);
				tab.setP_Deduction_ID( rs.getInt("P_Deduction_ID"));
				tab.setP_Employee_ID( rs.getInt("P_Employee_ID"));
				tab.setP_Payment_ID( Payment.getP_Payment_ID() );
				tab.setP_Period_ID(  rs.getInt("P_Period_ID"));
				tab.setP_Year_ID( rs.getInt("P_Year_ID" ));
				tab.setAccumulationAmount( ZERO );
				tab.setEmployee_Part( rs.getBigDecimal("Employee_Part").multiply(new BigDecimal( -1 )));
				tab.setEmployer_Part( rs.getBigDecimal("Employer_Part").multiply(new BigDecimal( -1 )));
				tab.setHours_Eligible( rs.getBigDecimal("Hours_Eligible").multiply(new BigDecimal( -1 )));
				tab.setRetrievalAmount( ZERO );
				tab.setSalary_Eligible( rs.getBigDecimal("Salary_Eligible").multiply(new BigDecimal( -1 )));
				tab.setIsExonerated( rs.getBoolean("IsExonerated"));
	           	tab.setIsActive( true);
	           	tab.setOrigine("AJT");
	           	tab.save();
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"Migration P_payment_deduction - " , e);
		}
		
	}
	

	
	/**
	 * 	Generate PO Class
	 * 	@param AD_Table_ID table id
	 * 	@param directory directory with \ or / at the end.
	 * 	@param packageName package name
	 * @throws SQLException
	 */
	public MAJ_Payment_Deduction () 
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);
		
		int count = create_payment();

		System.out.println("Generated = " + count);

	}
	
	private Properties getCtx()
	{
	  	return Env.getCtx();
	}

	   
	/**************************************************************************
	 * 	Migration des données pour la SIQ
	 */
	public static void main (String[] args)
	{
		System.out.println("Test   $Revision: 1.4 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		
		int count = 0;
		new MAJ_Payment_Deduction();
		count++;
		System.out.println("Generated = " + count);

	}	//	main


}
