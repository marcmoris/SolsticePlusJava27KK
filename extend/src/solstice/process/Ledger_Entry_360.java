/*
 * Created on 2005-03-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.model.MActivity;
import org.compiere.model.MElementValue;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import solstice.model.P_Assignment;
import solstice.model.P_BankAccount;
import solstice.model.P_Credits;
import solstice.model.P_Credits_Account_360;
import solstice.model.P_Deduction;
import solstice.model.P_Deduction_Account_360;
import solstice.model.P_Deduction_Param;
import solstice.model.P_Employee;
import solstice.model.P_Payment;
import solstice.model.P_Payment_Entryline_360;
import solstice.model.P_Payment_Group;
import solstice.model.P_Period;
import solstice.model.P_Post;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Time_Sheet_Error;
import solstice.model.P_Workplace;

/**
 * @author frabou01
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Ledger_Entry_360
{

	/**	Static Logger				*/
	private static CLogger		log = CLogger.getCLogger (Ledger_Entry_360.class);

    //Liste des imputations
    Vector imputationList = null;

    //La liste des paiements pour une période de paie donnée
    Vector<BigDecimal> paymentIdList = null;

    //La liste des périodes de paie comprises dans une période comptable
    Vector payPeriodIdList = null;

    P_Payment Payment;

    P_Period Period;

    P_Time_Sheet TimeSheet;

    BigDecimal totalAmount = null;

	private String m_trxName;

    P_Employee Employee; 
//    P_Workplace Workplace; 

	
    //Constructeur par défaut
    public Ledger_Entry_360( P_Payment _Payment , String trxName)
    {
    	setTrxName( trxName );
        Payment = _Payment; //new P_Payment(Env.getCtx(), paymentId, trxName);
        Period = P_Period.get(Env.getCtx(), Payment.getP_Period_ID(), trxName);
        imputationList = new Vector();
        TimeSheet = P_Time_Sheet.GetWithPaymentID(Env.getCtx(), Payment.getP_Payment_ID(), trxName);

        Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), getTrxName());

        
    }

    public void WriteEntry()
    {
//    	RemakeDistribution( Payment.getP_Payment_ID(), m_trxName );


    	Undo( Payment.getP_Payment_ID(), m_trxName );

		//Recreate MP
//		WriteMargnalProfitByPayment( );
		WriteMargnalProfitByPaymentNew( );

		WriteCreditsEntryByPaymentNew( );
		

    	//Imputation
        WriteImputation( );

        //Compte à payer
        WriteAccountPayable( );

        //Compte de dépense
        if ( Payment.getPaymentTypeDoc().equals( P_Payment.PAYMENTTYPEDOC_ExpenseAccount ))
        {
        	WriteExpensereports( );
        }
        
        //Compte à recevoir
        WriteAccountReceivable();

        //Compte à recevoir Credits
  //      WriteAccountPayableCredits();

        //Encaisse
        WriteCashBalance();
        
        //InterCompagnie
//        InterCompagnie();
        
        Boolean valide = ValidTotal( Payment.getP_Payment_ID(), m_trxName );
        if ( valide == false)
        {
//        	P_Time_Sheet timesheet =  P_Time_Sheet.GetWithPaymentID(Env.getCtx(), Payment.getP_Payment_ID(), m_trxName);
//        	timesheet.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Error);
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Erreur dans l'écriture comptable " , getTrxName());
        	
        }

    }
    
    public void WriteExpensereports( )
    {
        P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), getTrxName());

        P_Assignment Assignment = P_Assignment.get( Env.getCtx(), Employee.getLastEmployeeAssignment( Payment.getP_Payment_ID() ), null);
		P_Post Post = P_Post.get( Env.getCtx(), Assignment.getP_Post_ID(), null);
		P_Workplace Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);

 		if ( Workplace.getC_SalesRegion_ID() == 0 )
 		{
 	 		Assignment = P_Assignment.get( Env.getCtx(), Employee.GetAssignmentPrincipal(), null);
 	 		Post = P_Post.get( Env.getCtx(), Assignment.getP_Post_ID(), null);
 	 		Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);
 			
 		}

        try
        {

        	String Sql = " SELECT P_Employee_Expensereports.P_Employee_Expensereports_ID\r\n"
        			+ " , P_Employee_Expensereports_Detail.C_ELEMENTVALUE_ID\r\n"
        			+ " , sum( P_Employee_Expensereports_Detail.amount - P_Employee_Expensereports_Detail.tax_amount ) amt \r\n"
        			+ " from P_Employee_Expensereports \r\n"
        			+ "    INNER JOIN P_Employee_Expensereports_Detail on P_Employee_Expensereports_Detail.P_Employee_Expensereports_ID = P_Employee_Expensereports.P_Employee_Expensereports_ID \r\n"
        			+ "    WHERE P_Employee_Expensereports.isactive = 'Y' AND is_reimbursable = 'Y' \r\n"
        			+ "    AND P_Employee_Expensereports.P_Employee_ID = " + Payment.getP_Employee_ID() 
        			+ " GROUP BY P_Employee_Expensereports.P_Employee_Expensereports_ID, P_Employee_Expensereports_Detail.C_ELEMENTVALUE_ID";
        	
            PreparedStatement pstmt;

            pstmt = DB.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
//           pstmt.setInt(1, Payment.getP_Payment_ID());
            ResultSet rs = pstmt.executeQuery();
            P_Payment_Entryline_360 aEntryLine;
            
            BigDecimal total = Env.ZERO;
            while (rs.next())
            {
                aEntryLine = new P_Payment_Entryline_360(Env.getCtx(), -1, getTrxName());

                aEntryLine.setP_Payment_ID( Payment.getP_Payment_ID() );
                aEntryLine.setAD_Org_ID( Payment.getAD_Org_ID());
                aEntryLine.setOrg_ID( Payment.getAD_Org_ID());
                
                aEntryLine.setC_Activity_ID(Post.getC_Activity_ID());
                MActivity Activity = new MActivity( Env.getCtx(), aEntryLine.getC_Activity_ID(), null);
                if ( Activity == null ||  Activity.getValue360() == null )
                {
                	Activity = new MActivity( Env.getCtx(), Employee.getC_Activity_ID(), null);
                }
                
    		    if ( Activity.getValue360().startsWith("PROD"))
    		    {
    		    	aEntryLine.setOrg_ID( 1100013 );
    		    }
                aEntryLine.setC_SalesRegion_ID( Workplace.getC_SalesRegion_ID() );
                aEntryLine.setC_ElementValue_ID( rs.getInt("C_ELEMENTVALUE_ID") );
                aEntryLine.setAmtAcctDr(rs.getBigDecimal("amt").setScale(2, BigDecimal.ROUND_HALF_UP));
                
                aEntryLine.setOrigine( "DEP" );
                aEntryLine.setLine(1);
                aEntryLine.save();
            	
                total = total.add( rs.getBigDecimal("amt").setScale(2, BigDecimal.ROUND_HALF_UP) );
            }
            rs.close();
            pstmt.close();

            
            aEntryLine = new P_Payment_Entryline_360(Env.getCtx(), -1, getTrxName());

            aEntryLine.setP_Payment_ID( Payment.getP_Payment_ID() );
            aEntryLine.setAD_Org_ID( Payment.getAD_Org_ID());
            aEntryLine.setOrg_ID( Payment.getAD_Org_ID());
            
            aEntryLine.setC_Activity_ID(Post.getC_Activity_ID());
            MActivity Activity = new MActivity( Env.getCtx(), aEntryLine.getC_Activity_ID(), null);
            if ( Activity == null ||  Activity.getValue360() == null )
            {
            	Activity = new MActivity( Env.getCtx(), Employee.getC_Activity_ID(), null);
            }
            
		    if ( Activity.getValue360().startsWith("PROD"))
		    {
		    	aEntryLine.setOrg_ID( 1100013 );
		    }
            aEntryLine.setC_SalesRegion_ID( Workplace.getC_SalesRegion_ID() );
            aEntryLine.setC_ElementValue_ID( 1105249 ); // Compte 22720
            aEntryLine.setAmtAcctCr(total);
            
            aEntryLine.setOrigine( "DEP" );
            aEntryLine.setLine(1);
            aEntryLine.save();

        }
        catch (Exception e)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " + e.toString(), getTrxName());
            log.log (Level.SEVERE,"AccoutingEntryWriter.WriteExpensereports", e);
        }		
 		
    	
    }
    
    public void WriteAccountPayable( )
    {
        P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), getTrxName());

        P_Assignment Assignment = P_Assignment.get( Env.getCtx(), Employee.getLastEmployeeAssignment( Payment.getP_Payment_ID() ), null);
		P_Post Post = P_Post.get( Env.getCtx(), Assignment.getP_Post_ID(), null);
		P_Workplace Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);

 		if ( Workplace.getC_SalesRegion_ID() == 0 )
 		{
 	 		Assignment = P_Assignment.get( Env.getCtx(), Employee.GetAssignmentPrincipal(), null);
 	 		Post = P_Post.get( Env.getCtx(), Assignment.getP_Post_ID(), null);
 	 		Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);
 			
 		}

		
// TODO ajouté paramètre pour assurance.

