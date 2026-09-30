package solstice.process;

import solstice.model.P_Payment;
import solstice.model.P_Payment_Deduction;
import solstice.model.P_Payment_Deduction_Excep;
import solstice.model.P_Payment_Taxable_Benefit;
import solstice.model.P_Period;
import solstice.model.P_Region;
import solstice.model.P_Taxable_Benefit;
import solstice.model.P_Employee_Taxable_Benefit;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Time_Sheet_Error;
import solstice.model.P_Payment_Deduction_Insurance;
import solstice.model.P_Insurance_Dental;
import solstice.model.P_Insurance_Health;
import solstice.model.P_Insurance_Param;
import solstice.model.P_Insurance_LTD;
import solstice.model.P_Insurance_Program;
import solstice.model.P_Insurance_STD;
import solstice.model.P_LongTermLeave_Deduction;
import solstice.model.P_Method_Deduction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.logging.Level;

import org.compiere.model.MRegion;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import solstice.model.P_Assignment_Param;
import solstice.model.P_Deduction;
import solstice.model.P_Deduction_Param;
import solstice.model.P_Employee;
import solstice.model.P_Employee_Deduction;
import solstice.model.P_Employee_Profile;
import solstice.model.P_Frequency;

public class Calcul_insurance {

	private static CLogger		log = CLogger.getCLogger (Calcul_insurance.class);

	static final public int QUEBEC = 166;
	static final public int ONTARIO = 164;
	static final public int NUNAVUT = 163;
	static final public int NWT = 162;
	static final public int MANITOBA = 158;

	
	/**	Big Decimal 0	 */
	static final public java.math.BigDecimal ZERO = new java.math.BigDecimal(0.0);

	public	BigDecimal employerAmount = ZERO;
	public	BigDecimal employeeAmount = ZERO;
	public	BigDecimal salaryAdmissible = ZERO;
	public	BigDecimal hoursAdmissible  = ZERO;
	
	
	static final public int Group9 = 1100069;
	static final public int Group8 = 1100068;
	static final public int Group7 = 1100067;

	
	public void calcul ( P_Payment Payment, P_Period Period, MRegion _RegionResidence, P_Region _Region, String trxName ) 
	{
		MRegion RegionResidence = _RegionResidence;
		P_Region Region = _Region;

		P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), trxName);

		P_Employee_Profile EmployeeProfile = P_Employee_Profile.getProfileInsurance( Env.getCtx(), Employee.getP_Employee_ID(), Period.getEndDate(), trxName);
		
		P_Time_Sheet Timesheet = P_Time_Sheet.GetWithPaymentID( Env.getCtx(), Payment.getP_Payment_ID(), trxName );
	
		boolean isDI21Exonerated = false;


/*		
		P_Insurance_Program insurance_Program = null;
		if ( EmployeeProfile != null )
		{	
			insurance_Program = P_Insurance_Program.getByProfile(Env.getCtx(), EmployeeProfile.getP_Profile_ID() ,trxName);
		}
*/
		// Pas d'assurance sur compte de dépense.
		if ( Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_ExpenseAccount))
			return;
		
		String query = "SELECT P_Deduction.value, P_Deduction.name, P_Deduction.P_TAXABLE_BENEFIT_ID, P_Method_Deduction.Priority, P_Deduction.P_Deduction_ID, P_Deduction_Param.P_Deduction_Param_ID, P_Employee_Deduction.P_Employee_Deduction_ID, IsEmployerWhenZero \r\n"
				+ "FROM P_Deduction \r\n"
				+ "INNER JOIN P_Employee_Deduction ON P_Employee_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID \r\n"
				+ "INNER JOIN P_Deduction_Param ON P_Deduction_Param.P_Deduction_ID = P_Deduction.P_Deduction_ID  \r\n"
				+ "INNER JOIN P_Method_Deduction ON P_Method_Deduction.P_Method_Deduction_ID = P_Deduction_Param.P_Method_Deduction_ID \r\n"
				+ "\r\n"
				+ "WHERE P_Deduction.IsActive='Y'  \r\n"
				+ "AND P_Employee_Deduction.P_Employee_ID= " + Employee.getP_Employee_ID()
				+ " AND ( P_Employee_Deduction.EffectTo is null OR P_Employee_Deduction.EffectTo >= " + DB.TO_DATE( Period.getStartDate() ) + " )  \r\n"
				+ " AND P_Employee_Deduction.EffectIn in ( Select Max( EffectIn) FROM P_Employee_Deduction dat \r\n"
				+ " WHERE dat.P_Deduction_ID = P_Deduction.P_Deduction_ID and dat.P_Employee_ID = P_Employee_Deduction.P_Employee_ID \r\n"
				+ "and dat.EffectIn <= " + DB.TO_DATE( Period.getEndDate() ) +")  \r\n"
				+ "AND P_Deduction_Param.EffectIn in ( Select Max( EffectIn) FROM P_Deduction_PARAM \r\n"
				+ "WHERE P_Deduction_PARAM.P_Deduction_ID = P_Deduction.P_Deduction_ID and P_Deduction_PARAM.EffectIn <= " + DB.TO_DATE( Period.getEndDate() ) + ")\r\n"
				+ "AND P_DEDUCTION.P_DEDUCTION_FAMILY_ID = 1000001 \r\n"
				+ "ORDER BY P_Method_Deduction.Priority, P_Deduction.P_Deduction_ID ";
		
		PreparedStatement pstmt = DB.prepareStatement (query, trxName);

		
		Timestamp l_EffectIn = Period.getStartDate();
		if ( Employee.getDateHired().compareTo( Period.getStartDate() ) > 0)
			l_EffectIn = Employee.getDateHired();

		
		Hashtable<Integer,P_Employee_Deduction>	s_cacheEmployeeDeduction = new Hashtable<Integer,P_Employee_Deduction>();
		Hashtable<Integer,P_Deduction>	s_cacheDeduction = new Hashtable<Integer,P_Deduction>();
		Hashtable<Integer,P_Deduction_Param>	s_cacheDeductionParam = new Hashtable<Integer,P_Deduction_Param>();
		Hashtable<Integer,BigDecimal>	s_cacheSalaryAdmissible = new Hashtable<Integer,BigDecimal>();
		Hashtable<Integer,BigDecimal>	s_cacheHoursAdmissible = new Hashtable<Integer,BigDecimal>();
		Hashtable<Integer,BigDecimal>	s_cacheEmployee = new Hashtable<Integer,BigDecimal>();
		Hashtable<Integer,BigDecimal>	s_cacheEmployer = new Hashtable<Integer,BigDecimal>();
		
		BigDecimal EmployeeTotal = ZERO;
		BigDecimal EmployerTotal = ZERO;
		int nbrRec = 0;
		
		try
		{
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				if (EmployeeProfile == null)
				{
					P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "S.v.p Vérifier le profiles d'assurances", trxName);
					log.info( "S.v.p Vérifier le profiles d'assurances " + Employee.getValue() + " " + Employee.getName() ) ;	
				}
				
				
				this.employerAmount = ZERO;
				this.employeeAmount = ZERO;
				this.salaryAdmissible = ZERO;
				this.hoursAdmissible  = ZERO;	


				
				P_Employee_Deduction EmployeeDeduction = P_Employee_Deduction.get( Env.getCtx (), rs.getInt("P_Employee_Deduction_ID"), trxName);
				P_Deduction Deduction         = P_Deduction.get( Env.getCtx(), EmployeeDeduction.getP_Deduction_ID(), trxName);
				P_Deduction_Param Deduction_Param   = P_Deduction_Param.get( Env.getCtx(), rs.getInt("P_Deduction_Param_ID") , trxName);

				Integer key = new Integer ( nbrRec );


				
				boolean message = false;
				// pour les groupes 7 valide les déductions
				if ( EmployeeProfile != null && EmployeeProfile.getP_Profile_ID() == 1100067 ) 
				{
					if ( Deduction.isActive() 
							&& EmployeeDeduction.isActive() 
							&& EmployeeDeduction.getEffectTo() == null 
						 && ( 
						 Deduction.getValue().equals( "DI21") ||
						 Deduction.getValue().equals( "DI23") ||
						 Deduction.getValue().equals( "DI25") ||
						 Deduction.getValue().equals( "DI26")
						 )
						)
						message = true;
				}

				// pour les groupes 8 valide les déductions
				if ( EmployeeProfile.getP_Profile_ID() == Group8 )
				{
					if ( Deduction.isActive() 
							&& EmployeeDeduction.isActive() 
							&& EmployeeDeduction.getEffectTo() == null 
							&&  (
						 Deduction.getValue().equals( "DI21") ||
						 Deduction.getValue().equals( "DI23") ||
						 Deduction.getValue().equals( "DI25") ||
						 Deduction.getValue().equals( "DI26") ||
						 Deduction.getValue().equals( "DI27")
						 )
						)
						message = true;
					
				}
				// pour les groupes 9 valide les déductions
				if ( EmployeeProfile.getP_Profile_ID() == Group9 )
				{
					if ( Deduction.isActive() 
							&& EmployeeDeduction.isActive() 
							&& EmployeeDeduction.getEffectTo() == null 
							&& ( 
						 Deduction.getValue().equals( "DI23") ||
						 Deduction.getValue().equals( "DI24") ||
						 Deduction.getValue().equals( "DI25") ||
						 Deduction.getValue().equals( "DI26") ||
						 Deduction.getValue().equals( "DI27") ) 
						)
						message = true;
					
				}

				if (message == true)
				{
					P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "S.v.p Vérifier les déductions d'assurances", trxName);
				}
				
				
				P_Method_Deduction Method_Deduction  = P_Method_Deduction.get( Env.getCtx (), Deduction_Param.getP_Method_Deduction_ID() , trxName);

				if ( EmployeeDeduction.isExonerated() )
				{
					log.info( "Calcul Deduction Exonerated : " + Deduction.getValue() + " - " + Deduction.getName() ) ;		
				}

// 2025-06-16
//				if ( EmployeeDeduction.isActive() && ! EmployeeDeduction.isExonerated()  )
				if ( EmployeeDeduction.isActive()   )
				{
					
					if ( Deduction.getValue().equals("DI21") && EmployeeDeduction.isExonerated())
						isDI21Exonerated = true;
					
//					log.info( "Calcul Deduction : " + Deduction.getValue() + " - " + Deduction.getName() ) ;		
					calcul_deduction( Payment, Timesheet, Period, Employee, RegionResidence, Region, Method_Deduction,  EmployeeDeduction, Deduction, Deduction_Param, true, trxName );

					log.info( "Calcul Deduction : " + Deduction.getValue() + " - " + Deduction.getName() + " salaryAdmissible : " + salaryAdmissible + " employeeAmount : " + employeeAmount + " employerAmount : " + employerAmount ) ;		

					if ( employeeAmount == null ) employeeAmount = ZERO;
					if ( employerAmount == null ) employerAmount = ZERO;
					
					//2025-06-23
					if ( EmployeeDeduction.isExonerated() )
					{
						employeeAmount = ZERO;
						employerAmount = ZERO;
					}
					
					s_cacheEmployeeDeduction.remove(key);
					s_cacheEmployeeDeduction.put (key, EmployeeDeduction);
					s_cacheDeduction.remove(key);
					s_cacheDeduction.put (key, Deduction);
					s_cacheDeductionParam.remove(key);
					s_cacheDeductionParam.put (key, Deduction_Param);

					s_cacheSalaryAdmissible.remove(key);
					s_cacheSalaryAdmissible.put (key, salaryAdmissible);
					s_cacheHoursAdmissible.remove(key);
					s_cacheHoursAdmissible.put (key, hoursAdmissible);
					s_cacheEmployee.remove(key);
					s_cacheEmployee.put (key, employeeAmount);
					s_cacheEmployer.remove(key);
					s_cacheEmployer.put (key, employerAmount);
					
					EmployeeTotal = EmployeeTotal.add(  employeeAmount );
					EmployerTotal = EmployerTotal.add(  employerAmount );
					
					nbrRec += 1;
				}

				
				
/*				P_Taxable_Benefit Taxable_Benefit = P_Taxable_Benefit.get( Env.getCtx (),  rs.getInt("P_Taxable_Benefit_ID"), trxName );

				if ( Taxable_Benefit.getC_Region_ID() == 0 || Taxable_Benefit.getC_Region_ID() == Payment.getTaxation_Region_ID() )
				{
					P_Employee_Taxable_Benefit EmployeeTaxableBenefit = P_Employee_Taxable_Benefit.get( Env.getCtx (),  Employee.getP_Employee_ID(), Taxable_Benefit.getP_Taxable_Benefit_ID(),  Period.getEndDate() ,trxName );
					if ( EmployeeTaxableBenefit.getP_Employee_Taxable_Benefit_ID() <= 0)
					{
						EmployeeTaxableBenefit.setP_Employee_ID(Payment.getP_Employee_ID());
						EmployeeTaxableBenefit.setP_Taxable_Benefit_ID( Taxable_Benefit.getP_Taxable_Benefit_ID());
						EmployeeTaxableBenefit.setIsActive(true);
						EmployeeTaxableBenefit.setAD_Org_ID(Payment.getAD_Org_ID());
						EmployeeTaxableBenefit.setEffectIn(l_EffectIn);
						EmployeeTaxableBenefit.save();
						P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol09-I") + Taxable_Benefit.getValue(), trxName);
					}
					
				}
*/				
				
			}
			
			// Optimisation fiscal
			BigDecimal total = EmployeeTotal.add( EmployerTotal );
			BigDecimal repartir = total.divide( new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP);
			
			BigDecimal maxEmr = getInsuranceMaxEmployer( Timesheet, Period, Employee, Payment, s_cacheMaxEmployer , trxName);
			
			//Goupe 8 et 9 si exonoré
			if ( maxEmr != null && maxEmr.compareTo( new BigDecimal(99)) == 0 && ( EmployeeProfile.getP_Profile_ID() == Group8 || EmployeeProfile.getP_Profile_ID() == Group9 ))
				maxEmr = repartir;

			// 2025-08-18
			if ( EmployeeProfile != null && maxEmr == null && EmployeeProfile.getP_Profile_ID() == Group8  )
				maxEmr = total.multiply( new BigDecimal( 0.45 ));

			if ( maxEmr == null ) maxEmr = ZERO;
			
			
			/*
			if ( repartir.compareTo(maxEmr) > 0 &&  maxEmr.compareTo(ZERO) != 0 && maxEmr.compareTo(new BigDecimal(99999)) != 0 )
			{
				repartir = maxEmr;
			}
			*/
