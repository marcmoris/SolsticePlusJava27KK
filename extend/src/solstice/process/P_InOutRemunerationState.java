package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;

import solstice.model.P_Period;
import solstice.model.P_T_Remuneration_State;

/*
 * Created on Jul 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author rejgar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutRemunerationState //extends SvrProcess 
{
    private int Client_ID = 0;
    private int Org_ID = 0;
    private int StartPeriod = 0;
    private int EndPeriod = 0;
    private int Period_ID = 0;
    private int Instance_ID = 0;

	private String m_trxName;
    private Properties m_ctx;
    
    private CLogger			log = CLogger.getCLogger (getClass());

    public P_InOutRemunerationState( Properties ctx)
    {
        m_ctx = ctx;
    }

    protected void ExecuteProcess(int _Client_ID, int _Org_ID, int _StartPeriod, int _EndPeriod, int _Instance_ID)
    {
        StartPeriod = _StartPeriod;
        EndPeriod = _EndPeriod;
  	    Period_ID = StartPeriod;
  	    Client_ID = _Client_ID; 
  	    Org_ID = _Org_ID;
  	    Instance_ID = _Instance_ID;
     	String trxName = null;

    	setTrxName( trxName);

        log.log(Level.WARNING, "P_RemunerationState-AD_Org_ID = " + Org_ID);
        log.log(Level.WARNING, "P_RemunerationState-AD_Client_ID = " + Client_ID);
        log.log(Level.WARNING, "P_RemunerationState-StartPeriod = " + StartPeriod);
        log.log(Level.WARNING, "P_RemunerationState-EndPeriod = " + EndPeriod);
        DeleteYesterday();
        UpdatePaymentGroup();
        UpdateTotal();
        //return "";
    }

    public void DeleteYesterday()
    {
        String sql = "Delete from P_T_Remuneration_State where Created <= Getdate() - 1";

        PreparedStatement pstmp = null;
        try
        {
            pstmp = DB.prepareStatement(sql, null);
            pstmp.execute();
            //pstmp = DB.
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "P_InOutRemunerationState.Insert", e);
        }    
    }
    
    public void UpdatePaymentGroup ()
    {
        String StateSql = "Select AD_Client_id, AD_Org_ID, P_Payment_Group_id, P_Occupation_Group_ID," +
        "TypeLine, P_Taxable_benefit_id, sum(TaxableBenefitAmount) as TaxableBenefitAmount, P_Gain_id, sum(QuantityCalc) as QuantityCalc, " + 
        "sum(AmountCalc) as AmountCalc, P_Deduction_Id, sum(HoursEligible) as HoursEligible, sum(SalaryEligible) as SalaryEligible, " + 
        "sum(EmployeePart) as EmployeePart, sum(EmployerPart) as EmployerPart " +
        "From RV_P_RemunerationState " + 
        "where P_Period_ID >= " + StartPeriod +
        "  And P_period_ID <=" + EndPeriod +
        " Group by AD_Client_id, AD_Org_ID, P_Payment_Group_id, P_Occupation_Group_ID," +
        " TypeLine, P_Taxable_benefit_id, TaxableName, P_Gain_id, GainName, P_Deduction_Id, DeductionName" +
        " Order by P_Payment_Group_id, P_Occupation_Group_id, TypeLine, TaxableName, GainName, DeductionName";
     
        log.log(Level.WARNING, "StateSql "+ StateSql);
        PreparedStatement pstmp = null;
        try
        {
            pstmp = DB.prepareStatement( StateSql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, null);
        }
  	    catch (Exception e)
	    {
     		log.log(Level.SEVERE, "P_RemunerationState.UpdateFile", e);
	    }
    
  	    try
  	    {
  	        ResultSet rs = pstmp.executeQuery();
  	        P_T_Remuneration_State RemunerationState = null;
  	        P_Period PeriodStart = null;
  	        P_Period PeriodEnd = null;
  	        PeriodStart = P_Period.get(getCtx(), StartPeriod, getTrxName());
  	        PeriodEnd = P_Period.get(getCtx(), EndPeriod, getTrxName());
	        int NoLine = 0;	
	        String TypLine = ""; 
	        int Group_ID = 0;
	        int Occupation_Group_id = 0;
  	        while (rs.next())
  	        {
                if (Group_ID != rs.getInt("P_Payment_Group_ID"))
                     NoLine = 1;
                if (Occupation_Group_id != rs.getInt("P_Occupation_Group_ID"))
                    NoLine = 1;

                Group_ID = rs.getInt("P_Payment_Group_ID");
                Occupation_Group_id = rs.getInt("P_Occupation_Group_ID");

  	            if ( (rs.getString("TypeLine").equals("B")) || (rs.getString("TypeLine").equals("C")) || (rs.getString("TypeLine").equals("G")) )
  	            {
//  	  	        Taxable Benefit - Blank Line - Gain
  	                RemunerationState = new P_T_Remuneration_State(getCtx(), -1, getTrxName());
  	                RemunerationState.setAD_PInstance_ID(Instance_ID);
  	                RemunerationState.setP_Period_Start_ID(StartPeriod);
  	                RemunerationState.setP_Period_End_ID(EndPeriod);
  	                RemunerationState.setStartDate(PeriodStart.getStartDate());
  	                RemunerationState.setEndDate(PeriodEnd.getEndDate());
  	                RemunerationState.setP_Payment_Group_ID(rs.getInt("P_Payment_Group_ID"));
  	                RemunerationState.setP_Occupation_Group_ID(rs.getInt("P_Occupation_Group_ID"));
  	                RemunerationState.setLineType(rs.getString("TypeLine"));
  	                RemunerationState.setLine(NoLine);
  	                if (rs.getString("TypeLine").equals("B"))
  	                {
  	                    RemunerationState.setP_Taxable_Benefit_ID(rs.getInt("P_Taxable_Benefit_ID"));
  	                    RemunerationState.setTaxable_Benefit_Amount(rs.getBigDecimal("TaxableBenefitAmount"));
  	                }
  	                else if (rs.getString("TypeLine").equals("G"))
  	                {
  	                    RemunerationState.setP_Gain_ID(rs.getInt("P_Gain_ID"));
	                    RemunerationState.setQuantityCalc(rs.getBigDecimal("QuantityCalc"));
	                    RemunerationState.setAmountCalc(rs.getBigDecimal("AmountCalc"));
  	                }
  	                TypLine = rs.getString("TypeLine");
  	            }
  	            if (rs.getString("TypeLine").equals("H"))  // Deduction
  	            {
  	                if (TypLine != "H") 
                         NoLine = 1;
  	                TypLine = "H";
  	                Group_ID = rs.getInt("P_Payment_Group_ID");
  	                String StateDed = "Select P_T_Remuneration_State_id from P_T_Remuneration_State " +
  	                                  "where AD_Client_ID =" + Client_ID +
  	                                  "  and AD_Org_ID = " + Org_ID +
  	                                  "  and AD_Pinstance_ID = " + Instance_ID +
  	                                  "  and P_Payment_Group_ID = " + Group_ID +
  	                                  "  and P_Occupation_Group_id = " + Occupation_Group_id +
  	                                  "  and Line = " + NoLine;
  	                PreparedStatement pstmp2 = null;
  	                try
  	                {
  	                    pstmp2 = DB.prepareStatement(StateDed, null);
  	                }
  	                catch (Exception e)
  	                {
  	                    log.log(Level.SEVERE, "PRemunerationState.UpdateDED", e);
  	                }
  	                try
  	                {
  	                    ResultSet rsDed = pstmp2.executeQuery();
  	                    if (rsDed.next())
  	                    {
  	                        RemunerationState = P_T_Remuneration_State.get(getCtx(), rsDed.getInt("P_T_Remuneration_State_ID"), getTrxName());
  	                        RemunerationState.setP_Deduction_ID(rs.getInt(("P_Deduction_ID")));
	  	                    RemunerationState.setHours_Eligible(rs.getBigDecimal("HoursEligible"));
	  	                    RemunerationState.setSalary_Eligible(rs.getBigDecimal("SalaryEligible"));
	  	                    RemunerationState.setEmployee_Part(rs.getBigDecimal("EmployeePart"));
	  	                    RemunerationState.setEmployer_Part(rs.getBigDecimal("EmployerPart"));
  	                    }
  	                    else
  	                    {
  	    	                RemunerationState = new P_T_Remuneration_State(getCtx(), -1, getTrxName());
  	    	                RemunerationState.setAD_PInstance_ID(Instance_ID);
  	    	                RemunerationState.setP_Period_Start_ID(StartPeriod);
  	    	                RemunerationState.setP_Period_End_ID(EndPeriod);
  	    	                RemunerationState.setStartDate(PeriodStart.getStartDate());
  	    	                RemunerationState.setEndDate(PeriodEnd.getEndDate());
  	    	                RemunerationState.setP_Payment_Group_ID(rs.getInt("P_Payment_Group_ID"));
  	    	                RemunerationState.setP_Occupation_Group_ID(rs.getInt("P_Occupation_Group_ID"));
  	    	                RemunerationState.setLineType(rs.getString("TypeLine"));
  	    	                RemunerationState.setLine(NoLine);
  	                        RemunerationState.setP_Deduction_ID(rs.getInt(("P_Deduction_ID")));
	  	                    RemunerationState.setHours_Eligible(rs.getBigDecimal("HoursEligible"));
	  	                    RemunerationState.setSalary_Eligible(rs.getBigDecimal("SalaryEligible"));
	  	                    RemunerationState.setEmployee_Part(rs.getBigDecimal("EmployeePart"));
	  	                    RemunerationState.setEmployer_Part(rs.getBigDecimal("EmployerPart"));
  	                    }
  	                }
  	                catch (SQLException e)
  	                {
  	                    log.log(Level.SEVERE, "P_RemunerationState.Updateded", e);
  	                }
  	            }
  	            RemunerationState.save();
  	            NoLine++;
  	        }
  	        rs.close();
  	    }
  	    catch (SQLException e)
  	    {
  	        log.log(Level.SEVERE, "UpdateFile", e);
  	    }
    }
    
    public void UpdateTotal()
    {
        String StateSql = "Select AD_Client_id, AD_Org_ID, TypeLine, P_Taxable_benefit_id, sum(TaxableBenefitAmount) as TaxableBenefitAmount, " +
        "       P_Gain_id, sum(QuantityCalc) as QuantityCalc, sum(AmountCalc) as AmountCalc, " +
        "       P_Deduction_Id, sum(HoursEligible) as HoursEligible, sum(SalaryEligible) as SalaryEligible, sum(EmployeePart) as EmployeePart, sum(EmployerPart) as EmployerPart " +
        "From RV_P_RemunerationState " +
        "where P_Period_ID >= " + StartPeriod + "And P_period_ID <= " + EndPeriod + " " +
        "Group by AD_Client_id, AD_Org_ID, TypeLine, P_Taxable_benefit_id, TaxableName, P_Gain_id, GainName, P_Deduction_Id, DeductionName " +
        "Order by TypeLine, TaxableName, GainName, DeductionName ";

     
        log.log(Level.WARNING, "StateSql "+ StateSql);
        PreparedStatement pstmp = null;
        try
        {
            pstmp = DB.prepareStatement( StateSql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, null);
        }
  	    catch (Exception e)
	    {
     		log.log(Level.SEVERE, "P_RemunerationState.UpdateFile", e);
	    }
    
  	    try
  	    {
  	        ResultSet rs = pstmp.executeQuery();
  	        P_T_Remuneration_State RemunerationState = null;
  	        P_Period PeriodStart = null;
  	        P_Period PeriodEnd = null;
  	        PeriodStart = P_Period.get(getCtx(), StartPeriod, getTrxName());
  	        PeriodEnd = P_Period.get(getCtx(), EndPeriod, getTrxName());  	        
	        int NoLine = 1;	
	        String TypLine = ""; 
	        int Field_ID = 0;
  	        while (rs.next())
  	        {
  	            if ( (rs.getString("TypeLine").equals("B")) || (rs.getString("TypeLine").equals("C")) || (rs.getString("TypeLine").equals("G")) )
  	            {
//  	  	        Taxable Benefit - Blank Line - Gain
  	                RemunerationState = new P_T_Remuneration_State(getCtx(), -1, getTrxName());
  	                RemunerationState.setAD_PInstance_ID(Instance_ID);
  	                RemunerationState.setP_Period_Start_ID(StartPeriod);
  	                RemunerationState.setP_Period_End_ID(EndPeriod);
  	                RemunerationState.setStartDate(PeriodStart.getStartDate());
  	                RemunerationState.setEndDate(PeriodEnd.getEndDate());
  	                RemunerationState.setLineType(rs.getString("TypeLine"));
  	                RemunerationState.setLine(NoLine);
  	                if (rs.getString("TypeLine").equals("B"))
  	                {
  	                    RemunerationState.setP_Taxable_Benefit_ID(rs.getInt("P_Taxable_Benefit_ID"));
  	                    RemunerationState.setTaxable_Benefit_Amount(rs.getBigDecimal("TaxableBenefitAmount"));
  	                }
  	                else if (rs.getString("TypeLine").equals("G"))
  	                {
  	                    RemunerationState.setP_Gain_ID(rs.getInt("P_Gain_ID"));
	                    RemunerationState.setQuantityCalc(rs.getBigDecimal("QuantityCalc"));
	                    RemunerationState.setAmountCalc(rs.getBigDecimal("AmountCalc"));
  	                }
  	                TypLine = rs.getString("TypeLine");
  	            }
  	            if (rs.getString("TypeLine").equals("H"))  // Deduction
  	            {
  	                if (TypLine != "H") 
                         NoLine = 1;
  	                TypLine = "H";
  	                String StateDed = "Select P_T_Remuneration_State_id from P_T_Remuneration_State " +
  	                                  "where AD_Client_ID =" + Client_ID +
  	                                  "  and AD_Org_ID = " + Org_ID +
  	                                  "  and AD_Pinstance_ID = " + Instance_ID +
  	                                  "  and P_Payment_Group_ID is null" +
  	                                  "  and P_Occupation_Group_ID is null" +
  	                                  "  and Line = " + NoLine;
  	                PreparedStatement pstmp2 = null;
  	                try
  	                {
  	                    pstmp2 = DB.prepareStatement(StateDed, null);
  	                }
  	                catch (Exception e)
  	                {
  	                    log.log(Level.SEVERE, "PRemunerationState.UpdateDED", e);
  	                }
  	                try
  	                {
  	                    ResultSet rsDed = pstmp2.executeQuery();
  	                    if (rsDed.next())
  	                    {
  	                        RemunerationState = P_T_Remuneration_State.get(getCtx(), rsDed.getInt("P_T_Remuneration_State_ID"), getTrxName());
  	                        RemunerationState.setP_Deduction_ID(rs.getInt(("P_Deduction_ID")));
	  	                    RemunerationState.setHours_Eligible(rs.getBigDecimal("HoursEligible"));
	  	                    RemunerationState.setSalary_Eligible(rs.getBigDecimal("SalaryEligible"));
	  	                    RemunerationState.setEmployee_Part(rs.getBigDecimal("EmployeePart"));
	  	                    RemunerationState.setEmployer_Part(rs.getBigDecimal("EmployerPart"));
  	                    }
  	                    else
  	                    {
  	    	                RemunerationState = new P_T_Remuneration_State(getCtx(), -1, getTrxName());
  	    	                RemunerationState.setAD_PInstance_ID(Instance_ID);
  	    	                RemunerationState.setP_Period_Start_ID(StartPeriod);
  	    	                RemunerationState.setP_Period_End_ID(EndPeriod);
  	    	                RemunerationState.setStartDate(PeriodStart.getStartDate());
  	    	                RemunerationState.setEndDate(PeriodEnd.getEndDate());
  	    	                RemunerationState.setLineType(rs.getString("TypeLine"));
  	    	                RemunerationState.setLine(NoLine);
  	                        RemunerationState.setP_Deduction_ID(rs.getInt(("P_Deduction_ID")));
	  	                    RemunerationState.setHours_Eligible(rs.getBigDecimal("HoursEligible"));
	  	                    RemunerationState.setSalary_Eligible(rs.getBigDecimal("SalaryEligible"));
	  	                    RemunerationState.setEmployee_Part(rs.getBigDecimal("EmployeePart"));
	  	                    RemunerationState.setEmployer_Part(rs.getBigDecimal("EmployerPart"));
  	                    }
  	                }
  	                catch (SQLException e)
  	                {
  	                    log.log(Level.SEVERE, "P_RemunerationState.Updateded", e);
  	                }
  	            }
  	            RemunerationState.save();
  	            NoLine++;
  	        }
  	        rs.close();
  	    }
  	    catch (SQLException e)
  	    {
  	        log.log(Level.SEVERE, "UpdateFile", e);
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
    
	public Properties getCtx()
	{
		return m_ctx;
	}

}

