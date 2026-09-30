/*
 * Created on Sep 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MOrg;
import org.compiere.model.MSalesRegion;
import org.compiere.model.MActivity;
import org.compiere.model.MElementValue;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Accounting_Entry;
import solstice.model.P_Accounting_EntryLine;
import solstice.model.P_Period;
import solstice.model.P_Payment_Entryline;
import solstice.model.P_ExpenseCorrection;
import solstice.model.P_Payment_Group;
import solstice.model.P_Workplace;

/**
 * @author rejgar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutTransfertGeneralLedgerHelico 
{
//	private int GlobalLineNumber=1;
    private Properties m_ctx;
    private int Client_ID = 0;
    private int AD_Org_ID = 0;
    private int Frequency_ID = 0;
    private int Period_ID = 0;
    private int User_ID = 0;
    private int Instance_ID = 0;
    private String Value = "";
    private String FileValue = "";
    private CLogger			log = CLogger.getCLogger (getClass());
    private boolean IsRework = false;
    private boolean IsOnlyPaymentCanceled = false;

    private String FileName = "";

    public P_InOutTransfertGeneralLedgerHelico( Properties ctx)
    {
        m_ctx = ctx;
    }

    private StringBuffer NewFile;
    private P_Period Period;
    
    protected String ExecuteProcess(int _Client_ID, int _Org_ID, int _Frequency_ID, int _Period_ID, int _Instance_ID, boolean _IsRework, Boolean _IsOnlyPaymentCanceled)
    {
        String trxName = null;
        Client_ID = _Client_ID;
        AD_Org_ID = _Org_ID;
        Frequency_ID = _Frequency_ID;
        Period_ID = _Period_ID;
        Instance_ID = _Instance_ID;
        IsRework = _IsRework;
        IsOnlyPaymentCanceled = _IsOnlyPaymentCanceled;
        
        Period = P_Period.get(getCtx(), Period_ID, trxName);

        boolean Validation =  ValidTotal(trxName);
        if ( Validation )
        {
            UpdateAndCreateFile(trxName);
            ExpenseCorrection( trxName );
            CreateFileBen(trxName);
            UpdateFeuille(trxName);
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
                          "  Where P_Payment.P_Period_Id = " + Period_ID +
                          "    and P_Frequency_ID = " + Frequency_ID ;
        if ( AD_Org_ID != 0 )
            SqlEntry = SqlEntry + "    And P_Payment.AD_Org_ID = " + AD_Org_ID;
        	
        if ( IsRework )
            SqlEntry = SqlEntry + "    And ( P_Payment.Timesheetstatus IN ('E', 'T') OR PaymentTypeDoc = 'Annulation-' )" ;
        else
            SqlEntry = SqlEntry + "    And ( P_Payment.Timesheetstatus IN ( 'E' ) OR ( PaymentTypeDoc = 'Annulation-' AND isTransfer = 'N' )  )" ;

        if ( IsOnlyPaymentCanceled  )
            SqlEntry = SqlEntry + "    And PaymentTypeDoc Like 'Annulation%' " ;
        else
        	SqlEntry = SqlEntry + "    And PaymentTypeDoc not Like 'Annulation%' " ;
        
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

    private void UpdateAndCreateFile(String trxName) 
    {
        if ( IsRework )
        {
        	
        	String Sql = "Update P_ExpenseCorrection set P_Accounting_Entry_ID = null Where P_Accounting_Entry_ID in ( "
        		       + " Select P_Accounting_Entry_ID From P_Accounting_Entry  WHERE P_Period_Id = " + Period_ID 
        		       ;
            if ( AD_Org_ID != 0 )
                Sql = Sql + "    And P_Accounting_Entry.AD_Org_ID = " + AD_Org_ID;
        	
        	Sql = Sql + " ) ";
        	DB.executeUpdate(Sql, trxName);
        	
        	Sql = "Delete From P_Accounting_Entry WHERE P_Period_Id = " + Period_ID ;
            if ( AD_Org_ID != 0 )
                Sql = Sql + "    And P_Accounting_Entry.AD_Org_ID = " + AD_Org_ID;
        	DB.executeUpdate(Sql, trxName);

        }
    	
    	String SqlEntry = "Select isnull(Sum(P_Payment_Entryline.Amtacctdr),0) as AmountDr, isnull(Sum(P_Payment_Entryline.Amtacctcr),0) as AmountCr, " + 
            "P_Payment_Entryline.Org_ID, P_Payment_Entryline.C_SalesRegion_ID, P_Payment_Entryline.C_Activity_Id, P_Payment_Entryline.C_Elementvalue_Id, " +
            " P_Payment.P_Payment_Group_ID " +
            "  From P_Payment" +
            "  Inner Join P_Payment_Entryline On P_Payment.P_Payment_Id = P_Payment_Entryline.P_Payment_Id" +
            "  Left Outer Join C_Elementvalue ON P_Payment_Entryline.C_Elementvalue_Id = C_Elementvalue.C_Elementvalue_ID " +
            "  Where P_Period_Id = " + Period_ID +
            "    And P_Frequency_ID = " + Frequency_ID ;

        if ( AD_Org_ID != 0 )
            SqlEntry = SqlEntry + "    And P_Payment.AD_Org_ID = " + AD_Org_ID;

        if ( IsRework )
          SqlEntry = SqlEntry + "    And ( P_Payment.Timesheetstatus IN ('E', 'T') OR PaymentTypeDoc = 'Annulation-' )" ;
        else
          SqlEntry = SqlEntry + "    And ( P_Payment.Timesheetstatus IN ( 'E' ) OR ( PaymentTypeDoc = 'Annulation-' AND isTransfer = 'N' ) )" ;

        if ( IsOnlyPaymentCanceled  )
            SqlEntry = SqlEntry + "    And PaymentTypeDoc Like 'Annulation%' " ;
        else
        	SqlEntry = SqlEntry + "    And PaymentTypeDoc not Like 'Annulation%' " ;

        SqlEntry = SqlEntry + " Group By P_Payment_Entryline.Org_ID, P_Payment.P_Payment_Group_ID, P_Payment_Entryline.C_SalesRegion_ID, P_Payment_Entryline.C_Activity_Id, P_Payment_Entryline.C_Elementvalue_Id, C_Elementvalue.Value";
        SqlEntry = SqlEntry + " Having ( Sum(isnull(P_Payment_Entryline.Amtacctdr,0)) + Sum(isnull(P_Payment_Entryline.Amtacctcr,0))) <> 0 " ;
//        SqlEntry = SqlEntry + " ORDER BY P_Payment_Entryline.Org_ID, P_Payment.P_Payment_Group_ID, P_Payment_Entryline.C_SalesRegion_ID, P_Payment_Entryline.C_Elementvalue_Id, P_Payment_Entryline.C_Activity_Id " ;
        SqlEntry = SqlEntry + " ORDER BY P_Payment_Entryline.Org_ID, C_Elementvalue.Value, P_Payment_Entryline.C_Activity_Id, P_Payment.P_Payment_Group_ID, P_Payment_Entryline.C_SalesRegion_ID  " ;
        
        PreparedStatement pstmp = null;
        try
        {
            pstmp = DB.prepareStatement(SqlEntry, null);
            ResultSet rs = pstmp.executeQuery();
            int NewEntry = 0; 
            BigDecimal MntDebit = new BigDecimal(0);
            BigDecimal MntCredit = new BigDecimal(0);
            BigDecimal TotalDr = new BigDecimal(0);
            BigDecimal TotalCr = new BigDecimal(0);

            Calendar cal = Calendar.getInstance();
            Timestamp XDate = new Timestamp(cal.getTimeInMillis());
            int Org_ID = 0;

            int NoLigne = 1;
            NewFile = new StringBuffer();

            while ( rs.next() )
            {
//
            	if ( rs.getInt( "Org_ID") != Org_ID )
            	{
                    Value = DB.getDocumentNo(Client_ID, "P_Accounting_Entry", trxName);
                    FileValue = Value.trim();

                    if ( NewFile.length() != 0 )
            		{
                        CreateGrandTotal(NewFile, TotalDr, TotalCr, trxName, "REG");
                        TotalDr = Env.ZERO;
                        TotalCr = Env.ZERO;

                        MOrg Org = MOrg.get( getCtx(), Org_ID );
                        
                        if ( IsOnlyPaymentCanceled  )
                        	FileName = Org.getValue().trim() + "_Ventilation_" + Period.getEndDate().toString().substring(0,10) + "_" + FileValue + "_Ann";
                        else
                        	FileName = Org.getValue().trim() + "_Ventilation_" + Period.getEndDate().toString().substring(0,10) + "_" + FileValue;
                    	WriteToFile(NewFile, trxName);
                        NewFile = new StringBuffer();
            		}

                    NoLigne = 1;
                    P_Accounting_Entry Entry = new P_Accounting_Entry(getCtx(), -1, trxName);
                    Entry.setAD_Org_ID( rs.getInt( "Org_ID"));
                    Entry.setDocumentNo(Value);
                    Entry.setDateDoc(XDate);
                    Entry.setP_Period_ID(Period_ID);
                    Entry.setRegularWriting(true);
                    Entry.setP_Payment_Group_ID(rs.getInt( "P_Payment_Group_ID"));
                    Entry.save();
                    NewEntry = Entry.getP_Accounting_Entry_ID();
                    
                    Org_ID = rs.getInt( "Org_ID");
                    
                    CreateFirstLine(NewFile, Entry, trxName, "REG");

            	}
//
            	MntDebit = rs.getBigDecimal("AmountDR");
                MntCredit = rs.getBigDecimal("AmountCR");
                
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
//                MntTotalDebit = MntTotalDebit.add(MntDebit);
//                MntTotalCredit = MntTotalDebit.add(MntCredit);
                if ( ( MntDebit != Env.ZERO ) || ( MntCredit != Env.ZERO ) ) 
                {
                	P_Accounting_EntryLine EntryLine = new P_Accounting_EntryLine(getCtx(), -1, trxName);
                    EntryLine.setP_Payment_Group_ID(rs.getInt( "P_Payment_Group_ID"));
                    EntryLine.setP_Accounting_Entry_ID(NewEntry);
                    EntryLine.setC_Activity_ID(rs.getInt("C_Activity_ID"));
                    EntryLine.setC_ElementValue_ID(rs.getInt("C_Elementvalue_Id"));
                    EntryLine.setAD_Org_ID(rs.getInt("Org_ID"));
                    EntryLine.setOrg_ID(rs.getInt("Org_ID"));
                    EntryLine.setC_SalesRegion_ID(rs.getInt("C_SalesRegion_id"));
                    EntryLine.setAmtAcctCr(MntCredit);
                    EntryLine.setAmtAcctDr(MntDebit);
                    EntryLine.setLine(NoLigne);
                    EntryLine.save();

                    TotalDr = TotalDr.add(MntDebit);
                    TotalCr = TotalCr.add(MntCredit);

                    NoLigne++;
                    CreateFile(NewFile, EntryLine, trxName, "REG");

                }
            }
            rs.close();
	        pstmp.close();
	        pstmp = null;

            if ( NewFile.length() != 0 )
    		{
                CreateGrandTotal(NewFile, TotalDr, TotalCr, trxName, "REG");
                TotalDr = Env.ZERO;
                TotalCr = Env.ZERO;

                MOrg Org = MOrg.get( getCtx(), Org_ID );

                if ( IsOnlyPaymentCanceled  )
                	FileName = Org.getValue().trim() + "_Ventilation_" + Period.getEndDate().toString().substring(0,10) + "_" + FileValue + "_Ann";
                else
                	FileName = Org.getValue().trim() + "_Ventilation_" + Period.getEndDate().toString().substring(0,10) + "_" + FileValue;
                	
            	WriteToFile(NewFile, trxName);
                NewFile = new StringBuffer();
    		}

            User_ID = Env.getAD_User_ID(getCtx());
            

        }
        catch (Exception e)
        {
            log.log (Level.SEVERE,"P_InOutTransfertGeneralLedger.UpdateDetail", e);   
        }
    }

    private void ExpenseCorrection(String trxName)
    {
        if ( IsOnlyPaymentCanceled  )
        	return;

    	P_ExpenseCorrection Expense = null;

        Calendar cal = Calendar.getInstance();
        Timestamp XDate = new Timestamp(cal.getTimeInMillis());

        PreparedStatement pstmp = null;

        try
		{           
        	String sql = "Select 1 from P_ExpenseCorrection where P_Accounting_Entry_ID is null";


        	pstmp = DB.prepareStatement(sql, null);
	        ResultSet rs = pstmp.executeQuery();
        	
	        int Count = 0;
            int NoLigne = 1;
	        
	        if ( rs.next() )
	        {
	        	Count = rs.getInt(1);
	        }
	        rs.close();
	        pstmp.close();
	        pstmp = null;
        	
	        if (Count == 0)
	        {
	        	return;
	        }

	        BigDecimal MntDebit = new BigDecimal(0);
	        BigDecimal MntCredit = new BigDecimal(0);
            BigDecimal TotalDr = new BigDecimal(0);
            BigDecimal TotalCr = new BigDecimal(0);

	        int NewEntry = 0; 
            int Org_ID = 0;
            int P_ExpenseCorrection_ID = 0;

        	sql = "Select isnull(Sum(P_Payment_Entryline.Amtacctdr),0) as AmountDr, isnull(Sum(P_Payment_Entryline.Amtacctcr),0) as AmountCr, "  
                + " P_Payment_Entryline.Org_ID, P_Payment_Entryline.C_SalesRegion_ID, P_Payment_Entryline.C_Activity_Id, P_Payment_Entryline.C_Elementvalue_Id, " 
                + " P_Payment.P_Payment_Group_ID, P_ExpenseCorrection_ID " 
                + "  From P_Payment " 
                + "  Inner Join P_Payment_Entryline On P_Payment.P_Payment_Id = P_Payment_Entryline.P_Payment_Id " 
                + " Where P_Expensecorrection_Id In (Select P_ExpenseCorrection_ID from P_ExpenseCorrection where P_Accounting_Entry_ID is null) " 
                ;

            if ( AD_Org_ID != 0 )
                sql = sql + "    And P_Payment.AD_Org_ID = " + AD_Org_ID;

            sql = sql + " Group By P_ExpenseCorrection_ID, P_Payment_Entryline.Org_ID, P_Payment.P_Payment_Group_ID, P_Payment_Entryline.C_SalesRegion_ID, P_Payment_Entryline.C_Activity_Id, P_Payment_Entryline.C_Elementvalue_Id";
            sql = sql + " Having ( Sum(isnull(P_Payment_Entryline.Amtacctdr,0)) + Sum(isnull(P_Payment_Entryline.Amtacctcr,0))) <> 0 " ;
            sql = sql + " ORDER BY P_ExpenseCorrection_ID, P_Payment_Entryline.Org_ID, P_Payment.P_Payment_Group_ID, P_Payment_Entryline.C_SalesRegion_ID, P_Payment_Entryline.C_Elementvalue_Id, P_Payment_Entryline.C_Activity_Id " ;
;
	        pstmp = DB.prepareStatement(sql, null);
	        rs = pstmp.executeQuery();
                
	        if ( NewFile == null )
	        	NewFile = new StringBuffer();

	        while ( rs.next() )
	        {

//            	if ( rs.getInt( "Org_ID") != Org_ID || rs.getInt("P_ExpenseCorrection_ID") != P_ExpenseCorrection_ID )
//            	{
            	if ( rs.getInt("P_ExpenseCorrection_ID") != P_ExpenseCorrection_ID )
            	{
                    Value = DB.getDocumentNo(Client_ID, "P_Accounting_Entry", trxName);
                    FileValue = Value.trim();

                    
                    if ( NewFile.length() != 0 )
            		{
                        CreateGrandTotal(NewFile, TotalDr, TotalCr, trxName, "COR");
                        TotalDr = Env.ZERO;
                        TotalCr = Env.ZERO;
            	    	MOrg Org = MOrg.get( getCtx(), Org_ID );

            	    	FileName = Org.getValue().trim() + "_Correction_" + Period.getEndDate().toString().substring(0,10) + "_" + FileValue;
                    	WriteToFile(NewFile, trxName);
                        NewFile = new StringBuffer();
            		}

                    NoLigne = 1;
                    P_Accounting_Entry Entry = new P_Accounting_Entry(getCtx(), -1, trxName);
                    Entry.setAD_Org_ID( rs.getInt( "Org_ID"));
                    Entry.setDocumentNo(Value);
                    Entry.setDateDoc(XDate);
                    Entry.setP_Period_ID(Period_ID);
                    Entry.setRegularWriting(true);
                    Entry.setP_Payment_Group_ID(rs.getInt( "P_Payment_Group_ID"));
                    Entry.save();
                    NewEntry = Entry.getP_Accounting_Entry_ID();
                    
                    Org_ID = rs.getInt( "Org_ID");
                    P_ExpenseCorrection_ID = rs.getInt("P_ExpenseCorrection_ID");
                    
                    CreateFirstLine(NewFile, Entry, trxName, "COR");

				    Expense= new P_ExpenseCorrection(getCtx(), rs.getInt("P_ExpenseCorrection_ID"), trxName);
				    Expense.setP_Accounting_Entry_ID(Entry.getP_Accounting_Entry_ID());
				    Expense.save();

            	}
            	MntDebit = rs.getBigDecimal("AmountDR");
                MntCredit = rs.getBigDecimal("AmountCR");
                
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

                if ( ( MntDebit != Env.ZERO ) || ( MntCredit != Env.ZERO ) ) 
                {
                	P_Accounting_EntryLine EntryLine = new P_Accounting_EntryLine(getCtx(), -1, trxName);
                    EntryLine.setP_Payment_Group_ID(rs.getInt( "P_Payment_Group_ID"));
                    EntryLine.setP_Accounting_Entry_ID(NewEntry);
                    EntryLine.setC_Activity_ID(rs.getInt("C_Activity_ID"));
                    EntryLine.setC_ElementValue_ID(rs.getInt("C_Elementvalue_Id"));
                    EntryLine.setAD_Org_ID(rs.getInt("Org_ID"));
                    EntryLine.setOrg_ID(rs.getInt("Org_ID"));
                    EntryLine.setC_SalesRegion_ID(rs.getInt("C_SalesRegion_id"));
                    EntryLine.setAmtAcctCr(MntCredit);
                    EntryLine.setAmtAcctDr(MntDebit);
                    EntryLine.setLine(NoLigne);
                    EntryLine.save();

                    TotalDr = TotalDr.add(MntDebit);
                    TotalCr = TotalCr.add(MntCredit);

                    NoLigne++;
                    CreateFile(NewFile, EntryLine, trxName, "COR");
                }
            }
            rs.close();
	        pstmp.close();
	        pstmp = null;

            if ( NewFile.length() != 0 )
    		{
                CreateGrandTotal(NewFile, TotalDr, TotalCr, trxName, "COR");
                TotalDr = Env.ZERO;
                TotalCr = Env.ZERO;
    	    	MOrg Org = MOrg.get( getCtx(), Org_ID );

    	    	FileName = Org.getValue().trim() + "_Correction_" + Period.getEndDate().toString().substring(0,10) + "_" + FileValue;
            	WriteToFile(NewFile, trxName);
                NewFile = new StringBuffer();
    		}

		}
        catch (Exception e)
        {
            log.log(Level.SEVERE, "P_InOutTransfertGeneralLedger.ExpenseCorrection", e);
        }
    }

    /*


*/

    private void CreateFileBen(String trxName) 
    {      
    	String SqlEntry = "Select isnull(Sum(p_payment_gain.AmountCalc),0) as Amount, P_Payment.P_Payment_Group_ID, "  
  	  					+ " P_Payment.C_Activity_Id, P_Benefit_Param.percentage, P_Benefit_Param.AD_Org_ID As Org_ID , "
  	                    + " P_Benefit_Param.ACCT_DEBIT, P_Benefit_Param.ACCT_CREDIT, P_Payment.P_WorkPlace_ID " 
   	                    + " From P_Payment " 
  	                    + " Inner Join p_payment_gain On P_Payment.P_Payment_Id = p_payment_gain.P_Payment_Id " 
  	                    + " Inner Join P_Benefit_Param  On P_Benefit_Param.AD_Org_ID = P_Payment.Ad_Org_ID and P_Benefit_Param.C_Activity_ID = P_Payment.C_Activity_ID" 
  	                    + "	Inner Join P_Benefit_Param_Detail On P_Benefit_Param_Detail.P_Benefit_Param_ID = P_Benefit_Param.P_Benefit_Param_ID"
  	                    + " Inner Join P_Employee On P_Employee.P_Employee_ID = P_Payment.P_Employee_ID "
  	                    + " Where P_Payment.P_Period_Id = " + Period_ID  
  	                    + " And P_Payment.P_Frequency_ID = " + Frequency_ID  
  	                    + " And p_payment_gain.P_Gain_ID   in ( P_Benefit_Param_Detail.P_gain_id )"
  	                    + " And P_Employee.P_Employee_ID = P_Payment.P_Employee_ID "
  	                    ;

        if ( AD_Org_ID != 0 )
        	SqlEntry = SqlEntry + "    And P_Payment.AD_Org_ID = " + AD_Org_ID;

        if ( IsRework )
          SqlEntry = SqlEntry + "    And ( P_Payment.Timesheetstatus IN ('E', 'T') OR PaymentTypeDoc = 'Annulation-' )" ;
        else
          SqlEntry = SqlEntry + "    And ( P_Payment.Timesheetstatus IN ( 'E' ) OR ( PaymentTypeDoc = 'Annulation-' AND isTransfer = 'N' )  )" ;

        if ( IsOnlyPaymentCanceled  )
            SqlEntry = SqlEntry + "    And PaymentTypeDoc Like 'Annulation%' " ;
        else
        	SqlEntry = SqlEntry + "    And PaymentTypeDoc not Like 'Annulation%' " ;

        
        SqlEntry = SqlEntry + " Group By P_Payment.P_WorkPlace_ID, P_Payment.AD_Org_ID, P_Payment.C_Activity_Id , P_Payment.P_Payment_Group_ID, " 
		                    + " P_Benefit_Param.percentage, P_Benefit_Param.AD_Org_ID, P_Benefit_Param.ACCT_DEBIT, P_Benefit_Param.ACCT_CREDIT ";
        SqlEntry = SqlEntry + " ORDER BY P_Payment.AD_Org_ID   " ;
        
        PreparedStatement pstmp = null;
        try
        {
            pstmp = DB.prepareStatement(SqlEntry, null);
            ResultSet rs = pstmp.executeQuery();
            int NewEntry = 0; 
            BigDecimal MntDebit = new BigDecimal(0);
            BigDecimal MntCredit = new BigDecimal(0);
            BigDecimal TotalDr = new BigDecimal(0);
            BigDecimal TotalCr = new BigDecimal(0);

            Calendar cal = Calendar.getInstance();
            Timestamp XDate = new Timestamp(cal.getTimeInMillis());
            int Org_ID = 0;

            int NoLigne = 1;
            NewFile = new StringBuffer();

            while ( rs.next() )
            {
//
            	if ( rs.getInt( "Org_ID") != Org_ID )
            	{
                    Value = DB.getDocumentNo(Client_ID, "P_Accounting_Entry", trxName);
                    FileValue = Value.trim();

                    if ( NewFile.length() != 0 )
            		{
                        CreateGrandTotal(NewFile, TotalDr, TotalCr, trxName, "BEN");
                        TotalDr = Env.ZERO;
                        TotalCr = Env.ZERO;
            	    	MOrg Org = MOrg.get( getCtx(), Org_ID );

                        if ( IsOnlyPaymentCanceled  )
                        	FileName = Org.getValue().trim() + "_Benefice_" + Period.getEndDate().toString().substring(0,10) + "_" + FileValue + "+Ann";
                        else
                        	FileName = Org.getValue().trim() + "_Benefice_" + Period.getEndDate().toString().substring(0,10) + "_" + FileValue;
                    	WriteToFile(NewFile, trxName);
                        NewFile = new StringBuffer();
            		}

                    NoLigne = 1;
                    P_Accounting_Entry Entry = new P_Accounting_Entry(getCtx(), -1, trxName);
                    Entry.setAD_Org_ID( rs.getInt( "Org_ID"));
                    Entry.setDocumentNo(Value);
                    Entry.setDateDoc(XDate);
                    Entry.setP_Period_ID(Period_ID);
                    Entry.setRegularWriting(true);
                    Entry.setP_Payment_Group_ID(rs.getInt( "P_Payment_Group_ID"));
                    Entry.save();
                    NewEntry = Entry.getP_Accounting_Entry_ID();
                    
                    Org_ID = rs.getInt( "Org_ID");
                    
                    CreateFirstLine(NewFile, Entry, trxName, "BEN");
                    
            	}
//

            	MntDebit  = rs.getBigDecimal("Amount").multiply( rs.getBigDecimal("percentage").divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP) ).setScale(2, BigDecimal.ROUND_HALF_UP);
                MntCredit = MntDebit; 
                
                P_Workplace Workplace = P_Workplace.get( getCtx(), rs.getInt("P_WorkPlace_ID"), trxName);

                if ( ( MntDebit != Env.ZERO ) || ( MntCredit != Env.ZERO ) ) 
                {
                	P_Accounting_EntryLine EntryLine = new P_Accounting_EntryLine(getCtx(), -1, trxName);
                    EntryLine.setP_Payment_Group_ID(rs.getInt( "P_Payment_Group_ID"));
                    EntryLine.setP_Accounting_Entry_ID(NewEntry);
                    EntryLine.setC_Activity_ID(rs.getInt("C_Activity_ID"));
                    EntryLine.setC_ElementValue_ID(rs.getInt("ACCT_DEBIT"));
                    EntryLine.setAD_Org_ID(rs.getInt("Org_ID"));
                    EntryLine.setOrg_ID(rs.getInt("Org_ID"));
                    EntryLine.setC_SalesRegion_ID( Workplace.getC_SalesRegion_ID());
                    if ( MntDebit.compareTo( Env.ZERO) < 0 )
                    {
                        EntryLine.setAmtAcctCr(MntDebit.abs());
                        EntryLine.setAmtAcctDr(Env.ZERO);
                        TotalCr = TotalCr.add(MntDebit.abs());
                    }
                    else
                    {
                        EntryLine.setAmtAcctCr(Env.ZERO);
                        EntryLine.setAmtAcctDr(MntDebit);
                        TotalDr = TotalDr.add(MntDebit);
                    }
                    EntryLine.setLine(NoLigne);
                    EntryLine.save();

                    CreateFile(NewFile, EntryLine, trxName, "BEN");

                    NoLigne++;
                    
                    P_Payment_Group PaymentGroup = P_Payment_Group.get( getCtx(), rs.getInt("P_Payment_Group_ID" ), trxName);
                	EntryLine = new P_Accounting_EntryLine(getCtx(), -1, trxName);
                    EntryLine.setP_Payment_Group_ID(rs.getInt( "P_Payment_Group_ID"));
                    EntryLine.setP_Accounting_Entry_ID(NewEntry);
                    EntryLine.setC_Activity_ID(rs.getInt("C_Activity_ID"));
                    EntryLine.setC_ElementValue_ID(rs.getInt("ACCT_CREDIT"));
                    EntryLine.setAD_Org_ID(rs.getInt("Org_ID"));
                    EntryLine.setOrg_ID(rs.getInt("Org_ID"));
                    EntryLine.setC_SalesRegion_ID( PaymentGroup.getC_SalesRegion_ID() );
                    if ( MntCredit.compareTo( Env.ZERO) < 0 )
                    {
                        EntryLine.setAmtAcctCr(Env.ZERO);
                        EntryLine.setAmtAcctDr(MntCredit.abs());
                        TotalDr = TotalDr.add(MntCredit.abs());
                    }
                    else
                    {
                        EntryLine.setAmtAcctCr(MntCredit);
                        EntryLine.setAmtAcctDr(Env.ZERO);
                        TotalCr = TotalCr.add(MntCredit);
                    }
                    EntryLine.setLine(NoLigne);
                    EntryLine.save();

                    NoLigne++;

                    CreateFile(NewFile, EntryLine, trxName, "BEN");


                }
            }
            rs.close();
	        pstmp.close();
	        pstmp = null;

            if ( NewFile.length() != 0 )
    		{
                CreateGrandTotal(NewFile, TotalDr, TotalCr, trxName, "BEN");
                TotalDr = Env.ZERO;
                TotalCr = Env.ZERO;
    	    	MOrg Org = MOrg.get( getCtx(), Org_ID );

                if ( IsOnlyPaymentCanceled  )
                	FileName = Org.getValue().trim() + "_Benefice_" + Period.getEndDate().toString().substring(0,10) + "_" + FileValue + "_Ann";
                else
                	FileName = Org.getValue().trim() + "_Benefice_" + Period.getEndDate().toString().substring(0,10) + "_" + FileValue;
            	WriteToFile(NewFile, trxName);
                NewFile = new StringBuffer();
    		}

        
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "P_InOutTransfertGeneralLedger.Benefits", e);
        }
    }

    
    private void CreateFirstLine(StringBuffer Nf, P_Accounting_Entry Entry,  String trxName, String typeEr)
    {
/*
        if ( typeEr.equals("REG"))
        {
            String sql = "INSERT INTO AD_PInstance_PARA (AD_PInstance_id, seqno, ParameterName, P_String, P_String_To, P_Number, P_Number_To, P_Date, P_Date_To, Info, Info_To, Ad_Client_id, Ad_Org_id, Created, createdby, Updated, UpdatedBy, Isactive) " +
            "Select " + Instance_ID + ", " +
            "(Select Max(seqno) + 1 from AD_PInstance_Para where AD_PInstance_id = " + Instance_ID + "), " +
            "'DocumentNo', " + Entry.getDocumentNo() + ", null, null, null, null, null, null, null, " + Client_ID + ", " + AD_Org_ID + ", GetDate(), " + User_ID + ", GetDate(), " + User_ID + ", 'Y'";
            
            DB.executeUpdate(sql, null);
        }
*/
        
    	P_Payment_Group PaymentGroup = P_Payment_Group.get( getCtx(), Entry.getP_Payment_Group_ID(), trxName );
    	MOrg Org = MOrg.get( getCtx(), Entry.getAD_Org_ID() );
    	
        String DatePay = String.valueOf(Period.getEndDate()).substring(2, 4) +
                         String.valueOf(Period.getEndDate()).substring(5, 7) +
                         String.valueOf(Period.getEndDate()).substring(8, 10);

        String DateMonth = String.valueOf(Period.getEndDate()).substring(2, 4) +
        				 String.valueOf(Period.getEndDate()).substring(5, 7) ;

        String Cie;
        if ( PaymentGroup.getValue().equals("11"))
        	Cie = "3 ";
        else if ( PaymentGroup.getValue().equals("10"))
        	Cie = "4 ";
        else
            Cie = "18";

        
        String FirstLine = Cie + " " + PaymentGroup.getValue() + " " + "000" + DatePay + DateMonth + Org.getValue() 
                         + "                          "  + DatePay
                         + "                        " + DatePay + Org.getValue() + "     " + DatePay 
                         +  "\r\n"
                         ;
        if ( typeEr.equals("BEN"))
        {
//        	3  11 00007051507057AUZ                          070515                        0705157AUZ BEN 070515                                
            FirstLine = Cie + " " + PaymentGroup.getValue() + " " + "000" + DatePay + DateMonth + Org.getValue() 
            + "                          "  + DatePay
            + "                        " + DatePay + Org.getValue() + " BEN " + DatePay 
            +  "\r\n"
            ;

        }

        if ( IsOnlyPaymentCanceled  )
        {
            FirstLine = Cie + " " + PaymentGroup.getValue() + " " + "000" + DatePay + DateMonth + Org.getValue() 
            + "                          "  + DatePay
            + "                        " + DatePay + Org.getValue() + " ANN " + DatePay 
            +  "\r\n"
            ;
            if ( typeEr.equals("BEN"))
            {
            	//	3  11 00007051507057AUZ                          070515                        0705157AUZ BEN 070515                                
            	FirstLine = Cie + " " + PaymentGroup.getValue() + " " + "000" + DatePay + DateMonth + Org.getValue() 
            	+ "                          "  + DatePay
            	+ "                        " + DatePay + Org.getValue() + " BANN" + DatePay 
            	+  "\r\n"
            	;

            }	
        	
        }

        Nf.append(FirstLine);
    }

    private void CreateGrandTotal( StringBuffer Nf, BigDecimal TotalDr, BigDecimal TotalCR,  String trxName, String typeEr)
    {

       	String Dr = filler( String.valueOf( TotalDr ).replace(".", "") );
    	String Cr = filler( String.valueOf( TotalCR ).replace(".", "") );
    	
    	if ( typeEr.equals("BEN"))
    	{
        	Nf.append("GRAND TOT.                        " + Dr + "\r\n" );
            Nf.append("GRAND TOT.                                    " + Cr + "\r\n");
    	}
    	else
    	{
        	Nf.append("GRAND TOT.                        " + Dr + "\r\n" );
            Nf.append("GRAND TOT.                                    " + Cr + "\r\n");
    	}

    }
    
 
    private void CreateFile(StringBuffer Nf, P_Accounting_EntryLine EntryLine, String trxName, String typeEr)
    {

      	P_Payment_Group PaymentGroup = P_Payment_Group.get( getCtx(), EntryLine.getP_Payment_Group_ID(), trxName );
    	MOrg Org = MOrg.get( getCtx(), EntryLine.getOrg_ID() );
    	MActivity Activity = new MActivity( getCtx(), EntryLine.getC_Activity_ID(), trxName);

        MElementValue ElementValue = new MElementValue(getCtx(), EntryLine.getC_ElementValue_ID(), trxName);
        MSalesRegion  SalesRegion  = new MSalesRegion (getCtx(), EntryLine.getC_SalesRegion_ID(), trxName);
        
        String Cie;
        if ( PaymentGroup.getValue().equals("11"))
        	Cie = "3 ";
        else if ( PaymentGroup.getValue().equals("10"))
        	Cie = "4 ";
        else
            Cie = "18";
        
        String Dr = "           ";
        String Cr = "           ";
        if ( EntryLine.getAmtAcctDr().compareTo(Env.ZERO) != 0)
        {
        	Dr = filler( String.valueOf( EntryLine.getAmtAcctDr() ).replace(".", "") );
        }
        if ( EntryLine.getAmtAcctCr().compareTo(Env.ZERO) != 0)
        {
        	Cr = filler( String.valueOf( EntryLine.getAmtAcctCr() ).replace(".", "") );
        }


        String Line ;
        
        if ( typeEr.equals("BEN"))
        {
            Line = Cie + " " + PaymentGroup.getValue().trim() + " 000" + Cie + " " + PaymentGroup.getValue().trim() + " " + SalesRegion.getValue().trim() + ElementValue.getValue().trim() + "         "
            + Dr + " "
            + Cr
            + "                                               "
            + Activity.getValue().trim()
            + " \r\n"
            ;
//            3  11 0003  11 4265020000         00000118637                                                                                       
//            3  11 0003  11 0002126050                     00000118637                                                                           

        }
        else
        {
            Line = Cie + " " + PaymentGroup.getValue().trim() + " 000" + Cie + " " + PaymentGroup.getValue().trim() + " " + SalesRegion.getValue().trim() + ElementValue.getValue().trim() + "         "
            + Dr + " "
            + Cr
            + "                                               "
            + Activity.getValue().trim()
            + " \r\n"
            ;
        }
        
        Nf.append( Line );
    }
    
    
    private void WriteToFile(StringBuffer nf, String trxName)
    {
        String Path = "";
       	String sql = "Select TransfertPath from P_System_Parameters ";
        PreparedStatement psdoc = null;
       try
  	   { 
            psdoc = DB.prepareStatement(sql, null);
            ResultSet rsdoc = psdoc.executeQuery();
            if (rsdoc.next())
            {
                
                Path = rsdoc.getString("TransfertPath");
            }
            rsdoc.close();
           psdoc.close();
        }
        catch (SQLException e)
        {
            log.log(Level.WARNING, "CreateFileTransfertRBC.WriteToFile", e);
        }
       
//       Path = "C:\\Solstice\\";
       
        log.log(Level.INFO, "P_InOutTransfertGeneralLedger.WriteToFile - Path" + Path);
  	    File aFile = null;
  	    aFile = new File(Path + "Gl\\" + FileName + ".txt");
        FileOutputStream out; // declare a file output object
        PrintStream p; // declare a print stream object
        try
        {
            // Create a new file output stream
            out = new FileOutputStream(aFile);

            // Connect print stream to the output stream
            p = new PrintStream( out );

//            PrintWriter p2 = new PrintWriter( out);
            
            p.println (nf.toString());
//            p2.println(nf.toString());
            System.out.println(nf.toString());
//            p2.close();
            p.close();
        }
        catch (Exception e)
        {
               System.out.println ("Error writing to file :" + e.toString());
        }
        //log.debug("CreateFileTransfertRBC.WriteToFile - End");

    }
    
    private void UpdateFeuille(String trxName)
    {
       	String sql = "Update P_Time_Sheet " +
       			     "  set TimeSheetStatus = 'T' " +
       	             " where P_Period_id = " + Period_ID  +
       	             "   and P_Frequency_ID = " + Frequency_ID +
       	             "   and TimeSheetStatus in ( 'E' ) ";

        if ( AD_Org_ID != 0 )
        	sql = sql + "    And P_Time_Sheet.AD_Org_ID = " + AD_Org_ID;

       	
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
    
	private String filler(String theInput )
	{
		String vide="00000000000";

		String res= theInput ; // ta chaine

		return vide.substring(0,vide.length() - res.length()) + res;
	}
    
	public Properties getCtx()
	{
		return m_ctx;
	}

}

