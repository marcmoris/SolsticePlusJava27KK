/*
 * Created on 28-Feb-2005
 *
 * Calcul Federal Tax
 */
package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Properties;

import org.compiere.model.MRole;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Deduction_Param;
import solstice.model.P_Employee;
import solstice.model.P_Employee_Deduction;
import solstice.model.P_Frequency;
import solstice.model.P_Period;
import solstice.model.P_Region;
import solstice.model.P_Tax_Federal_Rate;
import solstice.model.P_Tax_Provincial_Rate;
import solstice.model.P_Time_Sheet_Error;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Payment;
import solstice.model.P_Form_Template;
import java.util.logging.*;



/*********************************************************************************
 * *
 *  Calcul Tax - Canada
 *  @auther : Marc Morissette - Progestion - Solstice 
 * 
 *********************************************************************************/

public class ImportYTDCalcul_federal_tax 
{ 

	/**	Static Logger				*/
	private static CLogger		log = CLogger.getCLogger (ImportYTDCalcul_federal_tax.class);
	/**	Big Decimal 0	 */
	static final public java.math.BigDecimal ZERO = new java.math.BigDecimal(0.0);

	private static P_Employee Employee;
	private static P_Payment  Payment;
	private static P_Employee_Deduction EmployeeDeduction ;
	private static P_Deduction_Param Deduction_Param ;
	private static P_Frequency Frequency ;
	private static P_Period    Period ;
	private static Hashtable 	taxTable = new Hashtable();
	private static BigDecimal m_PR = null;
	private static BigDecimal m_f1 = new BigDecimal( 0 );
	private static boolean Exonerated = false;

	private static Hashtable 	s_TD1prov = new Hashtable();  
	
	private BigDecimal t1 = new BigDecimal( 0 );	  	
	private BigDecimal t2 = new BigDecimal( 0 );
	private BigDecimal t3 = new BigDecimal( 0 );
	private BigDecimal t4 = new BigDecimal( 0 ); 	// Impot provincial ou territotial de base annuel.
	private BigDecimal a   = new BigDecimal( 0 ); 
	private BigDecimal v   = new BigDecimal( 0 );
	private BigDecimal kp  = new BigDecimal( 0 );
	private BigDecimal tcp = new BigDecimal( 0 ); // Montants crédits d'impot personnels non remnoursables
	private BigDecimal k1p = new BigDecimal( 0 ); // Crédits d'impot personnel provincial ou territorial
	private BigDecimal k1pRate = new BigDecimal( 0 );
	private BigDecimal k2p = new BigDecimal( 0 ); // Crédits d'impot provincial ou territorial pour les cotisations au RCP ou RRQ
	private BigDecimal v1  = new BigDecimal( 0 );
	private BigDecimal v2  = new BigDecimal( 0 );
	private BigDecimal s   = new BigDecimal( 0 );
	private BigDecimal a1  = new BigDecimal( 0 );
	private BigDecimal y1   = new BigDecimal( 0 );
	private BigDecimal y2   = new BigDecimal( 0 );
	private BigDecimal y3   = new BigDecimal( 0 );
	private BigDecimal y4   = new BigDecimal( 0 );
	private BigDecimal y5   = new BigDecimal( 0 );
	private BigDecimal YAmount1   = new BigDecimal( 0 );
	private BigDecimal YAmount2   = new BigDecimal( 0 );
	private BigDecimal YAmount3   = new BigDecimal( 0 );
	private BigDecimal YAmount4   = new BigDecimal( 0 );
	private BigDecimal YAmount5   = new BigDecimal( 0 );
	private BigDecimal lcp    = new BigDecimal( 0 );
	private BigDecimal V1Min  = new BigDecimal( 0 );
	private BigDecimal V1Rate = new BigDecimal( 0 );
	private BigDecimal hd     = new BigDecimal( 0 );        // Déduction annuel accordée aux résidents d'une région visée par règlement selon le formulaire TD1
	
	private BigDecimal SMin  = new BigDecimal( 0 );
	private BigDecimal SMax  = new BigDecimal( 0 );
	private BigDecimal SRate  = new BigDecimal( 0 );
	private BigDecimal SAmount  = new BigDecimal( 0 );
	
	// Variable pour le calcul de l'ontario	
	private BigDecimal V1Min1  = new BigDecimal( 0 );
	private BigDecimal V1Min2  = new BigDecimal( 0 );
	private BigDecimal V1Rate1  = new BigDecimal( 0 );
	private BigDecimal V1Rate2  = new BigDecimal( 0 );
	private BigDecimal V2Min1  = new BigDecimal( 0 );
	private BigDecimal V2Min2  = new BigDecimal( 0 );
	private BigDecimal V2Min3  = new BigDecimal( 0 );
	private BigDecimal V2Min4  = new BigDecimal( 0 );
	private BigDecimal V2Min5  = new BigDecimal( 0 );
	private BigDecimal V2Rate1  = new BigDecimal( 0 );
	private BigDecimal V2Rate2  = new BigDecimal( 0 );
	private BigDecimal V2Rate3  = new BigDecimal( 0 );
	private BigDecimal V2Rate4  = new BigDecimal( 0 );
	private BigDecimal V2Rate5  = new BigDecimal( 0 );
	private BigDecimal V2Amount1  = new BigDecimal( 0 );
	private BigDecimal V2Amount2  = new BigDecimal( 0 );
	private BigDecimal V2Amount3  = new BigDecimal( 0 );
	private BigDecimal V2Amount4  = new BigDecimal( 0 );
	private BigDecimal V2Amount5  = new BigDecimal( 0 );
	private BigDecimal Sfix  = new BigDecimal( 0 );
	private BigDecimal SMulti  = new BigDecimal( 0 );

	private BigDecimal lcf = new BigDecimal( 0 );  
	private BigDecimal ft = new BigDecimal( 0 );  

	private BigDecimal employeeAmount   = new BigDecimal( 0 );
	private BigDecimal employerAmount   = new BigDecimal( 0 );

	private P_Region Region;

	private Properties ctx;
//	private CalculNet master;
	private ImportYTD_Employer master;
	private String trxName;
	
//	public Calcul_federal_tax( Properties ctx, CalculNet master, String trxName)
	public ImportYTDCalcul_federal_tax( Properties ctx, ImportYTD_Employer master, String trxName)
	{
		this.ctx = ctx;
		this.master = master;
		this.trxName = trxName;
	}
	
	/*********************************************************************************
	 * 
	 *  Calcul Federal Tax - Canada 
	 * 
	 *********************************************************************************/
	public void ImportYTDcalcul_federal_tax( int taxID,  BigDecimal impos) throws Exception
	{

		/** Transaction			*/
		String m_trxName = trxName;

		Payment = master.getP_Payment();
		Employee = master.getP_Employee();
		EmployeeDeduction = master.getP_EmployeeDeduction();
		Deduction_Param = master.getP_Deduction_Param();
		Frequency = master.getP_Frequency();
		Period = master.getP_Period();

		Region = P_Region.get( ctx, Employee.getTaxation_Region_ID(), trxName);

		taxTable = master.getTaxTable();
		Hashtable<String, QuantityAmountPair> s_Matrix   = master.getS_Matrix();   
		Hashtable<String, BigDecimal> s_Employee = master.getS_Employee(); 
		Hashtable<String, BigDecimal> s_Employer = master.getS_Employer();
		Hashtable<String, BigDecimal> s_TD1=  new Hashtable<String, BigDecimal>();
		Hashtable 	taxTable = master.getTaxTable();

		readTD1( Employee, s_TD1, log );
					
		//
		//
		// Recherche des montants admissible /
		// Search admisible amount
		BigDecimal hoursAdmissible  = Calcul_tax.get_matrix_hoursAdmissible  ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
		BigDecimal salaryAdmissible = Calcul_tax.get_matrix_salaryAdmissible ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );

		// Nombre de période de paie dans l'année
		BigDecimal p = new BigDecimal( Frequency.getNumberOfPeriod() );

		
		// autre crédits d'impot fédéral
		BigDecimal f1 = m_f1;
		if ( m_PR != null && m_PR.compareTo(new BigDecimal(0)) != 0 )
		{
			f1 = p.multiply(f1).divide(m_PR,2,BigDecimal.ROUND_HALF_UP);
		}