//2025-06-13
//			if ( maxEmr.compareTo(ZERO) != 0 && maxEmr.compareTo(new BigDecimal(99999)) != 0  )
// 2026-09-22			
			if ( maxEmr.compareTo(ZERO) != 0 && maxEmr.compareTo(new BigDecimal(99999)) != 0 && repartir.compareTo( maxEmr) > 0   )
			{
				repartir = maxEmr;
			}
			
			
			BigDecimal A_repartir = repartir;

			// Profile 7
			if ( EmployeeProfile != null && EmployeeProfile.getP_Profile_ID() == 1100067  )
			{
				for ( int i = nbrRec -1; i >= 0; i-- )
				{
					
					Integer key = new Integer ( i );
					BigDecimal emp = (BigDecimal)s_cacheEmployee.get( key );
					BigDecimal emr = (BigDecimal)s_cacheEmployer.get( key );
					P_Deduction Deduction = (P_Deduction)s_cacheDeduction.get( key );
					P_Employee_Deduction EmployeeDeduction = (P_Employee_Deduction)s_cacheEmployeeDeduction.get( key );
					BigDecimal salaryAdmissible = (BigDecimal)s_cacheSalaryAdmissible.get( key );
					BigDecimal hoursAdmissible =  (BigDecimal)s_cacheHoursAdmissible.get( key );
					P_Deduction_Param Deduction_Param = (P_Deduction_Param)s_cacheDeductionParam.get( key );
					
					log.info( "Calcul Deduction : " + Deduction.getValue() + " - " + Deduction.getName() ); 
					
					// Optimisation fiscal juste pour les groupes 7 8 et 9
//					if ( EmployeeProfile.getP_Profile_ID() == 1100067 || EmployeeProfile.getP_Profile_ID() == Group8 || EmployeeProfile.getP_Profile_ID() == Group9 )
					{
						if ( repartir.compareTo(ZERO) == 0 && emr.compareTo(ZERO) != 0)
						{
							emp = emr;
							emr = ZERO;
						}
						
						
						if ( ! Deduction.getValue().equals( "DI26G7EX" ) )
						{
							if ( emp.add( emr ).compareTo( ZERO ) == 0 )
							{
								emr = repartir;
								emp = ZERO;
								
							}
							else if ( repartir.compareTo(ZERO) > 0 )
							{
								if ( emp.add( emr ).compareTo( repartir ) < 0  )
								{
									emr = emp.add( emr );
									emp = ZERO;
									
									repartir = repartir.subtract(emr);
								}
								else {
									BigDecimal tmp = (emp.add( emr ));
									emr = repartir;
//									if ( Deduction.getValue().equals("DI21") )
									if ( i == 0 )
									{
										emp = emp.subtract(emr);
										
									}
									else
									{
										emp = tmp.subtract(emr);
									}

									repartir = repartir.subtract(emr);

								}
								
								s_cacheEmployee.remove(key);
								s_cacheEmployee.put (key, emp);
								s_cacheEmployer.remove(key);
								s_cacheEmployer.put (key, emr);
							}
							else
							{
								s_cacheEmployer.remove(key);
								s_cacheEmployer.put (key, ZERO);
								
								emr = ZERO;
																		
							}
						}
					}
						
					boolean isExonerated = false;
					BigDecimal amts[] = Payment_Excep( Payment, Period, EmployeeDeduction,  salaryAdmissible, hoursAdmissible, emp, emr, isExonerated, trxName);					
					
					salaryAdmissible = amts[0];
					hoursAdmissible = amts[1];
					emp = amts[2];
					emr = amts[3];
					
					if ( EmployeeDeduction.isExonerated() )
					{
						emp = ZERO;
						emr = ZERO;
						hoursAdmissible = ZERO;
						salaryAdmissible = ZERO;
						
						isExonerated = true;
						
					}

					P_Period TimeSheetPeriod = P_Period.get( Env.getCtx(), Payment.getP_Period_ID() , trxName);
					BigDecimal cumAmts[] = get_deduction_cumulative_per( Payment, Period, EmployeeDeduction, TimeSheetPeriod, trxName );
					
					salaryAdmissible = salaryAdmissible.subtract( cumAmts[0] );
					hoursAdmissible  = hoursAdmissible.subtract( cumAmts[1] );
					emp   = emp.subtract( cumAmts[2] );
					emr   = emr.subtract( cumAmts[3] );
					
					P_Payment_Deduction_Insurance PaymentDeduction = new P_Payment_Deduction_Insurance ( Env.getCtx (), -1, trxName);
//					P_Payment_Deduction PaymentDeduction = new P_Payment_Deduction ( Env.getCtx (), -1, trxName);
					
//					PaymentDeduction.setP_Employee_ID( Payment.getP_Employee_ID ());
					PaymentDeduction.setP_Deduction_ID( EmployeeDeduction.getP_Deduction_ID() );
//					PaymentDeduction.setP_Year_ID(  Period.getP_Year_ID () );
					PaymentDeduction.setP_Period_ID( Period.getP_Period_ID () );
					PaymentDeduction.setP_Payment_ID( Payment.getP_Payment_ID ());
					
					BigDecimal NetPay   = Payment.getGrossEarnings();

					//
					// Recupération de Arrérage.
					//
					P_Method_Deduction method_deduction = P_Method_Deduction.get(Env.getCtx(), Deduction_Param.getP_Method_Deduction_ID() , null);
					
					if (  method_deduction.isAccumulation() )
					{
// 2025-11-18						
//						BigDecimal RetrievalAmount = EmployeeDeduction.getToRetrieveAmount( Deduction_Param, employeeAmount);
						BigDecimal RetrievalAmount = EmployeeDeduction.getToRetrieveAmount( Deduction_Param, emp);
				        if ( RetrievalAmount.compareTo( ZERO) != 0 )
						{
							if ( ( NetPay.subtract( emp ).subtract( RetrievalAmount ) ) .compareTo( ZERO) < 0 )
							{
								RetrievalAmount = NetPay.subtract( emp ); 
							}

							// 2025-08-25
							if ( RetrievalAmount.compareTo( Env.ZERO ) < 0 )
							{
								RetrievalAmount = Env.ZERO; 
							}
								
							
							PaymentDeduction.setRetrievalAmount( RetrievalAmount );
							emp = emp.add( RetrievalAmount );
						}
					}


					if ( NetPay.subtract( emp ).compareTo( ZERO) < 0  )
					{
						PaymentDeduction.setAccumulationAmount( emp.subtract( NetPay ).setScale(2, BigDecimal.ROUND_HALF_UP ) );
						emp = NetPay ;
					}

					PaymentDeduction.setEmployee_Part( emp.setScale(2,BigDecimal.ROUND_HALF_UP) );
					PaymentDeduction.setEmployer_Part( emr.setScale(2,BigDecimal.ROUND_HALF_UP) );
					PaymentDeduction.setSalary_Eligible( salaryAdmissible.setScale(2,BigDecimal.ROUND_HALF_UP) );
					PaymentDeduction.setHours_Eligible( hoursAdmissible.setScale(2,BigDecimal.ROUND_HALF_UP) );
					PaymentDeduction.setIsExonerated( isExonerated );
					PaymentDeduction.setOrigine("FTP");
					PaymentDeduction.setP_Deduction_Param_ID( Deduction_Param.getP_Deduction_Param_ID());
					PaymentDeduction.setP_Employee_Deduction_ID( EmployeeDeduction.getP_Employee_Deduction_ID());
					PaymentDeduction.save( );
					
					if ( Deduction.getP_Taxable_Benefit_ID() != 0 && emr.compareTo(ZERO) != 0 )
					{
						P_Taxable_Benefit Taxable_Benefit = P_Taxable_Benefit.get( Env.getCtx (),  Deduction.getP_Taxable_Benefit_ID(), trxName );
						P_Employee_Taxable_Benefit EmployeeTaxableBenefit = P_Employee_Taxable_Benefit.get( Env.getCtx (),  Employee.getP_Employee_ID(), Taxable_Benefit.getP_Taxable_Benefit_ID(),  Period.getEndDate() ,trxName );

						if ( EmployeeTaxableBenefit.isActive() && ( Taxable_Benefit.getC_Region_ID() == 0 || ( Taxable_Benefit.getC_Region_ID() != 0 && RegionResidence.getC_Region_ID() == Taxable_Benefit.getC_Region_ID() ) ))
						{
							P_Payment_Taxable_Benefit PTaxable_Benefit = P_Payment_Taxable_Benefit.get( Env.getCtx (), Payment.getP_Payment_ID (), Deduction.getP_Taxable_Benefit_ID(), Period.getP_Period_ID () , trxName );
							PTaxable_Benefit.setP_Employee_ID( Payment.getP_Employee_ID ());
							PTaxable_Benefit.setP_Taxable_Benefit_ID( Deduction.getP_Taxable_Benefit_ID() );
							PTaxable_Benefit.setP_Year_ID(  Period.getP_Year_ID () );
							PTaxable_Benefit.setP_Period_ID( Period.getP_Period_ID () );
							PTaxable_Benefit.setP_Payment_ID( Payment.getP_Payment_ID ());
//1
							if ( PTaxable_Benefit.getTaxable_Benefit_Amount() != null )
								PTaxable_Benefit.setTaxable_Benefit_Amount( PTaxable_Benefit.getTaxable_Benefit_Amount().add( emr ) );
							else
								PTaxable_Benefit.setTaxable_Benefit_Amount( emr  );
							PTaxable_Benefit.setP_Employee_Taxable_Benefit_ID( EmployeeTaxableBenefit.getP_Employee_Taxable_Benefit_ID() );
							PTaxable_Benefit.setOrigine("FTP");
							
							PTaxable_Benefit.save( );
							
						}
					}
				
				}
				
			}
			else
			{
				BigDecimal totalEmr = ZERO;
				BigDecimal totalEmp = ZERO;
				for ( int i = 0; i < nbrRec ; i++ )
				{
					
					Integer key = new Integer ( i );
					BigDecimal emp = (BigDecimal)s_cacheEmployee.get( key );
					BigDecimal emr = (BigDecimal)s_cacheEmployer.get( key );
					P_Deduction Deduction = (P_Deduction)s_cacheDeduction.get( key );
					P_Employee_Deduction EmployeeDeduction = (P_Employee_Deduction)s_cacheEmployeeDeduction.get( key );
					BigDecimal salaryAdmissible = (BigDecimal)s_cacheSalaryAdmissible.get( key );
					BigDecimal hoursAdmissible =  (BigDecimal)s_cacheHoursAdmissible.get( key );
					P_Deduction_Param Deduction_Param = (P_Deduction_Param)s_cacheDeductionParam.get( key );
							
					log.info( "Calcul Deduction : " + Deduction.getValue() + " - " + Deduction.getName() ); 
					
					// Optimisation fiscal juste pour les groupes 8 et 9
					if ( maxEmr.compareTo(ZERO) != 0 && ( EmployeeProfile.getP_Profile_ID() == Group8 || EmployeeProfile.getP_Profile_ID() == Group9 ))
					{		
						
						if ( repartir.compareTo(ZERO) == 0 && emr.compareTo(ZERO) != 0)
						{
							emp = emr;
							emr = ZERO;
						}
						
						if ( EmployeeDeduction.isExonerated())
							continue;
						
//						if ( Deduction.getValue().equals( "DI21" ) && EmployeeProfile.getP_Profile_ID() == Group9 && maxEmr.compareTo(A_repartir) > 0 )
						if ( i == nbrRec -1 && EmployeeProfile.getP_Profile_ID() == Group9 && maxEmr.compareTo(A_repartir) > 0  )
						{
							if ( totalEmp.compareTo( A_repartir ) <= 0 )
								emp = A_repartir.subtract(totalEmp);

							if ( totalEmr.compareTo( A_repartir ) <= 0 )
							{
								emr = A_repartir.subtract(totalEmr);
								repartir = ZERO;
							}

						}
						else if ( Deduction.getValue().equals( "DI23G9" )	||
								Deduction.getValue().equals( "DI24G9" )   
							|| Deduction.getValue().equals( "DI25" )   
							|| Deduction.getValue().equals( "DI25G9C" )   
							|| Deduction.getValue().equals( "DI25G9F" )   
							|| Deduction.getValue().equals( "DI25G9" )   	
							)
						{
							repartir = repartir.subtract(emr);
// Condition 							
							if (  Deduction.getValue().equals( "DI24G9" ) )
									repartir = repartir.subtract(emp.divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP));
							
							if ( isDI21Exonerated && Deduction.getValue().equals( "DI23G9") && repartir.compareTo( ZERO ) > 0 && emp.compareTo(ZERO) > 0 )
							{
								emr = repartir;
								emp = emp.subtract(repartir);
								if ( emp.compareTo(ZERO ) < 0 ) emp = ZERO;							
							}
								
						}
						else
						{
							if ( repartir.compareTo(ZERO) > 0 )
							{
								if ( emp.add( emr ).compareTo( ZERO ) == 0 )
								{
									emr = repartir;
									emp = ZERO;
									
								}
								else if ( emp.add( emr ).compareTo( repartir ) < 0  )
								{
									emr = emp.add( emr );
									
/*									if ( Deduction.getValue().equals( "DI23G9"))
										emr = emp.add( emr ).add( repartir );
*/										
									emp = ZERO;
									
									repartir = repartir.subtract(emr);
								}
								else {
									BigDecimal tmp = (emp.add( emr ));
									emr = repartir;
//									emp = tmp.subtract(emr);
									// i == nbrRec
//									if ( EmployeeProfile.getP_Profile_ID() == Group9 && Deduction.getValue().equals("DI21"))
									if ( EmployeeProfile.getP_Profile_ID() == Group9 && i == nbrRec -1 )
									{
										emr = A_repartir.subtract(repartir);
										
//										if ( A_repartir.compareTo( totalEmr.add(emr)  ) < 0  )
										if ( totalEmr.compareTo( maxEmr ) < 0 )
										{
											emr = A_repartir.subtract(totalEmr);
//											emr = A_repartir.subtract(repartir);
										}
										
/*										if ( A_repartir.compareTo(maxEmr) == 0 &&
												(repartir.subtract(emr)).compareTo(ZERO) < 0 )
										{
											emr = A_repartir.subtract(repartir);
										}
*/										
										BigDecimal tmpEmp = emp;
										emp = emp.subtract(emr);

										if ( totalEmr.add( emr ).compareTo( totalEmp.add(emp) ) > 0 )
										{
											emr = A_repartir.subtract(repartir);
											emp = tmpEmp.subtract(emr);
										}
										
										if ( totalEmr.add( emr ).compareTo( totalEmp.add(emp) ) > 0 )
										{
											BigDecimal tmp2 = ((totalEmr.add( emr )).subtract( totalEmp.add(emp) ) ).divide( new BigDecimal(2), 2, BigDecimal.ROUND_UP); 
											emr = emr.subtract( tmp2 );  
											emp = emp.add( tmp2 );
										}
										

										
									}
/*									if ( EmployeeProfile.getP_Profile_ID() == Group9 && Deduction.getValue().equals("DI21"))
									{
										if ( A_repartir.compareTo(maxEmr) < 0 )
											emr = A_repartir.subtract(repartir);
										else	
											emr = maxEmr.subtract(repartir);
										emp = emp.subtract(emr);
									}
*/
//									else if ( EmployeeProfile.getP_Profile_ID() == Group8 && Deduction.getValue().equals("DI21"))
									else if ( EmployeeProfile.getP_Profile_ID() == Group8 && i == nbrRec -1)
									{
										emp = emp.subtract(emr);
									}
									else
									{
										emp = tmp.subtract(emr);
									}
									
										
									
									repartir = repartir.subtract(emr);

								}
								
								s_cacheEmployee.remove(key);
								s_cacheEmployee.put (key, emp);
								s_cacheEmployer.remove(key);
								s_cacheEmployer.put (key, emr);
							}
							else
							{
								s_cacheEmployer.remove(key);
								s_cacheEmployer.put (key, ZERO);
								
								emr = ZERO;
																		
							}
						}
					}
					
					totalEmr= totalEmr.add( emr );
					totalEmp= totalEmp.add( emp );
					
					boolean isExonerated = false;
					BigDecimal amts[] = Payment_Excep( Payment, Period, EmployeeDeduction,  salaryAdmissible, hoursAdmissible, emp, emr, isExonerated, trxName);					
					
					salaryAdmissible = amts[0];
					hoursAdmissible = amts[1];
					emp = amts[2];
					emr = amts[3];

					if ( EmployeeDeduction.isExonerated() )
					{
						emp = ZERO;
						emr = ZERO;
						hoursAdmissible = ZERO;
						salaryAdmissible = ZERO;
						
						isExonerated = true;
						
					}
					
					P_Period TimeSheetPeriod = P_Period.get( Env.getCtx(), Payment.getP_Period_ID() , trxName);

					BigDecimal cumAmts[] = get_deduction_cumulative_per( Payment, Period, EmployeeDeduction, TimeSheetPeriod, trxName );
					
					salaryAdmissible = salaryAdmissible.subtract( cumAmts[0] );
					hoursAdmissible  = hoursAdmissible.subtract( cumAmts[1] );
					emp   = emp.subtract( cumAmts[2] );
					emr   = emr.subtract( cumAmts[3] );
					
					
					P_Payment_Deduction_Insurance PaymentDeduction = new P_Payment_Deduction_Insurance ( Env.getCtx (), -1, trxName);
					
//					PaymentDeduction.setP_Employee_ID( Payment.getP_Employee_ID ());
					PaymentDeduction.setP_Deduction_ID( EmployeeDeduction.getP_Deduction_ID() );
//					PaymentDeduction.setP_Year_ID(  Period.getP_Year_ID () );
					PaymentDeduction.setP_Period_ID( Period.getP_Period_ID () );
					PaymentDeduction.setP_Payment_ID( Payment.getP_Payment_ID ());
					
//					BigDecimal NetPay   = Payment.getGrossEarnings();
					BigDecimal NetPay   = Payment.getNetPay();

					//
					// Recupération de Arrérage.
					//
					P_Method_Deduction method_deduction = P_Method_Deduction.get(Env.getCtx(), Deduction_Param.getP_Method_Deduction_ID() , null);
					
					if (  method_deduction.isAccumulation() )
					{
//						BigDecimal RetrievalAmount = EmployeeDeduction.getToRetrieveAmount( Deduction_Param, employeeAmount);
						BigDecimal RetrievalAmount = EmployeeDeduction.getToRetrieveAmount( Deduction_Param, emp);
				        if ( RetrievalAmount.compareTo( ZERO) != 0 )
						{
							if ( ( NetPay.subtract( emp ).subtract( RetrievalAmount ) ) .compareTo( ZERO) < 0 )
							{
								RetrievalAmount = NetPay.subtract( emp ); 
							}

							// 2025-08-25
							if ( RetrievalAmount.compareTo( Env.ZERO ) < 0 )
							{
								RetrievalAmount = Env.ZERO; 
							}
							PaymentDeduction.setRetrievalAmount( RetrievalAmount );
							emp = emp.add( RetrievalAmount );
						}
					}
					
					if ( NetPay.subtract( emp ).compareTo( ZERO) <= 0  )
					{
						PaymentDeduction.setAccumulationAmount( emp.subtract( NetPay ).setScale(2, BigDecimal.ROUND_HALF_UP ) );
						emp = emp.subtract(PaymentDeduction.getAccumulationAmount())  ;
					}
					
					PaymentDeduction.setEmployee_Part( emp.setScale(2,BigDecimal.ROUND_HALF_UP) );
					PaymentDeduction.setEmployer_Part( emr.setScale(2,BigDecimal.ROUND_HALF_UP) );
					PaymentDeduction.setSalary_Eligible( salaryAdmissible.setScale(2,BigDecimal.ROUND_HALF_UP) );
					PaymentDeduction.setHours_Eligible( hoursAdmissible.setScale(2,BigDecimal.ROUND_HALF_UP) );
					PaymentDeduction.setIsExonerated( isExonerated );
					PaymentDeduction.setOrigine("FTP");
					PaymentDeduction.setP_Deduction_Param_ID( Deduction_Param.getP_Deduction_Param_ID());
					PaymentDeduction.setP_Employee_Deduction_ID( EmployeeDeduction.getP_Employee_Deduction_ID());
					PaymentDeduction.save( );
					
					if ( Deduction.getP_Taxable_Benefit_ID() != 0 && emr.compareTo(ZERO) != 0 )
					{
						P_Taxable_Benefit Taxable_Benefit = P_Taxable_Benefit.get( Env.getCtx (),  Deduction.getP_Taxable_Benefit_ID(), trxName );
						P_Employee_Taxable_Benefit EmployeeTaxableBenefit = P_Employee_Taxable_Benefit.get( Env.getCtx (),  Employee.getP_Employee_ID(), Taxable_Benefit.getP_Taxable_Benefit_ID(),  Period.getEndDate() ,trxName );

						if ( EmployeeTaxableBenefit.isActive() && ( Taxable_Benefit.getC_Region_ID() == 0 || ( Taxable_Benefit.getC_Region_ID() != 0 && RegionResidence.getC_Region_ID() == Taxable_Benefit.getC_Region_ID() ) ))
						{
							P_Payment_Taxable_Benefit PTaxable_Benefit = P_Payment_Taxable_Benefit.get( Env.getCtx (), Payment.getP_Payment_ID (), Deduction.getP_Taxable_Benefit_ID(), Period.getP_Period_ID () , trxName );
							PTaxable_Benefit.setP_Employee_ID( Payment.getP_Employee_ID ());
							PTaxable_Benefit.setP_Taxable_Benefit_ID( Deduction.getP_Taxable_Benefit_ID() );
							PTaxable_Benefit.setP_Year_ID(  Period.getP_Year_ID () );
							PTaxable_Benefit.setP_Period_ID( Period.getP_Period_ID () );
							PTaxable_Benefit.setP_Payment_ID( Payment.getP_Payment_ID ());
//2
							if ( PTaxable_Benefit.getTaxable_Benefit_Amount() != null )
								PTaxable_Benefit.setTaxable_Benefit_Amount( PTaxable_Benefit.getTaxable_Benefit_Amount().add( emr ) );
							else
								PTaxable_Benefit.setTaxable_Benefit_Amount( emr  );
							PTaxable_Benefit.setP_Employee_Taxable_Benefit_ID( EmployeeTaxableBenefit.getP_Employee_Taxable_Benefit_ID() );
							PTaxable_Benefit.setOrigine("FTP");
							
							PTaxable_Benefit.save( );
							
							PTaxable_Benefit = null;
							
						}
					}
				
				}
				
			}
				