/*        + "case P_Deduction.P_Deduction_Family_ID when 1000001 then " 
        + " isnull( sum(isnull(ppd.employee_part,0) + isnull(ppd.accumulationAmount,0)- isnull(ppd.retrievalamount,0)),0) "
        + " else  isnull( sum(isnull(ppd.employee_part,0) + isnull(ppd.employer_part,0) + isnull(ppd.accumulationAmount,0)- isnull(ppd.retrievalamount,0)),0)  "
        + " end as Amount ,"
*/
    	String Sql = "select pp.P_Payment_ID, " + "pp.P_Payment_Group_ID , " + "ppd.P_Deduction_ID  , "
    			   + " isnull( sum(isnull(ppd.employee_part,0) + isnull(ppd.employer_part,0) + isnull(ppd.accumulationAmount,0)- isnull(ppd.retrievalamount,0)),0) as amount , "  
                   + " isnull( sum(isnull(ppd.employee_part,0) + isnull(ppd.accumulationAmount,0)- isnull(ppd.retrievalamount,0)),0) as employee_part ,"
                   + " isnull(sum( ppd.employer_part),0) as employer_part  " 
                   + "from P_Payment pp " 
                   + " LEFT OUTER JOIN P_Payment_Deduction ppd ON ( pp.p_payment_id = ppd.p_payment_id  ) "
                   + " LEFT OUTER JOIN P_Deduction ON ( P_Deduction.p_deduction_id = ppd.p_deduction_id  ) "
                   + "where " + "pp.p_payment_id = ? "
				   + " group by pp.p_payment_id, pp.P_Payment_Group_ID , P_Deduction.P_Deduction_Family_ID, ppd.P_Deduction_ID  ";

        try
        {

            PreparedStatement pstmt;

            pstmt = DB.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
            pstmt.setInt(1, Payment.getP_Payment_ID());
            ResultSet rs = pstmt.executeQuery();
            P_Deduction Deduction;
            P_Payment_Group PaymentGroup;
            P_Payment_Entryline_360 aEntryLine;
            MElementValue ElementValue;
            
            while (rs.next())
            {
                if (rs.getBigDecimal(4).signum() != 0)
                {
                    Deduction = P_Deduction.get(Env.getCtx(), rs.getInt("P_Deduction_ID"), getTrxName());
                    PaymentGroup = P_Payment_Group.get(Env.getCtx(), rs.getInt("P_Payment_Group_ID"), getTrxName());
                    if (Deduction == null)
                        P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé le code de déduction : " + rs.getInt("P_Deduction_ID"), getTrxName());

                    if (PaymentGroup == null)
                        P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé le groupe de paiement : " + rs.getInt("P_Payment_Group_ID"), getTrxName());

                    if (Deduction.getDeduction_AP_Acct_360() == 0)
                        P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé de CAP de défini pour le code de déduction : " + Deduction.getValue() + " - " + Deduction.getName(), getTrxName());

                    if (PaymentGroup.getC_Activity_ID() == 0)
                        P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé d'unité administrative de défini pour groupe de paiement : " + PaymentGroup.getName(), getTrxName());

                    // Sépart part employé et employeur
                    if ( Deduction.getDeduction_AP_Acct_360() != 0)
                    {
                    	// part employé 
                        aEntryLine = new P_Payment_Entryline_360(Env.getCtx(), -1, getTrxName());

                        aEntryLine.setP_Payment_ID(rs.getInt("P_Payment_ID"));
                        aEntryLine.setAD_Org_ID( Payment.getAD_Org_ID());
                        aEntryLine.setOrg_ID( Payment.getAD_Org_ID());
                        
/*                        
                        if ( Payment.getAD_Org_ID() == 1000001 )
                        		aEntryLine.setOrg_ID( Payment.getAD_Org_ID() );
                        else  {
                            MElementValue Cpt = new MElementValue( Env.getCtx(),  Deduction.getDeduction_AP_Acct_360(), getTrxName());
                            aEntryLine.setOrg_ID( Cpt.getAD_Org_ID());
                        }
*/
                        
                        ElementValue = new MElementValue( Env.getCtx(), Deduction.getDeduction_AP_Acct_360(), getTrxName());
                        
/*                        //Exception loyer TET
                        if ( Deduction.getValue().equals("DL02"))
                        {
                        	aEntryLine.setOrg_ID( 1000002);
                        }
*/							

                        
// 2024-10-16                        
//                        aEntryLine.setC_Activity_ID(PaymentGroup.getC_Activity_ID());
                        aEntryLine.setC_Activity_ID(Post.getC_Activity_ID());
                        

                        MActivity Activity = new MActivity( Env.getCtx(), aEntryLine.getC_Activity_ID(), null);
                        
                        if ( Activity == null ||  Activity.getValue360() == null )
                        {
                        	Activity = new MActivity( Env.getCtx(), Employee.getC_Activity_ID(), null);
                        }
                        
            		    if ( Activity.getValue360().startsWith("PROD"))
            		    {
            		    	aEntryLine.setOrg_ID( 1100013 );
            		    }
                       

//                        if (  ElementValue.getAccountType().equals( MElementValue.ACCOUNTTYPE_Expense) )
                            aEntryLine.setC_SalesRegion_ID( Workplace.getC_SalesRegion_ID() );
//                        else	
//                        	aEntryLine.setC_SalesRegion_ID( PaymentGroup.getC_SalesRegion_ID() );
                        
                        aEntryLine.setC_ElementValue_ID(Deduction.getDeduction_AP_Acct_360());
                        if (rs.getBigDecimal("employee_part").signum() == -1)
                            aEntryLine.setAmtAcctDr(rs.getBigDecimal("employee_part").abs().setScale(2, BigDecimal.ROUND_HALF_UP));
                        else
                            aEntryLine.setAmtAcctCr(rs.getBigDecimal("employee_part").setScale(2, BigDecimal.ROUND_HALF_UP));
                        
                        aEntryLine.setOrigine( "FTR" );
                        aEntryLine.setLine(1);
                        aEntryLine.save();

                        //part employeur

                        MElementValue ElementValueYeur = new MElementValue( Env.getCtx(), Deduction.getDeduction_AP_Acct_360(), getTrxName());

                        aEntryLine = new P_Payment_Entryline_360(Env.getCtx(), -1, getTrxName());

                        aEntryLine.setP_Payment_ID(rs.getInt("P_Payment_ID"));
                        aEntryLine.setAD_Org_ID( Payment.getAD_Org_ID());
                        aEntryLine.setOrg_ID( Payment.getAD_Org_ID());
/*
                        if ( Payment.getAD_Org_ID() == 1000001 )
                    		aEntryLine.setOrg_ID( Payment.getAD_Org_ID());
                        else  {
                        	MElementValue Cpt2 = new MElementValue( Env.getCtx(),  Deduction.getDeduction_AP_Acct_360(), getTrxName());
                        	aEntryLine.setOrg_ID( Cpt2.getAD_Org_ID());
                        }
*/
                        //2024-10-16
//                        aEntryLine.setC_Activity_ID(PaymentGroup.getC_Activity_ID());
                        aEntryLine.setC_Activity_ID(Post.getC_Activity_ID());
                        
                        Activity = new MActivity( Env.getCtx(), aEntryLine.getC_Activity_ID(), null);
                        
                        if ( Activity == null ||  Activity.getValue360() == null )
                        {
                        	Activity = new MActivity( Env.getCtx(), Employee.getC_Activity_ID(), null);
                        }

                        
            		    if ( Activity.getValue360().startsWith("PROD"))
            		    {
            		    	aEntryLine.setOrg_ID( 1100013 );
            		    }

//                        if (  ElementValueYeur.getAccountType().equals( MElementValue.ACCOUNTTYPE_Expense)  )
                            aEntryLine.setC_SalesRegion_ID( Workplace.getC_SalesRegion_ID() );
//                        else	
//                        	aEntryLine.setC_SalesRegion_ID( PaymentGroup.getC_SalesRegion_ID() );
                        
                        aEntryLine.setC_ElementValue_ID(Deduction.getDeduction_AP_Acct_360() );
                        if (rs.getBigDecimal("employer_part").signum() == -1)
                            aEntryLine.setAmtAcctDr(rs.getBigDecimal("employer_part").abs().setScale(2, BigDecimal.ROUND_HALF_UP));
                        else
                            aEntryLine.setAmtAcctCr(rs.getBigDecimal("employer_part").setScale(2, BigDecimal.ROUND_HALF_UP));
                        
                        aEntryLine.setOrigine( "FTR" );
                        aEntryLine.setLine(1);
                        //2023-06-21 ne sauvegarde pas les lignes a 0 
                        if ( aEntryLine.getAmtAcctDr().compareTo(Env.ZERO ) != 0 || aEntryLine.getAmtAcctCr().compareTo( Env.ZERO) != 0 ) 
                        	aEntryLine.save();

                    }
                    // Part employé et employeur summarisé
                    else
                    {
                        aEntryLine = new P_Payment_Entryline_360(Env.getCtx(), -1, getTrxName());

                        aEntryLine.setP_Payment_ID(rs.getInt("P_Payment_ID"));
                        aEntryLine.setAD_Org_ID( Payment.getAD_Org_ID());
                        aEntryLine.setOrg_ID( Payment.getAD_Org_ID());
/*
                        //                        aEntryLine.setOrg_ID( 1000001);
                        if ( Payment.getAD_Org_ID() == 1000001 )
                    		aEntryLine.setOrg_ID( Payment.getAD_Org_ID());
                        else  {
                            MElementValue Cpt = new MElementValue( Env.getCtx(),  Deduction.getDeduction_AP_Acct_360(), getTrxName());
                            aEntryLine.setOrg_ID( Cpt.getAD_Org_ID());
                        }
*/

                        
//                        aEntryLine.setC_Activity_ID(PaymentGroup.getC_Activity_ID());
                        aEntryLine.setC_Activity_ID(Post.getC_Activity_ID());
                        
                        MActivity Activity = new MActivity( Env.getCtx(), aEntryLine.getC_Activity_ID(), null);
            		    if ( Activity.getValue360().startsWith("PROD"))
            		    {
            		    	aEntryLine.setOrg_ID( 1100013 );
            		    }

                        ElementValue = new MElementValue( Env.getCtx(), Deduction.getDeduction_AP_Acct_360(), getTrxName());

                        aEntryLine.setC_SalesRegion_ID( Workplace.getC_SalesRegion_ID() );
                        
                        aEntryLine.setC_ElementValue_ID(Deduction.getDeduction_AP_Acct_360());
                        if (rs.getBigDecimal(4).signum() == -1)
                            aEntryLine.setAmtAcctDr(rs.getBigDecimal(4).abs().setScale(2, BigDecimal.ROUND_HALF_UP));
                        else
                            aEntryLine.setAmtAcctCr(rs.getBigDecimal(4).setScale(2, BigDecimal.ROUND_HALF_UP));
                        
                        aEntryLine.setOrigine( "FTR" );
                        aEntryLine.setLine(1);
                        aEntryLine.save();
                    	
                    }
                }

            }

            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " + e.toString(), getTrxName());
            log.log (Level.SEVERE,"AccoutingEntryWriter.WriteAccountPayable", e);
        }

    }

    public void WriteAccountReceivable( )
    {


        String Sql = "select " + "pp.p_payment_id,   " + "pp.P_Payment_Group_ID , " + "ppd.P_Deduction_ID  , " + "sum(isnull(ppd.accumulationAmount,0)- isnull(ppd.retrievalamount,0)) Amount " 
                   + "from " + "p_payment pp, " + "p_payment_deduction ppd " 
                   + "where " + "pp.p_payment_id = ?   " 
                   + " and " + "pp.p_payment_id = ppd.p_payment_id " 
                   + "group by pp.p_payment_id, " + "  pp.P_Payment_Group_ID , " + "  ppd.P_Deduction_ID   ";
//                   + "having cast(sum(isnull(ppd.accumulationAmount,0)-
        // isnull(ppd.retrievalamount,0)) as numeric(10,2))<> 0";

        try
        {

            PreparedStatement pstmt;

            pstmt = DB.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
            pstmt.setInt(1, Payment.getP_Payment_ID());
            ResultSet rs = pstmt.executeQuery();
            boolean error = false;


            P_Deduction Deduction;
            P_Payment_Group PaymentGroup;
            P_Payment_Entryline_360 aEntryLine;
            
            while (rs.next())
            {
                BigDecimal amount = rs.getBigDecimal(4);
                if (amount.signum() != 0)
                {
                    Deduction = P_Deduction.get(Env.getCtx(), rs.getInt("P_Deduction_ID"), getTrxName());
                    PaymentGroup = P_Payment_Group.get(Env.getCtx(), rs.getInt("P_Payment_Group_ID"), getTrxName());

                    if (Deduction == null)
                    {
                        P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé le code de déduction : " + rs.getInt("P_Deduction_ID"), getTrxName());
                        error = true;
                    }

                    if (PaymentGroup == null)
                    {
                        P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé le groupe de paiement : " + rs.getInt("P_Payment_Group_ID"), getTrxName());
                        error = true;
                    }

/*                    if (Deduction.getBack_Payment_Acct() == 0)
                    {
                        P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé de CAR de défini pour le code de déduction : " + Deduction.getValue() + " - " + Deduction.getName(), getTrxName());
                        error = true;
                    }
*/
                    if (PaymentGroup.getC_Activity_ID() == 0)
                    {
                        P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé d'unité administrative de défini pour groupe de paiement : " + PaymentGroup.getName(), getTrxName());
                        error = true;
                    }

                    if (!error)
                    {
                        aEntryLine = new P_Payment_Entryline_360(Env.getCtx(), -1, getTrxName());

                        aEntryLine.setP_Payment_ID(rs.getInt(1));
//                        aEntryLine.setC_ElementValue_ID(Deduction.getBack_Payment_Acct());
                        aEntryLine.setC_ElementValue_ID(Deduction.getDeduction_AP_Acct_360());

                        aEntryLine.setAD_Org_ID( Payment.getAD_Org_ID());
                        aEntryLine.setOrg_ID( Payment.getAD_Org_ID());
/*
                        //                        aEntryLine.setOrg_ID( 1000001);
                        if ( Payment.getAD_Org_ID() == 1000001 )
                    		aEntryLine.setOrg_ID( Payment.getAD_Org_ID() );
                        else  {
                            MElementValue Cpt = new MElementValue( Env.getCtx(),  Deduction.getDeduction_AP_Acct_360(), getTrxName());
                            aEntryLine.setOrg_ID( Cpt.getAD_Org_ID());
                        }
*/

                        P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), getTrxName());
                		P_Assignment Assignment = P_Assignment.get( Env.getCtx(), Employee.getLastEmployeeAssignment( Payment.getP_Payment_ID() ), null);
                		P_Post Post = P_Post.get( Env.getCtx(), Assignment.getP_Post_ID(), null);
                		P_Workplace Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);

                 		if ( Workplace.getC_SalesRegion_ID() == 0 )
                 		{
                 	 		Assignment = P_Assignment.get( Env.getCtx(), Employee.GetAssignmentPrincipal(), null);
                 	 		Post = P_Post.get( Env.getCtx(), Assignment.getP_Post_ID(), null);
                 	 		Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);
                 			
                 		}

                		
                        aEntryLine.setC_Activity_ID( Post.getC_Activity_ID());
                        MActivity Activity = new MActivity( Env.getCtx(), aEntryLine.getC_Activity_ID(), null);
            		    if ( Activity.getValue360().startsWith("PROD"))
            		    {
            		    	aEntryLine.setOrg_ID( 1100013 );
            		    }

                        aEntryLine.setC_SalesRegion_ID( Workplace.getC_SalesRegion_ID() );
//                        aEntryLine.setC_SalesRegion_ID( PaymentGroup.getC_SalesRegion_ID() );

                        if (amount.signum() == -1)
                            aEntryLine.setAmtAcctCr(amount.abs().setScale(2, BigDecimal.ROUND_HALF_UP));
                        else
                            aEntryLine.setAmtAcctDr(amount.setScale(2, BigDecimal.ROUND_HALF_UP));

                        aEntryLine.setOrigine( "FTR" );

                        aEntryLine.setLine(1);

                        aEntryLine.save();
                    }
                }

            }

            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " + e.toString(), getTrxName());
            log.log (Level.SEVERE,"AccoutingEntryWriter.WriteAccountReceivable", e);
        }

    }
    

