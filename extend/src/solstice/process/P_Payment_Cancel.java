/*
 * Created on 2005-10-03
 */
package solstice.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Credits_Movement;
import solstice.model.P_Payment;
import solstice.model.P_Payment_Deduction;
import solstice.model.P_Payment_Distribution;
import solstice.model.P_Payment_Entryline;
import solstice.model.P_Payment_Gain;
import solstice.model.P_Payment_Gain_Distribution;
import solstice.model.P_Payment_Taxable_Benefit;
import solstice.model.P_Period;
import solstice.model.P_Time_Sheet;
import solstice.model.P_Time_Sheet_Detail;
import org.compiere.util.Msg;

/**
 * @author frafor01
 * 
 * Cette classe représente une procédure d'annulation de feuille de temps. Le principe
 * derrière cette procédure c'est de créer un enregistrement négatif pour le paiement
 * de la feuille. Ensuite, on doit annuler la feuille de temps. Si la feuille se trouve
 * dans la période de paie courrante, on doit en faire une copie qu'on place au statut
 * initiale.
 */
public class P_Payment_Cancel
{
    /*
     * Members
     */
    private P_Time_Sheet timeSheet;
    
    /*
     * Constructors
     */

    private P_Period period;

    /**
     * On construit l'objet de PO à partir du id de la feuille de temps et
     * d'un nom de transaction.
     */
    public P_Payment_Cancel(int timeSheetId, String trxName)
    {
        super();
        this.timeSheet = P_Time_Sheet.get(Env.getCtx(), timeSheetId, trxName);
    }

    /**
     * On utilise l'objet de PO passé en paramètre
     */
    public P_Payment_Cancel(P_Time_Sheet timeSheet)
    {
        super();
        this.timeSheet = timeSheet;
    }
    
    /**
     * On utilise le paiement pour obtenir la feuille de temps
     */
    public P_Payment_Cancel(P_Payment payment)
    {
        super();
        this.timeSheet = P_Time_Sheet.GetWithPaymentID(Env.getCtx(), payment.getP_Payment_ID(), payment.get_TrxName());
        //get( Env.getCtx(), this.timeSheet.getP_Period_ID(), this.timeSheet.get_TrxName());
    }

