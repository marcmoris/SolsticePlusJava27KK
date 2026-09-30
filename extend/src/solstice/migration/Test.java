package solstice.migration;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.compiere.Compiere;
import org.compiere.util.Env;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import solstice.web.Jtpl;

//import com.mashape.unirest.http.*;
import solstice.model.*;
import solstice.process.ImportYTDCalcul_federal_tax;
import solstice.process.ImportYTDCalcul_provincial_tax;
import solstice.process.QuantityAmountPair;
import solstice.utils.PgiUtil;

import org.compiere.model.MOrg;
import org.compiere.model.MActivity;
import org.compiere.*;

public class Test {

	Properties ctx = Env.getCtx();
	/**	Logger							*/
	protected CLogger			log = CLogger.getCLogger (getClass());

	/** Transaction			*/
	private String m_trxName = null; // Trx.createTrxName();

	/**	Big Decimal 0	 */
	static final public java.math.BigDecimal ZERO = new java.math.BigDecimal(0.0);
	//
	// Affectation Principal du paiement pour la période.
	// utiliser pour le calcul des fonds de pensions. ( CARRA )
	//
	P_Assignment Assignment = null;


	
	/**
	 * 	Generate PO Class
	 * 	@param AD_Table_ID table id
	 * 	@param directory directory with \ or / at the end.
	 * 	@param packageName package name
	 * @throws SQLException
	 */
	public Test () 
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);

		P_Period Period = P_Period.getOpenPeriod(ctx, m_trxName);

		String sql = "select P_ASSIGNMENT_param_ID, Taux_horaire_2024 from tmp_aff "
				+ "inner join p_employee on p_employee.p_employee_id = tmp_aff.p_employee_id "
				+ "inner join P_ASSIGNMENT on P_ASSIGNMENT.p_employee_id = p_employee.p_employee_id "
				+ "inner join P_ASSIGNMENT_PARAM on P_ASSIGNMENT_PARAM.P_ASSIGNMENT_id = P_ASSIGNMENT.P_ASSIGNMENT_id "
				+ "where P_ASSIGNMENT.ISACTIVE = 'Y' "
				+ "and P_ASSIGNMENT_param.EFFECTTO is null "
				+ "and p_employee.ad_org_id =1000001 "
				+ "and p_employee.value not in ( '1797', '1801', '1859' ) "
//			 	+ " and p_employee.value = '1859' "
				;
		try
		{

			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{ 
				BigDecimal Annual_Increase = new BigDecimal(52);
				
				P_Assignment_Param assignment_Param = new P_Assignment_Param( Env.getCtx(), rs.getInt("P_ASSIGNMENT_param_ID"), null);
				
				P_Assignment_Param newAssignment_Param = new P_Assignment_Param( Env.getCtx(), -1, null);
				newAssignment_Param.setAD_Org_ID(assignment_Param.getAD_Org_ID());
				
				newAssignment_Param.setCumulatedHours(assignment_Param.getCumulatedHours());
				newAssignment_Param.setDay_Hours(assignment_Param.getDay_Hours());
				
				Timestamp tsDate = TimeUtil.getDay(2024, 1, 1);
		    	
				newAssignment_Param.setEffectIn( tsDate );
				
				newAssignment_Param.setRemuneration_Method(assignment_Param.getRemuneration_Method());
				newAssignment_Param.setWeekly_Hours(assignment_Param.getWeekly_Hours());
//				newAssignment_Param.setAnnual_Salary( newSalary );
				newAssignment_Param.setHourly_Rate( rs.getBigDecimal("Taux_horaire_2024") );
				newAssignment_Param.setAnnual_Salary(rs.getBigDecimal("Taux_horaire_2024").multiply(newAssignment_Param.getWeekly_Hours().multiply( Annual_Increase )).setScale(4, BigDecimal.ROUND_HALF_UP));
				
				newAssignment_Param.setIsActive(true);
				newAssignment_Param.setIsOut_Of_Range(assignment_Param.isOut_Of_Range());
				newAssignment_Param.setIsOut_Of_Rate(assignment_Param.isOut_Of_Rate());
				newAssignment_Param.setOut_Of_Rate_Contractual(assignment_Param.getOut_Of_Rate_Contractual());
				newAssignment_Param.setOut_Of_Rate_Salary(assignment_Param.getOut_Of_Rate_Salary());
				newAssignment_Param.setP_Assignment_ID(assignment_Param.getP_Assignment_ID());
				newAssignment_Param.setP_Collective_Labour_Agr_ID(assignment_Param.getP_Collective_Labour_Agr_ID());
				newAssignment_Param.setP_Job_Title_ID(assignment_Param.getP_Job_Title_ID());
				newAssignment_Param.setP_Occupation_Group_ID(assignment_Param.getP_Occupation_Group_ID());
				newAssignment_Param.setP_Salary_Class_ID(assignment_Param.getP_Salary_Class_ID());
//				newAssignment_Param.setP_Salary_Scale_Detail_ID(assignment_Param.getP_Salary_Scale_Detail_ID() );
//				newAssignment_Param.setP_Salary_Scale_ID(assignment_Param.getP_Salary_Scale_ID());
				newAssignment_Param.setP_Schedule_ID(assignment_Param.getP_Schedule_ID());

				newAssignment_Param.setDaily_Salary(assignment_Param.getDaily_Salary());
				newAssignment_Param.setDay_Hours(assignment_Param.getDay_Hours());

				if(newAssignment_Param.save() == false)
				{
					log.log(Level.SEVERE, "Erreur lors de la sauvegarde du paramètre d'affectation (id de l'affectation = '"+assignment_Param.getP_Assignment_ID() );
				}
				
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
		}
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
		System.out.println("Test   $Revision: 1.3 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		
		int count = 0;
//		new Test();
		count++;
		System.out.println("Generated = " + count);

	}	//	main
	
}
