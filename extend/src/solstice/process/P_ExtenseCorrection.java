/*
 * Created on 10 juin 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.DB;
import org.compiere.process.*;
import solstice.model.*;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_ExtenseCorrection  extends SvrProcess 
{
	int P_ExpenseCorrection_ID;
	
	// On récupère le ID de la session
	protected void prepare() 
	{
	    P_ExpenseCorrection_ID = getRecord_ID();
	}
	
	// On effectue les corrections de dépense dans la table P_PAYMENT_GAIN_DISTRIBUTION
	// selon les critères de recherche entrés dans la table P_EXPENSECORRECTION
	protected String doIt() throws Exception 
	{

        String trxName = null; //Trx.createTrxName();

        undo( P_ExpenseCorrection_ID );

    	P_ExpenseCorrection ExpenseCorrection = new P_ExpenseCorrection( getCtx(), P_ExpenseCorrection_ID, null); 

    	String where = "";

        if(ExpenseCorrection.getP_Employee_ID() != 0)
        {
            if(where != "") where += " and ";
            where += "P_PAYMENT.P_EMPLOYEE_ID = " + ExpenseCorrection.getP_Employee_ID();
        }

        if(ExpenseCorrection.getP_Gain_ID() != 0)
        {
            if(where != "") where += " and ";
            where += "P_PAYMENT_GAIN.P_GAIN_ID = " + ExpenseCorrection.getP_Gain_ID();
        }

        if(ExpenseCorrection.getP_Period_Start_ID() != 0)
        {
            if(where != "") where += " and ";
            where += "P_PERIOD.STARTDATE >= (SELECT TOP 1 STARTDATE FROM P_PERIOD WHERE P_PERIOD_ID = " + ExpenseCorrection.getP_Period_Start_ID() + ")";
        }

        if(ExpenseCorrection.getP_Period_End_ID() != 0)
        {
            if(where != "") where += " and ";
            where += "P_PERIOD.ENDDATE <= (SELECT TOP 1 ENDDATE FROM P_PERIOD WHERE P_PERIOD_ID = " + ExpenseCorrection.getP_Period_End_ID() + ")";
        }
        
        if(ExpenseCorrection.getC_ElementValue_Src_ID() != 0)
        {
            if(where != "") where += " and ";
            where += "P_PAYMENT_GAIN_DISTRIBUTION.ACCOUNT_ID = " + ExpenseCorrection.getC_ElementValue_Src_ID();
        }
        
        if(ExpenseCorrection.getC_Activity_Src_ID() != 0)
        {
            if(where != "") where += " and ";
            where += "P_PAYMENT_GAIN_DISTRIBUTION.C_ACTIVITY_ID = " + ExpenseCorrection.getC_Activity_Src_ID();
        }

        boolean isAmount = false;
        if ( ExpenseCorrection.getAmount().compareTo(new BigDecimal(0)) != 0 )
        {
        	isAmount = true;
        }
        
        String sql
        	= " select P_PAYMENT_GAIN_DISTRIBUTION.AMOUNT, P_PAYMENT_GAIN.P_PAYMENT_GAIN_ID, P_PAYMENT.P_PAYMENT_ID, Distribution_Type, P_PAYMENT_GAIN_DISTRIBUTION.Account_ID, P_PAYMENT_GAIN_DISTRIBUTION.C_Activity_ID"
            + " from P_PAYMENT_GAIN_DISTRIBUTION "
            + " inner join P_PAYMENT_GAIN  on P_PAYMENT_GAIN_DISTRIBUTION.P_PAYMENT_GAIN_ID = P_PAYMENT_GAIN.P_PAYMENT_GAIN_ID " 
            + " inner join P_PAYMENT on P_PAYMENT_GAIN.P_PAYMENT_ID = P_PAYMENT.P_PAYMENT_ID" 
            + " inner join P_PERIOD on P_PAYMENT.P_PERIOD_ID = P_PERIOD.P_PERIOD_ID  "
            + " ";
        
        if(where != "") sql += " where " + where;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs2 = stmt.executeQuery();

        
        // Tant qu'il reste des enregistrements à modifier...
        
        BigDecimal AmountRest = null;
        
        int ElementValue_Src; 
        int Activity_Src;
        int ElementValue_Dst;
        int Activity_Dst;
        
        while(rs2.next())
        {
        	
        	if ( ExpenseCorrection.getC_ElementValue_Src_ID() != 0 )
        	{
        		ElementValue_Src = ExpenseCorrection.getC_ElementValue_Src_ID();
        	}
        	else
        	{
        		ElementValue_Src = rs2.getInt("Account_ID");
        	}

        	if ( ExpenseCorrection.getC_ElementValue_Dst_ID() != 0 )
        	{
            	ElementValue_Dst = ExpenseCorrection.getC_ElementValue_Dst_ID();
        	}
        	else
        	{
            	ElementValue_Dst = rs2.getInt("Account_ID");
        	}

        	if ( ExpenseCorrection.getC_Activity_Src_ID() != 0 )
        	{
        	    Activity_Src     = ExpenseCorrection.getC_Activity_Src_ID();
        	}
        	else
        	{
        	    Activity_Src     = rs2.getInt("C_Activity_ID");
        	}

        	if ( ExpenseCorrection.getC_Activity_Dst_ID() != 0 )
        	{
        		Activity_Dst     = ExpenseCorrection.getC_Activity_Dst_ID();
        	}
        	else
        	{
        		Activity_Dst     = rs2.getInt("C_Activity_ID");
        	}


            // ... on retire le montant du compte source ...
	        P_Payment_Gain_Distribution gain = new P_Payment_Gain_Distribution(getCtx(), -1, trxName);

	        BigDecimal Amount = rs2.getBigDecimal("AMOUNT");

	        if ( ExpenseCorrection.getRate() != null && ExpenseCorrection.getRate().compareTo( new BigDecimal(0)) != 0 )
	        	Amount = Amount.multiply(ExpenseCorrection.getRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP));
	        
	        if ( ExpenseCorrection.getAmount().compareTo(new BigDecimal(0)) != 0 )
	        {
	        	if ( ExpenseCorrection.getAmount().compareTo(Amount) >  0 )
	        	{
	        		Amount = ExpenseCorrection.getAmount();
	        	}
	        }
	        
	        if ( AmountRest == null || ! isAmount )
	        	AmountRest = Amount;
	        
	        if ( AmountRest.compareTo( new BigDecimal(0) ) != 0  )
	        {
		        gain.setAccount_ID( ElementValue_Src );
		        gain.setC_Activity_ID( Activity_Src );
		        gain.setAmount(Amount.negate());
		   
		        gain.setP_Payment_Gain_ID( rs2.getInt("P_PAYMENT_GAIN_ID") );
		        gain.setLine(this.getNextLine(rs2.getInt("P_PAYMENT_GAIN_ID")));
		        gain.setDistribution_Type( rs2.getString("Distribution_Type") );
		        gain.setP_ExpenseCorrection_ID( P_ExpenseCorrection_ID );
		        gain.save();
		        
		        // ... et on le place dans le compte de destination 
		        gain = new P_Payment_Gain_Distribution(getCtx(), -1, trxName);
		        gain.setAccount_ID( ElementValue_Dst );
		        gain.setC_Activity_ID( Activity_Dst );
	        	gain.setAmount( Amount );
		        
		        gain.setLine(this.getNextLine( rs2.getInt("P_PAYMENT_GAIN_ID")));
		        gain.setDistribution_Type( rs2.getString("Distribution_Type") );
		        gain.setP_Payment_Gain_ID( ((BigDecimal)rs2.getBigDecimal("P_PAYMENT_GAIN_ID")).intValue());
		        gain.setP_ExpenseCorrection_ID( P_ExpenseCorrection_ID );
		        gain.save();
		        
		        AmountRest = AmountRest.subtract( Amount );
	        	
	        }
	        
        }
        
        rs2.close();
        stmt.close();
		return "";
	}
	
	// On retourne la prochaine ligne pour une P_PAYMENT_GAIN_ID dans la table
	// P_PAYMENT_GAIN_DISTRIBUTION
	private BigDecimal getNextLine(int P_PAYMENT_GAIN_ID)
	{
	    String sql
	    	= " select isnull(max(P_PAYMENT_GAIN_DISTRIBUTION.LINE), 0) + 1"
	        + " from P_PAYMENT_GAIN_DISTRIBUTION"
	        + " where P_PAYMENT_GAIN_DISTRIBUTION.P_PAYMENT_GAIN_ID = " + P_PAYMENT_GAIN_ID;
	    
	    try
	    {
		    PreparedStatement s = DB.prepareStatement(sql, null);
		    ResultSet rs = s.executeQuery();
		    if(rs.next())
		    {
		        BigDecimal line = rs.getBigDecimal(1);
		        return line;
		    }
	    }
	    catch (SQLException e)
	    {
	        e.getMessage();
	    }
	    
	    return BigDecimal.valueOf( 0 );
	}

	public void undo( int P_ExpenseCorrection_ID )
	{
		String SqlDel = "Delete from P_Payment_Gain_Distribution where P_ExpenseCorrection_ID = " + P_ExpenseCorrection_ID ;
		DB.executeUpdate(SqlDel, null);
	}
}
