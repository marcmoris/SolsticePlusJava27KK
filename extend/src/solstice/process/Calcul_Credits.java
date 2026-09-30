

/*
 * Created on 2005-04-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;


import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.CLogger;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;

import solstice.model.P_Assignment;
import solstice.model.P_Assignment_Param;
import solstice.model.P_Credits;
import solstice.model.P_Credits_Alert;
import solstice.model.P_Credits_Bound;
import solstice.model.P_Credits_Family_Mth_Eval;
import solstice.model.P_Credits_Family_Mth_Var;
import solstice.model.P_Credits_Movement;
import solstice.model.P_Credits_Mth_Evaluation;
import solstice.model.P_Credits_Mth_Variation;
import solstice.model.P_Credits_Param;
import solstice.model.P_Employee;
import solstice.model.P_Employee_Credits;
import solstice.model.P_Gain;
import solstice.model.P_Gain_Credits;
import solstice.model.P_Gain_Parameter;
import solstice.model.P_Payment;
import solstice.model.P_Payment_Gain;
import solstice.model.P_Period;
import solstice.model.P_Year;
import solstice.utils.TimeUtilSolstice;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Time_Sheet_Error;
import solstice.model.P_Method_Credits;
import solstice.model.P_UOM;
import solstice.model.P_Booklet;
import solstice.model.P_Payment_Group;

/**
 * @author marmor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Calcul_Credits
{

	public CLogger			log = CLogger.getCLogger (getClass());
	private P_Employee 		m_Employee;
	private P_Time_Sheet 	m_TimeSheet;
	private P_Period 		m_Period;
	private P_Payment 		m_Payment;
	private String 			m_trxName;
	private int 			m_iP_Method_Credits_ID = -1;
	private Timestamp		m_eval_timestamp_result = null;
	
	//seulement utilisé par la varification des banques web
	private String			m_strLastAlert = "";

	//
	//   
	//
	public Calcul_Credits( P_Time_Sheet time, String trxName  )
	{
    	setTrxName( trxName);

    	m_TimeSheet = time;
	    m_Employee  = P_Employee.get( Env.getCtx(), m_TimeSheet.getP_Employee_ID(), getTrxName());
	    m_Payment   = P_Payment.get( Env.getCtx(), m_TimeSheet.getP_Payment_ID(), getTrxName());
	    m_Period    = P_Period.get( Env.getCtx(),  m_TimeSheet.getP_Period_ID(), getTrxName());	    
	}
	public Calcul_Credits( String trxName  )
	{
    	setTrxName( trxName);

    	m_TimeSheet = null;
	    m_Employee  = null;
	    m_Payment   = null;
	    m_Period    = null;	
	}


	public boolean CreditsEval( P_Credits credits, P_Credits_Mth_Evaluation mthEva,  P_Credits_Param creditsParam, Timestamp employeeCreditEffectTo, Timestamp Day )
	{
		//premiere journee du mois (utilise pour calculer la derniere)
		Calendar start = Calendar.getInstance();
		//start.set( m_Period.getStartDate().getYear(), m_Period.getStartDate().getMonth(), 1 );
		start.setTimeInMillis( m_Period.getStartDate().getTime());
		//on set a la premier journee du mois
		start.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = start.getTime();
		
		Timestamp tsStartDate;
		Timestamp tsEndDate;
		
		//si la date d'embauche est plus grande que la date du jours traiter...
		// 2008-08-06
//		if(m_Employee.getDateHired().compareTo( Day ) >= 0)
		if(m_Employee.getDateHired().compareTo( Day ) > 0)
		{
			return false;
		}
		
		//si la date de mise est pied est (non null et) est plus petite que
		// que la date traiter...
		if( m_Employee.getDateLayoff() != null &&
		    m_Employee.getDateLayoff().compareTo(m_Period.getStartDate() ) >= 0  &&	
			m_Employee.getDateLayoff().compareTo( Day ) < 0)
		{
			return false;
		}
		
		//si la date de fin (ajusté selon précédent)
		//est plus grande que la date de fin de relation employee banque...
		if(employeeCreditEffectTo != null &&
				employeeCreditEffectTo.compareTo( Day ) < 0)
		{
			return false;
		}
		
		tsStartDate = m_Period.getStartDate();
		tsEndDate = m_Period.getEndDate();
		
		if ( mthEva.getValue().compareTo("002" ) == 0 ) // one time by year
		{
	
		    Timestamp creditsDate = creditsParam.getCreditsDate( m_Period, m_Employee, getTrxName());
		    Timestamp firstDayLastYear = (Timestamp)creditsDate.clone();
		    Timestamp lastDayLastYear = (Timestamp)creditsDate.clone();
		    //la journée de début d'année (financiere) est la date d'Anniversaire de la banque, mais l'an passée
		    firstDayLastYear.setYear(Day.getYear() -1);
		    //la derniere journee del'annee est la journne d'anniversaire de la banque -1
		    lastDayLastYear = TimeUtil.addDays( lastDayLastYear, -1);
//		    lastDayLastYear.setDate(lastDayLastYear.getDate() -1);
	
// 2008-04-09		    
//			 if ( creditsDate.compareTo( Day ) == 0  )
			 if (  TimeUtil.isSameDay(creditsDate, Day) )
			 {
				 String tempMonth = credits.getCreditsMonth();
				 if(tempMonth.length() == 2 && tempMonth.substring(0,1).equals("0"))
				 {
					 tempMonth = tempMonth.substring(1,2);
				 }
				 Integer iTempMonth = null;
				 try{
				 iTempMonth = new Integer(tempMonth);
				 }
				 catch (NumberFormatException e)
				 {
					 log.log(Level.SEVERE, e.toString());
				 }
				 startDate.setDate(credits.getCreditsDay());
				 //month = 0 - 11
				 startDate.setMonth(iTempMonth.intValue() - 1);
				 m_eval_timestamp_result =  new Timestamp(startDate.getTime());
	
				if(isGainEvalPresent(m_Employee.getP_Employee_ID(), firstDayLastYear, lastDayLastYear, credits.getP_Credits_ID(), creditsParam) == false)
				{
					return false;
				}

			 	return true;
			 }
			 
			//log.log( Level.SEVERE , "Credits : " + Credits.getName() + " Date " + CreditsDate + " Between " +  m_Period.getStartDate() + " and " + m_Period.getEndDate() );  
			 
		}
		
		
		if ( mthEva.getValue().compareTo("003" ) == 0 ) // one time by month
		{
			Timestamp CreditsDate = creditsParam.getCreditsDate( m_Period, m_Employee, getTrxName());
	
			if ( CreditsDate.compareTo( Day ) == 0 )
			{
				tsStartDate = TimeUtil.addDays(Day, -31 );
				if(isGainEvalPresent(m_Employee.getP_Employee_ID(), tsStartDate, Day, credits.getP_Credits_ID(), creditsParam) == false)
				{
					return false;
				}

			    return true;
			}
		}
	
		if ( mthEva.getValue().compareTo("004" ) == 0 ) // end of the month
		{
			
			Calendar t = Calendar.getInstance();
			t.setTimeInMillis( m_Period.getStartDate().getTime());
			t.set(Calendar.DAY_OF_MONTH, t.getActualMaximum(Calendar.DAY_OF_MONTH));
			//t.set( CreditsParam.getCreditsDate( m_Period, Employee, getTrxName()).getYear(), CreditsParam.getCreditsDate( m_Period, Employee, getTrxName()).getMonth(), t.getMaximum( Calendar.DAY_OF_MONTH ) );
	
			Timestamp firstDayInMonth = new Timestamp(t.getTimeInMillis());
			firstDayInMonth.setDate(1);
			
			Date date = t.getTime();
			Timestamp CreditsDate = new Timestamp( date.getTime() ) ;
	
			if ( CreditsDate.compareTo( Day ) == 0 )
			{
				if(isGainEvalPresent(m_Employee.getP_Employee_ID(), firstDayInMonth, CreditsDate, credits.getP_Credits_ID(), creditsParam) == false)
				{
					return false;
				}
				m_eval_timestamp_result = CreditsDate;
			    return true;
			}
		}
	
		if ( mthEva.getValue().compareTo( "005" ) == 0 ) // each m_Period.
		{
			if(isGainEvalPresent(m_Employee.getP_Employee_ID(), tsStartDate, tsEndDate, credits.getP_Credits_ID(), creditsParam) == false)
			{
				return false;
			}

			m_eval_timestamp_result =  Day;
			return true;					
		}
	
		return false;
	}
	//
	// Évaluation fixe de banque
	//
	private boolean isCreditsEvaluationFixe = true; 
	
	
	public void CreditsEvaluationFixe( Timestamp Day ) 
	{
		
//		if ( isCreditsEvaluationFixe == false)
//			return;
		
		String sql = null;
		sql = "Select P_Employee_Credits_ID, P_Method_Credits_ID, P_Credits.P_Credits_ID From P_Employee_Credits, P_Credits ";
		sql +="  Where P_Employee_Credits.IsActive = 'Y' ";
		sql +="    And P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID " ;
		sql +="    And P_Employee_Credits.P_Employee_ID = " + m_Employee.getP_Employee_ID();
		sql +="    And P_Credits.IsActive = 'Y' ";
		sql +="    And P_Credits.isVirtualAccrualBank = 'N' ";
		sql +="  ORDER BY P_Credits.Priority ";

		
		P_Employee_Credits employeeCredits;
		P_Credits       credits;
		P_Credits_Param creditsParam;
//		P_Method_Credits method_Credits;
		
		//log.log( Level.SEVERE,"Gross Calcul - CreditsEvaluationFixe " + sql );

		boolean creditsEval = false;
		
		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, getTrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				creditsEval = false;
				
//				method_Credits	= P_Method_Credits.get(Env.getCtx(), rs.getInt(2), getTrxName());
//				m_iP_Method_Credits_ID = method_Credits.getP_Method_Credits_ID();
				m_iP_Method_Credits_ID = rs.getInt("P_Method_Credits_ID");
				
				employeeCredits = P_Employee_Credits.get(Env.getCtx (), rs.getInt("P_Employee_Credits_ID"), getTrxName());
				credits         = P_Credits.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), getTrxName() );
				//method_Credits.getP_Method_Credits_ID()
				creditsParam    = P_Credits_Param.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), m_iP_Method_Credits_ID, Day, getTrxName() );

				
				if ( creditsParam != null && creditsParam.getP_Credits_Param_ID() != 0 )
				{
					P_Credits_Family_Mth_Eval famEva = P_Credits_Family_Mth_Eval.get(Env.getCtx (), creditsParam.getP_Credits_Family_Mth_Eval_ID(), getTrxName());
					P_Credits_Mth_Evaluation  mthEva = P_Credits_Mth_Evaluation.get (Env.getCtx (), famEva.getP_Credits_Mth_Evaluation_ID(), getTrxName() );
					
					//
					// Détermine si c'est le temps d'évaluer une banque
					//
					
					//la date de l'Attribution
					m_eval_timestamp_result = null;
					creditsEval = CreditsEval( credits, mthEva, creditsParam, employeeCredits.getEffectTo(), Day );
	
					if ( creditsEval )
					{
						BigDecimal creditsVariation = creditsParam.getCreditsVariation();

						Timestamp tsEndDate;
						if(m_Employee.getDateLayoff() != null && m_Employee.getDateLayoff().compareTo( Day ) <= 0)
							tsEndDate = m_Employee.getDateLayoff();
						else
							tsEndDate = Day;
						CreditsVariation( employeeCredits, tsEndDate, creditsVariation, "V+", false, 0);
					}
				}

			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
            P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName());
            log.log( Level.SEVERE, "CalculCreditFixe - " + sql, e);
		}
		
	}
	
	
	
	
	// 2009.06.18 -- Génération d'un autre gain de paiement.
	public void CreditsPaymentOthersBank( P_Payment payment)
	{
		String sql;
		sql = "Select P_Gain_Credits_ID, P_Gain_Credits.P_Credits_ID, ";
		sql +=" P_Employee_Credits.P_Employee_Credits_ID, P_Gain_Parameter.P_Gain_ID, ";
		sql +=" P_Employee_Credits.P_Method_Credits_ID, P_Gain_Credits.P_Collective_Labour_Agr_ID, P_Payment_Gain.P_Collective_Labour_Agr_ID P_Collective_Labour_Agr_ID2, ";
		sql +=" P_Payment_Gain.Day, isnull( P_Payment_Gain.QuantityCalc, 0 ) QuantityCalc, isnull( P_Payment_Gain.Quantity, 0 ) Quantity, isnull( P_Payment_Gain.AmountCalc, 0 )  AmountCalc, P_Payment_Gain.P_Assignment_Param_ID ";
		sql +=" From P_Employee_Credits, P_Gain_Credits, P_Credits, P_Gain_Parameter, P_Payment_Gain ";
		sql +="  Where P_Gain_Credits.IsActive = 'Y' ";
		sql +="    And P_Credits.IsActive = 'Y' ";
		sql +="    And P_Gain_Credits.P_Credits_ID = P_Credits.P_Credits_ID " ;
		sql +="    AND P_Gain_Parameter.P_Gain_Parameter_ID = P_Gain_Credits.P_Gain_Parameter_ID";
		sql +="    And P_Gain_Parameter.P_Gain_ID = P_Payment_Gain.P_Gain_ID" ;
		sql +="	   And P_Payment_Gain.P_Payment_ID = " + payment.getP_Payment_ID();
		sql +="    And P_Employee_Credits.P_Employee_ID = " + m_Employee.getP_Employee_ID();
		sql +="    And P_Employee_Credits.IsActive = 'Y' ";
		sql +="    And P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID ";
		sql +="    And (P_Employee_Credits.EffectTo is null OR P_Employee_Credits.EffectTo >= P_Payment_Gain.Day ) ";
		sql +="    AND P_Gain_Credits.Credits_Function = 'P' ";
//2025-05-05
		sql +="    And NOT EXISTS( SELECT 1 FROM P_LONGTERMLEAVE_CREDITS WHERE P_LONGTERMLEAVE_CREDITS.P_Credits_ID = P_Credits.P_Credits_ID "
				+ " AND P_LONGTERMLEAVE_CREDITS.P_LONGTERMLEAVE_ID = "	+ m_Employee.getP_LongTermLeave_ID() + " )";

		
		//order by pour le filtre sur covention collective
		sql +=" ORDER BY P_Credits.Priority, P_Payment_Gain.Day, P_Gain_Credits.P_Collective_Labour_Agr_ID DESC";

		
		PreparedStatement pstmt = null;
//		P_Method_Credits methode;
		
		try
		{
			pstmt = DB.prepareStatement (sql, getTrxName());
			ResultSet rs = pstmt.executeQuery ();
			while ( rs.next())
			{
				m_iP_Method_Credits_ID = rs.getInt("P_Method_Credits_ID");

				P_Employee_Credits employeeCredits = P_Employee_Credits.get(Env.getCtx (), rs.getInt("P_Employee_Credits_ID"), getTrxName() );

				P_Credits creditsInfo = P_Credits.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), getTrxName());
				P_Gain_Credits gainCredits = P_Gain_Credits.get( Env.getCtx(), rs.getInt("P_Gain_Credits_ID"), getTrxName());

				P_Credits_Param creditsParam = P_Credits_Param.get(Env.getCtx(), creditsInfo.getP_Credits_ID(), m_iP_Method_Credits_ID, rs.getTimestamp("Day" ), getTrxName());

				if(gainCredits != null && gainCredits.getCredits_Function().substring(0, 1).equals("P") == true)
				{
			    	BigDecimal variation  = rs.getBigDecimal("Quantity");
		            
		            P_Gain Gain = P_Gain.get( Env.getCtx(), rs.getInt("P_Gain_ID"), getTrxName());

					int line = m_Payment.GetNextLine ( );

		           // On créer une ligne dans la feuille de temps. 
				   // Il serait peut-etre possible de penser de se servire d'une table qui servira a la prochaine génération de temps........			   //
					
					P_Assignment_Param assignment_Param = P_Assignment_Param.get( Env.getCtx (), m_Employee.GetPrincipalAssignment_Param(), getTrxName() );

					P_Payment_Gain PaymentGain = new P_Payment_Gain(Env.getCtx (), -1, getTrxName());
					PaymentGain.setP_Payment_ID( m_Payment.getP_Payment_ID() );
					PaymentGain.setP_Employee_ID( m_Payment.getP_Employee_ID());
					
					PaymentGain.setP_Assignment_ID( assignment_Param.getP_Assignment_ID() );
					PaymentGain.setP_Assignment_Param_ID( assignment_Param.getP_Assignment_Param_ID());
					PaymentGain.setP_Gain_ID( creditsParam.getGainAnnual() );
					PaymentGain.setHourly_Rate( assignment_Param.getHourly_Rate()  );
					
					P_Assignment Assignment = P_Assignment.get( Env.getCtx(),assignment_Param.getP_Assignment_ID(), getTrxName());

					if ( ! assignment_Param.isOut_Of_Range() )
					{
						PaymentGain.setP_Job_Title_ID(assignment_Param.getP_Job_Title_ID());
						PaymentGain.setP_Collective_Labour_Agr_ID( assignment_Param.getP_Collective_Labour_Agr_ID());
						PaymentGain.setP_Salary_Scale_Detail_ID( assignment_Param.getP_Salary_Scale_Detail_ID());
						PaymentGain.setP_Salary_Scale_ID( assignment_Param.getP_Salary_Scale_ID());
					}
					else
					{
						P_Employee Employee = P_Employee.get( Env.getCtx (), Assignment.getP_Employee_ID(), getTrxName() );
						PaymentGain.setP_Job_Title_ID(Employee.getP_Job_Title_ID());
						PaymentGain.setP_Collective_Labour_Agr_ID( Employee.getP_Collective_Labour_Agr_ID());
					}
					PaymentGain.setOriginTime("PBQ");
					Timestamp CreditsDate = rs.getTimestamp("Day" );
					
					int weekNumber = (TimeUtil.getDaysBetween( m_Period.getStartDate(), CreditsDate) /7 ) +1;
					PaymentGain.setWeekIndex( weekNumber);
					PaymentGain.setP_UOM_ID( Gain.getP_UOM_ID());
					PaymentGain.setP_Post_ID( Assignment.getP_Post_ID());
					
					P_Gain_Parameter GainParameter = P_Gain_Parameter.get( Env.getCtx (), Gain.getP_Gain_ID(), assignment_Param.getP_Collective_Labour_Agr_ID(), getTrxName() ); 

					PaymentGain.setP_Gain_Parameter_ID( GainParameter.getP_Gain_Parameter_ID());
					PaymentGain.setMultiplyRate( GainParameter.getMultiplyRate());
					PaymentGain.setType_Rate( Gain.getType_Rate() );
					
		/*			if ( ! creditsInfo.getCreditsContents().equals( P_Credits.CREDITSCONTENTS_Amount))
					{
						PaymentGain.setQuantity(  new BigDecimal(0).subtract(variation ));
						PaymentGain.setQuantityCalc(  new BigDecimal(0).subtract(variation ));
						PaymentGain.setAmountCalc( PaymentGain.getQuantityCalc().multiply( PaymentGain.getHourly_Rate()) );
					}
					else
					{
						PaymentGain.setQuantity( new BigDecimal(0).subtract(variation) );

						 * 
			 */
						if ( creditsParam.isVariationVacationRate() )  
						{
							
							P_Assignment_Param AssignmentParam = P_Assignment_Param.get( Env.getCtx(), rs.getInt( "P_Assignment_Param_ID" ), getTrxName() );
							P_Period endPeriod = P_Period.get(Env.getCtx(), m_TimeSheet.getP_Period_ID(), getTrxName());
							BigDecimal var = P_Employee_Credits.getEmployeeSolde(employeeCredits.getP_Credits_ID(), employeeCredits.getP_Employee_ID(), endPeriod.getEndDate(), getTrxName());
							
							BigDecimal nbrHrsUse = variation;

							variation =   var.multiply( m_Employee.getPrevVacationRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP) );
							BigDecimal nbrTotHre = (new BigDecimal( 255 ).multiply(m_Employee.getPrevVacationRate().divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP)).setScale(0,BigDecimal.ROUND_DOWN)) .multiply( AssignmentParam.getDay_Hours());
							
							//2009.07.28
							BigDecimal nbrHreUse = SumGain( Gain, endPeriod, payment,  getTrxName() );
							nbrTotHre = nbrTotHre.subtract( nbrHreUse );

							//2010.01.21 divide by zero
							if ( nbrTotHre.compareTo(Env.ZERO) == 0)
								variation = Env.ZERO;
							else
								variation = (variation.divide( nbrTotHre,8,BigDecimal.ROUND_HALF_UP).multiply( nbrHrsUse)).setScale(2, BigDecimal.ROUND_HALF_DOWN);

							//2134
							BigDecimal val = variation.divide( m_Employee.getPrevVacationRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP) ,2,BigDecimal.ROUND_HALF_UP);
							 
							// valide le maximum de la banque
							if ( var.compareTo( val) < 0)
							{
								variation = var.multiply( m_Employee.getPrevVacationRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP) ).setScale(2,BigDecimal.ROUND_DOWN);
							}

							if ( variation.compareTo(Env.ZERO) <= 0)
								variation = Env.ZERO;
							
							PaymentGain.setQuantity(  variation );
						}
							
				// }
					PaymentGain.setLine( line );
					//2009.11.24
					P_Period period = P_Period.get(Env.getCtx(), CreditsDate, m_trxName);
					PaymentGain.setP_Year_ID( period.getP_Year_ID()  );
//					PaymentGain.setP_Year_ID( m_Payment.getP_Year_ID()  );
					PaymentGain.setP_Period_ID( period.getP_Period_ID() );
//					PaymentGain.setP_Period_ID( m_Payment.getP_Period_ID() );
					PaymentGain.setDay( CreditsDate );
					PaymentGain.save();

				    // 
				    // TODO Si l'on paie une banque on devrait recalculer tout les gains a % sur d'autre gain.
				    //
					// Génération de l'imputation comptable.
				    TimeValidation val = new TimeValidation( m_TimeSheet , getTrxName());

			        val.calcul_gain( PaymentGain );
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
	        P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName() );
	        log.log( Level.SEVERE, "CreditsEvaluationTransaction - " + sql, e);
		}

	}
	
	private BigDecimal SumGain( P_Gain Gain, P_Period Period, P_Payment Payment, String trxName  )
	{
		P_Year Year = P_Year.get( Env.getCtx(), Period.getP_Year_ID(), trxName );
		String sql = null;
		//2010.01.07 -- gestion du chagement d'année.
		if ( TimeUtil.getDay(Year.getYear() , 6, 1).compareTo( Period.getStartDate()) > 0 ) 
			sql = " Select sum( QuantityCalc ) QuantityCalc From P_Payment_Gain, P_Payment Where P_Payment.P_Payment_ID = P_Payment_Gain.P_Payment_ID and P_Payment_Gain.P_Gain_ID = " + Gain.getP_Gain_ID() + " and P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID() + " and P_Payment_Gain.Day >= " + DB.TO_DATE( TimeUtil.getDay(Year.getYear() -1, 6, 1));
		else
			sql = " Select sum( QuantityCalc ) QuantityCalc From P_Payment_Gain, P_Payment Where P_Payment.P_Payment_ID = P_Payment_Gain.P_Payment_ID and P_Payment_Gain.P_Gain_ID = " + Gain.getP_Gain_ID() + " and P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID() + " and P_Payment_Gain.Day >= " + DB.TO_DATE( TimeUtil.getDay(Year.getYear() , 6, 1));
			

		PreparedStatement pstmt = null;
//		P_Method_Credits methode;
		BigDecimal var = Env.ZERO;
		try
		{
			pstmt = DB.prepareStatement (sql, getTrxName());
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next())
			{
				var = rs.getBigDecimal( "QuantityCalc");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
	        P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName() );
	        log.log( Level.SEVERE, "SumGain - " + sql, e);
		}

		if ( var == null)
			return Env.ZERO;
		
		return var;
	}

	// 2009.06.18 -- end
	
		
		
	
	
	//
	// Évaluation a la transaction
	//
	public void CreditsEvaluationTransaction( P_Payment payment, Timestamp Day ) 
	{		
		String sql = null;

		//On rempli un vector avec tous les gains de la feuille de temps
		//comme ca, si on a un gain qui sort pas dans la requete suivante,
		//on sait que l'employé n'a pas de banque pouvant accepter ce gain, donc
		//va falloir generer un erreur
		Vector<Integer> vecGainOnPayment = new Vector<Integer>(10);
		Vector<Integer> vecGainIsPresent = new Vector<Integer>(10);
		
		String strFetchPaymentGain = "SELECT distinct P_Payment_Gain.P_Gain_ID, P_Payment_Gain.P_Collective_Labour_Agr_ID "
			+ " FROM P_Payment_Gain  "
			+ " WHERE P_Payment_Gain.P_Payment_ID = " + payment.getP_Payment_ID()
            + " AND P_Payment_Gain.Day = " + DB.TO_DATE( Day )
			+ " AND P_Payment_Gain.IsActive = 'Y' "
			+ " AND EXISTS (SELECT 1 FROM P_Gain_Credits gc, P_Gain_Parameter gp WHERE gp.P_Gain_ID = P_Payment_Gain.P_Gain_ID "
			+ "             AND gc.P_Gain_Parameter_ID=gp.P_Gain_Parameter_ID and (gc.Credits_Function like '-%' OR gc.Credits_Function = '=') and (gc.P_Collective_LAbour_Agr_ID is null OR gc.P_Collective_LAbour_Agr_ID = P_Payment_Gain.P_Collective_LAbour_Agr_ID)) "
			+ " ORDER BY P_Payment_Gain.P_Gain_ID";
				 
		try
		{
			PreparedStatement pstmtPG = DB.prepareStatement (strFetchPaymentGain, getTrxName());
			ResultSet rsPG = pstmtPG.executeQuery ();
			while (rsPG.next ())
			{
				vecGainOnPayment.add(new Integer(rsPG.getInt(1)));
				vecGainIsPresent.add(new Integer(0));//par defaut, pas present
			}
		}
		catch(SQLException e)
		{
            P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName() );
            log.log( Level.SEVERE, "CreditsEvaluationTransaction - " + sql, e);
		}

		
		sql = "Select P_Gain_Credits_ID, P_Gain_Credits.P_Credits_ID, ";
		sql +=" P_Employee_Credits.P_Employee_Credits_ID, P_Gain_Parameter.P_Gain_ID, ";
		sql +=" P_Employee_Credits.P_Method_Credits_ID, P_Gain_Credits.P_Collective_Labour_Agr_ID, P_Payment_Gain.P_Collective_Labour_Agr_ID P_Collective_Labour_Agr_ID2, ";
		sql +=" P_Payment_Gain.Day, isnull( P_Payment_Gain.QuantityCalc, 0 ) QuantityCalc, isnull( P_Payment_Gain.AmountCalc, 0 )  AmountCalc ";
		sql +=" From P_Employee_Credits, P_Gain_Credits, P_Credits, P_Gain_Parameter, P_Payment_Gain ";
		sql +="  Where P_Gain_Credits.IsActive = 'Y' ";
		sql +="    And P_Credits.IsActive = 'Y' ";
		sql +="    And P_Gain_Credits.P_Credits_ID = P_Credits.P_Credits_ID " ;
		sql +="    AND P_Gain_Parameter.P_Gain_Parameter_ID = P_Gain_Credits.P_Gain_Parameter_ID";
		sql +="    And P_Gain_Parameter.P_Gain_ID = P_Payment_Gain.P_Gain_ID" ;
		sql +="	   And P_Payment_Gain.P_Payment_ID = " + payment.getP_Payment_ID();
        sql +="    And P_Payment_Gain.Day = " + DB.TO_DATE( Day );
		sql +="    And P_Employee_Credits.P_Employee_ID = " + m_Employee.getP_Employee_ID();
		sql +="    And P_Employee_Credits.IsActive = 'Y' ";
		sql +="    And P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID ";
		sql +="    And (P_Employee_Credits.EffectTo is null OR P_Employee_Credits.EffectTo >= P_Payment_Gain.Day ) ";

