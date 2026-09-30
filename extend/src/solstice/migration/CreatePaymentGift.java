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
package solstice.migration;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.Compiere;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;
import org.compiere.util.CLogger;

import solstice.model.IBookletTimeSheet;
import solstice.model.P_Assignment_Param;
import solstice.model.P_Deduction;
import solstice.model.P_Deduction_Param;
import solstice.model.P_Employee;
import solstice.model.P_Employee_Deduction;
import solstice.model.P_Employee_Deduction_Excep;
import solstice.model.P_Employee_LongTermLeave;
import solstice.model.P_Frequency;
import solstice.model.P_Gain;
import solstice.model.P_Payment;
import solstice.model.P_Payment_Deduction;
import solstice.model.P_Payment_Group;
import solstice.model.P_Period;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Time_Sheet_Detail;
import solstice.model.P_Time_Sheet_Error;
import solstice.process.TimeGeneration;
import solstice.utils.PgiUtil;

public class CreatePaymentGift {

	Properties ctx = Env.getCtx();
	/**	Logger							*/
	protected CLogger			log = CLogger.getCLogger (getClass());

	/** Transaction			*/
	private String trxName = null;

	/**	Big Decimal 0	 */
	static final public java.math.BigDecimal ZERO = new java.math.BigDecimal(0.0);

	
	/**
	 * 	Generate PO Class
	 * 	@param AD_Table_ID table id
	 * 	@param directory directory with \ or / at the end.
	 * 	@param packageName package name
	 * @throws SQLException
	 */
	public CreatePaymentGift () 
	{
		Env.setContext( ctx, "#AD_Client_ID", 1000004);
		Env.setContext( ctx, "#AD_Org_ID", 0);
		int count = 0;
		

		// Test de performance
/*		String sql = " select p_employee.p_employee_id, p_employee.value, p_employee.name, p_employee.ISACTIVE, p_deduction.p_deduction_id, P_Period.P_Period_ID "
+ "     , P_Period.Name, p_payment.value, p_payment.PAYMENTTYPEDOC, 0 salaire_eligible, round( employee_part * 1.181 ,2 ) - employer_part as employerAmount" 
+ " from p_payment "
+ " inner join p_employee on p_employee.p_employee_id = p_payment.p_employee_id " 
+ " inner join p_period on p_period.p_period_id = p_payment.p_period_id"   
+ " inner join p_deduction on p_deduction.value = '03'" 
 
+ " inner join P_PAYMENT_DEDUCTION on P_PAYMENT_DEDUCTION.p_payment_id = p_payment.p_payment_id and p_payment_deduction.p_deduction_id = p_deduction.p_deduction_id "
+ "  where p_period.p_year_id = 1000122 "
+ "  and p_payment.ad_org_id in ( 1000011, 1000012 ) "
+ "  and exists( select 1 from p_payment_deduction m where m.p_payment_id = p_payment.p_payment_id and m.p_deduction_id in ( select p_deduction_id from p_deduction where value = 'D53' ) ) "
+ " order by p_employee.name, p_period.name " 
;
*/
//		String sql = "Select * FROM tmp_assurance_ia_retro_g7g8 order by p_employee_id "
//				String sql = "Select * FROM tmp_assurance_ia_retro order by p_employee_id "
	String sql = "SELECT * FROM RV_Gift" ;//"Select * FROM tmp_ae order by p_employee_id "

				 ; 
		PreparedStatement pstmt = null;
		try
		{ 
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			int t_employee_id = 0;

			P_Time_Sheet TimeSheet = null; 
			P_Payment Payment = null;
			
			log.log(Level.INFO,"Debut - " );
			while (rs.next())
			{
				P_Employee Employee = P_Employee.get( ctx, rs.getInt("P_Employee_ID"), null);
				P_Period Period = P_Period.getOpenPeriodWithPaymentGroup(ctx, Employee.getP_Payment_Group_ID(), null);
				
//				if ( TimeSheet == null )
				if ( Employee.getP_Employee_ID() != t_employee_id )	
				{
					t_employee_id = Employee.getP_Employee_ID();
					
					TimeSheet =	new P_Time_Sheet( getCtx (), -1, trxName);
					String value = DB.getDocumentNo (Employee.getAD_Client_ID() , TimeSheet.get_TableName(), TimeSheet.get_TrxName() );

					TimeSheet.setAD_Org_ID( Employee.getAD_Org_ID());
					TimeSheet.setP_Department_ID( Employee.getP_Department_ID());
					TimeSheet.setC_Activity_ID( Employee.getC_Activity_ID());
					
					TimeSheet.setValue( Period.getName() + "-" + value.substring( 3, 7 ) );

					//		TimeSheet.setDescription( "");
					TimeSheet.setIsComplete( false );
					TimeSheet.setP_Employee_ID( Employee.getP_Employee_ID());
					TimeSheet.setP_Frequency_ID( Period.getP_Frequency_ID() );
					TimeSheet.setP_Year_ID( Period.getP_Year_ID());
					TimeSheet.setP_Period_ID( Period.getP_Period_ID());
					TimeSheet.setPaymentType( Employee.getPaymentType() );
			        TimeSheet.setSheetType( P_Time_Sheet.SHEETTYPE_Complementary );
					TimeSheet.setTimeSheetStatus( P_Time_Sheet.TIMESHEETSTATUS_Initial );
					TimeSheet.setP_Distribution_Booklet_ID(Employee.getP_Distribution_Booklet_ID( Period.getEndDate()));
					TimeSheet.setP_Distribution_ID( Employee.getP_Distribution_ID());
					TimeSheet.setP_Job_Title_ID( Employee.getP_Job_Title_ID());
					TimeSheet.setP_Job_Type_ID( Employee.getP_Job_Type_ID());
					TimeSheet.setP_Occupation_Group_ID( Employee.getP_Occupation_Group_ID());
					TimeSheet.setP_Collective_Labour_Agr_ID( Employee.getP_Collective_Labour_Agr_ID());
					TimeSheet.setIsError( false );
					TimeSheet.setP_Payment_Group_ID( Employee.getP_Payment_Group_ID()  );
//					+ 2011.11.16 ajout numéro employeur et lieu de travail sur la feuille de paie
				    TimeSheet.setP_Employer_ID(  Employee.getP_Employer_ID());
				    TimeSheet.setP_Workplace_ID( Employee.getP_Workplace_ID());
			//-	2011.11.16 			    

			        TimeSheet.setPayDate( Period.getPayDate());

					TimeSheet.save();		
					
					GenerationGift( ctx, TimeSheet, rs, null );
					
/*					Payment = new P_Payment( Env.getCtx (), -1, trxName);
					Payment.setValue( Payment.getP_Payment_ID() + ""  );
					Payment.setP_Employee_ID( TimeSheet.getP_Employee_ID ());
					
					Payment.setAD_Org_ID( TimeSheet.getAD_Org_ID());
					Payment.setP_Department_ID( TimeSheet.getP_Department_ID());
					Payment.setC_Activity_ID( TimeSheet.getC_Activity_ID());
					
					Payment.setIsActive( true );
					Payment.setP_Period_ID( Period.getP_Period_ID () );
					Payment.setP_Year_ID( Period.getP_Year_ID() );
					Payment.setP_Frequency_ID ( Period.getP_Frequency_ID() );
					
					Payment.setPayDate( TimeSheet.getPayDate());
					
					Payment.setPaymentType( TimeSheet.getPaymentType() );
					Payment.setP_Employer_ID( TimeSheet.getP_Employer_ID() );
					Payment.setP_Workplace_ID( TimeSheet.getP_Workplace_ID());
					Payment.setP_Payment_Group_ID( TimeSheet.getP_Payment_Group_ID() );
					
					Payment.setPaymentTypeDoc( TimeSheet.getSheetType() );
					Payment.setAnnualSalary( Employee.getAnnualSalary( Period.getEndDate() ));
					//+ 2011.11.07 Si l'employé n'a pas d'affectation principal alors on génère une erreur.
					if ( Payment.getAnnualSalary() == null )
					{
						P_Time_Sheet_Error.NewMessage( TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé de salaire annuel de base pour l'employé, vérifier si l'employé a une d'affectation principal" , null);
					}
					//- 2011.11.07
					
					Payment.setP_Job_Title_ID( TimeSheet.getP_Job_Title_ID());
					Payment.setP_Job_Type_ID(TimeSheet.getP_Job_Type_ID());
					Payment.setP_Occupation_Group_ID( TimeSheet.getP_Occupation_Group_ID());
					Payment.setP_Distribution_Booklet_ID( TimeSheet.getP_Distribution_Booklet_ID());
					Payment.setP_Distribution_ID( TimeSheet.getP_Distribution_ID());
					Payment.setValue( TimeSheet.getValue() );
					Payment.setIsTransfer(false);
					Payment.setIsReconciled(false);
					Payment.setIsSelfFinancedLeave(false);
//					Payment.setTaxation_Region_ID( Employee.getTaxation_Region_ID());
					Payment.setTaxation_Region_ID(TimeSheet.getTaxation_Region_ID());
					Payment.setP_Workplace_ID( Employee.getP_Workplace_ID());
					
	                Payment.save(  );

					TimeSheet.setTimeSheetStatus( P_Time_Sheet.TIMESHEETSTATUS_Calculated );

	                TimeSheet.setP_Payment_ID( Payment.getP_Payment_ID() );
	                TimeSheet.save(  );
*/
					
				}



			}
			log.log(Level.INFO,"Fin - " );
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE,"CreatePayment - " , e);
		}

