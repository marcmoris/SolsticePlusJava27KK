/*
 * Created on 2005-02-08
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.math.BigDecimal;
import java.util.Vector;
import org.compiere.util.Env;

import solstice.model.P_Assignment;
import solstice.model.P_Assignment_Param;
import solstice.model.P_Credits;
import solstice.model.P_Credits_Movement;
import solstice.model.P_Employee;
import solstice.model.P_Employee_Credits;
import solstice.model.P_Payment;
import solstice.model.P_Payment_Gain;
import solstice.model.P_Period;
import solstice.model.X_P_Credits_Account;
import solstice.model.X_P_Payment_Gain_Distribution;

/**
 * @author Marc Morissette
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class GainCreditsList
{

    //Le paiement dont dépend les banques
    int paymentId = -1;

    //Position courante de lecture
    int currentPos = -1;

    //Nombre de distribution dans la liste
    int numberOfDist = -1;

    boolean firstRead = true;

    Vector<BigDecimal> paymentCreditsIdList = null;

    Vector<BigDecimal> paymentCreditsAmountList = null;

    Vector<BigDecimal> paymentAccountIdList = null;

    Vector<Vector> provisionList = null;

    GainCreditsList()
    {
        paymentCreditsIdList = new Vector<BigDecimal>();
        paymentCreditsAmountList = new Vector<BigDecimal>();
        paymentAccountIdList = new Vector<BigDecimal>();
        provisionList = new Vector<Vector>();
    }

    public void AddCredits(BigDecimal creditsId, BigDecimal creditsAmnt)
    {

        paymentCreditsIdList.add(creditsId);
        paymentCreditsAmountList.add(creditsAmnt.setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    public void AddCredits(BigDecimal creditsId, BigDecimal creditsAmnt, BigDecimal accountId)
    {

        paymentCreditsIdList.add(creditsId);
        paymentAccountIdList.add(accountId);
        paymentCreditsAmountList.add(creditsAmnt.setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    public boolean Next()
    {

        if (firstRead)
        {
            numberOfDist = paymentCreditsIdList.size();
            firstRead = false;
        }

        if (numberOfDist > 0)
        {
            currentPos = currentPos + 1;
            numberOfDist = numberOfDist - 1;
            return true;
        }
        else
            return false;

    }

    public BigDecimal GetCreditsId()
    {
        return (BigDecimal) paymentCreditsIdList.get(currentPos);
    }

    public BigDecimal GetPaymentAccountId()
    {
        return (BigDecimal) paymentAccountIdList.get(currentPos);
    }

    public BigDecimal GetCreditsAmount()
    {
        return (BigDecimal) paymentCreditsAmountList.get(currentPos);
    }


    public void AddCreditsMP(BigDecimal distributionId, BigDecimal mpAmount)
    {

        //L'ajout se fait à la banque courante et nous devons conserver pour
        //chaque provision, l'Id de la distribution qui a permis de
        // calculer
        //le prorata. Cela servira à l'enregistrement pour surtout conserver le
        // numéro de compte.
        if (provisionList.size() - 1 < currentPos)
            provisionList.add(new Vector());

        ProvisionProfit mp = new ProvisionProfit(distributionId, mpAmount);
        ((Vector) provisionList.get(currentPos)).add(mp);
    }

    
    public BigDecimal GetCreditsMpSum()
    {
        BigDecimal result = new BigDecimal(0);
        Vector mpList = null;
        //Il s'agit de la banque courante
        mpList = ((Vector) provisionList.get(currentPos));
        for (int i = 0; i < mpList.size(); i++)
        {
            result = result.add(((ProvisionProfit) mpList.get(i)).GetProvisionProfitAmnt());
        }

        return result.setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    public void AddAmountToLastMp(BigDecimal amnt)
    {
        Vector mpList = null;
        //Il s'agit de la banque courante
        mpList = ((Vector) provisionList.get(currentPos));
        ((ProvisionProfit) mpList.get(mpList.size() - 1)).AddAmountToMp(amnt);
    }

    
    //Enregistre les BM issus de la banque courante
    public void SaveProvisionProfits( String trxName)
    {
        Vector mpList = null;
        ProvisionProfit mp = null;
        X_P_Payment_Gain_Distribution gainDist = null;
        X_P_Payment_Gain_Distribution creditsDist = null;
        X_P_Credits_Account creditsAccount = null;
        //Il s'agit de la banque courante
        mpList = ((Vector) provisionList.get(currentPos));
        creditsAccount = new X_P_Credits_Account(Env.getCtx(), GetPaymentAccountId().intValue(), trxName);
        for (int i = 0; i < mpList.size(); i++)
        {
            mp = (ProvisionProfit) mpList.get(i);

            //Charger la distribution sur laquelle la provision a été
            // calculé
            gainDist = new X_P_Payment_Gain_Distribution(Env.getCtx(), mp.GetDistributionId().intValue(), trxName);

            //** * Elements communs ** *

            //Provision à enregistrer,
            creditsDist = new X_P_Payment_Gain_Distribution(Env.getCtx(), -1, trxName);

            //Le virement se fait dans le compte de la banque de la table
            // p_credits_account
            creditsDist.setAccount_ID(creditsAccount.getP_Credits_Acct());

            //Le département
            creditsDist.setC_Activity_ID(gainDist.getC_Activity_ID());

            //le Gain
            creditsDist.setP_Payment_Gain_ID(gainDist.getP_Payment_Gain_ID());

            //Le numéro de ligne
            //Supposé être le maximum de de ligne pour un paiement + 1
            creditsDist.setLine(gainDist.getLine());


            creditsDist.setOrg_ID(gainDist.getOrg_ID());
			
			if ( creditsAccount.getC_SalesRegion_ID() != 0)
				creditsDist.setC_SalesRegion_ID(creditsAccount.getC_SalesRegion_ID());
			else
				creditsDist.setC_SalesRegion_ID(gainDist.getC_SalesRegion_ID());

            
            //** ** Eléments spécifiques *** *

            creditsDist.setAmount(mp.GetProvisionProfitAmnt());
            creditsDist.setDistribution_Type("B");

            if ( creditsDist.getAccount_ID() != 0 )
            {
                creditsDist.save();
  /*              
                P_Payment_Gain tmp = P_Payment_Gain.get( Env.getCtx(), gainDist.getP_Payment_Gain_ID(), trxName);
                P_Payment payment = P_Payment.get( Env.getCtx(), tmp.getP_Payment_ID(), trxName);
                P_Period Period = P_Period.get( Env.getCtx(), payment.getP_Period_ID(), trxName);
                
				P_Credits_Movement CreditsMovement = new P_Credits_Movement(Env.getCtx (), -1 , trxName ) ;

				P_Employee_Credits employeeCredits = P_Employee_Credits.get(Env.getCtx (), payment.getP_Employee_ID(), creditsAccount.getP_Credits_ID() , Period.getEndDate() , trxName );
				CreditsMovement.setP_Credits_ID( employeeCredits.getP_Credits_ID());
				CreditsMovement.setP_Employee_ID( employeeCredits.getP_Employee_ID());
				CreditsMovement.setPMovementDate( Period.getEndDate() );
				CreditsMovement.setPMovementOrigin( "FTP");
				CreditsMovement.setPMovementType( "PRD" );
				CreditsMovement.setP_Employee_Credits_ID(employeeCredits.getP_Employee_Credits_ID());
				
				CreditsMovement.setP_Method_Credits_ID( 1000001 );
				CreditsMovement.setDescription( "Provision Neg : " + creditsDist.getAmount()  );
				CreditsMovement.setPMovementVariation( Env.ZERO );
				CreditsMovement.setPMovementHour(  Env.ZERO );
				CreditsMovement.setAmt_Reserve(  creditsDist.getAmount() );
				CreditsMovement.setP_Payment_ID( tmp.getP_Payment_ID() );
				CreditsMovement.save();

 */               
            }
        }
    }


    public void ResetIteratorPosition()
    {
        firstRead = true;
        currentPos = -1;
    }

    public class ProvisionProfit
    {
        BigDecimal distributionId = null;

        BigDecimal mpAmount = null;

        ProvisionProfit(BigDecimal distId, BigDecimal mpAmt)
        {
            distributionId = distId;
            mpAmount = mpAmt;
        }

        public BigDecimal GetDistributionId()
        {
            return distributionId;
        }

        public BigDecimal GetProvisionProfitAmnt()
        {
            return mpAmount;
        }

        public void AddAmountToMp(BigDecimal amnt)
        {
            mpAmount = mpAmount.add(amnt);
        }
    }
}