//		 2009.06.18
		sql +="    AND P_Gain_Credits.Credits_Function <> 'P' ";
		// 2023-06-13
		sql += " AND P_Gain_Credits.MultiplyRate <> 0"
		;

		//order by pour le filtre sur covention collective
		sql +=" ORDER BY P_Credits.Priority, P_Payment_Gain.Day, P_Gain_Credits.P_Collective_Labour_Agr_ID DESC";
		//log.log( Level.SEVERE ,"Gross Calcul - EvaluationTransaction for Gain [" + P_Gain.get( Env.getCtx(), PaymentGain.getP_Gain_ID(), getTrxName()).getName() + "]  " + sql );
		
//		log.info("EvaluationTransaction sql = " + sql );

		P_Employee_Credits employeeCredits;
		
		PreparedStatement pstmt = null;
//		P_Method_Credits methode;
		
		try
		{
			pstmt = DB.prepareStatement (sql, getTrxName());
			ResultSet rs = pstmt.executeQuery ();
			Vector<Integer> creditsDone = new Vector<Integer>(10);
			Vector<Timestamp> creditsDoneDate = new Vector<Timestamp>(10);
			Vector<Integer> creditsWasNull = new Vector<Integer>(10);
			Vector<Integer> creditsGain = new Vector<Integer>(10);
			while (rs.next ())
			{
//				methode			= P_Method_Credits.get(Env.getCtx(), rs.getInt(5), getTrxName());
//				m_iP_Method_Credits_ID = methode.getP_Method_Credits_ID();
				
				m_iP_Method_Credits_ID = rs.getInt("P_Method_Credits_ID");

				employeeCredits = P_Employee_Credits.get(Env.getCtx (), rs.getInt(3), getTrxName() );

				
				
				P_Credits creditsInfo = P_Credits.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), getTrxName());
				

				P_Gain_Credits gainCredits = P_Gain_Credits.get( Env.getCtx(), rs.getInt(1), getTrxName());
				//on identifie le gain courant comme étant traité
				//si la fonction de credits est en diminution
				if(gainCredits != null && gainCredits.getCredits_Function().substring(0, 1).equals("-") == true)
						validPaymentGain(vecGainOnPayment, vecGainIsPresent, rs.getInt(4));

				//on verifie si c'est la bonne convention collective
				//si on pogne convention a null (a 0) on verifie si on
				// a deja passé. Sinon, c'est la bonne
				boolean newCredit = true;
				
				//si la convention est nulle....
				if(gainCredits.getP_Collective_Labour_Agr_ID() == 0)
				{
					//pour chaque ligne deja traitée
					for(int idx=0;idx<creditsDone.size();idx++)
					{
						//est-ce que la banque a deja ete traitee...
						if(((Integer)creditsDone.get(idx)).intValue() == gainCredits.getP_Credits_ID())
						{
							//pour le meme tag
							if(((Integer)creditsGain.get(idx)).intValue() == rs.getInt("P_Gain_ID"))
							{
								//pour la meme date...
								if(((Timestamp)creditsDoneDate.get(idx)).compareTo(rs.getTimestamp("Day")) == 0)
								{
									//pis la convention etait pas nulle
									if(((Integer)creditsWasNull.get(idx)).compareTo(new Integer(0)) == 0)
									{
										//on mets pas une nouvelle entree
										newCredit = false;
									}
								}
							}
						}
					}
				}
				if(gainCredits.getP_Collective_Labour_Agr_ID() == rs.getInt("P_Collective_Labour_Agr_ID2")
						|| (gainCredits.getP_Collective_Labour_Agr_ID() == 0 && newCredit == true))
				{
					//cette banque est reglé avec la bonne convention
					creditsDone.add(new Integer(gainCredits.getP_Credits_ID()));
					creditsDoneDate.add(rs.getTimestamp("Day"));
					creditsGain.add(new Integer(rs.getInt("P_Gain_ID")));
					if(gainCredits.getP_Collective_Labour_Agr_ID() == 0)
					{
						creditsWasNull.add(new Integer(1));
					}
					else
					{
						creditsWasNull.add(new Integer(0));
					}
					if ( creditsInfo.getCreditsContents().compareTo( P_Credits.CREDITSCONTENTS_Amount ) == 0 )
					{
	  				  //log.log( Level.SEVERE ,"Gross Calcul - EvaluationTransaction = Gain " + GainInfo.getValue() + " Credits " + CreditsInfo.getValue() + " Montant " + PaymentGain.getAmountCalc() );
	   				  BigDecimal creditsVariation = rs.getBigDecimal("AmountCalc").multiply(gainCredits.getMultiplyRate());
	   				  //si mouvement negatif, multiplier par -1
	   				  if(gainCredits.getCredits_Function().equals("-"))
	   				  {
	   					creditsVariation = creditsVariation.multiply(new BigDecimal(-1));
	   				  }
	   				  //on change la date pour prendre la date du mouvement
	   				  //vu qu'on cumule pu les tags a la transactions

					  //CreditsVariation( EmployeeCredits, m_Period.getEndDate(), creditsVariation , "V" + GainCredits.getCredits_Function(), true );
	   				  CreditsVariation( employeeCredits, rs.getTimestamp("Day"), creditsVariation , "V" + gainCredits.getCredits_Function(), true, rs.getInt("P_Gain_ID") );
					}
					else
					{
					  //log.log( Level.SEVERE ,"Gross Calcul - EvaluationTransaction = Gain " + GainInfo.getValue() + " Credits " + CreditsInfo.getValue() + " Quantity " + PaymentGain.getQuantityCalc() );
	   				  BigDecimal creditsVariation = rs.getBigDecimal("QuantityCalc").multiply(gainCredits.getMultiplyRate());
	   				  //si mouvement negatif, multiplier par -1
	   				  if(gainCredits.getCredits_Function().equals("-"))
	   				  {
	   					creditsVariation = creditsVariation.multiply(new BigDecimal(-1));
	   				  }
	   				  //2023-12-18
	   				  if(gainCredits.getCredits_Function().equals("="))
	   				  {
	   					creditsVariation = creditsVariation.multiply(new BigDecimal(-1));
	   				  }
	   				  //on change la date pour prendre la date du mouvement
	   				  //vu qu'on cumule pu les tags a la transactions
	   				
	   				  CreditsVariation( employeeCredits, rs.getTimestamp("Day"), creditsVariation , "V" + gainCredits.getCredits_Function(), true, rs.getInt("P_Gain_ID") );
					}
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
            P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName() );
            log.log( Level.SEVERE, "CreditsEvaluationTransaction - " + sql, e);
		}
		//avant de terminer, on s'assure que chaque gain inscrit sur la feuille de temps à été traité au moins une fois
		//il est interdit de mettre un gain sur une feuille de temps si l'employé ne possède aucune des banques 
		//associé au gain courant (p_payment_gain et p_gain_credits)
		for(int idx1 = 0;idx1<vecGainIsPresent.size();idx1++)
		{
			if(((Integer)vecGainIsPresent.get(idx1)).intValue() == 0)//pas présent
			{
				P_Gain ref = P_Gain.get(Env.getCtx(), ((Integer)vecGainOnPayment.get(idx1)).intValue(), getTrxName());
// Modif temporaire
				if(ref != null)
					P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le gain " + ref.getValue() + " n'est pas associé avec une banque associée à l'employé.", getTrxName() );
				else
					P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le gain (P_Gain_ID=" + ((Integer)vecGainOnPayment.get(idx1)).intValue() + ") n'est pas associé avec une banque associée à l'employé.", getTrxName() );
			}
		}
		
	}

	public void CreditsEvaluationTransaction( P_Payment payment, Timestamp Day, int P_Credits_ID ) 
	{		
		String sql = null;

		//On rempli un vector avec tous les gains de la feuille de temps
		//comme ca, si on a un gain qui sort pas dans la requete suivante,
		//on sait que l'employé n'a pas de banque pouvant accepter ce gain, donc
		//va falloir generer un erreur
		Vector<Integer> vecGainOnPayment = new Vector<Integer>(10);
		Vector<Integer> vecGainIsPresent = new Vector<Integer>(10);
		
		String strFetchPaymentGain = "SELECT distinct P_Payment_Gain.P_Gain_ID, P_Payment_Gain.P_Collective_Labour_Agr_ID "
			+ " FROM P_Payment_Gain  "
			+ " WHERE P_Payment_Gain.P_Payment_ID = " + payment.getP_Payment_ID()
            + " AND P_Payment_Gain.Day = " + DB.TO_DATE( Day )
			+ " AND P_Payment_Gain.IsActive = 'Y' "
			+ " AND EXISTS (SELECT 1 FROM P_Gain_Credits gc, P_Gain_Parameter gp WHERE gp.P_Gain_ID = P_Payment_Gain.P_Gain_ID "
			+ "             AND gc.P_Gain_Parameter_ID=gp.P_Gain_Parameter_ID and gc.Credits_Function like '-%' and (gc.P_Collective_LAbour_Agr_ID is null OR gc.P_Collective_LAbour_Agr_ID = P_Payment_Gain.P_Collective_LAbour_Agr_ID)) "
			+ " ORDER BY P_Payment_Gain.P_Gain_ID";
				 
		try
		{
			PreparedStatement pstmtPG = DB.prepareStatement (strFetchPaymentGain, getTrxName());
			ResultSet rsPG = pstmtPG.executeQuery ();
			while (rsPG.next ())
			{
				vecGainOnPayment.add(new Integer(rsPG.getInt(1)));
				vecGainIsPresent.add(new Integer(0));//par defaut, pas present
			}
		}
		catch(SQLException e)
		{
            P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName() );
            log.log( Level.SEVERE, "CreditsEvaluationTransaction - " + sql, e);
		}

		
		sql = "Select P_Gain_Credits_ID, P_Gain_Credits.P_Credits_ID, ";
		sql +=" P_Employee_Credits.P_Employee_Credits_ID, P_Gain_Parameter.P_Gain_ID, ";
		sql +=" P_Employee_Credits.P_Method_Credits_ID, P_Gain_Credits.P_Collective_Labour_Agr_ID, P_Payment_Gain.P_Collective_Labour_Agr_ID P_Collective_Labour_Agr_ID2, ";
		sql +=" P_Payment_Gain.Day, isnull( P_Payment_Gain.QuantityCalc, 0 ) QuantityCalc, isnull( P_Payment_Gain.AmountCalc, 0 )  AmountCalc ";
		sql +=" From P_Employee_Credits, P_Gain_Credits, P_Credits, P_Gain_Parameter, P_Payment_Gain ";
		sql +="  Where P_Gain_Credits.IsActive = 'Y' ";
		sql +="    And P_Credits.IsActive = 'Y' ";
		sql +="    And P_Gain_Credits.P_Credits_ID = P_Credits.P_Credits_ID " ;
		sql +="    AND P_Gain_Parameter.P_Gain_Parameter_ID = P_Gain_Credits.P_Gain_Parameter_ID";
		sql +="    And P_Gain_Parameter.P_Gain_ID = P_Payment_Gain.P_Gain_ID" ;
		sql +="	   And P_Payment_Gain.P_Payment_ID = " + payment.getP_Payment_ID();
        sql +="    And P_Payment_Gain.Day = " + DB.TO_DATE( Day );
//        sql +="    and P_Payment_Gain.p_gain_id in ( select p_gain_id from p_gain where value = '82')" ;
		sql +="    And P_Employee_Credits.P_Employee_ID = " + m_Employee.getP_Employee_ID();
		sql +="    And P_Employee_Credits.IsActive = 'Y' ";
		sql +="    And P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID ";
		sql +="    And (P_Employee_Credits.EffectTo is null OR P_Employee_Credits.EffectTo >= P_Payment_Gain.Day ) ";
		sql +="    AND P_Employee_Credits.P_Credits_ID =  " + P_Credits_ID;

		// 2023-06-13
		sql += " AND P_Gain_Credits.MultiplyRate <> 0"		;

		
		//order by pour le filtre sur covention collective
		sql +=" ORDER BY P_Credits.Priority, P_Payment_Gain.Day, P_Gain_Credits.P_Collective_Labour_Agr_ID DESC";
		//log.log( Level.SEVERE ,"Gross Calcul - EvaluationTransaction for Gain [" + P_Gain.get( Env.getCtx(), PaymentGain.getP_Gain_ID(), getTrxName()).getName() + "]  " + sql );
		
//		log.info("EvaluationTransaction sql = " + sql );

		P_Employee_Credits employeeCredits;
		
		PreparedStatement pstmt = null;
//		P_Method_Credits methode;
		
		try
		{
			pstmt = DB.prepareStatement (sql, getTrxName());
			ResultSet rs = pstmt.executeQuery ();
			Vector<Integer> creditsDone = new Vector<Integer>(10);
			Vector<Timestamp> creditsDoneDate = new Vector<Timestamp>(10);
			Vector<Integer> creditsWasNull = new Vector<Integer>(10);
			Vector<Integer> creditsGain = new Vector<Integer>(10);
			while (rs.next ())
			{
//				methode			= P_Method_Credits.get(Env.getCtx(), rs.getInt(5), getTrxName());
//				m_iP_Method_Credits_ID = methode.getP_Method_Credits_ID();
				
				m_iP_Method_Credits_ID = rs.getInt("P_Method_Credits_ID");

				employeeCredits = P_Employee_Credits.get(Env.getCtx (), rs.getInt(3), getTrxName() );

				
				
				P_Credits creditsInfo = P_Credits.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), getTrxName());
				

				P_Gain_Credits gainCredits = P_Gain_Credits.get( Env.getCtx(), rs.getInt(1), getTrxName());
				//on identifie le gain courant comme étant traité
				//si la fonction de credits est en diminution
				if(gainCredits != null && gainCredits.getCredits_Function().substring(0, 1).equals("-") == true)
						validPaymentGain(vecGainOnPayment, vecGainIsPresent, rs.getInt(4));

				//on verifie si c'est la bonne convention collective
				//si on pogne convention a null (a 0) on verifie si on
				// a deja passé. Sinon, c'est la bonne
				boolean newCredit = true;
				
				//si la convention est nulle....
				if(gainCredits.getP_Collective_Labour_Agr_ID() == 0)
				{
					//pour chaque ligne deja traitée
					for(int idx=0;idx<creditsDone.size();idx++)
					{
						//est-ce que la banque a deja ete traitee...
						if(((Integer)creditsDone.get(idx)).intValue() == gainCredits.getP_Credits_ID())
						{
							//pour le meme tag
							if(((Integer)creditsGain.get(idx)).intValue() == rs.getInt("P_Gain_ID"))
							{
								//pour la meme date...
								if(((Timestamp)creditsDoneDate.get(idx)).compareTo(rs.getTimestamp("Day")) == 0)
								{
									//pis la convention etait pas nulle
									if(((Integer)creditsWasNull.get(idx)).compareTo(new Integer(0)) == 0)
									{
										//on mets pas une nouvelle entree
										newCredit = false;
									}
								}
							}
						}
					}
				}
				if(gainCredits.getP_Collective_Labour_Agr_ID() == rs.getInt("P_Collective_Labour_Agr_ID2")
						|| (gainCredits.getP_Collective_Labour_Agr_ID() == 0 && newCredit == true))
				{
					//cette banque est reglé avec la bonne convention
					creditsDone.add(new Integer(gainCredits.getP_Credits_ID()));
					creditsDoneDate.add(rs.getTimestamp("Day"));
					creditsGain.add(new Integer(rs.getInt("P_Gain_ID")));
					if(gainCredits.getP_Collective_Labour_Agr_ID() == 0)
					{
						creditsWasNull.add(new Integer(1));
					}
					else
					{
						creditsWasNull.add(new Integer(0));
					}
					if ( creditsInfo.getCreditsContents().compareTo( P_Credits.CREDITSCONTENTS_Amount ) == 0 )
					{
	  				  //log.log( Level.SEVERE ,"Gross Calcul - EvaluationTransaction = Gain " + GainInfo.getValue() + " Credits " + CreditsInfo.getValue() + " Montant " + PaymentGain.getAmountCalc() );
	   				  BigDecimal creditsVariation = rs.getBigDecimal("AmountCalc").multiply(gainCredits.getMultiplyRate());
	   				  //si mouvement negatif, multiplier par -1
	   				  if(gainCredits.getCredits_Function().equals("-"))
	   				  {
	   					creditsVariation = creditsVariation.multiply(new BigDecimal(-1));
	   				  }
	   				  //on change la date pour prendre la date du mouvement
	   				  //vu qu'on cumule pu les tags a la transactions

					  //CreditsVariation( EmployeeCredits, m_Period.getEndDate(), creditsVariation , "V" + GainCredits.getCredits_Function(), true );
	   				  CreditsVariation( employeeCredits, rs.getTimestamp("Day"), creditsVariation , "V" + gainCredits.getCredits_Function(), true, rs.getInt("P_Gain_ID") );
					}
					else
					{
					  //log.log( Level.SEVERE ,"Gross Calcul - EvaluationTransaction = Gain " + GainInfo.getValue() + " Credits " + CreditsInfo.getValue() + " Quantity " + PaymentGain.getQuantityCalc() );
	   				  BigDecimal creditsVariation = rs.getBigDecimal("QuantityCalc").multiply(gainCredits.getMultiplyRate());
	   				  //si mouvement negatif, multiplier par -1
	   				  if(gainCredits.getCredits_Function().equals("-"))
	   				  {
	   					creditsVariation = creditsVariation.multiply(new BigDecimal(-1));
	   				  }
	   				  //on change la date pour prendre la date du mouvement
	   				  //vu qu'on cumule pu les tags a la transactions
	   				
	   				  CreditsVariation( employeeCredits, rs.getTimestamp("Day"), creditsVariation , "V" + gainCredits.getCredits_Function(), true, rs.getInt("P_Gain_ID") );
					}
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
            P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName() );
            log.log( Level.SEVERE, "CreditsEvaluationTransaction - " + sql, e);
		}
		//avant de terminer, on s'assure que chaque gain inscrit sur la feuille de temps à été traité au moins une fois
		//il est interdit de mettre un gain sur une feuille de temps si l'employé ne possède aucune des banques 
		//associé au gain courant (p_payment_gain et p_gain_credits)
		for(int idx1 = 0;idx1<vecGainIsPresent.size();idx1++)
		{
			if(((Integer)vecGainIsPresent.get(idx1)).intValue() == 0)//pas présent
			{
				P_Gain ref = P_Gain.get(Env.getCtx(), ((Integer)vecGainOnPayment.get(idx1)).intValue(), getTrxName());
// Modif temporaire
				if ( ! ( ref.getValue().equals("30") || ref.getValue().equals("82")) )
				{
					if(ref != null)
						P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le gain " + ref.getValue() + " n'est pas associé avec une banque associée à l'employé.", getTrxName() );
					else
						P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le gain (P_Gain_ID=" + ((Integer)vecGainOnPayment.get(idx1)).intValue() + ") n'est pas associé avec une banque associée à l'employé.", getTrxName() );
					
				}
			}
		}
		
	}

	
	//
	// Évaluation a la transaction
	//
	// 2010.05.11 génération d'une variation lorsque le calcul des banques créer un gain de paiement de banque.
	public void CreditsEvaluationTransaction( P_Payment_Gain paymentGain, Timestamp Day ) 
	{		
		String sql = null;
	
		sql = "Select P_Gain_Credits_ID, P_Gain_Credits.P_Credits_ID, ";
		sql +=" P_Employee_Credits.P_Employee_Credits_ID, P_Gain_Parameter.P_Gain_ID, ";
		sql +=" P_Employee_Credits.P_Method_Credits_ID, P_Gain_Credits.P_Collective_Labour_Agr_ID, P_Payment_Gain.P_Collective_Labour_Agr_ID P_Collective_Labour_Agr_ID2, ";
		sql +=" P_Payment_Gain.Day, isnull( P_Payment_Gain.QuantityCalc, 0 ) QuantityCalc, isnull( P_Payment_Gain.AmountCalc, 0 )  AmountCalc ";
		sql +=" From P_Employee_Credits, P_Gain_Credits, P_Credits, P_Gain_Parameter, P_Payment_Gain ";
		sql +="  Where P_Gain_Credits.IsActive = 'Y' ";
		sql +="    And P_Credits.IsActive = 'Y' ";
		sql +="    And P_Gain_Credits.P_Credits_ID = P_Credits.P_Credits_ID " ;
		sql +="    AND P_Gain_Parameter.P_Gain_Parameter_ID = P_Gain_Credits.P_Gain_Parameter_ID";
		sql +="    And P_Gain_Parameter.P_Gain_ID = P_Payment_Gain.P_Gain_ID" ;
		sql +="	   And P_Payment_Gain.P_Payment_Gain_ID = " + paymentGain.getP_Payment_Gain_ID();
//        sql +="    And P_Payment_Gain.Day = " + DB.TO_DATE( Day );
		sql +="    And P_Employee_Credits.P_Employee_ID = " + m_Employee.getP_Employee_ID();
		sql +="    And P_Employee_Credits.IsActive = 'Y' ";
		sql +="    And P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID ";
		sql +="    And (P_Employee_Credits.EffectTo is null OR P_Employee_Credits.EffectTo >= P_Payment_Gain.Day ) ";

//		 2009.06.18
//		sql +="    AND P_Gain_Credits.Credits_Function <> 'P' ";
		sql +="    AND P_Gain_Credits.Credits_Function = '+' ";
		
		// 2023-06-13
		sql += " AND P_Gain_Credits.MultiplyRate <> 0"		;
		
		//order by pour le filtre sur covention collective
		sql +=" ORDER BY P_Credits.Priority, P_Payment_Gain.Day, P_Gain_Credits.P_Collective_Labour_Agr_ID DESC";
		//log.log( Level.SEVERE ,"Gross Calcul - EvaluationTransaction for Gain [" + P_Gain.get( Env.getCtx(), PaymentGain.getP_Gain_ID(), getTrxName()).getName() + "]  " + sql );
		
//		log.info("EvaluationTransaction sql = " + sql );

		P_Employee_Credits employeeCredits;
		
		PreparedStatement pstmt = null;
//		P_Method_Credits methode;
		
		try
		{
			pstmt = DB.prepareStatement (sql, getTrxName());
			ResultSet rs = pstmt.executeQuery ();
			Vector<Integer> creditsDone = new Vector<Integer>(10);
			Vector<Timestamp> creditsDoneDate = new Vector<Timestamp>(10);
			Vector<Integer> creditsWasNull = new Vector<Integer>(10);
			Vector<Integer> creditsGain = new Vector<Integer>(10);
			while (rs.next ())
			{
//				methode			= P_Method_Credits.get(Env.getCtx(), rs.getInt(5), getTrxName());
//				m_iP_Method_Credits_ID = methode.getP_Method_Credits_ID();
				
				m_iP_Method_Credits_ID = rs.getInt("P_Method_Credits_ID");

				employeeCredits = P_Employee_Credits.get(Env.getCtx (), rs.getInt(3), getTrxName() );

				
				
				P_Credits creditsInfo = P_Credits.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), getTrxName());
				

				P_Gain_Credits gainCredits = P_Gain_Credits.get( Env.getCtx(), rs.getInt(1), getTrxName());
				//on identifie le gain courant comme étant traité
				//si la fonction de credits est en diminution
	//			if(gainCredits != null && gainCredits.getCredits_Function().substring(0, 1).equals("-") == true)
	//					validPaymentGain(vecGainOnPayment, vecGainIsPresent, rs.getInt(4));

				//on verifie si c'est la bonne convention collective
				//si on pogne convention a null (a 0) on verifie si on
				// a deja passé. Sinon, c'est la bonne
				boolean newCredit = true;
				
				//si la convention est nulle....
				if(gainCredits.getP_Collective_Labour_Agr_ID() == 0)
				{
					//pour chaque ligne deja traitée
					for(int idx=0;idx<creditsDone.size();idx++)
					{
						//est-ce que la banque a deja ete traitee...
						if(((Integer)creditsDone.get(idx)).intValue() == gainCredits.getP_Credits_ID())
						{
							//pour le meme tag
							if(((Integer)creditsGain.get(idx)).intValue() == rs.getInt("P_Gain_ID"))
							{
								//pour la meme date...
								if(((Timestamp)creditsDoneDate.get(idx)).compareTo(rs.getTimestamp("Day")) == 0)
								{
									//pis la convention etait pas nulle
									if(((Integer)creditsWasNull.get(idx)).compareTo(new Integer(0)) == 0)
									{
										//on mets pas une nouvelle entree
										newCredit = false;
									}
								}
							}
						}
					}
				}
				if(gainCredits.getP_Collective_Labour_Agr_ID() == rs.getInt("P_Collective_Labour_Agr_ID2")
						|| (gainCredits.getP_Collective_Labour_Agr_ID() == 0 && newCredit == true))
				{
					//cette banque est reglé avec la bonne convention
					creditsDone.add(new Integer(gainCredits.getP_Credits_ID()));
					creditsDoneDate.add(rs.getTimestamp("Day"));
					creditsGain.add(new Integer(rs.getInt("P_Gain_ID")));
					if(gainCredits.getP_Collective_Labour_Agr_ID() == 0)
					{
						creditsWasNull.add(new Integer(1));
					}
					else
					{
						creditsWasNull.add(new Integer(0));
					}
					if ( creditsInfo.getCreditsContents().compareTo( P_Credits.CREDITSCONTENTS_Amount ) == 0 )
					{
	  				  //log.log( Level.SEVERE ,"Gross Calcul - EvaluationTransaction = Gain " + GainInfo.getValue() + " Credits " + CreditsInfo.getValue() + " Montant " + PaymentGain.getAmountCalc() );
	   				  BigDecimal creditsVariation = rs.getBigDecimal("AmountCalc").multiply(gainCredits.getMultiplyRate());
	   				  //si mouvement negatif, multiplier par -1
	   				  if(gainCredits.getCredits_Function().equals("-"))
	   				  {
	   					creditsVariation = creditsVariation.multiply(new BigDecimal(-1));
	   				  }
	   				  //on change la date pour prendre la date du mouvement
	   				  //vu qu'on cumule pu les tags a la transactions

					  //CreditsVariation( EmployeeCredits, m_Period.getEndDate(), creditsVariation , "V" + GainCredits.getCredits_Function(), true );
	   				  CreditsVariation( employeeCredits, rs.getTimestamp("Day"), creditsVariation , "V" + gainCredits.getCredits_Function(), true, rs.getInt("P_Gain_ID") );
					}
					else
					{
					  //log.log( Level.SEVERE ,"Gross Calcul - EvaluationTransaction = Gain " + GainInfo.getValue() + " Credits " + CreditsInfo.getValue() + " Quantity " + PaymentGain.getQuantityCalc() );
	   				  BigDecimal creditsVariation = rs.getBigDecimal("QuantityCalc").multiply(gainCredits.getMultiplyRate());
	   				  //si mouvement negatif, multiplier par -1
	   				  if(gainCredits.getCredits_Function().equals("-"))
	   				  {
	   					creditsVariation = creditsVariation.multiply(new BigDecimal(-1));
	   				  }
	   				  //on change la date pour prendre la date du mouvement
	   				  //vu qu'on cumule pu les tags a la transactions
	   				
	   				  CreditsVariation( employeeCredits, rs.getTimestamp("Day"), creditsVariation , "V" + gainCredits.getCredits_Function(), true, rs.getInt("P_Gain_ID") );
					}
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
            P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName() );
            log.log( Level.SEVERE, "CreditsEvaluationTransaction - " + sql, e);
		}
		//avant de terminer, on s'assure que chaque gain inscrit sur la feuille de temps à été traité au moins une fois
		//il est interdit de mettre un gain sur une feuille de temps si l'employé ne possède aucune des banques 
		//associé au gain courant (p_payment_gain et p_gain_credits)