    /**
     * Traitement principal de l'annulation de la feuille de temps
     * @return Message d'erreur ou null
     */
    public String execute()
    {
        try
        {
	        // On doit d'abord s'assurer qu'il y a un paiement à annuler
	        if(this.timeSheet.getP_Payment_ID() != 0)
	        {
	            period = P_Period.getOpenPeriod(Env.getCtx(), timeSheet.getP_Payment_Group_ID(), timeSheet.get_TrxName());

	//            period = P_Period.get(Env.getCtx(), 1102509, null);
	            
	        	// On inverse maintenant le paiement et ses sous-tables en créant
	            // un paiement négatif
	            int newPaiementId = this.invertPaiement();
	            if(newPaiementId == 0)
	            {
	                return Msg.translate(Env.getCtx(), "ProcessFailed");
	            }
	            this.invertPaimentGain(newPaiementId);
	            this.invertPaimentDeduction(newPaiementId);
	            this.invertPaimentTaxableBenefit(newPaiementId);
                
                // On renverse ensuite le mouvement de banque
	            this.invertCreditsMovement(newPaiementId);
	            
	            // On renverse ensuite les entrées de paiements (P_Payment_EntryLine)
	            this.invertEntryLine(newPaiementId);
	            
	            // On renverse ensuite la distribution du paiement
	            this.invertPaymentDistribution(newPaiementId);
	            
/*                // On effectue une copie de la feuille de temps qu'on place au statut initial
                P_Time_Sheet timeSheetCopy = new P_Time_Sheet(Env.getCtx(), -1, this.timeSheet.get_TrxName());
                timeSheetCopy.setDescription(this.timeSheet.getDescription());
                if(this.timeSheet.getP_Distribution_Booklet_ID() > 0) timeSheetCopy.setP_Distribution_Booklet_ID(this.timeSheet.getP_Distribution_Booklet_ID());
                if(this.timeSheet.getP_Distribution_ID() > 0) timeSheetCopy.setP_Distribution_ID(this.timeSheet.getP_Distribution_ID());
                timeSheetCopy.setP_Employee_ID(this.timeSheet.getP_Employee_ID());
                timeSheetCopy.setP_Frequency_ID(this.timeSheet.getP_Frequency_ID());
                timeSheetCopy.setP_Job_Title_ID(this.timeSheet.getP_Job_Title_ID());
                timeSheetCopy.setP_Job_Type_ID(this.timeSheet.getP_Job_Type_ID());
                timeSheetCopy.setP_Occupation_Group_ID(this.timeSheet.getP_Occupation_Group_ID());
                timeSheetCopy.setP_Period_ID(this.timeSheet.getP_Period_ID());
                timeSheetCopy.setP_Year_ID(this.timeSheet.getP_Year_ID());
                timeSheetCopy.setSheetType(this.timeSheet.getSheetType());
                timeSheetCopy.setValue(this.generateTimeSheetValue());
                timeSheetCopy.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Initial);
                timeSheetCopy.setIsComplete(false);
                timeSheetCopy.setIsError(false);
                timeSheetCopy.setIsWarning(false);
                timeSheetCopy.setP_Payment_ID(newPaiementId);
                timeSheetCopy.setPaymentType(this.timeSheet.getPaymentType());
                
                if(!timeSheetCopy.save())
                    return Msg.translate(Env.getLanguage(Env.getCtx()), "ProcessFailed");
                
                // On copie ensuite le détail de la feuille de temps
                if(!this.copyDetail(timeSheetCopy.getP_Time_Sheet_ID()))
                    return Msg.translate(Env.getLanguage(Env.getCtx()), "ProcessFailed");
*/                    
	        }
	        
	        // On annule ensuite le paiement d'origine
	        DB.executeUpdate(
	                "UPDATE P_Payment SET IsCancelled = 'Y', PaymentTypeDoc = 'Annulation+'" +
	                " WHERE P_Payment_ID = " + this.timeSheet.getP_Payment_ID(), null);
	        
	        // On change finalement le statut de la feuille de temps pour l'annuler
	        this.timeSheet.setTimeSheetStatus(P_Time_Sheet.TIMESHEETSTATUS_Cancelled);
	        this.timeSheet.setDescription("Annulation");
	        if(!this.timeSheet.save())
	            return Msg.translate(Env.getCtx(), "ProcessFailed");
	        
	        return "Ce paiement à été annulé, vous pouvez maintenant effectuer le transfert au GL";

//	        return "Success";
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
            return Msg.translate(Env.getCtx(), "ProcessFailed");
        }
    }
    
    /**
     * Cette méthode copie le détail de la feuille de temps du constructuer
     * sous la feuille de temps dont le id est passé en paramètre
     * @return VRAI si l'opération a réussit
     */
    private boolean copyDetail(int timeSheetId) throws SQLException
    {
        // On doit d'abord sortir la liste des P_Time_Sheet_Detail pour la
        // feuille de temps du constructeur
        String sql
        = "select P_Time_Sheet_Detail_ID, [Day], P_Assignment_ID, P_Period_ID, DayQty, P_Gain_ID,"
            + " C_Campaign_ID, C_Project_ID, C_Activity_ID, P_Schedule_ID, SalaryPercentage,"
            + " WeekIndex, P_Employee_LongTermLeave_ID, OriginTime, StartDate, EndDate"
            + " from P_Time_Sheet_Detail"
            + " where P_Time_Sheet_ID = " + this.timeSheet.getP_Time_Sheet_ID();
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // Pour chaque enregistrement de détail, on doit récupérer l'information
            // de ceux-ci et créer un nouvel enregistrement pour la feuille de temps
            // dont le id est passé en paramètre.
            P_Time_Sheet_Detail timeSheetDetail = new P_Time_Sheet_Detail(Env.getCtx(), -1, this.timeSheet.get_TrxName());
            timeSheetDetail.setP_Time_Sheet_ID(timeSheetId);
            timeSheetDetail.setDay(rs.getTimestamp("Day"));
            timeSheetDetail.setP_Period_ID(rs.getInt("P_Period_ID"));
            timeSheetDetail.setP_Schedule_ID(rs.getInt("P_Schedule_ID"));
            if(rs.getObject("P_Assignment_ID") != null) timeSheetDetail.setP_Assignment_ID(rs.getInt("P_Assignment_ID"));
            if(rs.getObject("DayQty") != null) timeSheetDetail.setDayQty(rs.getBigDecimal("DayQty"));
            if(rs.getObject("P_Gain_ID") != null) timeSheetDetail.setP_Gain_ID(rs.getInt("P_Gain_ID"));
            if(rs.getObject("C_Campaign_ID") != null) timeSheetDetail.setC_Campaign_ID(rs.getInt("C_Campaign_ID"));
            if(rs.getObject("C_Project_ID") != null) timeSheetDetail.setC_Project_ID(rs.getInt("C_Project_ID"));
            if(rs.getObject("C_Activity_ID") != null) timeSheetDetail.setC_Activity_ID(rs.getInt("C_Activity_ID"));
            if(rs.getObject("SalaryPercentage") != null) timeSheetDetail.setSalaryPercentage(rs.getBigDecimal("SalaryPercentage"));
            if(rs.getObject("WeekIndex") != null) timeSheetDetail.setWeekIndex(rs.getInt("WeekIndex"));
            if(rs.getObject("P_Employee_LongTermLeave_ID") != null) timeSheetDetail.setP_Employee_LongTermLeave_ID(rs.getInt("P_Employee_LongTermLeave_ID"));
            if(rs.getObject("OriginTime") != null) timeSheetDetail.setOriginTime(rs.getString("OriginTime"));
            if(rs.getObject("StartDate") != null) timeSheetDetail.setStartDate(rs.getTimestamp("StartDate"));
            if(rs.getObject("EndDate") != null) timeSheetDetail.setEndDate(rs.getTimestamp("EndDate"));
            
            if(!timeSheetDetail.save())
            {
                rs.close();
                stmt.close();
                return false;
            }
        }
        