		// revenue brut pour la période de paie
		BigDecimal i;
		if ( impos != null)
			i = impos.divide( p,2,BigDecimal.ROUND_HALF_UP);
		else
			i = Calcul_tax.get_matrix_salaryAdmissible ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );

		
		// retenues sur la paies aux fin des cotisations RPA ou REER ou a une convention de retraite (CR)
		BigDecimal f = Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "024").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "024"));
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "026")); //.add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "026")));
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "030"));
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "005")); //.add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "026")));
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "006")); //.add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "026")));
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "007").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "007")));
		// les paiements de pensions alimentaires
		BigDecimal f2 = Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "022");
		
		// Cotisations syndicales
		BigDecimal u1 = Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "008");
		
		// Revenu imposable annuel
		// a = (p * ( i - f - f2 -u1 )) -hd -f1;
		a = p.multiply( i.subtract( f).subtract( f2).subtract( u1 ) ).subtract(hd).subtract(f1);


		// Autre déduction autoriser.
		if ( getTD1( s_TD1, "A" )!= null ) 
			a = a.subtract( getTD1( s_TD1, "A" ));

		P_Tax_Federal_Rate TaxRate = get_federal_rate( ctx, taxID, a, trxName);
		
		// Taux de l'impot fédéral
		BigDecimal r = TaxRate.getFederal_Tax_Rate();
		
		// constante fédérale
		BigDecimal k = TaxRate.getFederal_Constant_K();
		
		//	Log.trace( 5,"federal_tax - rate  = " + r );
	//	Log.trace( 5,"federal_tax - constant K  = " + k );
/*
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + "  ------- " );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " a = (p * ( i - f - f2 -u1 )) -hd -f1 " );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " p " + p );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " i " + i );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " f " + f );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " f2 " + f2 );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " u1 " + u1 );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " hd " + hd );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " f1 " + f1 );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " a = " + a );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + "  ------- " );
*/
		
		// crédit d'impot personnel de base	  	
		BigDecimal k1 = new BigDecimal( 0 );
		
		if ( s_TD1.containsKey( "1" )) //taxTable.containsKey( "TD1.01" ) 
		{
			// tc Total of TD1 Formular.
//			BigDecimal tc = (BigDecimal)taxTable.get( "TD1.01");
			BigDecimal tc = getTD1( s_TD1, "1" );			
			tc = tc.add( getTD1( s_TD1, "2" ).add( getTD1( s_TD1,"3" )).add( getTD1( s_TD1,"4" )).add( getTD1( s_TD1,"5" )).add( getTD1( s_TD1,"6" )).add( getTD1( s_TD1,"7" )).add( getTD1( s_TD1,"8" )).add( getTD1( s_TD1,"9" )).add( getTD1( s_TD1,"10" )).add( getTD1( s_TD1,"11" ) ));


			BigDecimal k1rate = (BigDecimal)taxTable.get( "K1"); // 16 %

			k1 = k1rate.multiply( tc ).setScale( 2, BigDecimal.ROUND_HALF_UP) ;  // aller lire le formulaire TD1
		}
		else
		{
			k1 = new BigDecimal( 0 );
			
		}


		// Crédits d'impot fédéral annuels pour les cotisations au RPC / RRQ
		BigDecimal kRate = (BigDecimal)taxTable.get( "K1"); // 16 %

		
		//
		BigDecimal ae = master.getAmount_Employment_Insurance(); //  Calcul_AE_For_tax( salaryAdmissible ); 
		// cotisation au RPC ou RRQ
		BigDecimal c = master.getAmount_Rpc(); // Calcul_RPC_RRQ_For_Tax( true, salaryAdmissible );
		
		// Ajout Stéphane 2007-08-23
		// Pour le québec on doit avoir les montant de RQAP pour calculer K2
		BigDecimal k2RQAP = Env.ZERO;
		if ( Region.getName().trim().equals(  "Québec" ) || Region.getName().trim().equals("QC") )  
		{
			k2RQAP = master.getAmount_RQAP(); 
		}
		// .16%(p*c) + .16%(p*ae)
		BigDecimal k2 = kRate.multiply( c ).add( kRate.multiply( ae ).add(kRate.multiply(k2RQAP)) );

		// .16%(p*c) + .16%(p*ae)
		// BigDecimal k2 = kRate.multiply(  c ).add( kRate.multiply( ae ) );

		k2 = k2.setScale(2, BigDecimal.ROUND_HALF_UP );

		BigDecimal k3 = Env.ZERO;
		
		// Crédit d’impôt à l’emploi au Canada
		BigDecimal k4tmp = (BigDecimal)taxTable.get( "K4"); // 1000$
		//Le moindre des deux montants suivants :
		//	(i) 0,155 × A;
		//	(ii) 0,155 × 1000 $
				
		BigDecimal k4 = kRate.multiply( a );
		if ( k4.compareTo( kRate.multiply( k4tmp ) ) > 0)
			k4 = kRate.multiply( k4tmp );
			
			
		// Impot fédéral de base annuel
		//  t3 = ( r x a ) -k -k1 -k2 -k3 -k4
		t3 = r.multiply( a );
		t3 = t3.subtract( k ).subtract( k1 ).subtract( k2 ).subtract( k3 ).subtract( k4 );
		
		if ( t3.compareTo( new BigDecimal(0) )  < 0 ) 
		{
			t3 = new BigDecimal( 0 );	  	
		}

		t3 = t3.setScale(2, BigDecimal.ROUND_HALF_UP );
/*		
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + "  ------- " );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " T3 = r(a) - k -k1 -k2 -k3 = "  );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " A " + a );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " R " + r );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " K  " + k );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " K1 " + k1 );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " K2 " + k2 );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " K3 " + k3 );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " T3 " + t3 );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + "  ------- " );
*/
		// Crédit d'impot fédéral
		

		// Crédit d'impot fédéral relatif à un fonds de travailleurs
		ft = Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "007");
		ft = ft.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "023"));

		ft = ft.multiply( p );
		
		BigDecimal lcfMax = (BigDecimal)taxTable.get( "LCF");  
		BigDecimal lcfRate = (BigDecimal)taxTable.get( "LCF.Rate");
		lcfRate = lcfRate.divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);
		lcf = lcfRate.multiply( ft );
		
		if ( lcfMax.compareTo( lcf ) < 0 )
			lcf = lcfMax;

		lcf = lcf.setScale(2, BigDecimal.ROUND_HALF_UP );


		//
		// Calcul Impot Provincial 
		//
		//
		if ( Region.getName().trim().equals(  "Québec" ) || Region.getName().trim().equals("QC") )  
		{
			BigDecimal lcfRateQC = (BigDecimal)taxTable.get( "LCF.RateQC");  
			lcfRateQC = lcfRateQC.divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);
			t1 = t3.subtract( lcf );	
			// Retenues d'impot fédéral annuel.
