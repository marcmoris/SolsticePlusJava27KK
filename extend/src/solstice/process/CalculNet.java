/*
 *  * Created on 2005-04-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;
 
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;


//import solstice.model.P_Column_Adm;
import solstice.model.P_Employee_Deduction;
import solstice.model.P_Employee_Taxable_Benefit;
import solstice.model.P_Deduction;
import solstice.model.P_Deduction_Param;
import solstice.model.P_Deduction_Insurance;
import solstice.model.P_Deduction_Insurance_Bound;
import solstice.model.P_Employee;
import solstice.model.P_Employee_Profile;
import solstice.model.P_Assignment;
import solstice.model.P_Assignment_Param;
import solstice.model.P_Collective_Labour_Agr;
import solstice.model.P_Employee_Deduction_Excep;
import solstice.model.P_Employee_Taxable_Benefit_Excep;
import solstice.model.P_Employer;
import solstice.model.P_Frequency;
import solstice.model.P_LongTermLeave_Deduction;
import solstice.model.P_Method_Deduction;
import solstice.model.P_Payment;
import solstice.model.P_Payment_Deduction;
import solstice.model.P_Payment_Deduction_Excep;
import solstice.model.P_Payment_Gain;
import solstice.model.P_Payment_Group;
import solstice.model.P_Payment_Taxable_Benefit;
import solstice.model.P_Period;
import solstice.model.P_Punch_Time;
import solstice.model.P_Region;
import solstice.model.P_Taxable_Benefit;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Time_Sheet_Error;
import solstice.model.P_Gain;
import solstice.model.P_Gain_Parameter;
import solstice.model.P_Insurance_Dental;
import solstice.model.P_Insurance_Health;
import solstice.model.P_Employee_Advance;
import solstice.model.P_Gain_GainInfo;
import solstice.model.P_GainInfo;
import solstice.model.P_UOM;

import solstice.model.P_Workplace;
import solstice.model.P_Year;

//import org.compiere.apps.form.PCalendar;
import org.compiere.model.MRegion;
import org.compiere.model.MLocation;
//import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


import solstice.model.P_Insurance_Param;
import solstice.model.P_Insurance_LTD;
import solstice.model.P_Insurance_Program;
import solstice.model.P_Insurance_STD;
//import solstice.process.QuantityAmountPair;
import solstice.utils.PgiUtil;

import java.util.logging.*;

import org.compiere.util.TimeUtil;

import java.text.DateFormat;
import java.util.Calendar;

import org.compiere.util.Msg;

/**
 * @author marmor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * 
 * 
 *
 */
public class CalculNet
{
	private static CLogger		log = CLogger.getCLogger (CalculNet.class);
    
	// Québec 
	static final public int QUEBEC = 166;
	static final public int ONTARIO = 164;
	static final public int NUNAVUT = 163;
	static final public int NWT = 162;
	static final public int MANITOBA = 158;

	/**	Big Decimal 0	 */
	static final public java.math.BigDecimal ZERO = new java.math.BigDecimal(0.0);

	private String trxName;

    //  Constructeur par défaut
	public CalculNet( Properties ctx, Timestamp PayDate, String _trxName  ) throws Exception
	{
        trxName = _trxName;

		Env.setContext( Env.getCtx(), "#AD_Client_ID", Env.getAD_Client_ID( ctx ));
//		Env.setContext( Env.getCtx(), "#AD_Org_ID", Env.getAD_Org_ID( ctx ) );

//        System.out.println("CalculNet "  + " INFO ENV " + Env.getAD_Client_ID( ctx ) + " " + Env.getAD_Org_ID( ctx ));

		load_tax_parameter( PayDate );
	}
	
	public void resetTrxName( String trxName ) { setTrxName(trxName); }
	
	int taxID = 0;

//	private Timestamp PeriodStartDate;
//	private Timestamp PeriodEndDate;
//	private Timestamp EmployeeBirthDate;
	
	private int	m_updated = 0;

//	Matrix
	private Hashtable<String, QuantityAmountPair> 	s_Matrix = new Hashtable<String, QuantityAmountPair>();	
	private Hashtable<String, QuantityAmountPair> 	s_MatrixTaxB = new Hashtable<String, QuantityAmountPair>();	
//	Total Employee of each Deduction
	private Hashtable<String, BigDecimal> 	s_Employee = new Hashtable<String, BigDecimal>();	
//	Total Employer of each Deduction
	private Hashtable<String, BigDecimal> 	s_Employer = new Hashtable<String, BigDecimal>();	
	private Hashtable<String, BigDecimal>   s_SalaryAdmissible = new Hashtable<String, BigDecimal>();
	private Hashtable<String, BigDecimal> 	taxTable = new Hashtable<String, BigDecimal>();

//	public Hashtable 	s_TP1015 = new Hashtable();	    //  TP1015	
//	public Hashtable 	s_TD1 = new Hashtable();	    //  TD1	
	  
	private P_Payment   Payment;
	private P_Employee  Employee;
	private P_Period    Period; 
	private P_Period    PeriodLine; 
	private P_Frequency Frequency; 
	
	private P_Employee_Deduction EmployeeDeduction; 
	private P_Deduction		     Deduction; 
	private P_Deduction_Param    Deduction_Param; 
	private P_Method_Deduction   Method_Deduction;
	
	private	BigDecimal employerAmount = ZERO;
	private	BigDecimal employeeAmount = ZERO;
	public	BigDecimal salaryAdmissible = ZERO;
	public	BigDecimal hoursAdmissible  = ZERO;
	
	// DEMO
	private BigDecimal employeeAccumulationAmount = ZERO;
	private BigDecimal employerAccumulationAmount = ZERO;

	private boolean isExonerated = false;
	
	private	BigDecimal cumEmployerAmount = ZERO;
	private	BigDecimal cumEmployeeAmount = ZERO;
	private	BigDecimal cumSalaryAdmissible = ZERO;
	private	BigDecimal cumHoursAdmissible  = ZERO;

	private BigDecimal NetPay = ZERO;
	
	private P_Period TimeSheetPeriod;

	private P_Time_Sheet Timesheet;
	
	boolean calculDeduction = false;
	
	private P_Region Region; 
	private MLocation Location;
	private MRegion RegionResidence;
	private P_Workplace Workplace;


	//
	// Conserve les montants Assurance Chomage et de RRQ pour le calcul des impots.
	//
	private BigDecimal Amount_Employment_Insurance = ZERO;
	private BigDecimal Amount_Rpc = ZERO;
	//Modif pour 2024
	private BigDecimal Amount_Rpc_QC = ZERO;
	private BigDecimal Amount_Rpc_F5 = ZERO;
	private BigDecimal Amount_Rpc2 = ZERO;
	private BigDecimal Salary_Rpc = ZERO;
	private BigDecimal Salary_Rpc2 = ZERO;
		
	private BigDecimal Amount_RQAP = ZERO;
	
	private Calcul_federal_tax calcul_federal_tax;
	//
	// Affectation Principal du paiement pour la période.
	// utiliser pour le calcul des fonds de pensions. ( CARRA )
	//
	private P_Assignment Assignment = null;
	
	
	public String Calculation ( P_Payment _Payment ) throws Exception
	{
		Integer key = new Integer ( _Payment.getP_Payment_ID() );


		/*
		s_cacheMaxEmployer.remove(key);
		s_cacheLifeInsurance.remove(key);
		s_cacheEmployerInsurance.remove(key);
		s_cacheEmployeeInsurance.remove(key);
		s_cacheEmployerInsuranceG7.remove(key);
		s_cacheEmployeeInsuranceG7.remove(key);
		s_cacheEmployerInsuranceG9.remove(key);
		s_cacheEmployeeInsuranceG9.remove(key);

		s_cacheEmployerInsuranceG9r.remove(key);
*/
		
		//		int no = 0;
//		boolean IsError = false;
//		boolean IsWarning = false;

//		Env.setAutoCommit( Env.getCtx(), false);
		
//		no = DB.executeUpdate("BEGIN TRANSACTION CALCUL");

		Payment = _Payment; //new P_Payment( Env.getCtx(), P_Payment_ID, trxName);
	
		Frequency = P_Frequency.get( Env.getCtx(), Payment.getP_Frequency_ID() , trxName);
		
		TimeSheetPeriod = P_Period.get( Env.getCtx(), Payment.getP_Period_ID() , trxName);
		
		if ( TimeSheetPeriod.getP_Frequency_ID() != Payment.getP_Frequency_ID() )
		{
			return " ** Error - Error with Parameter ";
		}
	    
		NetPay   = Payment.getGrossEarnings();

		P_Period PeriodPayment = P_Period.get( Env.getCtx (), Payment.getP_Period_ID(), trxName );
		Employee = P_Employee.get (Env.getCtx (), Payment.getP_Employee_ID () , trxName);
	
		Payment.setAnnualSalary( Employee.getAnnualSalary( PeriodPayment.getEndDate() ));
		Payment.save( );
		
		//log.info("Calcul - Employee - " + Employee.getValue () + " " + Employee.getName ()  +  " Payment - " + Payment.getP_Payment_ID () + " Period - " + PeriodPayment.getP_Period_ID() );
		
		
		Timesheet = P_Time_Sheet.GetWithPaymentID( Env.getCtx(), Payment.getP_Payment_ID(), trxName );

		Region = P_Region.get( Env.getCtx (), Timesheet.getTaxation_Region_ID(), trxName);
		Location = MLocation.get( Env.getCtx(), Employee.getC_Location_ID(), trxName);
		if ( Location != null )
			RegionResidence = new MRegion( Env.getCtx(), Location.getC_Region_ID(), trxName);
		
		if ( RegionResidence == null || Location.getC_Region_ID() == 0	)
		{
			P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "L'employé "+Employee.getValue()+" n'a pas province de résidence", trxName);
			RegionResidence = new MRegion( Env.getCtx(), Employee.getTaxation_Region_ID(), trxName);
		}
	
		//2015.09.30 Détermine le lieux de travail pour la taxe qui s'applique au employé du Nunavut et NWT
		Period = P_Period.get(  Env.getCtx (), Payment.getP_Period_ID(), trxName );
		Assignment = read_assignment_info();
		if ( Assignment == null )
		{
			Assignment = read_assignment_info2();
		}
		if ( Assignment != null && Assignment.getP_Workplace_ID() != 0 )
		{
			Workplace = P_Workplace.get( Env.getCtx(), Assignment.getP_Workplace_ID(), trxName);
			if ( Workplace.isTaxable() == false)
				Workplace = P_Workplace.get( Env.getCtx(), Payment.getP_Workplace_ID(), trxName);
		} else {
			Workplace = P_Workplace.get( Env.getCtx(), Payment.getP_Workplace_ID(), trxName);
		}
			
				
		//
		// Loop by origin Period 
		//
		String query = null;
//		query = "Select distinct P_Period_ID FROM P_Payment_Gain WHERE P_Payment_ID=" + Payment.getP_Payment_ID () + " AND IsActive='Y' ";
		// 2022-08-25 ajout gain non rémunérer en exeption 
		query = "Select distinct P_Period_ID FROM P_Payment_Gain WHERE P_Payment_ID=" + Payment.getP_Payment_ID () + " AND IsActive='Y' "
				+ " AND P_Gain_ID NOT IN ( select p_gain_id from P_GAIN_GAININFO "
				+ " inner join P_GAININFO ON P_GAININFO.P_GAININFO_ID = P_GAIN_GAININFO.P_GAININFO_ID "
				+ " where P_GAIN_GAININFO.TO_CONSIDER = 'Y' "
				+ " AND P_GAININFO.value = 'NonImpos' ) ";
		query += " union  select distinct P_Period_ID FROM P_Calcul_Net_Correction  WHERE P_Employee_ID = " + Payment.getP_Employee_ID();
		query += " order by P_Period_ID ";
		PreparedStatement pstmt_period = null;

		//Log.trace( 5, "Calcul - Query Period " + query  );

	
		try
		{
	
			pstmt_period = DB.prepareStatement (query, trxName);

			ResultSet rs_period = pstmt_period.executeQuery ();
			while (rs_period.next ())
			{

				s_Matrix.clear();
				s_MatrixTaxB.clear();
				s_Employee.clear();
				s_Employer.clear();
				s_SalaryAdmissible.clear();
				
				//+2023-05-28
				
				s_cacheMaxEmployer.remove(key);
				s_cacheLifeInsurance.remove(key);
				s_cacheEmployerInsurance.remove(key);
				s_cacheEmployeeInsurance.remove(key);
				s_cacheEmployerInsuranceG7.remove(key);
				s_cacheEmployeeInsuranceG7.remove(key);
				s_cacheEmployerInsuranceG9.remove(key);
				s_cacheEmployeeInsuranceG9.remove(key);
				s_cacheEmployerInsuranceG9r.remove(key);
				//-2023-05-28 

				
				isExonerated = false;
				this.Amount_Employment_Insurance = ZERO;
				this.Amount_Rpc = ZERO;

				//Modif pour 2024
				this.Amount_Rpc2 = ZERO;
				this.Amount_Rpc_QC = ZERO;
				this.Amount_Rpc_F5 = ZERO;
				this.Salary_Rpc = ZERO;
				this.Salary_Rpc2 = ZERO;
								
				
				this.Amount_RQAP = ZERO;
				
				
				
				calcul_federal_tax = new Calcul_federal_tax( Env.getCtx(), this, trxName );
				
				int period_ID = rs_period.getInt(1);
				
				Period = P_Period.get(  Env.getCtx (), period_ID, trxName );
				PeriodLine = P_Period.get(  Env.getCtx (), rs_period.getInt("P_Period_ID"), trxName );

				Calcul_insurance calass = new Calcul_insurance(); 

				//
				// On calcul des déductions seulement pour les feuilles de type Régulière ou Complémentaire.
				// POur les feuilles de type avance, la gestion ce fait au niveau de la validation du temps.
				//
				if ( Timesheet.getSheetType().equals( P_Time_Sheet.SHEETTYPE_Regular) 
					     || Timesheet.getSheetType().equals( P_Time_Sheet.SHEETTYPE_Complementary )
					     || Timesheet.getSheetType().equals( P_Time_Sheet.SHEETTYPE_ExpenseAccount )
					     || Timesheet.getSheetType().equals( P_Time_Sheet.SHEETTYPE_HolidayPayment )
					     || Timesheet.getSheetType().equals( P_Time_Sheet.SHEETTYPE_SeparationPay ))
				{
					//
					// Détermine l'affectation principal de la feuille de temps.
					//
					Assignment = read_assignment_info();
					if ( Assignment == null )
					{
						Assignment = read_assignment_info2();
					}
					
					if ( Assignment != null )
					{
						//
						// Update the matrix with gain information
						//
						read_gain_info( );

						//
						// Debug information
						//
//						info_matrix();

						//
						// Taxable Benefits Processing
						//
						if ( ! Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_ExpenseAccount ))
						{
//							log.info("Calcul -  Calcul Taxable Benefit "  );
							calculDeduction = false;
							calcul_taxable_benefit( );

							
							// 2025-06-03 Calcul les assurances 
							calass.calcul( Payment, Period, RegionResidence, Region, trxName);

						}


						//
						// Update the matrix with taxable benefit information
						//
						read_taxable_benefit_info( );
						
						// 2021-06-19
						// Création d'une ligne de gain pour le remboursement du taux réduit a l'assurence emploie.
						//
						if (PgiUtil.getSolsticeParameter(Env.getCtx(), "Refund_EI").equals("True") && Employee.isRefundEIActive()  )
						{
							if ( ! Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_ExpenseAccount ))
							{
								gen_time_sheet_detail_IE( Payment, Period, trxName );
								s_Matrix.clear();
								read_gain_info( );
								
								read_taxable_benefit_info() ;
								
							}

						}
						
						s_cacheEmployerInsurance.remove(key);
						s_cacheEmployeeInsurance.remove(key);
/*						s_cacheEmployerInsuranceG7.remove(key);
						s_cacheEmployeeInsuranceG7.remove(key);
						s_cacheEmployerInsuranceG9.remove(key);
						s_cacheEmployeeInsuranceG9.remove(key);
*/						
						//
						// Deduction Processing
						//
//						log.info("Calcul -  Calcul Deduction "  );
						// 2024-04-10 ne pas calculer des déductions sur un compte de dépense.
						if ( ! Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_ExpenseAccount ))
						{
							calculDeduction = true;

							Period = P_Period.get(  Env.getCtx (), period_ID, trxName );
						
							calcul_deduction( );	
						}
						
						calass.apply_insurance( Payment, Timesheet, trxName);
						
					}
					else
					{
						P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "L'employé "+Employee.getValue()+" n'a pas d'affectation principale en date du "+ DateFormat.getTimeInstance(DateFormat.SHORT).format( Period.getStartDate() ), trxName);
					}
				}

				Timesheet.setTimeSheetStatus( P_Time_Sheet.TIMESHEETSTATUS_Calculated);
				
/*				IsError   = P_Time_Sheet.CheckError( Timesheet.getP_Time_Sheet_ID(), trxName );
				IsWarning = P_Time_Sheet.CheckWarning( Timesheet.getP_Time_Sheet_ID(), trxName );
				
				if ( IsError )
	                Timesheet.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Error);

				Timesheet.setIsError( IsError );
				Timesheet.setIsWarning( IsWarning );
*/				
				Timesheet.save( );

			}
			rs_period.close ();
			pstmt_period.close ();
			pstmt_period = null;


			m_updated++;

			// pour les feuilles de temps sans détail.
			if ( Timesheet.getTimeSheetStatus().equals(P_Time_Sheet.TIMESHEETSTATUS_Validated))
			{
				Timesheet.setTimeSheetStatus( P_Time_Sheet.TIMESHEETSTATUS_Calculated);
				Timesheet.save( );

			}

			Payment.save( );

			//2009.02.25 valide le montant net du paiement
			if ( Payment.getNetPay().compareTo(Payment.getGrossEarnings()) > 0)
			{
	            P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Warning, "Le montant net est supérieur au montant brut !!! ", trxName);
			}

			if ( Payment.getNetPay().compareTo( Env.ZERO) < 0 && ! Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_Adjustement))
			{
	            P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Warning, "Le montant net est inférieur a 0 !!! ", trxName);
			}

			
			Validation_Client( Env.getCtx(), Payment, Employee, null);
			
