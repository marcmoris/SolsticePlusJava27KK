/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/
package solstice.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import solstice.utils.PgiUtil;

/**
 *  Payment_Gain Model
 *
 *  @author Marc Morissette - Progestion Inf
 */
public class MPayment_Gain extends X_P_Payment_Gain
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Payment_Gain
	 *	@param ctx context
	 * 	@param P_Payment_Gain_ID id
	 *	@return Payment_Gain
	 */
	public static MPayment_Gain get (Properties ctx, int P_Payment_Gain_ID, String trxName)
	{
		Integer key = new Integer (P_Payment_Gain_ID);
		MPayment_Gain Payment_Gain = (MPayment_Gain)s_cache.get(key);
		if (Payment_Gain != null)
			return Payment_Gain;
		Payment_Gain = new MPayment_Gain (ctx, P_Payment_Gain_ID, trxName);
		s_cache.put (key, Payment_Gain);
		return Payment_Gain;
	}	//	get


	/**	Cache						*/
	private static CCache<Integer,MPayment_Gain>	s_cache = new CCache<Integer,MPayment_Gain>("MPayment_Gain", 20);

	private P_Gain Gain = null;
	private P_Gain_Parameter GainParameter = null;
	private P_Assignment_Param AssignmentParam = null;
	private BigDecimal OldAmountCalc;
	P_Post Post = null;
			
	public P_Post getPost()
	{
		if ( Post == null )
			Post = P_Post.get( Env.getCtx(), this.getP_Post_ID(), this.get_TrxName());
		return Post;
	}
	
	
	public P_Assignment_Param getAssignmentParam()
	{
		if ( AssignmentParam == null && this.getP_Assignment_Param_ID() != 0)
			AssignmentParam = P_Assignment_Param.get( Env.getCtx(), this.getP_Assignment_Param_ID() , this.get_TrxName() );

		return AssignmentParam;
	};
	
	public P_Gain_Parameter getGainParameter()
	{
		if ( GainParameter == null && this.getP_Gain_Parameter_ID() != 0 )
		{
				GainParameter = P_Gain_Parameter.get( Env.getCtx (), Gain.getP_Gain_ID(), this.getP_Gain_Parameter_ID(), this.get_TrxName() ); 
		}

		return GainParameter;
	}
	


	public P_Gain getGain()
	{
		if ( Gain == null && this.getP_Gain_ID() != 0)
		  Gain = P_Gain.get( Env.getCtx (), this.getP_Gain_ID(), this.get_TrxName() );
		
		return Gain;
		
	}
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Payment_Gain_ID id
	 */
	public MPayment_Gain (Properties ctx, int P_Payment_Gain_ID, String trxName)
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
	public MPayment_Gain (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);

		OldAmountCalc = this.getAmountCalc();
	}	

	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public MPayment_Gain (Properties ctx, String trxName)
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
//2011.11.30 gestion des messages 					
//					P_Time_Sheet_Error.NewMessage( TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "Une déduction a été créer dans le dossier de l'employé pour récupérer l'avance du gain " + Gain.getValue() + ' ' + Gain.getName() , this.get_TrxName());
					P_Time_Sheet_Error.NewMessage( TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol01-I") + Gain.getValue() + ' ' + Gain.getName() , this.get_TrxName());
			}
		}
		else
		{
			if ( TimeSheet != null )
				//2011.11.30 gestion des messages 					
//				P_Time_Sheet_Error.NewMessage( TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, "Il n'y a pas de déduction de défini pour récupérer l'avance du gain " + Gain.getValue() + ' ' + Gain.getName() , this.get_TrxName());
				P_Time_Sheet_Error.NewMessage( TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Information, Msg.getMsg(Env.getCtx(), "Sol02-I") + Gain.getValue() + ' ' + Gain.getName() , this.get_TrxName());
		}		
	}


}	//	P_Payment_Gain
