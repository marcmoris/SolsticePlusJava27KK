/*
 */
package solstice.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import solstice.model.P_Assignment;
import solstice.model.P_Assignment_RWT;
import solstice.model.P_Employee_Carra;
import solstice.model.P_Gain_Parameter;
import solstice.model.P_Employee;
import solstice.model.P_Job_Title;
import solstice.model.P_Job_Type;
import solstice.model.P_Gain;
import solstice.model.P_Payment_Group;
import solstice.model.P_Period;
import solstice.model.P_Year;
import org.compiere.util.TimeUtil;




/**
 * @author frafor01
 *
 * Cette classe définit la procédure qui popule les tables
 * P_Employee_Carra et P_Equivalence_Factor
 */
public class P_Carra 
{
    private P_Employee employee;

	/**
     * @author frafor01
     *
     * Cette classe représente la stored proc SP_TF705_CALCUL_VALEUR
     */
    private class CalculValeur
    {
        /*
         * Paramètres in
         */
        private int periodId;
        private int periodStartId;
        private int periodEndId;
        private Timestamp startDate;
        private Timestamp endDate;
        private int employeeId;
        private int columnAdmId;
        private Timestamp date35;
        private int deductionId;
        private int yearId;
        private String recordType;
        
        /*
         * Paramètres out
         */
        private BigDecimal daysTRD;
        private BigDecimal absenceDays = new BigDecimal(0);
        private BigDecimal motherhoodDays = new BigDecimal(0);
        private BigDecimal disabilityDays = new BigDecimal(0);
        private BigDecimal contributoryDays = new BigDecimal(0);
        private int jobTypeId;
        private BigDecimal employeePart = Env.ZERO;
        private BigDecimal motherhoodSalary = new BigDecimal(0);
        private BigDecimal nonContributorySalary = new BigDecimal(0);
        private BigDecimal exoneratedSalary = new BigDecimal(0);
        private BigDecimal contributorySalary = new BigDecimal(0);
        private BigDecimal baseSalary = new BigDecimal(0);
        private int assignmentCounter = 0;
        private int assignmentId;
        private String strStatus = "";
        private BigDecimal joursTRD;
        private final BigDecimal bdHundred = new BigDecimal(100);

        /*
         * Getters
         */
        public BigDecimal getDaysTRD() {return this.daysTRD;}
        public BigDecimal getAbsenceDays() {return this.absenceDays;}
        public BigDecimal getMotherhoodDays() {return this.motherhoodDays;}
        public BigDecimal getDisabilityDays() {return this.disabilityDays;}
        public BigDecimal getContributoryDays() {return this.contributoryDays;}
        public int getJobTypeId() {return this.jobTypeId;}
        public BigDecimal getEmployeePart() {return this.employeePart;}
        public BigDecimal getMotherhoodSalary() {return this.motherhoodSalary;}
        public BigDecimal getNonContributorySalary() {return this.nonContributorySalary;}
        public BigDecimal getExoneratedSalary() {return this.exoneratedSalary;}
        public BigDecimal getContributorySalary() {return this.contributorySalary;}
        public BigDecimal getBaseSalary() {return this.baseSalary;}
        public int getAssignmentCounter() {return this.assignmentCounter;}
        public int getAssignmentId() {return this.assignmentId;}
        public String getStrStatus() {return this.strStatus;}
        public BigDecimal getjoursTRD() {return this.joursTRD;}


        /**
         * Constructeur
         */
        public CalculValeur(int periodId, int periodStartId, int periodEndId, Timestamp startDate,
                Timestamp endDate, int employeeId, int columnAdmId, Timestamp date35, int deductionId,
                int yearId, String recordType, BigDecimal bdDaysTRD)
        {
            this.periodId = periodId;
            this.periodStartId = periodStartId;
            this.periodEndId = periodEndId;
            this.startDate = startDate;
            this.endDate = endDate;
            this.employeeId = employeeId;
            this.columnAdmId = columnAdmId;
            this.date35 = date35;
            this.deductionId = deductionId;
            this.yearId = yearId;
            this.recordType = recordType;
            //this.daysTRD = bdDaysTRD;
            this.daysTRD = Env.ZERO;
            this.joursTRD = Env.ZERO;
            //System.out.println("Colonne d'admissibkilité ID :"+this.columnAdmId);
        }
        
        /**
         * Réfère à la procédure SP_TF705_CSTD
         */
        private BigDecimal cstd(int paymentId, BigDecimal pourcentageTRD) throws Exception
        {
        	
            // On va boucler sur tous les détail de paiement pour le paiemnet passé en paramètre
            String sql
            = "select P_Gain_ID, P_Job_Title_ID, QuantityCalc "
                + " from P_Payment_Gain"
                + " where P_Payment_ID = " + paymentId;
            
            PreparedStatement stmt = DB.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            pourcentageTRD = (new BigDecimal("-100")).add(pourcentageTRD);
            
            while(rs.next())
            {
            	P_Gain_Parameter gain_param = P_Gain_Parameter.get(Env.getCtx(), rs.getInt("P_Gain_ID"), 0, null);
            	//on va chercher les heures hebdo du titre d'emploi
            	BigDecimal weekly_Hours = getWeeklyHourJobTitle(rs.getInt(2));
            	Vector colsAdm = new Vector(2);
            	//on va chercher les ID des colonnes d'admissibilité 14 et 15
            	int colAdm14 = getAdmColIDbyValue(14);
            	colsAdm.add(new Integer(colAdm14));
            	int colAdm15 = getAdmColIDbyValue(15);
            	colsAdm.add(new Integer(colAdm15));
            	boolean isColAdm = colAdmInGain(gain_param.getP_Gain_Parameter_ID(), colsAdm);
            	if(isColAdm == true)
            	{
            		BigDecimal numerator =  rs.getBigDecimal(3).multiply(pourcentageTRD.divide(bdHundred, 4, BigDecimal.ROUND_HALF_UP));
            		BigDecimal denominator = weekly_Hours.divide(new BigDecimal(5), 4);
            		this.daysTRD = this.daysTRD.add(numerator.divide(denominator, 4));
            	}
            }
            
            rs.close();
            stmt.close();
            
            return this.daysTRD;
        }
        
        private BigDecimal getWeeklyHourJobTitle(int iP_Job_Title_ID)
        {
        	BigDecimal weekly_hour = Env.ZERO;
        	
        	//on va chercher les heures hebdo dans p_job_title
        	String sql = "SELECT Weekly_Hours FROM P_Job_Title "
        		+ " WHERE P_Job_Title_ID = " + iP_Job_Title_ID;
        	PreparedStatement pstm = DB.prepareStatement(sql);
        	ResultSet rs = null;
        	try
        	{
	        	rs = pstm.executeQuery();
	        	
	        	if(rs.next())
	        	{
	        		weekly_hour = rs.getBigDecimal(1);
	        	}
	        	else
	        	{
	        		log.log(Level.SEVERE, "getWeeklyHourJobTitle, no row");
	        	}
	            rs.close();
	            pstm.close();       		
        	}
        	catch(Exception e)
        	{
        		log.log (Level.SEVERE, "getWeeklyHourJobTitle", e);
        	}       	
        	return weekly_hour;
        }
        
        private int getAdmColIDbyValue(int value)
        {
        	int iP_Column_Adm = 0;
        	String sql = " SELECT P_Column_Adm_ID from P_Column_Adm "
        		+ " WHERE Value like '" + value + "' ";
        	PreparedStatement pstm = DB.prepareStatement(sql);
        	try
        	{
	        	ResultSet rs = pstm.executeQuery();
	        	
	        	if(rs.next())
	        	{
	        		iP_Column_Adm = rs.getInt(1);
	        	}
	        	else
	        	{
	        		log.log(Level.SEVERE, "getAdmColIDbyValue, no row");
	        	}
	        	rs.close();
	        	pstm.close();
        	}
        	catch(Exception e)
        	{
        		log.log (Level.SEVERE, "getAdmColIDbyValue", e);
        	}
        	
        	return iP_Column_Adm;

        }
        
        private boolean colAdmInGain(int iP_Gain_Param_ID, Vector colsAdm)
        {
        	//on verifie si les colonnes d'admissibilités (IDs dans le vector)
        	//sont joitn au Gain dans la table P_Gain_Column
        	boolean retValue = false;
        	
        	String sql = "SELECT 1 FROM P_Gain_Column "
        		+ " WHERE P_Gain_Parameter_ID ="+iP_Gain_Param_ID;
        	for(int idx=0;idx<colsAdm.size();idx++)
        	{
        		if(idx == 0)
        			sql += " AND (";
        		sql += " P_Column_Adm_ID = " + ((Integer)colsAdm.get(idx)).toString();
        		if(idx < colsAdm.size()-1)
        			sql += " OR ";
        	}
        	if(colsAdm.size() > 0)
        		sql += " )";
 
           	PreparedStatement pstm = DB.prepareStatement(sql);
        	try
        	{
	        	ResultSet rs = pstm.executeQuery();
	        	
	        	if(rs.next())
	        	{
	        		retValue = true;
	        	}
	        	rs.close();
	        	pstm.close();
        	}
        	catch(Exception e)
        	{
        		log.log (Level.SEVERE, "colAdmInGain", e);
        	}

        	return retValue;
        }
        /**
         * Traitement principal
         */
        public void execute() throws Exception
        {
            int cmptPGI = 0;
            Vector affDifferente = new Vector(10);
        	cmptPGI = cmptPGI + this.processDetail(this.employeeId, affDifferente);
/*
        	//System.out.println("execute calcul valeur");
            // On boucle sur toutes les paiements de l'employé entre la période début et la période de fin
            String sql
            = "select payment.P_Payment_ID"
                + " from P_Payment payment inner join P_Period period"
                + " on payment.P_Period_ID = period.P_Period_ID"
                + " where period.StartDate >= (select StartDate from P_Period where P_Period_ID = " + this.periodStartId + ")"
                + " and period.EndDate <= (select EndDate from P_Period where P_Period_ID = " + this.periodEndId + ")"
                + " and payment.P_Employee_ID = " + this.employeeId;
            
            PreparedStatement stmt = DB.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            //System.out.println("le sql :"+sql);
            int cmptPGI = 0;
            Vector affDifferente = new Vector(10);
            while(rs.next())
            {
            	
                // On boucle sur les détails
            	//System.out.println("Paiement ID :"+rs.getInt("P_Payment_ID"));
            	cmptPGI = cmptPGI + this.processDetail(rs.getInt("P_Payment_ID"), affDifferente);
            	
// ici, faire un seul select            	
            }
            
            rs.close();
            stmt.close();
*/
            // Calcul de l'employé cotisé
          String  sql = "select isnull(sum(paymentDeduction.Employer_Part + paymentDeduction.Employee_Part), 0) as Employee_Part, max( AnnualSalary ) AnnualSalary"
                + " from (P_Payment_Deduction paymentDeduction inner join P_Payment payment"
                + " on paymentDeduction.P_Payment_ID = payment.P_Payment_ID) inner join P_Period period"
                + " on payment.P_Period_ID = period.P_Period_ID"
                + " where payment.P_Employee_ID = " + this.employeeId
                + " and payment.p_year_id = " + this.yearId
                + " and paymentDeduction.P_Deduction_ID = " + this.deductionId;

//        + " and period.StartDate >= (select StartDate from P_Period where P_Period_ID = " + this.periodStartId + ")"
//        + " and period.EndDate <= (select EndDate from P_Period where P_Period_ID = " + this.periodEndId + ")"

            //System.out.println("Cotisation employé : "+sql);
          PreparedStatement stmt = DB.prepareStatement(sql);
          ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                this.employeePart = this.employeePart.add(rs.getBigDecimal("Employee_Part"));
                if(this.employeePart.compareTo(Env.ZERO) != 0)
                {
                	this.employeePart = this.employeePart.divide(new BigDecimal(2), 2);
                }
            }
            else
            {
                this.employeePart = new BigDecimal(0);
            }
            
