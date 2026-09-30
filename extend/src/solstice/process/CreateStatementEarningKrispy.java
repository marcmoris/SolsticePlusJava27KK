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
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import solstice.model.P_Assignment;
import solstice.model.P_Assignment_Param;
import solstice.model.P_Credits;
import solstice.model.P_Deduction;
import solstice.model.P_Employee;
import solstice.model.P_Gain;
import solstice.model.P_Language;
import solstice.model.P_Notice_Bank_Deposit;
import solstice.model.P_Payment;
import solstice.model.P_Period;
import solstice.model.P_Statement_Earning;
import solstice.model.P_Statement_Earning_Credits;
import solstice.model.P_Statement_Earning_Deduction;
import solstice.model.P_Statement_Earning_Gain;
import solstice.model.P_Statement_Earning_Notice;
import solstice.model.P_Statement_Earning_Taxable_Benefits;
import solstice.model.P_Taxable_Benefit;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Time_Sheet_Error;
import solstice.model.P_Payment.PaymentException;

public class CreateStatementEarningKrispy {

	private P_Payment Payment = null ;
	
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (CreateStatementEarning.class);

	public void createStatementEarning( P_Payment _Payment )
	{
		Payment = _Payment;
		
        // On supprime d'abord les anciennes valeurs dans Statement Earning s'il y a lieu
        String sql = "delete from P_Statement_Earning where P_Payment_ID = " + Payment.getP_Payment_ID();
        DB.executeUpdate(sql, null);
        
        //On remplit les tables
        try
        {
	        int statementEarningId = this.fillStatementEarning();

			s_log.log (Level.INFO,"fillStatementEarningCredits" );

	        this.fillStatementEarningCredits(statementEarningId);
			s_log.log (Level.INFO,"fillStatementEarningDeduction" );
	        this.fillStatementEarningDeduction(statementEarningId);
			s_log.log (Level.INFO,"fillStatementEarningDeductionNonAnnual" );
	        this.fillStatementEarningDeductionNonAnnual(statementEarningId);
			s_log.log (Level.INFO,"fillStatementEarningGain" );
	        this.fillStatementEarningGain(statementEarningId);
			s_log.log (Level.INFO,"fillStatementEarningTaxB" );
	        this.fillStatementEarningTaxableBenefits(statementEarningId);
			s_log.log (Level.INFO,"--------------" );
			
			P_Statement_Earning Statement_Earning = new P_Statement_Earning( Env.getCtx(), statementEarningId, null );
	    	P_Notice_Bank_Deposit note = P_Notice_Bank_Deposit.get(Env.getCtx(), Statement_Earning.getP_Period_ID(), Statement_Earning.getAD_Org_ID(), Statement_Earning.getP_Department_ID(), Statement_Earning.getC_Activity_ID(), Statement_Earning.getP_Employee_ID(), null);
	    	if ( note != null )
	    	{
	    		String s_note = note.getNotice();

	    		P_Statement_Earning_Notice Notice = new  P_Statement_Earning_Notice( Env.getCtx(), -1, null);
	    		Notice.setP_Statement_Earning_ID(statementEarningId  );
	    		Notice.setNotice( s_note );
	    		Notice.save();
	    	}
        }
        catch (PaymentException e)
        {
        	s_log.log(Level.SEVERE,"P_Payment After Save",e);
        }
	}
	
	private P_Employee Employee;
    private P_Language language;
    
    private P_Statement_Earning statementEarning = null;
    
