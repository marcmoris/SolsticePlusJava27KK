/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is             Compiere  ERP & CRM Smart Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 * Portions created by Jorg Janke are Copyright (C) 1999-2003 Jorg Janke, parts
 * created by ComPiere are Copyright (C) ComPiere, Inc.;   All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package solstice.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.model.MSequence;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import solstice.model.P_Deduction_Param;
import solstice.model.P_Employee;

import java.util.logging.*;

import org.compiere.util.*;

/**
 *  Employee_Deduction Model
 *
 *  @author Jorg Janke
 *  @version $Id: P_Employee_Deduction.java,v 1.9 2008/06/26 11:46:42 marmor01 Exp $
 */
public class P_Employee_Deduction extends X_P_Employee_Deduction
{

	/**
	 * 	Get Employee_Deduction
	 *	@param ctx context
	 * 	@param P_Employee_Deduction_ID id
	 *	@return Employee_Deduction
	 */

	
	public static P_Employee_Deduction get (Properties ctx, int P_Employee_Deduction_ID, String trxName )
	{
		Integer key = new Integer (P_Employee_Deduction_ID);
		P_Employee_Deduction employee_Deduction = (P_Employee_Deduction)s_cache.get(key);
		if (employee_Deduction != null)
			return employee_Deduction;
		employee_Deduction = new P_Employee_Deduction (ctx, P_Employee_Deduction_ID, trxName);
		s_cache.put (key, employee_Deduction);
		if(employee_Deduction != null)
		{
			employee_Deduction.setTrxName(trxName);
		}
		return employee_Deduction;
	}	//	get

	public static P_Employee_Deduction get (Properties ctx, int EmployeeID, int DeductionID, Timestamp EffectIn, String trxName)
	{
	    String sql = "SELECT  P_Employee_Deduction_ID FROM P_Employee_Deduction "
            + " WHERE P_Employee_ID   = " + EmployeeID
            + "   AND P_Deduction_ID  = " + DeductionID
		 	+ "    and EffectIn <= " + DB.TO_DATE( EffectIn )  
		 	+ "  Order By EffectIn Desc";
	    
	    PreparedStatement pstmt = null;
    
	    int P_Employee_Deduction_ID = -1;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
			    P_Employee_Deduction_ID = rs.getInt("P_Employee_Deduction_ID");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
		    System.out.println( "P_Employee_Deduction, get - " + e);
		    return null;
		}
	    
		P_Employee_Deduction Employee_Deduction;
		if ( P_Employee_Deduction_ID == -1 )
			Employee_Deduction = new P_Employee_Deduction( ctx, -1, trxName );
		else
			Employee_Deduction = new P_Employee_Deduction( ctx, P_Employee_Deduction_ID, trxName );