/*		for(int idx1 = 0;idx1<vecGainIsPresent.size();idx1++)
		{
			if(((Integer)vecGainIsPresent.get(idx1)).intValue() == 0)//pas présent
			{
				P_Gain ref = P_Gain.get(Env.getCtx(), ((Integer)vecGainOnPayment.get(idx1)).intValue(), getTrxName());
// Modif temporaire
				if ( ! ( ref.getValue().equals("30") || ref.getValue().equals("82")) )
				{
					if(ref != null)
						P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le gain " + ref.getValue() + " n'est pas associé avec une banque associée à l'employé.", getTrxName() );
					else
						P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le gain (P_Gain_ID=" + ((Integer)vecGainOnPayment.get(idx1)).intValue() + ") n'est pas associé avec une banque associée à l'employé.", getTrxName() );
					
				}
			}
		}
*/		
	}

	BigDecimal hoursForJRH_HRJ = Env.ZERO;

	//
	// Détermine la variation
	//
	public void CreditsVariation( P_Employee_Credits employeeCredits, Timestamp d, BigDecimal var, String movementType, boolean fromTransac, int iP_Gain_ID) throws SQLException  
	{
		P_Credits_Param creditsParam = P_Credits_Param.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), m_iP_Method_Credits_ID, d, getTrxName());
		if ( creditsParam == null )
			return;
		
		P_Employee employee = P_Employee.get(Env.getCtx(), employeeCredits.getP_Employee_ID(), getTrxName());
		//si l'employe n'Est pas encore engage a la date d
		if(d.compareTo(employee.getDateHired()) < 0)
			return;
		//si l'employe ne travaille plus a la date d
//2008.12.10		
//		if(employee.getDateLayoff() != null && d.compareTo(employee.getDateLayoff()) > 0)
//			return;

		P_Assignment_Param assignment_Param = P_Assignment_Param.get( Env.getCtx(), m_Employee.GetPrincipalAssignment_Param(), getTrxName());
		if(assignment_Param.getP_Assignment_ID() == 0)
		{
			P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "L'employé "+m_Employee.getValue()+" n'a pas d'affectation principale en date du "+DateFormat.getTimeInstance(DateFormat.SHORT).format(d), getTrxName());
			return;
		}
		

		//pour certaines banques et certains employes faut utiliser les heures 
		//accumulees de l'affectation
		if(creditsParam.isCumulatedHours() == true && assignment_Param.getCumulatedHours().compareTo(Env.ZERO) > 0)
		{
			hoursForJRH_HRJ = assignment_Param.getCumulatedHours();
		}	
		else
		{
			if ( assignment_Param.getDay_Hours().compareTo(Env.ZERO) == 0)
				hoursForJRH_HRJ = new BigDecimal(8);
			else
				hoursForJRH_HRJ = assignment_Param.getDay_Hours();
		}
		
		
		if ( creditsParam != null )
		{
			P_Credits creditsInfo = P_Credits.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), getTrxName());
			
			P_Credits_Family_Mth_Var famVar = P_Credits_Family_Mth_Var.get(Env.getCtx (), creditsParam.getP_Credits_Family_Mth_Var_ID(), getTrxName());
			P_Credits_Mth_Variation mthVar  = P_Credits_Mth_Variation.get(Env.getCtx (), famVar.getP_Credits_Mth_Variation_ID(), getTrxName() );
			
			if ( mthVar == null)
			{
				return;
			}
			BigDecimal Variation = new BigDecimal( 0 );
			BigDecimal NbrHour = new BigDecimal( 0 );



			//log.log( Level.SEVERE ,"Gross Calcul - CreditsVariation = " + creditsInfo.getValue() + " - " + creditsInfo.getName() + " Method variation " + MthVar.getValue() );

			//si on provient d'une transaction, on ne verifie pas le type de variation
			//on entre jusate ici pour l'attribution
			
			//ATTRIBUTION (verifier les methode de variation)
//ici
			if( ! fromTransac )
			{	
				//avant de faire la variation, on verfie si c'est un ALD
				String strSql = "SELECT eltl.StartDate, eltl.EndDate, ltlc.P_Credits_ID, ltlc.Day "
					+" FROM P_Employee_LongTermLeave eltl, P_LongTermLeave ltl, P_LongTermLeave_Credits ltlc "
					+" WHERE eltl.P_LongTermLeave_ID = ltl.P_LongTermLeave_ID "
					+" AND ltl.P_LongTermLeave_ID = ltlc.P_LongTermLeave_ID "
					+" AND eltl.P_Employee_ID = "+employee.getP_Employee_ID()+" "
					+" AND eltl.IsActive = 'Y' "
					+" AND ltl.IsActive = 'Y' "
					+" AND ltlc.IsActive = 'Y'";
				PreparedStatement pstmt = DB.prepareStatement(strSql, getTrxName());
				ResultSet rs = pstmt.executeQuery();
				
				//on entre ici si l'employee est en ALD
				
				//on flag a true si on skip la banque parce que l'ALD est actif dessus
				boolean mustExonerate = false;
				
				while(rs.next())
				{
					//on commence par calculer depuis quand l'employee est en ALD
					int DaySinceALD = 0;
					Timestamp endDate = null;
				
					//si on a une date de fin...
					if(rs.getTimestamp(2) != null)
					{
						//si la date de fin est plus petite que la date de la transaction
						//on pend la date de fin opur calculer le nombre de jour sur l'ALD
						if(m_eval_timestamp_result.compareTo(rs.getTimestamp(2)) >= 0)
						{
							endDate = rs.getTimestamp(2);
						}
						else
						{
							endDate = m_eval_timestamp_result;
						}
					}
					else
					{
						endDate = m_eval_timestamp_result;
					}
					
					DaySinceALD = TimeUtil.getDaysBetween(rs.getTimestamp(1), endDate) + 1;
					//si le nombre de jour est atteints
					if(rs.getInt(4) <= DaySinceALD)
					{
						//si la banque doit etre exonérée
						if(rs.getInt(3) == creditsInfo.getP_Credits_ID())
						{
							//de plus, on exonere seulement si la date de fin d'exoneration
							//est = ou plus grande a la date de mouvement
							if(endDate.compareTo(m_eval_timestamp_result) >= 0)
							{
								mustExonerate = true;
							}
						}
					}
				}

				//si le flag de ALD est a true, on sort (ici parce que ALD juste traité en Attribution
				if(mustExonerate == true)
				{
					//on sort sans traitement
					return;
				}
				if ( mthVar.getValue().compareTo("001" ) == 0 ) // Transaction Variation
				{
					Variation = var;
					//pour attribuer, il faut la présence d'au moins un tag évaluateur
				}
				
				if ( mthVar.getValue().compareTo("002") == 0 ) // Fixe Variation
				{
					Variation = creditsParam.getCreditsVariation();
				}
				
				if ( mthVar.getValue().compareTo("003") == 0 ) // Variation du solde autre Banque 
				{
					Variation = P_Credits.getEmployeeSolde( creditsInfo, m_Employee, d, getTrxName() );
				}
				
				if ( mthVar.getValue().compareTo("004") == 0 ) // Variation selon borne
				{
					//P_Credits CreditsJoin = P_Credits.get( Env.getCtx (), creditsParam.getP_Credits_Join(), getTrxName() );
					P_Credits[] creditsJoin = creditsParam.getP_Credits_Join(m_Employee.getP_Employee_ID());
					
					BigDecimal bdCumulSld = Env.ZERO;
					for(int idx = 0;idx<creditsJoin.length;idx++)
					{
						bdCumulSld = bdCumulSld.add(P_Credits.getEmployeeSolde( creditsJoin[idx], m_Employee, m_eval_timestamp_result, getTrxName() ));
					}
					P_Credits_Bound CreditsBound = P_Credits_Bound.get( Env.getCtx (), employeeCredits.getP_Credits_ID(), m_iP_Method_Credits_ID ,m_eval_timestamp_result ,bdCumulSld , getTrxName() );
					Variation = CreditsBound.getVariation();

					
					// 
					// gestion d'un maximum de variation.
					//
					maximumValidation( CreditsBound, creditsInfo, Variation);

				}

				if ( mthVar.getValue().compareTo("012") == 0 ) // Variation selon borne (solde en date banque)
				{
					P_Credits[] creditsJoin = creditsParam.getP_Credits_Join(m_Employee.getP_Employee_ID());

					String tempMonth = creditsInfo.getCreditsMonth();
					if(tempMonth.length() == 2 && tempMonth.substring(0,1).equals("0"))
					{
						tempMonth = tempMonth.substring(1,2);
					}
					Integer iTempMonth = null;
					try
					{
						iTempMonth = new Integer(tempMonth);
					}
					catch (NumberFormatException e)
					{
						log.log(Level.SEVERE, e.toString());
					}
					
					//premiere journee du mois (utilise pour calculer la derniere)
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.MONTH, iTempMonth.intValue() - 1);
					cal.set(Calendar.DAY_OF_MONTH, creditsInfo.getCreditsDay());
					cal.set(Calendar.YEAR, TimeUtil.getYear(d) );  //1900 + d.getYear()

					Timestamp		soldeDate = new Timestamp(cal.getTimeInMillis());  //.getTime().getTime()

					// il faut faire attention a l'année car souvent ce type de banque
					// auras le 1 avril comme date de banque
					// alors si on est exemple en février 2006 il ne faut pas 
					// aller lire le solde de la banque lier au 1 avril 2006 mais bien
					// au 1 avril 2005.
					///
					if ( soldeDate.compareTo(d ) > 0 )
				    {
						cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) -1 );
						soldeDate = new Timestamp(cal.getTimeInMillis());  // .getTime().getTime()
					}
					
					BigDecimal bdCumulSld = Env.ZERO;
					for(int idx = 0;idx<creditsJoin.length;idx++)
					{
						bdCumulSld = bdCumulSld.add(P_Credits.getEmployeeSolde( creditsJoin[idx], m_Employee, soldeDate, getTrxName() ));
					}
					P_Credits_Bound CreditsBound = P_Credits_Bound.get( Env.getCtx (), employeeCredits.getP_Credits_ID(), m_iP_Method_Credits_ID ,m_eval_timestamp_result ,bdCumulSld , getTrxName() );
					Variation = CreditsBound.getVariation();
				}

				if ( mthVar.getValue().compareTo("005") == 0 ) // Variation en jours/période
				{
					
					Timestamp dateHired = employee.getDateHired();
					Timestamp dateFired = employee.getDateLayoff();
					Timestamp startDate = null;
					Timestamp endDate = null;
					int numDay = 0;
					
					//si la date d'embauche de l'employe est plusgrande ou egal a la date de debut
					//de periode, on prend la date d'embauche comme date de debut
					if(dateHired != null && dateHired.compareTo(m_Period.getStartDate()) >= 0)
					{
						startDate = dateHired;
					}
					else
					{
						startDate = m_Period.getStartDate();
					}
					//si la date de mise a pied de l'employe est plus grande ou egal
					//a la date de fin de periode, on prend la date de mise a pied 
					//comme date de fin
					if(dateFired != null && dateFired.compareTo(m_Period.getEndDate()) <= 0)
					{
						endDate = dateFired;
					}
					else
					{
						endDate = m_Period.getEndDate();
					}
					
					
					//
					// on prend en compte le changement de status d'un employé en fontion 
					// de la date d'activation et de désactivation de la banque
					//
					if ( employeeCredits.getEffectTo() != null && employeeCredits.getEffectTo().compareTo(endDate) < 0 )
					{
						endDate = (Timestamp)employeeCredits.getEffectTo().clone();
					}
					
					if ( employeeCredits.getEffectIn() != null && employeeCredits.getEffectIn().compareTo(startDate) > 0 )
					{
						startDate = (Timestamp)employeeCredits.getEffectIn().clone();
					}
				
					if ( d.compareTo(endDate) == 0)
					{
						numDay = TimeUtil.getDaysBetween(startDate, endDate) + 1;
						//+1 parce que getDaysBetween est pas inclusif sur la date de debut
						Variation =  new BigDecimal( numDay );
					}

				}
	
				if ( mthVar.getValue().compareTo("006") == 0 )// aucune Variation
				{
					Variation = new BigDecimal( 0 );
				}
	
				if ( mthVar.getValue().compareTo("014") == 0 ) // Variation selon tableau de vacance
				{
					//on va chercher le solde de la premiere banque (Credits_Join)
					//on prend ce solde et on l'amene dans la borne de la banque courante
					//ce qui nous donne le solde sldExp
					//ensuite on prend le solde de la 2eme banque liee (Credits_Join_Other)
					//ce qui nous donne le solde sldDayWorked
					//avec ces 2 soldes, on va cherche le nombre de jour de vacances dans
					//la table P_ALAT (Annual Leave - Accumulation Table)
					
					//P_Credits creditsJoin = P_Credits.get( Env.getCtx (), creditsParam.getP_Credits_Join(), getTrxName() );
					P_Credits[] creditsJoin = creditsParam.getP_Credits_Join(m_Employee.getP_Employee_ID());
					
					BigDecimal bdCumulSld = Env.ZERO;
					for(int idx = 0;idx<creditsJoin.length;idx++)
					{
						bdCumulSld = bdCumulSld.add(P_Credits.getEmployeeSolde( creditsJoin[idx], m_Employee, m_eval_timestamp_result, getTrxName() ));
					}

					P_Credits_Bound creditsBound = P_Credits_Bound.get( Env.getCtx (), employeeCredits.getP_Credits_ID(), m_iP_Method_Credits_ID, d ,bdCumulSld, getTrxName() );
					
					P_Credits creditsJoin_Other = P_Credits.get( Env.getCtx (), creditsParam.getP_Credits_Join_Other(), getTrxName() );
					BigDecimal sldExp = Env.ZERO;
					BigDecimal sldDayWorked = Env.ZERO;
					sldExp = creditsBound.getVariation();
					sldDayWorked = P_Credits.getEmployeeSolde( creditsJoin_Other, m_Employee, m_eval_timestamp_result, getTrxName() );
					
					String sql007 = "SELECT IsNull(Max(ResultingAnnualLeave), 0) FROM P_Alat"
						+" WHERE DaysWorked <= ? "
						+" AND MaximumAnnualLeave = ? "
						+" AND IsActive = 'Y' ";
					PreparedStatement pstmt007 = DB.prepareStatement(sql007, getTrxName());
					pstmt007.setBigDecimal(1, sldDayWorked);
					pstmt007.setBigDecimal(2, sldExp);
					ResultSet rs007 = pstmt007.executeQuery();
					if(rs007.next())
					{
						Variation = rs007.getBigDecimal(1);
					}
					else
					{
						Variation = Env.ZERO;
						String message = "Erreur : Vacances - Table Accumulation -- Erreur de données";
						P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, message, getTrxName() );
					}
				}

				if ( mthVar.getValue().compareTo("007") == 0 ) // Variation selon borne (%)
				{
					P_Credits[] creditsJoin = creditsParam.getP_Credits_Join(m_Employee.getP_Employee_ID());
					
					BigDecimal bdCumulSld = Env.ZERO;
					for(int idx = 0;idx<creditsJoin.length;idx++)
					{
						bdCumulSld = bdCumulSld.add(P_Credits.getEmployeeSolde( creditsJoin[idx], m_Employee, m_eval_timestamp_result, getTrxName() ));
					}
					if ( m_Employee.isVacationFixeRate() )
					{
						Variation =   var.multiply( m_Employee.getVacationFixeRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP) );
					}
					else
					{
						P_Credits_Bound CreditsBound = P_Credits_Bound.get( Env.getCtx (), employeeCredits.getP_Credits_ID(), m_iP_Method_Credits_ID ,m_eval_timestamp_result ,bdCumulSld , getTrxName() );
						Variation =   var.multiply( CreditsBound.getVariation().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP) );
					}	
					
				}

				/***
				 * 
				 */
				if ( mthVar.getValue().compareTo("013") == 0 ) // Variation selon borne (%) ou % négocier
				{
					
					P_Credits creditsJoin = P_Credits.get( Env.getCtx(), creditsParam.getP_Credits_Join(), getTrxName() );
					
					BigDecimal bdCumulSld = Env.ZERO;

					bdCumulSld = bdCumulSld.add(P_Credits.getEmployeeSolde( creditsJoin, m_Employee, m_eval_timestamp_result, getTrxName() ));

					BigDecimal rate = Env.ZERO;
					if ( m_Employee.isVacationFixeRate() )
					{
						rate =   m_Employee.getVacationFixeRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP) ;
					}
					else
					{
						P_Credits_Bound CreditsBound = P_Credits_Bound.get( Env.getCtx (), employeeCredits.getP_Credits_ID(), m_iP_Method_Credits_ID ,m_eval_timestamp_result ,bdCumulSld , getTrxName() );
						rate =   CreditsBound.getVariation().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP) ;
					}
					
					// Calcul le nombre de jour de vacance que l'employé à droit;
					BigDecimal nbrDay = (new BigDecimal( 250).multiply( rate )).setScale(0, BigDecimal.ROUND_DOWN);

					// Nouveau employé - sans % de vacance négocier
					if ( TimeUtil.isSameMonth(employee.getDateHired(),m_Period.getEndDate() )					 	
					   && ! m_Employee.isVacationFixeRate())
					{
						// Nouveau employé débutant avant le 15 du mois, accumule 1 jour par mois jusqu'a concurence de 10
						if ( TimeUtil.isSameMonth(employee.getDateHired(),m_Period.getEndDate())
							 && TimeUtil.getDay_Of_Month(employee.getDateHired()) > 15 
						)
						{
							Variation = Env.ZERO;
						}
						else
						{
							Variation = Env.ONE;
						}
						
					}
					else
					{
						
						Variation = (nbrDay.divide(new BigDecimal(12), 2,BigDecimal.ROUND_HALF_UP));
						
					}

					// Si l'employé quitte avant le 15 du mois.
					if ( employee.getDateLayoff() != null && TimeUtil.isSameMonth(employee.getDateLayoff(),m_Period.getEndDate())
					     && TimeUtil.getDay_Of_Month(employee.getDateHired()) < 15 )
					{
						Variation = Env.ZERO;
					}
					 
					// Ajustement du mouvement de banque pour le dernier mois
					// on gere ici aussi bien le maximum de jours par année que l'employé à droit et l'ajustement en fin d'année
					BigDecimal sld = P_Credits.getEmployeeSolde( creditsInfo, m_Employee, m_eval_timestamp_result, getTrxName() );

					// converti le solde en jours si dans la banque on accumule des heures
					if ( creditsInfo.getCreditsContents().equals(  P_Credits.CREDITSCONTENTS_DaysConvertToHours ))
					{
						sld = sld.divide( hoursForJRH_HRJ , 2, BigDecimal.ROUND_HALF_UP );
					}

					
					if ( (sld.add( Variation )).setScale(0, BigDecimal.ROUND_UP) .compareTo( nbrDay  ) >= 0 )
						Variation = nbrDay.subtract( sld);
				}

				/***
				 * 
				 */				

				
				if ( mthVar.getValue().compareTo("913") == 0 ) // Variation selon borne (%) ou % négocier
				{
/*					
					P_Credits creditsJoin = P_Credits.get( Env.getCtx(), creditsParam.getP_Credits_Join(), getTrxName() );
					
					BigDecimal bdCumulSld = Env.ZERO;

					bdCumulSld = bdCumulSld.add(P_Credits.getEmployeeSolde( creditsJoin, m_Employee, m_eval_timestamp_result, getTrxName() ));
*/
					Timestamp dateFired = employee.getDateLayoff();
					Timestamp endDate = m_Period.getEndDate();

					Timestamp dateHired = employee.getDateHired();
					Timestamp startDate = m_Period.getStartDate();;
					
					
					//si la date de mise a pied de l'employe est plus grande ou egal
					//a la date de fin de periode, on prend la date de mise a pied 
					//comme date de fin
					if(dateFired != null && dateFired.compareTo(m_Period.getEndDate()) <= 0)
					{
						endDate = dateFired;
					}
					else
					{
						endDate = m_Period.getEndDate();
					}

					if ( startDate.compareTo( creditsParam.getEffectIn() ) < 0 )
					{
						startDate = creditsParam.getEffectIn();
					}
					
					if ( d.compareTo(endDate) == 0)
					{
						BigDecimal bdCumulSld = Env.ZERO;
						var = bdCumulSld.add(this.getEmployeeSoldeBV21( m_Payment, startDate, endDate, getTrxName() ));

						
						if ( m_Employee.isVacationFixeRate() )
						{
							if ( m_Employee.getVacationFixeRate() == null || m_Employee.getVacationFixeRate().compareTo(Env.ZERO) == 0)
							{
								String message = "Vérifier si l'employé % de vacances fixe, le système ne trouve pas de taux de vacances";
							    P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, message, getTrxName() );
							}
							Variation =   var.multiply( m_Employee.getVacationFixeRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP) );
						}
						else
						{
							if ( m_Employee.getVacationRate() == null)
							{
								String message = "Vérifier si l'employé a une banque de vacances, le système ne trouve pas de taux de vacances";
							    P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, message, getTrxName() );
								
							}
							else
							{
								Variation =   var.multiply( m_Employee.getVacationRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP) );
							}

							//						P_Credits_Bound CreditsBound = P_Credits_Bound.get( Env.getCtx (), employeeCredits.getP_Credits_ID(), m_iP_Method_Credits_ID ,m_eval_timestamp_result ,bdCumulSld , getTrxName() );
