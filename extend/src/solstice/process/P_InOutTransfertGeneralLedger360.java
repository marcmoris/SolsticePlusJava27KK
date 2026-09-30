/*
  */
package solstice.process;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MActivity;
import org.compiere.model.MElementValue;
import org.compiere.model.MOrg;
import org.compiere.model.MSalesRegion;
import org.compiere.model.MTree_Base;
import org.compiere.model.MTree_Node;
//import org.compiere.model.MPeriod;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.write.DateFormats;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import solstice.model.P_Accounting_Entry;
import solstice.model.P_Accounting_EntryLine;
import solstice.model.P_Distribution;
import solstice.model.P_Employee;
import solstice.model.P_Period;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Timecode;
import solstice.utils.TimeUtilSolstice;
import solstice.model.P_Payment_Entryline_360;
import solstice.model.P_ExpenseCorrection;
import solstice.model.P_Gain;
import solstice.model.P_Payment;

/**
 */
public class P_InOutTransfertGeneralLedger360 
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

    
    public P_InOutTransfertGeneralLedger360( Properties ctx)
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

        
        
        boolean Validation =  ValidTotal(trxName);
        if ( Validation )
        {
            UpdateAndCreateFile(  trxName);
            
            P_Period Period = P_Period.get( Env.getCtx(), _Period_ID, trxName);
            CreateExcelFile( Period );
            
            UpdateFeuille(trxName);
            UpdatePayment( trxName);
            return "Terminé avec succès";
        }
        else
            return "Les montants débits et crédits ne balance pas.";
    }
    
    private boolean ValidTotal(String trxName)
    {
        String SqlEntry = "Select Sum(isnull(P_Payment_Entryline_360.Amtacctdr, 0)) as AmountDr, Sum(isnull(P_Payment_Entryline_360.Amtacctcr, 0)) as AmountCr " + 
                          "  From P_Payment" +
                          "  Inner Join P_Payment_Entryline_360 On P_Payment.P_Payment_Id = P_Payment_Entryline_360.P_Payment_Id" +
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
    
    
    private void UpdateAndCreateFile( String trxName) 
    {
        String SqlEntry = "Select Sum(isnull(P_Payment_Entryline_360.Amtacctdr,0)) as AmountDr, Sum(isnull(P_Payment_Entryline_360.Amtacctcr,0)) as AmountCr, " + 
                " P_Payment_Entryline_360.C_Elementvalue_Id, P_Payment_Entryline_360.Org_ID, P_Payment_Entryline_360.C_Activity_ID, P_Payment_Entryline_360.C_SalesRegion_ID " +
                "  From P_Payment" +
                "  Inner Join P_Payment_Entryline_360 On P_Payment.P_Payment_Id = P_Payment_Entryline_360.P_Payment_Id" +
                "  Where P_Period_Id = " + Period_ID 
//                "    AND P_Payment_Group_ID = " +  P_Payment_Group_ID
                ;

		if ( IsRework )
		SqlEntry = SqlEntry + "    And P_Payment.Timesheetstatus IN ('E', 'Z', 'T')" ;
		else
		SqlEntry = SqlEntry + "    And P_Payment.Timesheetstatus IN ( 'E', 'Z' )" ;
		
		SqlEntry = SqlEntry + " Group By P_Payment_Entryline_360.C_Elementvalue_Id, P_Payment_Entryline_360.Org_ID, P_Payment_Entryline_360.C_Activity_ID, P_Payment_Entryline_360.C_SalesRegion_ID";
		SqlEntry = SqlEntry + " Having ( Sum(isnull(P_Payment_Entryline_360.Amtacctdr,0)) + Sum(isnull(P_Payment_Entryline_360.Amtacctcr,0))) <> 0 " ;
		SqlEntry = SqlEntry + " ORDER BY P_Payment_Entryline_360.C_Elementvalue_Id " ;

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
//            Entry.setP_Payment_Group_ID( P_Payment_Group_ID );
            Entry.setRegularWriting(true);

            //2023-08-03
            Entry.setAD_Org_ID( 0 );            
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
                    EntryLine.setAD_Org_ID(  0 );
                    EntryLine.setOrg_ID( rs.getInt("Org_ID"));
                    EntryLine.setP_Accounting_Entry_ID(NewEntry);
                    EntryLine.setC_Activity_ID(rs.getInt("C_Activity_ID"));
                    EntryLine.setC_SalesRegion_ID(rs.getInt("C_SalesRegion_ID"));
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
        P_Payment_Entryline_360 PaymentLine = null;
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
		                	  "From P_Payment_Entryline_360 " +
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

    

	private String getTrxName()
	{
		return m_trxName;
	}
    
	public Properties getCtx()
	{
		return m_ctx;
	}

	private static void writeDataSheetHeader(WritableSheet s ) 
		    throws WriteException
{
    /* Format the Font */
    WritableFont wf = new WritableFont(WritableFont.ARIAL, 
      10, WritableFont.BOLD);
    WritableCellFormat cf = new WritableCellFormat(wf);
    cf.setWrap(true);
    Colour c = Colour.GREY_25_PERCENT;
    cf.setBackground(c);
    cf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

    
    /* Creates Label and writes date to one cell of sheet*/
    Label l = new Label(0,0,"Nom de lot",cf);
    s.addCell(l);
    l = new Label(1,0,"Date de report",cf);
    s.addCell(l);
    l = new Label(2,0,"Type de document",cf);
    s.addCell(l);
    l = new Label(3,0,"N° de document",cf);
    s.addCell(l);
    l = new Label(4,0,"Type de compte",cf);
    s.addCell(l);
    l = new Label(5,0,"N° de compte",cf);
    s.addCell(l);
    l = new Label(6,0,"Nom du compte",cf);
    s.addCell(l);
    l = new Label(7,0,"Description",cf);
    s.addCell(l);
    l = new Label(8,0,"Code devise",cf);
    s.addCell(l);
    l = new Label(9,0,"Code de région fiscale",cf);
    s.addCell(l);
    l = new Label(10,0,"Montant",cf);
    s.addCell(l);
    l = new Label(11,0,"Montant ($)",cf);
    s.addCell(l);
    l = new Label(12,0,"Type compte contrôle",cf);
    s.addCell(l);
    l = new Label(13,0,"N° compte contrôle",cf);
    s.addCell(l);
    l = new Label(14,0,"Type report gén. contrepartie",cf);
    s.addCell(l);
    l = new Label(15,0,"Correction",cf);
    s.addCell(l);
    l = new Label(16,0,"Commentaire",cf);
    s.addCell(l);
    l = new Label(17,0,"Division Code",cf);
    s.addCell(l);
    l = new Label(18,0,"Centre de coût Groupe Code",cf);
    s.addCell(l);
    l = new Label(19,0,"Centre de coût Code",cf);
    s.addCell(l);
    l = new Label(20,0,"Catégorie Code",cf);
    s.addCell(l);
    l = new Label(21,0,"Segment de marché ",cf);
    s.addCell(l);
    l = new Label(22,0,"Grossiste Code",cf);
    s.addCell(l);
    l = new Label(23,0,"Division client Code",cf);
    s.addCell(l);
    l = new Label(24,0,"Bannière Code",cf);
    s.addCell(l);
    l = new Label(25,0,"NumberOfJournalRecords",cf);
    s.addCell(l);
    l = new Label(26,0,"Débit total",cf);
    s.addCell(l);
    l = new Label(27,0,"Crédit total",cf);
    s.addCell(l);
    l = new Label(28,0,"Solde",cf);
    s.addCell(l);
    l = new Label(29,0,"Solde total",cf);
    s.addCell(l);
	 
}

 private int writeDataSheet(WritableSheet s, P_Period Period) 
		    throws WriteException
{
	int cnt = 0;
    MTree_Base tree = new MTree_Base(Env.getCtx(), 1000032, null);


    WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
  	WritableCellFormat cf2 = new WritableCellFormat(wf2);
  	cf2.setWrap(true);
    WritableCellFormat nf1 = new WritableCellFormat(NumberFormats.FLOAT);
    WritableCellFormat nf2 = new WritableCellFormat(NumberFormats.INTEGER);
    WritableCellFormat df1 = new WritableCellFormat(DateFormats.DEFAULT);
	
    int recordCnt = 0;
    String sqlcnt = "SELECT Count(*) FROM P_Accounting_Entry "
    		+ " INNER JOIN P_Accounting_EntryLine on P_Accounting_EntryLine.P_Accounting_Entry_ID = P_Accounting_Entry.P_Accounting_Entry_ID "
    		+ " WHERE RegularWriting = 'Y' "
    		+ " AND P_Period_ID = " + Period.getP_Period_ID()
    		;
    PreparedStatement pstmpcnt = null;
    try
    {
        pstmpcnt = DB.prepareStatement(sqlcnt, null);
        ResultSet rscnt = pstmpcnt.executeQuery();
        if ( rscnt.next() )
        	recordCnt = rscnt.getInt(1);
    }
    catch (Exception e)
    {
       log.log (Level.SEVERE,"P_InOutTransfertGeneralLedger.Count", e);
    }

    		
    
    String sql = "SELECT P_Accounting_EntryLine.*, P_Accounting_Entry.AmtACCTDR as AmtACCTDRTotal, P_Accounting_Entry.AmtACCTCR as AmtACCTCRTotal"
    		+ " FROM P_Accounting_Entry "
    		+ " INNER JOIN P_Accounting_EntryLine on P_Accounting_EntryLine.P_Accounting_Entry_ID = P_Accounting_Entry.P_Accounting_Entry_ID "
    		+ " WHERE RegularWriting = 'Y' "
    		+ " AND P_Period_ID = " + Period.getP_Period_ID()
    		;
    
    		
    PreparedStatement pstmp = null;
    try
    {
        pstmp = DB.prepareStatement(sql, null);
        ResultSet rs = pstmp.executeQuery();
        
        while ( rs.next() )
        {
			cnt = cnt + 1;
        	//Nom de lot
            Label l0 = new Label(0,cnt,"PAIE" ,cf2);
		    s.addCell(l0);
		    
		    //Date de report
            Label l1 = new Label(1,cnt, Period.getPayDate().toString().substring(1,10) ,cf2);
		    s.addCell(l1);
		    
		    //Type de document
            Label l2 = new Label(2,cnt, "" ,cf2);
		    s.addCell(l2);
		    
		    //N° de document
            Label l3 = new Label(3,cnt, "PAY" + Period.getPayDate().toString().substring(1,10).replace("-", "") ,cf2);
		    s.addCell(l3);
		    
		    //Type de compte
		    Label l4 = new Label(4,cnt, "Compte du grand livre",cf2);
		    s.addCell(l4);
		    
		    //N° de compte
		    MElementValue ElementValue = new MElementValue( Env.getCtx(), rs.getInt("C_ElementValue_ID") , null); 
		    Label l5 = new Label(5,cnt, ElementValue.getValue() ,cf2);
		    s.addCell(l5);
		    //Nom du compte
		    Label l6 = new Label(6,cnt,ElementValue.getName(),cf2);
		    s.addCell(l6);
		    
		    //Description
		    Label l7 = new Label(7,cnt,"PAIE HEBDOMADAIRE",cf2);
		    s.addCell(l7);
		    
		    //Code devise
		    Label l8 = new Label(8,cnt,"",cf2);
		    s.addCell(l8);
		    //Code de région fiscale
		    
		    Label l9 = new Label(9,cnt,"" ,cf2);
		    s.addCell(l9);
		    //Montant
		    Number nI2 = new Number(10,cnt, Double.parseDouble( (rs.getBigDecimal("AmtACCTDR").subtract(rs.getBigDecimal("AmtACCTCR")).toString() )) ,nf1);
		    s.addCell(nI2);
		    //Montant ($)
		    Number nI3 = new Number(11,cnt, Double.parseDouble( (rs.getBigDecimal("AmtACCTDR").subtract(rs.getBigDecimal("AmtACCTCR")).toString() )) ,nf1);
		    s.addCell(nI3);
		    //Type compte contrôle
		    Label l12 = new Label(12,cnt,"Compte du grand livre",cf2);
		    s.addCell(l12);
		    //N° compte contrôle
		    Label l13 = new Label(13,cnt,"",cf2);
		    s.addCell(l13);

		    // Type report gén. contrepartie
		    Label l14 = new Label(14,cnt,"",cf2);
		    s.addCell(l14);
		    
		    // Correction
		    Label l15 = new Label(15,cnt,"FAUX",cf2);
		    s.addCell(l15);
		    
		    //Commentaire
            MSalesRegion SalesRegion = MSalesRegion.get(Env.getCtx(), rs.getInt("C_SalesRegion_ID"));
		    
		    Label l16 = new Label(16,cnt,"Lieu de travail : " + SalesRegion.getValue() + " " + SalesRegion.getName() ,cf2);
		    s.addCell(l16);

		    MActivity Activity = new MActivity( Env.getCtx(), rs.getInt( "C_Activity_ID" ), null);

		    Label l17 = new Label(17,cnt,"AKK",cf2);

		    if ( Activity != null && Activity.getValue360().startsWith("PROD"))
		    {
		    	MOrg Org = MOrg.get(Env.getCtx(), rs.getInt("Org_ID"));
			    l17 = new Label(17,cnt, Org.getValue(),cf2);
		    	
		    }

		    // Division Code
		    s.addCell(l17);
		    
		    
		    MTree_Node node = MTree_Node.get(tree , Activity.getC_Activity_ID() );
		    MActivity Grp = new MActivity( Env.getCtx(), node.getParent_ID(), null);
		    //Centre de coût Groupe Code
		    Label l18 = new Label(18,cnt, Grp.getValue(),cf2);
		    s.addCell(l18);
		    // Centre de coût Code
		    Label l19 = new Label(19,cnt, Activity.getValue360() ,cf2);
		    s.addCell(l19);
		    //Catégorie Code
		    Label l20 = new Label(20,cnt,"",cf2);
		    s.addCell(l20);
		    //Segment de marché 
		    Label l21 = new Label(21,cnt,"",cf2);
		    s.addCell(l21);
		    //Grossiste Code
		    Label l22 = new Label(22,cnt,"",cf2);
		    s.addCell(l22);
		    //Division client Code
		    Label l23 = new Label(23,cnt,"",cf2);
		    s.addCell(l23);
		    //Bannière Code
		    Label l24 = new Label(24,cnt,"",cf2);
		    s.addCell(l24);
		    
		    
		    
		    //NumberOfJournalRecords
		    Label l25 = new Label(25,cnt, String.valueOf( recordCnt ) ,cf2);
		    s.addCell(l25);
		    //Débit total
		    Number nI26 = new Number(26,cnt,Double.parseDouble( rs.getBigDecimal("AmtACCTDR").toString() ),cf2);
		    s.addCell(nI26);
		    //Crédit total
		    Number nI27 = new Number(27,cnt,Double.parseDouble( rs.getBigDecimal("AmtACCTCR").toString() ),cf2);
		    s.addCell(nI27);
		    //Solde
		    Number nI28 = new Number(28,cnt,Double.parseDouble( (rs.getBigDecimal("AmtACCTDRTotal") ).toString() ),cf2);
		    s.addCell(nI28);
		    //Solde total
		    Number nI29 = new Number(29,cnt,Double.parseDouble( (rs.getBigDecimal("AmtACCTDRTotal")).toString() ),cf2);
		    s.addCell(nI29);
		    
        }
    }
    catch (Exception e)
    {
        log.log (Level.SEVERE,"P_InOutTransfertGeneralLedger.ValidTotal", e);
    }
    return cnt;
}



    
	private void CreateExcelFile( P_Period Period  )
	{
	    
		int exported = 0;
	    try
	    {
     	  String Path = "C:\\Solstice\\Transferts\\GL360\\";
	    	if ( ! Path.endsWith("\\") )
	    		Path = Path + "\\";

	      String filename =  Path + "Journaux généraux - PAIE " + Period.getName() + ".xls";

	      WorkbookSettings ws = new WorkbookSettings();
	      ws.setLocale(new Locale("en", "EN"));
	      WritableWorkbook workbook = 
	      Workbook.createWorkbook(new File(filename), ws);
	      WritableSheet s = workbook.createSheet("Journaux généraux", 1);
	      // set la largeur des colonnes
	      s.setColumnView(0, 10);
	      s.setColumnView(1, 20);
	      s.setColumnView(2, 10);
	      s.setColumnView(3, 30);
	      s.setColumnView(4, 30);
	      s.setColumnView(5, 10);
	      s.setColumnView(6, 30);
	      s.setColumnView(7, 10);
	      s.setColumnView(8, 10);
	      s.setColumnView(9, 10);
	      s.setColumnView(10, 10);
	      s.setColumnView(11, 10);
	      s.setColumnView(12, 30);
	      s.setColumnView(13, 10);
	      s.setColumnView(14, 10);
	      s.setColumnView(15, 10);
	      s.setColumnView(16, 10);
	      s.setColumnView(17, 10);
	      s.setColumnView(18, 30);
	      s.setColumnView(19, 30);
	      s.setColumnView(20, 10);
	      s.setColumnView(21, 30);
	      s.setColumnView(22, 10);
	      s.setColumnView(23, 10);
	      s.setColumnView(24, 10);
	      s.setColumnView(25, 10);
	      s.setColumnView(26, 10);
	      s.setColumnView(27, 10);
	      s.setColumnView(28, 10);
	      s.setColumnView(29, 10);
	      s.setColumnView(30, 10);
	      s.setColumnView(31, 10);

		  writeDataSheetHeader(s );
	      exported = writeDataSheet(s, Period);

	      workbook.write();
	      workbook.close();      

	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	    catch (WriteException e)
	    {
	      e.printStackTrace();
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	}
	


}