/*    public void WriteAccountPayableCredits()
    {


        String Sql = "select pp.p_payment_id,   " 
        	       + "pp.P_Payment_Group_ID , " 
				   + "ppd.P_Credits_ID, " 
        	       + "ppd.PMovementType, "
				   + "sum(isnull(ppd.PMovementVariation,0))  Amount ,"
				   + "sum(isnull(ppd.Amt_Reserve,0)) Amt_Reserve "
                   + "from p_payment pp "
                   + "inner join p_credits_movement ppd on pp.p_payment_id = ppd.p_payment_id "
                   + "inner join p_credits pd on pd.p_credits_id = ppd.p_credits_id  "
                   + "where pp.p_payment_id = ? and " 
				   + "pd.isReporting = 'Y' " 
                   + " AND isnull(ppd.Amt_Reserve,0) <> 0 "
				   + "group by pp.p_payment_id," 
				   + "  pp.P_Payment_Group_ID , " 
				   + "  ppd.P_Credits_ID, "
				   + "  ppd.PMovementType "
				   ;

        try
        {

            PreparedStatement pstmt;

            pstmt = DB.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
            pstmt.setInt(1, Payment.getP_Payment_ID());
            ResultSet rs = pstmt.executeQuery();

            P_Credits Credits;
            P_Payment_Group PaymentGroup;
            P_Payment_Entryline_360 aEntryLine;
            while (rs.next())
            {
//                if (rs.getBigDecimal(4).signum() != 0)
//                {

                    Credits = P_Credits.get(Env.getCtx(), rs.getInt("P_Credits_ID"), getTrxName());
                    PaymentGroup = P_Payment_Group.get(Env.getCtx(), rs.getInt("P_Payment_Group_ID"), getTrxName());

                    if (Credits == null)
                        P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé le code de banque : " + rs.getInt("P_Credits_ID"), getTrxName());

                    if (PaymentGroup == null)
                        P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé le groupe de paiement : " + rs.getInt("P_Payment_Group_ID"), getTrxName());

                    if (Credits.getCredits_Acct() == 0)
                        P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé de compte comptable de défini pour le code de banque : " + Credits.getValue() + " - " + Credits.getName(), getTrxName());

                    if (PaymentGroup.getC_Activity_ID() == 0)
                        P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé d'unité administrative de défini pour groupe de paiement : " + PaymentGroup.getName(), getTrxName());

                    aEntryLine = new P_Payment_Entryline_360(Env.getCtx(), -1, getTrxName());

                    aEntryLine.setP_Payment_ID(rs.getInt(1));
                    aEntryLine.setC_ElementValue_ID(Credits.getCredits_Acct());

                    aEntryLine.setAD_Org_ID( Payment.getAD_Org_ID());
//                    aEntryLine.setOrg_ID( 1000001);
                    if ( Payment.getAD_Org_ID() == 1000001 )
                		aEntryLine.setOrg_ID( Payment.getAD_Org_ID() );
                    else  {
                        MElementValue Cpt = new MElementValue( Env.getCtx(),  Credits.getCredits_Acct(), getTrxName());
                        aEntryLine.setOrg_ID( Cpt.getAD_Org_ID());
                    }

                    

//                    aEntryLine.setC_Activity_ID(PaymentGroup.getC_Activity_ID());

                    
            		    P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), null );
            		    P_Assignment assignment = P_Assignment.get(Env.getCtx(), Employee.GetAssignmentPrincipal(), null);
            		    P_Post Post = P_Post.get( Env.getCtx(), assignment.getP_Post_ID(), null);
            		    P_Workplace Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID() , null); 
                        aEntryLine.setC_SalesRegion_ID( Workplace.getC_SalesRegion_ID() );
                        
                    	aEntryLine.setC_Activity_ID(Post.getC_Activity_ID());
                    	MActivity Activity = new MActivity( Env.getCtx(), aEntryLine.getC_Activity_ID(), null);
                        if ( Activity.getValue360().startsWith("PROD"))
                        {
                        	aEntryLine.setOrg_ID( 1100013 );
                        }
                        
                    // aEntryLine.setC_SalesRegion_ID( PaymentGroup.getC_SalesRegion_ID() );

//                    if ( rs.getString( "PMovementType").trim().equals( "PRO") || rs.getString( "PMovementType").trim().equals( "PRD") )
                    {
                        if (rs.getBigDecimal("Amt_Reserve").signum() == -1)
                            aEntryLine.setAmtAcctDr(rs.getBigDecimal("Amt_Reserve").abs().setScale(2, BigDecimal.ROUND_HALF_UP));
                        else
                            aEntryLine.setAmtAcctCr(rs.getBigDecimal("Amt_Reserve").setScale(2, BigDecimal.ROUND_HALF_UP));
                    }

                    aEntryLine.setOrigine( "FTB" );

                    aEntryLine.setLine(1);

                    aEntryLine.save();
                    

                }

//            }

            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " + e.toString(), getTrxName());
            log.log (Level.SEVERE,"AccoutingEntryWriter.WriteAccountReceivableCredits", e);
        }

    }
    */

    public void WriteCashBalance()
    {

        if ( Payment.getNetPay() == null ) return;

    	//X_P_Accounting_EntryLine aEntryLine = null;
        P_BankAccount BankAccount = P_BankAccount.getByClientID(Env.getCtx(), Payment.getAD_Client_ID(), getTrxName());

        P_Payment_Group PaymentGroup = P_Payment_Group.get(Env.getCtx(), Payment.getP_Payment_Group_ID(), getTrxName());

        if (BankAccount == null)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé de compte bancaire de défini   ", getTrxName());
            return;
        }

        if (PaymentGroup == null)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé le groupe de paiement : " + Payment.getP_Payment_Group_ID(), getTrxName());
            return;
        }

        if (BankAccount.getP_Current_Acct_360() == 0)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé de compte comptable de défini pour le compte bancaire : ", getTrxName());
            return;
        }

        if (PaymentGroup.getC_Activity_ID() == 0)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé d'unité administrative de défini pour groupe de paiement : " + PaymentGroup.getName(), getTrxName());
            return;
        }

        P_Payment_Entryline_360 aEntryLine = new P_Payment_Entryline_360(Env.getCtx(), -1, getTrxName());

        aEntryLine.setP_Payment_ID(Payment.getP_Payment_ID());
        aEntryLine.setC_ElementValue_ID(BankAccount.getP_Current_Acct_360());

        aEntryLine.setAD_Org_ID( Payment.getAD_Org_ID());
        aEntryLine.setOrg_ID( Payment.getAD_Org_ID());
/*        
//        aEntryLine.setOrg_ID( 1000001);
        if ( Payment.getAD_Org_ID() == 1000001 )
    		aEntryLine.setOrg_ID( Payment.getAD_Org_ID() );
        else  {
            MElementValue Cpt = new MElementValue( Env.getCtx(),  BankAccount.getP_Current_Acct(), getTrxName());
            aEntryLine.setOrg_ID( Cpt.getAD_Org_ID());
        }
*/
        
        aEntryLine.setC_Activity_ID(PaymentGroup.getC_Activity_ID());
        MActivity Activity = new MActivity( Env.getCtx(), aEntryLine.getC_Activity_ID(), null);
	    if ( Activity.getValue360().startsWith("PROD"))
	    {
	    	aEntryLine.setOrg_ID( 1100013 );
	    }

	    P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), null );
	    P_Assignment assignment = P_Assignment.get(Env.getCtx(), Employee.getLastEmployeeAssignment( Payment.getP_Payment_ID() ), null);
	    P_Post Post = P_Post.get( Env.getCtx(), assignment.getP_Post_ID(), null);
	    P_Workplace Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID() , null); 

 		if ( Workplace.getC_SalesRegion_ID() == 0 )
 		{
 			assignment = P_Assignment.get( Env.getCtx(), Employee.GetAssignmentPrincipal(), null);
 	 		Post = P_Post.get( Env.getCtx(), assignment.getP_Post_ID(), null);
 	 		Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);
 			
 		}
	    
	    aEntryLine.setC_SalesRegion_ID( Workplace.getC_SalesRegion_ID() );
//        aEntryLine.setC_SalesRegion_ID( PaymentGroup.getC_SalesRegion_ID() );

        if (Payment.getNetPay().signum() == -1)
            aEntryLine.setAmtAcctDr(Payment.getNetPay().abs().setScale(2,BigDecimal.ROUND_HALF_UP));
        else
            aEntryLine.setAmtAcctCr(Payment.getNetPay().setScale(2,BigDecimal.ROUND_HALF_UP));

        aEntryLine.setOrigine( "FTP" );

        aEntryLine.setLine(1);

//        if ( Payment.getNetPay().compareTo( Env.ZERO) != 0 )
       	aEntryLine.save();
    }