		return Employee_Deduction;
	}	//	get

	
	/**	Cache						*/
	private static CCache<Integer,P_Employee_Deduction>	s_cache = new CCache<Integer,P_Employee_Deduction>("P_Employee_Deduction", 20);

	/**	Logger							*/
	private static CLogger		s_log = CLogger.getCLogger (P_Employee_Deduction.class);
	
	private String				m_trxName = null;
	
	/**************************************************************************
	 * 	Standard Constructor
	 * 	@param ctx context
	 * 	@param P_Employee_Deduction_ID id
	 */
	public P_Employee_Deduction (Properties ctx, int P_Employee_Deduction_ID, String trxName)
	{
		super ( ctx , P_Employee_Deduction_ID, trxName);
		if (P_Employee_Deduction_ID == 0)
		{
			setAD_Org_ID(0);
		}

		m_trxName = trxName;
	}	//	P_Employee_Deduction

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public P_Employee_Deduction (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
		m_trxName = trxName;
	}
	
	/**
	 * 	Simplified Constructor
	 * 	@param ctx context
	 */
	public P_Employee_Deduction (Properties ctx, String trxName)
	{
		this (ctx, Env.getContextAsInt(ctx, "#P_Employee_Deduction_ID"), trxName);
		m_trxName = trxName;
	}	//	P_Employee_Deduction


 public BigDecimal getEmployeeAmount( Timestamp EffectIn ) 
 {
   
    BigDecimal mnt = super.getEmployeeAmount();
    
 	if ( mnt == null || mnt.equals( Env.ZERO) )
 	{
 	 	P_Deduction_Param DeductionParam = P_Deduction_Param.get( getCtx(), this.getP_Deduction_ID(), EffectIn, this.get_TrxName() ); 
 		return DeductionParam.getEmployeeAmount();
 	}
 	return mnt;
 }
 
 public BigDecimal getEmployerAmount( Timestamp EffectIn ) 
 {
    BigDecimal mnt = super.getEmployerAmount();

 	if ( mnt == null || mnt.compareTo( Env.ZERO) == 0)
 	{
 	 	P_Deduction_Param DeductionParam = P_Deduction_Param.get( getCtx(), this.getP_Deduction_ID(), EffectIn, this.get_TrxName() ); 
 		return DeductionParam.getEmployerAmount();
 	}
 	return mnt;
 }
 
 public BigDecimal getEmployerRate( Timestamp EffectIn ) 
 {
    BigDecimal rate = super.getEmployerRate();

 	if ( rate == null || rate.compareTo( Env.ZERO) == 0 )
 	{
	 	P_Deduction_Param DeductionParam = P_Deduction_Param.get( getCtx(), this.getP_Deduction_ID(), EffectIn, this.get_TrxName() ); 
 		return DeductionParam.getEmployerRate();
 	}
 	return rate;
 }
 
 public BigDecimal getEmployeeRate( Timestamp EffectIn ) 
 {
    BigDecimal rate = super.getEmployeeRate();

    if ( rate == null || rate.compareTo( Env.ZERO) == 0 )
 	{
	 	P_Deduction_Param DeductionParam = P_Deduction_Param.get( getCtx(), this.getP_Deduction_ID(), EffectIn, this.get_TrxName() ); 
 		return DeductionParam.getEmployeeRate();
 	}
 	return rate;
 }

 public BigDecimal getExemptionAmount( Timestamp EffectIn ) 
 {
    BigDecimal rate = super.getExemptionAmount();

    if ( rate == null || rate.compareTo( Env.ZERO) == 0 )
 	{
	 	P_Deduction_Param DeductionParam = P_Deduction_Param.get( getCtx(), this.getP_Deduction_ID(), EffectIn, this.get_TrxName() ); 
 		return DeductionParam.getExemptionAmount();
 	}
 	return rate;
 }

 public BigDecimal getExemptionAmountPeriod( P_Deduction_Param DeductionParam, int NumberOfPeriod)
 {
 	BigDecimal NumberPeriod = new BigDecimal(NumberOfPeriod);
 	BigDecimal ExemptionAmount = super.getExemptionAmount();
 	if ( ExemptionAmount == null || ExemptionAmount.compareTo( Env.ZERO ) == 0 )
 	{
 		ExemptionAmount = DeductionParam.getExemptionAmount();
 		if (ExemptionAmount == null || ExemptionAmount.compareTo( Env.ZERO ) == 0)
 		{
 			ExemptionAmount = Env.ZERO;
 			return ExemptionAmount;
 		}
        else
        {
        	if (DeductionParam.getExemptionType().equals(P_Deduction_Param.EXEMPTIONTYPE_Annual) )
        		return ExemptionAmount.divide( NumberPeriod, 8, BigDecimal.ROUND_HALF_UP);
        }
 	}
 	else
 	{
 		if ( this.getExemptionType().equals(P_Employee_Deduction.EXEMPTIONTYPE_Annual) )
     		return ExemptionAmount.divide( NumberPeriod , 8,BigDecimal.ROUND_HALF_UP);
 	}
 	return ExemptionAmount;
 }
 
 public BigDecimal getEmployeeAmount( P_Deduction_Param DeductionParam ) 
 {
   
    BigDecimal mnt = super.getEmployeeAmount();
    
 	if ( mnt == null || mnt.compareTo( Env.ZERO) == 0 )
 	{
 		return DeductionParam.getEmployeeAmount();
 	}
 	return mnt;
 }
 
 public BigDecimal getEmployerAmount( P_Deduction_Param DeductionParam ) 
 {
    BigDecimal mnt = super.getEmployerAmount();

 	if ( mnt == null || mnt.compareTo( Env.ZERO) == 0)
 	{
 		return DeductionParam.getEmployerAmount();
 	}
 	return mnt;
 }

 public BigDecimal getEmployerAmount( P_Deduction_Param DeductionParam, int P_Period_ID ) 
 {
    BigDecimal mnt = super.getEmployerAmount();

	P_Employee_Deduction_Excep deductionExcep = P_Employee_Deduction_Excep.get( Env.getCtx (),  this.getP_Employee_Deduction_ID(), P_Period_ID, this.get_TrxName() );

	if ( deductionExcep != null && deductionExcep.getExceptionAmountEmployer().compareTo(Env.ZERO ) != 0 )
	{
		return deductionExcep.getExceptionAmountEmployer();
	}
		
 	if ( mnt == null || mnt.compareTo( Env.ZERO) == 0)
 	{
 		return DeductionParam.getEmployerAmount();
 	}
 	return mnt;
 }

 public BigDecimal getEmployerRate( P_Deduction_Param DeductionParam ) 
 {
    BigDecimal rate = super.getEmployerRate();

 	if ( rate == null || rate.compareTo( Env.ZERO) == 0 )
 	{
 		return DeductionParam.getEmployerRate();
 	}
 	return rate;
 }
 
 public BigDecimal getEmployeeRate( P_Deduction_Param DeductionParam ) 
 {
    BigDecimal rate = super.getEmployeeRate();

    if ( rate == null || rate.compareTo( Env.ZERO) == 0 )
 	{
 		return DeductionParam.getEmployeeRate();
 	}
 	return rate;
 }

 public BigDecimal getExemptionAmount( P_Deduction_Param DeductionParam ) 
 {
    BigDecimal rate = super.getExemptionAmount();

    if ( rate == null || rate.compareTo( Env.ZERO) == 0 )
 	{
 		return DeductionParam.getExemptionAmount();
 	}
 	return rate;
 }

 
 public BigDecimal getNonAnnualMaximum( P_Deduction_Param DeductionParam ) 
 {
    BigDecimal nonAnnualMaximum = super.getNonAnnualMaximum();

    if ( nonAnnualMaximum == null || nonAnnualMaximum.compareTo( Env.ZERO) == 0 )
 	{
 		return DeductionParam.getNonAnnualMaximum();
 	}
 	return nonAnnualMaximum;
 }

 // TODO renomer le champs MaxYearly a AnnualMaximum ou inversement.
 //
 public BigDecimal getAnnualMaximum( P_Deduction_Param DeductionParam ) 
 {
    BigDecimal AnnualMaximum = super.getMaxYearly();

    if ( AnnualMaximum == null || AnnualMaximum.compareTo( Env.ZERO) == 0 )
 	{
 		return DeductionParam.getAnnualMaximum();
 	}
 	return AnnualMaximum;
 }

 public BigDecimal getAnnualMaximumYeur( P_Deduction_Param DeductionParam ) 
 {
    BigDecimal AnnualMaximum = super.getMaxYearlyYeur();

    if ( AnnualMaximum == null || AnnualMaximum.compareTo( Env.ZERO) == 0 )
 	{
 		return DeductionParam.getAnnualMaximumEmployer();
 	}
 	return AnnualMaximum;
 }


 public BigDecimal getBackPaymentPeriod( P_Deduction_Param DeductionParam ) 
 {
	    BigDecimal BackPaymentPeriod = null;
	    
	    if ( this.getMethodRecoveryBackPayment() != null && this.getMethodRecoveryBackPayment().equals(P_Employee_Deduction.METHODRECOVERYBACKPAYMENT_FixAmount)) 
	    	BackPaymentPeriod = super.getBackPaymentPeriod();

	    if ( BackPaymentPeriod == null || BackPaymentPeriod.compareTo( Env.ZERO) == 0 )
	 	{
	 		return DeductionParam.getBackPaymentPeriod();
	 	}
	 	return BackPaymentPeriod;
	 }

 public BigDecimal getBackPaymentRate( P_Deduction_Param DeductionParam ) 
 {
	    BigDecimal BackPaymentRate = null;
	    
	    if ( this.getMethodRecoveryBackPayment() != null && this.getMethodRecoveryBackPayment().equals(P_Employee_Deduction.METHODRECOVERYBACKPAYMENT_Percentage)) 
	    	BackPaymentRate = super.getBackPaymentRate();

	    if ( BackPaymentRate == null || BackPaymentRate.compareTo( Env.ZERO) == 0 )
	 	{
	 		return DeductionParam.getBackPaymentRate();
	 	}
	 	return BackPaymentRate;
	 }

 public String getMethodRecoveryBackPayment( P_Deduction_Param DeductionParam ) 
 {
	    String  MethodRecoveryBackPayment = super.getMethodRecoveryBackPayment();

	    if ( MethodRecoveryBackPayment == null  )
	 	{
	 		return DeductionParam.getMethodRecoveryBackPayment();
	 	}
	 	return MethodRecoveryBackPayment;
	 }
 
 