// Déplacer dans la validation du temps.			
//			Payment.createEntryLine( trxName);

		}
		catch (Exception e)
		{
            P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " +  e.toString(), trxName);
			Timesheet.setTimeSheetStatus( P_Time_Sheet.TIMESHEETSTATUS_Error );
			Timesheet.save( );
			log.log (Level.SEVERE,"calcul - " + query, e);
			throw e;
		}

		return "@Updated@ = " + m_updated;
		
	}   //	calcul
	
	private void load_tax_parameter( Timestamp CurrentDate ) throws Exception
	{
//		log.debug ("Load_tax_parameter Begin");
		taxTable.clear();

		taxID = Calcul_federal_tax.get_federal_tax_id( CurrentDate );
		taxTable = Calcul_federal_tax.get_federal_tax_para(  taxID, CurrentDate);

	}

	// *************************
	//                          
	// ***************************     
	private P_Assignment read_assignment_info (  ) throws Exception
	{
		//
		// Détermine l'affectation de la feuille de temps avec l'affectation la plus utiliser pour la période.
		//
//		P_Assignment AssignmentParam = null;
		
		String query = null;
		query = "SELECT P_Assignment_ID FROM P_Payment_Gain WHERE IsActive='Y' ";  
		query += " AND P_Payment_ID = " + Payment.getP_Payment_ID ();
		query += " AND P_Period_ID = " + this.Period.getP_Period_ID ();
		query += " GROUP BY P_Assignment_ID ";
		query += " ORDER BY COUNT(*) Desc";
		
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			Assignment =  P_Assignment.get( Env.getCtx(), rs.getInt("P_Assignment_ID"), trxName );
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
		
		return Assignment;
		
	}   // read_assignment_info

	// *************************
	//                          
	// ***************************     
	private P_Assignment read_assignment_info2 (  ) throws Exception
	{
		//
		// Détermine l'affectation de la feuille de temps avec l'affectation la plus utiliser pour la période.
		//
//		P_Assignment AssignmentParam = null;
		
		String query = null;
		query = "SELECT P_Assignment_ID FROM P_Payment_Gain WHERE IsActive='Y' ";  
//		query += " AND P_Payment_ID = " + Payment.getP_Payment_ID ();
		query += " AND P_Employee_ID = " + Payment.getP_Employee_ID ();
		query += " AND P_Period_ID = " + this.Period.getP_Period_ID ();
		query += " GROUP BY P_Assignment_ID ";
		query += " ORDER BY COUNT(*) Desc";
		
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			Assignment =  P_Assignment.get( Env.getCtx(), rs.getInt("P_Assignment_ID"), trxName );
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
		
		return Assignment;
		
	}   // read_assignment_info


	
	// *************************
	//                          
	// ***************************     
	private void read_gain_info (  ) throws Exception
	{
//		log.debug ("read_gain_info Begin");
		//
		// Loop on Payment Gain
		//
		
/*
 * SELECT P_Payment_Gain.P_Gain_Parameter_ID, QuantityCalc, AmountCalc, P_Gain_ID, P_Assignment_ID, P_Gain_Column.P_Column_Adm_ID P_Column_Adm_ID
FROM P_Payment_Gain, P_Gain_Column, P_Column_Adm
WHERE P_Payment_Gain.IsActive='Y'  AND P_Payment_Gain.P_Employee_ID = 1003139 
AND P_Gain_Column.P_Column_Adm_ID = P_Column_Adm.P_Column_Adm_ID  
AND P_Gain_Column.IsActive='Y'  
AND P_Column_Adm.IsActive = 'Y'  
AND P_Gain_Column.IsAdmissible = 'Y'  
AND P_Gain_Column.P_Gain_Parameter_ID=P_Payment_Gain.P_Gain_Parameter_ID
AND P_Period_ID = ( select p_period_id from p_period where name like '2005-19')
and P_Gain_Column.P_Column_Adm_ID = 102
ORDER BY line

 */		
		String query = null;
		query = "SELECT P_Payment_Gain.P_Gain_Parameter_ID, P_Payment_Gain.QuantityCalc, P_Payment_Gain.AmountCalc, P_Payment_Gain.P_Gain_ID, P_Payment_Gain.P_Assignment_ID, P_Method_Gain_ID, P_Payment_Gain.Day "
			  +" FROM P_Payment, P_Payment_Gain, P_Gain WHERE P_Payment_Gain.P_Gain_ID = P_Gain.P_Gain_ID ";  
//		query += " AND P_Payment_ID = " + Payment.getP_Payment_ID ();
		query += " AND P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID ();
		query += " AND P_Payment_Gain.P_Period_ID = " + this.Period.getP_Period_ID ();
		query += " AND P_Payment.P_Payment_ID = P_Payment_Gain.P_Payment_ID AND P_Payment.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID();  // <-- Correction pour un année antérieur ne doit pas aller revoir le cumulatif.

		//		 2009.10.14
//		if ( ! Payment.getPaymentTypeDoc().equals(P_Payment.PAYMENTTYPEDOC_SeparationPay))
			query += " AND P_Payment.PaymentTypeDoc <> 'Separation' ";

		// 20100903 Ne pas tenir comptes des paiements annulé
		query += " AND P_Payment.PaymentTypeDoc not like 'Annulation%' ";
		
		// 2025-10-01 Ne pas tenir comptes des ajustment
		query += " AND P_Payment.PaymentTypeDoc <> 'Adjustement' ";
		
		//+ 2011.03.01 ajout la gestion d<un changement de province
		query += " AND P_Payment.Taxation_Region_ID = " + Payment.getTaxation_Region_ID();
        //- 2011.03.01

		//+ 2024-01-10
		query += " AND P_Payment.P_Year_ID = " + Payment.getP_Year_ID();
		//+ 2024-01-10

		
		query += " ORDER BY line";
		
		//Log.trace( 5,"calcul_gain - query=" + query);
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
	
		P_Gain Gain;
		P_UOM Uom;
		while (rs.next ())
		{
			
			int P_Gain_Parameter_ID = rs.getInt("P_Gain_Parameter_ID");
			BigDecimal hoursAdmissible = ZERO;
			if ( rs.getInt( "P_Method_Gain_ID") != 105 && rs.getInt( "P_Method_Gain_ID") != 300 ) 
				hoursAdmissible = rs.getBigDecimal("QuantityCalc");
			
			Gain = P_Gain.get( Env.getCtx(), rs.getInt("P_Gain_ID"), trxName);
			Uom = P_UOM.get( Env.getCtx(), Gain.getP_UOM_ID(), trxName);
			
			// Si le code de gain est en jours
			if ( Uom.getValue().equals("J"))
			{
				hoursAdmissible = hoursAdmissible.multiply( new BigDecimal(8));
			}
			
			BigDecimal salaryAdmissible = rs.getBigDecimal("AmountCalc");
			
			update_matrix ( P_Gain_Parameter_ID, hoursAdmissible, salaryAdmissible, "Gain", rs.getInt("P_Assignment_ID"), rs.getTimestamp("Day") );
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
		
//		log.debug ("read_gain_info End");
		return ;
		
	}   // calcul_gain
	
	
	// *************************
	//                          
	// ***************************     
	private void read_taxable_benefit_info ( ) throws Exception
	{
//		log.debug ("read_taxable_benefit_info Begin");
		//
		// Loop on Payment Gain
		//
		String query = null;
		query = "SELECT P_Taxable_Benefit_ID, Taxable_Benefit_Amount FROM P_Payment_Taxable_Benefit, P_Payment WHERE P_Payment_Taxable_Benefit.IsActive='Y' "  
			+  " AND P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID ()
		    +  " AND P_Payment.P_Payment_ID = P_Payment_Taxable_Benefit.P_Payment_ID AND P_Payment.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID()  // <-- Correction pour un année antérieur ne doit pas aller revoir le cumulatif.
		    +  " AND P_Payment_Taxable_Benefit.P_Period_ID = " + this.Period.getP_Period_ID ()
		    ;
		// 20100903 Ne pas tenir comptes des paiements annulé
		query += " AND P_Payment.PaymentTypeDoc not like 'Annulation%' ";

		//+ 2011.03.01 ajout la gestion d<un changement de province
		query += " AND P_Payment.Taxation_Region_ID = " + Payment.getTaxation_Region_ID();
        //- 2011.03.01

		//+ 2024-01-10
		query += " AND P_Payment.P_Year_ID = " + Payment.getP_Year_ID();
		//+ 2024-01-10
		
//		Log.trace( 5,"read_taxable_benefit_info - query=" + query);
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();

		while (rs.next ())
		{
			
			int P_Taxable_Benefit_ID = rs.getInt("P_Taxable_Benefit_ID");
			BigDecimal hoursAdmissible = ZERO;
			BigDecimal salaryAdmissible = rs.getBigDecimal("Taxable_Benefit_Amount");
			
//			P_Taxable_Benefit Taxable_Benefit = P_Taxable_Benefit.get( Env.getCtx (),  P_Taxable_Benefit_ID, trxName );
			
			update_matrix ( P_Taxable_Benefit_ID, hoursAdmissible, salaryAdmissible, "Taxable_Benefit" , 0, null);
			
			
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
		return;
		
	}   // read_taxable_benefit_info
	
	
	
	// *************************
	//  
	//  Manage the admisibility column
	// 
	//                          
	// ***************************     
	private void update_matrix ( int p_id, BigDecimal hoursAdmissible, BigDecimal salaryAdmissible, String table, int P_Assignment_ID, Timestamp Day ) throws Exception
	{
		//
		// Loop on Gain Admisibility
		//
		//		log.debug ("update_matrix - Insert/Update into the Matrix - table : " + table );
		
//		int no = 0;
		
		BigDecimal hoursAdm;
		BigDecimal salaryAdm;

		BigDecimal hoursAdmTB;
		BigDecimal salaryAdmTB;

		String query = null;
		
		if ( 0 == table.compareTo( "Gain" )) 
		{
			
			query = "SELECT P_Gain_Column.P_Column_Adm_ID P_Column_Adm_ID "
				+ "FROM P_Gain_Column, P_Column_Adm "
				+ "WHERE P_Gain_Column.P_Column_Adm_ID = P_Column_Adm.P_Column_Adm_ID "
				+ " AND P_Gain_Column.IsActive='Y' "
				+ " AND P_Column_Adm.IsActive = 'Y' " 
				+ " AND P_Gain_Column.IsAdmissible = 'Y' "
				+ " AND P_Gain_Parameter_ID=" + p_id;
		}
		else {
			
			query = "SELECT P_Taxable_Benefit_Column.P_Column_Adm_ID P_Column_Adm_ID "
				+ " FROM P_Taxable_Benefit_Column, P_Column_Adm "
				+ "WHERE P_Taxable_Benefit_Column.P_Column_Adm_ID = P_Column_Adm.P_Column_Adm_ID "
				+ " AND P_Taxable_Benefit_Column.IsActive='Y' "
				+ " AND P_Column_Adm.IsActive = 'Y' " 
				+ " AND P_Taxable_Benefit_Column.IsAdmissible = 'Y' "
				+ " AND P_Taxable_Benefit_ID=" + p_id;
		}
		
		//Log.trace( 5,"update_matrix - query=" + query);
		PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			
			P_Gain_Parameter GainParameter;
			P_Gain Gain;
			P_Assignment_Param AssignmentParam;
			P_GainInfo GainInfo;
			P_Gain_GainInfo GainGainInfo;
			
			while (rs.next ())
			{
				String columnAdmin = rs.getString("P_Column_Adm_ID");
			
				//
				//  Permet d'ajouter les Gain d'heures non-rémunérés
				//  admissibles à la CARRA.  On calcul un montant admissible
				//  à partir des heures de la feuilles de temps.
				//
				// HNR P_Method_Gain_ID = 104
				// CARRA P_Column_Adm_ID = 104
				if ( P_Assignment_ID != 0 && rs.getInt("P_Column_Adm_ID") == 104 )
				{
					hoursAdm  = hoursAdmissible;
					salaryAdm = salaryAdmissible;

					GainParameter = P_Gain_Parameter.get( Env.getCtx(), p_id, trxName);
					Gain = new P_Gain( Env.getCtx(), GainParameter.getP_Gain_ID(), trxName );

//					if ( Gain.getP_Method_Gain_ID() == 104 && Day != null )
					if ( Gain.isHNR() && Day != null )
					{
						AssignmentParam = P_Assignment_Param.get( Env.getCtx(), P_Assignment_ID, Day , trxName); // Period.getStartDate()
						salaryAdm = hoursAdmissible.multiply( AssignmentParam.getHourly_Rate());
//						hoursAdmissible = rs.getBigDecimal(1);
					}
				}
				
				// Assurance emploi, les heures de temps suplémentaire ne doivent pas être inclus dans les heures adm.
				else if ( rs.getInt("P_Column_Adm_ID") == 103 && 0 == table.compareTo( "Gain" ))
				{
					hoursAdm  = hoursAdmissible;
					salaryAdm = salaryAdmissible;

					GainParameter = P_Gain_Parameter.get( Env.getCtx(), p_id, trxName);
//					GainInfo = P_GainInfo.get( Env.getCtx(), "SUP");
					// 2013.10.10 Heure non admissible assurance emploi.
					// traite immédiatement les heures non admissible et non sur le relevé d'emploi.					
					GainInfo = P_GainInfo.get( Env.getCtx(), "HreAE");
					GainGainInfo = P_Gain_GainInfo.get( Env.getCtx(), GainParameter.getP_Gain_ID(), GainInfo.getP_GainInfo_ID());
					if (GainGainInfo.isTo_Consider())
					{
						hoursAdm  = Env.ZERO;
					}
				}
				else 
				{
					hoursAdm  = hoursAdmissible;
					salaryAdm = salaryAdmissible;
				}
				
				if ( salaryAdm == null )
					salaryAdm = Env.ZERO;

				QuantityAmountPair vAdm = null;
				if ( hoursAdm != null )
				{
					if ( s_Matrix.containsKey( columnAdmin ) )
					{
						vAdm = (QuantityAmountPair)s_Matrix.get( columnAdmin );
						hoursAdm = hoursAdm.add( vAdm.getQuantity() );
						salaryAdm = salaryAdm.add( vAdm.getAmount() );
						s_Matrix.remove( columnAdmin );
					}
					vAdm = new QuantityAmountPair( hoursAdm, salaryAdm ); 
					s_Matrix.put( columnAdmin , vAdm );
				}

				// Conserve salaraire admissible des avantages imposables du Québec.
				if ( 0 != table.compareTo( "Gain" ))
				{
					P_Taxable_Benefit tb = P_Taxable_Benefit.get( Env.getCtx(), p_id, trxName);
					if ( salaryAdmissible != null && ( tb.getIsProvincial().equals(P_Taxable_Benefit.ISPROVINCIAL_Provincial) || tb.getIsProvincial().equals(P_Taxable_Benefit.ISPROVINCIAL_FederalAndProvincial )) )
					{
						hoursAdmTB = Env.ZERO;
						salaryAdmTB = salaryAdmissible; 
						if ( s_MatrixTaxB.containsKey( columnAdmin ) )
						{
							vAdm = (QuantityAmountPair)s_MatrixTaxB.get( columnAdmin );
							hoursAdmTB = hoursAdmTB.add( vAdm.getQuantity() );
							salaryAdmTB = salaryAdmTB.add( vAdm.getAmount() );
							s_MatrixTaxB.remove( columnAdmin );
						}
						vAdm = new QuantityAmountPair( hoursAdmTB, salaryAdmTB ); 
						s_MatrixTaxB.put( columnAdmin , vAdm );
					}
					
				}

				
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		
	}   //  update_matrix
	
	
	public BigDecimal get_matrix_hoursAdmissible ( int columnAdmin )
	{
		String sColumnAdmin = String.valueOf( columnAdmin );
		QuantityAmountPair vAdm;
		if ( s_Matrix.containsKey(  sColumnAdmin ))
		{
			vAdm = (QuantityAmountPair)s_Matrix.get( sColumnAdmin );
			return vAdm.getQuantity();
		}
		return ZERO;
		
	}   // get_matrix
	
	public BigDecimal get_matrix_salaryAdmissible ( int columnAdmin )
	{
		String sColumnAdmin = String.valueOf( columnAdmin );
		QuantityAmountPair vAdm;
		if ( s_Matrix.containsKey(  sColumnAdmin ))
		{
			vAdm = (QuantityAmountPair)s_Matrix.get( sColumnAdmin );
			return vAdm.getAmount();
		}
		return ZERO;
		
	}   // get_matrix

	private BigDecimal get_matrixTaxB_salaryAdmissible ( int columnAdmin )
	{
		String sColumnAdmin = String.valueOf( columnAdmin );
		QuantityAmountPair vAdm;
		if ( s_MatrixTaxB.containsKey(  sColumnAdmin ))
		{
			vAdm = (QuantityAmountPair)s_MatrixTaxB.get( sColumnAdmin );
			return vAdm.getAmount();
		}
		return ZERO;
		
	}   // get_matrix
	
	
	private void info_matrix (  )
	{
		// Iterate over the values in the map
		for( Enumeration e = s_Matrix.keys(); e.hasMoreElements(); )
		{
			
			QuantityAmountPair vAdm;
			vAdm = (QuantityAmountPair)s_Matrix.get( e.nextElement().toString());
			log.log( Level.INFO, "info_matrix - Column: " + e.nextElement().toString() + " Quantity " + vAdm.getQuantity() + " Amount " + vAdm.getAmount());
		}		
		
		
	}   // info_matrix
	

	private void createMissingTaxableBenefit ( ) throws Exception
	{
		String query = "select distinct P_Deduction.P_Taxable_Benefit_ID from P_Deduction " 
					 + " inner join P_Employee_Deduction on P_Employee_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID and P_Employee_Deduction.P_Employee_ID = " + Payment.getP_Employee_ID()
					 + " where p_taxable_benefit_id is not null "
					 + " and not exists( select 1 from P_Employee_Taxable_Benefit where P_Employee_Taxable_Benefit.P_Employee_ID = P_Employee_Deduction.P_Employee_ID and P_Employee_Taxable_Benefit.P_Taxable_Benefit_ID = P_Deduction.P_Taxable_Benefit_ID ) "
					 ;
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);

		ResultSet rs = pstmt.executeQuery ();
		P_Employee_Taxable_Benefit EmployeeTaxableBenefit;
		
		while (rs.next ())
		{
			Timestamp l_EffectIn = Period.getStartDate();
			if ( Employee.getDateHired().compareTo( Period.getStartDate() ) > 0)
				l_EffectIn = Employee.getDateHired();
			
			P_Taxable_Benefit Taxable_Benefit = P_Taxable_Benefit.get( Env.getCtx (),  rs.getInt("P_Taxable_Benefit_ID"), trxName );
			EmployeeTaxableBenefit = P_Employee_Taxable_Benefit.get( Env.getCtx (),  Employee.getP_Employee_ID(), Taxable_Benefit.getP_Taxable_Benefit_ID(),  Period.getEndDate() ,trxName );
			if ( EmployeeTaxableBenefit.getP_Employee_Taxable_Benefit_ID() <= 0)
			{
				EmployeeTaxableBenefit.setP_Employee_ID(Payment.getP_Employee_ID());
				EmployeeTaxableBenefit.setP_Taxable_Benefit_ID( Taxable_Benefit.getP_Taxable_Benefit_ID());
				EmployeeTaxableBenefit.setIsActive(true);
				EmployeeTaxableBenefit.setAD_Org_ID(Payment.getAD_Org_ID());
				EmployeeTaxableBenefit.setEffectIn( l_EffectIn );
				EmployeeTaxableBenefit.save();
				P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol09-I") +  " " + Taxable_Benefit.getValue() +  " " + Taxable_Benefit.getName(), trxName);
			}
		}

		rs.close ();
		pstmt.close ();
		pstmt = null;

		//2013.02.04 ajout ici car plusieur déduction peuvent avoir un avantage fédéral identique mais pas l'avantage provincial.
		// avant on conservait uniquement la dernière déduction.
//		RegionResidence = new MRegion( Env.getCtx(), Payment.getTaxation_Region_ID(), trxName);
		if (  RegionResidence.getC_Region_ID() == QUEBEC  )
		{

			query = "select distinct P_Deduction.P_Taxable_Benefit_Provincial_ID from P_Deduction " 
					 + " inner join P_Employee_Deduction on P_Employee_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID and P_Employee_Deduction.P_Employee_ID = " + Payment.getP_Employee_ID()
					 + " where P_Taxable_Benefit_Provincial_ID is not null "
					 + " and not exists( select 1 from P_Employee_Taxable_Benefit where P_Employee_Taxable_Benefit.P_Employee_ID = P_Employee_Deduction.P_Employee_ID and P_Employee_Taxable_Benefit.P_Taxable_Benefit_ID = P_Deduction.P_Taxable_Benefit_Provincial_ID ) "
					 ;
			pstmt = DB.prepareStatement (query, trxName);

			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				P_Taxable_Benefit Taxable_Benefit = P_Taxable_Benefit.get( Env.getCtx (),  rs.getInt("P_Taxable_Benefit_Provincial_ID"), trxName );
				//2021-10-21 utiliser la date d'embauche si plus grande que la date de début de période.
				Timestamp l_EffectIn = Period.getStartDate();
				if ( Employee.getDateHired().compareTo( Period.getStartDate() ) > 0)
					l_EffectIn = Employee.getDateHired();
					
				P_Employee_Taxable_Benefit EmployeeTaxableBenefitProv = P_Employee_Taxable_Benefit.get( Env.getCtx (),  Employee.getP_Employee_ID(), Taxable_Benefit.getP_Taxable_Benefit_ID() ,  Period.getEndDate() ,trxName );
				if ( EmployeeTaxableBenefitProv.getP_Employee_Taxable_Benefit_ID() <= 0)
				{
					EmployeeTaxableBenefitProv.setP_Employee_ID(Payment.getP_Employee_ID());
					EmployeeTaxableBenefitProv.setP_Taxable_Benefit_ID( Taxable_Benefit.getP_Taxable_Benefit_ID() );
					EmployeeTaxableBenefitProv.setIsActive(true);
					EmployeeTaxableBenefitProv.setAD_Org_ID(Payment.getAD_Org_ID());
					EmployeeTaxableBenefitProv.setEffectIn( l_EffectIn );
					EmployeeTaxableBenefitProv.save();

					//2013.02.04 message d'erreur car si l'avantage n'est pas dans le dossier employé il ne se calcul pas.
					P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol09-I") + " " + Taxable_Benefit.getValue() +  " " + Taxable_Benefit.getName(), trxName);
				}
				
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

		}

	}

	
/*	private void Create_Payment_Taxable_Benefit ( P_Payment Payment )
	{
		String sql = "SELECT P_Taxable_Benefit_ID, P_Payment_deduction.P_Period_ID, SUM(Employer_Part) BenefitAmount"
				+ " FROM P_Payment_deduction "
				+ " INNER JOIN P_deduction on P_deduction.P_deduction_ID = P_Payment_deduction.P_deduction_ID "
				+ " WHERE P_Payment_Deduction.P_Payment_ID = " + Payment.getP_Payment_ID() 
				+ " AND P_deduction.P_Taxable_Benefit_ID is not null"
				+ "  GROUP BY P_Taxable_Benefit_ID, P_Payment_deduction.P_Period_ID ";

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (sql, trxName);
		
		try
		{
			
			ResultSet rs = pstmt.executeQuery ();
		
			while (rs.next ())
			{
				P_Payment_Taxable_Benefit PTaxable_Benefit = new P_Payment_Taxable_Benefit( Env.getCtx (), -1 , trxName );
				
				P_Employee_Taxable_Benefit EmployeeTaxableBenefit = P_Employee_Taxable_Benefit.get( Env.getCtx (),  Payment.getP_Employee_ID(), rs.getInt("P_Taxable_Benefit_ID"),  Period.getEndDate() ,trxName );
	
				PTaxable_Benefit.setP_Employee_ID( Payment.getP_Employee_ID ());
				PTaxable_Benefit.setP_Taxable_Benefit_ID( rs.getInt("P_Taxable_Benefit_ID") );
				PTaxable_Benefit.setP_Year_ID(  Period.getP_Year_ID () );
				PTaxable_Benefit.setP_Period_ID( rs.getInt("P_Period_ID") );
				PTaxable_Benefit.setP_Payment_ID( Payment.getP_Payment_ID ());
	
				PTaxable_Benefit.setTaxable_Benefit_Amount( rs.getBigDecimal("BenefitAmount")  );
				PTaxable_Benefit.setP_Employee_Taxable_Benefit_ID( EmployeeTaxableBenefit.getP_Employee_Taxable_Benefit_ID() );
				PTaxable_Benefit.setOrigine("FTP");
				
				PTaxable_Benefit.save( );
				
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"Create_Payment_Taxable_Benefit - " + sql , e);
		}
	
			
		
		
		
	}
*/	
	
	private void Validation_Client( Properties ctx, P_Payment Payment, P_Employee Employee, String trxName )
	{
		
		//2023-11-27 si le salaire brut dépasse de 40% le salaire annuel
		BigDecimal SalaireByPay = Payment.getAnnualSalary().divide( new BigDecimal(Frequency.getNumberOfPeriod()),2,BigDecimal.ROUND_HALF_UP ).multiply(new BigDecimal(1.4) );
		// exclus les camionneurs. et les distributeur.
		if ( Payment.getGrossEarnings().compareTo( SalaireByPay ) > 0  
				&& Employee.getP_Collective_Labour_Agr_ID() != 1000006 
				&& Employee.getP_Collective_Labour_Agr_ID() != 1000007 
				&&	! Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_ExpenseAccount ) ) 
		{
            P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Warning, "Le montant brut de la paie est plus élever de 40% du salaire prévue. ", trxName);
		}

		
		// Alter nombre maximum de lunch a 5 par semaine.
		String sql = "SELECT P_TIME_SHEET.value, sum( P_TIME_SHEET_DETAIL.DAYQTY ) DAYQTY FROM P_TIME_SHEET "
				+ "INNER JOIN P_TIME_SHEET_DETAIL ON P_TIME_SHEET_DETAIL.P_TIME_SHEET_ID = P_TIME_SHEET.P_TIME_SHEET_ID "
				+ "INNER JOIN P_GAIN ON P_GAIN.P_GAIN_ID = P_TIME_SHEET_DETAIL.P_GAIN_ID "
//				+ "INNER JOIN P_EMPLOYEE ON P_EMPLOYEE.P_EMPLOYEE_ID = P_TIME_SHEET.P_EMPLOYEE_ID "
				+ "WHERE P_GAIN.VALUE = 'GL07' "
				+ " AND P_TIME_SHEET.P_PAYMENT_ID = " + Payment.getP_Payment_ID()
				+ "group by P_TIME_SHEET.value "
				+ "having sum( P_TIME_SHEET_DETAIL.DAYQTY ) > 5 "
				;
		
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (sql, trxName);

		try
		{
			ResultSet rs = pstmt.executeQuery ();
			
			if (rs.next ())
			{
	            P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Warning, "Le nombre de repas dépasse le maximum par semaine prévus (5) ", trxName);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE," CalculNet - Validation_Client - " + sql, e);

		}
		
		String sql2 = "SELECT P_EMPLOYEE.value, P_EMPLOYEE.name, P_JOB_TYPE.value, P_JOB_TYPE.name, [dbo].[getAgeEmployee]( P_EMPLOYEE.P_EMPLOYEE_ID ) "
				+ " FROM P_EMPLOYEE "
				+ " INNER JOIN P_COLLECTIVE_LABOUR_AGR on P_COLLECTIVE_LABOUR_AGR.P_COLLECTIVE_LABOUR_AGR_ID = P_EMPLOYEE.P_COLLECTIVE_LABOUR_AGR_ID "
				+ " INNER JOIN P_JOB_TYPE on P_JOB_TYPE.P_JOB_TYPE_ID = P_EMPLOYEE.P_JOB_TYPE_ID "
				+ " WHERE P_COLLECTIVE_LABOUR_AGR.VALUE = '8' "
				+ " AND NOT EXISTS ( SELECT 1 FROM P_EMPLOYEE_DEDUCTION INNER JOIN P_DEDUCTION ON P_DEDUCTION.P_DEDUCTION_ID = P_EMPLOYEE_DEDUCTION.P_DEDUCTION_ID "
				+ "				  WHERE P_EMPLOYEE_DEDUCTION.P_EMPLOYEE_ID = P_EMPLOYEE.P_EMPLOYEE_ID AND P_EMPLOYEE_DEDUCTION.ISACTIVE = 'Y' AND ( P_EMPLOYEE_DEDUCTION.EFFECTTO IS NULL OR  P_EMPLOYEE_DEDUCTION.EFFECTTO > getdate() ) "
				+ "				  AND P_DEDUCTION.VALUE IN ( 'DC07', 'DC07RP') "
				+ ") "
				+ " AND P_EMPLOYEE.ISACTIVE = 'Y' "
				+ " AND  [dbo].[getAgeEmployee]( P_EMPLOYEE.P_EMPLOYEE_ID ) < 70 "
				+ " AND P_JOB_TYPE.value = 'PER'" 
			    + " AND P_EMPLOYEE.P_EMPLOYEE_ID = " + Employee.getP_Employee_ID()
				;

		pstmt = DB.prepareStatement (sql2, trxName);

		try
		{
			ResultSet rs = pstmt.executeQuery ();
			
			if (rs.next ())
			{
	            P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Warning, "L'employé n'a pas la déduction DC07 ", trxName);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE," CalculNet - Validation_Client - " + sql2, e);

		}
		
		String sql3 = "select p_payment.value from p_payment where GROSSEARNINGS = 0 and exists( select 1 from P_Payment_Gain where P_Payment_Gain.p_payment_ID = p_payment.p_payment_ID "
				+ "and p_payment_gain.ORIGINTIME <> 'ALD' "
				+ "and P_Gain_ID not in ( select p_gain_id from p_gain where P_Method_Gain_ID IN ( 104, 107) )) "
				+ "and P_payment.PAYMENTTYPEDOC not in ( 'Adjustement') "
				+ "and p_payment.p_payment_Id = " + Payment.getP_Payment_ID()
				;
		pstmt = DB.prepareStatement (sql3, trxName);

		try
		{
			ResultSet rs = pstmt.executeQuery ();
			
			if (rs.next ())
			{
		           P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "Le paiement a un brut a 0 vérifier si c'est normal ", trxName);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE," CalculNet - Validation_Client - " + sql3, e);
		}
		
	}
	
	//
	// Calcul taxable benefit
	//
	private void calcul_taxable_benefit ( ) throws Exception
	{
		//2013.02.22 Ajoute les avantages imposable qui serait manquant au dossier employés.
		createMissingTaxableBenefit();
		
		BigDecimal BenefitAmount = ZERO;
		BigDecimal BenefitAmountProv = ZERO;
		//
		// Loop on Employee taxable benefit  
		//
		String query = null;
		query = "SELECT P_Employee_Taxable_Benefit.P_Taxable_Benefit_ID, P_Employee_Taxable_Benefit.P_Employee_Taxable_Benefit_ID, P_Employee_Taxable_Benefit.P_Employee_ID "
			+ "FROM P_Employee_Taxable_Benefit, P_Taxable_Benefit "
			+ "WHERE P_Employee_Taxable_Benefit.P_Taxable_Benefit_ID = P_Taxable_Benefit.P_Taxable_Benefit_ID "
			+ "  AND P_Taxable_Benefit.IsActive='Y'" 
			+ "  AND P_Employee_Taxable_Benefit.IsActive='Y'" 
			+ "  AND P_Employee_Taxable_Benefit.P_Employee_ID=" + this.Payment.getP_Employee_ID ()
			+ "  AND P_Employee_Taxable_Benefit.EffectIn in ( Select Max( EffectIn) FROM P_Employee_Taxable_Benefit dat WHERE dat.P_Taxable_Benefit_ID = P_Taxable_Benefit.P_Taxable_Benefit_ID and dat.P_Employee_ID = P_Employee_Taxable_Benefit.P_Employee_ID and dat.EffectIn <= " + DB.TO_DATE(Period.getEndDate() ) + ")"
			+ "  AND ( P_Employee_Taxable_Benefit.EffectTo is null OR P_Employee_Taxable_Benefit.EffectTo >= " + DB.TO_DATE( Period.getStartDate() ) +"  )"
//+ " AND P_Taxable_Benefit.isprovincial != 'Y'"
									//2013.02.01
			//			+ " ORDER BY P_Taxable_Benefit.Value"
			+ " ORDER BY P_Taxable_Benefit.isprovincial, P_Taxable_Benefit.Value"
			;
		
		//		Log.trace( 5,"calcul_taxable_benefit - query=" + query);
		PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);

			ResultSet rs = pstmt.executeQuery ();
			P_Employee Employee;
			P_Taxable_Benefit Taxable_Benefit;
			P_Employee_Taxable_Benefit EmployeeTaxableBenefit;
			
			while (rs.next ())
			{
				//
				// Method 001 - Rate of the amount of the employer part
				//
				
				Taxable_Benefit = P_Taxable_Benefit.get( Env.getCtx (),  rs.getInt("P_Taxable_Benefit_ID"), trxName );
				
				
				Employee = P_Employee.get( Env.getCtx(), rs.getInt("P_Employee_ID"), trxName);
				
				Timestamp l_EffectIn = Period.getStartDate();
				if ( Employee.getDateHired().compareTo( Period.getStartDate() ) > 0)
					l_EffectIn = Employee.getDateHired();

				EmployeeTaxableBenefit = P_Employee_Taxable_Benefit.get( Env.getCtx (),  Employee.getP_Employee_ID(), Taxable_Benefit.getP_Taxable_Benefit_ID(),  Period.getEndDate() ,trxName );
				if ( EmployeeTaxableBenefit.getP_Employee_Taxable_Benefit_ID() <= 0)
				{
					EmployeeTaxableBenefit.setP_Employee_ID(Payment.getP_Employee_ID());
					EmployeeTaxableBenefit.setP_Taxable_Benefit_ID( Taxable_Benefit.getP_Taxable_Benefit_ID());
					EmployeeTaxableBenefit.setIsActive(true);
					EmployeeTaxableBenefit.setAD_Org_ID(Payment.getAD_Org_ID());
					EmployeeTaxableBenefit.setEffectIn(l_EffectIn);
					EmployeeTaxableBenefit.save();
					P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol09-I") + Taxable_Benefit.getValue(), trxName);
//					P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "L'avantage imposable " + Taxable_Benefit.getValue() + " a été ajouté au dossier de l'employé ", trxName);
				}

				boolean isDeduction = false;
				boolean calcul = true;
				
				//Taxable_Benefit.IsProvincial()
				//2008-11-18 validation de la province si elle est spéficier.
				if ( EmployeeTaxableBenefit.isActive() && ( Taxable_Benefit.getC_Region_ID() == 0 || ( Taxable_Benefit.getC_Region_ID() != 0 && RegionResidence.getC_Region_ID() == Taxable_Benefit.getC_Region_ID() ) ))
				{

					BenefitAmount = ZERO;
					BenefitAmountProv = ZERO;
					if ( EmployeeTaxableBenefit.getTaxB_Fixed_Amount() != null && EmployeeTaxableBenefit.getTaxB_Fixed_Amount().compareTo( ZERO ) != 0 )
						BenefitAmount = EmployeeTaxableBenefit.getTaxB_Fixed_Amount();
					else if ( Taxable_Benefit.getTaxB_Fixed_Amount() != null && Taxable_Benefit.getTaxB_Fixed_Amount().compareTo( ZERO ) != 0 )
						BenefitAmount = Taxable_Benefit.getTaxB_Fixed_Amount();
					else 
					{
/*						Integer key = new Integer ( Payment.getP_Payment_ID() );
						s_cacheEmployerInsurance.remove(key);
						s_cacheEmployeeInsurance.remove(key);
*/						
						
						// 2013.02.01 change de place
						isDeduction = false;
						PreparedStatement pstmt_ded = null;
							String query_ded = null;
							query_ded = "SELECT  P_Deduction.value, P_Deduction.name, P_Deduction.P_TAXABLE_BENEFIT_ID, P_Method_Deduction.Priority, P_Deduction.P_Deduction_ID, P_Deduction_Param.P_Deduction_Param_ID, P_Employee_Deduction.P_Employee_Deduction_ID, IsEmployerWhenZero "
								+ "FROM P_Deduction"
								+ " INNER JOIN P_Employee_Deduction ON P_Employee_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID"
								+ " INNER JOIN P_Deduction_Param ON P_Deduction_Param.P_Deduction_ID = P_Deduction.P_Deduction_ID "
								+ " INNER JOIN P_Method_Deduction ON P_Method_Deduction.P_Method_Deduction_ID = P_Deduction_Param.P_Method_Deduction_ID "
								+ "WHERE ( P_Deduction.P_Taxable_Benefit_ID = " + EmployeeTaxableBenefit.getP_Taxable_Benefit_ID() + " OR  P_Deduction.P_Taxable_Benefit_Provincial_ID = " + EmployeeTaxableBenefit.getP_Taxable_Benefit_ID() + " ) "
								+ "  AND P_Deduction.IsActive='Y'" 
								+ "  AND P_Employee_Deduction.P_Employee_ID=" + this.Payment.getP_Employee_ID ()
								+ "  AND ( P_Employee_Deduction.EffectTo is null OR P_Employee_Deduction.EffectTo >= " + DB.TO_DATE( Period.getStartDate() ) +"  )"
								+ "  AND P_Employee_Deduction.EffectIn in ( Select Max( EffectIn) FROM P_Employee_Deduction dat WHERE dat.P_Deduction_ID = P_Deduction.P_Deduction_ID and dat.P_Employee_ID = P_Employee_Deduction.P_Employee_ID and dat.EffectIn <= " + DB.TO_DATE(Period.getEndDate() ) + ")"
								+ "  AND P_Deduction_Param.EffectIn in ( Select Max( EffectIn) FROM P_Deduction_PARAM WHERE P_Deduction_PARAM.P_Deduction_ID = P_Deduction.P_Deduction_ID and P_Deduction_PARAM.EffectIn <= " + DB.TO_DATE(Period.getEndDate() ) + ")"
								+ " AND P_Deduction.P_Deduction_Family_ID <> 1000001 "

								;

							pstmt_ded = DB.prepareStatement (query_ded, trxName);
							ResultSet rs_ded = pstmt_ded.executeQuery ();
							while (rs_ded.next ())
							{
								isDeduction = true;
								this.salaryAdmissible = ZERO;
								this.hoursAdmissible = ZERO;
								this.employeeAmount = ZERO;
								this.employerAmount = ZERO;

								EmployeeDeduction = P_Employee_Deduction.get( Env.getCtx (), rs_ded.getInt("P_Employee_Deduction_ID"), trxName);
								Deduction         = P_Deduction.get( Env.getCtx(), EmployeeDeduction.getP_Deduction_ID(), trxName);
								Deduction_Param   = P_Deduction_Param.get( Env.getCtx(), rs_ded.getInt("P_Deduction_Param_ID") , trxName);

				                //
								Method_Deduction  = P_Method_Deduction.get( Env.getCtx (), Deduction_Param.getP_Method_Deduction_ID() , trxName);
								calcul = isDeductionCalcul( Deduction, Method_Deduction, EmployeeDeduction );
//								calcul = isDeductionCalcul( Deduction, Method_Deduction, EmployeeDeduction );
								
								if (  EmployeeDeduction.isActive() && Deduction_Param.isActive() && calcul )
								{
									//
									// Condition pour amélioré la performance, cela ne donne rien de calculer les déductions sans part employeurs 
									//
									//2022-09-05 exection pour les assurances
									// Deduction.getP_Deduction_Family_ID() == 1000001 ||
									if (  Deduction.getP_Deduction_Family_ID() == 1000009 || EmployeeDeduction.getEmployerRate(Deduction_Param ).compareTo( ZERO ) != 0 || EmployeeDeduction.getEmployerAmount(Deduction_Param, Period.getP_Period_ID() ).compareTo( ZERO ) != 0)
									{
										_calcul_deduction( true );
										
										BenefitAmount = BenefitAmount.add( this.employerAmount );	

										/* 2025-04-01 déplacer après validation */
										
										if ( Payment.getGrossEarnings().compareTo( ZERO) >= 0 )
										{
											if ( NetPay.subtract( this.employeeAmount ).compareTo( ZERO) < 0  )
											{
												this.employeeAmount = NetPay ;
												
												// Si la part employé est à 0 il n'y a pas de part employeur
												// Ex : FTQ et Fond de pension.
												//
												if ( rs_ded.getString("IsEmployerWhenZero").equals("N") )
												{
													if ( this.employeeAmount == null || this.employeeAmount.compareTo(Env.ZERO) == 0)
														this.employerAmount = Env.ZERO;
												}

											}
										}
									}
								}
							}
							rs_ded.close ();
							pstmt_ded.close ();
							pstmt_ded = null;
					}


					if ( BenefitAmount.compareTo(Env.ZERO) == 0 && BenefitAmountProv.compareTo(Env.ZERO) == 0 &&  Payment.getPaymentTypeDoc().compareTo(P_Payment.PAYMENTTYPEDOC_Regular) != 0 )
						calcul = false;

//					if ( BenefitAmount.compareTo(Env.ZERO) == 0 && BenefitAmountProv.compareTo(Env.ZERO) == 0 & Payment.getP_Period_ID() != Period.getP_Period_ID () & Taxable_Benefit.getIsProvincial().equals( P_Taxable_Benefit.ISPROVINCIAL_Provincial))
//						calcul = false;

					if ( EmployeeTaxableBenefit.getTaxB_Rate() != null && EmployeeTaxableBenefit.getTaxB_Rate().compareTo( ZERO ) != 0 )
						BenefitAmount = BenefitAmount.multiply( EmployeeTaxableBenefit.getTaxB_Rate() );

					//2011.01.11
					// 2013.01.31 add isDeduction.
//					if ( calcul )							
					if ( calcul && isDeduction == true )
					{
						BenefitAmount = Valide_amount( true, BenefitAmount, EmployeeTaxableBenefit );
						
						// 2023-11-21 par d'avantage imposable négative.
						if (  BenefitAmount.compareTo(Env.ZERO ) < 0 && Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_Complementary) )
						{
							BenefitAmount = Env.ZERO;
						}
						
					}
					else if ( calcul && isDeduction == false && EmployeeTaxableBenefit.getTaxB_Fixed_Amount() != null && EmployeeTaxableBenefit.getTaxB_Fixed_Amount().compareTo(Env.ZERO) != 0 )

					{
						BenefitAmount = Valide_amount_AVI( true, BenefitAmount, EmployeeTaxableBenefit );
					}

					//+ 2012.02.21
					Boolean isException = false;
					// S'il existe une exception pour la période
					BigDecimal TaxableBenefitAmountExcep = Taxable_Benefits_Excep( EmployeeTaxableBenefit, BenefitAmount );
					if ( TaxableBenefitAmountExcep.compareTo( ZERO ) != 0)
					{
						BenefitAmount = TaxableBenefitAmountExcep;
						isException = true;
					}
					//- 2012.02.21
					
/*
 * 2025-06-17 enlerver cette partie					
					//+ Calcul par Période d'origine
					if ( ! isDeduction || isException )
					{
						BigDecimal cumBenefitAmount = ZERO;

						cumBenefitAmount = get_TaxableBenefit_cumulative_per( EmployeeTaxableBenefit  );
						BenefitAmount  = BenefitAmount.subtract( cumBenefitAmount );
						BenefitAmount = BenefitAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
					}
*/

					//Log.trace( 5, EmployeeDeduction.getValue() + " - " + Taxable_Benefit.getName() + " Rate " + EmployeeTaxableBenefit.getTaxB_Rate() + " Amount " + BenefitAmount + " fixed amount " + EmployeeTaxableBenefit.getTaxB_Fixed_Amount() );

					BenefitAmount = BenefitAmount.setScale(2, BigDecimal.ROUND_UP);
					
					// create a new Taxable Benefit
					if ( BenefitAmount != null && BenefitAmount.compareTo( ZERO ) != 0  )
					{
//						P_Payment_Taxable_Benefit  PTaxable_Benefit = new P_Payment_Taxable_Benefit( Env.getCtx (),  -1, trxName );
						P_Payment_Taxable_Benefit PTaxable_Benefit = P_Payment_Taxable_Benefit.get( Env.getCtx (), Payment.getP_Payment_ID (), Taxable_Benefit.getP_Taxable_Benefit_ID(), Period.getP_Period_ID () , trxName );
						PTaxable_Benefit.setP_Employee_ID( Payment.getP_Employee_ID ());
//						PTaxable_Benefit.setP_Taxable_Benefit_ID( Taxable_Benefit.getP_Taxable_Benefit_ID() );

						PTaxable_Benefit.setP_Taxable_Benefit_ID( rs.getInt("P_Taxable_Benefit_ID") );
						PTaxable_Benefit.setP_Year_ID(  Period.getP_Year_ID () );
						PTaxable_Benefit.setP_Period_ID( Period.getP_Period_ID () );
						PTaxable_Benefit.setP_Payment_ID( Payment.getP_Payment_ID ());

						if ( PTaxable_Benefit.getTaxable_Benefit_Amount() != null )
							PTaxable_Benefit.setTaxable_Benefit_Amount( PTaxable_Benefit.getTaxable_Benefit_Amount().add(BenefitAmount) );
						else
							PTaxable_Benefit.setTaxable_Benefit_Amount( BenefitAmount  );
//						PTaxable_Benefit.setTaxable_Benefit_Amount( BenefitAmount.setScale(2, BigDecimal.ROUND_HALF_UP)  );
						PTaxable_Benefit.setP_Employee_Taxable_Benefit_ID( EmployeeTaxableBenefit.getP_Employee_Taxable_Benefit_ID() );
						PTaxable_Benefit.setOrigine("FTP");
						
						PTaxable_Benefit.save( );
					}

				}
				    
			}


			rs.close ();
			pstmt.close ();
			pstmt = null;

		return ;
		
	}   // calcul_taxable_benefit

	//
	// Internal procedure for calcul deduction and taxable benefit.
	//
	private void _calcul_deduction ( boolean taxable_Benefit) throws Exception
	{
		this.hoursAdmissible  = ZERO;
		this.salaryAdmissible = ZERO;
		this.employeeAmount   = ZERO;
		this.employerAmount   = ZERO;

		this.employeeAccumulationAmount = ZERO;
		this.employerAccumulationAmount = ZERO;

		String    methodCalcul     = Method_Deduction.getValue();

		int       i_methodCalcul   = new Integer( methodCalcul ).intValue();

		if ( ! EmployeeDeduction.isActive() )
		{
           return;		    
		}

		log.info( "Calcul Deduction : " + Deduction.getValue() + " - " + Deduction.getName() ) ;		

		if ( EmployeeDeduction.isExonerated() )
		{
			log.info( "Calcul Deduction Exonerated : " + Deduction.getValue() + " - " + Deduction.getName() ) ;		

           return;		    
		}
		
		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName()  + " method - "   + methodCalcul  + " column - "   + Deduction_Param.getP_Column_Adm_ID()  );
//		Deduction = P_Deduction.get( Env.getCtx(), Deduction_Param.getP_Deduction_ID(), null);
		
///		this.employeeAmount = EmployeeDeduction.getEmployeeAmount( Period.getStartDate());
//		this.employerAmount = EmployeeDeduction.getEmployerAmount( Period.getStartDate());

		/*if ( Deduction_Param.getEmployeeAmount() != null )
			  this.employeeAmount = Deduction_Param.getEmployeeAmount();
		if ( Deduction_Param.getEmployerAmount() != null )
			  this.employerAmount = Deduction_Param.getEmployerAmount();
			*/
		switch ( i_methodCalcul ) 
		{
			case 2:
						calcul_rrq_rpc( true );  			// RRQ
			break;
			case 3: 
						calcul_rrq_rpc( false);			// RPC
			break;
			
			case 92:
				calcul_rrq_rpc2( true );  			// RRQ
				break;
			case 93: 
				calcul_rrq_rpc2( false);			// RPC
				break;
			
			case 4: 	calcul_employment_insurance(  );// Employment insurance 
			break;
			case 5: 	calcul_pension_fund (  ); // pension Fund  
			break;
			case 27: 	calcul_pension_fund (  ); // pension Fund sans arrérage.
			break;
			case 7: // FTQ
						calcul_deduction_others( );
			break;
			
			case 8: 	calcul_syndicat (  ); // Syndicat
			break;
			case 9:
				if ( ! this.Employee.isStatusIndian() )		// 2009.04.27
				{
					if ( Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_SeparationPay) && Payment.getTaxation_Region_ID() != QUEBEC 
							) 	
						calcul_federal_tax.getInclusivePayment_Fed();
					else
						calcul_federal_tax.calcul_federal_tax( taxID, null);
					
				}

			break;
			case 10: 
				if ( ! this.Employee.isStatusIndian() )		// 2009.04.27
				{
					calcul_federal_tax.calcul_federal_retro_tax( Env.getCtx(), this, taxID, trxName); // Federal Tax gratification 
				}
				
			break;
			case 11: 
				if ( ! this.Employee.isStatusIndian() )		// 2009.04.27
				{
					if ( Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_SeparationPay))
					{
						if (Region.getC_Region_ID() == QUEBEC )
						{
							Calcul_provincial_tax provincial_tax = new Calcul_provincial_tax(  );
							provincial_tax.getInclusivePayment_Qc(this);
						}
							
					}
					else if ( Region.getC_Region_ID() == QUEBEC  || this.RegionResidence.getC_Region_ID() == QUEBEC)  
					{
						Calcul_provincial_tax provincial_tax = new Calcul_provincial_tax(  );
						provincial_tax.calcul_quebec_tax( Env.getCtx(), this, TimeSheetPeriod.getPayDate(), trxName, false, Env.ZERO); // Provincial Tax
					}
					
				}
					
			break;
			case 12:
				if ( ! this.Employee.isStatusIndian() )		// 2009.04.27
				{
					if ( Region.getC_Region_ID() == QUEBEC )
					{
						Calcul_provincial_tax provincial_tax = new Calcul_provincial_tax( );
						provincial_tax.calcul_quebec_retro_tax( Env.getCtx(), this, Payment, Employee, Period, Deduction_Param, TimeSheetPeriod.getPayDate(), trxName); // Provincial Tax gratification
						
					}
//					else
//						calcul_federal_tax.calcul_Provincial( taxID);
				}
					
			break;
			case 13:    calcul_saisie( );
			break;
			case 14:    calcul_insurance( );
			break;
			case 32:    calcul_insurance_M32();
			break;
			case 35:    calcul_insurance_M35();
			break;
			case 38:    calcul_insurance_M38();
			break;
			// Invalité courte durée
			case 80:    calcul_insurance_M80();
			break;
			case 81:    calcul_insurance_M81( taxable_Benefit );
			break;
			case 82:    calcul_insurance_M82();
			break;
			case 83:    calcul_insurance_M83();
			break;
			// Identique a 81 mais priorité de calcul différente.
			case 84:    calcul_insurance_M81( taxable_Benefit );
			break;

			
			case 15:    calcul_insurance_age_bound();
			break;
			case 17:    Advance_Recovery_Part1();
			break;
			case 29:  // Deduction Mensuel.
				Timestamp DateOfMonth = null;
				int nbrDay = 0 - (TimeUtil.getDaysBetween( Period.getStartDate(), Period.getEndDate()  ) +1);
				if ( Deduction_Param.getDeductionFrequency() != null )
				{
					if ( Deduction_Param.getDeductionFrequency().equals( P_Deduction_Param.DEDUCTIONFREQUENCY_1OfTheMonth))
						DateOfMonth = TimeUtil.getDay( TimeUtil.getYear( Period.getPayDate()), TimeUtil.getMonth(Period.getPayDate()),01 );
//					DateOfMonth = TimeUtil.getDay( Period.getPayDate().getYear()+1900, Period.getPayDate().getMonth()+1,01 );

					if ( Deduction_Param.getDeductionFrequency().equals( P_Deduction_Param.DEDUCTIONFREQUENCY_15OfTheMonth))
						DateOfMonth = TimeUtil.getDay( TimeUtil.getYear( Period.getPayDate()), TimeUtil.getMonth(Period.getPayDate()),15 );
						
//					DateOfMonth = TimeUtil.getDay( Period.getPayDate().getYear()+1900, Period.getPayDate().getMonth()+1,15 );

					if ( Deduction_Param.getDeductionFrequency().equals( P_Deduction_Param.DEDUCTIONFREQUENCY_EndOfTheMonth))
						DateOfMonth = TimeUtil.getMonthLastDay( Period.getPayDate());

				
					if ( DateOfMonth.compareTo( Period.getPayDate()) <= 0 && 
					  	 DateOfMonth.compareTo( TimeUtil.addDays( Period.getPayDate(), nbrDay)) > 0 )
					{
						calcul_deduction_others( );
					}	
					
				}
			break;
			case 30: 	calcul_pension_fund_RRF_RRE();
			break;
			case 31: 	calcul_rqap();
			break;
			// Long-Term Disability Insurance
			case 33:    calcul_insurance_LTD( );
			break;
			case 36:    calcul_EUPP( false);
			break;
			case 37:    calcul_EUPP( true);
			break;
			// 2009.02.26
			case 40:    calcul_nunavut_nwt();
			break;
			case 41:    calcul_saisie_percentage();
			break;
			case 70: 	calcul_deduction_70( );
			break;
			case 71: 	calcul_deduction_Working_Conditions( );
			break;
			default :   calcul_deduction_others( );
		}

		isExonerated = false;
		//
		// Gestion des Exception et Exonération. Il y a maintenant 2 tables pour la gestion
		// la table employé que l'usager peut définir et la table sous le payment que 
		// le system aliment a partir des profiles absences long durée (ALD )
		//
//		if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "Environnement").equals("Test"))
		{
			if ( Deduction.getP_Deduction_Family_ID() != 1000001 )
				Employee_Excep();
			
		}
