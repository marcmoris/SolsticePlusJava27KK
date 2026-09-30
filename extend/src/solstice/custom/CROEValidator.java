/*
 * Created on 23 juin 2005
 */
package solstice.custom;

/**
 * @author frafor01
 *
 * Cette classe sert à valider un enregistrement dans la table P_Employee_Roe. Il
 * est également possible de recevoir les messages d'erreur de validation pour les
 * afficher dans une page web.
 */
public class CROEValidator
{/*
    public static void main(String[] args)
    {
        String TotalInsurableEarnings = "123";
	    if( (TotalInsurableEarnings != null  && !TotalInsurableEarnings.equals("") && !validMontant(TotalInsurableEarnings, new BigDecimal(0.00), new BigDecimal(99999.99))))
        {
            System.out.println("Le nombre d'heures assurables totales est obligatoire et doit être un nombre valide (case 15a)<br>");
        }
        System.out.println("wtf");
    }
    
    private boolean m_validationResult = false;
    private StringBuffer m_message = new StringBuffer();
    
    private String CommentsLine01;
    private String CommentsLine02;
    private String CommentsLine03;
    private String CommentsLine04;
    private String EarningsForPayPeriod01;
    private String EarningsForPayPeriod02;
    private String EarningsForPayPeriod03;
    private String EarningsForPayPeriod04;
    private String EarningsForPayPeriod05;
    private String EarningsForPayPeriod06;
    private String EarningsForPayPeriod07;
    private String EarningsForPayPeriod08;
    private String EarningsForPayPeriod09;
    private String EarningsForPayPeriod10;
    private String EarningsForPayPeriod11;
    private String EarningsForPayPeriod12;
    private String EarningsForPayPeriod13;
    private String EarningsForPayPeriod14;
    private String EarningsForPayPeriod15;
    private String EarningsForPayPeriod16;
    private String EarningsForPayPeriod17;
    private String EarningsForPayPeriod18;
    private String EarningsForPayPeriod19;
    private String EarningsForPayPeriod20;
    private String EarningsForPayPeriod21;
    private String EarningsForPayPeriod22;
    private String EarningsForPayPeriod23;
    private String EarningsForPayPeriod24;
    private String EarningsForPayPeriod25;
    private String EarningsForPayPeriod26;
    private String EarningsForPayPeriod27;
    private String EmployeeOccupation;
    private String ExpectedDateOfRecall;
    private String ExpectedRecallCode;
    private String FinalPayPeriodEndingDate;
    private String FirstDayWorked;
    private String LastDayForWhichPaid;
    private String OtherMoniesAmount01;
    private String OtherMoniesAmount02;
    private String OtherMoniesAmount03;
    private String OtherMoniesCode01;
    private String OtherMoniesCode02;
    private String OtherMoniesCode03;
    private String P_Employee_ID;
    private String P_Employee_Roe_ID;
    private String PaidSickAmount;
    private String PaidSickDate;
    private String PaidSickPeriod;
    private String ReasonForIssuingThisRoe  ;
    private String StatutoryHolidayPayAmount01;
    private String StatutoryHolidayPayAmount02;
    private String StatutoryHolidayPayAmount03;
    private String StatutoryHolidayPaydate01;
    private String StatutoryHolidayPaydate02;
    private String StatutoryHolidayPaydate03;
    private String TotalInsurableEarnings;
    private String TotalInsurableHours;
    private String VacationPayAmount;
    private String FrequencyValue; // Utile pour certaines validations

    // Constructeur
    // Il faut entrer tous les champs nécessaire pour créer un enregistrement dans la
    // table P_Employee_Roe
    public CROEValidator(
            String pCommentsLine01,
            String pCommentsLine02,
            String pCommentsLine03,
            String pCommentsLine04,
            String pEarningsForPayPeriod01,
            String pEarningsForPayPeriod02,
            String pEarningsForPayPeriod03,
            String pEarningsForPayPeriod04,
            String pEarningsForPayPeriod05,
            String pEarningsForPayPeriod06,
            String pEarningsForPayPeriod07,
            String pEarningsForPayPeriod08,
            String pEarningsForPayPeriod09,
            String pEarningsForPayPeriod10,
            String pEarningsForPayPeriod11,
            String pEarningsForPayPeriod12,
            String pEarningsForPayPeriod13,
            String pEarningsForPayPeriod14,
            String pEarningsForPayPeriod15,
            String pEarningsForPayPeriod16,
            String pEarningsForPayPeriod17,
            String pEarningsForPayPeriod18,
            String pEarningsForPayPeriod19,
            String pEarningsForPayPeriod20,
            String pEarningsForPayPeriod21,
            String pEarningsForPayPeriod22,
            String pEarningsForPayPeriod23,
            String pEarningsForPayPeriod24,
            String pEarningsForPayPeriod25,
            String pEarningsForPayPeriod26,
            String pEarningsForPayPeriod27,
            String pEmployeeOccupation,
            String pExpectedDateOfRecall,
            String pExpectedRecallCode,
            String pFinalPayPeriodEndingDate,
            String pFirstDayWorked,
            String pLastDayForWhichPaid,
            String pOtherMoniesAmount01,
            String pOtherMoniesAmount02,
            String pOtherMoniesAmount03,
            String pOtherMoniesCode01,
            String pOtherMoniesCode02,
            String pOtherMoniesCode03,
            String pP_Employee_ID,
            String pP_Employee_Roe_ID,
            String pPaidSickAmount,
            String pPaidSickDate,
            String pPaidSickPeriod,
            String pReasonForIssuingThisRoe  ,
            String pStatutoryHolidayPayAmount01,
            String pStatutoryHolidayPayAmount02,
            String pStatutoryHolidayPayAmount03,
            String pStatutoryHolidayPaydate01,
            String pStatutoryHolidayPaydate02,
            String pStatutoryHolidayPaydate03,
            String pTotalInsurableEarnings,
            String pTotalInsurableHours,
            String pVacationPayAmount,
            String pFrequencyValue)
    {
        CommentsLine01 = pCommentsLine01;
        CommentsLine02 = pCommentsLine02;
        CommentsLine03 = pCommentsLine03;
        CommentsLine04 = pCommentsLine04;
        EarningsForPayPeriod01 = pEarningsForPayPeriod01;
        EarningsForPayPeriod02 = pEarningsForPayPeriod02;
        EarningsForPayPeriod03 = pEarningsForPayPeriod03;
        EarningsForPayPeriod04 = pEarningsForPayPeriod04;
        EarningsForPayPeriod05 = pEarningsForPayPeriod05;
        EarningsForPayPeriod06 = pEarningsForPayPeriod06;
        EarningsForPayPeriod07 = pEarningsForPayPeriod07;
        EarningsForPayPeriod08 = pEarningsForPayPeriod08;
        EarningsForPayPeriod09 = pEarningsForPayPeriod09;
        EarningsForPayPeriod10 = pEarningsForPayPeriod10;
        EarningsForPayPeriod11 = pEarningsForPayPeriod11;
        EarningsForPayPeriod12 = pEarningsForPayPeriod12;
        EarningsForPayPeriod13 = pEarningsForPayPeriod13;
        EarningsForPayPeriod14 = pEarningsForPayPeriod14;
        EarningsForPayPeriod15 = pEarningsForPayPeriod15;
        EarningsForPayPeriod16 = pEarningsForPayPeriod16;
        EarningsForPayPeriod17 = pEarningsForPayPeriod17;
        EarningsForPayPeriod18 = pEarningsForPayPeriod18;
        EarningsForPayPeriod19 = pEarningsForPayPeriod19;
        EarningsForPayPeriod20 = pEarningsForPayPeriod20;
        EarningsForPayPeriod21 = pEarningsForPayPeriod21;
        EarningsForPayPeriod22 = pEarningsForPayPeriod22;
        EarningsForPayPeriod23 = pEarningsForPayPeriod23;
        EarningsForPayPeriod24 = pEarningsForPayPeriod24;
        EarningsForPayPeriod25 = pEarningsForPayPeriod25;
        EarningsForPayPeriod26 = pEarningsForPayPeriod26;
        EarningsForPayPeriod27 = pEarningsForPayPeriod27;
        EmployeeOccupation = pEmployeeOccupation;
        ExpectedDateOfRecall = pExpectedDateOfRecall;
        ExpectedRecallCode = pExpectedRecallCode;
        FinalPayPeriodEndingDate = pFinalPayPeriodEndingDate;
        FirstDayWorked = pFirstDayWorked;
        LastDayForWhichPaid = pLastDayForWhichPaid;
        OtherMoniesAmount01 = pOtherMoniesAmount01;
        OtherMoniesAmount02 = pOtherMoniesAmount02;
        OtherMoniesAmount03 = pOtherMoniesAmount03;
        OtherMoniesCode01 = pOtherMoniesCode01;
        OtherMoniesCode02 = pOtherMoniesCode02;
        OtherMoniesCode03 = pOtherMoniesCode03;
        P_Employee_ID = pP_Employee_ID;
        P_Employee_Roe_ID = pP_Employee_Roe_ID;
        PaidSickAmount = pPaidSickAmount;
        PaidSickDate = pPaidSickDate;
        PaidSickPeriod = pPaidSickPeriod;
        ReasonForIssuingThisRoe   = pReasonForIssuingThisRoe  ;
        StatutoryHolidayPayAmount01 = pStatutoryHolidayPayAmount01;
        StatutoryHolidayPayAmount02 = pStatutoryHolidayPayAmount02;
        StatutoryHolidayPayAmount03 = pStatutoryHolidayPayAmount03;
        StatutoryHolidayPaydate01 = pStatutoryHolidayPaydate01;
        StatutoryHolidayPaydate02 = pStatutoryHolidayPaydate02;
        StatutoryHolidayPaydate03 = pStatutoryHolidayPaydate03;
        TotalInsurableEarnings = pTotalInsurableEarnings;
        TotalInsurableHours = pTotalInsurableHours;
        VacationPayAmount = pVacationPayAmount;
        FrequencyValue = pFrequencyValue;
    }
    
    // Valide tous les champs
    public boolean Validate()
    {
        m_validationResult = true;
        m_message.delete(0, m_message.length());
        Timestamp first = null;
        Timestamp last = null;
        Timestamp period = null;
        
        if(!validDate(FirstDayWorked))
        {
            m_validationResult = false;
            m_message.append("Le format de la date du premier jour de travail n'est pas valide (case 10)<br>\n");
        }
        else
        {
            first = createDate(FirstDayWorked);
        }
        
        if(!validDate(LastDayForWhichPaid))
        {
            m_validationResult = false;
            m_message.append("Le format de la date du dernier jour pay&eacute; n'est pas valide (case 11)<br>\n");
        }
        else
        {
            last = createDate(LastDayForWhichPaid);
        }
        
        if(!validDate(FinalPayPeriodEndingDate))
        {
            m_validationResult = false;
            m_message.append("Le format de la date de fin de la derni&egrave;re p&eacute;riode de paye n'est pas valide (case 12)<br>\n");
        }
        else
        {
            period = createDate(FinalPayPeriodEndingDate);
        }
        
        if(!m_validationResult)
        {
            return m_validationResult; //On doit quitter la méthode maintenant car ces dates sont utilisées dans d'autres validations
        }
        
        if(first != null && last != null && period != null && (first.compareTo(last) > 0 || last.compareTo(period) > 0))
        {
            m_validationResult = false;
            m_message.append(
                    "La date du dernier jour pay&eacute; doit &ecirc;tre post&eacute;rieure ou correspondre " + 
                    "au premier jour de travail et doit &ecirc;tre inf&eacute;rieure ou correspondre &agrave; " + 
                    "la derni&egrave;re p&eacute;riode de paye (case 10, 11 et 12)<br>\n");
        }
        
        if(FrequencyValue != null && !FrequencyValue.equals(""))
        {
	        switch(FrequencyValue.charAt(0))
	        {
	        	case 'B':
	        	{
	        	    GregorianCalendar cal = new GregorianCalendar ();
	        	    cal.setTime(last);
	        	    cal.add(Calendar.DAY_OF_YEAR, 13);
	        	    if(cal.getTime().compareTo(period) < 0)
	        	    {
	        	        m_validationResult = false;
	        	        m_message.append("Quinzaine : ne peut &ecirc;tre plus de 13 jours apr&egrave;s le dernier jour pay&eacute; (case 12)<br>");
	        	    }
	        	    break;
	        	}
	        	case 'M':
	        	{   
	        	    GregorianCalendar cal = new GregorianCalendar ();
	        	    cal.setTime(last);
	        	    cal.add(Calendar.DAY_OF_YEAR, 30);
	        	    if(cal.getTime().compareTo(period) < 0)
	        	    {
	        	        m_validationResult = false;
	        	        m_message.append("Mensuelle : ne peut &ecirc;tre plus de 30 jours apr&egrave;s le dernier jour pay&eacute; (case 12)<br>");
	        	    }
	        	    if(lastDayOfMonth(period.getMonth(), period.getYear()) != period.getDay())
	        	    {
	        	        m_validationResult = false;
	        	        m_message.append("Mensuelle conventionnelle : Doit se terminer le dernier jour du mois (case 12)<br>");
	        	    }
	        	    break;
	        	}
	        	case 'O':
	        	{   
	        	    if(lastDayOfMonth(period.getMonth(), period.getYear()) == period.getDay())
	        	    {
	        	        m_validationResult = false;
	        	        m_message.append("Mensuelle non conventionnelle : Ne peut pas être le dernier jour du mois (case 12)<br>");
	        	    }
	        	    break;
	        	}
	        	case 'S':
	        	{   
	        	    GregorianCalendar cal = new GregorianCalendar ();
	        	    cal.setTime(last);
	        	    cal.add(Calendar.DAY_OF_YEAR, 15);
	        	    if(cal.getTime().compareTo(period) < 0)
	        	    {
	        	        m_validationResult = false;
	        	        m_message.append("Bimensuelle : ne peut &ecirc;tre plus de 15 jours apr&egrave;s le dernier jour pay&eacute; (case 12)<br>");
	        	    }
	        	    if(period.getDay() != 15 && lastDayOfMonth(period.getMonth(), period.getYear()) != period.getDay())
	        	    {
	        	        m_validationResult = false;
	        	        m_message.append("Bimensuelle conventionnelle : D le 15<sup>i&egrave;me</sup> jour ou le dernier jour du mois (case 12)<br>");
	        	    }
	        	    break;
	        	}
	        	case 'E':
	        	{   
	        	    if(period.getDay() == 15 || lastDayOfMonth(period.getMonth(), period.getYear()) == period.getDay())
	        	    {
	        	        m_validationResult = false;
	        	        m_message.append("Bimensuelle non conventionnelle : Ne peut pas être le 15<sup>i&egrave;me</sup> jour ou le dernier jour du mois (case 12)<br>");
	        	    }
	        	    break;
	        	}
	        	case 'H':
	        	{   
	        	    GregorianCalendar cal = new GregorianCalendar ();
	        	    cal.setTime(last);
	        	    cal.add(Calendar.DAY_OF_YEAR, 27);
	        	    if(cal.getTime().compareTo(period) < 0)
	        	    {
	        	        m_validationResult = false;
	        	        m_message.append("13 p&eacute;riodes de paie par ann&eacute;e : ne peut &ecirc;tre plus de 27 jours apr&egrave;s le dernier jour pay&eacute; (case 12)<br>");
	        	    }
	        	    break;
	        	}
	        	case 'W':
	        	{   
	        	    GregorianCalendar cal = new GregorianCalendar ();
	        	    cal.setTime(last);
	        	    cal.add(Calendar.DAY_OF_YEAR, 6);
	        	    if(cal.getTime().compareTo(period) < 0)
	        	    {
	        	        m_validationResult = false;
	        	        m_message.append("Hebdomadaire : ne peut &ecirc;tre plus de 6 jours apr&egrave;s le dernier jour pay&eacute; (case 12)<br>");
	        	    }
	        	    break;
	        	}
	        }
        }
        
        if(ExpectedRecallCode == null 
                || (!ExpectedRecallCode.equals("Y")
                        && !ExpectedRecallCode.equals("N")
                        && !ExpectedRecallCode.equals("U")
                        && !ExpectedRecallCode.equals("S")))
        {
            m_validationResult = false;
            m_message.append("Le code de la date prévue de rappel ne peut &ecirc;tre vide (case 14)<br>");
        }
        
        if(ExpectedRecallCode != null && ExpectedRecallCode.equals("Y") 
                && (this.ExpectedDateOfRecall == null || this.ExpectedDateOfRecall.equals("")))
        {
            m_validationResult = false;
            m_message.append("Si le code de rappel est Y, la date pr&eacute;vue de rappel ne doit pas &ecirc;tre vide (case 14)<br>");
        }
        
        if(this.TotalInsurableHours != null && !this.TotalInsurableHours.equals("")
                && this.TotalInsurableEarnings != null && !this.TotalInsurableHours.equals(""))
        {
	        Pattern p = Pattern.compile("[0-9]+(\\.[0-9]+){0,1}");
	        Matcher m = p.matcher(TotalInsurableHours);
	        if(!m.matches())
	        {
	            m_validationResult = false;
	            m_message.append("Le nombre d'heures assurables totales est obligatoire et doit être un nombre valide (case 15a)<br>");
	        }
	        else
	        {
	            double hours = Double.parseDouble(TotalInsurableHours);
	            if(hours > 8904)
	            {
	                m_validationResult = false;
	                m_message.append("Le nombre d'heures assurables totales doit être compris entre 0 et 8904 (case 15a)<br>");
	            }
	            else
	            {
	                GregorianCalendar cal1 = new GregorianCalendar ();
	                GregorianCalendar cal2 = new GregorianCalendar ();
	                
	                cal1.setTime(last);
	                cal2.setTime(first);
	                
	                long d1 = cal1.getTime().getTime();
	                long d2 = cal2.getTime().getTime();
	                
	                long dif = d2 - d1;
	                long days = dif / (1000*60*60*24);
	                
	                if((days + 1) * 24 > hours)
	                {
	                    m_validationResult = false;
	                    m_message.append("Le nombre d'heures assurables totales doit &ecirc;tre inf&eacute;rieur ou &eacute;gal &agrave : (Dernier jour pay&eacute; - premier jour de travail + 1) * 24 (case 15a)<br>");
	                }
	            }
	        }
        }
        else
        {
	        if(((EarningsForPayPeriod01 != null  && !EarningsForPayPeriod01.equals("") && !validMontant(EarningsForPayPeriod01, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod02 != null  && !EarningsForPayPeriod02.equals("") && !validMontant(EarningsForPayPeriod02, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod03 != null  && !EarningsForPayPeriod03.equals("") && !validMontant(EarningsForPayPeriod03, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod04 != null  && !EarningsForPayPeriod04.equals("") && !validMontant(EarningsForPayPeriod04, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod05 != null  && !EarningsForPayPeriod05.equals("") && !validMontant(EarningsForPayPeriod05, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod06 != null  && !EarningsForPayPeriod06.equals("") && !validMontant(EarningsForPayPeriod06, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod07 != null  && !EarningsForPayPeriod07.equals("") && !validMontant(EarningsForPayPeriod07, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod08 != null  && !EarningsForPayPeriod08.equals("") && !validMontant(EarningsForPayPeriod08, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod09 != null  && !EarningsForPayPeriod09.equals("") && !validMontant(EarningsForPayPeriod09, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod10 != null  && !EarningsForPayPeriod10.equals("") && !validMontant(EarningsForPayPeriod10, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod11 != null  && !EarningsForPayPeriod11.equals("") && !validMontant(EarningsForPayPeriod11, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod12 != null  && !EarningsForPayPeriod12.equals("") && !validMontant(EarningsForPayPeriod12, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod13 != null  && !EarningsForPayPeriod13.equals("") && !validMontant(EarningsForPayPeriod13, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod14 != null  && !EarningsForPayPeriod14.equals("") && !validMontant(EarningsForPayPeriod14, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod15 != null  && !EarningsForPayPeriod15.equals("") && !validMontant(EarningsForPayPeriod15, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod16 != null  && !EarningsForPayPeriod16.equals("") && !validMontant(EarningsForPayPeriod16, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod17 != null  && !EarningsForPayPeriod17.equals("") && !validMontant(EarningsForPayPeriod17, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod18 != null  && !EarningsForPayPeriod18.equals("") && !validMontant(EarningsForPayPeriod18, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod19 != null  && !EarningsForPayPeriod19.equals("") && !validMontant(EarningsForPayPeriod19, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod20 != null  && !EarningsForPayPeriod20.equals("") && !validMontant(EarningsForPayPeriod20, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod21 != null  && !EarningsForPayPeriod21.equals("") && !validMontant(EarningsForPayPeriod21, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod22 != null  && !EarningsForPayPeriod22.equals("") && !validMontant(EarningsForPayPeriod22, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod23 != null  && !EarningsForPayPeriod23.equals("") && !validMontant(EarningsForPayPeriod23, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod24 != null  && !EarningsForPayPeriod24.equals("") && !validMontant(EarningsForPayPeriod24, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod25 != null  && !EarningsForPayPeriod25.equals("") && !validMontant(EarningsForPayPeriod25, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod26 != null  && !EarningsForPayPeriod26.equals("") && !validMontant(EarningsForPayPeriod26, new BigDecimal(0.00), new BigDecimal(99999.99)))
	                || (EarningsForPayPeriod27 != null  && !EarningsForPayPeriod27.equals("") && !validMontant(EarningsForPayPeriod27, new BigDecimal(0.00), new BigDecimal(99999.99)))))
	        {
	            m_validationResult = false;
	            m_message.append("Les montant doivent obligatoirement suivre le format 99999.99 (case 15c)<br>");
	        }
        }
        
        if((OtherMoniesCode01 != null && OtherMoniesCode01 != "" && OtherMoniesAmount01 != null  && !OtherMoniesAmount01.equals("") && !validMontant(OtherMoniesAmount01, new BigDecimal(0.00), new BigDecimal(99999.99)))
				|| (OtherMoniesCode02 != null && OtherMoniesCode02 != "" && OtherMoniesAmount02 != null  && !OtherMoniesAmount02.equals("") && !validMontant(OtherMoniesAmount02, new BigDecimal(0.00), new BigDecimal(99999.99)))
				|| (OtherMoniesCode03 != null && OtherMoniesCode03 != "" && OtherMoniesAmount03 != null  && !OtherMoniesAmount03.equals("") && !validMontant(OtherMoniesAmount03, new BigDecimal(0.00), new BigDecimal(99999.99))))
        {
            m_validationResult = false;
            m_message.append("Les montant doivent obligatoirement suivre le format 99999.99 et sont obligatoires si le code est saisi (case 17c)<br>");
        }
        
        if( (PaidSickAmount != null  && !PaidSickAmount.equals("") && !validMontant(PaidSickAmount, new BigDecimal(0.00), new BigDecimal(99999.99))))
        {
            m_validationResult = false;
            m_message.append("Les montant doivent obligatoirement suivre le format 99999.99 (case 19)<br>");
        }
	    
        if( (StatutoryHolidayPaydate01 != null && !StatutoryHolidayPaydate01.equals("") && StatutoryHolidayPayAmount01 != null  && !StatutoryHolidayPayAmount01.equals("") && !validMontant(StatutoryHolidayPayAmount01, new BigDecimal(0.00), new BigDecimal(99999.99)))
				|| (StatutoryHolidayPaydate02 != null && !StatutoryHolidayPaydate02.equals("") && StatutoryHolidayPayAmount02 != null  && !StatutoryHolidayPayAmount02.equals("") && !validMontant(StatutoryHolidayPayAmount02, new BigDecimal(0.00), new BigDecimal(99999.99)))
				|| (StatutoryHolidayPaydate03 != null && !StatutoryHolidayPaydate03.equals("") && StatutoryHolidayPayAmount03 != null  && !StatutoryHolidayPayAmount03.equals("") && !validMontant(StatutoryHolidayPayAmount03, new BigDecimal(0.00), new BigDecimal(99999.99))))
	    {
            m_validationResult = false;
            m_message.append("Les montant doivent obligatoirement suivre le format 99999.99 et sont obligatoires si la date est saisie (case 17b)<br>");
	    }
	    
	    if( (TotalInsurableEarnings != null  && !TotalInsurableEarnings.equals("") && !validMontant(TotalInsurableEarnings, new BigDecimal(0.00), new BigDecimal(99999.99))))
	    {
            m_validationResult = false;
            m_message.append("Les montant doivent obligatoirement suivre le format 99999.99 (case 15a)<br>");
	    }
	    
	    if( (VacationPayAmount != null  && !VacationPayAmount.equals("") && !validMontant(VacationPayAmount, new BigDecimal(0.00), new BigDecimal(99999.99))))
	    {
            m_validationResult = false;
            m_message.append("Les montant doivent obligatoirement suivre le format 99999.99 (case 17a)<br>");
	    }
	    
	    return m_validationResult;
    }
    
    public boolean GetLastValidated()
    {
        return m_validationResult;
    }
    
    // Valide le montant selon le format 99999.99
    private static boolean validMontant(String montant, BigDecimal minValue, BigDecimal maxValue)
    {
        Pattern p = Pattern.compile("[0-9]+\\.[0-9]{2}");
        Matcher m = p.matcher(montant);
        if(m.matches())
        {
            BigDecimal amount = createMontant(montant);
            if(amount.compareTo( minValue ) >= 0 && amount.compareTo( maxValue ) <= 0)
                return true;
        }
        return false;
    }
    
    // Crée un BigDecimal avec un montant pré-validé ou vide
    private static BigDecimal createMontant(String validatedMontant)
    {
        try
        {
	        //validatedMontant = validatedMontant.replace(',', '.');
	        return new BigDecimal(validatedMontant);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    // Retourne les messages accumulés lors de la validation des différents champs
    public String GetMessage()
    {
        return m_message.toString();
    }
    
    // Retourne un dataAccess avec les valeurs des paramètres passées au constructeur
    public P_Employee_Roe CreateDataAccess()
    {
        String trxName = null; // Trx.createTrxName();
        
        if(m_validationResult)
        {
	        P_Employee_Roe roe = null;
	        if(P_Employee_Roe_ID != null && !P_Employee_Roe_ID.equals(""))
	        {
	            roe = new P_Employee_Roe(Env.getCtx(), Integer.parseInt(P_Employee_Roe_ID), trxName);
	        }
	        else
	        {
	            roe = new P_Employee_Roe(Env.getCtx(), -1, trxName);
	        }
	        
	        if(CommentsLine01 != null && !CommentsLine01.trim().equals("")) roe.setCommentsLine01(CommentsLine01);
	        if(CommentsLine02 != null && !CommentsLine02.trim().equals("")) roe.setCommentsLine02(CommentsLine02);
	        if(CommentsLine03 != null && !CommentsLine03.trim().equals("")) roe.setCommentsLine03(CommentsLine03);
	        if(CommentsLine04 != null && !CommentsLine04.trim().equals("")) roe.setCommentsLine04(CommentsLine04);
	        if(EarningsForPayPeriod01 != null && !EarningsForPayPeriod01.trim().equals("")) roe.setEarningsForPayPeriod01(createMontant(EarningsForPayPeriod01));
	        if(EarningsForPayPeriod02 != null && !EarningsForPayPeriod02.trim().equals("")) roe.setEarningsForPayPeriod02(createMontant(EarningsForPayPeriod02));
	        if(EarningsForPayPeriod03 != null && !EarningsForPayPeriod03.trim().equals("")) roe.setEarningsForPayPeriod03(createMontant(EarningsForPayPeriod03));
	        if(EarningsForPayPeriod04 != null && !EarningsForPayPeriod04.trim().equals("")) roe.setEarningsForPayPeriod04(createMontant(EarningsForPayPeriod04));
	        if(EarningsForPayPeriod05 != null && !EarningsForPayPeriod05.trim().equals("")) roe.setEarningsForPayPeriod05(createMontant(EarningsForPayPeriod05));
	        if(EarningsForPayPeriod06 != null && !EarningsForPayPeriod06.trim().equals("")) roe.setEarningsForPayPeriod06(createMontant(EarningsForPayPeriod06));
	        if(EarningsForPayPeriod07 != null && !EarningsForPayPeriod07.trim().equals("")) roe.setEarningsForPayPeriod07(createMontant(EarningsForPayPeriod07));
	        if(EarningsForPayPeriod08 != null && !EarningsForPayPeriod08.trim().equals("")) roe.setEarningsForPayPeriod08(createMontant(EarningsForPayPeriod08));
	        if(EarningsForPayPeriod09 != null && !EarningsForPayPeriod09.trim().equals("")) roe.setEarningsForPayPeriod09(createMontant(EarningsForPayPeriod09));
	        if(EarningsForPayPeriod10 != null && !EarningsForPayPeriod10.trim().equals("")) roe.setEarningsForPayPeriod10(createMontant(EarningsForPayPeriod10));
	        if(EarningsForPayPeriod11 != null && !EarningsForPayPeriod11.trim().equals("")) roe.setEarningsForPayPeriod11(createMontant(EarningsForPayPeriod11));
	        if(EarningsForPayPeriod12 != null && !EarningsForPayPeriod12.trim().equals("")) roe.setEarningsForPayPeriod12(createMontant(EarningsForPayPeriod12));
	        if(EarningsForPayPeriod13 != null && !EarningsForPayPeriod13.trim().equals("")) roe.setEarningsForPayPeriod13(createMontant(EarningsForPayPeriod13));
	        if(EarningsForPayPeriod14 != null && !EarningsForPayPeriod14.trim().equals("")) roe.setEarningsForPayPeriod14(createMontant(EarningsForPayPeriod14));
	        if(EarningsForPayPeriod15 != null && !EarningsForPayPeriod15.trim().equals("")) roe.setEarningsForPayPeriod15(createMontant(EarningsForPayPeriod15));
	        if(EarningsForPayPeriod16 != null && !EarningsForPayPeriod16.trim().equals("")) roe.setEarningsForPayPeriod16(createMontant(EarningsForPayPeriod16));
	        if(EarningsForPayPeriod17 != null && !EarningsForPayPeriod17.trim().equals("")) roe.setEarningsForPayPeriod17(createMontant(EarningsForPayPeriod17));
	        if(EarningsForPayPeriod18 != null && !EarningsForPayPeriod18.trim().equals("")) roe.setEarningsForPayPeriod18(createMontant(EarningsForPayPeriod18));
	        if(EarningsForPayPeriod19 != null && !EarningsForPayPeriod19.trim().equals("")) roe.setEarningsForPayPeriod19(createMontant(EarningsForPayPeriod19));
	        if(EarningsForPayPeriod20 != null && !EarningsForPayPeriod20.trim().equals("")) roe.setEarningsForPayPeriod20(createMontant(EarningsForPayPeriod20));
	        if(EarningsForPayPeriod21 != null && !EarningsForPayPeriod21.trim().equals("")) roe.setEarningsForPayPeriod21(createMontant(EarningsForPayPeriod21));
	        if(EarningsForPayPeriod22 != null && !EarningsForPayPeriod22.trim().equals("")) roe.setEarningsForPayPeriod22(createMontant(EarningsForPayPeriod22));
	        if(EarningsForPayPeriod23 != null && !EarningsForPayPeriod23.trim().equals("")) roe.setEarningsForPayPeriod23(createMontant(EarningsForPayPeriod23));
	        if(EarningsForPayPeriod24 != null && !EarningsForPayPeriod24.trim().equals("")) roe.setEarningsForPayPeriod24(createMontant(EarningsForPayPeriod24));
	        if(EarningsForPayPeriod25 != null && !EarningsForPayPeriod25.trim().equals("")) roe.setEarningsForPayPeriod25(createMontant(EarningsForPayPeriod25));
	        if(EarningsForPayPeriod26 != null && !EarningsForPayPeriod26.trim().equals("")) roe.setEarningsForPayPeriod26(createMontant(EarningsForPayPeriod26));
	        if(EarningsForPayPeriod27 != null && !EarningsForPayPeriod27.trim().equals("")) roe.setEarningsForPayPeriod27(createMontant(EarningsForPayPeriod27));
	        if(EmployeeOccupation != null && !EmployeeOccupation.trim().equals("")) roe.setEmployeeOccupation(EmployeeOccupation);
	        if(ExpectedDateOfRecall != null && !ExpectedDateOfRecall.trim().equals("")) roe.setExpectedDateOfRecall(createDate(ExpectedDateOfRecall));
	        if(ExpectedRecallCode != null && !ExpectedRecallCode.trim().equals("")) roe.setExpectedRecallCode(ExpectedRecallCode);
	        if(FinalPayPeriodEndingDate != null && !FinalPayPeriodEndingDate.trim().equals("")) roe.setFinalPayPeriodEndingDate(createDate(FinalPayPeriodEndingDate));
	        if(FirstDayWorked != null && !FirstDayWorked.trim().equals("")) roe.setFirstDayWorked(createDate(FirstDayWorked));
	        if(LastDayForWhichPaid != null && !LastDayForWhichPaid.trim().equals("")) roe.setLastDayForWhichPaid(createDate(LastDayForWhichPaid));
	        if(OtherMoniesAmount01 != null && !OtherMoniesAmount01.trim().equals("")) roe.setOtherMoniesAmount01(createMontant(OtherMoniesAmount01));
	        if(OtherMoniesAmount02 != null && !OtherMoniesAmount02.trim().equals("")) roe.setOtherMoniesAmount02(createMontant(OtherMoniesAmount02));
	        if(OtherMoniesAmount03 != null && !OtherMoniesAmount03.trim().equals("")) roe.setOtherMoniesAmount03(createMontant(OtherMoniesAmount03));
	        if(OtherMoniesCode01 != null && !OtherMoniesCode01.trim().equals("")) roe.setOtherMoniesCode01(OtherMoniesCode01);
	        if(OtherMoniesCode02 != null && !OtherMoniesCode02.trim().equals("")) roe.setOtherMoniesCode02(OtherMoniesCode02);
	        if(OtherMoniesCode03 != null && !OtherMoniesCode03.trim().equals("")) roe.setOtherMoniesCode03(OtherMoniesCode03);
	        if(P_Employee_ID != null && !P_Employee_ID.trim().equals("")) roe.setP_Employee_ID(Integer.parseInt(P_Employee_ID));
	        if(P_Employee_Roe_ID != null && !P_Employee_Roe_ID.trim().equals("")) roe.setP_Employee_Roe_ID(Integer.parseInt(P_Employee_Roe_ID));
	        if(PaidSickAmount != null && !PaidSickAmount.trim().equals("")) roe.setPaidSickAmount(createMontant(PaidSickAmount));
	        if(PaidSickDate != null && !PaidSickDate.trim().equals("")) roe.setPaidSickDate(createDate(PaidSickDate));
	        if(PaidSickPeriod != null && !PaidSickPeriod.trim().equals("")) roe.setPaidSickPeriod(PaidSickPeriod);
	        if(ReasonForIssuingThisRoe != null && !ReasonForIssuingThisRoe.trim().equals("")) roe.setReasonForIssuingThisRoe  (ReasonForIssuingThisRoe  );
	        if(StatutoryHolidayPayAmount01 != null && !StatutoryHolidayPayAmount01.trim().equals("")) roe.setStatutoryHolidayPayAmount01(createMontant(StatutoryHolidayPayAmount01));
	        if(StatutoryHolidayPayAmount02 != null && !StatutoryHolidayPayAmount02.trim().equals("")) roe.setStatutoryHolidayPayAmount02(createMontant(StatutoryHolidayPayAmount02));
	        if(StatutoryHolidayPayAmount03 != null && !StatutoryHolidayPayAmount03.trim().equals("")) roe.setStatutoryHolidayPayAmount03(createMontant(StatutoryHolidayPayAmount03));
	        if(StatutoryHolidayPaydate01 != null && !StatutoryHolidayPaydate01.trim().equals("")) roe.setStatutoryHolidayPaydate01(StatutoryHolidayPaydate01);
	        if(StatutoryHolidayPaydate02 != null && !StatutoryHolidayPaydate02.trim().equals("")) roe.setStatutoryHolidayPaydate02(StatutoryHolidayPaydate02);
	        if(StatutoryHolidayPaydate03 != null && !StatutoryHolidayPaydate03.trim().equals("")) roe.setStatutoryHolidayPaydate03(StatutoryHolidayPaydate03);
	        if(TotalInsurableEarnings != null && !TotalInsurableEarnings.trim().equals("")) roe.setTotalInsurableEarnings(createMontant(TotalInsurableEarnings));
	        if(TotalInsurableHours != null && !TotalInsurableHours.trim().equals("")) roe.setTotalInsurableHours(new BigDecimal ( Double.parseDouble(TotalInsurableHours)));
	        if(VacationPayAmount != null && !VacationPayAmount.trim().equals("")) roe.setVacationPayAmount(createMontant(VacationPayAmount));
	
	        return roe;
        }
        return null;
    }
    
    // Valide une date sous le format jj/mm/aaaa
    private boolean validDate(String date)
    {
        if(date == null) return false;
        
        Pattern p = Pattern.compile("[0-9]{2}/[0-9]{2}/[0-9]{4}");
        Matcher m = p.matcher(date);
        if(!m.matches()) return false;
        
        int jj = Integer.parseInt(date.substring(0, 2));
        int mm = Integer.parseInt(date.substring(3, 5));
        int aaaa = Integer.parseInt(date.substring(6));
        
        if(jj < 1 || mm < 1  || mm > 12 || aaaa < 0) return false;
        
        switch(mm)
        {
        	case 4: case 6: case 9: case 11:
        	    if(jj > 30) return false;
        	    break;
        	case 2:
        	    if(jj > 28 + (aaaa % 4 == 0 ? 1 : 0)) return false;
        	    break;
        	default:
        	    if(jj > 31) return false;
        	    break;
        }
        return true;
    }
    
    // Retourne une date à partir d'une date validée par la méthode validDate(...)
    private Timestamp createDate(String validatedDate)
    {
        try
        {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTimeInMillis(0);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(validatedDate.substring(0, 2)));
            calendar.set(Calendar.MONTH, Integer.parseInt(validatedDate.substring(3, 5))-1);
            calendar.set(Calendar.YEAR, Integer.parseInt(validatedDate.substring(6)));

            return new Timestamp(calendar.getTimeInMillis());
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    // Retourne le dernier jour du mois (0 à 11)
    private int lastDayOfMonth(int month, int year)
    {
        switch(month)
        {
        	case 3: case 5: case 8: case 10:
        	    return 30;
        	case 1:
        	    return 28 + (year % 4 == 0 ? 1 : 0);
        	default:
        	    return 31;
        }
    }*/
}