public BigDecimal getToRetrieveAmount(  P_Deduction_Param DeductionParam, BigDecimal employeeAmount )
{
	BigDecimal RetrievalAmount = new BigDecimal(0);
	if ( this.getMethodRecoveryBackPayment( DeductionParam ) == null )
	{
		return RetrievalAmount;
	}

	
	String sql = null;
	sql = "Select isnull( Sum( isnull( AccumulationAmount,0) - isnull( RetrievalAmount, 0)  ) , 0) "; 
	sql += " From P_Payment_Deduction WHERE P_Deduction_ID = " + this.getP_Deduction_ID();
	sql += " AND P_Employee_ID=" + this.getP_Employee_ID();
//	sql += " AND P_Year_ID=" +  year ;

	PreparedStatement pstmt = null;

	try
	{
		pstmt = DB.prepareStatement (sql, this.get_TrxName());
		ResultSet rs = pstmt.executeQuery ();
		if ( rs.next () )
			RetrievalAmount = rs.getBigDecimal(1);
		else
			RetrievalAmount = new BigDecimal(0);
		rs.close ();
		pstmt.close ();
		pstmt = null;
	}
	catch (Exception e)
	{
		s_log.log(Level.SEVERE,"P_Employee_Deduction - " + sql, e);
	}
	
	//8 june 2008
	if ( RetrievalAmount.compareTo(Env.ZERO) == 0 ) return Env.ZERO;

	BigDecimal MaximumAmount = Env.ZERO;

	if ( this.getMethodRecoveryBackPayment( DeductionParam ).equals(P_Employee_Deduction.METHODRECOVERYBACKPAYMENT_DoubleOfTheDeduction) )
	{
		MaximumAmount = employeeAmount; //.multiply( new BigDecimal( 2 ));
	}

	if ( this.getMethodRecoveryBackPayment( DeductionParam ).equals(P_Employee_Deduction.METHODRECOVERYBACKPAYMENT_FixAmount) )
	{
		MaximumAmount = this.getBackPaymentPeriod( DeductionParam );
	}

	if ( this.getMethodRecoveryBackPayment( DeductionParam ).equals(P_Employee_Deduction.METHODRECOVERYBACKPAYMENT_Percentage) )
	{
		MaximumAmount = RetrievalAmount.multiply( this.getBackPaymentRate( DeductionParam ).divide(new BigDecimal(100 ))  );
	}

	if ( this.getMethodRecoveryBackPayment( DeductionParam ).equals(P_Employee_Deduction.METHODRECOVERYBACKPAYMENT_Total) )
	{
		MaximumAmount = Env.ZERO;
	}

	if ( MaximumAmount.compareTo( Env.ZERO) != 0 &&  RetrievalAmount.compareTo( MaximumAmount ) > 0 )
	{
		RetrievalAmount = MaximumAmount;
	}

	// 2024-05-14
	if ( MaximumAmount.compareTo( Env.ZERO) != 0 &&  MaximumAmount.compareTo(Env.ZERO) < 0  && RetrievalAmount .compareTo(Env.ZERO) != 0 )
	{
		RetrievalAmount = MaximumAmount;
	}

	
	//2009.02.25 on ne peux pas avoir une récupération négative.
	if ( RetrievalAmount.compareTo( Env.ZERO) < 0 )
		return Env.ZERO;
	//2009.02.25


	if ( RetrievalAmount == null ) return Env.ZERO;
	return RetrievalAmount;
}

