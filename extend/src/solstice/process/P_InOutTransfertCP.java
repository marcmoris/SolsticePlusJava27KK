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
public class P_InOutTransfertCP {
    private int Client_ID = 0;
    private int Org_ID = 0;
    private int Period_ID = 0;
    private int User_ID = 0;
    private int Frequency_ID = 0;
    private int Payment_Group_ID = 0;
    private int NoLigne = 0;
    private int NbreTotal = 0;
    private String NoClient= "";
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

    private Properties m_ctx;
    
	private static CLogger		log = CLogger.getCLogger (P_InOutTransfertCP.class);

    public P_InOutTransfertCP( Properties ctx)
    {
        m_ctx = ctx;
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
    	//log.debug("CreateFileTransfertRBC.Blank - Début");
    	for (int i = 0; i < Pos; i++)
    	{
    		Bl.append(" ");
    	}
    	//log.debug("CreateFileTransfertRBC.Blank - Fin");
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
         *     03      11-20       10    Alpha       Numéro organisme émetteur
         *     04      21-24       4     Num         Numéro de création du fichier
         *     05      25-30       6     OAAJJJ      Quantième de la création du fichier
         *     06      31-35       5     Num         Centrale info. Desjardins (81510)
         *     07      36-55      20     Blanc       Remplissage
         *     08      56-1463  1408     Blanc       Remplissage
         *     09      1464-1464   1     "0"         Pour que le record ne soit
         *                                           pas tronqué
         * 
         */
        P_BankAccountDoc BankAccountDoc = null;
        String sql = "select b.P_BankAccount_ID "  +
        "From P_Financial_Institution a, P_BankAccount b " +
    	   "where a.AD_Org_ID = b.AD_Org_id and a.AD_Client_ID = b.AD_Client_id" +
          "  and a.P_Financial_Institution_ID = b.P_Financial_Institution_ID" +
          "  and a.IsActive = 'Y' and b.IsActive = 'Y'";
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
            log.log(Level.SEVERE, "CreateFileTransfertCP.CreateHeader", e);
        }
        