            rs.close();
            stmt.close();

/*
            sql = "Select AnnualSalary From P_Payment "
            	+ " where P_Payment_ID = ( select max( P_Payment_ID) from p_Payment m "
                + " where m.P_Employee_ID = " + this.employeeId
                + "   and m.p_year_id = " + this.yearId
                + " ) "
                ;
            stmt = DB.prepareStatement(sql);
            rs = stmt.executeQuery();
              
            if(rs.next())
            {
            }
            rs.close();
            stmt.close();
*/            
            
            sql = "SELECT P_Payment_ID, SalaryPercentage "
            	+ " FROM P_Payment "
            	+ " WHERE P_Employee_ID = " + this.employeeId
            	+ " AND P_Year_ID = " + this.yearId  // .periodId
            	+ " AND isnull(SalaryPercentage, 0) <> 0 ";
            
            stmt = DB.prepareStatement(sql);
            rs = stmt.executeQuery();
            BigDecimal tmp;
            while(rs.next())
            {
            	this.strStatus = "7";
            	tmp = cstd(rs.getInt(1), rs.getBigDecimal(2));
            	if ( tmp != null )
            		this.joursTRD = this.joursTRD.add( tmp );
            }
            
            rs.close();
            stmt.close();
            
            //
            // S'il y a une seule affectation alors on prend denier salaire que l'employé a recu
            // sinon on prend le salaire moyen 
            //
        	BigDecimal Annual_Increase = null;
    		Annual_Increase = P_Payment_Group.getAnnual_Increase( Env.getCtx(), this.employeeId, null);

            if(this.assignmentCounter == 1)
            {
            	P_Period Period = P_Period.get( Env.getCtx(), this.periodEndId, null);
            	BigDecimal temporarySalary = employee.getAnnualSalary( Period.getEndDate());
            	this.baseSalary = temporarySalary.divide( Annual_Increase,8,BigDecimal.ROUND_HALF_UP ).multiply( new BigDecimal(52) );
//            	this.baseSalary = temporarySalary.divide( new BigDecimal( 52.18 ),8,BigDecimal.ROUND_HALF_UP ).multiply( new BigDecimal(52) );
            }
            else
            {
            
            	//System.out.println("Compteur pgi :"+cmptPGI);
            	//test
            	if(this.baseSalary.compareTo(Env.ZERO) > 0 && cmptPGI != 0)
            		this.baseSalary = this.baseSalary.divide(new BigDecimal(cmptPGI), 4, BigDecimal.ROUND_HALF_UP);
            	this.baseSalary = baseSalary.divide( Annual_Increase,8,BigDecimal.ROUND_HALF_UP ).multiply( new BigDecimal(52) );
//            	this.baseSalary = baseSalary.divide( new BigDecimal( 52.18 ),8,BigDecimal.ROUND_HALF_UP ).multiply( new BigDecimal(52) );
            }
            //
    	}

        /**
         * Traitement pour le détail du paiement (P_Payment_Gain)
         */
        private int processDetail(int EmployeeId, Vector affDifferente) throws Exception
        {
        	int cmpt = 0;
            String sql
            = "select paymentGain.P_Assignment_ID, period.StartDate as PaymentPeriodStartDate, period.EndDate as PaymentPeriodEndDate,"
                + " jobTitle.Weekly_Hours, gain.Flag_Carra, gain.P_Method_Gain_ID, gain.P_Gain_ID, paymentGain.[Day], paymentGain.AmountCalc,"
                + " paymentGain.QuantityCalc, assignmentParam.Weekly_Hours as AssignmentParamWeeklyHours, methodGain.Name as MethodGain,"
                + " paymentGain.Hourly_Rate, P_Payment.P_Payment_ID"
                + " from P_Payment_Gain paymentGain inner join P_Payment on paymentGain.P_Payment_ID = P_Payment.P_Payment_ID" 
                + " inner join P_Period period on P_Payment.P_Period_ID = period.P_Period_ID "
                + " inner join P_Job_Title jobTitle on paymentGain.P_Job_Title_ID = jobTitle.P_Job_Title_ID "
                + " inner join P_Gain gain on paymentGain.P_Gain_ID = gain.P_Gain_ID " 
                + " inner join P_Method_Gain methodGain on gain.P_Method_Gain_ID = methodGain.P_Method_Gain_ID "
                + " inner join P_Assignment assignment on paymentGain.P_Assignment_ID = assignment.P_Assignment_ID "
                + " inner join P_Assignment_Param assignmentParam on assignmentParam.P_Assignment_Param_ID = paymentGain.P_Assignment_Param_ID "
                + " where P_Payment.P_Employee_ID = " + EmployeeId
                + "   And P_Payment.P_Year_ID = " + this.yearId
//                + " AND paymentGain.[Day] >= ? "
//                + " AND paymentGain.[Day] <= ? "
                + " Order By assignment.P_Assignment_ID, paymentGain.[Day] ";
            
            PreparedStatement stmt = DB.prepareStatement(sql);
            //System.out.println("Date de debut :"+startDate);
//            stmt.setTimestamp(1, startDate);
//            stmt.setTimestamp(2, endDate);
            ResultSet rs = stmt.executeQuery();

            int lastAffectation = 0;
            while(rs.next())
            {
                // Si l'affectation qu'on retrouve sur le détail du paiement n'est pas null
                // et que celle-ci n,est pas exipirée
                if(rs.getObject("P_Assignment_ID") != null
                        && !this.isExpired(rs.getInt("P_Assignment_ID")))
                {
                    // L'Affectation de retour est égale à l'affectation courante. On incrémente le compteur d'affectation

                    this.assignmentId = rs.getInt("P_Assignment_ID");
                	if(affDifferente.contains(new Integer(rs.getInt("P_Assignment_ID"))) == false)
                	{
                		affDifferente.add(new Integer(rs.getInt("P_Assignment_ID")));          		
                		//on veut juste compter les affectation, pas les parametres d'affectation
                    	this.assignmentCounter++;
                    	lastAffectation = this.assignmentId;
                    }
                }
                
                // On va chercher la date de début et la date de fin de la période pour la période
                // qu'on retrouve sur le détail du paiement courant
                Timestamp paymentPeriodStartDate = rs.getTimestamp("PaymentPeriodStartDate");
                Timestamp paymentPeriodEndDate = rs.getTimestamp("PaymentPeriodEndDate");
                
                // On va chercher le facteur d'annualité
                BigDecimal facteurAnnualite = new BigDecimal("52");
                
                // On va chercher les heures hebdomadaires du titre d'emploi selon le titre d'emploi
                // sur le détail de la feuille de temps
//                BigDecimal weeklyHours = rs.getBigDecimal("Weekly_Hours");
                BigDecimal weeklyHours = rs.getBigDecimal("AssignmentParamWeeklyHours");
                
                // On va chercher l'indicateur Carra sur le code de rémunération ainsi que la méthode de gain
                // retrouvé sur le détail du paiement courant
                String flagCarra = rs.getString("Flag_Carra");
                int methodGainId = rs.getInt("P_Method_Gain_ID");
                
                // On calcul le taux horaire (voir ref SP_CALCUL_TAUX_HORAIRE04)
                P_Assignment assignment = new P_Assignment(Env.getCtx(), rs.getInt("P_Assignment_ID"), null);
                //BigDecimal hourlyRate = assignment.getHourly_Rate(paymentPeriodEndDate, null);
                BigDecimal hourlyRate = null;
                if ( methodGainId != 114 ) // Gain a taux spéciaux.
                	hourlyRate = rs.getBigDecimal("Hourly_Rate");
                
                if(hourlyRate == null || hourlyRate.compareTo(Env.ZERO) == 0)
                	getHourlyRateFromOtherFTD(rs.getInt( "P_Payment_ID"), rs.getInt("P_Assignment_ID"), rs.getTimestamp("Day"));
                if(hourlyRate == null || hourlyRate.compareTo(Env.ZERO) == 0)
                	hourlyRate = assignment.getHourly_Rate(paymentPeriodEndDate, null);
                
                // Si le taux horaire trouvé est différent de null et de 0
                if(hourlyRate != null
                        && hourlyRate.doubleValue() != 0)
                {
                    // Calcul du salaire de base
                	// on addition tout les salaire ensemble pour a la fin avoir un salaire moyen.
                    this.baseSalary = this.baseSalary.add(hourlyRate.multiply(weeklyHours.setScale(4, BigDecimal.ROUND_HALF_UP)).multiply(facteurAnnualite.setScale(4, BigDecimal.ROUND_HALF_UP) ));
                    cmpt++;
                }
                
                // On va chercher les heures hebdomadaires de l'affectation selon L'affectation trouvée dans
                // le paiement. Les heures hebdomadaires sont situés dans P_Assignment_Param.Weekly_Hours. On
                // se sert de la date de début de période pour aller chercher le P_Assigment_Param valide.
                BigDecimal assignmentParamWeeklyHours = rs.getBigDecimal("AssignmentParamWeeklyHours");
                
                BigDecimal pgifct = new BigDecimal(1);
/*                // Si la valeur de l'affectation est égale à 0115
                if(assignment.getValue().equals("0115"))
                {
                    pgifct = weeklyHours.divide(assignmentParamWeeklyHours, 4, BigDecimal.ROUND_HALF_UP);
                }
                else
                {
*/                
                    // On va chercher les heures hebdomadaires ARTT et les heures de base ARTT. On utilise
                    // l'afectation et les dates début et fin pou récupérer l'enregistrement dans P_Assignment_rwt
                    P_Assignment_RWT assignmentRWT = assignment.GetAssignment_RWT(rs.getTimestamp("Day")); // this.startDate
                    if(assignmentRWT != null)
                    	pgifct = weeklyHours.divide(assignmentRWT.getWeekly_Hours(), 5, BigDecimal.ROUND_HALF_UP); //assignmentRWT.getWeekly_Hours().divide(assignmentRWT.getDay_Hours(), 4, BigDecimal.ROUND_HALF_UP)( new BigDecimal(5), 4, BigDecimal.ROUND_HALF_UP);
/*                }
 * 
 */

                BigDecimal AmountCalc = rs.getBigDecimal("AmountCalc");
                BigDecimal QuantityCalc = rs.getBigDecimal("QuantityCalc");
                // Si le code de rémunération a une colonne d'admissibilité égale à la colonne d'admissibilité
                // du constructeur et que la valeur du code de rémunération est différente de «REDUC»
                if(this.validGainColumn(rs.getInt("P_Gain_ID"))  )
                {
/*                	if ( this.typeGainReduc( rs.getInt("P_Gain_ID") ) )
                	{
                		AmountCalc = rs.getBigDecimal("AmountCalc").multiply(new BigDecimal(-1));
                		QuantityCalc = rs.getBigDecimal("QuantityCalc").multiply(new BigDecimal(-1));
                	}
*/                	
                    // Si la date 35 ans est différente de null et qu'elle est plus petite ou égale
                    // à la date de mouvement du détail du paiement
                    if(this.date35 != null && this.date35.compareTo(rs.getTimestamp("Day")) <= 0)
                    {
                        // Calcul du salaire non cotisable
                        this.nonContributorySalary = this.nonContributorySalary.add( AmountCalc );
                    }
                    else
                    {
                    	//Calcul du salaire cotisable et le nombre de jours cotisables
                    	if(methodGainId == 104 && hourlyRate != null)//104 = temps non rémunéré
                    	{
                    		this.contributorySalary = this.contributorySalary.add( QuantityCalc.multiply(hourlyRate)); // .multiply(pgifct)
                    	}
                    	else
                    	{
                    		this.contributorySalary = this.contributorySalary.add( AmountCalc); // .multiply(pgifct)
                    	}
                    	
                        //System.out.println("Salaire cotisable :"+rs.getBigDecimal("AmountCalc")+", pour  le : "+rs.getString("Day"));
                        //System.out.println("Cumul :"+this.contributorySalary);

//                    	if(weeklyHours.compareTo(Env.ZERO) > 0)
                    	if(weeklyHours.compareTo(Env.ZERO) != 0)
                        {
                        	//System.out.println("paymentPeriodStartDate : "+paymentPeriodStartDate+", QuantityCalc : "+ rs.getBigDecimal("QuantityCalc")+", weeklyHours"+weeklyHours);
                        	BigDecimal tempValue = QuantityCalc.divide(weeklyHours.divide(new BigDecimal(5), 4, BigDecimal.ROUND_HALF_UP), 4, BigDecimal.ROUND_HALF_UP);
                        	//System.out.println(" qty / (weekly hour / 5) : "+tempValue);
                        	//System.out.println("Affectation : "+assignment.getValue());
                        	this.contributoryDays = this.contributoryDays.add(tempValue); // .multiply(pgifct) 
                        	//System.out.println("Cumul : "+this.contributoryDays);
//                        	System.out.println("contributory Days :"+this.contributoryDays+", pour  le : "+rs.getString("Day"));
                        }
                    }
                }
                
               
                if(flagCarra != null)
                {
	                // Si le flag_Carra est égal à «AIN»
	                if(flagCarra.trim().toUpperCase().equals(P_Gain.FLAG_CARRA_AIN_Non_ContributoryDisabilityDays)  //  "AIN"
			                || flagCarra.trim().toUpperCase().equals(P_Gain.FLAG_CARRA_RASS_InsuranceRetroOnSalary)) //  "RASS"
	                {
	                    // Si la date 35 est différente de null et plus petite ou égale à la date du mouvement du paiement
	                    if(this.date35 != null && this.date35.compareTo(rs.getTimestamp("Day")) <= 0)
	                    {
	                        // Si la méthode de gain est égale à «014» et que le taux horaire du détail du paiement
	                        // est différent de null et de 0
	                        //if(!rs.getString("MethodGain").equals("014")
	                    	if(methodGainId != 114 
	                                && rs.getBigDecimal("Hourly_Rate") != null
	                                && rs.getBigDecimal("Hourly_Rate").doubleValue() != 0)
	                        {
	                            // Calcul du salaire non cotisable
	                            this.nonContributorySalary = this.nonContributorySalary.add(QuantityCalc.multiply(rs.getBigDecimal("Hourly_Rate")));
	                        }
	                        else
	                        {
	                            // Calcul du salaire non cotisable
	                            this.nonContributorySalary = this.nonContributorySalary.add(QuantityCalc.multiply(hourlyRate));
	                        }
	                    }
	                    else
	                    {
	                        // Si la méthode de gain est différente à «014» et que le taux horaire du détail du paiement
	                        // est différent de null et de 0
	                    	//System.out.println("Method de gain : "+rs.getString("MethodGain"));
	                        //if(!rs.getString("MethodGain").equals("014")
	                    	if(methodGainId != 114
	                                && rs.getBigDecimal("Hourly_Rate") != null
	                                && rs.getBigDecimal("Hourly_Rate").doubleValue() != 0)
	                        {
	                            // Calcul du salaire exonéré
	                    		if ( QuantityCalc.compareTo(Env.ZERO) != 0)
	                    			this.exoneratedSalary = this.exoneratedSalary.add(QuantityCalc.multiply(rs.getBigDecimal("Hourly_Rate"))); // .multiply(pgifct)
	                    		else
	                    			this.exoneratedSalary = this.exoneratedSalary.add(AmountCalc); // .multiply(pgifct)
	                    			
	                            //System.out.println("1exoneratedSalary : "+exoneratedSalary);
	                            //System.out.println("1QuantityCalc : "+rs.getBigDecimal("QuantityCalc"));
	                            //System.out.println("1Hourly_Rate : "+rs.getBigDecimal("Hourly_Rate"));
	                            //System.out.println("1pgifct : "+pgifct);
	                            //System.out.println("1date : "+rs.getTimestamp("Day"));
	                        }
	                        else
	                        {
	                            // Calcul du salaire exonéré
	                    		if ( QuantityCalc.compareTo(Env.ZERO) != 0)
	                    			this.exoneratedSalary = this.exoneratedSalary.add( QuantityCalc.multiply(hourlyRate)); // .multiply(pgifct)
	                    		else
	                    			this.exoneratedSalary = this.exoneratedSalary.add( AmountCalc ); // .multiply(pgifct)
	                            //System.out.println("2exoneratedSalary : "+exoneratedSalary);
	                            //System.out.println("2QuantityCalc : "+rs.getBigDecimal("QuantityCalc"));
	                            //System.out.println("2Hourly_Rate v2 : "+hourlyRate);
	                            //System.out.println("2pgifct : "+pgifct);
	                            //System.out.println("2date : "+rs.getTimestamp("Day"));
	                            
	                        }
                            // Jours d'invalidité
                            this.disabilityDays = this.disabilityDays.add(QuantityCalc.divide(weeklyHours.divide(new BigDecimal("5.00"), 2, BigDecimal.ROUND_HALF_UP), 4, BigDecimal.ROUND_HALF_UP)); // .multiply(pgifct)
                            
                            // Jours d'absence
                            
                            this.absenceDays = this.absenceDays.add( QuantityCalc.divide(weeklyHours.divide(new BigDecimal("5.00"), 2, BigDecimal.ROUND_HALF_UP), 4, BigDecimal.ROUND_HALF_UP)); // .multiply(pgifct)

	                    }
	                }
	                else if(flagCarra.trim().toUpperCase().equals(P_Gain.FLAG_CARRA_AMA_MaternityLeaveDaysOff)) // "AMA"
	                {
	                    // Si la date 35 ans est différente de null et qu'elle est plus petite ou égale
	                    // à la date de mouvement du détail du paiement
	                    if(this.date35 != null && this.date35.compareTo(rs.getTimestamp("Day")) <= 0)
	                    {
	                        this.nonContributorySalary = this.nonContributorySalary.add( QuantityCalc.multiply(rs.getBigDecimal("Hourly_Rate")));
	                    }
	                    else
	                    {
	                        // Calcul du salaire maternieté
	                        this.motherhoodSalary = this.motherhoodSalary.add(QuantityCalc.multiply(rs.getBigDecimal("Hourly_Rate")));
	                        
	                        // Calcul des jours de maternité
	                        this.motherhoodDays = this.motherhoodDays.add(QuantityCalc.divide(weeklyHours.divide(new BigDecimal("5.00"), 2, BigDecimal.ROUND_HALF_UP), 4, BigDecimal.ROUND_HALF_UP));
	                        
	                        // Calcul des jours d'absence
	                        this.absenceDays = this.absenceDays.add(QuantityCalc.divide(weeklyHours.divide(new BigDecimal("5.00"), 2, BigDecimal.ROUND_HALF_UP), 4, BigDecimal.ROUND_HALF_UP));
	                        //System.out.println("AMA Date : "+rs.getString("Day")+", this.absenceDays : "+this.absenceDays+" Qty Cal : "+rs.getBigDecimal("QuantityCalc")+", weeklyHours"+weeklyHours);
	                    }
	                }
	                else if(flagCarra.trim().toUpperCase().equals(P_Gain.FLAG_CARRA_ANP_Non_PaidDays)) // "ANP"
	                {
	                    // Si (la date 35 est différente de null ET qu'il est plus petite ou égale à la
	                    // date de mouvement du détail du paiement) OU (;a date 35 est null)
	                    if((this.date35 != null 
	                            && this.date35.compareTo(rs.getTimestamp("Day")) <= 0)
	                            || this.date35 == null)
	                    {
	                        this.absenceDays = this.absenceDays.add(QuantityCalc.divide(weeklyHours.divide(new BigDecimal("5.00"), 2, BigDecimal.ROUND_HALF_UP), 4, BigDecimal.ROUND_HALF_UP)); // .multiply(pgifct)
	                        //System.out.println("ANP  Date : "+rs.getString("Day")+", this.absenceDays : "+this.absenceDays);
	                        //System.out.println("ANP  QuantityCalc : "+rs.getBigDecimal("QuantityCalc")+", weeklyHours : "+weeklyHours+", pgifct : "+pgifct);
	                    }
	                }
/*	                else if(flagCarra.trim().toUpperCase().equals(P_Gain.FLAG_CARRA_RSAL_RetroOnSalary)) // "RASS"
	                {
	                   // A Venir, il faut aller lire le module de rétro.	
	                }
*/	                
                }
                
            }
            
            rs.close();
            stmt.close();
            return cmpt;
        }
        