//			t1 = t3.subtract( lcf ).setScale(2,BigDecimal.ROUND_HALF_UP);
			if ( t1.compareTo( new BigDecimal(0) )  < 0 ) 
			{
				t1 = new BigDecimal( 0 );	  	
			}
			
			t1 = t1.subtract( (lcfRateQC.multiply( t3)) ).setScale(2,BigDecimal.ROUND_HALF_UP) ;
			if ( t1.compareTo( new BigDecimal(0) )  < 0 ) 
			{
				t1 = new BigDecimal( 0 );	  	
			}
			
			t2 = new BigDecimal( 0 );

			t1 = t1.setScale(2, BigDecimal.ROUND_HALF_UP);
			
		}
		else if ( Region.getName().trim().equals("ZZ") )
		{
			BigDecimal lcfRateZZ = (BigDecimal)taxTable.get( "LCF.RateZZ");  
			lcfRateZZ = lcfRateZZ.divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP);
			t1 = t3.add( lcfRateZZ.multiply(t3)).subtract(lcf);
			t1 = t1.setScale(2, BigDecimal.ROUND_HALF_UP);
			t2 = new BigDecimal( 0 );
		  
		}
		else
			t1 = t3.subtract( lcf );	

		// AJOUT STEPHANE IMPORT YTD
		if ( Region.getName().trim().equals(  "Québec" ) || Region.getName().trim().equals("QC") )  
		{
			t2 = new BigDecimal( 0 );
		}
		else
			calcul_Provincial( taxID );
		
		//
		// l = Retenues d'impot supplémentaires pour la périodes de paie demandées par l'employe
		//
		BigDecimal l = getTD1( s_TD1, "L" );
		
		//
		// t = [ ( t1 + t2 ) / p ] + l
		BigDecimal t = t1.add( t2 );
		t = t.divide( p , 2, BigDecimal.ROUND_HALF_UP );

		employeeAmount = t.add( l );
		employerAmount = new BigDecimal( 0 );

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


		return;
	}

	/*********************************************************************************
	 * 
	 *  Calcul Provincial Tax - Canada
	 * 
	 * 
	 *********************************************************************************/
	public void calcul_Provincial( int taxID ) throws Exception
	{

		int P_Tax_Provincial_Rate_ID = 0;
		
		// **********
		P_Tax_Provincial_Rate_ID = getProvincialRateID( taxID, Employee.getTaxation_Region_ID(), a, log );
		
		P_Tax_Provincial_Rate TaxRate_prov = P_Tax_Provincial_Rate.get (ctx, P_Tax_Provincial_Rate_ID, trxName );
		// **********
		
		// Taux de l'impot provincial ou territorial
		v = TaxRate_prov.getProvincial_Tax_Rate();
		
//		Log.trace( 5,"provincial_tax - rate  = " + v );
		
		// constant provincial ou territorial
		kp = TaxRate_prov.getProvincial_Constant_K();
		
		readTD1prov( Employee, s_TD1prov, Region.getName(),log );
				
		k1pRate = getFirstRate( ctx, taxID, Payment, Employee , trxName );
		
		//********************
		// Toutes les provinces sauf Saskatchewan ont 11 lignes au 1 juillet 2007
		//  Le tcp est relu dans CALCUL_SK
		if ( s_TD1prov.containsKey( "1" )) //taxTable.containsKey( "TD1.01" ) 
		{
			// tc Total of TD1 Formular.
			tcp = getTD1( s_TD1prov, "1" );
			tcp = tcp.add( getTD1( s_TD1prov,"2" ));
			tcp = tcp.add( getTD1( s_TD1prov,"3" ));
			tcp = tcp.add( getTD1( s_TD1prov,"4" ));
			tcp = tcp.add( getTD1( s_TD1prov,"5" ));
			tcp = tcp.add( getTD1( s_TD1prov,"6" ));
			tcp = tcp.add( getTD1( s_TD1prov,"7" ));
			tcp = tcp.add( getTD1( s_TD1prov,"8" ));
			tcp = tcp.add( getTD1( s_TD1prov,"9" ));
			tcp = tcp.add( getTD1( s_TD1prov,"10" ));
			tcp = tcp.add( getTD1( s_TD1prov,"11" ));
		}
		else
		{
			tcp = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "TCP");
			
		}

		// Crédits d'impot personnel provincial ou territorial
		k1p = tcp.multiply( k1pRate );
		
//		lcp = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP");
//			lcpRate = (BigDecimal)taxTable.get( Region.getID() + "-" + "LCP.Rate");

		V1Min = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V1Min");
		V1Rate = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V1Rate");
		
		// Crédits d'impot provincial ou territorial pour les cotisations au RCP ou RRQ
		//
		// [( 0.1057 * ( p * c, max 1831.50 )) + ( 0,1057 * ( p * ae, max 772.20 ))]
		//
		
		//
		BigDecimal ae = master.getAmount_Employment_Insurance(); //  Calcul_AE_For_tax( salaryAdmissible ); 
		
		// cotisation au RPC ou RRQ
		BigDecimal c = master.getAmount_Rpc(); // Calcul_RPC_RRQ_For_Tax( true, salaryAdmissible );

		k2p = c.multiply( k1pRate ).add( ae.multiply( k1pRate ) ) ;
	
	
		v1  = new BigDecimal( 0 );
		s = new BigDecimal( 0 );
		
		//
		// "Newfoundland, Labrador"
		//
		if ( Region.getName().equals(  "Newfoundland, Labrador" ) || Region.getName().trim().equals("NL")  )
		{
			calcul_NL();
		}

		//
		// "Nova Scotia"
		//
		if ( Region.getName().equals(  "Nova Scotia" ) || Region.getName().trim().equals("NS") ) 
		{
			calcul_NS();
		}

		// Prince Edward Island
		if ( Region.getName().equals(  "Prince Edward Island" ) || Region.getName().trim().equals("PE") ) 
		{
			calcul_PE();
		}
		// Prince Edward Island
		
		//New Brunswick                                       
		if ( Region.getName().equals(  "New Brunswick" ) || Region.getName().trim().equals("NB") ) 
		{
			calcul_NB();
		}
		
		//
		// Ontario
		//
		if ( Region.getName().equals(  "Ontario" ) || Region.getName().trim().equals("ON")) 
		{
			calcul_ON();
		} 
		
		// Manitoba
		if ( Region.getName().equals(  "Manitoba" ) || Region.getName().trim().equals("MB") ) 
		{
			calcul_MB();
		}
		
		// Alberta
		if ( Region.getName().equals(  "Alberta" ) || Region.getName().trim().equals("AB") ) 
		{
			calcul_AB();
		}
		
		
		// Saskatchewan
		if ( Region.getName().equals(  "Saskatchewan" ) || Region.getName().trim().equals("SK")) 
		{
			calcul_SK();
		}
		
		
		// British Columbia
		if ( Region.getName().equals(  "British Columbia" ) || Region.getName().trim().equals("BC") ) 
		{
			calcul_BC();
		}
		
		
		// Yukon
		if ( Region.getName().equals(  "Yukon" ) || Region.getName().trim().equals("YT") ) 
		{
			calcul_YT();
		}
		
		// Northwest Territories
		if ( Region.getName().equals(  "Northwest Territories" ) || Region.getName().trim().equals("NT")) 
		{
			calcul_NT();
		}
		
		// Nunavut
		if ( Region.getName().equals(  "Nunavut" ) || Region.getName().trim().equals("NU")) 
		{
			calcul_NU();
		}

		t2 = t4.add( v1 ).add( v2 ).subtract( s ).subtract( lcp );
	
		
		if ( t2.compareTo( new BigDecimal(0) ) < 0 )
		{
			t2 = new BigDecimal( 0 );
		}