//DEMO
public BigDecimal getToRetrieveAmountEmployer(  P_Deduction_Param DeductionParam, BigDecimal employerAmount )
{
	BigDecimal RetrievalAmount = new BigDecimal(0);
	if ( this.getMethodRecoveryBackPayment( DeductionParam ) == null )
	{
		return RetrievalAmount;
	}

	
	String sql = null;
	sql = "Select isnull( Sum( isnull( AccumulationAmountEmployer,0) - isnull( RetrievalAmountEmployer, 0)  ) , 0) "; 
	sql += " From P_Payment_Deduction WHERE P_Deduction_ID = " + this.getP_Deduction_ID();
	sql += " AND P_Employee_ID=" + this.getP_Employee_ID();
//	sql += " AND P_Year_ID=" +  year ;

	PreparedStatement pstmt = null;

	try
	{
		pstmt = DB.prepareStatement (sql, this.get_TrxName());
		ResultSet rs = pstmt.executeQuery ();
		if ( rs.next () )
			RetrievalAmount = rs.getBigDecimal(1);
		else
			RetrievalAmount = new BigDecimal(0);
		rs.close ();
		pstmt.close ();
		pstmt = null;
	}
	catch (Exception e)
	{
		s_log.log(Level.SEVERE,"P_Employee_Deduction -  - " + sql, e);
	}
	

	if ( RetrievalAmount == null ) return Env.ZERO;
	return RetrievalAmount;
}



