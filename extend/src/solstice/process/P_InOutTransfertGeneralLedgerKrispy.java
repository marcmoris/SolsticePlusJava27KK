/*
 * Created on Sep 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MActivity;
import org.compiere.model.MElementValue;
//import org.compiere.model.MPeriod;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Accounting_Entry;
import solstice.model.P_Accounting_EntryLine;
import solstice.model.P_Period;
import solstice.model.P_Payment_Entryline;
import solstice.model.P_Payment_Group;
import solstice.model.P_ExpenseCorrection;

/**
 * @author rejgar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutTransfertGeneralLedgerKrispy 
{
	private String m_trxName;
    private Properties m_ctx;
    private int Client_ID = 0;
    private int Org_ID = 0;
    private int Frequency_ID = 0;
    private int Period_ID = 0;
    private int User_ID = 0;
    private int Instance_ID = 0;
    private String Value = "";
    private CLogger			log = CLogger.getCLogger (getClass());
    private boolean IsRework = false;

    
    public P_InOutTransfertGeneralLedgerKrispy( Properties ctx)
    {
        m_ctx = ctx;
    }

    protected String ExecuteProcess(int _Client_ID, int _Org_ID, int _Frequency_ID, int _Period_ID, int _Instance_ID, boolean _IsRework)
    {
        String trxName = null;
        Client_ID = _Client_ID;
        Org_ID = _Org_ID;
        Frequency_ID = _Frequency_ID;
        Period_ID = _Period_ID;
        Instance_ID = _Instance_ID;
        IsRework = _IsRework;
        
        String sql = "SELECT AD_Org_ID, P_PAYMENT_GROUP_ID FROM P_PAYMENT_GROUP WHERE ISACTIVE = 'Y'";
        
        boolean Validation =  ValidTotal(trxName);
        if ( Validation )
        {
            PreparedStatement pstmp = null;
            boolean Valid = true;
            try
            {
                pstmp = DB.prepareStatement(sql, null);
                ResultSet rs = pstmp.executeQuery();
                while ( rs.next() )
                {
                	GetSequenceNumber();
                    UpdateAndCreateFile( rs.getInt("AD_Org_ID"), rs.getInt("P_Payment_Group_ID"), trxName);
                	
                }
            }
            catch (Exception e)
            {
                log.log (Level.SEVERE,"P_InOutTransfertGeneralLedger.ValidTotal", e);
            }
             

//            ExpenseCorrection(trxName);
            UpdateFeuille(trxName);
            TransfertGsl(trxName);
            UpdatePayment( trxName);
            return "Terminé avec succès";
        }
        else
            return "Les montants débits et crédits ne balance pas.";
    }
    
    private boolean ValidTotal(String trxName)
    {
        String SqlEntry = "Select Sum(isnull(P_Payment_Entryline.Amtacctdr, 0)) as AmountDr, Sum(isnull(P_Payment_Entryline.Amtacctcr, 0)) as AmountCr " + 
                          "  From P_Payment" +
                          "  Inner Join P_Payment_Entryline On P_Payment.P_Payment_Id = P_Payment_Entryline.P_Payment_Id" +
                          "  Where P_Payment.P_Period_Id = " + Period_ID ;
        
        if ( IsRework )
            SqlEntry = SqlEntry + "    And ( P_Payment.Timesheetstatus IN ('E', 'T') OR PaymentTypeDoc = 'Annulation-' )" ;
        else
            SqlEntry = SqlEntry + "    And ( P_Payment.Timesheetstatus IN ( 'E' ) OR PaymentTypeDoc = 'Annulation-' )" ;
                          
        SqlEntry = SqlEntry + " Group By P_Payment.P_Period_id";
        
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
    
    
    private void UpdateAndCreateFile(int AD_Org_ID, int P_Payment_Group_ID, String trxName) 
    {
  /*      
        String SqlEntry = "Select Sum(isnull(P_Payment_Entryline.Amtacctdr,0)) as AmountDr, Sum(isnull(P_Payment_Entryline.Amtacctcr,0)) as AmountCr, " + 
                          "P_Payment_Entryline.C_Activity_Id, P_Payment_Entryline.C_Elementvalue_Id" +
                          "  From P_Payment" +
                          "  Inner Join P_Payment_Entryline On P_Payment.P_Payment_Id = P_Payment_Entryline.P_Payment_Id" +
                          "  Where P_Period_Id = " + Period_ID +
                          "    AND P_Payment_Group_ID = " +  P_Payment_Group_ID;

        if ( IsRework )
          SqlEntry = SqlEntry + "    And P_Payment.Timesheetstatus IN ('E', 'Z', 'T')" ;
        else
          SqlEntry = SqlEntry + "    And P_Payment.Timesheetstatus IN ( 'E', 'Z' )" ;

        SqlEntry = SqlEntry + " Group By P_Payment_Entryline.C_Activity_Id, P_Payment_Entryline.C_Elementvalue_Id";
        SqlEntry = SqlEntry + " Having ( Sum(isnull(P_Payment_Entryline.Amtacctdr,0)) + Sum(isnull(P_Payment_Entryline.Amtacctcr,0))) <> 0 " ;
        SqlEntry = SqlEntry + " ORDER BY P_Payment_Entryline.C_Elementvalue_Id, P_Payment_Entryline.C_Activity_Id " ;
*/        
        String SqlEntry = "Select Sum(isnull(P_Payment_Entryline.Amtacctdr,0)) as AmountDr, Sum(isnull(P_Payment_Entryline.Amtacctcr,0)) as AmountCr, " + 
                " P_Payment_Entryline.C_Elementvalue_Id, Org_ID" +
                "  From P_Payment" +
                "  Inner Join P_Payment_Entryline On P_Payment.P_Payment_Id = P_Payment_Entryline.P_Payment_Id" +
                "  Where P_Period_Id = " + Period_ID +
                "    AND P_Payment_Group_ID = " +  P_Payment_Group_ID;

		if ( IsRework )
		SqlEntry = SqlEntry + "    And P_Payment.Timesheetstatus IN ('E', 'Z', 'T')" ;
		else
		SqlEntry = SqlEntry + "    And P_Payment.Timesheetstatus IN ( 'E', 'Z' )" ;
		
		SqlEntry = SqlEntry + " Group By P_Payment_Entryline.C_Elementvalue_Id, Org_ID";
		SqlEntry = SqlEntry + " Having ( Sum(isnull(P_Payment_Entryline.Amtacctdr,0)) + Sum(isnull(P_Payment_Entryline.Amtacctcr,0))) <> 0 " ;
		SqlEntry = SqlEntry + " ORDER BY P_Payment_Entryline.C_Elementvalue_Id " ;

		P_Accounting_Entry Entry = null;
        P_Accounting_EntryLine EntryLine = null;
        PreparedStatement pstmp = null;
        try
        {
            pstmp = DB.prepareStatement(SqlEntry, null);
            ResultSet rs = pstmp.executeQuery();
            P_Period Period = null;
        //    MPeriod PeriodCtb = null;
 //           MActivity Activity = null;
            MElementValue ElementValue = null;
            Period = P_Period.get(getCtx(), Period_ID, trxName);
            int NewEntry = 0; 
            int PeriodGl =0;
            BigDecimal MntDebit = new BigDecimal(0);
            BigDecimal MntCredit = new BigDecimal(0);
            BigDecimal MntTotalDebit = new BigDecimal(0);
            BigDecimal MntTotalCredit = new BigDecimal(0);
            Calendar cal = Calendar.getInstance();
            Timestamp XDate = new Timestamp(cal.getTimeInMillis());
            Value = DB.getDocumentNo(Client_ID, "P_Accounting_Entry", trxName);
            int NoLigne = 1;
            Entry = new P_Accounting_Entry(getCtx(), -1, getTrxName());
            Entry.setDocumentNo(Value);
            Entry.setDateDoc(XDate);
            Entry.setP_Period_ID(Period_ID);
            Entry.setP_Payment_Group_ID( P_Payment_Group_ID );

            //2023-08-03
            Entry.setAD_Org_ID(AD_Org_ID );            
            Entry.save();
            NewEntry = Entry.getP_Accounting_Entry_ID();
            while ( rs.next() )
            {
                PeriodGl = Period.getC_Period_ID1();
            //    PeriodCtb = MPeriod.get(getCtx(), PeriodGl, trxName);
                ElementValue = new MElementValue(getCtx(), rs.getInt("C_ElementValue_ID"), trxName);
 //               Activity = new MActivity(getCtx(), rs.getInt("C_Activity_ID"), trxName);
                MntDebit = rs.getBigDecimal("AmountDR");
                MntCredit = rs.getBigDecimal("AmountCR");
/*                
                if ( MntDebit.compareTo(MntCredit) >= 0 )
                {
                    MntDebit = MntDebit.subtract(MntCredit);
                    MntCredit = Env.ZERO;
                }
                else
                {
                    MntCredit = MntCredit.subtract(MntDebit);
                    MntDebit = Env.ZERO;
                }
*/                
                MntTotalDebit = MntTotalDebit.add(MntDebit);
                MntTotalCredit = MntTotalDebit.add(MntCredit);
                if ( ( MntDebit != Env.ZERO ) || ( MntCredit != Env.ZERO ) ) 
                {
                    EntryLine = new P_Accounting_EntryLine(getCtx(), -1, getTrxName());
                    EntryLine.setAD_Org_ID(  AD_Org_ID );
                    EntryLine.setOrg_ID( rs.getInt("Org_ID"));
                    EntryLine.setP_Accounting_Entry_ID(NewEntry);
//                    EntryLine.setC_Activity_ID(rs.getInt("C_Activity_ID"));
                    EntryLine.setC_ElementValue_ID(rs.getInt("C_ElementValue_ID"));
//                    if ( MntDebit.compareTo(Env.ZERO) == 0)
 //                   {
                        EntryLine.setAmtAcctCr(MntCredit);
                        EntryLine.setAmtAcctDr(MntDebit);
                    	
//                    }
//                    else {
//                        EntryLine.setAmtAcctCr(MntCredit);
//                        EntryLine.setAmtAcctDr(MntDebit);
//                    }
                    EntryLine.setLine(NoLigne);
                    EntryLine.save();
                    NoLigne++;
                }
            }
            rs.close();
            User_ID = Env.getAD_User_ID(getCtx());
            
            String sql = "INSERT INTO AD_PInstance_PARA (AD_PInstance_id, seqno, ParameterName, P_String, P_String_To, P_Number, P_Number_To, P_Date, P_Date_To, Info, Info_To, Ad_Client_id, Ad_Org_id, Created, createdby, Updated, UpdatedBy, Isactive) " +
                         "Select " + Instance_ID + ", " +
                         "(Select Max(seqno) + 1 from AD_PInstance_Para where AD_PInstance_id = " + Instance_ID + "), " +
                         "'DocumentNo', " + Value + ", null, null, null, null, null, null, null, " + Client_ID + ", " + Org_ID + ", GetDate(), " + User_ID + ", GetDate(), " + User_ID + ", 'Y'";
            
            PreparedStatement pstmp2 = null;
            try
            {
                pstmp2 = DB.prepareStatement(sql, null);
                pstmp2.execute();
                //pstmp = DB.
            }
      	    catch (Exception e)
    	    {
         		log.log(Level.SEVERE, "LauncherRemunerationState.Insert", e);
    	    }

        }
        catch (Exception e)
        {
            log.log (Level.SEVERE,"P_InOutTransfertGeneralLedger.UpdateDetail", e);   
        }
    }
    
    
    private boolean ExpenseCorrection(String trxName)
    {
        P_Accounting_Entry AccountingEntry = null;
        P_Accounting_EntryLine AccountingEntryLine = null;
        P_Payment_Entryline PaymentLine = null;
        P_ExpenseCorrection Expense = null;
        P_Period Period = null;     
        PreparedStatement pstmp = null;
        
        try
		{           
        	String sql = "Select Count(*) Count from P_ExpenseCorrection where P_Accounting_Entry_ID is null";
	        pstmp = DB.prepareStatement(sql, null);
	        ResultSet rs = pstmp.executeQuery();
        	
	        int Count = 0;
	        
	        while ( rs.next() )
	        {
	        	Count = rs.getInt(1);
	        }
	        rs.close();
	        pstmp.close();
	        pstmp = null;
        	
	        if (Count>0)
	        {
		        AccountingEntry= new P_Accounting_Entry(getCtx(), -1, trxName);
		            	
		        Calendar cal = Calendar.getInstance();
		        Timestamp XDate = new Timestamp(cal.getTimeInMillis());
		        Value = DB.getDocumentNo(Client_ID, "P_Accounting_Entry", trxName);
		            	
		        AccountingEntry.setDocumentNo(Value);
		        AccountingEntry.setDateDoc(XDate);
		        AccountingEntry.setP_Period_ID(Period_ID);
		        AccountingEntry.save();
		            	            
		        int AccountingEntryID=AccountingEntry.getP_Accounting_Entry_ID();
		        BigDecimal MntDebit = new BigDecimal(0);
		        BigDecimal MntCredit = new BigDecimal(0);
		            	
		        sql = "Select C_Activity_Id,C_Elementvalue_Id,Sum(AmtAcctDr) AmtAcctDr,Sum(AmtAcctCr) AmtAcctCr " +
		                	  "From P_Payment_Entryline " +
		    				  "Where P_Expensecorrection_Id In (Select P_ExpenseCorrection_ID from P_ExpenseCorrection where P_Accounting_Entry_ID is null) " +
		    				  "Group By " +
		    				  "C_Activity_Id,C_Elementvalue_Id";
		        pstmp = DB.prepareStatement(sql, null);
		        rs = pstmp.executeQuery();
	                
		        while ( rs.next() )
		        {
		        	AccountingEntryLine = new P_Accounting_EntryLine(getCtx(), -1, trxName);
		                	
		            AccountingEntryLine.setP_Accounting_Entry_ID(AccountingEntryID);
		            AccountingEntryLine.setLine(1);
		            AccountingEntryLine.setDescription("Correction de la dépense");
		                	
		            MntDebit = rs.getBigDecimal("AmtAcctDr");
		            MntCredit = rs.getBigDecimal("AmtAcctCr");
		                    
		            if ( MntDebit.compareTo(MntCredit) >= 0 )
		            {
		            	MntDebit = MntDebit.subtract(MntCredit);
		                MntCredit = Env.ZERO;
		            }
		            else
		            {
		            	MntCredit = MntCredit.subtract(MntDebit);
		                MntDebit = Env.ZERO;
		            }
		                    
		            	AccountingEntryLine.setAmtAcctCr(MntCredit);
		                AccountingEntryLine.setAmtAcctDr(MntDebit);
		                	
		                AccountingEntryLine.setC_Activity_ID(rs.getInt("C_Activity_Id"));
		    			AccountingEntryLine.setC_ElementValue_ID(rs.getInt("C_Elementvalue_Id"));
		    				
		    			AccountingEntryLine.save();  
	        	}
		        rs.close();
		        pstmp.close();
		        pstmp = null;
		        
		        sql = "Select P_ExpenseCorrection_ID from P_ExpenseCorrection where P_Accounting_Entry_ID is null";
		        pstmp = DB.prepareStatement(sql, null);
		        rs= pstmp.executeQuery();
		        
		        while ( rs.next() )
		        {
				    Expense= new P_ExpenseCorrection(getCtx(), rs.getInt("P_ExpenseCorrection_ID"), trxName);
				    Expense.setP_Accounting_Entry_ID(AccountingEntryID);
				    Expense.save();
	        	}
		        rs.close();
		        pstmp.close();
		        pstmp = null;
		        return true;
	        }
		}
        catch (Exception e)
        {
            log.log(Level.SEVERE, "P_InOutTransfertGeneralLedger.ExpenseCorrection", e);
        }
        return false;
    }

    private void TransfertGsl(String trxName)
    {
       	String sql = "EXEC [dbo].[KRISPY_UPDATE_GL] " + Period_ID;
       	DB.executeUpdate(sql, trxName);
    }
    
    private void UpdateFeuille(String trxName)
    {
       	String sql = "Update P_Time_Sheet " +
       			     "  set TimeSheetStatus = 'T' " +
       	             " where P_Period_id = " + Period_ID  +
       	             "   and P_Frequency_ID = " + Frequency_ID +
       	             "   and TimeSheetStatus in ( 'E', 'Z' ) ";
        PreparedStatement psdoc = null;
       try
  	   { 
            psdoc = DB.prepareStatement(sql, null);
            psdoc.executeUpdate();
            //psdoc.executeQuery();
            psdoc.close();
        }
        catch (SQLException e)
        {
            log.log(Level.WARNING, "UpdateFeuille.WriteToFile", e);
        }
			
    }

    private void UpdatePayment(String trxName)
    {
       	String sql = "Update P_Payment " +
       			     "  set IsTransferred = 'Y' " +
       	             " where P_Period_id = " + Period_ID ;
        if ( IsRework )
        	sql = sql + "    And P_Payment.Timesheetstatus IN ('E', 'Z', 'T')" ;
        else
            sql = sql + "    And P_Payment.Timesheetstatus IN ( 'E', 'Z' )" ;

       	
        PreparedStatement psdoc = null;
       try
  	   { 
            psdoc = DB.prepareStatement(sql, null);
            psdoc.executeUpdate();
            //psdoc.executeQuery();
            psdoc.close();
        }
        catch (SQLException e)
        {
            log.log(Level.WARNING, "UpdateFeuille.WriteToFile", e);
        }
    }

    
    private void GetSequenceNumber()
    {
    	String StringSql = "SELECT COUNT(*) SEQUENCE FROM P_ACCOUNTING_ENTRY WHERE P_PERIOD_ID = "  + Period_ID;
    	int SequenceNumber = 0;
    	PreparedStatement pstmp = null;
        try
        {
            pstmp = DB.prepareStatement(StringSql, null);
            ResultSet rs = pstmp.executeQuery();
    	       
            while ( rs.next() )
            {
            	SequenceNumber = rs.getInt("SEQUENCE");
            }
            rs.close();
            
        }
        catch (Exception e)
		{
        	log.log(Level.SEVERE, "P_InOutTransfertGeneralLedger.GetSequenceNumber", e);
		}
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

