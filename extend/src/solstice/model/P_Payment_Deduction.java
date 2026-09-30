package solstice.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;

/**
 *  Payment_Deduction Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class P_Payment_Deduction extends X_P_Payment_Deduction
{
	/**
	 * 	Get Payment_Deduction
	 *	@param ctx context
	 * 	@param P_Payment_Deduction_ID id
	 *	@return Payment_Deduction
	 */
	public static P_Payment_Deduction get (Properties ctx, int P_Payment_Deduction_ID, String trxName)
	{
		Integer key = new Integer (P_Payment_Deduction_ID);
		P_Payment_Deduction Payment_Deduction = (P_Payment_Deduction)s_cache.get(key);
		if (Payment_Deduction != null)
			return Payment_Deduction;
		Payment_Deduction = new P_Payment_Deduction (ctx, P_Payment_Deduction_ID, trxName);
		s_cache.put (key, Payment_Deduction);
		return Payment_Deduction;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Payment_Deduction>	s_cache = new CCache<Integer,P_Payment_Deduction>("P_Payment_Deduction", 20);


	private BigDecimal OldAmountCalc;
	private P_Deduction_Param Deduction_Param;

	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Payment_Deduction_ID id
	 */
	public P_Payment_Deduction (Properties ctx, int P_Payment_Deduction_ID, String trxName)
	{
		super (ctx, P_Payment_Deduction_ID, trxName);
		if (P_Payment_Deduction_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setAD_Org_ID(0);
		}
	}	//	P_Payment_Deduction

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Payment_Deduction (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Payment_Deduction (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Payment_Deduction_ID"), trxName);
	}	//	P_Payment_Deduction

	
	protected boolean beforeDelete ()
	{
		String trxName = get_TrxName();
		//2025-02-28
		if ( this.getOrigine().equals("AJT"))
		{
			P_Employee_Deduction_Excep Excep = P_Employee_Deduction_Excep.get( Env.getCtx(), this.getP_Employee_Deduction_ID(), this.getP_Period_ID() , this.get_TrxName());
			if ( Excep != null )
			{
				Excep.setExceptionAmountEmployee( null );
				Excep.setExceptionAmountEmployer( null );
				Excep.save();
			}
		}	
		
		return true;
	}
	
	protected boolean beforeSave (boolean newRecord)
	{
		OldAmountCalc =  (BigDecimal)this.get_ValueOld( "Employee_Part") ;
		if ( this.getP_Year_ID() == 0 )
		{
			P_Period Period = P_Period.get( Env.getCtx(), this.getP_Period_ID(), this.get_TrxName());
			this.setP_Year_ID( Period.getP_Year_ID());
		}
		
		//2025-02-28
		if ( this.getOrigine().equals("AJT"))
		{
			P_Employee_Deduction_Excep Excep = P_Employee_Deduction_Excep.get( Env.getCtx(), this.getP_Employee_Deduction_ID(), this.getP_Period_ID() , this.get_TrxName());
			if ( Excep == null )
			{
				Excep = new P_Employee_Deduction_Excep( Env.getCtx(), -1, this.get_TrxName());
			}
			Excep.setP_Deduction_ID( this.getP_Deduction_ID());
			Excep.setP_Employee_ID( this.getP_Employee_ID());
			//2025-08-19
			if ( this.getP_Employee_Deduction_ID() == 0 )
			{
				P_Period Period = P_Period.get( Env.getCtx(), this.getP_Period_ID(), this.get_TrxName());
				P_Employee_Deduction Employee_Deduction = P_Employee_Deduction.get(Env.getCtx(), this.getP_Employee_ID(),  this.getP_Deduction_ID(), Period.getEndDate(), this.get_TrxName() );
				this.setP_Employee_Deduction_ID( Employee_Deduction.getP_Employee_Deduction_ID() );
			}
			Excep.setP_Employee_Deduction_ID( this.getP_Employee_Deduction_ID());
			Excep.setExceptionAmountEmployee( this.getEmployee_Part());
			Excep.setExceptionAmountEmployer( this.getEmployer_Part());
			Excep.setP_Period_ID(this.getP_Period_ID());
			Excep.save();		
			
				
		}
		
		return true;
	}


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
        if (this.getEmployee_Part().compareTo( Env.ZERO) != 0  )
        {
        	Deduction_Param = P_Deduction_Param.get( Env.getCtx(), this.getP_Deduction_Param_ID(), this.get_TrxName());
            if ( Deduction_Param.getP_Advance_ID() != 0)
            {
            	Advance_Recovery();
            }
        }


        return true;
    } //	afterSave


    private void Advance_Recovery()
    {
		P_Payment Payment = P_Payment.get( Env.getCtx(), this.getP_Payment_ID(), this.get_TrxName());
//		P_Time_Sheet TimeSheet = P_Time_Sheet.GetWithPaymentID(Env.getCtx(), this.getP_Payment_ID(), this.get_TrxName() );
		P_Period Period = P_Period.get( Env.getCtx(), this.getP_Period_ID(), this.get_TrxName());
    	
         P_Employee_Advance EmployeeAdvance = P_Employee_Advance.get( Env.getCtx(), Payment.getP_Employee_ID(), Deduction_Param.getP_Advance_ID(), this.get_TrxName() );
         if ( EmployeeAdvance.getP_Employee_Advance_ID() != 0 )
         {
             if ( this.getEmployee_Part().compareTo(Env.ZERO) != 0)
             {
                 P_Employee_Advance_Summary EmployeeAdvanceSummary = P_Employee_Advance_Summary.get( Env.getCtx(), Payment.getP_Employee_ID(), Deduction_Param.getP_Advance_ID(), 0, this.get_TrxName() );

                 // Remboursement uniquement si l'employé doit encore de l'argent.
                 P_Employee_Advance_Detail EmployeeAdvanceDetail = new P_Employee_Advance_Detail( Env.getCtx(), -1, this.get_TrxName() );
                 EmployeeAdvanceDetail.setP_Employee_Advance_ID(EmployeeAdvance.getP_Employee_Advance_ID());
                 EmployeeAdvanceDetail.setP_Payment_ID( Payment.getP_Payment_ID());

                 if ( OldAmountCalc == null ) OldAmountCalc = Env.ZERO;
                 
                 EmployeeAdvanceDetail.setAdvanceAmount(Env.ZERO.subtract( this.getEmployee_Part().subtract( OldAmountCalc ) ) );
                 EmployeeAdvanceDetail.setAdvanceDate( Period.getEndDate());
                 EmployeeAdvanceDetail.setIsRefund( true);
                 EmployeeAdvanceDetail.setP_Advance_ID( Deduction_Param.getP_Advance_ID());
                 EmployeeAdvanceDetail.setP_Employee_ID( Payment.getP_Employee_ID());
                 EmployeeAdvanceDetail.setP_Year_ID( Payment.getP_Year_ID());
                 EmployeeAdvanceDetail.setP_Period_ID( Payment.getP_Period_ID());
                 EmployeeAdvanceDetail.setP_Employee_Advance_Summary_ID( EmployeeAdvanceSummary.getP_Employee_Advance_Summary_ID() );
                 // EmployeeAdvanceDetail.setP_SpecialRate_ID() set to null
                 if ( EmployeeAdvanceDetail.getAdvanceAmount().compareTo(Env.ZERO) != 0 )
                	 EmployeeAdvanceDetail.save();
             }
         }
    }

    

}	//	P_Payment_Deduction