//			for ( int i = nbrRec -1; i >= 0; i-- )
	
		}
		catch (Exception e)
		{
	        P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), trxName);
			Timesheet.setTimeSheetStatus( P_Time_Sheet.TIMESHEETSTATUS_Error );
			Timesheet.save( );
			log.log (Level.SEVERE,"calcul - " + query, e);
		}

		

			
	}

	private void calcul_deduction( P_Payment Payment, P_Time_Sheet Timesheet, P_Period Period, P_Employee Employee, MRegion RegionResidence, P_Region Region, P_Method_Deduction   Method_Deduction, P_Employee_Deduction EmployeeDeduction, P_Deduction Deduction, P_Deduction_Param Deduction_Param, boolean taxable_Benefit, String trxName) throws Exception
	{
		String    methodCalcul     = Method_Deduction.getValue();
		int       i_methodCalcul   = new Integer( methodCalcul ).intValue();
		
		switch ( i_methodCalcul ) 
		{
			case 14:    calcul_insurance( Payment, Timesheet, Period, Employee, RegionResidence, Region, Method_Deduction, EmployeeDeduction, Deduction, Deduction_Param, trxName);
			break;
			case 32:    calcul_insurance_M32( Payment, Timesheet, Period, Employee, RegionResidence, Region, Method_Deduction, EmployeeDeduction, Deduction, Deduction_Param, trxName);
			break;
			case 33:    calcul_insurance_LTD( Payment, Timesheet, Period, Employee, RegionResidence, Region, Method_Deduction, EmployeeDeduction, Deduction, Deduction_Param, trxName ); // Long-Term Disability Insurance
			break;
			case 35:    calcul_insurance_M35( Payment, Timesheet, Period, Employee, RegionResidence, Region, Method_Deduction, EmployeeDeduction, Deduction, Deduction_Param, trxName);
			break;
			case 80:    calcul_insurance_M80( Payment, Timesheet, Period, Employee, RegionResidence, Region, Method_Deduction, EmployeeDeduction, Deduction, Deduction_Param, trxName);  // Invalité courte durée
			break;
			case 81:    calcul_insurance_M81( Payment, Timesheet, Period, Employee, RegionResidence, Region, Method_Deduction, EmployeeDeduction, Deduction, Deduction_Param, taxable_Benefit, trxName );
			break;
			case 82:    calcul_insurance_M82( Payment, Timesheet, Period, Employee, RegionResidence, Region, Method_Deduction, EmployeeDeduction, Deduction, Deduction_Param, trxName);
			break;
			case 84:    calcul_insurance_M81( Payment, Timesheet, Period, Employee, RegionResidence, Region, Method_Deduction, EmployeeDeduction, Deduction, Deduction_Param, taxable_Benefit, trxName );
			break;
			default :   calcul_deduction_others( Payment, Timesheet, Period, Employee, RegionResidence, Region, Method_Deduction, EmployeeDeduction, Deduction, Deduction_Param, trxName );

		}
	}

	//
	// calcul_insurance
	//
	private void calcul_insurance( P_Payment Payment, P_Time_Sheet Timesheet, P_Period Period, P_Employee Employee, MRegion RegionResidence, P_Region Region, P_Method_Deduction   Method_Deduction, P_Employee_Deduction EmployeeDeduction, P_Deduction Deduction, P_Deduction_Param Deduction_Param, String trxName ) throws Exception
	{
	
		if ( Deduction_Param.getMaximumAge() != null && Deduction_Param.getMaximumAge().compareTo(Env.ZERO) != 0 )
		{
//			BigDecimal age = Employee.getAgeWithMonth( Period.getPayDate() );
			BigDecimal age = new BigDecimal( Employee.getAge( Period.getPayDate() ));
			if ( age.compareTo( Deduction_Param.getMaximumAge() ) >= 0 )
	        {
	            this.salaryAdmissible = Env.ZERO;  
	            this.hoursAdmissible = Env.ZERO;
	    		this.employeeAmount = Env.ZERO;
	    		this.employerAmount = Env.ZERO;
	    		return;
	        }
		}
		//
		//
		// Recherche des montants admissible /
		// Search admisible amount
		//
//		getSalaryAdmissible( Deduction_Param );
		this.salaryAdmissible = Employee.getInsurableSalary( Period.getP_Period_ID(), Deduction_Param.getP_Deduction_ID() );  
//ici salaire admisible DMA 30000
		
		
		
		P_Employee_Profile EmployeeProfile = P_Employee_Profile.getProfileInsurance( Env.getCtx(), Employee.getP_Employee_ID(), Period.getEndDate(), trxName);
		if ( EmployeeProfile != null )
		{
			P_Insurance_Program insurance_Program = P_Insurance_Program.getByProfile(Env.getCtx(), EmployeeProfile.getP_Profile_ID() ,trxName);
			P_Insurance_Param insurance_Param = P_Insurance_Param.get (Env.getCtx(), insurance_Program.getP_Insurance_Program_ID(), Deduction_Param.getP_Deduction_ID(), Period.getPayDate(), null);
			
			if ( insurance_Param != null && this.salaryAdmissible.compareTo( insurance_Param.getMinimumSalaryInsurable() ) < 0 )
				this.salaryAdmissible = insurance_Param.getMinimumSalaryInsurable();
				
		}

		if (EmployeeDeduction.getProtectionAmount() != null && EmployeeDeduction.getProtectionAmount().compareTo(Env.ZERO) != 0 )
			this.salaryAdmissible = EmployeeDeduction.getProtectionAmount();

		// Gestion du minimum
			
		BigDecimal EmployeeRate = EmployeeDeduction.getEmployeeRate(Deduction_Param); 
		BigDecimal EmployerRate = EmployeeDeduction.getEmployerRate(Deduction_Param); 
		
		BigDecimal EmployeeAmount = EmployeeDeduction.getEmployeeAmount( Deduction_Param);
		BigDecimal EmployerAmount = EmployeeDeduction.getEmployerAmount( Deduction_Param); 

//		P_Employee_Deduction_Excep deductionExcep = P_Employee_Deduction_Excep.get( Env.getCtx (),  EmployeeDeduction.getP_Employee_Deduction_ID(), Period.getP_Period_ID(), trxName );

		
		if ( EmployeeDeduction.getMultiplyRate() != null && EmployeeDeduction.getMultiplyRate().compareTo(ZERO) != 0 )
			this.salaryAdmissible = this.salaryAdmissible.multiply( EmployeeDeduction.getMultiplyRate() );
		
		BigDecimal MaximumAllowableEarning = Deduction_Param.getMaximumAllowableEarnings();
		if ( MaximumAllowableEarning != null && MaximumAllowableEarning.compareTo(Env.ZERO) != 0 )
		{
			if(this.salaryAdmissible.compareTo(MaximumAllowableEarning) > 0 )
			{
				this.salaryAdmissible = MaximumAllowableEarning;
			}

			int age = Employee.getAge( Period.getPayDate() );

			if ( age >= 65 && this.salaryAdmissible.compareTo(MaximumAllowableEarning) >= 0)
			{
				this.salaryAdmissible = MaximumAllowableEarning.divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP);
				// DI24
				if ( Deduction.getValue().equals("DI24"))
					EmployeeAmount = EmployeeAmount.divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP);
			}

		}

		
		
		
		if ( this.salaryAdmissible != null && Deduction_Param.getMethod_Rounding().equals( new String( "T" ) ) )
		{
			this.salaryAdmissible = this.salaryAdmissible.divide( new BigDecimal( 1000 ), 0, BigDecimal.ROUND_UP );
			//Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + "- Round salaryAdmissible = " + this.salaryAdmissible);
		}
		

		if ( EmployeeAmount != null )
		{
			if ( this.employeeAmount != null )
				this.employeeAmount = this.employeeAmount.add( EmployeeAmount );
			else
				this.employeeAmount = EmployeeAmount; 
		}
		
		if ( EmployerAmount != null )
		{
			if ( this.employerAmount != null )
				this.employerAmount = this.employerAmount.add( EmployerAmount );
			else
				this.employerAmount = EmployerAmount; 
		}
		
		if ( EmployeeRate != null && EmployeeRate.compareTo( ZERO ) != 0 )
		{
			this.employeeAmount = this.salaryAdmissible.multiply( EmployeeRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) );
		}
		
		if ( EmployerRate != null && EmployerRate.compareTo( ZERO ) != 0 )
		{
			this.employerAmount =  this.salaryAdmissible.multiply( EmployerRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ) ;
		}
		// Taux mensuel
