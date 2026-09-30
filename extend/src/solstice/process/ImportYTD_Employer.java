package solstice.process;


import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import solstice.model.*;


public class ImportYTD_Employer extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	private int				m_P_Period_ID = 0;
	private int				m_P_Employee_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/**	Big Decimal 0	 */
	static final public java.math.BigDecimal ZERO = new java.math.BigDecimal(0.0);

	/** Effective						*/
	private Timestamp		m_DateValue = null;

	private X_I_YTD_Deduction imp;

	private String clientCheck ;
	private String trxName = null;
	private P_Payment Payment = null;
	private P_Time_Sheet Timesheet = null;
	private P_Period Period = null;
	private P_Employee Employee = null;
	
	
	int taxID = 0;

//	private Timestamp PeriodStartDate;
//	private Timestamp PeriodEndDate;
//	private Timestamp EmployeeBirthDate;
	
	private int	m_updated = 0;

	private Hashtable<String, QuantityAmountPair> 	s_Matrix = new Hashtable<String, QuantityAmountPair>();	//	Matrix
	private Hashtable<String, BigDecimal> 	s_Employee = new Hashtable<String, BigDecimal>();	//	Total Employee of each Deduction 
	private Hashtable<String, BigDecimal> 	s_Employer = new Hashtable<String, BigDecimal>();	//	Total Employer of each Deduction
	private Hashtable<String, BigDecimal>   s_SalaryAdmissible = new Hashtable<String, BigDecimal>();
	private Hashtable<String, BigDecimal> 	taxTable = new Hashtable<String, BigDecimal>();

//	public Hashtable 	s_TP1015 = new Hashtable();	    //  TP1015	
//	public Hashtable 	s_TD1 = new Hashtable();	    //  TD1	
	  
	private P_Frequency Frequency; 
	
	private P_Period TimeSheetPeriod;
	
	boolean calculDeduction = false;
	
	private P_Region Region; 


	//
	// Conserve les montants Assurance Chomage et de RRQ pour le calcul des impots.
	//
	private BigDecimal Amount_Employment_Insurance = ZERO;
	private BigDecimal Amount_Rpc = ZERO;
	private BigDecimal Amount_RQAP = ZERO;
	
	private ImportYTDCalcul_federal_tax calcul_federal_tax;
	//
	// Affectation Principal du paiement pour la période.
	// utiliser pour le calcul des fonds de pensions. ( CARRA )
	//
	P_Assignment Assignment = null;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("AD_Client_ID"))
				m_AD_Client_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Period_ID"))
				m_P_Period_ID = para[i].getParameterAsInt();
			else if (name.equals("P_Employee_ID"))
				m_P_Employee_ID = para[i].getParameterAsInt();
			else if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (m_DateValue == null)
			m_DateValue = new Timestamp (System.currentTimeMillis());
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		String sql = null;
		int no = 0;
		
		clientCheck = " AND AD_Client_ID=" + m_AD_Client_ID;

		if (m_deleteOldImported)
		{
			sql = "delete from p_payment_deduction where Employee_Part = 0 and Employer_Part <> 0 and P_Deduction_ID != 1002630 and P_Period_ID = " + m_P_Period_ID ;
			DB.executeUpdate(sql.toString(), null);
		}
		

		sql = "update P_Payment set Taxation_Region_ID  =  P_Employee.Taxation_Region_ID from P_Employee where P_Employee.P_employee_id = P_Payment.P_Employee_ID";
		DB.executeUpdate(sql.toString(), null);

		sql = "update P_Payment set Taxation_Region_ID =  p_deduction.C_Region_ID " + 
		      " from p_payment_deduction, p_deduction where p_deduction.c_region_id <> 0 " +
		      " and p_payment_deduction.p_deduction_id = p_deduction.p_deduction_id " +
		      " and P_Payment.P_Payment_ID = P_Payment_Deduction.P_Payment_ID " ;
		DB.executeUpdate(sql.toString(), null);

		// Transforme RPC en RRQ
		sql = "update p_payment_deduction set p_deduction_id = 1002712 " 
		    + " from p_payment "
		    + " where p_payment.p_payment_id = p_payment_deduction.p_payment_id "
		    + " and p_deduction_id = 1002570 "
		    + " and p_payment.taxation_region_id = 166 "
		    ;
		DB.executeUpdate(sql.toString(), null);

//		createAvi();
		
		
		Period = P_Period.get( getCtx(), m_P_Period_ID, trxName);
		Frequency = P_Frequency.get( Env.getCtx(), Period.getP_Frequency_ID() , trxName );

		load_tax_parameter( Period.getPayDate() );
		
		sql = "SELECT P_Payment_ID FROM P_Payment WHERE P_Period_ID = " + m_P_Period_ID;
		
		if ( m_P_Employee_ID != 0 )
			sql = sql + " AND  P_Employee_ID= " + m_P_Employee_ID ;
		