		System.out.println("Generated = " + count);

	}
	
	private IBookletTimeSheet TimeSheet;
	String m_trxName = null; // Trx.createTrxName();
	private int	m_inserted = 0;

	private P_Period m_Period;
	private P_Period Period;
	private P_Employee Employee;
	private P_Assignment_Param AssignmentParam;
	private P_Frequency Frequency;
	private P_Payment_Group PaymentGroup ;
	private BigDecimal Hourly_Rate;
	private boolean GenerationDetail; 


	public String GenerationGift( Properties ctx, P_Time_Sheet _TimeSheet, ResultSet rs, String TrxName ) throws Exception
	{
    	String trxName = null;

    	
        P_Period period = P_Period.get( Env.getCtx(), _TimeSheet.getP_Period_ID(), trxName);
        
		//+ 2010.05.04 
        // stop this procedure if the period is closed
        if (period.getPeriodStatus().equals(P_Period.PERIODSTATUS_Closed) || period.getPeriodStatus().equals(P_Period.PERIODSTATUS_PermanentlyClosed) )
    		return "@inserted@ " + m_inserted;
		//- 2010.05.04 

        //+ 2011.03.01 stop this procedure if the period is a adjustment Period
        if ( period.isAdjustmentPeriod() )
    		return "@inserted@ " + m_inserted;
        //- 2011.03.01 

		if ( ! _TimeSheet.getSheetType().equals( P_Time_Sheet.SHEETTYPE_Complementary )) 
    		return "@inserted@ " + m_inserted;

        
        P_Frequency frequency = P_Frequency.get( Env.getCtx(), period.getP_Frequency_ID(), trxName);
        //TODO read PaymentGroup
/*        if ( frequency.getNumberOfPeriod() == 24 )
        	DisplayDetail = 1;	
        else
        	DisplayDetail = 0;
  */      
        m_trxName = TrxName;


		m_Period = P_Period.get( getCtx (), _TimeSheet.getP_Period_ID() , m_trxName);
		Period = P_Period.get( getCtx (), _TimeSheet.getP_Period_ID() , m_trxName);
		Frequency = P_Frequency.get( getCtx(), Period.getP_Frequency_ID(), m_trxName);

//		Connaitre la période
		
		this.GenerationDetail = false;

		PaymentGroup = P_Payment_Group.get( getCtx(), _TimeSheet.getP_Payment_Group_ID(), null );
		
		Trx trx = null;
	    try {
	        m_trxName = null; // Trx.createTrxName("tsg_"+rs.getInt("P_Employee_ID"));
	        if( trx != null ) trx.start();

			Employee = P_Employee.get( getCtx (), _TimeSheet.getP_Employee_ID(), m_trxName );
			log.log (Level.INFO,"Generation - Employee " + Employee.toString() );
	        
			if ( _TimeSheet.getSheetType().equals( P_Time_Sheet.SHEETTYPE_Complementary )) 
			{

				// A chaque employe on doit initialiser Hourlyrate et assignmentparam
				// Sinon la programmation assume le premier taux trouvé
				Hourly_Rate = null ;
				AssignmentParam = null ;
				
				Hourly_Rate = null ;

				P_Time_Sheet_Detail TimeSheetDetail = new P_Time_Sheet_Detail( Env.getCtx(), -1,null);

//				TimeSheetDetail.setParent_ID( TimeSheet.getRecord_ID() );
				TimeSheetDetail.setP_Time_Sheet_ID( _TimeSheet.getP_Time_Sheet_ID() );
				
				TimeSheetDetail.setP_Assignment_ID( rs.getInt("P_Assignment_ID") );
				TimeSheetDetail.setP_Period_ID( Period.getP_Period_ID() );
				
				// 2023-06-04 Spécial pour Krispy
//				if ( Employee.getP_Collective_Labour_Agr_ID() == 1000006 )  // NDISTRIBUTEURS CORPORATIFS   
//				if ( Employee.isCustomFieldYesNo01() )	
//					TimeSheetDetail.setP_Period_ID( getLastPeriod() );
				
				P_Gain Gain = P_Gain.get(Env.getCtx(), rs.getInt("P_Gain_ID"), null);
				
				TimeSheetDetail.setP_Gain_ID( Gain.getP_Gain_ID() );
//				if ( Timecode != null && Timecode.getP_Timecode_ID() != 0)
//					TimeSheetDetail.setP_Timecode_ID( Timecode.getP_Timecode_ID());
/*				
				if ( startTime != null )
					TimeSheetDetail.setStartTime( startTime.substring(10,16) );
				if ( endTime != null )
					TimeSheetDetail.setEndTime( endTime.substring(10,16) ); 
*/
				
				TimeSheetDetail.setP_Schedule_ID( rs.getInt("P_Schedule_ID") );
				TimeSheetDetail.setWeekIndex( 1 );
				TimeSheetDetail.setOriginTime( "PER" );
				TimeSheetDetail.setDay( rs.getTimestamp("DAY"));
				TimeSheetDetail.setStartDate(rs.getTimestamp("StartDate"));
				TimeSheetDetail.setEndDate(rs.getTimestamp("EndDate"));

/*				if ( Activity != null )
					TimeSheetDetail.setC_Activity_ID( Activity.getC_Activity_ID());
				
				if ( SelfFinancedLeave != null)
					TimeSheetDetail.setSalaryPercentage( SelfFinancedLeave.getSalaryPercentage() );

				if ( EmployeeLongTermLeave != null )
					TimeSheetDetail.setP_Employee_LongTermLeave_ID( EmployeeLongTermLeave.getP_Employee_LongTermLeave_ID() );
	*/			
				TimeSheetDetail.setDayQty( rs.getBigDecimal("Dayqty") );
				
//				P_Payment_Group PaymentGroup = P_Payment_Group.get( getCtx(), TimeSheet.getP_Payment_Group_ID(), null );
				PaymentGroup = P_Payment_Group.get( getCtx(), _TimeSheet.getP_Payment_Group_ID(), null );
				
		//TODO		ici ajoute un paramètre.
				if ( PaymentGroup.getTimeSheetModel().equals( P_Payment_Group.TIMESHEETMODEL_Summary ))
				{
					// Stephane Morin 16 septembre 2007
					// Ajout de || Hourly_Rate.compareTo( ZERO ) == 0 pour pallier au return de la fonction 
					// ParticularSheet.getHourly_Rate() qui retourne zero si jamais elle trouve null!!
					

					
					if ( Hourly_Rate == null || Hourly_Rate.compareTo( ZERO ) == 0 )
					{
						//2009.11.11 taux le plus récent si l'employé change de taux en cours de période.
						//Hourly_Rate = AssignmentParam.getHourlyRate( Gain, GenStartDate );
						Hourly_Rate = AssignmentParam.getHourlyRate( Gain, rs.getTimestamp("EndDate") );
					}
					

					//2021-06-02 
					if ( Gain != null && Gain.getType_Rate() != null && Gain.getType_Rate().equals(  P_Gain.TYPE_RATE_FixedRate ) )
					{
						TimeSheetDetail.setHourly_Rate(Gain.getHourly_Rate()) ;
					}
					else {
						TimeSheetDetail.setHourly_Rate(Hourly_Rate) ;	
					}
				}
				
				TimeSheetDetail.save();
		    	//
		    	// Les heures non rémunéré coupe le prime sur quantité ainsi que les jours en CSST.
		    	// La différence avec l'autre méthode de coupe, c'est que le jour coupé est dans la même semaine, 
		    	// mais pas nécessairement le même jours.
		        //
//				log.log (Level.INFO,"Generation - Employee " + Employee.toString() + " Cut_Bonus " );
//		        Cut_Bonus();
			}
			log.log (Level.INFO,"Generation Bonus - Employee " + Employee.toString() + " Commit " );

		    if( trx != null ) trx.commit();
	    }
	    catch( Exception e ) {
			log.log (Level.SEVERE, "TimeGeneration", e);
			if( trx != null ) trx.rollback();
	    }
	    finally {
	        if( trx != null ) trx.close();
	    }
		
		return "Enregistrements créés " + m_inserted;
		
		
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
		System.out.println("CreatePayment   $Revision: 1.4 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		
		int count = 0;
		new CreatePaymentGift();
		count++;
		System.out.println("Generated = " + count);

	}	//	main
	
}
