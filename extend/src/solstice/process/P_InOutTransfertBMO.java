/*
* Created on 2022-04
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.model.MCurrency;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.CLogger;
import solstice.model.P_BankAccount;
import solstice.model.P_BankAccountDoc;
import solstice.model.P_Employee;
import solstice.model.P_Employer;
import solstice.model.P_Financial_Institution;
import solstice.model.P_Payment;
import solstice.model.P_Payment_Distribution;
import solstice.model.P_Period;
import solstice.model.P_Language;
import org.compiere.util.TimeUtil;

/**
 * @author rejgar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutTransfertBMO
{
    private int Client_ID = 0;
    private int Org_ID = 0;
    private int User_ID = 0;
    private int Period_ID = 0;
    private int Frequency_ID = 0;
    private int Payment_Group_ID = 0;
    private int NoLigne = 0;
    private int NbreTotal = 0;
    private String NoClient = "";
    private String NoTrans = "";
    private int Banque;
    private BigDecimal MntTotal;
    private String Path = "";
    private String FileName = "";
    private ResultSet rs = null;
    private String NumberTransfert;
    
    private boolean IsRework = false;
      
    // Déclaration des objects
    
    private MClient Client = null;
    private P_BankAccount BankAccount = null;
    
    private Properties m_ctx;
    private String DateDisponibility = "";
    
    private CLogger			log = CLogger.getCLogger (getClass());

    public P_InOutTransfertBMO( Properties ctx)
    {
        m_ctx = ctx;
    }
    

    public String CreateFile(ResultSet _rs, StringBuffer CreateFile, int _Period_ID, String _DateDisponibility)
    {
        String trxName = null; //	Trx.createTrxName();

        rs = _rs;
        Period_ID = _Period_ID;
        DateDisponibility = _DateDisponibility;
        Client = MClient.get(getCtx(), Env.getAD_Client_ID(getCtx()));
        CreateHeader( CreateFile, trxName );
        CreateSecondLine(CreateFile, trxName);
        CreateDetail( CreateFile, trxName);
        CreateEndLine( CreateFile);
        WriteToFile(CreateFile, trxName);

        return NumberTransfert;
//        return "Nombre de transaction traité: " + NbreTotal + "<BR>  ** Montant de la transaction: " + MntTotal;

    }

    public void CreateHeader(StringBuffer NewFile, String trxName)
    {
    	//log.debug("CreateFileTransfertBMO.CreateHeader - Début");
    	// Création du premier enregistrement
    	// Create the first line
    	
      String sql = "select b.P_BankAccount_ID "  +
                   "From P_Financial_Institution a, P_BankAccount b " +
               	   "where a.P_Financial_Institution_ID = b.P_Financial_Institution_ID" +
  	               "  and a.IsActive = 'Y' and b.IsActive = 'Y'" +
  	               "  and a.Value = '001'";
      //log.error(sql);
      String Protocol_rsn = "";
      PreparedStatement psacc = null;
      try
      { 
        psacc = DB.prepareStatement(sql, null);
        ResultSet rsacc = psacc.executeQuery();
        if (rsacc.next())
        {
          BankAccount = P_BankAccount.get(getCtx(), rsacc.getInt("P_BankAccount_ID"), trxName);
  //ICI        Protocol_rsn = BankAccount.getProtocolRsn();
          rsacc.close();
          psacc.close();
        }
      }
      catch (Exception e)
  	{
      	log.log(Level.WARNING, "CreateFileTransfertBMO.CreateHeader", e);
  	}
      //log.debug("CreateFileTransfertBMO.CreateHeader" + Protocol_rsn);
/*      NewFile.append(Protocol_rsn);
    	//
    	// 152 Correspond à la longeur de la ligne du fichier texte
    	//
    	InsertBlank(NewFile, 80);
    	NewFile.append("\n");
*/    	
    	//log.debug("CreateFileTransfertBMO.CreateHeader - Fin");
    }  // CreateHeader
      
    public void InsertBlank(StringBuffer Bl, int Pos)
    {
    	//log.debug("CreateFileTransfertBMO.InsertBlank - Début");
    	for (int i = 0; i < Pos; i++)
    	{
    		Bl.append(" ");
    	}
    	//log.debug("CreateFileTransfertBMO.InsertBlank - Fin");
    }

    public void ZeroFill(StringBuffer Bl, int Pos)
    {
    	//log.debug("CreateFileTransfertBMO.ZéroFill - Début");
    	for (int i = 0; i < Pos; i++)
    	{
    		Bl.append("0");
    	}
    	//log.debug("CreateFileTransfertBMO.ZéroFill - Fin");
    }

    
    public void CreateSecondLine(StringBuffer NewFile, String trxName)
    {
    	
    	//log.debug("CreateFileTransfertBMO.CréateSecondLine - Start");
    	P_BankAccountDoc BankAccountDoc = null;
    	String sql = "Select P_BankAccountDoc_id from P_BankAccountDoc " +
    		         " Where P_BankAccount_ID = " +  BankAccount.getP_BankAccount_ID() +
  				 "   and PaymentRule = 'D'";

    	PreparedStatement psdoc = null;
      try
      { 
        psdoc = DB.prepareStatement(sql, null);
        ResultSet rsdoc = psdoc.executeQuery();
        if (rsdoc.next())
        {
          BankAccountDoc = P_BankAccountDoc.get(getCtx(), rsdoc.getInt("P_BankAccountDoc_ID"), trxName);
        }
        rsdoc.close();
        psdoc.close();
      }
      catch (Exception e)
  	{
      	log.log(Level.WARNING, "CreateFileTransfertBMO.CreateSecondLine", e);
  	}
    	
  	NumberTransfert = String.valueOf(BankAccountDoc.getDepositNext());
  	BankAccountDoc.setDepositNext(BankAccountDoc.getDepositNext() + 1);
  	BankAccountDoc.save();
  	MCurrency Currency = MCurrency.get( getCtx(), BankAccount.getC_Currency_ID());
    	
  	NoLigne++;
  	//og.debug("CreateSecondLine" + Client.getName());
  	//  Creation de la première lieng d'entête
  	//  Create the first line of the title
  	String NoEnr1 = String.valueOf( NoLigne);
//  	String NoEnr =  "000000".substring(0, 6 - NoEnr1.length()) + String.valueOf( NoLigne );
	String NoEnr =  ""; 
  	
	//ITEM ENR_1      = ("A" + PAY_PAP_ORGANISME_C[1:10] + T_CREATION + "0" + T_DATE  + &
    //        "00110" + REMPLISSAGE[1:54])
	// AALKRIZ9610 094002210900110

  	String TypeEnr = "A";                              
  	String CodeOper = BankAccount.getProtocolRsn(); //"ALKRIZ9610";                           
  		
  	// Inséré le numéro de transfert
  	NoTrans = "0000".substring(0, 4 - NumberTransfert.length()) + NumberTransfert;                // Numéro de transfert
  	// Calcul de la date julienne
  	// Calc julian date
 // 	Calendar Year = Calendar.getInstance();
 // 	String DateTrans = String.valueOf(Year.get(Calendar.YEAR)) + "000".substring(0, 3 - String.valueOf(Year.get(Calendar.DAY_OF_YEAR)).length()) + String.valueOf(Year.get(Calendar.DAY_OF_YEAR));

  	Calendar Day = Calendar.getInstance();
  	int JulianDay = Day.get(Calendar.DAY_OF_YEAR);
	String Year = String.valueOf(Day.get(Calendar.YEAR));
	String DateJulien = "0" + Year.substring(2, 4) + "000".substring(0, 3 - String.valueOf(JulianDay).length()) + String.valueOf(JulianDay);

		
  	String Devise = Currency.getISO_Code(); 
  	String TypeEntre = "1";             
  	NewFile.append(NoEnr + TypeEnr + CodeOper );
  	NewFile.append(NoTrans +  DateJulien + "00110" );
  	InsertBlank(NewFile, 54);
  	NewFile.append("\n");
  	
 
  	
  	//log.debug("CreateFileTransfertBMO.CréateSecondLine - End");
    }   //     fin CreateSecondLine
    
    public void CreateDetail (StringBuffer NewFile, String trxName)
    {
    	//log.debug("CreateFileTransfertBMO.CreateDetail - Start");
    	try
  	{
    		// On va chercher tout les paiements
    		//ResultSet rs = pstmp.executeQuery ();

       	    P_Payment_Distribution Payment_dist = null;
    		P_Financial_Institution Financial_Institution = null;
    		P_Payment Payment = null;
    	    P_Employer Employer = null;
 
    		log.log(Level.WARNING, "Movefirst");
    		rs.beforeFirst();
    		log.log(Level.WARNING, "Après Movefirst");
    		P_Employee Employee = null;
    		int NoPaiment = 0;
    		BigDecimal Cent = new BigDecimal(100);
  	        MntTotal = new BigDecimal(0);
  	        while ( rs.next())
    		{
    			Payment = P_Payment.get( getCtx(), rs.getInt("P_Payment_ID"), trxName);
    			Payment.UpdatePaymentDistribution( trxName);
      			Payment.setIsTransfer(true);
      			Payment.setIsTransferredToBank(true);
    			Payment.setP_BankAccount_ID( BankAccount.getP_BankAccount_ID() );
      			Payment.setDateTransfer( new Timestamp(  Calendar.getInstance().getTimeInMillis()) );
      			
			  	if (DateDisponibility.length() != 0)
			  	{
			  	    int payTsYear = (new Integer(DateDisponibility.substring(0, 4))).intValue();
			  	    int payTsMonth = (new Integer(DateDisponibility.substring(5, 7))).intValue() - 1;
			  	    int payTsDay = (new Integer(DateDisponibility.substring(8, 10))).intValue();
    			  	Calendar PayDay = Calendar.getInstance();
    			  	PayDay.set(payTsYear, payTsMonth, payTsDay);
	      			Payment.setPayDate( new Timestamp(PayDay.getTimeInMillis() ) );
			  	}

			  	Payment.setTransfertNumber( NumberTransfert );
      			Payment.save();

    			NoPaiment++;
    			log.log(Level.INFO, "UpdatePaymentDistribution " + NoPaiment + "  " + Payment.getP_Payment_ID());
    			Employee = P_Employee.get(getCtx(), Payment.getP_Employee_ID(), trxName);
    			Employer = P_Employer.get(getCtx(), Payment.getP_Employer_ID(), trxName);
//    			log.debug("While rs.next() " + Payment.getP_Payment_ID());
    			//BigDecimal MntNet = Payment.getNetPay();
    			                       
    			String SqlDist = "Select P_Payment_Distribution_ID " +
  			                     " From P_Payment_Distribution " +
  			                     "where IsActive = 'Y' and P_Payment_ID=" + Payment.getP_Payment_ID();
    			//log.debug(SqlDist);
    			PreparedStatement pstmp_dist = null;
    			try
  			    {
    				pstmp_dist = DB.prepareStatement(SqlDist, null);
  			    }
    			catch (Exception e)
  			    {
    				log.log(Level.SEVERE, "CreateFileTransfertBMO.UpdatePaymetDistribution Employee_Dist", e);
  			    }
    			ResultSet rs_Dist = pstmp_dist.executeQuery();
    			int NoDist = 0;
    			
    			while (rs_Dist.next())
    			{
    				Payment_dist = P_Payment_Distribution.get( getCtx(), rs_Dist.getInt("P_Payment_Distribution_ID" ), trxName);
    				Financial_Institution = P_Financial_Institution.get(getCtx(), Payment_dist.getP_Financial_Institution_ID(), trxName);
    				NbreTotal++;
    				NoDist++;
    				NoLigne++;
    				
 /*   				
    			 	AALKRIZ9610094002210900110                                                      
    			  	XC200022111ALIMENTS KRISPYALIMENTS KRISPY KERNELS INC.  0001212551024165    
    			  	
    			    ITEM ENR_1 = "XC200" + "0" +  T_DATE + T_NOM_ABREGE + PAY_UNP_DESC + PAY_PAP_USAGER_CPT_C OF PAYPARMP[1:9] &
    			            + PAY_PAP_EMETTEUR_C+ "  " + "   
*/
    			  	//RECORD XC200
    				if ( NoLigne++ == 2 )
    				{
    					
        			  	Calendar LastDay = TimeUtil.getCalendar( Payment.getPayDate());
       			  	
      				    int JulianDay = LastDay.get(Calendar.DAY_OF_YEAR);
      				    String Year = String.valueOf(LastDay.get(Calendar.YEAR));
         				String DateJulien = "0" + Year.substring(2, 4) + "000".substring(0, 3 - String.valueOf(JulianDay).length()) + String.valueOf(JulianDay);
    				

        				NewFile.append("XC200"  + DateJulien + "ALIMENTS KRISPYALIMENTS KRISPY KERNELS INC.  000" + BankAccount.getBranchNumber()  + BankAccount.getTransfertNumber() );
        			  	InsertBlank(NewFile, 8);

        			  	NewFile.append("\n");
    				}
    				
    				
    			  	String TypeEnr = "C";                                                 // Type d'enregistrement
    			  	String CodeOper = "000";                                              // Code d'operation

    			    String Mntnet = String.valueOf(Payment_dist.getAmount().multiply(Cent).setScale(0));
    			    MntTotal = MntTotal.add(Payment_dist.getAmount());
    			  	String MontantNet = "0000000000".substring(0, 10 - Mntnet.trim().length()) + Mntnet;          // Montant Net

    			  	String NoBanque = "";
    			  	if (Financial_Institution.getValue().trim().length() > 4)
    			  		NoBanque = Financial_Institution.getValue().substring(0, 4);
    			  	else
    			  	   NoBanque = "0000".substring(0, 4 - Financial_Institution.getValue().trim().length()) + Financial_Institution.getValue().trim();                          // Numero de la banque
    			  	String Transit = "";
    			  	if (Payment_dist.getTransit().trim().length() > 5)
    			  		Transit = Payment_dist.getTransit().trim().substring(0, 5);
    			  	else
    			  		Transit = "00000".substring(0, 5 - Payment_dist.getTransit().trim().length()) + Payment_dist.getTransit().trim();
    			    String NoCompte = "";                                 // Numero de compte de l'employe
    			    if (Payment_dist.getFolio().trim().length() > 18)
    			    	NoCompte = Payment_dist.getFolio().trim().substring(0, 18);
    			    else
    			    	NoCompte = Payment_dist.getFolio().trim();
    			    
    			    NoCompte = NoCompte.replace("-", "");
    			  	
      				NewFile.append( TypeEnr + MontantNet  ); 

  		     		NewFile.append( NoBanque + Transit + NoCompte);
  			    	InsertBlank(NewFile, 12 - NoCompte.length());
  			    	
  	   				String Nom = "";
      				String Prenom = "";
      				String NomPrenom = "";
      				if (Employee.getSurname().trim().length() > 30)
    				    Nom = Employee.getSurname().trim().substring(0, 30);                                         // Nom  de l'employe
    			  	else
    			  		Nom = Employee.getSurname().trim();
      				if (Employee.getFirstName().trim().length() > 30)
    				    Prenom = Employee.getFirstName().trim().substring(0, 30);                                // prénom de l'employe
    			  	else
    			  		Prenom = Employee.getFirstName().trim();
    				NomPrenom = Nom + " " + Prenom;
    				
    				NomPrenom = convertSansAccent( NomPrenom );
    				
    				//2023-06-20
    				if ( NomPrenom.length() > 29)
    					NomPrenom = NomPrenom.substring(0,28);

    				NewFile.append( NomPrenom);
     				InsertBlank(NewFile, 29 - NomPrenom.length());

     				String NoEmploye = "";
    			  	if (Employee.getValue().trim().length() > 19)                                // Numero d'employe
   			  	  	    NoEmploye = Employee.getValue().trim().substring(0, 19);              
    			  	else	
    			  	    NoEmploye = Employee.getValue().trim();              

    				NewFile.append( NoEmploye);
    				
    				if (NoEmploye.length() == 4 )
    					InsertBlank(NewFile, 1);
  				    InsertBlank(NewFile, 14);

     		  	    NewFile.append("\n");
    			}
    			rs_Dist.close();
    			pstmp_dist.close();
    		}
    		rs.close();
    		
    		
      }
    	catch (Exception e)
  	{
    		log.log(Level.SEVERE, "CreateFileTransfertBMO.CreateDetail", e);
  	}
    	//log.debug("CreateFileTransfertBMO.CreateDetail - End");
    }
    
    public void CreateEndLine (StringBuffer NewFile)
    {
    	//log.debug("CreateFileTransfertBMO.CreateEndLine - Start");
    	NoLigne++;
//    	String NoEnr =  "000000".substring(0, 6 - String.valueOf(NoLigne).length()) + String.valueOf( NoLigne );

	  	String NbreOperation = "00000000".substring(0, 8 - String.valueOf(NbreTotal).length()) + String.valueOf(NbreTotal);
	  	BigDecimal Cent = new BigDecimal(100);
	  	String MntString = String.valueOf(MntTotal.multiply(Cent).setScale(0));
	  	String MontantTotal = "00000000000000".substring(0, 14 - MntString.length()) + MntString;


//		ITEM T_ENR2 = "YC" +  T_QTE_CHQ_A + T_MNT_CHQ_A

	  	String TypeEnr = "YC";                                                 // Type d'enregistrement
	  	NewFile.append( TypeEnr + NbreOperation + MontantTotal);
	  	InsertBlank(NewFile, 56);
	  	NewFile.append("\n");
  	
		
		
	  	//		ITEM T_ENRG_FIN = ("Z" + "0000000000000000000" + T_MNT_CHQ_A + T_QTE_CHQ_A[4:5]  
			
	  	TypeEnr = "Z";  // Type d'enregistrement
	  	NewFile.append( TypeEnr + "0000000000000000000" + MontantTotal + NbreOperation.substring(3,8)  );
	  	InsertBlank(NewFile, 41);
//	  	NewFile.append("\n");
	  	//log.debug("CreateFileTransfertBMO.CreateEndLine - End");
    }
    public void WriteToFile(StringBuffer NewFile, String trxName)
    {
    	String Path = PgiUtil.getSolsticeParameter(Env.getCtx(), "TransfertPathDeposit");

    	log.log(Level.WARNING, "CreateFileTransfertBMO.WriteToFile - Path" + Path);
    	//log.debug("CreateFileTransfertBMO.WriteToFile - Start");
    	P_Period Period = P_Period.get(getCtx(), Period_ID, trxName);
    	FileName = Period.getName();
    	File aFile = null;
  	    aFile = new File(Path + FileName + ".dat");
     	//aFile.createNewFile();
  	    //log.debug("CreateFileTransfert.WriteToFile - StringBufferCapacity " + NewFile.capacity());


//m.m  	    FileOutputStream out; // declare a file output object
        
        
        
        PrintStream p; // declare a print stream object
        try
        {
/*
        	// Create a new file output stream
              // connected to "myfile.txt"
              out = new FileOutputStream(aFile);

              // Connect print stream to the output stream
              p = new PrintStream( out );

              //StringBuffer sb = new StringBuffer();
              //sb.insert(0, "lalala");
              //sb.append("This is written to a file333333");
              p.println (NewFile.toString());
              //p.println ("asdasd");
              System.out.println(NewFile.toString());
              p.close();
 */             
        	 Writer out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(aFile), StandardCharsets.UTF_8));
			 out.append( NewFile.toString() );
			 out.flush();
			 out.close();
              
        }
        catch (Exception e)
        {
              System.out.println ("Error writing to file :" + e.toString());
        }
        //log.debug("CreateFileTransfertBMO.WriteToFile - End");
    }
    
	public Properties getCtx()
	{
		return m_ctx;
	}
	
	public static String convertSansAccent( String s )
	{
	    String output = "";

	    if ( s == null )
	    	return "";

	    for (int i = 0; i < s.length(); i++) {
	      output += removeAccent(s.charAt(i));
	    }
		return output;
	}

	
    private static String removeAccent(char c) 
    {
        if (c == 'Ä') return "A";
        if (c == 'ä') return "a";
        if (c == 'Ã') return "A";
        if (c == 'ã') return "a";
        if (c == 'Å') return "A";
        if (c == 'å') return "a";
        if (c == 'Æ') return "A";
        if (c == 'æ') return "a";
        if (c == 'Ç') return "C";
        if (c == 'ç') return "c";
        if (c == 'Ð') return "E";
        if (c == 'ð') return "e";
        if (c == 'É') return "E";
        if (c == 'é') return "e";
        if (c == 'È') return "E";
        if (c == 'è') return "e";
        if (c == 'Ê') return "E";
        if (c == 'ê') return "e";
        if (c == 'Ë') return "E";
        if (c == 'ë') return "e";
        if (c == 'Í') return "I";
        if (c == 'í') return "i";
        if (c == 'Ì') return "I";
        if (c == 'ì') return "i";
        if (c == 'Î') return "I";
        if (c == 'î') return "i";
        if (c == 'Ï') return "I";
        if (c == 'ï') return "i";
        if (c == 'Ñ') return "N";
        if (c == 'ñ') return "n";
        if (c == 'Ó') return "O";
        if (c == 'ó') return "o";
        if (c == 'Ò') return "O";
        if (c == 'ò') return "o";
        if (c == 'Ô') return "O";
        if (c == 'ô') return "o";
        if (c == 'Ö') return "O";
        if (c == 'ö') return "o";

        return "" + c;

    }
}