/*
 * 
 *   STEPHANE TEST IMPORT YTD
 * 
		//
		// t = [ ( t1 + t2 ) / p ] + l
		// Nombre de période de paie dans l'année
		BigDecimal p = new BigDecimal( Frequency.getNumberOfPeriod() );

		BigDecimal t = t2 ;
		t = t.divide( p , 2, BigDecimal.ROUND_HALF_UP );

		employeeAmount = t;
		employerAmount = new BigDecimal( 0 );

		// Recherche des montants admissible /
		// Search admisible amount
		Hashtable<String, QuantityAmountPair> s_Matrix   = master.getS_Matrix();   
		BigDecimal hoursAdmissible  = Calcul_tax.get_matrix_hoursAdmissible  ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
		BigDecimal salaryAdmissible = Calcul_tax.get_matrix_salaryAdmissible ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );

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

 *
 * 
 *   STEPHANE TEST IMPORT YTD
 */ 

	}

	// "Newfoundland, Labrador"
	private void calcul_NL()
	{
		// Autre crédit d'impot provincial ou territorial
		BigDecimal k3p = new BigDecimal( 0 );
		
		// Impot provincial ou territotial de base annuel.
		t4 = v.multiply( a );
		t4 = t4.subtract( kp);
		t4 = t4.subtract( k1p );
		t4 = t4.subtract( k2p );
		t4 = t4.subtract( k3p ) ;

        if (Period.getName().compareTo("2007-13") > 0)
    	{
          v1 = new BigDecimal( 0 );
    	}
        else
		{
    		if ( t4.compareTo ( V1Min ) <= 0 ) // 10000
    			v1 = new BigDecimal( 0 );
    		
    		if ( t4.compareTo ( V1Min ) > 0  ) // 10000
    		{
    			v1 = V1Rate;
    			v1 = v1.multiply( t4.subtract( V1Min ) ); // 10000
    		}
		}

		s = new BigDecimal( 0 );
		
		// LCP  2000 ou 15 %... 
		BigDecimal LCPmax = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP");
		BigDecimal LCPRate = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP.Rate");

		lcp = ( LCPRate.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP) .multiply( ft ));
		
		if ( LCPmax.compareTo( lcp ) < 0 )
			lcp = LCPmax;

		lcp = lcp.setScale(2, BigDecimal.ROUND_HALF_UP );

	}

	
	//
	// "Nova Scotia"
	//
	private void calcul_NS()
	{
		// Autre crédit d'impot provincial ou territorial
		BigDecimal k3p = new BigDecimal( 0 );
		
		// Impot provincial ou territotial de base annuel.
		t4 = v.multiply( a );
		t4 = t4.subtract( kp);
		t4 = t4.subtract( k1p );
		t4 = t4.subtract( k2p );
		t4 = t4.subtract( k3p ) ;
		
		if ( t4.compareTo ( V1Min ) <= 0 ) // 10000
			v1 = new BigDecimal( 0 );
		
		if ( t4.compareTo ( V1Min ) > 0  ) // 10000
		{
			v1 = V1Rate;
			v1 = v1.multiply( t4.subtract( V1Min ) ); // 10000
		}
		
		s = new BigDecimal( 0 );
		
		// LCP 750 ou 15%
		BigDecimal LCPmax = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP");
		BigDecimal LCPRate = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP.Rate");

		lcp = ( LCPRate.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP) .multiply( ft ));
		
		if ( LCPmax.compareTo( lcp ) < 0 )
			lcp = LCPmax;

		lcp = lcp.setScale(2, BigDecimal.ROUND_HALF_UP );

	}
	
	
	// Prince Edward Island
	private void calcul_PE()
	{
		
		// Autre crédit d'impot provincial ou territorial
		BigDecimal k3p = new BigDecimal( 0 );
		
		// Impot provincial ou territotial de base annuel.
		t4 = v.multiply( a );
		t4 = t4.subtract( kp);
		t4 = t4.subtract( k1p );
		t4 = t4.subtract( k2p );
		t4 = t4.subtract( k3p ) ;
		
		if ( t4.compareTo ( V1Min ) <= 0 ) // 10000
			v1 = new BigDecimal( 0 );
		
		if ( t4.compareTo ( V1Min ) > 0  ) // 10000
		{
			v1 = V1Rate;
			v1 = v1.multiply( t4.subtract( V1Min ) ); // 10000
		}
		
		s = new BigDecimal( 0 );
		lcp = new BigDecimal( 0 );

	}
	
	//New Brunswick                                       
	private void calcul_NB()
	{
		// Autre crédit d'impot provincial ou territorial
		BigDecimal k3p = new BigDecimal( 0 );
		
		// Impot provincial ou territotial de base annuel.
		t4 = v.multiply( a );
		t4 = t4.subtract( kp);
		t4 = t4.subtract( k1p );
		t4 = t4.subtract( k2p );
		t4 = t4.subtract( k3p ) ;
		
		s = new BigDecimal( 0 );
		v1 = new BigDecimal( 0 );
		
		// LCP  750 ou 15 %... 
		BigDecimal LCPmax = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP");
		BigDecimal LCPRate = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP.Rate");

		lcp = ( LCPRate.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP) .multiply( ft ));
		
		if ( LCPmax.compareTo( lcp ) < 0 )
			lcp = LCPmax;

		lcp = lcp.setScale(2, BigDecimal.ROUND_HALF_UP );
	}
	
	//
	// Ontario
	//
	private void calcul_ON()
	{
		// Autre crédit d'impot provincial ou territorial
		BigDecimal k3p = new BigDecimal( 0 );
		BigDecimal v2t = new BigDecimal( 0 );
		BigDecimal st = new BigDecimal( 0 );
			
		// Impot provincial ou territotial de base annuel.
		t4 = v.multiply( a );
		t4 = t4.subtract( kp);
		t4 = t4.subtract( k1p );
		t4 = t4.subtract( k2p );
		t4 = t4.subtract( k3p ) ;
/*
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + "  ------- " );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " t4 ( v * a ) - kp -k1p -k2p -kp3 = " + t4 );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " v " + v );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " kp " + kp );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " k1p " + k1p );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " k2p " + k2p );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " k3p " + k3p );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " k1pRate " + k1pRate );
		Log.trace( 5,EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + "  ------- " );
*/
						
		V1Min1 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V1Min.1");
		V1Min2 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V1Min.2");
		V1Rate1 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V1Rate.1");
		V1Rate1 = V1Rate1.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
		V1Rate2 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V1Rate.2");
//		V1Rate2 = ((BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V1Rate.2")).divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
		V1Rate2 = V1Rate2.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
		
		if ( t4.compareTo( V1Min1 ) <= 0)
			v1 = new BigDecimal( 0 );
		if ( t4.compareTo(  V1Min1 ) > 0 &&  t4.compareTo(  V1Min2 ) <= 0)
			v1 =  V1Rate1.multiply( t4.subtract( V1Min1 ));
		if ( t4.compareTo( V1Min2 ) > 0 )
		{
			v1 = V1Rate1.multiply( t4.subtract( V1Min1 ));
			v1 = v1.add(V1Rate2.multiply( t4.subtract( V1Min2 )));
		}
	
		V2Min1 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V2Min.1");
		V2Min2 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V2Min.2");
		V2Min3 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V2Min.3");
		V2Min4 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V2Min.4");
		V2Min5 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V2Min.5");
		V2Rate1 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V2Rate.1");
		V2Rate1 = V2Rate1.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
		V2Rate2 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V2Rate.2");
		V2Rate2 = V2Rate2.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
		V2Rate3 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V2Rate.3");
		V2Rate3 = V2Rate3.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
		V2Rate4 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V2Rate.4");
		V2Rate4 = V2Rate4.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
		V2Rate5 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V2Rate.5");
		V2Rate5 = V2Rate5.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
		V2Amount1 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V2Amount.1");
		V2Amount2 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V2Amount.2");
		V2Amount3 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V2Amount.3");
		V2Amount4 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V2Amount.4");
		V2Amount5 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "V2Amount.5");
		Sfix = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "Sfix");
		SMulti = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "Smulti");
		
		// A <= 20000
		if ( a.compareTo( V2Min1) <= 0)
			v2 = new BigDecimal( 0 );
		
		// A > 20000 et A <= 36000
		if ( a.compareTo( V2Min1 ) > 0 &&  a.compareTo( V2Min2) <= 0)
		{
			// le moindre de : 300 ou (0.06 * ( A - 20000 ))
			v2 = V2Amount1;
			v2t = a.subtract( V2Min1 );
			v2t = v2t.multiply(V2Rate1);
			if ( v2.compareTo( v2t ) < 0 )
				v2 = v2t;
		}

		// A > 36000 et A <= 48000
		if ( a.compareTo( V2Min2) > 0 &&  a.compareTo( V2Min3 ) <= 0)
		{
			// le moindre de : 450 ou 300 + (0.25 * ( A - 20000 ))
			v2 = V2Amount2 ;
			v2t = a.subtract( V2Min2 );
			v2t = v2t.multiply(V2Rate2);
			v2t = V2Amount1.add(v2t);
			if ( v2.compareTo(  v2t) > 0 )
				v2 = v2t ;
			
		}

		// A > 48000 et A <= 72000
		if ( a.compareTo( V2Min3) > 0 &&  a.compareTo( V2Min4) <= 0)
		{
			// le moindre de : 600 ou 450 + (0.25 * ( A - 48000 ))
			v2 = V2Amount3;
			v2t = a.subtract( V2Min3 );
			v2t = v2t.multiply(V2Rate5);
			v2t = V2Amount2.add(v2t);
			if ( v2.compareTo( v2t) > 0 )
				v2 = v2t;
			
		}
		// A > 72000 et A <= 200000
		if ( a.compareTo( V2Min4) > 0 &&  a.compareTo( V2Min5) <= 0)
		{
			// le moindre de : 750 ou 600 + (0.25 * ( A - 72000 ))
			v2 = V2Amount4;
			v2t = a.subtract( V2Min4 );
			v2t = v2t.multiply(V2Rate5);
			v2t = V2Amount3.add(v2t);
			if ( v2.compareTo(v2t) > 0 )
				v2 = v2t;
			
		}

		// A > 200000
		if ( a.compareTo( V2Min5) > 0)
		{
			// le moindre de : 900 ou 750 + (0.25 * ( A - 200000 ))
			v2 = V2Amount5;
			v2t = a.subtract( V2Min5 );
			v2t = v2t.multiply(V2Rate5);
			v2t = V2Amount4.add(v2t);
			if ( v2.compareTo(v2t) > 0 )
				v2 = v2t;
		}


		// le moindre des 2 montants suivant
		// i) t4 + v4
		// ii) [ 2 * ( 186 + y ) ] - [t4 + v1]
		//
		// Variable pour ontario
		// indique le nombre de personnes à charges selon leurs TD1 provinciaux
		if ( getTD1( s_TD1prov, "Y1" )!= null ) 
		   y1 = ( getTD1( s_TD1prov, "Y1" ));
		
		if ( getTD1( s_TD1prov, "Y2" )!= null ) 
			y2 = ( getTD1( s_TD1prov, "Y2" ));
		
		y1 = y1.multiply (SMulti);
        y2 = y2.multiply (SMulti);
	
		s   = t4.add( v1 );
		st = y1.add( Sfix ).add(y2);
		st = st.multiply( new BigDecimal(2));
		st = st.subtract(  t4.add( v1 ));
		if ( s.compareTo( st ) > 0 )
			s = st;
		
		if ( s.compareTo( new BigDecimal(0) ) < 0 )
			s = new BigDecimal( 0 );
		
		// LCP  750 ou 15 %... 
		BigDecimal LCPmax = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP");
		BigDecimal LCPRate = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP.Rate");

		lcp = ( LCPRate.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP) .multiply( ft ));
		
		if ( LCPmax.compareTo( lcp ) < 0 )
			lcp = LCPmax;

		lcp = lcp.setScale(2, BigDecimal.ROUND_HALF_UP );

	} 
	
	// Manitoba
	private void calcul_MB()
	{
		YAmount1 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "YAmount1");
		YAmount2 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "YAmount2");
		YAmount3 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "YAmount3");
		YAmount4 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "YAmount4");
		YAmount5 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "YAmount5");
		Sfix = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "Sfix");
		SRate = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "SRate");
		SRate = SRate.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
		
		if ( (getTD1( s_TD1prov, "6" )).compareTo(new BigDecimal(0)) > 0) 
			y1 = YAmount1;
		
		if ( (getTD1( s_TD1prov, "2" )).compareTo( new BigDecimal(0)) > 0 ) 
			y2 = YAmount2;
		
		if ( (getTD1( s_TD1prov, "Y3" )).compareTo( new BigDecimal(0)) > 0 ) 
			y3 = YAmount3.multiply(( getTD1( s_TD1prov, "Y3" )));

		if ( (getTD1( s_TD1prov, "5" )).compareTo( new BigDecimal(0)) > 0 ) 
			y4 = YAmount4;

		if ( (getTD1( s_TD1prov, "Y5" )).compareTo( new BigDecimal(0)) > 0 ) 
			y5 = YAmount5.multiply(( getTD1( s_TD1prov, "Y5" )));
		
		// Autre crédit d'impot provincial ou territorial
		BigDecimal k3p = new BigDecimal( 0 );
		
		// Impot provincial ou territotial de base annuel.
		t4 = v.multiply( a );
		t4 = t4.subtract(kp);
		t4 = t4.subtract(k1p);
		t4 = t4.subtract(k2p);
		t4 = t4.subtract(k3p) ;
		
		a1 = a.add( hd );
		
		BigDecimal ts = y1.add(y2.add(y3.add(y4.add(y5)))) ;
		ts = ts.add(Sfix);
		ts = ts.subtract((a1.multiply(SRate)));
		
		if ( ts.compareTo(t4) < 0 )
			s = ts;
		else
			s = t4;

		if ( s.compareTo(ZERO) < 0)
			s = ZERO;
		
		v1 = new BigDecimal( 0 );
		
		// LCP  750 ou 15 %... 
		BigDecimal LCPmax = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP");
		BigDecimal LCPRate = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP.Rate");

		lcp = ( LCPRate.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP) .multiply( ft ));
		
		if ( LCPmax.compareTo( lcp ) < 0 )
			lcp = LCPmax;

		lcp = lcp.setScale(2, BigDecimal.ROUND_HALF_UP );

	}
	
	// Alberta
	private void calcul_AB()
	{
		// Autre crédit d'impot provincial ou territorial
		BigDecimal k3p = new BigDecimal( 0 );				
		
		// Impot provincial ou territotial de base annuel.
		//t4 = v.multiply( a ).subtract( kp.subtract( k1p ).subtract( k2p ).subtract( k3p ) );
		t4 = v.multiply( a );
		t4 = t4.subtract( kp);
		t4 = t4.subtract( k1p );
		t4 = t4.subtract( k2p );
		t4 = t4.subtract( k3p ) ;
		
		v1 = new BigDecimal( 0 );
		s = new BigDecimal( 0 );
		lcp = new BigDecimal( 0 );
	}
	
	
	// Saskatchewan
	private void calcul_SK()
	{
		//  Le tcp est relu dans CALCUL_SK
		if ( s_TD1prov.containsKey( "1" )) //taxTable.containsKey( "TD1.01" ) 
		{
			// tc Total of TD1 Formular.
			tcp = getTD1( s_TD1prov, "1" );
			tcp = tcp.add( getTD1( s_TD1prov,"2" ));
			tcp = tcp.add( getTD1( s_TD1prov,"2a" ));
			tcp = tcp.add( getTD1( s_TD1prov,"3" ));
			tcp = tcp.add( getTD1( s_TD1prov,"4" ));
			tcp = tcp.add( getTD1( s_TD1prov,"5" ));
			tcp = tcp.add( getTD1( s_TD1prov,"6" ));
			tcp = tcp.add( getTD1( s_TD1prov,"7" ));
			tcp = tcp.add( getTD1( s_TD1prov,"8" ));
			tcp = tcp.add( getTD1( s_TD1prov,"9" ));
			tcp = tcp.add( getTD1( s_TD1prov,"10" ));
			tcp = tcp.add( getTD1( s_TD1prov,"11" ));
			tcp = tcp.add( getTD1( s_TD1prov,"11a" ));
		}
		else
		{
			tcp = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "TCP");
			
		}

		// Crédits d'impot personnel provincial ou territorial
		k1p = tcp.multiply( k1pRate );
		
		// Autre crédit d'impot provincial ou territorial
		BigDecimal k3p = new BigDecimal( 0 );
		
		// Impot provincial ou territotial de base annuel.
		t4 = v.multiply( a );
		t4 = t4.subtract( kp);
		t4 = t4.subtract( k1p );
		t4 = t4.subtract( k2p );
		t4 = t4.subtract( k3p ) ;
		
		s = new BigDecimal( 0 );
		v1 = new BigDecimal( 0 );
		
		// LCP  1000 ou 20 %... 
		BigDecimal LCPmax = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP");
		
		
		BigDecimal LCPmax1 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP.1");
		BigDecimal LCPRate1 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP.Rate");

		BigDecimal lcpa = ( LCPRate1.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP) .multiply( ft ));
		if ( LCPmax1.compareTo( lcpa  ) < 0 )			
			lcpa = LCPmax1;

		
		BigDecimal LCPmax2 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP.2");
		BigDecimal LCPRate2 = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP.Rate2");

		BigDecimal lcpb = ( LCPRate2.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP) .multiply( ft ));
		if ( LCPmax2.compareTo( lcpb  ) < 0 )			
			lcpb = LCPmax2;
		
		lcp = lcpa.add(lcpb);
		
		if ( LCPmax.compareTo( lcp  ) < 0 )			
			lcp = LCPmax;
		
		lcp = lcp.setScale(2, BigDecimal.ROUND_HALF_UP );
	}
	// Saskatchewan
	
	
	// British Columbia
	private void calcul_BC()
	{
		// Autre crédit d'impot provincial ou territorial
		BigDecimal k3p = new BigDecimal( 0 );
		
		a1 = a.add( hd );
		
		// Impot provincial ou territotial de base annuel.
		t4 = v.multiply( a );
		t4 = t4.subtract( kp);
		t4 = t4.subtract( k1p );
		t4 = t4.subtract( k2p );
		t4 = t4.subtract( k3p ) ;
		
		v1  = new BigDecimal( 0 );
		
		SMin = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "SMin");
		SMax = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "SMax");
		SRate = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "SRate");
		SAmount = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "SAmount");
		
		if (a1.compareTo (SMin) <= 0 )
		{
			if (t4.compareTo(SAmount)<= 0 )			
				s = t4;
			else
				s = SAmount;
		}
		
		if (a1.compareTo (SMin) > 0)
			if (a1.compareTo (SMax) <= 0)
			{
				BigDecimal Ttemp = new BigDecimal (0);
				Ttemp = SAmount.subtract(a1.subtract(SMin).multiply(SRate));
				
			  	if (t4.compareTo(Ttemp) <= 0)
			  		s = t4 ;
			  	else
			  		s = Ttemp;			  	   		
			}
		
		if (a1.compareTo(SMax) > 0)
			s = new BigDecimal(0);
		
		// LCP  2000 ou 15 %... 
		BigDecimal LCPmax = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP");
		BigDecimal LCPRate = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP.Rate");

		lcp = ( LCPRate.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP) .multiply( ft ));
		
		if ( LCPmax.compareTo( lcp ) < 0 )
			lcp = LCPmax;

		lcp = lcp.setScale(2, BigDecimal.ROUND_HALF_UP );

	}
	
	
	// Yukon
	private void calcul_YT()
	{
		// Autre crédit d'impot provincial ou territorial
		BigDecimal k3p = new BigDecimal( 0 );
		BigDecimal k4p = new BigDecimal( 0 );
		
		
		BigDecimal K4PAmount = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "K4PAmount");
		BigDecimal K4PRate = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "K4PRate");
		K4PRate = K4PRate.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP);
		
		k4p = ( K4PRate.multiply(K4PAmount));
		
		if ( (K4PRate.multiply(a)).compareTo( k4p ) < 0 )
			k4p = K4PRate.multiply(a);
		
        // Impot provincial ou territotial de base annuel.
		t4 = v.multiply( a );
		t4 = t4.subtract( kp);
		t4 = t4.subtract( k1p );
		t4 = t4.subtract( k2p );
		t4 = t4.subtract( k3p );
		t4 = t4.subtract( k4p );
		
		if ( t4.compareTo ( V1Min ) <= 0 ) // 6000
			v1 = new BigDecimal( 0 );
		
		if ( t4.compareTo ( V1Min ) > 0  ) 
		{
			v1 = V1Rate; // .05
			v1 = v1.multiply( t4.subtract( V1Min ) );
		}
		s = new BigDecimal( 0 );
		
		// LCP  1250 ou 15 %... 
		BigDecimal LCPmax = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP");
		BigDecimal LCPRate = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "LCP.Rate");

		lcp = ( LCPRate.divide(new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP) .multiply( ft ));
		
		if ( LCPmax.compareTo( lcp ) < 0 )
			lcp = LCPmax;

		lcp = lcp.setScale(2, BigDecimal.ROUND_HALF_UP );
		
		
	}
	
	// Northwest Territories
	private void calcul_NT()
	{
		// Autre crédit d'impot provincial ou territorial
		BigDecimal k3p = new BigDecimal( 0 );
		
		// Impot provincial ou territotial de base annuel.
		t4 = v.multiply( a );
		t4 = t4.subtract( kp);
		t4 = t4.subtract( k1p );
		t4 = t4.subtract( k2p );
		t4 = t4.subtract( k3p );
		
		v1  = new BigDecimal( 0 );
		s = new BigDecimal( 0 );
		lcp = new BigDecimal(0);
		
	}
	
	// Nunavut
	private void calcul_NU()
	{
		// Autre crédit d'impot provincial ou territorial
		BigDecimal k3p = new BigDecimal( 0 );
		
		// Impot provincial ou territotial de base annuel.
		t4 = v.multiply( a );
		t4 = t4.subtract( kp);
		t4 = t4.subtract( k1p );
		t4 = t4.subtract( k2p );
		t4 = t4.subtract( k3p );
		
		v1  = new BigDecimal( 0 );
		s = new BigDecimal( 0 );
		lcp = new BigDecimal(0);
		
	}

	
	public void ImportYTDcalcul_federal_retro_tax( Properties ctx, CalculNet master, int taxID, String trxName) throws Exception
	{

		
		Payment = master.getP_Payment();
		Employee = master.getP_Employee();
		EmployeeDeduction = master.getP_EmployeeDeduction();
		Deduction_Param = master.getP_Deduction_Param();
		Frequency = master.getP_Frequency();
		Period = master.getP_Period();

		taxTable = master.getTaxTable();
		Hashtable s_Matrix   = master.getS_Matrix();   
		Hashtable<String, BigDecimal> s_Employee = master.getS_Employee(); 
		Hashtable<String, BigDecimal> s_Employer = master.getS_Employer();
		Hashtable<String, BigDecimal> s_TD1 = new Hashtable<String, BigDecimal>();
		
		Hashtable<String, BigDecimal> 	taxTable = master.getTaxTable();
		
		BigDecimal employeeAmount   = new BigDecimal( 0 );
		BigDecimal employerAmount   = new BigDecimal( 0 );

		readTD1( Employee, s_TD1, log );

		BigDecimal hoursAdmissible  = Calcul_tax.get_matrix_hoursAdmissible  ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
		BigDecimal salaryAdmissible = Calcul_tax.get_matrix_salaryAdmissible ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );

		if ( salaryAdmissible.compareTo( Env.ZERO ) == 0 )
			   return;

		BigDecimal impos = new BigDecimal( 0 );
        BigDecimal cumulatif = new BigDecimal( 0 );
