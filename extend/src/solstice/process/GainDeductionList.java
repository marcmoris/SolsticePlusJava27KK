/*
 * Created on 2005-02-08
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.math.BigDecimal;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import solstice.model.*;

/**
 * @author frabou01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GainDeductionList {
	
    private CLogger			log = CLogger.getCLogger (getClass());

	//Le paiement dont dépend les déductions
	int paymentId = -1;
	
	//Position courante de lecture
	int currentPos = -1;
	
	//Nombre de distribution dans la liste
	int numberOfDist = -1;
	
	boolean firstRead = true;
	
	Vector<BigDecimal> paymentDeductionIdList = null;
	Vector<BigDecimal> paymentDeductionAmountList = null;
	Vector<BigDecimal> DeductionIdList = null;
	Vector<Vector> marginalProfitList = null;
	
	GainDeductionList(){
		paymentDeductionIdList = new Vector<BigDecimal>();
		paymentDeductionAmountList = new Vector<BigDecimal>();
		DeductionIdList = new Vector<BigDecimal>();
		marginalProfitList = new Vector<Vector>();
	}
	
	public void AddDeduction(BigDecimal deductionId, BigDecimal deductionAmnt){
		
		paymentDeductionIdList.add(deductionId);
		paymentDeductionAmountList.add(deductionAmnt.setScale(2,BigDecimal.ROUND_HALF_UP));
	}
	
	public void AddDeduction(BigDecimal PaymentdeductionId, BigDecimal deductionAmnt, BigDecimal DeductionId){
		
		paymentDeductionIdList.add(PaymentdeductionId);
		DeductionIdList.add(DeductionId);
		paymentDeductionAmountList.add(deductionAmnt.setScale(2,BigDecimal.ROUND_HALF_UP));
	}
	public boolean Next(){
		
		
		if(firstRead){
			numberOfDist = paymentDeductionIdList.size();
			firstRead = false;
		}
		
		
		if(numberOfDist > 0){
			currentPos = currentPos + 1;
			numberOfDist = numberOfDist - 1;
			return true;
		}else 
			return false;
			
	}
	
	public BigDecimal GetPaymentDeductionId(){
		return (BigDecimal)paymentDeductionIdList.get(currentPos);
	}
	
//	public BigDecimal GetDeductionAccountId(){
//		return (BigDecimal)DeductionAccountIdList.get(currentPos);
//	}

	public BigDecimal GetDeductionId(){
		return (BigDecimal)DeductionIdList.get(currentPos);
	}

	public BigDecimal GetDeductionAmount(){
		return (BigDecimal)paymentDeductionAmountList.get(currentPos);
	}
	
	public void AddDeductionMP(int Payment_ID, int Payment_Gain_ID, int P_Occupation_Group, int P_Job_Title_ID, int C_Activity_ID, BigDecimal Line, int C_SalesRegion_ID, int Org_ID,BigDecimal distributionId,BigDecimal mpAmount ){
		
		//L'ajout se fait à la déduction courante et nous devons conserver pour
		//chaque bénéfice marginal, l'Id de la distribution qui a permis de calculer
		//le prorata. Cela servira à l'enregistrement pour surtout conserver le numéro de compte.
		if(marginalProfitList.size()-1 < currentPos)
			marginalProfitList.add(new Vector());
		
		
		MarginalProfit mp = new MarginalProfit(Payment_ID, Payment_Gain_ID, P_Occupation_Group, P_Job_Title_ID, C_Activity_ID, Line, C_SalesRegion_ID, Org_ID, distributionId,mpAmount);
		((Vector)marginalProfitList.get(currentPos)).add(mp);
	}
	

	public BigDecimal GetDeductionMpSum(){
		BigDecimal result = new BigDecimal(0);
		Vector mpList = null;
		//Il s'agit de la déduction courante
		mpList = ((Vector)marginalProfitList.get(currentPos));
		for(int i=0 ; i < mpList.size(); i++){
			result = result.add(((MarginalProfit)mpList.get(i)).GetMarginalProfitAmnt());
		}
		
		return result.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	
	public void AddAmountToLastMp(BigDecimal amnt){
		Vector mpList = null;
		//Il s'agit de la déduction courante
		mpList = ((Vector)marginalProfitList.get(currentPos));
		((MarginalProfit)mpList.get(mpList.size() - 1)).AddAmountToMp(amnt);
	}
	
	//Enregistre les BM issus de la déduction courante
	public void SaveMarginalProfits( P_Time_Sheet TimeSheet, P_Payment Payment, P_Employee Employee,  String trxName){
		Vector mpList = null;
		MarginalProfit mp = null;
//		X_P_Payment_Gain_Distribution gainDist = null; 
		X_P_Payment_Gain_Distribution deductionDist = null;
		P_Deduction_Account deductionAccount = null;
		//Il s'agit de la déduction courante
		mpList = ((Vector)marginalProfitList.get(currentPos));
		for(int i = 0; i < mpList.size(); i++){
			mp = (MarginalProfit)mpList.get(i);
			
			//Charger la distribution sur laquelle le bénéfice marginal a été calculé
//			gainDist = new X_P_Payment_Gain_Distribution(Env.getCtx(), mp.GetDistributionId().intValue(), trxName);

//			P_Payment_Gain PaymentGain = P_Payment_Gain.get( Env.getCtx(), mp.getP_Payment_Gain_ID(), trxName );
//			P_Payment Payment = P_Payment.get( Env.getCtx(), mp.getP_Payment_ID(), trxName);

			
			if ( Payment.getAD_Client_ID() == 11 )
				deductionAccount = P_Deduction_Account.get(Env.getCtx(), GetDeductionId().intValue(), mp.getP_Job_Title_ID(), Payment.getP_Job_Type_ID(),  trxName);
			else
			{
//				P_Employee Employee = P_Employee.get( Env.getCtx(), Payment.getP_Employee_ID(), trxName);
//				if ( mp.getC_Activity_ID() != 0)
//					deductionAccount = P_Deduction_Account.get(Env.getCtx(), Payment.getAD_Client_ID(), Payment.getAD_Org_ID(), mp.getC_Activity_ID(), GetDeductionId().intValue(),  trxName);
//				else
//					deductionAccount = P_Deduction_Account.get(Env.getCtx(), Payment.getAD_Client_ID(), Payment.getAD_Org_ID(), Employee.getC_Activity_ID(), GetDeductionId().intValue(),  trxName);
//2010.06.08 prendre unité du payement
//				deductionAccount = P_Deduction_Account.get(Env.getCtx(), PayGetDeductionId().intValue(),  trxName);

				P_Assignment Assignment = P_Assignment.get( Env.getCtx(), Employee.GetAssignmentPrincipal(), null);
				P_Post Post = P_Post.get( Env.getCtx(), Assignment.getP_Post_ID(), null);
				P_Workplace WorkPlace = P_Workplace.get( Env.getCtx(), Post.getP_Workplace_ID(), null);
				int C_SalesRegion_ID = WorkPlace.getC_SalesRegion_ID();
				deductionAccount = P_Deduction_Account.getWithSaleRegion(Env.getCtx(), Payment.getAD_Client_ID(), Payment.getAD_Org_ID(), C_SalesRegion_ID, GetDeductionId().intValue(),  trxName);
				
			}
				
			if ( deductionAccount == null )
			{
	        	P_Deduction deduction = P_Deduction.get(Env.getCtx(), GetDeductionId().intValue(), trxName);
	        	String message = Msg.getMsg(Env.getCtx(), "DeductionAccountRequired") + "( " + deduction.getValue() + " )"; 
			    P_Time_Sheet_Error.NewMessage( TimeSheet, P_Time_Sheet_Error.ALERT_SEVERITY_LEVEL_Error, message, trxName );
				
			}
			else
			{
				/*** Elements communs ***/
				
				//Bénéfice marginal à enregistrer, 
				deductionDist = new P_Payment_Gain_Distribution(Env.getCtx(), -1, trxName);
				
				//Le virement se fait dans le compte de la déduction de la table p_deduction_account
				deductionDist.setAccount_ID(deductionAccount.getP_Deduction_Acct()); // .getP_Deduction_Acct()
				//Le département
				deductionDist.setC_Activity_ID(mp.getC_Activity_ID());
				
				deductionDist.setOrg_ID( 0 );
				