//		sql = sql + " AND  P_Employee_ID in (  1002094 ) ";
		
		
		log.info( "Sql " + sql);
		try
		{

			PreparedStatement pstmt = DB.prepareStatement(sql, trxName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{ 
				s_Matrix.clear();
				s_Employee.clear();
				s_Employer.clear();
				s_SalaryAdmissible.clear();
				isExonerated = false;
				this.Amount_Employment_Insurance = ZERO;
				this.Amount_Rpc = ZERO;
				this.Amount_RQAP = ZERO;
				
				calcul_federal_tax = new ImportYTDCalcul_federal_tax( Env.getCtx(), this, trxName );

				Payment = P_Payment.get( getCtx(), rs.getInt("P_Payment_ID"), trxName);
				TimeSheetPeriod = P_Period.get( Env.getCtx(), Payment.getP_Period_ID() , trxName);

				Timesheet = P_Time_Sheet.GetWithPaymentID(getCtx(), Payment.getP_Payment_ID(), trxName);
				Employee = P_Employee.get( getCtx(), Payment.getP_Employee_ID(), trxName);
				Region = P_Region.get( Env.getCtx (), Payment.getTaxation_Region_ID(), trxName);
				
				String sqldel = "delete from p_time_sheet_error where p_time_sheet_id = " + Timesheet.getP_Time_Sheet_ID();
				DB.executeUpdate( sqldel, null);
				// Ajout stephane pour p_valide_calculs
				sqldel = "delete from p_valide_calculs where p_period_id = " + Timesheet.getP_Period_ID() + " and p_employee_id = " + Employee.getP_Employee_ID();
				DB.executeUpdate( sqldel, null);
				
				Payment.setAnnualSalary( Employee.getAnnualSalary( Period.getEndDate() ));
				Payment.save( );
				NetPay   = Payment.getGrossEarnings();

				read_gain_info();
				//
				// Update the matrix with taxable benefit information
				//
				read_taxable_benefit_info( );

//				info_matrix (  );
				
				calcul_deduction();
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			System.err.println( "error : " + e);
		}

 
		sql = " update p_payment_deduction "  
		    + " set p_payment_deduction.employer_part = Dedn66 " // - isnull( dbo.dedn66(p_payment_deduction.p_employee_id, p_payment_deduction.p_period_id ), 0 ) " 
		    + " from i_ytd_deduction " 
		    + " where p_payment_Deduction.P_deduction_ID in ( select P_deduction_ID from p_deduction where value = '64' ) " 
		    + " and i_ytd_deduction.p_employee_id = p_payment_deduction.P_employee_id "
		    + " and i_ytd_deduction.p_period_id = p_payment_deduction.p_period_id "
		    + " and p_payment_deduction.p_period_id = " + m_P_Period_ID
		    ;
		no = DB.executeUpdate(sql, null);

		sql = " delete from p_payment_deduction "
		    + " where p_payment_Deduction.P_deduction_ID in ( select P_deduction_ID from p_deduction where value = '66' ) "
		    + " and p_payment_deduction.p_period_id = " + m_P_Period_ID
		    ;
		no = DB.executeUpdate(sql, null);


		
		return " ";

	}

	private void createAvi()
	{
		String sql = "SELECT P_Payment_ID, Employer_Part FROM P_Payment_Deduction WHERE Employer_Part <> 0 and P_Deduction_ID in ( select P_deduction_ID from p_deduction where value = '63' ) " ;
		try
		{
			int P_Taxable_Benefit_ID = 1000014;
			
			PreparedStatement pstmt = DB.prepareStatement(sql, trxName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				P_Payment Payment = P_Payment.get( Env.getCtx(), rs.getInt( "P_Payment_ID" ) , trxName);

				P_Payment_Taxable_Benefit pdetail = new P_Payment_Taxable_Benefit( getCtx(), -1, trxName);
				P_Employee_Taxable_Benefit emptb = P_Employee_Taxable_Benefit.get(getCtx(), Payment.getP_Employee_ID(), P_Taxable_Benefit_ID, trxName);
				if ( emptb.getP_Employee_Taxable_Benefit_ID() <= 0 )
				{
					emptb.setAD_Org_ID(0);
					emptb.setEffectIn(TimeUtil.getDay(2007, 01, 01));
					emptb.setIsActive(true);
					emptb.setP_Employee_ID(Payment.getP_Employee_ID());
					emptb.setP_Taxable_Benefit_ID(P_Taxable_Benefit_ID);
					emptb.save();
				}
				pdetail.setOrigine("FTP");
				pdetail.setIsActive(true);
				pdetail.setAD_Org_ID(Payment.getAD_Org_ID());
				pdetail.setP_Employee_ID(Payment.getP_Employee_ID());
				pdetail.setP_Employee_Taxable_Benefit_ID(emptb.getP_Employee_Taxable_Benefit_ID());
				pdetail.setP_Payment_ID(Payment.getP_Payment_ID());
				pdetail.setP_Period_ID(Payment.getP_Period_ID());
				pdetail.setP_Taxable_Benefit_ID(P_Taxable_Benefit_ID);
				pdetail.setP_Year_ID(Payment.getP_Year_ID());
				pdetail.setTaxable_Benefit_Amount( rs.getBigDecimal( "Employer_Part"));
				pdetail.save();

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

	
	private P_Deduction_Param Deduction_Param = null;
	private P_Employee_Deduction EmployeeDeduction = null;

	private	BigDecimal employerAmount = ZERO;
	private	BigDecimal employeeAmount = ZERO;
	private	BigDecimal salaryAdmissible = ZERO;
	private	BigDecimal hoursAdmissible  = ZERO;
	private boolean isExonerated = false;
	
	private	BigDecimal cumEmployerAmount = ZERO;
	private	BigDecimal cumEmployeeAmount = ZERO;
	private	BigDecimal cumSalaryAdmissible = ZERO;
	private	BigDecimal cumHoursAdmissible  = ZERO;

	private P_Method_Deduction   Method_Deduction;

	private BigDecimal NetPay = ZERO;

	private void load_tax_parameter( Timestamp CurrentDate ) throws Exception
	{
//		log.debug ("Load_tax_parameter Begin");
		taxTable.clear();

		taxID = ImportYTDCalcul_federal_tax.get_federal_tax_id( CurrentDate );
		taxTable = get_federal_tax_para( taxID, CurrentDate);

	}

	public static Hashtable<String, BigDecimal> get_federal_tax_para( int taxID, Timestamp EffectIn) throws Exception
	{
//		int taxID = 0;

		Hashtable<String, BigDecimal> taxTable = new Hashtable<String, BigDecimal>();
		taxTable.clear();

		PreparedStatement pstmt = null;
		String query = "Select Value, Amount FROM P_Tax_Federal WHERE p_tax_id = ? ";
		
		pstmt = null;
			pstmt = DB.prepareStatement (query, null);
			pstmt.setInt(1, taxID );
			ResultSet rs = pstmt.executeQuery ();
			
			while (rs.next ())
			{
				taxTable.put( rs.getString(1), rs.getBigDecimal( 2 ) );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		
		
		query = "Select Value, Amount FROM P_Tax_Federal_RPC WHERE p_tax_id = ? ";
		
		pstmt = null;
			pstmt = DB.prepareStatement (query, null);
			pstmt.setInt(1, taxID );
			rs = pstmt.executeQuery ();
			
			while (rs.next ())
			{
				taxTable.put( rs.getString(1), rs.getBigDecimal( 2 ) );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

		query = "Select Value, Amount FROM P_Tax_RRQ WHERE p_tax_id = ? ";
		
		pstmt = null;
			pstmt = DB.prepareStatement (query, null);
			pstmt.setInt(1, taxID );
			rs = pstmt.executeQuery ();
			
			while (rs.next ())
			{
				taxTable.put( rs.getString(1), rs.getBigDecimal( 2 ) );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

			query = "Select Value, Amount FROM P_Tax_RQAP WHERE p_tax_id = ? ";
			
			pstmt = null;
				pstmt = DB.prepareStatement (query, null);
				pstmt.setInt(1, taxID );
				rs = pstmt.executeQuery ();
				
				while (rs.next ())
				{
					taxTable.put( rs.getString(1), rs.getBigDecimal( 2 ) );
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;

		query = "Select Value, Amount FROM P_Tax_Federal_TD1 WHERE p_tax_id = ? ";
		
		pstmt = null;
			pstmt = DB.prepareStatement (query, null);
			pstmt.setInt(1, taxID );
			rs = pstmt.executeQuery ();
		
			while (rs.next ())
			{
				taxTable.put( rs.getString(1), rs.getBigDecimal( 2 ) );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

		query = "Select Value, Amount FROM P_Tax_Federal_Ei WHERE p_tax_id = ? ";
		
		pstmt = null;
			pstmt = DB.prepareStatement (query, null);
			pstmt.setInt(1, taxID );
			rs = pstmt.executeQuery ();
			
			while (rs.next ())
			{
				taxTable.put( rs.getString(1), rs.getBigDecimal( 2 ) );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

		query = "Select Federal_Tax_Rate FROM P_Tax_Federal_Rate "
			+ "WHERE P_Tax_Federal_Rate.P_Tax_ID = " + taxID
			+ " AND 0 = Limit_Min ";
		
		pstmt = null;
			pstmt = DB.prepareStatement (query, null);

			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				taxTable.put( "K1", rs.getBigDecimal(1) );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
		query = "Select C_Region_ID, Value, Amount FROM P_Tax_Provincial_TD1, P_Tax_Provincial WHERE P_Tax_Provincial.P_Tax_Provincial_ID = P_Tax_Provincial_TD1.P_Tax_Provincial_ID AND p_tax_id = ? ";
		
		pstmt = null;
			pstmt = DB.prepareStatement (query, null);
			pstmt.setInt(1, taxID );
			rs = pstmt.executeQuery ();
			
			while (rs.next ())
			{
				taxTable.put( rs.getInt(1) + "-" + rs.getString(2), rs.getBigDecimal( 3 ) );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		
		return taxTable;
	}

	private P_Payment_Deduction PaymentDeduction;

	
	private void calcul_deduction() throws Exception
	{
		BigDecimal MethodAmount = Env.ZERO;
//		BigDecimal totalEmployeeAmount = ZERO;
		
//		Payment.setTotalDeduction( ZERO);
		
		//
		// Loop on Employee deduction  
		//
		String query = null;
		query = "SELECT P_Method_Deduction.Priority, P_Deduction.P_Deduction_ID, IsAccumulation, P_Deduction_Param_ID, P_Employee_Deduction_ID  "
			+ "FROM P_Employee_Deduction, P_Deduction, P_Deduction_Param, P_Method_Deduction "
			+ "WHERE P_Employee_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID "
			+ "  AND P_Method_Deduction.P_Method_Deduction_ID = P_Deduction_Param.P_Method_Deduction_ID "
			+ "  AND P_Deduction.P_Deduction_ID = P_Deduction_Param.P_Deduction_ID "
			+ "  AND P_Deduction.IsActive='Y' " 
			+ "  AND P_Employee_Deduction.P_Employee_ID=" + this.Payment.getP_Employee_ID ()
			+ "  AND P_Employee_Deduction.EffectIn in ( Select Max( EffectIn) FROM P_Employee_Deduction dat WHERE dat.P_Deduction_ID = P_Deduction.P_Deduction_ID and dat.P_Employee_ID = P_Employee_Deduction.P_Employee_ID and dat.EffectIn <= " + DB.TO_DATE(Period.getEndDate() ) + ")"
			+ "  AND ( P_Employee_Deduction.EffectTo is null OR P_Employee_Deduction.EffectTo >= " + DB.TO_DATE( Period.getEndDate() ) +"  )"
			+ "  AND P_Deduction_Param.EffectIn in ( Select Max( EffectIn) FROM P_Deduction_PARAM WHERE P_Deduction_PARAM.P_Deduction_ID = P_Deduction.P_Deduction_ID and P_Deduction_PARAM.EffectIn <= " + DB.TO_DATE(Period.getEndDate() ) + ")"
		    + "  AND IsDeductionMandatory = 'N' "

		    + "	 UNION ALL " 

		    + " SELECT P_Method_Deduction.Priority, P_Deduction.P_Deduction_ID,  IsAccumulation, P_Deduction_Param_ID, 0 AS P_Employee_Deduction_ID " 
		    + " FROM P_Deduction, P_Deduction_Param, P_Method_Deduction  "
		    + " WHERE P_Method_Deduction.P_Method_Deduction_ID = P_Deduction_Param.P_Method_Deduction_ID " 
		    + "  AND P_Deduction.P_Deduction_ID = P_Deduction_Param.P_Deduction_ID "
		    + "  AND P_Deduction.IsActive='Y'  "
		    + "  AND IsDeductionMandatory = 'Y' "
			+ "  AND P_Deduction_Param.EffectIn in ( Select Max( EffectIn) FROM P_Deduction_PARAM WHERE P_Deduction_Param.P_Deduction_ID = P_Deduction.P_Deduction_ID and P_Deduction_Param.EffectIn <= " + DB.TO_DATE(Period.getEndDate() ) + ")"
		    + "  AND ( P_Deduction.C_Region_ID IS NULL OR C_Region_ID = " + Payment.getTaxation_Region_ID() + " ) "
;
/*
		    + "	 UNION ALL " 

		    + " SELECT P_Method_Deduction.Priority, P_Deduction.P_Deduction_ID,  IsAccumulation, P_Deduction_Param.P_Deduction_Param_ID, 0 AS P_Employee_Deduction_ID " 
		    + " FROM P_Deduction, P_Deduction_Param, P_Method_Deduction, P_Payment_Deduction  "
		    + " WHERE P_Method_Deduction.P_Method_Deduction_ID = P_Deduction_Param.P_Method_Deduction_ID " 
		    + "  AND P_Deduction.P_Deduction_ID = P_Deduction_Param.P_Deduction_ID "
		    + "  AND P_Deduction.IsActive='Y'  "
		    + "  AND IsDeductionMandatory = 'Y' "
            + "  AND P_Payment_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID "
            + "  AND P_Payment_Deduction.P_Payment_ID = " + Payment.getP_Payment_ID()
            + "  AND P_Payment_Deduction.Employee_Part != 0 "
			+ "  AND P_Deduction_Param.EffectIn in ( Select Max( EffectIn) FROM P_Deduction_PARAM WHERE P_Deduction_Param.P_Deduction_ID = P_Deduction.P_Deduction_ID and P_Deduction_Param.EffectIn <= " + DB.TO_DATE(Period.getEndDate() ) + ")"
		    + "  AND (  P_Deduction.C_Region_ID IS NOT NULL AND C_Region_ID != " + Payment.getTaxation_Region_ID() + " ) "
		    + "  ORDER BY P_Method_Deduction.Priority, P_Deduction.P_Deduction_ID " ;
*/
		
		
//		log.log (Level.INFO,"CalculNet - Query : " + query );

		//Log.trace( 5,"calcul_Deduction - query=" + query);
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			boolean calcul = true;
			while (rs.next ())
			{
				calcul = true;

				Deduction_Param = P_Deduction_Param.get( Env.getCtx(), rs.getInt("P_Deduction_Param_ID"),trxName);
                P_Deduction Deduction = P_Deduction.get( Env.getCtx(), Deduction_Param.getP_Deduction_ID(),trxName);

                // C.S.S.T.   Whapchiwem
				if ( Deduction.getP_Deduction_ID() == 1002698 && Employee.getAD_Org_ID() != 1000007 )
					calcul = false;

                // C.S.S.T.  non  Whapchiwem
				if ( Deduction.getP_Deduction_ID() == 1002571 && Employee.getAD_Org_ID() == 1000007 )
					calcul = false;

                
				if ( rs.getInt("P_Employee_Deduction_ID") != 0 )
					EmployeeDeduction = P_Employee_Deduction.get( Env.getCtx (), rs.getInt("P_Employee_Deduction_ID"), trxName);
				else
				{
					EmployeeDeduction = P_Employee_Deduction.get(Env.getCtx(), Payment.getP_Employee_ID(), Deduction.getP_Deduction_ID(), Period.getStartDate(), trxName);
					if ( EmployeeDeduction.getP_Employee_Deduction_ID() <= 0 )
					{
						EmployeeDeduction = new P_Employee_Deduction( Env.getCtx(), -1, trxName);
						EmployeeDeduction.setP_Deduction_ID( Deduction.getP_Deduction_ID());
						EmployeeDeduction.setIsActive(true);
						EmployeeDeduction.setP_Employee_ID(Payment.getP_Employee_ID());
						EmployeeDeduction.setEffectIn(Period.getStartDate());
						EmployeeDeduction.save();
						
					}
				}
                //
                //	On ne réévalue pas les deductions d'assurance en période d'orgine.
				if ( Deduction.getP_Deduction_Family_ID() == 1000001 &&  this.Period.getP_Period_ID () < Payment.getP_Period_ID() )
				{
					calcul = false;
				}

//				log.log (Level.INFO,"CalculNet - Deduction : " + Deduction.getValue() );

				if ( EmployeeDeduction.isActive() && Deduction_Param.isActive() && calcul )
				{
					
	                //
	                // avertissement pour éviter de calculer 2 déductions d'impot différente.
	                // c'est un avertissement car pour un correction en période d'origine cela peut etre normal.
	                //
/*	                if ( Deduction_Param.getP_Employer_ID() != 0 && Period.getP_Period_ID() == TimeSheetPeriod.getP_Period_ID() &&  
	                     Deduction_Param.getP_Employer_ID() != Employee.getP_Employer_ID() )
						P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "L'employé "+Employee.getValue()+" n'a pas le même numéro d'employeur que la déduction : " + Deduction.getValue() , trxName);
*/
	                
	                if ( Deduction_Param != null && Deduction_Param.isActive() )
					{
						Method_Deduction = P_Method_Deduction.get( Env.getCtx (), Deduction_Param.getP_Method_Deduction_ID() ,trxName);
						String    methodCalcul     = Method_Deduction.getValue();

						this.salaryAdmissible = ZERO;
						this.hoursAdmissible = ZERO;
						this.employeeAmount = ZERO;
						this.employerAmount = ZERO;

						int Payment_Deduction_ID = getPaymentDeduction( Payment, Deduction  );
						if ( Payment_Deduction_ID != 0 )
							PaymentDeduction = P_Payment_Deduction.get ( Env.getCtx (), Payment_Deduction_ID, trxName);
						else
							PaymentDeduction = new P_Payment_Deduction ( Env.getCtx (), -1, trxName);

						_calcul_deduction();

						
						PaymentDeduction.setP_Employee_ID( this.Payment.getP_Employee_ID ());
						PaymentDeduction.setP_Deduction_ID( EmployeeDeduction.getP_Deduction_ID() );
						PaymentDeduction.setP_Year_ID(  this.Period.getP_Year_ID () );
						PaymentDeduction.setP_Period_ID( this.Period.getP_Period_ID () );
						PaymentDeduction.setP_Payment_ID( this.Payment.getP_Payment_ID ());


						//On vérifie si les montants inscrits repondent aux criteres
						//de la deduction
						
						BigDecimal MinimumAmountToWithhold = Deduction_Param.getMinimumAmountToWithhold();
						BigDecimal MaximumAmountToWithhold = Deduction_Param.getMaximumAmountToWithhold();
						BigDecimal MaximumBackpaymentAccumulated = Deduction_Param.getMaximumBackpaymentAccumulated();
						BigDecimal MaximumBackpayment = Deduction_Param.getMaximumBackpayment();
						BigDecimal MaximumEmployer = Deduction_Param.getMaxEmployer();
						
						//MinimumAmountToWithhold : Minimum a prelever
						
						if(MinimumAmountToWithhold != null && MinimumAmountToWithhold.compareTo(Env.ZERO) > 0)
						{
							//si la part de l'employé est plus petite que le minimum
							if(PaymentDeduction.getEmployee_Part( ).compareTo(MinimumAmountToWithhold) < 0)
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

						// Maximum Employer
						if(MaximumEmployer != null && MaximumEmployer.compareTo(Env.ZERO) > 0)
						{
							//si la part de l'employer est plus grande que le maximum employer
							if(this.employerAmount.compareTo(MaximumEmployer) > 0 )
							{
								this.employerAmount= MaximumEmployer ;
							}
						}

/*						
						//MaximumBackpaymentAccumulated : Arrérage Accumulé
						
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
*/
						
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
						 
						int       i_methodCalcul   = new Integer( methodCalcul ).intValue();
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

						MethodAmount = this.employerAmount;
						if ( s_Employer.containsKey( methodCalcul ) )
						{
							MethodAmount = this.employerAmount.add( (BigDecimal)s_Employer.get( methodCalcul ));
							s_Employer.remove( methodCalcul );
						}
						s_Employer.put( methodCalcul , MethodAmount);

						MethodAmount = this.salaryAdmissible;
						if ( s_SalaryAdmissible.containsKey( methodCalcul ) )
						{
							MethodAmount = this.salaryAdmissible.add( (BigDecimal)s_SalaryAdmissible.get( methodCalcul ));
							s_SalaryAdmissible.remove( methodCalcul );
						}
						s_SalaryAdmissible.put( methodCalcul , MethodAmount);


						this.hoursAdmissible  = this.hoursAdmissible.subtract( cumHrsAdmissible );
						this.salaryAdmissible = this.salaryAdmissible.subtract( cumSalAdmissible );
					
						this.employeeAmount   = this.employeeAmount.subtract( cumEmpAmount );
						this.employerAmount   = this.employerAmount.subtract( cumEmprAmount );


						if ( this.salaryAdmissible.compareTo( ZERO ) == 0 && i_methodCalcul != 17 && i_methodCalcul != 5 && i_methodCalcul != 6 && i_methodCalcul != 7 && i_methodCalcul != 16 && i_methodCalcul != 29 )
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
						

					
						// *************************************************************
						// il n'y a pas de remboursement possible pour les assurances
						if (( i_methodCalcul == 14 || i_methodCalcul == 15 ) && (this.employeeAmount.compareTo(ZERO) < 0 || this.employerAmount.compareTo(ZERO) < 0))
						{
							this.salaryAdmissible = ZERO;
							this.hoursAdmissible = ZERO;
							this.employeeAmount = ZERO;
							this.employerAmount = ZERO;
						}

//						
						// Si la paye est pour être inférieur a 0. Le montant de la déduction égal le montant de la paie.
						if ( Payment.getGrossEarnings().compareTo( ZERO) >= 0 )
						{
/*							
							if ( NetPay.subtract( this.employeeAmount ).compareTo( ZERO) < 0  )
							{
								
								if (rs.getString("IsAccumulation").equals("Y"))
								{
									PaymentDeduction.setAccumulationAmount( this.employeeAmount.subtract( NetPay ).setScale(2, BigDecimal.ROUND_HALF_UP ) );
								}
								
								this.employeeAmount = NetPay ;
							}
*/							
/*
							else if ( rs.getString("IsAccumulation").equals("Y") )
							{
								BigDecimal RetrievalAmount = EmployeeDeduction.getToRetrieveAmount();
						        if ( RetrievalAmount.compareTo( ZERO) != 0 )
								{
									if ( ( NetPay.subtract( this.employeeAmount ).subtract( RetrievalAmount ) ) .compareTo( ZERO) < 0 )
									{
										RetrievalAmount = NetPay.subtract( this.employeeAmount ).subtract( RetrievalAmount ).abs();
									}

									PaymentDeduction.setRetrievalAmount( RetrievalAmount );
									this.employeeAmount = this.employeeAmount.add( RetrievalAmount );
								}
							}
*/							
						}
//						
						//
						// *************************************************************

//						if ( this.employeeAmount.compareTo( PaymentDeduction.getEmployee_Part()) != 0 &&  Deduction.getP_Deduction_ID() != 1002562 && Deduction.getP_Deduction_ID() != 1002563 )
						if ( this.employeeAmount.compareTo( PaymentDeduction.getEmployee_Part()) != 0 )
						{
							// Ajout stephane pour p_valide_calculs
							String queryINS = "insert into p_valide_calculs (p_period_id, p_employee_id,p_deduction_id) select " + PaymentDeduction.getP_Period_ID() + "," + PaymentDeduction.getP_Employee_ID() + "," + Deduction.getP_Deduction_ID();
							DB.executeUpdate(queryINS, null);
							String message = "*W* Employée : " + Employee.getValue() + " Déduction : " + Deduction.getValue() + " Part employé ADP : " + PaymentDeduction.getEmployee_Part() + " Part employé PGI : " + this.employeeAmount ; 
							log.log(Level.WARNING, message  );
						    P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, message, trxName );
						}

						// Déduction 02 Fond pension
						if ( Deduction.getP_Deduction_ID() == 1002580 )
							this.employerAmount = PaymentDeduction.getEmployee_Part();

						// Déduction 63 FTQ
						if ( Deduction.getP_Deduction_ID() == 1002627 )
							this.employerAmount = PaymentDeduction.getEmployer_Part();
/*						{
							this.employerAmount = PaymentDeduction.getEmployee_Part();
							if ( this.employerAmount.compareTo(new BigDecimal( 250)) > 0)
							{
								this.employerAmount = new BigDecimal( 250);
							}
						}

						// PATCH... 
						if ( ( Employee.getP_Employee_ID() == 1002111 || Employee.getP_Employee_ID() == 1002066)  
							   && Deduction.getP_Deduction_ID() == 1002627 && Period.getP_Period_ID() == 1001919	 )
								this.employerAmount= Env.ZERO ;
*/

						
//						PaymentDeduction.setEmployee_Part( this.employeeAmount.setScale(2,BigDecimal.ROUND_HALF_UP) );
						PaymentDeduction.setEmployer_Part( this.employerAmount.setScale(2,BigDecimal.ROUND_HALF_UP) );
						PaymentDeduction.setSalary_Eligible( this.salaryAdmissible.setScale(2,BigDecimal.ROUND_HALF_UP) );
						PaymentDeduction.setHours_Eligible( this.hoursAdmissible.setScale(2,BigDecimal.ROUND_HALF_UP) );
						PaymentDeduction.setIsExonerated( isExonerated );
						PaymentDeduction.setOrigine("FTP");
						PaymentDeduction.setP_Deduction_Param_ID( Deduction_Param.getP_Deduction_Param_ID());
						PaymentDeduction.setP_Employee_Deduction_ID( EmployeeDeduction.getP_Employee_Deduction_ID());

						//- Calcul par Période d'origine


//						PaymentDeduction.save( );
						
						if (    
						        PaymentDeduction.getEmployee_Part().compareTo( ZERO ) != 0 ||
						        PaymentDeduction.getEmployer_Part().compareTo( ZERO ) != 0 ||
//						        PaymentDeduction.getSalary_Eligible().compareTo( ZERO )!= 0 ||
						        PaymentDeduction.getHours_Eligible().compareTo( ZERO ) > 0 ||
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
		
	}
	
	//
	// Internal procedure for calcul deduction and taxable benefit.
	//
	private void _calcul_deduction () throws Exception
	{
		this.hoursAdmissible  = ZERO;
		this.salaryAdmissible = ZERO;
		this.employeeAmount   = ZERO;
		this.employerAmount   = ZERO;

		String    methodCalcul     = Method_Deduction.getValue();

		int       i_methodCalcul   = new Integer( methodCalcul ).intValue();

		if ( ! EmployeeDeduction.isActive() )
		{
           return;		    
		}

		if ( EmployeeDeduction.isExonerated() )
		{
           return;		    
		}
		
		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName()  + " method - "   + methodCalcul  + " column - "   + Deduction_Param.getP_Column_Adm_ID()  );
		
		
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
				if ( Region.getName().trim().equals(  "Québec" ) || Region.getName().trim().equals("QC") )  
					calcul_rrq_rpc( );  			// RRQ
			break;
			case 3: 
				if (! ( Region.getName().trim().equals(  "Québec" ) || Region.getName().trim().equals("QC")) )  
					calcul_rrq_rpc( );			// RPC
			break;
			case 4: 	calcul_employment_insurance(  );// Employment insurance 
			break;
			case 5: 	calcul_pension_fund (  ); // pension Fund  
			break;
			case 8: 	calcul_syndicat (  ); // Syndicat
			break;
			case 9: 	calcul_federal_tax.ImportYTDcalcul_federal_tax( taxID, null);
			break;
/*			case 10: 	calcul_federal_tax.calcul_federal_retro_tax( Env.getCtx(), this, taxID, trxName); // Federal Tax gratification 
			break;
*/			
			case 11:
				if ( Region.getName().trim().equals(  "Québec" ) || Region.getName().trim().equals("QC") )  
					ImportYTDCalcul_provincial_tax.ImportYTDcalcul_quebec_tax( Env.getCtx(), this, TimeSheetPeriod.getPayDate(), trxName, false); // Provincial Tax
/*				else
					calcul_federal_tax.calcul_Provincial( taxID );
			break;
			case 12: 	
				if ( Region.getName().trim().equals(  "Québec" ) || Region.getName().trim().equals("QC") )  
					Calcul_provincial_tax.calcul_quebec_retro_tax( Env.getCtx(), this, TimeSheetPeriod.getPayDate(), trxName); // Provincial Tax gratification
//				else
//					calcul_federal_tax.calcul_Provincial( taxID);
*/					
			break;			
			case 14:    calcul_insurance( );
			break;
			case 32:    calcul_insurance_M32();
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
			default :   calcul_deduction_others( );
		}

		isExonerated = false;
		//
		// Gestion des Exception et Exonération. Il y a maintenant 2 tables pour la gestion
		// la table employé que l'usager peut définir et la table sous le payment que 
		// le system aliment a partir des profiles absences long durée (ALD )
		//
//		Employee_Excep();
//		Payment_Excep();

		
	
	}   // calcul_calcul_deduction


	private BigDecimal cumEmpAmount = ZERO;
	private BigDecimal cumEmprAmount = ZERO;
	private BigDecimal cumSalAdmissible = ZERO;
	private BigDecimal cumHrsAdmissible  = ZERO;

	
	private BigDecimal get_matrix_hoursAdmissible ( int columnAdmin )
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
	
	private BigDecimal get_matrix_salaryAdmissible ( int columnAdmin )
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
	


	private void getSalaryAdmissible( P_Deduction_Param Deduction_Param ) throws Exception
	{
		
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

	}
	
	//
	// Calcul RRQ and RPC
	//
	private void calcul_rrq_rpc (  ) throws Exception
	{

		boolean federal = true;
		//
		// Québec ou Fédéral
		//
		if ( Region.getName().trim().equals(  "Québec" ) || Region.getName().trim().equals("QC") )  
			federal = false;
		else
			federal = true;
		
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
		rpc_rate = rpc_rate.divide( new BigDecimal( 100 ), 4, BigDecimal.ROUND_HALF_UP );

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
		
		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param );
		
		// 18 Year old...
		// employé ne cotise pas le mois de son anniversaire.
       //		int age = Employee.getAge( Period.getStartDate());
		int age = Employee.getAge( TimeUtil.addMonth( Period.getStartDate(), 1));

		if ( age <  18 )
		{
			return ;
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
		
		sal = sal.subtract( tmp ).setScale(2, BigDecimal.ROUND_HALF_UP);

		if ( sal.compareTo( Env.ZERO ) == 0 )
		{
			return ;
		}


		this.employeeAmount = sal.multiply( rpc_rate );

		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " sal " + sal +" employee amount " + this.employeeAmount + " employer amount " + employerAmount );
		
		
		// Read Cumulative information about a deduction for this Year..
        get_deduction_cumulative( );

		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " cumulative " + cumulative.toString() ) ;

        Amount_Rpc = this.employeeAmount.multiply( new BigDecimal( Frequency.getNumberOfPeriod()));

        if ( this.employeeAmount.add( this.cumEmployeeAmount  ).compareTo( rpc_max ) > 0 )
		{
			this.employeeAmount = rpc_max.subtract( this.cumEmployeeAmount ); 
			this.salaryAdmissible = rpc_max_gain.subtract( this.cumSalaryAdmissible  );
			
			Amount_Rpc = rpc_max;
		}
        
        if (Amount_Rpc.compareTo( rpc_max ) > 0)
			Amount_Rpc = rpc_max;
	
/*        
		if ( this.employeeAmount.compareTo( ZERO ) < 0 )
		{
			this.employeeAmount = ZERO;
		}
*/	
		
		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_DOWN );
		
		this.employerAmount =  this.employeeAmount ;
		
		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " salary Admissible " + salaryAdmissible + " sal " + sal +" employee amount " + this.employeeAmount + " employer amount " + employerAmount );
		
// AJOUT
// ****
		this.employerAmount = PaymentDeduction.getEmployee_Part();

		
	}   // calcul_rrq_rpc
	

	//
	// Régime québécois d'assurance parental
	//
	private void calcul_rqap (  ) throws Exception
	{

		this.salaryAdmissible = ZERO;
		this.hoursAdmissible = ZERO;
		this.employeeAmount = ZERO;
		this.employerAmount = ZERO;

//		if ( ! ( Region.getName().trim().equals(  "Québec" ) || Region.getName().trim().equals("QC") ) )  
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
		getSalaryAdmissible( Deduction_Param );
		
		if ( salaryAdmissible == null )
		{
			this.employeeAmount = ZERO;
			this.employerAmount = ZERO;
		}
		else
		{
			this.employeeAmount = salaryAdmissible.multiply( rqap_rate_employee );
			
//			 ICI 			
			this.employeeAmount = this.employeeAmount.setScale( 2, BigDecimal.ROUND_HALF_UP  );
			if ( this.employeeAmount.compareTo( PaymentDeduction.getEmployee_Part() ) != 0 ) 
			{
				this.salaryAdmissible = PaymentDeduction.getEmployee_Part().divide(rqap_rate_employee, 2, BigDecimal.ROUND_HALF_UP);
				String message = "*W* Employée : " + Employee.getValue() + " Déduction : 06 - RQAP - Vérifier salaire admissible " + this.employeeAmount + " " + PaymentDeduction.getEmployee_Part()   ; 
				log.log(Level.WARNING, message  );
			    P_Time_Sheet_Error.NewMessage( Timesheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, message, trxName );
			}
			
			this.employerAmount = this.salaryAdmissible.multiply( rqap_rate_employer );
		}
		
		// Read Cumulative information about a deduction for this Year..
		P_Deduction Deduction    = P_Deduction.get( Env.getCtx(), Deduction_Param.getP_Deduction_ID(), trxName);
        get_deduction_cumulative_family( Deduction.getP_Deduction_Family_ID() );

        // Conserve le montant employé RQAP pour crédit impôt (K2)
		Amount_RQAP = PaymentDeduction.getEmployee_Part().multiply( new BigDecimal( Frequency.getNumberOfPeriod()) );

		// Si l'employé atteint son maximum
		if ( (this.employeeAmount.add( this.cumEmployeeAmount )).compareTo( rqap_max_annual_yee ) > 0 )
		{
			this.employeeAmount = rqap_max_annual_yee.subtract( this.cumEmployeeAmount );
			Amount_RQAP = rqap_max_annual_yee;
		}
		
        // On s'assure que le crédit RQAP ne dépasse pas le max
		if ( Amount_RQAP.compareTo( rqap_max_annual_yee ) > 0)
			Amount_RQAP = rqap_max_annual_yee;
		
		// Si l'employeur atteint le maximum
		if ( (this.employerAmount.add( this.cumEmployerAmount )).compareTo( rqap_max_annual_yer ) > 0 )
		{
			this.employerAmount = rqap_max_annual_yer.subtract( this.cumEmployerAmount );
		}
	
		
		
		//+ Magnetho 262 
		// Validation du maximum du salaire admissible
		if (( this.cumSalaryAdmissible.add( this.salaryAdmissible )).compareTo( MaxRevenusAss ) > 0)
			this.salaryAdmissible = MaxRevenusAss.subtract(this.cumSalaryAdmissible );
		//- Magnetho 262

		
		this.employeeAmount = this.employeeAmount.setScale( 2, BigDecimal.ROUND_HALF_UP  );
		this.employerAmount = this.employerAmount.setScale( 2, BigDecimal.ROUND_HALF_UP  );

		
		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " employee amount " + this.employeeAmount + " employer amount " + employerAmount );
		
		
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

		if ( Region.getName().trim().equals(  "Québec" ) || Region.getName().trim().equals("QC") )  
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
		{	ei_rate_employer = (BigDecimal)taxTable.get( "AE.42");
			// TODO Hardcoder ( Wachiwem )
			if ( Employee.getAD_Org_ID() == 1000007 )
				ei_rate_employer = (BigDecimal)taxTable.get( "AE.43");
		}	
		else
		{
			ei_rate_employer = (BigDecimal)taxTable.get( "AE.41");
		}
		
//		ei_rate_employer = Deduction_Param.getEmployerRate();

		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param );
		
		if ( salaryAdmissible == null )
		{
			this.employeeAmount = ZERO;
			this.employerAmount = ZERO;
		}
		else
		{
			this.employeeAmount = salaryAdmissible.multiply( ei_rate_employee );
//			m.m
			this.employerAmount = PaymentDeduction.getEmployee_Part().multiply( ei_rate_employer );
			this.salaryAdmissible = PaymentDeduction.getEmployee_Part().divide(ei_rate_employee,2,BigDecimal.ROUND_HALF_UP);
//			this.employerAmount = this.employeeAmount.multiply( ei_rate_employer );
		}
		
		// Read Cumulative information about a deduction for this Year..
		P_Deduction Deduction    = P_Deduction.get( Env.getCtx(), Deduction_Param.getP_Deduction_ID(), trxName);
        get_deduction_cumulative_family( Deduction.getP_Deduction_Family_ID() );

		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " cumulative " + cumulative.toString() ) ;
		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " maximum " + ei_max_annual ) ;

		Amount_Employment_Insurance = this.employeeAmount.multiply( new BigDecimal( Frequency.getNumberOfPeriod()) );

		if ( this.employeeAmount.add( this.cumEmployeeAmount  ).compareTo( ei_max_annual ) > 0 )
		{
			
			this.employeeAmount = ei_max_annual.subtract( this.cumEmployeeAmount ); 
			this.employerAmount = this.employeeAmount.multiply( ei_rate_employer );

			Amount_Employment_Insurance = ei_max_annual;

		}

		//+ Magnetho 262
		// Validation du maximum du salaire admissible
        //
		//if (( this.cumSalaryAdmissible.add( this.salaryAdmissible )).compareTo( ei_max_gain ) > 0)
		//	this.salaryAdmissible = ei_max_gain.subtract(this.cumSalaryAdmissible );
		//- Magnetho 262

		
		if ( Amount_Employment_Insurance.compareTo( ei_max_annual ) > 0)
			Amount_Employment_Insurance = ei_max_annual;
		
/*		if ( this.employeeAmount.compareTo( ZERO ) < 0 )
		{
			this.employeeAmount = ZERO;
			this.employerAmount = ZERO;
		}
*/		

		this.employeeAmount = this.employeeAmount.setScale( 2, BigDecimal.ROUND_HALF_UP  );
		this.employerAmount = this.employerAmount.setScale( 2, BigDecimal.ROUND_HALF_UP  );

		
		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " employee amount " + this.employeeAmount + " employer amount " + employerAmount );
		
		
	}   // employment insurance
	
	
	//
	// calcul_insurance
	//
	private void calcul_insurance( ) throws Exception
	{

		//
		//
		// Recherche des montants admissible /
		// Search admisible amount
		//
		getSalaryAdmissible( Deduction_Param );

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

		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		this.employerAmount = this.employerAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		
		boolean Quebec = false;
		boolean Ontario = false;
		if ( Region.getName().trim().equals(  "Québec" ) || Region.getName().trim().equals("QC") )
			Quebec = true;

		if ( Region.getName().trim().equals(  "Ontario" ) || Region.getName().trim().equals("ON") )
			Ontario = true;
		
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

		
	}   // calcul_insurance



	private void calcul_insurance_M32(  ) throws Exception
	{
		
		//
		//
		// Recherche des montants admissible /
		// Search admisible amount
		BigDecimal insurableSalary = getInsurableSalary();  

		//
		// Si l'employé a un régime B ou C le salaire admissible est multiplier pour 2
		// 
		int RegimeB = 1000047; 
		int RegimeC = 1000044;
		P_Employee_Profile EmployeeProfile = P_Employee_Profile.get( Env.getCtx(), Employee.getP_Employee_ID(), RegimeB, Period.getStartDate(), trxName);
		if ( EmployeeProfile != null )
			insurableSalary = insurableSalary.multiply( new BigDecimal( 2 ));

		EmployeeProfile = P_Employee_Profile.get( Env.getCtx(), Employee.getP_Employee_ID(), RegimeC, Period.getStartDate(), trxName);
		if ( EmployeeProfile != null )
			insurableSalary = insurableSalary.multiply( new BigDecimal( 2 ));


		this.hoursAdmissible  = ZERO;
		this.salaryAdmissible = insurableSalary;
	

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

		this.employeeAmount =  insurableSalary.multiply( EmployeeRate ).divide( new BigDecimal( 2 ), 2, BigDecimal.ROUND_HALF_UP) ;
		this.employerAmount =  insurableSalary.multiply( EmployerRate ).divide( new BigDecimal( 2 ), 2, BigDecimal.ROUND_HALF_UP) ;

				
		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		this.employerAmount = this.employerAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		
		boolean Quebec = false;
		boolean Ontario = false;
		if ( Region.getName().trim().equals(  "Québec" ) || Region.getName().trim().equals("QC") )
			Quebec = true;

		if ( Region.getName().trim().equals(  "Ontario" ) || Region.getName().trim().equals("ON") )
			Ontario = true;
		
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

		
	}   // calcul_insurance


	
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
			age = Employee.getAge( DateForAge ) + 1;
		}
		else
		{
			age = Employee.getAge( DateForAge );  
		}
		BigDecimal InsuranceRate = ZERO;
		BigDecimal InsuranceAmount = ZERO;
		this.salaryAdmissible = ZERO;

		String sqlServerQuery  = "Select P_Deduction_Insurance_Bound_ID "
			+ "FROM P_Deduction_Insurance_Bound, P_Deduction_Insurance, P_Period  "
			+ "WHERE P_Deduction_Insurance_Bound.P_Deduction_Insurance_Id = P_Deduction_Insurance.P_Deduction_Insurance_Id "
			+ " AND P_Deduction_Insurance.P_Deduction_Param_ID = " + Deduction_Param.getP_Deduction_Param_ID()
			+ " AND P_Period.P_Period_ID = " + Period.getP_Period_ID() 
			+ " AND " + age + "  between AgeLimitMin and DateADD( day, AgeLimitMax, -1)";
		
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

				if ( Deduction_Param.getTaxeRate() != null && Deduction_Param.getTaxeRate().compareTo( ZERO ) != 0)
				{
					InsuranceAmount = InsuranceAmount.multiply( Env.ONE.add( Deduction_Param.getTaxeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ) ).setScale(2,BigDecimal.ROUND_HALF_UP));
				}
				

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

		
		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		this.employerAmount = this.employerAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		
		//Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + "employee amount " + this.employeeAmount + " employer amount " + employerAmount );
		
		
	}   // calcul_insurance_age_bound
	
	
	
	//
	// Salaire de l'employé + Salaire variable de l'année précédente.
	//
	private BigDecimal getInsurableSalary() throws Exception 
	{

		BigDecimal insurableSalary = Payment.getAnnualSalary();
		BigDecimal cumAmount = Env.ZERO;
		
		//TODO Harcode colonne 112 salaire Admissible fixe 
		//
		// Lecture des primes et autre gain admissible au calcul des assurances.
		//
		String query = null;
		query = "Select sum( Standard_Time_Amount ) Standard_Time_Amount FROM P_Standard_Time "
			+ "   Inner Join P_Gain ON P_Standard_Time.P_Gain_ID = P_Gain.P_Gain_ID "
			+ "   Inner Join P_Gain_Parameter ON P_Gain_Parameter.P_Gain_ID = P_Gain.P_Gain_ID " 
			+ "   Inner Join P_Gain_Column ON P_Gain_Column.P_Gain_Parameter_ID = P_Gain_Parameter.P_Gain_Parameter_ID AND IsAdmissible = 'Y' AND P_Gain_Column.P_Column_Adm_ID = 112"
			+ "   Where " + DB.TO_DATE( Period.getStartDate()) + " Between StartDate and IsNull(EndDate, " + DB.TO_DATE( Period.getStartDate() ) + " )"
			+ "     AND P_Standard_Time.P_Employee_ID = " + Employee.getP_Employee_ID()
			+ "     AND P_Standard_Time.IsActive = 'Y' "
 		    ;
		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " Query " + query ) ;
		
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				cumAmount     =   rs.getBigDecimal("Standard_Time_Amount");
			}
			
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "InsurableSalary : " + query, e);
		}
		
		if ( cumAmount != null)
			insurableSalary = insurableSalary.add(cumAmount );

		//
		// Une banque nous retourne le salaure variable de l'année précédente.
		//
		if ( Deduction_Param.getP_Credits_ID() != 0 )
		{
			BigDecimal solde = P_Credits.getEmployeeSolde(Deduction_Param.getP_Credits_ID(), EmployeeDeduction.getP_Employee_ID(), Period.getEndDate(), trxName );
			this.salaryAdmissible = this.salaryAdmissible.add( solde);
		}

		BigDecimal solde = P_Credits.getEmployeeSolde(Deduction_Param.getP_Credits_ID(), EmployeeDeduction.getP_Employee_ID(), Period.getEndDate(), trxName );
		insurableSalary = insurableSalary.add( solde);


		getSalaryAdmissible( Deduction_Param );

	
		// Salaire Minimum est différent selon le département de l'employé.
		P_Insurance_Param InsuranceParam = P_Insurance_Param.get( Env.getCtx(), Payment.getP_Department_ID(), Period.getStartDate() , trxName);
		
		if ( InsuranceParam != null )
		{
			if ( insurableSalary.compareTo( InsuranceParam.getMinimumSalaryInsurable() ) < 0 )
				insurableSalary = InsuranceParam.getMinimumSalaryInsurable();
		}