    	//P_BankAccountDoc BankAccountDoc = null;
    	String Sql = "Select P_BankAccountDoc_id from P_BankAccountDoc " +
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
      	log.log(Level.SEVERE, "CreateFileTransfertCP.CreateSecondLine", e);
  	}
    	
  	NumberTransfert = String.valueOf(BankAccountDoc.getDepositNext());
  	BankAccountDoc.setDepositNext(BankAccountDoc.getDepositNext() + 1);
  	BankAccountDoc.save();
  	MCurrency Currency = MCurrency.get( getCtx(), BankAccount.getC_Currency_ID());
    	
  	NoLigne++;
  	//og.debug("CreateSecondLine" + Client.getName());
  	//  Creation de la première lieng d'entête
  	//  Create the first line of the title
  	String NoEnr1 = String.valueOf(NoLigne);
  	String NoEnr =  "000000000".substring(0, 9 - NoEnr1.length()) + String.valueOf( NoLigne ); 
  	String TypeEnr = "A";                              
  	// Inséré le numero de client 5194710000
  	//NoClient = String.valueOf(BankAccount.getTransfertNumber());
  	String NoCli = String.valueOf(BankAccount.getTransfertNumber());
  	NoClient = "0000000000".substring(0, 10 - NoCli.length()) + NoCli;
  	// Inséré le numéro de transfert
  	NoTrans = "0000".substring(0, 4 - NumberTransfert.length()) + NumberTransfert;                // Numéro de transfert
  	// Date de création du fichier format (aajjj)
  	// Calc julian date
  	//DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
  	String CentraleInf = "81510";
  	//Calendar Year = Calendar.getInstance();
  	Calendar Year = Calendar.getInstance();
  	String DateTrans = String.valueOf(Year.get(Calendar.YEAR));
    //String x = DateTrans.substring(2, 4);
  	DateTrans = "0" + DateTrans.substring(2, 4) + "000".substring(0, 3 - String.valueOf(Year.get(Calendar.DAY_OF_YEAR)).length()) + String.valueOf(Year.get(Calendar.DAY_OF_YEAR));
  	String Devise = Currency.getISO_Code(); 
  	NewFile.append(TypeEnr + NoEnr + NoClient + NoTrans + DateTrans + CentraleInf);
  	InsertBlank(NewFile, 20);
  	NewFile.append(Devise);
  	InsertBlank(NewFile, 1406);
  	NewFile.append("\n");
  	//log.debug("CreateFileTransfertRBC.CréateSecondLine - End");
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
    	//log.debug("CreateFileTransfertRBC.CreateDetail - Start");
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
  			                     "where IsActive = 'Y' and P_Payment_ID=" + Payment.getP_Payment_ID();
    			//log.debug(SqlDist);
    			PreparedStatement pstmp_dist = null;
    			try
  			    {
    				pstmp_dist = DB.prepareStatement(SqlDist, null);
  			    }
    			catch (Exception e)
  			    {
    				log.log(Level.SEVERE, "CreateFileTransfertCP.UpdatePaymetDistribution Employee_Dist", e);
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
    			  	String CodeOper = "200";                                              // Code d'operation
    			  	String NoEmploye = "";
    			  	String PreprintedNo = "";
    			  	String NoReference = "";
    			  	if (Employee.getValue().trim().length() > 6)                                // Numero d'employe
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
//    			  	    tsYear = Payment.getPayDate().getYear() + 1900;
//    			  	    tsMonth = Payment.getPayDate().getMonth();
//    			  	    tsDay = Payment.getPayDate().getDate();
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
  		    		String NomCie = "";
  		    		if (Client.getDescription().trim().length() > 30)
  		    		    NomCie = Client.getDescription().trim().substring(0, 30);
  		    		else
  		    		    NomCie = Client.getDescription().trim();
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
    					NewFile.append(TypeEnr + NoEnr +  NoClient + NoTrans);
    				}
    				NewFile.append(CodeOper + MontantNet + DateJulien + NoInstitution + NoCompte);
    				InsertBlank(NewFile, 12 - NoCompte.length());
    				ZeroFill(NewFile, 25);
    				NewFile.append(NomAbrCie);
    				InsertBlank(NewFile, 15 - NomAbrCie.length());
    				NewFile.append(NomPrenom );
    				InsertBlank(NewFile, 30 - NomPrenom.length());
    				NewFile.append(NomCie);
    				InsertBlank(NewFile, 30 - NomCie.length());
    				NewFile.append(NoClient + NoReference);
    				InsertBlank(NewFile, 19 - NoReference.length());
    				NewFile.append(InstitutionReturn);
    				NewFile.append(NoFolioReturn);
    				InsertBlank(NewFile, 12 - NoFolioReturn.length());
    				NewFile.append(NoReference2);
    				InsertBlank(NewFile, 15 - NoReference2.length());
    				InsertBlank(NewFile, 22);
    				InsertBlank(NewFile, 2);
    				ZeroFill(NewFile, 11);
    				//InsertBlank(NewFile, 1200);
    				if (Count==6)
     		   	    {
    					NewFile.append("\n");
    					Count=0;
     		   	    }		
    			}	
    			rs_Dist.close();
    			pstmp_dist.close();
    		}
    		rs.close();
			if (Count!=0)
				NewFile.append("\n");
  		}
	    catch (SQLException e)
	  	{
	    		log.log(Level.SEVERE, "CreateFileTransfertCP.CreateDetail ", e);
	  	}
		log.log(Level.INFO, "CreateFileTransfertCP.CreateDetail - End");
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
         * No zone  Position  Longueur  Contenue    Description
         * -------  --------  --------  --------    -------------------------
         *    01        1         1     "Z"         Identifie l'enregistrement
         *    02       2-10       9     Num         Numéro enregistrement logique
         *    03      11-24      14     Alpha       Données de contrôle de création
         *    04      25-38      14     Num         Valeur totale enrg. D
         *    05      39-46       8     Num         Nombre total enrg. D
         *    06      47-60      14     Num         Valeur totale enrg. C
         *    07      61-68       8     Num         Nombre total enrg. C
         *    08      69-82      14     Num         Valeur totale enrg. E
         *    09      83-90       8     Num         Nombre total enrg. E
         *    10      91-104     14     Num         Valeur totale enrg. F
         *    11      105-112     8     Num         Nombre total enrg. F
         *    12      113-1463  1351    Alpha       Remplissage
         *    13      1464-1464    1    "0"         Pour pas que le record soit
         *                                          tronqué
         */
    	//log.debug("CreateFileTransfertRBC.CreateEndLine - Start");
    	NoLigne++;
    	String NoEnr =  "000000000".substring(0, 9 - String.valueOf(NoLigne).length()) + String.valueOf( NoLigne );
    	String TypeEnr = "Z";                                                 // Type d'enregistrement
    	String CodeOper = "TRL";                                              // Code d'operation
    	String NbreOperation = "00000000".substring(0, 8 - String.valueOf(NbreTotal).length()) + String.valueOf(NbreTotal);
  	    BigDecimal Cent = new BigDecimal(100);
    	String MntString = String.valueOf(MntTotal.multiply(Cent).setScale(0));
    	String MontantTotal = "00000000000000".substring(0, 14 - MntString.length()) + MntString;
    	//log.debug("Montant : " + MntString + "  " + MontantTotal + "  " + MntTotal);
    	NewFile.append(TypeEnr + NoEnr + NoClient + NoTrans); 
    	ZeroFill(NewFile, 14);
    	ZeroFill(NewFile, 8);
    	NewFile.append(MontantTotal + NbreOperation);
    	ZeroFill(NewFile, 14);
    	ZeroFill(NewFile, 8);
    	ZeroFill(NewFile, 14);
    	ZeroFill(NewFile, 8);
    	InsertBlank(NewFile, 1351);
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
            log.log(Level.SEVERE, "CreateFileTransfertCP.WriteToFile", e);
        }
        log.log(Level.INFO, "CreateFileTransfertCP.WriteToFile - Path" + Path);
    	P_Period Period = P_Period.get(getCtx(), Period_ID, trxName);
    	FileName = Period.getName();
  	    File aFile = null;
  	    aFile = new File(Path + FileName + ".dat");
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