//		else {
//			Employee_Excep();
//		}
		Payment_Excep();

		
	
	}   // calcul_calcul_deduction


	private BigDecimal cumEmpAmount = ZERO;
	private BigDecimal cumEmprAmount = ZERO;
	private BigDecimal cumSalAdmissible = ZERO;
	private BigDecimal cumHrsAdmissible  = ZERO;

	
	private boolean isDeductionCalcul( P_Deduction Deduction, P_Method_Deduction Method_Deduction, P_Employee_Deduction EmployeeDeduction) throws Exception
	{
		
		boolean calcul = true;
/*		
		if (    Employee.getValue().equals("001751") ||
				Employee.getValue().equals("001457") ||
				Employee.getValue().equals("002131") ||
				Employee.getValue().equals("002126") ||
				Employee.getValue().equals("002798") ||
				Employee.getValue().equals("002898") ||
				Employee.getValue().equals("001226") ||
				Employee.getValue().equals("002450") ||
				Employee.getValue().equals("002521") ||
				Employee.getValue().equals("002821") ||
				Employee.getValue().equals("002408") ||
				Employee.getValue().equals("002217") ||
				Employee.getValue().equals("002807") ||
				Employee.getValue().equals("001858") ||
				Employee.getValue().equals("002678") ||
				Employee.getValue().equals("002799") ||
				Employee.getValue().equals("001857")
				)
		{
			// 2019-03-30 Juste pour 17 employés
			if (  this.Period.getP_Period_ID () < Payment.getP_Period_ID() &&  (  Deduction.getValue().equals( "DA20") == false && Deduction.getValue().equals( "DA22") == false && Deduction.getValue().equals( "DA06") == false ) )
			{
				calcul = false;
			}
		}
*/		
        // Si la déduction est pour un groupe de paiement, l'employé doit être de ce group de paiement pour que l'on calcul
        // la déduction.
		//+ 2011.09.16 Ne pas calcule les deductions obligatoire en raison de la province de residence
		//             sauf s'il y a une exception.
//	    + "  exists ( select 1 from P_Employee_Deduction_Excep where P_Employee_Deduction_Excep.P_Employee_Deduction_ID = P_Employee_Deduction.P_Employee_Deduction_ID  and P_Employee_Deduction_Excep.P_Period_ID = " + Period.getP_Period_ID() + " ) ))"
		// s'il n'y a pas d'exception
		P_Employee_Deduction_Excep deductionExcep = null;
		if ( EmployeeDeduction != null )
			deductionExcep = P_Employee_Deduction_Excep.get( Env.getCtx (),  EmployeeDeduction.getP_Employee_Deduction_ID(), Period.getP_Period_ID(), trxName );

		if ( deductionExcep == null)
		{
			if ( Deduction.getP_Payment_Group_ID() != 0 && Deduction.getP_Payment_Group_ID() != Employee.getP_Payment_Group_ID()  )
				calcul = false;
			if ( Deduction.getResidence_Region_ID() != 0 && Deduction.getResidence_Region_ID() != this.RegionResidence.getC_Region_ID() && Deduction.getResidence_Region_ID() != this.Workplace.getC_Region_ID())
				calcul = false;
			
			if ( Deduction.getC_Region_ID() != 0 && Deduction.getC_Region_ID() != this.Region.getC_Region_ID() )
				calcul = false;
			
//			if ( Deduction.getP_Collective_Labour_Agr_ID() != 0 && Deduction.getP_Collective_Labour_Agr_ID() != Employee.getP_Collective_Labour_Agr_ID() )
//				calcul = false;
		} 	

//		ici
/*
		if ( EmployeeDeduction != null && EmployeeDeduction.isActive() == false && Deduction.isDeductionMandatory() )
		{
			P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, " L'employé a la déduction (" + Deduction.getValue() + " " + Deduction.getTrlName() + ") obligatoire inactive a sont dossier, cette déduction ne sera pas calculé." , trxName );
		}
*/
		//- 2011.09.16		    

        //
/*		if (PgiUtil.getSolsticeParameter(Env.getCtx(), "Client").toUpperCase().equals("SIQ")  || (PgiUtil.getSolsticeParameter(Env.getCtx(), "Client").toUpperCase().equals("SOQUIJ")))
		{
			if ( Deduction.getP_Deduction_Family_ID() == 1000001 &&  this.Period.getP_Period_ID () < Payment.getP_Period_ID() )
			{
				calcul = false;
			}
		}
*/
		// Temporaire pour le sysdicat en début d'implantation
		if ( Deduction.getValue().equals("DD03" )  &&  this.Period.getP_Period_ID () < Payment.getP_Period_ID() )
		{
			calcul = false;
		}

		
		String    methodCalcul     = Method_Deduction.getValue();
		int       i_methodCalcul   = new Integer( methodCalcul ).intValue();

		// RPC
		if ( Region.getC_Region_ID() == QUEBEC  && i_methodCalcul == 3 )  
			calcul = false;

		if ( Region.getC_Region_ID() == QUEBEC  && i_methodCalcul == 93 )  
			calcul = false;
		
/*
		// RRQ
		if ( Deduction.getC_Region_ID() != QUEBEC && RegionResidence.getC_Region_ID() != QUEBEC && i_methodCalcul == 2 )
			calcul = false;
		
		if ( Deduction.getC_Region_ID() == QUEBEC && Region.getC_Region_ID() != QUEBEC && RegionResidence.getC_Region_ID() != QUEBEC )  
			calcul = false;

		// CSST et WCB
		// On ne calcul pas de CSST pour les gens qui habite le Québec mais donc la province d'imposition est différent du Québec
		if ( Deduction.getC_Region_ID() == QUEBEC && i_methodCalcul == 20 && Region.getC_Region_ID() != QUEBEC  && RegionResidence.getC_Region_ID() == QUEBEC  )
			calcul = false;

		
		if ( i_methodCalcul == 20 && Deduction.getC_Region_ID() != 0 && Deduction.getC_Region_ID() != Region.getC_Region_ID()  )
			calcul = false;

		//
		// EHT et Exchequer 
		//
		if ( i_methodCalcul == 18 && Deduction.getC_Region_ID() != 0 && Deduction.getC_Region_ID() != Region.getC_Region_ID()  )
			calcul = false;
*/
		
		return calcul;
	}

	private boolean isDeductionCalculOld( P_Deduction Deduction, P_Method_Deduction Method_Deduction) throws Exception
	{
		
		boolean calcul = true;
		
        // C.S.S.T.   Whapchiwem
        // Si la déduction est pour un groupe de paiement, l'employé doit être de ce group de paiement pour que l'on calcul
        // la déduction.
		if ( Deduction.getP_Payment_Group_ID() != 0 && Deduction.getP_Payment_Group_ID() != Employee.getP_Payment_Group_ID()  )
			calcul = false;
		
        //
        //	SIQ On ne réévalue pas les deductions d'assurance en période d'orgine. S'il y a déjà un montant
/*		if ( Env.getAD_Client_ID(Env.getCtx()) == 11)
		{
			if ( Deduction.getP_Deduction_Family_ID() == 1000001 &&  this.Period.getP_Period_ID () < Payment.getP_Period_ID() )
			{
				calcul = false;
			}
		}
*/
		String    methodCalcul     = Method_Deduction.getValue();
		int       i_methodCalcul   = new Integer( methodCalcul ).intValue();

		// RPC
		if ( Region.getC_Region_ID() == QUEBEC  && i_methodCalcul == 3 )  
			calcul = false;
/*
		// RRQ
		if ( Deduction.getC_Region_ID() != QUEBEC && RegionResidence.getC_Region_ID() != QUEBEC && i_methodCalcul == 2 )
			calcul = false;
		
		if ( Deduction.getC_Region_ID() == QUEBEC && Region.getC_Region_ID() != QUEBEC && RegionResidence.getC_Region_ID() != QUEBEC )  
			calcul = false;

		// CSST et WCB
		// On ne calcul pas de CSST pour les gens qui habite le Québec mais donc la province d'imposition est différent du Québec
		if ( Deduction.getC_Region_ID() == QUEBEC && i_methodCalcul == 20 && Region.getC_Region_ID() != QUEBEC  && RegionResidence.getC_Region_ID() == QUEBEC  )
			calcul = false;

		
		if ( i_methodCalcul == 20 && Deduction.getC_Region_ID() != 0 && Deduction.getC_Region_ID() != Region.getC_Region_ID()  )
			calcul = false;

		//
		// EHT et Exchequer 
		//
		if ( i_methodCalcul == 18 && Deduction.getC_Region_ID() != 0 && Deduction.getC_Region_ID() != Region.getC_Region_ID()  )
			calcul = false;
*/
		
		return calcul;
	}

	private BigDecimal Valide_amount_AVI( boolean taxable_Benefit, BigDecimal Amount, P_Employee_Taxable_Benefit EmployeeTaxableBenefit ) throws Exception
	{
		
			BigDecimal bd = Amount;
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
			//+ Calcul par Période d'origine
			BigDecimal cumBenefitAmount = ZERO;

			cumBenefitAmount = get_TaxableBenefit_cumulative_per( EmployeeTaxableBenefit  );
			bd  = bd.subtract( cumBenefitAmount );
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);

			
			// Si la paye est pour être inférieur a 0. Le montant de la déduction égal le montant de la paie.
			return bd;
	}

	
	private BigDecimal Valide_amount( boolean taxable_Benefit, BigDecimal Amount, P_Employee_Taxable_Benefit EmployeeTaxableBenefit ) throws Exception
	{
		
		if ( taxable_Benefit )
		{
			BigDecimal bd = Amount;
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
			//+ Calcul par Période d'origine
			BigDecimal cumBenefitAmount = ZERO;

			cumBenefitAmount = get_TaxableBenefit_cumulative_per( EmployeeTaxableBenefit  );
			bd  = bd.subtract( cumBenefitAmount );
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);

//			if ( bd.compareTo(Env.ZERO) < 0)
//				bd = Env.ZERO;
			
			this.employerAmount = bd;
			bd = get_deduction_max_employeur( bd, null );
			
			// Si la paye est pour être inférieur a 0. Le montant de la déduction égal le montant de la paie.
			return bd;
		}
		else
		{
			this.employeeAmount   = this.employeeAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
			this.employerAmount   = this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
			this.salaryAdmissible = this.salaryAdmissible.setScale(2,BigDecimal.ROUND_HALF_UP);
			this.hoursAdmissible  = this.hoursAdmissible.setScale(2,BigDecimal.ROUND_HALF_UP);

			//On vérifie si les montants inscrits repondent aux criteres
			//de la deduction
			
			BigDecimal MinimumAmountToWithhold = Deduction_Param.getMinimumAmountToWithhold();
			BigDecimal MaximumAmountToWithhold = Deduction_Param.getMaximumAmountToWithhold();
			BigDecimal MaximumEmployer = Deduction_Param.getMaxEmployer();
			
			//MinimumAmountToWithhold : Minimum a prelever
			
			if(MinimumAmountToWithhold != null && MinimumAmountToWithhold.compareTo(Env.ZERO) > 0)
			{
				//si la part de l'employé est plus petite que le minimum
				if(this.employeeAmount.compareTo(MinimumAmountToWithhold) < 0)
				{
					this.employeeAmount = MinimumAmountToWithhold ;
				}
			}

			//MaximumAmountToWithhold : Maximum a prelever
			
			if(MaximumAmountToWithhold != null && MaximumAmountToWithhold.compareTo(Env.ZERO) > 0)
			{
				//si la part de l'employé est plus grande que le maximum
				if(this.employeeAmount.compareTo(MaximumAmountToWithhold) > 0 )
				{
					this.employeeAmount= MaximumAmountToWithhold ;
				}
			}

			// Maximum Employer périodique
			if(MaximumEmployer != null && MaximumEmployer.compareTo(Env.ZERO) > 0)
			{
				//si la part de l'employer est plus grande que le maximum employer
				if(this.employerAmount.compareTo(MaximumEmployer) > 0 )
				{
					this.employerAmount= MaximumEmployer ;
				}
			}


			
			//+ Calcul par Période d'origine
			//
			// Apres avoir calculer les deductions sur le total des gains que l'employé a recu pour différent paiment.
			// on calcul le différence entre le montant que l'employé a déjà payé et la montant qu'il doit payé aujourd'hui
			//
			//
			cumEmpAmount      = ZERO;
			cumEmprAmount     = ZERO;
			cumSalAdmissible  = ZERO;
			cumHrsAdmissible  = ZERO;

			String    methodCalcul     = Method_Deduction.getValue();
			int       i_methodCalcul   = new Integer( methodCalcul ).intValue();
			BigDecimal MethodAmount = ZERO;

			//
			// Pour les déductions a montant fixe, on utilise le total de toutes les années.
			//
			//i_methodCalcul != 5 &&
			//i_methodCalcul != 17 &&  i_methodCalcul != 6 && i_methodCalcul != 7 && i_methodCalcul != 16 && i_methodCalcul != 29 ) //
			 if ( ( i_methodCalcul != 16 && i_methodCalcul != 29 && i_methodCalcul != 7 ) && Deduction.isAnnual() )
				 get_deduction_cumulative_per(  );
			 else
				 get_deduction_cumulative_per2(  );

			//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName()  + " method - "   + methodCalcul  + " Employer Amount deduction info " + MethodAmount  );

			
		    // Cumul les montants par méthode de déduction pour le calcul de l'impot.
			MethodAmount = this.employeeAmount;
			if ( s_Employee.containsKey( methodCalcul ) )
			{
				MethodAmount = this.employeeAmount.add( (BigDecimal)s_Employee.get( methodCalcul ));
				s_Employee.remove( methodCalcul );
			}
			s_Employee.put( methodCalcul , MethodAmount);
			//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName()  + " method - "   + methodCalcul  + " Employee Amount deduction info " + MethodAmount  );

			// 2022-09-01 ICI
			// Assurance avec maximum
			
			if ( Deduction.getP_Deduction_Family_ID() == 1000001 )
			{
				if (Deduction.getValue().equals("DI11") || Deduction.getValue().equals("DI12")
						|| Deduction.getValue().equals("DI20"))
				{
					
				}
				else
				{
					BigDecimal amtEr = valideMaxEmployer( Period, Employee, Payment, EmployeeDeduction, Deduction, this.employerAmount, this.employeeAmount, false, trxName );	
					if ( amtEr != null )
					{
// 2022-09-15 ne fonctionne pas pour groupe 9 Anne poisson						
//						this.employeeAmount = this.employeeAmount.add( this.employerAmount.subtract( amtEr ) );
						this.employerAmount = amtEr;
					}
					
				}
			}
				
			P_Employee_Profile EmployeeProfile = P_Employee_Profile.getProfileInsurance(Env.getCtx(), Employee.getP_Employee_ID(), Period.getEndDate(), trxName);
			if (EmployeeProfile != null && EmployeeProfile.getP_Profile_ID() == 1100067) 
			{
				if (Deduction.getP_Deduction_Family_ID() == 1000001) 
				{
					Integer key = new Integer(Payment.getP_Payment_ID());
					
					BigDecimal cumAmt = (BigDecimal) s_cacheEmployerInsurance.get(key);
					if (cumAmt != null)
						cumAmt = (BigDecimal) s_cacheEmployerInsurance.get(key);
					else
						cumAmt = Env.ZERO;

					if (Deduction.getValue().equals("DI11") || Deduction.getValue().equals("DI12")
							|| Deduction.getValue().equals("DI20"))
						cumAmt = cumAmt.add(Env.ZERO);
					else
						cumAmt = cumAmt.add(this.employeeAmount); // .add(this.employerAmount);

					s_cacheEmployerInsurance.remove(key);
					s_cacheEmployerInsurance.put(key, cumAmt);

				}
			}			
			
			

			MethodAmount = this.salaryAdmissible;
			if ( s_SalaryAdmissible.containsKey( methodCalcul ) )
			{
				MethodAmount = this.salaryAdmissible.add( (BigDecimal)s_SalaryAdmissible.get( methodCalcul ));
				s_SalaryAdmissible.remove( methodCalcul );
			}
			s_SalaryAdmissible.put( methodCalcul , MethodAmount);

//ici
			this.hoursAdmissible  = this.hoursAdmissible.subtract( cumHrsAdmissible );
			this.salaryAdmissible = this.salaryAdmissible.subtract( cumSalAdmissible );
		
			this.employeeAmount   = this.employeeAmount.subtract( cumEmpAmount );
			this.employerAmount   = this.employerAmount.subtract( cumEmprAmount );

			//2023-11-21 ne rebourse pas les impots sur un complémentaire 
			if (  Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_Complementary)
				  && salaryAdmissible.compareTo(Env.ZERO) == 0
				  &&	this.employeeAmount.compareTo(Env.ZERO) < 0
				  && (i_methodCalcul == 9 || i_methodCalcul == 10 || i_methodCalcul == 11)
			)
				
			{
				this.employeeAmount = Env.ZERO;
			}

			if ( this.salaryAdmissible.compareTo( ZERO ) == 0 
					&& Method_Deduction.isCalculatedWhenEligibleSalaryIsZero() == false )
/*
			if ( this.salaryAdmissible.compareTo( ZERO ) == 0 && i_methodCalcul != 17 && i_methodCalcul != 5 && i_methodCalcul != 6 && i_methodCalcul != 7 && i_methodCalcul != 16 && i_methodCalcul != 29 && i_methodCalcul != 35
//					 2009.03.04					
					&& i_methodCalcul != 40 && i_methodCalcul != 9 && i_methodCalcul != 13
//+ 2010.11.01					
					&& i_methodCalcul != 11		
					&& i_methodCalcul != 7		
					&& i_methodCalcul != 31		
//- 2010.11.01					
//+ 2011.12.01					
					&& i_methodCalcul != 52		
//- 2011.12.01					
					
//					&& i_methodCalcul != 4
			   )
*/					
			{
				this.salaryAdmissible = ZERO;
				this.hoursAdmissible = ZERO;
				this.employeeAmount = ZERO;
				this.employerAmount = ZERO;
			}

			//on verifie ici si on ne depasse pas
			//les maximums annuels et non annuels (cumulés)
			get_deduction_cumulated_allYears();
			get_deduction_cumulated_curYears();

			this.employerAmount = get_deduction_max_employeur( this.employerAmount, null );
			
			// 2009.03.31 Déplacer pour tenir en compte des maximums au calcul de l'impot
			MethodAmount = this.employerAmount;
			if ( s_Employer.containsKey( methodCalcul ) )
			{
				MethodAmount = this.employerAmount.add( (BigDecimal)s_Employer.get( methodCalcul ));
				s_Employer.remove( methodCalcul );
			}
			s_Employer.put( methodCalcul , MethodAmount);
			
		}
		
		
		
		//
		// Ajout les montants d'exceptions.
//		if ( PgiUtil.getSolsticeParameter(Env.getCtx(), "Environnement").equals("Test"))
		{
			try
			{
				if ( Deduction.getP_Deduction_Family_ID() == 1000001 )
					Employee_Excep();	
			}
			catch (Exception e)
			{
				System.err.println("valideMaxEmployer " + e);
			}
		}
		
		return null;

	}
	
	//
	// Calcul Deduction
	//
	private void calcul_deduction ( ) throws Exception
	{
		
//		BigDecimal totalEmployeeAmount = ZERO;
		
//		Payment.setTotalDeduction( ZERO);
		
		//
		// Loop on Employee deduction  
		//
		String query = null;
		query = "SELECT P_Method_Deduction.Priority, P_Deduction.P_Deduction_ID, IsAccumulation, IsEmployerWhenZero, P_Deduction_Param_ID, P_Employee_Deduction_ID  "
			+ "FROM P_Employee_Deduction, P_Deduction, P_Deduction_Param, P_Method_Deduction "
			+ "WHERE P_Employee_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID "
			+ "  AND P_Method_Deduction.P_Method_Deduction_ID = P_Deduction_Param.P_Method_Deduction_ID "
			+ "  AND P_Deduction.P_Deduction_ID = P_Deduction_Param.P_Deduction_ID "
			+ "  AND P_Deduction.IsActive='Y' " 
			+ "  AND P_Employee_Deduction.P_Employee_ID=" + this.Payment.getP_Employee_ID ()
			+ "  AND P_Employee_Deduction.EffectIn in ( Select Max( EffectIn) FROM P_Employee_Deduction dat WHERE dat.P_Deduction_ID = P_Deduction.P_Deduction_ID and dat.P_Employee_ID = P_Employee_Deduction.P_Employee_ID and dat.EffectIn <= " + DB.TO_DATE(Period.getEndDate() ) + ")"
// 2009.07.09
//			+ "  AND ( P_Employee_Deduction.EffectTo is null OR P_Employee_Deduction.EffectTo >= " + DB.TO_DATE( Period.getEndDate() ) +"  )"
			+ "  AND ( P_Employee_Deduction.EffectTo is null OR P_Employee_Deduction.EffectTo >= " + DB.TO_DATE( Period.getStartDate() ) +"  )"
			+ "  AND P_Deduction_Param.EffectIn in ( Select Max( EffectIn) FROM P_Deduction_PARAM WHERE P_Deduction_PARAM.P_Deduction_ID = P_Deduction.P_Deduction_ID and P_Deduction_PARAM.EffectIn <= " + DB.TO_DATE(Period.getEndDate() ) + ")"
		    + "  AND IsDeductionMandatory = 'N' "
//+ 2011.09.13 Ne pas calcule les deductions obligatoire en raison de la province de residence		    
		    + "  AND ( isDeductionMandatoryResidenceRegion = 'N' or ( isDeductionMandatoryResidenceRegion = 'Y' and exists ( select 1 from P_Employee_Deduction_Excep where P_Employee_Deduction_Excep.P_Employee_Deduction_ID = P_Employee_Deduction.P_Employee_Deduction_ID  and P_Employee_Deduction_Excep.P_Period_ID = " + Period.getP_Period_ID() + " ) ))"
//- 2011.09.13		    
		    + "  AND P_DEDUCTION.P_DEDUCTION_FAMILY_ID <> 1000001 "
		    
		    + "	 UNION ALL " 

		    + " SELECT P_Method_Deduction.Priority, P_Deduction.P_Deduction_ID,  IsAccumulation, IsEmployerWhenZero, P_Deduction_Param_ID, 0 AS P_Employee_Deduction_ID " 
		    + " FROM P_Deduction, P_Deduction_Param, P_Method_Deduction  "
		    + " WHERE P_Method_Deduction.P_Method_Deduction_ID = P_Deduction_Param.P_Method_Deduction_ID " 
		    + "  AND P_Deduction.P_Deduction_ID = P_Deduction_Param.P_Deduction_ID "
		    + "  AND P_Deduction.IsActive='Y'  "
			+ "  AND P_Deduction_Param.EffectIn in ( Select Max( EffectIn) FROM P_Deduction_PARAM WHERE P_Deduction_Param.P_Deduction_ID = P_Deduction.P_Deduction_ID and P_Deduction_Param.EffectIn <= " + DB.TO_DATE(Period.getEndDate() ) + ")"
		    + "  AND (( IsDeductionMandatory = 'Y' AND ( P_Deduction.C_Region_ID IS NULL OR P_Deduction.C_Region_ID = " + Timesheet.getTaxation_Region_ID() + " )) " 
		    + "  OR ( isDeductionMandatoryResidenceRegion = 'Y' AND ( P_Deduction.Residence_Region_ID = " + RegionResidence.getC_Region_ID()   + " OR P_Deduction.Residence_Region_ID = " + Workplace.getC_Region_ID() + " ) ) ) " 
		    + "  AND P_Deduction.AD_Client_ID = " + this.Payment.getAD_Client_ID()

		    + "  AND P_DEDUCTION.P_DEDUCTION_FAMILY_ID <> 1000001 "

		    
			+ "  ORDER BY P_Method_Deduction.Priority, P_Deduction.P_Deduction_ID " ;
/*
	    + "  AND ( IsDeductionMandatory = 'Y' 
	    + "  AND ( P_Deduction.C_Region_ID IS NULL OR P_Deduction.C_Region_ID = " + Timesheet.getTaxation_Region_ID() + " OR P_Deduction.C_Region_ID = " + RegionResidence.getC_Region_ID() + " OR P_Deduction.Residence_Region_ID = " + RegionResidence.getC_Region_ID() + " ) "
  */ 

		
//		log.log (Level.INFO,"CalculNet - Query : " + query );

		//Log.trace( 5,"calcul_Deduction - query=" + query);
		PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			boolean calcul = true;
			
			P_Payment_Deduction PaymentDeduction;
			while (rs.next ())
			{
				calcul = true;

				Deduction_Param = P_Deduction_Param.get( Env.getCtx(), rs.getInt("P_Deduction_Param_ID"),trxName);
                Deduction = P_Deduction.get( Env.getCtx(), Deduction_Param.getP_Deduction_ID(),trxName);

                Method_Deduction = P_Method_Deduction.get( Env.getCtx (), Deduction_Param.getP_Method_Deduction_ID() ,trxName);

				if ( rs.getInt("P_Employee_Deduction_ID") != 0 )
					EmployeeDeduction = P_Employee_Deduction.get( Env.getCtx (), rs.getInt("P_Employee_Deduction_ID"), trxName);

                calcul = isDeductionCalcul( Deduction, Method_Deduction, EmployeeDeduction);


				EmployeeDeduction = null;
                if ( calcul )
                {
    				if ( rs.getInt("P_Employee_Deduction_ID") != 0 )
    					EmployeeDeduction = P_Employee_Deduction.get( Env.getCtx (), rs.getInt("P_Employee_Deduction_ID"), trxName);
    				else
    				{
    					EmployeeDeduction = P_Employee_Deduction.get(Env.getCtx(), Payment.getP_Employee_ID(), Deduction.getP_Deduction_ID(), Period.getEndDate(), trxName);
    					if ( EmployeeDeduction.getP_Employee_Deduction_ID() <= 0 )
    					{
    						EmployeeDeduction = new P_Employee_Deduction( Env.getCtx(), -1, trxName);
    						EmployeeDeduction.setP_Deduction_ID( Deduction.getP_Deduction_ID());
    						EmployeeDeduction.setIsActive(true);
    						EmployeeDeduction.setP_Employee_ID(Payment.getP_Employee_ID());
    						EmployeeDeduction.setEffectIn(Period.getStartDate());
    						
    						// Déduction Nunavut ou NWT à 2%
    						if ( Method_Deduction.getValue().equals("40") )
    							EmployeeDeduction.setEmployeeRate(new BigDecimal(2));

    						EmployeeDeduction.save();
    						
    						P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "La Déduction " + Deduction.getValue() + " à été ajouté au dossier employé ", trxName);
    						
    					}
    				}
                }

//				log.log (Level.INFO,"CalculNet - Deduction : " + Deduction.getValue() );

				if ( EmployeeDeduction != null && EmployeeDeduction.isActive() && Deduction_Param.isActive() && calcul )
				{
					
	                //
	                // avertissement pour éviter de calculer 2 déductions d'impot différente.
	                // c'est un avertissement car pour un correction en période d'origine cela peut etre normal.
	                //
	                if ( Deduction_Param.getP_Employer_ID() != 0 && Period.getP_Period_ID() == TimeSheetPeriod.getP_Period_ID() &&  
	                     Deduction_Param.getP_Employer_ID() != Employee.getP_Employer_ID() )
						P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "L'employé "+Employee.getValue()+" n'a pas le même numéro d'employeur que la déduction : " + Deduction.getValue() , trxName);

	                if ( Deduction_Param != null && Deduction_Param.isActive() )
					{
//						Method_Deduction = P_Method_Deduction.get( Env.getCtx (), Deduction_Param.getP_Method_Deduction_ID() ,trxName);
//						String    methodCalcul     = Method_Deduction.getValue();

						this.salaryAdmissible = ZERO;
						this.hoursAdmissible = ZERO;
						this.employeeAmount = ZERO;
						this.employerAmount = ZERO;

						_calcul_deduction( false );

						PaymentDeduction = new P_Payment_Deduction ( Env.getCtx (), -1, trxName);
						
						PaymentDeduction.setP_Employee_ID( this.Payment.getP_Employee_ID ());
						PaymentDeduction.setP_Deduction_ID( EmployeeDeduction.getP_Deduction_ID() );
						PaymentDeduction.setP_Year_ID(  this.Period.getP_Year_ID () );
						PaymentDeduction.setP_Period_ID( this.Period.getP_Period_ID () );
						PaymentDeduction.setP_Payment_ID( this.Payment.getP_Payment_ID ());

						String    methodCalcul     = Method_Deduction.getValue();
						int       i_methodCalcul   = new Integer( methodCalcul ).intValue();

						 // 2009.04.22
						if ( ! ( Payment.getPaymentTypeDoc().equals(P_Payment.PAYMENTTYPEDOC_SeparationPay) && ( i_methodCalcul == 9 || i_methodCalcul == 10 || i_methodCalcul == 11 ) ))
							Valide_amount( false, null, null );

					
						// *************************************************************
				
//
// 						
						
						// Si la paye est pour être inférieur a 0. Le montant de la déduction égal le montant de la paie.
						if ( Payment.getGrossEarnings().compareTo( ZERO) >= 0 )
						{
							if ( NetPay.subtract( this.employeeAmount ).compareTo( ZERO) < 0  )
							{
								//
								// On va en arrérage.
								//
								if (rs.getString("IsAccumulation").equals("Y"))
								{
									PaymentDeduction.setAccumulationAmount( this.employeeAmount.subtract( NetPay ).setScale(2, BigDecimal.ROUND_HALF_UP ) );
								}
								
								this.employeeAmount = NetPay ;
								
								// Si la part employé est à 0 il n'y a pas de part employeur
								// Ex : FTQ et Fond de pension.
								//
								if ( rs.getString("IsEmployerWhenZero").equals("N") )
								{
									if ( this.employeeAmount == null || this.employeeAmount.compareTo(Env.ZERO) == 0)
										this.employerAmount = Env.ZERO;
								}

							}
							//
							// Recupération de Arrérage.
							//
							else if ( rs.getString("IsAccumulation").equals("Y") )
							{
								BigDecimal RetrievalAmount = EmployeeDeduction.getToRetrieveAmount( Deduction_Param, employeeAmount);
						        if ( RetrievalAmount.compareTo( ZERO) != 0 )
								{
									if ( ( NetPay.subtract( this.employeeAmount ).subtract( RetrievalAmount ) ) .compareTo( ZERO) < 0 )
									{
										//2008-06-25
										RetrievalAmount = NetPay.subtract( this.employeeAmount ); //.subtract( RetrievalAmount ).abs();
									}

									PaymentDeduction.setRetrievalAmount( RetrievalAmount );
									this.employeeAmount = this.employeeAmount.add( RetrievalAmount );
								}

							}
						}

						//						
						//
						// *************************************************************

						if ( this.employeeAccumulationAmount.compareTo( ZERO) != 0)
							PaymentDeduction.setAccumulationAmount( this.employeeAccumulationAmount);
//						if ( this.employerAccumulationAmount.compareTo( ZERO) != 0)
//							PaymentDeduction.setAccumulationAmountEmployer( this.employerAccumulationAmount);
						
						PaymentDeduction.setEmployee_Part( this.employeeAmount.setScale(2,BigDecimal.ROUND_HALF_UP) );
						PaymentDeduction.setEmployer_Part( this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP) );
						PaymentDeduction.setSalary_Eligible( this.salaryAdmissible.setScale(2,BigDecimal.ROUND_HALF_UP) );
						PaymentDeduction.setHours_Eligible( this.hoursAdmissible.setScale(2,BigDecimal.ROUND_HALF_UP) );
						PaymentDeduction.setIsExonerated( isExonerated );
						PaymentDeduction.setOrigine("FTP");
						PaymentDeduction.setP_Deduction_Param_ID( Deduction_Param.getP_Deduction_Param_ID());
						PaymentDeduction.setP_Employee_Deduction_ID( EmployeeDeduction.getP_Employee_Deduction_ID());

						//- Calcul par Période d'origine

						
						//2022-05-05
						if ( ! Method_Deduction.isWithHoursEligible())
							PaymentDeduction.setHours_Eligible(ZERO );

						
						//MaximumBackpaymentAccumulated : Arrérage Accumulé
						BigDecimal MaximumBackpaymentAccumulated = Deduction_Param.getMaximumBackpaymentAccumulated();
						BigDecimal MaximumBackpayment = Deduction_Param.getMaximumBackpayment();
						
						if(MaximumBackpaymentAccumulated != null && MaximumBackpaymentAccumulated.compareTo(Env.ZERO) == 1)
						{
							//si l'arrerage accumulé est plus grand que le maximum
							if(PaymentDeduction.getAccumulationAmount().compareTo(MaximumBackpaymentAccumulated) == 1)
							{
								PaymentDeduction.setAccumulationAmount(MaximumBackpaymentAccumulated);
							}
						}

						//MaximumBackpayment : Arrérage
						
						if(MaximumBackpayment != null && MaximumBackpayment.compareTo(Env.ZERO) == 1)
						{
							//si l'arrerage est plus grand que le maximum
							if(PaymentDeduction.getRetrievalAmount().compareTo(MaximumBackpayment) == 1)
							{
								PaymentDeduction.setRetrievalAmount(MaximumBackpayment);
							}
						}

						if ( PaymentDeduction.isExonerated() )
						{
							if (    
							        PaymentDeduction.getEmployee_Part().compareTo( ZERO ) == 0 &&
							        PaymentDeduction.getEmployer_Part().compareTo( ZERO ) == 0 
							   )
							PaymentDeduction.setSalary_Eligible( ZERO );
							PaymentDeduction.setHours_Eligible(ZERO );
						}

						
//						PaymentDeduction.save( );
						
						if (    
						        PaymentDeduction.getEmployee_Part().compareTo( ZERO ) != 0 ||
						        PaymentDeduction.getEmployer_Part().compareTo( ZERO ) != 0 ||
// 2008.10.02
//2010.11.16
					
//2021.07.20						        
						        ( PaymentDeduction.getSalary_Eligible().compareTo( ZERO )!= 0 && (i_methodCalcul == 4  || i_methodCalcul == 31 )) ||
//2010.11.16
//						        
						        PaymentDeduction.getHours_Eligible().compareTo( ZERO ) != 0 ||
//+ 2008-06-10
						        PaymentDeduction.getAccumulationAmount().compareTo( ZERO ) != 0 ||
						        PaymentDeduction.getRetrievalAmount().compareTo( ZERO ) != 0 ||
//- 2008-06-10

						        PaymentDeduction.isExonerated() 
						   )
						{
							PaymentDeduction.save( );
						}

						
						
						
						NetPay =  NetPay.subtract( this.employeeAmount ); 

					}
					else
					{
						//Log.trace( 5, "**" + EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " no param found for this Deduction  ** "   );
					}
					    
				}
	 			 
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
//			Payment.setNetPay(NetPay);
		
	}   // calcul_deduction
	

	private BigDecimal Taxable_Benefits_Excep( P_Employee_Taxable_Benefit Employee_Taxable_Benefit, BigDecimal BenefitAmount ) throws Exception
	{
		P_Employee_Taxable_Benefit_Excep Exception = P_Employee_Taxable_Benefit_Excep.get( Env.getCtx (),  Employee_Taxable_Benefit.getP_Employee_Taxable_Benefit_ID(), Period.getP_Period_ID(), trxName );

		if ( Exception != null)
		{
			if ( Exception.getExceptionAmount() != null &&  Exception.getExceptionAmount().compareTo(ZERO) != 0 )
				BenefitAmount   = Exception.getExceptionAmount();

			if ( Exception.getExceptionAmountAdd() != null &&  Exception.getExceptionAmountAdd().compareTo(ZERO) != 0 )
				BenefitAmount   = BenefitAmount.add( Exception.getExceptionAmountAdd() );
			
			if ( ! Exception.isActive() )
			{
				BenefitAmount   = ZERO;
				
			}
		}
		else return ZERO;
		
		return BenefitAmount;
	}

	
	
	private void Employee_Excep() throws Exception
	{
		P_Employee_Deduction_Excep deductionExcep = P_Employee_Deduction_Excep.get( Env.getCtx (),  EmployeeDeduction.getP_Employee_Deduction_ID(), Period.getP_Period_ID(), trxName );

		if ( deductionExcep != null)
		{
			//
			// Recherche les montants admissible /
			//modifier 2008-05-07 - Le salaire admissible des assurances utilises un fonction différents.
			//getSalaryAdmissible( Deduction_Param );

			if ( deductionExcep.getExceptionAmountEmployee() != null &&  deductionExcep.getExceptionAmountEmployee().compareTo(ZERO) != 0 )
				this.employeeAmount   = deductionExcep.getExceptionAmountEmployee();
			
			if ( deductionExcep.getExceptionAmountEmployer() != null &&  deductionExcep.getExceptionAmountEmployer().compareTo(ZERO) != 0 )
				this.employerAmount   = deductionExcep.getExceptionAmountEmployer();

			if ( deductionExcep.getExceptionAmountEmployeeAdd() != null &&  deductionExcep.getExceptionAmountEmployeeAdd().compareTo(ZERO) != 0 )
				this.employeeAmount   = this.employeeAmount.add( deductionExcep.getExceptionAmountEmployeeAdd() );
			
			if ( deductionExcep.getExceptionAmountEmployerAdd() != null &&  deductionExcep.getExceptionAmountEmployerAdd().compareTo(ZERO) != 0 )
				this.employerAmount   = this.employerAmount.add( deductionExcep.getExceptionAmountEmployerAdd() );
			
			
			if ( deductionExcep.getExceptionSalaryRate().compareTo(ZERO) != 0  )
			{
				//
				// Recherche les montants admissible /
				getSalaryAdmissible( Deduction_Param );
				this.employeeAmount = salaryAdmissible.multiply(deductionExcep.getExceptionSalaryRate()).setScale(2,BigDecimal.ROUND_HALF_UP); 
			}

			if ( deductionExcep.getExceptionEmployerRate().compareTo(ZERO) != 0  )
			{
				//
				// Recherche les montants admissible /
				getSalaryAdmissible( Deduction_Param );
				this.employerAmount = salaryAdmissible.multiply(deductionExcep.getExceptionEmployerRate()).setScale(2,BigDecimal.ROUND_HALF_UP); 
			}
			if (deductionExcep.isExonerated() )
			{
				this.employeeAmount = ZERO;
				isExonerated = true;
			}
			
			if ( ! deductionExcep.isActive() )
			{
				this.hoursAdmissible  = ZERO;
				this.salaryAdmissible = ZERO;
				this.employeeAmount   = ZERO;
				this.employerAmount   = ZERO;
				
			}
		}

	}

	private void Payment_Excep() throws Exception
	{
//		ici.. faire le total et non pas juste un get.....
		
		
		P_Payment_Deduction_Excep[] deductionExcep = P_Payment_Deduction_Excep.get( Env.getCtx (), Payment.getP_Payment_ID(), EmployeeDeduction.getP_Employee_Deduction_ID(), Period.getP_Period_ID(), Payment.getP_Employee_ID(),  trxName );
// 2009.10.23
//		P_Payment_Deduction_Excep deductionExcep = P_Payment_Deduction_Excep.get( Env.getCtx (), Payment.getP_Payment_ID(), EmployeeDeduction.getP_Employee_Deduction_ID(), Period.getP_Period_ID(),  trxName );

		if ( deductionExcep != null)
		{
			//
			// Recherche les montants admissible /
//			getSalaryAdmissible( Deduction_Param );

			//2010.04.14
			BigDecimal employeeAmountCum = ZERO;
			BigDecimal employerAmountCum = ZERO;
			
			for ( int i=0; deductionExcep.length > i; i++)
			{
				if (  deductionExcep[i] != null)
				{
					if ( deductionExcep[i].getEmployeeAssessment().equals( P_LongTermLeave_Deduction.EMPLOYEEASSESSMENT_RelievedPlusEmployer) )
					{
						this.employeeAmount = this.employerAmount;
						isExonerated = true;
					}
					if ( deductionExcep[i].getEmployerAssessment().equals( P_LongTermLeave_Deduction.EMPLOYERASSESSMENT_AssessPlusEmployee) )
					{
						this.employerAmount = this.employerAmount.add( this.employeeAmount);
					}
					if ( deductionExcep[i].getEmployeeAssessment().equals( P_LongTermLeave_Deduction.EMPLOYEEASSESSMENT_No) )
					{
						this.employeeAmount = ZERO;
						isExonerated = false;
					}
					if ( deductionExcep[i].getEmployeeAssessment().equals( P_LongTermLeave_Deduction.EMPLOYEEASSESSMENT_Relieved) )
					{
						this.employeeAmount = ZERO;
						isExonerated = true;
					}
				
					if ( deductionExcep[i].getEmployerAssessment().equals( P_LongTermLeave_Deduction.EMPLOYERASSESSMENT_No) )
					{
						this.employerAmount = ZERO;
					}

					// 2009.07.08 change Employee_Deduction_Excep by Payment_Deduction_Excep for gain with deduction auto.
					if ( deductionExcep[i].getExceptionAmountEmployee() != null && deductionExcep[i].getExceptionAmountEmployee().compareTo(Env.ZERO) != 0)
					{
						// 2009.08.07 exception avec correction dans le passé. ajout de ce qui a déjà être payer.
						//2010.04.14
						employeeAmountCum = employeeAmountCum.add( deductionExcep[i].getExceptionAmountEmployee() );
						this.employeeAmount = employeeAmountCum;
					}
					if ( deductionExcep[i].getExceptionAmountEmployer() != null && deductionExcep[i].getExceptionAmountEmployer().compareTo(Env.ZERO) != 0)
					{
						// 2009.08.07 exception avec correction dans le passé. ajout de ce qui a déjà être payer.
						//2010.04.14
						employerAmountCum = employerAmountCum.add( deductionExcep[i].getExceptionAmountEmployer() );
						this.employerAmount = employerAmountCum;
					}


					//			if ( deductionExcep.getEmployerAssessment().equals( P_LongTermLeave_Deduction.EMPLOYERASSESSMENT_Assess) )
					
					if ( ! deductionExcep[i].isActive() )
					{
						this.hoursAdmissible  = ZERO;
						this.salaryAdmissible = ZERO;
						this.employeeAmount   = ZERO;
						this.employerAmount   = ZERO;
						
					}
				}
			}
		}

	}

	/*
	private void Payment_Excep() throws Exception
	{
		P_Payment_Deduction_Excep deductionExcep = P_Payment_Deduction_Excep.get( Env.getCtx (), Payment.getP_Payment_ID(), EmployeeDeduction.getP_Employee_Deduction_ID(), Period.getP_Period_ID(), Payment.getP_Employee_ID(),  trxName );
// 2009.10.23
//		P_Payment_Deduction_Excep deductionExcep = P_Payment_Deduction_Excep.get( Env.getCtx (), Payment.getP_Payment_ID(), EmployeeDeduction.getP_Employee_Deduction_ID(), Period.getP_Period_ID(),  trxName );

		if ( deductionExcep != null)
		{
			//
			// Recherche les montants admissible /
			getSalaryAdmissible( Deduction_Param );

			if ( deductionExcep.getEmployeeAssessment().equals( P_LongTermLeave_Deduction.EMPLOYEEASSESSMENT_RelievedPlusEmployer) )
			{
				this.employeeAmount = this.employerAmount;
				isExonerated = true;
			}
			if ( deductionExcep.getEmployerAssessment().equals( P_LongTermLeave_Deduction.EMPLOYERASSESSMENT_AssessPlusEmployee) )
			{
				this.employerAmount = this.employerAmount.add( this.employeeAmount);
			}
			if ( deductionExcep.getEmployeeAssessment().equals( P_LongTermLeave_Deduction.EMPLOYEEASSESSMENT_No) )
			{
				this.employeeAmount = ZERO;
				isExonerated = false;
			}
			if ( deductionExcep.getEmployeeAssessment().equals( P_LongTermLeave_Deduction.EMPLOYEEASSESSMENT_Relieved) )
			{
				this.employeeAmount = ZERO;
				isExonerated = true;
			}
		
			if ( deductionExcep.getEmployerAssessment().equals( P_LongTermLeave_Deduction.EMPLOYERASSESSMENT_No) )
			{
				this.employerAmount = ZERO;
			}

			// 2009.07.08 change Employee_Deduction_Excep by Payment_Deduction_Excep for gain with deduction auto.
			if ( deductionExcep.getExceptionAmountEmployee() != null && deductionExcep.getExceptionAmountEmployee().compareTo(Env.ZERO) != 0)
			{
				// 2009.08.07 exception avec correction dans le passé. ajout de ce qui a déjà être payer.
//				this.employeeAmount = deductionExcep.getExceptionAmountEmployee();
//				get_deduction_cumulative_per(  );
				this.employeeAmount = deductionExcep.getExceptionAmountEmployee();
//				this.employeeAmount = this.employeeAmount.add( cumEmpAmount);
			}
			if ( deductionExcep.getExceptionAmountEmployer() != null && deductionExcep.getExceptionAmountEmployer().compareTo(Env.ZERO) != 0)
			{
				// 2009.08.07 exception avec correction dans le passé. ajout de ce qui a déjà être payer.
//				this.employerAmount = deductionExcep.getExceptionAmountEmployer();
//				get_deduction_cumulative_per(  );
				this.employerAmount = deductionExcep.getExceptionAmountEmployer();
//				this.employerAmount = this.employerAmount.add( cumEmprAmount);
			}


			//			if ( deductionExcep.getEmployerAssessment().equals( P_LongTermLeave_Deduction.EMPLOYERASSESSMENT_Assess) )
			
			if ( ! deductionExcep.isActive() )
			{
				this.hoursAdmissible  = ZERO;
				this.salaryAdmissible = ZERO;
				this.employeeAmount   = ZERO;
				this.employerAmount   = ZERO;
				
			}
		}

	}
*/
	private BigDecimal getNbrDay( int P_Payment_ID, int P_Employee_ID, int P_Period_ID, int P_Column_Adm_ID ) throws Exception
	{
		
		String query = "Select count(day) nbrDay "
				+ " FROM P_PAYMENT_GAIN "
				+ " WHERE quantitycalc >= 1"
//				+ "  AND P_Payment_ID = " + P_Payment_ID 
				+ "  AND P_Employee_ID = " + P_Employee_ID 
		        + "  AND P_PERIOD_ID = " + P_Period_ID
		        ;
		query = query + " and P_Gain_ID in ( "
	      + "  select p_gain_id from p_gain_column "
	      + " inner join p_gain_Parameter on  p_gain_Parameter.p_gain_Parameter_id = p_gain_column.p_gain_Parameter_id "
	      + "   where P_Column_Adm_ID = " + P_Column_Adm_ID
	      + " and isadmissible = 'Y' "
	      +  " )" ;
		
		BigDecimal nbrDay = Env.ZERO;
		
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			nbrDay =  rs.getBigDecimal("nbrDay");
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;

		
		
		return nbrDay;

	}

	//2010.04.20 - gestion des maximums salaire adm. RQAP, RRQ, EI.
	private void getSalaryAdmissible( P_Deduction_Param Deduction_Param ) throws Exception
	{
		getSalaryAdmissible( Deduction_Param, null );
	}

	private void getSalaryAdmissible( P_Deduction_Param Deduction_Param, BigDecimal MaxSalary ) throws Exception
	{
		

		// Si l'employé habite le Québec et qu'il travail dans un autre province.
		if ( Deduction.getC_Region_ID() == QUEBEC && RegionResidence.getC_Region_ID() == QUEBEC && Region.getC_Region_ID() != QUEBEC )
		{
			// salaire admissible = total des avantages imposable provincial admissible.
			salaryAdmissible = get_matrixTaxB_salaryAdmissible( Deduction_Param.getP_Column_Adm_ID() );
			return;
		}

		
		if ( Deduction_Param.getP_Column_Adm_ID() != 0 )
		{
			hoursAdmissible  = get_matrix_hoursAdmissible  ( Deduction_Param.getP_Column_Adm_ID() );
			salaryAdmissible = get_matrix_salaryAdmissible ( Deduction_Param.getP_Column_Adm_ID() );
		}
		else
		{
			this.salaryAdmissible = Payment.getAnnualSalary();
		
		}

/*		
		if ( salaryAdmissible.compareTo( ZERO )  < 0 ) 
		{
			hoursAdmissible  = ZERO;
			salaryAdmissible = ZERO;
//			log.log( Level.WARNING,"calcul_rrq_rpc - *** admissible amount negative *** " );
//			return ;
		}
*/
		
		//on verifie si le salaire admissible ne depasse pas le maximum des gains
		//admissible tel qu'indique sur la deduction
		BigDecimal MaximumAllowableEarning = Deduction_Param.getMaximumAllowableEarnings();
		//si ya un max positif
		if(MaximumAllowableEarning != null && MaximumAllowableEarning.compareTo(Env.ZERO) != 0 )
		{
			//donc si le salaire admissible est plus grand que le max dans la deduction, on 
			//met le salaire au max
			if ( Deduction_Param.getP_Method_Deduction_ID()==117 )
			{
				if(this.salaryAdmissible.compareTo(MaximumAllowableEarning) > 0 )
				{
					this.salaryAdmissible = MaximumAllowableEarning;
				}
				
				if ( Deduction.getValue().equals("DT01"))
				{
					if(this.salaryAdmissible.compareTo(MaximumAllowableEarning.divide(new BigDecimal(52),2,BigDecimal.ROUND_HALF_UP)) > 0 )
					{
						this.salaryAdmissible = MaximumAllowableEarning.divide(new BigDecimal(52),2,BigDecimal.ROUND_HALF_UP);
					}
					
				}

					
			}

			if ( Deduction_Param.getP_Method_Deduction_ID()==113 )
			{
				if(this.salaryAdmissible.compareTo(MaximumAllowableEarning) > 0 )
				{
					this.salaryAdmissible = MaximumAllowableEarning;
				}
			}

/*	ENLEVER 2022-06-01 le temps de trouver un formule.
 * REMIS 2022-09-29		
			// Norme du travail
			 */ 
			if ( Deduction_Param.getP_Method_Deduction_ID()==160 )
			{
				if(this.salaryAdmissible.compareTo(MaximumAllowableEarning.divide( new BigDecimal(Frequency.getNumberOfPeriod()),2,BigDecimal.ROUND_HALF_UP)) > 0 )
				{
					this.salaryAdmissible = MaximumAllowableEarning.divide(new BigDecimal(Frequency.getNumberOfPeriod()),2,BigDecimal.ROUND_HALF_UP);
				}
			}
			
			if ( Deduction_Param.getP_Method_Deduction_ID()==119 )
			{
				// si le maximum du salaire admissible a été atteind.
				get_deduction_cumulative( );
				
				if ((this.cumSalaryAdmissible.add( this.salaryAdmissible )).compareTo(MaximumAllowableEarning) >= 0 )
				{
					this.salaryAdmissible = MaximumAllowableEarning.subtract(this.cumSalaryAdmissible);
				}
				if ( this.salaryAdmissible.compareTo( Env.ZERO ) == 0 )
					this.hoursAdmissible = ZERO;
			}
		}
		
		//2010.04.20 - gestion des maximums salaire adm. RQAP, RRQ, EI.
		if ( MaxSalary != null)
		{
			// si le maximum du salaire admissible a été atteind.
			get_deduction_cumulative( );
			
			if ((this.cumSalaryAdmissible.add( this.salaryAdmissible )).compareTo( MaxSalary ) >= 0 )
			{
				this.salaryAdmissible = MaxSalary.subtract(this.cumSalaryAdmissible);
				//+ 2010.09.03 pour assurance emploi 
				if ( this.salaryAdmissible.compareTo( Env.ZERO) < 0)
					this.salaryAdmissible = Env.ZERO;
				//- 2010.09.03
			}
			
			if ( this.salaryAdmissible.compareTo( Env.ZERO ) == 0 )
				this.hoursAdmissible = ZERO;
			
		}


	}
	
	//
	// Calcul RRQ and RPC
	//
	private void calcul_rrq_rpc_new ( boolean rrq ) throws Exception
	{
		// 2009.04.27
		// les indiens inscrit ne paie pas de RRQ ou de CPP.
		if ( this.Employee.isStatusIndian() )
			return;
		
		boolean federal = true;
		//
		// Québec ou Fédéral
		//
		if ( Region.getC_Region_ID() == QUEBEC )  
			federal = false;
		else
			federal = true;
		

		//+ 2008-08-21
		// S'il y a un montant fixe dans le dossier employee
		BigDecimal EmployeeAmount = EmployeeDeduction.getEmployeeAmount( Deduction_Param);
		BigDecimal EmployerAmount = EmployeeDeduction.getEmployerAmount( Deduction_Param); 

		if ( ( EmployeeAmount != null && EmployeeAmount.compareTo(Env.ZERO) != 0 ) || 
			 ( EmployerAmount != null && EmployerAmount.compareTo(Env.ZERO) != 0 )
			   )
		{
			this.employeeAmount = EmployeeAmount;
			this.employerAmount = EmployerAmount;
			return;
		}
		//-

		
		BigDecimal rpc_rate = ZERO;
		BigDecimal rpc_max_gain = ZERO;
		BigDecimal rpc_exemption = ZERO;
		BigDecimal rpc_max = ZERO;
		
		if ( federal )
		{
			rpc_rate      = (BigDecimal)taxTable.get( "RPC.1");
			rpc_max_gain  = (BigDecimal)taxTable.get( "RPC.2");
			rpc_exemption = (BigDecimal)taxTable.get( "RPC.3");
			rpc_max       = (BigDecimal)taxTable.get( "RPC.4");
		}
		else
		{
			rpc_rate      = (BigDecimal)taxTable.get( "RRQ.1");
			rpc_max_gain  = (BigDecimal)taxTable.get( "RRQ.2");
			rpc_exemption = (BigDecimal)taxTable.get( "RRQ.3");
			rpc_max       = (BigDecimal)taxTable.get( "RRQ.4");
			
		}

//		2017-05-28 Lire le salaire admissible sans le maximum
		getSalaryAdmissible( Deduction_Param );
		BigDecimal salAdm = salaryAdmissible ;

		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param , rpc_max_gain);

		// si on calcul du RRQ pour un employé dont la province d'imposition n'est pas le Québec
		// alors on ne considère pas d'exemption.
		if ( rrq && Region.getC_Region_ID() != QUEBEC )
			rpc_exemption = Env.ZERO;

		
		rpc_rate = rpc_rate.divide( new BigDecimal( 100 ), 6, BigDecimal.ROUND_HALF_UP );

		//
		// Pour payé du RRQ ou RPC, il faut être agé entre 18 ans.
		// Lorsque l'employé atteint 18 ans dans l'année,
		// alors il faut modifier le maximum annuel au RRQ ou RPC selon le
		// mois où il atteint un ou l'autre.
		//
		// Exemple : 18 ans au mois de mars alors l'employé commence à cotisé
		//           au moins d'avril et le maximum est calculé comme suit:
		//           Max rrq = Max rrq * (nombre cotisable (9) / 12)
		//
		//	get_matrix ( P_Column_Adm_ID, qty, amount );
		
		
		// 18 Year old...
		// employé ne cotise pas le mois de son anniversaire.
       //		int age = Employee.getAge( Period.getStartDate());
