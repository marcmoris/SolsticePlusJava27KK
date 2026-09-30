/*
 * Created on 28-Feb-2005
 *
 * Calcul Tax of québec
 */
package solstice.process;
  
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Properties;

import org.compiere.model.MRegion;
import org.compiere.util.DB;

import solstice.model.P_Deduction_Param;
import solstice.model.P_Employee;
import solstice.model.P_Form_Template;
import solstice.model.P_Employee_Deduction;
import solstice.model.P_Frequency;
import solstice.model.P_Payment;
import solstice.model.P_Period;
import solstice.model.P_Tax_Provincial_Rate;
import org.compiere.util.CLogger;
import java.util.logging.*;
 
/**
 * @author marcmori
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ImportYTDCalcul_provincial_tax {

	/**	Static Logger				*/
	private static CLogger		log = CLogger.getCLogger (ImportYTDCalcul_provincial_tax.class);

	private static P_Employee Employee;
	private static P_Payment  Payment;
	private static P_Employee_Deduction EmployeeDeduction ;
	private static P_Deduction_Param Deduction_Param ;
	private static P_Frequency Frequency ;
	private static P_Period    Period ;
	private static Hashtable 	taxTable = new Hashtable();
	
	private static BigDecimal Taux_Imp;
	
	private static boolean Exonerated = false;
	private static BigDecimal m_PR = null;
	//
    //	 calcul_quebec_tax
	//
	public static void ImportYTDcalcul_quebec_tax ( Properties ctx, ImportYTD_Employer master, Timestamp EffectIn, String trxName, boolean gratification ) throws Exception
	{
		
		Payment = master.getP_Payment();
		Employee = master.getP_Employee();

		MRegion Region = new MRegion( ctx, Employee.getTaxation_Region_ID(), trxName );
		if ( ! Region.getName().equals("QC") )
			return;

		EmployeeDeduction = master.getP_EmployeeDeduction();
		Deduction_Param = master.getP_Deduction_Param();
		Frequency = master.getP_Frequency();
		Period = master.getP_Period();
		
		taxTable = master.getTaxTable();
		Hashtable s_Matrix   = master.getS_Matrix();   
		Hashtable s_Employee = master.getS_Employee(); 
		Hashtable s_Employer = master.getS_Employer();
		Hashtable s_SalaryAdmissible = master.getS_SalaryAdmissible();

		Hashtable<String, Object> s_TP1015 = new Hashtable<String, Object>();

		BigDecimal employeeAmount   = new BigDecimal( 0 );
		BigDecimal employerAmount   = new BigDecimal( 0 );
	
		readTP1015( ctx, Employee, s_TP1015, trxName );

		
		// impot a retenir a la source pour la période
		BigDecimal a  = new BigDecimal( 0 );
		
		BigDecimal e  = new BigDecimal( 0 );
		BigDecimal e1 = new BigDecimal( 0 );
		BigDecimal e2 = new BigDecimal( 0 );
		
		BigDecimal f  = new BigDecimal( 0 );
		BigDecimal f1 = new BigDecimal( 0 );
		BigDecimal g  = new BigDecimal( 0 );
		BigDecimal g1 = new BigDecimal( 0 );
		BigDecimal i  = new BigDecimal( 0 );
		BigDecimal j  = new BigDecimal( 0 );
		BigDecimal j1 = new BigDecimal( 0 );
		BigDecimal j2 = new BigDecimal( 0 );
		BigDecimal j3 = new BigDecimal( 0 );
		BigDecimal k  = new BigDecimal( 0 );
		BigDecimal k1 = new BigDecimal( 0 );
		BigDecimal k2 = new BigDecimal( 0 );
		
		BigDecimal l = new BigDecimal( 0 );
		// Nombre de période de paie dans l'année
		BigDecimal p  = new BigDecimal( Frequency.getNumberOfPeriod() );
		BigDecimal pr  = p.subtract( new BigDecimal( Period.getPeriodNo()) );
		
		BigDecimal q  = new BigDecimal( 0 );
		BigDecimal t  = new BigDecimal( 0 );
		
		// Impot pour l'année
		BigDecimal y  = new BigDecimal( 0 );
		
		// Salaire Brut assujetti à la retenue d'impot.
		BigDecimal d  = new BigDecimal( 0 );
		BigDecimal h  = new BigDecimal( 0 );
		
		employeeAmount   = new BigDecimal( 0 );
		employerAmount   = new BigDecimal( 0 );
		
		//
		//
		// Recherche des montants admissible /
		// Search admisible amount
		
		BigDecimal hoursAdmissible  = Calcul_tax.get_matrix_hoursAdmissible  ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
		BigDecimal salaryAdmissible = Calcul_tax.get_matrix_salaryAdmissible ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
		
		
//		j 	= new BigDecimal(0); //getTP1015( ctx, Employee, s_TP1015, "19", trxName ); //.divide( p, 2, BigDecimal.ROUND_HALF_UP ); 
		//		j1 	= getTP1016() ;            // ici
		
		j  = getTP1015( ctx, Employee, s_TP1015, "19", trxName );//.divide( p, 2, BigDecimal.ROUND_HALF_UP );

		// retenues sur la paies aux fin des cotisations RPA ou REER ou a une convention de retraite (CR)
		f = Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "024").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "024"));
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "026"));
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "030"));
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "005"));
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "006"));
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "007").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "007")));

		// Salaire brut
		
		if ( gratification )
		{
			g = calculSalaryAdmissibleMethod11(trxName); // Calcul_tax.getDeductionSalaryAdmissibleMethod(s_SalaryAdmissible, "011");
//			BigDecimal z = g.subtract( f );  // Salaire imposable - reer.
//			z = z.multiply( p ); // Salaire imposable annualiser

			g1 = calcul_g1(trxName); // Salaire admissible dans l'année a l'impot gratification.

			g = g.add( g1 ); // + Gratification de l'annéee
			
			i = g.add( salaryAdmissible ); // + gratification courante
		}
		else
		{
			g = salaryAdmissible;
			d = salaryAdmissible;
			
			BigDecimal HRate = getTP1015( ctx, Employee, s_TP1015, "98", trxName );
			BigDecimal HMax = getTP1015( ctx, Employee, s_TP1015, "99", trxName );
			HRate = HRate.divide( new BigDecimal( 100 ) , 8, BigDecimal.ROUND_HALF_UP );
			
			h = d.multiply( HRate );
			if ( h.compareTo( HMax.divide(p,2,BigDecimal.ROUND_HALF_UP)) > 0 )
			{
				h = HMax.divide(p,2,BigDecimal.ROUND_HALF_UP);
			}

			
			// Revenu imposable annuel
			if ( m_PR != null && m_PR.compareTo(new BigDecimal(0)) != 0 )
			{
			
				j = (p.multiply(j)).divide(m_PR,2,BigDecimal.ROUND_HALF_UP);
				j1 = (p.multiply(j1)).divide(m_PR,2,BigDecimal.ROUND_HALF_UP);
				
				i = p.multiply( g.subtract( f ).subtract( h )).subtract( j ).subtract( j1 );
			}
			else
			
				i = p.multiply( g.subtract( f ).subtract( h )).subtract( j ).subtract( j1 );
			
		}
		i = i.setScale(2, BigDecimal.ROUND_HALF_UP);

		// **********
		
		int P_Tax_Provincial_Rate_ID = 0;
		