/*    
    public void InterCompagnie(  )
    {
    	P_Payment_Group PaymentGroup = P_Payment_Group.get(Env.getCtx(), Payment.getP_Payment_Group_ID(), getTrxName());

    	X_P_Interorg_Acct Inter = new X_P_Interorg_Acct( Env.getCtx(), 9000 , m_trxName);
    	if ( Payment.getAD_Org_ID() == 1000002 )
    	{
    		String sql = " select sum( P_PAYMENT_ENTRYLINE.AMTACCTDR) AMTACCTDR, SUM( P_PAYMENT_ENTRYLINE.AMTACCTCR) AMTACCTCR from P_PAYMENT_ENTRYLINE WHERE ORG_ID = " + Payment.getAD_Org_ID()
    		           + " AND P_PAYMENT_ID = " +  Payment.getP_Payment_ID();
	        try
	        {
	
	            PreparedStatement pstmt;
	
	            pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
	//            pstmt.setInt(1, Payment.getP_Payment_ID());
	            ResultSet rs = pstmt.executeQuery();
	            while ( rs.next() )
	            {
	                P_Payment_Entryline aEntryLine = new P_Payment_Entryline(Env.getCtx(), -1, getTrxName());
	                aEntryLine.setP_Payment_ID(Payment.getP_Payment_ID());
	                aEntryLine.setC_ElementValue_ID( Inter.getIntercompanyDueFrom_Acct() );
	
                    aEntryLine.setAD_Org_ID( Payment.getAD_Org_ID());
                    //aEntryLine.setOrg_ID( 1000001);
                    if ( Payment.getAD_Org_ID() == 1000001 )
                		aEntryLine.setOrg_ID( Payment.getAD_Org_ID() );
                    else  {
                        MElementValue Cpt = new MElementValue( Env.getCtx(),  Inter.getIntercompanyDueFrom_Acct(), getTrxName());
                        aEntryLine.setOrg_ID( Cpt.getAD_Org_ID());
                    }
                    


	                aEntryLine.setC_Activity_ID( PaymentGroup.getC_Activity_ID() );
            		    P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), null );
            		    P_Assignment assignment = P_Assignment.get(Env.getCtx(), Employee.GetAssignmentPrincipal(), null);
            		    P_Post Post = P_Post.get( Env.getCtx(), assignment.getP_Post_ID(), null);
            		    P_Workplace Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID() , null); 
                        aEntryLine.setC_SalesRegion_ID( Workplace.getC_SalesRegion_ID() );
//	                aEntryLine.setC_SalesRegion_ID( PaymentGroup.getC_SalesRegion_ID() );
	
	                aEntryLine.setOrigine( "INT" );
	
	                BigDecimal Dr = rs.getBigDecimal("AMTACCTDR"); //.subtract( rs.getBigDecimal("AmtAcctCr") );
	                BigDecimal Cr = rs.getBigDecimal("AMTACCTCR"); //.subtract( rs.getBigDecimal("AmtAcctCr") );
	                
	                if ( Dr == null ) Dr = Env.ZERO;
	                if ( Cr == null ) Cr = Env.ZERO;
	                
                    aEntryLine.setAmtAcctCr( Cr );
                    aEntryLine.setAmtAcctDr( Dr);
	                
	                aEntryLine.setLine(8);
	               	aEntryLine.save();

	                aEntryLine = new P_Payment_Entryline(Env.getCtx(), -1, getTrxName());
	                aEntryLine.setP_Payment_ID(Payment.getP_Payment_ID());
	                aEntryLine.setC_ElementValue_ID( Inter.getIntercompanyDueTo_Acct()  );
	
                    aEntryLine.setAD_Org_ID( Payment.getAD_Org_ID());
                    //aEntryLine.setOrg_ID( 1000002);
                    if ( Payment.getAD_Org_ID() == 1000001 )
                		aEntryLine.setOrg_ID( Payment.getAD_Org_ID() );
                    else  {
                        MElementValue Cpt2 = new MElementValue( Env.getCtx(),  Inter.getIntercompanyDueTo_Acct() , getTrxName());
                        aEntryLine.setOrg_ID( Cpt2.getAD_Org_ID());
                    }


	                aEntryLine.setC_Activity_ID( PaymentGroup.getC_Activity_ID() );
            		    P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), null );
            		    P_Assignment assignment = P_Assignment.get(Env.getCtx(), Employee.GetAssignmentPrincipal(), null);
            		    P_Post Post = P_Post.get( Env.getCtx(), assignment.getP_Post_ID(), null);
            		    P_Workplace Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID() , null); 
                        aEntryLine.setC_SalesRegion_ID( Workplace.getC_SalesRegion_ID() );
//	                aEntryLine.setC_SalesRegion_ID( PaymentGroup.getC_SalesRegion_ID() );

                    aEntryLine.setAmtAcctCr( Dr );
                    aEntryLine.setAmtAcctDr( Cr);

	                aEntryLine.setOrigine( "INT" );
	
	                aEntryLine.setLine(9);
	               	aEntryLine.save();

	               	
	            }
	            rs.close();
	            pstmt.close();
	
	        }
	        catch (Exception e)
	        {
	            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "InterCompagnie - Internal error - Exception  " + e.toString(), getTrxName());
	            log.log (Level.SEVERE,"AccoutingEntryWriter.InterCompagnie", e);
	        }
    	}
    }
*/    
    public void WriteImputation( )
    {
        //X_P_Accounting_EntryLine aEntryLine = null;

        String Sql = "select ppg.p_payment_id, " 
                   + "ppgd.c_activity_id ," 
                   + "ppgd.C_SalesRegion_ID, "
                   + "ppgd.Org_ID, "
				   + "ppgd.account_id account,"
				   + "sum(amount) amount " 
                   + "from " + "p_payment_gain ppg "
				   + " inner join P_Payment_Gain_Distribution_360 ppgd on ppg.p_payment_gain_id = ppgd.p_payment_gain_id " 
                   + " where " + " ppg.p_payment_id = ? and " 
				   + "ppgd.distribution_type in ('A','S','B')  " 
				   + " group by ppg.p_payment_id,ppgd.c_activity_id, ppgd.C_SalesRegion_ID, ppgd.Org_ID, ppgd.account_id " 
				   ;

        try
        {

            //			Pour des fins de tests
            //	org.compiere.Compiere.startupClient();
            PreparedStatement pstmt;

            pstmt = DB.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
            pstmt.setInt(1, Payment.getP_Payment_ID());

            ResultSet rs = pstmt.executeQuery();
            
            P_Payment_Entryline_360 aEntryLine;
            MElementValue ElementValue;
            P_Payment_Group PaymentGroup = P_Payment_Group.get(Env.getCtx(), Payment.getP_Payment_Group_ID(), getTrxName());

            
            while (rs.next())
            {
//                if (rs.getBigDecimal(4).signum() != 0)
//                {
                    aEntryLine = new P_Payment_Entryline_360(Env.getCtx(), -1, getTrxName());

                    aEntryLine.setP_Payment_ID(rs.getInt("P_Payment_ID"));

/*                    P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), getTrxName());
            		P_Assignment Assignment = P_Assignment.get( Env.getCtx(), Employee.GetAssignmentPrincipal(), null);
            		P_Post Post = P_Post.get( Env.getCtx(), Assignment.getP_Post_ID(), null);
            		P_Workplace Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);
  */
                    
                    aEntryLine.setC_Activity_ID(rs.getInt("C_Activity_ID"));
//                    aEntryLine.setC_Activity_ID(Post.getC_Activity_ID());
                    MActivity Activity = new MActivity( Env.getCtx(), aEntryLine.getC_Activity_ID(), null);
        		    if ( Activity != null && Activity.getValue360().startsWith("PROD"))
        		    {
        		    	aEntryLine.setOrg_ID( 1100013 );
        		    }

                    aEntryLine.setC_ElementValue_ID(rs.getInt("account"));

                    aEntryLine.setAD_Org_ID( Payment.getAD_Org_ID());
                    aEntryLine.setOrg_ID( Payment.getAD_Org_ID());

/*                    if ( Payment.getAD_Org_ID() == 1000001 )
                		aEntryLine.setOrg_ID( Payment.getAD_Org_ID() );
                    else  {
                        MElementValue Cpt2 = new MElementValue( Env.getCtx(),  rs.getInt("account") , getTrxName());
                        aEntryLine.setOrg_ID( Cpt2.getAD_Org_ID());
                    }
*/
                    aEntryLine.setOrg_ID( rs.getInt("Org_ID"));

                    //DEBUG
//                    MSalesRegion SalesRegion = MSalesRegion.get(Env.getCtx(), rs.getInt("C_SalesRegion_ID"));
//                    log.log (Level.WARNING,"**DEBUG** WriteImputation : SalesRegion " + SalesRegion.getName() );
                    
                    aEntryLine.setC_SalesRegion_ID( rs.getInt("C_SalesRegion_ID") );

                    ElementValue = new MElementValue( Env.getCtx(), rs.getInt("account"), getTrxName());
//					if (  ElementValue.getAccountType().equals( MElementValue.ACCOUNTTYPE_Asset)  )
 //                   	aEntryLine.setC_SalesRegion_ID( PaymentGroup.getC_SalesRegion_ID() );

                    
                    if (rs.getBigDecimal("amount").signum() == -1)
                        aEntryLine.setAmtAcctCr(rs.getBigDecimal("amount").abs().setScale(2, BigDecimal.ROUND_HALF_UP));
                    else
                        aEntryLine.setAmtAcctDr(rs.getBigDecimal("amount").setScale(2, BigDecimal.ROUND_HALF_UP));

                    aEntryLine.setOrigine( "FTD" );

                    aEntryLine.setLine(1);
                    aEntryLine.save();
                }
//            }

            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " + e.toString(), getTrxName());
            log.log (Level.SEVERE,"AccoutingEntryWriter.WriteImputation", e);
        }
    }

    public GainDistributionList GetPaymentGainDistribution( P_Deduction_Param DeductionParam ) throws Exception
    {
        GainDistributionList distList = new GainDistributionList();

        totalAmount = Env.ZERO;

        boolean found = false;
        String sql1;
        String sql2;

        // Si la déduction est pour une colonne d'admissibilité alors on trouve tout les gains 
        // admissible on calcul le prorata sur tout les gains.

        sql1 = "select ppgd.p_payment_gain_distribution_360_id DistributionId," 
 	       + "ppgd.amount Amount, ppg.P_Payment_Gain_ID, ppg.P_Payment_ID, ppgd.C_Activity_Id,  ppg.P_Job_Title_ID, ppgd.Line, ppgd.C_SalesRegion_ID, ppgd.Org_ID " 
            + "from p_payment_gain ppg " 
			+ " INNER JOIN p_payment_gain_distribution_360 ppgd ON ppg.p_payment_gain_id = ppgd.p_payment_gain_id " 
            + " INNER JOIN p_gain pg ON ppg.p_gain_id = pg.p_gain_id " 
			+ " INNER JOIN p_method_gain pmg ON pg.p_method_gain_id = pmg.p_method_gain_id"
			+ " INNER JOIN P_Gain_Column ON P_Gain_Column.P_Column_Adm_ID = " + DeductionParam.getP_Column_Adm_ID()
//			+ " INNER JOIN P_Post ON P_Post.P_Post_ID = ppg.P_Post_ID "
//			+ " INNER JOIN P_Payment_Gain " 
            + "where ppg.p_payment_id =? AND " 
			   + "(pmg.isprorate_allow='Y') and " 
			   + " ppgd.distribution_type = 'S' " 
			   + " AND P_Gain_Column.P_Gain_Parameter_ID = ppg.P_Gain_Parameter_ID"
//			   + " AND ppgd.P_Payment_Gain_ID = P_Payment_Gain.P_Payment_Gain_ID" 
//			   + " AND P_Gain_Column.P_Gain_Parameter_ID = P_Payment_Gain.P_Gain_Parameter_ID" 
			   + " AND P_Gain_Column.IsAdmissible = 'Y'"; 

        //
        // Si la déduction n'est pas pour une colonne d'admissibilité ou s'il aucun gain n'a été trouvé pour cette
        // déduction, alors on calcul le prorata sur tout les gains.
        //
        sql2 = "select ppgd.p_payment_gain_distribution_360_id DistributionId, " 
  	       + "ppgd.amount Amount, ppg.P_Payment_Gain_ID, ppg.P_Payment_ID, ppgd.C_Activity_Id, ppg.P_Job_Title_ID, ppgd.Line, ppgd.C_SalesRegion_ID, ppgd.Org_ID " 
             + "from p_payment_gain ppg  " 
 			 + " INNER JOIN p_payment_gain_distribution_360 ppgd ON ppg.p_payment_gain_id = ppgd.p_payment_gain_id " 
             + " INNER JOIN p_gain pg ON ppg.p_gain_id = pg.p_gain_id  " 
 			 + " INNER JOIN p_method_gain pmg ON pg.p_method_gain_id = pmg.p_method_gain_id " 
// 			 + " INNER JOIN P_Post ON P_Post.P_Post_ID = ppg.P_Post_ID "
             + "where ppg.p_payment_id =? and " 
 			   + "(pmg.isprorate_allow='Y') and " 
 			   + "ppgd.distribution_type = 'S' "; 


     if ( DeductionParam.getP_Column_Adm_ID() != 0)
     {
         PreparedStatement pstmt = DB.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
         pstmt.setInt(1, Payment.getP_Payment_ID());
         ResultSet rs = pstmt.executeQuery();

         while (rs.next())
         {
        	 int C_Activity_Id = rs.getInt("C_Activity_Id");
        	 if ( rs.getInt("C_Activity_Id") ==1001041 )
        	 {
        	     P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), getTrxName());

        		 C_Activity_Id = Employee.getC_Activity_ID();
        	 }
        	 else
        		 C_Activity_Id = rs.getInt("C_Activity_Id");
        		 
			 distList.AddDistribution(rs.getBigDecimal("P_Payment_ID"), rs.getBigDecimal("P_Payment_Gain_ID"), new BigDecimal(C_Activity_Id), new BigDecimal( Payment.getP_Occupation_Group_ID() ), Env.ZERO, rs.getBigDecimal("C_SalesRegion_ID"), rs.getBigDecimal("Org_Id"),  rs.getBigDecimal("Line"), rs.getBigDecimal("DistributionId"), rs.getBigDecimal("Amount"));
             found = true;
             totalAmount = totalAmount.add( rs.getBigDecimal(2));
         }

         rs.close();
         pstmt.close();
     }

     if ( ! found )
     {
         PreparedStatement pstmt = DB.prepareStatement(sql2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
         pstmt.setInt(1, Payment.getP_Payment_ID());
         ResultSet rs = pstmt.executeQuery();

         while (rs.next())
         {
             distList.AddDistribution(rs.getBigDecimal("P_Payment_ID"), rs.getBigDecimal("P_Payment_Gain_ID"), rs.getBigDecimal("C_Activity_Id"), new BigDecimal( Payment.getP_Occupation_Group_ID() ), Env.ZERO, rs.getBigDecimal("C_SalesRegion_ID"), rs.getBigDecimal("Org_Id"), rs.getBigDecimal("Line"), rs.getBigDecimal("DistributionId"), rs.getBigDecimal("Amount"));
             found = true;
             totalAmount = totalAmount.add( rs.getBigDecimal(2));
         }

         rs.close();
         pstmt.close();
     }

     if ( ! found )
     {
    	 return null;
     }
     
        return distList;
    }
    
    
    //Fournit les distributions salariales pour un paiement donné qui sont
    // admissibles au bénéfices marginaux
    public GainDistributionList GetPaymentGainDistribution()
    {
        GainDistributionList distList = new GainDistributionList();

        String sql = "select ppgd.P_Payment_Gain_Distribution_360_id DistributionId, " 
        	       + "ppgd.amount Amount, ppg.P_Payment_Gain_ID, ppg.P_Payment_ID, ppgd.C_Activity_Id, ppg.P_Job_Title_ID, ppgd.Line, ppgd.C_SalesRegion_ID, ppgd.Org_ID  " 
                   + "from p_payment_gain ppg " 
				   + " inner join P_Payment_Gain_Distribution_360 ppgd on ppg.p_payment_gain_id = ppgd.p_payment_gain_id " 
                   + " inner join p_gain pg on ppg.p_gain_id = pg.p_gain_id  " 
				   + " inner join p_method_gain pmg on pg.p_method_gain_id = pmg.p_method_gain_id  "
// 	 			   + " INNER JOIN P_Post ON P_Post.P_Post_ID = ppg.P_Post_ID "
                   
                   + "where ppg.p_payment_id =? and " 
				   + "(pmg.isprorate_allow='Y') and " 
				   + "ppgd.distribution_type = 'S' "; 

        try
        {

            PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
            pstmt.setInt(1, Payment.getP_Payment_ID());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
	           	 int C_Activity_Id = rs.getInt("C_Activity_Id");
	           	 if ( rs.getInt("C_Activity_Id") ==1001041 )
	           	 {
	           	     P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), getTrxName());
	
	           		 C_Activity_Id = Employee.getC_Activity_ID();
	           	 }
	           	 else
	           		 C_Activity_Id = rs.getInt("C_Activity_Id");
           	 
            	distList.AddDistribution(rs.getBigDecimal("P_Payment_ID"), rs.getBigDecimal("P_Payment_Gain_ID"), new BigDecimal( C_Activity_Id ), new BigDecimal( Payment.getP_Occupation_Group_ID() ), Env.ZERO, rs.getBigDecimal("C_SalesRegion_ID"), rs.getBigDecimal("Org_Id"), rs.getBigDecimal("Line"),rs.getBigDecimal("DistributionId"),rs.getBigDecimal("Amount"));
            }

            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " + e.toString(), getTrxName());
            log.log (Level.SEVERE,"MarginalProfitWriter.LoadPaymentIdList", e);
        }

        return distList;
    }

    
    //Fournit les distributions salariales pour un paiement donné qui sont
    // admissibles au bénéfices marginaux