//							Variation =   var.multiply( CreditsBound.getVariation().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP) );
						}
						
					}


					
				}

				
				if ( mthVar.getValue().compareTo("008") == 0 ) // Variation solde BQ avec %  
				{
					Variation = P_Credits.getEmployeeSolde( creditsInfo, m_Employee, d, getTrxName() ); //.multiply( );
				}
				if ( mthVar.getValue().compareTo("009") == 0 ) // Variation selon Borne Mensuelle
					//en fait, une variation selon une banque jumelé avec une variation
					//selon une borne (en %)
					//nonobstant l'analyse de SMorin, cette variation prend toujours en compte le
					//nombre de jour ouvrable qui aurait du etre travaillé dans le mois
				{	
					//premiere journee du mois (utilise pour calculer la derniere)
					Calendar start = Calendar.getInstance();
					//start.set( m_Period.getStartDate().getYear(), m_Period.getStartDate().getMonth(), 1 );
					start.setTimeInMillis( m_Period.getStartDate().getTime());
					//on set a la premier journee du mois
					start.set(Calendar.DAY_OF_MONTH, 1);
					Date startDate = start.getTime();
					Timestamp monthStartDate = new Timestamp( startDate.getTime());
							
					//derniere journée du mois
					Calendar end = Calendar.getInstance();
					end.setTimeInMillis( m_Period.getStartDate().getTime());
					//on va chercher la derniere journée du mois (maximum jour du mois)
					end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
					Date endDate = end.getTime();
					Timestamp monthEndDate = new Timestamp( endDate.getTime() ) ;

					//P_Credits creditsJoin = P_Credits.get( Env.getCtx (), creditsParam.getP_Credits_Join(), getTrxName() );
					P_Credits[] creditsJoin = creditsParam.getP_Credits_Join(m_Employee.getP_Employee_ID());
					
					BigDecimal bdCumulSld = Env.ZERO;
					for(int idx = 0;idx<creditsJoin.length;idx++)
					{
						bdCumulSld = bdCumulSld.add(P_Credits.getCreditsVariation( creditsJoin[idx], m_Employee, monthStartDate, monthEndDate, getTrxName() ));
					}

					//donc on va chercher le solde a la derniere journée
					//BigDecimal postSolde = P_Credits.getEmployeeSolde( creditsJoin, m_Employee, monthEndDate, getTrxName() );
//					donc on va chercher le solde a la premiere journée
					//BigDecimal preSolde = P_Credits.getEmployeeSolde( creditsJoin, m_Employee, monthStartDate, getTrxName() );
					//BigDecimal autreSolde = P_Credits.getCreditsVariation( creditsJoin, m_Employee, monthStartDate, monthEndDate, getTrxName() );
					BigDecimal autreSolde = bdCumulSld;
					//on va chercher le nombre de jour qui aurait du etre travaillé pendant le mois
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(m_Period.getStartDate().getTime());
					BigDecimal soldeTheorique = new BigDecimal(employee.getWorkDayInMonth(cal.get(Calendar.YEAR), TimeUtil.getMonth(m_Period.getStartDate())));
					//on divise le nombre de jour reel par le nombre de jour theorique
					//avoir le pourcentage de jour travaille
					BigDecimal bdPourcentage = autreSolde.divide(soldeTheorique, 4, BigDecimal.ROUND_HALF_UP);//4 = scale
					//on aplliaque ce pourcentage dans la borne
					P_Credits_Bound CreditsBound = P_Credits_Bound.get( Env.getCtx (), employeeCredits.getP_Credits_ID(), m_iP_Method_Credits_ID, d, bdPourcentage, getTrxName());
			  	    //log.log( Level.SEVERE ,"Variation method 009 " + " Credits " + creditsInfo.getValue() + " variation " + var + " % " + CreditsBound.getVariation().divide( new BigDecimal(100), 5 , BigDecimal.ROUND_HALF_UP) ) ;
					
					Variation = CreditsBound.getVariation();
				}
				if ( mthVar.getValue().compareTo("010") == 0 ) // Variation selon Borne/majorée
				{
					
				}
				if ( mthVar.getValue().compareTo("011") == 0 ) // Variation quotidienne  
				{
					Timestamp dateHired = (Timestamp)employee.getDateHired().clone();
					Timestamp dateFired = null;
					if(employee.getDateLayoff() != null)
						dateFired = (Timestamp)employee.getDateLayoff().clone();
					Timestamp startDate = null;
					Timestamp endDate = null;
					
					//si la date d'embauche de l'employe est plusgrande ou egal a la date de debut
					//de periode, on prend la date d'embauche comme date de debut
					if(dateHired != null && dateHired.compareTo(m_Period.getStartDate()) >= 0)
					{
						startDate = (Timestamp)dateHired.clone();
					}
					else
					{
						startDate = (Timestamp)m_Period.getStartDate().clone();
					}
					
					
					
					//si la date de mise a pied de l'employe est plus grande ou egal
					//a la date de fin de periode, on prend la date de mise a pied 
					//comme date de fin
					if(dateFired != null && dateFired.compareTo(m_Period.getEndDate()) <= 0)
					{
						endDate = (Timestamp)dateFired.clone();
					}
					else
					{
						endDate = (Timestamp)m_Period.getEndDate().clone();
					}
					
					//
					// on prend en compte le changement de status d'un employé en fontion 
					// de la date d'activation et de désactivation de la banque
					//
					if ( employeeCredits.getEffectTo() != null && employeeCredits.getEffectTo().compareTo(endDate) < 0 )
					{
						endDate = (Timestamp)employeeCredits.getEffectTo().clone();
					}
					
					if ( employeeCredits.getEffectIn() != null && employeeCredits.getEffectIn().compareTo(startDate) >= 0 )
					{
						startDate = (Timestamp)employeeCredits.getEffectIn().clone();
					}
					
					
					//int iNumDayBetween = TimeUtil.getDaysBetween(startDate, endDate) + 1;
					//for(int idx1=0;idx1<iNumDayBetween;idx1++)
					{
						Timestamp day = d; //m.m(Timestamp)startDate.clone();
						BigDecimal dayVariation = Env.ZERO;
						//m.m day.setDate( startDate.getDate()+idx1);
						/*if ( creditsInfo.getCreditsContents().compareTo( P_Credits.CREDITSCONTENTS_Amount ) == 0 )
						{
							strCalc = "AmountCalc";
						}
						else
						{
							strCalc = "QuantityCalc";
						}*/
						String strSqlBqe = "SELECT isnull(Sum(pg.Quantity), 0) "
							+" FROM P_Gain_Credits gc, P_Payment_Gain pg, P_Gain_Parameter gp"
							+" WHERE gc.P_Credits_ID="+creditsInfo.getP_Credits_ID() 
							+" AND pg.P_Gain_ID=gp.P_Gain_ID"
							+" AND gc.P_Gain_Parameter_ID=gp.P_Gain_Parameter_ID"
							+" AND pg.P_Employee_ID="+employee.getP_Employee_ID()
							+" AND pg.Day = ?"
							+" GROUP BY pg.P_Gain_ID";
						PreparedStatement pstmtBqe = DB.prepareStatement(strSqlBqe, getTrxName());
						pstmtBqe.setTimestamp(1, day);
						ResultSet rsBqe = pstmtBqe.executeQuery();
						while(rsBqe.next())
						{
							if(rsBqe.getBigDecimal(1).compareTo(new BigDecimal(0)) > 0)
							{
								dayVariation = dayVariation.add(new BigDecimal("1.4"));
							}
						}
						rsBqe.close();
						pstmtBqe.close();

						if(dayVariation.compareTo(new BigDecimal("1.4")) > 0)
						{
							dayVariation = new BigDecimal("1.4");
						}
						P_Credits_Movement CreditsMovement = P_Credits_Movement.get(Env.getCtx (), m_Payment.getP_Payment_ID(), employeeCredits.getP_Credits_ID(), d, movementType, "FTP", getTrxName() ) ;

						CreditsMovement.setP_Credits_ID( creditsInfo.getP_Credits_ID());
						CreditsMovement.setP_Employee_ID( employee.getP_Employee_ID());
						CreditsMovement.setPMovementDate( day );
						CreditsMovement.setPMovementOrigin( "FTP");
						CreditsMovement.setPMovementType( "V+" );
						CreditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());
						
						CreditsMovement.setP_Method_Credits_ID( creditsParam.getP_Method_Credits_ID() );
//						CreditsMovement.setDescription( " ");

						dayVariation = dayVariation.setScale(2, BigDecimal.ROUND_HALF_UP );
						NbrHour = dayVariation.setScale(2, BigDecimal.ROUND_HALF_UP );		 			 

						if  (  CreditsMovement.getPMovementVariation() != null )
						{
							CreditsMovement.setPMovementVariation( CreditsMovement.getPMovementVariation().add( dayVariation ) );
							CreditsMovement.setPMovementHour( CreditsMovement.getPMovementHour().add( NbrHour ) );
						}
						else
						{
							CreditsMovement.setPMovementVariation( dayVariation );
							CreditsMovement.setPMovementHour(  NbrHour );
						}


						CreditsMovement.setP_Payment_ID( m_Payment.getP_Payment_ID() );
						CreditsMovement.save();
					}
					//les mouvements ont deja étés sauvegardes, donc ont genere un mouvement
					//a zero pour qu'il poursuive la boucle sans pour autant enregistrer d'autre mouvement
					Variation = Env.ZERO;
					
					//ICI COMMENCE LA VÉRIFICATION DE CORRECTIONS EN PAIES ANTERIEURS 
					//POUR LES BANQUES EN VARIATION QUOTIDIENNE
					
					//On va chercher les gains en CPA sur le paiement courant
					//qui influence la banque courante (méthode de variation quotidienne)
					String strSqlCpa = "SELECT distinct P_Payment_Gain.Day, P_Payment_Gain.P_Period_ID "
						+" FROM P_Gain_Credits, P_Payment_Gain, P_Gain_Parameter, P_Payment"
						+" WHERE P_Gain_Credits.P_Credits_ID="+creditsInfo.getP_Credits_ID()
						+" AND P_Payment_Gain.P_Period_ID <> P_Payment.P_Period_ID "
						+" AND P_Payment_Gain.P_Gain_ID=P_Gain_Parameter.P_Gain_ID"
						+" AND P_Gain_Credits.P_Gain_Parameter_ID=P_Gain_Parameter.P_Gain_Parameter_ID"
						+" AND P_Payment_Gain.P_Payment_ID = " + m_Payment.getP_Payment_ID()
						+" AND P_Payment.P_Payment_ID = " + m_Payment.getP_Payment_ID()
						+" AND P_Payment_Gain.Quantity <> 0"

					// 2023-06-13
						+ " AND P_Gain_Credits.MultiplyRate <> 0"		;

					PreparedStatement pstmCPA = DB.prepareStatement(strSqlCpa, getTrxName());
					ResultSet rsCPA = pstmCPA.executeQuery();

					while(rsCPA.next())
					{

						//on a une CPA qui influence ma banque courante
						//on va donc aller voir tous les gain de cette journée, en période d'origine ou en CPA
						String strSqlDay = "SELECT isnull(Sum(P_Payment_Gain.Quantity), 0) "
							+" FROM P_Gain_Credits, P_Payment_Gain, P_Gain_Parameter, P_Payment"
							+" WHERE P_Gain_Credits.P_Credits_ID="+creditsInfo.getP_Credits_ID()
							+" AND P_Payment_Gain.P_Gain_ID=P_Gain_Parameter.P_Gain_ID"
							+" AND P_Gain_Credits.P_Gain_Parameter_ID=P_Gain_Parameter.P_Gain_Parameter_ID"
							+" AND P_Payment_Gain.P_Payment_ID = P_Payment.P_Payment_ID"
							+" AND P_Payment_Gain.Day >= ?"
							+" AND P_Payment_Gain.Day <= ?"
							+" AND P_Payment_Gain.P_Period_ID = " + rsCPA.getInt("P_Period_ID")
							+" AND P_Payment.P_Employee_ID = "+employee.getP_Employee_ID()
							// 2023-06-13
							+" AND P_Gain_Credits.MultiplyRate <> 0"
							;
												
							//+" GROUP BY P_Payment_Gain.P_Gain_ID";
						PreparedStatement pstmDAY = DB.prepareStatement(strSqlDay, getTrxName());
						pstmDAY.setTimestamp(1, rsCPA.getTimestamp("Day"));
						pstmDAY.setTimestamp(2, m_Period.getEndDate());
						ResultSet rsDAY = pstmDAY.executeQuery();
						

						while(rsDAY.next())
						{
							//bon, avant de statuer sur l'Attribution, on va aller vérifier si
							//on a attribué ou non dans l'historique de smouvements de banques
							String strSqlMove = "SELECT isnull(Sum(PMovementVariation), 0) "
								+" FROM P_Credits_Movement"
								+" WHERE P_Credits_ID =" + creditsInfo.getP_Credits_ID()
								+" AND P_Employee_ID =" + employee.getP_Employee_ID()
								+" AND PMovementDate = ?"
								+" GROUP BY PMovementDate";
							boolean wasGiven = false;
							PreparedStatement pstmMove = DB.prepareStatement(strSqlMove, getTrxName());
							pstmMove.setTimestamp(1, rsCPA.getTimestamp("Day"));
							ResultSet rsMove = pstmMove.executeQuery();
							if(rsMove.next())
							{
								if(rsMove.getBigDecimal(1).compareTo(new BigDecimal(0)) > 0)
								{
									wasGiven = true;
								}
								else
								{
									wasGiven = false;
								}
							}
							else
							{
								//pas de records = pas de movement
								wasGiven = false;
							}

							rsMove.close();
							pstmMove.close();
							
							if(rsDAY.getBigDecimal(1).compareTo(new BigDecimal(0)) > 0)
							{
								//Apres vérification de tous les mouvements, on doit attribuer
								if(wasGiven == false)
								{
									//ici on doit créer le movement positif
									P_Credits_Movement CreditsMovement = new P_Credits_Movement(Env.getCtx (), getTrxName() ) ;

									CreditsMovement.setP_Credits_ID( creditsInfo.getP_Credits_ID());
									CreditsMovement.setP_Employee_ID( employee.getP_Employee_ID());
									CreditsMovement.setPMovementDate( rsCPA.getTimestamp("Day") );
									CreditsMovement.setPMovementOrigin( "FTP");
									CreditsMovement.setPMovementType( "V+" );
									CreditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());
									
									CreditsMovement.setP_Method_Credits_ID( creditsParam.getP_Method_Credits_ID() );
//									CreditsMovement.setDescription( " ");

									CreditsMovement.setPMovementVariation( new BigDecimal("1.4") );
									CreditsMovement.setPMovementHour( new BigDecimal("0") );



									CreditsMovement.setP_Payment_ID( m_Payment.getP_Payment_ID() );
									CreditsMovement.save();

								}
							}
							else
							{
								//Apres vérification de tous les mouvements, on ne doit pas attribuer
								if(wasGiven == true)
								{
									//ici on doit créer le movement négatif
									P_Credits_Movement CreditsMovement = new P_Credits_Movement(Env.getCtx (), getTrxName() ) ;

									CreditsMovement.setP_Credits_ID( creditsInfo.getP_Credits_ID());
									CreditsMovement.setP_Employee_ID( employee.getP_Employee_ID());
									CreditsMovement.setPMovementDate( rsCPA.getTimestamp("Day") );
									CreditsMovement.setPMovementOrigin( "FTP");
									CreditsMovement.setPMovementType( "V+" );
									CreditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());
									
									CreditsMovement.setP_Method_Credits_ID( creditsParam.getP_Method_Credits_ID() );
//									CreditsMovement.setDescription( " ");

									BigDecimal minus1dot4 = new BigDecimal("1.4");
									minus1dot4 = minus1dot4.negate();
									CreditsMovement.setPMovementVariation( minus1dot4 );
									CreditsMovement.setPMovementHour( new BigDecimal("0") );



									CreditsMovement.setP_Payment_ID( m_Payment.getP_Payment_ID() );
									CreditsMovement.save();
								}
							}
						}
						rsDAY.close();
						pstmDAY.close();
					}
					rsCPA.close();
					pstmCPA.close();
				}


/*
 * Marc M. 
 * Les attribution négative doivent rester un V+
 *
 * */
/* 				if(Variation.compareTo(Env.ZERO) == -1  )
				{
					movementType = "V-";
				}
*/
				//en attribution, on vérifie si la banque peut etre modifie par
				// l'artt de l'employée
				//Facteur ARTT
				P_Assignment assignment = P_Assignment.get(Env.getCtx(), assignment_Param.getP_Assignment_ID(),getTrxName());
				if(creditsParam.isAffectedByRWT() == true && assignment.isRWT(d) == true)
				{
					BigDecimal rwtFactor = Env.ZERO;
					rwtFactor = assignment.getWeekly_HoursRWT(d).divide(assignment_Param.getWeekly_Hours(), 4, BigDecimal.ROUND_HALF_UP);
					Variation = Variation.multiply(rwtFactor);
					// 7 jul 2008
					Variation = Variation.setScale(2, BigDecimal.ROUND_HALF_UP);
				}
				
				if ( creditsInfo.getCreditsContents().equals(  P_Credits.CREDITSCONTENTS_HoursConvertToDays ))
				{
				    Variation = Variation.divide( hoursForJRH_HRJ , 2, BigDecimal.ROUND_HALF_UP );
				}
				else if ( creditsInfo.getCreditsContents().equals(  P_Credits.CREDITSCONTENTS_DaysConvertToHours ))
				{
				    Variation = Variation.multiply( hoursForJRH_HRJ );
				}
				
				if(creditsParam.isProrataWorkHired() == true)
					Variation = vacancesEmbaucheProrata(Variation, creditsParam, employee);

			}
			//TRANSACTION (pas de verification des methode de variation)
			//on fait juste s'assurer que l'unité de mesure du gain est semblable a celui de la banque
			else
			{
				Variation = var;

				if ( creditsParam.isVariationVacationRate() ) // "3320"
					//2010.11.15 division par 0
					if ( this.m_Employee.getPrevVacationRate().compareTo(Env.ZERO ) != 0)
						Variation = var.divide( this.m_Employee.getPrevVacationRate().divide(new BigDecimal(100) ) ,2,BigDecimal.ROUND_HALF_UP );

				if (  creditsInfo.getValue().equals( "7800")) {
					if ( this.m_Employee.getPrevVacationRate().compareTo(Env.ZERO ) != 0)
						Variation = var.multiply( this.m_Employee.getPrevVacationRate().divide(new BigDecimal(100) ) );
				}
				
				//2025-05-03
				if ( mthVar.getValue().compareTo("913") == 0 ) // Variation selon borne (%) ou % négocier
				{
					Variation = Env.ZERO;
/*					if ( m_Employee.isVacationFixeRate() )
					{
						Variation =   var.multiply( m_Employee.getVacationFixeRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP) );
					}
					else
					{
						Variation =   var.multiply( m_Employee.getVacationRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP) );
					}
*/					
				}
				
				P_Gain gain = P_Gain.get(Env.getCtx(), iP_Gain_ID, getTrxName());
				
				P_UOM tagUnits = P_UOM.get(Env.getCtx(), gain.getP_UOM_ID(), getTrxName());
				P_UOM creditsUnits = P_UOM.get(Env.getCtx(), creditsInfo.getP_UOM_ID(), getTrxName());
				//pour l'instant on traite juste les codes de remuneration
				//en Heures (H) et en $$ (M pour montant)
				
				//GAIN EN HEURES
				if(tagUnits.getValue().equals("H"))
				{
					//BANQUE EN HEURES
					if(creditsUnits.getValue().equals("H"))
					{
						//Variation = Variation;
					}
//					BANQUE EN JOURS
					else if(creditsUnits.getValue().equals("J"))
					{
						Variation = Variation.divide( hoursForJRH_HRJ , 2, BigDecimal.ROUND_HALF_UP );
					}

				}
				//GAIN EN MONTANT
				else if(tagUnits.getValue().equals("M"))
				{
					//si le gain en montant mais la banque pas en montant...
					if(creditsUnits.getValue().equals("M") == false)
					{
				        //String message = Msg.getMsg(Env.getCtx(), ""); 
						String message = "L'unité de mesure (" + tagUnits.getValue() +")"
							+" pour le code de rémunération "+gain.getValue()+" n'est pas compatible avec"
							+" l'unité de mesure ("+creditsUnits.getValue()+") de la banque "+creditsInfo.getValue();
					    P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, message, getTrxName() );
						Variation = Env.ZERO;
					}
				}
				
				m_eval_timestamp_result = d;
			}

			//log.log( Level.SEVERE ,"Gross Calcul - CreditsVariation = " + creditsInfo.getValue() + " Method variation " + MthVar.getValue() + " variation " + Variation );

			//si le mouvement est different de 0, on le creer
			if ( Variation.compareTo( new BigDecimal( 0 )) != 0 )
			{
				//on va chercher le mouvement
				/*P_Credits_Movement creditsMovement = P_Credits_Movement.get(Env.getCtx (), m_Payment.getP_Payment_ID(), employeeCredits.getP_Credits_ID(), d, movementType, "FTP", getTrxName() ) ;
				
				//avant d'utiliser le mouvement recuperer, on s'assure que ce soit une attribution si
				//le mouvement actuel provient d'une attribution ou une transaction si le mouvement actuel provient d'une transaction
				if(creditsMovement.isAttribution() == true)
				{
					if(fromTransac == true)
					{
						creditsMovement = null;
						creditsMovement = new P_Credits_Movement(Env.getCtx(), 0, getTrxName());
					}
				}
				else
				{
					if(fromTransac == false)
					{
						creditsMovement = null;
						creditsMovement = new P_Credits_Movement(Env.getCtx(), 0, getTrxName());
					}
				}*/
				P_Credits_Movement creditsMovement = new P_Credits_Movement(Env.getCtx(), 0, getTrxName());

				creditsMovement.setP_Credits_ID( employeeCredits.getP_Credits_ID());
				creditsMovement.setP_Employee_ID( employeeCredits.getP_Employee_ID());
				if(m_eval_timestamp_result != null)
					creditsMovement.setPMovementDate( m_eval_timestamp_result );
				else
					creditsMovement.setPMovementDate( d );
				
				//2023-07-21
				if ( this.m_Payment != null && this.m_Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_HolidayPayment))
				{
					P_Period Period = P_Period.getOpenPeriod(Env.getCtx(), getTrxName() );
					if ( creditsMovement.getPMovementDate().compareTo( Period.getEndDate()) > 0)
					{
						creditsMovement.setPMovementDate( Period.getEndDate() );
					}
					
				}
				//
				
				creditsMovement.setPMovementOrigin( "FTP");
				creditsMovement.setPMovementType( movementType );
				creditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());
				
				creditsMovement.setP_Method_Credits_ID( creditsParam.getP_Method_Credits_ID() );
//				creditsMovement.setDescription( " ");

				Variation = Variation.setScale(2, BigDecimal.ROUND_HALF_UP );
				NbrHour = Variation.setScale(2, BigDecimal.ROUND_HALF_UP );		 			 

				if  (  creditsMovement.getPMovementVariation() != null )
				{
					creditsMovement.setPMovementVariation( creditsMovement.getPMovementVariation().add( Variation ) );
					creditsMovement.setPMovementHour( creditsMovement.getPMovementHour().add( NbrHour ) );
				}
				else
				{
					creditsMovement.setPMovementVariation( Variation );
					creditsMovement.setPMovementHour(  NbrHour );
				}


				creditsMovement.setP_Payment_ID( m_Payment.getP_Payment_ID() );

				if(fromTransac == true)
					creditsMovement.setIsAttribution(false);
				else
					creditsMovement.setIsAttribution(true);
					
					


				if ( creditsMovement.getPMovementVariation().compareTo(Env.ZERO) != 0)
					creditsMovement.save();
				
			}
		}
		
	}
	

	//
	// Valisation des limites 
	//
	public boolean CreditsValidation( Timestamp Day  ) 
	{
		//log.log( Level.SEVERE, "Gross Calcul - CreditsValidation Begin " );

		
		boolean error = false;
		
		String sql = null;

		sql = "Select P_Credits_Movement_ID From P_Credits_Movement Where P_Payment_ID = " + m_Payment.getP_Payment_ID()
		    + " And PMovementDate = " + DB.TO_DATE( Day )
			+ " ORDER BY PMovementDate, IsAttribution desc ";
		//log.log( Level.SEVERE,"Gross Calcul - CreditsValidation - SQL " + sql );
		
		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, getTrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				P_Credits_Movement creditsMovement = P_Credits_Movement.get( Env.getCtx(), rs.getInt(1), getTrxName());
				P_Credits credits = P_Credits.get( Env.getCtx(), creditsMovement.getP_Credits_ID(), getTrxName());
				
				Timestamp tsMovementDate = null;
				if(m_Period == null)
				{
					P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - CreditsValidation - Periode invalide  ", getTrxName() );
					log.log( Level.SEVERE, "CreditsValidation - Periode invalide.");
					return false;					
				}
				else
				{
//					if(creditsMovement.getPMovementDate().before(m_Period.getStartDate()))
//						tsMovementDate = (Timestamp)m_Period.getStartDate().clone();
//					else
						tsMovementDate = (Timestamp)creditsMovement.getPMovementDate().clone();
				}

				if ( CreditsAlert(creditsMovement, credits, tsMovementDate ,  creditsMovement.getPMovementVariation(), false, null, false) ) 
				{
					error = true;
				}

			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
            P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName() );
            log.log( Level.SEVERE, "CreditsValidation - " + sql, e);
		}
		
		//log.log( Level.SEVERE,"Gross Calcul - CreditsValidation End " );
		return error;
	}

	private BigDecimal convertVariation( P_Credits creditsInfo, P_Credits_Param creditsParam, BigDecimal _variation)
	{
		BigDecimal variation = _variation ;
//		+ Conversion en Jours en Heures
		if ( creditsInfo.getCreditsContents().equals(  P_Credits.CREDITSCONTENTS_DaysConvertToHours ))
		{
			BigDecimal hoursForJRH_HRJ = Env.ZERO;

			P_Assignment_Param assignment_Param = P_Assignment_Param.get( Env.getCtx(), m_Employee.GetPrincipalAssignment_Param(), getTrxName());
			if(assignment_Param.getP_Assignment_ID() == 0)
			{
				P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "L'employé "+m_Employee.getValue()+" n'a pas d'affectation principale ", getTrxName());
				return Env.ZERO;
			}

			//pour certaines banques et certains employes faut utiliser les heures 
			//accumulees de l'affectation
			if(creditsParam.isCumulatedHours() == true && assignment_Param.getCumulatedHours().compareTo(Env.ZERO) > 0)
			{
				hoursForJRH_HRJ = assignment_Param.getCumulatedHours();
			}	
			else
			{
				hoursForJRH_HRJ = assignment_Param.getDay_Hours();
			}
			
		    variation = variation.multiply( hoursForJRH_HRJ );
		}
//		- Conversion en Jours en Heures									

		return variation;
	}

	
	private BigDecimal maximumValidation( P_Credits_Bound CreditsBound, P_Credits creditsInfo, BigDecimal Variation)
	{
		BigDecimal var = Variation;

		if ( CreditsBound.getMaximumVariationByYear() != null && CreditsBound.getMaximumVariationByYear().compareTo(Env.ZERO) != 0 )
		{
			BigDecimal bdSld = P_Credits.getEmployeeSolde( creditsInfo, m_Employee, m_eval_timestamp_result, getTrxName());

			
			bdSld = bdSld.add( Variation);
			
			if ( bdSld.compareTo(CreditsBound.getMaximumVariationByYear()) >= 0 )
			{
				var = CreditsBound.getMaximumVariationByYear().subtract( bdSld);
			}
			
		}
		return var;

	}
	
	//
	// Valide s'il y a des mouvement d'ouverture ou de fermeture a effectuer.
	//
	private boolean isCreditsAnnuel = false;
	private boolean[] isCreditsAnnuelInfo;
	