//		log.debug("quebec_tax - salaryAdmissible i = " + i );
		
		P_Tax_Provincial_Rate_ID = getProvincialRateID( Employee, i, log, trxName );
		
//		log.debug("quebec_tax - P_Tax_Provincial_Rate_ID  = " + P_Tax_Provincial_Rate_ID );
		
		
		P_Tax_Provincial_Rate TaxRate_prov = P_Tax_Provincial_Rate.get (ctx, P_Tax_Provincial_Rate_ID, trxName );
		// **********
		
		// Taux de l'impot provincial ou territorial
		t = TaxRate_prov.getProvincial_Tax_Rate();
//		t = t.divide( new BigDecimal( 100 ) , 8, BigDecimal.ROUND_HALF_UP );
		
		
		Taux_Imp = t;
//		log.debug("provincial_tax - rate  = " + t );
		
		// constant provincial ou territorial
		k = TaxRate_prov.getProvincial_Constant_K();
		
		
		
		//		e1 = new BigDecimal( 9150 ); 
		e1 = getTP1015( ctx, Employee, s_TP1015, "5", trxName );
		if ( e1 == null )
			e1 = new BigDecimal( 0 );
		
//		e2 = getTP1015( ctx, Employee, s_TP1015, "9", trxName );
		e2 = getTP1015( ctx, Employee, s_TP1015, "6", trxName );
		e2 = e2.add( getTP1015( ctx, Employee, s_TP1015, "8", trxName ));
				
		l  = getTP1015( ctx, Employee, s_TP1015, "11", trxName ); 
		
		if ( e2 == null )
			e2 = new BigDecimal( 0 );
		
		// nombre de période de paie qui restent dans l'année    
		pr  = p.subtract( new BigDecimal( Period.getPeriodNo()) );
 		
		
		// Somme retenue pour la période de paie pour l'achat d'action FTQ.
		q = Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "007");
		
		
		// Valeur indexée.
