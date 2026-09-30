package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import solstice.model.P_Assignment_Param;
import solstice.model.P_Credits;
import solstice.model.P_Deduction;
import solstice.model.P_Employee;
import solstice.model.P_Gain;
import solstice.model.P_Language;
import solstice.model.P_Payment;
import solstice.model.P_Period;
import solstice.model.P_Statement_Earning;
import solstice.model.P_Statement_Earning_Credits;
import solstice.model.P_Statement_Earning_Deduction;
import solstice.model.P_Statement_Earning_Gain;
import solstice.model.P_Statement_Earning_InsDetail;
import solstice.model.P_Statement_Earning_Taxable_Benefits;
import solstice.model.P_Taxable_Benefit;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Time_Sheet_Error;
import solstice.model.P_Payment.PaymentException;

import java.util.logging.*;

import org.compiere.util.*;

public class CreateStatementEarning {

	private P_Payment Payment = null ;
	
	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (CreateStatementEarning.class);

	public void createStatementEarning( P_Payment _Payment)
	{
		Payment = _Payment;
		
        // On supprime d'abord les anciennes valeurs dans Statement Earning s'il y a lieu
        String sql = "delete from P_Statement_Earning where P_Payment_ID = " + Payment.getP_Payment_ID();
        DB.executeUpdate(sql, Payment.get_TrxName());
        
        //On remplit les tables
        try
        {
	        int statementEarningId = this.fillStatementEarning();
	        this.fillStatementEarningCredits(statementEarningId);
	        this.fillStatementEarningDeduction(statementEarningId);
	        this.fillStatementEarningGain(statementEarningId);
	        this.fillStatementEarningTaxableBenefits(statementEarningId);
        }
        catch ( Exception e)
        {
        	s_log.log(Level.SEVERE,"P_Payment After Save",e);
        }
	}
	