/*    
    public GainDistributionList GetPaymentGainDistribution( P_Deduction_Param DeductionParam ) throws Exception
    {
        GainDistributionList distList = new GainDistributionList();

        totalAmount = Env.ZERO;

        boolean found = false;
        String sql1;
        String sql2;

        // Si la déduction est pour une colonne d'admissibilité alors on trouve tout les gains 
        // admissible on calcul le prorata sur tout les gains.

        sql1 = "select ppgd.P_Payment_Gain_Distribution_360_id DistributionId," 
 	       + "ppgd.amount Amount, ppg.P_Payment_Gain_ID, ppg.P_Payment_ID, ppgd.C_Activity_Id,  ppg.P_Job_Title_ID, ppgd.Line, ppgd.C_SalesRegion_ID, ppgd.Org_ID " 
            + "from p_payment_gain ppg, " 
			   + "P_Payment_Gain_Distribution_360 ppgd ," 
            + "p_gain pg , " 
			   + "p_method_gain pmg,  P_Gain_Column, P_Payment_Gain " 
            + "where ppg.p_gain_id = pg.p_gain_id and " 
			   + "pg.p_method_gain_id = pmg.p_method_gain_id and " 
			   + "ppg.p_payment_gain_id = ppgd.p_payment_gain_id and " 
			   + "ppg.p_payment_id =? and " 
			   + "(pmg.isprorate_allow='Y') and " 
			   + "ppgd.distribution_type = 'S' " 
			   + " AND ppgd.P_Payment_Gain_ID = P_Payment_Gain.P_Payment_Gain_ID" 
			   + " AND P_Gain_Column.P_Gain_Parameter_ID = P_Payment_Gain.P_Gain_Parameter_ID" 
			   + " AND P_Gain_Column.P_Column_Adm_ID = " + DeductionParam.getP_Column_Adm_ID()
			   + " AND P_Gain_Column.IsAdmissible = 'Y'"; 

        //
        // Si la déduction n'est pas pour une colonne d'admissibilité ou s'il aucun gain n'a été trouvé pour cette
        // déduction, alors on calcul le prorata sur tout les gains.
        //
        sql2 = "select ppgd.P_Payment_Gain_Distribution_360_id DistributionId, " 
  	       + "ppgd.amount Amount, ppg.P_Payment_Gain_ID, ppg.P_Payment_ID, ppgd.C_Activity_Id, ppg.P_Job_Title_ID, ppgd.Line, ppgd.C_SalesRegion_ID, ppgd.Org_ID " 
             + "from p_payment_gain ppg, " 
 			   + "P_Payment_Gain_Distribution_360 ppgd ," 
             + "p_gain pg , " 
 			   + "p_method_gain pmg " 
             + "where ppg.p_gain_id = pg.p_gain_id and " 
 			   + "pg.p_method_gain_id = pmg.p_method_gain_id and " 
 			   + "ppg.p_payment_gain_id = ppgd.p_payment_gain_id and " 
 			   + "ppg.p_payment_id =? and " 
 			   + "(pmg.isprorate_allow='Y') and " 
 			   + "ppgd.distribution_type = 'S' "; 


     if ( DeductionParam.getP_Column_Adm_ID() != 0)
     {
         PreparedStatement pstmt = DB.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
         pstmt.setInt(1, Payment.getP_Payment_ID());
         ResultSet rs = pstmt.executeQuery();

         while (rs.next())
         {
			 distList.AddDistribution(rs.getBigDecimal("P_Payment_ID"), rs.getBigDecimal("P_Payment_Gain_ID"), rs.getBigDecimal("C_Activity_Id"), new BigDecimal( Payment.getP_Occupation_Group_ID() ), rs.getBigDecimal("P_Job_Title_ID"), rs.getBigDecimal("Line"), rs.getBigDecimal("C_SalesRegion_ID"), rs.getBigDecimal("Org_Id"), rs.getBigDecimal("DistributionId"), rs.getBigDecimal("Amount"));
             found = true;
             totalAmount = totalAmount.add( rs.getBigDecimal(2));
         }

         rs.close();
         pstmt.close();
     }

     if ( ! found )
     {
         PreparedStatement pstmt = DB.prepareStatement(sql2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
         pstmt.setInt(1, Payment.getP_Payment_ID());
         ResultSet rs = pstmt.executeQuery();

         while (rs.next())
         {
             distList.AddDistribution(rs.getBigDecimal("P_Payment_ID"), rs.getBigDecimal("P_Payment_Gain_ID"), rs.getBigDecimal("C_Activity_Id"), new BigDecimal( Payment.getP_Occupation_Group_ID() ), rs.getBigDecimal("P_Job_Title_ID"), rs.getBigDecimal("Line"), rs.getBigDecimal("C_SalesRegion_ID"), rs.getBigDecimal("Org_Id"), rs.getBigDecimal("DistributionId"), rs.getBigDecimal("Amount"));
             found = true;
             totalAmount = totalAmount.add( rs.getBigDecimal(2));
         }

         rs.close();
         pstmt.close();
     }

     if ( ! found )
     {
    	 return null;
     }
     
        return distList;
    }
*/
    
    //Calculer les bénéfices marginaux concernant un seul paiement.