//		int age = Employee.getAge( TimeUtil.addMonth( Period.getStartDate(), -1));
// 2009.10.27 
		int age = Employee.getAge( Payment.getPayDate() );
  	    BigDecimal ageRRQ = Employee.getAgeWithMonth( Payment.getPayDate() );

        //2015.11.09 gestion du changement de province.
		int nbrProvince = getNbrProvince ( );


		if ( age <  18 )
		{
			P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol10-I" ) + " " + age, trxName);
			this.salaryAdmissible = Env.ZERO;	
			this.hoursAdmissible = Env.ZERO;
			return ;
		}

		// 2009.10.27 
  	    if ( ageRRQ.compareTo( new BigDecimal(18.00)) ==  0 && TimeUtil.getMonth( Employee.getBirthDate()) == TimeUtil.getMonth( Payment.getPayDate()))
//		if ( age ==  18 && TimeUtil.getMonth( Employee.getBirthDate()) == TimeUtil.getMonth( Payment.getPayDate()))
		{
			P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol10-I"   ) + " " + age, trxName);
			this.salaryAdmissible = Env.ZERO;	
			this.hoursAdmissible = Env.ZERO;
			return ;
			
		}

		if ( federal )
		{
//			age = Employee.getAge( TimeUtil.addMonth( Period.getEndDate(), -1));
//			age = Employee.getAge( TimeUtil.addMonth( Period.getEndDate(), -1));
			if ( age >=  70 )
			{
				P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol10-I" ) + " " + age, trxName);
				this.salaryAdmissible = Env.ZERO;	
				this.hoursAdmissible = Env.ZERO;

				return ;
			}		
		}
		
		// Formula
		//
		// C = 0.0495 ( S3 - V/P ) until M - A5
		//
		
		BigDecimal sal = ZERO;
		BigDecimal tmp = ZERO;
		sal =  salaryAdmissible ;
		
		tmp =  rpc_exemption.divide( new BigDecimal( Frequency.getNumberOfPeriod()) , 2, BigDecimal.ROUND_HALF_UP);
		
		// si le montant d'exemption est plus petit que le salaire.
		if ( tmp.compareTo(sal) > 0 && sal.compareTo(ZERO) > 0)
			tmp = sal.setScale(2,BigDecimal.ROUND_HALF_UP);
		

		//+2011.07.19 RRQ
		if ( sal.compareTo( ZERO ) != 0) {
		//-2011.07.19 RRQ
			sal = sal.subtract( tmp ).setScale(2, BigDecimal.ROUND_HALF_UP);
			//2017-05-28 
			salAdm = salAdm.subtract( tmp ).setScale(2, BigDecimal.ROUND_HALF_UP);
		}		

		//2017-05-28
		//		this.employeeAmount = sal.multiply( rpc_rate );
		this.employeeAmount = salAdm.multiply( rpc_rate );

		//2015.09.11 le cumulatif de la déduction et non celui de la famille
		get_deduction_cumulative_family( Deduction.getP_Deduction_Family_ID() );
		

        Amount_Rpc = this.employeeAmount.multiply( new BigDecimal( Frequency.getNumberOfPeriod()));


		if (  federal && nbrProvince > 1 && if_rrq_rpc() ) {

			// Formule qui doit être utilisée pour déterminer les contributions au RPC pour les
			// travailleurs lorsqu’ils changent d’emploi en provenance du Québec vers une autre province ou
			// territoire durant l’année dont les salaires ou traitements ont été perçues
			//Au moins élevé des montants suivants : 
				// 2 748,90 $ – [(DQ x (0,051/0,0555*)) + D];
				// ii) 0,051 × [PI – (3 500 $ / P)]
				//  Si le résultat est négatif, C = 0 $. 

			//D Cumul annuel des cotisations de l’employé au Régime de pensions du Canada avec l’employeur (ne peut être plus élevé que le maximum annuel)
            //DQ Cumul annuel des cotisations de l’employé au Régime de rentes du Québec avec l’employeur (ne peut être plus élevé que le maximum annuel) 
			
			rpc_rate      = (BigDecimal)taxTable.get( "RPC.1");
			BigDecimal rrq_rate      = (BigDecimal)taxTable.get( "RRQ.1");
			rpc_max  = (BigDecimal)taxTable.get( "RPC.4");

			BigDecimal d = get_deduction_cumulative_rpc();
			BigDecimal dq = get_deduction_cumulative_rrq(); 

			BigDecimal mnt1 = rpc_max.subtract( ( dq.multiply( rpc_rate.divide( rrq_rate, 8, BigDecimal.ROUND_HALF_UP  ) )  )).add( d );
			BigDecimal mnt2 = this.employeeAmount;
			
			if ( mnt1.compareTo( mnt2 ) < 0 ) {
				this.employeeAmount = mnt1;
			}
			
			if ( this.employeeAmount.compareTo( Env.ZERO ) < 0 ) {
				this.employeeAmount = Env.ZERO;
			}
			
		} 

        
		if ( nbrProvince > 1 && this.employeeAmount.add( this.cumEmployeeAmount  ).compareTo( rpc_max ) > 0)
		{
			if ( if_rrq_rpc() ) {
				rpc_max_gain  = (BigDecimal)taxTable.get( "RRQ.2");
				rpc_max       = (BigDecimal)taxTable.get( "RRQ.4");
			}
				
		}
			

        if ( this.employeeAmount.add( this.cumEmployeeAmount  ).compareTo( rpc_max ) > 0 )
		{
			this.employeeAmount = rpc_max.subtract( this.cumEmployeeAmount ); 
			
			Amount_Rpc = rpc_max;
		}

        
        if (Amount_Rpc.compareTo( rpc_max ) > 0)
			Amount_Rpc = rpc_max;
	
		
		// 2008.10.02 adjuste employer part si l'employé n'a pas assez de salaire pour payé la déduction 
		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_DOWN );

		
        //  s'il a dépassé le maximum, il ne doit pas y avoir de remboursement.
        //
		
		if ( Period.getP_Period_ID() != Payment.getP_Period_ID() || ! Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_Regular))
		{
		    if ( this.cumSalAdmissible.compareTo( rpc_max_gain ) == 0)
	   		{
	  		    //calcul par périod d'origine demande le total de la période.
	  		    get_deduction_cumulative_per_rrq(  );
	   			this.hoursAdmissible = this.cumHrsAdmissible;
	   			this.salaryAdmissible = this.cumSalAdmissible;
	  		    this.employeeAmount = this.cumEmpAmount; 
	   		}
		}
		
		this.employerAmount =  this.employeeAmount ;
		
       	//2010.05.14
       	if ( this.cumSalaryAdmissible.compareTo( rpc_max_gain ) >= 0 )
			Amount_Rpc = rpc_max;
       	
        if (Amount_Rpc.compareTo( rpc_max ) > 0)
			Amount_Rpc = rpc_max;
        
        
	}   // calcul_rrq_rpc

	//
	// Read Cumulative information about a deduction for this Year..
	// 
	private BigDecimal get_deduction_cumulative_rpc( ) throws Exception
	{
		String query = null;
		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible"
			+ " from P_Payment_Deduction, P_Payment, P_Deduction, P_Deduction_Param "
			+ " where P_Payment_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID " 
			+ " and P_Payment.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID ()  
			+ " and P_Payment_Deduction.P_Period_ID <> " + this.Period.getP_Period_ID()			
			+ " and P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID "
			+ " and P_Payment.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
			+ " and P_Deduction_Param.P_Deduction_ID = P_Deduction.P_deduction_id "
			+ "  AND P_Deduction_Param.EffectIn in ( Select Max( EffectIn) FROM P_Deduction_PARAM WHERE P_Deduction_PARAM.P_Deduction_ID = P_Deduction.P_Deduction_ID and P_Deduction_PARAM.EffectIn <= " + DB.TO_DATE(Period.getEndDate() ) + ")"
			+ " and P_Deduction_Param.P_Method_Deduction_ID=102 "  // RPC
			
			
			;


		PreparedStatement pstmt = null;
		BigDecimal cumEmployeeAmount = ZERO;
			
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			cumEmployeeAmount =     rs.getBigDecimal("Employee_Part");
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
			
		return cumEmployeeAmount;
	}	

	//
	// Read Cumulative information about a deduction for this Year..
	// 
	private BigDecimal get_deduction_cumulative_rrq( ) throws Exception
	{
		String query = null;
		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible"
			+ " from P_Payment_Deduction, P_Payment, P_Deduction, P_Deduction_Param "
			+ " where P_Payment_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID " 
			+ " and P_Payment.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID ()  
			+ " and P_Payment_Deduction.P_Period_ID <> " + this.Period.getP_Period_ID()			
			+ " and P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID "
			+ " and P_Payment.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
			+ " and P_Deduction_Param.P_Deduction_ID = P_Deduction.P_deduction_id "
			+ "  AND P_Deduction_Param.EffectIn in ( Select Max( EffectIn) FROM P_Deduction_PARAM WHERE P_Deduction_PARAM.P_Deduction_ID = P_Deduction.P_Deduction_ID and P_Deduction_PARAM.EffectIn <= " + DB.TO_DATE(Period.getEndDate() ) + ")"
			+ " and P_Deduction_Param.P_Method_Deduction_ID=101 "  // RRQ
			
			;


		PreparedStatement pstmt = null;
		BigDecimal cumEmployeeAmount = ZERO;
			
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			cumEmployeeAmount =     rs.getBigDecimal("Employee_Part");
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
			
		return cumEmployeeAmount;
	}	


	
	//
	// Calcul RRQ and RPC
	//
	private void calcul_rrq_rpc ( boolean rrq ) throws Exception
	{
		// 2009.04.27
		// les indiens inscrit ne paie pas de RRQ ou de CPP.
		if ( this.Employee.isStatusIndian() )
			return;
		
		boolean federal = true;
		//
		// Québec ou Fédéral
		//
		if ( Region.getC_Region_ID() == QUEBEC )  
			federal = false;
		else
			federal = true;
		

		//+ 2008-08-21
		// S'il y a un montant fixe dans le dossier employee
		BigDecimal EmployeeAmount = EmployeeDeduction.getEmployeeAmount( Deduction_Param);
		BigDecimal EmployerAmount = EmployeeDeduction.getEmployerAmount( Deduction_Param); 

		if ( ( EmployeeAmount != null && EmployeeAmount.compareTo(Env.ZERO) != 0 ) || 
			 ( EmployerAmount != null && EmployerAmount.compareTo(Env.ZERO) != 0 )
			   )
		{
			this.employeeAmount = EmployeeAmount;
			this.employerAmount = EmployerAmount;
			return;
		}
		//-

		
		BigDecimal rpc_rate = ZERO;
		BigDecimal rpc_max_gain = ZERO;
		BigDecimal rpc_exemption = ZERO;
		BigDecimal rpc_max = ZERO;
		
		if ( federal )
		{
			rpc_rate      = (BigDecimal)taxTable.get( "RPC.1");
			rpc_max_gain  = (BigDecimal)taxTable.get( "RPC.2");
			rpc_exemption = (BigDecimal)taxTable.get( "RPC.3");
			rpc_max       = (BigDecimal)taxTable.get( "RPC.4");
		}
		else
		{
			rpc_rate      = (BigDecimal)taxTable.get( "RRQ.1");
			rpc_max_gain  = (BigDecimal)taxTable.get( "RRQ.2");
			rpc_exemption = (BigDecimal)taxTable.get( "RRQ.3");
			rpc_max       = (BigDecimal)taxTable.get( "RRQ.4");
			
		}

//		2017-05-28 Lire le salaire admissible sans le maximum
		getSalaryAdmissible( Deduction_Param );
		BigDecimal salAdm = salaryAdmissible ;

		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param , rpc_max_gain);

		// si on calcul du RRQ pour un employé dont la province d'imposition n'est pas le Québec
		// alors on ne considère pas d'exemption.
		if ( rrq && Region.getC_Region_ID() != QUEBEC )
			rpc_exemption = Env.ZERO;

		//2026-04-01
		if ( ! Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_Regular )  )
			rpc_exemption = Env.ZERO;
		
		
		rpc_rate = rpc_rate.divide( new BigDecimal( 100 ), 6, BigDecimal.ROUND_HALF_UP );

		//
		// Pour payé du RRQ ou RPC, il faut être agé entre 18 ans.
		// Lorsque l'employé atteint 18 ans dans l'année,
		// alors il faut modifier le maximum annuel au RRQ ou RPC selon le
		// mois où il atteint un ou l'autre.
		//
		// Exemple : 18 ans au mois de mars alors l'employé commence à cotisé
		//           au moins d'avril et le maximum est calculé comme suit:
		//           Max rrq = Max rrq * (nombre cotisable (9) / 12)
		//
		//	get_matrix ( P_Column_Adm_ID, qty, amount );
		
		
		// 18 Year old...
		// employé ne cotise pas le mois de son anniversaire.
       //		int age = Employee.getAge( Period.getStartDate());
//		int age = Employee.getAge( TimeUtil.addMonth( Period.getStartDate(), -1));
// 2009.10.27 
		int age = Employee.getAge( Payment.getPayDate() );
  	    BigDecimal ageRRQ = Employee.getAgeWithMonth( Payment.getPayDate() );


		if ( age <  18 )
		{
			P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol10-I" ) + " " + age, trxName);
			this.salaryAdmissible = Env.ZERO;	
			this.hoursAdmissible = Env.ZERO;
			return ;
		}

		// 2009.10.27 
  	    if ( ageRRQ.compareTo( new BigDecimal(18.00)) ==  0 && TimeUtil.getMonth( Employee.getBirthDate()) == TimeUtil.getMonth( Payment.getPayDate()))
//		if ( age ==  18 && TimeUtil.getMonth( Employee.getBirthDate()) == TimeUtil.getMonth( Payment.getPayDate()))
		{
			P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol10-I"   ) + " " + age, trxName);
			this.salaryAdmissible = Env.ZERO;	
			this.hoursAdmissible = Env.ZERO;
			return ;
			
		}

		
		if ( federal )
		{
//			age = Employee.getAge( TimeUtil.addMonth( Period.getEndDate(), -1));
//			age = Employee.getAge( TimeUtil.addMonth( Period.getEndDate(), -1));
			if ( age >=  70 )
			{
				P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol10-I" ) + " " + age, trxName);
				this.salaryAdmissible = Env.ZERO;	
				this.hoursAdmissible = Env.ZERO;

				return ;
			}		
		}
		else
		{
			P_Year Year = P_Year.get( Env.getCtx(), Period.getP_Year_ID(), null);
			Timestamp Date31Dec = TimeUtil.getDay(Year.getYear() -1, 12, 31);
	  	    BigDecimal ageRRQ2 = Employee.getAgeWithMonth( Date31Dec );

			if ( ageRRQ2.intValue() >=  72 )
			{
				P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol10-I" ) + " " + ageRRQ2.intValue(), trxName);
				this.salaryAdmissible = Env.ZERO;	
				this.hoursAdmissible = Env.ZERO;

				return ;
			}		
			
		}
			
		
		// Formula
		//
		// C = 0.0495 ( S3 - V/P ) until M - A5
		//
		
		BigDecimal sal = ZERO;
		BigDecimal tmp = ZERO;
		sal =  salaryAdmissible ;
		
		tmp =  rpc_exemption.divide( new BigDecimal( Frequency.getNumberOfPeriod()) , 2, BigDecimal.ROUND_HALF_UP);
		
		// si le montant d'exemption est plus petit que le salaire.
		if ( tmp.compareTo(sal) > 0 && sal.compareTo(ZERO) > 0)
			tmp = sal.setScale(2,BigDecimal.ROUND_HALF_UP);
		

		//+2011.07.19 RRQ
		if ( sal.compareTo( ZERO ) != 0) {
		//-2011.07.19 RRQ
			sal = sal.subtract( tmp ).setScale(2, BigDecimal.ROUND_HALF_UP);
			//2017-05-28 
			salAdm = salAdm.subtract( tmp ).setScale(2, BigDecimal.ROUND_HALF_UP);
		}		
//sal = sal.subtract( tmp ).setScale(2, BigDecimal.ROUND_HALF_UP);
/*
		if ( sal.compareTo( Env.ZERO ) == 0 )
		{
			this.salaryAdmissible = ZERO;
			this.hoursAdmissible = ZERO;
			return ;
		}
*/

		//2017-05-28
		//		this.employeeAmount = sal.multiply( rpc_rate );
		this.employeeAmount = salAdm.multiply( rpc_rate );

		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " sal " + sal +" employee amount " + this.employeeAmount + " employer amount " + employerAmount );
		
		
		// Read Cumulative information about a deduction for this Year..

		// Modifier 2007-11-01 pour géré quand un employé change du RRQ au RPC
	    // get_deduction_cumulative( );

//		Deduction    = P_Deduction.get( Env.getCtx(), Deduction_Param.getP_Deduction_ID(), trxName);

		//2015.09.11 le cumulatif de la déduction et non celui de la famille
		
		get_deduction_cumulative_family_rrq( Deduction.getP_Deduction_Family_ID() );
		
		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " cumulative " + cumulative.toString() ) ;

        Amount_Rpc = this.employeeAmount.multiply( new BigDecimal( Frequency.getNumberOfPeriod()));
        Amount_Rpc = Amount_Rpc.setScale(2, BigDecimal.ROUND_HALF_UP);
        
		//Modif pour 2024 
        Salary_Rpc = salaryAdmissible;
        
        
        //2015.11.09 gestion du changement de province.
		int nbrProvince = getNbrProvince ( );

		if ( nbrProvince > 1 && this.employeeAmount.add( this.cumEmployeeAmount  ).compareTo( rpc_max ) > 0)
		{
			if ( if_rrq_rpc() ) {
				rpc_max_gain  = (BigDecimal)taxTable.get( "RRQ.2");
				rpc_max       = (BigDecimal)taxTable.get( "RRQ.4");
				
			}
		}
			

        if ( this.employeeAmount.add( this.cumEmployeeAmount  ).compareTo( rpc_max ) > 0 )
		{
			this.employeeAmount = rpc_max.subtract( this.cumEmployeeAmount ); 
//			this.salaryAdmissible = rpc_max_gain.subtract( this.cumSalaryAdmissible  );

  		    //2008-06-10 calcul par périod d'origine demande le total de la période.
//  		    get_deduction_cumulative_per_rrq(  );
//  		    this.employeeAmount = this.employeeAmount.add(this.cumEmpAmount); 
			
//			Amount_Rpc = rpc_max;
		}

        
 /*       if (Amount_Rpc.compareTo( rpc_max ) > 0)
			Amount_Rpc = rpc_max;
*/			
	
/*        
		if ( this.employeeAmount.compareTo( ZERO ) < 0 )
		{
			this.employeeAmount = ZERO;
		}
*/	
		
		// 2008.10.02 adjuste employer part si l'employé n'a pas assez de salaire pour payé la déduction 
        // 2023-03-13 modifier l'arondi
		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_HALF_DOWN );
		
		
		if ( Period.getP_Period_ID() != Payment.getP_Period_ID() || ! Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_Regular))
		{
//	       	if ( getTotalSalaryAdm( Deduction_Param.getP_Column_Adm_ID() ).compareTo( rpc_max_gain ) > 0 )
		    if ( this.cumSalAdmissible.compareTo( rpc_max_gain ) == 0)
	   		{
	  		    //calcul par périod d'origine demande le total de la période.
	  		    get_deduction_cumulative_per_rrq(  );
	   			this.hoursAdmissible = this.cumHrsAdmissible;
	   			this.salaryAdmissible = this.cumSalAdmissible;
	  		    this.employeeAmount = this.cumEmpAmount; 
	   		}
		}
		
		this.employerAmount =  this.employeeAmount ;
/*		
       	//2010.05.14
       	if ( this.cumSalaryAdmissible.compareTo( rpc_max_gain ) >= 0 )
			Amount_Rpc = rpc_max;
       	
        if (Amount_Rpc.compareTo( rpc_max ) > 0)
			Amount_Rpc = rpc_max;
*/
        //2023-12-13 pour 2024
        Amount_Rpc_F5  = this.employeeAmount ;
		Amount_Rpc_QC = this.employeeAmount ;
        
	}   // calcul_rrq_rpc

	//
	// Calcul RRQ and RPC
	//
	private void calcul_rrq_rpc2 ( boolean rrq ) throws Exception
	{
		// les indiens inscrit ne paie pas de RRQ ou de CPP.
		if ( this.Employee.isStatusIndian() )
			return;
		
		boolean federal = true;
		//
		// Québec ou Fédéral
		//
		if ( Region.getC_Region_ID() == QUEBEC )  
			federal = false;
		else
			federal = true;
				
		BigDecimal rpc_rate = ZERO;
		BigDecimal rpc_max_gain = ZERO;
		BigDecimal rpc_max_cotisable = ZERO;
		BigDecimal rpc_max = ZERO;
		BigDecimal rpc_max_emr = ZERO;
		BigDecimal max = ZERO;
		BigDecimal max2 = ZERO;
		
		if ( federal )
		{
			rpc_rate          = (BigDecimal)taxTable.get( "RPC2.1");
			rpc_max_gain      = (BigDecimal)taxTable.get( "RPC2.2");
			rpc_max_cotisable = (BigDecimal)taxTable.get( "RPC2.3");
			rpc_max           = (BigDecimal)taxTable.get( "RPC2.4");
			rpc_max_emr       = (BigDecimal)taxTable.get( "RPC2.5");
			max               = (BigDecimal)taxTable.get( "RPC2.6");
			max2              = (BigDecimal)taxTable.get( "RPC2.6");
		}
		else
		{
			rpc_rate          = (BigDecimal)taxTable.get( "RRQ2.1");
			rpc_max_gain      = (BigDecimal)taxTable.get( "RRQ2.2");
			rpc_max_cotisable = (BigDecimal)taxTable.get( "RRQ2.3");
			rpc_max           = (BigDecimal)taxTable.get( "RRQ2.4");
			rpc_max_emr       = (BigDecimal)taxTable.get( "RRQ2.5");
			max               = (BigDecimal)taxTable.get( "RRQ2.6");
			max2              = (BigDecimal)taxTable.get( "RRQ2.6");
		
		}

//2024-09-25
		//        get_deduction_cumulative_family( Deduction.getP_Deduction_Family_ID());
        
        get_deduction_cumulative_family_rrq2( Deduction.getP_Deduction_Family_ID() );
        // Si l'employé n'a pas encore atteint le maximum.
        
        BigDecimal tmpSal = getSalary_Rpc();
        if ( tmpSal == null) tmpSal = Env.ZERO;
        
        if ( this.cumSalaryAdmissible.add( tmpSal ).compareTo( max2) < 0 )
        {
			this.salaryAdmissible = Env.ZERO;	
			this.hoursAdmissible = Env.ZERO;
			this.employeeAmount = Env.ZERO;
			this.employerAmount = Env.ZERO;
        	return;
        	
        }
/*
        if ( this.cumSalaryAdmissible.compareTo( rpc_max_gain) >= 0 )
        {
			this.salaryAdmissible = Env.ZERO;	
			this.hoursAdmissible = Env.ZERO;
			this.employeeAmount = Env.ZERO;
			this.employerAmount = Env.ZERO;
        	return;
        	
        }
  */  
		getSalaryAdmissible( Deduction_Param );
		BigDecimal salAdm = salaryAdmissible ;
        
        if ( this.cumSalaryAdmissible.compareTo( max) < 0 
        	&& this.cumSalaryAdmissible.add( tmpSal ).compareTo( max) >= 0 )
        {
        	this.salaryAdmissible = this.salaryAdmissible.subtract(tmpSal);
        	salAdm = salAdm.subtract(tmpSal);
        }
        

		//
		// Recherche les montants admissible /
//		getSalaryAdmissible( Deduction_Param , rpc_max_gain);
		this.cumEmployeeAmount =     ZERO;
		this.cumEmployerAmount =     ZERO;
		this.cumSalaryAdmissible =   ZERO;
		this.cumHoursAdmissible =    ZERO;
		// si le maximum du salaire admissible a été atteind.
		get_deduction_cumulative( );
		
        if ( this.cumSalaryAdmissible.add( salAdm ).compareTo( rpc_max_cotisable) > 0 )
        {
        	this.salaryAdmissible = rpc_max_cotisable.subtract(this.cumSalaryAdmissible);
        	salAdm = rpc_max_cotisable.subtract(this.cumSalaryAdmissible);
        }        
		
		
		rpc_rate = rpc_rate.divide( new BigDecimal( 100 ), 6, BigDecimal.ROUND_HALF_UP );

		int age = Employee.getAge( Payment.getPayDate() );
		
		// Les personnes de 18 ans et mons ne cotiser pas au RRQ / RPC.
		if ( age <  18 )
		{
			P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol10-I"  ) + " " + age, trxName);

			this.salaryAdmissible = Env.ZERO;	
			this.hoursAdmissible = Env.ZERO;
			return ;
		}

  	    BigDecimal ageRRQ = Employee.getAgeWithMonth( Payment.getPayDate() );


  	    if ( ageRRQ.compareTo( new BigDecimal(18.00)) ==  0 && TimeUtil.getMonth( Employee.getBirthDate()) == TimeUtil.getMonth( Payment.getPayDate()))
		{
			P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol10-I" ) + " " + age , trxName);
			this.salaryAdmissible = Env.ZERO;	
			this.hoursAdmissible = Env.ZERO;
			return ;
			
		}

  	    // modif 2024 pour le provincail 73 ans
		if ( ! federal )
		{
			// Les personnes de plus de 73 ans ne cotiser pas au  RRQ.
			// Votre employé atteint l’âge de 73 ans – retenez les cotisations au RPC jusqu'à la dernière période de paie du mois où
			if ( age >=  73 )
			{
				P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol10-I" ) + " " + age, trxName);

				this.salaryAdmissible = Env.ZERO;	
				this.hoursAdmissible = Env.ZERO;

				return ;
			}		
		}
		
		
		if ( federal )
		{
			// Les personnes de plus de 70 ans ne cotiser pas au RPC
			if ( age >=  70 )
			{
				P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol10-I" ) + " " + age, trxName);

				this.salaryAdmissible = Env.ZERO;	
				this.hoursAdmissible = Env.ZERO;

				return ;
			}		
		}
		
		
		this.employeeAmount = salAdm.multiply( rpc_rate ).setScale(2, BigDecimal.ROUND_HALF_UP);

        
        // Cumulatif pour la 
//        get_deduction_cumulative_family( Deduction.getP_Deduction_Family_ID() );
        
        // total salaire adm pour la gestion du maximum lors de correction dans le passé 
//        BigDecimal TotalSalaryAdm =  getTotalSalaryAdm( Deduction_Param.getP_Column_Adm_ID() );

        Amount_Rpc2 = this.employeeAmount.multiply( new BigDecimal( Frequency.getNumberOfPeriod()));
		
        if ( this.employeeAmount.add( this.cumEmployeeAmount  ).compareTo( rpc_max ) > 0 )
		{
			this.employeeAmount = rpc_max.subtract( this.cumEmployeeAmount ); 

			Amount_Rpc2 = rpc_max;
		}

        // 2025-10-06
        BigDecimal b2 = ( this.employeeAmount.add( this.cumEmployeeAmount  ).subtract(rpc_max) );
        if ( b2.abs().compareTo( new BigDecimal( 0.10) ) <= 0 )
		{
			this.employeeAmount = rpc_max.subtract( this.cumEmployeeAmount ); 
			Amount_Rpc2 = rpc_max;
		}

        if ( this.cumSalaryAdmissible.compareTo( rpc_max_cotisable ) >= 0  && this.employeeAmount.compareTo( ZERO ) == 0 )
        {
        	Amount_Rpc2 = ZERO;
			this.salaryAdmissible = Env.ZERO;	
			this.hoursAdmissible = Env.ZERO;
			this.employeeAmount = Env.ZERO;
			this.employerAmount = Env.ZERO;
        	return;
        	
        }

        
        
        
        if (Amount_Rpc2.compareTo( rpc_max ) > 0)
			Amount_Rpc2 = rpc_max;
	
		
		// 2008.10.02 adjuste employer part si l'employé n'a pas assez de salaire pour payé la déduction 
		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_DOWN );
		
		
		if ( Period.getP_Period_ID() != Payment.getP_Period_ID() || ! Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_Regular))
		{
		    if ( this.cumSalAdmissible.compareTo( rpc_max_gain ) == 0)
	   		{
	  		    //calcul par périod d'origine demande le total de la période.
	  		    get_deduction_cumulative_per_rrq(  );
	   			this.hoursAdmissible = this.cumHrsAdmissible;
	   			this.salaryAdmissible = this.cumSalAdmissible;
	  		    this.employeeAmount = this.cumEmpAmount; 
	   		}
		}
		
		this.employerAmount =  this.employeeAmount ;
