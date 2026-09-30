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


import solstice.model.P_Assignment;
import solstice.model.P_Assignment_Param;
import solstice.model.P_Column_Adm;
import solstice.model.P_Credits_Movement;
import solstice.model.P_Credits_Param;
import solstice.model.P_Deduction;
import solstice.model.P_Deduction_Param;
import solstice.model.P_Employee;
import solstice.model.P_Employee_Credits;
import solstice.model.P_Employee_Deduction;
import solstice.model.P_Employee_Taxable_Benefit;
import solstice.model.P_Frequency;
import solstice.model.P_Gain;
import solstice.model.P_GainInfo;
import solstice.model.P_Gain_GainInfo;
import solstice.model.P_Gain_Parameter;
import solstice.model.P_Method_Deduction;
import solstice.model.P_Payment;
import solstice.model.P_Payment_Deduction;
import solstice.model.P_Period;
import solstice.model.P_Region;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Time_Sheet_Error;
import solstice.model.X_I_YTD_Deduction;
import solstice.web.Jtpl;
import solstice.model.*;
import solstice.process.Calcul_Credits;
import solstice.process.ImportYTDCalcul_federal_tax;
import solstice.process.ImportYTDCalcul_provincial_tax;
import solstice.process.QuantityAmountPair;

import org.compiere.model.MOrg;
import org.compiere.model.MActivity;
import solstice.model.P_Department;

import org.compiere.*;

public class Maj_Credits {

	Properties ctx = Env.getCtx();
	/**	Logger							*/
	protected CLogger			log = CLogger.getCLogger (getClass());

	/** Transaction			*/
	private String trxName = null; // Trx.createTrxName();
	private P_Employee Employee;
	private P_Period Period;
	private P_Credits_Param creditsParam;
	private P_Employee_Credits employeeCredits;

	// 1001130
	
//	private String P_Credit_ID = " (1001134, 1001136, 1001135, 1001144 )";  // ,  1001144, 1001135, 1001130 
//	private String P_Credit_ID = " (1001133 )";  // anciente 
//	private String P_Credit_ID = " (1001130 )";  // maladie 4000 

	private String P_Credit_ID = " (1101167 )";  // AN01
//	private String P_Credit_ID = " (1001146 )";  // maladie 3320

//	private String P_Credit_ID = " (1001150 )";  // maladie 3330
	
	
//	private String P_Credit_ID = " (1001146, 1001150 )"; 
//	private String P_Credit_ID2 = " (1001146, 1001150 )"; 

	
	/**
	 * 	Generate PO Class
	 * 	@param AD_Table_ID table id
	 * 	@param directory directory with \ or / at the end.
	 * 	@param packageName package name
	 * @throws SQLException
	 */
	public Maj_Credits () 
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);

		// 2008-09
//		Period = new P_Period( getCtx(), 1001950, trxName);

		String sql;
/*		sql = "DELETE FROM P_Credits_Movement where P_Credits_ID in ( 1001130 ) and PMovementOrigin = 'INIT' and PmovementDate <> dbo.to_date( 20080430, '')";
		DB.executeUpdate(sql, null);


		// m.a.j solde
		maj_sld();
		sql = "delete from P_Credits_Movement where P_Credits_ID in " + P_Credit_ID + " and pmovementtype = 'V-'" ;
		DB.executeUpdate(sql, null);

*/

//		sql = "delete from P_Credits_Movement where P_Credits_ID in (1001146, 1001150) and pmovementtype in ( 'BO','BF') and  pmovementdate >= DBO.TO_DATE( 20090531, '')" ;
//		DB.executeUpdate(sql, null);



		sql = "SELECT distinct P_Employee_ID, P_Method_Credits_ID FROM P_Employee_Credits Where IsActive = 'Y' and P_Credits_ID in " + P_Credit_ID
		;