/*    public void WriteMargnalProfitByPayment( )
    {
        GainDeductionList360 deductionList = null;
        GainDistributionList distributionList = null;

        deductionList = GetGainDeductionList();
        distributionList = GetPaymentGainDistribution();

        //Calcul du gain total admissible
        totalAmount = GetPaymentEligibleGain().setScale(2,BigDecimal.ROUND_HALF_UP);

        if ( totalAmount == null)
        	return;
        
        //Log.trace( 5,"Montant Elligible : " + totalAmount.toString() +
        // " --- PaimentID : " + ((BigDecimal)paymentIdList.get(i)).toString());
        //Calcul du prorata de chaque distribution
        ComputeDistributionPct(distributionList, totalAmount);

        //Calcul des bénéfices marginaux et enregistrement
        ComputeMarginalProfit(distributionList, deductionList);
        //}
    }
*/
    
    public BigDecimal GetPaymentEligibleGain()
    {
        BigDecimal amount = Env.ZERO;
        String sql = "select " + " sum(ppg.amountcalc) ElligibleGain " 
                   + "from p_payment_gain ppg ," 
				   + "p_gain pg," 
				   + "p_method_gain pmg " 
                   + "where ppg.p_gain_id = pg.p_gain_id and " 
				   + "pg.p_method_gain_id = pmg.p_method_gain_id and " 
				   + "(pmg.isprorate_allow='Y') and " 
				   + "ppg.p_payment_id =? " 
				   + "group by " + " ppg.p_payment_id ";

        try
        {

            PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
            pstmt.setInt(1, Payment.getP_Payment_ID());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next())
            {
                amount = rs.getBigDecimal(1);
            }

            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " + e.toString(), getTrxName());
            log.log (Level.SEVERE,"MarginalProfitWriter.GetPaymentEligibleGain", e);
        }

        return amount;
    }

    

    //Fournit les déductions sur un paiement donné.
    
    //1- Aller chercher les deductions pour un paiement donné
    //2- Aller chercher les comptes comptable associé a cette déduction
    //3- Choisir le compte selon a) Le Occupation_Group_ID, b) Le Job_Type_ID
    //4- Inscrire la ligne dans le Grand livre temporaire
    public GainDeductionList360 GetGainDeductionList( )
    {
        GainDeductionList360 deductionList = new GainDeductionList360();

        //On commence par aller chercher toutes les déduction sur le paiement
        String sqlMaster = "SELECT ppd.p_payment_deduction_id , "
        				+" ppd.employer_part , "
						+" ppd.P_DEDUCTION_ID , "
						+" ppd.P_Employee_ID ,  ppd.p_deduction_id"
                   + " FROM p_payment_deduction ppd " 
                   + " WHERE ppd.p_payment_id = ? "//1-Payment.Payment_ID
				   ;
        try
        {

            PreparedStatement pstmt = DB.prepareStatement(sqlMaster, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, getTrxName());
        	//PreparedStatement pstmt = DB.prepareStatement(sqlMaster);
            pstmt.setInt(1, Payment.getP_Payment_ID());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
            	
            	// une deduction peut etre negative mais ne doit etre egal a 0
                if (rs.getBigDecimal("employer_part").compareTo( Env.ZERO ) != 0)
                {
                    	deductionList.AddDeduction(rs.getBigDecimal("p_payment_deduction_id"), rs.getBigDecimal("employer_part"), rs.getBigDecimal( "P_Deduction_ID"));
                }
            }

            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " + e.toString(), getTrxName());
            log.log (Level.SEVERE,"MarginalProfitWriter.LoadPaymentIdList", e);
        }

        return deductionList;
    }

    //Calcule les bénéfices marginaux en fonction du pourcentage des
    // distributions / rapport au gain admissible
    public void ComputeMarginalProfit(GainDistributionList gdl, GainDeductionList360 deductionList)
    {
        BigDecimal mpDiff = null;
        BigDecimal deductionAmnt = null;
        BigDecimal marginalProfit = null;
        BigDecimal mpSum = null;
        //Pour chaque deduction calculer la repartition
        //On commence par calculer les BM si il y a des distributions
        // elligibles
        if (!gdl.IsEmpty())
        {
            while (deductionList.Next())
            {
                deductionAmnt = deductionList.GetDeductionAmount();

                //Pour chaque distribution
                while (gdl.Next())
                {
                    //Calculer le BM
                    marginalProfit = deductionAmnt.multiply(gdl.GetDistributionPct()).setScale(2,BigDecimal.ROUND_HALF_UP);

                    //Enregistrer l'information
/*                    if ( gdl.getP_Job_Title_ID() != null )
                    	deductionList.AddDeductionMP(gdl.getP_Payment_ID().intValue(), gdl.getPayment_Gain_Id().intValue(), gdl.getP_Occupation_Group_ID().intValue(), gdl.getP_Job_Title_ID().intValue(),gdl.getC_Activity_ID().intValue(), gdl.getLine(), 0 , gdl.getOrg_ID().intValue(), gdl.GetDistributionId(), marginalProfit);
                    else
                    {
 */                   	
                    	int SaleRegion_ID = 0;
                    	if ( gdl.getC_SalesRegion_ID() != null )
                    		SaleRegion_ID = gdl.getC_SalesRegion_ID().intValue();
                    	else
                    	{
                            P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), getTrxName());
                    		P_Assignment Assignment = P_Assignment.get( Env.getCtx(), Employee.getLastEmployeeAssignment( Payment.getP_Payment_ID() ), null);
                    		P_Post Post = P_Post.get( Env.getCtx(), Assignment.getP_Post_ID(), null);
                    		P_Workplace Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);

                     		if ( Workplace.getC_SalesRegion_ID() == 0 )
                     		{
                     	 		Assignment = P_Assignment.get( Env.getCtx(), Employee.GetAssignmentPrincipal(), null);
                     	 		Post = P_Post.get( Env.getCtx(), Assignment.getP_Post_ID(), null);
                     	 		Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);
                     			
                     		}
                    		
                    		
                    		SaleRegion_ID = Workplace.getC_SalesRegion_ID();
                    		
                    	}
                    		
                    	deductionList.AddDeductionMP(gdl.getP_Payment_ID().intValue(), gdl.getPayment_Gain_Id().intValue(), gdl.getP_Occupation_Group_ID().intValue(), 0 ,gdl.getC_Activity_ID().intValue(), gdl.getLine(), SaleRegion_ID, gdl.getOrg_ID().intValue(), gdl.GetDistributionId(), marginalProfit);
                    	
//                    }
                    	
                }

                //Réinitialiser le pointeur des distributions
                gdl.ResetIteratorPosition();

                //Valider la répartition des BM, il faut que la somme des BM
                // corresponde
                //à la deduction, si la BM n'est pas valide, ajouter la
                // différence au dernier BM
                mpSum = deductionList.GetDeductionMpSum();
                if (!mpSum.equals(deductionList.GetDeductionAmount()))
                {
                    //La somme des déductions sera inférieure ou égale à la
                    // déduction dans tous les cas
                    //Pas besoin de véririfier pour le signe de la différence.
                    mpDiff = deductionList.GetDeductionAmount().subtract(mpSum);

                    //Ajouter cette différence au dernier bénéfice marginal.
                    deductionList.AddAmountToLastMp(mpDiff);
                }

                //Enregistrer la repartition de la déduction courante
                deductionList.SaveMarginalProfits( TimeSheet, Payment, Employee, getTrxName() );
            }

            //Réinitialiser le pointeur des déductions
            deductionList.ResetIteratorPosition();
        }
    }

    

    //	Procéde à l'écriture comptable pour un paiement donné
    public void WriteAccountingEntryByPayment( )
    {
        WriteEntry();

        if ( Payment.getTotalDebit(getTrxName()).compareTo(Payment.getTotalCredit(getTrxName())) != 0)
        {
            String message = Msg.getMsg(Env.getCtx(), "WriteAccountingEntryError");

            message = message + " - débit : " + Payment.getTotalDebit(getTrxName()).toString();
            message = message + " crédit : " + Payment.getTotalCredit(getTrxName()).toString();

            //            P_Time_Sheet TimeSheet = P_Time_Sheet.GetWithPaymentID(
            // Env.getCtx(), Payment.getP_Payment_ID() );
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, message, getTrxName());
        }

    }

    //Vérifier
    public boolean CheckAccoutingEntryByPaiement()
    {

        boolean isOk = true;

        return isOk;

    }

    //Calcule les bénéfices marginaux en fonction du pourcentage des
    // distributions / rapport au gain admissible
    public void ComputeProvision(GainDistributionList gdl, GainCreditsList CreditsList)
    {
        BigDecimal mpDiff = null;
        BigDecimal creditsAmnt = null;
        BigDecimal provision = null;
        BigDecimal mpSum = null;
        //Pour chaque credits calculer la repartition
        //On commence par calculer les BM si il y a des distributions
        // elligibles
        if (!gdl.IsEmpty())
        {
            while (CreditsList.Next())
            {
                creditsAmnt = CreditsList.GetCreditsAmount();

                //Pour chaque distribution
                while (gdl.Next())
                {
                    //Calculer le BM
                    provision = creditsAmnt.multiply(gdl.GetDistributionPct());

                    //Enregistrer l'information
                    CreditsList.AddCreditsMP(gdl.GetDistributionId(), provision);
                }

                //Réinitialiser le pointeur des distributions
                gdl.ResetIteratorPosition();

                //Valider la répartition des BM, il faut que la somme des BM
                // corresponde
                //à la credit, si la BM n'est pas valide, ajouter la
                // différence au dernier BM
                mpSum = CreditsList.GetCreditsMpSum();
                if (!mpSum.equals(CreditsList.GetCreditsAmount()))
                {
                    //La somme des déductions sera inférieure ou égale à la
                    // déduction dans tous les cas
                    //Pas besoin de véririfier pour le signe de la différence.
                    mpDiff = CreditsList.GetCreditsAmount().subtract(mpSum);

                    //Ajouter cette différence au dernier provision.
                    CreditsList.AddAmountToLastMp(mpDiff);

                }

                //Enregistrer la repartition de la déduction courante
                CreditsList.SaveProvisionProfits( getTrxName());
            }

            //Réinitialiser le pointeur des déductions
//            CreditsList.ResetIteratorPosition();
        }
        else
        {

        }
    }

    //Calcule le pourcentage des distributions par rapport a un gain admissible
    public void ComputeDistributionPct(GainDistributionList gdl, BigDecimal eligibleAmnt)
    {

//        BigDecimal pct = null;
//        BigDecimal distAmnt = null;
        //Réinitialiser la position de la liste de distribution
        gdl.ResetIteratorPosition();

        if (eligibleAmnt == null ||  eligibleAmnt.signum() == 0)  // 
        {
            //Le montant elligible est zéro, il faut donc attribuer 100% à la
            // première imputation et le reste des imputations sera zéro
            gdl.Next();
            gdl.SetDistributionPct(new BigDecimal(1.0));
            while (gdl.Next())
            {
                gdl.SetDistributionPct(new BigDecimal(0));
            }
        }
        else
        {
            //Pour chaque distribution le pourcentage est la division du
            // montant de la distribution
            // par le gain total Eligible, on prend pour acqquis que la somme
            // des distributions est égale au gain total
            gdl.ResetIteratorPosition();

            while (gdl.Next())
            {
                //Log.trace( 5,"Distribution : " + distAmnt.toString() +
                // "--- Pct: " + gdl.GetDistributionPct().toString() );
//                distAmnt = gdl.GetDistributionAmount();
                gdl.SetDistributionPct((gdl.GetDistributionAmount()).divide(eligibleAmnt, BigDecimal.ROUND_HALF_UP));
            }
        }

        //Reinitialiser la position de la liste de distribution
        gdl.ResetIteratorPosition();
    }

    //Charge la liste des paiements en fonction d'une période de paie donnée
    public void LoadPaymentIdList( )
    {
        if (paymentIdList == null)
            paymentIdList = new Vector<BigDecimal>();

        String sql = "select p_payment_id from p_payment where p_period_id = ? ";
        //String payPeriodIdClause = BuildPayPeriodClause() ;
        //sql = sql + " and p_payment_id = 1018329"; //paiement pour des fins
        // des tests

        try
        {
            //			Pour des fins de tests
            //org.compiere.Compiere.startupClient();

            PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
            pstmt.setInt(1, Payment.getP_Period_ID());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                paymentIdList.add(rs.getBigDecimal(1));
            }

            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " + e.toString(), getTrxName());
            log.log (Level.SEVERE,"CreditsEntryWriter.LoadPaymentIdList", e);
        }
    }

    //Fournit les distributions salariales pour un paiement donné qui sont
    // admissibles au bénéfices marginaux