	private int fillStatementEarning( ) throws PaymentException
	{
	    P_Statement_Earning statementEarning = new P_Statement_Earning(Env.getCtx(), -1, Payment.get_TrxName());
	    
	    statementEarning.setP_Employee_ID(Payment.getP_Employee_ID());
	    statementEarning.setP_Period_ID(Payment.getP_Period_ID());
	    statementEarning.setPrePrintedNo(Payment.getPrePrintedNo());
	    statementEarning.setGrossEarningsPeriod(Payment.getGrossEarnings());
	    statementEarning.setTaxableBenefitsPeriod(Payment.getTotalTaxableBenefitProv());
	    statementEarning.setDeductionPeriod(Payment.getTotalDeduction());
	    statementEarning.setPayDate( Payment.getPayDate());
	    statementEarning.setP_Payment_ID( Payment.getP_Payment_ID());
	    
	    P_Period period = P_Period.get(Env.getCtx(), Payment.getP_Period_ID(), Payment.get_TrxName());
	    statementEarning.setPayFinishing(period.getEndDate());
	    
	    // P_Distribution_ID
	    P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), Payment.get_TrxName());
	    
	    if ( Employee.getP_Distribution_ID() == 0 )
	    {
		    P_Time_Sheet TimeSheet = P_Time_Sheet.GetWithPaymentID( Env.getCtx(), Payment.getP_Payment_ID(), Payment.get_TrxName());
            P_Time_Sheet_Error.NewMessage( TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "L'employé n'a pas de code de distribution", Payment.get_TrxName() );
            TimeSheet.setIsError(true);
            TimeSheet.save();
            return 0;
	    }
	    	
        statementEarning.setP_Distribution_ID( Employee.getP_Distribution_ID() );
	    
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
	            throw new PaymentException("Erreur dans le chargement des heures");
	        }
	        rs.close();
	        stmt.close();
	    }
	    catch(Exception e)
	    {
	        throw new PaymentException (e);
	    }
	    
	    P_Assignment_Param Assignment_Param = P_Assignment_Param.get( Env.getCtx(), Employee.GetPrincipalAssignment_Param(), Payment.get_TrxName());
    
	    BigDecimal HRate = Assignment_Param.getHourly_Rate( );
	    BigDecimal HSal = Assignment_Param.getAnnual_Salary( );
	    
	    
	    sql
	    = "select P_Assignment_Param.P_Job_Title_ID, P_Assignment_Param.P_Salary_Class_ID, P_Assignment_Param.P_Salary_Scale_Detail_ID, ("
			+ " case P_Assignment_Param.Remuneration_Method"
			+ "     when 'hourly' then P_Assignment_Param.Hourly_Rate"
			+ "     else P_Assignment_Param.Annual_Salary"
			+ "   end"
			+ " ) as Rate, P_Assignment_Param.Remuneration_Method"
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
	        }
	        rs.close();
	        stmt.close();
	        
	    }
	    catch(Exception e)
	    {
	        throw new PaymentException(e);
	    }

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
	        throw new PaymentException(e);
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
	        throw new PaymentException(e);
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
	        throw new PaymentException(e);
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
	        throw new PaymentException(e);
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
		    throw new PaymentException(e);
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
	        throw new PaymentException(e);
	    }
	    
	    // C_Activity_ID
	    
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
	        throw new PaymentException(e);
	    }
	    
	    if(!statementEarning.save())
	        throw new PaymentException("fillStatementEarning()::statementEarning.save()");
	    return statementEarning.getP_Statement_Earning_ID();
	}

	public void fillStatementEarningGain(int statementEarningId) throws PaymentException
	{
		// Cette requète sort les heures et les montants périodiques pour
		// chaque regroupement de gain de la période incluant tout ceux
		// qui ont figuré dans les relevés antérieurs à l'année du paiement
		// courrant.
		String sql
		= "select gain.P_Gain_ID, gain.Name as Description, sum(paymentGain.QuantityCalc) as UnitaryCum, sum(paymentGain.AmountCalc) as AmountCum"
			+ " from (P_Gain gain inner join AD_TreeNode treeNode"
			+ " on gain.P_Gain_ID = treeNode.parent_ID) inner join ((P_Payment payment inner join P_Period period"
			+ " on payment.P_Period_ID = period.P_Period_ID) inner join P_Payment_Gain paymentGain"
			+ " on payment.P_Payment_ID = paymentGain.P_Payment_ID)"
			+ " on treeNode.node_ID = paymentGain.P_Gain_ID"
			+ " where treeNode.AD_Tree_ID = 1000003"
			+ " and payment.P_Employee_ID = " + Payment.getP_Employee_ID()
			+ " and exists ("
			+ "   select 1"
			+ "   from P_Period"
			+ "   where P_Period_ID = " + Payment.getP_Period_ID()
			+ "   and P_Year_ID = period.P_Year_ID"
			+ "   and StartDate >= period.StartDate"
			+ " )"
			+ " group by gain.P_Gain_ID, gain.Name order by gain.P_Gain_ID";
				
		try
		{
			PreparedStatement stmt = DB.prepareStatement(sql, Payment.get_TrxName());
			ResultSet rs = stmt.executeQuery();
			
			int count=0;
			
			while(rs.next())
			{
				P_Statement_Earning_Gain statementEarningGain = new P_Statement_Earning_Gain(Env.getCtx(), -1, null);
				statementEarningGain.setP_Statement_Earning_ID(statementEarningId);
				statementEarningGain.setDescription(rs.getString("Description"));
				
				P_Gain gain = P_Gain.get( Env.getCtx(), rs.getInt( "P_Gain_ID") , null);
				if ( gain.getP_Method_Gain_ID() == 118 )
					statementEarningGain.setUnitaryCum(new BigDecimal(0));
				else	
					statementEarningGain.setUnitaryCum(rs.getBigDecimal("UnitaryCum"));
				
				statementEarningGain.setAmountcumul(rs.getBigDecimal("AmountCum"));
				
				// Cette requète sort les cumulatif des heures et des montants pour
				// un regroupement de gain du début de l'année à la période courrante
				sql
				= "select gain.P_Gain_ID, gain.Name as Description, isnull(sum(paymentGain.QuantityCalc), 0) as UnitaryPeriod, isnull(sum(paymentGain.AmountCalc), 0) as AmountPeriod"
			    + " from (P_Gain gain inner join AD_TreeNode treeNode"
			    + " on gain.P_Gain_ID = treeNode.parent_ID) left join (P_Payment payment inner join P_Payment_Gain paymentGain"
			    + " on payment.P_Payment_ID = paymentGain.P_Payment_ID"
			    + " and payment.P_Payment_ID = " + Payment.getP_Payment_ID() + ")"
			    + " on treeNode.node_ID = paymentGain.P_Gain_ID"
			    + " where treeNode.AD_Tree_ID = 1000003"
				+ " and gain.P_Gain_ID = " + rs.getInt("P_Gain_ID")
			    + " group by gain.P_Gain_ID, gain.Name";
				
				PreparedStatement ps = DB.prepareStatement(sql, Payment.get_TrxName());
				ResultSet r = ps.executeQuery();

				if(r.next())
				{
					P_Gain gainCum = P_Gain.get( Env.getCtx(), rs.getInt( "P_Gain_ID") , null);
					if ( gainCum.getP_Method_Gain_ID() == 118 )
						statementEarningGain.setUnitaryPeriod(new BigDecimal(0));
					else	
						statementEarningGain.setUnitaryPeriod(r.getBigDecimal("UnitaryPeriod"));
					statementEarningGain.setAmountPeriod(r.getBigDecimal("AmountPeriod"));
				}
				else
				{
					statementEarningGain.setUnitaryPeriod(new BigDecimal(0));
					statementEarningGain.setAmountPeriod(new BigDecimal(0));
				}	
					 
				r.close();
				ps.close();
				
				if (( Payment.getPaymentType().equals( P_Payment.PAYMENTTYPE_Cheque) && count <= 14 ) || Payment.getPaymentType().equals( P_Payment.PAYMENTTYPE_Deposit) )
				{
					count++;
					statementEarningGain.save();
				}
			}
			
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			throw new PaymentException(e);
		}
	}

	private void fillStatementEarningDeduction(int statementEarningId) throws PaymentException
	{
		// Cette requète sort les parts de l'employé et le montant restant périodique
		// cumulés pour chaque regroupement de déduction à un paiement donné. Elle sort
		// également de détail des assurances
		String sql
		= "select deduction.P_Deduction_ID, deduction.Name as Description, isnull(sum(paymentDeduction.Employee_Part), 0) as AmountCum, 0 as Balance, 'N' as Insurance, deduction.IsBaseInsurance"
			+ " from (P_Deduction deduction inner join AD_TreeNode treeNode"
			+ " on deduction.P_Deduction_ID = treeNode.parent_ID) inner join ((P_Payment payment inner join P_Period period"
			+ " on payment.P_Period_ID = period.P_Period_ID) inner join P_Payment_Deduction paymentDeduction"
			+ " on payment.P_Payment_ID = paymentDeduction.P_Payment_ID)"
			+ " on treeNode.node_ID = paymentDeduction.P_Deduction_ID"
			+ " where treeNode.AD_Tree_ID = 1000004"
			+ " and payment.P_Employee_ID = " + Payment.getP_Employee_ID()
			+ " and exists ("
			+ "   select 1"
			+ "   from P_Period"
			+ "   where P_Period_ID = " + Payment.getP_Period_ID()
			+ "   and (( P_Year_ID = period.P_Year_ID "
			+ "   and StartDate >= period.StartDate ) or ( deduction.isannual = 'N' and Period.P_Period_ID >= dbo.FN_PeriodDeductionNonAnnual(payment.P_Employee_ID,paymentDeduction.P_Deduction_ID ) and dbo.FN_EndYearDeductionNonAnnual(payment.P_Employee_ID,paymentDeduction.P_Deduction_ID, Period.P_Period_ID ) >= datepart( year, Period.EndDate ) ))"
			+ " )"
		    + " group by deduction.P_Deduction_ID, deduction.Name, deduction.IsBaseInsurance"
		    + " union all"
		    + " select deduction.P_Deduction_ID, deduction.Name as Description, isnull(sum(paymentDeduction.Employee_Part), 0) as AmountCum, isnull(sum(paymentDeduction.RetrievalAmount), 0) as Balance, 'Y' as Insurance, deduction.IsBaseInsurance"
			+ " from P_Deduction deduction inner join ((P_Payment payment inner join P_Period period"
			+ " on payment.P_Period_ID = period.P_Period_ID) inner join P_Payment_Deduction paymentDeduction"
			+ " on payment.P_Payment_ID = paymentDeduction.P_Payment_ID)"
			+ " on deduction.P_Deduction_ID = paymentDeduction.P_Deduction_ID"
			+ " where deduction.P_Deduction_Family_ID = 1000001"
			+ " and payment.P_Employee_ID = " + Payment.getP_Employee_ID()
			+ " and exists ("
			+ "   select 1"
			+ "   from P_Period"
			+ "   where P_Period_ID = " + Payment.getP_Period_ID()
			+ "   and ( ( P_Year_ID = period.P_Year_ID"
			+ "   and StartDate >= period.StartDate ) or ( deduction.isannual = 'N' and Period.P_Period_ID >= dbo.FN_PeriodDeductionNonAnnual(payment.P_Employee_ID,paymentDeduction.P_Deduction_ID ) and dbo.FN_EndYearDeductionNonAnnual(payment.P_Employee_ID,paymentDeduction.P_Deduction_ID, Period.P_Period_ID ) >= datepart( year, Period.EndDate ) ))"
			+ " )"
		    + " group by deduction.P_Deduction_ID, deduction.Name, deduction.IsBaseInsurance";
		try
		{
			PreparedStatement stmt = DB.prepareStatement(sql, Payment.get_TrxName());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
			{
				P_Statement_Earning_Deduction statementEarningDeduction = new P_Statement_Earning_Deduction(Env.getCtx(), -1, null);
				
				statementEarningDeduction.setP_Statement_Earning_ID(statementEarningId);
				statementEarningDeduction.setP_Deduction_ID(rs.getInt("P_Deduction_ID"));
				statementEarningDeduction.setDescription(rs.getString("Description"));
//				statementEarningDeduction.setAmountPeriod(rs.getBigDecimal("AmountPeriod"));
				statementEarningDeduction.setAmountcumul(rs.getBigDecimal("AmountCum"));
				statementEarningDeduction.setInsurance(rs.getString("Insurance").equals("Y"));
				if(rs.getString("IsBaseInsurance") != null)
				    statementEarningDeduction.setIsBaseInsurance(rs.getString("IsBaseInsurance").equals("Y"));

				if(rs.getString("Insurance").equals("Y"))
				{
					// Cette requète sort les cumulatifs pour les détail d'assurances
					// du début de l'année à une période et un employé donnés 
					sql
					= "select deduction.P_Deduction_ID, deduction.Name, sum(paymentDeduction.Employee_Part) as AmountPeriod, sum(isnull(paymentDeduction.AccumulationAmount, 0)) as Balance"
						+ " from P_Deduction deduction inner join ((P_Payment payment inner join P_Period period"
						+ " on payment.P_Period_ID = period.P_Period_ID) inner join P_Payment_Deduction paymentDeduction"
						+ " on payment.P_Payment_ID = paymentDeduction.P_Payment_ID)"
						+ " on deduction.P_Deduction_ID = paymentDeduction.P_Deduction_ID"
						+ " where deduction.P_Deduction_Family_ID = 1000001"
						+ " and deduction.P_Deduction_ID = " + rs.getInt("P_Deduction_ID")
						+ " and payment.P_Employee_ID = " + Payment.getP_Employee_ID() 
						+ " and payment.P_Payment_ID  = " + Payment.getP_Payment_ID() 
						+ " group by deduction.P_Deduction_ID, deduction.Name";
				}
				else
				{
					// Cette requète sort les cumulatifs pour les regroupements de déduction
					// du début de l'année à une période et un employé donnés
					sql
					= "select deduction.P_Deduction_ID, deduction.Name, sum(paymentDeduction.Employee_Part) as AmountPeriod, sum(isnull(paymentDeduction.AccumulationAmount, 0)) as Balance"
						+ " from (P_Deduction deduction inner join AD_TreeNode treeNode"
						+ " on deduction.P_Deduction_ID = treeNode.parent_ID) inner join ((P_Payment payment inner join P_Period period"
						+ " on payment.P_Period_ID = period.P_Period_ID) inner join P_Payment_Deduction paymentDeduction"
						+ " on payment.P_Payment_ID = paymentDeduction.P_Payment_ID)"
						+ " on treeNode.node_ID = paymentDeduction.P_Deduction_ID"
						+ " where treeNode.AD_Tree_ID = 1000004"
						+ " and deduction.P_Deduction_ID = " + rs.getInt("P_Deduction_ID")
						+ " and payment.P_Employee_ID = " + Payment.getP_Employee_ID()
						+ " and payment.P_Payment_ID = " + Payment.getP_Payment_ID()
						+ " group by deduction.P_Deduction_ID, deduction.Name";
				}
				
				PreparedStatement ps = DB.prepareStatement(sql, Payment.get_TrxName());
				ResultSet r = ps.executeQuery();
				
				if(r.next())
				{
					statementEarningDeduction.setAmountPeriod(r.getBigDecimal("AmountPeriod"));
					statementEarningDeduction.setBalance(r.getBigDecimal("Balance"));
				}
				else
				{
					statementEarningDeduction.setAmountPeriod(new BigDecimal(0));
					statementEarningDeduction.setBalance(new BigDecimal(0));
				}
				
				r.close();
				ps.close();
				
				if(statementEarningDeduction.getAmountcumul().compareTo( Env.ZERO ) != 0 || statementEarningDeduction.getBalance().compareTo( Env.ZERO ) != 0 )
				{
				    statementEarningDeduction.save();
						// On doit maintenant remplir la table P_Statement_Earning_Deduction_InsDetail
				    if(statementEarningDeduction.isInsurance() && !statementEarningDeduction.isBaseInsurance())
//					    	ici
//					    if(statementEarningDeduction.isInsurance() && statementEarningDeduction.isBaseInsurance())
				        fillStatementEarningDeductionInsDetail(statementEarningDeduction.getP_Statement_Earning_Deduction_ID());
				}
				
			}
			
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			throw new PaymentException(e);
		}
	}
	
	private void fillStatementEarningDeductionInsDetail(int statementEarningDeductionId) throws PaymentException
	{
	    // Dans le cas des déductions d'assurance, on doit vérifier s'il existe un détail
	    // d'assurance pour l'employé
	    String sql
	    = "select IsSmoker, MultiplyRate, ProtectionAmount"
	        + " from P_Employee_Deduction"
	        + " where P_Employee_ID = " + Payment.getP_Employee_ID()
	        + "   and dbo.P_Deduction_Family_Get( P_Employee_Deduction.P_Deduction_ID ) = 1000001 "
	        + "   and dbo.P_Deduction_Method_Get( P_Employee_Deduction.P_Deduction_ID )=114  "  // and dbo.P_Deduction_IsBaseInsurance( P_Deduction_ID ) = 'N'
	        + "   and EffectIn = ( Select Max( EffectIn ) From P_Employee_Deduction Eff Where Eff.P_Employee_ID = " + Payment.getP_Employee_ID() + " AND Eff.P_Deduction_ID = P_Employee_Deduction.P_Deduction_ID AND EffectIn < (select top 1 StartDate from P_Period where P_Period_ID = " + Payment.getP_Period_ID() + "))"
	        + " and exists ("
	        + "   select 1"
	        + "   from P_Statement_Earning_Deduction"
	        + "   where P_Deduction_ID = P_Employee_Deduction.P_Deduction_ID"
	        + "   and P_Statement_Earning_Deduction_ID = " + statementEarningDeductionId
	        + " ) ";
	    
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, Payment.get_TrxName());
	        ResultSet rs = stmt.executeQuery();
	        
	        String msg = "";
	        while(rs.next())
	        {
/*	        	
	            if(rs.getString("IsSmoker") != null)
	            {
		            P_Statement_Earning_InsDetail statementEarningDeductionInsDetail = new P_Statement_Earning_InsDetail(Env.getCtx(), -1, null);
		            statementEarningDeductionInsDetail.setP_Statement_Earning_Deduction_ID(statementEarningDeductionId);
	                statementEarningDeductionInsDetail.setName("Fumeur: " + (rs.getString("IsSmoker").equals("Y") ? "Oui" : "Non"));
	                statementEarningDeductionInsDetail.save();
	            }
	            if(rs.getBigDecimal("MultiplyRate") != null
	                    && rs.getBigDecimal("MultiplyRate").doubleValue() > 0)
	            {
	                P_Statement_Earning_InsDetail statementEarningDeductionInsDetail = new P_Statement_Earning_InsDetail(Env.getCtx(), -1, null);
		            statementEarningDeductionInsDetail.setP_Statement_Earning_Deduction_ID(statementEarningDeductionId);
	                statementEarningDeductionInsDetail.setName("Nbr de fois le salaire:");
	                statementEarningDeductionInsDetail.setDecimalValue(rs.getBigDecimal("MultiplyRate"));
	                statementEarningDeductionInsDetail.save();
	            }
	            if(rs.getBigDecimal("ProtectionAmount") != null
	                    && rs.getBigDecimal("ProtectionAmount").doubleValue() > 0)
	            {
	                P_Statement_Earning_InsDetail statementEarningDeductionInsDetail = new P_Statement_Earning_InsDetail(Env.getCtx(), -1, null);
		            statementEarningDeductionInsDetail.setP_Statement_Earning_Deduction_ID(statementEarningDeductionId);
	                statementEarningDeductionInsDetail.setName("Protection:");
	                statementEarningDeductionInsDetail.setDecimalValue(rs.getBigDecimal("ProtectionAmount"));
	                statementEarningDeductionInsDetail.save();
	            }
	        */

	            if(rs.getString("IsSmoker") != null)
	            {
	            	msg = msg + "Fumeur: " + (rs.getString("IsSmoker").equals("Y") ? "Oui" : "Non");
	            }

	            if(rs.getBigDecimal("MultiplyRate") != null
	                    && rs.getBigDecimal("MultiplyRate").doubleValue() > 0)
	            {
	            	if ( msg.trim().length() != 0 ) msg = msg + ", ";
	            	msg = msg + "Nbr fois salaire: " + rs.getBigDecimal("MultiplyRate").setScale(2,BigDecimal.ROUND_HALF_UP).toString().replace('.', ',' );
	            }

	            if(rs.getBigDecimal("ProtectionAmount") != null
	                    && rs.getBigDecimal("ProtectionAmount").doubleValue() > 0)
	            {
	            	if ( msg.trim().length() != 0 ) msg = msg + ", ";
	            	msg = msg + "Protection: " + rs.getBigDecimal("ProtectionAmount").setScale(2,BigDecimal.ROUND_HALF_UP).toString().replace('.', ',' );
	            }
	        	
	        	if ( msg.trim().length() != 0)
	            {
	                P_Statement_Earning_InsDetail statementEarningDeductionInsDetail = new P_Statement_Earning_InsDetail(Env.getCtx(), -1, null);
		            statementEarningDeductionInsDetail.setP_Statement_Earning_Deduction_ID(statementEarningDeductionId);
	                statementEarningDeductionInsDetail.setName( msg);
	                statementEarningDeductionInsDetail.setDecimalValue( Env.ZERO);
	                statementEarningDeductionInsDetail.save();
	            	
	            }
	        }
	        
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
	        throw new PaymentException(e);
	    }
	}

	private void fillStatementEarningTaxableBenefits(int statementEarningId) throws PaymentException
	{
	    String sql
	    = " select taxableBenefit.Name as Description, isnull(sum(paymentTaxableBenefit.Taxable_Benefit_Amount), 0) as PAmount,"
	        + "   (select isnull(sum(ptb.Taxable_Benefit_Amount), 0)"
	        + "   from P_Period pe inner join (P_Payment p inner join P_Payment_Taxable_Benefit ptb"
	        + "   on p.P_Payment_ID = ptb.P_Payment_ID)"
	        + "   on pe.P_Period_ID = p.P_Period_ID"
	        + "   where p.P_Year_ID = payment.P_Year_ID"
	        + "   and period.P_Year_ID = " + Payment.getP_Year_ID() 
	        + "   and pe.StartDate <= period.StartDate"
	        + "   and p.P_Employee_ID = payment.P_Employee_ID" 
	        + "   and ptb.P_Taxable_Benefit_id = paymentTaxableBenefit.P_Taxable_Benefit_ID" 
	        +		") as CAmount"
	        + " from P_Period period inner join (P_Payment payment inner join (P_Payment_Taxable_Benefit paymentTaxableBenefit inner join P_Taxable_Benefit taxableBenefit"
	        + " on paymentTaxableBenefit.P_Taxable_Benefit_ID = taxableBenefit.P_Taxable_Benefit_ID)"
	        + " on payment.P_Payment_ID = paymentTaxableBenefit.P_Payment_ID)"
	        + " on period.P_Period_ID = payment.P_Period_ID"
	        + " where payment.P_Payment_ID = " + Payment.getP_Payment_ID()
            + " and period.P_Year_ID = " + Payment.getP_Year_ID() 
	        + " group by period.StartDate, period.P_Year_ID, payment.P_Year_ID, payment.P_Employee_ID, paymentTaxableBenefit.P_Taxable_Benefit_ID, taxableBenefit.Name";
	    
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, Payment.get_TrxName());
	        ResultSet rs = stmt.executeQuery();
	        
	        while(rs.next())
	        {
	            P_Statement_Earning_Taxable_Benefits statementEarningTaxableBenefits = new P_Statement_Earning_Taxable_Benefits(Env.getCtx(), -1, Payment.get_TrxName());
	            statementEarningTaxableBenefits.setP_Statement_Earning_ID(statementEarningId);
	            statementEarningTaxableBenefits.setDescription(rs.getString("Description"));
	            statementEarningTaxableBenefits.setAmountPeriod(rs.getBigDecimal("PAmount"));
	            statementEarningTaxableBenefits.setAmountcumul(rs.getBigDecimal("CAmount"));
	            if(!statementEarningTaxableBenefits.save())
	            {
	                rs.close();
	                stmt.close();
	                throw new PaymentException("statementEarningTaxableBenefits.save()");
	            }
	        }
            rs.close();
            stmt.close();
	    }
	    catch (Exception e)
	    {
	        throw new PaymentException(e);
	    }
	}
	
	private void fillStatementEarningCredits(int statementEarningId) throws PaymentException
	{
		P_Period Period = P_Period.get(Env.getCtx(), Payment.getP_Period_ID(), Payment.get_TrxName());

 	    String sql 
	    ="select P_Employee_Credits.P_Credits_ID, P_Employee_Credits.P_Employee_ID, P_Credits.P_UOM_ID, " 
	        + "max(P_Credits.Name) as Credits_Name , " 
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
			+ " Group BY P_Employee_Credits.P_Credits_ID, P_Employee_Credits.P_Employee_ID, P_Credits.P_UOM_ID"
			;

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
	        	
	            P_Statement_Earning_Credits statementEarningCredits = new P_Statement_Earning_Credits(Env.getCtx(), -1, Payment.get_TrxName());
	            statementEarningCredits.setP_Statement_Earning_ID(statementEarningId);
//	            statementEarningCredits.setP_Credits_ID( rs.getInt( "P_Credits_ID"));
	            statementEarningCredits.setDescription(rs.getString("Credits_Name"));
	            statementEarningCredits.setBalance( P_Credits.getEmployeeSolde( rs.getInt( "P_Credits_ID"), rs.getInt("P_Employee_ID"), Period.getEndDate(), Payment.get_TrxName()) );
	            statementEarningCredits.setCalculUnit(rs.getString("Unit"));
//	            statementEarningCredits.setP_UOM_ID( rs.getInt( "P_UOM_ID"));
	            if(!statementEarningCredits.save())
	            {
	    	        rs.close();
	    	        stmt.close();
	                throw new PaymentException("fillStatementEarningCredits::statementEarningCredits.save()");
	            }
	        }
	        
	        rs.close();
	        stmt.close();
	    }
	    catch (Exception e)
	    {
	        throw new PaymentException("fillStatementEarningCredits::" + sql + "::" + e);
	    }
	}

	
    /**
     * Pour distinguer les exceptions "NORMALES"
     */
    public class PaymentException extends Exception
    {
        public PaymentException() {super();}
        public PaymentException(String arg0) {super(arg0);}
        public PaymentException(Throwable arg0) {super(arg0);}
        public PaymentException(String arg0, Throwable arg1) {super(arg0, arg1);}
    }

}