        rs.close();
        stmt.close();
        
        return true;
    }
    
    /**
     * Cette méthode crée un nouveau paiement à partir du paiement du constructeur
     * et retourne le nouvel id. Ce paiement doit servir pour inverser le paiement
     */
    private int invertPaiement() throws SQLException
    {
        // On récupère d'abord l'information du paiement à inverser
        String sql
        = "select Value, Name, Description, P_Employee_ID, PayDate, PaymentType, P_Employer_ID,"
            + " P_Period_ID, P_Year_ID, P_Frequency_ID, P_Payment_Group_ID, IsReconciled,"
            + " DateTransfer, IsTransfer, PreprintedNo, AnnualSalary, P_Occupation_Group_ID, "
            + " P_Job_Title_ID, P_Job_Type_ID, P_Distribution_ID, P_Distribution_Booklet_ID, "
            + " SalaryPercentage, Taxation_Region_ID,  AD_Org_ID,  P_Department_ID, C_Activity_ID "
            + " from P_Payment"
            + " where P_Payment_ID = " + this.timeSheet.getP_Payment_ID();
        
        int paymentId = 0;
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
        {
            // On crée maintenant le nouveau paiement
            P_Payment payment = new P_Payment(Env.getCtx(), -1, this.timeSheet.get_TrxName());
            payment.setPaymentTypeDoc(P_Payment.PAYMENTTYPEDOC_Annulation_);
            payment.setValue(this.generateTimeSheetValue());
            payment.setName(rs.getString("Name"));
            payment.setDescription(rs.getString("Description"));
            payment.setP_Employee_ID(rs.getInt("P_Employee_ID"));
            payment.setPayDate(rs.getTimestamp("PayDate"));
            payment.setPaymentType(rs.getString("PaymentType"));
            payment.setP_Employer_ID(rs.getInt("P_Employer_ID"));
            payment.setP_Period_ID(period.getP_Period_ID());
            payment.setP_Year_ID(rs.getInt("P_Year_ID"));
            payment.setP_Frequency_ID(rs.getInt("P_Frequency_ID"));
            payment.setP_Payment_Group_ID(rs.getInt("P_Payment_Group_ID"));
            payment.setTaxation_Region_ID(rs.getInt("Taxation_Region_ID"));
			payment.setAD_Org_ID( rs.getInt("AD_Org_ID"));
			payment.setP_Department_ID( rs.getInt("P_Department_ID"));
			payment.setC_Activity_ID( rs.getInt("C_Activity_ID"));

            payment.setIsReconciled(false);
            payment.setDateTransfer(null);
            payment.setIsTransfer(false);
            payment.setPrePrintedNo(rs.getString("PreprintedNo"));
            payment.setAnnualSalary(rs.getBigDecimal("AnnualSalary"));
            payment.setIsCancelled("Y");
            if(rs.getInt("P_Occupation_Group_ID") != 0)
            	payment.setP_Occupation_Group_ID(rs.getInt("P_Occupation_Group_ID"));
            if(rs.getInt("P_Job_Title_ID") != 0)
            	payment.setP_Job_Title_ID(rs.getInt("P_Job_Title_ID"));
            if(rs.getInt("P_Job_Type_ID") != 0)
            	payment.setP_Job_Type_ID(rs.getInt("P_Job_Type_ID"));
            if(rs.getInt("P_Distribution_ID") != 0)
            	payment.setP_Distribution_ID(rs.getInt("P_Distribution_ID"));
            if(rs.getInt("P_Distribution_Booklet_ID") != 0)
            	payment.setP_Distribution_Booklet_ID(rs.getInt("P_Distribution_Booklet_ID"));
            payment.setSalaryPercentage(rs.getBigDecimal("SalaryPercentage"));
            
            if(payment.save())
                paymentId = payment.getP_Payment_ID();
            
        }
        
        rs.close();
        stmt.close();
        
        return paymentId;
    }
    
    /**
     * Cette méthode crée des enregistrements dans P_Payment_Taxable_Benefit pour inverser les
     * paiements du constructeur vers le paiement dont l'id est passé en paramètre
     */
    private void invertPaimentTaxableBenefit(int paymentId) throws SQLException
    {
        // On récupère les enregistrements de la table P_Payment_Taxable_Benefit. Il est
        // à notter que le montant est directement inversé dans la requête
        String sql
        = "select P_TAXABLE_BENEFIT_ID, TAXABLE_BENEFIT_AMOUNT * -1 as TAXABLE_BENEFIT_AMOUNT,"
            + " P_PERIOD_ID, P_EMPLOYEE_ID, P_YEAR_ID"
            + " from P_Payment_Taxable_Benefit"
            + " where P_Payment_ID = " + this.timeSheet.getP_Payment_ID();
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée le nouvel enregistrement
            P_Payment_Taxable_Benefit paymentTaxableBenefit = new P_Payment_Taxable_Benefit(Env.getCtx(), -1, this.timeSheet.get_TrxName());
            paymentTaxableBenefit.setP_Payment_ID(paymentId);
            paymentTaxableBenefit.setP_Taxable_Benefit_ID(rs.getInt("P_TAXABLE_BENEFIT_ID"));
            paymentTaxableBenefit.setTaxable_Benefit_Amount(rs.getBigDecimal("TAXABLE_BENEFIT_AMOUNT"));
            paymentTaxableBenefit.setP_Period_ID(rs.getInt("P_PERIOD_ID"));
            paymentTaxableBenefit.setP_Employee_ID(rs.getInt("P_EMPLOYEE_ID"));
            paymentTaxableBenefit.setP_Year_ID(rs.getInt("P_YEAR_ID"));
            
            if(!paymentTaxableBenefit.save())
            {
                rs.close();
                stmt.close();
                throw new SQLException("sauvegarde du P_Payment_Taxable_Benefit échouée");
            }
        }
        
        rs.close();
        stmt.close();
    }
    
    /**
     * Cette méthode crée des enregistrements dans P_Payment_Deduction pour inverser les
     * paiements du constructeur vers le paiement dont l'id est passé en paramètre
     */
    private void invertPaimentDeduction(int paymentId) throws SQLException
    {
        // On récupère les enregistrements de la table P_Payment_Deduction. Il est
        // à notter que le montant est directement inversé dans la requête
        String sql
        = "select P_DEDUCTION_ID, SALARY_ELIGIBLE * -1 as SALARY_ELIGIBLE, HOURS_ELIGIBLE * -1 as HOURS_ELIGIBLE, "
            + " EMPLOYEE_PART * -1 as EMPLOYEE_PART, EMPLOYER_PART * -1 as EMPLOYER_PART, P_PERIOD_ID, "
            + " ACCUMULATIONAMOUNT * -1 as ACCUMULATIONAMOUNT, RETRIEVALAMOUNT * -1 as RETRIEVALAMOUNT, P_EMPLOYEE_ID, P_YEAR_ID, PensionFundCharges * -1 as PensionFundCharges, Origine "
            + " from P_Payment_Deduction"
            + " where P_Payment_ID = " + this.timeSheet.getP_Payment_ID();
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée le nouvel enregistrement
            P_Payment_Deduction paymentDeduction = new P_Payment_Deduction(Env.getCtx(), -1, this.timeSheet.get_TrxName());
            paymentDeduction.setP_Payment_ID(paymentId);
            paymentDeduction.setP_Deduction_ID(rs.getInt("P_DEDUCTION_ID"));
            paymentDeduction.setSalary_Eligible(rs.getBigDecimal("SALARY_ELIGIBLE"));
            paymentDeduction.setHours_Eligible(rs.getBigDecimal("HOURS_ELIGIBLE"));
            paymentDeduction.setEmployee_Part(rs.getBigDecimal("EMPLOYEE_PART"));
            paymentDeduction.setEmployer_Part(rs.getBigDecimal("EMPLOYER_PART"));
            paymentDeduction.setP_Period_ID(rs.getInt("P_PERIOD_ID"));
            paymentDeduction.setAccumulationAmount(rs.getBigDecimal("ACCUMULATIONAMOUNT"));
            paymentDeduction.setRetrievalAmount(rs.getBigDecimal("RETRIEVALAMOUNT"));
            paymentDeduction.setP_Employee_ID(rs.getInt("P_EMPLOYEE_ID"));
            paymentDeduction.setP_Year_ID(rs.getInt("P_YEAR_ID"));
            paymentDeduction.setPensionFundCharges(rs.getBigDecimal("PensionFundCharges"));
            paymentDeduction.setOrigine( rs.getString("Origine"));
            
            if(!paymentDeduction.save())
            {
                rs.close();
                stmt.close();
                throw new SQLException("sauvegarde du P_Payment_Deduction échouée");
            }
        }
        
        rs.close();
        stmt.close();
    }
    
    /**
     * Cette méthode crée des enregistrements dans P_Payment_Gain pour inverser les
     * paiements du constructeur vers le paiement dont l'id est passé en paramètre
     */
    private void invertPaimentGain(int paymentId) throws SQLException
    {
        // On récupère les enregistrements de la table P_Payment_Gain. Il est
        // à notter que le montant est directement inversé dans la requête
        String sql
        = "select P_EMPLOYEE_ID, P_ASSIGNMENT_ID, P_GAIN_ID, "
            + " HOURLY_RATE, TYPE_RATE, MULTIPLYRATE, "
            + " QUANTITY * -1 as QUANTITY, QUANTITYCALC * -1 as QUANTITYCALC, "
            + " AMOUNTCALC * -1 as AMOUNTCALC, FIXED_AMOUNT * -1 as FIXED_AMOUNT, BONUS_AMOUNT * -1 as BONUS_AMOUNT, "
            + " P_ADVANCE_ID, P_PERIOD_ID, LINE, "
            + " P_YEAR_ID, P_Post_ID, Day, C_Activity_ID, P_UOM_ID, P_Job_Title_ID, P_Salary_Scale_ID, P_Salary_Scale_Detail_ID, "
            + " P_Collective_Labour_Agr_ID, P_Gain_Parameter_ID, WeekIndex, SalaryPercentage, P_Employee_LongTermLeave_ID, "
            + " OriginTime, StartDate, EndDate, P_Payment_Gain_ID "
            + " from P_Payment_Gain"
            + " where P_Payment_ID = " + this.timeSheet.getP_Payment_ID();
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée maintenant le nouvel enregistrement
            P_Payment_Gain paymentGain = new P_Payment_Gain(Env.getCtx(), -1, this.timeSheet.get_TrxName());
            paymentGain.setP_Payment_ID(paymentId);
            paymentGain.setP_Employee_ID(rs.getInt("P_EMPLOYEE_ID"));
            paymentGain.setP_Assignment_ID(rs.getInt("P_ASSIGNMENT_ID"));
            paymentGain.setP_Gain_ID(rs.getInt("P_GAIN_ID"));
            paymentGain.setHourly_Rate(rs.getBigDecimal("HOURLY_RATE"));
            paymentGain.setType_Rate(rs.getString("TYPE_RATE"));
            paymentGain.setMultiplyRate(rs.getBigDecimal("MULTIPLYRATE"));
            paymentGain.setQuantity(rs.getBigDecimal("QUANTITY"));
            paymentGain.setQuantityCalc(rs.getBigDecimal("QUANTITYCALC"));
            paymentGain.setAmountCalc(rs.getBigDecimal("AMOUNTCALC"));
            paymentGain.setFixed_Amount(rs.getBigDecimal("FIXED_AMOUNT"));
            paymentGain.setBonus_Amount(rs.getBigDecimal("BONUS_AMOUNT"));
            paymentGain.setP_Advance_ID(rs.getInt("P_ADVANCE_ID"));
            paymentGain.setP_Period_ID(rs.getInt("P_PERIOD_ID"));
            paymentGain.setLine(rs.getInt("LINE"));
            paymentGain.setP_Year_ID(rs.getInt("P_YEAR_ID"));
            if(rs.getInt("P_Post_ID") != 0)
            	paymentGain.setP_Post_ID(rs.getInt("P_Post_ID"));
            paymentGain.setDay(rs.getTimestamp("Day"));
            if(rs.getInt("C_Activity_ID") != 0)
            	paymentGain.setC_Activity_ID(rs.getInt("C_Activity_ID"));
            if(rs.getInt("P_UOM_ID") != 0)
            	paymentGain.setP_UOM_ID( rs.getInt("P_UOM_ID") );
            if(rs.getInt("P_Job_Title_ID") != 0)
            	paymentGain.setP_Job_Title_ID(rs.getInt("P_Job_Title_ID"));
            if(rs.getInt("P_Salary_Scale_ID") != 0)
            	paymentGain.setP_Salary_Scale_ID(rs.getInt("P_Salary_Scale_ID"));
            if(rs.getInt("P_Salary_Scale_Detail_ID") != 0)
            	paymentGain.setP_Salary_Scale_Detail_ID(rs.getInt("P_Salary_Scale_Detail_ID"));
            if(rs.getInt("P_Collective_Labour_Agr_ID") != 0)
            	paymentGain.setP_Collective_Labour_Agr_ID(rs.getInt("P_Collective_Labour_Agr_ID"));
            if(rs.getInt("P_Gain_Parameter_ID") != 0)
            	paymentGain.setP_Gain_Parameter_ID(rs.getInt("P_Gain_Parameter_ID"));
            if(rs.getInt("WeekIndex") != 0)
            	paymentGain.setWeekIndex(rs.getInt("WeekIndex"));
            paymentGain.setSalaryPercentage(rs.getBigDecimal("SalaryPercentage"));
            if(rs.getInt("P_Employee_LongTermLeave_ID") != 0)
            	paymentGain.setP_Employee_LongTermLeave_ID(rs.getInt("P_Employee_LongTermLeave_ID"));
            paymentGain.setOriginTime(rs.getString("OriginTime"));
            paymentGain.setStartDate(rs.getTimestamp("StartDate"));
            paymentGain.setEndDate(rs.getTimestamp("EndDate"));
            if(!paymentGain.save())
            {
                rs.close();
                stmt.close();
                throw new SQLException("sauvegarde du P_Payment_Gain échouée");
            }
            else
            {
            	this.invertPaymentGainDistribution(paymentId, paymentGain.getP_Payment_Gain_ID(), rs.getInt("P_Payment_Gain_ID"));
            }
            

        }
        
        rs.close();
        stmt.close();
    }
    
    /**
     * Cette méthode inverse les mouvements de banques
     */
    private void invertCreditsMovement(int paymentId) throws SQLException
    {
        // On récupère les enregistrements de la table P_Credits_Movement. Il est
        // à notter que le montant est directement inversé dans la requête
        String sql
        = "select P_CREDITS_ID, PMOVEMENTTYPE, PMOVEMENTDATE, PMOVEMENTORIGIN, "
            + " PMOVEMENTVARIATION * -1 as PMOVEMENTVARIATION, P_EMPLOYEE_ID, PMovementHour, ISATTRIBUTION "
            + " from P_Credits_Movement"
            + " where P_Payment_ID = " + this.timeSheet.getP_Payment_ID();
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();

        while(rs.next())
        {
            P_Credits_Movement creditsMovement = new P_Credits_Movement(Env.getCtx(), -1, null);
            creditsMovement.setP_Payment_ID(paymentId);
            creditsMovement.setP_Credits_ID(rs.getInt("P_CREDITS_ID"));
            creditsMovement.setPMovementType(rs.getString("PMOVEMENTTYPE"));
            creditsMovement.setPMovementDate(rs.getTimestamp("PMOVEMENTDATE"));
            creditsMovement.setPMovementOrigin(rs.getString("PMOVEMENTORIGIN"));
            creditsMovement.setPMovementVariation(rs.getBigDecimal("PMOVEMENTVARIATION"));
            creditsMovement.setP_Employee_ID(rs.getInt("P_EMPLOYEE_ID"));
            //creditsMovement.setPMovementHour(rs.getBigDecimal("PMovementHour"));
            //creditsMovement.setIsAttribution(rs.getBoolean("ISATTRIBUTION"));
            
            if(!creditsMovement.save())
            {
                rs.close();
                stmt.close();
                throw new SQLException("sauvegarde du P_Credits_Movement échouée");
            }
        }
        
        rs.close();
        stmt.close();
    }
    
    /**
     * Cette méthode crée des enregistrements dans P_Payment_EntryLine pour inverser les
     * paiements du constructeur vers le paiement dont l'id est passé en paramètre
     */
    private void invertEntryLine(int paymentId) throws SQLException
    {
        // On récupère les enregistrements de la table P_Payment_Gain. Il est
        // à notter que le montant est directement inversé dans la requête
        String sql
        = "select * from P_Payment_EntryLine "
            + " where P_Payment_ID = " + this.timeSheet.getP_Payment_ID();
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée maintenant le nouvel enregistrement
            P_Payment_Entryline paymentEntryLine = new P_Payment_Entryline(Env.getCtx(), -1, this.timeSheet.get_TrxName());

            paymentEntryLine.setP_Payment_ID(paymentId);
            paymentEntryLine.setLine(rs.getInt("Line"));
            paymentEntryLine.setDescription(rs.getString("Description"));
            // inverse Debit / Credit
            if ( rs.getBigDecimal("AmtAcctCr") != null )
            	paymentEntryLine.setAmtAcctDr(rs.getBigDecimal("AmtAcctCr"));
            if ( rs.getBigDecimal("AmtAcctDr") != null )
            	paymentEntryLine.setAmtAcctCr(rs.getBigDecimal("AmtAcctDr"));
            if(rs.getInt("C_ValidCombination_ID") != 0)
            	paymentEntryLine.setC_ValidCombination_ID(rs.getInt("C_ValidCombination_ID"));
            if(rs.getInt("C_Activity_ID") != 0)
            	paymentEntryLine.setC_Activity_ID(rs.getInt("C_Activity_ID"));
            if(rs.getInt("C_ElementValue_ID") != 0)
            	paymentEntryLine.setC_ElementValue_ID(rs.getInt("C_ElementValue_ID"));
            
            if(rs.getInt("Org_ID") != 0)
            	paymentEntryLine.setOrg_ID(rs.getInt("Org_ID"));
            if(rs.getInt("C_SalesRegion_ID") != 0)
            	paymentEntryLine.setC_SalesRegion_ID(rs.getInt("C_SalesRegion_ID"));
            if(rs.getInt("P_ExpenseCorrection_ID") != 0)
                paymentEntryLine.setP_ExpenseCorrection_ID(rs.getInt("P_ExpenseCorrection_ID"));
            if(rs.getInt("M_Product_ID") != 0)
            	paymentEntryLine.setM_Product_ID(rs.getInt("M_Product_ID"));
            if(rs.getInt("C_BPartner_ID") != 0)
            	paymentEntryLine.setC_BPartner_ID(rs.getInt("C_BPartner_ID"));
            if(rs.getInt("C_Project_ID") != 0)
            	paymentEntryLine.setC_Project_ID(rs.getInt("C_Project_ID"));
            if(rs.getInt("C_Campaign_ID") != 0)
            	paymentEntryLine.setC_Campaign_ID(rs.getInt("C_Campaign_ID"));
           
            
            if(!paymentEntryLine.save())
            {
                rs.close();
                stmt.close();
                throw new SQLException("sauvegarde du P_Payment_Entryline échouée");
            }
        }
        
        rs.close();
        stmt.close();
    }
    
    /**
     * Cette méthode crée des enregistrements dans P_Payment_Distribution pour inverser les
     * paiements du constructeur vers le paiement dont l'id est passé en paramètre
     */
    private void invertPaymentDistribution(int paymentId) throws SQLException
    {
        // On récupère les enregistrements de la table P_Payment_Gain. Il est
        // à notter que le montant est directement inversé dans la requête
        String sql
        = "select P_Financial_Institution_ID, Transit, Folio, Amount * -1 as Amount "
            + " from P_Payment_Distribution "
            + " where P_Payment_ID = " + this.timeSheet.getP_Payment_ID();
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée maintenant le nouvel enregistrement
            P_Payment_Distribution paymentDistribution = new P_Payment_Distribution(Env.getCtx(), -1, this.timeSheet.get_TrxName());

            paymentDistribution.setP_Payment_ID(paymentId);
            if(rs.getInt("P_Financial_Institution_ID") != 0)
            	paymentDistribution.setP_Financial_Institution_ID(rs.getInt("P_Financial_Institution_ID"));
            paymentDistribution.setTransit(rs.getString("Transit"));
            paymentDistribution.setFolio(rs.getString("Folio"));
            paymentDistribution.setAmount(rs.getBigDecimal("Amount"));
            
            if(!paymentDistribution.save())
            {
                rs.close();
                stmt.close();
                throw new SQLException("sauvegarde du P_Payment_Distribution échouée");
            }
        }
        
        rs.close();
        stmt.close();
    }
    
    /**
     * Cette méthode crée des enregistrements dans P_Payment_Distribution pour inverser les
     * paiements du constructeur vers le paiement dont l'id est passé en paramètre
     */
    private void invertPaymentGainDistribution(int newPaymentId, int newPaymentGainId, int oldPaymentGain) throws SQLException
    {
        // On récupère les enregistrements de la table P_Payment_Gain. Il est
        // à notter que le montant est directement inversé dans la requête
        String sql
        = "select Line, Account_ID, Org_ID, M_Product_ID, C_BPartner_ID, C_Project_ID, C_Campaign_ID, C_Activity_ID, "
        	+ " C_SalesRegion_ID, Quantity * -1 as Quantity, Amount * -1 as Amount, Distribution_Type, P_ExpenseCorrection_ID "
            + " from P_Payment_Gain_Distribution "
            + " where P_Payment_Gain_ID = " + oldPaymentGain;
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next())
        {
            // On crée maintenant le nouvel enregistrement
            P_Payment_Gain_Distribution paymentGainDistribution = new P_Payment_Gain_Distribution(Env.getCtx(), -1, this.timeSheet.get_TrxName());

            paymentGainDistribution.setP_Payment_Gain_ID(newPaymentGainId);
            paymentGainDistribution.setLine(rs.getBigDecimal("Line"));
            if(rs.getInt("Account_ID") != 0)
            	paymentGainDistribution.setAccount_ID(rs.getInt("Account_ID"));
            if(rs.getInt("Org_ID") != 0)
            	paymentGainDistribution.setOrg_ID(rs.getInt("Org_ID"));
            if(rs.getInt("M_Product_ID") != 0)
            	paymentGainDistribution.setM_Product_ID(rs.getInt("M_Product_ID"));
            if(rs.getInt("C_BPartner_ID") != 0)
            	paymentGainDistribution.setC_BPartner_ID(rs.getInt("C_BPartner_ID"));
            if(rs.getInt("C_Project_ID") != 0)
            	paymentGainDistribution.setC_Project_ID(rs.getInt("C_Project_ID"));
            if(rs.getInt("C_Campaign_ID") != 0)
            	paymentGainDistribution.setC_Campaign_ID(rs.getInt("C_Campaign_ID"));
            if(rs.getInt("C_Activity_ID") != 0)
            	paymentGainDistribution.setC_Activity_ID(rs.getInt("C_Activity_ID"));
            if(rs.getInt("C_SalesRegion_ID") != 0)
            	paymentGainDistribution.setC_SalesRegion_ID(rs.getInt("C_SalesRegion_ID"));
            paymentGainDistribution.setQuantity(rs.getBigDecimal("Quantity"));
            paymentGainDistribution.setAmount(rs.getBigDecimal("Amount"));
            paymentGainDistribution.setDistribution_Type(rs.getString("Distribution_Type"));
            if(rs.getInt("P_ExpenseCorrection_ID") != 0)
            	paymentGainDistribution.setP_ExpenseCorrection_ID(rs.getInt("P_ExpenseCorrection_ID"));
            
            if(!paymentGainDistribution.save())
            {
                rs.close();
                stmt.close();
                throw new SQLException("sauvegarde du P_Payment_Distribution échouée");
            }
        }
        
        rs.close();
        stmt.close();
    }
    
    /**
     * Cette méthode génère la clé de la feuille de temps
     */
    private String generateTimeSheetValue()
    {
        String value = DB.getDocumentNo (this.timeSheet.getAD_Client_ID() , "P_Time_Sheet", this.timeSheet.get_TrxName() );
        return period.getName() + "-" + value.substring( 3, 7 ) ;
    }
    
}
