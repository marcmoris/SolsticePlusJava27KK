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
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.model.MCurrency;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.CLogger;
import org.compiere.util.TimeUtil;

import solstice.model.P_Bank;
import solstice.model.P_BankAccount;
import solstice.model.P_BankAccountDoc;
import solstice.model.P_Employee;
import solstice.model.P_Employer;
import solstice.model.P_Financial_Institution;
import solstice.model.P_Language;
import solstice.model.P_Payment;
import solstice.model.P_Period;

import java.sql.Timestamp;

/**
 * @author rejgar01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_InOutTransfertBNC {
    private int Client_ID = 0;
    private int Org_ID = 0;
    private int Period_ID = 0;
    private int User_ID = 0;
    private int Frequency_ID = 0;
    private int Payment_Group_ID = 0;
    private int NoLigne = 0;
    private int NbreTotal = 0;
    private String NoUsager= "";
    private String NoTrans= "";
    private int Banque = 0;
    private BigDecimal MntTotal;
    private String Path = "";
    private String FileName = "";
    private String DateDisponibility = "";
    private ResultSet rs = null;
    
    
    private boolean IsRework = false;
      
    // Déclaration des objects
    
    private MClient Client = null;
    private P_BankAccount BankAccount = null;   

  	private String NumberTransfert;
  	private int m_NumberTransfert = 0;

    private Properties m_ctx;
    private P_Bank Bank;
    
	private static CLogger		log = CLogger.getCLogger (P_InOutTransfertCP.class);

    public P_InOutTransfertBNC( Properties ctx, int numberTransfert, int P_Bank_ID)
    {
        m_ctx = ctx;
        m_NumberTransfert = numberTransfert;
        Bank = P_Bank.get(ctx, P_Bank_ID, null);
    }
    
    public String CreateFile(ResultSet _rs, StringBuffer CreateFile, int _Period_ID, String _DateDisponibility)
    {
        String trxName = null; //	Trx.createTrxName();

        Client = MClient.get(getCtx(), Env.getAD_Client_ID(getCtx()));
        rs = _rs;
        Period_ID = _Period_ID;
        DateDisponibility = _DateDisponibility;
      	P_BankAccount BankAccount = null;
      	CreateLineA(CreateFile, trxName);
        CreateDetail( CreateFile, trxName);
        CreateEndLine( CreateFile);
        WriteToFile(CreateFile, trxName);

        return NumberTransfert;
        //return "Nombre de transaction traité: " + NbreTotal + "<BR>  ** Montant de la transaction: " + MntTotal;
    }

    public void InsertBlank(StringBuffer Bl, int Pos)
    {
    	//log.debug("CreateFileTransfertBNC.Blank - Début");
    	for (int i = 0; i < Pos; i++)
    	{
    		Bl.append(" ");
    	}
    	//log.debug("CreateFileTransfertBNC.Blank - Fin");
    }

    public void ZeroFill(StringBuffer Bl, int Pos)
    {
    	//log.debug("CreateFileTransfertBNC.ZéroFill - Début");
    	for (int i = 0; i < Pos; i++)
    	{
    		Bl.append("0");
    	}
    	//log.debug("CreateFileTransfertBNC.ZéroFill - Fin");
    }
    
    public void CreateLineA(StringBuffer NewFile, String trxName)
    {
        /*  Enregistrement en-tête de type A.

         *  Cet enregistrement sert à identifier le fichier et à indiquer les paramètres.
         *  Il constitue le premier enregistrement logique de chaque fichier
         *  et ne doit apparaître qu'une fois par fichier.
         *
         *  No zone  Position  Longueur  Contenue    Description
         *  -------  --------  --------  --------    -------------------------
         *     01        1         1     "A"         Identifie l'enregistrement
         *     02       2-10       9     Num         Numéro de l'enregistrement
         *     03      11-20       10    AlphaN       Numéro de l'usager
         *     04      21-24       4     Num         Numéro de création du fichier
         *     05      25-30       6     OAAJJJ      Date de création
         *     06      31-35       5     Num         Centrale info. Desjardins (81510)
         *     07      36-55      20     Blanc       Réservé/Remplissage
         *     08	   56-58	   3	 A			 Code de devise(CAD ou USD)
         *     09	   59-1464	1406	 AlphaN		 Réservé/Remplissage	
         *     
         *     
         *   //Diffère de CP    
         *     08      56-1463  1408     Blanc       Remplissage
         *     09      1464-1464   1     "0"         Pour que le record ne soit
         *                                           pas tronqué
         * 
         */
        P_BankAccountDoc BankAccountDoc = null;
        String sql = "select b.P_BankAccount_ID From P_Financial_Institution a, P_BankAccount b" 
        			+ " where  b.P_Bank_ID = " + Bank.getP_Bank_ID() + " AND a.P_Financial_Institution_ID = b.P_Financial_Institution_ID"
        			+ " and a.IsActive = 'Y' and b.IsActive = 'Y' and b.ad_client_id = " + Env.getAD_Client_ID(Env.getCtx()); 

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
                   rsacc.close();
                   psacc.close();
                }
        }
        catch (SQLException e)
        {
            log.log(Level.SEVERE, "CreateFileTransfertBNC.CreateHeader", e);
        }

    	//P_BankAccountDoc BankAccountDoc = null;
    	String Sql = "Select P_BankAccountDoc_ID from P_BankAccountDoc " +
    		         " Where P_BankAccount_ID = " +  BankAccount.getP_BankAccount_ID() +
    		         "   and PaymentRule = 'D'";

    	PreparedStatement psdoc = null;
      try
      { 
        psdoc = DB.prepareStatement(Sql, null);
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
      	log.log(Level.SEVERE, "CreateFileTransfertBNC.CreateSecondLine", e);
  	}
    	
    if ( m_NumberTransfert != 0)
    {
    	NumberTransfert = String.valueOf( m_NumberTransfert );
        BankAccountDoc.setDepositNext( m_NumberTransfert + 1);
    }
    else
    {
    	NumberTransfert = String.valueOf(BankAccountDoc.getDepositNext());
      	BankAccountDoc.setDepositNext(BankAccountDoc.getDepositNext() + 1);
    }
    	
  	BankAccountDoc.save();
  	MCurrency Currency = MCurrency.get( getCtx(), BankAccount.getC_Currency_ID());
    	
  	NoLigne++;
  	//  Creation de la première lien d'entête
  	//  Create the first line of the title
  	//TypeEnr colonne attribuer dans le fichier: 01
  	String TypeEnr = "A";     
  	//NoEnr colonne attribuer dans le fichier: 02
  	String NoEnr =  "000000001"; 
  	//NoUsager colonne attribuer dans le fichier: 03       