//		+ " AND P_Employee_ID = 1002069 ";
		try
		{

			PreparedStatement pstmt = DB.prepareStatement(sql, trxName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{

				
				Employee = new P_Employee( Env.getCtx(), rs.getInt("P_Employee_ID"), trxName);
				System.out.println("Maj_Credits : " + Employee.getName() + ' ' + Employee.getValue() );

				{
					calcul_Seniority( rs.getInt("P_Method_Credits_ID"));
//					calcul();
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

	/*				
	Employee = new P_Employee( Env.getCtx(), rs.getInt("P_Employee_ID"), trxName);
	employeeCredits = P_Employee_Credits.get(ctx, Employee.getP_Employee_ID(), Credits_ID, Period.getStartDate(), trxName);
	if ( employeeCredits == null )
	{
		employeeCredits = new P_Employee_Credits(Env.getCtx (), -1, trxName);
		employeeCredits.setAD_Org_ID(Employee.getAD_Org_ID());
		employeeCredits.setEffectIn(Employee.getDateSeniority());
		employeeCredits.setIsActive(true);
		employeeCredits.setP_Credits_ID(1001133);
		employeeCredits.setP_Employee_ID(Employee.getP_Employee_ID());
		employeeCredits.setP_Method_Credits_ID(P_Method_Credits_ID);
		employeeCredits.save();
		
	}
	creditsParam = P_Credits_Param.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), P_Method_Credits_ID, Period.getStartDate(), trxName);
*/				
//	calcul_Seniority();

	
/*				Employee = new P_Employee( Env.getCtx(), rs.getInt("P_Employee_ID"), trxName);
	employeeCredits = new P_Employee_Credits(Env.getCtx (), -1, trxName);
	employeeCredits.setAD_Org_ID(0);
	employeeCredits.setEffectIn(Employee.getDateSeniority());
	employeeCredits.setIsActive(true);
	employeeCredits.setP_Credits_ID(1001136);
	employeeCredits.setP_Employee_ID(Employee.getP_Employee_ID());
	employeeCredits.setP_Method_Credits_ID(P_Method_Credits_ID);
	employeeCredits.save();

	Employee = new P_Employee( Env.getCtx(), rs.getInt("P_Employee_ID"), trxName);
	employeeCredits = new P_Employee_Credits(Env.getCtx (), -1, trxName);
	employeeCredits.setAD_Org_ID(0);
	employeeCredits.setEffectIn(Employee.getDateSeniority());
	employeeCredits.setIsActive(true);
	employeeCredits.setP_Credits_ID(1001144);
	employeeCredits.setP_Employee_ID(Employee.getP_Employee_ID());
	employeeCredits.setP_Method_Credits_ID(P_Method_Credits_ID);
	employeeCredits.save();
*/

	
	private void calcul()
	{
		
		String sql = "Select P_Payment_ID from P_Payment Where P_Year_ID in ( 1000096)"  //2009
				   + " and p_period_id = 1001975"
			       + " And P_Employee_ID = " + Employee.getP_Employee_ID()
//			       + " And exists( select 1 from p_payment_gain where p_payment_gain.p_payment_id = p_payment.p_payment_id and p_gain_id in ( select p_gain_id from p_gain where value = '82'))"
			       + " ORDER BY P_Payment_ID "
			;
		try
		{

			PreparedStatement pstmt = DB.prepareStatement(sql, trxName);
			ResultSet rs = pstmt.executeQuery();
			P_Period l_Period;
			while (rs.next())
			{
			    Payment = P_Payment.get( Env.getCtx(), rs.getInt( "P_Payment_ID" ), trxName);	
				TimeSheet = P_Time_Sheet.GetWithPaymentID(ctx, Payment.getP_Payment_ID(), trxName);
				if ( TimeSheet != null && Payment != null)
				{
					calCredits = new Calcul_Credits( TimeSheet, trxName );
				    l_Period = P_Period.get( Env.getCtx(), Payment.getP_Period_ID(), trxName);
				    Timestamp Day = l_Period.getStartDate();

//	 		        if ( TimeSheet.getSheetType().equals( P_Time_Sheet.SHEETTYPE_Regular)  ) 
//						calcul_Close( l_Period.getStartDate(), 1001150);

//	 		        	calCredits.CreditsVariationAnnuelOpen( Day, 1001150 );
//	 		        	calCredits.CreditsEvaluationFixe( Day );

					calcul_gain_credits( Payment, trxName, 1001131);
/*
	 		        if ( TimeSheet.getSheetType().equals( P_Time_Sheet.SHEETTYPE_Regular) && l_Period.getP_Period_ID() == 1001950 ) 
						calcul_Close( l_Period.getStartDate());

	 		        if ( TimeSheet.getSheetType().equals( P_Time_Sheet.SHEETTYPE_Regular) && l_Period.getP_Period_ID() == 1001926 ) 
						calcul_Close( l_Period.getStartDate() );
*/
/*				    if ( l_Period.getP_Period_ID() == 1001926 )
				    	calcul2( 1001926 );
				    if ( l_Period.getP_Period_ID() == 1001950 )
				    	calcul2( 1001950 );
				    	*/ 

					
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

	
	private Calcul_Credits calCredits;
    private P_Time_Sheet  TimeSheet;
    private P_Payment  Payment; 

    private void calcul2( int P_Period_ID)
	{
		
		String sql = "Select P_Payment_ID from P_Payment Where P_Period_ID = " + P_Period_ID  // 1001950 " // >= 1001926"
			       + " And P_Employee_ID = " + Employee.getP_Employee_ID()
			       + " ORDER BY P_Payment_ID "
			;
		try
		{

			PreparedStatement pstmt = DB.prepareStatement(sql, trxName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
			    Payment = P_Payment.get( Env.getCtx(), rs.getInt( "P_Payment_ID" ), trxName);	
				TimeSheet = P_Time_Sheet.GetWithPaymentID(ctx, Payment.getP_Payment_ID(), trxName);
				calCredits = new Calcul_Credits( TimeSheet, trxName );

//ici 		        if ( TimeSheet.getSheetType().equals( P_Time_Sheet.SHEETTYPE_Regular) ) 
//ici					calcul_Close();

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


	
	public boolean calcul_gain_credits( P_Payment Payment , String trxName, int P_Credits_ID) throws Exception
	{
		
	    boolean isError = false;

		String sql = null;
		sql = "Select distinct P_Period_ID From P_Payment_Gain WHERE P_Payment_ID = " + Payment.getP_Payment_ID() 
//		   + " and p_gain_id in ( select p_gain_id from p_gain where value = '82')" 
		   + " Order By P_Period_ID ";

		PreparedStatement pstmt  = null;
		pstmt = DB.prepareStatement (sql,trxName);
		ResultSet rs = pstmt.executeQuery ();
			
		P_Period l_Period;
		while (rs.next ())
		{

		   l_Period = P_Period.get( Env.getCtx(), rs.getInt("P_Period_ID"), trxName);
		   int nbrDays = TimeUtil.getDaysBetween( l_Period.getStartDate() , l_Period.getEndDate() ) + 1;


		   Timestamp Day = l_Period.getStartDate();

		   for (int j = 0; j < nbrDays ; j++)
		   {
		      Day = TimeUtil.addDays( l_Period.getStartDate(), j ); 
		      if ( exists_time( Payment, Day ) )
		    	  {
						log.log (Level.INFO,"Validation - CreditsEvaluationTransaction " + Day.toString());
				        calCredits.CreditsEvaluationTransaction( Payment, Day, P_Credits_ID  );
//				        calCredits.CreditsValidation( Day );
		    	  }
		      }
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
	
		return isError;   
	}

	private boolean exists_time( P_Payment Payment, Timestamp Day ) throws Exception
	{
		
		boolean isFound = false;
		
		String sql = null;
		sql = "Select Top 1 P_Payment_Gain_ID From P_Payment_Gain " 
            + " WHERE P_Payment_ID = " + Payment.getP_Payment_ID()
            + "  AND P_Payment_Gain.Day = " + DB.TO_DATE( Day )
            ;
		
		PreparedStatement pstmt  = null;
		pstmt = DB.prepareStatement (sql,trxName);
		ResultSet rs = pstmt.executeQuery ();
		
		if (rs.next ())
		{
			isFound = true;
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;

		return isFound;

	}

	
	private void maj_sld()
	{

		String sql = "select * from i_credits_movement where p_employee_id is not null ";
		
		P_Credits_Movement CreditsMovement ;
		P_Period Period = new P_Period( getCtx(), 1001949, trxName); //2008-08
		P_Employee Employee;
		P_Employee_Credits employeeCredits;
		P_Assignment_Param assignment_Param;

		BigDecimal hoursForJRH_HRJ = Env.ZERO;

		PreparedStatement pstmt  = null;
		try
		{
			pstmt = DB.prepareStatement (sql,trxName);
			ResultSet rs = pstmt.executeQuery ();


			while (rs.next ())
			{
			
				Employee = P_Employee.get( Env.getCtx(), rs.getInt("P_Employee_ID"), trxName);
				
				assignment_Param = P_Assignment_Param.get( Env.getCtx(), Employee.GetPrincipalAssignment_Param(), trxName);
				if ( assignment_Param.getDay_Hours().compareTo(Env.ZERO) == 0)
					hoursForJRH_HRJ = new BigDecimal(8);
				else
					hoursForJRH_HRJ = assignment_Param.getDay_Hours();

/*				employeeCredits = P_Employee_Credits.get(ctx, rs.getInt("P_Credits_Vacation_ID"), trxName);
				if ( employeeCredits != null  && rs.getInt("P_Credits_Vacation_ID") != 0)
				{
					CreditsMovement = new P_Credits_Movement(Env.getCtx (), -1, trxName ) ;
					CreditsMovement.setP_Credits_ID( employeeCredits.getP_Credits_ID() );
					CreditsMovement.setP_Employee_ID( Employee.getP_Employee_ID());
					CreditsMovement.setPMovementDate( rs.getTimestamp("DateMovement") );
					CreditsMovement.setPMovementOrigin( "INIT");
					CreditsMovement.setPMovementType( "BO" );
					CreditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());
					CreditsMovement.setP_Method_Credits_ID( employeeCredits.getP_Method_Credits_ID() );
					CreditsMovement.setPMovementVariation( rs.getBigDecimal("Vacation").multiply(hoursForJRH_HRJ));
					CreditsMovement.save();
					
				}
*/				

				employeeCredits = P_Employee_Credits.get(ctx, rs.getInt("P_Credits_Sick_ID"), trxName);
				if ( employeeCredits != null && rs.getInt("P_Credits_Sick_ID") != 0)
				{
					CreditsMovement = new P_Credits_Movement(Env.getCtx (), -1, trxName ) ;
					CreditsMovement.setP_Credits_ID( employeeCredits.getP_Credits_ID() );
					CreditsMovement.setP_Employee_ID( Employee.getP_Employee_ID());
					CreditsMovement.setPMovementDate( rs.getTimestamp("DateMovement") );
					CreditsMovement.setPMovementOrigin( "INIT");
					CreditsMovement.setPMovementType( "BO" );
					CreditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());
					CreditsMovement.setP_Method_Credits_ID( employeeCredits.getP_Method_Credits_ID() );
					CreditsMovement.setPMovementVariation( rs.getBigDecimal("Sick").multiply(hoursForJRH_HRJ));
					CreditsMovement.save();
				}
				
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
		}

		
	}

	
	private void calcul_Seniority( int P_Method_Credits_ID)
	{
		Timestamp d = TimeUtil.getDay(2021, 05, 01);
//		Timestamp m = TimeUtil.getDay(2021, 05, 01);
//		Timestamp m = TimeUtil.getDay(TimeUtil.getYear(Employee.getDateSeniority()), 05, 01);
		Timestamp m = Employee.getDateSeniority();
		
/*		if ( m.compareTo(Employee.getDateSeniority()) < 0)
		{
			m = TimeUtil.getDay(TimeUtil.getYear(Employee.getDateSeniority()) + 1, 05, 01);
		}
*/		
		if ( Employee.getDateHired().compareTo(d) >= 0)
			return ;
		
		P_Period Period = P_Period.get(Env.getCtx(), 1102318 , null);  // 2021-18  paie qui inclus le 1er mai
		
	// 1101167 = AN01
		
		employeeCredits = P_Employee_Credits.get(ctx, Employee.getP_Employee_ID(), 1101167, Period.getStartDate(), trxName);
		creditsParam = P_Credits_Param.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), P_Method_Credits_ID, Period.getStartDate(), trxName);

		int nbrYear = TimeUtil.getDaysBetween( m, d ) / 365 ;

		for ( int i=0; i <= nbrYear; i++)
		{
			P_Credits_Movement CreditsMovement = new P_Credits_Movement(Env.getCtx (), -1, trxName ) ;

			CreditsMovement.setP_Credits_ID( employeeCredits.getP_Credits_ID() );
			CreditsMovement.setP_Employee_ID( Employee.getP_Employee_ID());
			CreditsMovement.setPMovementDate( TimeUtil.addYear(m, i ) );
			CreditsMovement.setPMovementOrigin( "FTP");
			CreditsMovement.setPMovementType( "V+" );
			CreditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());
			CreditsMovement.setP_Method_Credits_ID( creditsParam.getP_Method_Credits_ID() );
			
//			CreditsMovement.setDescription( " ");

			CreditsMovement.setPMovementVariation( Env.ONE );
//			CreditsMovement.setPMovementHour( CreditsMovement.getPMovementHour().add( NbrHour ) );
//			CreditsMovement.setP_Payment_ID(  );
			CreditsMovement.save();
			
		}
		
	}


	private void calcul_Close( Timestamp d, int P_Credit_ID )
	{
//		Timestamp d = TimeUtil.getDay(2008, 05, 01);

   	    calCredits.CreditsVariationAnnuelClose( d, P_Credit_ID );

	}

	
    private Properties getCtx()
    {
    	return Env.getCtx();
    }

	/**************************************************************************
	 * 	Migration des données 
	 */
	public static void main (String[] args)
	{
		System.out.println("Maj_Credits   $Revision: 1.1 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		
		int count = 0;
		
		new Maj_Credits();
		count++;
		System.out.println("Generated = " + count);

	}	//	main
	
}