//		this.employeeAmount = this.employeeAmount.multiply( new BigDecimal(12)).divide(new BigDecimal(Frequency.getNumberOfPeriod()),2,BigDecimal.ROUND_HALF_UP );
//		this.employerAmount = this.employerAmount.multiply( new BigDecimal(12)).divide(new BigDecimal(Frequency.getNumberOfPeriod()),2,BigDecimal.ROUND_HALF_UP );

//		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
//		this.employerAmount = this.employerAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		
		boolean Quebec = false;
		boolean Ontario = false;
		boolean Manitoba = false;

		if ( RegionResidence != null )
		{
			if ( RegionResidence.getC_Region_ID() == QUEBEC )	Quebec = true;
			if ( RegionResidence.getC_Region_ID() == ONTARIO || Region.getC_Region_ID() == ONTARIO )	Ontario = true;
			if ( RegionResidence.getC_Region_ID() == MANITOBA )	Manitoba = true;
		}
		
		if ( ! Employee.isStatusIndian() )
		{
			if ( Quebec && ( Deduction_Param.getTaxeRate() != null && Deduction_Param.getTaxeRate().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}

			if ( Ontario && ( Deduction_Param.getTaxeRateOntario() != null && Deduction_Param.getTaxeRateOntario().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}
		
			if ( Manitoba && ( Deduction_Param.getTaxeRateManitoba() != null && Deduction_Param.getTaxeRateManitoba().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}

		}
		
		if ( this.salaryAdmissible != null && Deduction_Param.getMethod_Rounding().equals( new String( "T" ) ) )
		{
			this.salaryAdmissible = this.salaryAdmissible.multiply(new BigDecimal( 1000 ));
			//Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + "- Round salaryAdmissible = " + this.salaryAdmissible);
		}


		if ( this.employeeAmount.compareTo(Env.ZERO) == 0 && this.employerAmount.compareTo(Env.ZERO) == 0 )
		{
			this.salaryAdmissible = Env.ZERO;
		}
/*		
		if ( Deduction.getValue( ).equals("DI23G7"))
		{
			Integer key = new Integer ( Payment.getP_Payment_ID() );

			BigDecimal cumAmt = (BigDecimal)s_cacheEmployeeInsuranceG7.get(key);
			BigDecimal cumAmtEmr = (BigDecimal)s_cacheEmployerInsuranceG7.get(key);
			if ( cumAmt != null )
			{
//				BigDecimal maxEmr = new BigDecimal(61.15); //getInsuranceMaxEmployer();
				BigDecimal maxEmr = (cumAmt.add( cumAmtEmr )).divide(new BigDecimal( 2 ),2,BigDecimal.ROUND_HALF_UP );
				if ( cumAmtEmr.compareTo( maxEmr) > 0 )
				{
					this.employerAmount = cumAmtEmr.subtract(cumAmt);
					this.employeeAmount = this.employeeAmount.subtract(employerAmount);
				}
					
				
			}

	//		if 
		}
	*/	
		
		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		this.employerAmount = this.employerAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );

	}   // calcul_insurance

	private void calcul_insurance_M32( P_Payment Payment, P_Time_Sheet Timesheet, P_Period Period, P_Employee Employee, MRegion RegionResidence, P_Region Region, P_Method_Deduction   Method_Deduction, P_Employee_Deduction EmployeeDeduction, P_Deduction Deduction, P_Deduction_Param Deduction_Param, String trxName ) throws Exception
	{
		
		//
		//
		// Recherche des montants admissible /
		// Search admisible amount
		BigDecimal insurableSalary = Employee.getInsurableSalary( Period.getP_Period_ID(), Deduction_Param.getP_Deduction_ID() );  

//		if ( Deduction.getValue().equals("DA01") )
		if ( Deduction_Param.getMaximumAge() != null && Deduction_Param.getMaximumAge().compareTo(Env.ZERO) != 0  )
		{
//			int age = Employee.getAge( Period.getPayDate() );
//			BigDecimal age = Employee.getAgeWithMonth( Period.getPayDate() );
			BigDecimal age = new BigDecimal( Employee.getAge( Period.getPayDate() ));

			if ((age).compareTo( Deduction_Param.getMaximumAge()) >= 0  )
			{
				BigDecimal pourcent = new BigDecimal(0.5);
				insurableSalary = insurableSalary.multiply(pourcent) ;
			}
		}

/*
		if ( Deduction.getValue().equals("A06") || Deduction.getValue().equals("A08") || Deduction.getValue().equals("A09") )
		{
			int age = Employee.getAge( Period.getPayDate() );

			if (age >= 65  )
			{
				BigDecimal pourcent = new BigDecimal(0.5);
				insurableSalary = insurableSalary.multiply(pourcent) ;
			}
		}
*/
		//
		// Si l'employé a un régime B le salaire admissible est multiplier pour 2
		// 
		int RegimeB = 1000047; 
		int RegimeE = 1000045;
		int RegimeF = 1000046;

		int age = Employee.getAge( Period.getPayDate() );

		//Le calcul des assurances par born d'age doit déterminé l'age au mois suivant la date d'anniversaire a moins que l'employé soit né le 1ier du mois.
		//deduction 51 et A08

/* 2025-07-08		
		if ( TimeUtil.getDay_Of_Month( Employee.getBirthDate()) != 1 )
		{
			age = getAge( TimeUtil.addMonth( Employee.getBirthDate(), 1 ), Period.getPayDate() );  
		};
*/		

//		P_Employee_Profile EmployeeProfile = P_Employee_Profile.get( Env.getCtx(), Employee.getP_Employee_ID(), RegimeB, Period.getStartDate(), trxName);

		P_Employee_Profile EmployeeProfile = P_Employee_Profile.getProfileInsurance( Env.getCtx(), Employee.getP_Employee_ID(), Period.getEndDate(), trxName);
		if ( EmployeeProfile != null )
		{
//			insurableSalary = insurableSalary.multiply( new BigDecimal( 2 ));
			P_Insurance_Program Insurance_Program = P_Insurance_Program.getByProfile( Env.getCtx(), EmployeeProfile.getP_Profile_ID(), trxName); 
			insurableSalary = insurableSalary.multiply( Insurance_Program.getMultiplyRate() );

			if ( EmployeeProfile.getP_Profile_ID() == RegimeE || EmployeeProfile.getP_Profile_ID() == RegimeF)
			{
				insurableSalary =  new BigDecimal( 20000 );
			}

			// 2009.02.18 -- employé de plus de 70 ans ne paie pas 
//			if (age >= 70)
			if ((new BigDecimal(age)).compareTo( Deduction_Param.getMaximumAge()) >= 0  )
				
			{
				insurableSalary = Env.ZERO;
			}

		}
		
		this.hoursAdmissible  = ZERO;
		this.salaryAdmissible = insurableSalary;

		// D et MA
		if ( EmployeeDeduction.getProtectionAmount() != null && EmployeeDeduction.getProtectionAmount().compareTo(ZERO) != 0)
		{
			this.salaryAdmissible = EmployeeDeduction.getProtectionAmount();
			insurableSalary = EmployeeDeduction.getProtectionAmount();
			
//			if (age >= 65  )
			if ((new BigDecimal(age)).compareTo( Deduction_Param.getMaximumAge()) >= 0  )
			{
				BigDecimal pourcent = new BigDecimal(0.5);
				insurableSalary = insurableSalary.multiply(pourcent) ;
			}

			
//2012.05.16			
			if (age >= 70)
			{
				insurableSalary = Env.ZERO;
			}
		}


		// Gestion du minimum
			
		BigDecimal EmployeeRate = EmployeeDeduction.getEmployeeRate(Deduction_Param); 
		BigDecimal EmployerRate = EmployeeDeduction.getEmployerRate(Deduction_Param); 
		
		BigDecimal EmployeeAmount = EmployeeDeduction.getEmployeeAmount( Deduction_Param);
		BigDecimal EmployerAmount = EmployeeDeduction.getEmployerAmount( Deduction_Param); 

		if ( EmployeeAmount != null )
		{
			if ( this.employeeAmount != null )
				this.employeeAmount = this.employeeAmount.add( EmployeeAmount );
			else
				this.employeeAmount = EmployeeAmount; 
		}
		
		if ( EmployerAmount != null )
		{
			if ( this.employerAmount != null )
				this.employerAmount = this.employerAmount.add( EmployerAmount );
			else
				this.employerAmount = EmployerAmount; 
		}
		

		if ( insurableSalary != null && Deduction_Param.getMethod_Rounding().equals( new String( "T" ) ) )
		{
			insurableSalary = insurableSalary.divide( new BigDecimal( 1000 ), 0, BigDecimal.ROUND_UP );
		}

		if ( EmployeeRate != null )
			this.employeeAmount =  insurableSalary.multiply( EmployeeRate );

		if ( EmployerRate != null )
			this.employerAmount =  insurableSalary.multiply( EmployerRate );

		this.employeeAmount = this.employeeAmount.divide( new BigDecimal( 2 ), 2, BigDecimal.ROUND_HALF_UP) ;
		this.employerAmount = this.employerAmount.divide( new BigDecimal( 2 ), 2, BigDecimal.ROUND_HALF_UP) ;
				
		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		this.employerAmount = this.employerAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		
		boolean Quebec = false;
		boolean Ontario = false;
		boolean Manitoba = false;

		if ( RegionResidence != null )
		{
			if ( RegionResidence.getC_Region_ID() == QUEBEC )	Quebec = true;
			if ( RegionResidence.getC_Region_ID() == ONTARIO || Region.getC_Region_ID() == ONTARIO )	Ontario = true;
			if ( RegionResidence.getC_Region_ID() == MANITOBA )	Manitoba = true;
		}

		if ( ! Employee.isStatusIndian() )
		{
			if ( Quebec && ( Deduction_Param.getTaxeRate() != null && Deduction_Param.getTaxeRate().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}

			if ( Ontario && ( Deduction_Param.getTaxeRateOntario() != null && Deduction_Param.getTaxeRateOntario().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}

			if ( Manitoba && ( Deduction_Param.getTaxeRateManitoba() != null && Deduction_Param.getTaxeRateManitoba().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}
		}


		if ( this.employeeAmount.compareTo(Env.ZERO) == 0 && this.employerAmount.compareTo(Env.ZERO) == 0 )
		{
			this.salaryAdmissible = Env.ZERO;
		}
		
		this.employeeAmount = this.employeeAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
		this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
		
	}   // calcul_insurance


	private void calcul_insurance_M35( P_Payment Payment, P_Time_Sheet Timesheet, P_Period Period, P_Employee Employee, MRegion RegionResidence, P_Region Region, P_Method_Deduction   Method_Deduction, P_Employee_Deduction EmployeeDeduction, P_Deduction Deduction, P_Deduction_Param Deduction_Param, String trxName ) throws Exception
	{
		
		P_Frequency Frequency = P_Frequency.get( Env.getCtx(), Period.getP_Frequency_ID(), trxName);
		
		// Gestion du minimum
		
		BigDecimal EmployeeRate = EmployeeDeduction.getEmployeeRate(Deduction_Param); 
		BigDecimal EmployerRate = EmployeeDeduction.getEmployerRate(Deduction_Param); 
		
		BigDecimal EmployeeAmount = EmployeeDeduction.getEmployeeAmount( Deduction_Param);
		BigDecimal EmployerAmount = EmployeeDeduction.getEmployerAmount( Deduction_Param); 

//		P_Employee_Deduction_Excep deductionExcep = P_Employee_Deduction_Excep.get( Env.getCtx (),  EmployeeDeduction.getP_Employee_Deduction_ID(), Period.getP_Period_ID(), trxName );

		
		if ( this.salaryAdmissible != null && Deduction_Param.getMethod_Rounding().equals( new String( "T" ) ) )
		{
			this.salaryAdmissible = this.salaryAdmissible.divide( new BigDecimal( 1000 ), 0, BigDecimal.ROUND_UP ).multiply(new BigDecimal( 1000 ));
			//Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + "- Round salaryAdmissible = " + this.salaryAdmissible);
		}
		

		if ( EmployeeAmount != null )
		{
			if ( this.employeeAmount != null )
				this.employeeAmount = this.employeeAmount.add( EmployeeAmount );
			else
				this.employeeAmount = EmployeeAmount; 
		}
		
		if ( EmployerAmount != null )
		{
			if ( this.employerAmount != null )
				this.employerAmount = this.employerAmount.add( EmployerAmount );
			else
				this.employerAmount = EmployerAmount; 
		}
		
		if ( EmployeeRate != null && EmployeeRate.compareTo( ZERO ) != 0 )
		{
			this.employeeAmount = this.salaryAdmissible.multiply( EmployeeRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) );
		}
		
		if ( EmployerRate != null && EmployerRate.compareTo( ZERO ) != 0 )
		{
			this.employerAmount =  this.salaryAdmissible.multiply( EmployerRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ) ;
		}

//		this.employeeAmount = this.employeeAmount.divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP);
//		this.employerAmount = this.employerAmount.divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP);


/*		
		int Month = TimeUtil.getMonth( Period.getStartDate() );

		String query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull( sum( Employer_Part ),0) Employer_Part "
			+ " from P_Payment_Deduction, P_Period"
			+ " where P_Payment_Deduction.P_Deduction_ID = " + EmployeeDeduction.getP_Deduction_ID ()
			+ " and P_Payment_Deduction.P_Period_ID = P_Period.P_Period_ID "  
			+ " and P_Payment_Deduction.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
			+ " and datepart( m, P_Period.StartDate ) = " + Month 


			;

		BigDecimal employeePartCumul =     ZERO;
		BigDecimal employerPartCumul =     ZERO;

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			employeePartCumul = rs.getBigDecimal("Employee_Part");
			employerPartCumul = rs.getBigDecimal("Employer_Part");
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
		
		if ( employeePartCumul == null ) employeePartCumul = ZERO;
		if ( employerPartCumul == null ) employerPartCumul = ZERO;
*/
		boolean Quebec = false;
		boolean Ontario = false;
		boolean Manitoba = false;

		if ( RegionResidence != null )
		{
			if ( RegionResidence.getC_Region_ID() == QUEBEC )	Quebec = true;
			if ( RegionResidence.getC_Region_ID() == ONTARIO || Region.getC_Region_ID() == ONTARIO )	Ontario = true;
			if ( RegionResidence.getC_Region_ID() == MANITOBA )	Manitoba = true;
		}

		if ( ! Employee.isStatusIndian() )
		{
			if ( Quebec && ( Deduction_Param.getTaxeRate() != null && Deduction_Param.getTaxeRate().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(8,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(8,BigDecimal.ROUND_HALF_UP));
			}

			if ( Ontario && ( Deduction_Param.getTaxeRateOntario() != null && Deduction_Param.getTaxeRateOntario().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(8,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(8,BigDecimal.ROUND_HALF_UP));
			}

			if ( Manitoba && ( Deduction_Param.getTaxeRateManitoba() != null && Deduction_Param.getTaxeRateManitoba().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(8,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(8,BigDecimal.ROUND_HALF_UP));
			}
		}

/*	2022-05-25
 * 	if (employeePartCumul.compareTo(ZERO) == 0)
		{
			this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
			this.employerAmount = this.employerAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
			this.employeeAccumulationAmount = this.employeeAmount;
			this.employerAccumulationAmount = this.employerAmount;
		}
		else 
		{
			this.employeeAmount = ZERO;
			this.employerAmount = ZERO;
		}
		*/

		if ( this.employeeAmount.compareTo(Env.ZERO) == 0 && this.employerAmount.compareTo(Env.ZERO) == 0 )
		{
			this.salaryAdmissible = Env.ZERO;
		}

		this.employeeAmount = this.employeeAmount.multiply(new BigDecimal(12)).divide(new BigDecimal(Frequency.getNumberOfPeriod()), 8, BigDecimal.ROUND_HALF_UP);
		this.employerAmount = this.employerAmount.multiply(new BigDecimal(12)).divide(new BigDecimal(Frequency.getNumberOfPeriod()), 8, BigDecimal.ROUND_HALF_UP);

		this.employeeAmount = this.employeeAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
		this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP);


	}   // calcul_insurance

	//
	// Calcul les assurance invalidité longue durée.
	//
	private void calcul_insurance_LTD( P_Payment Payment, P_Time_Sheet Timesheet, P_Period Period, P_Employee Employee, MRegion RegionResidence, P_Region Region, P_Method_Deduction   Method_Deduction, P_Employee_Deduction EmployeeDeduction, P_Deduction Deduction, P_Deduction_Param Deduction_Param, String trxName ) throws Exception
	{
		
		P_Frequency Frequency = P_Frequency.get( Env.getCtx(), Period.getP_Frequency_ID(), trxName);

// 
//l'age en date du 1er mai a valider		
		// l'employé ne paie plus quand il atteint 64 ans 

		BigDecimal age = Employee.getAgeWithMonth( Period.getPayDate() );
		//BigDecimal age = new BigDecimal( Employee.getAge( Period.getPayDate() ));
		

  	    P_Employee_Profile EmployeeProfile = P_Employee_Profile.getProfileInsurance( Env.getCtx(), Employee.getP_Employee_ID(), Period.getEndDate(), trxName);
		if ( EmployeeProfile == null )
		{
			P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "L'employé "+Employee.getValue()+" n'a pas de profil d'assurance actif", trxName);
			return;
		}
		
  	    P_Insurance_Program Insurance_Program = P_Insurance_Program.getByProfile( Env.getCtx(), EmployeeProfile.getP_Profile_ID(), trxName); 
        
//		if ( age.compareTo( new BigDecimal( 64.06 )) >= 0 )
		if ( age.compareTo( Insurance_Program.getAgeLimitMax() ) >= 0 )
        {
            this.salaryAdmissible = Env.ZERO;  
            this.hoursAdmissible = Env.ZERO;
    		this.employeeAmount = Env.ZERO;
    		this.employerAmount = Env.ZERO;
    		return;
        }

		//Pour obtenir l'âge sous un format décimal (comme 64,74), on divise le nombre de jours total vécus par la durée moyenne d'une année.
		// Nombre total de jours
		int nbrDay = TimeUtil.getDaysBetween(Employee.getBirthDate(), Period.getPayDate());
		age = new BigDecimal( nbrDay).divide( new BigDecimal(365.2425 ),4, BigDecimal.ROUND_HALF_UP );
// .06 c'est la même chose que .67 , 64 et 6 mois.		
		if ( age.compareTo( new BigDecimal( 64.67 )) >= 0 )
        {
            this.salaryAdmissible = Env.ZERO;  
            this.hoursAdmissible = Env.ZERO;
    		this.employeeAmount = Env.ZERO;
    		this.employerAmount = Env.ZERO;

    		P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "Assurance invalidité longue durée - employé qui à plus de 64.67 ans", trxName);
			log.info( "S.v.p Vérifier le profiles d'assurances " + Employee.getValue() + " " + Employee.getName() ) ;	

    		return;
        }
		
		
/*		if ( Deduction_Param.getMaximumAge() != null && Deduction_Param.getMaximumAge().compareTo(Env.ZERO) != 0 && age.compareTo( Deduction_Param.getMaximumAge() ) >= 0 )
        {
            this.salaryAdmissible = Env.ZERO;  
            this.hoursAdmissible = Env.ZERO;
    		this.employeeAmount = Env.ZERO;
    		this.employerAmount = Env.ZERO;
    		return;
        }
*/	
		
		P_Insurance_LTD Insurance_LTD = null ;
		//
		//
		// Recherche des montants admissible /
		// Search admisible amount
		BigDecimal insurableSalary = Employee.getInsurableSalary( Period.getP_Period_ID(), Deduction_Param.getP_Deduction_ID() );  

		BigDecimal EmployeeSalary_Eligible = EmployeeDeduction.getSalary_Eligible( );
		if ( EmployeeSalary_Eligible != null && EmployeeSalary_Eligible.compareTo( Env.ZERO) != 0)
		{
			insurableSalary = EmployeeSalary_Eligible;
		}
		
		if ( insurableSalary == null)
		{
	        P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Salaire assurable a null ", trxName);
			Timesheet.setTimeSheetStatus( P_Time_Sheet.TIMESHEETSTATUS_Error );
			Timesheet.save( );
			
			insurableSalary = Env.ZERO;
		}


		// 2022-09-14
		insurableSalary = insurableSalary.setScale(0, BigDecimal.ROUND_HALF_UP );

		this.hoursAdmissible  = ZERO;
		this.salaryAdmissible = insurableSalary;
		
		insurableSalary = insurableSalary.divide(new BigDecimal(12),8,BigDecimal.ROUND_HALF_UP );

		//
		// Si l'employé a un régime C n'ont pas le même maximum.
		// 
//		int RegimeC = 1000044;
//		P_Employee_Profile EmployeeProfile = P_Employee_Profile.get( Env.getCtx(), Employee.getP_Employee_ID(), RegimeC, Period.getEndDate(), trxName);
		
//		P_Employee_Profile EmployeeProfile = P_Employee_Profile.getProfileInsurance(Env.getCtx(), Employee.getP_Employee_ID(),Period.getEndDate(), trxName);
		
		Insurance_LTD = P_Insurance_LTD.getByProfile(Env.getCtx(), EmployeeProfile.getP_Profile_ID(), Period.getEndDate() , trxName);
		// valide Maximum assurable par mois  
		if ( insurableSalary.compareTo( Insurance_LTD.getMaxInsurableSalary() ) > 0 )
			insurableSalary = Insurance_LTD.getMaxInsurableSalary();
		

		BigDecimal layer1 = Insurance_LTD.getLayer01() ;
		BigDecimal rate1  = Insurance_LTD.getRate01().divide(new BigDecimal(100));
		
		BigDecimal layer2 =  Insurance_LTD.getLayer02() ;
		BigDecimal rate2  =  Insurance_LTD.getRate02().divide(new BigDecimal(100));
		BigDecimal rate3  =  Insurance_LTD.getRate03().divide(new BigDecimal(100));

/*		
		BigDecimal layer1 =  new BigDecimal(2000);
		BigDecimal rate1  =  new BigDecimal(0.6);
		
		BigDecimal layer2 =  new BigDecimal(3000);
		BigDecimal rate2  =  new BigDecimal(0.5);
		BigDecimal rate3  =  new BigDecimal(0.45);
*/		
		this.employerAmount = ZERO;
		
		if ( insurableSalary.compareTo( layer1 ) > 0 )
		{
			this.employeeAmount = layer1.multiply( rate1 );
			insurableSalary = insurableSalary.subtract( layer1 );

			if ( insurableSalary.compareTo( layer2 ) > 0 )
			{
				this.employeeAmount = this.employeeAmount.add( layer2.multiply( rate2 ) );
				insurableSalary = insurableSalary.subtract( layer2 );
				this.employeeAmount = this.employeeAmount.add( insurableSalary.multiply( rate3 ).setScale(2,BigDecimal.ROUND_HALF_UP) );
			}
			else
			{
				this.employeeAmount = this.employeeAmount.add( insurableSalary.multiply( rate2 ) );
			}
		}
		else
		{
			this.employeeAmount = insurableSalary.multiply( rate1 ); 
		}

		//2022-09-14
		this.employeeAmount = this.employeeAmount .setScale(0,BigDecimal.ROUND_HALF_UP );
		this.salaryAdmissible = this.employeeAmount;

		this.employeeAmount = this.employeeAmount.multiply( rate3 );

		

//		this.employeeAmount = this.employeeAmount.multiply( Deduction_Param.getEmployeeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ));

//		ici *12 / 52

//		this.employeeAmount = this.employeeAmount.divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP);
		this.employeeAmount = this.employeeAmount.multiply(new BigDecimal(12)).divide(new BigDecimal(Frequency.getNumberOfPeriod()), 2, BigDecimal.ROUND_HALF_UP);
		

		//2022-06-23
		if ( Deduction_Param.getEmployerRate() != null )
		{
			if ( Deduction_Param.getEmployerRate().compareTo(Env.ONE) == 0 )
			{
/*				//2022-09-21
				if ( EmployeeProfile != null && (EmployeeProfile.getP_Profile_ID() == Group8 ))
				{
					this.employerAmount = this.employeeAmount;
					this.employeeAmount = this.employeeAmount.subtract(this.employerAmount);
					BigDecimal amtEr = valideMaxEmployer( this.employerAmount, this.employeeAmount );	
					if ( amtEr != null )
					{
						this.employerAmount = amtEr;
					}
					
				}
				else
				{
*/				
					this.employerAmount = this.employeeAmount;
					this.employeeAmount = this.employeeAmount.subtract(this.employerAmount);
//				}
				
			}
			else
			{
				this.employerAmount = this.employeeAmount.multiply(Deduction_Param.getEmployerRate());
				this.employeeAmount = this.employeeAmount.subtract(this.employerAmount);
			}

		}
			

		this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP);

		
		boolean Quebec = false;
		boolean Ontario = false;
		boolean Manitoba = false;

		if ( RegionResidence != null )
		{
			if ( RegionResidence.getC_Region_ID() == QUEBEC )	Quebec = true;
			if ( RegionResidence.getC_Region_ID() == ONTARIO || Region.getC_Region_ID() == ONTARIO )	Ontario = true;
			if ( RegionResidence.getC_Region_ID() == MANITOBA )	Manitoba = true;
		}

		if ( ! Employee.isStatusIndian() )
		{
			if ( Quebec && ( Deduction_Param.getTaxeRate() != null && Deduction_Param.getTaxeRate().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}

			if ( Ontario && ( Deduction_Param.getTaxeRateOntario() != null && Deduction_Param.getTaxeRateOntario().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}

			if ( Manitoba && ( Deduction_Param.getTaxeRateManitoba() != null && Deduction_Param.getTaxeRateManitoba().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}
		}


		//2023-05-10 modifier ROUND_HALF_UP pour ROUND_UP
		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_UP );
		this.employerAmount = this.employerAmount.setScale( 2 , BigDecimal.ROUND_UP );

		if ( this.employeeAmount.compareTo(Env.ZERO) == 0 && this.employerAmount.compareTo(Env.ZERO) == 0 )
		{
			this.salaryAdmissible = Env.ZERO;
		}

	}
	
	//Invalité courte durée
	private void calcul_insurance_M80( P_Payment Payment, P_Time_Sheet Timesheet, P_Period Period, P_Employee Employee, MRegion RegionResidence, P_Region Region, P_Method_Deduction   Method_Deduction, P_Employee_Deduction EmployeeDeduction, P_Deduction Deduction, P_Deduction_Param Deduction_Param, String trxName  ) throws Exception
	{
		
		P_Frequency Frequency = P_Frequency.get( Env.getCtx(), Period.getP_Frequency_ID(), trxName);

		// Recherche des montants admissible /
		// Search admisible amount
//		BigDecimal insurableSalary = Employee.getInsurableSalary( Period.getP_Period_ID() ).divide(new BigDecimal(Frequency.getNumberOfPeriod()),2,BigDecimal.ROUND_HALF_UP );  
		BigDecimal insurableSalary = Employee.getInsurableSalary( Period.getP_Period_ID(), Deduction_Param.getP_Deduction_ID() ).divide(new BigDecimal(Frequency.getNumberOfPeriod()),2,BigDecimal.ROUND_HALF_UP );
		
		P_Employee_Profile EmployeeProfile = P_Employee_Profile.getProfileInsurance( Env.getCtx(), Employee.getP_Employee_ID(), Period.getEndDate(), trxName);

		if ( EmployeeProfile == null )
			P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "L'employé "+Employee.getValue()+" n'a pas de profil d'assurance " + Deduction.getName() , trxName);

		if ( EmployeeProfile.getP_Profile_ID() == Group9 )  // Groupe 9
			insurableSalary = Employee.getInsurableSalary( Period.getP_Period_ID(), Deduction_Param.getP_Deduction_ID() ).divide(new BigDecimal( 52 ),2,BigDecimal.ROUND_HALF_UP );
		
		if ( EmployeeProfile != null )
		{
//			insurableSalary = insurableSalary.multiply( new BigDecimal( 2 ));
//			P_Insurance_Program Insurance_Program = P_Insurance_Program.getByProfile( Env.getCtx(), EmployeeProfile.getP_Profile_ID(), trxName);
			
			P_Insurance_STD Insurance_STD = P_Insurance_STD.getByProfile(Env.getCtx(), EmployeeProfile.getP_Profile_ID(), Period.getP_Period_ID(), trxName);
			
			if ( Insurance_STD == null)
				return;
			
			insurableSalary = insurableSalary.multiply( Insurance_STD.getSalaryRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP ) );

			insurableSalary = insurableSalary.setScale(0,BigDecimal.ROUND_UP);
			
			if ( insurableSalary.compareTo( Insurance_STD.getMaxInsurableSalary() ) > 0 )
				insurableSalary = Insurance_STD.getMaxInsurableSalary();

			this.hoursAdmissible  = ZERO;
			this.salaryAdmissible = insurableSalary;

			this.employeeAmount =  insurableSalary.multiply( Insurance_STD.getRate01().divide(new BigDecimal(10),8,BigDecimal.ROUND_HALF_UP ) );
			this.employeeAmount = this.employeeAmount.multiply( new BigDecimal(12)).divide(new BigDecimal(Frequency.getNumberOfPeriod()),8,BigDecimal.ROUND_HALF_UP );
		}


		if ( insurableSalary != null && Deduction_Param.getMethod_Rounding().equals( new String( "T" ) ) )
		{
			insurableSalary = insurableSalary.divide( new BigDecimal( 1000 ), 0, BigDecimal.ROUND_UP );
		}

		
		boolean Quebec = false;
		boolean Ontario = false;
		boolean Manitoba = false;

		if ( RegionResidence != null )
		{
			if ( RegionResidence.getC_Region_ID() == QUEBEC )	Quebec = true;
			if ( RegionResidence.getC_Region_ID() == ONTARIO || Region.getC_Region_ID() == ONTARIO )	Ontario = true;
			if ( RegionResidence.getC_Region_ID() == MANITOBA )	Manitoba = true;
		}
//2023-05-16
//		this.employeeAmount = this.employeeAmount.setScale(2,BigDecimal.ROUND_HALF_UP);

/*		if ( Deduction_Param.getEmployerRate() != null )
			this.employerAmount = this.employeeAmount.multiply(Deduction_Param.getEmployerRate());
*/
		//2022-06-23
		if ( Deduction_Param.getEmployerRate() != null )
		{
			if ( Deduction_Param.getEmployerRate().compareTo(Env.ONE) == 0 )
			{
				this.employerAmount = this.employeeAmount;
				this.employeeAmount = this.employeeAmount.subtract(this.employerAmount);
				
			}
			else
			{
				this.employerAmount = this.employeeAmount.multiply(Deduction_Param.getEmployerRate());
				this.employeeAmount = this.employeeAmount.subtract(this.employerAmount);
				
			}

				
		}
	
		//2023-05-16		
//		this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
		
		if ( ! Employee.isStatusIndian() )
		{
			if ( Quebec && ( Deduction_Param.getTaxeRate() != null && Deduction_Param.getTaxeRate().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}

			if ( Ontario && ( Deduction_Param.getTaxeRateOntario() != null && Deduction_Param.getTaxeRateOntario().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}

			if ( Manitoba && ( Deduction_Param.getTaxeRateManitoba() != null && Deduction_Param.getTaxeRateManitoba().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}
		}
		
		//2023-05-10 modifier ROUND_UP

		this.employeeAmount = this.employeeAmount.setScale(2,BigDecimal.ROUND_UP);
		this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_UP);
	
			
		
	}

	//Assurance Maladie
	private void calcul_insurance_M81( P_Payment Payment, P_Time_Sheet Timesheet, P_Period Period, P_Employee Employee, MRegion RegionResidence, P_Region Region, P_Method_Deduction   Method_Deduction, P_Employee_Deduction EmployeeDeduction, P_Deduction Deduction, P_Deduction_Param Deduction_Param, Boolean taxable_Benefit, String trxName ) throws Exception
	{
	
		P_Frequency Frequency = P_Frequency.get( Env.getCtx(), Period.getP_Frequency_ID(), trxName);
		BigDecimal age = new BigDecimal( Employee.getAge( Period.getPayDate() ));

		if ( Deduction_Param.getMaximumAge() != null && Deduction_Param.getMaximumAge().compareTo(Env.ZERO) != 0 )
		{
//			BigDecimal age = Employee.getAgeWithMonth( Period.getPayDate() );
			
			if ( age.compareTo( Deduction_Param.getMaximumAge() ) >= 0 )
	        {
	            this.salaryAdmissible = Env.ZERO;  
	            this.hoursAdmissible = Env.ZERO;
	    		this.employeeAmount = Env.ZERO;
	    		this.employerAmount = Env.ZERO;
	    		return;
	        }
		}

		if ( Deduction.getValue().equals("DI21G8") && age.compareTo( new BigDecimal(65) ) >= 0 )
        {
            this.salaryAdmissible = Env.ZERO;  
            this.hoursAdmissible = Env.ZERO;
    		this.employeeAmount = Env.ZERO;
    		this.employerAmount = Env.ZERO;
    		return;
        }
		
		
		// Recherche des montants admissible /
		// Search admisible amount
		BigDecimal insurableSalary = Employee.getInsurableSalary( Period.getP_Period_ID(), Deduction_Param.getP_Deduction_ID() ).divide(new BigDecimal(Frequency.getNumberOfPeriod()),2,BigDecimal.ROUND_HALF_UP );  
		P_Employee_Profile EmployeeProfile = P_Employee_Profile.getProfileInsurance( Env.getCtx(), Employee.getP_Employee_ID(), Period.getEndDate(), trxName);

		if ( EmployeeProfile == null )
			P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "L'employé "+Employee.getValue()+" n'a pas de profil d'assurance " + Deduction.getName() , trxName);
		
		if ( EmployeeProfile != null )
		{
//			insurableSalary = insurableSalary.multiply( new BigDecimal( 2 ));
//			P_Insurance_Program Insurance_Program = P_Insurance_Program.getByProfile( Env.getCtx(), EmployeeProfile.getP_Profile_ID(), trxName);
			
			P_Insurance_Health Insurance_Health = P_Insurance_Health.getByProfile(Env.getCtx(), EmployeeProfile.getP_Profile_ID(), Period.getP_Period_ID(), trxName);
			
			if ( Insurance_Health == null)
				return;
						
//			insurableSalary = insurableSalary.multiply( Insurance_Health.getSalaryRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP ) );

//			if ( insurableSalary.compareTo( Insurance_Health.getMaxInsurableSalary() ) > 0 )
//				insurableSalary = Insurance_Health.getMaxInsurableSalary();

			this.hoursAdmissible  = ZERO;
			this.salaryAdmissible = insurableSalary;

			if ( EmployeeDeduction.getTypeRate() == null )
			{
				EmployeeDeduction.setTypeRate( P_Employee_Deduction.TYPERATE_Individual);
				P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "L'employé "+Employee.getValue()+" n'a pas de type de taux pour l'assurance " + Deduction.getValue() + " " + Deduction.getDescription() , trxName);

			}

			
			if ( EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_Individual))
			{
				this.employeeAmount =   Insurance_Health.getIndividualRate();
				
			}
			if ( EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_SingleParent))
			{
				this.employeeAmount =   Insurance_Health.getSingleParentRate();
				
			}
			if ( EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_Family))
			{
				this.employeeAmount =   Insurance_Health.getFamilyRate();
				
			}
			if ( EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_Couple))
			{
				this.employeeAmount =   Insurance_Health.getCoupleRate();
				
			}

				
			this.employeeAmount = this.employeeAmount.multiply( new BigDecimal(12)).divide(new BigDecimal(Frequency.getNumberOfPeriod()),2,BigDecimal.ROUND_HALF_UP );
		}


		if ( insurableSalary != null && Deduction_Param.getMethod_Rounding().equals( new String( "T" ) ) )
		{
			insurableSalary = insurableSalary.divide( new BigDecimal( 1000 ), 0, BigDecimal.ROUND_UP );
		}

		
		this.employeeAmount = this.employeeAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
			
		if ( Deduction_Param.getEmployerRate() != null )
		{
			this.employerAmount = this.employeeAmount.multiply(Deduction_Param.getEmployerRate());
		}

		//2026-09-22
		if ( Deduction.getValue().equals("DI21") && age.compareTo( new BigDecimal(65) ) >= 0 )
        {
			this.employerAmount = ZERO;
        }



		this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
		
		boolean Quebec = false;
		boolean Ontario = false;
		boolean Manitoba = false;

		if ( RegionResidence != null )
		{
			if ( RegionResidence.getC_Region_ID() == QUEBEC )	Quebec = true;
			if ( RegionResidence.getC_Region_ID() == ONTARIO || Region.getC_Region_ID() == ONTARIO )	Ontario = true;
			if ( RegionResidence.getC_Region_ID() == MANITOBA )	Manitoba = true;
		}

		this.employeeAmount = this.employeeAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
		this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
		
		if ( ! Employee.isStatusIndian() )
		{
			if ( Quebec && ( Deduction_Param.getTaxeRate() != null && Deduction_Param.getTaxeRate().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}

			if ( Ontario && ( Deduction_Param.getTaxeRateOntario() != null && Deduction_Param.getTaxeRateOntario().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}

			if ( Manitoba && ( Deduction_Param.getTaxeRateManitoba() != null && Deduction_Param.getTaxeRateManitoba().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}
		}	
		
		
		if ( Deduction.getValue().equals("DI21G7") )
		{
			this.employerAmount = this.employeeAmount.divide( new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP );
			this.employeeAmount = this.employeeAmount.divide( new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP );
		}
		else
		{
			// Groupe 8 et 9
			if ( EmployeeProfile != null && (EmployeeProfile.getP_Profile_ID() == Group8 || EmployeeProfile.getP_Profile_ID() == Group9 ))
			{
				
			} else
			{
				if ( Deduction_Param.getEmployerRate() != null )
				{
					this.employerAmount = this.employeeAmount.multiply(Deduction_Param.getEmployerRate());
					this.employeeAmount = this.employeeAmount.subtract(this.employerAmount);
				}
				
			}
				
		}

/*
		this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
		
		if ( ! this.Employee.isStatusIndian() )
		{
			if ( Quebec && ( Deduction_Param.getTaxeRate() != null && Deduction_Param.getTaxeRate().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}

			if ( Ontario && ( Deduction_Param.getTaxeRateOntario() != null && Deduction_Param.getTaxeRateOntario().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}

			if ( Manitoba && ( Deduction_Param.getTaxeRateManitoba() != null && Deduction_Param.getTaxeRateManitoba().compareTo( ZERO ) != 0 ))
			{
				this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
			}
		}	
*/		
		if ( Deduction.getValue().equals("DI21G7") )
		{
			Integer key = new Integer ( Payment.getP_Payment_ID() );
			
			BigDecimal Bamt = (BigDecimal)s_cacheEmployerInsurance.get(key);
			if ( Bamt != null )
			{
				this.employeeAmount =  ((this.employeeAmount.add(this.employerAmount)).subtract( Bamt )).divide( new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP );
				if ( this.employeeAmount.compareTo(Env.ZERO) < 0 && taxable_Benefit == false )
				{
					this.employerAmount = this.employeeAmount.add( Bamt );

					this.employerAmount = this.employerAmount.add(this.employeeAmount);
					this.employeeAmount = Env.ZERO;
				}
				else
				{
				
					this.employerAmount = this.employeeAmount.add( Bamt );
				}
				
			}
//			this.employerAmount = this.employerAmount.add( this.employeeAmount );
		}

		this.employeeAmount = this.employeeAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
		this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
		
	}


	/**	Cache						*/
	private Hashtable<Integer,BigDecimal>	s_cacheMaxEmployer = new Hashtable<Integer,BigDecimal>();
	private Hashtable<Integer,P_Employee_Deduction>	s_cacheLifeInsurance = new Hashtable<Integer,P_Employee_Deduction>();
	
	private Hashtable<Integer,BigDecimal>	s_cacheEmployerInsurance = new Hashtable<Integer,BigDecimal>();
	private Hashtable<Integer,BigDecimal>	s_cacheEmployeeInsurance = new Hashtable<Integer,BigDecimal>();

	
	private Hashtable<Integer,BigDecimal>	s_cacheEmployerInsuranceG7 = new Hashtable<Integer,BigDecimal>();
	private Hashtable<Integer,BigDecimal>	s_cacheEmployeeInsuranceG7 = new Hashtable<Integer,BigDecimal>();

	//
	// Différence avec le Groupe 9 un calcul tout les primes durant le calcul des AVI pour avoir le total et géré le maximum.
	//

	private Hashtable<Integer,BigDecimal>	s_cacheEmployerInsuranceG9r = new Hashtable<Integer,BigDecimal>();
	private Hashtable<Integer,BigDecimal>	s_cacheEmployerInsuranceG9 = new Hashtable<Integer,BigDecimal>();
	private Hashtable<Integer,BigDecimal>	s_cacheEmployeeInsuranceG9 = new Hashtable<Integer,BigDecimal>();

	private BigDecimal getInsuranceMaxEmployer( P_Time_Sheet Timesheet, P_Period Period, P_Employee Employee, P_Payment Payment, Hashtable<Integer,BigDecimal> s_cacheMaxEmployer, String trxName)
	{
		P_Employee_Profile EmployeeProfile = P_Employee_Profile.getProfileInsurance( Env.getCtx(), Employee.getP_Employee_ID(), Period.getEndDate(), trxName);
		P_Insurance_Program insurance_Program = null;
		if ( EmployeeProfile != null )
		{
			insurance_Program = P_Insurance_Program.getByProfile(Env.getCtx(), EmployeeProfile.getP_Profile_ID() ,trxName);
		}
		else
		{
			return null;
		}

		BigDecimal maxEmr = null;


		Integer key = new Integer ( Payment.getP_Payment_ID() );
		

		BigDecimal Bamt = (BigDecimal)s_cacheMaxEmployer.get(key);
		if (Bamt != null)
		{
			maxEmr = Bamt;
		}

	
		if (Bamt == null)
		{
			
			String sql = " SELECT TOP 1 P_Employee_Deduction_ID "
					+ " FROM P_Employee_Deduction "
					+ " WHERE P_Employee_Deduction.P_Deduction_ID IN ( "
					+ " select P_Deduction.P_Deduction_ID from P_Deduction "
					+ " inner join P_DEDUCTION_PARAM on P_DEDUCTION_PARAM.p_deduction_id = p_deduction.p_deduction_id"
					+ " where P_METHOD_DEDUCTION_ID IN ( 171, 173, 174)"
					+ " and ( P_DEDUCTION_PARAM.EFFECTTO is null or P_DEDUCTION_PARAM.EFFECTTO >= " + DB.TO_DATE( Period.getStartDate() ) + " )"
//					+ " and ( P_DEDUCTION_PARAM.EFFECTIN is null or P_DEDUCTION_PARAM.EFFECTIN <= " + DB.TO_DATE( Period.getStartDate() ) + " )"
					+ " and ( P_DEDUCTION_PARAM.EFFECTIN is null or P_DEDUCTION_PARAM.EFFECTIN <= " + DB.TO_DATE( Period.getEndDate() ) + " )"
					+ " AND P_DEDUCTION.VALUE NOT IN ('DI11','DI12','DI20' ) " 
					+ " ) "
					+ " And P_EMPLOYEE_DEDUCTION.P_Employee_ID = " + Employee.getP_Employee_ID()
					+ " AND P_EMPLOYEE_DEDUCTION.ISACTIVE = 'Y'"
					+ " and ( P_EMPLOYEE_DEDUCTION.EFFECTTO is null OR P_EMPLOYEE_DEDUCTION.EFFECTTO >= " + DB.TO_DATE( Period.getStartDate() ) + " )"
//					+ " and ( P_EMPLOYEE_DEDUCTION.EFFECTIN is null OR P_EMPLOYEE_DEDUCTION.EFFECTIN <= " + DB.TO_DATE( Period.getStartDate() ) + " )"
					+ " and ( P_EMPLOYEE_DEDUCTION.EFFECTIN is null OR P_EMPLOYEE_DEDUCTION.EFFECTIN <= " + DB.TO_DATE( Period.getEndDate() ) + " )"
					;
	
			P_Employee_Deduction TMP_EmployeeDeduction = null;
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, trxName);
			try
			{
		
				ResultSet rs = pstmt.executeQuery ();
				if (rs.next ())
				{
					TMP_EmployeeDeduction = P_Employee_Deduction.get( Env.getCtx(), rs.getInt("P_Employee_Deduction_ID"),null);
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				System.err.println("getStartPeriod - get - " + e);
			}

			//+ 2025-10-20
			BigDecimal age = new BigDecimal( Employee.getAge( Period.getPayDate() ));
			if ( TMP_EmployeeDeduction != null )
			{
				P_Deduction Deduction = P_Deduction.get( Env.getCtx(), TMP_EmployeeDeduction.getP_Deduction_ID(), trxName);
				if ( Deduction.getValue().equals("DI21G8") && age.compareTo( new BigDecimal(65) ) >= 0 )
		        {
					P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "Employé de plus de 65 ans, la déduction DI21G8 est considéré comme inactive.", trxName);

					return null;
		        }
				//- 2025-10-20
			}
			
			if ( TMP_EmployeeDeduction == null )
				return null;

			if ( TMP_EmployeeDeduction.getTypeRate() == null )
			{
				maxEmr = insurance_Program.getIndividualMaxEmployer();
			}
			else 
			{
				if ( TMP_EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_Individual))
				{
					maxEmr = insurance_Program.getIndividualMaxEmployer();
		
				}
				if ( TMP_EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_SingleParent))
				{
					maxEmr = insurance_Program.getSingleMaxEmployer();
				}
				if ( TMP_EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_Family))
				{
					maxEmr = insurance_Program.getFamilyMaxEmployer();
				}
				if ( TMP_EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_Couple))
				{
					maxEmr = insurance_Program.getCoupleMaxEmployer();
				}
				
			}
			
			if ( TMP_EmployeeDeduction.isExonerated() )
			{
				maxEmr = insurance_Program.getExemptMaxEmployer();
			}

			if ( maxEmr != null && maxEmr.compareTo( Env.ZERO) != 0 )
			{
				s_cacheMaxEmployer.put (key, maxEmr);
			}
			s_cacheLifeInsurance.put (key, TMP_EmployeeDeduction);
			
		}

		return maxEmr;
		
	}
	
	
	//Assurance Dental
	private void calcul_insurance_M82( P_Payment Payment, P_Time_Sheet Timesheet, P_Period Period, P_Employee Employee, MRegion RegionResidence, P_Region Region, P_Method_Deduction   Method_Deduction, P_Employee_Deduction EmployeeDeduction, P_Deduction Deduction, P_Deduction_Param Deduction_Param, String trxName ) throws Exception
	{
		P_Frequency Frequency = P_Frequency.get( Env.getCtx(), Period.getP_Frequency_ID(), trxName);
		
		if ( Deduction_Param.getMaximumAge() != null && Deduction_Param.getMaximumAge().compareTo(Env.ZERO) != 0 )
		{
//			BigDecimal age = Employee.getAgeWithMonth( Period.getPayDate() );
			BigDecimal age = new BigDecimal( Employee.getAge( Period.getPayDate() ));

			if ( age.compareTo( Deduction_Param.getMaximumAge() ) >= 0 )
	        {
	            this.salaryAdmissible = Env.ZERO;  
	            this.hoursAdmissible = Env.ZERO;
	    		this.employeeAmount = Env.ZERO;
	    		this.employerAmount = Env.ZERO;
	    		return;
	        }
		}
		
		// Recherche des montants admissible /
		// Search admisible amount
		BigDecimal insurableSalary = Employee.getInsurableSalary( Period.getP_Period_ID(), Deduction_Param.getP_Deduction_ID() ).divide(new BigDecimal(Frequency.getNumberOfPeriod()),2,BigDecimal.ROUND_HALF_UP );  
		P_Employee_Profile EmployeeProfile = P_Employee_Profile.getProfileInsurance( Env.getCtx(), Employee.getP_Employee_ID(), Period.getEndDate(), trxName);
		
		if ( EmployeeProfile == null )
			P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "L'employé "+Employee.getValue()+" n'a pas de profil d'assurance " + Deduction.getName() , trxName);

		if ( EmployeeProfile != null )
		{
			P_Insurance_Dental Insurance_Dental = P_Insurance_Dental.getByProfile(Env.getCtx(), EmployeeProfile.getP_Profile_ID(), Period.getP_Period_ID(), trxName);
			
			if ( Insurance_Dental == null)
				return;
	
			this.hoursAdmissible  = ZERO;
			this.salaryAdmissible = insurableSalary;

			if ( EmployeeDeduction.getTypeRate() == null )
			{
				EmployeeDeduction.setTypeRate( P_Employee_Deduction.TYPERATE_Individual);
				P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "L'employé "+Employee.getValue()+" n'a pas de type de taux pour l'assurance " + Deduction.getValue() + " " + Deduction.getDescription() , trxName);
				
			}

			
			if ( EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_Individual))
			{
				this.employeeAmount =   Insurance_Dental.getIndividualRate();
				
			}
			if ( EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_SingleParent))
			{
				this.employeeAmount =   Insurance_Dental.getSingleParentRate();
				
			}
			if ( EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_Family))
			{
				this.employeeAmount =   Insurance_Dental.getFamilyRate();
				
			}
			if ( EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_Couple))
			{
				this.employeeAmount =   Insurance_Dental.getCoupleRate();
				
			}

			this.employeeAmount = this.employeeAmount.multiply( new BigDecimal(12)).divide(new BigDecimal(Frequency.getNumberOfPeriod()),2,BigDecimal.ROUND_HALF_UP );
		}


		if ( insurableSalary != null && Deduction_Param.getMethod_Rounding().equals( new String( "T" ) ) )
		{
			insurableSalary = insurableSalary.divide( new BigDecimal( 1000 ), 0, BigDecimal.ROUND_UP );
		}

		
			this.employeeAmount = this.employeeAmount.setScale(2,BigDecimal.ROUND_HALF_UP);

			if ( Deduction_Param.getEmployerRate() != null )
				this.employerAmount = this.employeeAmount.multiply(Deduction_Param.getEmployerRate());

			this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
		
			boolean Quebec = false;
			boolean Ontario = false;
			boolean Manitoba = false;

			if ( RegionResidence != null )
			{
				if ( RegionResidence.getC_Region_ID() == QUEBEC )	Quebec = true;
				if ( RegionResidence.getC_Region_ID() == ONTARIO || Region.getC_Region_ID() == ONTARIO )	Ontario = true;
				if ( RegionResidence.getC_Region_ID() == MANITOBA )	Manitoba = true;
			}

			this.employeeAmount = this.employeeAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
