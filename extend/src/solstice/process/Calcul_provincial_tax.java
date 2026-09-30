/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/
package solstice.process;
  
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Properties;

import org.compiere.model.MLocation;
import org.compiere.model.MRegion;
import org.compiere.model.MRole;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

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
public class Calcul_provincial_tax {

	/**	Static Logger				*/
	private static CLogger		log = CLogger.getCLogger (Calcul_provincial_tax.class);

	static final public BigDecimal TAUX_RETRO_QC_FED_1 = new BigDecimal(5);
	static final public BigDecimal TAUX_RETRO_QC_FED_2 = new BigDecimal(10);	
	static final public BigDecimal TAUX_RETRO_QC_FED_3 = new BigDecimal(15);
	static final public BigDecimal TAUX_RETRO_HORS_QC_FED_1 =  new BigDecimal(10);
	static final public BigDecimal TAUX_RETRO_HORS_QC_FED_2 =  new BigDecimal(20);
	static final public BigDecimal TAUX_RETRO_HORS_QC_FED_3 =  new BigDecimal(30);

// depuis de 1er juillet 2023	
//	static final public BigDecimal TAUX_RETRO_QC_PROV_1 =  new BigDecimal(16);
//	static final public BigDecimal TAUX_RETRO_QC_PROV_2 =  new BigDecimal(20);
	static final public BigDecimal TAUX_RETRO_QC_PROV_1 =  new BigDecimal(14);
	static final public BigDecimal TAUX_RETRO_QC_PROV_2 =  new BigDecimal(19);
	static final public BigDecimal INFINITE = null;

	private  P_Employee Employee;
	private  P_Payment  Payment;
	private  P_Employee_Deduction EmployeeDeduction ;
	private  P_Deduction_Param Deduction_Param ;
	private  P_Frequency Frequency ;
	private  P_Period    Period ;
	private  Hashtable<?, ?> 	taxTable = new Hashtable<Object, Object>();
	
	private  CalculNet m_master;
	private  BigDecimal Taux_Imp;
	