/*		
       	//2010.05.14
       	if ( this.cumSalaryAdmissible.compareTo( rpc_max_gain ) >= 0 )
			Amount_Rpc2 = rpc_max;
       	
        if (Amount_Rpc2.compareTo( rpc_max ) > 0)
			Amount_Rpc2 = rpc_max;
        */
        
        Amount_Rpc2 = this.employeeAmount ;
        
        Salary_Rpc2 = salAdm;
        
        
        
	}   // calcul_rrq_rpc2

		
	

	//
	// Régime québécois d'assurance parental
	//
	private void calcul_rqap (  ) throws Exception
	{

		this.salaryAdmissible = ZERO;
		this.hoursAdmissible = ZERO;
		this.employeeAmount = ZERO;
		this.employerAmount = ZERO;

//		if ( ! ( Region.getC_Region_ID() == QUEBEC ) )  
//			return;
	
		

		// Montant maximal des cotisations de l'employé au RQAP
		BigDecimal rqap_max_annual_yee    = ZERO;
		// Montant maximal des cotisations de l'employeur au RQAP
		BigDecimal rqap_max_annual_yer    = ZERO;
        // Revenus maximum assurables
		BigDecimal MaxRevenusAss = ZERO;
        // Taux employé et employeur
		BigDecimal rqap_rate_employee = ZERO;
		BigDecimal rqap_rate_employer = ZERO;
		
		// 
		MaxRevenusAss      = (BigDecimal)taxTable.get( "RQAP.1");
		rqap_rate_employee = ((BigDecimal)taxTable.get( "RQAP.2")).divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);
		rqap_rate_employer = ((BigDecimal)taxTable.get( "RQAP.4")).divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);
		rqap_max_annual_yee  = (BigDecimal)taxTable.get( "RQAP.3");
		rqap_max_annual_yer  = (BigDecimal)taxTable.get( "RQAP.5");

		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param , MaxRevenusAss);
		
		if ( salaryAdmissible == null )
		{
			this.employeeAmount = ZERO;
			this.employerAmount = ZERO;
		}
		else
		{
			this.employeeAmount = salaryAdmissible.multiply( rqap_rate_employee );
			this.employerAmount = salaryAdmissible.multiply( rqap_rate_employer );
		}
		
		// Read Cumulative information about a deduction for this Year..
//		Deduction    = P_Deduction.get( Env.getCtx(), Deduction_Param.getP_Deduction_ID(), trxName);
        get_deduction_cumulative_family( Deduction.getP_Deduction_Family_ID() );

        // Conserve le montant employé RQAP pour crédit impôt (K2)
		Amount_RQAP = this.employeeAmount.multiply( new BigDecimal( Frequency.getNumberOfPeriod()) );
		Amount_RQAP = Amount_RQAP.setScale(2, BigDecimal.ROUND_HALF_UP);

		// Si l'employé atteint son maximum
		if ( (this.employeeAmount.add( this.cumEmployeeAmount )).compareTo( rqap_max_annual_yee ) > 0 )
		{
			this.employeeAmount = rqap_max_annual_yee.subtract( this.cumEmployeeAmount );
			Amount_RQAP = rqap_max_annual_yee;

			//2008-06-10 calcul par périod d'origine demande le total de la période.
	//		get_deduction_cumulative_per(  );
	//		this.employeeAmount = this.employeeAmount.add(this.cumEmpAmount); 
			
		}
		
		//2010.10.12
		BigDecimal b = (this.employeeAmount.add( this.cumEmployeeAmount )).subtract(rqap_max_annual_yee);  
		if  ( b.abs().compareTo( new BigDecimal(.20)) <= 0 ) 
			this.employeeAmount = rqap_max_annual_yee.subtract( this.cumEmployeeAmount ); 

		BigDecimal b2 = (this.employerAmount.add( this.cumEmployerAmount )).subtract(rqap_max_annual_yer);  
		if  ( b2.abs().compareTo( new BigDecimal(.20)) <= 0 ) 
			this.employerAmount = rqap_max_annual_yer.subtract( this.cumEmployerAmount ); 

      	if ( this.cumSalaryAdmissible.compareTo( MaxRevenusAss ) >= 0 )
 			Amount_RQAP = rqap_max_annual_yee;

        // On s'assure que le crédit RQAP ne dépasse pas le max
		if ( Amount_RQAP.compareTo( rqap_max_annual_yee ) > 0)
			Amount_RQAP = rqap_max_annual_yee;
		
		// Si l'employeur atteint le maximum
		if ( Period.getP_Period_ID() != Payment.getP_Period_ID() )
		{
			if ( (this.employerAmount.add( this.cumEmployerAmount )).compareTo( rqap_max_annual_yer ) > 0 )
			{
				this.employerAmount = rqap_max_annual_yer.subtract( this.cumEmployerAmount );

			}
		}

		this.employeeAmount = this.employeeAmount.setScale( 2, BigDecimal.ROUND_HALF_UP  );
		this.employerAmount = this.employerAmount.setScale( 2, BigDecimal.ROUND_HALF_UP  );

		
	}   // RQAP
	
	
	
	//
	// employment insurance ( assurance emploie )
	//
	private void calcul_employment_insurance (  ) throws Exception
	{
		
		BigDecimal ei_rate_employee = ZERO;
		BigDecimal ei_rate_employer = ZERO;
		BigDecimal ei_max_gain      = ZERO;
		BigDecimal ei_max_annual    = ZERO;

		ei_max_gain      = (BigDecimal)taxTable.get( "AE.1");

		if ( Region.getC_Region_ID() == QUEBEC )  
		{
//			ei_max_gain      = (BigDecimal)taxTable.get( "AE.1");
			ei_rate_employee = (BigDecimal)taxTable.get( "AE.2");
			ei_max_annual    = (BigDecimal)taxTable.get( "AE.3");
		}
		else
		{
			ei_rate_employee = (BigDecimal)taxTable.get( "AE.5");
			ei_max_annual    = (BigDecimal)taxTable.get( "AE.6");
		}

		
		ei_rate_employee = ei_rate_employee.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP );

		P_Employer Employer = P_Employer.get( Env.getCtx(), Payment.getP_Employer_ID(), trxName ); 
		if ( Employer.isReducedEI() )
		{
			ei_rate_employer = (BigDecimal)taxTable.get( "AE.42");
			// TODO Hardcoder ( Acasta )
			if ( Employee.getAD_Org_ID() == 1000011 || Employee.getAD_Org_ID() == 1000012 )
				ei_rate_employer = (BigDecimal)taxTable.get( "AE.43");
		}	
		else
			ei_rate_employer = (BigDecimal)taxTable.get( "AE.41");


//		ei_rate_employer = Deduction_Param.getEmployerRate();

		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param , ei_max_gain );

		BigDecimal Amount_Employment_Salary = this.salaryAdmissible;
		BigDecimal TotalSalaryAdm =  getTotalSalaryAdm( Deduction_Param.getP_Column_Adm_ID() );
        //  s'il a dépassé le maximum, il ne doit pas y avoir de remboursement.
 /*
		if ( TotalSalaryAdm.compareTo( ei_max_gain ) > 0 )
   		{
  		    get_deduction_cumulative_per(  );
       		if ( Payment.getP_Period_ID() == Period.getP_Period_ID())
       		{
           		get_deduction_cumulative( );
           		Amount_Employment_Salary = ei_max_gain.subtract( this.cumSalaryAdmissible );
           		if ( Amount_Employment_Salary.compareTo(Env.ZERO) <= 0 )
           		{
           			Amount_Employment_Salary = this.cumSalAdmissible;
           		}
       		}
       		else
       		{
      		    Amount_Employment_Salary = this.cumSalAdmissible;
      		    this.employeeAmount = this.cumEmpAmount; 
      		    this.employerAmount = this.cumEmprAmount; 
      		    return;
      		}
       		
   		}
*/
		int nbrProvince = getNbrProvince ( );

		// Si l'employé a changer de province, il doit quand meme payé de l'assuranc emploi pour le total du maximum assurable
		if ((  Region.getC_Region_ID() == QUEBEC && nbrProvince > 1))
		{
			ei_max_annual    = (BigDecimal)taxTable.get( "AE.6");
		}
       	//-2009.11-30
		
	
		if ( Amount_Employment_Salary == null )
		{
			this.employeeAmount = ZERO;
			this.employerAmount = ZERO;
		}
		else
		{
			this.employeeAmount = Amount_Employment_Salary.multiply( ei_rate_employee );
			this.employerAmount = this.employeeAmount.multiply( ei_rate_employer );
		}
		
		// Read Cumulative information about a deduction for this Year..
//		Deduction    = P_Deduction.get( Env.getCtx(), Deduction_Param.getP_Deduction_ID(), trxName);
        get_deduction_cumulative_family( Deduction.getP_Deduction_Family_ID() );

		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " cumulative " + cumulative.toString() ) ;
		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " maximum " + ei_max_annual ) ;

		Amount_Employment_Insurance = this.employeeAmount.multiply( new BigDecimal( Frequency.getNumberOfPeriod()) );


       	//+2009.11-30

//		if ((  Region.getC_Region_ID() == QUEBEC && this.cumEmployeeAmount.compareTo( (BigDecimal)taxTable.get( "AE.3") ) >= 0 && nbrProvince > 1))
/*		if ((  Region.getC_Region_ID() == QUEBEC && this.cumSalaryAdmissible.compareTo( ei_max_gain ) >= 0 && nbrProvince > 1))
		{

			this.employeeAmount = Env.ZERO;
			this.employerAmount = Env.ZERO;
			return;
		}
*/
 	if ( this.employeeAmount.add( this.cumEmployeeAmount  ).compareTo( ei_max_annual ) > 0 ||
			(this.employeeAmount.add( this.cumEmployeeAmount  ).compareTo( ei_max_annual ) < 0 && TotalSalaryAdm.compareTo( ei_max_gain ) > 0 )	)
//		if ( ( this.cumEmployeeAmount  ).compareTo( ei_max_annual ) > 0 ||
//				(( this.cumEmployeeAmount  ).compareTo( ei_max_annual ) < 0 && TotalSalaryAdm.compareTo( ei_max_gain ) > 0 )	)
		{
			
			this.employeeAmount = ei_max_annual.subtract( this.cumEmployeeAmount ); 

  		    //2008-06-10 calcul par périod d'origine demande le total de la période.
 // 		    get_deduction_cumulative_per(  );
 // 		    this.employeeAmount = this.employeeAmount.add(this.cumEmpAmount); 

			this.employerAmount = this.employeeAmount.multiply( ei_rate_employer );

			Amount_Employment_Insurance = ei_max_annual;

		}

		//+ Magnetho 262
		// Validation du maximum du salaire admissible
        //
//		if (( this.cumSalaryAdmissible.add( this.salaryAdmissible )).compareTo( ei_max_gain ) > 0)
//			this.salaryAdmissible = ei_max_gain.subtract(this.cumSalaryAdmissible );
		//- Magnetho 262

		
		