        private boolean typeGainReduc( int gainId) throws Exception
        {
            // À changer à cause qu'il faut passer par le P_Gain_Parameter_ID
            String sql = "select tag.value "
                + " from p_gain tag, p_gain_gaininfo ggi, p_gaininfo info "
                + " where info.p_gaininfo_id = ggi.p_gaininfo_id "
                + " and tag.p_gain_id = ggi.p_gain_id "
                + " and tag.p_gain_id =  " + gainId
                + " and info.value = 'REDUC' "
                + " and ggi.To_Consider = 'Y' " 
                + " ";
            
            	
            PreparedStatement stmt = DB.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            boolean valid = rs.next();            
            rs.close();
            stmt.close();
            
            return valid;
        }
        
        /**
         * Cette méthode vérifie si la colonne d'admissibilité du gain passé en paramètre
         * est égale à la colonne d'admissibilité du constructeur et que la valeur du
         * gain est différente de «REDUC»
         */
        private boolean validGainColumn(int gainId) throws Exception
        {
            // À changer à cause qu'il faut passer par le P_Gain_Parameter_ID
            String sql
            = "select value"
                + " from P_Gain"
                + " where P_Gain_ID = " + gainId
                //+ " and Value <> 'REDUC'" -- on prend l'info complementaire, voir plus bas
                + " and exists ("
                + "   select 1"
                + "   from P_Gain_Column"
                + "   where P_Gain_Parameter_ID = ( "
                + "		Select P_Gain_Parameter.P_Gain_Parameter_ID "
                + "		From P_Gain_Parameter where P_Gain_Parameter.P_Gain_ID = P_Gain.P_Gain_ID "
                + "		And P_Gain_Parameter.P_Collective_Labour_Agr_ID is null "
                + "		) "
                + "   and P_Column_Adm_ID = " + this.columnAdmId
                + "   And IsAdmissible = 'Y' "
                + " )"
                + " and not exists ( select tag.value "
                + " from p_gain tag, p_gain_gaininfo ggi, p_gaininfo info "
                + " where info.p_gaininfo_id = ggi.p_gaininfo_id "
                + " and tag.p_gain_id = ggi.p_gain_id "
                + " and tag.p_gain_id =  " + gainId
                + " and info.value = 'REDUC' "
                + " and ggi.To_Consider = 'Y' " 
                + " ) ";
                ;
            	
            PreparedStatement stmt = DB.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            boolean valid = rs.next();            
            rs.close();
            stmt.close();
            
            return valid;
        }
        
        /**
         * Cette méthode vérifie si une affectation est expirée
         */
        private boolean isExpired(int assignmentId) throws Exception
        {
            String sql
            = "SELECT 1"
                + " FROM P_Assignment"
                + " WHERE P_Assignment_ID = " +assignmentId
                + " AND EndDate is not null AND EndDate < ?";
            
            PreparedStatement stmt = DB.prepareStatement(sql);
            stmt.setTimestamp(1, this.startDate);
            ResultSet rs = stmt.executeQuery();
            
            boolean expired = rs.next();
            
            rs.close();
            stmt.close();
            
            return expired;
        } 
        