/*		String query = null;
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
			cumulatif =  rs.getBigDecimal(1) ;
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
*/

		// Nombre de période de paie dans l'année
		BigDecimal p = new BigDecimal( Frequency.getNumberOfPeriod() );

		// Période ou nous somme dans l'année
		BigDecimal pr  = new BigDecimal( Period.getPeriodNo());

        //		impos =  Payment.getAnnualSalary().add(  cumulatif  ).add( salaryAdmissible.multiply( new BigDecimal( Frequency.getNumberOfPeriod() ) ) );
		impos = (calculSalaryAdmissibleMethod09( trxName ).multiply(p)).divide( pr ,2, BigDecimal.ROUND_HALF_UP);
		impos = impos.add(  cumulatif  ).add( salaryAdmissible ); //
		ImportYTDcalcul_federal_tax( taxID, impos);
		
		P_Tax_Federal_Rate TaxRate = get_federal_rate( ctx, taxID, impos, trxName);
		
		// Taux de l'impot fédéral
		BigDecimal r = TaxRate.getFederal_Tax_Rate().divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP );

	    BigDecimal t = (salaryAdmissible.multiply( r ));
		
/*		
		BigDecimal etapeA_T = master.getEmployeeAmount();
		
		BigDecimal pr2  = new BigDecimal( Period.getPeriodNo() -1);
		etapeA_T = (etapeA_T.multiply(pr2));

//		impos =  Payment.getAnnualSalary().add(  cumulatif  );
		impos = ((calculSalaryAdmissibleMethod09( trxName ).multiply(p)).divide( pr ,2, BigDecimal.ROUND_HALF_UP)).add(  cumulatif  );
		calcul_federal_tax(ctx, master, EffectIn, trxName, impos);
		BigDecimal etapeB_T = master.getEmployeeAmount();
		etapeB_T = (etapeB_T.multiply(pr2));

/*		BigDecimal b = Calcul_tax.get_matrix_salaryAdmissible ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
		
		BigDecimal etapeA_T = calcul_federal_tax_T1T2(ctx, master, EffectIn, b, trxName);
		BigDecimal etapeB_T = calcul_federal_tax_T1T2(ctx, master, EffectIn, null, trxName);
*/
		/*
		BigDecimal t = etapeA_T.subtract(etapeB_T);
		
		if ( t.compareTo( new BigDecimal( 0 )) < 0 )
		{
			t = new BigDecimal( 0 );
		}
*/
		employeeAmount = t;   
		employerAmount = new BigDecimal( 0 );

		master.setEmployeeAmount   ( employeeAmount);
		master.setEmployerAmount   ( employerAmount); 
		master.setHoursAdmissible  ( hoursAdmissible); 
		master.setSalaryAdmissible ( salaryAdmissible); 

	}


	public static int get_federal_tax_id( Timestamp EffectIn) throws Exception
	{
		int taxID = 0;

//		log.debug ("Load_tax_parameter Begin");
		
		String query = "Select P_Tax_ID FROM P_Tax ";
        query = MRole.getDefault().addAccessSQL (query, "P_Tax", true, false);	// fully qualidfied - RO 

		query = query + " And P_Tax.EffectIn = ( Select max( EffectIn ) FROM P_Tax WHERE EffectIn < " + DB.TO_DATE( EffectIn ) +  ")";

//		log.debug ("Load_tax_parameter Sql : " + query  );

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (query, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				taxID = rs.getInt( 1 );
			}

			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"X_P_Tax_Provincial_Rate - " + query, e);
		}

		return taxID;
		
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
	
	/*
	private static BigDecimal Calcul_AE_For_tax( BigDecimal salaryAdmissible )
	{
		BigDecimal ei_rate_employee = ZERO;
		BigDecimal ei_rate_employer = ZERO;
		BigDecimal ei_max_gain      = ZERO;
		BigDecimal ei_max_annual    = ZERO;

		ei_max_gain      = (BigDecimal)taxTable.get( "AE.1");
		ei_rate_employee = (BigDecimal)taxTable.get( "AE.2");
		ei_rate_employee = ei_rate_employee.divide( new BigDecimal( 100 ), 8, BigDecimal.ROUND_HALF_UP );

		ei_max_annual    = (BigDecimal)taxTable.get( "AE.3");
//		ei_rate_employer = (BigDecimal)taxTable.get( "AE.4");
		ei_rate_employer = Deduction_Param.getEmployerRate();


		if ( Payment.getSalaryPercentage() != null && Payment.getSalaryPercentage().compareTo( Env.ZERO) != 0)
		{
			BigDecimal rate = Env.ONE.add( new BigDecimal(100).subtract( Payment.getSalaryPercentage()).divide( new BigDecimal(100),8, BigDecimal.ROUND_HALF_UP)); 
			salaryAdmissible = salaryAdmissible.multiply( rate).setScale(2, BigDecimal.ROUND_HALF_UP);
		}

		BigDecimal Amount = salaryAdmissible.multiply( ei_rate_employee ).multiply( new BigDecimal( Frequency.getNumberOfPeriod()));
		
		if (Amount.compareTo( ei_max_annual ) > 0 )
		{
			Amount = ei_max_annual; 
		}
		
        return Amount;
	}
	*/
	/*
	private static BigDecimal Calcul_RPC_RRQ_For_Tax( boolean federal, BigDecimal salaryAdmissible)
	{
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
		
		
		// 18 Year old...
		
		int age = Employee.getAge( Period.getEndDate());

		//Log.trace( 5, EmployeeDeduction.getValue() + " - " + EmployeeDeduction.getName() + " - Employee age = " +  age ) ;

		if ( age < 18  )
		{
			return ZERO;
		}
		
		
		// Formula
		//
		// C = 0.0495 ( S3 - V/P ) until M - A5
		//
		
		BigDecimal sal = ZERO;
		BigDecimal tmp = ZERO;
		sal =  salaryAdmissible ;
		
		tmp =  rpc_exemption.divide( new BigDecimal( Frequency.getNumberOfPeriod()) , 2, BigDecimal.ROUND_HALF_UP);
		
		sal = sal.subtract( tmp );
		
		

		BigDecimal Amount = sal.multiply( rpc_rate ).multiply( new BigDecimal( Frequency.getNumberOfPeriod()));
		
		if (Amount.compareTo( rpc_max ) > 0 )
		{
			Amount = rpc_max; 
		}

		return Amount;
	}
	*/

	private static P_Tax_Federal_Rate get_federal_rate ( Properties ctx, int taxID, BigDecimal salary , String trxName ) throws Exception
	{
		int P_Tax_Federal_Rate_ID = -1;
		
		BigDecimal sal = salary.abs();

		String query = null;
		query = "Select P_Tax_Federal_Rate_ID FROM P_Tax_Federal_Rate "
			+ "WHERE P_Tax_Federal_Rate.P_Tax_ID = " + taxID
			+ " AND " + sal + " >= Limit_Min "
			+ " AND ( " + sal + " < Limit_Max or Limit_Max is null or Limit_Max = 0 ) ";
		
		PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, null);

			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				P_Tax_Federal_Rate_ID =  rs.getInt(1);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
		
	//	log.debug ("X_P_Tax_Federal_Rate - id  " + P_Tax_Federal_Rate_ID );
		
		
		return P_Tax_Federal_Rate.get (ctx, P_Tax_Federal_Rate_ID, trxName );
	}

	//
	// getTD1
	//	
	private static BigDecimal getTD1 ( Hashtable s_TD1, String lineNo  )
	{
		BigDecimal tp = new BigDecimal( 0 );
		
		if ( s_TD1.containsKey( lineNo ) )
			tp = (BigDecimal)s_TD1.get( lineNo  );
		return tp;
	}
	//
	// getTD1
	//	

	// getProvincialRateID
	private static int getProvincialRateID( int taxID, int Taxation_Region_ID , BigDecimal salary, Logger log ) throws Exception
	{
		
		int P_Tax_Provincial_Rate_ID = 0;
		
		if ( salary.compareTo( new BigDecimal(0) ) < 0 )
		{
			return 0;
		}

		String query = "Select P_Tax_Provincial_Rate_ID FROM P_Tax_Provincial_Rate, P_Tax, P_Tax_Provincial "
			+ " WHERE P_Tax.P_Tax_ID = P_Tax_Provincial.P_Tax_ID "
			+ " AND P_Tax_Provincial.P_Tax_Provincial_ID = P_Tax_Provincial_Rate.P_Tax_Provincial_ID "
			+ " AND P_Tax.P_Tax_ID = " + taxID
			+ " AND " + salary + " >= Limit_Min "
			+ " AND ( " + salary + " < Limit_Max or Limit_Max is null or Limit_Max = 0) "
			+ " AND P_Tax_Provincial.C_Region_ID = " + Taxation_Region_ID ;

        

		
	//	log.debug ("X_P_Tax_Provincial_Rate - " + query );
		
		PreparedStatement pstmt_prov = null;
		try
		{
			pstmt_prov = DB.prepareStatement (query, null);
		
			ResultSet rs_prov = pstmt_prov.executeQuery ();
			while (rs_prov.next ())
			{
				P_Tax_Provincial_Rate_ID =  rs_prov.getInt(1);
			}
			rs_prov.close ();
			pstmt_prov.close ();
			pstmt_prov = null;
			
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"X_P_Tax_Provincial_Rate - " + query, e);
		}
		
		//		log.debug ("X_P_Tax_Provincial_Rate - id  " + P_Tax_Provincial_Rate_ID );
		
		return P_Tax_Provincial_Rate_ID;
		
	}   	// getProvincialRateID

	
	//
	// getFirstRate
	//
	private static BigDecimal getFirstRate ( Properties ctx, int taxID, P_Payment Payment, P_Employee Employee, String trxName ) throws Exception
	{
		
		BigDecimal FirstRate = new BigDecimal( 0 );
		
		String query = null;
		
            
   		query = "Select Provincial_Tax_Rate FROM P_Tax_Provincial_Rate, P_Tax, P_Tax_Provincial "
    			+ "WHERE P_Tax.P_Tax_ID = P_Tax_Provincial.P_Tax_ID "
    			+ "AND P_Tax_Provincial.P_Tax_Provincial_ID = P_Tax_Provincial_Rate.P_Tax_Provincial_ID "
    			+ "AND P_Tax.P_Tax_ID = " + taxID
    			+ "AND Limit_Min = 0  "
    			+ "AND P_Tax_Provincial.C_Region_ID = " + Employee.getTaxation_Region_ID();
        
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, null);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			FirstRate =  rs.getBigDecimal(1);
		}
		else
		{
		    P_Time_Sheet TimeSheet = P_Time_Sheet.GetWithPaymentID( ctx, Payment.getP_Payment_ID(), trxName);

		    P_Region TaxRegion = P_Region.get( ctx, Employee.getTaxation_Region_ID(), trxName );
            P_Time_Sheet_Error.NewMessage( TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé de paramètre de tax pour la région : " + TaxRegion.getName(), trxName );
		}
		    
		if ( FirstRate == null )
		{
		    P_Time_Sheet TimeSheet = P_Time_Sheet.GetWithPaymentID( ctx, Payment.getP_Payment_ID(), trxName);

		    P_Region TaxRegion = P_Region.get( ctx, Employee.getTaxation_Region_ID() , trxName);
            P_Time_Sheet_Error.NewMessage( TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé de paramètre de tax pour la région : " + TaxRegion.getName(), trxName );
		}
		
		rs.close ();
		pstmt.close ();
		pstmt = null;
		
		if ( FirstRate == null )
		{
		    return new BigDecimal( 0 );
		}
		
		return FirstRate;
		//.divide( new BigDecimal( 100 ), 6, BigDecimal.ROUND_HALF_UP);
		
	}   // getFirstRate
	
	private static String readTD1 ( P_Employee Employee, Hashtable<String, BigDecimal> s_TD1, Logger log ) throws Exception
	{
		
		int lineNo;
		BigDecimal amount;
		BigDecimal total = new BigDecimal( 0 );
		
		String s_lineNo = " ";
		
		String query = null;
		
		query = "Select P_Form_Template_Detail.Value, " 
			  + "P_Employee_Form_Detail.FormFieldAmount, " 
			  + "P_Employee_Form_Detail.FormFieldText, " 
			  + "P_Employee_Form_Detail.FormTypeField " 
			  + " From P_Form_Template_Detail, P_Employee_Form_Detail , P_Employee_Form "
		      + "Where P_Form_Template_Detail.P_Form_Template_Detail_ID = P_Employee_Form_Detail.P_Form_Template_Detail_ID " 
		      + "  AND P_Employee_Form.P_Employee_Form_ID = P_Employee_Form_Detail.P_Employee_Form_ID" 
		      + "  AND P_Employee_Form.P_Form_Template_ID = " + P_Form_Template.getForm_Template_ID("TD1")
			  + "  AND P_Employee_Form.P_Employee_ID = " + Employee.getP_Employee_ID()
			  + "  AND P_Employee_Form.EffectIn = ( Select Max( EffectIn ) From P_Employee_Form dat WHERE dat.P_Employee_ID = P_Employee_Form.P_Employee_ID AND dat.P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID AND EffectIn <= " + DB.TO_DATE( Period.getStartDate()) + " )" 
			  ; 
		
		s_TD1.clear();
		
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, null);
		ResultSet rs = pstmt.executeQuery ();
		
		boolean td1 = false;
		
		Exonerated = false;

		m_PR = Env.ZERO;
		m_f1 = Env.ZERO;
		while (rs.next ())
		{
			td1 = true;
			s_lineNo =  rs.getString("Value");
			amount =  rs.getBigDecimal("FormFieldAmount");  
			if ( amount != null && ! s_lineNo.equals("A") )
			{
				s_TD1.put( s_lineNo , amount );
				total = total.add( amount );
			}
			//
			// Si le montant est a 0 cela veux dire que l'employé n'a pas de montant personnel de base
			// Si il est a null cela veux dire que l'on utilise la valeur par défaut.
			//
			else if ( amount == null && s_lineNo.equals("1") )
			{
				BigDecimal tp = (BigDecimal)taxTable.get( "TD1.01");   // Read global Federal Parameter
				s_lineNo = String.valueOf( 1 );
				s_TD1.put( s_lineNo ,  tp );
				total = tp;
			}
			//
			// La ligne PR est un champs text qui dit si l'employé le nombre de période de paie qui reste dans l'année.
			//
			if ( s_lineNo.equals("PR") && rs.getString("FormFieldText") != null )
			{
				m_PR = rs.getBigDecimal("FormFieldText") ;
			}

			//
			// La ligne A qui dit si l'employé le nombre de période de paie qui reste dans l'année.
			//
			if ( s_lineNo.equals("A") && rs.getBigDecimal("FormFieldAmount") != null )
			{
				m_f1 = rs.getBigDecimal("FormFieldAmount") ;
			}
			
			//
			// La ligne 20 est un champs text qui dit si l'employé ne doit pas payer d'impot.
			//
			if ( s_lineNo.equals("20") && rs.getString("FormFieldText")!= null && rs.getString("FormFieldText").trim().equals("X"))
			{
				Exonerated = true;
			}


		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
		
		if ( ! td1 )
		{
			BigDecimal tp = (BigDecimal)taxTable.get( "TD1.01");   // Read global Federal Parameter
			s_lineNo = String.valueOf( 1 );
			s_TD1.put( s_lineNo ,  tp );
			total = tp;
		}

		
		s_lineNo = String.valueOf( 19 );
		s_TD1.put( s_lineNo , total );
			
		return " ";
		
	}	// readTD1
	
	private String readTD1prov ( P_Employee Employee, Hashtable<String, BigDecimal> s_TD1, String RegionName, Logger log ) throws Exception
	{
		
		int lineNo;
		BigDecimal amount;
		BigDecimal total = new BigDecimal( 0 );
		
		String s_lineNo = " ";
		
		String query = null;
		
		query = "Select P_Form_Template_Detail.Value, " 
			  + "P_Employee_Form_Detail.FormFieldAmount, " 
			  + "P_Employee_Form_Detail.FormFieldText, " 
			  + "P_Employee_Form_Detail.FormTypeField " 
			  + " From P_Form_Template_Detail, P_Employee_Form_Detail , P_Employee_Form "
		      + "Where P_Form_Template_Detail.P_Form_Template_Detail_ID = P_Employee_Form_Detail.P_Form_Template_Detail_ID " 
		      + "  AND P_Employee_Form.P_Employee_Form_ID = P_Employee_Form_Detail.P_Employee_Form_ID" 
		      + "  AND P_Employee_Form.P_Form_Template_ID = " + P_Form_Template.getForm_Template_ID("TD1"+RegionName)
			  + "  AND P_Employee_Form.P_Employee_ID = " + Employee.getP_Employee_ID()
			  + "  AND P_Employee_Form.EffectIn = ( Select Max( EffectIn ) From P_Employee_Form dat WHERE dat.P_Employee_ID = P_Employee_Form.P_Employee_ID AND dat.P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID AND EffectIn <= " + DB.TO_DATE( Period.getStartDate()) + " )" 
			  ; 
		
		s_TD1.clear();
		
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, null);
		ResultSet rs = pstmt.executeQuery ();
		
		boolean td1 = false;
		
		Exonerated = false;

		m_PR = Env.ZERO;
		m_f1 = Env.ZERO;
		while (rs.next ())
		{
			td1 = true;
			s_lineNo =  rs.getString("Value");
			amount =  rs.getBigDecimal("FormFieldAmount");  
			if ( amount != null && ! s_lineNo.equals("A") )
			{
				s_TD1.put( s_lineNo , amount );
				total = total.add( amount );
			}
			//
			// Si le montant est a 0 cela veux dire que l'employé n'a pas de montant personnel de base
			// Si il est a null cela veux dire que l'on utilise la valeur par défaut.
			//
			
			else if ( amount == null && s_lineNo.equals("1") )
			{
				BigDecimal tp = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" +"TD1"+RegionName+".01");   // Read global Federal Parameter
				s_lineNo = String.valueOf( 1 );
				s_TD1.put( s_lineNo ,  tp );
				total = tp;
			}
			//
			// La ligne PR est un champs text qui dit si l'employé le nombre de période de paie qui reste dans l'année.
			//
			if ( s_lineNo.equals("PR") && rs.getString("FormFieldText") != null )
			{
				m_PR = rs.getBigDecimal("FormFieldText") ;
			}

			//
			// La ligne A qui dit si l'employé le nombre de période de paie qui reste dans l'année.
			//
			if ( s_lineNo.equals("A") && rs.getBigDecimal("FormFieldAmount") != null )
			{
				m_f1 = rs.getBigDecimal("FormFieldAmount") ;
			}
			
			//
			// La ligne 20 est un champs text qui dit si l'employé ne doit pas payer d'impot.
			//
			if ( s_lineNo.equals("20") && rs.getString("FormFieldText")!= null && rs.getString("FormFieldText").trim().equals("X"))
			{
				Exonerated = true;
			}


		}
		rs.close ();
		pstmt.close ();
		pstmt = null;
		
		if ( ! td1 )
		{
			BigDecimal tp = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" +"TD1"+RegionName+".01");   // Read global Federal Parameter
			s_lineNo = String.valueOf( 1 );
			s_TD1.put( s_lineNo ,  tp );
			total = tp;
		}

		
		s_lineNo = String.valueOf( 19 );
		s_TD1.put( s_lineNo , total );
			
		return " ";
		
	}	// readTD1prov
	
	private static BigDecimal calculSalaryAdmissibleMethod09(String trxName ) throws SQLException
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
		    + " and P_Method_Deduction.Value in ( '009' )" // '011',
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

/*	public BigDecimal get_provincial_amount()
	{
		return provincial_amount;

	}

	public BigDecimal get_provincial_amount_retro()
	{
		return provincial_amount_retro;
	}

	public BigDecimal get_provincial_salaryAdmissible()
	{
		return 	provincial_salaryAdmissible;
	}

	public BigDecimal get_provincial_hoursAdmissible()
	{
		return 	provincial_hoursAdmissible;
	}
*/
}