/*		if ( this.employeeAmount.compareTo( ZERO ) < 0 )
		{
			this.employeeAmount = ZERO;
			this.employerAmount = ZERO;
		}
*/		

		this.employeeAmount = this.employeeAmount.setScale( 2, BigDecimal.ROUND_HALF_UP  );
		this.employerAmount = this.employerAmount.setScale( 2, BigDecimal.ROUND_HALF_UP  );

		// 2008.10.02 adjuste Maximum employer 
		// maximum employer - Round if +/- 0.10$
		BigDecimal maxEmployer = ei_max_annual.multiply( ei_rate_employer ).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		BigDecimal b = (this.employerAmount.add( cumEmployerAmount )).subtract(maxEmployer);  
		if  ( b.abs().compareTo( new BigDecimal(1.20)) <= 0 ) 
			this.employerAmount = maxEmployer.subtract( this.cumEmployerAmount ); 

		BigDecimal b2 = (this.employeeAmount.add( cumEmployeeAmount )).subtract(ei_max_annual);  
		if  ( b2.abs().compareTo( new BigDecimal(1.20)) <= 0 ) 
			this.employeeAmount = ei_max_annual.subtract( this.cumEmployeeAmount ); 

		/*
		//2015-11-01 ajustement si l'employé ou l'employeur dépasse le maximum
		if  ( cumEmployerAmount.compareTo( maxEmployer ) > 0 ) 
			this.employerAmount = maxEmployer.subtract( this.cumEmployerAmount ); 
*/
		if  ( cumEmployeeAmount.compareTo( ei_max_annual) > 0 ) 
			this.employeeAmount = ei_max_annual.subtract( this.cumEmployeeAmount ); 

		
		
		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " employee amount " + this.employeeAmount + " employer amount " + employerAmount );

		getSalaryAdmissible( Deduction_Param , null );

		//2010.05.14
		if (( this.cumSalaryAdmissible.add( this.salaryAdmissible )).compareTo( ei_max_gain ) >= 0)
			Amount_Employment_Insurance = ei_max_annual;

		if ( Amount_Employment_Insurance.compareTo( ei_max_annual ) > 0)
			Amount_Employment_Insurance = ei_max_annual;

	}   // employment insurance
	
		
	
	
	//
	// calcul_insurance
	//
	private void calcul_insurance( ) throws Exception
	{
	
		if ( Deduction_Param.getMaximumAge() != null && Deduction_Param.getMaximumAge().compareTo(Env.ZERO) != 0 )
		{
			BigDecimal age = Employee.getAgeWithMonth( Period.getPayDate() );

//			BigDecimal age = new BigDecimal( Employee.getAge( Period.getPayDate() ));
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
		
		if ( this.salaryAdmissible != null && Deduction_Param.getMethod_Rounding().equals( new String( "T" ) ) )
		{
			this.salaryAdmissible = this.salaryAdmissible.multiply(new BigDecimal( 1000 ));
			//Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + "- Round salaryAdmissible = " + this.salaryAdmissible);
		}


		if ( this.employeeAmount.compareTo(Env.ZERO) == 0 && this.employerAmount.compareTo(Env.ZERO) == 0 )
		{
			this.salaryAdmissible = Env.ZERO;
		}
		
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
		
		
		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		this.employerAmount = this.employerAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );

	}   // calcul_insurance



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
	
	//Invalité courte durée
	private void calcul_insurance_M80(  ) throws Exception
	{
		// Recherche des montants admissible /
		// Search admisible amount
//		BigDecimal insurableSalary = Employee.getInsurableSalary( Period.getP_Period_ID() ).divide(new BigDecimal(Frequency.getNumberOfPeriod()),2,BigDecimal.ROUND_HALF_UP );  
		BigDecimal insurableSalary = Employee.getInsurableSalary( Period.getP_Period_ID(), Deduction_Param.getP_Deduction_ID() ).divide(new BigDecimal(Frequency.getNumberOfPeriod()),2,BigDecimal.ROUND_HALF_UP );  
		P_Employee_Profile EmployeeProfile = P_Employee_Profile.getProfileInsurance( Env.getCtx(), Employee.getP_Employee_ID(), Period.getEndDate(), trxName);

		if ( EmployeeProfile == null )
			P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "L'employé "+Employee.getValue()+" n'a pas de profil d'assurance " + Deduction.getName() , trxName);

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
		
		//2023-05-10 modifier ROUND_UP

		this.employeeAmount = this.employeeAmount.setScale(2,BigDecimal.ROUND_UP);
		this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_UP);
	
			
		
	}

	//Assurance Maladie
	private void calcul_insurance_M81( Boolean taxable_Benefit ) throws Exception
	{
		
		if ( Deduction_Param.getMaximumAge() != null && Deduction_Param.getMaximumAge().compareTo(Env.ZERO) != 0 )
		{
			BigDecimal age = Employee.getAgeWithMonth( Period.getPayDate() );
//			BigDecimal age = new BigDecimal( Employee.getAge( Period.getPayDate() ));
			
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
		
		
		if ( Deduction.getValue().equals("DI21G7") )
		{
			this.employerAmount = this.employeeAmount.divide( new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP );
			this.employeeAmount = this.employeeAmount.divide( new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP );
		}
		else
		{
			// Groupe 8 et 9
			if ( EmployeeProfile != null && (EmployeeProfile.getP_Profile_ID() == 1100068 || EmployeeProfile.getP_Profile_ID() == 1100069 ))
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

	//Assurance Maladie avec maximum employeur
	private void calcul_insurance_M83(  ) throws Exception
	{
		
		if ( Deduction_Param.getMaximumAge() != null && Deduction_Param.getMaximumAge().compareTo(Env.ZERO) != 0 )
		{
			BigDecimal age = Employee.getAgeWithMonth( Period.getPayDate() );
//			BigDecimal age = new BigDecimal( Employee.getAge( Period.getPayDate() ));
			
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

		if ( Deduction_Param.getEmployerRate() != null )
		{
			this.employerAmount = this.employeeAmount.multiply(Deduction_Param.getEmployerRate());
			this.employeeAmount = this.employeeAmount.subtract(this.employerAmount);
		}

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
		
		//Si exempter.
		BigDecimal maxEmployer = new BigDecimal(16.38);
		
		// Individuel
		if ( EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_Individual))
		{
			maxEmployer = new BigDecimal(23.77);
			
		}
		if ( EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_SingleParent))
		{
			//Mono
			maxEmployer = new BigDecimal(25.15);
			
		}
		if ( EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_Family))
		{
			//Famil
			maxEmployer = new BigDecimal(27.69);
			
		}
		if ( EmployeeDeduction.getTypeRate().equals( P_Employee_Deduction.TYPERATE_Couple))
		{
			//Famil
			maxEmployer = new BigDecimal(27.69);
		
		}

		
		
		
		
		if ( this.employerAmount.compareTo( maxEmployer ) > 0 )
		{
			this.employeeAmount = this.employeeAmount.add( this.employerAmount.subtract( maxEmployer) );
			this.employerAmount = maxEmployer;
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

	private BigDecimal getInsuranceMaxEmployer( P_Period Period, P_Employee Employee, P_Payment Payment, Hashtable<Integer,BigDecimal> s_cacheMaxEmployer, String trxName)
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
	private BigDecimal valideMaxEmployer( P_Period Period, P_Employee Employee, P_Payment Payment, P_Employee_Deduction EmployeeDeduction, P_Deduction Deduction, BigDecimal employerAmt, BigDecimal employeeAmt, Boolean taxableBenefit, String trxName )
	{
		
		Integer key = new Integer ( Payment.getP_Payment_ID() );

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

		// Groupe 8 et 9
		if ( ! ( EmployeeProfile.getP_Profile_ID() == 1100068 || EmployeeProfile.getP_Profile_ID() == 1100069 ))
		{
			return null;
		}

		if (Deduction.getValue().equals("DI11") || Deduction.getValue().equals("DI12")
				|| Deduction.getValue().equals("DI20"))
		{
			return null;
		}

		log.info("DEBUG valideMaxEmployer CalculNet key " + key + " Employee : " + Employee.toString() + " Deduction : " + Deduction.getValue() + " Profile : " + EmployeeProfile.getP_Profile_ID() ) ;
		
		
		BigDecimal amt = null;	
		
		BigDecimal maxEmr = getInsuranceMaxEmployer( Period, Employee, Payment, s_cacheMaxEmployer , trxName);
		
		log.info("DEBUG valideMaxEmployer " + maxEmr );
		
		if ( insurance_Program != null && maxEmr != null && maxEmr.compareTo( Env.ZERO) != 0  )
		{

			BigDecimal cumAmt = Env.ZERO;
			BigDecimal cumEmpAmt = Env.ZERO;
			try
			{
//				cumAmt = get_deduction_cumulative_Insurable();
				cumAmt = (BigDecimal)s_cacheEmployerInsurance.get(key);
				if ( cumAmt == null) cumAmt = Env.ZERO;

				cumEmpAmt = (BigDecimal)s_cacheEmployeeInsurance.get(key);
				if ( cumEmpAmt == null) cumEmpAmt = Env.ZERO;

			}
			catch (Exception e)
			{
				System.err.println("getStartPeriod - get - " + e);
			}

			 // si déja au maximum
			if ( cumAmt.compareTo( maxEmr) == 0 && employerAmount.compareTo(Env.ZERO) != 0)
			{
				if  ( EmployeeProfile.getP_Profile_ID() == 1100068 || EmployeeProfile.getP_Profile_ID() == 1100069)
				{
					if ( EmployeeDeduction.isExonerated() )
					{
						employeeAmount = Env.ZERO;
						employerAmount = Env.ZERO;
					}
					else
					{
						employeeAmount = employerAmount;
						employerAmount = Env.ZERO;
					}
					
					
				}
				else
					return Env.ZERO;
			}
				
			
			// calcul profil 9 exempt.
			if ( EmployeeProfile.getP_Profile_ID() == 1100069 && taxableBenefit == false )
			{
				BigDecimal t_cumAmt = Env.ZERO;
				BigDecimal t_cumEmpAmt = Env.ZERO;
				if ( insurance_Program != null && maxEmr != null && maxEmr.compareTo( Env.ZERO) != 0  )
				{

					try
					{
						t_cumAmt = (BigDecimal)s_cacheEmployerInsuranceG9.get(key);
						if ( t_cumAmt == null) t_cumAmt = Env.ZERO;

						t_cumEmpAmt = (BigDecimal)s_cacheEmployeeInsuranceG9.get(key);  
						if ( t_cumEmpAmt == null) t_cumEmpAmt = Env.ZERO;

					}
					catch (Exception e)
					{
						System.err.println("getStartPeriod - get - " + e);

					}
				}
				
				if ( Deduction.getValue().equals( "DI26G9" )
						|| Deduction.getValue().equals( "DI27G9" )   
						|| Deduction.getValue().equals( "DI23G9" )   
						)
				{
					BigDecimal amttmp = (t_cumEmpAmt).divide(new BigDecimal( 2 ),2,BigDecimal.ROUND_HALF_UP );

					BigDecimal t_cumErAmt = (BigDecimal)s_cacheEmployerInsuranceG9r.get(key);
					if ( t_cumErAmt == null) t_cumErAmt = Env.ZERO;

					log.info("DEBUG Calcul G9 " + key + " Employee : " + Employee.toString() + " Deduction : " + Deduction.getValue() + " amttmp : "  + amttmp.toString() + " t_cumErAmt " + t_cumErAmt.toString() ) ;

					
					if ( employerAmt.subtract(amttmp).compareTo(Env.ZERO) >= 0 &&  maxEmr.compareTo(t_cumAmt) > 0)
					{
						employeeAmount = employerAmt.subtract(amttmp);
						employerAmount = amttmp;
					}
/*					else if ( amttmp.compareTo(t_cumAmt) > 0 ) //&& this.employerAmount.compareTo(Env.ZERO) == 0 )
					{
					//	BigDecimal t_a = this.employeeAmount;
						this.employerAmount = amttmp.subtract(t_cumAmt);
						if ( this.employeeAmount.compareTo(Env.ZERO) != 0 )
							this.employeeAmount = this.employeeAmount.subtract( this.employerAmount );
						else
							this.employeeAmount = amttmp.subtract( this.employerAmount );
					}
*/					
/*					
					//ICI pour 16421
					else if ( amttmp.compareTo(t_cumAmt) < 0 && employerAmt.compareTo(Env.ZERO) != 0 )
					{
					  // amttmp = 11.51
					  // t_cumAmt = 19.15
					  // employerAmount et decrait aller dans employé
						this.employeeAmount = employerAmt;
						this.employerAmount = Env.ZERO;
					}
*/
					// Pour 10730
/*					else if ( amttmp.compareTo(t_cumErAmt.add(employerAmt)) > 0 )
					{
					//	BigDecimal t_a = this.employeeAmount;
						this.employerAmount = amttmp.subtract(t_cumAmt);
						this.employeeAmount = this.employeeAmount.subtract( this.employerAmount );
					}
*/		
					else if ( amttmp.compareTo(t_cumErAmt) > 0 && (amttmp.subtract(t_cumAmt)).compareTo(Env.ZERO) > 0 )
					{
						employerAmount = amttmp.subtract(t_cumAmt);
//						this.employeeAmount = this.employeeAmount.subtract( this.employerAmount );
						if ( employeeAmount.compareTo(Env.ZERO) != 0 )
							employeeAmount = employeeAmount.subtract( employerAmount );
						else
							employeeAmount = amttmp.subtract( employerAmount );						
					}
					
					//ICI pour 16421
					else if ( amttmp.compareTo(t_cumErAmt) == 0 )
					{
					  // amttmp = 11.51
					  // t_cumAmt = 19.15
					  // employerAmount et decrait aller dans employé
						employeeAmount = employerAmt.add( employeeAmount );
						employerAmount = Env.ZERO;
					}
					
					else if ( employerAmt.compareTo(Env.ZERO) != 0 )
					{
						employeeAmount = Env.ZERO;
						employerAmount = employerAmt;
					}
					s_cacheEmployerInsuranceG9r.remove(key);
					s_cacheEmployerInsuranceG9r.put (key, t_cumErAmt.add(employerAmount));
				}
			}
			
			// si dépasse le maximum
//			if ( cumAmt.add( employerAmt ).compareTo( maxEmr) > 0 )
			BigDecimal tmp_amt = ((cumEmpAmt.add(employeeAmt)).divide(new BigDecimal( 2 ),2,BigDecimal.ROUND_HALF_UP ));

			// GROUP 8 pas de division par 2
			if  ( EmployeeProfile.getP_Profile_ID() == 1100068  )
			{
//				tmp_amt = ((cumEmpAmt.add(employerAmt)));
				if ( Deduction.getValue().equals("DI27G8")  )
				{
					if ( cumAmt.add(employerAmt ).compareTo(maxEmr) > 0   )
					{
						amt = maxEmr.subtract(cumAmt);
						employerAmount = maxEmr.subtract(cumAmt);
						employeeAmount = employerAmt.subtract( amt );
					}
						
				}
				if ( Deduction.getValue().equals("DI21G8") && cumAmt.compareTo(maxEmr) != 0  )  
				{
					tmp_amt = ((cumEmpAmt.add(employerAmt))).add(employeeAmt);	
					
					//2023-05-29				
					if ( tmp_amt.compareTo(employeeAmt) > 0)
						tmp_amt = employeeAmt;
					
				}
				else if ( Deduction.getValue().equals("DI21G8") && cumAmt.compareTo(maxEmr) == 0  )
				{
					//2023-05-29				
					tmp_amt = Env.ZERO; 
					tmp_amt = employeeAmt;

				}
				
				else
				{
					tmp_amt = ((cumEmpAmt.add(employerAmt)));	
				}
				
			}
				
//				
			
			
			
			if ( maxEmr.compareTo( Env.ZERO) != 0 && tmp_amt.compareTo( maxEmr) > 0 )
			{ //2024-04-23
				amt = maxEmr.subtract(cumAmt);
				if  ( EmployeeProfile.getP_Profile_ID() == 1100068 || EmployeeProfile.getP_Profile_ID() == 1100069)
				{
					if ( EmployeeDeduction.isExonerated() )
					{
						employeeAmount = Env.ZERO;
						amt = Env.ZERO;
					}
					else
					{
//2022-09-29						
//
						// Groupe 8
						if ( EmployeeProfile.getP_Profile_ID() == 1100068 )
						{
// 2023-06-13						
							//2023-06-20
							if ( amt.compareTo(Env.ZERO) == 0)
							{
								// NE RIEN FAIRE.
							}
							else
							{
//								if ( Employee.getValue().equals("1030") 
//										|| Employee.getValue().equals("1015")
//										)
								if ( ! Deduction.getValue().equals("DI27G8") )
								{
									employeeAmount = tmp_amt.subtract( amt );
								}
//								else
//									employeeAmount = tmp_amt.subtract( maxEmr );
								
							}
								
//							
						}
						else
						{
						
							employeeAmount = employeeAmount.subtract(amt );
							if ( employeeAmount.compareTo(Env.ZERO) < 0 )
								employeeAmount = Env.ZERO;
						}
							
								
					}
					
				}
					
				else
				{
					employeeAmount = employeeAmount.subtract(amt );
				}

				s_cacheEmployerInsurance.remove(key);
				s_cacheEmployerInsurance.put (key, cumAmt.add(amt) );

				if (  taxableBenefit == true )
				{
					s_cacheEmployerInsuranceG9.remove(key);
//					s_cacheEmployerInsuranceG9.put (key, cumAmt.add(employerAmt));
					s_cacheEmployerInsuranceG9.put (key, cumAmt.add(amt));
//					s_cacheEmployeeInsuranceG8.remove(key);
//					s_cacheEmployeeInsuranceG8.put (key, cumEmpAmt.add(employeeAmt));
				}
			}
			else
			{
				if ( Deduction.getValue().equals("DI21") )
				{

					if ( EmployeeDeduction.isExonerated() )
					{
						employeeAmount = Env.ZERO;
						amt = Env.ZERO;
					}
					else
					{
						amt = ((cumEmpAmt.add(employeeAmt)).divide(new BigDecimal( 2 ),2,BigDecimal.ROUND_HALF_UP ));
						amt = amt.subtract( cumAmt );
						employerAmount = amt;

						employeeAmount = employeeAmount.subtract(employerAmount );
					}
					amt = null;
					
				}
				// 2022-09-23
/*				else 
				{
					// Group 9
					if ( EmployeeProfile.getP_Profile_ID() == 1100069 ) 
					{
						if ( EmployeeDeduction.isExonerated() )
						{
							amt = employeeAmt;
						}
						
					}
						
				}
*/					

				if ( ! Deduction.getValue().equals("DI23FACG9") )
				{
					s_cacheEmployerInsurance.remove(key);
					s_cacheEmployerInsurance.put (key, cumAmt.add(employerAmt));
					s_cacheEmployeeInsurance.put (key, cumEmpAmt.add(employeeAmt).add(employerAmt));
					
				}

				if (  taxableBenefit == true )
				{
					s_cacheEmployerInsuranceG9.remove(key);
					s_cacheEmployerInsuranceG9.put (key, cumAmt.add(employerAmt));
					s_cacheEmployeeInsuranceG9.remove(key);
					s_cacheEmployeeInsuranceG9.put (key, cumEmpAmt.add(employeeAmt));
				}

/*				if (  Deduction.getValue().equals("DI26G9") )
				{
					s_cacheEmployerInsuranceG9.remove(key);
					s_cacheEmployerInsuranceG9.put (key, cumAmt.add(employerAmt));
					s_cacheEmployeeInsuranceG9.remove(key);
					s_cacheEmployeeInsuranceG9.put (key, cumEmpAmt.add(employeeAmt));
				}
*/
			
			}
			
		}

		log.info("DEBUG Calcul G9 " + key + " employeeAmt " + employeeAmt + " employerAmt : " + employerAmt ) ;
		return amt;
		
	}

	
	//Assurance Dental
	private void calcul_insurance_M82(  ) throws Exception
	{
		if ( Deduction_Param.getMaximumAge() != null && Deduction_Param.getMaximumAge().compareTo(Env.ZERO) != 0 )
		{
			BigDecimal age = Employee.getAgeWithMonth( Period.getPayDate() );
//			BigDecimal age = new BigDecimal( Employee.getAge( Period.getPayDate() ));

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

	
	private void calcul_insurance_M32(  ) throws Exception
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
			BigDecimal age = Employee.getAgeWithMonth( Period.getPayDate() );

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

		if ( TimeUtil.getDay_Of_Month( Employee.getBirthDate()) != 1 )
		{
			age = getAge( TimeUtil.addMonth( Employee.getBirthDate(), 1 ), Period.getPayDate() );  
		};

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


		if ( this.employeeAmount.compareTo(Env.ZERO) == 0 && this.employerAmount.compareTo(Env.ZERO) == 0 )
		{
			this.salaryAdmissible = Env.ZERO;
		}
		
		this.employeeAmount = this.employeeAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
		this.employerAmount = this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
		
	}   // calcul_insurance


	private void calcul_insurance_M35(  ) throws Exception
	{
		
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

		if ( ! this.Employee.isStatusIndian() )
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

	
	
	private void calcul_insurance_M38(  ) throws Exception
	{
		P_Deduction_Insurance DeductionInsurance = null;
		Timestamp DateForAge; 
		
		//
		// La fonction TimeUtil me permet d'évité que la modification a la variable
		// DateForAge modifie par le fait même la date de début de la période.
		//
		DateForAge = TimeUtil.addDays( Period.getPayDate(), 0 );
		int age;
		if ( Deduction_Param.getDeductionDay() != 0 && Deduction_Param.getDeductionMonth() != null  )
		{
			Calendar cal = TimeUtil.getCalendar( DateForAge);
			cal.set( Calendar.MONTH, Integer.parseInt(Deduction_Param.getDeductionMonth()) -1 );
			cal.set( Calendar.DAY_OF_MONTH, Deduction_Param.getDeductionDay());
			DateForAge = new Timestamp( cal.getTimeInMillis());
//			DateForAge.setMonth( Integer.parseInt( Deduction_Param.getDeductionMonth())-1);
//			DateForAge.setDate( Deduction_Param.getDeductionDay()  );
			age = EmployeeDeduction.getAge( DateForAge ) + 1;
		}
		else
		{
			age = EmployeeDeduction.getAge( DateForAge );  

			Timestamp BirthDate;
			if ( EmployeeDeduction.getBirthDate() != null)
				BirthDate = EmployeeDeduction.getBirthDate();
			else
				BirthDate = Employee.getBirthDate();

			if ( TimeUtil.getDay_Of_Month( BirthDate ) != 1 )
			{
				if ( TimeUtil.getMonth(BirthDate) +1  > 12 )
					age = getAge( TimeUtil.getDay(TimeUtil.getYear( BirthDate) + 1, 1, 1 ), Period.getPayDate() );  
				else
					age = getAge( TimeUtil.getDay(TimeUtil.getYear( BirthDate), TimeUtil.getMonth(BirthDate) +1, 1 ), Period.getPayDate() );  
			};
		}
		
		BigDecimal InsuranceRate = ZERO;
		BigDecimal InsuranceAmount = ZERO;
		this.salaryAdmissible = ZERO;

		String sqlServerQuery  = "Select P_Deduction_Insurance_Bound_ID "
			+ "FROM P_Deduction_Insurance_Bound, P_Deduction_Insurance, P_Period  "
			+ "WHERE P_Deduction_Insurance_Bound.P_Deduction_Insurance_Id = P_Deduction_Insurance.P_Deduction_Insurance_Id "
			+ " AND P_Deduction_Insurance.P_Deduction_Param_ID = " + Deduction_Param.getP_Deduction_Param_ID()
			+ " AND P_Period.P_Period_ID = " + Period.getP_Period_ID();
		
		sqlServerQuery += " AND " + age + "  between AgeLimitMin and AgeLimitMax ";
			// DateADD( day, AgeLimitMax, -1)";
		
		String query = sqlServerQuery;

		//Log.trace( 5,"deduction_insurance_age_bound - query = " + query );
		
		
		PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				P_Deduction_Insurance_Bound DeductionInsuranceBound = P_Deduction_Insurance_Bound.get( Env.getCtx(), rs.getInt("P_Deduction_Insurance_Bound_ID"), trxName);
				DeductionInsurance = P_Deduction_Insurance.get(Env.getCtx(), DeductionInsuranceBound.getP_Deduction_Insurance_ID(), trxName);

				if ( DeductionInsurance == null )
					log.info("Calcul - Employee - " + Employee.getValue () + " " + Employee.getName ()  + " Assurance Parameter not found " + Deduction_Param.toString() );

				
				if ( EmployeeDeduction.getGender() == null )
					EmployeeDeduction.setGender( Employee.getGender()); 

				if ( EmployeeDeduction.getGender() == null )
				{
		        	String message = "Erreur dans la définition des assurances : " + "( " + EmployeeDeduction.toString() + " )"; 
				    P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, message, trxName );
				}

				if ( EmployeeDeduction.isSmoker())
				{
					if ( EmployeeDeduction.getGender().equals( P_Employee_Deduction.GENDER_Man))
					{
						InsuranceRate = DeductionInsuranceBound.getSmokerMale(); 
					}
					else
					{
						InsuranceRate = DeductionInsuranceBound.getSmokerFemal(); 
					}
					
				}
				else
				{
					if ( EmployeeDeduction.getGender().equals( P_Employee_Deduction.GENDER_Man))
					{
						InsuranceRate = DeductionInsuranceBound.getNoSmokerMale(); 
					}
					else
					{
						InsuranceRate = DeductionInsuranceBound.getNoSmokerFemal(); 
					}
				}
				
//				Log.trace( 5,"deduction_insurance_age_bound - IsSmoker       = " + IsSmoker );
//				Log.trace( 5,"deduction_insurance_age_bound - Gender         = " + Gender );
//				Log.trace( 5,"deduction_insurance_age_bound - SmokerFemal    = " + SmokerFemal );
//				Log.trace( 5,"deduction_insurance_age_bound - NoSmokerFemal  = " + NoSmokerFemal );
//				Log.trace( 5,"deduction_insurance_age_bound - SmokerMale     = " + SmokerMale );
//				Log.trace( 5,"deduction_insurance_age_bound - NoSmokerMale   = " + NoSmokerMale );
//				Log.trace( 5,"deduction_insurance_age_bound - ProtectionAmount   = " + ProtectionAmount );
				
				// ici déterminer le nombre de fois 
				//InsuranceAmount = ProtectionAmount / rcafctbas * InsuranceRate ;
				
				if ( EmployeeDeduction.getProtectionAmount() != null && EmployeeDeduction.getProtectionAmount().compareTo(ZERO) != 0)
					this.salaryAdmissible = EmployeeDeduction.getProtectionAmount();
				else
				{
					this.salaryAdmissible = Payment.getAnnualSalary();

					// Elever le numéro d'employé a la paie 2006-03
/*					if ( Employee.getValue().trim().equals( "0582") || Employee.getValue().trim().equals("7340") )
					{
						
					}
					else
					{
						if ( Assignment.isRWT( Period.getStartDate()))
						{
						
							P_Assignment_Param AssignmentParamBase = P_Assignment_Param.get( Env.getCtx(), Employee.GetPrincipalAssignment_Param(), trxName );
							BigDecimal Weekly_Hours_Base = AssignmentParamBase.getWeekly_Hours();
							BigDecimal Weekly_Hours_RWT  = Assignment.getWeekly_Hours( Period.getStartDate(), trxName );
						
							this.salaryAdmissible = this.salaryAdmissible.multiply( Weekly_Hours_RWT.divide(Weekly_Hours_Base,8,BigDecimal.ROUND_HALF_UP ) );
						
						}
					}
*/										
										
				}

				
				if ( EmployeeDeduction.getMultiplyRate() != null && EmployeeDeduction.getMultiplyRate().compareTo(ZERO) != 0 )
					this.salaryAdmissible = this.salaryAdmissible.multiply( EmployeeDeduction.getMultiplyRate() );

				if ( DeductionInsurance != null )
				{
//					this.salaryAdmissible = this.salaryAdmissible.divide( new BigDecimal( DeductionInsurance.getInsuranceLayer()), BigDecimal.ROUND_HALF_UP );
					if ( DeductionInsurance.getInsuranceLayer() == 0 )
						InsuranceAmount = this.salaryAdmissible.multiply( InsuranceRate);
					else
						InsuranceAmount = this.salaryAdmissible.divide( new BigDecimal( DeductionInsurance.getInsuranceLayer()), 8, BigDecimal.ROUND_HALF_UP ).multiply( InsuranceRate);
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

				if ( ! this.Employee.isStatusIndian() )
				{
					if ( Quebec && ( Deduction_Param.getTaxeRate() != null && Deduction_Param.getTaxeRate().compareTo( ZERO ) != 0 ))
					{
						InsuranceAmount = InsuranceAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
					}

					if ( Ontario && ( Deduction_Param.getTaxeRateOntario() != null && Deduction_Param.getTaxeRateOntario().compareTo( ZERO ) != 0 ))
					{
						InsuranceAmount = InsuranceAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
					}

					if ( Manitoba && ( Deduction_Param.getTaxeRateManitoba() != null && Deduction_Param.getTaxeRateManitoba().compareTo( ZERO ) != 0 ))
					{
						InsuranceAmount = InsuranceAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
					}

				}


/*				if ( Deduction_Param.getTaxeRate() != null && Deduction_Param.getTaxeRate().compareTo( ZERO ) != 0)
				{
					InsuranceAmount = InsuranceAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				}
*/				

			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

		
		
		//
		//
		// Recherche des montants admissible /
		// Search admisible amount
		
		this.hoursAdmissible  = ZERO;
		
		if ( DeductionInsurance != null )
		{
			if ( DeductionInsurance.getInsuranceTypeRate() != null && DeductionInsurance.getInsuranceTypeRate().equals(P_Deduction_Insurance.INSURANCETYPERATE_Employer))
				this.employerAmount = InsuranceAmount;
			else
				this.employeeAmount = InsuranceAmount;
		}


		this.employeeAmount = this.employeeAmount.divide( new BigDecimal( 2 ), 2, BigDecimal.ROUND_HALF_UP) ;
		this.employerAmount = this.employerAmount.divide( new BigDecimal( 2 ), 2, BigDecimal.ROUND_HALF_UP) ;

		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		this.employerAmount = this.employerAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		
		//Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + "employee amount " + this.employeeAmount + " employer amount " + employerAmount );
		
		if ( this.employeeAmount.compareTo(Env.ZERO) == 0 && this.employerAmount.compareTo(Env.ZERO) == 0 )
		{
			this.salaryAdmissible = Env.ZERO;
		}
		
	}
	
	//
	// calcul_insurance
	//
	private void calcul_insurance_age_bound() throws Exception
	{
		P_Deduction_Insurance DeductionInsurance = null;
		Timestamp DateForAge; 
		
		//
		// La fonction TimeUtil me permet d'évité que la modification a la variable
		// DateForAge modifie par le fait même la date de début de la période.
		//
		DateForAge = TimeUtil.addDays( Period.getStartDate(), 0 );
		int age;
		if ( Deduction_Param.getDeductionDay() != 0 && Deduction_Param.getDeductionMonth() != null  )
		{
			Calendar cal = TimeUtil.getCalendar( DateForAge);
			cal.set( Calendar.MONTH, Integer.parseInt(Deduction_Param.getDeductionMonth()) -1 );
			cal.set( Calendar.DAY_OF_MONTH, Deduction_Param.getDeductionDay());
			DateForAge = new Timestamp( cal.getTimeInMillis());
//			DateForAge.setMonth( Integer.parseInt( Deduction_Param.getDeductionMonth())-1);
//			DateForAge.setDate( Deduction_Param.getDeductionDay()  );
			age = EmployeeDeduction.getAge( DateForAge ) + 1;
		}
		else
		{
			age = EmployeeDeduction.getAge( DateForAge );  
		}
		
		BigDecimal InsuranceRate = ZERO;
		BigDecimal InsuranceAmount = ZERO;
		this.salaryAdmissible = ZERO;

		String sqlServerQuery  = "Select P_Deduction_Insurance_Bound_ID "
			+ "FROM P_Deduction_Insurance_Bound, P_Deduction_Insurance, P_Period  "
			+ "WHERE P_Deduction_Insurance_Bound.P_Deduction_Insurance_Id = P_Deduction_Insurance.P_Deduction_Insurance_Id "
			+ " AND P_Deduction_Insurance.P_Deduction_Param_ID = " + Deduction_Param.getP_Deduction_Param_ID()
			+ " AND P_Period.P_Period_ID = " + Period.getP_Period_ID();
		
		sqlServerQuery += " AND " + age + "  between AgeLimitMin and AgeLimitMax ";
			// DateADD( day, AgeLimitMax, -1)";
		
		String query = sqlServerQuery;

		//Log.trace( 5,"deduction_insurance_age_bound - query = " + query );
		
		
		PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				P_Deduction_Insurance_Bound DeductionInsuranceBound = P_Deduction_Insurance_Bound.get( Env.getCtx(), rs.getInt("P_Deduction_Insurance_Bound_ID"), trxName);
				DeductionInsurance = P_Deduction_Insurance.get(Env.getCtx(), DeductionInsuranceBound.getP_Deduction_Insurance_ID(), trxName);

				if ( DeductionInsurance == null )
					log.info("Calcul - Employee - " + Employee.getValue () + " " + Employee.getName ()  + " Assurance Parameter not found " + Deduction_Param.toString() );

				
				if ( EmployeeDeduction.getGender() == null )
					EmployeeDeduction.setGender( Employee.getGender()); 

				if ( EmployeeDeduction.getGender() == null )
				{
		        	String message = "Erreur dans la définition des assurances : " + "( " + EmployeeDeduction.toString() + " )"; 
				    P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, message, trxName );
				}

				if ( EmployeeDeduction.isSmoker())
				{
					if ( EmployeeDeduction.getGender().equals( P_Employee_Deduction.GENDER_Man))
					{
						InsuranceRate = DeductionInsuranceBound.getSmokerMale(); 
					}
					else
					{
						InsuranceRate = DeductionInsuranceBound.getSmokerFemal(); 
					}
					
				}
				else
				{
					if ( EmployeeDeduction.getGender().equals( P_Employee_Deduction.GENDER_Man))
					{
						InsuranceRate = DeductionInsuranceBound.getNoSmokerMale(); 
					}
					else
					{
						InsuranceRate = DeductionInsuranceBound.getNoSmokerFemal(); 
					}
				}
				
//				Log.trace( 5,"deduction_insurance_age_bound - IsSmoker       = " + IsSmoker );
//				Log.trace( 5,"deduction_insurance_age_bound - Gender         = " + Gender );
//				Log.trace( 5,"deduction_insurance_age_bound - SmokerFemal    = " + SmokerFemal );
//				Log.trace( 5,"deduction_insurance_age_bound - NoSmokerFemal  = " + NoSmokerFemal );
//				Log.trace( 5,"deduction_insurance_age_bound - SmokerMale     = " + SmokerMale );
//				Log.trace( 5,"deduction_insurance_age_bound - NoSmokerMale   = " + NoSmokerMale );
//				Log.trace( 5,"deduction_insurance_age_bound - ProtectionAmount   = " + ProtectionAmount );
				
				// ici déterminer le nombre de fois 
				//InsuranceAmount = ProtectionAmount / rcafctbas * InsuranceRate ;
				
				if ( EmployeeDeduction.getProtectionAmount() != null && EmployeeDeduction.getProtectionAmount().compareTo(ZERO) != 0)
					this.salaryAdmissible = EmployeeDeduction.getProtectionAmount();
				else
				{
					this.salaryAdmissible = Payment.getAnnualSalary();

					// Elever le numéro d'employé a la paie 2006-03
/*					if ( Employee.getValue().trim().equals( "0582") || Employee.getValue().trim().equals("7340") )
					{
						
					}
					else
					{
						if ( Assignment.isRWT( Period.getStartDate()))
						{
						
							P_Assignment_Param AssignmentParamBase = P_Assignment_Param.get( Env.getCtx(), Employee.GetPrincipalAssignment_Param(), trxName );
							BigDecimal Weekly_Hours_Base = AssignmentParamBase.getWeekly_Hours();
							BigDecimal Weekly_Hours_RWT  = Assignment.getWeekly_Hours( Period.getStartDate(), trxName );
						
							this.salaryAdmissible = this.salaryAdmissible.multiply( Weekly_Hours_RWT.divide(Weekly_Hours_Base,8,BigDecimal.ROUND_HALF_UP ) );
						
						}
					}
*/										
										
				}

				
				if ( EmployeeDeduction.getMultiplyRate() != null && EmployeeDeduction.getMultiplyRate().compareTo(ZERO) != 0 )
					this.salaryAdmissible = this.salaryAdmissible.multiply( EmployeeDeduction.getMultiplyRate() );

				if ( DeductionInsurance != null )
				{
//					this.salaryAdmissible = this.salaryAdmissible.divide( new BigDecimal( DeductionInsurance.getInsuranceLayer()), BigDecimal.ROUND_HALF_UP );
					if ( DeductionInsurance.getInsuranceLayer() == 0 )
						InsuranceAmount = this.salaryAdmissible.multiply( InsuranceRate);
					else
						InsuranceAmount = this.salaryAdmissible.divide( new BigDecimal( DeductionInsurance.getInsuranceLayer()), 8, BigDecimal.ROUND_HALF_UP ).multiply( InsuranceRate);
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

				if ( ! this.Employee.isStatusIndian() )
				{
					if ( Quebec && ( Deduction_Param.getTaxeRate() != null && Deduction_Param.getTaxeRate().compareTo( ZERO ) != 0 ))
					{
						InsuranceAmount = InsuranceAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
					}

					if ( Ontario && ( Deduction_Param.getTaxeRateOntario() != null && Deduction_Param.getTaxeRateOntario().compareTo( ZERO ) != 0 ))
					{
						InsuranceAmount = InsuranceAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateOntario().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
					}
					
					if ( Manitoba && ( Deduction_Param.getTaxeRateManitoba() != null && Deduction_Param.getTaxeRateManitoba().compareTo( ZERO ) != 0 ))
					{
						InsuranceAmount = InsuranceAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRateManitoba().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
					}

				}


/*				if ( Deduction_Param.getTaxeRate() != null && Deduction_Param.getTaxeRate().compareTo( ZERO ) != 0)
				{
					InsuranceAmount = InsuranceAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				}
*/				

			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

		
		
		//
		//
		// Recherche des montants admissible /
		// Search admisible amount
		
		this.hoursAdmissible  = ZERO;
		
		if ( DeductionInsurance != null )
		{
			if ( DeductionInsurance.getInsuranceTypeRate() != null && DeductionInsurance.getInsuranceTypeRate().equals(P_Deduction_Insurance.INSURANCETYPERATE_Employer))
				this.employerAmount = InsuranceAmount;
			else
				this.employeeAmount = InsuranceAmount;
		}


		// TODO add parameter pour indiqué que le taux est mensuel.
		if ( Employee.getAD_Client_ID() != 11)
		{
			this.employeeAmount = this.employeeAmount.divide( new BigDecimal( 2 ), 2, BigDecimal.ROUND_HALF_UP) ;
			this.employerAmount = this.employerAmount.divide( new BigDecimal( 2 ), 2, BigDecimal.ROUND_HALF_UP) ;
		}

		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		this.employerAmount = this.employerAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		
		//Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + "employee amount " + this.employeeAmount + " employer amount " + employerAmount );
		
		if ( this.employeeAmount.compareTo(Env.ZERO) == 0 && this.employerAmount.compareTo(Env.ZERO) == 0 )
		{
			this.salaryAdmissible = Env.ZERO;
		}
		
	}   // calcul_insurance_age_bound

	

	
	
	//
	// Calcul les assurance invalidité longue durée.
	//
	private void calcul_insurance_LTD() throws Exception
	{
// 
//l'age en date du 1er mai a valider		
		// l'employé ne paie plus quand il atteint 64 ans 
//  	    BigDecimal age = Employee.getAgeWithMonth( TimeUtil.addMonth( Period.getStartDate(), -1));
//		BigDecimal age = new BigDecimal( Employee.getAge( Period.getPayDate() ));

		BigDecimal age = Employee.getAgeWithMonth( Period.getPayDate() );
		

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

		if ( Deduction_Param.getMaximumAge() != null && Deduction_Param.getMaximumAge().compareTo(Env.ZERO) != 0 && age.compareTo( Deduction_Param.getMaximumAge() ) >= 0 )
        {
            this.salaryAdmissible = Env.ZERO;  
            this.hoursAdmissible = Env.ZERO;
    		this.employeeAmount = Env.ZERO;
    		this.employerAmount = Env.ZERO;
    		return;
        }
	
		
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
				if ( EmployeeProfile != null && (EmployeeProfile.getP_Profile_ID() == 1100068 ))
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


		//2023-05-10 modifier ROUND_HALF_UP pour ROUND_UP
		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_UP );
		this.employerAmount = this.employerAmount.setScale( 2 , BigDecimal.ROUND_UP );

		if ( this.employeeAmount.compareTo(Env.ZERO) == 0 && this.employerAmount.compareTo(Env.ZERO) == 0 )
		{
			this.salaryAdmissible = Env.ZERO;
		}

	}
	
	//
	// calcul pension fund
	//
	private void calcul_pension_fund() throws Exception
	{
		//
		// Employé a atteint 35 années de service
		//
		if ( EmployeeDeduction.getDate35() != null && EmployeeDeduction.getDate35().compareTo(Period.getStartDate()) < 0)
		{
			employerAmount = Env.ZERO;
			employeeAmount = Env.ZERO;
			return;
		}
		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param );
		
		BigDecimal EmployeeRate = EmployeeDeduction.getEmployeeRate(Deduction_Param); 
		BigDecimal EmployerRate = EmployeeDeduction.getEmployerRate(Deduction_Param); 

		BigDecimal EmployeeAmount = EmployeeDeduction.getEmployeeAmount(Deduction_Param );
		BigDecimal EmployerAmount = EmployeeDeduction.getEmployerAmount(Deduction_Param ); 

		BigDecimal SalaryAdmissible = this.salaryAdmissible;
		//ExemptionAmount
		BigDecimal ExemptionAmount = EmployeeDeduction.getExemptionAmountPeriod(Deduction_Param, Frequency.getNumberOfPeriod());

		//2024-12-31
//		if ( SalaryAdmissible.compareTo(Env.ZERO) == 0) return;
		
		
	    if ( ExemptionAmount.compareTo(Env.ZERO) != 0 && this.hoursAdmissible != null )
		{
			
			P_Assignment_Param AssignmentParam = P_Assignment_Param.get( Env.getCtx(), Assignment.getP_Assignment_ID(), Period.getEndDate(), trxName );
			if ( AssignmentParam == null )
				AssignmentParam = P_Assignment_Param.get( Env.getCtx(), Employee.GetPrincipalAssignment_Param(), trxName );

			ExemptionAmount = ExemptionAmount.multiply( this.hoursAdmissible.divide( AssignmentParam.getWeekly_Hours().multiply(new BigDecimal(2)), 8,BigDecimal.ROUND_HALF_UP));
            ExemptionAmount = ExemptionAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
						
			if ( Payment.getSalaryPercentage() != null && Payment.getSalaryPercentage().compareTo( Env.ZERO) != 0)
			{
				ExemptionAmount = ExemptionAmount.multiply(Payment.getSalaryPercentage().divide( new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP)).setScale(2,BigDecimal.ROUND_HALF_UP);
			}

			
			SalaryAdmissible = SalaryAdmissible.subtract( ExemptionAmount);
		}
		
		if ( Deduction_Param.getP_Column_Adm_ID() != 0 )
		{
//			P_Column_Adm ColumnAdm = P_Column_Adm.get( Env.getCtx (), Deduction_Param.getP_Column_Adm_ID() , trxName); 

			if ( EmployeeRate != null && EmployeeRate.compareTo( ZERO ) != 0 )
			{
				employeeAmount = SalaryAdmissible.multiply( EmployeeRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) );
			}
			
			if ( EmployerRate != null && EmployerRate.compareTo( ZERO ) != 0 )
			{
				employerAmount =  SalaryAdmissible.multiply( EmployerRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ) ;
			}
		}
		else
		{
			if ( EmployeeRate != null )
			{
				employeeAmount = SalaryAdmissible.multiply( EmployeeRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) );
			}
			
			if ( EmployerRate != null )
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
		
		
		if ( Deduction_Param.getP_RWT_ID() != 0 )
		{
//			P_Assignment Assignment = P_Assignment.get( Env.getCtx(), Employee.GetPrincipalAssignment(), trxName);
			if ( Assignment.isRWT( Period.getStartDate()))
			{

				P_Assignment_Param AssignmentParamBase = P_Assignment_Param.get( Env.getCtx(), Employee.GetPrincipalAssignment_Param(), trxName );
				BigDecimal Weekly_Hours_Base = AssignmentParamBase.getWeekly_Hours();
				BigDecimal Weekly_Hours_RWT  = Assignment.getWeekly_Hours( Period.getStartDate(), trxName );
				
				employerAmount = employeeAmount.multiply( Weekly_Hours_Base.divide(Weekly_Hours_RWT,8,BigDecimal.ROUND_HALF_UP ));
				employerAmount = employerAmount.subtract( employeeAmount);
				employerAmount = employerAmount.multiply( new BigDecimal(2)).add( employeeAmount);

				//
				// ARTT l'avantage imposable égale la 1/2 de la différence entre la part employé et de la part employeur.
				// 
				if 	( !calculDeduction )
					employerAmount = (employerAmount.subtract( employeeAmount)).divide(new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP );
				
			}
			//
			// Il n'y a pas d'avantage imposable si l'employé n'est pas en ARTT
			else if	( !calculDeduction )
			{
				this.employeeAmount = ZERO;
				this.employerAmount = ZERO;
				this.salaryAdmissible = ZERO;
				this.hoursAdmissible = ZERO;
			}

		}
		
//		if ( PgiUtil.getSolsticeParameter( Env.getCtx(), "KRISPY").trim().equals("Test") )
		{

			if ( Deduction.getValue().equals("DC07RP")  )
			{
				BigDecimal aldHours = getAldHours(  Payment.getP_Payment_ID() );
				if ( aldHours == null ) aldHours = Env.ZERO;
//				BigDecimal aldHours = Env.ZERO;
				
				if ( (this.hoursAdmissible.add(aldHours)).compareTo(Env.ZERO) != 0 )
				{
					BigDecimal rate = new BigDecimal(0.0175);
//					P_Assignment_Param AssignmentParam = P_Assignment_Param.get( Env.getCtx(), Assignment.getP_Assignment_ID(), Period.getEndDate(), trxName );
//					if ( AssignmentParam == null )
					P_Assignment_Param	AssignmentParam = P_Assignment_Param.get( Env.getCtx(), Employee.GetPrincipalAssignment_Param(), trxName );
					BigDecimal weekly_Hours = AssignmentParam.getWeekly_Hours();
					if ( weekly_Hours.compareTo( new BigDecimal(37)) < 0)
						weekly_Hours = new BigDecimal(37);
					
					BigDecimal AmtBase = weekly_Hours.multiply( AssignmentParam.getHourly_Rate() ).multiply(rate);
					AmtBase = AmtBase.setScale(2, BigDecimal.ROUND_HALF_UP);
					
					if ( aldHours == null ) aldHours = Env.ZERO;
					
					if ( this.hoursAdmissible.compareTo( Env.ZERO) > 0)
					{
						if ( this.hoursAdmissible != null  && this.hoursAdmissible.compareTo( aldHours ) != 0)
						{
							BigDecimal heuresAjuste =  this.hoursAdmissible.add( aldHours );
							
							// les employé en retraite progressive doivent être a 37h
							if ( heuresAjuste.compareTo( AssignmentParam.getWeekly_Hours() ) > 0)
								heuresAjuste =  new BigDecimal(37);
//								heuresAjuste = AssignmentParam.getWeekly_Hours();
							
							BigDecimal prorata = heuresAjuste.multiply( AssignmentParam.getHourly_Rate() ).multiply(rate);
							this.employerAmount = prorata;
						}
						else
						{
							this.employerAmount = AmtBase;
						}
						
					}
					else this.employerAmount = Env.ZERO;
					
					
				}
				else this.employerAmount = Env.ZERO;
			}
			
		}
			


		//Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " employee amount " + this.employeeAmount + " employer amount " + employerAmount );
		
	}  // Pension Fund 

	
	private static BigDecimal getAldHours( int P_Payment_ID )
	{
		BigDecimal hours = null;
		String sql = "SELECT SUM( quantitycalc ) "
				+ "FROM P_PAYMENT_GAIN "
				+ "Inner join P_GAIN on P_GAIN.P_GAIN_ID = P_PAYMENT_GAIN.P_GAIN_ID "
//				+ "INNER JOIN P_PAYMENT on P_PAYMENT.P_PAYMENT_ID = P_PAYMENT_GAIN.P_PAYMENT_ID "
				+ " WHERE P_GAIN.VALUE = '0076'"
				+ " AND P_PAYMENT_GAIN.P_PAYMENT_ID = " + P_Payment_ID
				;
 
		PreparedStatement pstmt = null;
		try
		{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				if ( rs.next () )
				{
					hours = rs.getBigDecimal(1);
				}
				
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
		}
		catch (Exception e)
		{
			System.out.println ("* Error * getAldHours - " + sql + " - " + e);
			log.log(Level.SEVERE, "* Error *  getAldHours - SQL " + sql   , e );
		}
		
		return hours;

	}

	
	
	//
	// calcul pension fund RRF et RRE
	//
	// Formule
	//    ( Exemption RRQ * Taux de cotisation ) + [( MGA - Exemption RRQ ) * (Taux de cotisation - 1.8%)]
	//    + ( Excédent du salaire * Taux de cotisation )
	//
	// MGA : Le montant utilisé est le moindre du salaire cotisable ou du "maximum des gain admissible" (MGA)
	//
	//
	
	private void calcul_pension_fund_RRF_RRE() throws Exception
	{

		//
		// Employé a atteint 35 années de service
		//
		if ( EmployeeDeduction.getDate35() != null && EmployeeDeduction.getDate35().compareTo(Period.getStartDate()) < 0)
		{
			employerAmount = Env.ZERO;
			employeeAmount = Env.ZERO;
			return;
		}

		BigDecimal EmployeeRate = EmployeeDeduction.getEmployeeRate(Deduction_Param ); 
		BigDecimal EmployerRate = EmployeeDeduction.getEmployerRate(Deduction_Param ); 

//		BigDecimal EmployeeAmount = EmployeeDeduction.getEmployeeAmount(Deduction_Param );
//		BigDecimal EmployerAmount = EmployeeDeduction.getEmployerAmount(Deduction_Param ); 

		getSalaryAdmissible( Deduction_Param );

		// Read Cumulative information about a deduction for this Year..
        get_deduction_cumulative( );

//		BigDecimal Salary = this.cumSalaryAdmissible.add( salaryAdmissible ); 
		BigDecimal Salary = salaryAdmissible.multiply( new BigDecimal( Frequency.getNumberOfPeriod()) ) ; 

		// EmployeeRate 8.08%
		BigDecimal MGA = Deduction_Param.getMaximumAllowableEarnings() ;   //41100
		BigDecimal ExemptionAmount = EmployeeDeduction.getExemptionAmountPeriod(Deduction_Param, Frequency.getNumberOfPeriod());		
//		BigDecimal ExemptionAmount = EmployeeDeduction.getExemptionAmount(Deduction_Param); // 3500
		
//		BigDecimal Exemption = (MGA.multiply( EmployerRate )).divide( new BigDecimal( Frequency.getNumberOfPeriod()), 2, BigDecimal.ROUND_HALF_UP);

		// TODO taxrate a remplacer par un autre variable...
		
		BigDecimal CotisationRate =  Deduction_Param.getTaxeRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);
		BigDecimal RRERate =  EmployeeRate.divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);
		BigDecimal RRE = Env.ZERO;

		//salaryAdmissible.multiply( new BigDecimal( Frequency.getNumberOfPeriod()) );
		RRE = ExemptionAmount.multiply( RRERate );
		RRE = RRE.add( (MGA.subtract(ExemptionAmount)).multiply( RRERate.subtract( CotisationRate ) ) );  // new BigDecimal(0.018)
		RRE = RRE.add( (Salary.subtract( MGA )).multiply( RRERate ) );
		
		this.employeeAmount = RRE.divide( new BigDecimal( Frequency.getNumberOfPeriod()), 2, BigDecimal.ROUND_HALF_UP);

		RRERate =  EmployerRate.divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);
		RRE = Env.ZERO;
		//salaryAdmissible.multiply( new BigDecimal( Frequency.getNumberOfPeriod()) );
		RRE = ExemptionAmount.multiply( RRERate );
		RRE = RRE.add( (MGA.subtract(ExemptionAmount)).multiply( RRERate.subtract( CotisationRate ) ) );  // new BigDecimal(0.018)
		RRE = RRE.add( (Salary.subtract( MGA )).multiply( RRERate ) );
		this.employerAmount = RRE.divide( new BigDecimal( Frequency.getNumberOfPeriod()), 2, BigDecimal.ROUND_HALF_UP);
		