        private BigDecimal getHourlyRateFromOtherFTD(int paymentId, int assignmentId, Timestamp day)
        {
        	BigDecimal retValue = Env.ZERO;
        	String sql = " SELECT Hourly_Rate "
        		+ " FROM P_Payment_Gain "
        		+ " WHERE P_Payment_ID = "+paymentId
        		+ " AND P_Assignment_ID = "+assignmentId
        		+ " And Day <= ? "
        		+ " Order by Day desc ";
        	
        	PreparedStatement pstm = DB.prepareStatement(sql);
        	try
        	{
        		pstm.setTimestamp(1, day);
        		ResultSet rs = pstm.executeQuery();
        		boolean bContinu = true;
        		while (rs.next() && bContinu == true)
        		{
        			retValue = rs.getBigDecimal("Hourly_Rate");
        			if(retValue != null && retValue.compareTo(Env.ZERO) > 0)
        			{
        				bContinu = false;
        			}
        		}
        		
        		rs.close();
        		pstm.close();
        	}
        	catch(Exception e)
        	{
        		log.log (Level.SEVERE, "getHourlyRateFromOtherFTD", e);
        	}
        	
        	if(retValue == null || retValue.compareTo(Env.ZERO) == 0)
        	{
        		//log.log(Level.SEVERE, "getHourlyRateFromOtherFTD, Aucun Taux Horaire Trouvé : date = '"+day+"', sql = "+sql);
        	}
        	return retValue;
        }
    }
    
    /*
     * Members
     */
    private boolean flagMAJ;
    private int period_ID;
    private String recordType;
    private int employeeId;
    private int periodId;
    private int yearId;
    private int deductionId;
    private Timestamp lastYearDate;
    private Timestamp firstYearDate;
    private BigDecimal annualBase;
    private int nbrPeriod;
    private int columnAdmId;
    private int rwtId;
    private int	assignmentCounter;
    private Timestamp dateDebutPremierAff;
    private Timestamp dateFinDerniereAff;
    private BigDecimal BaseSalary;
    private BigDecimal contributoryDays;
    private BigDecimal motherhoodDays;
    private BigDecimal disabilityDays;
    private BigDecimal computedService;
    private BigDecimal creditedService;
    private BigDecimal contributorySalary;
    private BigDecimal exoneratedSalary;
    private BigDecimal motherhoodSalary;
    private BigDecimal deductibleSalary;
    private BigDecimal annualDeductibleSalaray;
    private BigDecimal maximumAmountETC;
    private BigDecimal theoriticalRetro;
    private BigDecimal adjustedDeductibleSalaray;
    private BigDecimal YMPE;
    private BigDecimal exemption;
    private BigDecimal exemptionPercentage;
    private BigDecimal adjustmentFactorStandardAmount;
    private BigDecimal calculPercentage;
    private BigDecimal RRSP;
    private BigDecimal adjustmentFactor;
    private BigDecimal manualRetro;
    private BigDecimal adjustementFactorAmount;
    private BigDecimal maximumAmountFE;
    private BigDecimal leaveOfAbsenceWithoutPay;
    private BigDecimal adjustedLeaveOfAbsenceDays;
    private BigDecimal workTimePercentageDiff;
    private BigDecimal workTimePercentage;
    private BigDecimal workTimePercentageWithoutDiff;
    private String	   strStatus;
    private BigDecimal joursTRD;
    private final BigDecimal bdHundred = new BigDecimal(100);

    public CLogger			log = CLogger.getCLogger (getClass());
    /**
     * Constructeur
     */

    public int cmptInsertion = 0;
    public int cmptUpdate = 0;

    public P_Carra(	int P_Period_ID, 
    				int P_Employee_ID,
    				int P_Deduction_ID,
    				int P_Year_ID,
    				boolean Update,
    				int NumberOfPeriod,
    				String RecordType)
    {
        this.flagMAJ = false;
        this.period_ID = 0;
        this.recordType = "";
        this.yearId = 0;
        this.deductionId = 0;
        this.employeeId = 0;
        this.nbrPeriod = 0;
        this.joursTRD = Env.ZERO;
        this.dateDebutPremierAff = null;
        this.dateFinDerniereAff = null;
        this.BaseSalary = Env.ZERO;
        this.contributoryDays = Env.ZERO;
        this.motherhoodDays = Env.ZERO;
        this.disabilityDays = Env.ZERO;
        this.computedService = Env.ZERO;
        this.creditedService = Env.ZERO;
        this.contributorySalary = Env.ZERO;
        this.exoneratedSalary = Env.ZERO;
        this.motherhoodSalary = Env.ZERO;
        this.deductibleSalary = Env.ZERO;
        this.annualDeductibleSalaray = Env.ZERO;
        this.maximumAmountETC = Env.ZERO;
        this.theoriticalRetro = Env.ZERO;
        this.adjustedDeductibleSalaray = Env.ZERO;
        this.YMPE = Env.ZERO;
        this.exemption = Env.ZERO;
        this.exemptionPercentage = Env.ZERO;
        this.adjustmentFactorStandardAmount = Env.ZERO;
        this.calculPercentage = Env.ZERO;
        this.RRSP = Env.ZERO;
        this.adjustmentFactor = Env.ZERO;
        this.manualRetro = Env.ZERO;
        this.adjustementFactorAmount = Env.ZERO;
        this.maximumAmountFE = Env.ZERO;
        this.leaveOfAbsenceWithoutPay = Env.ZERO;
        this.adjustedLeaveOfAbsenceDays = Env.ZERO;
        this.workTimePercentageDiff = Env.ZERO;
        this.workTimePercentage = Env.ZERO;
        this.workTimePercentageWithoutDiff = Env.ZERO;
        this.strStatus = "";
        
		this.period_ID = P_Period_ID;
		this.employeeId = P_Employee_ID;
		this.deductionId = P_Deduction_ID;
		this.yearId = P_Year_ID;
		this.flagMAJ = Update;
		this.nbrPeriod = NumberOfPeriod;
		this.recordType = RecordType;



    }

    /**
     * Traitement principal
     */
    protected String doIt() throws Exception
    {
    	if(this.nbrPeriod == 0)
    		return "Le nombre de périodes doit être supérieur à 0";
        // On doit d'abord obtenir première date de début de la plus petite période
        // de paie de l'année de référence et la plus grande de la dernière période
        this.setMinMaxDates();
        
        // On va chercher la olonne d'admissibilité associée à cette déduction à
        // l'aide des dates de début et de fin de la période
        String sql
        = "select deductionParam.P_Column_Adm_ID, deductionParam.P_RWT_ID"
            + " from P_Deduction_Param deductionParam"
            + " where deductionParam.P_Deduction_ID = " + this.deductionId
            + " and EffectIn = ("
            + "   select max(EffectIn)"
            + "   from P_Deduction_Param"
            + "   where P_Deduction_ID = deductionParam.P_Deduction_ID"
            + "   and EffectIn <= " + DB.TO_DATE(this.lastYearDate) //getdate()"
            + " )";
        
        PreparedStatement stmt = DB.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
        {
            this.columnAdmId = rs.getObject("P_Column_Adm_ID") == null ? 0 : rs.getInt("P_Column_Adm_ID");
            this.rwtId = rs.getObject("P_RWT_ID") == null ? 0 : rs.getInt("P_RWT_ID");
            
            // On va chercher le dossier CARRA de la déduction
            try
            {
            this.processCARRA();
            }
            catch(Exception e)
            {
        		log.log (Level.SEVERE, "doit", e);
            	//e.printStackTrace();
            	return "Erreur pendant le traitement. "+e;
            }
        }
        
        rs.close();
        stmt.close();
        
        return Msg.translate(Env.getCtx(), "Success - @Inserted@ = " + cmptInsertion  + " @Updated@" + cmptUpdate );

    }
    
    /**
     * Cette méthode traite le dossier CARRA pour la déduction à l'année de référence
     */
    private void processCARRA() throws Exception
    {
        String sql
        = "select Annual_Base, MaximumAmountETC, MaximumAmountFE, CalculPercentage,"
            + " RRSP, ExemptionPercentage, YMPE, CARRA"
            + " from P_Equivalence_Factor_Param"
            + " where P_Deduction_ID = " + this.deductionId
            + " and P_Year_ID = " + this.yearId
            + " and IsActive = 'Y'";
        
        PreparedStatement stmt = DB.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
        {
            this.annualBase = rs.getBigDecimal("Annual_Base");
            this.maximumAmountETC = rs.getBigDecimal("MaximumAmountETC");
            this.YMPE = rs.getBigDecimal("YMPE");
            this.exemptionPercentage = rs.getBigDecimal("ExemptionPercentage");
            this.calculPercentage = rs.getBigDecimal("CalculPercentage");
            this.RRSP = rs.getBigDecimal("RRSP");
            this.maximumAmountFE = rs.getBigDecimal("MaximumAmountFE");
            // On va chercher le dossier Employé Déduction pour l'employé et la déduction en
            // paramètre. Si on a passé un null comme valeur au paramètre d'employé, on va
            // chercher le dossier employé déduction pour tous les employés et la déduction
            // passée en paramètre.
            this.processEmployeeDeduction();
        }
        
        rs.close();
        stmt.close();
    }
    
    /**
     * Traite le dossier de l'employé déduction
     */
    private void processEmployeeDeduction() throws Exception
    {
        // On va chercher le dossier Employé Déduction pour l'employé et la déduction en
        // paramètre. Si on a passé un null comme valeur au paramètre d'employé, on va
        // chercher le dossier employé déduction pour tous les employés et la déduction
        // passée en paramètre.
        String sql
        = "select P_Employee_Deduction_ID, P_Employee_ID, Date35, EffectIn, EffectTo"
            + " from P_Employee_Deduction employeeDeduction"
            + " where P_Deduction_ID = " + this.deductionId
            + " and ( EffectTo is null OR EffectTo > " + DB.TO_DATE(this.firstYearDate) + " )" //between " + DB.TO_DATE(this.firstYearDate) + " AND " + DB.TO_DATE(this.lastYearDate) + ") "
            + (this.employeeId == 0 ? "" : " and P_Employee_ID = " + this.employeeId)
            + " and EffectIn = ("
            + "   select max(EffectIn)"
            + "   from P_Employee_Deduction"
            + "   where P_Employee_ID = employeeDeduction.P_Employee_ID"
            + "   and P_Deduction_ID = employeeDeduction.P_Deduction_ID"
            + "   and EffectIn < " + DB.TO_DATE(this.lastYearDate)
            + " ) order by P_Employee_ID ";
        
        PreparedStatement stmt = DB.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        /*try
        {*/
        while(rs.next())
        {
            boolean flagError = false;
            boolean flagExecution = false;
            boolean flagAjout = false;
            
            employee = P_Employee.get(Env.getCtx(), rs.getInt("P_Employee_ID"), null);
            log.log( Level.INFO, "CARRA - Employé : " + employee.getValue() + " " + employee.getName() );
            if(employee == null)
            {
            	//System.out.println("employé null : "+rs.getInt("P_Employee_ID"));
            	continue;
            }
            // Si la date de fin de l'assiciation employé déduction est différente de null
            // et est plus petite que la dernière date de l'année
            if(rs.getTimestamp("EffectTo") != null
                    && rs.getTimestamp("EffectTo").getTime() <= this.lastYearDate.getTime())
            {
                // Si l'employé courant possède d'autre association avec une autre déduction,
                // que ces associations soient valides dans l'année et que cette déduction
                // possède un dossier CARRA, on renvoit une erreur pour cet employé
                if(this.existsOtherAssociation(employee.getP_Employee_ID(), rs.getInt("P_Employee_Deduction_ID")))
                {
                    flagError = true;
                }
            }
            
            // On va chercher la date d'embauche, de départ et le statut d'emploi de l'employé courant
            P_Employee_Carra employeeCarra = new P_Employee_Carra(Env.getCtx(), -1, null);
            employeeCarra.setP_Employee_ID(rs.getInt("P_Employee_ID"));
            employeeCarra.setP_Deduction_ID(this.deductionId);
            employeeCarra.setP_Year_ID(this.yearId);
            Timestamp[] dates = this.setEmployeeStatut(employeeCarra);
            dates = this.findGoodDate(dates[0], dates[1], rs.getTimestamp("EffectIn"), rs.getTimestamp("EffectTo"), employeeCarra);
            
            if(this.firstYearDate.after(dates[0]) == true)
            	dates[0] = (Timestamp)this.firstYearDate.clone();
            
            if(this.lastYearDate.before(dates[1]) == true)
            	dates[1] = (Timestamp)this.lastYearDate.clone();
            
            // On va chercher la période qui contient la date de début et celle qui conteint la date de fin
            int startPeriodId = this.findPeriod(dates[0]);
            int endPeriodId = this.findPeriod(dates[1]);
            
            // S'il n'existe pas d'enregistrement dans la table P_Employee_Carra pour l'employé
            // courant, l'année courante et la déduction courante et le type d'enregistrement
            if(!this.alreadyExists(employeeCarra.getP_Employee_ID()))
            {
                flagAjout = true;
                flagExecution = true;
            }
            else
            {
                // On va chercher le flag de fin du dossier carra existant
                boolean isDone = this.getFlagFin(employeeCarra.getP_Employee_ID());
                flagAjout = false;
                
                // Si le flag de mise à jour passé en paramètre est actif et que
                // le flag de fin est égal à 0
                if(this.flagMAJ && !isDone)
                {
                    // On met le flag d'exécution à 1
                    flagExecution = true;
                }
            }
            
            // Si le flag d'erreur est actif, on met le flag d'exécution à 0
            flagExecution = flagExecution && !flagError;
            
            // Si le flag d'exécution est actif
            CalculValeur calculValeur = null;
            BigDecimal joursDAbsence = Env.ZERO;
            if(flagExecution)
            {
                // On instancie une variable Nombre jour = Base Annuel du dossier
                // carra * nombre de période (passé en paramètre) / 26
                BigDecimal nbrJours = this.annualBase.multiply(new BigDecimal((double)this.nbrPeriod)).divide(new BigDecimal(26.0d), 4, BigDecimal.ROUND_HALF_UP);
                
                // S'il existe un détail de feuille de temps pour l'employé courant
                // où la période du détail est entre la période de début et la période de
                // fin , et que la période du détail est semblable à la période filtre (année courante)
                if(this.existsTimesheetDetail(employeeCarra.getP_Employee_ID(), startPeriodId, endPeriodId))
                {
                    /*public CalculValeur(int periodId, int periodStartId, int periodEndId, Timestamp startDate,
                            Timestamp endDate, int employeeId, int columnAdmId, Timestamp date35, int deductionId,
                            int yearId, String recordType, BigDecimal bdDaysTRD)*/

                    // On calcul les valeurs
                    calculValeur = new CalculValeur(
                    		this.periodId, 
                    		startPeriodId, 
                    		endPeriodId, 
                    		dates[0], 
                    		dates[1], 
                    		employeeCarra.getP_Employee_ID(),
                            this.columnAdmId, 
                            rs.getTimestamp("Date35"), 
                            this.deductionId, 
                            this.yearId, 
                            this.recordType, 
                            this.joursTRD);
                    calculValeur.execute();
                    this.assignmentCounter = calculValeur.getAssignmentCounter();
                    this.BaseSalary = calculValeur.getBaseSalary();
                    this.contributoryDays = calculValeur.getContributoryDays();
                    this.motherhoodDays = calculValeur.getMotherhoodDays();
                    this.disabilityDays = calculValeur.getDisabilityDays();
                    this.contributorySalary = calculValeur.getContributorySalary();
                    this.exoneratedSalary = calculValeur.getExoneratedSalary();
                    this.motherhoodSalary = calculValeur.getMotherhoodSalary();
                    joursDAbsence = calculValeur.getAbsenceDays();
                    this.strStatus = calculValeur.getStrStatus();
                    //this.joursTRD = calculValeur.getjoursTRD();
                    this.joursTRD = calculValeur.getDaysTRD();
                }
                else
                {
                	//Si on a pas calculé de valeur pour cette employé, on passe au suivant
                	//TODO Message pour dire pourquoi on l'a pas calculé?
                	//System.out.println("Employé invalide");
                	
                	//System.out.println(employee.getValue());
                	continue;//while(rs.next()) line 840
                }
                boolean brisDeContrat = false;
                if(this.assignmentCounter > 1)
                {
                 	brisDeContrat = verifAffNonContinu(this.employeeId, this.firstYearDate, this.lastYearDate);
                }
                int numDays;
                if(this.assignmentCounter == 1 || brisDeContrat == false)
                {
                	Timestamp dateDebut = null;
                	Timestamp dateFin = null;
                	
                	if(this.assignmentCounter != 1 && brisDeContrat == false)
                	{
                		dateDebut = this.dateDebutPremierAff;
                		dateFin = this.dateFinDerniereAff;
                	}
                	else
                	{
                		P_Assignment assignment = P_Assignment.get(Env.getCtx(), calculValeur.getAssignmentId(), null);
                		dateDebut = assignment.getStartDate();
                		dateFin = assignment.getEndDate();
                		assignment = null;
                	}
                	
                	if(dateDebut == null || dateDebut.before(dates[0]))
                	{
                		dateDebut = (Timestamp)dates[0].clone();
                	}
                	
                	if(dateFin == null || dateFin.after(dates[1]))
                	{
                		dateFin = (Timestamp)dates[1].clone();
                	}
                	numDays = 0;
                	numDays = getNumWorkDaysInBetween(dateDebut, dateFin);
                	numDays++;//parce que la fonction de date 'Before' prend < mais pas <=
                }
                else
                {
                	numDays = this.annualBase.intValue();
                }
                
                //si les jours d'Absence = 0, les jours cotisable = Celing (jours cotisable)
                
                joursDAbsence = joursDAbsence.setScale(0, BigDecimal.ROUND_CEILING);
                if(joursDAbsence.compareTo(Env.ZERO) == 0)
                {
                	// Commence par arondire a 2 décimal. Car le celling est trop séver sinon.
                	//on fait un ceiling, mias comme on a pas la fonction ceiling sur BigDecimal, on divise par 1 avec arrondi Ceiling
                	this.contributoryDays = this.contributoryDays.setScale(2, BigDecimal.ROUND_HALF_UP); 
                	this.contributoryDays = this.contributoryDays.setScale(0, BigDecimal.ROUND_CEILING); 
                }
                else
                {
                	//on fait un arrondi au plus pres, mias comme on a pas la fonction 
                	//ROUND sur BigDecimal, on divise par 1 avec arrondi Halp_UP (au plus pres, et en haut si egal distance)
                	this.contributoryDays = this.contributoryDays.setScale(0, BigDecimal.ROUND_HALF_UP);               	
                }
                //Ceiling sur les champs suivants
                this.motherhoodDays = this.motherhoodDays.setScale(0, BigDecimal.ROUND_CEILING);
                this.disabilityDays = this.disabilityDays.setScale(0, BigDecimal.ROUND_CEILING);
                this.joursTRD = this.joursTRD.setScale(0, BigDecimal.ROUND_CEILING);
                //si la base annuel est plus petite que les jours de cot + les jours d'absence
                if(this.annualBase.compareTo(this.contributoryDays.add(joursDAbsence)) < 0)
                {
                	this.contributoryDays = this.contributoryDays.add(this.annualBase.subtract(this.contributoryDays.add(joursDAbsence)));
                }
//              Calcul FE Début               
                BigDecimal tempValue = this.annualBase.multiply(new BigDecimal(nbrPeriod).divide(new BigDecimal(26), 4, BigDecimal.ROUND_HALF_UP));
                //System.out.println("valeur de calcul : "+tempValue);
                //service calculés = (jours cot + jours inv + jours mat + jours trd ) / base annuel * nbr periode / 26
                this.computedService = this.contributoryDays.add(this.disabilityDays.add(this.motherhoodDays).add(this.joursTRD));
                this.computedService = this.computedService.divide(tempValue, 4, BigDecimal.ROUND_HALF_UP);
                this.computedService = this.computedService.setScale(2, BigDecimal.ROUND_HALF_UP);
                
                //service crédité = (jours cot + jours inv + jours mat ) / base annuel * nbr periode / 26
                this.creditedService = this.contributoryDays.add(this.disabilityDays.add(this.motherhoodDays));
                this.creditedService = this.creditedService.divide(tempValue, 4, BigDecimal.ROUND_HALF_UP);
                this.creditedService = this.creditedService.setScale(2, BigDecimal.ROUND_HALF_UP);

                //salaire admissible = salaire cotisable + salaire exonéré + salaire de maternité
                this.deductibleSalary = this.contributorySalary.add(this.exoneratedSalary.add(this.motherhoodSalary));
                //System.out.println("Salire admissible : "+this.deductibleSalary);
                //System.out.println("Salire cotisable : "+this.contributorySalary);
                //System.out.println("Salire exo : "+this.exoneratedSalary);
                //System.out.println("Salire maternité : "+this.motherhoodSalary);

                if(this.computedService.compareTo(Env.ZERO) != 0)
                {
                	//si le service calculé est différent de 0, on set annual deductible salary :
                	// salaire admissible / service calculé
                	this.annualDeductibleSalaray = this.deductibleSalary.divide(this.computedService, 4, BigDecimal.ROUND_HALF_UP);
                }
                
                if(this.annualDeductibleSalaray.compareTo(this.maximumAmountETC) > 0 && this.BaseSalary.compareTo(this.maximumAmountETC) < 0)
                {
                	this.theoriticalRetro = this.contributorySalary.subtract(this.BaseSalary);
                	//si la retro theorique est moins que 0, on l'a met a 0
                	if(this.theoriticalRetro.compareTo(Env.ZERO) < 0)
                	{
                		this.theoriticalRetro = Env.ZERO;
                	}
                }
                
                if(this.computedService != null && this.computedService.compareTo(Env.ZERO) != 0)
                {
                	//salaire admissible ajusté = salaire admissible - retro annuel / service calc * service credit
                	this.adjustedDeductibleSalaray = ((this.deductibleSalary.subtract(this.theoriticalRetro)).divide(this.computedService, 4, BigDecimal.ROUND_HALF_UP)).multiply(this.creditedService);
                }
                
                if(this.annualDeductibleSalaray.compareTo(this.YMPE) <= 0)
                {
                	this.exemption = this.exemptionPercentage.multiply(this.annualDeductibleSalaray.multiply(this.creditedService)).divide(bdHundred, 4, BigDecimal.ROUND_HALF_UP);
                }
                else
                {
                	this.exemption = this.exemptionPercentage.multiply(this.YMPE.multiply(this.creditedService)).divide(bdHundred, 4, BigDecimal.ROUND_HALF_UP);
                }
                if(this.exemption.compareTo(Env.ZERO) < 0)
                {
                	this.exemption = Env.ZERO;
                }

                this.adjustmentFactorStandardAmount = ((this.adjustedDeductibleSalaray.subtract(this.exemption))
                										.multiply(this.calculPercentage.divide(bdHundred, 4, BigDecimal.ROUND_HALF_UP))).subtract((this.creditedService.multiply(this.RRSP)));
                
                //System.out.println("adjustedDeductibleSalaray :"+this.adjustedDeductibleSalaray);
                //System.out.println("exemption :"+this.exemption);
                //System.out.println("calculPercentage :"+this.calculPercentage);
                //System.out.println("creditedService :"+this.creditedService);
                //System.out.println("RRSP :"+this.RRSP);
                if(this.adjustmentFactorStandardAmount.compareTo(Env.ZERO) < 0)
                {
                	this.adjustmentFactorStandardAmount = Env.ZERO;
                }
                
                this.adjustmentFactor = this.theoriticalRetro.multiply(this.calculPercentage);
                
                if(this.manualRetro.compareTo(Env.ZERO) != 0)
                {
                	this.adjustmentFactor = this.manualRetro.multiply(this.calculPercentage);
                }
                else
                {
                	this.manualRetro = this.theoriticalRetro;
                }
                
                if(this.adjustmentFactor.compareTo(Env.ZERO) < 0)
                {
                	this.adjustmentFactor = Env.ZERO;
                }
                
                this.adjustementFactorAmount = this.adjustmentFactorStandardAmount.add(this.adjustmentFactor);
                
                if(this.adjustementFactorAmount.compareTo(this.maximumAmountFE) > 0)
                {
                	this.adjustementFactorAmount = this.maximumAmountFE;
                }

                // si le service calculé est égal a 0 et le service crédité
                // alors le montant de Fe = 18% du salaire cotisable.
                if ( this.computedService.compareTo(Env.ZERO) == 0 && this.creditedService.compareTo(Env.ZERO) == 0 )
                {
                	this.adjustmentFactorStandardAmount = this.contributorySalary.multiply(this.calculPercentage.divide(bdHundred, 4, BigDecimal.ROUND_HALF_UP));
                	this.adjustementFactorAmount = this.contributorySalary.multiply(this.calculPercentage.divide(bdHundred, 4, BigDecimal.ROUND_HALF_UP));
                }
                
                this.leaveOfAbsenceWithoutPay = joursDAbsence.subtract(this.adjustedLeaveOfAbsenceDays);
                
                this.adjustedLeaveOfAbsenceDays = Env.ZERO;
              
                //nouvelle parti pour le tag 572 (info complementaire PCTTTOCC)
                BigDecimal bdAmountPCTTTOCC = Env.ZERO;
                P_Job_Title title = P_Job_Title.get(Env.getCtx(), employee.getP_Job_Title_ID(), null);
                BigDecimal PCTTTOCCdivider = title.getWeekly_Hours().divide(new BigDecimal("5"), 2, BigDecimal.ROUND_HALF_UP);
                String strPCTTTOCC = " SELECT isnull(sum(pg.QuantityCalc), 0) as quantity "
                	+ " FROM P_Payment_Gain pg, P_Payment, P_Period period "
                	+ " WHERE P_Payment.P_Payment_ID = pg.P_Payment_ID" 
                	+ " AND P_Payment.P_Employee_ID = " + employee.getP_Employee_ID()
                	+ " AND P_Payment.P_Period_ID = period.P_Period_ID "
                	+ " AND P_Payment.P_Year_ID = " + this.yearId
//                	+ " AND period.StartDate >= (SELECT period2.StartDate FROM P_Period period2 where period2.P_Period_ID = "+startPeriodId+" ) "
//                	+ " AND period.EndDate <= (SELECT period3.StartDate FROM P_Period period3 where period3.P_Period_ID = "+endPeriodId+" ) "
                	+ " AND pg.P_Gain_ID = ("
                	+ " 	SELECT gain.P_Gain_ID "
                	+ " 	FROM P_Gain gain, P_GainInfo info, P_Gain_GainInfo ggi "
                	+ "  	WHERE gain.P_Gain_ID = ggi.P_Gain_ID "
                	+ "   	AND info.P_GainInfo_ID = ggi.P_GainInfo_ID "
                	+ "		AND info.Value = 'PCTTTOCC' "
                	+ " 	AND TO_Consider = 'Y' "
                	+ " )";
                
                PreparedStatement pstmPCTTTOCC = DB.prepareStatement(strPCTTTOCC);
                ResultSet rsPCTTTOCC = pstmPCTTTOCC.executeQuery();
                if(rsPCTTTOCC.next())
                {
                	bdAmountPCTTTOCC = rsPCTTTOCC.getBigDecimal("quantity");
                	bdAmountPCTTTOCC = bdAmountPCTTTOCC.divide(PCTTTOCCdivider, 2, BigDecimal.ROUND_HALF_UP);
                }
                rsPCTTTOCC.close();
                pstmPCTTTOCC.close();
                pstmPCTTTOCC = null;
                rsPCTTTOCC = null;
                
                
                P_Job_Type empJobType = P_Job_Type.get(Env.getCtx(), employee.getP_Job_Type_ID(), null);
//                if(empJobType.getValue().substring(0, 4).toUpperCase().equals("OCCA"))
//                {
                	if(numDays != 0)
                	{
                		this.workTimePercentageDiff = (joursDAbsence.add(this.contributoryDays.add(this.joursTRD).add( bdAmountPCTTTOCC))).divide(new BigDecimal(numDays), 4, BigDecimal.ROUND_HALF_UP);
                	}
//                }
//                else
//                {
//                	this.workTimePercentageDiff = Env.ONE; //bdHundred;//100%
//                }
                
                if(this.workTimePercentageDiff.compareTo(Env.ZERO) == 0)
                {
                	//this.workTimePercentage = new BigDecimal(0.02); 
                	//Je ne sais pas que'ect-ce que ca fait ici pis ya pas de commentaire, pas fort
                	this.workTimePercentage = new BigDecimal("0.00");
                }
                else
                {
                	this.workTimePercentage = this.workTimePercentageDiff;
                }
                
                workTimePercentage = workTimePercentage.multiply( bdHundred ).setScale(0,BigDecimal.ROUND_HALF_UP);
                
                if(empJobType.getValue().substring(0, 4).toUpperCase().equals("OCCA") && strStatus.equals("7"))
                {
                	if(numDays != 0)
                	{
                		this.workTimePercentageWithoutDiff = (joursDAbsence.add(this.contributoryDays)).divide(new BigDecimal(numDays), 4, BigDecimal.ROUND_HALF_UP);
                	}
                	
                	this.workTimePercentage = new BigDecimal(1).subtract(this.workTimePercentageWithoutDiff.subtract(this.workTimePercentageDiff));
                	this.workTimePercentage = this.workTimePercentage.setScale(2, BigDecimal.ROUND_HALF_UP);
                	if(this.workTimePercentage.compareTo(Env.ZERO) == 0)
                	{
                		//Je ne sais pas qu'est-ce que ca fait ici pis ya pas de commentaire, pas fort
                		//this.workTimePercentage = new BigDecimal(2);
                		this.workTimePercentage = new BigDecimal("0.00");
                	}
                	
                	this.adjustedLeaveOfAbsenceDays = (bdHundred.subtract(this.workTimePercentageWithoutDiff.multiply(bdHundred))).multiply(nbrJours.multiply(new BigDecimal(nbrPeriod)).divide(new BigDecimal(26), 4, BigDecimal.ROUND_HALF_UP)).divide(bdHundred,  4, BigDecimal.ROUND_HALF_UP);
                }
//              ca donne 2600000, donc jdivise par 100000 pour continuer les tests
                joursDAbsence = joursDAbsence.add(this.adjustedLeaveOfAbsenceDays);
                //if(joursDAbsence.compareTo(new BigDecimal(100000)) > 0)
                //{
                //	joursDAbsence = joursDAbsence.divide(new BigDecimal(100000), 2);
                //}
                
                boolean flgOK = false;
                if(		this.contributorySalary.compareTo(Env.ZERO) != 0 ||
                		this.contributoryDays.compareTo(Env.ZERO) != 0 ||
                		this.exoneratedSalary.compareTo(Env.ZERO) != 0 ||
                		this.disabilityDays.compareTo(Env.ZERO) != 0 ||
                		this.motherhoodSalary.compareTo(Env.ZERO) != 0 ||
                		this.motherhoodDays.compareTo(Env.ZERO) != 0 ||
                		joursDAbsence.compareTo(Env.ZERO) != 0 ||
                		this.joursTRD.compareTo(Env.ZERO) != 0 ||
                		calculValeur.getNonContributorySalary().compareTo(Env.ZERO) != 0)
                {
                	flgOK = true;
                	P_Year year = P_Year.get(Env.getCtx(), this.yearId, null);
                	
                	//on termine en verifiant que l'employe n'a pas pris sa retraite l'an passe
                	Calendar firstDayOfYear = Calendar.getInstance();
                	Calendar lastDayOfYear= Calendar.getInstance();
                	firstDayOfYear.set(Calendar.YEAR, new Integer(year.getYear()).intValue() - 1);
                	firstDayOfYear.set(Calendar.MONTH, 0);
                	firstDayOfYear.set(Calendar.DAY_OF_MONTH, 1);
                	
                	lastDayOfYear.set(Calendar.YEAR, new Integer(year.getYear()).intValue() - 1);
                	lastDayOfYear.set(Calendar.MONTH, 11);
                	lastDayOfYear.set(Calendar.DAY_OF_MONTH, 31);
                	
                	if(employeeLeaveBetweenDates(employee.getP_Employee_ID(), new Timestamp(firstDayOfYear.getTimeInMillis()), new Timestamp(lastDayOfYear.getTimeInMillis())))
                	{
                		//date debut = 0??
                		//date fin = 9999??
                		this.computedService = Env.ZERO;
                		this.BaseSalary = Env.ZERO;
                	}
                }
                else
                {
                	flgOK = false;
                }
                
                this.BaseSalary = this.BaseSalary.setScale(4, BigDecimal.ROUND_HALF_UP);
                this.contributorySalary = this.contributorySalary.setScale(2, BigDecimal.ROUND_HALF_UP);
                this.exoneratedSalary = this.exoneratedSalary.setScale(2, BigDecimal.ROUND_HALF_UP);
                this.motherhoodSalary = this.motherhoodSalary.setScale(2, BigDecimal.ROUND_HALF_UP);
                this.deductibleSalary = this.deductibleSalary.setScale(2, BigDecimal.ROUND_HALF_UP);
                this.manualRetro = this.manualRetro.setScale(2, BigDecimal.ROUND_HALF_UP);
                this.theoriticalRetro = this.theoriticalRetro.setScale(2, BigDecimal.ROUND_HALF_UP);
                this.adjustmentFactor= this.adjustmentFactor.setScale(4, BigDecimal.ROUND_HALF_UP);

                this.adjustmentFactorStandardAmount = this.adjustmentFactorStandardAmount.setScale(4, BigDecimal.ROUND_HALF_UP);
                this.adjustementFactorAmount = this.adjustementFactorAmount.setScale(4, BigDecimal.ROUND_HALF_UP);
                this.computedService = this.computedService.setScale(2, BigDecimal.ROUND_HALF_UP);
                this.creditedService = this.creditedService.setScale(2, BigDecimal.ROUND_HALF_UP); 
                nbrJours = nbrJours.setScale(0, BigDecimal.ROUND_UNNECESSARY);
                joursDAbsence = joursDAbsence.setScale(4, BigDecimal.ROUND_HALF_UP);
  
// Calcul FE Fin                
                boolean recordExist = existRecord(employeeCarra.getP_Employee_ID(),
                									employeeCarra.getP_Deduction_ID(),
                									employeeCarra.getP_Year_ID(),
                									this.recordType);
                
                boolean newRecord = false;
                boolean go = false;
                if(recordExist == true && this.flagMAJ == true)
                {
                	newRecord = false;
                	go = true;
                }
                else if(recordExist == true && this.flagMAJ == false)
                {
                	newRecord = false;
                	go = false;
                }
                else if(recordExist == false && this.flagMAJ == true)
                {
                   	newRecord = true;
                	go = true;               	
                }
                if(recordExist == false && this.flagMAJ == false)
                {
                   	newRecord = true;
                	go = true;                	
                }
                
                if(flgOK == true && newRecord == true && go == true)
                {
                	//INSERTION
                	cmptInsertion++;
                	
                   	//TODO faire le lien entre siq et solstice (id - 0 ou 9999 ?????)
                	//voir document d'analyse
                	period_ID = 0;
                	
                	//employee, deduction and year already set
                	employeeCarra.setP_Job_Type_ID(employee.getP_Job_Type_ID());
                	employeeCarra.setP_RWT_ID(this.rwtId);
                	employeeCarra.setNumberOfDay(nbrJours);
                	employeeCarra.setIsActive(true ); //employee.isActive()
                	//dates already set
                	employeeCarra.setBaseSalary(this.BaseSalary);
                	employeeCarra.setContributorySalary(this.contributorySalary);
                	employeeCarra.setExoneratedSalary(this.exoneratedSalary);
                	employeeCarra.setMotherHoodSalary(this.motherhoodSalary);
                	employeeCarra.setEmployee_Part(calculValeur.getEmployeePart());
                	employeeCarra.setContributoryDays(this.contributoryDays);
                	employeeCarra.setDisabilityDays(this.disabilityDays);
                	employeeCarra.setMotherHoodDays(this.motherhoodDays);
                	employeeCarra.setLeaveOfAbsenceWithoutPay(joursDAbsence);
                	employeeCarra.setSelfFinancedLeaveDays(this.joursTRD);
                	employeeCarra.setDeductibleSalary(this.deductibleSalary);
                	employeeCarra.setTheoricalRetro(this.theoriticalRetro);
                	employeeCarra.setManualRetro(this.manualRetro);
                	employeeCarra.setAdjustmentFactor(this.adjustmentFactor);
                	employeeCarra.setComputedService(this.computedService);
                	employeeCarra.setCreditedService(this.creditedService);
                	employeeCarra.setRecordType(this.recordType);
                	employeeCarra.setP_Period_ID(period_ID);
                	employeeCarra.setAdjustedLeaveOfAbsenceDays(Env.ZERO);
                	employeeCarra.setAdjustmentFactorSTDAmount(this.adjustmentFactorStandardAmount);
                	employeeCarra.setNonContributorySalary(calculValeur.getNonContributorySalary());
                	employeeCarra.setAdjustmentFactorAmount(this.adjustementFactorAmount);
                	employeeCarra.setWorkedTimePercentage(this.workTimePercentage);
                	employeeCarra.setIsDone(false);
                	employeeCarra.setAssignmentQty(new BigDecimal(calculValeur.getAssignmentCounter()));
                	if ( strStatus.trim().length() != 0 )
                		employeeCarra.setStatus(strStatus);
                	else 
                		employeeCarra.setStatus( null );
                	employeeCarra.save();
                	//
                	// Le Salaire annuel de base doit être 0 quand la date de fin est 9999
                	//
                    if ( employeeCarra.getEffectToCarra() == "9999" )
                    {
                    	employeeCarra.setBaseSalary( Env.ZERO );
                    	employeeCarra.save();
                    }

                }
                if(flgOK == true && newRecord == false && go == true)
                {
                	//MISE À JOUR
                	cmptUpdate++;
                   	//TODO faire le lien entre siq et solstice (id - 0 ou 9999 ?????)
                	//voir document d analyse
                	int periode_id = 0;
                	
                	P_Employee_Carra employeeCarraMAJ = P_Employee_Carra.get(Env.getCtx(), employeeCarra.getP_Employee_ID(), employeeCarra.getP_Deduction_ID(), this.recordType, employeeCarra.getP_Year_ID(), null);
                	
                	if(employeeCarraMAJ == null)
                	{
                		System.out.print("Impossible de mettre à jour l'enregistrement pour l'ID employé '"+employeeCarra.getP_Employee_ID()+"'"
                				+" et pour l'ID déduction "+employeeCarra.getP_Deduction_ID());
                	}
                	else
                	{
                		employeeCarraMAJ.setP_Employee_ID(employeeCarra.getP_Employee_ID());
                		employeeCarraMAJ.setP_Deduction_ID(employeeCarra.getP_Deduction_ID());
                        employeeCarraMAJ.setP_Year_ID(employeeCarra.getP_Year_ID());
	                	employeeCarraMAJ.setP_Job_Type_ID(employee.getP_Job_Type_ID());
	                	employeeCarraMAJ.setP_RWT_ID(this.rwtId);
	                	employeeCarraMAJ.setNumberOfDay(nbrJours);
	                	employeeCarraMAJ.setIsActive(true); // employee.isActive()
	                	employeeCarraMAJ.setEffectIn(employeeCarra.getEffectIn());
	                	employeeCarraMAJ.setEffectTo(employeeCarra.getEffectTo());
	                	employeeCarraMAJ.setBaseSalary(this.BaseSalary);
	                	employeeCarraMAJ.setContributorySalary(this.contributorySalary);
	                	employeeCarraMAJ.setExoneratedSalary(this.exoneratedSalary);
	                	employeeCarraMAJ.setMotherHoodSalary(this.motherhoodSalary);
	                	employeeCarraMAJ.setEmployee_Part(calculValeur.getEmployeePart());
	                	employeeCarraMAJ.setContributoryDays(this.contributoryDays);
	                	employeeCarraMAJ.setDisabilityDays(this.disabilityDays);
	                	employeeCarraMAJ.setMotherHoodDays(this.motherhoodDays);
	                	employeeCarraMAJ.setLeaveOfAbsenceWithoutPay(joursDAbsence);
	                	if(joursDAbsence.compareTo(Env.ZERO) < 0)
	                	{
	                		System.out.println(" Current employee has CSTD neg : "+joursDAbsence);
	                	}
	                	employeeCarraMAJ.setSelfFinancedLeaveDays(this.joursTRD);
	                	employeeCarraMAJ.setDeductibleSalary(this.deductibleSalary);
	                	employeeCarraMAJ.setTheoricalRetro(this.theoriticalRetro);
	                	employeeCarraMAJ.setManualRetro(this.manualRetro);
	                	employeeCarraMAJ.setAdjustmentFactor(this.adjustmentFactor);
	                	employeeCarraMAJ.setComputedService(this.computedService);
	                	employeeCarraMAJ.setCreditedService(this.creditedService);
	                	employeeCarraMAJ.setRecordType(this.recordType);
	                	employeeCarraMAJ.setP_Period_ID(periode_id);
	                	employeeCarraMAJ.setAdjustedLeaveOfAbsenceDays(Env.ZERO);
	                	employeeCarraMAJ.setAdjustmentFactorSTDAmount(this.adjustmentFactorStandardAmount);
	                	employeeCarraMAJ.setNonContributorySalary(calculValeur.getNonContributorySalary());
	                	employeeCarraMAJ.setAdjustmentFactorAmount(this.adjustementFactorAmount);
	                	employeeCarraMAJ.setWorkedTimePercentage(this.workTimePercentage);
	                	employeeCarraMAJ.setIsDone(false);
	                	employeeCarraMAJ.setAssignmentQty(new BigDecimal(calculValeur.getAssignmentCounter()));
	                	if ( strStatus.trim().length() != 0 )
	                		employeeCarraMAJ.setStatus(strStatus);
	                	else
	                		employeeCarraMAJ.setStatus(null);
	                	employeeCarraMAJ.save();
	                	//
	                	// Le Salaire annuel de base doit être 0 quand la date de fin est 9999
	                	//
	                    if ( employeeCarraMAJ.getEffectToCarra() != null && employeeCarraMAJ.getEffectToCarra().equals( "9999" ))
	                    {
	                    	employeeCarraMAJ.setBaseSalary( Env.ZERO );
	                    	employeeCarraMAJ.save();
	                    }
	                	
	                	employeeCarraMAJ = null;


                	}
                }
                System.out.println("Number of inserted : "+cmptInsertion+" || number of updated : "+cmptUpdate);
            }
         	this.BaseSalary = null;
        	this.contributorySalary = null;
        	this.exoneratedSalary = null;
        	this.motherhoodSalary = null;
        	calculValeur = null;
        	this.contributoryDays = null;
        	this.disabilityDays = null;
        	this.motherhoodDays = null;
        	joursDAbsence = null;
        	this.joursTRD = null;
        	this.deductibleSalary = null;
        	this.theoriticalRetro = null;
        	this.manualRetro = null;
        	this.adjustmentFactor = null;
        	this.computedService = null;
        	this.creditedService = null;
        	//this.recordType = null;
        	this.adjustmentFactorStandardAmount = null;
        	this.adjustementFactorAmount = null;
        	this.workTimePercentage = null;
        	employee = null;
        	employeeCarra = null;
        	
            Runtime rt = Runtime.getRuntime();
            rt.gc();
            
         	this.BaseSalary = Env.ZERO;
        	this.contributorySalary = Env.ZERO;
        	this.exoneratedSalary = Env.ZERO;
        	this.motherhoodSalary = Env.ZERO;
        	this.contributoryDays = Env.ZERO;
        	this.disabilityDays = Env.ZERO;
        	this.motherhoodDays = Env.ZERO;
        	this.joursTRD = Env.ZERO;
        	this.deductibleSalary = Env.ZERO;
        	this.theoriticalRetro = Env.ZERO;
        	this.manualRetro = Env.ZERO;
        	this.adjustmentFactor = Env.ZERO;
        	this.computedService = Env.ZERO;
        	this.creditedService = Env.ZERO;
        	//this.recordType = "";
        	this.adjustmentFactorStandardAmount = Env.ZERO;
        	this.adjustementFactorAmount = Env.ZERO;
        	this.workTimePercentage = Env.ZERO;
        	
        }
        /*}
        catch(Exception e)
        {
        	System.out.println(e.toString());
        	e.printStackTrace();
        }*/
        
        rs.close();
        stmt.close();
    }
    
    //on verifie si l'employé a ca date de mise a pied entre les 2 dates données
    private boolean employeeLeaveBetweenDates(int iP_Employee_ID, Timestamp firstDate, Timestamp lastDate)
    {
    	boolean retValue = false;
    	String sql = "SELECT 1 FROM P_Employee "
    		+ " WHERE P_Employee_ID = "+iP_Employee_ID
    		+ " AND DateLayoff >= ? "
    		+ " AND DateLayoff <= ? ";
    	PreparedStatement pstm = DB.prepareStatement(sql);
    	try
    	{
    		pstm.setTimestamp(1, firstDate);
    		pstm.setTimestamp(2, lastDate);
    		ResultSet rs = pstm.executeQuery();
    		if(rs.next())
    		{
    			retValue = true;
    		}
    		rs.close();
    		rs = null;
    		pstm.close();
    		pstm = null;
    	}
    	catch(Exception e)
    	{
    		log.log (Level.SEVERE, "employeeLeaveBetweenDates", e);
    	}
    	return retValue;
    }
    
    //on va chercher le nombre de jour de travail entre la date de debut et la date de fin
    //on compte pas ni les samedis ni les dimanches
    private int getNumWorkDaysInBetween(Timestamp tsDateDebut, Timestamp tsDateFin)
    {
    	int retValue = 0;
    	
    	while(tsDateDebut.before(tsDateFin))
    	{
			Calendar cal = TimeUtil.getCalendar( tsDateDebut );
    		
    		//si diff de 0 (dimanche) et de 6 (samedi) on incremente
//    		if(tsDateDebut.getDay() != 0 && tsDateDebut.getDay() != 6)
  			if (  cal.get( Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
    			  cal.get( Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
    		   )
    		{
    			retValue ++;
    		}
//    		tsDateDebut.setDate(tsDateDebut.getDate()+1);
  			tsDateDebut = TimeUtil.addDays( tsDateDebut, 1);
    	}
    	
    	return retValue;
    }
    
   	//on navigue au travers toutes les affectations pour savoir 
	//s'il y a un bris entre les affectations
	//=> date debut d'aff est plus de 1 jours supérieur à la date
	//de fin d.affectation precedente

    private boolean verifAffNonContinu(int iP_Employee_ID, Timestamp tsDateDebut, Timestamp tsDateFin)
    {
    	boolean retValue = false;
    	
    	String sql = "SELECT StartDate, EndDate "
    		+ " FROM P_Assignment "
    		+ " WHERE P_Employee_ID = " + iP_Employee_ID
    		+ " AND StartDate < ? "
    		+ " AND ( EndDate >= ? or EndDate is null ) "
    		+ " ORDER BY StartDate ";
    	PreparedStatement pstmt = DB.prepareStatement(sql);
    	try
    	{
    		pstmt.setTimestamp(1, tsDateFin);
    		pstmt.setTimestamp(2, tsDateDebut); // 
	    	ResultSet rs = pstmt.executeQuery();
	    	//date affectation courante
	    	Timestamp dateDebCour = null;
	    	Timestamp dateFinCour = null;
	    	//date affectation precedente
	    	Timestamp dateFinPrec = null;
	    	//pour la premiere date de debut d'affectation
	    	int index = 0;
	    	while(rs.next())
	    	{
	    		if(index == 0)
	    			this.dateDebutPremierAff = rs.getTimestamp(1);
	    		//this.dateFinDerniereAff = rs.getTimestamp(2);
	    		dateDebCour = rs.getTimestamp(1);
	    		if ( rs.getTimestamp(2) != null )
	    			dateFinCour = rs.getTimestamp(2);
	    		else 
	    			dateFinCour = tsDateFin;
	    		//on ajoute une journée a la date de fin de la periode precedente
	    		//si c'est encore plus petit que la date de debut de la periode courante
	    		//on a un bris
	    		if(dateFinPrec != null)
	    		{
//	    			dateFinPrec.setDate(dateFinPrec.getDate() + 1);
	    			dateFinPrec = TimeUtil.addDays( dateFinPrec, 1);

	    			if(dateFinPrec.before(dateDebCour))
	    				retValue = true;
	    		}
	    		if(dateFinCour != null)
	    		{
	    			dateFinPrec = (Timestamp)dateFinCour.clone();
	    			this.dateFinDerniereAff = (Timestamp)dateFinCour.clone();
	    		}
	    		index++;
	    	}
	    	rs.close();
	    	pstmt.close();
    	}
    	catch(Exception e)
    	{
    		log.log (Level.SEVERE, "verifAffNonContinu", e);
    	}
    	return retValue;
    }
    /**
     * Cette méthode vérifie s'il existe un détail de feuille de temps pour l,employé à l'année courante
     */
    private boolean existsTimesheetDetail(int employeeId, int startPeriodId, int endPeriodId) throws Exception
    {
        String sql
          = " Select top 1 1 FROM P_Payment " 
          + " Where P_Year_ID = " + this.yearId
          + " AND P_Employee_ID = " + employeeId
          + " AND Exists( Select 1 From P_Payment_Gain Where P_Payment_Gain.P_Payment_ID = P_Payment.P_Payment_ID )"
          ;
/*        = "select 1"
            + " from P_Time_Sheet timeSheet inner join (P_Time_Sheet_Detail timeSheetDetail inner join P_Period period"
            + " on timeSheetDetail.P_Period_ID = period.P_Period_ID)"
            + " on timeSheet.P_Time_Sheet_ID = timeSheetDetail.P_Time_Sheet_ID"
            + " where timeSheet.P_Employee_ID = " + employeeId
            + " and period.P_Year_ID = " + this.yearId
            + " and period.StartDate >= (select StartDate from P_Period where P_Period_ID = " + startPeriodId + ")"
            + " and period.EndDate <= (select EndDate from P_Period where P_Period_ID = " + endPeriodId + ")";
 */       
        PreparedStatement stmt = DB.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        boolean exists = rs.next();
        
        rs.close();
        stmt.close();
        
        return exists;
    }
    
    /**
     * Retourne le flag de fin du dossier carra pour l,employé passé en paramètre avec
     * la déduction, l'année et le type d'enregistrmenet courant
     */
    private boolean getFlagFin(int employeeId) throws Exception
    {
        String sql
        = "select IsDone"
            + " from P_Employee_Carra"
            + " where P_Employee_ID = " + employeeId
            + " and P_Deduction_ID = " + this.deductionId
            + " and P_Year_ID = " + this.yearId
            + " and RecordType = '" + this.recordType + "'";
        
        PreparedStatement stmt = DB.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        boolean isDone = rs.next() ? rs.getString("IsDone").equals("Y") : false;
        
        rs.close();
        stmt.close();
        
        return isDone;
    }
    
    /**
     * Cette méthode vérifie s'il existe déjà un enregistremetn dans la table P_Employee_Carra
     * pour l'employé passé en paramètre avec la déduction et l'année courante et le type d'enregistrement
     */
    private boolean alreadyExists(int employeeId) throws Exception
    {
        String sql
        = "select 1"
            + " from P_Employee_Carra"
            + " where P_Employee_ID = " + employeeId
            + " and P_Deduction_ID = " + this.deductionId
            + " and P_Year_ID = " + this.yearId
        	+ " and RecordType = '" + this.recordType + "'";
        
        PreparedStatement stmt = DB.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        boolean alreadyExists = rs.next();
        
        rs.close();
        stmt.close();
        
        return alreadyExists;
    }
    
    /**
     * Cette méthode trouve la période qui conteint la date passée en paramètre
     */
    private int findPeriod(Timestamp date) throws Exception
    {
        String sql
        = "select P_Period_ID"
            + " from P_Period"
            + " where StartDate <= " + DB.TO_DATE(date)
            + " and EndDate >= " + DB.TO_DATE(date);
        
        PreparedStatement stmt = DB.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        int periodId = rs.next() ? rs.getInt("P_Period_ID") : 0;
        
        rs.close();
        stmt.close();
        
        return periodId;
    }
    
    /**
     * Cette méthode va chercher la date d'embauche, de départ et le statut d'emploi de
     * l'employé passé en paramètre
     * @return [0] = date d'embauche; [1] = date renvoit
     */
    private Timestamp[] setEmployeeStatut(P_Employee_Carra employeeCarra) throws Exception
    {
        Timestamp [] dates = new Timestamp[2];
        String sql
        = "select DateHired, DateReHired, DateLayoff, P_Job_Type_ID"
            + " from P_Employee"
            + " where P_Employee_ID = " + employeeCarra.getP_Employee_ID();
        
        PreparedStatement stmt = DB.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
        {
            dates[0] = rs.getTimestamp("DateHired");
            if ( rs.getTimestamp("DateReHired") != null )
            	dates[0] = rs.getTimestamp("DateReHired");
            
            dates[1] = rs.getTimestamp("DateLayoff");
            employeeCarra.setP_Job_Type_ID(rs.getInt("P_Job_Type_ID"));
        }
        
        rs.close();
        stmt.close();
        return dates;
    }
    
    /**
     * Cette méthode trouve les bonnes dates de début de fin pour le reste du traitement
     * à partir de la date d'embauche et la date de fin d'emploi.
     * @return [0] date début; [1] date fin
     */
    private Timestamp[] findGoodDate(Timestamp dateHired, Timestamp dateLayoff, Timestamp startDate, Timestamp endDate, P_Employee_Carra employeeCarra) throws Exception
    {
        Timestamp[] dates = new Timestamp[2];
        // Si la date de début de la relation déduction-employé courante est
        // plus grande que la date d'embauche de l'employé courant
        if(dateHired == null || (startDate != null && startDate.compareTo(dateHired) > 0))
            dates[0] = startDate;
        else
            dates[0] = dateHired;
        
        // Si la date de fin de la relation déduction-employé courante est plus
        // petite que la date de mise à pied de l'employé
        if(dateLayoff == null || (endDate != null && endDate.compareTo(dateLayoff) < 0))
            dates[1] = endDate;
        else
            dates[1] = dateLayoff;
        
        // Si l'année de la variable date de début est différente de l'année courrante,
        // on met null comme date de début
        //if(!this.equalsCurrentYear(dates[0]))
        //{
        //    dates[0] = null;
        //}
        
        // Si l'année de la variable date de fin est différente de l'année courrante,
        // on met null comme date de fin
        //if(!this.equalsCurrentYear(dates[1]))
        //{
        //    dates[1] = null;
        //}
        
        // Si la date de début de la première période de paie est plus grande ou égale
        // à la date de début, la date de début est égale à la date de début de la première
        // période et met la date de début du nouvel enregistrement à null
        if(dates[0] == null || this.firstYearDate.compareTo(dates[0]) >= 0)
        {
            dates[0] = this.firstYearDate;
            employeeCarra.setEffectIn(null);
        }
        else
        {
            // Sinon, on instancie la date de début du nouvel enregistrement à la date de début trouvée
            employeeCarra.setEffectIn(dates[0]);
        }
        
        // Si la date de fin de la dernière période de paie est plus petite que la date de fin,
        // la date de fin est égale à la date de fin de la dernière période et la date de fin
        // du nouvel enregistrement prend null
        if(dates[1] == null || this.lastYearDate.compareTo(dates[1]) <= 0)
        {
            dates[1] = this.lastYearDate;
            employeeCarra.setEffectTo(null);
        }
        else
        {
            // Sinon, la date de fin du nouvel enregistrement prend la valeur de la date de fin
            employeeCarra.setEffectTo(dates[1]);
        }
        
        // Si la date de début est différente de null
        //if(dates[0] != null)
        if(1 == 12)
        {
            // Si elle existe, on va chercher la date de fin de relation employé déduction
            // pour une déduction différente de la courante (on vérifie s'il y a une 2eme
            // relation pour l'employé courant et l'année courante)
            String sql
            = "select EffectTo"
                + " from P_Employee_Deduction"
                + " where P_Employee_ID = " + employeeCarra.getP_Employee_ID()
                + " and P_Deduction_ID <> " + this.deductionId
                + " and isnull(year(EffectTo), 0) = ("
                + "   select [Year]"
                + "   from P_Year"
                + "   where P_Year_ID = " + this.yearId
                + " )";
            
            PreparedStatement stmt = DB.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                // La date de début est égale à la date de début de la première période
                dates[0] = this.firstYearDate;
            }
            
            rs.close();
            stmt.close();
        }
        
        //if(dates[1] != null)
        if(1 == 12)
        {
            // Si elle existe, on va chercher la date de début de la relation employé déduction
            // pour une déduction différente de la courante (on vérifie s'il y a une 2eme
            // relation pour lemployé courant et l'année courante)
            String sql
            = "select EffectIn"
                + " from P_Employee_Deduction"
                + " where P_Employee_ID = " + employeeCarra.getP_Employee_ID()
                + " and P_Deduction_ID <> " + this.deductionId
                + " and isnull(year(EffectIn), 0) = ("
                + "   select [Year]"
                + "   from P_Year"
                + "   where P_Year_ID = " + this.yearId
                + " )";
        
            PreparedStatement stmt = DB.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                // La date de fin est égale à null
                dates[1] = null;
            }
            
            rs.close();
            stmt.close();
        }
        
        // Si la date de fin est égale à null, la date de fin devient la date de fin de la dernière période
        if(dates[1] == null)
        {
            dates[1] = this.lastYearDate;
        }
        
        return dates;
    }
    
    /**
     * Cette méthode indique si l'année de la date passée en paramêtre est égale à l'année courrante
     */
    private boolean equalsCurrentYear(Timestamp date) throws Exception
    {
        String sql
        = "select 1"
            + " from P_Year"
            + " where [Year] = year(" + DB.TO_DATE(date) + ")"
        	+ " and P_Year_ID = " + this.yearId;
        
        PreparedStatement stmt = DB.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        boolean equals = rs.next();
        
        rs.close();
        stmt.close();
        
        return equals;
    }
    
    /**
     * Cette méthode vérifie s'il existe une autre association employé-déduction
     * que celle passé en paramètre pour l'employé au cours de l'année courrante
     * et touchant une autre déduction
     */
    private boolean existsOtherAssociation(int employeeId, int employeeDeductionId) throws Exception
    {
        String sql
        = "select 1"
            + " from P_Employee_Deduction"
            + " where P_Employee_ID = " + employeeId
            + " and P_Employee_Deduction_ID <> " + employeeDeductionId
            + " and EffectIn <= " + DB.TO_DATE(this.firstYearDate)
            + " and EffectTo >= " + DB.TO_DATE(this.lastYearDate)
            + " and IsActive = 'Y'"
            + " and exists ("
            + "   select 1"
            + "   from P_Equivalence_Factor_Param"
            + "   where P_Deduction_ID = P_Employee_Deduction.P_Deduction_ID"
            + "   and IsActive = 'Y'"
            + "   and P_Year_ID = " + this.yearId
            + " )";
        
        PreparedStatement stmt = DB.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        boolean exists = rs.next();
        
        rs.close();
        stmt.close();
        
        return exists;
    }
    
    /**
     * Cette méthode ajuste la plus petite et la plus grande dates référencées
     * par les périodes de paie de l'année de référence
     */
    private void setMinMaxDates() throws Exception
    {
        String sql
        = "select min(StartDate) \"Min\", max(EndDate) as \"Max\""
            + " from P_Period "
            + " where P_Year_ID = " + this.yearId;
        
        PreparedStatement stmt = DB.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
        {
            this.firstYearDate = rs.getTimestamp("Min");
            this.lastYearDate = rs.getTimestamp("Max");
        }
        
        rs.close();
        stmt.close();
    }
    
    /*
     * Permet de vérifier si on a un enregistrement pour 
     * un employé, une déduction, une année et un type d'enregistrement
     */
    private boolean existRecord(int iP_Employee_ID, 
    							int iP_Deduction_ID,
    							int iP_Year_ID,
    							String recordType)
    {
    	boolean retValue = false;
    	
    	String sql = " SELECT 1 "
    		+ " FROM P_Employee_Carra "
    		+ " WHERE P_Deduction_ID = "+iP_Deduction_ID
    		+ " AND P_Employee_ID = "+iP_Employee_ID
    		+ " AND P_Year_ID = "+iP_Year_ID
    		+ " AND RecordType = '"+recordType+"'";
    	
    	PreparedStatement pstm = DB.prepareStatement(sql);
    	try
    	{
    		ResultSet rs = pstm.executeQuery();
    		if(rs.next())
    		{
    			retValue = true;
    		}
    		rs.close();
    		pstm.close();
    	}
    	catch(Exception e)
    	{
    		log.log(Level.SEVERE, "existRecord, sql :"+sql, e);
    	}
    	return retValue;
    }
}