	private  boolean Exonerated = false;
	private  boolean ExoneratedHeatContribution = false;
	private  BigDecimal m_PR = null;


	
	//
    //	 calcul_quebec_tax
	//
	public  void calcul_quebec_tax ( Properties ctx, CalculNet master, Timestamp EffectIn, String trxName, boolean gratification, BigDecimal impos ) throws Exception
	{
		
		Payment = master.getP_Payment();
		Employee = master.getP_Employee();

		m_master = master;
		
		if ( Employee.isStatusIndian() )		// 2009.04.27
			  return ;

		//2011.10.26 replace Employee.getTaxation_Region_ID par Payment.getTaxation_Region_ID
		//
		
		MRegion Region = new MRegion( ctx, Payment.getTaxation_Region_ID(), trxName );
		MRegion RegionResidence = null; 

		MLocation Location = MLocation.get( Env.getCtx(), Employee.getC_Location_ID(), trxName);
		if ( Location != null )
			RegionResidence = new MRegion( Env.getCtx(), Location.getC_Region_ID(), trxName);

		if ( Region.getC_Region_ID() != CalculNet.QUEBEC &&  RegionResidence.getC_Region_ID() != CalculNet.QUEBEC  )
			return;

		EmployeeDeduction = master.getP_EmployeeDeduction();
		Deduction_Param = master.getP_Deduction_Param();
		Frequency = master.getP_Frequency();
		Period = master.getP_Period();
		
		taxTable = master.getTaxTable();
		Hashtable<?, ?> s_Matrix   = master.getS_Matrix();   
		Hashtable<?, ?> s_MatrixTaxB = master.getS_MatrixTaxB();   
		Hashtable<?, ?> s_Employee = master.getS_Employee(); 
		Hashtable<?, ?> s_Employer = master.getS_Employer();
		Hashtable<?, ?> s_SalaryAdmissible = master.getS_SalaryAdmissible();

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
		BigDecimal f2  = new BigDecimal( 0 );
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
		
		BigDecimal q   = new BigDecimal( 0 );
		BigDecimal q1  = new BigDecimal( 0 );
		BigDecimal t  = new BigDecimal( 0 );
		
		// Impot pour l'année
		BigDecimal y  = new BigDecimal( 0 );
		
		// Salaire Brut assujetti à la retenue d'impot.
		BigDecimal d  = new BigDecimal( 0 );
		BigDecimal h  = new BigDecimal( 0 );
		
		
		//Z = contribution santé pour l’année selon le revenu net annuel estimatif
		BigDecimal z  = new BigDecimal( 0 );
		BigDecimal r = new BigDecimal( 0 );
		
		employeeAmount   = new BigDecimal( 0 );
		employerAmount   = new BigDecimal( 0 );
		
		//
		//
		// Recherche des montants admissible /
		// Search admisible amount
		BigDecimal hoursAdmissible;
		BigDecimal salaryAdmissible;
 		if ( Region.getC_Region_ID() != CalculNet.QUEBEC &&  RegionResidence.getC_Region_ID() == CalculNet.QUEBEC  )
 		{
 			hoursAdmissible  = Calcul_tax.get_matrix_hoursAdmissible  ( s_MatrixTaxB, Deduction_Param.getP_Column_Adm_ID() );
 			salaryAdmissible = Calcul_tax.get_matrix_salaryAdmissible ( s_MatrixTaxB, Deduction_Param.getP_Column_Adm_ID() );
 		}
 		else
 		{
 			hoursAdmissible  = Calcul_tax.get_matrix_hoursAdmissible  ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
 			salaryAdmissible = Calcul_tax.get_matrix_salaryAdmissible ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
 		}
		
		
//		j 	= new BigDecimal(0); //getTP1015( ctx, Employee, s_TP1015, "19", trxName ); //.divide( p, 2, BigDecimal.ROUND_HALF_UP ); 
		//		j1 	= getTP1016() ;            // ici
		
 		if (!impos.equals(new BigDecimal(0)))
 		{			
 			salaryAdmissible = impos;
 		}
 		
 		
		j  = getTP1015( ctx, Employee, s_TP1015, "19", trxName );//.divide( p, 2, BigDecimal.ROUND_HALF_UP );

		//total des sommes suivantes pour la période de paie :
		//	• les sommes retenues comme cotisation à un RPA;
		//	• les sommes retenues comme cotisation à un REER;
		//	• les sommes retenues comme cotisation versée selon une convention de retraite;
		//	• la déduction relative au RIC, soit 125 % de la somme prélevée sur la rémunération de
		//	l’employé, pour lui permettre d’acquérir une part privilégiée admissible au RIC;
		//	• la déduction relative aux voyages pour les résidents d’une région éloignée reconnue;
		//	• la déduction pour option d’achat de titres;
		//	• la partie de la rémunération qui donne droit à l’une des déductions suivantes :
		//	– déduction pour un revenu « situé » dans une réserve ou un « local »,
		//	– déduction pour un revenu d’emploi gagné sur un navire,
		//	– déduction pour un employé qui travaille dans un CFI,
		//	– déduction pour un spécialiste étranger,
		//	– déduction pour un chercheur étranger,
		//	– déduction pour un chercheur étranger en stage postdoctoral,
		//	– déduction pour un expert étranger,
		//	– déduction pour un professeur étranger,
		//	– déduction pour un producteur étranger et pour un particulier étranger occupant un
		//	poste clé dans une production étrangère tournée au Québec,
		//	– déduction pour un travailleur agricole étranger,
		//	– déduction pour le personnel des Forces canadiennes ou des forces policières.
			
		// retenues sur la paies aux fin des cotisations RPA ou REER ou a une convention de retraite (CR)
		f = Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "024").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "024"));
		//Calcul REER - Part employé          
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "034"));
		//Calcul EUPP REER
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "037"));
		//Syndicat indépendant
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "026"));
		//Calcul de fonds pension - RRE et RRF
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "030"));
		//Calcul de fonds pension - RREGOP
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "058"));
		//Calcul de fonds pension - base
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "005"));
		//Fonds pension par borne de MGA
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "006"));
		//Calcul FTQ versé dans un REER 
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "007").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "007")));
		//Fondaction CSN versé dans un REER 
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "050").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "050")));
		//Calcul de fonds pension - base
		f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "027").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "027")));
		
		//F2
		//total des sommes suivantes pour la période de paie :
		//	• les sommes retenues comme cotisation à un RPA;
		//	• les sommes retenues comme cotisation à un REER;
		//	• les sommes retenues comme cotisation versée selon une convention de retraite;
		//	• la déduction relative aux voyages pour les résidents d’une région éloignée reconnue.
		f2 = Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "024").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "024"));
		//Calcul REER - Part employé          
		f2 = f2.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "034"));
		//Calcul EUPP REER
		f2 = f2.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "037"));
		//Calcul de fonds pension - RRE et RRF
		f2 = f2.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "030"));
		//Calcul de fonds pension - RREGOP
		f2 = f2.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "058"));
		//Calcul de fonds pension - base
		f2 = f2.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "005"));
		//Fonds pension par borne de MGA
		f2 = f2.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "006"));
		//Calcul FTQ versé dans un REER 
		f2 = f2.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "007").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "007")));
		//Fondaction CSN versé dans un REER 
		f2 = f2.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "050").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "050")));
		//Calcul de fonds pension - base
		f2 = f2.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "027"));

		
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
/*			h = d.multiply( new BigDecimal( 0.06 ) );
			if ( h.compareTo( new BigDecimal( 1000).divide(p,2,BigDecimal.ROUND_HALF_UP)) > 0 )
			{
				h = new BigDecimal( 1000).divide(p,2,BigDecimal.ROUND_HALF_UP);
			}
*/

			/*
			CSA = déduction des cotisations supplémentaires de l’employée ou de l’employé au RRQ relatives à
					son salaire admissible au RRQ pour la période de paie, excepté les gratifications, les
					paiements rétroactifs ou les autres paiements forfaitaires semblables
					CS × [(S3 – B2) ÷ S3]
					*/
			
			BigDecimal c = master.getAmount_Rpc_QC();
			if ( c == null) c = Env.ZERO;
			BigDecimal c2 = master.getAmount_Rpc2();
			if ( c2 == null) c2 = Env.ZERO;
				
			BigDecimal cs = c.multiply( new BigDecimal( 0.01).divide( new BigDecimal(0.0640),8,BigDecimal.ROUND_HALF_UP )).add( c2 );
// 2025-09-10			
//			BigDecimal s3 = master.getSalary_Rpc();
			BigDecimal s3 = master.getSalary_Rpc().add( master.getSalary_Rpc2());

			BigDecimal b2 = Env.ZERO;
			BigDecimal CSA = Env.ZERO;
			
			if ( s3.compareTo( Env.ZERO) != 0)
				CSA = cs.multiply( (s3.add( b2 ))).divide( s3,8,BigDecimal.ROUND_HALF_UP );
			
			CSA = CSA.setScale(2, BigDecimal.ROUND_HALF_UP);

//			BigDecimal CSA = Env.ZERO;
			
			// Revenu imposable annuel
			if ( m_PR != null && m_PR.compareTo(new BigDecimal(0)) != 0 )
			{
			
				j = (p.multiply(j)).divide(m_PR,2,BigDecimal.ROUND_HALF_UP);
				j1 = (p.multiply(j1)).divide(m_PR,2,BigDecimal.ROUND_HALF_UP);
				
				i = p.multiply( g.subtract( f ).subtract( h ).subtract(CSA )).subtract( j ).subtract( j1 );
				r = p.multiply( g.subtract( f2 ).subtract( h ).subtract(CSA )).subtract( j ).subtract( j1 );
			}
			else
			{
				i = p.multiply( g.subtract( f ).subtract( h ).subtract(CSA )).subtract( j ).subtract( j1 );
				r = p.multiply( g.subtract( f2 ).subtract( h ).subtract(CSA )).subtract( j ).subtract( j1 );
				
			}

				
			
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
		e1 = getTP1015( ctx, Employee, s_TP1015, "7", trxName );
		if ( e1 == null )
			e1 = new BigDecimal( 0 );
		