/*
		if ( Salary.compareTo(  ExemptionAmount ) < 0 )
		{
			RRE = salaryAdmissible.multiply( RRERate );
		}
		if ( Salary.compareTo( MGA ) < 0 && Salary.compareTo(  ExemptionAmount ) > 0 )
		{
			RRE = RRE.add( salaryAdmissible.multiply( RRERate.subtract( CotisationRate ) ));  // new BigDecimal(0.018)
		}
		
		if ( Salary.compareTo( MGA ) > 0 )
		{
			BigDecimal diff =  MGA.subtract( this.cumSalaryAdmissible );
			if ( diff.compareTo( Env.ZERO) > 0)
			{
				RRE = RRE.add( diff.multiply( RRERate.subtract( CotisationRate ) ));
				salaryAdmissible = salaryAdmissible.subtract( diff );
			}
			RRE = RRE.add( salaryAdmissible.multiply( RRERate ) );
		}
	*/	
		

		if ( Assignment.isRWT( Period.getStartDate()))
		{

			P_Assignment_Param AssignmentParamBase = P_Assignment_Param.get( Env.getCtx(), Employee.GetPrincipalAssignment_Param(), trxName );
			BigDecimal Weekly_Hours_Base = AssignmentParamBase.getWeekly_Hours();
			BigDecimal Weekly_Hours_RWT  = Assignment.getWeekly_Hours( Period.getStartDate(), trxName );
			
			employerAmount = employeeAmount.multiply( Weekly_Hours_Base.divide(Weekly_Hours_RWT,8,BigDecimal.ROUND_HALF_UP ));
			employerAmount = employerAmount.subtract( employeeAmount);
			employerAmount = employerAmount.multiply( new BigDecimal(2)).add( employeeAmount);

			//
			// ARTT l'avantage imposable égale la 1/2 de la différence entre la part employé et de la part employeur.
			// 
			if 	( !calculDeduction )
				employerAmount = (employerAmount.subtract( employeeAmount)).divide(new BigDecimal(2),2,BigDecimal.ROUND_HALF_UP );
			
		}

		
	}  // Pension Fund RRF RRE 

	//
	// Récupération des avances 
	//
	/*
	private void Advance_Recovery()  throws Exception
	{
		P_Employee_Advance EmployeeAdvance = P_Employee_Advance.get( Env.getCtx(), Payment.getP_Employee_ID(), Deduction_Param.getP_Advance_ID(), trxName );
		if ( EmployeeAdvance.getP_Employee_Advance_ID() != 0 )
		{
			this.employeeAmount   = EmployeeAdvance.getAdvanceAmount();
			this.employerAmount   = Env.ZERO; 
			this.salaryAdmissible = Env.ZERO;
			this.hoursAdmissible  = Env.ZERO;
			
			if ( this.employeeAmount.compareTo(Env.ZERO) != 0)
			{
				P_Employee_Advance_Detail EmployeeAdvanceDetail = new P_Employee_Advance_Detail( Env.getCtx(), -1, trxName );
				EmployeeAdvanceDetail.setP_Employee_Advance_ID(EmployeeAdvance.getP_Employee_Advance_ID());
				EmployeeAdvanceDetail.setP_Payment_ID( Payment.getP_Payment_ID());
				EmployeeAdvanceDetail.setAdvanceAmount(Env.ZERO.subtract( EmployeeAdvance.getAdvanceAmount()));
				EmployeeAdvanceDetail.setAdvanceDate( Period.getEndDate());
				EmployeeAdvanceDetail.setIsRefund( true);
				EmployeeAdvanceDetail.save();
			}
		}
		
	}
	*/
    //

    // Récupération des avances 
    //
	// On a pas a créer le sommaire des avances, car la méthode aftersave dans
	// P_Payment_Gain créer le sommaire des avances automatiquement.
	//
    private void Advance_Recovery_Part1()  throws Exception
    {
         P_Employee_Advance EmployeeAdvance = P_Employee_Advance.get( Env.getCtx(), Payment.getP_Employee_ID(), Deduction_Param.getP_Advance_ID(), trxName );
         if ( EmployeeAdvance.getP_Employee_Advance_ID() != 0 && Period.getP_Period_ID() == TimeSheetPeriod.getP_Period_ID() )
         {

        	 //
        	 // Dans le cas du remboursement d'un avance. on doit ajouter au montant qui est dû par l'employé, le montant qui a déjà été payer
        	 // au cours de la période de paie courante. Car le calcul des déductions ce fait toujours pour l'ensemble de la période 
        	 // et non uniquement pour la feuile de temps courante.
        	 //
    	 	 get_deduction_cumulative_per(  );

        	 this.employeeAmount   = EmployeeAdvance.getAdvanceAmountBalance().add(this.cumEmpAmount ); 
        	 this.employerAmount   = Env.ZERO; 
        	 this.salaryAdmissible = Env.ZERO;
        	 this.hoursAdmissible  = Env.ZERO;
         }
    }



	
	//
	// Calcul syndicat
	// Seul différence avec le Syndicat c'est que si le salaire admissible est négatif, on rembourse le montant fixe de contribution.
	private void calcul_syndicat()	throws Exception
	{

		BigDecimal EmployeeRate = EmployeeDeduction.getEmployeeRate(Deduction_Param ); 
		BigDecimal EmployerRate = EmployeeDeduction.getEmployerRate(Deduction_Param ); 

		BigDecimal EmployeeAmount = EmployeeDeduction.getEmployeeAmount(Deduction_Param );

		if ( ! Deduction.getValue().equals("DD03") )
		{
			// Recherche les montants admissible /
			getSalaryAdmissible( Deduction_Param );
			
			if ( salaryAdmissible.compareTo(Env.ZERO) == 0 )
			{
				return;
			}

		} else
		{
			hoursAdmissible = this.getNbrDay( Payment.getP_Payment_ID(), Payment.getP_Employee_ID(), Payment.getP_Period_ID(),  Deduction_Param.getP_Column_Adm_ID() );
			
		}

		//

		if ( Deduction_Param.getMinimumHoursAllowable() != null && Deduction_Param.getMinimumHoursAllowable().compareTo(Env.ZERO) > 0 )
		{
			if ( hoursAdmissible.compareTo(Deduction_Param.getMinimumHoursAllowable() ) < 0 &&
				 hoursAdmissible.compareTo( Env.ZERO ) > 0
					) 
				hoursAdmissible = Deduction_Param.getMinimumHoursAllowable();
		}

		if ( Deduction_Param.getMaximumHoursAllowable() != null && Deduction_Param.getMaximumHoursAllowable().compareTo(Env.ZERO) > 0)
		{
			P_Assignment_Param AssignmentParam = P_Assignment_Param.get( Env.getCtx(), Assignment.getP_Assignment_ID(), Period.getEndDate(), trxName );
			if ( AssignmentParam == null )
				AssignmentParam = P_Assignment_Param.get( Env.getCtx(), Employee.GetPrincipalAssignment_Param(), trxName );

			if ( ! Deduction.getValue().equals("DD03") )
			{
				BigDecimal rate = hoursAdmissible.divide(  AssignmentParam.getWeekly_Hours(),6,BigDecimal.ROUND_HALF_UP );
				if ( rate.compareTo( Env.ONE) > 0)
					rate = Env.ONE;

				if ( hoursAdmissible.compareTo(Deduction_Param.getMaximumHoursAllowable() ) > 0)
					hoursAdmissible = Deduction_Param.getMaximumHoursAllowable();
				

				hoursAdmissible = (hoursAdmissible.multiply( rate )).setScale(0, BigDecimal.ROUND_DOWN); 
				
			} else
			{
				
				if ( hoursAdmissible.compareTo(Deduction_Param.getMaximumHoursAllowable() ) > 0)
					hoursAdmissible = Deduction_Param.getMaximumHoursAllowable();
				
			}


			
			salaryAdmissible = Env.ZERO;
//			EmployeeAmount = hoursAdmissible.multiply( Deduction_Param.getEmployeeAmount() );
			EmployeeAmount = hoursAdmissible.multiply( Deduction_Param.getUnionDuesFixedAmt() );
			
			
		} 
		else
		{

//			BigDecimal EmployerAmount = EmployeeDeduction.getEmployerAmount(Deduction_Param ); 

		    employeeAmount = ZERO;
		    employerAmount = ZERO;
		    
			if ( Deduction_Param.getP_Column_Adm_ID() != 0 )
			{
//				P_Column_Adm ColumnAdm = P_Column_Adm.get( Env.getCtx (), Deduction_Param.getP_Column_Adm_ID() , trxName); 

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
			
		}


/*		if ( salaryAdmissible.compareTo(Env.ZERO ) < 0 )
		{
			EmployeeAmount = EmployeeAmount.negate();	
		}
*/		
		if ( EmployeeAmount != null )
		{
			employeeAmount = employeeAmount.add( EmployeeAmount );
		}
/*		
		if ( EmployerAmount != null )
		{
			employerAmount = employerAmount.add( EmployerAmount );
		}
*/		
		 
		
		this.employeeAmount = employeeAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		this.employerAmount = employerAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		// Le salaire admissible conservé est sans le montant exemption.
		
	}   // Calcul syndicat
	
	//
	// EUPP, déduction avec maximum par borne
	// permier 1000 payer a 100% employé et employeur
	// 1500 suivant 100 employé et 50% employeur
	//
	private void calcul_EUPP( boolean Reer )  throws Exception
	{
//		get_deduction_cumulative_EUPP( );
		get_deduction_cumulative( );
        
		
		BigDecimal EmployeeRate = EmployeeDeduction.getEmployeeRate(Deduction_Param ); 
		BigDecimal EmployerRate = EmployeeDeduction.getEmployerRate(Deduction_Param ); 

		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param );

		BigDecimal EmployeeAmount = EmployeeDeduction.getEmployeeAmount(Deduction_Param );
		BigDecimal EmployerAmount = EmployeeDeduction.getEmployerAmount(Deduction_Param ); 

		
	    employeeAmount = ZERO;
	    employerAmount = ZERO;

		if ( EmployeeRate != null && EmployeeRate.compareTo( Env.ZERO) != 0 )
		{
			employeeAmount = this.salaryAdmissible.multiply( EmployeeRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) );
		}

		if ( EmployerRate == null || EmployerRate.compareTo( Env.ZERO) == 0)
		{
			this.employerAmount = ZERO;
			return;
		}

		
		//
		//
		employerAmount = employeeAmount.multiply( EmployerRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) );

		BigDecimal MaximumEmployer = Deduction_Param.getAnnualMaximumEmployer(); // new BigDecimal( 2500 );

		
		//+ 2010.12.03 seulement si l'employé a été engagé dans l'année.
		if ( TimeUtil.getYear( Employee.getDateHired() ) == TimeUtil.getYear( EmployeeDeduction.getEffectIn()) )
		{
			// maximum / 24 * nbr period restant
			P_Period startPeriod = getStartPeriod(Env.getCtx(), Payment.getP_Year_ID(), EmployeeDeduction, trxName);
		    BigDecimal pr = new BigDecimal( Frequency.getNumberOfPeriod() - startPeriod.getPeriodNo() + 1 );
			MaximumEmployer = MaximumEmployer.divide(new BigDecimal( Frequency.getNumberOfPeriod()),8,BigDecimal.ROUND_HALF_UP ).multiply( pr ).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		if ( cumEmployerAmount.add(employerAmount).compareTo(MaximumEmployer) > 0 )
		{
			// Si le cumulatif dépase 2500
			if ( cumEmployerAmount.compareTo( MaximumEmployer ) >= 0 )
			{
				employerAmount =  Env.ZERO;
			}
			else 
			{
				// cum 950 + 100 min 1000
				employerAmount = MaximumEmployer.subtract( cumEmployerAmount );
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
	
		
	}
	
	private P_Period getStartPeriod ( Properties ctx, int P_Year_ID, P_Employee_Deduction Employee_Deduction, String trxName )
	{
		//+ 2010.12.03
		Timestamp PeriodDate = null;
		try
		{
			String sql = "Select min( EFFECTIN ) From P_Employee_Deduction Where P_Employee_ID = " + Employee_Deduction.getP_Employee_ID() + " and P_Deduction_ID = " + Employee_Deduction.getP_Deduction_ID() ;
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				PeriodDate = rs.getTimestamp(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("getStartPeriod - get - " + e);
		}
		//- 2010.12.03
		
		String sql = "Select min(P_Period_ID) From P_Period Where P_Year_ID = " + P_Year_ID;

		PreparedStatement pstmt = null;

		int Period_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				Period_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("getStartPeriod - get - " + e);
		}

		sql = "Select min(P_Period_ID) From P_Period Where " + DB.TO_DATE(PeriodDate) + " Between StartDate and EndDate ";
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
			{
				if ( Period_ID < rs.getInt(1))
					Period_ID = rs.getInt(1);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("getStartPeriod - get - " + e);
		}
		
		return P_Period.get( ctx, Period_ID , trxName);
		
	}

	
/*	
	private void calcul_EUPP( boolean Reer )  throws Exception
	{
		get_deduction_cumulative_EUPP( );
        
		
		BigDecimal EmployeeRate = EmployeeDeduction.getEmployeeRate(Deduction_Param ); 
		BigDecimal EmployerRate = EmployeeDeduction.getEmployerRate(Deduction_Param ); 

		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param );

		BigDecimal EmployeeAmount = EmployeeDeduction.getEmployeeAmount(Deduction_Param );
		BigDecimal EmployerAmount = EmployeeDeduction.getEmployerAmount(Deduction_Param ); 

		
	    employeeAmount = ZERO;
	    employerAmount = ZERO;

		if ( EmployeeRate != null && EmployeeRate.compareTo( Env.ZERO) != 0 )
		{
			employeeAmount = this.salaryAdmissible.multiply( EmployeeRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) );
		}

		if ( EmployerRate == null || EmployerRate.compareTo( Env.ZERO) == 0)
		{
			this.employerAmount = ZERO;
			return;
		}

		
		//
		// Si le taux au dossier employé entre employé et employer est déjà différent.
		// 2008-04-04
		//
		if ( EmployerRate != null && EmployerRate.compareTo( Env.ZERO) != 0 && EmployerRate.compareTo(EmployeeRate) != 0  )
		{
			employerAmount = this.salaryAdmissible.multiply( EmployerRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) );

			BigDecimal min = new BigDecimal( 2500 );
			if ( cumEmployerAmount.add(employerAmount).compareTo(min) > 0 )
			{
				// Si le cumulatif dépase 2500
				if ( cumEmployerAmount.compareTo(min ) >= 0 )
				{
					employerAmount =  Env.ZERO;
				}
				else 
				{
					// cum 950 + 100 min 1000
					employerAmount = min.subtract( cumEmployerAmount );
				}
			}

			
		}
		else
		{
			String sql = "Select * FROM  P_Deduction_Param_Ympe "
			           + "WHERE P_Deduction_Param_ID = " + Deduction_Param.getP_Deduction_Param_ID() 
				       + " ORDER BY YMPEMIN Desc " 
				       ;

			PreparedStatement pstmt = null;
			PreparedStatement pstmt2 = null;
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();

			pstmt2 = DB.prepareStatement (sql, trxName);
			ResultSet rs2 = pstmt2.executeQuery ();

			boolean found = false; 

			rs2.next ();
			
			BigDecimal newEmployerRateTst = Env.ZERO;
			while (rs.next () && ! found )
			{
				//   0%  2005$ - x
				//  50%  1000$ - 2500$ $
				// 100%  0$ - 1000$ $
				
				if ( rs2.next ())
					newEmployerRateTst = EmployerRate.multiply( rs2.getBigDecimal("YmpeEmployerRate").divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP) );
				else
					newEmployerRateTst = EmployerRate.multiply( rs.getBigDecimal("YmpeEmployerRate").divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP) );
					
				
		    	BigDecimal min = rs.getBigDecimal("YmpeMin");
				BigDecimal newEmployerRate = EmployerRate.multiply( rs.getBigDecimal("YmpeEmployerRate").divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP) ); 

//				employerAmount =  this.salaryAdmissible.multiply( EmployerRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ) ;
				employerAmount =  this.salaryAdmissible.multiply( newEmployerRateTst.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP )  ) ;
				
				if ( cumEmployerAmount.add(employerAmount).compareTo(min) > 0 )
				{
					found = true;
					// Si le cumulatif dépase déjà la borne
					if ( cumEmployerAmount.compareTo(min ) > 0 )
					{
						employerAmount =  this.salaryAdmissible.multiply( newEmployerRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ) ;
					}
					else 
					{
						// cum 950 + 100 min 1000
						employerAmount = min.subtract( cumEmployerAmount );
						BigDecimal salary = this.salaryAdmissible.subtract( employerAmount.divide(EmployerRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ),2,BigDecimal.ROUND_HALF_UP) );   
						employerAmount =  employerAmount.add( salary.multiply( newEmployerRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ) );
					}
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
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

		
		
	}
*/


	// Calcul des saisie à pourcentage.
	// le calcul égale le salaire admissible moins les déductions obligatoires.
	private void calcul_saisie_percentage() throws Exception
	{

		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param );

		//RRQ
		BigDecimal MandatotyDeductionAmount = Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "002");

		//RPC
		MandatotyDeductionAmount = MandatotyDeductionAmount.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "003") );

		// EI
		MandatotyDeductionAmount = MandatotyDeductionAmount.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "004") );

		// impot
		MandatotyDeductionAmount = MandatotyDeductionAmount.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "009") );
		MandatotyDeductionAmount = MandatotyDeductionAmount.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "010") );
		MandatotyDeductionAmount = MandatotyDeductionAmount.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "011") );
		MandatotyDeductionAmount = MandatotyDeductionAmount.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "012") );

		salaryAdmissible = salaryAdmissible.subtract(MandatotyDeductionAmount);
		
		BigDecimal EmployeeRate = EmployeeDeduction.getEmployeeRate(Deduction_Param ); 
		BigDecimal EmployerRate = EmployeeDeduction.getEmployerRate(Deduction_Param ); 

		BigDecimal EmployeeAmount = EmployeeDeduction.getEmployeeAmount(Deduction_Param );
		BigDecimal EmployerAmount = EmployeeDeduction.getEmployerAmount(Deduction_Param ); 
		
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

	}

	//
	// calcul_nunavute/nwt
	//
	private void calcul_nunavut_nwt (  ) throws Exception
	{
		
		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param );

		BigDecimal EmployeeRate = EmployeeDeduction.getEmployeeRate(Deduction_Param ); 
		BigDecimal EmployerRate = EmployeeDeduction.getEmployerRate(Deduction_Param ); 

		BigDecimal EmployeeAmount = EmployeeDeduction.getEmployeeAmount(Deduction_Param );
		BigDecimal EmployerAmount = EmployeeDeduction.getEmployerAmount(Deduction_Param ); 

		
	    employeeAmount = ZERO;
	    employerAmount = ZERO;
	    
		if ( EmployeeRate != null && EmployeeRate.compareTo( Env.ZERO) != 0 )
		{
			employeeAmount = salaryAdmissible.multiply( EmployeeRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) );
		}
			
		if ( EmployerRate != null && EmployerRate.compareTo( Env.ZERO) != 0)
		{
			employerAmount =  salaryAdmissible.multiply( EmployerRate.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ) ;
		}

		//
		// Si l'employé habite au nunavut ou dans les territoires du nord west alors l'employé paie le 2% de taxe
		// Sinon si l'employé travail au nunavut sans y résider alors l'employer paie le 2% de taxe.
		//
		//+2012.02.21 
		if ( Deduction.getResidence_Region_ID() != RegionResidence.getC_Region_ID() && Deduction.getResidence_Region_ID() != Workplace.getC_Region_ID() )
		{
			employeeAmount = Env.ZERO;
		}
		//-2012.02.21 

		if (  ( RegionResidence.getC_Region_ID() == NUNAVUT || RegionResidence.getC_Region_ID() == NWT  || Workplace.getC_Region_ID() == NUNAVUT || Workplace.getC_Region_ID() == NWT))
		{
			this.employerAmount = Env.ZERO;
		}
		else if (( Employee.getTaxation_Region_ID() == NUNAVUT || Employee.getTaxation_Region_ID() == NWT ) )
		{
			this.employeeAmount = Env.ZERO;
		}
		else
		{
			this.employerAmount = Env.ZERO;
			this.employeeAmount = Env.ZERO;
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
		
		if ( this.employeeAmount.compareTo(Env.ZERO ) == 0 && this.employerAmount.compareTo(Env.ZERO ) == 0 )
		{
			this.salaryAdmissible = Env.ZERO;
			this.hoursAdmissible = Env.ZERO;
				
		}
	
		
	}   // calcul_nunavut_nwt

	// Working_Conditions
	private void calcul_deduction_Working_Conditions (  ) throws Exception
{
		
		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param , Deduction_Param.getMaximumAllowableEarnings() );

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

		
	}   // calcul_deduction_Working_Conditions

	private void calcul_saisie (  ) throws Exception
	{
		
		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param );

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

		if ( this.employeeAmount.compareTo(Env.ZERO) <= 0 ) this.employeeAmount = Env.ZERO;
		if ( this.employerAmount.compareTo(Env.ZERO) <= 0 ) this.employerAmount = Env.ZERO;


		
	}   // calcul_saisie

	
	
	//
	// calcul_deduction_others
	//
	private void calcul_deduction_others (  ) throws Exception
	{
		
		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param );

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
			
	//2026-01-12 ajout exception si l'employé n'a pas d'heures admissible		
			if ( this.hoursAdmissible.compareTo(Env.ZERO) != 0)
			   	ExemptionAmount = ExemptionAmount.multiply( this.hoursAdmissible.divide(AssignmentParam.getWeekly_Hours(), 2,BigDecimal.ROUND_HALF_UP));
//			   	ExemptionAmount = ExemptionAmount.multiply( this.hoursAdmissible.divide(AssignmentParam.getWeekly_Hours().multiply(new BigDecimal(2)), 2,BigDecimal.ROUND_HALF_UP));

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
	

	//
	// calcul_deduction_others
	//
	private void calcul_deduction_70 (  ) throws Exception
	{
		
		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param );

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

	
	
	//
	// Read Cumulative information about a deduction for this Year..
	// 
	private void get_deduction_cumulative( ) throws Exception
	{
		String query = null;
		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible"
			+ " from P_Payment_Deduction, P_Payment"
			+ " where P_Payment_Deduction.P_Deduction_ID = " + EmployeeDeduction.getP_Deduction_ID ()
			+ " and P_Payment.P_Year_ID = " + this.Period.getP_Year_ID ()
			+ " and P_Payment_Deduction.P_Period_ID <> " + this.Period.getP_Period_ID()
			+ " and P_Payment.P_Payment_ID <> " + Payment.getP_Payment_ID()
			+ " and P_Payment_Deduction.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
			+ " and P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID "
			;

		// 20100903 Ne pas tenir comptes des paiements annulé
		query += " AND P_Payment.PaymentTypeDoc not like 'Annulation%' ";

		//+ 2011.03.01 ajout la gestion d<un changement de province
		query += " AND P_Payment.Taxation_Region_ID = " + Payment.getTaxation_Region_ID();
        //- 2011.03.01

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
				this.cumEmployeeAmount =     ZERO;
				this.cumEmployerAmount =     ZERO;
				this.cumSalaryAdmissible =   ZERO;
				this.cumHoursAdmissible =    ZERO;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			if ( this.cumEmployeeAmount == null )
				this.cumEmployeeAmount =     ZERO;
			if ( this.cumEmployerAmount == null)
				this.cumEmployerAmount =     ZERO;
			if ( this.cumSalaryAdmissible == null)
				this.cumSalaryAdmissible =   ZERO;
			if (this.cumHoursAdmissible == null)
				this.cumHoursAdmissible =    ZERO;
			
	}	
	//
	// Read Cumulative information about a deduction for all Year sin validation..
	// 
	private void get_deduction_cumulative_EUPP( ) throws Exception
	{
		String query = null;
		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible"
			+ " from P_Payment_Deduction, P_Payment"
			+ " where P_Payment_Deduction.P_Deduction_ID IN ( 1002717, 1002718 ) " // + EmployeeDeduction.getP_Deduction_ID ()
			+ " and P_Payment_Deduction.P_Period_ID <= " + this.Period.getP_Period_ID()
			+ " and P_Payment_Deduction.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
			+ " and P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID "
//			+ " and P_Payment_Deduction.P_Period_ID >= dbo.FN_PeriodDeductionNonAnnual( " + this.Payment.getP_Employee_ID ()+ " , " + EmployeeDeduction.getP_Deduction_ID () + " )"
			;

		//+ 2011.03.01 ajout la gestion d<un changement de province
		query += " AND P_Payment.Taxation_Region_ID = " + Payment.getTaxation_Region_ID();
        //- 2011.03.01

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
				this.cumEmployeeAmount =     ZERO;
				this.cumEmployerAmount =     ZERO;
				this.cumSalaryAdmissible =   ZERO;
				this.cumHoursAdmissible =    ZERO;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			if ( this.cumEmployeeAmount == null )
				this.cumEmployeeAmount =     ZERO;
			if ( this.cumEmployerAmount == null)
				this.cumEmployerAmount =     ZERO;
			if ( this.cumSalaryAdmissible == null)
				this.cumSalaryAdmissible =   ZERO;
			if (this.cumHoursAdmissible == null)
				this.cumHoursAdmissible =    ZERO;
			
	}	


	// 2009.11.11
	//
	// Fonction qui permet de lire le salaire admissible total pour l'année en cours et le column d'admissibilité en cours.
	//
	private BigDecimal getTotalSalaryAdm( int P_Column_Adm_ID ) throws Exception
	{
		BigDecimal totalSalaryAdm = Env.ZERO;
		
		String query = "Select sum( AmountCalc ) Amount "
		      + " from p_payment_gain, p_payment " 
		      + " where p_payment.p_payment_id = p_payment_gain.p_payment_id "
		      + " and p_payment.p_year_id =  "  + Payment.getP_Year_ID()
//		      + " and p_payment.taxation_region_id = " + Payment.getTaxation_Region_ID()
		      +	" and p_payment.p_employee_id = " + Payment.getP_Employee_ID()
		      + " and P_Gain_ID in ( "
		      + "  select p_gain_id from p_gain_column "
		      + " inner join p_gain_Parameter on  p_gain_Parameter.p_gain_Parameter_id = p_gain_column.p_gain_Parameter_id "
		      + "   where P_Column_Adm_ID = " + P_Column_Adm_ID
		      + " and isadmissible = 'Y' "
		      +  " )" 
				// 2025-10-01 Ne pas tenir comptes des ajustment
			  + " AND P_Payment.PaymentTypeDoc <> 'Adjustement' "
		      ;

		        
		String query2 = "Select sum( taxable_benefit_amount ) Amount "
		      + " from p_payment_taxable_benefit , p_payment where p_payment.p_payment_id = p_payment_taxable_benefit.p_payment_id "
		      + " and p_payment.p_year_id =  " + Payment.getP_Year_ID()
//		      + " and p_payment.taxation_region_id = " + Payment.getTaxation_Region_ID() 
		      + " and p_payment.p_employee_id = " + Payment.getP_Employee_ID()
		      + " and P_taxable_benefit_ID in ( "
		      + " select p_taxable_benefit_id from p_taxable_benefit_column "
		      + " where P_Column_Adm_ID = " + P_Column_Adm_ID
		      + " and isadmissible = 'Y' "
		      + "  ) "
				// 2025-10-01 Ne pas tenir comptes des ajustment
				  + " AND P_Payment.PaymentTypeDoc <> 'Adjustement' "
		      ;
		
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			totalSalaryAdm = rs.getBigDecimal("Amount");
		}
		else
		{
			totalSalaryAdm = Env.ZERO;
		}
		rs.close ();
		pstmt.close ();

		pstmt = DB.prepareStatement (query2, trxName);
	    rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			// 2009.11.24 - ajout de la condition != null
			if ( totalSalaryAdm == null )
				totalSalaryAdm = Env.ZERO;
			
			
			if ( rs.getBigDecimal("Amount") != null)
				totalSalaryAdm = totalSalaryAdm.add( rs.getBigDecimal("Amount") );
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
		if (totalSalaryAdm == null)
			return Env.ZERO;
		
		return totalSalaryAdm;

	}

	private int getNbrDeduction ( int P_Deduction_Family_ID ) throws Exception
	{
		int result = 0;
		String query = null;
		query = " Select count(*) from ( "
            + " Select P_Payment_Deduction.P_Deduction_ID " 
            + " from P_Payment_Deduction, P_Deduction " 
			+ " where P_Payment_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID " 
			+ " and P_Payment_Deduction.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID () 
			+ " and P_Payment_Deduction.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
			+ " and P_Deduction.P_Deduction_Family_ID = " + P_Deduction_Family_ID 
			+ " group by P_Payment_Deduction.P_Deduction_ID ) as detail"
			;
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			result = rs.getInt(1);
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
		
		return result;
			
	}

	private int getNbrProvince ( ) throws Exception
	{
		int result = 0;
		String query = null;
		query = " Select count(*) from ( "
            + " Select P_Payment.Taxation_Region_ID " 
            + " from P_Payment " 
			+ " where P_Payment.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID () 
			+ " and P_Payment.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
			+ " group by P_Payment.Taxation_Region_ID ) as detail";
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			result = rs.getInt(1);
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
		
		return result;
			
	}


	
	//
	// Read Cumulative information about a deduction for this Year..
	// 
	private void get_deduction_cumulative_family( int P_Deduction_Family_ID ) throws Exception
	{
		String query = null;
		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible"
			+ " from P_Payment_Deduction, P_Payment, P_Deduction "
			+ " where P_Payment_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID " 
			+ " and P_Payment.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID ()  // <-- Correction pour un année antérieur ne doit pas aller revoir le cumulatif.
//2009.11.02
			+ " and P_Payment_Deduction.P_Period_ID <> " + this.Period.getP_Period_ID()			
			
//			+ " and P_Payment.P_Period_ID <= " + this.Period.getP_Period_ID()
// 2009.04.22			+ " and P_Payment.P_Period_ID <> " + this.Period.getP_Period_ID()

			//27 mai 2008
			// bug quand un employé a du RRQ et RPC sur la meme paie ou 2 déduction RRQ/RPC pour des périodes différents.
// 2009.04.22			+ " and ( P_Payment.P_Payment_ID <> " + Payment.getP_Payment_ID() + " OR ( P_Payment.P_Payment_ID = " + Payment.getP_Payment_ID() + " AND ( P_Payment_Deduction.P_Deduction_ID != " + this.Deduction.getP_Deduction_ID() + " OR P_Payment_Deduction.P_Period_ID != " + Period.getP_Period_ID() + ") ) )" 
//			+ " and P_Payment.P_Payment_ID <> " + Payment.getP_Payment_ID()
			+ " and P_Payment_Deduction.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
			+ " and P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID "
			+ " and P_Deduction.P_Deduction_Family_ID = " + P_Deduction_Family_ID 
			;

		//+ 2011.03.01 ajout la gestion d<un changement de province
//		if ( ! allRegion )
//		query += " AND P_Payment.Taxation_Region_ID = " + Payment.getTaxation_Region_ID();
        //- 2011.03.01

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
				this.cumEmployeeAmount =     ZERO;
				this.cumEmployerAmount =     ZERO;
				this.cumSalaryAdmissible =   ZERO;
				this.cumHoursAdmissible =    ZERO;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			if ( this.cumEmployeeAmount == null )
				this.cumEmployeeAmount =     ZERO;
			if ( this.cumEmployerAmount == null)
				this.cumEmployerAmount =     ZERO;
			if ( this.cumSalaryAdmissible == null)
				this.cumSalaryAdmissible =   ZERO;
			if (this.cumHoursAdmissible == null)
				this.cumHoursAdmissible =    ZERO;
			
	}	

	//
	// Read Cumulative information about a deduction for this Year..
	// 
	private void get_deduction_cumulative_family_rrq( int P_Deduction_Family_ID ) throws Exception
	{
		String query = null;
/*		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible"
			+ " from P_Payment_Deduction, P_Payment, P_Deduction "
			+ " where P_Payment_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID " 
			+ " and P_Payment.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID ()  // <-- Correction pour un année antérieur ne doit pas aller revoir le cumulatif.
			+ " and P_Payment_Deduction.P_Period_ID <> " + this.Period.getP_Period_ID()			
			+ " and P_Payment_Deduction.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
			+ " and P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID "
			+ " and P_Deduction.P_Deduction_Family_ID = " + P_Deduction_Family_ID
			+ " and P_deduction.value not in ( 'DA01B','DA02B' ) "
			+ " and P_Method_Deduction_ID <> 201 "
			;
*/
		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible"
				+ " from P_Payment_Deduction"
				+ "  INNER JOIN P_Payment ON P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID  "
				+ "  INNER JOIN  P_Deduction ON P_Payment_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID "
				+ "  INNER JOIN P_Deduction_Param ON P_Deduction_Param.P_Deduction_ID = P_Deduction.P_Deduction_ID  "
				+ " WHERE  " 
				+ " P_Payment.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID ()  // <-- Correction pour un année antérieur ne doit pas aller revoir le cumulatif.
				+ " and P_Payment_Deduction.P_Period_ID <> " + this.Period.getP_Period_ID()			
				+ " and P_Payment_Deduction.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
				+ " and P_Deduction.P_Deduction_Family_ID = " + P_Deduction_Family_ID
				+ " and P_Method_Deduction_ID NOT IN ( 201, 202 ) "
				;
		
		//+ 2011.03.01 ajout la gestion d<un changement de province
//		if ( ! allRegion )
//		query += " AND P_Payment.Taxation_Region_ID = " + Payment.getTaxation_Region_ID();
        //- 2011.03.01

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
				this.cumEmployeeAmount =     ZERO;
				this.cumEmployerAmount =     ZERO;
				this.cumSalaryAdmissible =   ZERO;
				this.cumHoursAdmissible =    ZERO;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			if ( this.cumEmployeeAmount == null )
				this.cumEmployeeAmount =     ZERO;
			if ( this.cumEmployerAmount == null)
				this.cumEmployerAmount =     ZERO;
			if ( this.cumSalaryAdmissible == null)
				this.cumSalaryAdmissible =   ZERO;
			if (this.cumHoursAdmissible == null)
				this.cumHoursAdmissible =    ZERO;
			
	}	

	private void get_deduction_cumulative_family_rrq2( int P_Deduction_Family_ID ) throws Exception
	{
		String query = null;
		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible"
			+ " from P_Payment_Deduction"
			+ "  INNER JOIN P_Payment ON P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID  "
			+ "  INNER JOIN  P_Deduction ON P_Payment_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID "
			+ "  INNER JOIN P_Deduction_Param ON P_Deduction_Param.P_Deduction_ID = P_Deduction.P_Deduction_ID  "
			+ " WHERE  " 
			+ " P_Payment.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID ()  // <-- Correction pour un année antérieur ne doit pas aller revoir le cumulatif.
			+ " and P_Payment_Deduction.P_Period_ID <> " + this.Period.getP_Period_ID()
			+ " and P_Payment_Deduction.P_Period_ID < " + this.Period.getP_Period_ID()
			+ " and P_Payment_Deduction.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
			+ " and P_Deduction.P_Deduction_Family_ID = " + P_Deduction_Family_ID
			;

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
				this.cumEmployeeAmount =     ZERO;
				this.cumEmployerAmount =     ZERO;
				this.cumSalaryAdmissible =   ZERO;
				this.cumHoursAdmissible =    ZERO;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			if ( this.cumEmployeeAmount == null )
				this.cumEmployeeAmount =     ZERO;
			if ( this.cumEmployerAmount == null)
				this.cumEmployerAmount =     ZERO;
			if ( this.cumSalaryAdmissible == null)
				this.cumSalaryAdmissible =   ZERO;
			if (this.cumHoursAdmissible == null)
				this.cumHoursAdmissible =    ZERO;
			
	}	

	
	
	//
	// Check cumulative amount for a deduction for all years
	// 
	private void get_deduction_cumulated_allYears(  ) throws Exception
	{
		BigDecimal employeePartCumul = Env.ZERO;
		BigDecimal maxAllowedCurrent = Env.ZERO;

		BigDecimal NonAnnualMaximum = EmployeeDeduction.getNonAnnualMaximum(Deduction_Param ); 
		if(NonAnnualMaximum != null && NonAnnualMaximum.compareTo(Env.ZERO) == 1)
		{
			String query = null;
			//2010.05.03 - gestion du maximum même lors d'un arrérage.
			//			query = "Select isnull( sum( Employee_Part ),0) Employee_Part"
			query = "Select isnull( sum( Employee_Part ),0) + isnull( sum( accumulationAmount ),0) - isnull( sum( RetrievalAmount ),0) Employee_Part"
				+ " from P_Payment_Deduction"
				+ " where P_Payment_Deduction.P_Deduction_ID = " + EmployeeDeduction.getP_Deduction_ID ()
				+ " and P_Payment_Deduction.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
				+ " and P_Payment_Deduction.P_Period_ID >= dbo.FN_PeriodDeductionNonAnnual( " + this.Payment.getP_Employee_ID ()+ " , " + EmployeeDeduction.getP_Deduction_ID () + " )"
				;

			PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement (query, trxName);
				ResultSet rs = pstmt.executeQuery ();
				if (rs.next ())
				{
					employeePartCumul =     rs.getBigDecimal("Employee_Part");
				}
				else
				{
					employeePartCumul =     ZERO;
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
				if ( employeePartCumul == null )
					employeePartCumul =     ZERO;
				
				boolean isMaxed = false;
				//on vérifie si il y a un maximum non annuel
				//si le montant cummule est plus petit ou egal au max
				if ((employeePartCumul.add(this.employeeAmount)).compareTo(NonAnnualMaximum) >= 0)
				{
					//le maximum courant est egal au Maximum non annuel moins le cumul annuel
					maxAllowedCurrent = NonAnnualMaximum.subtract(employeePartCumul);
					isMaxed = true;
				}
				
				//si le montant courant est superieur au montant courant permis
				//et qu'on a calcule un maximum non annuel, on cap
				if(this.employeeAmount.compareTo(maxAllowedCurrent) > 0 && isMaxed == true)
				{
					this.employeeAmount = maxAllowedCurrent;
				}
				
				if(employeePartCumul.compareTo(NonAnnualMaximum) > 0)
				{
					this.employeeAmount = Env.ZERO;
				}
			
		}

	}		

/*	//
	// Check cumulative amount for a deduction for a month
	private void get_deduction_max_month( ) throws Exception
	{
		String query = null;
		BigDecimal employeePartCumul = Env.ZERO;
		BigDecimal employerPartCumul = Env.ZERO;
		BigDecimal maxAllowedCurrent = Env.ZERO;
		BigDecimal maxAllowedCurrentYeur = Env.ZERO;
		//on vérifie si il y a un maximum annuel

		BigDecimal MonthMaximum     = Deduction_Param.getMonthMaximum(); 
		BigDecimal MonthMaximumYeur = Deduction_Param.getMonthMaximumEmployer(); 

		if( (MonthMaximumYeur != null && MonthMaximumYeur.compareTo(Env.ZERO) != 0) ||
			(MonthMaximum != null && MonthMaximum.compareTo(Env.ZERO) != 0)	
		  )
		{
			int Month = TimeUtil.getMonth( Period.getStartDate() );
			
			
			query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull( sum( Employer_Part ),0) Employer_Part "
				+ " from P_Payment_Deduction, P_Period"
				+ " where P_Payment_Deduction.P_Deduction_ID = " + EmployeeDeduction.getP_Deduction_ID ()
				+ " and P_Payment_Deduction.P_Period_ID = P_Period.P_Period_ID "  
				+ " and P_Payment_Deduction.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
				+ " and datepart( m," + DB.TO_DATE( Period.getStartDate() ) + ") = " + Month 
				
				;

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				employeePartCumul =     rs.getBigDecimal("Employee_Part");
				employerPartCumul =     rs.getBigDecimal("Employer_Part");
			}
			else
			{
				employeePartCumul =     ZERO;
				employerPartCumul =     ZERO;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			if ( employeePartCumul == null ) employeePartCumul =     ZERO;
			if ( employerPartCumul == null ) employerPartCumul =     ZERO;

			boolean isMaxedYeur = false;
			boolean isMaxed = false;

			if(employeePartCumul.compareTo(MonthMaximum) >= 0 )
			{
				//le maximum courant est egal au Maximum annuel moins le cumul annuel
				maxAllowedCurrent = MonthMaximum.subtract(employeePartCumul);
				isMaxed = true;
			}

			if(employerPartCumul.compareTo(MonthMaximumYeur) >= 0 )
			{
				//le maximum courant est egal au Maximum annuel moins le cumul annuel
				maxAllowedCurrentYeur = MonthMaximumYeur.subtract(employerPartCumul);
				isMaxedYeur = true;
			}

			//si le montant courant est superieur au montant courant permis
			//et qu'on a calcule un maximum annuel, on cap
			if(this.employeeAmount.compareTo(maxAllowedCurrent) == 1 && isMaxed == true)
			{
				this.employeeAmount = maxAllowedCurrent;
			}

			//si le montant courant est superieur au montant courant permis
			//et qu'on a calcule un maximum annuel, on cap
			if(this.employerAmount.compareTo(maxAllowedCurrentYeur) == 1 && isMaxedYeur == true)
			{
				this.employerAmount = maxAllowedCurrentYeur;
			}

		}
		
	}	
*/
	
	//
	// Check cumulative amount for a deduction for current years
	// 
	private void get_deduction_cumulated_curYears( ) throws Exception
	{
		String query = null;
		BigDecimal employeePartCumul = Env.ZERO;
		BigDecimal maxAllowedCurrent = Env.ZERO;
		//on vérifie si il y a un maximum annuel

		BigDecimal AnnualMaximum = null;

		AnnualMaximum = EmployeeDeduction.getAnnualMaximum(Deduction_Param ); 


		if(AnnualMaximum != null && AnnualMaximum.compareTo(Env.ZERO) == 1)
		{
			query = "Select isnull( sum( Employee_Part ),0) Employee_Part "
				+ " from P_Payment_Deduction"
				+ " where P_Payment_Deduction.P_Deduction_ID = " + EmployeeDeduction.getP_Deduction_ID ()
				+ " and P_Payment_Deduction.P_Year_ID = " + this.Period.getP_Year_ID ()
				+ " and P_Payment_Deduction.P_Employee_ID = " + this.Payment.getP_Employee_ID ();

			PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement (query, trxName);
				ResultSet rs = pstmt.executeQuery ();
				if (rs.next ())
				{
					employeePartCumul =     rs.getBigDecimal("Employee_Part");
				}
				else
				{
					employeePartCumul =     ZERO;
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
				if ( employeePartCumul == null )
					employeePartCumul =     ZERO;
				
				boolean isMaxed = false;
					//si le montant cummule est plus petit ou egal au max
					if ((employeePartCumul.add(this.employeeAmount)).compareTo(AnnualMaximum) >= 0)
					{
						//le maximum courant est egal au Maximum annuel moins le cumul annuel
						maxAllowedCurrent = AnnualMaximum.subtract(employeePartCumul);
						isMaxed = true;
					}
				
				//si le montant courant est superieur au montant courant permis
				//et qu'on a calcule un maximum annuel, on cap
				if(this.employeeAmount.compareTo(maxAllowedCurrent) == 1 && isMaxed == true)
				{
					this.employeeAmount = maxAllowedCurrent;
				}
		}
		
	}	
	
	//
	// Check cumulative amount for a deduction for this years
	// 
	private BigDecimal get_deduction_max_employeur( BigDecimal Amount, BigDecimal Maximum ) throws Exception
	{
		String query = null;
		BigDecimal employerPartCumul = Env.ZERO;
		BigDecimal maxAllowedCurrentYeur = Env.ZERO;
		//on vérifie si il y a un maximum annuel
//        if ( Deduction_Param == null || Deduction_Param.getAnnualMaximumEmployer() == null || Deduction_Param.getAnnualMaximumEmployer().compareTo(Env.ZERO) == 0  )
 //       	return this.employerAmount;
		
		BigDecimal AnnualMaximumYeur = null ;
		
		if ( Maximum != null)
		{
			AnnualMaximumYeur = Maximum; 
		}
		else
		{
			if ( EmployeeDeduction != null && Deduction_Param != null)
				AnnualMaximumYeur = EmployeeDeduction.getAnnualMaximumYeur(Deduction_Param ); 
			
		}

        if ( Deduction_Param == null || AnnualMaximumYeur == null || AnnualMaximumYeur.compareTo(Env.ZERO) == 0  )
        	return this.employerAmount;

		if( (AnnualMaximumYeur != null && AnnualMaximumYeur.compareTo(Env.ZERO) != 0) )
		{
			query = "Select isnull( sum( Employer_Part ),0) Employer_Part "
				+ " from P_Payment_Deduction"
				+ " where P_Payment_Deduction.P_Deduction_ID = " + EmployeeDeduction.getP_Deduction_ID ()
				+ " and P_Payment_Deduction.P_Year_ID = " + this.Period.getP_Year_ID ()
				+ " and P_Payment_Deduction.P_Employee_ID = " + this.Payment.getP_Employee_ID ();

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				employerPartCumul =     rs.getBigDecimal("Employer_Part");
			}
			else
			{
				employerPartCumul =     ZERO;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			if ( employerPartCumul == null )
				employerPartCumul =     ZERO;

			boolean isMaxedYeur = false;
		
			if ((employerPartCumul.add( Amount)).compareTo(AnnualMaximumYeur) >= 0 )
			{
				//le maximum courant est egal au Maximum annuel moins le cumul annuel
				maxAllowedCurrentYeur = AnnualMaximumYeur.subtract(employerPartCumul);
				isMaxedYeur = true;
			}

			//si le montant courant est superieur au montant courant permis
			//et qu'on a calcule un maximum annuel, on cap
			if( Amount.compareTo(maxAllowedCurrentYeur) == 1 && isMaxedYeur == true)
			{
				Amount = maxAllowedCurrentYeur;
			}
		}
		
		return Amount;
		
	}	

	
	
	//
	// TODO : creer une class independate qui retourn les 2 valeurs possibles au lieux d'avoir 4 variable global;
	// Read Cumulative information about a deduction for this Period..
	// 
	private void get_deduction_cumulative_per(  ) throws Exception
	{
		String query = null;
		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible, isnull(sum( RetrievalAmount),0) RetrievalAmount, isnull(sum( AccumulationAmount),0) AccumulationAmount "
			+ " from P_Payment_Deduction, P_Payment"
			+ " where P_Payment_Deduction.P_Deduction_ID = " + EmployeeDeduction.getP_Deduction_ID ()
			+ " and P_Payment_Deduction.P_Period_ID = " + Period.getP_Period_ID ()
			+ " and P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID ()
 		    + " and P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID AND P_Payment.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID ()  // <-- Correction pour un année antérieur ne doit pas aller revoir le cumulatif.
 		    // 2008-05-12 on ne doit pas inclure les ajustements, si on les inclus, le calcul va automatiquement l'annuler
// 		    + " and P_Payment_Deduction.Origine != 'AJT'"
 		    ;

		//		 2009.10.14
//			if ( ! Payment.getPaymentTypeDoc().equals(P_Payment.PAYMENTTYPEDOC_SeparationPay))
				query += " AND P_Payment.PaymentTypeDoc <> 'Separation'";

				
			// 2025-10-01 Ne pas tenir comptes des ajustment
			query += " AND P_Payment.PaymentTypeDoc <> 'Adjustement'";
				
				
				
			// 20100903 Ne pas tenir comptes des paiements annulé
			query += " AND P_Payment.PaymentTypeDoc not like 'Annulation%' ";

			//+ 2011.03.01 ajout la gestion d<un changement de province
			query += " AND P_Payment.Taxation_Region_ID = " + Payment.getTaxation_Region_ID();
	        //- 2011.03.01

			//+ 2024-01-10
			query += " AND P_Payment.P_Year_ID = " + Payment.getP_Year_ID();
			//+ 2024-01-10

		
		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " Query " + query ) ;
		
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
	}	


	

	private void get_deduction_cumulative_per_rrq(  ) throws Exception
	{
		String query = null;
		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible, isnull(sum( RetrievalAmount),0) RetrievalAmount, isnull(sum( AccumulationAmount),0) AccumulationAmount "
			+ " from P_Payment_Deduction, P_Payment"
			+ " where P_Payment_Deduction.P_Deduction_ID = " + EmployeeDeduction.getP_Deduction_ID ()
			+ " and P_Payment_Deduction.P_Period_ID = " + Period.getP_Period_ID ()
			+ " and P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID ()
 		    + " and P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID AND P_Payment.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID ()  // <-- Correction pour un année antérieur ne doit pas aller revoir le cumulatif.
 		    + " and P_Payment.P_Payment_ID != " + Payment.getP_Payment_ID()
 		    // 2008-05-12 on ne doit pas inclure les ajustements, si on les inclus, le calcul va automatiquement l'annuler
// 		    + " and P_Payment_Deduction.Origine != 'AJT'"
 		    ;

		//+ 2011.03.01 ajout la gestion d<un changement de province
//  		query += " AND P_Payment.Taxation_Region_ID = " + Payment.getTaxation_Region_ID();
        //- 2011.03.01

		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " Query " + query ) ;
		
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
	}	

	private boolean if_rrq_rpc(  ) throws Exception
	{
		boolean ret = false;
		String query = null;
		query = "Select 1 "
			+ " from P_Payment"
			+ " where P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID ()
 		    + " AND P_Payment.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID ()  
 		    + " and P_Payment.P_Payment_ID != " + Payment.getP_Payment_ID();

 		query += " AND P_Payment.Taxation_Region_ID = " + QUEBEC;

		
		PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				ret = true;
			}
			else
			{
				ret = false;
			}

			
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			return ret;
	}	

	//
	// TODO : creer une class independate qui retourn les 2 valeurs possibles au lieux d'avoir 4 variable global;
	// Read Cumulative information about a deduction for this Period..
	// 
	private void get_deduction_cumulative_per2(  ) throws Exception
	{
		String query = null;
		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible, isnull(sum( RetrievalAmount),0) RetrievalAmount, isnull(sum( AccumulationAmount),0) AccumulationAmount "
			+ " from P_Payment_Deduction" //, P_Payment"
			//+ 2011.03.01 ajout la gestion d<un changement de province			
   		    + "   INNER JOIN P_PAYMENT ON P_PAYMENT.P_PAYMENT_ID = P_Payment_Deduction.P_Payment_ID"
   		    //-
			+ " where P_Payment_Deduction.P_Deduction_ID = " + EmployeeDeduction.getP_Deduction_ID ()
			+ " and P_Payment_Deduction.P_Period_ID = " + Period.getP_Period_ID ()
			+ " and P_Payment_Deduction.P_Employee_ID = " + Payment.getP_Employee_ID ()
// 		    + " and P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID AND P_Payment.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID ()  // <-- Correction pour un année antérieur ne doit pas aller revoir le cumulatif.
			//
			// 2008-05-12 on ne doit pas inclure les ajustements, si on les inclus, le calcul va automatiquement l'annuler
// 		    + " and P_Payment_Deduction.Origine != 'AJT'"
 		    ;

		//+ 2024-01-10
		query += " AND P_Payment.P_Year_ID = " + Payment.getP_Year_ID();
		//+ 2024-01-10

		

		//+ 2011.03.01 ajout la gestion d<un changement de province
		query += " AND P_Payment.Taxation_Region_ID = " + Payment.getTaxation_Region_ID();
        //- 2011.03.01

		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " Query " + query ) ;
		
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
	}	

	
	private BigDecimal get_deduction_cumulative_Insurable(  ) throws Exception
	{
		BigDecimal EmprAmount = Env.ZERO;
		
		String query = null;
		query = "Select isnull(sum( Employer_Part),0) Employer_Part"
			+ " from P_Payment_Deduction" //, P_Payment"
   		    + "   INNER JOIN P_PAYMENT ON P_PAYMENT.P_PAYMENT_ID = P_Payment_Deduction.P_Payment_ID"
			+ " where P_Payment_Deduction.P_Period_ID = " + Period.getP_Period_ID ()
			+ " and P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID ()
			+ " and P_Payment_Deduction.P_Deduction_ID IN ( Select P_Deduction_ID From P_Deduction where P_Deduction.P_DEDUCTION_FAMILY_ID = 1000001 and P_Deduction.isactive = 'Y' )"
			;
				// 1000002 = Assurance
		
		PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				EmprAmount    =   rs.getBigDecimal("Employer_Part");
			}
			else
			{
				EmprAmount    =   ZERO;
			}


			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			return EmprAmount;
	}	

	
	//
	// Read Total of Taxable Benefit for the period
	//
	private BigDecimal get_TaxableBenefit_cumulative_per( P_Employee_Taxable_Benefit  Taxable_Benefit) throws Exception
	{
		BigDecimal cumBenefitAmount = ZERO;
		
		String query = null;
		query = "SELECT sum( Taxable_Benefit_Amount ) Taxable_Benefit_Amount " 
			  +	" FROM P_Payment_Taxable_Benefit"
			  + "   INNER JOIN P_PAYMENT ON P_PAYMENT.P_PAYMENT_ID = P_Payment_Taxable_Benefit.P_Payment_ID"
			  + " WHERE P_Payment_Taxable_Benefit.IsActive='Y' "  
		      + " AND P_Payment_Taxable_Benefit.P_Taxable_Benefit_ID = " + Taxable_Benefit.getP_Taxable_Benefit_ID()
              + " AND P_Payment_Taxable_Benefit.P_Period_ID = " + this.Period.getP_Period_ID ()
			  + " AND P_Payment_Taxable_Benefit.P_Employee_ID = " + Taxable_Benefit.getP_Employee_ID ()
//+2012.02.21 			  
//	    + " and P_Payment_Taxable_Benefit.Origine != 'AJT'"
//-2012.02.21 			  
//		2011.01.11			  
		  + " and P_Payment.P_Payment_ID <> " + Payment.getP_Payment_ID()
		  ;

		
		//+ 2011.03.01 ajout la gestion d<un changement de province
		query += " AND P_Payment.Taxation_Region_ID = " + Payment.getTaxation_Region_ID();
        //- 2011.03.01


		//Log.trace( 5, Taxable_Benefit.getValue() + " - " + Taxable_Benefit.getName() + " Query " + query ) ;

		
		PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				cumBenefitAmount =     rs.getBigDecimal("Taxable_Benefit_Amount");
			}
			else
			{
				cumBenefitAmount =     ZERO;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			if ( cumBenefitAmount == null )
				cumBenefitAmount =     ZERO;

		return cumBenefitAmount;
	}
	
	
	//
	// Read deduction employee amount
	//
	private BigDecimal getDeductionEmployeeAmountMethod ( String Method)
	{
		if ( s_Employee.containsKey( Method ) )
			return (BigDecimal)s_Employee.get( Method );
		
		return ZERO;

	}

	//
	// Get deduction employer amount
	//
	private BigDecimal getDeductionEmployerAmountMethod ( String Method)
	{
		if ( s_Employer.containsKey( Method ) )
			return (BigDecimal)s_Employer.get( Method );
		
		return ZERO;

	}

	private BigDecimal getDeductionSalaryAdmissibleMethod ( String Method)
	{
		if ( s_SalaryAdmissible.containsKey( Method ) )
			return (BigDecimal)s_SalaryAdmissible.get( Method );
		return ZERO;
	}

	
	
	//
	// Undo
	//
	public void undo( int PaymentID ) 
	{
		Payment  = P_Payment.get (Env.getCtx (), PaymentID, trxName );

		//log.info("Calcul - Payment - " + Payment.getP_Payment_ID ()  );
	
		//Log.trace( 5,"Calcul - Delele Deduction - "   );

		StringBuffer sql = new StringBuffer(  "Delete P_Payment_Deduction Where P_Payment_ID = ").append( Payment.getP_Payment_ID () );
		int no = DB.executeUpdate (sql.toString (), null);
		
		//Log.trace( 5,"Calcul - Delele Taxable Benefit - "   );
		
		sql = new StringBuffer(  "Delete P_Payment_Taxable_Benefit Where P_Payment_ID = ").append( Payment.getP_Payment_ID () );
		no = DB.executeUpdate (sql.toString (), null);
		
		//Log.trace( 5,"Calcul - Update statut Time Sheet "  );
		
		P_Time_Sheet Timesheet = P_Time_Sheet.GetWithPaymentID( Env.getCtx(), Payment.getP_Payment_ID(), trxName );
		Timesheet.setIsError( false );
		Timesheet.setTimeSheetStatus( P_Time_Sheet.TIMESHEETSTATUS_Validated );
		Timesheet.save( );

	}

	private void setTrxName( String _trxName)
	{
		trxName = _trxName;
	}


	public Hashtable<String, QuantityAmountPair> getS_Matrix()
	{
	  return s_Matrix;
	}

	public Hashtable<String, QuantityAmountPair> getS_MatrixTaxB()
	{
	  return s_MatrixTaxB;
	}

	public Hashtable<String, BigDecimal> getS_Employee()
	{
	  return s_Employee;
	}
	public Hashtable<String, BigDecimal> getS_Employer()
	{
	  return s_Employer;
	}

	public Hashtable getS_SalaryAdmissible()
	{
	  return s_SalaryAdmissible;
	}

	
	public Hashtable<String, BigDecimal> getTaxTable()
	{
	  return taxTable;
	}

    public BigDecimal getAmount_Employment_Insurance()
    {
    	return Amount_Employment_Insurance;
    }

    public BigDecimal getAmount_RQAP()
    {
    	return Amount_RQAP;
    }

    public BigDecimal getAmount_Rpc()
    {
    	
		BigDecimal rpc_max = ZERO;
		
		if ( Employee.getTaxation_Region_ID() == QUEBEC)
		{
			rpc_max       = (BigDecimal)taxTable.get( "RRQ.4");
		}
		else
		{
			rpc_max       = (BigDecimal)taxTable.get( "RPC.4");
		}

    	if ( Amount_Rpc.compareTo(rpc_max) > 0 )
    			return rpc_max;
    			
    	return Amount_Rpc;
    }
    
  //Modif pour 2024 	
    public BigDecimal getAmount_Rpc_QC()
    {
    	return Amount_Rpc_QC;
    }

    public BigDecimal getAmount_Rpc_F5()
    {
		BigDecimal max = ZERO;
		
		if ( Employee.getTaxation_Region_ID() == QUEBEC)
		{
			max       = (BigDecimal)taxTable.get( "RRQ2.4");
		}
		else
		{
			max       = (BigDecimal)taxTable.get( "RPC2.4");
		}

    	if ( Amount_Rpc_F5.compareTo(max) > 0 )
    			return max;
    			

    	return Amount_Rpc_F5;
    }

    public BigDecimal getAmount_Rpc2()
    {
    	return Amount_Rpc2;
    }

    
    public BigDecimal getSalary_Rpc()
    {
    	return Salary_Rpc;
    }
	//Modif pour 2024 	
    public BigDecimal getSalary_Rpc2()
    {
    	return Salary_Rpc2;
    }

	 
	public P_Payment   getP_Payment()
	{
		return Payment;
	}
	public P_Employee  getP_Employee()
	{
		return Employee;
	}
	public P_Period    getP_Period()
	{
		return Period;
	}
	
	public P_Period    getP_PeriodLine()
	{
		return PeriodLine;
	}

	
	public P_Frequency getP_Frequency() 
	{
		return Frequency;
	}
	public P_Employee_Deduction getP_EmployeeDeduction()
	{
		return EmployeeDeduction;
	}
	public P_Deduction_Param    getP_Deduction_Param() 
	{
		return Deduction_Param;
	}

	
	public void	setEmployeeAmount( BigDecimal amount)
	{
		employeeAmount = amount;
	}

	public BigDecimal	getEmployeeAmount( )
	{
		return employeeAmount;
	}

	public void	setEmployerAmount( BigDecimal amount)
	{
		employerAmount = amount;
	}

	public BigDecimal	getHoursAdmissible( )
	{
		return hoursAdmissible;
	}

	public BigDecimal	getSalaryAdmissible( )
	{
		return salaryAdmissible;
	}

	
	public void	setHoursAdmissible( BigDecimal amount)
	{
		hoursAdmissible = amount;
	}

	public void	setSalaryAdmissible( BigDecimal amount)
	{
		salaryAdmissible = amount;
	}
	
	//
	// Reverse Ingenering. recois la part employé et calcul le salaire admissible et le part employer
	//
	//
	// Reverse Ingenering. recois la part employé et calcul le salaire admissible et le part employer
	//
	public void calculDeduction( P_Payment_Deduction Payment_Deduction, P_Deduction_Param Deduction_Param , String trxName)
	{
		
		Hashtable<String, BigDecimal> 	taxTable = new Hashtable<String, BigDecimal>();

		taxTable.clear();

		P_Payment Payment = P_Payment.get( Env.getCtx(), Payment_Deduction.getP_Payment_ID(), trxName);
		P_Region Region = P_Region.get( Env.getCtx (), Payment.getTaxation_Region_ID(), trxName);
		P_Period Period = P_Period.get(Env.getCtx(), Payment.getP_Period_ID(), trxName);
		
		try
		{
			int taxID = Calcul_federal_tax.get_federal_tax_id( Period.getPayDate() );
			taxTable = Calcul_federal_tax.get_federal_tax_para(  taxID, Period.getPayDate() );
			
//	        System.out.println("DEBUG Calcul TaxTable  ID = " + taxID );

		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"calcul reverse ingenering- ", e);
		}

		BigDecimal Salary_Eligible = Env.ZERO;
		BigDecimal Hours_Eligible = Env.ZERO;
		BigDecimal EmployerPart = Env.ZERO;
	
		P_Method_Deduction Method_Deduction  = P_Method_Deduction.get( Env.getCtx (), Deduction_Param.getP_Method_Deduction_ID() , trxName);

		String    methodCalcul     = Method_Deduction.getValue();

		int       i_methodCalcul   = new Integer( methodCalcul ).intValue();

		if ( i_methodCalcul != 2 && i_methodCalcul != 3 && i_methodCalcul != 4 & i_methodCalcul != 7 && i_methodCalcul != 31 )
			  return;

		P_Frequency Frequency = P_Frequency.get( Env.getCtx(), Payment.getP_Frequency_ID() , trxName);
		
		switch ( i_methodCalcul ) 
		{
			case 2: // RRQ, RPC
			case 3:
				BigDecimal rpc_rate      = (BigDecimal)taxTable.get( "RPC.1");
				rpc_rate = rpc_rate.divide( new BigDecimal( 100 ), 4, BigDecimal.ROUND_HALF_UP );

				BigDecimal exemption = (BigDecimal)taxTable.get( "RPC.3").divide( new BigDecimal( Frequency.getNumberOfPeriod()) , 2, BigDecimal.ROUND_HALF_UP);

				Salary_Eligible = Payment_Deduction.getEmployee_Part().divide(rpc_rate,2,BigDecimal.ROUND_HALF_UP);
				Salary_Eligible = Salary_Eligible.add( exemption);

				EmployerPart = Payment_Deduction.getEmployee_Part();
				
			break;
			case 4: 	// Employment insurance 
				BigDecimal ei_rate_employee = ZERO;
				BigDecimal ei_rate_employer = ZERO;
				if ( Region.getName().trim().equals(  "Québec" ) || Region.getName().trim().equals("QC") )  
				{
					ei_rate_employee = (BigDecimal)taxTable.get( "AE.2");
				}
				else
				{
					ei_rate_employee = (BigDecimal)taxTable.get( "AE.5");
				}
				ei_rate_employee = ei_rate_employee.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP );

				P_Employer Employer = P_Employer.get( Env.getCtx(), Payment.getP_Employer_ID(), trxName ); 
				if ( Employer.isReducedEI() )
				{
					ei_rate_employer = (BigDecimal)taxTable.get( "AE.42");
					// 	TODO Hardcoder ( Wachiwem )
					if ( Payment.getAD_Org_ID() == 1000007 )
						ei_rate_employer = (BigDecimal)taxTable.get( "AE.43");
				}	
				else
					ei_rate_employer = (BigDecimal)taxTable.get( "AE.41");

				Salary_Eligible = Payment_Deduction.getEmployee_Part().divide(ei_rate_employee,2,BigDecimal.ROUND_HALF_UP);
				EmployerPart = Payment_Deduction.getEmployee_Part().multiply(ei_rate_employer).setScale(2, BigDecimal.ROUND_HALF_UP);

				
			break;
			case 7: // FTQ 
				Salary_Eligible = Env.ZERO;
				EmployerPart = Payment_Deduction.getEmployee_Part();
				
			break;

			case 31: // RQAP 	
				BigDecimal rqap_rate_employee = ((BigDecimal)taxTable.get( "RQAP.2")).divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);
				BigDecimal rqap_rate_employer = ((BigDecimal)taxTable.get( "RQAP.4")).divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);

				Salary_Eligible = Payment_Deduction.getEmployee_Part().divide(rqap_rate_employee,2,BigDecimal.ROUND_HALF_UP);
				EmployerPart = 	Salary_Eligible.multiply( rqap_rate_employer ).setScale(2, BigDecimal.ROUND_HALF_UP);
 

			break;
		}
		
		
		Payment_Deduction.setSalary_Eligible( Salary_Eligible );
		Payment_Deduction.setHours_Eligible( Hours_Eligible );
		Payment_Deduction.setEmployer_Part( EmployerPart );
		
	}