/*
			if ( Deduction_Param.getEmployerRate() != null )
			{
				this.employeeAmount = this.employeeAmount.setScale(2,BigDecimal.ROUND_HALF_UP);

				this.employerAmount = this.employeeAmount.multiply(Deduction_Param.getEmployerRate());

				this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP);

				this.employeeAmount = this.employeeAmount.subtract(this.employerAmount);
			}
*/
			this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
			
			if ( ! Employee.isStatusIndian() )
			{
				if ( Quebec && ( Deduction_Param.getTaxeRate() != null && Deduction_Param.getTaxeRate().compareTo( ZERO ) != 0 ))
				{
					this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
					this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				}

				if ( Ontario && ( Deduction_Param.getTaxeRateOntario() != null && Deduction_Param.getTaxeRateOntario().compareTo( ZERO ) != 0 ))
				{
					this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
					this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				}

				if ( Manitoba && ( Deduction_Param.getTaxeRateManitoba() != null && Deduction_Param.getTaxeRateManitoba().compareTo( ZERO ) != 0 ))
				{
					this.employeeAmount = this.employeeAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
					this.employerAmount = this.employerAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				}
			}	
			
			if ( Deduction_Param.getEmployerRate() != null )
			{
				this.employeeAmount = this.employeeAmount.setScale(2,BigDecimal.ROUND_HALF_UP);

				this.employerAmount = this.employeeAmount.multiply(Deduction_Param.getEmployerRate());

				this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP);

				this.employeeAmount = this.employeeAmount.subtract(this.employerAmount);
			}
			
			this.employeeAmount = this.employeeAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
			this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
				
	}

	//
	// calcul_deduction_others
	//
	private void calcul_deduction_others ( P_Payment Payment, P_Time_Sheet Timesheet, P_Period Period, P_Employee Employee, MRegion RegionResidence, P_Region Region, P_Method_Deduction   Method_Deduction, P_Employee_Deduction EmployeeDeduction, P_Deduction Deduction, P_Deduction_Param Deduction_Param, String trxName ) throws Exception
	{
		P_Frequency Frequency = P_Frequency.get( Env.getCtx(), Period.getP_Frequency_ID(), trxName);
		//
		// Recherche les montants admissible /
//		getSalaryAdmissible( Deduction_Param );

		BigDecimal EmployeeRate = EmployeeDeduction.getEmployeeRate(Deduction_Param ); 
		BigDecimal EmployerRate = EmployeeDeduction.getEmployerRate(Deduction_Param ); 

		BigDecimal EmployeeAmount = EmployeeDeduction.getEmployeeAmount(Deduction_Param );
		BigDecimal EmployerAmount = EmployeeDeduction.getEmployerAmount(Deduction_Param ); 

		//ExemptionAmount
		BigDecimal ExemptionAmount = EmployeeDeduction.getExemptionAmountPeriod(Deduction_Param, Frequency.getNumberOfPeriod());		
//		BigDecimal ExemptionAmount = EmployeeDeduction.getExemptionAmount(Deduction_Param) ;
		if(ExemptionAmount == null )
			ExemptionAmount = Env.ZERO;

		if ( Deduction_Param.getP_Method_Deduction_ID() == 112 && ExemptionAmount.compareTo(Env.ZERO) != 0)
		{
//	    	ExemptionAmount = ExemptionAmount.divide( new BigDecimal(Frequency.getNumberOfPeriod()) ,2,BigDecimal.ROUND_HALF_UP);
			this.salaryAdmissible = salaryAdmissible.subtract( ExemptionAmount);
		}
		else if ( ExemptionAmount.compareTo(Env.ZERO) != 0)
		{
			P_Assignment_Param AssignmentParam = P_Assignment_Param.get( Env.getCtx(), Employee.GetPrincipalAssignment_Param(), trxName );

//	    	ExemptionAmount = ExemptionAmount.divide( new BigDecimal(Frequency.getNumberOfPeriod()) ,2,BigDecimal.ROUND_HALF_UP).multiply( this.hoursAdmissible.divide(AssignmentParam.getWeekly_Hours().multiply(new BigDecimal(2)), 2,BigDecimal.ROUND_HALF_UP)); rejgar
	    	ExemptionAmount = ExemptionAmount.multiply( this.hoursAdmissible.divide(AssignmentParam.getWeekly_Hours().multiply(new BigDecimal(2)), 2,BigDecimal.ROUND_HALF_UP));

			if ( Payment.getSalaryPercentage() != null && Payment.getSalaryPercentage().compareTo( Env.ZERO) != 0)
			{
				ExemptionAmount = ExemptionAmount.multiply(Payment.getSalaryPercentage().divide( new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP)).setScale(2,BigDecimal.ROUND_HALF_UP);
			}
			
			this.salaryAdmissible = salaryAdmissible.subtract( ExemptionAmount);
		}
		
	    employeeAmount = ZERO;
	    employerAmount = ZERO;
	    
		if ( Deduction_Param.getP_Column_Adm_ID() != 0 )
		{
//			P_Column_Adm ColumnAdm = P_Column_Adm.get( Env.getCtx (), Deduction_Param.getP_Column_Adm_ID() , trxName); 

			if ( EmployeeRate != null && EmployeeRate.compareTo( ZERO ) != 0 )
			{
				employeeAmount = salaryAdmissible.multiply( EmployeeRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) );
			}
			
			if ( EmployerRate != null && EmployerRate.compareTo( ZERO ) != 0 )
			{
				employerAmount =  salaryAdmissible.multiply( EmployerRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ) ;
			}
		}
		else
		{
			if ( EmployeeRate != null && EmployeeRate.compareTo( Env.ZERO) != 0 )
			{
				employeeAmount = salaryAdmissible.multiply( EmployeeRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) );
			}
			
			if ( EmployerRate != null && EmployerRate.compareTo( Env.ZERO) != 0)
			{
				employerAmount =  employeeAmount.multiply( EmployerRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ) ;
			}
		}

		
		if ( EmployeeAmount != null )
		{
			employeeAmount = employeeAmount.add( EmployeeAmount );
		}
		
		if ( EmployerAmount != null )
		{
			employerAmount = employerAmount.add( EmployerAmount );
		}
		
		 
		
		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		this.employerAmount = this.employerAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		// Le salaire admissible conservé est sans le montant exemption.
		this.salaryAdmissible = salaryAdmissible.add( ExemptionAmount);
		

		String    methodCalcul     = Method_Deduction.getValue();
		int       i_methodCalcul   = new Integer( methodCalcul ).intValue();
		if ( (i_methodCalcul == 16 || i_methodCalcul == 29) && Deduction_Param.getP_Column_Adm_ID() == 0)
		{
			this.salaryAdmissible = Env.ZERO;
		}

		
	}   // calcul_deduction_others

	private int getAge( Timestamp birthDate, Timestamp currentDate )
	{

		String Year1 = String.valueOf( TimeUtil.getYear(birthDate  ));
		String Year2 = String.valueOf( TimeUtil.getYear(currentDate));
		String Month1 = String.valueOf( TimeUtil.getMonth(birthDate  ));
		String Month2 = String.valueOf( TimeUtil.getMonth(currentDate));
		String Day1 = String.valueOf( TimeUtil.getDay_Of_Month(birthDate  )) ;
		String Day2 = String.valueOf( TimeUtil.getDay_Of_Month(currentDate));
		
		if ( Month1.length() == 1)
			Month1 = "0" + Month1;

		if ( Month2.length() == 1)
			Month2 = "0" + Month2;

		if ( Day1.length() == 1)
			Day1 = "0" + Day1;

		if ( Day2.length() == 1)
			Day2 = "0" + Day2;

		String t1 = Year1 + Month1 + Day1;
		String t2 = Year2 + Month2 + Day2;


		BigInteger i_birthDate = new BigInteger( t1 );
		BigInteger i_currentDate   = new BigInteger( t2 );

		int age = i_currentDate.subtract( i_birthDate ).divide( new BigInteger( "10000" )).intValue();
//		int age2 = TimeUtil.getDaysBetween(birthDate, currentDate ) / 365;
		return age;
	}

	private BigDecimal[] Payment_Excep( P_Payment Payment, P_Period Period, P_Employee_Deduction EmployeeDeduction,  BigDecimal salaryAdmissible, BigDecimal hoursAdmissible, BigDecimal employeeAmount, BigDecimal employerAmount, boolean isExonerated, String trxName) throws Exception
	{
		
		P_Payment_Deduction_Excep[] deductionExcep = P_Payment_Deduction_Excep.get( Env.getCtx (), Payment.getP_Payment_ID(), EmployeeDeduction.getP_Employee_Deduction_ID(), Period.getP_Period_ID(), Payment.getP_Employee_ID(),  trxName );
		BigDecimal[] amts = {null,null,null,null};
		
		if ( deductionExcep != null)
		{
			BigDecimal employeeAmountCum = ZERO;
			BigDecimal employerAmountCum = ZERO;
			
			for ( int i=0; deductionExcep.length > i; i++)
			{
				if (  deductionExcep[i] != null)
				{
					if ( deductionExcep[i].getEmployeeAssessment().equals( P_LongTermLeave_Deduction.EMPLOYEEASSESSMENT_RelievedPlusEmployer) )
					{
						employeeAmount = employerAmount;
						isExonerated = true;
					}
					if ( deductionExcep[i].getEmployerAssessment().equals( P_LongTermLeave_Deduction.EMPLOYERASSESSMENT_AssessPlusEmployee) )
					{
						employerAmount = employerAmount.add( employeeAmount);
					}
					if ( deductionExcep[i].getEmployeeAssessment().equals( P_LongTermLeave_Deduction.EMPLOYEEASSESSMENT_No) )
					{
						employeeAmount = ZERO;
						isExonerated = false;
					}
					if ( deductionExcep[i].getEmployeeAssessment().equals( P_LongTermLeave_Deduction.EMPLOYEEASSESSMENT_Relieved) )
					{
						employeeAmount = ZERO;
						isExonerated = true;
					}
				
					if ( deductionExcep[i].getEmployerAssessment().equals( P_LongTermLeave_Deduction.EMPLOYERASSESSMENT_No) )
					{
						employerAmount = ZERO;
					}

					// 2009.07.08 change Employee_Deduction_Excep by Payment_Deduction_Excep for gain with deduction auto.
					if ( deductionExcep[i].getExceptionAmountEmployee() != null && deductionExcep[i].getExceptionAmountEmployee().compareTo(Env.ZERO) != 0)
					{
						// 2009.08.07 exception avec correction dans le passé. ajout de ce qui a déjà être payer.
						//2010.04.14
						employeeAmountCum = employeeAmountCum.add( deductionExcep[i].getExceptionAmountEmployee() );
						employeeAmount = employeeAmountCum;
					}
					if ( deductionExcep[i].getExceptionAmountEmployer() != null && deductionExcep[i].getExceptionAmountEmployer().compareTo(Env.ZERO) != 0)
					{
						// 2009.08.07 exception avec correction dans le passé. ajout de ce qui a déjà être payer.
						//2010.04.14
						employerAmountCum = employerAmountCum.add( deductionExcep[i].getExceptionAmountEmployer() );
						employerAmount = employerAmountCum;
					}


					//			if ( deductionExcep.getEmployerAssessment().equals( P_LongTermLeave_Deduction.EMPLOYERASSESSMENT_Assess) )
					
					if ( ! deductionExcep[i].isActive() )
					{
						hoursAdmissible  = ZERO;
						salaryAdmissible = ZERO;
						employeeAmount   = ZERO;
						employerAmount   = ZERO;
						
					}
				}
			}
		}
		
		amts[0] = salaryAdmissible;
		amts[1] = hoursAdmissible;
		amts[2] = employeeAmount;
		amts[3] = employerAmount;
		
		return amts;

		
	}


	private BigDecimal[] get_deduction_cumulative_per( P_Payment Payment, P_Period Period, P_Employee_Deduction EmployeeDeduction, P_Period TimeSheetPeriod, String trxName ) throws Exception
	{
		
		BigDecimal cumSalAdmissible = ZERO;
		BigDecimal cumHrsAdmissible = ZERO;
		BigDecimal cumEmpAmount = ZERO;
		BigDecimal cumEmprAmount = ZERO;
		BigDecimal[] amts = {null,null,null,null};

		String query = null;
		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible, isnull(sum( RetrievalAmount),0) RetrievalAmount, isnull(sum( AccumulationAmount),0) AccumulationAmount "
			+ " from P_Payment_Deduction, P_Payment"
			+ " where P_Payment_Deduction.P_Deduction_ID = " + EmployeeDeduction.getP_Deduction_ID ()
			+ " and P_Payment_Deduction.P_Period_ID = " + Period.getP_Period_ID ()
			+ " and P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID ()
 		    + " and P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID AND P_Payment.P_Year_ID = " + TimeSheetPeriod.getP_Year_ID () 
 		    ;
		query += " AND P_Payment.PaymentTypeDoc <> 'Separation'";

		// 2025-10-01 Ne pas tenir comptes des ajustment
		query += " AND P_Payment.PaymentTypeDoc <> 'Adjustement' ";
		
		
		query += " AND P_Payment.PaymentTypeDoc not like 'Annulation%' ";

		query += " AND P_Payment.Taxation_Region_ID = " + Payment.getTaxation_Region_ID();
		query += " AND P_Payment.P_Year_ID = " + Payment.getP_Year_ID();
		
		PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				cumEmpAmount     =   rs.getBigDecimal("Employee_Part").subtract( rs.getBigDecimal("RetrievalAmount") );
				cumEmprAmount    =   rs.getBigDecimal("Employer_Part");
				cumSalAdmissible =   rs.getBigDecimal("Salary_Eligible");
				cumHrsAdmissible =   rs.getBigDecimal("Hours_Eligible");
			}
			else
			{
				cumEmpAmount     =   ZERO;
				cumEmprAmount    =   ZERO;
				cumSalAdmissible =   ZERO;
				cumHrsAdmissible =   ZERO;
			}

			//Log.trace( 5,"calcul_Deduction - cumulatif period emp!! =" + cumEmpAmount );
			//Log.trace( 5,"calcul_Deduction - cumulatif period emr!! =" + cumEmprAmount );
			
			rs.close ();
			pstmt.close ();
			pstmt = null;

	
			amts[0] = cumSalAdmissible;
			amts[1] = cumHrsAdmissible;
			amts[2] = cumEmpAmount;
			amts[3] = cumEmprAmount;
			
			return amts;

	}	

	
	public void apply_insurance( P_Payment Payment, P_Time_Sheet Timesheet, String trxName )
	{
		String query = "Select * FROM P_Payment_Deduction_Insurance Where P_Payment_ID = " + Timesheet.getP_Payment_ID();
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		try
		{
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				BigDecimal NetPay   = Payment.getNetPay();				
				P_Payment_Deduction_Insurance Payment_Ins = new P_Payment_Deduction_Insurance( Env.getCtx(), rs, trxName);

				P_Deduction_Param Deduction_Param = P_Deduction_Param.get( Env.getCtx(), Payment_Ins.getP_Deduction_Param_ID() , trxName);
				P_Method_Deduction method_deduction = P_Method_Deduction.get(Env.getCtx(), Deduction_Param.getP_Method_Deduction_ID() , trxName);

				P_Payment_Deduction PaymentDeduction = new P_Payment_Deduction ( Env.getCtx (), -1, trxName);
				
				PaymentDeduction.setP_Employee_ID( Payment.getP_Employee_ID() );
				PaymentDeduction.setP_Deduction_ID( Payment_Ins.getP_Deduction_ID() );
				P_Period Period = P_Period.get( Env.getCtx(), Payment_Ins.getP_Period_ID(), trxName);
				PaymentDeduction.setP_Year_ID(  Period.getP_Year_ID () );
				PaymentDeduction.setP_Period_ID( Payment_Ins.getP_Period_ID () );
				PaymentDeduction.setP_Payment_ID( Payment_Ins.getP_Payment_ID ());
				PaymentDeduction.setEmployee_Part( Payment_Ins.getEmployee_Part() );
				PaymentDeduction.setEmployer_Part( Payment_Ins.getEmployer_Part() );
				PaymentDeduction.setSalary_Eligible( Payment_Ins.getSalary_Eligible() );
				PaymentDeduction.setHours_Eligible( Payment_Ins.getHours_Eligible() );
				PaymentDeduction.setIsExonerated( Payment_Ins.isExonerated() );
				PaymentDeduction.setOrigine( Payment_Ins.getOrigine());
				PaymentDeduction.setP_Deduction_Param_ID( Payment_Ins.getP_Deduction_Param_ID());
				PaymentDeduction.setP_Employee_Deduction_ID( Payment_Ins.getP_Employee_Deduction_ID() );

				PaymentDeduction.setAccumulationAmount( Payment_Ins.getAccumulationAmount() );
				PaymentDeduction.setRetrievalAmount( Payment_Ins.getRetrievalAmount() );

				
				if (  method_deduction.isAccumulation() )
				{

					BigDecimal emp = Payment_Ins.getEmployee_Part();
//					if ( NetPay.subtract( emp ).compareTo( ZERO) <= 0   )
					if ( NetPay.compareTo( ZERO) > 0 && NetPay.subtract( emp ).compareTo( ZERO) <= 0   )
					{
						PaymentDeduction.setAccumulationAmount( PaymentDeduction.getAccumulationAmount().add( emp.subtract( NetPay ).setScale(2, BigDecimal.ROUND_HALF_UP )) );

						emp = NetPay ;
						PaymentDeduction.setEmployee_Part( emp );
						

					}
				}

				if (  PaymentDeduction.getEmployee_Part().compareTo(ZERO) != 0  ||
						PaymentDeduction.getEmployer_Part().compareTo(ZERO) != 0 ||
						PaymentDeduction.getAccumulationAmount().compareTo(ZERO) != 0 ||
						PaymentDeduction.getRetrievalAmount().compareTo(ZERO) != 0 
					)
				PaymentDeduction.save( );

			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
		}
		catch (Exception e)
		{
	        P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), trxName);
			Timesheet.setTimeSheetStatus( P_Time_Sheet.TIMESHEETSTATUS_Error );
			Timesheet.save( );
			log.log (Level.SEVERE,"calcul - " + query, e);
		}
		
		
		String query2 = "DELETE FROM P_Payment_Deduction_Insurance Where P_Payment_ID = " + Timesheet.getP_Payment_ID();
		DB.executeUpdate(query2, trxName);


	}
	
}