//  	String NoCli = String.valueOf(BankAccount.getTransfertNumber());
//TODO Hardcode
  	
  	//NoUsager = "5231300610"; //"0000000000".substring(0, 10 - NoCli.length()) + NoCli;
  	
  	NoUsager = Bank.getNoUsager();
  	//86990
  	
  	//  NoTrans colonne attribuer dans le fichier: 04  
  	NoTrans = "0000".substring(0, 4 - NumberTransfert.length()) + NumberTransfert;                // Numéro de transfert
  	//  DateTrans colonne attribuer dans le fichier: 05  
  	// Date de création du fichier format (aajjj)
  	// Calc julian date
  	Calendar Year = Calendar.getInstance();
  	String DateTrans = String.valueOf(Year.get(Calendar.YEAR));
  	DateTrans = "0" + DateTrans.substring(2, 4) + "000".substring(0, 3 - String.valueOf(Year.get(Calendar.DAY_OF_YEAR)).length()) + String.valueOf(Year.get(Calendar.DAY_OF_YEAR));
  	//Destinataire colonne attribuer dans le fichier: 06  
  	String Destinataire = "00610";
  	// Devise colonne attribuer dans le fichier: 08  
  	String Devise = Currency.getISO_Code(); 
  	NewFile.append(TypeEnr + NoEnr + NoUsager + NoTrans + DateTrans + Destinataire);
  	//  Blank colonne attribuer dans le fichier: 07  
  	InsertBlank(NewFile, 20);
  	NewFile.append(Devise);
  	//  Blank colonne attribuer dans le fichier: 09  
  	InsertBlank(NewFile, 1406);
  	NewFile.append("\r\n");
  	//log.debug("CreateFileTransfertBNC.CréateSecondLine - End");
    }   //     fin CreateSecondLine
    
    public void CreateDetail (StringBuffer NewFile, String trxName)
    {
        /*
         * Enregistrement de type C. L'enregistrement est composé du dépôt de 6
         *                           employés.
         * 
         * Cet enregistrement identifie le montant et les coordonnées de chaque
         * dépôt.
         * 
         * 
         * No zone  Position  Longueur  Contenue    Description
         * -------  --------  --------  --------    -------------------------
         *    01        1         1     "C"         Identifie l'enregistrement
         *    02       2-10       9     Num         Numéro enregistrement
         *    03      11-24       14    Alpha       Données de contrôle de création
         *                                           . Numéro organisme émetteur
         *                                           . Numéro fichier transfert
         * SEGMENT UN
         *    04      25-27       3     Num         Type d'opération
         *    05      28-37       10    Num         Montant
         *    06      38-43       6     Num         Date disponibilité fonds
         *    07      44-52       9     0bbbsssss   Code d'identification de
         *                                          l'institution de l'employé.
         *                                          0     = zéro
         *                                          bbb   = Numéro de banque
         *                                          sssss = transit
         *    08      53-64       12    Alpha       No. compte bénéficiaire
         *    09      65-86       22    Num         No. repère
         *    10      87-89       3     Num         Type d'opération initiale
         *                                          (lors de la retransmission)
         *    11      90-104      15    Alpha       Nom abrégé de l'organisme
         *    12     105-134      30    Alpha       Nom du bénéficiaire
         *    13     135-164      30    Alpha       Nom de l'organisme
         *    14     165-174      10    Alpha       Numéro de l'organisme émetteur
         *    15     175-193      19    Alpha       Numéro de référence
         *    16     194-202      9     0bbbsssss   Code d'identification de
         *                                          l'institution de retour
         *                                          0     = zéro
         *                                          bbb   = Numéro de banque
         *                                          sssss = transit
         *    17     203-214      12    ALpha       Numéro compte pour les retours
         *    18     215-229      15    Alpha       Champ réservé à l'organisme
         *                                          Employé C(6)
         *                                          Période C(2)
         *                                          Date fin période N(6)
         *    19     230-251      22    Alpha       Remplissage
         *    20     252-253      2     Alpha       Code de règlement
         *    21     254-264      11    Num         Identif. d'éléments invalides
         * 
         * SEGMENT DEUX     : 265-504
         * SEGMENT TROIS    : 505-744
         * SEGMENT QUATRE   : 745-984
         * SEGMENT CINQ     : 985-1224
         * SEGMENT SIX      : 1225-1464
         * 
         */
    	//log.debug("CreateFileTransfertBNC.CreateDetail - Start");
    	try
  	{
    	    P_Payment Payment = null;
//    	    P_Payment_Distribution Payment_dist = null;
    	    P_Employee Employee = null;
    	    P_Financial_Institution Financial_Institution = null;
    	    P_Financial_Institution Financial_Institution_Return = null;
    		P_Employer Employer = null;
    		P_Period Period = null;
    		int Count=0;
    		
    		// On va chercher tout les paiements
    		//ResultSet rs = pstmp.executeQuery ();
    		rs.beforeFirst();
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

    			Period = P_Period.get(getCtx(), Period_ID, trxName);
    			NoPaiment++;
    			Employee = P_Employee.get(getCtx(), Payment.getP_Employee_ID(), trxName);
    			Employer = P_Employer.get(getCtx(), Payment.getP_Employer_ID(), trxName);

    			log.log(Level.INFO, "UpdatePaymentDistribution " + NoPaiment + " Payment:  " + Payment.getValue() + " Employee: " + Employee.getValue());

    			String SqlDist = "Select * " +
  			                     " From P_Payment_Distribution " +
  			                     "where IsActive = 'Y' and Amount > 0 and P_Payment_ID=" + Payment.getP_Payment_ID();
    			//log.debug(SqlDist);
    			PreparedStatement pstmp_dist = null;
    			try
  			    {
    				pstmp_dist = DB.prepareStatement(SqlDist, null);
  			    }
    			catch (Exception e)
  			    {
    				log.log(Level.SEVERE, "CreateFileTransfertBNC.UpdatePaymetDistribution Employee_Dist", e);
  			    }
    			ResultSet rs_Dist = pstmp_dist.executeQuery();
    			int NoDist = 0;

    			while (rs_Dist.next())
    			{
      	        	Count++;
    			    MClient Client = MClient.get(getCtx(), Env.getAD_Client_ID(getCtx()));
//    				Payment_dist = P_Payment_Distribution.get( getCtx(), rs_Dist.getInt("P_Payment_Distribution_ID" ), trxName);

    				Financial_Institution = P_Financial_Institution.get(getCtx(), rs_Dist.getInt( "P_Financial_Institution_ID" ), trxName);
                    Financial_Institution_Return = P_Financial_Institution.get(getCtx(), BankAccount.getP_Financial_Institution_ID(), trxName);
    				NbreTotal++;
    				NoDist++;
    				String NoEnr="";
    				String TypeEnr=""; 
    			  	String CodeOper = "200";         // Code d'operation
    			  	String NoEmploye = "";
    			  	String PreprintedNo = "";
    			  	String NoReference = "";
    			  	if (Employee.getValue().trim().length() > 6)         // Numero d'employe
    			  	    NoEmploye = Employee.getValue().trim().substring(0, 6);
    			  	else
    			  	    NoEmploye = "000000".substring(0, 6 - Employee.getValue().trim().length()) + Employee.getValue().trim();
  					if ( Payment.getPrePrintedNo() != null ) 
  					{
  	    			  	if (Payment.getPrePrintedNo().trim().length() > 13)
  	    			  	    PreprintedNo = Payment.getPrePrintedNo().substring(0, 13);
  	    			  	else
  	    			  	    PreprintedNo = Payment.getPrePrintedNo().trim();
  					}
    			  	NoReference = NoEmploye + PreprintedNo;
    			  	String NoPayment = "00".substring(0, 2 - String.valueOf(NoDist).length()) + String.valueOf(NoDist);                                // Numero du payment
    			  	String NoBanque = "";
    			  	if (Financial_Institution.getValue().trim().length() > 3)
    			  		NoBanque = Financial_Institution.getValue().substring(0, 3);
    			  	else
    			  	   NoBanque = "000".substring(0, 3 - Financial_Institution.getValue().trim().length()) + Financial_Institution.getValue().trim();                          // Numero de la banque
    			  	String Transit = "";
    			  	if (rs_Dist.getString("Transit").trim().length() > 5)
    			  		Transit = rs_Dist.getString("Transit").trim().substring(0, 5);
    			  	else
    			  		Transit = "00000".substring(0, 5 - rs_Dist.getString("Transit").trim().length()) + rs_Dist.getString("Transit").trim();
    			  	String NoInstitution = "0" + NoBanque + Transit;
    			    String NoCompte = "";                                 // Numero de compte de l'employe
    			    if (rs_Dist.getString("Folio").trim().length() > 12)
    			    	NoCompte = rs_Dist.getString("Folio").trim().substring(0, 12);
    			    else
    			    	NoCompte = rs_Dist.getString("Folio").trim();
    			    String Mntnet = String.valueOf(rs_Dist.getBigDecimal("Amount").multiply(Cent).setScale(0));
    			    MntTotal = MntTotal.add(rs_Dist.getBigDecimal("Amount"));
    			  	String MontantNet = "0000000000".substring(0, 10 - Mntnet.trim().length()) + Mntnet;          // Montant Net
    			    //log.debug("Montant Net : " + Mntnet + " Montant : " + MntTotal);
    			  	int tsYear = 0;
    			  	int tsMonth = 0;
    			  	int tsDay = 0;
    			  	Calendar LastDay = Calendar.getInstance();

    			  	if (DateDisponibility.length() == 0)
    			  	{
        			  	LastDay = TimeUtil.getCalendar( Payment.getPayDate());
  			        }
    			  	else
    			  	{
    			  	    try
    			  	    {
        			  	    tsYear = (new Integer(DateDisponibility.substring(0, 4))).intValue();
        			  	    tsMonth = (new Integer(DateDisponibility.substring(5, 7))).intValue() - 1;
        			  	    tsDay = (new Integer(DateDisponibility.substring(8, 10))).intValue();
          				    LastDay.set(tsYear, tsMonth, tsDay);
    			  	    }
    			  	    catch(Exception e)
    			  	    {
    			  	        log.log(Level.SEVERE, "CreateDetail- DateDisponibility", e);
    			  	    }
    			  	}
  				    int JulianDay = LastDay.get(Calendar.DAY_OF_YEAR);
  				    String Year = String.valueOf(LastDay.get(Calendar.YEAR));
     				String DateJulien = "0" + Year.substring(2, 4) + "000".substring(0, 3 - String.valueOf(JulianDay).length()) + String.valueOf(JulianDay);
      				String Nom = "";
      				String Prenom = "";
      				String NomPrenom = "";
      				if (Employee.getName().trim().length() > 30)
    				    Nom = Employee.getName().trim().substring(0, 30);                                // Nom et prénom de l'employe
    			  	else
    			  		Nom = Employee.getName().trim();
      				if (Employee.getFirstName().trim().length() > 30)
    				    Prenom = Employee.getFirstName().trim().substring(0, 30);                                // Nom et prénom de l'employe
    			  	else
    			  		Prenom = Employee.getFirstName().trim();
    				NomPrenom = Nom; //+ " " + Prenom;
    				String Langue = "";
    				P_Language Language = P_Language.get( Env.getCtx(), Employee.getP_Language_ID(), trxName );
  				    if (Language.getValue().compareTo("fr_CA") == 0)
  		    		  	Langue = "F";                                  // Langue
  			    	else
  				     	Langue = "A";                                  // Langue
     				String Remplissage3 = " ";
  	    		  	String NomAbrCie = "";                                // Nom abrégé de la compagnie
  		    		if (Client.getValue().trim().length() > 15)
  			    		NomAbrCie = Client.getValue().trim().substring(0, 15);
  				    else
    					NomAbrCie = Client.getValue().trim();

  		    		//TODO Hardcode. ajout champ dans la table
  		    		NomAbrCie = "CHL";
  		    		
  		    		String NomCie = "";
  		    		// Pour hélico et pour siq getdescription
  		    		if (Client.getName().trim().length() > 30)
  		    		    NomCie = Client.getName().trim().substring(0, 30);
  		    		else
  		    		    NomCie = Client.getName().trim();
  		    		NomCie = NomCie.toUpperCase().replaceAll("É", "E").replaceAll("È", "E");
  		    		String NoInstitutionReturn = "";   		
  		    		if (Financial_Institution.getValue().trim().length() > 3)
  		    		    NoInstitutionReturn = Financial_Institution_Return.getValue().substring(0, 3);
  		    		else
  		    		    NoInstitutionReturn = Financial_Institution_Return.getValue().trim();
  		    		String NoTransitReturn = "";
  		    		if (BankAccount.getBranchNumber().trim().length() > 5)
  		    		     NoTransitReturn = BankAccount.getBranchNumber().substring(0, 5);
  		    		else
  		    		    NoTransitReturn ="00000".substring(0, 5 - BankAccount.getBranchNumber().length()) + BankAccount.getBranchNumber().trim();; 
  		    		String InstitutionReturn = "0" + NoInstitutionReturn + NoTransitReturn; 
  		    		String NoFolioReturn = "";
  		    		if (BankAccount.getAccountNo().trim().length() > 12)
  		    		    NoFolioReturn = BankAccount.getAccountNo().substring(0, 12);
  		    		else
  		    		    NoFolioReturn = BankAccount.getAccountNo().trim();
  				    String PeriodNo = "";
  				    String EndDate = "";
  				    PeriodNo = "00".substring(0, 2 - String.valueOf(Period.getPeriodNo()).trim().length()) + String.valueOf(Period.getPeriodNo()).trim();
  				    EndDate = String.valueOf(Period.getEndDate());
  				    String NoReference2 = "";
  				    NoReference2 = NoEmploye + PeriodNo + EndDate.substring(2, 4) + EndDate.substring(5, 7) + EndDate.substring(8, 10);
    				if (Count==1)
    				{
    					NoLigne++;
    					NoEnr =  "000000000".substring(0, 9 - String.valueOf(NoLigne).length()) + String.valueOf( NoLigne ); 
						TypeEnr = "C";
						//Colonne 01-02-03
    					NewFile.append(TypeEnr + NoEnr +  NoUsager + NoTrans);
    				}
    				//Colonne 04-05-06-07-08
    				NewFile.append(CodeOper + MontantNet + DateJulien + NoInstitution + NoCompte);
    				InsertBlank(NewFile, 12 - NoCompte.length());
    				//Colonne 09-10
    				ZeroFill(NewFile, 25);
    				//Colonne 11
    				NewFile.append(NomAbrCie);
    				InsertBlank(NewFile, 15 - NomAbrCie.length());
    				//Colonne 12
    				NewFile.append(NomPrenom );
    				InsertBlank(NewFile, 30 - NomPrenom.length());
    				//Colonne 13
    				NewFile.append(NomCie);
    				InsertBlank(NewFile, 30 - NomCie.length());
    				//Colonne 14-15
    				NewFile.append(NoUsager + NoReference);
    				InsertBlank(NewFile, 19 - NoReference.length());
    				//Colonne 16
    				NewFile.append(InstitutionReturn);
    				//Colonne 17
    				NewFile.append(NoFolioReturn);
    				InsertBlank(NewFile, 12 - NoFolioReturn.length());
    				//Colonne 18 ??????
    				NewFile.append(NoReference2);
    				InsertBlank(NewFile, 15 - NoReference2.length());
    				//Colonne 19
    				InsertBlank(NewFile, 22);
    				//Colonne 20
    				InsertBlank(NewFile, 2);
    				//Colonne 21
    				ZeroFill(NewFile, 11);
    				//InsertBlank(NewFile, 1200);
    				if (Count==6)
     		   	    {
    					NewFile.append("\r\n");
    					Count=0;
     		   	    }		
    			}	
    			rs_Dist.close();
    			pstmp_dist.close();
    		}
    		rs.close();
			if (Count!=0)
				NewFile.append("\r\n");
  		}
	    catch (SQLException e)
	  	{
	    		log.log(Level.SEVERE, "CreateFileTransfertBNC.CreateDetail ", e);
	  	}
		log.log(Level.INFO, "CreateFileTransfertBNC.CreateDetail - End");
    }
    
    public void CreateEndLine (StringBuffer NewFile)
    {
        /*
         * Enregistrement de fin de type Z
         * 
         * Cet article fin constitue le dernier enregistrement logique du fichier. Il
         * fournit des paramètres indépendants de ceux que fournissent les étiquettes
         * ou les libellés.
         * 
         * No zone  Position  Longueur  Format	    Description
         * -------  --------  --------  --------    -------------------------
         *    01        1         1     "Z"         Identifie l'enregistrement
         *    02       2-10       9     Num         Numéro de séquence
         *    03      11-24      14     Alpha       Numéro de contrôle
         *    04      25-38      14     Num         Réservé/Remplissage 0
         *    05      39-46       8     Num         Réservé/Remplissage 0
         *    06      47-60      14     Num         Valeur totale des crédits
         *    07      61-68       8     Num         Nombre total des crédits
         *    08      69-82      14     Num         Réservé/Remplissage 0
         *    09      83-90       8     Num         Réservé/Remplissage 0
         *    10      91-104     14     Num         Réservé/Remplissage 0
         *    11      105-112     8     Num         Réservé/Remplissage 0
         *    12      113-1464  1352    Alpha       Remplissage de blanc
         *    
         *    //diffère CP
         *    13      1464-1464    1    "0"         Pour pas que le record soit
         *                                          tronqué
         */
    	//log.debug("CreateFileTransfertBNC.CreateEndLine - Start");
    	NoLigne++;
    	//NoSeq col attribuer 02
    	String NoSeq =  "000000000".substring(0, 9 - String.valueOf(NoLigne).length()) + String.valueOf( NoLigne );
    	//TypeEnr col attribuer 01
    	String TypeEnr = "Z"; 
    	// Type d'enregistrement
    	//String CodeOper = "TRL";  // Code d'operation
    	//NbreOperation cp; attribuer 03
    	String NbreOperation = "00000000".substring(0, 8 - String.valueOf(NbreTotal).length()) + String.valueOf(NbreTotal);
  	    BigDecimal Cent = new BigDecimal(100);
    	String MntString = String.valueOf(MntTotal.multiply(Cent).setScale(0));
    	String MontantTotal = "00000000000000".substring(0, 14 - MntString.length()) + MntString;
    	//log.debug("Montant : " + MntString + "  " + MontantTotal + "  " + MntTotal);
    	//NoUsager et NoTrans col attribuer 03
    	NewFile.append(TypeEnr + NoSeq + NoUsager + NoTrans); 
    	ZeroFill(NewFile, 14);
    	ZeroFill(NewFile, 8);
    	NewFile.append(MontantTotal + NbreOperation);
    	ZeroFill(NewFile, 14);
    	ZeroFill(NewFile, 8);
    	ZeroFill(NewFile, 14);
    	ZeroFill(NewFile, 8);
    	InsertBlank(NewFile, 1352);
    	NewFile.append("\r\n");
    	//log.debug("CreateFileTransfertBNC.CreateEndLine - End");
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
            log.log(Level.SEVERE, "CreateFileTransfertCP.WriteToFile", e);
        }
        log.log(Level.INFO, "CreateFileTransfertBNC.WriteToFile - Path" + Path);
    	P_Period Period = P_Period.get(getCtx(), Period_ID, trxName);

    	//    	FileName = "tf0380052313.txt" ;//"Deposit-" +Period.getName() + "-" + NumberTransfert;
    	
    	FileName = Bank.getFileName();
    	
  	    File aFile = null;
  	    Path = PgiUtil.getSolsticeParameter(Env.getCtx(), "TransfertPathDeposit");
  	    aFile = new File(Path + FileName ); //+ ".dat");
        FileOutputStream out; // declare a file output object
        PrintStream p; // declare a print stream object
        try
        {
              // Create a new file output stream
              // connected to "myfile.txt"
              out = new FileOutputStream(aFile);

              // Connect print stream to the output stream
              p = new PrintStream( out );

              p.println (NewFile.toString());
              System.out.println(NewFile.toString());
              p.close();
        }
        catch (Exception e)
        {
        	log.log (Level.SEVERE, "Error writing to file :", e);
        }
   }
    
	public Properties getCtx()
	{
		return m_ctx;
	}


}

