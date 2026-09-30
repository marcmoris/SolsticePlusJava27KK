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
import solstice.model.P_ExpenseCorrection;

/**
 * @author rejgar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutTransfertGeneralLedger 
{
	private int GlobalLineNumber=1;
	private String m_trxName;
    private Properties m_ctx;
    private int Client_ID = 0;
    private int Org_ID = 0;
    private int Frequency_ID = 0;
    private int Period_ID = 0;
    private int User_ID = 0;
    private int Accounting_Entry_ID = 0;
    private int Instance_ID = 0;
    private String Value = "";
    private String FileValue = "";
    private CLogger			log = CLogger.getCLogger (getClass());
    private boolean IsRework = false;

    
    private String FillNumber = "0";

    public P_InOutTransfertGeneralLedger( Properties ctx)
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
        
        StringBuffer NewFile = new StringBuffer();
        boolean Validation =  ValidTotal(trxName);
        if ( Validation )
        {
        	GetSequenceNumber();
            CreateFirstLine(NewFile, trxName);
            UpdateAndCreateFile(trxName);
            CreateFile(NewFile, trxName);
            if ( ExpenseCorrection(trxName) )
            {
            	CreateFile(NewFile, trxName);
            }
        	WriteToFile(NewFile, trxName);
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
    
    private void CreateFirstLine(StringBuffer Nf, String trxName)
    {
        String FirstLine = "";
        FirstLine = "ldr_entity_id" +
                    "\t" +                        
                    "jrnl_id" +
                    "\t" +
                    "eff_date" +
                    "\t" +
                    "jrnl_seq_nbr" +
                    "\t" +
                    "jrnl_line_nbr" +
                    "\t" +
                    "line_ldr_entity_id" +
                    "\t" +
                    "compte" +
                    "\t" +
                    "centre" +
                    "\t" +
                    "projet" +
                    "\t" +
                    "trans_amt" +
                    "\t" +
                    "prim_dr_cr_code" +
                    "\t" +
                    "jrnl_user_alpha_fld_1" +
                    "\t" +
                    "jrnl_user_alpha_fld_2" +
                    "\t" +
                    "jrnl_user_alpha_fld_3" +
                    "\t" +
                    "descp" 
                    +  "\n"
                    ;
        Nf.append(FirstLine);
    }
    
    private void UpdateAndCreateFile(String trxName) 
    {
        
        String SqlEntry = "Select Sum(isnull(P_Payment_Entryline.Amtacctdr,0)) as AmountDr, Sum(isnull(P_Payment_Entryline.Amtacctcr,0)) as AmountCr, " + 
                          "P_Payment_Entryline.C_Activity_Id, P_Payment_Entryline.C_Elementvalue_Id" +
                          "  From P_Payment" +
                          "  Inner Join P_Payment_Entryline On P_Payment.P_Payment_Id = P_Payment_Entryline.P_Payment_Id" +
                          "  Where P_Period_Id = " + Period_ID +
                          "    And P_Frequency_ID = " + Frequency_ID ;
        if ( IsRework )
          SqlEntry = SqlEntry + "    And P_Payment.Timesheetstatus IN ('E', 'Z', 'T')" ;
        else
          SqlEntry = SqlEntry + "    And P_Payment.Timesheetstatus IN ( 'E', 'Z' )" ;

        SqlEntry = SqlEntry + " Group By P_Payment_Entryline.C_Activity_Id, P_Payment_Entryline.C_Elementvalue_Id";
        SqlEntry = SqlEntry + " Having ( Sum(isnull(P_Payment_Entryline.Amtacctdr,0)) + Sum(isnull(P_Payment_Entryline.Amtacctcr,0))) <> 0 " ;
        SqlEntry = SqlEntry + " ORDER BY P_Payment_Entryline.C_Elementvalue_Id, P_Payment_Entryline.C_Activity_Id " ;
        
        P_Accounting_Entry Entry = null;
        P_Accounting_EntryLine EntryLine = null;
        PreparedStatement pstmp = null;
        try
        {
            pstmp = DB.prepareStatement(SqlEntry, null);
            ResultSet rs = pstmp.executeQuery();
            P_Period Period = null;
        //    MPeriod PeriodCtb = null;
            MActivity Activity = null;
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
            String Siq = "SIQ";
            String Paie = "PAIE    ";
            String DatePay = String.valueOf(Period.getEndDate()).substring(0, 4) + "/" +
                             String.valueOf(Period.getEndDate()).substring(5, 7) + "/" +
                             String.valueOf(Period.getEndDate()).substring(8, 10);
            String FillChar = " ";
            String Compte = "";
            String Unite = "";
            String Projet = "      ";
            String Montant = "";
            String AccountType = "";
            Value = DB.getDocumentNo(Client_ID, "P_Accounting_Entry", trxName);
            FileValue = Value.trim();
            int NoLigne = 1;
            Entry = new P_Accounting_Entry(getCtx(), -1, getTrxName());
            Entry.setDocumentNo(Value);
            Entry.setDateDoc(XDate);
            Entry.setP_Period_ID(Period_ID);
            Entry.save();
            NewEntry = Entry.getP_Accounting_Entry_ID();
            while ( rs.next() )
            {
                PeriodGl = Period.getC_Period_ID1();
            //    PeriodCtb = MPeriod.get(getCtx(), PeriodGl, trxName);
                ElementValue = new MElementValue(getCtx(), rs.getInt("C_ElementValue_ID"), trxName);
                Activity = new MActivity(getCtx(), rs.getInt("C_Activity_ID"), trxName);
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
                MntTotalDebit = MntTotalDebit.add(MntDebit);
                MntTotalCredit = MntTotalDebit.add(MntCredit);
                if ( ( MntDebit != Env.ZERO ) || ( MntCredit != Env.ZERO ) ) 
                {
                    EntryLine = new P_Accounting_EntryLine(getCtx(), -1, getTrxName());
                    EntryLine.setP_Accounting_Entry_ID(NewEntry);
                    EntryLine.setC_Activity_ID(rs.getInt("C_Activity_ID"));
                    EntryLine.setC_ElementValue_ID(rs.getInt("C_ElementValue_ID"));
                    if ( MntDebit.compareTo(Env.ZERO) == 0)
                        EntryLine.setAmtAcctCr(MntCredit);
                    else
                        EntryLine.setAmtAcctDr(MntDebit);
                    EntryLine.setLine(NoLigne);
                    EntryLine.save();
                    /*
                    Compte = ElementValue.getValue();
                    Unite = Activity.getValue() + "   ";
                    //Unite = Unite + "        ".substring(Unite.trim().length(), 8 - Unite.trim().length());
                    if ( MntCredit.compareTo(Env.ZERO) > 0 )
                        Montant = String.valueOf(MntCredit);
                    else
                        Montant = String.valueOf(MntDebit);
                    AccountType = ElementValue.getAccountSign();
                    String NoLign = "      ".substring(0, 6 - String.valueOf(NoLigne).length()) + String.valueOf( NoLigne ); 
                    String StrMontant = "            ".substring(0, 11 - String.valueOf(Montant).length()) + String.valueOf(Montant) + " ";
                    Nf.append(Siq + "\t" + 
                              Paie + "\t" + 
                              DatePay + "\t" +
                              FillNumber + "\t" +
                              NoLign + "\t" + 
                              Siq + "\t" + 
                              Compte + "\t" +
                              Unite + "\t" +
                              Projet + "\t" +
                              StrMontant + "\t" +
                              AccountType + "\t" +
                              FillChar + "\t" +
                              FillChar + "\t" +
                              FillChar + "\t" +
                              FillChar + "\t" +
                              "\n");
                    */
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
    
    private void CreateFile(StringBuffer Nf, String trxName)
    {
        String SqlEntry = "Select b.line, b.C_Activity_ID, b.C_ElementValue_ID, isnull(b.AmtAcctDR, 0) as AmtAcctDr, isnull(b.AmtAcctCR, 0) as AmtAcctCR " +
                          "from P_Accounting_Entry a " +
                          " Inner join P_Accounting_Entryline b on a.P_Accounting_Entry_ID = b.P_Accounting_Entry_ID " +
                          "  Where a.P_Period_Id = " + Period_ID +
                          "    And a.DocumentNo = " + Value 
                          ;
        PreparedStatement pstmp = null;
        try
        {
            pstmp = DB.prepareStatement(SqlEntry, null);
            ResultSet rs = pstmp.executeQuery();
            P_Period Period = null;
            //MPeriod PeriodCtb = null;
            MActivity Activity = null;
            MElementValue ElementValue = null;
            BigDecimal MntDebit = new BigDecimal(0);
            BigDecimal MntCredit = new BigDecimal(0);
            Period = P_Period.get(getCtx(), Period_ID, trxName);
            Calendar cal = Calendar.getInstance();
            Timestamp XDate = new Timestamp(cal.getTimeInMillis());
            String Siq = "SIQ";
            String Paie = "PAIE    ";
            String DatePay = String.valueOf(Period.getEndDate()).substring(0, 4) + "/" +
                             String.valueOf(Period.getEndDate()).substring(5, 7) + "/" +
                             String.valueOf(Period.getEndDate()).substring(8, 10);
            String FillChar = " ";
            String Compte = "";
            String Unite = "";
            String Projet = "      ";
            String Montant = "";
            String AccountType = "";
            int Noligne = GlobalLineNumber;
            while ( rs.next() )
            {
                //Noligne = rs.getInt("Line");
                ElementValue = new MElementValue(getCtx(), rs.getInt("C_ElementValue_ID"), trxName);
                Activity = new MActivity(getCtx(), rs.getInt("C_Activity_ID"), trxName);
                MntDebit = rs.getBigDecimal("AmtAcctDR");
                MntCredit = rs.getBigDecimal("AmtAcctCR");
                Compte = ElementValue.getValue().trim();
                Unite = Activity.getValue().trim() + "    ";
                if ( MntCredit.compareTo(Env.ZERO) > 0 )
                {
                    Montant = "-" + String.valueOf(MntCredit);
                    AccountType = "C";
                }
                else
                {
                    Montant = String.valueOf(MntDebit);
                    AccountType = "D";
              	
                }
                
//                AccountType = ElementValue.getAccountSign();

                /*                if (AccountType.compareTo("C")==0)
                {
                	Montant="-" + Montant;
                }
*/                
                String NoLign = "      ".substring(0, 6 - String.valueOf(Noligne).length()) + String.valueOf( Noligne ); 
                String StrMontant = "            ".substring(0, 11 - String.valueOf(Montant).length()) + String.valueOf(Montant) + " ";
                Nf.append(Siq + "\t" + 
                          Paie + "\t" + 
                          DatePay + "\t" +
                          FillNumber + "\t" +
                          NoLign + "\t" + 
                          Siq + "\t" + 
                          Compte + "\t" +
                          Unite + "\t" +
                          Projet + "\t" +
                          StrMontant + "\t" +
                          AccountType + "\t" +
                          FillChar + "\t" +
                          FillChar + "\t" +
                          FillChar + "\t" 
//                          FillChar + "\t" +
                         + " \n"
                          );
                Noligne++;
            }
            rs.close();
            GlobalLineNumber=Noligne;
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
    
    private void WriteToFile(StringBuffer nf, String trxName)
    {
        String Path = "";
        String FileName = "";
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
        log.log(Level.INFO, "P_InOutTransfertGeneralLedger.WriteToFile - Path" + Path);
    	//log.debug("CreateFileTransfertRBC.WriteToFile - Start");
    	P_Period Period = P_Period.get(getCtx(), Period_ID, trxName);
    	FileName = "Painte" + FileValue;
  	    File aFile = null;
  	    aFile = new File(Path + FileName + ".txt");
     	//aFile.createNewFile();
  	    //log.debug("CreateFileTransfert.WriteToFile - StringBufferCapacity " + NewFile.capacity());
        FileOutputStream out; // declare a file output object
        PrintStream p; // declare a print stream object
        try
        {
              // Create a new file output stream
              // connected to "myfile.txt"
              out = new FileOutputStream(aFile);

              // Connect print stream to the output stream
              p = new PrintStream( out );

              //StringBuffer sb = new StringBuffer();
              //sb.insert(0, "lalala");
              //sb.append("This is written to a file333333");
              p.println (nf.toString());
              //p.println ("asdasd");
              System.out.println(nf.toString());
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
            
            if (SequenceNumber!=0)
            {
            	FillNumber = String.valueOf(SequenceNumber); 
//            	FillNumber = InsertZero(4-String.valueOf(SequenceNumber).length()) + String.valueOf(SequenceNumber); 
            }
        }
        catch (Exception e)
		{
        	log.log(Level.SEVERE, "P_InOutTransfertGeneralLedger.GetSequenceNumber", e);
		}
    }
    
	private String InsertZero(int Pos)
	{
		String Zeros="";
    	for (int i = 0; i < Pos; i++)
    	{
    		Zeros+="0";
    	}
    	return Zeros;
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