/**
 * 	Before Save
 *	@param newRecord new
 *	@return true or false
 */

protected boolean beforeSave (boolean newRecord)
{
	//	Employee

    P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
	P_Deduction_Param DeductionParam = P_Deduction_Param.get( getCtx(), this.getP_Deduction_ID(), this.getEffectIn(), this.get_TrxName() ); 

	//+ 2011.07.05 + sécurité par compagnie.
	this.setAD_Org_ID( Employee.getAD_Org_ID());
	//- 2011.07.05 + sécurité par compagnie.

	if ( newRecord && DeductionParam != null && DeductionParam.getP_Employer_ID() != 0 && Employee.getP_Employer_ID() != DeductionParam.getP_Employer_ID() )
	{
		log.saveError("ValidationError","L'employé n'a pas le même numéro d'employeur que la déduction");
		s_log.log(Level.SEVERE,"ValidationError", "L'employé n'a pas le même numéro d'employeur que la déduction");
		return false;
	}
	
	return true;
}	//	beforeSave


	protected boolean afterSave (boolean newRecord, boolean success)
	{
	    
	    if (!success )
		{
			return success;
		}
	
	    //
	    // Si la déduction est un avantage imposable alors on créer automatiquement 
	    // l'avantage imposable dans le dossier de l'employé, on ne prend pas en considération la date d'entré en vigeur
	    // si l'avantage existe déjà ( car plusiers déduction peuvent référer le même avantage.
	    //
	    if ( newRecord )
	    {
	    	P_Deduction Deduction = P_Deduction.get( Env.getCtx(), this.getP_Deduction_ID(), this.get_TrxName());
	    	if ( Deduction.getP_Taxable_Benefit_ID() != 0 && this.isActive()  )
	    	{
		    	P_Employee_Taxable_Benefit EmployeeTaxableBenefit = P_Employee_Taxable_Benefit.get( Env.getCtx(), this.getP_Employee_ID(), Deduction.getP_Taxable_Benefit_ID(), this.get_TrxName());
		    	EmployeeTaxableBenefit.setAD_Org_ID(0);
		    	EmployeeTaxableBenefit.setP_Employee_ID(this.getP_Employee_ID());
		    	EmployeeTaxableBenefit.setP_Taxable_Benefit_ID( Deduction.getP_Taxable_Benefit_ID());
		    	if ( EmployeeTaxableBenefit.getEffectIn() == null )
		    		EmployeeTaxableBenefit.setEffectIn( this.getEffectIn());
		    	// 2008-08-08 
		    	if ( EmployeeTaxableBenefit.getEffectTo() != null )
		    		EmployeeTaxableBenefit.setEffectTo( null );
		    	EmployeeTaxableBenefit.setIsActive( this.isActive());
		    	EmployeeTaxableBenefit.save();
	    	}

	    	if ( Deduction.getP_Taxable_Benefit_Provincial_ID() != 0 && this.isActive()  )
	    	{
				P_Employee Employee = P_Employee.get( Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName() );
	    		P_Taxable_Benefit TaxableBenefit = P_Taxable_Benefit.get( Env.getCtx(), Deduction.getP_Taxable_Benefit_Provincial_ID(), this.get_TrxName() );

	    		//
	    		// Ne créer pas les avantage imposable d'assurance dans une région de résidence autre de celle de l'employé.
	    		//
	    		boolean create = true;
	    		if ( Deduction.isBaseInsurance() && TaxableBenefit.getC_Region_ID() != 0 && TaxableBenefit.getC_Region_ID() != Employee.getRegionResidence().getC_Region_ID() )
	    		{
	    		  create = false;	
	    		}

	    		if ( create )
	    		{
			    	P_Employee_Taxable_Benefit EmployeeTaxableBenefit = P_Employee_Taxable_Benefit.get( Env.getCtx(), this.getP_Employee_ID(), Deduction.getP_Taxable_Benefit_Provincial_ID(), this.get_TrxName());
			    	EmployeeTaxableBenefit.setAD_Org_ID(0);
			    	EmployeeTaxableBenefit.setP_Employee_ID(this.getP_Employee_ID());
			    	EmployeeTaxableBenefit.setP_Taxable_Benefit_ID( Deduction.getP_Taxable_Benefit_Provincial_ID());
			    	if ( EmployeeTaxableBenefit.getEffectIn() == null )
			    		EmployeeTaxableBenefit.setEffectIn( this.getEffectIn());
			    	// 2008-08-08 
			    	if ( EmployeeTaxableBenefit.getEffectTo() != null )
			    		EmployeeTaxableBenefit.setEffectTo( null );
			    	EmployeeTaxableBenefit.setIsActive( this.isActive());
			    	EmployeeTaxableBenefit.save();
	    			
	    		}
	    	}

	    }
	    
	    //on va chercher l'objet precedent pour mettre sa date de fin a jour
	    String sql = "SELECT P_Employee_Deduction_ID "
	    	+ " FROM P_Employee_Deduction "
	    	+ " WHERE P_Employee_ID = " + this.getP_Employee_ID()
	    	+ " AND P_Deduction_ID = " + this.getP_Deduction_ID()
	    	+ " AND EffectIn < " + DB.TO_DATE( this.getEffectIn() )
	    	+ " AND EffectTo IS NULL "
	    	+ " ORDER BY EffectIn Desc ";
	    PreparedStatement pstmt = DB.prepareStatement(sql, null);

	    try
	    {
		    ResultSet rs = pstmt.executeQuery();
		    if(rs.next())
		    {
		    	//si on a un objet, on set sa date de fin a la veille de la date de debut du nouvelle objet
		    	P_Employee_Deduction employee_Deduction = P_Employee_Deduction.get(Env.getCtx(), rs.getInt(1), m_trxName);
		    	if(employee_Deduction == null || employee_Deduction.getP_Employee_Deduction_ID() <= 0)
		    	{
		    		return success;
		    	}
		    	Timestamp tsDate = this.getEffectIn();
		    	// - 1 pour la veille
		    	tsDate = TimeUtil.addDays( tsDate, -1);
		    	employee_Deduction.setEffectTo(tsDate);
		    	employee_Deduction.save();
		    }
	    }
	    catch(SQLException e)
	    {
	    	s_log.log(Level.SEVERE,"P_Employee_Deduction - afterSave - " + sql, e);	    	
	    }
	    
		//
		// M.a.j de la Sequence de RV_P_Employee_Deduction_2.
		//
		MSequence msequence_cur = MSequence.get(Env.getCtx(), "P_Employee_Deduction");
		MSequence msequence = MSequence.get(Env.getCtx(), "RV_P_Employee_Deduction_2");
		if ( msequence_cur.getCurrentNext() > msequence.getCurrentNext())
		{
			msequence.setCurrentNext( msequence_cur.getCurrentNext() );
			msequence.save();
		}
		//

	    
		return true;
	}  //	afterSave	


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		P_Employee Employee = P_Employee.get( getCtx(), this.getP_Employee_ID(), this.get_TrxName() ); 
		P_Deduction Deduction = P_Deduction.get( getCtx(), this.getP_Deduction_ID(), this.get_TrxName() ); 

		StringBuffer sb = new StringBuffer ("P_Employee_Deduction[ID=")
			.append( this.getP_Employee_Deduction_ID())
			.append(",Employee Value=").append(Employee.getValue())
			.append(",Name=").append(Employee.getName())
			.append(",Deduction Value=").append(Deduction.getValue())
			.append(",Name=").append(Deduction.getName())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	public int getAge( Timestamp d ) 
	{
		
		if ( this.getBirthDate() == null )
		{
			P_Employee Employee = P_Employee.get(Env.getCtx(), this.getP_Employee_ID(), this.get_TrxName());
			return Employee.getAge(d);
		}
		
		Timestamp tt2 = this.getBirthDate();

		String t1 = d.toString();
		String t2 = tt2.toString();

		t1 = t1.substring(0,4) + t1.substring(5,7) + t1.substring(8,10);
		t2 = t2.substring(0,4) + t2.substring(5,7) + t2.substring(8,10);
			
		BigInteger endDate   = new BigInteger( t1 );
		BigInteger birthDate = new BigInteger( t2 );

		//+ 2009.09.05 - age au 01 du mois suivant sauf le l'employé est née le 1ier du mois.
		if ( TimeUtil.getDay_Of_Month(tt2) != 01 && TimeUtil.getMonth( d ) == TimeUtil.getMonth( tt2 ) )
		{
			return ( endDate.subtract( birthDate ).divide( new BigInteger( "10000" )).intValue() ) - 1;
		}
		//-

		return ( endDate.subtract( birthDate ).divide( new BigInteger( "10000" )).intValue() );
	}


//	 
//	 
	private void setTrxName ( String trxName)
	{
		m_trxName = trxName;
	}
}	//	P_Employee_Deduction
