/*
 * Created on 2005-10-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Employee_Carra extends X_P_Employee_Carra
{
	/**
	 * 	Get Credits
	 *	@param ctx context
	 * 	@param P_Credits_ID id
	 *	@return Credits
	 */
	/**	Logger							*/
	
	private static CLogger		s_log = CLogger.getCLogger (P_Employee_Carra.class);

	public static P_Employee_Carra get (Properties ctx, int iP_Employee_Carra_ID, String trxName)
	{
		Integer key = new Integer (iP_Employee_Carra_ID);
		P_Employee_Carra employee_Carra = (P_Employee_Carra)s_cache.get(key);
		if (employee_Carra != null)
			return employee_Carra;
		employee_Carra = new P_Employee_Carra (ctx, iP_Employee_Carra_ID, trxName);
		s_cache.put (key, employee_Carra);
		return employee_Carra;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Employee_Carra>	s_cache = new CCache<Integer,P_Employee_Carra>("P_Employee_Carra", 20);

    /**
     * @param ctx
     * @param P_Employee_Carra_ID
     * @param trxName
     */
    public P_Employee_Carra(Properties ctx, int P_Employee_Carra_ID,
            String trxName)
    {
        super(ctx, P_Employee_Carra_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Employee_Carra(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

	public static P_Employee_Carra get (Properties ctx, int iP_Employee_ID, int iP_Deduction_ID, String recordType, int iP_Year_ID, String trxName )
	{
		int iP_Employee_Carra_ID = 0;

		String sql = null;
		sql = "Select P_Employee_Carra_ID From P_Employee_Carra ";
		sql +="  Where P_Employee_ID = " + iP_Employee_ID;
		sql +="    and P_Deduction_ID = " + iP_Deduction_ID; 
		sql +="    and RecordType = '" + recordType +"'";
		sql +="    and P_Year_ID = " + iP_Year_ID;
		
			
		PreparedStatement pstmt = null;
		try
		{
				pstmt = DB.prepareStatement (sql, null);
				ResultSet rs = pstmt.executeQuery ();
				if ( rs.next () )
					iP_Employee_Carra_ID = rs.getInt(1);
				else
					iP_Employee_Carra_ID = 0;
				
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
		}
		catch (Exception e)
		{
			System.out.println ("* Error * P_Employee_Carra - get - " + sql + " - " + e);
		}

		if ( iP_Employee_Carra_ID == 0)
		{
			return null;
		}
		
		return P_Employee_Carra.get( ctx, iP_Employee_Carra_ID , trxName);
	}

	/**
	 * 	Executed before Delete operation.
	 *	@return true if record can be deleted
	 */

	protected boolean beforeDelete ()
	{
		if ( this.isDone() )
		{
			s_log.saveError("ValidationError","Enregistrement non modifiable - Status Final ");
//			s_log.log(Level.SEVERE,"ValidationError", "Enregistrement non modifiable - Status Final");
			return false;
		}
		return true;
	} 	//	beforeDelete

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 */

	protected boolean beforeSave (boolean newRecord)
	{
		
/*		if ( this.isDone() )
		{
			s_log.saveError("ValidationError","Enregistrement non modifiable - Status Final ");
//			s_log.log(Level.SEVERE,"ValidationError", "Enregistrement non modifiable - Status Final");
			return false;
		}
*/
/*
		try
		{
			calcul_fe( this.getP_Year_ID(), this.getP_Deduction_ID() , 1000007);
		}
		catch (Exception e)
		{
			System.out.println ("* Error * P_Employee_Carra - Calcul FE "+ " - " + e);
			return false;
		}
*/
		
        this.deductibleSalary = this.contributorySalary.add(this.exoneratedSalary.add(this.motherhoodSalary));

		return true;

	}
	
	
	
	/**
	 * 	Called after Save for Post-Save Operation
	 * 	@param newRecord new record
	 *	@param success true if save operation was success
	 *	@return if save was a success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{


		return success;
	}	//	afterSave


	private BigDecimal computedService;
    private BigDecimal creditedService;
    private BigDecimal contributorySalary;
    private BigDecimal exoneratedSalary;
    private BigDecimal motherhoodSalary;
    private BigDecimal deductibleSalary;
    private BigDecimal annualDeductibleSalaray;
    private BigDecimal theoriticalRetro;
    private BigDecimal adjustedDeductibleSalaray;
    private BigDecimal exemption;
    private BigDecimal adjustmentFactorStandardAmount;
    private BigDecimal calculPercentage;
    private BigDecimal adjustmentFactor;
    private BigDecimal manualRetro;
    private BigDecimal adjustementFactorAmount;
//    private BigDecimal leaveOfAbsenceWithoutPay;
    private BigDecimal adjustedLeaveOfAbsenceDays;
//    private BigDecimal workTimePercentageDiff;
//    private BigDecimal workTimePercentage;
//    private BigDecimal workTimePercentageWithoutDiff;
    private final BigDecimal bdHundred = new BigDecimal(100);

    // a initialier
    private BigDecimal YMPE;
    private BigDecimal RRSP;
    private BigDecimal annualBase;
    private BigDecimal maximumAmountETC;
    private BigDecimal exemptionPercentage;
    private BigDecimal maximumAmountFE;

    private BigDecimal joursDAbsence = Env.ZERO;
    private BigDecimal BaseSalary = Env.ZERO;
    private BigDecimal contributoryDays = Env.ZERO;
    private BigDecimal disabilityDays;
    private BigDecimal motherhoodDays;
    private int nbrPeriod;
    private BigDecimal joursTRD;
    private String strStatus = "";

	public void calcul_fe(  ) throws SQLException
	{
		int yearID = this.getP_Year_ID();
		int deductionID = this.getP_Deduction_ID();
		int FrequencyID = getP_Frequency_ID(); 
			
        String sql
        = "select Annual_Base, MaximumAmountETC, MaximumAmountFE, CalculPercentage,"
            + " RRSP, ExemptionPercentage, YMPE, CARRA"
            + " from P_Equivalence_Factor_Param"
            + " where P_Deduction_ID = " + deductionID
            + " and P_Year_ID = " + yearID
            + " and IsActive = 'Y'";
        
        PreparedStatement stmt = DB.prepareStatement(sql, null);
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
        }
        
        rs.close();
        stmt.close();
		
        joursDAbsence = this.getLeaveOfAbsenceWithoutPay();
        BaseSalary = this.getBaseSalary();
        contributoryDays = this.getContributoryDays();
        contributorySalary = this.getContributorySalary();
        disabilityDays = this.getDisabilityDays();
        motherhoodSalary = this.getMotherHoodSalary();
        motherhoodDays = this.getMotherHoodDays();
        if ( motherhoodDays == null)
        	motherhoodDays = Env.ZERO;

        exoneratedSalary = this.getExoneratedSalary();

        if ( exoneratedSalary == null)
        	exoneratedSalary = Env.ZERO;
        
        manualRetro = this.getManualRetro();

        theoriticalRetro = Env.ZERO;
        adjustedDeductibleSalaray = Env.ZERO;
        
        P_Frequency Frequency = P_Frequency.get( Env.getCtx(), FrequencyID, this.get_TrxName());
        
        nbrPeriod = 26 ; //Frequency.getNumberOfPeriod();
        joursTRD = this.getSelfFinancedLeaveDays();
        
        if ( this.getStatus() != null)
        	strStatus = this.getStatus();

		
	    P_Employee employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
        // On instancie une variable Nombre jour = Base Annuel du dossier
        // carra * nombre de période (passé en paramètre) / 26
        BigDecimal nbrJours = this.annualBase.multiply(new BigDecimal((double)this.nbrPeriod)).divide(new BigDecimal(26.0d), 4, BigDecimal.ROUND_HALF_UP);

	    
//	    Calcul FE Début
        BigDecimal tempValue = this.annualBase.multiply(new BigDecimal(nbrPeriod).divide(new BigDecimal(26), 4, BigDecimal.ROUND_HALF_UP));

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
        
//        this.leaveOfAbsenceWithoutPay = joursDAbsence.subtract(this.adjustedLeaveOfAbsenceDays);
        
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
        	+ " AND P_Payment.P_Year_Id = " + yearID
        	+ " AND pg.P_Gain_ID = ("
        	+ " 	SELECT gain.P_Gain_ID "
        	+ " 	FROM P_Gain gain, P_GainInfo info, P_Gain_GainInfo ggi "
        	+ "  	WHERE gain.P_Gain_ID = ggi.P_Gain_ID "
        	+ "   	AND info.P_GainInfo_ID = ggi.P_GainInfo_ID "
        	+ "		AND info.Value = 'PCTTTOCC' "
        	+ " 	AND TO_Consider = 'Y' "
        	+ " )";
        
        PreparedStatement pstmPCTTTOCC = DB.prepareStatement(strPCTTTOCC, null);
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
        
        
//        P_Job_Type empJobType = P_Job_Type.get(Env.getCtx(), employee.getP_Job_Type_ID(), null);
        
//        this.workTimePercentageDiff = this.getWorkedTimePercentage();
  
//        workTimePercentage = this.getWorkedTimePercentage();
        joursDAbsence = this.getLeaveOfAbsenceWithoutPay();
        
        boolean flgOK = false;
        if(		this.contributorySalary.compareTo(Env.ZERO) != 0 ||
        		this.contributoryDays.compareTo(Env.ZERO) != 0 ||
        		this.exoneratedSalary.compareTo(Env.ZERO) != 0 ||
        		this.disabilityDays.compareTo(Env.ZERO) != 0 ||
        		this.motherhoodSalary.compareTo(Env.ZERO) != 0 ||
        		this.motherhoodDays.compareTo(Env.ZERO) != 0 ||
        		joursDAbsence.compareTo(Env.ZERO) != 0 ||
        		this.joursTRD.compareTo(Env.ZERO) != 0 ||
        		this.getNonContributorySalary().compareTo(Env.ZERO) != 0)
        {
        	flgOK = true;
        	P_Year year = P_Year.get(Env.getCtx(), yearID, null);
        	
        	//on termine en verifiant que l'employe n'a pas pris sa retraite l'an passe
        	Calendar firstDayOfYear = Calendar.getInstance();
        	Calendar lastDayOfYear= Calendar.getInstance();
        	firstDayOfYear.set(Calendar.YEAR, new Integer(year.getYear()).intValue() - 1);
        	firstDayOfYear.set(Calendar.MONTH, 0);
        	firstDayOfYear.set(Calendar.DAY_OF_MONTH, 1);
        	
        	lastDayOfYear.set(Calendar.YEAR, new Integer(year.getYear()).intValue() - 1);
        	lastDayOfYear.set(Calendar.MONTH, 11);
        	lastDayOfYear.set(Calendar.DAY_OF_MONTH, 31);
        	
/*        	if(employeeLeaveBetweenDates(employee.getP_Employee_ID(), new Timestamp(firstDayOfYear.getTimeInMillis()), new Timestamp(lastDayOfYear.getTimeInMillis())))
        	{
        		//date debut = 0??
        		//date fin = 9999??
        		this.computedService = Env.ZERO;
        		this.BaseSalary = Env.ZERO;
        	}
*/        	
        }
        else
        {
        	flgOK = false;
        }
        
        int cmptInsertion = 0;
        int cmptUpdate = 0;
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

        
    	this.setBaseSalary(this.BaseSalary);
    	this.setContributorySalary(this.contributorySalary);
    	this.setExoneratedSalary(this.exoneratedSalary);
    	this.setMotherHoodSalary(this.motherhoodSalary);
//    	this.setEmployee_Part(calculValeur.getEmployeePart());
    	this.setContributoryDays(this.contributoryDays);
    	this.setDisabilityDays(this.disabilityDays);
    	this.setMotherHoodDays(this.motherhoodDays);
    	this.setLeaveOfAbsenceWithoutPay(joursDAbsence);
    	this.setSelfFinancedLeaveDays(this.joursTRD);
    	this.setDeductibleSalary(this.deductibleSalary);
    	this.setTheoricalRetro(this.theoriticalRetro);
    	this.setManualRetro(this.manualRetro);
    	this.setAdjustmentFactor(this.adjustmentFactor);
    	this.setComputedService(this.computedService);
    	this.setCreditedService(this.creditedService);
//    	this.setRecordType(this.recordType);
//    	this.setP_Period_ID(period_ID);
    	this.setAdjustedLeaveOfAbsenceDays(Env.ZERO);
    	this.setAdjustmentFactorSTDAmount(this.adjustmentFactorStandardAmount);
//    	this.setNonContributorySalary(calculValeur.getNonContributorySalary());
    	this.setAdjustmentFactorAmount(this.adjustementFactorAmount);

        
//Calcul FE Fin                
	    
	}

	private int getP_Frequency_ID()
	{
		int id = 0;
		String sql = "Select P_Frequency_ID from P_Frequency";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				id = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.err.println("getP_Frequency_ID - " + e);
		}
		
		return id;
		
	}

	
}
