package solstice.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Payment_Gain Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Payment_Gain extends X_P_Payment_Gain
{
	/**
	 * 	Get Payment_Gain
	 *	@param ctx context
	 * 	@param P_Payment_Gain_ID id
	 *	@return Payment_Gain
	 */
	public static P_Payment_Gain get (Properties ctx, int P_Payment_Gain_ID, String trxName)
	{
		Integer key = new Integer (P_Payment_Gain_ID);
		P_Payment_Gain Payment_Gain = (P_Payment_Gain)s_cache.get(key);
		if (Payment_Gain != null)
			return Payment_Gain;
		Payment_Gain = new P_Payment_Gain (ctx, P_Payment_Gain_ID, trxName);
		s_cache.put (key, Payment_Gain);
		return Payment_Gain;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,P_Payment_Gain>	s_cache = new CCache<Integer,P_Payment_Gain>("P_Payment_Gain", 20);

	private static P_Gain Gain;
	private BigDecimal OldAmountCalc;
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Payment_Gain_ID id
	 */
	public P_Payment_Gain (Properties ctx, int P_Payment_Gain_ID, String trxName)
	{
		super (ctx, P_Payment_Gain_ID, trxName);
		if (P_Payment_Gain_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
		OldAmountCalc = this.getAmountCalc();
	}	//	P_Payment_Gain

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Payment_Gain (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);

		OldAmountCalc = this.getAmountCalc();
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Payment_Gain (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Payment_Gain_ID"), trxName);
		OldAmountCalc = this.getAmountCalc();
	}	//	P_Payment_Gain

    
	/**
     * After Save
     * 
     * @param newRecord
     *            new
     * @param success
     *            success
     */
    protected boolean afterSave(boolean newRecord, boolean success)
    {

        if (!success)
        {
            return success;
        }
        
        //
        // Si le gain est de type avance, alors l'on créer un record dans le sommaire des avances.
        //
        if (this.getAmountCalc().compareTo( Env.ZERO) != 0  )
        {
        	Gain = P_Gain.get( Env.getCtx(), this.getP_Gain_ID(), this.get_TrxName());
            if ( Gain.getP_Advance_ID() != 0)
            {
            	Create_Advance_Info();
            }
        }


        return true;
    } //	afterSave


	private void Create_Advance_Info( )
	{
		P_Payment Payment = P_Payment.get( Env.getCtx(), this.getP_Payment_ID(), this.get_TrxName());
		P_Time_Sheet TimeSheet = P_Time_Sheet.GetWithPaymentID(Env.getCtx(), this.getP_Payment_ID(), this.get_TrxName() );
		P_Period Period = P_Period.get( Env.getCtx(), this.getP_Period_ID(), this.get_TrxName());
		//
		// Maj ou Création d'un historique de l'avance
		//
		P_Employee_Advance EmployeeAdvance = P_Employee_Advance.get( Env.getCtx(), Payment.getP_Employee_ID(), Gain.getP_Advance_ID(), this.get_TrxName() );
		EmployeeAdvance.setP_Employee_ID( Payment.getP_Employee_ID());
		EmployeeAdvance.setP_Advance_ID( Gain.getP_Advance_ID());
		EmployeeAdvance.setIsActive(true);
		EmployeeAdvance.save();

		P_Employee_Advance_Summary EmployeeAdvance_Summary = P_Employee_Advance_Summary.get( Env.getCtx(), Payment.getP_Employee_ID(), Gain.getP_Advance_ID(), Gain.getP_SpecialRate_ID(), this.get_TrxName() );
		EmployeeAdvance_Summary.setP_Employee_ID( Payment.getP_Employee_ID());
		EmployeeAdvance_Summary.setP_Advance_ID( Gain.getP_Advance_ID());
		EmployeeAdvance_Summary.setP_Employee_Advance_ID( EmployeeAdvance.getP_Employee_Advance_ID());
        if ( Gain.getP_SpecialRate_ID() != 0 )
        	EmployeeAdvance_Summary.setP_SpecialRate_ID( Gain.getP_SpecialRate_ID() );
		EmployeeAdvance_Summary.setIsActive(true);
		EmployeeAdvance_Summary.save();

		//
		// Maj ou Création d'un detail de l'avance
		//
		P_Employee_Advance_Detail EmployeeAdvanceDetail = new P_Employee_Advance_Detail( Env.getCtx(), -1, this.get_TrxName() );
		EmployeeAdvanceDetail.setP_Employee_Advance_ID(EmployeeAdvance.getP_Employee_Advance_ID());
		EmployeeAdvanceDetail.setP_Payment_ID( this.getP_Payment_ID());
		EmployeeAdvanceDetail.setAdvanceAmount(this.getAmountCalc().subtract( OldAmountCalc ));
//		EmployeeAdvanceDetail.setAdvanceAmount(this.getAmountCalc());
		EmployeeAdvanceDetail.setAdvanceDate( this.getDay());
		EmployeeAdvanceDetail.setIsRefund( false );
		EmployeeAdvanceDetail.setP_Advance_ID( Gain.getP_Advance_ID());
        EmployeeAdvanceDetail.setP_Employee_ID( Payment.getP_Employee_ID());
        EmployeeAdvanceDetail.setP_Year_ID( Payment.getP_Year_ID());
        EmployeeAdvanceDetail.setP_Period_ID( Payment.getP_Period_ID());
        EmployeeAdvanceDetail.setP_Employee_Advance_Summary_ID( EmployeeAdvance_Summary.getP_Employee_Advance_Summary_ID() );
        if ( Gain.getP_SpecialRate_ID() != 0 )
        	EmployeeAdvanceDetail.setP_SpecialRate_ID( Gain.getP_SpecialRate_ID());


		EmployeeAdvanceDetail.save();
		
		P_Deduction Deduction = P_Deduction.getDeductionRecovery( Env.getCtx(), Gain.getP_Advance_ID(), this.get_TrxName());
		if ( Deduction != null )
		{
			P_Employee_Deduction EmployeeDeduction = P_Employee_Deduction.get( Env.getCtx(),Payment.getP_Employee_ID(), Deduction.getP_Deduction_ID(), Period.getStartDate(), this.get_TrxName() );
			if ( EmployeeDeduction.getP_Deduction_ID() == 0)
			{
				EmployeeDeduction.setP_Deduction_ID( Deduction.getP_Deduction_ID());
				EmployeeDeduction.setP_Employee_ID( Payment.getP_Employee_ID());
				EmployeeDeduction.setEffectIn( Period.getStartDate());
				EmployeeDeduction.setIsActive(true);
				EmployeeDeduction.save();
				if ( TimeSheet != null )
					P_Time_Sheet_Error.NewMessage( TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "Une déduction a été créer dans le dossier de l'employé pour récupérer l'avance du gain " + Gain.getValue() + ' ' + Gain.getName() , this.get_TrxName());
			}
		}
		else
		{
			if ( TimeSheet != null )
				P_Time_Sheet_Error.NewMessage( TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "Il n'y a pas de déduction de défini pour récupérer l'avance du gain " + Gain.getValue() + ' ' + Gain.getName() , this.get_TrxName());
		}		
	}

}	//	P_Payment_Gain