//		e2 = getTP1015( ctx, Employee, s_TP1015, "9", trxName );
		e2 = getTP1015( ctx, Employee, s_TP1015, "9", trxName );
//		e2 = e2.add( getTP1015( ctx, Employee, s_TP1015, "9", trxName ));
				
		l  = getTP1015( ctx, Employee, s_TP1015, "11", trxName ); 
		BigDecimal l2  = getTP1015( ctx, Employee, s_TP1015, "L2", trxName ); 
		
		if ( e2 == null )
			e2 = new BigDecimal( 0 );
		
		// nombre de période de paie qui restent dans l'année    
		pr  = p.subtract( new BigDecimal( Period.getPeriodNo()) );
 		
		
		// Somme retenue pour la période de paie pour l'achat d'action FTQ.
		q = Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "007").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "007"));
		q = q.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "023") );
		
		// Somme retenue pour la période de paie pour l’achat d’actions de catégorie A ou B de Fondaction – le Fonds de
		//		développement de la Confédération des syndicats nationaux (CSN) pour la coopération et l’emploi		
		q1 = Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "050").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "050"));
		q1 = q1.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "051") );

		
		// Valeur indexée.
/*		e = e1.multiply(  new BigDecimal(  1 + 0.01427  ) );   
		e = e.add( e2 );
		
		e = e.setScale( 2, BigDecimal.ROUND_HALF_UP );
*/
		e = e1.add( e2 );
		y = t.multiply(i);
		
		//2013
		// Y = [(T x I) – K – K1 – (0,20 x E) – (0,15 x P x Q) – (0,25 x P x Q1)] + Z
		
// 2019-03-26		
//		y = y.subtract( k ).subtract( k1 ).subtract( new BigDecimal(  0.20  ).multiply(e)); 
//		y = y.subtract( k ).subtract( k1 ).subtract( new BigDecimal(  0.15  ).multiply(e)); 
		y = y.subtract( k ).subtract( k1 ).subtract( new BigDecimal(  0.14  ).multiply(e)); 
		y = y.subtract( p.multiply(q).multiply( new BigDecimal( 0.15 ) ) );
		
//		y = y.subtract( p.multiply(q1).multiply( new BigDecimal( 0.25 ) ) );
		//2015.05.24 Modification temporaire du taux du crédit d’impôt pour l’acquisition d’actions de Fondaction
		//2021-12-21 de .20 a .15
		y = y.subtract( p.multiply(q1).multiply( new BigDecimal( 0.15 ) ) );