/*	
	//
	// Méthode pour amélioré la performance 
	//
	public void setCreditsInfoPerformance( int P_Period_ID )
	{
		
		String sql = null;
		sql = "Select P_Credits.P_Credits_ID From P_Credits, P_Credits_Param ";
		sql +="  Where P_Credits.IsActive = 'Y' AND P_Credits.P_Credits_ID = P_Credits_Param.P_Credits_ID";
//		sql +="    And isnull(CreditsAnnual, 0) != 0 ";	

		P_Credits_Param creditsParam;
		P_Credits       creditsInfo;
		
		PreparedStatement pstmt = null;
	    Timestamp CreditsDate;
 	    P_Period l_Period = P_Period.get( Env.getCtx(), P_Period_ID , getTrxName());

  	    int nbrDays = TimeUtil.getDaysBetween( l_Period.getStartDate() , l_Period.getEndDate() ) + 1;

 	    Timestamp Day = l_Period.getStartDate();
		 
		for (int j = 0; j < nbrDays ; j++)
		{
		    Day = TimeUtil.addDays( l_Period.getStartDate(), j );
			try
			{
				pstmt = DB.prepareStatement (sql, getTrxName());
				ResultSet rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					creditsParam    = P_Credits_Param.get( Env.getCtx(), rs.getInt("P_Credits_ID"), m_iP_Method_Credits_ID, m_Period.getStartDate(), getTrxName() );
					
					if ( creditsParam != null && creditsParam.getP_Credits_Param_ID() != 0 )
					{
						// type de date 2 et 3 sont variable pour chaque employé.
						creditsInfo     = P_Credits.get( Env.getCtx(), rs.getInt("P_Credits_ID"), getTrxName() );
					    if ( ! creditsInfo.getCreditsTypeDate().equals("1")  )
					    {
							isCreditsAnnuel = true;
							isCreditsAnnuelInfo[ TimeUtil.getDay_Of_Month(Day) ] = true;
					    }
					    else
					    {
						    CreditsDate = creditsParam.getCreditsDate( m_Period, null, getTrxName());
							
							if ( TimeUtil.isSameDay(CreditsDate, Day)  )
							{
								isCreditsAnnuel = true;
								isCreditsAnnuelInfo[ TimeUtil.getDay_Of_Month(Day) ] = true;
							}
					    }
					}
				}
			}
			catch (Exception e)
			{
	            P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName());
	            log.log( Level.SEVERE, "setCreditsInfoPerformance - " + sql, e);
			}

			sql = "Select P_Credits.P_Credits_ID From P_Credits, P_Credits_Param ";
			sql +="  Where P_Credits.IsActive = 'Y' AND P_Credits.P_Credits_ID = P_Credits_Param.P_Credits_ID";
//			sql +="    And IsNull( CreditsVariation, 0 ) != 0 ";
			try
			{
				pstmt = DB.prepareStatement (sql, getTrxName());
				ResultSet rs = pstmt.executeQuery ();
				if (rs.next ())
				{
					// variation fix
					isCreditsEvaluationFixe = true;
				}
			}
			catch (Exception e)
			{
	            P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName());
	            log.log( Level.SEVERE, "setCreditsInfoPerformance - " + sql, e);
			}
			
		}

	}
*/	
	public void CreditsVariationAnnuelOpen ( Timestamp Day ) 
	{
		//
		//
/*		if (  isCreditsAnnuel == false)
			return;

		if (  isCreditsAnnuelInfo[ TimeUtil.getDay_Of_Month(Day) ] == false)
			return;
*/			

		//BigDecimal variation2 = null;
		BigDecimal variation  = null;
		
		String sql = null;
		sql = "Select P_Employee_Credits_ID, P_Credits.P_Credits_ID, P_Method_Credits_ID "
				+ " From P_Employee_Credits"
				+ " INNER JOIN P_Credits ON P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID ";
		sql +="  Where P_Employee_Credits.IsActive = 'Y' ";
		sql +="    And P_Employee_Credits.P_Employee_ID = " + m_Employee.getP_Employee_ID();
		sql +="    And P_Credits.IsActive = 'Y' ";
//		ici		
//		sql +="    AND P_Employee_Credits.P_Credits_ID in (1001130 )";

		sql +="    And P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID ";
//		sql +="    And isnull(CreditsAnnual, 0) != 0 ";	
		
		sql +="    And NOT EXISTS( SELECT 1 FROM P_LONGTERMLEAVE_CREDITS WHERE P_LONGTERMLEAVE_CREDITS.P_Credits_ID = P_Credits.P_Credits_ID "
				+ " AND P_LONGTERMLEAVE_CREDITS.P_LONGTERMLEAVE_ID = "	+ m_Employee.getP_LongTermLeave_ID() + " )";

		
		P_Employee_Credits employeeCredits;
		P_Credits       creditsInfo;
		P_Credits_Param creditsParam;
		
		//log.log( Level.SEVERE,"Gross Calcul - CreditsEvaluationFixe " + sql );

		
		PreparedStatement pstmt = null;
	    Timestamp CreditsDate;

		try
		{
			pstmt = DB.prepareStatement (sql, getTrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				variation   = new BigDecimal(0);
//				variation2  = new BigDecimal(0);

				m_iP_Method_Credits_ID = rs.getInt("P_Method_Credits_ID");

				creditsParam    = P_Credits_Param.get( Env.getCtx(), rs.getInt("P_Credits_ID"), m_iP_Method_Credits_ID, m_Period.getStartDate(), getTrxName() );
				
				if ( creditsParam != null && creditsParam.getP_Credits_Param_ID() != 0 )
				{
				    CreditsDate = creditsParam.getCreditsDate( m_Period, m_Employee, getTrxName());

					employeeCredits = P_Employee_Credits.get(Env.getCtx (), rs.getInt("P_Employee_Credits_ID"), getTrxName());

					// 2026-09-30 on n'ajuste pas une variation si la banque début en même temps que l'emplyés
					if ( TimeUtil.isSameDay(CreditsDate, Day) && ! TimeUtil.isSameDay(CreditsDate, employeeCredits.getEffectIn() )  )
//					if ( TimeUtil.isSameDay(CreditsDate, Day)  )
					{

						creditsInfo     = P_Credits.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), getTrxName() );

						//on verifie si la relation employee credits en encore valide
						if(employeeCredits.getEffectTo() == null || CreditsDate.compareTo(employeeCredits.getEffectTo()) <= 0)
						{
							//log.log( Level.SEVERE, "Credits : " + CreditsInfo.getName() + " Date " + CreditsDate + " Between " +  m_Period.getStartDate() + " and " + m_Period.getEndDate() + " - " + creditsEval );  
					    	
							variation = new BigDecimal( 0 );
	
							//
							// Si il y a un crédit annuel initial a verser a la banque
							//
														
							//+ 2008-08-04 - le crédit annuel initial pour être variable selon une borne.
							P_Credits_Family_Mth_Var famVar = P_Credits_Family_Mth_Var.get(Env.getCtx (), creditsParam.getP_Credits_Family_Mth_Var_ID(), getTrxName());
							P_Credits_Mth_Variation mthVar  = P_Credits_Mth_Variation.get(Env.getCtx (), famVar.getP_Credits_Mth_Variation_ID(), getTrxName() );

							if ( mthVar.getValue().compareTo("004") == 0 ) // Variation selon borne
							{
/*								
								P_Credits[] creditsJoin = creditsParam.getP_Credits_Join(m_Employee.getP_Employee_ID());
								
								BigDecimal bdCumulSld = Env.ZERO;
								for(int idx = 0;idx<creditsJoin.length;idx++)
								{
									bdCumulSld = bdCumulSld.add(P_Credits.getEmployeeSolde( creditsJoin[idx], m_Employee, m_eval_timestamp_result, getTrxName() ));
								}
*/
								P_Credits creditsJoin = P_Credits.get( Env.getCtx(), creditsParam.getP_Credits_Join(), getTrxName());
								BigDecimal bdCumulSld = P_Credits.getEmployeeSolde( creditsJoin , m_Employee, m_eval_timestamp_result, getTrxName() );
								
								P_Credits_Bound CreditsBound = P_Credits_Bound.get( Env.getCtx (), employeeCredits.getP_Credits_ID(), m_iP_Method_Credits_ID ,m_eval_timestamp_result ,bdCumulSld , getTrxName() );
								if ( CreditsBound.getCreditsAnnual() != null && CreditsBound.getCreditsAnnual().compareTo(Env.ZERO) != 0 )
								{
									creditsParam.setCreditsAnnual( CreditsBound.getCreditsAnnual() );
								}
							}
							//-
								
							// Si il y a un crédit annuel initial a verser a la banque
							if ( creditsParam.getCreditsAnnual() != null && creditsParam.getCreditsAnnual().compareTo( new BigDecimal( 0 )) != 0)
							{
								P_Credits_Movement creditsMovement = P_Credits_Movement.get(Env.getCtx (), m_Payment.getP_Payment_ID(), employeeCredits.getP_Credits_ID(), CreditsDate, "BF", "FTP", getTrxName() ) ;
								
								creditsMovement.setP_Credits_ID( employeeCredits.getP_Credits_ID());
								creditsMovement.setP_Employee_ID( employeeCredits.getP_Employee_ID());
								creditsMovement.setPMovementDate( CreditsDate );
								creditsMovement.setPMovementOrigin( "FTP");
								creditsMovement.setPMovementType( "BO" );
								creditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());

								creditsMovement.setP_Method_Credits_ID(creditsParam.getP_Method_Credits_ID());
	//							CreditsMovement.setDescription( " ");
	
								variation = creditsParam.getCreditsAnnual().setScale(2, BigDecimal.ROUND_HALF_UP );
								variation = convertVariation( creditsInfo, creditsParam, variation);
								
								//
								// Si l'employé est en ARTT on applique le facteur au solde d'ouverture.
								//

								P_Assignment_Param assignment_Param = P_Assignment_Param.get( Env.getCtx (), m_Employee.GetPrincipalAssignment_Param(), getTrxName() );
								P_Assignment assignment = P_Assignment.get(Env.getCtx(), assignment_Param.getP_Assignment_ID(),getTrxName());
								if(creditsParam.isAffectedByRWT() == true && assignment.isRWT( CreditsDate ) == true)
								{
									BigDecimal rwtFactor = Env.ZERO;
									rwtFactor = assignment.getWeekly_HoursRWT( CreditsDate ).divide(assignment_Param.getWeekly_Hours(), 4, BigDecimal.ROUND_HALF_UP);
									variation = variation.multiply(rwtFactor);
									// 7 jul 2008
									variation = variation.setScale(2, BigDecimal.ROUND_HALF_UP);

								}


								if  (  creditsMovement.getPMovementVariation() != null )
								{
									creditsMovement.setPMovementVariation( creditsMovement.getPMovementVariation().add( variation ) );
								}
								else
								{
									creditsMovement.setPMovementVariation( variation );
								}
								creditsMovement.setP_Payment_ID( m_Payment.getP_Payment_ID() );
								creditsMovement.save();
								
							}
						}
					}		
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
            P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName());
            log.log( Level.SEVERE, "CreditsVariationAnnuel - " + sql, e);
		}
		
	}

	public void CreditsVariationAnnuelClose ( Timestamp Day ) 
	{
		CreditsVariationAnnuelClose( Day, 0 );
	}
	
	

    //
	// Variation annuel.
	//
	public void CreditsVariationAnnuelClose ( Timestamp Day, int P_Credits_ID ) 
	{

		//
		//
/*		if (  isCreditsAnnuel == false)
			return;

		if (  isCreditsAnnuelInfo[TimeUtil.getDay_Of_Month(Day) ] == false)
			return;
*/
		
		//
		//
		BigDecimal variation2 = null;
		BigDecimal variation  = null;
		
		String sql = null;
		sql = "Select P_Employee_Credits_ID, P_Credits.P_Credits_ID, P_Method_Credits_ID "
			+ " From P_Employee_Credits "
			+ "  INNER JOIN P_Credits ON P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID ";
		sql +="  Where P_Employee_Credits.IsActive = 'Y' ";
		sql +="    And P_Employee_Credits.P_Employee_ID = " + m_Employee.getP_Employee_ID();
		sql +="    And P_Credits.IsActive = 'Y' ";
		sql +="    And P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID ";

		if ( P_Credits_ID != 0)
			sql +=" and P_Credits.P_Credits_ID = " + P_Credits_ID;
		
		sql +="    And NOT EXISTS( SELECT 1 FROM P_LONGTERMLEAVE_CREDITS WHERE P_LONGTERMLEAVE_CREDITS.P_Credits_ID = P_Credits.P_Credits_ID "
			+ " AND P_LONGTERMLEAVE_CREDITS.P_LONGTERMLEAVE_ID = "	+ m_Employee.getP_LongTermLeave_ID() + " )";

		
		P_Employee_Credits employeeCredits;
		P_Credits       creditsInfo;
		P_Credits_Param creditsParam;
		
		//log.log( Level.SEVERE,"Gross Calcul - CreditsEvaluationFixe " + sql );

		
		PreparedStatement pstmt = null;
	    Timestamp CreditsDate;

		try
		{
			pstmt = DB.prepareStatement (sql, getTrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				variation   = new BigDecimal(0);
				variation2  = new BigDecimal(0);
				
				m_iP_Method_Credits_ID = rs.getInt("P_Method_Credits_ID");

				creditsParam    = P_Credits_Param.get( Env.getCtx(), rs.getInt("P_Credits_ID"), m_iP_Method_Credits_ID, m_Period.getStartDate(), getTrxName() );
				
				if ( creditsParam != null && creditsParam.getP_Credits_Param_ID() != 0 )
				{
				    CreditsDate = creditsParam.getCreditsDate( m_Period, m_Employee, getTrxName());
				
					if ( TimeUtilSolstice.isSameDay(CreditsDate, Day)  )
					{

						employeeCredits = P_Employee_Credits.get(Env.getCtx (), rs.getInt("P_Employee_Credits_ID"), getTrxName());
						creditsInfo     = P_Credits.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), getTrxName() );

						//on verifie si la relation employee credits en encore valide
						if(employeeCredits.getEffectTo() == null || CreditsDate.compareTo(employeeCredits.getEffectTo()) <= 0)
						{
							//log.log( Level.SEVERE, "Credits : " + CreditsInfo.getName() + " Date " + CreditsDate + " Between " +  m_Period.getStartDate() + " and " + m_Period.getEndDate() + " - " + creditsEval );  
					    	
							variation = new BigDecimal( 0 );
						    Timestamp CreditsDateEnd = TimeUtil.addDays( CreditsDate, -1);
	
	
							//log.log( Level.SEVERE,"Gross Calcul - CreditsVariationAnnuel = " + CreditsInfo.getValue() + " - " + CreditsInfo.getName() + " Method variation Annual" + CreditsParam.getMethod_Annual() );
	
							// 0 - Sold is Reseted
							if ( creditsParam.getMethod_Annual().equals( P_Credits_Param.METHOD_ANNUAL_SoldeIsReseted )) 
							{
								variation = new BigDecimal(0).subtract(P_Credits.getEmployeeSolde( creditsInfo, m_Employee, CreditsDateEnd, getTrxName() ));
							}
	
							// 1 - Sold is Perserved
							if ( creditsParam.getMethod_Annual().equals( P_Credits_Param.METHOD_ANNUAL_SoldIsPreserved )) 
							{
								;//on fait rien
							}
	
							// 2 - sold is transferred
							if ( creditsParam.getMethod_Annual().equals( P_Credits_Param.METHOD_ANNUAL_SoldIsTransferredInAOthersAccumulatedCredits )) 
							{
								// le solde est le solde de la banque -1 jours. 
//								Timestamp tempMoveDate = (Timestamp)CreditsDate.clone();
//								tempMoveDate = TimeUtil.addDays( tempMoveDate, -1);
//								tempMoveDate.setDate(tempMoveDate.getDate() - 1);
								variation2 = P_Credits.getEmployeeSolde( creditsInfo, m_Employee, CreditsDateEnd, getTrxName() );
								variation  = new BigDecimal(0).subtract(P_Credits.getEmployeeSolde( creditsInfo, m_Employee, CreditsDateEnd, getTrxName() ));
								if ( variation2.compareTo( Env.ZERO) < 0) 
								{
									variation2 = Env.ZERO;
									variation  = Env.ZERO;
								}

							}
	
							// 3 - Sold is pay
							if ( creditsParam.getMethod_Annual().equals( P_Credits_Param.METHOD_ANNUAL_SoldIsPay )) 
							{
	
							    if ( creditsParam.getGainAnnual() == 0 )
	   						    {
							        String message = Msg.getMsg(Env.getCtx(), "GainAnnualRequired"); 
									P_Time_Sheet_Error.NewMessage( creditsInfo, m_TimeSheet, P_Credits_Alert.ALERT_SEVERITY_LEVEL_Error , message, getTrxName() );
	   						    }
							    else
							    {
							    	variation  = new BigDecimal(0).subtract(P_Credits.getEmployeeSolde( creditsInfo, m_Employee, CreditsDateEnd, getTrxName() ));
					                
							    	if ( variation.compareTo(Env.ZERO) > 0)
							    		variation = Env.ZERO;
							    	
					                P_Gain Gain = P_Gain.get( Env.getCtx(), creditsParam.getGainAnnual(), getTrxName());
	
									int line = m_Payment.GetNextLine ( );
	
					               // On créer une ligne dans la feuille de temps. 
								   // Il serait peut-etre possible de penser de se servire d'une table qui servira a la prochaine génération de temps........			   //
									
									P_Assignment_Param assignment_Param = P_Assignment_Param.get( Env.getCtx (), m_Employee.GetPrincipalAssignment_Param(), getTrxName() );
	
									P_Payment_Gain PaymentGain = new P_Payment_Gain(Env.getCtx (), -1, getTrxName());
									PaymentGain.setP_Payment_ID( m_Payment.getP_Payment_ID() );
									PaymentGain.setP_Employee_ID( m_Payment.getP_Employee_ID());
									
									PaymentGain.setP_Assignment_ID( assignment_Param.getP_Assignment_ID() );
									PaymentGain.setP_Assignment_Param_ID( assignment_Param.getP_Assignment_Param_ID());
									PaymentGain.setP_Gain_ID( creditsParam.getGainAnnual() );
									PaymentGain.setHourly_Rate( assignment_Param.getHourly_Rate()  );
									
									P_Assignment Assignment = P_Assignment.get( Env.getCtx(),assignment_Param.getP_Assignment_ID(), getTrxName());

									if ( ! assignment_Param.isOut_Of_Range() )
									{
										PaymentGain.setP_Job_Title_ID(assignment_Param.getP_Job_Title_ID());
										PaymentGain.setP_Collective_Labour_Agr_ID( assignment_Param.getP_Collective_Labour_Agr_ID());
										PaymentGain.setP_Salary_Scale_Detail_ID( assignment_Param.getP_Salary_Scale_Detail_ID());
										PaymentGain.setP_Salary_Scale_ID( assignment_Param.getP_Salary_Scale_ID());
									}
									else
									{
										P_Employee Employee = P_Employee.get( Env.getCtx (), Assignment.getP_Employee_ID(), getTrxName() );
										PaymentGain.setP_Job_Title_ID(Employee.getP_Job_Title_ID());
										PaymentGain.setP_Collective_Labour_Agr_ID( Employee.getP_Collective_Labour_Agr_ID());
									}
									PaymentGain.setOriginTime("PBQ");
									int weekNumber = (TimeUtil.getDaysBetween( m_Period.getStartDate(), CreditsDate) /7 ) +1;
									PaymentGain.setWeekIndex( weekNumber);
									PaymentGain.setP_UOM_ID( Gain.getP_UOM_ID());
									PaymentGain.setP_Post_ID( Assignment.getP_Post_ID());
									
									P_Gain_Parameter GainParameter = P_Gain_Parameter.get( Env.getCtx (), Gain.getP_Gain_ID(), assignment_Param.getP_Collective_Labour_Agr_ID(), getTrxName() ); 

									PaymentGain.setP_Gain_Parameter_ID( GainParameter.getP_Gain_Parameter_ID());
									PaymentGain.setMultiplyRate( GainParameter.getMultiplyRate());
									PaymentGain.setType_Rate( Gain.getType_Rate() );
									
									if ( ! creditsInfo.getCreditsContents().equals( P_Credits.CREDITSCONTENTS_Amount))
									{
										PaymentGain.setQuantity(  new BigDecimal(0).subtract(variation ));
										PaymentGain.setQuantityCalc(  new BigDecimal(0).subtract(variation ));
										PaymentGain.setAmountCalc( PaymentGain.getQuantityCalc().multiply( PaymentGain.getHourly_Rate()) );
									}
									else
									{
										PaymentGain.setQuantity( new BigDecimal(0).subtract(variation) );

										if ( creditsParam.isVariationVacationRate() ) // "3320"										if (creditsInfo.getValue().equals("3320"))
											PaymentGain.setQuantity( (new BigDecimal(0).subtract(variation)).multiply( this.m_Employee.getPrevVacationRate().divide(new BigDecimal(100) ) ) );

//										PaymentGain.setQuantity( new BigDecimal(0) );
//										PaymentGain.setFixed_Amount( new BigDecimal(0).subtract(variation) );
//										PaymentGain.setAmountCalc( new BigDecimal(0).subtract(variation));
									}
									PaymentGain.setLine( line );
									PaymentGain.setP_Year_ID( m_Payment.getP_Year_ID()  );
									PaymentGain.setP_Period_ID( m_Payment.getP_Period_ID() );
									PaymentGain.setDay( CreditsDate );
									PaymentGain.save();

								    // 
								    // TODO Si l'on paie une banque on devrait recalculer tout les gains a % sur d'autre gain.
								    //
									// Génération de l'imputation comptable.
								    TimeValidation val = new TimeValidation( m_TimeSheet , getTrxName());

							        val.calcul_gain( PaymentGain );

							        //2010.05.11
							        this.CreditsEvaluationTransaction( PaymentGain, CreditsDate   );

								  //  val.Create_PaymentGainDistribution(m_Payment, PaymentGain.getP_Payment_Gain_ID()) ;
							        
							    }
							}
							
							//
							// Ajoute la quantité a ajouter a chaque début d'année
							//
							//variation2 = variation2.add( creditsParam.getCreditsAnnual());
							
	
							//log.log( Level.SEVERE,"Gross Calcul - CreditsVariation = " + CreditsInfo.getValue() + " Method variation Annual" + CreditsParam.getMethod_Annual() + " variation " + Variation );
	
							if ( variation.compareTo( new BigDecimal( 0 )) != 0 )
							{
								P_Credits_Movement creditsMovement = P_Credits_Movement.get(Env.getCtx (), m_Payment.getP_Payment_ID(), employeeCredits.getP_Credits_ID(), CreditsDate, "BF", "FTP", getTrxName() ) ;
	
								creditsMovement.setP_Credits_ID( employeeCredits.getP_Credits_ID());
								creditsMovement.setP_Employee_ID( employeeCredits.getP_Employee_ID());
								//les BF sont fait la veille de la journée d'anniversaire
								Timestamp tempMoveDate = (Timestamp)CreditsDate.clone();
								tempMoveDate = TimeUtil.addDays( tempMoveDate, -1);
//								tempMoveDate.setDate(tempMoveDate.getDate() - 1);
								creditsMovement.setPMovementDate( tempMoveDate );
								creditsMovement.setPMovementOrigin( "FTP");
								creditsMovement.setPMovementType( "BF" );
								creditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());

								creditsMovement.setP_Method_Credits_ID(creditsParam.getP_Method_Credits_ID());
	//							CreditsMovement.setDescription( " ");
	
								variation = variation.setScale(2, BigDecimal.ROUND_HALF_UP );
	
								if  (  creditsMovement.getPMovementVariation() != null )
								{
									creditsMovement.setPMovementVariation( creditsMovement.getPMovementVariation().add( variation ) );
								}
								else
								{
									creditsMovement.setPMovementVariation( variation );
								}
								creditsMovement.setP_Payment_ID( m_Payment.getP_Payment_ID() );
								creditsMovement.save();
							}

/*							
							//
							// Si il y a un crédit annuel initial a verser a la banque
							//
							if ( creditsParam.getCreditsAnnual() != null && creditsParam.getCreditsAnnual().compareTo( new BigDecimal( 0 )) != 0)
							{
								P_Credits_Movement creditsMovement = P_Credits_Movement.get(Env.getCtx (), m_Payment.getP_Payment_ID(), employeeCredits.getP_Credits_ID(), CreditsDate, "BF", "FTP", getTrxName() ) ;
								
								creditsMovement.setP_Credits_ID( employeeCredits.getP_Credits_ID());
								creditsMovement.setP_Employee_ID( employeeCredits.getP_Employee_ID());
								creditsMovement.setPMovementDate( CreditsDate );
								creditsMovement.setPMovementOrigin( "FTP");
								creditsMovement.setPMovementType( "BO" );
								creditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());

								creditsMovement.setP_Method_Credits_ID(creditsParam.getP_Method_Credits_ID());
	//							CreditsMovement.setDescription( " ");
	
								variation = creditsParam.getCreditsAnnual().setScale(2, BigDecimal.ROUND_HALF_UP );
								variation = convertVariation( creditsInfo, creditsParam, variation);
								
								//
								// Si l'employé est en ARTT on applique le facteur au solde d'ouverture.
								//

								P_Assignment_Param assignment_Param = P_Assignment_Param.get( Env.getCtx (), m_Employee.GetPrincipalAssignment_Param(), getTrxName() );
								P_Assignment assignment = P_Assignment.get(Env.getCtx(), assignment_Param.getP_Assignment_ID(),getTrxName());
								if(creditsParam.isAffectedByRWT() == true && assignment.isRWT( CreditsDate ) == true)
								{
									BigDecimal rwtFactor = Env.ZERO;
									rwtFactor = assignment.getWeekly_HoursRWT( CreditsDate ).divide(assignment_Param.getWeekly_Hours(), 4, BigDecimal.ROUND_HALF_UP);
									variation = variation.multiply(rwtFactor);
								}


								if  (  creditsMovement.getPMovementVariation() != null )
								{
									creditsMovement.setPMovementVariation( creditsMovement.getPMovementVariation().add( variation ) );
								}
								else
								{
									creditsMovement.setPMovementVariation( variation );
								}
								creditsMovement.setP_Payment_ID( m_Payment.getP_Payment_ID() );
								creditsMovement.save();
								
							}
*/
							
							if ( variation2.compareTo( new BigDecimal( 0 )) != 0 )
							{
								P_Credits_Movement creditsMovement = P_Credits_Movement.get(Env.getCtx (), m_Payment.getP_Payment_ID(), creditsParam.getP_Credits_Target(), CreditsDate, "BO", "FTP", getTrxName() ) ;
	
								P_Employee_Credits employeeCreditsTarget = P_Employee_Credits.get( Env.getCtx(), employeeCredits.getP_Employee_ID(), creditsParam.getP_Credits_Target(), CreditsDate, getTrxName()  ); 
								if ( employeeCreditsTarget == null )
								{
								    employeeCreditsTarget = new P_Employee_Credits( Env.getCtx(), -1, getTrxName());
								    employeeCreditsTarget.setP_Credits_ID( creditsParam.getP_Credits_Target());
								    employeeCreditsTarget.setP_Employee_ID(employeeCredits.getP_Employee_ID());
								    employeeCreditsTarget.setEffectIn( CreditsDate );
								    //TODO avoir une valeur par default
								    employeeCreditsTarget.setP_Method_Credits_ID(1000001);
								    employeeCreditsTarget.save();
								    
								    P_Credits CreditsTarget = P_Credits.get( Env.getCtx(), creditsParam.getP_Credits_Target(), getTrxName() );
							        String message = Msg.getMsg(Env.getCtx(), "CreditsTargetCreated"); 
									P_Time_Sheet_Error.NewMessage( CreditsTarget, m_TimeSheet, P_Credits_Alert.ALERT_SEVERITY_LEVEL_Information, message, getTrxName() );
								}
								if ( creditsParam.getP_Credits_Target() == 0 )
								{
							        String message = Msg.getMsg(Env.getCtx(), "CreditsTargetRequired"); 
									P_Time_Sheet_Error.NewMessage( creditsInfo, m_TimeSheet, P_Credits_Alert.ALERT_SEVERITY_LEVEL_Error , message, getTrxName() );
								}
								else
								{
									creditsMovement.setP_Credits_ID( creditsParam.getP_Credits_Target());
									creditsMovement.setP_Employee_ID( employeeCredits.getP_Employee_ID());
									creditsMovement.setPMovementDate( CreditsDate );
									creditsMovement.setPMovementOrigin( "FTP");
									creditsMovement.setPMovementType( "BO" );
									creditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());

	//								CreditsMovement.setDescription( " ");
	
									variation = variation2.setScale(2, BigDecimal.ROUND_HALF_UP );
									// + Conversion  
//									variation = convertVariation( creditsInfo, creditsParam, variation);
									
									if  (  creditsMovement.getPMovementVariation() != null )
									{
										creditsMovement.setPMovementVariation( creditsMovement.getPMovementVariation().add( variation ) );
									}
									else
									{
										creditsMovement.setPMovementVariation( variation );
									}
									creditsMovement.setP_Payment_ID( m_Payment.getP_Payment_ID() );
									creditsMovement.save();							    
								}							
							}
						}
					}		
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
            P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName());
            log.log( Level.SEVERE, "CreditsVariationAnnuel - " + sql, e);
		}
		
	}

	//
	// Variation annuel.
	//
	public void CreditsVariationAnnuel( Timestamp Day ) 
	{
		
		BigDecimal variation2 = null;
		BigDecimal variation  = null;
		
		String sql = null;
		sql = "Select P_Employee_Credits_ID, P_Method_Credits_ID From P_Employee_Credits, P_Credits ";
		sql +="  Where P_Employee_Credits.IsActive = 'Y' ";
		sql +="    And P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID " ;
		sql +="    And P_Employee_Credits.P_Employee_ID = " + m_Employee.getP_Employee_ID();
		sql +="    And P_Credits.IsActive = 'Y' ";
		sql +="    And P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID ";

		//2025-05-05
		sql +="    And NOT EXISTS( SELECT 1 FROM P_LONGTERMLEAVE_CREDITS WHERE P_LONGTERMLEAVE_CREDITS.P_Credits_ID = P_Credits.P_Credits_ID "
				+ " AND P_LONGTERMLEAVE_CREDITS.P_LONGTERMLEAVE_ID = "	+ m_Employee.getP_LongTermLeave_ID() + " )";
		
		P_Employee_Credits employeeCredits;
		P_Credits       creditsInfo;
		P_Credits_Param creditsParam;
		
		//log.log( Level.SEVERE,"Gross Calcul - CreditsEvaluationFixe " + sql );

		
		PreparedStatement pstmt = null;
	    Timestamp CreditsDate;

		try
		{
			pstmt = DB.prepareStatement (sql, getTrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				variation   = new BigDecimal(0);
				variation2  = new BigDecimal(0);
				
				employeeCredits = P_Employee_Credits.get(Env.getCtx (), rs.getInt(1), getTrxName());
				creditsInfo     = P_Credits.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), getTrxName() );

				m_iP_Method_Credits_ID = rs.getInt("P_Method_Credits_ID");

				creditsParam    = P_Credits_Param.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), m_iP_Method_Credits_ID, m_Period.getStartDate(), getTrxName() );
				
				if ( creditsParam != null && creditsParam.getP_Credits_Param_ID() != 0 )
				{
				    CreditsDate = creditsParam.getCreditsDate( m_Period, m_Employee, getTrxName());
				
					if ( TimeUtil.isSameDay(CreditsDate, Day)  )
					{
						
						//on verifie si la relation employee credits en encore valide
						if(employeeCredits.getEffectTo() == null || CreditsDate.compareTo(employeeCredits.getEffectTo()) <= 0)
						{
							//log.log( Level.SEVERE, "Credits : " + CreditsInfo.getName() + " Date " + CreditsDate + " Between " +  m_Period.getStartDate() + " and " + m_Period.getEndDate() + " - " + creditsEval );  
					    	
							variation = new BigDecimal( 0 );
	
	
							//log.log( Level.SEVERE,"Gross Calcul - CreditsVariationAnnuel = " + CreditsInfo.getValue() + " - " + CreditsInfo.getName() + " Method variation Annual" + CreditsParam.getMethod_Annual() );
	
							// 0 - Sold is Reseted
							if ( creditsParam.getMethod_Annual().equals( P_Credits_Param.METHOD_ANNUAL_SoldeIsReseted )) 
							{
								variation = new BigDecimal(0).subtract(P_Credits.getEmployeeSolde( creditsInfo, m_Employee, CreditsDate, getTrxName() ));
							}
	
							// 1 - Sold is Perserved
							if ( creditsParam.getMethod_Annual().equals( P_Credits_Param.METHOD_ANNUAL_SoldIsPreserved )) 
							{
								;//on fait rien
							}
	
							// 2 - sold is transferred
							if ( creditsParam.getMethod_Annual().equals( P_Credits_Param.METHOD_ANNUAL_SoldIsTransferredInAOthersAccumulatedCredits )) 
							{
								// le solde est le solde de la banque -1 jours. 
//								Timestamp tempMoveDate = (Timestamp)CreditsDate.clone();
//								tempMoveDate = TimeUtil.addDays( tempMoveDate, -1);
								
//								tempMoveDate.setDate(tempMoveDate.getDate() - 1);
								variation2 = P_Credits.getEmployeeSolde( creditsInfo, m_Employee, CreditsDate, getTrxName() );
								variation  = new BigDecimal(0).subtract(P_Credits.getEmployeeSolde( creditsInfo, m_Employee, CreditsDate, getTrxName() ));
							}
	
							// 3 - Sold is pay
							if ( creditsParam.getMethod_Annual().equals( P_Credits_Param.METHOD_ANNUAL_SoldIsPay )) 
							{
	
							    if ( creditsParam.getGainAnnual() == 0 )
	   						    {
							        String message = Msg.getMsg(Env.getCtx(), "GainAnnualRequired"); 
									P_Time_Sheet_Error.NewMessage( creditsInfo, m_TimeSheet, P_Credits_Alert.ALERT_SEVERITY_LEVEL_Error , message, getTrxName() );
	   						    }
							    else
							    {
							    	variation  = new BigDecimal(0).subtract(P_Credits.getEmployeeSolde( creditsInfo, m_Employee, CreditsDate, getTrxName() ));
					                
					                P_Gain Gain = P_Gain.get( Env.getCtx(), creditsParam.getGainAnnual(), getTrxName());
	
									int line = m_Payment.GetNextLine ( );
	
					               // On créer une ligne dans la feuille de temps. 
								   // Il serait peut-etre possible de penser de se servire d'une table qui servira a la prochaine génération de temps........			   //
									
									P_Assignment_Param assignment_Param = P_Assignment_Param.get( Env.getCtx (), m_Employee.GetPrincipalAssignment_Param(), getTrxName() );
	
									P_Payment_Gain PaymentGain = new P_Payment_Gain(Env.getCtx (), -1, getTrxName());
									PaymentGain.setP_Payment_ID( m_Payment.getP_Payment_ID() );
									PaymentGain.setP_Employee_ID( m_Payment.getP_Employee_ID());
									
									PaymentGain.setP_Assignment_ID( assignment_Param.getP_Assignment_ID() );
									PaymentGain.setP_Assignment_Param_ID( assignment_Param.getP_Assignment_Param_ID());
									PaymentGain.setP_Gain_ID( creditsParam.getGainAnnual() );
									PaymentGain.setHourly_Rate( assignment_Param.getHourly_Rate()  );
									
									P_Assignment Assignment = P_Assignment.get( Env.getCtx(),assignment_Param.getP_Assignment_ID(), getTrxName());

									if ( ! assignment_Param.isOut_Of_Range() )
									{
										PaymentGain.setP_Job_Title_ID(assignment_Param.getP_Job_Title_ID());
										PaymentGain.setP_Collective_Labour_Agr_ID( assignment_Param.getP_Collective_Labour_Agr_ID());
										PaymentGain.setP_Salary_Scale_Detail_ID( assignment_Param.getP_Salary_Scale_Detail_ID());
										PaymentGain.setP_Salary_Scale_ID( assignment_Param.getP_Salary_Scale_ID());
									}
									else
									{
										P_Employee Employee = P_Employee.get( Env.getCtx (), Assignment.getP_Employee_ID(), getTrxName() );
										PaymentGain.setP_Job_Title_ID(Employee.getP_Job_Title_ID());
										PaymentGain.setP_Collective_Labour_Agr_ID( Employee.getP_Collective_Labour_Agr_ID());
									}
									PaymentGain.setOriginTime("PBQ");
									int weekNumber = (TimeUtil.getDaysBetween( m_Period.getStartDate(), CreditsDate) /7 ) +1;
									PaymentGain.setWeekIndex( weekNumber);
									PaymentGain.setP_UOM_ID( Gain.getP_UOM_ID());
									PaymentGain.setP_Post_ID( Assignment.getP_Post_ID());
									
									P_Gain_Parameter GainParameter = P_Gain_Parameter.get( Env.getCtx (), Gain.getP_Gain_ID(), assignment_Param.getP_Collective_Labour_Agr_ID(), getTrxName() ); 

									PaymentGain.setP_Gain_Parameter_ID( GainParameter.getP_Gain_Parameter_ID());
									PaymentGain.setMultiplyRate( GainParameter.getMultiplyRate());
									PaymentGain.setType_Rate( Gain.getType_Rate() );
									
									if ( ! creditsInfo.getCreditsContents().equals( P_Credits.CREDITSCONTENTS_Amount))
									{
										PaymentGain.setQuantity(  new BigDecimal(0).subtract(variation ));
										PaymentGain.setQuantityCalc(  new BigDecimal(0).subtract(variation ));
										PaymentGain.setAmountCalc( PaymentGain.getQuantityCalc().multiply( PaymentGain.getHourly_Rate()) );
									}
									else
									{
										PaymentGain.setQuantity( new BigDecimal(0) );
										PaymentGain.setFixed_Amount( new BigDecimal(0).subtract(variation) );
										PaymentGain.setAmountCalc( new BigDecimal(0).subtract(variation));
									}
									PaymentGain.setLine( line );
									PaymentGain.setP_Year_ID( m_Payment.getP_Year_ID()  );
									PaymentGain.setP_Period_ID( m_Payment.getP_Period_ID() );
									PaymentGain.setDay( CreditsDate );
									PaymentGain.save();

								    // 
								    // TODO Si l'on paie une banque on devrait recalculer tout les gains a % sur d'autre gain.
								    //
									// Génération de l'imputation comptable.
								    TimeValidation val = new TimeValidation( m_TimeSheet , getTrxName());

							        val.calcul_gain( PaymentGain );

								  //  val.Create_PaymentGainDistribution(m_Payment, PaymentGain.getP_Payment_Gain_ID()) ;
							        
							    }
							}
							
							//
							// Ajoute la quantité a ajouter a chaque début d'année
							//
							//variation2 = variation2.add( creditsParam.getCreditsAnnual());
							
	
							//log.log( Level.SEVERE,"Gross Calcul - CreditsVariation = " + CreditsInfo.getValue() + " Method variation Annual" + CreditsParam.getMethod_Annual() + " variation " + Variation );
	
							if ( variation.compareTo( new BigDecimal( 0 )) != 0 )
							{
								P_Credits_Movement creditsMovement = P_Credits_Movement.get(Env.getCtx (), m_Payment.getP_Payment_ID(), employeeCredits.getP_Credits_ID(), CreditsDate, "BF", "FTP", getTrxName() ) ;
	
								creditsMovement.setP_Credits_ID( employeeCredits.getP_Credits_ID());
								creditsMovement.setP_Employee_ID( employeeCredits.getP_Employee_ID());
								//les BF sont fait la veille de la journée d'anniversaire
								Timestamp tempMoveDate = (Timestamp)CreditsDate.clone();
								tempMoveDate = TimeUtil.addDays( tempMoveDate, -1);
//								tempMoveDate.setDate(tempMoveDate.getDate() - 1);
								creditsMovement.setPMovementDate( tempMoveDate );
								creditsMovement.setPMovementOrigin( "FTP");
								creditsMovement.setPMovementType( "BF" );
								creditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());

								creditsMovement.setP_Method_Credits_ID(creditsParam.getP_Method_Credits_ID());
	//							CreditsMovement.setDescription( " ");
	
								variation = variation.setScale(2, BigDecimal.ROUND_HALF_UP );
	
								if  (  creditsMovement.getPMovementVariation() != null )
								{
									creditsMovement.setPMovementVariation( creditsMovement.getPMovementVariation().add( variation ) );
								}
								else
								{
									creditsMovement.setPMovementVariation( variation );
								}
								creditsMovement.setP_Payment_ID( m_Payment.getP_Payment_ID() );
								creditsMovement.save();
							}
							//
							// Si il y a un crédit annuel initial a verser a la banque
							//
							if ( creditsParam.getCreditsAnnual() != null && creditsParam.getCreditsAnnual().compareTo( new BigDecimal( 0 )) != 0)
							{
								P_Credits_Movement creditsMovement = P_Credits_Movement.get(Env.getCtx (), m_Payment.getP_Payment_ID(), employeeCredits.getP_Credits_ID(), CreditsDate, "BF", "FTP", getTrxName() ) ;
								
								creditsMovement.setP_Credits_ID( employeeCredits.getP_Credits_ID());
								creditsMovement.setP_Employee_ID( employeeCredits.getP_Employee_ID());
								creditsMovement.setPMovementDate( CreditsDate );
								creditsMovement.setPMovementOrigin( "FTP");
								creditsMovement.setPMovementType( "BO" );
								creditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());

								creditsMovement.setP_Method_Credits_ID(creditsParam.getP_Method_Credits_ID());
	//							CreditsMovement.setDescription( " ");
	
								variation = creditsParam.getCreditsAnnual().setScale(2, BigDecimal.ROUND_HALF_UP );
								variation = convertVariation( creditsInfo, creditsParam, variation);
								
								//
								// Si l'employé est en ARTT on applique le facteur au solde d'ouverture.
								//

								P_Assignment_Param assignment_Param = P_Assignment_Param.get( Env.getCtx (), m_Employee.GetPrincipalAssignment_Param(), getTrxName() );
								P_Assignment assignment = P_Assignment.get(Env.getCtx(), assignment_Param.getP_Assignment_ID(),getTrxName());
								if(creditsParam.isAffectedByRWT() == true && assignment.isRWT( CreditsDate ) == true)
								{
									BigDecimal rwtFactor = Env.ZERO;
									rwtFactor = assignment.getWeekly_HoursRWT( CreditsDate ).divide(assignment_Param.getWeekly_Hours(), 4, BigDecimal.ROUND_HALF_UP);
									variation = variation.multiply(rwtFactor);
									// 7 jul 2008
									variation = variation.setScale(2, BigDecimal.ROUND_HALF_UP);

								}


								if  (  creditsMovement.getPMovementVariation() != null )
								{
									creditsMovement.setPMovementVariation( creditsMovement.getPMovementVariation().add( variation ) );
								}
								else
								{
									creditsMovement.setPMovementVariation( variation );
								}
								creditsMovement.setP_Payment_ID( m_Payment.getP_Payment_ID() );
								creditsMovement.save();
								
							}

							
							if ( variation2.compareTo( new BigDecimal( 0 )) != 0 )
							{
								P_Credits_Movement creditsMovement = P_Credits_Movement.get(Env.getCtx (), m_Payment.getP_Payment_ID(), creditsParam.getP_Credits_Target(), CreditsDate, "BO", "FTP", getTrxName() ) ;
	
								P_Employee_Credits employeeCreditsTarget = P_Employee_Credits.get( Env.getCtx(), employeeCredits.getP_Employee_ID(), creditsParam.getP_Credits_Target(), CreditsDate, getTrxName()  ); 
								if ( employeeCreditsTarget == null )
								{
								    employeeCreditsTarget = new P_Employee_Credits( Env.getCtx(), -1, getTrxName());
								    employeeCreditsTarget.setP_Credits_ID( creditsParam.getP_Credits_Target());
								    employeeCreditsTarget.setP_Employee_ID(employeeCredits.getP_Employee_ID());
								    employeeCreditsTarget.setEffectIn( CreditsDate );
								    //TODO avoir une valeur par default
								    employeeCreditsTarget.setP_Method_Credits_ID(1000001);
								    employeeCreditsTarget.save();
								    
								    P_Credits CreditsTarget = P_Credits.get( Env.getCtx(), creditsParam.getP_Credits_Target(), getTrxName() );
							        String message = Msg.getMsg(Env.getCtx(), "CreditsTargetCreated"); 
									P_Time_Sheet_Error.NewMessage( CreditsTarget, m_TimeSheet, P_Credits_Alert.ALERT_SEVERITY_LEVEL_Information, message, getTrxName() );
								}
								if ( creditsParam.getP_Credits_Target() == 0 )
								{
							        String message = Msg.getMsg(Env.getCtx(), "CreditsTargetRequired"); 
									P_Time_Sheet_Error.NewMessage( creditsInfo, m_TimeSheet, P_Credits_Alert.ALERT_SEVERITY_LEVEL_Error , message, getTrxName() );
								}
								else
								{
									creditsMovement.setP_Credits_ID( creditsParam.getP_Credits_Target());
									creditsMovement.setP_Employee_ID( employeeCredits.getP_Employee_ID());
									creditsMovement.setPMovementDate( CreditsDate );
									creditsMovement.setPMovementOrigin( "FTP");
									creditsMovement.setPMovementType( "BO" );
									creditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());

	//								CreditsMovement.setDescription( " ");
	
									variation = variation2.setScale(2, BigDecimal.ROUND_HALF_UP );
									// + Conversion  
//									variation = convertVariation( creditsInfo, creditsParam, variation);
									
									if  (  creditsMovement.getPMovementVariation() != null )
									{
										creditsMovement.setPMovementVariation( creditsMovement.getPMovementVariation().add( variation ) );
									}
									else
									{
										creditsMovement.setPMovementVariation( variation );
									}
									creditsMovement.setP_Payment_ID( m_Payment.getP_Payment_ID() );
									creditsMovement.save();							    
								}							
							}
						}
					}		
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
            P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName());
            log.log( Level.SEVERE, "CreditsVariationAnnuel - " + sql, e);
		}
		
	}


	//
	// Génération des alerts
	//
	public boolean CreditsAlert(P_Credits_Movement credits_movement, P_Credits credits, Timestamp d, BigDecimal variation , boolean bFromWeb)
	{
		return CreditsAlert(credits_movement, credits, d, variation , bFromWeb, null, false);
	}
	public boolean CreditsAlert(P_Credits_Movement credits_movement, P_Credits credits, Timestamp d, BigDecimal variation , boolean bFromWeb, BigDecimal soldePrecedent, boolean recursifCall ) 
	{
		
		//var pour conserver le surplus dépassant de la limite
		BigDecimal reminder = Env.ZERO;
		//var pour conserver le solde des banques liées
		BigDecimal sldAutre = Env.ZERO;

		BigDecimal solde = new BigDecimal(0);
		
		//solde = P_Credits.getEmployeeSolde( Credits, m_Employee, d ).add( variation );
		//le mouvement courant est deja dans le solde
		solde = P_Credits.getEmployeeSolde( credits, m_Employee, d, getTrxName() );
		if(bFromWeb == true && recursifCall == false)
			//solde = solde.add(variation);
			solde = soldePrecedent.add(variation);
		
		boolean bAlert = false;

		String condition = new String( "=" );
		
		
		String sql = null;
		String query = null;
//		sql = "Select Alert_Limit, Alert_Condition, Alert_Message, Alert_Severity_Level from P_Credits_Alert WHERE IsActive='Y' ";
		
		sql = "Select P_Credits_Alert_ID From P_Credits_Alert WHERE IsActive='Y' ";
		sql += " AND P_Credits_ID=" + credits.getP_Credits_ID();
		sql += " AND P_Method_Credits_ID in(100," + m_iP_Method_Credits_ID +")";
		
		
		P_Employee Employee = P_Employee.get(Env.getCtx(), credits_movement.getP_Employee_ID(), null );
		sql += " AND ( P_JOB_TYPE_ID IS NULL OR P_JOB_TYPE_ID =  " + Employee.getP_Job_Type_ID() + " ) " ; 
		//
		//log.log( Level.SEVERE, "P_Credits Validation - Sql - " + sql );
		
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;

		
		try
		{
			pstmt = DB.prepareStatement (sql, getTrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				P_Credits_Alert creditsAlert = P_Credits_Alert.get( Env.getCtx (), rs.getInt(1), getTrxName());
				
				//pour certain cas, on additionne le solde d'une autre banque a celui de la banque courante pour la validation
				//si on est pas en chainage, mais qu'on a une banque lié, on l'utilise pour la validation
				if(creditsAlert.getAlert_Severity_Level().equals("E") || creditsAlert.getAlert_Severity_Level().equals("W"))
				{
					if(creditsAlert.getP_Credits_Join_ID() != 0)
					{
						P_Credits linkedCredits = P_Credits.get(Env.getCtx(), creditsAlert.getP_Credits_Join_ID(), getTrxName());
						if(linkedCredits == null)
						{
							String message = "La banque liée à la limite de la banque "+credits.getValue()+" n'est pas valide";
							m_strLastAlert = message;
							if(bFromWeb == false)
								P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, message, getTrxName() );
						}
						else
						{
							sldAutre = P_Credits.getEmployeeSolde( linkedCredits, m_Employee, d, getTrxName() );
						}
					}
				}

				switch ( Integer.parseInt( creditsAlert.getAlert_Condition() ) ) 
				{ 
					case 1: condition = "<";
							//if(solde.compareTo(creditsAlert.getAlert_Limit()) == -1)
							//correction pour les banques de verifications (sans chainages)
				    //cette verification se fait juste pour les limites minimum
				    //pas de limites maximum supportés pour l'instant

							if(solde.add(sldAutre).compareTo(creditsAlert.getAlert_Limit()) == -1)
							{
								reminder = creditsAlert.getAlert_Limit().subtract(solde);
								//le reste de l'opération ne peut être plus gros que l'opération elle meme
								if(reminder.abs().compareTo(variation.abs()) == 1 && bFromWeb == false)
									reminder = variation;
							}
					break;
					case 2: condition = "<=";
						//if(solde.compareTo(creditsAlert.getAlert_Limit()) == -1 || solde.compareTo(creditsAlert.getAlert_Limit()) == 0)
						//correction pour les banques de verifications (sans chainages)
					    //cette verification se fait juste pour les limites minimum
					    //pas de limites maximum supportés pour l'instant
							if(solde.add(sldAutre).compareTo(creditsAlert.getAlert_Limit()) == -1 || solde.compareTo(creditsAlert.getAlert_Limit()) == 0)
								reminder = creditsAlert.getAlert_Limit().subtract(solde);			
					break;
					case 3: condition = "<>";
						if(solde.compareTo(creditsAlert.getAlert_Limit()) != 0)
						{
							reminder = creditsAlert.getAlert_Limit().subtract(solde);
							//si remider est negatif, on le ramene en positif
							if(reminder.compareTo(Env.ZERO) == -1)
							{
								reminder.multiply(new BigDecimal(-1));
							}
						}
					break;
					case 4: condition = "=";
						if(solde.compareTo(creditsAlert.getAlert_Limit()) == 0)
							reminder = Env.ZERO;			
					break;
					case 5: condition = ">";
						if(solde.compareTo(creditsAlert.getAlert_Limit()) == 1)
							reminder = solde.subtract(creditsAlert.getAlert_Limit());			
					break;
					case 6: condition = ">=";
						if(solde.compareTo(creditsAlert.getAlert_Limit()) == 1 || solde.compareTo(creditsAlert.getAlert_Limit()) == 0)
							reminder = solde.subtract(creditsAlert.getAlert_Limit());			
					break;
				}
				
				query = "Select 1 From P_Employee "  
				      +  "Where  P_Employee_ID = " + m_Employee.getP_Employee_ID() + " And " + solde.add(sldAutre) + " " + condition + " " + creditsAlert.getAlert_Limit() ;
				
				if ( creditsAlert.getAlert_Option() != null )
				{
					switch ( Integer.parseInt( creditsAlert.getAlert_Option() ) )
					{ 
						case 1: query = query + " and P_Job_Type_ID = " + m_Employee.getP_Job_Type_ID();
						break;
						case 2: query = query + " and P_Job_Title_ID = " + m_Employee.getP_Job_Title_ID();
						break;
						case 3: query = query + " and P_Occupation_Group_ID = " + m_Employee.getP_Occupation_Group_ID();
						break;
						case 4: query = query + " and P_Payment_Group_ID = " + m_Employee.getP_Payment_Group_ID();
						break;
						case 5: query = query + " and P_Workplace_ID = " + m_Employee.getP_Workplace_ID();
						break;
	     			}
				}
				    
				//log.log( Level.SEVERE, "P_Credits Validation - query - " + query );

				try
				{
					pstmt2 = DB.prepareStatement (query, getTrxName());
					ResultSet rs2 = pstmt2.executeQuery ();
					if ( rs2.next () )
					{
						if ( creditsAlert.getAlert_Severity_Level().equals("E")  )
						{
						   bAlert = true;
						   String message = creditsAlert.getAlert_Message();
						   m_strLastAlert = message;
						   if(bFromWeb == false)
							   P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, message, getTrxName() );
						}
						else if ( creditsAlert.getAlert_Severity_Level().equals("W")  )
						{
						   String message = creditsAlert.getAlert_Message();
						   m_strLastAlert = message;
						   if(bFromWeb == false)
							   P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Warning, message, getTrxName() );
						}

						//si on est en chainage, on retourne pas l'erreur tout de suite
						//on va aller voir dans la banque associée pour savoir si ya alerte
						else if(creditsAlert.getAlert_Severity_Level().equals("C"))
						{
							//si le mouvement provient d'une attribution et qu'il est négatif, on chaîne pas
							//CETTE MODIFICATION POURRAIT DEVOIR ÊTRE PARAMÉTRISABLE!!!!!!!
							
//							if(credits_movement.isAttribution() == true &&  credits_movement.getPMovementType().substring(0,2).equals("V-") )
							if(credits_movement.isAttribution() == true && credits_movement.getPMovementVariation().compareTo(Env.ZERO) < 0 ) // credits_movement.getPMovementType().substring(0,2).equals("V-")
							{
								//on génère un warning pis on fait le mouvement qui est possible a faire.
								String message = "La banque "+credits.getValue()+" ne possède pas le solde nécessaire ("+credits_movement.getPMovementVariation()+") pour effectuer son attribution négative.";
								m_strLastAlert = message;
								if(bFromWeb == false)
									P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Warning, message, getTrxName() );
								//si le reminder est négatif, on additionne, sinon on soustrait
								if(reminder.compareTo(Env.ZERO) < 1)
									credits_movement.setPMovementVariation(variation.subtract(reminder));
								else
									credits_movement.setPMovementVariation(variation.add(reminder));
								
								credits_movement.save();
							}
							else
							{
								//on va chercher la banque avec laquelle chainer
								P_Credits credits_Alert_join = P_Credits.get(Env.getCtx (), creditsAlert.getP_Credits_Join_ID(), getTrxName() );
								//on creer ensuite le movement de banque, sans le sauvegarder
								//l'info provient du movement originel
									
								P_Credits_Movement alternate_credits_movement = new P_Credits_Movement(Env.getCtx(), 0, getTrxName());
								alternate_credits_movement.setP_Credits_ID(credits_Alert_join.getP_Credits_ID());
								alternate_credits_movement.setAD_Org_ID(credits_movement.getAD_Org_ID());
								alternate_credits_movement.setDescription(credits_movement.getDescription());
								alternate_credits_movement.setP_Employee_ID(credits_movement.getP_Employee_ID());
								alternate_credits_movement.setP_Payment_ID(credits_movement.getP_Payment_ID());
								//a voir ici
								//alternate_credits_movement.setP_Profile_ID(credits_movement.getP_Profile_ID());
								alternate_credits_movement.setPMovementDate(credits_movement.getPMovementDate());
								alternate_credits_movement.setPMovementHour(credits_movement.getPMovementHour());
								alternate_credits_movement.setPMovementOrigin(credits_movement.getPMovementOrigin());
								alternate_credits_movement.setPMovementType(credits_movement.getPMovementType());
								alternate_credits_movement.setP_Employee_Credits_ID(credits_movement.getP_Employee_Credits_ID());

								if(alternate_credits_movement.getPMovementType().substring(0,2).equals("V-"))
								{
									if(reminder.compareTo(Env.ZERO) > 0)
										reminder = reminder.multiply(new BigDecimal(-1));
								}
								else
								{
									if(reminder.compareTo(Env.ZERO) < 0)
										reminder = reminder.multiply(new BigDecimal(-1));
								}

								//si le reminder est négatif, on additionne, sinon on soustrait

								alternate_credits_movement.setPMovementVariation(reminder);
								//si on en sauvegarde un nouveau
								//faut supprimer l'Ancien ou le mettre a jour avec le nouveau solde
								
								//si le reste est egal a la variation initiale, on supprime le movement initiale
								if(reminder.compareTo(variation) == 0)
								{
									//seulement si pas web
									if(bFromWeb == false)
										credits_movement.delete(true);
								}
								//sinon, on mets a jour le mouvement initiale avec le solde disponible
								else
								{
									credits_movement.setPMovementVariation(variation.subtract(reminder));
	//								seulement si pas web
									if(bFromWeb == false)
										credits_movement.save();
								}
	//							seulement si pas web
								if(bFromWeb == false)
									alternate_credits_movement.save();

								bAlert = CreditsAlert(alternate_credits_movement, credits_Alert_join, d ,  reminder, bFromWeb, soldePrecedent, true);

/* 2009.06.22
								if ( credits_Alert_join.getValue().equals("3300"))
								{
									
									rs.next();
									P_Credits_Alert creditsAlert2 = P_Credits_Alert.get( Env.getCtx (), rs.getInt(1), getTrxName());
									P_Credits credits_Alert_join2 = P_Credits.get(Env.getCtx (), creditsAlert2.getP_Credits_Join_ID(), getTrxName() );

									P_Credits_Movement alternate_credits_movement2 = new P_Credits_Movement(Env.getCtx(), 0, getTrxName());
									alternate_credits_movement2.setP_Credits_ID( creditsAlert2.getP_Credits_Join_ID() );
									alternate_credits_movement2.setAD_Org_ID(alternate_credits_movement.getAD_Org_ID());
									alternate_credits_movement2.setDescription(alternate_credits_movement.getDescription());
									alternate_credits_movement2.setP_Employee_ID(alternate_credits_movement.getP_Employee_ID());
									alternate_credits_movement2.setP_Payment_ID(alternate_credits_movement.getP_Payment_ID());
									//a voir ici
									//alternate_credits_movement.setP_Profile_ID(credits_movement.getP_Profile_ID());
									alternate_credits_movement2.setPMovementDate(alternate_credits_movement.getPMovementDate());
									alternate_credits_movement2.setPMovementHour(alternate_credits_movement.getPMovementHour());
									alternate_credits_movement2.setPMovementOrigin(alternate_credits_movement.getPMovementOrigin());
									alternate_credits_movement2.setPMovementType(alternate_credits_movement.getPMovementType());
									alternate_credits_movement2.setP_Employee_Credits_ID(alternate_credits_movement.getP_Employee_Credits_ID());
									
									//.multiply(new BigDecimal(-1))
									//pour certaines banques et certains employes faut utiliser les heures 
									//accumulees de l'affectation
									P_Credits_Param creditsParam    = P_Credits_Param.get( Env.getCtx(), creditsAlert2.getP_Credits_Join_ID(), m_iP_Method_Credits_ID, alternate_credits_movement.getPMovementDate(), getTrxName() );

									P_Assignment_Param assignment_Param = P_Assignment_Param.get( Env.getCtx(), m_Employee.GetPrincipalAssignment_Param(), getTrxName());
									if(creditsParam.isCumulatedHours() == true && assignment_Param.getCumulatedHours().compareTo(Env.ZERO) > 0)
									{
										hoursForJRH_HRJ = assignment_Param.getCumulatedHours();
									}	
									else
									{
										if ( assignment_Param.getDay_Hours().compareTo(Env.ZERO) == 0)
											hoursForJRH_HRJ = new BigDecimal(8);
										else
											hoursForJRH_HRJ = assignment_Param.getDay_Hours();
									}
									BigDecimal nbrdays = (reminder).divide( hoursForJRH_HRJ, 2, BigDecimal.ROUND_HALF_UP);

								    Timestamp creditsDate = creditsParam.getCreditsDate( m_Period, m_Employee, getTrxName());

									BigDecimal sld = P_Credits.getEmployeeSolde( credits_Alert_join2, m_Employee, creditsDate, getTrxName() );
									BigDecimal var = sld.divide(nbrdays, 2, BigDecimal.ROUND_HALF_UP );
									alternate_credits_movement2.setPMovementVariation(var);
									alternate_credits_movement2.save();
								}
	
								bAlert = CreditsAlert(alternate_credits_movement, credits_Alert_join, d ,  reminder, bFromWeb, soldePrecedent, true);
								//si ca passe (bAlert == false) on sauvegarde le credits_movement
								 * 
								 */
							}
						
						}

						/*String message = creditsAlert.getAlert_Message();
						if(message == null || message.length() == 0)
						{
							message = "Aucun message d'erreur sur la limite de la banque "+credits.getValue();
						}

						P_Time_Sheet_Error.NewMessage( credits, m_TimeSheet, creditsAlert.getAlert_Severity_Level(), message, getTrxName() );
						*/
					}
					rs2.close ();
					pstmt2.close ();
					pstmt2 = null;
				}
				catch (Exception e)
				{
					if(bFromWeb == false)
						P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName());
		            log.log( Level.SEVERE, "*Error* P_Credits.creditsValidation - " + query, e );
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			if(bFromWeb == false)
				P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName());
            log.log( Level.SEVERE, "*Error* P_Credits.creditsValidation - " + sql, e );
		}

		return bAlert;
	}
	
	//
	// Gestion du départ des employés.
	//
	public void CreditsParting( Timestamp Day )
	{

	    if ( m_Employee.getDateLayoff() == null )
	        return;
	   
        if (	m_Employee.getDateLayoff().compareTo ( Day ) == 0 )
	    {

        	//log.log( Level.SEVERE, "CreditsParting - Date Layoff : " + m_Employee.getDateLayoff().toString() );
        	//log.log( Level.SEVERE, "CreditsParting - Date Period   : " + m_Period.getStartDate().toString() );
        	//log.log( Level.SEVERE, "CreditsParting - Date Period 2 : " + m_Period.getEndDate().toString() );

			BigDecimal Variation  = null;
			
			String sql = null;
			sql = "Select P_Employee_Credits_ID, P_Method_Credits_ID From P_Employee_Credits, P_Credits ";
			sql +="  Where P_Employee_Credits.IsActive = 'Y' ";
			sql +="    And P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID " ;
			sql +="    And P_Employee_Credits.P_Employee_ID = " + m_Employee.getP_Employee_ID();
			sql +="    And P_Credits.IsActive = 'Y' ";
			sql +="    And P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID ";
			sql +="    And (P_Employee_Credits.EffectTo is null OR P_Employee_Credits.EffectTo >= ?)";

			P_Employee_Credits employeeCredits;
			P_Credits       creditsInfo;
			P_Credits_Param creditsParam;
			
			//log.log( Level.SEVERE,"Gross Calcul - CreditsParting " + sql );

			
			PreparedStatement pstmt = null;
		    Timestamp CreditsDate;

			try
			{
				pstmt = DB.prepareStatement (sql, getTrxName());
				pstmt.setTimestamp(1, m_Employee.getDateLayoff());
				ResultSet rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					Variation   = new BigDecimal(0);
					
					m_iP_Method_Credits_ID = rs.getInt("P_Method_Credits_ID");

					employeeCredits = P_Employee_Credits.get(Env.getCtx (), rs.getInt(1), getTrxName());
					creditsInfo     = P_Credits.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), getTrxName() );
					creditsParam    = P_Credits_Param.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), m_iP_Method_Credits_ID, m_Period.getStartDate(), getTrxName() );
					
					if ( creditsParam != null && creditsParam.getP_Credits_Param_ID() != 0 )
					{
						// ! 1 - Sold is Perserved
						if ( ! creditsParam.getMethod_Parting().equals( P_Credits_Param.METHOD_PARTING_SoldIsPreserved )) 
						{
							CreditsDate = creditsParam.getCreditsDate( m_Period, m_Employee, getTrxName());
								
							Variation = new BigDecimal( 0 );

							//log.log( Level.SEVERE,"Gross Calcul - CreditsParting = " + CreditsInfo.getValue() + " - " + CreditsInfo.getName() + " Method variation Annual" + CreditsParam.getMethod_Annual() );

							// 0 - Sold is Reseted
							if ( creditsParam.getMethod_Parting().equals( P_Credits_Param.METHOD_PARTING_SoldIsReseted )) 
							{
								//2008-07-31
								Variation = new BigDecimal(0).subtract(P_Credits.getEmployeeSolde( creditsInfo, m_Employee, m_Period.getEndDate(), getTrxName() ));
							}


							// 2 - Sold is pay
							if ( creditsParam.getMethod_Parting().equals( P_Credits_Param.METHOD_PARTING_SoldIsPay )) 
							{

								//si on a un facteur de paiement, on l'applique
								if(creditsParam.getPaymentFactor() != null)
								{
									BigDecimal bdPayFactor = Env.ZERO;
									bdPayFactor = creditsParam.getPaymentFactor();
									//on divise le facteur par 100 pour avoir un pourcentage
									bdPayFactor = bdPayFactor.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP);
									//on multiplie la variation par le facteur (en %)
									Variation = Variation.multiply(bdPayFactor);
								}
								
							    //
							    // Vérifier que les paramètre sont bien définie.
							    //
							    
							    if ( creditsParam.getGainParting() == 0 )
	   						    {
							        String message = Msg.getMsg(Env.getCtx(), "GainPartingRequired"); 
									P_Time_Sheet_Error.NewMessage( creditsInfo, m_TimeSheet, P_Credits_Alert.ALERT_SEVERITY_LEVEL_Error , message, getTrxName() );
	   						    }
							    
							    if ( creditsParam.getGainParting() != 0 )
	   						    {
					                Variation  = new BigDecimal(0).subtract(P_Credits.getEmployeeSolde( creditsInfo, m_Employee, CreditsDate, getTrxName() ));
					                
					                P_Gain Gain = P_Gain.get( Env.getCtx(), creditsParam.getGainAnnual(), getTrxName());

									int line = m_Payment.GetNextLine ( );

					               // On créer une ligne dans la feuille de temps. 
								   // Il serait peut-etre possible de penser de se servire d'une table qui servira a la prochaine génération de temps........			   //
									
									P_Assignment_Param assignment_Param = P_Assignment_Param.get( Env.getCtx (), m_Employee.GetPrincipalAssignment_Param(), getTrxName() );

									P_Payment_Gain paymentGain = new P_Payment_Gain(Env.getCtx (), -1, getTrxName());
									paymentGain.setP_Payment_ID( m_Payment.getP_Payment_ID() );
									paymentGain.setP_Employee_ID( m_Payment.getP_Employee_ID());
									
									paymentGain.setP_Assignment_ID( assignment_Param.getP_Assignment_ID() );
									paymentGain.setP_Gain_ID( creditsParam.getGainParting() );
									paymentGain.setHourly_Rate( assignment_Param.getHourly_Rate()  );
									paymentGain.setType_Rate( Gain.getType_Rate() );
									
									if ( ! creditsInfo.getCreditsContents().equals( P_Credits.CREDITSCONTENTS_Amount))
									{
										paymentGain.setQuantity(  new BigDecimal(0).subtract(Variation ));
										paymentGain.setFixed_Amount( new BigDecimal(0) );
									}
									else
									{
										paymentGain.setQuantity( new BigDecimal(0) );
										paymentGain.setFixed_Amount( new BigDecimal(0).subtract(Variation) );
									}
									paymentGain.setLine( line );
									paymentGain.setP_Year_ID( m_Payment.getP_Year_ID()  );
									paymentGain.setP_Period_ID( m_Payment.getP_Period_ID() );
									paymentGain.setDay( CreditsDate );
									paymentGain.save();
							        
							    }

							
							}
							
							//log.log( Level.SEVERE,"Gross Calcul - CreditsParting = " + CreditsInfo.getValue() + " Method variation Annual" + CreditsParam.getMethod_Annual() + " variation " + Variation );

							if ( Variation.compareTo( new BigDecimal( 0 )) != 0 )
							{
								P_Credits_Movement CreditsMovement = P_Credits_Movement.get(Env.getCtx (), m_Payment.getP_Payment_ID(), employeeCredits.getP_Credits_ID(), CreditsDate, "BF", "FTP", getTrxName() ) ;

								CreditsMovement.setP_Credits_ID( employeeCredits.getP_Credits_ID());
								CreditsMovement.setP_Employee_ID( employeeCredits.getP_Employee_ID());
								CreditsMovement.setPMovementDate( CreditsDate );
								CreditsMovement.setPMovementOrigin( "FTP");
								CreditsMovement.setPMovementType( "BF" );
								CreditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());
								
								Variation = Variation.setScale(2, BigDecimal.ROUND_HALF_UP );
								
								if  (  CreditsMovement.getPMovementVariation() != null )
								{
									CreditsMovement.setPMovementVariation( CreditsMovement.getPMovementVariation().add( Variation ) );
								}
								else
								{
									CreditsMovement.setPMovementVariation( Variation );
								}
								CreditsMovement.setP_Payment_ID( m_Payment.getP_Payment_ID() );
								CreditsMovement.save();
							}
						}
					}

				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
	            P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), getTrxName());
	            log.log( Level.SEVERE, "CreditsParting - " + sql, e);
			}
	    }
	    
	}
	
	private void setTrxName( String trxName)
	{
		m_trxName = trxName;
	}

	private String getTrxName()
	{
		return m_trxName;
	}

	//
	// Détermine la variation
	//
	public String CreditsVariationFromWeb( int iP_Employee_ID, Timestamp d, BigDecimal var, int iP_Gain_ID) throws SQLException  
	{
		String strResult = "";
		BigDecimal hoursForJRH_HRJ = Env.ZERO;
		BigDecimal Variation = new BigDecimal( 0 );
		BigDecimal NbrHour = new BigDecimal( 0 );

		
		
		//P_Credits_Param CreditsParam = P_Credits_Param.get( Env.getCtx(), EmployeeCredits.getP_Credits_ID(), m_iP_Method_Credits_ID, d, getTrxName());
		m_Employee = P_Employee.get(Env.getCtx(), iP_Employee_ID, getTrxName());
		if(m_Employee.getP_Employee_ID() == 0)
		{
			strResult = "L'employé avec l'identifiant "+iP_Employee_ID+" n'existe pas dans le système.";
			return strResult;
		}
		P_Assignment_Param assignment_Param = P_Assignment_Param.get( Env.getCtx(), m_Employee.GetPrincipalAssignment_Param(), getTrxName());
		if(assignment_Param.getP_Assignment_ID() == 0)
		{
			strResult = "L'employé "+m_Employee.getValue()+" n'a pas d'affectation principale en date du "+ DateFormat.getTimeInstance(DateFormat.SHORT).format(d);
			return strResult;
		}
		P_Gain gain = P_Gain.get(Env.getCtx(), iP_Gain_ID, getTrxName());
		P_Gain_Parameter gain_param = P_Gain_Parameter.get(Env.getCtx(), iP_Gain_ID, assignment_Param.getP_Collective_Labour_Agr_ID(), getTrxName());
		if(gain_param.getP_Gain_Parameter_ID() == 0)
		{
			strResult = "Le code de rémunération "+gain.getValue()+" n'a pas de paramètre associé.";
			return strResult;
		}
		
		//premiere etape, aller chercher les banques modifie par ce tag pour cet employe
		
		String sqlSelect = " SELECT gc.P_Credits_ID, gc.Credits_Function, gc.MultiplyRate, ec.P_Method_Credits_ID, ec.P_Employee_Credits_ID "
			+ " FROM P_Employee_Credits ec, P_Gain_Credits gc "
			+ " WHERE ec.P_Credits_ID = gc.P_Credits_ID "
			+ " AND ec.P_Employee_ID = "+m_Employee.getP_Employee_ID()
			+ " AND gc.P_Gain_Parameter_ID = "+gain_param.getP_Gain_Parameter_ID()
			+ " AND gc.IsActive = 'Y' "
			+ " AND ec.IsActive = 'Y' "
			+ " AND ec.EffectIn < 1 "
			+ " AND (ec.EffectTo is null OR ec.EffectTo > 2) "
			+ " AND (gc.P_Collective_Labour_Agr_ID is null OR "
			+"		gc.P_Collective_Labour_Agr_ID = "+assignment_Param.getP_Collective_Labour_Agr_ID()+") "
			
			// 2023-06-13
			+ " AND P_Gain_Credits.MultiplyRate <> 0"		
			
			+" ORDER BY gc.P_Collective_Labour_Agr_ID ";
		
		PreparedStatement pstmt = DB.prepareStatement(sqlSelect, getTrxName());
		Vector<Integer> vCredits = new Vector<Integer>(10);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next())
		{
			//on verifie si la banque a deja ete traitee (plusieurs fois la meme banques
			//a cause de la convention collective pouvant etre null)
			if(vCredits.contains(new Integer(rs.getInt(1))) == true)
			{
				break;
			}
			else
			{
				vCredits.add(new Integer(rs.getInt(1)));
			}
			
			P_Credits credits = P_Credits.get(Env.getCtx(), rs.getInt(1), getTrxName());
			P_Credits_Param credits_param = P_Credits_Param.get(Env.getCtx(), credits.getP_Credits_ID(), rs.getInt(4), d, getTrxName());
			if(credits_param.getP_Credits_Param_ID() == 0)
			{
				strResult = "La banque "+credits.getValue()+" n'a pas de paramètre associé.";
				return strResult;
			}
			//on va cherche la quantité du mouvement
			Variation = var;
			//on ajuste selon le facteur multiplicatif
			Variation = Variation.multiply(rs.getBigDecimal(3));
			//on ajuste selon le signe de la transaction
			if(rs.getString(2).substring(0, 1).equals("-"))
			{
				Variation = Variation.multiply(new BigDecimal(-1));
			}
			//doit-on reajuster avec les heures cummulés?
			if(credits_param.isCumulatedHours() == true && assignment_Param.getCumulatedHours().compareTo(Env.ZERO) > 0)
			{
				hoursForJRH_HRJ = assignment_Param.getCumulatedHours();
			}	
			else
			{
				hoursForJRH_HRJ = assignment_Param.getDay_Hours();
			}
			P_UOM tagUnits = P_UOM.get(Env.getCtx(), gain.getP_UOM_ID(), getTrxName());
			P_UOM creditsUnits = P_UOM.get(Env.getCtx(), credits.getP_UOM_ID(), getTrxName());
			//pour l'instant on traite juste les codes de remuneration
			//en Heures (H) et en $$ (M pour montant)
			
			//GAIN EN HEURES
			if(tagUnits.getValue().equals("H"))
			{
				//BANQUE EN HEURES
				if(creditsUnits.getValue().equals("H"))
				{
					//Variation = Variation;
				}
//					BANQUE EN JOURS
				else if(creditsUnits.getValue().equals("J"))
				{
					Variation = Variation.divide( hoursForJRH_HRJ , 2, BigDecimal.ROUND_HALF_UP );
				}

			}
			//GAIN EN MONTANT
			else if(tagUnits.getValue().equals("M"))
			{
				//si le gain en montant mais la banque pas en montant...
				if(creditsUnits.getValue().equals("M") == false)
				{
			        //String message = Msg.getMsg(Env.getCtx(), ""); 
					strResult = "L'unité de mesure (" + tagUnits.getValue() +")"
						+" pour le code de rémunération "+gain.getValue()+" n'est pas compatible avec"
						+" l'unité de mesure ("+creditsUnits.getValue()+") de la banque "+credits.getValue();
					return strResult;
				}
			}
			//ici, on crée un mouvement de credits bidon pour lancer la verification des limites de banques
			//on va chercher le mouvement
			P_Credits_Movement creditsMovement = new P_Credits_Movement(Env.getCtx (), getTrxName() ) ;

			
			creditsMovement.setP_Credits_ID( credits.getP_Credits_ID());
			creditsMovement.setP_Employee_ID( m_Employee.getP_Employee_ID());
			creditsMovement.setPMovementDate( d );
			creditsMovement.setPMovementOrigin( "FTP");
			creditsMovement.setPMovementType( "V" + rs.getString(2).substring(0, 1) );
			creditsMovement.setP_Employee_Credits_ID(rs.getInt("P_Employee_Credits_ID"));
			
			creditsMovement.setP_Method_Credits_ID( credits_param.getP_Method_Credits_ID() );

			Variation = Variation.setScale(2, BigDecimal.ROUND_HALF_UP );
			NbrHour = Variation.setScale(2, BigDecimal.ROUND_HALF_UP );		 			 
			creditsMovement.setPMovementVariation( Variation );
			creditsMovement.setPMovementHour(  NbrHour );
			creditsMovement.setP_Payment_ID( 0 );
			
			//Ne Pas sauvegarder le mouvement
			boolean retValue = CreditsAlert(creditsMovement, credits, d, Variation, true, null, false );
			if(retValue == true)
			{
				strResult = m_strLastAlert;
				return strResult;
			}
					
		}
		
		//rendu ici, tout est correct, on retourne string vide pour signifier
		//que tout est OK
		return "";
	}
	//
	// Détermine la variation
	//
	
	public String CreditsVariationFromWebV2( int iP_Employee_ID, Timestamp tsDay, BigDecimal var, int iP_Gain_ID, int iP_Booklet_ID) throws SQLException  
	{
		String strResult = "";
		BigDecimal hoursForJRH_HRJ = Env.ZERO;
		BigDecimal Variation = new BigDecimal( 0 );
		BigDecimal NbrHour = new BigDecimal( 0 );

		P_Booklet booklet = new P_Booklet(Env.getCtx(), iP_Booklet_ID, null);
		if(booklet == null)
		{
			return "L'identifiant du livret est invalide.";
		}
		P_Employee Employee = P_Employee.get( Env.getCtx(), iP_Employee_ID, null);
		P_Payment_Group PaymentGroup = P_Payment_Group.get( Env.getCtx(), Employee.getP_Payment_Group_ID(), null);
		
		String strSqlPeriod = " SELECT P_Period_ID FROM P_Period "
			+ " WHERE  ? between Startdate AND EndDate "
            + " and P_Frequency_ID = " + PaymentGroup.getP_Frequency_ID()
			;
		int iP_Period_ID = 0;
		PreparedStatement pstmPeriod = DB.prepareStatement(strSqlPeriod, null);
		pstmPeriod.setTimestamp(1, tsDay);
		try
		{
			ResultSet rsPeriod = pstmPeriod.executeQuery();
			if(rsPeriod.next())
			{
				iP_Period_ID = rsPeriod.getInt("P_Period_ID");
			}
		}
		catch(Exception e)
		{
			log.log( Level.SEVERE, "CreditsVariationFromWebV2 - " + strSqlPeriod, e);
		}
		 
		P_Period Period = P_Period.get( Env.getCtx(), iP_Period_ID, null);
		
		String strMovementSelect = "select credits_name,unit,value,sum(solde) as solde, P_Credits_ID ";
		strMovementSelect +="       from (select Credits.Name as Credits_Name, ";
		strMovementSelect +="       	   SUM( PMOVEMENTVARIATION ) Solde, ";
		strMovementSelect +="       	   UOM.Value as Unit, ";
		strMovementSelect +="       	   Credits.Value, Credits.P_Credits_ID ";
		strMovementSelect +="         from P_Employee Employee inner join (P_Credits_Movement Movement   ";
		strMovementSelect +="       inner join (P_Credits Credits inner join P_UOM UOM ";
		strMovementSelect +="       	on Credits.P_UOM_ID = UOM.P_UOM_ID) ";
		strMovementSelect +="       	on Movement.P_Credits_ID = Credits.P_Credits_ID) ";
		strMovementSelect +="       	on Employee.P_Employee_ID = Movement.P_Employee_ID ";
		strMovementSelect +="       where Movement.PMovementDate <= " + DB.TO_DATE(Period.getStartDate() );
		strMovementSelect +="       and Employee.P_Employee_ID =  "+ Employee.getP_Employee_ID() +" ";
		strMovementSelect +="       and Credits.IsPrinted = 'Y' ";
		strMovementSelect +="       group by Credits.Name, UOM.Value, Credits.Value, Credits.P_Credits_ID ";
		strMovementSelect +="       union ";
		strMovementSelect +="       Select P_Credits.Name as Credits_Name, ";
		strMovementSelect +="       	   SUM(CASE WHEN Credits_Function = '+' ";
		strMovementSelect +="       				THEN P_Booklet_Detail.DayQty * P_Gain_Credits.MultiplyRate ";
		strMovementSelect +="       				ELSE P_Booklet_Detail.DayQty * P_Gain_Credits.MultiplyRate * -1 ";
		strMovementSelect +="       			 END) as Solde, ";
		strMovementSelect +="       			 P_UOM.Value as Unit, ";
		strMovementSelect +="       			 P_Credits.Value, ";
		strMovementSelect +="       			 P_Credits.P_Credits_ID ";
		strMovementSelect +="         From P_Employee_Credits, ";
		strMovementSelect +="       	   P_Gain_Credits, ";
		strMovementSelect +="       	   P_Credits, ";
		strMovementSelect +="       	   P_Gain_Parameter, ";
		strMovementSelect +="       	   P_Booklet_Detail, ";
		strMovementSelect +="       	   P_employee , ";
		strMovementSelect +="       	   P_UOM ";
		strMovementSelect +="        Where P_Gain_Credits.IsActive = 'Y' ";
		strMovementSelect +="          And P_Credits.IsActive = 'Y' ";
		strMovementSelect +="          And P_Gain_Credits.P_Credits_ID = P_Credits.P_Credits_ID ";
		strMovementSelect +="          AND P_Gain_Parameter.P_Gain_Parameter_ID =  ";
		strMovementSelect +="       P_Gain_Credits.P_Gain_Parameter_ID ";
		strMovementSelect +="          And P_Gain_Parameter.P_Gain_ID = P_Booklet_Detail.P_Gain_ID ";
		strMovementSelect +="          And P_Credits.P_UOM_ID = P_UOM.P_UOM_ID ";
		strMovementSelect +="          And P_Booklet_Detail.P_Booklet_ID =  "+booklet.getP_Booklet_ID();
		strMovementSelect +="          And P_Employee_Credits.P_Employee_ID =  "+ Employee.getP_Employee_ID() ;
		strMovementSelect +="          And P_Employee_Credits.IsActive = 'Y' ";
		strMovementSelect +="          And (P_Employee_Credits.EffectTo is null OR  P_Employee_Credits.EffectTo >= ?)";
		strMovementSelect +="          And P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID ";
		strMovementSelect +="          And P_Employee.p_employee_id = p_employee_credits.p_employee_id ";
		strMovementSelect +="          And ISNULL(P_Gain_Credits.P_Collective_Labour_Agr_ID,0) in ";
		strMovementSelect +="       	   (SELECT ISNULL(Max (P_Collective_Labour_Agr_ID),0) ";
		strMovementSelect +="       		  FROM P_Gain_Credits credits ";
		strMovementSelect +="       		 WHERE credits.p_gain_parameter_id =  ";
		strMovementSelect +="       p_gain_credits.p_gain_parameter_id ";
		strMovementSelect +="       		   and credits.p_credits_id = p_gain_credits.p_credits_id ";
		strMovementSelect +="       		   and credits.credits_function = p_gain_credits.credits_function ";
		strMovementSelect +="       		   and (Credits.P_Collective_Labour_Agr_ID is null ";
		strMovementSelect +="       			or Credits.P_Collective_Labour_Agr_ID =  ";
		strMovementSelect +="       P_employee.P_Collective_Labour_Agr_ID) ) ";
		strMovementSelect +="       GROUP BY P_Credits.Name, P_Credits.Value, P_UOM.Value, P_Credits.P_Credits_ID )  ";
		strMovementSelect +="       solde_theorique ";
		strMovementSelect +="       group by credits_name,unit,value, p_credits_id ";

		Vector<Integer> vecBanque = new Vector<Integer>(10);
		Vector<BigDecimal> vecDeltaSolde = new Vector<BigDecimal>(10);

		
		//Commentaire :
		//on va chercher le solde des banques que possedent l'employe
		// en 2 parties. 1ere partie, le solde avant la periode, 2eme partie, solde des attributions pendants la periode
		//malgré ce que laisse penser l'Algo suivant, il n'y aurait jamais 2 entrées par banques, seulement 1 contenant le solde theorique
		//a la fin de la periode courante mais sans tenir compte du mouvement courant
		PreparedStatement pstmMoveSelect = DB.prepareStatement(strMovementSelect, null);
		try
		{
			pstmMoveSelect.setTimestamp(1, tsDay);
			
            System.out.println(" CreditsVariationFromWebV2 : "+ strMovementSelect + "  date : " + tsDay );

			ResultSet rsMoveSelect = pstmMoveSelect.executeQuery();
			while(rsMoveSelect.next())
			{
				Integer creditId = new Integer(rsMoveSelect.getInt("P_Credits_ID"));
				BigDecimal currentVar = rsMoveSelect.getBigDecimal("solde");
				int vecIndex = vecBanque.indexOf(creditId);
				if(vecIndex >= 0)
				{
					vecDeltaSolde.set(vecIndex, ((BigDecimal)vecDeltaSolde.get(vecIndex)).add(currentVar));
				}
				else
				{
					vecBanque.add(creditId);
					vecDeltaSolde.add(currentVar);
				}
			}
		}
		catch(Exception e)
		{
			log.log( Level.SEVERE, "isTagEvalPresent - " + strMovementSelect, e);
		}
		
		
		//P_Credits_Param CreditsParam = P_Credits_Param.get( Env.getCtx(), EmployeeCredits.getP_Credits_ID(), m_iP_Method_Credits_ID, d, getTrxName());
		
		
		m_Employee = P_Employee.get(Env.getCtx(), iP_Employee_ID, getTrxName());
		if(m_Employee.getP_Employee_ID() == 0)
		{
			strResult = "L'employé avec l'identifiant "+iP_Employee_ID+" n'existe pas dans le système.";
			return strResult;
		}
		P_Assignment_Param assignment_Param = P_Assignment_Param.get( Env.getCtx(), m_Employee.GetPrincipalAssignment_Param(), getTrxName());
		if(assignment_Param.getP_Assignment_ID() == 0)
		{
			strResult = "L'employé "+m_Employee.getValue()+" n'a pas d'affectation principale en date du "+ DateFormat.getTimeInstance(DateFormat.SHORT).format(tsDay);
			return strResult;
		}
		P_Gain gain = P_Gain.get(Env.getCtx(), iP_Gain_ID, getTrxName());
		P_Gain_Parameter gain_param = P_Gain_Parameter.get(Env.getCtx(), iP_Gain_ID, m_Employee.getP_Collective_Labour_Agr_ID(), getTrxName());
		if(gain_param.getP_Gain_Parameter_ID() == 0)
		{
			strResult = "Le code de rémunération "+gain.getValue()+" n'a pas de paramètre associé.";
			return strResult;
		}
		
		//premiere etape, aller chercher les banques modifie par ce tag pour cet employe
		
		String sqlSelect = " SELECT gc.P_Credits_ID, gc.Credits_Function, gc.MultiplyRate, ec.P_Method_Credits_ID, ec.P_Method_Credits_ID, ec.P_Employee_Credits_ID "
			+ " FROM P_Employee_Credits ec, P_Gain_Credits gc, P_Credits cre "
			+ " WHERE ec.P_Credits_ID = gc.P_Credits_ID "
			+ " AND ec.P_Employee_ID = "+m_Employee.getP_Employee_ID()
			+ " AND gc.P_Gain_Parameter_ID = "+gain_param.getP_Gain_Parameter_ID()
			+ " AND cre.P_Credits_ID=ec.P_Credits_ID "
			+ " AND gc.IsActive = 'Y' "
			+ " AND ec.IsActive = 'Y' "
			+ " AND ec.EffectIn < ? "
			+ " AND cre.IsActive = 'Y' "
			+ " AND (ec.EffectTo is null OR ec.EffectTo > ?) "
			+ " AND (gc.P_Collective_Labour_Agr_ID is null OR "
			+"		gc.P_Collective_Labour_Agr_ID = "+m_Employee.getP_Collective_Labour_Agr_ID()+") "
			+" ORDER BY gc.P_Collective_Labour_Agr_ID desc ";
		
		PreparedStatement pstmt = DB.prepareStatement(sqlSelect, getTrxName());
		pstmt.setTimestamp(1,tsDay);
		pstmt.setTimestamp(2,tsDay);
		Vector<Integer> vCredits = new Vector<Integer>(10);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next())
		{
			m_iP_Method_Credits_ID = rs.getInt(5);
			//on verifie si la banque a deja ete traitee (plusieurs fois la meme banques
			//a cause de la convention collective pouvant etre null)
			if(vCredits.contains(new Integer(rs.getInt(1))) == true)
			{
				break;
			}
			else
			{
				vCredits.add(new Integer(rs.getInt(1)));
			}
			
			P_Credits credits = P_Credits.get(Env.getCtx(), rs.getInt(1), getTrxName());
			P_Credits_Param credits_param = P_Credits_Param.get(Env.getCtx(), credits.getP_Credits_ID(), rs.getInt(4), tsDay, getTrxName());
			if(credits_param.getP_Credits_Param_ID() == 0)
			{
				strResult = "La banque "+credits.getValue()+" n'a pas de paramètre associé.";
				return strResult;
			}
			//on va cherche la quantité du mouvement
			Variation = var;
			//on ajuste selon le facteur multiplicatif
			Variation = Variation.multiply(rs.getBigDecimal(3));
			//on ajuste selon le signe de la transaction
			if(rs.getString(2).substring(0, 1).equals("-"))
			{
				Variation = Variation.multiply(new BigDecimal(-1));
			}
			//doit-on reajuster avec les heures cummulés?
			if(credits_param.isCumulatedHours() == true && assignment_Param.getCumulatedHours().compareTo(Env.ZERO) > 0)
			{
				hoursForJRH_HRJ = assignment_Param.getCumulatedHours();
			}	
			else
			{
				hoursForJRH_HRJ = assignment_Param.getDay_Hours();
			}
			P_UOM tagUnits = P_UOM.get(Env.getCtx(), gain.getP_UOM_ID(), getTrxName());
			P_UOM creditsUnits = P_UOM.get(Env.getCtx(), credits.getP_UOM_ID(), getTrxName());
			//pour l'instant on traite juste les codes de remuneration
			//en Heures (H) et en $$ (M pour montant)
			
			//GAIN EN HEURES
			if(tagUnits.getValue().equals("H"))
			{
				//BANQUE EN HEURES
				if(creditsUnits.getValue().equals("H"))
				{
					//Variation = Variation;
				}
//					BANQUE EN JOURS
				else if(creditsUnits.getValue().equals("J"))
				{
					Variation = Variation.divide( hoursForJRH_HRJ , 2, BigDecimal.ROUND_HALF_UP );
				}

			}
			//GAIN EN MONTANT
			else if(tagUnits.getValue().equals("M"))
			{
				//si le gain en montant mais la banque pas en montant...
				if(creditsUnits.getValue().equals("M") == false)
				{
			        //String message = Msg.getMsg(Env.getCtx(), ""); 
					strResult = "L'unité de mesure (" + tagUnits.getValue() +")"
						+" pour le code de rémunération "+gain.getValue()+" n'est pas compatible avec"
						+" l'unité de mesure ("+creditsUnits.getValue()+") de la banque "+credits.getValue();
					return strResult;
				}
			}
			//ici, on crée un mouvement de credits bidon pour lancer la verification des limites de banques
			//on va chercher le mouvement
			P_Credits_Movement creditsMovement = new P_Credits_Movement(Env.getCtx (), getTrxName() ) ;

			creditsMovement.setP_Credits_ID( credits.getP_Credits_ID());
			creditsMovement.setP_Employee_ID( m_Employee.getP_Employee_ID());
			creditsMovement.setPMovementDate( tsDay );
			creditsMovement.setPMovementOrigin( "FTP");
			creditsMovement.setPMovementType( "V" + rs.getString(2).substring(0, 1) );
			creditsMovement.setP_Employee_Credits_ID(rs.getInt("P_Employee_Credits_ID"));

			creditsMovement.setIsAttribution(false);
			creditsMovement.setP_Method_Credits_ID( credits_param.getP_Method_Credits_ID() );

			Variation = Variation.setScale(2, BigDecimal.ROUND_HALF_UP );
			NbrHour = Variation.setScale(2, BigDecimal.ROUND_HALF_UP );
			
			//Si il existe deja une variation de cette banque avant le traitement du gain courant
			//on additionne la modif existante a la courante pour valider puisque le solde de la banque
			//n'a pas été modifié par la modif existante
			
			//on va chercher le delta deja calculé selon les detail du livret
			
			BigDecimal soldePrecedent = Env.ZERO;
			for(int idx = 0; idx < vecBanque.size();idx++)
			{
				if(credits.getP_Credits_ID() == ((Integer)vecBanque.get(idx)).intValue())
				{
					soldePrecedent = soldePrecedent.add((BigDecimal)vecDeltaSolde.get(idx));
				}
			}
			//creditsMovement.setPMovementVariation( Variation.add(deltaAlreadyIN) );
			creditsMovement.setPMovementVariation( Variation);
			creditsMovement.setPMovementHour(  NbrHour );
			creditsMovement.setP_Payment_ID( 0 );
		
			//Ne Pas sauvegarder le mouvement
			boolean retValue = CreditsAlert(creditsMovement, credits, tsDay, Variation, true,  soldePrecedent, false);
			if(retValue == true)
			{
				strResult = m_strLastAlert;
				return strResult;
			}
					
		}
		
		//rendu ici, tout est correct, on retourne string vide pour signifier
		//que tout est OK
		return "";
	}
	
	
	private void validPaymentGain(Vector vecGain, Vector<Integer> vecGainIsPresent, int iP_Gain_ID)
	{
		for(int idx=0;idx<vecGain.size();idx++)
		{
			if(((Integer)vecGain.get(idx)).intValue() == iP_Gain_ID)
			{
				vecGainIsPresent.set(idx, new Integer(1));
			}
		}
	}

	
	private boolean isGainEvalPresent(int iP_Employee_ID, Timestamp dateDebut, Timestamp dateFin, int iP_Credit_ID, P_Credits_Param creditsParam)
	{
		if ( ! creditsParam.isEmployeeMustHaveWorked() )
			return true;

		boolean retValue = false;
		String sql = "SELECT isnull(Sum(P_Payment_Gain.Quantity), 0) "
			+" FROM P_Gain_Credits, P_Payment, P_Payment_Gain, P_Gain_Parameter"
			+" WHERE P_Gain_Credits.P_Credits_ID="+iP_Credit_ID
			+" AND P_Payment.P_Employee_ID = " + iP_Employee_ID
			+" AND P_Payment.P_Payment_ID = P_Payment_Gain.P_Payment_ID "
			+" AND P_Payment_Gain.P_Gain_ID=P_Gain_Parameter.P_Gain_ID"
			+" AND P_Gain_Credits.P_Gain_Parameter_ID=P_Gain_Parameter.P_Gain_Parameter_ID"
			+" AND P_Payment_Gain.Day between ? AND ? "
			+" GROUP BY P_Payment_Gain.P_Gain_ID";
		PreparedStatement pstmt = DB.prepareStatement(sql, getTrxName());
		try
		{
			pstmt.setTimestamp(1, dateDebut);
			pstmt.setTimestamp(2, dateFin);
			ResultSet rs = pstmt.executeQuery();
			boolean bContinue = true;
			while(rs.next() && bContinue)
			{
				if(rs.getBigDecimal(1).compareTo(new BigDecimal(0)) > 0)
				{
					retValue = true;
					bContinue = false;
				}
			}
			rs.close();
			pstmt.close();
		}
		catch(SQLException e)
		{
			log.log( Level.SEVERE, "isTagEvalPresent - " + sql, e);
		}

		return retValue;
	}

	//Nous permet de faire le prorata des jours travaillés pour un employé embauché dans le mois
	//Donc, si un employé recoit une attribution de vacances mensuelles de 11 heures et qu'il n'A travaillé que 50%
	//du mois, il ne recoit que 5.5 heures
	//PATCH POUR SIQ
	//BANQUE 4003 - 4005
	//TODO harcoded !! 
	private BigDecimal vacancesEmbaucheProrata(BigDecimal variation, P_Credits_Param creditParam, P_Employee employee)
	{
		BigDecimal retValue = Env.ZERO;
/*		
		//on va chercher la banque 8050 pour calculer le temps travaillé pour Vacances
		P_Credits creditsJoin = null;
		String sql8050 = "SELECT P_Credits_ID from P_Credits WHERE Value = '8050' ";
		PreparedStatement pstm8050 = DB.prepareStatement(sql8050, null);
		try
		{
			ResultSet rs8050 = pstm8050.executeQuery();
			if(rs8050.next())
			{
				creditsJoin = P_Credits.get( Env.getCtx (), rs8050.getInt(1), getTrxName() );
			}
			else
			{
				log.log( Level.SEVERE, "vacancesEmbaucheProrata - " + sql8050);
				return Env.ZERO;
			}
			rs8050.close();
			pstm8050.close();
		}
		catch(Exception e)
		{
			log.log( Level.SEVERE, "vacancesEmbaucheProrata - " + sql8050);
			return Env.ZERO;			
		}
*/		
		//on set les date de debut et fin
		Timestamp dateHired = (Timestamp)employee.getDateHired().clone();
		Timestamp dateFired = null;
		if(employee.getDateLayoff() != null)
			dateFired = (Timestamp)employee.getDateLayoff().clone();
		Timestamp startDateFinal = null;
		Timestamp endDateFinal = null;
		
		//on va chercher le solde de la banque associée pour la derniere journée du mois
		
		//premiere journee du mois (utilise pour calculer la derniere)
		Calendar start = Calendar.getInstance();
		//start.set( m_Period.getStartDate().getYear(), m_Period.getStartDate().getMonth(), 1 );
		start.setTimeInMillis( m_Period.getStartDate().getTime());
		//on set a la premier journee du mois
		start.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = start.getTime();
		Timestamp monthStartDate = new Timestamp( startDate.getTime());
				
		//derniere journée du mois
		Calendar end = Calendar.getInstance();
		end.setTimeInMillis( m_Period.getStartDate().getTime());
		//on va chercher la derniere journée du mois (maximum jour du mois)
		end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date endDate = end.getTime();
		Timestamp monthEndDate = new Timestamp( endDate.getTime() ) ;

		//si la date d'embauche de l'employe est plusgrande ou egal a la date de debut
		//de periode, on prend la date d'embauche comme date de debut
		if(dateHired != null && dateHired.compareTo(monthStartDate) >= 0)
		{
			startDateFinal = (Timestamp)dateHired.clone();
		}
		else
		{
			startDateFinal = (Timestamp)monthStartDate.clone();
		}
		//si la date de mise a pied de l'employe est plus grande ou egal
		//a la date de fin de periode, on prend la date de mise a pied 
		//comme date de fin
		if(dateFired != null && dateFired.compareTo(monthEndDate) <= 0)
		{
			endDateFinal = (Timestamp)dateFired.clone();
		}
		else
		{
			endDateFinal = (Timestamp)monthEndDate.clone();
		}

		//on va chercher la variation d'un 8050 pour le mois (ou moins si embauché ou mise a pied dans le mois)
		BigDecimal autreSolde = P_Credits.getCreditsVariation8050( m_Employee, startDateFinal, endDateFinal, getTrxName() );
		//on va chercher le nombre de jour qui aurait du etre travaillé pendant le mois
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(m_Period.getStartDate().getTime());
		BigDecimal soldeTheorique = new BigDecimal(employee.getWorkDayInMonth(cal.get(Calendar.YEAR), TimeUtil.getMonth(m_Period.getStartDate())));
		//on divise le nombre de jour reel par le nombre de jour theorique
		//avoir le pourcentage de jour travaille
		BigDecimal bdPourcentage = autreSolde.divide(soldeTheorique, 4, BigDecimal.ROUND_HALF_UP);//4 = scale
		//on applique ce pourcentage sur la variation
		
//		if (bdPourcentage.compareTo(Env.ONE) > 0 )
//			bdPourcentage = Env.ONE;
		
		retValue = variation.multiply(bdPourcentage);
		return retValue;
	}

	public void calcul_provision()
	{
		
		int creditsAN01 = 1101167;
		int creditsAN05 = 1101188;
		P_Employee_Credits cAnc = P_Employee_Credits.get(Env.getCtx (), m_Payment.getP_Employee_ID(), creditsAN01, m_Period.getEndDate(),  getTrxName());
		P_Employee_Credits cAnc5 = P_Employee_Credits.get(Env.getCtx (), m_Payment.getP_Employee_ID(), creditsAN05, m_Period.getEndDate(),  getTrxName());
		if ( cAnc == null )
		{
            P_Time_Sheet_Error.NewMessage( m_TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Employé sans banque d'ancienneté " , getTrxName());
            return;
		}
		BigDecimal sldAnc = cAnc.getTotalCredits( m_Period.getEndDate() );
		BigDecimal sldAnc05 = Env.ZERO;
		if ( cAnc5 != null )
			sldAnc05 = cAnc5.getTotalCredits( m_Period.getEndDate() ); 
		
		String sql = "SELECT P_EMPLOYEE_CREDITS.P_EMPLOYEE_CREDITS_ID, P_EMPLOYEE_CREDITS.P_Employee_ID,  P_EMPLOYEE_CREDITS.P_CREDITS_ID,   SUM( P_PAYMENT_GAIN.amountcalc), round( SUM( P_PAYMENT_GAIN.amountcalc) * max( dbo.Divide( rate ,100) ) ,2) Amt_Reserve "
				+ "FROM P_EMPLOYEE_CREDITS "
//				+ "INNER JOIN P_CREDITS ON P_CREDITS.P_CREDITS_ID = P_EMPLOYEE_CREDITS.P_CREDITS_ID "
				+ " INNER JOIN P_EMPLOYEE ON P_EMPLOYEE.P_EMPLOYEE_ID = P_EMPLOYEE_CREDITS.P_EMPLOYEE_ID "
				+ " INNER JOIN P_Gain_Credits_Reserve as P_GAIN_CREDITS ON P_GAIN_CREDITS.P_CREDITS_ID = P_EMPLOYEE_CREDITS.P_CREDITS_ID "
				+ " INNER JOIN P_PAYMENT ON P_PAYMENT.P_PAYMENT_ID = " + m_Payment.getP_Payment_ID()
				+ " INNER JOIN P_PAYMENT_GAIN ON P_PAYMENT_GAIN.P_PAYMENT_ID = P_PAYMENT.P_PAYMENT_ID AND P_PAYMENT_GAIN.P_GAIN_PARAMETER_ID = P_GAIN_CREDITS.P_GAIN_PARAMETER_ID "
//				+ "INNER JOIN P_GAIN ON P_GAIN.P_GAIN_ID = P_PAYMENT_GAIN.P_GAIN_ID "
				+ " INNER JOIN P_CREDITS_RETAINER_PARAM p ON p.P_CREDITS_ID = P_EMPLOYEE_CREDITS.P_CREDITS_ID "
				+ " and P.P_COLLECTIVE_LABOUR_AGR_ID = P_Employee.P_COLLECTIVE_LABOUR_AGR_ID "
				+ " and p.P_JOB_TYPE_ID = P_Employee.P_JOB_TYPE_ID "
				+ " and p.P_PAYMENT_GROUP_ID = P_Employee.P_PAYMENT_GROUP_ID "
				+ " WHERE P_Employee.P_Employee_ID = " + m_Payment.getP_Employee_ID() + " AND  P_EMPLOYEE_CREDITS.isactive = 'Y' AND "
				// BV02 on utilise la banque AN05 et non AN01 pour ancienneté
//				+   sldAnc + " between p.Senority_Start and p.Senority_End "
				+ " CASE WHEN P_EMPLOYEE_CREDITS.p_credits_ID = 1101164 THEN " + sldAnc05 + " ELSE " + sldAnc + " END between p.Senority_Start and p.Senority_End "
				+ " GROUP BY P_EMPLOYEE_CREDITS.P_EMPLOYEE_ID,  P_EMPLOYEE_CREDITS.P_CREDITS_ID, P_EMPLOYEE_CREDITS.P_EMPLOYEE_CREDITS_ID"
				;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, getTrxName());
			ResultSet rs = pstmt.executeQuery ();
			while ( rs.next())
			{
				P_Employee_Credits employeeCredits = P_Employee_Credits.get(Env.getCtx (), rs.getInt("P_Employee_Credits_ID"),  getTrxName());

				
				P_Credits_Movement CreditsMovement = new P_Credits_Movement(Env.getCtx (), -1 , getTrxName() ) ;

				CreditsMovement.setP_Credits_ID( employeeCredits.getP_Credits_ID());
				CreditsMovement.setP_Employee_ID( employeeCredits.getP_Employee_ID());
				CreditsMovement.setPMovementDate( m_Period.getEndDate() );
				CreditsMovement.setPMovementOrigin( "FTP");
				CreditsMovement.setPMovementType( "PRO" );
				CreditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());
				
				CreditsMovement.setP_Method_Credits_ID( 1000001 );
//				CreditsMovement.setDescription( "Provision : " + rs.getBigDecimal( "Amt_Reserve" ).toString() );
				CreditsMovement.setDescription( "" );
				CreditsMovement.setPMovementVariation( Env.ZERO );
				CreditsMovement.setPMovementHour(  Env.ZERO );
				CreditsMovement.setAmt_Reserve( rs.getBigDecimal( "Amt_Reserve" ));
				CreditsMovement.setP_Payment_ID( m_Payment.getP_Payment_ID() );
				CreditsMovement.save();
				
			}
		}
		catch(Exception e)
		{
			log.log( Level.SEVERE, "import credits movement : " , e);
		}


	}

	
	public void import_credits_movement()
	{
		// Copie les mouvement de banques uniquement pour le paiment régulier.
		if ( ! m_Payment.getPaymentTypeDoc().equals(P_Payment.PAYMENTTYPEDOC_Regular) )
			return;
			
		P_Employee_Credits employeeCredits;
		P_Credits       credits;
		P_Credits_Param creditsParam;

		Timestamp Day;
		BigDecimal dayVariation = Env.ZERO;
		BigDecimal NbrHour = Env.ZERO;
		P_Period Period = P_Period.get( Env.getCtx(), m_Payment.getP_Period_ID(), null );

		
		String sql = "Select * From P_Particular_Credits Where P_Employee_ID = " + m_Payment.getP_Employee_ID() ;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, getTrxName());
			ResultSet rs = pstmt.executeQuery ();
			while ( rs.next())
			{
				if ( rs.getTimestamp("Day") == null)
					Day = Period.getStartDate();
				else
					Day = rs.getTimestamp("Day");
				
//				if ( rs.getTimestamp("Day") = null)
//					Day = Period.aasdas
				dayVariation = rs.getBigDecimal("DayQty");
				NbrHour = dayVariation;
				employeeCredits = P_Employee_Credits.get(Env.getCtx (), rs.getInt("P_Employee_ID"), rs.getInt("P_Credits_ID"), Day,  getTrxName());
				credits         = P_Credits.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), getTrxName() );

				m_iP_Method_Credits_ID = employeeCredits.getP_Method_Credits_ID();

				creditsParam    = P_Credits_Param.get( Env.getCtx(), employeeCredits.getP_Credits_ID(), m_iP_Method_Credits_ID, Day, getTrxName() );

				P_Credits_Movement CreditsMovement = P_Credits_Movement.get(Env.getCtx (), m_Payment.getP_Payment_ID(), employeeCredits.getP_Credits_ID(), Day, "V+", "IMP", getTrxName() ) ;

				CreditsMovement.setP_Credits_ID( employeeCredits.getP_Credits_ID());
				CreditsMovement.setP_Employee_ID( employeeCredits.getP_Employee_ID());
				CreditsMovement.setPMovementDate( Day );
				CreditsMovement.setPMovementOrigin( "IMP");
				CreditsMovement.setPMovementType( "V+" );
				CreditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());
				
				CreditsMovement.setP_Method_Credits_ID( creditsParam.getP_Method_Credits_ID() );