/*		e = e1.multiply(  new BigDecimal(  1 + 0.01427  ) );   
		e = e.add( e2 );
		
		e = e.setScale( 2, BigDecimal.ROUND_HALF_UP );
*/
		e = e1.add( e2 );
		y = t.multiply(i);
		
		
		y = y.subtract( k ).subtract( k1 ).subtract( new BigDecimal(  0.20  ).multiply(e)); 
		y = y.subtract( p.multiply(q).multiply( new BigDecimal( 0.15 ) ) );
		if ( y.compareTo( new BigDecimal(0) ) < 0 )
		{
			y = new BigDecimal( 0 );
		}

		y = y.setScale( 2, BigDecimal.ROUND_HALF_UP );

		a = y.divide( p , 2, BigDecimal.ROUND_HALF_UP );
		// add impot additionnel
		a = a.add( l );
		
//		a = a.add( );
		
		employeeAmount   = a;
/*
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " F " + f );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " G " + g );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " I " + i );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " J " + j );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " J1 " + j1 );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " J2 " + j2 );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " J3 " + j3 );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " K " + k );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " K1 " + k1 );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " K2 " + k2 );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " L " + l );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " P " + p );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " PR " + pr );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " Q " + q );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " T " + t );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " E " + e );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " E1 " + e1 );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " E2 " + e2 );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " Y " + y );
		
		
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " employee amount " + employeeAmount + " employer amount " + employerAmount );
*/	

		if ( Exonerated )
		{
			master.setHoursAdmissible  ( hoursAdmissible); 
			master.setSalaryAdmissible ( salaryAdmissible); 
			master.setEmployeeAmount   ( new BigDecimal(0));
			master.setEmployerAmount   ( new BigDecimal(0)); 
		}
		else
		{
			master.setEmployeeAmount   ( employeeAmount);
			master.setEmployerAmount   ( employerAmount); 
			master.setHoursAdmissible  ( hoursAdmissible); 
			master.setSalaryAdmissible ( salaryAdmissible); 
			
		}
		
	}   // calcul_quebec_tax


	private static BigDecimal calcul_f1( String trxName ) throws SQLException
	{
		BigDecimal f1 = new BigDecimal(0);
		
        String query = null;
		query = "Select isnull( sum( Employee_Part ),0), isnull( sum( case P_Deduction_Param.P_Method_Deduction_ID WHEN '024' THEN Employer_Part WHEN '007' THEN Employer_Part ELSE 0 END ),0)"  
			+ " from P_Payment_Deduction, P_Deduction, P_Deduction_Param, P_Method_Deduction "
			+ " where P_Payment_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID "
			+ " and P_Payment_Deduction.P_Year_ID = " + Period.getP_Year_ID ()
			+ " and P_Payment_Deduction.P_Employee_ID = " + Payment.getP_Employee_ID ()
		    + " and P_Deduction.P_Deduction_ID = P_Deduction_Param.P_Deduction_ID "
		    + " and P_Deduction_Param.EffectIn  in ( Select max( EffectIn ) FROM P_deduction_Param eff where eff.P_Deduction_ID = P_Deduction.P_Deduction_ID ) "
		    + " and P_Deduction_Param.P_Method_Deduction_ID = P_Method_Deduction.P_Method_Deduction_ID " 
		    + " and P_Method_Deduction.value IN ( '024','026','030','005','006','007' )"
		    ;

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			f1 =  rs.getBigDecimal(1).add(rs.getBigDecimal(2)) ;
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;

	   return f1;
	}

	private static BigDecimal calculSalaryAdmissibleMethod11(String trxName ) throws SQLException
	{
		BigDecimal g = new BigDecimal(0);
		
        String query = null;
		query = "Select isnull( sum( SALARY_ELIGIBLE ),0)"  
			+ " from P_Payment_Deduction, P_Deduction, P_Deduction_Param, P_Method_Deduction "
			+ " where P_Payment_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID "
			+ " and P_Payment_Deduction.P_Year_ID = " + Period.getP_Year_ID ()
			+ " and P_Payment_Deduction.P_Employee_ID = " + Payment.getP_Employee_ID ()
		    + " and P_Deduction.P_Deduction_ID = P_Deduction_Param.P_Deduction_ID "
		    + " and P_Deduction_Param.EffectIn  in ( Select max( EffectIn ) FROM P_deduction_Param eff where eff.P_Deduction_ID = P_Deduction.P_Deduction_ID  ) "
		    + " and P_Deduction_Param.P_Method_Deduction_ID = P_Method_Deduction.P_Method_Deduction_ID " 
		    + " and P_Method_Deduction.Value in ( '011' )" // '011',
		    ;

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			g =  rs.getBigDecimal(1);
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;

	   return g;
	}

	private static BigDecimal calcul_g1( String trxName ) throws SQLException
	{
		BigDecimal g1 = new BigDecimal(0);
		
        String query = null;
		query = "Select isnull( sum( SALARY_ELIGIBLE ),0)"  
			+ " from P_Payment_Deduction, P_Deduction, P_Deduction_Param, P_Method_Deduction "
			+ " where P_Payment_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID "
			+ " and P_Payment_Deduction.P_Year_ID = " + Period.getP_Year_ID ()
			+ " and P_Payment_Deduction.P_Employee_ID = " + Payment.getP_Employee_ID ()
		    + " and P_Deduction.P_Deduction_ID = P_Deduction_Param.P_Deduction_ID "
		    + " and P_Deduction_Param.EffectIn  in ( Select max( EffectIn ) FROM P_deduction_Param eff where eff.P_Deduction_ID = P_Deduction.P_Deduction_ID  ) "
		    + " and P_Deduction_Param.P_Method_Deduction_ID = P_Method_Deduction.P_Method_Deduction_ID " 
		    + " and P_Method_Deduction.Value in ( '012' )" // '011',
		    ;

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			g1 =  rs.getBigDecimal(1);
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;

	   return g1;
	}

	public static void ImportYTDcalcul_quebec_retro_tax ( Properties ctx, ImportYTD_Employer master, Timestamp EffectIn, String trxName ) throws Exception
	{

		Payment = master.getP_Payment();
		Employee = master.getP_Employee();
		
		MRegion Region = new MRegion( ctx, Employee.getTaxation_Region_ID(), trxName );
		if ( ! Region.getName().equals("QC") )
			return;
		
		
		EmployeeDeduction = master.getP_EmployeeDeduction();
		Deduction_Param = master.getP_Deduction_Param();
		Frequency = master.getP_Frequency();
		Period = master.getP_Period();
		
		taxTable = master.getTaxTable();
		Hashtable s_Matrix   = master.getS_Matrix();   
		Hashtable s_Employee = master.getS_Employee(); 
		Hashtable s_Employer = master.getS_Employer();

		BigDecimal employeeAmount   = new BigDecimal( 0 );
		BigDecimal employerAmount   = new BigDecimal( 0 );
		BigDecimal hoursAdmissible  = Calcul_tax.get_matrix_hoursAdmissible  ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
		BigDecimal salaryAdmissible = Calcul_tax.get_matrix_salaryAdmissible ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );

		if ( salaryAdmissible.compareTo( new BigDecimal( 0 ) ) == 0)
			return ;

		ImportYTDcalcul_quebec_tax(ctx, master, EffectIn, trxName, true );
	
		BigDecimal t = salaryAdmissible.multiply( Taux_Imp );

		if ( t.compareTo( new BigDecimal( 0 )) < 0 )
		{
			t = new BigDecimal( 0 );
		}

		employeeAmount = t;   
		employerAmount = new BigDecimal( 0 );

		master.setEmployeeAmount   ( employeeAmount);
		master.setEmployerAmount   ( employerAmount); 
		master.setHoursAdmissible  ( hoursAdmissible); 
		master.setSalaryAdmissible ( salaryAdmissible); 
		

	}
	
	/*
	private static BigDecimal calcul_provincial_tax_T1T2(Properties ctx, CalculNet master, Timestamp EffectIn, BigDecimal B, String trxName) throws Exception
	{

		Payment = master.getP_Payment();
		Employee = master.getP_Employee();
		EmployeeDeduction = master.getP_EmployeeDeduction();
		Deduction_Param = master.getP_Deduction_Param();
		Frequency = master.getP_Frequency();
		Period = master.getP_Period();
		
		taxTable = master.getTaxTable();
		Hashtable s_Matrix   = master.getS_Matrix();   
		Hashtable s_Employee = master.getS_Employee(); 
		Hashtable s_Employer = master.getS_Employer();

		Hashtable s_TP1015 = new Hashtable();

		BigDecimal employeeAmount   = new BigDecimal( 0 );
		BigDecimal employerAmount   = new BigDecimal( 0 );
	
		readTP1015( ctx, Employee, s_TP1015, trxName );

		
		// impot a retenir a la source pour la période
		BigDecimal a  = new BigDecimal( 0 );
		
		BigDecimal e  = new BigDecimal( 0 );
		BigDecimal e1 = new BigDecimal( 0 );
		BigDecimal e2 = new BigDecimal( 0 );
		
		BigDecimal f  = new BigDecimal( 0 );
		BigDecimal g  = new BigDecimal( 0 );
		BigDecimal i  = new BigDecimal( 0 );
		BigDecimal j  = new BigDecimal( 0 );
		BigDecimal j1 = new BigDecimal( 0 );
//		BigDecimal j2 = new BigDecimal( 0 );
		BigDecimal j3 = new BigDecimal( 0 );
		BigDecimal k  = new BigDecimal( 0 );
		BigDecimal k1 = new BigDecimal( 0 );
//		BigDecimal k2 = new BigDecimal( 0 );
		
		BigDecimal l = new BigDecimal( 0 );
		// Nombre de période de paie dans l'année
		BigDecimal p  = new BigDecimal( Frequency.getNumberOfPeriod() );
//		BigDecimal pr  = p.subtract( new BigDecimal( Period.getPeriodNo()) );
		
		BigDecimal q  = new BigDecimal( 0 );
		BigDecimal t  = new BigDecimal( 0 );
		
		// Impot pour l'année
		BigDecimal y  = new BigDecimal( 0 );
		
		employeeAmount   = new BigDecimal( 0 );
		employerAmount   = new BigDecimal( 0 );
		
		//
		//
		// Recherche des montants admissible /
		// Search admisible amount
		
		BigDecimal hoursAdmissible  = Calcul_tax.get_matrix_hoursAdmissible  ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
		BigDecimal salaryAdmissible = Calcul_tax.get_matrix_salaryAdmissible ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
		
		//
		// Calcul seulement s'il y a un salaire admissible.
		//
		if ( salaryAdmissible.compareTo( new BigDecimal( 0 ) ) == 0)
			return new BigDecimal( 0 );
		
//		j 	= new BigDecimal(0); //getTP1015( ctx, Employee, taxTable, s_TP1015, "19", trxName ); //.divide( p, 2, BigDecimal.ROUND_HALF_UP ); 
		
		j  = getTP1015( ctx, Employee, s_TP1015, "19", trxName ); //.divide( p, 2, BigDecimal.ROUND_HALF_UP );  
		
		// retenues sur la paies aux fin des cotisations RPA ou REER ou a une convention de retraite (CR)
		f = Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "024").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "024"));
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "026"));
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "030"));
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "005"));
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "006"));
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "007").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "007")));

		// Salaire brut
		g = Payment.getAnnualSalary(); //salaryAdmissible;

//		log.debug("quebec_tax - salaryAdmissible p = " + p );
//		log.debug("quebec_tax - salaryAdmissible g = " + g );
//		log.debug("quebec_tax - salaryAdmissible f = " + f );
//		log.debug("quebec_tax - salaryAdmissible j = " + j );
//		log.debug("quebec_tax - salaryAdmissible j1 = " + j1 );

		BigDecimal b1 = new BigDecimal(0);
		String query = null;
		query = "Select isnull( sum( SALARY_ELIGIBLE ),0)"  
			+ " from P_Payment_Deduction"
			+ " where P_Payment_Deduction.P_Deduction_ID = " + EmployeeDeduction.getP_Deduction_ID ()
			+ " and P_Payment_Deduction.P_Year_ID = " + Period.getP_Year_ID ()
			+ " and P_Payment_Deduction.P_Employee_ID = " + Payment.getP_Employee_ID ();

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			b1 =     rs.getBigDecimal(1);
		}
			
		rs.close ();
		pstmt.close ();
		pstmt = null;
		
		g = g.add( b1) ;	
			
		if( B != null ) g = g.add(B);
		
		// Revenu imposable annuel
		// p.multiply
		i = g.subtract( f ).subtract( j ).subtract( j1 );
		
		i = i.setScale(2, BigDecimal.ROUND_HALF_UP);

		// **********
		
		int P_Tax_Provincial_Rate_ID = 0;
		
//		log.debug("quebec_tax - salaryAdmissible i = " + i );
		
		P_Tax_Provincial_Rate_ID = getProvincialRateID( Employee, i, log, trxName );
		
//		log.debug("quebec_tax - P_Tax_Provincial_Rate_ID  = " + P_Tax_Provincial_Rate_ID );
		
		
		P_Tax_Provincial_Rate TaxRate_prov = P_Tax_Provincial_Rate.get (ctx, P_Tax_Provincial_Rate_ID, trxName );
		// **********
		
		// Taux de l'impot provincial ou territorial
		t = TaxRate_prov.getProvincial_Tax_Rate();
		t = t.divide( new BigDecimal( 100 ) , 8, BigDecimal.ROUND_HALF_UP );
		
//		log.debug("provincial_tax - rate  = " + t );
		
		// constant provincial ou territorial
		k = TaxRate_prov.getProvincial_Constant_K();
		
		
		
		//		e1 = new BigDecimal( 9150 ); 
		e1 = getTP1015( ctx, Employee, s_TP1015, "5", trxName );
		if ( e1 == null )
			e1 = new BigDecimal( 0 );
		
//		e2 = getTP1015( ctx, Employee, taxTable, s_TP1015, "9", trxName );
		e2 = getTP1015( ctx, Employee, s_TP1015, "6", trxName );
		e2 = e2.add( getTP1015( ctx, Employee, s_TP1015, "8", trxName ));
				
		l  = getTP1015( ctx, Employee, s_TP1015, "11", trxName ); 
		
		if ( e2 == null )
			e2 = new BigDecimal( 0 );
		
		// nombre de période de paie qui restent dans l'année    
//		pr  = p.subtract( new BigDecimal( Period.getPeriodNo()) );
 		
		
		// Somme retenue pour la période de paie pour l'achat d'action FTQ.
		q = Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "007");
		
		
		// Valeur indexée.
		e = e1.add( e2 );
		y = t.multiply(i);
		
		
		y = y.subtract( k ).subtract( k1 ).subtract( new BigDecimal(  0.20  ).multiply(e)); 
		y = y.subtract( p.multiply(q).multiply( new BigDecimal( 0.15 ) ) );
		if ( y.compareTo( new BigDecimal(0) ) < 0 )
		{
			y = new BigDecimal( 0 );
		}

		y = y.setScale( 2, BigDecimal.ROUND_HALF_UP );

		a = y.divide( p , 2, BigDecimal.ROUND_HALF_UP );
		// add impot additionnel
		a = a.add( l );

		return a;

	}
	
*/
	// getProvincialRateID
	private static int getProvincialRateID( P_Employee Employee, BigDecimal salary, Logger log, String trxName ) throws Exception
	{
		
		int P_Tax_Provincial_Rate_ID = 0;
		
		BigDecimal sal = salary.abs();
		
		String sqlServerQuery = "Select P_Tax_Provincial_Rate_ID FROM P_Tax_Provincial_Rate, P_Tax, P_Tax_Provincial "
			+ "WHERE P_Tax.P_Tax_ID = P_Tax_Provincial.P_Tax_ID "
			+ "AND P_Tax_Provincial.P_Tax_Provincial_ID = P_Tax_Provincial_Rate.P_Tax_Provincial_ID "
			+ "AND P_Tax.EffectIn = ( Select max( EffectIn ) FROM P_Tax WHERE EffectIn < getdate() AND AD_Client_ID = " + Employee.getAD_Client_ID() + " ) "
			+ "AND " + sal + " >= Limit_Min "
			+ "AND ( " + sal + " < Limit_Max or Limit_Max is null or Limit_Max = 0) "
			+ "AND P_Tax_Provincial.C_Region_ID = " + Employee.getTaxation_Region_ID()
			+ " AND P_Tax_Provincial.AD_client_ID = " + Employee.getAD_Client_ID();

		//Detect on wich server we're connected
		//The default is sql server

        String query = sqlServerQuery;
		
	//	log.debug ("X_P_Tax_Provincial_Rate - " + query );
		
		PreparedStatement pstmt_prov = null;
			pstmt_prov = DB.prepareStatement (query, trxName);
			ResultSet rs_prov = pstmt_prov.executeQuery ();
			while (rs_prov.next ())
			{
				P_Tax_Provincial_Rate_ID =  rs_prov.getInt(1);
			}
			rs_prov.close ();
			pstmt_prov.close ();
			pstmt_prov = null;
			
		
		return P_Tax_Provincial_Rate_ID;
		
	}   	// getProvincialRateID
	

	//
    //	 readTP1015
    //		 
	public static void readTP1015 (Properties ctx,  P_Employee Employee, Hashtable<String, Object> s_TP1015, String trxName ) throws Exception
	{
		s_TP1015.clear();

		MRegion Region = new MRegion( ctx, Employee.getTaxation_Region_ID(), trxName );
		if ( ! Region.getName().equals("QC") )
			return;

		int lineNo;
		BigDecimal amount;
		BigDecimal total = new BigDecimal( 0 );
		
		String s_lineNo = " ";
	

		boolean tp1015 = false;

		
		String query = null;

		query = "Select P_Form_Template_Detail.Value, " 
			  + "P_Employee_Form_Detail.FormFieldAmount, " 
			  + "P_Employee_Form_Detail.FormFieldText, " 
			  + "P_Employee_Form_Detail.FormTypeField " 
			  + " From P_Form_Template_Detail, P_Employee_Form_Detail , P_Employee_Form "
		      + "Where P_Form_Template_Detail.P_Form_Template_Detail_ID = P_Employee_Form_Detail.P_Form_Template_Detail_ID " 
		      + "  AND P_Employee_Form.P_Employee_Form_ID = P_Employee_Form_Detail.P_Employee_Form_ID"
			  + "  AND P_Employee_Form.P_Employee_ID = " + Employee.getP_Employee_ID()
		      + "  AND P_Employee_Form.P_Form_Template_ID = " + P_Form_Template.getForm_Template_ID("TP1015")  
   		  + "  AND P_Employee_Form.EffectIn = ( Select Max( EffectIn ) From P_Employee_Form dat WHERE dat.P_Employee_ID = P_Employee_Form.P_Employee_ID AND dat.P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID AND EffectIn <= " + DB.TO_DATE( Period.getStartDate()) + " )"
			  ;

		PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);

			Exonerated = false;
			m_PR = new BigDecimal(0);
			
			ResultSet rs = pstmt.executeQuery (); 
			while (rs.next ())
			{
				tp1015 = true;
//				lineNo =  rs.getInt(1);
				s_lineNo =  rs.getString(1);
				amount =  rs.getBigDecimal(2);  
				if ( amount != null )
				{
					s_TP1015.put( s_lineNo , amount );
					total = total.add( amount );
				}
				else if ( amount == null && s_lineNo.equals("1"))
				{
					BigDecimal tp = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "TP1015.01");   // Read global Federal Parameter
					s_lineNo = String.valueOf( 1 );
					if ( tp != null )
					{
						s_TP1015.put( s_lineNo ,  tp );
						total = tp;
					}
				}
					
				//
				// La ligne 20 est un champs text qui dit si l'employé ne doit pas payer d'impot.
				//
				if ( s_lineNo.equals("20") && rs.getString("FormFieldText")!= null && rs.getString("FormFieldText").trim().equals("X"))
				{
					Exonerated = true;
				}
				//
				// La ligne PR est un champs text qui dit si l'employé le nombre de période de paie qui reste dans l'année.
				//
				if ( s_lineNo.equals("PR") && rs.getString("FormFieldText") != null )
				{
					m_PR = rs.getBigDecimal("FormFieldText") ;
				}
	
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		


		if ( ! tp1015 )
		{
			BigDecimal tp = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "TP1015.01");   // Read global Federal Parameter
			s_lineNo = String.valueOf( 1 );
			s_TP1015.put( s_lineNo ,  tp );
			total = tp;
		}
		//
		// Grand total du formulaire line 19
		//
		BigDecimal total19 = new BigDecimal(0); 
		total19 = total19.add( getTP1015( ctx, Employee, s_TP1015, "14", trxName )); 
		total19 = total19.add( getTP1015( ctx, Employee, s_TP1015, "15", trxName ));
		total19 = total19.add( getTP1015( ctx, Employee, s_TP1015, "A", trxName ));
		s_lineNo = String.valueOf( 19 );
		s_TP1015.put( s_lineNo , total19 );

		//
		// Sous-Total line 5 du formulaire.
		//
		BigDecimal total5 = new BigDecimal(0); 
		total5 = total5.add( getTP1015( ctx, Employee, s_TP1015, "1", trxName )); 
		total5 = total5.add( getTP1015( ctx, Employee, s_TP1015, "2", trxName ));
		total5 = total5.add( getTP1015( ctx, Employee, s_TP1015, "3", trxName ));
		total5 = total5.add( getTP1015( ctx, Employee, s_TP1015, "4", trxName ));
		s_lineNo = String.valueOf( 5 );
		s_TP1015.put( s_lineNo , total5 );

        // Ajout Stéphane Morin 2007-08-23 Déduction pour emploi utilisé paramètre au lieu de hardcoding
		// il change aux années depuis quelques temps
		BigDecimal HRate = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "HRate");   // Read global Federal Parameter
		s_lineNo = String.valueOf( 98 );
		s_TP1015.put( s_lineNo ,  HRate );
		BigDecimal HMax = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "HMax");   // Read global Federal Parameter
		s_lineNo = String.valueOf( 99 );
		s_TP1015.put( s_lineNo ,  HMax );
			
	    return ;
		
	}	// readTP1015

	//
    //	 getTP1015
    //		  
	public static BigDecimal getTP1015 ( Properties ctx, P_Employee Employee, Hashtable s_TP1015, String lineNo, String trxName  )
	{
		BigDecimal tp = new BigDecimal( 0 );
		
		if ( s_TP1015.containsKey( lineNo ) )
			tp = (BigDecimal)s_TP1015.get( lineNo  );
		else 
			tp = new BigDecimal( 0 );
//		}
		return tp;
	}
	//
 	//  getTP1015
    //		

	
}