//		y = y.subtract( p.multiply(q1).multiply( new BigDecimal( 0.20 ) ) );
		
		
		// l'employé doit avoir plus de 18 ans pour contribuer à la CONTRIBUTION SANTÉ DES PARTICULIERS
		int age = Employee.getAge( Payment.getPayDate() );
		if ( age <  18 )
			ExoneratedHeatContribution = true;
		
		if ( y.compareTo( new BigDecimal(0) ) < 0 )
		{
			y = new BigDecimal( 0 );
		}

		y = y.setScale( 2, BigDecimal.ROUND_HALF_UP );

		a = y.divide( p , 2, BigDecimal.ROUND_HALF_UP );
		// add impot additionnel
		a = a.add( l );

		if ( l2 != null && l2.compareTo(Env.ZERO) != 0)
		{
			a = a.add( a.multiply(l2.divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP) ));
			a = a.setScale(2,BigDecimal.ROUND_HALF_UP);
		}
		
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
		boolean Exonerated = getTP1015Exonerated( ctx, Employee, Period, "22", trxName ).equals("X");

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


	private  BigDecimal calcul_f1( String trxName ) throws SQLException
	{
		BigDecimal f1 = new BigDecimal(0);
		
        String query = null;
		query = "Select isnull( sum( Employee_Part ),0), isnull( sum( case P_Deduction_Param.Value WHEN '024' THEN Employer_Part WHEN '007' THEN Employer_Part ELSE 0 END ),0)"  
			+ " from P_Payment_Deduction, P_Deduction, P_Deduction_Param, P_Method_Deduction "
			+ " where P_Payment_Deduction.P_Deduction_ID = P_Deduction.P_Deduction_ID "
			+ " and P_Payment_Deduction.P_Year_ID = " + Period.getP_Year_ID ()
			+ " and P_Payment_Deduction.P_Employee_ID = " + Payment.getP_Employee_ID ()
		    + " and P_Deduction.P_Deduction_ID = P_Deduction_Param.P_Deduction_ID "
		    + " and P_Deduction_Param.EffectIn  in ( Select max( EffectIn ) FROM P_deduction_Param eff where eff.P_Deduction_ID = P_Deduction.P_Deduction_ID ) "
		    + " and P_Deduction_Param.P_Method_Deduction_ID = P_Method_Deduction.P_Method_Deduction_ID " 
		    + " and P_Method_Deduction.value IN ( '024','026','030','058','005','006','007' )"
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

	private  BigDecimal calculSalaryAdmissibleMethod11(String trxName ) throws SQLException
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

	private  BigDecimal calcul_g1( String trxName ) throws SQLException
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


	private BigDecimal calcul_g1( P_Period Period, P_Payment Payment, String trxName ) throws SQLException
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
		    + " and P_Method_Deduction.Value in ( '011' )" // '012',
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


	private BigDecimal calcul_f1( P_Period Period, P_Payment Payment, String trxName ) throws SQLException
	{
		BigDecimal f1 = new BigDecimal(0);
		
        String query = null;
//		query = "Select isnull( sum( Employee_Part ),0), isnull( sum( case P_Method_Deduction.Value WHEN '024' THEN Employer_Part WHEN '007' THEN Employer_Part ELSE 0 END ),0)"  
		query = "Select SUM( Case when P_Method_Deduction.Value IN ( '024','007','050' ) then isnull(Employee_Part,0) + isnull(Employer_Part,0)  else isnull(Employee_Part,0) end )"  
			+ " from P_Payment_Deduction "
			+ "  Inner Join P_Deduction ON P_Deduction.P_Deduction_ID = P_Payment_Deduction.P_Deduction_ID   "
			+ "  Inner Join P_Deduction_Param ON P_Deduction_Param.P_Deduction_ID = P_Deduction.P_Deduction_ID "
			+ "  Inner Join P_Method_Deduction On P_Method_Deduction.P_Method_Deduction_ID  = P_Deduction_Param.P_Method_Deduction_ID "
			+ " WHERE "
			+ " P_Payment_Deduction.P_Year_ID = " + Period.getP_Year_ID ()
			+ " and P_Payment_Deduction.P_Employee_ID = " + Payment.getP_Employee_ID ()
		    + " and P_Deduction_Param.EffectIn  in ( Select max( EffectIn ) FROM P_deduction_Param eff where eff.P_Deduction_ID = P_Deduction.P_Deduction_ID ) "
		    + " and P_Method_Deduction.value IN ( '024','026','030','037','058','258','005','006','007','050', '027' )"
		    ;
		
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);
		ResultSet rs = pstmt.executeQuery ();
		if (rs.next ())
		{
			f1 =  rs.getBigDecimal(1) ;
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;

	   return f1;
	}

	private BigDecimal calculSalaryAdmissibleMethod11( P_Period Period, P_Payment Payment, int P_Period_ID, String trxName ) throws SQLException
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
		if ( P_Period_ID != 0)
			query = query + " and P_Payment_Deduction.P_Period_ID = " + P_Period_ID;

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

	
	public void calcul_quebec_retro_tax ( Properties ctx, CalculNet master, P_Payment Payment, P_Employee Employee, P_Period Period, P_Deduction_Param Deduction_Param,  Timestamp EffectIn, String trxName ) throws Exception
	{

//		Payment = master.getP_Payment();
//		Employee = master.getP_Employee();
		
		if ( Employee.isStatusIndian() )		// 2009.04.27
			  return ;

		//2011.10.26 replace Employee.getTaxation_Region_ID par Payment.getTaxation_Region_ID
		//
		MRegion Region = new MRegion( ctx, Payment.getTaxation_Region_ID(), trxName );
		MRegion RegionResidence = null; 

		MLocation Location = MLocation.get( Env.getCtx(), Employee.getC_Location_ID(), trxName);
		if ( Location != null )
			RegionResidence = new MRegion( Env.getCtx(), Location.getC_Region_ID(), trxName);

		if ( Region.getC_Region_ID() != CalculNet.QUEBEC &&  RegionResidence.getC_Region_ID() != CalculNet.QUEBEC  )
			return;

//		EmployeeDeduction = master.getP_EmployeeDeduction();
//		Deduction_Param = master.getP_Deduction_Param();
//		Frequency = master.getP_Frequency();
//		Period = master.getP_Period();

		P_Frequency Frequency = P_Frequency.get( Env.getCtx(), Period.getP_Frequency_ID(), trxName);

		Hashtable<?, ?> taxTable = master.getTaxTable();
		Hashtable<?, ?> s_Matrix   = master.getS_Matrix();   
		Hashtable<?, ?> s_MatrixTaxB   = master.getS_MatrixTaxB();   
		Hashtable<?, ?> s_Employee = master.getS_Employee(); 
		Hashtable<?, ?> s_Employer = master.getS_Employer();

		BigDecimal employeeAmount   = new BigDecimal( 0 );
		BigDecimal employerAmount   = new BigDecimal( 0 );

		BigDecimal hoursAdmissible;
		BigDecimal salaryAdmissible;
 		if ( Region.getC_Region_ID() != CalculNet.QUEBEC &&  RegionResidence.getC_Region_ID() == CalculNet.QUEBEC  )
 		{
 			hoursAdmissible  = Calcul_tax.get_matrix_hoursAdmissible  ( s_MatrixTaxB, Deduction_Param.getP_Column_Adm_ID() );
 			salaryAdmissible = Calcul_tax.get_matrix_salaryAdmissible ( s_MatrixTaxB, Deduction_Param.getP_Column_Adm_ID() );
 		}
 		else
 		{
 			hoursAdmissible  = Calcul_tax.get_matrix_hoursAdmissible  ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
 			salaryAdmissible = Calcul_tax.get_matrix_salaryAdmissible ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
 		}


		if ( salaryAdmissible.compareTo( new BigDecimal( 0 ) ) == 0)
			return ;
		/** Ajout Steph
		 * 
		 */

		boolean method01 = true;
		
		// (G1 – F1 – H1 ) + [Pr × (G – F – H2	)] – J – J1
		
		if  ( method01 == true )
		{
			BigDecimal BonusAmount = salaryAdmissible;
			// nombre de périodes de paie dans l’année
			BigDecimal p = new BigDecimal( Frequency.getNumberOfPeriod() );

			// nombre de périodes de paie qui restent dans l’année (y compris la période de paie courante
			P_Period paymentPeriod = P_Period.get(Env.getCtx(), Payment.getP_Period_ID(), trxName);
//			BigDecimal pr  = p.subtract( new BigDecimal( paymentPeriod.getPeriodNo() -1 ));
			BigDecimal pr  = p.subtract( new BigDecimal( Period.getPeriodNo() -1 ));
			
			//total des sommes mentionnées à la variable G et accumulées avant le début de la période de paie courante
			BigDecimal g1 = new BigDecimal(0);

			g1 = calcul_g1(Period, Payment, trxName); // Salaire admissible dans l'année a l'impot gratification.

			
			// total des sommes mentionnées à la variable F et accumulées avant le début de la période
			// de paie courante
			BigDecimal f1 = new BigDecimal(0);
			f1 = calcul_f1(Period, Payment, trxName);
		
			
			//total des sommes mentionnées à la variable H et accumulées avant le début de la période de paie courante
			BigDecimal h1 = new BigDecimal(0);
			//A VALIDER
			
			Hashtable<String, Object> s_TP1015 = new Hashtable<String, Object>();

			BigDecimal m_PR = readTP1015( ctx, Payment, Employee, Period, taxTable, s_TP1015, trxName );

			
			BigDecimal HRate = getTP1015( ctx, Employee, s_TP1015, "98", trxName );
			BigDecimal HMax = getTP1015( ctx, Employee, s_TP1015, "99", trxName );
			HRate = HRate.divide( new BigDecimal( 100 ) , 8, BigDecimal.ROUND_HALF_UP );

			h1 = g1.multiply( HRate );
			if ( h1.compareTo( HMax ) > 0 )
			{
				h1 = HMax;
			}
			
			// rémunération brute assujettie à la retenue d’impôt pour la période de paie, excepté les
			// gratifications, les paiements rétroactifs ou les autres paiements forfaitaires semblables
			BigDecimal g = new BigDecimal(0);
			g = (calculSalaryAdmissibleMethod11( Period, Payment, Period.getP_Period_ID(), trxName ));

			//total des sommes mentionnées à la variable F et accumulées avant le début de la période de paie courante
			BigDecimal f = new BigDecimal(0);
			// retenues sur la paies aux fin des cotisations RPA ou REER ou a une convention de retraite (CR)
			f = Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "024").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "024"));
			//Calcul REER - Part employé          
			f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "034"));
			//Calcul EUPP REER
			f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "037"));
			//Syndicat indépendant          
			f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "026"));
			//Calcul de fonds pension - RRE et RRF
			f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "030"));
			//Calcul de fonds pension - RREGOP
			f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "058"));
			f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "258"));
			//Calcul de fonds pension - base
			f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "005"));
			//Fonds pension par borne de MGA
			f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "006"));
			//Calcul FTQ versé dans un REER 
			f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "007").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "007")));
			//Fondaction CSN versé dans un REER 
			f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod( s_Employee, "050").add( Calcul_tax.getDeductionEmployerAmountMethod( s_Employer, "050")));
			//Calcul de fonds pension - base
			f = f.add( Calcul_tax.getDeductionEmployeeAmountMethod(s_Employee, "027"));


			// déduction pour emploi
			BigDecimal h2 = new BigDecimal(0);

			h2 = BonusAmount.multiply( HRate );
			if ( h2.compareTo( HMax.divide(p,2,BigDecimal.ROUND_HALF_UP)) > 0 )
			{
				h2 = HMax.divide(p,2,BigDecimal.ROUND_HALF_UP);
			}
			


			
			// Déductions inscrites à la ligne 19 du formulaire TP-1015.3.
			// Si la valeur de J est déterminée après la première période de paie de l’année, elle est plutôt
			// égale au résultat du calcul suivant :
			BigDecimal j = new BigDecimal(0);
			j  = getTP1015( ctx, Employee, s_TP1015, "19", trxName );//.divide( p, 2, BigDecimal.ROUND_HALF_UP );
			// Revenu imposable annuel
			
			//déductions annuelles que nous avons autorisées après que le particulier a rempli le
			//formulaire TP-1016.
			//Si la valeur de J1
			// est déterminée après la première période de paie de l’année, elle est plutôt
			//égale au résultat du calcul suivant :
			BigDecimal j1 = new BigDecimal(0);
			
			BigDecimal tc = pr.multiply( ( g.subtract( f ).subtract( h2 ) ) );
			if ( f1 == null ) f1 = Env.ZERO;
			if ( h1 == null ) h1 = Env.ZERO;
			if ( tc == null ) tc = Env.ZERO;
			
			BigDecimal t = (g1.subtract( f1 ).subtract( h1 )).add( tc  ).subtract( j ).subtract( j1 );

			int P_Tax_Provincial_Rate_ID = getProvincialRateID( Payment, Employee, Period,  t, log, trxName );
			P_Tax_Provincial_Rate TaxRate_prov = P_Tax_Provincial_Rate.get (ctx, P_Tax_Provincial_Rate_ID, trxName );

			BigDecimal Taux_Imp = TaxRate_prov.getProvincial_Tax_Rate();

			salaryAdmissible = BonusAmount;

			t = salaryAdmissible.multiply( Taux_Imp );

			if ( t.compareTo( new BigDecimal( 0 )) < 0 )
			{
				t = new BigDecimal( 0 );
			}
			
			employeeAmount = t;   
			employerAmount = new BigDecimal( 0 );

		}	
		else  // method 02 
		{
			BigDecimal BonusAmount = salaryAdmissible;
			// Nombre de période de paie dans l'année
			BigDecimal p = new BigDecimal( Frequency.getNumberOfPeriod() );

			// Période ou nous somme dans l'année
			BigDecimal pr  = p.subtract( new BigDecimal( Period.getPeriodNo() -1 ));

			salaryAdmissible = (calculSalaryAdmissibleMethod11( Period, Payment, Period.getP_Period_ID(), trxName )); // .multiply(p)).divide( pr ,2, BigDecimal.ROUND_HALF_UP);

			int P_Tax_Provincial_Rate_ID = getProvincialRateID( Payment, Employee, Period,  salaryAdmissible, log, trxName );
			P_Tax_Provincial_Rate TaxRate_prov = P_Tax_Provincial_Rate.get (ctx, P_Tax_Provincial_Rate_ID, trxName );

			BigDecimal Taux_Imp = TaxRate_prov.getProvincial_Tax_Rate();

			
			calcul_quebec_tax(ctx, master, EffectIn, trxName, false, salaryAdmissible );
			
			salaryAdmissible = BonusAmount ;
			
			

			BigDecimal t = salaryAdmissible.multiply( Taux_Imp );

			if ( t.compareTo( new BigDecimal( 0 )) < 0 )
			{
				t = new BigDecimal( 0 );
			}

			employeeAmount = t;   
			employerAmount = new BigDecimal( 0 );
			
		}

		master.setEmployeeAmount   ( employeeAmount);
		master.setEmployerAmount   ( employerAmount); 
		master.setHoursAdmissible  ( hoursAdmissible); 
		master.setSalaryAdmissible ( salaryAdmissible); 
		

	}
		
	//
    //	 readTP1015
    //		 
	public BigDecimal readTP1015 (Properties ctx, P_Payment Payment, P_Employee Employee, P_Period Period, Hashtable<?, ?> taxTable,  Hashtable<String, Object> s_TP1015, String trxName ) throws Exception
	{
		s_TP1015.clear();

		//2011.10.26 replace Employee.getTaxation_Region_ID par Payment.getTaxation_Region_ID
		//
		
		MRegion Region = new MRegion( ctx, Payment.getTaxation_Region_ID(), trxName );
		if ( ! Region.getName().equals("QC") )
			return null;

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
			  // Ajout SM 2013-01-09 ajout de la gestion de la date de fin
			  // et du statut sur le formulaire employé.
		      + "  AND P_Employee_Form.IsActive = 'Y' "
     		  + "  AND P_Employee_Form.EffectIn = ( Select Max( EffectIn ) From P_Employee_Form dat WHERE dat.P_Employee_ID = P_Employee_Form.P_Employee_ID AND dat.P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID AND EffectIn <= " + DB.TO_DATE( Period.getPayDate())  + " and ( EFFECTTO is null or EFFECTTO > " + DB.TO_DATE( Period.getPayDate()) + "))"
			  ;

		PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (query, trxName);

//			boolean Exonerated = false;
			boolean ExoneratedHeatContribution = false;
			BigDecimal m_PR = new BigDecimal(0);
			
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
				// La ligne 24 Cochez la case ci-contre si vous demandez d’être exonéré de la retenue à la source de la contribution santé.
				//
				if ( (s_lineNo.equals("22") && rs.getString("FormFieldText")!= null && rs.getString("FormFieldText").toUpperCase().trim().equals("X")))
				{
					ExoneratedHeatContribution = true;
					log.log(Level.SEVERE, "PayProcessor DEBUG ExoneratedHeatContribution : " + Employee.getValue() + " " + Employee.getName() ); 
			    }
				
				//
				// La ligne 22 est un champs text qui dit si l'employé ne doit pas payer d'impot.
				//
				if ( s_lineNo.equals("20") && rs.getString("FormFieldText")!= null && rs.getString("FormFieldText").toUpperCase().trim().equals("X"))
				{
//					Exonerated = true;
					log.log(Level.SEVERE, "PayProcessor DEBUG Exonerated : " + Employee.getValue() + " " + Employee.getName() ); 

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
		total5 = total5.add( getTP1015( ctx, Employee, s_TP1015, "5", trxName ));
		s_lineNo = String.valueOf( 7 );
		s_TP1015.put( s_lineNo , total5 );
		
        // Ajout Stéphane Morin 2007-08-23 Déduction pour emploi utilisé paramètre au lieu de hardcoding
		// il change aux années depuis quelques temps
		BigDecimal HRate = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "HRate");   // Read global Federal Parameter
		s_lineNo = String.valueOf( 98 );
		if ( HRate != null )
			s_TP1015.put( s_lineNo ,  HRate );
		BigDecimal HMax = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "HMax");   // Read global Federal Parameter
		s_lineNo = String.valueOf( 99 );
		if ( HMax != null)
			s_TP1015.put( s_lineNo ,  HMax );
	
	    return m_PR;
		
	}	// readTP1015
	
	