/*
		P_Insurance_Program InsuranceProgram = P_Insurance_Program.get( Env.getCtx(), Payment.getP_Department_ID(), Period.getStartDate() , trxName);
		
		if ( InsuranceProgram != null )
		{
			
		}
*/
		
		return insurableSalary;
		
	}

	//
	// Calcul les assurance invalidité longue durée.
	//
	private void calcul_insurance_LTD() throws Exception
	{
		//
		//
		// Recherche des montants admissible /
		// Search admisible amount
		
		BigDecimal insurableSalary = getInsurableSalary();  

		this.hoursAdmissible  = ZERO;
		this.salaryAdmissible = insurableSalary;
		
		insurableSalary = insurableSalary.divide(new BigDecimal(12),2,BigDecimal.ROUND_HALF_UP );
		
		BigDecimal layer1 =  new BigDecimal(2000);
		BigDecimal rate1  =  new BigDecimal(0.6);
		
		BigDecimal layer2 =  new BigDecimal(3000);
		BigDecimal rate2  =  new BigDecimal(0.5);
		BigDecimal rate3  =  new BigDecimal(0.45);
		
		this.employerAmount = ZERO;
		
		if ( insurableSalary.compareTo( layer1 ) > 0 )
		{
			this.employeeAmount = layer1.multiply( rate1 );
			insurableSalary = insurableSalary.subtract( layer1 );

			if ( insurableSalary.compareTo( layer2 ) > 0 )
			{
				this.employeeAmount = this.employeeAmount.add( layer2.multiply( rate2 ) );
				insurableSalary = insurableSalary.subtract( layer2 );
				this.employeeAmount = this.employeeAmount.add( insurableSalary.multiply( rate3 ) );
			}
			else
			{
				this.employeeAmount = this.employeeAmount.add( insurableSalary.multiply( rate2 ) );
			}
		}
		else
		{
			this.employeeAmount = this.salaryAdmissible.multiply( rate1 ); 
		}

		
		this.employeeAmount = this.employeeAmount.multiply( Deduction_Param.getEmployeeRate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP ));

		this.employeeAmount = this.employeeAmount.divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP);
		
		boolean Quebec = false;
		boolean Ontario = false;
		if ( Region.getName().trim().equals(  "Québec" ) || Region.getName().trim().equals("QC") )
			Quebec = true;

		if ( Region.getName().trim().equals(  "Ontario" ) || Region.getName().trim().equals("ON") )
			Ontario = true;
		
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

		this.employeeAmount = this.employeeAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );
		this.employerAmount = this.employerAmount.setScale( 2 , BigDecimal.ROUND_HALF_UP );

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
			P_Column_Adm ColumnAdm = P_Column_Adm.get( Env.getCtx (), Deduction_Param.getP_Column_Adm_ID() , trxName); 

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


		//Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " employee amount " + this.employeeAmount + " employer amount " + employerAmount );
		
	}  // Pension Fund 

	
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

		BigDecimal EmployeeAmount = EmployeeDeduction.getEmployeeAmount(Deduction_Param );
		BigDecimal EmployerAmount = EmployeeDeduction.getEmployerAmount(Deduction_Param ); 

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
         if ( EmployeeAdvance.getP_Employee_Advance_ID() != 0 )
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
		
		//
		// Recherche les montants admissible /
		getSalaryAdmissible( Deduction_Param );

		BigDecimal EmployeeRate = EmployeeDeduction.getEmployeeRate(Deduction_Param ); 
		BigDecimal EmployerRate = EmployeeDeduction.getEmployerRate(Deduction_Param ); 

		BigDecimal EmployeeAmount = EmployeeDeduction.getEmployeeAmount(Deduction_Param );
		BigDecimal EmployerAmount = EmployeeDeduction.getEmployerAmount(Deduction_Param ); 

	    employeeAmount = ZERO;
	    employerAmount = ZERO;
	    
		if ( Deduction_Param.getP_Column_Adm_ID() != 0 )
		{
			P_Column_Adm ColumnAdm = P_Column_Adm.get( Env.getCtx (), Deduction_Param.getP_Column_Adm_ID() , trxName); 

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
			P_Column_Adm ColumnAdm = P_Column_Adm.get( Env.getCtx (), Deduction_Param.getP_Column_Adm_ID() , trxName); 

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
		if ( i_methodCalcul == 16 || i_methodCalcul == 29 )
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
			+ " and P_Payment_Deduction.P_Period_ID < " + this.Period.getP_Period_ID()
			+ " and P_Payment_Deduction.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
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
/*
	//
	// Read Cumulative information about a deduction for this Year..
	// 
	private void get_deduction_cumulative_rrq( ) throws Exception
	{
		String query = null;
		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible"
			+ " from P_Payment_Deduction, P_Payment"
			+ " where P_Payment_Deduction.P_Deduction_ID = " + EmployeeDeduction.getP_Deduction_ID ()
			+ " and P_Payment.P_Year_ID = " + this.Period.getP_Year_ID ()
			+ " and P_Payment.P_Period_ID <= " + this.Period.getP_Period_ID()
			+ " and P_Payment_Deduction.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
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
*/
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
			+ " and P_Payment.P_Period_ID < " + this.Period.getP_Period_ID()
			+ " and P_Payment_Deduction.P_Employee_ID = " + this.Payment.getP_Employee_ID ()
			+ " and P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID "
			+ " and P_Deduction.P_Deduction_Family_ID = " + P_Deduction_Family_ID 
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
			query = "Select isnull( sum( Employee_Part ),0) Employee_Part"
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
				if(employeePartCumul.compareTo(NonAnnualMaximum) <= 0)
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

//	a tester a retour de réunion
	//
	// Check cumulative amount for a deduction for all years
	// 
	private void get_deduction_cumulated_curYears( ) throws Exception
	{
		String query = null;
		BigDecimal employeePartCumul = Env.ZERO;
		BigDecimal maxAllowedCurrent = Env.ZERO;
		//on vérifie si il y a un maximum annuel

		BigDecimal AnnualMaximum = EmployeeDeduction.getAnnualMaximum(Deduction_Param ); 

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
					if(employeePartCumul.compareTo(AnnualMaximum) < 1)
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
 		    + " and P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID AND P_Payment.P_Year_ID = " + TimeSheetPeriod.getP_Year_ID ()  // <-- Correction pour un année antérieur ne doit pas aller revoir le cumulatif.
 		    + " and P_Payment.P_Payment_ID <> " + Payment.getP_Payment_ID()
 		    + " and P_Payment.AD_Org_ID = " + Payment.getAD_Org_ID()
 		    ;
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

	//
	// TODO : creer une class independate qui retourn les 2 valeurs possibles au lieux d'avoir 4 variable global;
	// Read Cumulative information about a deduction for this Period..
	// 
	private void get_deduction_cumulative_per2(  ) throws Exception
	{
		String query = null;
		query = "Select isnull( sum( Employee_Part ),0) Employee_Part, isnull(sum( Employer_Part),0) Employer_Part, isnull(sum( Salary_Eligible ), 0) Salary_Eligible, isnull( sum( Hours_Eligible ), 0) Hours_Eligible, isnull(sum( RetrievalAmount),0) RetrievalAmount, isnull(sum( AccumulationAmount),0) AccumulationAmount "
			+ " from P_Payment_Deduction" //, P_Payment"
			+ " where P_Payment_Deduction.P_Deduction_ID = " + EmployeeDeduction.getP_Deduction_ID ()
			+ " and P_Payment_Deduction.P_Period_ID = " + Period.getP_Period_ID ()
			+ " and P_Payment_Deduction.P_Employee_ID = " + Payment.getP_Employee_ID ()
// 		    + " and P_Payment_Deduction.P_Payment_ID = P_Payment.P_Payment_ID AND P_Payment.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID ()  // <-- Correction pour un année antérieur ne doit pas aller revoir le cumulatif.
 		    + " and P_Payment_Deduction.P_Payment_ID <> " + Payment.getP_Payment_ID()
// 		    + " and P_Payment.AD_Org_ID = " + Payment.getAD_Org_ID()
 		    ;
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

	//
	// Read Total of Taxable Benefit for the period
	//
	private BigDecimal get_TaxableBenefit_cumulative_per( P_Employee_Taxable_Benefit  Taxable_Benefit) throws Exception
	{
		BigDecimal cumBenefitAmount = ZERO;
		
		String query = null;
		query = "SELECT sum( Taxable_Benefit_Amount ) Taxable_Benefit_Amount FROM P_Payment_Taxable_Benefit"
			  + " WHERE IsActive='Y' "  
		      + " AND P_Taxable_Benefit_ID = " + Taxable_Benefit.getP_Taxable_Benefit_ID()
              + " AND P_Period_ID = " + this.Period.getP_Period_ID ()
			  + " AND P_Employee_ID = " + Taxable_Benefit.getP_Employee_ID ();

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


	public Hashtable<String, QuantityAmountPair> getS_Matrix()
	{
	  return s_Matrix;
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
    	return Amount_Rpc;
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

	public void	setHoursAdmissible( BigDecimal amount)
	{
		hoursAdmissible = amount;
	}

	public void	setSalaryAdmissible( BigDecimal amount)
	{
		salaryAdmissible = amount;
	}


	private int getPaymentDeduction( P_Payment Payment, P_Deduction Deduction  )
	{
		int id = 0;
		String sql = "Select P_Payment_Deduction_ID from P_Payment_Deduction where P_Payment_ID = " + Payment.getP_Payment_ID() + " AND P_Deduction_ID = " + Deduction.getP_Deduction_ID();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("ImportYTD getP_PaymentDeduction_ID - " + e);
		}
		
		return id;
		
	}
	
	// *************************
	//                          
	// ***************************     
	private void read_gain_info (  ) throws Exception
	{
		String query = null;
		query = "SELECT P_Payment_Gain.P_Gain_Parameter_ID, P_Payment_Gain.QuantityCalc, P_Payment_Gain.AmountCalc, P_Payment_Gain.P_Gain_ID, P_Payment_Gain.P_Assignment_ID, P_Method_Gain_ID, P_Payment_Gain.Day "
			  +" FROM P_Payment, P_Payment_Gain, P_Gain WHERE P_Payment_Gain.P_Gain_ID = P_Gain.P_Gain_ID ";  
//		query += " AND P_Payment_ID = " + Payment.getP_Payment_ID ();
		query += " AND P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID ();
		query += " AND P_Payment_Gain.P_Period_ID = " + this.Period.getP_Period_ID ();
		query += " AND P_Payment.P_Payment_ID = P_Payment_Gain.P_Payment_ID AND P_Payment.P_Year_ID = " + this.TimeSheetPeriod.getP_Year_ID();  // <-- Correction pour un année antérieur ne doit pas aller revoir le cumulatif.
		query += " ORDER BY line";
		
		//Log.trace( 5,"calcul_gain - query=" + query);
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
	
		while (rs.next ())
		{
			
			int P_Gain_Parameter_ID = rs.getInt("P_Gain_Parameter_ID");
			BigDecimal hoursAdmissible = ZERO;
			if ( rs.getInt( "P_Method_Gain_ID") != 105 )
				hoursAdmissible = rs.getBigDecimal("QuantityCalc");
			BigDecimal salaryAdmissible = rs.getBigDecimal("AmountCalc");
			
			update_matrix ( P_Gain_Parameter_ID, hoursAdmissible, salaryAdmissible, "Gain", rs.getInt("P_Assignment_ID"), rs.getTimestamp("Day") );
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
		
//		log.debug ("read_gain_info End");
		return ;
		
	}   

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
		
		int no = 0;
		
		BigDecimal hoursAdm;
		BigDecimal salaryAdm;
		
		String query = null;
		
		if ( 0 == table.compareTo( "Gain" )) {
			
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

					P_Gain_Parameter GainParameter = P_Gain_Parameter.get( Env.getCtx(), p_id, trxName);
					P_Gain Gain = new P_Gain( Env.getCtx(), GainParameter.getP_Gain_ID(), trxName );

					if ( Gain.getP_Method_Gain_ID() == 104 && Day != null )
					{
						P_Assignment_Param AssignmentParam = P_Assignment_Param.get( Env.getCtx(), P_Assignment_ID, Day , trxName); // Period.getStartDate()
						salaryAdm = hoursAdmissible.multiply( AssignmentParam.getHourly_Rate());
//						hoursAdmissible = rs.getBigDecimal(1);
					}
				}
				
				// Assurance emploi, les heures de temps suplémentaire ne doivent pas être inclus dans les heures adm.
				else if ( rs.getInt("P_Column_Adm_ID") == 103 && 0 == table.compareTo( "Gain" ))
				{
					hoursAdm  = hoursAdmissible;
					salaryAdm = salaryAdmissible;

					P_Gain_Parameter GainParameter = P_Gain_Parameter.get( Env.getCtx(), p_id, trxName);
					P_GainInfo GainInfo = P_GainInfo.get( Env.getCtx(), "SUP");
					P_Gain_GainInfo GainGainInfo = P_Gain_GainInfo.get( Env.getCtx(), GainParameter.getP_Gain_ID(), GainInfo.getP_GainInfo_ID());
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
				
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		
	}   //  update_matrix

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
	

}
