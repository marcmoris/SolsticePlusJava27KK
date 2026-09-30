/*
 * Created on Jun 23, 2005
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
public class P_InOutTransfertRBC
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

    public P_InOutTransfertRBC( Properties ctx)
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
    	//log.debug("CreateFileTransfertRBC.CreateHeader - Début");
    	// Création du premier enregistrement
    	// Create the first line
    	
      String sql = "select b.P_BankAccount_ID "  +
                   "From P_Financial_Institution a, P_BankAccount b " +
               	   "where a.AD_Org_ID = b.AD_Org_id and a.AD_Client_ID = b.AD_Client_id" +
  	               "  and a.P_Financial_Institution_ID = b.P_Financial_Institution_ID" +
  	               "  and a.IsActive = 'Y' and b.IsActive = 'Y'" +
  	               "  and a.Value = '003'";
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
          Protocol_rsn = BankAccount.getProtocolRsn();
          rsacc.close();
          psacc.close();
        }
      }
      catch (SQLException e)
  	{
      	log.log(Level.WARNING, "CreateFileTransfertRBC.CreateHeader", e);
  	}
      //log.debug("CreateFileTransfertRBC.CreateHeader" + Protocol_rsn);
      NewFile.append(Protocol_rsn);
    	//
    	// 152 Correspond à la longeur de la ligne du fichier texte
    	//
    	InsertBlank(NewFile, 152 - Protocol_rsn.trim().length());
    	NewFile.append("\n");
    	//log.debug("CreateFileTransfertRBC.CreateHeader - Fin");
    }  // CreateHeader
      
    public void InsertBlank(StringBuffer Bl, int Pos)
    {
    	//log.debug("CreateFileTransfertRBC.InsertBlank - Début");
    	for (int i = 0; i < Pos; i++)
    	{
    		Bl.append(" ");
    	}
    	//log.debug("CreateFileTransfertRBC.InsertBlank - Fin");
    }

    public void ZeroFill(StringBuffer Bl, int Pos)
    {
    	//log.debug("CreateFileTransfertRBC.ZéroFill - Début");
    	for (int i = 0; i < Pos; i++)
    	{
    		Bl.append("0");
    	}
    	//log.debug("CreateFileTransfertRBC.ZéroFill - Fin");
    }
    
    public void CreateSecondLine(StringBuffer NewFile, String trxName)
    {
    	//log.debug("CreateFileTransfertRBC.CréateSecondLine - Start");
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
      catch (SQLException e)
  	{
      	log.log(Level.WARNING, "CreateFileTransfertRBC.CreateSecondLine", e);
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
  	String NoEnr =  "000000".substring(0, 6 - NoEnr1.length()) + String.valueOf( NoLigne ); 
  	String TypeEnr = "A";                              
  	String CodeOper = "HDR";                           
  	// Inséré le numero de client 5194710000
  	NoClient = String.valueOf(BankAccount.getTransfertNumber());
  	// Inséré le Nom de la compagnie sur 30 de long
  	String NomClient = "";
  	if (Client.getName().trim().length() > 30)
  		NomClient = Client.getName().substring(0, 30);
  	else
  		NomClient = Client.getName().trim();
  	// Inséré le numéro de transfert
  	NoTrans = "0000".substring(0, 4 - NumberTransfert.length()) + NumberTransfert;                // Numéro de transfert
  	// Calcul de la date julienne
  	// Calc julian date
  	Calendar Year = Calendar.getInstance();
  	String DateTrans = String.valueOf(Year.get(Calendar.YEAR)) + "000".substring(0, 3 - String.valueOf(Year.get(Calendar.DAY_OF_YEAR)).length()) + String.valueOf(Year.get(Calendar.DAY_OF_YEAR));
  	String Devise = Currency.getISO_Code(); 
  	String TypeEntre = "1";             
  	NewFile.append(NoEnr + TypeEnr + CodeOper + NoClient + NomClient);
  	InsertBlank(NewFile, 30 - NomClient.length());
  	NewFile.append(NoTrans + DateTrans + Devise + TypeEntre);
  	InsertBlank(NewFile, 15);
  	InsertBlank(NewFile, 6);
  	InsertBlank(NewFile, 8);
  	InsertBlank(NewFile, 9);
  	InsertBlank(NewFile, 46);
  	InsertBlank(NewFile, 2);
  	NewFile.append("N");
  	NewFile.append("\n");
  	//log.debug("CreateFileTransfertRBC.CréateSecondLine - End");
    }   //     fin CreateSecondLine
    
    public void CreateDetail (StringBuffer NewFile, String trxName)
    {
    	//log.debug("CreateFileTransfertRBC.CreateDetail - Start");
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
  	        log.log(Level.WARNING, "Avant rs.next()");
  	        while ( rs.next())
    		{
  		        log.log(Level.WARNING, "Après rs.next()");
    			Payment = P_Payment.get( getCtx(), rs.getInt("P_Payment_ID"), trxName);
    			Payment.UpdatePaymentDistribution( trxName);
      			Payment.setIsTransfer(true);
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
    			log.log(Level.WARNING, "UpdatePaymentDistribution " + NoPaiment + "  " + Payment.getP_Payment_ID());
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
    				log.log(Level.SEVERE, "CreateFileTransfertRBC.UpdatePaymetDistribution Employee_Dist", e);
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
    				String NoEnr =  "000000".substring(0, 6 - String.valueOf(NoLigne).length()) + String.valueOf( NoLigne ); 
    			  	String TypeEnr = "C";                                                 // Type d'enregistrement
    			  	String CodeOper = "200";                                              // Code d'operation
    			  	String NoClient = String.valueOf(BankAccount.getTransfertNumber());   // Numero client
    			  	String Remplissage = " ";                                             // Remplissage de 1 blanc
    			  	String NoEmploye = "";
    			  	if (Employee.getValue().trim().length() > 19)                                // Numero d'employe
   			  	  	    NoEmploye = Employee.getValue().trim().substring(0, 19);              
    			  	else	
    			  	    NoEmploye = Employee.getValue().trim();              
    			  	String NoPayment = "00".substring(0, 2 - String.valueOf(NoDist).length()) + String.valueOf(NoDist);                                // Numero du payment
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
    			    String Remplissage2 = " ";                                                           // Remplissage de 1 blanc
    			    String Mntnet = String.valueOf(Payment_dist.getAmount().multiply(Cent).setScale(0));
    			    MntTotal = MntTotal.add(Payment_dist.getAmount());
    			  	String MontantNet = "0000000000".substring(0, 10 - Mntnet.trim().length()) + Mntnet;          // Montant Net
    			    //log.debug("Montant Net : " + Mntnet + " Montant : " + MntTotal);
    			  	/*
    			    int tsYear = Payment.getPayDate().getYear() + 1900;
    			    int tsMonth = Payment.getPayDate().getMonth();
    			    int tsDay = Payment.getPayDate().getDate();
    			    Calendar LastDay = Calendar.getInstance();
  				    LastDay.set(tsYear, tsMonth, tsDay);
  				    */
    			  	Calendar LastDay = TimeUtil.getCalendar( Payment.getPayDate());
  				    int JulianDay = LastDay.get(Calendar.DAY_OF_YEAR);
  				    String DateJulien = String.valueOf(LastDay.get(Calendar.YEAR) + "000".substring(0, 3 - String.valueOf(JulianDay).length()) + String.valueOf(JulianDay));
      				String Nom = "";
      				String Prenom = "";
      				String NomPrenom = "";
      				if (Employee.getName().trim().length() > 30)
    				    Nom = Employee.getName().trim().substring(0, 30);                                         // Nom  de l'employe
    			  	else
    			  		Nom = Employee.getName().trim();
      				if (Employee.getFirstName().trim().length() > 30)
    				    Prenom = Employee.getFirstName().trim().substring(0, 30);                                // prénom de l'employe
    			  	else
    			  		Prenom = Employee.getFirstName().trim();
    				NomPrenom = Nom + " " + Prenom;
    				String Langue = "";
    				P_Language Language = P_Language.get( Env.getCtx(), Employee.getP_Language_ID(), trxName );
    				
  				    if (Language.getValue().compareTo("fr_CA") == 0)
  				  	    Langue = "F";                                  // Langue
  				    else
  				  	    Langue = "A";                                  // Langue
  				    String Remplissage3 = " ";
  			  	    String NomAbrCie = "";                                // Nom abrégé de la compagnie
  				    if (Employer.getName().trim().length() > 15)
  					    NomAbrCie = Employer.getName().trim().substring(0, 15);
  				    else
  					    NomAbrCie = Employer.getName().trim();
  				    String Devise = "CAD";
  				    String Pays = "CAN";
  				
      				NewFile.append(NoEnr + TypeEnr + CodeOper + NoClient + Remplissage + NoEmploye); 
  	    			InsertBlank(NewFile, 19 - NoEmploye.length());
  		     		NewFile.append(NoPayment + NoBanque + Transit + NoCompte);
  			    	InsertBlank(NewFile, 18 - NoCompte.length());
     				NewFile.append(Remplissage2 + MontantNet);
  	   		    	ZeroFill(NewFile, 6);
  		     		NewFile.append(DateJulien + NomPrenom);
     				InsertBlank(NewFile, 30 - NomPrenom.length());
  	    			NewFile.append(Langue + Remplissage3 + NomAbrCie);
  		    		InsertBlank(NewFile, 15 - NomAbrCie.length());
  			    	NewFile.append(Devise + " " + Pays);
  				    InsertBlank(NewFile, 4);
  				    NewFile.append("N");
     		  	    NewFile.append("\n");
    			}
    			rs_Dist.close();
    			pstmp_dist.close();
    		}
    		rs.close();
      }
    	catch (SQLException e)
  	{
    		log.log(Level.SEVERE, "CreateFileTransfertRBC.CreateDetail", e);
  	}
    	//log.debug("CreateFileTransfertRBC.CreateDetail - End");
    }
    
    public void CreateEndLine (StringBuffer NewFile)
    {
    	//log.debug("CreateFileTransfertRBC.CreateEndLine - Start");
    	NoLigne++;
    	String NoEnr =  "000000".substring(0, 6 - String.valueOf(NoLigne).length()) + String.valueOf( NoLigne ); 
  	String TypeEnr = "Z";                                                 // Type d'enregistrement
  	String CodeOper = "TRL";                                              // Code d'operation
  	String NbreOperation = "000000".substring(0, 6 - String.valueOf(NbreTotal).length()) + String.valueOf(NbreTotal);
  	BigDecimal Cent = new BigDecimal(100);
  	String MntString = String.valueOf(MntTotal.multiply(Cent).setScale(0));
  	String MontantTotal = "00000000000000".substring(0, 14 - MntString.length()) + MntString;
  	//log.debug("Montant : " + MntString + "  " + MontantTotal + "  " + MntTotal);
  	NewFile.append(NoEnr + TypeEnr + CodeOper + NoClient + NbreOperation + MontantTotal);
  	ZeroFill(NewFile, 6);
  	ZeroFill(NewFile, 14);
  	ZeroFill(NewFile, 2);
  	ZeroFill(NewFile, 6);
  	InsertBlank(NewFile, 12);
  	InsertBlank(NewFile, 6);
  	InsertBlank(NewFile, 63);
  	InsertBlank(NewFile, 2);
  	InsertBlank(NewFile, 1);
  	NewFile.append("\n");
  	//log.debug("CreateFileTransfertRBC.CreateEndLine - End");
    }
    public void WriteToFile(StringBuffer NewFile, String trxName)
    {
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
        log.log(Level.WARNING, "CreateFileTransfertRBC.WriteToFile - Path" + Path);
    	//log.debug("CreateFileTransfertRBC.WriteToFile - Start");
    	P_Period Period = P_Period.get(getCtx(), Period_ID, trxName);
    	FileName = Period.getName();
    	File aFile = null;
  	    aFile = new File(Path + FileName + ".dat");
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
              p.println (NewFile.toString());
              //p.println ("asdasd");
              System.out.println(NewFile.toString());
              p.close();
        }
        catch (Exception e)
        {
              System.out.println ("Error writing to file :" + e.toString());
        }
        //log.debug("CreateFileTransfertRBC.WriteToFile - End");
    }
    
	public Properties getCtx()
	{
		return m_ctx;
	}
}