/*
	public  void calcul_quebec_retro_tax ( Properties ctx, CalculNet master, Timestamp EffectIn, String trxName ) throws Exception
	{

		Payment = master.getP_Payment();
		Employee = master.getP_Employee();
		
		if ( Employee.isStatusIndian() )		// 2009.04.27
			  return ;

		//2011.10.26 replace Employee.getTaxation_Region_ID par Payment.getTaxation_Region_ID
		//
		MRegion Region = new MRegion( ctx, Payment.getTaxation_Region_ID(), trxName );
		MRegion RegionResidence = null; 

		MLocation Location = MLocation.get( Env.getCtx(), Employee.getC_Location_ID(), trxName);
		if ( Location != null )
			RegionResidence = new MRegion( Env.getCtx(), Location.getC_Region_ID(), trxName);

		if ( Region.getC_Region_ID() != CalculNet.QUEBEC &&  RegionResidence.getC_Region_ID() != CalculNet.QUEBEC  )
			return;

		
		EmployeeDeduction = master.getP_EmployeeDeduction();
		Deduction_Param = master.getP_Deduction_Param();
		Frequency = master.getP_Frequency();
		Period = master.getP_Period();
		
		taxTable = master.getTaxTable();
		Hashtable<?, ?> s_Matrix   = master.getS_Matrix();   
		Hashtable<?, ?> s_MatrixTaxB   = master.getS_MatrixTaxB();   
		Hashtable<?, ?> s_Employee = master.getS_Employee(); 
		Hashtable<?, ?> s_Employer = master.getS_Employer();

		BigDecimal employeeAmount   = new BigDecimal( 0 );
		BigDecimal employerAmount   = new BigDecimal( 0 );

		BigDecimal hoursAdmissible;
		BigDecimal salaryAdmissible;
 		if ( Region.getC_Region_ID() != CalculNet.QUEBEC &&  RegionResidence.getC_Region_ID() == CalculNet.QUEBEC  )
 		{
 			hoursAdmissible  = Calcul_tax.get_matrix_hoursAdmissible  ( s_MatrixTaxB, Deduction_Param.getP_Column_Adm_ID() );
 			salaryAdmissible = Calcul_tax.get_matrix_salaryAdmissible ( s_MatrixTaxB, Deduction_Param.getP_Column_Adm_ID() );
 		}
 		else
 		{
 			hoursAdmissible  = Calcul_tax.get_matrix_hoursAdmissible  ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
 			salaryAdmissible = Calcul_tax.get_matrix_salaryAdmissible ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
 		}

//		BigDecimal hoursAdmissible  = Calcul_tax.get_matrix_hoursAdmissible  ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );
//		BigDecimal salaryAdmissible = Calcul_tax.get_matrix_salaryAdmissible ( s_Matrix, Deduction_Param.getP_Column_Adm_ID() );

		if ( salaryAdmissible.compareTo( new BigDecimal( 0 ) ) == 0)
			return ;

		calcul_quebec_tax(ctx, master, EffectIn, trxName, true );
	
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
*/	
	
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
	public static int get_federal_tax_id( Timestamp EffectIn) throws Exception
	{
		int taxID = 0;

//		log.debug ("Load_tax_parameter Begin");
		
		String query = "Select P_Tax_ID FROM P_Tax ";
        query = MRole.getDefault(Env.getCtx(), false).addAccessSQL (query, "P_Tax", true, false);	// fully qualidfied - RO 

		query = query + " And P_Tax.EffectIn = ( Select max( a.EffectIn ) FROM P_Tax a WHERE a.EffectIn < " + DB.TO_DATE( EffectIn ) +  ")";

//		log.debug ("Load_tax_parameter Sql : " + query  );

//        System.out.println( "DEBUG Load_tax_parameter Sql : " + query );

		
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
	
	
	// getProvincialRateID
	private  int getProvincialRateID( P_Employee Employee, BigDecimal salary, Logger log, String trxName ) throws Exception
	{
		int P_Tax_ID = get_federal_tax_id( Period.getPayDate() );
		int P_Tax_Provincial_Rate_ID = 0;
		
		BigDecimal sal = salary.abs();
		
		//2011.10.26 replace Employee.getTaxation_Region_ID par Payment.getTaxation_Region_ID
		//
		
		String sqlServerQuery = "Select P_Tax_Provincial_Rate_ID FROM P_Tax_Provincial_Rate, P_Tax, P_Tax_Provincial "
			+ "WHERE P_Tax.P_Tax_ID = P_Tax_Provincial.P_Tax_ID "
			+ "AND P_Tax_Provincial.P_Tax_Provincial_ID = P_Tax_Provincial_Rate.P_Tax_Provincial_ID "
			+ "AND P_Tax.P_Tax_ID = " + P_Tax_ID
			+ "AND " + sal + " >= Limit_Min "
			+ "AND ( " + sal + " < Limit_Max or Limit_Max is null or Limit_Max = 0) "
			+ "AND P_Tax_Provincial.C_Region_ID = " + Payment.getTaxation_Region_ID()
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
	
	public String getTP1015Exonerated ( Properties ctx, P_Employee Employee, P_Period Period, String lineNo, String trxName  ) throws Exception
	{
		String ret = "";
		String query = "Select P_Form_Template_Detail.Value, " 
				  + "P_Employee_Form_Detail.FormFieldAmount, " 
				  + "P_Employee_Form_Detail.FormFieldText, " 
				  + "P_Employee_Form_Detail.FormTypeField " 
				  + " From P_Form_Template_Detail "
				  + " INNER JOIN P_Employee_Form_Detail ON P_Form_Template_Detail.P_Form_Template_Detail_ID = P_Employee_Form_Detail.P_Form_Template_Detail_ID "
				  + " INNER JOIN P_Employee_Form ON P_Employee_Form.P_Employee_Form_ID = P_Employee_Form_Detail.P_Employee_Form_ID "
			      + " Where P_Employee_Form.P_Employee_ID = " + Employee.getP_Employee_ID()
			      + "  AND P_Employee_Form.P_Form_Template_ID = " + P_Form_Template.getForm_Template_ID("TP1015")  
			      + "  AND P_Employee_Form.IsActive = 'Y' "
	     		  + "  AND P_Employee_Form.EffectIn = ( Select Max( dat.EffectIn ) From P_Employee_Form dat WHERE dat.P_Employee_ID = P_Employee_Form.P_Employee_ID AND dat.P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID AND EffectIn <= " + DB.TO_DATE( Period.getPayDate())  + " and ( EFFECTTO is null or EFFECTTO > " + DB.TO_DATE( Period.getPayDate()) + "))"
	     		  + "  AND P_Form_Template_Detail.Value = " + DB.TO_STRING( lineNo )
				  ;

		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (query, trxName);

		ResultSet rs = pstmt.executeQuery (); 
		if (rs.next ())
		{
			ret = rs.getString("FormFieldText");
		}
		rs.close ();
		pstmt.close ();
		pstmt = null;

		if (ret == null)
			ret = "";
		
		return ret; 
	}

	//
    //	 readTP1015
    //		 
	public  void readTP1015 (Properties ctx,  P_Employee Employee, Hashtable<String, Object> s_TP1015, String trxName ) throws Exception
	{
		s_TP1015.clear();

		//2011.10.26 replace Employee.getTaxation_Region_ID par Payment.getTaxation_Region_ID
		//
		
		MRegion Region = new MRegion( ctx, Payment.getTaxation_Region_ID(), trxName );
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
			ExoneratedHeatContribution = false;
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
				// La ligne 24 Cochez la case ci-contre si vous demandez d’être exonéré de la retenue à la source de la contribution santé.
				//
				if ( s_lineNo.equals("22") && rs.getString("FormFieldText")!= null && rs.getString("FormFieldText").toUpperCase().trim().equals("X"))
				{
					ExoneratedHeatContribution = true;
				}
				
				//
				// La ligne 22 est un champs text qui dit si l'employé ne doit pas payer d'impot.
				//
				if ( s_lineNo.equals("20") && rs.getString("FormFieldText")!= null && rs.getString("FormFieldText").toUpperCase().trim().equals("X"))
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
		total5 = total5.add( getTP1015( ctx, Employee, s_TP1015, "5", trxName ));
		s_lineNo = String.valueOf( 7 );
		s_TP1015.put( s_lineNo , total5 );
		
        // Ajout Stéphane Morin 2007-08-23 Déduction pour emploi utilisé paramètre au lieu de hardcoding
		// il change aux années depuis quelques temps
		BigDecimal HRate = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "HRate");   // Read global Federal Parameter
		s_lineNo = String.valueOf( 98 );
		if ( HRate != null )
			s_TP1015.put( s_lineNo ,  HRate );
		BigDecimal HMax = (BigDecimal)taxTable.get( Region.getC_Region_ID() + "-" + "HMax");   // Read global Federal Parameter
		s_lineNo = String.valueOf( 99 );
		if ( HMax != null)
			s_TP1015.put( s_lineNo ,  HMax );
	
	    return ;
		
	}	// readTP1015

	//
    //	 getTP1015
    //		  
	public static BigDecimal getTP1015 ( Properties ctx, P_Employee Employee, Hashtable<String, Object> s_TP1015, String lineNo, String trxName  )
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

	
	/*
	 * GetInclusivePayment_QC : Calcul de l'impôt sur les gains rétroactifs pour la 
	 * province de Québec (impôts du Québec).
	 * 
	 */
	
	public  void getInclusivePayment_Qc(CalculNet master) throws Exception
	{

		// Bornes pour le calcul au Québec
		 BigDecimal[] INT_QC_1 = {Env.ZERO, new BigDecimal(5000)};
		 BigDecimal[] INT_QC_2 = {new BigDecimal(5000.01),INFINITE};
		 BigDecimal tauxProv = null;
		 BigDecimal divisor = new BigDecimal(100);
		 BigDecimal hoursAdmissible;
		BigDecimal salaryAdmissible;
		BigDecimal employeeAmount;
		 
		 m_master = master;
		 // 2009.04.22

		 Payment = master.getP_Payment();
		 Employee = master.getP_Employee();
		 Deduction_Param = master.getP_Deduction_Param();

 		 if ( Employee.isStatusIndian() )		// 2009.04.27
		    return ;

		
		 salaryAdmissible = Payment.getGrossEarnings(); //this.master.get_matrix_salaryAdmissible(Deduction_Param.getP_Column_Adm_ID());
		 hoursAdmissible = m_master.get_matrix_hoursAdmissible(Deduction_Param.getP_Column_Adm_ID());
		
		
		if ( salaryAdmissible != null && salaryAdmissible.compareTo(Env.ZERO) != 0 )
		{
			// Calcul du taux au provincial (Québec seulement)
			if (isIncludeInIntervall(INT_QC_1,salaryAdmissible))
				tauxProv = TAUX_RETRO_QC_PROV_1;
			else if (isIncludeInIntervall(INT_QC_2,salaryAdmissible))
				tauxProv = TAUX_RETRO_QC_PROV_2;
			else 
				tauxProv = TAUX_RETRO_QC_PROV_2;
			
			employeeAmount = salaryAdmissible.multiply(tauxProv.divide(divisor));

			m_master.setEmployeeAmount(employeeAmount.setScale( 2, BigDecimal.ROUND_HALF_UP  ));
			m_master.setSalaryAdmissible(salaryAdmissible);
			m_master.setHoursAdmissible(hoursAdmissible);
			
		}
	}
	
	/*
	 * isIncludeInIntervall : Détermine si un montant est situé entre deux bornes
	 * 
	 */
	private static boolean isIncludeInIntervall(BigDecimal tab[], BigDecimal tauxAdmisible)
	{
		boolean ret = false;
		if (tab[1] == INFINITE)
		{
			if (tab[0].compareTo(tauxAdmisible) == 0 || tab[0].compareTo(tauxAdmisible) == 1)
				return true;
		}
		else
		{
			if ((tab[0].compareTo(tauxAdmisible) == 0 || tab[0].compareTo(tauxAdmisible) == -1) &&(tab[1].compareTo(tauxAdmisible) >=0 ))
				ret = true;
		}
		return ret;
	}

	// getProvincialRateID
		private int getProvincialRateID( P_Payment Payment, P_Employee Employee, P_Period Period, BigDecimal salary, Logger log, String trxName ) throws Exception
		{
			
			int P_Tax_Id = get_federal_tax_id( Period.getPayDate() );
			int P_Tax_Provincial_Rate_ID = 0;
			
			BigDecimal sal = salary.abs();
			
			//2011.10.26 replace Employee.getTaxation_Region_ID par Payment.getTaxation_Region_ID
			//
			
			String sqlServerQuery = "Select P_Tax_Provincial_Rate_ID FROM P_Tax_Provincial_Rate, P_Tax, P_Tax_Provincial "
				+ " WHERE P_Tax.P_Tax_ID = P_Tax_Provincial.P_Tax_ID "
				+ " AND P_Tax_Provincial.P_Tax_Provincial_ID = P_Tax_Provincial_Rate.P_Tax_Provincial_ID "
//				+ "AND P_Tax.EffectIn = ( Select max( EffectIn ) FROM P_Tax a WHERE a.EffectIn < " + DB.TO_DATE( Period.getPayDate() ) + " AND a.AD_Client_ID = " + Employee.getAD_Client_ID() + " ) "
				+ " AND P_Tax.P_Tax_ID = " + P_Tax_Id
				+ " AND " + sal + " >= Limit_Min "
				+ " AND ( " + sal + " < Limit_Max or Limit_Max is null or Limit_Max = 0) "
				+ " AND P_Tax_Provincial.C_Region_ID = " + Payment.getTaxation_Region_ID()
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
		
	
}