	private int fillStatementEarning( ) throws PaymentException
	{
	    statementEarning = new P_Statement_Earning(Env.getCtx(), -1, Payment.get_TrxName());
	    
	    statementEarning.setAD_Org_ID( Payment.getAD_Org_ID());
	    statementEarning.setP_Employee_ID(Payment.getP_Employee_ID());
	    statementEarning.setP_Period_ID(Payment.getP_Period_ID());
	    statementEarning.setPrePrintedNo(Payment.getPrePrintedNo());
	    statementEarning.setGrossEarningsPeriod(Payment.getGrossEarnings());
	    statementEarning.setTaxableBenefitsPeriod(Payment.getTotalTaxableBenefit());
	    statementEarning.setDeductionPeriod(Payment.getTotalDeduction());
	    statementEarning.setPayDate( Payment.getPayDate());
	    statementEarning.setP_Payment_ID( Payment.getP_Payment_ID());

	    Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), Payment.get_TrxName());

		s_log.log (Level.INFO,"Create StatementEarning - Employee : " + Employee.getValue() + " " + Employee.getName() );
	    
		P_Assignment Assignment = null;
		if ( Employee.GetAssignmentPrincipal( ) == 0) {
			s_log.log (Level.SEVERE," Employee sans affectation principal : " + Employee.getValue() + " " + Employee.getName() );
		    P_Period Period = P_Period.get( Env.getCtx(), Payment.getP_Period_ID(), Payment.get_TrxName());
			P_Assignment_Param param = Employee.getLastEmployeeAssignment( Employee.getP_Employee_ID(), Period.getStartDate(), Payment.get_TrxName());
			Assignment = P_Assignment.get( Env.getCtx(), param.getP_Assignment_ID() , null );
		}	
		else
			Assignment = P_Assignment.get( Env.getCtx(), Employee.GetAssignmentPrincipal( ), null );
	    statementEarning.setP_Post_ID( Assignment.getP_Post_ID());
	    P_Period period = P_Period.get(Env.getCtx(), Payment.getP_Period_ID(), Payment.get_TrxName());
	    statementEarning.setPayFinishing(period.getEndDate());

	    //2023-09-18 - ajout l'information des dates.
	    statementEarning.setStartDate(period.getStartDate());
	    statementEarning.setEndDate( period.getEndDate() );
	    
	    // Paie de vacance ou employé avec paie décaler.
    	if ( Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_HolidayPayment) ||  Employee.isCustomFieldYesNo01() )	
    	{
    		String sqlsp = " select P_Payment.P_Payment_ID "
    				+ ", ( select MIN( P_Period.startdate) from P_Payment_Gain "
    				+ "     inner join P_PERIOD on P_PERIOD.P_PERIOD_ID = P_Payment_Gain.P_PERIOD_ID "
    				+ "     WHERE P_Payment_Gain.P_PAYMENT_ID = P_PAYMENT.P_PAYMENT_ID "
    				+ "	 ) startdate "
    				+ ", ( select MAX( P_Period.enddate) from P_Payment_Gain "
    				+ "     inner join P_PERIOD on P_PERIOD.P_PERIOD_ID = P_Payment_Gain.P_PERIOD_ID "
    				+ "     WHERE P_Payment_Gain.P_PAYMENT_ID = P_PAYMENT.P_PAYMENT_ID "
    				+ "	 ) enddate "
    				+ "from p_payment "
    				+ " WHERE P_Payment.P_Payment_ID  = " + Payment.getP_Payment_ID()
    			;
    	    try
    	    {
    	        PreparedStatement stmt = DB.prepareStatement(sqlsp, Payment.get_TrxName());
    	        ResultSet rs = stmt.executeQuery();
    	        
    	        if(rs.next())
    	        {
    	    	    statementEarning.setStartDate( rs.getTimestamp( "startdate"));
    	    	    statementEarning.setEndDate( rs.getTimestamp("enddate" ) );
    	        }
    	        rs.close();
    	        stmt.close();
    	    }
    	    catch(Exception e)
    	    {
    			s_log.log(Level.SEVERE," Create Statement Earning - " + sqlsp, e);
    	    }        
    		
    	}

	    
	    // P_Distribution_ID
	    language = P_Language.get( Env.getCtx(), Employee.getP_Language_ID(), Payment.get_TrxName());
	    

	    if ( Employee.getP_Distribution_ID() == 0 )
	    {
		    P_Time_Sheet TimeSheet = P_Time_Sheet.GetWithPaymentID( Env.getCtx(), Payment.getP_Payment_ID(), Payment.get_TrxName());
            P_Time_Sheet_Error.NewMessage( TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "L'employé n'a pas de code de distribution", Payment.get_TrxName() );
            TimeSheet.setIsError(true);
            TimeSheet.save();
            return 0;
	    }
	    	
        statementEarning.setP_Distribution_ID( Payment.getP_Distribution_ID() );
        statementEarning.setC_Activity_ID( Payment.getC_Activity_ID());

        statementEarning.setP_Department_ID( Payment.getP_Department_ID());

        
        
        P_Period Period = P_Period.get( Env.getCtx(), Payment.getP_Period_ID(), Payment.get_TrxName());
        
	    //Heures payées et non payées
	    String sql
	    = " select"

	        + "   (select isnull(sum(payment.GrossEarnings), 0)"
	        + "   from P_Payment payment inner join P_Period period"
	        + "   on payment.P_Period_ID = period.P_Period_ID"
	        + "   where payment.P_Payment_ID = " + Payment.getP_Payment_ID()
	        + "   and period.StartDate <= (select StartDate from P_Period where P_Period_ID = " + Payment.getP_Period_ID() + ")) as salaireBrutPeriodique,"
	        
	        + "   (select isnull(sum(payment.GrossEarnings), 0)"
	        + "   from P_Payment payment inner join P_Period period"
	        + "   on payment.P_Period_ID = period.P_Period_ID"
	        + "   where payment.P_Employee_ID = " + Payment.getP_Employee_ID()
	        + "   and period.P_Year_ID = " + Payment.getP_Year_ID() 
	        + "   and period.StartDate <= " + DB.TO_DATE(Period.getStartDate())
	        + "   and payment.P_Year_ID = " + Payment.getP_Year_ID() + ") as salaireBrutCumulatif,"
	        
	        + "   (select isnull(sum(paymentGain.QuantityCalc), 0)"
	        + "   from P_Period period inner join (P_Payment_Gain paymentGain inner join (P_Gain gain inner join P_Gain_Family gainFamily"
	        + "   on gain.P_Gain_Family_ID = gainFamily.P_Gain_Family_ID)"
	        + "   on paymentGain.P_Gain_ID = gain.P_Gain_ID)"
	        + "   on period.P_Period_ID = paymentGain.P_Period_ID"
	        + "   where paymentGain.P_Payment_ID = " + Payment.getP_Payment_ID()
	        + "   and period.P_Year_ID = " + Payment.getP_Year_ID() 
	        + "   and period.StartDate <= " + DB.TO_DATE(Period.getStartDate())
	        + "   and gainFamily.P_Gain_Family_ID = 105"
	        + "   and exists ("
	        + "     select 1"
	        + "     from P_Gain_GainInfo inner join P_GainInfo"
	        + "     on P_Gain_GainInfo.P_GainInfo_ID = P_GainInfo.P_GainInfo_ID"
	        + "     where P_GainInfo.Value = 'EXCHNR'"
	        + "     and P_Gain_GainInfo.P_Gain_ID = gain.P_Gain_ID"
	        + "     and To_Consider = 'N'"
	        + "   )) as hnr,"

	        + "   (select isnull(sum(paymentGain.QuantityCalc), 0)"
	        + "   from P_Period period inner join (P_Payment_Gain paymentGain inner join (P_Gain gain inner join P_Gain_Family gainFamily"
	        + "   on gain.P_Gain_Family_ID = gainFamily.P_Gain_Family_ID)"
	        + "   on paymentGain.P_Gain_ID = gain.P_Gain_ID)"
	        + "   on period.P_Period_ID = paymentGain.P_Period_ID"
	        + "   where paymentGain.P_Payment_ID = " + Payment.getP_Payment_ID()
	        + "   and period.P_Year_ID = " + Payment.getP_Year_ID() 
	        + "   and period.StartDate <= " + DB.TO_DATE(Period.getStartDate())
	        + "   and gainFamily.P_Gain_Family_ID not in (105, 106)) as hr,"

	        + "   (select isnull(sum(paymentGain.QuantityCalc), 0)"
	        + "   from P_Period period inner join (P_Payment_Gain paymentGain inner join (P_Gain gain inner join P_Gain_Family gainFamily"
	        + "   on gain.P_Gain_Family_ID = gainFamily.P_Gain_Family_ID)"
	        + "   on paymentGain.P_Gain_ID = gain.P_Gain_ID)"
	        + "   on period.P_Period_ID = paymentGain.P_Period_ID"
	        + "   where paymentGain.P_Year_ID = " + Payment.getP_Year_ID()
	        + "   and paymentGain.P_Employee_ID = " + Payment.getP_Employee_ID()
	        + "   and period.P_Year_ID = " + Payment.getP_Year_ID() 
	        + "   and period.StartDate <= " + DB.TO_DATE(Period.getStartDate())
	        + "   and gainFamily.P_Gain_Family_ID = 105" 
	        + "   and exists ("
	        + "     select 1"
	        + "     from P_Gain_GainInfo inner join P_GainInfo"
	        + "     on P_Gain_GainInfo.P_GainInfo_ID = P_GainInfo.P_GainInfo_ID"
	        + "     where P_GainInfo.Value = 'EXCHNR'"
	        + "     and P_Gain_GainInfo.P_Gain_ID = gain.P_Gain_ID"
	        + "     and To_Consider = 'N'"
	        + "   )) as hnrc,"

	        + "   (select isnull(sum(paymentGain.QuantityCalc), 0)"
	        + "   from P_Period period inner join (P_Payment_Gain paymentGain inner join (P_Gain gain inner join P_Gain_Family gainFamily"
	        + "   on gain.P_Gain_Family_ID = gainFamily.P_Gain_Family_ID)"
	        + "   on paymentGain.P_Gain_ID = gain.P_Gain_ID)"
	        + "   on period.P_Period_ID = paymentGain.P_Period_ID"
	        + "   where paymentGain.P_Year_ID = " + Payment.getP_Year_ID()
	        + "   and paymentGain.P_Employee_ID = " + Payment.getP_Employee_ID()
	        + "   and period.P_Year_ID = " + Payment.getP_Year_ID() 
	        + "   and period.StartDate <= " + DB.TO_DATE(Period.getStartDate())
	        + "   and gainFamily.P_Gain_Family_ID not in (105, 106)) as hrc,"
	        
	        + "   (select isnull(sum(payment.TotalTaxableBenefit), 0)"
	        + "   from P_Payment payment inner join P_Period period"
	        + "   on payment.P_Period_ID = period.P_Period_ID"
	        + "   where payment.P_Year_ID = " + Payment.getP_Year_ID()
	        + "   and period.P_Year_ID = " + Payment.getP_Year_ID() 
	        + "   and period.StartDate <= " + DB.TO_DATE(Period.getStartDate())
	        + "   and payment.P_Employee_ID = " + Payment.getP_Employee_ID() + ") as TBCum,"
	        
	        + "   (select isnull(sum(payment.TotalDeduction), 0)"
	        + "   from P_Payment payment inner join P_Period period"
	        + "   on payment.P_Period_ID = period.P_Period_ID"
	        + "   where payment.P_Year_ID = " + Payment.getP_Year_ID()
	        + "   and period.P_Year_ID = " + Payment.getP_Year_ID() 
	        + "   and period.StartDate <= " + DB.TO_DATE(Period.getStartDate())
	        + "   and payment.P_Employee_ID = " + Payment.getP_Employee_ID() + ") as DCum";

	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, Payment.get_TrxName());
	        ResultSet rs = stmt.executeQuery();
	        
	        if(rs.next())
	        {
	            statementEarning.setGrossEarningsCum(rs.getBigDecimal("salaireBrutCumulatif"));
	            statementEarning.setGrossEarningsPeriod(rs.getBigDecimal("salaireBrutPeriodique"));
	            statementEarning.setNotPaidHourPer(rs.getBigDecimal("hnr"));
	            statementEarning.setPaidHourPer(rs.getBigDecimal("hr"));
	            statementEarning.setNotPaidHourCum(rs.getBigDecimal("hnrc"));
	            statementEarning.setPaidHourCum(rs.getBigDecimal("hrc"));
	            statementEarning.setTaxableBenefitsCum(rs.getBigDecimal("TBCum"));
	            statementEarning.setDeductionCum(rs.getBigDecimal("DCum"));
	        }
	        else
	        {
	            rs.close();
	            stmt.close();
				s_log.log(Level.SEVERE," Create Statement Earning - Erreur dans le chargement des heures " );
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch(Exception e)
	    {
			s_log.log(Level.SEVERE," Create Statement Earning - " + sql, e);
	    }
	    
	  //+ 2012.01.10 on prend le taux horaire en fonction de la date de fin de la période	    
//	    P_Assignment_Param Assignment_Param = P_Assignment_Param.get( Env.getCtx(), Employee.GetPrincipalAssignment_Param( Period.getEndDate()), Payment.get_TrxName());
	    P_Assignment_Param Assignment_Param = Employee.getAssignment_Param( Period.getEndDate() , Payment.get_TrxName());
//- 2012.01.10 	    
	    
	    BigDecimal HRate = Env.ZERO;
	    BigDecimal HSal = Env.ZERO;
	    
	    if ( Assignment_Param == null ) {
		    P_Time_Sheet TimeSheet = P_Time_Sheet.GetWithPaymentID( Env.getCtx(), Payment.getP_Payment_ID(), Payment.get_TrxName());
            P_Time_Sheet_Error.NewMessage( TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "L'employé n'a pas d'affectation principal", Payment.get_TrxName() );
//            TimeSheet.setIsError(true);
//            TimeSheet.save();
	    } else
	    {
		    HRate = Assignment_Param.getHourly_Rate( );
		    HSal = Assignment_Param.getAnnual_Salary( );
	    	
	    }
	    
        statementEarning.setP_Job_Title_ID(Assignment_Param.getP_Job_Title_ID());
        statementEarning.setP_Salary_Class_ID(Assignment_Param.getP_Salary_Class_ID());
        statementEarning.setP_Salary_Scale_Detail_ID(Assignment_Param.getP_Salary_Scale_Detail_ID());
        if (Assignment_Param.getRemuneration_Method().equals( P_Assignment_Param.REMUNERATION_METHOD_Hourly ))
        {
	        statementEarning.setHourly_Rate(HRate);
        }
        else
        {
            statementEarning.setHourly_Rate(HSal);	            	
        }

 //       statementEarning.setAnnualSalary(HSal);	            	

        statementEarning.setRemuneration_Method(Assignment_Param.getRemuneration_Method());
        statementEarning.setC_Activity_ID( Payment.getC_Activity_ID());

/*	    
	    sql
	    = "select P_Assignment_Param.P_Job_Title_ID, P_Assignment_Param.P_Salary_Class_ID, P_Assignment_Param.P_Salary_Scale_Detail_ID, ("
			+ " case P_Assignment_Param.Remuneration_Method"
			+ "     when 'hourly' then P_Assignment_Param.Hourly_Rate"
			+ "     else P_Assignment_Param.Annual_Salary"
			+ "   end"
			+ " ) as Rate, P_Assignment_Param.Remuneration_Method, "
			+ "   P_Assignment.P_Post_ID "
			+ " from P_Assignment inner join P_Assignment_Param"
			+ " on P_Assignment.P_Assignment_ID = P_Assignment_Param.P_Assignment_ID"
			+ " where AssignmentType = 'P'"
			+ " and P_Employee_ID = " + Payment.getP_Employee_ID()
			+ " and  P_Assignment_Param.EffectIn = ("
			+ "   select max(EffectIn)"
			+ "   from P_Assignment_Param"
			+ "   where P_Assignment_ID = P_Assignment.P_Assignment_ID"
			+ "   and EffectIn <= " + DB.TO_DATE(Period.getEndDate()) 
			+ " )";
	    
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, Payment.get_TrxName());
	        ResultSet rs = stmt.executeQuery();
	        
	        if(rs.next())
	        {
	            statementEarning.setP_Post_ID( rs.getInt("P_Post_ID" ));
	            statementEarning.setP_Job_Title_ID(rs.getInt("P_Job_Title_ID"));
	            statementEarning.setP_Salary_Class_ID(rs.getInt("P_Salary_Class_ID"));
	            statementEarning.setP_Salary_Scale_Detail_ID(rs.getInt("P_Salary_Scale_Detail_ID"));
//	            statementEarning.setHourly_Rate(rs.getBigDecimal("Rate"));
	            if (rs.getString("Remuneration_Method").equals("hourly"))
	            {
	    	        statementEarning.setHourly_Rate(HRate);
	            }
	            else
	            {
		            statementEarning.setHourly_Rate(HSal);	            	
	            }
	            statementEarning.setRemuneration_Method(rs.getString("Remuneration_Method"));
	    	    statementEarning.setP_Post_ID ( rs.getInt("P_Post_ID"));

	        }
	        rs.close();
	        stmt.close();
	        
	    }
	    catch(Exception e)
	    {
			s_log.log(Level.SEVERE," Create Statement Earning - " + sql, e);
	    }
*/
	    sql = "select isnull(sum(payment.GrossEarnings), 0)"
	        + " from P_Payment payment inner join P_Period period"
	        + " on payment.P_Period_ID = period.P_Period_ID"
	    	+ " where payment.P_Year_ID = " + Payment.getP_Year_ID()
	    	+ " and payment.P_Employee_ID = " + Payment.getP_Employee_ID()
	        + " and period.P_Year_ID = " + Payment.getP_Year_ID() 
	    	+ " and period.StartDate <= " + DB.TO_DATE(Period.getStartDate())
	    	;
	    
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, Payment.get_TrxName());
	        ResultSet rs = stmt.executeQuery();
	        
	        if ( rs.next())
	        	statementEarning.setGrossEarningsCum(rs.getBigDecimal(1));
	        rs.close();
	        stmt.close();
	    }
	    catch(Exception e)
	    {
			s_log.log(Level.SEVERE," Create Statement Earning - " + sql, e);
	    }

	    BigDecimal FederalTaxationCredit = Env.ZERO;
	    BigDecimal ProvincialTaxationCredit = Env.ZERO;
	    BigDecimal FederalBaseAmount = Env.ZERO;
	    BigDecimal ProvincialBaseAmount = Env.ZERO;
	    try
	    {

		    sql = "select top 1 taxFederal.Amount FederalTaxationCredit"
	            + "   from P_Tax_Federal_TD1 taxFederal"
	            + "   where taxFederal.Value = 'TD1.01' "
	            + "   and taxFederal.P_Tax_ID = ("
	            + "     select top 1 P_Tax_ID"
	            + "     from P_Tax"
	            + "     where EffectIn <= " + DB.TO_DATE(Period.getPayDate())
	            + "     order by EffectIn desc )";
	            
	        PreparedStatement stmt = DB.prepareStatement(sql, Payment.get_TrxName());
	        ResultSet rs = stmt.executeQuery();
	        if ( rs.next() )
		        FederalTaxationCredit = rs.getBigDecimal("FederalTaxationCredit");
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
			s_log.log(Level.SEVERE," Create Statement Earning - " + sql, e);
	    }

	    try
	    {
	        sql = "select top 1 taxProvincialTD1.Amount ProvincialTaxationCredit"
		        + "   from P_Tax_Provincial_TD1 taxProvincialTD1 inner join P_Tax_Provincial taxProvincial"
		        + "   on taxProvincialTD1.P_Tax_Provincial_ID = taxProvincial.P_Tax_Provincial_ID"
		        + "   where taxProvincialTD1.Value = 'TP1015.01' "
		        + "   and taxProvincial.C_Region_ID = (select top 1 Taxation_Region_ID from P_Employee where P_Employee_ID = " + Payment.getP_Employee_ID() + ")"
		        + "   and taxProvincial.P_Tax_ID = ("
		        + "     select top 1 P_Tax_ID"
		        + "     from P_Tax"
		        + "     where EffectIn <= " + DB.TO_DATE(Period.getPayDate())
		        + "     order by EffectIn desc )" ;

	            
	        PreparedStatement stmt = DB.prepareStatement(sql, Payment.get_TrxName());
	        ResultSet rs = stmt.executeQuery();
	        if ( rs.next() )
	        	ProvincialTaxationCredit = rs.getBigDecimal("ProvincialTaxationCredit");
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
			s_log.log(Level.SEVERE," Create Statement Earning - " + sql, e);
	    }
	
	    try
	    {
	        sql = " select P_Employee_Form_Detail.FormFieldAmount BaseAmount"
	        + "   from P_Employee_Form_Detail inner join P_Employee_Form "
	        + "   on P_Employee_Form_Detail.P_Employee_Form_ID = P_Employee_Form.P_Employee_Form_ID"
	        + "   where P_Employee_Form.P_Employee_ID = " + Payment.getP_Employee_ID()
	        + "   and P_Employee_Form.P_Form_Template_ID = ( select P_Form_Template_ID from P_Form_Template where value = 'TD1')"
	        + "   and P_Employee_Form_Detail.P_Form_Template_Detail_ID = ( select P_Form_Template_Detail_ID from P_Form_Template_Detail where P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID and value = '1')"
	        + "   and EffectIn = ( Select Max( EffectIn ) From P_Employee_Form m Where m.P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID and m.P_Employee_ID = " + Payment.getP_Employee_ID() + " And m.EffectIn < (select top 1 PayDate from P_Period where P_Period_ID = " + Payment.getP_Period_ID() + "))"
	        ;
	        PreparedStatement stmt = DB.prepareStatement(sql, Payment.get_TrxName());
	        ResultSet rs = stmt.executeQuery();
	        if ( rs.next() )
	        	FederalBaseAmount = rs.getBigDecimal("BaseAmount");
	        
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
			s_log.log(Level.SEVERE," Create Statement Earning - " + sql, e);
	    }


	    try
	    {
	        sql = " select P_Employee_Form_Detail.FormFieldAmount BaseAmount"
	        + "   from P_Employee_Form_Detail inner join P_Employee_Form "
	        + "   on P_Employee_Form_Detail.P_Employee_Form_ID = P_Employee_Form.P_Employee_Form_ID"
	        + "   where P_Employee_Form.P_Employee_ID = " + Payment.getP_Employee_ID()
	        + "   and P_Employee_Form.P_Form_Template_ID = ( select P_Form_Template_ID from P_Form_Template where value = 'TP1015')"
	        + "   and P_Employee_Form_Detail.P_Form_Template_Detail_ID = ( select P_Form_Template_Detail_ID from P_Form_Template_Detail where P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID and value = '1')"
	        + "   and EffectIn = ( Select Max( EffectIn ) From P_Employee_Form m Where m.P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID and m.P_Employee_ID = " + Payment.getP_Employee_ID() + " And m.EffectIn < (select top 1 PayDate from P_Period where P_Period_ID = " + Payment.getP_Period_ID() + "))"
	        ;
	        PreparedStatement stmt = DB.prepareStatement(sql, Payment.get_TrxName());
	        ResultSet rs = stmt.executeQuery();
	        if ( rs.next() )
	        	ProvincialBaseAmount = rs.getBigDecimal("BaseAmount");
	        rs.close();
	        stmt.close();
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE," Create Statement Earning - " + sql, e);
		}

	    try
	    {
		    sql
		    = " select"
		        + "   (select sum(isnull(P_Employee_Form_Detail.FormFieldAmount, 0))"
		        + "   from P_Employee_Form_Detail inner join P_Employee_Form "
		        + "   on P_Employee_Form_Detail.P_Employee_Form_ID = P_Employee_Form.P_Employee_Form_ID"
		        + "   where P_Employee_Form.P_Employee_ID = " + Payment.getP_Employee_ID()
		        + "   and P_Employee_Form.P_Form_Template_ID = ( select P_Form_Template_ID from P_Form_Template where value = 'TD1')"
		        + "   and P_Employee_Form_Detail.P_Form_Template_Detail_ID <> ( select P_Form_Template_Detail_ID from P_Form_Template_Detail where P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID and value = 'A')"
		        + "   and EffectIn = ( Select Max( EffectIn ) From P_Employee_Form m Where m.P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID and m.P_Employee_ID = " + Payment.getP_Employee_ID() + " And m.EffectIn < (select top 1 PayDate from P_Period where P_Period_ID = " + Payment.getP_Period_ID() + "))"
               // Magnetho 247 Ne pas inclure l'impot additionnel
		        + "   and P_Employee_Form_Detail.P_Form_Template_Detail_ID <> ( select P_Form_Template_Detail_ID from P_Form_Template_Detail where P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID and value = 'L')"
		        + "   ) as FederalTaxationCreditEmp,"
	
		        + "   (select sum(isnull(P_Employee_Form_Detail.FormFieldAmount, 0))"
		        + "   from P_Employee_Form_Detail inner join P_Employee_Form "
		        + "   on P_Employee_Form_Detail.P_Employee_Form_ID = P_Employee_Form.P_Employee_Form_ID"
		        + "   where P_Employee_Form.P_Employee_ID = " + Payment.getP_Employee_ID()
		        + "   and P_Employee_Form.P_Form_Template_ID = ( select P_Form_Template_ID from P_Form_Template where value = 'TP1015')"
		        + "   and P_Employee_Form_Detail.P_Form_Template_Detail_ID <> ( select P_Form_Template_Detail_ID from P_Form_Template_Detail where P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID and value = 'A')"
		        + "   and EffectIn = ( Select Max( EffectIn ) From P_Employee_Form m Where m.P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID and m.P_Employee_ID = " + Payment.getP_Employee_ID() + " And m.EffectIn < (select top 1 PayDate from P_Period where P_Period_ID = " + Payment.getP_Period_ID() + "))"
	               // Magnetho 247 Ne pas inclure l'impot additionnel
		        + "   and P_Employee_Form_Detail.P_Form_Template_Detail_ID <> ( select P_Form_Template_Detail_ID from P_Form_Template_Detail where P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID and value = '11')"
		        + "   ) as ProvincialTaxationCreditEmp,"
	
		        + "   (select isnull(sum(P_Employee_Form_Detail.FormFieldAmount), 0)"
		        + "   from P_Employee_Form_Detail inner join P_Employee_Form "
		        + "   on P_Employee_Form_Detail.P_Employee_Form_ID = P_Employee_Form.P_Employee_Form_ID"
		        + "   where P_Employee_Form.P_Employee_ID = " + Payment.getP_Employee_ID()
		        + "   and P_Employee_Form.P_Form_Template_ID = ( select P_Form_Template_ID from P_Form_Template where value = 'TD1')"
		        + "   and P_Employee_Form_Detail.P_Form_Template_Detail_ID = ( select P_Form_Template_Detail_ID from P_Form_Template_Detail where P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID and value = 'A')"
		        + "   and EffectIn = ( Select Max( EffectIn ) From P_Employee_Form m Where m.P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID And m.P_Employee_ID = " + Payment.getP_Employee_ID() + " And m.EffectIn < (select top 1 PayDate from P_Period where P_Period_ID = " + Payment.getP_Period_ID() + "))"
	               // Magnetho 247 Ne pas inclure l'impot additionnel
		        + "   and P_Employee_Form_Detail.P_Form_Template_Detail_ID <> ( select P_Form_Template_Detail_ID from P_Form_Template_Detail where P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID and value = 'L')"
		        + "   ) as FederalTaxationReduction,"
	
		        + "   (select isnull(sum(P_Employee_Form_Detail.FormFieldAmount), 0)"
		        + "   from P_Employee_Form_Detail inner join P_Employee_Form "
		        + "   on P_Employee_Form_Detail.P_Employee_Form_ID = P_Employee_Form.P_Employee_Form_ID"
		        + "   where P_Employee_Form.P_Employee_ID = " + Payment.getP_Employee_ID()
		        + "   and P_Employee_Form.P_Form_Template_ID = ( select P_Form_Template_ID from P_Form_Template where value = 'TP1015')"
		        + "   and P_Employee_Form_Detail.P_Form_Template_Detail_ID = ( select P_Form_Template_Detail_ID from P_Form_Template_Detail where P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID and value = 'A')"
		        + "   and EffectIn = ( Select Max( EffectIn ) From P_Employee_Form m Where m.P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID And P_Employee_ID = " + Payment.getP_Employee_ID() + " And EffectIn < (select top 1 PayDate from P_Period where P_Period_ID = " + Payment.getP_Period_ID() + "))"
	               // Magnetho 247 Ne pas inclure l'impot additionnel
		        + "   and P_Employee_Form_Detail.P_Form_Template_Detail_ID <> ( select P_Form_Template_Detail_ID from P_Form_Template_Detail where P_Form_Template_ID = P_Employee_Form.P_Form_Template_ID and value = '11')"
		        + "   ) as ProvincialTaxationReduction"
	
		        ;

	        PreparedStatement stmt = DB.prepareStatement(sql, Payment.get_TrxName());
	        ResultSet rs = stmt.executeQuery();

	        if(rs.next())
	        {

	        	BigDecimal FederalTaxationCreditEmp = rs.getBigDecimal("FederalTaxationCreditEmp");
	        	if ( FederalBaseAmount == null )
	        		FederalTaxationCreditEmp = FederalTaxationCreditEmp.add(FederalTaxationCredit);
	        	
	        	BigDecimal ProvincialTaxationCreditEmp = rs.getBigDecimal("ProvincialTaxationCreditEmp");
	        	if ( ProvincialBaseAmount == null )
	        		ProvincialTaxationCreditEmp = ProvincialTaxationCreditEmp.add(ProvincialTaxationCredit);
	        
	        	if ( FederalTaxationCreditEmp != null )
	        		statementEarning.setFederalTaxationCredit(FederalTaxationCreditEmp);
	        	else
	        		statementEarning.setFederalTaxationCredit( FederalTaxationCredit );

	        	if ( ProvincialTaxationCreditEmp != null )
	        		statementEarning.setProvincialTaxationCredit(ProvincialTaxationCreditEmp);
	        	else
	        		statementEarning.setProvincialTaxationCredit( ProvincialTaxationCredit );
	        	
	            statementEarning.setFederalTaxationReduction(rs.getBigDecimal("FederalTaxationReduction"));
	            statementEarning.setProvincialTaxationReduction(rs.getBigDecimal("ProvincialTaxationReduction"));
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
			s_log.log(Level.SEVERE," Create Statement Earning - " + sql, e);
	    }
	    
	    // C_Activity_ID
/*	    
	    if ( statementEarning.getC_Activity_ID() == 0)
	    {
		    sql
		    = "select post.C_Activity_ID "
		        + " from P_Employee employee inner join (P_Assignment assignment inner join P_Post post"
		        + " on assignment.P_Post_ID = post.P_Post_ID)"
		        + " on employee.P_Employee_ID = assignment.P_Employee_ID"
		        + " where assignment.AssignmentType = 'P'"
		        + " and employee.P_Employee_ID = " + Payment.getP_Employee_ID();
		    
		    try
		    {
		        PreparedStatement stmt = DB.prepareStatement(sql, Payment.get_TrxName());
		        ResultSet rs = stmt.executeQuery();
		        
		        if(rs.next())
		        {
		            statementEarning.setC_Activity_ID(rs.getInt(1));
		        }
		        
		        rs.close();
		        stmt.close();
		    }
		    catch(Exception e)
		    {
				s_log.log(Level.SEVERE," Create Statement Earning - " + sql, e);
		    }
	    	
	    }
*/	    
	    if(!statementEarning.save())
			s_log.log(Level.SEVERE," Create Statement Earning - fillStatementEarning()::statementEarning.save()" );
	    return statementEarning.getP_Statement_Earning_ID();
	}

	private P_Statement_Earning_Gain statementEarningGain = null;
	private P_Gain gain = null;
	
	public void fillStatementEarningGain(int statementEarningId) throws PaymentException
	{
		// Cette requète sort les heures et les montants périodiques pour
		// chaque regroupement de gain de la période incluant tout ceux
		// qui ont figuré dans les relevés antérieurs à l'année du paiement
		// courrant.

		P_Period period = P_Period.get( Env.getCtx(), Payment.getP_Period_ID(), null);

		String sql = "select P_Gain.P_Gain_ID, " 
			+ " sum(P_Payment_Gain.QuantityCalc) as QuantityCalc, " 
			+ " sum(P_Payment_Gain.AmountCalc) as AmountCalc "
			+ " from P_Payment " 
			+ " Inner join P_Payment_Gain on P_Payment.P_Payment_ID = P_Payment_Gain.P_Payment_ID "
//			+ " inner join P_Gain on P_Gain.P_Gain_ID = P_Payment_Gain.P_Gain_ID "
			
			+ " Inner join AD_TreeNode  ON AD_TreeNode.AD_Tree_ID = 1000003 AND AD_TreeNode.node_ID = P_Payment_Gain.P_Gain_ID "
			+ " Inner join P_Gain on P_Gain.P_Gain_ID = CASE WHEN AD_TreeNode.parent_ID = 0 THEN P_Payment_Gain.P_Gain_ID ELSE AD_TreeNode.parent_ID END "
			
			+ " where P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID()
			+ "   and P_Payment.P_Year_ID = " + period.getP_Year_ID()
			+ "  AND P_Gain.Value not in ( '0019') " // 2023-08-16 ne pas imprimer sur l'avis de dépôt.
			+ " AND (P_Gain.P_Gain_Family_ID not in ( 105 ) or P_Gain.Value in ( '0028', '0028G') )"
			+ " Group by P_Gain.P_Gain_ID   "
			+ " Order by max(P_Gain.Value)"
			;
		
		
		try
		{
			PreparedStatement stmt = DB.prepareStatement(sql, null);
			ResultSet rs = stmt.executeQuery();
			
			int count=0;
			
			while(rs.next())
			{
/*
				String sqlper = "select P_Gain.P_Gain_ID, " 
//					+ " P_Payment_Gain.Hourly_Rate, "
					+ " case when P_Gain.P_UOM_ID = 101 THEN 0 ELSE P_Payment_Gain.Hourly_Rate END AS Hourly_Rate, "
					+ " sum(P_Payment_Gain.QuantityCalc) as UnitaryPeriod, " 
					+ " sum(P_Payment_Gain.AmountCalc) as AmountPeriod  "
					+ " , max( P_Payment_Gain.multiplyrate ) as multiplyrate "
					+ " from P_Payment " 
					+ " Inner join P_Payment_Gain on P_Payment.P_Payment_ID = P_Payment_Gain.P_Payment_ID AND P_Payment_Gain.P_Gain_ID = " + rs.getInt("P_Gain_ID")
//					+ " Inner Join P_Gain on P_Payment_Gain.P_Gain_ID = P_Gain.P_Gain_ID "

					+ " Inner join AD_TreeNode  ON AD_TreeNode.AD_Tree_ID = 1000003 AND AD_TreeNode.node_ID = P_Payment_Gain.P_Gain_ID "
					+ " Inner join P_Gain on P_Gain.P_Gain_ID = CASE WHEN AD_TreeNode.parent_ID = 0 THEN P_Payment_Gain.P_Gain_ID ELSE AD_TreeNode.parent_ID END "
					
					+ " where P_Payment.P_Payment_ID = " + Payment.getP_Payment_ID()
					+ "  AND P_Gain.Value not in ( '0019') " // 2023-08-16 ne pas imprimer sur l'avis de dépôt.
//					+ " AND P_Gain.P_Gain_Family_ID not in ( 105 ) "
					+ " AND (P_Gain.P_Gain_Family_ID not in ( 105 ) or P_Gain.Value in ( '0028', '0028G') )"
					
//					+ " Group by P_Payment_Gain.P_Gain_ID, P_Gain.VALUE, P_Payment_Gain.Hourly_Rate " 
					+ " Group by P_Gain.P_Gain_ID, P_Gain.VALUE, case when P_Gain.P_UOM_ID = 101 THEN 0 ELSE P_Payment_Gain.Hourly_Rate END  " 
					;
*/	

				// 2026-09-16 OR P_Gain.isSummary = 'Y'
				
				String sqlper = "select P_gain.P_Gain_ID, " 
						+ " case when P_GAIN_FAMILY_ID = 106 THEN case when isnull( P_Payment_Gain.Hourly_Rate,0) = 0 then P_Payment_Gain.MULTIPLYRATE else P_Payment_Gain.Hourly_Rate end  when (P_Gain.P_UOM_ID = 101 ) THEN 0 ELSE P_Payment_Gain.Hourly_Rate END AS Hourly_Rate, "
						+ " sum(P_Payment_Gain.QuantityCalc) as UnitaryPeriod, " 
						+ " sum(P_Payment_Gain.AmountCalc) as AmountPeriod  "
						+ " , max( P_Payment_Gain.multiplyrate ) as multiplyrate "
						+ " from P_Payment " 
						+ " Inner join P_Payment_Gain on P_Payment.P_Payment_ID = P_Payment_Gain.P_Payment_ID " 
//						AND P_Payment_Gain.P_Gain_ID = " + rs.getInt("P_Gain_ID")
						+ " Inner join AD_TreeNode  ON AD_TreeNode.AD_Tree_ID = 1000003 AND AD_TreeNode.node_ID = P_Payment_Gain.P_Gain_ID "
						+ " Inner join P_Gain on P_Gain.P_Gain_ID = CASE WHEN AD_TreeNode.parent_ID = 0 THEN P_Payment_Gain.P_Gain_ID ELSE AD_TreeNode.parent_ID END "
						+ " where P_Payment.P_Payment_ID = " + Payment.getP_Payment_ID()
						+ " and (AD_TreeNode.Parent_ID = " + rs.getInt("P_Gain_ID") + " or ( AD_TreeNode.Parent_ID = 0 AND P_Payment_Gain.P_Gain_ID = " + rs.getInt("P_Gain_ID") + " ) ) "
//						+ "  AND P_Gain.Value not in ( '0019') " // 2023-08-16 ne pas imprimer sur l'avis de dépôt.
//						+ " AND (P_Gain.P_Gain_Family_ID not in ( 105 ) or P_Gain.Value in ( '0028', '0028G') )"
// OR P_Gain.isSummary = 'Y'						
						+ " Group by P_gain.P_Gain_ID, case when P_GAIN_FAMILY_ID = 106 THEN case when isnull( P_Payment_Gain.Hourly_Rate,0) = 0 then P_Payment_Gain.MULTIPLYRATE else P_Payment_Gain.Hourly_Rate end when (P_Gain.P_UOM_ID = 101 ) THEN 0 ELSE P_Payment_Gain.Hourly_Rate END  " 
						;
						
//, P_Gain.VALUE				
				
				PreparedStatement stmtper = DB.prepareStatement(sqlper, null);
				ResultSet rsper = stmtper.executeQuery();
				
				int countper=0;

				gain = P_Gain.get( Env.getCtx(), rs.getInt( "P_Gain_ID") , null);
				
				// S'il y a plusieurs taux horaire dans la période traité
				while(rsper.next())
				{
					countper ++;

					statementEarningGain = new P_Statement_Earning_Gain(Env.getCtx(), -1, null);
					statementEarningGain.setP_Statement_Earning_ID(statementEarningId);
					
					statementEarningGain.setDescription( gain.getTrlName( language.getValue() ) );
					if ( countper == 1 )
					{
//						if ( gain.getP_Gain_Family_ID() != 106 && gain.getP_Gain_Family_ID() != 105)
						if ( gain.getP_Gain_Family_ID() != 105)
							statementEarningGain.setUnitaryCum(rs.getBigDecimal("QuantityCalc"));

						if ( gain.getValue().equals("0028") || gain.getValue().equals("0028G"))
							statementEarningGain.setUnitaryCum(rs.getBigDecimal("QuantityCalc"));
						
						if ( gain.getP_Method_Gain_ID() == 118 )
							statementEarningGain.setUnitaryCum(new BigDecimal(0));
						
						statementEarningGain.setAmountcumul(rs.getBigDecimal("AmountCalc"));
					}
					else
					{
						statementEarningGain.setUnitaryCum(Env.ZERO);
						statementEarningGain.setAmountcumul(Env.ZERO);
					}
						
//				    if ( gain.getP_Gain_Family_ID() != 106 && gain.getP_Gain_Family_ID() != 105) 
				    if ( gain.getP_Gain_Family_ID() != 105) 
						statementEarningGain.setUnitaryPeriod(rsper.getBigDecimal("UnitaryPeriod"));

				    if ( gain.getValue().equals("0028") || gain.getValue().equals("0028G"))
						statementEarningGain.setUnitaryPeriod(rsper.getBigDecimal("UnitaryPeriod"));
						
					if ( gain.getP_Method_Gain_ID() == 118 )
						statementEarningGain.setUnitaryPeriod(new BigDecimal(0));

				    
					statementEarningGain.setAmountPeriod(rsper.getBigDecimal("AmountPeriod"));
//					if ( gain.getP_UOM_ID() != 101 && gain.getP_Gain_Family_ID() != 106) // différent de montant. Prime pas de taux horaire
					if ( gain.getP_UOM_ID() != 101 ) // différent de montant. Prime pas de taux horaire
					{
						statementEarningGain.setHourly_Rate( rsper.getBigDecimal("Hourly_Rate"));
						// 2023-11-28 Taux horaire au pour le temps supp.
						BigDecimal multiplyrate = rsper.getBigDecimal("multiplyrate");
						if ( gain.getP_Gain_Family_ID() != 106 )
						{
							if ( multiplyrate != null && multiplyrate.compareTo(Env.ZERO) != 0)
								statementEarningGain.setHourly_Rate( (rsper.getBigDecimal("Hourly_Rate").multiply(multiplyrate)).setScale(2, BigDecimal.ROUND_HALF_UP) );
						}
					}
					else
						statementEarningGain.setHourly_Rate( null );
					statementEarningGain.setP_Gain_ID(rs.getInt( "P_Gain_ID"));
					
					if (( Payment.getPaymentType().equals( P_Payment.PAYMENTTYPE_Cheque) && count <= 14 ) || Payment.getPaymentType().equals( P_Payment.PAYMENTTYPE_Deposit) )
					{
						count++;
						statementEarningGain.save();
					}

				}
				// S'il existe seulement un cumulatif
				if ( countper == 0 )
				{
					statementEarningGain = new P_Statement_Earning_Gain(Env.getCtx(), -1, null);
					statementEarningGain.setP_Statement_Earning_ID(statementEarningId);
					
					statementEarningGain.setDescription( gain.getTrlName( language.getValue() ) );
//					if ( gain.getP_Gain_Family_ID() != 106 && gain.getP_Gain_Family_ID() != 105 )
					if (  gain.getP_Gain_Family_ID() != 105 )
						statementEarningGain.setUnitaryCum(rs.getBigDecimal("QuantityCalc"));
					
				    if ( gain.getValue().equals("0028") || gain.getValue().equals("0028G"))
						statementEarningGain.setUnitaryCum(rs.getBigDecimal("QuantityCalc"));
					
					statementEarningGain.setAmountcumul(rs.getBigDecimal("AmountCalc"));

					statementEarningGain.setUnitaryPeriod(Env.ZERO);
					statementEarningGain.setAmountPeriod(Env.ZERO);
					statementEarningGain.setHourly_Rate( null );
					statementEarningGain.setP_Gain_ID(rs.getInt( "P_Gain_ID"));

					if (( Payment.getPaymentType().equals( P_Payment.PAYMENTTYPE_Cheque) && count <= 14 ) || Payment.getPaymentType().equals( P_Payment.PAYMENTTYPE_Deposit) )
					{
						count++;
						statementEarningGain.save();
					}

				}
				
				
				rsper.close();
				stmtper.close();

				
			}
			
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE," Create Statement Earning - " + sql, e);
		}
	}

	private P_Deduction deduction = null;
	private P_Statement_Earning_Deduction statementEarningDeduction = null;

	private void fillStatementEarningDeduction(int statementEarningId) throws PaymentException
	{
		P_Period period = P_Period.get( Env.getCtx(), Payment.getP_Period_ID(), null);

		String sql = "select P_Deduction.P_Deduction_ID, sum(P_Payment_Deduction.Employee_Part) as AmountCum, "
			+ " sum ( case P_Payment.P_Payment_ID when " + Payment.getP_Payment_ID() + " then P_Payment_Deduction.Employee_Part else 0 end ) as AmountPeriod, "
			+ " sum ( case P_Payment.P_Payment_ID when " + Payment.getP_Payment_ID() + " then P_Payment_Deduction.RetrievalAmount else 0 end ) as Balance "
			+ " from P_Payment " 
			+ " Inner join P_Payment_Deduction on P_Payment.P_Payment_ID = P_Payment_Deduction.P_Payment_ID "
			+ " Inner join AD_TreeNode  ON AD_TreeNode.AD_Tree_ID = 1000004 AND AD_TreeNode.node_ID = P_Payment_Deduction.P_Deduction_ID "
			+ " Inner join P_Deduction on P_Deduction.P_Deduction_ID = CASE WHEN AD_TreeNode.parent_ID = 0 THEN P_Payment_Deduction.P_Deduction_ID ELSE AD_TreeNode.parent_ID END "
			
//			+ " inner join P_Deduction on P_Deduction.P_Deduction_ID = P_Payment_Deduction.P_Deduction_ID "
			+ " inner join P_Period on P_Payment.P_Period_ID = P_Period.P_Period_ID	" 
			+ " where P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID()
			+ "   and  P_Payment.P_Year_ID = " + period.getP_Year_ID() 
			+ " and P_Deduction.IsAnnual <> 'N' "
			+ "   and P_Payment.P_Period_ID <= " + period.getP_Period_ID()

			
			//2008-05-29
//			      +	"And ( exists( select 1 from P_Payment_Deduction a where a.p_employee_id = " + Payment.getP_Employee_ID() + " and a.P_Deduction_id = P_Payment_Deduction.P_Deduction_ID and a.P_Year_ID = " + period.getP_Year_ID() + " ))" 
//			      +		")"
			+ " Group by P_Deduction.P_Deduction_ID Order by P_Deduction.P_Deduction_ID";
				try
		{
			PreparedStatement stmt = DB.prepareStatement(sql, null);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
			{
				deduction = P_Deduction.get( Env.getCtx(), rs.getInt( "P_Deduction_ID") , null);

				statementEarningDeduction = new P_Statement_Earning_Deduction(Env.getCtx(), -1, null);
				
				statementEarningDeduction.setP_Statement_Earning_ID(statementEarningId);
				statementEarningDeduction.setP_Deduction_ID(rs.getInt("P_Deduction_ID"));
				statementEarningDeduction.setDescription( deduction.getTrlName( language.getValue() ));
				statementEarningDeduction.setAmountPeriod(rs.getBigDecimal("AmountPeriod"));
				statementEarningDeduction.setAmountcumul(rs.getBigDecimal("AmountCum"));
				statementEarningDeduction.setBalance(rs.getBigDecimal("Balance"));
				statementEarningDeduction.setInsurance(false);
				statementEarningDeduction.setIsAnnual( true);
				
				// 2009.04.11 - ajout de la condution sur la montant de la période
				if(statementEarningDeduction.getAmountPeriod().compareTo(Env.ZERO) != 0 || statementEarningDeduction.getAmountcumul().compareTo( Env.ZERO ) != 0 || statementEarningDeduction.getBalance().compareTo( Env.ZERO ) != 0 ) 
				{
				    statementEarningDeduction.save();
				}

			}
			
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE," Create Statement Earning - " + sql, e);
		}
		
	}

	private void fillStatementEarningDeductionNonAnnual(int statementEarningId) throws PaymentException
	{
		P_Period period = P_Period.get( Env.getCtx(), Payment.getP_Period_ID(), null);

		String sql = "select P_Deduction.P_Deduction_ID, sum(P_Payment_Deduction.Employee_Part) as AmountCum, "
			+ " sum ( case P_Payment.P_Payment_ID when " + Payment.getP_Payment_ID() + " then P_Payment_Deduction.Employee_Part else 0 end ) as AmountPeriod, "
			+ " sum ( case P_Payment.P_Payment_ID when " + Payment.getP_Payment_ID() + " then P_Payment_Deduction.RetrievalAmount else 0 end ) as Balance "
			+ " from P_Payment " 
			+ " Inner join P_Payment_Deduction on P_Payment.P_Payment_ID = P_Payment_Deduction.P_Payment_ID "
			+ " inner join P_Deduction on P_Deduction.P_Deduction_ID = P_Payment_Deduction.P_Deduction_ID "
			+ " inner join P_Period on P_Payment.P_Period_ID = P_Period.P_Period_ID	" 
			+ " where P_Deduction.IsAnnual = 'N' " 
			+ " and P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID()
			+ "   and  ( P_Deduction.IsAnnual = 'N' and P_Period.P_Period_ID >= dbo.FN_PeriodDeductionNonAnnual(P_Payment.P_Employee_ID, P_Payment_Deduction.P_Deduction_ID ) "
			      + " and dbo.FN_EndYearDeductionNonAnnual(P_Payment.P_Employee_ID, P_Payment_Deduction.P_Deduction_ID, P_Period.P_Period_ID ) >= datepart( year, P_Period.EndDate ) " 
			      +	"And ( exists( select 1 from P_Payment_Deduction a where a.p_employee_id = " + Payment.getP_Employee_ID() + " and a.P_Deduction_id = P_Payment_Deduction.P_Deduction_ID and a.P_Year_ID = " + period.getP_Year_ID() + " ))" +
			      		")"
			+ " Group by P_Deduction.P_Deduction_ID Order by P_Deduction.P_Deduction_ID";
				try
		{
			PreparedStatement stmt = DB.prepareStatement(sql, null);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
			{
				deduction = P_Deduction.get( Env.getCtx(), rs.getInt( "P_Deduction_ID") , null);

				statementEarningDeduction = new P_Statement_Earning_Deduction(Env.getCtx(), -1, null);
				
				statementEarningDeduction.setP_Statement_Earning_ID(statementEarningId);
				statementEarningDeduction.setP_Deduction_ID(rs.getInt("P_Deduction_ID"));
				statementEarningDeduction.setDescription( deduction.getTrlName( language.getValue() ));
				statementEarningDeduction.setAmountPeriod(rs.getBigDecimal("AmountPeriod"));
				statementEarningDeduction.setAmountcumul(rs.getBigDecimal("AmountCum"));
				statementEarningDeduction.setBalance(rs.getBigDecimal("Balance"));
				statementEarningDeduction.setInsurance(false);
				statementEarningDeduction.setIsAnnual( false);
				if(statementEarningDeduction.getAmountcumul().compareTo( Env.ZERO ) != 0 || statementEarningDeduction.getBalance().compareTo( Env.ZERO ) != 0 ) 
				{
				    statementEarningDeduction.save();
				}

			}
			
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE," Create Statement Earning - " + sql, e);
		}
		
	}

	
    private P_Taxable_Benefit taxableBenefit = null;
    private P_Statement_Earning_Taxable_Benefits statementEarningTaxableBenefits = null;

	private void fillStatementEarningTaxableBenefits(int statementEarningId) throws PaymentException
	{

		P_Period period = P_Period.get( Env.getCtx(), Payment.getP_Period_ID(), null);

		String sql = "select P_Taxable_Benefit.P_Taxable_Benefit_ID"
			+ ", sum(P_Payment_Taxable_Benefit.Taxable_Benefit_Amount) as AmountCum, "
			+ " sum ( case P_Payment.P_Payment_ID when " + Payment.getP_Payment_ID() + " then P_Payment_Taxable_Benefit.Taxable_Benefit_Amount else 0 end ) as AmountPeriod "
			+ " from P_Payment " 
			+ " Inner join P_Payment_Taxable_Benefit on P_Payment.P_Payment_ID = P_Payment_Taxable_Benefit.P_Payment_ID "
			+ " inner join P_Taxable_Benefit on P_Taxable_Benefit.P_Taxable_Benefit_ID = P_Payment_Taxable_Benefit.P_Taxable_Benefit_ID "
			+ " where P_Payment.P_Employee_ID = " + Payment.getP_Employee_ID()
			+ "   and P_Payment.P_Year_ID = " + period.getP_Year_ID()
			+ "   and P_Payment.P_Period_ID <= " + period.getP_Period_ID()
			+ " Group by P_Taxable_Benefit.P_Taxable_Benefit_ID Order by P_Taxable_Benefit.P_Taxable_Benefit_ID";

	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, Payment.get_TrxName());
	        ResultSet rs = stmt.executeQuery();
	        
	        while(rs.next())
	        {
				taxableBenefit = P_Taxable_Benefit.get( Env.getCtx(), rs.getInt( "P_Taxable_Benefit_ID") , null);

	        	statementEarningTaxableBenefits = new P_Statement_Earning_Taxable_Benefits(Env.getCtx(), -1, Payment.get_TrxName());
	            statementEarningTaxableBenefits.setP_Statement_Earning_ID(statementEarningId);
	            statementEarningTaxableBenefits.setDescription(taxableBenefit.getTrlName( language.getValue() ));
	            statementEarningTaxableBenefits.setAmountPeriod(rs.getBigDecimal("AmountPeriod"));
	            statementEarningTaxableBenefits.setAmountcumul(rs.getBigDecimal("AmountCum"));
	            statementEarningTaxableBenefits.setP_Taxable_Benefit_ID( rs.getInt( "P_Taxable_Benefit_ID") );
	            if(!statementEarningTaxableBenefits.save())
	            {
	                rs.close();
	                stmt.close();
	    			s_log.log(Level.SEVERE," Create Statement Earning - " + sql );
	            }
	        }
            rs.close();
            stmt.close();
	    }
	    catch (Exception e)
	    {
			s_log.log(Level.SEVERE," Create Statement Earning - " + sql, e);
	    }
	}
	
	private P_Statement_Earning_Credits statementEarningCredits = null;
	P_Credits credits= null; 
	
	private void fillStatementEarningCredits(int statementEarningId) throws PaymentException
	{
		P_Period Period = P_Period.get(Env.getCtx(), Payment.getP_Period_ID(), Payment.get_TrxName());

		String sql 
	    ="select max(P_Employee_Credits.P_Employee_Credits_ID) P_Employee_Credits_ID, P_Employee_Credits.P_Credits_ID, P_Employee_Credits.P_Employee_ID, P_Credits.P_UOM_ID, " 
		    + "max(P_UOM.Value) as Unit, " 
		    + "max(P_Credits.Value ) as Value "
			+ " from P_Employee  "
			+ " inner join P_Employee_Credits on P_Employee.P_Employee_ID = P_Employee_Credits.P_Employee_ID "
			+ "	inner join P_Credits on  P_Credits.P_Credits_ID   = P_Employee_Credits.p_Credits_ID  "
			+ "	inner join P_UOM on P_Credits.P_UOM_ID = P_UOM.P_UOM_ID "
			+ "where P_Employee.P_Employee_ID = " + Payment.getP_Employee_ID()
			+ " and P_Credits.IsActive = 'Y' "	
			+ " and P_Credits.IsPrinted = 'Y' "
			+ " and P_Employee_Credits.IsActive = 'Y' "
		    + " and P_Employee_Credits.effectin = ( select max( effectin ) "
		    + "                                       from P_Employee_Credits a "
		    + "                                       Where P_Employee_Credits.p_employee_id = a.P_employee_id " 
		    + "                                         and P_Employee_Credits.p_credits_id = a.p_credits_id " 
		    + "                                         and a.effectin <= " + DB.TO_DATE(Period.getEndDate())
		    + "                                    ) "
			+ " Group BY P_Employee_Credits.P_Credits_ID, P_Employee_Credits.P_Employee_ID, P_Credits.P_UOM_ID, P_Employee_Credits.P_Employee_Credits_ID"
			;
		/*
 	    String sql 
	    ="select max(P_Employee_Credits.P_Employee_Credits_ID) P_Employee_Credits_ID, P_Employee_Credits.P_Credits_ID, P_Employee_Credits.P_Employee_ID, P_Credits.P_UOM_ID, " 
		    + "max(P_UOM.Value) as Unit, " 
		    + "max(P_Credits.Value ) as Value "
			+ " from P_Employee , "
		  	+ "     P_Employee_Credits, "
		    + "     P_Credits , "
			+ "     P_UOM "
			+ "where P_Employee.P_Employee_ID = " + Payment.getP_Employee_ID()
			+ " and P_Credits.P_UOM_ID = P_UOM.P_UOM_ID "
			+ " and P_Employee.P_Employee_ID = P_Employee_Credits.P_Employee_ID "
			+ " and P_Credits.P_Credits_ID   = P_Employee_Credits.p_Credits_ID "
			+ " and P_Credits.IsActive = 'Y' "	
			+ " and P_Credits.IsPrinted = 'Y' "
			+ " and P_Employee_Credits.IsActive = 'Y' "
		    + " and P_Employee_Credits.effectin = ( select max( effectin ) "
		    + "                                       from P_Employee_Credits a "
		    + "                                       Where P_Employee_Credits.p_employee_id = a.P_employee_id " 
		    + "                                         and P_Employee_Credits.p_credits_id = a.p_credits_id " 
		    + "                                         and a.effectin <= " + DB.TO_DATE(Period.getEndDate())
		    + "                                    ) "
			+ " Group BY P_Employee_Credits.P_Credits_ID, P_Employee_Credits.P_Employee_ID, P_Credits.P_UOM_ID, P_Employee_Credits.P_Employee_Credits_ID"
			;
*/ 	    

/*		
	    String sql = " select Credits.Name as Credits_Name, " 
            + " SUM( PMOVEMENTVARIATION ) Solde, " 
            + " UOM.Value as Unit, " 
            + " Credits.Value "
            + " from P_Employee Employee, "
            + "      P_Credits_Movement Movement, " 
            + "      P_Credits Credits, " 
            + "      P_UOM UOM "
            + " where Credits.P_UOM_ID = UOM.P_UOM_ID "
            + " and Movement.P_Credits_ID = Credits.P_Credits_ID "
            + " and Employee.P_Employee_ID = Movement.P_Employee_ID "
            + " and Movement.PMovementDate <= (select EndDate from P_Period where P_Period_ID = " + Payment.getP_Period_ID() + " )"
            + " and Employee.P_Employee_ID = " + Payment.getP_Employee_ID()
            + " and Credits.IsPrinted = 'Y' "
            + "  and exists ( select 1 from P_Employee_Credits employeeCredits "
            + "                Where Employee.p_employee_id = employeeCredits.P_employee_id "  
            + "                 and credits.p_credits_id = employeeCredits.p_credits_id " 
            + "                 and employeeCredits.IsActive = 'Y' "
            + "                 and employeeCredits.effectin = ( select max( effectin ) "
            + "                                                   from P_Employee_Credits "
            + "                                                     Where P_Employee_Credits.p_employee_id = employeeCredits.P_employee_id " 
            + "                                                      and P_Employee_Credits.p_credits_id = employeeCredits.p_credits_id " 
            + "                                               ) "
            + "            ) "
            + "	    group by Credits.Name, UOM.Value, Credits.Value "
  ;
*/
//	    System.out.println("Select:"+sql);
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, Payment.get_TrxName()); 
	        ResultSet rs = stmt.executeQuery();
	        while(rs.next())
	        {

				credits = P_Credits.get( Env.getCtx(), rs.getInt( "P_Credits_ID") , null);

	            statementEarningCredits = new P_Statement_Earning_Credits(Env.getCtx(), -1, Payment.get_TrxName());
	            statementEarningCredits.setP_Statement_Earning_ID(statementEarningId);
	            statementEarningCredits.setP_Credits_ID( rs.getInt( "P_Credits_ID"));
	            statementEarningCredits.setDescription( credits.getTrlName( language.getValue() ));

	            statementEarningCredits.setBalance( P_Credits.getEmployeeSoldeConv( Env.getCtx(),rs.getInt( "P_Employee_Credits_ID" ), rs.getInt( "P_Credits_ID"), rs.getInt("P_Employee_ID"), Period.getEndDate(), Payment.get_TrxName()) );
	            statementEarningCredits.setOpeningBalance( P_Credits.getEmployeeOpeningBalanceConv( Env.getCtx(),rs.getInt( "P_Employee_Credits_ID" ), rs.getInt( "P_Credits_ID"), rs.getInt("P_Employee_ID"), Period.getEndDate(), Payment.get_TrxName()) );
	            statementEarningCredits.setAccumulatedBalance( P_Credits.getEmployeeAccumulatedBalanceConv( Env.getCtx(),rs.getInt( "P_Employee_Credits_ID" ), rs.getInt( "P_Credits_ID"), rs.getInt("P_Employee_ID"), Period.getEndDate(), Payment.get_TrxName()) );

	            statementEarningCredits.setUseBalance( statementEarningCredits.getOpeningBalance().add(statementEarningCredits.getAccumulatedBalance()).subtract( statementEarningCredits.getBalance() ) );
	            
	            BigDecimal sldPrec = P_Credits.getEmployeeSoldeConv( Env.getCtx(),rs.getInt( "P_Employee_Credits_ID" ), rs.getInt( "P_Credits_ID"), rs.getInt("P_Employee_ID"), TimeUtil.addDays( Period.getStartDate(), -1 ), Payment.get_TrxName());
	            
	            BigDecimal variation = P_Credits.getEmployeeSoldeConv(Env.getCtx(), rs.getInt( "P_Employee_Credits_ID" ), rs.getInt( "P_Credits_ID"), rs.getInt("P_Employee_ID"), Period.getEndDate(), Payment.get_TrxName());
/*
	            statementEarningCredits.setBalance( P_Credits.getEmployeeSoldeConv( rs.getInt( "P_Credits_ID"), rs.getInt("P_Employee_ID"), Period.getEndDate(), Payment.get_TrxName()) );
	            BigDecimal sldPrec = P_Credits.getEmployeeSoldeConv( rs.getInt( "P_Credits_ID"), rs.getInt("P_Employee_ID"), TimeUtil.addDays( Period.getStartDate(), -1 ), Payment.get_TrxName());
	            
	            BigDecimal variation = P_Credits.getEmployeeSoldeConv( rs.getInt( "P_Credits_ID"), rs.getInt("P_Employee_ID"), Period.getEndDate(), Payment.get_TrxName());
*/
	            variation = variation.subtract( sldPrec );
	            					   
	            statementEarningCredits.setPMovementVariation( variation );
	            statementEarningCredits.setCalculUnit(rs.getString("Unit"));
//	            statementEarningCredits.setP_UOM_ID( rs.getInt( "P_UOM_ID"));
	            if(!statementEarningCredits.save())
	            {
	    	        rs.close();
	    	        stmt.close();
	    			s_log.log(Level.SEVERE," Create Statement Earning - " + sql);
	            }
	        }
	        
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
			s_log.log(Level.SEVERE," Create Statement Earning - " + sql, e);
	    }
	}

}
