<%@ page import="java.io.*" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>


<%@ page import="org.compiere.util.DB" %>
<%@ page import="org.compiere.util.Env" %>
<%@ page import="org.compiere.util.Msg" %>

<%@ page import="solstice.custom.IUserInfo" %>

<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Calendar" %>

<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="solstice.web.PageWebSolstice" %>
<%@ page import="solstice.model.P_Employee_Roe" %>
<%@ page import="solstice.model.P_Employee" %>
<%@ page import="solstice.model.P_Payment_Group" %>
<%@ page import="solstice.model.P_Employer" %>
<%@ page import="solstice.model.P_Frequency" %>
<%@ page import="solstice.model.P_Job_Title" %>
<%@ page import="solstice.model.P_Language" %>
<%@ page import="solstice.model.P_Period" %>
<%@ page import="solstice.model.P_Post" %>
<%@ page import="solstice.model.P_Assignment" %>
<%@ page import="solstice.model.P_Time_Sheet" %>
<%@ page import="solstice.model.P_Job_Type" %>

<%@ page import="org.compiere.model.MActivity" %>
<%@ page import="org.compiere.model.MLocation" %>
<%@ page import="org.compiere.model.MOrg" %>
<%@ page import="org.compiere.model.MClient" %>
<%@ page import="org.compiere.model.MUser" %>
<%@ page import="java.util.logging.Level" %>
<%@ page import="org.compiere.util.CLogger" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page extends="solstice.web.PageWebSolstice" %>
<%!

	int roeID ;
	int employeeID ;

	boolean isSOTrx = true;

    P_Employee_Roe employeeRoe;
    P_Employee employee;
    P_Employer employer;
    P_Frequency frequency;
    P_Job_Title jobTitle;
    P_Payment_Group PaymentGroup;

    
	private String ComboOtherMonies( String OtherMonies)
	{
		String comboBox;
		String[] values = { " ", "A", "B", "E", "G", "H", "I", "N", "O", "R", "S", "U", "V", "Y" }; 
		String[] description = {  " ", "A - Paiement-date", "B - Prime", "E - Indemnit&eacute; de d&eacute;part", "G - Gratifications", "H - Honoraires", "I - Cr&eacute;dit de cong&eacute;s de maladie", "N - Rente(s)", "O - Autre", "R - Cr&eacute;dit de cong&eacute; de retraite", "S - R&egrave;glement d'un diff&eacute;rend", "U - Prestations suppl&eacute;mentaires", "V - Paye de vacances", "Y - Indemnit&eacute; de pr&eacute;avis"}; 
		
		int nbr = 13;
		
		comboBox = "<option>" + "</option>";
		for ( int i = 1; i <= 13 ;i++)
		{
			if ( values[i].equals( OtherMonies ))
				comboBox = comboBox + "<option value=\"" + values[i] + "\" Selected > " + description[i] +"</option>" +"\n";  
			else	
				comboBox = comboBox + "<option value=\"" + values[i] + "\"> " + description[i] +"</option>" +"\n";  
		}
		return comboBox;
		
	}
	
	/*
	B = Quinzaine
	M = Mensuel
	O = Mensuel non conventionnel
	S = Bimensuel
	E = Bimensuel non conventionnel
	H = 13 périodes de paye par année
	W = Hebdomadaire
	*/
	private String comboBoxPayPeriodType( String PeriodTyp)
	{
		String comboBox;
		String[] values = { "B", "M", "O", "S", "E", "H", "W" }; 
		String[] description = {  "B - Quinzaine", "M = Mensuel", "O = Mensuel non conventionnel",	"S = Bimensuel",	"E = Bimensuel non conventionnel",	"H = 13 p&eacute;riodes de paye par ann&eacute;e", "W = Hebdomadaire"}; 
		
		int nbr = 6;

		comboBox = "<option>" + "</option>";
		for ( int i = 0; i <= 6 ;i++)
		{
			if ( values[i].equals( PeriodTyp ))
				comboBox = comboBox + "<option value=\"" + values[i] + "\" Selected > " + description[i] +"</option>" +"\n";  
			else	
				comboBox = comboBox + "<option value=\"" + values[i] + "\"> " + description[i] +"</option>" +"\n";  
		}
		
		return comboBox;
	}
	
	private String ComboBoxReason( String Reason)  
	{
		String comboBox;
		String[] values = { "K", "N", "Z", "M", "E", "J", "B", "D", "A", "F", "C", "P", "G", "H", "R" }; 
		String[] description = {  "K - Autre", "N - Cong&eacute;", "Z - Cong&eacute; de compassion",	"M - Cong&eacute;diement",	"E - D&eacute;part volontaire",	"J - Formation en apprentissage", "B - Gr&egrave;ve ou lock-out", "D - Maladie ou blessure", "A - Manque de travail", "F - Maternit&eacute;", "C - Retour aux &eacute;tudes", "P - Parental", "G - Retraite", "H - Travail partag&eacute", "R - Décès"}; 
		
		int nbr = 14;

		comboBox = "<option>" + "</option>";
		for ( int i = 0; i <= 14 ;i++)
		{
			if ( values[i].equals( Reason ))
				comboBox = comboBox + "<option value=\"" + values[i] + "\" Selected > " + description[i] +"</option>" +"\n";  
			else	
				comboBox = comboBox + "<option value=\"" + values[i] + "\"> " + description[i] +"</option>" +"\n";  
		}
		
		return comboBox;
	}
    
    /**
     * Cette méthode crée une table de hachage pour le relevé d'emploi passé en paramètre
     */
    private void loadROE( )
    {
	
        MLocation location_employer = MLocation.get( ctx, employer.getC_Location_ID(), null );
        MLocation location_employee = MLocation.get( ctx, employee.getC_Location_ID(), null );

 //       MClient Client = MClient.get(ctx, employee.getAD_Client_ID());

        MOrg    Org    = MOrg.get( ctx, employee.getAD_Org_ID());
        
//        String employerAddress =  Client.getName() + "<br>" ;// Org.getDescription() + "<br>" ;
        String employerAddress = null;
        if ( Org.getDescription() != null )
        	employerAddress =  Org.getDescription() + "<br>" ;// Org.getDescription() + "<br>" ;
        else
        	employerAddress =  Org.getName() + "<br>" ;// Org.getDescription() + "<br>" ;
        
        if ( location_employer.getAddress1() != null )
        	employerAddress += location_employer.getAddress1() + "<br>" ;

        if ( location_employer.getAddress2() != null )
        	employerAddress += location_employer.getAddress2() + "<br>"; 

        if ( location_employer.getCity() != null )
        	employerAddress += location_employer.getCity() + ", " ;
        if ( location_employer.getRegionName() != null )
        	employerAddress += location_employer.getRegionName() + ", "; 
        if ( location_employer.getCountryName() != null )
        	employerAddress += location_employer.getCountryName();
                               

        String employeeAddress = employee.getName() + "<br>" ;
        if ( location_employee.getAddress1() != null )
        	employeeAddress += location_employee.getAddress1() + "<br>"; 
        if ( location_employee.getAddress2() != null )
        	employeeAddress += location_employee.getAddress2() + "<br>";
        
    	employeeAddress += location_employee.getPostal() + "<br>"; 

        if ( location_employee.getCity() != null )
        	employeeAddress += location_employee.getCity() + ", " ;
        if ( location_employee.getRegionName() != null )
        	employeeAddress += location_employee.getRegionName() + ", "; 
        if ( location_employee.getCountryName() != null )
        	employeeAddress += location_employee.getCountryName();
		   					

 //       MUser User = MUser.get(ctx, employeeRoe.getAD_User_ID());
        P_Employee employeeContact = P_Employee.getWithUserID(ctx, employeeRoe.getAD_User_ID(), null);
        P_Assignment Assignment = null;
        P_Post Post = null;

        boolean newroe = true;

        
        if ( employeeRoe.getPhone() != null || employeeRoe.getPhoneExt() != null)
        {
    		tpl.assign("Signataire", employeeRoe.getSignataire() );
    		tpl.assign("FirstNameContactPerson", employeeRoe.getFirstNameContactPerson());
    		tpl.assign("LastNameContactPerson", employeeRoe.getLastNameContactPerson());
    		tpl.assign("PhoneAreaCode", employeeRoe.getPhoneAreaCode());
            tpl.assign("Phone", employeeRoe.getPhone());
    		tpl.assign("PhoneExt", employeeRoe.getPhoneExt());
    		
    		newroe = false;
        }

        if ( employeeRoe.getSignataire2() != null || employeeRoe.getPhone2() != null || employeeRoe.getPhoneExt2() != null )
        {
    		tpl.assign("Signataire2", employeeRoe.getSignataire2() );
    		tpl.assign("PhoneAreaCode2", employeeRoe.getPhoneAreaCode2() );
            tpl.assign("Phone2", employeeRoe.getPhone2());
    		tpl.assign("PhoneExt2", employeeRoe.getPhoneExt2());
    		newroe = false;
        }
        
        if ( newroe)
        {

        	if ( employeeContact != null )
            {
                Assignment = P_Assignment.get(ctx, employeeContact.GetAssignmentPrincipal(), null);
                Post = P_Post.get( ctx, Assignment.getP_Post_ID(), null);

        		tpl.assign("Signataire", employeeContact.getName() );
        		tpl.assign("Signataire2", employeeContact.getName() );

 				tpl.assign("PhoneAreaCode", PgiUtil.getPhoneArea( Post.getPhone() ));
 				tpl.assign("PhoneAreaCode2", PgiUtil.getPhoneArea( Post.getPhone() ));
 				tpl.assign("Phone", PgiUtil.getPhoneNoArea( Post.getPhone() ));
 				if ( Post.getPhoneExt() != null)
					tpl.assign("PhoneExt", Post.getPhoneExt());
 				else
					tpl.assign("PhoneExt", "&nbsp;" );
 				tpl.assign("Phone2", PgiUtil.getPhoneNoArea( Post.getPhone() ));

 				if ( Post.getPhoneExt() != null)
					tpl.assign("PhoneExt2", Post.getPhoneExt());
 				else
					tpl.assign("PhoneExt2", "&nbsp;" );
 				
				tpl.assign("FirstNameContactPerson", employeeContact.getFirstName());
				tpl.assign("LastNameContactPerson", employeeContact.getSurname());

				/*
                tpl.assign("PhoneAreaCode", "450");
                tpl.assign("PhoneAreaCode2", "&nbsp;");
                tpl.assign("Phone", "452-3000");
        		tpl.assign("PhoneExt", "&nbsp;");
                tpl.assign("Phone2", "452-3000");
        		tpl.assign("PhoneExt2", "&nbsp;");
        		tpl.assign("FirstNameContactPerson", employeeContact.getFirstName());
        		tpl.assign("LastNameContactPerson", employeeContact.getSurname());

           		if ( Post != null )
        		{
                    tpl.assign("Phone", Post.getPhone());
            		tpl.assign("PhoneExt", Post.getPhoneExt());
                    tpl.assign("Phone2", Post.getPhone());
            		tpl.assign("PhoneExt2", Post.getPhoneExt());
        		}
           		*/

/* 
        		if ( Client.getAD_Client_ID() == 11)
        		{
                    tpl.assign("PhoneAreaCode", "418");
                    tpl.assign("PhoneAreaCode2", "418");
                    tpl.assign("Phone", Post.getPhone());
            		tpl.assign("PhoneExt", Post.getPhoneExt());
                    tpl.assign("Phone2", Post.getPhone());
            		tpl.assign("PhoneExt2", Post.getPhoneExt());
            		tpl.assign("FirstNameContactPerson", employeeContact.getFirstName());
            		tpl.assign("LastNameContactPerson", employeeContact.getSurname());
        			
        		}
        		else
        		{
                    tpl.assign("PhoneAreaCode", "450");
                    tpl.assign("PhoneAreaCode2", "&nbsp;");
                    tpl.assign("Phone", "452-3000");
            		tpl.assign("PhoneExt", "&nbsp;");
                    tpl.assign("Phone2", "452-3000");
            		tpl.assign("PhoneExt2", "&nbsp;");
            		tpl.assign("FirstNameContactPerson", employeeContact.getFirstName());
            		tpl.assign("LastNameContactPerson", employeeContact.getSurname());
        			
        		}
*/
            }
            else
            {
        		tpl.assign("FirstNameContactPerson", "&nbsp;");
        		tpl.assign("LastNameContactPerson", "&nbsp;");

            	tpl.assign("PhoneAreaCode", "&nbsp;");
                tpl.assign("PhoneAreaCode2", "&nbsp;");
                tpl.assign("Phone", "&nbsp;");
        		tpl.assign("PhoneExt", "&nbsp;");
                tpl.assign("Phone2", "&nbsp;");
        		tpl.assign("PhoneExt2", "&nbsp;");
        		tpl.assign("Signataire", "&nbsp;" );
        		tpl.assign("Signataire2", "&nbsp;" );
            }
        	
        }

		tpl.assign( "SelectPeriod" ,ComboBoxPeriod() );

        if ( employeeRoe.getP_Language_ID() != 0)
    		tpl.assign("ComboBoxLanguage", ComboBoxLanguage( employeeRoe.getP_Language_ID(), false ));
        else
        	tpl.assign("ComboBoxLanguage", ComboBoxLanguage( employee.getP_Language_ID(), false) );

    	if (employeeRoe.getPrintLanguageID() != 0)
  	    {
        	tpl.assign("ComboBoxPrintingLanguage", ComboBoxLanguage( employeeRoe.getPrintLanguageID(), true));
  	    }
  	    else
  		    tpl.assign("ComboBoxPrintingLanguage", ComboBoxLanguage( employee.getP_Language_ID(), true));  

        
        tpl.assign( "LabelPeriod", "Période de sélection" 	);

        
//		tpl.assign("Contact", User.getName() );
		P_Language Language = P_Language.get(ctx, employee.getP_Language_ID(), null);
		
		tpl.assign("LabelPhoneAreaCode", "Ind. Rég :" );
		tpl.assign("LabelPhone", "Tél." );
		tpl.assign("LabelPhoneExt", "ext." );


        tpl.assign("Row01", employeeRoe.getP_Employee_Roe_ID());
        if ( employeeRoe.getSerialNumber() != null )
        	tpl.assign("Row02", employeeRoe.getSerialNumber());
        else
        	tpl.assign("Row02", "&nbsp;");
		if ( PgiUtil.getSolsticeParameter(ctx, "ROE_EmployeeNo").equals("2") )
		{
			MActivity Activity = new MActivity( ctx, employee.getC_Activity_ID(), null); 
			tpl.assign("Row03", employee.getValue() + '(' + Activity.getValue() + ')');
		}
		else
		{
			tpl.assign("Row03", employee.getValue());
		}
			
		tpl.assign("Row04", employerAddress);
		tpl.assign("Row05", employer.getValue());
//2012.09.21 Can modify payPeriodType		
//		tpl.assign("Row06", frequency.getPayPeriodType() + " - " + frequency.getName() );
		tpl.assign("PayPeriodType", comboBoxPayPeriodType( employeeRoe.getPayPeriodType() ) );
		if ( employeeRoe.getPayPeriodType() == null )
			tpl.assign("PayPeriodType", comboBoxPayPeriodType( frequency.getPayPeriodType() ) );
		tpl.assign("Row07", location_employer.getPostal());
		tpl.assign("Row08", employee.getSin());   
		tpl.assign("Row09", employeeAddress );
		
    	tpl.assign("FirstDayWorked",           employeeRoe.getFirstDayWorked());
    	if ( employeeRoe.getFirstDayWorked() == null )
    		tpl.assign("FirstDayWorked", P_Employee_Roe.getFirstDayWorked(ctx, employee.getP_Employee_ID() ) );

    	tpl.assign("LastDayForWhichPaid",      employeeRoe.getLastDayForWhichPaid());
		tpl.assign("FinalPayPeriodEndingDate", employeeRoe.getFinalPayPeriodEndingDate());

		tpl.assign("EmployeeOccupation",  employeeRoe.getEmployeeOccupation() );

		tpl.assign("ExpectedDateOfRecall",     employeeRoe.getExpectedDateOfRecall());  
		tpl.assign("TotalInsurableHours",      employeeRoe.getTotalInsurableHours() );
		tpl.assign("TotalInsurableEarnings",   employeeRoe.getTotalInsurableEarnings() );	
		tpl.assign("Row15c01", employeeRoe.getEarningsForPayPeriod01());
		tpl.assign("Row15c02", employeeRoe.getEarningsForPayPeriod02());
		tpl.assign("Row15c03", employeeRoe.getEarningsForPayPeriod03());
		tpl.assign("Row15c04", employeeRoe.getEarningsForPayPeriod04());
		tpl.assign("Row15c05", employeeRoe.getEarningsForPayPeriod05());
		tpl.assign("Row15c06", employeeRoe.getEarningsForPayPeriod06());
		tpl.assign("Row15c07", employeeRoe.getEarningsForPayPeriod07());
		tpl.assign("Row15c08", employeeRoe.getEarningsForPayPeriod08());
		tpl.assign("Row15c09", employeeRoe.getEarningsForPayPeriod09());
		tpl.assign("Row15c10", employeeRoe.getEarningsForPayPeriod10());
		tpl.assign("Row15c11", employeeRoe.getEarningsForPayPeriod11());
		tpl.assign("Row15c12", employeeRoe.getEarningsForPayPeriod12());
		tpl.assign("Row15c13", employeeRoe.getEarningsForPayPeriod13());
		tpl.assign("Row15c14", employeeRoe.getEarningsForPayPeriod14());
		tpl.assign("Row15c15", employeeRoe.getEarningsForPayPeriod15());
		tpl.assign("Row15c16", employeeRoe.getEarningsForPayPeriod16());
		tpl.assign("Row15c17", employeeRoe.getEarningsForPayPeriod17());
		tpl.assign("Row15c18", employeeRoe.getEarningsForPayPeriod18());
		tpl.assign("Row15c19", employeeRoe.getEarningsForPayPeriod19());
		tpl.assign("Row15c20", employeeRoe.getEarningsForPayPeriod20());
		tpl.assign("Row15c21", employeeRoe.getEarningsForPayPeriod21());
		tpl.assign("Row15c22", employeeRoe.getEarningsForPayPeriod22());
		tpl.assign("Row15c23", employeeRoe.getEarningsForPayPeriod23());
		tpl.assign("Row15c24", employeeRoe.getEarningsForPayPeriod24());
		tpl.assign("Row15c25", employeeRoe.getEarningsForPayPeriod25());
		tpl.assign("Row15c26", employeeRoe.getEarningsForPayPeriod26());
		tpl.assign("Row15c27", employeeRoe.getEarningsForPayPeriod27());
		tpl.assign("Row15c28", employeeRoe.getEarningsForPayPeriod28());
		tpl.assign("Row15c29", employeeRoe.getEarningsForPayPeriod29());
		tpl.assign("Row15c30", employeeRoe.getEarningsForPayPeriod30());
		tpl.assign("Row15c31", employeeRoe.getEarningsForPayPeriod31());
		tpl.assign("Row15c32", employeeRoe.getEarningsForPayPeriod32());
		tpl.assign("Row15c33", employeeRoe.getEarningsForPayPeriod33());
		tpl.assign("Row15c34", employeeRoe.getEarningsForPayPeriod34());
		tpl.assign("Row15c35", employeeRoe.getEarningsForPayPeriod35());
		tpl.assign("Row15c36", employeeRoe.getEarningsForPayPeriod36());
		tpl.assign("Row15c37", employeeRoe.getEarningsForPayPeriod37());
		tpl.assign("Row15c38", employeeRoe.getEarningsForPayPeriod38());
		tpl.assign("Row15c39", employeeRoe.getEarningsForPayPeriod39());
		tpl.assign("Row15c40", employeeRoe.getEarningsForPayPeriod40());
		tpl.assign("Row15c41", employeeRoe.getEarningsForPayPeriod41());
		tpl.assign("Row15c42", employeeRoe.getEarningsForPayPeriod42());
		tpl.assign("Row15c43", employeeRoe.getEarningsForPayPeriod43());
		tpl.assign("Row15c44", employeeRoe.getEarningsForPayPeriod44());
		tpl.assign("Row15c45", employeeRoe.getEarningsForPayPeriod45());
		tpl.assign("Row15c46", employeeRoe.getEarningsForPayPeriod46());
		tpl.assign("Row15c47", employeeRoe.getEarningsForPayPeriod47());
		tpl.assign("Row15c48", employeeRoe.getEarningsForPayPeriod48());
		tpl.assign("Row15c49", employeeRoe.getEarningsForPayPeriod49());
		tpl.assign("Row15c50", employeeRoe.getEarningsForPayPeriod50());
		tpl.assign("Row15c51", employeeRoe.getEarningsForPayPeriod51());
		tpl.assign("Row15c52", employeeRoe.getEarningsForPayPeriod52());
		tpl.assign("Row15c53", employeeRoe.getEarningsForPayPeriod53());



		if ( employeeRoe.getReasonForIssuingThisRoe() == null )
    	{
    		tpl.assign("ReasonForIssuingThisRoe",   ComboBoxReason( employee.getLayoffCode() )  );

    	}
    	else
    	{
    		tpl.assign("ReasonForIssuingThisRoe",   ComboBoxReason( employeeRoe.getReasonForIssuingThisRoe() )  );
    		
    	}
		
		
		tpl.assign("VacationPayAmount",           employeeRoe.getVacationPayAmount());
		tpl.assign("StatutoryHolidayPayAmount01", employeeRoe.getStatutoryHolidayPayAmount01());
		tpl.assign("StatutoryHolidayPayAmount02", employeeRoe.getStatutoryHolidayPayAmount02());
		tpl.assign("StatutoryHolidayPayAmount03", employeeRoe.getStatutoryHolidayPayAmount03());
		tpl.assign("StatutoryHolidayPayDate01",   employeeRoe.getStatutoryHolidayPaydate01());
		tpl.assign("StatutoryHolidayPayDate02",   employeeRoe.getStatutoryHolidayPaydate02());
		tpl.assign("StatutoryHolidayPayDate03",   employeeRoe.getStatutoryHolidayPaydate03());
		tpl.assign("OtherMoniesCode01",   ComboOtherMonies( employeeRoe.getOtherMoniesCode01() ));
		tpl.assign("OtherMoniesCode02",   ComboOtherMonies( employeeRoe.getOtherMoniesCode02() ));
		tpl.assign("OtherMoniesCode03",   ComboOtherMonies( employeeRoe.getOtherMoniesCode03() ));
		tpl.assign("OtherMoniesAmount01", employeeRoe.getOtherMoniesAmount01());
		tpl.assign("OtherMoniesAmount02", employeeRoe.getOtherMoniesAmount02());
		tpl.assign("OtherMoniesAmount03", employeeRoe.getOtherMoniesAmount03());
		
		tpl.assign("PaidSickAmount", employeeRoe.getPaidSickAmount());
		tpl.assign("PaidSickDate",   employeeRoe.getPaidSickDate());
		tpl.assign("PaidSickPeriod", employeeRoe.getPaidSickPeriod());
		tpl.assign("CommentsLine01", employeeRoe.getCommentsLine01());
		//2009.07.31
		if ( employee.getLayoffCode() != null && employee.getLayoffCode().trim().equals( P_Employee_Roe.REASONFORISSUINGTHISROE_Other ) && employeeRoe.getCommentsLine01() == null )
		{
			tpl.assign("CommentsLine01", employee.getLayoffComment());
		}
		//
		
		tpl.assign("CommentsLine02", employeeRoe.getCommentsLine02());
		tpl.assign("CommentsLine03", employeeRoe.getCommentsLine03());
		tpl.assign("CommentsLine04", employeeRoe.getCommentsLine04());
/*		tpl.assign("Signataire", employeeRoe.getSignataire());
		tpl.assign("Phone", employeeRoe.getPhone());
		tpl.assign("PhoneExt", employeeRoe.getPhoneExt());
		tpl.assign("Phone2", employeeRoe.getPhone2());
		tpl.assign("PhoneExt2", employeeRoe.getPhoneExt2());
		tpl.assign("Signataire2", employeeRoe.getSignataire2());
*/		
		tpl.assign("CanadaRevenueBusinessNumber", employeeRoe.getCanadaRevenueBusinessNumber());
		
		if ( employeeRoe.getExpectedRecallCode() != null )
		{
			if ( employeeRoe.getExpectedRecallCode().equals("Y"))
			{
				tpl.assign("ExpectedRecallCodeY" , "CHECKED" );
				tpl.assign("ExpectedRecallCodeN" , "" );
				tpl.assign("ExpectedRecallCodeU" , "" );
			}
			if ( employeeRoe.getExpectedRecallCode().equals("N"))
			{
				tpl.assign("ExpectedRecallCodeY" , "" );
				tpl.assign("ExpectedRecallCodeN" , "CHECKED" );
				tpl.assign("ExpectedRecallCodeU" , "" );
			}
			if ( employeeRoe.getExpectedRecallCode().equals("U"))
			{
				tpl.assign("ExpectedRecallCodeY" , "" );
				tpl.assign("ExpectedRecallCodeN" , "" );
				tpl.assign("ExpectedRecallCodeU" , "CHECKED" );
			}
			
		}
		else
		{
			tpl.assign("ExpectedRecallCodeY" , "" );
			tpl.assign("ExpectedRecallCodeN" , "" );
			tpl.assign("ExpectedRecallCodeU" , "" );
		}

		if ( employeeRoe.getPaidSickPeriod() != null)
		{
			if ( employeeRoe.getPaidSickPeriod().equals("W"))
			{
				tpl.assign("PaidSickPeriodW" , "CHECKED" );
				tpl.assign("PaidSickPeriodD" , "" );
			}
			if ( employeeRoe.getPaidSickPeriod().equals("D"))
			{
				tpl.assign("PaidSickPeriodW" , "" );
				tpl.assign("PaidSickPeriodD" , "CHECKED" );
				
			}
		}
		else
		{
			tpl.assign("PaidSickPeriodW" , "CHECKED" );
			tpl.assign("PaidSickPeriodD" , "" );
		}
		
		if ( employeeRoe.isOvertimeIncluded())
			tpl.assign("isOvertimeIncluded", "CHECKED" );
		else
			tpl.assign("isOvertimeIncluded", "" );
			
		
    }



    public String GeneratePage( ) throws Exception 
	{

    	
		this.setPageId("releveEmploi_releve");
		this.setSecurityCheck( true );
		this.setTemplate("EmployeeRoe.jtpl");
		
		if ( ! SecurityCheck() )
		{
			response.sendError(403); 
		}

		if ( request.getParameter("employeeRoeId") != null )
		{
			roeID =  Integer.parseInt( request.getParameter("employeeRoeId").trim());
		}
		else
		{
			roeID =  -1;
		}

		if ( roeID == 0 )
			roeID =  -1;
		
		if ( request.getParameter("employeeId") != null )
		{
			employeeID =  Integer.parseInt( request.getParameter("employeeId").trim());
		}
		
    	// On récupère les informations pour le relevé d'emploi
        employeeRoe = new P_Employee_Roe( ctx, roeID, null);


        
        if ( roeID == -1 )
        {
            employeeID = Integer.parseInt( request.getParameter("employeeId").trim() );
            employee = P_Employee.get( ctx, employeeID, null);

            employer = P_Employer.get( ctx, employee.getP_Employer_ID(), null);
            employeeRoe.setAD_User_ID( ((IUserInfo)session.getAttribute("userInfo")).getUserId() );

            P_Payment_Group PaymentGroup = P_Payment_Group.get( ctx, employee.getP_Payment_Group_ID(), null);
            jobTitle = P_Job_Title.get( ctx,  employee.getP_Job_Title_ID(), null );
            
	        
            P_Assignment assignment = P_Assignment.get(ctx, employee.GetAssignmentPrincipal(), null );
	        P_Post post = P_Post.get( ctx, assignment.getP_Post_ID(), null);
	        P_Job_Type job_Type =  P_Job_Type.get( Env.getCtx(), employee.getP_Job_Type_ID(), null);

			if ( post != null)
				employeeRoe.setEmployeeOccupation( post.getName() + " (" + job_Type.getName() + ")" );
			else
				employeeRoe.setEmployeeOccupation( "(" + job_Type.getName() + ")"  );

            employeeRoe.setP_Employee_ID(employee.getP_Employee_ID());
            employeeRoe.setP_Employer_ID(employer.getP_Employer_ID());
            employeeRoe.setAD_Org_ID(employee.getAD_Org_ID());
            employeeRoe.setP_Job_Title_ID(jobTitle.getP_Job_Title_ID());
            
            P_Period Period = P_Period.getOpenPeriodWithCalendar(ctx, PaymentGroup.getP_Calendar_ID(), null);
            
            
            employeeRoe.setP_Period_ID( Period.getP_Period_ID() );
            employeeRoe.setP_Language_ID( employee.getP_Language_ID() );
	        employeeRoe.setPrintLanguageID( employee.getP_Language_ID() );

            frequency = P_Frequency.get( ctx, Period.getP_Frequency_ID(), null);
            
            employeeRoe.setP_Frequency_ID(Period.getP_Frequency_ID());
            
            employeeRoe.setPayPeriodType( frequency.getPayPeriodType() );
            
            if ( request.getParameter("FirstDayWorked") != null && request.getParameter("FirstDayWorked").trim().length() != 0 )
            	employeeRoe.setFirstDayWorked(this.getDate(request.getParameter("FirstDayWorked")));
            else
               	employeeRoe.setFirstDayWorked( P_Employee_Roe.getFirstDayWorked( ctx, employee.getP_Employee_ID() ) );

            if ( request.getParameter("LastDayForWhichPaid") != null && request.getParameter("LastDayForWhichPaid").trim().length() != 0 )
            	employeeRoe.setLastDayForWhichPaid(this.getDate(request.getParameter("LastDayForWhichPaid")));
            else
               	employeeRoe.setLastDayForWhichPaid( P_Employee_Roe.getLastDayForWhichPaid( ctx, employee.getP_Employee_ID()) );
            	
            if ( request.getParameter("FinalPayPeriodEndingDate") != null && request.getParameter("FinalPayPeriodEndingDate").trim().length() != 0 )
               	employeeRoe.setFinalPayPeriodEndingDate(this.getDate(request.getParameter("FinalPayPeriodEndingDate")));
            else
            	employeeRoe.setFinalPayPeriodEndingDate( P_Employee_Roe.getFinalPayPeriodEndingDate( ctx, employee.getP_Employee_ID()));
   			// update record.
   			employeeRoe.fillRoe();
        }
        else
        {
            employeeID = employeeRoe.getP_Employee_ID();

            employee = P_Employee.get( ctx, employeeRoe.getP_Employee_ID(), null);
            employer = P_Employer.get( ctx, employeeRoe.getP_Employer_ID(), null);
            frequency = P_Frequency.get( ctx, employeeRoe.getP_Frequency_ID(), null);
            jobTitle = P_Job_Title.get( ctx,  employeeRoe.getP_Job_Title_ID(), null );
        }
        
		PaymentGroup = P_Payment_Group.get( ctx, employee.getP_Payment_Group_ID(), null);


        String action = request.getParameter("action");
        if(action != null && ! employeeRoe.isTransfered())
        {


            if(action.equals("save") || action.equals("updated"))
            {
                // L'utilisateur veut sauvegarder. On récupère donc les informations du dernier
                // post et on crée un nouvel enregistrement dans la table P_Employee_ROE ou
                // on le met à jour dépendemment du employeeRoeId passé en post
               	employeeRoe.setP_Employee_ID( employee.getP_Employee_ID() );
                employeeRoe.setP_Employer_ID(employee.getP_Employer_ID());
                employeeRoe.setP_Job_Title_ID(employee.getP_Job_Title_ID());

                employeeRoe.setP_Frequency_ID( frequency.getP_Frequency_ID() );

               	employeeRoe.setTransfered(false);
               			
                employeeRoe.setFirstDayWorked(this.getDate(request.getParameter("FirstDayWorked")));
                employeeRoe.setLastDayForWhichPaid(this.getDate(request.getParameter("LastDayForWhichPaid")));
                
                employeeRoe.setFinalPayPeriodEndingDate(this.getDate(request.getParameter("FinalPayPeriodEndingDate")));
                employeeRoe.setCommentsLine01(PgiUtil.convertHTMLString(this.getString(request.getParameter("CommentsLine01"))));
                employeeRoe.setCommentsLine02(PgiUtil.convertHTMLString(this.getString(request.getParameter("CommentsLine02"))));
                employeeRoe.setCommentsLine03(PgiUtil.convertHTMLString(this.getString(request.getParameter("CommentsLine03"))));
                employeeRoe.setCommentsLine04(PgiUtil.convertHTMLString(this.getString(request.getParameter("CommentsLine04"))));
                if ( request.getParameter("ExpectedDateOfRecall") != null && request.getParameter("ExpectedDateOfRecall").trim().length() != 0 )
                	employeeRoe.setExpectedDateOfRecall(this.getDate(request.getParameter("ExpectedDateOfRecall")));
                
                employeeRoe.setExpectedRecallCode(this.getString(request.getParameter("ExpectedRecallCode")));
                employeeRoe.setOtherMoniesAmount01(this.getBigDecimal(request.getParameter("OtherMoniesAmount01")));
                employeeRoe.setOtherMoniesAmount02(this.getBigDecimal(request.getParameter("OtherMoniesAmount02")));
                employeeRoe.setOtherMoniesAmount03(this.getBigDecimal(request.getParameter("OtherMoniesAmount03")));
                employeeRoe.setOtherMoniesCode01(this.getString(request.getParameter("OtherMoniesCode01")));
                employeeRoe.setOtherMoniesCode02(this.getString(request.getParameter("OtherMoniesCode02")));
                employeeRoe.setOtherMoniesCode03(this.getString(request.getParameter("OtherMoniesCode03")));
                employeeRoe.setPaidSickAmount(this.getBigDecimal(request.getParameter("PaidSickAmount")));
                if ( request.getParameter("PaidSickDate") != null && request.getParameter("PaidSickDate").trim().length() != 0 )
                	employeeRoe.setPaidSickDate(this.getDate(request.getParameter("PaidSickDate")));
                employeeRoe.setPaidSickPeriod(this.getString(request.getParameter("PaidSickPeriod")));
                employeeRoe.setReasonForIssuingThisRoe(this.getString(request.getParameter("ReasonForIssuingThisROE")));
                employeeRoe.setStatutoryHolidayPayAmount01(this.getBigDecimal(request.getParameter("StatutoryHolidayPayAmount01")));
                employeeRoe.setStatutoryHolidayPayAmount02(this.getBigDecimal(request.getParameter("StatutoryHolidayPayAmount02")));
                employeeRoe.setStatutoryHolidayPayAmount03(this.getBigDecimal(request.getParameter("StatutoryHolidayPayAmount03")));
                if ( request.getParameter("StatutoryHolidayPayDate01") != null && request.getParameter("StatutoryHolidayPayDate01").trim().length() != 0 )
                	employeeRoe.setStatutoryHolidayPaydate01(this.getDate(request.getParameter("StatutoryHolidayPayDate01")));
                if ( request.getParameter("StatutoryHolidayPayDate02") != null && request.getParameter("StatutoryHolidayPayDate02").trim().length() != 0 )
                	employeeRoe.setStatutoryHolidayPaydate02(this.getDate(request.getParameter("StatutoryHolidayPayDate02")));
                if ( request.getParameter("StatutoryHolidayPayDate03") != null && request.getParameter("StatutoryHolidayPayDate03").trim().length() != 0 )
                	employeeRoe.setStatutoryHolidayPaydate03(this.getDate(request.getParameter("StatutoryHolidayPayDate03")));
                employeeRoe.setVacationPayAmount(this.getBigDecimal(request.getParameter("VacationPayAmount")));
                employeeRoe.setTotalInsurableHours(this.getBigDecimal(request.getParameter("TotalInsurableHours")));
                employeeRoe.setTotalInsurableEarnings(this.getBigDecimal(request.getParameter("TotalInsurableEarnings")));
                employeeRoe.setEarningsForPayPeriod01(this.getBigDecimal(request.getParameter("EarningsForPayPeriod01")));
                employeeRoe.setEarningsForPayPeriod02(this.getBigDecimal(request.getParameter("EarningsForPayPeriod02")));
                employeeRoe.setEarningsForPayPeriod03(this.getBigDecimal(request.getParameter("EarningsForPayPeriod03")));
                employeeRoe.setEarningsForPayPeriod04(this.getBigDecimal(request.getParameter("EarningsForPayPeriod04")));
                employeeRoe.setEarningsForPayPeriod05(this.getBigDecimal(request.getParameter("EarningsForPayPeriod05")));
                employeeRoe.setEarningsForPayPeriod06(this.getBigDecimal(request.getParameter("EarningsForPayPeriod06")));
                employeeRoe.setEarningsForPayPeriod07(this.getBigDecimal(request.getParameter("EarningsForPayPeriod07")));
                employeeRoe.setEarningsForPayPeriod08(this.getBigDecimal(request.getParameter("EarningsForPayPeriod08")));
                employeeRoe.setEarningsForPayPeriod09(this.getBigDecimal(request.getParameter("EarningsForPayPeriod09")));
                employeeRoe.setEarningsForPayPeriod10(this.getBigDecimal(request.getParameter("EarningsForPayPeriod10")));
                employeeRoe.setEarningsForPayPeriod11(this.getBigDecimal(request.getParameter("EarningsForPayPeriod11")));
                employeeRoe.setEarningsForPayPeriod12(this.getBigDecimal(request.getParameter("EarningsForPayPeriod12")));
                employeeRoe.setEarningsForPayPeriod13(this.getBigDecimal(request.getParameter("EarningsForPayPeriod13")));
                employeeRoe.setEarningsForPayPeriod14(this.getBigDecimal(request.getParameter("EarningsForPayPeriod14")));
                employeeRoe.setEarningsForPayPeriod15(this.getBigDecimal(request.getParameter("EarningsForPayPeriod15")));
                employeeRoe.setEarningsForPayPeriod16(this.getBigDecimal(request.getParameter("EarningsForPayPeriod16")));
                employeeRoe.setEarningsForPayPeriod17(this.getBigDecimal(request.getParameter("EarningsForPayPeriod17")));
                employeeRoe.setEarningsForPayPeriod18(this.getBigDecimal(request.getParameter("EarningsForPayPeriod18")));
                employeeRoe.setEarningsForPayPeriod19(this.getBigDecimal(request.getParameter("EarningsForPayPeriod19")));
                employeeRoe.setEarningsForPayPeriod20(this.getBigDecimal(request.getParameter("EarningsForPayPeriod20")));
                employeeRoe.setEarningsForPayPeriod21(this.getBigDecimal(request.getParameter("EarningsForPayPeriod21")));
                employeeRoe.setEarningsForPayPeriod22(this.getBigDecimal(request.getParameter("EarningsForPayPeriod22")));
                employeeRoe.setEarningsForPayPeriod23(this.getBigDecimal(request.getParameter("EarningsForPayPeriod23")));
                employeeRoe.setEarningsForPayPeriod24(this.getBigDecimal(request.getParameter("EarningsForPayPeriod24")));
                employeeRoe.setEarningsForPayPeriod25(this.getBigDecimal(request.getParameter("EarningsForPayPeriod25")));
                employeeRoe.setEarningsForPayPeriod26(this.getBigDecimal(request.getParameter("EarningsForPayPeriod26")));
                employeeRoe.setEarningsForPayPeriod27(this.getBigDecimal(request.getParameter("EarningsForPayPeriod27")));
                employeeRoe.setEarningsForPayPeriod28(this.getBigDecimal(request.getParameter("EarningsForPayPeriod28")));
                employeeRoe.setEarningsForPayPeriod29(this.getBigDecimal(request.getParameter("EarningsForPayPeriod29")));
                employeeRoe.setEarningsForPayPeriod30(this.getBigDecimal(request.getParameter("EarningsForPayPeriod30")));
                employeeRoe.setEarningsForPayPeriod31(this.getBigDecimal(request.getParameter("EarningsForPayPeriod31")));
                employeeRoe.setEarningsForPayPeriod32(this.getBigDecimal(request.getParameter("EarningsForPayPeriod32")));
                employeeRoe.setEarningsForPayPeriod33(this.getBigDecimal(request.getParameter("EarningsForPayPeriod33")));
                employeeRoe.setEarningsForPayPeriod34(this.getBigDecimal(request.getParameter("EarningsForPayPeriod34")));
                employeeRoe.setEarningsForPayPeriod35(this.getBigDecimal(request.getParameter("EarningsForPayPeriod35")));
                employeeRoe.setEarningsForPayPeriod36(this.getBigDecimal(request.getParameter("EarningsForPayPeriod36")));
                employeeRoe.setEarningsForPayPeriod37(this.getBigDecimal(request.getParameter("EarningsForPayPeriod37")));
                employeeRoe.setEarningsForPayPeriod38(this.getBigDecimal(request.getParameter("EarningsForPayPeriod38")));
                employeeRoe.setEarningsForPayPeriod39(this.getBigDecimal(request.getParameter("EarningsForPayPeriod39")));
                employeeRoe.setEarningsForPayPeriod40(this.getBigDecimal(request.getParameter("EarningsForPayPeriod40")));
                employeeRoe.setEarningsForPayPeriod41(this.getBigDecimal(request.getParameter("EarningsForPayPeriod41")));
                employeeRoe.setEarningsForPayPeriod42(this.getBigDecimal(request.getParameter("EarningsForPayPeriod42")));
                employeeRoe.setEarningsForPayPeriod43(this.getBigDecimal(request.getParameter("EarningsForPayPeriod43")));
                employeeRoe.setEarningsForPayPeriod44(this.getBigDecimal(request.getParameter("EarningsForPayPeriod44")));
                employeeRoe.setEarningsForPayPeriod45(this.getBigDecimal(request.getParameter("EarningsForPayPeriod45")));
                employeeRoe.setEarningsForPayPeriod46(this.getBigDecimal(request.getParameter("EarningsForPayPeriod46")));
                employeeRoe.setEarningsForPayPeriod47(this.getBigDecimal(request.getParameter("EarningsForPayPeriod47")));
                employeeRoe.setEarningsForPayPeriod48(this.getBigDecimal(request.getParameter("EarningsForPayPeriod48")));
                employeeRoe.setEarningsForPayPeriod49(this.getBigDecimal(request.getParameter("EarningsForPayPeriod49")));
                employeeRoe.setEarningsForPayPeriod50(this.getBigDecimal(request.getParameter("EarningsForPayPeriod50")));
                employeeRoe.setEarningsForPayPeriod51(this.getBigDecimal(request.getParameter("EarningsForPayPeriod51")));
                employeeRoe.setEarningsForPayPeriod52(this.getBigDecimal(request.getParameter("EarningsForPayPeriod52")));
                employeeRoe.setEarningsForPayPeriod53(this.getBigDecimal(request.getParameter("EarningsForPayPeriod53")));
                employeeRoe.setEmployeeOccupation( this.getString(request.getParameter("EmployeeOccupation")) );
                employeeRoe.setCanadaRevenueBusinessNumber(employer.getCanadaRevenueBusinessNumber());
                employeeRoe.setAD_User_ID(((IUserInfo)this.session.getAttribute("userInfo")).getUserId());



                employeeRoe.setSignataire( this.getString(request.getParameter("Signataire")));
                employeeRoe.setPhone     ( this.getString(request.getParameter("Phone")));
                employeeRoe.setPhoneExt  ( this.getString(request.getParameter("PhoneExt")));
                employeeRoe.setPhone2    ( this.getString(request.getParameter("Phone2")));
                employeeRoe.setPhoneExt2 ( this.getString(request.getParameter("PhoneExt2")));
                employeeRoe.setSignataire2( this.getString(request.getParameter("Signataire2")));

                employeeRoe.setFirstNameContactPerson(this.getString(request.getParameter("FirstNameContactPerson")));
		        employeeRoe.setLastNameContactPerson(this.getString(request.getParameter("LastNameContactPerson")));
//		        employeeRoe.setP_Period_ID(Integer.parseInt(request.getParameter("periodId")));
		        employeeRoe.setP_Language_ID(Integer.parseInt(request.getParameter("LanguageId") ));
		        employeeRoe.setPrintLanguageID(Integer.parseInt(request.getParameter("PrintLanguage")));
		        employeeRoe.setPhoneAreaCode(this.getString(request.getParameter("PhoneAreaCode")));
		        employeeRoe.setPhoneAreaCode2(this.getString(request.getParameter("PhoneAreaCode2")));
		        employeeRoe.setPayPeriodType(this.getString(request.getParameter("PayPeriodType")));
                
                //2013.10.10
            	System.out.println("OvertimeIncluded :" + request.getParameter("OvertimeIncluded") );
                
                if ( request.getParameter("OvertimeIncluded") != null )
	                employeeRoe.setisOvertimeIncluded( true );
	            else
	                employeeRoe.setisOvertimeIncluded( false );
	            
//                this.ftra(employeeRoe);

            	if(action.equals("updated"))
                {
        			employeeRoe.fillRoe();
                }

                if( ! employeeRoe.save())
                {
		            log.log (Level.SEVERE, "Erreur dans la sauvegarde de P_Employee_Roe_ID = " + request.getParameter("employeeRoeId") );

                }
                
                
                // m.a.j du nouvel ID.
                roeID = employeeRoe.getP_Employee_Roe_ID();
//                RoeID = String.valueOf( employeeRoe.getP_Employee_Roe_ID() );
                
                String webAppUrl = PgiUtil.getSolsticeParameter(ctx, "WEBAPPURL");
                response.sendRedirect( webAppUrl + "/releveEmploi/releve.jsp?employeeRoeId=" + roeID );
                
            }
            else if(action.equals("export"))
            {
	            employeeRoe = new P_Employee_Roe( ctx, roeID, null);
        		employeeRoe.exportRoe();
            }
            // 2013.04.23 ajout la possibilité de faire un amandé.
            else if(action.equals("copy"))
            {
           		P_Employee_Roe roe = P_Employee_Roe.copyFrom( ctx, roeID, null);
           		this.response.sendRedirect("releve.jsp?employeeRoeId=" + roe.getP_Employee_Roe_ID() ) ;
            }
            
            else if(action.equals("delete"))
            {
                // L'utilisateur veut supprimer le relevé d'emploi. On récupère donc le numéro
                // du relevé et on le supprime. Par la suite, on redirige l'utilisateur vers
                // le formulaire de recherche pour l'employé courant
                int employeeRoeId = Integer.parseInt(request.getParameter("employeeRoeId"));
                DB.executeUpdate("delete from P_Employee_Roe where P_Employee_Roe_ID = " + employeeRoeId, null);
                try
                {
                    this.response.sendRedirect("index.jsp?employeeId=" + request.getParameter("employeeId"));
                }
                catch (IOException e)
                {
                    e.printStackTrace(System.out);
                }
            }

        }

        tpl.assign("hiddenEmployeeRoeId", employeeRoe.getP_Employee_Roe_ID());
		tpl.assign("hiddenEmployeeID", employee.getP_Employee_ID());

        String Language = "fr_CA"; // this.getLanguage();
        
        tpl.assign("OvertimeIncluded", "Inclure les heures de temps supplémentaire mise en banque durant la période du présent relevé.");
		tpl.assign("TITLE", Msg.getMsg(Language, "ROE_Title", true ));  // Record of Employment
		tpl.assign("Label01", Msg.getElement(Language, "P_Employee_Roe_ID", isSOTrx));
		tpl.assign("Label02", Msg.getMsg(Language, "ROE_Block2", true ));
		tpl.assign("Label03", Msg.getMsg(Language, "ROE_Block3", true ));
		tpl.assign("Label04", Msg.getMsg(Language, "ROE_Block4", true ));
		tpl.assign("Label05", Msg.getMsg(Language, "ROE_Block5", true ));
		tpl.assign("Label06", Msg.getMsg(Language, "ROE_Block6", true ));	
		tpl.assign("Label07", Msg.getElement(Language, "Postal", isSOTrx));
		tpl.assign("Label08", Msg.getElement(Language, "Sin", isSOTrx));
		tpl.assign("Label09", Msg.getMsg(Language, "ROE_Block9", true));
		tpl.assign("Label10", Msg.getElement(Language, "FirstDayWorked", isSOTrx));
		tpl.assign("Label11", Msg.getElement(Language, "LastDayForWhichPaid", isSOTrx));
		tpl.assign("Label12", Msg.getElement(Language, "FinalPayPeriodEndingDate", isSOTrx)); //"Date de fin de la dernière période de paie");
		tpl.assign("Label13", Msg.getElement(Language, "EmployeeOccupation", isSOTrx));
		tpl.assign("Label14", Msg.getElement(Language, "ExpectedDateOfRecall", isSOTrx)); //"Date prévue de rappel");
		tpl.assign("Label15a", Msg.getElement(Language, "TotalInsurableHours", isSOTrx)); 
		tpl.assign("Label15b", Msg.getElement(Language, "TotalInsurableEarnings", isSOTrx)); 
		tpl.assign("Label15c", Msg.getMsg(Language, "ROE_Block15C", true));
		tpl.assign("Label15c2", Msg.getMsg(Language, "ROE_Block15C2", true));
		tpl.assign("Label16",  Msg.getElement(Language, "ReasonForIssuingThisRoe" , isSOTrx)); //"Raison du présent relevé d'emploi");
		tpl.assign("LabelInfo", Msg.getMsg(Language, "ROE_Block16a", true)); 

		tpl.assign("Label17", Msg.getMsg(Language, "ROE_Block17", true)); //"À compléter seulement si paiements (autres que le salaire habituel) payé au cours de, en prévision de ou après la dernière période de paye.");
		tpl.assign("Label17a", Msg.getMsg(Language, "ROE_Block17a", true)); 
		tpl.assign("Label17b", Msg.getMsg(Language, "ROE_Block17b", true)); 
		tpl.assign("Label17c", Msg.getMsg(Language, "ROE_Block17c", true)); 
		tpl.assign("Label18", Msg.getMsg(Language, "ROE_Block18", true));//"Observation");
		tpl.assign("Label19", Msg.getMsg(Language, "ROE_Block19", true));//"À compléter seulement si congé de maladie, de maternité ou parental ou indemnité d'assurance salaire (payable aprè le dernier jour de travail).");
		tpl.assign("Label19a", Msg.getMsg(Language, "ROE_Block19a", true));//"Nom du signataire");
		tpl.assign("Label19b", Msg.getMsg(Language, "ROE_Block19b", true));
		tpl.assign("Label19c", Msg.getMsg(Language, "ROE_Block19c", true));

		tpl.assign("LabelSignataire", Msg.getMsg(Language, "ROE_Block19a", true));//"Nom du signataire"

		tpl.assign("Label20", Msg.getMsg(Language, "ROE_Block20", true)); //"Communication préférée en");
		tpl.assign("Label21", Msg.getMsg(Language, "ROE_Block21", true)); //"N&deg; de téléphone");
		tpl.assign("Label22", Msg.getMsg(Language, "ROE_Block22", true)); // "JE RECONNAIS QUE TOUTE FAUSSE DÉCLARATION CONSTITUE UNE INFRACTION ET J'ATTESTE, PAR LES PRÉSENTES, QUE TOUTES LES DÉCLARATIONS DE CE FORMULAIRE SONT VÉRIDIQUES.");
		tpl.assign("Label23", Msg.getMsg(Language, "ROE_Block23", true));
		tpl.assign("OverLib", Msg.getMsg(Language, "ROE_OverLib", true));
		
		tpl.assign("Search", Msg.getMsg(Language, "Search", true));
		if ( ! employeeRoe.isTransfered() )
		{
			tpl.assign("Delete", Msg.getMsg(Language, "Delete", true));
			tpl.assign("Save", Msg.getMsg(Language, "Save", true));
			tpl.assign("Copy", Msg.getMsg(Language, "Copy", true));
			tpl.assign("Updated", Msg.getMsg(Language, "Updated", true));
			tpl.parse("main.update");
		}

		tpl.assign("LabelPaidSickPeriodD", Msg.getMsg(Language, "ROE_PaidSickPeriodD", true));
		tpl.assign("LabelPaidSickPeriodW", Msg.getMsg(Language, "ROE_PaidSickPeriodW", true));
		tpl.assign("LabelExpectedRecallCodeY", Msg.getMsg(Language, "ROE_ExpectedRecallCodeY", true));
		tpl.assign("LabelExpectedRecallCodeN", Msg.getMsg(Language, "ROE_ExpectedRecallCodeN", true));
		tpl.assign("LabelExpectedRecallCodeU", Msg.getMsg(Language, "ROE_ExpectedRecallCodeU", true));

		this.loadROE( );
	
		tpl.parse("main");
		return (tpl.out());
	}
    
	
    private Timestamp getDate(String date)
    {
    	if ( date == null)
    		return null;
    	
    	//2012.07.19
    	if ( date.trim().length() < 8 )
    		return null;
    	
        try
        {
            Calendar calendar = PgiUtil.stringToDate(date);
            return new Timestamp(calendar.getTimeInMillis());
        }
        catch (Exception e)
        {
            log.log (Level.SEVERE,  " Erreur dans la conversion vers un Timestamp "  , e);
            return null;
        }
    }
    
    private String getString(String string)
    {
    	if ( string == null)
    		return null;
   	
    	if(string == null || string.trim().length() == 0)
            return null;

//    	string = string.replaceAll("$", "\\$");
 //   	string = string.replaceAll("'", "\\'");
 //   	string = string.replaceAll("\"", "\\\"");
    	
    	return string.trim();
    }
    
    private BigDecimal getBigDecimal(String value)
    {
    	if ( value == null)
    		return null;

    	try
        {
            // On doit supprimer les blanc
            double val = Double.parseDouble(value);
            BigDecimal amount = new BigDecimal(val);
            amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
            return amount;
        }
        catch(Exception e)
        {
            log.log (Level.SEVERE, "Erreur dans la conversion vers un BigDecimal. " , e);
            return null;
        }
    }

    
	public String ComboBoxLanguage( int P_Language_ID, boolean isPrintingLanguage )
	{
		String result  = null;
		//String sql = "select P_Language_ID, Name From P_Language Where value like '%CA' order by name ";
		String sql = "select p_language.p_language_id, isnull( p_language_trl.name, p_language.name ) as name"
			+ " from p_language left outer join p_language_trl"
			+ " on p_language_trl.p_language_id =  p_language.p_language_id"
			+ " and ad_language = '" + ctx.getProperty("#AD_Language")+"'"
			+ " where p_language.value like '%CA'";
		
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (isPrintingLanguage)
				result  = "<select name=\"PrintLanguage\" id=\"PrintLanguage\"  onKeyPress=\"autoSelect(this);\">";
			else
				result  = "<select name=\"LanguageId\" id=\"LanguageId\"  onKeyPress=\"autoSelect(this);\">";
			/*while (rs.next ())
			{
				if ( rs.getInt(1) == P_Language_ID )
					result += "<option value=\"" + rs.getString(1) + "\" selected > " + rs.getString(2) + "</option> \n";
				else
					result += "<option value=\"" + rs.getString(1) + "\" > " + rs.getString(2) + "</option> \n";
			}*/
			while (rs.next())
			{
				if ( rs.getInt(1) == P_Language_ID )
					result += "<option value=\"" + rs.getString(1) + "\" selected > " + rs.getString(2) + "</option> \n";
				else
					result += "<option value=\"" + rs.getString(1) + "\" > " + rs.getString(2) + "</option> \n";
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			result += "</select>";

		}
		catch (Exception e)
		{
            log.log (Level.SEVERE, "Erreur releve.jsp read language : "  , e);
		}

		return result;

	}
%>