/*    
    public GainDistributionList GetPaymentGainDistribution(int creditId)
    {
        GainDistributionList distList = new GainDistributionList();
        String sql = "select " + "ppgd.P_Payment_Gain_Distribution_360_id DistributionId, " + "ppgd.amount Amount,"
                   + " ppg.P_Payment_Gain_ID, ppg.P_Payment_ID, ppgd.C_Activity_Id, ppg.P_Job_Title_ID, ppgd.Line, ppgd.C_SalesRegion_ID, ppgd.Org_ID  " 
		           + "from p_payment_gain ppg " 
				   + " inner join P_Payment_Gain_Distribution_360 ppgd on ppg.p_payment_gain_id = ppgd.p_payment_gain_id " 
				   + " inner join p_gain pg on ppg.p_gain_id = pg.p_gain_id  " 
				   + " inner join p_gain_credits pmg on ppg.p_gain_parameter_id = pmg.p_gain_parameter_id" 
				   + "where ppg.p_payment_id =? and " + "pmg.p_credits_id =? and " 
				   + "ppgd.distribution_type = 'S' " ;
//				   + "order by ppgd.P_Payment_Gain_Distribution_360_id  ";

//        Log.trace( 5,"sql :" + sql);
        try
        {

            PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
            pstmt.setInt(1, Payment.getP_Payment_ID());
            pstmt.setInt(2, creditId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                distList.AddDistribution(rs.getBigDecimal("P_Payment_ID"), rs.getBigDecimal("P_Payment_Gain_ID"), rs.getBigDecimal("C_Activity_Id"), new BigDecimal( Payment.getP_Occupation_Group_ID() ), rs.getBigDecimal("P_Job_Title_ID"), rs.getBigDecimal("Line"), rs.getBigDecimal("C_SalesRegion_ID"), rs.getBigDecimal("Org_ID"), rs.getBigDecimal("DistributionId"), rs.getBigDecimal("Amount"));
            }

            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " + e.toString(), getTrxName());
            log.log (Level.SEVERE,"CreditsEntryWriter.LoadPaymentIdList", e);
        }

        return distList;
    }
*/
    
    public BigDecimal GetPaymentEligibleGain(int creditsId )
    {
        BigDecimal amount = null;

        String sql = "select " + " sum(ppg.amountcalc) ElligibleGain " 
		           + "from  p_payment_gain ppg , p_gain_credits pcg " 
		           + "where ppg.p_payment_id = ? and " 
				   + "ppg.p_gain_parameter_id = pcg.p_gain_parameter_id " 
				   + "and pcg.p_credits_id = ? " 
				   + "group by " + " ppg.p_payment_id ";

//        Log.trace( 5,"sql :" + sql);
        try
        {

            PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
            pstmt.setInt(1, Payment.getP_Payment_ID());
            pstmt.setInt(2, creditsId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next())
            {
                amount = rs.getBigDecimal(1);
            }

            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " + e.toString(), getTrxName());
            log.log (Level.SEVERE,"CreditsEntryWriter.GetPaymentEligibleGain", e);
        }

        return amount;
    }

  /*  
    //Construit la liste des periodes de paies pour la clause In du sql
    private String BuildPayPeriodClause()
    {
        String clause = "( ";

        for (int i = 0; i < payPeriodIdList.size(); i++)
        {
            if (i == payPeriodIdList.size() - 1)
                clause += " " + ((BigDecimal) payPeriodIdList.get(i)).toString() + " )";
            else
                clause += " " + ((BigDecimal) payPeriodIdList.get(i)).toString() + ",";
        }

        return clause;
    }
*/
    
    //Fonctions qui servent au test
    public Vector getPayPeriodList()
    {
        return payPeriodIdList;
    }

    public Vector getPaymentList()
    {
        return paymentIdList;
    }

    //
    //  Delete the Ledger entry for a payment
    // 
    public static void Undo(int paymentID, String trxName)
    {

        String sql = " delete P_Payment_Gain_Distribution_360 where " +
        		"P_Payment_Gain_Distribution_360.p_payment_gain_id in (select p_payment_gain.p_payment_gain_id from p_payment_gain " +
        		"where p_payment_gain.p_payment_id = " + paymentID + ")" + 
				" and P_Payment_Gain_Distribution_360.distribution_type in ( 'A', 'B' )";

        try
        {
             DB.executeUpdate(sql, trxName);
        }
        catch (Exception ex)
        {
            log.log (Level.SEVERE,"Ledger_Entry_360.DeleteCurrentMarginalBenefit", ex);
        }

        sql = " delete p_payment_entryline_360 where p_payment_id = " + paymentID;

        try
        {
            DB.executeUpdate(sql, trxName);
        }
        catch (Exception ex)
        {
            log.log (Level.SEVERE,"Ledger_Entry_360.DeleteCurrentAccountingEntry", ex);
        }
    }

    /*
     * public void CreateAccoutingEntryHeader() { // Pour des fins de tests //
     * org.compiere.Compiere.startupClient();
     * 
     * //On crée l'entetê de l'écriture qu'une seule fois. X_P_Accounting_Entry
     * accountingEntry = new X_P_Accounting_Entry(Env.getCtx(), -1);
     * 
     * //La date du document accountingEntry.setDateDoc(Period.getPayDate());
     * 
     * //La période accountingEntry.setP_Period_ID(Period.getP_Period_ID());
     * 
     * //Enregistrer l'entête de l'écriture accountingEntry.save();
     * 
     * //Pour le moment le numéro du document correspond à l'ID de // l'écriture
     * Integer temp = new Integer(accountingEntry.getP_Accounting_Entry_ID());
     * accountingEntry.setDocumentNo(temp.toString()); }
     */

	private void setTrxName( String trxName)
	{
		m_trxName = trxName;
	}

	private String getTrxName()
	{
		return m_trxName;
	}