/*
	private void UpdatePaymentDistribution(int Payment_ID) throws SQLException
    {
	   	
	  	// Vérification de la période sélectionné
	    P_Payment Payment = null;
	    P_Employee_Payment_Dist Emp_Distribution = null;
	  	P_Payment_Distribution Payment_dist = null; 
		int NoPaiment = 0;
		int Count=0;
	  	//log.debug("Juste avant Try");
		
		String SqlDel = "Delete from P_Payment_Distribution where P_Payment_ID = " +  Payment_ID ;
		DB.executeUpdate(SqlDel);

		Payment = P_Payment.get( Env.getCtx(), Payment_ID, trxName);
		NoPaiment++;
		BigDecimal MntNet = Payment.getNetPay();
  		int DistResidual = 0;
  			                       
  		String SqlDist = "Select P_Employee_Payment_Dist_ID " +
			                 " From P_Employee_Payment_Dist " +
			                 "where IsActive = 'Y' and P_Employee_ID=" + Payment.getP_Employee_ID() +
							 " order by Priority";
  			//log.debug(SqlDist);
  		PreparedStatement pstmp_dist = null;
		pstmp_dist = DB.prepareStatement(SqlDist);
		ResultSet rs_Dist = pstmp_dist.executeQuery();
  		while (rs_Dist.next())
  		{
  			log.log (Level.INFO,"Nouvel enregistrement" + Count);
			BigDecimal MntDist = new BigDecimal (0);
				Emp_Distribution = P_Employee_Payment_Dist.get( Env.getCtx(), rs_Dist.getInt("P_Employee_Payment_Dist_ID"), trxName);
  				if (Emp_Distribution.getAmount().compareTo(new BigDecimal(0)) != 0)
  				{
  					if ( MntNet.compareTo(Emp_Distribution.getAmount()) > 0 )
  						MntDist = Emp_Distribution.getAmount();
  					else
  						MntDist = MntNet;
  				}
  				else
  				{
  					BigDecimal PrcRate = Emp_Distribution.getRate().divide(new BigDecimal(100), 2, 2);
  					MntDist = Payment.getNetPay().multiply(PrcRate); 
  					if ( MntNet.compareTo(MntDist) < 0 )
  					   MntDist = MntNet;
  				}
  				MntNet = MntNet.subtract(MntDist);
  				Payment_dist = new P_Payment_Distribution(Env.getCtx(), -1, trxName);
  				Payment_dist.setP_Payment_ID(Payment.getP_Payment_ID());
  				Payment_dist.setP_Financial_Institution_ID(Emp_Distribution.getP_Financial_Institution_ID());
  				Payment_dist.setTransit(Emp_Distribution.getTransit());
  				Payment_dist.setFolio(Emp_Distribution.getFolio());
  				Payment_dist.setAmount(MntDist.setScale(2, BigDecimal.ROUND_HALF_UP));
  				Payment_dist.save();
  				
  				if ( Emp_Distribution.isResidual() )
  					DistResidual = Payment_dist.getP_Payment_Distribution_ID();
  			}
  			if ( Payment_dist != null )
  			{
  	  			if ( DistResidual == 0)
  					DistResidual = Payment_dist.getP_Payment_Distribution_ID();
  			}
  			
  			rs_Dist.close();
  			pstmp_dist.close();
  			
  			if (MntNet.compareTo(new BigDecimal(0)) > 0 && DistResidual != 0)
  			{
  				//log.debug( "Mise à jour distribution: " + DistResidual );
  				Payment_dist = P_Payment_Distribution.get( Env.getCtx(), DistResidual, trxName ); 
  				Payment_dist.setAmount( Payment_dist.getAmount().add( MntNet ) );
  				Payment_dist.save();
  			}
	  	}
*/
	
	
	private BigDecimal getCumulEaringEI( P_Period Period, String trxName )
	{

		String Refund_EI_Earning = PgiUtil.getSolsticeParameter(Env.getCtx(), "Refund_EI_Earning");

		//2023-08-29
		if ( Payment.getTaxation_Region_ID() != 166 )  // Québec
			Refund_EI_Earning = PgiUtil.getSolsticeParameter(Env.getCtx(), "Refund_EI_Earning_Out_QC");
		
	    P_Gain Gain = P_Gain.getWithValue( Env.getCtx(), Refund_EI_Earning, null);

	    //2023-08-30 modifier QuantityCalc AmountCalc 
		String sqlCum = " SELECT sum( p_payment_gain.AmountCalc ) "
				      + " FROM P_Payment_Gain "
				      + " inner join P_Payment on P_Payment.P_Payment_ID = P_Payment_Gain.P_Payment_ID "
				      + " and P_Payment.P_Payment_ID <> " + Payment.getP_Payment_ID()
				      + " and P_Payment.P_year_ID = " + Payment.getP_Year_ID() 
				      + " and P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID() 
				      + " And P_Payment_Gain.P_Gain_id = " + Gain.getP_Gain_ID() 
				      + " and P_Payment_Gain.P_Period_Id = " + Period.getP_Period_ID(); 
		
		PreparedStatement pstmt = null;

		BigDecimal amtCum = null;
		try
		{
			pstmt = DB.prepareStatement (sqlCum, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				amtCum = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE," CalculNet - getCumulEaringEI - " + sqlCum, e);
		}
		
		if ( amtCum != null && amtCum.compareTo(Env.ZERO) == 0 )  return null;
		
		return amtCum;
	}
	
	
	private BigDecimal getSalaryAdmissibleEI(  BigDecimal MaxSalary ) throws Exception
	{
		
		BigDecimal amtCum = Env.ZERO;
		
		String sqlCum = " select sum( p_payment_deduction.SALARY_ELIGIBLE ) from P_Payment_Deduction"
				+ " inner join P_Payment on p_payment.P_PAYMENT_ID = P_Payment_Deduction.P_PAYMENT_ID "
				+ " and P_Payment.P_year_ID = " + Payment.getP_Year_ID() 
				+ " and P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID() 
				+ " And P_Payment_Deduction.P_deduction_id IN ( SELECT P_Deduction_ID From P_Deduction where  P_DEDUCTION_FAMILY_ID = 1000000 ) " 
				+ " and P_Payment_Deduction.P_Period_Id <= " + Period.getP_Period_ID(); ;  
		
		PreparedStatement pstmt = null;

		
		try
		{
			pstmt = DB.prepareStatement (sqlCum, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				amtCum = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE," TimeValidation - calcul_ei - " + sqlCum, e);
		}
		
		return amtCum;

	}


	
	private BigDecimal calcul_ei( P_Period Period, String trxName )
	{
	//	P_Period StartPeriod = P_Period.getFirstPeriodYear(Env.getCtx(), Payment.getP_Year_ID(), trxName);
		
		
		Deduction = P_Deduction.getWithMethod(Env.getCtx(), 103, trxName);
		EmployeeDeduction = P_Employee_Deduction.get(Env.getCtx(), Payment.getP_Employee_ID(), Deduction.getP_Deduction_ID(), Period.getEndDate(), trxName);

		P_Period payPeriod = P_Period.get(Env.getCtx(), Payment.getP_Period_ID(), null);
		Timestamp CurrentDate = payPeriod.getPayDate();  // Period.getEndDate();

		/*		
		taxTable.clear();

		P_Period payPeriod = P_Period.get(Env.getCtx(), Payment.getP_Period_ID(), null);
		Timestamp CurrentDate = payPeriod.getPayDate();  // Period.getEndDate();
//		Timestamp CurrentDate = Period.getPayDate();  // Period.getEndDate();

		try
		{

			int taxID = Calcul_federal_tax.get_federal_tax_id( CurrentDate );
			taxTable = Calcul_federal_tax.get_federal_tax_para(  taxID, CurrentDate);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE," TimeValidation - calcul_ei get rate " , e);
		}
*/

		BigDecimal ei_rate_employee = ZERO;
		BigDecimal ei_rate_employer = ZERO;
		BigDecimal ei_max_gain      = ZERO;
		BigDecimal ei_max_annual    = ZERO;

		ei_max_gain      = (BigDecimal)taxTable.get( "AE.1");
		
		P_Region Region = P_Region.get( Env.getCtx (), Payment.getTaxation_Region_ID(), null);

		if ( Region.getC_Region_ID() == QUEBEC )  
		{
//			ei_max_gain      = (BigDecimal)taxTable.get( "AE.1");
			ei_rate_employee = (BigDecimal)taxTable.get( "AE.2");
			ei_max_annual    = (BigDecimal)taxTable.get( "AE.3");
		}
		else
		{
			ei_rate_employee = (BigDecimal)taxTable.get( "AE.5");
			ei_max_annual    = (BigDecimal)taxTable.get( "AE.6");
		}

		int nbrProvince = 1;
		try
		{
			nbrProvince = getNbrProvince ();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE," CalculNet - calcul_ei_remboursement - " +  e);
		}

		// Si l'employé a changer de province, il doit quand meme payé de l'assuranc emploi pour le total du maximum assurable
		if ((  Region.getC_Region_ID() == QUEBEC && nbrProvince > 1))
		{
			ei_max_annual    = (BigDecimal)taxTable.get( "AE.6");
		}
		
		
		ei_rate_employee = ei_rate_employee.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP );

		P_Employer Employer = P_Employer.get( Env.getCtx(), Payment.getP_Employer_ID(), null ); 
		if ( Employer.isReducedEI() )
		{
			ei_rate_employer = (BigDecimal)taxTable.get( "AE.42");
			// TODO Hardcoder ( Acasta )
			if ( Employee.getAD_Org_ID() == 1000011 || Employee.getAD_Org_ID() == 1000012 )
				ei_rate_employer = (BigDecimal)taxTable.get( "AE.43");
		}	
		else
			ei_rate_employer = (BigDecimal)taxTable.get( "AE.41");

		
		P_Deduction_Param Deduction_Param   = P_Deduction_Param.get(Env.getCtx(), Deduction.getP_Deduction_ID(), CurrentDate, null);

		BigDecimal amt = Env.ZERO;
		try
		{
	//2022-09-22		
			getSalaryAdmissible( Deduction_Param , ei_max_gain );
	        get_deduction_cumulative_family( Deduction.getP_Deduction_Family_ID() );

			if (( this.cumSalaryAdmissible.add( this.salaryAdmissible )).compareTo( ei_max_gain ) > 0)
		    {
				amt = ei_max_gain.subtract(this.cumSalaryAdmissible );
				if ( amt.compareTo(Env.ZERO) < 0 )
					amt = Env.ZERO;

		    }
	        else
	        {
	        	amt = salaryAdmissible;
	        	
	    		BigDecimal t_cumAmt = getCumulEaringEI( Period, null);
	    		if ( t_cumAmt != null)
	    			amt = amt.subtract( t_cumAmt ) ;
	        }
	        	
	        
	        
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE," CalculNet - calcul_ei_remboursement - " +  e);
		}

		amt = amt.multiply(ei_rate_employee);
		BigDecimal emr1 = amt.multiply(  (BigDecimal)taxTable.get( "AE.41") ) ;  // 1.4
		BigDecimal emr2 = amt.multiply(  ei_rate_employer ) ; 

		BigDecimal cumAmt = getCumulEaringEI( Period, null);
		BigDecimal returnAmt = emr1.subtract( emr2 ); 
		
		if ( cumAmt != null )
		{
			returnAmt = returnAmt.subtract( cumAmt   );
		}
		
		return returnAmt;
				
	}


	
	public void gen_time_sheet_detail_IE( P_Payment Payment, P_Period l_Period, String trxName ) throws Exception
	{
		P_Assignment Assignment;
		P_Assignment_Param AssignmentParam;
		P_Gain_Parameter GainParameter;
		P_Collective_Labour_Agr CollectiveLabourArg;
		P_Payment_Gain PaymentGain;
		String Refund_EI_Earning = PgiUtil.getSolsticeParameter(Env.getCtx(), "Refund_EI_Earning");
		
		if ( Payment.getTaxation_Region_ID() != 166 )  // Québec
			Refund_EI_Earning = PgiUtil.getSolsticeParameter(Env.getCtx(), "Refund_EI_Earning_Out_QC");
		
	    P_Gain Gain = P_Gain.getWithValue( Env.getCtx(), Refund_EI_Earning, null);
	    P_Payment_Group PaymentGroup = P_Payment_Group.get(Env.getCtx(),Payment.getP_Payment_Group_ID(), trxName);

	    	   
	
		P_Employer Employer = P_Employer.get( Env.getCtx(), Payment.getP_Employer_ID(), null);
		if ( Employer.isReducedEI() )
		{
				BigDecimal Quantity = calcul_ei( l_Period, null );

				Quantity = Quantity.setScale(2,BigDecimal.ROUND_HALF_UP);

				int line = Payment.GetNextLine();
				
				if ( Employee.getLastEmployeeAssignment( Payment.getP_Payment_ID() ) != 0)
					AssignmentParam = P_Assignment_Param.get( Env.getCtx(), Employee.getLastEmployeeAssignment( Payment.getP_Payment_ID() ), l_Period.getEndDate(), null );
				else 
					AssignmentParam = Employee.getLastEmployeeAssignment(Employee.getP_Employee_ID(), l_Period.getEndDate(), null);

				Assignment = P_Assignment.get( Env.getCtx(),AssignmentParam.getP_Assignment_ID(), trxName );
				
				
				if ( AssignmentParam != null )
				{
					if ( Employee.getAD_Client_ID() == 11)
					{
						GainParameter = P_Gain_Parameter.get( Env.getCtx (), Gain.getP_Gain_ID(), AssignmentParam.getP_Collective_Labour_Agr_ID(), trxName ); 
					}
					else
					{
						GainParameter = P_Gain_Parameter.get( Env.getCtx (), Gain.getP_Gain_ID(), Employee.getP_Collective_Labour_Agr_ID(), trxName ); 
					}

					if ( GainParameter == null)
					{
						CollectiveLabourArg = P_Collective_Labour_Agr.get( Env.getCtx(), AssignmentParam.getP_Collective_Labour_Agr_ID(), trxName );
						P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le gain  "+Gain.getValue()+" n'a pas de paramètre actif - Convention collective : " + CollectiveLabourArg.getValue() + " - " + CollectiveLabourArg.getName() , trxName);
					}
					else
					{
						PaymentGain = new P_Payment_Gain( Env.getCtx (), -1, trxName);
						PaymentGain.setP_Payment_ID( Payment.getP_Payment_ID() );
						PaymentGain.setP_Employee_ID( Payment.getP_Employee_ID());
						
						PaymentGain.setP_Assignment_ID( Assignment.getP_Assignment_ID() );
						PaymentGain.setP_Post_ID( Assignment.getP_Post_ID());
						PaymentGain.setP_Gain_ID( Gain.getP_Gain_ID() );
						PaymentGain.setP_Gain_Parameter_ID( GainParameter.getP_Gain_Parameter_ID() );
						PaymentGain.setMultiplyRate( GainParameter.getMultiplyRate());
						PaymentGain.setType_Rate( Gain.getType_Rate() );
						
						PaymentGain.setQuantity( Quantity );
						PaymentGain.setFixed_Amount( GainParameter.getFixed_Amount() );
						PaymentGain.setBonus_Amount( GainParameter.getBonus_Amount() );
						PaymentGain.setLine( line );
						PaymentGain.setP_Year_ID( Payment.getP_Year_ID()  );
						PaymentGain.setP_Period_ID( l_Period.getP_Period_ID() );
						PaymentGain.setP_Advance_ID( Gain.getP_Advance_ID() );
						PaymentGain.setDay( l_Period.getEndDate() );
						PaymentGain.setStartDate( l_Period.getStartDate() );
						PaymentGain.setEndDate( l_Period.getEndDate() );
//						PaymentGain.setC_Activity_ID( rs.getInt("C_Activity_ID") );
						PaymentGain.setP_Assignment_Param_ID( AssignmentParam.getP_Assignment_Param_ID());
						if ( ! AssignmentParam.isOut_Of_Range() )
						{
							PaymentGain.setP_Job_Title_ID(AssignmentParam.getP_Job_Title_ID());
							PaymentGain.setP_Collective_Labour_Agr_ID( AssignmentParam.getP_Collective_Labour_Agr_ID());
							PaymentGain.setP_Salary_Scale_Detail_ID( AssignmentParam.getP_Salary_Scale_Detail_ID());
							PaymentGain.setP_Salary_Scale_ID( AssignmentParam.getP_Salary_Scale_ID());
						}
						else
						{
							PaymentGain.setP_Job_Title_ID(Employee.getP_Job_Title_ID());
							PaymentGain.setP_Collective_Labour_Agr_ID( Employee.getP_Collective_Labour_Agr_ID());
						}
						PaymentGain.setP_UOM_ID( Gain.getP_UOM_ID());
						PaymentGain.setWeekIndex( 1 );
						PaymentGain.setSalaryPercentage( null );
						PaymentGain.setOriginTime( "STD" );
//						PaymentGain.setP_Employee_LongTermLeave_ID( 0 );



								
						//
						// 5/12 0.4166
						PaymentGain.setHourly_Rate( Gain.getHourly_Rate() );
//2023-05-30 Ne pas mettre de quantité calculer.						
//						PaymentGain.setQuantityCalc(Quantity );
						PaymentGain.setQuantityCalc( Env.ZERO );
						
			//	2022-01-21 pour krispy		PaymentGain.setAmountCalc(PaymentGain.getQuantityCalc().multiply( PaymentGain.getHourly_Rate()).setScale(2, BigDecimal.ROUND_HALF_UP)  );
						PaymentGain.setAmountCalc( Quantity );

						// Ne sauvegarde pas les montant a 0.
						if ( PaymentGain.getAmountCalc().compareTo( Env.ZERO) != 0 )
							PaymentGain.save();
					}
				}
		
		}
	}

	
}

