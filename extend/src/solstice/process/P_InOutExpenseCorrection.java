/*
 * Created on 12 Novembre 2005
 *
 * Génération de la transaction d'écriture de dépense.
 */
package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.process.*;
import solstice.model.*;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutExpenseCorrection  extends SvrProcess 
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

        P_ExpenseCorrection ExpenseCorrection = new P_ExpenseCorrection( getCtx(), P_ExpenseCorrection_ID, null); 

        if ( ExpenseCorrection.getP_Accounting_Entry_ID() != 0)
        	return "*Erreur* L'écriture comptable a déjà été généré, modification impossible";
        
        undo( P_ExpenseCorrection_ID );


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

        if(ExpenseCorrection.getC_ElementValue_MP_Src_ID() != 0)
        {
            if(where != "") where += " and ";
            where += "(P_PAYMENT_GAIN_DISTRIBUTION.ACCOUNT_ID = " + ExpenseCorrection.getC_ElementValue_MP_Src_ID() + " and Distribution_Type = 'A')";
        }
        
        if(ExpenseCorrection.getAD_Org_Src_ID() != 0)
        {
            if(where != "") where += " and ";
            where += "P_PAYMENT_GAIN_DISTRIBUTION.Org_ID = " + ExpenseCorrection.getAD_Org_Src_ID();
        }

 /*  	if ( ExpenseCorrection.getC_SalesRegion_Src_ID() != 0 )
        {
            if(where != "") where += " and ";
            where += "P_PAYMENT_GAIN_DISTRIBUTION.C_SalesRegion_ID = " + ExpenseCorrection.getC_SalesRegion_Src_ID();
        }
*/
//        where += " AND Distribution_type = 'S'";
//TEMPORARAIRE.        
        where += " AND P_PAYMENT_GAIN.P_POST_ID <> 1000178" ;
        
        boolean isAmount = false;
        if ( ExpenseCorrection.getAmount().compareTo(new BigDecimal(0)) != 0 )
        {
        	isAmount = true;
        }
        
        String sql
        	= " select P_PAYMENT_GAIN_DISTRIBUTION.AMOUNT, P_PAYMENT_GAIN_DISTRIBUTION.Quantity, P_PAYMENT_GAIN.P_PAYMENT_GAIN_ID, P_PAYMENT.P_PAYMENT_ID, Distribution_Type, P_PAYMENT_GAIN_DISTRIBUTION.Account_ID, P_PAYMENT_GAIN_DISTRIBUTION.C_Activity_ID , P_PAYMENT_GAIN_DISTRIBUTION.AD_Org_ID, P_PAYMENT_GAIN_DISTRIBUTION.Org_ID, P_PAYMENT_GAIN_DISTRIBUTION.C_SalesRegion_ID, P_PAYMENT_GAIN.P_Gain_ID, P_PAYMENT_GAIN_DISTRIBUTION.Distribution_type, P_PAYMENT_GAIN.P_Post_ID, P_Payment.P_Employee_ID "
            + " from P_PAYMENT_GAIN_DISTRIBUTION "
            + " inner join P_PAYMENT_GAIN  on P_PAYMENT_GAIN_DISTRIBUTION.P_PAYMENT_GAIN_ID = P_PAYMENT_GAIN.P_PAYMENT_GAIN_ID " 
            + " inner join P_PAYMENT on P_PAYMENT_GAIN.P_PAYMENT_ID = P_PAYMENT.P_PAYMENT_ID" 
            + " inner join P_PERIOD on P_PAYMENT.P_PERIOD_ID = P_PERIOD.P_PERIOD_ID  "
            + " ";
        
        if(where != "") sql += " where " + where;
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        ResultSet rs2 = stmt.executeQuery();

	        
	        // Tant qu'il reste des enregistrements à modifier...
	        
	        BigDecimal AmountRest = null;
	        
	        int ElementValue_Src; 
	        int Activity_Src;
	        int ElementValue_Dst = 0;
	        int Activity_Dst;
	        
	        int Org_Src;
	        int Org_Dst;
	        int SalesRegion_Src;
	        int SalesRegion_Dst;

	        P_Gain_Account GainAcct = null;
	        while(rs2.next())
	        {
	        	P_Payment Payment = P_Payment.get( Env.getCtx(), rs2.getInt("P_Payment_ID"), null);
	        	
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


	        	if ( ExpenseCorrection.getAD_Org_Src_ID() != 0 )
	        	{
	        	    Org_Src     = ExpenseCorrection.getAD_Org_Src_ID();
	        	}
	        	else
	        	{
	        	    Org_Src     = rs2.getInt("Org_ID");
	        	}

	        	if ( ExpenseCorrection.getAD_Org_Dst_ID() != 0 )
	        	{
	        		Org_Dst     = ExpenseCorrection.getAD_Org_Dst_ID();
	        	}
	        	else
	        	{
	        		Org_Dst     = rs2.getInt("Org_ID");
	        	}

	        	if ( ExpenseCorrection.getC_SalesRegion_Src_ID() != 0 )
	        	{
	        		SalesRegion_Src     = ExpenseCorrection.getC_SalesRegion_Src_ID();
	        		SalesRegion_Dst     = ExpenseCorrection.getC_SalesRegion_Dst_ID();
	
	            	if ( rs2.getString("Distribution_type").trim().equals( P_Payment_Gain_Distribution.DISTRIBUTION_TYPE_A ) )
	            	{
		        		SalesRegion_Src = rs2.getInt("C_SalesRegion_ID");
	            		SalesRegion_Dst = rs2.getInt("C_SalesRegion_ID");
	            	}

	        	}
	        	else
	        	{
	        		SalesRegion_Src     = rs2.getInt("C_SalesRegion_ID");
	        		SalesRegion_Dst     = rs2.getInt("C_SalesRegion_ID");
	        	}

	        	if ( ExpenseCorrection.getC_ElementValue_Src_ID() != 0 )
	        	{
	        		ElementValue_Src = ExpenseCorrection.getC_ElementValue_Src_ID();
	        	}
	        	else
	        	{
            		ElementValue_Src = rs2.getInt("Account_ID");
            		
	            	if ( rs2.getString("Distribution_type").trim().equals( P_Payment_Gain_Distribution.DISTRIBUTION_TYPE_A ) )
	            	{
	            		P_Post Post = P_Post.get( Env.getCtx(), rs2.getInt("P_Post_ID"), null);
	    				P_Workplace WorkPlace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);

	            		int P_Deduction_ID = getDeduction_ID( ExpenseCorrection.getAD_Client_ID(), Org_Src, Activity_Src, ElementValue_Src, WorkPlace.getC_SalesRegion_ID() );
	            		
	            		if ( P_Deduction_ID == 0 )
	            		{
	            			P_Employee Employee = P_Employee.get( Env.getCtx(), rs2.getInt("P_Employee_ID"), null);
		    				P_Assignment Assignment = P_Assignment.get( Env.getCtx(), Employee.GetAssignmentPrincipal(), null);
		    				WorkPlace = P_Workplace.get( Env.getCtx(), Assignment.getP_Workplace_ID(), null);

	            			P_Deduction_ID = getDeduction_ID( ExpenseCorrection.getAD_Client_ID(), Org_Src, Activity_Src, ElementValue_Src, WorkPlace.getC_SalesRegion_ID() );
	            		}
	            		
//	            		P_Deduction_Account account = P_Deduction_Account.get(getCtx(), ExpenseCorrection.getAD_Client_ID(), Org_Dst, Activity_Dst, P_Deduction_ID, trxName);

	    				int C_SalesRegion_ID = WorkPlace.getC_SalesRegion_ID();
	    				P_Deduction_Account account = P_Deduction_Account.getWithSaleRegion(Env.getCtx(), Payment.getAD_Client_ID(), Payment.getAD_Org_ID(), C_SalesRegion_ID, P_Deduction_ID,  null);
	            		
	            		if ( account != null)
	            			ElementValue_Src = account.getP_Deduction_Acct();
	            		else
	            		{
	            			log.log(Level.WARNING," Account not found - for Deduction " + P_Deduction_ID );
	            			ElementValue_Src = rs2.getInt("Account_ID");
	            		}
            		}
/*	            	else
	            	{
	            		GainAcct = P_Gain_Account.get(getCtx(), ExpenseCorrection.getAD_Client_ID(), Org_Dst, Activity_Dst, rs2.getInt("P_Gain_ID"), trxName);
	            		if ( GainAcct != null)
	            		{
	            			ElementValue_Dst = GainAcct.getP_Gain_Acct();
	            		}
	            		else
	            		{
	            			P_Gain Gain = P_Gain.get( getCtx(), rs2.getInt("P_Gain_ID"), trxName);
	            			log.log(Level.WARNING," Gain not found - " + Gain.getValue() + ' ' + Gain.getName());
		                	ElementValue_Dst = rs2.getInt("Account_ID");
	            		}
	            	}
	 */           	
	        	}


	        	if ( ExpenseCorrection.getC_ElementValue_Dst_ID() != 0 )
	        	{
	        		ElementValue_Dst = ExpenseCorrection.getC_ElementValue_Dst_ID();
	        		
	            	if ( rs2.getString("Distribution_type").trim().equals( P_Payment_Gain_Distribution.DISTRIBUTION_TYPE_A ) )
	            	{
	            		P_Post Post = P_Post.get( Env.getCtx(), rs2.getInt("P_Post_ID"), null);
	            		
	    				P_Workplace WorkPlace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);

	    				int P_Deduction_ID = getDeduction_ID( ExpenseCorrection.getAD_Client_ID(), Org_Src, Activity_Src, ElementValue_Src, WorkPlace.getC_SalesRegion_ID() );
	            		if ( P_Deduction_ID == 0 )
	            		{
	            			P_Employee Employee = P_Employee.get( Env.getCtx(), rs2.getInt("P_Employee_ID"), null);
		    				P_Assignment Assignment = P_Assignment.get( Env.getCtx(), Employee.GetAssignmentPrincipal(), null);
		    				WorkPlace = P_Workplace.get( Env.getCtx(), Assignment.getP_Workplace_ID(), null);
	            			P_Deduction_ID = getDeduction_ID( ExpenseCorrection.getAD_Client_ID(), Org_Src, Activity_Src, ElementValue_Src, WorkPlace.getC_SalesRegion_ID() );
	            		}
	            		
//	            		P_Deduction_Account account = P_Deduction_Account.get(getCtx(), ExpenseCorrection.getAD_Client_ID(), Org_Dst, Activity_Dst, P_Deduction_ID, trxName);

	    				int C_SalesRegion_ID = WorkPlace.getC_SalesRegion_ID();
	    				
	    				C_SalesRegion_ID = 1101425;
	    				P_Deduction_Account account = P_Deduction_Account.getWithSaleRegion(Env.getCtx(), Payment.getAD_Client_ID(), Payment.getAD_Org_ID(), C_SalesRegion_ID, P_Deduction_ID,  null);
	    					            		
						if ( account != null)
							ElementValue_Dst = account.getP_Deduction_Acct();
						else
						{
							log.log(Level.WARNING," Account not found - for Deduction " + P_Deduction_ID );
							ElementValue_Dst = rs2.getInt("Account_ID");
						}

	            	}

	        	}
	        	
		        BigDecimal Amount = rs2.getBigDecimal("AMOUNT");
		        BigDecimal Quantity = rs2.getBigDecimal("Quantity");

		        if (Amount == null)  Amount = new BigDecimal(0);
		        if (Quantity == null)  Quantity = new BigDecimal(0);
		        
		        
		        if ( ExpenseCorrection.getRate() != null && ExpenseCorrection.getRate().compareTo( new BigDecimal(0)) != 0 )
		        {
		        	Amount = Amount.multiply(ExpenseCorrection.getRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP));
		        	Quantity = Quantity.multiply(ExpenseCorrection.getRate().divide(new BigDecimal(100),8,BigDecimal.ROUND_HALF_UP));
		        }
		        
		        if ( ExpenseCorrection.getAmount().compareTo(new BigDecimal(0)) != 0 )
		        {
		        	if ( ExpenseCorrection.getAmount().compareTo(Amount) >  0 )
		        	{
		        		Quantity = (Quantity.multiply( Amount )).divide( ExpenseCorrection.getAmount(),2,BigDecimal.ROUND_HALF_UP);
		        		Amount = ExpenseCorrection.getAmount();
		        	}
		        }
		        
		        if ( AmountRest == null || ! isAmount )
		        	AmountRest = Amount;
		        
		        if ( AmountRest.compareTo( new BigDecimal(0) ) != 0  )
		        {
		            // ... on retire le montant du compte source ...
			        P_Payment_Gain_Distribution gain = new P_Payment_Gain_Distribution(getCtx(), -1, trxName);
			        gain.setAccount_ID( ElementValue_Src );
			        gain.setC_Activity_ID( Activity_Src );
			        gain.setOrg_ID( Org_Src);
			        gain.setC_SalesRegion_ID(SalesRegion_Src);

			        gain.setAmount(Amount.negate());
			        gain.setQuantity( Quantity.negate() );
		        	gain.setAD_Org_ID(rs2.getInt("AD_Org_ID"));
			        gain.setP_Payment_Gain_ID( rs2.getInt("P_PAYMENT_GAIN_ID") );
			        gain.setLine(this.getNextLine(rs2.getInt("P_PAYMENT_GAIN_ID")));
			        gain.setDistribution_Type( rs2.getString("Distribution_Type") );
			        gain.setP_ExpenseCorrection_ID( P_ExpenseCorrection_ID );
			        gain.save();
			        
			        // ... et on le place dans le compte de destination 
			        gain = new P_Payment_Gain_Distribution(getCtx(), -1, trxName);
			        gain.setAccount_ID( ElementValue_Dst );
			        gain.setC_Activity_ID( Activity_Dst );
			        gain.setOrg_ID( Org_Dst );
			        gain.setC_SalesRegion_ID(SalesRegion_Dst);

			        gain.setAmount( Amount );
			        gain.setQuantity( Quantity );
		        	gain.setAD_Org_ID(rs2.getInt("AD_Org_ID"));
			        gain.setLine(this.getNextLine( rs2.getInt("P_PAYMENT_GAIN_ID")));
			        gain.setDistribution_Type( rs2.getString("Distribution_Type") );
			        gain.setP_Payment_Gain_ID( rs2.getInt("P_PAYMENT_GAIN_ID"));
			        gain.setP_ExpenseCorrection_ID( P_ExpenseCorrection_ID );
			        gain.save();

			        
			        P_Payment_Entryline Payment_EntryLine = new P_Payment_Entryline(getCtx(), -1, trxName);
			        Payment_EntryLine.setP_Payment_ID( rs2.getInt("P_Payment_ID") );
			        if ( Amount.compareTo( new BigDecimal(0)) < 0 )
			        {
				        Payment_EntryLine.setAmtAcctCr( new BigDecimal(0));
				        Payment_EntryLine.setAmtAcctDr( Amount.negate());
			        }
			        else
			        {
				        Payment_EntryLine.setAmtAcctCr( Amount);
				        Payment_EntryLine.setAmtAcctDr( new BigDecimal(0));
			        }

			        Payment_EntryLine.setAD_Org_ID(rs2.getInt("AD_Org_ID"));
			        Payment_EntryLine.setOrg_ID(Org_Src);
			        Payment_EntryLine.setC_SalesRegion_ID(SalesRegion_Src);
			        Payment_EntryLine.setC_Activity_ID( Activity_Src );
			        Payment_EntryLine.setC_ElementValue_ID( ElementValue_Src );
			        Payment_EntryLine.setP_ExpenseCorrection_ID( P_ExpenseCorrection_ID );
			        Payment_EntryLine.setDescription( ExpenseCorrection.getDescription() );
			        Payment_EntryLine.setLine(this.getNextLine2( rs2.getInt("P_PAYMENT_ID")));
			        Payment_EntryLine.save();

			        Payment_EntryLine = new P_Payment_Entryline(getCtx(), -1, trxName);
			        Payment_EntryLine.setP_Payment_ID( rs2.getInt("P_Payment_ID") );
			        Payment_EntryLine.setAmtAcctCr( new BigDecimal(0));
			        if ( Amount.compareTo( new BigDecimal(0)) < 0 )
			        {
				        Payment_EntryLine.setAmtAcctCr( Amount.negate());
				        Payment_EntryLine.setAmtAcctDr( new BigDecimal(0));
			        }
			        else
			        {
				        Payment_EntryLine.setAmtAcctCr( new BigDecimal(0));
				        Payment_EntryLine.setAmtAcctDr( Amount);
			        }
			        Payment_EntryLine.setAD_Org_ID(rs2.getInt("AD_Org_ID"));
			        Payment_EntryLine.setOrg_ID(Org_Dst);
			        Payment_EntryLine.setC_SalesRegion_ID(SalesRegion_Dst);
			        Payment_EntryLine.setC_Activity_ID( Activity_Dst );
			        Payment_EntryLine.setC_ElementValue_ID( ElementValue_Dst );
			        Payment_EntryLine.setP_ExpenseCorrection_ID( P_ExpenseCorrection_ID );
			        Payment_EntryLine.setDescription( ExpenseCorrection.getDescription() );
			        Payment_EntryLine.setLine(this.getNextLine2( rs2.getInt("P_PAYMENT_ID")));
			        Payment_EntryLine.save();
		        
			        AmountRest = AmountRest.subtract( Amount );
		        	
		        }
		        
	        }
	        
	        rs2.close();
	        stmt.close();

	    }
	    catch (Exception e)
	    {
			log.log(Level.SEVERE,"P_Employee - " + sql, e);
	        e.getMessage();
	    }
	    
		return "";
	}
	
	//
	//
	//
	private int getDeduction_ID( int AD_Client_ID, int AD_Org_ID, int C_Activity_ID, int Acct_ID, int C_SalesRegion_ID )
	{
		
		int P_Deduction_ID = 0;
		
		String sql = "Select P_Deduction_ID From P_Deduction_Account "
		       + " Where AD_Client_ID = " + AD_Client_ID
		       + "  AND AD_Org_ID = " + AD_Org_ID
//		       + "  AND C_Activity_Src_ID = " + C_Activity_ID
		       + "  AND C_SalesRegion_ID = " + C_SalesRegion_ID
		       + "  AND P_Deduction_Acct = " + Acct_ID
		   	;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				P_Deduction_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"P_Deduction_Account - Get - " + sql, e);
		}
		return P_Deduction_ID;

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
	    catch (Exception e)
	    {
			log.log(Level.SEVERE,"P_Employee - " + sql, e);
	        e.getMessage();
	    }
	    
	    return BigDecimal.valueOf( 0 );
	}

	private int getNextLine2(int P_PAYMENT_ID)
	{
	    String sql
	    	= " select isnull(max(P_Payment_EntryLine.LINE), 0) + 1"
	        + " from P_Payment_EntryLine"
	        + " where P_Payment_EntryLine.P_PAYMENT_ID = " + P_PAYMENT_ID;
	    
	    try
	    {
		    PreparedStatement s = DB.prepareStatement(sql, null);
		    ResultSet rs = s.executeQuery();
		    if(rs.next())
		    {
		        int line = rs.getInt(1);
		        return line;
		    }
	    }
	    catch (Exception e)
	    {
			log.log(Level.SEVERE,"P_Employee - " + sql, e);
	        e.getMessage();
	    }
	    
	    return 1;
	}

	public void undo( int P_ExpenseCorrection_ID )
	{
		String SqlDel = "Delete from P_Payment_Gain_Distribution where P_ExpenseCorrection_ID = " + P_ExpenseCorrection_ID ;
		DB.executeUpdate(SqlDel, null);

		SqlDel = "Delete from P_Payment_EntryLine where P_ExpenseCorrection_ID = " + P_ExpenseCorrection_ID ;
		DB.executeUpdate(SqlDel, null);
	}
}