//				CreditsMovement.setDescription( " ");

				dayVariation = dayVariation.setScale(8, BigDecimal.ROUND_HALF_UP );

				// 2009.01.23
				//TODO HELICO... a paramétriser pour dir conversion ou non.
				if ( ! ( ( m_Payment.getAD_Org_ID() == 1000005 || m_Payment.getAD_Org_ID() == 1000008 ) && m_Payment.getP_Distribution_Booklet_ID() == 1000551))
					dayVariation = convertVariation( credits, creditsParam, dayVariation);


//				dayVariation = convertVariation( credits, creditsParam, dayVariation);
				NbrHour = dayVariation.setScale(2, BigDecimal.ROUND_HALF_UP );		 			 

				if  (  CreditsMovement.getPMovementVariation() != null )
				{
					CreditsMovement.setPMovementVariation( CreditsMovement.getPMovementVariation().add( dayVariation ) );
					CreditsMovement.setPMovementHour( CreditsMovement.getPMovementHour().add( NbrHour ) );
				}
				else
				{
					CreditsMovement.setPMovementVariation( dayVariation );
					CreditsMovement.setPMovementHour(  NbrHour );
				}


				CreditsMovement.setP_Payment_ID( m_Payment.getP_Payment_ID() );
				CreditsMovement.save();
			}
		}
		catch(Exception e)
		{
			log.log( Level.SEVERE, "import credits movement : " , e);
		}

	}

	
	public BigDecimal getEmployeeSoldeBV21(  P_Payment Payment, Timestamp startDate, Timestamp endDate, String trxName ) 
	{
		String sql = null;
		sql = "SELECT SUM( CASE WHEN P_GAIN.value in ('I04', '0051', '0054', '0053', 'I81','I02','I05', '0053','0054', '0055','0059','0074') THEN P_PAYMENT_GAIN.QUANTITYCALC * P_ASSIGNMENT_PARAM.HOURLY_RATE  ELSE P_PAYMENT_GAIN.AMOUNTCALC  END  ) "
				+ " FROM P_PAYMENT_GAIN  "
				+ " INNER JOIN P_GAIN ON P_GAIN.P_GAIN_ID = P_PAYMENT_GAIN.P_GAIN_ID "
				+ " INNER JOIN P_ASSIGNMENT_PARAM ON P_ASSIGNMENT_PARAM.P_ASSIGNMENT_PARAM_ID = P_PAYMENT_GAIN.P_ASSIGNMENT_PARAM_ID "
				+ " WHERE P_PAYMENT_GAIN.P_Payment_ID = " + Payment.getP_Payment_ID()
				+ " AND P_PAYMENT_GAIN.DAY BETWEEN " + DB.TO_DATE(startDate ) + " and " + DB.TO_DATE(endDate ) 
				+ " AND P_PAYMENT_GAIN.P_GAIN_ID IN ( "
				+ " SELECT P_GAIN_PARAMETER.P_GAIN_ID FROM P_CREDITS "
				+ " INNER JOIN P_GAIN_CREDITS ON P_GAIN_CREDITS.P_CREDITS_ID = P_CREDITS.P_CREDITS_ID and P_GAIN_CREDITS.ISACTIVE = 'Y' "
				+ " INNER JOIN P_GAIN_PARAMETER ON P_GAIN_PARAMETER.P_GAIN_PARAMETER_ID = P_GAIN_CREDITS.P_GAIN_PARAMETER_ID "
				+ " INNER JOIN P_EMPLOYEE_CREDITS ON P_EMPLOYEE_CREDITS.P_EMPLOYEE_ID = " + Payment.getP_Employee_ID() + " AND P_EMPLOYEE_CREDITS.P_CREDITS_ID = P_CREDITS.P_CREDITS_ID "
				+ " where P_CREDITS.value like 'BV21%' "
				+ "  and credits_function = '+' ) ";
		//
//		System.out.println ("P_Credits - Sql - " + sql );
		
		PreparedStatement pstmt = null;

		BigDecimal sld = new BigDecimal(0);
		
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				sld = rs.getBigDecimal(1);

			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"getEmployeeSoldeBV21 - " + sql, e);
			System.out.println ("P_Credits - get solde - " + sql + " - " + e);
		}

		if ( sld == null )
		    return  new BigDecimal( 0 );
		
		return sld;
	}

	
}