//	            log.log (Level.WARNING,"P_Payment : " + Payment.getValue() + " Org_ID : " + mp.getOrg_ID() + "Deduction : " + GetDeductionId().intValue() ); 
				
				if ( deductionAccount.getC_SalesRegion_ID() != 0)
					deductionDist.setC_SalesRegion_ID(deductionAccount.getC_SalesRegion_ID());
				else
					deductionDist.setC_SalesRegion_ID(mp.getC_SalesRegion_ID());
				
				//le Gain
				deductionDist.setP_Payment_Gain_ID(mp.getP_Payment_Gain_ID());
				
				//Le numéro de ligne
				//Supposé être le maximum de de ligne pour un paiement + 1
				deductionDist.setLine(mp.getLine());
				
				/****  Eléments spécifiques ****/
				
				deductionDist.setAmount(mp.GetMarginalProfitAmnt());
				deductionDist.setDistribution_Type("A");
				
				deductionDist.save();
				
			}

		}
	}
	public void ResetIteratorPosition(){
		firstRead = true;
		currentPos = -1;
	}
	
	public class MarginalProfit{
		BigDecimal distributionId = null;
		BigDecimal mpAmount = null;
		int Payment_Gain_ID;
		int Payment_ID;
		int P_Occupation_Group_ID;
		int P_Job_Title_ID;
		int C_Activity_ID;
		int C_SalesRegion_ID;
		int Org_ID;
		BigDecimal Line;
		
		MarginalProfit(int _Payment_ID, int _Payment_Gain_ID, int _P_Occupation_Group_ID, int _P_Job_Title_ID, int _C_Activity_ID, BigDecimal _Line, int _C_SalesRegion_ID, int _Org_ID, BigDecimal distId, BigDecimal mpAmt)
		{
			Payment_Gain_ID = _Payment_Gain_ID;
			distributionId = distId;
			mpAmount = mpAmt;
			Payment_ID = _Payment_ID;
			P_Occupation_Group_ID = _P_Occupation_Group_ID;
			P_Job_Title_ID = _P_Job_Title_ID;
			C_Activity_ID = _C_Activity_ID;
			C_SalesRegion_ID = _C_SalesRegion_ID; 
			Org_ID = _Org_ID;
			Line = _Line;
		}
		
		public BigDecimal GetDistributionId(){
			return distributionId;
		}
		
		public BigDecimal GetMarginalProfitAmnt(){
			return mpAmount;
		}
		
		public void AddAmountToMp(BigDecimal amnt){
			mpAmount = mpAmount.add(amnt);
		}
		
		public int getP_Payment_Gain_ID()
		{
			return Payment_Gain_ID;
		}

		public int getP_Payment_ID()
		{
			return Payment_ID;
		}

		public int getP_Occupation_Group_ID()
		{
			return P_Occupation_Group_ID;
		}

		public int getP_Job_Title_ID()
		{
			return P_Job_Title_ID;
		}

		public int getC_Activity_ID()
		{
			return C_Activity_ID;
		}

		public int getC_SalesRegion_ID()
		{
			return C_SalesRegion_ID;
		}

		public int getOrg_ID()
		{
			return Org_ID;
		}

		public BigDecimal getLine()
		{
			return Line;
		}
	}
}
