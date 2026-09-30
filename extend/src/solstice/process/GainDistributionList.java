/*
 * Created on 2005-02-08
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.process;

import java.math.BigDecimal;
import java.util.Vector;

/**
 * @author frabou01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GainDistributionList {
	
	//Le paiement dont dépend les distributions
	int paymentId = -1;
	
	//Position courante de lecture
	int currentPos = -1;
	
	//Nombre de distribution dans la liste
	int numberOfDist = -1;
	
	boolean firstRead = true;
	
	Vector<BigDecimal> distributionIdList = null;
	Vector<BigDecimal> Payment_Gain_IdList = null;
	Vector<BigDecimal> distributionAmountList = null;
	Vector<BigDecimal> distributionAmountPctList = null;
	Vector<BigDecimal> C_Activity_IdList = null;
	Vector<BigDecimal> C_SalesRegion_IdList = null;
	Vector<BigDecimal> Org_IdList = null;
	Vector<BigDecimal> LineList = null;
	Vector<BigDecimal> P_Job_TitleList = null;
	Vector<BigDecimal> P_Occupation_GroupList = null;
	Vector<BigDecimal> P_PaymentList = null;
	
	GainDistributionList(){
		distributionIdList = new Vector<BigDecimal>();
		Payment_Gain_IdList = new Vector<BigDecimal>();
		distributionAmountList = new Vector<BigDecimal>();
		distributionAmountPctList = new Vector<BigDecimal>();
		C_Activity_IdList =  new Vector<BigDecimal>();
		C_SalesRegion_IdList =  new Vector<BigDecimal>();
		Org_IdList =  new Vector<BigDecimal>();
		LineList =  new Vector<BigDecimal>();
		P_Occupation_GroupList =  new Vector<BigDecimal>();
		P_Job_TitleList =  new Vector<BigDecimal>();
		P_PaymentList =  new Vector<BigDecimal>();

	}
	

	public void AddDistribution(BigDecimal P_Payment_ID, BigDecimal Payment_Gain_Id, BigDecimal C_Activity_Id, BigDecimal P_Occupation_Group_ID, BigDecimal P_Job_Title_ID, BigDecimal C_SalesRegion_ID, BigDecimal Org_ID, BigDecimal Line, BigDecimal distId, BigDecimal distAmnt )
	{
		distributionIdList.add(distId);
		Payment_Gain_IdList.add( Payment_Gain_Id );
		//distributionAmountList.add(distAmnt.setScale(2,BigDecimal.ROUND_HALF_UP));
		distributionAmountList.add(distAmnt.setScale(4,BigDecimal.ROUND_HALF_UP));
		C_Activity_IdList.add(C_Activity_Id);
		C_SalesRegion_IdList.add( C_SalesRegion_ID );
		Org_IdList.add( Org_ID );
		LineList.add(Line);
		P_Occupation_GroupList.add( P_Occupation_Group_ID );
		P_Job_TitleList.add(P_Job_Title_ID);
		P_PaymentList.add(P_Payment_ID);

	}
	
	public boolean Next(){
		
		
		if(firstRead){
			numberOfDist = distributionIdList.size();
			firstRead = false;
			
		}
		
		
		if(numberOfDist > 0){
			currentPos = currentPos + 1;
			numberOfDist = numberOfDist - 1;
			return true;
		}else 
			return false;
			
	}
	
	public BigDecimal GetDistributionId(){
		return (BigDecimal)distributionIdList.get(currentPos);
	}
	public BigDecimal getPayment_Gain_Id(){
		return (BigDecimal)Payment_Gain_IdList.get(currentPos);
	}

	public BigDecimal getC_Activity_ID()
	{
		return (BigDecimal)C_Activity_IdList.get(currentPos);
	}

	public BigDecimal getC_SalesRegion_ID()
	{
		return (BigDecimal)C_SalesRegion_IdList.get(currentPos);
	}

	public BigDecimal getOrg_ID()
	{
		return (BigDecimal)Org_IdList.get(currentPos);
	}

	
	public BigDecimal getLine()
	{
		return (BigDecimal)LineList.get(currentPos);
	}
	
	public BigDecimal getP_Job_Title_ID()
	{
		return (BigDecimal)P_Job_TitleList.get(currentPos);
	}

	public BigDecimal getP_Occupation_Group_ID()
	{
		return (BigDecimal)P_Occupation_GroupList.get( currentPos);
	}
	
	public BigDecimal getP_Payment_ID()
	{
		return (BigDecimal)P_PaymentList.get(currentPos);
	}

	public BigDecimal GetDistributionAmount(){
		return (BigDecimal)distributionAmountList.get(currentPos);
	}
	
	public BigDecimal GetDistributionPct(){
		return (BigDecimal)distributionAmountPctList.get(currentPos);
	}
	
	public void SetDistributionPct(BigDecimal pct){
		distributionAmountPctList.add(pct);
	}
	
	public void ResetIteratorPosition(){
		firstRead = true;
		currentPos = -1;
	}
	
	
	public boolean IsPourcentageValid(){
		BigDecimal tempPct = new BigDecimal(0);
		//Il faut que la somme des prorata donne 1, pour valider la repartition
		for(int i = 0; i < distributionAmountPctList.size();i++){
			tempPct = tempPct.add((BigDecimal)distributionAmountPctList.get(i));
		}
		
		return tempPct.toString().equals("1.0000");
		
		//return tempPct.toString();
	}
	
	public boolean IsEmpty(){
		return !(distributionIdList.size() != 0);
	}
}