/*
	public static void main (String[] args)
	{
		Properties ctx = Env.getCtx();

		System.out.println("Ecriture comptable $Revision: 1.12 $");
		System.out.println("----------------------------------");
		//
		Compiere.startup(true);
		
		Env.setContext( ctx, "#AD_Client_ID", 11);
		Env.setContext( ctx, "#AD_Org_ID", 11);
		
		Ledger_Entry_360 t = new Ledger_Entry_360( 1295771, null);
		t.WriteEntry();

		t = new Ledger_Entry_360( 1295968, null);
		t.WriteEntry();

		t = new Ledger_Entry_360( 1296058, null);
		t.WriteEntry();

		t = new Ledger_Entry_360( 1296133, null);
		t.WriteEntry();

		t = new Ledger_Entry_360( 1296139, null);
		t.WriteEntry();


	}	//	main
*/
	
	private void RemakeDistribution ( int P_Payment_ID, String trxName ) 
	{
		int count = 0;

		String sql = "Select P_Payment_ID from P_Payment where P_Payment_ID = " + P_Payment_ID;
		
		PreparedStatement pstmt = null;

		try 
		{

			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			P_Payment payment;
			P_Time_Sheet TimeSheet;
			TimeValidation timeValidation;
			
			while (rs.next())
			{
	            payment = new P_Payment( Env.getCtx(), rs.getInt("P_Payment_ID"), trxName);
	            TimeSheet = P_Time_Sheet.GetWithPaymentID(Env.getCtx(), payment.getP_Payment_ID(), trxName);
	            if ( TimeSheet != null)
	            {
	                timeValidation = new TimeValidation( TimeSheet, trxName);
	                DB.executeUpdate("Delete from P_Payment_Gain_Distribution_360 where P_Payment_Gain_ID in ( select P_Payment_Gain_ID From P_Payment_Gain where P_Payment_ID = " + payment.getP_Payment_ID() + ")", trxName);
					timeValidation.Create_PaymentGainDistribution_360(payment, 0);
//	        		payment.save( trxName );
	
					count++;
	            }
			}
			rs.close();
			pstmt.close();
			pstmt = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    //Calculer les bénéfices marginaux concernant un seul paiement.
    public void WriteCreditsEntryByPaymentNew( )
    {
    	
        P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), getTrxName());
		P_Assignment Assignment = P_Assignment.get( Env.getCtx(), Employee.getLastEmployeeAssignment( Payment.getP_Payment_ID() ), null);
		P_Post Post = P_Post.get( Env.getCtx(), Assignment.getP_Post_ID(), null);
		P_Workplace Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);

 		if ( Workplace.getC_SalesRegion_ID() == 0 )
 		{
 	 		Assignment = P_Assignment.get( Env.getCtx(), Employee.GetAssignmentPrincipal(), null);
 	 		Post = P_Post.get( Env.getCtx(), Assignment.getP_Post_ID(), null);
 	 		Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);
 			
 		}

        String sql = "SELECT ppd.P_Credits_ID,  "
 				   + " sum(isnull(ppd.Amt_Reserve,0)) Amt_Reserve, "
				   + " pe.C_Activity_ID , "
				   + " pe.AD_Org_ID , "
                   + " PMovementType , "
				   + " max( ppd.P_Credits_Movement_ID ) P_Credits_Movement_ID "
                   + " FROM P_Credits_Movement ppd " 
                   + "  INNER JOIN p_employee pe ON  ppd.P_employee_id = pe.p_employee_id " 
                   + "  INNER JOIN p_credits pc ON pc.p_credits_id = ppd.p_credits_id "
                   + "  LEFT OUTER JOIN P_CREDITS_CTB ON P_CREDITS_CTB.P_Credits_ID = pc.p_credits_id  "
                   + "         AND P_CREDITS_CTB.P_COLLECTIVE_LABOUR_AGR_ID = PE.P_COLLECTIVE_LABOUR_AGR_ID "
                   + "         AND P_CREDITS_CTB.P_PAYMENT_GROUP_ID = PE.P_PAYMENT_GROUP_ID "
                   + "         AND P_CREDITS_CTB.P_JOB_TYPE_ID = PE.P_JOB_TYPE_ID "
                   + " WHERE ppd.p_payment_id = ? " 
                   + " AND isReporting = 'Y'  " 
                   + " AND ISCOUNT = 'Y' "
                   + " AND PMovementType = 'PRO' "
                   + " GROUP BY ppd.P_Credits_ID, pe.C_Activity_ID, pe.AD_Org_ID, PMovementType ";


        try
        {

            PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, getTrxName());
            pstmt.setInt(1, Payment.getP_Payment_ID());
            ResultSet rs = pstmt.executeQuery();
            P_Credits_Account_360 CreditsAccount;
            while (rs.next())
            {

                int creditID = rs.getInt(1);
                int activityID = Post.getC_Activity_ID(); //rs.getInt("C_Activity_ID");
                int orgID = rs.getInt("AD_Org_ID");
                CreditsAccount = P_Credits_Account_360.getWithSalesRegion(Env.getCtx(), Payment.getAD_Client_ID(), Payment.getAD_Org_ID(), Workplace.getC_SalesRegion_ID() , creditID, null);
                P_Credits Credits = P_Credits.get(Env.getCtx(), creditID, getTrxName());

                if (CreditsAccount == null)
                {
                	
                    MActivity Act = new MActivity( Env.getCtx(), activityID, null );
                    P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé de compte comptable pour la banque : " + Credits.getValue() + " - " + Credits.getName() + " - " + Act.getName(), getTrxName());
                }

                if (CreditsAccount != null)
                {
                	BigDecimal amt = rs.getBigDecimal("Amt_Reserve");

                	P_Payment_Entryline_360 aEntryLine;
                    aEntryLine = new P_Payment_Entryline_360(Env.getCtx(), -1, getTrxName());

                    aEntryLine.setP_Payment_ID(Payment.getP_Payment_ID() );
                    aEntryLine.setC_ElementValue_ID(CreditsAccount.getP_Credits_Acct() );

                    aEntryLine.setC_SalesRegion_ID( Workplace.getC_SalesRegion_ID() );
                    aEntryLine.setAD_Org_ID( Payment.getAD_Org_ID());

                    
//                    aEntryLine.setC_Activity_ID( Payment.getC_Activity_ID());
                    aEntryLine.setC_Activity_ID( Post.getC_Activity_ID());
                    MActivity Activity = new MActivity( Env.getCtx(), aEntryLine.getC_Activity_ID(), null);
        		    if ( Activity.getValue360().startsWith("PROD"))
        		    {
        		    	aEntryLine.setOrg_ID( 1100013 );
        		    }
                    
                    if (rs.getBigDecimal("Amt_Reserve").signum() == -1)
                         aEntryLine.setAmtAcctCr(rs.getBigDecimal("Amt_Reserve").abs().setScale(2, BigDecimal.ROUND_HALF_UP));
                    else
                         aEntryLine.setAmtAcctDr(rs.getBigDecimal("Amt_Reserve").setScale(2, BigDecimal.ROUND_HALF_UP));
                    aEntryLine.setOrigine( "FTB" );
                    aEntryLine.setLine(2);
                    aEntryLine.save();
                    
                    //---
                    // CAP
                    //--
                    P_Payment_Group PaymentGroup = P_Payment_Group.get(Env.getCtx(), Payment.getP_Payment_Group_ID(), getTrxName());

                    aEntryLine = new P_Payment_Entryline_360(Env.getCtx(), -1, getTrxName());

                    aEntryLine.setP_Payment_ID( Payment.getP_Payment_ID() );
                    aEntryLine.setC_ElementValue_ID(Credits.getCredits_Acct_360());

                    aEntryLine.setAD_Org_ID( Payment.getAD_Org_ID());
//                    aEntryLine.setOrg_ID( 1000001);
                    if ( Payment.getAD_Org_ID() == 1000001 )
                		aEntryLine.setOrg_ID( Payment.getAD_Org_ID() );
                    else  {
                        MElementValue Cpt2 = new MElementValue( Env.getCtx(),  Credits.getCredits_Acct_360(), getTrxName());
                        aEntryLine.setOrg_ID( Cpt2.getAD_Org_ID());
                    }

                    aEntryLine.setC_Activity_ID(Post.getC_Activity_ID());
                    Activity = new MActivity( Env.getCtx(), aEntryLine.getC_Activity_ID(), null);
                    
        		    if ( Activity.getValue360().startsWith("PROD"))
        		    {
        		    	aEntryLine.setOrg_ID( 1100013 );
        		    }

                    aEntryLine.setC_SalesRegion_ID( Workplace.getC_SalesRegion_ID() );

                    if (rs.getBigDecimal("Amt_Reserve").signum() == -1)
                         aEntryLine.setAmtAcctDr(rs.getBigDecimal("Amt_Reserve").abs().setScale(2, BigDecimal.ROUND_HALF_UP));
                    else
                         aEntryLine.setAmtAcctCr(rs.getBigDecimal("Amt_Reserve").setScale(2, BigDecimal.ROUND_HALF_UP));
                    aEntryLine.setOrigine( "FTB" );
                    aEntryLine.setLine(2);
                    aEntryLine.save();
                    
                }
            }

            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " + e.toString(), getTrxName());
            log.log (Level.SEVERE,"CreditsEntryWriter.LoadPaymentIdList", e);
        }
    }

    //Calculer les bénéfices marginaux concernant un seul paiement.
    public void WriteMargnalProfitByPaymentNew( )
    {
        GainDistributionList distributionList = null;

        P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), getTrxName());
 		P_Assignment Assignment = P_Assignment.get( Env.getCtx(), Employee.getLastEmployeeAssignment( Payment.getP_Payment_ID() ), null);
 		P_Post Post = P_Post.get( Env.getCtx(), Assignment.getP_Post_ID(), null);
 		P_Workplace Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);
        
 		if ( Workplace.getC_SalesRegion_ID() == 0 )
 		{
 	 		Assignment = P_Assignment.get( Env.getCtx(), Employee.GetAssignmentPrincipal(), null);
 	 		Post = P_Post.get( Env.getCtx(), Assignment.getP_Post_ID(), null);
 	 		Workplace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);
 			
 		}
        
        //On commence par aller chercher toutes les déduction sur le paiement
        // TODO ajouté un paramètre pour les assurances.
        String sqlMaster = "SELECT ppd.p_payment_deduction_id , "
        				+" ppd.employer_part , "
						+" ppd.P_DEDUCTION_ID , "
						+" ppd.P_Employee_ID ,  ppd.p_deduction_id"
                   + " FROM p_payment_deduction ppd, P_Deduction " 
                   + " WHERE ppd.p_payment_id = ? AND employer_part <> 0"//1-Payment.Payment_ID
                   + "  AND P_Deduction.P_Deduction_ID = ppd.P_Deduction_ID "
  //                 + "  AND P_Deduction.P_Deduction_Family_ID != 1000001" // Pas les assurances.
				   ;
        try
        {

            PreparedStatement pstmt = DB.prepareStatement(sqlMaster, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, getTrxName());
        	//PreparedStatement pstmt = DB.prepareStatement(sqlMaster);
            pstmt.setInt(1, Payment.getP_Payment_ID());

            ResultSet rs = pstmt.executeQuery();

            GainDeductionList360 deductionList;
            P_Payment_Entryline_360 aEntryLine;
            
            MElementValue ElementValue;
            P_Payment_Group PaymentGroup = P_Payment_Group.get(Env.getCtx(), Payment.getP_Payment_Group_ID(), getTrxName());

            while (rs.next())
            {
            	
                deductionList = new GainDeductionList360();
            	// une deduction peut etre negative mais ne doit etre egal a 0
//                if (rs.getBigDecimal("employer_part").compareTo( Env.ZERO ) != 0)
//                {
        		deductionList.AddDeduction(rs.getBigDecimal("p_payment_deduction_id"), rs.getBigDecimal("employer_part"), rs.getBigDecimal( "P_Deduction_ID"));

//        	    P_Deduction Deduction = P_Deduction.get( Env.getCtx(), rs.getInt( "P_Deduction_ID"), m_trxName);
        	    P_Deduction_Param DeductionParam = P_Deduction_Param.get( Env.getCtx(), rs.getInt( "P_Deduction_ID"), Period.getEndDate(), m_trxName);

                //Calcul du gain total admissible
                totalAmount = null; 

                distributionList = GetPaymentGainDistribution( DeductionParam );

                if ( distributionList == null)
                {
                    //part employeur

                    aEntryLine = new P_Payment_Entryline_360(Env.getCtx(), -1, getTrxName());
                    
    				Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), getTrxName());
//    				deductionAccount = P_Deduction_Account.get(Env.getCtx(), Payment.getAD_Client_ID(), Payment.getAD_Org_ID(), Payment.getC_Activity_ID(), rs.getInt( "P_Deduction_ID"),  getTrxName());

    				P_Deduction_Account_360 deductionAccount = P_Deduction_Account_360.getWithSaleRegion(Env.getCtx(), Payment.getAD_Client_ID(), Payment.getAD_Org_ID(), Workplace.getC_SalesRegion_ID(), rs.getInt( "P_Deduction_ID"),  getTrxName());
                    
    				if ( deductionAccount == null)
    				{
    					P_Deduction Deduction = P_Deduction.get( Env.getCtx(), rs.getInt( "P_Deduction_ID"), getTrxName() );
                        P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, "Le système n'a pas trouvé de compte comptable pour la part employeur : " + Deduction.getValue() + " " + Deduction.getName() , getTrxName());
                        return;
    				}

                    aEntryLine.setP_Payment_ID(Payment.getP_Payment_ID());
                    aEntryLine.setAD_Org_ID( Payment.getAD_Org_ID());

                    if ( deductionAccount.getC_Activity_ID() != 0)
                    {
                    	aEntryLine.setC_Activity_ID(deductionAccount.getC_Activity_ID());
                    }
                    else
                    {
                    	aEntryLine.setC_Activity_ID(Post.getC_Activity_ID());
                    }
                    
                    MActivity Activity = new MActivity( Env.getCtx(), aEntryLine.getC_Activity_ID(), null);
        		    if ( Activity.getValue360().startsWith("PROD"))
        		    {
        		    	aEntryLine.setOrg_ID( 1100013 );
        		    }

                    
                    if ( deductionAccount.getC_SalesRegion_ID() != 0)
                    	aEntryLine.setC_SalesRegion_ID( deductionAccount.getC_SalesRegion_ID() );
                    else
                    	aEntryLine.setC_SalesRegion_ID( Workplace.getC_SalesRegion_ID() );

					ElementValue = new MElementValue( Env.getCtx(), deductionAccount.getP_Deduction_Acct(), getTrxName());
					
					//2014.04.28 compte de bilan la base et 000
//					if (  ElementValue.getAccountType().equals( MElementValue.ACCOUNTTYPE_Asset)  )
//					aEntryLine.setC_SalesRegion_ID( PaymentGroup.getC_SalesRegion_ID() );

                    aEntryLine.setC_ElementValue_ID( deductionAccount.getP_Deduction_Acct() );
                    if (rs.getBigDecimal("employer_part").signum() == -1)
                        aEntryLine.setAmtAcctCr(rs.getBigDecimal("employer_part").abs().setScale(2, BigDecimal.ROUND_HALF_UP));
                    else
                        aEntryLine.setAmtAcctDr(rs.getBigDecimal("employer_part").setScale(2, BigDecimal.ROUND_HALF_UP));
                    
                    aEntryLine.setOrigine( "FTR" );
                    aEntryLine.setLine(1);
                    aEntryLine.save();
                	
                }
                else
                {
                    if ( totalAmount != null)
                    {
                        //Log.trace( 5,"Montant Elligible : " + totalAmount.toString() +
                        // " --- PaimentID : " + ((BigDecimal)paymentIdList.get(i)).toString());
                        //Calcul du prorata de chaque distribution
                        ComputeDistributionPct(distributionList, totalAmount);

                        //Calcul des bénéfices marginaux et enregistrement
                        ComputeMarginalProfit(distributionList, deductionList);
                    }
                	
                }
        }
            rs.close();
            pstmt.close();

        }
        catch (Exception e)
        {
            P_Time_Sheet_Error.NewMessage(TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, " Internal error - Exception  " + e.toString(), getTrxName());
            log.log (Level.SEVERE,"MarginalProfitWriter.LoadPaymentIdList", e);
        }
        
        
    }

 /*   
	public int GetAssignment( int P_Payment_ID  )
	{
		String sql = null;
		sql = "Select TOP 1 P_Payment_Gain.P_Assignment_ID, sum( quantitycalc) quantitycalc "; 
		sql += " From P_Payment_Gain";
		sql += " WHERE P_Payment_ID = " + P_Payment_ID ;
		sql += " GROUP BY P_Payment_Gain.P_Assignment_ID  ";
		sql += " ORDER BY sum( quantitycalc) desc ";

		int iAssignment_ID = 0;

		PreparedStatement pstmt = null;

		try
		{
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if ( rs.next () )
				iAssignment_ID = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE,"Ledger_Entry - GetAssignment - " + sql, e);
		}
			

		return iAssignment_ID;
		
	}
*/

    private boolean ValidTotal(int P_Payment_ID,  String trxName)
    {
        String SqlEntry = "Select Sum(isnull(P_Payment_Entryline_360.Amtacctdr, 0)) as AmountDr, Sum(isnull(P_Payment_Entryline_360.Amtacctcr, 0)) as AmountCr " + 
                          "  From P_Payment" +
                          "  Inner Join P_Payment_Entryline_360 On P_Payment.P_Payment_Id = P_Payment_Entryline_360.P_Payment_Id" +
                          "  Where P_Payment.P_Payment_Id = " + P_Payment_ID ;
        
        
        PreparedStatement pstmp = null;
        boolean Valid = true;
        try
        {
            pstmp = DB.prepareStatement(SqlEntry, null);
            ResultSet rs = pstmp.executeQuery();
            while ( rs.next() )
            {
                BigDecimal MntDebit = new BigDecimal(0);
                BigDecimal MntCredit = new BigDecimal(0);
                MntDebit = rs.getBigDecimal("AmountDr");
                MntCredit = rs.getBigDecimal("AmountCr");
                if ( MntDebit.compareTo(MntCredit) == 0 )
                    Valid = true;
                else
                    Valid = false;
            }
            rs.close();
        }
        catch (Exception e)
        {
            log.log (Level.SEVERE,"P_InOutTransfertGeneralLedger.ValidTotal", e);
        }
        return Valid;
    }

	
}
